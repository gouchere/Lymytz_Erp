/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage;

import java.util.Date;
import yvs.base.compta.UtilCompta;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.param.YvsBaseCentreValorisation;
import yvs.entity.param.YvsBaseTypeEtat;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.formulaire.YvsParamChampFormulaire;
import yvs.entity.param.formulaire.YvsParamFormulaire;
import yvs.entity.param.formulaire.YvsParamModelFormulaire;
import yvs.entity.param.formulaire.YvsParamProprieteChamp;
import yvs.entity.users.YvsUsersAgence;
import yvs.parametrage.formulaire.ChampFormulaire;
import yvs.parametrage.formulaire.ProprieteChamp;
import yvs.parametrage.formulaire.Formulaire;
import yvs.parametrage.formulaire.ModelFormulaire;

/**
 *
 * @author Lymytz Dowes
 */
public class UtilParam {

    public static SectionDeValorisation buildBeanCentreValorisation(YvsBaseCentreValorisation y) {
        SectionDeValorisation c = new SectionDeValorisation();
        if (y != null ? y.getId() != null : false) {
            c.setId(y.getId());
            c.setReference(y.getReference());
            c.setDesignation(y.getDesignation());
            c.setActif(y.getActif());
            c.setDescription(y.getDescription());
//            c.setCompte(UtilCompta.buildBeanCompte(y.getCompte()));
            c.setCentre(UtilCompta.buildBeanCentreAnalytique(y.getCentreAnalyse()));
            c.setTypeValeur(y.getTypeValeur());
            c.setDateSave(y.getDateSave());
        }
        return c;
    }

    public static YvsBaseCentreValorisation buildCentreValorisation(SectionDeValorisation y, YvsUsersAgence u, YvsSocietes s) {
        YvsBaseCentreValorisation c = new YvsBaseCentreValorisation();
        c.setId(y.getId());
        c.setReference(y.getReference());
        c.setDesignation(y.getDesignation());
        c.setActif(y.isActif());
        c.setDescription(y.getDescription());
        c.setTypeValeur(y.getTypeValeur());
//            c.setCompte(UtilCompta.buildBeanCompte(y.getCompte()));
        if (y.getCentre() != null ? y.getCentre().getId() > 0 : false) {
            c.setCentreAnalyse(new YvsComptaCentreAnalytique(y.getCentre().getId(), y.getCentre().getCodeRef(), y.getCentre().getIntitule()));
        }
        c.setDateSave(y.getDateSave());
        c.setDateUpdate(new Date());
        c.setAuthor(u);
        c.setSociete(s); 
        c.setNew_(true);
        return c;
    }

    public static TypeEtat buildBeanTypeEtat(YvsBaseTypeEtat y) {
        TypeEtat c = new TypeEtat();
        if (y != null ? y.getId() != null : false) {
            c.setId(y.getId());
            c.setLibelle(y.getLibelle());
            c.setDescription(y.getDescription());
        }
        return c;
    }

    public static YvsBaseTypeEtat buildTypeEtat(TypeEtat y, YvsSocietes s, YvsUsersAgence u) {
        YvsBaseTypeEtat c = new YvsBaseTypeEtat();
        c.setId(y.getId());
        c.setLibelle(y.getLibelle());
        c.setDescription(y.getDescription());
        c.setSociete(s);
        c.setAuthor(u);
        return c;
    }

    public static Formulaire buildBeanFormulaire(YvsParamFormulaire y) {
        Formulaire r = new Formulaire();
        if (y != null) {
            r.setId(y.getId());
            r.setComposants(y.getComposants());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setIntitule(y.getIntitule());
        }
        return r;
    }

    public static YvsParamFormulaire buildFormulaire(Formulaire y, YvsUsersAgence ua) {
        YvsParamFormulaire r = new YvsParamFormulaire();
        if (y != null) {
            r.setId(y.getId());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setIntitule(y.getIntitule());
            r.setSelect(true);
            r.setAuthor(ua);
        }
        return r;
    }

    public static ChampFormulaire buildBeanChampFormulaire(YvsParamChampFormulaire y) {
        ChampFormulaire r = new ChampFormulaire();
        if (y != null) {
            r.setChamp(y.getChamp());
            r.setFormulaire(buildBeanFormulaire(y.getFormulaire()));
            r.setCode(y.getCode());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
        }
        return r;
    }

    public static YvsParamChampFormulaire buildChampFormulaire(ChampFormulaire y, YvsUsersAgence ua) {
        YvsParamChampFormulaire r = new YvsParamChampFormulaire();
        if (y != null) {
            r.setChamp(y.getChamp());
            if (y.getFormulaire() != null ? y.getFormulaire().getId() > 0 : false) {
                r.setFormulaire(new YvsParamFormulaire(y.getFormulaire().getId()));
            }
            r.setCode(y.getCode());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setSelect(true);
            r.setAuthor(ua);
        }
        return r;
    }

    public static ModelFormulaire buildBeanModelFormulaire(YvsParamModelFormulaire y) {
        ModelFormulaire r = new ModelFormulaire();
        if (y != null) {
            r.setId(y.getId());
            r.setComposants(y.getComposants());
            r.setFormulaire(buildBeanFormulaire(y.getFormulaire()));
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setIntitule(y.getIntitule());
        }
        return r;
    }

    public static YvsParamModelFormulaire buildModelFormulaire(ModelFormulaire y, YvsUsersAgence ua) {
        YvsParamModelFormulaire r = new YvsParamModelFormulaire();
        if (y != null) {
            r.setId(y.getId());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setIntitule(y.getIntitule());
            if (y.getFormulaire() != null ? y.getFormulaire().getId() > 0 : false) {
                r.setFormulaire(new YvsParamFormulaire(y.getFormulaire().getId()));
            }
            r.setSelect(true);
            r.setAuthor(ua);
        }
        return r;
    }

    public static ProprieteChamp buildBeanComposantFormulaire(YvsParamProprieteChamp y) {
        ProprieteChamp r = new ProprieteChamp();
        if (y != null) {
            r.setEditable(y.getEditable());
            r.setModele(buildBeanModelFormulaire(y.getModele()));
            r.setChamp(buildBeanChampFormulaire(y.getChamp()));
            r.setObligatoire(y.getObligatoire());
            r.setVisible(y.getVisible());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
        }
        return r;
    }

    public static YvsParamProprieteChamp buildComposantFormulaire(ProprieteChamp y, YvsUsersAgence ua) {
        YvsParamProprieteChamp r = new YvsParamProprieteChamp();
        if (y != null) {
            r.setEditable(y.isEditable());
            if (y.getModele() != null ? y.getModele().getId() > 0 : false) {
                r.setModele(new YvsParamModelFormulaire(y.getModele().getId()));
            }
            if (y.getChamp() != null ? y.getChamp().getId() > 0 : false) {
                r.setChamp(new YvsParamChampFormulaire(y.getChamp().getId(), y.getChamp().getCode(), y.getChamp().getChamp()));
            }
            r.setObligatoire(y.isObligatoire());
            r.setVisible(y.isVisible());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setSelect(true);
            r.setAuthor(ua);
        }
        return r;
    }
}
