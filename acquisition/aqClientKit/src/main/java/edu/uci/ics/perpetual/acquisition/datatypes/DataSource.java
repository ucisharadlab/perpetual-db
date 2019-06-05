package edu.uci.ics.perpetual.acquisition.datatypes;
import edu.uci.ics.perpetual.acquisition.datatypes.DataSourceType;
public class DataSource {

    int dsInstanceId;

    public DataSourceType getType() {
        return type;
    }

    public void setType(DataSourceType type) {
        this.type = type;
    }

    DataSourceType type;

    public int getDsInstanceId() {
        return dsInstanceId;
    }

    public void setDsInstanceId(int dsInstanceId) {
        this.dsInstanceId = dsInstanceId;
    }

    public String getDsInstanceName() {
        return dsInstanceName;
    }

    public void setDsInstanceName(String dsInstanceName) {
        this.dsInstanceName = dsInstanceName;
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public String getDsParams() {
        return dsParams;
    }

    public void setDsParams(String dsParams) {
        this.dsParams = dsParams;
    }

    String dsInstanceName;
    String dsName;
    String dsParams;
}
