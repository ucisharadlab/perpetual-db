package edu.uci.ics.perpetual.functions;

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

    private EnrichmentFunction(String pathToJar){
        try {
            URLClassLoader child = new URLClassLoader (new URL[] {new URL(pathToJar)});
            Class classToLoad = Class.forName("edu.uci.ics.perpetual.enrichment.Enrichment", true, child);
            method = classToLoad.getDeclaredMethod("enrich", DataObject.class);
            enrichmentInstance = classToLoad.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static EnrichmentFunction getEnrichmentFunction(String pathToJar){
        return new EnrichmentFunction(pathToJar);
    }


    public DataObject execute(DataObject dataObject) {
        try {
            return (DataObject) method.invoke(enrichmentInstance, dataObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
