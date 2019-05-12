package com.uci.perpetualdb.acquisition.requestmanagement;

import com.uci.perpetualdb.acquisition.datatypes.Request;
import com.uci.perpetualdb.acquisition.datatypes.RequestStatus;
import java.util.Date;
import java.util.Timer;

public class RequestScheduler {

    public void scheduleRequest(Request request) throws Exception{
        ProducerTask task = new ProducerTask(request);
        Timer timer = new Timer("Timer");
        // TODO LOG
        // TODO Inform Ingestion Engine scheduled
        timer.schedule(task, request.getStartTime() - new Date().getTime()) ;
        // TODO - think how to time the task instead of sleep
        Thread.sleep(request.getEndTime() - new Date().getTime());
        request.setStatus(RequestStatus.SUCCESS);
        // TODO LOG
        // TODO Inform Ingestion Engine completion -- Is it needed?
        timer.cancel();
    }

}