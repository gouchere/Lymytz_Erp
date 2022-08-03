
ALTER TABLE yvs_base_mode_reglement_informations ADD COLUMN channel_user_msisdn character varying;
ALTER TABLE yvs_base_mode_reglement_informations ADD COLUMN channel_user_pin character varying;

ALTER TABLE yvs_base_caisse ADD COLUMN vente_online boolean;

ALTER TABLE yvs_warning_model_doc ADD COLUMN description boolean;