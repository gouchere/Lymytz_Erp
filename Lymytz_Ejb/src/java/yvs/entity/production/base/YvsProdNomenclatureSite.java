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
@Table(name = "yvs_prod_nomenclature_site")
@NamedQueries({
    @NamedQuery(name = "YvsProdNomenclatureSite.findAll", query = "SELECT y FROM YvsProdNomenclatureSite y WHERE y.site.agence.societe = :societe"),
    @NamedQuery(name = "YvsProdNomenclatureSite.findById", query = "SELECT y FROM YvsProdNomenclatureSite y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdNomenclatureSite.findOne", query = "SELECT y FROM YvsProdNomenclatureSite y WHERE y.nomenclature = :nomenclature AND y.site = :site"),
    @NamedQuery(name = "YvsProdNomenclatureSite.findCOne", query = "SELECT COUNT(y.id) FROM YvsProdNomenclatureSite y WHERE y.nomenclature = :nomenclature AND y.site = :site"),
    @NamedQuery(name = "YvsProdNomenclatureSite.findByNomenclature", query = "SELECT y FROM YvsProdNomenclatureSite y WHERE y.nomenclature = :nomenclature"),
    @NamedQuery(name = "YvsProdNomenclatureSite.findBySite", query = "SELECT y FROM YvsProdNomenclatureSite y WHERE y.site = :site"),
    @NamedQuery(name = "YvsProdNomenclatureSite.findNomenclatureBySite", query = "SELECT DISTINCT y.nomenclature FROM YvsProdNomenclatureSite y WHERE y.site = :site"),
    @NamedQuery(name = "YvsProdNomenclatureSite.findOneNomenclatureBySite", query = "SELECT DISTINCT y.nomenclature FROM YvsProdNomenclatureSite y JOIN FETCH y.nomenclature JOIN FETCH y.nomenclature.article "
            + "JOIN FETCH y.nomenclature.uniteMesure JOIN FETCH y.nomenclature.uniteMesure.unite "
            + "WHERE y.site = :site AND y.nomenclature.article=:article AND y.nomenclature.actif=true AND y.nomenclature.forConditionnement=:for ORDER BY y.nomenclature.principal DESC"),
    @NamedQuery(name = "YvsProdNomenclatureSite.findSiteByGamme", query = "SELECT DISTINCT y.site FROM YvsProdNomenclatureSite y WHERE y.nomenclature = :nomenclature")})
public class YvsProdNomenclatureSite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_nomenclature_site_id_seq", name = "yvs_prod_nomenclature_site_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_nomenclature_site_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "nomenclature", referencedColumnName = "id")
    @ManyToOne
    private YvsProdNomenclature nomenclature;
    @JoinColumn(name = "site", referencedColumnName = "id")
    @ManyToOne
    private YvsProdSiteProduction site;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsProdNomenclatureSite() {
    }

    public YvsProdNomenclatureSite(Long id) {
        this.id = id;
    }

    public YvsProdNomenclatureSite(YvsProdNomenclature nomenclature, YvsProdSiteProduction site) {
        this.nomenclature = nomenclature;
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

    public YvsProdNomenclature getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(YvsProdNomenclature nomenclature) {
        this.nomenclature = nomenclature;
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
        if (!(object instanceof YvsProdNomenclatureSite)) {
            return false;
        }
        YvsProdNomenclatureSite other = (YvsProdNomenclatureSite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdNomenclatureSite[ id=" + id + " ]";
    }

}
