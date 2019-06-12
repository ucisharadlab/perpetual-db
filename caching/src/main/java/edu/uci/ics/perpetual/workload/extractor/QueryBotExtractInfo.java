package edu.uci.ics.perpetual.workload.extractor;

import edu.uci.ics.perpetual.util.PrettyPrintingMap;

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

    public String toString() {
        return String.format("Type : Count ==> \n%s\n\nType :: Tag : Count ==>\n%s\n\n\n",
                new PrettyPrintingMap(typeInfo), new PrettyPrintingMap(tagInfo));
    }

}
