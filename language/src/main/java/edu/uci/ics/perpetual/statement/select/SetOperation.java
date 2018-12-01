
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;
import edu.uci.ics.perpetual.statement.select.SetOperationList.SetOperationType;

/**
 * Single Set-Operation (name). Placeholder for one specific set operation, e.g. union, intersect.
 *
 * @author tw
 */
public abstract class SetOperation extends ASTNodeAccessImpl {

    private SetOperationType type;

    public SetOperation(SetOperationType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type.name();
    }
}
