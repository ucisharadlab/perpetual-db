package edu.uci.ics.perpetual.persistence;

import com.lambdaworks.redis.RedisConnection;
import edu.uci.ics.perpetual.rule.list.Rule;

import java.util.List;

public class RulePersistence implements Runnable {

    private RedisConnection<String, String> connection;

    public RulePersistence() {
        connection = RedisConnectionManager.getConnection();
    }

    public String marshall(Rule rule) {
        return null;
    }

    public Rule unmarshall(String rule) {
        return null;
    }

    public void persist(List<Rule> rules) {

    }

    public List<Rule> load() {
        return null;
    }

    @Override
    public void run() {

    }


}
