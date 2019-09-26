import com.google.gson.JsonObject;
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

import org.json.JSONArray;
import org.json.JSONObject;



public class TweetSentimentSVM_LIN {

    Logger LOGGER = Logger.getLogger(TweetSentimentSVM_LIN.class);

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
			jsonObject.put("classifier","SVM_LIN");
			
		    String urlParameters = jsonObject.toString();
		    //System.out.println("urlParameters = "+urlParameters);

		    // Send post request
		    conn.setDoOutput(true);
		    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		    wr.writeBytes(urlParameters);
		    wr.flush();
		    wr.close();

		    int responseCode = conn.getResponseCode();
		    System.out.println("\nSending 'POST' request to URL : " + url);
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
		    
		    JSONObject responseObject = new JSONObject(responseValue);
		    double probVal = responseObject.getDouble("probVal");
		    System.out.println("probVal = "+probVal);
		    
		    if(probVal > 0.55) {
		    		return "Positive";
		    }else if(probVal < 0.35){
		    		return "Negative";
		    }
		    
		    conn.disconnect();
        }catch(Exception e) {
        		e.printStackTrace();
        }
			
		return null;
    }

}