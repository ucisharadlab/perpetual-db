package edu.uci.ics.perpetual.acquisition.requestmanagement;

import edu.uci.ics.perpetual.acquisition.datatypes.DataSource;
import edu.uci.ics.perpetual.acquisition.utils.JavaUtils;
import edu.uci.ics.perpetual.acquisition.datatypes.Producer;
import edu.uci.ics.perpetual.acquisition.datatypes.Request;

import java.util.TimerTask;

public class ProducerTask extends TimerTask {

    public  ProducerTask(Request request, DataSource source){
        this.request = request;
        this.source = source;
    }
    private Request request;
    private DataSource source;

    private Producer producer;
    @Override
    public void run() {
        System.out.println("Running request: "+ request.getReqId() + " for datasource: "+ source.getDsInstanceName()  );
        try{
            producer = (Producer) JavaUtils.getObjectOfClass(request.getAcquisitionFunction().getPath(), request.getAcquisitionFunction().getName(), new Object[]{request, source});
            // TODO LOG
            producer.fetch();

        } catch(Exception e){
            // TODO LOG SCHEDULE FAILED
            System.out.println( "Producer Task failed for request:  " + request.getReqId()  + " datasource: "+ source.getDsInstanceName());
            e.printStackTrace();
        }

    }

    @Override
    public boolean cancel() {
        producer.close();
        return super.cancel();
    }
}
