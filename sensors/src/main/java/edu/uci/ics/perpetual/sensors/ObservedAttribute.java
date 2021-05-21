package edu.uci.ics.perpetual.sensors;

public class ObservedAttribute {
    String name;
    String valueType;
    String value;

    ObservedAttribute(String name, String valueType, String value) {
        this.name = name;
        this.valueType = valueType;
        this.value = value;
    }
}
