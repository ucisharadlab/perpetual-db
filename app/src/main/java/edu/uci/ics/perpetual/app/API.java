package edu.uci.ics.perpetual.app;

import com.google.gson.JsonObject;
import edu.uci.ics.perpetual.JSQLParserException;
import edu.uci.ics.perpetual.Schema;
import edu.uci.ics.perpetual.SchemaManager;
import edu.uci.ics.perpetual.acquisition.requestmanagement.AcquisitionRequestManager;
import edu.uci.ics.perpetual.parser.CCJSqlParserManager;
import edu.uci.ics.perpetual.parser.ParseException;
import edu.uci.ics.perpetual.request.AcquisitionRequest;
import edu.uci.ics.perpetual.request.SqlRequest;
import edu.uci.ics.perpetual.rule.list.ListRule;
import edu.uci.ics.perpetual.rulegen.QueryBotRuleGen;
import edu.uci.ics.perpetual.types.DataSource;
import edu.uci.ics.perpetual.types.DataSourceType;
import edu.uci.ics.perpetual.util.PrettyPrintingMap;
import edu.uci.ics.perpetual.workload.extractor.QueryBotExtractInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import spark.Request;
import spark.Response;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static spark.Spark.*;

public class API {

    public static Server server;
    private static CCJSqlParserManager parser;


    public static void main(String[] args) {

        server = new Server();
        server.initialize();

        parser = new CCJSqlParserManager();


        // Configure Spark
        port(5001);
        threadPool(8);
        // ipAddress("0.0.0.0");

        // Set up routes
        get("/sources",  API::sources);
        get("/requests",  API::requests);
        get("/policy",  API::policy);
        get("/workload",  API::workload);
        get("/ingestion",  API::sources);

        post("/requests",  API::addRequest);
        post("/sources",  API::addSource);

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

    }

    public static JSONObject sources(Request req, Response res) throws JSONException {

        try {

            Schema schema = server.schemaManager.getSchema();
            JSONObject response = new JSONObject();
            response.put("dataSourceTypes", sourceTypessMapToJsonString(schema.getDataSourceTypeMap()));
            response.put("dataSources", sourcesMapToJsonString(schema.getDataSourceMap()));

            res.type("application/json");
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject response = new JSONObject();
            response.put("error", 1);
            return response;
        }
    }

    public static JSONObject addSource(Request req, Response res) throws Exception {

        JSONObject reqJson = new JSONObject(req.body());
        String query = reqJson.getString("query");

        AcquisitionRequestManager acqReqManager = server.acquisitionManager.getRequestManager();
        acqReqManager.addRequest(parser.parse(query));
        server.schemaManager.accept(new SqlRequest(parser.parse(query)));

        JSONObject response = new JSONObject();
        response.put("success", 1);

        res.type("application/json");
        return response;

    }

    public static JSONObject requests(Request req, Response res) throws JSONException {

        try {

            AcquisitionRequestManager acqReqManager = server.acquisitionManager.getRequestManager();
            JSONObject response = new JSONObject();
            response.put("requests", acqReqManager.toJsonString());

            res.type("application/json");
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject response = new JSONObject();
            response.put("error", 1);
            return response;
        }
    }

    public static JSONObject addRequest(Request req, Response res) throws Exception {

        JSONObject reqJson = new JSONObject(req.body());
        String query = reqJson.getString("query");

        AcquisitionRequestManager acqReqManager = server.acquisitionManager.getRequestManager();
        acqReqManager.addRequest(parser.parse(query));

        JSONObject response = new JSONObject();
        response.put("success", 1);

        res.type("application/json");
        return response;

    }

    public static JSONObject policy(Request req, Response res) throws JSONException {

        try {

            ListRule rules = server.cachingManager.getRules();
            JSONObject response = new JSONObject();
            response.put("rules", rules.toJson().toString());

            res.type("application/json");
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject response = new JSONObject();
            response.put("error", 1);
            return response;
        }
    }

    public static JSONObject workload(Request req, Response res) throws JSONException {

        try {

            QueryBotExtractInfo workload = server.cachingManager.getRuleGen().getExInfo();
            JSONObject response = new JSONObject();
            response.put("workload", workload.toHTML());

            res.type("application/json");
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject response = new JSONObject();
            response.put("error", 1);
            return response;
        }
    }

    public static JSONObject ingestion(Request req, Response res) throws JSONException {

        try {
            System.out.println("Request Received " + req.body());
            JSONObject json = new JSONObject(req.body());

            System.out.println("Result returned");
            JSONObject response = new JSONObject();

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject response = new JSONObject();
            response.put("label", 0);
            return response;
        }
    }


    // Helper functions to serializer data to be sent to the client

    public static String sourceTypessMapToJsonString(Map<String, DataSourceType> sourceTypes) {

        JsonObject sb = new JsonObject();
        Iterator<Map.Entry<String, DataSourceType>> iter = sourceTypes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, DataSourceType> entry = (Map.Entry) iter.next();
            sb.add(entry.getKey().toString(), entry.getValue().toJson());
        }
        return sb.toString();

    }

    public static String sourcesMapToJsonString(Map<Integer, DataSource> sources) {

        JsonObject sb = new JsonObject();
        Iterator<Map.Entry<Integer, DataSource>> iter = sources.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, DataSource> entry = (Map.Entry) iter.next();
            sb.add(entry.getKey().toString(), entry.getValue().toJson());
        }
        return sb.toString();

    }



}
