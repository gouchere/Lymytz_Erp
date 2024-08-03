/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.depot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.entity.base.YvsBaseArticleEmplacement;
import yvs.entity.base.YvsBaseConditionnementDepot;
import yvs.entity.base.YvsBaseConditionnementPoint;
import yvs.entity.commercial.rrr.YvsComRabais;
import yvs.entity.users.YvsUsersAgence;
import yvs.parametrage.entrepot.Depots;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ArticleDepot implements Serializable {

    private long id;
    private double stockMax;
    private double stockMin, stockInitial, stockNet, margStockMoyen;
    private String designation, refArt;
    private String categorie;
    private String modeAppro = Constantes.APPRO_ACHT_PROD_EN, nameModeAppro;
    private String uniteInterval = Constantes.UNITE_JOUR;
    private String modeReappro = Constantes.REAPPRO_RUPTURE, nameModeReappro = "Rupture Stock";
    private int intervalApprov;
    private double pr, remise;
    private double quantiteStock, qteReel, tauxPua = 0;
    private boolean supp, proportionPua;
    private double avanceCommance = 0;
    private boolean actif, changePrix, prioritaire, prixMinIsTaux;
    private double stockAlert;
    private double stockSecurite;
    private double puv, puvMin;
    private String typeDocumentGenerer;
    private boolean genererDocument;
    private boolean defaultPr;
    private boolean requiereLot, suiviStock = true, sellWithoutStock = true;
    private Date dateAppro = new Date();
    private int lotLivraison;
    private Articles article = new Articles();
    private List<YvsBaseArticleEmplacement> articles;
    private String emplacements;
    private String naturePrixMin = Constantes.NATURE_MTANT;
    private String natureRemise = Constantes.NATURE_MTANT;
    private Depots depot = new Depots();
    private Depots depotPr = new Depots();
    private PointVente point = new PointVente();
    private boolean selectActif, new_, update, error, selectArt, listArt;
    private List<YvsBaseConditionnementPoint> conditionnements;
    private List<YvsBaseConditionnementDepot> conditionnements_;
    private List<YvsComRabais> rabais;
    private Conditionnement conditionnement = new Conditionnement();    //conditionnement vente

    private String valeur_exacte, valeur_prevu, operation, nature;
    private List<ArticleDepot> audits;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private YvsUsersAgence author;

    public ArticleDepot() {
        articles = new ArrayList<>();
        audits = new ArrayList<>();
        conditionnements = new ArrayList<>();
        conditionnements_ = new ArrayList<>();
        rabais = new ArrayList<>();
    }

    public ArticleDepot(long id) {
        this();
        this.id = id;
    }

    public ArticleDepot(long id, Articles article) {
        this(id);
        this.article = article;
    }

    public String getTypeDocumentGenerer() {
        return typeDocumentGenerer != null ? typeDocumentGenerer.trim().length() > 0 ? typeDocumentGenerer : Constantes.TYPE_FA : Constantes.TYPE_FA;
    }

    public void setTypeDocumentGenerer(String typeDocumentGenerer) {
        this.typeDocumentGenerer = typeDocumentGenerer;
    }

    public boolean isGenererDocument() {
        return genererDocument;
    }

    public void setGenererDocument(boolean genererDocument) {
        this.genererDocument = genererDocument;
    }

    public double getTauxPua() {
        return tauxPua;
    }

    public void setTauxPua(double tauxPua) {
        this.tauxPua = tauxPua;
    }

    public boolean isProportionPua() {
        return proportionPua;
    }

    public void setProportionPua(boolean proportionPua) {
        this.proportionPua = proportionPua;
    }

    public double getPuv() {
        return puv;
    }

    public void setPuv(double puv) {
        this.puv = puv;
    }

    public double getAvanceCommance() {
        return avanceCommance;
    }

    public void setAvanceCommance(double avanceCommance) {
        this.avanceCommance = avanceCommance;
    }

    public double getMargStockMoyen() {
        return margStockMoyen;
    }

    public void setMargStockMoyen(double margStockMoyen) {
        this.margStockMoyen = margStockMoyen;
    }

    public double getStockNet() {
        return stockNet;
    }

    public void setStockNet(double stockNet) {
        this.stockNet = stockNet;
    }

    public List<ArticleDepot> getAudits() {
        return audits;
    }

    public void setAudits(List<ArticleDepot> audits) {
        this.audits = audits;
    }

    public Depots getDepotPr() {
        return depotPr;
    }

    public void setDepotPr(Depots depotPr) {
        this.depotPr = depotPr;
    }

    public boolean isSelectArt() {
        return selectArt;
    }

    public void setSelectArt(boolean selectArt) {
        this.selectArt = selectArt;
    }

    public boolean isListArt() {
        return listArt;
    }

    public void setListArt(boolean listArt) {
        this.listArt = listArt;
    }

    public boolean isSellWithoutStock() {
        return sellWithoutStock;
    }

    public void setSellWithoutStock(boolean sellWithoutStock) {
        this.sellWithoutStock = sellWithoutStock;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isPrixMinIsTaux() {
        return prixMinIsTaux;
    }

    public void setPrixMinIsTaux(boolean prixMinIsTaux) {
        this.prixMinIsTaux = prixMinIsTaux;
    }

    public String getNatureRemise() {
        return natureRemise != null ? (natureRemise.trim().length() > 0 ? natureRemise : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNatureRemise(String natureRemise) {
        this.natureRemise = natureRemise;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public String getNaturePrixMin() {
        return naturePrixMin != null ? (naturePrixMin.trim().length() > 0 ? naturePrixMin : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNaturePrixMin(String naturePrixMin) {
        this.naturePrixMin = naturePrixMin;
    }

    public double getQteReel() {
        return qteReel;
    }

    public void setQteReel(double qteReel) {
        this.qteReel = qteReel;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public PointVente getPoint() {
        return point;
    }

    public void setPoint(PointVente point) {
        this.point = point;
    }

    public boolean isPrioritaire() {
        return prioritaire;
    }

    public void setPrioritaire(boolean prioritaire) {
        this.prioritaire = prioritaire;
    }

    public double getPuvMin() {
        return puvMin;
    }

    public void setPuvMin(double puvMin) {
        this.puvMin = puvMin;
    }

    public boolean isChangePrix() {
        return changePrix;
    }

    public void setChangePrix(boolean changePrix) {
        this.changePrix = changePrix;
    }

    public String getDesignation() {
        refArt = article != null ? article.getDesignation() : "";
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getRefArt() {
        refArt = article != null ? article.getRefArt() : "";
        return refArt;
    }

    public void setRefArt(String refArt) {
        this.refArt = refArt;
    }

    public String getEmplacements() {
        return emplacements != null ? emplacements : "";
    }

    public void setEmplacements(String emplacements) {
        this.emplacements = emplacements;
    }

    public double getStockInitial() {
        return stockInitial;
    }

    public void setStockInitial(double stockInitial) {
        this.stockInitial = stockInitial;
    }

    public void setArticles(List<YvsBaseArticleEmplacement> articles) {
        this.articles = articles;
    }

    public List<YvsBaseArticleEmplacement> getArticles() {
        return articles;
    }

    public Depots getDepot() {
        return depot;
    }

    public void setDepot(Depots depot) {
        this.depot = depot;
    }

    public String getUniteInterval() {
        return uniteInterval;
    }

    public void setUniteInterval(String uniteInterval) {
        this.uniteInterval = uniteInterval;
    }

    public String getNameModeAppro() {
        return nameModeAppro;
    }

    public Date getDateAppro() {
        return dateAppro;
    }

    public void setDateAppro(Date dateAppro) {
        this.dateAppro = dateAppro;
    }

    public int getLotLivraison() {
        return lotLivraison;
    }

    public void setLotLivraison(int lotLivraison) {
        this.lotLivraison = lotLivraison;
    }

    public void setNameModeAppro(String nameModeAppro) {
        this.nameModeAppro = nameModeAppro;
    }

    public String getNameModeReappro() {
        return nameModeReappro;
    }

    public void setNameModeReappro(String nameModeReappro) {
        this.nameModeReappro = nameModeReappro;
    }

    public double getStockSecurite() {
        return stockSecurite;
    }

    public void setStockSecurite(double stockSecurite) {
        this.stockSecurite = stockSecurite;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getStockMax() {
        return stockMax;
    }

    public void setStockMax(double stockMax) {
        this.stockMax = stockMax;
    }

    public double getStockMin() {
        return stockMin;
    }

    public void setStockMin(double stockMin) {
        this.stockMin = stockMin;
    }

    public String getModeAppro() {
        return modeAppro;
    }

    public void setModeAppro(String modeAppro) {
        this.modeAppro = modeAppro;
    }

    public String getModeReappro() {
        return modeReappro;
    }

    public void setModeReappro(String modeReappro) {
        this.modeReappro = modeReappro;
    }

    public int getIntervalApprov() {
        return intervalApprov;
    }

    public void setIntervalApprov(int intervalApprov) {
        this.intervalApprov = intervalApprov;
    }

    public double getPr() {
        return pr;
    }

    public void setPr(double pr) {
        this.pr = pr;
    }

    public boolean isRequiereLot() {
        return requiereLot;
    }

    public void setRequiereLot(boolean requiereLot) {
        this.requiereLot = requiereLot;
    }

    public double getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(double quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    public boolean isSupp() {
        return supp;
    }

    public void setSupp(boolean supp) {
        this.supp = supp;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public double getStockAlert() {
        stockAlert = getStockMin() + getStockSecurite();
        return stockAlert;
    }

    public void setStockAlert(double stockAlert) {
        this.stockAlert = stockAlert;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
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

    public String getValeur_exacte() {
        return valeur_exacte;
    }

    public void setValeur_exacte(String valeur_exacte) {
        this.valeur_exacte = valeur_exacte;
    }

    public String getValeur_prevu() {
        return valeur_prevu;
    }

    public void setValeur_prevu(String valeur_prevu) {
        this.valeur_prevu = valeur_prevu;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public List<YvsBaseConditionnementPoint> getConditionnements() {
        return conditionnements;
    }

    public void setConditionnements(List<YvsBaseConditionnementPoint> conditionnements) {
        this.conditionnements = conditionnements;
    }

    public List<YvsBaseConditionnementDepot> getConditionnements_() {
        return conditionnements_;
    }

    public void setConditionnements_(List<YvsBaseConditionnementDepot> conditionnements_) {
        this.conditionnements_ = conditionnements_;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public boolean isSuiviStock() {
        return suiviStock;
    }

    public void setSuiviStock(boolean suiviStock) {
        this.suiviStock = suiviStock;
    }

    public List<YvsComRabais> getRabais() {
        return rabais;
    }

    public void setRabais(List<YvsComRabais> rabais) {
        this.rabais = rabais;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public boolean isDefaultPr() {
        return defaultPr;
    }

    public void setDefaultPr(boolean defaultPr) {
        this.defaultPr = defaultPr;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ArticleDepot other = (ArticleDepot) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
