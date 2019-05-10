package edu.uci.ics.perpetual.workload.parser;

import edu.uci.ics.perpetual.statement.Statement;

import java.util.List;

public interface IWorkloadParser {

    public List<Statement> parseFileInMemory();

}
