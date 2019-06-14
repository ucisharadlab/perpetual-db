package edu.uci.ics.perpetual.request;

/**
 * All communicate (internally or externally)
 * with SchemaManager will be done through Requests
 */
public abstract class Request {

    private RequestStatus status;

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
