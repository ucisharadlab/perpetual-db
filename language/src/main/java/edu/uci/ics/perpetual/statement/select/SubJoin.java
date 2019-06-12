
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.expressions.Alias;

import java.util.List;

/**
 * A table created by "(tab1 [join tab2]* )".
 */
public class SubJoin implements FromItem {

    private FromItem left;
    private Alias alias;
    private Pivot pivot;
    private List<Join> joinList;

    @Override
    public void accept(FromItemVisitor fromItemVisitor) {
        fromItemVisitor.visit(this);
    }

    public FromItem getLeft() {
        return left;
    }

    public void setLeft(FromItem l) {
        left = l;
    }

    public List<Join> getJoinList() {
        return joinList;
    }

    public void setJoinList(List<Join> joinList) {
        this.joinList = joinList;
    }

    @Override
    public Pivot getPivot() {
        return pivot;
    }

    @Override
    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(left);
        for(Join j : joinList) {
            sb.append(" ").append(j);
        }

        sb.append(")").append((alias != null) ? (" " + alias.toString()) : "").append((pivot != null) ? " " + pivot : "");
        return sb.toString();
    }
}
