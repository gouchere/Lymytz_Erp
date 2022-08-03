/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.fournisseur;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.commercial.UtilCom;
import yvs.entity.base.YvsBaseCategorieFournisseur;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedCategorieFsseur extends Managed<CategorieFournisseur, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{categorieFournisseur}")
    private CategorieFournisseur categorieFournisseur;
    private List<YvsBaseCategorieFournisseur> categories, categoriesParent;
    private YvsBaseCategorieFournisseur categorieSelect;

    private String tabIds;

    public ManagedCategorieFsseur() {
        categories = new ArrayList<>();
        categoriesParent = new ArrayList<>();
    }

    public YvsBaseCategorieFournisseur getCategorieSelect() {
        return categorieSelect;
    }

    public void setCategorieSelect(YvsBaseCategorieFournisseur categorieSelect) {
        this.categorieSelect = categorieSelect;
    }

    public List<YvsBaseCategorieFournisseur> getCategoriesParent() {
        return categoriesParent;
    }

    public void setCategoriesParent(List<YvsBaseCategorieFournisseur> categoriesParent) {
        this.categoriesParent = categoriesParent;
    }

    public CategorieFournisseur getCategorieFournisseur() {
        return categorieFournisseur;
    }

    public void setCategorieFournisseur(CategorieFournisseur categorieFournisseur) {
        this.categorieFournisseur = categorieFournisseur;
    }

    public List<YvsBaseCategorieFournisseur> getCategories() {
        return categories;
    }

    public void setCategories(List<YvsBaseCategorieFournisseur> categories) {
        this.categories = categories;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        loadAllCategorie();
        if (categorieFournisseur.getParent() != null ? categorieFournisseur.getParent().getId() < 1 : true) {
            categorieFournisseur.setParent(new CategorieFournisseur());
        }
        loadAllParent(categorieSelect);
    }

    public void loadAllCategorie() {
        categoriesParent.clear();
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        categories = dao.loadNameQueries("YvsBaseCategorieFournisseur.findAll", champ, val);
        categoriesParent.addAll(categories);
    }

    public void loadAllParent(YvsBaseCategorieFournisseur y) {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsBaseCategorieFournisseur.findAll";
        if (y != null ? y.getId() > 0 : false) {
            champ = new String[]{"societe", "id"};
            val = new Object[]{currentAgence.getSociete(), y.getId()};
            nameQueri = "YvsBaseCategorieFournisseur.findByNotId";
        }
        categoriesParent = dao.loadNameQueries(nameQueri, champ, val);
    }

    @Override
    public boolean controleFiche(CategorieFournisseur bean) {
        if (bean.getCode() == null || bean.getCode().equals("")) {
            getErrorMessage("Vous devez entrer le code");
            return false;
        }
        if (bean.getLibelle() == null || bean.getLibelle().equals("")) {
            getErrorMessage("Vous devez entrer le libelle");
            return false;
        }
        if (bean.getId() > 0) {
            YvsBaseCategorieFournisseur t = (YvsBaseCategorieFournisseur) dao.loadOneByNameQueries("YvsBaseCategorieFournisseur.findByCurrentCode", new String[]{"code", "societe"}, new Object[]{bean.getCode(), currentAgence.getSociete()});
            if (t != null ? t.getId() > 0 : false) {
                getErrorMessage("Vous avez déja crée cette catégorie");
                return false;
            }
        }
        return true;
    }

    @Override
    public void populateView(CategorieFournisseur bean) {
        cloneObject(categorieFournisseur, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(categorieFournisseur);
        categorieFournisseur.setFils(new ArrayList<CategorieFournisseur>());
        categorieFournisseur.setParent(new CategorieFournisseur());
        tabIds = "";
        categorieSelect = new YvsBaseCategorieFournisseur();
        loadAllParent(categorieSelect);
    }

    @Override
    public boolean saveNew() {
        String action = categorieFournisseur.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(categorieFournisseur)) {
                YvsBaseCategorieFournisseur entity = UtilCom.buildCategorieFournisseur(categorieFournisseur, currentUser, currentAgence.getSociete());
                if (categorieFournisseur.getId() < 1) {
                    entity.setId(null);
                    entity = (YvsBaseCategorieFournisseur) dao.save1(entity);
                    categorieFournisseur.setId(entity.getId());
                    categories.add(0, entity);
                    categoriesParent.add(0, entity);
                } else {
                    dao.update(entity);
                    categories.set(categories.indexOf(entity), entity);
                    if (categoriesParent.contains(entity)) {
                        categoriesParent.set(categoriesParent.indexOf(entity), entity);
                    }
                }
                succes();
                resetFiche();
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                String[] tab = tabIds.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsBaseCategorieFournisseur bean = categories.get(categories.indexOf(new YvsBaseCategorieFournisseur(id)));
                    dao.delete(bean);
                    categories.remove(bean);
                    categoriesParent.remove(bean);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsBaseCategorieFournisseur y) {
        categorieSelect = y;
    }

    public void deleteBean_() {
        try {
            if (categorieSelect != null) {
                dao.delete(categorieSelect);
                categories.remove(categorieSelect);
                categoriesParent.remove(categorieSelect);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            categorieSelect = (YvsBaseCategorieFournisseur) ev.getObject();
            populateView(UtilCom.buildBeanCategorieFournisseur(categorieSelect));
            loadAllParent(categorieSelect);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void chooseParent() {
        if (categorieFournisseur.getParent() != null ? categorieFournisseur.getParent().getId() > 0 : false) {
            int idx = categoriesParent.indexOf(new YvsBaseCategorieFournisseur(categorieFournisseur.getParent().getId()));
            if (idx > -1) {
                categorieFournisseur.setParent(UtilCom.buildBeanCategorieFournisseur(categoriesParent.get(idx)));
            }
        }
    }
}
