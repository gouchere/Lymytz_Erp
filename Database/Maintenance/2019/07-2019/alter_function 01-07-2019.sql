INSERT INTO yvs_compta_content_journal_entete_facture_vente (entete, piece, author)
SELECT DISTINCT j.ref_externe, j.piece, j.author FROM yvs_compta_content_journal j WHERE j.table_externe = 'JOURNAL_VENTE';

INSERT INTO yvs_compta_content_journal_entete_bulletin (entete, piece, author)
SELECT DISTINCT j.ref_externe, j.piece, j.author FROM yvs_compta_content_journal j WHERE j.table_externe = 'ORDRE_SALAIRE';

INSERT INTO yvs_compta_content_journal_piece_mission (reglement, piece, author)
SELECT DISTINCT j.ref_externe, j.piece, j.author FROM yvs_compta_content_journal j WHERE j.table_externe = 'FRAIS_MISSION';

ALTER TABLE yvs_com_doc_ventes DISABLE TRIGGER update_;
ALTER TABLE yvs_com_doc_ventes DISABLE TRIGGER action_on_facture_vente;
UPDATE yvs_com_doc_ventes SET comptabilise = TRUE WHERE id IN (SELECT y.facture FROM yvs_compta_content_journal_facture_vente y);
ALTER TABLE yvs_com_doc_ventes ENABLE TRIGGER update_;
ALTER TABLE yvs_com_doc_ventes ENABLE TRIGGER action_on_facture_vente;

ALTER TABLE yvs_com_entete_doc_vente DISABLE TRIGGER com_update_on_entete_doc_vente;
UPDATE yvs_com_entete_doc_vente SET comptabilise = TRUE WHERE id IN (SELECT y.entete FROM yvs_compta_content_journal_entete_facture_vente y);
ALTER TABLE yvs_com_entete_doc_vente ENABLE TRIGGER com_update_on_entete_doc_vente;

ALTER TABLE yvs_com_doc_achats DISABLE TRIGGER update_;
UPDATE yvs_com_doc_achats SET comptabilise = TRUE WHERE id IN (SELECT y.facture FROM yvs_compta_content_journal_facture_achat y);
ALTER TABLE yvs_com_doc_achats ENABLE TRIGGER update_;

UPDATE yvs_compta_abonement_doc_divers SET comptabilise = TRUE WHERE id IN (SELECT y.abonnement FROM yvs_compta_content_journal_abonnement_divers y);

ALTER TABLE yvs_compta_acompte_client DISABLE TRIGGER compta_action_on_acompte_client;
UPDATE yvs_compta_acompte_client SET comptabilise = TRUE WHERE id IN (SELECT y.acompte FROM yvs_compta_content_journal_acompte_client y);
ALTER TABLE yvs_compta_acompte_client ENABLE TRIGGER compta_action_on_acompte_client;

ALTER TABLE yvs_compta_acompte_fournisseur DISABLE TRIGGER compta_action_on_acompte_fournisseur;
UPDATE yvs_compta_acompte_fournisseur SET comptabilise = TRUE WHERE id IN (SELECT y.acompte FROM yvs_compta_content_journal_acompte_fournisseur y);
ALTER TABLE yvs_compta_acompte_fournisseur ENABLE TRIGGER compta_action_on_acompte_fournisseur;

UPDATE yvs_grh_bulletins SET comptabilise = TRUE WHERE id IN (SELECT y.bulletin FROM yvs_compta_content_journal_bulletin y);

UPDATE yvs_compta_credit_client SET comptabilise = TRUE WHERE id IN (SELECT y.credit FROM yvs_compta_content_journal_credit_client y);

UPDATE yvs_compta_credit_fournisseur SET comptabilise = TRUE WHERE id IN (SELECT y.credit FROM yvs_compta_content_journal_credit_fournisseur y);

UPDATE yvs_compta_caisse_doc_divers SET comptabilise = TRUE WHERE id IN (SELECT y.divers FROM yvs_compta_content_journal_doc_divers y);

UPDATE yvs_compta_phase_acompte_achat SET comptabilise = TRUE WHERE id IN (SELECT y.etape FROM yvs_compta_content_journal_etape_acompte_achat y);

UPDATE yvs_compta_phase_acompte_vente SET comptabilise = TRUE WHERE id IN (SELECT y.etape FROM yvs_compta_content_journal_etape_acompte_vente y);

UPDATE yvs_compta_phase_piece_achat SET comptabilise = TRUE WHERE id IN (SELECT y.etape FROM yvs_compta_content_journal_etape_piece_achat y);

UPDATE yvs_compta_phase_piece_divers SET comptabilise = TRUE WHERE id IN (SELECT y.etape FROM yvs_compta_content_journal_etape_piece_divers y);

UPDATE yvs_compta_phase_piece SET comptabilise = TRUE WHERE id IN (SELECT y.etape FROM yvs_compta_content_journal_etape_piece_vente y);

UPDATE yvs_compta_phase_piece_virement SET comptabilise = TRUE WHERE id IN (SELECT y.etape FROM yvs_compta_content_journal_etape_piece_virement y);

UPDATE yvs_compta_phase_reglement_credit_client SET comptabilise = TRUE WHERE id IN (SELECT y.etape FROM yvs_compta_content_journal_etape_reglement_credit_client y);

UPDATE yvs_compta_phase_reglement_credit_fournisseur SET comptabilise = TRUE WHERE id IN (SELECT y.etape FROM yvs_compta_content_journal_etape_reglement_credit_fournisseur y);

ALTER TABLE yvs_compta_caisse_piece_achat DISABLE TRIGGER compta_action_on_piece_achat;
UPDATE yvs_compta_caisse_piece_achat SET comptabilise = TRUE WHERE id IN (SELECT y.reglement FROM yvs_compta_content_journal_piece_achat y);
ALTER TABLE yvs_compta_caisse_piece_achat ENABLE TRIGGER compta_action_on_piece_achat;

ALTER TABLE yvs_compta_caisse_piece_divers DISABLE TRIGGER compta_action_on_piece_divers;
UPDATE yvs_compta_caisse_piece_divers SET comptabilise = TRUE WHERE id IN (SELECT y.reglement FROM yvs_compta_content_journal_piece_divers y);
ALTER TABLE yvs_compta_caisse_piece_divers ENABLE TRIGGER compta_action_on_piece_divers;

ALTER TABLE yvs_compta_caisse_piece_vente DISABLE TRIGGER compta_action_on_piece_vente;
UPDATE yvs_compta_caisse_piece_vente SET comptabilise = TRUE WHERE id IN (SELECT y.reglement FROM yvs_compta_content_journal_piece_vente y);
ALTER TABLE yvs_compta_caisse_piece_vente ENABLE TRIGGER compta_action_on_piece_vente;

ALTER TABLE yvs_compta_caisse_piece_virement DISABLE TRIGGER compta_action_on_piece_virement;
UPDATE yvs_compta_caisse_piece_virement SET comptabilise = TRUE WHERE id IN (SELECT y.reglement FROM yvs_compta_content_journal_piece_virement y);
ALTER TABLE yvs_compta_caisse_piece_virement ENABLE TRIGGER compta_action_on_piece_virement;

UPDATE yvs_compta_reglement_credit_client SET comptabilise = TRUE WHERE id IN (SELECT y.reglement FROM yvs_compta_content_journal_reglement_credit_client y);

UPDATE yvs_compta_reglement_credit_fournisseur SET comptabilise = TRUE WHERE id IN (SELECT y.reglement FROM yvs_compta_content_journal_reglement_credit_fournisseur y);


-- Function: compta_action_on_content_journal_abonnement_divers()
-- DROP FUNCTION compta_action_on_content_journal_abonnement_divers();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_abonnement_divers()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_abonement_doc_divers SET comptabilise = TRUE WHERE id = NEW.abonnement;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_abonement_doc_divers SET comptabilise = FALSE WHERE id = OLD.abonnement;
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_abonnement_divers()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_abonnement_divers on yvs_compta_content_journal_abonnement_divers
-- DROP TRIGGER compta_action_on_content_journal_abonnement_divers ON yvs_compta_content_journal_abonnement_divers;
CREATE TRIGGER compta_action_on_content_journal_abonnement_divers
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_abonnement_divers
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_abonnement_divers();


-- Function: compta_action_on_content_journal_acompte_client()
-- DROP FUNCTION compta_action_on_content_journal_acompte_client();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_acompte_client()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	ALTER TABLE yvs_compta_acompte_client DISABLE TRIGGER compta_action_on_acompte_client;
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_acompte_client SET comptabilise = TRUE WHERE id = NEW.acompte;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_acompte_client SET comptabilise = FALSE WHERE id = OLD.acompte;
	END IF;
	ALTER TABLE yvs_compta_acompte_client ENABLE TRIGGER compta_action_on_acompte_client;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_acompte_client()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_acompte_client on yvs_compta_content_journal_acompte_client
-- DROP TRIGGER compta_action_on_content_journal_acompte_client ON yvs_compta_content_journal_acompte_client;
CREATE TRIGGER compta_action_on_content_journal_acompte_client
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_acompte_client
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_acompte_client();


-- Function: compta_action_on_content_journal_acompte_fournisseur()
-- DROP FUNCTION compta_action_on_content_journal_acompte_fournisseur();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_acompte_fournisseur()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	ALTER TABLE yvs_compta_acompte_fournisseur DISABLE TRIGGER compta_action_on_acompte_fournisseur;
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_acompte_fournisseur SET comptabilise = TRUE WHERE id = NEW.acompte;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_acompte_fournisseur SET comptabilise = FALSE WHERE id = OLD.acompte;
	END IF;
	ALTER TABLE yvs_compta_acompte_fournisseur ENABLE TRIGGER compta_action_on_acompte_fournisseur;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_acompte_fournisseur()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_acompte_fournisseur on yvs_compta_content_journal_acompte_fournisseur
-- DROP TRIGGER compta_action_on_content_journal_acompte_fournisseur ON yvs_compta_content_journal_acompte_fournisseur;
CREATE TRIGGER compta_action_on_content_journal_acompte_fournisseur
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_acompte_fournisseur
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_acompte_fournisseur();


-- Function: compta_action_on_content_journal_bulletin()
-- DROP FUNCTION compta_action_on_content_journal_bulletin();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_bulletin()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_grh_bulletins SET comptabilise = TRUE WHERE id = NEW.bulletin;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_grh_bulletins SET comptabilise = FALSE WHERE id = OLD.bulletin;
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_bulletin()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_bulletin on yvs_compta_content_journal_bulletin
-- DROP TRIGGER compta_action_on_content_journal_bulletin ON yvs_compta_content_journal_bulletin;
CREATE TRIGGER compta_action_on_content_journal_bulletin
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_bulletin
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_bulletin();


-- Function: compta_action_on_content_journal_credit_client()
-- DROP FUNCTION compta_action_on_content_journal_credit_client();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_credit_client()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_credit_client SET comptabilise = TRUE WHERE id = NEW.credit;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_credit_client SET comptabilise = FALSE WHERE id = OLD.credit;
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_credit_client()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_credit_client on yvs_compta_content_journal_credit_client
-- DROP TRIGGER compta_action_on_content_journal_credit_client ON yvs_compta_content_journal_credit_client;
CREATE TRIGGER compta_action_on_content_journal_credit_client
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_credit_client
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_credit_client();


-- Function: compta_action_on_content_journal_credit_fournisseur()
-- DROP FUNCTION compta_action_on_content_journal_credit_fournisseur();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_credit_fournisseur()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_credit_fournisseur SET comptabilise = TRUE WHERE id = NEW.credit;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_credit_fournisseur SET comptabilise = FALSE WHERE id = OLD.credit;
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_credit_fournisseur()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_credit_fournisseur on yvs_compta_content_journal_credit_fournisseur
-- DROP TRIGGER compta_action_on_content_journal_credit_fournisseur ON yvs_compta_content_journal_credit_fournisseur;
CREATE TRIGGER compta_action_on_content_journal_credit_fournisseur
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_credit_fournisseur
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_credit_fournisseur();


-- Function: compta_action_on_content_journal_doc_divers()
-- DROP FUNCTION compta_action_on_content_journal_doc_divers();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_doc_divers()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	ALTER TABLE yvs_compta_caisse_doc_divers DISABLE TRIGGER compta_action_on_divers;
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_caisse_doc_divers SET comptabilise = TRUE WHERE id = NEW.divers;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_caisse_doc_divers SET comptabilise = FALSE WHERE id = OLD.divers;
	END IF;
	ALTER TABLE yvs_compta_caisse_doc_divers ENABLE TRIGGER compta_action_on_divers;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_doc_divers()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_doc_divers on yvs_compta_content_journal_doc_divers
-- DROP TRIGGER compta_action_on_content_journal_doc_divers ON yvs_compta_content_journal_doc_divers;
CREATE TRIGGER compta_action_on_content_journal_doc_divers
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_doc_divers
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_doc_divers();


-- Function: compta_action_on_content_journal_entete_facture_vente()
-- DROP FUNCTION compta_action_on_content_journal_entete_facture_vente();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_entete_facture_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	ALTER TABLE yvs_com_entete_doc_vente DISABLE TRIGGER com_update_on_entete_doc_vente;
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_com_entete_doc_vente SET comptabilise = TRUE WHERE id = NEW.entete;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_com_entete_doc_vente SET comptabilise = FALSE WHERE id = OLD.entete;
	END IF;
	ALTER TABLE yvs_com_entete_doc_vente ENABLE TRIGGER com_update_on_entete_doc_vente;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_entete_facture_vente()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_entete_facture_vente on yvs_compta_content_journal_entete_facture_vente
-- DROP TRIGGER compta_action_on_content_journal_entete_facture_vente ON yvs_compta_content_journal_entete_facture_vente;
CREATE TRIGGER compta_action_on_content_journal_entete_facture_vente
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_entete_facture_vente
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_entete_facture_vente();


-- Function: compta_action_on_content_journal_etape_acompte_achat()
-- DROP FUNCTION compta_action_on_content_journal_etape_acompte_achat();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_etape_acompte_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_phase_acompte_achat SET comptabilise = TRUE WHERE id = NEW.etape;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_phase_acompte_achat SET comptabilise = FALSE WHERE id = OLD.etape;
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_etape_acompte_achat()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_etape_acompte_achat on yvs_compta_content_journal_etape_acompte_achat
-- DROP TRIGGER compta_action_on_content_journal_etape_acompte_achat ON yvs_compta_content_journal_etape_acompte_achat;
CREATE TRIGGER compta_action_on_content_journal_etape_acompte_achat
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_etape_acompte_achat
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_etape_acompte_achat();


-- Function: compta_action_on_content_journal_etape_acompte_vente()
-- DROP FUNCTION compta_action_on_content_journal_etape_acompte_vente();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_etape_acompte_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_phase_acompte_vente SET comptabilise = TRUE WHERE id = NEW.etape;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_phase_acompte_vente SET comptabilise = FALSE WHERE id = OLD.etape;
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_etape_acompte_vente()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_etape_acompte_vente on yvs_compta_content_journal_etape_acompte_vente
-- DROP TRIGGER compta_action_on_content_journal_etape_acompte_vente ON yvs_compta_content_journal_etape_acompte_vente;
CREATE TRIGGER compta_action_on_content_journal_etape_acompte_vente
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_etape_acompte_vente
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_etape_acompte_vente();


-- Function: compta_action_on_content_journal_etape_piece_achat()
-- DROP FUNCTION compta_action_on_content_journal_etape_piece_achat();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_etape_piece_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_phase_piece_achat SET comptabilise = TRUE WHERE id = NEW.etape;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_phase_piece_achat SET comptabilise = FALSE WHERE id = OLD.etape;
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_etape_piece_achat()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_etape_piece_achat on yvs_compta_content_journal_etape_piece_achat
-- DROP TRIGGER compta_action_on_content_journal_etape_piece_achat ON yvs_compta_content_journal_etape_piece_achat;
CREATE TRIGGER compta_action_on_content_journal_etape_piece_achat
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_etape_piece_achat
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_etape_piece_achat();


-- Function: compta_action_on_content_journal_etape_piece_divers()
-- DROP FUNCTION compta_action_on_content_journal_etape_piece_divers();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_etape_piece_divers()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_phase_piece_divers SET comptabilise = TRUE WHERE id = NEW.etape;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_phase_piece_divers SET comptabilise = FALSE WHERE id = OLD.etape;
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_etape_piece_divers()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_etape_piece_divers on yvs_compta_content_journal_etape_piece_divers
-- DROP TRIGGER compta_action_on_content_journal_etape_piece_divers ON yvs_compta_content_journal_etape_piece_divers;
CREATE TRIGGER compta_action_on_content_journal_etape_piece_divers
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_etape_piece_divers
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_etape_piece_divers();


-- Function: compta_action_on_content_journal_etape_piece_vente()
-- DROP FUNCTION compta_action_on_content_journal_etape_piece_vente();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_etape_piece_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_phase_piece SET comptabilise = TRUE WHERE id = NEW.etape;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_phase_piece SET comptabilise = FALSE WHERE id = OLD.etape;
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_etape_piece_vente()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_etape_piece_vente on yvs_compta_content_journal_etape_piece_vente
-- DROP TRIGGER compta_action_on_content_journal_etape_piece_vente ON yvs_compta_content_journal_etape_piece_vente;
CREATE TRIGGER compta_action_on_content_journal_etape_piece_vente
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_etape_piece_vente
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_etape_piece_vente();


-- Function: compta_action_on_content_journal_etape_piece_virement()
-- DROP FUNCTION compta_action_on_content_journal_etape_piece_virement();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_etape_piece_virement()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_phase_piece_virement SET comptabilise = TRUE WHERE id = NEW.etape;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_phase_piece_virement SET comptabilise = FALSE WHERE id = OLD.etape;
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_etape_piece_virement()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_etape_piece_virement on yvs_compta_content_journal_etape_piece_virement
-- DROP TRIGGER compta_action_on_content_journal_etape_piece_virement ON yvs_compta_content_journal_etape_piece_virement;
CREATE TRIGGER compta_action_on_content_journal_etape_piece_virement
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_etape_piece_virement
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_etape_piece_virement();


-- Function: compta_action_on_content_journal_etape_reglement_credit_client()
-- DROP FUNCTION compta_action_on_content_journal_etape_reglement_credit_client();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_etape_reglement_credit_client()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_phase_reglement_credit_client SET comptabilise = TRUE WHERE id = NEW.etape;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_phase_reglement_credit_client SET comptabilise = FALSE WHERE id = OLD.etape;
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_etape_reglement_credit_client()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_etape_reglement_credit_client on yvs_compta_content_journal_etape_reglement_credit_client
-- DROP TRIGGER compta_action_on_content_journal_etape_reglement_credit_client ON yvs_compta_content_journal_etape_reglement_credit_client;
CREATE TRIGGER compta_action_on_content_journal_etape_reglement_credit_client
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_etape_reglement_credit_client
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_etape_reglement_credit_client();


-- Function: compta_action_on_content_journal_etape_reglement_credit_fournisseur()
-- DROP FUNCTION compta_action_on_content_journal_etape_reglement_credit_fournisseur();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_etape_reglement_credit_fournisseur()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_phase_reglement_credit_fournisseur SET comptabilise = TRUE WHERE id = NEW.etape;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_phase_reglement_credit_fournisseur SET comptabilise = FALSE WHERE id = OLD.etape;
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_etape_reglement_credit_fournisseur()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_etape_reglement_credit_fournisseur on yvs_compta_content_journal_etape_reglement_credit_fournisseur
-- DROP TRIGGER compta_action_on_content_journal_etape_reglement_credit_fournisseur ON yvs_compta_content_journal_etape_reglement_credit_fournisseur;
CREATE TRIGGER compta_action_on_content_journal_etape_reglement_credit_fournisseur
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_etape_reglement_credit_fournisseur
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_etape_reglement_credit_fournisseur();


-- Function: compta_action_on_content_journal_facture_achat()
-- DROP FUNCTION compta_action_on_content_journal_facture_achat();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_facture_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	ALTER TABLE yvs_com_doc_achats DISABLE TRIGGER update_;
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_com_doc_achats SET comptabilise = TRUE WHERE id = NEW.facture;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_com_doc_achats SET comptabilise = FALSE WHERE id = OLD.facture;
	END IF;
	ALTER TABLE yvs_com_doc_achats ENABLE TRIGGER update_;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_facture_achat()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_facture_achat on yvs_compta_content_journal_facture_achat
-- DROP TRIGGER compta_action_on_content_journal_facture_achat ON yvs_compta_content_journal_facture_achat;
CREATE TRIGGER compta_action_on_content_journal_facture_achat
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_facture_achat
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_facture_achat();


-- Function: compta_action_on_content_journal_facture_vente()
-- DROP FUNCTION compta_action_on_content_journal_facture_vente();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_facture_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	ALTER TABLE yvs_com_doc_ventes DISABLE TRIGGER update_;
	ALTER TABLE yvs_com_doc_ventes DISABLE TRIGGER action_on_facture_vente;
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_com_doc_ventes SET comptabilise = TRUE WHERE id = NEW.facture;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_com_doc_ventes SET comptabilise = FALSE WHERE id = OLD.facture;
	END IF;
	ALTER TABLE yvs_com_doc_ventes ENABLE TRIGGER update_;
	ALTER TABLE yvs_com_doc_ventes ENABLE TRIGGER action_on_facture_vente;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_facture_vente()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_facture_vente on yvs_compta_content_journal_facture_vente
-- DROP TRIGGER compta_action_on_content_journal_facture_vente ON yvs_compta_content_journal_facture_vente;
CREATE TRIGGER compta_action_on_content_journal_facture_vente
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_facture_vente
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_facture_vente();


-- Function: compta_action_on_content_journal_piece_achat()
-- DROP FUNCTION compta_action_on_content_journal_piece_achat();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_piece_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	ALTER TABLE yvs_compta_caisse_piece_achat DISABLE TRIGGER compta_action_on_piece_achat;
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_caisse_piece_achat SET comptabilise = TRUE WHERE id = NEW.reglement;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_caisse_piece_achat SET comptabilise = FALSE WHERE id = OLD.reglement;
	END IF;
	ALTER TABLE yvs_compta_caisse_piece_achat ENABLE TRIGGER compta_action_on_piece_achat;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_piece_achat()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_piece_achat on yvs_compta_content_journal_piece_achat
-- DROP TRIGGER compta_action_on_content_journal_piece_achat ON yvs_compta_content_journal_piece_achat;
CREATE TRIGGER compta_action_on_content_journal_piece_achat
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_piece_achat
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_piece_achat();


-- Function: compta_action_on_content_journal_piece_mission()
-- DROP FUNCTION compta_action_on_content_journal_piece_mission();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_piece_mission()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	ALTER TABLE yvs_compta_caisse_piece_mission DISABLE TRIGGER compta_insert_new_piece_mission;
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_caisse_piece_mission SET comptabilise = TRUE WHERE id = NEW.reglement;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_caisse_piece_mission SET comptabilise = FALSE WHERE id = OLD.reglement;
	END IF;
	ALTER TABLE yvs_compta_caisse_piece_mission ENABLE TRIGGER compta_insert_new_piece_mission;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_piece_mission()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_piece_mission on yvs_compta_content_journal_piece_mission
-- DROP TRIGGER compta_action_on_content_journal_piece_mission ON yvs_compta_content_journal_piece_mission;
CREATE TRIGGER compta_action_on_content_journal_piece_mission
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_piece_mission
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_piece_mission();


-- Function: compta_action_on_content_journal_piece_divers()
-- DROP FUNCTION compta_action_on_content_journal_piece_divers();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_piece_divers()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	ALTER TABLE yvs_compta_caisse_piece_divers DISABLE TRIGGER compta_action_on_piece_divers;
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_caisse_piece_divers SET comptabilise = TRUE WHERE id = NEW.reglement;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_caisse_piece_divers SET comptabilise = FALSE WHERE id = OLD.reglement;
	END IF;
	ALTER TABLE yvs_compta_caisse_piece_divers ENABLE TRIGGER compta_action_on_piece_divers;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_piece_divers()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_piece_divers on yvs_compta_content_journal_piece_divers
-- DROP TRIGGER compta_action_on_content_journal_piece_divers ON yvs_compta_content_journal_piece_divers;
CREATE TRIGGER compta_action_on_content_journal_piece_divers
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_piece_divers
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_piece_divers();


-- Function: compta_action_on_content_journal_piece_vente()
-- DROP FUNCTION compta_action_on_content_journal_piece_vente();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_piece_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	ALTER TABLE yvs_compta_caisse_piece_vente DISABLE TRIGGER compta_action_on_piece_vente;
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_caisse_piece_vente SET comptabilise = TRUE WHERE id = NEW.reglement;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_caisse_piece_vente SET comptabilise = FALSE WHERE id = OLD.reglement;
	END IF;
	ALTER TABLE yvs_compta_caisse_piece_vente ENABLE TRIGGER compta_action_on_piece_vente;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_piece_vente()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_piece_vente on yvs_compta_content_journal_piece_vente
-- DROP TRIGGER compta_action_on_content_journal_piece_vente ON yvs_compta_content_journal_piece_vente;
CREATE TRIGGER compta_action_on_content_journal_piece_vente
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_piece_vente
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_piece_vente();


-- Function: compta_action_on_content_journal_reglement_credit_client()
-- DROP FUNCTION compta_action_on_content_journal_reglement_credit_client();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_reglement_credit_client()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	ALTER TABLE yvs_compta_reglement_credit_client DISABLE TRIGGER compta_action_on_credit_client;
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_reglement_credit_client SET comptabilise = TRUE WHERE id = NEW.reglement;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_reglement_credit_client SET comptabilise = FALSE WHERE id = OLD.reglement;
	END IF;
	ALTER TABLE yvs_compta_reglement_credit_client ENABLE TRIGGER compta_action_on_credit_client;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_reglement_credit_client()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_reglement_credit_client on yvs_compta_content_journal_reglement_credit_client
-- DROP TRIGGER compta_action_on_content_journal_reglement_credit_client ON yvs_compta_content_journal_reglement_credit_client;
CREATE TRIGGER compta_action_on_content_journal_reglement_credit_client
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_reglement_credit_client
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_reglement_credit_client();


-- Function: compta_action_on_content_journal_reglement_credit_fournisseur()
-- DROP FUNCTION compta_action_on_content_journal_reglement_credit_fournisseur();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_reglement_credit_fournisseur()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	ALTER TABLE yvs_compta_reglement_credit_fournisseur DISABLE TRIGGER compta_action_on_credit_fournisseur;
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_reglement_credit_fournisseur SET comptabilise = TRUE WHERE id = NEW.reglement;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_reglement_credit_fournisseur SET comptabilise = FALSE WHERE id = OLD.reglement;
	END IF;
	ALTER TABLE yvs_compta_reglement_credit_fournisseur ENABLE TRIGGER compta_action_on_credit_fournisseur;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_reglement_credit_fournisseur()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_reglement_credit_fournisseur on yvs_compta_content_journal_reglement_credit_fournisseur
-- DROP TRIGGER compta_action_on_content_journal_reglement_credit_fournisseur ON yvs_compta_content_journal_reglement_credit_fournisseur;
CREATE TRIGGER compta_action_on_content_journal_reglement_credit_fournisseur
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_reglement_credit_fournisseur
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_reglement_credit_fournisseur();


-- Function: compta_action_on_content_journal_piece_virement()
-- DROP FUNCTION compta_action_on_content_journal_piece_virement();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_piece_virement()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	ALTER TABLE yvs_compta_caisse_piece_virement DISABLE TRIGGER compta_action_on_piece_virement;
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_compta_caisse_piece_virement SET comptabilise = TRUE WHERE id = NEW.reglement;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_compta_caisse_piece_virement SET comptabilise = FALSE WHERE id = OLD.reglement;
	END IF;
	ALTER TABLE yvs_compta_caisse_piece_virement ENABLE TRIGGER compta_action_on_piece_virement;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_piece_virement()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_piece_virement on yvs_compta_content_journal_piece_virement
-- DROP TRIGGER compta_action_on_content_journal_piece_virement ON yvs_compta_content_journal_piece_virement;
CREATE TRIGGER compta_action_on_content_journal_piece_virement
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_piece_virement
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_piece_virement();


-- Function: compta_action_on_content_journal_entete_bulletin()
-- DROP FUNCTION compta_action_on_content_journal_entete_bulletin();
CREATE OR REPLACE FUNCTION compta_action_on_content_journal_entete_bulletin()
  RETURNS trigger AS
$BODY$    
DECLARE
	
BEGIN	
	IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
		UPDATE yvs_grh_ordre_calcul_salaire SET comptabilise = TRUE WHERE id = NEW.entete;
	ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
		UPDATE yvs_grh_ordre_calcul_salaire SET comptabilise = FALSE WHERE id = OLD.entete;
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_entete_bulletin()
  OWNER TO postgres;

-- Trigger: compta_action_on_content_journal_entete_bulletin on yvs_compta_content_journal_entete_bulletin
-- DROP TRIGGER compta_action_on_content_journal_entete_bulletin ON yvs_compta_content_journal_entete_bulletin;
CREATE TRIGGER compta_action_on_content_journal_entete_bulletin
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_entete_bulletin
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_entete_bulletin();

