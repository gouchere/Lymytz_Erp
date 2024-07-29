/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.util.Constantes;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class Calendrier implements Serializable {

    private int id;
    private String reference, module;
    private boolean defaut;
    private double totalHeureHebdo, totalHeureMensuel;
    private Date marge;
    private Date dateSave = new Date();
    private boolean actif;

    private List<YvsJoursOuvres> listJoursOuvres;

    public Calendrier() {
        listJoursOuvres = new ArrayList<>();
    }

    public Calendrier(int id) {
        this.id = id;
        listJoursOuvres = new ArrayList<>();
    }

    public Calendrier(int id, String reference) {
        this.id = id;
        this.reference = reference;
        listJoursOuvres = new ArrayList<>();
    }

    public String getModule() {
        return module != null ? (module.trim().length() > 0 ? module : Constantes.MOD_GRH) : Constantes.MOD_GRH;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public double getTotalHeureHebdo() {
        return totalHeureHebdo;
    }

    public void setTotalHeureHebdo(double totalHeureHebdo) {
        this.totalHeureHebdo = totalHeureHebdo;
    }

    public double getTotalHeureMensuel() {
        return totalHeureMensuel;
    }

    public void setTotalHeureMensuel(double totalHeureMensuel) {
        this.totalHeureMensuel = totalHeureMensuel;
    }

    public boolean isDefaut() {
        return defaut;
    }

    public void setDefaut(boolean defaut) {
        this.defaut = defaut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<YvsJoursOuvres> getListJoursOuvres() {
        return listJoursOuvres;
    }

    public void setListJoursOuvres(List<YvsJoursOuvres> listJoursOuvres) {
        this.listJoursOuvres = listJoursOuvres;
    }

    public Date getMarge() {
        return marge;
    }

    public void setMarge(Date marge) {
        this.marge = marge;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isActif() {
        return actif;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
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
        final Calendrier other = (Calendrier) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
