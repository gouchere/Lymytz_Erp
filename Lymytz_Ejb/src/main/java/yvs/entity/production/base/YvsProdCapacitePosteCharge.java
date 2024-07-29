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
import javax.persistence.Entity; import javax.persistence.FetchType;
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
import javax.persistence.Transient;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_capacite_poste_charge")
@NamedQueries({
    @NamedQuery(name = "YvsProdCapacitePosteCharge.findAll", query = "SELECT y FROM YvsProdCapacitePosteCharge y"),
    @NamedQuery(name = "YvsProdCapacitePosteCharge.findById", query = "SELECT y FROM YvsProdCapacitePosteCharge y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdCapacitePosteCharge.findByCapaciteQ", query = "SELECT y FROM YvsProdCapacitePosteCharge y WHERE y.capaciteQ = :capaciteQ"),

    @NamedQuery(name = "YvsProdCapacitePosteCharge.findByPosteArticle", query = "SELECT y FROM YvsProdCapacitePosteCharge y WHERE y.articles = :article AND y.posteCharge = :poste")})
public class YvsProdCapacitePosteCharge implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_capacite_ressource_production_id_seq", name = "yvs_prod_capacite_ressource_production_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_capacite_ressource_production_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "capacite_q")
    private Double capaciteQ;
    @JoinColumn(name = "poste_charge", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdPosteCharge posteCharge;
    @JoinColumn(name = "articles", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles articles;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsProdCapacitePosteCharge() {
    }

    public YvsProdCapacitePosteCharge(Integer id) {
        this.id = id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getCapaciteQ() {
        return capaciteQ != null ? capaciteQ : 0.0;
    }

    public void setCapaciteQ(Double capaciteQ) {
        this.capaciteQ = capaciteQ;
    }

    public YvsProdPosteCharge getPosteCharge() {
        return posteCharge;
    }

    public void setPosteCharge(YvsProdPosteCharge posteCharge) {
        this.posteCharge = posteCharge;
    }

    public YvsBaseArticles getArticles() {
        return articles;
    }

    public void setArticles(YvsBaseArticles articles) {
        this.articles = articles;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
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
        if (!(object instanceof YvsProdCapacitePosteCharge)) {
            return false;
        }
        YvsProdCapacitePosteCharge other = (YvsProdCapacitePosteCharge) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdCapacitePosteCharge[ id=" + id + " ]";
    }

}
