package edu.uci.ics.perpetual.types;

import edu.uci.ics.perpetual.table.Attribute;

import java.util.List;

public class MetadataType {
    private String name;

    private List<Attribute> attributes;

    public MetadataType(String name, List<Attribute> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    // region getter and setter
    public String getName() {
        return name;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    // endregion
}
