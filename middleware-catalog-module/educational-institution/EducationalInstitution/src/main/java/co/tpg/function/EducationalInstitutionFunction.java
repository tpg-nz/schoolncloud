package co.tpg.function;

import co.tpg.model.EducationalInstitution;
import co.tpg.model.TeachingStaff;
import co.tpg.request.EducationalInstitutionRequest;
import co.tpg.request.HttpMethod;
import co.tpg.request.TeachingStaffRequest;
import co.tpg.response.EducationalInstitutionalResponse;
import co.tpg.response.TeachingStaffResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler for requests to Educational Institution Lambda function.
 * @author Pamela
 * @since 2019-09-18
 */
public class EducationalInstitutionFunction implements RequestHandler<EducationalInstitutionRequest, EducationalInstitutionalResponse> {

    public EducationalInstitutionalResponse handleRequest(final EducationalInstitutionRequest input, final Context context) {
        final EducationalInstitutionalResponse response = new EducationalInstitutionalResponse();
        final Map<String, String> headers = new HashMap<>();
        String id;

        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);

        context.getLogger().log(String.format("HTTP method: %s\n", input.getHttpMethod()));

        switch (input.getHttpMethod()) {
            case HttpMethod.POST:
                context.getLogger().log(String.format("POST body=%s\n",input.getBody()));
                // todo: SEND TEACHING STAFF TO THE DATABASE
                response.setBody(EducationalInstitution.builder().id("12345").name("Post Test").build());
                response.setStatusCode(HttpServletResponse.SC_CREATED);
                break;
            case HttpMethod.GET:
                id = input.getQueryStringParameters().get("id");
                context.getLogger().log(String.format("GET ID=%s\n",id));
                response.setBody(EducationalInstitution.builder().id(id).name("Get Test").build());
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
