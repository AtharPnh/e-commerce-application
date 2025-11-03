CREATE SEQUENCE IF NOT EXISTS category_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS product_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE If not exists category
(
    id          INTEGER NOT NULL PRIMARY KEY,
    name        VARCHAR(255),
    description VARCHAR(255)
);

CREATE TABLE If not exists  product
(
    id                 INTEGER    NOT NULL PRIMARY KEY,
    name               VARCHAR(255),
    description        VARCHAR(255),
    price              DECIMAL,
    available_quantity DOUBLE PRECISION NOT NULL,
    category_id        INTEGER
    CONSTRAINT FK_PRODUCT_ON_CATEGORY REFERENCES category
);


