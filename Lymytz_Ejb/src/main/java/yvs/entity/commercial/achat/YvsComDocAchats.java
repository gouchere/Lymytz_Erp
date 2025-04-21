/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and charger the template in the editor.
 */
package yvs.entity.commercial.achat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
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
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.salaire.service.Constantes;
import static yvs.dao.salaire.service.Constantes.dfs;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import yvs.dao.YvsEntity;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.compta.YvsBaseTypeDocDivers;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.divers.YvsComptaJustifBonAchat;
import yvs.entity.compta.saisie.YvsComptaContentJournalFactureAchat;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.workflow.YvsWorkflowValidFactureAchat;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_doc_achats")
@NamedQueries({
    @NamedQuery(name = "YvsComDocAchats.findAll", query = "SELECT y FROM YvsComDocAchats y WHERE y.agence.societe = :societe"),
    @NamedQuery(name = "YvsComDocAchats.countAll", query = "SELECT COUNT(y) FROM YvsComDocAchats y WHERE y.agence.societe = :societe"),
    @NamedQuery(name = "YvsComDocAchats.findByReference", query = "SELECT y FROM YvsComDocAchats y WHERE y.author.agence.societe = :societe AND y.numDoc LIKE :numRefDoc ORDER by y.numDoc DESC"),
    @NamedQuery(name = "YvsComDocAchats.findByReference_", query = "SELECT y FROM YvsComDocAchats y WHERE y.author.agence.societe = :societe AND y.typeDoc = :typeDoc AND y.numDoc LIKE :numRefDoc ORDER by y.numDoc DESC"),
    @NamedQuery(name = "YvsComDocAchats.findByAgenceReference", query = "SELECT y FROM YvsComDocAchats y WHERE y.author.agence = :agence AND y.typeDoc = :typeDoc AND y.numDoc LIKE :numRefDoc ORDER by y.numDoc DESC"),
    @NamedQuery(name = "YvsComDocAchats.findByDepotReference", query = "SELECT y FROM YvsComDocAchats y WHERE y.depotReception = :depot AND y.typeDoc = :typeDoc AND y.numDoc LIKE :numRefDoc ORDER by y.numDoc DESC"),
    @NamedQuery(name = "YvsComDocAchats.findByParentFournisseurTypeDoc", query = "SELECT y FROM YvsComDocAchats y WHERE y.documentLie = :documentLie AND y.fournisseur = :fournisseur AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocAchats.findByDepotFournisseurTypeDoc", query = "SELECT y FROM YvsComDocAchats y WHERE y.depotReception = :depot AND y.fournisseur = :fournisseur AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocAchats.findByParentFournisseur", query = "SELECT y FROM YvsComDocAchats y WHERE y.documentLie = :documentLie AND y.fournisseur = :fournisseur"),
    @NamedQuery(name = "YvsComDocAchats.findByFournisseurDateTypeDoc", query = "SELECT y FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.fournisseur = :fournisseur AND y.dateDoc = :dateDoc"),
    @NamedQuery(name = "YvsComDocAchats.findByParent", query = "SELECT y FROM YvsComDocAchats y WHERE y.documentLie = :documentLie"),
    @NamedQuery(name = "YvsComDocAchats.findByParentStatut", query = "SELECT y FROM YvsComDocAchats y WHERE y.documentLie = :documentLie AND y.statut = :statut"),
    @NamedQuery(name = "YvsComDocAchats.findByParentTypeDoc", query = "SELECT y FROM YvsComDocAchats y WHERE y.documentLie = :documentLie AND y.typeDoc = :typeDoc AND y.agence.societe = :societe"),
    @NamedQuery(name = "YvsComDocAchats.findByParentTypeDocStatut", query = "SELECT y FROM YvsComDocAchats y WHERE y.documentLie = :documentLie AND y.typeDoc = :typeDoc AND (y.statut = :statut OR y.statutRegle = :statut OR y.statutLivre = :statut) AND y.agence.societe = :societe"),
    @NamedQuery(name = "YvsComDocAchats.findByParentTypeDocStatut_", query = "SELECT y FROM YvsComDocAchats y WHERE y.documentLie = :documentLie AND y.typeDoc = :typeDoc AND y.statut = :statut"),
    @NamedQuery(name = "YvsComDocAchats.findByParentFournisseurTypeDocDate", query = "SELECT y FROM YvsComDocAchats y WHERE y.documentLie = :documentLie AND y.fournisseur = :fournisseur AND y.typeDoc = :typeDoc AND y.dateDoc = :dateDoc"),
    @NamedQuery(name = "YvsComDocAchats.findByParentTypeDocDate", query = "SELECT y FROM YvsComDocAchats y WHERE y.documentLie = :documentLie AND y.typeDoc = :typeDoc AND y.dateDoc = :dateDoc"),
    @NamedQuery(name = "YvsComDocAchats.findById", query = "SELECT y FROM YvsComDocAchats y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComDocAchats.findByFournisseurNonRegle", query = "SELECT y FROM YvsComDocAchats y WHERE y.fournisseur = :fournisseur AND y.statutRegle!=:statutRegle AND y.statut='V' AND y.typeDoc='FA' ORDER BY y.dateDoc ASC"),
    @NamedQuery(name = "YvsComDocAchats.findByFournisseurNonRegleByAgence", query = "SELECT y FROM YvsComDocAchats y WHERE y.fournisseur = :fournisseur AND y.statutRegle!=:statutRegle AND y.statut='V' AND y.typeDoc='FA' AND y.agence = :agence ORDER BY y.dateDoc ASC"),
    @NamedQuery(name = "YvsComDocAchats.findByIds", query = "SELECT y FROM YvsComDocAchats y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsComDocAchats.findByNumPiece", query = "SELECT y FROM YvsComDocAchats y WHERE y.numPiece = :numPiece"),
    @NamedQuery(name = "YvsComDocAchats.findByStatut", query = "SELECT y FROM YvsComDocAchats y WHERE y.statut = :statut"),
    @NamedQuery(name = "YvsComDocAchats.findByFsseurTypeDoc", query = "SELECT y FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.fournisseur = :fournisseur"),
    @NamedQuery(name = "YvsComDocAchats.findByTiersTypeDoc", query = "SELECT y FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.fournisseur.tiers = :tiers"),
    @NamedQuery(name = "YvsComDocAchats.findByTiersTypeStatuts", query = "SELECT y FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.fournisseur.tiers = :tiers AND y.statut IN :statut AND y.statutRegle IN :statutRegle ORDER BY y.dateDoc ASC"),
    @NamedQuery(name = "YvsComDocAchats.findByFsseurTypeDocStatut", query = "SELECT y FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.fournisseur = :fournisseur AND y.statut = :statut"),
    @NamedQuery(name = "YvsComDocAchats.findByFsseurTypeDocCount", query = "SELECT COUNT(y) FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.fournisseur = :fournisseur"),
    @NamedQuery(name = "YvsComDocAchats.findByTypeDocCount", query = "SELECT COUNT(y) FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.agence.societe = :societe"),
    @NamedQuery(name = "YvsComDocAchats.findByDateTypeDocCount", query = "SELECT COUNT(y) FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.agence.societe = :societe AND y.dateDoc BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComDocAchats.findByDateTypeDoc", query = "SELECT y FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.agence.societe = :societe AND y.dateDoc BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComDocAchats.findByNumDoc", query = "SELECT y FROM YvsComDocAchats y WHERE y.numDoc = :numDoc"),
    @NamedQuery(name = "YvsComDocAchats.findByNumDocs", query = "SELECT y FROM YvsComDocAchats y WHERE y.author.agence.societe = :societe AND y.numDoc = :numDoc"),
    @NamedQuery(name = "YvsComDocAchats.findByCategorieComptable", query = "SELECT y FROM YvsComDocAchats y WHERE y.categorieComptable = :categorieComptable"),
    @NamedQuery(name = "YvsComDocAchats.findByDateDoc", query = "SELECT y FROM YvsComDocAchats y WHERE y.dateDoc = :dateDoc"),
    @NamedQuery(name = "YvsComDocAchats.findBySupp", query = "SELECT y FROM YvsComDocAchats y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsComDocAchats.findByActif", query = "SELECT y FROM YvsComDocAchats y WHERE y.actif = :actif"),

    @NamedQuery(name = "YvsComDocAchats.findLieById", query = "SELECT y.documentLie FROM YvsComDocAchats y WHERE y.id = :id"),

    @NamedQuery(name = "YvsComDocAchats.findDepotByParentType", query = "SELECT DISTINCT y.depotReception FROM YvsComDocAchats y WHERE y.documentLie = :documentLie AND y.typeDoc = :type"),

    @NamedQuery(name = "YvsComDocAchats.findByStatutDates", query = "SELECT y FROM YvsComDocAchats y WHERE y.agence.societe = :societe AND y.statut = :statut AND y.dateDoc BETWEEN :dateDebut AND :dateFin AND y.typeDoc = :type"),

    @NamedQuery(name = "YvsComDocAchats.findDateByImpayeDatesAgence", query = "SELECT DISTINCT y.dateDoc FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.agence = :agence AND y.dateDoc BETWEEN :dateDebut AND :dateFin AND y.statutRegle != 'P' ORDER BY y.dateDoc"),
    @NamedQuery(name = "YvsComDocAchats.findDateByImpayeDates", query = "SELECT DISTINCT y.dateDoc FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.agence.societe = :societe AND y.dateDoc BETWEEN :dateDebut AND :dateFin AND y.statutRegle != 'P' ORDER BY y.dateDoc"),
    @NamedQuery(name = "YvsComDocAchats.findFournisseurByImpayeDatesAgence", query = "SELECT DISTINCT y.fournisseur FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.agence = :agence AND y.dateDoc BETWEEN :dateDebut AND :dateFin AND y.statutRegle != 'P' ORDER BY y.fournisseur.nom"),
    @NamedQuery(name = "YvsComDocAchats.findFournisseurByImpayeDates", query = "SELECT DISTINCT y.fournisseur FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.agence.societe = :societe AND y.dateDoc BETWEEN :dateDebut AND :dateFin AND y.statutRegle != 'P' ORDER BY y.fournisseur.nom"),

    @NamedQuery(name = "YvsComDocAchats.findByDepotsStatutAuto", query = "SELECT y FROM YvsComDocAchats y WHERE y.depotReception = :depot AND y.fournisseur = :fournisseur AND y.automatique = :auto AND y.statut = :statut ORDER BY y.dateDoc DESC"),
    @NamedQuery(name = "YvsComDocAchats.findImpayesByFactureDateAgence", query = "SELECT y FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.agence = :agence AND y.dateDoc = :dateDoc AND y.statutRegle != 'P' ORDER BY y.fournisseur.nom"),
    @NamedQuery(name = "YvsComDocAchats.findImpayesByFactureDate2Agence", query = "SELECT y FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.agence = :agence AND y.dateDoc BETWEEN :dateDebut AND :dateFin AND y.statutRegle != 'P' ORDER BY y.fournisseur.nom"),
    @NamedQuery(name = "YvsComDocAchats.findImpayesByFactureDate", query = "SELECT y FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.agence.societe = :societe AND y.dateDoc = :dateDoc AND y.statutRegle != 'P' ORDER BY y.fournisseur.nom"),
    @NamedQuery(name = "YvsComDocAchats.findImpayesByFactureDate2", query = "SELECT y FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.agence.societe = :societe AND y.dateDoc BETWEEN :dateDebut AND :dateFin AND y.statutRegle != 'P' ORDER BY y.fournisseur.nom"),
    @NamedQuery(name = "YvsComDocAchats.findImpayesByFactureDatesAgence", query = "SELECT y FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.fournisseur = :fournisseur AND y.agence = :agence AND y.dateDoc BETWEEN :dateDebut AND :dateFin AND y.statutRegle != 'P' ORDER BY y.dateDoc"),
    @NamedQuery(name = "YvsComDocAchats.findImpayesByFactureDates", query = "SELECT y FROM YvsComDocAchats y WHERE y.typeDoc = :typeDoc AND y.fournisseur = :fournisseur AND y.dateDoc BETWEEN :dateDebut AND :dateFin AND y.statutRegle != 'P' ORDER BY y.dateDoc"),
    @NamedQuery(name = "YvsComDocAchats.findByFsseurDepotInvalid", query = "SELECT y FROM YvsComDocAchats y WHERE y.typeDoc = 'BLA' AND y.depotReception = :depot AND y.fournisseur = :fournisseur AND y.statut != 'V' ORDER BY y.dateLivraison DESC")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsComDocAchats extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_doc_achats_id_seq", name = "yvs_com_doc_achats_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_doc_achats_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Size(max = 2147483647)
    @Column(name = "num_piece")
    private String numPiece;
    @Column(name = "statut")
    private String statut;
    @Column(name = "statut_livre")
    private String statutLivre;
    @Column(name = "statut_regle")
    private String statutRegle;
    @Size(max = 2147483647)
    @Column(name = "type_doc")
    private String typeDoc;
    @Size(max = 2147483647)
    @Column(name = "num_doc")
    private String numDoc;
    @Column(name = "reference_ext")
    private String referenceExterne;
    @Column(name = "fichier")
    private String fichier;
    @Column(name = "date_doc")
    @Temporal(TemporalType.DATE)
    private Date dateDoc;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @Column(name = "impression")
    private Integer impression;
    @Column(name = "date_solder")
    @Temporal(TemporalType.DATE)
    private Date dateSolder;
    @Column(name = "date_livraison")
    @Temporal(TemporalType.DATE)
    private Date dateLivraison;
    @Column(name = "automatique")
    private Boolean automatique;
    @Column(name = "cloturer")
    private Boolean cloturer;
    @Column(name = "date_cloturer")
    @Temporal(TemporalType.DATE)
    private Date dateCloturer;
    @Column(name = "date_valider")
    @Temporal(TemporalType.DATE)
    private Date dateValider;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Column(name = "etape_valide")
    private Integer etapeValide;
    @Column(name = "etape_total")
    private Integer etapeTotal;
    @Column(name = "date_annuler")
    @Temporal(TemporalType.DATE)
    private Date dateAnnuler;
    @Column(name = "generer_facture_auto")
    private Boolean genererFactureAuto;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "notes")
    private String notes;

    @JoinColumn(name = "model_reglement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModelReglement modelReglement;
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
    @JoinColumn(name = "cloturer_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers cloturerBy;
    @JoinColumn(name = "tranche", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTrancheHoraire tranche;
    @JoinColumn(name = "categorie_comptable", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCategorieComptable categorieComptable;
    @JoinColumn(name = "depot_reception", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depotReception;
    @JsonManagedReference
    @JoinColumn(name = "document_lie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocAchats documentLie;
    @JoinColumn(name = "fournisseur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseFournisseur fournisseur;
    @JoinColumn(name = "type_achat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTypeDocDivers typeAchat;

    @OneToOne(mappedBy = "facture", fetch = FetchType.LAZY)
    private YvsComptaContentJournalFactureAchat pieceContenu;

    @JsonBackReference
    @OneToMany(mappedBy = "docAchat", fetch = FetchType.LAZY)
    private List<YvsComContenuDocAchat> contenus;
    @JsonBackReference
    @OneToMany(mappedBy = "docAchat", fetch = FetchType.LAZY)
    private List<YvsComCoutSupDocAchat> couts;
    @JsonBackReference
    @OneToMany(mappedBy = "facture", fetch = FetchType.LAZY)
    private List<YvsComMensualiteFactureAchat> mensualites;
    @JsonBackReference
    @OneToMany(mappedBy = "documentLie", fetch = FetchType.LAZY)
    private List<YvsComDocAchats> documents;
    @JsonBackReference
    @OneToMany(mappedBy = "factureAchat", fetch = FetchType.LAZY)
    private List<YvsWorkflowValidFactureAchat> etapesValidations;
    @JsonBackReference
    @OneToMany(mappedBy = "achat", fetch = FetchType.LAZY)
    private List<YvsComptaCaissePieceAchat> reglements;
    @Transient
    private List<YvsComptaJustifBonAchat> bonsProvisoire;
    @Transient
    private YvsNiveauAcces niveau = new YvsNiveauAcces();
    @Transient
    private YvsComptaJournaux journal;
    @Transient
    private long idDistant;
    @Transient
    private boolean synchroniser;
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
    private boolean int_;
    @Transient
    private boolean error;
    @Transient
    private boolean facture;
    @Transient
    private boolean validIncomplet;
    @Transient
    private boolean livraisonDo;
    @Transient
    private boolean charger;
    @Transient
    private String libEtapes;
    @Transient
    private String maDateDoc;
    @Transient
    private String maDateSave;
    @Transient
    private double montantTTC, montantHT, montantRemise, montantTaxe, montantTaxeR, montantCS, montantTotal, montantAvance, montantNetApayer, montantResteApayer, montantAvoir, montantAvanceAvoir;

    public YvsComDocAchats() {
        etapesValidations = new ArrayList<>();
        contenus = new ArrayList<>();
        mensualites = new ArrayList<>();
        documents = new ArrayList<>();
        reglements = new ArrayList<>();
        couts = new ArrayList<>();
        bonsProvisoire = new ArrayList<>();
    }

    public YvsComDocAchats(Long id) {
        this();
        this.id = id;
    }

    public YvsComDocAchats(Long id, YvsBaseFournisseur fournisseur) {
        this(id);
        this.fournisseur = fournisseur;
    }

    public YvsComDocAchats(boolean error) {
        this();
        this.error = error;
    }

    public YvsComDocAchats(Long id, String numDoc, String statut) {
        this(id);
        this.statut = statut;
        this.numDoc = numDoc;
    }

    public YvsComDocAchats(Long id, String numDoc, String statut, String statutLivre, String statutRegle) {
        this(id, numDoc, statut);
        this.statutLivre = statutLivre;
        this.statutRegle = statutRegle;
    }

    public YvsComDocAchats(YvsComDocAchats y) {
        this(y.getId(), y);
    }

    public YvsComDocAchats(Long id, YvsComDocAchats y) {
        this.id = id;
        this.dateUpdate = y.getDateUpdate();
        this.numPiece = y.getNumPiece();
        this.statut = y.getStatut();
        this.statutLivre = y.getStatutLivre();
        this.statutRegle = y.getStatutRegle();
        this.typeDoc = y.getTypeDoc();
        this.numDoc = y.getNumDoc();
        this.referenceExterne = y.getReferenceExterne();
        this.fichier = y.getFichier();
        this.dateDoc = y.getDateDoc();
        this.supp = y.getSupp();
        this.couts = y.getCouts();
        this.actif = y.getActif();
        this.comptabilise = y.getComptabilise();
        this.impression = y.getImpression();
        this.dateSolder = y.getDateSolder();
        this.dateLivraison = y.getDateLivraison();
        this.cloturer = y.getCloturer();
        this.dateCloturer = y.getDateCloturer();
        this.dateValider = y.getDateValider();
        this.description = y.getDescription();
        this.dateAnnuler = y.getDateAnnuler();
        this.dateSave = y.getDateSave();
        this.modelReglement = y.getModelReglement();
        this.author = y.getAuthor();
        this.agence = y.getAgence();
        this.validerBy = y.getValiderBy();
        this.annulerBy = y.getAnnulerBy();
        this.cloturerBy = y.getCloturerBy();
        this.tranche = y.getTranche();
        this.categorieComptable = y.getCategorieComptable();
        this.depotReception = y.getDepotReception();
        this.documentLie = y.getDocumentLie();
        this.fournisseur = y.getFournisseur();
        this.contenus = y.getContenus();
        this.mensualites = y.getMensualites();
        this.documents = y.getDocuments();
        this.etapesValidations = y.getEtapesValidations();
        this.reglements = y.getReglements();
        this.selectActif = y.isSelectActif();
        this.update = y.isUpdate();
        this.new_ = y.isNew_();
        this.comptabilised = y.isComptabilised();
        this.int_ = y.isInt_();
        this.facture = y.isFacture();
        this.montantTTC = y.getMontantTTC();
        this.validIncomplet = y.isValidIncomplet();
        this.etapeValide = y.getEtapeValide();
        this.etapeTotal = y.getEtapeTotal();
        this.libEtapes = y.getLibEtapes();
        this.automatique = y.getAutomatique();
        this.genererFactureAuto = y.getGenererFactureAuto();
        this.idDistant = y.getIdDistant();
    }

    public boolean isCharger() {
        return charger;
    }

    public void setCharger(boolean charger) {
        this.charger = charger;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getIdDistant() {
        return idDistant;
    }

    public void setIdDistant(long idDistant) {
        this.idDistant = idDistant;
    }

    public Boolean getComptabilise() {
        return comptabilise != null ? comptabilise : false;
    }

    public void setComptabilise(Boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public Boolean getGenererFactureAuto() {
        return genererFactureAuto != null ? genererFactureAuto : false;
    }

    public void setGenererFactureAuto(Boolean genererFactureAuto) {
        this.genererFactureAuto = genererFactureAuto;
    }

    public Boolean getAutomatique() {
        return automatique != null ? automatique : false;
    }

    public void setAutomatique(Boolean automatique) {
        this.automatique = automatique;
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

    public String getLibEtapes() {
        libEtapes = "Etp. " + getEtapeValide() + " / " + getEtapeTotal();
        return libEtapes;
    }

    public void setLibEtapes(String libEtapes) {
        this.libEtapes = libEtapes;
    }

    public boolean isLivraisonDo() {
        return livraisonDo;
    }

    public void setLivraisonDo(boolean livraisonDo) {
        this.livraisonDo = livraisonDo;
    }

    public boolean isValidIncomplet() {
        validIncomplet = false;
        if (getStatut().equals(Constantes.ETAT_VALIDE) ? !getStatutLivre().equals(Constantes.ETAT_LIVRE) : false) {
            for (YvsComContenuDocAchat c : contenus) {
                if (!c.getStatut().equals(Constantes.ETAT_VALIDE)) {
                    validIncomplet = true;
                    break;
                }
            }
        }
        return validIncomplet;
    }

    public void setValidIncomplet(boolean validIncomplet) {
        this.validIncomplet = validIncomplet;
    }

    public String getStatut() {
        statut = (statut != null) ? ((statut.trim().length() > 0) ? statut : Constantes.ETAT_EDITABLE) : Constantes.ETAT_EDITABLE;
        if (statut.equals(Constantes.ETAT_VALIDE)) {
            livraisonDo = true;
        }
        return statut;
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

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Integer getEtapeValide() {
        return etapeValide != null ? etapeValide : 0;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFacture() {
        facture = (typeDoc != null ? (typeDoc.equals("FA") ? true : (typeDoc.equals("FAA") ? true : (typeDoc.equals("FRA")))) : false);
        return facture;
    }

    public void setFacture(boolean facture) {
        this.facture = facture;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
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

    public boolean isInt_() {
        return int_;
    }

    public void setInt_(boolean int_) {
        this.int_ = int_;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public Long getId() {
        return id != null ? id : -1L;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumPiece() {
        return numPiece;
    }

    public void setNumPiece(String numPiece) {
        this.numPiece = numPiece;
    }

    public String getStatutLivre() {
        return (statutLivre != null) ? ((statutLivre.trim().length() > 0) ? statutLivre : Constantes.ETAT_ATTENTE) : Constantes.ETAT_ATTENTE;
    }

    public void setStatutLivre(String statutLivre) {
        this.statutLivre = statutLivre;
    }

    public String getStatutRegle() {
        return statutRegle != null ? statutRegle.trim().length() > 0 ? statutRegle : Constantes.ETAT_ATTENTE : Constantes.ETAT_ATTENTE;
    }

    public void setStatutRegle(String statutRegle) {
        this.statutRegle = statutRegle;
    }

    public String getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(String typeDoc) {
        this.typeDoc = typeDoc;
    }

    public String getNumDoc() {
        return numDoc != null ? numDoc : "";
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateDoc() {
        return dateDoc != null ? dateDoc : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateDoc(Date dateDoc) {
        this.dateDoc = dateDoc;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaContentJournalFactureAchat getPieceContenu() {
        return pieceContenu;
    }

    public void setPieceContenu(YvsComptaContentJournalFactureAchat pieceContenu) {
        this.pieceContenu = pieceContenu;
    }

    public List<YvsComptaCaissePieceAchat> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaCaissePieceAchat> reglements) {
        this.reglements = reglements;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public YvsBaseModelReglement getModelReglement() {
        return modelReglement;
    }

    public void setModelReglement(YvsBaseModelReglement modelReglement) {
        this.modelReglement = modelReglement;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsers getCloturerBy() {
        return cloturerBy;
    }

    public void setCloturerBy(YvsUsers cloturerBy) {
        this.cloturerBy = cloturerBy;
    }

    public List<YvsComContenuDocAchat> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComContenuDocAchat> contenus) {
        this.contenus = contenus;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComDocAchats> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComDocAchats> documents) {
        this.documents = documents;
    }

    public YvsComDocAchats getDocumentLie() {
        return documentLie;
    }

    public void setDocumentLie(YvsComDocAchats documentLie) {
        this.documentLie = documentLie;
    }

    public YvsBaseFournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(YvsBaseFournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public List<YvsWorkflowValidFactureAchat> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidFactureAchat> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    public YvsBaseDepots getDepotReception() {
        return depotReception;
    }

    public void setDepotReception(YvsBaseDepots depotReception) {
        this.depotReception = depotReception;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComMensualiteFactureAchat> getMensualites() {
        return mensualites;
    }

    public void setMensualites(List<YvsComMensualiteFactureAchat> mensualites) {
        this.mensualites = mensualites;
    }

    public YvsBaseCategorieComptable getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(YvsBaseCategorieComptable categorieComptable) {
        this.categorieComptable = categorieComptable;
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

    public Integer getImpression() {
        return impression;
    }

    public void setImpression(Integer impression) {
        this.impression = impression;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSolder() {
        return dateSolder;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSolder(Date dateSolder) {
        this.dateSolder = dateSolder;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateLivraison() {
        return dateLivraison != null ? dateLivraison : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public Boolean getCloturer() {
        return cloturer != null ? cloturer : false;
    }

    public void setCloturer(Boolean cloturer) {
        this.cloturer = cloturer;
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

    public YvsUsers getValiderBy() {
        return validerBy;
    }

    public void setValiderBy(YvsUsers validerBy) {
        this.validerBy = validerBy;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsers getAnnulerBy() {
        return annulerBy;
    }

    public void setAnnulerBy(YvsUsers annulerBy) {
        this.annulerBy = annulerBy;
    }

    public YvsGrhTrancheHoraire getTranche() {
        return tranche != null ? tranche : new YvsGrhTrancheHoraire();
    }

    public void setTranche(YvsGrhTrancheHoraire tranche) {
        this.tranche = tranche;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComCoutSupDocAchat> getCouts() {
        return couts;
    }

    public void setCouts(List<YvsComCoutSupDocAchat> couts) {
        this.couts = couts;
    }

    public double getMontantAvance() {
        return montantAvance;
    }

    public void setMontantAvance(double montantAvance) {
        this.montantAvance = montantAvance;
    }

    public double getMontantHT() {
        montantHT = getMontantTTC() - getMontantTaxe();
        return montantHT;
    }

    public void setMontantHT(double montantHT) {
        this.montantHT = montantHT;
    }

    public double getMontantTaxe() {
        return montantTaxe;
    }

    public void setMontantTaxe(double montantTaxe) {
        this.montantTaxe = montantTaxe;
    }

    public double getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(double montantTTC) {
        this.montantTTC = montantTTC;
    }

    public double getMontantRemise() {
        return montantRemise;
    }

    public void setMontantRemise(double montantRemise) {
        this.montantRemise = montantRemise;
    }

    public double getMontantTotal() {
        montantTotal = getMontantTTC() - getMontantCS() - getMontantRemise() - getMontantAvoir() + getMontantAvanceAvoir();
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public double getMontantAvoir() {
        return montantAvoir;
    }

    public void setMontantAvoir(double montantAvoir) {
        this.montantAvoir = montantAvoir;
    }

    public double getMontantAvanceAvoir() {
        return montantAvanceAvoir;
    }

    public void setMontantAvanceAvoir(double montantAvanceAvoir) {
        this.montantAvanceAvoir = montantAvanceAvoir;
    }

    public double getMontantCS() {
        return montantCS;
    }

    public void setMontantCS(double montantCS) {
        this.montantCS = montantCS;
    }

    public double getMontantResteApayer() {
        montantResteApayer = getMontantTotal() - getMontantAvance();
        return montantResteApayer;
    }

    public void setMontantResteApayer(double montantResteApayer) {
        this.montantResteApayer = montantResteApayer;
    }

    public double getMontantNetApayer() {
        montantNetApayer = getMontantTotal() - getMontantAvance();
        return montantNetApayer;
    }

    public void setMontantNetApayer(double montantNetApayer) {
        this.montantNetApayer = montantNetApayer;
    }

    public double getMontantTaxeR() {
        return montantTaxeR;
    }

    public void setMontantTaxeR(double montantTaxeR) {
        this.montantTaxeR = montantTaxeR;
    }

    public String getMaDateDoc() {
        return getDateDoc() != null ? dfs.format(getDateDoc()) : "";
    }

    public void setMaDateDoc(String maDateDoc) {
        this.maDateDoc = maDateDoc;
    }

    public String getMaDateSave() {
        return getDateSave() != null ? dfs.format(getDateSave()) : "";
    }

    public void setMaDateSave(String maDateSave) {
        this.maDateSave = maDateSave;
    }

    public double getMontantResteAPlanifier() {
        double re = getMontantTotal();
        for (YvsComptaCaissePieceAchat p : reglements) {
            re -= p.getMontant();
        }
        return re;
    }

    public YvsBaseTypeDocDivers getTypeAchat() {
        return typeAchat;
    }

    public void setTypeAchat(YvsBaseTypeDocDivers typeAchat) {
        this.typeAchat = typeAchat;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaJustifBonAchat> getBonsProvisoire() {
        return bonsProvisoire;
    }

    public void setBonsProvisoire(List<YvsComptaJustifBonAchat> bonsProvisoire) {
        this.bonsProvisoire = bonsProvisoire;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public YvsNiveauAcces getNiveau() {
        return niveau;
    }

    public void setNiveau(YvsNiveauAcces niveau) {
        this.niveau = niveau;
    }

    public boolean isSynchroniser() {
        return synchroniser;
    }

    public void setSynchroniser(boolean synchroniser) {
        this.synchroniser = synchroniser;
    }

    public YvsComptaJournaux getJournal() {
        return journal;
    }

    public void setJournal(YvsComptaJournaux journal) {
        this.journal = journal;
    }

    public boolean canEditable() {
        return statut.equals(Constantes.ETAT_ATTENTE) || statut.equals(Constantes.ETAT_EDITABLE);
    }

    public boolean canDelete() {
        return statut.equals(Constantes.ETAT_ATTENTE) || statut.equals(Constantes.ETAT_EDITABLE) || statut.equals(Constantes.ETAT_SUSPENDU) || statut.equals(Constantes.ETAT_ANNULE);
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
        if (!(object instanceof YvsComDocAchats)) {
            return false;
        }
        YvsComDocAchats other = (YvsComDocAchats) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.achat.YvsComDocAchats[ id=" + id + " ]";
    }

}
