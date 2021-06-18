\connect perpetual;

CREATE TABLE ObservationTypes (
    name VARCHAR(50) PRIMARY KEY,
    attribute VARCHAR(50) NOT NULL,
    valueType VARCHAR(50) NOT NULL
);

CREATE TABLE SensorTypes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    observationType VARCHAR(50) NOT NULL
);

CREATE TABLE Sensors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    type int NOT NULL,
    platformId int,
    mobile bit,
    location VARCHAR(50),
    viewArea VARCHAR(50),
    spec TEXT
);

CREATE TABLE Platforms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    mobile bit
);

CREATE TABLE MobileObjects (
    id int,
    locationSource VARCHAR(50),
    type VARCHAR(10)
);
