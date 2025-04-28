CREATE TABLE auth.reset_passwords
(
    id      UUID         NOT NULL,
    token   VARCHAR(255) NOT NULL,
    expires TIMESTAMP WITHOUT TIME ZONE,
    user_id UUID,
    CONSTRAINT pk_reset_passwords PRIMARY KEY (id)
);

ALTER TABLE auth.reset_passwords
    ADD CONSTRAINT uc_reset_passwords_token UNIQUE (token);

ALTER TABLE auth.reset_passwords
    ADD CONSTRAINT FK_RESET_PASSWORDS_ON_USER FOREIGN KEY (user_id) REFERENCES auth.users (id);