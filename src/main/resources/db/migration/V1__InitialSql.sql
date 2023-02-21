CREATE SEQUENCE student_id_seq;

CREATE TABLE IF NOT EXISTS 'student' (
    id bigint NOT NULL DEFAULT NEXTVAL('student_id_seq'),
    date_of_birth character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    CONSTRAINT student_pkey PRIMARY KEY (id)
);