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


import edu.uci.ics.perpetual.statement.add.AddAcquisitionFunction;
import edu.uci.ics.perpetual.statement.add.AddDataSource;
import edu.uci.ics.perpetual.statement.add.AddRequest;
import edu.uci.ics.perpetual.statement.add.AddTag;
import edu.uci.ics.perpetual.statement.create.type.CreateDataSourceType;
import edu.uci.ics.perpetual.statement.create.type.CreateFunction;
import edu.uci.ics.perpetual.statement.create.type.CreateMetadataType;
import edu.uci.ics.perpetual.statement.create.type.CreateRawType;
import edu.uci.ics.perpetual.statement.drop.Drop;
import edu.uci.ics.perpetual.statement.insert.Insert;
import edu.uci.ics.perpetual.statement.select.Select;
import edu.uci.ics.perpetual.statement.values.ValuesStatement;

public class StatementVisitorAdapter implements StatementVisitor {

    @Override
    public void visit(Select select) {

    }

    @Override
    public void visit(Insert insert) {

    }

    @Override
    public void visit(Drop drop) {

    }

    @Override
    public void visit(CreateMetadataType createMetadataType) {

    }

    @Override
    public void visit(CreateRawType createRawType) {

    }

    @Override
    public void visit(Statements stmts) {
        for (Statement statement : stmts.getStatements()) {
            statement.accept(this);
        }
    }

    @Override
    public void visit(SetStatement set) {

    }

    @Override
    public void visit(Block block) {
    }

    @Override
    public void visit(ValuesStatement values) {
    }

    @Override
    public void visit(AddTag addTag) {

    }

    @Override
    public void visit(AddAcquisitionFunction addAcquisitionFunction) {

    }

    @Override
    public void visit(AddDataSource addDataSource) {

    }

    @Override
    public void visit(AddRequest addRequest) {

    }

    @Override
    public void visit(CreateFunction createFunction) {

    }

    @Override
    public void visit(CreateDataSourceType createDataSourceType) {

    }
}
