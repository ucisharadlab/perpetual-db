
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.statement.select.SetOperationList.SetOperationType;

/**
 *
 * @author tw
 */
public class ExceptOp extends SetOperation {

    public ExceptOp() {
        super(SetOperationType.EXCEPT);
    }
}
