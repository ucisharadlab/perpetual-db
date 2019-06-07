
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.expressions.Alias;
import edu.uci.ics.perpetual.expressions.Expression;
import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

/**
 * An expression as in "SELECT expr1 AS EXPR"
 */
public class SelectExpressionItem extends ASTNodeAccessImpl implements SelectItem {

    private Expression expression;
    private Alias alias;

    public SelectExpressionItem() {
    }

    public SelectExpressionItem(Expression expression) {
        this.expression = expression;
    }

    public Alias getAlias() {
        return alias;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void accept(SelectItemVisitor selectItemVisitor) {
        selectItemVisitor.visit(this);
    }

    @Override
    public String toString() {
        return expression + ((alias != null) ? alias.toString() : "");
    }
}
