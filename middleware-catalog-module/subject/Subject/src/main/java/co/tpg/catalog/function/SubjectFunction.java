package co.tpg.catalog.function;

import co.tpg.catalog.dao.DAO;
import co.tpg.catalog.dao.SubjectDAO;
import co.tpg.catalog.dao.exception.BackendException;
import co.tpg.catalog.exception.ProcessingException;
import co.tpg.catalog.model.Subject;
import co.tpg.catalog.request.HttpMethod;
import co.tpg.catalog.request.SubjectRequest;
import co.tpg.catalog.response.AbstractResponse;
import co.tpg.catalog.response.ErrorResponse;
import co.tpg.catalog.response.SubjectListResponse;
import co.tpg.catalog.response.SubjectResponse;
import co.tpg.function.AbstractFunction;
import com.amazonaws.services.lambda.runtime.Context;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler for requests to Subject.
 * @author maxx
 * @since 2019-10-03
 */
public class SubjectFunction extends AbstractFunction<SubjectRequest, AbstractResponse> {

    public AbstractResponse handleRequest(final SubjectRequest input, final Context context) {
        final SubjectResponse response = new SubjectResponse();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Map<String, String> headers = new HashMap<>();
        final SubjectDAO subjectDAO = new SubjectDAO();
        final Subject subject;
        final String id;

        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);
        errorResponse.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        context.getLogger().log(String.format("HTTP method: %s\n",input.getHttpMethod()));

        try {
            switch (input.getHttpMethod()) {
                case HttpMethod.POST: 
                    subject = input.getBody();
                    context.getLogger().log(String.format("POST body=%s\n", input.getBody()));
                    subjectDAO.create(subject);
                    response.setBody(subject);
                    response.setStatusCode(HttpServletResponse.SC_CREATED);
                    break;
                case HttpMethod.GET:
                    id = input.getPathParameters().get("id");
                    context.getLogger().log(String.format("GET ID=%s\n",id));
                    if( id == null ) { // get paginated items.
                        final SubjectListResponse listResponse = new SubjectListResponse();
                        final String lastId = input.getQueryStringParameters().get("lastId");
                        listResponse.setBody(subjectDAO.retrieveAll(lastId, DAO.PAGE_SIZE));
                        listResponse.setStatusCode(HttpServletResponse.SC_OK);
                        return listResponse;
                    } else { // get item by id
                        subject = subjectDAO.retrieveById(id);
                        if( subject == null ) {
                            errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                            errorResponse.setBody(ProcessingException.builder().message(String.format("Subject with ID=%s not found.",id)).build());
                            return errorResponse;
                        }
                        response.setBody(subject);
                    }
                    response.setStatusCode(HttpServletResponse.SC_OK);
                    break;
                case HttpMethod.PUT:
                case HttpMethod.PATCH: 
                    id = input.getPathParameters().get("id");
                    context.getLogger().log(String.format("PUT/PATCH id=%s body=%s\n", id, input.getBody()));
                    if (id == null) {
                        errorResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                        errorResponse.setBody(ProcessingException.builder().message("Invalid path parameter <id>.").build());
                        return errorResponse;
                    }
                    subject = subjectDAO.retrieveById(id);
                    if (subject == null) {
                        errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                        errorResponse.setBody(ProcessingException.builder().message(String.format("Subject with ID=%s not found.", id)).build());
                    }
                    subjectDAO.update(input.getBody());
                    response.setBody(input.getBody());
                    response.setStatusCode(HttpServletResponse.SC_OK);
                    break;
                case HttpMethod.DELETE: {
                    id = input.getPathParameters().get("id");
                    context.getLogger().log(String.format("DELETE id=%s \n", id, input.getBody()));
                    if (id == null) {
                        errorResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                        errorResponse.setBody(ProcessingException.builder().message("Invalid path parameter <id>.").build());
                        return errorResponse;
                    }
                    subject = subjectDAO.retrieveById(id);
                    if (subject == null) {
                        errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                        errorResponse.setBody(ProcessingException.builder().message(String.format("Subject with ID=%s not found.", id)).build());
                        return errorResponse;
                    }
                    subjectDAO.delete(subject);
                    response.setStatusCode(HttpServletResponse.SC_OK);
                    break;
                }
                default:
                    response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (BackendException e) {
            errorResponse.setBody(ProcessingException.builder().message(e.getMessage()).build());
            return errorResponse;
        }
        return response;
    }

    @Override
    public Class<SubjectRequest> getClazz() {
        return SubjectRequest.class;
    }
}
