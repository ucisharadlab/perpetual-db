
package edu.uci.ics.perpetual.parser;

import java.io.Reader;

import edu.uci.ics.perpetual.JSQLParserException;
import edu.uci.ics.perpetual.statement.Statement;

/**
 * Every parser must implements this interface
 */
public interface JSqlParser {

    Statement parse(Reader statementReader) throws JSQLParserException;

    Statement parse(String query) throws JSQLParserException, ParseException;

}
