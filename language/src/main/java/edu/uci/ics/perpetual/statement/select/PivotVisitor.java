
package edu.uci.ics.perpetual.statement.select;

public interface PivotVisitor {

    void visit(Pivot pivot);

    void visit(PivotXml pivot);

}
