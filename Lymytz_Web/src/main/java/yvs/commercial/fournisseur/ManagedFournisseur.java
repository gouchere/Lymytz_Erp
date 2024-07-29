/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.fournisseur;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.UploadedFile;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.Comptes;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.ModelReglement;
import yvs.base.compta.UtilCompta;
import yvs.base.produits.ArticleFournisseur;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.base.produits.ManagedArticles;
import yvs.base.produits.ManagedUniteMesure;
import yvs.production.UtilProd;
import yvs.base.tiers.ManagedTiers;
import yvs.commercial.UtilCom;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseArticleFournisseur;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseCategorieFournisseur;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.commercial.YvsBaseOperationCompteFsseur;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.tiers.YvsBaseTiers;

import yvs.base.tiers.Tiers;
import yvs.base.tiers.UtilTiers;
import yvs.commercial.ManagedCatCompt;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.ManagedModeReglement;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseConditionnementFournisseur;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.ext.YvsExtFournisseur;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsAgences;
import yvs.entity.tiers.YvsBaseTiersTemplate;
import yvs.grh.UtilGrh;
import static yvs.init.Initialisation.USER_DOWNLOAD;
import static yvs.init.Initialisation.USER_DOWNLOAD_LINUX;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.dico.ManagedDico;
import yvs.util.Constantes;
import static yvs.util.Managed.isWindows;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedFournisseur extends ManagedCommercial<Fournisseur, YvsBaseFournisseur> implements Serializable {

    @ManagedProperty(value = "#{fournisseur}")
    private Fournisseur fournisseur;
    private YvsBaseFournisseur selectFsseur;
    private List<YvsBaseFournisseur> fournisseurs;
    private List<Long> ids_fournisseurs;

    private ArticleFournisseur article = new ArticleFournisseur();
    private List<YvsBaseArticleFournisseur> articles;
    private YvsBaseArticleFournisseur selectTarif;
    private ConditionnementFsseur unite = new ConditionnementFsseur();

    private OpeCptFsseur operation = new OpeCptFsseur();
    private List<YvsBaseOperationCompteFsseur> operations;
    private YvsBaseOperationCompteFsseur selectOperation;

    private List<YvsBaseTiers> tiersLoad;

    private YvsBaseTiersTemplate selectTemplate;

    private boolean updateFournisseur, updateArticle, generation;
    private String tabIds, tabIds_article, tabIds_operation;
    private String fusionneTo;
    private List<String> fusionnesBy;

    private UploadedFile file;

    private boolean actionEmploye = true, actionTiers = true, actionClient = true, actionCommercial = true;

    private String typeSearch, compteSearch, codeExterne;
    private long paysSearch, villeSearch, secteurSearch, categorieSearch, groupeSearch;
    private Boolean actifSearch;
    private boolean _first = true, displayParamExt;

    public ManagedFournisseur() {
        operations = new ArrayList<>();
        tiersLoad = new ArrayList<>();
        fournisseurs = new ArrayList<>();
        articles = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
        ids_fournisseurs = new ArrayList<>();
    }

    public boolean isActionEmploye() {
        return actionEmploye;
    }

    public void setActionEmploye(boolean actionEmploye) {
        this.actionEmploye = actionEmploye;
    }

    public boolean isActionTiers() {
        return actionTiers;
    }

    public void setActionTiers(boolean actionTiers) {
        this.actionTiers = actionTiers;
    }

    public boolean isActionClient() {
        return actionClient;
    }

    public void setActionClient(boolean actionClient) {
        this.actionClient = actionClient;
    }

    public boolean isActionCommercial() {
        return actionCommercial;
    }

    public void setActionCommercial(boolean actionCommercial) {
        this.actionCommercial = actionCommercial;
    }

    public List<Long> getIds_fournisseurs() {
        return ids_fournisseurs;
    }

    public void setIds_fournisseurs(List<Long> ids_fournisseurs) {
        this.ids_fournisseurs = ids_fournisseurs;
    }

    public ConditionnementFsseur getUnite() {
        return unite;
    }

    public void setUnite(ConditionnementFsseur unite) {
        this.unite = unite;
    }

    public boolean isFirst() {
        return _first;
    }

    public void setFirst(boolean _first) {
        this._first = _first;
    }

    public boolean isDisplayParamExt() {
        return displayParamExt;
    }

    public void setDisplayParamExt(boolean displayParamExt) {
        this.displayParamExt = displayParamExt;
    }

    public String getCodeExterne() {
        return codeExterne;
    }

    public void setCodeExterne(String codeExterne) {
        this.codeExterne = codeExterne;
    }

    public String getFusionneTo() {
        return fusionneTo;
    }

    public void setFusionneTo(String fusionneTo) {
        this.fusionneTo = fusionneTo;
    }

    public List<String> getFusionnesBy() {
        return fusionnesBy;
    }

    public void setFusionnesBy(List<String> fusionnesBy) {
        this.fusionnesBy = fusionnesBy;
    }

    public List<YvsBaseFournisseur> getFournisseurs() {
        return fournisseurs;
    }

    public void setFournisseurs(List<YvsBaseFournisseur> fournisseurs) {
        this.fournisseurs = fournisseurs;
    }

    public String getCompteSearch() {
        return compteSearch;
    }

    public void setCompteSearch(String compteSearch) {
        this.compteSearch = compteSearch;
    }

    public boolean isGeneration() {
        return generation;
    }

    public void setGeneration(boolean generation) {
        this.generation = generation;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public YvsBaseTiersTemplate getSelectTemplate() {
        return selectTemplate;
    }

    public void setSelectTemplate(YvsBaseTiersTemplate selectTemplate) {
        this.selectTemplate = selectTemplate;
    }

    public long getGroupeSearch() {
        return groupeSearch;
    }

    public void setGroupeSearch(long groupeSearch) {
        this.groupeSearch = groupeSearch;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public long getPaysSearch() {
        return paysSearch;
    }

    public void setPaysSearch(long paysSearch) {
        this.paysSearch = paysSearch;
    }

    public long getVilleSearch() {
        return villeSearch;
    }

    public void setVilleSearch(long villeSearch) {
        this.villeSearch = villeSearch;
    }

    public long getSecteurSearch() {
        return secteurSearch;
    }

    public void setSecteurSearch(long secteurSearch) {
        this.secteurSearch = secteurSearch;
    }

    public long getCategorieSearch() {
        return categorieSearch;
    }

    public void setCategorieSearch(long categorieSearch) {
        this.categorieSearch = categorieSearch;
    }

    public List<YvsBaseOperationCompteFsseur> getOperations() {
        return operations;
    }

    public void setOperations(List<YvsBaseOperationCompteFsseur> operations) {
        this.operations = operations;
    }

    public YvsBaseOperationCompteFsseur getSelectOperation() {
        return selectOperation;
    }

    public void setSelectOperation(YvsBaseOperationCompteFsseur selectOperation) {
        this.selectOperation = selectOperation;
    }

    public List<YvsBaseArticleFournisseur> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticleFournisseur> articles) {
        this.articles = articles;
    }

    public YvsBaseArticleFournisseur getSelectTarif() {
        return selectTarif;
    }

    public void setSelectTarif(YvsBaseArticleFournisseur selectTarif) {
        this.selectTarif = selectTarif;
    }

    public YvsBaseFournisseur getSelectFsseur() {
        return selectFsseur;
    }

    public void setSelectFsseur(YvsBaseFournisseur selectFsseur) {
        this.selectFsseur = selectFsseur;
    }

    public List<YvsBaseTiers> getTiersLoad() {
        return tiersLoad;
    }

    public void setTiersLoad(List<YvsBaseTiers> tiersLoad) {
        this.tiersLoad = tiersLoad;
    }

    public String getTabIds_operation() {
        return tabIds_operation;
    }

    public void setTabIds_operation(String tabIds_operation) {
        this.tabIds_operation = tabIds_operation;
    }

    public OpeCptFsseur getOperation() {
        return operation;
    }

    public void setOperation(OpeCptFsseur operation) {
        this.operation = operation;
    }

    public String getTabIds_article() {
        return tabIds_article;
    }

    public void setTabIds_article(String tabIds_article) {
        this.tabIds_article = tabIds_article;
    }

    public boolean isUpdateArticle() {
        return article.getId() > 0;
    }

    public void setUpdateArticle(boolean updateArticle) {
        this.updateArticle = updateArticle;
    }

    public ArticleFournisseur getArticle() {
        return article;
    }

    public void setArticle(ArticleFournisseur article) {
        this.article = article;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public boolean isUpdateFournisseur() {
        return updateFournisseur;
    }

    public void setUpdateFournisseur(boolean updateFournisseur) {
        this.updateFournisseur = updateFournisseur;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        loadAllFournisseur(true, true);
        loadComptes();
        _first = true;

        if (fournisseur != null ? fournisseur.getModel() != null ? fournisseur.getModel().getId() < 0 : false : false) {
            fournisseur.getModel().setId(0);
        }
    }

    public void init(Boolean load) {

    }

    public void loadAllFournisseur_(boolean avance, boolean init) {
        loadAllFournisseur_(avance, init, true);
    }

    public void loadAllFournisseur_(boolean avance, boolean init, boolean count) {
        paginator.addParam(new ParametreRequete("y.tiers.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        fournisseurs = paginator.executeDynamicQuery(count, "y", "y", "YvsBaseFournisseur y JOIN FETCH y.tiers", "y.codeFsseur", avance, init, (int) imax, "id", dao);
    }

    public void loadAllFournisseur(boolean avance, boolean init) {
        loadAllFournisseur_(avance, init);
        champ = paginator.getChamp();
        val = paginator.getVal();
        nameQueri = paginator.buildDynamicQuery(paginator.getParams(), "SELECT y.id FROM YvsBaseFournisseur y WHERE") + " ORDER BY y.nom, y.prenom, y.codeFsseur";
        ids_fournisseurs = dao.loadEntity(nameQueri, champ, val);

        if (fournisseurs != null ? fournisseurs.size() == 1 : false) {
            onSelectObject(fournisseurs.get(0));
            execute("collapseForm('fournisseur')");
        } else {
            execute("collapseList('fournisseur')");
        }
        update("data_fournisseur");
    }

    public void loadAllFournisseur(long imax) {
        if (_first) {
            clearParams();
        }
        this.imax = imax;
        _first = false;
        addParamActif();
    }

    public void loadActifFournisseur(Boolean actif) {
        if (_first) {
            clearParams();
        }
        actifSearch = actif;
        _first = false;
        addParamActif();
    }

    public void loadFournisseurActif() {
        fournisseurs = dao.loadNameQueries("YvsBaseFournisseur.findAllActif", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    private void loadAllArticle(YvsBaseFournisseur y) {
        champ = new String[]{"fournisseur"};
        val = new Object[]{y};
        articles = dao.loadNameQueries("YvsBaseArticleFournisseur.findByFournisseur", champ, val);
    }

    public void loadAllArticle(YvsBaseArticles y) {
        champ = new String[]{"article"};
        val = new Object[]{y};
        articles = dao.loadNameQueries("YvsBaseArticleFournisseur.findByArticle", champ, val);
    }

    public void loadAllArticleAndFournisseur(YvsBaseArticles y) {
        champ = new String[]{"article"};
        val = new Object[]{y};
        articles = dao.loadNameQueries("YvsBaseArticleFournisseur.findByArticle", champ, val);
        //charge les fournisseur
        List<YvsBaseFournisseur> lf = dao.loadNameQueries("YvsBaseFournisseur.findAllDiffArt", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        YvsBaseArticleFournisseur af;
        long id = -10000;
        for (YvsBaseFournisseur f : lf) {
            af = new YvsBaseArticleFournisseur(id++);
            af.setFournisseur(f);
            af.setArticle(y);
            articles.add(af);
        }
    }

    private void loadOperations(YvsBaseFournisseur y) {
        champ = new String[]{"fournisseur"};
        val = new Object[]{y};
        operations = dao.loadNameQueries("YvsBaseOperationCompteFsseur.findByFournisseur", champ, val);
    }

    public YvsBaseFournisseur buildFournisseur(Fournisseur y, YvsBaseTiers t) {
        YvsBaseFournisseur f = UtilCom.buildFournisseur(y, currentUser);
        if (y != null) {
            if ((t != null) ? t.getId() > 0 : false) {
                f.setTiers(t);
            }
            if ((y.getCategorie() != null) ? y.getCategorie().getId() > 0 : false) {
                ManagedCategorieFsseur m = (ManagedCategorieFsseur) giveManagedBean(ManagedCategorieFsseur.class);
                if (m != null) {
                    if (m.getCategories().contains(new YvsBaseCategorieFournisseur(y.getCategorie().getId()))) {
                        f.setCategorie(m.getCategories().get(m.getCategories().indexOf(new YvsBaseCategorieFournisseur(y.getCategorie().getId()))));
                    }
                }
            }
            if ((y.getCategorieComptable() != null) ? y.getCategorieComptable().getId() > 0 : false) {
                ManagedCatCompt m = (ManagedCatCompt) giveManagedBean(ManagedCatCompt.class);
                if (m != null) {
                    if (m.getCategories().contains(new YvsBaseCategorieComptable(y.getCategorieComptable().getId()))) {
                        f.setCategorieComptable(m.getCategories().get(m.getCategories().indexOf(new YvsBaseCategorieComptable(y.getCategorieComptable().getId()))));
                    }
                }
            }
            if (y.getCompteCollectif() != null ? y.getCompteCollectif().getId() > 0 : false) {
                f.setCompte(new YvsBasePlanComptable(y.getCompteCollectif().getId()));
            }
            if (y.getModel() != null ? y.getModel().getId() > 0 : false) {
                ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
                if (m != null) {
                    if (m.getModels().contains(new YvsBaseModelReglement(y.getModel().getId()))) {
                        f.setModel(m.getModels().get(m.getModels().indexOf(new YvsBaseModelReglement(y.getModel().getId()))));
                    }
                }
            } else {
                f.setModel(null);
            }
        }
        return f;
    }

    public YvsBaseArticleFournisseur buildArticleFournisseur(ArticleFournisseur f) {
        YvsBaseArticleFournisseur r = new YvsBaseArticleFournisseur();
        if (f != null) {
            r.setId(f.getId());
            r.setDelaiLivraison(f.getDelaiLivraison());
            r.setDureeGarantie(f.getDureeGarantie());
            r.setDureeVie(f.getDureeVie());
            r.setPuv(f.getPrix());
            r.setRemise(f.getRemise());
            if ((f.getArticle() != null) ? f.getArticle().getId() > 0 : false) {
                r.setArticle(new YvsBaseArticles(f.getArticle().getId(), f.getArticle().getRefArt(), f.getArticle().getDesignation()));
            }
            r.setUniteDelai(f.getUniteDelaisLiv());
            r.setUniteGarantie(f.getUniteDureeGaranti());
            r.setUniteVie(f.getUniteDureeVie());
            r.setRefArtExterne(f.getRefArtExterne());
            r.setDesArtExterne(f.getDesArtExterne());
            if (f.getFournisseur() != null ? f.getFournisseur().getId() > 0 : false) {
                r.setFournisseur(new YvsBaseFournisseur(f.getFournisseur().getId()));
            }
            r.setNatureRemise(f.getNatureRemise());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                r.setAuthor(currentUser);
            }
        }
        return r;
    }

    public YvsBaseModelReglement buildModelReglement(ModelReglement y) {
        YvsBaseModelReglement c = new YvsBaseModelReglement();
        if (y != null) {
            c.setId(y.getId());
            c.setReference(y.getDesignation());
            c.setDescription(y.getDescription());
            c.setActif(y.isActif());
            c.setSociete(currentAgence.getSociete());
            c.setType(y.getType());
            c.setNew_(true);
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                c.setAuthor(currentUser);
            }
        }
        return c;
    }

    public YvsBaseOperationCompteFsseur buildOpeCptFsseur(OpeCptFsseur y) {
        YvsBaseOperationCompteFsseur o = new YvsBaseOperationCompteFsseur();
        if (y != null) {
            o.setId(y.getId());
            o.setDateOperation((y.getDateOperation() != null) ? y.getDateOperation() : new Date());
            o.setHeureOperation((y.getHeureOperation() != null) ? y.getHeureOperation() : new Date());
            o.setMontant(y.getMontant());
            if (selectFsseur != null ? selectFsseur.getId() > 0 : false) {
                o.setFournisseur(selectFsseur);
            }
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                o.setAuthor(currentUser);
            }
        }
        return o;
    }

    public YvsBaseCategorieComptable buildCategorieComptable(CategorieComptable y) {
        YvsBaseCategorieComptable c = new YvsBaseCategorieComptable();
        if (y != null) {
            c.setActif(y.isActif());
            c.setCode(y.getCodeCategorie());
            c.setCodeAppel(y.getCodeAppel());
            c.setId(y.getId());
            c.setNature(y.getNature());
            c.setDesignation(y.getDesignation());
            c.setSociete(currentAgence.getSociete());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                c.setAuthor(currentUser);
            }
        }
        return c;
    }

    @Override
    public Fournisseur recopieView() {
        return fournisseur;
    }

    public ArticleFournisseur recopieViewArticle() {
        return recopieViewArticle(selectFsseur);
    }

    public ArticleFournisseur recopieViewArticle(YvsBaseFournisseur y) {
        if (y != null ? y.getId() > 0 : false) {
            article.setFournisseur(UtilCom.buildBeanFournisseur(y));
        } else {
            article.setFournisseur(article.getFournisseur());
        }
        return article;
    }

    public Tiers recopieViewTiers(Fournisseur fournisseur) {
        Tiers r = new Tiers();
        r.setId(fournisseur.getTiers().getId());
        r.setCodeTiers(fournisseur.getTiers().getCodeTiers());
        r.setAgence(fournisseur.getTiers().getAgence());
        r.setNom(fournisseur.getTiers().getNom());
        r.setPrenom(fournisseur.getTiers().getPrenom());
        r.setCodePostal(fournisseur.getTiers().getCodePostal());
        r.setCivilite(fournisseur.getTiers().getCivilite());
        r.setAdresse(fournisseur.getTiers().getAdresse());
        r.setEmail(fournisseur.getTiers().getEmail());
        r.setSecteur(fournisseur.getTiers().getSecteur());
        r.setVille(fournisseur.getTiers().getVille());
        r.setPays(fournisseur.getTiers().getPays());
        r.setBp(fournisseur.getTiers().getBp());
        r.setLogo(fournisseur.getTiers().getLogo());
        r.setResponsable(fournisseur.getTiers().getResponsable());
        r.setTelephone(fournisseur.getTiers().getTelephone());
        r.setActif(fournisseur.getTiers().isUpdate() ? fournisseur.getTiers().isActif() : true);

        r.setCompte(fournisseur.getTiers().getCompte());
        r.setCompteCollectif(fournisseur.getTiers().getCompteCollectif());
        r.setCategorieComptable(isUpdateFournisseur() ? fournisseur.getTiers().getCategorieComptable() : fournisseur.getCategorieComptable());
        r.setModelDeRglt(fournisseur.getTiers().getModelDeRglt());
        r.setPlanComission(fournisseur.getTiers().getPlanComission());
        r.setPlanRistourne(fournisseur.getTiers().getPlanRistourne());

        r.setSociete(fournisseur.getTiers().isSociete());
        r.setClient(fournisseur.getTiers().isClient());
        r.setFournisseur(true);
        r.setRepresentant(fournisseur.getTiers().isRepresentant());
        r.setFabricant(fournisseur.getTiers().isFabricant());
        r.setSite(fournisseur.getTiers().getSite());
        r.setUpdate(fournisseur.getTiers().isUpdate());
        return r;
    }

    public Dictionnaire recopie(Dictionnaire y, Dictionnaire p, String Titre) {
        Dictionnaire d = new Dictionnaire();
        d.setId(y.getId());
        d.setLibelle(y.getLibelle());
        d.setAbreviation(y.getAbreviation());
        d.setParent(p);
        d.setTitre(Titre);
        return d;
    }

    public OpeCptFsseur recopieViewOperation() {
        operation.setDateOperation((operation.getDateOperation() != null) ? operation.getDateOperation() : new Date());
        operation.setHeureOperation((operation.getHeureOperation() != null) ? operation.getHeureOperation() : new Date());
        operation.setNew_(true);
        return operation;
    }

    @Override
    public boolean controleFiche(Fournisseur bean) {
        return controleFiche(bean, true);
    }

    @Override
    public void onSelectDistant(YvsBaseFournisseur y) {
        Navigations n = (Navigations) giveManagedBean(Navigations.class);
        if (n != null) {
            n.naviguationView("Fournisseurs", "modDonneBase", "smenFournisseurCom", true);
        }
        onSelectObject(y);
    }

    public boolean controleFiche(Fournisseur bean, boolean controle) {
        if ((bean.getTiers() != null) ? bean.getTiers().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le tiers");
            return false;
        }
        if (bean.getCodeFsseur() == null || bean.getCodeFsseur().trim().equals("")) {
            getErrorMessage("Vous devez entrer le code fournisseur");
            return false;
        }
        if (controle) {
            if ((bean.getCategorieComptable() != null) ? bean.getCategorieComptable().getId() < 1 : false) {
                getErrorMessage("Vous devez entrer la catégorie comptable");
                return false;
            }
        }
        YvsBaseFournisseur t = (YvsBaseFournisseur) dao.loadOneByNameQueries("YvsBaseFournisseur.findByCodeFsseur", new String[]{"codeFsseur", "societe"}, new Object[]{bean.getCodeFsseur(), currentAgence.getSociete()});
        if (t != null ? (t.getId() > 0 ? !t.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez déja crée un fournisseur avec ce code");
            return false;
        }
        return true;
    }

    public boolean controleFicheArticle(ArticleFournisseur bean) {
        if ((bean.getFournisseur() != null) ? bean.getFournisseur().getId() < 1 : true) {
            getErrorMessage("Vous devez enregsitrer un fournisseur");
            return false;
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        champ = new String[]{"article", "fournisseur"};
        val = new Object[]{new YvsBaseArticles(bean.getArticle().getId()), new YvsBaseFournisseur(bean.getFournisseur().getId())};
        YvsBaseArticleFournisseur a = (YvsBaseArticleFournisseur) dao.loadOneByNameQueries("YvsBaseArticleFournisseur.findByFsseurArt", champ, val);
        if (a != null ? a.getId() > 0 ? !a.getId().equals(bean.getId()) : false : false) {
            getErrorMessage("Vous avez déja associé cet article");
            return false;
        }
        return true;
    }

    public boolean controleFicheConditionnement(ConditionnementFsseur bean) {
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        if ((bean.getConditionnement() != null) ? bean.getConditionnement().getId() < 1 : true) {
            if (bean.getConditionnement().getId() == -1) {
                champ = new String[]{"article", "unite"};
                val = new Object[]{new YvsBaseArticles(bean.getArticle().getArticle().getId()), new YvsBaseUniteMesure(bean.getConditionnement().getUnite().getId())};
                YvsBaseConditionnement y = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findByArticleUnite", champ, val);
                if (y != null ? y.getId() < 1 : true) {
                    y = new YvsBaseConditionnement();
                    y.setArticle(new YvsBaseArticles(bean.getArticle().getArticle().getId()));
                    y.setAuthor(currentUser);
                    y.setDateSave(new Date());
                    y.setDateUpdate(new Date());
                    y.setPrixAchat(bean.getPua());
                    y.setUnite(new YvsBaseUniteMesure(bean.getConditionnement().getUnite().getId()));
                    y = (YvsBaseConditionnement) dao.save1(y);
                    ManagedArticles w = (ManagedArticles) giveManagedBean("Marticle");
                    if (w != null) {
                        int idx = w.getArticle().getConditionnements().indexOf(y);
                        if (idx < 0) {
                            w.getArticle().getConditionnements().add(y);
                            update("tbvArticle:data_conditionnement_article");
                        }
                    }
                }
                bean.getConditionnement().setId(y.getId());
            } else {
                getErrorMessage("Vous devez selectionner le conditionnement");
                return false;
            }
        }
        champ = new String[]{"article", "unite"};
        val = new Object[]{new YvsBaseArticleFournisseur(bean.getArticle().getId()), new YvsBaseConditionnement(bean.getConditionnement().getId())};
        YvsBaseConditionnementFournisseur y = (YvsBaseConditionnementFournisseur) dao.loadOneByNameQueries("YvsBaseConditionnementFournisseur.findOne", champ, val);
        if (y != null ? (y.getId() != null ? !y.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez deja associé ce conditionnement");
            return false;
        }
        return true;
    }

    public boolean controleFicheOperation(OpeCptFsseur bean) {
        if (!isUpdateFournisseur()) {
            getErrorMessage("Vous devez d'abord enregistrer le fournisseur");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(Fournisseur bean) {
        cloneObject(fournisseur, bean);
        setUpdateFournisseur(true);
    }

    public void populateViewArticle(ArticleFournisseur bean) {
        cloneObject(article, bean);
        setUpdateArticle(true);
    }

    public void populateViewOperation(OpeCptFsseur bean) {
        cloneObject(operation, bean);
    }

    @Override
    public void resetFiche() {
        Tiers tiers = new Tiers();
        ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (w != null) {
            tiers = w.resetTiers(fournisseur.getTiers());
        }
        resetFiche(fournisseur);
        fournisseur.setCategorie(new CategorieFournisseur());
        fournisseur.setCategorieComptable(new CategorieComptable());
        fournisseur.setCompteCollectif(new Comptes());
        fournisseur.setModel(new ModelReglement());
        fournisseur.setTiers(tiers);
        fournisseur.getTiers().setCivilite("M");
        tabIds = "";
        selectFsseur = null;

        articles.clear();
        operations.clear();

        generation = false;
        selectTemplate = null;
        codeExterne = "";

        setUpdateFournisseur(false);

        if (w != null) {
            w.resetFiche(false);
            w.resetFicheContact();
        }
        resetFicheArticle();
        resetFicheOperation();
        update("blog_form_fournisseur");
    }

    public void resetFicheArticle() {
        article = new ArticleFournisseur();
        article.setArticle(new Articles());
        tabIds_article = "";
        selectTarif = null;
        setUpdateArticle(false);
    }

    public void resetFicheOperation() {
        operation = new OpeCptFsseur();
        operation.setFournisseur(new Fournisseur());
        tabIds_operation = "";
        selectOperation = null;
    }

    @Override
    public boolean saveNew() {
        selectFsseur = saveNew(fournisseur);
        if (selectFsseur != null ? selectFsseur.getId() > 0 : false) {
            succes();
            actionOpenOrResetAfter(this);
            update("data_fournisseur");
            update("data_tiers0");
            return true;
        }
        return false;
    }

    public YvsBaseFournisseur saveNew(Fournisseur fournisseu) {
        YvsBaseTiers y = null;
        ManagedTiers service = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (service != null) {
            y = service.saveNew(recopieViewTiers(fournisseu));
        }
        return saveNew(fournisseu, y, true);
    }

    public YvsBaseFournisseur saveNew(Fournisseur fournisseur, YvsBaseTiers y, boolean controle) {
        String action = isUpdateFournisseur() ? "Modification" : "Insertion";
        try {
            if (y != null ? y.getId() > 0 : false) {
                Tiers tiers = UtilTiers.buildBeanTiers(y);
                fournisseur.setTiers(tiers);
                if (controleFiche(fournisseur, controle)) {
                    YvsBaseFournisseur en = buildFournisseur(fournisseur, y);
                    if (!isUpdateFournisseur()) {
                        if (!autoriser("base_fournisseur_save")) {
                            openNotAcces();
                            return null;
                        }
                        en.setId(null);
                        en = (YvsBaseFournisseur) dao.save1(en);
                        fournisseur.setId(en.getId());
                        fournisseurs.add(0, en);
                    } else {
                        if (!autoriser("base_fournisseur_update")) {
                            openNotAcces();
                            return null;
                        }
                        en.setDateUpdate(new Date());
                        dao.update(en);
                        int idx = fournisseurs.indexOf(en);
                        if (idx >= 0) {
                            fournisseurs.set(idx, en);
                        }
                    }
                    setUpdateFournisseur(true);
                    fournisseur.setTiers(tiers);
                    return en;
                }
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return null;
        }
        return null;
    }

    public void saveNewArticleFournisseur(YvsBaseArticleFournisseur af) {
        af.setId(null);
        af = (YvsBaseArticleFournisseur) dao.save1(af);
    }

    public YvsBaseArticleFournisseur saveNewArticle_(ArticleFournisseur bean) {
        String action = isUpdateArticle() ? "Modification" : "Insertion";
        YvsBaseArticleFournisseur entity = null;
        try {
            if (controleFicheArticle(bean)) {
                entity = buildArticleFournisseur(bean);
                if (bean.getId() <= 0) {
                    entity.setId(null);
                    entity = (YvsBaseArticleFournisseur) dao.save1(entity);
                    article.setId(entity.getId());
                    articles.add(0, entity);
                } else {
                    dao.update(entity);
                    int idx = articles.indexOf(entity);
                    if (idx > -1) {
                        articles.set(articles.indexOf(entity), entity);
                    }
                }
                update("data_article_fournisseur");
                update("data_fournisseur");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return null;
        }
        return entity;
    }

    public boolean saveNewArticle() {
        String action = isUpdateArticle() ? "Modification" : "Insertion";
        try {
            YvsBaseArticleFournisseur entity = saveNewArticle_(recopieViewArticle());
            if (entity != null) {
                if (article.getConditionnement().getUnite() != null ? article.getConditionnement().getUnite().getId() > 0 : false) {
                    if (article.getConditionnement().getId() == -1) {
                        champ = new String[]{"article", "unite"};
                        val = new Object[]{new YvsBaseArticles(entity.getArticle().getId()), new YvsBaseUniteMesure(article.getConditionnement().getUnite().getId())};
                        YvsBaseConditionnement _y = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findByArticleUnite", champ, val);
                        if (_y != null ? _y.getId() < 1 : true) {
                            _y = new YvsBaseConditionnement();
                            _y.setArticle(new YvsBaseArticles(entity.getArticle().getId()));
                            _y.setAuthor(currentUser);
                            _y.setDateSave(new Date());
                            _y.setDateUpdate(new Date());
                            _y.setPrixAchat(entity.getArticle().getPua());
                            _y.setUnite(new YvsBaseUniteMesure(article.getConditionnement().getUnite().getId()));
                            _y = (YvsBaseConditionnement) dao.save1(_y);
                            ManagedArticles w = (ManagedArticles) giveManagedBean("Marticle");
                            if (w != null) {
                                int idx = w.getArticle().getConditionnements().indexOf(_y);
                                if (idx < 0) {
                                    w.getArticle().getConditionnements().add(_y);
                                    update("tbvArticle:data_conditionnement_article");
                                }
                            }
                        }
                        article.getConditionnement().setId(_y.getId());
                    }
                    if (article.getConditionnement() != null ? article.getConditionnement().getId() > 0 : false) {
                        champ = new String[]{"article", "principal"};
                        val = new Object[]{entity, true};
                        YvsBaseConditionnementFournisseur y = (YvsBaseConditionnementFournisseur) dao.loadOneByNameQueries("YvsBaseConditionnementFournisseur.findByPrincipal", champ, val);
                        if (y != null ? (y.getId() != null ? y.getId() < 1 : true) : true) {
                            champ = new String[]{"article", "unite"};
                            val = new Object[]{entity, new YvsBaseConditionnement(article.getConditionnement().getId())};
                            y = (YvsBaseConditionnementFournisseur) dao.loadOneByNameQueries("YvsBaseConditionnementFournisseur.findOne", champ, val);
                            if (y != null ? (y.getId() != null ? y.getId() < 1 : true) : true) {
                                y = new YvsBaseConditionnementFournisseur();
                                y.setArticle(entity);
                                y.setConditionnement(new YvsBaseConditionnement(article.getConditionnement().getId()));
                                y.setPrincipal(true);
                                y.setPua(article.getPrix());
                                y.setDateSave(new Date());
                                y.setDateUpdate(new Date());
                                y.setAuthor(currentUser);
                                dao.save1(y);
                            } else {
                                y.setPrincipal(true);
                                y.setPua(article.getPrix());
                                y.setDateSave(new Date());
                                y.setDateUpdate(new Date());
                                y.setAuthor(currentUser);
                                dao.update(y);
                            }
                        }
                    }
                }
                succes();
            }
            resetFicheArticle();
            update("data_article_fournisseur");
            update("data_fournisseur");
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewOperation() {
        String action = operation.isUpdate() ? "Modification" : "Insertion";
        try {
            OpeCptFsseur bean = recopieViewOperation();
            if (controleFicheOperation(bean)) {
                YvsBaseOperationCompteFsseur entity = buildOpeCptFsseur(bean);
                if (!bean.isUpdate()) {
                    entity.setId(null);
                    entity = (YvsBaseOperationCompteFsseur) dao.save1(entity);
                    operation.setId(entity.getId());
                    operations.add(0, entity);
                } else {
                    dao.update(entity);
                    operations.set(operations.indexOf(bean), entity);
                }
                succes();
                resetFicheOperation();
                update("data_fournisseur_operation");
                update("blog_form_montant_solde");
                update("data_fournisseur");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public void saveNewConditionnement() {
        try {
            if (controleFicheConditionnement(unite)) {
                YvsBaseConditionnementFournisseur y = UtilCom.buildConditionnement(unite, currentUser);
                if (y.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsBaseConditionnementFournisseur) dao.save1(y);
                }
                int idx = selectTarif.getConditionnements().indexOf(y);
                if (idx > -1) {
                    selectTarif.getConditionnements().set(idx, y);
                } else {
                    selectTarif.getConditionnements().add(0, y);
                }
                unite.setConditionnement(new Conditionnement());
                unite.setPua(0);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            getException("Error : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteBean() {
        deleteBean(false);
    }

    public void deleteBean(boolean deleteTiers) {
        try {
            if (!autoriser("base_fournisseur_delete")) {
                openNotAcces();
                return;
            }
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsBaseFournisseur> list = new ArrayList<>();
                YvsBaseFournisseur bean;
                for (Long ids : l) {
                    int index = fournisseurs.indexOf(new YvsBaseFournisseur(ids));
                    if (index > -1) {
                        bean = fournisseurs.get(index);
                        list.add(bean);
                        bean.setAuthor(currentUser);
                        bean.setDateUpdate(new Date());
                        dao.delete(bean);
                        String rq = "UPDATE yvs_base_tiers SET fournisseur = false WHERE id=?";
                        if (deleteTiers) {
                            rq = "DELETE FROM yvs_base_tiers WHERE id=?";
                        }
                        Options[] param = new Options[]{new Options(bean.getTiers().getId(), 1)};
                        dao.requeteLibre(rq, param);
                        if (bean.getId() == fournisseur.getId()) {
                            resetFiche();
                        }
                    }
                }

                fournisseurs.removeAll(list);
                succes();
                update("data_fournisseur");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsBaseFournisseur y) {
        selectFsseur = y;
    }

    public void deleteBean_() {
        try {
            if (!autoriser("base_fournisseur_delete")) {
                openNotAcces();
                return;
            }
            if (selectFsseur != null) {
                dao.delete(selectFsseur);
                fournisseurs.remove(selectFsseur);
                String rq = "UPDATE yvs_base_tiers SET fournisseur = false WHERE id=?";
                Options[] param = new Options[]{new Options(selectFsseur.getTiers().getId(), 1)};
                dao.requeteLibre(rq, param);
                succes();
                if (selectFsseur.getId() == fournisseur.getId()) {
                    resetFiche();
                }
                update("data_fournisseur");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanArticle() {
        try {
            if ((tabIds_article != null) ? !tabIds_article.equals("") : false) {
                String[] tab = tabIds_article.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsBaseArticleFournisseur bean = articles.get(articles.indexOf(new YvsBaseArticleFournisseur(id)));
                    dao.delete(new YvsBaseArticleFournisseur(id));
                    articles.remove(bean);
                }
                succes();
                resetFicheArticle();
                update("data_fournisseur");
                update("data_article_fournisseur");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanArticle_(YvsBaseArticleFournisseur y) {
        selectTarif = y;
    }

    public void deleteBeanArticle_() {
        try {
            if (selectTarif != null) {
                dao.delete(selectTarif);
                articles.remove(selectTarif);
                succes();
                resetFicheArticle();
                update("data_fournisseur");
                update("data_article_fournisseur");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanConditionnement(YvsBaseConditionnementFournisseur y) {
        try {
            if (y != null) {
                dao.delete(y);
                selectTarif.getConditionnements().remove(y);
                if (y.getId().equals(unite.getId())) {
                    unite = new ConditionnementFsseur();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanOperation() {
        try {
            if ((tabIds_operation != null) ? !tabIds_operation.equals("") : false) {
                String[] tab = tabIds_operation.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    dao.delete(new YvsBaseOperationCompteFsseur(id));
                    YvsBaseOperationCompteFsseur bean = operations.get(operations.indexOf(new YvsBaseOperationCompteFsseur(id)));
                    operations.remove(bean);
                }
                succes();
                resetFicheOperation();
                update("blog_form_fournisseur_operation");
                update("blog_form_montant_solde");
                update("data_fournisseur_operation");
                update("data_fournisseur");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanOperation_(YvsBaseOperationCompteFsseur y) {
        selectOperation = y;
    }

    public void deleteBeanOperation_() {
        try {
            if (selectOperation != null) {
                dao.delete(selectOperation);
                operations.remove(selectOperation);
                succes();
                resetFicheOperation();
                update("blog_form_fournisseur_operation");
                update("blog_form_montant_solde");
                update("data_fournisseur_operation");
                update("data_fournisseur");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void onSelectObject(YvsBaseFournisseur bean) {
        selectFsseur = bean;
        populateView(UtilCom.buildBeanFournisseur(bean));
        if (bean.getTiers() != null) {
            ManagedTiers m = (ManagedTiers) giveManagedBean(ManagedTiers.class);
            if (m != null) {
                m.setSelectTiers(bean.getTiers());
                m.loadContact(bean.getTiers());
                m.resetFicheContact();
            }
            if (fournisseur.getNom() != null ? fournisseur.getNom().trim().length() < 1 : true) {
                fournisseur.setNom(bean.getTiers().getNom());
            }
            if (fournisseur.getPrenom() != null ? fournisseur.getPrenom().trim().length() < 1 : true) {
                fournisseur.setPrenom(bean.getTiers().getPrenom());
            }
            ManagedDico d = (ManagedDico) giveManagedBean("Mdico");
            if (d != null) {
                d.loadVilles(bean.getTiers().getPays());
                d.loadSecteurs(bean.getTiers().getVille());
            }
        }
        codeExterne = bean.getCodeExterne() != null ? bean.getCodeExterne().getCodeExterne() : "";

        loadOperations(bean);
        loadAllArticle(bean);

        generation = false;
        selectTemplate = null;

        resetFicheArticle();
        update("blog_form_fournisseur");
        update("txt_pays_ville_1");
        update("txt_pays_secteur_1");
        update("txt_ville_secteur_1");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseFournisseur bean = (YvsBaseFournisseur) ev.getObject();
            onSelectObject(bean);
            execute("collapseForm('fournisseur')");
            execute("slideZoneUp()");
            execute("collapseForm('fournisseur_contact')");
            execute("collapseForm('fournisseur_article')");
            execute("collapseForm('fournisseur_plan_reglement')");
            tabIds = fournisseurs.indexOf(bean) + "";

        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("blog_form_fournisseur");
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticleFournisseur bean = (YvsBaseArticleFournisseur) ev.getObject();
            populateViewArticle(UtilCom.buildBeanArticleFournisseur(bean));
            update("blog_form_fournisseur_article");
        }
    }

    public void unLoadOnViewArticle(UnselectEvent ev) {
        resetFicheArticle();
        update("blog_form_fournisseur_article");
    }

    public void loadOnViewConditionnement(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseConditionnementFournisseur bean = (YvsBaseConditionnementFournisseur) ev.getObject();
            unite = UtilCom.buildBeanConditionnement(bean);
        }
    }

    public void unLoadOnViewConditionnement(UnselectEvent ev) {
        unite = new ConditionnementFsseur();
    }

    public void loadOnViewOperation(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseOperationCompteFsseur bean = (YvsBaseOperationCompteFsseur) ev.getObject();
            populateViewOperation(UtilCom.buildBeanOpeCptFsseur(bean));
            update("blog_form_fournisseur_operation");
        }
    }

    public void unLoadOnViewOperation(UnselectEvent ev) {
        resetFicheOperation();
        update("blog_form_fournisseur_operation");
    }

    public void loadOnViewTiers(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseTiers y = (YvsBaseTiers) ev.getObject();
            Tiers bean = UtilTiers.buildBeanTiers(y);
            chooseTiers(bean);
        }
    }

    public void loadOnViewTemplate(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBaseTiersTemplate bean = (YvsBaseTiersTemplate) ev.getObject();
            copie(bean);
            ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
            if (m != null) {
                m.loadVilles(bean.getPays());
                m.loadSecteurs(bean.getVille());
            }
            update("infos_tiers_forunisseur");
        }
    }

    public void unLoadOnViewTemplate(UnselectEvent ev) {
        fournisseur.getTiers().setTemplate(null);
        update("txt_code_tiers");
    }

    public void loadOnViewArticle_(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            article.setArticle(UtilProd.buildBeanArticles(bean));
            article.setPrix(bean.getPuv());
            article.setRemise(bean.getRemise());
            update("form_fournisseur_article");
        }
    }

    public void loadOnViewCompte(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBasePlanComptable bean = (YvsBasePlanComptable) ev.getObject();
            fournisseur.setCompteCollectif(UtilCompta.buildBeanCompte(bean));
            update("txt_compte_fournisseur");
        }
    }

    public void loadOnViewModel(SelectEvent ev) {
        if (ev != null) {
            YvsBaseModelReglement y = (YvsBaseModelReglement) ev.getObject();
            ModelReglement m = UtilCom.buildBeanModelReglement(y);
            fournisseur.setModel(m);
            update("select_model_fournisseur");
        }
    }

    public void onArticleSelect(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles d = (YvsBaseArticles) ev.getObject();
            cloneObject(article.getArticle(), UtilGrh.buildBeanArticles(d));
            article.setPrix(d.getPuv());
            article.setRemise(d.getRemise());
        } else {
            resetFicheArticle();
        }
    }

    public void onArticleUnselect(UnselectEvent ev) {
        resetFicheArticle();
    }

    public void choosePays() {
        update("txt_pays_ville_1");
        update("txt_pays_secteur_1");
        fournisseur.getTiers().setVille(new Dictionnaire());
        fournisseur.getTiers().setSecteur(new Dictionnaire());
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            Dictionnaire d = m.choosePays(fournisseur.getTiers().getPays().getId());
            if (d != null ? d.getId() > 0 : false) {
                cloneObject(fournisseur.getTiers().getPays(), d);
            }
        }
    }

    public void chooseVille() {
        update("txt_ville_secteur_1");
        fournisseur.getTiers().setSecteur(new Dictionnaire());
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            Dictionnaire d = m.chooseVille(fournisseur.getTiers().getPays(), fournisseur.getTiers().getVille().getId());
            if (d != null ? d.getId() > 0 : false) {
                cloneObject(fournisseur.getTiers().getVille(), d);
            }
        }
    }

    public void chooseSecteur() {
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            Dictionnaire d = m.chooseSecteur(fournisseur.getTiers().getVille(), fournisseur.getTiers().getSecteur().getId());
            if (d != null ? d.getId() > 0 : false) {
                cloneObject(fournisseur.getTiers().getSecteur(), d);
            }
        }
    }

    public void chooseConditionnement() {
        ManagedUniteMesure w = (ManagedUniteMesure) giveManagedBean(ManagedUniteMesure.class);
        if (w != null) {
            if (unite.getUnite() != null ? unite.getUnite().getId() > 0 : false) {
                int idx = w.getUnites().indexOf(new YvsBaseUniteMesure(unite.getUnite().getId()));
                if (idx > -1) {
                    YvsBaseConditionnement y = new YvsBaseConditionnement((long) -1);
                    y.setUnite(w.getUnites().get(idx));
                    unite.setConditionnement(UtilProd.buildBeanConditionnement(y));
                }
            }
        }
    }

    public void chooseCondit() {
        ManagedUniteMesure w = (ManagedUniteMesure) giveManagedBean(ManagedUniteMesure.class);
        if (w != null) {
            if (article.getConditionnement().getUnite() != null ? article.getConditionnement().getUnite().getId() > 0 : false) {
                long id = article.getConditionnement().getUnite().getId();
                int idx = w.getUnites().indexOf(new YvsBaseUniteMesure(id));
                if (idx > -1) {
                    YvsBaseConditionnement y = new YvsBaseConditionnement((long) -1);
                    y.setUnite(w.getUnites().get(idx));
                    article.setConditionnement(UtilProd.buildBeanConditionnement(y));
                } else {
                    boolean pagine = false;
                    boolean next = false;
                    if (id == -1) {
                        pagine = true;
                        next = false;
                    } else if (id == -2) {
                        pagine = true;
                        next = true;
                    }
                    if (pagine) {
                        ManagedUniteMesure m = (ManagedUniteMesure) giveManagedBean(ManagedUniteMesure.class);
                        if (m != null) {
                            m.paginator.getParams().clear();
                            m.loadAll(next, pagine);
                        }
                    }
                    article.getConditionnement().getUnite().setId(0L);
                }
            }
        }
    }

    public void chooseArticle(YvsBaseArticles y) {
        if ((article.getArticle() != null) ? article.getArticle().getId() > 0 : false) {
            Articles d = UtilProd.buildBeanArticles(y);
            cloneObject(article.getArticle(), d);
        }
    }

    public void chooseTiers(Tiers bean) {
        fournisseur.setCodeFsseur(bean.getCodeTiers());
        cloneObject(fournisseur.getTiers(), bean);
        cloneObject(fournisseur.getCompteCollectif(), bean.getCompte());
        if (fournisseur.getNom() != null ? fournisseur.getNom().trim().length() < 1 : true) {
            fournisseur.setNom(bean.getNom());
        }
        if (fournisseur.getPrenom() != null ? fournisseur.getPrenom().trim().length() < 1 : true) {
            fournisseur.setPrenom(bean.getPrenom());
        }
        ManagedTiers m = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (m != null) {
            YvsBaseTiers y = new YvsBaseTiers(bean.getId());
            m.setSelectTiers(y);
            ManagedDico d = (ManagedDico) giveManagedBean("Mdico");
            if (d != null) {
                d.loadVilles(new YvsDictionnaire(bean.getPays().getId()));
                d.loadSecteurs(new YvsDictionnaire(bean.getVille().getId()));
            }
            m.loadContact(y);
        }
        update("chk_societe_fsseur");
        update("txt_code_tiers_fsseur");
        update("infos_tiers_forunisseur");
    }

    public void onCompteSelect(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBasePlanComptable y = (YvsBasePlanComptable) ev.getObject();
            y = comptes.get(comptes.indexOf(y));
            Comptes d = UtilCompta.buildBeanCompte(y);
            cloneObject(fournisseur.getCompteCollectif(), d);
        }
    }

    public void activeTiers(YvsBaseFournisseur bean) {
        if (bean != null) {
            if (!autoriser("base_fournisseur_update")) {
                openNotAcces();
                return;
            }
            bean.setActif(!bean.getActif());
            bean.setAuthor(currentUser);
            bean.setDateUpdate(new Date());
            dao.update(bean);
            if (actionTiers) {
                bean.getTiers().setActif(bean.getActif());
                bean.getTiers().setAuthor(currentUser);
                bean.getTiers().setDateUpdate(new Date());
                dao.update(bean.getTiers());
            }
            if (actionClient) {
                for (YvsComClient y : bean.getTiers().getClients()) {
                    y.setActif(bean.getActif());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                }
            }
            if (actionEmploye) {
                for (YvsGrhEmployes y : bean.getTiers().getEmployes()) {
                    y.setActif(bean.getActif());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                }
            }
            if (actionCommercial) {
                for (YvsComComerciale y : bean.getTiers().getCommerciaux()) {
                    y.setActif(bean.getActif());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                }
            }
            int index = fournisseurs.indexOf(bean);
            if (index > -1) {
                fournisseurs.set(index, bean);
                update("data_fournisseur");
            }
        }
    }

    public void activeGenerationCode() {
        if (!isGeneration() && (selectTemplate != null ? selectTemplate.getId() < 1 : true)) {
            getErrorMessage("Vous devez au préalable selectionner le template");
            return;
        }
        setGeneration(!isGeneration());
    }

    public void initComptes() {
        comptesResult.clear();
        comptesResult.addAll(comptes);
        update("data_comptes_fournisseur");
    }

    public void initArticles() {
        ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
        if (m != null) {
            m.initArticles("A", article.getArticle());
        }
        update("data_articles_fournisseur");
    }

    public void init(boolean next) {
        loadAllFournisseur(next, true);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllFournisseur(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllFournisseur(true, true);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsBaseFournisseur> re = paginator.parcoursDynamicData("YvsBaseFournisseur", "y", "y.codeFsseur", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void searchTiers() {
        String num = fournisseur.getTiers().getCodeTiers();
        fournisseur.getTiers().setId(0);
        fournisseur.getTiers().setError(true);
        ManagedTiers m = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (m != null) {
            Tiers y = m.findTiers(num, false);
            if (m.getListTiers() != null ? !m.getListTiers().isEmpty() : false) {
                if (m.getListTiers().size() > 1) {
                    update("data_tiers_fournisseur");
                } else {
                    chooseTiers(y);
                }
                fournisseur.getTiers().setError(false);
            }
        }
        if (!Util.asString(fournisseur.getCodeFsseur())) {
            fournisseur.setCodeFsseur(num);
            update("txt_code_fournisseur");
        }
    }

    public void searchCompte() {
        String num = fournisseur.getCompteCollectif().getNumCompte();
        fournisseur.getCompteCollectif().setId(0);
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
            if (service != null) {
                service.findCompteByNum(num);
                fournisseur.getCompteCollectif().setError(service.getListComptes().isEmpty());
                if (service.getListComptes() != null ? !service.getListComptes().isEmpty() : false) {
                    if (service.getListComptes().size() > 1) {
                        openDialog("dlgListComptes");
                        update("data_comptes_fournisseur");
                    } else {
                        YvsBasePlanComptable c = service.getListComptes().get(0);
                        fournisseur.setCompteCollectif(UtilCompta.buildBeanCompte(c));
                    }
                    fournisseur.getCompteCollectif().setError(false);
                } else {
                    fournisseur.getCompteCollectif().setError(true);
                }
            }
        }
    }

    public void searchArticle() {
        String num = article.getArticle().getRefArt();
        article.getArticle().setDesignation("");
        article.getArticle().setError(true);
        article.getArticle().setId(0);
        ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
        if (m != null) {
            Articles y = m.searchArticleActif("A", num, true);
            if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                if (m.getArticlesResult().size() > 1) {
                    update("data_articles_fournisseur");
                } else {
                    article.setArticle(y);
                    article.setPrix(y.getPuv());
                    article.setRemise(y.getRemise());
                    article.setPuaTtc(y.isPuaTtc());
                }
                article.getArticle().setError(false);
            }
        }
    }

    public void searchByNum() {
        ParametreRequete p = new ParametreRequete(null, "codeF", "fournisseur", "=", "AND");
        if (numSearch_ != null ? numSearch_.trim().length() > 0 : false) {
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.codeFsseur)", "code", "%" + numSearch_.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nom)", "code", "%" + numSearch_.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.prenom)", "code", "%" + numSearch_.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.tiers.codeTiers)", "code", "%" + numSearch_.trim().toUpperCase() + "%", "LIKE", "OR"));
        } else {
            p.setObjet(null);
        }
        paginator.addParam(p);
        loadAllFournisseur(true, true);

        if (fournisseurs != null ? fournisseurs.size() == 1 : false) {
            execute("collapseForm('fournisseur')");
        } else {
            execute("collapseList('fournisseur')");
        }
    }

    public void addParamCode(String num) {
        numSearch_ = num;
        searchByNum();
    }

    public void clearParams() {
        clearParams(true);
    }

    public void clearParams(boolean load) {
        typeSearch = null;
        paysSearch = 0;
        villeSearch = 0;
        secteurSearch = 0;
        numSearch_ = null;
        actifSearch = null;
        compteSearch = null;
        typeSearch = null;
        categorieSearch = 0;
        groupeSearch = 0;
        paginator.getParams().clear();
        _first = true;
        if (load) {
            loadAllFournisseur(true, true);
        }
        update("blog_plus_option_fournisseur");
    }

    public void addParamActif() {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", actifSearch);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        loadAllFournisseur(true, true);
    }

    public void addParamCompte() {
        ParametreRequete p;
        if (compteSearch != null ? compteSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.compte.numCompte", "numCompte", compteSearch + "%");
            p.setOperation(" LIKE ");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.compte.numCompte", "numCompte", null);
        }
        paginator.addParam(p);
        loadAllFournisseur(true, true);
    }

    public void addParamPays() {
        ParametreRequete p;
        if (paysSearch > 0) {
            p = new ParametreRequete("y.tiers.pays", "pays", new YvsDictionnaire(paysSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.tiers.pays", "pays", null);
            villeSearch = 0;
            addParamVille();
            secteurSearch = 0;
            addParamSecteur();
        }
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            m.loadVilles(new YvsDictionnaire(paysSearch));
        }
        paginator.addParam(p);
        loadAllFournisseur(true, true);
    }

    public void addParamVille() {
        ParametreRequete p;
        if (villeSearch > 0) {
            p = new ParametreRequete("y.tiers.ville", "ville", new YvsDictionnaire(villeSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.tiers.ville", "ville", null);
            secteurSearch = 0;
            addParamSecteur();
        }
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            m.loadSecteurs(new YvsDictionnaire(paysSearch));
        }
        paginator.addParam(p);
        loadAllFournisseur(true, true);
    }

    public void addParamSecteur() {
        ParametreRequete p;
        if (secteurSearch > 0) {
            p = new ParametreRequete("y.tiers.secteur", "secteur", new YvsDictionnaire(secteurSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.tiers.secteur", "secteur", null);
        }
        paginator.addParam(p);
        loadAllFournisseur(true, true);
    }

    public void addParamCategorie() {
        ParametreRequete p;
        if (categorieSearch > 0) {
            p = new ParametreRequete("y.categorieComptable", "categorieComptable", new YvsBaseCategorieComptable(categorieSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.categorieComptable", "categorieComptable", null);
        }
        paginator.addParam(p);
        loadAllFournisseur(true, true);
    }

    public void addParamGroupe() {
        ParametreRequete p;
        if (groupeSearch > 0) {
            p = new ParametreRequete("y.categorie", "categorie", new YvsBaseCategorieFournisseur(groupeSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.categorie", "categorie", null);
        }
        paginator.addParam(p);
        loadAllFournisseur(true, true);
    }

    public void addParamType() {
        switch (typeSearch) {
            case "SF": {
                addParamSte(true);
                addParamFab(null);
                break;
            }
            case "SFB": {
                addParamSte(true);
                addParamFab(true);
                break;
            }
            default: {
                addParamSte(null);
                addParamFab(null);
                break;
            }
        }
        loadAllFournisseur(true, true);
    }

    private void addParamSte(Boolean steSearch) {
        ParametreRequete p = new ParametreRequete("y.tiers.stSociete", "stSociete", steSearch);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
    }

    private void addParamFab(Boolean fabSearch) {
        ParametreRequete p = new ParametreRequete("y.tiers.fabriquant", "fabriquant", fabSearch);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
    }

    public void changeName() {
        changeName(fournisseur, selectTemplate, isGeneration());
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllFournisseur(true, true);
    }

    public void changeName(Fournisseur fournisseur, YvsBaseTiersTemplate y, boolean generation) {
        if (!fournisseur.getTiers().isUpdate()) {
            copie(fournisseur, y);
        }
        if (generation) {
            if (fournisseur.getTiers().isUpdate()) {
                ManagedTiers service = (ManagedTiers) giveManagedBean(ManagedTiers.class);
                if (service != null) {
                    fournisseur.setCodeFsseur(service.getCodeTiers(y, fournisseur.getTiers().getSecteur_(), fournisseur.getNom(), fournisseur.getPrenom()));
                }
                checkCode(fournisseur, true);
            }
            update("txt_code_fournisseur");
        }
        update("txt_code_tiers_fsseur");
    }

    public Fournisseur searchFsseur(boolean open) {
        return searchFsseur(fournisseur.getCodeFsseur(), open);
    }

    public Fournisseur searchFsseur(String num, boolean open) {
        Fournisseur a = new Fournisseur();
        a.setCodeFsseur(num);
        a.setError(true);
        paginator.addParam(new ParametreRequete("y.codeFsseur", "code", null));
        if (num != null ? num.trim().length() > 0 : false) {
            ParametreRequete p = new ParametreRequete(null, "code", num.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.codeFsseur)", "code", "%" + num.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nom)", "code", "%" + num.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(CONCAT(y.nom, ' ', y.prenom))", "code", "%" + num.trim().toUpperCase() + "%", "LIKE", "OR"));
            paginator.addParam(p);
        }
        loadAllFournisseur(true, true);
        a = chechFsseurResult(open);
        if (a != null ? a.getId() < 1 : true) {
            a.setCodeFsseur(num);
            a.setError(true);
        }
        return a;
    }

    private Fournisseur chechFsseurResult(boolean open) {
        Fournisseur a = new Fournisseur();
        if (fournisseurs != null ? !fournisseurs.isEmpty() : false) {
            if (fournisseurs.size() > 1) {
                if (open) {
                    openDialog("dlgListFournisseurs");
                }
                a.setViewList(true);
            } else {
                YvsBaseFournisseur c = fournisseurs.get(0);
                a = UtilCom.buildBeanFournisseur(c);
            }
            a.setError(false);
        }
        return a;
    }

    public void initFsseurs(Fournisseur a) {
        if (a == null) {
            a = new Fournisseur();
        }
        paginator.addParam(new ParametreRequete("y.codeFsseur", "code", null));
        loadAllFournisseur(true, true);
        a.setViewList(true);
    }

    public void initConditionnement(YvsBaseArticleFournisseur y) {
        if (y != null) {
            selectTarif = y;
            unite = new ConditionnementFsseur();
            unite.setArticle(UtilProd.buildBeanArticleFournisseur(y));
        }
    }

    public void uploadFile(FileUploadEvent event) {
        if (event != null) {
            String path = (isWindows() ? USER_DOWNLOAD : USER_DOWNLOAD_LINUX);
            try {
                file = event.getFile();
                File f = Util.createRessource(file);
                if (f != null) {
                    try {
                        buildData(f.getAbsolutePath());
                    } finally {
                        f.delete();
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(ManagedFournisseur.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        update("txt_name_file");
    }

    private void buildData(String path) {
        tiersLoad.clear();
        if (new File(path).exists()) {
            List<List<Object>> data = readFileXLS(path);
            if (data != null) {
                if (!data.isEmpty()) {
                    List<Object> head = data.get(0);
                    if (head != null) {
                        if (!head.isEmpty()) {
                            for (int i = 1; i < data.size(); i++) {
                                YvsBaseTiers c = new YvsBaseTiers((long) tiersLoad.size());
                                if (data.get(i).size() > 0) {
                                    c.setCodeTiers((String) data.get(i).get(0));
                                    if (data.get(i).size() > 1) {
                                        c.setNom((String) data.get(i).get(1));
                                        if (data.get(i).size() > 3) {
                                            Double d = (Double) data.get(i).get(3);
                                            String str = new DecimalFormat("#").format(d);
                                            YvsBasePlanComptable p = new YvsBasePlanComptable(str);
                                            c.setCompteCollectif(p);
                                        }
                                    }
                                    champ = new String[]{"societe", "code"};
                                    val = new Object[]{currentAgence.getSociete(), c.getCodeTiers()};
                                    nameQueri = "YvsBaseTiers.findByCode";
                                    List<YvsBaseTiers> l = dao.loadNameQueries(nameQueri, champ, val);
                                    c.setNew_(l != null ? !l.isEmpty() : false);
                                    tiersLoad.add(c);
                                }
                            }
                            openDialog("dlgLoadTiers");
                            update("form_tiers_fournisseur_load");
                        } else {
                            getErrorMessage("Fichier Vide");
                        }
                    } else {
                        getErrorMessage("Fichier Incorrect");
                    }
                } else {
                    getErrorMessage("Fichier Vide");
                }
            } else {
                getErrorMessage("Fichier Incorrect");
            }
        } else {
            getErrorMessage("Le fichier n'existe pas");
        }
    }

    public void fullTiers() {
        if (!tiersLoad.isEmpty()) {
            for (YvsBaseTiers t : tiersLoad) {
                if (!t.isNew_()) {
                    if (t.getCompteCollectif() != null ? !t.getCompteCollectif().getNumCompte().equals("") : false) {
                        YvsBasePlanComptable c = t.getCompteCollectif();
                        champ = new String[]{"societe", "numCompte"};
                        val = new Object[]{currentAgence.getSociete(), c.getNumCompte()};
                        YvsBasePlanComptable c_ = (YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findByNum", champ, val);
                        if (c_ != null ? c_.getId() < 1 : true) {
                            c.setActif(true);
                            if (currentUser != null ? currentUser.getId() > 0 : false) {
                                c.setAuthor(currentUser);
                            }
                            c.setId(null);
                            c = (YvsBasePlanComptable) dao.save1(c);
                        } else {
                            c = c_;
                        }
                        t.setCompteCollectif(c);
                    }
                    t.setSociete(currentAgence.getSociete());
                    t.setActif(true);
                    if (currentUser != null ? currentUser.getId() > 0 : false) {
                        t.setAuthor(currentUser);
                    }
                    t.setFournisseur(true);
                    champ = new String[]{"societe", "code"};
                    val = new Object[]{currentAgence.getSociete(), t.getCodeTiers()};
                    nameQueri = "YvsBaseTiers.findByCode";
                    List<YvsBaseTiers> l = dao.loadNameQueries(nameQueri, champ, val);
                    if (l != null ? l.isEmpty() : true) {
                        t.setId(null);
                        t = (YvsBaseTiers) dao.save1(t);
                    } else {
                        t = l.get(0);
                        t.setFournisseur(true);
                        dao.update(t);
                    }
                    if (t.getFournisseurs() != null ? t.getFournisseurs().isEmpty() : true) {
                        YvsBaseFournisseur c = new YvsBaseFournisseur((long) 0, t);
                        c.setCodeFsseur(t.getCodeTiers());

                        champ = new String[]{"societe", "code"};
                        val = new Object[]{currentAgence.getSociete(), c.getCodeFsseur()};
                        YvsBaseFournisseur c_ = (YvsBaseFournisseur) dao.loadOneByNameQueries("YvsBaseFournisseur.findByCode", champ, val);
                        if (c_ != null ? c_.getId() < 1 : true) {
                            if (t.getCompteCollectif() != null ? t.getCompteCollectif().getId() > 0 : false) {
                                c.setCompte(t.getCompteCollectif());
                            }
                            if (currentUser != null ? currentUser.getId() > 0 : false) {
                                c.setAuthor(currentUser);
                            }
                            c.setId(null);
                            c = (YvsBaseFournisseur) dao.save1(c);
                            if (fournisseurs.size() < getNbMax()) {
                                fournisseurs.add(c);
                            }
                        }
                    }
                }
            }
            update("data_fournisseur");
            tiersLoad.clear();
            succes();
        }
    }

    public void copie(YvsBaseTiersTemplate y) {
        copie(fournisseur, y);
    }

    public void copie(Fournisseur fournisseur, YvsBaseTiersTemplate y) {
        ManagedTiers service = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (service == null) {
            service = new ManagedTiers();
        }
        if (!fournisseur.getTiers().isUpdate()) {
            fournisseur.getTiers().setNom(fournisseur.getNom());
            fournisseur.getTiers().setPrenom(fournisseur.getPrenom());
            service.copie(y, fournisseur.getTiers());
            if (y != null ? y.getType() == 'F' : false) {
                fournisseur.setCompteCollectif(UtilCompta.buildBeanCompte(y.getCompteCollectif()));
                fournisseur.setCategorieComptable(UtilCom.buildBeanCategorieComptable(y.getCategorieComptable()));
            }
            fournisseur.setCodeFsseur(fournisseur.getTiers().getCodeTiers());
        } else {
            String code = service.getCodeTiers(y, fournisseur.getTiers().getSecteur_(), fournisseur.getNom(), fournisseur.getPrenom());
            if (code != null ? code.trim().length() > 0 : false) {
                fournisseur.setCodeFsseur(code);
            } else {
                fournisseur.setCodeFsseur(fournisseur.getTiers().getCodeTiers());
            }
        }
        checkCode(true);
    }

    public void checkCode(boolean by_tiers) {
        checkCode(fournisseur, by_tiers);
    }

    public void checkCode(Fournisseur fournisseur, boolean by_tiers) {
        fournisseur.setCodeFsseur(checkCode(fournisseur.getCodeFsseur(), fournisseur.getId(), by_tiers));
    }

    public String checkCode(String code, long id, boolean by_tiers) {
        List<YvsBaseFournisseur> lt = dao.loadNameQueries("YvsBaseFournisseur.findByCode", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), code}, 0, 1);
        if (lt != null ? !lt.isEmpty() : false) {
            if (!lt.get(0).getId().equals(id)) {
                for (int i = 1; i < 1000; i++) {
                    String num = code + (i < 10 ? "00" : (i < 100 ? "0" + i : i)) + i;
                    lt = dao.loadNameQueries("YvsBaseFournisseur.findByCode", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), num}, 0, 1);
                    if (lt != null ? (lt.isEmpty() ? true : (lt.get(0).getId().equals(id))) : true) {
                        code = num;
                        break;
                    }
                }
            } else if (!by_tiers) {
                code += "001";
            }
        } else if (!by_tiers) {
            code += "001";
        }
        return code;
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void changeDefaut() {
        if (fournisseur.isDefaut()) {
            YvsBaseFournisseur y = searchClientDefaut(fournisseur);
            if (y != null ? y.getId() > 0 : false) {
                fournisseur.setDefaut(false);
                return;
            }
            if (fournisseur.isDefaut()) {
                fournisseur.getTiers().setSociete(false);
            }
        }
    }

    public YvsBaseFournisseur searchClientDefaut(Fournisseur bean) {
        boolean in = false;
        if (bean.getTiers().getVille() != null ? bean.getTiers().getVille().getId() > 0 : false) {
            champ = new String[]{"societe", "ville"};
            val = new Object[]{currentAgence.getSociete(), new YvsDictionnaire(bean.getTiers().getVille().getId())};
            nameQueri = "YvsBaseFournisseur.findDefautVille";
            in = true;
        } else {
            if (bean.getTiers().getPays() != null ? bean.getTiers().getPays().getId() > 0 : false) {
                champ = new String[]{"societe", "pays"};
                val = new Object[]{currentAgence.getSociete(), new YvsDictionnaire(bean.getTiers().getPays().getId())};
                nameQueri = "YvsBaseFournisseur.findDefautPays";
                in = true;
            }
        }
        if (in) {
            List<YvsBaseFournisseur> lc = dao.loadNameQueries(nameQueri, champ, val);
            if (lc != null ? !lc.isEmpty() : false) {
                for (YvsBaseFournisseur c : lc) {
                    if (!c.getId().equals(bean.getId())) {
                        getErrorMessage("Il y'a deja un fournisseur par défaut pour ce secteur");
                        return c;
                    }
                }
            }
        }
        return null;
    }

    public void saveLiaison() {
        if (fournisseur.getId() > 0) {
            if ((codeExterne != null) ? codeExterne.trim().length() > 2 : false) {
                //il ne dois pas déjà exister un mappage pour ce client
                YvsExtFournisseur y = (YvsExtFournisseur) dao.loadOneByNameQueries("YvsExtFournisseur.findByFournisseur", new String[]{"fournisseur"}, new Object[]{new YvsBaseFournisseur(fournisseur.getId())});
                if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                    y.setCodeExterne(codeExterne);
                    y.setDateSave(y.getDateSave());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                } else {
                    y = new YvsExtFournisseur();
                    y.setFournisseur(new YvsBaseFournisseur(fournisseur.getId()));
                    y.setDateSave(new Date());
                    y.setAuthor(currentUser);
                    y.setCodeExterne(codeExterne);
                    y.setDateUpdate(new Date());
                    dao.save(y);
                }
                succes();
            } else {
                getErrorMessage("Veuillez entrer le code de liaison externe !");
            }
        } else {
            getErrorMessage("Aucun fournisseur n'est selectionné !");
        }
    }

    public void fusionner(boolean check) {
        try {
            fusionneTo = "";
            fusionnesBy.clear();
            if (tabIds != null ? tabIds.trim().length() > 0 : false) {
                String[] tabs = tabIds.split("-");
                if (tabs != null ? tabs.length > 0 : false) {
                    long newValue = Long.valueOf(tabs[0]);
                    if (!check) {
                        if (!autoriser("base_user_fusion")) {
                            openNotAcces();
                            return;
                        }
                        for (int i = 1; i < tabs.length; i++) {
                            long oldValue = Long.valueOf(tabs[i]);
                            if (dao.fusionneData("yvs_base_fournisseur", newValue, oldValue)) {
                                int idx = fournisseurs.indexOf(new YvsBaseFournisseur(oldValue));
                                if (idx > -1) {
                                    fournisseurs.remove(idx);
                                }
                            }
                        }
                        tabIds = "";
                        succes();
                    } else {
                        int idx = fournisseurs.indexOf(new YvsBaseFournisseur(newValue));
                        if (idx > -1) {
                            fusionneTo = fournisseurs.get(idx).getNom_prenom();
                        } else {
                            YvsBaseFournisseur c = (YvsBaseFournisseur) dao.loadOneByNameQueries("YvsBaseFournisseur.findById", new String[]{"id"}, new Object[]{newValue});
                            if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                                fusionneTo = c.getNom_prenom();
                            }
                        }
                        YvsBaseFournisseur c;
                        for (int i = 1; i < tabs.length; i++) {
                            long oldValue = Long.valueOf(tabs[i]);
                            idx = fournisseurs.indexOf(new YvsBaseFournisseur(oldValue));
                            if (idx > -1) {
                                fusionnesBy.add(fournisseurs.get(idx).getNom_prenom());
                            } else {
                                c = (YvsBaseFournisseur) dao.loadOneByNameQueries("YvsBaseFournisseur.findById", new String[]{"id"}, new Object[]{oldValue});
                                if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                                    fusionnesBy.add(c.getNom_prenom());
                                }
                            }
                        }
                    }
                } else {
                    getErrorMessage("Vous devez selectionner au moins 2 fournisseurs");
                }
            }
        } catch (NumberFormatException ex) {
        }
    }

    public double creance(YvsBaseFournisseur bean) {
        champ = new String[]{"fournisseur"};
        val = new Object[]{bean};
        nameQueri = "YvsComContenuDocAchat.findTTCByFournisseur";
        Double s = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setDebit((s != null ? s : 0));

        nameQueri = "YvsComptaCreditFournisseur.findCreanceByFournisseur";
        s = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setCreance(s != null ? s : 0);
        nameQueri = "YvsComptaReglementCreditFournisseur.findSumByFournisseur";
        s = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setCreance(bean.getCreance() - (s != null ? s : 0));

        nameQueri = "YvsComptaAcompteFournisseur.findAcompteByFournisseur";
        s = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setAcompte(s != null ? s : 0);
        nameQueri = "YvsComptaNotifReglementAchat.findSumByFournisseur";
        s = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setAcompte(bean.getAcompte() - (s != null ? s : 0));

        nameQueri = "YvsComptaCaissePieceAchat.findSumByFournisseur";
        s = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setCredit(s != null ? s : 0);

        bean.setSolde((bean.getCredit() + bean.getAcompte()) - (bean.getDebit() + bean.getCreance()));
        return bean.getSolde();
    }

    public double creanceByAgence(long agence, YvsBaseFournisseur bean, Date dateDebut, Date dateFin) {
        return creance(new YvsAgences(agence), bean, dateDebut, dateFin);
    }

    public double creance(YvsBaseFournisseur bean, Date dateDebut, Date dateFin) {
        return creance(currentAgence, bean, dateDebut, dateFin);
    }

    public double creance(YvsAgences agence, YvsBaseFournisseur bean, Date dateDebut, Date dateFin) {
        return creance(agence, bean, dateDebut, dateFin, true);
    }

    public double creance(YvsBaseFournisseur bean, Date dateDebut, Date dateFin, boolean solde) {
        return creance(currentAgence, bean, dateDebut, dateFin, solde);
    }

    public double creance(YvsAgences agence, YvsBaseFournisseur bean, Date dateDebut, Date dateFin, boolean solde) {
        double soldeInitial = 0;
        if (solde) {
            Calendar dd = Calendar.getInstance();
            dd.set(Calendar.YEAR, 2015);
            Calendar df = Calendar.getInstance();
            df.setTime(dateDebut);
            df.add(Calendar.DATE, -1);
            soldeInitial = creance(agence, bean, dd.getTime(), df.getTime(), false);
        }
        bean.setSoldeInitial(soldeInitial);

        if (agence != null ? agence.getId() > 0 : false) {
            nameQueri = "YvsComCoutSupDocAchat.findSumByFournisseurDatesAgence";
            champ = new String[]{"agence", "fournisseur", "sens", "dateDebut", "dateFin"};
            val = new Object[]{agence, bean, true, dateDebut, dateFin};
        } else {
            nameQueri = "YvsComCoutSupDocAchat.findSumByFournisseurDates";
            champ = new String[]{"fournisseur", "sens", "dateDebut", "dateFin"};
            val = new Object[]{bean, true, dateDebut, dateFin};
        }
        Double cp = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        if (agence != null ? agence.getId() > 0 : false) {
            val = new Object[]{agence, bean, false, dateDebut, dateFin};
        } else {
            val = new Object[]{bean, false, dateDebut, dateFin};
        }
        Double cm = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        double c = (cp != null ? cp : 0) - (cm != null ? cm : 0);

        if (agence != null ? agence.getId() > 0 : false) {
            champ = new String[]{"agence", "fournisseur", "dateDebut", "dateFin"};
            val = new Object[]{agence, bean, dateDebut, dateFin};
            nameQueri = "YvsComContenuDocAchat.findTTCByFournisseurAgenceDates";
        } else {
            champ = new String[]{"fournisseur", "dateDebut", "dateFin"};
            val = new Object[]{bean, dateDebut, dateFin};
            nameQueri = "YvsComContenuDocAchat.findTTCByFournisseurDates";
        }
        Double s = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setDebit((s != null ? s : 0) + c);

        if (agence != null ? agence.getId() > 0 : false) {
            nameQueri = "YvsComptaCreditFournisseur.findCreanceByFournisseurDatesAgence";
        } else {
            nameQueri = "YvsComptaCreditFournisseur.findCreanceByFournisseurDates";
        }
        s = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setCreance(s != null ? s : 0);

        if (agence != null ? agence.getId() > 0 : false) {
            nameQueri = "YvsComptaReglementCreditFournisseur.findSumByFournisseurDatesAgence";
        } else {
            nameQueri = "YvsComptaReglementCreditFournisseur.findSumByFournisseurDates";
        }
        s = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setCreance(bean.getCreance() - (s != null ? s : 0));

        if (agence != null ? agence.getId() > 0 : false) {
            nameQueri = "YvsComptaAcompteFournisseur.findAcompteByFournisseurDatesAgence";
        } else {
            nameQueri = "YvsComptaAcompteFournisseur.findAcompteByFournisseurDates";
        }
        s = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setAcompte(s != null ? s : 0);
        if (agence != null ? agence.getId() > 0 : false) {
            nameQueri = "YvsComptaNotifReglementAchat.findSumByFournisseurDatesAgence";
        } else {
            nameQueri = "YvsComptaNotifReglementAchat.findSumByFournisseurDates";
        }
        s = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setAcompte(bean.getAcompte() - (s != null ? s : 0));

        if (agence != null ? agence.getId() > 0 : false) {
            champ = new String[]{"agence", "fournisseur", "dateDebut", "dateFin", "statut"};
            val = new Object[]{agence, bean, dateDebut, dateFin, Constantes.STATUT_DOC_PAYER};
            nameQueri = "YvsComptaCaissePieceAchat.findSumByFournisseurDatesAgence";
        } else {
            champ = new String[]{"fournisseur", "dateDebut", "dateFin", "statut"};
            val = new Object[]{bean, dateDebut, dateFin, Constantes.STATUT_DOC_PAYER};
            nameQueri = "YvsComptaCaissePieceAchat.findSumByFournisseurDates";
        }
        s = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setCredit(s != null ? s : 0);

        bean.setSolde(bean.getSoldeInitial() + (bean.getCredit() + bean.getAcompte()) - (bean.getDebit() + bean.getCreance()));
        return bean.getSolde();
    }

    public void onSelectDistantTiers(long id) {
        if (id > 0) {
            YvsBaseTiers y = (YvsBaseTiers) dao.loadOneByNameQueries("YvsBaseTiers.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? y.getId() > 0 : false) {
                ManagedTiers s = (ManagedTiers) giveManagedBean(ManagedTiers.class);
                if (s != null) {
                    s.onSelectObject(y);
                    Navigations n = (Navigations) giveManagedBean(Navigations.class);
                    if (n != null) {
                        n.naviguationView("Tiers", "modDonneBase", "smenTiersCom", true);
                    }
                }
            }
        } else {
            getErrorMessage("Aucun code tiers n'a été selectionné avec ce critère !");
        }
    }

}
