package edu.uci.ics.perpetual.predicate;

import edu.uci.ics.perpetual.data.DataObject;

import java.util.List;
import java.util.stream.Collectors;

public class ExpressionPredicate implements IPredicate {

    private LogicalOperator lop;

    private List<Expression> expressions;

    public ExpressionPredicate(LogicalOperator lop, List<Expression> expressions) {
        this.lop = lop;
        this.expressions = expressions;
    }

    public LogicalOperator getLop() {
        return lop;
    }

    public void setLop(LogicalOperator lop) {
        this.lop = lop;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public String toString() {

        return String.join(
                lop.toString(),
                expressions.stream().map(Object::toString).collect(Collectors.toList())
        );
    }

    @Override
    public boolean evaluate(DataObject dataObject) {
        return false;
    }

}
