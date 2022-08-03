-- Table: yvs_grh_element_formule_non_interprete

-- DROP TABLE yvs_grh_element_formule_non_interprete;

CREATE TABLE yvs_grh_element_formule_non_interprete
(
  id serial NOT NULL,
  code_formule character varying,
  societe integer,
  CONSTRAINT yvs_grh_element_formule_non_interprete_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_grh_element_formule_non_interprete_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_grh_element_formule_non_interprete
  OWNER TO postgres;
