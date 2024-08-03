/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.entrepot;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsAgences;
//import yvs.entity.param.YvsNiveauDepot;
import yvs.grh.bean.Employe;
import yvs.parametrage.agence.Agence;

/**
 *
 * @author GOUCHERE YVES
 */
public class UtilDepots {

    @EJB
    public static DaoInterfaceLocal dao;

    public UtilDepots() {
    }

    public static YvsBaseDepots buildBeanDepot(Depots dep) {
        YvsBaseDepots d = new YvsBaseDepots(dep.getId());
        d.setAbbreviation(dep.getAbbreviation());
        d.setActif(dep.isActif());
        d.setAdresse(dep.getAdresse());
        d.setAgence(new YvsAgences((long)dep.getId()));
        d.setCode(dep.getCode());
        d.setControlStock(dep.isControlStock());
        d.setDesignation(dep.getDesignation());
        if (dep.getResponsable().getId() != 0) {
            d.setResponsable(new YvsGrhEmployes(dep.getResponsable().getId()));
        }
        d.setOpAchat(dep.isOpAchat());
        d.setOpProduction(dep.isOpProduction());
        d.setOpTransit(dep.isOpTransit());
        d.setOpVente(dep.isOpVente());
        return d;
    }

    public static Depots buildBeanDepot(YvsBaseDepots dep) {
        Depots d = new Depots(dep.getId());
        d.setAbbreviation(dep.getAbbreviation());
        d.setActif(dep.getActif());
        d.setAdresse(dep.getAdresse());
        if (dep.getAgence() != null) {
            Agence a = new Agence(dep.getAgence().getId(), dep.getAgence().getCodeagence());
        }
        if (dep.getResponsable() != null) {
            d.setResponsable(new Employe(dep.getResponsable().getId(), dep.getResponsable().getNom()));
        }
        d.setCode(dep.getCode());
        d.setControlStock(dep.getControlStock());
        d.setDesignation(dep.getDesignation());
        d.setOpAchat(dep.getOpAchat());
        d.setOpProduction(dep.getOpProduction());
        d.setOpTransit(dep.getOpTransit());
        d.setOpVente(dep.getOpVente());
//        if (dep.getYvsLiaisonDepotsList() != null) {
//            for (YvsLiaisonDepots dd : dep.getYvsLiaisonDepotsList()) {
//                Depots dde = new Depots(dd.getIdDepot2().getId());
//                dde.setCodeDepot(dd.getIdDepot2().getCode());
//                dde.setDesignation(dd.getIdDepot2().getDesignation());
//                d.getDepotsLie().add(dde);
//            }
//        }
//        if (dep.getYvsDepotCrenauxList() != null) {
//            for (YvsDepotCrenaux dc : dep.getYvsDepotCrenauxList()) {
//                d.getListCrenaux().add(UtilCrenaux.buildBeanDepot(dc.getIdCrenaux()));
//            }
//        }

//        if (dep.getYvsNiveauDepotList() != null) {
//            for (YvsNiveauDepot dc : dep.getYvsNiveauDepotList()) {
////                d.get().add(new Niveaux(dc.getId(), dc.getDesignation(), dc.getDescription()));
//            }
//        }
        return d;
    }

    public static List<Depots> buildBeanDepot(List<YvsBaseDepots> l) {
        List<Depots> result = new ArrayList<>();
        for (YvsBaseDepots d : l) {
            result.add(buildBeanDepot(d));
        }
        return result;
    }
}
