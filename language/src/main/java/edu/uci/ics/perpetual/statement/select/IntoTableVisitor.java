
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.schema.Type;

public interface IntoTableVisitor {

    void visit(Type typeName);
}
