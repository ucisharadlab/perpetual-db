import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import java.util.Random;


public class TweetSentiment0 {

    Logger LOGGER = Logger.getLogger(TweetSentiment0.class);

    public String enrich(JsonObject data) {
        LOGGER.info("Running Enrichment");
        Random r = new Random();
		double rand = r.nextDouble();
		double resoultionProb = 0.1;
		if(rand < resoultionProb)
		{
			if(rand < resoultionProb / 2)
				return "Positive";
			else
				return "Negative";
		}
		else
			return null;
    }

}