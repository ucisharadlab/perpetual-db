
package edu.uci.ics.perpetual.statement.truncate;

import edu.uci.ics.perpetual.schema.Table;
import edu.uci.ics.perpetual.statement.Statement;
import edu.uci.ics.perpetual.statement.StatementVisitor;

/**
 * A TRUNCATE TABLE statement
 */
public class Truncate implements Statement {

    private Table table;
    boolean cascade;  // to support TRUNCATE TABLE ... CASCADE

    @Override
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public boolean getCascade(){
        return cascade;
    }

    public void setCascade(boolean c){
        cascade=c;
    }

    @Override
    public String toString() {
        if(cascade){
            return "TRUNCATE TABLE " + table+" CASCADE";
        }
        return "TRUNCATE TABLE " + table;
    }
}
