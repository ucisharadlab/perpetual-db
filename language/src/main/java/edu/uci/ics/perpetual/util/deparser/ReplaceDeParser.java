
package edu.uci.ics.perpetual.util.deparser;

import java.util.Iterator;

import edu.uci.ics.perpetual.expression.Expression;
import edu.uci.ics.perpetual.expression.ExpressionVisitor;
import edu.uci.ics.perpetual.expression.operators.relational.ExpressionList;
import edu.uci.ics.perpetual.expression.operators.relational.NamedExpressionList;
import edu.uci.ics.perpetual.expression.operators.relational.ItemsListVisitor;
import edu.uci.ics.perpetual.expression.operators.relational.MultiExpressionList;
import edu.uci.ics.perpetual.schema.Column;
import edu.uci.ics.perpetual.statement.replace.Replace;
import edu.uci.ics.perpetual.statement.select.SelectVisitor;
import edu.uci.ics.perpetual.statement.select.SubSelect;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a string) a
 * {@link edu.uci.ics.perpetual.statement.replace.Replace}
 */
public class ReplaceDeParser implements ItemsListVisitor {

    protected StringBuilder buffer;
    private ExpressionVisitor expressionVisitor;
    private SelectVisitor selectVisitor;

    public ReplaceDeParser() {
    }

    /**
     * @param expressionVisitor a {@link ExpressionVisitor} to de-parse expressions. It has to share
     * the same<br>
     * StringBuilder (buffer parameter) as this object in order to work
     * @param selectVisitor a {@link SelectVisitor} to de-parse
     * {@link edu.uci.ics.perpetual.statement.select.Select}s. It has to share the same<br>
     * StringBuilder (buffer parameter) as this object in order to work
     * @param buffer the buffer that will be filled with the select
     */
    public ReplaceDeParser(ExpressionVisitor expressionVisitor, SelectVisitor selectVisitor, StringBuilder buffer) {
        this.buffer = buffer;
        this.expressionVisitor = expressionVisitor;
        this.selectVisitor = selectVisitor;
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public void deParse(Replace replace) {
        buffer.append("REPLACE ");
        if (replace.isUseIntoTables()) {
            buffer.append("INTO ");
        }
        buffer.append(replace.getTable().getFullyQualifiedName());
        if (replace.getItemsList() != null) {
            if (replace.getColumns() != null) {
                buffer.append(" (");
                for (int i = 0; i < replace.getColumns().size(); i++) {
                    Column column = replace.getColumns().get(i);
                    buffer.append(column.getFullyQualifiedName());
                    if (i < replace.getColumns().size() - 1) {
                        buffer.append(", ");
                    }
                }
                buffer.append(") ");
            } else {
                buffer.append(" ");
            }

        } else {
            buffer.append(" SET ");
            for (int i = 0; i < replace.getColumns().size(); i++) {
                Column column = replace.getColumns().get(i);
                buffer.append(column.getFullyQualifiedName()).append("=");

                Expression expression = replace.getExpressions().get(i);
                expression.accept(expressionVisitor);
                if (i < replace.getColumns().size() - 1) {
                    buffer.append(", ");
                }

            }
        }

        if (replace.getItemsList() != null) {
            // REPLACE mytab SELECT * FROM mytab2
            // or VALUES ('as', ?, 565)
            replace.getItemsList().accept(this);
        }
    }

    @Override
    public void visit(ExpressionList expressionList) {
        buffer.append("VALUES (");
        for (Iterator<Expression> iter = expressionList.getExpressions().iterator(); iter.hasNext();) {
            Expression expression = iter.next();
            expression.accept(expressionVisitor);
            if (iter.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append(")");
    }

// NamedExpressionList not use by top-level Replace
    @Override
    public void visit(NamedExpressionList namedExpressionList) {
    }

    @Override
    public void visit(SubSelect subSelect) {
        subSelect.getSelectBody().accept(selectVisitor);
    }

    public ExpressionVisitor getExpressionVisitor() {
        return expressionVisitor;
    }

    public SelectVisitor getSelectVisitor() {
        return selectVisitor;
    }

    public void setExpressionVisitor(ExpressionVisitor visitor) {
        expressionVisitor = visitor;
    }

    public void setSelectVisitor(SelectVisitor visitor) {
        selectVisitor = visitor;
    }

    @Override
    public void visit(MultiExpressionList multiExprList) {
        buffer.append("VALUES ");
        for (Iterator<ExpressionList> it = multiExprList.getExprList().iterator(); it.hasNext();) {
            buffer.append("(");
            for (Iterator<Expression> iter = it.next().getExpressions().iterator(); iter.hasNext();) {
                Expression expression = iter.next();
                expression.accept(expressionVisitor);
                if (iter.hasNext()) {
                    buffer.append(", ");
                }
            }
            buffer.append(")");
            if (it.hasNext()) {
                buffer.append(", ");
            }
        }
    }
}
