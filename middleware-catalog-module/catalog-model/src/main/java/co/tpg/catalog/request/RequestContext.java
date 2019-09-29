package co.tpg.catalog.request;

/**
 * Request Context for lambda requests.
 * @author Rod
 * @since 2019-09-04
 */
public class RequestContext {
    private String accountId;
    private String resourceId;
    private String stage;
    private String requestId;
    private String requestTime;
    private int requestTimeEpoch;
    private Identity identity;

    public RequestContext(String accountId, String resourceId, String stage, String requestId, String requestTime, int requestTimeEpoch, Identity identity) {
        this.accountId = accountId;
        this.resourceId = resourceId;
        this.stage = stage;
        this.requestId = requestId;
        this.requestTime = requestTime;
        this.requestTimeEpoch = requestTimeEpoch;
        this.identity = identity;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public int getRequestTimeEpoch() {
        return requestTimeEpoch;
    }

    public void setRequestTimeEpoch(int requestTimeEpoch) {
        this.requestTimeEpoch = requestTimeEpoch;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }
}
