
package edu.uci.ics.perpetual.expressions;

import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

/**
 * A "NULL" in a sql statement
 */
public class NullValue extends ASTNodeAccessImpl implements Expression {

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return "NULL";
    }
}
