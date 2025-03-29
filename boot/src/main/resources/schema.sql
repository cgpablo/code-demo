DROP TABLE IF EXISTS PRICES CASCADE;

CREATE TABLE prices (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    PRICE_ID UUID NOT NULL,
    BRAND_ID BIGINT NOT NULL,
    START_DATE TIMESTAMP(6) NOT NULL,
    END_DATE TIMESTAMP(6) NOT NULL,
    PRICE_LIST BIGINT NOT NULL,
    PRODUCT_ID BIGINT NOT NULL,
    PRIORITY INTEGER NOT NULL,
    PRICE NUMERIC(38,2) NOT NULL,
    CURR VARCHAR(255) NOT NULL
);