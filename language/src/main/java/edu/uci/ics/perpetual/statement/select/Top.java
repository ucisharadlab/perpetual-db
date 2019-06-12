
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.expressions.Expression;

/**
 * A top clause in the form [TOP (row_count) or TOP row_count]
 */
public class Top {

    private boolean hasParenthesis = false;
    private boolean isPercentage = false;
    private Expression expression;

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public boolean hasParenthesis() {
        return hasParenthesis;
    }

    public void setParenthesis(boolean hasParenthesis) {
        this.hasParenthesis = hasParenthesis;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    public void setPercentage(boolean percentage) {
        this.isPercentage = percentage;
    }

    @Override
    public String toString() {
        String result = "TOP ";

        if (hasParenthesis) {
            result += "(";
        }

        result += expression.toString();

        if (hasParenthesis) {
            result += ")";
        }

        if (isPercentage) {
            result += " PERCENT";
        }

        return result;
    }
}
