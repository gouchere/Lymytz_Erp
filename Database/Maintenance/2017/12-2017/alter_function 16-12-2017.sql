-- Function: equilibre_achat(bigint)

-- DROP FUNCTION equilibre_achat(bigint);

CREATE OR REPLACE FUNCTION equilibre_achat(id_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE
	ttc_ double precision default 0;
	av_ double precision default 0;
	ch_ bigint default 0;
	contenu_ record;
	qte_ double precision default 0;
	correct boolean default true;
	encours boolean default false;
	in_ boolean default false;

BEGIN
	-- Equilibre de l'etat reglé
	ttc_ = (select get_ttc_achat(id_));
	select into av_ sum(montant) from yvs_compta_caisse_piece_achat where achat = id_ and statut_piece = 'P';
	if(av_ is null)then
		av_ = 0;
	end if;

	-- Equilibre de l'etat livré
	for contenu_ in select id, article, conditionnement as unite, quantite_recu as qte from yvs_com_contenu_doc_achat where doc_achat = id_
	loop
		in_ = true;
		select into qte_ sum(c.quantite_recu) from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on c.doc_achat = d.id 
		where c.parent = contenu_.id and c.article = contenu_.article and c.conditionnement = contenu_.unite and c.article = contenu_.article and d.type_doc = 'BLA' and d.statut = 'V';
		if(qte_ is null)then
			qte_ = 0;
		end if;
		if(qte_ < contenu_.qte)then
			correct = false;
			if(qte_ > 0)then
				encours = true;
				exit;
			end if;
		end if;
	end loop;
	-- Bonus
	for contenu_ in select id, article_bonus as article, conditionnement_bonus as unite, coalesce(quantite_bonus, 0) as qte from yvs_com_contenu_doc_achat where doc_achat = id_ and coalesce(quantite_bonus, 0) > 0
	loop
		in_ = true;
		select into qte_ sum(c.quantite_recu) from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on c.doc_achat = d.id 
		where c.parent = contenu_.id and c.article = contenu_.article and c.conditionnement = contenu_.unite and c.article = contenu_.article and d.type_doc = 'BLA' and d.statut = 'V';
		if(qte_ is null)then
			qte_ = 0;
		end if;
		if(qte_ < contenu_.qte)then
			correct = false;
			if(qte_ > 0)then
				encours = true;
				exit;
			end if;
		end if;
	end loop;
	RAISE NOTICE 'in_ %',in_;
	if(in_)then
		select into ch_ count(y.id) from yvs_compta_caisse_piece_achat y inner join yvs_base_mode_reglement m on y.model = m.id where y.achat = id_ and m.type_reglement = 'BANQUE';
		if(av_>=ttc_)then
			update yvs_com_doc_achats set statut_regle = 'P' where id = id_;
		elsif (av_ > 0 or ch_ > 0) then
			update yvs_com_doc_achats set statut_regle = 'R' where id = id_;
		else
			update yvs_com_doc_achats set statut_regle = 'W' where id = id_;
		end if;
		
		RAISE NOTICE 'correct % ',correct;
		if(correct)then
			update yvs_com_doc_achats set statut_livre = 'L' where id = id_;
		else
			RAISE NOTICE 'encours % ',encours;
			if(encours)then
				update yvs_com_doc_achats set statut_livre = 'R' where id = id_;
			else
				update yvs_com_doc_achats set statut_livre = 'W' where id = id_;
			end if;
		end if;	
	else
		update yvs_com_doc_achats set statut_regle = 'W', statut_livre = 'W' where id = id_;
	end if;	
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_achat(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_achat(bigint) IS 'equilibre l''etat reglé et l''etat livré des documents de achat';
