package edu.uci.ics.perpetual.enrichment;

import edu.uci.ics.perpetual.data.DataObject;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class EnrichmentFunction {

    public final String functionName = null;
    Class classToLoad;
    Method method;
    Object enrichmentInstance;
    private EnrichmentFunction(String pathToJar){


        try {
            URLClassLoader child = new URLClassLoader (new URL[] {new URL(pathToJar)});

            classToLoad = Class.forName("edu.uci.ics.perpetual.enrichment.Enrichment", true, child);
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
