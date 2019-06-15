package edu.uci.ics.perpetual.acquisition.requestmanagement;

import edu.uci.ics.perpetual.request.AcquisitionRequest;
import edu.uci.ics.perpetual.acquisition.utils.ScheduledStopTask;
import edu.uci.ics.perpetual.request.AcquisitionRequestStatus;
import java.util.HashMap;
import java.util.Timer;

public class RequestScheduler {

    public static void scheduleRequest(AcquisitionRequest request) throws Exception{
       // TODO LOG
        System.out.println("ACQUISITION ENGINE: Scheduling request: " + request.getRequestId()  + " datasource: "+ request.getDataSourceId());
        try {
            HashMap<String, String> params = request.getAcquisitionFunctionParameters();
            ProducerTask task = new ProducerTask(request);
            Timer timer = new Timer();
            Long startTime = request.getStartTime().getTime()  - System.currentTimeMillis();
            startTime = 10L;
            Long endTime = request.getEndTime().getTime()  - System.currentTimeMillis();
            timer.schedule( task, startTime);
            ScheduledStopTask stoppingTask = new ScheduledStopTask(task,request, request.getRequestId()+"-stop-task");
            timer.schedule(stoppingTask,endTime);
            System.out.println("ACQUISITION ENGINE: Scheduled request: " + request.getRequestId()  + " datasource: "+ request.getDataSourceId());
            request.setStatus(AcquisitionRequestStatus.SCHEDULED);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("ACQUISITION ENGINE: Request Schedule Failed: " + request.getRequestId()  + " datasource: "+ request.getDataSourceId());
            request.setStatus(AcquisitionRequestStatus.ERROR);
        }
        // TODO LOG
    }

}