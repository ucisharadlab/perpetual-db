
package edu.uci.ics.perpetual.expressions.operators.relational;

import edu.uci.ics.perpetual.expressions.ExpressionVisitor;

public class MinorThan extends ComparisonOperator {

    public MinorThan() {
        super("<");
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }
}
