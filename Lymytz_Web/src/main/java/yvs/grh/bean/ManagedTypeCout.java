/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.Comptes;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.UtilCompta;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.grh.UtilGrh;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author LYMYTZ
 */
@SessionScoped
@ManagedBean
public class ManagedTypeCout extends Managed<TypeCout, YvsGrhTypeCout> implements Serializable {

    private TypeCout typeCout = new TypeCout();
    private YvsGrhTypeCout selectType;
    private List<YvsGrhTypeCout> types;

    private String tabIds, module;
    private String natureSearch;

    public ManagedTypeCout() {
        types = new ArrayList<>();
    }

    public TypeCout getTypeCout() {
        return typeCout;
    }

    public void setTypeCout(TypeCout typeCout) {
        this.typeCout = typeCout;
    }

    public YvsGrhTypeCout getSelectType() {
        return selectType;
    }

    public void setSelectType(YvsGrhTypeCout selectType) {
        this.selectType = selectType;
    }

    public List<YvsGrhTypeCout> getTypes() {
        return types;
    }

    public void setTypes(List<YvsGrhTypeCout> types) {
        this.types = types;
    }

    public String getNatureSearch() {
        return natureSearch;
    }

    public void setNatureSearch(String natureSearch) {
        this.natureSearch = natureSearch;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        module = null;
        loadAllType(true, true);
    }

    public void loadAll(String module) {
        loadTypeByModule(module);
    }

    public void loadTypeByModule(String module) {
        this.module = module;
        List<String> modules = new ArrayList<>();
        for (SelectItem m : Constantes.COUTS(module)) {
            modules.add(m.getValue().toString());
        }
        champ = new String[]{"societe", "module"};
        val = new Object[]{currentAgence.getSociete(), modules};
        types = dao.loadNameQueries("YvsGrhTypeCout.findByModules", champ, val);
    }

    public void loadTypeByType(String type) {
        if (Util.asString(type)) {
            champ = new String[]{"societe", "module"};
            val = new Object[]{currentAgence.getSociete(), type};
            nameQueri = "YvsGrhTypeCout.findByModule";
        } else {
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
            nameQueri = "YvsGrhTypeCout.findAll";
        }
        types = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadAllType(String module) {
        paginator.addParam(new ParametreRequete("y.moduleCout", "module", module, "=", "AND"));
        loadAllType(true, true);
    }

    public void loadAllType(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        types = paginator.executeDynamicQuery("YvsGrhTypeCout", "y.libelle", avance, init, (int) imax, dao);
    }

    public void init(boolean next) {
        loadAllType(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllType(true, true);
    }

    @Override
    public boolean controleFiche(TypeCout bean) {
        if (bean.getLibelle() == null || bean.getLibelle().trim().equals("")) {
            getErrorMessage("Vous devez précisez le libelle");
            return false;
        }
        if (selectType != null ? (selectType.getId() != null ? selectType.getId() > 0 : false) : false) {
            if (bean.isUpdate() && !bean.getModule().equals(selectType.getModuleCout())) {
                bean.setUpdate(false);
            }
        }
        return true;
    }

    @Override
    public void populateView(TypeCout bean) {
        cloneObject(typeCout, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(typeCout);
        typeCout.setModule(Constantes.COUT_MIXTE);
        typeCout.setCompte(new Comptes());
        selectType = new YvsGrhTypeCout();
        tabIds = "";
    }

    @Override
    public boolean saveNew() {
        String action = typeCout.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(typeCout)) {
                selectType = UtilGrh.buildTypeCout(typeCout, currentAgence.getSociete(), currentUser);
                if (typeCout.getId() < 1) {
                    selectType.setId(null);
                    selectType.setDateSave(new Date());
                    selectType = (YvsGrhTypeCout) dao.save1(selectType);
                    typeCout.setId(selectType.getId());
                } else {
                    dao.update(selectType);
                }
                int idx = types.indexOf(selectType);
                if (idx < 1) {
                    types.add(0, selectType);
                } else {
                    types.set(idx, selectType);
                }
                actionOpenOrResetAfter(this);
                succes();
                return true;
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException(action + " Impossible : " + ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                String[] tab = tabIds.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsGrhTypeCout bean = types.get(types.indexOf(new YvsGrhTypeCout(id)));
                    dao.delete(bean);
                    types.remove(bean);
                    if (id == typeCout.getId()) {
                        resetFiche();
                    }
                }
                succes();
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsGrhTypeCout y) {
        selectType = y;
    }

    public void deleteBean_() {
        try {
            if (selectType != null) {
                dao.delete(selectType);
                types.remove(selectType);
                if (selectType.getId() == typeCout.getId()) {
                    resetFiche();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectType = (YvsGrhTypeCout) ev.getObject();
            populateView(UtilGrh.buildBeanTypeCout(selectType));
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewCompte(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBasePlanComptable bean = (YvsBasePlanComptable) ev.getObject();
            typeCout.setCompte(UtilCompta.buildBeanCompte(bean));
            update(":txt_compte_type_cout_com");
        }
    }

    public void searchCompte() {
        searchCompte(typeCout);
    }

    public void searchCompte(TypeCout art) {
        //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
        ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (service != null) {
            service.findCompteByNum(art.getCompte().getNumCompte());
            art.getCompte().setError(service.getListComptes().isEmpty());
            if (service.getListComptes() != null) {
                if (!service.getListComptes().isEmpty()) {
                    if (service.getListComptes().size() == 1) {
                        art.getCompte().setError(false);
                        art.setCompte(UtilCompta.buildBeanCompte(service.getListComptes().get(0)));
                    } else {
                        openDialog("dlgListComptes");
                        update("data_comptes_type_cout_base");
                        update("data_comptes_type_cout_com");
                    }
                } else {
                    art.getCompte().setError(true);
                }
            } else {
                art.getCompte().setError(true);
            }
        }

    }

    public void initComptes() {
        update("data_comptes_type_cout_base");
        update("data_comptes_type_cout_com");
    }

    public void chooseType(ValueChangeEvent ev) {
        if (ev != null) {
            String type = (String) ev.getNewValue();
            if (type != null ? type.trim().length() > 0 : false) {
                typeCout.setModule(type);
                loadTypeByType(type);
            } else {
                if (module != null ? module.trim().length() > 0 : false) {
                    loadAll(module);
                } else {
                    loadAll();
                }
            }
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
