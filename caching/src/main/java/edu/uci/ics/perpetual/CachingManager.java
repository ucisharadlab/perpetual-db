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


    public CachingManager(){
        schemaManager = SchemaManager.getInstance();
        workloadManager = new WorkloadManager(CachingConfig.DATADIR, CachingConfig.WTYPE, CachingConfig.SLEEP_INTERVAl);
        ruleGen = new QueryBotRuleGen(workloadManager, schemaManager.getSchema());
        init();

    }

    private void init() {
        Thread ruleGenThread = new Thread(ruleGen);
        ruleGenThread.start();
    }


    public IAction match(DataObject dataObject) {

        switch (CachingConfig.ruleType) {
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

        System.out.println(((QueryBotRuleGen)ruleGen).getExInfo());
        ruleStore = ruleGen.generateRules();
        return (ListRule) ruleStore;

    }

    public static void main(String args[]) {

        CachingManager cmanager = CachingManagerFactory.getCachingManager();
        ListRule rules = cmanager.ruleGen.getRuleStore();

        System.out.println(rules);
    }

}
