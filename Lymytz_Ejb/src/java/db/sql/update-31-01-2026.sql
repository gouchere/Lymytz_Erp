SELECT insert_droit('base_article_update_puv', 'Modifier les prix de vente d''un article',
                    (SELECT id FROM yvs_page_module WHERE reference = 'base_view_article'), null, 'A,B','R');


