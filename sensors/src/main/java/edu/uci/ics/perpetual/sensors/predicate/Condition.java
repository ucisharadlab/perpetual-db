package edu.uci.ics.perpetual.sensors.predicate;

public class Condition {
    public static String GREATER = ">";
    public static String LESSER = "<";
    public static String EQUAL = "=";
    public static String GREATER_EQ = ">=";
    public static String LESSER_EQ = "<=";
    public static String NOT_EQUAL = "<>";

    public String condition;

    public Condition(String condition){
        this.condition = condition;
    }

    @Override
    public String toString() {
        return condition;
    }
}
