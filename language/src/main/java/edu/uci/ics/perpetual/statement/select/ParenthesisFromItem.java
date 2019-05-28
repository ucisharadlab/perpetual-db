
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.expression.*;

/**
 * It represents an expression like "(" expression ")"
 */
public class ParenthesisFromItem implements FromItem {

    private FromItem fromItem;
    
    private Alias alias;

    public ParenthesisFromItem() {
    }

    public ParenthesisFromItem(FromItem fromItem) {
        setFromItem(fromItem);
    }

    public FromItem getFromItem() {
        return fromItem;
    }

    public final void setFromItem(FromItem fromItem) {
        this.fromItem = fromItem;
    }

    @Override
    public void accept(FromItemVisitor fromItemVisitor) {
        fromItemVisitor.visit(this);
    }

    @Override
    public String toString() {
        return "(" + fromItem + ")" + (alias!=null?alias.toString():"");
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
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void setPivot(Pivot pivot) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
