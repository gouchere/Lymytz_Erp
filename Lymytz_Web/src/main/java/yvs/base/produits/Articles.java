/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.tiers.Tiers;
import yvs.dao.Util;
import yvs.entity.base.YvsBaseArticleAnalytique;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseArticleEquivalent;
import yvs.entity.base.YvsBaseArticleFournisseur;
import yvs.entity.base.YvsBaseArticleSubstitution;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.commercial.client.YvsBasePlanTarifaire;
import yvs.entity.param.YvsBaseTarifPointLivraison;
import yvs.entity.production.base.YvsProdGammeArticle;
import yvs.entity.production.base.YvsProdNomenclature;
import yvs.entity.produits.YvsBaseArticleCodeBarre;
import yvs.entity.produits.YvsBaseArticleDescription;
import yvs.entity.produits.YvsBaseArticlePack;
import yvs.entity.produits.YvsBaseArticlesAvis;
import yvs.util.Constantes;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class Articles implements Serializable, Comparator<Articles> {

    //donnees de base
    private long id;
    private String refArt, refArtExterne;
    private String designation, libelle, designationExterne;
    private String description;
    private String fichier;
    private String photo, photoExtension;
    private String photo1, photo1Extension;
    private String photo2, photo2Extension;
    private String photo3, photo3Extension;
    private String tags;
    private String codeBarre;
    private List<String> photos, photosExtension;
    private Tiers fabricant = new Tiers();
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private Date dateLastMvt = new Date();
    private char typeService;
    private double volume;
    private String categorie;
    private double tauxEcartPr;
    private String naturePrixMin = yvs.util.Constantes.NATURE_MTANT;
    private double masseNet;//masse d'une unité du produit. on multiplie par le conditionnement pour avoir la masse dans son conditionnement
    private UniteMesure unite = new UniteMesure(), uniteVolume = new UniteMesure();
    private UniteMesure uniteVente = new UniteMesure(), uniteStockage = new UniteMesure();
//    private Agence agence = new Agence();
    private List<YvsBaseArticleFournisseur> fournisseurs;
    private TemplateArticles template = new TemplateArticles();
    private List<YvsBasePlanTarifaire> plans_tarifaires;
    private List<YvsBaseArticleDescription> descriptions;
    private List<YvsBaseArticlesAvis> avis;
    private List<YvsBaseTarifPointLivraison> tarifs;
    private Conditionnement uniteVenteByDefault;
    private boolean requiereLot = false, sellWithOutStock, controleFournisseur = false;

    private ClassesStat classe1 = new ClassesStat();
    private ClassesStat classe2 = new ClassesStat();

    //donnees relatives au prix
    private double pua, lastPua;
    private double pr;
    private double puv;
    private double stock = 0, stock_ = 0;
    private boolean selectACtif, new_, displayExterne;
    private double puvMin;
    private double remise;
    private boolean puaTtc; //signifie que le prix d'achat est TTC;
    private boolean puvTtc; //signifie que le prix de vente est TTC;
    private boolean changePrix; //signifie que le prix de vente est négociable;
    private double coefficient;/*
     * le coeficient de stockage est lié au conditionnement. une unité de stock
     * d'un produit dans son conditionnement vaut son coefficient. par exemple,
     * les oeuf son conditionné en alvéole de coeficient 30.
     */

    //donnees liees a la production
    private double tauxEquivalenceStock;
    private String modeConso;
    private boolean defNorme;
    private boolean visibleEnSynthese, service;
    private boolean normeFixe;//indique si la norme est fixe ou variable. avec une norme fixe, on doit indiquer la quantité du produit qu'on veut fabriquer; avec une norme variable, la quantité du produit à fabriquer dépend de la somme des composants   

    //donnees liees au stockage
    private boolean suiviEnStock = true;
    private String methodeVal = "CMPI";

    //donnees liees a la classification
    private String refGroupe;
    private long idGroupe;
    private String classeStat;
    private GroupeArticle groupe = new GroupeArticle();
    private FamilleArticle famille = new FamilleArticle();
    private List<YvsBaseArticleEquivalent> articlesEquivalents;
    private List<YvsBaseArticleSubstitution> articlesSubstitutions;
    private List<YvsBaseArticlePack> packs;

    //donnees liees a la planification
    private double delaiLivraison;
    private double dureeGarantie;

    private List<YvsBaseArticleDepot> listArtDepots;
    private List<YvsBaseArticleCategorieComptable> listArticleCatComptable;
    private List<YvsBaseConditionnement> conditionnements;
    private List<YvsBaseArticleAnalytique> analytiques;
    private boolean actif = true, selectActif;
    private boolean model, error;

    private double lotApprovisionnement = 1;
    private double lotFabrication = 1;

    private long idArtSubstitution, idArtEquivalent;
    private boolean selectArt, listArt;

    /*Gestion de la production */
    private List<YvsProdGammeArticle> gammes;
    private List<YvsProdNomenclature> nomenclatures;
    private List<YvsBaseArticleCodeBarre> codeBarres;

    public Articles() {
        listArtDepots = new ArrayList<>();
        photos = new ArrayList<>();
        photosExtension = new ArrayList<>();
        plans_tarifaires = new ArrayList<>();
        articlesEquivalents = new ArrayList<>();
        articlesSubstitutions = new ArrayList<>();
        fournisseurs = new ArrayList<>();
        listArticleCatComptable = new ArrayList<>();
        conditionnements = new ArrayList<>();
        gammes = new ArrayList<>();
        nomenclatures = new ArrayList<>();
        codeBarres = new ArrayList<>();
        descriptions = new ArrayList<>();
        packs = new ArrayList<>();
        analytiques = new ArrayList<>();
        avis = new ArrayList<>();
    }

    public Articles(long id) {
        this();
        this.id = id;
    }

    public Articles(long id, String designation) {
        this(id);
        this.designation = designation;
    }

    public Articles(String designation) {
        this(0, designation);
    }

    public Articles(String designation, String refArt) {
        this(0, designation);
        this.refArt = refArt;
    }

    public Articles(long id, String designation, int lotFabrication) {
        this(id, designation);
        this.lotFabrication = lotFabrication;
    }

    public Articles(long id, String refArt, String designation) {
        this(id, designation);
        this.refArt = refArt;
    }

    public Date getDateLastMvt() {
        return dateLastMvt;
    }

    public void setDateLastMvt(Date dateLastMvt) {
        this.dateLastMvt = dateLastMvt;
    }

    public String getRefArtExterne() {
        return refArtExterne != null ? refArtExterne : "";
    }

    public void setRefArtExterne(String refArtExterne) {
        this.refArtExterne = refArtExterne;
    }

    public String getDesignationExterne() {
        return designationExterne != null ? designationExterne : "";
    }

    public void setDesignationExterne(String designationExterne) {
        this.designationExterne = designationExterne;
    }

    public boolean isDisplayExterne() {
        displayExterne = !"".equals(getRefArtExterne()) || !"".equals(getDesignationExterne());
        return displayExterne;
    }

    public void setDisplayExterne(boolean displayExterne) {
        this.displayExterne = displayExterne;
    }

    public ClassesStat getClasse1() {
        return classe1;
    }

    public void setClasse1(ClassesStat classe1) {
        this.classe1 = classe1;
    }

    public ClassesStat getClasse2() {
        return classe2;
    }

    public void setClasse2(ClassesStat classe2) {
        this.classe2 = classe2;
    }

    public List<String> listTags() {
        if (Util.asString(tags)) {
            return Arrays.asList(tags.split(";"));
        }
        return new ArrayList<>();
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public char getTypeService() {
        return typeService != ' ' ? typeService : Constantes.SERVICE_COMMERCE_GENERAL;
    }

    public void setTypeService(char typeService) {
        this.typeService = typeService;
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

    public String getCodeBarre() {
        codeBarre = null;
        for (YvsBaseArticleCodeBarre c : codeBarres) {
            if (codeBarre == null) {
                codeBarre = "[" + c.getCodeBarre() + "]";
            } else {
                codeBarre += " [" + c.getCodeBarre() + "]";
            }
        }
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public boolean withUnite() {
        if (getUnite() != null ? getUnite().getId() > 0 : false) {
            return true;
        }
        if (getUniteVolume() != null ? getUniteVolume().getId() > 0 : false) {
            return true;
        }
        return false;
    }

    public boolean chooseUnite() {
        if ((getUnite() != null ? getUnite().getId() > 0 : false) && (getUniteVolume() != null ? getUniteVolume().getId() > 0 : false)) {
            return true;
        } else {
            if (getUnite() != null ? getUnite().getId() > 0 : false) {
                return false;
            }
            if (getUniteVolume() != null ? getUniteVolume().getId() > 0 : false) {
                return false;
            }
        }
        return true;
    }

    public boolean isControleFournisseur() {
        return controleFournisseur;
    }

    public void setControleFournisseur(boolean controleFournisseur) {
        this.controleFournisseur = controleFournisseur;
    }

    public boolean isSellWithOutStock() {
        return sellWithOutStock;
    }

    public void setSellWithOutStock(boolean sellWithOutStock) {
        this.sellWithOutStock = sellWithOutStock;
    }

    public boolean isRequiereLot() {
        return requiereLot;
    }

    public void setRequiereLot(boolean requiereLot) {
        this.requiereLot = requiereLot;
    }

    public Conditionnement getUniteVenteByDefault() {
        return uniteVenteByDefault;
    }

    public void setUniteVenteByDefault(Conditionnement uniteVenteByDefault) {
        this.uniteVenteByDefault = uniteVenteByDefault;
    }

    public double getTauxEquivalenceStock() {
        return tauxEquivalenceStock;
    }

    public void setTauxEquivalenceStock(double tauxEquivalenceStock) {
        this.tauxEquivalenceStock = tauxEquivalenceStock;
    }

    public List<YvsBaseConditionnement> getConditionnements() {
        return conditionnements;
    }

    public void setConditionnements(List<YvsBaseConditionnement> conditionnements) {
        this.conditionnements = conditionnements;
    }

    public boolean isVisibleEnSynthese() {
        return visibleEnSynthese;
    }

    public void setVisibleEnSynthese(boolean visibleEnSynthese) {
        this.visibleEnSynthese = visibleEnSynthese;
    }

    public boolean isService() {
        return service;
    }

    public void setService(boolean service) {
        this.service = service;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public boolean isPuaTtc() {
        return puaTtc;
    }

    public void setPuaTtc(boolean puaTtc) {
        this.puaTtc = puaTtc;
    }

    public boolean isPuvTtc() {
        return puvTtc;
    }

    public void setPuvTtc(boolean puvTtc) {
        this.puvTtc = puvTtc;
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

    public String getNaturePrixMin() {
        return naturePrixMin != null ? (naturePrixMin.trim().length() > 0 ? naturePrixMin : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNaturePrixMin(String naturePrixMin) {
        this.naturePrixMin = naturePrixMin;
    }

    public List<YvsBasePlanTarifaire> getPlans_tarifaires() {
        return plans_tarifaires;
    }

    public void setPlans_tarifaires(List<YvsBasePlanTarifaire> plans_tarifaires) {
        this.plans_tarifaires = plans_tarifaires;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getStock_() {
        return stock_;
    }

    public void setStock_(double stock_) {
        this.stock_ = stock_;
    }

    public boolean isSelectACtif() {
        return selectACtif;
    }

    public void setSelectACtif(boolean selectACtif) {
        this.selectACtif = selectACtif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public List<YvsBaseArticleCategorieComptable> getListArticleCatComptable() {
        return listArticleCatComptable;
    }

    public void setListArticleCatComptable(List<YvsBaseArticleCategorieComptable> listArticleCatComptable) {
        this.listArticleCatComptable = listArticleCatComptable;
    }

    public long getIdArtEquivalent() {
        return idArtEquivalent;
    }

    public void setIdArtEquivalent(long idArtEquivalent) {
        this.idArtEquivalent = idArtEquivalent;
    }

    public long getIdArtSubstitution() {
        return idArtSubstitution;
    }

    public void setIdArtSubstitution(long idArtSubstitution) {
        this.idArtSubstitution = idArtSubstitution;
    }

    public UniteMesure getUniteVolume() {
        return uniteVolume;
    }

    public void setUniteVolume(UniteMesure uniteVolume) {
        this.uniteVolume = uniteVolume;
    }

    public TemplateArticles getTemplate() {
        return template;
    }

    public void setTemplate(TemplateArticles template) {
        this.template = template;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public List<YvsBaseArticleFournisseur> getFournisseurs() {
        return fournisseurs;
    }

    public void setFournisseurs(List<YvsBaseArticleFournisseur> fournisseurs) {
        this.fournisseurs = fournisseurs;
    }

    public double getDureeGarantie() {
        return dureeGarantie;
    }

    public void setDureeGarantie(double dureeGarantie) {
        this.dureeGarantie = dureeGarantie;
    }

    public String getFichier() {
        return fichier != null ? fichier : "";
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public String getPhotoExtension() {
        photoExtension = null;
        getPhotos();
        if (photosExtension != null ? !photosExtension.isEmpty() : false) {
            photoExtension = photosExtension.get(0);
        }
        return photoExtension != null ? photoExtension.trim().length() > 0 ? photoExtension : "jpeg" : "jpeg";
    }

    public void setPhotoExtension(String photoExtension) {
        this.photoExtension = photoExtension;
    }

    public String getPhoto() {
        photo = null;
        getPhotos();
        if (photos != null ? !photos.isEmpty() : false) {
            photo = photos.get(0);
        }
        return photo != null ? photo.trim().length() > 0 ? photo : Constantes.DEFAULT_PHOTO() : Constantes.DEFAULT_PHOTO();
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

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

    public void setPhotosExtension(List<String> photosExtension) {
        this.photosExtension = photosExtension;
    }

    public List<String> getPhotos() {
        photos.clear();
        if (getPhoto1() != null ? photo1.trim().length() > 0 : false) {
            photos.add(photo1);
        }
        if (getPhoto2() != null ? photo2.trim().length() > 0 : false) {
            photos.add(photo2);
        }
        if (getPhoto3() != null ? photo3.trim().length() > 0 : false) {
            photos.add(photo3);
        }
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto1Extension() {
        return photo1Extension != null ? photo1Extension.trim().length() > 0 ? photo1Extension : "png" : "png";
    }

    public void setPhoto1Extension(String photo1Extension) {
        this.photo1Extension = photo1Extension;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto2Extension() {
        return photo2Extension != null ? photo2Extension.trim().length() > 0 ? photo2Extension : "png" : "png";
    }

    public void setPhoto2Extension(String photo2Extension) {
        this.photo2Extension = photo2Extension;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto3Extension() {
        return photo3Extension != null ? photo3Extension.trim().length() > 0 ? photo3Extension : "png" : "png";
    }

    public void setPhoto3Extension(String photo3Extension) {
        this.photo3Extension = photo3Extension;
    }

    public Tiers getFabricant() {
        return fabricant;
    }

    public void setFabricant(Tiers fabricant) {
        this.fabricant = fabricant;
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

    public List<YvsBaseArticleEquivalent> getArticlesEquivalents() {
        return articlesEquivalents;
    }

    public void setArticlesEquivalents(List<YvsBaseArticleEquivalent> articlesEquivalents) {
        this.articlesEquivalents = articlesEquivalents;
    }

    public List<YvsBaseArticleSubstitution> getArticlesSubstitutions() {
        return articlesSubstitutions;
    }

    public void setArticlesSubstitutions(List<YvsBaseArticleSubstitution> articlesSubstitutions) {
        this.articlesSubstitutions = articlesSubstitutions;
    }

    public String getCategorie() {
        return categorie != null ? categorie : "";
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

    public UniteMesure getUniteVente() {
        return uniteVente;
    }

    public void setUniteVente(UniteMesure uniteVente) {
        this.uniteVente = uniteVente;
    }

    public UniteMesure getUniteStockage() {
        return uniteStockage;
    }

    public void setUniteStockage(UniteMesure uniteStockage) {
        this.uniteStockage = uniteStockage;
    }

    public boolean isDefNorme() {
        return defNorme;
    }

    public void setDefNorme(boolean defNorme) {
        this.defNorme = defNorme;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDesignation() {
        return designation != null ? designation : "";
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

    public List<YvsBaseArticleDepot> getListArtDepots() {
        return listArtDepots;
    }

    public void setListArtDepots(List<YvsBaseArticleDepot> listArtDepots) {
        this.listArtDepots = listArtDepots;
    }

    public double getMasseNet() {
        return masseNet;
    }

    public void setMasseNet(double masseNet) {
        this.masseNet = masseNet;
    }

    public String getModeConso() {
        return modeConso != null ? modeConso : "";
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
        return refArt != null ? refArt : "";
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

    public UniteMesure getUnite() {
        return unite;
    }

    public void setUnite(UniteMesure unite) {
        this.unite = unite;
    }

    public String getClasseStat() {
        return classeStat != null ? classeStat : "";
    }

    public void setClasseStat(String classeStat) {
        this.classeStat = classeStat;
    }

    public String getMethodeVal() {
        return methodeVal != null ? methodeVal : "";
    }

    public void setMethodeVal(String methodeVal) {
        this.methodeVal = methodeVal;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isModel() {
        return model;
    }

    public void setModel(boolean model) {
        this.model = model;
    }

    public double getDelaiLivraison() {
        return delaiLivraison;
    }

    public void setDelaiLivraison(double delaiLivraison) {
        this.delaiLivraison = delaiLivraison;
    }

    public double getLotApprovisionnement() {
        return lotApprovisionnement;
    }

    public void setLotApprovisionnement(double lotApprovisionnement) {
        this.lotApprovisionnement = lotApprovisionnement;
    }

    public double getLotFabrication() {
        return lotFabrication;
    }

    public void setLotFabrication(double lotFabrication) {
        this.lotFabrication = lotFabrication;
    }

    public List<YvsProdGammeArticle> getGammes() {
        return gammes;
    }

    public void setGammes(List<YvsProdGammeArticle> gammes) {
        this.gammes = gammes;
    }

    public List<YvsProdNomenclature> getNomenclatures() {
        return nomenclatures;
    }

    public void setNomenclatures(List<YvsProdNomenclature> nomenclatures) {
        this.nomenclatures = nomenclatures;
    }

    public double getLastPua() {
        return lastPua;
    }

    public void setLastPua(double lastPua) {
        this.lastPua = lastPua;
    }

    public List<YvsBaseArticleCodeBarre> getCodeBarres() {
        return codeBarres;
    }

    public void setCodeBarres(List<YvsBaseArticleCodeBarre> codeBarres) {
        this.codeBarres = codeBarres;
    }

    public List<YvsBaseArticleDescription> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<YvsBaseArticleDescription> descriptions) {
        this.descriptions = descriptions;
    }

    public List<YvsBaseArticlePack> getPacks() {
        return packs;
    }

    public void setPacks(List<YvsBaseArticlePack> packs) {
        this.packs = packs;
    }

    public List<YvsBaseArticleAnalytique> getAnalytiques() {
        return analytiques;
    }

    public void setAnalytiques(List<YvsBaseArticleAnalytique> analytiques) {
        this.analytiques = analytiques;
    }

    public List<YvsBaseArticlesAvis> getAvis() {
        return avis;
    }

    public void setAvis(List<YvsBaseArticlesAvis> avis) {
        this.avis = avis;
    }

    public List<YvsBaseTarifPointLivraison> getTarifs() {
        return tarifs;
    }

    public void setTarifs(List<YvsBaseTarifPointLivraison> tarifs) {
        this.tarifs = tarifs;
    }

    public double getPr() {
        return pr;
    }

    public void setPr(double pr) {
        this.pr = pr;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Articles other = (Articles) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compare(Articles o1, Articles o2) {
        if (o1.getRefGroupe().compareTo(o2.getRefGroupe()) > 0) {
            return 1;
        } else if (o1.getRefGroupe().compareTo(o2.getRefGroupe()) < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    public boolean canSale() {
        return categorie != null ? categorie.equals(Constantes.CAT_MARCHANDISE) || categorie.equals(Constantes.CAT_PF) || categorie.equals(Constantes.CAT_SERVICE) : true;
    }

    public boolean canStock() {
        return categorie != null ? !categorie.equals(Constantes.CAT_SERVICE) : true;
    }

    public double getTauxEcartPr() {
        return tauxEcartPr;
    }

    public void setTauxEcartPr(double tauxEcartPr) {
        this.tauxEcartPr = tauxEcartPr;
    }

}
