package co.tpg.response;

import java.util.Map;

/**
 * Abstract class to provide response for lambda functions
 * @author Rod
 * @since 2019-09-04
 */
public abstract class AbstractResponse<T> {
    protected Map<String, String> headers;
    protected T body;
    protected int statusCode;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
