
package edu.uci.ics.perpetual.expressions;

import edu.uci.ics.perpetual.expressions.operators.arithmetic.Addition;
import edu.uci.ics.perpetual.expressions.operators.arithmetic.BitwiseAnd;
import edu.uci.ics.perpetual.expressions.operators.arithmetic.BitwiseLeftShift;
import edu.uci.ics.perpetual.expressions.operators.arithmetic.BitwiseOr;
import edu.uci.ics.perpetual.expressions.operators.arithmetic.BitwiseRightShift;
import edu.uci.ics.perpetual.expressions.operators.arithmetic.BitwiseXor;
import edu.uci.ics.perpetual.expressions.operators.arithmetic.Concat;
import edu.uci.ics.perpetual.expressions.operators.arithmetic.Division;
import edu.uci.ics.perpetual.expressions.operators.arithmetic.Modulo;
import edu.uci.ics.perpetual.expressions.operators.arithmetic.Multiplication;
import edu.uci.ics.perpetual.expressions.operators.arithmetic.Subtraction;
import edu.uci.ics.perpetual.expressions.operators.conditional.AndExpression;
import edu.uci.ics.perpetual.expressions.operators.conditional.OrExpression;
import edu.uci.ics.perpetual.expressions.operators.relational.Between;
import edu.uci.ics.perpetual.expressions.operators.relational.EqualsTo;
import edu.uci.ics.perpetual.expressions.operators.relational.ExistsExpression;
import edu.uci.ics.perpetual.expressions.operators.relational.GreaterThan;
import edu.uci.ics.perpetual.expressions.operators.relational.GreaterThanEquals;
import edu.uci.ics.perpetual.expressions.operators.relational.InExpression;
import edu.uci.ics.perpetual.expressions.operators.relational.IsNullExpression;
import edu.uci.ics.perpetual.expressions.operators.relational.LikeExpression;
import edu.uci.ics.perpetual.expressions.operators.relational.Matches;
import edu.uci.ics.perpetual.expressions.operators.relational.MinorThan;
import edu.uci.ics.perpetual.expressions.operators.relational.MinorThanEquals;
import edu.uci.ics.perpetual.expressions.operators.relational.NotEqualsTo;
import edu.uci.ics.perpetual.expressions.operators.relational.RegExpMatchOperator;
import edu.uci.ics.perpetual.expressions.operators.relational.RegExpMySQLOperator;
import edu.uci.ics.perpetual.expressions.operators.relational.JsonOperator;
import edu.uci.ics.perpetual.schema.Column;
import edu.uci.ics.perpetual.statement.select.SubSelect;

public interface ExpressionVisitor {

    public void visit(BitwiseRightShift aThis);

    public void visit(BitwiseLeftShift aThis);

    void visit(NullValue nullValue);

    void visit(UDFFunction UDFFunction);

    void visit(SignedExpression signedExpression);

    void visit(JdbcParameter jdbcParameter);

    void visit(JdbcNamedParameter jdbcNamedParameter);

    void visit(DoubleValue doubleValue);

    void visit(LongValue longValue);

    void visit(HexValue hexValue);

    void visit(DateValue dateValue);

    void visit(TimeValue timeValue);

    void visit(TimestampValue timestampValue);

    void visit(Parenthesis parenthesis);

    void visit(StringValue stringValue);

    void visit(Addition addition);

    void visit(Division division);

    void visit(Multiplication multiplication);

    void visit(Subtraction subtraction);

    void visit(AndExpression andExpression);

    void visit(OrExpression orExpression);

    void visit(Between between);

    void visit(EqualsTo equalsTo);

    void visit(GreaterThan greaterThan);

    void visit(GreaterThanEquals greaterThanEquals);

    void visit(InExpression inExpression);

    void visit(IsNullExpression isNullExpression);

    void visit(LikeExpression likeExpression);

    void visit(MinorThan minorThan);

    void visit(MinorThanEquals minorThanEquals);

    void visit(NotEqualsTo notEqualsTo);

    void visit(Column tableColumn);

    void visit(SubSelect subSelect);

    void visit(CaseExpression caseExpression);

    void visit(WhenClause whenClause);

    void visit(ExistsExpression existsExpression);

    void visit(AllComparisonExpression allComparisonExpression);

    void visit(AnyComparisonExpression anyComparisonExpression);

    void visit(Concat concat);

    void visit(Matches matches);

    void visit(BitwiseAnd bitwiseAnd);

    void visit(BitwiseOr bitwiseOr);

    void visit(BitwiseXor bitwiseXor);

    void visit(CastExpression cast);

    void visit(Modulo modulo);

    void visit(AnalyticExpression aexpr);

    void visit(ExtractExpression eexpr);

    void visit(IntervalExpression iexpr);

    void visit(OracleHierarchicalExpression oexpr);

    void visit(RegExpMatchOperator rexpr);

    void visit(JsonExpression jsonExpr);

    void visit(JsonOperator jsonExpr);

    void visit(RegExpMySQLOperator regExpMySQLOperator);

    void visit(UserVariable var);

    void visit(NumericBind bind);

    void visit(KeepExpression aexpr);

    void visit(MySQLGroupConcat groupConcat);
    
    void visit(ValueListExpression valueList);

    void visit(RowConstructor rowConstructor);

    void visit(OracleHint hint);

    void visit(TimeKeyExpression timeKeyExpression);

    void visit(DateTimeLiteralExpression literal);

    public void visit(NotExpression aThis);

}
