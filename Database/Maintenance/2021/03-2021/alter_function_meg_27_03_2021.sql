INSERT INTO yvs_stat_grh_etat(libelle, societe, code) SELECT 'RECAPITULATIF BULLETIN', s.id, 'RECAP_BUL' FROM yvs_societes s;
