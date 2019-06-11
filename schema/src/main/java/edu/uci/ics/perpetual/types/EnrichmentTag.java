package edu.uci.ics.perpetual.types;

import edu.uci.ics.perpetual.schema.IType;

import java.util.List;
import java.util.Set;

public class EnrichmentTag implements IType {

    private String name;

    private String type;

    private String rawType;

    public EnrichmentTag(String name, String type, String rawType) {
        this.name = name;
        this.type = type;
        this.rawType = rawType;
    }

    // region getter
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
    // endregion

    public String toString() {
        StringBuilder sb = new StringBuilder();
//        sb.append(name).append(" : ");
        sb.append(rawType);
        return sb.toString();

    }
}
