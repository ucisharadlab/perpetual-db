package edu.uci.ics.perpetual.geo;

import edu.uci.ics.perpetual.geo.model.*;
import org.apache.log4j.BasicConfigurator;

import java.util.LinkedList;
import java.util.List;

public class TestManager {
    static SpaceManager manager = new SpaceManager();

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        testGetCoords();
    }
    private static void testGetCoords() {
        manager.getSpaceCoordinates("uci");
    }
}