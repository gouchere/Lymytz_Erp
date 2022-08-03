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
import yvs.base.compta.CategorieComptable;
import yvs.dao.Options;
import yvs.dao.Util;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.util.Constantes;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedCatCompt extends Managed<CategorieComptable, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{categorieComptable}")
    private CategorieComptable categorieComptable;
    private List<YvsBaseCategorieComptable> categories, categoriesVente, categoriesAchat;
    private YvsBaseCategorieComptable categorieSelect;
    private String tabIds;

    public ManagedCatCompt() {
        categories = new ArrayList<>();
        categoriesVente = new ArrayList<>();
        categoriesAchat = new ArrayList<>();
    }

    public List<YvsBaseCategorieComptable> getCategoriesVente() {
        return categoriesVente;
    }

    public void setCategoriesVente(List<YvsBaseCategorieComptable> categoriesVente) {
        this.categoriesVente = categoriesVente;
    }

    public List<YvsBaseCategorieComptable> getCategoriesAchat() {
        return categoriesAchat;
    }

    public void setCategoriesAchat(List<YvsBaseCategorieComptable> categoriesAchat) {
        this.categoriesAchat = categoriesAchat;
    }

    public YvsBaseCategorieComptable getCategorieSelect() {
        return categorieSelect;
    }

    public void setCategorieSelect(YvsBaseCategorieComptable categorieSelect) {
        this.categorieSelect = categorieSelect;
    }

    public List<YvsBaseCategorieComptable> getCategories() {
        return categories;
    }

    public void setCategories(List<YvsBaseCategorieComptable> categories) {
        this.categories = categories;
    }

    public CategorieComptable getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(CategorieComptable planRemise) {
        this.categorieComptable = planRemise;
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
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        categories = dao.loadNameQueries("YvsBaseCategorieComptable.findAll", champ, val);
    }

    public void loadAllCategorie(String nature) {
        champ = new String[]{"societe", "nature"};
        val = new Object[]{currentAgence.getSociete(), nature};
        categories = dao.loadNameQueries("YvsBaseCategorieComptable.findByNature", champ, val);
    }

    public void loadAllCategorieVente() {
        champ = new String[]{"societe", "nature"};
        val = new Object[]{currentAgence.getSociete(), Constantes.VENTE};
        categoriesVente = dao.loadNameQueries("YvsBaseCategorieComptable.findByNature", champ, val);
    }

    public void loadAllCategorieAchat() {
        champ = new String[]{"societe", "nature"};
        val = new Object[]{currentAgence.getSociete(), Constantes.ACHAT};
        categoriesAchat = dao.loadNameQueries("YvsBaseCategorieComptable.findByNature", champ, val);
    }

    public void loadComptable(String nature) {
        if (nature != null ? nature.trim().length() > 0 : false) {
            champ = new String[]{"societe", "nature"};
            val = new Object[]{currentAgence.getSociete(), nature};
            nameQueri = "YvsBaseCategorieComptable.findByNature";
        } else {
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
            nameQueri = "YvsBaseCategorieComptable.findAll";
        }
        categories = dao.loadNameQueries(nameQueri, champ, val);
    }

    @Override
    public CategorieComptable recopieView() {
        return categorieComptable;
    }

    @Override
    public boolean controleFiche(CategorieComptable bean) {
        if (bean.getCodeCategorie() == null || bean.getCodeCategorie().equals("")) {
            getErrorMessage("vous devez entrer le code de la categorie");
            return false;
        }
        if (!bean.isUpdate()) {
            YvsBaseCategorieComptable t = (YvsBaseCategorieComptable) dao.loadOneByNameQueries("YvsBaseCategorieComptable.findByCode", new String[]{"code", "societe"}, new Object[]{bean.getCodeCategorie(), currentUser.getAgence().getSociete()});
            if (t != null ? t.getId() > 0 : false) {
                getErrorMessage("Vous avez déja crée cette catégorie");
                return false;
            }
        }
        return true;
    }

    @Override
    public void populateView(CategorieComptable bean) {
        cloneObject(categorieComptable, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(categorieComptable);
        categorieComptable.setNature(Constantes.ACHAT);
        tabIds = "";
    }

    @Override
    public boolean saveNew() {
        return saveNew(null);
    }

    public boolean saveNew(String nature) {
        String action = categorieComptable.isUpdate() ? "Modification" : "Insertion";
        try {
            if (nature != null ? nature.trim().length() > 0 : false) {
                categorieComptable.setNature(nature);
            }
            CategorieComptable bean = recopieView();
            if (controleFiche(bean)) {
                YvsBaseCategorieComptable entity = UtilCom.buildCategorieComptable(bean, currentUser, currentAgence.getSociete());
                if (bean.getId() < 1) {
                    entity.setId(null);
                    entity = (YvsBaseCategorieComptable) dao.save1(entity);
                    categorieComptable.setId(entity.getId());
                } else {
                    dao.update(entity);
                }
                int idx = categories.indexOf(entity);
                if (idx > -1) {
                    categories.set(idx, entity);
                } else {
                    categories.add(entity);
                }
                if (nature != null) {
                    switch (nature) {
                        case Constantes.VENTE:
                            idx = categoriesVente.indexOf(entity);
                            if (idx > -1) {
                                categoriesVente.set(idx, entity);
                            } else {
                                categoriesVente.add(entity);
                            }
                            break;
                        case Constantes.ACHAT:
                            idx = categoriesAchat.indexOf(entity);
                            if (idx > -1) {
                                categoriesAchat.set(idx, entity);
                            } else {
                                categoriesAchat.add(entity);
                            }
                            break;
                        default:
                            break;
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
                    YvsBaseCategorieComptable bean = categories.get(categories.indexOf(new YvsBaseCategorieComptable(id)));
                    dao.delete(bean);
                    categories.remove(bean);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBean_(YvsBaseCategorieComptable y) {
        categorieSelect = y;
    }

    public void deleteBean_() {
        try {
            if (categorieSelect != null) {
                dao.delete(categorieSelect);
                categories.remove(categorieSelect);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseCategorieComptable bean = (YvsBaseCategorieComptable) ev.getObject();
            populateView(UtilCom.buildBeanCategorieComptable(bean));
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void activeCategorie(YvsBaseCategorieComptable bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            String rq = "UPDATE yvs_base_categorie_comptable SET actif=" + bean.getActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            categories.set(categories.indexOf(bean), bean);
            update("data_taxe_article");
        }
    }

    public CategorieComptable searchCategorieVente(String code, boolean open) {
        CategorieComptable a = new CategorieComptable();
        a.setCodeCategorie(code);
        a.setError(true);
        if (Util.asString(code)) {
            champ = new String[]{"code", "nature", "societe"};
            val = new Object[]{code + "%", Constantes.VENTE , currentAgence.getSociete()};
            categoriesVente = dao.loadNameQueries("YvsBaseCategorieComptable.findLikeCodeNature", champ, val);
            if (categoriesVente != null ? !categoriesVente.isEmpty() : false) {
                if (categoriesVente.size() == 1) {
                    a = UtilCom.buildBeanCategorieComptable(categoriesVente.get(0));
                } else if (open) {
                    openDialog("dlgListCategorieVente");
                }
            }
        }
        return a;
    }

    public CategorieComptable searchCategorieAchat(String code, boolean open) {
        CategorieComptable a = new CategorieComptable();
        a.setCodeCategorie(code);
        a.setError(true);
        if (Util.asString(code)) {
            champ = new String[]{"code", "nature", "societe"};
            val = new Object[]{code, Constantes.ACHAT, currentAgence.getSociete()};
            categoriesAchat = dao.loadNameQueries("YvsBaseCategorieComptable.findLikeCodeNature", champ, val);
            if (categoriesAchat != null ? !categoriesAchat.isEmpty() : false) {
                if (categoriesAchat.size() == 1) {
                    a = UtilCom.buildBeanCategorieComptable(categoriesAchat.get(0));
                } else if (open) {
                    openDialog("dlgListCategorieAchat");
                }
            }
        }
        return a;
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
