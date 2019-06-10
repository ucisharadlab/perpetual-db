
package edu.uci.ics.perpetual.expressions.operators.relational;

import java.util.Arrays;
import java.util.List;

import edu.uci.ics.perpetual.expressions.Expression;
import edu.uci.ics.perpetual.statement.select.PlainSelect;

/**
 * A list of expressions, as in SELECT A FROM TAB WHERE B IN (expr1,expr2,expr3)
 */
public class ExpressionList implements ItemsList {

    private List<Expression> expressions;

    public ExpressionList() {
    }

    public ExpressionList(List<Expression> expressions) {
        this.expressions = expressions;
    }
    
    public ExpressionList(Expression ... expressions) {
        this.expressions = Arrays.asList(expressions);
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<Expression> list) {
        expressions = list;
    }

    @Override
    public void accept(ItemsListVisitor itemsListVisitor) {
        itemsListVisitor.visit(this);
    }

    @Override
    public String toString() {
        return PlainSelect.getStringList(expressions, true, true);
    }
}
