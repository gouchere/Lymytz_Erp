/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.operation;

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
import javax.validation.constraints.Size;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.mutuelle.YvsMutPeriodeExercice;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_operation_compte")
@NamedQueries({
    @NamedQuery(name = "YvsMutOperationCompte.findAll", query = "SELECT y FROM YvsMutOperationCompte y WHERE y.compte.mutualiste.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutOperationCompte.countAll", query = "SELECT COUNT(y) FROM YvsMutOperationCompte y WHERE y.compte.mutualiste.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutOperationCompte.findById", query = "SELECT y FROM YvsMutOperationCompte y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutOperationCompte.findByNumOp", query = "SELECT y FROM YvsMutOperationCompte y WHERE y.referenceOperation LIKE :numeroPiece AND y.compte.mutualiste.mutuelle=:mutuelle ORDER BY y.referenceOperation DESC"),
    @NamedQuery(name = "YvsMutOperationCompte.findEpargnePeriode", query = "SELECT COUNT(y) FROM YvsMutOperationCompte y WHERE y.compte=:compte AND y.periode=:periode AND y.nature=:nature"),
    @NamedQuery(name = "YvsMutOperationCompte.findEpargnePeriode_", query = "SELECT y FROM YvsMutOperationCompte y WHERE y.compte=:compte AND y.periode=:periode AND y.nature=:nature"),
    @NamedQuery(name = "YvsMutOperationCompte.findByMutuelle", query = "SELECT y FROM YvsMutOperationCompte y WHERE y.compte.mutualiste.mutuelle = :mutuelle"),
    @NamedQuery(name = "YvsMutOperationCompte.findByMutuelleDates", query = "SELECT y FROM YvsMutOperationCompte y WHERE y.compte.mutualiste.mutuelle = :mutuelle AND y.nature = :nature AND y.dateOperation BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsMutOperationCompte.findByMutualistePeriode", query = "SELECT y FROM YvsMutOperationCompte y WHERE y.compte.mutualiste = :mutualiste AND y.periode=:periode"),
    @NamedQuery(name = "YvsMutOperationCompte.findByMutualiste", query = "SELECT y FROM YvsMutOperationCompte y WHERE y.compte.mutualiste = :mutualiste ORDER BY y.periode.dateFin DESC, y.dateOperation DESC"),
    @NamedQuery(name = "YvsMutOperationCompte.findByMutualisteDates_", query = "SELECT y FROM YvsMutOperationCompte y WHERE y.compte.mutualiste = :mutualiste AND y.nature = :nature AND (y.dateOperation BETWEEN :dateDebut AND :dateFin) ORDER BY y.dateOperation DESC"),
    @NamedQuery(name = "YvsMutOperationCompte.findByMutualisteDates", query = "SELECT y FROM YvsMutOperationCompte y WHERE y.compte.mutualiste = :mutualiste AND y.nature = :nature AND (y.dateOperation BETWEEN :dateDebut AND :dateFin) AND y.epargneMensuel = TRUE ORDER BY y.dateOperation DESC"),
    @NamedQuery(name = "YvsMutOperationCompte.findByMutuelleDepot", query = "SELECT y FROM YvsMutOperationCompte y WHERE y.compte.mutualiste.mutuelle = :mutuelle AND y.nature = 'Depot' ORDER BY y.dateOperation DESC, y.compte.mutualiste.employe.prenom ASC"),
    @NamedQuery(name = "YvsMutOperationCompte.findByCompte", query = "SELECT y FROM YvsMutOperationCompte y WHERE y.compte = :compte AND y.nature = :nature"),
    @NamedQuery(name = "YvsMutOperationCompte.findByDateOperation", query = "SELECT y FROM YvsMutOperationCompte y WHERE y.dateOperation = :dateOperation"),
    @NamedQuery(name = "YvsMutOperationCompte.findByMontant", query = "SELECT y FROM YvsMutOperationCompte y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsMutOperationCompte.findByCodeOp", query = "SELECT y FROM YvsMutOperationCompte y WHERE y.codeOperation = :codeOperation AND y.periode=:periode"),
    /*Total des mouvements d'un compte (Entrée ou sortie)*/
    @NamedQuery(name = "YvsMutOperationCompte.findTotalCompte", query = "SELECT SUM(y.montant) FROM YvsMutOperationCompte y WHERE y.compte=:compte AND y.sensOperation=:mouvement"),
    /*Solde d'un type de compte pour un mutualiste*/
    @NamedQuery(name = "YvsMutOperationCompte.findSoldeByOperationMutualiste", query = "SELECT SUM(y.montant) FROM YvsMutOperationCompte y WHERE y.compte.mutualiste = :mutualiste AND y.compte.typeCompte.nature = :nature AND y.sensOperation=:mouvement"),
    /*Solde d'un type de compte pour un mutualiste à une date donnée*/
    @NamedQuery(name = "YvsMutOperationCompte.findSumCompteDate", query = "SELECT SUM(y.montant) FROM YvsMutOperationCompte y WHERE y.compte.mutualiste = :mutualiste AND y.compte.typeCompte.nature = :nature AND y.periode.dateFin <= :date AND y.sensOperation=:mouvement"),
    @NamedQuery(name = "YvsMutOperationCompte.findCumulTypeOperationByNaturePeriodeAll", query = "SELECT SUM(y.montant) FROM YvsMutOperationCompte y WHERE y.compte.mutualiste.mutuelle = :mutuelle AND y.compte.typeCompte.nature=:nature AND y.sensOperation=:mouvement AND y.periode.dateFin<=:date"),
    /*Epargne du mutualiste à une période*/
    @NamedQuery(name = "YvsMutOperationCompte.findSommeTypeOperationByNaturePeriode", query = "SELECT SUM(y.montant) FROM YvsMutOperationCompte y WHERE y.compte.mutualiste = :mutualiste AND y.compte.typeCompte.nature=:nature AND y.sensOperation=:mouvement AND y.periode.dateFin BETWEEN :debut AND :fin"),
    @NamedQuery(name = "YvsMutOperationCompte.findSommeTypeOperationByNaturePeriodeAll", query = "SELECT SUM(y.montant) FROM YvsMutOperationCompte y WHERE y.compte.mutualiste.mutuelle = :mutuelle AND y.compte.typeCompte.nature=:nature AND y.sensOperation=:mouvement AND y.periode.dateFin BETWEEN :debut AND :fin"),

    /*Epargne total du mutualiste*/
    @NamedQuery(name = "YvsMutOperationCompte.findSoldeTypeCompteMutualiste", query = "SELECT SUM(y.montant) FROM YvsMutOperationCompte y WHERE y.compte.typeCompte.nature=:nature AND y.compte.mutualiste=:mutualiste AND y.sensOperation=:mouvement"),
    @NamedQuery(name = "YvsMutOperationCompte.findSalaireMoyen", query = "SELECT y FROM YvsMutOperationCompte y WHERE y.compte.mutualiste = :mutualiste AND y.compte.typeCompte.nature=:natureCpte AND y.dateOperation <=:date AND y.sensOperation=:mouvement AND y.nature=:natureOp ORDER BY y.periode.dateDebut DESC"),
    @NamedQuery(name = "YvsMutOperationCompte.findByNature", query = "SELECT y FROM YvsMutOperationCompte y WHERE y.nature = :nature")})
public class YvsMutOperationCompte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_operation_compte_id_seq", name = "yvs_mut_operation_compte_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_operation_compte_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_operation")
    @Temporal(TemporalType.DATE)
    private Date dateOperation;
    @Size(max = 2147483647)
    @Column(name = "nature")
    private String nature;
    @Column(name = "sens_operation")
    private String sensOperation;
    @Column(name = "epargne_mensuel")
    private Boolean epargneMensuel;
    @Column(name = "table_source")
    private String tableSource;
    @Column(name = "reference_operation")
    private String referenceOperation;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "souce_reglement")
    private Long souceReglement;
    @Column(name = "heure_operation")
    @Temporal(TemporalType.TIME)
    private Date heureOperation;
    @Size(max = 2147483647)
    @Column(name = "commentaire")
    private String commentaire;
    @Column(name = "code_operation")
    private String codeOperation;   // Permet lors de l'enregistrement des salaire de faire le lien avec d'autres ligne (Retenues, epargne mensuelle, mensualite)
    @Column(name = "automatique")
    private Boolean automatique;

    @JoinColumn(name = "compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCompte compte;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCaisse caisse;
    @JoinColumn(name = "periode", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutPeriodeExercice periode;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private boolean applyRetenu = true;
    @Transient
    private double montantEpargne = 0;
    @Transient
    private double retenueFixe = 0;
    @Transient
    private boolean epargneSave;
    @Transient
    private boolean retenuSave;
    @Transient
    private boolean mensualiteSave;
    @Transient
    private boolean salaireSave;

    public YvsMutOperationCompte() {
    }

    public YvsMutOperationCompte(Long id) {
        this.id = id;
    }

    public YvsMutOperationCompte(Long id, YvsMutCompte compte, YvsMutPeriodeExercice periode, Double montant) {
        this(id);
        this.montant = montant;
        this.compte = compte;
        this.periode = periode;
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

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public String getTableSource() {
        return tableSource;
    }

    public void setTableSource(String tableSource) {
        this.tableSource = tableSource;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(Date dateOperation) {
        this.dateOperation = dateOperation;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public YvsMutCompte getCompte() {
        return compte;
    }

    public void setCompte(YvsMutCompte compte) {
        this.compte = compte;
    }

    public void setEpargneMensuel(Boolean epargneMensuel) {
        this.epargneMensuel = epargneMensuel;
    }

    public Boolean getEpargneMensuel() {
        return this.epargneMensuel;
    }

    public Long getSouceReglement() {
        return souceReglement;
    }

    public void setSouceReglement(Long souceReglement) {
        this.souceReglement = souceReglement;
    }

    public YvsMutCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsMutCaisse caisse) {
        this.caisse = caisse;
    }

    public YvsMutPeriodeExercice getPeriode() {
        return periode;
    }

    public String NamePeriode() {
        if (getPeriode() != null ? getPeriode().getId() > 0 : false) {
            return getPeriode().getReferencePeriode();
        }
        return "";
    }

    public void setPeriode(YvsMutPeriodeExercice periode) {
        this.periode = periode;
    }

    public String getReferenceOperation() {
        return referenceOperation != null ? referenceOperation : "";
    }

    public void setReferenceOperation(String referenceOperation) {
        this.referenceOperation = referenceOperation;
    }

    public String getSensOperation() {
        return sensOperation;
    }

    public void setSensOperation(String sensOperation) {
        this.sensOperation = sensOperation;
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
        if (!(object instanceof YvsMutOperationCompte)) {
            return false;
        }
        YvsMutOperationCompte other = (YvsMutOperationCompte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.base.YvsMutOperationCompte[ id=" + id + " ]";
    }

    public Date getHeureOperation() {
        return heureOperation;
    }

    public void setHeureOperation(Date heureOperation) {
        this.heureOperation = heureOperation;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Boolean getAutomatique() {
        return automatique != null ? automatique : false;
    }

    public void setAutomatique(Boolean automatique) {
        this.automatique = automatique;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public boolean isApplyRetenu() {
        return applyRetenu;
    }

    public void setApplyRetenu(boolean applyRetenu) {
        this.applyRetenu = applyRetenu;
    }

    public double getMontantEpargne() {
        return montantEpargne;
    }

    public void setMontantEpargne(double montantEpargne) {
        this.montantEpargne = montantEpargne;
    }

    public double getRetenueFixe() {
        return retenueFixe;
    }

    public void setRetenueFixe(double retenueFixe) {
        this.retenueFixe = retenueFixe;
    }

    public boolean isEpargneSave() {
        return epargneSave;
    }

    public void setEpargneSave(boolean epargneSave) {
        this.epargneSave = epargneSave;
    }

    public boolean isRetenuSave() {
        return retenuSave;
    }

    public void setRetenuSave(boolean retenuSave) {
        this.retenuSave = retenuSave;
    }

    public boolean isMensualiteSave() {
        return mensualiteSave;
    }

    public void setMensualiteSave(boolean mensualiteSave) {
        this.mensualiteSave = mensualiteSave;
    }

    public boolean isSalaireSave() {
        return salaireSave;
    }

    public void setSalaireSave(boolean salaireSave) {
        this.salaireSave = salaireSave;
    }

    public String getCodeOperation() {
        return codeOperation;
    }

    public void setCodeOperation(String codeOperation) {
        this.codeOperation = codeOperation;
    }

}
