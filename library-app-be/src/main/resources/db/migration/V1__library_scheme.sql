CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS Users (
    id char(36) NOT NULL PRIMARY KEY DEFAULT (UUID_GENERATE_V4()),
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    birthday TIMESTAMP NOT NULL,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Author (
    id char(36) NOT NULL PRIMARY KEY DEFAULT (UUID_GENERATE_V4()),
    full_name VARCHAR(255) NOT NULL,
    birthday TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS Book (
    id char(36) NOT NULL PRIMARY KEY DEFAULT (UUID_GENERATE_V4()),
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(255) NOT NULL,
    available  NOT NULL,
    note VARCHAR(255) NOT NULL,
    genre VARCHAR(255) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    author_id CHAR(36) NOT NULL,
    FOREIGN KEY (author_id) REFERENCES Author(id)
);

CREATE TABLE IF NOT EXISTS Rent (
    id char(36) NOT NULL PRIMARY KEY DEFAULT (UUID_GENERATE_V4()),
    lending_date TIMESTAMP NOT NULL,
    return_date TIMESTAMP,
    status VARCHAR(255) NOT NULL,
    note VARCHAR(255),
    users_id CHAR(36) NOT NULL,
    FOREIGN KEY (users_id) REFERENCES Users(id),
    book_id CHAR(36) NOT NULL,
    FOREIGN KEY (book_id) REFERENCES Book(id)
);