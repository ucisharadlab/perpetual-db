
package edu.uci.ics.perpetual.util.deparser;

import edu.uci.ics.perpetual.statement.create.type.ColumnDefinition;
import edu.uci.ics.perpetual.statement.create.type.CreateMetadataType;
import edu.uci.ics.perpetual.statement.create.type.Index;
import edu.uci.ics.perpetual.statement.select.PlainSelect;
import edu.uci.ics.perpetual.statement.select.Select;

import java.util.Iterator;


/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a string) a
 * {@link CreateMetadataType}
 */
public class CreateTableDeParser {

    protected StringBuilder buffer;
    private StatementDeParser statementDeParser;

    /**
     * @param buffer the buffer that will be filled with the select
     */
    public CreateTableDeParser(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public CreateTableDeParser(StatementDeParser statementDeParser, StringBuilder buffer) {
        this.buffer = buffer;
        this.statementDeParser = statementDeParser;
    }


    public void deParse(CreateMetadataType createMetadataType) {
        buffer.append("CREATE ");
        if (createMetadataType.isUnlogged()) {
            buffer.append("UNLOGGED ");
        }
        String params = PlainSelect.
                getStringList(createMetadataType.getCreateOptionsStrings(), false, false);
        if (!"".equals(params)) {
            buffer.append(params).append(' ');
        }

        buffer.append("TABLE ");
        if (createMetadataType.isIfNotExists()) {
            buffer.append("IF NOT EXISTS ");
        }
        buffer.append(createMetadataType.getType().getFullyQualifiedName());
        if (createMetadataType.getSelect() != null) {
            buffer.append(" AS ");
            if (createMetadataType.isSelectParenthesis()) {
                buffer.append("(");
            }
            Select sel = createMetadataType.getSelect();
            sel.accept(this.statementDeParser);
            if (createMetadataType.isSelectParenthesis()) {
                buffer.append(")");
            }
        } else {
            if (createMetadataType.getColumnDefinitions() != null) {
                buffer.append(" (");
                for (Iterator<ColumnDefinition> iter = createMetadataType.getColumnDefinitions().iterator(); iter.
                        hasNext();) {
                    ColumnDefinition columnDefinition = iter.next();
                    buffer.append(columnDefinition.getColumnName());
                    buffer.append(" ");
                    buffer.append(columnDefinition.getColDataType().toString());
                    if (columnDefinition.getColumnSpecStrings() != null) {
                        for (String s : columnDefinition.getColumnSpecStrings()) {
                            buffer.append(" ");
                            buffer.append(s);
                        }
                    }

                    if (iter.hasNext()) {
                        buffer.append(", ");
                    }
                }

                if (createMetadataType.getIndexes() != null) {
                    for (Iterator<Index> iter = createMetadataType.getIndexes().iterator(); iter.hasNext();) {
                        buffer.append(", ");
                        Index index = iter.next();
                        buffer.append(index.toString());
                    }
                }

                buffer.append(")");
            }
        }

        params = PlainSelect.getStringList(createMetadataType.getTypeOptionsStrings(), false, false);
        if (!"".equals(params)) {
            buffer.append(' ').append(params);
        }
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }
}
