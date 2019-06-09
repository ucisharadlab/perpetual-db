package edu.uci.ics.perpetual;

public class TestAll {


}


/**
 CREATE DATA SOURCE TYPE wifi_sensor(id, name, ip, port)
 CREATE RAW TYPE  wifi_observation(device_mac, timestamp, wifi_ap)
 ADD FOR wifi_observation TAG Person(persons)
 ADD FOR wifi_observation TAG BuildingLocation(buildinglocations)
 ADD FOR wifi_observation TAG RegionLocation(regionlocations)
 ADD FOR wifi_observation TAG RoomLocation(roomlocations)
 ADD FOR wifi_observation TAG Office(office)
 ADD FOR wifi_observation TAG Affinity(affinities)
 UDF Function:
 CREATE FUNCTION getPersonFromMac(device_mac) RETURNS Person Cost
 CREATE FUNCTION getRoomLocation(device_mac, timestamp) RETURNS RoomLocation;
 CREATE FUNCTION getRegionLocation(device_mac, timestamp) RETURNS RegionLocation;
 CREATE FUNCTION getBuildingLocation(device_mac, timestamp) RETURNS BuildingLocation;
 CREATE FUNCTION getOffice(device_mac) RETURNS Office;
 CREATE FUNCTION getAffinity(device_mac, device_mac, timestamp) RETURNS Affinity;
*/