package edu.uci.ics.perpetual.request;

import java.util.Date;
import java.util.HashMap;

public class AcquisitionRequest {

    private int requestId;

    private String acquisitionName;

    private int dataSourceId;

    private String acquisitionFunctionPath;

    private HashMap<String, String> acquisitionFunctionParameters;

    private AcquisitionRequestStatus status;

    private HashMap<String, String> rawTypeScheme;

    private Date startTime;

    private Date endTime;

    private int frequency;

    public AcquisitionRequest() {
    }

    public AcquisitionRequest(int dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public AcquisitionRequest(int requestId, int dataSourceId) {
        this.requestId = requestId;
        this.dataSourceId = dataSourceId;
    }


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

    public AcquisitionRequestStatus getStatus() {
        return status;
    }

    public void setStatus(AcquisitionRequestStatus status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {

        return String.format("RequestId = %s, DataSourceId = %s, Status = %s, FunctionPath = %s",
                requestId, dataSourceId, status, acquisitionFunctionPath);

    }
}
