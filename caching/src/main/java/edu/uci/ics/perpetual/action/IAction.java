package edu.uci.ics.perpetual.action;

import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;

import java.util.Iterator;

public interface IAction {

    public Iterator<EnrichmentFunction> iterator();

}
