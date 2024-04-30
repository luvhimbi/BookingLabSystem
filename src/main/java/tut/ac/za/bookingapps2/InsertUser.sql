
-- Add a student user
INSERT INTO users (firstname, lastname, email, password, role,username)
VALUES ('John', 'Doe', 'john.doe@example.com', 'password123', 'STUDENT','johnDoe');

-- Add a mentor user
INSERT INTO users (firstname, lastname, email, password, role,username)
VALUES ('Jane', 'Smith', 'jane.smith@example.com', 'password456', 'MENTOR','JaneSmith');

-- Add a tutor user
INSERT INTO users (firstname, lastname, email, password, role,username)
VALUES ('Michael', 'Johnson', 'michael.johnson@example.com', 'password789', 'TUTOR','MichealJohnson');

-- Add an admin user
INSERT INTO users (firstname, lastname, email, password, role,username)
VALUES ('Emily', 'Davis', 'emily.davis@example.com', 'password012', 'ADMIN','EmilyDavis');

-- Add another student user
INSERT INTO users (firstname, lastname, email, password, role,username)
VALUES ('David', 'Wilson', 'david.wilson@example.com', 'password345', 'STUDENT','David Wilson');