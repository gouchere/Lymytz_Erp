/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_parametre")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsComptaParametre.findAll", query = "SELECT y FROM YvsComptaParametre y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsComptaParametre.findById", query = "SELECT y FROM YvsComptaParametre y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaParametre.findByTailleCompte", query = "SELECT y FROM YvsComptaParametre y WHERE y.tailleCompte = :tailleCompte"),
    @NamedQuery(name = "YvsComptaParametre.findByMultipleArrondi", query = "SELECT y FROM YvsComptaParametre y WHERE y.multipleArrondi = :multipleArrondi"),
    @NamedQuery(name = "YvsComptaParametre.findByDecimalArrondi", query = "SELECT y FROM YvsComptaParametre y WHERE y.decimalArrondi = :decimalArrondi"),
    @NamedQuery(name = "YvsComptaParametre.findByModeArrondi", query = "SELECT y FROM YvsComptaParametre y WHERE y.modeArrondi = :modeArrondi")})
public class YvsComptaParametre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_parametre_id_seq", name = "yvs_compta_parametre_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_parametre_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "taille_compte")
    private Integer tailleCompte;
    @Column(name = "valeur_arrondi")
    private Integer valeurArrondi;
    @Column(name = "converter")
    private Integer converter = 0;
    @Column(name = "ecart_day_solde_client")
    private Integer ecartDaySoldeClient;
    @Column(name = "jour_anterieur")
    private Integer jourAnterieur;
    @Column(name = "jour_antidater_paiement")
    private Integer jourAntidaterPaiement;
    @Column(name = "jour_anterieur_cancel")
    private Integer jourAnterieurCancel;
    @Column(name = "nombre_ligne_solde_client")
    private Integer nombreLigneSoldeClient;
    @Column(name = "montant_seuil_depense_od")
    private Double montantSeuilDepenseOd = 0D;
    @Column(name = "montant_seuil_recette_od")
    private Double montantSeuilRecetteOd = 0D;
    @Column(name = "valeur_limite_arrondi")
    private Double valeurLimiteArrondi = 0D;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "multiple_arrondi")
    private Double multipleArrondi;
    @Column(name = "decimal_arrondi")
    private Boolean decimalArrondi;
    @Column(name = "report_by_agence")
    private Boolean reportByAgence;
    @Size(max = 2147483647)
    @Column(name = "mode_arrondi")
    private String modeArrondi;
    @Column(name = "maj_compta_auto_divers")
    private Boolean majComptaAutoDivers;
    @Column(name = "maj_compta_statut_divers")
    private Character majComptaStatutDivers;
    @Column(name = "plafond_bp")
    private Double plafondBP;   //plafond des bons provisoire: (on ne valide pas de nouveaux bp si la somme de ceux non justifié dépasse ce montant
    @Column(name = "nb_max_bp")
    private Integer nbMaxBp;   //nombre de bp non justifié autorisé

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "compte_benefice_report", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compteBeneficeReport;
    @JoinColumn(name = "compte_perte_report", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable comptePerteReport;
    @JoinColumn(name = "journal_report", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaJournaux journalReport;

    public YvsComptaParametre() {
    }

    public YvsComptaParametre(Integer id) {
        this.id = id;
    }

    public Double getValeurLimiteArrondi() {
        return valeurLimiteArrondi != null ? valeurLimiteArrondi : 0;
    }

    public void setValeurLimiteArrondi(Double valeurLimiteArrondi) {
        this.valeurLimiteArrondi = valeurLimiteArrondi;
    }

    public Integer getConverter() {
        return converter != null ? converter : 0;
    }

    public void setConverter(Integer converter) {
        this.converter = converter;
    }

    public Integer getEcartDaySoldeClient() {
        return ecartDaySoldeClient != null ? ecartDaySoldeClient : 7;
    }

    public void setEcartDaySoldeClient(Integer ecartDaySoldeClient) {
        this.ecartDaySoldeClient = ecartDaySoldeClient;
    }

    public Integer getNombreLigneSoldeClient() {
        return nombreLigneSoldeClient != null ? nombreLigneSoldeClient : 4;
    }

    public void setNombreLigneSoldeClient(Integer nombreLigneSoldeClient) {
        this.nombreLigneSoldeClient = nombreLigneSoldeClient;
    }

    public Integer getJourAnterieur() {
        return jourAnterieur != null ? jourAnterieur : 0;
    }

    public void setJourAnterieur(Integer jourAnterieur) {
        this.jourAnterieur = jourAnterieur;
    }

    public Integer getJourAnterieurCancel() {
        return jourAnterieurCancel != null ? jourAnterieurCancel : 0;
    }

    public void setJourAnterieurCancel(Integer jourAnterieurCancel) {
        this.jourAnterieurCancel = jourAnterieurCancel;
    }

    public Integer getJourAntidaterPaiement() {
        return jourAntidaterPaiement != null ? jourAntidaterPaiement : -1;
    }

    public void setJourAntidaterPaiement(Integer jourAntidaterPaiement) {
        this.jourAntidaterPaiement = jourAntidaterPaiement;
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

    public Boolean getReportByAgence() {
        return reportByAgence != null ? reportByAgence : false;
    }

    public void setReportByAgence(Boolean reportByAgence) {
        this.reportByAgence = reportByAgence;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValeurArrondi() {
        return valeurArrondi != null ? valeurArrondi : 0;
    }

    public void setValeurArrondi(Integer valeurArrondi) {
        this.valeurArrondi = valeurArrondi;
    }

    public Integer getTailleCompte() {
        return tailleCompte != null ? tailleCompte : 0;
    }

    public void setTailleCompte(Integer tailleCompte) {
        this.tailleCompte = tailleCompte;
    }

    public Double getMultipleArrondi() {
        return multipleArrondi != null ? multipleArrondi : 0;
    }

    public void setMultipleArrondi(Double multipleArrondi) {
        this.multipleArrondi = multipleArrondi;
    }

    public Boolean getDecimalArrondi() {
        return decimalArrondi != null ? decimalArrondi : false;
    }

    public void setDecimalArrondi(Boolean decimalArrondi) {
        this.decimalArrondi = decimalArrondi;
    }

    public Boolean getMajComptaAutoDivers() {
        return majComptaAutoDivers != null ? majComptaAutoDivers : false;
    }

    public Double getMontantSeuilDepenseOd() {
        return montantSeuilDepenseOd != null ? montantSeuilDepenseOd : 0;
    }

    public void setMontantSeuilDepenseOd(Double montantSeuilDepenseOd) {
        this.montantSeuilDepenseOd = montantSeuilDepenseOd;
    }

    public Double getMontantSeuilRecetteOd() {
        return montantSeuilRecetteOd != null ? montantSeuilRecetteOd : 0;
    }

    public void setMontantSeuilRecetteOd(Double montantSeuilRecetteOd) {
        this.montantSeuilRecetteOd = montantSeuilRecetteOd;
    }

    public void setMajComptaAutoDivers(Boolean majComptaAutoDivers) {
        this.majComptaAutoDivers = majComptaAutoDivers;
    }

    public Character getMajComptaStatutDivers() {
        return majComptaStatutDivers != null ? String.valueOf(majComptaStatutDivers).trim().length() > 0 ? majComptaStatutDivers : Constantes.STATUT_DOC_PAYER : Constantes.STATUT_DOC_PAYER;
    }

    public void setMajComptaStatutDivers(Character majComptaStatutDivers) {
        this.majComptaStatutDivers = majComptaStatutDivers;
    }

    public String getModeArrondi() {
        return modeArrondi != null ? modeArrondi.trim().length() > 0 ? modeArrondi : "A" : "A";
    }

    public void setModeArrondi(String modeArrondi) {
        this.modeArrondi = modeArrondi;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsBasePlanComptable getCompteBeneficeReport() {
        return compteBeneficeReport;
    }

    public void setCompteBeneficeReport(YvsBasePlanComptable compteBeneficeReport) {
        this.compteBeneficeReport = compteBeneficeReport;
    }

    public YvsBasePlanComptable getComptePerteReport() {
        return comptePerteReport;
    }

    public void setComptePerteReport(YvsBasePlanComptable comptePerteReport) {
        this.comptePerteReport = comptePerteReport;
    }

    public YvsComptaJournaux getJournalReport() {
        return journalReport;
    }

    public void setJournalReport(YvsComptaJournaux journalReport) {
        this.journalReport = journalReport;
    }

    public Double getPlafondBP() {
        return plafondBP != null ? plafondBP : 0;
    }

    public void setPlafondBP(Double plafondBP) {
        this.plafondBP = plafondBP;
    }

    public Integer getNbMaxBp() {
        return nbMaxBp;
    }

    public void setNbMaxBp(Integer nbMaxBp) {
        this.nbMaxBp = nbMaxBp;
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
        if (!(object instanceof YvsComptaParametre)) {
            return false;
        }
        YvsComptaParametre other = (YvsComptaParametre) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaParametre[ id=" + id + " ]";
    }

}
