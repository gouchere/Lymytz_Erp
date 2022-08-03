CREATE TABLE yvs_users_memo
(
  id bigserial NOT NULL,
  titre character varying,
  description character varying,
  date_memo date,
  date_debut_rappel date,
  date_fin_rappel date,
  users integer,
  author bigint,
  CONSTRAINT yvs_users_memo_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_users_memo_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_users_memo_user_fkey FOREIGN KEY (users)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_users_memo
  OWNER TO postgres;