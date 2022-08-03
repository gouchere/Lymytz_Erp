/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.entity.production.base;

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
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_default_depot_composants")
@NamedQueries({
    @NamedQuery(name = "YvsProdDefaultDepotComposants.findAll", query = "SELECT y FROM YvsProdDefaultDepotComposants y"),
    @NamedQuery(name = "YvsProdDefaultDepotComposants.findOne", query = "SELECT y FROM YvsProdDefaultDepotComposants y WHERE y.site=:site AND y.composant=:composant"),
    @NamedQuery(name = "YvsProdDefaultDepotComposants.findOneDepot", query = "SELECT y.depotConso FROM YvsProdDefaultDepotComposants y WHERE y.site=:site AND y.composant=:composant"),
    @NamedQuery(name = "YvsProdDefaultDepotComposants.findById", query = "SELECT y FROM YvsProdDefaultDepotComposants y WHERE y.id = :id")})
public class YvsProdDefaultDepotComposants implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "composant", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticles composant;
    @JoinColumn(name = "depot_conso", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseDepots depotConso;
    @JoinColumn(name = "site", referencedColumnName = "id")
    @ManyToOne
    private YvsProdSiteProduction site;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsProdDefaultDepotComposants() {
    }

    public YvsProdDefaultDepotComposants(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsBaseArticles getComposant() {
        return composant;
    }

    public void setComposant(YvsBaseArticles composant) {
        this.composant = composant;
    }

    public YvsBaseDepots getDepotConso() {
        return depotConso;
    }

    public void setDepotConso(YvsBaseDepots depotConso) {
        this.depotConso = depotConso;
    }

    public YvsProdSiteProduction getSite() {
        return site;
    }

    public void setSite(YvsProdSiteProduction site) {
        this.site = site;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsProdDefaultDepotComposants)) {
            return false;
        }
        YvsProdDefaultDepotComposants other = (YvsProdDefaultDepotComposants) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdDefaultDepotComposants[ id=" + id + " ]";
    }
    
}
