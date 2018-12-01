
package edu.uci.ics.perpetual.expression.operators.conditional;

import edu.uci.ics.perpetual.expression.BinaryExpression;
import edu.uci.ics.perpetual.expression.Expression;
import edu.uci.ics.perpetual.expression.ExpressionVisitor;

public class OrExpression extends BinaryExpression {

    public OrExpression(Expression leftExpression, Expression rightExpression) {
        setLeftExpression(leftExpression);
        setRightExpression(rightExpression);
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String getStringExpression() {
        return "OR";
    }
}
