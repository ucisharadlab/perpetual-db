package edu.uci.ics.perpetual;

public class IngestionThread implements Runnable {
    private final int requestId;
    private long checkTimeInterval;

    public IngestionThread(int requestId, long checkTimeInterval){
        this.requestId = requestId;
        this.checkTimeInterval = checkTimeInterval;
    }

    public void run() {

    }

    private void tagObject(Object o){

    }

}
