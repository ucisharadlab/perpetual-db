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
		double thresholdProb = 0.5;
		String text = "";
		
		try {
            URL url = new URL("http://localhost:5000/sentiment/");
			System.out.println("url:"+url);
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			
			JSONObject jsonObject = new JSONObject();
			
	        
	        	text = data.get("text").toString();
	        	System.out.println("text ="+text);
	        
			jsonObject.put("text",text);
			jsonObject.put("classifier","DT");
			
		    String urlParameters = jsonObject.toString();
		    

		    // Send post request
		    conn.setDoOutput(true);
		    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		    wr.writeBytes(urlParameters);
		    wr.flush();
		    wr.close();

		    int responseCode = conn.getResponseCode();
		    System.out.println("\nSending 'POST' request to URL : " + url);
		    System.out.println("Post parameters : " + urlParameters);
		    System.out.println("Response Code : " + responseCode);

		    BufferedReader in = new BufferedReader(
		            new InputStreamReader(conn.getInputStream()));
		    String inputLine;
		    StringBuffer response = new StringBuffer();

		    while ((inputLine = in.readLine()) != null) {
		        response.append(inputLine);
		    }
		    in.close();
		  //print result
		    System.out.println(response.toString());
		    String responseValue = response.toString();
		     System.out.println("response from server"+responseValue);
		    conn.disconnect();
        }catch(Exception e) {
        		e.printStackTrace();
        }
		
		
		
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