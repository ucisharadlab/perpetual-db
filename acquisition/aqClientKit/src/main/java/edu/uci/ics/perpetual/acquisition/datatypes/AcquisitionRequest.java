package edu.uci.ics.perpetual.acquisition.datatypes;

import java.util.HashMap;

public class AcquisitionRequest {

    private int requestId;

    private String acquisitionName;

    private int dataSourceId;

    private String acquisitionFunctionPath;

    private HashMap<String, String> acquisitionFunctionParameters;

    private RequestStatus status;

    private HashMap<String, String> rawTypeScheme;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getAcquisitionName() {
        return acquisitionName;
    }

    public void setAcquisitionName(String acquisitionName) {
        this.acquisitionName = acquisitionName;
    }

    public int getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(int dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getAcquisitionFunctionPath() {
        return acquisitionFunctionPath;
    }

    public void setAcquisitionFunctionPath(String acquisitionFunctionPath) {
        this.acquisitionFunctionPath = acquisitionFunctionPath;
    }

    public HashMap <String, String> getAcquisitionFunctionParameters() {
        return acquisitionFunctionParameters;
    }

    public void setAcquisitionFunctionParameters(HashMap <String, String> acquisitionFunctionParameters) {
        this.acquisitionFunctionParameters = acquisitionFunctionParameters;
    }

    public HashMap <String, String> getRawTypeScheme() {
        return rawTypeScheme;
    }

    public void setRawTypeScheme(HashMap <String, String> rawTypeScheme) {
        this.rawTypeScheme = rawTypeScheme;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }


}
