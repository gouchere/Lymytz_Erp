/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.salaire.service;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
public abstract class GenericService {
    
    public DaoInterfaceLocal dao;
    public YvsNiveauAcces niveau;
    public YvsUsersAgence currentUser;
    public YvsSocietes currentScte;
    public ResultatAction result = new ResultatAction();
    
    public final Logger log = Logger.getLogger(getClass().getName());
    
    public boolean autoriser(String ressource) {
        return dao.autoriser(ressource, niveau);
    }
    
    public void getException(String message, Throwable ex) {
        log.log(Level.SEVERE, message, ex);
    }
    
    public String controleStock(long article, long conditionnement, long depot, long tranche, double newQte, double oldQte, String action, String mouvement, Date date) {
        
        String queryS = "SELECT jour::date FROM generate_series(?, current_date, interval '1 day') as jour "
                + "WHERE (get_stock(?, ?, ?, 0, 0, jour::date, ?)- ?) < 0 limit 1";
        Date re;
        switch (action) {
            case "INSERT":
                switch (mouvement) {
                    case "E":
                        //pas de contrôle necessaire
                        return null;
                    case "S":
                        re = (Date) dao.loadObjectBySqlQuery(queryS, new yvs.dao.Options[]{new yvs.dao.Options(date, 1), new yvs.dao.Options(article, 2),
                            new yvs.dao.Options(tranche, 3), new yvs.dao.Options(depot, 4),
                            new yvs.dao.Options(conditionnement, 5), new yvs.dao.Options(newQte, 6)});
                        return (re != null) ? Constantes.dfN.format(re) : null;
                }
                break;
            case "UPDATE":
                switch (mouvement) {
                    case "E":
                        if (newQte - oldQte >= 0) {//augmentation du stock
                            return null;
                        } else {//diminution du stock
                            re = (Date) dao.loadObjectBySqlQuery(queryS, new yvs.dao.Options[]{new yvs.dao.Options(date, 1), new yvs.dao.Options(article, 2),
                                new yvs.dao.Options(tranche, 3), new yvs.dao.Options(depot, 4),
                                new yvs.dao.Options(conditionnement, 5), new yvs.dao.Options((oldQte - newQte), 6)});
                            return (re != null) ? Constantes.dfN.format(re) : null;
                        }
                    case "S":
                        if (newQte - oldQte <= 0) { //augmentation du stock
                            return null;
                        } else {                    // réduction du stock de (new-old)
                            re = (Date) dao.loadObjectBySqlQuery(queryS, new yvs.dao.Options[]{new yvs.dao.Options(date, 1), new yvs.dao.Options(article, 2),
                                new yvs.dao.Options(tranche, 3), new yvs.dao.Options(depot, 4),
                                new yvs.dao.Options(conditionnement, 5), new yvs.dao.Options((newQte - oldQte), 6)});
                            return (re != null) ? Constantes.dfN.format(re) : null;
                        }
                }
                break;
            case "DELETE":
                switch (mouvement) {
                    case "E":
                        re = (Date) dao.loadObjectBySqlQuery(queryS, new yvs.dao.Options[]{new yvs.dao.Options(date, 1), new yvs.dao.Options(article, 2),
                            new yvs.dao.Options(tranche, 3), new yvs.dao.Options(depot, 4),
                            new yvs.dao.Options(conditionnement, 5), new yvs.dao.Options(newQte, 6)});
                        return (re != null) ? Constantes.dfN.format(re) : null;
                    
                    case "S":
                        return null;
                }
                break;
        }
        return null;
    }
    
    public ResultatAction verifyDate(Date date, int ecart) {
        if (autoriser("com_save_hors_limit")) {
            return result.succes();
        }
        YvsBaseExercice exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findActifByDate", new String[]{"dateJour", "societe"}, new Object[]{date, currentUser.getAgence().getSociete()});
        if (exo != null ? exo.getId() < 1 : true) {
            return result.exerciceNotFind();
        }
        if (exo.getCloturer()) {
            return result.exerciceClose();
        }
        if (ecart > 0) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            
            Calendar d = Calendar.getInstance();
            d.setTime(date);
            d.set(Calendar.HOUR_OF_DAY, 0);
            d.set(Calendar.MINUTE, 0);
            d.set(Calendar.SECOND, 0);
            d.set(Calendar.MILLISECOND, 0);
            if (d.after(c)) {
                return result.canNotSaveInFuture();
            }
            if (ecart > 0) {
                c.add(Calendar.DATE, -ecart);
                if (d.before(c)) {
                    return result.timeIsPast();
                }
            }
        }
        return result.succes();
    }
    
    public ResultatAction verifyDateAchat(Date date) {
        int ecart = -1;
        Integer nbJr = (Integer) dao.loadObjectByNameQueries("YvsComParametreAchat.findnbJourAnByAgence", new String[]{"agence"}, new Object[]{currentUser.getAgence()});
        if ((nbJr != null) ? nbJr > 0 : false) {
            ecart = nbJr;
        }
        return verifyDate(date, ecart);
    }
    
    public ResultatAction verifyDateVente(Date date) {
        int ecart = -1;
        Integer nbJr = (Integer) dao.loadObjectByNameQueries("YvsComParametreVente.findnbJourAnByAgence", new String[]{"agence"}, new Object[]{currentUser.getAgence()});
        if ((nbJr != null) ? nbJr > 0 : false) {
            ecart = nbJr;
        }
        return verifyDate(date, ecart);
    }
    
    public ResultatAction verifyDateStock(Date date) {
        int ecart = -1;
        Integer nbJr = (Integer) dao.loadObjectByNameQueries("YvsComParametreStock.findnbJourAnByAgence", new String[]{"agence"}, new Object[]{currentUser.getAgence()});
        if ((nbJr != null) ? nbJr > 0 : false) {
            ecart = nbJr;
        }
        return verifyDate(date, ecart);
    }
    
    public YvsBaseModeReglement modeEspece(YvsSocietes scte) {
        String nameQueri = "YvsBaseModeReglement.findByDefault";
        String[] champ = new String[]{"societe", "actif", "type", "defaut"};
        Object[] val = new Object[]{scte, true, Constantes.MODE_PAIEMENT_ESPECE, true};
        YvsBaseModeReglement modeByEspece = (YvsBaseModeReglement) dao.loadOneByNameQueries(nameQueri, champ, val);
        if (modeByEspece != null ? (modeByEspece.getId() != null ? modeByEspece.getId() < 1 : true) : true) {
            nameQueri = "YvsBaseModeReglement.findByTypeActif";
            champ = new String[]{"societe", "actif", "type"};
            val = new Object[]{scte, true, Constantes.MODE_PAIEMENT_ESPECE};
            modeByEspece = (YvsBaseModeReglement) dao.loadOneByNameQueries(nameQueri, champ, val);
        }
        return modeByEspece;
    }
}
