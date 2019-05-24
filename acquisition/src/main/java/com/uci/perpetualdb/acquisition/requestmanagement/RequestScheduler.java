package com.uci.perpetualdb.acquisition.requestmanagement;

import com.uci.perpetualdb.acquisition.datatypes.Request;
import com.uci.perpetualdb.acquisition.datatypes.RequestStatus;
import com.uci.perpetualdb.acquisition.utils.ScheduledStopTask;
import java.util.Date;
import java.util.Timer;

public class RequestScheduler {

    public void scheduleRequest(Request request) throws Exception{
        // TODO if Policy.isAcquisitionAllowed(request)
        ProducerTask task = new ProducerTask(request);
        ScheduledStopTask stoppingTask = new ScheduledStopTask(task);
        Timer timer = new Timer("Timer");
        // TODO LOG
        // TODO Inform Ingestion Engine scheduled
        long currentTime = new Date().getTime();
        timer.schedule(task, request.getStartTime() - currentTime) ;
        timer.schedule(stoppingTask, request.getEndTime() - currentTime);
        request.setStatus(RequestStatus.SCHEDULED);
        // TODO LOG
        // TODO Inform Ingestion Engine completion -- Is it needed?
    }

}