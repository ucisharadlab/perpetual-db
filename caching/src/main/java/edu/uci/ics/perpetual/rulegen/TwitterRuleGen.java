package edu.uci.ics.perpetual.rulegen;

import edu.uci.ics.perpetual.CachingConfig;
import edu.uci.ics.perpetual.Schema;
import edu.uci.ics.perpetual.action.StaticAction;
import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;
import edu.uci.ics.perpetual.predicate.ComparisionOperator;
import edu.uci.ics.perpetual.predicate.Expression;
import edu.uci.ics.perpetual.predicate.ExpressionPredicate;
import edu.uci.ics.perpetual.predicate.LogicalOperator;
import edu.uci.ics.perpetual.rule.list.ListRule;
import edu.uci.ics.perpetual.rule.list.Rule;
import edu.uci.ics.perpetual.statistics.IStats;
import edu.uci.ics.perpetual.types.DataObjectType;
import edu.uci.ics.perpetual.workload.WorkloadManager;
import edu.uci.ics.perpetual.workload.clusterer.IClusteredInfo;
import edu.uci.ics.perpetual.workload.extractor.IExtractInfo;

import java.util.ArrayList;
import java.util.List;

public class TwitterRuleGen extends QueryBotRuleGen {

    private ListRule ruleStore;


    public TwitterRuleGen(WorkloadManager workloadManager, Schema schema) {
        super(workloadManager, schema);
    }

    public TwitterRuleGen(IExtractInfo workloadInfo, IStats stats) {
        super(workloadInfo, stats);
    }

    public TwitterRuleGen(IClusteredInfo workloadInfo) {
        super(workloadInfo);
    }

    @Override
    public ListRule generateRules() {

        this.ruleStore = new ListRule();
        try {
            List<String> topRawTypes = getTopRawTypes(CachingConfig.TOP_TYPES);
            if (topRawTypes.size() > CachingConfig.TOP_TYPES)
                topRawTypes = topRawTypes.subList(0, CachingConfig.TOP_TYPES);

            for (String type : topRawTypes) {
                List<String> topTags = getTopTagsForRawType(type);
                if (topTags.size() > CachingConfig.TOP_TAGS) topTags = topTags.subList(0, CachingConfig.TOP_TAGS);

                DataObjectType dataObjectType = new DataObjectType();
                dataObjectType.setName(type);

                Expression<String> lowString = new Expression<>("text", ComparisionOperator.GT, "T");
                Expression<String> highString = new Expression<>("text", ComparisionOperator.GT, "I");

                for (String tag : topTags) {

                    List<EnrichmentFunction> functions = new ArrayList<>();
                    EnrichmentFunction enrichmentFunction = getEnrichementFunctioByTag(tag, type);
                    if (enrichmentFunction!= null) {
                        functions.add(enrichmentFunction);
                    } else {
                        functions.add(EnrichmentFunction.getEnrichmentFunction(CachingConfig.DUMMY_ENRICH_FUNC));
                    }
                    StaticAction action = new StaticAction(functions);

                    List<Expression> expressions = new ArrayList<>();

                    if (tag.equalsIgnoreCase("interest")) {
                        expressions.add(lowString);
                    } else {
                        expressions.add(highString);
                    }
                    ExpressionPredicate predicate = new ExpressionPredicate(
                            LogicalOperator.AND,
                            expressions);

                    Rule rule = new Rule();

                    rule.setType(dataObjectType);
                    rule.setPredicate(predicate);
                    rule.setAction(action);
                    ruleStore.addRule(rule);
                }

            }
            return ruleStore;
        } catch (Exception e) {
            System.out.println("No Rules Generated\n\n");
            e.printStackTrace();
        }
        return ruleStore;
    }

    @Override
    public void run() {
        while (true) {
            generateRules();
            try {
                Thread.sleep(CachingConfig.SLEEP_INTERVAl);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ListRule getRuleStore() {
        return ruleStore;
    }

}
