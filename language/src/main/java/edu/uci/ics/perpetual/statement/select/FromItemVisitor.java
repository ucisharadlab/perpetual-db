
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.schema.Type;

public interface FromItemVisitor {

    void visit(Type typeName);

    void visit(SubSelect subSelect);

    void visit(SubJoin subjoin);

    void visit(LateralSubSelect lateralSubSelect);

    void visit(ValuesList valuesList);

    void visit(TableFunction tableFunction);

    public void visit(ParenthesisFromItem aThis);
}
