package edu.uci.ics.perpetual.predicate;

import edu.uci.ics.perpetual.data.DataObject;

public class Expression<T> {

    private String tag;

    private ComparisionOperator cop;

    private T value;

    public Expression(String tag, ComparisionOperator cop, T value) {
        this.tag = tag;
        this.cop = cop;
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ComparisionOperator getCop() {
        return cop;
    }

    public void setCop(ComparisionOperator cop) {
        this.cop = cop;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean check(DataObject dataObject) {

        switch (cop) {

            case EQ:
                if (value instanceof String) {
                    return dataObject.getObject().get(tag).getAsString().equalsIgnoreCase((String)value);
                } else if (value instanceof Integer) {
                    return dataObject.getObject().get(tag).getAsInt() == (Integer) value;
                } else if (value instanceof Float) {
                    return dataObject.getObject().get(tag).getAsFloat() == (Float) value;
                }
                break;
            case GT:
                if (value instanceof String) {
                    return dataObject.getObject().get(tag).getAsString().compareToIgnoreCase((String)value) > 0;
                } else if (value instanceof Integer) {
                    return dataObject.getObject().get(tag).getAsInt() > (Integer) value;
                } else if (value instanceof Float) {
                    return dataObject.getObject().get(tag).getAsFloat() > (Float) value;
                }
                break;
            case LT:
                if (value instanceof String) {
                    return dataObject.getObject().get(tag).getAsString().compareToIgnoreCase((String)value) < 0;
                } else if (value instanceof Integer) {
                    return dataObject.getObject().get(tag).getAsInt() < (Integer) value;
                } else if (value instanceof Float) {
                    return dataObject.getObject().get(tag).getAsFloat() < (Float) value;
                }
                break;
            case NEQ:
                if (value instanceof String) {
                    return !dataObject.getObject().get(tag).getAsString().equalsIgnoreCase((String)value);
                } else if (value instanceof Integer) {
                    return dataObject.getObject().get(tag).getAsInt() != (Integer) value;
                } else if (value instanceof Float) {
                    return dataObject.getObject().get(tag).getAsFloat() != (Float) value;
                }
                break;

        }

        return false;

    }

    public String toString() {

        return String.format("%s %s %s", tag, cop, value);

    }

}
