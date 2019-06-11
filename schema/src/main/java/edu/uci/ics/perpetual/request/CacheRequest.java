package edu.uci.ics.perpetual.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CacheRequest extends Request {

    private boolean findAll;

    private List<String> allRawTypes;

    private HashMap<String, ArrayList<String>> tagFunctionMapping;

    private String rawTypeName;

    public CacheRequest() {
        this.findAll = true;
    }

    public CacheRequest(String rawTypeName) {
        this.findAll = false;
        this.rawTypeName = rawTypeName;
    }

    // region Getter
    public boolean isFindAll() {
        return findAll;
    }

    public String getRawTypeName() {
        return rawTypeName;
    }

    public List<String> getAllRawTypes() {
        return allRawTypes;
    }

    public HashMap<String, ArrayList<String>> getTagFunctionMapping() {
        return tagFunctionMapping;
    }

    // endregion

    // region Setter
    public void setAllRawTypes(List<String> allRawTypes) {
        this.allRawTypes = allRawTypes;
    }

    public void setTagFunctionMapping(HashMap<String, ArrayList<String>> tagFunctionMapping) {
        this.tagFunctionMapping = tagFunctionMapping;
    }
// endregion
}
