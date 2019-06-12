package edu.uci.ics.perpetual.request;

public class LoadRequest extends Request {

    public enum LoadOption {SCHEMA, RELATION}

    private LoadOption option;

    private Object result;

    public LoadRequest(LoadOption option) {
        this.option = option;
    }

    public LoadOption getOption() {
        return option;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }
}
