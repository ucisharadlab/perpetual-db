package edu.uci.ics.perpetual.acquisition.utils;

import edu.uci.ics.perpetual.request.AcquisitionRequest;
import edu.uci.ics.perpetual.request.AcquisitionRequestStatus;

import java.util.TimerTask;

public class ScheduledStopTask extends TimerTask {

    String taskName;
    AcquisitionRequest request;
    TimerTask producerTask;

    public ScheduledStopTask(TimerTask task, AcquisitionRequest request, String name){
        System.out.println("ACQUISITION ENGINE: stopping task for producer of request " + request.getRequestId() +" scheduled.");
        this.producerTask = task; this.taskName = name;
        this.request = request;
    }

    @Override
    public void run() {
        this.request.setStatus( AcquisitionRequestStatus.DONE );
        System.out.println("Stopping the task!!!" );
        producerTask.cancel();
    }
}
