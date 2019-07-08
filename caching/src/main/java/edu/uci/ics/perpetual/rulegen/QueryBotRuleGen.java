package edu.uci.ics.perpetual.rulegen;

import edu.uci.ics.perpetual.CachingConfig;
import edu.uci.ics.perpetual.Schema;
import edu.uci.ics.perpetual.action.StaticAction;
import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;
import edu.uci.ics.perpetual.persistence.RulePersistence;
import edu.uci.ics.perpetual.persistence.mysql.MySQLPersistence;
import edu.uci.ics.perpetual.predicate.ComparisionOperator;
import edu.uci.ics.perpetual.predicate.Expression;
import edu.uci.ics.perpetual.predicate.ExpressionPredicate;
import edu.uci.ics.perpetual.predicate.LogicalOperator;
import edu.uci.ics.perpetual.rule.IRuleStore;
import edu.uci.ics.perpetual.rule.list.ListRule;
import edu.uci.ics.perpetual.rule.list.Rule;
import edu.uci.ics.perpetual.statistics.IStats;
import edu.uci.ics.perpetual.types.DataObjectType;
import edu.uci.ics.perpetual.types.TaggingFunction;
import edu.uci.ics.perpetual.workload.WorkloadManager;
import edu.uci.ics.perpetual.workload.clusterer.IClusteredInfo;
import edu.uci.ics.perpetual.workload.extractor.IExtractInfo;
import edu.uci.ics.perpetual.workload.extractor.QueryBotExtractInfo;
import edu.uci.ics.perpetual.workload.extractor.QueryBotExtractor;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;
import java.util.stream.Collectors;

public class QueryBotRuleGen implements IRuleGen, Runnable  {

    private Schema schema;
    private QueryBotExtractInfo exInfo;
    protected RulePersistence persistence;

    private ListRule ruleStore;

    public QueryBotRuleGen(WorkloadManager workloadManager, Schema schema) {
        workloadManager.run();
        this.exInfo = (QueryBotExtractInfo) workloadManager.getExtractInfo();
        System.out.print(exInfo);
        this.schema = schema;
        this.persistence = new MySQLPersistence();
    }

    public QueryBotRuleGen(IExtractInfo workloadInfo, IStats stats) {
    }

    public QueryBotRuleGen(IClusteredInfo workloadInfo) {
    }

    @Override
    public ListRule generateRules() {

        ruleStore = new ListRule();
        try {
            List<String> topRawTypes = getTopRawTypes(CachingConfig.TOP_TYPES);
            if (topRawTypes.size() > CachingConfig.TOP_TYPES)
                topRawTypes = topRawTypes.subList(0, CachingConfig.TOP_TYPES);

            for (String type : topRawTypes) {
                List<String> topTags = getTopTagsForRawType(type);
                if (topTags.size() > CachingConfig.TOP_TAGS) topTags = topTags.subList(0, CachingConfig.TOP_TAGS);

                DataObjectType dataObjectType = new DataObjectType();
                dataObjectType.setName(type);

                List<Expression> expressions = new ArrayList<>();
                expressions.add(new Expression<>(CachingConfig.TYPE_STR, ComparisionOperator.EQ, type));
                ExpressionPredicate predicate = new ExpressionPredicate(
                        LogicalOperator.AND,
                        expressions);

                for (String tag : topTags) {

                    List<EnrichmentFunction> functions = new ArrayList<>();
                    EnrichmentFunction enrichmentFunction = getEnrichementFunctioByTag(tag, type);
                    if (enrichmentFunction!= null) {
                        functions.add(enrichmentFunction);
                    } else {
                        functions.add(EnrichmentFunction.getEnrichmentFunction(CachingConfig.DUMMY_ENRICH_FUNC));
                    }
                    StaticAction action = new StaticAction(functions);

                    Rule rule = new Rule();
                    rule.setType(dataObjectType);
                    rule.setPredicate(predicate);
                    rule.setAction(action);
                    ruleStore.addRule(rule);
                }

            }

            if (CachingConfig.PERSIST) {
                persistence.persist(ruleStore);
            }

            return ruleStore;
        } catch (Exception e) {
            System.out.println("No Rules Generated\n\n");
            e.printStackTrace();
        }
        return ruleStore;
    }

    EnrichmentFunction getEnrichementFunctioByTag(String tag, String rawType) {

        for (Map.Entry<String, TaggingFunction> entry : schema.getEnrichmentFunctions().entrySet()) {
            if(entry.getValue().getReturnTag().equalsIgnoreCase(tag) &&
                    entry.getValue().getSourceType().equalsIgnoreCase(rawType))
                return EnrichmentFunction.getEnrichmentFunction(entry.getValue().getPath(), tag);

        }

        return null;

    }

    List<String> getTopRawTypes(int N) {

        try {
            List<Map.Entry<String, Integer>> types = new ArrayList<>(exInfo.getTypeInfo().entrySet());
            types.sort((a, b) -> b.getValue() - a.getValue());

            return types.stream()
                    .filter(a -> schema.getRawMap().keySet().contains(a.getKey().toUpperCase()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

    List<String> getTopTagsForRawType(String rawType) {

        try {
            List<String> tags = schema.getTagMap().entrySet().stream()
                    .filter(a -> a.getValue().getRawType().equalsIgnoreCase(rawType))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            Set<Map.Entry<String, Integer>> extractedTags = new HashSet<>();
            exInfo.getTagInfo().forEach(
                    (key, value) -> value.entrySet().forEach(a -> {
                        if (tags.contains(a.getKey().toUpperCase())) extractedTags.add(a);
                    })

            );

            List<Map.Entry<String, Integer>> extractedTagsList = new ArrayList<>(extractedTags);
            extractedTagsList.sort((a, b) -> b.getValue() - a.getValue());

            return  extractedTagsList.stream()
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
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

    public void setRuleStore(ListRule ruleStore) {
        this.ruleStore = ruleStore;
    }

    public QueryBotExtractInfo getExInfo() {
        return exInfo;
    }

    public void setExInfo(QueryBotExtractInfo exInfo) {
        this.exInfo = exInfo;
    }
}
