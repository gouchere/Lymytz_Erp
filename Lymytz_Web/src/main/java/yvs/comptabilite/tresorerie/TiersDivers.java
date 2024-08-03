/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.tresorerie;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.tiers.Tiers;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class TiersDivers implements Serializable {

    private long id;
    private double montant = 0;
    private long idTiers;
    private String tableTiers;
    private DocCaissesDivers docDivers = new DocCaissesDivers();
    private Tiers tiers = new Tiers();
    private Date dateSave = new Date();

    public TiersDivers() {
    }

    public TiersDivers(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public long getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(long idTiers) {
        this.idTiers = idTiers;
    }

    public String getTableTiers() {
        return tableTiers;
    }

    public void setTableTiers(String tableTiers) {
        this.tableTiers = tableTiers;
    }

    public DocCaissesDivers getDocDivers() {
        return docDivers;
    }

    public void setDocDivers(DocCaissesDivers docDivers) {
        this.docDivers = docDivers;
    }

    public Tiers getTiers() {
        return tiers;
    }

    public void setTiers(Tiers tiers) {
        this.tiers = tiers;
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
        hash = 73 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final TiersDivers other = (TiersDivers) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
