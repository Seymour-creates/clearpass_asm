CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(60) UNIQUE NOT NULL,
    password    VARCHAR(60) NOT NULL,               -- BCrypt hash
    clearance   VARCHAR(32) NOT NULL                -- matches ClearanceLevel enum
);

CREATE TABLE roles (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(40) UNIQUE NOT NULL
);

CREATE TABLE users_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);
