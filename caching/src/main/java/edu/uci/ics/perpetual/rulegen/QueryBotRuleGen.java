package edu.uci.ics.perpetual.rulegen;

import edu.uci.ics.perpetual.statistics.IStats;
import edu.uci.ics.perpetual.workload.clusterer.IClusteredInfo;
import edu.uci.ics.perpetual.workload.extractor.IExtractInfo;
import edu.uci.ics.perpetual.workload.extractor.QueryBotExtractInfo;
import edu.uci.ics.perpetual.workload.extractor.QueryBotExtractor;

public class QueryBotRuleGen implements IRuleGen {

    private QueryBotExtractInfo workloadInfo;

    public QueryBotRuleGen(QueryBotExtractInfo workloadInfo) {
        this.workloadInfo = workloadInfo;
    }

    public QueryBotRuleGen(IExtractInfo workloadInfo, IStats stats) {

    }

    public QueryBotRuleGen(IClusteredInfo workloadInfo) {

    }

}
