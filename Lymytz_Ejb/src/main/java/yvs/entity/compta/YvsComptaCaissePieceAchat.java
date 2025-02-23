/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import yvs.dao.YvsEntity;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.compta.achat.YvsComptaNotifReglementAchat;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.compta.divers.YvsComptaJustifBonAchat;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceAchat;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_caisse_piece_achat", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findAll", query = "SELECT y FROM YvsComptaCaissePieceAchat y WHERE y.author.agence.societe=:societe"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.countAll", query = "SELECT COUNT(y) FROM YvsComptaCaissePieceAchat y WHERE y.author.agence.societe=:societe"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findById", query = "SELECT y FROM YvsComptaCaissePieceAchat y JOIN FETCH y.achat LEFT JOIN FETCH y.caisse LEFT JOIN FETCH y.model JOIN FETCH y.achat.fournisseur LEFT JOIN FETCH y.achat.categorieComptable  WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findByIds", query = "SELECT y FROM YvsComptaCaissePieceAchat y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findByFacture", query = "SELECT y FROM YvsComptaCaissePieceAchat y WHERE y.achat = :facture"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findByFactures", query = "SELECT y FROM YvsComptaCaissePieceAchat y WHERE y.achat.id IN :factures"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findByFactureStatut", query = "SELECT y FROM YvsComptaCaissePieceAchat y WHERE y.achat = :facture AND y.statutPiece = :statut"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findIdByFactureStatut", query = "SELECT y.id FROM YvsComptaCaissePieceAchat y WHERE y.achat = :facture AND y.statutPiece = :statut"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findByFactureStatutS", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceAchat y WHERE y.achat = :facture AND y.statutPiece = :statut"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findByMensualite", query = "SELECT y FROM YvsComptaCaissePieceAchat y WHERE y.achat = :achat"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findByMensualiteStatut", query = "SELECT y FROM YvsComptaCaissePieceAchat y WHERE y.achat = :achat AND y.statutPiece = :statu"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findByMensualiteStatutS", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceAchat y WHERE y.achat = :facture AND y.statutPiece = :statut AND y.datePaiement <= :date"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findByNumeroPiece", query = "SELECT y FROM YvsComptaCaissePieceAchat y WHERE y.numeroPiece = :numeroPiece AND y.achat=:docAchat"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findByMontant", query = "SELECT y FROM YvsComptaCaissePieceAchat y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findSumMontantByCaisse", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceAchat y WHERE y.caisse = :caisse"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findByStatutPiece", query = "SELECT y FROM YvsComptaCaissePieceAchat y WHERE y.statutPiece = :statutPiece"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findByFacturesRegle", query = "SELECT y FROM YvsComptaCaissePieceAchat y WHERE y.statutPiece=:statutPiece AND (y.achat.fournisseur.tiers=:tiers AND y.achat.typeDoc=:typeDoc AND y.achat.statut IN :statut AND y.achat.statutRegle IN :statutRegle)"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findByStatutSameDates", query = "SELECT y FROM YvsComptaCaissePieceAchat y WHERE y.achat = :docAchat AND y.statutPiece = :statut AND y.datePiece = y.achat.dateDoc ORDER BY y.datePiece"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findByStatutDiffDates", query = "SELECT y FROM YvsComptaCaissePieceAchat y WHERE y.achat = :docAchat AND y.statutPiece = :statut AND y.datePiece != y.achat.dateDoc ORDER BY y.datePiece"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findByFactureTypeStatut", query = "SELECT y FROM YvsComptaCaissePieceAchat y WHERE y.achat = :achat AND (y.model IS NULL OR y.model.typeReglement = :type) AND y.statutPiece = :statut"),

    @NamedQuery(name = "YvsComptaCaissePieceAchat.findComptabiliseById", query = "SELECT y.comptabilise FROM YvsComptaCaissePieceAchat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findByNumPiece", query = "SELECT y FROM YvsComptaCaissePieceAchat y WHERE y.achat.agence.societe = :societe AND y.numeroPiece LIKE :numero ORDER BY y.numeroPiece DESC"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.sumByFournisseurAgenceDate", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceAchat y WHERE y.achat.agence = :agence AND y.achat.fournisseur = :fournisseur AND y.statutPiece = 'P' AND y.datePaiement <= :dateFin"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.sumByFournisseurDate", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceAchat y WHERE y.achat.fournisseur = :fournisseur AND y.statutPiece = 'P' AND y.datePaiement <= :dateFin"),

    @NamedQuery(name = "YvsComptaCaissePieceAchat.findSumByFournisseurDate", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceAchat y WHERE y.achat.fournisseur = :fournisseur AND y.statutPiece = :statut AND y.datePaiement <= :dateFin"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findSumByFournisseurDates", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceAchat y WHERE y.achat.fournisseur = :fournisseur AND y.statutPiece = :statut AND y.datePaiement BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findSumByFournisseurDatesAgence", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceAchat y WHERE y.achat.agence = :agence AND y.achat.fournisseur = :fournisseur AND y.statutPiece = :statut AND y.datePaiement BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findSumByFournisseur", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceAchat y WHERE y.achat.fournisseur = :fournisseur AND y.statutPiece = 'P'"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.countByParent", query = "SELECT COUNT(y) FROM YvsComptaCaissePieceAchat y WHERE y.parent=:piece"),
    @NamedQuery(name = "YvsComptaCaissePieceAchat.findFactureByTiersPaiement", query = "SELECT DISTINCT y.achat FROM YvsComptaCaissePieceAchat y WHERE y.achat.fournisseur = :fournisseur AND y.achat.dateDoc >= :dateDebut AND y.datePaiement <= :dateFin AND y.statutPiece = :statut ORDER BY y.achat.dateDoc, y.datePaiement DESC")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsComptaCaissePieceAchat extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_caisse_piece_achat_id_seq", name = "yvs_compta_caisse_piece_achat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_caisse_piece_achat_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "montant")
    private Double montant;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "numero_piece")
    private String numeroPiece;
    @Column(name = "statut_piece")
    private Character statutPiece;
    @Size(max = 2147483647)
    @Column(name = "note")
    private String note;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @Column(name = "date_piece")
    @Temporal(TemporalType.DATE)
    private Date datePiece;
    @Column(name = "date_paiement")
    @Temporal(TemporalType.DATE)
    private Date datePaiement;
    @Column(name = "date_paiment_prevu")
    @Temporal(TemporalType.DATE)
    private Date datePaimentPrevu;
    @Column(name = "reference_externe")
    private String referenceExterne;

    @OneToOne(mappedBy = "reglement")
    private YvsComptaContentJournalPieceAchat pieceContenu;

    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModeReglement model;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JsonManagedReference
    @JoinColumn(name = "achat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocAchats achat;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisse = new YvsBaseCaisse();
    @JoinColumn(name = "caissier", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers caissier;
    @JoinColumn(name = "fournisseur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseFournisseur fournisseur;
    @JsonManagedReference
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaissePieceAchat parent;

    @OneToOne(mappedBy = "piece")
    private YvsComptaJustifBonAchat justify;

    @OneToMany(mappedBy = "parent")
    private List<YvsComptaCaissePieceAchat> sousAchats;
    @OneToOne(mappedBy = "pieceAchat")
    private YvsComptaNotifReglementAchat notifs;
    @OneToMany(mappedBy = "achat")
    private List<YvsComptaCaissePieceCompensation> compensations;
    @OneToMany(mappedBy = "pieceAchat")
    private List<YvsComptaPhasePieceAchat> phasesReglement;

    @Transient
    private YvsComptaJournaux journal;
    @Transient
    private YvsComptaBonProvisoire bonProvisoire = new YvsComptaBonProvisoire();
    @Transient
    private boolean selectActif;
    @Transient
    private boolean update;
    @Transient
    private boolean comptabilised;
    @Transient
    private boolean errorComptabilise;
    @Transient
    private boolean new_;
    @Transient
    private double montantRest;
    @Transient
    private boolean outDelai;
    @Transient
    private String nameMens;
    @Transient
    private double sommeVente;

    public YvsComptaCaissePieceAchat() {
        compensations = new ArrayList<>();
        sousAchats = new ArrayList<>();
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaCaissePieceAchat(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaCaissePieceAchat(YvsComptaCaissePieceAchat y) {
        this();
        this.id = y.getId();
        this.numeroPiece = y.numeroPiece;
        this.statutPiece = y.statutPiece;
        this.montant = y.montant;
        this.note = y.note;
        this.comptabilise = y.comptabilise;
        this.datePiece = y.datePiece;
        this.datePaiement = y.datePaiement;
        this.referenceExterne = y.referenceExterne;
        this.model = y.model;
        this.author = y.author;
        this.achat = y.achat;
        this.caisse = y.caisse;
        this.caissier = y.caissier;
        this.fournisseur = y.fournisseur;
        this.datePaimentPrevu = y.datePaimentPrevu;
        this.selectActif = y.selectActif;
        this.update = y.update;
        this.parent = y.parent;
        this.new_ = y.new_;
        this.montantRest = y.montantRest;
        this.outDelai = y.outDelai;
        this.nameMens = y.nameMens;
        this.pieceContenu = y.pieceContenu;
        this.compensations = y.compensations;
        this.sousAchats = y.sousAchats;
        this.notifs = y.notifs;
        this.justify = y.justify;
        this.idDistant = y.idDistant;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getComptabilise() {
        return comptabilise != null ? comptabilise : false;
    }

    public void setComptabilise(Boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDatePaimentPrevu() {
        return datePaimentPrevu != null ? datePaimentPrevu : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDatePaimentPrevu(Date datePaimentPrevu) {
        this.datePaimentPrevu = datePaimentPrevu;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDatePiece() {
        return datePiece;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDatePiece(Date datePiece) {
        this.datePiece = datePiece;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDatePaiement() {
        return datePaiement;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public double getMontantRest() {
        return montantRest;
    }

    public void setMontantRest(double montantRest) {
        this.montantRest = montantRest;
    }

    public boolean isOutDelai() {
        outDelai = dateToCalendar(getDatePaimentPrevu()).before(dateToCalendar(new Date()));
        return outDelai;
    }

    public void setOutDelai(boolean outDelai) {
        this.outDelai = outDelai;
    }

    public String getNameMens() {
        return nameMens;
    }

    public void setNameMens(String nameMens) {
        this.nameMens = nameMens;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public boolean isErrorComptabilise() {
        return errorComptabilise;
    }

    public void setErrorComptabilise(boolean errorComptabilise) {
        this.errorComptabilise = errorComptabilise;
    }

    public boolean isComptabilised() {
        return comptabilised;
    }

    public void setComptabilised(boolean comptabilised) {
        this.comptabilised = comptabilised;
    }

    public String getNumeroPiece() {
        return numeroPiece != null ? numeroPiece : "";
    }

    public void setNumeroPiece(String numeroPiece) {
        this.numeroPiece = numeroPiece;
    }

    public Character getStatutPiece() {
        return statutPiece != null ? statutPiece : 'W';
    }

    public void setStatutPiece(Character statutPiece) {
        this.statutPiece = statutPiece;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComDocAchats getAchat() {
        return achat;
    }

    public void setAchat(YvsComDocAchats achat) {
        this.achat = achat;
    }

    public YvsBaseCaisse getCaisse() {
        return caisse != null ? caisse : new YvsBaseCaisse();
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
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

    public YvsUsers getCaissier() {
        return caissier;
    }

    public void setCaissier(YvsUsers caissier) {
        this.caissier = caissier;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaContentJournalPieceAchat getPieceContenu() {
        return pieceContenu;
    }

    public void setPieceContenu(YvsComptaContentJournalPieceAchat pieceContenu) {
        this.pieceContenu = pieceContenu;
    }

    @XmlTransient
    @JsonIgnore
    public double getSommeVente() {
        sommeVente = 0;
        for (YvsComptaCaissePieceCompensation y : compensations) {
            if (y.getId() > 0) {
                sommeVente += y.getVente().getMontant();
            }
        }
        return sommeVente;
    }

    public void setSommeVente(double sommeVente) {
        this.sommeVente = sommeVente;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaCaissePieceAchat getParent() {
        return parent;
    }

    public void setParent(YvsComptaCaissePieceAchat parent) {
        this.parent = parent;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaissePieceCompensation> getCompensations() {
        return compensations;
    }

    public void setCompensations(List<YvsComptaCaissePieceCompensation> compensations) {
        this.compensations = compensations;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaNotifReglementAchat getNotifs() {
        return notifs;
    }

    public void setNotifs(YvsComptaNotifReglementAchat notifs) {
        this.notifs = notifs;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaPhasePieceAchat> getPhasesReglement() {
        return phasesReglement;
    }

    @XmlTransient
    @JsonIgnore
    public double getMontantNonCompense() {
        double re = getMontant();
        if (compensations != null ? compensations.isEmpty() : false) {
            for (YvsComptaCaissePieceCompensation p : compensations) {
                re -= p.getAchat().getMontant();
            }
        }
        return re;
    }

    public void setPhasesReglement(List<YvsComptaPhasePieceAchat> phasesReglement) {
        this.phasesReglement = phasesReglement;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaissePieceAchat> getSousAchats() {
        return sousAchats;
    }

    public void setSousAchats(List<YvsComptaCaissePieceAchat> sousAchats) {
        this.sousAchats = sousAchats;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaJustifBonAchat getJustify() {
        return justify;
    }

    public void setJustify(YvsComptaJustifBonAchat justify) {
        this.justify = justify;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaBonProvisoire getBonProvisoire() {
        return bonProvisoire;
    }

    public void setBonProvisoire(YvsComptaBonProvisoire bonProvisoire) {
        this.bonProvisoire = bonProvisoire;
    }

    public YvsComptaJournaux getJournal() {
        return journal;
    }

    public void setJournal(YvsComptaJournaux journal) {
        this.journal = journal;
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
        if (!(object instanceof YvsComptaCaissePieceAchat)) {
            return false;
        }
        YvsComptaCaissePieceAchat other = (YvsComptaCaissePieceAchat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaCaissePieceAchat[ id=" + id + " ]";
    }

    Calendar dateToCalendar(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(date);
            return cal;
        }
        return Calendar.getInstance();
    }

    @XmlTransient
    @JsonIgnore
    public String canValide() {
        String ERROR = "";
        double montant = 0;
        if (getModel() != null ? getModel().getId() > 0 ? !getModel().getTypeReglement().equals("COMPENSATION") : false : false) {
            montant = getMontant();
        }
        if (!getAchat().getStatut().equals(Constantes.ETAT_VALIDE)) {
            ERROR = "La facture n'est pas encore validée !";
        }
        for (YvsComptaCaissePieceCompensation y : compensations) {
            if (y.getVente().getModel() != null ? y.getVente().getModel().getId() < 1 : true) {
                ERROR = "Un reglement n'a pas de mode de paiement";
                break;
            }
            if (getModel() != null ? getModel().getId() > 0 ? !getModel().getTypeReglement().equals("COMPENSATION") : false : false) {
                if (y.getVente().getCaisse() != null ? y.getVente().getCaisse().getId() < 1 : true) {
                    ERROR = "Un reglement n'a pas de caisse";
                    break;
                }
            }
            montant += y.getVente().getMontant();
        }
        if (ERROR != null ? ERROR.trim().length() < 1 : true) {
            if (getMontant() != montant) {
                ERROR = "Cette compensation n'est pas équilibrée";
            }
        }
        return ERROR;
    }

    public String getAction() {
        if (statutPiece != null) {
            if (statutPiece == Constantes.STATUT_ATTENTE) {
                return "Valider";
            } else if (statutPiece == Constantes.STATUT_DOC_ATTENTE || statutPiece == Constantes.STATUT_DOC_VALIDE) {
                return "Encaisser";
            } else {
                return "Annuler";
            }
        } else {
            return "Annuler";
        }
    }

    @XmlTransient
    @JsonIgnore
    public int getPhaseValide() {
        int nb = 0;
        for (YvsComptaPhasePieceAchat vm : getPhasesReglement()) {
            if (vm.getPhaseOk()) {
                nb++;
            }
        }
        return nb++;
    }

    @XmlTransient
    @JsonIgnore
    public String getLibphases() {
        return "Etp. " + getPhaseValide() + " / " + getPhasesReglement().size();
    }

}
