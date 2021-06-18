package edu.uci.ics.perpetual.sensors.predicate;

import java.util.List;

public class RelationalNot extends RelationalOperator {
    @Override
    public String toString() {
        return NOT;
    }

    @Override
    public String combine(List<Predicate> predicates) {
        if (predicates.size() > 1)
            return null; // exception?

        return Constants.OPEN_PARENTHESIS
                + predicates.get(0).toSql()
                + Constants.CLOSE_PARENTHESIS;
    }
}
