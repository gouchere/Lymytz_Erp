/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.presence;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Lymytz Dowes
 */
public class ParametreIOEM  implements Serializable{

    private List<IOEMDevice> pointages;
    private boolean auto;
    private boolean addEmploye;
    private long employe;
    private long societe;
    private boolean addDate;
    private Date dateDebut;
    private Date dateFin;
    private boolean invalid;
    private boolean filter;

    public ParametreIOEM() {
    }

    public List<IOEMDevice> getPointages() {
        return pointages;
    }

    public void setPointages(List<IOEMDevice> pointages) {
        this.pointages = pointages;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public boolean isAddEmploye() {
        return addEmploye;
    }

    public void setAddEmploye(boolean addEmploye) {
        this.addEmploye = addEmploye;
    }

    public long getEmploye() {
        return employe;
    }

    public void setEmploye(long employe) {
        this.employe = employe;
    }

    public boolean isAddDate() {
        return addDate;
    }

    public void setAddDate(boolean addDate) {
        this.addDate = addDate;
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

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public boolean isFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public long getSociete() {
        return societe;
    }

    public void setSociete(long societe) {
        this.societe = societe;
    }
    
    
}
