package edu.uci.ics.perpetual.rulegen;

import edu.uci.ics.perpetual.Schema;
import edu.uci.ics.perpetual.rule.IRuleStore;
import edu.uci.ics.perpetual.rule.list.ListRule;
import edu.uci.ics.perpetual.workload.WorkloadManager;
import edu.uci.ics.perpetual.workload.extractor.IExtractInfo;

public class ListRuleGen implements IRuleGen {

    private IExtractInfo extractInfo;
    private Schema schema;

    @Override
    public ListRule generateRules() {
        return null;
    }

}
