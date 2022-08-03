-- DROP TRIGGER connection_update ON yvs_connection;
CREATE TRIGGER connection_update
  AFTER UPDATE
  ON yvs_connection
  FOR EACH ROW
  WHEN (COALESCE(new.id_session, '') <> COALESCE(old.id_session, ''))
  EXECUTE PROCEDURE connection_insert();
  
  
-- Sequence: yvs_connection_historique_id_seq
-- DROP SEQUENCE yvs_connection_historique_id_seq;
CREATE SEQUENCE yvs_connection_historique_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 21288
  CACHE 1;
ALTER TABLE yvs_connection_historique_id_seq
  OWNER TO postgres;

  
SELECT setval('public.yvs_connection_historique_id_seq', (SELECT MAX(id) FROM yvs_connection_historique), true);
ALTER TABLE yvs_connection_historique ALTER COLUMN id SET DEFAULT nextval('yvs_connection_historique_id_seq'::regclass);

DELETE FROM yvs_connection_historique WHERE agence IS NULL OR users IS NULL

