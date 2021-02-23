DROP TABLE dishes IF EXISTS;

CREATE TABLE dishes  (
    dishes_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(20),
    origin VARCHAR(20),
    characteristics VARCHAR(30)
);