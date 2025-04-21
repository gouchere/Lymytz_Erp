/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.param.YvsBaseTypeEtat;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedTypeEtat extends Managed<TypeEtat, YvsBaseTypeEtat> implements Serializable {

    @ManagedProperty(value = "#{typeEtat}")
    private TypeEtat typeEtat;
    private List<YvsBaseTypeEtat> types;
    private YvsBaseTypeEtat selectType;

    private String tabIds;

    public ManagedTypeEtat() {
        types = new ArrayList<>();
    }

    public TypeEtat getTypeEtat() {
        return typeEtat;
    }

    public void setTypeEtat(TypeEtat typeEtat) {
        this.typeEtat = typeEtat;
    }

    public List<YvsBaseTypeEtat> getTypes() {
        return types;
    }

    public void setTypes(List<YvsBaseTypeEtat> types) {
        this.types = types;
    }

    public YvsBaseTypeEtat getSelectType() {
        return selectType;
    }

    public void setSelectType(YvsBaseTypeEtat selectType) {
        this.selectType = selectType;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        types = dao.loadNameQueries("YvsBaseTypeEtat.findAll", champ, val);
    }

    public void loadAll(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        types = paginator.executeDynamicQuery("YvsBaseTypeEtat", "y.reference", avance, init, (int) imax, dao);
    }

    public void init(boolean next) {
        loadAll(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAll(true, true);
    }

    @Override
    public boolean controleFiche(TypeEtat bean) {
        if (bean.getLibelle() == null || bean.getLibelle().trim().length() < 1) {
            getErrorMessage("Vous devez preciser le libelle");
            return false;
        }
        champ = new String[]{"libelle", "societe"};
        val = new Object[]{bean.getLibelle(), currentAgence.getSociete()};
        YvsBaseTypeEtat y = (YvsBaseTypeEtat) dao.loadOneByNameQueries("YvsBaseTypeEtat.findByLibelle", champ, val);
        if (y != null ? (y.getId() != null ? !y.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez deja inseré ce type d'etat");
            return false;
        }
        return true;
    }

    @Override
    public TypeEtat recopieView() {
        TypeEtat s = new TypeEtat();
        s.setId(typeEtat.getId());
        s.setDescription(typeEtat.getDescription());
        s.setLibelle(typeEtat.getLibelle());
        return s;
    }

    @Override
    public void populateView(TypeEtat bean) {
        cloneObject(typeEtat, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(typeEtat);
        tabIds = "";
        selectType = null;
    }

    @Override
    public boolean saveNew() {
        String action = typeEtat.getId() > 0 ? "Modification" : "Insertion";
        try {
            TypeEtat bean = recopieView();
            if (controleFiche(bean)) {
                YvsBaseTypeEtat y = UtilParam.buildTypeEtat(bean, currentAgence.getSociete(), currentUser);
                if (y.getId() != null ? y.getId() > 0 : false) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsBaseTypeEtat) dao.save1(y);
                    typeEtat.setId(y.getId());
                }
                int idx = types.indexOf(y);
                if (idx > -1) {
                    types.set(idx, y);
                } else {
                    types.add(0, y);
                    if (types.size() > imax) {
                        types.remove(types.size() - 1);
                    }
                }
                resetFiche();
                succes();
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error " + action, ex);
            return false;
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (tabIds != null ? tabIds.trim().length() > 0 : false) {
                String[] ids = tabIds.trim().split("-");
                for (String o : ids) {
                    long id = Long.valueOf(o);
                    dao.delete(new YvsBaseTypeEtat((int) id));
                    int idx = types.indexOf(new YvsBaseTypeEtat((int) id));
                    if (idx > -1) {
                        types.remove(idx);
                    }
                    if (id == typeEtat.getId()) {
                        resetFiche();
                    }
                }
                succes();
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    public void deleteBean_(YvsBaseTypeEtat y) {
        selectType = y;
    }

    public void deleteBean_() {
        try {
            if (selectType != null ? selectType.getId() > 0 : false) {
                dao.delete(selectType);
                int idx = types.indexOf(selectType);
                if (idx > -1) {
                    types.remove(idx);
                }
                if (selectType.getId() == typeEtat.getId()) {
                    resetFiche();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBaseTypeEtat y = (YvsBaseTypeEtat) ev.getObject();
            populateView(UtilParam.buildBeanTypeEtat(y));
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

}
