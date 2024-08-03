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

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_valorisation_retenues")
@NamedQueries({
    @NamedQuery(name = "YvsGrhValorisationRetenues.findAll", query = "SELECT y FROM YvsGrhValorisationRetenues y"),
    @NamedQuery(name = "YvsGrhValorisationRetenues.findByRetenue", query = "SELECT y FROM YvsGrhValorisationRetenues y JOIN FETCH y.bulletin WHERE y.retenue=:retenue"),
    @NamedQuery(name = "YvsGrhValorisationRetenues.findById", query = "SELECT y FROM YvsGrhValorisationRetenues y WHERE y.id = :id")})
public class YvsGrhValorisationRetenues implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "bulletin", referencedColumnName = "id")
    @ManyToOne
    private YvsGrhBulletins bulletin;
    @JoinColumn(name = "retenue", referencedColumnName = "id")
    @ManyToOne
    private YvsGrhDetailPrelevementEmps retenue;

    public YvsGrhValorisationRetenues() {
    }

    public YvsGrhValorisationRetenues(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsGrhBulletins getBulletin() {
        return bulletin;
    }

    public void setBulletin(YvsGrhBulletins bulletin) {
        this.bulletin = bulletin;
    }

    public YvsGrhDetailPrelevementEmps getRetenue() {
        return retenue;
    }

    public void setRetenue(YvsGrhDetailPrelevementEmps retenue) {
        this.retenue = retenue;
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
        if (!(object instanceof YvsGrhValorisationRetenues)) {
            return false;
        }
        YvsGrhValorisationRetenues other = (YvsGrhValorisationRetenues) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsGrhValorisationRetenues[ id=" + id + " ]";
    }
    
}
