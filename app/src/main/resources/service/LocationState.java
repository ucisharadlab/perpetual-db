package service;

import java.util.ArrayList;
import java.util.List;

public class LocationState {
    public String accessPoint;
    public List<String> possibleRooms = new ArrayList<>();
    public double insideProb;
    public boolean inside;

    public void print() {
        System.out.println(String.format("AP: %s, Inside Prob: %.3f, Inside Classification: %s, Possible Rooms: %s",
                accessPoint, insideProb, inside, possibleRooms.toString()));
    }

    boolean verifyIn(String truth, int verbose, String timestamp, String name){
        if (!inside) {
            if (verbose >= 2) {
                System.out.println(String.format("%s: %s Predicted outside, truth is inside, room: %s.", name, timestamp, truth));
            }
            return false;
        }
        return true;
    }

    boolean verifyOut(int verbose, String timestamp, String name){
        if (inside) {
            if (verbose >= 2) {
                System.out.println(String.format("%s: %s Predicted inside, truth is out.", name, timestamp));
            }
            return false;
        }
        return true;
    }

    boolean verifyAP(String truth, APtoRoom aptoRoom, int verbose, String timestamp, String name) {
        List<String> possibleRoom = aptoRoom.find(accessPoint);
        for (String room : possibleRoom) {
            if (room.equals(truth)) {
                return true;
            }
        }
        if (verbose >= 1) {
            System.out.println(String.format("%s: %s Predicted AP: %s, its room list %s, true room: %s",
                    name, timestamp, accessPoint, possibleRoom.toString(), truth));
        }
        return false;
    }
}
