
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.schema.Column;

import java.util.List;
import edu.uci.ics.perpetual.expressions.Alias;

public class Pivot {

    private List<FunctionItem> functionItems;
    private List<Column> forColumns;
    private List<SelectExpressionItem> singleInItems;
    private List<ExpressionListItem> multiInItems;
    private Alias alias;

    public void accept(PivotVisitor pivotVisitor) {
        pivotVisitor.visit(this);
    }

    public List<SelectExpressionItem> getSingleInItems() {
        return singleInItems;
    }

    public void setSingleInItems(List<SelectExpressionItem> singleInItems) {
        this.singleInItems = singleInItems;
    }

    public List<ExpressionListItem> getMultiInItems() {
        return multiInItems;
    }

    public void setMultiInItems(List<ExpressionListItem> multiInItems) {
        this.multiInItems = multiInItems;
    }

    public List<FunctionItem> getFunctionItems() {
        return functionItems;
    }

    public void setFunctionItems(List<FunctionItem> functionItems) {
        this.functionItems = functionItems;
    }

    public List<Column> getForColumns() {
        return forColumns;
    }

    public void setForColumns(List<Column> forColumns) {
        this.forColumns = forColumns;
    }

    public List<?> getInItems() {
        return singleInItems == null ? multiInItems : singleInItems;
    }

    public Alias getAlias() {
        return alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "PIVOT ("
                + PlainSelect.getStringList(functionItems)
                + " FOR " + PlainSelect.
                        getStringList(forColumns, true, forColumns != null && forColumns.size() > 1)
                + " IN " + PlainSelect.getStringList(getInItems(), true, true) + ")"
                + (alias!=null?alias.toString():"");
    }
}
