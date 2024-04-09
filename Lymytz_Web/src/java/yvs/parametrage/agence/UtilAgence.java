/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.agence;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsGrhSecteurs;
import yvs.grh.UtilGrh;
import static yvs.grh.UtilGrh.buildBeanSimpleEmployePartial;
import yvs.grh.bean.EmployePartial;
import yvs.parametrage.societe.Societe;
import yvs.parametrage.societe.UtilSte;
import yvs.parametrage.dico.Dictionnaire;

/**
 *
 * @author lymytz
 */
public class UtilAgence {

    @EJB
    public static DaoInterfaceLocal dao;

    /**
     * *******DEBUT GESTION AGENCE**********
     *
     * @param y
     * @return
     */
    public static Agence buildBeanAgence(YvsAgences y) {
        Agence r = new Agence();
        if (y != null) {
            r.setAbbreviation(y.getAbbreviation());
            r.setActif((y.getActif() != null) ? y.getActif() : true);
            r.setAdresse(y.getAdresse());
            r.setCodeAgence(y.getCodeagence());
            r.setDesignation(y.getDesignation());
            r.setId(y.getId());
            r.setRegion(y.getRegion());
            r.setTelephone(y.getTelephone());
            r.setEmail(y.getEmail());
            r.setSociete((y.getSociete() != null) ? UtilSte.buildSimpleBeanSociete(y.getSociete()) : new Societe());
            r.setSecteur((y.getSecteurActivite() != null) ? buildBeanSecteurActivite(y.getSecteurActivite()) : new SecteurActivite());
            r.setVille(UtilGrh.buildBeanDictionnaire(y.getVille()));
            r.setPays(r.getVille() != null ? (r.getVille().getParent() != null ? r.getVille().getParent() : new Dictionnaire()) : new Dictionnaire());
            r.setResponsableAgence((y.getChefAgence() != null) ? buildBeanSimpleEmployePartial(y.getChefAgence()) : new EmployePartial());
            r.setEmployeStagiaire(y.getEmployeStagiaire());
            r.setEmployeTacheron(y.getEmployeTacheron());
            r.setEmployeTemporaire(y.getEmployeTemporaire());
            r.setDateSave(y.getDateSave());
        }
        return r;
    }

    public static List<Agence> buildBeanListeAgence(List<YvsAgences> list) {
        List<Agence> result = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsAgences a : list) {
                    Agence agence = buildBeanAgence(a);
                    result.add(agence);
                }
            }
        }
        return result;
    }

    public static SecteurActivite buildBeanSecteurActivite(YvsGrhSecteurs s) {
        SecteurActivite r = new SecteurActivite();
        if (s != null) {
            r.setId(s.getId());
            r.setDescription(s.getDescription());
            r.setNom(s.getNom());
            r.setSociete((s.getSociete() != null) ? new Societe(s.getSociete().getId(), s.getSociete().getName()) : new Societe());
        }
        return r;
    }

    public static List<SecteurActivite> buildBeanListeSecteurActivite(List<YvsGrhSecteurs> list) {
        List<SecteurActivite> result = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsGrhSecteurs a : list) {
                    SecteurActivite agence = buildBeanSecteurActivite(a);
                    result.add(agence);
                }
            }
        }
        return result;
    }

    /**
     * *******FIN GESTION AGENCE**********
     */
}
