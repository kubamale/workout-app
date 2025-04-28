CREATE TABLE workouts.workout_exercises
(
    id             UUID    NOT NULL,
    dtype          VARCHAR(31),
    workout_id     UUID,
    exercise_id    UUID,
    exercise_order INTEGER NOT NULL,
    CONSTRAINT pk_workout_exercises PRIMARY KEY (id)
);

ALTER TABLE workouts.workout_exercises
    ADD CONSTRAINT FK_WORKOUT_EXERCISES_ON_WORKOUT FOREIGN KEY (workout_id) REFERENCES workouts.workouts (id);