
package edu.uci.ics.perpetual.util.deparser;

import java.util.Iterator;
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
import edu.uci.ics.perpetual.statement.select.Select;
import edu.uci.ics.perpetual.statement.select.WithItem;
import edu.uci.ics.perpetual.statement.values.ValuesStatement;

public class StatementDeParser implements StatementVisitor {

    private ExpressionDeParser expressionDeParser;

    private SelectDeParser selectDeParser;

    protected StringBuilder buffer;

    public StatementDeParser(StringBuilder buffer) {
        this(new ExpressionDeParser(), new SelectDeParser(), buffer);
    }

    public StatementDeParser(ExpressionDeParser expressionDeParser, SelectDeParser selectDeParser, StringBuilder buffer) {
        this.expressionDeParser = expressionDeParser;
        this.selectDeParser = selectDeParser;
        this.buffer = buffer;
    }

    @Override
    public void visit(CreateMetadataType createMetadataType) {
        CreateTableDeParser createTableDeParser = new CreateTableDeParser(this, buffer);
        createTableDeParser.deParse(createMetadataType);
    }

    @Override
    public void visit(CreateTable createTable) {
//        CreateTableDeParser createTableDeParser = new CreateTableDeParser(this, buffer);
//        createTableDeParser.deParse(createTable);
    }


    @Override
    public void visit(CreateRawType createRawType) {

    }

    @Override
    public void visit(Drop drop) {
        DropDeParser dropDeParser = new DropDeParser(buffer);
        dropDeParser.deParse(drop);
    }

    @Override
    public void visit(Insert insert) {
        selectDeParser.setBuffer(buffer);
        expressionDeParser.setSelectVisitor(selectDeParser);
        expressionDeParser.setBuffer(buffer);
        selectDeParser.setExpressionVisitor(expressionDeParser);
        InsertDeParser insertDeParser = new InsertDeParser(expressionDeParser, selectDeParser, buffer);
        insertDeParser.deParse(insert);
    }

    @Override
    public void visit(Select select) {
        selectDeParser.setBuffer(buffer);
        expressionDeParser.setSelectVisitor(selectDeParser);
        expressionDeParser.setBuffer(buffer);
        selectDeParser.setExpressionVisitor(expressionDeParser);
        if (select.getWithItemsList() != null && !select.getWithItemsList().isEmpty()) {
            buffer.append("WITH ");
            for (Iterator<WithItem> iter = select.getWithItemsList().iterator(); iter.hasNext();) {
                WithItem withItem = iter.next();
                withItem.accept(selectDeParser);
                if (iter.hasNext()) {
                    buffer.append(",");
                }
                buffer.append(" ");
            }
        }
        select.getSelectBody().accept(selectDeParser);
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

    @Override
    public void visit(Statements stmts) {
        stmts.accept(this);
    }

    @Override
    public void visit(SetStatement set) {
        selectDeParser.setBuffer(buffer);
        expressionDeParser.setSelectVisitor(selectDeParser);
        expressionDeParser.setBuffer(buffer);
        SetStatementDeParser setStatementDeparser = new SetStatementDeParser(expressionDeParser, buffer);
        selectDeParser.setExpressionVisitor(expressionDeParser);
        setStatementDeparser.deParse(set);
    }


    @Override
    public void visit(Block block) {
        buffer.append("BEGIN\n");
        if (block.getStatements() != null) {
            for (Statement stmt : block.getStatements().getStatements()) {
                stmt.accept(this);
                buffer.append(";\n");
            }
        }
        buffer.append("END");
    }

    @Override
    public void visit(ValuesStatement values) {
        expressionDeParser.setBuffer(buffer);
        new ValuesStatementDeParser(expressionDeParser, buffer).deParse(values);
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
