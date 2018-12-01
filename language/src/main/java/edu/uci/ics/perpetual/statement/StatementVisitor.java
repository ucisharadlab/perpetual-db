
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

public interface StatementVisitor {

    void visit(Comment comment);

    void visit(Commit commit);

    void visit(Delete delete);

    void visit(Update update);

    void visit(Insert insert);

    void visit(Replace replace);

    void visit(Drop drop);

    void visit(Truncate truncate);

    void visit(CreateIndex createIndex);

    void visit(CreateTable createTable);

    void visit(CreateView createView);

    void visit(AlterView alterView);

    void visit(Alter alter);

    void visit(Statements stmts);

    void visit(Execute execute);

    void visit(SetStatement set);

    void visit(Merge merge);

    void visit(Select select);

    void visit(Upsert upsert);

    void visit(UseStatement use);

    void visit(Block block);

    void visit(ValuesStatement values);
}
