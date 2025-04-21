/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.achat;

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
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.commercial.YvsComQualite;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.analytique.YvsComptaCentreContenuAchat;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.proj.projet.YvsProjProjetContenuDocAchat;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_contenu_doc_achat")
@NamedQueries({
    @NamedQuery(name = "YvsComContenuDocAchat.findAll", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.article.famille.societe = :societe"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByArticle", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.article = :article"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByDocAchat", query = "SELECT y FROM YvsComContenuDocAchat y JOIN FETCH y.article JOIN FETCH y.conditionnement JOIN FETCH y.conditionnement.unite WHERE y.docAchat = :docAchat"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByExterne", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.externe = :externe"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByExterneStatut", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.externe = :externe AND y.docAchat.statut =:statut"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByParent", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.parent = :parent"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByParentTypeStatut", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.parent = :parent AND y.docAchat.statut =:statut AND y.docAchat.typeDoc =:typeDoc"),
    @NamedQuery(name = "YvsComContenuDocAchat.findById", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByQuantiteAttendu", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.quantiteRecu = :quantiteAttendu"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByRemiseRecu", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.remise = :remiseRecu"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByPuaRecu", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.prixAchat = :puaRecu"),
    @NamedQuery(name = "YvsComContenuDocAchat.findBySupp", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByActif", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByQuantiteRecu", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.quantiteRecu = :quantiteRecu"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByCommentaire", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.commentaire = :commentaire"),
    
    @NamedQuery(name = "YvsComContenuDocAchat.countByLotParent", query = "SELECT COUNT(y.id) FROM YvsComContenuDocAchat y WHERE y.docAchat.typeDoc = :typeDoc AND y.docAchat.documentLie = :parent AND y.lot = :lot AND y.conditionnement = :conditionnement "),
    @NamedQuery(name = "YvsComContenuDocAchat.countByArticle", query = "SELECT COUNT(y.id) FROM YvsComContenuDocAchat y WHERE y.article = :article"),
    
    @NamedQuery(name = "YvsComContenuDocAchat.findLotByArticleParent", query = "SELECT DISTINCT y.lot FROM YvsComContenuDocAchat y WHERE y.docAchat.typeDoc = :typeDoc AND y.docAchat.documentLie = :parent AND y.conditionnement = :conditionnement "),

    @NamedQuery(name = "YvsComContenuDocAchat.findTaxeByFacture", query = "SELECT SUM(y.taxe) FROM YvsComContenuDocAchat y WHERE y.docAchat = :docAchat"),
    @NamedQuery(name = "YvsComContenuDocAchat.findTTCByFacture", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocAchat y WHERE y.docAchat = :docAchat"),
    @NamedQuery(name = "YvsComContenuDocAchat.findHTByFacture", query = "SELECT SUM(y.prixTotal - y.taxe) FROM YvsComContenuDocAchat y WHERE y.docAchat = :docAchat"),

    @NamedQuery(name = "YvsComContenuDocAchat.findTTCByFournisseurDate", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocAchat y WHERE y.docAchat.fournisseur = :fournisseur AND y.docAchat.typeDoc = :typeDoc AND y.docAchat.statut = 'V' AND y.docAchat.dateDoc <= :dateFin"),
    @NamedQuery(name = "YvsComContenuDocAchat.findTTCByFournisseurAgenceDate", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocAchat y WHERE y.docAchat.agence = :agence AND y.docAchat.fournisseur = :fournisseur AND y.docAchat.typeDoc = :typeDoc AND y.docAchat.statut = 'V' AND y.docAchat.dateDoc <= :dateFin"),
    @NamedQuery(name = "YvsComContenuDocAchat.findTTCByFournisseurDates", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocAchat y WHERE y.docAchat.fournisseur = :fournisseur AND y.docAchat.typeDoc = 'FA' AND y.docAchat.statut = 'V' AND y.docAchat.dateDoc BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComContenuDocAchat.findTTCByFournisseurAgenceDates", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocAchat y WHERE y.docAchat.agence = :agence AND y.docAchat.fournisseur = :fournisseur AND y.docAchat.typeDoc = 'FA' AND y.docAchat.statut = 'V' AND y.docAchat.dateDoc BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComContenuDocAchat.findTTCByFournisseur", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocAchat y WHERE y.docAchat.fournisseur = :fournisseur AND y.docAchat.typeDoc = 'FA' AND y.docAchat.statut = 'V'"),

    @NamedQuery(name = "YvsComContenuDocAchat.findQteVenduByArticle", query = "SELECT SUM(y.quantiteCommande)  FROM YvsComContenuDocAchat y WHERE y.conditionnement = :conditionnement AND y.docAchat.typeDoc = :type_doc"),
    @NamedQuery(name = "YvsComContenuDocAchat.findCAByArticle", query = "SELECT SUM(y.prixTotal)  FROM YvsComContenuDocAchat y WHERE y.article = :article AND y.docAchat.typeDoc = :type_doc AND y.docAchat.statut='V' "),
    @NamedQuery(name = "YvsComContenuDocAchat.findLastByArticle", query = "SELECT y  FROM YvsComContenuDocAchat y WHERE y.article = :article AND y.docAchat.typeDoc=:typeDoc ORDER BY y.docAchat.dateDoc DESC"),
    @NamedQuery(name = "YvsComContenuDocAchat.findNbreDocByArticle", query = "SELECT COUNT(DISTINCT(y.docAchat))  FROM YvsComContenuDocAchat y WHERE y.article = :article AND y.docAchat.typeDoc=:typeDoc AND y.docAchat.statut='V' "),
    @NamedQuery(name = "YvsComContenuDocAchat.findNbreDocByConditionnement", query = "SELECT COUNT(DISTINCT(y.docAchat))  FROM YvsComContenuDocAchat y WHERE y.conditionnement = :conditionnement"),

    @NamedQuery(name = "YvsComContenuDocAchat.findByTypeDocArticle", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.docAchat.typeDoc = :typeDoc AND y.article = :article"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByDocParentTypeDocArticle", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.docAchat.documentLie = :documentLie AND y.docAchat.typeDoc = :typeDoc AND y.article = :article"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByParentStatut", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.parent = :parent AND y.docAchat.statut =:statut"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByArticleDocAchat", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.article = :article AND y.docAchat = :docAchat"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByDocAchatQ", query = "SELECT SUM(y.quantiteRecu) FROM YvsComContenuDocAchat y WHERE y.docAchat = :docAchat"),

    @NamedQuery(name = "YvsComContenuDocAchat.findQteByDocParentTypeDocArticle", query = "SELECT SUM(y.quantiteRecu) FROM YvsComContenuDocAchat y WHERE y.docAchat.documentLie = :documentLie AND y.docAchat.typeDoc = :typeDoc AND y.article = :article AND y.conditionnement = :unite"),
    @NamedQuery(name = "YvsComContenuDocAchat.findPrixByDocParentTypeDocArticle", query = "SELECT AVG(y.prixAchat) FROM YvsComContenuDocAchat y WHERE y.docAchat.documentLie = :documentLie AND y.docAchat.typeDoc = :typeDoc AND y.article = :article AND y.conditionnement = :unite"),

    @NamedQuery(name = "YvsComContenuDocAchat.findByArticles", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.article = :article AND y.docAchat = :docAchat AND y.conditionnement = :unite"),
    @NamedQuery(name = "YvsComContenuDocAchat.findQteBonusByFacture", query = "SELECT SUM(y.quantiteBonus) FROM YvsComContenuDocAchat y WHERE y.article = :article AND y.conditionnement =:unite AND y.docAchat = :docAchat"),
    @NamedQuery(name = "YvsComContenuDocAchat.findQteBonusByArticle", query = "SELECT SUM(y.quantiteBonus) FROM YvsComContenuDocAchat y WHERE y.articleBonus = :article AND y.conditionnementBonus =:unite AND y.docAchat = :docAchat"),
    @NamedQuery(name = "YvsComContenuDocAchat.findQteByArticleFacture", query = "SELECT SUM(y.quantiteCommande) FROM YvsComContenuDocAchat y WHERE y.article = :article AND y.docAchat = :docAchat AND y.conditionnement = :unite"),
    @NamedQuery(name = "YvsComContenuDocAchat.findQteByArticle", query = "SELECT SUM(y.quantiteRecu) FROM YvsComContenuDocAchat y WHERE y.article = :article AND y.docAchat = :docAchat AND y.conditionnement = :unite"),
    @NamedQuery(name = "YvsComContenuDocAchat.findQteByArticles", query = "SELECT SUM(y.quantiteCommande) FROM YvsComContenuDocAchat y WHERE y.article = :article AND y.docAchat = :docAchat AND y.conditionnement = :unite"),
    @NamedQuery(name = "YvsComContenuDocAchat.findRemByArticle", query = "SELECT SUM(y.remise) FROM YvsComContenuDocAchat y WHERE y.article = :article AND y.docAchat = :docAchat AND y.conditionnement = :unite"),
    @NamedQuery(name = "YvsComContenuDocAchat.findPTByArticle", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocAchat y WHERE y.article = :article AND y.docAchat = :docAchat AND y.conditionnement = :unite"),

    @NamedQuery(name = "YvsComContenuDocAchat.findSumByUniteDocLieTypeStatut", query = "SELECT SUM(y.quantiteCommande) FROM YvsComContenuDocAchat y WHERE y.docAchat.documentLie = :facture AND y.conditionnement = :conditionnement AND y.docAchat.statut =:statut AND y.docAchat.typeDoc =:typeDoc"),

    @NamedQuery(name = "YvsComContenuDocAchat.findByParentTypeStatut", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.parent = :parent AND y.docAchat.statut =:statut AND y.docAchat.typeDoc =:typeDoc"),
    @NamedQuery(name = "YvsComContenuDocAchat.findQteByDocAchat", query = "SELECT SUM(y.quantiteRecu) FROM YvsComContenuDocAchat y WHERE y.docAchat = :docAchat"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByDocLierTypeStatutArticle", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.docAchat.documentLie = :docAchat AND y.docAchat.statut = :statut AND y.docAchat.typeDoc = :typeDoc AND y.article = :article"),
    @NamedQuery(name = "YvsComContenuDocAchat.findQteByTypeStatutParent", query = "SELECT SUM(y.quantiteRecu) FROM YvsComContenuDocAchat y WHERE y.parent = :parent AND y.docAchat.statut = :statut AND y.docAchat.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByDocLierTypeStatutArticleS", query = "SELECT SUM(y.quantiteRecu) FROM YvsComContenuDocAchat y WHERE y.docAchat.documentLie = :docAchat AND y.docAchat.statut = :statut AND y.docAchat.typeDoc = :typeDoc AND y.article = :article AND y.conditionnement = :unite"),
    @NamedQuery(name = "YvsComContenuDocAchat.findFsseurByArticle", query = "SELECT y.docAchat.fournisseur FROM YvsComContenuDocAchat y WHERE y.article = :article ORDER BY y.docAchat.dateDoc DESC"),

    @NamedQuery(name = "YvsComContenuDocAchat.findByParentTypeStatutArticle", query = "SELECT y FROM YvsComContenuDocAchat y WHERE y.parent = :parent AND y.docAchat.statut = :statut AND y.docAchat.typeDoc = :typeDoc AND y.statut=:statut"),
    @NamedQuery(name = "YvsComContenuDocAchat.findByParentTypeStatutArticleS", query = "SELECT SUM(y.quantiteCommande) FROM YvsComContenuDocAchat y WHERE y.parent = :parent AND y.docAchat.statut = :statut AND y.docAchat.typeDoc = :typeDoc")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsComContenuDocAchat extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_contenu_doc_achat_id_seq", name = "yvs_com_contenu_doc_achat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_contenu_doc_achat_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "quantite_recu")
    private Double quantiteRecu;
    @Column(name = "quantite_attendu")
    private Double quantiteCommande;
    @Column(name = "pua_recu")
    private double prixAchat;
    @Column(name = "remise_recu")
    private double remise;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "quantite_bonus")
    private Double quantiteBonus;
    @Column(name = "taxe")
    private double taxe;
    @Size(max = 2147483647)
    @Column(name = "commentaire")
    private String commentaire;
    @Column(name = "date_livraison")
    @Temporal(TemporalType.DATE)
    private Date dateLivraison;
    @Size(max = 2147483647)
    @Column(name = "statut")
    private String statut;
    @Column(name = "statut_livree")
    private String statutLivree;
    @Column(name = "num_serie")
    private String numSerie;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_contenu")
    @Temporal(TemporalType.DATE)
    private Date dateContenu;
    @Column(name = "prix_total")
    private Double prixTotal;
    @Column(name = "calcul_pr")
    private Boolean calculPr;

    @JsonManagedReference
    @JoinColumn(name = "doc_achat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocAchats docAchat;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "article_bonus", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles articleBonus;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComContenuDocAchat parent;
    @JoinColumn(name = "externe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComArticleApprovisionnement externe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "lot", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComLotReception lot;
    @JoinColumn(name = "qualite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComQualite qualite;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "conditionnement_bonus", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnementBonus;

    @OneToMany(mappedBy = "contenu")
    private List<YvsComTaxeContenuAchat> taxes;
    @OneToMany(mappedBy = "parent")
    private List<YvsComContenuDocAchat> contenus;
    @OneToMany(mappedBy = "contenu")
    private List<YvsComptaCentreContenuAchat> analytiques;

    @Transient
    private List<YvsComLotReception> lots;
    @Transient
    private List<YvsProjProjetContenuDocAchat> projets;
    @Transient
    private YvsBasePlanComptable compte = new YvsBasePlanComptable();
    @Transient
    private long idDistant;
    @Transient
    private long save;
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean requiereLot;
    @Transient
    private boolean update;
    @Transient
    private boolean errorPua;
    @Transient
    private boolean errorRemise;
    @Transient
    private boolean errorComptabilise;
    @Transient
    private double tauxRemise;
    @Transient
    private boolean errorQte;
    @Transient
    private double qteRestant;
    @Transient
    private double qteLivree;
    @Transient
    private double qteAttente;
    @Transient
    private double taxeRecu;
    @Transient
    private boolean onComment;
    @Transient
    private boolean retardLivraison;
    @Transient
    private boolean bonus;
    @Transient
    private String messageError;

    public YvsComContenuDocAchat() {
        taxes = new ArrayList<>();
        contenus = new ArrayList<>();
        analytiques = new ArrayList<>();
        lots = new ArrayList<>();
    }

    public YvsComContenuDocAchat(Long id) {
        this();
        this.id = id;
    }

    public YvsComContenuDocAchat(Long id, YvsUsersAgence author) {
        this(id);
        this.author = author;
    }

    public YvsComContenuDocAchat(YvsComContenuDocAchat y) {
        this(y.getId(), y);
    }

    public YvsComContenuDocAchat(Long id, YvsComContenuDocAchat y) {
        this();
        this.id = id;
        this.dateUpdate = y.getDateUpdate();
        this.quantiteRecu = y.getQuantiteRecu();
        this.prixAchat = y.getPrixAchat();
        this.remise = y.getRemise();
        this.supp = y.getSupp();
        this.actif = y.getActif();
        this.quantiteCommande = y.getQuantiteCommande();
        this.taxe = y.getTaxe();
        this.commentaire = y.getCommentaire();
        this.dateLivraison = y.getDateLivraison();
        this.statut = y.getStatut();
        this.statutLivree = y.getStatutLivree();
        this.numSerie = y.getNumSerie();
        this.dateSave = y.getDateSave();
        this.dateContenu = y.getDateContenu();
        this.prixTotal = y.getPrixTotal();
        this.docAchat = y.getDocAchat();
        this.article = y.getArticle();
        this.parent = y.getParent();
        this.externe = y.getExterne();
        this.author = y.getAuthor();
        this.lot = y.getLot();
        this.qualite = y.getQualite();
        this.conditionnement = y.getConditionnement();
        this.taxes = y.getTaxes();
        this.contenus = y.getContenus();
        this.new_ = y.isNew_();
        this.selectActif = y.isSelectActif();
        this.update = y.isUpdate();
        this.errorPua = y.isErrorPua();
        this.errorRemise = y.isErrorRemise();
        this.tauxRemise = y.getTauxRemise();
        this.errorQte = y.isErrorQte();
        this.qteRestant = y.getQteRestant();
        this.qteLivree = y.getQteLivree();
        this.qteAttente = y.getQteAttente();
        this.taxeRecu = y.getTaxeRecu();
        this.onComment = y.isOnComment();
        this.save = y.getSave();
        this.bonus = y.isBonus();
        this.calculPr = y.getCalculPr();
        this.retardLivraison = y.isRetardLivraison();
        this.quantiteBonus = y.getQuantiteBonus();
        this.articleBonus = y.getArticleBonus();
        this.conditionnementBonus = y.getConditionnementBonus();
        this.idDistant = y.getIdDistant();
    }

    public long getIdDistant() {
        return idDistant;
    }

    public void setIdDistant(long idDistant) {
        this.idDistant = idDistant;
    }

    public long getSave() {
        return save;
    }

    public void setSave(long save) {
        this.save = save;
    }

    public Boolean getCalculPr() {
        return calculPr != null ? calculPr : true;
    }

    public void setCalculPr(Boolean calculPr) {
        this.calculPr = calculPr;
    }

    public boolean isBonus() {
        return bonus;
    }

    public void setBonus(boolean bonus) {
        this.bonus = bonus;
    }

    public Double getQuantiteBonus() {
        return quantiteBonus != null ? quantiteBonus : 0;
    }

    public void setQuantiteBonus(Double quantiteBonus) {
        this.quantiteBonus = quantiteBonus;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBaseArticles getArticleBonus() {
        return articleBonus;
    }

    public void setArticleBonus(YvsBaseArticles articleBonus) {
        this.articleBonus = articleBonus;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBaseConditionnement getConditionnementBonus() {
        return conditionnementBonus;
    }

    public void setConditionnementBonus(YvsBaseConditionnement conditionnementBonus) {
        this.conditionnementBonus = conditionnementBonus;
    }

    public double getQteLivree() {
        return qteLivree;
    }

    public void setQteLivree(double qteLivree) {
        this.qteLivree = qteLivree;
    }

    public String getStatutLivree() {
        return statutLivree != null ? statutLivree.trim().length() > 0 ? statutLivree : "W" : "W";
    }

    public void setStatutLivree(String statutLivree) {
        this.statutLivree = statutLivree;
    }

    public double getTaxeRecu() {
        return taxeRecu;
    }

    public void setTaxeRecu(double taxeRecu) {
        this.taxeRecu = taxeRecu;
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

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComContenuDocAchat> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComContenuDocAchat> contenus) {
        this.contenus = contenus;
    }

    public boolean isRetardLivraison() {
        retardLivraison = false;
        if (docAchat != null && (article != null ? !article.getCategorie().equals(Constantes.CAT_SERVICE) : false)) {
            if (!docAchat.getStatutLivre().equals("L") && getStatut().equals("V")) {
                Calendar now = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                if (dateLivraison != null) {
                    c.setTime(dateLivraison);
                }
                retardLivraison = c.before(now);
            }
        }
        return retardLivraison;
    }

    public void setRetardLivraison(boolean retardLivraison) {
        this.retardLivraison = retardLivraison;
    }

    public boolean isOnComment() {
        onComment = false;
        if (commentaire != null ? commentaire.trim().length() > 0 : false) {
            onComment = true;
        }
        return onComment;
    }

    public void setOnComment(boolean onComment) {
        this.onComment = onComment;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComQualite getQualite() {
        return qualite;
    }

    public void setQualite(YvsComQualite qualite) {
        this.qualite = qualite;
    }

    @XmlTransient
    @JsonIgnore
    public double getTaxe() {
        taxe = 0;
        for (YvsComTaxeContenuAchat t : taxes) {
            taxe += t.getMontant();
        }
        return taxe;
    }

    public void setTaxe(double taxe) {
        this.taxe = taxe;
    }

    @XmlTransient
    @JsonIgnore
    public double getTauxRemise() {
        tauxRemise = ((getRemise() * 100) / (getQuantiteRecu() * (getPrixAchat())));
        return tauxRemise;
    }

    public void setTauxRemise(double tauxRemise) {
        this.tauxRemise = tauxRemise;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public boolean isErrorPua() {
        return errorPua;
    }

    public void setErrorPua(boolean errorPua) {
        this.errorPua = errorPua;
    }

    public boolean isErrorRemise() {
        return errorRemise;
    }

    public void setErrorRemise(boolean errorRemise) {
        this.errorRemise = errorRemise;
    }

    public boolean isErrorQte() {
        return errorQte;
    }

    public void setErrorQte(boolean errorQte) {
        this.errorQte = errorQte;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComArticleApprovisionnement getExterne() {
        return externe;
    }

    public void setExterne(YvsComArticleApprovisionnement externe) {
        this.externe = externe;
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

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public Double getPrixTotal() {
        return prixTotal != null ? prixTotal : 0;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public double getQteRestant() {
        return qteRestant;
    }

    public void setQteRestant(double qteRestant) {
        this.qteRestant = qteRestant;
    }

    public double getQteAttente() {
        return qteAttente;
    }

    public void setQteAttente(double qteAttente) {
        this.qteAttente = qteAttente;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComContenuDocAchat getParent() {
        return parent;
    }

    public void setParent(YvsComContenuDocAchat parent) {
        this.parent = parent;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateLivraison() {
        return dateLivraison;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getStatut() {
        return statut != null ? statut.trim().length() > 0 ? statut : Constantes.ETAT_EDITABLE : Constantes.ETAT_EDITABLE;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantiteRecu() {
        return quantiteRecu != null ? quantiteRecu : 0;
    }

    public void setQuantiteRecu(Double quantiteRecu) {
        this.quantiteRecu = quantiteRecu;
    }

    public double getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(double prixAchat) {
        this.prixAchat = prixAchat;
    }

    public boolean isErrorComptabilise() {
        return errorComptabilise;
    }

    public void setErrorComptabilise(boolean errorComptabilise) {
        this.errorComptabilise = errorComptabilise;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public Boolean getSupp() {
        return supp != null ? supp : false;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Double getQuantiteCommande() {
        return quantiteCommande != null ? quantiteCommande != 0 ? quantiteCommande : getQuantiteRecu() : getQuantiteRecu();
    }

    public void setQuantiteCommande(double quantiteCommande) {
        this.quantiteCommande = quantiteCommande;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public YvsComDocAchats getDocAchat() {
        return docAchat;
    }

    public void setDocAchat(YvsComDocAchats docAchat) {
        this.docAchat = docAchat;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public boolean isRequiereLot() {
        return requiereLot;
    }

    public void setRequiereLot(boolean requiereLot) {
        this.requiereLot = requiereLot;
    }

    public List<YvsComTaxeContenuAchat> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<YvsComTaxeContenuAchat> taxes) {
        this.taxes = taxes;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComLotReception getLot() {
        return lot;
    }

    public void setLot(YvsComLotReception lot) {
        this.lot = lot;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateContenu() {
        return dateContenu;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateContenu(Date dateContenu) {
        this.dateContenu = dateContenu;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComLotReception> getLots() {
        return lots;
    }

    public void setLots(List<YvsComLotReception> lots) {
        this.lots = lots;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCentreContenuAchat> getAnalytiques() {
        return analytiques;
    }

    public void setAnalytiques(List<YvsComptaCentreContenuAchat> analytiques) {
        this.analytiques = analytiques;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsProjProjetContenuDocAchat> getProjets() {
        return projets;
    }

    public void setProjets(List<YvsProjProjetContenuDocAchat> projets) {
        this.projets = projets;
    }

    public YvsBasePlanComptable getCompte() {
        return compte;
    }

    public void setCompte(YvsBasePlanComptable compte) {
        this.compte = compte;
    }

    public boolean isHasMessageError() {
        return messageError != null ? !messageError.trim().equals("") : false;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
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
        if (!(object instanceof YvsComContenuDocAchat)) {
            return false;
        }
        YvsComContenuDocAchat other = (YvsComContenuDocAchat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.achat.YvsComContenuDocAchat[ id=" + id + " ]";
    }

}
