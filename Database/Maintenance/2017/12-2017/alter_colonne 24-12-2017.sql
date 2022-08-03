
ALTER TABLE yvs_compta_bielletage ADD COLUMN billet boolean;
ALTER TABLE yvs_compta_bielletage ALTER COLUMN billet SET DEFAULT false;