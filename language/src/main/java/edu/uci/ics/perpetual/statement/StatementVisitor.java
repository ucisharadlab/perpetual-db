
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

public interface StatementVisitor {

    void visit(Insert insert);

    void visit(Drop drop);

    void visit(CreateMetadataType createMetadataType);

    void visit(CreateRawType createRawType);

    void visit(Statements stmts);

    void visit(SetStatement set);

    void visit(Select select);

    void visit(Block block);

    void visit(ValuesStatement values);

    void visit(AddTag addTag);

    void visit(AddAcquisitionFunction addAcquisitionFunction);

    void visit(AddDataSource addDataSource);

    void visit(AddRequest addRequest);

    void visit(CreateFunction createFunction);

    void visit(CreateDataSourceType createDataSourceType);
}
