///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.entity.mutuelle.operation;
//
//import yvs.entity.mutuelle.base.YvsMutTiers;
//import yvs.entity.mutuelle.YvsMutCaisse;
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
//import yvs.entity.users.YvsUsers;
//
///**
// *
// * @author lymytz
// */
//@Entity
//@Table(name = "yvs_mut_operation_caisse")
//@NamedQueries({
//    @NamedQuery(name = "YvsMutOperationCaisse.findAll", query = "SELECT y FROM YvsMutOperationCaisse y WHERE y.caisse.mutuelle.societe = :societe"),
//    @NamedQuery(name = "YvsMutOperationCaisse.findById", query = "SELECT y FROM YvsMutOperationCaisse y WHERE y.id = :id"),
//    @NamedQuery(name = "YvsMutOperationCaisse.findSoldeMutuelle", query = "SELECT SUM(y.montant) FROM YvsMutOperationCaisse y WHERE y.caisse.mutuelle = :mutuelle AND y.nature=:nature AND y.dateOperation BETWEEN :debut AND :fin"),
//    @NamedQuery(name = "YvsMutOperationCaisse.findByMutuelle", query = "SELECT y FROM YvsMutOperationCaisse y WHERE y.caisse.mutuelle = :mutuelle"),
//    @NamedQuery(name = "YvsMutOperationCaisse.findByDateOperation", query = "SELECT y FROM YvsMutOperationCaisse y WHERE y.dateOperation = :dateOperation"),
//    @NamedQuery(name = "YvsMutOperationCaisse.findByNature", query = "SELECT y FROM YvsMutOperationCaisse y WHERE y.nature = :nature"),
//    @NamedQuery(name = "YvsMutOperationCaisse.findByMontant", query = "SELECT y FROM YvsMutOperationCaisse y WHERE y.montant = :montant")})
//public class YvsMutOperationCaisse implements Serializable {
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
//    @Column(name = "date_operation")
//    @Temporal(TemporalType.DATE)
//    private Date dateOperation;
//    @Size(max = 2147483647)
//    @Column(name = "nature")
//    private String nature;
//    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
//    @Column(name = "montant")
//    private Double montant;
//    @Column(name = "heure_operation")
//    @Temporal(TemporalType.TIME)
//    private Date heureOperation;
//    @Size(max = 2147483647)
//    @Column(name = "commentaire")
//    private String commentaire;
//    @Column(name = "automatique")
//    private Boolean automatique;
//    @JoinColumn(name = "author", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsUsers author;
//    @JoinColumn(name = "caisse", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsMutCaisse caisse;
//    @JoinColumn(name = "source", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsMutTiers source;
//    @Transient
//    private boolean selectActif;
//    @Transient
//    private boolean new_;
//
//    public YvsMutOperationCaisse() {
//    }
//
//    public YvsMutOperationCaisse(Long id) {
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
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Date getDateOperation() {
//        return dateOperation;
//    }
//
//    public void setDateOperation(Date dateOperation) {
//        this.dateOperation = dateOperation;
//    }
//
//    public String getNature() {
//        return nature;
//    }
//
//    public void setNature(String nature) {
//        this.nature = nature;
//    }
//
//    public YvsUsers getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(YvsUsers author) {
//        this.author = author;
//    }
//
//    public YvsMutCaisse getCaisse() {
//        return caisse;
//    }
//
//    public void setCaisse(YvsMutCaisse caisse) {
//        this.caisse = caisse;
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
//        if (!(object instanceof YvsMutOperationCaisse)) {
//            return false;
//        }
//        YvsMutOperationCaisse other = (YvsMutOperationCaisse) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "yvs.entity.mutuelle.operation.YvsMutOperationCaisse[ id=" + id + " ]";
//    }
//
//    public Date getHeureOperation() {
//        return heureOperation;
//    }
//
//    public void setHeureOperation(Date heureOperation) {
//        this.heureOperation = heureOperation;
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
//    public Boolean getAutomatique() {
//        return automatique;
//    }
//
//    public void setAutomatique(Boolean automatique) {
//        this.automatique = automatique;
//    }
//
//    public YvsMutTiers getSource() {
//        return source;
//    }
//
//    public void setSource(YvsMutTiers source) {
//        this.source = source;
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
//}
