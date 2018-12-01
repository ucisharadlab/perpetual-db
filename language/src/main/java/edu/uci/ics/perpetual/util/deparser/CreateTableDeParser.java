
package edu.uci.ics.perpetual.util.deparser;

import java.util.Iterator;

import edu.uci.ics.perpetual.statement.create.table.ColumnDefinition;
import edu.uci.ics.perpetual.statement.create.table.CreateTable;
import edu.uci.ics.perpetual.statement.create.table.Index;
import edu.uci.ics.perpetual.statement.select.PlainSelect;
import edu.uci.ics.perpetual.statement.select.Select;


/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a string) a
 * {@link edu.uci.ics.perpetual.statement.create.table.CreateTable}
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


    public void deParse(CreateTable createTable) {
        buffer.append("CREATE ");
        if (createTable.isUnlogged()) {
            buffer.append("UNLOGGED ");
        }
        String params = PlainSelect.
                getStringList(createTable.getCreateOptionsStrings(), false, false);
        if (!"".equals(params)) {
            buffer.append(params).append(' ');
        }

        buffer.append("TABLE ");
        if (createTable.isIfNotExists()) {
            buffer.append("IF NOT EXISTS ");
        }
        buffer.append(createTable.getTable().getFullyQualifiedName());
        if (createTable.getSelect() != null) {
            buffer.append(" AS ");
            if (createTable.isSelectParenthesis()) {
                buffer.append("(");
            }
            Select sel = createTable.getSelect();
            sel.accept(this.statementDeParser);
            if (createTable.isSelectParenthesis()) {
                buffer.append(")");
            }
        } else {
            if (createTable.getColumnDefinitions() != null) {
                buffer.append(" (");
                for (Iterator<ColumnDefinition> iter = createTable.getColumnDefinitions().iterator(); iter.
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

                if (createTable.getIndexes() != null) {
                    for (Iterator<Index> iter = createTable.getIndexes().iterator(); iter.hasNext();) {
                        buffer.append(", ");
                        Index index = iter.next();
                        buffer.append(index.toString());
                    }
                }

                buffer.append(")");
            }
        }

        params = PlainSelect.getStringList(createTable.getTableOptionsStrings(), false, false);
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
