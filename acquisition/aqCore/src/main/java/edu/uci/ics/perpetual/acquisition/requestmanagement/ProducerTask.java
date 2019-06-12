package edu.uci.ics.perpetual.acquisition.requestmanagement;

import edu.uci.ics.perpetual.acquisition.datatypes.AcquisitionRequest;
import edu.uci.ics.perpetual.acquisition.datatypes.Producer;
import edu.uci.ics.perpetual.acquisition.datatypes.RequestStatus;
import edu.uci.ics.perpetual.acquisition.utils.JavaUtils;
import edu.uci.ics.perpetual.ingestion.IngestionThread;

import java.util.TimerTask;

public class ProducerTask extends TimerTask {

    public  ProducerTask(AcquisitionRequest request){
        System.out.println("ACQUISITION ENGINE: Producer Task Initialized");
        this.request = request;
    }
    private AcquisitionRequest request;

    private Producer producer;

    @Override
    public void run() {
        request.setStatus( RequestStatus.INPROGRESS );
        System.out.println("ACQUISITION ENGINE: Running request - "+ request.getRequestId() + " for datasource: "+ request.getDataSourceId()  );
        try{
            startConsumerThread();
            produce();
        } catch(Exception e){
            // TODO LOG SCHEDULE FAILED
            System.out.println( "ACQUISITION ENGINE:  Producer Task failed for request:  " + request.getRequestId()  + " datasource: "+ request.getDataSourceId());
            e.printStackTrace();
        }
    }

    private void produce() throws Exception {
        producer = (Producer) JavaUtils.getObjectOfClass(request.getAcquisitionFunctionPath(), request.getAcquisitionName(), new Object[]{request});
        // TODO LOG
        System.out.println("ACQUISITION ENGINE: Calling fetch");
        producer.fetch();
    }

    private void startConsumerThread() {
        IngestionThread ingestionTask = new IngestionThread(request.getRequestId(),Long.parseLong(request.getAcquisitionFunctionParameters().get("resolution")));
        Thread th = new Thread(ingestionTask);
        th.start();
        System.out.println("ACQUISITION ENGINE: Ingestion thread spawned!");
    }

    @Override
    public boolean cancel() {
        System.out.println( "ACQUISITION ENGINE: Stopping the Producer Thread" );
        producer.close();
        return super.cancel();
    }
}
