/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.base;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_gamme_site")
@NamedQueries({
    @NamedQuery(name = "YvsProdGammeSite.findAll", query = "SELECT y FROM YvsProdGammeSite y WHERE y.site.agence.societe = :societe"),
    @NamedQuery(name = "YvsProdGammeSite.findById", query = "SELECT y FROM YvsProdGammeSite y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdGammeSite.findOne", query = "SELECT y FROM YvsProdGammeSite y WHERE y.gamme = :gamme AND y.site = :site"),
    @NamedQuery(name = "YvsProdGammeSite.findByGamme", query = "SELECT y FROM YvsProdGammeSite y WHERE y.gamme = :gamme"),
    @NamedQuery(name = "YvsProdGammeSite.findBySite", query = "SELECT y FROM YvsProdGammeSite y WHERE y.site = :site"),
    @NamedQuery(name = "YvsProdGammeSite.findOneGammeBySite", query = "SELECT y.gamme FROM YvsProdGammeSite y WHERE y.site = :site AND y.gamme.article=:article AND y.gamme.actif=true ORDER BY y.gamme.principal DESC"),
    @NamedQuery(name = "YvsProdGammeSite.findGammeBySite", query = "SELECT DISTINCT y.gamme FROM YvsProdGammeSite y WHERE y.site = :site"),
    @NamedQuery(name = "YvsProdGammeSite.findSiteByGamme", query = "SELECT DISTINCT y.site FROM YvsProdGammeSite y WHERE y.gamme = :gamme")})
public class YvsProdGammeSite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_gamme_site_id_seq", name = "yvs_prod_gamme_site_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_gamme_site_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "gamme", referencedColumnName = "id")
    @ManyToOne
    private YvsProdGammeArticle gamme;
    @JoinColumn(name = "site", referencedColumnName = "id")
    @ManyToOne
    private YvsProdSiteProduction site;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsProdGammeSite() {
    }

    public YvsProdGammeSite(Long id) {
        this.id = id;
    }

    public YvsProdGammeSite(YvsProdGammeArticle gamme, YvsProdSiteProduction site) {
        this.gamme = gamme;
        this.site = site;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsProdGammeArticle getGamme() {
        return gamme;
    }

    public void setGamme(YvsProdGammeArticle gamme) {
        this.gamme = gamme;
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
        if (!(object instanceof YvsProdGammeSite)) {
            return false;
        }
        YvsProdGammeSite other = (YvsProdGammeSite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdGammeSite[ id=" + id + " ]";
    }

}
