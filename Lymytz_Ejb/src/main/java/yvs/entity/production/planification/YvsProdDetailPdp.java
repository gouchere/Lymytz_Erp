/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.planification;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import yvs.entity.base.YvsBaseArticleFournisseur;
import yvs.entity.production.base.YvsProdComposantMrp;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_detail_pdp")
@NamedQueries({
    @NamedQuery(name = "YvsProdDetailPdp.findAll", query = "SELECT y FROM YvsProdDetailPdp y"),
    @NamedQuery(name = "YvsProdDetailPdp.findSociete", query = "SELECT y FROM YvsProdDetailPdp y WHERE y.periode.plan.societe = :societe"),
    @NamedQuery(name = "YvsProdDetailPdp.findById", query = "SELECT y FROM YvsProdDetailPdp y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdDetailPdp.findByTypeVal", query = "SELECT y FROM YvsProdDetailPdp y WHERE y.typeVal = :typeVal"),
    @NamedQuery(name = "YvsProdDetailPdp.findByPDP", query = "SELECT y FROM YvsProdDetailPdp y WHERE  y.periode.plan.societe = :societe AND y.typeVal = 'OPD'"),
    @NamedQuery(name = "YvsProdDetailPdp.findByValeur", query = "SELECT y FROM YvsProdDetailPdp y WHERE y.valeur = :valeur")})
public class YvsProdDetailPdp implements Serializable {

    @OneToMany(mappedBy = "pdp")
    private List<YvsProdDetailPdc> yvsProdDetailPdcList;
    @JoinColumn(name = "fournisseur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticleFournisseur fournisseur;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valeur")
    private Double valeur;
    @Column(name = "type_val")
    private String typeVal;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_detail_pdp_id_seq", name = "yvs_prod_detail_pdp_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_detail_pdp_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "periode", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdPeriodePlan periode;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private YvsUsersAgence author;

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsProdDetailPdp() {
    }

    public YvsProdDetailPdp(Long id) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsProdPeriodePlan getPeriode() {
        return periode;
    }

    public void setPeriode(YvsProdPeriodePlan periode) {
        this.periode = periode;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
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
        if (!(object instanceof YvsProdDetailPdp)) {
            return false;
        }
        YvsProdDetailPdp other = (YvsProdDetailPdp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdDetailPdp[ id=" + id + " ]";
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public String getTypeVal() {
        return typeVal;
    }

    public void setTypeVal(String typeVal) {
        this.typeVal = typeVal;
    }

    public YvsBaseArticleFournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(YvsBaseArticleFournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public List<YvsProdDetailPdc> getYvsProdDetailPdcList() {
        return yvsProdDetailPdcList;
    }

    public void setYvsProdDetailPdcList(List<YvsProdDetailPdc> yvsProdDetailPdcList) {
        this.yvsProdDetailPdcList = yvsProdDetailPdcList;
    }

}
