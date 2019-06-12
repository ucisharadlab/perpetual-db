package edu.uci.ics.perpetual.request;

import edu.uci.ics.perpetual.statement.Statement;

public class SqlRequest extends Request {

    private Statement statement;

    public SqlRequest(Statement statement) {
        this.statement = statement;
    }

    public Statement getStatement() {
        return statement;
    }
}
