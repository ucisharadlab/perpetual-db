package acquisition.requestmanagement;

import acquisition.utils.ScheduledStopTask;
import datatypes.Request;
import datatypes.RequestStatus;
import edu.uci.ics.perpetual.IngestionThread;

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
        IngestionThread ingestion = new IngestionThread(request.getReqId(),request.getResolution());
        long currentTime = new Date().getTime();
        timer.schedule(task, request.getStartTime() - currentTime) ;
        ingestion.run();
        timer.schedule(stoppingTask, request.getEndTime() - currentTime);
        request.setStatus(RequestStatus.SCHEDULED);
        // TODO LOG
    }

}