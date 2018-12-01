
package edu.uci.ics.perpetual.expression;

import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;
import edu.uci.ics.perpetual.statement.select.SubSelect;

/**
 * Combines ANY and SOME expressions.
 *
 * @author toben
 */
public class AnyComparisonExpression extends ASTNodeAccessImpl implements Expression {

    private final SubSelect subSelect;
    private final AnyType anyType;

    public AnyComparisonExpression(AnyType anyType, SubSelect subSelect) {
        this.anyType = anyType;
        this.subSelect = subSelect;
    }

    public SubSelect getSubSelect() {
        return subSelect;
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public AnyType getAnyType() {
        return anyType;
    }

    @Override
    public String toString() {
        return anyType.name() + " " + subSelect.toString();
    }
}
