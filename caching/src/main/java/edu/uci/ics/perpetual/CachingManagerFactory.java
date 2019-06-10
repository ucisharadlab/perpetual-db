package edu.uci.ics.perpetual;

public class CachingManagerFactory {

    private static final int PORT_NUM = 6379;
    private static CachingManager cachingManager = null;

    public static CachingManager getCachingManager() {
        if (cachingManager == null) {
            cachingManager = new CachingManager();
        }

        return cachingManager;
    }

}
