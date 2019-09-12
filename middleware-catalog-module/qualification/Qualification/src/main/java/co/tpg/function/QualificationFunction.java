package co.tpg.function;

import co.tpg.model.Qualification;
import co.tpg.request.HttpMethod;
import co.tpg.request.QualificationRequest;
import co.tpg.response.QualificationResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * Handler for requests to Qualification Lambda function.
 * @author James
 * @since 2019-09-11
 */
public class QualificationFunction implements RequestHandler<Qualification, Qualification> {

    public Qualification handleRequest(final Object input, final Context context) {
        final QualificationResponse response = new QualificationResponse();
        final Map<String, String> headers = new HashMap<>();
        String id;

        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);

        context.getLogger().log("HTTP method: "+ input.getHttpMethod());

        switch (input.getHttpMethod()) {
            case HttpMethod.POST:
                context.getLogger().log(String.format("POST body=%s\n",input.getBody()));
                // todo: SEND QUALIFICATION TO THE DATABASE
                response.setBody(Qualification.builder().id("12345").name("Post Test").hyperlink("Test Link").build());
                response.setStatusCode(HttpServletResponse.SC_CREATED);
                break;
            case HttpMethod.GET:
                id = input.getQueryStringParameters().get("id");
                context.getLogger().log(String.format("GET ID=%s\n",id));
                response.setBody(Qualification.builder().id(id).name("Post Test").hyperlink("Test Link").build());
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
