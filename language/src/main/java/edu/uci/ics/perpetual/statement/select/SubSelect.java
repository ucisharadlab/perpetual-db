
package edu.uci.ics.perpetual.statement.select;

import java.util.Iterator;
import java.util.List;
import edu.uci.ics.perpetual.expressions.Alias;
import edu.uci.ics.perpetual.expressions.Expression;
import edu.uci.ics.perpetual.expressions.ExpressionVisitor;
import edu.uci.ics.perpetual.expressions.operators.relational.ItemsList;
import edu.uci.ics.perpetual.expressions.operators.relational.ItemsListVisitor;
import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

/**
 * A subselect followed by an optional alias.
 */
public class SubSelect extends ASTNodeAccessImpl implements FromItem, Expression, ItemsList {

    private SelectBody selectBody;
    private Alias alias;
    private boolean useBrackets = true;
    private List<WithItem> withItemsList;

    private Pivot pivot;

    @Override
    public void accept(FromItemVisitor fromItemVisitor) {
        fromItemVisitor.visit(this);
    }

    public SelectBody getSelectBody() {
        return selectBody;
    }

    public void setSelectBody(SelectBody body) {
        selectBody = body;
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public Alias getAlias() {
        return alias;
    }

    @Override
    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    @Override
    public Pivot getPivot() {
        return pivot;
    }

    @Override
    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }

    public boolean isUseBrackets() {
        return useBrackets;
    }

    public void setUseBrackets(boolean useBrackets) {
        this.useBrackets = useBrackets;
    }

    public List<WithItem> getWithItemsList() {
        return withItemsList;
    }

    public void setWithItemsList(List<WithItem> withItemsList) {
        this.withItemsList = withItemsList;
    }

    @Override
    public void accept(ItemsListVisitor itemsListVisitor) {
        itemsListVisitor.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder retval = new StringBuilder();
        if (useBrackets) {
            retval.append("(");
        }
        if (withItemsList != null && !withItemsList.isEmpty()) {
            retval.append("WITH ");
            for (Iterator<WithItem> iter = withItemsList.iterator(); iter.hasNext();) {
                WithItem withItem = iter.next();
                retval.append(withItem);
                if (iter.hasNext()) {
                    retval.append(",");
                }
                retval.append(" ");
            }
        }
        retval.append(selectBody);
        if (useBrackets) {
            retval.append(")");
        }

        if (alias != null) {
            retval.append(alias.toString());
        }
        if (pivot != null) {
            retval.append(" ").append(pivot);
        }

        return retval.toString();
    }
}
