package edu.uci.ics.perpetual.sensors.model;

public class ObservedAttribute {
    public String name;
    public String valueType;
    public String value;

    public ObservedAttribute(String name, String valueType, String value) {
        this.name = name;
        this.valueType = valueType;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj || this.getClass() != obj.getClass())
            return false;

        final ObservedAttribute that = (ObservedAttribute) obj;
        return this.name.equals(that.name)
                // && this.valueType.equals(that.valueType)
                && this.value.equals(that.value);
    }
}
