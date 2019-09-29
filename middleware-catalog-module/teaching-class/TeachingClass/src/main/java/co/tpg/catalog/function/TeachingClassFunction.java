package co.tpg.catalog.function;

import co.tpg.catalog.exception.ProcessingException;
import co.tpg.catalog.model.TeachingClass;
import co.tpg.catalog.request.HttpMethod;
import co.tpg.catalog.request.TeachingClassRequest;
import co.tpg.catalog.response.AbstractResponse;
import co.tpg.catalog.response.ErrorResponse;
import co.tpg.catalog.response.TeachingClassResponse;
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
 * Handler for requests to Teaching Class (Classroom) Lambda function.
 * @author Rod
 * @since 2019-09-28
 */
public class TeachingClassFunction implements RequestHandler<TeachingClassRequest, AbstractResponse> {

    public AbstractResponse handleRequest(final TeachingClassRequest input, final Context context) {
        final String DYNAMO_TABLE_NAME = "TeachingClass";
        final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
        final TeachingClassResponse response = new TeachingClassResponse();
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
                    TeachingClass teachingClass = input.getBody();
                    teachingClass.setId(UUID.randomUUID().toString());
                    context.getLogger().log(String.format("POST body=%s\n", input.getBody()));
                    mapper.save(teachingClass);
                    response.setBody(teachingClass);
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
                        final TeachingClass teachingClass = mapper.load(TeachingClass.class,id);
                        if( teachingClass == null ) {
                            errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                            errorResponse.setBody(ProcessingException.builder().message(String.format("Teaching class with ID=%s not found.",id)).build());
                            return errorResponse;
                        }
                        //TODO: call helpers to fetch Campus and Paper objects from dynamoDB
                        //CatalogHelper.fetchCampus(teachingClass.getCampusId());
                        //CatalogHelper.fetchPaper(teachingClass.getPaperId());
                        response.setBody(teachingClass);
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
                final TeachingClass teachingClass = mapper.load(TeachingClass.class, id);
                if (teachingClass == null) {
                    errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                    errorResponse.setBody(ProcessingException.builder().message(String.format("Teaching class with ID=%s not found.", id)).build());
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
                    final TeachingClass teachingClass = mapper.load(TeachingClass.class, id);
                    if (teachingClass == null) {
                        errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                        errorResponse.setBody(ProcessingException.builder().message(String.format("Teaching class with ID=%s not found.", id)).build());
                    }
                    mapper.delete(teachingClass);
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
