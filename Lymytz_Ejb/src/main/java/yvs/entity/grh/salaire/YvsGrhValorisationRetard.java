/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.entity.grh.salaire;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import yvs.entity.grh.presence.YvsGrhPresence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_valorisation_retard")
@NamedQueries({
    @NamedQuery(name = "YvsGrhValorisationRetard.findAll", query = "SELECT y FROM YvsGrhValorisationRetard y"),
    @NamedQuery(name = "YvsGrhValorisationRetard.findById", query = "SELECT y FROM YvsGrhValorisationRetard y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhValorisationRetard.findByRetardValorise", query = "SELECT y FROM YvsGrhValorisationRetard y WHERE y.retardValorise = :retardValorise")})
public class YvsGrhValorisationRetard implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "retard_valorise")
    private Double retardValorise;
    @JoinColumn(name = "bulletin", referencedColumnName = "id")
    @ManyToOne
    private YvsGrhBulletins bulletin;
    @JoinColumn(name = "fiche", referencedColumnName = "id")
    @ManyToOne
    private YvsGrhPresence fiche;

    public YvsGrhValorisationRetard() {
    }

    public YvsGrhValorisationRetard(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRetardValorise() {
        return retardValorise;
    }

    public void setRetardValorise(Double retardValorise) {
        this.retardValorise = retardValorise;
    }

    public YvsGrhBulletins getBulletin() {
        return bulletin;
    }

    public void setBulletin(YvsGrhBulletins bulletin) {
        this.bulletin = bulletin;
    }

    public YvsGrhPresence getFiche() {
        return fiche;
    }

    public void setFiche(YvsGrhPresence fiche) {
        this.fiche = fiche;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsGrhValorisationRetard)) {
            return false;
        }
        YvsGrhValorisationRetard other = (YvsGrhValorisationRetard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsGrhValorisationRetard[ id=" + id + " ]";
    }
    
}
