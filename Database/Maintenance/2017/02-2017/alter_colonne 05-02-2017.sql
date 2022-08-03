ALTER TABLE yvs_compta_journaux DROP COLUMN default_for;
ALTER TABLE yvs_compta_journaux ADD COLUMN default_for boolean default false;