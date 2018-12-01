
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.statement.select.SetOperationList.SetOperationType;

/**
 *
 * @author tw
 */
public class MinusOp extends SetOperation {

    public MinusOp() {
        super(SetOperationType.MINUS);
    }
}
