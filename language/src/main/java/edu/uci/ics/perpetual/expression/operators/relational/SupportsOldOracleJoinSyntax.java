
package edu.uci.ics.perpetual.expression.operators.relational;

public interface SupportsOldOracleJoinSyntax {

    int NO_ORACLE_JOIN = 0;
    int ORACLE_JOIN_RIGHT = 1;
    int ORACLE_JOIN_LEFT = 2;
    int NO_ORACLE_PRIOR = 0;
    int ORACLE_PRIOR_START = 1;
    int ORACLE_PRIOR_END = 2;

    int getOldOracleJoinSyntax();

    void setOldOracleJoinSyntax(int oldOracleJoinSyntax);

    int getOraclePriorPosition();

    void setOraclePriorPosition(int priorPosition);
}
