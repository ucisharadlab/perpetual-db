package edu.uci.ics.perpetual.request;

/**
 * RequestStatus represents if a request is success or not.
 * It embeds the error message when the request fails
 */
public class RequestStatus {
    // single instance status, representing success
    private static final RequestStatus successRequest = new RequestStatus();

    private boolean success;

    // If success, errMsg is null
    private String errMsg;

    public RequestStatus() {
        success = true; // default is true.
    }

    public static RequestStatus success() {
        return successRequest;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setErrMsg(String errMsg) {
        this.success = false;
        this.errMsg = errMsg;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
