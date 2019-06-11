package edu.uci.ics.perpetual;

import edu.uci.ics.perpetual.action.IAction;
import edu.uci.ics.perpetual.action.NoAction;
import edu.uci.ics.perpetual.caching.RuleType;
import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;
import edu.uci.ics.perpetual.rule.IRuleStore;
import edu.uci.ics.perpetual.rule.list.ListRule;
import edu.uci.ics.perpetual.rule.list.Rule;
import edu.uci.ics.perpetual.rulegen.QueryBotRuleGen;
import edu.uci.ics.perpetual.workload.WorkloadManager;
import edu.uci.ics.perpetual.caching.WorkloadType;
import org.apache.commons.lang3.NotImplementedException;

public class CachingManager {

    private WorkloadManager workloadManager;
    private SchemaManager  schemaManager;
    private QueryBotRuleGen ruleGen;

    private IRuleStore ruleStore;
    private final int SLEEP_INTERVAl = 1000000;
    private final String DATADIR = "/home/peeyush/Downloads/perpetual-db/caching/src/main/resources/query-bot-5000.sample";

    private final WorkloadType WTYPE = WorkloadType.QueryBot;
    private final RuleType ruleType = RuleType.List;

    public CachingManager(){
        schemaManager = SchemaManager.getInstance();
        workloadManager = new WorkloadManager(DATADIR, WTYPE, SLEEP_INTERVAl);
        ruleGen = new QueryBotRuleGen(workloadManager, schemaManager.getSchema());
        init();

    }

    private void init() {
        Thread ruleGenThread = new Thread(ruleGen);
        ruleGenThread.start();
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

    public ListRule getRules() {
//        ruleGen.run();
        System.out.println(((QueryBotRuleGen)ruleGen).getExInfo());
        return ruleGen.generateRules();
    }

    public static void main(String args[]) {

        CachingManager cmanager = CachingManagerFactory.getCachingManager();
        ListRule rules = cmanager.ruleGen.getRuleStore();

        System.out.println(rules);
    }

}
