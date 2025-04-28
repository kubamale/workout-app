DELETE
FROM users;
INSERT INTO users(id, date_of_birth, active, length_units, weight_units, email, first_name, last_name, password)
VALUES ('a2939535-acc9-4cad-aadd-406df78a4715', '2000-01-01', true, 0, 0, 'test@ex.com', 'test', 'test',
        '$2a$10$rCl6QpY6TTs8ZgpHEPbaVeVO6c2IS2Atd2yUhHxT0vc/bL2h59/jq');