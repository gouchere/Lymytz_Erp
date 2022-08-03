/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.ModelReglement;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.base.produits.FamilleArticle;
import yvs.base.produits.ManagedArticles;
import yvs.base.produits.ManagedFamilleArticle;
import yvs.base.produits.UniteMesure;
import yvs.commercial.ManagedModeReglement;
import yvs.commercial.UtilCom;
import yvs.commercial.rrr.GrilleRabais;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsBasePlanTarifaire;
import yvs.entity.commercial.client.YvsBasePlanTarifaireTranche;
import yvs.entity.commercial.client.YvsComCategorieTarifaire;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.production.UtilProd;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedCategorieClt extends Managed<CategorieClient, YvsBaseCategorieClient> implements Serializable {

    @ManagedProperty(value = "#{categorieClient}")
    private CategorieClient categorieClient;
    private List<YvsBaseCategorieClient> categories, categoriesParent;
    private YvsBaseCategorieClient categorieSelect;

    private List<YvsBaseArticles> artiolesSelect;
    private boolean addAllArt;

    private double coefficient;
    private String natureCoefficient = Constantes.NATURE_MTANT;
    private PlanTarifaireClient planTarif = new PlanTarifaireClient();
    private List<YvsBasePlanTarifaire> tarifs;
    private YvsBasePlanTarifaire tarifSelect;

    private GrilleRabais grille = new GrilleRabais();
    private List<YvsBasePlanTarifaireTranche> grilles;
    private YvsBasePlanTarifaireTranche grilleSelect;

    private boolean listArt;
    private String tabIds, tabIds_tarif, tabIds_grille;

    private CategorieTarifaire tarifaire = new CategorieTarifaire();
    private List<Integer> priorites;

    private PaginatorResult<YvsBasePlanTarifaire> pa = new PaginatorResult<>();
    private String artSearch;
    private long max = 10;

    public ManagedCategorieClt() {
        categories = new ArrayList<>();
        categoriesParent = new ArrayList<>();
        artiolesSelect = new ArrayList<>();
        tarifs = new ArrayList<>();
        priorites = new ArrayList<>();
        grilles = new ArrayList<>();
    }

    public String getArtSearch() {
        return artSearch;
    }

    public void setArtSearch(String artSearch) {
        this.artSearch = artSearch;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public PaginatorResult<YvsBasePlanTarifaire> getPa() {
        return pa;
    }

    public void setPa(PaginatorResult<YvsBasePlanTarifaire> pa) {
        this.pa = pa;
    }

    public YvsBasePlanTarifaireTranche getGrilleSelect() {
        return grilleSelect;
    }

    public void setGrilleSelect(YvsBasePlanTarifaireTranche grilleSelect) {
        this.grilleSelect = grilleSelect;
    }

    public String getNatureCoefficient() {
        return natureCoefficient;
    }

    public void setNatureCoefficient(String natureCoefficient) {
        this.natureCoefficient = natureCoefficient;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public GrilleRabais getGrille() {
        return grille;
    }

    public void setGrille(GrilleRabais grille) {
        this.grille = grille;
    }

    public List<YvsBasePlanTarifaireTranche> getGrilles() {
        return grilles;
    }

    public void setGrilles(List<YvsBasePlanTarifaireTranche> grilles) {
        this.grilles = grilles;
    }

    public String getTabIds_grille() {
        return tabIds_grille;
    }

    public void setTabIds_grille(String tabIds_grille) {
        this.tabIds_grille = tabIds_grille;
    }

    public boolean isListArt() {
        return listArt;
    }

    public void setListArt(boolean listArt) {
        this.listArt = listArt;
    }

    public CategorieTarifaire getTarifaire() {
        return tarifaire;
    }

    public void setTarifaire(CategorieTarifaire tarifaire) {
        this.tarifaire = tarifaire;
    }

    public List<Integer> getPriorites() {
        return priorites;
    }

    public void setPriorites(List<Integer> priorites) {
        this.priorites = priorites;
    }

    public boolean isAddAllArt() {
        return addAllArt;
    }

    public void setAddAllArt(boolean addAllArt) {
        this.addAllArt = addAllArt;
    }

    public List<YvsBaseArticles> getArtiolesSelect() {
        return artiolesSelect;
    }

    public void setArtiolesSelect(List<YvsBaseArticles> artiolesSelect) {
        this.artiolesSelect = artiolesSelect;
    }

    public List<YvsBasePlanTarifaire> getTarifs() {
        return tarifs;
    }

    public void setTarifs(List<YvsBasePlanTarifaire> tarifs) {
        this.tarifs = tarifs;
    }

    public YvsBasePlanTarifaire getTarifSelect() {
        return tarifSelect;
    }

    public void setTarifSelect(YvsBasePlanTarifaire tarifSelect) {
        this.tarifSelect = tarifSelect;
    }

    public PlanTarifaireClient getPlanTarif() {
        return planTarif;
    }

    public void setPlanTarif(PlanTarifaireClient planTarif) {
        this.planTarif = planTarif;
    }

    public String getTabIds_tarif() {
        return tabIds_tarif;
    }

    public void setTabIds_tarif(String tabIds_tarif) {
        this.tabIds_tarif = tabIds_tarif;
    }

    public YvsBaseCategorieClient getCategorieSelect() {
        return categorieSelect;
    }

    public void setCategorieSelect(YvsBaseCategorieClient categorieSelect) {
        this.categorieSelect = categorieSelect;
    }

    public List<YvsBaseCategorieClient> getCategoriesParent() {
        return categoriesParent;
    }

    public void setCategoriesParent(List<YvsBaseCategorieClient> categoriesParent) {
        this.categoriesParent = categoriesParent;
    }

    public CategorieClient getCategorieClient() {
        return categorieClient;
    }

    public void setCategorieClient(CategorieClient categorieClient) {
        this.categorieClient = categorieClient;
    }

    public List<YvsBaseCategorieClient> getCategories() {
        return categories;
    }

    public void setCategories(List<YvsBaseCategorieClient> categories) {
        this.categories = categories;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        loadAll(false);
    }

    public void loadAll(boolean actif) {
        if (actif) {
            loadAllCategorieActif();
        } else {
            loadAllCategorie();
        }
        loadParents(categorieSelect);
        if (categorieClient.getParent() != null ? categorieClient.getParent().getId() < 1 : true) {
            categorieClient.setParent(new CategorieClient());
        }
    }

    public void loadAllCategorie() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        categories = dao.loadNameQueries("YvsBaseCategorieClient.findAll", champ, val);
    }

    public void loadParents(YvsBaseCategorieClient y) {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsBaseCategorieClient.findAll";
        if (y != null ? y.getId() > 0 : false) {
            champ = new String[]{"societe", "id"};
            val = new Object[]{currentAgence.getSociete(), y.getId()};
            nameQueri = "YvsBaseCategorieClient.findByNotId";
        }
        categoriesParent = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadAllCategorieActif() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        categories = dao.loadNameQueries("YvsBaseCategorieClient.findAllActif", champ, val);
    }

    public void loadGrille(YvsBasePlanTarifaire y) {
        champ = new String[]{"plan"};
        val = new Object[]{y};
        nameQueri = "YvsBasePlanTarifaireTranche.findByPlan";
        grilles = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadTarif(boolean avance, boolean init) {
        if (categorieSelect != null ? categorieSelect.getId() > 0 : false) {
            pa.addParam(new ParametreRequete("y.categorie", "categorie", categorieSelect, "=", "AND"));
            tarifs = pa.executeDynamicQuery("YvsBasePlanTarifaire", "y.id", avance, init, (int) max, dao);
            for (YvsBasePlanTarifaire t : tarifs) {
                if (t.getArticle() != null ? t.getArticle().getId() > 0 : false) {
                    champ = new String[]{"parent", "article"};
                    val = new Object[]{categorieSelect.getParent(), t.getArticle()};
                    nameQueri = "YvsBasePlanTarifaire.findByParentArticle";
                } else {
                    champ = new String[]{"parent", "famille"};
                    val = new Object[]{categorieSelect.getParent(), t.getFamille()};
                    nameQueri = "YvsBasePlanTarifaire.findByParentFamille";
                }
                List<YvsBasePlanTarifaire> lt = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
                if (lt != null ? !lt.isEmpty() : false) {
                    t.setParent(lt.get(0));
                }
            }
        }
    }

    public void _choosePaginator(ValueChangeEvent ev) {
        if ((ev != null) ? ev.getNewValue() != null : false) {
            long v = (long) ev.getNewValue();
            max = v;
            loadTarif(true, true);
        }
    }

    public void addParamArticleOnTarif() {
        ParametreRequete p;
        if (artSearch != null ? artSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "refArt", artSearch, "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "refArt", artSearch.toUpperCase() + "%", " LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "refArt", artSearch.toUpperCase() + "%", " LIKE", "OR"));
        } else {
            p = new ParametreRequete("y.article", "refArt", null);
        }
        pa.addParam(p);
        loadTarif(true, true);
    }

    public YvsBaseCategorieClient buildCategorieClient(CategorieClient y) {
        YvsBaseCategorieClient c = UtilCom.buildCategorieClient(y, currentUser, currentAgence.getSociete());
        if (y.getModel() != null ? y.getModel().getId() > 0 : false) {
            ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
            if (m != null) {
                c.setModel(m.getModels().get(m.getModels().indexOf(new YvsBaseModelReglement(y.getModel().getId()))));
            } else {
                c.setModel(new YvsBaseModelReglement(y.getModel().getId(), y.getModel().getDesignation()));
            }
        }
        if ((y.getParent() != null) ? y.getParent().getId() > 0 : false) {
            if (categoriesParent.contains(new YvsBaseCategorieClient(y.getParent().getId()))) {
                c.setParent(categoriesParent.get(categoriesParent.indexOf(new YvsBaseCategorieClient(y.getParent().getId()))));
            }
        }
        return c;
    }

    public YvsComCategorieTarifaire buildTarifaire(CategorieTarifaire y, YvsComClient cc) {
        YvsComCategorieTarifaire c = UtilCom.buildTarifaire(y, currentUser);
        c.setClient(cc);
        c.setCategorie(categorieSelect);
        return c;
    }

    public PlanTarifaireClient recopiewViewTarif(CategorieClient y) {
        planTarif.setCategorie(y);
        return planTarif;
    }

    public GrilleRabais recopiewViewTrancheTarif() {
        GrilleRabais c = new GrilleRabais();
        c.setId(grille.getId());
        c.setBase(grille.getBase());
        if (grille.isUnique()) {
            c.setMontantMinimal(0);
            c.setMontantMaximal(Double.MAX_VALUE);
        } else {
            c.setMontantMaximal(grille.getMontantMaximal());
            c.setMontantMinimal(grille.getMontantMinimal());
        }
        c.setMontantRabais(grille.getMontantRabais());
        c.setNatureMontant(grille.getNatureMontant());
        c.setParent(grille.isUpdate() ? grille.getParent() : tarifSelect.getId());
        c.setValeur(grille.getValeur());
        c.setUpdate(grille.isUpdate());
        c.setDateSave(grille.getDateSave());
        c.setNew_(true);
        return c;
    }

    @Override
    public boolean controleFiche(CategorieClient bean) {
        if (bean.getCode() == null || bean.getCode().equals("")) {
            getErrorMessage("Vous devez entrer le code");
            return false;
        }
        if (bean.getLibelle() == null || bean.getLibelle().equals("")) {
            getErrorMessage("Vous devez entrer le libelle");
            return false;
        }
        if (bean.isDefaut()) {
            for (YvsBaseCategorieClient c : categories) {
                if (c.getDefaut() && !c.getId().equals(bean.getId())) {
                    getErrorMessage("Il y'a deja une catégorie par défaut");
                    return false;
                }
            }
        }
        if (!bean.isUpdate()) {
            YvsBaseCategorieClient t = (YvsBaseCategorieClient) dao.loadOneByNameQueries("YvsBaseCategorieClient.findByCurrentCode", new String[]{"code", "societe"}, new Object[]{bean.getCode(), currentAgence.getSociete()});
            if (t != null ? t.getId() > 0 : false) {
                getErrorMessage("Vous avez déja crée cette catégorie");
                return false;
            }
        }
        return true;
    }

    public boolean controleFicheTarif(PlanTarifaireClient bean, boolean msg) {
        if (bean.isForArticle()) {
            if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
                if (msg) {
                    getErrorMessage("Vous devez specifier l'article");
                }
                return false;
            }
            if ((bean.getConditionnement() != null) ? bean.getConditionnement().getId() < 1 : true) {
                if (msg) {
                    getErrorMessage("Vous devez specifier le conditionnement de l'article");
                }
                return false;
            }
        } else {
            if ((bean.getFamille() != null) ? bean.getFamille().getId() < 1 : true) {
                if (msg) {
                    getErrorMessage("Vous devez specifier la famille");
                }
                return false;
            }
        }
        if ((bean.getCategorie() != null) ? bean.getCategorie().getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez specifier la catégorie");
            }
            return false;
        }
        if (bean.getArticle() != null ? bean.getArticle().getId() > 0 : false) {
            champ = new String[]{"categorie", "article"};
            val = new Object[]{new YvsBaseCategorieClient(bean.getCategorie().getId()), new YvsBaseArticles(bean.getArticle().getId())};
            nameQueri = "YvsBasePlanTarifaire.findByCategorieArticle";
        } else {
            champ = new String[]{"categorie", "famille"};
            val = new Object[]{new YvsBaseCategorieClient(bean.getCategorie().getId()), new YvsBaseFamilleArticle(bean.getFamille().getId())};
            nameQueri = "YvsBasePlanTarifaire.findByCategorieFamille";
        }
        List<YvsBasePlanTarifaire> lp = dao.loadNameQueries(nameQueri, champ, val);
        if (lp != null ? !lp.isEmpty() : false) {
            if (!lp.get(0).getId().equals(bean.getId())) {
                if (msg) {
                    getErrorMessage("Vous avez deja associé cet élément");
                }
                return false;
            }
        }
        return true;
    }

    public boolean controleFicheTrancheTarif(GrilleRabais bean) {
        if (bean.getParent() < 1) {
            getErrorMessage("Vous devez specifier le tarif");
            return false;
        }
        if (bean.getMontantMaximal() < bean.getMontantMinimal()) {
            getErrorMessage("Le montant maximal ne peut pas etre inferieur au montant minimal");
            return false;
        }
        return true;
    }

    public boolean controleFicheMode(ModelReglement bean) {
        if (bean == null) {
            getErrorMessage("L'object ne peut pas etre null");
            return false;
        }
        if (bean.getDesignation() == null || bean.getDesignation().equals("")) {
            getErrorMessage("Vous devez entrer l'indicateur");
            return false;
        }
        if (!bean.isUpdate()) {
            List<YvsBaseModelReglement> lm = dao.loadNameQueries("YvsBaseModelReglement.findByReference", new String[]{"societe", "reference"}, new Object[]{currentAgence.getSociete(), bean.getDesignation()});
            if (lm != null ? !lm.isEmpty() : false) {
                getErrorMessage("Vousa avez deja un model avec cet indicateur");
                return false;
            }
        }
        return true;
    }

    @Override
    public void populateView(CategorieClient bean) {
        cloneObject(categorieClient, bean);
        if (categorieClient.getModel() == null) {
            categorieClient.setModel(new ModelReglement());
        }
        if (categorieClient.getParent() == null) {
            categorieClient.setParent(new CategorieClient());
        }
    }

    public void populateViewPlanTarif(PlanTarifaireClient bean) {
        cloneObject(planTarif, bean);
    }

    public void populateViewTranchePlanTarif(GrilleRabais bean) {
        cloneObject(grille, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(categorieClient);
        categorieClient.setFils(new ArrayList<CategorieClient>());
        categorieClient.setParent(new CategorieClient());
        categorieClient.setModel(new ModelReglement());
        categorieClient.setDefaut(false);
        tabIds = "";

        tarifs.clear();
        categorieSelect = new YvsBaseCategorieClient();
        loadParents(categorieSelect);

        resetFichePlanTarifaire();
        update("blog_form_categorie_client");
    }

    public void resetFichePlanTarifaire() {
        planTarif = new PlanTarifaireClient();
        planTarif.setNatureCoefAugmentation(Constantes.NATURE_MTANT);
        planTarif.setNatureRemise(Constantes.NATURE_MTANT);
        planTarif.setNatureRistourne(Constantes.NATURE_MTANT);
        tabIds_tarif = "";
        tarifSelect = null;
        listArt = false;
    }

    public void resetFicheTranchePlanTarifaire() {
        resetFiche(grille);
        grille.setNatureMontant(Constantes.NATURE_MTANT);
        grille.setBase(Constantes.BASE_QTE);
        if (tarifs.contains(tarifSelect)) {
            YvsBasePlanTarifaire p = tarifs.get(tarifs.indexOf(tarifSelect));
            grille.setValeur(p.getPuv());
            grille.setMontantRabais(p.getRemise());
            grille.setReference("Tarif article '" + p.getArticle().getDesignation() + "'");
        }
        tabIds_grille = "";
    }

    @Override
    public boolean saveNew() {
        String action = categorieClient.isUpdate() ? "Modification" : "Insertion";
        try {
            if (controleFiche(categorieClient)) {
                categorieSelect = buildCategorieClient(categorieClient);
                if (!categorieClient.isUpdate()) {
                    categorieSelect.setId(null);
                    categorieSelect = (YvsBaseCategorieClient) dao.save1(categorieSelect);
                    categorieClient.setId(categorieSelect.getId());
                    categories.add(0, categorieSelect);
                    categoriesParent.add(0, categorieSelect);
                } else {
                    dao.update(categorieSelect);
                    categories.set(categories.indexOf(categorieSelect), categorieSelect);
                    if (categoriesParent.contains(categorieSelect)) {
                        categoriesParent.set(categoriesParent.indexOf(categorieSelect), categorieSelect);
                    }
                }
                succes();
                actionOpenOrResetAfter(this);
                update("data_categorie_client");
                update("select_categorie_parent_client");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewPlanTarifaire() {
        try {
            if (!autoriser("base_article_save_tarifaire")) {
                openNotAcces();
                return false;
            }
            if (!planTarif.isForArticle() && planTarif.isApplyOnArticle()) {
                if (planTarif.getFamille() != null ? planTarif.getFamille().getId() < 1 : true) {
                    getErrorMessage("Vous devez selectionner une famille");
                    return false;
                }
                List<YvsBaseArticles> articles = dao.loadNameQueries("YvsBaseArticles.findByFamilleActif", new String[]{"famille", "actif"}, new Object[]{new YvsBaseFamilleArticle(planTarif.getFamille().getId()), true});
                PlanTarifaireClient bean;
                YvsBasePlanTarifaire entity;
                boolean succes = false;
                for (YvsBaseArticles a : articles) {
                    for (YvsBaseConditionnement c : a.getConditionnements()) {
                        if (c.getByVente()) {
                            bean = new PlanTarifaireClient(0, planTarif);
                            bean.setFamille(null);
                            bean.setForArticle(true);
                            bean.setArticle(new Articles(a.getId(), a.getRefArt(), a.getDesignation()));
                            bean.setConditionnement(new Conditionnement(c.getId(), new UniteMesure(c.getUnite().getReference(), c.getUnite().getLibelle())));
                            bean.setCategorie(categorieClient);
                            entity = saveNewPlanTarifaire(bean, false);
                            if (entity != null ? entity.getId() > 0 : false) {
                                succes = true;
                            }
                        }
                    }
                }
                if (succes) {
                    succes();
                }
                resetFichePlanTarifaire();
                update("data_plan_tarifaire_client");
                update("data_categorie_client");
                return true;
            } else {
                YvsBasePlanTarifaire entity = saveNewPlanTarifaire(recopiewViewTarif(categorieClient), true);
                if (entity != null ? entity.getId() > 0 : false) {
                    succes();
                    resetFichePlanTarifaire();
                    update("data_plan_tarifaire_client");
                    update("data_categorie_client");
                    return true;
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            getException("Error (saveNewPlanTarifaire) : " + ex.getMessage(), ex);
        }
        return false;
    }

    public YvsBasePlanTarifaire saveNewPlanTarifaire(PlanTarifaireClient bean, boolean msg) {
        String action = bean.isUpdate() ? "Modification" : "Insertion";
        try {
            if (controleFicheTarif(bean, msg)) {
                YvsBasePlanTarifaire y = UtilCom.buildPlanTarifaireClient(bean, currentUser);
                if (bean.getId() <= 0) {
                    y.setId(null);
                    y = (YvsBasePlanTarifaire) dao.save1(y);
                    tarifs.add(0, y);
                } else {
                    dao.update(y);
                    int idx = tarifs.indexOf(y);
                    if (idx > -1) {
                        tarifs.set(idx, y);
                    }
                }
                return y;
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
        }
        return null;
    }

    public void chooseConditionnementTarif() {
        if (planTarif != null ? planTarif.getArticle() != null ? planTarif.getArticle().getId() > 0 : false : false) {
            if (planTarif.getId() < 1) {
                planTarif.setRemise(planTarif.getArticle().getRemise());
                planTarif.setPuv(planTarif.getArticle().getPuv());
            }
            if (planTarif.getConditionnement() != null ? planTarif.getConditionnement().getId() > 0 : false) {
                int idx = planTarif.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(planTarif.getConditionnement().getId()));
                if (idx > -1) {
                    YvsBaseConditionnement y = planTarif.getArticle().getConditionnements().get(idx);
                    if (planTarif.getId() < 1) {
                        planTarif.setPuv(y.getPrix());
                        planTarif.setPuvMin(y.getPrixMin());
                        planTarif.setRemise(y.getRemise());
                    }
                    planTarif.setConditionnement(UtilProd.buildBeanConditionnement(y));
                }
            }
        }
    }

    public boolean saveNewTranchePlanTarifaire() {
        String action = grille.isUpdate() ? "Modification" : "Insertion";
        try {
            if (!autoriser("base_article_save_tarifaire")) {
                openNotAcces();
                return false;
            }
            GrilleRabais bean = recopiewViewTrancheTarif();
            if (controleFicheTrancheTarif(bean)) {
                YvsBasePlanTarifaireTranche entity = UtilCom.buildGrilleTarifaireClient(bean, currentUser);
                if (!bean.isUpdate()) {
                    entity.setId(null);
                    dao.save1(entity);
                    grilles.add(0, entity);
                    if (tarifs.contains(tarifSelect)) {
                        tarifs.get(tarifs.indexOf(tarifSelect)).getGrilles().add(entity);
                    }
                } else {
                    dao.update(entity);
                    grilles.set(grilles.indexOf(entity), entity);
                }
                resetFicheTranchePlanTarifaire();
                succes();
                update("data_grille_plan_tarifaire_client");
                update("data_plan_tarifaire_client");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public void saveLiaisonClient() {
        if (categorieSelect != null ? categorieSelect.getId() > 0 : false) {
            List<YvsComClient> clients = dao.loadNameQueries("YvsComClient.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            if (clients != null ? !clients.isEmpty() : false) {
                for (YvsComClient c : clients) {
                    champ = new String[]{"categorie", "client"};
                    val = new Object[]{categorieSelect, c};
                    List<YvsComCategorieTarifaire> lc = dao.loadNameQueries("YvsComCategorieTarifaire.findByClientCategorie", champ, val, 0, 1);
                    if (lc != null ? lc.isEmpty() : true) {
                        YvsComCategorieTarifaire y = buildTarifaire(tarifaire, c);
                        champ = new String[]{"client"};
                        val = new Object[]{c};
                        lc = dao.loadNameQueries("YvsComCategorieTarifaire.findByClient", champ, val);
                        if (lc != null ? !lc.isEmpty() : false) {
                            for (YvsComCategorieTarifaire l : lc) {
                                if (y.getPriorite() <= l.getPriorite()) {
                                    l.setPriorite(l.getPriorite() + 1);
                                    dao.update(l);
                                }
                            }
                        }
                        y.setId(null);
                        dao.save(y);
                    }
                }
                succes();
            }
        } else {
            getErrorMessage("Vous devez selectionner la catégorie");
        }
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsBaseCategorieClient> list = new ArrayList<>();
                YvsBaseCategorieClient bean;
                for (Long ids : l) {
                    bean = categories.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);
                    categoriesParent.remove(bean);
                }

                categories.removeAll(list);
                succes();
                tabIds = "";
                resetFiche();
                update("data_categorie_client");
                update("select_categorie_parent_client");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsBaseCategorieClient y) {
        categorieSelect = y;
    }

    public void deleteBean_() {
        try {
            if (categorieSelect != null) {
                dao.delete(categorieSelect);
                categories.remove(categorieSelect);
                categoriesParent.remove(categorieSelect);
                if (categorieSelect.getId().equals(categorieClient.getId())) {
                    resetFiche();
                }
                succes();
                update("data_categorie_client");
                update("select_categorie_parent_client");
            } else {
                getErrorMessage("Vous devez selectionner la catégorie");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanPlanTarif() {
        try {
            if ((tabIds_tarif != null) ? !tabIds_tarif.equals("") : false) {
                String[] tab = tabIds_tarif.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsBasePlanTarifaire y = new YvsBasePlanTarifaire(id);
                    dao.delete(y);
                    tarifs.remove(y);
                }
                succes();
                update("data_plan_tarifaire_client");
                update("data_categorie_client");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanPlanTarif_(YvsBasePlanTarifaire y) {
        tarifSelect = y;
    }

    public void deleteBeanPlanTarif_() {
        try {
            if (tarifSelect != null) {
                dao.delete(tarifSelect);
                tarifs.remove(tarifSelect);
                succes();
                update("data_plan_tarifaire_client");
                update("data_categorie_client");
            } else {
                getErrorMessage("Vous devez selectionner le tarif");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanTranchePlanTarif() {
        try {
            if ((tabIds_grille != null) ? !tabIds_grille.equals("") : false) {
                String[] tab = tabIds_grille.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    dao.delete(new YvsBasePlanTarifaireTranche(id));
                    grilles.remove(new YvsBasePlanTarifaireTranche(id));
                    if (tarifs.contains(tarifSelect)) {
                        tarifs.get(tarifs.indexOf(tarifSelect)).getGrilles().remove(new YvsBasePlanTarifaireTranche(id));
                    }
                }
                succes();
                update("data_grille_plan_tarifaire_client");
                update("data_plan_tarifaire_client");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanTranchePlanTarif_(YvsBasePlanTarifaireTranche y) {
        if (!autoriser("base_article_save_tarifaire")) {
            openNotAcces();
            return;
        }
        grilleSelect = y;
    }

    public void deleteBeanTranchePlanTarif_() {
        try {
            if (!autoriser("base_article_save_tarifaire")) {
                openNotAcces();
                return;
            }
            if (grilleSelect != null) {
                dao.delete(grilleSelect);
                grilles.remove(grilleSelect);
                if (tarifs.contains(tarifSelect)) {
                    tarifs.get(tarifs.indexOf(tarifSelect)).getGrilles().remove(grilleSelect);
                }
                succes();
                update("data_grille_plan_tarifaire_client");
                update("data_plan_tarifaire_client");
            } else {
                getErrorMessage("Vous devez selectionner le tarif");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            YvsBaseCategorieClient y = (YvsBaseCategorieClient) ev.getObject();
            onSelectCategorie(y);
            tabIds = categories.indexOf(y) + "";
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        YvsBaseCategorieClient bean = (YvsBaseCategorieClient) ev.getObject();
        ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
        if (m != null) {
            if ((!bean.getModel().getActif()) && m.getModels().contains(bean.getModel())) {
                m.getModels().remove(bean.getModel());
                update("select_model_categorie");
            }
        }
        resetFiche();
        update("blog_form_categorie_client");
        update("form_plan_tarifaire_client");
    }

    public void loadOnViewModel(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBaseModelReglement y = (YvsBaseModelReglement) ev.getObject();
            categorieClient.setModel(UtilCom.buildBeanModelReglement(y));
            update("select_model_categorie");
        }
    }

    public void loadOnViewPlanTarif(SelectEvent ev) {
        if (ev != null && tarifSelect == null) {
            tarifSelect = (YvsBasePlanTarifaire) ev.getObject();
        }
        populateViewPlanTarif(UtilCom.buildBeanPlanTarifaireClient(tarifSelect));
        update("blog_form_plan_tarifaire_client");
    }

    public void unLoadOnViewPlanTarif(UnselectEvent ev) {
        resetFichePlanTarifaire();
        update("blog_form_plan_tarifaire_client");
    }

    public void loadOnViewTranchePlanTarif(SelectEvent ev) {
        if (ev != null) {
            YvsBasePlanTarifaireTranche y = (YvsBasePlanTarifaireTranche) ev.getObject();
            populateViewTranchePlanTarif(UtilCom.buildBeanGrilleTarifaireClient(y));
            update("form_grille_plan_tarifaire_client");
        }
    }

    public void unLoadOnViewTranchePlanTarif(UnselectEvent ev) {
        resetFicheTranchePlanTarifaire();
        update("form_grille_plan_tarifaire_client");
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            planTarif.setArticle(UtilCom.buildBeanArticle(bean));
            if (!planTarif.isUpdate()) {
                planTarif.setPuv(planTarif.getArticle().getPuv());
                planTarif.setRemise(planTarif.getArticle().getRemise());
            }
            bean.setNew_(true);
            ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
            if (m != null) {
                m.getArticlesResult().set(m.getArticlesResult().indexOf(bean), bean);
            }
            update("article_plan_tarifaire_client");
        }
    }

    public void unloadOnViewArticle(UnselectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            if (artiolesSelect != null ? !artiolesSelect.isEmpty() : false) {
                YvsBaseArticles a = artiolesSelect.get(artiolesSelect.size() - 1);
                planTarif.setArticle(UtilCom.buildBeanArticle(a));
            } else {
                planTarif.setArticle(new Articles());
            }
            planTarif.setPuv(planTarif.getArticle().getPuv());
            planTarif.setRemise(planTarif.getArticle().getRemise());

            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            bean.setNew_(false);
            ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
            if (m != null) {
                m.getArticlesResult().set(m.getArticlesResult().indexOf(bean), bean);
            }
            update("article_plan_tarifaire_client");
        }
    }

    public void onSelectCategorie(YvsBaseCategorieClient ev) {
        categorieSelect = ev;
        populateView(UtilCom.buildBeanCategorieClient(categorieSelect));
        initTarif(categorieSelect);
        categoriesParent.clear();
        categoriesParent.addAll(categories);
        if (categoriesParent.contains(categorieSelect)) {
            categoriesParent.remove(categorieSelect);
        }
        loadParents(ev);
        ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
        if (m != null) {
            if (categorieSelect.getModel() != null ? !m.getModels().contains(categorieSelect.getModel()) : false) {
                m.getModels().add(0, categorieSelect.getModel());
                update("select_model_categorie");
            }
        }
        update("blog_form_categorie_client");
        update("form_plan_tarifaire_client");
    }

    public void initArticles() {
        ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
        if (m != null) {
            m.initArticles("V", planTarif.getArticle());
            listArt = planTarif.getArticle().isListArt();
        }
        update("data_articles_categorie_client");
    }

    public void initTarif(YvsBaseCategorieClient y) {
        if (y != null ? y.getId() > 0 : false) {
            categorieSelect = y;
            loadTarif(true, true);
            update("data_plan_tarifaire_client");
            update("article_plan_tarifaire_client");
        } else {
            getErrorMessage("Vous devez selectionner la catégorie");
        }
    }

    public void initLiaison(YvsBaseCategorieClient bean) {
        tarifaire = new CategorieTarifaire();
        priorites.clear();
        categorieSelect = bean;

        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        List<YvsComCategorieTarifaire> lc = dao.loadNameQueries("YvsComCategorieTarifaire.findAll", champ, val);
        if (lc != null ? !lc.isEmpty() : false) {
            for (YvsComCategorieTarifaire c : lc) {
                if (!priorites.contains(c.getPriorite())) {
                    priorites.add(c.getPriorite());
                }
            }
            priorites.add(priorites.get(0) + 1);
            Collections.sort(priorites);
        } else {
            priorites.add(1);
        }
        tarifaire.setPriorite(priorites.get(0));
        update("form_categorie_tarifaire_categorie");
    }

    public void initGrille(YvsBasePlanTarifaire y) {
        tarifSelect = y;
        resetFicheTranchePlanTarifaire();
        loadGrille(y);
    }

    public void activePlanTarifaire(YvsBasePlanTarifaire bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            String rq = "UPDATE yvs_base_plan_tarifaire SET actif=" + bean.getActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            tarifs.set(tarifs.indexOf(bean), bean);
            update("data_categorie_client");
        }
    }

    public void activeDefaut(YvsBaseCategorieClient bean) {
        if (bean != null) {
            bean.setDefaut(!bean.getDefaut());
            if (bean.getDefaut()) {
                for (YvsBaseCategorieClient c : categories) {
                    if (c.getDefaut() && !c.equals(bean)) {
                        bean.setDefaut(false);
                        getErrorMessage("Il existe déja une catégorie pas défaut");
                        return;
                    }
                }
            }
            categories.set(categories.indexOf(bean), bean);
            String rq = "UPDATE yvs_base_categorie_client SET defaut=" + bean.getDefaut() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
        }
    }

    public void genererPrix() {
        double puv = planTarif.getPuv();
        if (planTarif.getNatureCoefAugmentation().equals(Constantes.NATURE_TAUX)) {
            puv = puv + ((puv * planTarif.getCoefAugmentation()) > 0 ? ((puv * planTarif.getCoefAugmentation()) / 100) : 0);
        } else {
            puv = puv + planTarif.getCoefAugmentation();
        }
        planTarif.setPuvMin(puv - (planTarif.getPuv() - planTarif.getPuvMin()));
        planTarif.setPuv(puv);
    }

    public void applyCoefToAllSelection() {
        for (YvsBasePlanTarifaire ct : tarifs) {
            ct.setCoefAugmentation(planTarif.getCoefAugmentation());
            dao.update(ct);
        }
        update("data_plan_tarifaire_client");
        succes();
    }

    public void applyPrixMinToAllSelection() {
        for (YvsBasePlanTarifaire ct : tarifs) {
            if (planTarif.getNaturePrixMin().equals(Constantes.NATURE_TAUX)) {
                ct.setPuvMin(ct.getPuv() - (ct.getPuv() * planTarif.getPuvMin() / 100));
            } else {
                ct.setPuvMin(ct.getPuv() - (planTarif.getPuvMin()));
            }
            dao.update(ct);
        }
        update("data_plan_tarifaire_client");
        succes();
    }

    public void selectModel(YvsBaseModelReglement y) {
        ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
        if (m != null) {
            m.setModelSelect(y);
            m._populateView(UtilCom.buildBeanModelReglement(y));
            m.resetFicheTranche();
            update("blog_form_model_categorie");
        }
    }

    public void searchArticle() {
        String num = planTarif.getArticle().getRefArt();
        if ((num != null) ? !num.trim().isEmpty() : false) {
            planTarif.getArticle().setDesignation("");
            planTarif.getArticle().setError(true);
            planTarif.getArticle().setId(0);
            ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
            if (m != null) {
                Articles y = m.searchArticleActif("V", num, true);
                if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                    if (m.getArticlesResult().size() > 1) {
                        update("data_articles_categorie_client");
                        update("data_articles_client");
                    } else {
                        planTarif.setArticle(y);
                        if (planTarif.getPuv() < 1) {
                            searchArticleParent(planTarif);
                        }
                    }
                    planTarif.getArticle().setError(false);
                }
                listArt = y.isListArt();
            }
        }
    }

    public void searchArticleParent(PlanTarifaireClient y) {
        if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
            if (categorieClient.getParent() != null ? categorieClient.getParent().getId() > 0 : false) {
                if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                    champ = new String[]{"parent", "article"};
                    val = new Object[]{new YvsBaseCategorieClient(categorieClient.getParent().getId()), new YvsBaseArticles(y.getArticle().getId())};
                    nameQueri = "YvsBasePlanTarifaire.findByParentArticle";
                } else {
                    champ = new String[]{"parent", "famille"};
                    val = new Object[]{new YvsBaseCategorieClient(categorieClient.getParent().getId()), new YvsBaseFamilleArticle(y.getFamille().getId())};
                    nameQueri = "YvsBasePlanTarifaire.findByParentFamille";
                }
                List<YvsBasePlanTarifaire> lt = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
                if (lt != null ? !lt.isEmpty() : false) {
                    planTarif.setPuv(lt.get(0).getPuv());
                    planTarif.setRemise(lt.get(0).getRemise());
                    return;
                }
            }
            planTarif.setPuv(y.getArticle().getPuv());
            planTarif.setRemise(y.getArticle().getRemise());
        }
    }

    public void changeByElement() {
        if (planTarif.isForArticle()) {
            planTarif.setFamille(new FamilleArticle());
        } else {
            planTarif.setArticle(new Articles());
        }
        planTarif.setApplyOnArticle(true);
    }

    public void chooseFamille() {
        if (planTarif.getFamille() != null ? planTarif.getFamille().getId() > 0 : false) {
            ManagedFamilleArticle w = (ManagedFamilleArticle) giveManagedBean(ManagedFamilleArticle.class);
            if (w != null) {
                int idx = w.getFamilles().indexOf(new YvsBaseFamilleArticle(planTarif.getFamille().getId()));
                if (idx > -1) {
                    planTarif.setFamille(UtilProd.buildBeanFamilleArticle(w.getFamilles().get(idx)));
                }
            }
        }
    }

    public void addCoefficient() {
        try {
            if ((tabIds_tarif != null) ? !tabIds_tarif.equals("") : false) {
                String[] tab = tabIds_tarif.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    if (tarifs.contains(new YvsBasePlanTarifaire(id))) {
                        YvsBasePlanTarifaire a = tarifs.get(tarifs.indexOf(new YvsBasePlanTarifaire(id)));
                        a.setCoefAugmentation(coefficient);
                        a.setNatureCoefAugmentation(natureCoefficient);
                        a.setAuthor(currentUser);
                        a.setSelectActif(false);
                        dao.update(a);
                        tarifs.set(tarifs.indexOf(a), a);
                    }
                }
                tabIds_tarif = "";
                coefficient = 0;
                natureCoefficient = Constantes.NATURE_MTANT;
                update("data_plan_tarifaire_client");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void addAllArticle() {
        ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
        if (m != null) {
            if (addAllArt) {
                for (YvsBaseArticles a : m.getArticlesResult()) {
                    if (!artiolesSelect.contains(a)) {
                        artiolesSelect.add(a);
                        a.setNew_(true);
                    }
                }
            } else {
                artiolesSelect.clear();
                for (YvsBaseArticles a : m.getArticlesResult()) {
                    a.setNew_(false);
                }
            }
        }
    }

    public void addOneArticle(YvsBaseArticles ev) {
        if (artiolesSelect.contains(ev)) {
            artiolesSelect.remove(ev);
            ev.setNew_(false);
        } else {
            artiolesSelect.add(ev);
            ev.setNew_(true);
        }
        ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
        if (m != null) {
            m.getArticlesResult().set(m.getArticlesResult().indexOf(ev), ev);
            addAllArt = artiolesSelect.size() == m.getArticlesResult().size();
        }
    }

    public void addArticles() {
        if (categorieClient != null ? categorieClient.getId() > 0 : false) {
            if (artiolesSelect != null ? !artiolesSelect.isEmpty() : false) {
                if (artiolesSelect.size() > 2) {
                    for (YvsBaseArticles a : artiolesSelect) {
                        YvsBasePlanTarifaire p = new YvsBasePlanTarifaire();
                        p.setActif(true);
                        p.setAuthor(currentUser);
                        p.setCategorie(new YvsBaseCategorieClient(categorieClient.getId()));
                        p.setArticle(a);
                        p.setCoefAugmentation(0.0);
                        p.setNatureCoefAugmentation(Constantes.NATURE_MTANT);
                        p.setNew_(true);
                        p.setPuv(a.getPuv());
                        p.setRemise(a.getRemise());
                        p.setPuvMin(a.getPrixMin());
                        p = (YvsBasePlanTarifaire) dao.save1(p);
                        tarifs.add(p);
                    }
                    succes();
                }
                listArt = false;
                update("data_articles_categorie_client");
            }
        } else {
            getErrorMessage("Vous devez precisez la catégorie");
        }
    }

    public void getProgatationPrix(YvsBaseCategorieClient y) {
        try {
            progatationPrix(y);
            succes();
        } catch (Exception ex) {
            getErrorMessage("Propagation Générale impossible");
            System.err.println("Error " + ex.getMessage());
        }
    }

    public void getRecalculerPrix(YvsBaseCategorieClient y) {
        try {
            recalculerPrix(y);
            succes();
        } catch (Exception ex) {
            getErrorMessage("Propagation Générale impossible");
            System.err.println("Error " + ex.getMessage());
        }
    }

    private void progatationPrix(YvsBaseCategorieClient y) {
        champ = new String[]{"categorie"};
        val = new Object[]{y};
        nameQueri = "YvsBasePlanTarifaire.findByCategorie";
        List<YvsBasePlanTarifaire> lt = dao.loadNameQueries(nameQueri, champ, val);
        if (lt != null ? !lt.isEmpty() : false) {
            for (YvsBasePlanTarifaire t : lt) {
                _progatationPrix(t);
            }
        }
        for (YvsBaseCategorieClient c : y.getFils()) {
            progatationPrix(c);
        }
    }

    private void recalculerPrix(YvsBaseCategorieClient y) {
        champ = new String[]{"categorie"};
        val = new Object[]{y};
        nameQueri = "YvsBasePlanTarifaire.findByCategorie";
        List<YvsBasePlanTarifaire> lt = dao.loadNameQueries(nameQueri, champ, val);
        if (lt != null ? !lt.isEmpty() : false) {
            for (YvsBasePlanTarifaire t : lt) {
                _recalculerPrix(t);
            }
        }
    }

    public void _getProgatationPrix(YvsBasePlanTarifaire y) {
        try {
            _progatationPrix(y);
            succes();
        } catch (Exception ex) {
            getErrorMessage("Propagation impossible");
            System.err.println("Error " + ex.getMessage());
        }
    }

    public void _getRecalculerPrix(YvsBasePlanTarifaire y) {
        try {
            _recalculerPrix(y);
            succes();
        } catch (Exception ex) {
            getErrorMessage("Propagation impossible");
            System.err.println("Error " + ex.getMessage());
        }
    }

    private void _progatationPrix(YvsBasePlanTarifaire entree) {
        if (entree != null ? entree.getId() > 0 : false) {
            if (entree.getCategorie() != null ? entree.getCategorie().getId() > 0 : false) {
                champ = new String[]{"parent"};
                val = new Object[]{entree.getCategorie()};
                nameQueri = "YvsBaseCategorieClient.findByParent";
                List<YvsBaseCategorieClient> lc = dao.loadNameQueries(nameQueri, champ, val);
                if (lc != null ? !lc.isEmpty() : false) {
                    for (YvsBaseCategorieClient c : lc) {
                        if (entree.getArticle() != null ? entree.getArticle().getId() > 0 : false) {
                            val = new Object[]{entree.getArticle(), c};
                            champ = new String[]{"article", "categorie"};
                            nameQueri = "YvsBasePlanTarifaire.findByArticleCategorie";
                        } else {
                            val = new Object[]{entree.getFamille(), c};
                            champ = new String[]{"famille", "categorie"};
                            nameQueri = "YvsBasePlanTarifaire.findByFamilleCategorie";
                        }
                        List<YvsBasePlanTarifaire> lt = dao.loadNameQueries(nameQueri, champ, val);
                        for (YvsBasePlanTarifaire sortie : lt) {
                            definedSousPrix(entree, sortie);
                        }
                    }
                }
                int idx = categories.indexOf(entree.getCategorie());
                if (idx > -1) {
                    YvsBaseCategorieClient c = categories.get(idx);
                    if (c != null ? c.getId() > 0 : false) {
                        for (YvsBaseCategorieClient f : c.getFils()) {
                            if (entree.getArticle() != null ? entree.getArticle().getId() > 0 : false) {
                                val = new Object[]{entree.getArticle(), f};
                                champ = new String[]{"article", "categorie"};
                                nameQueri = "YvsBasePlanTarifaire.findByArticleCategorie";
                            } else {
                                val = new Object[]{entree.getFamille(), f};
                                champ = new String[]{"famille", "categorie"};
                                nameQueri = "YvsBasePlanTarifaire.findByFamilleCategorie";
                            }
                            List<YvsBasePlanTarifaire> lt = dao.loadNameQueries(nameQueri, champ, val);
                            for (YvsBasePlanTarifaire t : lt) {
                                _progatationPrix(t);
                            }
                        }
                    }
                }
            }
        }
    }

    private void definedSousPrix(YvsBasePlanTarifaire entree, YvsBasePlanTarifaire sortie) {
        if ((entree != null ? entree.getId() > 0 : false) && (sortie != null ? sortie.getId() > 0 : false)) {
            double puv = entree.getPuv();
            if (sortie.getNatureCoefAugmentation().equals(Constantes.NATURE_TAUX)) {
                puv = puv + ((puv * sortie.getCoefAugmentation()) / 100);
            } else {
                puv += sortie.getCoefAugmentation();
            }
            sortie.setPuvMin(puv - (sortie.getPuv() - sortie.getPuvMin()));
            sortie.setPuv(puv);
            sortie.setAuthor(currentUser);
            dao.update(sortie);
            for (YvsBasePlanTarifaireTranche g : sortie.getGrilles()) {
                g.setPuv(g.getPuv() + (sortie.getPuv() - g.getPuv()));
                g.setAuthor(currentUser);
                dao.update(g);
            }
        }
    }

    private void _recalculerPrix(YvsBasePlanTarifaire sortie) {
        if (sortie != null ? sortie.getId() > 0 : false) {
            if (sortie.getCategorie() != null ? sortie.getCategorie().getId() > 0 : false) {
                champ = new String[]{"id"};
                val = new Object[]{sortie.getCategorie().getId()};
                nameQueri = "YvsBaseCategorieClient.findById";
                YvsBaseCategorieClient ca = (YvsBaseCategorieClient) dao.loadOneByNameQueries(nameQueri, champ, val);
                if (ca.getParent() != null ? ca.getParent().getId() > 0 : false) {
                    if (sortie.getArticle() != null ? sortie.getArticle().getId() > 0 : false) {
                        champ = new String[]{"article", "categorie"};
                        val = new Object[]{sortie.getArticle(), ca.getParent()};
                        nameQueri = "YvsBasePlanTarifaire.findByArticleCategorie";
                    } else {
                        champ = new String[]{"famille", "categorie"};
                        val = new Object[]{sortie.getFamille(), ca.getParent()};
                        nameQueri = "YvsBasePlanTarifaire.findByFamilleCategorie";
                    }
                    YvsBasePlanTarifaire entree = (YvsBasePlanTarifaire) dao.loadOneByNameQueries(nameQueri, champ, val);
                    if (entree != null ? entree.getId() > 0 : false) {
                        definedSousPrix(entree, sortie);
                    }
                }
            }
        }
    }

    public void copierTarifsParent(YvsBaseCategorieClient y) {
        if (y != null ? y.getId() > 0 : false) {
            if (y.getParent() != null ? y.getParent().getId() > 0 : false) {
                champ = new String[]{"categorie"};
                val = new Object[]{y.getParent()};
                List<YvsBasePlanTarifaire> lp = dao.loadNameQueries("YvsBasePlanTarifaire.findByCategorie", champ, val);
                for (YvsBasePlanTarifaire p : lp) {
                    if (p.getArticle() != null ? p.getArticle().getId() > 0 : false) {
                        champ = new String[]{"categorie", "article"};
                        val = new Object[]{y, p.getArticle()};
                        nameQueri = "YvsBasePlanTarifaire.findByArticleCategorie";
                    } else {
                        champ = new String[]{"categorie", "famille"};
                        val = new Object[]{y, p.getFamille()};
                        nameQueri = "YvsBasePlanTarifaire.findByFamilleCategorie";
                    }
                    List<YvsBasePlanTarifaire> lp_ = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
                    if (lp_ != null ? lp_.isEmpty() : true) {
                        p.setCategorie(y);
                        p.setAuthor(currentUser);
                        p.setId(null);
                        dao.save(p);
                        tarifs.add(p);
                    }
                }
                succes();
            }
        }
    }

    public void cloneTarifsParent(boolean cascade) {
        if (categorieSelect != null ? categorieSelect.getId() > 0 : false) {
            champ = new String[]{"categorie"};
            val = new Object[]{categorieSelect};
            List<YvsBasePlanTarifaire> lp = dao.loadNameQueries("YvsBasePlanTarifaire.findByCategorie", champ, val);
            for (YvsBasePlanTarifaire p : lp) {
                for (YvsBaseCategorieClient y : categorieSelect.getFils()) {
                    if (p.getArticle() != null ? p.getArticle().getId() > 0 : false) {
                        champ = new String[]{"categorie", "article"};
                        val = new Object[]{y, p.getArticle()};
                        nameQueri = "YvsBasePlanTarifaire.findByArticleCategorie";
                    } else {
                        champ = new String[]{"categorie", "famille"};
                        val = new Object[]{y, p.getFamille()};
                        nameQueri = "YvsBasePlanTarifaire.findByFamilleCategorie";
                    }
                    List<YvsBasePlanTarifaire> lp_ = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
                    if (lp_ != null ? lp_.isEmpty() : true) {
                        p.setCategorie(y);
                        p.setAuthor(currentUser);
                        p.setId(null);
                        dao.save(p);
                    }
                }
            }
            if (cascade) {
                for (YvsBaseCategorieClient y : categorieSelect.getFils()) {
                    _cloneTarifParent(y);
                }
            }
            succes();
        }
    }

    private void _cloneTarifParent(YvsBaseCategorieClient bean) {
        if (bean != null ? bean.getId() > 0 : false) {
            champ = new String[]{"categorie"};
            val = new Object[]{bean};
            List<YvsBasePlanTarifaire> lp = dao.loadNameQueries("YvsBasePlanTarifaire.findByCategorie", champ, val);
            for (YvsBasePlanTarifaire p : lp) {
                for (YvsBaseCategorieClient y : bean.getFils()) {
                    if (p.getArticle() != null ? p.getArticle().getId() > 0 : false) {
                        champ = new String[]{"categorie", "article"};
                        val = new Object[]{y, p.getArticle()};
                        nameQueri = "YvsBasePlanTarifaire.findByArticleCategorie";
                    } else {
                        champ = new String[]{"categorie", "famille"};
                        val = new Object[]{y, p.getFamille()};
                        nameQueri = "YvsBasePlanTarifaire.findByFamilleCategorie";
                    }
                    List<YvsBasePlanTarifaire> lp_ = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
                    if (lp_ != null ? lp_.isEmpty() : true) {
                        p.setCategorie(y);
                        p.setAuthor(currentUser);
                        p.setId(null);
                        dao.save(p);
                    }
                }
            }
            for (YvsBaseCategorieClient y : bean.getFils()) {
                _cloneTarifParent(y);
            }
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
