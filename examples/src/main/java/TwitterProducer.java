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
        System.out.println("temp file path ="+tempFilePath);
        br = new BufferedReader(new FileReader(tempFilePath));
        String line;
        int idx = 0;
        line = br.readLine();
        JsonParser parser = new JsonParser();

        while(line != null) {
        		try {
	        		System.out.println("line = "+line);
	            JsonObject tweet = (JsonObject) parser.parse(line);
	            System.out.println("tweet "+tweet);
	            JsonObject sendTweet = new JsonObject();
	            sendTweet.add("id", tweet.get("id"));
	            sendTweet.add("text", tweet.get("text"));
	            sendTweet.add("timestamp", tweet.get("timestamp"));
	            sendTweet.add("user", tweet.get("user"));
	
	            LOGGER.info("PRODUCER UDF : Sending..." + sendTweet.toString());
	            sendMessage(0, sendTweet.toString());
	            LOGGER.info("Sending data");
	
	            line = br.readLine();
        		}catch(Exception e) {
        			e.printStackTrace();
        		}
            Thread.sleep(1000);

        }

    }


}