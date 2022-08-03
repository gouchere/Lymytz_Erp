ALTER TABLE yvs_com_parametre_achat DROP COLUMN etat_mouv_stock;
ALTER TABLE yvs_com_parametre_achat DROP COLUMN journal;
ALTER TABLE yvs_com_parametre_achat DROP COLUMN livre_auto;

ALTER TABLE yvs_com_parametre_achat ADD COLUMN comptabilisation_auto boolean default false;
ALTER TABLE yvs_com_parametre_achat ADD COLUMN comptabilisation_mode character varying default 'D';
COMMENT ON COLUMN yvs_com_parametre_achat.comptabilisation_mode IS 'D pour <par document> P pour <par periode>';

ALTER TABLE yvs_com_parametre_stock DROP COLUMN valid_auto;
ALTER TABLE yvs_com_parametre_stock DROP COLUMN etat_mouv_stock;
ALTER TABLE yvs_com_parametre_stock DROP COLUMN journal;

ALTER TABLE yvs_com_parametre_stock ADD COLUMN comptabilisation_auto boolean default false;
ALTER TABLE yvs_com_parametre_stock ADD COLUMN comptabilisation_mode character varying default 'D';
COMMENT ON COLUMN yvs_com_parametre_stock.comptabilisation_mode IS 'D pour <par document> P pour <par periode>';

ALTER TABLE yvs_com_parametre_vente DROP COLUMN livre_auto;
ALTER TABLE yvs_com_parametre_vente DROP COLUMN etat_mouv_stock;
ALTER TABLE yvs_com_parametre_vente DROP COLUMN journal;

ALTER TABLE yvs_com_parametre_vente ADD COLUMN comptabilisation_auto boolean default false;
ALTER TABLE yvs_com_parametre_vente ADD COLUMN comptabilisation_mode character varying default 'D';
COMMENT ON COLUMN yvs_com_parametre_vente.comptabilisation_mode IS 'D pour <par document> H pour <par journal> P pour <par periode>';

