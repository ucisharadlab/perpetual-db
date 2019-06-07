
package edu.uci.ics.perpetual.expressions;

import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

/**
 * A '?' in a statement or a ?<number> e.g. ?4
 */
public class JdbcParameter extends ASTNodeAccessImpl implements Expression {

    private Integer index;
    private boolean useFixedIndex = false;

    public JdbcParameter() {
    }

    public JdbcParameter(Integer index, boolean useFixedIndex) {
        this.index = index;
        this.useFixedIndex = useFixedIndex;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public boolean isUseFixedIndex() {
        return useFixedIndex;
    }

    public void setUseFixedIndex(boolean useFixedIndex) {
        this.useFixedIndex = useFixedIndex;
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return useFixedIndex ? "?" + index : "?";
    }
}
