package co.tpg.workflow.function;

import co.tpg.workflow.dao.DAO;
import co.tpg.workflow.dao.StepDAO;
import co.tpg.workflow.dao.StepFieldDAO;
import co.tpg.workflow.dao.exception.BackendException;
import co.tpg.workflow.exception.ProcessingException;
import co.tpg.workflow.model.Step;
import co.tpg.workflow.model.StepField;
import co.tpg.workflow.request.HttpMethod;
import co.tpg.workflow.request.StepRequest;
import co.tpg.workflow.response.AbstractResponse;
import co.tpg.workflow.response.ErrorResponse;
import co.tpg.workflow.response.StepListResponse;
import co.tpg.workflow.response.StepResponse;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Handler for requests to Workflow step Lambda function.
 * @author Andrej
 * @since 2019-10-09
 */
public class StepFunction implements RequestHandler<StepRequest, AbstractResponse> {

    @Override
    public AbstractResponse handleRequest(final StepRequest stepRequest, final Context context) {

        final String DYNAMO_TABLE_NAME = "Step";
        final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
        final StepResponse response = new StepResponse();
        final ErrorResponse errorResponse = new ErrorResponse();
        final StepListResponse stepListResponse = new StepListResponse();
        final Map<String, String> headers = new HashMap<>();
        final DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        final StepDAO stepDAO = new StepDAO();
        final StepFieldDAO stepFieldDAO = new StepFieldDAO();

        Step step = null;
        String id;
        LambdaLogger logger = context.getLogger();          // Get logger
        String httpMethod = stepRequest.getHttpMethod();    // get http method

        // Set headers
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);

        // Set status code and log method
        errorResponse.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        logger.log(String.format("HTTP method: %s\n",stepRequest.getHttpMethod()));

        // get step ID and check its existence
        id = stepRequest.getBody().getId();
        if ( id == null ) {
            if ( !((httpMethod.equals(HttpMethod.POST) ) || (httpMethod.equals(HttpMethod.GET)) ) ) { // in case of post id does not exists yet
                errorResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                errorResponse.setBody(ProcessingException.builder().message("Invalid path parameter <id>.").build());
                return errorResponse;
            }
        } else { // check if step exists

            step = mapper.load(Step.class, id);
            if (step == null) { // step was not found - error
                errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                errorResponse.setBody(ProcessingException.builder().message(String.format("Workflow step with ID=%s was not found.", id)).build());
                return errorResponse;
            }
        }

        // Execute rest operation (CRUD)
        switch (stepRequest.getHttpMethod()) {

            case HttpMethod.GET: {
                // log operation
                logger.log(String.format("GET id=%s body=%s\n", id, stepRequest.getBody()));

                if (id == null) { // get paginated result
                    try {
                        final String lastId = stepRequest.getQueryStringParameters().get("lastId");

                        // TODO -> validate this approach
                        List<Step> steps = stepDAO.retrieveAll(lastId, DAO.PAGE_SIZE);
                        if (steps != null) {
                            for (Step localStep : steps ) {
                                try {
                                    localStep.setSteps(stepFieldDAO.retrieveByParentId(localStep.getId()));
                                } catch (BackendException exc ) {
                                    logger.log(exc.getMessage());
                                }
                            }
                        }

                        stepListResponse.setBody(steps);
                        stepListResponse.setStatusCode(HttpServletResponse.SC_OK);
                    } catch (BackendException e) {
                        errorResponse.setBody(ProcessingException.builder().message(String.format(e.getMessage())).build());
                        return errorResponse;
                    }

                    return stepListResponse;
                } else { // retrieve by id
                    try {

                        step = stepDAO.retrieveById(id);

                        // TODO -> validate this approach
                        try {
                            List<StepField> stepFields = stepFieldDAO.retrieveByParentId(id);
                            if ( stepFields != null ) {
                                step.setSteps(stepFields);
                            }
                        } catch (BackendException exc ) {
                            logger.log(exc.getMessage());
                        }

                        response.setBody(step);

                    } catch (ResourceNotFoundException ex) {
                        errorResponse.setBody(ProcessingException.builder().message(String.format("The table named %s could not be found in the backend system.",DYNAMO_TABLE_NAME)).build());
                        return errorResponse;
                    } catch (AmazonServiceException ex) {
                        errorResponse.setBody(ProcessingException.builder().message(ex.getMessage()).build());
                        return errorResponse;
                    } catch (BackendException ex) {
                        errorResponse.setBody(ProcessingException.builder().message(String.format(ex.getMessage())).build());
                        return errorResponse;
                    }
                }
                break;
            }

            case HttpMethod.POST: {

                // log operation
                logger.log(String.format("POST body=%s\n", stepRequest.getBody()));

                try {
                    // generate UUID and set it to id
                    step = stepRequest.getBody();
                    step.setId(UUID.randomUUID().toString());

                    try {
                        // save to DB + set response
                        stepDAO.create(step);

                        // TODO -> validate this approach
                        ArrayList<StepField> stepFields = step.getSteps();
                        if (stepFields != null ) {
                            for (StepField stepField: stepFields ) {
                                try {
                                    stepFieldDAO.create(stepField);
                                } catch (BackendException exc ) {
                                    logger.log(exc.getMessage());
                                }
                            }
                        }

                        mapper.save(step);
                        response.setBody(step);
                        response.setStatusCode(HttpServletResponse.SC_CREATED);
                    } catch (BackendException exc) {
                        errorResponse.setBody(ProcessingException.builder().message(String.format(exc.getMessage())).build());
                        return errorResponse;
                    }
                } catch (ResourceNotFoundException ex) {
                    errorResponse.setBody(ProcessingException.builder().message(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME)).build());
                    return errorResponse;
                } catch (AmazonServiceException ex) {
                    errorResponse.setBody(ProcessingException.builder().message(ex.getMessage()).build());
                    return errorResponse;
                }
                break;
            }

            case HttpMethod.PUT:
            case HttpMethod.PATCH: {

                // log operation
                logger.log(String.format("PUT/PATCH id=%s body=%s\n", id, stepRequest.getBody()));

            try {
                step = stepDAO.retrieveById(id); // if it does not exists, exception is thrown
                stepDAO.update(stepRequest.getBody());

                // TODO -> validate this approach
                List<StepField> stepFields = step.getSteps();
                if (stepFields != null ) {
                    for (StepField stepField: stepFields) {
                        try {
                            stepFieldDAO.update(stepField);
                        } catch (BackendException exc ) {
                            logger.log(exc.getMessage());
                        }
                    }
                }

                mapper.save(stepRequest.getBody());
                response.setBody(stepRequest.getBody() );
                response.setStatusCode(HttpServletResponse.SC_OK);
            } catch (AmazonServiceException ex) {
                errorResponse.setBody(ProcessingException.builder().message(ex.getMessage()).build());
                return errorResponse;
            } catch (BackendException e) {
                errorResponse.setBody(ProcessingException.builder().message(String.format(e.getMessage())).build());
                return errorResponse;
            }
                break;
            }
            // Delete step - using ID
            case HttpMethod.DELETE: {

                // log operation
                logger.log(String.format("DELETE id=%s \n", id, stepRequest.getBody()));

                try {
                    // delete workflow step
                    step = stepDAO.retrieveById(id);

                    // TODO -> validate this approach
                    List<StepField> stepFields = step.getSteps();
                    if (stepFields != null ) {
                        for (StepField stepField: stepFields ) {
                            try {
                                stepFieldDAO.delete(stepField);
                            } catch (BackendException exc ) {
                                logger.log(exc.getMessage());
                            }
                        }
                    }

                    stepDAO.delete(step);
                    mapper.delete(step);
                    response.setStatusCode(HttpServletResponse.SC_OK);

                } catch (ResourceNotFoundException ex) {
                    errorResponse.setBody(ProcessingException.builder().message(String.format("The table named %s could not be found in the backend system.",DYNAMO_TABLE_NAME)).build());
                    return errorResponse;
                } catch (AmazonServiceException ex) {
                    errorResponse.setBody(ProcessingException.builder().message(ex.getMessage()).build());
                    return errorResponse;
                } catch (BackendException e) {
                    errorResponse.setBody(ProcessingException.builder().message(String.format(e.getMessage())).build());
                    return errorResponse;
                }
                break;
            }
            default:
                // set default servlet response to BAD_REQUEST
                response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            }

        return response;
    }
}
