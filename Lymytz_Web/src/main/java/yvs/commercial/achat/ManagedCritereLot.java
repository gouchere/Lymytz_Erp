/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.achat;

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
import yvs.entity.commercial.YvsComCritereLot;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedCritereLot extends Managed<CritereLot, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{critereLot}")
    private CritereLot critereLot;
    private List<YvsComCritereLot> criteres;
    private YvsComCritereLot critereSelect;

    private String tabIds;

    public ManagedCritereLot() {
        criteres = new ArrayList<>();
    }

    public YvsComCritereLot getCritereSelect() {
        return critereSelect;
    }

    public void setCritereSelect(YvsComCritereLot critereSelect) {
        this.critereSelect = critereSelect;
    }

    public CritereLot getCritereLot() {
        return critereLot;
    }

    public void setCritereLot(CritereLot critereLot) {
        this.critereLot = critereLot;
    }

    public List<YvsComCritereLot> getCriteres() {
        return criteres;
    }

    public void setCriteres(List<YvsComCritereLot> criteres) {
        this.criteres = criteres;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        loadAllCritere();
    }

    public void loadAllCritere() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        criteres = dao.loadNameQueries("YvsComCritereLot.findAll", champ, val);
    }

    @Override
    public boolean controleFiche(CritereLot bean) {
        if (bean.getValeur() == null || bean.getValeur().equals("")) {
            getErrorMessage("Vous devez entrer la valeur");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(CritereLot bean) {
        cloneObject(critereLot, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(critereLot);
        tabIds = "";
    }

    @Override
    public boolean saveNew() {
        String action = critereLot.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(critereLot)) {
                YvsComCritereLot entity = UtilCom.buildCritereLot(critereLot, currentUser, currentAgence.getSociete());
                if (critereLot.getId() < 1) {
                    entity.setId(null);
                    entity = (YvsComCritereLot) dao.save1(entity);
                    critereLot.setId(entity.getId());
                    criteres.add(0, entity);
                } else {
                    dao.update(entity);
                    criteres.set(criteres.indexOf(entity), entity);
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
                    YvsComCritereLot bean = criteres.get(criteres.indexOf(new YvsComCritereLot(id)));
                    dao.delete(bean);
                    criteres.remove(bean);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBean_(YvsComCritereLot y) {
        critereSelect = y;
    }

    public void deleteBean_() {
        try {
            if (critereSelect != null) {
                dao.delete(critereSelect);
                criteres.remove(critereSelect);
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
            YvsComCritereLot bean = (YvsComCritereLot) ev.getObject();
            populateView(UtilCom.buildBeanCritereLot(bean));
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void activeCritere(YvsComCritereLot bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            dao.update(bean);
            criteres.set(criteres.indexOf(bean), bean);
        }
    }

    public void activeComparable(YvsComCritereLot bean) {
        if (bean != null) {
            bean.setComparable(!bean.getComparable());
            dao.update(bean);
            criteres.set(criteres.indexOf(bean), bean);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
