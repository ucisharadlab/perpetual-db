
package edu.uci.ics.perpetual.statement.select;

import edu.uci.ics.perpetual.expression.Alias;
import edu.uci.ics.perpetual.expression.UDFFunction;

public class FunctionItem {

    private UDFFunction UDFFunction;
    private Alias alias;

    public Alias getAlias() {
        return alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    public UDFFunction getUDFFunction() {
        return UDFFunction;
    }

    public void setUDFFunction(UDFFunction UDFFunction) {
        this.UDFFunction = UDFFunction;
    }

    @Override
    public String toString() {
        return UDFFunction + ((alias != null) ? alias.toString() : "");
    }
}
