
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.parser.ASTNodeAccess;

/**
 * Anything between "SELECT" and "FROM"<BR>
 * (that is, any column or expression etc to be retrieved with the query)
 */
public interface SelectItem extends ASTNodeAccess {

    void accept(SelectItemVisitor selectItemVisitor);
}
