package edu.uci.ics.perpetual.acquisition.requestmanagement;

import edu.uci.ics.perpetual.acquisition.datatypes.Request;

import java.util.ArrayList;
import java.util.List;

public class AcquisitionRequestManager {

    private AcquisitionRequestManager(){
        super();
    }

    private static AcquisitionRequestManager instance;

    public static AcquisitionRequestManager getInstance(){
        if(null != instance){
            return instance;
        }
        instance = new AcquisitionRequestManager();
        return instance;
    }

    private List<Request> requests = new ArrayList<>();

    public boolean addRequest(Request request) throws Exception{
        requests.add(request);
        RequestScheduler.scheduleRequest( request );
        return true;
    }

    public boolean addRequests(List<Request> newRequests) throws Exception{
        for(Request request: newRequests){
            addRequest( request );
        }
        return true;
    }
}
