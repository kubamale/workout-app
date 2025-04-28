CREATE TABLE statistics.measurements
(
    id          UUID             NOT NULL,
    user_id     UUID             NOT NULL,
    date        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    weight      DOUBLE PRECISION NOT NULL,
    body_fat    DOUBLE PRECISION,
    left_arm    DOUBLE PRECISION,
    right_arm   DOUBLE PRECISION,
    chest       DOUBLE PRECISION,
    waist       DOUBLE PRECISION,
    hips        DOUBLE PRECISION,
    left_thigh  DOUBLE PRECISION,
    right_thigh DOUBLE PRECISION,
    left_calf   DOUBLE PRECISION,
    right_calf  DOUBLE PRECISION,
    shoulders   DOUBLE PRECISION,
    CONSTRAINT pk_measurements PRIMARY KEY (id)
);