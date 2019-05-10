
package edu.uci.ics.perpetual.statement.select;

public interface SelectItemVisitor {

    void visit(AllColumns allColumns);

    void visit(AllTypeColumns allTypeColumns);

    void visit(SelectExpressionItem selectExpressionItem);
}
