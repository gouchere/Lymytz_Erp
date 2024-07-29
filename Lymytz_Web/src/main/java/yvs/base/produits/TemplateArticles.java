/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import yvs.commercial.depot.ArticleDepot;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.commercial.client.YvsBasePlanTarifaire;
import yvs.entity.produits.YvsBaseArticles;
import yvs.parametrage.agence.Agence;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean()
@SessionScoped
public class TemplateArticles implements Serializable {

    //donnees de base
    private long id;
    private String libelle;
    private String refArt;
    private boolean changeRefArt;
    private String designation;
    private boolean changeDesignation;
    private String description;
    private String fichier;
    private boolean changeFichier;
    private String photo1;
    private String photo2;
    private String photo3;
    private ClassesStat classe = new ClassesStat();
    private boolean changeClasse;
    private String categorie;
    private boolean changeCategorie;
    private double masseNet;
    private Agence agence = new Agence();
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private List<ArticleFournisseur> fournisseurs;
    private double tauxEquivalenceStock;

    //donnees relatives au prix
    private double pua;
    private double puv;
    private double puvMin;
    private double remise;
    private boolean changePrix, changeChangePrix; //signifie que le prix de vente est négociable;
    private double coefficient;
    private boolean changeCoefficient;/*
     * le coeficient de stockage est lié au conditionnement. une unité de stock
     * d'un produit dans son conditionnement vaut son coefficient. par exemple,
     * les oeuf son conditionné en alvéole de coeficient 30.
     */

    //donnees liees a la production
    private String modeConso;
    private boolean changeModeConso;
    private boolean defNorme, changeDefNorme;
    private boolean normeFixe, changeNormeFixe;//indique si la norme est fixe ou variable. avec une norme fixe, on doit indiquer la quantité du produit qu'on veut fabriquer; avec une norme variable, la quantité du produit à fabriquer dépend de la somme des composants   

    //donnees liees au stockage
    private boolean suiviEnStock = true, changeSuiviEnStock;
    private boolean visibleEnSynthese, changeVisibleEnSynthese;
    private boolean service, changeService;
    private String methodeVal;
    private boolean changeMethodeVal;

    //donnees liees a la classification
    private String refGroupe;
    private long idGroupe;
    private GroupeArticle groupe = new GroupeArticle();
    private boolean changeGroupe;
    private FamilleArticle famille = new FamilleArticle();
    private boolean changeFamille;
    private List<TemplateArticles> articlesEquivalents, articlesSubstitutions;

    //donnees liees a la planification
    private double dureeVie;
    private boolean changeDureeVie;
    private double dureeGarantie;
    private boolean changeDureeGarantie;

    private List<ArticleDepot> listArtDepot;
    private List<YvsBasePlanTarifaire> tarifaires;
    private List<YvsBaseArticleCategorieComptable> comptes;
    private List<YvsBaseArticles> articles;
    private boolean selectActif, actif = true;

    public TemplateArticles() {
        listArtDepot = new ArrayList<>();
        tarifaires = new ArrayList<>();
        comptes = new ArrayList<>();
        articlesEquivalents = new ArrayList<>();
        articlesSubstitutions = new ArrayList<>();
        fournisseurs = new ArrayList<>();
        articles = new ArrayList<>();
    }

    public TemplateArticles(long id) {
        this();
        this.id = id;
    }

    public TemplateArticles(long id, String designation) {
        this(id);
        this.designation = designation;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Date getDateSave() {
        return dateSave;
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

    public double getTauxEquivalenceStock() {
        return tauxEquivalenceStock;
    }

    public void setTauxEquivalenceStock(double tauxEquivalenceStock) {
        this.tauxEquivalenceStock = tauxEquivalenceStock;
    }

    public List<YvsBaseArticles> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticles> articles) {
        this.articles = articles;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isService() {
        return service;
    }

    public void setService(boolean service) {
        this.service = service;
    }

    public boolean isVisibleEnSynthese() {
        return visibleEnSynthese;
    }

    public void setVisibleEnSynthese(boolean visibleEnSynthese) {
        this.visibleEnSynthese = visibleEnSynthese;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public List<ArticleFournisseur> getFournisseurs() {
        return fournisseurs;
    }

    public void setFournisseurs(List<ArticleFournisseur> fournisseurs) {
        this.fournisseurs = fournisseurs;
    }

    public double getDureeVie() {
        return dureeVie;
    }

    public void setDureeVie(double duree_vie) {
        this.dureeVie = duree_vie;
    }

    public double getDureeGarantie() {
        return dureeGarantie;
    }

    public void setDureeGarantie(double duree_garantie) {
        this.dureeGarantie = duree_garantie;
    }

    public String getFichier() {
        return fichier;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public ClassesStat getClasse() {
        return classe;
    }

    public void setClasse(ClassesStat classe) {
        this.classe = classe;
    }

    public GroupeArticle getGroupe() {
        return groupe;
    }

    public void setGroupe(GroupeArticle groupe) {
        this.groupe = groupe;
    }

    public FamilleArticle getFamille() {
        return famille;
    }

    public void setFamille(FamilleArticle famille) {
        this.famille = famille;
    }

    public List<TemplateArticles> getArticlesEquivalents() {
        return articlesEquivalents;
    }

    public void setArticlesEquivalents(List<TemplateArticles> articlesEquivalents) {
        this.articlesEquivalents = articlesEquivalents;
    }

    public List<TemplateArticles> getArticlesSubstitutions() {
        return articlesSubstitutions;
    }

    public void setArticlesSubstitutions(List<TemplateArticles> articlesSubstitutions) {
        this.articlesSubstitutions = articlesSubstitutions;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public boolean isChangePrix() {
        return changePrix;
    }

    public void setChangePrix(boolean changePrix) {
        this.changePrix = changePrix;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public boolean isChangeClasse() {
        return changeClasse;
    }

    public void setChangeClasse(boolean changeClasse) {
        this.changeClasse = changeClasse;
    }

    public boolean isDefNorme() {
        return defNorme;
    }

    public void setDefNorme(boolean defNorme) {
        this.defNorme = defNorme;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(long idGroupe) {
        this.idGroupe = idGroupe;
    }

    public List<ArticleDepot> getListArtDepot() {
        return listArtDepot;
    }

    public void setListArtDepot(List<ArticleDepot> listArtDepot) {
        this.listArtDepot = listArtDepot;
    }

    public List<YvsBasePlanTarifaire> getTarifaires() {
        return tarifaires;
    }

    public void setTarifaires(List<YvsBasePlanTarifaire> tarifaires) {
        this.tarifaires = tarifaires;
    }

    public double getMasseNet() {
        return masseNet;
    }

    public void setMasseNet(double masseNet) {
        this.masseNet = masseNet;
    }

    public String getModeConso() {
        return modeConso;
    }

    public void setModeConso(String modeConso) {
        this.modeConso = modeConso;
    }

    public boolean isNormeFixe() {
        return normeFixe;
    }

    public void setNormeFixe(boolean normeFixe) {
        this.normeFixe = normeFixe;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public double getPua() {
        return pua;
    }

    public void setPua(double pua) {
        this.pua = pua;
    }

    public double getPuv() {
        return puv;
    }

    public void setPuv(double puv) {
        this.puv = puv;
    }

    public double getPuvMin() {
        return puvMin;
    }

    public void setPuvMin(double puvMin) {
        this.puvMin = puvMin;
    }

    public String getRefArt() {
        return refArt;
    }

    public void setRefArt(String refArt) {
        this.refArt = refArt;
    }

    public String getRefGroupe() {
        return refGroupe;
    }

    public void setRefGroupe(String refGroupe) {
        this.refGroupe = refGroupe;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public boolean isSuiviEnStock() {
        return suiviEnStock;
    }

    public void setSuiviEnStock(boolean suiviEnStock) {
        this.suiviEnStock = suiviEnStock;
    }

    public List<YvsBaseArticleCategorieComptable> getComptes() {
        return comptes;
    }

    public void setComptes(List<YvsBaseArticleCategorieComptable> comptes) {
        this.comptes = comptes;
    }

    public String getMethodeVal() {
        return methodeVal;
    }

    public void setMethodeVal(String methodeVal) {
        this.methodeVal = methodeVal;
    }

    public boolean isChangeRefArt() {
        return changeRefArt;
    }

    public void setChangeRefArt(boolean changeRefArt) {
        this.changeRefArt = changeRefArt;
    }

    public boolean isChangeDesignation() {
        return changeDesignation;
    }

    public void setChangeDesignation(boolean changeDesignation) {
        this.changeDesignation = changeDesignation;
    }

    public boolean isChangeFichier() {
        return changeFichier;
    }

    public void setChangeFichier(boolean changeFichier) {
        this.changeFichier = changeFichier;
    }

    public boolean isChangeCategorie() {
        return changeCategorie;
    }

    public void setChangeCategorie(boolean changeCategorie) {
        this.changeCategorie = changeCategorie;
    }

    public boolean isChangeChangePrix() {
        return changeChangePrix;
    }

    public void setChangeChangePrix(boolean changeChangePrix) {
        this.changeChangePrix = changeChangePrix;
    }

    public boolean isChangeCoefficient() {
        return changeCoefficient;
    }

    public void setChangeCoefficient(boolean changeCoefficient) {
        this.changeCoefficient = changeCoefficient;
    }

    public boolean isChangeModeConso() {
        return changeModeConso;
    }

    public void setChangeModeConso(boolean changeModeConso) {
        this.changeModeConso = changeModeConso;
    }

    public boolean isChangeDefNorme() {
        return changeDefNorme;
    }

    public void setChangeDefNorme(boolean changeDefNorme) {
        this.changeDefNorme = changeDefNorme;
    }

    public boolean isChangeNormeFixe() {
        return changeNormeFixe;
    }

    public void setChangeNormeFixe(boolean changeNormeFixe) {
        this.changeNormeFixe = changeNormeFixe;
    }

    public boolean isChangeSuiviEnStock() {
        return changeSuiviEnStock;
    }

    public void setChangeSuiviEnStock(boolean changeSuiviEnStock) {
        this.changeSuiviEnStock = changeSuiviEnStock;
    }

    public boolean isChangeVisibleEnSynthese() {
        return changeVisibleEnSynthese;
    }

    public void setChangeVisibleEnSynthese(boolean changeVisibleEnSynthese) {
        this.changeVisibleEnSynthese = changeVisibleEnSynthese;
    }

    public boolean isChangeService() {
        return changeService;
    }

    public void setChangeService(boolean changeService) {
        this.changeService = changeService;
    }

    public boolean isChangeMethodeVal() {
        return changeMethodeVal;
    }

    public void setChangeMethodeVal(boolean changeMethodeVal) {
        this.changeMethodeVal = changeMethodeVal;
    }

    public boolean isChangeGroupe() {
        return changeGroupe;
    }

    public void setChangeGroupe(boolean changeGroupe) {
        this.changeGroupe = changeGroupe;
    }

    public boolean isChangeFamille() {
        return changeFamille;
    }

    public void setChangeFamille(boolean changeFamille) {
        this.changeFamille = changeFamille;
    }

    public boolean isChangeDureeVie() {
        return changeDureeVie;
    }

    public void setChangeDureeVie(boolean changeDureeVie) {
        this.changeDureeVie = changeDureeVie;
    }

    public boolean isChangeDureeGarantie() {
        return changeDureeGarantie;
    }

    public void setChangeDureeGarantie(boolean changeDureeGarantie) {
        this.changeDureeGarantie = changeDureeGarantie;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final TemplateArticles other = (TemplateArticles) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public boolean checkUpdate(TemplateArticles old) {
        changeChangePrix = isChangePrix() != old.isChangePrix();
        changeDefNorme = isDefNorme() != old.isDefNorme();
        changeDesignation = !getDesignation().equals(old.getDesignation());
        changeModeConso = !getModeConso().equals(old.getModeConso());
        changeNormeFixe = isNormeFixe() != old.isNormeFixe();
        changeSuiviEnStock = isSuiviEnStock() != old.isSuiviEnStock();
        changeVisibleEnSynthese = isVisibleEnSynthese() != old.isVisibleEnSynthese();
        changeCoefficient = getCoefficient() != old.getCoefficient();
        changeService = isService() != old.isService();
        changeMethodeVal = !getMethodeVal().equals(old.getMethodeVal());
        changeCategorie = !getCategorie().equals(old.getCategorie());
        changeDureeVie = getDureeVie() != old.getDureeVie();
        changeDureeGarantie = getDureeGarantie() != old.getDureeGarantie();
        changeFichier = !getFichier().equals(old.getFichier());
        changeRefArt = !getRefArt().equals(old.getRefArt());
        changeGroupe = !getGroupe().equals(old.getGroupe());
        changeClasse = !getClasse().equals(old.getClasse());
        changeFamille = !getFamille().equals(old.getFamille());

        if (changeChangePrix || changeDefNorme || changeDesignation || changeModeConso || changeNormeFixe
                || changeSuiviEnStock || changeVisibleEnSynthese || changeCoefficient || changeService
                || changeMethodeVal || changeCategorie || changeDureeVie || changeDureeGarantie || changeFichier || changeRefArt
                || changeGroupe || changeClasse || changeFamille) {
            return true;
        }
        return false;
    }

    public void checkUpdate(YvsBaseArticles a, TemplateArticles ne) {
        changeChangePrix = a.getChangePrix() == ne.isChangePrix();
        changeDefNorme = a.getDefNorme()== ne.isDefNorme();
        changeDesignation = a.getDesignation().equals(ne.getDesignation());
        changeModeConso = a.getModeConso().equals(ne.getModeConso());
        changeNormeFixe = a.getNorme() == ne.isNormeFixe();
        changeSuiviEnStock = a.getSuiviEnStock() == ne.isSuiviEnStock();
        changeVisibleEnSynthese = a.getVisibleEnSynthese() == ne.isVisibleEnSynthese();
        changeCoefficient = a.getCoefficient() == ne.getCoefficient();
        changeService = a.getService() == ne.isService();
        changeMethodeVal = a.getMethodeVal().equals(ne.getMethodeVal());
        changeCategorie = a.getCategorie().equals(ne.getCategorie());
        changeDureeVie = a.getDelaiLivraison() == ne.getDureeVie();
        changeDureeGarantie = a.getDureeGarantie() == ne.getDureeGarantie();
        changeFichier = a.getFichier().equals(ne.getFichier());
        changeRefArt = a.getRefArt().equals(ne.getRefArt());
        changeGroupe = a.getGroupe().getId().equals(ne.getGroupe().getId());
        changeClasse = a.getClasse1() != null ? (a.getClasse1().getId().equals(ne.getClasse().getId())) : true;
        changeFamille = a.getFamille() != null ? (a.getFamille().getId().equals(ne.getFamille().getId())) : true;
    }

    public void checkUpdate(Articles a, TemplateArticles ne) {
        changeChangePrix = a.isChangePrix() == ne.isChangePrix();
        changeDefNorme = a.isDefNorme() == ne.isDefNorme();
        changeDesignation = a.getDesignation().equals(ne.getDesignation());
        changeModeConso = a.getModeConso().equals(ne.getModeConso());
        changeNormeFixe = a.isNormeFixe() == ne.isNormeFixe();
        changeSuiviEnStock = a.isSuiviEnStock() == ne.isSuiviEnStock();
        changeVisibleEnSynthese = a.isVisibleEnSynthese() == ne.isVisibleEnSynthese();
        changeClasse = a.getClasse1().equals(ne.getClasse());
        changeCoefficient = a.getCoefficient() == ne.getCoefficient();
        changeService = a.isService() == ne.isService();
        changeMethodeVal = a.getMethodeVal().equals(ne.getMethodeVal());
        changeCategorie = a.getCategorie().equals(ne.getCategorie());
        changeDureeVie = a.getDelaiLivraison() == ne.getDureeVie();
        changeDureeGarantie = a.getDureeGarantie() == ne.getDureeGarantie();
        changeFichier = a.getFichier().equals(ne.getFichier());
        changeRefArt = a.getRefArt().equals(ne.getRefArt());
        changeGroupe = a.getGroupe().equals(ne.getGroupe());
        changeFamille = a.getFamille().equals(ne.getFamille());
    }
}
