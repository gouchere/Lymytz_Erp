SELECT insert_droit('base_user_change_niveau', 'Changer le niveau d''acces d''un utilisateur', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_user_'), 16, 'A,B','R');	
	
SELECT insert_droit('base_user_reinitialise_password', 'Réinitialiser le mot de passe d''un utilisateur', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_user_'), 16, 'A,B','R');