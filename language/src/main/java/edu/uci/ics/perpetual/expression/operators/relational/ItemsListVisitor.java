
package edu.uci.ics.perpetual.expression.operators.relational;

import edu.uci.ics.perpetual.statement.select.SubSelect;

public interface ItemsListVisitor {

    void visit(SubSelect subSelect);

    void visit(ExpressionList expressionList);

    void visit(NamedExpressionList namedExpressionList);

    void visit(MultiExpressionList multiExprList);
}
