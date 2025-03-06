INSERT INTO workouts(id, name, user_id)
VALUES ('837e92ab-8efc-4db7-9f18-bd53e81d9a5e', 'legs', 'e00e52c7-f272-41a3-b6f3-ff16867f874a'),
       ('7bab682e-b861-41c7-9dce-95f283eab0bd', 'arms', 'e00e52c7-f272-41a3-b6f3-ff16867f874a');

INSERT INTO workout_exercises(id, exercise_order, exercise_id, workout_id, dtype)
VALUES ('ddbf13f8-07cc-4003-9a13-64d066176ae4', 0, '699cfcbb-6fa5-4799-8ae9-b53c0b79c6df',
        '837e92ab-8efc-4db7-9f18-bd53e81d9a5e', 'WeightWorkoutExerciseEntity'),
       ('f863384d-f9c1-490e-a1b2-8840ca01bad1', 1, '560401f2-95e5-4a15-bc7a-59e39e1d5466',
        '837e92ab-8efc-4db7-9f18-bd53e81d9a5e', 'TimeWorkoutExerciseEntity'),
       ('ccf49de1-4191-413d-bd2c-e313acdc6420', 2, 'a7730833-8251-45fb-976a-f84feee47aa6',
        '837e92ab-8efc-4db7-9f18-bd53e81d9a5e', 'DistanceWorkoutExerciseEntity'),
       ('a7730833-8251-45fb-976a-f84feee47aa6', 0, '4d6335e4-7a5b-4d33-8d0c-2d4f67f64fca',
        '7bab682e-b861-41c7-9dce-95f283eab0bd', 'WeightWorkoutExerciseEntity'),
       ('daaba337-7f70-4e94-9496-6d52fc910255', 1, 'a7730833-8251-45fb-976a-f84feee47aa6',
        '7bab682e-b861-41c7-9dce-95f283eab0bd', 'WeightWorkoutExerciseEntity');

INSERT INTO weight_sets(id, set_order, reps, weight, workout_exercise_id)
VALUES ('d539eb1a-cd75-4a5d-b3ca-0f945d0b211d', 0, 10, 30.0, 'ddbf13f8-07cc-4003-9a13-64d066176ae4'),
       ('3d8c0fc7-2e72-47d9-adfb-3c4c98d6ade0', 1, 8, 32.0, 'ddbf13f8-07cc-4003-9a13-64d066176ae4'),
       ('b2fa6060-6840-4f0e-8100-04d5026ee491', 2, 6, 32.0, 'ddbf13f8-07cc-4003-9a13-64d066176ae4'),
       ('d909c965-77cd-4fef-a27e-d5a854eacec3', 0, 8, 24.0, 'a7730833-8251-45fb-976a-f84feee47aa6'),
       ('b8aad0ec-78e8-42f5-9053-f28e9cb0e59a', 1, 8, 24.0, 'a7730833-8251-45fb-976a-f84feee47aa6'),
       ('631854f9-b307-4806-aa53-bd80023a2d7b', 0, 12, 36.0, 'daaba337-7f70-4e94-9496-6d52fc910255');

INSERT INTO time_sets(id, set_order, weight, time, workout_exercise_id)
VALUES ('1e25baac-a153-4947-a2eb-3af45836e5a4', 0, 30.0, 360, 'f863384d-f9c1-490e-a1b2-8840ca01bad1'),
       ('9b41c491-0460-4bca-ae2f-92cd05a68246', 1, 30.0, 360, 'f863384d-f9c1-490e-a1b2-8840ca01bad1'),
       ('dada4d7b-ee67-4bcc-a10b-b33d495acf21', 2, 30.0, 360, 'f863384d-f9c1-490e-a1b2-8840ca01bad1');

INSERT INTO distance_sets(id, set_order, distance, workout_exercise_id)
VALUES ('55763ec7-261d-4d68-8d5c-ff16464fe5a2', 0, 23.5, 'ccf49de1-4191-413d-bd2c-e313acdc6420'),
       ('67542394-718c-436b-b20a-bbbf996d10af', 1, 20.0, 'ccf49de1-4191-413d-bd2c-e313acdc6420');



