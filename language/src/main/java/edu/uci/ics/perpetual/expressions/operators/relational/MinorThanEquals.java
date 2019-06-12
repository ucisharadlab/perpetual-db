
package edu.uci.ics.perpetual.expressions.operators.relational;

import edu.uci.ics.perpetual.expressions.ExpressionVisitor;

public class MinorThanEquals extends ComparisonOperator {

    public MinorThanEquals() {
        super("<=");
    }

    public MinorThanEquals(String operator) {
        super(operator);
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }
}
