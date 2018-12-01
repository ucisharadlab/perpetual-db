
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.expression.Alias;

/**
 * An item in a "SELECT [...] FROM item1" statement. (for example a table or a sub-select)
 */
public interface FromItem {

    void accept(FromItemVisitor fromItemVisitor);

    Alias getAlias();

    void setAlias(Alias alias);

    Pivot getPivot();

    void setPivot(Pivot pivot);

}
