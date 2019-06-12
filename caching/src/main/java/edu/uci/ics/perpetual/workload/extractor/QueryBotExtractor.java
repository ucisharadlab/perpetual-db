package edu.uci.ics.perpetual.workload.extractor;

import edu.uci.ics.perpetual.statement.Statement;
import edu.uci.ics.perpetual.statement.StatementVisitor;
import edu.uci.ics.perpetual.types.DataObjectType;
import edu.uci.ics.perpetual.workload.parser.IWorkloadParser;
import edu.uci.ics.perpetual.workload.parser.QueryBotWorkloadParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryBotExtractor implements IExtractor {

    public IWorkloadParser workloadParser;

    public QueryBotExtractor(IWorkloadParser workloadParser) {

        this.workloadParser = workloadParser;

    }

    @Override
    public QueryBotExtractInfo extractAll() {

        QueryBotExtractInfo qbInfo = new QueryBotExtractInfo();
        List<Statement> statements = workloadParser.parseFileInMemory();

        Map<String, Integer> doTypesCountMap = new HashMap<>();
        Map<String, Map<String, Integer>> tagsCountMap = new HashMap<>();

        for (Statement stmt : statements) {
            extractDOTypes(stmt, doTypesCountMap);
            extractTags(stmt, tagsCountMap);
        }

        qbInfo.setTypeInfo(doTypesCountMap);
        qbInfo.setTagInfo(tagsCountMap);
        return qbInfo;
    }

    public void extractDOTypes(Statement stmt, Map<String, Integer> doTypesCountMap) {

        TypesVisitor visitor = new TypesVisitor();
        List<String> types = visitor.getTableList(stmt);

        for (String type: types) {
            if (type.contains("."))
                type = type.split("\\.")[1];
            doTypesCountMap.put(type, doTypesCountMap.getOrDefault(type, 0) + 1);
        }
    }

    public void extractTags(Statement stmt, Map<String, Map<String, Integer>> tagsCountMap ) {
        TagsVisitor visitor = new TagsVisitor();
        List<String> attributes = visitor.getAttributeList(stmt);

        for (String attribute: attributes) {
            String[] data = attribute.split("\\.");
            String type = data[0];
            String tag = data[1];

            Map<String, Integer> innerMap = tagsCountMap.getOrDefault(type, new HashMap<>());
            innerMap.put(tag, innerMap.getOrDefault(tag, 0)+1);
            tagsCountMap.put(type, innerMap);
        }

    }

    public static void main(String args[]) {
        QueryBotWorkloadParser queryBotWorkloadParser = new QueryBotWorkloadParser(
                "/home/peeyush/Downloads/perpetual-db/caching/src/main/resources/query-bot-5000.sample");
        QueryBotExtractor extractor = new QueryBotExtractor(queryBotWorkloadParser);
        extractor.extractAll();
    }


}
