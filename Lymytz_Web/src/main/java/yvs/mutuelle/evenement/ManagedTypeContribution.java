/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.evenement;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.mutuelle.evenement.YvsMutActiviteType;
import yvs.mutuelle.UtilMut;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedTypeContribution extends Managed<TypeContribution, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{typeContribution}")
    private TypeContribution typeContribution;
    private List<YvsMutActiviteType> types;

    private String tabIds, input_reset;
    private boolean updateTypeContribution;

    public ManagedTypeContribution() {
        types = new ArrayList<>();
    }

    public TypeContribution getTypeContribution() {
        return typeContribution;
    }

    public void setTypeContribution(TypeContribution typeContribution) {
        this.typeContribution = typeContribution;
    }

    public List<YvsMutActiviteType> getTypes() {
        return types;
    }

    public void setTypes(List<YvsMutActiviteType> types) {
        this.types = types;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getInput_reset() {
        return input_reset;
    }

    public void setInput_reset(String input_reset) {
        this.input_reset = input_reset;
    }

    public boolean isUpdateTypeContribution() {
        return updateTypeContribution;
    }

    public void setUpdateTypeContribution(boolean updateTypeContribution) {
        this.updateTypeContribution = updateTypeContribution;
    }

    @Override
    public void loadAll() {

        loadAllTypeContribution();
        tabIds = "";
    }

    public void loadAllTypeContribution() {
        types = dao.loadNameQueries("YvsMutActiviteType.findByMutuelle", new String[]{"mutuelle"}, new Object[]{currentMutuel});
    }

    public YvsMutActiviteType buildTypeContribution(TypeContribution y) {
        YvsMutActiviteType t = new YvsMutActiviteType();
        if (y != null) {
            t.setDescription(y.getDescription());
            t.setId(y.getId());
            t.setLibelle(y.getLibelle());
            t.setMutuelle(currentMutuel);
            t.setAuthor(currentUser);
            t.setDateSave(y.getDateSave());
            t.setDateUpdate(new Date());
        }
        return t;
    }

    @Override
    public TypeContribution recopieView() {
        TypeContribution t = new TypeContribution();
        t.setDescription(typeContribution.getDescription());
        t.setId(typeContribution.getId());
        t.setLibelle(typeContribution.getLibelle());
        t.setMutuelle(UtilMut.buildBeanMutuelle(currentMutuel));
        return t;
    }

    @Override
    public boolean controleFiche(TypeContribution bean) {
        if (bean.getLibelle() == null || bean.getLibelle().equals("")) {
            getErrorMessage("Vous devez entrer le libelle");
            return false;
        }
        if (currentMutuel != null ? currentMutuel.getId() < 1 : true) {
            getErrorMessage("Vous devez etre connecté dans une mutuelle");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(TypeContribution bean) {
        cloneObject(typeContribution, bean);
        setUpdateTypeContribution(true);
    }

    @Override
    public void resetFiche() {
        resetFiche(typeContribution);

        tabIds = "";
        input_reset = "";
        setUpdateTypeContribution(false);
    }

    @Override
    public boolean saveNew() {
        if (input_reset != null && input_reset.equals("reset")) {
            setUpdateTypeContribution(false);
            input_reset = "";
        }
        String action = isUpdateTypeContribution() ? "Modification" : "Insertion";
        try {
            TypeContribution bean = recopieView();
            bean.setNew_(true);
            if (controleFiche(bean)) {
                YvsMutActiviteType entity = buildTypeContribution(bean);
                if (!isUpdateTypeContribution()) {
                    entity.setId(null);
                    entity = (YvsMutActiviteType) dao.save1(entity);
                    types.add(0, entity);
                } else {
                    dao.update(entity);
                    types.set(types.indexOf(entity), entity);
                }
                succes();
                resetFiche();
            }
        } catch (Exception ex) {
            getException("Error " + action + " : " + ex.getMessage(), ex);
            getErrorMessage(action + " Impossible !");
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? tabIds.length() > 0 : false) {
                String[] ids = tabIds.split("-");
                if ((ids != null) ? ids.length > 0 : false) {
                    for (String s : ids) {
                        long id = Integer.valueOf(s);
                        YvsMutActiviteType bean = types.get(types.indexOf(new YvsMutActiviteType(id)));
                        dao.delete(new YvsMutActiviteType(bean.getId()));
                        types.remove(bean);
                    }
                    resetFiche();
                    succes();
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    public void deleteBean(YvsMutActiviteType y) {
        try {
            if (y != null) {
                dao.delete(y);
                types.remove(y);
                if (y.getId() == typeContribution.getId()) {
                    resetFiche();
                }
                succes();
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    @Override
    public void updateBean() {
        if ((tabIds != null) ? tabIds.length() > 0 : false) {
            String[] ids = tabIds.split("-");
            setUpdateTypeContribution((ids != null) ? ids.length > 0 : false);
            if (isUpdateTypeContribution()) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                YvsMutActiviteType bean = types.get(types.indexOf(new YvsMutActiviteType(id)));
                populateView(UtilMut.buildBeanTypeContribution(bean));
                tabIds = "";
                input_reset = "";
                update("blog_form_type_contribution_");
            }
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutActiviteType bean = (YvsMutActiviteType) ev.getObject();
            populateView(UtilMut.buildBeanTypeContribution(bean));
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

}
