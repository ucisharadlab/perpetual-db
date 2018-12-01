
package edu.uci.ics.perpetual.statement.select;

public interface SelectBody {

    void accept(SelectVisitor selectVisitor);
}
