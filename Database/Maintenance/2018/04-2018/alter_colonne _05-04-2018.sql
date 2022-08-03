select alter_action_colonne_key('yvs_base_fournisseur', false, false);
delete from yvs_base_article_fournisseur where fournisseur in (select id from yvs_base_fournisseur where nom is null or nom in (' ', ''));
delete from yvs_base_operation_compte_fsseur where fournisseur in (select id from yvs_base_fournisseur where nom is null or nom in (' ', ''));
delete from yvs_base_tranche_reglement_fournisseur where plan in (select id from yvs_base_plan_reglement_fournisseur where fournisseur in (select id from yvs_base_fournisseur where nom is null or nom in (' ', '')));
delete from yvs_base_plan_reglement_fournisseur where fournisseur in (select id from yvs_base_fournisseur where nom is null or nom in (' ', ''));
delete from yvs_base_fournisseur where nom is null or nom in (' ', '');

select alter_action_colonne_key('yvs_base_articles', false, false);
delete from yvs_base_article_categorie_comptable where article in (select id from yvs_base_articles where ref_art is null or ref_art in (' ', ''));
delete from yvs_base_articles where ref_art is null or ref_art in (' ', '');

update yvs_compta_phase_reglement set for_emission = false where for_emission is null;

ALTER TABLE yvs_societes ADD COLUMN mode_chargement character varying;
ALTER TABLE yvs_societes ALTER COLUMN mode_chargement SET DEFAULT 'PRETTY'::character varying;

ALTER TABLE yvs_compta_parametre DROP COLUMN converter;
ALTER TABLE yvs_compta_parametre ADD COLUMN converter integer default 0;
ALTER TABLE yvs_com_parametre DROP COLUMN converter;
ALTER TABLE yvs_com_parametre ADD COLUMN converter integer default 0;

CREATE TABLE yvs_prod_parametre
(
  id serial NOT NULL,
  converter integer default 0,
  societe bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_prod_parametre_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_parametre_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_parametre_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_parametre
  OWNER TO postgres;