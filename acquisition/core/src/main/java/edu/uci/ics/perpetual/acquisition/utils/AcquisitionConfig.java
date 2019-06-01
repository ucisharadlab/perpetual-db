package acquisition.utils;

import java.util.Map;

import static acquisition.utils.JavaUtils.loadConfigs;

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
