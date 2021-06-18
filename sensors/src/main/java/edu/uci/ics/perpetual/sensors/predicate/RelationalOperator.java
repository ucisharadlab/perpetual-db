package edu.uci.ics.perpetual.sensors.predicate;

import java.util.List;
import java.util.stream.Collectors;

public class RelationalOperator {
    public static String AND = "AND";
    public static String OR = "OR";
    public static String NOT = "NOT";

    public static String binaryCombine(String operator, List<Predicate> predicates) {
        return Constants.OPEN_PARENTHESIS
                + predicates.stream().map(Predicate::toSql)
                        .collect(Collectors.joining(Constants.SPACE + operator + Constants.SPACE))
                + Constants.CLOSE_PARENTHESIS;
    }

    public static String unaryCombine(String operator, List<Predicate> predicates) {
        if (predicates.size() < 1)
            return null; // exception?

        return Constants.OPEN_PARENTHESIS
                + predicates.get(0).toSql() // if list has more than one element, the rest are ignored
                + Constants.CLOSE_PARENTHESIS;
    }

    public static String operate(String operator, List<Predicate> predicates) {
        if (NOT.equals(operator))
            return unaryCombine(operator, predicates);
        return binaryCombine(operator, predicates);
    }
}
