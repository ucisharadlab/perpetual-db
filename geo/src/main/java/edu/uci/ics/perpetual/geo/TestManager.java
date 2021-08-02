package edu.uci.ics.perpetual.geo;

import edu.uci.ics.perpetual.geo.model.Coordinate;
import edu.uci.ics.perpetual.geo.model.Space;
import org.apache.log4j.BasicConfigurator;

import java.util.List;


public class TestManager {
    static SpaceManager manager = new SpaceManager();

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
       // testGetCoords();
       //  testGetCoordSys();
       // testRemoveSpace();
       // testGetSpace();
       // testGetParent();
       // testGetShape();
       //  testIntersects();
       // testGetArea();
       // testGetDistance();
       // testContains();

    }


    private static void testGetCoords() {
        List<Coordinate> coords = manager.getSpaceCoordinates("dbh");
        for (Coordinate vertex : coords) {
            System.out.println(vertex);
        }
    }
    private static void testGetCoordSys() {
        String coordSys = manager.getCoordinateSystem("uci");
        System.out.println(coordSys);

    }
    private static void testRemoveSpace() throws Exception {
        manager.removeSpace("dbh2081");

    }
    private static void testGetSpace() {
        Space space = manager.getSpace("dbh");
        System.out.println(space);
    }
    private static void testGetParent(){
        Space parentName = manager.getParentSpace("uci");
        System.out.println(parentName);
    }
    private static void testGetShape(){
        String shape = manager.getSpaceShape("uci");
        System.out.println(shape);
    }
    private static void testIntersects(){
        Boolean flag = manager.intersects("dbh2092","test");
        System.out.println(flag);
    }
    private static void testGetArea(){
        Double area = manager.getArea("dbh2081");
        System.out.println(area);
    }
    private static void testGetDistance(){
        Double distance = manager.getDistance("dbh2092","test");
        System.out.println(distance);
    }
    private static void testContains(){
        Boolean flag = manager.contains("dbh2092","test");
        System.out.println(flag);
    }


}