package edu.uci.ics.perpetual.types;

import edu.uci.ics.perpetual.schema.IType;

import java.util.HashMap;

public class RawType implements IType {
    private String name;

    private HashMap<String, String> attributes;

    public RawType(String name, HashMap<String, String> attributes) {
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
}
