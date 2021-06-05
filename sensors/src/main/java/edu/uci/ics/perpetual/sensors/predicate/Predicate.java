package edu.uci.ics.perpetual.sensors.predicate;

import java.util.List;

public class Predicate {
    public String field;
    public String value;
    public Condition condition;

    public RelationalOperator childOperator;
    public List<Predicate> children;

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(Constants.OPEN_PARENTHESIS
                + field + Constants.SPACE + condition.toString() + Constants.SPACE + value);

        if (!children.isEmpty())
            str.append(childOperator.toString()).append(Constants.SPACE).append(getChildPredicates());

        str.append(Constants.CLOSE_PARENTHESIS);
        return str.toString();
    }

    protected String getChildPredicates() {
        return Constants.OPEN_PARENTHESIS + childOperator.combine(children) + Constants.CLOSE_PARENTHESIS;
    }
}
