package co.tpg.catalog.function;

import co.tpg.catalog.exception.ProcessingException;
import co.tpg.catalog.model.Subject;
import co.tpg.catalog.request.HttpMethod;
import co.tpg.catalog.request.SubjectRequest;
import co.tpg.catalog.response.AbstractResponse;
import co.tpg.catalog.response.ErrorResponse;
import co.tpg.catalog.response.SubjectResponse;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



/**
 * Handler for requests to Subject.
 * @author maxx
 * @since 2019-10-03
 */
public class SubjectFunction implements RequestHandler<SubjectRequest, AbstractResponse> {

    public AbstractResponse handleRequest(final SubjectRequest input, final Context context) {
        final String DYNAMO_TABLE_NAME = "Subject";
        final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
        final SubjectResponse response = new SubjectResponse();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Map<String, String> headers = new HashMap<>();
        final DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        String id;

        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);

        errorResponse.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        context.getLogger().log(String.format("HTTP method: %s\n",input.getHttpMethod()));

        switch (input.getHttpMethod()) {
            case HttpMethod.POST: {
                try {
                    Subject subject = input.getBody();
                    subject.setId(UUID.randomUUID().toString());
                    context.getLogger().log(String.format("POST body=%s\n", input.getBody()));
                    mapper.save(subject);
                    response.setBody(subject);
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
            case HttpMethod.GET:
                id = input.getQueryStringParameters().get("id");
                context.getLogger().log(String.format("GET ID=%s\n",id));
                try {
                    if( id == null ) { // get paginated items.
                        //TODO: provide the pagination logic using dynamoDB
                    } else { // get item by id
                        final Subject subject = mapper.load(Subject.class,id);
                        if( subject == null ) {
                            errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                            errorResponse.setBody(ProcessingException.builder().message(String.format("Subject with ID=%s not found.",id)).build());
                            return errorResponse;
                        }

                        response.setBody(subject);
                    }
                    response.setStatusCode(HttpServletResponse.SC_OK);
                } catch (ResourceNotFoundException ex) {
                    errorResponse.setBody(ProcessingException.builder().message(String.format("The table named %s could not be found in the backend system.",DYNAMO_TABLE_NAME)).build());
                    return errorResponse;
                } catch (AmazonServiceException ex) {
                    errorResponse.setBody(ProcessingException.builder().message(ex.getMessage()).build());
                    return errorResponse;
                }
                break;
            case HttpMethod.PUT:
            case HttpMethod.PATCH: {
                id = input.getPathParameters().get("id");
                context.getLogger().log(String.format("PUT/PATCH id=%s body=%s\n", id, input.getBody()));
                if (id == null) {
                    errorResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                    errorResponse.setBody(ProcessingException.builder().message("Invalid path parameter <id>.").build());
                    return errorResponse;
                }
                final Subject subject = mapper.load(Subject.class, id);
                if (subject == null) {
                    errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                    errorResponse.setBody(ProcessingException.builder().message(String.format("Subject with ID=%s not found.", id)).build());
                }
                mapper.save(input.getBody());
                response.setBody(input.getBody());
                response.setStatusCode(HttpServletResponse.SC_OK);
                break;
            }
            case HttpMethod.DELETE: {
                id = input.getPathParameters().get("id");
                context.getLogger().log(String.format("DELETE id=%s \n", id, input.getBody()));
                try {
                    if (id == null) {
                        errorResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                        errorResponse.setBody(ProcessingException.builder().message("Invalid path parameter <id>.").build());
                        return errorResponse;
                    }
                    final Subject subject = mapper.load(Subject.class, id);
                    if (subject == null) {
                        errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                        errorResponse.setBody(ProcessingException.builder().message(String.format("Subject with ID=%s not found.", id)).build());
                    }
                    mapper.delete(subject);
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
                response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
        }
        return response;
    }
}
