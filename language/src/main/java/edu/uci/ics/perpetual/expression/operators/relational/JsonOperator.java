
package edu.uci.ics.perpetual.expression.operators.relational;

import edu.uci.ics.perpetual.expression.BinaryExpression;
import edu.uci.ics.perpetual.expression.ExpressionVisitor;

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
