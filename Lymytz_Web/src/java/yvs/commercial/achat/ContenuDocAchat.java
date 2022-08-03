/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.achat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.commercial.Qualite;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.commercial.achat.YvsComTaxeContenuAchat;
import yvs.entity.compta.analytique.YvsComptaCentreContenuAchat;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ContenuDocAchat implements Serializable, Comparable {

    private long id;
    private boolean errorQte;
    private double quantiteCommende, quantiteRecu;
    private double quantiteReste, quantiteBonus;
    private double saveFacture, saveLivre, saveEncours, saveBonus;
    private boolean errorPua;
    private double prixAchat;
    private double taxe;
    private double taxeRecu;
    private double coutAchat;
    private boolean errorRemise;
    private boolean calculPr = true;
    private double remiseRecu;
    private Date dateSave = new Date();
    private double prixTotalRecu, prixTotalAttendu;
    private double prixTvaRecu, prixTvaAttendu;
    private Date dateLivraison = new Date(), dateContenu = new Date();
    private String nameArticle;
    private String statut = Constantes.ETAT_EDITABLE;
    private boolean actif;
    private String commentaire;
    private String statutLivree = Constantes.ETAT_LIVRE;
    private String numSerie;
    private DocAchat docAchat = new DocAchat();
    private Articles article = new Articles();
    private Articles articleSave = new Articles();
    private Articles articleBonus = new Articles();
    private Qualite qualite = new Qualite();
    private ArticleApprovisionnement externe = new ArticleApprovisionnement();
    private LotReception lot = new LotReception();
    private boolean selectActif, new_;
    private ContenuDocAchat parent, bonus;
    private Conditionnement conditionnement = new Conditionnement();
    private Conditionnement conditionnementBonus = new Conditionnement();
    private List<YvsComTaxeContenuAchat> taxes;
    private List<YvsComptaCentreContenuAchat> analytiques;
    private List<YvsComLotReception> lots;

    public ContenuDocAchat() {
        taxes = new ArrayList<>();
        analytiques = new ArrayList<>();
        lots = new ArrayList<>();
    }

    public ContenuDocAchat(long id) {
        this();
        this.id = id;
    }

    public Articles getArticleSave() {
        return articleSave;
    }

    public void setArticleSave(Articles articleSave) {
        this.articleSave = articleSave;
    }

    public boolean isCalculPr() {
        return calculPr;
    }

    public void setCalculPr(boolean calculPr) {
        this.calculPr = calculPr;
    }

    public ContenuDocAchat getBonus() {
        return bonus;
    }

    public void setBonus(ContenuDocAchat bonus) {
        this.bonus = bonus;
    }

    public double getQuantiteBonus() {
        return quantiteBonus;
    }

    public void setQuantiteBonus(double quantiteBonus) {
        this.quantiteBonus = quantiteBonus;
    }

    public Articles getArticleBonus() {
        return articleBonus;
    }

    public void setArticleBonus(Articles articleBonus) {
        this.articleBonus = articleBonus;
    }

    public Conditionnement getConditionnementBonus() {
        return conditionnementBonus;
    }

    public void setConditionnementBonus(Conditionnement conditionnementBonus) {
        this.conditionnementBonus = conditionnementBonus;
    }

    public double getTaxeRecu() {
        return taxeRecu;
    }

    public void setTaxeRecu(double taxeRecu) {
        this.taxeRecu = taxeRecu;
    }

    public String getStatutLivree() {
        return statutLivree;
    }

    public void setStatutLivree(String statutLivree) {
        this.statutLivree = statutLivree;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public Qualite getQualite() {
        return qualite;
    }

    public void setQualite(Qualite qualite) {
        this.qualite = qualite;
    }

    public List<YvsComTaxeContenuAchat> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<YvsComTaxeContenuAchat> taxes) {
        this.taxes = taxes;
    }

    public double getTaxe() {
        return taxe;
    }

    public void setTaxe(double taxe) {
        this.taxe = taxe;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public ArticleApprovisionnement getExterne() {
        return externe;
    }

    public void setExterne(ArticleApprovisionnement externe) {
        this.externe = externe;
    }

    public ContenuDocAchat getParent() {
        return parent;
    }

    public void setParent(ContenuDocAchat parent) {
        this.parent = parent;
    }

    public double getCoutAchat() {
        return coutAchat;
    }

    public void setCoutAchat(double coutAchat) {
        this.coutAchat = coutAchat;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateContenu() {
        return dateContenu != null ? dateContenu : new Date();
    }

    public void setDateContenu(Date dateContenu) {
        this.dateContenu = dateContenu;
    }

    public double getPrixTvaRecu() {
        return prixTvaRecu;
    }

    public void setPrixTvaRecu(double prixTvaRecu) {
        this.prixTvaRecu = prixTvaRecu;
    }

    public double getPrixTvaAttendu() {
        return prixTvaAttendu;
    }

    public void setPrixTvaAttendu(double prixTvaAttendu) {
        this.prixTvaAttendu = prixTvaAttendu;
    }

    public boolean isErrorQte() {
        return errorQte;
    }

    public void setErrorQte(boolean errorQte) {
        this.errorQte = errorQte;
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

    public double getPrixTotalAttendu() {
        return prixTotalAttendu;
    }

    public void setPrixTotalAttendu(double prixTotalAttendu) {
        this.prixTotalAttendu = prixTotalAttendu;
    }

    public double getPrixTotalRecu() {
        return prixTotalRecu;
    }

    public void setPrixTotalRecu(double prixTotalRecu) {
        this.prixTotalRecu = prixTotalRecu;
    }

    public String getNameArticle() {
        return nameArticle;
    }

    public void setNameArticle(String nameArticle) {
        this.nameArticle = nameArticle;
    }

    public LotReception getLot() {
        return lot;
    }

    public void setLot(LotReception lot) {
        this.lot = lot;
    }

    public Date getDateLivraison() {
        return dateLivraison != null ? dateLivraison : new Date();
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getStatut() {
        return statut != null ? statut.trim().length() > 0 ? statut : Constantes.ETAT_EDITABLE : Constantes.ETAT_EDITABLE;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getQuantiteCommende() {
        return quantiteCommende;
    }

    public void setQuantiteCommende(double quantiteCommende) {
        this.quantiteCommende = quantiteCommende;
    }

    public double getQuantiteReste() {
        return quantiteReste;
    }

    public void setQuantiteReste(double quantiteReste) {
        this.quantiteReste = quantiteReste;
    }

    public double getQuantiteRecu() {
        return quantiteRecu;
    }

    public void setQuantiteRecu(double quantiteRecu) {
        this.quantiteRecu = quantiteRecu;
    }

    public double getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(double prixAchat) {
        this.prixAchat = prixAchat;
    }

    public double getRemiseRecu() {
        return remiseRecu;
    }

    public void setRemiseRecu(double remiseRecu) {
        this.remiseRecu = remiseRecu;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public DocAchat getDocAchat() {
        return docAchat;
    }

    public void setDocAchat(DocAchat docAchat) {
        this.docAchat = docAchat;
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

    public double getSaveFacture() {
        return saveFacture;
    }

    public void setSaveFacture(double saveFacture) {
        this.saveFacture = saveFacture;
    }

    public double getSaveBonus() {
        return saveBonus;
    }

    public void setSaveBonus(double saveBonus) {
        this.saveBonus = saveBonus;
    }

    public double getSaveLivre() {
        return saveLivre;
    }

    public void setSaveLivre(double saveLivre) {
        this.saveLivre = saveLivre;
    }

    public double getSaveEncours() {
        return saveEncours;
    }

    public void setSaveEncours(double saveEncours) {
        this.saveEncours = saveEncours;
    }

    public List<YvsComptaCentreContenuAchat> getAnalytiques() {
        return analytiques;
    }

    public void setAnalytiques(List<YvsComptaCentreContenuAchat> analytiques) {
        this.analytiques = analytiques;
    }

    public List<YvsComLotReception> getLots() {
        return lots;
    }

    public void setLots(List<YvsComLotReception> lots) {
        this.lots = lots;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ContenuDocAchat other = (ContenuDocAchat) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        ContenuDocAchat c = (ContenuDocAchat) o;
        if (dateLivraison.equals(c.dateLivraison)) {
            return Long.valueOf(id).compareTo(c.id);
        }
        return dateLivraison.compareTo(c.dateLivraison);
    }

}
