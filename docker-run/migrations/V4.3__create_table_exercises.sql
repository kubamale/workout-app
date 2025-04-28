CREATE TABLE statistics.exercises
(
    id                  UUID NOT NULL,
    dtype               VARCHAR(31),
    exercise_id         UUID,
    workout_exercise_id UUID,
    workout_id          UUID,
    CONSTRAINT pk_exercises PRIMARY KEY (id)
);

ALTER TABLE statistics.exercises
    ADD CONSTRAINT FK_EXERCISES_ON_WORKOUT FOREIGN KEY (workout_id) REFERENCES statistics.user_workouts (id);