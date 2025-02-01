/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.divers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import static yvs.dao.salaire.service.Constantes.dfs;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaNotifReglementDocDivers;
import yvs.entity.compta.YvsComptaPhasePieceDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceDivers;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_compta_caisse_piece_divers")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findById", query = "SELECT y FROM YvsComptaCaissePieceDivers y "
            + " JOIN FETCH y.modePaiement JOIN FETCH y.docDivers JOIN FETCH y.caisse LEFT JOIN FETCH y.caissier WHERE y.id = :id ORDER BY y.datePiece DESC"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findByDatePiece", query = "SELECT y FROM YvsComptaCaissePieceDivers y WHERE y.datePiece = :datePiece ORDER BY y.datePiece DESC"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findByMontant", query = "SELECT y FROM YvsComptaCaissePieceDivers y WHERE y.montant = :montant ORDER BY y.datePiece DESC"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findSumMontantByCaisse", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceDivers y WHERE y.caisse IS NOT NULL AND y.caisse = :caisse AND y.mouvement = :mouvement"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findSumMontantByDoc", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceDivers y WHERE y.docDivers = :docDivers AND y.statutPiece = 'P'"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findByAgenceReference", query = "SELECT y FROM YvsComptaCaissePieceDivers y WHERE y.author.agence = :agence AND y.numPiece LIKE :numDoc ORDER by y.numPiece DESC"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findByAgence", query = "SELECT y FROM YvsComptaCaissePieceDivers y WHERE y.author.agence = :agence ORDER BY y.datePiece DESC"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findByAgenceC", query = "SELECT COUNT(y) FROM YvsComptaCaissePieceDivers y WHERE y.author.agence = :agence"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findByNumeroPiece", query = "SELECT y FROM YvsComptaCaissePieceDivers y WHERE y.numPiece = :numeroPiece AND y.docDivers = :docDivers ORDER BY y.datePiece DESC, y.numPiece DESC"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findByAgenceDates", query = "SELECT y FROM YvsComptaCaissePieceDivers y WHERE y.author.agence = :agence AND y.datePiece BETWEEN :dateDebut AND :dateFin ORDER BY y.datePiece DESC"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findByAgenceDatesC", query = "SELECT COUNT(y) FROM YvsComptaCaissePieceDivers y WHERE y.author.agence = :agence AND y.datePiece BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findByDocDivers", query = "SELECT y FROM YvsComptaCaissePieceDivers y WHERE y.docDivers = :docDivers ORDER BY y.datePiece"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findByStatutSameDates", query = "SELECT y FROM YvsComptaCaissePieceDivers y WHERE y.docDivers = :docDivers AND y.statutPiece = :statut AND y.datePiece = y.docDivers.dateDoc ORDER BY y.datePiece"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findByStatutDiffDates", query = "SELECT y FROM YvsComptaCaissePieceDivers y WHERE y.docDivers = :docDivers AND y.statutPiece = :statut AND y.datePiece != y.docDivers.dateDoc ORDER BY y.datePiece"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findByDocDiversStatut", query = "SELECT y FROM YvsComptaCaissePieceDivers y WHERE y.docDivers = :docDivers AND y.statutPiece = :statut ORDER BY y.datePiece"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findIdByDocDiversStatut", query = "SELECT y.id FROM YvsComptaCaissePieceDivers y WHERE y.docDivers = :docDivers AND y.statutPiece = :statut ORDER BY y.datePiece"),

    @NamedQuery(name = "YvsComptaCaissePieceDivers.findDocDiversByNumPiece", query = "SELECT DISTINCT y.docDivers FROM YvsComptaCaissePieceDivers y WHERE y.docDivers.agence.societe = :societe AND y.numPiece LIKE :numero ORDER BY y.numPiece DESC"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findByNumPiece", query = "SELECT y FROM YvsComptaCaissePieceDivers y WHERE y.docDivers.agence.societe = :societe AND y.numPiece LIKE :numero ORDER BY y.numPiece DESC"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.countByParent", query = "SELECT COUNT(y) FROM YvsComptaCaissePieceDivers y WHERE y.parent=:piece"),
    @NamedQuery(name = "YvsComptaCaissePieceDivers.findComptabiliseById", query = "SELECT y.comptabilise FROM YvsComptaCaissePieceDivers y WHERE y.id = :id ORDER BY y.datePiece DESC")})

public class YvsComptaCaissePieceDivers extends YvsEntity implements Serializable, Comparator<YvsComptaCaissePieceDivers> {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_caisse_piece_divers_id_seq", name = "yvs_compta_caisse_piece_divers_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_caisse_piece_divers_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "num_piece")
    private String numPiece;
    @Column(name = "note")
    private String note;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @Column(name = "statut_piece")
    private Character statutPiece;
    @Column(name = "date_piece")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePiece;
    @Column(name = "date_paiment_prevu")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePaimentPrevu;
    @Size(max = 2147483647)
    @Column(name = "mouvement")
    private String mouvement;
    @Column(name = "date_valider")
    @Temporal(TemporalType.DATE)
    private Date dateValider;
    @Column(name = "date_annuler")
    @Temporal(TemporalType.DATE)
    private Date dateAnnuler;
    @Column(name = "montant")
    private Double montant;
    @Column(name = "societe")
    private BigInteger societe;
    @Size(max = 2147483647)
    @Column(name = "reference_externe")
    private String referenceExterne;
    @Size(max = 2147483647)
    @Column(name = "beneficiaire")
    private String beneficiaire;

    @OneToOne(mappedBy = "piece")
    private YvsComptaCaissePieceCoutDivers pieceCout;
    @JoinColumn(name = "mode_paiement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModeReglement modePaiement;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "valider_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers validerBy;
    @JoinColumn(name = "annuler_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers annulerBy;
    @JoinColumn(name = "doc_divers", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaisseDocDivers docDivers;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisse;
    @JoinColumn(name = "caissier", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers caissier;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaissePieceDivers parent;

    @OneToOne(mappedBy = "pieceDocDivers", fetch = FetchType.LAZY)
    private YvsComptaNotifReglementDocDivers notifs;
    @OneToOne(mappedBy = "piece")
    private YvsComptaJustificatifBon justify;

    @OneToOne(mappedBy = "reglement")
    private YvsComptaContentJournalPieceDivers pieceContenu;

    @OneToMany(mappedBy = "pieceDivers")
    private List<YvsComptaPhasePieceDivers> phasesReglement;
    @OneToMany(mappedBy = "parent")
    private List<YvsComptaCaissePieceDivers> sousDivers;
    @OneToMany(mappedBy = "piece")
    private List<YvsComptaJustificatifBon> bonsProvisoire;

    @Transient
    private YvsComptaJournaux journal;
    @Transient
    private boolean select;
    @Transient
    private boolean changeMode;
    @Transient
    private boolean comptabilised;
    @Transient
    private boolean errorComptabilise;
    @Transient
    private boolean unComptabiliseAll = true;
    @Transient
    private boolean justificatif;
    @Transient
    private String numeroExterne;
    @Transient
    private String maDatePiece;
    @Transient
    private YvsComptaCoutSupDocDivers cout;
    @Transient
    private YvsComptaBonProvisoire bonProvisoire = new YvsComptaBonProvisoire();
    @Transient
    private List<YvsComptaContentJournal> contenus;

    public YvsComptaCaissePieceDivers() {
        sousDivers = new ArrayList<>();
        bonsProvisoire = new ArrayList<>();
        contenus = new ArrayList<>();
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaCaissePieceDivers(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaCaissePieceDivers(Long id, YvsComptaCaissePieceDivers y) {
        this(id);
        this.dateUpdate = y.dateUpdate;
        this.dateSave = y.dateSave;
        this.numPiece = y.numPiece;
        this.note = y.note;
        this.statutPiece = y.statutPiece;
        this.datePiece = y.datePiece;
        this.comptabilise = y.comptabilise;
        this.datePaimentPrevu = y.datePaimentPrevu;
        this.mouvement = y.mouvement;
        this.dateValider = y.dateValider;
        this.dateAnnuler = y.dateAnnuler;
        this.montant = y.montant;
        this.societe = y.societe;
        this.referenceExterne = y.referenceExterne;
        this.modePaiement = y.modePaiement;
        this.author = y.author;
        this.comptabilised = y.comptabilised;
        this.validerBy = y.validerBy;
        this.annulerBy = y.annulerBy;
        this.docDivers = y.docDivers;
        this.caisse = y.caisse;
        this.caissier = y.caissier;
        this.beneficiaire = y.beneficiaire;
        this.select = y.select;
        this.numeroExterne = y.numeroExterne;
        this.bonProvisoire = y.bonProvisoire;
        this.justificatif = y.justificatif;
        this.justify = y.justify;
        this.pieceCout = y.pieceCout;
        this.phasesReglement = y.phasesReglement;
        this.sousDivers = y.sousDivers;
        this.bonsProvisoire = y.bonsProvisoire;
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

    @XmlTransient
    public YvsComptaCaissePieceDivers getParent() {
        return parent;
    }

    public void setParent(YvsComptaCaissePieceDivers parent) {
        this.parent = parent;
    }

    @XmlTransient
    public List<YvsComptaCaissePieceDivers> getSousDivers() {
        return sousDivers;
    }

    public void setSousDivers(List<YvsComptaCaissePieceDivers> sousDivers) {
        this.sousDivers = sousDivers;
    }

    public boolean isJustificatif() {
        justificatif = getJustify() != null ? getJustify().getId() > 0 : false;
        return justificatif;
    }

    public void setJustificatif(boolean justificatif) {
        this.justificatif = justificatif;
    }

    @XmlTransient
    public YvsComptaBonProvisoire getBonProvisoire() {
        return bonProvisoire;
    }

    public void setBonProvisoire(YvsComptaBonProvisoire bonProvisoire) {
        this.bonProvisoire = bonProvisoire;
    }

    public String getNumeroExterne() {
        return numeroExterne;
    }

    public void setNumeroExterne(String numeroExterne) {
        this.numeroExterne = numeroExterne;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getNote() {
        return note != null ? note : "";
    }

    public void setNote(String note) {
        this.note = note;
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

    @XmlTransient
    public YvsUsers getValiderBy() {
        return validerBy;
    }

    public void setValiderBy(YvsUsers validerBy) {
        this.validerBy = validerBy;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateValider() {
        return dateValider;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateValider(Date dateValider) {
        this.dateValider = dateValider;
    }

    public boolean isUnComptabiliseAll() {
        return unComptabiliseAll;
    }

    public void setUnComptabiliseAll(boolean unComptabiliseAll) {
        this.unComptabiliseAll = unComptabiliseAll;
    }

    @XmlTransient
    public YvsComptaContentJournalPieceDivers getPieceContenu() {
        return pieceContenu;
    }

    public void setPieceContenu(YvsComptaContentJournalPieceDivers pieceContenu) {
        this.pieceContenu = pieceContenu;
    }

    @XmlTransient
    public YvsUsers getAnnulerBy() {
        return annulerBy;
    }

    public void setAnnulerBy(YvsUsers annulerBy) {
        this.annulerBy = annulerBy;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateAnnuler() {
        return dateAnnuler;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateAnnuler(Date dateAnnuler) {
        this.dateAnnuler = dateAnnuler;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Character getStatutPiece() {
        return statutPiece == null ? ' ' : statutPiece;
    }

    public void setStatutPiece(Character statutPiece) {
        this.statutPiece = statutPiece;
    }

    public String getNumPiece() {
        return numPiece;
    }

    public void setNumPiece(String numPiece) {
        this.numPiece = numPiece;
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

    public YvsComptaCaisseDocDivers getDocDivers() {
        return docDivers;
    }

    public void setDocDivers(YvsComptaCaisseDocDivers docDivers) {
        this.docDivers = docDivers;
    }
    public YvsBaseCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
    }

    public YvsUsers getCaissier() {
        return caissier;
    }

    public void setCaissier(YvsUsers caissier) {
        this.caissier = caissier;
    }

    public String getMouvement() {
        return mouvement;
    }

    public void setMouvement(String mouvement) {
        this.mouvement = mouvement;
    }

    public YvsBaseModeReglement getModePaiement() {
        return modePaiement != null ? modePaiement : new YvsBaseModeReglement(-1l);
    }

    public void setModePaiement(YvsBaseModeReglement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public BigInteger getSociete() {
        return societe;
    }

    public void setSociete(BigInteger societe) {
        this.societe = societe;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public String getBeneficiaire() {
        return beneficiaire != null ? beneficiaire : "";
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    @XmlTransient
    public YvsComptaCoutSupDocDivers getCout() {
        return cout;
    }

    public void setCout(YvsComptaCoutSupDocDivers cout) {
        this.cout = cout;
    }

    @XmlTransient
    public YvsComptaCaissePieceCoutDivers getPieceCout() {
        return pieceCout;
    }

    public void setPieceCout(YvsComptaCaissePieceCoutDivers pieceCout) {
        this.pieceCout = pieceCout;
    }

    @XmlTransient
    public List<YvsComptaPhasePieceDivers> getPhasesReglement() {
        return phasesReglement;
    }

    @XmlTransient
    public void setPhasesReglement(List<YvsComptaPhasePieceDivers> phasesReglement) {
        this.phasesReglement = phasesReglement;
    }

    @XmlTransient
    public YvsComptaJustificatifBon getJustify() {
        return justify;
    }

    public void setJustify(YvsComptaJustificatifBon justify) {
        this.justify = justify;
    }

    public String getMaDatePiece() {
        return getDatePiece() != null ? dfs.format(getDatePiece()) : "";
    }

    public void setMaDatePiece(String maDatePiece) {
        this.maDatePiece = maDatePiece;
    }

    public boolean isChangeMode() {
        return changeMode;
    }

    public void setChangeMode(boolean changeMode) {
        this.changeMode = changeMode;
    }

    @XmlTransient
    public List<YvsComptaJustificatifBon> getBonsProvisoire() {
        return bonsProvisoire;
    }

    public void setBonsProvisoire(List<YvsComptaJustificatifBon> bonsProvisoire) {
        this.bonsProvisoire = bonsProvisoire;
    }

    @XmlTransient
    public List<YvsComptaContentJournal> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComptaContentJournal> contenus) {
        this.contenus = contenus;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaNotifReglementDocDivers getNotifs() {
        return notifs;
    }

    public void setNotifs(YvsComptaNotifReglementDocDivers notifs) {
        this.notifs = notifs;
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
        if (!(object instanceof YvsComptaCaissePieceDivers)) {
            return false;
        }
        YvsComptaCaissePieceDivers other = (YvsComptaCaissePieceDivers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsComptaCaissePieceDivers[ id=" + id + " ]";
    }

    @XmlTransient
    public int getPhaseValide() {
        int nb = 0;
        for (YvsComptaPhasePieceDivers vm : getPhasesReglement()) {
            if (vm.getPhaseOk()) {
                nb++;
            }
        }
        return nb++;
    }

    @XmlTransient
    public String getLibphases() {
        return "Etp. " + getPhaseValide() + " / " + getPhasesReglement().size();
    }

    @Override
    public int compare(YvsComptaCaissePieceDivers o1, YvsComptaCaissePieceDivers o2) {
        if (o1 != null && o2 != null) {
            return (int) (o1.getMontant().compareTo(o2.getMontant()));
        } else if (o1 != null) {
            return 1;
        } else {
            return -1;
        }
    }

}
