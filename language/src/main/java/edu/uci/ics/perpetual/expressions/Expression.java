
package edu.uci.ics.perpetual.expressions;

import edu.uci.ics.perpetual.parser.ASTNodeAccess;

public interface Expression extends ASTNodeAccess {

    void accept(ExpressionVisitor expressionVisitor);
}
