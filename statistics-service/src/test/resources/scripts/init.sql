INSERT INTO measurements(id, user_id, date, weight, body_fat, left_arm, right_arm, chest, waist, hips, left_thigh,
                         right_thigh, left_calf, right_calf, shoulders)
VALUES ('4a86008b-872d-46c5-9135-bfea11ff9cd1', 'ad78bab6-2b61-471e-ae9e-e70a4ccb242b', '2025-01-01 11:20:00', 66.0,
        22.0, 34.0,
        34.5, 100.0, 70.0, 80.0, 60.0, 60.0, 33.0, 33.0, 140.0),
       ('1b93eb7a-00fb-45a7-81f4-d877d043d45a', 'ad78bab6-2b61-471e-ae9e-e70a4ccb242b', '2025-01-15 11:20:00', 70.0,
        23.0, 35.0,
        35.0, 101.0, 70.0, 80.0, 60.0, 60.0, 33.0, 33.0, 142.0),
       ('1afd6b29-cd97-43a8-b24a-712db1ccb820', 'ad78bab6-2b61-471e-ae9e-e70a4ccb242b', '2025-02-01 11:20:00', 72.0,
        22.0, 36.0,
        36.5, 103.0, 70.5, 80.0, 60.0, 60.0, 33.0, 33.0, 143.0);

INSERT INTO user_workouts(id, user_id, workout_id, date)
VALUES ('4e4d2119-15e2-44bb-9938-0220daf6cf12', 'ad78bab6-2b61-471e-ae9e-e70a4ccb242b',
        '4ff7b3b8-782f-4dc0-b8e1-a17270e7d449', '2025-01-01 12:12:00'),
       ('353d1001-e934-4a60-96f6-03ef0c6fd121', 'ad78bab6-2b61-471e-ae9e-e70a4ccb242b',
        '4ff7b3b8-782f-4dc0-b8e1-a17270e7d449', '2025-01-03 12:12:00'),
       ('f18e6ddc-e856-47c8-bf8f-19d4c592698d', 'ad78bab6-2b61-471e-ae9e-e70a4ccb242b',
        '4ff7b3b8-782f-4dc0-b8e1-a17270e7d449', '2025-01-06 12:12:00');

INSERT INTO exercises(id, exercise_id, workout_exercise_id, workout_id, dtype)
VALUES ('f7402920-322c-44b4-a2fc-ad70064e13ef', 'b123861c-965b-41eb-8a55-11e94758f71f',
        '001de546-7c65-4c93-9679-f4bc7c19ab12', '353d1001-e934-4a60-96f6-03ef0c6fd121', 'weight'),
       ('f01f6143-353a-49b7-98ca-b0c01f8dc69e', '77168257-6637-4098-87e5-5adf0e18fb5b',
        '1229781a-04e2-47cd-bbc4-896b94733784', '4e4d2119-15e2-44bb-9938-0220daf6cf12', 'weight'),
       ('e2ac9de0-6e39-440d-8437-9056e967e055', '8dd7c09c-2135-4002-beca-aa070b9fb463',
        'e917a829-16f0-44e5-9937-475712f6ddfc', '353d1001-e934-4a60-96f6-03ef0c6fd121', 'time'),
       ('2b98e42d-bc70-4417-9ebe-f2dda8aa5b2f', '8dd7c09c-2135-4002-beca-aa070b9fb463',
        'c0ae2c21-4cd6-4f49-b5a0-7fd74a54d1a0', 'f18e6ddc-e856-47c8-bf8f-19d4c592698d', 'distance');

INSERT INTO weight_sets(id, reps, weight, exercise_id)
VALUES ('855b7cbe-60dc-46b9-b5d9-b2cd043698d2', 10, 30.0, 'f7402920-322c-44b4-a2fc-ad70064e13ef'),
       ('116968b0-fe11-4cff-a497-0b827a3e5f95', 8, 32.0, 'f7402920-322c-44b4-a2fc-ad70064e13ef'),
       ('6493bf76-1465-49fa-adc3-02b4608e805c', 8, 32.0, 'f7402920-322c-44b4-a2fc-ad70064e13ef'),
       ('5d3063d6-70d2-4615-b28d-419e52e045c7', 8, 36.0, 'f01f6143-353a-49b7-98ca-b0c01f8dc69e'),
       ('013d7a82-7d5c-4962-a152-958c68a4ce25', 8, 36.0, 'f01f6143-353a-49b7-98ca-b0c01f8dc69e');

INSERT INTO time_sets(id, time, weight, exercise_id)
VALUES ('f94e49ae-5831-4256-80af-bec3b7902d7b', 1800, 0.0, 'e2ac9de0-6e39-440d-8437-9056e967e055'),
       ('b7d3f0bb-6e90-4e9c-a97c-62c5503b5f1e', 1500, 0.0, 'e2ac9de0-6e39-440d-8437-9056e967e055');

insert into distance_sets(id, distance, exercise_id)
VALUES ('9271025a-730f-44ba-939d-c935c752c787', 23.0, '2b98e42d-bc70-4417-9ebe-f2dda8aa5b2f');




