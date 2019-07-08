package edu.uci.ics.perpetual.acquisition.requestmanagement;
import com.google.gson.reflect.TypeToken;
import edu.uci.ics.perpetual.request.AcquisitionRequest;
import edu.uci.ics.perpetual.request.AcquisitionRequestStatus;
import edu.uci.ics.perpetual.storage.MysqlStorage;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.util.SystemPropertiesPropertySource;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import static edu.uci.ics.perpetual.acquisition.utils.AcquisitionConfig.config;

public class RequestPersistanceManager {

    private MysqlStorage storage;
    private static RequestPersistanceManager instance;
    private static Logger LOGGER = Logger.getLogger(RequestPersistanceManager.class);

    private RequestPersistanceManager(){
        storage = MysqlStorage.getInstance();
    }

    public static RequestPersistanceManager getInstance(){
        if(instance == null){
            instance = new RequestPersistanceManager();
        }
        return instance;
    }

    public boolean insertRequest(AcquisitionRequest request){
        try{
            PreparedStatement ps = storage.getConn().prepareStatement( config.get( "request.add" ) );
            ps.setInt( 1, request.getRequestId() );
            ps.setString( 2, request.getAcquisitionName() );
            ps.setInt( 3, request.getDataSourceId() );
            ps.setString( 4, request.getAcquisitionFunctionPath() );
            Gson gson = new Gson();
            ps.setString( 5, gson.toJson(request.getAcquisitionFunctionParameters()) );
            ps.setString( 6, gson.toJson( request.getRawTypeScheme() ));
            ps.setTimestamp( 7, new Timestamp( System.currentTimeMillis()));
            ps.setTimestamp( 8, new Timestamp( System.currentTimeMillis()));
            ps.setTimestamp( 9, new Timestamp( request.getStartTime().getTime()));
            ps.setTimestamp( 10, new Timestamp( request.getEndTime().getTime()));
            ps.setInt(11, request.getFrequency());
            ps.setString( 12, request.getStatus().name() );

            System.out.println( ps );
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            LOGGER.error( "ACQUISITION ENGINE: Failed to add request: " + request.getRequestId(), e);
            return false;
        }
        return true;
    }

    public void updateRequestStatus(AcquisitionRequest request){
        try{
            PreparedStatement ps = storage.getConn().prepareStatement( config.get( "request.status.update" ) );
            ps.setString( 1 , request.getStatus().name() );
            ps.setTimestamp( 2 , new Timestamp( System.currentTimeMillis()));
            ps.setInt( 3, request.getRequestId() );
            ps.executeUpdate();
        }catch (SQLException e){
            LOGGER.error( "ACQUISITION ENGINE: Failed to update status of request: " + request.getRequestId(), e);
        }
    }

    public List<AcquisitionRequest> getAllRequests(){
        List<AcquisitionRequest> requests = null;
        try{
            PreparedStatement ps = storage.getConn().prepareStatement( config.get( "requests.fetch.all" ) );
            ResultSet results = ps.executeQuery();
            System.out.println( ps );
            requests = toRequests(  results );
            System.out.println( "Found " + requests.size() + "pending requests....");

        }catch (SQLException e){
            LOGGER.error( "ACQUISITION ENGINE: Failed to load all requests.", e);
        }
        return requests;
    }

    public List<AcquisitionRequest> getPendingRequests(){
        List<AcquisitionRequest> requests = null;
        try{
            PreparedStatement ps = storage.getConn().prepareStatement( config.get( "requests.fetch.pending" ) );
            ps.setTimestamp( 1, new Timestamp( System.currentTimeMillis() ) );
            ResultSet results = ps.executeQuery();
            System.out.println( ps );

            requests = toRequests(  results );

        }catch (SQLException e){
            e.printStackTrace();
            LOGGER.error( "ACQUISITION ENGINE: Failed to load old requests.", e);
        }
        return requests;
    }

    private List<AcquisitionRequest> toRequests( ResultSet results) throws SQLException {
        List<AcquisitionRequest> requests = new ArrayList<>();
        while(results.next()){
            AcquisitionRequest request = new AcquisitionRequest(  );
            request.setRequestId(results.getInt( 1 ));
            request.setAcquisitionName(results.getString( 2));
            request.setDataSourceId(results.getInt( 3) );
            request.setAcquisitionFunctionPath(results.getString( 4 ) );
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            request.setAcquisitionFunctionParameters(gson.fromJson( results.getString( 5 ), type ));
            request.setRawTypeScheme(gson.fromJson( results.getString( 6 ), type ));
            request.setStartTime(new java.util.Date( results.getTimestamp( 9 ).getTime() ));
            request.setEndTime(new java.util.Date( results.getTimestamp( 10 ).getTime()) );
            request.setFrequency( results.getInt( 11 ) );
            request.setStatus( AcquisitionRequestStatus.valueOf( results.getString( 12 ) ) );
            requests.add( request );
        }
        return requests;
    }
}