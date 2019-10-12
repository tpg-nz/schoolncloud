package co.tpg.workflow.function;

import co.tpg.workflow.dao.DAO;
import co.tpg.workflow.dao.StepFieldDAO;
import co.tpg.workflow.dao.exception.BackendException;
import co.tpg.workflow.function.exception.ProcessingException;
import co.tpg.workflow.function.model.StepField;
import co.tpg.workflow.function.request.HttpMethod;
import co.tpg.workflow.function.request.StepFieldRequest;
import co.tpg.workflow.function.response.AbstractResponse;
import co.tpg.workflow.function.response.ErrorResponse;
import co.tpg.workflow.function.response.StepFieldListResponse;
import co.tpg.workflow.function.response.StepFieldResponse;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Lambda function handler for workflow step fields requests
 * @author Andrej
 * @since 2019-10-12
 */
public class StepFieldFunction implements RequestHandler<StepFieldRequest, AbstractResponse> {

    /**
     * Lambda function handler
     * @param stepFieldRequest   The lambda request object
     * @param context           The lambda runtime request context
     * @return                  The response of the rest operation
     */
    @Override
    public AbstractResponse handleRequest(StepFieldRequest stepFieldRequest, Context context) {

        final String DYNAMO_TABLE_NAME = "StepField";
        final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
        final StepFieldResponse response = new StepFieldResponse();
        final ErrorResponse errorResponse = new ErrorResponse();
        final StepFieldListResponse stepFieldListResponse = new StepFieldListResponse();
        final Map<String, String> headers = new HashMap<>();
        final DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        final StepFieldDAO stepFieldDAO = new StepFieldDAO();

        StepField stepField;
        LambdaLogger logger = context.getLogger();              // Get logger
        String httpMethod = stepFieldRequest.getHttpMethod();    // get http method

        // Set headers
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);

        // Set status code and log method
        errorResponse.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        logger.log(String.format("HTTP method: %s\n",stepFieldRequest.getHttpMethod()));

        // get worklow ID and check its existence
        String id = stepFieldRequest.getBody().getId();
        if ( id == null ) {
            if ( !((httpMethod.equals(HttpMethod.POST) ) || (httpMethod.equals(HttpMethod.GET)) ) ) { // in case of post id does not exists yet
                errorResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                errorResponse.setBody(ProcessingException.builder().message("Invalid path parameter <id>.").build());
                return errorResponse;
            }
        } else { // check if workflow exists
            stepField = mapper.load(StepField.class, id);
            if (stepField == null) { // step field was not found - error
                errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                errorResponse.setBody(ProcessingException.builder().message(String.format("Workflow step field with ID=%s was not found.", id)).build());
                return errorResponse;
            }
        }

        // Execute rest operation (CRUD)
        switch (httpMethod) {

            case HttpMethod.GET: {

                // log operation
                logger.log(String.format("GET id=%s body=%s\n", id, stepFieldRequest.getBody()));

                if (id == null) { // get paginated result
                    try {
                        // get last id
                        final String lastId = stepFieldRequest.getQueryStringParameters().get("lastId");

                        // Get all workflows with dependant nodes
                        List<StepField> stepFields = stepFieldDAO.retrieveAll(lastId, DAO.PAGE_SIZE);
                        stepFieldListResponse.setBody(stepFields);
                        stepFieldListResponse.setStatusCode(HttpServletResponse.SC_OK);
                    } catch (BackendException e) {
                        errorResponse.setBody(ProcessingException.builder().message(String.format(e.getMessage())).build());
                        return errorResponse;
                    }

                    return stepFieldListResponse;
                } else { // retrieve by id
                    try {
                        stepField = stepFieldDAO.retrieveById(id);
                        response.setBody(stepField);
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
                logger.log(String.format("POST body=%s\n", stepFieldRequest.getBody()));

                try {
                    // generate UUID and set it to id
                    stepField = stepFieldRequest.getBody();
                    stepField.setId(UUID.randomUUID().toString());

                    try {
                        // create workflow + dependant nodes (implemented in DAO)
                        stepFieldDAO.create(stepField);
                        mapper.save(stepField);
                        response.setBody(stepField);
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
                logger.log(String.format("PUT/PATCH id=%s body=%s\n", id, stepFieldRequest.getBody()));

                try {
                    // check if workflow exists -  if it does not exists, exception is thrown
                    stepField = stepFieldDAO.retrieveById(id);
                    // update step field
                    stepFieldDAO.update(stepFieldRequest.getBody());
                    mapper.save(stepFieldRequest.getBody());
                    response.setBody(stepFieldRequest.getBody() );
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
            
        // Delete workflow - using ID
        case HttpMethod.DELETE: {

            // log operation
            logger.log(String.format("DELETE id=%s \n", id, stepFieldRequest.getBody()));

            try {
                // check if step field exists
                stepField = stepFieldDAO.retrieveById(id);
                // delete step field
                stepFieldDAO.delete(stepField);
                mapper.delete(stepField);
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
