CREATE DATABASE IF NOT EXISTS app_db;
USE app_db;
CREATE TABLE IF NOT EXISTS products (
    id UUID NOT NULL,
    availableQuantity INT,
    CONSTRAINT id_pk PRIMARY KEY (id)
);
CREATE USER 'springUser'@'%' IDENTIFIED BY 'ThePassword';
GRANT SELECT, INSERT, DELETE, UPDATE ON app_db.* TO 'springUser'@'%';