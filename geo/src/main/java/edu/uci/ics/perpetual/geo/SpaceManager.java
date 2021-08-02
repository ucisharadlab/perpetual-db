package edu.uci.ics.perpetual.geo;

import edu.uci.ics.perpetual.geo.model.*;
//import edu.uci.ics.perpetual.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class SpaceManager {
    private final SpaceRepository repo = new SpaceRepository();

    //Vertices is a list of vertices with the (lat, lng) format
    public void createSpace(String spaceName, String parentSpaceId, String coordinateSystemName, String spaceShape, List<List<String>> vertices) throws Exception {
        List<Coordinate> coordinates = new LinkedList<Coordinate>();
        for (List<String> vertex : vertices) {
            coordinates.add(new Coordinate(vertex));
        }
        repo.insertSpace(new Space(spaceName, parentSpaceId, coordinateSystemName, spaceShape, coordinates));
    }
    public List<Coordinate> getSpaceCoordinates(String spaceName) {return repo.getSpaceCoordinates(spaceName);}
    public Space getSpace(String spaceName) {
        return repo.fetchSpace(spaceName);
    }
    public String getCoordinateSystem(String spaceName) {
       return repo.getCoordSys(spaceName);
    }
    public Space getParentSpace(String spaceName) {
        return repo.getParentSpace(spaceName);
    }
    public String getSpaceShape(String spaceName) {
        return repo.getShape(spaceName);
    }
    public void removeSpace(String spaceName) throws Exception {repo.deleteSpace(spaceName); }
    public Boolean intersects(String spaceName1, String spaceName2) {
        return repo.intersect(spaceName1, spaceName2);
    }
    public double getArea(String spaceName) { return repo.getArea(spaceName);}
    public double getDistance(String spaceName1, String spaceName2) {return repo.getDistance(spaceName1, spaceName2);}
    public Boolean contains(String spaceName1, String spaceName2) {return repo.contains(spaceName1, spaceName2);}
}