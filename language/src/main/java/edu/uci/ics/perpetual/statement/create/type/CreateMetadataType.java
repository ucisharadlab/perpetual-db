
package edu.uci.ics.perpetual.statement.create.type;

import java.util.List;

import edu.uci.ics.perpetual.schema.Type;
import edu.uci.ics.perpetual.statement.Statement;
import edu.uci.ics.perpetual.statement.StatementVisitor;
import edu.uci.ics.perpetual.statement.select.PlainSelect;
import edu.uci.ics.perpetual.statement.select.Select;

/**
 * A "CREATE METADATA TYPE" statement
 */
public class CreateMetadataType implements Statement {

    private Type type;
    private boolean unlogged = false;
    private List<String> createOptionsStrings;
    private List<String> typeOptionsStrings;
    private List<ColumnDefinition> columnDefinitions;
    private List<Index> indexes;
    private Select select;
    private boolean selectParenthesis;
    private boolean ifNotExists = false;

    @Override
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    /**
     * The name of the type to be created
     */
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Whether the type is unlogged or not (PostgreSQL 9.1+ feature)
     *
     * @return
     */
    public boolean isUnlogged() {
        return unlogged;
    }

    public void setUnlogged(boolean unlogged) {
        this.unlogged = unlogged;
    }

    /**
     * A list of {@link ColumnDefinition}s of this type.
     */
    public List<ColumnDefinition> getColumnDefinitions() {
        return columnDefinitions;
    }

    public void setColumnDefinitions(List<ColumnDefinition> list) {
        columnDefinitions = list;
    }

    /**
     * A list of options (as simple strings) of this type definition, as ("TYPE", "=", "MYISAM")
     */
    public List<?> getTypeOptionsStrings() {
        return typeOptionsStrings;
    }

    public void setTypeOptionsStrings(List<String> list) {
        typeOptionsStrings = list;
    }

    public List<String> getCreateOptionsStrings() {
        return createOptionsStrings;
    }

    public void setCreateOptionsStrings(List<String> createOptionsStrings) {
        this.createOptionsStrings = createOptionsStrings;
    }

    /**
     * A list of {@link Index}es (for example "PRIMARY KEY") of this type.<br>
     * Indexes created with column definitions (as in mycol INT PRIMARY KEY) are not inserted into
     * this list.
     */
    public List<Index> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<Index> list) {
        indexes = list;
    }

    public Select getSelect() {
        return select;
    }

    public void setSelect(Select select, boolean parenthesis) {
        this.select = select;
        this.selectParenthesis = parenthesis;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public void setIfNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }

    public boolean isSelectParenthesis() {
        return selectParenthesis;
    }

    public void setSelectParenthesis(boolean selectParenthesis) {
        this.selectParenthesis = selectParenthesis;
    }

    @Override
    public String toString() {
        String sql;
        String createOps = PlainSelect.getStringList(createOptionsStrings, false, false);

        sql = "CREATE " + (unlogged ? "UNLOGGED " : "")
                + (!"".equals(createOps) ? createOps + " " : "")
                + "METADATA TYPE " + (ifNotExists ? "IF NOT EXISTS " : "") + type;

        if (select != null) {
            sql += " AS " + (selectParenthesis ? "(" : "") + select.toString() + (selectParenthesis ? ")" : "");
        } else {
            sql += " (";

            sql += PlainSelect.getStringList(columnDefinitions, true, false);
            if (indexes != null && !indexes.isEmpty()) {
                sql += ", ";
                sql += PlainSelect.getStringList(indexes);
            }
            sql += ")";
            String options = PlainSelect.getStringList(typeOptionsStrings, false, false);
            if (options != null && options.length() > 0) {
                sql += " " + options;
            }
        }

        return sql;
    }
}
