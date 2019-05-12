package com.uci.perpetualdb.acquisition;

public class AcquisitionManager {

    AcquisitionManager instance;

    private AcquisitionManager(){
        super();
    }
    public AcquisitionManager getInstance(){
        if(null != instance){
            return instance;
        }
        instance = new AcquisitionManager();
        return instance;
    }

    /**
     * Invoked by Ingestion Engine
     */
    public Object getData(){
        // TODO implement function - get complete data structure updated by various consumers.
        return null;
    }

}
