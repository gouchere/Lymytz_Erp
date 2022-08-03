-- Function: action_on_contenu_facture_vente()

-- DROP FUNCTION action_on_contenu_facture_vente();

CREATE OR REPLACE FUNCTION action_on_contenu_facture_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	vente_ BIGINT DEFAULT 0;
	id_ BIGINT DEFAULT 0;
	parent_ BIGINT DEFAULT 0;
	action_ character varying;
	total_ double precision default 0;
	encours_ double precision default 0;
	record_ RECORD;
	execute_ BOOLEAN DEFAULT TRUE;
	
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE        
	IF(action_='INSERT') THEN
		vente_ = NEW.doc_vente;
		id_ = NEW.id;
		parent_ = NEW.parent;
		encours_= (NEW.quantite + NEW.quantite_bonus);
	ELSIF (action_='UPDATE') THEN 
		vente_ = NEW.doc_vente;	  
		id_ = NEW.id;       
		parent_ = NEW.parent;  
		encours_= (NEW.quantite + NEW.quantite_bonus);
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		vente_ = OLD.doc_vente;	
		id_ = OLD.id;
		parent_ = OLD.parent;
		encours_= (OLD.quantite + OLD.quantite_bonus);
		execute_ = FALSE;	    	 
	END IF;

	SELECT INTO record_ * FROM yvs_com_doc_ventes WHERE id = vente_;
	IF(record_.statut = 'V')THEN	
		IF(record_.type_doc = 'BLV')THEN
			SELECT INTO encours_ COALESCE(SUM(c.quantite + c.quantite_bonus), 0) FROM yvs_com_contenu_doc_vente c WHERE c.id = parent_;
		END IF;
		if(execute_ = TRUE)THEN
			IF(record_.type_doc = 'FV')THEN
				SELECT INTO total_ COALESCE(SUM(c.quantite), 0) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id WHERE d.statut = 'V' AND parent = id_;
				IF(encours_ <= total_)THEN
					NEW.statut_livree = 'L';
				ELSIF(total_ > 0)THEN
					NEW.statut_livree = 'R';
				ELSE
					NEW.statut_livree = 'W';
				END IF;
			ELSIF(record_.type_doc = 'BLV')THEN
				SELECT INTO total_ COALESCE(SUM(c.quantite), 0) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id WHERE d.statut = 'V' AND c.parent = parent_;
				IF(encours_ <= total_)THEN
					UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'L' WHERE id = parent_;
				ELSIF(total_ > 0)THEN
					UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'R' WHERE id = parent_;
				ELSE
					UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'W' WHERE id = parent_;
				END IF;
			END IF;
		ELSIF(record_.type_doc = 'BLV')THEN
			SELECT INTO total_ COALESCE(SUM(c.quantite), 0) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id WHERE d.statut = 'V' AND c.parent = parent_;
			IF(encours_ <= total_)THEN
				UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'L' WHERE id = parent_;
			ELSIF(total_ > 0)THEN
				UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'R' WHERE id = parent_;
			ELSE
				UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'W' WHERE id = parent_;
			END IF;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_contenu_facture_vente()
  OWNER TO postgres;

  
CREATE TRIGGER action_on_contenu_facture_vente
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_com_contenu_doc_vente
  FOR EACH ROW
  EXECUTE PROCEDURE action_on_contenu_facture_vente();

  
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
	
	-- Mise à jour des contenus de vente (statut livre)
	IF(type_ = 'FV')THEN
		IF(execute_ = TRUE)THEN
			FOR contenu_ IN SELECT * FROM yvs_com_contenu_doc_vente WHERE doc_vente = id_
			LOOP
				SELECT INTO total_ COALESCE(SUM(c.quantite), 0) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id WHERE d.statut = 'V' AND parent = contenu_.id;
				IF((contenu_.quantite + contenu_.quantite_bonus) <= total_)THEN
					UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'L' WHERE id = contenu_.id;
				ELSIF(total_ > 0)THEN
					UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'R' WHERE id = contenu_.id;
				ELSE
					UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'W' WHERE id = contenu_.id;
				END IF;
			END LOOP;
		END IF;
	ELSIF (type_ = 'BLV')THEN
		FOR contenu_ IN SELECT y.* FROM yvs_com_contenu_doc_vente y INNER JOIN yvs_com_contenu_doc_vente c ON y.id = c.parent WHERE c.doc_vente = id_
		LOOP
			SELECT INTO total_ COALESCE(SUM(c.quantite), 0) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id WHERE d.statut = 'V' AND parent = contenu_.id;
			IF((contenu_.quantite + contenu_.quantite_bonus) <= total_)THEN
				UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'L' WHERE id = contenu_.id;
			ELSIF(total_ > 0)THEN
				UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'R' WHERE id = contenu_.id;
			ELSE
				UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'W' WHERE id = contenu_.id;
			END IF;
		END LOOP;
	END IF;

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
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_facture_vente()
  OWNER TO postgres;

 