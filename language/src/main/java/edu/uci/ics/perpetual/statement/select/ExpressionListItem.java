
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.expressions.Alias;
import edu.uci.ics.perpetual.expressions.operators.relational.ExpressionList;

public class ExpressionListItem {

    private ExpressionList expressionList;
    private Alias alias;

    public ExpressionList getExpressionList() {
        return expressionList;
    }

    public void setExpressionList(ExpressionList expressionList) {
        this.expressionList = expressionList;
    }

    public Alias getAlias() {
        return alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return expressionList + ((alias != null) ? alias.toString() : "");
    }
}
