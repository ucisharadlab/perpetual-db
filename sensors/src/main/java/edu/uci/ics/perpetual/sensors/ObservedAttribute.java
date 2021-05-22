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

    @Override
    public boolean equals(Object obj) {
        if (null == obj || this.getClass() != obj.getClass())
            return false;

        final ObservedAttribute that = (ObservedAttribute) obj;
        return this.name.equals(that.name)
                && this.valueType.equals(that.valueType)
                && this.value.equals(that.value);
    }
}
