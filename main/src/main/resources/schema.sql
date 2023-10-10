CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uq_category UNIQUE (name)
);
CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_email UNIQUE (email)
);
CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat DECIMAL                                 NOT NULL,
    lon DECIMAL                                 NOT NULL,

    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2500)                           NOT NULL,
    category_id        BIGINT                                  NOT NULL,
    created_on         timestamp   default (now())             NOT NULL,
    description        VARCHAR(7100)                           NOT NULL,
    event_date         timestamp                               NOT NULL,
    initiator_id       BIGINT                                  NOT NULL,
    location_id        BIGINT                                  NOT NULL,
    paid               BOOLEAN     default false               NOT NULL,
    participant_limit  INTEGER     default 0                   NOT NULL,
    confirmed_requests INTEGER     default 0                   NOT NULL,
    published_on       timestamp,
    request_moderation BOOLEAN     default true                NOT NULL,
    state              VARCHAR(25) default 'PENDING'           NOT NULL,
    title              VARCHAR(130)                            NOT NULL,

    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_events_categories FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_events_locations FOREIGN KEY (location_id) REFERENCES locations (id),
    CONSTRAINT fk_events_users FOREIGN KEY (initiator_id) REFERENCES users (id)
);
CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    created      timestamp                               NOT NULL,
    status       VARCHAR(25)                             NOT NULL,

    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_requests_users FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT fk_requests_events FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT uq_requests UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN                                 NOT NULL,
    title  VARCHAR(50)                             NOT NULL,

    CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events_compilations
(
    compilation_id BIGINT NOT NULL,
    event_id       BIGINT NOT NULL,

    CONSTRAINT pk_events_compilations PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT fk_events_compilations_compilations FOREIGN KEY (compilation_id) REFERENCES compilations (id),
    CONSTRAINT fk_events_compilations_events FOREIGN KEY (event_id) REFERENCES events (id)
);