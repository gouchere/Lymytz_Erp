/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.catTarif;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.util.Managed;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "McatT")
@SessionScoped
public class ManagedCategorieT extends Managed<CategorieTarifaire, YvsBaseCaisse> implements Serializable {

    List<CategorieTarifaire> listCatT;
    List<CategorieTarifaire> listFiltre;
    @ManagedProperty(value = "#{catT}")
    private CategorieTarifaire categorie;
    private CategorieTarifaire selectCatC;

    public ManagedCategorieT() {
        listCatT = new ArrayList<>();
    }

    public CategorieTarifaire getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieTarifaire categorie) {
        this.categorie = categorie;
    }

    public List<CategorieTarifaire> getListCatT() {
        return listCatT;
    }

    public void setListCatT(List<CategorieTarifaire> listCatT) {
        this.listCatT = listCatT;
    }

    public List<CategorieTarifaire> getListFiltre() {
        return listFiltre;
    }

    public void setListFiltre(List<CategorieTarifaire> listFiltre) {
        this.listFiltre = listFiltre;
    }

    public CategorieTarifaire getSelectCatC() {
        return selectCatC;
    }

    public void setSelectCatC(CategorieTarifaire selectCatC) {
        this.selectCatC = selectCatC;
    }

    @Override
    public boolean controleFiche(CategorieTarifaire bean) {
        if (bean.getDesignation() == null) {
//            getMessage(message.getMessage("catC"), FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }
    private long key;

    @Override
    public boolean saveNew() {
        CategorieTarifaire cat = recopieView();
        if (controleFiche(cat)) {
            cat.setAuteur(getUserOnLine());
            cat.setLastAuteur(getUserOnLine());
            cat.setLastDateUpdate(dft.format(new Date()));
            YvsBaseCategorieClient catt = buildEntityFromBean(cat);
            catt.setId(null);
            YvsBaseCategorieClient id = ((YvsBaseCategorieClient) dao.save1(catt));
            cat.setId(id.getId());
            listCatT.add(0, cat);
            update("catT-table");
            succes();
            setDisableSave(true);
        }
        return true;
    }

    @Override
    public void updateBean() {
        CategorieTarifaire dep = recopieView();
        if (controleFiche(dep)) {
            dep.setLastAuteur(getUserOnLine());
            dep.setLastDateUpdate(dft.format(new Date()));
            YvsBaseCategorieClient cat = buildEntityFromBean(dep);
            dao.update(cat);
            listCatT.set(listCatT.indexOf(dep), dep);
            update("catT-table");
            succes();
        }
    }

    @Override
    public CategorieTarifaire recopieView() {
        CategorieTarifaire cat = new CategorieTarifaire(categorie.getDesignation());
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
    public void populateView(CategorieTarifaire bean) {
        categorie.setId(bean.getId());
        categorie.setDesignation(bean.getDesignation());
        categorie.setAuteur(bean.getAuteur());
        categorie.setLastAuteur(bean.getLastAuteur());
        categorie.setLastDateUpdate(bean.getLastDateUpdate());
        categorie.setCodeAppel(bean.getCodeAppel());
        categorie.setActif(bean.isActif());
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        CategorieTarifaire bean = (CategorieTarifaire) ev.getObject();
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
        String[] ch = {"societe"};
        Object[] v = {currentAgence.getSociete().getId()};
        List<YvsBaseCategorieClient> l = dao.loadNameQueries("YvsCatTarif.findAll", ch, v);
        listCatT.clear();
        for (YvsBaseCategorieClient d : l) {
            listCatT.add(buildBeanFromEntity(d));
        }
    }
    YvsBaseCategorieClient currentCat;

    @PostConstruct
    public void loadActif() {
        String[] ch = {"societe"};
        Object[] v = {currentAgence.getSociete().getId()};
        List<Object[]> l = dao.loadNameQueries("YvsCatTarif.findByActif", ch, v);
        listCatT.clear();
        for (Object[] d : l) {
            currentCat = new YvsBaseCategorieClient((long) d[0]);
            listCatT.add(buildBeanFromEntity(currentCat));
        }
    }

    private YvsBaseCategorieClient buildEntityFromBean(CategorieTarifaire cat) {
        YvsBaseCategorieClient result = new YvsBaseCategorieClient();
//        result.setDesignation(cat.getDesignation());
//        result.setCodeAppel(cat.getCodeAppel());
//        result.setActif(cat.isActif());
//        try {           
//            if (cat.getLastDateUpdate() != null) {
//                result.setLastDateSave(dft.parse(cat.getLastDateUpdate()));
//            } else {
//                result.setLastDateSave(new Date());
//            }
//        } catch (ParseException ex) {
//            Logger.getLogger(ManagedCategorieT.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        result.setId(cat.getId());
//        if (cat.getAuteur() != null) {
//            result.setAuthor(cat.getAuteur());
//        } else {
//            result.setAuthor(getUserOnLine());
//        }
//        if (cat.getLastAuteur() != null) {
//            result.setLastAuthor(cat.getLastAuteur());
//        } else {
//            result.setLastAuthor(getUserOnLine());
//        }
        return result;
    }

    private CategorieTarifaire buildBeanFromEntity(YvsBaseCategorieClient cat) {
        CategorieTarifaire result = new CategorieTarifaire();
//        result.setActif(cat.getActif());
//        result.setDesignation(cat.getDesignation());
//        result.setCodeAppel(cat.getCodeAppel());      
//        if (cat.getLastDateSave() != null) {
//            result.setLastDateUpdate(dft.format(cat.getLastDateSave()));
//        } else {
//            result.setLastDateUpdate(dft.format(new Date()));
//        }
//        result.setAuteur(cat.getAuthor());
//        result.setLastAuteur(cat.getLastAuthor());
        result.setId(cat.getId());
        return result;
    }

    public void disable() {
        YvsBaseCategorieClient cc = buildEntityFromBean(selectCatC);
//        cc.setLastAuthor(getUserOnLine());
//        cc.setLastDateSave(new Date());
//        cc.setActif(false);
        selectCatC.setLastAuteur(getUserOnLine());
        selectCatC.setLastDateUpdate(dft.format(new Date()));
        selectCatC.setActif(false);
        dao.update(cc);
        listCatT.set(listCatT.indexOf(selectCatC), selectCatC);
        update("catT-table");
        succes();
    }

    public void delete() {
        YvsBaseCategorieClient cc = buildEntityFromBean(selectCatC);
//        cc.setLastAuthor(getUserOnLine());
//        cc.setLastDateSave(new Date());
//        cc.setActif(false);
//        cc.setSupp(true);
        dao.update(cc);
        listCatT.remove(selectCatC);
        update("catT-table");
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
