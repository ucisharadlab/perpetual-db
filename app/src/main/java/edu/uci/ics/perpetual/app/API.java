package edu.uci.ics.perpetual.app;

import edu.uci.ics.perpetual.Schema;
import edu.uci.ics.perpetual.SchemaManager;
import edu.uci.ics.perpetual.acquisition.requestmanagement.AcquisitionRequestManager;
import edu.uci.ics.perpetual.rule.list.ListRule;
import edu.uci.ics.perpetual.rulegen.QueryBotRuleGen;
import edu.uci.ics.perpetual.util.PrettyPrintingMap;
import edu.uci.ics.perpetual.workload.extractor.QueryBotExtractInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import spark.Request;
import spark.Response;

import java.lang.reflect.Array;
import java.util.Arrays;

import static spark.Spark.*;

public class API {

    public static Server server;

    public static void main(String[] args) {

        server = new Server();
        server.initialize();

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
            response.put("dataSourceTypes", new PrettyPrintingMap<>(schema.getDataSourceTypeMap()).toString());
            response.put("dataSources", new PrettyPrintingMap<>(schema.getDataSourceMap()).toString());

            res.type("application/json");
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject response = new JSONObject();
            response.put("error", 1);
            return response;
        }
    }

    public static JSONObject requests(Request req, Response res) throws JSONException {

        try {

            AcquisitionRequestManager acqReqManager = server.acquisitionManager.getRequestManager();
            JSONObject response = new JSONObject();
            response.put("requests", acqReqManager.toString());

            res.type("application/json");
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject response = new JSONObject();
            response.put("error", 1);
            return response;
        }
    }

    public static JSONObject addRequests(Request req, Response res) throws JSONException {

        try {

            AcquisitionRequestManager acqReqManager = server.acquisitionManager.getRequestManager();
            JSONObject response = new JSONObject();
            response.put("requests", acqReqManager.toString());

            res.type("application/json");
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject response = new JSONObject();
            response.put("error", 1);
            return response;
        }
    }

    public static JSONObject policy(Request req, Response res) throws JSONException {

        try {

            ListRule rules = server.cachingManager.getRules();
            JSONObject response = new JSONObject();
            response.put("rules", rules.toString());

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
            response.put("workload", workload.toString());

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

}
