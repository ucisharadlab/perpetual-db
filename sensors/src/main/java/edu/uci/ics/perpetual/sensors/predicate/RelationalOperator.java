package edu.uci.ics.perpetual.sensors.predicate;

import java.util.List;
import java.util.stream.Collectors;

public abstract class RelationalOperator {
    public static String AND = "AND";
    public static String OR = "OR";
    public static String NOT = "NOT";

    @Override
    public abstract String toString();

    public String combine(List<Predicate> predicates) {
        return Constants.OPEN_PARENTHESIS
                + predicates.stream().map(Predicate::toSql)
                        .collect(Collectors.joining(Constants.SPACE + this + Constants.SPACE))
                + Constants.CLOSE_PARENTHESIS;
    }
}
