
package edu.uci.ics.perpetual.schema;

import java.util.List;
import edu.uci.ics.perpetual.expressions.*;
import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

/**
 * A column. It can have the type name it belongs to.
 */
public final class Column extends ASTNodeAccessImpl implements Expression, MultiPartName {

    private Type type;
    private String columnName;

    public Column() {
    }

    public Column(Type type, String columnName) {
        setType(type);
        setColumnName(columnName);
    }

    public Column(List<String> nameParts) {
        this(nameParts.size() > 1
                ? new Type(nameParts.subList(0, nameParts.size() - 1)) : null,
                nameParts.get(nameParts.size() - 1));
    }

    public Column(String columnName) {
        this(null, columnName);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String string) {
        columnName = string;
    }

    @Override
    public String getFullyQualifiedName() {
        return getName(false);
    }

    /**
     * Get name with out without using aliases.
     *
     * @param aliases
     * @return
     */
    public String getName(boolean aliases) {
        StringBuilder fqn = new StringBuilder();

        if (type != null) {
            if (type.getAlias() != null && aliases) {
                fqn.append(type.getAlias().getName());
            } else {
                fqn.append(type.getFullyQualifiedName());
            }
        }
        if (fqn.length() > 0) {
            fqn.append('.');
        }
        if (columnName != null) {
            fqn.append(columnName);
        }
        return fqn.toString();
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return getName(true);
    }
}
