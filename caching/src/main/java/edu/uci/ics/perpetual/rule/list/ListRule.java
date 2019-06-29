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

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Caching Rules\n-------------------------------------------\n");
        for (Rule rule: rules) {
            sb.append(rule).append("\n");
        }
        sb.append("-------------------------------------------\n");
        return sb.toString();

    }


}
