package edu.uci.ics.perpetual.common.enrichment;

import edu.uci.ics.perpetual.data.DataObject;

public class Enrichment{

    public DataObject enrich(DataObject dataObject) {
        dataObject.getObject().addProperty("Test", "Success");
        return dataObject;
    }
}
