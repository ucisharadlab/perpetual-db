
package edu.uci.ics.perpetual.expression;

import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

/**
 * Every number with a point or a exponential format is a DoubleValue
 */
public class HexValue extends ASTNodeAccessImpl implements Expression {

    private String stringValue;

    public HexValue(final String value) {
        String val = value;
        this.stringValue = val;
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public String getValue() {
        return stringValue;
    }

    public void setValue(String d) {
        stringValue = d;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
