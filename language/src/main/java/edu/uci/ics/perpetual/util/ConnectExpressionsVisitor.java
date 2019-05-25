
package edu.uci.ics.perpetual.util;

import edu.uci.ics.perpetual.expression.Alias;
import edu.uci.ics.perpetual.expression.BinaryExpression;
import edu.uci.ics.perpetual.statement.select.*;
import edu.uci.ics.perpetual.statement.values.ValuesStatement;

import java.util.LinkedList;
import java.util.List;

/**
 * Connect all selected expressions with a binary expression. Out of select a,b from table one gets
 * select a || b as expr from table. The type of binary expression is set by overwriting this class
 * abstract method createBinaryExpression.
 *
 * @author tw
 */
public abstract class ConnectExpressionsVisitor implements SelectVisitor, SelectItemVisitor {

    private String alias = "expr";
    private final List<SelectExpressionItem> itemsExpr = new LinkedList<SelectExpressionItem>();

    public ConnectExpressionsVisitor() {
    }

    public ConnectExpressionsVisitor(String alias) {
        this.alias = alias;
    }

    /**
     * Create instances of this binary expression that connects all selected expressions.
     *
     * @return
     */
    protected abstract BinaryExpression createBinaryExpression();

    @Override
    public void visit(PlainSelect plainSelect) {
        for (SelectItem item : plainSelect.getSelectItems()) {
            item.accept(this);
        }

        if (itemsExpr.size() > 1) {
            BinaryExpression binExpr = createBinaryExpression();
            binExpr.setLeftExpression(itemsExpr.get(0).getExpression());
            for (int i = 1; i < itemsExpr.size() - 1; i++) {
                binExpr.setRightExpression(itemsExpr.get(i).getExpression());
                BinaryExpression binExpr2 = createBinaryExpression();
                binExpr2.setLeftExpression(binExpr);
                binExpr = binExpr2;
            }
            binExpr.setRightExpression(itemsExpr.get(itemsExpr.size() - 1).getExpression());

            SelectExpressionItem sei = new SelectExpressionItem();
            sei.setExpression(binExpr);

            plainSelect.getSelectItems().clear();
            plainSelect.getSelectItems().add(sei);
        }

        ((SelectExpressionItem) plainSelect.getSelectItems().get(0)).setAlias(new Alias(alias));
    }

    @Override
    public void visit(SetOperationList setOpList) {
        for (SelectBody select : setOpList.getSelects()) {
            select.accept(this);
        }
    }

    @Override
    public void visit(WithItem withItem) {
    }

    @Override
    public void visit(AllTypeColumns allTypeColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visit(AllColumns allColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visit(SelectExpressionItem selectExpressionItem) {
        itemsExpr.add(selectExpressionItem);
    }

    @Override
    public void visit(ValuesStatement aThis) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
