import com.google.gson.JsonObject;
import org.apache.log4j.Logger;


public class tweet_to_interest {

    Logger LOGGER = Logger.getLogger(tweet_to_interest.class);

    public String enrich(JsonObject data) {
        LOGGER.info("Running Enrichment");
        return "40";
    }

}