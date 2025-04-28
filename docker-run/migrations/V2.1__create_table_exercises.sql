CREATE TABLE exercises.exercises
(
    id           UUID NOT NULL,
    name         VARCHAR(255),
    muscle_group VARCHAR(255),
    description  VARCHAR(255),
    type         VARCHAR(255),
    equipment    VARCHAR(255),
    pictureurl   VARCHAR(255),
    CONSTRAINT pk_exercises PRIMARY KEY (id)
);