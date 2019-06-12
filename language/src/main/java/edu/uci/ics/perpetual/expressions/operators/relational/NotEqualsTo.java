
package edu.uci.ics.perpetual.expressions.operators.relational;

import edu.uci.ics.perpetual.expressions.ExpressionVisitor;

public class NotEqualsTo extends ComparisonOperator {

    public NotEqualsTo() {
        super("<>");
    }

    public NotEqualsTo(String operator) {
        super(operator);
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }
}
