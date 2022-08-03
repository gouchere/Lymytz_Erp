-- Function: get_ca_vente(bigint, bigint, character varying, character varying, character varying, bigint, bigint, bigint, bigint, bigint, date, date, character varying)

-- DROP FUNCTION get_ca_vente(bigint, bigint, character varying, character varying, character varying, bigint, bigint, bigint, bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION get_ca_vente(societe_ bigint, agence_ bigint, statut_ character varying, statut_l character varying, statut_r character varying, facture_ bigint, header_ bigint, client_ bigint, vendeur_ bigint, point_ bigint, debut_ date, fin_ date, type_resultat_ character varying)
  RETURNS double precision AS
$BODY$
DECLARE
	total_facture_ double precision default 0;
	cs_p_ double precision default 0;
	cs_m_ double precision default 0;
	avoir_ double precision default 0;

	query_total character varying ;
	query_cs_p character varying ; -- cout sup en plus
	query_cs_m character varying ;-- cout sup en moins
	query_avoir character varying ;
	query_avance_avoir character varying ;
	
	result_ character varying ;--TV=Total vente,  CA=chiffre d'affaire, SUP=Service  supp, AV = Ca avoir

BEGIN
	IF(COALESCE(statut_,'')='') THEN
		statut_='V';
	END IF;
	result_=COALESCE(type_resultat_, 'CA');
	query_total='SELECT SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c WHERE c.prix_total>0';
	query_cs_p='SELECT SUM(c.montant) FROM yvs_com_cout_sup_doc_vente c INNER JOIN yvs_grh_type_cout t ON c.type_cout = t.id  
									  WHERE t.augmentation IS TRUE AND c.service IS TRUE';
	query_cs_m='SELECT SUM(c.montant) FROM yvs_com_cout_sup_doc_vente c INNER JOIN yvs_grh_type_cout t ON c.type_cout = t.id  
									  WHERE t.augmentation IS FALSE AND c.service IS TRUE';
	query_avoir='SELECT SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente 
	WHERE d.type_doc=''FAV'' AND d.statut=''V''';
	--query_avance_avoir='SELECT SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON d.id=p.vente WHERE d.type_doc=''FAV'' AND d.statut=''V'' AND p.statut_piece=''P''';		
	IF(COALESCE(societe_,0)=0 and COALESCE(agence_,0)=0 and COALESCE(facture_,0)=0 and COALESCE(header_,0)=0 and COALESCE(client_,0)=0 and COALESCE(vendeur_,0)=0 and COALESCE(point_,0)=0 ) THEN
		return 0;
	END IF;
	IF(COALESCE(facture_,0)!=0) THEN 		
			query_total= query_total || ' AND c.doc_vente= '||facture_;
			query_cs_p= query_cs_p || ' AND c.doc_vente= '||facture_;			
			query_cs_m= query_cs_m || ' AND c.doc_vente= '||facture_;	
			query_avoir= query_avoir ||' AND d.document_lie= '||facture_;
			--query_avance_avoir= query_avance_avoir ||' AND d.document_lie= '||facture_;
	ELSE			
		IF(COALESCE(societe_,0)!=0) THEN 
			query_total= replace (query_total , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc INNER JOIN yvs_agences a ON a.id=e.agence WHERE a.societe= '||societe_ ||' AND '); 
			query_cs_p= replace (query_cs_p , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc INNER JOIN yvs_agences a ON a.id=e.agence WHERE a.societe= '||societe_  ||' AND ');
			query_cs_m= replace (query_cs_m , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc INNER JOIN yvs_agences a ON a.id=e.agence WHERE a.societe= '||societe_ ||' AND ');
			query_avoir= replace (query_avoir , 'WHERE', 'INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc INNER JOIN yvs_agences a ON a.id=e.agence WHERE a.societe= '||societe_ ||' AND ');
		ELSE 
			RETURN 0;
		END IF;		
		IF(COALESCE(agence_,0)!=0) THEN 
				query_total= query_total || ' AND a.id= '||agence_;
				query_cs_p= query_cs_p || ' AND a.id= '||agence_;
				query_cs_m= query_cs_m || ' AND a.id= '||agence_;
				query_avoir= query_avoir || ' AND a.id= '||agence_;
		END IF;
		IF(COALESCE(statut_,'')!='') THEN 
			IF((SELECT position('yvs_com_doc_ventes' IN query_total))=0) THEN
					query_total= replace (query_total , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE d.statut= '||QUOTE_LITERAL(statut_)|| ' AND ');
					query_cs_p= replace (query_cs_p , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE d.statut= '||QUOTE_LITERAL(statut_)|| ' AND ');
					query_cs_m= replace (query_cs_m , 'WHERE','INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE d.statut= '||QUOTE_LITERAL(statut_)|| ' AND ');
				ELSE 
					query_total= query_total || ' AND d.statut= '||QUOTE_LITERAL(statut_);
					query_cs_p= query_cs_p || ' AND d.statut= '||QUOTE_LITERAL(statut_);
					query_cs_m= query_cs_m || ' AND d.statut= '||QUOTE_LITERAL(statut_);
			END IF;
		END IF;
		IF(COALESCE(statut_L,'')!='') THEN 
			IF((SELECT position('yvs_com_doc_ventes' IN query_total))=0) THEN
				query_total= replace (query_total , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE d.statut_livre= '||QUOTE_LITERAL(statut_L)|| ' AND ');
				query_cs_p= replace (query_cs_p , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE d.statut_livre= '||QUOTE_LITERAL(statut_L)|| ' AND ');
				query_cs_m= replace (query_cs_m , 'WHERE','INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE d.statut_livre= '||QUOTE_LITERAL(statut_L)|| ' AND ');
			ELSE 
				query_total= query_total || ' AND d.statut_livre= '||QUOTE_LITERAL(statut_L);
				query_cs_p= query_cs_p || ' AND d.statut_livre= '||QUOTE_LITERAL(statut_L);
				query_cs_m= query_cs_m || ' AND d.statut_livre= '||QUOTE_LITERAL(statut_L);
			END IF;
		END IF;
		IF(COALESCE(statut_R,'')!='') THEN 
			IF((SELECT position('yvs_com_doc_ventes' IN query_total))=0) THEN
				query_total= replace (query_total , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE d.statut_regle= '||QUOTE_LITERAL(statut_R)|| ' AND ');
				query_cs_p= replace (query_cs_p , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE d.statut_regle= '||QUOTE_LITERAL(statut_R)|| ' AND ');
				query_cs_m= replace (query_cs_m , 'WHERE' ,'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE d.statut_regle= '||QUOTE_LITERAL(statut_R)|| ' AND ');
			ELSE 
				query_total= query_total || ' AND d.statut_regle= '||QUOTE_LITERAL(statut_R);
				query_cs_p= query_cs_p || ' AND d.statut_regle= '||QUOTE_LITERAL(statut_R);
				query_cs_m= query_cs_m || ' AND d.statut_regle= '||QUOTE_LITERAL(statut_R);
			END IF;
		END IF;
		--condition sur header
		IF(COALESCE(header_,0)!=0) THEN
			IF((SELECT position('yvs_com_doc_ventes' IN query_total))=0) THEN
				query_total= replace (query_total , 'WHERE' ,'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE ');			
				query_cs_p= replace (query_cs_p , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE ');			
				query_cs_m= replace (query_cs_m , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE ');			
			END IF;
			query_total= replace (query_total , 'WHERE', 'INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc WHERE e.id='||header_ ||' AND ');			
			query_cs_p= replace (query_cs_p , 'WHERE', 'INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc WHERE e.id='||header_ ||' AND ');			
			query_cs_m= replace (query_cs_m , 'WHERE', 'INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc WHERE e.id='||header_ ||' AND ');	
			query_avoir= query_avoir ||' AND e.id='||header_ ;	
		END IF;
		-- condition clients
		IF(COALESCE(client_,0)!=0) THEN 
			IF((SELECT position('yvs_com_doc_ventes' IN query_total))=0) THEN
				query_total= replace (query_total , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE d.client= '||client_|| ' AND ');
				query_cs_p= replace (query_cs_p , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE d.client= '||client_|| ' AND ');
				query_cs_m= replace (query_cs_m , 'WHERE' ,'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE d.client= '||client_|| ' AND ');
			ELSE 
				query_total= query_total || ' AND d.client= '|| client_;
				query_cs_p= query_cs_p || ' AND d.client= '|| client_;
				query_cs_m= query_cs_m || ' AND d.client= '|| client_;
			END IF;
			query_avoir= query_avoir || ' AND d.client= '|| client_;
		END IF;
		-- condition date
		IF(debut_ is not null and  fin_ is not null)THEN 
			IF((SELECT position('yvs_com_doc_ventes' IN query_total))=0) THEN
				query_total= replace (query_total , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE ');			
				query_cs_p= replace (query_cs_p , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE ');			
				query_cs_m= replace (query_cs_m , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE ');			
			END IF;
			IF((SELECT position('yvs_com_entete_doc_vente' IN query_total))=0) THEN
				query_total= replace (query_total , 'WHERE', 'INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc WHERE ');			
				query_cs_p= replace (query_cs_p , 'WHERE' ,'INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc WHERE ');			
				query_cs_m= replace (query_cs_m , 'WHERE', 'INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc WHERE ');			
			END IF;
			query_total= query_total || ' AND e.date_entete BETWEEN '||QUOTE_LITERAL(debut_) ||'AND'|| QUOTE_LITERAL(fin_);
			query_cs_p= query_cs_p || ' AND e.date_entete BETWEEN '||QUOTE_LITERAL(debut_) ||'AND'|| QUOTE_LITERAL(fin_);
			query_cs_m= query_cs_m || ' AND e.date_entete BETWEEN '||QUOTE_LITERAL(debut_) ||'AND'|| QUOTE_LITERAL(fin_);
			query_avoir= query_avoir || ' AND e.date_entete BETWEEN '||QUOTE_LITERAL(debut_) ||'AND'|| QUOTE_LITERAL(fin_);
		END IF;
		-- condition vendeur
		IF(COALESCE(vendeur_,0)!=0) THEN 
			IF((SELECT position('yvs_com_doc_ventes' IN query_total))=0) THEN
				query_total= replace (query_total , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE ');
				query_cs_p= replace (query_cs_p , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE ');
				query_cs_m= replace (query_cs_m , 'WHERE' ,'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE ');
			END IF; 
			IF((SELECT position('yvs_com_entete_doc_vente' IN query_total))=0) THEN
				query_total= replace (query_total , 'WHERE', 'INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc WHERE ');
				query_cs_p= replace (query_cs_p , 'WHERE', 'INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc WHERE ');
				query_cs_m= replace (query_cs_m , 'WHERE' ,'INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc WHERE ');
				query_avoir= replace (query_avoir , 'WHERE' ,'INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc WHERE ');
			END IF;
			    query_total= replace (query_total , 'WHERE', 'INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau WHERE cu.users= '||vendeur_ || ' AND ');
			    query_cs_p= replace (query_cs_p , 'WHERE', 'INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau WHERE cu.users= '||vendeur_ || ' AND ');
			    query_cs_m= replace (query_cs_m , 'WHERE', 'INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau WHERE cu.users= '||vendeur_ || ' AND ');
			    query_avoir= replace (query_avoir , 'WHERE' ,'INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau WHERE cu.users= '||vendeur_ || ' AND ');
		END IF;
		-- condition point de vente
		IF(COALESCE(point_,0)!=0) THEN 
			IF((SELECT position('yvs_com_doc_ventes' IN query_total))=0) THEN
				query_total= replace (query_total , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE ');
				query_cs_p= replace (query_cs_p , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE ');
				query_cs_m= replace (query_cs_m , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE ');
			END IF; 
			IF((SELECT position('yvs_com_entete_doc_vente' IN query_total))=0) THEN
				query_total= replace (query_total , 'WHERE', 'INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc WHERE ');
				query_cs_p= replace (query_cs_p , 'WHERE', 'INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc WHERE ');
				query_cs_m= replace (query_cs_m , 'WHERE', 'INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc WHERE ');
				query_avoir= replace (query_avoir , 'WHERE' ,'INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc WHERE ');
			END IF;
			IF((SELECT position('yvs_com_creneau_horaire_users' IN query_total))=0) THEN
				query_total= replace (query_total , 'WHERE' ,'INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau WHERE ');
				query_cs_p= replace (query_cs_p , 'WHERE', 'INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau WHERE ');
				query_cs_m= replace (query_cs_m , 'WHERE' ,'INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau WHERE ');
				query_avoir= replace (query_avoir , 'WHERE' ,'INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau WHERE ');
			END IF;
			    query_total= replace (query_total , 'WHERE', 'INNER JOIN yvs_com_creneau_point cp ON cp.id=cu.creneau_point WHERE cp.point= '||point_ || ' AND ');
			    query_cs_p= replace (query_cs_p , 'WHERE', 'INNER JOIN yvs_com_creneau_point cp ON cp.id=cu.creneau_point WHERE cp.point= '||point_ || ' AND ');
			    query_cs_m= replace (query_cs_m , 'WHERE' ,'INNER JOIN yvs_com_creneau_point cp ON cp.id=cu.creneau_point WHERE cp.point= '||point_ || ' AND ');
			    query_avoir= replace (query_avoir , 'WHERE', 'INNER JOIN yvs_com_creneau_point cp ON cp.id=cu.creneau_point WHERE cp.point= '||point_ || ' AND ');
		END IF;
	END IF;
	IF((SELECT position('yvs_com_doc_ventes' IN query_total))=0) THEN
			query_total= replace (query_total , 'WHERE' ,'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE ');
			query_cs_p= replace (query_cs_p , 'WHERE', 'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE ');
			query_cs_m= replace (query_cs_m , 'WHERE' ,'INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente WHERE ');
	END IF;
		query_total= query_total ||' AND d.type_doc=''FV''';
		query_cs_p= query_cs_p ||' AND d.type_doc=''FV''';
		query_cs_m= query_cs_m ||' AND d.type_doc=''FV''';
		
	
	EXECUTE query_total INTO total_facture_;	
	EXECUTE query_cs_p INTO cs_p_;	
	EXECUTE query_cs_m INTO cs_m_;	
	EXECUTE query_avoir INTO avoir_;	
	RAISE NOTICE 'Q1 %', query_total;
	RAISE NOTICE 'Q2 %', query_cs_p;
	RAISE NOTICE 'Q3 %', query_cs_m;
	RAISE NOTICE 'Q4 %', query_avoir;	
	RAISE NOTICE '1 %', total_facture_;
	RAISE NOTICE '2 %', cs_p_;
	RAISE NOTICE '3 %', cs_m_;
	RAISE NOTICE '4 %', avoir_;
	if(total_facture_ is null)then
		total_facture_ = 0;
	end if;
	if(cs_p_ is null)then
		cs_p_ = 0;
	end if;
	if(cs_m_ is null)then
		cs_m_ = 0;
	end if;
	if(avoir_ is null)then
		avoir_ = 0;
	end if;	
	IF(result_='CA') THEN
		IF(COALESCE(statut_,'')='') THEN
			RETURN (total_facture_ + cs_p_ -cs_m_-avoir_);
		ELSE
			RETURN (total_facture_ + cs_p_ -cs_m_);
		END IF;
	ELSIF(result_='SUP') THEN
		RETURN cs_p_ -cs_m_;
	ELSIF(result_='AV') THEN 
		RETURN avoir_;
	ELSIF(result_='TV') THEN 
		RETURN total_facture_;
		ELSE RETURN 0;
	END IF;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ca_vente(bigint, bigint, character varying, character varying, character varying, bigint, bigint, bigint, bigint, bigint, date, date, character varying)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ca_vente(bigint, bigint, character varying, character varying, character varying, bigint, bigint, bigint, bigint, bigint, date, date, character varying) IS 'retourne le chiffre d''affaire d''un doc vente';
