/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.services.bases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Options;
import yvs.dao.salaire.service.GenericService;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsResourcePageGroup;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
public class ServiceAutorisation extends GenericService implements Serializable {

    public ServiceAutorisation(DaoInterfaceLocal dao, YvsNiveauAcces niveau, YvsUsersAgence user, YvsSocietes societe) {
        this.dao = dao;
        this.niveau = niveau;
        this.currentUser = user;
        this.currentScte = societe;
    }

    public List<Autorisations> loadAutorisation() {
        return loadAutorisation(niveau);
    }

    public List<Autorisations> loadAutorisation(YvsNiveauAcces niveau) {
        List<Autorisations> autorisationses = new ArrayList<>();
        try {
            if (currentScte == null) {
                if (niveau != null ? niveau.getId() > 0 ? niveau.getSociete() != null ? niveau.getSociete().getId() < 1 : true : false : false) {
                    niveau = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauAcces.findById", new String[]{"id"}, new Object[]{niveau.getId()});
                }
                currentScte = niveau != null ? niveau.getSociete() : new YvsSocietes();
            }
//            String query = "SELECT y.id, y.reference, y.libelle, y.description, coalesce((select a.acces from yvs_autorisation_module a where a.module = y.id and a.niveau_acces = ? limit 1),false) acces from yvs_module y inner join yvs_module_active w on w.module = y.id where w.actif is true and w.societe = ?";
            String query = "SELECT y.id, y.reference, y.libelle, y.description, coalesce(a.acces, false) as acces, a.date_update, ua.id, u.nom_users "
                    + "FROM yvs_module y inner join yvs_module_active w on w.module = y.id left join yvs_autorisation_module a on (y.id = a.module and a.niveau_acces = ?) left join yvs_users_agence ua on a.author = ua.id left join yvs_users u on ua.users = u.id where w.actif is true and w.societe = ?";
            List<Object> list = dao.loadListBySqlQuery(query, new Options[]{new Options(niveau.getId(), 1), new Options(currentScte.getId(), 2)});
            if (list != null) {
                Object[] tab;
                Date dateUpdate;
                for (Object o : list) {
                    tab = (Object[]) o;
                    dateUpdate = tab[5] != null ? new Date(((java.sql.Timestamp) tab[5]).getTime()) : null;
                    autorisationses.add(new Autorisations((Integer) tab[0], (String) tab[1], (String) tab[2], (String) tab[3], (Boolean) tab[4], "YvsModule", "", dateUpdate, new YvsUsersAgence((Long) tab[6], new YvsUsers(0L, (String) tab[7]))));
                }
            }
//            query = "SELECT y.id, y.reference, y.libelle, y.description, coalesce((select a.acces from yvs_autorisation_page_module a where a.page_module = y.id and a.niveau_acces = ? limit 1), false) acces, m.id, m.reference, m.libelle, m.description from yvs_page_module y inner join yvs_module m on y.module = m.id inner join yvs_module_active w on w.module = m.id where w.actif is true and w.societe = ?";
            query = "SELECT y.id, y.reference, y.libelle, y.description, coalesce(a.acces, false) as acces, m.id, m.reference, m.libelle, m.description, a.date_update, ua.id, u.nom_users "
                    + "FROM yvs_page_module y inner join yvs_module m on y.module = m.id inner join yvs_module_active w on w.module = m.id left join yvs_autorisation_page_module a on (y.id = a.page_module and a.niveau_acces = ?) left join yvs_users_agence ua on a.author = ua.id left join yvs_users u on ua.users = u.id where w.actif is true and w.societe = ?";
            list = dao.loadListBySqlQuery(query, new Options[]{new Options(niveau.getId(), 1), new Options(currentScte.getId(), 2)});
            if (list != null) {
                Object[] tab;
                Date dateUpdate;
                Autorisations module;
                for (Object o : list) {
                    tab = (Object[]) o;
                    dateUpdate = tab[9] != null ? new Date(((java.sql.Timestamp) tab[9]).getTime()) : null;
                    module = new Autorisations((Integer) tab[5], (String) tab[6], (String) tab[7], (String) tab[8], false, "YvsModule", "", null, null);
                    autorisationses.add(new Autorisations((Integer) tab[0], (String) tab[1], (String) tab[2], (String) tab[3], (Boolean) tab[4], "YvsPageModule", module.getDesignation(), module, dateUpdate, new YvsUsersAgence((Long) tab[10], new YvsUsers(0L, (String) tab[11]))));
                }
            }
//            query = "SELECT y.id, y.reference_ressource, y.libelle, y.description, coalesce((select a.acces from yvs_autorisation_ressources_page a where a.ressource_page = y.id and a.niveau_acces = ? limit 1), false) acces, p.id, p.reference, p.libelle, p.description, m.id, m.reference, m.libelle, m.description from yvs_ressources_page y inner join yvs_page_module p on y.page_module = p.id inner join yvs_module m on p.module = m.id inner join yvs_module_active w on w.module = m.id where w.actif is true and w.societe = ?";
            query = "SELECT y.id, y.reference_ressource, y.libelle, y.description, coalesce(a.acces, false) as acces, p.id, p.reference, p.libelle, p.description, m.id, m.reference, m.libelle, m.description, a.date_update, ua.id, u.nom_users, gp.id, y.date_save "
                    + "FROM yvs_ressources_page y inner join yvs_page_module p on y.page_module = p.id "
                    + "INNER JOIN yvs_module m on p.module = m.id "
                    + "INNER JOIN yvs_module_active w on w.module = m.id "
                    + "LEFT JOIN yvs_autorisation_ressources_page a on (y.id = a.ressource_page and a.niveau_acces = ?) "
                    + "LEFT JOIN yvs_users_agence ua on a.author = ua.id "
                    + "LEFT JOIN yvs_users u on ua.users = u.id "
                    + "LEFT JOIN yvs_resource_page_group gp on gp.id = y.groupe "
                    + "WHERE w.actif is true and w.societe = ? ORDER BY y.groupe ASC";
            list = dao.loadListBySqlQuery(query, new Options[]{new Options(niveau.getId(), 1), new Options(currentScte.getId(), 2)});
            if (list != null) {
                Object[] tab;
                Date dateUpdate;
                Autorisations page;
                Autorisations module;
                YvsResourcePageGroup grpe = new YvsResourcePageGroup();
                Autorisations line;
                for (Object o : list) {
                    tab = (Object[]) o;
                    dateUpdate = tab[13] != null ? new Date(((java.sql.Timestamp) tab[13]).getTime()) : null;
                    module = new Autorisations((Integer) tab[9], (String) tab[10], (String) tab[11], (String) tab[12], false, "YvsModule", "", null, null);
                    page = new Autorisations((Integer) tab[5], (String) tab[6], (String) tab[7], (String) tab[8], false, "YvsPageModule", module.getDesignation(), module, null, null);
                    if (tab[16] != null) {
                        grpe = new YvsResourcePageGroup((Long) tab[16]);
                    }
                    line = new Autorisations((Integer) tab[0], (String) tab[1], (String) tab[2], (String) tab[3], (Boolean) tab[4], "YvsRessourcesPage", page.getParent() + " --> " + page.getDesignation(), page, module, dateUpdate, new YvsUsersAgence((Long) tab[14], new YvsUsers(0L, (String) tab[15])));
                    line.setGroupe(grpe);
                    if (tab[17] != null) {
                        line.setDateSave((Date)tab[17]);
                    }
                    autorisationses.add(line);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(ServiceAutorisation.class.getName()).log(Level.SEVERE, null, e);
        }
        return autorisationses;
    }
}
