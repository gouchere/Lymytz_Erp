/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.commercial.YvsComCategoriePersonnel;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedCategoriePerso extends Managed<CategoriePerso, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{categoriePerso}")
    private CategoriePerso categoriePerso;
    private CategoriePerso categorieParent = new CategoriePerso();
    private List<YvsComCategoriePersonnel> categories, categoriesParent;
    private YvsComCategoriePersonnel categorieSelect;

    private String tabIds;

    public ManagedCategoriePerso() {
        categories = new ArrayList<>();
        categoriesParent = new ArrayList<>();
    }

    public YvsComCategoriePersonnel getCategorieSelect() {
        return categorieSelect;
    }

    public void setCategorieSelect(YvsComCategoriePersonnel categorieSelect) {
        this.categorieSelect = categorieSelect;
    }

    public CategoriePerso getCategorieParent() {
        return categorieParent;
    }

    public void setCategorieParent(CategoriePerso categorieParent) {
        this.categorieParent = categorieParent;
    }

    public List<YvsComCategoriePersonnel> getCategoriesParent() {
        return categoriesParent;
    }

    public void setCategoriesParent(List<YvsComCategoriePersonnel> categoriesParent) {
        this.categoriesParent = categoriesParent;
    }

    public CategoriePerso getCategoriePerso() {
        return categoriePerso;
    }

    public void setCategoriePerso(CategoriePerso categoriePerso) {
        this.categoriePerso = categoriePerso;
    }

    public List<YvsComCategoriePersonnel> getCategories() {
        return categories;
    }

    public void setCategories(List<YvsComCategoriePersonnel> categories) {
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
    }

    public void loadAllCategorie() {
        categoriesParent.clear();
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        categories = dao.loadNameQueries("YvsComCategoriePersonnel.findAll", champ, val);
        categoriesParent.addAll(categories);
    }

    public YvsComCategoriePersonnel buildCategoriePerso(CategoriePerso y) {
        YvsComCategoriePersonnel c = UtilCom.buildCategoriePerso(y, currentUser, currentAgence.getSociete());
        if (c != null ? ((categorieParent != null) ? categorieParent.getId() > 0 : false) : false) {
            c.setParent(categoriesParent.get(categoriesParent.indexOf(new YvsComCategoriePersonnel(categorieParent.getId()))));
        }
        return c;
    }

    @Override
    public CategoriePerso recopieView() {
        CategoriePerso c = new CategoriePerso();
        c.setId(categoriePerso.getId());
        c.setDescription(categoriePerso.getDescription());
        c.setCode(categoriePerso.getCode());
        c.setLibelle(categoriePerso.getLibelle());
        c.setParent(new CategoriePerso());
        c.setParent(categoriePerso.getParent());
        c.setUpdate(categoriePerso.isUpdate());
        c.setNew_(true);
        return c;
    }

    @Override
    public boolean controleFiche(CategoriePerso bean) {
        if (bean.getCode() == null || bean.getCommissions().equals("")) {
            getErrorMessage("Vous devez entrer le code");
            return false;
        }
        if (bean.getLibelle() == null || bean.getLibelle().equals("")) {
            getErrorMessage("Vous devez entrer le libelle");
            return false;
        }
        if (!bean.isUpdate()) {
            YvsComCategoriePersonnel t = (YvsComCategoriePersonnel) dao.loadOneByNameQueries("YvsComCategoriePersonnel.findByCurrentCode", new String[]{"code", "societe"}, new Object[]{bean.getCode(), currentAgence.getSociete()});
            if (t != null ? t.getId() > 0 : false) {
                getErrorMessage("Vous avez déja crée cette catégorie");
                return false;
            }
        }
        return true;
    }

    @Override
    public void populateView(CategoriePerso bean) {
        cloneObject(categoriePerso, bean);
        if ((bean.getParent() != null) ? bean.getParent().getId() > 0 : false) {
            cloneObject(categorieParent, bean.getParent());
        }
    }

    @Override
    public void resetFiche() {
        resetFiche(categoriePerso);
        categoriePerso.setFils(new ArrayList<CategoriePerso>());
        categoriePerso.setParent(new CategoriePerso());
        tabIds = "";

        categorieSelect = null;
        resetFiche(categorieParent);
        categorieParent.setFils(new ArrayList<CategoriePerso>());
        categorieParent.setParent(new CategoriePerso());
    }

    @Override
    public boolean saveNew() {
        String action = categoriePerso.isUpdate() ? "Modification" : "Insertion";
        try {
            CategoriePerso bean = recopieView();
            if (controleFiche(bean)) {
                YvsComCategoriePersonnel entity = buildCategoriePerso(bean);
                if (!bean.isUpdate()) {
                    entity.setId(null);
                    entity = (YvsComCategoriePersonnel) dao.save1(entity);
                    bean.setId(entity.getId());
                    categoriePerso.setId(entity.getId());
                    categories.add(0, entity);
                    categoriesParent.add(0, entity);
                } else {
                    dao.update(entity);
                    categories.set(categories.indexOf(entity), entity);
                    if (categoriesParent.contains(entity)) {
                        categoriesParent.set(categoriesParent.indexOf(entity), entity);
                    }
                }
                bean.setUpdate(true);
                categoriePerso.setUpdate(bean.isUpdate());
                succes();
                resetFiche();
                update("data_categorie_perso");
                update("select_parent_categorie_perso");
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
                    YvsComCategoriePersonnel bean = categories.get(categories.indexOf(new YvsComCategoriePersonnel(id)));
                    dao.delete(bean);
                    categories.remove(bean);
                    categoriesParent.remove(bean);
                }
                succes();
                update("data_categorie_perso");
                update("select_parent_categorie_perso");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsComCategoriePersonnel y) {
        categorieSelect = y;
    }

    public void deleteBean_() {
        try {
            if (categorieSelect != null) {
                dao.delete(categorieSelect);
                categories.remove(categorieSelect);
                categoriesParent.remove(categorieSelect);
                resetFiche();
                succes();
            } else {
                getErrorMessage("Vous devez selectionner la catégorie");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComCategoriePersonnel bean = (YvsComCategoriePersonnel) ev.getObject();
            populateView(UtilCom.buildBeanCategoriePerso(bean));
            if (categoriesParent.contains(bean)) {
                categoriesParent.remove(bean);
            }
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        YvsComCategoriePersonnel bean = (YvsComCategoriePersonnel) ev.getObject();
        if (!categoriesParent.contains(bean)) {
            categoriesParent.add(bean);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
