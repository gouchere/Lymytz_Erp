-- Function: equilibre_achat(bigint, boolean)
-- DROP FUNCTION equilibre_achat(bigint, boolean);
CREATE OR REPLACE FUNCTION equilibre_achat(IN id_ bigint, IN by_parent_ boolean)
  RETURNS TABLE(statut_livre character varying, statut_regle character varying) AS
$BODY$
DECLARE
	ch_ bigint default 0;
	
	data_ record;
	contenu_ record;
	
	count_ bigint default 0;
	
	ttc_ double precision default 0;
	av_ double precision default 0;
	qte_ double precision default 0;
	
	correct boolean default true;
	encours boolean default false;
	in_ boolean default false;

	statut_livre_ character varying default 'W';
	statut_regle_ character varying default 'W';
	query_control character varying;
	query_content character varying;

BEGIN
	CREATE TEMP TABLE IF NOT EXISTS table_statut_achat(_statut_livre_ character varying, _statut_regle_ character varying); 
	DELETE FROM table_statut_achat;
	SELECT INTO count_ COUNT(c.id) FROM yvs_com_contenu_doc_achat c WHERE c.doc_achat = id_;
	IF(COALESCE(count_, 0) > 0)THEN
		in_ = true;
		-- Equilibre de l'etat reglé
		SELECT INTO ch_ a.societe FROM yvs_com_doc_achats d INNER JOIN yvs_agences a ON d.agence = a.id WHERE d.id = id_;
		ttc_ = (select get_ttc_achat(id_));
		ttc_ = arrondi(ch_, ttc_);
		select into av_ sum(montant) from yvs_compta_caisse_piece_achat where achat = id_ and statut_piece = 'P';
		if(av_ is null)then
			av_ = 0;
		end if;

		-- Equilibre de l'etat livré
		query_control = 'select sum(c.quantite_recu) from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on c.doc_achat = d.id 
			where d.type_doc = ''BLA'' and d.statut = ''V'' and d.document_lie = '||id_;
		IF(by_parent_)THEN
			query_content = 'select c.id, c.conditionnement as unite, c.quantite_attendu::decimal as qte from yvs_com_contenu_doc_achat c inner join yvs_base_articles a on c.article = a.id where a.suivi_en_stock IS TRUE AND c.doc_achat = '||id_;
		ELSE
			query_content = 'select c.article as id, c.conditionnement as unite, sum(c.quantite_attendu)::decimal as qte from yvs_com_contenu_doc_achat c inner join yvs_base_articles a on c.article = a.id where a.suivi_en_stock IS TRUE AND c.doc_achat = '||id_||' group by c.article, c.conditionnement';	
		END IF;
		for contenu_ in execute query_content
		loop
			IF(by_parent_)THEN
				execute query_control || ' and c.parent = '||COALESCE(contenu_.id, 0) into qte_;
			ELSE
				execute query_control || ' and c.conditionnement = '||COALESCE(contenu_.unite, 0) into qte_;
			END IF;
			qte_ = coalesce(qte_, 0);
			if(qte_ < contenu_.qte)then
				correct = false;
				if(qte_ > 0)then
					encours = true;
					exit;
				end if;
			end if;
		end loop;
		-- Bonus
		IF(by_parent_)THEN
			query_content = 'select c.id, c.conditionnement_bonus as unite, coalesce(c.quantite_bonus, 0)::decimal as qte from yvs_com_contenu_doc_achat c inner join yvs_base_articles a on c.article = a.id where a.suivi_en_stock IS TRUE AND c.doc_achat = '||id_||' and coalesce(c.quantite_bonus, 0) > 0';
		ELSE
			query_content = 'select c.article_bonus as id, c.conditionnement_bonus as unite, sum(c.quantite_bonus)::decimal as qte from yvs_com_contenu_doc_achat c inner join yvs_base_articles a on c.article = a.id where a.suivi_en_stock IS TRUE AND c.doc_achat = '||id_||' and coalesce(c.quantite_bonus, 0) > 0 group by c.article_bonus, c.conditionnement_bonus';
		END IF;
		for contenu_ in execute query_content
		loop
			IF(by_parent_)THEN
				execute query_control || ' and c.parent = '||COALESCE(contenu_.id, 0) into qte_;
			ELSE
				execute query_control || ' and c.conditionnement = '||COALESCE(contenu_.unite, 0) into qte_;
			END IF;
			qte_ = coalesce(qte_, 0);
			if(qte_ < contenu_.qte)then
				correct = false;
				if(qte_ > 0)then
					encours = true;
					exit;
				end if;
			end if;
		end loop;
	END IF;
	RAISE NOTICE 'in_ %',in_;
	if(in_)then
		select into ch_ count(y.id) from yvs_compta_caisse_piece_achat y inner join yvs_base_mode_reglement m on y.model = m.id where y.achat = id_ and m.type_reglement = 'BANQUE';
		RAISE NOTICE 'av_ % ',av_;
		RAISE NOTICE 'ttc_ % ',ttc_;
		RAISE NOTICE 'ch_ % ',ch_;
		if(av_ > 0 and (av_+1) >= ttc_)then
			statut_regle_ = 'P';
			update yvs_com_doc_achats set statut_regle = statut_regle_ where id = id_;
		elsif (av_ > 0 or ch_ > 0) then
			statut_regle_ = 'R';
			update yvs_com_doc_achats set statut_regle = statut_regle_ where id = id_;
		else
			update yvs_com_doc_achats set statut_regle = statut_regle_ where id = id_;
		end if;
		
		RAISE NOTICE 'correct % ',correct;
		if(correct)then
			statut_livre_ = 'L';
			update yvs_com_doc_achats set statut_livre = statut_livre_ where id = id_;
		else
			RAISE NOTICE 'encours % ',encours;
			if(encours)then
				statut_livre_ = 'R';
				update yvs_com_doc_achats set statut_livre = statut_livre_ where id = id_;
			elsif(by_parent_)then
				update yvs_com_doc_achats set statut_livre = statut_livre_ where id = id_;
			end if;
			IF(by_parent_)then
				SELECT INTO data_ * FROM equilibre_achat(id_, false);
				statut_livre_ = data_.statut_livre;
				statut_regle_ = data_.statut_regle;
			end if;
		end if;	
	else
		update yvs_com_doc_achats set statut_regle = statut_regle_, statut_livre = statut_livre_ where id = id_;
	end if;	
	DELETE FROM table_statut_achat;
	INSERT INTO table_statut_achat VALUES(statut_livre_, statut_regle_);
	update yvs_workflow_valid_facture_achat set date_update = date_update where facture_achat = id_;
	RETURN QUERY SELECT * FROM table_statut_achat;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION equilibre_achat(bigint, boolean)
  OWNER TO postgres;
