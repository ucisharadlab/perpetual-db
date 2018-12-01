
package edu.uci.ics.perpetual.util.deparser;

import java.util.Iterator;

import edu.uci.ics.perpetual.expression.Expression;
import edu.uci.ics.perpetual.expression.ExpressionVisitor;
import edu.uci.ics.perpetual.expression.ExpressionVisitorAdapter;
import edu.uci.ics.perpetual.schema.Column;
import edu.uci.ics.perpetual.statement.select.Join;
import edu.uci.ics.perpetual.statement.select.PlainSelect;
import edu.uci.ics.perpetual.statement.update.Update;
import edu.uci.ics.perpetual.statement.select.Select;
import edu.uci.ics.perpetual.statement.select.SelectVisitor;
import edu.uci.ics.perpetual.statement.select.OrderByVisitor;
import edu.uci.ics.perpetual.statement.select.OrderByElement;
import edu.uci.ics.perpetual.statement.select.SelectExpressionItem;
import edu.uci.ics.perpetual.statement.select.SelectVisitorAdapter;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a string) an
 * {@link edu.uci.ics.perpetual.statement.update.Update}
 */
public class UpdateDeParser implements OrderByVisitor {

    protected StringBuilder buffer = new StringBuilder();
    private ExpressionVisitor expressionVisitor = new ExpressionVisitorAdapter();
    private SelectVisitor selectVisitor = new SelectVisitorAdapter();

    public UpdateDeParser() {
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
    public UpdateDeParser(ExpressionVisitor expressionVisitor, SelectVisitor selectVisitor, StringBuilder buffer) {
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

    public void deParse(Update update) {
        buffer.append("UPDATE ").append(PlainSelect.getStringList(update.getTables(), true, false)).
                append(" SET ");

        if (!update.isUseSelect()) {
            for (int i = 0; i < update.getColumns().size(); i++) {
                Column column = update.getColumns().get(i);
                column.accept(expressionVisitor);

                buffer.append(" = ");

                Expression expression = update.getExpressions().get(i);
                expression.accept(expressionVisitor);
                if (i < update.getColumns().size() - 1) {
                    buffer.append(", ");
                }
            }
        } else {
            if (update.isUseColumnsBrackets()) {
                buffer.append("(");
            }
            for (int i = 0; i < update.getColumns().size(); i++) {
                if (i != 0) {
                    buffer.append(", ");
                }
                Column column = update.getColumns().get(i);
                column.accept(expressionVisitor);
            }
            if (update.isUseColumnsBrackets()) {
                buffer.append(")");
            }
            buffer.append(" = ");
            buffer.append("(");
            Select select = update.getSelect();
            select.getSelectBody().accept(selectVisitor);
            buffer.append(")");
        }

        if (update.getFromItem() != null) {
            buffer.append(" FROM ").append(update.getFromItem());
            if (update.getJoins() != null) {
                for (Join join : update.getJoins()) {
                    if (join.isSimple()) {
                        buffer.append(", ").append(join);
                    } else {
                        buffer.append(" ").append(join);
                    }
                }
            }
        }

        if (update.getWhere() != null) {
            buffer.append(" WHERE ");
            update.getWhere().accept(expressionVisitor);
        }
        if (update.getOrderByElements() != null) {
            new OrderByDeParser(expressionVisitor, buffer).deParse(update.getOrderByElements());
        }
        if (update.getLimit() != null) {
            new LimitDeparser(buffer).deParse(update.getLimit());
        }

        if (update.isReturningAllColumns()) {
            buffer.append(" RETURNING *");
        } else if (update.getReturningExpressionList() != null) {
            buffer.append(" RETURNING ");
            for (Iterator<SelectExpressionItem> iter = update.getReturningExpressionList().
                    iterator(); iter.hasNext();) {
                buffer.append(iter.next().toString());
                if (iter.hasNext()) {
                    buffer.append(", ");
                }
            }
        }
    }

    public ExpressionVisitor getExpressionVisitor() {
        return expressionVisitor;
    }

    public void setExpressionVisitor(ExpressionVisitor visitor) {
        expressionVisitor = visitor;
    }

    @Override
    public void visit(OrderByElement orderBy) {
        orderBy.getExpression().accept(expressionVisitor);
        if (!orderBy.isAsc()) {
            buffer.append(" DESC");
        } else if (orderBy.isAscDescPresent()) {
            buffer.append(" ASC");
        }
        if (orderBy.getNullOrdering() != null) {
            buffer.append(' ');
            buffer.
                    append(orderBy.getNullOrdering() == OrderByElement.NullOrdering.NULLS_FIRST ? "NULLS FIRST" : "NULLS LAST");
        }
    }
}
