///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.entity.mutuelle.salaire;
//
//import java.io.Serializable;
//import java.util.Date;
//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.Entity; import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.validation.constraints.Size;
//
///**
// *
// * @author lymytz
// */
//@Entity
//@Table(name = "yvs_mut_reglement_avance")
//@NamedQueries({
//    @NamedQuery(name = "YvsMutReglementAvance.findAll", query = "SELECT y FROM YvsMutReglementAvance y WHERE y.avance.mutualiste.mutuelle.societe = :societe"),
//    @NamedQuery(name = "YvsMutReglementAvance.findByMutuelle", query = "SELECT y FROM YvsMutReglementAvance y WHERE y.avance.mutualiste.mutuelle = :mutuelle"),
//    @NamedQuery(name = "YvsMutReglementAvance.findByMutualiste", query = "SELECT y FROM YvsMutReglementAvance y WHERE y.avance.mutualiste = :mutualiste"),
//    @NamedQuery(name = "YvsMutReglementAvance.findById", query = "SELECT y FROM YvsMutReglementAvance y WHERE y.id = :id"),
//    @NamedQuery(name = "YvsMutReglementAvance.findByDateReglement", query = "SELECT y FROM YvsMutReglementAvance y WHERE y.dateReglement = :dateReglement"),
//    @NamedQuery(name = "YvsMutReglementAvance.findByMontant", query = "SELECT y FROM YvsMutReglementAvance y WHERE y.montant = :montant")})
//public class YvsMutReglementAvance implements Serializable {
//
//    @Size(max = 2147483647)
//    @Column(name = "regle_par")
//    private String reglePar;
//
//    private static final long serialVersionUID = 1L;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
//    @Column(name = "id")
//    private Long id;
//    @Column(name = "date_update")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date dateUpdate;
//    @Column(name = "date_save")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date dateSave;
//    @Column(name = "date_reglement")
//    @Temporal(TemporalType.DATE)
//    private Date dateReglement;
//    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
//    @Column(name = "montant")
//    private Double montant;
//    @JoinColumn(name = "avance", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsMutAvanceSalaire avance;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "souce_reglement", referencedColumnName = "id")
//    private YvsMutPaiementSalaire souceReglement; // modélise le règlement d'un salaire
//
//    public YvsMutReglementAvance() {
//    }
//
//    public YvsMutReglementAvance(Long id) {
//        this.id = id;
//    }
//
//    public Date getDateUpdate() {
//        return dateUpdate;
//    }
//
//    public void setDateUpdate(Date dateUpdate) {
//        this.dateUpdate = dateUpdate;
//    }
//
//    public Date getDateSave() {
//        return dateSave;
//    }
//
//    public void setDateSave(Date dateSave) {
//        this.dateSave = dateSave;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Date getDateReglement() {
//        return dateReglement;
//    }
//
//    public void setDateReglement(Date dateReglement) {
//        this.dateReglement = dateReglement;
//    }
//
//    public Double getMontant() {
//        return montant;
//    }
//
//    public void setMontant(Double montant) {
//        this.montant = montant;
//    }
//
//    public YvsMutAvanceSalaire getAvance() {
//        return avance;
//    }
//
//    public void setAvance(YvsMutAvanceSalaire avance) {
//        this.avance = avance;
//    }
//
//    public YvsMutPaiementSalaire getSouceReglement() {
//        return souceReglement;
//    }
//
//    public void setSouceReglement(YvsMutPaiementSalaire souceReglement) {
//        this.souceReglement = souceReglement;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof YvsMutReglementAvance)) {
//            return false;
//        }
//        YvsMutReglementAvance other = (YvsMutReglementAvance) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "yvs.entity.mutuelle.salaire.YvsMutReglementAvance[ id=" + id + " ]";
//    }
//
//    public String getReglePar() {
//        return reglePar;
//    }
//
//    public void setReglePar(String reglePar) {
//        this.reglePar = reglePar;
//    }
//
//}
