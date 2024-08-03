/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.activite;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity; import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_couts_formation")
@NamedQueries({
    @NamedQuery(name = "YvsCoutsFormation.findAll", query = "SELECT y FROM YvsCoutsFormation y"),
    @NamedQuery(name = "YvsCoutsFormation.findByMontant", query = "SELECT y FROM YvsCoutsFormation y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsCoutsFormation.findByFormation", query = "SELECT y FROM YvsCoutsFormation y WHERE y.yvsCoutsFormationPK.formation = :formation"),
    @NamedQuery(name = "YvsCoutsFormation.findByEmploye", query = "SELECT y FROM YvsCoutsFormation y WHERE y.yvsFormationEmps.employe = :employe"),
    @NamedQuery(name = "YvsCoutsFormation.findBySupp", query = "SELECT y FROM YvsCoutsFormation y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsCoutsFormation.findByActif", query = "SELECT y FROM YvsCoutsFormation y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsCoutsFormation.findByTypeCout", query = "SELECT y FROM YvsCoutsFormation y WHERE y.yvsCoutsFormationPK.typeCout = :typeCout")})
public class YvsCoutsFormation implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected YvsCoutsFormationPK yvsCoutsFormationPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "type_cout", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeCout yvsGrhTypeCout;
    @JoinColumn(name = "formation", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhFormationEmps yvsFormationEmps;

    public YvsCoutsFormation() {
    }

    public YvsCoutsFormation(YvsCoutsFormationPK yvsCoutsFormationPK) {
        this.yvsCoutsFormationPK = yvsCoutsFormationPK;
    }

    public YvsCoutsFormation(int formation, int typeCout) {
        this.yvsCoutsFormationPK = new YvsCoutsFormationPK(formation, typeCout);
    }

    public YvsCoutsFormationPK getYvsCoutsFormationPK() {
        return yvsCoutsFormationPK;
    }

    public void setYvsCoutsFormationPK(YvsCoutsFormationPK yvsCoutsFormationPK) {
        this.yvsCoutsFormationPK = yvsCoutsFormationPK;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsGrhTypeCout getYvsGrhTypeCout() {
        return yvsGrhTypeCout;
    }

    public void setYvsGrhTypeCout(YvsGrhTypeCout yvsGrhTypeCout) {
        this.yvsGrhTypeCout = yvsGrhTypeCout;
    }

    public YvsGrhFormationEmps getYvsFormationEmps() {
        return yvsFormationEmps;
    }

    public void setYvsFormationEmps(YvsGrhFormationEmps yvsFormationEmps) {
        this.yvsFormationEmps = yvsFormationEmps;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (yvsCoutsFormationPK != null ? yvsCoutsFormationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsCoutsFormation)) {
            return false;
        }
        YvsCoutsFormation other = (YvsCoutsFormation) object;
        if ((this.yvsCoutsFormationPK == null && other.yvsCoutsFormationPK != null) || (this.yvsCoutsFormationPK != null && !this.yvsCoutsFormationPK.equals(other.yvsCoutsFormationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.activite.YvsCoutsFormation[ yvsCoutsFormationPK=" + yvsCoutsFormationPK + " ]";
    }
    
}
