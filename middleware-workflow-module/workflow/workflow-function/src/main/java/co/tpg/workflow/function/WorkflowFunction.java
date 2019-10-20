package co.tpg.workflow.function;

import co.tpg.function.AbstractFunction;
import co.tpg.workflow.dao.DAO;
import co.tpg.workflow.dao.WorkflowDAO;
import co.tpg.workflow.dao.exception.BackendException;
import co.tpg.workflow.exception.ProcessingException;
import co.tpg.workflow.function.model.Workflow;
import co.tpg.workflow.function.request.HttpMethod;
import co.tpg.workflow.function.request.WorkflowRequest;
import co.tpg.workflow.function.response.AbstractResponse;
import co.tpg.workflow.function.response.ErrorResponse;
import co.tpg.workflow.function.response.WorkflowListResponse;
import co.tpg.workflow.function.response.WorkflowResponse;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Lambda function handler for workflow requests
 * @author Andrej
 * @since 2019-10-12
 */
public class WorkflowFunction extends AbstractFunction<WorkflowRequest, AbstractResponse> {

    /**
     *Lambda function handler
     * @param workflowRequest   The lambda request object
     * @param context           The lambda runtime request context
     * @return                  The response of the rest operation
     */
    @Override
    public AbstractResponse handleRequest(final WorkflowRequest workflowRequest, final Context context) {

        final String DYNAMO_TABLE_NAME = "Workflow";
        final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
        final WorkflowResponse response = new WorkflowResponse();
        final ErrorResponse errorResponse = new ErrorResponse();
        final WorkflowListResponse workflowListResponse = new WorkflowListResponse();
        final Map<String, String> headers = new HashMap<>();
        final DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        final WorkflowDAO workflowDAO = new WorkflowDAO();

        Workflow workflow;
        LambdaLogger logger = context.getLogger();              // Get logger
        String httpMethod = workflowRequest.getHttpMethod();    // get http method

        // Set headers
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);

        // Set status code and log method
        errorResponse.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        logger.log(String.format("HTTP method: %s\n",workflowRequest.getHttpMethod()));

        // get workflow ID and check its existence
        String id = workflowRequest.getBody().getId();
        if ( id == null ) {
            if ( !((httpMethod.equals(HttpMethod.POST) ) || (httpMethod.equals(HttpMethod.GET)) ) ) { // in case of post id does not exists yet
                errorResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                errorResponse.setBody(ProcessingException.builder().message("Invalid path parameter <id>.").build());
                return errorResponse;
            }
        } else { // check if workflow exists
            workflow = mapper.load(Workflow.class, id);
            if (workflow == null) { // workflow was not found - error
                errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                errorResponse.setBody(ProcessingException.builder().message(String.format("Workflow with ID=%s was not found.", id)).build());
                return errorResponse;
            }
        }

        // Execute rest operation (CRUD)
        switch (httpMethod) {

            case HttpMethod.GET: {

                // log operation
                logger.log(String.format("GET id=%s body=%s\n", id, workflowRequest.getBody()));

                if (id == null) { // get paginated result
                    try {
                        // get last id
                        final String lastId = workflowRequest.getQueryStringParameters().get("lastId");

                        // Get all workflows with dependant nodes
                        List<Workflow> workflows = workflowDAO.retrieveAll(lastId, DAO.PAGE_SIZE);
                        workflowListResponse.setBody(workflows);
                        workflowListResponse.setStatusCode(HttpServletResponse.SC_OK);
                    } catch (BackendException e) {
                        errorResponse.setBody(ProcessingException.builder().message(String.format(e.getMessage())).build());
                        return errorResponse;
                    }

                    return workflowListResponse;
                } else { // retrieve by id
                    try {
                        workflow = workflowDAO.retrieveById(id);
                        response.setBody(workflow);
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
            logger.log(String.format("POST body=%s\n", workflowRequest.getBody()));

            try {
                // generate UUID and set it to id
                workflow = workflowRequest.getBody();
                workflow.setId(UUID.randomUUID().toString());

                try {
                    // create workflow + dependant nodes (implemented in DAO)
                    workflowDAO.create(workflow);
                    mapper.save(workflow);
                    response.setBody(workflow);
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
            logger.log(String.format("PUT/PATCH id=%s body=%s\n", id, workflowRequest.getBody()));

            try {
                // check if workflow exists
                workflow = workflowDAO.retrieveById(id); // if it does not exists, exception is thrown

                // udpate workflow and the dependant nodes
                workflowDAO.update(workflowRequest.getBody());
                mapper.save(workflowRequest.getBody());
                response.setBody(workflowRequest.getBody() );
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
                logger.log(String.format("DELETE id=%s \n", id, workflowRequest.getBody()));

                try {
                    // check if workflow exists
                    workflow = workflowDAO.retrieveById(id);
                    // delete workflow with dependant nodes
                    workflowDAO.delete(workflow);
                    mapper.delete(workflow);
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

    @Override
    public Class<WorkflowRequest> getClazz() {
        return WorkflowRequest.class;
    }
}
