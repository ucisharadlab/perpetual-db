import com.google.gson.JsonObject;
import org.apache.log4j.Logger;


public class TwitterEnrichment {

    Logger LOGGER = Logger.getLogger(TwitterEnrichment.class);

    public String enrich(JsonObject data) {
        LOGGER.info("Running Enrichment");
        return "Success";
    }

}