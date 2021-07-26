package edu.uci.ics.perpetual.geo.model;

import java.util.List;

public class Coordinate {
    String lat;
    String lng;
    String z;

    public Coordinate(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
        this.z = null;
    }
    public Coordinate(String x, String y, String z) {
        this.lat = x;
        this.lng = y;
        this.z = z;
    }
    public Coordinate(List<String> coord) {
        this.lat = coord.get(0);
        this.lng = coord.get(1);
        if (coord.size() > 2) {
            this.z = coord.get(2);
        }
    }

    public String getLat() {
        return lat;
    }
    public String getLng() {
        return lng;
    }

    @Override
    public String toString(){
        if (this.z == null) return "("+this.lat+","+this.lng+ ", NULL)";
        else return "("+this.lat+","+this.lng+","+ this.z+")";
    }
}