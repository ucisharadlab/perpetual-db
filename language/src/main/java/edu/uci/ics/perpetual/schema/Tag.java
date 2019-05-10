
package edu.uci.ics.perpetual.schema;

import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

import java.util.List;

/**
 * A column. It can have the type name it belongs to.
 */
public final class Tag extends ASTNodeAccessImpl {

    private Type type;
    private String name;

    public Tag() {
    }

    public Tag(Type type, String name) {
        setType(type);
        setName(name);
    }

    public Tag(List<String> nameParts) {
        this(nameParts.size() > 1
                ? new Type(nameParts.subList(0, nameParts.size() - 1)) : null,
                nameParts.get(nameParts.size() - 1));
    }

    public Tag(String name) {
        this(null, name);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String string) {
        name = string;
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
        if (name != null) {
            fqn.append(name);
        }
        return fqn.toString();
    }

    @Override
    public String toString() {
        return getName(true);
    }
}
