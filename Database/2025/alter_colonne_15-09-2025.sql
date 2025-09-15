alter table yvs_stat_export_etat drop column for_exportation;
alter table yvs_stat_export_etat drop column libelle;
alter table yvs_stat_export_etat drop column table_principal;
alter table yvs_stat_export_etat drop column colonne_principal;
alter table yvs_stat_export_etat drop column order_by;

alter table yvs_stat_export_colonne drop column colonne_date;
alter table yvs_stat_export_colonne drop column format_date;

alter table yvs_stat_export_colonne add column key boolean;
alter table yvs_stat_export_colonne add column type character varying;
alter table yvs_stat_export_colonne add column jointure character varying;
alter table yvs_stat_export_colonne add column condition boolean;
alter table yvs_stat_export_colonne add column condition_expression character varying;
alter table yvs_stat_export_colonne add column condition_expression_other character varying;
alter table yvs_stat_export_colonne add column condition_operator character varying;