
package edu.uci.ics.perpetual.expressions.operators.relational;

import edu.uci.ics.perpetual.expressions.BinaryExpression;
import edu.uci.ics.perpetual.expressions.ExpressionVisitor;

public class JsonOperator extends BinaryExpression {

    private String op; //"@>"

    public JsonOperator(String op) {
        this.op = op;
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String getStringExpression() {
        return op;
    }
}
