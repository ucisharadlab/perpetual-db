package edu.uci.ics.perpetual.acquisition.requestmanagement;

import edu.uci.ics.perpetual.acquisition.datatypes.Request;

import java.util.ArrayList;
import java.util.List;

public class RequestManager {


    private List<Request> requests = new ArrayList<>();

    protected boolean addRequest(Request request){
        requests.add(request);

        return true;
    }

    protected boolean addRequests(List<Request> newRequests){
        requests.addAll(newRequests);
        return true;
    }
}
