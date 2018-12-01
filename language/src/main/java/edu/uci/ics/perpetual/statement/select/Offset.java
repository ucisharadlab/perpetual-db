
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.expression.Expression;
import edu.uci.ics.perpetual.expression.JdbcNamedParameter;
import edu.uci.ics.perpetual.expression.JdbcParameter;

/**
 * An offset clause in the form OFFSET offset or in the form OFFSET offset (ROW | ROWS)
 */
public class Offset {

    private long offset;
    private Expression offsetJdbcParameter = null;
    private String offsetParam = null;

    public long getOffset() {
        return offset;
    }

    public String getOffsetParam() {
        return offsetParam;
    }

    public void setOffset(long l) {
        offset = l;
    }

    public void setOffsetParam(String s) {
        offsetParam = s;
    }

    public Expression getOffsetJdbcParameter() {
        return offsetJdbcParameter;
    }

    public void setOffsetJdbcParameter(JdbcParameter jdbc) {
        offsetJdbcParameter = jdbc;
    }
    
    public void setOffsetJdbcParameter(JdbcNamedParameter jdbc) {
        offsetJdbcParameter = jdbc;
    }

    @Override
    public String toString() {
        return " OFFSET " + (offsetJdbcParameter!=null ? offsetJdbcParameter.toString() : offset) + (offsetParam != null ? " " + offsetParam : "");
    }
}
