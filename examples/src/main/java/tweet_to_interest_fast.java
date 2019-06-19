import com.google.gson.JsonObject;
import org.apache.log4j.Logger;


public class tweet_to_interest_fast {

    Logger LOGGER = Logger.getLogger(tweet_to_interest_fast.class);

    public String enrich(JsonObject data) {
        LOGGER.info("Running Enrichment");
        return "100";
    }

}