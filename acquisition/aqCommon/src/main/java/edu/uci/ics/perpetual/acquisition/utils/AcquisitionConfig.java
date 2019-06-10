package edu.uci.ics.perpetual.acquisition.utils;

import java.util.Map;

import static edu.uci.ics.perpetual.acquisition.utils.JavaUtils.loadConfigs;

public class AcquisitionConfig {

    public static Map<String,String> config;

    static {
        try {
            config = loadConfigs();
        }
        catch(Exception e){
            System.out.print("ERROR: Failed to load Configurations!!!");
            e.printStackTrace();
            throw new RuntimeException("ERROR: Failed to load Configurations!!!");
        }
    }

}
