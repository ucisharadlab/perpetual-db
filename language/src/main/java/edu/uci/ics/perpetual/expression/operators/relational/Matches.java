
package edu.uci.ics.perpetual.expression.operators.relational;

import edu.uci.ics.perpetual.expression.ExpressionVisitor;

public class Matches extends OldOracleJoinBinaryExpression {

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String getStringExpression() {
        return "@@";
    }
}
