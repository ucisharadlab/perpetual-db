package edu.uci.ics.perpetual.acquisition.requestmanagement;

import edu.uci.ics.perpetual.SchemaManager;
import edu.uci.ics.perpetual.request.AcquisitionRequest;
import edu.uci.ics.perpetual.request.AcquisitionRequestStatus;
import edu.uci.ics.perpetual.statement.*;
import edu.uci.ics.perpetual.statement.add.AddAcquisitionFunction;
import edu.uci.ics.perpetual.statement.add.AddDataSource;
import edu.uci.ics.perpetual.statement.add.AddRequest;
import edu.uci.ics.perpetual.statement.add.AddTag;
import edu.uci.ics.perpetual.statement.create.type.CreateDataSourceType;
import edu.uci.ics.perpetual.statement.create.type.CreateFunction;
import edu.uci.ics.perpetual.statement.create.type.CreateMetadataType;
import edu.uci.ics.perpetual.statement.create.type.CreateRawType;
import edu.uci.ics.perpetual.statement.drop.Drop;
import edu.uci.ics.perpetual.statement.insert.Insert;
import edu.uci.ics.perpetual.statement.select.Select;
import edu.uci.ics.perpetual.statement.values.ValuesStatement;
import edu.uci.ics.perpetual.types.DataObjectType;
import edu.uci.ics.perpetual.types.DataSourceType;
import edu.uci.ics.perpetual.util.PrettyPrintingMap;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class AcquisitionRequestManager {

    private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");


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
            request.setStatus( AcquisitionRequestStatus.NEW );
            requests.put(request.getRequestId(),request);
            RequestScheduler.scheduleRequest(request);
            return true;
        }
        return false;
    }

    public boolean addRequest(Statement stmt) throws Exception {

        AcquisitionRequest acquisitionRequest = new AcquisitionRequest();
        boolean send = false;

        stmt.accept(new StatementVisitorAdapter() {

            @Override
            public void visit(AddRequest addRequest) {

                acquisitionRequest.setDataSourceId(addRequest.getDataSourceId());
                acquisitionRequest.setRequestId(addRequest.getId());
                try {
                    String start = StringUtils.strip(addRequest.getStartTime(),"'");
                    String end = StringUtils.strip(addRequest.getEndTime(), "'");
                    acquisitionRequest.setStartTime(formatter.parse(start));
                    acquisitionRequest.setEndTime(formatter.parse(end));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                schema.accept(acquisitionRequest);
            }

        });

        return addRequest(acquisitionRequest);

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

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Acquisition Requests\n-------------------------------------------\n");
        sb.append(new PrettyPrintingMap(requests));
        sb.append("\n-------------------------------------------------\n\n");

        return sb.toString();

    }
}
