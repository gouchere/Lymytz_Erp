ALTER TABLE yvs_synchro_listen_table DROP COLUMN groupe_table;

ALTER TABLE yvs_synchro_listen_table ADD COLUMN message character varying;

ALTER TABLE yvs_base_unite_mesure ALTER COLUMN id type bigint;
