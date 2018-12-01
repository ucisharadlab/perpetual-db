
package edu.uci.ics.perpetual.expression.operators.relational;

import edu.uci.ics.perpetual.expression.Expression;
import edu.uci.ics.perpetual.expression.ExpressionVisitor;
import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

public class IsNullExpression extends ASTNodeAccessImpl implements Expression {

    private Expression leftExpression;
    private boolean not = false;
    private boolean useIsNull = false;

    public Expression getLeftExpression() {
        return leftExpression;
    }

    public boolean isNot() {
        return not;
    }

    public void setLeftExpression(Expression expression) {
        leftExpression = expression;
    }

    public void setNot(boolean b) {
        not = b;
    }

    public boolean isUseIsNull() {
        return useIsNull;
    }

    public void setUseIsNull(boolean useIsNull) {
        this.useIsNull = useIsNull;
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        if (isUseIsNull()) {
            return leftExpression + (not ? " NOT" : "") + " ISNULL";
        } else {
            return leftExpression + " IS " + (not ? "NOT " : "") + "NULL";
        }
    }
}
