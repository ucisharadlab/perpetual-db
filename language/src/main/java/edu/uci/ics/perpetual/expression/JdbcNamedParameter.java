
package edu.uci.ics.perpetual.expression;

import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

/**
 *
 * @author aud
 */
public class JdbcNamedParameter extends ASTNodeAccessImpl implements Expression {

    private String name;

    public JdbcNamedParameter() {
    }

    public JdbcNamedParameter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return ":" + name;
    }
}
