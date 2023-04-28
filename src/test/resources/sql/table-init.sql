CREATE SCHEMA IF NOT EXISTS taxi;

create table if not exists taxi.region
(
    id serial primary key,
    name varchar
);
