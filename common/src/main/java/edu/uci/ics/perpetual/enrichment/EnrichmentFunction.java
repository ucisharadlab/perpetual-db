package edu.uci.ics.perpetual.enrichment;

import com.google.gson.JsonObject;
import edu.uci.ics.perpetual.data.DataObject;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class EnrichmentFunction {
    private String functionName;
    private Method method;
    private Object enrichmentInstance;
    private String tag;

    private EnrichmentFunction(String pathToJar, String tag) {
        try {
            this.tag = tag;
            pathToJar = "file://" + StringUtils.strip(pathToJar, "'");
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
        return new EnrichmentFunction(pathToJar, null);
    }

    public static EnrichmentFunction getEnrichmentFunction(String pathToJar, String tag) {
        return new EnrichmentFunction(pathToJar, tag);
    }

    public void execute(DataObject dataObject) {
        try {
            String result = (String) method.invoke(enrichmentInstance, dataObject.getObject());
            if (tag != null) {
                dataObject.getObject().addProperty(tag, result);
            } else {
                dataObject.getObject().addProperty(functionName, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String executeAndReturnResult(DataObject dataObject) {
        try {
            String result = (String) method.invoke(enrichmentInstance, dataObject.getObject());
            dataObject.getObject().addProperty(functionName, result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }

    @Override
    public String toString() {
        return functionName;
    }
}
