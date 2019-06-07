package edu.uci.ics.perpetual.rulegen;

import edu.uci.ics.perpetual.rule.list.Rule;
import edu.uci.ics.perpetual.types.DataObjectType;
import edu.uci.ics.perpetual.workload.extractor.QueryBotExtractInfo;

import java.util.ArrayList;
import java.util.List;

public class ExampleRuleGen implements IRuleGen {

    private QueryBotExtractInfo workloadInfo;

    public ExampleRuleGen(QueryBotExtractInfo workloadInfo) {

        this.workloadInfo = workloadInfo;

    }


    public List<Rule> getAllRules() {

        DataObjectType wifiData = new DataObjectType();

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();

        rule.setType(wifiData);

        return null;

    }

}
