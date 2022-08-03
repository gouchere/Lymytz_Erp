-- Function: mut_action_on_reglement_credit()

-- DROP FUNCTION mut_action_on_reglement_credit();

CREATE OR REPLACE FUNCTION mut_insert_into_operation_compte(IN id_ bigint, table_ character varying)
  RETURNS boolean AS
$BODY$    
DECLARE	
	infos_cr_ record;
	libelle_ character varying;
	mouvement_ character varying;
	nature_ character varying;
	reference_ character varying;
	line_ record;
	old_id_ bigint;
	periode_ bigint;
	caisse_ bigint;
BEGIN	
	CASE table_
		WHEN 'REGLEMENT_CREDIT' THEN
			SELECT INTO line_ * FROM yvs_mut_reglement_credit WHERE id=id_;
			SELECT INTO infos_cr_ m.id AS employe, CONCAT(e.nom, ' ', e.prenom) AS nom, cr.reference FROM yvs_mut_credit cr INNER JOIN yvs_mut_compte c ON cr.compte = c.id 
																											 INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id 
																											 INNER JOIN yvs_grh_employes e ON m.employe = e.id 
																											 WHERE cr.id = line_.credit;
			libelle_='Paiement du crédit '||infos_cr_.reference;
			mouvement_='D';
			nature_='DEPOT';
			caisse_=line_.caisse;
			reference_ =infos_cr_.reference;
			SELECT INTO periode_ p.id FROM yvs_mut_periode_exercice p WHERE line_.date_reglement BETWEEN p.date_debut AND p.date_fin;
		WHEN 'REGLEMENT_MENSUALITE' THEN
			SELECT INTO line_ rm.*, me.date_mensualite, cr.reference AS reference_credit FROM yvs_mut_reglement_mensualite rm INNER JOIN yvs_mut_mensualite me ON me.id=rm.mensualite 
																							INNER JOIN yvs_mut_echellonage ech ON ech.id=me.echellonage
																							INNER JOIN yvs_mut_credit cr ON cr.id=ech.credit
																							WHERE rm.id=id_;
			SELECT INTO infos_cr_ m.id AS employe, CONCAT(e.nom, ' ', e.prenom) AS nom FROM yvs_mut_compte c INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id 
																											 INNER JOIN yvs_grh_employes e ON m.employe = e.id 
																											 WHERE c.id = line_.compte;
			libelle_='Remboursement de la mensualité du '||line_.date_mensualite::character varying || ' CR: '||line_.reference_credit;
			mouvement_='R';
			nature_='RETRAIT';
			caisse_=line_.caisse;
			reference_=line_.code_operation;		
			SELECT INTO periode_ p.id FROM yvs_mut_periode_exercice p WHERE line_.date_reglement BETWEEN p.date_debut AND p.date_fin;
		WHEN 'REGLEMENT_INTERET' THEN
			SELECT INTO line_  ri.id,ri.date_interet date_reglement, ri.montant, ri.compte,ri.author, ri.periode, p.reference_periode FROM yvs_mut_interet ri INNER JOIN yvs_mut_periode_exercice p ON p.id=ri.periode 
																																      WHERE ri.id=id_;
			libelle_='PAIEMENT DE L''INTERET DE '|| line_.reference_periode;
			mouvement_='D';
			nature_='DEPOT';
			reference_='';		
			periode_ =line_.periode;
			caisse_=null;
		WHEN 'REGLEMENT_PRIME' THEN
			SELECT INTO line_ ri.id, ri.date_prime date_reglement, ri.montant, ri.compte,ri.author, ri.periode FROM yvs_mut_reglement_prime ri WHERE ri.id=id_;
			libelle_='PAIEMENT DE LA PRIME DE GESTION '||line_.date_reglement::character varying;
			mouvement_='D';
			nature_='DEPOT';
			reference_='';		
			periode_ =line_.periode;
			caisse_=null;
		WHEN 'CONTRIBUTION_EVENEMENT' THEN
			SELECT INTO line_ ce.id, ce.date_contribution AS date_reglement, m.societe AS societe, t.designation, ce.compte, e.caisse_event, ce.montant, ce.author FROM  yvs_mut_contribution_evenement ce INNER JOIN yvs_mut_evenement e ON e.id=ce.evenement INNER JOIN yvs_mut_type_evenement t ON t.id=e.type 
																		INNER JOIN yvs_mut_mutuelle m ON m.id=t.mutuelle WHERE ce.id=id_;					
			libelle_='RETRAIT ASSURANCE POUR L''EVENEMENT '||line_.designation;
			mouvement_='R';
			nature_='RETRAIT';
			reference_='';
			SELECT INTO periode_ p.id FROM  yvs_mut_periode_exercice p INNER JOIN yvs_base_exercice e ON p.exercice=e.id WHERE line_.date_reglement BETWEEN p.date_debut AND p.date_fin AND e.societe=line_.societe;
			caisse_=null;
	END CASE;
		SELECT INTO old_id_ id FROM yvs_mut_operation_compte WHERE table_source=table_ AND souce_reglement=id_;
		IF(old_id_ IS NULL OR old_id_<=0) THEN
			INSERT INTO yvs_mut_operation_compte(date_operation, montant, nature, compte, heure_operation, commentaire, automatique, epargne_mensuel, souce_reglement, table_source, 
											author, periode,caisse, reference_operation, date_update, date_save,sens_operation,code_operation)
										VALUES (line_.date_reglement, line_.montant, nature_, line_.compte, current_time,libelle_, true,false,id_,table_, 
											line_.author,periode_,caisse_,reference_,current_date,current_date,mouvement_, reference_);
		ELSE
			UPDATE yvs_mut_operation_compte
					   SET date_operation=line_.date_reglement, montant=line_.montant, nature=nature_, compte=line_.compte, 
						    epargne_mensuel=false, souce_reglement=line_.id, table_source=table_, caisse=caisse_, periode=periode_,
						   date_update=current_date, sens_operation=mouvement_, commentaire=libelle_, code_operation=reference_
						WHERE id = old_id_;
		END IF;
		RETURN true;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_insert_into_operation_compte(bigint, character varying)
  OWNER TO postgres;
