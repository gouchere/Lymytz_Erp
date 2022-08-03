-- Function: arrondi(bigint, double precision)

-- DROP FUNCTION arrondi(bigint, double precision);

CREATE OR REPLACE FUNCTION arrondi(societe_ bigint, valeur_ double precision)
  RETURNS double precision AS
$BODY$   
DECLARE 
	_unite_ INT DEFAULT 0;
	_valeur_ DOUBLE PRECISION DEFAULT 0;
	_valeur_string_ CHARACTER VARYING DEFAULT '0';
	_params_ RECORD;
BEGIN
	SELECT INTO _params_ y.* FROM yvs_compta_parametre y WHERE y.societe = societe_;
	IF(_params_.id IS NOT NULL AND _params_.id > 0)THEN
		IF(_params_.decimal_arrondi IS TRUE)THEN
			_valeur_ = round(valeur_::numeric, _params_.valeur_arrondi);
		ELSE
			valeur_ = round(valeur_::numeric);
			IF(_params_.mode_arrondi IS NOT NULL)THEN
				_valeur_string_ = cast((valeur_::numeric) AS CHARACTER VARYING);
				_unite_ = CAST((SUBSTRING(_valeur_string_, char_length(_valeur_string_), char_length(_valeur_string_))) AS INT);
				CASE _params_.mode_arrondi
					WHEN 'I' THEN 
						IF (_unite_ < _params_.multiple_arrondi) THEN
						    _valeur_ = valeur_ - _unite_;
						ELSE
						    _valeur_ = valeur_ - (_unite_ - _params_.multiple_arrondi);
						END IF;
					WHEN 'S' THEN 
						IF (_unite_ < _params_.multiple_arrondi) THEN
						    _valeur_ = valeur_ + (_params_.multiple_arrondi - _unite_);
						ELSE
						    _valeur_ = valeur_ + ((_params_.multiple_arrondi * 2) - _unite_);
						END IF;
					ELSE
						IF (_unite_ < _params_.multiple_arrondi) THEN
						    IF (_unite_ < (_params_.multiple_arrondi / 2)) THEN
							_valeur_ = valeur_ - _unite_;
						    ELSE
							_valeur_ = valeur_ + (_params_.multiple_arrondi - _unite_);
						    END IF;
						ELSE
						    IF (_unite_ < (_params_.multiple_arrondi + (_params_.multiple_arrondi / 2))) THEN
							_valeur_ = valeur_ - (_unite_ - _params_.multiple_arrondi);
						    ELSE
							_valeur_ = valeur_ + ((_params_.multiple_arrondi * 2) - _unite_);
						    END IF;
						END IF;
			        END CASE;
			ELSE
				_valeur_ = valeur_;
			END IF;
		END IF;
	ELSE
		_valeur_ = round(valeur_::numeric, 0);
	END IF;
	RETURN _valeur_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION arrondi(bigint, double precision)
  OWNER TO postgres;
