package edu.uci.ics.perpetual.rule.list;

import edu.uci.ics.perpetual.action.IAction;
import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.predicate.IPredicate;
import edu.uci.ics.perpetual.types.DataObjectType;

public class Rule {

    private DataObjectType type;

    private IPredicate predicate;

    private IAction action;

    private int priority;

    public Rule(DataObjectType type, IPredicate predicate, IAction action, int priority) {
        this.type = type;
        this.predicate = predicate;
        this.action = action;
        this.priority = priority;
    }

    public Rule(DataObjectType type, IPredicate predicate, IAction action) {
        this.type = type;
        this.predicate = predicate;
        this.action = action;
    }

    public Rule(DataObjectType type, IAction action) {
        this.type = type;
        this.action = action;
    }

    public Rule() {

    }

    public DataObjectType getType() {
        return type;
    }

    public void setType(DataObjectType type) {
        this.type = type;
    }

    public IPredicate getPredicate() {
        return predicate;
    }

    public void setPredicate(IPredicate predicate) {
        this.predicate = predicate;
    }

    public IAction getAction() {
        return action;
    }

    public void setAction(IAction action) {
        this.action = action;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean match(DataObject dataObject) {

        return true;

    }


    public String toString() {

        return String.format("List Rule: Type = %s, Predicate = %s, Action = %s, Priority = %s",
                type, predicate, action, priority);

    }

}
