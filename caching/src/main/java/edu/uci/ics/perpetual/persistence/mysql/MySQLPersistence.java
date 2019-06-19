package edu.uci.ics.perpetual.persistence.mysql;

import edu.uci.ics.perpetual.persistence.RulePersistence;
import edu.uci.ics.perpetual.rule.IRuleStore;
import edu.uci.ics.perpetual.rule.list.ListRule;
import edu.uci.ics.perpetual.rule.list.Rule;
import edu.uci.ics.perpetual.storage.MysqlStorage;
import org.apache.commons.lang3.NotImplementedException;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQLPersistence extends RulePersistence {

    private MySQLService service;

    public MySQLPersistence() {
        service = new MySQLService(MysqlStorage.getInstance());
    }

    @Override
    public String marshall(Rule rule) {
        return rule.toString();
    }

    @Override
    public Rule unmarshall(String rule) {
        return null;
    }

    @Override
    public void persist(IRuleStore ruleStore) {

        int version = service.bumpVersion();
        if(ruleStore instanceof ListRule) {
            for (Rule rule : ((ListRule) ruleStore).getRules()) {
                service.insertRule(marshall(rule), version);
            }
        } else {
            throw new NotImplementedException("");
        }

    }

    @Override
    public IRuleStore load(int version) {
        List<String> rules = service.getCachingRules(version);
        ListRule ruleStore = new ListRule();

        for(String rule: rules) {
            ruleStore.addRule(unmarshall(rule));
        }
        return ruleStore;
    }

    @Override
    public int maxVersion() {
        return service.getMaxVersion();
    }

    public Map<Integer, IRuleStore> getAllRules() {

        Map<Integer, List<String>> rules = service.getAllRules();
        Map<Integer, IRuleStore> ruleMap = new HashMap<>();

        for (Map.Entry<Integer, List<String>> ruleList : rules.entrySet()) {
            int version = ruleList.getKey();
            ListRule ruleStore = new ListRule();

            for (String rule : ruleList.getValue()) {
                ruleStore.addRule(unmarshall(rule));
            }
            ruleMap.put(version, ruleStore);
        }
        return ruleMap;

    }
}
