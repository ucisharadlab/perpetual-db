
package edu.uci.ics.perpetual.statement.create.type;

import edu.uci.ics.perpetual.expression.Expression;
import edu.uci.ics.perpetual.schema.Type;

/**
 * Type Check Constraint Eg. ' CONSTRAINT less_than_ten CHECK (count < 10) ' @au
 *
 *
 * thor mw
 */
public class CheckConstraint extends NamedConstraint {

    private Type type;
    private Expression expression;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
