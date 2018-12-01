/*
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2013 JSQLParser
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
package edu.uci.ics.perpetual.statement;

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
import edu.uci.ics.perpetual.statement.select.Select;
import edu.uci.ics.perpetual.statement.truncate.Truncate;
import edu.uci.ics.perpetual.statement.update.Update;
import edu.uci.ics.perpetual.statement.upsert.Upsert;
import edu.uci.ics.perpetual.statement.values.ValuesStatement;

public class StatementVisitorAdapter implements StatementVisitor {

    @Override
    public void visit(Comment comment) {

    }

    @Override
    public void visit(Commit commit) {

    }

    @Override
    public void visit(Select select) {

    }

    @Override
    public void visit(Delete delete) {

    }

    @Override
    public void visit(Update update) {

    }

    @Override
    public void visit(Insert insert) {

    }

    @Override
    public void visit(Replace replace) {

    }

    @Override
    public void visit(Drop drop) {

    }

    @Override
    public void visit(Truncate truncate) {

    }

    @Override
    public void visit(CreateIndex createIndex) {

    }

    @Override
    public void visit(CreateTable createTable) {

    }

    @Override
    public void visit(CreateView createView) {

    }

    @Override
    public void visit(Alter alter) {

    }

    @Override
    public void visit(Statements stmts) {
        for (Statement statement : stmts.getStatements()) {
            statement.accept(this);
        }
    }

    @Override
    public void visit(Execute execute) {

    }

    @Override
    public void visit(SetStatement set) {

    }

    @Override
    public void visit(Merge merge) {

    }

    @Override
    public void visit(AlterView alterView) {
    }

    @Override
    public void visit(Upsert upsert) {
    }

    @Override
    public void visit(UseStatement use) {
    }

    @Override
    public void visit(Block block) {
    }

    @Override
    public void visit(ValuesStatement values) {
    }
}
