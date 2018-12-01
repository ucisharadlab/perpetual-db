
package edu.uci.ics.perpetual.expression.operators.arithmetic;

import edu.uci.ics.perpetual.expression.BinaryExpression;
import edu.uci.ics.perpetual.expression.ExpressionVisitor;

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
