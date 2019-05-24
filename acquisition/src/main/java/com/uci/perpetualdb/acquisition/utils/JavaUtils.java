package com.uci.perpetualdb.acquisition.utils;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

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

}
