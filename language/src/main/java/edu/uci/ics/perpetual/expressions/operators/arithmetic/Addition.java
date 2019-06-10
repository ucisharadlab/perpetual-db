
package edu.uci.ics.perpetual.expressions.operators.arithmetic;

import edu.uci.ics.perpetual.expressions.BinaryExpression;
import edu.uci.ics.perpetual.expressions.ExpressionVisitor;

public class Addition extends BinaryExpression {

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String getStringExpression() {
        return "+";
    }
}
