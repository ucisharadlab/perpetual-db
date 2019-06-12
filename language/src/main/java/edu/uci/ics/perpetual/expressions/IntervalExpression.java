
package edu.uci.ics.perpetual.expressions;

import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

/**
 *
 * @author wumpz
 */
public class IntervalExpression extends ASTNodeAccessImpl implements Expression {

    private String parameter = null;
    private String intervalType = null;

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getIntervalType() {
        return intervalType;
    }

    public void setIntervalType(String intervalType) {
        this.intervalType = intervalType;
    }

    @Override
    public String toString() {
        return "INTERVAL " + parameter + (intervalType != null ? " " + intervalType : "");
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }
}
