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
        return String.format("Extracted Info From Workload\n---------------------------------------\n" +
                        "Type : Count ==> \n%s\n\nType :: Tag : Count ==>\n%s" +
                        "------------------------------------\n\n",
                new PrettyPrintingMap(typeInfo), new PrettyPrintingMap(tagInfo));
    }

    public String toHTML() {

        StringBuilder sb = new StringBuilder();

        sb.append("Types And Corresponding Number Of Queries\n\n");
        sb.append(new PrettyPrintingMap(typeInfo));
        sb.append("\n\n");

//        return String.format(
//                        "Type : Count ==> \n%s\n\nType :: Tag : Count ==>\n%s" +
//                        "------------------------------------\n\n",
//                new PrettyPrintingMap(typeInfo), new PrettyPrintingMap(tagInfo));
//

        for (String key:tagInfo.keySet()) {
            sb.append(String.format("Extracted Tags And Their Count For Type: %s \n\n", key));
            sb.append(new PrettyPrintingMap(tagInfo.get(key)));
            sb.append("\n\n");
        }

        return sb.toString();
    }

}
