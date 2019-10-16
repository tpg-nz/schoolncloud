package co.tpg.catalog.function;

import co.tpg.catalog.dao.DAO;
import co.tpg.catalog.dao.QualificationDAO;
import co.tpg.catalog.dao.exception.BackendException;
import co.tpg.catalog.exception.ProcessingException;
import co.tpg.catalog.model.Qualification;
import co.tpg.catalog.request.HttpMethod;
import co.tpg.catalog.request.QualificationRequest;
import co.tpg.catalog.response.AbstractResponse;
import co.tpg.catalog.response.ErrorResponse;
import co.tpg.catalog.response.QualificationListResponse;
import co.tpg.catalog.response.QualificationResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler for requests to Qualification Lambda function.
 * @author James
 * @since 2019-10-13
 */
public class QualificationFunction implements RequestHandler<QualificationRequest, AbstractResponse> {

    public AbstractResponse handleRequest(final QualificationRequest input, final Context context) {
        final QualificationResponse response = new QualificationResponse();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Map<String, String> headers = new HashMap<>();
        final QualificationDAO qualificationDAO = new QualificationDAO();
        final Qualification qualification;
        final String id;

        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);
        errorResponse.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        context.getLogger().log(String.format("HTTP method: %s\n",input.getHttpMethod()));

        try {
            switch (input.getHttpMethod()) {
                case HttpMethod.POST:
                    qualification = input.getBody();
                    context.getLogger().log(String.format("POST body=%s\n",input.getBody()));
                    qualificationDAO.create(qualification);
                    response.setBody(qualification);
                    response.setStatusCode(HttpServletResponse.SC_CREATED);
                    break;
                case HttpMethod.GET:
                    id = input.getPathParameters().get("id");
                    context.getLogger().log(String.format("GET ID=%s\n",id));
                    if (id == null) { // get paginated items.
                        final QualificationListResponse listResponse = new QualificationListResponse();
                        final String lastId = input.getQueryStringParameters().get("lastId");
                        listResponse.setBody(qualificationDAO.retrieveAll(lastId, DAO.PAGE_SIZE));
                        listResponse.setStatusCode(HttpServletResponse.SC_OK);
                        return listResponse;
                    }
                    else { // get item by id.
                        qualification = qualificationDAO.retrieveById(id);
                        if (qualification == null) {
                            errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                            errorResponse.setBody(ProcessingException.builder().message(String.format("Qualification with ID=%s not found.", id)).build());
                            return errorResponse;
                        }
                        response.setBody(qualification);
                    }
                    response.setStatusCode(HttpServletResponse.SC_OK);
                    break;
                case HttpMethod.PUT:
                case HttpMethod.PATCH:
                    id = input.getPathParameters().get("id");
                    context.getLogger().log(String.format("PUT/PATCH id=%s body=%s\n",id,input.getBody()));
                    if (id == null) {
                        errorResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                        errorResponse.setBody(ProcessingException.builder().message("Invalid path parameter <id>.").build());
                        return errorResponse;
                    }
                    qualification = qualificationDAO.retrieveById(id);
                    if (qualification == null) {
                        errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                        errorResponse.setBody(ProcessingException.builder().message(String.format("Qualification with ID=%s not found.", id)).build());
                    }
                    qualificationDAO.update(input.getBody());
                    response.setBody(input.getBody());
                    response.setStatusCode(HttpServletResponse.SC_OK);
                    break;
                case HttpMethod.DELETE:
                    id = input.getPathParameters().get("id");
                    context.getLogger().log(String.format("DELETE id=%s \n",id,input.getBody()));
                    if (id == null) {
                        errorResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                        errorResponse.setBody(ProcessingException.builder().message("Invalid path parameter <id>.").build());
                        return errorResponse;
                    }
                    qualification = qualificationDAO.retrieveById(id);
                    if (qualification == null) {
                        errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                        errorResponse.setBody(ProcessingException.builder().message(String.format("Qualification with ID=%s not found.", id)).build());
                    }
                    qualificationDAO.delete(qualification);
                    response.setStatusCode(HttpServletResponse.SC_OK);
                    break;
                default:
                    response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        catch (BackendException e) {
            errorResponse.setBody(ProcessingException.builder().message(e.getMessage()).build());
            return errorResponse;
        }

        return response;
    }
}
