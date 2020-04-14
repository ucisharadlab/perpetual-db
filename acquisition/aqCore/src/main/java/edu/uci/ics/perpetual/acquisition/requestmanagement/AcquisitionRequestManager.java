package edu.uci.ics.perpetual.acquisition.requestmanagement;

import com.google.gson.JsonObject;
import edu.uci.ics.perpetual.SchemaManager;
import edu.uci.ics.perpetual.request.AcquisitionRequest;
import edu.uci.ics.perpetual.request.AcquisitionRequestStatus;
import edu.uci.ics.perpetual.statement.Statement;
import edu.uci.ics.perpetual.statement.StatementVisitorAdapter;
import edu.uci.ics.perpetual.statement.add.AddRequest;
import edu.uci.ics.perpetual.types.DataObjectType;
import edu.uci.ics.perpetual.util.PrettyPrintingMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AcquisitionRequestManager {
    private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    private static RequestPersistanceManager db;
    private static AcquisitionRequestManager instance;
    private static SchemaManager schema;
    private static Logger LOGGER = Logger.getLogger(RequestPersistanceManager.class);

    private AcquisitionRequestManager(){
        super();
        db =  RequestPersistanceManager.getInstance();
        schema = SchemaManager.getInstance();
        loadRequests();
    }

    public static AcquisitionRequestManager getInstance(){
        if(null != instance){
            return instance;
        }
        instance = new AcquisitionRequestManager();

        return instance;
    }

    private void loadRequests(){
        try{
            handlePendingRequests(db.getPendingRequests());
        }catch(Exception e){
            System.out.println( "Failed to load pending requests from past session..." );
            LOGGER.error( "ACQUISITION ENGINE: Failed to reload pending requests from database" ,e);
        }
    }

    private Map<Integer , AcquisitionRequest> requests = new HashMap <>();

    public boolean processRequest(AcquisitionRequest request) throws Exception{
        requests.put(request.getRequestId(),request);
        RequestScheduler.scheduleRequest(request);
        return true;
    }

    public boolean addRequest(Statement stmt) throws Exception {

        AcquisitionRequest acquisitionRequest = new AcquisitionRequest();
        boolean send = false;

        stmt.accept(new StatementVisitorAdapter() {

            @Override
            public void visit(AddRequest addRequest) {

                acquisitionRequest.setDataSourceId(addRequest.getDataSourceId());
                acquisitionRequest.setRequestId(addRequest.getId());
                acquisitionRequest.setFrequency(addRequest.getFrequency());
                try {
                    String start = StringUtils.strip(addRequest.getStartTime(),"'");
                    String end = StringUtils.strip(addRequest.getEndTime(), "'");
                    acquisitionRequest.setStartTime(formatter.parse(start));
                    acquisitionRequest.setEndTime(formatter.parse(end));
                    acquisitionRequest.setStatus( AcquisitionRequestStatus.NEW );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                schema.accept(acquisitionRequest);
            }

        });

        if(validateRequest(acquisitionRequest) && db.insertRequest( acquisitionRequest))
            return processRequest(acquisitionRequest);

        return false;
    }

    public AcquisitionRequest getRequest(int requestId){
        return requests.get( requestId );
    }

    public DataObjectType getRequestDataSourceType(int requestId){
        return schema.getDataObjectTypeByDataSourceId(requests.get(requestId).getDataSourceId());
    }

    public AcquisitionRequestStatus getRequestStatus(int requestId){
        return requests.get( requestId ).getStatus();
    }

    private boolean validateRequest(AcquisitionRequest request) {
        //  TODO: Invoke PolicyManager once ready, if (PolicyManager.isAcquisitionAllowed(request)){};
        return true;
    }

    public boolean handlePendingRequests(List<AcquisitionRequest> requests) throws Exception{
        for(AcquisitionRequest request: requests){
            processRequest( request );
        }
        return true;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Acquisition Requests\n-------------------------------------------\n");
        sb.append(new PrettyPrintingMap(requests));
        sb.append("\n-------------------------------------------------\n\n");

        return sb.toString();

    }

    public String toJsonString() {
        JsonObject sb = new JsonObject();
        Iterator<Map.Entry<Integer, AcquisitionRequest>> iter = requests.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, AcquisitionRequest> entry = (Map.Entry) iter.next();
            sb.add(entry.getKey().toString(), entry.getValue().toJson());
        }
        return sb.toString();
    }
}
