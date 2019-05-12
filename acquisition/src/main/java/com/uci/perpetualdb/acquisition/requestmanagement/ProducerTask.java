package com.uci.perpetualdb.acquisition.requestmanagement;

import com.uci.perpetualdb.acquisition.utils.JavaUtils;
import com.uci.perpetualdb.acquisition.datatypes.Producer;
import com.uci.perpetualdb.acquisition.datatypes.Request;

import java.util.TimerTask;

public class ProducerTask extends TimerTask {

    public  ProducerTask(Request request){
        this.request = request;
    }
    private Request request;

    @Override
    public void run() {
         Producer producer = (Producer) JavaUtils.getObjectOfClass(request.getAcquisitionFunction().getPath(), request.getAcquisitionFunction().getName(), new Object[]{request});
         // TODO LOG
         producer.fetch();
    }


}
