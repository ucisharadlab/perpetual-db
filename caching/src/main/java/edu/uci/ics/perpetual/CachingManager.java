package edu.uci.ics.perpetual;

import edu.uci.ics.perpetual.action.IAction;
import edu.uci.ics.perpetual.action.NoAction;
import edu.uci.ics.perpetual.caching.RuleType;
import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;
import edu.uci.ics.perpetual.rule.IRuleStore;
import edu.uci.ics.perpetual.rule.list.ListRule;
import edu.uci.ics.perpetual.rule.list.Rule;
import edu.uci.ics.perpetual.workload.WorkloadManager;
import edu.uci.ics.perpetual.caching.WorkloadType;
import org.apache.commons.lang3.NotImplementedException;

public class CachingManager {

    private WorkloadManager workloadManager;
    private SchemaManager  schemaManager;
    private IRuleStore ruleStore;
    private final int SLEEP_INTERVAl = 1000000;
    private final String DATADIR = "/home/peeyush/Downloads/perpetual-db/caching/src/main/resources/query-bot-5000.sample";

    private final WorkloadType WTYPE = WorkloadType.QueryBot;
    private final RuleType ruleType = RuleType.List;

    public CachingManager(){
        schemaManager = SchemaManager.getInstance();
        workloadManager = new WorkloadManager(DATADIR, WTYPE, SLEEP_INTERVAl);
        init();

    }

    private void init() {
        workloadManager.run();
    }


    public IAction match(DataObject dataObject) {

        switch (ruleType) {
            case List:
                for (Rule rule: ((ListRule)ruleStore).getRules()) {
                    if (rule.match(dataObject))
                        return rule.getAction();
                }
                return new NoAction();
            default:
                throw new NotImplementedException("");
        }

    }

    public static void main(String args[]) {

        CachingManager cmanager = CachingManagerFactory.getCachingManager();

    }

}
