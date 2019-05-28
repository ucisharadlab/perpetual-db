
package edu.uci.ics.perpetual.statement.select;

/**
 * All the columns of a type (as in "SELECT TableName.* FROM ...")
 */

import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;
import edu.uci.ics.perpetual.schema.Type;

public class AllTypeColumns extends ASTNodeAccessImpl implements SelectItem {

    private Type type;

    public AllTypeColumns() {
    }

    public AllTypeColumns(Type typeName) {
        this.type = typeName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public void accept(SelectItemVisitor selectItemVisitor) {
        selectItemVisitor.visit(this);
    }

    @Override
    public String toString() {
        return type + ".*";
    }
}
