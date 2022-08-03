-- Function: equilibre_achat(bigint, date, date)

-- DROP FUNCTION equilibre_achat(bigint, date, date);

CREATE OR REPLACE FUNCTION maj_migration_data_production()
  RETURNS boolean AS
$BODY$
DECLARE
	headers_ record;
	ordres_ record;
	operations_ record;
	line_ record;
	dates_ record;
	id_session_ bigint;
	id_session_of_ bigint;
	id_op_of_ bigint;

BEGIN
	FOR headers_ IN SELECT DISTINCT s.date_flux, s.tranche, s.equipe, c.depot_conso, ua.users,ua.id AS author from yvs_prod_of_suivi_flux s inner join yvs_prod_flux_composant fx on fx.id=s.composant 
					inner join yvs_prod_composant_of c on c.id=fx.composant INNER JOIN yvs_users_agence ua on ua.id=s.author
					LOOP
						RAISE NOTICE 'SESSION-- % - % - % - %',headers_.date_flux,headers_.equipe, headers_.tranche, headers_.users;
						-- Insère la ligne de session prod pour chaque instance
						-- SELECT INTO dates_ s.date_save, s.date_update FROM yvs_prod_of_suivi_flux s WHERE s.date_flux=headers_.date_flux AND s.equipe=headers_.equipe AND s.tranche=headers_.tranche;
							  SELECT INTO id_session_ nextval('yvs_prod_session_prod_id_seq'::regclass);
							  INSERT INTO yvs_prod_session_prod(id,date_session, equipe, tranche, producteur, author, date_save,date_update, depot)
								VALUES (id_session_,headers_.date_flux,headers_.equipe, headers_.tranche, headers_.users, headers_.author, headers_.date_flux,headers_.date_flux, headers_.depot_conso);
							FOR ordres_ IN SELECT distinct c.ordre_fabrication from yvs_prod_of_suivi_flux s inner join yvs_prod_flux_composant fx on fx.id=s.composant 
																										inner join yvs_prod_composant_of c on c.id=fx.composant 
																										INNER JOIN yvs_users_agence ua on ua.id=s.author
											WHERE s.date_flux=headers_.date_flux and s.tranche=headers_.tranche and s.equipe=headers_.equipe and c.depot_conso=headers_.depot_conso and ua.users=headers_.users
								LOOP
									-- Insère la ligne session_of
									SELECT INTO id_session_of_ nextval('yvs_prod_session_of_id_seq'::regclass);
									INSERT INTO yvs_prod_session_of(id,ordre, session_prod, author, date_save, date_update)
											VALUES (id_session_of_, ordres_.ordre_fabrication, id_session_, headers_.author, headers_.date_flux,headers_.date_flux);

									FOR operations_ IN SELECT DISTINCT o.*,s.date_flux, s.tranche,s.equipe FROM yvs_prod_of_suivi_flux s INNER JOIN yvs_prod_flux_composant fx on fx.id=s.composant 
																						  INNER JOIN yvs_prod_operations_of o on o.id=fx.operation 
																  WHERE o.ordre_fabrication=ordres_.ordre_fabrication AND s.date_flux=headers_.date_flux AND s.tranche=headers_.tranche AND s.equipe=headers_.equipe
										
										-- Insère un cycle opération et 
										LOOP
											SELECT INTO id_op_of_ nextval('yvs_prod_suivi_operations_id_seq'::regclass);
											INSERT INTO yvs_prod_suivi_operations(id, session_of, operation_of, date_debut, date_fin, heure_debut, 
																					heure_fin, cout, author, date_save, date_upadate, ref_operation, statut_op)
												 VALUES (id_op_of_, id_session_of_, operations_.id, operations_.date_debut, operations_.date_fin, operations_.heure_debut, operations_.heure_fin,
																0, operations_.author, operations_.date_save, operations_.date_update, operations_.numero||'_01','T');

											FOR line_ IN SELECT s.* FROM yvs_prod_of_suivi_flux s INNER JOIN yvs_prod_flux_composant fx on fx.id=s.composant 
																							  INNER JOIN yvs_prod_operations_of o on o.id=fx.operation 
																WHERE o.ordre_fabrication=ordres_.ordre_fabrication AND s.date_flux=operations_.date_flux and s.tranche=operations_.tranche and s.equipe=operations_.equipe
																and fx.operation=operations_.id
											LOOP
															UPDATE yvs_prod_of_suivi_flux SET id_operation=id_op_of_ WHERE id=line_.id;
												-- Insère les flux 
											END LOOP;
										END LOOP;
								END LOOP;

					END LOOP;
					return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION maj_migration_data_production()
  OWNER TO postgres;
