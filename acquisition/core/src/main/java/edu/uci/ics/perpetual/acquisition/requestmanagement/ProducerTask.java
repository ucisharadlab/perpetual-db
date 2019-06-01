package acquisition.requestmanagement;

import acquisition.utils.JavaUtils;
import datatypes.Producer;
import datatypes.Request;

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
