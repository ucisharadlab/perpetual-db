package edu.uci.ics.perpetual.request;

import edu.uci.ics.perpetual.Schema;

/**
 * THe LoadRequest is used internally for loading data from Database to memory
 */
public class LoadRequest extends Request {

    private Schema schema;

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Schema getResult() {
        return schema;
    }
}
