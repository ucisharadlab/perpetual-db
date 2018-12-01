
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

/**
 * All the columns (as in "SELECT * FROM ...")
 */
public class AllColumns extends ASTNodeAccessImpl implements SelectItem {

    public AllColumns() {
    }

    @Override
    public void accept(SelectItemVisitor selectItemVisitor) {
        selectItemVisitor.visit(this);
    }

    @Override
    public String toString() {
        return "*";
    }
}
