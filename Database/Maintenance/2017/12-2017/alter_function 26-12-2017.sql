CREATE OR REPLACE FUNCTION mut_action_reglement_credit()
  RETURNS trigger AS
$BODY$    
DECLARE
	id_credit BIGINT;
	credit_ record;
	payer_ double precision default 0;
	
	action_ character varying default '';
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		id_credit = NEW.credit;
	ELSE
		id_credit = OLD.credit;
	END IF;   
	
	SELECT INTO credit_ * FROM yvs_mut_credit WHERE id = id_credit;
	SELECT INTO payer_ SUM(COALESCE(montant, 0)) FROM yvs_mut_reglement_credit WHERE statut_piece = 'P' AND credit = id_credit;
	IF(credit_.montant <= payer_ AND credit_.etat = 'V')THEN
		UPDATE yvs_mut_credit SET statut_credit = 'P', montant_verse = payer_ WHERE id = id_credit;
	ELSE
		UPDATE yvs_mut_credit SET statut_credit = 'W', montant_verse = payer_ WHERE id = id_credit;
	END IF;
	
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_reglement_credit()
  OWNER TO postgres;
  
  
-- Function: get_taxe_achat(bigint)

-- DROP FUNCTION get_taxe_achat(bigint);

CREATE OR REPLACE FUNCTION get_taxe_achat(IN id_ bigint)
  RETURNS TABLE(taxe bigint, montant double precision) AS
$BODY$
DECLARE
	contenu_ record;
	categorie_ bigint;
	data_ record;
	ligne_ bigint;
	total_ double precision default 0;
	valeur_ double precision default 0;
	

BEGIN
	select into categorie_ categorie_comptable from yvs_com_doc_achats where id = id_;
	
	CREATE TEMP TABLE IF NOT EXISTS com_taxe_achat(taxe_ bigint, montant_ double precision);
	DELETE FROM com_taxe_achat;

	for contenu_ in select * from yvs_com_contenu_doc_achat where doc_achat = id_
	loop
		total_ = contenu_.quantite_attendu * (contenu_.pua_recu);
		for data_ in select y.id, y.taux, t.app_remise from yvs_base_taxes y inner join yvs_base_article_categorie_comptable_taxe t on y.id = t.taxe inner join yvs_base_article_categorie_comptable c on c.id = t.article_categorie where c.article = contenu_.article 
		and c.categorie = categorie_
		loop
			if(data_.app_remise)then
				valeur_ = (((total_ - contenu_.remise_recu) * data_.taux) / 100);
			else
				valeur_ = ((total_ * data_.taux) / 100);
			end if;
			select into ligne_ taxe_ from com_taxe_achat where taxe_ = data_.id;
			if(ligne_ is not null and ligne_ >0)then
				update com_taxe_achat set montant_ = montant_ + valeur_ where taxe_ = ligne_;
			else
				insert into com_taxe_achat values (data_.id, valeur_);
			end if;
		end loop;
	end loop;
	
	return QUERY SELECT * FROM com_taxe_achat order by taxe_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION get_taxe_achat(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_taxe_achat(bigint) IS 'retourne les taxes d''un document de achat';

  
ALTER TABLE yvs_com_doc_achats DROP COLUMN solde;
ALTER TABLE yvs_com_doc_achats DROP COLUMN mouv_stock;
ALTER TABLE yvs_com_doc_achats DROP COLUMN livrer;
ALTER TABLE yvs_com_doc_achats DROP COLUMN legende_type;



CREATE TABLE yvs_compta_phase_piece_achat
(
  id bigserial NOT NULL,
  piece_achat bigint,
  phase_reg bigint,
  phase_ok boolean,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_phase_piece_achat_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_phase_piece_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_phase_piece_phase_reg_fkey FOREIGN KEY (phase_reg)
      REFERENCES yvs_compta_phase_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_phase_piece_achat_piece_achat_fkey FOREIGN KEY (piece_achat)
      REFERENCES yvs_compta_caisse_piece_achat (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_phase_piece_achat
  OWNER TO postgres;


INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES 
('compta_cancel_piece_valide', 'Annuler le statut validé/Payé d''une pièce de caisse', 'Annuler le statut validé/Payé d''une pièce de caisse', (SELECT y.id FROM yvs_page_module y WHERE reference = 'compta_view_reg_vente'), 16);