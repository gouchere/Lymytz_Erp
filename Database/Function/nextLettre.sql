-- Function: nextLettre()
-- DROP FUNCTION nextLettre(bigint, bigint, character varying, date);
CREATE OR REPLACE FUNCTION nextLettre(societe_ bigint, date_ date)
  RETURNS character varying AS
$BODY$   
DECLARE	
	line_ RECORD;
	
	length_ BIGINT DEFAULT 0;
	exercice_ BIGINT DEFAULT 0;
	
	i INtegeR DEFAULT 0;
	index_ INtegeR DEFAULT 0;
	
	alphabet ChARACTER VARYING default 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	newValue ChARACTER VARYING default 'A';
	oldValue ChARACTER VARYING default 'A';
	
	ch ChARACTER default 'A';
	rs ChARACTER default 'A';

	continu_ BOOLEAN DEFAULT true;
	increment_ BOOLEAN DEFAULT false;
BEGIN
	SELECT INTO exercice_ id FROM yvs_base_exercice WHERE societe = societe_ AND date_ BETWEEN date_debut AND date_fin;
	RAISE NOTICE 'exercice_ : %',exercice_;
	IF(COALESCE(exercice_, 0)> 0)THEN
		SELECT INTO length_ MAX(LENGTH(y.lettrage)) FROM yvs_compta_content_journal y INNER JOIN yvs_compta_pieces_comptable p ON y.piece = p .id 
			INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id
			WHERE a.societe = societe_ AND y.lettrage IS NOT NULL AND p.exercice = exercice_;
		RAISE NOTICE 'length_ : %',length_;
		IF(COALESCE(length_, 0) > 0)THEN
			SELECT INTO newValue MAX(y.lettrage) FROM yvs_compta_content_journal y INNER JOIN yvs_compta_pieces_comptable p ON y.piece = p .id 
				INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id
				WHERE a.societe = societe_ AND y.lettrage IS NOT NULL AND LENGTH(y.lettrage) = length_ AND p.exercice = exercice_;
			RAISE NOTICE 'newValue : %',newValue;
			oldValue = newValue;
			length_ := LENGTH(newValue);--Recuperer la taille de la chaine entrée
			FOR i IN REVERSE length_..1 LOOP--Boucle inverse sur la taille
				IF(continu_ IS FALSE)THEN
					--;
				ELSE
					rs := 'A';
					ch := SUBSTRING(newValue, i, 1);--Recuperer le caracter a la position i
					index_ := POSITION(ch in alphabet);--Recupere l'index dans l'alphabet
					IF(index_ = LENGTH(alphabet))THEN--Verifie si on est a la dernier lettre de l'alphabet
						increment_ = true;--On precise On va revenir a la lettre A
					ELSE
						rs := SUBSTRING(alphabet, index_ + 1, 1);--Recupere la lettre suivante
						IF (increment_ IS TRUE) THEN--Verifie si on est passé a la lettre A
							continu_ = false;
						END IF;
						index_ := POSITION(rs in alphabet);--Recupere l'index dans l'alphabet
						IF(index_ != LENGTH(alphabet))THEN--Verifie si on n'est pas a la dernier lettre de l'alphabet
							continu_ = false;
						END IF;
					END IF;
					newValue := SUBSTRING(newValue, 0, i) || rs;--Change la lettre courante en sa nouvelle valeur
					IF(i < length_)THEN--Verifie si on n'est plus a la 1ere lettre a modifier
						newValue := newValue || SUBSTRING(oldValue, i + 1, LENGTH(oldValue));--Ajoute la suite des lettres a la nouvelle chaine
					END IF;
					oldValue = newValue;--Sauvegarde la nouvelle valeur
				END IF;
			END LOOP;
			increment_ = true;--Cas ou on a est passé a la lettre A pour toutes les lettres
			FOR i IN 1..LENGTH(newValue) LOOP
				IF(increment_ IS TRUE)THEN
					IF (SUBSTRING(newValue, i, 1) != 'A') THEN
					    increment_ = false;
					END IF;
				END IF;
			END LOOP;
			IF (increment_ IS TRUE) THEN--On ajoute une nouvelle lettre A
				newValue := newValue || 'A';
			END IF;
			RAISE NOTICE 'newValue : %',newValue;
		END IF;
	END IF;
	RETURN newValue;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION nextLettre(bigint, date)
  OWNER TO postgres;
