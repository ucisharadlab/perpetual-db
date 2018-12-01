
package edu.uci.ics.perpetual.expression;

import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

/**
 * It represents an expression like "(" expression ")"
 */
public class Parenthesis extends ASTNodeAccessImpl implements Expression {

    private Expression expression;
    
    private boolean not = false;

    public Parenthesis() {
    }

    public Parenthesis(Expression expression) {
        setExpression(expression);
    }

    public Expression getExpression() {
        return expression;
    }

    public final void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public void setNot() {
        not = true;
    }

    public boolean isNot() {
        return not;
    }

    @Override
    public String toString() {
        return (not ? "NOT " : "") + "(" + expression + ")";
    }
}
