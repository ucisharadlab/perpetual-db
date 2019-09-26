package edu.uci.ics.perpetual.asterixdb;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class AsterixDBConnectionManager {

    private static final Logger LOGGER = Logger.getLogger(AsterixDBConnectionManager.class);
    private static AsterixDBConnectionManager _instance = new AsterixDBConnectionManager();
    private static String SERVER;
    private static String PORT;
    private static String DATAVERSE;

    private AsterixDBConnectionManager() {
        try {

            SERVER = AsterixDBConfig.SERVER;
            PORT = AsterixDBConfig.PORT;
            DATAVERSE = AsterixDBConfig.DATAVERSE;

            // Warming Up
            sendQuery(";");

        } catch (Exception ie) {
            LOGGER.error(ie);
        }
    }

    public static AsterixDBConnectionManager getInstance() {
        return _instance;
    }

    public HttpResponse sendQuery(String query) {
        return sendQuery(query, true);
    }

    public HttpResponse sendQuery(String query, boolean withDataverse) {
        CloseableHttpClient client = HttpClients.createDefault();
        String url = String.format("http://%s:%s/query/service", SERVER, PORT);
        HttpPost httpPost = new HttpPost(url);

        if (withDataverse)
            query = String.format("Use %s; ", DATAVERSE) + query;

        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("statement", query));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpPost);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}