package co.tpg.catalog.function;

import co.tpg.catalog.dao.DAO;
import co.tpg.catalog.dao.TeachingClassDAO;
import co.tpg.catalog.dao.exception.BackendException;
import co.tpg.catalog.exception.ProcessingException;
import co.tpg.catalog.model.TeachingClass;
import co.tpg.catalog.request.HttpMethod;
import co.tpg.catalog.request.TeachingClassRequest;
import co.tpg.catalog.response.AbstractResponse;
import co.tpg.catalog.response.ErrorResponse;
import co.tpg.catalog.response.TeachingClassListResponse;
import co.tpg.catalog.response.TeachingClassResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler for requests to Teaching Class (Classroom) Lambda function.
 * @author Rod
 * @since 2019-09-28
 */
public class TeachingClassFunction implements RequestHandler<TeachingClassRequest, AbstractResponse> {

    public AbstractResponse handleRequest(final TeachingClassRequest input, final Context context) {
        final TeachingClassResponse response = new TeachingClassResponse();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Map<String, String> headers = new HashMap<>();
        final TeachingClassDAO teachingClassDAO = new TeachingClassDAO();
        final TeachingClass teachingClass;
        final String id;

        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);
        errorResponse.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        context.getLogger().log(String.format("HTTP method: %s\n",input.getHttpMethod()));

        try {
            switch (input.getHttpMethod()) {
                case HttpMethod.POST:
                    teachingClass = input.getBody();
                    context.getLogger().log(String.format("POST body=%s\n", input.getBody()));
                    teachingClassDAO.create(teachingClass);
                    response.setBody(teachingClass);
                    response.setStatusCode(HttpServletResponse.SC_CREATED);
                    break;
                case HttpMethod.GET:
                    id = input.getPathParameters().get("id");
                    context.getLogger().log(String.format("GET ID=%s\n", id));
                    if (id == null) { // get paginated items.
                        final TeachingClassListResponse listResponse = new TeachingClassListResponse();
                        final String lastId = input.getQueryStringParameters().get("lastId");
                        listResponse.setBody(teachingClassDAO.retrieveAll(lastId, DAO.PAGE_SIZE));
                        listResponse.setStatusCode(HttpServletResponse.SC_OK);
                        return listResponse;
                    } else { // get item by id
                        teachingClass = teachingClassDAO.retrieveById(id);
                        if (teachingClass == null) {
                            errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                            errorResponse.setBody(ProcessingException.builder().message(String.format("Teaching class with ID=%s not found.", id)).build());
                            return errorResponse;
                        }
                        //TODO: call helpers to fetch Campus and Paper objects from dynamoDB
                        //CatalogHelper.fetchCampus(teachingClass.getCampusId());
                        //CatalogHelper.fetchPaper(teachingClass.getPaperId());
                        response.setBody(teachingClass);
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
                    teachingClass = teachingClassDAO.retrieveById(id);
                    if (teachingClass == null) {
                        errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                        errorResponse.setBody(ProcessingException.builder().message(String.format("Teaching class with ID=%s not found.", id)).build());
                    }
                    teachingClassDAO.update(input.getBody());
                    response.setBody(input.getBody());
                    response.setStatusCode(HttpServletResponse.SC_OK);
                    break;
                case HttpMethod.DELETE:
                    id = input.getPathParameters().get("id");
                    context.getLogger().log(String.format("DELETE id=%s \n", id, input.getBody()));
                    if (id == null) {
                        errorResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                        errorResponse.setBody(ProcessingException.builder().message("Invalid path parameter <id>.").build());
                        return errorResponse;
                    }
                    teachingClass = teachingClassDAO.retrieveById(id);
                    if (teachingClass == null) {
                        errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                        errorResponse.setBody(ProcessingException.builder().message(String.format("Teaching class with ID=%s not found.", id)).build());
                    }
                    teachingClassDAO.delete(teachingClass);
                    response.setStatusCode(HttpServletResponse.SC_OK);
                    break;
                default:
                    response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (BackendException e) {
            errorResponse.setBody(ProcessingException.builder().message(e.getMessage()).build());
            return errorResponse;
        }

        return response;
    }
}
