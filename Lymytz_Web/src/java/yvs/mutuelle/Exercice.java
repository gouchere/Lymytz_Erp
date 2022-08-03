/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.mutuelle.YvsMutPeriodeExercice;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean(name = "beanExercice")
@SessionScoped
public class Exercice implements Serializable {

    private long id;
    private String reference, nameExercice;
    private Date dateDebut = new Date();
    private Date dateFin = new Date();
    private Date dateSave = new Date();
    private boolean actif;
    private Mutuelle mutuelle = new Mutuelle();
    private boolean selectActif, new_;
    private List<YvsMutPeriodeExercice> periodes;
    private int intervalle = 1;
    private boolean cloture;

    public Exercice() {
//        initDate();
        periodes = new ArrayList<>();
    }

    public Exercice(long id) {
        this.id = id;
        initDate();
        periodes = new ArrayList<>();
    }

    public int getIntervalle() {
        return intervalle;
    }

    public void setIntervalle(int intervalle) {
        this.intervalle = intervalle;
    }

    public List<YvsMutPeriodeExercice> getPeriodes() {
        return periodes;
    }

    public void setPeriodes(List<YvsMutPeriodeExercice> periodes) {
        this.periodes = periodes;
    }

    public String getNameExercice() {
        return nameExercice;
    }

    public void setNameExercice(String nameExercice) {
        this.nameExercice = nameExercice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Mutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(Mutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isCloture() {
        return cloture;
    }

    public void setCloture(boolean cloture) {
        this.cloture = cloture;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public static Exercice getDefault() {
        Exercice e = new Exercice();
        e.setReference("Default");
        e.setId(0);
        e.setActif(true);
        Calendar cal = Util.dateToCalendar(new Date());
        int year = cal.get(Calendar.YEAR);
        cal.clear();
        cal.set(year, 01, 01);
        e.setDateDebut(cal.getTime());
        cal.clear();
        cal.set(year, 12, 31);
        e.setDateFin(cal.getTime());
        return e;
    }

    private void initDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        cal.clear();
        cal.set(year, 01, 01);
        cal.add(Calendar.MONTH, -1);
        this.dateDebut = cal.getTime();
        cal.clear();
        cal.set(year, 12, 31);
        cal.add(Calendar.MONTH, -1);
        this.dateFin = cal.getTime();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Exercice other = (Exercice) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
