/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.stock;

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
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.YvsEntity;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.YvsComQualite;
import yvs.entity.commercial.achat.YvsComArticleApprovisionnement;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_contenu_doc_stock")
@NamedQueries({
    @NamedQuery(name = "YvsComContenuDocStock.findAll", query = "SELECT y FROM YvsComContenuDocStock y WHERE y.docStock.source.agence.societe = :societe"),
    @NamedQuery(name = "YvsComContenuDocStock.findById", query = "SELECT y FROM YvsComContenuDocStock y JOIN FETCH y.docStock JOIN FETCH y.docStock.source JOIN FETCH y.article "
            + "JOIN FETCH y.conditionnement JOIN FETCH y.conditionnement.unite WHERE y.id = :id"),
    @NamedQuery(name = "YvsComContenuDocStock.findByDocStock", query = "SELECT y FROM YvsComContenuDocStock y JOIN FETCH y.article JOIN FETCH y.conditionnement JOIN FETCH y.conditionnement.unite WHERE y.docStock = :docStock"),
    @NamedQuery(name = "YvsComContenuDocStock.findByDocStockT", query = "SELECT y FROM YvsComContenuDocStock y JOIN FETCH y.article JOIN FETCH y.conditionnement "
            + "JOIN FETCH y.conditionnement.unite JOIN FETCH y.conditionnementEntree JOIN FETCH y.conditionnementEntree.unite WHERE y.docStock = :docStock"),
    @NamedQuery(name = "YvsComContenuDocStock.findByExterne", query = "SELECT y FROM YvsComContenuDocStock y WHERE y.externe = :externe"),
    @NamedQuery(name = "YvsComContenuDocStock.findByQuantite", query = "SELECT y FROM YvsComContenuDocStock y WHERE y.quantite = :quantite"),
    @NamedQuery(name = "YvsComContenuDocStock.findByPrix", query = "SELECT y FROM YvsComContenuDocStock y WHERE y.prix = :prix"),
    @NamedQuery(name = "YvsComContenuDocStock.findBySupp", query = "SELECT y FROM YvsComContenuDocStock y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsComContenuDocStock.findByActif", query = "SELECT y FROM YvsComContenuDocStock y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsComContenuDocStock.findByDocArticle", query = "SELECT y FROM YvsComContenuDocStock y WHERE y.docStock = :docStock AND y.article = :article"),
    @NamedQuery(name = "YvsComContenuDocStock.findByDocId", query = "SELECT y FROM YvsComContenuDocStock y WHERE y.docStock = :docStock AND y.id = :id"),
    @NamedQuery(name = "YvsComContenuDocStock.findByParent", query = "SELECT y FROM YvsComContenuDocStock y WHERE y.parent = :parent"),
    @NamedQuery(name = "YvsComContenuDocStock.findTransfertToValid", query = "SELECT y FROM YvsComContenuDocStock y WHERE y.docStock.destination = :depot AND y.docStock.typeDoc = :typeDoc AND (y.docStock.statut = :statut OR y.docStock.statut=:statut2)"),
    @NamedQuery(name = "YvsComContenuDocStock.countTransfertToValid", query = "SELECT COUNT(y) FROM YvsComContenuDocStock y WHERE y.docStock.destination = :depot AND y.docStock.typeDoc = :typeDoc AND y.statut != :statut"),
    @NamedQuery(name = "YvsComContenuDocStock.findDepotByArticleDepot", query = "SELECT DISTINCT(y.docStock.source) FROM YvsComContenuDocStock y WHERE y.article = :article AND y.docStock.destination = :depot"),

    @NamedQuery(name = "YvsComContenuDocStock.countByArticle", query = "SELECT COUNT(y.id) FROM YvsComContenuDocStock y WHERE y.article = :article"),

    @NamedQuery(name = "YvsComContenuDocStock.findOneByInventaire", query = "SELECT y FROM YvsComContenuDocStock y WHERE (y.conditionnement = :conditionnement OR y.conditionnementEntree = :conditionnement) AND y.docStock.statut = :statut AND y.docStock.documentLie = :document"),
    @NamedQuery(name = "YvsComContenuDocStock.findByInventaireUnite", query = "SELECT y FROM YvsComContenuDocStock y WHERE (y.conditionnement = :conditionnement OR y.conditionnementEntree = :conditionnement) AND y.docStock.documentLie = :document"),
    @NamedQuery(name = "YvsComContenuDocStock.findByInventaireUniteLot", query = "SELECT y FROM YvsComContenuDocStock y WHERE (y.conditionnement = :conditionnement OR y.conditionnementEntree = :conditionnement) AND (y.lotSortie = :lot OR y.lotEntree = :lot) AND y.docStock.documentLie = :document"),
    @NamedQuery(name = "YvsComContenuDocStock.findByTypeParent", query = "SELECT y FROM YvsComContenuDocStock y WHERE (y.conditionnement = :conditionnement OR y.conditionnementEntree = :conditionnement) AND y.docStock.statut = :statut AND y.docStock.typeDoc = :type AND y.docStock.documentLie = :document"),
    @NamedQuery(name = "YvsComContenuDocStock.findByTypesParent", query = "SELECT y FROM YvsComContenuDocStock y WHERE (y.conditionnement = :conditionnement OR y.conditionnementEntree = :conditionnement) AND y.docStock.statut = :statut AND y.docStock.typeDoc IN :types AND y.docStock.documentLie = :document")})
public class YvsComContenuDocStock extends YvsEntity implements Serializable {

    @Id
    @SequenceGenerator(sequenceName = "yvs_contenu_doc_stock_id_seq", name = "yvs_contenu_doc_stock_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_contenu_doc_stock_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_contenu")
    @Temporal(TemporalType.DATE)
    private Date dateContenu;
    private static final long serialVersionUID = 1L;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "qte_attendu")
    private Double qteAttente;
    @Column(name = "prix")
    private Double prix;
    @Column(name = "prix_entree")
    private Double prixEntree;
    @Column(name = "supp")
    private Boolean supp = false;
    @Column(name = "actif")
    private Boolean actif = true;
    @Column(name = "impact_valeur_inventaire")
    private Boolean impactValeurInventaire = true;
    @Column(name = "commentaire")
    private String commentaire;
    @Column(name = "statut")
    private String statut = Constantes.ETAT_EDITABLE;
    @Column(name = "num_serie")
    private String numSerie;
    @Column(name = "quantite_entree")
    private Double quantiteEntree;
    @Column(name = "calcul_pr")
    private Boolean calculPr = true;
    @Column(name = "date_reception")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReception;

    @JsonManagedReference
    @JoinColumn(name = "doc_stock", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocStocks docStock;
    @JoinColumn(name = "lot_sortie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComLotReception lotSortie;
    @JoinColumn(name = "lot_entree", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComLotReception lotEntree;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JsonManagedReference
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComContenuDocStock parent;
    @JoinColumn(name = "externe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComArticleApprovisionnement externe;
    @JoinColumn(name = "qualite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComQualite qualite;
    @JoinColumn(name = "qualite_entree", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComQualite qualiteEntree;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "conditionnement_entree", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnementEntree;

    @OneToMany(mappedBy = "contenu", fetch = FetchType.LAZY)
    private List<YvsComContenuDocStockReception> receptions;

    @Transient
    private boolean new_;
    @Transient
    private boolean incoherence;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean update;
    @Transient
    private boolean onComment;
    @Transient
    private double prixTotal;
    @Transient
    private double quantiteRecu;
    @Transient
    private double qteRestant;
    @Transient
    private int nbre;
    @Transient
    private long idQualite;

    public YvsComContenuDocStock() {
        receptions = new ArrayList<>();
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        this.dateContenu = new Date();
    }

    public YvsComContenuDocStock(Long id) {
        this();
        this.id = id;
    }

    public YvsComContenuDocStock(Long id, YvsUsersAgence author) {
        this(id);
        this.author = author;
    }

    public YvsComContenuDocStock(long id, YvsComContenuDocStock y) {
        this(id);
        this.dateSave = y.getDateSave();
        this.dateContenu = y.getDateContenu();
        this.docStock = y.getDocStock();
        this.dateReception = y.getDateReception();
        this.quantite = y.getQuantite();
        this.qteAttente = y.getQteAttente();
        this.prix = y.getPrix();
        this.prixEntree = y.getPrixEntree();
        this.supp = y.getSupp();
        this.actif = y.getActif();
        this.commentaire = y.getCommentaire();
        this.statut = y.getStatut();
        this.numSerie = y.getNumSerie();
        this.author = y.getAuthor();
        this.article = y.getArticle();
        this.parent = y.getParent();
        this.externe = y.getExterne();
        this.qualite = y.getQualite();
        this.new_ = y.isNew_();
        this.incoherence = y.isIncoherence();
        this.lotEntree = y.getLotEntree();
        this.lotSortie = y.getLotSortie();
        this.selectActif = y.isSelectActif();
        this.update = y.isUpdate();
        this.onComment = y.isOnComment();
        this.prixTotal = y.getPrixTotal();
        this.qteRestant = y.getQteRestant();
        this.calculPr = y.getCalculPr();
        this.conditionnement = y.getConditionnement();
        this.conditionnementEntree = y.getConditionnementEntree();
        this.receptions = y.getReceptions();
    }

    public Boolean getCalculPr() {
        return calculPr != null ? calculPr : true;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateReception() {
        return dateReception;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateReception(Date dateReception) {
        this.dateReception = dateReception;
    }

    public void setCalculPr(Boolean calculPr) {
        this.calculPr = calculPr;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public long getIdQualite() {
        return idQualite;
    }

    public void setIdQualite(long idQualite) {
        this.idQualite = idQualite;
    }

    public int getNbre() {
        return nbre;
    }

    public void setNbre(int nbre) {
        this.nbre = nbre;
    }

    public String getStatut() {
        return statut != null ? (statut.trim().length() > 0 ? statut : Constantes.ETAT_EDITABLE) : Constantes.ETAT_EDITABLE;
    }

    public void setStatut(String statut) {
        this.statut = statut;
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

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComArticleApprovisionnement getExterne() {
        return externe;
    }

    public void setExterne(YvsComArticleApprovisionnement externe) {
        this.externe = externe;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComContenuDocStock getParent() {
        return parent;
    }

    public void setParent(YvsComContenuDocStock parent) {
        this.parent = parent;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComLotReception getLotSortie() {
        return lotSortie;
    }

    public void setLotSortie(YvsComLotReception lotSortie) {
        this.lotSortie = lotSortie;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComLotReception getLotEntree() {
        return lotEntree;
    }

    public void setLotEntree(YvsComLotReception lotEntree) {
        this.lotEntree = lotEntree;
    }

    public Double getQteAttente() {
        return qteAttente != null ? qteAttente : 0;
    }

    public void setQteAttente(Double qteAttente) {
        this.qteAttente = qteAttente;
    }

    public double getQteRestant() {
        return qteRestant;
    }

    public void setQteRestant(double qteRestant) {
        this.qteRestant = qteRestant;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public double getPrixTotal() {
        prixTotal = getQuantite() * getPrix();
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public boolean isIncoherence() {
        return incoherence;
    }

    public void setIncoherence(boolean incoherence) {
        this.incoherence = incoherence;
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

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateContenu() {
        return dateContenu;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateContenu(Date dateContenu) {
        this.dateContenu = dateContenu;
    }

    @Override
    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsComDocStocks getDocStock() {
        return docStock;
    }

    public void setDocStock(YvsComDocStocks docStock) {
        this.docStock = docStock;
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

    public Boolean getImpactValeurInventaire() {
        return impactValeurInventaire != null ? impactValeurInventaire : true;
    }

    public void setImpactValeurInventaire(Boolean impactValeurInventaire) {
        this.impactValeurInventaire = impactValeurInventaire;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public Double getPrixEntree() {
        return prixEntree != null ? prixEntree : 0;
    }

    public void setPrixEntree(Double prixEntree) {
        this.prixEntree = prixEntree;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComQualite getQualiteEntree() {
        return qualiteEntree;
    }

    public void setQualiteEntree(YvsComQualite qualiteEntree) {
        this.qualiteEntree = qualiteEntree;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Double getQuantiteEntree() {
        return quantiteEntree != null ? quantiteEntree : 0;
    }

    public void setQuantiteEntree(Double quantiteEntree) {
        this.quantiteEntree = quantiteEntree;
    }

    public double getQuantiteRecu() {
        quantiteRecu = 0;
        if (receptions != null) {
            for (YvsComContenuDocStockReception r : receptions) {
                quantiteRecu += r.getQuantite();
            }
        }
        return quantiteRecu;
    }

    public void setQuantiteRecu(double quantiteRecu) {
        this.quantiteRecu = quantiteRecu;
    }

//    @XmlTransient  @JsonIgnore
    public YvsBaseConditionnement getConditionnementEntree() {
        return conditionnementEntree;
    }

    public void setConditionnementEntree(YvsBaseConditionnement conditionnementEntree) {
        this.conditionnementEntree = conditionnementEntree;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComContenuDocStockReception> getReceptions() {
        return receptions;
    }

    public void setReceptions(List<YvsComContenuDocStockReception> receptions) {
        this.receptions = receptions;
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
        if (!(object instanceof YvsComContenuDocStock)) {
            return false;
        }
        YvsComContenuDocStock other = (YvsComContenuDocStock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.stock.YvsComContenuDocStock[ id=" + id + " ]";
    }

    @Transient
    public double getPrix(DaoInterfaceLocal dao, YvsComDocStocksValeur valeur) {
        double prix = getPrix();
        double coefficient = 1;
        if (valeur != null ? valeur.getId() > 0 : false) {
            coefficient = valeur.getCoefficient();
            String valoriseMs = valeur.getValoriseMsBy();
            String valoriseMp = valeur.getValoriseMpBy();
            String valorisePf = valeur.getValorisePfBy();
            String valorisePfs = valeur.getValorisePfsBy();
            switch (getArticle().getCategorie()) {
                case Constantes.CAT_MP:
                    prix = getPrix(dao, valoriseMp);
                    break;
                case Constantes.CAT_MARCHANDISE:
                    prix = getPrix(dao, valoriseMs);
                    break;
                case Constantes.CAT_PF:
                    prix = getPrix(dao, valorisePf);
                    break;
                case Constantes.CAT_PSF:
                    prix = getPrix(dao, valorisePfs);
                    break;
            }
        }
        prix = prix * coefficient;
        return prix;
    }

    @Transient
    public double getPrix(DaoInterfaceLocal dao, String valoriseBy) {
        double prix = getPrix();
        YvsBaseDepots depot = getDocStock().getSource() != null ? getDocStock().getSource() : getDocStock().getDestination();
        if (getConditionnement() != null ? getConditionnement().getId() > 0 : false) {
            switch (valoriseBy) {
                case "A":
                    prix = dao.getPua(getArticle().getId(), 0, depot.getId(), getConditionnement().getId(), getDocStock().getDateDoc(), (depot.getAgence() != null ? depot.getAgence().getId() != null ? depot.getAgence().getId() : 0 : 0));
                    break;
                case "V":
                    prix = getConditionnement().getPrix();
                    break;
                case "R":
                    prix = dao.getPr(getArticle().getId(), depot.getId(), 0, getDocStock().getDateDoc(), getConditionnement().getId());
                    break;
            }
        } else if (getConditionnementEntree() != null ? getConditionnementEntree().getId() > 0 : false) {
            switch (valoriseBy) {
                case "A":
                    prix = dao.getPua(getArticle().getId(), 0, depot.getId(), getConditionnementEntree().getId(), getDocStock().getDateDoc(), (depot.getAgence() != null ? depot.getAgence().getId() != null ? depot.getAgence().getId() : 0 : 0));
                    break;
                case "V":
                    prix = getConditionnementEntree().getPrix();
                    break;
                case "R":
                    prix = dao.getPr(getArticle().getId(), depot.getId(), 0, getDocStock().getDateDoc(), getConditionnementEntree().getId());
                    break;
            }
        }
        return prix;
    }

}
