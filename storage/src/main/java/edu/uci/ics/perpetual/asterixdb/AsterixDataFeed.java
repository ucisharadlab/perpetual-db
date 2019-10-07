package edu.uci.ics.perpetual.asterixdb;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class AsterixDataFeed {

    private String START_FEED_STMT = "start feed TwitterSocketFeed;";
    private String STOP_FEED_STMT = "stop feed TwitterSocketFeed;";
    private String collectionName;
    private AsterixDBConnectionManager connectionManager;
    private Socket client;
    private DataOutputStream outputStream;


    public AsterixDataFeed(String collectionName, AsterixDBConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.collectionName = collectionName;
        startFeed();
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        createSocketConnection();
    }

    public void createSocketConnection() {

        try {
            client = new Socket(AsterixDBConfig.ASTERIX_FEED_IP, AsterixDBConfig.ASTERIX_FEED_PORT);
            outputStream = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    public void startFeed() {
        connectionManager.sendQuery(START_FEED_STMT);
    }

    public void sendDataToFeed(String line) {
        try {
            line = line.replace("\\", "\\\\\\");
            //line = line.replace(":", " ");
            outputStream.writeBytes(line);
            outputStream.flush();
            System.out.println(line);
        } catch (IOException e) {
            System.out.println(line);
            e.printStackTrace();
        }
    }

    public void stopFeed() {
        try {
            outputStream.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectionManager.sendQuery(String.format(STOP_FEED_STMT, collectionName, collectionName, collectionName));

    }

}
