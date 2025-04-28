CREATE TABLE statistics.user_workouts
(
    id         UUID NOT NULL,
    user_id    UUID,
    workout_id UUID,
    date       TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_user_workouts PRIMARY KEY (id)
);