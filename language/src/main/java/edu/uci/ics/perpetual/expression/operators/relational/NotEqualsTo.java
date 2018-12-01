
package edu.uci.ics.perpetual.expression.operators.relational;

import edu.uci.ics.perpetual.expression.ExpressionVisitor;

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
