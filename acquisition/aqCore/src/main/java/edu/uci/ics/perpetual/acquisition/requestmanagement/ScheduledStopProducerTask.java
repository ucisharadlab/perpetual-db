package edu.uci.ics.perpetual.acquisition.requestmanagement;

import edu.uci.ics.perpetual.request.AcquisitionRequest;
import edu.uci.ics.perpetual.request.AcquisitionRequestStatus;
import org.apache.log4j.Logger;

import java.util.TimerTask;

public class ScheduledStopProducerTask extends TimerTask {

    String taskName;
    AcquisitionRequest request;
    TimerTask producerTask;

    Logger LOGGER = Logger.getLogger( ScheduledStopProducerTask.class);

    public ScheduledStopProducerTask(TimerTask task, AcquisitionRequest request, String name){
        LOGGER.info("ACQUISITION ENGINE: stopping task for producer of request " + request.getRequestId() +" scheduled.");
        this.producerTask = task; this.taskName = name;
        this.request = request;
    }

    @Override
    public void run() {
        this.request.setStatus( AcquisitionRequestStatus.DONE );
        LOGGER.info("ACQUISITION ENGINE: Stopping the task!!!" );
        RequestPersistanceManager.getInstance().updateRequestStatus( request );
        producerTask.cancel();
    }
}
