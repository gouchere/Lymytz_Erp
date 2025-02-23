/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import yvs.production.UtilProd;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.ArticleTaxe;
import yvs.base.compta.ManagedCompte;
import yvs.commercial.ManagedCatCompt;
import yvs.commercial.ManagedTaxes;
import yvs.commercial.UtilCom;
import yvs.commercial.client.ManagedCategorieClt;
import yvs.commercial.client.PlanTarifaireClient;
import yvs.commercial.rrr.GrilleRabais;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.base.YvsBaseArticleCategorieComptableTaxe;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseClassesStat;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsBasePlanTarifaire;
import yvs.entity.commercial.client.YvsBasePlanTarifaireTranche;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.prod.YvsBaseArticlesTemplate;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.entity.produits.group.YvsBaseGroupesArticle;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean()
@SessionScoped
public class ManagedTemplateArticle extends Managed<TemplateArticles, YvsBaseArticlesTemplate> implements Serializable {

    @ManagedProperty(value = "#{templateArticles}")
    private TemplateArticles templateArticles;
    TemplateArticles temp = new TemplateArticles();
    private List<YvsBaseArticlesTemplate> templates;
    private YvsBaseArticlesTemplate selecTemplate;

    private ArticlesCatComptable compte = new ArticlesCatComptable();
    private YvsBaseArticleCategorieComptable selectCompte;
    List<YvsBaseArticleCategorieComptable> tempComptes;
    private ArticleTaxe taxe = new ArticleTaxe();
    private YvsBaseArticleCategorieComptableTaxe selectTaxe;

    private PlanTarifaireClient plan = new PlanTarifaireClient();
    private YvsBasePlanTarifaire selectPlan;
    List<YvsBasePlanTarifaire> tempPlans;
    private GrilleRabais tarifs = new GrilleRabais();
    private YvsBasePlanTarifaireTranche selectTarifs;

    private String tabIds, tabIds_plan, tabIds_tarifs, tabIds_compte, tabIds_taxe;
    private String unite = Constantes.UNITE_QUANTITE;
    private boolean stockage = true;

    private String[] typesUnites = new String[]{Constantes.UNITE_QUANTITE, Constantes.UNITE_QUANTITE, Constantes.UNITE_QUANTITE};
    private long groupeSearch, familleSearch, uniteSearch;
    private String categorieSearch, numSearch;
    private Boolean actifSearch;

    private List<YvsBaseArticles> articles;
    private List<YvsBaseArticles> selectArticles;

    public ManagedTemplateArticle() {
        templates = new ArrayList<>();
        articles = new ArrayList<>();
        selectArticles = new ArrayList<>();
        tempComptes = new ArrayList<>();
        tempPlans = new ArrayList<>();
    }

    public List<YvsBaseArticles> getSelectArticles() {
        return selectArticles;
    }

    public void setSelectArticles(List<YvsBaseArticles> selectArticles) {
        this.selectArticles = selectArticles;
    }

    public boolean isStockage() {
        return stockage;
    }

    public void setStockage(boolean stockage) {
        this.stockage = stockage;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public List<YvsBaseArticles> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticles> articles) {
        this.articles = articles;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public long getGroupeSearch() {
        return groupeSearch;
    }

    public void setGroupeSearch(long groupeSearch) {
        this.groupeSearch = groupeSearch;
    }

    public long getFamilleSearch() {
        return familleSearch;
    }

    public void setFamilleSearch(long familleSearch) {
        this.familleSearch = familleSearch;
    }

    public long getUniteSearch() {
        return uniteSearch;
    }

    public void setUniteSearch(long uniteSearch) {
        this.uniteSearch = uniteSearch;
    }

    public String getCategorieSearch() {
        return categorieSearch;
    }

    public void setCategorieSearch(String categorieSearch) {
        this.categorieSearch = categorieSearch;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public String[] getTypesUnites() {
        return typesUnites;
    }

    public void setTypesUnites(String[] typesUnites) {
        this.typesUnites = typesUnites;
    }

    public String getTabIds_tarifs() {
        return tabIds_tarifs;
    }

    public void setTabIds_tarifs(String tabIds_tarifs) {
        this.tabIds_tarifs = tabIds_tarifs;
    }

    public String getTabIds_taxe() {
        return tabIds_taxe;
    }

    public void setTabIds_taxe(String tabIds_taxe) {
        this.tabIds_taxe = tabIds_taxe;
    }

    public TemplateArticles getTemplateArticles() {
        return templateArticles;
    }

    public void setTemplateArticles(TemplateArticles templateArticles) {
        this.templateArticles = templateArticles;
    }

    public List<YvsBaseArticlesTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<YvsBaseArticlesTemplate> templates) {
        this.templates = templates;
    }

    public YvsBaseArticlesTemplate getSelecTemplate() {
        return selecTemplate;
    }

    public void setSelecTemplate(YvsBaseArticlesTemplate selecTemplate) {
        this.selecTemplate = selecTemplate;
    }

    public ArticlesCatComptable getCompte() {
        return compte;
    }

    public void setCompte(ArticlesCatComptable compte) {
        this.compte = compte;
    }

    public YvsBaseArticleCategorieComptable getSelectCompte() {
        return selectCompte;
    }

    public void setSelectCompte(YvsBaseArticleCategorieComptable selectCompte) {
        this.selectCompte = selectCompte;
    }

    public ArticleTaxe getTaxe() {
        return taxe;
    }

    public void setTaxe(ArticleTaxe taxe) {
        this.taxe = taxe;
    }

    public YvsBaseArticleCategorieComptableTaxe getSelectTaxe() {
        return selectTaxe;
    }

    public void setSelectTaxe(YvsBaseArticleCategorieComptableTaxe selectTaxe) {
        this.selectTaxe = selectTaxe;
    }

    public PlanTarifaireClient getPlan() {
        return plan;
    }

    public void setPlan(PlanTarifaireClient plan) {
        this.plan = plan;
    }

    public YvsBasePlanTarifaire getSelectPlan() {
        return selectPlan;
    }

    public void setSelectPlan(YvsBasePlanTarifaire selectPlan) {
        this.selectPlan = selectPlan;
    }

    public GrilleRabais getTarifs() {
        return tarifs;
    }

    public void setTarifs(GrilleRabais tarifs) {
        this.tarifs = tarifs;
    }

    public YvsBasePlanTarifaireTranche getSelectTarifs() {
        return selectTarifs;
    }

    public void setSelectTarifs(YvsBasePlanTarifaireTranche selectTarifs) {
        this.selectTarifs = selectTarifs;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getTabIds_plan() {
        return tabIds_plan;
    }

    public void setTabIds_plan(String tabIds_plan) {
        this.tabIds_plan = tabIds_plan;
    }

    public String getTabIds_compte() {
        return tabIds_compte;
    }

    public void setTabIds_compte(String tabIds_compte) {
        this.tabIds_compte = tabIds_compte;
    }

    public TemplateArticles getTemp() {
        return temp;
    }

    public void setTemp(TemplateArticles temp) {
        this.temp = temp;
    }

    @Override
    public void loadAll() {
        loadAll(true, true);
    }

    public void loadAlls() {
        templates = dao.loadNameQueries("YvsArticlesTemplate.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAll(boolean avance, boolean init, boolean count) {
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        templates = paginator.executeDynamicQuery(count, "y", "y", "YvsBaseArticlesTemplate y LEFT JOIN FETCH y.famille LEFT JOIN FETCH y.articles", "y.refArt", avance, init, (int) imax, "id", dao);
    }

    public void loadAll(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        templates = paginator.executeDynamicQuery("y", "y", "YvsBaseArticlesTemplate y LEFT JOIN FETCH y.famille LEFT JOIN FETCH y.groupe LEFT JOIN FETCH y.classe", "y.refArt", avance, init, (int) imax, dao);
    }

    @Override
    public boolean controleFiche(TemplateArticles bean) {
        if (bean == null) {
            getErrorMessage("Action impossible");
            return false;
        }
        if (bean.getDescription() != null ? bean.getDescription().trim().length() < 1 : true) {
            getErrorMessage("Vous devez preciser le titre");
            return false;
        }
        return true;
    }

    public boolean controleFiche(ArticlesCatComptable bean) {
        if (bean == null) {
            getErrorMessage("Action impossible");
            return false;
        }
        if (bean.getTemplate() != null ? bean.getTemplate().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le template");
            return false;
        }
        if (bean.getCategorie() != null ? bean.getCategorie().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser la catégorie");
            return false;
        }
        if (bean.getCompte() != null ? bean.getCompte().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le compte");
            return false;
        }
        return true;
    }

    public boolean controleFiche(ArticleTaxe bean) {
        if (bean == null) {
            getErrorMessage("Action impossible");
            return false;
        }
        if (bean.getTaxe() != null ? bean.getTaxe().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le template");
            return false;
        }
        if (bean.getArticle() != null ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser la catégorie");
            return false;
        }
        champ = new String[]{"article", "taxe"};
        val = new Object[]{new YvsBaseArticleCategorieComptable(bean.getArticle().getId()), new YvsBaseTaxes(bean.getTaxe().getId())};
        nameQueri = "YvsBaseArticleCategorieComptableTaxe.findByArticleTaxe";
        YvsBaseArticleCategorieComptableTaxe y = (YvsBaseArticleCategorieComptableTaxe) dao.loadOneByNameQueries(nameQueri, champ, val);
        if (y != null ? (y.getId() != null ? !y.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez deja associé cette taxe");
            return false;
        }
        return true;
    }

    public boolean controleFiche(PlanTarifaireClient bean) {
        if (bean == null) {
            getErrorMessage("Action impossible");
            return false;
        }
        if (bean.getTemplate() != null ? bean.getTemplate().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le template");
            return false;
        }
        if (bean.getCategorie() != null ? bean.getCategorie().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser la catégorie");
            return false;
        }
        return true;
    }

    public boolean controleFiche(GrilleRabais bean) {
        if (bean == null) {
            getErrorMessage("Action impossible");
            return false;
        }
        if (bean.getParent() < 1) {
            getErrorMessage("Vous devez preciser le plan tarifaire");
            return false;
        }
        return true;
    }

    @Override
    public TemplateArticles recopieView() {
        if (templateArticles != null) {
            ManagedFamilleArticle mf = (ManagedFamilleArticle) giveManagedBean(ManagedFamilleArticle.class);
            if (mf != null) {
                int idx = mf.getFamilles().indexOf(new YvsBaseFamilleArticle(templateArticles.getFamille().getId()));
                if (idx > -1) {
                    YvsBaseFamilleArticle f = mf.getFamilles().get(idx);
                    templateArticles.setFamille(UtilProd.buildBeanFamilleArticle(f));
                }
            }
        }
        return templateArticles;
    }

    public ArticlesCatComptable recopieViewCompte(TemplateArticles y) {
        ArticlesCatComptable re = new ArticlesCatComptable();
        re.setId(compte.getId());
        re.setCategorie(compte.getCategorie());
        re.setCompte(compte.getCompte());
        re.setTemplate(y);
        re.setTaxes(compte.getTaxes());
        return re;
    }

    public PlanTarifaireClient recopieViewPlan(TemplateArticles y) {
        PlanTarifaireClient c = new PlanTarifaireClient();
        if (y != null) {
            c.setId(plan.getId());
            c.setTemplate(y);
            c.setCategorie(plan.getCategorie());
            c.setPuv(plan.getPuv());
            c.setPuvMin(plan.getPuvMin());
            c.setRemise(plan.getRemise());
            c.setRistourne(plan.getRistourne());
            c.setActif(plan.isActif());
            c.setCoefAugmentation(plan.getCoefAugmentation());
            c.setNatureCoefAugmentation(plan.getNatureCoefAugmentation());
            c.setNatureRemise(plan.getNatureRemise());
            c.setNatureRistourne(plan.getNatureRistourne());
            c.setUpdate(true);
        }
        return c;
    }

    public GrilleRabais recopieViewTarifs() {
        GrilleRabais c = new GrilleRabais();
        c.setId(tarifs.getId());
        c.setBase(tarifs.getBase());
        if (tarifs.isUnique()) {
            c.setMontantMinimal(0);
            c.setMontantMaximal(Double.MAX_VALUE);
        } else {
            c.setMontantMaximal(tarifs.getMontantMaximal());
            c.setMontantMinimal(tarifs.getMontantMinimal());
        }
        c.setMontantRabais(tarifs.getMontantRabais());
        c.setNatureMontant(tarifs.getNatureMontant());
        c.setParent(tarifs.isUpdate() ? tarifs.getParent() : selectPlan.getId());
        c.setValeur(tarifs.getValeur());
        c.setUpdate(tarifs.isUpdate());
        c.setNew_(true);
        return c;
    }

    @Override
    public void populateView(TemplateArticles bean) {
        cloneObject(templateArticles, bean);
        cloneObject(temp, bean);

        articles.clear();
        articles.addAll(bean.getArticles());
        tempComptes.clear();
        tempComptes.addAll(bean.getComptes());
        tempPlans.clear();
        tempPlans.addAll(bean.getTarifaires());
    }

    public void populateView(ArticlesCatComptable bean) {
        cloneObject(compte, bean);
    }

    public void populateView(ArticleTaxe bean) {
        cloneObject(taxe, bean);
    }

    public void populateView(PlanTarifaireClient bean) {
        cloneObject(plan, bean);
    }

    public void populateView(GrilleRabais bean) {
        cloneObject(tarifs, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(templateArticles);
        templateArticles.setGroupe(new GroupeArticle());
        templateArticles.setClasse(new ClassesStat());
        templateArticles.setFamille(new FamilleArticle());
        templateArticles.setTarifaires(new ArrayList<YvsBasePlanTarifaire>());
        templateArticles.setComptes(new ArrayList<YvsBaseArticleCategorieComptable>());

        selecTemplate = null;
        tabIds = "";

        resetFichePlan();
        resetFicheCompte();
        update("blog_form_template_article");
        update("save_template_article");
    }

    public void resetFichePlan() {
        plan = new PlanTarifaireClient();
        selectPlan = null;
        tabIds_plan = "";
        resetFicheTarifs();
    }

    public void resetFicheTarifs() {
        tarifs = new GrilleRabais();
        selectTarifs = null;
        tabIds_tarifs = "";
    }

    public void resetFicheCompte() {
        compte = new ArticlesCatComptable();
        selectCompte = null;
        tabIds_compte = "";
        resetFicheTaxe();
    }

    public void resetFicheTaxe() {
        taxe = new ArticleTaxe();
        selectTaxe = null;
        tabIds_taxe = "";
    }

    @Override
    public boolean saveNew() {
        TemplateArticles bean = recopieView();
        boolean update = bean.getId() > 0;
        selecTemplate = saveNew(bean);
        if (selecTemplate != null ? selecTemplate.getId() != null ? selecTemplate.getId() > 0 : false : false) {
            templateArticles.setId(bean.getId());
            if (update && !bean.getArticles().isEmpty()) {
                openDialog("dlgConfirmPropagation");
            }
            actionOpenOrResetAfter(this);
            succes();
            return true;
        }
        return false;
    }

    public YvsBaseArticlesTemplate saveNew(TemplateArticles bean) {
        if (bean != null) {
            String action = bean.getId() > 0 ? "Modification" : "Insertion";
            try {
                if (controleFiche(bean)) {
                    YvsBaseArticlesTemplate y = UtilProd.buildTemplateArticles(bean, currentUser, currentAgence.getSociete());
                    if (y.getId() > 0) {
                        dao.update(y);
                    } else {
                        y.setId(null);
                        y = (YvsBaseArticlesTemplate) dao.save1(y);
                        bean.setId(y.getId());
                    }
                    int idx = templates.indexOf(y);
                    if (idx > -1) {
                        templates.set(idx, y);
                    } else {
                        templates.add(0, y);
                    }
                    return y;
                }
            } catch (Exception ex) {
                getErrorMessage(action + " impossible");
                getException(action, ex);
            }
        }
        return null;
    }

    public boolean saveNewCompte() {
        String action = compte.getId() > 0 ? "Modification" : "Insertion";
        try {
            ArticlesCatComptable bean = recopieViewCompte(templateArticles);
            if (controleFiche(bean)) {
                YvsBaseArticleCategorieComptable y = UtilProd.buildArticleCatC(bean, currentUser);
                if (bean.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsBaseArticleCategorieComptable) dao.save1(y);
                    compte.setId(y.getId());
                }
                int idx = templateArticles.getComptes().indexOf(y);
                if (idx > -1) {
                    templateArticles.getComptes().set(idx, y);
                } else {
                    templateArticles.getComptes().add(idx, y);
                }
                if (!templateArticles.getArticles().isEmpty()) {
                    openDialog("dlgConfirmPropagationCompte");
                }
                succes();
                return true;
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException(action, ex);
            return false;
        }
    }

    public boolean saveNewTaxe() {
        try {
            ManagedTaxes mt = (ManagedTaxes) giveManagedBean(ManagedTaxes.class);
            if (mt != null) {
                YvsBaseTaxes entity = mt.saveReturn();
                if (entity != null ? (entity.getId() != null ? entity.getId() > 0 : false) : false) {
                    YvsBaseArticleCategorieComptableTaxe a = new YvsBaseArticleCategorieComptableTaxe(Long.valueOf(-(selectCompte.getTaxes().size())));
                    a.setActif(true);
                    a.setAppRemise(false);
                    a.setArticleCategorie(selectCompte);
                    a.setNew_(true);
                    a.setTaxe(entity);
                    a.setAuthor(currentUser);
                    selectCompte.getTaxes().add(a);

                    succes();
                    update("select_taxe_taxe");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            return false;
        }
        return true;
    }

    public boolean saveNewPlan() {
        String action = plan.getId() > 0 ? "Modification" : "Insertion";
        try {
            PlanTarifaireClient bean = recopieViewPlan(templateArticles);
            if (controleFiche(bean)) {
                YvsBasePlanTarifaire y = UtilCom.buildPlanTarifaireClient(bean, currentUser);
                if (bean.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsBasePlanTarifaire) dao.save1(y);
                    plan.setId(y.getId());
                }
                int idx = templateArticles.getTarifaires().indexOf(y);
                if (idx > -1) {
                    templateArticles.getTarifaires().set(idx, y);
                } else {
                    templateArticles.getTarifaires().add(0, y);
                }
                if (!templateArticles.getArticles().isEmpty()) {
                    openDialog("dlgConfirmPropagationPlan");
                }
                succes();
                return true;
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException(action, ex);
            return false;
        }
    }

    public boolean saveNewTarif() {
        String action = tarifs.getId() > 0 ? "Modification" : "Insertion";
        try {
            GrilleRabais bean = recopieViewTarifs();
            if (controleFiche(bean)) {
                YvsBasePlanTarifaireTranche y = UtilCom.buildGrilleTarifaireClient(bean, currentUser);
                if (bean.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsBasePlanTarifaireTranche) dao.save1(y);
                    tarifs.setId(y.getId());
                }
                int idx = plan.getGrilles().indexOf(y);
                if (idx > -1) {
                    plan.getGrilles().set(idx, y);
                } else {
                    plan.getGrilles().add(idx, y);
                }
                idx = templateArticles.getTarifaires().indexOf(y.getPlan());
                if (idx > -1) {
                    int i = templateArticles.getTarifaires().get(idx).getGrilles().indexOf(y);
                    if (i > -1) {
                        templateArticles.getTarifaires().get(idx).getGrilles().set(i, y);
                    } else {
                        templateArticles.getTarifaires().get(idx).getGrilles().add(0, y);
                    }
                }
                succes();
                return true;
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException(action, ex);
            return false;
        }
    }

    public void addAllCategoriesComptable() {
        if (templateArticles.getId() > 0) {
            ManagedCatCompt mc = (ManagedCatCompt) giveManagedBean(ManagedCatCompt.class);
            if (mc != null) {
                for (YvsBaseCategorieComptable cc : mc.getCategories()) {
                    champ = new String[]{"categorie", "template"};
                    val = new Object[]{cc, new YvsBaseArticlesTemplate(templateArticles.getId())};
                    List<YvsBaseArticleCategorieComptable> lp = dao.loadNameQueries("YvsBaseArticleCategorieComptable.findByCategorieTemplate", champ, val);
                    if (lp != null ? lp.isEmpty() : true) {
                        YvsBaseArticleCategorieComptable c = new YvsBaseArticleCategorieComptable();
                        c.setActif(true);
                        c.setTemplate(new YvsBaseArticlesTemplate(templateArticles.getId()));
                        c.setAuthor(currentUser);
                        c.setCategorie(cc);
                        c = (YvsBaseArticleCategorieComptable) dao.save1(c);
                        templateArticles.getComptes().add(c);
                        if (!selecTemplate.getComptes().contains(c)) {
                            selecTemplate.getComptes().add(c);
                        }
                    }
                }
            }
            int idx = templates.indexOf(selecTemplate);
            if (idx > -1) {
                templates.set(idx, selecTemplate);
            }
            succes();
        } else {
            getErrorMessage("Vous devez precisez le template");
        }
    }

    public void addAllCategories() {
        if (templateArticles.getId() > 0) {
            ManagedCategorieClt s = (ManagedCategorieClt) giveManagedBean(ManagedCategorieClt.class);
            if (s != null) {
                for (YvsBaseCategorieClient cc : s.getCategories()) {
                    champ = new String[]{"categorie", "template"};
                    val = new Object[]{cc, new YvsBaseArticlesTemplate(templateArticles.getId())};
                    List<YvsBasePlanTarifaire> lp = dao.loadNameQueries("YvsBasePlanTarifaire.findByCategorieTemplate", champ, val);
                    if (lp != null ? lp.isEmpty() : true) {
                        YvsBasePlanTarifaire c = new YvsBasePlanTarifaire();
                        c.setTemplate(new YvsBaseArticlesTemplate(templateArticles.getId()));
                        c.setPuv(1.0);
                        c.setRemise(0.0);
                        c.setActif(false);
                        c.setNatureCoefAugmentation(Constantes.NATURE_MTANT);
                        c.setCoefAugmentation(0.0);
                        c.setCategorie(cc);
                        c.setAuthor(currentUser);
                        c = (YvsBasePlanTarifaire) dao.save1(c);
                        templateArticles.getTarifaires().add(0, c);
                    }
                }
            }
            resetFichePlan();
            succes();
        } else {
            getErrorMessage("Vous devez precisez le template");
        }
    }

    public void addTaxeToCategorie(YvsBaseArticleCategorieComptableTaxe y) {
        try {
            if (y != null) {
                y.setNew_(!y.isNew_());
                int pos = selectCompte.getTaxes().indexOf(y);
                y.setId(null);
                y.setDateSave(new Date());
                y.setDateUpdate(new Date());
                y = (YvsBaseArticleCategorieComptableTaxe) dao.save1(y);
                selectCompte.getTaxes().set(pos, y);
                templateArticles.getComptes().set(templateArticles.getComptes().indexOf(selectCompte), selectCompte);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible d'ajouter cet element");
        }
    }

    public void removeTaxeToCategorie(YvsBaseArticleCategorieComptableTaxe y) {
        try {
            if (y != null) {
                dao.delete(y);
                y.setNew_(!y.isNew_());
                selectCompte.getTaxes().set(selectCompte.getTaxes().indexOf(y), y);
                templateArticles.getComptes().set(templateArticles.getComptes().indexOf(selectCompte), selectCompte);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet element");
        }
    }

    @Override
    public void deleteBean() {
        try {
            System.err.println("tabIds = " + tabIds);
            if (tabIds != null ? tabIds.trim().length() > 0 : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsBaseArticlesTemplate> list = new ArrayList<>();
                YvsBaseArticlesTemplate bean;
                for (Long ids : l) {
                    bean = templates.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);
                    if (bean.getId() == templateArticles.getId()) {
                        resetFiche();
                    }
                }
                templates.removeAll(list);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible");
            getException("Suppression", ex);
        }
    }

    public void deleteBean_() {
        try {
            if (selecTemplate != null) {
                dao.delete(selecTemplate);
                int idx = templates.indexOf(selecTemplate);
                if (idx > -1) {
                    templates.remove(idx);
                }
                if (selecTemplate.getId() == templateArticles.getId()) {
                    resetFiche();
                }
                succes();
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression impossible");
            getException("Suppression", ex);
        }
    }

    public void deleteBeanCompte(boolean list) {
        try {
            if (list) {
                if (tabIds_compte != null ? tabIds_compte.trim().length() > 0 : false) {
                    String[] tab = tabIds_compte.split("-");
                    for (String ids : tab) {
                        long id = Long.valueOf(ids);
                        dao.delete(new YvsBaseArticleCategorieComptable(id));
                        int idx = templateArticles.getComptes().indexOf(new YvsBaseArticleCategorieComptable(id));
                        if (idx > -1) {
                            templateArticles.getComptes().remove(idx);
                        }
//                        idx = templates.indexOf(new YvsBaseArticlesTemplate(templateArticles.getId()));
//                        if (idx > -1) {
//                            int i = templates.get(idx).getComptes().indexOf(new YvsBaseArticleCategorieComptable(id));
//                            if (i > -1) {
//                                templates.get(idx).getComptes().remove(i);
//                            }
//                        }
                        if (id == compte.getId()) {
                            resetFicheCompte();
                        }
                    }
                    if (!templateArticles.getArticles().isEmpty()) {
                        openDialog("dlgConfirmPropagationCompte");
                    }
                    succes();
                }
            } else {
                if (selectCompte != null) {
                    dao.delete(selectCompte);
                    int idx = templateArticles.getComptes().indexOf(selectCompte);
                    if (idx > -1) {
                        templateArticles.getComptes().remove(idx);
                    }
//                    idx = templates.indexOf(new YvsBaseArticlesTemplate(templateArticles.getId()));
//                    if (idx > -1) {
//                        int i = templates.get(idx).getComptes().indexOf(selectCompte);
//                        if (i > -1) {
//                            templates.get(idx).getComptes().remove(i);
//                        }
//                    }
                    if (selectCompte.getId() == compte.getId()) {
                        resetFicheCompte();
                    }
                    if (!templateArticles.getArticles().isEmpty()) {
                        openDialog("dlgConfirmPropagationCompte");
                    }
                    succes();
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression impossible");
            getException("Suppression", ex);
        }
    }

    public void deleteBeanPlan(boolean list) {
        try {
            if (list) {
                if (tabIds_plan != null ? tabIds_plan.trim().length() > 0 : false) {
                    String[] tab = tabIds_plan.split("-");
                    for (String ids : tab) {
                        long id = Long.valueOf(ids);
                        dao.delete(new YvsBasePlanTarifaire(id));
                        int idx = templateArticles.getTarifaires().indexOf(new YvsBasePlanTarifaire(id));
                        if (idx > -1) {
                            templateArticles.getTarifaires().remove(idx);
                        }
//                        idx = templates.indexOf(new YvsBaseArticlesTemplate(templateArticles.getId()));
//                        if (idx > -1) {
//                            int i = templates.get(idx).getTarifaires().indexOf(new YvsBasePlanTarifaire(id));
//                            if (i > -1) {
//                                templates.get(idx).getTarifaires().remove(i);
//                            }
//                        }
                        if (id == plan.getId()) {
                            resetFichePlan();
                        }
                    }
                    if (!templateArticles.getArticles().isEmpty()) {
                        openDialog("dlgConfirmPropagationPlan");
                    }
                    succes();
                }
            } else {
                if (selectPlan != null) {
                    dao.delete(selectPlan);
                    int idx = templateArticles.getTarifaires().indexOf(selectPlan);
                    if (idx > -1) {
                        templateArticles.getTarifaires().remove(idx);
                    }
//                    idx = templates.indexOf(new YvsBaseArticlesTemplate(templateArticles.getId()));
//                    if (idx > -1) {
//                        int i = templates.get(idx).getTarifaires().indexOf(selectPlan);
//                        if (i > -1) {
//                            templates.get(idx).getTarifaires().remove(i);
//                        }
//                    }
                    if (selectPlan.getId() == plan.getId()) {
                        resetFichePlan();
                    }
                    if (!templateArticles.getArticles().isEmpty()) {
                        openDialog("dlgConfirmPropagationPlan");
                    }
                    succes();
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression impossible");
            getException("Suppression", ex);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSelectObject(YvsBaseArticlesTemplate y) {
        selecTemplate = y;
        populateView(UtilProd.buildBeanTemplateArticles(y));
        update("blog_form_template_article");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            execute("collapseForm('template_article')");
            YvsBaseArticlesTemplate y = (YvsBaseArticlesTemplate) ev.getObject();
            onSelectObject(y);
            tabIds = templates.indexOf(y) + "";

        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void onCompteEdit(RowEditEvent ev) {
        if (ev != null) {
            YvsBaseArticleCategorieComptable y = (YvsBaseArticleCategorieComptable) ev.getObject();
            dao.update(y);
            if (!templateArticles.getArticles().isEmpty()) {
                openDialog("dlgConfirmPropagationCompte");
            }
            succes();
        }
    }

    public void loadOnViewPlan(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectPlan = (YvsBasePlanTarifaire) ev.getObject();
            populateView(UtilCom.buildBeanPlanTarifaireClient(selectPlan));
        }
    }

    public void unLoadOnViewPlan(UnselectEvent ev) {
        resetFichePlan();
    }

    public void loadOnViewTarifs(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectTarifs = (YvsBasePlanTarifaireTranche) ev.getObject();
            populateView(UtilCom.buildBeanGrilleTarifaireClient(selectTarifs));
        }
    }

    public void unLoadOnViewTarifs(UnselectEvent ev) {
        resetFicheTarifs();
    }

    public void loadOnViewCompte(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            if (selectCompte != null ? selectCompte.getId() > 0 : false) {
                YvsBasePlanComptable bean = (YvsBasePlanComptable) ev.getObject();
                selectCompte.setCompte(bean);
                selectCompte.setNumCompte(bean.getNumCompte());
                dao.update(selectCompte);
                templateArticles.getComptes().set(templateArticles.getComptes().indexOf(selectCompte), selectCompte);
                selectCompte = new YvsBaseArticleCategorieComptable();
                succes();
                if (!templateArticles.getArticles().isEmpty()) {
                    openDialog("dlgConfirmPropagationCompte");
                }
            }
        }
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBaseArticles y = (YvsBaseArticles) ev.getObject();
            y.setNew_(!y.isNew_());
            if (articles.contains(y)) {
                articles.remove(y);
            } else {
                articles.add(y);
            }
            templateArticles.getArticles().set(templateArticles.getArticles().indexOf(y), y);
        }
    }

    public void init(boolean next) {
        loadAll(next, false);
    }

    public void initTaxes(YvsBaseArticleCategorieComptable y) {
        selectCompte = y;
        champ = new String[]{"articleCategorie"};
        val = new Object[]{y};
        selectCompte.setTaxes(dao.loadNameQueries("YvsBaseArticleCategorieComptableTaxe.findByArticleCategorie", champ, val));
        ManagedTaxes mt = (ManagedTaxes) giveManagedBean(ManagedTaxes.class);
        if (mt != null) {
            for (YvsBaseTaxes t : mt.getTaxesList()) {
                boolean deja = false;
                for (YvsBaseArticleCategorieComptableTaxe a : selectCompte.getTaxes()) {
                    if (a.getTaxe().equals(t)) {
                        a.setNew_(false);
                        deja = true;
                        break;
                    }
                }
                if (!deja) {
                    YvsBaseArticleCategorieComptableTaxe a = new YvsBaseArticleCategorieComptableTaxe(Long.valueOf(-(selectCompte.getTaxes().size())));
                    a.setActif(true);
                    a.setAppRemise(false);
                    a.setArticleCategorie(y);
                    a.setNew_(true);
                    a.setTaxe(t);
                    a.setAuthor(currentUser);
                    selectCompte.getTaxes().add(a);
                }
            }
        }
        update("data_taxe_template_article_categorie");
    }

    public void initComptes(YvsBaseArticleCategorieComptable y) {
        selectCompte = y;
        update("data_comptes_template_article_categorie");
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAll(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAll(true, true);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsBaseArticlesTemplate> re = paginator.parcoursDynamicData("YvsBaseArticlesTemplate", "y", "y.designation", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void active(YvsBaseArticlesTemplate art) {
        if (art != null) {
            art.setActif(!art.getActif());
            art.setAuthor(currentUser);
            dao.update(art);
            templates.set(templates.indexOf(art), art);
        }
    }

    public void activePlan(YvsBasePlanTarifaire bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            bean.setAuthor(currentUser);
            dao.update(bean);
            templateArticles.getTarifaires().set(templateArticles.getTarifaires().indexOf(bean), bean);
        }
    }

    public void activeAppRemiseTaxe(YvsBaseArticleCategorieComptableTaxe y) {
        try {
            if (y != null) {
                y.setAppRemise(!y.getAppRemise());
                dao.update(y);
                selectCompte.getTaxes().set(selectCompte.getTaxes().indexOf(y), y);
                templateArticles.getComptes().set(templateArticles.getComptes().indexOf(selectCompte), selectCompte);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet element");
        }
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.refArt", "reference", null);
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", numSearch.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.refArt)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.designation)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.libelle)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(false, true);
    }

    public void addParamCategorie() {
        ParametreRequete p = new ParametreRequete("y.categorie", "categorie", null);
        if (categorieSearch != null ? categorieSearch.trim().length() > 0 : false) {
            p.setObjet(categorieSearch);
            p.setOperation("=");
            p.setPredicat("AND");
        }
        paginator.addParam(p);
        loadAll(false, true);
    }

    public void addParamGroupe() {
        ParametreRequete p = new ParametreRequete("y.groupe", "groupe", null);
        if (groupeSearch > 0) {
            p.setObjet(new YvsBaseGroupesArticle(groupeSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        }
        paginator.addParam(p);
        loadAll(false, true);
    }

    public void addParamFamille() {
        ParametreRequete p = new ParametreRequete("y.famille", "famille", null);
        if (familleSearch > 0) {
            p.setObjet(new YvsBaseFamilleArticle(familleSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        }
        paginator.addParam(p);
        loadAll(false, true);
    }

    public void addParamActif() {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", null);
        if (actifSearch != null) {
            p.setObjet(actifSearch);
            p.setOperation("=");
            p.setPredicat("AND");
        }
        paginator.addParam(p);
        loadAll(false, true);
    }

    public void addParamConditionnement() {
        ParametreRequete p = new ParametreRequete("y.uniteStockage", "stockage", null);
        if (uniteSearch > 0) {
            p.setObjet(new YvsBaseUniteMesure(uniteSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        }
        paginator.addParam(p);
        loadAll(false, true);
    }

    public String getNameBtn(String type) {
        String nameBtn = "Enregistrer";
        if (type != null) {
            boolean update;
            switch (type) {
                case "T":
                    update = templateArticles != null ? templateArticles.getId() > 0 : false;
                    break;
                case "P":
                    update = plan != null ? plan.getId() > 0 : false;
                    break;
                default:
                    update = compte != null ? compte.getId() > 0 : false;
                    break;
            }
            if (update) {
                nameBtn = "Modifier";
                if (templateArticles.getArticles() != null ? !templateArticles.getArticles().isEmpty() : false) {
                    nameBtn = "Modifier & Propager";
                }
            } else {
                if (templateArticles.getArticles() != null ? !templateArticles.getArticles().isEmpty() : false) {
                    nameBtn = "Enregistrer & Propager";
                }
            }
        }
        return nameBtn;
    }

    public void ecouteSaisieCG(YvsBaseArticleCategorieComptable art) {
        //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
        ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (service != null) {
            service.findCompteByNum(art.getNumCompte());
            art.setError(service.getListComptes().isEmpty());
            if (service.getListComptes() != null) {
                if (!service.getListComptes().isEmpty()) {
                    if (service.getListComptes().size() == 1) {
                        art.setError(false);
                        art.setCompte(service.getListComptes().get(0));
                        art.setNumCompte(art.getCompte().getNumCompte());
                    } else {
                        openDialog("dlgListComptes");
                        selectCompte = art;
                        update("data_comptes_template_article_categorie");
                    }
                } else {
                    art.setError(true);
                }
            } else {
                art.setError(true);
            }
        }
    }

    public void propagation(boolean template, boolean compte, boolean plan) {
        try {
            TemplateArticles bean = recopieView();
            if (articles.size() > 0) {
                if (temp.checkUpdate(bean) || compte || plan) {
                    TemplateArticles t;
                    for (YvsBaseArticles a : articles) {
                        t = new TemplateArticles();
                        if (template) {
                            t.checkUpdate(a, temp);
                            if (temp.isChangeRefArt() && t.isChangeRefArt()) {
                                a.setRefArt(bean.getRefArt());
                            }
                            if (temp.isChangeDesignation() && t.isChangeDesignation()) {
                                a.setDesignation(bean.getDesignation());
                            }
                            if (temp.isChangeFichier() && t.isChangeFichier()) {
                                a.setFichier(bean.getFichier());
                            }
                            if (temp.isChangeCategorie() && t.isChangeCategorie()) {
                                a.setCategorie(bean.getCategorie());
                            }
                            if (temp.isChangeChangePrix() && t.isChangeChangePrix()) {
                                a.setChangePrix(bean.isChangePrix());
                            }
                            if (temp.isChangeCoefficient() && t.isChangeCoefficient()) {
                                a.setCoefficient(bean.getCoefficient());
                            }
                            if (temp.isChangeModeConso() && t.isChangeModeConso()) {
                                a.setModeConso(bean.getModeConso());
                            }
                            if (temp.isChangeDefNorme() && t.isChangeDefNorme()) {
                                a.setDefNorme(bean.isDefNorme());
                            }
                            if (temp.isChangeNormeFixe() && t.isChangeNormeFixe()) {
                                a.setNorme(bean.isNormeFixe());
                            }
                            if (temp.isChangeSuiviEnStock() && t.isChangeSuiviEnStock()) {
                                a.setSuiviEnStock(bean.isSuiviEnStock());
                            }
                            if (temp.isChangeVisibleEnSynthese() && t.isChangeVisibleEnSynthese()) {
                                a.setVisibleEnSynthese(bean.isVisibleEnSynthese());
                            }
                            if (temp.isChangeService() && t.isChangeService()) {
                                a.setService(bean.isService());
                            }
                            if (temp.isChangeMethodeVal() && t.isChangeMethodeVal()) {
                                a.setMethodeVal(bean.getMethodeVal());
                            }
                            if (temp.isChangeDureeVie() && t.isChangeDureeVie()) {
                                a.setDelaiLivraison(bean.getDureeVie());
                            }
                            if (temp.isChangeDureeGarantie() && t.isChangeDureeGarantie()) {
                                a.setDureeGarantie(bean.getDureeGarantie());
                            }
                            if (temp.isChangeClasse() && t.isChangeClasse() && bean.getClasse().getId() > 0) {
                                a.setClasse1(new YvsBaseClassesStat(bean.getClasse().getId()));
                            }
                            if (temp.isChangeGroupe() && t.isChangeGroupe() && bean.getGroupe().getId() > 0) {
                                a.setGroupe(new YvsBaseGroupesArticle(bean.getGroupe().getId()));
                            }
                            if (temp.isChangeFamille() && t.isChangeFamille() && bean.getFamille().getId() > 0) {
                                a.setFamille(new YvsBaseFamilleArticle(bean.getFamille().getId()));
                            }
                            a.setAuthor(currentUser);
                            dao.update(a);
                        }
                        if (compte) {
                            YvsBaseArticleCategorieComptable yc;
                            YvsBaseArticleCategorieComptable ytemp;
                            YvsBaseArticleCategorieComptableTaxe yt;
                            List<YvsBaseArticleCategorieComptable> _comptes = new ArrayList<>();
                            _comptes.addAll(bean.getComptes());

                            for (YvsBaseArticleCategorieComptable y : bean.getComptes()) {
                                champ = new String[]{"categorie", "article"};
                                val = new Object[]{y.getCategorie(), new YvsBaseArticles(a.getId())};
                                nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieArticleNoCompte";
                                if (y.getCompte() != null ? y.getCompte().getId() > 0 : false) {
                                    champ = new String[]{"categorie", "compte", "article"};
                                    val = new Object[]{y.getCategorie(), y.getCompte(), new YvsBaseArticles(a.getId())};
                                    nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieArticleCompte";
                                }
                                yc = (YvsBaseArticleCategorieComptable) dao.loadOneByNameQueries(nameQueri, champ, val);

                                int idx = tempComptes.indexOf(y);
                                if (idx < 0) {
                                    if (yc != null ? (yc.getId() != null ? yc.getId() < 1 : true) : true) {
                                        yc = new YvsBaseArticleCategorieComptable();
                                        yc.setActif(y.getActif());
                                        yc.setArticle(new YvsBaseArticles(a.getId()));
                                        yc.setCategorie(y.getCategorie());
                                        yc.setCompte(y.getCompte());
                                        yc.setAuthor(currentUser);
                                        yc = (YvsBaseArticleCategorieComptable) dao.save1(yc);

                                        for (int i = 0; i < y.getTaxes().size(); i++) {
                                            yt = y.getTaxes().get(i);
                                            yt.setArticleCategorie(yc);
                                            yt.setAuthor(currentUser);
                                            yt.setId(null);
                                            yt = (YvsBaseArticleCategorieComptableTaxe) dao.save1(yt);
                                        }
                                        a.getComptes().add(yc);
                                    }
                                } else {
                                    ytemp = tempComptes.get(idx);
                                    if (yc != null ? (yc.getId() != null ? yc.getId() < 1 : true) : true) {
                                        yc = new YvsBaseArticleCategorieComptable((long) -(a.getComptes().size()));
                                        yc.setActif(y.getActif());
                                        yc.setArticle(new YvsBaseArticles(a.getId()));
                                    }
                                    yc.setAuthor(currentUser);
                                    if (!ytemp.getCategorie().equals(y.getCategorie()) && !ytemp.getCategorie().equals(yc.getCategorie())
                                            || (yc.getCategorie() != null ? yc.getCategorie().getId() < 1 : true)) {
                                        yc.setCategorie(y.getCategorie());
                                    }
                                    if (!ytemp.getCompte().equals(y.getCompte()) && !ytemp.getCompte().equals(yc.getCompte())
                                            || (yc.getCompte() != null ? yc.getCompte().getId() < 1 : true)) {
                                        yc.setCompte(y.getCompte());
                                    }
                                    if (yc.getId() != null ? yc.getId() > 0 : false) {
                                        dao.update(yc);
                                    } else {
                                        yc.setId(null);
                                        yc = (YvsBaseArticleCategorieComptable) dao.save1(yc);
                                        for (int i = 0; i < y.getTaxes().size(); i++) {
                                            yt = y.getTaxes().get(i);
                                            yt.setId(null);
                                            yt.setArticleCategorie(yc);
                                            yt.setAuthor(currentUser);
                                            yt = (YvsBaseArticleCategorieComptableTaxe) dao.save1(yt);
                                        }
                                        a.getComptes().add(yc);
                                    }
                                }
                            }
                            for (YvsBaseArticleCategorieComptable y : tempComptes) {
                                int idx = _comptes.indexOf(y);
                                if (idx < 0) {
                                    champ = new String[]{"categorie", "article"};
                                    val = new Object[]{y.getCategorie(), new YvsBaseArticles(a.getId())};
                                    nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieArticleNoCompte";
                                    if (y.getCompte() != null ? y.getCompte().getId() > 0 : false) {
                                        champ = new String[]{"categorie", "compte", "article"};
                                        val = new Object[]{y.getCategorie(), y.getCompte(), new YvsBaseArticles(a.getId())};
                                        nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieArticleCompte";
                                    }
                                    yc = (YvsBaseArticleCategorieComptable) dao.loadOneByNameQueries(nameQueri, champ, val);
                                    if (yc != null ? yc.getId() > 0 : false) {
                                        dao.delete(yc);
                                    }
                                }
                            }
                        }
                        if (plan) {
                            YvsBasePlanTarifaire yc;
                            YvsBasePlanTarifaire ytemp;
                            YvsBasePlanTarifaireTranche yt;
                            List<YvsBasePlanTarifaire> _tarifaires = new ArrayList<>();
                            _tarifaires.addAll(bean.getTarifaires());

                            for (YvsBasePlanTarifaire y : bean.getTarifaires()) {
                                champ = new String[]{"categorie", "article"};
                                val = new Object[]{y.getCategorie(), new YvsBaseArticles(a.getId())};
                                nameQueri = "YvsBasePlanTarifaire.findByCategorieArticle";
                                yc = (YvsBasePlanTarifaire) dao.loadOneByNameQueries(nameQueri, champ, val);

                                int idx = tempPlans.indexOf(y);
                                if (idx < 0) {
                                    if (yc != null ? (yc.getId() != null ? yc.getId() < 1 : true) : true) {
                                        yc = new YvsBasePlanTarifaire();
                                        yc.setActif(y.getActif());
                                        yc.setArticle(new YvsBaseArticles(a.getId()));
                                        yc.setCategorie(y.getCategorie());
                                        yc.setCoefAugmentation(y.getCoefAugmentation());
                                        yc.setRemise(y.getRemise());
                                        yc.setPuv(y.getPuv());
                                        yc.setPuvMin(y.getPuvMin());
                                        yc.setRistourne(y.getRistourne());
                                        yc.setNatureCoefAugmentation(y.getNatureCoefAugmentation());
                                        yc.setNaturePrixMin(y.getNaturePrixMin());
                                        yc.setNatureRemise(y.getNatureRemise());
                                        yc.setNatureRistourne(y.getNatureRistourne());
                                        yc.setAuthor(currentUser);
                                        yc = (YvsBasePlanTarifaire) dao.save1(yc);

                                        for (int i = 0; i < y.getGrilles().size(); i++) {
                                            yt = y.getGrilles().get(i);
                                            yt.setId(null);
                                            yt.setPlan(yc);
                                            yt.setAuthor(currentUser);
                                            dao.save1(yt);
                                        }
                                        a.getPlans_tarifaires().add(yc);
                                    }
                                } else {
                                    ytemp = tempPlans.get(idx);
                                    if (yc != null ? (yc.getId() != null ? yc.getId() < 1 : true) : true) {
                                        yc = new YvsBasePlanTarifaire((long) -(a.getPlans_tarifaires().size()));
                                        yc.setActif(y.getActif());
                                        yc.setArticle(new YvsBaseArticles(a.getId()));
                                    }
                                    yc.setAuthor(currentUser);
                                    if (!ytemp.getCategorie().equals(y.getCategorie()) && !ytemp.getCategorie().equals(yc.getCategorie())
                                            || (yc.getCategorie() != null ? yc.getCategorie().getId() < 1 : true)) {
                                        yc.setCategorie(y.getCategorie());
                                    }
                                    if (!ytemp.getCoefAugmentation().equals(y.getCoefAugmentation()) && !ytemp.getCoefAugmentation().equals(yc.getCoefAugmentation())) {
                                        yc.setCoefAugmentation(y.getCoefAugmentation());
                                    }
                                    if (!ytemp.getPuv().equals(y.getPuv()) && !ytemp.getPuv().equals(yc.getPuv())) {
                                        yc.setPuv(y.getPuv());
                                    }
                                    if (!ytemp.getPuvMin().equals(y.getPuvMin()) && !ytemp.getPuvMin().equals(yc.getPuvMin())) {
                                        yc.setPuvMin(y.getPuvMin());
                                    }
                                    if (!ytemp.getRemise().equals(y.getRemise()) && !ytemp.getRemise().equals(yc.getRemise())) {
                                        yc.setRemise(y.getRemise());
                                    }
                                    if (!ytemp.getRistourne().equals(y.getRistourne()) && !ytemp.getRistourne().equals(yc.getRistourne())) {
                                        yc.setRistourne(y.getRistourne());
                                    }
                                    if (!ytemp.getNatureCoefAugmentation().equals(y.getNatureCoefAugmentation()) && !ytemp.getNatureCoefAugmentation().equals(yc.getNatureCoefAugmentation())) {
                                        yc.setNatureCoefAugmentation(y.getNatureCoefAugmentation());
                                    }
                                    if (!ytemp.getNaturePrixMin().equals(y.getNaturePrixMin()) && !ytemp.getNaturePrixMin().equals(yc.getNaturePrixMin())) {
                                        yc.setNaturePrixMin(y.getNaturePrixMin());
                                    }
                                    if (!ytemp.getNatureRemise().equals(y.getNatureRemise()) && !ytemp.getNatureRemise().equals(yc.getNatureRemise())) {
                                        yc.setNatureRemise(y.getNatureRemise());
                                    }
                                    if (!ytemp.getNatureRistourne().equals(y.getNatureRistourne()) && !ytemp.getNatureRistourne().equals(yc.getNatureRistourne())) {
                                        yc.setNatureRistourne(y.getNatureRistourne());
                                    }
                                    if (yc.getId() != null ? yc.getId() > 0 : false) {
                                        dao.update(yc);
                                    } else {
                                        yc.setId(null);
                                        yc = (YvsBasePlanTarifaire) dao.save1(yc);
                                        for (int i = 0; i < y.getGrilles().size(); i++) {
                                            yt = y.getGrilles().get(i);
                                            yt.setId(null);
                                            yt.setPlan(yc);
                                            yt.setAuthor(currentUser);
                                            dao.save1(yt);
                                        }
                                        a.getPlans_tarifaires().add(yc);
                                    }
                                }
                            }
                            for (YvsBasePlanTarifaire y : tempPlans) {
                                int idx = _tarifaires.indexOf(y);
                                if (idx < 0) {
                                    champ = new String[]{"categorie", "article"};
                                    val = new Object[]{y.getCategorie(), new YvsBaseArticles(a.getId())};
                                    nameQueri = "YvsBasePlanTarifaire.findByCategorieArticle";
                                    yc = (YvsBasePlanTarifaire) dao.loadOneByNameQueries(nameQueri, champ, val);
                                    if (yc != null ? yc.getId() > 0 : false) {
                                        dao.delete(yc);
                                    }
                                }
                            }
                        }
                    }
                    cloneObject(temp, bean);
                    tempComptes.clear();
                    tempComptes.addAll(bean.getComptes());
                    tempPlans.clear();
                    tempPlans.addAll(bean.getTarifaires());
                    succes();
                }
            } else {
                getErrorMessage("Veuillez selectionner les articles à modifier");
            }
        } catch (Exception ex) {
            getErrorMessage("Propagation impossible");
            getException("Propagation", ex);
        }
    }

    public void cloneValue(Articles art, YvsBaseArticlesTemplate temp, boolean force) {
        if (temp != null) {
            art.setChangePrix(force ? temp.getChangePrix() : art.isChangePrix());
            art.setDefNorme(force ? temp.getDefNorme() : art.isDefNorme());
            art.setModeConso(force ? temp.getModeConso() : (art.getModeConso().isEmpty() ? temp.getModeConso() : art.getModeConso()));
            art.setSuiviEnStock(force ? temp.getSuiviEnStock() : art.isSuiviEnStock());
            art.setVisibleEnSynthese(force ? temp.getVisibleEnSynthese() : art.isVisibleEnSynthese());
            art.setCoefficient(force ? temp.getCoefficient() : (art.getCoefficient() == 0 ? temp.getCoefficient() : art.getCoefficient()));
            art.setService(force ? temp.getService() : art.isService());
            art.setMethodeVal(force ? temp.getMethodeVal() : (art.getMethodeVal().isEmpty() ? temp.getMethodeVal() : art.getMethodeVal()));
            art.setCategorie(force ? temp.getCategorie() : (art.getCategorie().isEmpty() ? temp.getCategorie() : art.getCategorie()));
            art.setDelaiLivraison(force ? temp.getDureeVie() : (art.getDelaiLivraison() == 0 ? temp.getDureeVie() : art.getDelaiLivraison()));
            art.setDureeGarantie(force ? temp.getDureeGarantie() : (art.getDureeGarantie() == 0 ? temp.getDureeGarantie() : art.getDureeGarantie()));
            art.setFichier(force ? temp.getFichier() : (art.getFichier().isEmpty() ? temp.getFichier() : art.getFichier()));
            art.setGroupe(force ? (UtilProd.buildBeanGroupeArticle(temp.getGroupe())) : (art.getGroupe().getId() < 1 ? (UtilProd.buildBeanGroupeArticle(temp.getGroupe())) : art.getGroupe()));
            art.setClasse1(force ? UtilProd.buildBeanClasseStat(temp.getClasse()) : (art.getClasse1().getId() < 1 ? UtilProd.buildBeanClasseStat(temp.getClasse()) : art.getClasse1()));
            art.setFamille(force ? UtilProd.buildBeanFamilleArticle(temp.getFamille()) : (art.getFamille().getId() < 1 ? UtilProd.buildBeanFamilleArticle(temp.getFamille()) : art.getFamille()));

            if (force) {
                art.getListArticleCatComptable().clear();
                YvsBaseArticleCategorieComptable yc;
                YvsBaseArticleCategorieComptableTaxe yt;
                for (YvsBaseArticleCategorieComptable c : temp.getComptes()) {
                    champ = new String[]{"categorie", "compte", "article"};
                    val = new Object[]{c.getCategorie(), c.getCompte(), new YvsBaseArticles(art.getId())};
                    nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieArticleCompte";
                    yc = (YvsBaseArticleCategorieComptable) dao.loadOneByNameQueries(nameQueri, champ, val);
                    if (yc != null ? (yc.getId() != null ? yc.getId() < 1 : true) : true) {
                        yc = new YvsBaseArticleCategorieComptable((long) -(art.getListArticleCatComptable().size()));
                        yc.setActif(c.getActif());
                        yc.setArticle(new YvsBaseArticles(art.getId()));
                        yc.setCategorie(c.getCategorie());
                        yc.setCompte(c.getCompte());
                        yc.setAuthor(currentUser);

                        for (int i = 0; i < c.getTaxes().size(); i++) {
                            yt = c.getTaxes().get(i);
                            yt.setId((long) -(yc.getTaxes().size()));
                            yt.setArticleCategorie(yc);
                            yt.setAuthor(currentUser);
                            yc.getTaxes().add(yt);
                        }
                        art.getListArticleCatComptable().add(yc);
                    }
                }

                art.getPlans_tarifaires().clear();
                YvsBasePlanTarifaire yp;
                YvsBasePlanTarifaireTranche yg;
                for (YvsBasePlanTarifaire c : temp.getPlans_tarifaires()) {
                    champ = new String[]{"categorie", "article"};
                    val = new Object[]{c.getCategorie(), new YvsBaseArticles(art.getId())};
                    nameQueri = "YvsBasePlanTarifaire.findByCategorieArticle";
                    yp = (YvsBasePlanTarifaire) dao.loadOneByNameQueries(nameQueri, champ, val);
                    if (yp != null ? (yp.getId() != null ? yp.getId() < 1 : true) : true) {
                        yp = new YvsBasePlanTarifaire((long) -(art.getPlans_tarifaires().size()));
                        yp.setActif(c.getActif());
                        yp.setArticle(new YvsBaseArticles(art.getId()));
                        yp.setCategorie(c.getCategorie());
                        yp.setAuthor(currentUser);

                        for (int i = 0; i < c.getGrilles().size(); i++) {
                            yg = c.getGrilles().get(i);
                            yg.setId((long) -(yp.getGrilles().size()));
                            yg.setPlan(yp);
                            yg.setAuthor(currentUser);
                            yp.getGrilles().add(yg);
                        }
                        art.getPlans_tarifaires().add(yp);
                    }
                }
            }
        }
    }

    public TemplateArticles generer(Articles temp) {
        TemplateArticles art = new TemplateArticles();
        if (temp != null) {
            champ = new String[]{"code", "societe"};
            val = new Object[]{temp.getRefArt(), currentAgence.getSociete()};
            nameQueri = "YvsArticlesTemplate.findByCode";
            YvsBaseArticlesTemplate y = (YvsBaseArticlesTemplate) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (y != null ? (y.getId() != null ? y.getId() < 1 : true) : true) {
                art.setId(-1);
                art.setChangePrix(temp.isChangePrix());
                art.setDescription(temp.getDesignation());
                art.setDefNorme(temp.isDefNorme());
                art.setDesignation(temp.getDesignation());
                art.setModeConso(temp.getModeConso());
                art.setNormeFixe(temp.isNormeFixe());
                art.setSuiviEnStock(temp.isSuiviEnStock());
                art.setVisibleEnSynthese(temp.isVisibleEnSynthese());
                art.setClasse(temp.getClasse1());
                art.setCoefficient(temp.getCoefficient());
                art.setService(temp.isService());
                art.setMethodeVal(temp.getMethodeVal());
                art.setCategorie(temp.getCategorie());
                art.setDureeVie(temp.getDelaiLivraison());
                art.setDureeGarantie(temp.getDureeGarantie());
                art.setFichier(temp.getFichier());
                art.setRefArt(temp.getRefArt());
                art.setGroupe(temp.getGroupe());
                art.setFamille(temp.getFamille());
                art.setActif(temp.isActif());

                YvsBaseArticleCategorieComptable yc;
                YvsBaseArticleCategorieComptableTaxe yt;
                for (YvsBaseArticleCategorieComptable c : temp.getListArticleCatComptable()) {
                    yc = new YvsBaseArticleCategorieComptable((long) -(art.getComptes().size()));
                    yc.setActif(c.getActif());
                    yc.setArticle(null);
                    yc.setCategorie(c.getCategorie());
                    yc.setCompte(c.getCompte());
                    yc.setAuthor(currentUser);

                    for (int i = 0; i < c.getTaxes().size(); i++) {
                        yt = c.getTaxes().get(i);
                        yt.setId((long) -(yc.getTaxes().size()));
                        yt.setArticleCategorie(yc);
                        yt.setAuthor(currentUser);
                        yc.getTaxes().add(yt);
                    }
                    art.getComptes().add(yc);
                }

                YvsBasePlanTarifaire yp;
                YvsBasePlanTarifaireTranche yg;
                for (YvsBasePlanTarifaire c : temp.getPlans_tarifaires()) {
                    yp = new YvsBasePlanTarifaire((long) -(art.getTarifaires().size()));
                    yp.setActif(c.getActif());
                    yp.setArticle(null);
                    yp.setCategorie(c.getCategorie());
                    yp.setAuthor(currentUser);

                    for (int i = 0; i < c.getGrilles().size(); i++) {
                        yg = c.getGrilles().get(i);
                        yg.setId((long) -(yp.getGrilles().size()));
                        yg.setPlan(yp);
                        yg.setAuthor(currentUser);
                        yp.getGrilles().add(yg);
                    }
                    art.getTarifaires().add(yp);
                }
            } else {
                getErrorMessage("Vous avez deja créer un template avec cette article");
            }
        }
        return art;
    }

    public void applyArticleOnTemplate() {
        if (selectArticles != null ? !selectArticles.isEmpty() : false) {
            List<YvsBaseArticleCategorieComptable> comptesSave = new ArrayList<>();
            List<YvsBaseArticleCategorieComptable> comptesArticles = new ArrayList<>();
            List<YvsBaseArticleCategorieComptableTaxe> taxesSave = new ArrayList<>();
            for (YvsBaseArticles a : selectArticles) {
                TemplateArticles t = new TemplateArticles();
                if (temp.isChangeChangePrix() && t.isChangeChangePrix()) {
                    a.setChangePrix(templateArticles.isChangePrix());
                }
                if (temp.isChangeSuiviEnStock() && t.isChangeSuiviEnStock()) {
                    a.setSuiviEnStock(templateArticles.isSuiviEnStock());
                }
                if (temp.isChangeMethodeVal() && t.isChangeMethodeVal()) {
                    a.setMethodeVal(templateArticles.getMethodeVal());
                }
                a.setAuthor(currentUser);
                a.setDateUpdate(new Date());
                a.setTemplate(selecTemplate);
                dao.update(a);
                //gestion des catégories comptables
                YvsBaseArticleCategorieComptable yc = null;
                YvsBaseArticleCategorieComptableTaxe yt;
                List<YvsBaseArticleCategorieComptable> _comptes = new ArrayList<>();
                _comptes.addAll(templateArticles.getComptes());
                comptesArticles = dao.loadNameQueries("YvsBaseArticleCategorieComptable.findSimpleByArticle", new String[]{"article"}, new Object[]{a});
                for (YvsBaseArticleCategorieComptable y : templateArticles.getComptes()) {
                    champ = new String[]{"categorie", "article"};
                    val = new Object[]{y.getCategorie(), a};
                    nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieArticle";
//                    if (y.getCompte() != null ? y.getCompte().getId() > 0 : false) {
//                        champ = new String[]{"categorie", "compte", "article"};
//                        val = new Object[]{y.getCategorie(), y.getCompte(), a};
//                        nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieArticleCompte";
//                    }
                    comptesSave = dao.loadNameQueries(nameQueri, champ, val);
                    if (comptesSave != null ? comptesSave.size() > 1 : false) {
                        yc = comptesSave.get(0);
                        deleteAll(comptesSave);
                    } else {
                        if (comptesSave != null ? !comptesSave.isEmpty() : false) {
                            yc = comptesSave.get(0);
                        }
                    }
                    if (yc != null ? (yc.getId() != null ? yc.getId() < 1 : true) : true) {
                        yc = new YvsBaseArticleCategorieComptable();
                        yc.setActif(y.getActif());
                        yc.setArticle(new YvsBaseArticles(a.getId()));
                        yc.setCategorie(y.getCategorie());
                        yc.setCompte(y.getCompte());
                        yc.setAuthor(currentUser);
                        yc.setTemplate(null);
                        yc = (YvsBaseArticleCategorieComptable) dao.save1(yc);
                        for (int i = 0; i < y.getTaxes().size(); i++) {
                            yt = y.getTaxes().get(i);
                            if (yt.isNew_()) {
                                yt.setArticleCategorie(yc);
                                yt.setAuthor(currentUser);
                                yt.setId(null);
                                yt.setAppRemise(true);
                                yt.setActif(true);
                                yt.setDateSave(new Date());
                                yt.setDateUpdate(new Date());
                                try {
                                    yt = (YvsBaseArticleCategorieComptableTaxe) dao.save1(yt);
                                    System.err.println("Id save " + yt.getId());
                                } catch (Exception ex) {
                                    getException("", ex);
                                }
                            }
                        }
                    } else {
                        yc.setCompte(y.getCompte());
                        yc.setAuthor(currentUser);
                        yc.setTemplate(null);
                        yc.getTaxes().clear();
                        dao.update(yc);
                        //modifie les taxes (on supprime et on recrée)
                        taxesSave = dao.loadNameQueries("YvsBaseArticleCategorieComptableTaxe.findByArticleCategorie", new String[]{"articleCategorie"}, new Object[]{yc});
                        for (YvsBaseArticleCategorieComptableTaxe one : taxesSave) {
                            dao.delete(one);
                        }
                        for (int i = 0; i < y.getTaxes().size(); i++) {
                            yt = y.getTaxes().get(i);
                            if (yt.isNew_()) {
                                yt.setArticleCategorie(yc);
                                yt.setAuthor(currentUser);
                                yt.setDateSave(new Date());
                                yt.setDateUpdate(new Date());
                                yt.setId(null);
                                yt = (YvsBaseArticleCategorieComptableTaxe) dao.save1(yt);
                            }

                        }
                        comptesArticles.remove(yc);
                    }
                }
                deleteAll(comptesArticles);
                //traitement des catégorie tarifaires
                YvsBasePlanTarifaire planT;
                YvsBasePlanTarifaire planTtemp;
                YvsBasePlanTarifaireTranche tranchePT;
                List<YvsBasePlanTarifaire> _tarifaires = new ArrayList<>();
                _tarifaires.addAll(templateArticles.getTarifaires());

                for (YvsBasePlanTarifaire y : templateArticles.getTarifaires()) {
                    champ = new String[]{"categorie", "article"};
                    val = new Object[]{y.getCategorie(), new YvsBaseArticles(a.getId())};
                    nameQueri = "YvsBasePlanTarifaire.findByCategorieArticle";
                    planT = (YvsBasePlanTarifaire) dao.loadOneByNameQueries(nameQueri, champ, val);

                    int idx = tempPlans.indexOf(y);
                    if (idx < 0) {
                        if (planT != null ? (planT.getId() != null ? planT.getId() < 1 : true) : true) {
                            planT = new YvsBasePlanTarifaire();
                            planT.setActif(y.getActif());
                            planT.setArticle(new YvsBaseArticles(a.getId()));
                            planT.setCategorie(y.getCategorie());
                            planT.setCoefAugmentation(y.getCoefAugmentation());
                            planT.setRemise(y.getRemise());
                            planT.setPuv(y.getPuv());
                            planT.setPuvMin(y.getPuvMin());
                            planT.setRistourne(y.getRistourne());
                            planT.setNatureCoefAugmentation(y.getNatureCoefAugmentation());
                            planT.setNaturePrixMin(y.getNaturePrixMin());
                            planT.setNatureRemise(y.getNatureRemise());
                            planT.setNatureRistourne(y.getNatureRistourne());
                            planT.setAuthor(currentUser);
                            planT = (YvsBasePlanTarifaire) dao.save1(planT);

                            for (int i = 0; i < y.getGrilles().size(); i++) {
                                tranchePT = y.getGrilles().get(i);
                                tranchePT.setId(null);
                                tranchePT.setPlan(planT);
                                tranchePT.setAuthor(currentUser);
                                dao.save1(tranchePT);
                            }
                            a.getPlans_tarifaires().add(planT);
                        }
                    } else {
                        planTtemp = tempPlans.get(idx);
                        if (planT != null ? (planT.getId() != null ? planT.getId() < 1 : true) : true) {
                            planT = new YvsBasePlanTarifaire((long) -(a.getPlans_tarifaires().size()));
                            planT.setActif(y.getActif());
                            planT.setArticle(new YvsBaseArticles(a.getId()));
                        }
                        planT.setAuthor(currentUser);
                        if (!planTtemp.getCategorie().equals(y.getCategorie()) && !planTtemp.getCategorie().equals(planT.getCategorie())
                                || (planT.getCategorie() != null ? planT.getCategorie().getId() < 1 : true)) {
                            planT.setCategorie(y.getCategorie());
                        }
                        if (!planTtemp.getCoefAugmentation().equals(y.getCoefAugmentation()) && !planTtemp.getCoefAugmentation().equals(planT.getCoefAugmentation())) {
                            planT.setCoefAugmentation(y.getCoefAugmentation());
                        }
                        if (!planTtemp.getPuv().equals(y.getPuv()) && !planTtemp.getPuv().equals(planT.getPuv())) {
                            planT.setPuv(y.getPuv());
                        }
                        if (!planTtemp.getPuvMin().equals(y.getPuvMin()) && !planTtemp.getPuvMin().equals(planT.getPuvMin())) {
                            planT.setPuvMin(y.getPuvMin());
                        }
                        if (!planTtemp.getRemise().equals(y.getRemise()) && !planTtemp.getRemise().equals(planT.getRemise())) {
                            planT.setRemise(y.getRemise());
                        }
                        if (!planTtemp.getRistourne().equals(y.getRistourne()) && !planTtemp.getRistourne().equals(planT.getRistourne())) {
                            planT.setRistourne(y.getRistourne());
                        }
                        if (!planTtemp.getNatureCoefAugmentation().equals(y.getNatureCoefAugmentation()) && !planTtemp.getNatureCoefAugmentation().equals(planT.getNatureCoefAugmentation())) {
                            planT.setNatureCoefAugmentation(y.getNatureCoefAugmentation());
                        }
                        if (!planTtemp.getNaturePrixMin().equals(y.getNaturePrixMin()) && !planTtemp.getNaturePrixMin().equals(planT.getNaturePrixMin())) {
                            planT.setNaturePrixMin(y.getNaturePrixMin());
                        }
                        if (!planTtemp.getNatureRemise().equals(y.getNatureRemise()) && !planTtemp.getNatureRemise().equals(planT.getNatureRemise())) {
                            planT.setNatureRemise(y.getNatureRemise());
                        }
                        if (!planTtemp.getNatureRistourne().equals(y.getNatureRistourne()) && !planTtemp.getNatureRistourne().equals(planT.getNatureRistourne())) {
                            planT.setNatureRistourne(y.getNatureRistourne());
                        }
                        if (planT.getId() != null ? planT.getId() > 0 : false) {
                            dao.update(planT);
                        } else {
                            planT.setId(null);
                            planT = (YvsBasePlanTarifaire) dao.save1(planT);
                            for (int i = 0; i < y.getGrilles().size(); i++) {
                                tranchePT = y.getGrilles().get(i);
                                tranchePT.setId(null);
                                tranchePT.setPlan(planT);
                                tranchePT.setAuthor(currentUser);
                                dao.save1(tranchePT);
                            }
                            a.getPlans_tarifaires().add(planT);
                        }
                    }
                }
                for (YvsBasePlanTarifaire y : tempPlans) {
                    int idx = _tarifaires.indexOf(y);
                    if (idx < 0) {
                        champ = new String[]{"categorie", "article"};
                        val = new Object[]{y.getCategorie(), new YvsBaseArticles(a.getId())};
                        nameQueri = "YvsBasePlanTarifaire.findByCategorieArticle";
                        planT = (YvsBasePlanTarifaire) dao.loadOneByNameQueries(nameQueri, champ, val);
                        if (planT != null ? planT.getId() > 0 : false) {
                            dao.delete(planT);
                        }
                    }
                }

            }
            succes();
        }
    }

    public void deleteAll(List<YvsBaseArticleCategorieComptable> l) {
        for (YvsBaseArticleCategorieComptable c : l) {
            dao.delete(c);
        }
    }
}
