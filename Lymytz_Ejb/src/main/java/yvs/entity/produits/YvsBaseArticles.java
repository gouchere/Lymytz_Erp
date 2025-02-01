/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.produits;

import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.entity.produits.group.YvsBaseGroupesArticle;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.base.YvsBaseArticleAnalytique;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.prod.YvsBaseArticlesTemplate;
import yvs.entity.base.YvsBaseArticleEquivalent;
import yvs.entity.base.YvsBaseArticleFournisseur;
import yvs.entity.base.YvsBaseArticlePoint;
import yvs.entity.base.YvsBaseArticleSubstitution;
import yvs.entity.base.YvsBaseClassesStat;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.client.YvsBasePlanTarifaire;
import yvs.entity.param.YvsBaseTarifPointLivraison;
import yvs.entity.production.base.YvsProdGammeArticle;
import yvs.entity.production.base.YvsProdNomenclature;
import yvs.entity.production.pilotage.YvsProdContenuConditionnement;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_articles")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticles.findCountAll", query = "SELECT COUNT(y) FROM YvsBaseArticles y WHERE y.famille.societe =:societe"),
    @NamedQuery(name = "YvsBaseArticles.findAll", query = "SELECT y FROM YvsBaseArticles y WHERE y.famille.societe =:societe ORDER BY y.refArt"),
    @NamedQuery(name = "YvsBaseArticles.findAllOrder", query = "SELECT DISTINCT y.ordre FROM YvsBaseArticles y WHERE y.famille.societe =:societe ORDER BY y.ordre DESC"),
    @NamedQuery(name = "YvsBaseArticles.findAllArticleOrder", query = "SELECT y FROM YvsBaseArticles y WHERE y.famille.societe =:societe AND y.ordre = :ordre ORDER BY y.ordre DESC"),
    @NamedQuery(name = "YvsBaseArticles.findTrue", query = "SELECT y FROM YvsBaseArticles y WHERE y.famille.societe =:societe AND y.actif = true ORDER BY y.refArt"),
    @NamedQuery(name = "YvsBaseArticles.findById", query = "SELECT y FROM YvsBaseArticles y JOIN FETCH y.famille  WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticles.findAllById", query = "SELECT y FROM YvsBaseArticles y JOIN FETCH y.famille LEFT JOIN FETCH y.classe1 LEFT JOIN FETCH y.classe2 LEFT JOIN FETCH y.template"
            + " LEFT JOIN FETCH y.groupe WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticles.findByNotId", query = "SELECT y FROM YvsBaseArticles y WHERE y.id != :id AND y.famille.societe =:societe ORDER BY y.refArt"),
    @NamedQuery(name = "YvsBaseArticles.findByIds", query = "SELECT y FROM YvsBaseArticles y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsBaseArticles.findCodeByIds", query = "SELECT y.refArt FROM YvsBaseArticles y WHERE y.id IN :ids ORDER BY y.id"),
    @NamedQuery(name = "YvsBaseArticles.findLast", query = "SELECT y FROM YvsBaseArticles y WHERE y.famille.societe=:societe ORDER BY y.id DESC"),
    @NamedQuery(name = "YvsBaseArticles.findByCodeLActif", query = "SELECT y FROM YvsBaseArticles y WHERE y.famille.societe = :societe AND (UPPER(y.refArt) LIKE UPPER(:code) OR UPPER(y.designation) LIKE UPPER (:code)) AND y.actif = true ORDER BY y.refArt"),
    @NamedQuery(name = "YvsBaseArticles.findByCodeFamilleActif", query = "SELECT y FROM YvsBaseArticles y WHERE y.famille.societe = :societe AND (UPPER(y.famille.designation) LIKE UPPER(:code) OR UPPER(y.famille.referenceFamille) LIKE UPPER (:code)) AND y.actif = true ORDER BY y.refArt"),
    @NamedQuery(name = "YvsBaseArticles.countByCodeLActif", query = "SELECT COUNT(y) FROM YvsBaseArticles y WHERE y.famille.societe = :societe AND (UPPER(y.refArt) LIKE UPPER(:code) OR UPPER(y.designation) LIKE UPPER(:code)) AND y.actif = true "),
    @NamedQuery(name = "YvsBaseArticles.countyCodeFamilleActif", query = "SELECT COUNT(y) FROM YvsBaseArticles y WHERE y.famille.societe = :societe AND (UPPER(y.famille.designation) LIKE UPPER(:code) OR UPPER(y.famille.referenceFamille) LIKE UPPER (:code)) AND y.actif = true "),
    @NamedQuery(name = "YvsBaseArticles.findByCodeL", query = "SELECT y FROM YvsBaseArticles y WHERE y.famille.societe = :societe AND ((UPPER(y.refArt) LIKE UPPER(:code) OR UPPER(y.designation) LIKE UPPER(:code))) ORDER BY y.refArt"),
    @NamedQuery(name = "YvsBaseArticles.findSimpleByCodeL", query = "SELECT y.id, y.refArt, y.designation FROM YvsBaseArticles y WHERE y.famille.societe = :societe AND ((UPPER(y.refArt) LIKE UPPER(:code) OR UPPER(y.designation) LIKE UPPER(:code))) ORDER BY y.refArt"),
    @NamedQuery(name = "YvsBaseArticles.findIdByCodeL", query = "SELECT y.id FROM YvsBaseArticles y WHERE y.famille.societe = :societe AND ((UPPER(y.refArt) LIKE UPPER(:code) OR UPPER(y.designation) LIKE UPPER(:code))) ORDER BY y.refArt"),
    @NamedQuery(name = "YvsBaseArticles.findByCode", query = "SELECT y FROM YvsBaseArticles y WHERE y.famille.societe = :societe AND y.refArt = :code"),
    @NamedQuery(name = "YvsBaseArticles.controlByCode", query = "SELECT COUNT(y) FROM YvsBaseArticles y WHERE y.famille.societe = :societe AND y.refArt = :code AND y.id!=:id"),
    @NamedQuery(name = "YvsBaseArticles.findByChangePrix", query = "SELECT y FROM YvsBaseArticles y WHERE y.changePrix = :changePrix"),
    @NamedQuery(name = "YvsBaseArticles.findByDescription", query = "SELECT y FROM YvsBaseArticles y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseArticles.findByDefNorme", query = "SELECT y FROM YvsBaseArticles y WHERE y.defNorme = :defNorme"),
    @NamedQuery(name = "YvsBaseArticles.findByDesignation", query = "SELECT y FROM YvsBaseArticles y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseArticles.findDesignationArtById", query = "SELECT y.designation FROM YvsBaseArticles y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticles.findByModeConso", query = "SELECT y FROM YvsBaseArticles y WHERE y.modeConso = :modeConso"),
    @NamedQuery(name = "YvsBaseArticles.findByNorme", query = "SELECT y FROM YvsBaseArticles y WHERE y.norme = :norme"),
    @NamedQuery(name = "YvsBaseArticles.findByPhoto1", query = "SELECT y FROM YvsBaseArticles y WHERE y.photo1 = :photo1"),
    @NamedQuery(name = "YvsBaseArticles.findByMasseNet", query = "SELECT y FROM YvsBaseArticles y WHERE y.masseNet = :masseNet"),
    @NamedQuery(name = "YvsBaseArticles.findByPrixMin", query = "SELECT y FROM YvsBaseArticles y WHERE y.prixMin = :prixMin"),
    @NamedQuery(name = "YvsBaseArticles.findByPua", query = "SELECT y FROM YvsBaseArticles y WHERE y.pua = :pua"),

    @NamedQuery(name = "YvsBaseArticles.findByNotClassStat", query = "SELECT y FROM YvsBaseArticles y WHERE y.classe1 is null AND y.classe2 is null AND y.famille.societe.id=:societe"),
    @NamedQuery(name = "YvsBaseArticles.findIdByNotClassStat", query = "SELECT y.id FROM YvsBaseArticles y WHERE y.classe1 is null AND y.classe2 is null AND y.famille.societe.id=:societe"),

    @NamedQuery(name = "YvsBaseArticles.findByPuv", query = "SELECT y FROM YvsBaseArticles y WHERE y.puv = :puv"),
    @NamedQuery(name = "YvsBaseArticles.findByFamille", query = "SELECT y FROM YvsBaseArticles y WHERE y.famille = :famille ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseArticles.findLastCodeLikeCode", query = "SELECT y.refArt FROM YvsBaseArticles y WHERE y.famille.societe = :societe AND UPPER(y.refArt) LIKE UPPER(:code) ORDER BY y.refArt DESC"),
    @NamedQuery(name = "YvsBaseArticles.findBySociete", query = "SELECT y FROM YvsBaseArticles y WHERE y.famille.societe = :societe AND y.actif = true ORDER BY y.refArt"),
    @NamedQuery(name = "YvsBaseArticles.findByRefArt", query = "SELECT y FROM YvsBaseArticles y WHERE y.refArt = :refArt"),
    @NamedQuery(name = "YvsBaseArticles.findCByRefArtEx", query = "SELECT COUNT(y) FROM YvsBaseArticles y WHERE y.refArt = :refArt AND y.id!=:id AND y.famille.societe.id=:societe"),
    @NamedQuery(name = "YvsBaseArticles.findCByRefArt", query = "SELECT COUNT(y) FROM YvsBaseArticles y WHERE y.refArt = :refArt AND y.famille.societe=:societe"),
    @NamedQuery(name = "YvsBaseArticles.findByRemise", query = "SELECT y FROM YvsBaseArticles y WHERE y.remise = :remise"),
    @NamedQuery(name = "YvsBaseArticles.findBySuiviEnStock", query = "SELECT y FROM YvsBaseArticles y WHERE y.suiviEnStock = :suiviEnStock"),
    @NamedQuery(name = "YvsBaseArticles.findByUniteDeMasse", query = "SELECT y FROM YvsBaseArticles y WHERE y.uniteDeMasse = :uniteDeMasse"),
    @NamedQuery(name = "YvsBaseArticles.findByVisibleEnSynthese", query = "SELECT y FROM YvsBaseArticles y WHERE y.visibleEnSynthese = :visibleEnSynthese"),
    @NamedQuery(name = "YvsBaseArticles.findByClassStat", query = "SELECT y FROM YvsBaseArticles y WHERE y.classStat = :classStat"),
    @NamedQuery(name = "YvsBaseArticles.findByCoefficient", query = "SELECT y FROM YvsBaseArticles y WHERE y.coefficient = :coefficient"),
    @NamedQuery(name = "YvsBaseArticles.findByService", query = "SELECT y FROM YvsBaseArticles y WHERE y.service = :service"),
    @NamedQuery(name = "YvsBaseArticles.findByMethodeVal", query = "SELECT y FROM YvsBaseArticles y WHERE y.methodeVal = :methodeVal"),
    @NamedQuery(name = "YvsBaseArticles.findByActif", query = "SELECT y FROM YvsBaseArticles y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseArticles.findByFamilleActif", query = "SELECT y FROM YvsBaseArticles y WHERE y.famille = :famille AND y.actif = :actif"),
    @NamedQuery(name = "YvsBaseArticles.findByPhoto2", query = "SELECT y FROM YvsBaseArticles y WHERE y.photo2 = :photo2"),
    @NamedQuery(name = "YvsBaseArticles.findByPhoto3", query = "SELECT y FROM YvsBaseArticles y WHERE y.photo3 = :photo3"),
    @NamedQuery(name = "YvsBaseArticles.findByCategorie", query = "SELECT y FROM YvsBaseArticles y WHERE y.categorie != :categorie"),
    @NamedQuery(name = "YvsBaseArticles.findByPFANDNEGOCE", query = "SELECT y.id, y.refArt, y.designation FROM YvsBaseArticles y WHERE (y.categorie = :categorie OR y.categorie=:categorie1) AND y.actif=true AND y.famille.societe=:societe ORDER BY y.refArt"),
    @NamedQuery(name = "YvsBaseArticles.findFullByPFANDNEGOCE", query = "SELECT y FROM YvsBaseArticles y WHERE (y.categorie = :categorie OR y.categorie=:categorie1) AND y.actif=true AND y.famille.societe=:societe ORDER BY y.refArt"),
    @NamedQuery(name = "YvsBaseArticles.findByIds_", query = "SELECT y.id, y.refArt, y.designation FROM YvsBaseArticles y WHERE y.id IN :ids ORDER BY y.refArt"),
    @NamedQuery(name = "YvsBaseArticles.findByDureeVie", query = "SELECT y FROM YvsBaseArticles y WHERE y.delaiLivraison = :dureeVie"),
    @NamedQuery(name = "YvsBaseArticles.findByDureeGarantie", query = "SELECT y FROM YvsBaseArticles y WHERE y.dureeGarantie = :dureeGarantie"),

    @NamedQuery(name = "YvsBaseArticles.findAllNotIds", query = "SELECT y FROM YvsBaseArticles y WHERE y.famille.societe =:societe AND y.id NOT IN :ids ORDER BY y.refArt"),

    @NamedQuery(name = "YvsBaseArticles.findServiceByOnline", query = "SELECT DISTINCT y.typeService FROM YvsBaseArticles y WHERE y.famille.societe.venteOnline = :venteOnline"),
    @NamedQuery(name = "YvsBaseArticles.findServiceBySociete", query = "SELECT DISTINCT y.typeService FROM YvsBaseArticles y WHERE y.famille.societe = :societe"),
    @NamedQuery(name = "YvsBaseArticles.findGroupeByOnline", query = "SELECT DISTINCT y.groupe FROM YvsBaseArticles y WHERE y.typeService = :typeService AND y.famille.societe.venteOnline = :venteOnline ORDER BY y.groupe.designation"),
    @NamedQuery(name = "YvsBaseArticles.findGroupeBySociete", query = "SELECT DISTINCT y.groupe FROM YvsBaseArticles y WHERE y.typeService = :typeService AND y.famille.societe = :societe ORDER BY y.groupe.designation"),

    @NamedQuery(name = "YvsBaseArticles.findByTemplate", query = "SELECT y FROM YvsBaseArticles y WHERE y.template = :template")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseArticles extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_articles_id_seq", name = "yvs_articles_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_articles_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "tags")
    private String tags;
    @Size(max = 255)
    @Column(name = "designation")
    private String designation;
    @Size(max = 255)
    @Column(name = "mode_conso")
    private String modeConso;
    @Size(max = 2147483647)
    @Column(name = "photo_1")
    private String photo1;
    @Size(max = 255)
    @Column(name = "photo_1_extension")
    private String photo1Extension;
    @Size(max = 255)
    @Column(name = "ref_art")
    private String refArt;
    @Size(max = 2147483647)
    @Column(name = "class_stat")
    private String classStat;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "coefficient")
    private Double coefficient;
    @Column(name = "service")
    private Boolean service;
    @Size(max = 2147483647)
    @Column(name = "methode_val")
    private String methodeVal;
    @Column(name = "actif")
    private Boolean actif;
    @Size(max = 2147483647)
    @Column(name = "photo_2")
    private String photo2;
    @Size(max = 255)
    @Column(name = "photo_2_extension")
    private String photo2Extension;
    @Size(max = 2147483647)
    @Column(name = "photo_3")
    private String photo3;
    @Size(max = 255)
    @Column(name = "photo_3_extension")
    private String photo3Extension;
    @Size(max = 2147483647)
    @Column(name = "categorie")
    private String categorie;
    @Column(name = "duree_vie")
    private Double delaiLivraison;
    @Column(name = "ordre")
    private Integer ordre;
    @Column(name = "duree_garantie")
    private Double dureeGarantie;
    @Column(name = "def_norme")
    private Boolean defNorme;
    @Column(name = "norme")
    private Boolean norme;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "masse_net")
    private Double masseNet;
    @Column(name = "prix_min")
    private Double prixMin;
    @Column(name = "pua")
    private Double pua;
    @Column(name = "pua_ttc")
    private Boolean puaTtc;
    @Column(name = "puv")
    private Double puv;
    @Column(name = "change_prix")
    private Boolean changePrix;
    @Column(name = "puv_ttc")
    private Boolean puvTtc;
    @Column(name = "remise")
    private Double remise;
    @Column(name = "suivi_en_stock")
    private Boolean suiviEnStock;
    @Column(name = "visible_en_synthese")
    private Boolean visibleEnSynthese;
    @Column(name = "controle_fournisseur")
    private Boolean controleFournisseur;
    @Size(max = 2147483647)
    @Column(name = "fichier")
    private String fichier;
    @Column(name = "nature_prix_min")
    private String naturePrixMin;
    @Column(name = "type_service")
    private Character typeService;
    @Column(name = "lot_fabrication")
    private Integer lotFabrication;
    @Column(name = "taux_ecart_pr")
    private Double tauxEcartPr;
    @Column(name = "requiere_lot")
    private Boolean requiereLot = false;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "template", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    private YvsBaseArticlesTemplate template;
    @JoinColumn(name = "groupe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    private YvsBaseGroupesArticle groupe;
    @JoinColumn(name = "famille", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    private YvsBaseFamilleArticle famille;
    @JoinColumn(name = "classe1", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    private YvsBaseClassesStat classe1;
    @JoinColumn(name = "classe2", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    private YvsBaseClassesStat classe2;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    private YvsUsersAgence author;

    @JoinColumn(name = "fabriquant", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTiers fabriquant;
    @JoinColumn(name = "unite_stockage", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseUniteMesure uniteStockage;
    @JoinColumn(name = "unite_vente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseUniteMesure uniteVente;
    @JoinColumn(name = "unite_de_masse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseUniteMesure uniteDeMasse;
    @JoinColumn(name = "unite_volume", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseUniteMesure uniteDeVolume;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<YvsBaseArticlesAvis> avis;
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<YvsBaseArticleDescription> descriptions;
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<YvsBasePlanTarifaire> plans_tarifaires;
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<YvsBaseArticleCategorieComptable> comptes;
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<YvsBaseArticleFournisseur> fournisseurs;
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<YvsBaseArticleEquivalent> articlesEquivalents;
    @OneToMany(mappedBy = "articleEquivalent", fetch = FetchType.LAZY)
    private List<YvsBaseArticleEquivalent> yvsProdArticleEquivalentList1;
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<YvsBaseArticleSubstitution> articlesSubstitutions;
    @OneToMany(mappedBy = "articleSubstitution", fetch = FetchType.LAZY)
    private List<YvsBaseArticleSubstitution> yvsProdArticleSubstitutionList1;
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<YvsBaseArticleDepot> yvsBaseArticleDepotList;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<YvsProdContenuConditionnement> yvsProdContenuConditionnementList;
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<YvsProdNomenclature> yvsProdNomenclatureList;
//    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    @Transient
    private List<YvsBaseConditionnement> conditionnements;
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<YvsBaseArticleAnalytique> analytiques;
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<YvsBaseTarifPointLivraison> tarifs;

    @Transient
    private List<YvsBaseArticlePoint> articlesPoints;
    @Transient
    private List<YvsProdGammeArticle> gammes;
    @Transient
    private List<YvsProdNomenclature> nomenclatures;
    @Transient
    private String message_service;
    @Transient
    private int other;
    @Transient
    private boolean new_;
    @Transient
    private boolean useLotInDepot;
    @Transient
    private boolean error;
    @Transient
    private boolean synchroniser = true;
    @Transient
    private double quantite;
    @Transient
    private double quantitePrevu;
    @Transient
    private double quantitePrec;
    @Transient
    private double quantitePrevuPrec;
    @Transient
    private String libelle;
    @Transient
    private String photo, bytePhoto1, bytePhoto2, bytePhoto3;
    @Transient
    private String codeBarre;
    @Transient
    private YvsBaseConditionnement unite;
    @Transient
    private YvsBaseTarifPointLivraison infoLiv;
    @Transient
    private List<String> photos, photosExtension;

    public YvsBaseArticles() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        plans_tarifaires = new ArrayList<>();
        photos = new ArrayList<>();
        photosExtension = new ArrayList<>();
        comptes = new ArrayList<>();
        conditionnements = new ArrayList<>();
        descriptions = new ArrayList<>();
        analytiques = new ArrayList<>();
        avis = new ArrayList<>();
        tarifs = new ArrayList<>();
        articlesPoints = new ArrayList<>();
    }

    public YvsBaseArticles(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseArticles(Long id, String ref, String des) {
        this(id);
        this.refArt = ref;
        this.designation = des;
    }

    public YvsBaseArticles(Long id, String ref, String des, boolean puvTtc) {
        this(id, ref, des);
        this.puvTtc = puvTtc;
    }

    public YvsBaseArticles(Long id, String ref, String des, boolean puvTtc, boolean actif) {
        this(id, ref, des, puvTtc);
        this.actif = actif;
    }

    public YvsBaseArticles(Long id, boolean changePrix, boolean defNorme, boolean norme, double masseNet, double prixMin, double pua, double puv, double remise, boolean suiviEnStock, boolean visibleEnSynthese) {
        this(id);
        this.changePrix = changePrix;
        this.defNorme = defNorme;
        this.norme = norme;
        this.masseNet = masseNet;
        this.prixMin = prixMin;
        this.pua = pua;
        this.puv = puv;
        this.remise = remise;
        this.suiviEnStock = suiviEnStock;
        this.visibleEnSynthese = visibleEnSynthese;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMessage_service() {
        return message_service;
    }

    public void setMessage_service(String message_service) {
        this.message_service = message_service;
    }

    public boolean getChangePrix() {
        return changePrix != null ? changePrix : false;
    }

    public void setChangePrix(Boolean changePrix) {
        this.changePrix = changePrix;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLibelle() {
        libelle = getDesignation().trim().length() < 15 ? getDesignation() : getDesignation().substring(0, 15);
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDesignation() {
        return designation != null ? designation : "";
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getModeConso() {
        return modeConso != null ? modeConso : "";
    }

    public void setModeConso(String modeConso) {
        this.modeConso = modeConso;
    }

    public Double getMasseNet() {
        return masseNet != null ? masseNet : 0.0;
    }

    public void setMasseNet(Double masseNet) {
        this.masseNet = masseNet;
    }

    public Double getPrixMin() {
        return prixMin != null ? prixMin : 0.0;
    }

    public void setPrixMin(Double prixMin) {
        this.prixMin = prixMin;
    }

    public String getRefArt() {
        return refArt != null ? refArt : "";
    }

    public void setRefArt(String refArt) {
        this.refArt = refArt;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Boolean getControleFournisseur() {
        return controleFournisseur != null ? controleFournisseur : false;
    }

    public void setControleFournisseur(Boolean controleFournisseur) {
        this.controleFournisseur = controleFournisseur;
    }

    public Boolean getSuiviEnStock() {
        return suiviEnStock != null ? suiviEnStock : true;
    }

    public void setSuiviEnStock(Boolean suiviEnStock) {
        this.suiviEnStock = suiviEnStock;
    }

    public Boolean getVisibleEnSynthese() {
        return visibleEnSynthese != null ? visibleEnSynthese : false;
    }

    public void setVisibleEnSynthese(Boolean visibleEnSynthese) {
        this.visibleEnSynthese = visibleEnSynthese;
    }

    public String getClassStat() {
        return classStat != null ? classStat : "";
    }

    public void setClassStat(String classStat) {
        this.classStat = classStat;
    }

    public Double getCoefficient() {
        return coefficient != null ? coefficient : 0.0;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public Boolean getService() {
        return service != null ? service : false;
    }

    public void setService(Boolean service) {
        this.service = service;
    }

    public String getMethodeVal() {
        return methodeVal != null ? methodeVal : "FIFO";
    }

    public void setMethodeVal(String methodeVal) {
        this.methodeVal = methodeVal;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getPhoto1() {
//        String chemin = Chemins.getCheminArticle(this);
//        File file = new File(chemin + FILE_SEPARATOR + photo1);
//        if (!file.exists()) {
//
//        }
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
//        String chemin = Chemins.getCheminArticle(this);
//        File file = new File(chemin + FILE_SEPARATOR + photo2);
//        if (!file.exists()) {
//
//        }
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
//        String chemin = Chemins.getCheminArticle(this);
//        File file = new File(chemin + FILE_SEPARATOR + photo3);
//        if (!file.exists()) {
//
//        }
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getCategorie() {
        return categorie != null ? categorie : "";
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Double getDelaiLivraison() {
        return delaiLivraison != null ? delaiLivraison : 0.0;
    }

    public void setDelaiLivraison(Double delaiLivraison) {
        this.delaiLivraison = delaiLivraison;
    }

    public Double getDureeGarantie() {
        return dureeGarantie != null ? dureeGarantie : 0.0;
    }

    public void setDureeGarantie(Double dureeGarantie) {
        this.dureeGarantie = dureeGarantie;
    }

    public boolean isUseLotInDepot() {
        return useLotInDepot;
    }

    public void setUseLotInDepot(boolean useLotInDepot) {
        this.useLotInDepot = useLotInDepot;
    }

    public Boolean getRequiereLot() {
        return requiereLot != null ? requiereLot : false;
    }

    public void setRequiereLot(Boolean requiereLot) {
        this.requiereLot = requiereLot;
    }

    public double getQuantitePrec() {
        return quantitePrec;
    }

    public void setQuantitePrec(double quantitePrec) {
        this.quantitePrec = quantitePrec;
    }

    public double getQuantitePrevuPrec() {
        return quantitePrevuPrec;
    }

    public void setQuantitePrevuPrec(double quantitePrevuPrec) {
        this.quantitePrevuPrec = quantitePrevuPrec;
    }

    public double getQuantitePrevu() {
        return quantitePrevu;
    }

    public void setQuantitePrevu(double quantitePrevu) {
        this.quantitePrevu = quantitePrevu;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public Boolean getPuaTtc() {
        return puaTtc != null ? puaTtc : false;
    }

    public void setPuaTtc(Boolean puaTtc) {
        this.puaTtc = puaTtc;
    }

    public Boolean getPuvTtc() {
        return puvTtc != null ? puvTtc : false;
    }

    public void setPuvTtc(Boolean puvTtc) {
        this.puvTtc = puvTtc;
    }

    public String getNaturePrixMin() {
        return naturePrixMin != null ? (naturePrixMin.trim().length() > 0 ? naturePrixMin : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNaturePrixMin(String naturePrixMin) {
        this.naturePrixMin = naturePrixMin;
    }

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public String getFichier() {
        return fichier != null ? fichier : "";
    }

    public Character getTypeService() {
        return typeService != null ? (typeService != ' ' ? typeService : Constantes.SERVICE_COMMERCE_GENERAL) : Constantes.SERVICE_COMMERCE_GENERAL;
    }

    public void setTypeService(Character typeService) {
        this.typeService = typeService;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public Integer getLotFabrication() {
        return lotFabrication != null ? lotFabrication : 1;
    }

    public void setLotFabrication(Integer lotFabrication) {
        this.lotFabrication = lotFabrication;
    }

    public Boolean getDefNorme() {
        return defNorme != null ? defNorme : false;
    }

    public void setDefNorme(Boolean defNorme) {
        this.defNorme = defNorme;
    }

    public Boolean getNorme() {
        return norme != null ? norme : false;
    }

    public void setNorme(Boolean norme) {
        this.norme = norme;
    }

    public Double getPua() {
        return pua != null ? pua : 0;
    }

    public void setPua(Double pua) {
        this.pua = pua;
    }

    public Double getPuv() {
        return puv != null ? puv : 0;
    }

    public void setPuv(Double puv) {
        this.puv = puv;
    }

    public Double getRemise() {
        return remise != null ? remise : 0;
    }

    public void setRemise(Double remise) {
        this.remise = remise;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public boolean isError() {
        return error;
    }

    public boolean isSynchroniser() {
        return synchroniser;
    }

    public void setSynchroniser(boolean synchroniser) {
        this.synchroniser = synchroniser;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<YvsBaseArticlePoint> getArticlesPoints() {
        return articlesPoints;
    }

    public void setArticlesPoints(List<YvsBaseArticlePoint> articlesPoints) {
        this.articlesPoints = articlesPoints;
    }

    @XmlTransient
    @JsonIgnore
    public List<String> getPhotos() {
        photos.clear();
        if (photo1 != null ? photo1.trim().length() > 0 : false) {
            photos.add(photo1);
        }
        if (photo2 != null ? photo2.trim().length() > 0 : false) {
            photos.add(photo2);
        }
        if (photo3 != null ? photo3.trim().length() > 0 : false) {
            photos.add(photo3);
        }
        return photos;
    }

    @XmlTransient
    @JsonIgnore
    public List<String> getPhotosExtension() {
        photosExtension.clear();
        if (photo1 != null ? photo1.trim().length() > 0 : false) {
            photosExtension.add(getPhoto1Extension());
        }
        if (photo2 != null ? photo2.trim().length() > 0 : false) {
            photosExtension.add(getPhoto2Extension());
        }
        if (photo3 != null ? photo3.trim().length() > 0 : false) {
            photosExtension.add(getPhoto3Extension());
        }
        return photosExtension;
    }

    @XmlTransient
    @JsonIgnore
    public String getPhoto() {
        photo = null;
        getPhotos();
        if (photos != null ? !photos.isEmpty() : false) {
            photo = photos.get(0);
        }
        return photo;
    }

    public String getBytePhoto1() {
        return bytePhoto1;
    }

    public void setBytePhoto1(String bytePhoto1) {
        this.bytePhoto1 = bytePhoto1;
    }

    public String getBytePhoto2() {
        return bytePhoto2;
    }

    public void setBytePhoto2(String bytePhoto2) {
        this.bytePhoto2 = bytePhoto2;
    }

    public String getBytePhoto3() {
        return bytePhoto3;
    }

    public void setBytePhoto3(String bytePhoto3) {
        this.bytePhoto3 = bytePhoto3;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto1Extension() {
        return photo1Extension != null ? photo1Extension.trim().length() > 0 ? photo1Extension : "png" : "png";
    }

    public void setPhoto1Extension(String photo1Extension) {
        this.photo1Extension = photo1Extension;
    }

    public String getPhoto2Extension() {
        return photo2Extension != null ? photo2Extension.trim().length() > 0 ? photo2Extension : "png" : "png";
    }

    public void setPhoto2Extension(String photo2Extension) {
        this.photo2Extension = photo2Extension;
    }

    public String getPhoto3Extension() {
        return photo3Extension != null ? photo3Extension.trim().length() > 0 ? photo3Extension : "png" : "png";
    }

    public void setPhoto3Extension(String photo3Extension) {
        this.photo3Extension = photo3Extension;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBaseConditionnement getUnite() {
        if (conditionnements != null ? !conditionnements.isEmpty() : false) {
            if (conditionnements.size() == 1) {
                unite = conditionnements.get(0);
            } else {
                for (YvsBaseConditionnement c : conditionnements) {
                    if (c.getByVente() && c.getDefaut()) {
                        unite = c;
                        break;
                    }
                }
                if (unite != null ? unite.getId() < 1 : false) {
                    for (YvsBaseConditionnement c : conditionnements) {
                        if (c.getByVente()) {
                            unite = c;
                            break;
                        }
                    }
                }
            }
        }
        return unite;
    }

    public void setUnite(YvsBaseConditionnement unite) {
        this.unite = unite;
    }

    public YvsBaseClassesStat getClasse1() {
        return classe1;
    }

    public void setClasse1(YvsBaseClassesStat classe1) {
        this.classe1 = classe1;
    }

    public YvsBaseClassesStat getClasse2() {
        return classe2;
    }

    public void setClasse2(YvsBaseClassesStat classe2) {
        this.classe2 = classe2;
    }

    public YvsBaseUniteMesure getUniteDeVolume() {
        return uniteDeVolume;
    }

    public void setUniteDeVolume(YvsBaseUniteMesure uniteDeVolume) {
        this.uniteDeVolume = uniteDeVolume;
    }

    public YvsBaseTiers getFabriquant() {
        return fabriquant;
    }

    public void setFabriquant(YvsBaseTiers fabriquant) {
        this.fabriquant = fabriquant;
    }

    public YvsBaseGroupesArticle getGroupe() {
        return groupe;
    }

    public void setGroupe(YvsBaseGroupesArticle groupe) {
        this.groupe = groupe;
    }

    public YvsBaseFamilleArticle getFamille() {
        return famille;
    }

    public void setFamille(YvsBaseFamilleArticle famille) {
        this.famille = famille;
    }

    public YvsBaseUniteMesure getUniteStockage() {
        return uniteStockage;
    }

    public void setUniteStockage(YvsBaseUniteMesure uniteStockage) {
        this.uniteStockage = uniteStockage;
    }

    public YvsBaseUniteMesure getUniteVente() {
        return uniteVente;
    }

    public void setUniteVente(YvsBaseUniteMesure uniteVente) {
        this.uniteVente = uniteVente;
    }

    @Override
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseArticlesTemplate getTemplate() {
        return template;
    }

    public void setTemplate(YvsBaseArticlesTemplate template) {
        this.template = template;
    }

    public YvsBaseUniteMesure getUniteDeMasse() {
        return uniteDeMasse;
    }

    public void setUniteDeMasse(YvsBaseUniteMesure uniteDeMasse) {
        this.uniteDeMasse = uniteDeMasse;
    }

    public Double getTauxEcartPr() {
        return tauxEcartPr != null ? tauxEcartPr : 0;
    }

    public void setTauxEcartPr(Double tauxEcartPr) {
        this.tauxEcartPr = tauxEcartPr;
    }

    public Integer getOrdre() {
        return ordre != null ? ordre : 0;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    @XmlTransient
    @JsonIgnore

    public YvsBaseTarifPointLivraison getInfoLiv() {
        return infoLiv;
    }

    public void setInfoLiv(YvsBaseTarifPointLivraison infoLiv) {
        this.infoLiv = infoLiv;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticlesAvis> getAvis() {
        return avis;
    }

    public void setAvis(List<YvsBaseArticlesAvis> avis) {
        this.avis = avis;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseConditionnement> getConditionnements() {
        return conditionnements;
    }

    public void setConditionnements(List<YvsBaseConditionnement> conditionnements) {
        this.conditionnements = conditionnements;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBasePlanTarifaire> getPlans_tarifaires() {
        return plans_tarifaires;
    }

    public void setPlans_tarifaires(List<YvsBasePlanTarifaire> plans_tarifaires) {
        this.plans_tarifaires = plans_tarifaires;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticleFournisseur> getFournisseurs() {
        return fournisseurs;
    }

    public void setFournisseurs(List<YvsBaseArticleFournisseur> fournisseurs) {
        this.fournisseurs = fournisseurs;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticleEquivalent> getArticlesEquivalents() {
        return articlesEquivalents;
    }

    public void setArticlesEquivalents(List<YvsBaseArticleEquivalent> articlesEquivalents) {
        this.articlesEquivalents = articlesEquivalents;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticleEquivalent> getYvsProdArticleEquivalentList1() {
        return yvsProdArticleEquivalentList1;
    }

    public void setYvsProdArticleEquivalentList1(List<YvsBaseArticleEquivalent> yvsProdArticleEquivalentList1) {
        this.yvsProdArticleEquivalentList1 = yvsProdArticleEquivalentList1;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticleSubstitution> getArticlesSubstitutions() {
        return articlesSubstitutions;
    }

    public void setArticlesSubstitutions(List<YvsBaseArticleSubstitution> articlesSubstitutions) {
        this.articlesSubstitutions = articlesSubstitutions;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticleSubstitution> getYvsProdArticleSubstitutionList1() {
        return yvsProdArticleSubstitutionList1;
    }

    public void setYvsProdArticleSubstitutionList1(List<YvsBaseArticleSubstitution> yvsProdArticleSubstitutionList1) {
        this.yvsProdArticleSubstitutionList1 = yvsProdArticleSubstitutionList1;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticleDepot> getYvsBaseArticleDepotList() {
        return yvsBaseArticleDepotList;
    }

    public void setYvsBaseArticleDepotList(List<YvsBaseArticleDepot> yvsBaseArticleDepotList) {
        this.yvsBaseArticleDepotList = yvsBaseArticleDepotList;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticleAnalytique> getAnalytiques() {
        return analytiques;
    }

    public void setAnalytiques(List<YvsBaseArticleAnalytique> analytiques) {
        this.analytiques = analytiques;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticleCategorieComptable> getComptes() {
        return comptes;
    }

    public void setComptes(List<YvsBaseArticleCategorieComptable> comptes) {
        this.comptes = comptes;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsProdGammeArticle> getGammes() {
        return gammes;
    }

    public void setGammes(List<YvsProdGammeArticle> gammes) {
        this.gammes = gammes;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsProdNomenclature> getNomenclatures() {
        return nomenclatures;
    }

    public void setNomenclatures(List<YvsProdNomenclature> nomenclatures) {
        this.nomenclatures = nomenclatures;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsProdContenuConditionnement> getYvsProdContenuConditionnementList() {
        return yvsProdContenuConditionnementList;
    }

    public void setYvsProdContenuConditionnementList(List<YvsProdContenuConditionnement> yvsProdContenuConditionnementList) {
        this.yvsProdContenuConditionnementList = yvsProdContenuConditionnementList;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsProdNomenclature> getYvsProdNomenclatureList() {
        return yvsProdNomenclatureList;
    }

    public void setYvsProdNomenclatureList(List<YvsProdNomenclature> yvsProdNomenclatureList) {
        this.yvsProdNomenclatureList = yvsProdNomenclatureList;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticleDescription> getDescriptions() {
        return descriptions;
    }

    @XmlTransient
    @JsonIgnore
    public void setDescriptions(List<YvsBaseArticleDescription> descriptions) {
        this.descriptions = descriptions;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseTarifPointLivraison> getTarifs() {
        return tarifs;
    }

    public void setTarifs(List<YvsBaseTarifPointLivraison> tarifs) {
        this.tarifs = tarifs;
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
        if (!(object instanceof YvsBaseArticles)) {
            return false;
        }
        YvsBaseArticles other = (YvsBaseArticles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.produits.YvsArticles[ id=" + id + " ]";
    }

}
