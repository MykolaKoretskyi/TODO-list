CREATE TABLE IF NOT EXISTS todo_list
(
    id              int8 GENERATED ALWAYS AS IDENTITY NOT NULL ,
    what            varchar(255)                      NOT NULL,
    execution_date  date                              NOT NULL,
    status          varchar(125)                      NOT NULL,
    PRIMARY KEY (id)
);
