package edu.uci.ics.perpetual.types;

import java.util.HashMap;
import java.util.Map;

public class MetadataType {
    private String name;

    private HashMap<String, String> attributes;

    public MetadataType(String name, HashMap<String, String> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    // region getter and setter
    public String getName() {
        return name;
    }

    public HashMap<String, String> getAttributes() {
        return attributes;
    }
    // endregion

    public String toString() {
        StringBuilder sb = new StringBuilder();
//        sb.append(name).append(": ");
        for (Map.Entry att: attributes.entrySet()) {
            sb.append(String.format("(%s,%s),", att.getKey(), att.getValue()));
        }

        return sb.toString();

    }

}
