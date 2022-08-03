SELECT insert_droit('base_view_emplacement', 'Voir la page des emplacements ', 
	(SELECT id FROM yvs_module WHERE reference = 'base_'), null, 'A,B,J,F,D','P');

ALTER TABLE yvs_base_famille_article ALTER COLUMN id TYPE bigint