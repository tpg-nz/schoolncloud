package co.tpg.function;

import co.tpg.model.Campus;
import co.tpg.request.CampusRequest;
import co.tpg.request.HttpMethod;
import co.tpg.response.CampusResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler for requests to TeachingStaff Lambda function.
 * @author Rod
 * @since 2019-09-04
 */
public class CampusFunction implements RequestHandler<CampusRequest, CampusResponse> {

    public CampusResponse handleRequest(final CampusRequest input, final Context context) {
        final CampusResponse response = new CampusResponse();
        final Map<String, String> headers = new HashMap<>();
        String id;

        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);

        context.getLogger().log(String.format("HTTP method: %s\n",input.getHttpMethod()));

        switch (input.getHttpMethod()) {
            case HttpMethod.POST:
                context.getLogger().log(String.format("POST body=%s\n",input.getBody()));
                // todo: SEND TEACHING STAFF TO THE DATABASE
                response.setBody(Campus.builder().id("12345").name("Campus POST Test").build());
                response.setStatusCode(HttpServletResponse.SC_CREATED);
                break;
            case HttpMethod.GET:
                id = input.getQueryStringParameters().get("id");
                context.getLogger().log(String.format("GET ID=%s\n",id));
                response.setBody(Campus.builder().id(id).name("Campus GET Test").build());
                response.setStatusCode(HttpServletResponse.SC_OK);
                break;
            case HttpMethod.PUT:
            case HttpMethod.PATCH:
                id = input.getPathParameters().get("id");
                context.getLogger().log(String.format("PUT/PATCH id=%s body=%s\n",id,input.getBody()));
                response.setBody(input.getBody());
                response.setStatusCode(HttpServletResponse.SC_OK);
                break;
            case HttpMethod.DELETE:
                id = input.getPathParameters().get("id");
                context.getLogger().log(String.format("DELETE id=%s \n",id,input.getBody()));
                response.setStatusCode(HttpServletResponse.SC_OK);
                break;
            default:

        }
        return response;
    }
}
