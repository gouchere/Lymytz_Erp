/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.achat;

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
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_article_approvisionnement")
@NamedQueries({
    @NamedQuery(name = "YvsComArticleApprovisionnement.findAll", query = "SELECT y FROM YvsComArticleApprovisionnement y WHERE y.fiche.depot.agence.societe = :societe"),
    @NamedQuery(name = "YvsComArticleApprovisionnement.findById", query = "SELECT y FROM YvsComArticleApprovisionnement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComArticleApprovisionnement.findByQuantite", query = "SELECT y FROM YvsComArticleApprovisionnement y WHERE y.quantite = :quantite"),
    @NamedQuery(name = "YvsComArticleApprovisionnement.findByFiche", query = "SELECT y FROM YvsComArticleApprovisionnement y WHERE y.fiche = :fiche"),
    @NamedQuery(name = "YvsComArticleApprovisionnement.findByArticleFiche", query = "SELECT y FROM YvsComArticleApprovisionnement y WHERE y.fiche = :fiche AND y.article = :article"),
    @NamedQuery(name = "YvsComArticleApprovisionnement.findByDateLivraison", query = "SELECT y FROM YvsComArticleApprovisionnement y WHERE y.dateLivraison = :dateLivraison")})
public class YvsComArticleApprovisionnement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_article_approvisionnement_id_seq", name = "yvs_com_article_approvisionnement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_article_approvisionnement_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "date_livraison")
    @Temporal(TemporalType.DATE)
    private Date dateLivraison;

    @JoinColumn(name = "fiche", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComFicheApprovisionnement fiche;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticleDepot article;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnement;

    @OneToMany(mappedBy = "article")
    private List<YvsComArticleFourniAchat> articles;
    @OneToMany(mappedBy = "externe")
    private List<YvsComContenuDocAchat> achats;
    @OneToMany(mappedBy = "externe")
    private List<YvsComContenuDocStock> stocks;

    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private boolean int_;
    @Transient
    private boolean update;
    @Transient
    private Double quantiteRest;
    @Transient
    private double qteWaitFacture;
    @Transient
    private double qteWaitTransfert;
    @Transient
    private double stock;
    @Transient
    private double prixAchat;
    @Transient
    private double remise;
    @Transient
    private boolean onAchat;
    @Transient
    private boolean onTransfert;
    @Transient
    private String critere = "SM";
    @Transient
    private YvsBaseDepots depot;
    @Transient
    private YvsBaseFournisseur fournisseur;

    public YvsComArticleApprovisionnement() {
        achats = new ArrayList<>();
        stocks = new ArrayList<>();
        articles = new ArrayList<>();
    }

    public YvsComArticleApprovisionnement(Long id) {
        this();
        this.id = id;
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

    public double getQteWaitFacture() {
        qteWaitFacture = 0;
        for (YvsComContenuDocAchat a : achats) {
            if (a.getDocAchat().getTypeDoc().equals("FA")) {
                if (a.getDocAchat().getStatut().equals(Constantes.ETAT_EDITABLE)) {
                    qteWaitFacture += a.getQuantiteRecu();
                }
            }
        }
        return qteWaitFacture;
    }

    public void setQteWaitFacture(double qteWaitFacture) {
        this.qteWaitFacture = qteWaitFacture;
    }

    public double getQteWaitTransfert() {
        qteWaitTransfert = 0;
        for (YvsComContenuDocStock a : stocks) {
            if (a.getDocStock().getStatut().equals(Constantes.ETAT_EDITABLE)) {
                qteWaitTransfert += a.getQuantite();
            }
        }
        return qteWaitTransfert;
    }

    public void setQteWaitTransfert(double qteWaitTransfert) {
        this.qteWaitTransfert = qteWaitTransfert;
    }

    public List<YvsComContenuDocAchat> getAchats() {
        return achats;
    }

    public void setAchats(List<YvsComContenuDocAchat> achats) {
        this.achats = achats;
    }

    public List<YvsComContenuDocStock> getStocks() {
        return stocks;
    }

    public void setStocks(List<YvsComContenuDocStock> stocks) {
        this.stocks = stocks;
    }

    public boolean isOnAchat() {
        onAchat = false;
        if (getArticle() != null ? getArticle().getModeAppro() != null : false) {
            switch (getArticle().getModeAppro()) {
                case Constantes.APPRO_ACHTON:
                case Constantes.APPRO_ACHT_EN:
                case Constantes.APPRO_ACHT_PROD:
                case Constantes.APPRO_ACHT_PROD_EN:
                    onAchat = true;
                    break;
                default:
                    break;
            }
        }
        return onAchat;
    }

    public void setOnAchat(boolean onAchat) {
        this.onAchat = onAchat;
    }

    public boolean isOnTransfert() {
        onTransfert = false;
        if (getArticle() != null ? getArticle().getModeAppro() != null : false) {
            switch (getArticle().getModeAppro()) {
                case Constantes.APPRO_ENON:
                case Constantes.APPRO_ACHT_PROD_EN:
                case Constantes.APPRO_PRODON:
                case Constantes.APPRO_PROD_EN:
                case Constantes.APPRO_ACHT_EN:
                case Constantes.APPRO_ACHT_PROD:
                    onTransfert = true;
                    break;
                default:
                    break;
            }
        }
        return onTransfert;
    }

    public void setOnTransfert(boolean onTransfert) {
        this.onTransfert = onTransfert;
    }

    public String getCritere() {
        return critere;
    }

    public void setCritere(String critere) {
        this.critere = critere;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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

    public Double getQuantiteRest() {
        quantiteRest = getQuantite();
        for (YvsComContenuDocStock a : stocks) {
            if (!a.getDocStock().getStatut().equals(Constantes.ETAT_EDITABLE) && !a.getDocStock().getStatut().equals(Constantes.ETAT_ANNULE)) {
                quantiteRest -= a.getQuantite();
            }
        }
        for (YvsComContenuDocAchat a : achats) {
            if (a.getDocAchat().getTypeDoc().equals("FA")) {
                if (!a.getDocAchat().getStatut().equals(Constantes.ETAT_EDITABLE) && !a.getDocAchat().getStatut().equals(Constantes.ETAT_ANNULE)) {
                    quantiteRest -= a.getQuantiteRecu();
                }
            }
        }
        return quantiteRest != null ? (quantiteRest > 0 ? quantiteRest : 0) : 0;
    }

    public void setQuantiteRest(Double quantiteRest) {
        this.quantiteRest = quantiteRest;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantite() {
        return quantite != null ? quantite : 0;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Date getDateLivraison() {
        return dateLivraison != null ? dateLivraison : new Date();
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public YvsComFicheApprovisionnement getFiche() {
        return fiche;
    }

    public void setFiche(YvsComFicheApprovisionnement fiche) {
        this.fiche = fiche;
    }

    public YvsBaseArticleDepot getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticleDepot article) {
        this.article = article;
    }

    public List<YvsComArticleFourniAchat> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsComArticleFourniAchat> articles) {
        this.articles = articles;
    }

    public YvsBaseDepots getDepot() {
        return depot;
    }

    public void setDepot(YvsBaseDepots depot) {
        this.depot = depot;
    }

    public YvsBaseFournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(YvsBaseFournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public double getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(double prixAchat) {
        this.prixAchat = prixAchat;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
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
        if (!(object instanceof YvsComArticleApprovisionnement)) {
            return false;
        }
        YvsComArticleApprovisionnement other = (YvsComArticleApprovisionnement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.achat.YvsComArticleApprovisionnement[ id=" + id + " ]";
    }

}
