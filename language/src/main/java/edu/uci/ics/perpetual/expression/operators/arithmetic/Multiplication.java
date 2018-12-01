
package edu.uci.ics.perpetual.expression.operators.arithmetic;

import edu.uci.ics.perpetual.expression.BinaryExpression;
import edu.uci.ics.perpetual.expression.ExpressionVisitor;

public class Multiplication extends BinaryExpression {

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String getStringExpression() {
        return "*";
    }
}
