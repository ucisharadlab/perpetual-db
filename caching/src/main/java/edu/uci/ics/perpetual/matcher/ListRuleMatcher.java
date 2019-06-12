package edu.uci.ics.perpetual.matcher;

import edu.uci.ics.perpetual.action.IAction;
import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.rule.list.Rule;

import java.util.List;

public class ListRuleMatcher implements IMatcher {

    private List<Rule> rules;

    public ListRuleMatcher(List<Rule> rules) {
        this.rules = rules;
    }

    public IAction getAction(DataObject dataObject) {

        for (Rule rule: rules) {
            if (rule.match(dataObject))
                return rule.getAction();
        }

        // No rule Matched
        return null;

    }


}
