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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_tranche_reglement_fournisseur")
@NamedQueries({
    @NamedQuery(name = "YvsBaseTrancheReglementFournisseur.findAll", query = "SELECT y FROM YvsBaseTrancheReglementFournisseur y WHERE y.plan.model.societe = :societe"),
    @NamedQuery(name = "YvsBaseTrancheReglementFournisseur.findById", query = "SELECT y FROM YvsBaseTrancheReglementFournisseur y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseTrancheReglementFournisseur.findByPlan", query = "SELECT y FROM YvsBaseTrancheReglementFournisseur y WHERE y.plan = :plan"),
    @NamedQuery(name = "YvsBaseTrancheReglementFournisseur.findByNumero", query = "SELECT y FROM YvsBaseTrancheReglementFournisseur y WHERE y.numero = :numero"),
    @NamedQuery(name = "YvsBaseTrancheReglementFournisseur.findByTaux", query = "SELECT y FROM YvsBaseTrancheReglementFournisseur y WHERE y.taux = :taux"),
    @NamedQuery(name = "YvsBaseTrancheReglementFournisseur.findByIntervalJour", query = "SELECT y FROM YvsBaseTrancheReglementFournisseur y WHERE y.intervalJour = :intervalJour")})
public class YvsBaseTrancheReglementFournisseur implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_tranche_reglement_fournisseur_id_seq", name = "yvs_base_tranche_reglement_fournisseur_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_tranche_reglement_fournisseur_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
       private Long id; 
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "numero")
    private Integer numero;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux")
    private Double taux;
    @Column(name = "interval_jour")
    private Integer intervalJour;
    @JoinColumn(name = "plan", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanReglementFournisseur plan;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsBaseTrancheReglementFournisseur() {
    }

    public YvsBaseTrancheReglementFournisseur(Long id) {
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

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public Integer getIntervalJour() {
        return intervalJour;
    }

    public void setIntervalJour(Integer intervalJour) {
        this.intervalJour = intervalJour;
    }

    public YvsBasePlanReglementFournisseur getPlan() {
        return plan;
    }

    public void setPlan(YvsBasePlanReglementFournisseur plan) {
        this.plan = plan;
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
        if (!(object instanceof YvsBaseTrancheReglementFournisseur)) {
            return false;
        }
        YvsBaseTrancheReglementFournisseur other = (YvsBaseTrancheReglementFournisseur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseTrancheReglementFournisseur[ id=" + id + " ]";
    }
    
}
