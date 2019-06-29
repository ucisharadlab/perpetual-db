package edu.uci.ics.perpetual.persistence;

import com.lambdaworks.redis.RedisConnection;
import edu.uci.ics.perpetual.rule.IRuleStore;
import edu.uci.ics.perpetual.rule.list.ListRule;
import edu.uci.ics.perpetual.rule.list.Rule;

import java.util.List;

public abstract class RulePersistence {

    public abstract String marshall(Rule rule);

    public abstract Rule unmarshall(String rule);

    public abstract void persist(IRuleStore ruleStore);

    public abstract IRuleStore load(int version);

    public abstract int maxVersion();


}
