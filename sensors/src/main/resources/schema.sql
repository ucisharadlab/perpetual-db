\connect perpetual;

CREATE TABLE ObservationType (
    name VARCHAR(50) NOT NULL,
    attribute VARCHAR(50) NOT NULL,
    valueType VARCHAR(50) NOT NULL
);

CREATE TABLE SensorType (
    name VARCHAR(50) NOT NULL,
    observationType VARCHAR(50) NOT NULL
);

CREATE TABLE Sensors (
    name VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    spec TEXT,
    static bit
);
