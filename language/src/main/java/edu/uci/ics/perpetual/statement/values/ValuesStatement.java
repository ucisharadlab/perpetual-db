
package edu.uci.ics.perpetual.statement.values;

import edu.uci.ics.perpetual.expression.Expression;
import edu.uci.ics.perpetual.statement.Statement;
import edu.uci.ics.perpetual.statement.StatementVisitor;
import edu.uci.ics.perpetual.statement.select.PlainSelect;
import edu.uci.ics.perpetual.statement.select.SelectBody;
import edu.uci.ics.perpetual.statement.select.SelectVisitor;

import java.util.List;

/**
 * The replace statement.
 */
public class ValuesStatement implements Statement, SelectBody {
    
    private List<Expression> expressions;
    
    public ValuesStatement(List<Expression> expressions) {
        this.expressions = expressions;
    }
    
    @Override
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }
    
    public List<Expression> getExpressions() {
        return expressions;
    }
    
    public void setExpressions(List<Expression> list) {
        expressions = list;
    }
    
    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder();
        sql.append("VALUES ");
        sql.append(PlainSelect.getStringList(expressions, true, true));
        return sql.toString();
    }
    
    @Override
    public void accept(SelectVisitor selectVisitor) {
        selectVisitor.visit(this);
    }
}
