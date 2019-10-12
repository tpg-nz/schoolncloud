package co.tpg.workflow.function;

import co.tpg.workflow.dao.DAO;
import co.tpg.workflow.dao.StepDAO;
import co.tpg.workflow.dao.exception.BackendException;
import co.tpg.workflow.function.exception.ProcessingException;
import co.tpg.workflow.function.model.Step;
import co.tpg.workflow.function.request.HttpMethod;
import co.tpg.workflow.function.request.StepRequest;
import co.tpg.workflow.function.response.AbstractResponse;
import co.tpg.workflow.function.response.ErrorResponse;
import co.tpg.workflow.function.response.StepListResponse;
import co.tpg.workflow.function.response.StepResponse;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Lambda function handler for workflow step requests
 * @author Andrej
 * @since 2019-10-09
 */
public class StepFunction implements RequestHandler<StepRequest, AbstractResponse> {
    /**
     * Lambda function handler
     * @param stepRequest   The lambda request object
     * @param context       The lambda runtime request context
     * @return              The response of the rest operation
     */
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

        Step step;
        LambdaLogger logger = context.getLogger();          // Get logger
        String httpMethod = stepRequest.getHttpMethod();    // get http method

        // Set headers
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);

        // Set status code and log method
        errorResponse.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        logger.log(String.format("HTTP method: %s\n",stepRequest.getHttpMethod()));

        // get step ID and check its existence
        String id = stepRequest.getBody().getId();
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

        // Execute rest operation
        switch (httpMethod) {

            case HttpMethod.GET: {

                // log operation
                logger.log(String.format("GET id=%s body=%s\n", id, stepRequest.getBody()));

                if (id == null) { // get paginated result
                    try {
                        final String lastId = stepRequest.getQueryStringParameters().get("lastId");
                        //List<Step> steps = stepDAO.retrieveAll(lastId, DAO.PAGE_SIZE);
                        stepListResponse.setBody(stepDAO.retrieveAll(lastId, DAO.PAGE_SIZE));
                        stepListResponse.setStatusCode(HttpServletResponse.SC_OK);
                    } catch (BackendException e) {
                        errorResponse.setBody(ProcessingException.builder().message(String.format(e.getMessage())).build());
                        return errorResponse;
                    }
                    return stepListResponse;
                } else {
                    try { // retrieve by id
                        //step = stepDAO.retrieveById(id);
                        response.setBody(stepDAO.retrieveById(id));
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
                    // generate UUID
                    step = stepRequest.getBody();
                    step.setId(UUID.randomUUID().toString());

                    try {
                        // create new step in DB with dep. nodes + set response
                        stepDAO.create(step);
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
                // check if step exists
                step = stepDAO.retrieveById(id); // if it does not exists, exception is thrown
                // update the step and the dependant nodes
                stepDAO.update(stepRequest.getBody());
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
                    // get workflow step
                    step = stepDAO.retrieveById(id);
                    // delete workflow step dependant nodes + step
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
