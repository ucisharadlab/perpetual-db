
package edu.uci.ics.perpetual.statement.select;

/**
 * All the columns of a table (as in "SELECT TableName.* FROM ...")
 */
import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;
import edu.uci.ics.perpetual.schema.*;

public class AllTableColumns extends ASTNodeAccessImpl implements SelectItem {

    private Table table;

    public AllTableColumns() {
    }

    public AllTableColumns(Table tableName) {
        this.table = tableName;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    @Override
    public void accept(SelectItemVisitor selectItemVisitor) {
        selectItemVisitor.visit(this);
    }

    @Override
    public String toString() {
        return table + ".*";
    }
}
