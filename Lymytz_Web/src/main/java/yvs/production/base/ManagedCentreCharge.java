/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.production.UtilProd;
import yvs.dao.Options;
import yvs.entity.production.base.YvsProdCentreCharge;
import yvs.entity.production.base.YvsProdSiteProduction;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedCentreCharge extends Managed<CentreCharge, YvsProdCentreCharge> implements Serializable {
    
    @ManagedProperty(value = "#{centreCharge}")
    private CentreCharge centreCharge;
    private List<YvsProdCentreCharge> centres;
    private YvsProdCentreCharge selectCentre;
    
    private String tabIds;
    
    public ManagedCentreCharge() {
        centres = new ArrayList<>();
    }
    
    public CentreCharge getCentreCharge() {
        return centreCharge;
    }
    
    public void setCentreCharge(CentreCharge centreCharge) {
        this.centreCharge = centreCharge;
    }
    
    public List<YvsProdCentreCharge> getCentres() {
        return centres;
    }
    
    public void setCentres(List<YvsProdCentreCharge> centres) {
        this.centres = centres;
    }
    
    public YvsProdCentreCharge getSelectCentre() {
        return selectCentre;
    }
    
    public void setSelectCentre(YvsProdCentreCharge selectCentre) {
        this.selectCentre = selectCentre;
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
        centres = dao.loadNameQueries("YvsProdCentreCharge.findAll", champ, val);
    }
    
    public void loadAll(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.siteProduction.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        centres = paginator.executeDynamicQuery("YvsProdCentreCharge", "y.reference", avance, init, (int) imax, dao);
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
    public boolean controleFiche(CentreCharge bean) {
        if (bean.getReference() == null || bean.getReference().trim().length() < 1) {
            getErrorMessage("Vous devez preciser la reference");
            return false;
        }
        if (bean.getSiteProduction() != null ? bean.getSiteProduction().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le site de production");
            return false;
        }
        champ = new String[]{"reference", "societe"};
        val = new Object[]{bean.getReference(), currentAgence.getSociete()};
        YvsProdCentreCharge y = (YvsProdCentreCharge) dao.loadOneByNameQueries("YvsProdCentreCharge.findByReference", champ, val);
        if (y != null ? (y.getId() != null ? !y.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez deja inseré ce centre");
            return false;
        }
        return true;
    }
    
    @Override
    public CentreCharge recopieView() {
        CentreCharge s = new CentreCharge();
        s.setId(centreCharge.getId());
        s.setActif(centreCharge.isActif());
        s.setDescription(centreCharge.getDescription());
        s.setDesignation(centreCharge.getDesignation());
        s.setReference(centreCharge.getReference());
        ManagedSiteProduction m = (ManagedSiteProduction) giveManagedBean(ManagedSiteProduction.class);
        if (m != null ? m.getSites().contains(new YvsProdSiteProduction(centreCharge.getSiteProduction().getId())) : false) {
            centreCharge.setSiteProduction(UtilProd.buildBeanSiteProduction(m.getSites().get(m.getSites().indexOf(new YvsProdSiteProduction(centreCharge.getSiteProduction().getId())))));
        }
        s.setSiteProduction(centreCharge.getSiteProduction());
        return s;
    }
    
    @Override
    public void populateView(CentreCharge bean) {
        cloneObject(centreCharge, bean);
    }
    
    @Override
    public void resetFiche() {
        resetFiche(centreCharge);
        centreCharge.setSiteProduction(new SiteProduction());
        tabIds = "";
        selectCentre = null;
    }
    
    @Override
    public boolean saveNew() {
        String action = centreCharge.getId() > 0 ? "Modification" : "Insertion";
        try {
            CentreCharge bean = recopieView();
            if (controleFiche(bean)) {
                YvsProdCentreCharge y = UtilProd.buildCentreCharge(bean, currentUser);
                if (y.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsProdCentreCharge) dao.save1(y);
                    centreCharge.setId(y.getId());
                }
                int idx = centres.indexOf(y);
                if (idx > -1) {
                    centres.set(idx, y);
                } else {
                    centres.add(0, y);
                    if (centres.size() > imax) {
                        centres.remove(centres.size() - 1);
                    }
                }
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
                    dao.delete(new YvsProdCentreCharge((int) id));
                    int idx = centres.indexOf(new YvsProdCentreCharge((int) id));
                    if (idx > -1) {
                        centres.remove(idx);
                    }
                    if (id == centreCharge.getId()) {
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
    
    public void deleteBean_(YvsProdCentreCharge y) {
        selectCentre = y;
    }
    
    public void deleteBean_() {
        try {
            if (selectCentre != null ? selectCentre.getId() > 0 : false) {
                dao.delete(selectCentre);
                int idx = centres.indexOf(selectCentre);
                if (idx > -1) {
                    centres.remove(idx);
                }
                if (selectCentre.getId() == centreCharge.getId()) {
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
            YvsProdCentreCharge y = (YvsProdCentreCharge) ev.getObject();
            populateView(UtilProd.buildBeanCentreCharge(y));
        }
    }
    
    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }
    
    public void setActive(YvsProdCentreCharge y) {
        y.setActif(!y.getActif());
        String rq = "UPDATE yvs_prod_centre_charge SET actif=" + y.getActif() + " WHERE id=?";
        Options[] param = new Options[]{new Options(y.getId(), 1)};
        dao.requeteLibre(rq, param);
        centres.set(centres.indexOf(y), y);
    }
    
}
