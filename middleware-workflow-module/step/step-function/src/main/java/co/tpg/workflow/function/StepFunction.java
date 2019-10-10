package co.tpg.workflow.function;

import co.tpg.workflow.exception.ProcessingException;
import co.tpg.workflow.model.Step;
import co.tpg.workflow.request.HttpMethod;
import co.tpg.workflow.request.StepRequest;
import co.tpg.workflow.response.AbstractResponse;
import co.tpg.workflow.response.ErrorResponse;
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
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

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
        final Map<String, String> headers = new HashMap<>();
        final DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

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
        id = stepRequest.getPathParameters().get("id");
        if ( id == null ) {
            if (httpMethod != HttpMethod.POST ) { // in case of post id does not exists yet
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
                try {
                    response.setBody(step);
                } catch (ResourceNotFoundException ex) {
                    errorResponse.setBody(ProcessingException.builder().message(String.format("The table named %s could not be found in the backend system.",DYNAMO_TABLE_NAME)).build());
                    return errorResponse;
                } catch (AmazonServiceException ex) {
                    errorResponse.setBody(ProcessingException.builder().message(ex.getMessage()).build());
                    return errorResponse;
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

                    // save to DB + set response
                    mapper.save(step);
                    response.setBody(step);
                    response.setStatusCode(HttpServletResponse.SC_CREATED);

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
                mapper.save(stepRequest.getBody());
                response.setBody(stepRequest.getBody());
                response.setStatusCode(HttpServletResponse.SC_OK);
            } catch (AmazonServiceException ex) {
                errorResponse.setBody(ProcessingException.builder().message(ex.getMessage()).build());
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
                    mapper.delete(step);
                    response.setStatusCode(HttpServletResponse.SC_OK);

                } catch (ResourceNotFoundException ex) {
                    errorResponse.setBody(ProcessingException.builder().message(String.format("The table named %s could not be found in the backend system.",DYNAMO_TABLE_NAME)).build());
                    return errorResponse;
                } catch (AmazonServiceException ex) {
                    errorResponse.setBody(ProcessingException.builder().message(ex.getMessage()).build());
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
