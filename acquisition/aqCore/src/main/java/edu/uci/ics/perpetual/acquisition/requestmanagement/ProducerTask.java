package edu.uci.ics.perpetual.acquisition.requestmanagement;

import edu.uci.ics.perpetual.request.AcquisitionRequest;
import edu.uci.ics.perpetual.acquisition.datatypes.Producer;
import edu.uci.ics.perpetual.request.AcquisitionRequestStatus;
import edu.uci.ics.perpetual.acquisition.utils.JavaUtils;
import edu.uci.ics.perpetual.ingestion.IngestionThread;
import org.apache.log4j.Logger;

import java.util.TimerTask;

public class ProducerTask extends TimerTask {

    Logger LOGGER = Logger.getLogger(ProducerTask.class);

    public  ProducerTask(AcquisitionRequest request){
        LOGGER.info("ACQUISITION ENGINE: Producer Task Initialized");
        this.request = request;
    }
    private AcquisitionRequest request;

    private Producer producer;

    @Override
    public void run() {
        request.setStatus( AcquisitionRequestStatus.INPROGRESS );
        LOGGER.info("ACQUISITION ENGINE: Running request - "+ request.getRequestId() + " for datasource: "+ request.getDataSourceId()  );
        try{
            startConsumerThread();
            produce();
        } catch(Exception e){
            // TODO LOG SCHEDULE FAILED
            LOGGER.info( "ACQUISITION ENGINE:  Producer Task failed for request:  " + request.getRequestId()  + " datasource: "+ request.getDataSourceId());
            e.printStackTrace();
        }
    }

    private void produce() throws Exception {
        producer = (Producer) JavaUtils.getObjectOfClass(request.getAcquisitionFunctionPath(),
                request.getAcquisitionName(), new Object[]{request});
        // TODO LOG
        LOGGER.info("ACQUISITION ENGINE: Calling fetch");
        producer.fetch();
    }

    private void startConsumerThread() {
        // TODO: Change this to user provided value
        request.getAcquisitionFunctionParameters().put("resolution", "10000");
        IngestionThread ingestionTask = new IngestionThread(request.getRequestId(),Long.parseLong(request.getAcquisitionFunctionParameters().get("resolution")));
        Thread th = new Thread(ingestionTask);
        th.start();
        LOGGER.info("ACQUISITION ENGINE: Ingestion thread spawned!");
    }

    @Override
    public boolean cancel() {
        LOGGER.info( "ACQUISITION ENGINE: Stopping the Producer Thread" );
        producer.close();
        return super.cancel();
    }
}
