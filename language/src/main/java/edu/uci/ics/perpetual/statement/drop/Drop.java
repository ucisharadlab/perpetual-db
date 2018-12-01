
package edu.uci.ics.perpetual.statement.drop;

import java.util.List;
import edu.uci.ics.perpetual.schema.Table;

import edu.uci.ics.perpetual.statement.Statement;
import edu.uci.ics.perpetual.statement.StatementVisitor;
import edu.uci.ics.perpetual.statement.select.PlainSelect;

public class Drop implements Statement {

    private String type;
    private Table name;
    private List<String> parameters;
    private boolean ifExists = false;

    @Override
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public Table getName() {
        return name;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public String getType() {
        return type;
    }

    public void setName(Table string) {
        name = string;
    }

    public void setParameters(List<String> list) {
        parameters = list;
    }

    public void setType(String string) {
        type = string;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public void setIfExists(boolean ifExists) {
        this.ifExists = ifExists;
    }

    @Override
    public String toString() {
        String sql = "DROP " + type + " "
                + (ifExists ? "IF EXISTS " : "") + name.toString();

        if (parameters != null && !parameters.isEmpty()) {
            sql += " " + PlainSelect.getStringList(parameters);
        }

        return sql;
    }
}
