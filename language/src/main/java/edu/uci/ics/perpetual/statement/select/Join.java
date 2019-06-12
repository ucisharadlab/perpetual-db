
package edu.uci.ics.perpetual.statement.select;

import java.util.List;

import edu.uci.ics.perpetual.expressions.Expression;
import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;
import edu.uci.ics.perpetual.schema.Column;

/**
 * A join clause
 */
public class Join extends ASTNodeAccessImpl {

    private boolean outer = false;
    private boolean right = false;
    private boolean left = false;
    private boolean natural = false;
    private boolean full = false;
    private boolean inner = false;
    private boolean simple = false;
    private boolean cross = false;
    private boolean semi = false;
    private FromItem rightItem;
    private Expression onExpression;
    private List<Column> usingColumns;

    /**
     * Whether is a tab1,tab2 join
     *
     * @return true if is a "tab1,tab2" join
     */
    public boolean isSimple() {
        return simple;
    }

    public void setSimple(boolean b) {
        simple = b;
    }

    /**
     * Whether is a "INNER" join
     *
     * @return true if is a "INNER" join
     */
    public boolean isInner() {
        return inner;
    }

    public void setInner(boolean b) {
        inner = b;
    }

    /**
     * Whether is a "OUTER" join
     *
     * @return true if is a "OUTER" join
     */
    public boolean isOuter() {
        return outer;
    }

    public void setOuter(boolean b) {
        outer = b;
    }

    /**
     * Whether is a "SEMI" join
     *
     * @return true if is a "SEMI" join
     */
    public boolean isSemi() {
        return semi;
    }

    public void setSemi(boolean b) {
        semi = b;
    }

    /**
     * Whether is a "LEFT" join
     *
     * @return true if is a "LEFT" join
     */
    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean b) {
        left = b;
    }

    /**
     * Whether is a "RIGHT" join
     *
     * @return true if is a "RIGHT" join
     */
    public boolean isRight() {
        return right;
    }

    public void setRight(boolean b) {
        right = b;
    }

    /**
     * Whether is a "NATURAL" join
     *
     * @return true if is a "NATURAL" join
     */
    public boolean isNatural() {
        return natural;
    }

    public void setNatural(boolean b) {
        natural = b;
    }

    /**
     * Whether is a "FULL" join
     *
     * @return true if is a "FULL" join
     */
    public boolean isFull() {
        return full;
    }

    public void setFull(boolean b) {
        full = b;
    }

    public boolean isCross() {
        return cross;
    }

    public void setCross(boolean cross) {
        this.cross = cross;
    }

    /**
     * Returns the right item of the join
     */
    public FromItem getRightItem() {
        return rightItem;
    }

    public void setRightItem(FromItem item) {
        rightItem = item;
    }

    /**
     * Returns the "ON" expression (if any)
     */
    public Expression getOnExpression() {
        return onExpression;
    }

    public void setOnExpression(Expression expression) {
        onExpression = expression;
    }

    /**
     * Returns the "USING" list of {@link edu.uci.ics.perpetual.schema.Column}s (if any)
     */
    public List<Column> getUsingColumns() {
        return usingColumns;
    }

    public void setUsingColumns(List<Column> list) {
        usingColumns = list;
    }

    @Override
    public String toString() {
        if (isSimple()) {
            return "" + rightItem;
        } else {
            String type = "";

            if (isRight()) {
                type += "RIGHT ";
            } else if (isNatural()) {
                type += "NATURAL ";
            } else if (isFull()) {
                type += "FULL ";
            } else if (isLeft()) {
                type += "LEFT ";
            } else if (isCross()) {
                type += "CROSS ";
            }

            if (isOuter()) {
                type += "OUTER ";
            } else if (isInner()) {
                type += "INNER ";
            } else if (isSemi()) {
                type += "SEMI ";
            }

            return type + "JOIN " + rightItem + ((onExpression != null) ? " ON " + onExpression + "" : "")
                    + PlainSelect.getFormatedList(usingColumns, "USING", true, true);
        }

    }
}
