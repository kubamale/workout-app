CREATE TABLE workouts.workouts
(
    id      UUID NOT NULL,
    name    VARCHAR(255),
    user_id UUID,
    CONSTRAINT pk_workouts PRIMARY KEY (id)
);