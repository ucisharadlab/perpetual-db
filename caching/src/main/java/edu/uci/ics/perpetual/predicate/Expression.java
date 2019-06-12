package edu.uci.ics.perpetual.predicate;

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

    public String toString() {

        return String.format("%s %s %s", tag, cop, value);

    }

}
