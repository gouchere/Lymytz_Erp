UPDATE yvs_base_point_vente SET comptabilisation_auto = COALESCE(p.comptabilisation_auto, FALSE) FROM yvs_com_parametre_vente p WHERE p.agence = yvs_base_point_vente.agence;