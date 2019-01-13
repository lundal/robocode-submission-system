CREATE TABLE location (
    id serial primary key,
    title varchar(30) unique not null,
    registration_code varchar(50) unique not null,
    deadline timestamp not null
);

insert into location(title, registration_code, deadline)
values ('escape', 'escape2019', (TIMESTAMP '2019-01-24 20:00:00'));

CREATE TABLE administrator (
    id serial primary key,
    title varchar(30) not null unique,
    password varchar(80) not null,
    superuser boolean default false not null,
    location_id bigint not null references location(id)
);

insert into administrator(title, password, superuser, location_id)
values ('admin', '$2a$10$CpfkIWlSNzE1GyNZj.2VIe9GZI1Y.Q5bMH6CovPD2YJ5NENmlu/i.', true, (select id from location where title = 'escape'));

CREATE TABLE team (
    id serial primary key,
    title varchar(30) not null unique,
    password varchar(80) not null unique,
    members text not null,
    location_id bigint not null references location(id)
);

CREATE TABLE delivery (
    id serial primary key,
    created timestamp not null,
    filename varchar(100) not null,
    team_id bigint not null references team(id)
)