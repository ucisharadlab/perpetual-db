package edu.uci.ics.perpetual.request;

import javafx.util.Pair;

/**
 * THe LoadRequest is used internally for saving data to Database.
 */
public class StorageRequest extends Request {

    private boolean isType;

    private Object object;

    private Pair<String, String> relation;

    public StorageRequest(Object object) {
        this.isType = true;
        this.object = object;
    }

    public StorageRequest(String parent, String child) {
        this.isType = false;
        this.relation = new Pair<>(parent, child);
    }

    public boolean isType() {
        return isType;
    }

    public Object getObject() {
        return object;
    }

    public Pair<String, String> getRelation() {
        return relation;
    }
}
