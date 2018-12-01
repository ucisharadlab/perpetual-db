
package edu.uci.ics.perpetual.statement.create.table;

import edu.uci.ics.perpetual.expression.Expression;
import edu.uci.ics.perpetual.schema.Table;

/**
 * Table Check Constraint Eg. ' CONSTRAINT less_than_ten CHECK (count < 10) ' @au
 *
 *
 * thor mw
 */
public class CheckConstraint extends NamedConstraint {

    private Table table;
    private Expression expression;

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "CONSTRAINT " + getName() + " CHECK (" + expression + ")";
    }
}
