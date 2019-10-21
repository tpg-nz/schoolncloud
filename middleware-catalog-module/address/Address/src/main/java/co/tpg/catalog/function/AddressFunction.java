package co.tpg.catalog.function;

import co.tpg.catalog.dao.DAO;
import co.tpg.catalog.dao.exception.BackendException;
import co.tpg.catalog.exception.ProcessingException;
import co.tpg.catalog.model.Address;
import co.tpg.catalog.request.HttpMethod;
import co.tpg.catalog.request.AddressRequest;
import co.tpg.catalog.response.AbstractResponse;
import co.tpg.catalog.response.ErrorResponse;
import co.tpg.catalog.response.AddressListResponse;
import co.tpg.catalog.response.AddressResponse;
import co.tpg.catalog.dao.AddressDAO;
import co.tpg.function.AbstractFunction;
import com.amazonaws.services.lambda.runtime.Context;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler for requests to Address.
 * @author maxx
 * @since 2019-10-11
 */
public class AddressFunction extends AbstractFunction<AddressRequest, AbstractResponse> {

    public AbstractResponse handleRequest(final AddressRequest input, final Context context) {
        final AddressResponse response = new AddressResponse();
        final AddressDAO addressDAO = new AddressDAO();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Map<String, String> headers = new HashMap<>();
        final Address address;
        String id;

        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);
        errorResponse.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        context.getLogger().log(String.format("HTTP method: %s\n",input.getHttpMethod()));

        try {
            switch (input.getHttpMethod()) {
                case HttpMethod.POST: {
                    address = input.getBody();
                    context.getLogger().log(String.format("POST body=%s\n", input.getBody()));
                    addressDAO.create(address);
                    response.setBody(address);
                    response.setStatusCode(HttpServletResponse.SC_CREATED);
                    break;
                }
                case HttpMethod.GET:
                    id = input.getPathParameters().get("id");
                    context.getLogger().log(String.format("GET ID=%s\n",id));
                    if( id == null ) { // get paginated items.
                        final AddressListResponse listResponse = new AddressListResponse();
                        final String lastId = input.getQueryStringParameters().get("lastId");
                        listResponse.setBody(addressDAO.retrieveAll(lastId, DAO.PAGE_SIZE));
                        listResponse.setStatusCode(HttpServletResponse.SC_OK);
                        return listResponse;
                    } else { // get item by id
                        address = addressDAO.retrieveById(id);
                        if( address == null ) {
                            errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                            errorResponse.setBody(ProcessingException.builder().message(String.format("Address with ID=%s not found.",id)).build());
                            return errorResponse;
                        }
                        response.setBody(address);
                    }
                    response.setStatusCode(HttpServletResponse.SC_OK);
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
                    address = addressDAO.retrieveById(id);
                    if (address == null) {
                        errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                        errorResponse.setBody(ProcessingException.builder().message(String.format("Address with ID=%s not found.", id)).build());
                    }
                    addressDAO.update(input.getBody());
                    response.setBody(input.getBody());
                    response.setStatusCode(HttpServletResponse.SC_OK);
                    break;
                }
                case HttpMethod.DELETE: {
                    id = input.getPathParameters().get("id");
                    context.getLogger().log(String.format("DELETE id=%s \n", id, input.getBody()));
                    if (id == null) {
                        errorResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                        errorResponse.setBody(ProcessingException.builder().message("Invalid path parameter <id>.").build());
                        return errorResponse;
                    }
                    address = addressDAO.retrieveById(id);
                    if (address == null) {
                        errorResponse.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
                        errorResponse.setBody(ProcessingException.builder().message(String.format("Address with ID=%s not found.", id)).build());
                        return errorResponse;
                    }
                    addressDAO.delete(address);
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
    public Class<AddressRequest> getClazz() {
        return AddressRequest.class;
    }
}
