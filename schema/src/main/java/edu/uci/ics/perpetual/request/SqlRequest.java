package edu.uci.ics.perpetual.request;

import edu.uci.ics.perpetual.statement.Statement;

/**
 * THe SqlRequest is intermediary that uses for communicating
 * between SchemaManager and SqlManager (incoming SQL statements)
 */
public class SqlRequest extends Request {

    private Statement statement;

    public SqlRequest(Statement statement) {
        this.statement = statement;
    }

    public Statement getStatement() {
        return statement;
    }
}
