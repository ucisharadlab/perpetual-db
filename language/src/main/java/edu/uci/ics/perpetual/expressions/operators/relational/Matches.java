
package edu.uci.ics.perpetual.expressions.operators.relational;

import edu.uci.ics.perpetual.expressions.ExpressionVisitor;

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
