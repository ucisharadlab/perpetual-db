
package edu.uci.ics.perpetual.expression.operators.relational;

import edu.uci.ics.perpetual.expression.ExpressionVisitor;

public class EqualsTo extends ComparisonOperator {

    public EqualsTo() {
        super("=");
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }
}
