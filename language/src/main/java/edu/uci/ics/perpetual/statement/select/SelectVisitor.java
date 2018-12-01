
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.statement.values.ValuesStatement;

public interface SelectVisitor {

    void visit(PlainSelect plainSelect);

    void visit(SetOperationList setOpList);

    void visit(WithItem withItem);

    void visit(ValuesStatement aThis);
}
