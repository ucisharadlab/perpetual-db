
package edu.uci.ics.perpetual.util.deparser;

import java.util.Iterator;
import java.util.List;
import edu.uci.ics.perpetual.expression.AllComparisonExpression;
import edu.uci.ics.perpetual.expression.AnalyticExpression;
import edu.uci.ics.perpetual.expression.AnyComparisonExpression;
import edu.uci.ics.perpetual.expression.BinaryExpression;
import edu.uci.ics.perpetual.expression.CaseExpression;
import edu.uci.ics.perpetual.expression.CastExpression;
import edu.uci.ics.perpetual.expression.DateTimeLiteralExpression;
import edu.uci.ics.perpetual.expression.DateValue;
import edu.uci.ics.perpetual.expression.DoubleValue;
import edu.uci.ics.perpetual.expression.Expression;
import edu.uci.ics.perpetual.expression.ExpressionVisitor;
import edu.uci.ics.perpetual.expression.ExtractExpression;
import edu.uci.ics.perpetual.expression.Function;
import edu.uci.ics.perpetual.expression.HexValue;
import edu.uci.ics.perpetual.expression.IntervalExpression;
import edu.uci.ics.perpetual.expression.JdbcNamedParameter;
import edu.uci.ics.perpetual.expression.JdbcParameter;
import edu.uci.ics.perpetual.expression.JsonExpression;
import edu.uci.ics.perpetual.expression.KeepExpression;
import edu.uci.ics.perpetual.expression.LongValue;
import edu.uci.ics.perpetual.expression.MySQLGroupConcat;
import edu.uci.ics.perpetual.expression.NotExpression;
import edu.uci.ics.perpetual.expression.NullValue;
import edu.uci.ics.perpetual.expression.NumericBind;
import edu.uci.ics.perpetual.expression.OracleHierarchicalExpression;
import edu.uci.ics.perpetual.expression.OracleHint;
import edu.uci.ics.perpetual.expression.Parenthesis;
import edu.uci.ics.perpetual.expression.RowConstructor;
import edu.uci.ics.perpetual.expression.SignedExpression;
import edu.uci.ics.perpetual.expression.StringValue;
import edu.uci.ics.perpetual.expression.TimeKeyExpression;
import edu.uci.ics.perpetual.expression.TimeValue;
import edu.uci.ics.perpetual.expression.TimestampValue;
import edu.uci.ics.perpetual.expression.UserVariable;
import edu.uci.ics.perpetual.expression.ValueListExpression;
import edu.uci.ics.perpetual.expression.WhenClause;
import edu.uci.ics.perpetual.expression.WindowElement;
import edu.uci.ics.perpetual.expression.operators.arithmetic.Addition;
import edu.uci.ics.perpetual.expression.operators.arithmetic.BitwiseAnd;
import edu.uci.ics.perpetual.expression.operators.arithmetic.BitwiseLeftShift;
import edu.uci.ics.perpetual.expression.operators.arithmetic.BitwiseOr;
import edu.uci.ics.perpetual.expression.operators.arithmetic.BitwiseRightShift;
import edu.uci.ics.perpetual.expression.operators.arithmetic.BitwiseXor;
import edu.uci.ics.perpetual.expression.operators.arithmetic.Concat;
import edu.uci.ics.perpetual.expression.operators.arithmetic.Division;
import edu.uci.ics.perpetual.expression.operators.arithmetic.Modulo;
import edu.uci.ics.perpetual.expression.operators.arithmetic.Multiplication;
import edu.uci.ics.perpetual.expression.operators.arithmetic.Subtraction;
import edu.uci.ics.perpetual.expression.operators.conditional.AndExpression;
import edu.uci.ics.perpetual.expression.operators.conditional.OrExpression;
import edu.uci.ics.perpetual.expression.operators.relational.Between;
import edu.uci.ics.perpetual.expression.operators.relational.EqualsTo;
import edu.uci.ics.perpetual.expression.operators.relational.ExistsExpression;
import edu.uci.ics.perpetual.expression.operators.relational.ExpressionList;
import edu.uci.ics.perpetual.expression.operators.relational.NamedExpressionList;
import edu.uci.ics.perpetual.expression.operators.relational.GreaterThan;
import edu.uci.ics.perpetual.expression.operators.relational.GreaterThanEquals;
import edu.uci.ics.perpetual.expression.operators.relational.InExpression;
import edu.uci.ics.perpetual.expression.operators.relational.IsNullExpression;
import edu.uci.ics.perpetual.expression.operators.relational.ItemsListVisitor;
import edu.uci.ics.perpetual.expression.operators.relational.JsonOperator;
import edu.uci.ics.perpetual.expression.operators.relational.LikeExpression;
import edu.uci.ics.perpetual.expression.operators.relational.Matches;
import edu.uci.ics.perpetual.expression.operators.relational.MinorThan;
import edu.uci.ics.perpetual.expression.operators.relational.MinorThanEquals;
import edu.uci.ics.perpetual.expression.operators.relational.MultiExpressionList;
import edu.uci.ics.perpetual.expression.operators.relational.NotEqualsTo;
import edu.uci.ics.perpetual.expression.operators.relational.OldOracleJoinBinaryExpression;
import edu.uci.ics.perpetual.expression.operators.relational.RegExpMatchOperator;
import edu.uci.ics.perpetual.expression.operators.relational.RegExpMySQLOperator;
import edu.uci.ics.perpetual.expression.operators.relational.SupportsOldOracleJoinSyntax;
import edu.uci.ics.perpetual.schema.Column;
import edu.uci.ics.perpetual.schema.Table;
import edu.uci.ics.perpetual.statement.select.OrderByElement;
import edu.uci.ics.perpetual.statement.select.SelectVisitor;
import edu.uci.ics.perpetual.statement.select.SubSelect;
import edu.uci.ics.perpetual.statement.select.WithItem;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a string) an
 * {@link edu.uci.ics.perpetual.expression.Expression}
 */
public class ExpressionDeParser implements ExpressionVisitor, ItemsListVisitor {

    private static final String NOT = "NOT ";
    protected StringBuilder buffer = new StringBuilder();
    private SelectVisitor selectVisitor;
    private boolean useBracketsInExprList = true;
    private OrderByDeParser orderByDeParser = new OrderByDeParser();

    public ExpressionDeParser() {
    }

    /**
     * @param selectVisitor a SelectVisitor to de-parse SubSelects. It has to share the same<br>
     * StringBuilder as this object in order to work, as:
     *
     * <pre>
     * <code>
     * StringBuilder myBuf = new StringBuilder();
     * MySelectDeparser selectDeparser = new  MySelectDeparser();
     * selectDeparser.setBuffer(myBuf);
     * ExpressionDeParser expressionDeParser = new ExpressionDeParser(selectDeparser, myBuf);
     * </code>
     * </pre>
     *
     * @param buffer the buffer that will be filled with the expression
     */
    public ExpressionDeParser(SelectVisitor selectVisitor, StringBuilder buffer) {
        this(selectVisitor, buffer, new OrderByDeParser());
    }

    ExpressionDeParser(SelectVisitor selectVisitor, StringBuilder buffer, OrderByDeParser orderByDeParser) {
        this.selectVisitor = selectVisitor;
        this.buffer = buffer;
        this.orderByDeParser = orderByDeParser;
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

    @Override
    public void visit(Addition addition) {
        visitBinaryExpression(addition, " + ");
    }

    @Override
    public void visit(AndExpression andExpression) {
        visitBinaryExpression(andExpression, " AND ");
    }

    @Override
    public void visit(Between between) {
        between.getLeftExpression().accept(this);
        if (between.isNot()) {
            buffer.append(" NOT");
        }

        buffer.append(" BETWEEN ");
        between.getBetweenExpressionStart().accept(this);
        buffer.append(" AND ");
        between.getBetweenExpressionEnd().accept(this);

    }

    @Override
    public void visit(EqualsTo equalsTo) {
        visitOldOracleJoinBinaryExpression(equalsTo, " = ");
    }

    @Override
    public void visit(Division division) {
        visitBinaryExpression(division, " / ");
    }

    @Override
    public void visit(DoubleValue doubleValue) {
        buffer.append(doubleValue.toString());
    }

    @Override
    public void visit(HexValue hexValue) {
        buffer.append(hexValue.toString());
    }

    @Override
    public void visit(NotExpression notExpr) {
        buffer.append(NOT);
        notExpr.getExpression().accept(this);
    }

    @Override
    public void visit(BitwiseRightShift expr) {
        visitBinaryExpression(expr, " >> ");
    }

    @Override
    public void visit(BitwiseLeftShift expr) {
        visitBinaryExpression(expr, " << ");
    }

    public void visitOldOracleJoinBinaryExpression(OldOracleJoinBinaryExpression expression, String operator) {
        if (expression.isNot()) {
            buffer.append(NOT);
        }
        expression.getLeftExpression().accept(this);
        if (expression.getOldOracleJoinSyntax() == EqualsTo.ORACLE_JOIN_RIGHT) {
            buffer.append("(+)");
        }
        buffer.append(operator);
        expression.getRightExpression().accept(this);
        if (expression.getOldOracleJoinSyntax() == EqualsTo.ORACLE_JOIN_LEFT) {
            buffer.append("(+)");
        }
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        visitOldOracleJoinBinaryExpression(greaterThan, " > ");
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        visitOldOracleJoinBinaryExpression(greaterThanEquals, " >= ");

    }

    @Override
    public void visit(InExpression inExpression) {
        if (inExpression.getLeftExpression() == null) {
            inExpression.getLeftItemsList().accept(this);
        } else {
            inExpression.getLeftExpression().accept(this);
            if (inExpression.getOldOracleJoinSyntax() == SupportsOldOracleJoinSyntax.ORACLE_JOIN_RIGHT) {
                buffer.append("(+)");
            }
        }
        if (inExpression.isNot()) {
            buffer.append(" NOT");
        }
        buffer.append(" IN ");

        inExpression.getRightItemsList().accept(this);
    }

    @Override
    public void visit(SignedExpression signedExpression) {
        buffer.append(signedExpression.getSign());
        signedExpression.getExpression().accept(this);
    }

    @Override
    public void visit(IsNullExpression isNullExpression) {
        isNullExpression.getLeftExpression().accept(this);
        if (isNullExpression.isUseIsNull()) {
            if (isNullExpression.isNot()) {
                buffer.append(" NOT ISNULL");
            } else {
                buffer.append(" ISNULL");
            }
        } else {
            if (isNullExpression.isNot()) {
                buffer.append(" IS NOT NULL");
            } else {
                buffer.append(" IS NULL");
            }
        }
    }

    @Override
    public void visit(JdbcParameter jdbcParameter) {
        buffer.append("?");
        if (jdbcParameter.isUseFixedIndex()) {
            buffer.append(jdbcParameter.getIndex());
        }

    }

    @Override
    public void visit(LikeExpression likeExpression) {
        visitBinaryExpression(likeExpression, likeExpression.isCaseInsensitive() ? " ILIKE " : " LIKE ");
        String escape = likeExpression.getEscape();
        if (escape != null) {
            buffer.append(" ESCAPE '").append(escape).append('\'');
        }
    }

    @Override
    public void visit(ExistsExpression existsExpression) {
        if (existsExpression.isNot()) {
            buffer.append("NOT EXISTS ");
        } else {
            buffer.append("EXISTS ");
        }
        existsExpression.getRightExpression().accept(this);
    }

    @Override
    public void visit(LongValue longValue) {
        buffer.append(longValue.getStringValue());

    }

    @Override
    public void visit(MinorThan minorThan) {
        visitOldOracleJoinBinaryExpression(minorThan, " < ");

    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        visitOldOracleJoinBinaryExpression(minorThanEquals, " <= ");

    }

    @Override
    public void visit(Multiplication multiplication) {
        visitBinaryExpression(multiplication, " * ");

    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        visitOldOracleJoinBinaryExpression(notEqualsTo, " " + notEqualsTo.getStringExpression() + " ");

    }

    @Override
    public void visit(NullValue nullValue) {
        buffer.append(nullValue.toString());

    }

    @Override
    public void visit(OrExpression orExpression) {
        visitBinaryExpression(orExpression, " OR ");

    }

    @Override
    public void visit(Parenthesis parenthesis) {
        if (parenthesis.isNot()) {
            buffer.append(NOT);
        }

        buffer.append("(");
        parenthesis.getExpression().accept(this);
        buffer.append(")");

    }

    @Override
    public void visit(StringValue stringValue) {
        if (stringValue.getPrefix() != null) {
            buffer.append(stringValue.getPrefix());
        }
        buffer.append("'").append(stringValue.getValue()).append("'");

    }

    @Override
    public void visit(Subtraction subtraction) {
        visitBinaryExpression(subtraction, " - ");
    }

    private void visitBinaryExpression(BinaryExpression binaryExpression, String operator) {
        if (binaryExpression.isNot()) {
            buffer.append(NOT);
        }
        binaryExpression.getLeftExpression().accept(this);
        buffer.append(operator);
        binaryExpression.getRightExpression().accept(this);

    }

    @Override
    public void visit(SubSelect subSelect) {
        if (subSelect.isUseBrackets()) {
            buffer.append("(");
        }
        if (selectVisitor != null) {
            if (subSelect.getWithItemsList() != null) {
                buffer.append("WITH ");
                for (Iterator<WithItem> iter = subSelect.getWithItemsList().iterator(); iter.
                        hasNext();) {
                    iter.next().accept(selectVisitor);
                    if (iter.hasNext()) {
                        buffer.append(", ");
                    }
                    buffer.append(" ");
                }
                buffer.append(" ");
            }

            subSelect.getSelectBody().accept(selectVisitor);
        }
        if (subSelect.isUseBrackets()) {
            buffer.append(")");
        }
    }

    @Override
    public void visit(Column tableColumn) {
        final Table table = tableColumn.getTable();
        String tableName = null;
        if (table != null) {
            if (table.getAlias() != null) {
                tableName = table.getAlias().getName();
            } else {
                tableName = table.getFullyQualifiedName();
            }
        }
        if (tableName != null && !tableName.isEmpty()) {
            buffer.append(tableName).append(".");
        }

        buffer.append(tableColumn.getColumnName());
    }

    @Override
    public void visit(Function function) {
        if (function.isEscaped()) {
            buffer.append("{fn ");
        }

        buffer.append(function.getName());
        if (function.isAllColumns() && function.getParameters() == null) {
            buffer.append("(*)");
        } else if (function.getParameters() == null && function.getNamedParameters() == null) {
            buffer.append("()");
        } else {
            boolean oldUseBracketsInExprList = useBracketsInExprList;
            if (function.isDistinct()) {
                useBracketsInExprList = false;
                buffer.append("(DISTINCT ");
            } else if (function.isAllColumns()) {
                useBracketsInExprList = false;
                buffer.append("(ALL ");
            }
            if(function.getNamedParameters() != null){
                visit(function.getNamedParameters());
            }
            if(function.getParameters() != null){
                visit(function.getParameters());
            }
            useBracketsInExprList = oldUseBracketsInExprList;
            if (function.isDistinct() || function.isAllColumns()) {
                buffer.append(")");
            }
        }

        if (function.getAttribute() != null) {
            buffer.append(".").append(function.getAttribute());
        }
        if (function.getKeep() != null) {
            buffer.append(" ").append(function.getKeep());
        }

        if (function.isEscaped()) {
            buffer.append("}");
        }
    }

    @Override
    public void visit(ExpressionList expressionList) {
        if (useBracketsInExprList) {
            buffer.append("(");
        }
        for (Iterator<Expression> iter = expressionList.getExpressions().iterator(); iter.hasNext();) {
            Expression expression = iter.next();
            expression.accept(this);
            if (iter.hasNext()) {
                buffer.append(", ");
            }
        }
        if (useBracketsInExprList) {
            buffer.append(")");
        }
    }

    @Override
    public void visit(NamedExpressionList namedExpressionList) {
        if (useBracketsInExprList) {
            buffer.append("(");
        }
        List<String> names = namedExpressionList.getNames();
        List<Expression> expressions = namedExpressionList.getExpressions();
        for (int i=0; i<names.size(); i++){
            if(i>0){
                buffer.append(" ");
            }
            String name = names.get(i);
            if(! name.equals("")){
                buffer.append(name);
                buffer.append(" ");
            }
            expressions.get(i).accept(this);
        }
        if (useBracketsInExprList) {
            buffer.append(")");
        }
    }

    public SelectVisitor getSelectVisitor() {
        return selectVisitor;
    }

    public void setSelectVisitor(SelectVisitor visitor) {
        selectVisitor = visitor;
    }

    @Override
    public void visit(DateValue dateValue) {
        buffer.append("{d '").append(dateValue.getValue().toString()).append("'}");
    }

    @Override
    public void visit(TimestampValue timestampValue) {
        buffer.append("{ts '").append(timestampValue.getValue().toString()).append("'}");
    }

    @Override
    public void visit(TimeValue timeValue) {
        buffer.append("{t '").append(timeValue.getValue().toString()).append("'}");
    }

    @Override
    public void visit(CaseExpression caseExpression) {
        buffer.append("CASE ");
        Expression switchExp = caseExpression.getSwitchExpression();
        if (switchExp != null) {
            switchExp.accept(this);
            buffer.append(" ");
        }

        for (Expression exp : caseExpression.getWhenClauses()) {
            exp.accept(this);
        }

        Expression elseExp = caseExpression.getElseExpression();
        if (elseExp != null) {
            buffer.append("ELSE ");
            elseExp.accept(this);
            buffer.append(" ");
        }

        buffer.append("END");
    }

    @Override
    public void visit(WhenClause whenClause) {
        buffer.append("WHEN ");
        whenClause.getWhenExpression().accept(this);
        buffer.append(" THEN ");
        whenClause.getThenExpression().accept(this);
        buffer.append(" ");
    }

    @Override
    public void visit(AllComparisonExpression allComparisonExpression) {
        buffer.append("ALL ");
        allComparisonExpression.getSubSelect().accept((ExpressionVisitor) this);
    }

    @Override
    public void visit(AnyComparisonExpression anyComparisonExpression) {
        buffer.append(anyComparisonExpression.getAnyType().name()).append(" ");
        anyComparisonExpression.getSubSelect().accept((ExpressionVisitor) this);
    }

    @Override
    public void visit(Concat concat) {
        visitBinaryExpression(concat, " || ");
    }

    @Override
    public void visit(Matches matches) {
        visitOldOracleJoinBinaryExpression(matches, " @@ ");
    }

    @Override
    public void visit(BitwiseAnd bitwiseAnd) {
        visitBinaryExpression(bitwiseAnd, " & ");
    }

    @Override
    public void visit(BitwiseOr bitwiseOr) {
        visitBinaryExpression(bitwiseOr, " | ");
    }

    @Override
    public void visit(BitwiseXor bitwiseXor) {
        visitBinaryExpression(bitwiseXor, " ^ ");
    }

    @Override
    public void visit(CastExpression cast) {
        if (cast.isUseCastKeyword()) {
            buffer.append("CAST(");
            buffer.append(cast.getLeftExpression());
            buffer.append(" AS ");
            buffer.append(cast.getType());
            buffer.append(")");
        } else {
            buffer.append(cast.getLeftExpression());
            buffer.append("::");
            buffer.append(cast.getType());
        }
    }

    @Override
    public void visit(Modulo modulo) {
        visitBinaryExpression(modulo, " % ");
    }

    @Override
    public void visit(AnalyticExpression aexpr) {
        String name = aexpr.getName();
        Expression expression = aexpr.getExpression();
        Expression offset = aexpr.getOffset();
        Expression defaultValue = aexpr.getDefaultValue();
        boolean isAllColumns = aexpr.isAllColumns();
        KeepExpression keep = aexpr.getKeep();
        ExpressionList partitionExpressionList = aexpr.getPartitionExpressionList();
        List<OrderByElement> orderByElements = aexpr.getOrderByElements();
        WindowElement windowElement = aexpr.getWindowElement();

        buffer.append(name).append("(");
        if (aexpr.isDistinct()) {
            buffer.append("DISTINCT ");
        }
        if (expression != null) {
            expression.accept(this);
            if (offset != null) {
                buffer.append(", ");
                offset.accept(this);
                if (defaultValue != null) {
                    buffer.append(", ");
                    defaultValue.accept(this);
                }
            }
        } else if (isAllColumns) {
            buffer.append("*");
        }
        if (aexpr.isIgnoreNulls()) {
            buffer.append(" IGNORE NULLS");
        }
        buffer.append(") ");
        if (keep != null) {
            keep.accept(this);
            buffer.append(" ");
        }

        switch (aexpr.getType()) {
            case WITHIN_GROUP:
                buffer.append("WITHIN GROUP");
                break;
            default:
                buffer.append("OVER");
        }
        buffer.append(" (");

        if (partitionExpressionList != null && !partitionExpressionList.getExpressions().isEmpty()) {
            buffer.append("PARTITION BY ");
            List<Expression> expressions = partitionExpressionList.getExpressions();
            for (int i = 0; i < expressions.size(); i++) {
                if (i > 0) {
                    buffer.append(", ");
                }
                expressions.get(i).accept(this);
            }
            buffer.append(" ");
        }
        if (orderByElements != null && !orderByElements.isEmpty()) {
            buffer.append("ORDER BY ");
            orderByDeParser.setExpressionVisitor(this);
            orderByDeParser.setBuffer(buffer);
            for (int i = 0; i < orderByElements.size(); i++) {
                if (i > 0) {
                    buffer.append(", ");
                }
                orderByDeParser.deParseElement(orderByElements.get(i));
            }

            if (windowElement != null) {
                buffer.append(' ');
                buffer.append(windowElement);
            }
        }

        buffer.append(")");
    }

    @Override
    public void visit(ExtractExpression eexpr) {
        buffer.append("EXTRACT(").append(eexpr.getName());
        buffer.append(" FROM ");
        eexpr.getExpression().accept(this);
        buffer.append(')');
    }

    @Override
    public void visit(MultiExpressionList multiExprList) {
        for (Iterator<ExpressionList> it = multiExprList.getExprList().iterator(); it.hasNext();) {
            it.next().accept(this);
            if (it.hasNext()) {
                buffer.append(", ");
            }
        }
    }

    @Override
    public void visit(IntervalExpression iexpr) {
        buffer.append(iexpr.toString());
    }

    @Override
    public void visit(JdbcNamedParameter jdbcNamedParameter) {
        buffer.append(jdbcNamedParameter.toString());
    }

    @Override
    public void visit(OracleHierarchicalExpression oexpr) {
        buffer.append(oexpr.toString());
    }

    @Override
    public void visit(RegExpMatchOperator rexpr) {
        visitBinaryExpression(rexpr, " " + rexpr.getStringExpression() + " ");
    }

    @Override
    public void visit(RegExpMySQLOperator rexpr) {
        visitBinaryExpression(rexpr, " " + rexpr.getStringExpression() + " ");
    }

    @Override
    public void visit(JsonExpression jsonExpr) {
        buffer.append(jsonExpr.toString());
    }

    @Override
    public void visit(JsonOperator jsonExpr) {
        visitBinaryExpression(jsonExpr, " " + jsonExpr.getStringExpression() + " ");
    }

    @Override
    public void visit(UserVariable var) {
        buffer.append(var.toString());
    }

    @Override
    public void visit(NumericBind bind) {
        buffer.append(bind.toString());
    }

    @Override
    public void visit(KeepExpression aexpr) {
        buffer.append(aexpr.toString());
    }

    @Override
    public void visit(MySQLGroupConcat groupConcat) {
        buffer.append(groupConcat.toString());
    }

    @Override
    public void visit(ValueListExpression valueList) {
        buffer.append(valueList.toString());
    }

    @Override
    public void visit(RowConstructor rowConstructor) {
        if (rowConstructor.getName() != null) {
            buffer.append(rowConstructor.getName());
        }
        buffer.append("(");
        boolean first = true;
        for (Expression expr : rowConstructor.getExprList().getExpressions()) {
            if (first) {
                first = false;
            } else {
                buffer.append(", ");
            }
            expr.accept(this);
        }
        buffer.append(")");
    }

    @Override
    public void visit(OracleHint hint) {
        buffer.append(hint.toString());
    }

    @Override
    public void visit(TimeKeyExpression timeKeyExpression) {
        buffer.append(timeKeyExpression.toString());
    }

    @Override
    public void visit(DateTimeLiteralExpression literal) {
        buffer.append(literal.toString());
    }

}
