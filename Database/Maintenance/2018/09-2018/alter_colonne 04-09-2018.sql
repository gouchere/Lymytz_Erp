ALTER TABLE yvs_base_groupes_article ADD COLUMN photo CHARACTER VARYING;
ALTER TABLE yvs_base_point_vente ADD COLUMN photo CHARACTER VARYING;

ALTER TABLE yvs_users ADD COLUMN vente_online BOOLEAN DEFAULT false;
ALTER TABLE yvs_grh_tranche_horaire ADD COLUMN vente_online BOOLEAN DEFAULT false;
ALTER TABLE yvs_societes ADD COLUMN vente_online BOOLEAN DEFAULT false;
ALTER TABLE yvs_base_categorie_comptable ADD COLUMN vente_online BOOLEAN DEFAULT false;
ALTER TABLE yvs_base_categorie_client ADD COLUMN vente_online BOOLEAN DEFAULT false;
ALTER TABLE yvs_base_plan_comptable ADD COLUMN vente_online BOOLEAN DEFAULT false;

ALTER TABLE yvs_com_client ADD COLUMN confirmer BOOLEAN DEFAULT true;

-- Ajouter cette option sur JVM de Glassfish ""-Dcom.sun.jersey.server.impl.cdi.lookupExtensionInBeanManager=true""

