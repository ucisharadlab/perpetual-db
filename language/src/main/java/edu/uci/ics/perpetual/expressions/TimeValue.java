
package edu.uci.ics.perpetual.expressions;

import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

import java.sql.Time;

/**
 * A Time in the form {t 'hh:mm:ss'}
 */
public class TimeValue extends ASTNodeAccessImpl implements Expression {

    private Time value;

    public TimeValue(String value) {
        this.value = Time.valueOf(value.substring(1, value.length() - 1));
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public Time getValue() {
        return value;
    }

    public void setValue(Time d) {
        value = d;
    }

    @Override
    public String toString() {
        return "{t '" + value + "'}";
    }
}
