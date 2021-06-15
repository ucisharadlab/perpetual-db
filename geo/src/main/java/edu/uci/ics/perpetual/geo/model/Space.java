package edu.uci.ics.perpetual.sensors.model;

import java.util.List;


public class Space {

	public class Coordinate {
		String lat;
		String lng;
		
		public Coordinate(String lat, String lng) {
			this.lat = lat;
			this.lng = lng;
		}	
	}

	public String space_id;
	public String space_name;
	public String parent_space_id;
	public String coordinate_system_name;
	public List<Coordinate> vertices; 

	public Space(String space_id, String space_name, String parent_space_id, String coordinate_system_name, List<Coordinate> vertices) {
	this.space_id = space_id;
	this.space_name = space_name;
	this.parent_space_id = parent_space_id;
	this.coordinate_system_name = coordinate_system_name;
	this.vertices = vertices;
}


}
