package edu.uci.ics.perpetual.rulegen;

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
import edu.uci.ics.perpetual.workload.extractor.QueryBotExtractInfo;
import edu.uci.ics.perpetual.workload.extractor.QueryBotExtractor;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryBotRuleGen implements IRuleGen, Runnable  {

    private Schema schema;
    private QueryBotExtractInfo exInfo;
    private final int TOP_TYPES = 2;
    private final int TOP_TAGS = 2;
    private ListRule ruleStore;
    private final String TYPE_STR = "type";

    public QueryBotRuleGen(WorkloadManager workloadManager, Schema schema) {
        workloadManager.run();
        this.exInfo = (QueryBotExtractInfo) workloadManager.getExtractInfo();
        System.out.print(exInfo);
        this.schema = schema;
        ruleStore = new ListRule();
    }

    public QueryBotRuleGen(IExtractInfo workloadInfo, IStats stats) {
    }

    public QueryBotRuleGen(IClusteredInfo workloadInfo) {
    }

    @Override
    public ListRule generateRules() {

        List<String> topRawTypes = getTopRawTypes(TOP_TYPES);

        for (String type: topRawTypes) {

            List<String> topTags = getTopTagsForRawType(type, TOP_TAGS);

            DataObjectType dataObjectType = new DataObjectType();
            dataObjectType.setName(type);

            List<Expression> expressions = new ArrayList<>();
            expressions.add(new Expression<>(TYPE_STR, ComparisionOperator.EQ, type));
            ExpressionPredicate predicate = new ExpressionPredicate(
                    LogicalOperator.AND,
                    expressions);

            for (String tag:topTags) {

                List<EnrichmentFunction> functions = new ArrayList<>();
                functions.add(new EnrichmentFunction());

                StaticAction action = new StaticAction(functions);

                Rule rule = new Rule();
                rule.setType(dataObjectType);
                rule.setPredicate(predicate);
                rule.setAction(action);
                ruleStore.addRule(rule);
            }

        }
        return ruleStore;

    }

    public List<String> getTopRawTypes(int N) {

        List<Map.Entry<String, Integer>> types = new ArrayList<>(exInfo.getTypeInfo().entrySet());
        types.sort((a,b) -> b.getValue() - a.getValue());

        return types.stream()
                .filter(a-> schema.getRawMap().keySet().contains(a.getKey()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList())
                .subList(0, N);

    }

    public List<String> getTopTagsForRawType(String rawType, int N) {

        List<String> tags = schema.getTagMap().entrySet().stream()
                .filter(a->a.getValue().getType().equalsIgnoreCase(rawType))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<Map.Entry<String, Integer>> extractedTags = new ArrayList<>();
        exInfo.getTagInfo().forEach(
                (key, value) -> value.entrySet().stream()
                        .filter(b -> tags.contains(b.getKey()))
                        .map(extractedTags::add)
        );

        extractedTags.sort((a,b) -> b.getValue() - a.getValue());

        return extractedTags.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList())
                .subList(0, N);


    }

    @Override
    public void run() {
        while (true) {

            generateRules();


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
