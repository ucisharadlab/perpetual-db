package edu.uci.ics.perpetual.app;

public class Server {

    private PerpetualCMDClient cmdClient;

    public void start() {

        cmdClient = new PerpetualCMDClient();
        cmdClient.start();

    }

    public void stop() {

    }

    private void configure() {

    }

}
