/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

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
import javax.persistence.Transient;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_plan_reglement_fournisseur")
@NamedQueries({
    @NamedQuery(name = "YvsBasePlanReglementFournisseur.findAll", query = "SELECT y FROM YvsBasePlanReglementFournisseur y WHERE y.fournisseur.tiers.societe = :societe"),
    @NamedQuery(name = "YvsBasePlanReglementFournisseur.findById", query = "SELECT y FROM YvsBasePlanReglementFournisseur y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBasePlanReglementFournisseur.findByFournisseur", query = "SELECT y FROM YvsBasePlanReglementFournisseur y WHERE y.fournisseur = :fournisseur"),
    @NamedQuery(name = "YvsBasePlanReglementFournisseur.findByMontantMinimal", query = "SELECT y FROM YvsBasePlanReglementFournisseur y WHERE y.montantMinimal = :montantMinimal"),
    @NamedQuery(name = "YvsBasePlanReglementFournisseur.findByMontantMaximal", query = "SELECT y FROM YvsBasePlanReglementFournisseur y WHERE y.montantMaximal = :montantMaximal"),
    @NamedQuery(name = "YvsBasePlanReglementFournisseur.findByActif", query = "SELECT y FROM YvsBasePlanReglementFournisseur y WHERE y.actif = :actif")})
public class YvsBasePlanReglementFournisseur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_plan_reglement_fournisseur_id_seq", name = "yvs_base_plan_reglement_fournisseur_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_plan_reglement_fournisseur_id_seq_name", strategy = GenerationType.SEQUENCE) 
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
    @Column(name = "montant_minimal")
    private Double montantMinimal;
    @Column(name = "montant_maximal")
    private Double montantMaximal;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModeReglement model;
    @JoinColumn(name = "fournisseur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseFournisseur fournisseur;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "plan")
    private List<YvsBaseTrancheReglementFournisseur> tranches;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsBasePlanReglementFournisseur() {
    }

    public YvsBasePlanReglementFournisseur(Long id) {
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

    public Double getMontantMinimal() {
        return montantMinimal;
    }

    public void setMontantMinimal(Double montantMinimal) {
        this.montantMinimal = montantMinimal;
    }

    public Double getMontantMaximal() {
        return montantMaximal;
    }

    public void setMontantMaximal(Double montantMaximal) {
        this.montantMaximal = montantMaximal;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsBaseModeReglement getModel() {
        return model;
    }

    public void setModel(YvsBaseModeReglement model) {
        this.model = model;
    }

    public YvsBaseFournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(YvsBaseFournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public List<YvsBaseTrancheReglementFournisseur> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsBaseTrancheReglementFournisseur> tranches) {
        this.tranches = tranches;
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
        if (!(object instanceof YvsBasePlanReglementFournisseur)) {
            return false;
        }
        YvsBasePlanReglementFournisseur other = (YvsBasePlanReglementFournisseur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBasePlanReglementFournisseur[ id=" + id + " ]";
    }

}
