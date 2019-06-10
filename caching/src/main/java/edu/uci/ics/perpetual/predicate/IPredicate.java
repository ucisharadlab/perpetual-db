package edu.uci.ics.perpetual.predicate;

import edu.uci.ics.perpetual.data.DataObject;

public interface IPredicate {

    boolean evaluate(DataObject dataObject);

}
