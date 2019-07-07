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
            timer.schedule( task, startTime);
            ScheduledStopProducerTask stoppingTask = new ScheduledStopProducerTask(task,request, request.getRequestId()+"-stop-task");
            timer.schedule(stoppingTask,endTime);
            LOGGER.info("ACQUISITION ENGINE: Scheduled request: " + request.getRequestId()  + " datasource: "+ request.getDataSourceId());
            if(request.getStatus() != AcquisitionRequestStatus.NEW){
                request.setStatus(AcquisitionRequestStatus.RESCHEDULED);
            }else{
                request.setStatus(AcquisitionRequestStatus.SCHEDULED);
            }


        }catch(Exception e){
            e.printStackTrace();
            LOGGER.info("ACQUISITION ENGINE: Request Schedule Failed: " + request.getRequestId()  + " datasource: "+ request.getDataSourceId());
            request.setStatus(AcquisitionRequestStatus.ERROR);
        }
        db.updateRequestStatus( request );
        // TODO LOG
    }

}