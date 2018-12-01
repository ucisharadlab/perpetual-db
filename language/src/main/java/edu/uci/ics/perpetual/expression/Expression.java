
package edu.uci.ics.perpetual.expression;

import edu.uci.ics.perpetual.parser.ASTNodeAccess;

public interface Expression extends ASTNodeAccess {

    void accept(ExpressionVisitor expressionVisitor);
}
