
package edu.uci.ics.perpetual.statement;

/**
 * An operation on the db (SELECT, UPDATE ecc.)
 */
public interface Statement {

    void accept(StatementVisitor statementVisitor);
}
