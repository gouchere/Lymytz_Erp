/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.th;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.param.YvsCrenauxHoraire;
import yvs.util.Managed;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "Mth")
@SessionScoped
public class ManagedCrenauxHoraire extends Managed<CrenauxHoraire, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{th}")
    private CrenauxHoraire creno;
    private List<CrenauxHoraire> listCreno;
    private List<CrenauxHoraire> listCrenoFilter;

    public ManagedCrenauxHoraire() {
        listCreno = new ArrayList<>();
    }

    public CrenauxHoraire getCreno() {
        return creno;
    }

    public void setCreno(CrenauxHoraire creno) {
        this.creno = creno;
    }

    public List<CrenauxHoraire> getListCreno() {
        return listCreno;
    }

    public void setListCreno(List<CrenauxHoraire> listCreno) {
        this.listCreno = listCreno;
    }

    public List<CrenauxHoraire> getListCrenoFilter() {
        return listCrenoFilter;
    }

    public void setListCrenoFilter(List<CrenauxHoraire> listCrenoFilter) {
        this.listCrenoFilter = listCrenoFilter;
    }

    @Override
    public boolean controleFiche(CrenauxHoraire bean) {
        if (bean.getCodeTranche() == null) {
            getMessage("Vous devez indiquer un code pour le créno", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getTypeDeJournee() == null) {
            getMessage("Vous devez Choisir le type de journée", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getHeureDeb() == null) {
            getMessage("Vous devez préciser l'heure de début", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getHeureFin()== null) {
            getMessage("Vous devez préciser l'heure de fin", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getHeureDeb().equals(bean.getHeureFin()) || bean.getHeureDeb().after(bean.getHeureFin())) {
            getMessage("L'heure de fin et de début ne correspondent pas", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void updateBean() {
        CrenauxHoraire cr = recopieView();
        if (controleFiche(cr)) {
            YvsCrenauxHoraire entity = buildEntityFromBean(cr);
            dao.update(entity);
            listCreno.set(listCreno.indexOf(cr), cr);
            update("th-table");
            succes();
            setDisableSave(true);
        }

    }

    @Override
    public void resetFiche() {
        resetFiche(creno);
        setDisableSave(false);
    }

    @Override
    public boolean saveNew() {
        CrenauxHoraire cr = recopieView();
        if (controleFiche(cr)) {
            cr.setId(0);
            YvsCrenauxHoraire id = buildEntityFromBean(cr);
            id.setIdAgence(currentAgence);
            id.setId(null);
            id = ((YvsCrenauxHoraire) dao.save1(id));
            cr.setId(id.getId());
            listCreno.add(0, cr);
            update("th-table");
            succes();
            setDisableSave(true);
        }
        return true;
    }

    @Override
    public CrenauxHoraire recopieView() {
        CrenauxHoraire cr = new CrenauxHoraire();
        cr.setActif(creno.isActif());
        cr.setCodeTranche(creno.getCodeTranche());
        cr.setHeureDeb(creno.getHeureDeb());
        cr.setHeureFin(creno.getHeureFin());
        cr.setId(creno.getId());
        cr.setLibelle(creno.getLibelle());
        cr.setOrdre(creno.getOrdre());
        cr.setTypeDeJournee(creno.getTypeDeJournee());
        if (creno.getAuteur() != null) {
            cr.setAuteur(creno.getAuteur());
        } else {
            cr.setAuteur(getUserOnLine());
        }
        if (creno.getLastAuteur() != null) {
            cr.setLastAuteur(creno.getLastAuteur());
        } else {
            cr.setLastAuteur(getUserOnLine());
        }
        if (creno.getLastDateUpdate() != null) {
            cr.setLastDateUpdate(creno.getLastDateUpdate());
        } else {
            cr.setLastDateUpdate(dft.format(new Date()));
        }
        return cr;
    }

    @Override
    public void populateView(CrenauxHoraire bean) {
        creno.setActif(bean.isActif());
        creno.setCodeTranche(bean.getCodeTranche());
        creno.setHeureDeb(bean.getHeureDeb());
        creno.setHeureFin(bean.getHeureFin());
        creno.setId(bean.getId());
        creno.setLibelle(bean.getLibelle());
        creno.setOrdre(bean.getOrdre());
        creno.setTypeDeJournee(bean.getTypeDeJournee());
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        CrenauxHoraire cr = (CrenauxHoraire) ev.getObject();
        if (cr != null) {
            populateView(cr);
            setDisableSave(true);
        }
    }

    @Override
    public void loadAll() {
        String[] ch = {"agence"};
        Object[] v = {currentAgence.getId()};
        List<YvsCrenauxHoraire> l = dao.loadNameQueries("YvsCrenauxHoraire.findAll", ch, v);
        listCreno.clear();
        for (YvsCrenauxHoraire cr : l) {
            listCreno.add(buildBeanFromEntity(cr));
        }
    }

    private CrenauxHoraire buildBeanFromEntity(YvsCrenauxHoraire cr) {
        CrenauxHoraire c = new CrenauxHoraire();
        c.setActif(cr.getActif());
        c.setCodeTranche(cr.getCodeTranche());
        c.setHeureDeb(cr.getHeureDeb());
        c.setHeureFin(cr.getHeureFin());
        c.setId(cr.getId());
        c.setLibelle(cr.getLibelle());
        c.setOrdre(cr.getOrdre());
        c.setTypeDeJournee(cr.getTypeDeJournee());
        return c;
    }

    private YvsCrenauxHoraire buildEntityFromBean(CrenauxHoraire cr) {
        YvsCrenauxHoraire c = new YvsCrenauxHoraire();
        c.setActif(cr.isActif());
        c.setCodeTranche(cr.getCodeTranche());
        c.setHeureDeb(cr.getHeureDeb());
        c.setHeureFin(cr.getHeureFin());
        c.setId(cr.getId());
        c.setLibelle(cr.getLibelle());
        c.setOrdre(cr.getOrdre());
        c.setTypeDeJournee(cr.getTypeDeJournee());
        return c;
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
