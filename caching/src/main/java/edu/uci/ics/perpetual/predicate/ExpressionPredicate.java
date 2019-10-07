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


        boolean flag = false;
        if (lop == LogicalOperator.AND)
            flag = true;

        for (Expression expression : expressions) {

            switch (lop) {
                case AND:
                    flag = flag && expression.check(dataObject);
                    break;
                case OR:
                    flag = flag || expression.check(dataObject);
                    break;
            }
        }

        return flag;
    }

}
