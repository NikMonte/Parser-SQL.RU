CREATE TABLE IF NOT EXISTS vacancies (
    id serial primary key,
    "name" varchar(2000) NOT NULL,
    "text" text NOT NULL,
    url varchar(1000) NOT NULL UNIQUE
);