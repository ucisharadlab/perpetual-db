
package edu.uci.ics.perpetual.expression;

import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

import java.sql.Date;

/**
 * A Date in the form {d 'yyyy-mm-dd'}
 */
public class DateValue extends ASTNodeAccessImpl implements Expression {

    private Date value;

    public DateValue(String value) {
        this.value = Date.valueOf(value.substring(1, value.length() - 1));
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public Date getValue() {
        return value;
    }

    public void setValue(Date d) {
        value = d;
    }

    @Override
    public String toString() {
        return "{d '" + value.toString() + "'}";
    }
}
