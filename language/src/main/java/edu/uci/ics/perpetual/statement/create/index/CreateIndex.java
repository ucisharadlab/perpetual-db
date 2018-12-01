
package edu.uci.ics.perpetual.statement.create.index;

import edu.uci.ics.perpetual.schema.*;
import edu.uci.ics.perpetual.statement.*;
import edu.uci.ics.perpetual.statement.create.table.*;

import java.util.*;

/**
 * A "CREATE INDEX" statement
 *
 * @author Raymond Augé
 */
public class CreateIndex implements Statement {

    private Table table;
    private Index index;

    @Override
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    /**
     * The index to be created
     */
    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    /**
     * The table on which the index is to be created
     */
    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        buffer.append("CREATE ");

        if (index.getType() != null) {
            buffer.append(index.getType());
            buffer.append(" ");
        }

        buffer.append("INDEX ");
        buffer.append(index.getName());
        buffer.append(" ON ");
        buffer.append(table.getFullyQualifiedName());

        if (index.getColumnsNames() != null) {
            buffer.append(" (");

            for (Iterator iter = index.getColumnsNames().iterator(); iter.hasNext();) {
                String columnName = (String) iter.next();

                buffer.append(columnName);

                if (iter.hasNext()) {
                    buffer.append(", ");
                }
            }

            buffer.append(")");
        }

        return buffer.toString();
    }

}
