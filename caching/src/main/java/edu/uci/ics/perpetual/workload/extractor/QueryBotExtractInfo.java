package edu.uci.ics.perpetual.workload.extractor;

import java.util.HashMap;
import java.util.Map;

public class QueryBotExtractInfo implements IExtractInfo{

    private Map<String, Integer> typeInfo = new HashMap<>();
    private Map<String, Map<String, Integer>> tagInfo = new HashMap();

    public Map<String, Integer> getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(Map<String, Integer> typeInfo) {
        this.typeInfo = typeInfo;
    }

    public Map<String, Map<String, Integer>> getTagInfo() {
        return tagInfo;
    }

    public void setTagInfo(Map<String, Map<String, Integer>> tagInfo) {
        this.tagInfo = tagInfo;
    }
}
