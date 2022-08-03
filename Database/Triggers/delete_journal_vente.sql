/*PROTOCOLE DE SUPPRESSION DES JOURNAUX 
	
	La suppression d'un journal doit:
	
		1- Supprimer les factures
		2- Supprimer les contenus
		2- Supprimer les règlements
		3- Supprimer les coûts supplémentaires
		
		4- Supprimer la comptabilisation
		5- Supprimer les mouvements de stocks
		6- Supprimer les alertes
		7- Suppression des mouvements de caisses
		
*/		
-- Function: alter_all_table_add_date_save()

-- DROP FUNCTION alter_all_table_add_date_save();

CREATE OR REPLACE FUNCTION delete_journal_vente(id_journal character varying, societe_ bigint  )
  RETURNS boolean AS
$BODY$
DECLARE
    

BEGIN
	-- désactive l'exécution des triggers
	SELECT set_config('EXECUTE_MVT_CAISSE','false',true);
	SELECT set_config('EXECUTE_TRIGGER','false',true);
	-- ACTIVE LES DELETE CASCADE
	ALTER TABLE yvs_com_doc_ventes DROP CONSTRAINT yvs_com_doc_ventes_entete_doc_fkey;
	ALTER TABLE yvs_com_doc_ventes
	ADD CONSTRAINT yvs_com_doc_ventes_entete_doc_fkey FOREIGN KEY (entete_doc)
      REFERENCES yvs_com_entete_doc_vente (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;

	IF(id_journal IS NOT NULL AND id_journal!='') THEN
		-- effacer les pièces comptables
		 -- facture
			DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_facture_vente c INNER JOIN yvs_com_doc_ventes d ON d.id=c.facture
			WHERE p.id=c.piece AND d.entete_doc::character varying IN (select val from regexp_split_to_table(id_journal,',') val);
		--règlement
			DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_piece_vente c INNER JOIN yvs_compta_caisse_piece_vente r ON r.id=c.reglement INNER JOIN yvs_com_doc_ventes d ON d.id=r.vente
			WHERE p.id=c.piece AND d.entete_doc::character varying IN (select val from regexp_split_to_table(id_journal,',') val);
		-- effacer les factures
		DELETE FROM yvs_com_entete_doc_vente WHERE id::character varying IN (select val from regexp_split_to_table(id_journal,',') val);
	ELSE
		-- effacer les pièces comptables
		 -- facture
			DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_facture_vente c INNER JOIN yvs_com_doc_ventes d ON d.id=c.facture
			INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc INNER JOIN yvs_agences a ON a.id=e.agence
			WHERE p.id=c.piece AND a.societe=societe_;
		--règlement
			DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_piece_vente c INNER JOIN yvs_compta_caisse_piece_vente r ON r.id=c.reglement 
			INNER JOIN yvs_com_doc_ventes d ON d.id=r.vente
			INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc INNER JOIN yvs_agences a ON a.id=e.agence
			WHERE p.id=c.piece AND a.societe=societe_;
		
		DELETE FROM yvs_com_entete_doc_vente e USING yvs_agences a INNER JOIN yvs_societes s ON s.id=a.societe
		WHERE (e.agence=a.id or e.agence IS NULL) and s.id=societe_; 
	END IF;
	-- réactive les contrainte
	ALTER TABLE yvs_com_doc_ventes DROP CONSTRAINT yvs_com_doc_ventes_entete_doc_fkey;
	ALTER TABLE yvs_com_doc_ventes
	ADD CONSTRAINT yvs_com_doc_ventes_entete_doc_fkey FOREIGN KEY (entete_doc)
      REFERENCES yvs_com_entete_doc_vente (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

    return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_journal_vente(character varying, bigint)
  OWNER TO postgres;
