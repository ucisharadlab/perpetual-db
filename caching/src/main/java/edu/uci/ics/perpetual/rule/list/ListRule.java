package edu.uci.ics.perpetual.rule.list;

import edu.uci.ics.perpetual.rule.IRuleStore;

import java.util.ArrayList;
import java.util.List;

public class ListRule implements IRuleStore {

    private List<Rule> rules;

    public ListRule(List<Rule> rules) {
        this.rules = rules;
    }

    public ListRule() {
        rules = new ArrayList<>();
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }



}
