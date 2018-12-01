
package edu.uci.ics.perpetual.expression;

import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;
import edu.uci.ics.perpetual.statement.select.SubSelect;

public class AllComparisonExpression extends ASTNodeAccessImpl implements Expression {

    private final SubSelect subSelect;

    public AllComparisonExpression(SubSelect subSelect) {
        this.subSelect = subSelect;
    }

    public SubSelect getSubSelect() {
        return subSelect;
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return "ALL " + subSelect.toString();
    }
}
