package datatypes;

import java.util.List;

public class Request {

    DataSourceType datasourceType;
    int reqId;
    long startTime;
    long endTime;
    int resolution;
    String params;


    AcquisitionFunction acquisitionFunction;

    public List<DataSource> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    List<DataSource> dataSources;

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    RequestStatus status;

    public DataSourceType getDatasourceType() {
        return datasourceType;
    }

    public void setDatasourceType(DataSourceType datasourceType) {
        this.datasourceType = datasourceType;
    }

    public int getReqId() {
        return reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public AcquisitionFunction getAcquisitionFunction() {
        return acquisitionFunction;
    }

    public void setAcquisitionFunction(AcquisitionFunction acquisitionFunction) {
        this.acquisitionFunction = acquisitionFunction;
    }

}
