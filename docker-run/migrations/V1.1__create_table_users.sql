CREATE TABLE auth.users
(
    active        BOOLEAN NOT NULL,
    date_of_birth date,
    length_units  SMALLINT,
    weight_units  SMALLINT,
    id            UUID    NOT NULL,
    email         VARCHAR(255),
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    password      VARCHAR(255),
    CONSTRAINT users_pkey PRIMARY KEY (id)
);