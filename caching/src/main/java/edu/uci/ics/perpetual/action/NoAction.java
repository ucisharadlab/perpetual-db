package edu.uci.ics.perpetual.action;

import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;

import java.util.Collections;
import java.util.Iterator;

public class NoAction implements IAction {

    @Override
    public Iterator<EnrichmentFunction> iterator() {
        return Collections.emptyIterator();
    }

}
