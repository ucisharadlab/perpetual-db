package edu.uci.ics.perpetual.sensors.predicate;

import java.util.LinkedList;
import java.util.List;

public class Predicate {
    public String lhs;
    public String rhs;
    public String condition;

    public String childOperator;
    public List<Predicate> children;

    public Predicate() {}

    public Predicate(String lhs, String condition, String rhs) {
        this.lhs = lhs;
        this.condition = condition;
        this.rhs = rhs;
    }

    public static Predicate getWrapper(String childOperator) {
        Predicate filter = new Predicate();
        filter.initialiseOperator(childOperator);
        return filter;
    }

    public void initialiseOperator(String childOperator) {
        this.childOperator = childOperator;
        this.children = new LinkedList<>();
    }

    public void addChild(Predicate predicate) {
        if (null == predicate) return;
        this.children.add(predicate);
    }

    public String toSql() {
        StringBuilder str = new StringBuilder(Constants.OPEN_PARENTHESIS);
        boolean hasFilter = lhs != null && !lhs.isEmpty();
        boolean hasChildren = children != null && !children.isEmpty();

        if (!hasFilter && !hasChildren)
            return "";

        if (hasFilter)
            str.append(lhs).append(Constants.SPACE).append(condition).append(Constants.SPACE).append(rhs);

        if (hasFilter && hasChildren)
            str.append(Constants.SPACE).append(childOperator);

        if (hasChildren)
            str.append(Constants.SPACE).append(getChildPredicates());

        str.append(Constants.CLOSE_PARENTHESIS);
        return str.toString();
    }

    protected String getChildPredicates() {
        return RelationalOperator.operate(childOperator, children);
    }
}
