/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.vente;

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
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import yvs.annotation.Descriptor;
import yvs.dao.YvsEntity;
import yvs.dao.services.DocVenteContenus;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.YvsComQualite;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.commercial.stock.YvsComReservationStock;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_contenu_doc_vente")
@Descriptor(name = "Contenu Facture de vente")
@NamedQueries({
    @NamedQuery(name = "YvsComContenuDocVente.findAll", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.docVente.enteteDoc.creneau.creneauPoint.point.agence.societe = :societe"),
    @NamedQuery(name = "YvsComContenuDocVente.findById", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComContenuDocVente.findByQuantite", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.quantite = :quantite"),
    @NamedQuery(name = "YvsComContenuDocVente.findByPrix", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.prix = :prix"),
    @NamedQuery(name = "YvsComContenuDocVente.findByRistourne", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.ristourne = :ristourne"),
    @NamedQuery(name = "YvsComContenuDocVente.findByComission", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.comission = :comission"),
    @NamedQuery(name = "YvsComContenuDocVente.findBySupp", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsComContenuDocVente.findByActif", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsComContenuDocVente.findByContentBlFacture", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.docVente.documentLie = :docVente AND y.docVente.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComContenuDocVente.findByFacture", query = "SELECT DISTINCT y FROM YvsComContenuDocVente y JOIN FETCH y.conditionnement JOIN FETCH y.article JOIN FETCH y.conditionnement.unite "
            + "LEFT JOIN FETCH y.taxes T JOIN FETCH T.taxe "
            + "WHERE y.docVente = :docVente ORDER BY y.id ASC"),

    @NamedQuery(name = "YvsComContenuDocVente.countByLotParent", query = "SELECT COUNT(y.id) FROM YvsComContenuDocVente y WHERE y.docVente.typeDoc = :typeDoc AND y.docVente.documentLie = :parent AND y.lot = :lot AND y.conditionnement = :conditionnement "),
    @NamedQuery(name = "YvsComContenuDocVente.countByArticle", query = "SELECT COUNT(y.id) FROM YvsComContenuDocVente y WHERE y.article = :article"),

    @NamedQuery(name = "YvsComContenuDocVente.findLotByArticleParent", query = "SELECT DISTINCT y.lot FROM YvsComContenuDocVente y WHERE y.docVente.typeDoc = :typeDoc AND y.docVente.documentLie = :parent AND y.conditionnement = :conditionnement "),

    @NamedQuery(name = "YvsComContenuDocVente.findTTCByFacture", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente = :docVente"),
    @NamedQuery(name = "YvsComContenuDocVente.findTTCByFactures", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.id IN :docVente"),

    @NamedQuery(name = "YvsComContenuDocVente.findTTCByDates", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.enteteDoc.creneau.creneauPoint.point.agence.societe = :societe AND y.docVente.typeDoc = :type AND y.docVente.statut = :statut AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComContenuDocVente.findTTCByAgenceDates", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.enteteDoc.creneau.creneauPoint.point.agence = :agence AND y.docVente.typeDoc = :type AND y.docVente.statut = :statut AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),

    @NamedQuery(name = "YvsComContenuDocVente.findFactureByDatesLivre", query = "SELECT DISTINCT(y.docVente.id) FROM YvsComContenuDocVente y WHERE y.docVente.enteteDoc.creneau.creneauPoint.point.agence.societe = :societe AND y.docVente.typeDoc = :type AND y.docVente.statut = :statut AND y.docVente.dateLivraison BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComContenuDocVente.findTTCByDatesLivre", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.enteteDoc.creneau.creneauPoint.point.agence.societe = :societe AND y.docVente.typeDoc = :type AND y.docVente.statut = :statut AND y.docVente.dateLivraison BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComContenuDocVente.findTTCByAgenceDatesLivre", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.enteteDoc.creneau.creneauPoint.point.agence = :agence AND y.docVente.typeDoc = :type AND y.docVente.statut = :statut AND y.docVente.dateLivraison BETWEEN :dateDebut AND :dateFin"),

    @NamedQuery(name = "YvsComContenuDocVente.findTTCByClientDate", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.client = :client AND y.docVente.typeDoc = 'FV' AND y.docVente.statut = 'V' AND y.docVente.enteteDoc.dateEntete <= :dateFin"),
    @NamedQuery(name = "YvsComContenuDocVente.findTTCByClientDates", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.client = :client AND y.docVente.typeDoc = 'FV' AND y.docVente.statut = 'V' AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComContenuDocVente.findTTCByClientDatesAgence", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.enteteDoc.agence = :agence AND y.docVente.client = :client AND y.docVente.typeDoc = 'FV' AND y.docVente.statut = 'V' AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComContenuDocVente.findTTCByClientDatesArt", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.client = :client AND y.docVente.typeDoc = 'FV' AND y.docVente.statut = 'V' AND y.article=:article AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComContenuDocVente.findTTCByClient", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.client = :client AND y.docVente.typeDoc = 'FV' AND y.docVente.statut = 'V'"),

    @NamedQuery(name = "YvsComContenuDocVente.findTTCAvoirByClientDatesAgence", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.enteteDoc.agence = :agence AND y.docVente.client = :client AND y.docVente.documentLie.typeDoc = 'FV' AND y.docVente.typeDoc = 'FAV' AND y.docVente.statut = 'V' AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComContenuDocVente.findTTCAvoirByClientDates", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.client = :client AND y.docVente.documentLie.typeDoc = 'FV' AND y.docVente.typeDoc = 'FAV' AND y.docVente.statut = 'V' AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComContenuDocVente.findTTCAvoirByClient", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.client = :client AND y.docVente.documentLie.typeDoc = 'FV' AND y.docVente.typeDoc = 'FAV' AND y.docVente.statut = 'V'"),

    @NamedQuery(name = "YvsComContenuDocVente.findTTCByVendeurDates", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.enteteDoc.creneau.users = :vendeur AND y.docVente.typeDoc = 'FV' AND y.docVente.statut = 'V' AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComContenuDocVente.findTTCByVendeurDatesArt", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.enteteDoc.creneau.users = :vendeur AND y.docVente.typeDoc = 'FV' AND y.docVente.statut = 'V' AND y.article=:article AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComContenuDocVente.findTTCByVendeur", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.enteteDoc.creneau.users = :vendeur AND y.docVente.typeDoc = 'FV' AND y.docVente.statut = 'V'"),

    @NamedQuery(name = "YvsComContenuDocVente.findSumByClientDates", query = "SELECT SUM(y.prixTotal), SUM(y.quantite) FROM YvsComContenuDocVente y WHERE y.docVente.client = :client AND y.docVente.typeDoc = 'FV' AND y.docVente.statut = 'V' AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComContenuDocVente.findSumByClientDatesArt", query = "SELECT SUM(y.prixTotal), SUM(y.quantite) FROM YvsComContenuDocVente y WHERE y.docVente.client = :client AND y.docVente.typeDoc = 'FV' AND y.docVente.statut = 'V' AND y.article=:article AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComContenuDocVente.findSumByClient", query = "SELECT SUM(y.prixTotal), SUM(y.quantite) FROM YvsComContenuDocVente y WHERE y.docVente.client = :client AND y.docVente.typeDoc = 'FV' AND y.docVente.statut = 'V'"),

    @NamedQuery(name = "YvsComContenuDocVente.findTotalByTypeDocAndHeader", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.enteteDoc = :header AND y.docVente.typeDoc = :typeDoc AND y.docVente.statut = 'V'"),
    @NamedQuery(name = "YvsComContenuDocVente.findTotalByTypeDocAndLivre", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.enteteDoc = :header AND y.docVente.typeDoc = :typeDoc AND y.docVente.statut = 'V' AND y.docVente.statutLivre = :statutL"),
    @NamedQuery(name = "YvsComContenuDocVente.findTotalByTypeDocAndRegle", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.enteteDoc = :header AND y.docVente.typeDoc = :typeDoc AND y.docVente.statut = 'V' AND y.docVente.statutRegle = :statutR"),

    @NamedQuery(name = "YvsComContenuDocVente.findByArticles", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.article = :article AND y.conditionnement =:unite AND y.docVente = :docVente"),

    @NamedQuery(name = "YvsComContenuDocVente.findMargeByArticle", query = "SELECT SUM(y.prixTotal-(y.pr * y.quantite))  FROM YvsComContenuDocVente y WHERE y.article = :article AND y.docVente.typeDoc='FV' AND y.docVente.statut='V' "),
    @NamedQuery(name = "YvsComContenuDocVente.findQteVenduByArticle", query = "SELECT SUM(y.quantite)  FROM YvsComContenuDocVente y WHERE y.conditionnement = :conditionnement  AND y.docVente.typeDoc = :type_doc"),
    @NamedQuery(name = "YvsComContenuDocVente.findLastByArticle", query = "SELECT y  FROM YvsComContenuDocVente y WHERE y.article = :article AND y.docVente.typeDoc=:typeDoc ORDER BY y.docVente.enteteDoc.dateEntete DESC"),
    @NamedQuery(name = "YvsComContenuDocVente.findNbreDocByArticle", query = "SELECT COUNT(DISTINCT(y.docVente))  FROM YvsComContenuDocVente y WHERE y.article = :article AND y.docVente.typeDoc=:typeDoc AND y.docVente.statut='V' "),
    @NamedQuery(name = "YvsComContenuDocVente.findNbreDocByConditionnement", query = "SELECT COUNT(DISTINCT(y.docVente))  FROM YvsComContenuDocVente y WHERE y.conditionnement = :conditionnement"),

    @NamedQuery(name = "YvsComContenuDocVente.findCAByArticle", query = "SELECT SUM(y.prixTotal)  FROM YvsComContenuDocVente y WHERE y.article = :article AND y.docVente.typeDoc = :type_doc AND y.docVente.statut='V' "),
    @NamedQuery(name = "YvsComContenuDocVente.findQteBonusByFacture", query = "SELECT SUM(y.quantiteBonus) FROM YvsComContenuDocVente y WHERE y.article = :article AND y.conditionnement =:unite AND y.docVente = :docVente"),
    @NamedQuery(name = "YvsComContenuDocVente.findQteBonusByArticle", query = "SELECT SUM(y.quantiteBonus) FROM YvsComContenuDocVente y WHERE y.articleBonus = :article AND y.conditionnementBonus =:unite AND y.docVente = :docVente"),
    @NamedQuery(name = "YvsComContenuDocVente.findQteByArticle", query = "SELECT SUM(y.quantite) FROM YvsComContenuDocVente y WHERE y.article = :article AND y.conditionnement =:unite AND y.docVente = :docVente"),
    @NamedQuery(name = "YvsComContenuDocVente.findRemByArticle", query = "SELECT SUM(y.remise) FROM YvsComContenuDocVente y WHERE y.article = :article AND y.conditionnement =:unite AND y.docVente = :docVente"),
    @NamedQuery(name = "YvsComContenuDocVente.findPTByArticle", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.article = :article AND y.conditionnement =:unite AND y.docVente = :docVente"),

    @NamedQuery(name = "YvsComContenuDocVente.findArticleByClientCategorie", query = "SELECT DISTINCT(y.article.id), y.article.refArt, y.article.designation FROM YvsComContenuDocVente y WHERE (y.article.categorie = :categorie OR y.article.categorie=:categorie1) AND y.docVente.client =:client ORDER BY y.article.refArt"),
    @NamedQuery(name = "YvsComContenuDocVente.findArticleByIdClient", query = "SELECT DISTINCT(y.article.id), y.article.refArt, y.article.designation FROM YvsComContenuDocVente y WHERE y.article.id IN :ids AND y.docVente.client =:client ORDER BY y.article.refArt"),

    @NamedQuery(name = "YvsComContenuDocVente.findByClientTypeStatut", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.docVente.client = :client AND y.docVente.statut =:statut AND y.docVente.typeDoc =:typeDoc"),
    @NamedQuery(name = "YvsComContenuDocVente.findByNumeroFacture", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.docVente.numDoc = :numDoc AND y.docVente.enteteDoc.creneau.users.agence.societe = :societe"),
    @NamedQuery(name = "YvsComContenuDocVente.findByParentTypeStatut", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.parent = :parent AND y.docVente.statut =:statut AND y.docVente.typeDoc =:typeDoc"),
    @NamedQuery(name = "YvsComContenuDocVente.findSumByParentTypeStatut", query = "SELECT SUM(y.quantite) FROM YvsComContenuDocVente y WHERE y.parent = :parent AND y.docVente.statut =:statut AND y.docVente.typeDoc =:typeDoc"),
    @NamedQuery(name = "YvsComContenuDocVente.findSumByUniteDocLieTypeStatut", query = "SELECT SUM(y.quantite) FROM YvsComContenuDocVente y WHERE y.docVente.documentLie = :facture AND y.conditionnement = :conditionnement AND y.docVente.statut =:statut AND y.docVente.typeDoc =:typeDoc"),

    @NamedQuery(name = "YvsComContenuDocVente.findByVenteArticle", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.docVente = :docVente AND y.article = :article ORDER BY y.prix DESC"),
    @NamedQuery(name = "YvsComContenuDocVente.findByVenteArticleNoId", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.docVente = :docVente AND y.article = :article AND y.id != :id"),
    @NamedQuery(name = "YvsComContenuDocVente.findByVenteUniteNoId", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.docVente = :docVente AND y.conditionnement = :conditionnement AND y.id != :id"),
    @NamedQuery(name = "YvsComContenuDocVente.findByDocVente", query = "SELECT y FROM YvsComContenuDocVente y JOIN FETCH y.docVente JOIN FETCH y.article JOIN FETCH y.conditionnement JOIN FETCH y.conditionnement.unite WHERE y.docVente = :docVente ORDER BY y.id ASC"),
    @NamedQuery(name = "YvsComContenuDocVente.findByDocVenteCanSend", query = "SELECT y FROM YvsComContenuDocVente y JOIN FETCH y.docVente JOIN FETCH y.article JOIN FETCH y.conditionnement JOIN FETCH y.conditionnement.unite WHERE y.docVente = :docVente AND y.article.suiviEnStock = TRUE ORDER BY y.id ASC"),
    @NamedQuery(name = "YvsComContenuDocVente.findQteByDocVente", query = "SELECT SUM(y.quantite) FROM YvsComContenuDocVente y WHERE y.docVente = :docVente"),
    @NamedQuery(name = "YvsComContenuDocVente.findQteByDocLivre", query = "SELECT SUM(y.quantite) FROM YvsComContenuDocVente y WHERE y.docVente.documentLie = :facture AND y.docVente.typeDoc=:typeDoc AND y.article=:article AND y.docVente.statut=:statut AND y.conditionnement = :unite"),
    @NamedQuery(name = "YvsComContenuDocVente.findByDocLierTypeStatutArticle", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.docVente.documentLie = :docVente AND y.docVente.statut = :statut AND y.docVente.typeDoc = :typeDoc AND y.article = :article AND y.statut=:statut AND y.conditionnement = :unite"),
    @NamedQuery(name = "YvsComContenuDocVente.findByDocLierTypeStatutArticleS", query = "SELECT SUM(y.quantite) FROM YvsComContenuDocVente y WHERE y.docVente.documentLie = :docVente AND y.docVente.statut = :statut AND y.docVente.typeDoc = :typeDoc AND y.article = :article AND y.conditionnement = :unite"),
    @NamedQuery(name = "YvsComContenuDocVente.findQteByTypeStatutParent", query = "SELECT SUM(y.quantite) FROM YvsComContenuDocVente y WHERE y.parent = :parent AND y.docVente.statut = :statut AND y.docVente.typeDoc = :typeDoc"),

    @NamedQuery(name = "YvsComContenuDocVente.findByParentTypeStatutArticle", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.parent = :parent AND y.docVente.statut = :statut AND y.docVente.typeDoc = :typeDoc AND y.statut=:statut"),
    @NamedQuery(name = "YvsComContenuDocVente.findByParentTypeStatutArticleS", query = "SELECT SUM(y.quantite) FROM YvsComContenuDocVente y WHERE y.parent = :parent AND y.docVente.statut = :statut AND y.docVente.typeDoc = :typeDoc"),

//    @NamedQuery(name = "YvsComContenuDocVente.findArticleRated", query = "SELECT y.article FROM YvsComContenuDocVente y GROUP BY y.article ORDER BY (SELECT COUNT(y1.id) FROM YvsComContenuDocVente y1 WHERE y1.article = y.article)"),
    @NamedQuery(name = "YvsComContenuDocVente.findQteByArticle_", query = "SELECT SUM(y.quantite) FROM YvsComContenuDocVente y WHERE y.article = :article AND y.conditionnement = :unite AND y.conditionnementBonus = :unite AND y.docVente = :docVente")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsComContenuDocVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_contenu_doc_vente_id_seq", name = "yvs_contenu_doc_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_contenu_doc_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "ristourne")
    private Double ristourne;
    @Column(name = "comission")
    private Double comission;
    @Column(name = "pr")
    private Double pr;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "mouv_stock")
    private Boolean mouvStock;
    @Column(name = "date_contenu")
    @Temporal(TemporalType.DATE)
    private Date dateContenu;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "commentaire")
    private String commentaire;
    @Column(name = "num_serie")
    private String numSerie;
    @Column(name = "statut")
    private String statut;
    @Column(name = "puv_min")
    private Double puvMin;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "prix")
    private Double prix;
    @Column(name = "remise")
    private Double remise;
    @Column(name = "rabais")
    private Double rabais;
    @Column(name = "quantite_bonus")
    private Double quantiteBonus;
    @Column(name = "prix_total")
    private Double prixTotal;
    @Column(name = "statut_livree")
    private Character statutLivree;
    @Column(name = "calcul_pr")
    private Boolean calculPr;
    @Column(name = "execute_trigger")
    private String executeTrigger;

    @JoinColumn(name = "lot", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComLotReception lot;
    @JoinColumn(name = "depot_livraison_prevu", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depoLivraisonPrevu;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "doc_vente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @XmlJavaTypeAdapter(DocVenteContenus.class)
    @JsonManagedReference
    private YvsComDocVentes docVente;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "article_bonus", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles articleBonus;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComContenuDocVente parent;
    @JoinColumn(name = "id_reservation", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComReservationStock idReservation;
    @JoinColumn(name = "qualite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComQualite qualite;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "conditionnement_bonus", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnementBonus;

    @OneToMany(mappedBy = "contenu", fetch = FetchType.LAZY)
    private List<YvsComContenuDocVenteEtat> etats;
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<YvsComContenuDocVente> contenus;
    @OneToMany(mappedBy = "contenu", fetch = FetchType.LAZY)
    private List<YvsComTaxeContenuVente> taxes;

    @Transient
    private List<YvsComLotReception> lots;
    @Transient
    private YvsBasePlanComptable compte = new YvsBasePlanComptable();
    @Transient
    private long save;
    @Transient
    private double taxe;
    @Transient
    private double qteRestant;
    @Transient
    private boolean requiereLot;
    @Transient
    private double qteLivree;
    @Transient
    private double qteAttente;
    @Transient
    private double tauxRemise;
    @Transient
    private double quantite_;
    @Transient
    private double prix_;
    @Transient
    private double remise_;
    @Transient
    private double taxe_;
    @Transient
    private boolean onComment;
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean errorComptabilise;
    @Transient
    private boolean update;
    @Transient
    private boolean bonus;
    @Transient
    private String messageError;

    public YvsComContenuDocVente() {
        super();
        contenus = new ArrayList<>();
        taxes = new ArrayList<>();
        lots = new ArrayList<>();
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComContenuDocVente(boolean initDoc) {
        this();
        if (initDoc) {
            this.setDocVente(new YvsComDocVentes());
        }
    }

    public YvsComContenuDocVente(Long id) {
        this();
        this.id = id;
    }

    public YvsComContenuDocVente(Long id, YvsUsersAgence author) {
        this(id);
        this.author = author;
    }

    public YvsComContenuDocVente(YvsComContenuDocVente y) {
        this(y.id, y);
    }

    public YvsComContenuDocVente(Long id, YvsComContenuDocVente y) {
        this.id = id;
        this.dateSave = y.getDateSave();
        this.save = y.getSave();
        this.quantite = y.getQuantite();
        this.quantiteBonus = y.getQuantiteBonus();
        this.prix = y.getPrix();
        this.remise = y.getRemise();
        this.taxe = y.getTaxe();
        this.ristourne = y.getRistourne();
        this.comission = y.getComission();
        this.rabais = y.getRabais();
        this.pr = y.getPr();
        this.prixTotal = y.getPrixTotal();
        this.articleBonus = y.getArticleBonus();
        this.conditionnementBonus = y.getConditionnementBonus();
        this.supp = y.getSupp();
        this.mouvStock = y.getMouvStock();
        this.dateContenu = y.getDateContenu();
        this.actif = y.getActif();
        this.commentaire = y.getCommentaire();
        this.numSerie = y.getNumSerie();
        this.statut = y.getStatut();
        this.puvMin = y.getPuvMin();
        this.depoLivraisonPrevu = y.getDepoLivraisonPrevu();
        this.author = y.getAuthor();
        this.docVente = y.getDocVente();
        this.article = y.getArticle();
        this.lot = y.getLot();
        this.parent = y.getParent();
        this.idReservation = y.getIdReservation();
        this.qualite = y.getQualite();
        this.new_ = y.isNew_();
        this.selectActif = y.isSelectActif();
        this.update = y.isUpdate();
        this.qteRestant = y.getQteRestant();
        this.qteAttente = y.getQteAttente();
        this.tauxRemise = y.getTauxRemise();
        this.quantite_ = y.getQuantite_();
        this.prix_ = y.getPrix_();
        this.remise_ = y.getRemise_();
        this.taxe_ = y.getTaxe_();
        this.calculPr = y.getCalculPr();
        this.onComment = y.isOnComment();
        this.contenus = y.getContenus();
        this.taxes = y.getTaxes();
        this.conditionnement = y.getConditionnement();
        this.bonus = y.isBonus();
        this.lots = y.getLots();
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getCalculPr() {
        return calculPr != null ? calculPr : true;
    }

    public void setCalculPr(Boolean calculPr) {
        this.calculPr = calculPr;
    }

    public long getSave() {
        return save;
    }

    public void setSave(long save) {
        this.save = save;
    }

    public boolean isBonus() {
        return bonus;
    }

    public void setBonus(boolean bonus) {
        this.bonus = bonus;
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

    public Character getStatutLivree() {
        return statutLivree != null ? (String.valueOf(statutLivree).trim().length() > 0 ? statutLivree : Constantes.STATUT_DOC_ATTENTE) : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatutLivree(Character statutLivree) {
        this.statutLivree = statutLivree;
    }

    public Boolean getMouvStock() {
        return mouvStock != null ? mouvStock : false;
    }

    public void setMouvStock(Boolean mouvStock) {
        this.mouvStock = mouvStock;
    }

    public String getStatut() {
        return statut != null ? (statut.trim().length() > 0 ? statut : Constantes.ETAT_EDITABLE) : Constantes.ETAT_EDITABLE;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Double getPr() {
        return pr != null ? pr : 0;
    }

    public void setPr(Double pr) {
        this.pr = pr;
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

    public String etat() {
        if (getPrix() < getPr()) {
            return "P";
        } else {
            if (getPuvMin() > getPrix()) {
                return "R";
            }
        }
        return "E";
    }

    public String getNumSerie() {
        return numSerie != null ? numSerie : "";
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public double getQuantite_() {
        return quantite_;
    }

    public void setQuantite_(double quantite_) {
        this.quantite_ = quantite_;
    }

    public double getPrix_() {
        return prix_;
    }

    public void setPrix_(double prix_) {
        this.prix_ = prix_;
    }

    public double getRemise_() {
        return remise_;
    }

    public void setRemise_(double remise_) {
        this.remise_ = remise_;
    }

    public double getTaxe_() {
        return taxe_;
    }

    public void setTaxe_(double taxe_) {
        this.taxe_ = taxe_;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
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

    public double getQteLivree() {
        return qteLivree;
    }

    public void setQteLivree(double qteLivree) {
        this.qteLivree = qteLivree;
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

    public double getTauxRemise() {
        tauxRemise = ((getRemise() * 100) / (getQuantite() * (getPrix() - getRabais())));
        return tauxRemise;
    }

    public void setTauxRemise(double tauxRemise) {
        this.tauxRemise = tauxRemise;
    }

    public Double getRistourne() {
        return ristourne != null ? ristourne : 0;
    }

    public void setRistourne(Double ristourne) {
        this.ristourne = ristourne;
    }

    public Double getComission() {
        return comission != null ? comission : 0;
    }

    public void setComission(Double comission) {
        this.comission = comission;
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

    public boolean isErrorComptabilise() {
        return errorComptabilise;
    }

    public void setErrorComptabilise(boolean errorComptabilise) {
        this.errorComptabilise = errorComptabilise;
    }

    public Double getPuvMin() {
        return puvMin != null ? puvMin : 0;
    }

    public void setPuvMin(Double puvMin) {
        this.puvMin = puvMin;
    }

    public Double getQuantiteBonus() {
        return quantiteBonus != null ? quantiteBonus : 0;
    }

    public void setQuantiteBonus(double quantiteBonus) {
        this.quantiteBonus = quantiteBonus;
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

    public Double getQuantite() {
        return quantite != null ? quantite : 0;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Double getPrix() {
        return prix != null ? prix : 0;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Double getRemise() {
        return remise != null ? remise : 0;
    }

    public void setRemise(Double remise) {
        this.remise = remise;
    }

    @XmlTransient
    @JsonIgnore
    public double getTaxe() {
        taxe = 0;
        if (taxes != null) {
            for (YvsComTaxeContenuVente t : taxes) {
                taxe += t.getMontant();
            }
        }
        return taxe;
    }

    public void setTaxe(Double taxe) {
        this.taxe = taxe;
    }

    public Double getRabais() {
        return rabais != null ? rabais : 0;
    }

    public void setRabais(Double rabais) {
        this.rabais = rabais;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public YvsComLotReception getLot() {
        return lot;
    }

    public void setLot(YvsComLotReception lot) {
        this.lot = lot;
    }

    public YvsBaseConditionnement getConditionnementBonus() {
        return conditionnementBonus;
    }

    public void setConditionnementBonus(YvsBaseConditionnement conditionnementBonus) {
        this.conditionnementBonus = conditionnementBonus;
    }

    public YvsBaseArticles getArticleBonus() {
        return articleBonus;
    }

    public void setArticleBonus(YvsBaseArticles articleBonus) {
        this.articleBonus = articleBonus;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public YvsBaseDepots getDepoLivraisonPrevu() {
        return depoLivraisonPrevu;
    }

    public void setDepoLivraisonPrevu(YvsBaseDepots depoLivraisonPrevu) {
        this.depoLivraisonPrevu = depoLivraisonPrevu;
    }

    public YvsComQualite getQualite() {
        return qualite;
    }

    public void setQualite(YvsComQualite qualite) {
        this.qualite = qualite;
    }

    public YvsComContenuDocVente getParent() {
        return parent;
    }

    public void setParent(YvsComContenuDocVente parent) {
        this.parent = parent;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComDocVentes getDocVente() {
        return docVente;
    }

    public void setDocVente(YvsComDocVentes docVente) {
        this.docVente = docVente;
    }

    public boolean isRequiereLot() {
        return requiereLot;
    }

    public void setRequiereLot(boolean requiereLot) {
        this.requiereLot = requiereLot;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public List<YvsComLotReception> getLots() {
        return lots;
    }

    public void setLots(List<YvsComLotReception> lots) {
        this.lots = lots;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBasePlanComptable getCompte() {
        return compte;
    }

    public void setCompte(YvsBasePlanComptable compte) {
        this.compte = compte;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComReservationStock getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(YvsComReservationStock idReservation) {
        this.idReservation = idReservation;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComTaxeContenuVente> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<YvsComTaxeContenuVente> taxes) {
        this.taxes = taxes;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComContenuDocVente> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComContenuDocVente> contenus) {
        this.contenus = contenus;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComContenuDocVenteEtat> getEtats() {
        return etats;
    }

    public void setEtats(List<YvsComContenuDocVenteEtat> etats) {
        this.etats = etats;
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
        if (!(object instanceof YvsComContenuDocVente)) {
            return false;
        }
        YvsComContenuDocVente other = (YvsComContenuDocVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.vente.YvsComContenuDocVente[ id=" + id + " ]";
    }

}
