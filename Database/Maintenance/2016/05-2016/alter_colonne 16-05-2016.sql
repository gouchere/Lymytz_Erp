
ALTER TABLE yvs_com_creneau_horaire ADD COLUMN permanent boolean;

ALTER TABLE yvs_com_creneau_horaire_users ADD COLUMN point bigint;
ALTER TABLE yvs_com_creneau_horaire_users
  ADD CONSTRAINT yvs_com_creneau_horaire_users_point_fkey FOREIGN KEY (point)
      REFERENCES yvs_base_point_vente (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_tiers ADD COLUMN site character varying;

CREATE TABLE yvs_base_depot_operation
(
  id serial NOT NULL,
  operation character varying,
  type character varying,
  depot bigint,
  author bigint,
  CONSTRAINT yvs_base_depot_operation_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_depot_operation_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_depot_operation_depot_fkey FOREIGN KEY (depot)
      REFERENCES yvs_base_depots (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_depot_operation
  OWNER TO postgres;  
  
ALTER TABLE yvs_com_client ADD COLUMN code_client character varying;
ALTER TABLE yvs_base_fournisseur ADD COLUMN code_fsseur character varying;

ALTER TABLE yvs_com_doc_stocks ADD COLUMN nature character varying(255);

ALTER TABLE yvs_agences ADD COLUMN adresse_ip character varying;
ALTER TABLE yvs_societes ADD COLUMN adresse_ip character varying;

ALTER TABLE yvs_mut_exercice ADD COLUMN cloturer boolean;

CREATE TABLE yvs_base_exercice_semestre
(
  id serial NOT NULL,
  date_debut date,
  date_fin date,
  cloturer boolean DEFAULT false,
  exercice bigint,
  author bigint,
  CONSTRAINT yvs_base_exericice_semestre_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_exericice_semestre_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_exericice_semestre_exercice_fkey FOREIGN KEY (exercice)
      REFERENCES yvs_mut_exercice (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_exercice_semestre
  OWNER TO postgres;  
  
CREATE TABLE yvs_base_exercice_trimestre
(
  id serial NOT NULL,
  date_debut date,
  date_fin date,
  cloturer boolean DEFAULT false,
  semestre integer,
  author bigint,
  CONSTRAINT yvs_base_exercice_trimestre_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_exercice_trimestre_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_exercice_trimestre_semestre_fkey FOREIGN KEY (semestre)
      REFERENCES yvs_base_exercice_semestre (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_exercice_trimestre
  OWNER TO postgres;  
  

CREATE TABLE yvs_base_exercice_mois
(
  id serial NOT NULL,
  date_debut date,
  date_fin date,
  cloturer boolean DEFAULT false,
  trimestre integer,
  author bigint,
  CONSTRAINT yvs_base_exercice_mois_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_exercice_mois_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_exercice_mois_trimestre_fkey FOREIGN KEY (trimestre)
      REFERENCES yvs_base_exercice_trimestre (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_exercice_mois
  OWNER TO postgres;

CREATE TABLE yvs_base_exercice_semaine
(
  id serial NOT NULL,
  date_debut date,
  date_fin date,
  cloturer boolean DEFAULT false,
  mois integer,
  author bigint,
  CONSTRAINT yvs_base_exercice_semaine_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_exercice_semaine_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_exercice_semaine_mois_fkey FOREIGN KEY (mois)
      REFERENCES yvs_base_exercice_mois (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_exercice_semaine
  OWNER TO postgres;

CREATE TABLE yvs_base_exercice_jour
(
  id serial NOT NULL,
  date_jour date,
  cloturer boolean DEFAULT false,
  semaine integer,
  author bigint,
  CONSTRAINT yvs_base_exercice_jour_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_exercice_jour_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_exercice_jour_semaine_fkey FOREIGN KEY (semaine)
      REFERENCES yvs_base_exercice_semaine (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_exercice_jour
  OWNER TO postgres;
  

ALTER TABLE yvs_base_mouvement_stock ADD COLUMN tranche bigint;
ALTER TABLE yvs_base_mouvement_stock
  ADD CONSTRAINT yvs_base_mouvement_stock_tranche_fkey FOREIGN KEY (tranche)
      REFERENCES yvs_com_type_creneau_horaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
	  

ALTER TABLE yvs_base_point_vente_depot ADD COLUMN principal boolean;
ALTER TABLE yvs_base_point_vente_depot ALTER COLUMN principal SET DEFAULT false;

ALTER TABLE yvs_com_parametre ADD COLUMN seuil_fsseur double precision;
ALTER TABLE yvs_com_parametre ALTER COLUMN seuil_fsseur SET DEFAULT 0;
ALTER TABLE yvs_com_parametre ADD COLUMN seuil_client double precision;
ALTER TABLE yvs_com_parametre ALTER COLUMN seuil_client SET DEFAULT 0;

CREATE TABLE yvs_com_parametre_achat
(
  id serial NOT NULL,
  livre_auto boolean DEFAULT false,
  etat_mouv_stock character varying DEFAULT 'VALIDE'::character varying,
  jour_anterieur integer DEFAULT 0,
  author bigint,
  agence bigint,
  CONSTRAINT yvs_com_parametre_achat_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_parametre_achat_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_parametre_achat_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_parametre_achat
  OWNER TO postgres;

CREATE TABLE yvs_com_parametre_stock
(
  id serial NOT NULL,
  valid_auto boolean DEFAULT false,
  etat_mouv_stock character varying DEFAULT 'VALIDE'::character varying,
  jour_anterieur integer DEFAULT 0,
  author bigint,
  agence bigint,
  CONSTRAINT yvs_com_parametre_stock_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_parametre_stock_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_parametre_stock_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_parametre_stock
  OWNER TO postgres;

CREATE TABLE yvs_com_parametre_vente
(
  id serial NOT NULL,
  livre_auto boolean DEFAULT false,
  etat_mouv_stock character varying DEFAULT 'VALIDE'::character varying,
  jour_anterieur integer DEFAULT 0,
  author bigint,
  agence bigint,
  CONSTRAINT yvs_com_parametre_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_parametre_vente_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_parametre_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_parametre_vente
  OWNER TO postgres;

CREATE TABLE yvs_compta_parametre
(
  id serial NOT NULL,
  taille_compte integer default 8,
  societe bigint,
  author bigint,
  CONSTRAINT yvs_compta_parametre_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_parametre_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_parametre_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_parametre
  OWNER TO postgres;
  
  
ALTER TABLE yvs_pointeuse ADD COLUMN societe bigint;
ALTER TABLE yvs_pointeuse
  ADD CONSTRAINT yvs_pointeuse_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;



