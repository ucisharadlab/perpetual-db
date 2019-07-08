import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import java.util.Random;


public class TweetSentiment3 {

    Logger LOGGER = Logger.getLogger(TweetSentiment3.class);

    public String enrich(JsonObject data) {
        LOGGER.info("Running Enrichment");
        Random r = new Random();
		double rand = r.nextDouble();
		double resoultionProb = 0.4;
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