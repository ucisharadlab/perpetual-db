package edu.uci.ics.perpetual.types;

import java.util.HashMap;

public class MetadataType {
    private String name;

    private HashMap<String, String> attributes;

    public MetadataType(String name, HashMap<String, String> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, String> getAttributes() {
        return attributes;
    }
}
