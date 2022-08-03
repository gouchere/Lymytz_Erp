-- Function: com_get_versement_attendu(character varying)

-- DROP FUNCTION com_get_versement_attendu(character varying);

CREATE OR REPLACE FUNCTION com_get_versement_attendu(headers_ character varying)
  RETURNS double precision AS
$BODY$
DECLARE
	ca_ DOUBLE PRECISION DEFAULT 0;
	avance_ DOUBLE PRECISION DEFAULT 0;
	result_ DOUBLE PRECISION DEFAULT 0;

	cs_m double precision default 0;
	cs_p double precision default 0;
	cs_ double precision default 0;

	head_ RECORD;
BEGIN
	-- Recupere le montant TTC du contenu de la facture
	select into ca_ sum(c.prix_total) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
	inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id
	where d.entete_doc::CHARACTER VARYING IN (SELECT head FROM regexp_split_to_table(headers_,',') head) and d.type_doc = 'FV' and d.statut = 'V'
	and d.document_lie is null;

	-- Recupere le total des couts de service supplementaire d'une facture
	select into cs_p sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id inner join yvs_com_doc_ventes d on o.doc_vente = d.id
	inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id
	where d.entete_doc::CHARACTER VARYING IN (SELECT head FROM regexp_split_to_table(headers_,',') head)
	and d.type_doc = 'FV' and d.statut = 'V' and t.augmentation is true and o.service = true and (d.document_lie is null or (d.document_lie is not null and d.statut_regle in ('R', 'P')));

	select into cs_m sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id inner join yvs_com_doc_ventes d on o.doc_vente = d.id
	inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id
	where d.entete_doc::CHARACTER VARYING IN (SELECT head FROM regexp_split_to_table(headers_,',') head)
	and d.type_doc = 'FV' and d.statut = 'V' and t.augmentation is false and o.service = true and (d.document_lie is null or (d.document_lie is not null and d.statut_regle in ('R', 'P')));

	cs_  = COALESCE(cs_p, 0) - COALESCE(cs_m, 0);
	ca_ = COALESCE(ca_, 0) + COALESCE(cs_, 0);

	FOR head_ IN SELECT DISTINCT h.users, e.date_entete FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id WHERE e.id::CHARACTER VARYING IN (SELECT head FROM regexp_split_to_table(headers_,',') head)
	LOOP
		SELECT INTO result_ SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON p.vente = d.id WHERE (d.type_doc = 'BCV' OR (d.type_doc = 'FV' AND d.document_lie IS NOT NULL)) AND p.caissier = head_.users AND p.date_paiement = head_.date_entete;
		RAISE NOTICE 'result_ : %',result_;
		avance_ = avance_ + COALESCE(result_, 0);
	END LOOP;
	RAISE NOTICE 'ca : %',ca_;
	RAISE NOTICE 'avances : %',avance_;
	result_ = COALESCE(ca_, 0) + COALESCE(avance_, 0);
	RETURN COALESCE(result_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_versement_attendu(character varying)
  OWNER TO postgres;
