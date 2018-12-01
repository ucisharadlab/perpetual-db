
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.schema.Table;

public interface IntoTableVisitor {

    void visit(Table tableName);
}
