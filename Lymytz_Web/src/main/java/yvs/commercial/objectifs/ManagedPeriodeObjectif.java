/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.objectifs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.commercial.UtilCom;
import yvs.entity.commercial.objectifs.YvsComPeriodeObjectif;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedPeriodeObjectif extends Managed<PeriodesObjectifs, YvsComPeriodeObjectif> implements Serializable {

    private PeriodesObjectifs periode = new PeriodesObjectifs();
    private List<YvsComPeriodeObjectif> periodes;
    private YvsComPeriodeObjectif selectedPeriode;
    private String tabIds;

    public ManagedPeriodeObjectif() {
        periodes = new ArrayList<>();
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public PeriodesObjectifs getPeriode() {
        return periode;
    }

    public void setPeriode(PeriodesObjectifs periode) {
        this.periode = periode;
    }

    public List<YvsComPeriodeObjectif> getPeriodes() {
        return periodes;
    }

    public void setPeriodes(List<YvsComPeriodeObjectif> periodes) {
        this.periodes = periodes;
    }

    public YvsComPeriodeObjectif getSelectedPeriode() {
        return selectedPeriode;
    }

    public void setSelectedPeriode(YvsComPeriodeObjectif selectedPeriode) {
        this.selectedPeriode = selectedPeriode;
    }

    @Override
    public boolean controleFiche(PeriodesObjectifs bean) {
        if ((bean.getDebut() == null)) {
            getErrorMessage("Vous devez entrer la date de début !");
            return false;
        }
        if ((bean.getFin() == null)) {
            getErrorMessage("Vous devez entrer la date de fin !");
            return false;
        }
        if (bean.isPeriodMonth()) {
            bean.setReference(giveReferenceByDate(bean.getFin()));
        }
        if (bean.getReference() == null) {
            getErrorMessage("Précisé une référence de période");
            return false;
        }
        if (bean.getDebut().after(bean.getFin()) || bean.getDebut().equals(bean.getFin())) {
            getErrorMessage("Les valeurs de date sont incohérentes !");
            return false;
        }
        return true;
    }

    @Override
    public void loadAll() {
        periodes = dao.loadNameQueries("YvsComPeriodeObjectif.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    @Override
    public PeriodesObjectifs recopieView() {
        PeriodesObjectifs pe = new PeriodesObjectifs();
        pe.setDebut(periode.getDebut());
        pe.setFin(periode.getFin());
        pe.setReference(giveReferenceByDate(periode.getFin()));
        pe.setId(periode.getId());
        return periode;
    }

    @Override
    public void populateView(PeriodesObjectifs bean) {
        cloneObject(periode, bean);
    }

    @Override
    public void deleteBean() {
        try {
            if (periode.getId() >= 0) {
                dao.delete(new YvsComPeriodeObjectif(periode.getId()));
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible !");
            getException("Lymytz-Error >>>>>", ex);
        }
    }

    public void selectToDelete(YvsComPeriodeObjectif pe) {
        selectedPeriode = pe;
        openDialog("dlgDelPeriode");
    }

    public void deleteLinePeriode() {
        if ((selectedPeriode != null) ? selectedPeriode.getId() > 0 : false) {
            try {
                dao.delete(new YvsComPeriodeObjectif(selectedPeriode.getId()));
                periodes.remove(selectedPeriode);
                update("table_periodes_objectifVente");
            } catch (Exception ex) {
                getErrorMessage("Suppression impossible !");
                getException("Lymytz Error >>>", ex);
            }
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            YvsComPeriodeObjectif pe = (YvsComPeriodeObjectif) ev.getObject();
            selectOneLine(pe);
        }
    }

    public void selectOneLine(YvsComPeriodeObjectif obj) {
        populateView(UtilCom.buildBeanPeriode(obj));
        update("form_main_period_objectifV");
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        resetFiche(periode);
        update("form_main_period_objectifV");
    }

    @Override
    public boolean saveNew() {
        if (controleFiche(periode)) {
            YvsComPeriodeObjectif pe = UtilCom.buildBeanPeriode(recopieView());
            pe.setSociete(currentAgence.getSociete());
            pe.setAuthor(currentUser);
            if (pe.getId() > 0) {
                pe.setDateUpdate(new Date());
                pe.setAuthor(currentUser);
                dao.update(pe);
                int idx = periodes.indexOf(pe);
                if (idx >= 0) {
                    periodes.set(idx, pe);
                }
            } else {
                pe.setId(null);
                pe.setDateSave(new Date());
                pe.setDateUpdate(new Date());
                pe = (YvsComPeriodeObjectif) dao.save1(pe);
                periode.setId(pe.getId());
                periodes.add(0, pe);
            }
            succes();
            resetFiche();
            update("table_periodes_objectifVente");
        }
        return true;
    }
    public boolean initForm = true;

    public void loadAllPeriodeObjectif(boolean avancer) {
        loadAllPeriodeObjectif(avancer, initForm);
    }

    public void loadAllPeriodeObjectif(boolean avancer, boolean init) {
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        periodes = paginator.executeDynamicQuery("YvsComPeriodeObjectif", "y.dateDebut DESC", avancer, init, (int) imax, dao);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComPeriodeObjectif> re = paginator.parcoursDynamicData("YvsComPeriodeObjectif", "y", "y.dateDebut DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            selectOneLine(re.get(0));
        }
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
    }

    private String giveReferenceByDate(Date d) {
        String ref = "";
        Calendar date = Calendar.getInstance();
        date.setTime(d);
        int m = date.get(Calendar.MONTH);
        int y = date.get(Calendar.YEAR);
        ref = (Constantes.tabMois[m] + " " + y);
        return ref;
    }
}
