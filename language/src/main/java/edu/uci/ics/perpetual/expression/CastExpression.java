
package edu.uci.ics.perpetual.expression;

import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;
import edu.uci.ics.perpetual.statement.create.table.ColDataType;

/**
 *
 * @author tw
 */
public class CastExpression extends ASTNodeAccessImpl implements Expression {

    private Expression leftExpression;
    private ColDataType type;
    private boolean useCastKeyword = true;

    public ColDataType getType() {
        return type;
    }

    public void setType(ColDataType type) {
        this.type = type;
    }

    public Expression getLeftExpression() {
        return leftExpression;
    }

    public void setLeftExpression(Expression expression) {
        leftExpression = expression;
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public boolean isUseCastKeyword() {
        return useCastKeyword;
    }

    public void setUseCastKeyword(boolean useCastKeyword) {
        this.useCastKeyword = useCastKeyword;
    }

    @Override
    public String toString() {
        if (useCastKeyword) {
            return "CAST(" + leftExpression + " AS " + type.toString() + ")";
        } else {
            return leftExpression + "::" + type.toString();
        }
    }
}
