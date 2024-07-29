/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.vente;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import yvs.dao.YvsEntity;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_proformat_vente_contenu")
@NamedQueries({
    @NamedQuery(name = "YvsComProformaVenteContenu.findAll", query = "SELECT y FROM YvsComProformaVenteContenu y WHERE y.proforma.agence.societe = :societe"),
    @NamedQuery(name = "YvsComProformaVenteContenu.findById", query = "SELECT y FROM YvsComProformaVenteContenu y  WHERE y.id = :id"),
    @NamedQuery(name = "YvsComProformaVenteContenu.findByIds", query = "SELECT y FROM YvsComProformaVenteContenu y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsComProformaVenteContenu.findByProformat", query = "SELECT y FROM YvsComProformaVenteContenu y WHERE y.proforma = :proformat"),})
public class YvsComProformaVenteContenu extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_proformat_vente_contenu_id_seq", name = "yvs_com_proformat_vente_contenu_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_proformat_vente_contenu_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "prix")
    private Double prix;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;

    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "proformat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComProformaVente proforma;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private boolean new_;

    public YvsComProformaVenteContenu() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComProformaVenteContenu(Long id) {
        this();
        this.id = id;
    }

    @Override
    public Long getId() {
        return id; //To change body of generated methods, choose Tools | Templates.
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantite() {
        return quantite != null ? quantite : 0;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Double getPrix() {
        return prix != null ? prix : 0;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public YvsComProformaVente getProforma() {
        return proforma;
    }

    public void setProforma(YvsComProformaVente proforma) {
        this.proforma = proforma;
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

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsComProformaVenteContenu)) {
            return false;
        }
        YvsComProformaVenteContenu other = (YvsComProformaVenteContenu) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.vente.YvsComProformaVenteContenu[ id=" + id + " ]";
    }
}
