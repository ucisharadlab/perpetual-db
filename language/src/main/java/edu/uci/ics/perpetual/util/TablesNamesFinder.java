
package edu.uci.ics.perpetual.util;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.perpetual.expressions.AllComparisonExpression;
import edu.uci.ics.perpetual.expressions.AnalyticExpression;
import edu.uci.ics.perpetual.expressions.AnyComparisonExpression;
import edu.uci.ics.perpetual.expressions.BinaryExpression;
import edu.uci.ics.perpetual.expressions.CaseExpression;
import edu.uci.ics.perpetual.expressions.CastExpression;
import edu.uci.ics.perpetual.expressions.DateTimeLiteralExpression;
import edu.uci.ics.perpetual.expressions.DateValue;
import edu.uci.ics.perpetual.expressions.DoubleValue;
import edu.uci.ics.perpetual.expressions.Expression;
import edu.uci.ics.perpetual.expressions.ExpressionVisitor;
import edu.uci.ics.perpetual.expressions.ExtractExpression;
import edu.uci.ics.perpetual.expressions.UDFFunction;
import edu.uci.ics.perpetual.expressions.HexValue;
import edu.uci.ics.perpetual.expressions.IntervalExpression;
import edu.uci.ics.perpetual.expressions.JdbcNamedParameter;
import edu.uci.ics.perpetual.expressions.JdbcParameter;
import edu.uci.ics.perpetual.expressions.JsonExpression;
import edu.uci.ics.perpetual.expressions.KeepExpression;
import edu.uci.ics.perpetual.expressions.LongValue;
import edu.uci.ics.perpetual.expressions.MySQLGroupConcat;
import edu.uci.ics.perpetual.expressions.NotExpression;
import edu.uci.ics.perpetual.expressions.NullValue;
import edu.uci.ics.perpetual.expressions.NumericBind;
import edu.uci.ics.perpetual.expressions.OracleHierarchicalExpression;
import edu.uci.ics.perpetual.expressions.OracleHint;
import edu.uci.ics.perpetual.expressions.Parenthesis;
import edu.uci.ics.perpetual.expressions.RowConstructor;
import edu.uci.ics.perpetual.expressions.SignedExpression;
import edu.uci.ics.perpetual.expressions.StringValue;
import edu.uci.ics.perpetual.expressions.TimeKeyExpression;
import edu.uci.ics.perpetual.expressions.TimeValue;
import edu.uci.ics.perpetual.expressions.TimestampValue;
import edu.uci.ics.perpetual.expressions.UserVariable;
import edu.uci.ics.perpetual.expressions.ValueListExpression;
import edu.uci.ics.perpetual.expressions.WhenClause;
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
import edu.uci.ics.perpetual.expressions.operators.relational.ExpressionList;
import edu.uci.ics.perpetual.expressions.operators.relational.NamedExpressionList;
import edu.uci.ics.perpetual.expressions.operators.relational.GreaterThan;
import edu.uci.ics.perpetual.expressions.operators.relational.GreaterThanEquals;
import edu.uci.ics.perpetual.expressions.operators.relational.InExpression;
import edu.uci.ics.perpetual.expressions.operators.relational.IsNullExpression;
import edu.uci.ics.perpetual.expressions.operators.relational.ItemsListVisitor;
import edu.uci.ics.perpetual.expressions.operators.relational.JsonOperator;
import edu.uci.ics.perpetual.expressions.operators.relational.LikeExpression;
import edu.uci.ics.perpetual.expressions.operators.relational.Matches;
import edu.uci.ics.perpetual.expressions.operators.relational.MinorThan;
import edu.uci.ics.perpetual.expressions.operators.relational.MinorThanEquals;
import edu.uci.ics.perpetual.expressions.operators.relational.MultiExpressionList;
import edu.uci.ics.perpetual.expressions.operators.relational.NotEqualsTo;
import edu.uci.ics.perpetual.expressions.operators.relational.RegExpMatchOperator;
import edu.uci.ics.perpetual.expressions.operators.relational.RegExpMySQLOperator;
import edu.uci.ics.perpetual.schema.Column;
import edu.uci.ics.perpetual.schema.DataSourceType;
import edu.uci.ics.perpetual.schema.Type;
import edu.uci.ics.perpetual.statement.Block;
import edu.uci.ics.perpetual.statement.SetStatement;
import edu.uci.ics.perpetual.statement.Statement;
import edu.uci.ics.perpetual.statement.StatementVisitor;
import edu.uci.ics.perpetual.statement.Statements;
import edu.uci.ics.perpetual.statement.add.AddAcquisitionFunction;
import edu.uci.ics.perpetual.statement.add.AddDataSource;
import edu.uci.ics.perpetual.statement.add.AddRequest;
import edu.uci.ics.perpetual.statement.add.AddTag;
import edu.uci.ics.perpetual.statement.create.type.*;
import edu.uci.ics.perpetual.statement.drop.Drop;
import edu.uci.ics.perpetual.statement.insert.Insert;
import edu.uci.ics.perpetual.statement.select.AllColumns;
import edu.uci.ics.perpetual.statement.select.AllTypeColumns;
import edu.uci.ics.perpetual.statement.select.FromItemVisitor;
import edu.uci.ics.perpetual.statement.select.Join;
import edu.uci.ics.perpetual.statement.select.LateralSubSelect;
import edu.uci.ics.perpetual.statement.select.ParenthesisFromItem;
import edu.uci.ics.perpetual.statement.select.PlainSelect;
import edu.uci.ics.perpetual.statement.select.Select;
import edu.uci.ics.perpetual.statement.select.SelectBody;
import edu.uci.ics.perpetual.statement.select.SelectExpressionItem;
import edu.uci.ics.perpetual.statement.select.SelectItem;
import edu.uci.ics.perpetual.statement.select.SelectItemVisitor;
import edu.uci.ics.perpetual.statement.select.SelectVisitor;
import edu.uci.ics.perpetual.statement.select.SetOperationList;
import edu.uci.ics.perpetual.statement.select.SubJoin;
import edu.uci.ics.perpetual.statement.select.SubSelect;
import edu.uci.ics.perpetual.statement.select.TableFunction;
import edu.uci.ics.perpetual.statement.select.ValuesList;
import edu.uci.ics.perpetual.statement.select.WithItem;
import edu.uci.ics.perpetual.statement.values.ValuesStatement;

/**
 * Find all used tables within an select statement.
 *
 * Override extractTableName method to modify the extracted table names (e.g. without schema).
 */
public class TablesNamesFinder implements SelectVisitor, FromItemVisitor, ExpressionVisitor, ItemsListVisitor, SelectItemVisitor, StatementVisitor {

    private static final String NOT_SUPPORTED_YET = "Not supported yet.";
    private List<String> tables;
    private boolean allowColumnProcessing = false;

    /**
     * There are special names, that are not table names but are parsed as tables. These names are
     * collected here and are not included in the tables - names anymore.
     */
    private List<String> otherItemNames;

    /**
     * Main entry for this Tool class. A list of found tables is returned.
     */
    public List<String> getTableList(Statement statement) {
        init(false);
        statement.accept(this);
        return tables;
    }

    @Override
    public void visit(Select select) {
        if (select.getWithItemsList() != null) {
            for (WithItem withItem : select.getWithItemsList()) {
                withItem.accept(this);
            }
        }
        select.getSelectBody().accept(this);
    }

    /**
     * Main entry for this Tool class. A list of found tables is returned.
     */
    public List<String> getTableList(Expression expr) {
        init(true);
        expr.accept(this);
        return tables;
    }

    @Override
    public void visit(WithItem withItem) {
        otherItemNames.add(withItem.getName().toLowerCase());
        withItem.getSelectBody().accept(this);
    }

    @Override
    public void visit(PlainSelect plainSelect) {
        if (plainSelect.getSelectItems() != null) {
            for (SelectItem item : plainSelect.getSelectItems()) {
                item.accept(this);
            }
        }

        if (plainSelect.getFromItem() != null) {
            plainSelect.getFromItem().accept(this);
        }

        if (plainSelect.getJoins() != null) {
            for (Join join : plainSelect.getJoins()) {
                join.getRightItem().accept(this);
            }
        }
        if (plainSelect.getWhere() != null) {
            plainSelect.getWhere().accept(this);
        }

        if (plainSelect.getHaving() != null) {
            plainSelect.getHaving().accept(this);
        }

        if (plainSelect.getOracleHierarchical() != null) {
            plainSelect.getOracleHierarchical().accept(this);
        }
    }

    /**
     * Override to adapt the tableName generation (e.g. with / without schema).
     *
     * @param type
     * @return
     */
    protected String extractTableName(Type type) {
        return type.getFullyQualifiedName();
    }

    @Override
    public void visit(Type typeName) {
        String tableWholeName = extractTableName(typeName);
        if (!otherItemNames.contains(tableWholeName.toLowerCase())
                && !tables.contains(tableWholeName)) {
            tables.add(tableWholeName);
        }
    }

    @Override
    public void visit(SubSelect subSelect) {
        if (subSelect.getWithItemsList() != null) {
            for (WithItem withItem : subSelect.getWithItemsList()) {
                withItem.accept(this);
            }
        }
        subSelect.getSelectBody().accept(this);
    }

    @Override
    public void visit(Addition addition) {
        visitBinaryExpression(addition);
    }

    @Override
    public void visit(AndExpression andExpression) {
        visitBinaryExpression(andExpression);
    }

    @Override
    public void visit(Between between) {
        between.getLeftExpression().accept(this);
        between.getBetweenExpressionStart().accept(this);
        between.getBetweenExpressionEnd().accept(this);
    }

    @Override
    public void visit(Column tableColumn) {
        if (allowColumnProcessing && tableColumn.getType() != null && tableColumn.getType().getName() != null) {
            visit(tableColumn.getType());
        }
    }

    @Override
    public void visit(Division division) {
        visitBinaryExpression(division);
    }

    @Override
    public void visit(DoubleValue doubleValue) {
    }

    @Override
    public void visit(EqualsTo equalsTo) {
        visitBinaryExpression(equalsTo);
    }

    @Override
    public void visit(UDFFunction UDFFunction) {
        ExpressionList exprList = UDFFunction.getParameters();
        if (exprList != null) {
            visit(exprList);
        }
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        visitBinaryExpression(greaterThan);
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        visitBinaryExpression(greaterThanEquals);
    }

    @Override
    public void visit(InExpression inExpression) {
        if (inExpression.getLeftExpression() != null) {
            inExpression.getLeftExpression().accept(this);
        } else if (inExpression.getLeftItemsList() != null) {
            inExpression.getLeftItemsList().accept(this);
        }
        inExpression.getRightItemsList().accept(this);
    }

    @Override
    public void visit(SignedExpression signedExpression) {
        signedExpression.getExpression().accept(this);
    }

    @Override
    public void visit(IsNullExpression isNullExpression) {
    }

    @Override
    public void visit(JdbcParameter jdbcParameter) {
    }

    @Override
    public void visit(LikeExpression likeExpression) {
        visitBinaryExpression(likeExpression);
    }

    @Override
    public void visit(ExistsExpression existsExpression) {
        existsExpression.getRightExpression().accept(this);
    }

    @Override
    public void visit(LongValue longValue) {
    }

    @Override
    public void visit(MinorThan minorThan) {
        visitBinaryExpression(minorThan);
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        visitBinaryExpression(minorThanEquals);
    }

    @Override
    public void visit(Multiplication multiplication) {
        visitBinaryExpression(multiplication);
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        visitBinaryExpression(notEqualsTo);
    }

    @Override
    public void visit(NullValue nullValue) {
    }

    @Override
    public void visit(OrExpression orExpression) {
        visitBinaryExpression(orExpression);
    }

    @Override
    public void visit(Parenthesis parenthesis) {
        parenthesis.getExpression().accept(this);
    }

    @Override
    public void visit(StringValue stringValue) {
    }

    @Override
    public void visit(Subtraction subtraction) {
        visitBinaryExpression(subtraction);
    }

    @Override
    public void visit(NotExpression notExpr) {
        notExpr.getExpression().accept(this);
    }

    @Override
    public void visit(BitwiseRightShift expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(BitwiseLeftShift expr) {
        visitBinaryExpression(expr);
    }

    public void visitBinaryExpression(BinaryExpression binaryExpression) {
        binaryExpression.getLeftExpression().accept(this);
        binaryExpression.getRightExpression().accept(this);
    }

    @Override
    public void visit(ExpressionList expressionList) {
        for (Expression expression : expressionList.getExpressions()) {
            expression.accept(this);
        }
    }

    @Override
    public void visit(NamedExpressionList namedExpressionList) {
        for (Expression expression : namedExpressionList.getExpressions()) {
            expression.accept(this);
        }
    }

    @Override
    public void visit(DateValue dateValue) {
    }

    @Override
    public void visit(TimestampValue timestampValue) {
    }

    @Override
    public void visit(TimeValue timeValue) {
    }

    /*
     * (non-Javadoc)
     *
     * @see edu.uci.ics.perpetual.expression.ExpressionVisitor#visit(edu.uci.ics.perpetual.expression.CaseExpression)
     */
    @Override
    public void visit(CaseExpression caseExpression) {
        if (caseExpression.getSwitchExpression() != null) {
            caseExpression.getSwitchExpression().accept(this);
        }
        if (caseExpression.getWhenClauses() != null) {
            for (WhenClause when : caseExpression.getWhenClauses()) {
                when.accept(this);
            }
        }
        if (caseExpression.getElseExpression() != null) {
            caseExpression.getElseExpression().accept(this);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see edu.uci.ics.perpetual.expression.ExpressionVisitor#visit(edu.uci.ics.perpetual.expression.WhenClause)
     */
    @Override
    public void visit(WhenClause whenClause) {
        if (whenClause.getWhenExpression() != null) {
            whenClause.getWhenExpression().accept(this);
        }
        if (whenClause.getThenExpression() != null) {
            whenClause.getThenExpression().accept(this);
        }
    }

    @Override
    public void visit(AllComparisonExpression allComparisonExpression) {
        allComparisonExpression.getSubSelect().getSelectBody().accept(this);
    }

    @Override
    public void visit(AnyComparisonExpression anyComparisonExpression) {
        anyComparisonExpression.getSubSelect().getSelectBody().accept(this);
    }

    @Override
    public void visit(SubJoin subjoin) {
        subjoin.getLeft().accept(this);
        for (Join join : subjoin.getJoinList()) {
            join.getRightItem().accept(this);
        }
    }

    @Override
    public void visit(Concat concat) {
        visitBinaryExpression(concat);
    }

    @Override
    public void visit(Matches matches) {
        visitBinaryExpression(matches);
    }

    @Override
    public void visit(BitwiseAnd bitwiseAnd) {
        visitBinaryExpression(bitwiseAnd);
    }

    @Override
    public void visit(BitwiseOr bitwiseOr) {
        visitBinaryExpression(bitwiseOr);
    }

    @Override
    public void visit(BitwiseXor bitwiseXor) {
        visitBinaryExpression(bitwiseXor);
    }

    @Override
    public void visit(CastExpression cast) {
        cast.getLeftExpression().accept(this);
    }

    @Override
    public void visit(Modulo modulo) {
        visitBinaryExpression(modulo);
    }

    @Override
    public void visit(AnalyticExpression analytic) {
    }

    @Override
    public void visit(SetOperationList list) {
        for (SelectBody plainSelect : list.getSelects()) {
            plainSelect.accept(this);
        }
    }

    @Override
    public void visit(ExtractExpression eexpr) {
    }

    @Override
    public void visit(LateralSubSelect lateralSubSelect) {
        lateralSubSelect.getSubSelect().getSelectBody().accept(this);
    }

    @Override
    public void visit(MultiExpressionList multiExprList) {
        for (ExpressionList exprList : multiExprList.getExprList()) {
            exprList.accept(this);
        }
    }

    @Override
    public void visit(ValuesList valuesList) {
    }

    /**
     * Initializes table names collector. Important is the usage of Column instances to find table
     * names. This is only allowed for expression parsing, where a better place for tablenames could
     * not be there. For complete statements only from items are used to avoid some alias as
     * tablenames.
     *
     * @param allowColumnProcessing
     */
    protected void init(boolean allowColumnProcessing) {
        otherItemNames = new ArrayList<String>();
        tables = new ArrayList<String>();
        this.allowColumnProcessing = allowColumnProcessing;
    }

    @Override
    public void visit(IntervalExpression iexpr) {
    }

    @Override
    public void visit(JdbcNamedParameter jdbcNamedParameter) {
    }

    @Override
    public void visit(OracleHierarchicalExpression oexpr) {
        if (oexpr.getStartExpression() != null) {
            oexpr.getStartExpression().accept(this);
        }

        if (oexpr.getConnectExpression() != null) {
            oexpr.getConnectExpression().accept(this);
        }
    }

    @Override
    public void visit(RegExpMatchOperator rexpr) {
        visitBinaryExpression(rexpr);
    }

    @Override
    public void visit(RegExpMySQLOperator rexpr) {
        visitBinaryExpression(rexpr);
    }

    @Override
    public void visit(JsonExpression jsonExpr) {
    }

    @Override
    public void visit(JsonOperator jsonExpr) {
    }

    @Override
    public void visit(AllColumns allColumns) {
    }

    @Override
    public void visit(AllTypeColumns allTypeColumns) {
    }

    @Override
    public void visit(SelectExpressionItem item) {
        item.getExpression().accept(this);
    }

    @Override
    public void visit(UserVariable var) {
    }

    @Override
    public void visit(NumericBind bind) {

    }

    @Override
    public void visit(KeepExpression aexpr) {
    }

    @Override
    public void visit(MySQLGroupConcat groupConcat) {
    }

    @Override
    public void visit(ValueListExpression valueList) {
        valueList.getExpressionList().accept(this);
    }


    @Override
    public void visit(Insert insert) {
        visit(insert.getType());
        if (insert.getItemsList() != null) {
            insert.getItemsList().accept(this);
        }
        if (insert.getSelect() != null) {
            visit(insert.getSelect());
        }
    }

    @Override
    public void visit(AddTag insert) {
        visit(insert.getType());
        if (insert.getItemsList() != null) {
            insert.getItemsList().accept(this);
        }
        if (insert.getSelect() != null) {
            visit(insert.getSelect());
        }
    }

    @Override
    public void visit(AddRequest insert) {
        visit(insert.getType());
        if (insert.getItemsList() != null) {
            insert.getItemsList().accept(this);
        }
        if (insert.getSelect() != null) {
            visit(insert.getSelect());
        }
    }

    @Override
    public void visit(AddAcquisitionFunction insert) {
        visit(insert.getType());
        if (insert.getItemsList() != null) {
            insert.getItemsList().accept(this);
        }
        if (insert.getSelect() != null) {
            visit(insert.getSelect());
        }
    }

    @Override
    public void visit(AddDataSource insert) {
        visit(insert.getType());
        if (insert.getItemsList() != null) {
            insert.getItemsList().accept(this);
        }
        if (insert.getSelect() != null) {
            visit(insert.getSelect());
        }
    }


    @Override
    public void visit(Drop drop) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(CreateMetadataType create) {
        visit(create.getType());
        if (create.getSelect() != null) {
            create.getSelect().accept(this);
        }
    }

    @Override
    public void visit(CreateTable create) {
        visit(create.getType());
        if (create.getSelect() != null) {
            create.getSelect().accept(this);
        }
    }

    @Override
    public void visit(CreateRawType create) {
        visit(create.getType());
        if (create.getSelect() != null) {
            create.getSelect().accept(this);
        }
    }

    @Override
    public void visit(CreateFunction create) {
        visit(create.getType());
        if (create.getSelect() != null) {
            create.getSelect().accept(this);
        }
    }

    @Override
    public void visit(CreateDataSourceType create) {
        visit(create.getType());
        if (create.getSelect() != null) {
            create.getSelect().accept(this);
        }
    }

    private void visit(DataSourceType type) {
    }

    @Override
    public void visit(Statements stmts) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }


    @Override
    public void visit(SetStatement set) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(RowConstructor rowConstructor) {
        for (Expression expr : rowConstructor.getExprList().getExpressions()) {
            expr.accept(this);
        }
    }

    @Override
    public void visit(HexValue hexValue) {

    }

    @Override
    public void visit(OracleHint hint) {
    }

    @Override
    public void visit(TableFunction valuesList) {
    }

    @Override
    public void visit(TimeKeyExpression timeKeyExpression) {
    }

    @Override
    public void visit(DateTimeLiteralExpression literal) {

    }

    @Override
    public void visit(ParenthesisFromItem parenthesis) {
        parenthesis.getFromItem().accept(this);
    }

    @Override
    public void visit(Block block) {
        if (block.getStatements() != null) {
            visit(block.getStatements());
        }
    }
  
    @Override
    public void visit(ValuesStatement values) {
        for (Expression expr : values.getExpressions()) {
            expr.accept(this);
        }
    }
}
