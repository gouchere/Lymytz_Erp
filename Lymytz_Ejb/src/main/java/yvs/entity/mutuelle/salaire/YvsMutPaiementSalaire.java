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
//import javax.persistence.Transient;
//import javax.validation.constraints.Size;
//import yvs.entity.mutuelle.base.YvsMutMutualiste;
//import yvs.entity.users.YvsUsersAgence;
//
///**
// *
// * @author lymytz
// */
//@Entity
//@Table(name = "yvs_mut_paiement_salaire")
//@NamedQueries({
//    @NamedQuery(name = "YvsMutPaiementSalaire.findAll", query = "SELECT y FROM YvsMutPaiementSalaire y WHERE y.mutualiste.mutuelle.societe = :societe"),
//    @NamedQuery(name = "YvsMutPaiementSalaire.findSalairePrecedent", query = "SELECT y FROM YvsMutPaiementSalaire y WHERE y.mutualiste = :mutualiste AND y.periode.periodeRh.debutMois BETWEEN :debut AND :fin"),
//    @NamedQuery(name = "YvsMutPaiementSalaire.findByMutuelle", query = "SELECT y FROM YvsMutPaiementSalaire y WHERE y.mutualiste.mutuelle = :mutuelle AND y.datePaiement BETWEEN :dateDebut AND :dateFin ORDER BY y.datePaiement DESC"),
//    @NamedQuery(name = "YvsMutPaiementSalaire.findSalairePaye", query = "SELECT y FROM YvsMutPaiementSalaire y WHERE y.mutualiste.mutuelle = :mutuelle AND y.periode.actif=true ORDER BY y.datePaiement DESC, y.mutualiste.employe.nom"),
//    @NamedQuery(name = "YvsMutPaiementSalaire.findByMutualiste", query = "SELECT y FROM YvsMutPaiementSalaire y WHERE y.mutualiste = :mutualiste"),
//    @NamedQuery(name = "YvsMutPaiementSalaire.findByMutualisteDates", query = "SELECT y FROM YvsMutPaiementSalaire y WHERE y.mutualiste = :mutualiste AND y.datePaiement BETWEEN :dateDebut AND :dateFin ORDER BY y.datePaiement DESC"),
//    @NamedQuery(name = "YvsMutPaiementSalaire.findHistoriquePaiement", query = "SELECT y FROM YvsMutPaiementSalaire y WHERE y.mutualiste = :mutualiste AND y.datePaiement BETWEEN :dateDebut AND :dateFin AND y.payer=true ORDER BY y.datePaiement DESC"),
//    @NamedQuery(name = "YvsMutPaiementSalaire.findByMutualistePeriode", query = "SELECT y FROM YvsMutPaiementSalaire y WHERE y.mutualiste = :mutualiste AND y.periode.periodeRh=:periode"),
//    @NamedQuery(name = "YvsMutPaiementSalaire.findById", query = "SELECT y FROM YvsMutPaiementSalaire y WHERE y.id = :id"),
//    @NamedQuery(name = "YvsMutPaiementSalaire.findByDatePaiement", query = "SELECT y FROM YvsMutPaiementSalaire y WHERE y.datePaiement = :datePaiement"),
//    @NamedQuery(name = "YvsMutPaiementSalaire.findBetweenDate", query = "SELECT y FROM YvsMutPaiementSalaire y WHERE y.mutualiste.mutuelle=:mutuelle AND y.datePaiement BETWEEN :date1 AND :date2"),
//    @NamedQuery(name = "YvsMutPaiementSalaire.findByMontant", query = "SELECT y FROM YvsMutPaiementSalaire y WHERE y.montantPaye = :montant"),
//    //@NamedQuery(name = "YvsMutPaiementSalaire.findByPeriode", query = "SELECT y FROM YvsMutPaiementSalaire y WHERE y.periode = :periode AND y.mutualiste.mutuelle=:mutuelle"),
//    //@NamedQuery(name = "YvsMutPaiementSalaire.findEmployeNonPaye", query = "SELECT y.mutualiste FROM YvsMutPaiementSalaire y JOIN y.mutualiste m ON m.id=y.mutualiste.id WHERE y.mutualiste.mutuelle=:mutuelle"),
//    @NamedQuery(name = "YvsMutPaiementSalaire.findEmployeAndSession", query = "SELECT y FROM YvsMutPaiementSalaire y WHERE y.mutualiste.employe.matricule=:matricule AND y.periode=:periode"),
//    @NamedQuery(name = "YvsMutPaiementSalaire.findByCommentaire", query = "SELECT y FROM YvsMutPaiementSalaire y WHERE y.commentaire = :commentaire")})
//public class YvsMutPaiementSalaire implements Serializable {
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
//    @Column(name = "date_paiement")
//    @Temporal(TemporalType.DATE)
//    private Date datePaiement;
//    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
//    @Column(name = "montant_paye")
//    private Double montantPaye;
//    @Column(name = "salaire")
//    private Double salaire;
//    @Size(max = 2147483647)
//    @Column(name = "commentaire")
//    private String commentaire;
//    @Column(name = "credit_retenu")
//    private Double creditRetenu;
//    @Column(name = "avance_salaire_retenu")
//    private Double avanceSalaireRetenu;
//    @Column(name = "epargne_du_mois")
//    private Double epargneDuMois;
//    @Column(name = "payer")
//    private Boolean payer;
//    @Column(name = "date_save")
//    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
//    private Date dateSave;
//    @JoinColumn(name = "mutualiste", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsMutMutualiste mutualiste;
//    @JoinColumn(name = "periode", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsMutPeriodeSalaire periode;
//    @JoinColumn(name = "author", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsUsersAgence author;
//    @Transient
//    private boolean selectActif;
//    @Transient
//    private boolean new_;
//    @Transient
//    private String chainePeriode;
//    @Transient
//    private double montantAPayer;
//    @Transient
//    private double soeAcompte;
//    @Transient
//    private double soeCredit;
//
//    public YvsMutPaiementSalaire() {
//    }
//
//    public YvsMutPaiementSalaire(Long id) {
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
//    public double getSoeAcompte() {
//        return soeAcompte;
//    }
//
//    public void setSoeAcompte(double soeAcompte) {
//        this.soeAcompte = soeAcompte;
//    }
//
//    public double getSoeCredit() {
//        return soeCredit;
//    }
//
//    public void setSoeCredit(double soeCredit) {
//        this.soeCredit = soeCredit;
//    }
//
//    public double getMontantAPayer() {
//        return montantAPayer;
//    }
//
//    public void setMontantAPayer(double montantAPayer) {
//        this.montantAPayer = montantAPayer;
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
//    public String getChainePeriode() {
//        return chainePeriode;
//    }
//
//    public void setChainePeriode(String chainePeriode) {
//        this.chainePeriode = chainePeriode;
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
//    public Date getDatePaiement() {
//        return datePaiement;
//    }
//
//    public void setDatePaiement(Date datePaiement) {
//        this.datePaiement = datePaiement;
//    }
//
//    public Double getMontantPaye() {
//        return montantPaye != null ? montantPaye : 0;
//    }
//
//    public void setMontantPaye(Double montantPaye) {
//        this.montantPaye = montantPaye;
//    }
//
//    public String getCommentaire() {
//        return commentaire;
//    }
//
//    public void setCommentaire(String commentaire) {
//        this.commentaire = commentaire;
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
//    public void setPeriode(YvsMutPeriodeSalaire periode) {
//        this.periode = periode;
//    }
//
//    public YvsMutPeriodeSalaire getPeriode() {
//        return periode;
//    }
//
//    public Double getSalaire() {
//        return salaire != null ? salaire : 0;
//    }
//
//    public void setSalaire(Double salaire) {
//        this.salaire = salaire;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }
//
//    public Double getCreditRetenu() {
//        return creditRetenu != null ? creditRetenu : 0;
//    }
//
//    public void setCreditRetenu(Double creditRetenu) {
//        this.creditRetenu = creditRetenu;
//    }
//
//    public Double getAvanceSalaireRetenu() {
//        return avanceSalaireRetenu != null ? avanceSalaireRetenu : 0;
//    }
//
//    public void setAvanceSalaireRetenu(Double avanceSalaireRetenu) {
//        this.avanceSalaireRetenu = avanceSalaireRetenu;
//    }
//
//    public Double getEpargneDuMois() {
//        return epargneDuMois != null ? epargneDuMois : 0;
//    }
//
//    public void setEpargneDuMois(Double epargneDuMois) {
//        this.epargneDuMois = epargneDuMois;
//    }
//
//    public Boolean getPayer() {
//        return payer != null ? payer : false;
//    }
//
//    public void setPayer(Boolean payer) {
//        this.payer = payer;
//    }
//
//    public YvsUsersAgence getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(YvsUsersAgence author) {
//        this.author = author;
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
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof YvsMutPaiementSalaire)) {
//            return false;
//        }
//        YvsMutPaiementSalaire other = (YvsMutPaiementSalaire) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "yvs.entity.mutuelle.salaire.YvsMutPaiementSalaire[ id=" + id + " ]";
//    }
//
//}
