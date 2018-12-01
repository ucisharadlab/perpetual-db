
package edu.uci.ics.perpetual.expression.operators.relational;

import edu.uci.ics.perpetual.expression.ExpressionVisitor;

public class GreaterThanEquals extends ComparisonOperator {

    public GreaterThanEquals() {
        super(">=");
    }

    public GreaterThanEquals(String operator) {
        super(operator);
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }
}
