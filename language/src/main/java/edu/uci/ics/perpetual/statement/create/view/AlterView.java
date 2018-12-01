
package edu.uci.ics.perpetual.statement.create.view;

import java.util.List;
import edu.uci.ics.perpetual.schema.Table;
import edu.uci.ics.perpetual.statement.Statement;
import edu.uci.ics.perpetual.statement.StatementVisitor;
import edu.uci.ics.perpetual.statement.select.PlainSelect;
import edu.uci.ics.perpetual.statement.select.SelectBody;

/**
 * A "CREATE VIEW" statement
 */
public class AlterView implements Statement {

    private Table view;
    private SelectBody selectBody;
    private boolean useReplace = false;
    private List<String> columnNames = null;

    @Override
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    /**
     * In the syntax tree, a view looks and acts just like a Table.
     *
     * @return The name of the view to be created.
     */
    public Table getView() {
        return view;
    }

    public void setView(Table view) {
        this.view = view;
    }

    /**
     * @return the SelectBody
     */
    public SelectBody getSelectBody() {
        return selectBody;
    }

    public void setSelectBody(SelectBody selectBody) {
        this.selectBody = selectBody;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public boolean isUseReplace() {
        return useReplace;
    }

    public void setUseReplace(boolean useReplace) {
        this.useReplace = useReplace;
    }


    @Override
    public String toString() {
        StringBuilder sql;
        if(useReplace){
            sql = new StringBuilder("REPLACE ");
        }else{
            sql = new StringBuilder("ALTER ");
        }
        sql.append("VIEW ");
        sql.append(view);
        if (columnNames != null) {
            sql.append(PlainSelect.getStringList(columnNames, true, true));
        }
        sql.append(" AS ").append(selectBody);
        return sql.toString();
    }
}
