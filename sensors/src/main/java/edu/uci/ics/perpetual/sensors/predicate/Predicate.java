package edu.uci.ics.perpetual.sensors.predicate;

import java.util.List;

public class Predicate {
    public String field;
    public String value;
    public String condition;

    public String childOperator;
    public List<Predicate> children;

    public Predicate() {}

    public Predicate(String field, String condition, String value) {
        this.field = field;
        this.condition = condition;
        this.value = value;
    }

    public String toSql() {
        StringBuilder str = new StringBuilder(Constants.OPEN_PARENTHESIS);
        if (field != null && !field.isEmpty())
            str.append(field).append(Constants.SPACE).append(condition).append(Constants.SPACE).append(value);

        if (children != null && !children.isEmpty())
            str.append(Constants.SPACE).append(childOperator).append(Constants.SPACE).append(getChildPredicates());

        str.append(Constants.CLOSE_PARENTHESIS);
        return str.toString();
    }

    protected String getChildPredicates() {
        return RelationalOperator.operate(childOperator, children);
    }
}
