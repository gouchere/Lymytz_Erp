/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.vente;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.commercial.Qualite;
import yvs.commercial.achat.LotReception;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.commercial.vente.YvsComContenuDocVenteEtat;
import yvs.entity.commercial.vente.YvsComTaxeContenuVente;
import yvs.parametrage.entrepot.Depots;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
public class ContenuDocVente {

    private long id;
    private double quantite, quantite_, quantiteBonus;
    private double saveFacture, saveLivre, saveEncours, saveBonus;
    private double prix, prix_, prixMin, prixRabaix;
    private double remise, remise_;
    private double taxe, taxe_;
    private double ristourne, ristourne_;
    private double commission, commission_;
    private double prixTotal, prixTotal_;
    private double prixTaxe, prixTaxe_;
    private boolean calculPr = true;
    private double rabais;
    private double pr;
    private double ttc;
    private boolean supp;
    private boolean additionnel;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private Date dateContenu = new Date();
    private Qualite qualite = new Qualite();
    private LotReception lot = new LotReception();
    private boolean actif, mouvStock;
    private DocVente docVente = new DocVente();
    private Articles article = new Articles();
    private Articles articleBonus = new Articles();
    private boolean selectActif, new_, update, toBonus;
    private String commentaire, numSerie, statut, name;
    private ContenuDocVente parent, bonus;
    private Depots depoLivraisonPrevu = new Depots();
    private Conditionnement conditionnement = new Conditionnement();
    private Conditionnement conditionnementBonus = new Conditionnement();
    private List<YvsComTaxeContenuVente> taxes;
    private List<YvsComContenuDocVenteEtat> etats;
    private List<YvsComLotReception> lots;

    public ContenuDocVente() {
        taxes = new ArrayList<>();
        etats = new ArrayList<>();
        lots = new ArrayList<>();
    }

    public ContenuDocVente(long id) {
        this();
        this.id = id;
    }

    public boolean isCalculPr() {
        return calculPr;
    }

    public void setCalculPr(boolean calculPr) {
        this.calculPr = calculPr;
    }

    public boolean isAdditionnel() {
        return additionnel;
    }

    public void setAdditionnel(boolean additionnel) {
        this.additionnel = additionnel;
    }

    public ContenuDocVente getBonus() {
        return bonus;
    }

    public void setBonus(ContenuDocVente bonus) {
        this.bonus = bonus;
    }

    public boolean isToBonus() {
        return toBonus;
    }

    public void setToBonus(boolean toBonus) {
        this.toBonus = toBonus;
    }

    public double getSaveBonus() {
        return saveBonus;
    }

    public void setSaveBonus(double saveBonus) {
        this.saveBonus = saveBonus;
    }

    public Conditionnement getConditionnementBonus() {
        return conditionnementBonus;
    }

    public void setConditionnementBonus(Conditionnement conditionnementBonus) {
        this.conditionnementBonus = conditionnementBonus;
    }

    public Articles getArticleBonus() {
        return articleBonus;
    }

    public void setArticleBonus(Articles articleBonus) {
        this.articleBonus = articleBonus;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public Depots getDepoLivraisonPrevu() {
        return depoLivraisonPrevu;
    }

    public void setDepoLivraisonPrevu(Depots depoLivraisonPrevu) {
        this.depoLivraisonPrevu = depoLivraisonPrevu;
    }

    public Qualite getQualite() {
        return qualite;
    }

    public void setQualite(Qualite qualite) {
        this.qualite = qualite;
    }

    public double getPrix_() {
        return prix_;
    }

    public void setPrix_(double prix_) {
        this.prix_ = prix_;
    }

    public LotReception getLot() {
        return lot;
    }

    public void setLot(LotReception lot) {
        this.lot = lot;
    }

    public boolean isMouvStock() {
        return mouvStock;
    }

    public void setMouvStock(boolean mouvStock) {
        this.mouvStock = mouvStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<YvsComContenuDocVenteEtat> getEtats() {
        return etats;
    }

    public void setEtats(List<YvsComContenuDocVenteEtat> etats) {
        this.etats = etats;
    }

    public List<YvsComTaxeContenuVente> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<YvsComTaxeContenuVente> taxes) {
        this.taxes = taxes;
    }

    public String getStatut() {
        return statut != null ? (statut.trim().length() > 0 ? statut : Constantes.ETAT_EDITABLE) : Constantes.ETAT_EDITABLE;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public double getPr() {
        return pr;
    }

    public void setPr(double pr) {
        this.pr = pr;
    }

    public double getRabais() {
        return rabais;
    }

    public void setRabais(double rabais) {
        this.rabais = rabais;
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

    public double getQuantite_() {
        return quantite_;
    }

    public void setQuantite_(double quantite_) {
        this.quantite_ = quantite_;
    }

    public double getPrixMin() {
        return prixMin;
    }

    public void setPrixMin(double prixMin) {
        this.prixMin = prixMin;
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

    public double getRistourne_() {
        return ristourne_;
    }

    public void setRistourne_(double ristourne_) {
        this.ristourne_ = ristourne_;
    }

    public double getCommission_() {
        return commission_;
    }

    public void setCommission_(double commission_) {
        this.commission_ = commission_;
    }

    public double getPrixTotal_() {
        return prixTotal_;
    }

    public void setPrixTotal_(double prixTotal_) {
        this.prixTotal_ = prixTotal_;
    }

    public double getPrixTaxe_() {
        return prixTaxe_;
    }

    public void setPrixTaxe_(double prixTaxe_) {
        this.prixTaxe_ = prixTaxe_;
    }

    public ContenuDocVente getParent() {
        return parent != null ? parent : new ContenuDocVente();
    }

    public void setParent(ContenuDocVente parent) {
        this.parent = parent;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public double getPrixTaxe() {
        return prixTaxe;
    }

    public void setPrixTaxe(double prixTaxe) {
        this.prixTaxe = prixTaxe;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getQuantite() {
        return quantite <= 0 ? 1 : quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public double getTaxe() {
        return taxe;
    }

    public void setTaxe(double taxe) {
        this.taxe = taxe;
    }

    public double getRistourne() {
        return ristourne;
    }

    public void setRistourne(double ristourne) {
        this.ristourne = ristourne;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public boolean isSupp() {
        return supp;
    }

    public void setSupp(boolean supp) {
        this.supp = supp;
    }

    public Date getDateContenu() {
        return dateContenu != null ? dateContenu : new Date();
    }

    public void setDateContenu(Date dateContenu) {
        this.dateContenu = dateContenu;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public DocVente getDocVente() {
        return docVente;
    }

    public void setDocVente(DocVente docVente) {
        this.docVente = docVente;
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

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public double getQuantiteBonus() {
        return quantiteBonus;
    }

    public void setQuantiteBonus(double quantiteBonus) {
        this.quantiteBonus = quantiteBonus;
    }

    public double getPrixRabaix() {
        prixRabaix = prix - rabais;
        return prixRabaix;
    }

    public void setPrixRabaix(double prixRabaix) {
        this.prixRabaix = prixRabaix;
    }

    public double getTtc() {
        ttc = (getQuantite() * getPrixRabaix()) + taxe - remise;
        return ttc;
    }

    public void setTtc(double ttc) {
        this.ttc = ttc;
    }

    public double getSaveFacture() {
        return saveFacture;
    }

    public void setSaveFacture(double saveFacture) {
        this.saveFacture = saveFacture;
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

    public boolean controlTotal() {
        return prixTotal == ((quantite * (prix - rabais)) - remise + (article != null ? article.isPuvTtc() ? 0 : taxe : 0));
    }

    public List<YvsComLotReception> getLots() {
        return lots;
    }

    public void setLots(List<YvsComLotReception> lots) {
        this.lots = lots;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ContenuDocVente other = (ContenuDocVente) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
