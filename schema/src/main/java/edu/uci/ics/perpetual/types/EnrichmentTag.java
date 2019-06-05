package edu.uci.ics.perpetual.types;

import edu.uci.ics.perpetual.schema.IType;

import java.util.List;
import java.util.Set;

public class EnrichmentTag implements IType {

    private String name;

    private String type;

    public EnrichmentTag(String name, String type) {
        this.name = name;
        this.type = type;
    }

    // region getter
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
    // endregion
}
