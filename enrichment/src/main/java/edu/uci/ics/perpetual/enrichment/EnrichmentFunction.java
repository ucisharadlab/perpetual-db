package edu.uci.ics.perpetual.enrichment;

import com.google.gson.JsonObject;
import edu.uci.ics.perpetual.data.DataObject;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class EnrichmentFunction {
    private String functionName = null;
    private Method method;
    private Object enrichmentInstance;

    public EnrichmentFunction(){

    }

    private EnrichmentFunction(String functionName, String pathToJar){
        try {
            URLClassLoader child = new URLClassLoader (new URL[] {new URL(pathToJar)});
            Class classToLoad = Class.forName("edu.uci.ics.perpetual.enrichment." + functionName, true, child);
            method = classToLoad.getDeclaredMethod("enrich", DataObject.class);
            enrichmentInstance = classToLoad.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static EnrichmentFunction getEnrichmentFunction(String functionName, String pathToJar){
        return new EnrichmentFunction(functionName, pathToJar);
    }


    public void execute(DataObject dataObject) {
        try {
            JsonObject object = dataObject.getObject();
            String result = (String) method.invoke(enrichmentInstance, object);
            object.addProperty(functionName, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
