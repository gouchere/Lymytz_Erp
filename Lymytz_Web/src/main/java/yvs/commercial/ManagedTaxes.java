/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial;

import yvs.base.compta.Taxes;
import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.Comptes;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.UtilCompta;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedTaxes extends ManagedCommercial<Taxes, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{taxes}")
    private Taxes taxes;
    private List<YvsBaseTaxes> taxesList;
    private YvsBaseTaxes taxeSelect;
    private String tabIds;

    public ManagedTaxes() {
        taxesList = new ArrayList<>();
    }

    public YvsBaseTaxes getTaxeSelect() {
        return taxeSelect;
    }

    public void setTaxeSelect(YvsBaseTaxes taxeSelect) {
        this.taxeSelect = taxeSelect;
    }

    public List<YvsBaseTaxes> getTaxesList() {
        return taxesList;
    }

    public void setTaxesList(List<YvsBaseTaxes> taxes) {
        this.taxesList = taxes;
    }

    public Taxes getTaxes() {
        return taxes;
    }

    public void setTaxes(Taxes planRemise) {
        this.taxes = planRemise;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        loadAllTaxes();
        loadComptes();
    }

    public void loadAllTaxes() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        taxesList = dao.loadNameQueries("YvsBaseTaxes.findAll", champ, val);
    }

    public void loadAllTaxes(final String module) {
        if (Util.asString(module)) {
            List<String> modules = new ArrayList<String>(){
                {
                    add("M");
                    add(module);
                }
            };
            champ = new String[]{"societe", "modules"};
            val = new Object[]{currentAgence.getSociete(), modules};
            nameQueri = "YvsBaseTaxes.findByModules";
        } else {
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
            nameQueri = "YvsBaseTaxes.findByModules";
        }
        taxesList = dao.loadNameQueries(nameQueri, champ, val);
    }

    @Override
    public Taxes recopieView() {
        taxes.setNew_(true);
        return taxes;
    }

    @Override
    public boolean controleFiche(Taxes bean) {
        if (bean.getCodeTaxe() == null || bean.getCodeTaxe().equals("")) {
            getErrorMessage("vous devez entrer le code de la taxe");
            return false;
        }
        YvsBaseTaxes t = (YvsBaseTaxes) dao.loadOneByNameQueries("YvsBaseTaxes.findByCodeTaxe", new String[]{"code", "societe"}, new Object[]{bean.getCodeTaxe(), currentUser.getAgence().getSociete()});
        if (t != null ? (t.getId() != null ? !t.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez déja crée cette taxe");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(Taxes bean) {
        cloneObject(taxes, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(taxes);
        taxes.setCompte(new Comptes());
        tabIds = "";
        taxeSelect = new YvsBaseTaxes();
    }

    @Override
    public boolean saveNew() {
        YvsBaseTaxes y = saveReturn();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            resetFiche();
            succes();
            return true;
        }
        return false;
    }

    public YvsBaseTaxes saveReturn() {
        String action = taxes.isUpdate() ? "Modification" : "Insertion";
        try {
            Taxes bean = recopieView();
            if (controleFiche(bean)) {
                YvsBaseTaxes entity = UtilCom.buildTaxes(bean, currentAgence.getSociete(), currentUser);
                if (!bean.isUpdate()) {
                    entity.setId(null);
                    entity = (YvsBaseTaxes) dao.save1(entity);
                    taxes.setId(entity.getId());
                    taxesList.add(0, entity);
                } else {
                    dao.update(entity);
                    taxesList.set(taxesList.indexOf(entity), entity);
                }
                return entity;
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return null;
        }
        return null;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                String[] tab = tabIds.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsBaseTaxes bean = taxesList.get(taxesList.indexOf(new YvsBaseTaxes(id)));
                    dao.delete(bean);
                    taxesList.remove(bean);
                }
                succes();
                update("data_taxes");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBean_(YvsBaseTaxes y) {
        taxeSelect = y;
    }

    public void deleteBean_() {
        try {
            if (taxeSelect != null) {
                dao.delete(taxeSelect);
                taxesList.remove(taxeSelect);
                succes();
                update("data_taxes");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            taxeSelect = (YvsBaseTaxes) ev.getObject();
            populateView(UtilCom.buildBeanTaxes(taxeSelect));
            update("form_taxes");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("form_taxes");
    }

    public void loadOnViewCompte(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBasePlanComptable bean = (YvsBasePlanComptable) ev.getObject();
            taxes.setCompte(UtilCompta.buildBeanCompte(bean));
        }
    }

    public void activeTaxe(YvsBaseTaxes bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            String rq = "UPDATE yvs_base_taxes SET actif=" + bean.getActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            taxesList.set(taxesList.indexOf(bean), bean);
        }
    }

    public void searchCompte() {
        searchCompte(taxes);
    }

    public void searchCompte(Taxes art) {
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
                        openDialog("dlgListBaseComptes");
                        update("data_comptes_taxes_base");
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
        update("data_comptes_taxes_base");
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
