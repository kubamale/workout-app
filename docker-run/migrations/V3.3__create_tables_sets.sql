CREATE TABLE workouts.distance_sets
(
    id                  UUID             NOT NULL,
    set_order           INTEGER          NOT NULL,
    workout_exercise_id UUID,
    distance            DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_distance_sets PRIMARY KEY (id)
);

CREATE TABLE workouts.time_sets
(
    id                  UUID             NOT NULL,
    set_order           INTEGER          NOT NULL,
    workout_exercise_id UUID,
    time                BIGINT           NOT NULL,
    weight              DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_time_sets PRIMARY KEY (id)
);

CREATE TABLE workouts.weight_sets
(
    id                  UUID             NOT NULL,
    set_order           INTEGER          NOT NULL,
    workout_exercise_id UUID,
    reps                INTEGER          NOT NULL,
    weight              DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_weight_sets PRIMARY KEY (id)
);

ALTER TABLE workouts.distance_sets
    ADD CONSTRAINT FK_DISTANCE_SETS_ON_WORKOUTEXERCISE FOREIGN KEY (workout_exercise_id) REFERENCES workouts.workout_exercises (id);

ALTER TABLE workouts.time_sets
    ADD CONSTRAINT FK_TIME_SETS_ON_WORKOUTEXERCISE FOREIGN KEY (workout_exercise_id) REFERENCES workouts.workout_exercises (id);

ALTER TABLE workouts.weight_sets
    ADD CONSTRAINT FK_WEIGHT_SETS_ON_WORKOUTEXERCISE FOREIGN KEY (workout_exercise_id) REFERENCES workouts.workout_exercises (id);