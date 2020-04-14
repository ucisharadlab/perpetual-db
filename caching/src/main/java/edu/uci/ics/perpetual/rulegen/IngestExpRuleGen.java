package edu.uci.ics.perpetual.rulegen;

import edu.uci.ics.perpetual.CachingConfig;
import edu.uci.ics.perpetual.Schema;
import edu.uci.ics.perpetual.action.StaticAction;
import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;
import edu.uci.ics.perpetual.predicate.ComparisionOperator;
import edu.uci.ics.perpetual.predicate.Expression;
import edu.uci.ics.perpetual.predicate.ExpressionPredicate;
import edu.uci.ics.perpetual.predicate.LogicalOperator;
import edu.uci.ics.perpetual.rule.IRuleStore;
import edu.uci.ics.perpetual.rule.list.ListRule;
import edu.uci.ics.perpetual.rule.list.Rule;
import edu.uci.ics.perpetual.statistics.IStats;
import edu.uci.ics.perpetual.types.DataObjectType;
import edu.uci.ics.perpetual.workload.WorkloadManager;
import edu.uci.ics.perpetual.workload.clusterer.IClusteredInfo;
import edu.uci.ics.perpetual.workload.extractor.IExtractInfo;

import java.util.ArrayList;
import java.util.List;

public class IngestExpRuleGen extends QueryBotRuleGen  {

    private ListRule ruleStore;

    public IngestExpRuleGen(WorkloadManager workloadManager, Schema schema) {
        super(workloadManager, schema);

    }

    public IngestExpRuleGen(IExtractInfo workloadInfo, IStats stats) {
        super(workloadInfo, stats);
    }

    public IngestExpRuleGen(IClusteredInfo workloadInfo) {
        super(workloadInfo);
    }

    @Override
    public ListRule generateRules() {
        ruleStore = new ListRule();
        try {

            String type = "Tweets";
            DataObjectType dataObjectType = new DataObjectType();
            dataObjectType.setName(type);

            List<Expression> expressions = new ArrayList<>();
//            expressions.add(new Expression<>("text", ComparisionOperator.GT, "A"));
            ExpressionPredicate predicate = new ExpressionPredicate(
                    LogicalOperator.AND,
                    expressions);
            List<EnrichmentFunction> functions = new ArrayList<>();
            functions.add(EnrichmentFunction.getEnrichmentFunction("/home/peeyush/Downloads/perpetual-db/examples/src/main/java/TweetSentiment9.jar"));
            StaticAction action = new StaticAction(functions);

            Rule rule = new Rule();
            rule.setType(dataObjectType);
            rule.setPredicate(predicate);
            rule.setAction(action);
            ruleStore.addRule(rule);

            return ruleStore;
        } catch (Exception e) {
            System.out.println("No Rules Generated\n\n");
            e.printStackTrace();
        }
        return ruleStore;
    }

    public ListRule getRuleStore() {
        return ruleStore;
    }

}
