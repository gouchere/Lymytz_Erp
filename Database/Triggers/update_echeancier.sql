-- Function: update_echeancier()

-- DROP FUNCTION update_echeancier();

CREATE OR REPLACE FUNCTION update_echeancier()
  RETURNS trigger AS
$BODY$      
DECLARE
	etat varchar default null;
	credit_ record;
	montant_ double precision default 0;
	verse_ double precision default 0;
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN
	IF(EXEC_) THEN
		select into credit_ montant from yvs_mut_credit where id = OLD.credit;
		SELECT INTO verse_ COALESCE(SUM(r.montant), 0) FROM yvs_mut_reglement_mensualite r INNER JOIN yvs_mut_mensualite m ON r.mensualite = m.id INNER JOIN yvs_mut_echellonage e ON m.echellonage = e.id WHERE r.montant IS NOT NULL AND e.credit = OLD.credit AND r.statut_piece = 'P';
		IF (credit_.montant = verse_) THEN
			NEW.etat = 'P';
			update yvs_mut_credit set statut_credit = 'P' where id = OLD.credit;
		end if;
	END IF;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_echeancier()
  OWNER TO postgres;
