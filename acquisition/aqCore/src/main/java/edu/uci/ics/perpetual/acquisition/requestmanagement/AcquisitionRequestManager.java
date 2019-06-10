package edu.uci.ics.perpetual.acquisition.requestmanagement;

import edu.uci.ics.perpetual.SchemaManager;
import edu.uci.ics.perpetual.acquisition.datatypes.AcquisitionRequest;
import edu.uci.ics.perpetual.acquisition.datatypes.RequestStatus;
import java.util.HashMap;
import java.util.Map;

public class AcquisitionRequestManager {

    private AcquisitionRequestManager(){
        super();
    }

    private static AcquisitionRequestManager instance;
    private static SchemaManager schema;
    public static AcquisitionRequestManager getInstance(){
        if(null != instance){
            return instance;
        }
        instance = new AcquisitionRequestManager();
        schema = SchemaManager.getInstance();
        return instance;
    }

    private Map<Integer , AcquisitionRequest> requests = new HashMap <>();

    public boolean addRequest(int requestId) throws Exception{
      /* TODO Uncomment once schema manager code is available.
        AcquisitionRequest request = schema.getRequest(requestId);
        return addRequest(request);*/
        return true;
    }

    public boolean addRequest(AcquisitionRequest request) throws Exception{
        if(validateRequest(request)){
            request.setStatus( RequestStatus.NEW );
            requests.put(request.getRequestId(),request);
            RequestScheduler.scheduleRequest(request);
            return true;
        }
        return false;
    }

    public RequestStatus getRequestStatus(int requestId){
        return requests.get( requestId ).getStatus();
    }

    private boolean validateRequest(AcquisitionRequest request) {
        //  if (PolicyManager.isAcquisitionAllowed(request)){};
        return true;
    }
    /* FOR FUTURE SUPPORT TO ADD MULTIPLE REQUESTS AT ONCE
    public boolean addRequests(List<Request> newRequests) throws Exception{
        for(Request request: newRequests){
            addRequest( request );
        }
        return true;
    }*/
}
