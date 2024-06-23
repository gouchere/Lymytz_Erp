DROP FUNCTION public.com_et_valorise_inventaire(int8, int8, varchar, varchar, varchar, varchar, varchar, float8, date, date, bool);

CREATE OR REPLACE FUNCTION public.com_et_valorise_inventaire(societe_ bigint, depot_ bigint, editeur_ character varying, valorise_ms_ character varying, valorise_pf_ character varying, valorise_psf_ character varying, valorise_mp_ character varying, coefficient_ double precision, date_debut_ date, date_fin_ date, valorise_excedent_ boolean)
 RETURNS TABLE(users bigint, code character varying, nom character varying, article bigint, refart character varying, designation character varying, categorie character varying, reffam character varying, famille character varying, unite bigint, reference character varying, excedent double precision, manquant double precision, quantite double precision, prix double precision, total_excedent double precision, total_manquant double precision, total double precision)
 LANGUAGE plpgsql
AS $function$
declare 

   users_ RECORD;
   data_ RECORD;
   prec_ RECORD;

   prix_ DOUBLE PRECISION DEFAULT 0;
   total_ DOUBLE PRECISION DEFAULT 0;
   total_excedent_ DOUBLE PRECISION DEFAULT 0;
   total_manquant_ DOUBLE PRECISION DEFAULT 0;
   excedent_ DOUBLE PRECISION DEFAULT 0;
   manquant_ DOUBLE PRECISION DEFAULT 0;
   quantite_ DOUBLE PRECISION DEFAULT 0;
  
   update_ BOOLEAN default false;

   query_ CHARACTER VARYING DEFAULT 'SELECT DISTINCT u.id, u.code_users, u.nom_users FROM yvs_users u INNER JOIN yvs_agences a ON u.agence = a.id
										INNER JOIN yvs_com_doc_stocks i ON i.editeur = u.id
									 	WHERE a.societe = '||societe_||' AND i.type_doc = ''IN'' AND i.date_doc BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
   
begin 	
	if((editeur_ is not NULL and TRIM(editeur_) != '') and (coalesce(depot_, 0) > 0))then
		query_ = query_ || ' AND u.id = -1';
	else 
		IF(coalesce(depot_, 0) > 0)then
			query_ = query_ || ' AND (i.source = '||depot_||' OR i.destination = '||depot_||')';
		end if;
		IF(editeur_ is not NULL and TRIM(editeur_) != '')then
			query_ = query_ || ' AND u.id::character varying in (select val from regexp_split_to_table('||QUOTE_LITERAL(editeur_)||','','') val)';
		ELSE
			query_ = query_ || ' UNION SELECT -1 as id, ''AUCUN'' as code_users, ''AUCUN'' as nom_users';
		end if;
	end if;
	DROP TABLE IF EXISTS table_et_valorise_inventaire;
	CREATE TEMP TABLE IF NOT EXISTS table_et_valorise_inventaire(_users_ bigint, _code_ character varying, _nom_ character varying, _article_ bigint, _refart_ character varying, _designation_ character varying, _categorie_ character varying, _reffam_ character varying, _famille_ character varying, _unite_ bigint, _reference_ character varying, _excedent_ double precision, _manquant_ double precision, _quantite_ double precision, _prix_ double precision, _total_excedent_ double precision, _total_manquant_ double precision, _total_ double precision);
	DELETE FROM table_et_valorise_inventaire;
	for users_ in EXECUTE query_ 
	loop
		FOR data_ IN SELECT a.id, a.ref_art, a.designation, c.id as conditionnement, c.prix, c.prix_achat, u.reference, 
			SUM(y.quantite) as quantite, a.categorie, d.type_doc, f.reference_famille, f.designation as famille from yvs_com_contenu_doc_stock y 
			inner join yvs_base_articles a on y.article = a.id inner join yvs_base_famille_article f on a.famille = f.id
			inner join yvs_base_conditionnement c on y.conditionnement = c.id inner join yvs_base_unite_mesure u on c.unite = u.id 
			inner join yvs_com_doc_stocks d on y.doc_stock = d.id inner join yvs_com_doc_stocks i on d.document_lie = i.id
			where i.type_doc = 'IN' and d.type_doc in ('ES', 'SS') and d.date_doc between date_debut_ and date_fin_
			and ((coalesce(depot_, 0) > 0 and (d.source = depot_ or d.destination = depot_)) or (coalesce(depot_, 0) < 1 and d.id is not null))
			and ((users_.id != -1 and i.editeur = users_.id) or (users_.id = -1 and i.editeur is null))
			group by d.type_doc, f.id, a.id, c.id, u.id
			order by a.id, d.type_doc DESC
		loop
			excedent_ = 0;
			manquant_ = 0;
			quantite_ = 0;
			update_ = false;
                        select into prec_ _quantite_, _prix_ from table_et_valorise_inventaire where _users_ = users_.id and _unite_ = data_.conditionnement;
                        if(prec_ is not null and coalesce(prec_._quantite_, 0) != 0)then
                                update_ = true;
                        end if;
			IF(data_.type_doc = 'SS')then 
				manquant_ = coalesce(data_.quantite, 0) + coalesce(prec_._quantite_, 0);
                                quantite_ = manquant_;
			else
				excedent_ = coalesce(data_.quantite, 0) + coalesce(prec_._quantite_, 0);
				if(coalesce(valorise_excedent_, false) = true)then
					quantite_ = coalesce(prec_._quantite_, 0) - coalesce(excedent_, 0);
				end if;
			end if;
			if(manquant_ > 0 OR excedent_ > 0)then	
				if(update_ = false)then
					if(data_.categorie = 'FOURNITURE' or data_.categorie = 'EMBALLAGE' or data_.categorie = 'MATIERE PREMIERE' or data_.categorie = 'MP')then
						if(valorise_mp_ = 'PUV')then
							prix_ = data_.prix;
						elsif(valorise_mp_ = 'PUA')then
							prix_ = data_.prix_achat;
						else
							prix_ = COALESCE((SELECT get_pr(agence_ , data_.id, depot_, 0, dates_.date_debut, data_.conditionnement, 0)), 0);
						end if;
					elsif(data_.categorie = 'PRODUIT SEMI FINI' or data_.categorie = 'PSF')then
						if(valorise_psf_ = 'PUV')then
							prix_ = data_.prix;
						elsif(valorise_psf_ = 'PUA')then
							prix_ = data_.prix_achat;
						else
							prix_ = COALESCE((SELECT get_pr(agence_ , data_.id, depot_, 0, dates_.date_debut, data_.conditionnement, 0)), 0);
						end if;
					elsif(data_.categorie = 'PRODUIT FINI' or data_.categorie = 'PF')then
						if(valorise_pf_ = 'PUV')then
							prix_ = data_.prix;
						elsif(valorise_pf_ = 'PUA')then
							prix_ = data_.prix_achat;
						else
							prix_ = COALESCE((SELECT get_pr(agence_ , data_.id, depot_, 0, dates_.date_debut, data_.conditionnement, 0)), 0);
						end if;
					else
						if(valorise_ms_ = 'PUV')then
							prix_ = data_.prix;
						elsif(valorise_ms_ = 'PUA')then
							prix_ = data_.prix_achat;
						else
							prix_ = COALESCE((SELECT get_pr(agence_ , data_.id, depot_, 0, dates_.date_debut, data_.conditionnement, 0)), 0);
						end if;
					end if;
					total_ = quantite_ * prix_ * COALESCE(coefficient_, 1);
					total_excedent_ = excedent_ * prix_ * COALESCE(coefficient_, 1);
					total_manquant_ = manquant_ * prix_ * COALESCE(coefficient_, 1);
					insert into table_et_valorise_inventaire values (users_.id, users_.code_users, users_.nom_users, 
						data_.id, TRIM(data_.ref_art), TRIM(data_.designation), data_.categorie, data_.reference_famille, data_.famille, 
						data_.conditionnement, data_.reference, excedent_, manquant_, quantite_, prix_, total_excedent_, total_manquant_, total_);
				else
					total_ = quantite_ * coalesce(prec_._prix_, 0) * COALESCE(coefficient_, 1);
					total_excedent_ = excedent_ * prix_ * COALESCE(coefficient_, 1);
					update table_et_valorise_inventaire set _excedent_ = excedent_, _quantite_ = quantite_, _total_excedent_ = total_excedent_, _total_ = total_
                                             where _users_ = users_.id and _unite_ = data_.conditionnement;
				end if;
			end if;
		END LOOP;
	end loop;
	RETURN QUERY SELECT * FROM table_et_valorise_inventaire ORDER BY _nom_, _designation_;
END;
$function$
;
