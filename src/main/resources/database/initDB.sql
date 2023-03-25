CREATE TABLE IF NOT EXISTS User
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(55) NOT NULL
);
INSERT INTO user(name)
VALUES ('Nik');
INSERT INTO user(name)
VALUES ('Tom');
INSERT INTO user(name)
VALUES ('Tim');

SELECT *
FROM user;

CREATE TABLE IF NOT EXISTS Event
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES User (id)
        ON DELETE CASCADE,
    file_id INT UNIQUE,
    FOREIGN KEY (file_id) REFERENCES File (id)
        ON DELETE CASCADE
);

INSERT INTO event(user_id, file_id)
VALUES (1, 1);


CREATE TABLE IF NOT EXISTS File
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(55) NOT NULL,
    filePath VARCHAR(55) NOT NULL
);

INSERT INTO File (name, filePath)
VALUES ('file1', 'path1');

DROP TABLE user;
DROP TABLE event;
DROP TABLE file;
