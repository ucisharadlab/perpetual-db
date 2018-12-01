
package edu.uci.ics.perpetual.util.deparser;

import java.util.List;
import edu.uci.ics.perpetual.expression.Expression;
import edu.uci.ics.perpetual.expression.ExpressionVisitor;
import edu.uci.ics.perpetual.statement.execute.Execute;

public class ExecuteDeParser {

    protected StringBuilder buffer;
    private ExpressionVisitor expressionVisitor;

    /**
     * @param expressionVisitor a {@link ExpressionVisitor} to de-parse expressions. It has to share
     * the same<br>
     * StringBuilder (buffer parameter) as this object in order to work
     * @param buffer the buffer that will be filled with the select
     */
    public ExecuteDeParser(ExpressionVisitor expressionVisitor, StringBuilder buffer) {
        this.buffer = buffer;
        this.expressionVisitor = expressionVisitor;
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public void deParse(Execute execute) {
        buffer.append(execute.getExecType().name()).append(" ").append(execute.getName());
        if (execute.isParenthesis()) {
            buffer.append(" (");
        } else if (execute.getExprList() != null) {
            buffer.append(" ");
        }
        if (execute.getExprList() != null) {
            List<Expression> expressions = execute.getExprList().getExpressions();
            for (int i = 0; i < expressions.size(); i++) {
                if (i > 0) {
                    buffer.append(", ");
                }
                expressions.get(i).accept(expressionVisitor);
            }
        }
        if (execute.isParenthesis()) {
            buffer.append(")");
        }
    }

    public ExpressionVisitor getExpressionVisitor() {
        return expressionVisitor;
    }

    public void setExpressionVisitor(ExpressionVisitor visitor) {
        expressionVisitor = visitor;
    }
}
