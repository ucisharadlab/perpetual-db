package edu.uci.ics.perpetual.acquisition.requestmanagement;

import edu.uci.ics.perpetual.request.AcquisitionRequest;
import edu.uci.ics.perpetual.request.AcquisitionRequestStatus;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Timer;

public class RequestScheduler {

    private static RequestPersistanceManager db = RequestPersistanceManager.getInstance();
    static Logger LOGGER = Logger.getLogger(RequestScheduler.class);

    public static void scheduleRequest(AcquisitionRequest request) throws Exception{
       // TODO LOG
        LOGGER.info("ACQUISITION ENGINE: Scheduling request: " + request.getRequestId()  + " datasource: "+ request.getDataSourceId());
        try {
            HashMap<String, String> params = request.getAcquisitionFunctionParameters();
            ProducerTask task = new ProducerTask(request);
            Timer timer = new Timer();
            Long startTime = request.getStartTime().getTime()  - System.currentTimeMillis();
            Long endTime = request.getEndTime().getTime()  - System.currentTimeMillis();
            if(startTime < 0){
                LOGGER.debug( "ACQUISITION ENGINE: Got a request with past start time. Scheduling it immediately." );
                startTime = System.currentTimeMillis() + 10;
            }

            timer.schedule( task, startTime);
            ScheduledStopProducerTask stoppingTask = new ScheduledStopProducerTask(task,request, request.getRequestId()+"-stop-task");
            timer.schedule(stoppingTask,endTime);
            if(request.getStatus() != AcquisitionRequestStatus.NEW){
                LOGGER.info("ACQUISITION ENGINE: ReScheduled request: " + request.getRequestId()  + " datasource: "+ request.getDataSourceId());
                request.setStatus(AcquisitionRequestStatus.RESCHEDULED);
            }else{
                LOGGER.info("ACQUISITION ENGINE: Scheduled request: " + request.getRequestId()  + " datasource: "+ request.getDataSourceId());
                request.setStatus(AcquisitionRequestStatus.SCHEDULED);
            }


        }catch(Exception e){
            LOGGER.error("ACQUISITION ENGINE: Request Schedule Failed: " + request.getRequestId()  + " datasource: "+ request.getDataSourceId(),e);
            request.setStatus(AcquisitionRequestStatus.ERROR);
        }
        db.updateRequestStatus( request );
        // TODO LOG
    }

}