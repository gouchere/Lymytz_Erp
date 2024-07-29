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
import yvs.entity.production.base.YvsProdCoefficientVariationPic;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_detail_pic")
@NamedQueries({
    @NamedQuery(name = "YvsProdDetailPic.findAll", query = "SELECT y FROM YvsProdDetailPic y"),
    @NamedQuery(name = "YvsProdDetailPic.findById", query = "SELECT y FROM YvsProdDetailPic y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdDetailPic.findByPeriode", query = "SELECT y FROM YvsProdDetailPic y WHERE y.periode = :periode ORDER BY y.famille.id ASC"),})
public class YvsProdDetailPic implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_detail_pic_id_seq", name = "yvs_detail_pic_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_detail_pic_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valeur")
    private Double valeur;
    @OneToMany(mappedBy = "detailPic")
    private List<YvsProdCoefficientVariationPic> yvsProdCoefficientVariationPicList;
    @JoinColumn(name = "periode", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdPeriodePlan periode;
    @JoinColumn(name = "famille", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseFamilleArticle famille;
    @Column(name = "type_val")
    private String typeVal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private YvsUsersAgence author;

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsProdDetailPic() {
    }

    public YvsProdDetailPic(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public List<YvsProdCoefficientVariationPic> getYvsProdCoefficientVariationPicList() {
        return yvsProdCoefficientVariationPicList;
    }

    public void setYvsProdCoefficientVariationPicList(List<YvsProdCoefficientVariationPic> yvsProdCoefficientVariationPicList) {
        this.yvsProdCoefficientVariationPicList = yvsProdCoefficientVariationPicList;
    }

    public YvsProdPeriodePlan getPeriode() {
        return periode;
    }

    public void setPeriode(YvsProdPeriodePlan periode) {
        this.periode = periode;
    }

    public YvsBaseFamilleArticle getFamille() {
        return famille;
    }

    public void setFamille(YvsBaseFamilleArticle famille) {
        this.famille = famille;
    }

    public String getTypeVal() {
        return typeVal;
    }

    public void setTypeVal(String typeVal) {
        this.typeVal = typeVal;
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
        if (!(object instanceof YvsProdDetailPic)) {
            return false;
        }
        YvsProdDetailPic other = (YvsProdDetailPic) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdDetailPic[ id=" + id + " ]";
    }

}
