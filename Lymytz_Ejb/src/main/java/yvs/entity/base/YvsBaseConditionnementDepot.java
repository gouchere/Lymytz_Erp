/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

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
import javax.xml.bind.annotation.XmlRootElement;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_base_conditionnement_depot")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsBaseConditionnementDepot.findAll", query = "SELECT y FROM YvsBaseConditionnementDepot y"),
    @NamedQuery(name = "YvsBaseConditionnementDepot.findById", query = "SELECT y FROM YvsBaseConditionnementDepot y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseConditionnementDepot.findOne", query = "SELECT y FROM YvsBaseConditionnementDepot y WHERE y.article = :article AND y.operation = :operation AND y.conditionnement = :unite"),
    @NamedQuery(name = "YvsBaseConditionnementDepot.findConditionnement", query = "SELECT DISTINCT(y.conditionnement) FROM YvsBaseConditionnementDepot y WHERE y.article.article = :article AND y.article.depot = :depot AND y.operation.operation = :operation AND y.operation.type = :type AND y.conditionnement.actif = TRUE")})
public class YvsBaseConditionnementDepot implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_conditionnement_depot_id_seq", name = "yvs_base_conditionnement_depot_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_conditionnement_depot_id_seq_name", strategy = GenerationType.SEQUENCE) 
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "operation", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepotOperation operation;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticleDepot article;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsBaseConditionnementDepot() {
    }

    public YvsBaseConditionnementDepot(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsBaseDepotOperation getOperation() {
        return operation;
    }

    public void setOperation(YvsBaseDepotOperation operation) {
        this.operation = operation;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public YvsBaseArticleDepot getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticleDepot article) {
        this.article = article;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
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
        if (!(object instanceof YvsBaseConditionnementDepot)) {
            return false;
        }
        YvsBaseConditionnementDepot other = (YvsBaseConditionnementDepot) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseConditionnementDepot[ id=" + id + " ]";
    }

}
