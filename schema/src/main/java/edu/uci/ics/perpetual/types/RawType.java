package edu.uci.ics.perpetual.types;

import edu.uci.ics.perpetual.schema.IType;
import edu.uci.ics.perpetual.table.Attribute;

import java.util.List;

public class RawType implements IType {
    private String name;

    private List<Attribute> attributes;

    public RawType(String name, List<Attribute> attributes) {
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
//        sb.append(name).append(": ");
        for (Attribute att: attributes) {
            sb.append(String.format("(%s,%s),", att.getKey(), att.getValue()));
        }

        return sb.toString();

    }
}
