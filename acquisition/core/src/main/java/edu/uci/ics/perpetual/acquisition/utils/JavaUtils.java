package edu.uci.ics.perpetual.acquisition.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class JavaUtils {


    /**
     * Creates an instance of a class sent in the method by loading the jar from provided path.
     * It also passes the constructor arguments sent to this method when creating the instance.
     */
    public static Object getObjectOfClass(String path, String className, Object[] constructorArgs) throws Exception {
        File jarFile = new File(path);
        if (jarFile.exists()) {
            URLClassLoader child = new URLClassLoader(
                    new URL[]{jarFile.toURI().toURL()},
                    JavaUtils.class.getClassLoader()
            );
            Class classToLoad = Class.forName(className, true, child);
            return classToLoad.getConstructor().newInstance(constructorArgs);
        }
        return null;
    }

    public static Map<String,String> loadConfigs(){
        Map<String,String> configs = new HashMap <>();
        InputStream inputStream = JavaUtils.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            Properties pr = new Properties();
            pr.load(inputStream);
            Iterator<String> it = pr.stringPropertyNames().iterator();
            while (it.hasNext())
            {
                String key = it.next();
                configs.put(key,pr.getProperty(key));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return configs;
    }


}
