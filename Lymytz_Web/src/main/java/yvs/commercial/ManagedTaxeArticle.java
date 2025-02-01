/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial;

import yvs.base.compta.Taxes;
import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.ArticleTaxe;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.Comptes;
import yvs.base.compta.UtilCompta;
import yvs.base.produits.Articles;
import yvs.base.produits.ArticlesCatComptable;
import yvs.production.UtilProd;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.base.YvsBaseArticleCategorieComptableTaxe;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.produits.YvsBaseArticles;
import yvs.theme.ArticleService;
import yvs.theme.CompteService;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedTaxeArticle extends Managed<ArticlesCatComptable, YvsBaseCaisse> implements Serializable {

    private ArticlesCatComptable artcileCat = new ArticlesCatComptable();
    private List<YvsBaseArticleCategorieComptable> taxesArticles, taxesArticlesAll, historiques;
    private YvsBaseArticleCategorieComptable selectTaxeArticle;

    private List<YvsBaseArticleCategorieComptableTaxe> articlesTaxe;
    private YvsBaseArticleCategorieComptableTaxe selectArticleTaxe;
    private ArticleTaxe article = new ArticleTaxe();

    private CategorieComptable categorie = new CategorieComptable();
    private List<YvsBaseCategorieComptable> categories;

    private Taxes taxe = new Taxes();
    private List<YvsBaseTaxes> taxes;

    @ManagedProperty(value = "#{compteService}")
    private CompteService compteService;
    private YvsBasePlanComptable compteSelect;
    private Comptes compte = new Comptes();
    private List<YvsBasePlanComptable> comptes;

    @ManagedProperty(value = "#{articleService}")
    private ArticleService articleService;
    private YvsBaseArticles articleSelect;
    private List<YvsBaseArticles> articles;

    private String tabIds, tabIds_article;

    public ManagedTaxeArticle() {
        articles = new ArrayList<>();
        articlesTaxe = new ArrayList<>();
        taxesArticles = new ArrayList<>();
        taxesArticlesAll = new ArrayList<>();
        historiques = new ArrayList<>();
        categories = new ArrayList<>();
        taxes = new ArrayList<>();
        comptes = new ArrayList<>();
    }

    public CompteService getCompteService() {
        return compteService;
    }

    public void setCompteService(CompteService compteService) {
        this.compteService = compteService;
    }

    public YvsBasePlanComptable getCompteSelect() {
        return compteSelect;
    }

    public void setCompteSelect(YvsBasePlanComptable compteSelect) {
        this.compteSelect = compteSelect;
    }

    public YvsBaseArticleCategorieComptable getSelectTaxeArticle() {
        return selectTaxeArticle;
    }

    public void setSelectTaxeArticle(YvsBaseArticleCategorieComptable selectTaxeArticle) {
        this.selectTaxeArticle = selectTaxeArticle;
    }

    public List<YvsBaseArticleCategorieComptableTaxe> getArticlesTaxe() {
        return articlesTaxe;
    }

    public void setArticlesTaxe(List<YvsBaseArticleCategorieComptableTaxe> articlesTaxe) {
        this.articlesTaxe = articlesTaxe;
    }

    public YvsBaseArticleCategorieComptableTaxe getSelectArticleTaxe() {
        return selectArticleTaxe;
    }

    public void setSelectArticleTaxe(YvsBaseArticleCategorieComptableTaxe selectArticleTaxe) {
        this.selectArticleTaxe = selectArticleTaxe;
    }

    public List<YvsBaseCategorieComptable> getCategories() {
        return categories;
    }

    public void setCategories(List<YvsBaseCategorieComptable> categories) {
        this.categories = categories;
    }

    public ArticleTaxe getArticle() {
        return article;
    }

    public void setArticle(ArticleTaxe article) {
        this.article = article;
    }

    public String getTabIds_article() {
        return tabIds_article;
    }

    public void setTabIds_article(String tabIds_article) {
        this.tabIds_article = tabIds_article;
    }

    public ArticlesCatComptable getArtcileCat() {
        return artcileCat;
    }

    public void setArtcileCat(ArticlesCatComptable artcileCat) {
        this.artcileCat = artcileCat;
    }

    public List<YvsBaseArticleCategorieComptable> getTaxesArticles() {
        return taxesArticles;
    }

    public void setTaxesArticles(List<YvsBaseArticleCategorieComptable> taxesArticles) {
        this.taxesArticles = taxesArticles;
    }

    public List<YvsBaseArticleCategorieComptable> getTaxesArticlesAll() {
        return taxesArticlesAll;
    }

    public void setTaxesArticlesAll(List<YvsBaseArticleCategorieComptable> taxesArticlesAll) {
        this.taxesArticlesAll = taxesArticlesAll;
    }

    public List<YvsBaseArticleCategorieComptable> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(List<YvsBaseArticleCategorieComptable> historiques) {
        this.historiques = historiques;
    }

    public CategorieComptable getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieComptable categorie) {
        this.categorie = categorie;
    }

    public Taxes getTaxe() {
        return taxe;
    }

    public void setTaxe(Taxes taxe) {
        this.taxe = taxe;
    }

    public List<YvsBaseTaxes> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<YvsBaseTaxes> taxes) {
        this.taxes = taxes;
    }

    public Comptes getCompte() {
        return compte;
    }

    public void setCompte(Comptes compte) {
        this.compte = compte;
    }

    public List<YvsBasePlanComptable> getComptes() {
        return comptes;
    }

    public void setComptes(List<YvsBasePlanComptable> comptes) {
        this.comptes = comptes;
    }

    public List<YvsBaseArticles> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticles> articles) {
        this.articles = articles;
    }

    public ArticleService getArticleService() {
        return articleService;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    public YvsBaseArticles getArticleSelect() {
        return articleSelect;
    }

    public void setArticleSelect(YvsBaseArticles articleSelect) {
        this.articleSelect = articleSelect;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        loadAllArticlesCatComptable();
        loadAllArticle();
        loadAllCategorie();
        loadAllTaxes();
        loadAllComptes();
    }

    public void loadAllArticlesCatComptable() {
        taxesArticles = dao.loadNameQueries("YvsBaseArticleCategorieComptable.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllArticlesCatComptableHist() {
        historiques.clear();
        boolean correct = true;
        if ((artcileCat.getCategorie() != null) ? artcileCat.getCategorie().getId() > 0 : false) {
            if ((artcileCat.getCompte() != null) ? artcileCat.getCompte().getId() > 0 : false) {
                if ((artcileCat.getArticle() != null) ? artcileCat.getArticle().getId() > 0 : false) {
                    champ = new String[]{"categorie", "compte", "article"};
                    val = new Object[]{new YvsBaseCategorieComptable(artcileCat.getCategorie().getId()),
                        new YvsBasePlanComptable(artcileCat.getCompte().getId()),
                        new YvsBaseArticles(artcileCat.getArticle().getId())};
                    nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieArticleCompte";
                } else {
                    champ = new String[]{"categorie", "compte"};
                    val = new Object[]{new YvsBaseCategorieComptable(artcileCat.getCategorie().getId()),
                        new YvsBasePlanComptable(artcileCat.getCompte().getId())};
                    nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieCompte";
                }
            } else {
                if ((artcileCat.getArticle() != null) ? artcileCat.getArticle().getId() > 0 : false) {
                    champ = new String[]{"categorie", "article"};
                    val = new Object[]{new YvsBaseCategorieComptable(artcileCat.getCategorie().getId()),
                        new YvsBaseArticles(artcileCat.getArticle().getId())};
                    nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieArticle";
                } else {
                    champ = new String[]{"categorie"};
                    val = new Object[]{new YvsBaseCategorieComptable(artcileCat.getCategorie().getId())};
                    nameQueri = "YvsBaseArticleCategorieComptable.findByCategorie";
                }
            }
        } else {
            if ((artcileCat.getCompte() != null) ? artcileCat.getCompte().getId() > 0 : false) {
                if ((artcileCat.getArticle() != null) ? artcileCat.getArticle().getId() > 0 : false) {
                    champ = new String[]{"compte", "article"};
                    val = new Object[]{new YvsBasePlanComptable(artcileCat.getCompte().getId()),
                        new YvsBaseArticles(artcileCat.getArticle().getId())};
                    nameQueri = "YvsBaseArticleCategorieComptable.findByArticleCompte";
                } else {
                    champ = new String[]{"compte"};
                    val = new Object[]{new YvsBasePlanComptable(artcileCat.getCompte().getId())};
                    nameQueri = "YvsBaseArticleCategorieComptable.findByCompte";
                }
            } else {
                if ((artcileCat.getArticle() != null) ? artcileCat.getArticle().getId() > 0 : false) {
                    champ = new String[]{"article"};
                    val = new Object[]{new YvsBaseArticles(artcileCat.getArticle().getId())};
                    nameQueri = "YvsBaseArticleCategorieComptable.findByArticle";
                } else {
                    correct = false;
                }
            }
        }
        if (correct) {
            historiques = dao.loadNameQueries(nameQueri, champ, val);
        }
    }

    public void loadAllArticleTaxe(YvsBaseArticleCategorieComptable y) {
        articleService.getArticles().clear();
        articlesTaxe = dao.loadNameQueries("YvsBaseArticleCategorieComptableTaxe.findByArticleCategorie", new String[]{"articleCategorie"}, new Object[]{y});
        update("data_taxe_article_taxe");
    }

    public void loadAllArticle() {
        articleService.getArticles().clear();
        articles = dao.loadNameQueries("YvsBaseArticles.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        articleService.setArticles(articles);
    }

    public void loadAllCategorie() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        categories = dao.loadNameQueries("YvsBaseCategorieComptable.findAll", champ, val);
    }

    public void loadAllTaxes() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        taxes = dao.loadNameQueries("YvsBaseTaxes.findAll", champ, val);
    }

    public void loadAllComptes() {
        compteService.getComptes().clear();
        comptes = dao.loadNameQueries("YvsBasePlanComptable.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        compteService.setComptes(comptes);
    }

    public YvsBaseArticleCategorieComptable buildArticlesCatComptable(ArticlesCatComptable y) {
        YvsBaseArticleCategorieComptable t = new YvsBaseArticleCategorieComptable();
        if (y != null) {
            t.setActif(y.isActif());
            t.setId(y.getId());
            if ((y.getCompte() != null) ? y.getCompte().getId() > 0 : false) {
                t.setCompte(new YvsBasePlanComptable(y.getCompte().getId()));
            }
            if ((y.getArticle() != null) ? y.getArticle().getId() > 0 : false) {
                t.setArticle(new YvsBaseArticles(y.getArticle().getId()));
            }
            if ((y.getCategorie() != null) ? y.getCategorie().getId() > 0 : false) {
                t.setCategorie(new YvsBaseCategorieComptable(y.getCategorie().getId()));
            }
        }
        return t;
    }

    public YvsBaseCategorieComptable buildCategorieComptable(CategorieComptable y) {
        YvsBaseCategorieComptable c = new YvsBaseCategorieComptable();
        if (y != null) {
            c.setActif(y.isActif());
            c.setCode(y.getCodeCategorie());
            c.setCodeAppel(y.getCodeAppel());
            c.setId(y.getId());
            c.setNature(y.getNature());
            c.setSociete(currentAgence.getSociete());
        }
        return c;
    }

    public YvsBaseArticleCategorieComptableTaxe buildTaxeArticle(ArticleTaxe y) {
        YvsBaseArticleCategorieComptableTaxe t = new YvsBaseArticleCategorieComptableTaxe();
        if (y != null) {
            t.setActif(y.isActif());
            t.setAppRemise(y.isAppRemise());
            t.setId(y.getId());
            if ((y.getTaxe() != null) ? y.getTaxe().getId() > 0 : false) {
                t.setTaxe(new YvsBaseTaxes(y.getTaxe().getId()));
            }
            if ((y.getArticle() != null) ? y.getArticle().getId() > 0 : false) {
                t.setArticleCategorie(new YvsBaseArticleCategorieComptable(y.getArticle().getId()));
            }
        }
        return t;
    }

    public YvsBaseTaxes buildTaxes(Taxes y) {
        YvsBaseTaxes t = new YvsBaseTaxes();
        if (y != null) {
            t.setActif(y.isActif());
            t.setSupp(y.isSupp());
            t.setCodeAppel(y.getCodeAppel());
            t.setCodeTaxe(y.getCodeTaxe());
            t.setDesignation(y.getDesignation());
            t.setId(y.getId());
            t.setTaux(y.getTaux());
            t.setSociete(currentAgence.getSociete());
        }
        return t;
    }

    @Override
    public ArticlesCatComptable recopieView() {
        ArticlesCatComptable t = new ArticlesCatComptable();
        t.setActif(artcileCat.isActif());
        t.setId(artcileCat.getId());
        t.setCompte(artcileCat.getCompte());
        t.setArticle(artcileCat.getArticle());
        t.setCategorie(artcileCat.getCategorie());
        t.setUpdate(artcileCat.isUpdate());
        t.setNew_(true);
        return t;
    }

    public Taxes recopieViewTaxe() {
        Taxes t = new Taxes();
        t.setActif(taxe.isActif());
        t.setSupp(taxe.isSupp());
        t.setCodeAppel(taxe.getCodeAppel());
        t.setCodeTaxe(taxe.getCodeTaxe());
        t.setDesignation(taxe.getDesignation());
        t.setId(taxe.getId());
        t.setTaux(taxe.getTaux());
        t.setUpdate(taxe.isUpdate());
        t.setNew_(true);
        return t;
    }

    public CategorieComptable recopieViewCategorie() {
        CategorieComptable c = new CategorieComptable();
        c.setActif(categorie.isActif());
        c.setCodeCategorie(categorie.getCodeCategorie());
        c.setCodeAppel(categorie.getCodeAppel());
        c.setId(categorie.getId());
        c.setNature(categorie.getNature());
        c.setUpdate(categorie.isUpdate());
        return c;
    }

    public ArticleTaxe recopieViewTaxeArticle(ArticlesCatComptable art) {
        ArticleTaxe t = new ArticleTaxe();
        t.setActif(article.isActif());
        t.setAppRemise(article.isAppRemise());
        t.setId(article.getId());
        t.setUpdate(true);
        t.setTaxe(article.getTaxe());
        t.setArticle(art);
        t.setUpdate(article.isUpdate());
        t.setNew_(true);
        return t;
    }

    @Override
    public boolean controleFiche(ArticlesCatComptable bean) {
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        if ((bean.getCategorie() != null) ? bean.getCategorie().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier une categorie");
            return false;
        }
        if ((bean.getCompte() != null) ? bean.getCompte().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier un compte");
            return false;
        }
        return true;
    }

    public boolean controleFicheTaxe(Taxes bean) {
        if (bean.getCodeTaxe() == null || bean.getCodeTaxe().equals("")) {
            getErrorMessage("vous devez entrer le code de la taxe");
            return false;
        }
        return true;
    }

    public boolean controleFicheCategorie(CategorieComptable bean) {
        if (bean.getCodeCategorie() == null || bean.getCodeCategorie().equals("")) {
            getErrorMessage("vous devez entrer le code de la categorie");
            return false;
        }
        return true;
    }

    public boolean controleFicheTaxeArticle(ArticleTaxe bean) {
        if (!bean.getArticle().isUpdate()) {
            getErrorMessage("Vous devez specifier dabord enregistrer un article categorie comptable");
            return false;
        }
        if ((bean.getTaxe() != null) ? bean.getTaxe().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier une taxe");
            return false;
        }
        for (YvsBaseArticleCategorieComptableTaxe a : bean.getArticle().getTaxes()) {
            if (a.getTaxe().getId().equals(bean.getTaxe().getId())) {
                getErrorMessage("Cette taxe a deja ete attribué");
                return false;
            }
        }
        return true;
    }

    @Override
    public void populateView(ArticlesCatComptable bean) {
        cloneObject(artcileCat, bean);
    }

    public void populateViewTaxeArticle(ArticleTaxe bean) {
        cloneObject(article, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(artcileCat);
        artcileCat.setArticle(new Articles());
        artcileCat.setCompte(new Comptes());
        artcileCat.setCategorie(new CategorieComptable());
        artcileCat.setTaxes(new ArrayList<YvsBaseArticleCategorieComptableTaxe>());
        articleSelect = new YvsBaseArticles();
        compteSelect = new YvsBasePlanComptable();
        tabIds = "";
        historiques.clear();
        selectTaxeArticle = new YvsBaseArticleCategorieComptable();
        articlesTaxe.clear();
        resetFicheTaxeArticle();
        update("blog_form_taxe_article");
    }

    public void resetFicheTaxeArticle() {
        resetFiche(article);
        taxe = new Taxes();
        article.setTaxe(new Taxes());
        tabIds_article = "";
        selectArticleTaxe = new YvsBaseArticleCategorieComptableTaxe();
    }

    public void resetFicheTaxe() {
        resetFiche(taxe);
    }

    public void resetFicheCategorie() {
        resetFiche(categorie);
    }

    @Override
    public boolean saveNew() {
        String action = artcileCat.isUpdate() ? "Modification" : "Insertion";
        try {
            ArticlesCatComptable bean = recopieView();
            if (controleFiche(bean)) {
                selectTaxeArticle = buildArticlesCatComptable(bean);
                if (!bean.isUpdate()) {
                    selectTaxeArticle.setId(null);
                    selectTaxeArticle = (YvsBaseArticleCategorieComptable) dao.save1(selectTaxeArticle);
                    bean.setId(selectTaxeArticle.getId());
                    bean.setUpdate(true);
                    artcileCat.setId(selectTaxeArticle.getId());
                    taxesArticles.add(0, selectTaxeArticle);
                    historiques.add(0, selectTaxeArticle);
                } else {
                    dao.update(selectTaxeArticle);
                    bean.setUpdate(true);
                    taxesArticles.set(taxesArticles.indexOf(selectTaxeArticle), selectTaxeArticle);
                    if (historiques.contains(selectTaxeArticle)) {
                        historiques.set(historiques.indexOf(selectTaxeArticle), selectTaxeArticle);
                    }
                }
                succes();
                artcileCat.setId(0);
                artcileCat.setArticle(new Articles());
                articleSelect = new YvsBaseArticles();
                actionOpenOrResetAfter(this);
                update("data_taxe_article");
                update("data_taxe_article_hist");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewArticle() {
        String action = article.isUpdate() ? "Modification" : "Insertion";
        try {
            ArticleTaxe bean = recopieViewTaxeArticle(artcileCat);
            if (controleFicheTaxeArticle(bean)) {
                YvsBaseArticleCategorieComptableTaxe entity = buildTaxeArticle(bean);
                if (!bean.isUpdate()) {
                    entity.setId(null);
                    entity = (YvsBaseArticleCategorieComptableTaxe) dao.save1(entity);
                    articlesTaxe.add(0, entity);
                } else {
                    dao.update(entity);
                    articlesTaxe.set(articlesTaxe.indexOf(entity), entity);
                }
                succes();
                resetFicheTaxeArticle();
                update("data_taxe_article_taxe");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewTaxe() {
        try {
            Taxes bean = recopieViewTaxe();
            if (controleFicheTaxe(bean)) {
                YvsBaseTaxes entity = buildTaxes(bean);
                entity.setId(null);
                entity = (YvsBaseTaxes) dao.save1(entity);
                bean.setId(entity.getId());
                taxes.add(0, entity);
                article.setTaxe(bean);
                succes();
                resetFicheTaxe();
                update("select_taxe_taxe");
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            System.err.println("Error Insertion : " + ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean saveNewCategorie() {
        try {
            CategorieComptable bean = recopieViewCategorie();
            if (controleFicheCategorie(bean)) {
                YvsBaseCategorieComptable entity = buildCategorieComptable(bean);
                entity.setId(null);
                entity = (YvsBaseCategorieComptable) dao.save1(entity);
                bean.setId(entity.getId());
                categories.add(0, entity);
                artcileCat.setCategorie(bean);
                succes();
                resetFicheCategorie();
                update("select_categorie_taxe");
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            System.err.println("Error Insertion : " + ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                String[] tab = tabIds.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsBaseArticleCategorieComptable bean = taxesArticles.get(taxesArticles.indexOf(new YvsBaseArticleCategorieComptable(id)));
                    dao.delete(new YvsBaseArticleCategorieComptable(bean.getId()));
                    taxesArticles.remove(bean);
                    if (historiques.contains(bean)) {
                        historiques.remove(bean);
                    }
                }
                succes();
                update("data_taxe_article");
                update("data_taxe_article_hist");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBean_(YvsBaseArticleCategorieComptable y) {
        selectTaxeArticle = y;
    }

    public void deleteBean_() {
        try {
            if (selectTaxeArticle != null) {
                dao.delete(selectTaxeArticle);
                taxesArticles.remove(selectTaxeArticle);
                if (historiques.contains(selectTaxeArticle)) {
                    historiques.remove(selectTaxeArticle);
                }

                succes();
                update("data_taxe_article");
                update("data_taxe_article_hist");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBeanArticle() {
        try {
            if ((tabIds_article != null) ? !tabIds_article.equals("") : false) {
                String[] tab = tabIds_article.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsBaseArticleCategorieComptableTaxe bean = articlesTaxe.get(articlesTaxe.indexOf(new YvsBaseArticleCategorieComptableTaxe(id)));
                    dao.delete(new YvsBaseArticleCategorieComptableTaxe(bean.getId()));
                    articlesTaxe.remove(bean);
                }
                succes();
                update("data_taxe_article_taxe");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBeanArticle_(YvsBaseArticleCategorieComptableTaxe y) {
        selectArticleTaxe = y;
    }

    public void deleteBeanArticle_() {
        try {
            if (selectArticleTaxe != null) {
                dao.delete(selectArticleTaxe);
                articlesTaxe.remove(selectArticleTaxe);

                succes();
                update("data_taxe_article_taxe");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticleCategorieComptable bean = (YvsBaseArticleCategorieComptable) ev.getObject();
            articleSelect = bean.getArticle();
            compteSelect = bean.getCompte();
            populateView(UtilCom.buildBeanArticleCategorie(bean));
            loadAllArticlesCatComptableHist();
            loadAllArticleTaxe(bean);
            update("blog_form_taxe_article");
        }
    }

    public void loadOnView_(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticleCategorieComptable bean = (YvsBaseArticleCategorieComptable) ev.getObject();
            articleSelect = bean.getArticle();
            compteSelect = bean.getCompte();
            populateView(UtilCom.buildBeanArticleCategorie(bean));
            loadAllArticleTaxe(bean);
            update("form_taxe_article");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("form_taxe_article");
        update("blog_form_taxe_article_taxe");
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticleCategorieComptableTaxe bean = (YvsBaseArticleCategorieComptableTaxe) ev.getObject();
            populateViewTaxeArticle(UtilCom.buildBeanTaxeArticle(bean));
            update("form_taxe_article_taxe");
        }
    }

    public void unLoadOnViewArticle(UnselectEvent ev) {
        resetFicheTaxeArticle();
        update("form_taxe_article_taxe");
    }

    public void chooseTaxe() {
        if ((article.getTaxe() != null) ? article.getTaxe().getId() > 0 : false) {
            YvsBaseTaxes d_ = taxes.get(taxes.indexOf(new YvsBaseTaxes(article.getTaxe().getId())));
            Taxes d = UtilCom.buildBeanTaxes(d_);
            cloneObject(article.getTaxe(), d);
        } else if ((article.getTaxe() != null) ? article.getTaxe().getId() == -1 : false) {
            openDialog("dlgAddTaxe");
        } else {
            article.setTaxe(new Taxes());
        }
    }

    public void chooseCategorie() {
        if ((artcileCat.getCategorie() != null) ? artcileCat.getCategorie().getId() > 0 : false) {
            YvsBaseCategorieComptable d_ = categories.get(categories.indexOf(new YvsBaseCategorieComptable(artcileCat.getCategorie().getId())));
            CategorieComptable d = UtilCom.buildBeanCategorieComptable(d_);
            cloneObject(artcileCat.getCategorie(), d);
        } else if ((artcileCat.getCategorie() != null) ? artcileCat.getCategorie().getId() == -1 : false) {
            openDialog("dlgAddCategorieComp");
        } else {
            artcileCat.setCategorie(new CategorieComptable());
        }
        loadAllArticlesCatComptableHist();
        update("data_taxe_article_hist");
    }

    public void onArticleSelect(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles t_ = (YvsBaseArticles) ev.getObject();
            t_ = articles.get(articles.indexOf(new YvsBaseArticles(t_.getId())));
            Articles t = UtilProd.buildBeanArticles(t_);
            cloneObject(artcileCat.getArticle(), t);
        } else {
            artcileCat.setArticle(new Articles());
        }
        loadAllArticlesCatComptableHist();
        update("data_taxe_article_hist");
    }

    public void onArticleBlur() {
        if ((articleSelect != null)
                ? (articleSelect.getDesignation() != null)
                ? articleSelect.getDesignation().trim().equals("")
                : true : true) {
            artcileCat.setArticle(new Articles());
            loadAllArticlesCatComptableHist();
            update("data_taxe_article_hist");
        }
    }

    public void onCompteSelect(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBasePlanComptable t_ = (YvsBasePlanComptable) ev.getObject();
            t_ = comptes.get(comptes.indexOf(new YvsBasePlanComptable(t_.getId())));
            Comptes t = UtilCompta.buildBeanCompte(t_);
            cloneObject(artcileCat.getCompte(), t);
        } else {
            artcileCat.setCompte(new Comptes());
        }
        loadAllArticlesCatComptableHist();
        update("data_taxe_article_hist");
    }

    public List<YvsBaseArticles> completeArticle(String query) {
        List<YvsBaseArticles> allThemes = articleService.getArticles();
        List<YvsBaseArticles> filteredThemes = new ArrayList<>();
        for (YvsBaseArticles t : allThemes) {
            if (t.getRefArt().toLowerCase().startsWith(query.toLowerCase())) {
                filteredThemes.add(t);
            }
        }
        return filteredThemes;
    }

    public List<YvsBasePlanComptable> completeCompte(String query) {
        List<YvsBasePlanComptable> allThemes = compteService.getComptes();
        List<YvsBasePlanComptable> filteredThemes = new ArrayList<>();
        for (YvsBasePlanComptable t : allThemes) {
            if (t.getNumCompte().toLowerCase().startsWith(query.toLowerCase())) {
                filteredThemes.add(t);
            }
        }
        return filteredThemes;
    }

    public void activeTaxe(YvsBaseArticleCategorieComptable bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            taxesArticles.get(taxesArticles.indexOf(bean)).setActif(bean.getActif());
            String rq = "UPDATE yvs_base_article_categorie_comptable SET actif=" + bean.getActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            update("data_taxe_article");
        }
    }

    public void activeArticleTaxe(YvsBaseArticleCategorieComptableTaxe bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            articlesTaxe.get(articlesTaxe.indexOf(bean)).setActif(bean.getActif());
            String rq = "UPDATE yvs_base_article_categorie_comptable_taxe SET actif=" + bean.getActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
