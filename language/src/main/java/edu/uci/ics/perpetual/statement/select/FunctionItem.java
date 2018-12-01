
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.expression.Alias;
import edu.uci.ics.perpetual.expression.Function;

public class FunctionItem {

    private Function function;
    private Alias alias;

    public Alias getAlias() {
        return alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    @Override
    public String toString() {
        return function + ((alias != null) ? alias.toString() : "");
    }
}
