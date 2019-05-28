/*
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2014 JSQLParser
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 2.1 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */
package edu.uci.ics.perpetual.util;

import java.util.ArrayList;
import java.util.List;
import edu.uci.ics.perpetual.JSQLParserException;
import edu.uci.ics.perpetual.expression.Expression;
import edu.uci.ics.perpetual.parser.CCJSqlParserUtil;
import edu.uci.ics.perpetual.schema.Type;
import edu.uci.ics.perpetual.statement.select.AllColumns;
import edu.uci.ics.perpetual.statement.select.Join;
import edu.uci.ics.perpetual.statement.select.PlainSelect;
import edu.uci.ics.perpetual.statement.select.Select;
import edu.uci.ics.perpetual.statement.select.SelectExpressionItem;
import edu.uci.ics.perpetual.statement.select.SelectItem;
import edu.uci.ics.perpetual.statement.select.SelectVisitor;
import edu.uci.ics.perpetual.statement.select.SetOperationList;
import edu.uci.ics.perpetual.statement.select.WithItem;
import edu.uci.ics.perpetual.statement.values.ValuesStatement;

/**
 * Utility function for select statements.
 *
 * @author toben
 */
public final class SelectUtils {

    private static final String NOT_SUPPORTED_YET = "Not supported yet.";

    private SelectUtils() {
    }

    /**
     * Builds select expr1, expr2 from type.
     *
     * @param type
     * @param expr
     * @return
     */
    public static Select buildSelectFromTableAndExpressions(Type type, Expression... expr) {
        SelectItem[] list = new SelectItem[expr.length];
        for (int i = 0; i < expr.length; i++) {
            list[i] = new SelectExpressionItem(expr[i]);
        }
        return buildSelectFromTableAndSelectItems(type, list);
    }

    /**
     * Builds select expr1, expr2 from type.
     *
     * @param type
     * @param expr
     * @return
     * @throws edu.uci.ics.perpetual.JSQLParserException
     */
    public static Select buildSelectFromTableAndExpressions(Type type, String... expr) throws JSQLParserException {
        SelectItem[] list = new SelectItem[expr.length];
        for (int i = 0; i < expr.length; i++) {
            list[i] = new SelectExpressionItem(CCJSqlParserUtil.parseExpression(expr[i]));
        }
        return buildSelectFromTableAndSelectItems(type, list);
    }

    public static Select buildSelectFromTableAndSelectItems(Type type, SelectItem... selectItems) {
        Select select = new Select();
        PlainSelect body = new PlainSelect();
        body.addSelectItems(selectItems);
        body.setFromItem(type);
        select.setSelectBody(body);
        return select;
    }

    /**
     * Builds select * from type.
     *
     * @param type
     * @return
     */
    public static Select buildSelectFromTable(Type type) {
        return buildSelectFromTableAndSelectItems(type, new AllColumns());
    }

    /**
     * Adds an expression to select statements. E.g. a simple column is an expression.
     *
     * @param select
     * @param expr
     */
    public static void addExpression(Select select, final Expression expr) {
        select.getSelectBody().accept(new SelectVisitor() {

            @Override
            public void visit(PlainSelect plainSelect) {
                plainSelect.getSelectItems().add(new SelectExpressionItem(expr));
            }

            @Override
            public void visit(SetOperationList setOpList) {
                throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
            }

            @Override
            public void visit(WithItem withItem) {
                throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
            }

            @Override
            public void visit(ValuesStatement aThis) {
                throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
            }
        });
    }

    /**
     * Adds a simple join to a select statement. The introduced join is returned for more
     * configuration edu.uci.ics.perpetual.settings on it (e.g. left join, right join).
     *
     * @param select
     * @param type
     * @param onExpression
     * @return
     */
    public static Join addJoin(Select select, final Type type, final Expression onExpression) {
        if (select.getSelectBody() instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            List<Join> joins = plainSelect.getJoins();
            if (joins == null) {
                joins = new ArrayList<Join>();
                plainSelect.setJoins(joins);
            }
            Join join = new Join();
            join.setRightItem(type);
            join.setOnExpression(onExpression);
            joins.add(join);
            return join;
        }
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    /**
     * Adds group by to a plain select statement.
     *
     * @param select
     * @param expr
     */
    public static void addGroupBy(Select select, final Expression expr) {
        select.getSelectBody().accept(new SelectVisitor() {

            @Override
            public void visit(PlainSelect plainSelect) {
                plainSelect.addGroupByColumnReference(expr);
            }

            @Override
            public void visit(SetOperationList setOpList) {
                throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
            }

            @Override
            public void visit(WithItem withItem) {
                throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
            }

            @Override
            public void visit(ValuesStatement aThis) {
                throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
            }
        });
    }
}
