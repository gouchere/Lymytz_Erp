-- Function: action_on_facture_vente()

-- DROP FUNCTION action_on_facture_vente();

CREATE OR REPLACE FUNCTION action_on_facture_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	entete_ BIGINT;
	action_ character varying;
	total_ double precision default 0;
	encours_ double precision default 0;
	valide_ double precision default 0;
	type_ CHARACTER VARYING DEFAULT 'FV';
	statut_ CHARACTER VARYING DEFAULT 'W';
	id_ BIGINT DEFAULT '0';
	contenu_ RECORD;
	execute_ BOOLEAN DEFAULT TRUE;
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE        
	IF(action_='INSERT') THEN
		entete_ = NEW.entete_doc;
		type_ = NEW.type_doc;
		statut_ = NEW.statut;
		id_ = NEW.id;
	ELSIF (action_='UPDATE') THEN 
		entete_ = NEW.entete_doc;	
		type_ = NEW.type_doc;     
		statut_ = NEW.statut;   
		id_ = NEW.id;   
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		entete_ = OLD.entete_doc;	
		type_ = OLD.type_doc;	
		statut_ = OLD.statut;   
		id_ = OLD.id; 	 
		execute_ = FALSE;
	END IF;
	
	IF(EXEC_) THEN
		-- Mise à jour du journal de vente (statut principal)
		SELECT INTO total_ COUNT(id) FROM yvs_com_doc_ventes WHERE entete_doc = entete_ AND type_doc = 'FV';
		SELECT INTO encours_ COUNT(id) FROM yvs_com_doc_ventes WHERE entete_doc = entete_ AND type_doc = 'FV' AND statut = 'R';
		SELECT INTO valide_ COUNT(id) FROM yvs_com_doc_ventes WHERE entete_doc = entete_ AND type_doc = 'FV' AND statut = 'V';
		IF(total_ = 0)THEN
			UPDATE yvs_com_entete_doc_vente SET etat = 'W' WHERE id = entete_;
		ELSIF(encours_ > 0 OR total_ > valide_)THEN
			UPDATE yvs_com_entete_doc_vente SET etat = 'R' WHERE id = entete_;
		ELSIF(valide_ = total_)THEN
			UPDATE yvs_com_entete_doc_vente SET etat = 'V' WHERE id = entete_;
		ELSE
			UPDATE yvs_com_entete_doc_vente SET etat = 'W' WHERE id = entete_;
		END IF;
		
		-- Mise à jour du journal de vente (statut livre)
		SELECT INTO encours_ COUNT(id) FROM yvs_com_doc_ventes WHERE entete_doc = entete_ AND type_doc = 'FV' AND statut_livre = 'R';
		SELECT INTO valide_ COUNT(id) FROM yvs_com_doc_ventes WHERE entete_doc = entete_ AND type_doc = 'FV' AND statut_livre = 'L';
		IF(total_ = 0)THEN
			UPDATE yvs_com_entete_doc_vente SET statut_livre = 'W' WHERE id = entete_;
		ELSIF(encours_ > 0 OR total_ > valide_)THEN
			UPDATE yvs_com_entete_doc_vente SET statut_livre = 'R' WHERE id = entete_;
		ELSIF(valide_ = total_)THEN
			UPDATE yvs_com_entete_doc_vente SET statut_livre = 'L' WHERE id = entete_;
		ELSE
			UPDATE yvs_com_entete_doc_vente SET statut_livre = 'W' WHERE id = entete_;
		END IF;
		
		-- Mise à jour du journal de vente (statut regle)
		SELECT INTO encours_ COUNT(id) FROM yvs_com_doc_ventes WHERE entete_doc = entete_ AND type_doc = 'FV' AND statut_regle = 'R';
		SELECT INTO valide_ COUNT(id) FROM yvs_com_doc_ventes WHERE entete_doc = entete_ AND type_doc = 'FV' AND statut_regle = 'P';
		IF(total_ = 0)THEN
			UPDATE yvs_com_entete_doc_vente SET statut_regle = 'W' WHERE id = entete_;
		ELSIF(encours_ > 0 OR total_ > valide_)THEN
			UPDATE yvs_com_entete_doc_vente SET statut_regle = 'R' WHERE id = entete_;
		ELSIF(valide_ = total_)THEN
			UPDATE yvs_com_entete_doc_vente SET statut_regle = 'P' WHERE id = entete_;
		ELSE
			UPDATE yvs_com_entete_doc_vente SET statut_regle = 'W' WHERE id = entete_;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_facture_vente()
  OWNER TO postgres;
