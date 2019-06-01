package com.uci.perpetualdb.acquisition.utils;

import static com.uci.perpetualdb.acquisition.utils.JavaUtils.loadConfigs;

import java.util.Map;

public class AcquisitionConfig {

    public static Map<String,String> config;

    static {
        try {
            config = loadConfigs();
        }
        catch(Exception e){
            System.out.print("ERROR: Failed to load Configurations!!!");
            throw new RuntimeException("ERROR: Failed to load Configurations!!!");
        }
    }

}
