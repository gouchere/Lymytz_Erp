UPDATE yvs_base_famille_article SET prefixe = SUBSTRING(reference_famille, 0, 4) WHERE COALESCE(prefixe, '') = ''