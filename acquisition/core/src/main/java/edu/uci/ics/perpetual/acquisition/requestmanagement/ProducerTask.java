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

    @Override
    public void run() {
        try{
            Producer producer = (Producer) JavaUtils.getObjectOfClass(request.getAcquisitionFunction().getPath(), request.getAcquisitionFunction().getName(), new Object[]{request});
            // TODO LOG
            producer.fetch();
        } catch(Exception e){
            // TODO LOG SCHEDULE FAILED
        }

    }


}
