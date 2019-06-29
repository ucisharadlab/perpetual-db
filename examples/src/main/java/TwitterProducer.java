import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.uci.ics.perpetual.acquisition.datatypes.Producer;
import edu.uci.ics.perpetual.request.AcquisitionRequest;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.Random;

public class TwitterProducer extends Producer {

    Logger LOGGER = Logger.getLogger(TwitterProducer.class);

    public TwitterProducer(AcquisitionRequest request) {
        super(request);
    }

    public void fetch() throws Exception {

        Random r = new Random();
        LOGGER.info("Fetching Tweets");
        BufferedReader br = null;

        Map<String, String> params = request.getAcquisitionFunctionParameters();
        String tempFilePath = params.get("filePath");
        br = new BufferedReader(new FileReader(tempFilePath));
        String line;
        int idx = 0;
        line = br.readLine();
        JsonParser parser = new JsonParser();

        while(line != null) {
            JsonObject tweet = (JsonObject) parser.parse(line);
            JsonObject sendTweet = new JsonObject();
            sendTweet.addProperty("id", tweet.getAsJsonObject("_id").get("$numberLong").getAsLong());
            sendTweet.addProperty("text", tweet.getAsJsonObject("status").get("text").getAsString());
            sendTweet.addProperty("timestamp", tweet.getAsJsonObject("timestamp").get("$numberLong").getAsLong());
            sendTweet.addProperty("user", tweet.getAsJsonObject("status").getAsJsonObject("user").get("name").getAsString());

            LOGGER.info("PRODUCER UDF : Sending..." + sendTweet.toString());
            sendMessage(0, sendTweet.toString());
            LOGGER.info("Sending data");

            line = br.readLine();
            Thread.sleep(1000);

        }

    }


}