package edu.uci.ics.perpetual.request;

public class RequestStatus {
    enum Status {SUCCESS, FAILED}

    // single instance status, representing success
    private static final RequestStatus successRequest = new RequestStatus();

    private boolean success;

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
