INSERT INTO workouts (id, name, user_id)
VALUES ('dcd7e472-ab3d-45d6-80de-ea817b2044ec', 'Łapy', 'a2939535-acc9-4cad-aadd-406df78a4715');

INSERT INTO workout_exercises(id, exercise_id, exercise_order, workout_id, dtype)
VALUES ('a43a829a-de1d-4441-b67f-dd7003991612', '9d0ba726-7f52-43d1-9921-f824939357d7', 0,
        'dcd7e472-ab3d-45d6-80de-ea817b2044ec', 'WeightWorkoutExerciseEntity');

INSERT INTO weight_sets(id, set_order, workout_exercise_id, reps, weight)
VALUES ('678b7573-f703-4eca-a04e-7ef25a0ef69e', 0, 'a43a829a-de1d-4441-b67f-dd7003991612', 10, 23),
       ('42313a59-c4b6-4fa5-b8e4-4b64891135c7', 1, 'a43a829a-de1d-4441-b67f-dd7003991612', 8, 25),
       ('3f0af1b8-6b0a-47bb-87e6-3c21b833e6af', 2, 'a43a829a-de1d-4441-b67f-dd7003991612', 6, 27);