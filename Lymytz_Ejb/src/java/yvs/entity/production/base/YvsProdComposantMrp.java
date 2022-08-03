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
import yvs.entity.production.planification.YvsProdDetailPdp;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_composant_mrp")
@NamedQueries({
    @NamedQuery(name = "YvsProdComposantMrp.findAll", query = "SELECT y FROM YvsProdComposantMrp y"),
    @NamedQuery(name = "YvsProdComposantMrp.findById", query = "SELECT y FROM YvsProdComposantMrp y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdComposantMrp.findByQuantite", query = "SELECT y FROM YvsProdComposantMrp y WHERE y.quantite = :quantite")})
public class YvsProdComposantMrp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_composant_mrp_id_seq", name = "yvs_prod_composant_mrp_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_composant_mrp_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantite")
    private Double quantite;
    @JoinColumn(name = "detail_pdp", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdDetailPdp detailPdp;
    @JoinColumn(name = "composant_nomenclature", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdComposantNomenclature composantNomenclature;

    public YvsProdComposantMrp() {
    }

    public YvsProdComposantMrp(Integer id) {
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

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public YvsProdDetailPdp getDetailPdp() {
        return detailPdp;
    }

    public void setDetailPdp(YvsProdDetailPdp detailPdp) {
        this.detailPdp = detailPdp;
    }

    public YvsProdComposantNomenclature getComposantNomenclature() {
        return composantNomenclature;
    }

    public void setComposantNomenclature(YvsProdComposantNomenclature composantNomenclature) {
        this.composantNomenclature = composantNomenclature;
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
        if (!(object instanceof YvsProdComposantMrp)) {
            return false;
        }
        YvsProdComposantMrp other = (YvsProdComposantMrp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdComposantMrp[ id=" + id + " ]";
    }

}
