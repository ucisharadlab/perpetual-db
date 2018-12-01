
package edu.uci.ics.perpetual.expression.operators.relational;

public abstract class ComparisonOperator extends OldOracleJoinBinaryExpression {

    private final String operator;

    public ComparisonOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String getStringExpression() {
        return operator;
    }
}
