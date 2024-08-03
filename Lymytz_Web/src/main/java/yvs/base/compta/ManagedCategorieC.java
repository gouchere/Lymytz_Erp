/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.io.Serializable;import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.compta.YvsBaseCaisse;
//import yvs.entity.produits.YvsArticles;
//import yvs.entity.param.YvsCatcompta;
import yvs.util.Managed;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "McatC")
@SessionScoped
public class ManagedCategorieC extends Managed<CategorieComptable, YvsBaseCaisse> implements Serializable {

    List<CategorieComptable> listCac;
    List<CategorieComptable> listFiltre;
    @ManagedProperty(value = "#{catC}")
    private CategorieComptable categorie;
    private CategorieComptable selectCatC;

    public ManagedCategorieC() {
        listCac = new ArrayList<>();
    }

    public CategorieComptable getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieComptable categorie) {
        this.categorie = categorie;
    }

    public List<CategorieComptable> getListCac() {
        return listCac;
    }

    public void setListCac(List<CategorieComptable> listCac) {
        this.listCac = listCac;
    }

    public List<CategorieComptable> getListFiltre() {
        return listFiltre;
    }

    public void setListFiltre(List<CategorieComptable> listFiltre) {
        this.listFiltre = listFiltre;
    }

    public CategorieComptable getSelectCatC() {
        return selectCatC;
    }

    public void setSelectCatC(CategorieComptable selectCatC) {
        this.selectCatC = selectCatC;
    }

    @Override
    public boolean controleFiche(CategorieComptable bean) {
        if (bean.getCodeCategorie() == null) {
//            getMessage(message.getMessage("catC"), FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }
    private long key;

    @Override
    public boolean saveNew() {
        CategorieComptable cat = recopieView();
        if (controleFiche(cat)) {
            cat.setAuteur(getUserOnLine());
            cat.setLastAuteur(getUserOnLine());
            cat.setLastDateUpdate(dft.format(new Date()));
            YvsBaseCategorieComptable catC = buildEntityFromBean(cat);
            catC.setId(null);
            catC.setSociete(currentAgence.getSociete());
            YvsBaseCategorieComptable id = ((YvsBaseCategorieComptable) dao.save1(catC));
            cat.setId(id.getId());
            listCac.add(0, cat);
            update("catc-table");
            succes();
            setDisableSave(true);
        }
        return true;
    }

    @Override
    public void updateBean() {
        CategorieComptable dep = recopieView();
        if (controleFiche(dep)) {
            dep.setLastAuteur(getUserOnLine());
            dep.setLastDateUpdate(dft.format(new Date()));
            YvsBaseCategorieComptable cat = buildEntityFromBean(dep);
//            cat.setLastauteurmodification(getUserOnLine());
//            cat.setDatelastmodification(new Date());
            dao.update(cat);
            listCac.set(listCac.indexOf(dep), dep);
            update("catc-table");
            succes();
        }
    }

    @Override
    public CategorieComptable recopieView() {
        CategorieComptable cat = new CategorieComptable(categorie.getCodeCategorie(), categorie.getNature());
        cat.setActif(categorie.isActif());
        cat.setCodeAppel(categorie.getCodeAppel());
        if (categorie.getAuteur() != null) {
            cat.setAuteur(categorie.getAuteur());
        } else {
            cat.setAuteur(getUserOnLine());
        }
        if (categorie.getLastAuteur() != null) {
            cat.setLastAuteur(categorie.getLastAuteur());
        } else {
            cat.setLastAuteur(getUserOnLine());
        }
        if (categorie.getLastDateUpdate() != null) {
            cat.setLastDateUpdate(categorie.getLastDateUpdate());
        } else {
            cat.setLastDateUpdate(dft.format(new Date()));
        }
        cat.setId(categorie.getId());
        return cat;
    }

    @Override
    public void populateView(CategorieComptable bean) {
        categorie.setId(bean.getId());
        categorie.setCodeCategorie(bean.getCodeCategorie());
        categorie.setNature(bean.getNature());
        categorie.setAuteur(bean.getAuteur());
        categorie.setLastAuteur(bean.getLastAuteur());
        categorie.setLastDateUpdate(bean.getLastDateUpdate());
        categorie.setActif(bean.isActif());
        categorie.setCodeAppel(bean.getCodeAppel());
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        CategorieComptable bean = (CategorieComptable) ev.getObject();
        if (bean != null) {
            populateView(bean);
            setDisableSave(true);
        }
    }

    @Override
    public void resetFiche() {
        resetFiche(categorie);
        setDisableSave(false);
    }

    @Override
    public void loadAll() {
        String[] ch = {};
        Object[] v = {};
        List<YvsBaseCategorieComptable> l = dao.loadNameQueries("YvsCatcompta.findAll", ch, v);
        listCac.clear();
        for (YvsBaseCategorieComptable d : l) {
            listCac.add(buildBeanFromEntity(d));
        }
    }

    public void loadActif() {
        String[] ch = {};
        Object[] v = {};
        List<YvsBaseCategorieComptable> l = dao.loadNameQueries("YvsCatcompta.findByActif", ch, v);
        listCac.clear();
        for (YvsBaseCategorieComptable d : l) {
            listCac.add(buildBeanFromEntity(d));
        }
    }

    private YvsBaseCategorieComptable buildEntityFromBean(CategorieComptable cat) {
        YvsBaseCategorieComptable result = new YvsBaseCategorieComptable();
//        result.setCodecat(cat.getCodeCategorie());
//        result.setActif(cat.isActif());
//        result.setCodeAppel(cat.getCodeAppel());
//        try {
//            if (cat.getDateSave() != null) {
//                result.setDatesave(dft.parse(cat.getDateSave()));
//            } else {
//                result.setDatesave(new Date());
//            }
//            if (cat.getLastDateUpdate() != null) {
//                result.setDatelastmodification(dft.parse(cat.getLastDateUpdate()));
//            } else {
//                result.setDatelastmodification(new Date());
//            }
//        } catch (ParseException ex) {
//            Logger.getLogger(ManagedCategorieC.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        result.setNature(cat.getNature());
//        result.setId(cat.getId());
//        if (cat.getAuteur() != null) {
//            result.setAuteur(cat.getAuteur());
//        } else {
//            result.setAuteur(getUserOnLine());
//        }
//        if (cat.getLastAuteur() != null) {
//            result.setLastauteurmodification(cat.getLastAuteur());
//        } else {
//            result.setLastauteurmodification(getUserOnLine());
//        }
        return result;
    }

    private CategorieComptable buildBeanFromEntity(YvsBaseCategorieComptable cat) {
        CategorieComptable result = new CategorieComptable();
        result.setActif(cat.getActif());
//        result.setCodeCategorie(cat.getCodecat());
//        result.setCodeAppel(cat.getCodeAppel());
//        if (cat.getDatesave() != null) {
//            result.setDateSave(dft.format(cat.getDatesave()));
//        } else {
//            result.setDateSave(dft.format(new Date()));
//        }
//        if (cat.getLastauteurmodification() != null) {
//            result.setLastDateUpdate(dft.format(cat.getDatelastmodification()));
//        } else {
//            result.setLastDateUpdate(dft.format(new Date()));
//        }
//        result.setNature(cat.getNature());
////        result.setIdSave(cat.getId());
//        result.setAuteur(cat.getAuteur());
//        result.setLastAuteur(cat.getLastauteurmodification());
//        result.setId(cat.getId());
        return result;
    }

    public void disable() {
        YvsBaseCategorieComptable cc = buildEntityFromBean(selectCatC);
//        cc.setLastauteurmodification(getUserOnLine());
//        cc.setDatelastmodification(new Date());
        cc.setActif(false);
        selectCatC.setLastAuteur(getUserOnLine());
        selectCatC.setLastDateUpdate(dft.format(new Date()));
        selectCatC.setActif(false);
        dao.update(cc);
        listCac.set(listCac.indexOf(selectCatC), selectCatC);
        update("catc-table");
        succes();
    }

    public void delete() {
        YvsBaseCategorieComptable cc = buildEntityFromBean(selectCatC);
//        cc.setLastauteurmodification(getUserOnLine());
//        cc.setDatelastmodification(new Date());
        cc.setActif(false);
        cc.setSupp(true);
        dao.update(cc);
        listCac.remove(selectCatC);
        update("catc-table");
        succes();
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
