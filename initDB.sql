CREATE DATABASE IF NOT EXISTS javafx;

USE javafx;

CREATE TABLE IF NOT EXISTS people
(
    id         INT(5) PRIMARY KEY AUTO_INCREMENT,
    first_name varchar(250) NOT NULL,
    last_name  varchar(250) NOT NULL,
    birthdate  Date         NOT NULL
);