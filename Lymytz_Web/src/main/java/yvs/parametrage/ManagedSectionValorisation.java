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
import yvs.base.compta.CentreAnalytique;
import yvs.base.compta.Comptes;
import yvs.base.compta.ManagedCentreAnalytique;
import yvs.base.compta.UtilCompta;
import yvs.dao.Options;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.param.YvsBaseCentreValorisation;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedSectionValorisation extends Managed<SectionDeValorisation, YvsBaseCentreValorisation> implements Serializable {

    @ManagedProperty(value = "#{sectionDeValorisation}")
    private SectionDeValorisation sectionDeValorisation;
    private List<YvsBaseCentreValorisation> centres;
    private YvsBaseCentreValorisation selectCentre;

    private String tabIds;

    public ManagedSectionValorisation() {
        centres = new ArrayList<>();
    }

    public SectionDeValorisation getSectionDeValorisation() {
        return sectionDeValorisation;
    }

    public void setSectionDeValorisation(SectionDeValorisation sectionDeValorisation) {
        this.sectionDeValorisation = sectionDeValorisation;
    }

    public List<YvsBaseCentreValorisation> getCentres() {
        return centres;
    }

    public void setCentres(List<YvsBaseCentreValorisation> centres) {
        this.centres = centres;
    }

    public YvsBaseCentreValorisation getSelectCentre() {
        return selectCentre;
    }

    public void setSelectCentre(YvsBaseCentreValorisation selectCentre) {
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
        centres = dao.loadNameQueries("YvsBaseCentreValorisation.findAll", champ, val);
    }

    public void loadAll(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        centres = paginator.executeDynamicQuery("YvsBaseCentreValorisation", "y.reference", avance, init, (int) imax, dao);
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
    public boolean controleFiche(SectionDeValorisation bean) {
        if (bean.getReference() == null || bean.getReference().trim().length() < 1) {
            getErrorMessage("Vous devez preciser la reference");
            return false;
        }
        champ = new String[]{"reference", "societe"};
        val = new Object[]{bean.getReference(), currentAgence.getSociete()};
        YvsBaseCentreValorisation y = (YvsBaseCentreValorisation) dao.loadOneByNameQueries("YvsBaseCentreValorisation.findByReference", champ, val);
        if (y != null ? (y.getId() != null ? !y.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez deja inseré ce centre de valorisation");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(SectionDeValorisation bean) {
        cloneObject(sectionDeValorisation, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(sectionDeValorisation);
        sectionDeValorisation.setCentre(new CentreAnalytique());
        sectionDeValorisation.setCompte(new Comptes());
        tabIds = "";
        selectCentre = new YvsBaseCentreValorisation();
    }

    @Override
    public boolean saveNew() {
        String action = sectionDeValorisation.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(sectionDeValorisation)) {
                YvsBaseCentreValorisation y = UtilParam.buildCentreValorisation(sectionDeValorisation, currentUser, currentAgence.getSociete());
                if (y.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsBaseCentreValorisation) dao.save1(y);
                    sectionDeValorisation.setId(y.getId());
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
                    dao.delete(new YvsBaseCentreValorisation((int) id));
                    int idx = centres.indexOf(new YvsBaseCentreValorisation((int) id));
                    if (idx > -1) {
                        centres.remove(idx);
                    }
                    if (id == sectionDeValorisation.getId()) {
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

    public void deleteBean_(YvsBaseCentreValorisation y) {
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
                if (selectCentre.getId() == sectionDeValorisation.getId()) {
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
            selectCentre = (YvsBaseCentreValorisation) ev.getObject();
            populateView(UtilParam.buildBeanCentreValorisation(selectCentre));
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void setActive(YvsBaseCentreValorisation y) {
        y.setActif(!y.getActif());
        String rq = "UPDATE yvs_base_centre_valorisation SET actif=" + y.getActif() + " WHERE id=?";
        Options[] param = new Options[]{new Options(y.getId(), 1)};
        dao.requeteLibre(rq, param);
        centres.set(centres.indexOf(y), y);
    }

    public void chooseCentre() {
        if (sectionDeValorisation.getCentre() != null ? sectionDeValorisation.getCentre().getId() > 0 : false) {
            ManagedCentreAnalytique w = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
            if (w != null) {
                int idx = w.getSecondaires().indexOf(new YvsComptaCentreAnalytique(sectionDeValorisation.getCentre().getId()));
                if (idx > -1) {
                    YvsComptaCentreAnalytique y = w.getSecondaires().get(idx);
                    sectionDeValorisation.setCentre(UtilCompta.buildBeanCentreAnalytique(y));
                }
            }
        }
    }

}
