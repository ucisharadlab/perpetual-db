package edu.uci.ics.perpetual.table;

import java.util.List;

public class Parameters {

    private List<String> paramList;

    public Parameters(List<String> paramList) {
        this.paramList = paramList;
    }

    public List<String> getParamList() {
        return paramList;
    }

    public boolean hasParameter(String parameter) {
        return parameter.contains(parameter);
    }
}
