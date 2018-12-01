
package edu.uci.ics.perpetual.expression.operators.relational;

import edu.uci.ics.perpetual.expression.ExpressionVisitor;

public class GreaterThan extends ComparisonOperator {

    public GreaterThan() {
        super(">");
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }
}
