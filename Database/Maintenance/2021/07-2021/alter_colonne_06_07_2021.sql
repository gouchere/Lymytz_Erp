ALTER TABLE yvs_com_proformat_vente DROP COLUMN client;
ALTER TABLE yvs_com_proformat_vente ADD COLUMN adresse character varying;
ALTER TABLE yvs_com_proformat_vente ADD COLUMN client character varying;

ALTER TABLE yvs_com_ecart_entete_vente ADD COLUMN agence bigint;
ALTER TABLE yvs_com_ecart_entete_vente
  ADD CONSTRAINT yvs_com_ecart_entete_vente_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;