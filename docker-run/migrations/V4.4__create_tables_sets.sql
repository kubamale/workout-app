CREATE TABLE statistics.distance_sets
(
    id          UUID             NOT NULL,
    exercise_id UUID,
    distance    DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_distance_sets PRIMARY KEY (id)
);

CREATE TABLE statistics.time_sets
(
    id          UUID             NOT NULL,
    exercise_id UUID,
    time        BIGINT           NOT NULL,
    weight      DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_time_sets PRIMARY KEY (id)
);

CREATE TABLE statistics.weight_sets
(
    id          UUID             NOT NULL,
    exercise_id UUID,
    reps        INTEGER          NOT NULL,
    weight      DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_weight_sets PRIMARY KEY (id)
);

ALTER TABLE statistics.distance_sets
    ADD CONSTRAINT FK_DISTANCE_SETS_ON_EXERCISE FOREIGN KEY (exercise_id) REFERENCES statistics.exercises (id);

ALTER TABLE statistics.time_sets
    ADD CONSTRAINT FK_TIME_SETS_ON_EXERCISE FOREIGN KEY (exercise_id) REFERENCES statistics.exercises (id);

ALTER TABLE statistics.weight_sets
    ADD CONSTRAINT FK_WEIGHT_SETS_ON_EXERCISE FOREIGN KEY (exercise_id) REFERENCES statistics.exercises (id);