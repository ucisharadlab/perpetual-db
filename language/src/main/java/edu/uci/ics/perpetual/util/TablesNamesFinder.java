
package edu.uci.ics.perpetual.util;

import java.util.ArrayList;
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
import edu.uci.ics.perpetual.expression.operators.relational.RegExpMatchOperator;
import edu.uci.ics.perpetual.expression.operators.relational.RegExpMySQLOperator;
import edu.uci.ics.perpetual.schema.Column;
import edu.uci.ics.perpetual.schema.Table;
import edu.uci.ics.perpetual.statement.Block;
import edu.uci.ics.perpetual.statement.Commit;
import edu.uci.ics.perpetual.statement.SetStatement;
import edu.uci.ics.perpetual.statement.Statement;
import edu.uci.ics.perpetual.statement.StatementVisitor;
import edu.uci.ics.perpetual.statement.Statements;
import edu.uci.ics.perpetual.statement.UseStatement;
import edu.uci.ics.perpetual.statement.alter.Alter;
import edu.uci.ics.perpetual.statement.comment.Comment;
import edu.uci.ics.perpetual.statement.create.index.CreateIndex;
import edu.uci.ics.perpetual.statement.create.table.CreateTable;
import edu.uci.ics.perpetual.statement.create.view.AlterView;
import edu.uci.ics.perpetual.statement.create.view.CreateView;
import edu.uci.ics.perpetual.statement.delete.Delete;
import edu.uci.ics.perpetual.statement.drop.Drop;
import edu.uci.ics.perpetual.statement.execute.Execute;
import edu.uci.ics.perpetual.statement.insert.Insert;
import edu.uci.ics.perpetual.statement.merge.Merge;
import edu.uci.ics.perpetual.statement.replace.Replace;
import edu.uci.ics.perpetual.statement.select.AllColumns;
import edu.uci.ics.perpetual.statement.select.AllTableColumns;
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
import edu.uci.ics.perpetual.statement.truncate.Truncate;
import edu.uci.ics.perpetual.statement.update.Update;
import edu.uci.ics.perpetual.statement.upsert.Upsert;
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
     * @param table
     * @return
     */
    protected String extractTableName(Table table) {
        return table.getFullyQualifiedName();
    }

    @Override
    public void visit(Table tableName) {
        String tableWholeName = extractTableName(tableName);
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
        if (allowColumnProcessing && tableColumn.getTable() != null && tableColumn.getTable().getName() != null) {
            visit(tableColumn.getTable());
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
    public void visit(Function function) {
        ExpressionList exprList = function.getParameters();
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
    public void visit(AllTableColumns allTableColumns) {
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
    public void visit(Delete delete) {
        visit(delete.getTable());

        if (delete.getJoins() != null) {
            for (Join join : delete.getJoins()) {
                join.getRightItem().accept(this);
            }
        }

        if (delete.getWhere() != null) {
            delete.getWhere().accept(this);
        }
    }

    @Override
    public void visit(Update update) {
        for (Table table : update.getTables()) {
            visit(table);
        }
        if (update.getExpressions() != null) {
            for (Expression expression : update.getExpressions()) {
                expression.accept(this);
            }
        }

        if (update.getFromItem() != null) {
            update.getFromItem().accept(this);
        }

        if (update.getJoins() != null) {
            for (Join join : update.getJoins()) {
                join.getRightItem().accept(this);
            }
        }

        if (update.getWhere() != null) {
            update.getWhere().accept(this);
        }
    }

    @Override
    public void visit(Insert insert) {
        visit(insert.getTable());
        if (insert.getItemsList() != null) {
            insert.getItemsList().accept(this);
        }
        if (insert.getSelect() != null) {
            visit(insert.getSelect());
        }
    }

    @Override
    public void visit(Replace replace) {
        visit(replace.getTable());
        if (replace.getExpressions() != null) {
            for (Expression expression : replace.getExpressions()) {
                expression.accept(this);
            }
        }
        if (replace.getItemsList() != null) {
            replace.getItemsList().accept(this);
        }
    }

    @Override
    public void visit(Drop drop) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(Truncate truncate) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(CreateIndex createIndex) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(CreateTable create) {
        visit(create.getTable());
        if (create.getSelect() != null) {
            create.getSelect().accept(this);
        }
    }

    @Override
    public void visit(CreateView createView) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(Alter alter) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(Statements stmts) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(Execute execute) {
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
    public void visit(Merge merge) {
        visit(merge.getTable());
        if (merge.getUsingTable() != null) {
            merge.getUsingTable().accept(this);
        } else if (merge.getUsingSelect() != null) {
            merge.getUsingSelect().accept((FromItemVisitor) this);
        }
    }

    @Override
    public void visit(OracleHint hint) {
    }

    @Override
    public void visit(TableFunction valuesList) {
    }

    @Override
    public void visit(AlterView alterView) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(TimeKeyExpression timeKeyExpression) {
    }

    @Override
    public void visit(DateTimeLiteralExpression literal) {

    }

    @Override
    public void visit(Commit commit) {

    }

    @Override
    public void visit(Upsert upsert) {
        visit(upsert.getTable());
        if (upsert.getItemsList() != null) {
            upsert.getItemsList().accept(this);
        }
        if (upsert.getSelect() != null) {
            visit(upsert.getSelect());
        }
    }

    @Override
    public void visit(UseStatement use) {
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
    public void visit(Comment comment) {
        if (comment.getTable() != null) {
            visit(comment.getTable());
        }
        if (comment.getColumn() != null) {
            Table table = comment.getColumn().getTable();
            if (table != null) {
                visit(table);
            }
        }
    }
  
    @Override
    public void visit(ValuesStatement values) {
        for (Expression expr : values.getExpressions()) {
            expr.accept(this);
        }
    }
}
