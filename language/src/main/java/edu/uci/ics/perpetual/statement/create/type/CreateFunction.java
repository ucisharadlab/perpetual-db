
package edu.uci.ics.perpetual.statement.create.type;

import edu.uci.ics.perpetual.schema.Function;
import edu.uci.ics.perpetual.schema.Tag;
import edu.uci.ics.perpetual.schema.Type;
import edu.uci.ics.perpetual.statement.Statement;
import edu.uci.ics.perpetual.statement.StatementVisitor;
import edu.uci.ics.perpetual.statement.select.PlainSelect;
import edu.uci.ics.perpetual.statement.select.Select;

import java.util.List;

/**
 * A "CREATE METADATA TYPE" statement
 */
public class CreateFunction implements Statement {

    private Function function;
    private Type type;
    private boolean unlogged = false;
    private List<String> createOptionsStrings;
    private List<String> typeOptionsStrings;
    private List<Tag> tags;
    private List<Index> indexes;
    private Select select;
    private boolean selectParenthesis;
    private boolean ifNotExists = false;
    private int cost;
    private Tag returnTag;

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
                + "FUNCTION " + (ifNotExists ? "IF NOT EXISTS " : "") + type;

        if (select != null) {
            sql += " AS " + (selectParenthesis ? "(" : "") + select.toString() + (selectParenthesis ? ")" : "");
        } else {
            sql += " (";

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

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Tag getReturnTag() {
        return returnTag;
    }

    public void setReturnTag(Tag returnTag) {
        this.returnTag = returnTag;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
