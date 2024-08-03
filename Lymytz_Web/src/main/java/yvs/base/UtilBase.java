/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base;

import java.util.Date;
import yvs.entity.base.YvsBaseDepartement;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
public class UtilBase {

    public static YvsBaseDepartement buildDepartement(Departement dep, YvsUsersAgence ua, YvsSocietes ste) {
        YvsBaseDepartement d = null;
        if (dep != null) {
            d = new YvsBaseDepartement(dep.getId());
            d.setDescription(dep.getDescription());
            d.setIntitule(dep.getIntitule());
            d.setCodeDepartement(dep.getCodeDepartement());
            d.setActif(true);
            if (dep.getParent() != null ? dep.getParent().getId() > 0 : false) {
                d.setDepartementParent(new YvsBaseDepartement(dep.getParent().getId(), dep.getParent().getCodeDepartement(), dep.getParent().getIntitule()));
            }
            d.setDateSave(dep.getDateSave());
            d.setDateUpdate(new Date());
            d.setAuthor(ua);
            d.setSociete(ste);
        }
        return d;
    }

    public static Departement buildBeanDepartement(YvsBaseDepartement dep) {
        Departement d = buildSimpleBeanDepartement(dep);
        if (dep != null) {
            d.setParent(buildSimpleBeanDepartement(dep.getDepartementParent()));
        }
        return d;
    }

    public static Departement buildSimpleBeanDepartement(YvsBaseDepartement dep) {
        Departement d = new Departement();
        if (dep != null) {
            d.setId(dep.getId());
            d.setDescription(dep.getDescription());
            d.setIntitule(dep.getIntitule());
            d.setCodeDepartement(dep.getCodeDepartement());
            d.setActif(dep.getActif());
        }
        return d;
    }
}
