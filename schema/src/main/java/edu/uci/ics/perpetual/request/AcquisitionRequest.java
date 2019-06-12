package edu.uci.ics.perpetual.request;

import java.util.HashMap;

public class AcquisitionRequest extends Request {
    // provided by AcquisitionManager
    private int requestId;
    private int dataSourceId;

    private String acquisitionFunctionPath;

    private HashMap<String, String> acquisitionFunctionParameters;

    private HashMap<String, String> rawTypeScheme;

    public AcquisitionRequest(int dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public AcquisitionRequest(int requestId, int dataSourceId) {
        this.requestId = requestId;
        this.dataSourceId = dataSourceId;
    }

    // region Setter
    public void setAcquisitionFunctionPath(String acquisitionFunctionPath) {
        this.acquisitionFunctionPath = acquisitionFunctionPath;
    }

    public void setAcquisitionFunctionParameters(HashMap<String, String> acquisitionFunctionParameters) {
        this.acquisitionFunctionParameters = acquisitionFunctionParameters;
    }

    public void setRawTypeScheme(HashMap<String, String> rawTypeScheme) {
        this.rawTypeScheme = rawTypeScheme;
    }
    // endregion

    // region Getter
    public int getRequestId() {
        return requestId;
    }

    public int getDataSourceId() {
        return dataSourceId;
    }

    public String getAcquisitionFunctionPath() {
        return acquisitionFunctionPath;
    }

    public HashMap<String, String> getAcquisitionFunctionParameters() {
        return acquisitionFunctionParameters;
    }

    public HashMap<String, String> getRawTypeScheme() {
        return rawTypeScheme;
    }
    // endregion
}
