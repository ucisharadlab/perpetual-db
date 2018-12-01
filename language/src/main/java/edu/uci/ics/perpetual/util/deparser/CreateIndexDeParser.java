
package edu.uci.ics.perpetual.util.deparser;

import java.util.Iterator;

import edu.uci.ics.perpetual.statement.create.index.CreateIndex;
import edu.uci.ics.perpetual.statement.create.table.Index;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a string) a
 * {@link edu.uci.ics.perpetual.statement.create.index.CreateIndex}
 *
 * @author Raymond Augé
 */
public class CreateIndexDeParser {

    protected StringBuilder buffer;

    /**
     * @param buffer the buffer that will be filled with the create
     */
    public CreateIndexDeParser(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public void deParse(CreateIndex createIndex) {
        Index index = createIndex.getIndex();

        buffer.append("CREATE ");

        if (index.getType() != null) {
            buffer.append(index.getType());
            buffer.append(" ");
        }

        buffer.append("INDEX ");
        buffer.append(index.getName());
        buffer.append(" ON ");
        buffer.append(createIndex.getTable().getFullyQualifiedName());

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
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

}
