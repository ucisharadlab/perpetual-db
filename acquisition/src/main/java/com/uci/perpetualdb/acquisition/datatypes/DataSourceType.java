package com.uci.perpetualdb.acquisition.datatypes;

public class DataSourceType {
    int dsTypeId;
    String dsTypeParams;
    RawType rawType;

    public int getDsTypeId() {
        return dsTypeId;
    }

    public void setDsTypeId(int dsTypeId) {
        this.dsTypeId = dsTypeId;
    }

    public String getDsTypeParams() {
        return dsTypeParams;
    }

    public void setDsTypeParams(String dsTypeParams) {
        this.dsTypeParams = dsTypeParams;
    }

    public RawType getRawType() {
        return rawType;
    }

    public void setRawType(RawType rawType) {
        this.rawType = rawType;
    }

    public String getDsTypeName() {
        return dsTypeName;
    }

    public void setDsTypeName(String dsTypeName) {
        this.dsTypeName = dsTypeName;
    }

    String dsTypeName;

}
