-- Function: equilibre_vente_regle(bigint, boolean)
DROP FUNCTION equilibre_vente_regle(bigint, boolean);
CREATE OR REPLACE FUNCTION equilibre_vente_regle(id_ bigint, by_parent_ boolean)
  RETURNS character varying AS
$BODY$
DECLARE
	ch_ bigint default 0;
	
	line_ record;
	contenu_ record;
	
	ttc_ double precision default 0;
	av_ double precision default 0;
	qte_ double precision default 0;
	
	correct boolean default true;
	encours boolean default false;
	in_ boolean default false;

	statut_ character varying default 'W';
	query_control character varying;
	query_content character varying;

BEGIN
	-- Equilibre de l'etat reglé
	SELECT INTO line_ a.societe, d.type_doc FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
	INNER JOIN yvs_com_creneau_horaire_users u ON e.creneau = u.id INNER JOIN yvs_users s on s.id = u.users INNER JOIN yvs_agences a ON s.agence = a.id WHERE d.id = id_;
	IF(line_.type_doc='FV' OR line_.type_doc='BCV') THEN
		ttc_ = (select get_ttc_vente(id_));
		ttc_ = arrondi(line_.societe, ttc_);
		SELECT INTO av_ SUM(coalesce(montant,0)) FROM yvs_compta_caisse_piece_vente WHERE vente = id_ AND statut_piece = 'P';
		IF(av_ IS NULL)THEN
			av_ = 0;
		END IF;
		av_ = arrondi(line_.societe, av_);
		if(coalesce(ttc_, 0) > 0)then
			select into ch_ count(y.id) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_base_mode_reglement m on y.model = m.id 
				WHERE y.vente = id_ and m.type_reglement = 'BANQUE';
			if(av_>=ttc_)then
				statut_ = 'P';
				update yvs_com_doc_ventes set statut_regle = statut_ where id = id_;
			elsif (av_ > 0 or ch_ > 0) then
				statut_ = 'R';
				update yvs_com_doc_ventes set statut_regle = statut_ where id = id_;
			else
				update yvs_com_doc_ventes set statut_regle = statut_ where id = id_;
			end if;
		else
			update yvs_com_doc_ventes set statut_regle = statut_ where id = id_;
		end if;	
		update yvs_workflow_valid_facture_vente set date_update = date_update where facture_vente = id_;
	END IF;
	return statut_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_vente_regle(bigint, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_vente_regle(bigint, boolean) IS 'equilibre l''etat reglé et l''etat livré des documents de vente';
