
package edu.uci.ics.perpetual.expressions.operators.arithmetic;

import edu.uci.ics.perpetual.expressions.BinaryExpression;
import edu.uci.ics.perpetual.expressions.ExpressionVisitor;

/**
 * Modulo expression (a % b).
 *
 * @author toben
 */
public class Modulo extends BinaryExpression {

    public Modulo() {
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String getStringExpression() {
        return "%";
    }
}
