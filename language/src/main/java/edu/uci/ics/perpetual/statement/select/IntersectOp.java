
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.statement.select.SetOperationList.SetOperationType;

/**
 *
 * @author tw
 */
public class IntersectOp extends SetOperation {

    public IntersectOp() {
        super(SetOperationType.INTERSECT);
    }
}
