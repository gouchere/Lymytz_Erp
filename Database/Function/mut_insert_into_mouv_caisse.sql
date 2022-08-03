-- Function: mut_action_on_reglement_credit()

-- DROP FUNCTION mut_action_on_reglement_credit();

CREATE OR REPLACE FUNCTION mut_insert_into_mouv_caisse(IN id_ bigint, table_ character varying)
  RETURNS boolean AS
$BODY$    
DECLARE	
	infos_cr_ record;
	libelle_ character varying;
	reference_ character varying;
	mouvement_ character varying;
	line_ record;
	old_id_ bigint;
	date_ date;
	statut_ character;
BEGIN	
	RAISE NOTICE 'NEW UPDATE MENSUALITE';
	CASE table_
		WHEN 'REGLEMENT_CREDIT' THEN
			SELECT INTO line_ * FROM yvs_mut_reglement_credit WHERE id=id_;
			SELECT INTO infos_cr_ m.id AS employe, CONCAT(e.nom, ' ', e.prenom) AS nom, cr.reference FROM yvs_mut_credit cr INNER JOIN yvs_mut_compte c ON cr.compte = c.id 
																											 INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id 
																											 INNER JOIN yvs_grh_employes e ON m.employe = e.id 
																											 WHERE cr.id = line_.credit;
			libelle_='Paiement du crédit '||infos_cr_.reference;
			mouvement_='R';
			reference_=infos_cr_.reference;
			date_=line_.date_reglement;
			statut_=line_.statut_piece;
		WHEN 'OPERATION_COMPTE' THEN
			SELECT INTO line_ * FROM yvs_mut_operation_compte WHERE id=id_;
			SELECT INTO infos_cr_ m.id AS employe, CONCAT(e.nom, ' ', e.prenom) AS nom FROM yvs_mut_compte c INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id 
																											 INNER JOIN yvs_grh_employes e ON m.employe = e.id 
																											 WHERE c.id = line_.compte;
			libelle_=line_.commentaire;
			if(line_.sens_operation='D') THEN
				mouvement_='D';	--Dépots
			ELSE 
				mouvement_='R'; --Retrait
			END IF;
			reference_=line_.reference_operation;
			date_=line_.date_operation;
			statut_='P';
		WHEN 'REGLEMENT_MENSUALITE' THEN
			SELECT INTO line_ rm.*, me.date_mensualite FROM yvs_mut_reglement_mensualite rm INNER JOIN yvs_mut_mensualite me ON me.id=rm.mensualite WHERE rm.id=id_;
			SELECT INTO infos_cr_ m.id AS employe, CONCAT(e.nom, ' ', e.prenom) AS nom FROM yvs_mut_compte c INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id 
																											 INNER JOIN yvs_grh_employes e ON m.employe = e.id 
																											 WHERE c.id = line_.compte;
			libelle_='Remboursement de la mensualité du'||line_.date_mensualite::character varying;
			mouvement_='D'; --Dépôt
			reference_='';
			date_=line_.date_reglement;
			statut_=line_.statut_piece;
		WHEN 'REGLEMENT_INTERET' THEN
			SELECT INTO line_ i.*, p.reference_periode FROM yvs_mut_interet i INNER JOIN yvs_mut_periode_exercice p ON p.id=i.periode WHERE i.id=id_;
			SELECT INTO infos_cr_ m.id AS employe, CONCAT(e.nom, ' ', e.prenom) AS nom FROM  yvs_mut_mutualiste m  INNER JOIN yvs_grh_employes e ON m.employe = e.id 
																					   WHERE m.id = line_.mutualiste;
			libelle_='PAIEMENT DE L''INTERET DE '||line_.reference_periode;
			mouvement_='D';
			reference_='';
			date_=line_.date_interet;
			statut_=line_.statut_paiement;
		WHEN 'CONTRIBUTION_EVENEMENT' THEN
			SELECT INTO line_ ce.id, ce.date_contribution AS date_reglement, s.id AS societe, t.designation, e.caisse_event AS caisse, ce.montant, ce.author, ce.date_save, ce.date_update FROM  yvs_mut_contribution_evenement ce INNER JOIN yvs_mut_evenement e ON e.id=ce.evenement INNER JOIN yvs_mut_type_evenement t ON t.id=e.type 
																		INNER JOIN yvs_mut_mutuelle m ON m.id=t.mutuelle INNER JOIN yvs_societes s ON s.id=m.societe WHERE ce.id=id_;	
			SELECT INTO infos_cr_ m.id AS employe, CONCAT(e.nom, ' ', e.prenom) AS nom, cr.reference FROM yvs_mut_contribution_evenement ce INNER JOIN yvs_mut_compte c ON ce.compte = c.id 
																											 INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id 
																											 INNER JOIN yvs_grh_employes e ON m.employe = e.id 
																											 WHERE ce.id = line_.id;
			libelle_='PAIEMENT ASSURANCE POUR L''EVENEMENT '||line_.designation;
			mouvement_='D';
			reference_='';
			statut_='W';
			date_=line_.date_reglement;
		WHEN 'EVENEMENT' THEN
			SELECT INTO line_ e.id, e.date_evenement, t.designation, SUM(ce.montant) AS montant, e.author, e.date_save, e.date_update, e.caisse_event AS caisse FROM  yvs_mut_evenement e INNER JOIN  yvs_mut_type_evenement t ON t.id=e.type INNER JOIN yvs_mut_contribution_evenement ce ON ce.evenement=e.id
																WHERE e.id=id_				
																GROUP BY t.designation, e.date_evenement, e.id,e.author, e.date_save, e.date_update, e.caisse_event;
			SELECT INTO infos_cr_ e.nom AS nom, e.id AS employe FROM yvs_grh_employes e WHERE e.id=0;
			libelle_='FINANCEMENT DE L''EVENEMENT '||line_.designation;
			mouvement_='R';
			reference_='';
			statut_='W';
			date_=line_.date_evenement;
		END CASE;
		SELECT INTO old_id_ id FROM yvs_mut_mouvement_caisse WHERE table_externe=table_ AND id_externe=id_;
		IF(old_id_ IS NULL OR old_id_<=0) THEN
			INSERT INTO yvs_mut_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, caissier, statut_piece, 
									author, caisse, mouvement, tiers_interne, tiers_externe, name_tiers, date_update, date_save,in_solde_caisse)
			VALUES (reference_, line_.id, libelle_, table_, line_.montant, reference_,date_, null,statut_, line_.author, 
									line_.caisse, mouvement_, infos_cr_.employe, null, infos_cr_.nom, line_.date_update, line_.date_save,false);
		ELSE
			UPDATE yvs_mut_mouvement_caisse SET montant = line_.montant, date_mvt = date_, caisse = line_.caisse,
						numero = reference_, reference_externe = reference_, statut_piece =statut_, note=libelle_, date_update=current_timestamp,
						tiers_interne = infos_cr_.employe, tiers_externe = null, name_tiers = infos_cr_.nom, mouvement = mouvement_ ,in_solde_caisse=false WHERE id = old_id_;
		END IF;
		RETURN true;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_insert_into_mouv_caisse(bigint, character varying)
  OWNER TO postgres;
