package edu.uci.ics.perpetual.enrichment;

import com.google.gson.JsonObject;
import edu.uci.ics.perpetual.data.DataObject;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class EnrichmentFunction {
    private String functionName;
    private Method method;
    private Object enrichmentInstance;

    private EnrichmentFunction(String pathToJar) {
        try {
            String functionName = pathToJar.substring(pathToJar.lastIndexOf('/') + 1, pathToJar.lastIndexOf(".jar"));
            this.functionName = functionName;
            URLClassLoader child = new URLClassLoader(new URL[] { new URL(pathToJar) });
            Class classToLoad = Class.forName(functionName, true, child);
            method = classToLoad.getDeclaredMethod("enrich", JsonObject.class);
            enrichmentInstance = classToLoad.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static EnrichmentFunction getEnrichmentFunction(String pathToJar) {
        return new EnrichmentFunction(pathToJar);
    }

    public void execute(DataObject dataObject) {
        try {
            String result = (String) method.invoke(enrichmentInstance, dataObject.getObject());
            dataObject.getObject().addProperty(functionName, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
