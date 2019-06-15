package edu.uci.ics.perpetual;

import edu.uci.ics.perpetual.caching.RuleType;
import edu.uci.ics.perpetual.caching.WorkloadType;

public class CachingConfig {

    public static final int SLEEP_INTERVAl = 1000000;

    public static final String DATADIR = "/home/peeyush/Downloads/perpetual-db/caching/src/main/resources/query-bot-5000.sample";

    public static final WorkloadType WTYPE = WorkloadType.QueryBot;

    public static final RuleType ruleType = RuleType.List;

    public static final String TYPE_STR = "type";

    public static final String DUMMY_ENRICH_FUNC = "file:////home/peeyush/Downloads/perpetual-db/examples/TwitterEnrichment.jar";

    public static final int TOP_TYPES = 2;

    public static final int TOP_TAGS = 2;

}
