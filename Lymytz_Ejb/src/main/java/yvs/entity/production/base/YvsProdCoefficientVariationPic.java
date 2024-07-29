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
import yvs.entity.production.planification.YvsProdDetailPic;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_coefficient_variation_pic")
@NamedQueries({
    @NamedQuery(name = "YvsProdCoefficientVariationPic.findAll", query = "SELECT y FROM YvsProdCoefficientVariationPic y"),
    @NamedQuery(name = "YvsProdCoefficientVariationPic.findById", query = "SELECT y FROM YvsProdCoefficientVariationPic y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdCoefficientVariationPic.findByValeur", query = "SELECT y FROM YvsProdCoefficientVariationPic y WHERE y.valeur = :valeur")})
public class YvsProdCoefficientVariationPic implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_coefficient_variation_pic_id_seq", name = "yvs_prod_coefficient_variation_pic_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_coefficient_variation_pic_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "detail_pic", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdDetailPic detailPic;
    @JoinColumn(name = "type_coefficient", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTypeCoefficient type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private YvsUsersAgence author;

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    private Boolean percent;

    public YvsProdCoefficientVariationPic() {
    }

    public YvsProdCoefficientVariationPic(Long id) {
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

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public YvsProdDetailPic getDetailPic() {
        return detailPic;
    }

    public void setDetailPic(YvsProdDetailPic detailPic) {
        this.detailPic = detailPic;
    }

    public YvsBaseTypeCoefficient getType() {
        return type;
    }

    public void setType(YvsBaseTypeCoefficient type) {
        this.type = type;
    }

    public void setPercent(Boolean percent) {
        this.percent = percent;
    }

    public Boolean isPercent() {
        return percent;
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
        if (!(object instanceof YvsProdCoefficientVariationPic)) {
            return false;
        }
        YvsProdCoefficientVariationPic other = (YvsProdCoefficientVariationPic) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdCoefficientVariationPic[ id=" + id + " ]";
    }

}
