package edu.uci.ics.perpetual.action;

import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;

import java.util.Iterator;
import java.util.List;

public class StaticAction implements IAction {

    private List<EnrichmentFunction> functions;

    private StaticAction(List<EnrichmentFunction> functions) {
        this.functions = functions;
    }

    @Override
    public Iterator<EnrichmentFunction> iterator() {
        return functions.iterator();
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        for(EnrichmentFunction func : functions) {
            sb.append(func);
            sb.append(" --> ");
        }
        return sb.toString();

    }

}
