package edu.uci.ics.perpetual.app;

import edu.uci.ics.perpetual.CachingManager;
import edu.uci.ics.perpetual.CachingManagerFactory;
import edu.uci.ics.perpetual.SchemaManager;

public class Server {

    private PerpetualCMDClient cmdClient;
    private SchemaManager schemaManager;
    private CachingManager cachingManager;


    public void start() {

        System.out.println("Starting PerpetualDB Server ...........................\n");

        System.out.println("Starting Schema Manager ...........................\n");
        schemaManager = SchemaManager.getInstance();

        System.out.println("Starting Caching Manager ...........................\n");
        cachingManager = CachingManagerFactory.getCachingManager();

        System.out.println("Starting Acquisition and Enrichment Engine ...........................\n");
//        cachingManager = CachingManagerFactory.getCachingManager();

        System.out.println("Server Initialization Complete ...........................\n\n\n");

        cmdClient = new PerpetualCMDClient(schemaManager, cachingManager);
        cmdClient.start();
    }

    public void stop() {

    }

    private void configure() {

    }

    public static void main(String args[]) {
        Server server = new Server();
        server.start();
    }

}
