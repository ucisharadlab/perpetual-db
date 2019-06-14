package edu.uci.ics.perpetual.request;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * THe CacheManager is a intermediary that uses for communicating
 * between SchemaManager and CacheManager
 */
public class CacheRequest extends Request {

    private boolean findAll;

    private List<String> allRawTypes;

    private HashMap<String, ArrayList<Pair<String, Integer>>> tagFunctionMapping;

    private String rawTypeName;

    public CacheRequest() {
        this.findAll = true;
    }

    public CacheRequest(String rawTypeName) {
        this.findAll = false;
        this.rawTypeName = rawTypeName;
    }

    public boolean isFindAll() {
        return findAll;
    }

    public String getRawTypeName() {
        return rawTypeName;
    }

    public List<String> getAllRawTypes() {
        return allRawTypes;
    }

    public HashMap<String, ArrayList<Pair<String, Integer>>> getTagFunctionMapping() {
        return tagFunctionMapping;
    }

    public void setAllRawTypes(List<String> allRawTypes) {
        this.allRawTypes = allRawTypes;
    }

    public void setTagFunctionMapping(HashMap<String, ArrayList<Pair<String, Integer>>> tagFunctionMapping) {
        this.tagFunctionMapping = tagFunctionMapping;
    }
}
