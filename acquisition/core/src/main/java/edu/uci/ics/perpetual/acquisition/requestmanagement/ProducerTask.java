package edu.uci.ics.perpetual.acquisition.requestmanagement;

import edu.uci.ics.perpetual.acquisition.utils.JavaUtils;
import edu.uci.ics.perpetual.acquisition.datatypes.Producer;
import edu.uci.ics.perpetual.acquisition.datatypes.Request;

import java.util.TimerTask;

public class ProducerTask extends TimerTask {

    public  ProducerTask(Request request){
        this.request = request;
    }
    private Request request;

    private Producer producer;
    @Override
    public void run() {
        try{
            producer = (Producer) JavaUtils.getObjectOfClass(request.getAcquisitionFunction().getPath(), request.getAcquisitionFunction().getName(), new Object[]{request});
            // TODO LOG
            producer.fetch();

        } catch(Exception e){
            // TODO LOG SCHEDULE FAILED
        }

    }

    @Override
    public boolean cancel() {
        producer.close();
        return super.cancel();
    }
}
