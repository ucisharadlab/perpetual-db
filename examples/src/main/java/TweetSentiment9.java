import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;
import java.util.Random;
import java.util.HashSet; 
import java.util.Set;
import java.util.ArrayList;
import java.util.List;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;


public class TweetSentiment9 {

    Logger LOGGER = Logger.getLogger(TweetSentiment9.class);
    public List<String> listPositive, listNegative;
    public Set<String> hashSetPositive, hashSetNegative;
	JsonParser parser = new JsonParser();
	int count = 0;

	public String enrich(JsonObject data) {
        LOGGER.info("Running Enrichment");
        Random r = new Random();
		double rand = r.nextDouble();
		double resoultionProb = 0.1;
		double thresholdProb = 0.5;
		String text = "";
		
		try {
			count += 1;

			if (count%20 != 0) return null;
            URL url = new URL("http://localhost:5000/sentiment/");
//			System.out.println("url:"+url);
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			
	
			JsonObject jsonObject = new JsonObject();
			
	        
	        	text = data.get("text").toString();
//	        	System.out.println("text ="+text);
	        
			jsonObject.addProperty("text",text);
			jsonObject.addProperty("classifier","RF");
			
		    String urlParameters = jsonObject.toString();
		    //System.out.println("urlParameters = "+urlParameters);

		    // Send post request
		    conn.setDoOutput(true);
		    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		    wr.writeBytes(urlParameters);
		    wr.flush();
		    wr.close();

		    int responseCode = conn.getResponseCode();
//		    System.out.println("\nSending 'POST' request to URL : " + url);
		    //System.out.println("Post parameters : " + urlParameters);
		    //System.out.println("Response Code : " + responseCode);

		    BufferedReader in = new BufferedReader(
		            new InputStreamReader(conn.getInputStream()));
		    String inputLine;
		    StringBuffer response = new StringBuffer();

		    while ((inputLine = in.readLine()) != null) {
		        response.append(inputLine);
		    }
		    in.close();
		    //print result
		    //System.out.println(response.toString());
		    String responseValue = response.toString();
		    
		    JsonObject responseObject = (JsonObject) parser.parse(responseValue);
		    double probVal = responseObject.get("probVal").getAsDouble();
//		    System.out.println("probVal = "+probVal);

			conn.disconnect();
		    if(probVal > 0.55) {
		    		return "Positive";
		    }else if(probVal < 0.35){
		    		return "Negative";
		    } else {
		    	return "Neutral";
			}

        }catch(Exception e) {
//        		e.printStackTrace();
        }
			
		return null;
    }

    
    /*
     * 
    
    public String enrich(JsonObject data) {
        LOGGER.info("Running Enrichment");
        System.out.println("data = "+data);
        double resoultionProb = 1.0;
        String text = "";
        try {
        		text = data.get("text").toString();
        		System.out.println("text ="+text);
        }catch(Exception e) {
        		e.printStackTrace();
        }
        
        		
        
        while(text.length()>0) {
        		text = text.trim();
        		int index = text.indexOf(" ");
        		if(index == -1)
        			break;
        		String word = text.substring(0,index);
        		
        		if(hashSetPositive.contains(word)) {
        			System.out.println("result = Positive");
        			return "Positive";
        		}
        		else if(hashSetNegative.contains(word)) {
        			System.out.println("result = Negative");
        			return "Negative";
        		}
        		text = text.substring(index+1);
        		
        				
        }
        return null;
        
    }
     */

}