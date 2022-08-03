/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.stock;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Util;
import yvs.dao.YvsEntity;
import yvs.dao.salaire.service.Constantes;
import static yvs.dao.salaire.service.Constantes.dfs;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.workflow.YvsWorkflowValidDocStock;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_doc_stocks")
@NamedQueries({
    @NamedQuery(name = "YvsComDocStocks.findAll", query = "SELECT y FROM YvsComDocStocks y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsComDocStocks.findByIds", query = "SELECT y FROM YvsComDocStocks y WHERE y.id in :ids"),
    @NamedQuery(name = "YvsComDocStocks.findById", query = "SELECT y FROM YvsComDocStocks y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComDocStocks.findByDateDoc", query = "SELECT y FROM YvsComDocStocks y WHERE y.dateDoc = :dateDoc AND  y.societe = :societe"),
    @NamedQuery(name = "YvsComDocStocks.findByNumPiece", query = "SELECT y FROM YvsComDocStocks y WHERE y.numPiece = :numPiece AND  y.societe = :societe"),
    @NamedQuery(name = "YvsComDocStocks.findByNumDoc", query = "SELECT y FROM YvsComDocStocks y WHERE y.numDoc = :numRef AND y.societe = :societe"),
    @NamedQuery(name = "YvsComDocStocks.findByReference", query = "SELECT y FROM YvsComDocStocks y WHERE y.societe = :societe AND y.numDoc LIKE :numDoc ORDER by y.numDoc DESC"),
    @NamedQuery(name = "YvsComDocStocks.findReferenceByReference", query = "SELECT y.numDoc FROM YvsComDocStocks y WHERE y.societe = :societe AND y.numDoc>:numDoc AND y.numDoc<:numDoc1 ORDER by y.numDoc DESC"),
    @NamedQuery(name = "YvsComDocStocks.findByReference_", query = "SELECT y FROM YvsComDocStocks y WHERE y.societe = :societe AND y.typeDoc = :typeDoc AND y.numDoc LIKE :numDoc ORDER by y.numDoc DESC"),
    @NamedQuery(name = "YvsComDocStocks.findByReferences_", query = "SELECT y FROM YvsComDocStocks y WHERE y.societe = :societe AND (y.typeDoc = :type1 OR y.typeDoc = :type2) AND y.numDoc LIKE :numDoc ORDER by y.numDoc DESC"),
    @NamedQuery(name = "YvsComDocStocks.findByStatut", query = "SELECT y FROM YvsComDocStocks y WHERE y.statut = :statut AND y.societe = :societe"),
    @NamedQuery(name = "YvsComDocStocks.findByTypeDocCount", query = "SELECT COUNT(y) FROM YvsComDocStocks y WHERE y.typeDoc = :typeDoc AND y.societe = :societe"),

    @NamedQuery(name = "YvsComDocStocks.findStatutById", query = "SELECT y.statut FROM YvsComDocStocks y WHERE y.id = :id"),

    @NamedQuery(name = "YvsComDocStocks.findOneDocStock", query = "SELECT y FROM YvsComDocStocks y WHERE y.statut=:statut AND y.dateDoc=:date AND y.creneauSource=:crSource AND y.creneauDestinataire=:crDest"),
    @NamedQuery(name = "YvsComDocStocks.findOneDocStockToEdit", query = "SELECT y FROM YvsComDocStocks y WHERE (y.statut=:statut OR y.statut=:statut1 OR y.statut=:statut2) AND y.dateDoc=:date AND y.creneauSource=:crSource AND y.creneauDestinataire=:crDest"),

    @NamedQuery(name = "YvsComDocStocks.findByCreneauSource", query = "SELECT y FROM YvsComDocStocks y WHERE y.creneauSource = :creneauSource AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocStocks.findBySourceStatut", query = "SELECT y FROM YvsComDocStocks y WHERE y.source = :depot AND y.typeDoc = :typeDoc AND y.statut = :statut"),
    @NamedQuery(name = "YvsComDocStocks.findBySourceDate", query = "SELECT y FROM YvsComDocStocks y WHERE y.source = :depot AND y.typeDoc = :typeDoc AND y.dateDoc = :dateDoc ORDER BY y.creneauSource.tranche.heureDebut DESC, y.creneauSource.tranche.heureFin DESC, y.id DESC"),
    @NamedQuery(name = "YvsComDocStocks.findByCreneauDestination", query = "SELECT y FROM YvsComDocStocks y WHERE y.creneauDestinataire = :creneauDestinataire AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocStocks.findByCreneauSourceCreneauDestination", query = "SELECT y FROM YvsComDocStocks y WHERE y.creneauSource = :creneauSource AND y.creneauDestinataire = :creneauDestinataire AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocStocks.findBySourceDestination", query = "SELECT y FROM YvsComDocStocks y WHERE y.source = :source AND y.destination = :destination AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocStocks.findBySourceCreneauSource", query = "SELECT y FROM YvsComDocStocks y WHERE y.source = :source AND y.creneauSource = :creneauSource AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocStocks.findBySourceCreneauDestination", query = "SELECT y FROM YvsComDocStocks y WHERE y.source = :source AND y.creneauDestinataire = :creneauDestinataire AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocStocks.findByDestinationCreneauSource", query = "SELECT y FROM YvsComDocStocks y WHERE y.destination = :destination AND y.creneauSource = :creneauSource AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocStocks.findBySourceDestinationCreneauSource", query = "SELECT y FROM YvsComDocStocks y WHERE y.source = :source AND y.destination = :destination AND y.creneauSource = :creneauSource AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocStocks.findBySourceDestinationCreneauDestination", query = "SELECT y FROM YvsComDocStocks y WHERE y.source = :source AND y.destination = :destination AND y.creneauDestinataire = :creneauDestinataire AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocStocks.findByDestinationCreneauDestination", query = "SELECT y FROM YvsComDocStocks y WHERE y.destination = :destination AND y.creneauDestinataire = :creneauDestinataire AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocStocks.findBySourceCreneauSourceStatut", query = "SELECT y FROM YvsComDocStocks y WHERE y.statut = :statut AND y.source = :source AND y.creneauSource = :creneauSource AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocStocks.findByDocNonValide", query = "SELECT COUNT(y) FROM YvsComDocStocks y WHERE y.documentLie IS NULL AND y.statut IN :statuts AND (y.source=:depot OR y.destination=:depot) AND (y.dateDoc<:date OR (y.dateDoc=:date AND y.creneauSource.tranche.id IN :tranches))"),
    @NamedQuery(name = "YvsComDocStocks.findByDocNonValideNoId", query = "SELECT COUNT(y) FROM YvsComDocStocks y WHERE y.documentLie IS NULL AND y.id != :id AND y.statut IN :statuts AND (y.source=:depot OR y.destination=:depot) AND (y.dateDoc<:date OR (y.dateDoc=:date AND y.creneauSource.tranche.id IN :tranches))"),
    @NamedQuery(name = "YvsComDocStocks.countTrNonValide", query = "SELECT COUNT(y) FROM YvsComDocStocks y WHERE y.statut!=:statut AND y.destination=:depot AND y.dateDoc<=:date AND y.typeDoc=:type"),
    @NamedQuery(name = "YvsComDocStocks.findByDocInventaireAfter", query = "SELECT COUNT(y) FROM YvsComDocStocks y WHERE y.statut=:statut AND (y.source=:depot OR y.destination=:depot) AND y.typeDoc=:type AND (y.dateDoc>:date OR (y.dateDoc=:date AND ((y.creneauSource.tranche.heureFin>=:heure AND y.creneauSource.tranche.typeJournee=:typeJourne AND y.creneauSource.tranche.heureDebut<y.creneauSource.tranche.heureFin) OR (y.creneauDestinataire.tranche.heureFin>=:heure AND y.creneauDestinataire.tranche.typeJournee=:typeJourne AND y.creneauDestinataire.tranche.heureDebut<y.creneauDestinataire.tranche.heureFin))))"),
    @NamedQuery(name = "YvsComDocStocks.findByDocInventaireAfter1", query = "SELECT COUNT(y) FROM YvsComDocStocks y WHERE y.statut=:statut AND (y.source=:depot OR y.destination=:depot) AND y.typeDoc=:type AND (y.dateDoc>:date OR (y.dateDoc=:date AND (y.creneauSource.tranche.id IN :tranche OR y.creneauDestinataire.tranche.id IN :tranche)))"),
    @NamedQuery(name = "YvsComDocStocks.findLastByDocInventaireAfter", query = "SELECT y FROM YvsComDocStocks y WHERE y.statut=:statut AND (y.source=:depot OR y.destination=:depot) AND y.typeDoc=:type AND (y.dateDoc>:date OR (y.dateDoc=:date AND (y.creneauSource.tranche.id IN :tranche OR y.creneauDestinataire.tranche.id IN :tranche))) ORDER By y.dateDoc"),
    @NamedQuery(name = "YvsComDocStocks.findByDestinationCreneauDestinationStatut", query = "SELECT y FROM YvsComDocStocks y WHERE y.statut = :statut AND y.destination = :destination AND y.creneauDestinataire = :creneauDestinataire AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocStocks.findByDestinationCreneauSourceCreneauDestination", query = "SELECT y FROM YvsComDocStocks y WHERE y.destination = :destination AND y.creneauSource = :creneauSource AND y.creneauDestinataire = :creneauDestinataire AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocStocks.findBySourceCreneauSourceCreneauDestination", query = "SELECT y FROM YvsComDocStocks y WHERE y.source = :source AND y.creneauSource = :creneauSource AND y.creneauDestinataire = :creneauDestinataire AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocStocks.findBySourceDestinationCreneauSourceCreneauDestination", query = "SELECT y FROM YvsComDocStocks y WHERE y.destination = :destination AND y.source = :source AND y.creneauSource = :creneauSource AND y.creneauDestinataire = :creneauDestinataire AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocStocks.findByDestinationDateTypeDoc", query = "SELECT y FROM YvsComDocStocks y WHERE y.destination = :destination AND y.typeDoc = :typeDoc AND y.dateDoc = :dateDoc"),

    @NamedQuery(name = "YvsComDocStocks.findByDocumentLier", query = "SELECT y FROM YvsComDocStocks y WHERE y.documentLie = :document"),
    @NamedQuery(name = "YvsComDocStocks.findByDocumentLierStatut", query = "SELECT y FROM YvsComDocStocks y WHERE y.documentLie = :document AND y.statut = :statut"),
    @NamedQuery(name = "YvsComDocStocks.findByParentTypeStatut", query = "SELECT y FROM YvsComDocStocks y WHERE y.documentLie = :document AND y.statut = :statut AND y.typeDoc = :type"),

    @NamedQuery(name = "YvsComDocStocks.findByTypeDoc_", query = "SELECT y FROM YvsComDocStocks y WHERE (y.typeDoc = :typeDoc1 OR y.typeDoc = :typeDoc2) AND  y.societe = :societe"),
    @NamedQuery(name = "YvsComDocStocks.findLastByDepotsType", query = "SELECT y FROM YvsComDocStocks y WHERE y.statut=:statut AND y.typeDoc = :type AND (y.destination = :depot OR y.source = :depot) AND y.dateDoc <= :date ORDER BY y.dateDoc DESC"),
    @NamedQuery(name = "YvsComDocStocks.findFisrtByDepotsType", query = "SELECT y FROM YvsComDocStocks y WHERE y.statut=:statut AND y.typeDoc = :type AND (y.destination = :depot OR y.source = :depot) AND y.dateDoc >= :date ORDER BY y.dateDoc"),
    @NamedQuery(name = "YvsComDocStocks.findLastByDepotsTypeNoId", query = "SELECT y FROM YvsComDocStocks y WHERE y.statut=:statut AND y.typeDoc = :type AND (y.destination = :depot OR y.source = :depot) AND y.dateDoc <= :date AND y.id != :id ORDER BY y.dateDoc DESC"),
    @NamedQuery(name = "YvsComDocStocks.findFisrtByDepotsTypeNoId", query = "SELECT y FROM YvsComDocStocks y WHERE y.statut=:statut AND y.typeDoc = :type AND (y.destination = :depot OR y.source = :depot) AND y.dateDoc >= :date AND y.id != :id ORDER BY y.dateDoc"),
    @NamedQuery(name = "YvsComDocStocks.findByDepotsTranchesType", query = "SELECT y FROM YvsComDocStocks y WHERE y.statut=:statut AND y.typeDoc = :type AND ((y.destination = :depot AND y.creneauDestinataire.tranche = :tranche) OR (y.source = :depot AND y.creneauSource.tranche = :tranche)) AND y.dateDoc >= :date ORDER BY y.dateDoc"),
    @NamedQuery(name = "YvsComDocStocks.findBySupp", query = "SELECT y FROM YvsComDocStocks y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsComDocStocks.countMine", query = "SELECT COUNT(y) FROM YvsComDocStocks y WHERE (y.creneauDestinataire = :creno OR y.creneauDestinataire.depot.responsable=:responsable) AND y.statut=:statut AND y.typeDoc=:typeDoc"),
    @NamedQuery(name = "YvsComDocStocks.countByTypeDate", query = "SELECT COUNT(y) FROM YvsComDocStocks y WHERE y.dateDoc BETWEEN :dateDebut AND :dateFin AND y.statut=:statut AND y.typeDoc=:typeDoc AND y.societe = :societe"),
    @NamedQuery(name = "YvsComDocStocks.countByStatutsTypeDate", query = "SELECT COUNT(y) FROM YvsComDocStocks y WHERE y.dateDoc BETWEEN :dateDebut AND :dateFin AND y.statut IN :statuts AND y.typeDoc=:typeDoc AND y.societe = :societe"),
    @NamedQuery(name = "YvsComDocStocks.countByTypeDepot", query = "SELECT COUNT(y) FROM YvsComDocStocks y WHERE y.dateDoc BETWEEN :dateDebut AND :dateFin AND y.statut=:statut AND y.typeDoc=:typeDoc AND (y.destination = :depot OR y.source = :depot)"),
    @NamedQuery(name = "YvsComDocStocks.countByStatutsTypeDepot", query = "SELECT COUNT(y) FROM YvsComDocStocks y WHERE y.dateDoc BETWEEN :dateDebut AND :dateFin AND y.statut IN :statuts AND y.typeDoc=:typeDoc AND (y.destination = :depot OR y.source = :depot)"),
    @NamedQuery(name = "YvsComDocStocks.findByDepotsStatutAuto", query = "SELECT y FROM YvsComDocStocks y WHERE y.source = :source AND y.destination = :destination AND y.automatique = :auto AND y.statut = :statut ORDER BY y.dateDoc DESC"),
    @NamedQuery(name = "YvsComDocStocks.findByActif", query = "SELECT y FROM YvsComDocStocks y WHERE y.actif = :actif")})

public class YvsComDocStocks extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_doc_stocks_id_seq", name = "yvs_doc_stocks_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_doc_stocks_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_reception")
    @Temporal(TemporalType.DATE)
    private Date dateReception;
    @Size(max = 255)
    @Column(name = "num_piece")
    private String numPiece;
    @Size(max = 255)
    @Column(name = "num_doc")
    private String numDoc;
    @Column(name = "statut")
    private String statut;
    @Size(max = 255)
    @Column(name = "type_doc")
    private String typeDoc;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Column(name = "nature")
    private String nature;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "cloturer")
    private Boolean cloturer;
    @Column(name = "date_cloturer")
    @Temporal(TemporalType.DATE)
    private Date dateCloturer;
    @Column(name = "date_valider")
    @Temporal(TemporalType.DATE)
    private Date dateValider;
    @Column(name = "date_transmis")
    @Temporal(TemporalType.DATE)
    private Date dateTransmis;
    @Column(name = "date_annuler")
    @Temporal(TemporalType.DATE)
    private Date dateAnnuler;
    @Column(name = "heure_doc")
    @Temporal(TemporalType.TIME)
    private Date heureDoc;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "automatique")
    private Boolean automatique;
    @Column(name = "taux_ecart_inventaire")
    private Double tauxEcartInventaire;
    @Column(name = "etape_valide")
    private Integer etapeValide;
    @Column(name = "etape_total")
    private Integer etapeTotal;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_doc")
    @Temporal(TemporalType.DATE)
    private Date dateDoc;
    @Column(name = "impression")
    private Integer impression;

    @JoinColumn(name = "creneau_destinataire", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComCreneauDepot creneauDestinataire;
    @JoinColumn(name = "creneau_source", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComCreneauDepot creneauSource;
    @JoinColumn(name = "source", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots source;
    @JoinColumn(name = "destination", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots destination;
    @JoinColumn(name = "categorie_comptable", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCategorieComptable categorieComptable;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "valider_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers validerBy;
    @JoinColumn(name = "annuler_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers annulerBy;
    @JoinColumn(name = "cloturer_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers cloturerBy;
    @JoinColumn(name = "editeur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers editeur;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "author_transmis", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence authorTransmis;
    @JsonManagedReference
    @JoinColumn(name = "document_lie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocStocks documentLie;
    @JoinColumn(name = "nature_doc", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComNatureDoc natureDoc;
    @OneToOne(mappedBy = "docStock", fetch = FetchType.LAZY)
    private YvsComDocStocksValeur valeur;

    @JsonBackReference
    @OneToMany(mappedBy = "documentLie", fetch = FetchType.LAZY)
    private List<YvsComDocStocks> documents;
    @JsonBackReference
    @OneToMany(mappedBy = "docStock")
    private List<YvsComCoutSupDocStock> couts;
    @JsonBackReference
    @OneToMany(mappedBy = "docStock", fetch = FetchType.LAZY)
    private List<YvsComContenuDocStock> contenus;
    @JsonBackReference
    @OneToMany(mappedBy = "docStock")
    private List<YvsWorkflowValidDocStock> etapesValidations;
    @JsonBackReference
    @OneToMany(mappedBy = "docStock")
    private List<YvsComDocStocksEcart> ecarts;

    @Transient
    private static YvsComDocStocksValeur valeurInventaire = null;
    @Transient
    private List<YvsComDocStocksEcart> filters;
    @Transient
    private String mouvement;
    @Transient
    private boolean new_;
    @Transient
    private boolean in;
    @Transient
    private boolean liee;
    @Transient
    private boolean update;
    @Transient
    private boolean selectActif;
    @Transient
    private double qteRestant;
    @Transient
    private double montantTotal;
    @Transient
    private double montantTotalES;
    @Transient
    private boolean validIncomplet;
    @Transient
    private boolean charger;
    @Transient
    private String libEtapes;
    @Transient
    private String maDateSave;

    public YvsComDocStocks() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        etapesValidations = new ArrayList<>();
        documents = new ArrayList<>();
        contenus = new ArrayList<>();
        filters = new ArrayList<>();
    }

    public YvsComDocStocks(Long id) {
        this();
        this.id = id;
    }

    public YvsComDocStocks(Long id, String numDoc, String statut) {
        this(id);
        this.numDoc = numDoc;
        this.statut = statut;
    }

    public YvsComDocStocks(Long id, String numDoc, String statut, Date dateDoc) {
        this(id, numDoc, statut);
        this.dateDoc = dateDoc;
    }

    public YvsComDocStocks(YvsComDocStocks doc) {
        this(doc.id, doc);
    }

    public YvsComDocStocks(Long id, YvsComDocStocks doc) {
        this(id);
        this.dateUpdate = doc.getDateUpdate();
        this.dateDoc = doc.getDateDoc();
        this.dateReception = doc.getDateReception();
        this.numPiece = doc.getNumPiece();
        this.numDoc = doc.getNumDoc();
        this.statut = doc.getStatut();
        this.typeDoc = doc.getTypeDoc();
        this.description = doc.getDescription();
        this.nature = doc.getNature();
        this.supp = doc.getSupp();
        this.actif = doc.getActif();
        this.cloturer = doc.getCloturer();
        this.dateCloturer = doc.getDateCloturer();
        this.dateValider = doc.getDateValider();
        this.dateAnnuler = doc.getDateAnnuler();
        this.heureDoc = doc.getHeureDoc();
        this.dateSave = doc.getDateSave();
        this.automatique = doc.getAutomatique();
        this.etapeValide = doc.getEtapeValide();
        this.etapeTotal = doc.getEtapeTotal();
        this.creneauDestinataire = doc.getCreneauDestinataire();
        this.creneauSource = doc.getCreneauSource();
        this.source = doc.getSource();
        this.destination = doc.getDestination();
        this.categorieComptable = doc.getCategorieComptable();
        this.societe = doc.getSociete();
        this.validerBy = doc.getValiderBy();
        this.annulerBy = doc.getAnnulerBy();
        this.cloturerBy = doc.getCloturerBy();
        this.editeur = doc.getEditeur();
        this.impression = doc.getImpression();
        this.author = doc.getAuthor();
        this.documentLie = doc.getDocumentLie();
    }

    public YvsComDocStocks(Long id, Date dateUpdate, Date dateDoc, String numPiece, String numDoc, String statut, String typeDoc, String description, String nature, Boolean supp, Boolean actif, Boolean cloturer, Date dateCloturer, Date dateValider, Date dateAnnuler, Date heureDoc, Date dateSave, Boolean automatique, Integer etapeValide, Integer etapeTotal, YvsComCreneauDepot creneauDestinataire, YvsComCreneauDepot creneauSource, YvsBaseDepots source, YvsBaseDepots destination, YvsBaseCategorieComptable categorieComptable, YvsSocietes societe, YvsUsers validerBy, YvsUsers annulerBy, YvsUsers cloturerBy, YvsUsers editeur, Integer impression, YvsUsersAgence author, YvsComDocStocks documentLie, List<YvsComDocStocks> documents, List<YvsComCoutSupDocStock> couts, List<YvsComContenuDocStock> contenus, List<YvsWorkflowValidDocStock> etapesValidations, String mouvement, boolean new_, boolean in, boolean liee, boolean update, boolean selectActif, double qteRestant, boolean validIncomplet, String libEtapes) {

    }

    public Boolean getAutomatique() {
        return automatique != null ? automatique : false;
    }

    public void setAutomatique(Boolean automatique) {
        this.automatique = automatique;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public List<YvsWorkflowValidDocStock> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidDocStock> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    public String getLibEtapes() {
        libEtapes = "Etp. " + getEtapeValide() + " / " + getEtapeTotal();
        return libEtapes;
    }

    public void setLibEtapes(String libEtapes) {
        this.libEtapes = libEtapes;
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

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateReception() {
        return dateReception;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateReception(Date dateReception) {
        this.dateReception = dateReception;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsers getEditeur() {
        return editeur;
    }

    public void setEditeur(YvsUsers editeur) {
        this.editeur = editeur;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsers getCloturerBy() {
        return cloturerBy;
    }

    public void setCloturerBy(YvsUsers cloturerBy) {
        this.cloturerBy = cloturerBy;
    }

    public boolean isValidIncomplet() {
        if (getStatut().equals(Constantes.ETAT_VALIDE)) {
            for (YvsComContenuDocStock c : contenus) {
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

    public boolean isLiee() {
        liee = documents != null ? !documents.isEmpty() : false;
        return liee;
    }

    public void setValeurInventaire(YvsComDocStocksValeur valeurInventaire) {
        YvsComDocStocks.valeurInventaire = valeurInventaire;
    }

    public void setLiee(boolean liee) {
        this.liee = liee;
    }

    public boolean isUpdate() {
        return update;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isIn() {
        return in;
    }

    public void setIn(boolean in) {
        this.in = in;
    }

    public double getQteRestant() {
        return qteRestant;
    }

    public void setQteRestant(double qteRestant) {
        this.qteRestant = qteRestant;
    }

    public String getMouvement() {
        if ("ES".equals(typeDoc)) {
            mouvement = "E";
        } else {
            mouvement = "S";
        }
        return mouvement;
    }

    public void setMouvement(String mouvement) {
        this.mouvement = mouvement;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public boolean isCharger() {
        return charger;
    }

    public void setCharger(boolean charger) {
        this.charger = charger;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateDoc() {
        return dateDoc != null ? dateDoc : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateDoc(Date dateDoc) {
        this.dateDoc = dateDoc;
    }

    public String getNumPiece() {
        return numPiece;
    }

    public void setNumPiece(String numPiece) {
        this.numPiece = numPiece;
    }

    public String getNumDoc() {
        return numDoc != null ? numDoc : "";
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public String getStatut() {
        return statut != null ? (statut.trim().length() > 0 ? statut : Constantes.ETAT_EDITABLE) : Constantes.ETAT_EDITABLE;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getTypeDoc() {
        return typeDoc != null ? typeDoc : "";
    }

    public void setTypeDoc(String typeDoc) {
        this.typeDoc = typeDoc;
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

    public Double getTauxEcartInventaire() {
        return tauxEcartInventaire != null ? tauxEcartInventaire : 1;
    }

    public void setTauxEcartInventaire(Double tauxEcartInventaire) {
        this.tauxEcartInventaire = tauxEcartInventaire;
    }

    public double getMontantTotalES() {
        return montantTotalES;
    }

    public void setMontantTotalES(double montantTotalES) {
        this.montantTotalES = montantTotalES;
    }

    public YvsComCreneauDepot getCreneauDestinataire() {
        return creneauDestinataire;
    }

    public void setCreneauDestinataire(YvsComCreneauDepot creneauDestinataire) {
        this.creneauDestinataire = creneauDestinataire;
    }

//    @XmlTransient  @JsonIgnore
    public YvsComCreneauDepot getCreneauSource() {
        return creneauSource;
    }

    public void setCreneauSource(YvsComCreneauDepot creneauSource) {
        this.creneauSource = creneauSource;
    }

//    @XmlTransient  @JsonIgnore
    public YvsBaseDepots getSource() {
        return source;
    }

    public void setSource(YvsBaseDepots source) {
        this.source = source;
    }

    public YvsBaseDepots getDestination() {
        return destination;
    }

    public void setDestination(YvsBaseDepots destination) {
        this.destination = destination;
    }

    public YvsBaseCategorieComptable getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(YvsBaseCategorieComptable categorieComptable) {
        this.categorieComptable = categorieComptable;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComCoutSupDocStock> getCouts() {
        return couts;
    }

    public void setCouts(List<YvsComCoutSupDocStock> couts) {
        this.couts = couts;
    }

    public List<YvsComContenuDocStock> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComContenuDocStock> contenus) {
        this.contenus = contenus;
    }

    public List<YvsComDocStocks> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComDocStocks> documents) {
        this.documents = documents;
    }

    public YvsComDocStocks getDocumentLie() {
        return documentLie;
    }

    public void setDocumentLie(YvsComDocStocks documentLie) {
        this.documentLie = documentLie;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getHeureDoc() {
        return heureDoc;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setHeureDoc(Date heureDoc) {
        this.heureDoc = heureDoc;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Integer getImpression() {
        return impression;
    }

    public void setImpression(Integer impression) {
        this.impression = impression;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getCloturer() {
        return cloturer != null ? cloturer : false;
    }

    public void setCloturer(Boolean cloturer) {
        this.cloturer = cloturer;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateCloturer() {
        return dateCloturer;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateCloturer(Date dateCloturer) {
        this.dateCloturer = dateCloturer;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateValider() {
        return dateValider;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateValider(Date dateValider) {
        this.dateValider = dateValider;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateAnnuler() {
        return dateAnnuler;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateAnnuler(Date dateAnnuler) {
        this.dateAnnuler = dateAnnuler;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateTransmis() {
        return dateTransmis != null ? dateTransmis : getDateUpdate();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateTransmis(Date dateTransmis) {
        this.dateTransmis = dateTransmis;
    }

    public YvsUsersAgence getAuthorTransmis() {
        return authorTransmis != null ? authorTransmis : getAuthor();
    }

    public void setAuthorTransmis(YvsUsersAgence authorTransmis) {
        this.authorTransmis = authorTransmis;
    }

    @XmlTransient
    @JsonIgnore
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

    public String getMaDateSave() {
        return getDateSave() != null ? dfs.format(getDateSave()) : "";
    }

    public void setMaDateSave(String maDateSave) {
        this.maDateSave = maDateSave;
    }

    @XmlTransient
    @JsonIgnore
    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComDocStocksValeur getValeur() {
        return valeur;
    }

    public void setValeur(YvsComDocStocksValeur valeur) {
        this.valeur = valeur;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComDocStocksEcart> getFilters() {
        return filters;
    }

    public void setFilters(List<YvsComDocStocksEcart> filters) {
        this.filters = filters;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComDocStocksEcart> getEcarts() {
        return ecarts;
    }

    public void setEcarts(List<YvsComDocStocksEcart> ecarts) {
        this.ecarts = ecarts;
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
        if (!(object instanceof YvsComDocStocks)) {
            return false;
        }
        YvsComDocStocks other = (YvsComDocStocks) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.stock.YvsComDocStocks[ id=" + id + " ]";
    }

    public boolean canEditable() {
        return getStatut().equals(Constantes.ETAT_ATTENTE) || getStatut().equals(Constantes.ETAT_EDITABLE);
    }

    public boolean canDelete() {
        return getStatut().equals(Constantes.ETAT_ATTENTE) || getStatut().equals(Constantes.ETAT_EDITABLE) || getStatut().equals(Constantes.ETAT_SUSPENDU) || getStatut().equals(Constantes.ETAT_ANNULE);
    }

    public YvsComNatureDoc getNatureDoc() {
        return natureDoc;
    }

    public void setNatureDoc(YvsComNatureDoc natureDoc) {
        this.natureDoc = natureDoc;
    }

    @XmlTransient
    @JsonIgnore
    public double calculMontantTotal(DaoInterfaceLocal dao, YvsComDocStocksValeur valeurInventaire) {
        return calculMontantTotal(dao, valeurInventaire, false);
    }

    public double calculMontantTotal(DaoInterfaceLocal dao, YvsComDocStocksValeur valeurInventaire, boolean justify) {
        montantTotal = 0;
        if (Util.asString(getTypeDoc())) {
            if (getTypeDoc().equals(Constantes.TYPE_IN)) {
                double manquant = 0, excedent = 0;
                this.valeurInventaire = valeurInventaire;
                for (YvsComDocStocks d : getDocuments()) {
                    if (d.getTypeDoc().equals(Constantes.TYPE_ES)) {
                        excedent = d.calculMontantTotal(dao, justify);
                    } else {
                        manquant = d.calculMontantTotal(dao, justify);
                    }
                }
                montantTotal = manquant - excedent;
            } else {
                YvsComDocStocksValeur valeur = this.valeurInventaire != null ? this.valeurInventaire : (valeurInventaire != null ? valeurInventaire : getValeur());
                double coefficient = valeur != null ? valeur.getCoefficient() : 1;
                String valoriseMs = valeur != null ? valeur.getValoriseMsBy() : "V";
                String valoriseMp = valeur != null ? valeur.getValoriseMpBy() : "A";
                String valorisePf = valeur != null ? valeur.getValorisePfBy() : "V";
                String valorisePfs = valeur != null ? valeur.getValorisePfsBy() : "R";
                if (getContenus() != null ? getContenus().isEmpty() : true) {
                    setContenus(dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{this}));
                }
                for (YvsComContenuDocStock c : getContenus()) {
                    if (c.getImpactValeurInventaire()) {
                        double prix = c.getPrix();
                        if (valeur != null ? valeur.getId() > 0 : false) {
                            switch (c.getArticle().getCategorie()) {
                                case Constantes.CAT_MP:
                                    prix = c.getPrix(dao, valoriseMp);
                                    break;
                                case Constantes.CAT_MARCHANDISE:
                                    prix = c.getPrix(dao, valoriseMs);
                                    break;
                                case Constantes.CAT_PF:
                                    prix = c.getPrix(dao, valorisePf);
                                    break;
                                case Constantes.CAT_PSF:
                                    prix = c.getPrix(dao, valorisePfs);
                                    break;
                            }
                        }
                        prix = prix * coefficient;
                        double quantite = (c.getQuantite() != 0 ? c.getQuantite() : c.getQuantiteEntree());
                        if (justify) {
                            quantite -= c.getQteAttente();
                        }
                        double montant = (quantite * prix);
                        montantTotal += montant;
                    }
                }
            }
        }
        return montantTotal;
    }

    @XmlTransient
    @JsonIgnore
    public double calculMontantTotal(DaoInterfaceLocal dao, boolean justify) {
        montantTotal = calculMontantTotal(dao, getValeur(), justify);
        return montantTotal;
    }

}
