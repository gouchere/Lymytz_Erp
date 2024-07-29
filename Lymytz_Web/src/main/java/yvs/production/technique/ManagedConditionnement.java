/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.technique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.ManagedArticles;
import yvs.commercial.UtilCom;
import yvs.commercial.depot.ManagedDepot;
import yvs.commercial.stock.ManagedStockArticle;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.production.base.YvsProdComposantNomenclature;
import yvs.entity.production.base.YvsProdNomenclature;
import yvs.entity.production.pilotage.YvsProdConditionnementDeclaration;
import yvs.entity.production.pilotage.YvsProdContenuConditionnement;
import yvs.entity.production.pilotage.YvsProdDeclarationProduction;
import yvs.entity.production.pilotage.YvsProdFicheConditionnement;
import yvs.entity.produits.YvsBaseArticles;
import yvs.parametrage.entrepot.Depots;
import yvs.production.UtilProd;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedConditionnement extends Managed<FicheConditionnement, YvsProdFicheConditionnement> implements Serializable {

    private FicheConditionnement fiche = new FicheConditionnement();
    private List<YvsProdFicheConditionnement> fiches;
    private YvsProdFicheConditionnement selectFiche;

    private List<YvsProdNomenclature> nomenclatures;
    private ContenuConditionnement contenu = new ContenuConditionnement();
    private YvsProdContenuConditionnement selectContenu;

    private YvsProdDeclarationProduction selectDeclaration;

    private String tabIds;
    private boolean compose = true;

    private String numSearch, statutSearch, egaliteStatut = "!=";
    private boolean date, _first;
    private Boolean actifSearch;
    private Date debutSearch, finSearch;
    private long depotSearch;

    public ManagedConditionnement() {
        fiches = new ArrayList<>();
        nomenclatures = new ArrayList<>();
    }

    public YvsProdDeclarationProduction getSelectDeclaration() {
        return selectDeclaration;
    }

    public void setSelectDeclaration(YvsProdDeclarationProduction selectDeclaration) {
        this.selectDeclaration = selectDeclaration;
    }

    public boolean isCompose() {
        return compose;
    }

    public void setCompose(boolean compose) {
        this.compose = compose;
    }

    public boolean isFirst() {
        return _first;
    }

    public void setFirst(boolean _first) {
        this._first = _first;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getStatutSearch() {
        return statutSearch;
    }

    public void setStatutSearch(String statutSearch) {
        this.statutSearch = statutSearch;
    }

    public String getEgaliteStatut() {
        return egaliteStatut;
    }

    public void setEgaliteStatut(String egaliteStatut) {
        this.egaliteStatut = egaliteStatut;
    }

    public long getDepotSearch() {
        return depotSearch;
    }

    public void setDepotSearch(long depotSearch) {
        this.depotSearch = depotSearch;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public FicheConditionnement getFiche() {
        return fiche;
    }

    public void setFiche(FicheConditionnement fiche) {
        this.fiche = fiche;
    }

    public List<YvsProdFicheConditionnement> getFiches() {
        return fiches;
    }

    public void setFiches(List<YvsProdFicheConditionnement> fiches) {
        this.fiches = fiches;
    }

    public YvsProdFicheConditionnement getSelectFiche() {
        return selectFiche;
    }

    public void setSelectFiche(YvsProdFicheConditionnement selectFiche) {
        this.selectFiche = selectFiche;
    }

    public List<YvsProdNomenclature> getNomenclatures() {
        return nomenclatures;
    }

    public void setNomenclatures(List<YvsProdNomenclature> nomenclatures) {
        this.nomenclatures = nomenclatures;
    }

    public ContenuConditionnement getContenu() {
        return contenu;
    }

    public void setContenu(ContenuConditionnement contenu) {
        this.contenu = contenu;
    }

    public YvsProdContenuConditionnement getSelectContenu() {
        return selectContenu;
    }

    public void setSelectContenu(YvsProdContenuConditionnement selectContenu) {
        this.selectContenu = selectContenu;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public boolean isDate() {
        return date;
    }

    public void setDate(boolean date) {
        this.date = date;
    }

    public Date getDebutSearch() {
        return debutSearch;
    }

    public void setDebutSearch(Date debutSearch) {
        this.debutSearch = debutSearch;
    }

    public Date getFinSearch() {
        return finSearch;
    }

    public void setFinSearch(Date finSearch) {
        this.finSearch = finSearch;
    }

    @Override
    public void loadAll() {
        selectDeclaration = new YvsProdDeclarationProduction();
        if (statutSearch != null ? statutSearch.trim().length() < 1 : true) {
            this.egaliteStatut = "!=";
            this.statutSearch = Constantes.ETAT_VALIDE;
            addParamStatut();
        }
    }

    public void loadAll(boolean init, boolean avance) {
        paginator.addParam(new ParametreRequete("y.nomenclature.article.famille.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        paginator.addParam(new ParametreRequete("y.id", "ids", null, "IN", "AND"));
        if (selectDeclaration != null ? selectDeclaration.getId() > 0 : false) {
            List<Long> ids = dao.loadNameQueries("YvsProdConditionnementDeclaration.findFicheByDeclaration", new String[]{"declaration"}, new Object[]{selectDeclaration});
            if (ids.isEmpty()) {
                ids.add(-1L);
            }
            paginator.addParam(new ParametreRequete("y.id", "ids", ids, "IN", "AND"));
        }
        fiches = paginator.executeDynamicQuery("YvsProdFicheConditionnement", "y.dateConditionnement DESC", init, avance, dao);
    }

    public void loadNomenclature(long article) {
        nomenclatures.clear();
        if (article > 0) {
            nomenclatures = dao.loadNameQueries("YvsProdNomenclature.findByArticleFor", new String[]{"for", "article"}, new Object[]{true, new YvsBaseArticles(article)});
        }
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsProdFicheConditionnement> re = paginator.parcoursDynamicData("YvsProdFicheConditionnement", "y", "y.dateConditionnement DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void paginer(boolean next) {
        loadAll(next, false);
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

    public void chooseDepotConditionnement() {
        if ((fiche.getDepot() != null) ? fiche.getDepot().getId() > 0 : false) {
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null) {
                int idx = m.getDepots_all().indexOf(new YvsBaseDepots(fiche.getDepot().getId()));
                if (idx > -1) {
                    YvsBaseDepots y = m.getDepots_all().get(idx);
                    fiche.setDepot(UtilCom.buildBeanDepot(y));
                } else {
                    fiche.setDepot(new Depots());
                }
            }
        }
    }

    @Override
    public boolean controleFiche(FicheConditionnement bean) {
        if (bean.getNomenclature() != null ? bean.getNomenclature().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner la nomenclature");
            return false;
        }
        if (bean.getQuantite() <= 0) {
            getErrorMessage("Vous devez entrer la quantitée");
            return false;
        }
        if (selectDeclaration != null ? selectDeclaration.getId() > 0 : false) {
            if (bean.getQuantite() > (selectDeclaration.getReste() + (selectFiche != null ? selectFiche.getQuantite() : 0))) {
                getErrorMessage("Vous ne pouvez pas conditionner cette quantitée pour cette déclaration");
                return false;
            }
        }
        for (YvsProdContenuConditionnement o : fiche.getContenus()) {
            if (!controleQte(o)) {
                getErrorMessage("La valeur du sous article " + o.getArticle().getDesignation() + " ne doit pas être arrondi !");
                return false;
            }
        }
        if ((selectFiche != null ? selectFiche.getId() > 0 ? !selectFiche.getDateConditionnement().equals(bean.getDateConditionnement()) : false : false)
                || (bean.getNumero() == null || bean.getNumero().trim().length() < 1)) {
            String ref = genererReference(Constantes.TYPE_RC_NAME, bean.getDateConditionnement(), bean.getDepot().getId(), Constantes.DEPOT);
            if ((ref != null) ? ref.trim().equals("") : true) {
                return false;
            }
            bean.setNumero(ref);
        }
        return true;
    }

    public boolean controleFiche(ContenuConditionnement bean) {
        if (bean.getFiche() != null ? bean.getFiche().getId() < 1 : true) {
            getErrorMessage("Vous devez enregistrer la fiche l'article");
            return false;
        }
        if (bean.getArticle() != null ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner l'article");
            return false;
        }
        if (bean.getCondition() != null ? bean.getCondition().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner le conditionnement");
            return false;
        }
        if (bean.getQuantite() <= 0) {
            getErrorMessage("Vous devez entrer la quantitée");
            return false;
        }
        return true;
    }

    @Override
    public void resetFiche() {
        fiche = new FicheConditionnement();
        selectFiche = new YvsProdFicheConditionnement();
        selectDeclaration = new YvsProdDeclarationProduction();
        nomenclatures.clear();
        resetFicheContenu();
        update("blog_conditionnement");
    }

    public void resetFicheContenu() {
        contenu = new ContenuConditionnement();
        selectContenu = new YvsProdContenuConditionnement();
    }

    @Override
    public boolean saveNew() {
        String action = fiche.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(fiche)) {
                selectFiche = UtilProd.buildFicheConditionnement(fiche, currentUser);
                if (fiche.getId() < 1) {
                    selectFiche.setId(null);
                    selectFiche = (YvsProdFicheConditionnement) dao.save1(selectFiche);
                    fiche.setId(selectFiche.getId());
                    saveConditionnementDeclaration(selectDeclaration, selectFiche);
                } else {
                    dao.update(selectFiche);
                }
                int idx = fiches.indexOf(selectFiche);
                if (idx > -1) {
                    fiches.set(idx, selectFiche);
                } else {
                    fiches.add(selectFiche);
                }
                recopieOrSaveComposant(fiche.getNomenclature(), true);
                succes();
                actionOpenOrResetAfter(this);
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("ERROR (saveNew)", ex);
        }
        return false;
    }

    public void saveConditionnementDeclaration(YvsProdDeclarationProduction y, YvsProdFicheConditionnement f) {
        if ((y != null ? y.getId() > 0 : false) && (f != null ? f.getId() > 0 : false)) {
            YvsProdConditionnementDeclaration e = new YvsProdConditionnementDeclaration(f, y);
            e.setAuthor(currentUser);
            e.setDateSave(new Date());
            e.setDateUpdate(new Date());
            e.setId(null);
            e = (YvsProdConditionnementDeclaration) dao.save1(e);
            y.getConditionnements().add(e);
        }
    }

    private boolean controleQte(YvsProdContenuConditionnement cc) {
        if (cc.getModeArrondi().equals("N")) {
            double doubleVal = cc.getQuantite();
            int valInt = cc.getQuantite().intValue();
            return ((double) (doubleVal - valInt)) == 0;
        }
        return true;
    }

    public void recopieOrSaveComposant(Nomenclature em, boolean save) {
        if (save) {
            YvsProdContenuConditionnement y;
            int idx = fiches.indexOf(selectFiche);
            for (YvsProdContenuConditionnement o : fiche.getContenus()) {
                o.setFiche(selectFiche);
                y = (YvsProdContenuConditionnement) dao.loadOneByNameQueries("YvsProdContenuConditionnement.findOne", new String[]{"article", "unite", "fiche"}, new Object[]{o.getArticle(), o.getConditionnement(), o.getFiche()});
                if (y != null ? y.getId() < 1 : true) {
                    o.setId(null);
                    o = (YvsProdContenuConditionnement) dao.save1(o);
                } else {
                    o.setId(y.getId());
                    dao.update(o);
                }
            }
            if (idx >= 0) {
                selectFiche.setContenus(new ArrayList<>(fiche.getContenus()));
                fiches.set(idx, selectFiche);
            }
        } else {
            fiche.getContenus().clear();
            if (fiche.getQuantite() != 0) {
                YvsProdContenuConditionnement c;
                List<YvsProdComposantNomenclature> list = dao.loadNameQueries("YvsProdComposantNomenclature.findByNomenclature", new String[]{"nomenclature"}, new Object[]{new YvsProdNomenclature(em.getId())});
                for (YvsProdComposantNomenclature o : list) {
                    c = new YvsProdContenuConditionnement((long) -(fiche.getContenus().size() + 1));
                    c.setArticle(o.getArticle());
                    c.setAuthor(currentUser);
                    c.setConditionnement(o.getUnite());
                    c.setDateSave(new Date());
                    c.setDateUpdate(new Date());
                    c.setConsommable(o.getType().equals(Constantes.PROD_OP_TYPE_COMPOSANT_NORMAL));
                    if (em.getQuantite() != 0) {
                        c.setQuantite(fiche.getQuantite() * o.getQuantite() / em.getQuantite());
                    }
                    c.setModeArrondi(o.getModeArrondi());
                    c.setNew_(true);
                    fiche.getContenus().add(c);
                }
            }
        }
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsProdFicheConditionnement> list = new ArrayList<>();
                YvsProdFicheConditionnement bean;
                for (Long ids : l) {
                    bean = fiches.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    if (bean.canDelete()) {
                        dao.delete(bean);
                        if (fiche.getId() == bean.getId()) {
                            resetFiche();
                        }
                    }
                }
                fiches.removeAll(list);
                succes();
                tabIds = "";
            }
        } catch (Exception ex) {
            getException("Suppression Impossible !", ex);
        }
    }

    public void openTodeleteBean(YvsProdFicheConditionnement y) {
         if (y != null ? y.getId() > 0 : false) {
            int index = fiches.indexOf(y);
            if (index > 1) {
                tabIds = "" + index;
            }

        }
    }

    public void deleteBean(YvsProdFicheConditionnement y) {
        try {
            if (y != null) {
                if (!y.canDelete()) {
                    getErrorMessage("Vous ne pouvez pas supprimer cette transformation");
                    return;
                }
                dao.delete(y);
                if (fiches.contains(y)) {
                    fiches.remove(y);
                }
                if (fiche.getId() == y.getId()) {
                    resetFiche();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !", "Il est possible que l'élément soit en relation avec d'autres ressources");
            getException("Lymytz Erp Error...", ex);
        }
    }

    public void deleteBeanContenu(YvsProdContenuConditionnement y) {
        try {
            if (y != null) {
                if (!selectFiche.canDelete()) {
                    getErrorMessage("Vous ne pouvez pas supprimer cette ligne de reconditionnement");
                    return;
                }
                dao.delete(y);
                fiche.getContenus().remove(y);
                selectFiche.getContenus().remove(y);
                int idx = fiches.indexOf(selectFiche);
                if (idx > -1) {
                    fiches.set(idx, selectFiche);
                } else {
                    fiches.add(0, selectFiche);
                }
                if (contenu.getId() == y.getId()) {
                    resetFicheContenu();
                }
                succes();
            }
        } catch (NumberFormatException ex) {
            getException("Suppression Impossible !", ex);
        }
    }

    @Override
    public void onSelectObject(YvsProdFicheConditionnement y) {
        selectFiche = y;
        fiche = UtilProd.buildBeanFicheConditionnement(y);
        for (YvsProdContenuConditionnement c : fiche.getContenus()) {
            c.setModeArrondi((String) dao.loadObjectByNameQueries("YvsProdComposantNomenclature.findModeArrondi", new String[]{"article", "nomenclature"}, new Object[]{c.getArticle(), y.getNomenclature()}));
        }
        loadNomenclature(y.getNomenclature().getArticle().getId());
        resetFicheContenu();
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsProdFicheConditionnement) ev.getObject());
            tabIds = fiches.indexOf((YvsProdFicheConditionnement) ev.getObject()) + "";
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            chooseArticle(UtilProd.buildBeanArticles(bean), true);
        }
    }

    public void blurQuantiteFiche() {
        recopieOrSaveComposant(fiche.getNomenclature(), false);
    }

    public void chooseDepot() {
        ManagedDepot s = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (s != null) {
            ManagedStockArticle a = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if ((fiche.getDepot() != null) ? fiche.getDepot().getId() > 0 : false) {
                YvsBaseDepots y = s.getDepots().get(s.getDepots().indexOf(new YvsBaseDepots(fiche.getDepot().getId())));
                Depots d = UtilCom.buildBeanDepot(y);
                cloneObject(fiche.getDepot(), d);
            }
        }
    }

    public void chooseNomenclature() {
        if (fiche.getNomenclature() != null ? fiche.getNomenclature().getId() > 0 : false) {
            int idx = nomenclatures.indexOf(new YvsProdNomenclature(fiche.getNomenclature().getId()));
            if (idx > -1) {
                fiche.setNomenclature(UtilProd.buildBeanNomenclature(nomenclatures.get(idx)));
                fiche.setConditionnement(fiche.getNomenclature().getUnite());
                chooseArticle(fiche.getArticle(), false);
                recopieOrSaveComposant(fiche.getNomenclature(), false);
            }
        }
    }

    public void chooseArticle(Articles art, boolean load) {
        if ((art != null) ? art.getId() > 0 : false) {
            art.setStock(dao.stocks(art.getId(), 0, fiche.getDepot().getId(), 0, 0, fiche.getDateConditionnement(), fiche.getConditionnement().getId(), 0));
            if (compose) {
                if (load) {
                    loadNomenclature(art.getId());
                    cloneObject(fiche.getArticle(), art);
                }
                fiche.getArticle().setStock(art.getStock());
                update("form_conditionnement");
            } else {
                cloneObject(contenu.getArticle(), art);
            }
        }
    }

    public void searchArticle(boolean compose) {
        this.compose = compose;
        if (compose) {
            String num = fiche.getArticle().getRefArt();
            fiche.getArticle().setDesignation("");
            fiche.getArticle().setError(true);
            fiche.getArticle().setId(0);
            if (num != null ? num.trim().length() > 0 : false) {
                ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
                if (m != null) {
                    Articles y = m.searchArticleActif(null, num, true);
                    if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                        if (m.getArticlesResult().size() > 1) {
                            update("data_article_fiche_conditionnement");
                        } else {
                            chooseArticle(y, true);
                        }
                        fiche.getArticle().setError(false);
                    }
                }
            }
        } else {
            String num = contenu.getArticle().getRefArt();
            contenu.getArticle().setDesignation("");
            contenu.getArticle().setError(true);
            contenu.getArticle().setId(0);
            if (num != null ? num.trim().length() > 0 : false) {
                ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
                if (m != null) {
                    Articles y = m.searchArticleActif(null, num, true);
                    if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                        if (m.getArticlesResult().size() > 1) {
                            update("data_article_fiche_conditionnement");
                        } else {
                            chooseArticle(y, false);
                        }
                        contenu.getArticle().setError(false);
                    }
                }
            }
        }
    }

    public void initArticles(boolean compose) {
        this.compose = compose;
        ManagedArticles a = (ManagedArticles) giveManagedBean("Marticle");
        if (a != null) {
            a.initArticles(null, (compose ? fiche.getArticle() : contenu.getArticle()));
        }
        update("data_article_fiche_conditionnement");
    }

    public boolean canAutorisation(int action) {
        //action : valide 0 -- Annule 1 -- Editer 2
        switch (action) {
            case 0:
                if (currentUser != null ? (currentUser.getUsers() != null) : false) {
                    if (selectFiche != null ? selectFiche.getDepot() != null : false) {
                        if (currentUser.getUsers().getEmploye() != null && selectFiche.getDepot().getResponsable() != null) {
                            // Controle si le employé courant est le responsable du depot destinataire
                            if (!selectFiche.getDepot().getResponsable().equals(currentUser.getUsers().getEmploye())) {
                                getErrorMessage("Vous ne pouvez pas modifier cette transformation...car vous n'êtes pas habilité à le faire");
                                return false;
                            }
                        } else {
                            getErrorMessage("Vous ne pouvez pas modifier cette transformation...car vous n'êtes pas habilité à le faire");
                            return false;
                        }
                    }
                } else {
                    getErrorMessage("Vous ne pouvez pas modifier cette transformation...car vous n'êtes pas habilité à le faire");
                    return false;
                }
                break;
            case 1:

            case 2:

                break;
        }
        return true;
    }

    public void terminer() {
        if (selectFiche == null) {
            return;
        }
        if (selectFiche.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
            if (changeStatut(Constantes.ETAT_TERMINE)) {
                succes();
            }
        } else {
            getErrorMessage("Cette transformation doit etre validée");
        }
    }

    public void annuler(boolean suspend, boolean force) {
        if (selectFiche == null) {
            return;
        }
        if (selectFiche != null ? selectFiche.getId() > 0 : false) {
            if (!canAutorisation(suspend ? 1 : 2)) {
                return;
            }
            if (suspend) {
                for (YvsProdContenuConditionnement c : fiche.getContenus()) {
                    if (!c.getConsommable()) {
                        double stock = dao.stocks(c.getArticle().getId(), 0, fiche.getDepot().getId(), 0, 0, fiche.getDateConditionnement(), (c.getConditionnement() != null) ? c.getConditionnement().getId() : -1, 0);
                        if (stock < c.getQuantite()) {
                            getErrorMessageDefaultStock(c.getArticle().getDesignation(), fiche.getDepot().getDesignation(), fiche.getDateConditionnement());
                            return;
                        }
                        stock = dao.stocks(c.getArticle().getId(), 0, fiche.getDepot().getId(), 0, 0, new Date(), (c.getConditionnement() != null) ? c.getConditionnement().getId() : -1, 0);
                        if (stock < contenu.getQuantite()) {
                            getErrorMessageDefaultStock(c.getArticle().getDesignation(), fiche.getDepot().getDesignation(), new Date());
                            return;
                        }
                    }
                }
            }
            if (!force && selectFiche.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                openDialog(suspend ? "dlgConfirmAnnuler" : "dlgConfirmEditer");
            } else {
                if (changeStatut(suspend ? Constantes.ETAT_ANNULE : Constantes.ETAT_EDITABLE)) {
                    succes();
                }
            }
        }
    }

    public void valider() {
        if (selectFiche == null) {
            return;
        }
        if (!canAutorisation(3)) {
            return;
        }
        //trouve la déclaration lié à cette fiche (On a en particulier une déclaration par fiche de cond)
        YvsProdDeclarationProduction dec = (YvsProdDeclarationProduction) dao.loadOneByNameQueries("YvsProdConditionnementDeclaration.findDeclarationByFiche", new String[]{"fiche"}, new Object[]{selectFiche});
        YvsBaseDepots depot = (dec != null) ? dec.getSessionOf().getSessionProd().getDepot() : selectFiche.getDepot();
        double stock = dao.stocks(fiche.getArticle().getId(), 0, depot.getId(), 0, 0, fiche.getDateConditionnement(), (fiche.getConditionnement() != null) ? fiche.getConditionnement().getId() : -1, 0);
//        if (stock < fiche.getQuantite()) {
//            getErrorMessageDefaultStock(fiche.getArticle().getDesignation(), depot.getDesignation(), fiche.getDateConditionnement());
//            return;
//        }
        for (YvsProdContenuConditionnement c : fiche.getContenus()) {
            if (c.getConsommable()) {
                stock = dao.stocks(c.getArticle().getId(), 0, depot.getId(), 0, 0, fiche.getDateConditionnement(), (c.getConditionnement() != null) ? c.getConditionnement().getId() : -1, 0);
                if (stock < c.getQuantite()) {
                    getErrorMessageDefaultStock(c.getArticle().getDesignation(), depot.getDesignation(), fiche.getDateConditionnement());
                    return;
                }
                stock = dao.stocks(c.getArticle().getId(), 0, depot.getId(), 0, 0, new Date(), (c.getConditionnement() != null) ? c.getConditionnement().getId() : -1, 0);
                if (stock < contenu.getQuantite()) {
                    getErrorMessageDefaultStock(c.getArticle().getDesignation(), depot.getDesignation(), new Date());
                    return;
                }
            }
        }
        if (changeStatut(Constantes.ETAT_VALIDE)) {
            succes();
        }
    }

    public boolean changeStatut(String etat) {
        if (!etat.equals("") && selectFiche != null) {
            selectFiche.setStatut(etat.charAt(0));
            selectFiche.setAuthor(currentUser);
            dao.update(selectFiche);
            if (fiches.contains(selectFiche)) {
                fiches.set(fiches.indexOf(selectFiche), selectFiche);
            }
            fiche.setStatut(etat.charAt(0));
            update("data_fiche_conditionnement");
            update("form_conditionnement");
            update("grp_btn_etat_conditionnement");
            return true;
        }
        return false;
    }

    public void clearParams(boolean load) {
        actifSearch = null;
        depotSearch = 0;
        numSearch = null;
        statutSearch = null;
        date = false;
        debutSearch = new Date();
        finSearch = new Date();
        selectDeclaration = new YvsProdDeclarationProduction();
        paginator.getParams().clear();
        _first = true;
        if (load) {
            loadAll(true, true);
        }
    }

    public void addParamActif() {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
        loadAll(true, true);
    }

    public void addParamSource() {
        ParametreRequete p = new ParametreRequete("y.depot", "depot", null, "=", "AND");
        if (depotSearch > 0) {
            p = new ParametreRequete("y.depot", "depot", new YvsBaseDepots(depotSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.numDoc", "reference", null, "LIKE", "AND");
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", numSearch.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nomenclature.reference)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nomenclature.article.refArt)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nomenclature.article.designation)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateConditionnement", "dates", null, "=", "AND");
        if (date) {
            p = new ParametreRequete("y.dateConditionnement", "dates", debutSearch, finSearch, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void chooseStatut(ValueChangeEvent ev) {
        statutSearch = ((String) ev.getNewValue());
        addParamStatut();
    }

    public void addParamStatut() {
        ParametreRequete p = new ParametreRequete("y.statut", "statut", null);
        if (statutSearch != null ? statutSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statut", "statut", statutSearch.charAt(0), egaliteStatut, "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

}
