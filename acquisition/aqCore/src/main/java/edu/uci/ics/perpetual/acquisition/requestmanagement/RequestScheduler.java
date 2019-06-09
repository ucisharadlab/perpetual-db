package edu.uci.ics.perpetual.acquisition.requestmanagement;

import edu.uci.ics.perpetual.acquisition.datatypes.AcquisitionRequest;
import edu.uci.ics.perpetual.acquisition.utils.ScheduledStopTask;
import edu.uci.ics.perpetual.acquisition.datatypes.RequestStatus;
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
            Long startTime = Long.parseLong( params.get( "startTime" ) )  - System.currentTimeMillis();
            Long endTime = Long.parseLong( params.get( "endTime" ) )  - System.currentTimeMillis();
            timer.schedule( task, startTime);
            ScheduledStopTask stoppingTask = new ScheduledStopTask(task,request, request.getRequestId()+"-stop-task");
            timer.schedule(stoppingTask,endTime);
            System.out.println("ACQUISITION ENGINE: Scheduled request: " + request.getRequestId()  + " datasource: "+ request.getDataSourceId());
            request.setStatus(RequestStatus.SCHEDULED);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("ACQUISITION ENGINE: Request Schedule Failed: " + request.getRequestId()  + " datasource: "+ request.getDataSourceId());
            request.setStatus(RequestStatus.ERROR);
        }
        // TODO LOG
    }

}