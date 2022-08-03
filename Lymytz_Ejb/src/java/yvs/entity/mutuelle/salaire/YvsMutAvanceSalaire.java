///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.entity.mutuelle.salaire;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
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
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.persistence.Transient;
//import javax.validation.constraints.Size;
//import yvs.dao.salaire.service.Constantes;
//import yvs.entity.mutuelle.base.YvsMutMutualiste;
//import yvs.entity.mutuelle.credit.YvsMutTypeCredit;
//
///**
// *
// * @author lymytz
// */
//@Entity
//@Table(name = "yvs_mut_avance_salaire")
//@NamedQueries({
//    @NamedQuery(name = "YvsMutAvanceSalaire.findAll", query = "SELECT y FROM YvsMutAvanceSalaire y WHERE y.mutualiste.mutuelle.societe = :societe"),
//    @NamedQuery(name = "YvsMutAvanceSalaire.findById", query = "SELECT y FROM YvsMutAvanceSalaire y WHERE y.id = :id"),
//    @NamedQuery(name = "YvsMutAvanceSalaire.findByMutuelle", query = "SELECT y FROM YvsMutAvanceSalaire y WHERE y.mutualiste.mutuelle = :mutuelle"),
//    @NamedQuery(name = "YvsMutAvanceSalaire.findAvanceEnCours", query = "SELECT y FROM YvsMutAvanceSalaire y WHERE y.mutualiste = :mutualiste AND y.dateAvance <= :dateAvance AND y.etat=:etat"),
//    @NamedQuery(name = "YvsMutAvanceSalaire.findByMutuelleDate", query = "SELECT y FROM YvsMutAvanceSalaire y WHERE y.mutualiste.mutuelle = :mutuelle AND y.dateAvance = :dateAvance"),
//    @NamedQuery(name = "YvsMutAvanceSalaire.findByMutuelleDates", query = "SELECT y FROM YvsMutAvanceSalaire y WHERE y.mutualiste.mutuelle = :mutuelle AND y.dateAvance BETWEEN :dateDebut AND :dateFin"),
//    @NamedQuery(name = "YvsMutAvanceSalaire.findAvanceMois", query = "SELECT y FROM YvsMutAvanceSalaire y WHERE y.mutualiste.mutuelle = :mutuelle AND y.dateAvance<=:dateFin"),
//    @NamedQuery(name = "YvsMutAvanceSalaire.findByMutualisteDates", query = "SELECT y FROM YvsMutAvanceSalaire y WHERE y.mutualiste = :mutualiste AND y.dateAvance BETWEEN :dateDebut AND :dateFin"),
//    @NamedQuery(name = "YvsMutAvanceSalaire.findByDateAvance", query = "SELECT y FROM YvsMutAvanceSalaire y WHERE y.dateAvance = :dateAvance"),
//    @NamedQuery(name = "YvsMutAvanceSalaire.findByMutualiste", query = "SELECT y FROM YvsMutAvanceSalaire y WHERE y.mutualiste = :mutualiste"),
//    @NamedQuery(name = "YvsMutAvanceSalaire.findByReference", query = "SELECT y FROM YvsMutAvanceSalaire y WHERE y.reference = :reference"),
//    @NamedQuery(name = "YvsMutAvanceSalaire.findByMontant", query = "SELECT y FROM YvsMutAvanceSalaire y WHERE y.montant = :montant")})
//public class YvsMutAvanceSalaire implements Serializable {
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
//    @Column(name = "date_avance")
//    @Temporal(TemporalType.DATE)
//    private Date dateAvance;
//    @Size(max = 2147483647)
//    @Column(name = "reference")
//    private String reference;
//    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
//    @Column(name = "montant")
//    private Double montant;
//    @Column(name = "etat")
//    private String etat;
//    @Column(name = "statut_paiement")
//    private Character statutPaiement;
//    @Column(name = "motif_refus")
//    private String motifRefus;
//    @JoinColumn(name = "type", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsMutTypeCredit type;
//    @JoinColumn(name = "mutualiste", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsMutMutualiste mutualiste;
//    @OneToMany(mappedBy = "avance")
//    private List<YvsMutConditionAvance> conditions;
//    @OneToMany(mappedBy = "avance")
//    private List<YvsMutReglementAvance> reglements;
//    @Transient
//    private boolean selectActif;
//    @Transient
//    private boolean new_;
//    @Transient
//    private boolean update;
//    @Transient
//    private double montantReste;
//
//    public YvsMutAvanceSalaire() {
//        reglements = new ArrayList<>();
//    }
//
//    public YvsMutAvanceSalaire(Long id) {
//        this.id = id;
//        reglements = new ArrayList<>();
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
//    public boolean isUpdate() {
//        return update;
//    }
//
//    public void setUpdate(boolean update) {
//        this.update = update;
//    }
//
//    public boolean isSelectActif() {
//        return selectActif;
//    }
//
//    public void setSelectActif(boolean selectActif) {
//        this.selectActif = selectActif;
//    }
//
//    public boolean isNew_() {
//        return new_;
//    }
//
//    public void setNew_(boolean new_) {
//        this.new_ = new_;
//    }
//
//    public double getMontantReste() {
//        return montantReste;
//    }
//
//    public void setMontantReste(double montantReste) {
//        this.montantReste = montantReste;
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
//    public Date getDateAvance() {
//        return dateAvance;
//    }
//
//    public void setDateAvance(Date dateAvance) {
//        this.dateAvance = dateAvance;
//    }
//
//    public char getStatutPaiement() {
//        return statutPaiement != null ? String.valueOf(statutPaiement).trim().length() > 0 ? statutPaiement : Constantes.STATUT_DOC_ATTENTE : Constantes.STATUT_DOC_ATTENTE;
//    }
//
//    public void setStatutPaiement(char statutPaiement) {
//        this.statutPaiement = statutPaiement;
//    }
//
//    public String getEtat() {
//        return etat;
//    }
//
//    public void setEtat(String etat) {
//        this.etat = etat;
//    }
//
//    public String getMotifRefus() {
//        return motifRefus;
//    }
//
//    public void setMotifRefus(String motifRefus) {
//        this.motifRefus = motifRefus;
//    }
//
//    public String getReference() {
//        return reference;
//    }
//
//    public void setReference(String reference) {
//        this.reference = reference;
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
//    public YvsMutTypeCredit getType() {
//        return type;
//    }
//
//    public void setType(YvsMutTypeCredit type) {
//        this.type = type;
//    }
//
//    public YvsMutMutualiste getMutualiste() {
//        return mutualiste;
//    }
//
//    public void setMutualiste(YvsMutMutualiste mutualiste) {
//        this.mutualiste = mutualiste;
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
//        if (!(object instanceof YvsMutAvanceSalaire)) {
//            return false;
//        }
//        YvsMutAvanceSalaire other = (YvsMutAvanceSalaire) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "yvs.entity.mutuelle.salaire.YvsMutAvanceSalaire[ id=" + id + " ]";
//    }
//
//    public List<YvsMutReglementAvance> getReglements() {
//        return reglements;
//    }
//
//    public void setReglements(List<YvsMutReglementAvance> reglements) {
//        this.reglements = reglements;
//    }
//
//    public List<YvsMutConditionAvance> getConditions() {
//        return conditions;
//    }
//
//    public void setConditions(List<YvsMutConditionAvance> conditions) {
//        this.conditions = conditions;
//    }
//
//}
