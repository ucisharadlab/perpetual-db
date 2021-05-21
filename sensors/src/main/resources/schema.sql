\connect perpetual;

CREATE TABLE ObservationTypes (
    name VARCHAR(50) PRIMARY KEY,
    attribute VARCHAR(50) NOT NULL,
    valueType VARCHAR(50) NOT NULL
);

CREATE TABLE SensorTypes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    observationType VARCHAR(50) NOT NULL
);

CREATE TABLE Sensors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type int NOT NULL,
    platformName VARCHAR(50),
    locationSource int,
    location VARCHAR(50),
    viewArea VARCHAR(50),
    spec TEXT
);

-- CREATE TABLE Platforms (
--     id SERIAL PRIMARY KEY,
--     name VARCHAR(50) NOT NULL,
--     sensor int NOT NULL
-- );

