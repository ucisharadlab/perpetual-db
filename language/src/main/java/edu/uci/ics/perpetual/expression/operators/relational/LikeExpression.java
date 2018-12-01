
package edu.uci.ics.perpetual.expression.operators.relational;

import edu.uci.ics.perpetual.expression.BinaryExpression;
import edu.uci.ics.perpetual.expression.ExpressionVisitor;

public class LikeExpression extends BinaryExpression {

    //private boolean not = false;
    private String escape = null;
    private boolean caseInsensitive = false;

//    @Override
//    public boolean isNot() {
//        return not;
//    }
//
//    public void setNot(boolean b) {
//        not = b;
//    }
    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String getStringExpression() {
        return caseInsensitive ? "ILIKE" : "LIKE";
    }

    @Override
    public String toString() {
        String retval = super.toString();
        if (escape != null) {
            retval += " ESCAPE " + "'" + escape + "'";
        }

        return retval;
    }

    public String getEscape() {
        return escape;
    }

    public void setEscape(String escape) {
        this.escape = escape;
    }

    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    public void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }
}
