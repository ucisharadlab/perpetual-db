package edu.uci.ics.perpetual.app;

import edu.uci.ics.perpetual.CachingManager;
import edu.uci.ics.perpetual.CachingManagerFactory;
import edu.uci.ics.perpetual.SchemaManager;
import edu.uci.ics.perpetual.acquisition.AcquisitionManager;

public class Server {

    public PerpetualCMDClient cmdClient;
    public SchemaManager schemaManager;
    public CachingManager cachingManager;
    public AcquisitionManager acquisitionManager;


    public void start() {

        initialize();

        cmdClient = new PerpetualCMDClient(schemaManager, cachingManager, acquisitionManager);
        cmdClient.start();
    }

    public void initialize() {
        System.out.println("Starting PerpetualDB Server ...........................\n");

        System.out.println("Starting Schema Manager ...........................\n");
        schemaManager = SchemaManager.getInstance();

        System.out.println("Starting Caching Manager ...........................\n");
        cachingManager = CachingManagerFactory.getCachingManager();

        System.out.println("Starting Acquisition and Enrichment Engine ...........................\n");
        acquisitionManager = AcquisitionManager.getInstance();

        System.out.println("Server Initialization Complete ...........................\n\n");
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
