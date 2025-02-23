/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.salaire;

import java.io.Serializable;import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@SessionScoped
@ManagedBean
public class PeriodeSalaireMut implements Serializable {

    private long id;
    private boolean actif = true;
    private boolean cloture;    //précise si la  période de paiement est clôturé ou non
    private Date dateSave;
    private OrdreCalculSalaire periodeRh = new OrdreCalculSalaire();
//    private List<YvsMutPaiementSalaire> salaires;

    public PeriodeSalaireMut() {
//        salaires = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public OrdreCalculSalaire getPeriodeRh() {
        return periodeRh;
    }

    public void setPeriodeRh(OrdreCalculSalaire periodeRh) {
        this.periodeRh = periodeRh;
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

//    public List<YvsMutPaiementSalaire> getSalaires() {
//        return salaires;
//    }
//
//    public void setSalaires(List<YvsMutPaiementSalaire> salaires) {
//        this.salaires = salaires;
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PeriodeSalaireMut other = (PeriodeSalaireMut) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
