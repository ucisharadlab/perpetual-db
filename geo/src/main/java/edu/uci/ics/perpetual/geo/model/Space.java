package edu.uci.ics.perpetual.geo.model;

import java.util.List;


public class Space {

	public String space_id;
	public String space_name;
	public String parent_space_id;
	public String coordinate_system_name;
	public String space_shape;
	public List<Coordinate> vertices; 


	public Space(String space_id, String space_name, String parent_space_id, String coordinate_system_name, String space_shape, List<Coordinate> vertices) {
	this.space_id = space_id;
	this.space_name = space_name;
	this.parent_space_id = parent_space_id;
	this.coordinate_system_name = coordinate_system_name;
	this.space_shape = space_shape;
	this.vertices = vertices;
}

	public Space(String space_name, String parent_space_id, String coordinate_system_name,String space_shape, List<Coordinate> vertices) {
		this.space_name = space_name;
		this.parent_space_id = parent_space_id;
		this.coordinate_system_name = coordinate_system_name;
		this.space_shape = space_shape;
		this.vertices = vertices;
	}
	public Space(String space_name, String parent_space_id, String coordinate_system_name, String space_shape) {
		this.space_name = space_name;
		this.parent_space_id = parent_space_id;
		this.coordinate_system_name = coordinate_system_name;
		this.space_shape = space_shape;
	}
	@Override
	public String toString() {
		String space ="{space_id: "+ this.space_id + ", space_name: " + this.space_name + ", parent_space_id: " + this.parent_space_id + ", coordinate_system_name: "
				+ this.coordinate_system_name + ", space_shape: " + this.space_shape + ", vertices: {";
		if (this.vertices.size() == 0) { space += "},";};
		for (Coordinate vertex : this.vertices) {
			space = space + vertex.toString() + ",";
		}
		space = space.substring(0, space.length()-1) + "}}";
		return space;
	}
}
