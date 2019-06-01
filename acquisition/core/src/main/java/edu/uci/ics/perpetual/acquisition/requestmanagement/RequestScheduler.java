package edu.uci.ics.perpetual.acquisition.requestmanagement;

import edu.uci.ics.perpetual.acquisition.datatypes.DataSource;
import edu.uci.ics.perpetual.acquisition.utils.ScheduledStopTask;
import edu.uci.ics.perpetual.acquisition.datatypes.Request;
import edu.uci.ics.perpetual.acquisition.datatypes.RequestStatus;
import edu.uci.ics.perpetual.IngestionThread;

import java.util.Date;
import java.util.Timer;

public class RequestScheduler {

    public void scheduleRequest(Request request) throws Exception{
        // TODO if Policy.isAcquisitionAllowed(request)
        for(DataSource source : request.getDataSources()){
            ProducerTask task = new ProducerTask(request,source);
            ScheduledStopTask stoppingTask = new ScheduledStopTask(task);
            Timer timer = new Timer("Timer");
            // TODO LOG
            long currentTime = new Date().getTime();
            timer.schedule(task, request.getStartTime() - currentTime) ;
            timer.schedule(stoppingTask, request.getEndTime() - currentTime);
        }
        // Inform Ingestion Engine scheduled
        IngestionThread ingestion = new IngestionThread(request.getReqId(),request.getResolution());
        ingestion.run();
        request.setStatus(RequestStatus.SCHEDULED);
        // TODO LOG
    }

}