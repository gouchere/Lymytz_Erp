/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.proj;

import java.util.Date;
import yvs.base.UtilBase;
import yvs.entity.param.YvsSocietes;
import yvs.entity.proj.YvsProjDepartement;
import yvs.entity.proj.projet.YvsProjProjet;
import yvs.entity.users.YvsUsersAgence;
import yvs.proj.param.DepartementProj;
import yvs.proj.param.Projet;

/**
 *
 * @author Lymytz Dowes
 */
public class UtilProj {

    public static DepartementProj buildBeanDepartement(YvsProjDepartement y) {
        DepartementProj r = new DepartementProj();
        if (y != null) {
            r.setId(y.getId());
            r.setActif(y.getActif());
            r.setDateSave(y.getDateSave());
            r.setService(UtilBase.buildBeanDepartement(y.getService()));
        }
        return r;
    }

    public static YvsProjDepartement buildDepartement(DepartementProj y, YvsUsersAgence ua, YvsSocietes ste) {
        YvsProjDepartement r = new YvsProjDepartement();
        if (y != null) {
            r.setId(y.getId());
            r.setActif(y.isActif());
            r.setDateSave(y.getDateSave());
            if (y.getService() != null ? y.getService().getId() > 0 : false) {
                r.setService(UtilBase.buildDepartement(y.getService(), ua, ste));
            }
            r.setAuthor(ua);
            r.setDateUpdate(new Date());
        }
        return r;
    }

    public static YvsProjProjet buildProjet(Projet y, YvsUsersAgence ua, YvsSocietes ste) {
        YvsProjProjet r = new YvsProjProjet();
        if (y != null) {
            r.setActif(y.isActif());
            r.setAuthor(ua);
            r.setCode(y.getCode());
            r.setDirect(y.isDirect());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setDescription(y.getDescription());
            r.setId(y.getId());
            r.setLibelle(y.getLibelle());
            r.setSociete(ste);
            if (y.getParent() != null ? y.getParent().getId() > 0 : false) {
                r.setParent(new YvsProjProjet(y.getParent().getId(), y.getParent().getCode(), y.getParent().getLibelle()));
            }
        }
        return r;
    }

    public static Projet buildBeanProjet(YvsProjProjet y) {
        Projet r = new Projet();
        if (y != null) {
            r.setActif(y.getActif());
            r.setCode(y.getCode());
            r.setDirect(y.getDirect());
            r.setDateSave(y.getDateSave());
            r.setDescription(y.getDescription());
            r.setId(y.getId());
            r.setLibelle(y.getLibelle());
            r.setParent(y.getParent() != null ? new Projet(y.getParent().getId()) : new Projet());
            r.setCodeAcces(y.getCodeAcces() != null ? y.getCodeAcces().getCode() : "");
            r.setServices(y.getServices());
        }
        return r;
    }
}
