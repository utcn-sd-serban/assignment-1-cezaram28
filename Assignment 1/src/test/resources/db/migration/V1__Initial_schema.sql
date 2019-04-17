CREATE TABLE IF NOT EXISTS user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(32),
    password VARCHAR(32),
    email VARCHAR(64),
    score INT,
    is_admin BOOLEAN,
    is_banned BOOLEAN
);

CREATE TABLE IF NOT EXISTS question (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(64),
    author_id INT,
    text VARCHAR(500),
    creation_date DATETIME,
    vote_count INT,
    FOREIGN KEY(author_id) REFERENCES user(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS answer (
    id INT PRIMARY KEY AUTO_INCREMENT,
    question_id INT,
    author_id INT,
    text VARCHAR(500),
    creation_date DATETIME,
    vote_count INT,
    FOREIGN KEY(author_id) REFERENCES user(id) ON DELETE SET NULL,
    FOREIGN KEY(question_id) REFERENCES question(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tag (
     id INT PRIMARY KEY AUTO_INCREMENT,
     name VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS question_tags (
     question_id INT,
     tag_id INT,
     FOREIGN KEY(question_id) REFERENCES question(id) ON DELETE CASCADE,
     FOREIGN KEY(tag_id) REFERENCES tag(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS vote (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(6),
    question_id INT,
    answer_id INT,
    user_id INT,
    FOREIGN KEY(question_id) REFERENCES question(id) ON DELETE CASCADE,
    FOREIGN KEY(answer_id) REFERENCES answer(id) ON DELETE CASCADE,
    FOREIGN KEY(user_id) REFERENCES user(id) ON DELETE CASCADE
);