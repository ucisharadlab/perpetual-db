
package edu.uci.ics.perpetual.expressions.operators.relational;

import edu.uci.ics.perpetual.expressions.Expression;
import edu.uci.ics.perpetual.expressions.ExpressionVisitor;
import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

public class ExistsExpression extends ASTNodeAccessImpl implements Expression {

    private Expression rightExpression;
    private boolean not = false;

    public Expression getRightExpression() {
        return rightExpression;
    }

    public void setRightExpression(Expression expression) {
        rightExpression = expression;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean b) {
        not = b;
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public String getStringExpression() {
        return (not ? "NOT " : "") + "EXISTS";
    }

    @Override
    public String toString() {
        return getStringExpression() + " " + rightExpression.toString();
    }
}
