package edu.uci.ics.perpetual.table;

public class Attribute {
    private AttributeKind kind;

    private String key;

    private String value;

    // region constructor wrapper
    private Attribute(AttributeKind kind, String key, String value) {
        this.kind = kind;
        this.key = key;
        this.value = value;
    }

    public static Attribute of(AttributeKind kind,  String key, String value) {
        return new Attribute(kind, key, value);
    }

    public static Attribute parameterAttribute(String key, String value) {
        return new Attribute(AttributeKind.PARAMETER, key, value);
    }

    public static Attribute valueAttribute(String key, String value) {
        return new Attribute(AttributeKind.VALUE, key, value);
    }
    // endregion

    // region getter and setter
    public AttributeKind getKind() {
        return kind;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    // endregion
}
