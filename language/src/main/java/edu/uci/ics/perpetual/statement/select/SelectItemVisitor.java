
package edu.uci.ics.perpetual.statement.select;

public interface SelectItemVisitor {

    void visit(AllColumns allColumns);

    void visit(AllTableColumns allTableColumns);

    void visit(SelectExpressionItem selectExpressionItem);
}
