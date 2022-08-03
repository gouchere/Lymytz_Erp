/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.param.YvsGrhEchelons;
import yvs.util.Managed;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class Echelons extends Managed<Echelons, YvsBaseCaisse> implements Serializable {

    private int id, degre;
    private String titre, echelon;
    private long echelonActif; //indiue l'echelon actif d'un employé à un moment donnée
    private double salaireH, salaireM;
    private List<Echelons> listValue, selections;
    private boolean displayButonDelete;

    private Date dateSave = new Date();

    public Echelons() {
        listValue = new ArrayList<>();
    }

    public String getEchelon() {
        return echelon;
    }

    public void setEchelon(String echelon) {
        this.echelon = echelon;
    }

    public boolean isDisplayButonDelete() {
        return displayButonDelete;
    }

    public void setDisplayButonDelete(boolean displayButonDelete) {
        this.displayButonDelete = displayButonDelete;
    }

    public Echelons(int id) {
        this.id = id;
    }

    public void setDegre(int degre) {
        this.degre = degre;
    }

    public int getDegre() {
        return degre;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Echelons(int id, String titre) {
        this.id = id;
        this.titre = titre;
    }

    public Echelons(int id, String titre, double salaireH, double salaireM) {
        this.id = id;
        this.titre = titre;
        this.salaireH = salaireH;
        this.salaireM = salaireM;
    }

    public long getEchelonActif() {
        return echelonActif;
    }

    public void setEchelonActif(long echelonActif) {
        this.echelonActif = echelonActif;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public List<Echelons> getListValue() {
        return listValue;
    }

    public void setListValue(List<Echelons> listValue) {
        this.listValue = listValue;
    }

    public double getSalaireH() {
        return salaireH;
    }

    public void setSalaireH(double salaireH) {
        this.salaireH = salaireH;
    }

    public double getSalaireM() {
        return salaireM;
    }

    public void setSalaireM(double salaireM) {
        this.salaireM = salaireM;
    }

    public List<Echelons> getSelections() {
        return selections;
    }

    public void setSelections(List<Echelons> selections) {
        this.selections = selections;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Echelons other = (Echelons) obj;
        return this.id == other.id;
    }

    @Override
    public boolean controleFiche(Echelons bean) {
        //on s'assure qu'il y a pas déja d'ntité portant le même titre en bd
        champ = new String[]{"echelon", "societe"};
        val = new Object[]{bean.getTitre(), currentAgence.getSociete()};
        YvsGrhEchelons e = (YvsGrhEchelons) dao.loadObjectByNameQueries("YvsEchelons.findByEchelonSociete", champ, val);
        return e == null;
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Echelons recopieView() {
        return this;
    }

    @Override
    public void populateView(Echelons bean) {
        cloneObject(this, bean);
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        Echelons c = (Echelons) ev.getObject();
        cloneObject(this, c);
    }

    @Override
    public void deleteBean() {
    }

    public void deleteBean(Echelons e) {
        try {
            dao.delete(buildEntityByBean(e));
//                    dao.delete(new YvsGrhEchelons(c.getId()));
            listValue.remove(e);
            update("tab-echelon");
        } catch (Exception ex) {
            getMessage("Impossible de terminer cette action! La catégorie est certainement utilisé par d'autres ressource", FacesMessage.SEVERITY_ERROR);
        }
    }

    @Override
    public void loadAll() {
        listValue.clear();
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        List<YvsGrhEchelons> l = dao.loadNameQueries("YvsEchelons.findAll", champ, val);
        for (YvsGrhEchelons d : l) {
            Echelons dd = buildEntityByBean(d);
            listValue.add(dd);
        }
        if (!listValue.isEmpty()) {
            setDegre(listValue.get(listValue.size() - 1).degre + 1);
        }
    }

    @Override
    public boolean saveNew() {
        if (controleFiche(this)) {
            //persiste
            YvsGrhEchelons ech = buildEntityByBean(this);
            ech.setDateUpdate(new Date());
            ech.setId(null);
            ech = (YvsGrhEchelons) dao.save1(ech);
            listValue.add(0, new Echelons(ech.getId(), titre));
            actionOpenOrResetAfter(this);
            return true;
        } else {
            getErrorMessage("Création impossible.... " + this.echelon + " existe deja");
            return false;
        }
    }

    private Echelons buildEntityByBean(YvsGrhEchelons c) {
        Echelons cat = new Echelons();
        cat.setTitre(c.getEchelon());
        cat.setId(c.getId());
        cat.setDegre(c.getDegre());
        cat.setDateSave(c.getDateSave());
        return cat;
    }

    private YvsGrhEchelons buildEntityByBean(Echelons c) {
        YvsGrhEchelons cat = new YvsGrhEchelons();
        cat.setEchelon(c.getTitre());
        cat.setId(c.getId());
        cat.setDegre(c.getDegre());
        cat.setActif(true);
        cat.setSupp(false);
        cat.setSociete(currentAgence.getSociete());
        cat.setDateSave(c.getDateSave());
        cat.setDateUpdate(new Date());
        return cat;
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        titre = "";
        degre = 0;
        update("titleE");
        update("deg-echP");
    }

}
