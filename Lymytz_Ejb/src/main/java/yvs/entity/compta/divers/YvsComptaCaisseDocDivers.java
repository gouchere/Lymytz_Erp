/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.divers;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.salaire.service.Constantes;
import static yvs.dao.salaire.service.Constantes.dfs;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsBaseTypeDocDivers;
import yvs.entity.compta.YvsComptaAbonementDocDivers;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.saisie.YvsComptaContentJournalDocDivers;
import yvs.entity.grh.contrat.retenue.YvsGrhRetenueDocDivers;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.workflow.YvsWorkflowValidDocCaisse;
import yvs.entity.proj.projet.YvsProjProjetCaisseDocDivers;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_compta_caisse_doc_divers")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findAll", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.author.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findAllC", query = "SELECT COUNT(y) FROM YvsComptaCaisseDocDivers y WHERE y.author.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findById", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByIds", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByNumPiece", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.numPiece = :numPiece"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByNumero", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.numPiece = :numPiece AND y.author.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByNumPieceLike", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.numPiece LIKE :numPiece AND y.societe=:societe ORDER BY y.numPiece DESC"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByNumPieceLike_", query = "SELECT y.numPiece FROM YvsComptaCaisseDocDivers y WHERE y.numPiece LIKE :numPiece AND y.societe=:societe ORDER BY y.numPiece DESC"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByNumPieceLikeMouv", query = "SELECT y.numPiece FROM YvsComptaCaisseDocDivers y WHERE y.numPiece LIKE :numPiece AND y.mouvement = :mouvement AND y.societe=:societe ORDER BY y.numPiece DESC"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.countByMouvement", query = "SELECT COUNT(y) FROM YvsComptaCaisseDocDivers y WHERE y.mouvement = :mouvement AND y.societe=:societe"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByTypeDocCount", query = "SELECT COUNT(y) FROM YvsComptaCaisseDocDivers y WHERE y.typeDoc = :typeDoc AND y.author.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByTypeDocCount", query = "SELECT COUNT(y) FROM YvsComptaCaisseDocDivers y WHERE y.typeDoc = :typeDoc AND y.author.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByDateTypeDoc", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.typeDoc = :typeDoc AND y.author.agence.societe = :societe AND y.dateDoc BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByActif", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByDateDoc", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.dateDoc = :dateDoc"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByMontant", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.montant = :montant"),

    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByTypeDoc", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.author.agence.societe = :societe AND y.typeDoc = :typeDoc ORDER BY y.dateDoc DESC"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByAgence", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.author.agence = :agence AND y.typeDoc = :typeDoc ORDER BY y.dateDoc DESC"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByAgenceC", query = "SELECT COUNT(y) FROM YvsComptaCaisseDocDivers y WHERE y.author.agence = :agence AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByTypeDocDates", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.author.agence.societe = :societe AND y.typeDoc = :typeDoc AND y.dateDoc BETWEEN :dateDebut AND :dateFin ORDER BY y.dateDoc DESC"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByTypeDocDatesC", query = "SELECT COUNT(y) FROM YvsComptaCaisseDocDivers y WHERE y.author.agence.societe = :societe AND y.typeDoc = :typeDoc AND y.dateDoc BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByAgenceDates", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.author.agence = :agence AND y.typeDoc = :typeDoc AND y.dateDoc BETWEEN :dateDebut AND :dateFin ORDER BY y.dateDoc DESC"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByAgenceDatesC", query = "SELECT COUNT(y) FROM YvsComptaCaisseDocDivers y WHERE y.author.agence = :agence AND y.typeDoc = :typeDoc AND y.dateDoc BETWEEN :dateDebut AND :dateFin"),

    @NamedQuery(name = "YvsComptaCaisseDocDivers.countDocByNumDoc", query = "SELECT COUNT(y) FROM YvsComptaCaisseDocDivers y WHERE y.agence.societe = :societe AND y.numPiece = :numDoc AND y.id!=:id"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByFournisseurNonRegle", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.idTiers = :idTiers AND y.tableTiers =:tableTiers AND y.statutDoc ='V' AND y.statutRegle !=:statutRegler AND y.mouvement='D' ORDER BY y.dateDoc"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByFournisseurNonRegleByAgence", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.idTiers = :idTiers AND y.tableTiers =:tableTiers AND y.statutDoc ='V' AND y.statutRegle !=:statutRegler AND y.mouvement='D' AND y.agence = :agence ORDER BY y.dateDoc"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByClientNonRegle", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.idTiers = :idTiers AND y.tableTiers =:tableTiers AND y.statutDoc ='V' AND y.statutRegle !=:statutRegler AND y.mouvement='R' ORDER BY y.dateDoc"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByClientNonRegleByAgence", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.idTiers = :idTiers AND y.tableTiers =:tableTiers AND y.statutDoc ='V' AND y.statutRegle !=:statutRegler AND y.mouvement='R' AND y.agence = :agence ORDER BY y.dateDoc"),

    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByTypeDocMouv", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.author.agence.societe = :societe AND y.typeDoc = :typeDoc AND y.mouvement = :mouvement ORDER BY y.dateDoc DESC"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByTypeDocMouvC", query = "SELECT COUNT(y) FROM YvsComptaCaisseDocDivers y WHERE y.author.agence.societe = :societe AND y.typeDoc = :typeDoc AND y.mouvement = :mouvement"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByAgenceMouv", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.author.agence = :agence AND y.typeDoc = :typeDoc AND y.mouvement = :mouvement ORDER BY y.dateDoc DESC"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByAgenceMouvC", query = "SELECT COUNT(y) FROM YvsComptaCaisseDocDivers y WHERE y.author.agence = :agence AND y.typeDoc = :typeDoc AND y.mouvement = :mouvement"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByTypeDocDatesMouv", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.author.agence.societe = :societe AND y.typeDoc = :typeDoc AND y.mouvement = :mouvement AND y.dateDoc BETWEEN :dateDebut AND :dateFin ORDER BY y.dateDoc DESC"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByTypeDocDatesMouvC", query = "SELECT COUNT(y) FROM YvsComptaCaisseDocDivers y WHERE y.author.agence.societe = :societe AND y.typeDoc = :typeDoc AND y.mouvement = :mouvement AND y.dateDoc BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByAgenceDatesMouv", query = "SELECT y FROM YvsComptaCaisseDocDivers y WHERE y.author.agence = :agence AND y.typeDoc = :typeDoc AND y.mouvement = :mouvement AND y.dateDoc BETWEEN :dateDebut AND :dateFin ORDER BY y.dateDoc DESC"),
    @NamedQuery(name = "YvsComptaCaisseDocDivers.findByAgenceDatesMouvC", query = "SELECT COUNT(y) FROM YvsComptaCaisseDocDivers y WHERE y.author.agence = :agence AND y.typeDoc = :typeDoc AND y.mouvement = :mouvement AND y.dateDoc BETWEEN :dateDebut AND :dateFin"),})
public class YvsComptaCaisseDocDivers extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_caisse_doc_divers_id_seq", name = "yvs_compta_caisse_doc_divers_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_caisse_doc_divers_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Size(max = 2147483647)
    @Column(name = "num_piece")
    private String numPiece;
    @Column(name = "mouvement")
    private String mouvement;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @Column(name = "is_montant_ttc")
    private Boolean isMontantTTc;
    @Column(name = "date_doc")
    @Temporal(TemporalType.DATE)
    private Date dateDoc;
    @Column(name = "montant")
    private Double montant;
    @Column(name = "date_valider")
    @Temporal(TemporalType.DATE)
    private Date dateValider;
    @Column(name = "date_annuler")
    @Temporal(TemporalType.DATE)
    private Date dateAnnuler;
    @Column(name = "description")
    private String description;
    @Column(name = "date_cloturer")
    @Temporal(TemporalType.DATE)
    private Date dateCloturer;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "statut_doc")
    private String statutDoc;
    @Column(name = "statut_regle")
    private Character statutRegle;
    @Column(name = "document_lie")
    private BigInteger documentLie;
    @Size(max = 2147483647)
    @Column(name = "reference_externe")
    private String referenceExterne;
    @Column(name = "etape_valide")
    private Integer etapeValide;
    @Column(name = "etape_total")
    private Integer etapeTotal;

    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisse;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @JoinColumn(name = "valider_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers validerBy;
    @JoinColumn(name = "annuler_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers annulerBy;
    @JoinColumn(name = "compte_general", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compteGeneral;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaisseDocDivers parent;
    @JoinColumn(name = "type_doc", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTypeDocDivers typeDoc;
    @Column(name = "id_tiers")
    private Long idTiers;
    @Column(name = "table_tiers")
    private String tableTiers;
    @Column(name = "libelle_comptable")
    private String libelleComptable;
    @Column(name = "notes")
    private String notes;

    @OneToOne(mappedBy = "divers", fetch = FetchType.LAZY)
    private YvsComptaContentJournalDocDivers pieceContenu;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<YvsComptaCaisseDocDivers> documents;
    @OneToMany(mappedBy = "docCaisse", fetch = FetchType.LAZY)
    private List<YvsWorkflowValidDocCaisse> etapesValidations;
    @OneToMany(mappedBy = "docDivers", fetch = FetchType.LAZY)
    private List<YvsComptaCaissePieceDivers> reglements;
    @OneToMany(mappedBy = "docDivers", fetch = FetchType.LAZY)
    private List<YvsComptaAbonementDocDivers> abonnements;
    @OneToMany(mappedBy = "docDivers", fetch = FetchType.LAZY)
    private List<YvsComptaCoutSupDocDivers> couts;
    @OneToMany(mappedBy = "docDivers", fetch = FetchType.LAZY)
    private List<YvsComptaTaxeDocDivers> taxes;
    @OneToMany(mappedBy = "docDivers", fetch = FetchType.LAZY)
    private List<YvsComptaCentreDocDivers> sections;
    @OneToMany(mappedBy = "docDivers", fetch = FetchType.LAZY)
    private List<YvsComptaCaisseDocDiversTiers> tiers;
    @OneToMany(mappedBy = "docDivers", fetch = FetchType.LAZY)
    private List<YvsGrhRetenueDocDivers> retenues;

    @Transient
    private List<YvsProjProjetCaisseDocDivers> projets;
    @Transient
    private List<YvsComptaJustificatifBon> bonsProvisoire;

    @Transient
    private YvsComptaJournaux journal;
    @Transient
    private boolean new_;
    @Transient
    private boolean correct;
    @Transient
    private double totalPlanifie;
    @Transient
    private double montantTotal;
    @Transient
    private boolean comptabilised;
    @Transient
    private boolean errorComptabilise;
    @Transient
    private boolean unComptabiliseAll = true;
    @Transient
    private boolean error;
    @Transient
    private double taxe;
    @Transient
    private double cout;
    @Transient
    private String libEtapes;
    @Transient
    private String maDateSave;
    @Transient
    private String beneficiaire;
    @Transient
    private String oldReference;

    @Transient
    private boolean synchroniser;

    public YvsComptaCaisseDocDivers() {
        couts = new ArrayList<>();
        taxes = new ArrayList<>();
        reglements = new ArrayList<>();
        etapesValidations = new ArrayList<>();
        documents = new ArrayList<>();
        sections = new ArrayList<>();
        bonsProvisoire = new ArrayList<>();
        tiers = new ArrayList<>();
        retenues = new ArrayList<>();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public YvsComptaCaisseDocDivers(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaCaisseDocDivers(boolean error) {
        this();
        this.error = error;
    }

    public YvsComptaCaisseDocDivers(Long id, String statut) {
        this(id);
        this.statutDoc = statut;
    }

    public YvsComptaCaisseDocDivers(Long id, String numPiece, String referenceExterne, String statut, Character statutRegle) {
        this(id, statut);
        this.numPiece = numPiece;
        this.referenceExterne = referenceExterne;
        this.statutRegle = statutRegle;
    }

    public Boolean getComptabilise() {
        return comptabilise != null ? comptabilise : false;
    }

    public void setComptabilise(Boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
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

    @XmlTransient
    public boolean isCorrect() {
        correct = !reglements.isEmpty();
        for (YvsComptaCaissePieceDivers r : reglements) {
            if (r.getStatutPiece() != 'P') {
                correct = false;
                break;
            }
        }
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaContentJournalDocDivers getPieceContenu() {
        return pieceContenu;
    }

    public void setPieceContenu(YvsComptaContentJournalDocDivers pieceContenu) {
        this.pieceContenu = pieceContenu;
    }

    @XmlTransient
    @JsonIgnore
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

    @XmlTransient
    @JsonIgnore
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

    public String getMouvement() {
        return mouvement;
    }

    public void setMouvement(String mouvement) {
        this.mouvement = mouvement;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumPiece() {
        return numPiece != null ? numPiece : "";
    }

    public void setNumPiece(String numPiece) {
        this.numPiece = numPiece;
    }

    public String getOldReference() {
        return oldReference;
    }

    public void setOldReference(String oldReference) {
        this.oldReference = oldReference;
    }

    public YvsBaseCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
    }

    public YvsBaseTypeDocDivers getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(YvsBaseTypeDocDivers typeDoc) {
        this.typeDoc = typeDoc;
    }

    public Character getStatutRegle() {
        return statutRegle != null ? String.valueOf(statutRegle).trim().length() > 0 ? statutRegle : 'W' : 'W';
    }

    public void setStatutRegle(Character statutRegle) {
        this.statutRegle = statutRegle;
    }

    public String getStatutDoc() {
        return statutDoc != null ? statutDoc.trim().length() > 0 ? statutDoc : Constantes.ETAT_EDITABLE : Constantes.ETAT_EDITABLE;
    }

    public void setStatutDoc(String statutDoc) {
        this.statutDoc = statutDoc;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateDoc() {
        return dateDoc;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateDoc(Date dateDoc) {
        this.dateDoc = dateDoc;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public List<YvsComptaCaissePieceDivers> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaCaissePieceDivers> echeanciers) {
        this.reglements = echeanciers;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

//    @XmlTransient  @JsonIgnore
//    public YvsBaseTiers getTiers() {
//        return tiers;
//    }
//
//    public void setTiers(YvsBaseTiers tiers) {
//        this.tiers = tiers;
//    }
    public YvsBasePlanComptable getCompteGeneral() {
        return compteGeneral;
    }

    public void setCompteGeneral(YvsBasePlanComptable compteGeneral) {
        this.compteGeneral = compteGeneral;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEtapeValide() {
        return etapeValide != null ? etapeValide > getEtapeTotal() ? getEtapeTotal() : etapeValide : 0;
    }

    public void setEtapeValide(Integer etapeValide) {
        this.etapeValide = etapeValide;
    }

    public Integer getEtapeTotal() {
        return etapeTotal != null ? etapeTotal : 0;
    }

    public void setEtapeTotal(Integer etapeTotal) {
        this.etapeTotal = etapeTotal;
    }

    public List<YvsWorkflowValidDocCaisse> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidDocCaisse> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
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
    public Date getDateCloturer() {
        return dateCloturer;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateCloturer(Date dateCloturer) {
        this.dateCloturer = dateCloturer;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaAbonementDocDivers> getAbonnements() {
        return abonnements;
    }

    public void setAbonnements(List<YvsComptaAbonementDocDivers> abonnements) {
        this.abonnements = abonnements;
    }

    public BigInteger getDocumentLie() {
        return documentLie;
    }

    public void setDocumentLie(BigInteger documentLie) {
        this.documentLie = documentLie;
    }

    public String getReferenceExterne() {
        return referenceExterne != null ? referenceExterne : "";
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
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

    public double getTaxe() {
        taxe = 0;
        for (YvsComptaTaxeDocDivers t : taxes) {
            taxe += t.getMontant();
        }
        return taxe;
    }

    public void setTaxe(double taxe) {
        this.taxe = taxe;
    }

    public double getCout() {
        cout = 0;
        for (YvsComptaCoutSupDocDivers t : couts) {
            cout += t.getMontant();
        }
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCoutSupDocDivers> getCouts() {
        return couts;
    }

    public void setCouts(List<YvsComptaCoutSupDocDivers> couts) {
        this.couts = couts;
    }

    @XmlTransient
    public List<YvsComptaTaxeDocDivers> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<YvsComptaTaxeDocDivers> taxes) {
        this.taxes = taxes;
    }

    public Boolean getIsMontantTTc() {
        return isMontantTTc != null ? isMontantTTc : false;
    }

    public void setIsMontantTTc(Boolean isMontantTTc) {
        this.isMontantTTc = isMontantTTc;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaisseDocDivers> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComptaCaisseDocDivers> documents) {
        this.documents = documents;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaCaisseDocDivers getParent() {
        return parent;
    }

    public void setParent(YvsComptaCaisseDocDivers parent) {
        this.parent = parent;
    }

    public String getLibEtapes() {
        libEtapes = "Etp. " + getEtapeValide() + " / " + getEtapeTotal();
        return libEtapes;
    }

    public void setLibEtapes(String libEtapes) {
        this.libEtapes = libEtapes;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCentreDocDivers> getSections() {
        return sections;
    }

    public void setSections(List<YvsComptaCentreDocDivers> sections) {
        this.sections = sections;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaJustificatifBon> getBonsProvisoire() {
        return bonsProvisoire;
    }

    public void setBonsProvisoire(List<YvsComptaJustificatifBon> bonsProvisoire) {
        this.bonsProvisoire = bonsProvisoire;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaisseDocDiversTiers> getTiers() {
        return tiers;
    }

    public void setTiers(List<YvsComptaCaisseDocDiversTiers> tiers) {
        this.tiers = tiers;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsGrhRetenueDocDivers> getRetenues() {
        return retenues;
    }

    public void setRetenues(List<YvsGrhRetenueDocDivers> retenues) {
        this.retenues = retenues;
    }

    public String getMaDateSave() {
        return getDateSave() != null ? dfs.format(getDateSave()) : "";
    }

    public void setMaDateSave(String maDateSave) {
        this.maDateSave = maDateSave;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public Long getIdTiers() {
        return idTiers != null ? idTiers : 0;
    }

    public void setIdTiers(Long idTiers) {
        this.idTiers = idTiers;
    }

    public String getTableTiers() {
        return tableTiers;
    }

    public void setTableTiers(String tableTiers) {
        this.tableTiers = tableTiers;
    }

    public String getLibelleComptable() {
        return libelleComptable;
    }

    public void setLibelleComptable(String libelleComptable) {
        this.libelleComptable = libelleComptable;
    }

    public boolean isUnComptabiliseAll() {
        return unComptabiliseAll;
    }

    public void setUnComptabiliseAll(boolean unComptabiliseAll) {
        this.unComptabiliseAll = unComptabiliseAll;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsProjProjetCaisseDocDivers> getProjets() {
        return projets;
    }

    public void setProjets(List<YvsProjProjetCaisseDocDivers> projets) {
        this.projets = projets;
    }

    public double getTotalPlanifie() {
        return totalPlanifie;
    }

    public void setTotalPlanifie(double totalPlanifie) {
        this.totalPlanifie = totalPlanifie;
    }

    public boolean isSynchroniser() {
        return synchroniser;
    }

    public void setSynchroniser(boolean synchroniser) {
        this.synchroniser = synchroniser;
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    @XmlTransient
    public double getResteAPlanifier() {
        return (getMontant() + getCout()) - totalPlanifie;
    }

    @XmlTransient
    public double getMontantTotal() {
        montantTotal = (getMontant() + getCout());
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
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
        if (!(object instanceof YvsComptaCaisseDocDivers)) {
            return false;
        }
        YvsComptaCaisseDocDivers other = (YvsComptaCaisseDocDivers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsComptaCaisseDocDivers[ id=" + id + " ]";
    }

    @XmlTransient
    public boolean isSuspendu() {
        return statutDoc.equals(Constantes.ETAT_SUSPENDU) || statutDoc.equals(Constantes.ETAT_ANNULE);
    }

    public boolean canEditable() {
        return statutDoc.equals(Constantes.ETAT_ATTENTE) || statutDoc.equals(Constantes.ETAT_EDITABLE);
    }

    public boolean canDelete() {
        return statutDoc.equals(Constantes.ETAT_ATTENTE) || statutDoc.equals(Constantes.ETAT_EDITABLE) || statutDoc.equals(Constantes.ETAT_SUSPENDU) || statutDoc.equals(Constantes.ETAT_ANNULE);
    }

    public boolean canPaye() {
        boolean result = statutDoc.equals(Constantes.ETAT_VALIDE) || statutDoc.equals(Constantes.ETAT_CLOTURE);
        if (!result ? (getEtapesValidations() != null ? !getEtapesValidations().isEmpty() : false) : false) {
            for (YvsWorkflowValidDocCaisse w : getEtapesValidations()) {
                if (w.getEtapeValid() && w.getEtape().getReglementHere()) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

}
