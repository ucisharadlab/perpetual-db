\connect perpetual;

-- Enable PostGIS (as of 3.0 contains just geometry/geography)
CREATE EXTENSION postgis;
-- enable raster support (for 3+)
CREATE EXTENSION postgis_raster;
-- Enable Topology
CREATE EXTENSION postgis_topology;
-- Enable PostGIS Advanced 3D
-- and other geoprocessing algorithms
-- sfcgal not available with all distributions
CREATE EXTENSION postgis_sfcgal;
-- fuzzy matching needed for Tiger
CREATE EXTENSION fuzzystrmatch;
-- rule based standardizer
CREATE EXTENSION address_standardizer;
-- example rule data set
CREATE EXTENSION address_standardizer_data_us;
-- Enable US Tiger Geocoder
CREATE EXTENSION postgis_tiger_geocoder;

CREATE TYPE coordinate AS
(
    lat text,
    lng text,
    z text
);

CREATE TABLE space
(
    space_id SERIAL,
    space_name text NOT NULL,
    parent_space_id integer,
    coordinate_system_name text NOT NULL,
    space_shape text,
    vertices coordinate[],
    PRIMARY KEY(space_id),
    CONSTRAINT unique_name UNIQUE(space_name),
	FOREIGN KEY(parent_space_id) REFERENCES space(space_id)
);

CREATE TABLE geo
(
    space_id integer,
    geom geometry,
    geog geography,
    PRIMARY KEY(space_id),
    FOREIGN KEY(space_id)
      REFERENCES space(space_id)
);

CREATE TABLE cust_coord_sys
(
    space_id integer NOT NULL,
    srid integer NOT NULL,
    PRIMARY KEY(space_id, srid),
    FOREIGN KEY(space_id)
      REFERENCES space(space_id)
);

INSERT INTO space(space_name, coordinate_system_name) VALUES('Earth', 'GPS');

CREATE OR REPLACE FUNCTION populate_geo_func()
RETURNS TRIGGER LANGUAGE PLPGSQL AS $$
DECLARE
	parent_coord_sys TEXT;
	child_coord_sys TEXT;
	parent_space_name TEXT;
	existing_srid NUMERIC;
	newVertices coordinate[];
	geom TEXT;
	vertex coordinate;
	insertSRIDQuery TEXT;
	insertCustCoordQuery TEXT;
	insertSpaceQuery TEXT;
	lat_0 NUMERIC;
	lng_0 NUMERIC;
BEGIN
	SELECT s.coordinate_system_name, s.space_name INTO parent_coord_sys, parent_space_name FROM space s WHERE s.space_id = NEW.parent_space_id::integer;
	SELECT c.srid INTO existing_srid FROM cust_coord_sys c WHERE c.space_id = NEW.parent_space_id::integer;
	newVertices := NEW.vertices;

	RAISE NOTICE 'CHILDCOORD: %', NEW.coordinate_system_name;
	RAISE NOTICE 'PARENTCOORD: %', parent_coord_sys;
 	IF parent_coord_sys IS NOT NULL AND parent_coord_sys <> NEW.coordinate_system_name AND existing_srid IS NULL THEN
		SELECT (MIN(V.lat::numeric)+MAX(V.lat::numeric))/2 , (MIN(V.lng::numeric)+ MAX(V.lng::numeric))/2 INTO lat_0, lng_0 FROM space s, UNNEST(s.vertices) AS V WHERE s.space_id = NEW.parent_space_id::integer;

		existing_srid := floor(random()* (998999-910000+1) + 910000)::integer;
		insertSRIDQuery := 'INSERT INTO spatial_ref_sys(srid, proj4text, srtext) VALUES (' || existing_srid::text || ', ' || '''+proj=tmerc +lat_0=' || lat_0 || ' +lon_0=' || lng_0 || ' +k=1 +x_0=0 +y_0=0 +ellps=GRS80 +datum=NAD83 +units=m +no_defs '', ''PROJCS["' || parent_space_name || '", GEOGCS["' || parent_space_name || '",DATUM["North_American_Datum_1983", SPHEROID["GRS 1980",6378137,298.257222101,AUTHORITY["EPSG","7019"]],AUTHORITY["EPSG","6269"]],PRIMEM["Greenwich",0,AUTHORITY["EPSG","8901"]],UNIT["degree",0.01745329251994328,AUTHORITY["EPSG","9122"]],AUTHORITY["EPSG","4269"]],UNIT["metre",1,AUTHORITY["EPSG","9001"]],PROJECTION["Transverse_Mercator"],PARAMETER["latitude_of_origin",30.5],PARAMETER["central_meridian",-85.83333333333333],PARAMETER["scale_factor",0.99996],PARAMETER["false_easting",0],PARAMETER["false_northing",0],AXIS["X",EAST],AXIS["Y",NORTH]]'');';
		insertCustCoordQuery := 'INSERT INTO cust_coord_sys(space_id, srid) VALUES (' || NEW.parent_space_id::text || ', ' || existing_srid::text || ');';
		EXECUTE insertSRIDQuery;
		EXECUTE insertCustCoordQuery;
		RAISE NOTICE 'INSERTQUERY: %', insertSRIDQuery;
		RAISE NOTICE 'CUSTCOORD: %', insertCustCoordQuery;
	END IF;
	--Populating the geo table by converting vertices to PostGIS datatypes
	--Case if coordinate system is GPS then Geography type is used
	IF (NEW.coordinate_system_name) = 'gps' THEN
		geom := 'ST_GeogFromText(''';
		IF LOWER(NEW.space_shape)  = 'polygon' THEN
			geom:= geom || 'POLYGON((';
			FOREACH vertex in ARRAY newVertices
			LOOP
				geom := geom || vertex.lat || ' ' || vertex.lng || ', ';
			END LOOP;
			geom := SUBSTRING(geom, 0, length(geom)-1);
			geom := geom || '))'')';
		ELSIF LOWER(NEW.space_shape) = 'point' THEN
			geom := geom || 'POINT(';
			vertex := newVertices[1];
			geom := geom || vertex.lat || ' ' || vertex.lng || ')'')';
		ELSE
			RAISE NOTICE 'Shape currently not supported';
		END IF;
	insertSpaceQuery := 'INSERT INTO geo(space_id, geog) VALUES(' || NEW.space_id::text || ', ' || geom || ');';
	EXECUTE insertSpaceQuery;
	--Case if coordinate system is Cartesian then Geometry type is used
	ELSE
		geom := 'ST_GeomFromText(''';
		IF LOWER(NEW.space_shape) = 'polygon' THEN
			geom := geom || 'POLYGON((';
			FOREACH vertex in ARRAY newVertices
			LOOP
				IF LOWER(NEW.coordinate_system_name) = 'cartesian2d' THEN
					geom := geom || vertex.lat || ' ' || vertex.lng || ', ';
				ELSIF LOWER(NEW.coordinate_system_name) = 'cartesian2hfd' THEN
					geom := geom || vertex.lat || ' ' || vertex.lng || ' ' || vertex.z || ', ' ;
				ELSE
					RAISE NOTICE 'Unknown coordinate system';
				END IF;
			END LOOP;
			geom := SUBSTRING(geom, 0, length(geom)-1);
			geom := geom || '))'',' || existing_srid::text || ')';
		ELSIF LOWER(NEW.space_shape) = 'point' THEN
			RAISE NOTICE 'POINT';
			geom := geom || 'POINT(';
			vertex := newVertices[1];
			IF LOWER(NEW.coordinate_system_name) = 'cartesian2d' THEN
				geom := geom || vertex.lat || ' ' || vertex.lng || ')'',' || existing_srid::text || ')';
			ELSIF LOWER(NEW.coordinate_system_name) = 'cartesian2hfd' THEN
					geom := geom || vertex.lat || ' ' || vertex.lng || ' ' || vertex.z || ')'',' || existing_srid::text || ')';
			ELSE
				RAISE NOTICE 'Unknown coordinate system';
			END IF;
		END IF;
	insertSpaceQuery := 'INSERT INTO geo(space_id, geom) VALUES(' || NEW.space_id::text || ', ' || geom || ');';
	RAISE NOTICE 'INSERTSPACEQUERY: %',insertSpaceQuery;
	EXECUTE insertSpaceQuery;
	END IF;
	RETURN NEW;
END;
$$


CREATE TRIGGER new_space_trigger
AFTER INSERT
ON space
FOR EACH ROW
EXECUTE PROCEDURE populate_geo_func();

CREATE OR REPLACE FUNCTION transforming_func()
RETURNS TRIGGER LANGUAGE PLPGSQL AS $$
DECLARE
	transformQuery TEXT;
	geog geography;
BEGIN
	SELECT ST_Transform(NEW.geom, 4326)::geography INTO geog;
	IF geog IS NOT NULL THEN
		transformQuery:= 'UPDATE geo SET geog = $1 WHERE space_id = $2' ;
	EXECUTE transformQuery USING geog, NEW.space_id;
	END IF;
	RETURN NEW;
END;
$$

CREATE TRIGGER transform_geom
AFTER INSERT
ON geo
FOR EACH ROW
EXECUTE PROCEDURE transforming_func();
