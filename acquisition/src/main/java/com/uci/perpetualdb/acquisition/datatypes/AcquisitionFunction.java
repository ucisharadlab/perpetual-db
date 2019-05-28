package com.uci.perpetualdb.acquisition.datatypes;

public class AcquisitionFunction {
    DataSourceType forType;
    String name;
    String path;
    String params;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataSourceType getForType() {
        return forType;
    }

    public void setForType(DataSourceType forType) {
        this.forType = forType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
