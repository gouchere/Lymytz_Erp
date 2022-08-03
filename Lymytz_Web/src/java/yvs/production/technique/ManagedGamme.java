/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.technique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.ManagedArticles;
import yvs.base.produits.ManagedUniteMesure;
import yvs.base.produits.UniteMesure;
import yvs.production.UtilProd;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseCodeAcces;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.production.base.YvsProdComposantNomenclature;
import yvs.entity.production.base.YvsProdDocumentTechnique;
import yvs.entity.production.base.YvsProdGammeArticle;
import yvs.entity.production.base.YvsProdGammeSite;
import yvs.entity.production.base.YvsProdIndicateurOp;
import yvs.entity.production.base.YvsProdNomenclatureSite;
import yvs.entity.production.base.YvsProdOperationsGamme;
import yvs.entity.production.base.YvsProdPosteCharge;
import yvs.entity.production.base.YvsProdPosteOperation;
import yvs.entity.production.base.YvsProdSiteProduction;
import yvs.entity.production.base.YvsProdValeursQualitative;
import yvs.entity.production.pilotage.YvsProdComposantOp;
import yvs.entity.production.pilotage.YvsProdContentModeleQuantitatif;
import yvs.entity.production.pilotage.YvsProdModeleQuantitatif;
import yvs.entity.produits.YvsBaseArticles;
import yvs.production.DocumentTechnique;
import yvs.production.base.ValeurIndicateur;
import yvs.production.base.IndicateurReussite;
import yvs.production.base.OperationsGamme;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedGamme extends Managed<GammeArticle, YvsProdGammeArticle> implements Serializable {

    @ManagedProperty(value = "#{gammeArticle}")
    private GammeArticle gamme;
    private OperationsGamme operation = new OperationsGamme();
    private PosteOperation poste = new PosteOperation();
    private DocumentTechnique document = new DocumentTechnique();
    private IndicateurReussite indicateur = new IndicateurReussite();
    private ValeurIndicateur valeur = new ValeurIndicateur();
    private YvsProdComposantOp selectedComposantOp = new YvsProdComposantOp(-1);
    private YvsProdIndicateurOp selectedIndicateur;
    private YvsBaseCodeAcces codeAcces;

    private String txt_reslut, tabIds, tabIds_phase, tabIds_poste, tabIds_document, tabIds_indicateur,
            input_reset, input_reset_phase, input_reset_poste, input_reset_document, input_reset_indicateur;
    private boolean updateDocument, updateIndicateur,
            allPhase, allPoste, allDocument, allIndicateur;

    YvsProdGammeArticle entityGamme;
    YvsProdOperationsGamme selectedOperation;

    private List<OperationsGamme> phases;
    private List<YvsProdGammeArticle> gammes, listGamme;

    public List<YvsBaseArticles> articles;
    private List<YvsProdPosteCharge> postes;
    private List<YvsProdValeursQualitative> valeursQualitative;
    private List<YvsProdModeleQuantitatif> modelsQuantitatifs;
    private Long idModel;

    int droit = 0;
    private boolean dateSeacrh;
    private Boolean actifSearch, forCondSearch;
    private String numSearch, artSearch, categorieSearch;
    private int siteSearch;
    private Date dateDebutSearch, dateFinSearch;

    private String refModel;

    private String fusionneTo;
    private List<String> fusionnesBy;
    private String fusionneToOperation;
    private List<String> fusionnesByOperation;

//    private boolean formPhase;
    public ManagedGamme() {
//        unites = new ArrayList<>();
        valeursQualitative = new ArrayList<>();
        postes = new ArrayList<>();
        gammes = new ArrayList<>();
        listGamme = new ArrayList<>();
        phases = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
        modelsQuantitatifs = new ArrayList<>();
        fusionnesByOperation = new ArrayList<>();
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

    public String getFusionneToOperation() {
        return fusionneToOperation;
    }

    public void setFusionneToOperation(String fusionneToOperation) {
        this.fusionneToOperation = fusionneToOperation;
    }

    public List<String> getFusionnesByOperation() {
        return fusionnesByOperation;
    }

    public void setFusionnesByOperation(List<String> fusionnesByOperation) {
        this.fusionnesByOperation = fusionnesByOperation;
    }

    public List<YvsProdModeleQuantitatif> getModelsQuantitatifs() {
        return modelsQuantitatifs;
    }

    public void setModelsQuantitatifs(List<YvsProdModeleQuantitatif> modelsQuantitatifs) {
        this.modelsQuantitatifs = modelsQuantitatifs;
    }

    public String getCategorieSearch() {
        return categorieSearch;
    }

    public void setCategorieSearch(String categorieSearch) {
        this.categorieSearch = categorieSearch;
    }

    public YvsBaseCodeAcces getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(YvsBaseCodeAcces codeAcces) {
        this.codeAcces = codeAcces;
    }

    public YvsProdGammeArticle getEntityGamme() {
        return entityGamme;
    }

    public void setEntityGamme(YvsProdGammeArticle entityGamme) {
        this.entityGamme = entityGamme;
    }

    public boolean isDateSeacrh() {
        return dateSeacrh;
    }

    public void setDateSeacrh(boolean dateSeacrh) {
        this.dateSeacrh = dateSeacrh;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public Boolean isForCondSearch() {
        return forCondSearch;
    }

    public void setForCondSearch(Boolean forCondSearch) {
        this.forCondSearch = forCondSearch;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public String getArtSearch() {
        return artSearch;
    }

    public void setArtSearch(String artSearch) {
        this.artSearch = artSearch;
    }

    public int getSiteSearch() {
        return siteSearch;
    }

    public void setSiteSearch(int siteSearch) {
        this.siteSearch = siteSearch;
    }

    public Date getDateDebutSearch() {
        return dateDebutSearch;
    }

    public void setDateDebutSearch(Date dateDebutSearch) {
        this.dateDebutSearch = dateDebutSearch;
    }

    public Date getDateFinSearch() {
        return dateFinSearch;
    }

    public void setDateFinSearch(Date dateFinSearch) {
        this.dateFinSearch = dateFinSearch;
    }

    public List<PosteOperation> getTabPostePhase() {
        return tabPostePhase;
    }

    public void setTabPostePhase(List<PosteOperation> tabPostePhase) {
        this.tabPostePhase = tabPostePhase;
    }

    public List<YvsProdDocumentTechnique> getTabDocumentTechnique() {
        return tabDocumentTechnique;
    }

    public void setTabDocumentTechnique(List<YvsProdDocumentTechnique> tabDocumentTechnique) {
        this.tabDocumentTechnique = tabDocumentTechnique;
    }

    public OperationsGamme getOperation() {
        return operation;
    }

    public void setOperation(OperationsGamme operation) {
        this.operation = operation;
    }

    public List<OperationsGamme> getPhases() {
        return phases;
    }

    public void setPhases(List<OperationsGamme> phases) {
        this.phases = phases;
    }

//    public UniteMesure getUnite() {
//        return unite;
//    }
//
//    public void setUnite(UniteMesure unite) {
//        this.unite = unite;
//    }
    public String getTxt_reslut() {
        return txt_reslut;
    }

    public void setTxt_reslut(String txt_reslut) {
        this.txt_reslut = txt_reslut;
    }

    public IndicateurReussite getIndicateur() {
        return indicateur;
    }

    public void setIndicateur(IndicateurReussite indicateur) {
        this.indicateur = indicateur;
    }

    public List<YvsProdValeursQualitative> getValeursQualitative() {
        return valeursQualitative;
    }

    public void setValeursQualitative(List<YvsProdValeursQualitative> valeursQualitative) {
        this.valeursQualitative = valeursQualitative;
    }

    public ValeurIndicateur getValeur() {
        return valeur;
    }

    public void setValeur(ValeurIndicateur valeur) {
        this.valeur = valeur;
    }

    public String getTabIds_indicateur() {
        return tabIds_indicateur;
    }

    public void setTabIds_indicateur(String tabIds_indicateur) {
        this.tabIds_indicateur = tabIds_indicateur;
    }

    public String getInput_reset_indicateur() {
        return input_reset_indicateur;
    }

    public void setInput_reset_indicateur(String input_reset_indicateur) {
        this.input_reset_indicateur = input_reset_indicateur;
    }

    public boolean isUpdateIndicateur() {
        return updateIndicateur;
    }

    public void setUpdateIndicateur(boolean updateIndicateur) {
        this.updateIndicateur = updateIndicateur;
    }

    public boolean isAllIndicateur() {
        return allIndicateur;
    }

    public void setAllIndicateur(boolean allIndicateur) {
        this.allIndicateur = allIndicateur;
    }

    public DocumentTechnique getDocument() {
        return document;
    }

    public void setDocument(DocumentTechnique document) {
        this.document = document;
    }

    public String getTabIds_document() {
        return tabIds_document;
    }

    public void setTabIds_document(String tabIds_document) {
        this.tabIds_document = tabIds_document;
    }

    public String getInput_reset_document() {
        return input_reset_document;
    }

    public void setInput_reset_document(String input_reset_document) {
        this.input_reset_document = input_reset_document;
    }

    public boolean isUpdateDocument() {
        return updateDocument;
    }

    public void setUpdateDocument(boolean updateDocument) {
        this.updateDocument = updateDocument;
    }

    public boolean isAllDocument() {
        return allDocument;
    }

    public void setAllDocument(boolean allDocument) {
        this.allDocument = allDocument;
    }

    public boolean isAllPoste() {
        return allPoste;
    }

    public void setAllPoste(boolean allPoste) {
        this.allPoste = allPoste;
    }

    public String getTabIds_poste() {
        return tabIds_poste;
    }

    public void setTabIds_poste(String tabIds_poste) {
        this.tabIds_poste = tabIds_poste;
    }

    public String getInput_reset_poste() {
        return input_reset_poste;
    }

    public void setInput_reset_poste(String input_reset_poste) {
        this.input_reset_poste = input_reset_poste;
    }

    public List<YvsProdPosteCharge> getPostes() {
        return postes;
    }

    public void setPostes(List<YvsProdPosteCharge> postes) {
        this.postes = postes;
    }

    public PosteOperation getPoste() {
        return poste;
    }

    public void setPoste(PosteOperation poste) {
        this.poste = poste;
    }

    public boolean isAllPhase() {
        return allPhase;
    }

    public void setAllPhase(boolean allPhase) {
        this.allPhase = allPhase;
    }

    public List<YvsProdGammeArticle> getListGamme() {
        return listGamme;
    }

    public void setListGamme(List<YvsProdGammeArticle> listGamme) {
        this.listGamme = listGamme;
    }

    public String getInput_reset_phase() {
        return input_reset_phase;
    }

    public void setInput_reset_phase(String input_reset_phase) {
        this.input_reset_phase = input_reset_phase;
    }

    public String getTabIds_phase() {
        return tabIds_phase;
    }

    public void setTabIds_phase(String tabIds_phase) {
        this.tabIds_phase = tabIds_phase;
    }

    public List<YvsProdGammeArticle> getGammes() {
        return gammes;
    }

    public void setGammes(List<YvsProdGammeArticle> gammes) {
        this.gammes = gammes;
    }

    public String getInput_reset() {
        return input_reset;
    }

    public void setInput_reset(String input_reset) {
        this.input_reset = input_reset;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public GammeArticle getGamme() {
        return gamme;
    }

    public void setGamme(GammeArticle gamme) {
        this.gamme = gamme;
    }

    public YvsProdOperationsGamme getSelectedOperation() {
        return selectedOperation;
    }

    public void setSelectedOperation(YvsProdOperationsGamme selectedOperation) {
        this.selectedOperation = selectedOperation;
    }

    public YvsProdComposantOp getSelectedComposantOp() {
        return selectedComposantOp;
    }

    public void setSelectedComposantOp(YvsProdComposantOp selectedComposantOp) {
        this.selectedComposantOp = selectedComposantOp;
    }

    public YvsProdIndicateurOp getSelectedIndicateur() {
        return selectedIndicateur;
    }

    public void setSelectedIndicateur(YvsProdIndicateurOp selectedIndicateur) {
        this.selectedIndicateur = selectedIndicateur;
    }

    public String getRefModel() {
        return refModel;
    }

    public void setRefModel(String refModel) {
        this.refModel = refModel;
    }

    public Long getIdModel() {
        return idModel;
    }

    public void setIdModel(Long idModel) {
        this.idModel = idModel;
    }

    public void choixOptionPermanant(ValueChangeEvent ev) {
        Boolean b = (Boolean) ev.getNewValue();
        if (b) {
            gamme.setDebutValidite(null);
            gamme.setFinValidite(null);
        }
    }

    public void chooseUniteTemps() {
        if (gamme.getUniteTemps() != null ? gamme.getUniteTemps().getId() > 0 : false) {
            ManagedUniteMesure w = (ManagedUniteMesure) giveManagedBean(ManagedUniteMesure.class);
            if (w != null) {
                int idx = w.getUnites().indexOf(new YvsBaseUniteMesure(gamme.getUniteTemps().getId()));
                if (idx > -1) {
                    UniteMesure m = UtilProd.buildBeanUniteMesure(w.getUnites().get(idx));
                    cloneObject(gamme.getUniteTemps(), m);
                }
            }
        }
    }

    @Override
    public void loadAll() {
        droit = buildDroit();
        loadAll(true, true);
        loadAllPosteCharge();
        loadAllIndicateur();

    }

    public void loadAllGammeArticle() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        gammes = dao.loadNameQueries("YvsProdGammeArticle.findAll", champ, val);
    }

    private int buildDroit() {
        return autoriser("prod_gamme_load_all") ? 1 : 2;
    }

    public void loadAll(boolean avance, boolean init) {
        switch (droit) {
            case 1:
                paginator.addParam(new ParametreRequete("y.article.famille.societe", "societe", currentAgence.getSociete(), "=", "AND"));
                gammes = paginator.executeDynamicQuery("YvsProdGammeArticle", "y.codeRef, y.article.refArt ASC", avance, init, (int) imax, dao);
                break;
            case 2:
                if (currentCreneauEquipe != null) {
                    paginator.addParam(new ParametreRequete("y.site", "site", currentCreneauEquipe.getSite(), "=", "AND"));
                    gammes = paginator.executeDynamicQuery("DISTINCT y.gamme", "DISTINCT y.gamme", "YvsProdGammeSite y", "y.gamme.codeRef, y.gamme.article.refArt ASC", avance, init, (int) imax, dao);
                }
                break;
        }
    }

    public void paginer(boolean next) {
        //To change body of generated methods, choose Tools | Templates.
        loadAll(next, false);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAll(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAll(true, true);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsProdGammeArticle> re = paginator.parcoursDynamicData("YvsProdGammeArticle", "y", "y.codeRef, y.article.refArt ASC", getOffset(), dao);
        if (!re.isEmpty()) {
            entityGamme = re.get(0);
            onSelectObject(entityGamme);
        }
    }

    public void loadAllGammeRefere() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listGamme = dao.loadNameQueries("YvsProdGammeArticle.findAll", champ, val);
    }

    public void loadAllPosteCharge() {
        postes = dao.loadNameQueries("YvsProdPosteCharge.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllIndicateur() {
//        indicateurs.clear();
//        List<YvsBaseIndicateurReussite> l = dao.loadNameQueries("YvsBaseIndicateurReussite.findAll", new String[]{}, new Object[]{});
//        indicateurs = UtilProd.buildBeanListIndicateurReussite(l);
    }

    private void chooseArticle(Articles y) {
        gamme.setArticle(y);
        gamme.setReference(getReference(y));
        update("txt_article_gammes");
        update("txt_refence_gammes");
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            chooseArticle(UtilProd.buildSimpleBeanArticles(bean));
        }
    }

    public void searchArticle(String refArticle) {
        gamme.getArticle().setDesignation("");
        gamme.getArticle().setError(true);
        gamme.getArticle().setId(0);
        if (refArticle != null ? refArticle.trim().length() > 0 : false) {
            ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
            if (m != null) {
                m.setSearchArticle(refArticle);
                m.findArticle_();
                if (m.getListArticle() != null ? !m.getListArticle().isEmpty() : false) {
                    if (m.getListArticle().size() > 1) {
                        update("data_article_nomenclature");
                    } else if (m.getListArticle().size() == 1) {
                        chooseArticle(UtilProd.buildBeanArticles(m.getListArticle().get(0)));
                    }
                    gamme.getArticle().setError(false);
                }
            }
        }
    }

    public void initArticles(boolean compose) {
        ManagedArticles a = (ManagedArticles) giveManagedBean("Marticle");
        if (a != null) {
            a.initArticles("P", gamme.getArticle());
        }
        update("data_article_gammes");
    }

//    public YvsBaseUniteMesure buildUniteMasse(UniteMesure u) {
//        YvsBaseUniteMesure r = new YvsBaseUniteMesure();
//        if (u != null) {
//            r.setId(u.getId());
//            r.setReference(u.getReference());
//            r.setDescription(u.getDescription());
//            r.setType("T");
//            r.setLibelle(u.getLibelle());
//            r.setSociete(currentAgence.getSociete());
//        }
//        return r;
//    }
//    public UniteMesure recopiewUnite() {
//        UniteMesure r = new UniteMesure();
//        r.setId(unite.getId());
//        r.setReference(unite.getReference());
//        r.setDescription(unite.getDescription());
//        r.setType(unite.getType());
//        r.setLibelle(unite.getLibelle());
//        return r;
//    }
//    public boolean controleFicheUnite(UniteMesure bean) {
//        if (bean.getLibelle() == null || bean.getLibelle().equals("")) {
//            getErrorMessage("Entrer le libelle!");
//            return false;
//        }
//        return true;
//    }
//    public void saveNewUnite() {
//        UniteMesure bean = recopiewUnite();
//        if (controleFicheUnite(bean)) {
//            YvsBaseUniteMesure entity = buildUniteMasse(bean);
//            entity = (YvsBaseUniteMesure) dao.save1(entity);
//            bean.setId(entity.getId());
////            unites.add(bean);
//            resetFiche(unite);
//        }
//        update("unite_duree");
//    }
    @Override
    public GammeArticle recopieView() {
        GammeArticle g = new GammeArticle();
        g.setId(gamme.getId());
        g.setArticle(gamme.getArticle());
        g.setDescription(gamme.getDescription());
        g.setDesignation(gamme.getDesignation());
        g.setReference(gamme.getReference());
        g.setActif(gamme.isActif());
        g.setPrincipal(gamme.isPrincipal());
        g.setUniteTemps(gamme.getUniteTemps());
        g.setDebutValidite(gamme.getDebutValidite());
        g.setFinValidite(gamme.getFinValidite());
        g.setPermanant(gamme.isPermanant());
        return g;
    }

    @Override
    public boolean controleFiche(GammeArticle bean) {
        return true;
    }

    public boolean controleFiche(GammeArticle bean, boolean control) {
        if (control) {
            if (bean.getReference().equals("") || bean.getReference() == null) {
                getErrorMessage("Vous devez entrer la reference");
                return false;
            }
            if (bean.getArticle().getId() <= 0) {
                getErrorMessage("Vous devez entrer la reference de l'article");
                return false;
            }
            if (bean.isPrincipal()) {
                //vérifie si l'article a déjà une gamme principal
                champ = new String[]{"article"};
                val = new Object[]{new YvsBaseArticles(gamme.getArticle().getId())};
                Long re = (Long) dao.loadObjectByNameQueries("YvsProdGammeArticle.CountByPrincipalArticle", champ, val);
                if (re != null) {
                    if (re != 0) {
                        if (gamme.getId() > 0 && re == 1) {
                            return true;
                        }
                        openDialog("dlgConfirmSave");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean saveNew() {

        return false;
    }

    public boolean saveNewGamme(boolean main, boolean control) {
        String action = gamme.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(gamme, control)) {
                entityGamme = UtilProd.buildGammeArticle(gamme, currentUser);
                if (main) {
                    changeStateGamme();
                    entityGamme.setPrincipal(true);
                } else {
                    entityGamme.setPrincipal(false);
                }
                if (gamme.getId() <= 0) {
                    entityGamme.setId(null);
                    entityGamme = (YvsProdGammeArticle) dao.save1(entityGamme);
                    gamme.setId(entityGamme.getId());
                    gammes.add(entityGamme);
                } else {
                    dao.update(entityGamme);
                    gammes.set(gammes.indexOf(entityGamme), entityGamme);
                }
                succes();
                update("data_gamme");
                input_reset = "";
                actionOpenOrResetAfter(this);
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            Logger.getLogger(ManagedGamme.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }

    public void changeStatutActif(YvsProdGammeArticle gamme) {
        //Contrôle droits
        if (gamme != null) {
            gamme.setActif(!gamme.getActif());
            gamme.setAuthor(currentUser);
            dao.update(gamme);
            update("data_gamme");
        } else {
            getErrorMessage("Aucune selection trouvé !");
        }
    }

    private void changeStateGamme() {
        String rq = "UPDATE yvs_prod_gamme_article SET principal=false WHERE article=?";
        Options[] op = new Options[]{new Options(gamme.getArticle().getId(), 1)};
        dao.requeteLibre(rq, op);
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? tabIds.length() > 0 : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsProdGammeArticle> list = new ArrayList<>();
                YvsProdGammeArticle bean;
                for (Long ids : l) {
                    bean = gammes.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);
                }
                gammes.removeAll(list);
                resetFiche();
                succes();
                update("data_gamme");

            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    @Override
    public void updateBean() {
//        if ((tabIds != null) ? tabIds.length() > 0 : false) {
//            String[] ids = tabIds.split("-");
//            setUpdateGamme((ids != null) ? ids.length > 0 : false);
//            if (isUpdateGamme()) {
//                int id = Integer.valueOf(ids[ids.length - 1]);
//                GammeArticle bean = gammes.get(id);
//                populateView(bean);
//                update("blog_form_gamme");
//            }
//        }
    }

    public void loadGammeSite(YvsProdGammeArticle y) {
        List<YvsProdSiteProduction> list = dao.loadNameQueries("YvsProdSiteProduction.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        YvsProdGammeSite g;
        for (YvsProdSiteProduction s : list) {
            boolean exist = false;
            for (YvsProdGammeSite m : y.getSites()) {
                if (m.getSite().equals(s)) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                g = new YvsProdGammeSite(y, s);
                g.setDateSave(new Date());
                g.setDateUpdate(new Date());
                g.setAuthor(currentUser);
                gamme.getSites().add(g);
            }
        }
    }

    @Override
    public void onSelectObject(YvsProdGammeArticle y) {
        y.setOperations(dao.loadNameQueries("YvsProdOperationsGamme.findByGamme", new String[]{"gamme"}, new Object[]{y}));
        populateView(UtilProd.buildSimpleBeanGammeArticle(y));
        loadGammeSite(y);
        //calcule le prochain numéro d'opération
        if (!gamme.getOperations().isEmpty()) {
            operation.setNumero(gamme.getOperations().get(gamme.getOperations().size() - 1).getNumero() + 10);
        }
        update("blog_form_gamme");
    }

    public void openGammeForUpdate(SelectEvent ev) {
        entityGamme = (YvsProdGammeArticle) ev.getObject();
        onSelectObject(entityGamme);
        tabIds = gammes.indexOf(entityGamme) + "";
    }

    @Override
    public void populateView(GammeArticle bean) {
        cloneObject(gamme, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(gamme);
        gamme.setArticle(new Articles());
        gamme.setMasquer(false);
        gamme.getOperations().clear();
        gamme.setPermanant(false);
        gamme.setUniteTemps(new UniteMesure());
        gamme.setPermanant(true);
        gamme.getSites().clear();
        tabIds = "";
//        resetPage();
        update("blog_form_gamme");
    }

    @Override
    public void resetPage() {
//        for (GammeArticle g : gammes) {
//            g.setSelectActif(false);
//        }
    }

    @Override
    public void selectOnView(GammeArticle bean) {
        if ((bean != null) ? bean.getId() != 0 : false) {
            System.err.println("bean : " + bean.getReference());
        }
    }

    public boolean controleFicheOperation(OperationsGamme bean) {
        if (bean.getNumero() <= 0) {
            getErrorMessage("Veuillez préciser un numéro de operation valide !");
            return false;
        }
        if (bean.getReference() == null) {
            getErrorMessage("Veuillez entrer la référence de l'opération !");
            return false;
        }
        return true;
    }

    public void resetFicheOperation() {
        operation = new OperationsGamme();
        //calcule le prochain numéro d'opération
        if (!gamme.getOperations().isEmpty()) {
            operation.setNumero(gamme.getOperations().get(gamme.getOperations().size() - 1).getNumero() + 10);
        }
        tabIds_phase = "";
    }

    public void populateViewOperation(OperationsGamme bean) {
        cloneObject(operation, bean);
        update("blog_form_phase_poste");
        update("data_phase_poste");

        update("blog_form_phase_document");
        update("data_phase_document");

        update("blog_form_phase_indicateur");
        update("data_phase_indicateur");
    }

    public void saveNewOperation() {
        if (gamme.getId() > 0) {
            String action = operation.getId() > 0 ? "Modification" : "Insertion";
            try {
                if (controleFicheOperation(operation)) {
                    selectedOperation = UtilProd.buildOperationGammes(operation);
                    selectedOperation.setAuthor(currentUser);
                    selectedOperation.setGammeArticle(entityGamme);
                    List<YvsProdComposantOp> composants = new ArrayList<>(selectedOperation.getComposants());
                    selectedOperation.getComposants().clear();
                    if (operation.getId() <= 0) {
                        selectedOperation.setId(null);
                        selectedOperation = (YvsProdOperationsGamme) dao.save1(selectedOperation);
                        operation.setId(selectedOperation.getId());
                    } else {
                        dao.update(selectedOperation);
                    }
                    selectedOperation.setComposants(composants);
                    int idx = gamme.getOperations().indexOf(selectedOperation);
                    if (idx >= 0) {
                        gamme.getOperations().set(idx, selectedOperation);
                    } else {
                        gamme.getOperations().add(0, selectedOperation);
                    }
                    if (entityGamme != null) {
                        idx = entityGamme.getOperations().indexOf(selectedOperation);
                        if (idx >= 0) {
                            entityGamme.getOperations().set(idx, selectedOperation);
                        } else {
                            entityGamme.getOperations().add(0, selectedOperation);
                        }
                        idx = gammes.indexOf(entityGamme);
                        if (idx >= 0) {
                            gammes.set(idx, entityGamme);
                        }
                    }
                    loaAllComposantsNomenclature(selectedOperation, false);
                    succes();
                    //calcule le prochain numéro d'opération
                    if (!gamme.getOperations().isEmpty()) {
                        operation.setNumero(gamme.getOperations().get(gamme.getOperations().size() - 1).getNumero() + 10);
                    }
                    update("data_gamme");
                    update("data_gamme_phase");
                }
            } catch (Exception ex) {
                Logger.getLogger(ManagedGamme.class.getName()).log(Level.SEVERE, null, ex);
                getErrorMessage(action + " Impossible !");
            }
        } else {
            getErrorMessage("Aucune référence à la gamme n'a été trouvé !");
            txt_reslut = "false";
        }
    }

    public void deleteBeanOperation(YvsProdOperationsGamme bean) {
        try {
            dao.delete(new YvsProdOperationsGamme(bean.getId()));
            gamme.getOperations().remove(bean);
            if (operation.getId() == bean.getId()) {
                resetFicheOperation();
            }
            succes();
            update("table_operationsGamme");
            update("data_gamme_phase");
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            Logger.getLogger(ManagedGamme.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public YvsProdPosteOperation buildPostePhase(PosteOperation y) {
        YvsProdPosteOperation p = new YvsProdPosteOperation();
        if (y != null) {
            p.setId(y.getId());
            if ((y.getPoste() != null) ? y.getPoste().getId() != 0 : false) {
                p.setPosteCharge(UtilProd.buildPosteCharge(y.getPoste(), currentUser));
            }
            p.setOperations(new YvsProdOperationsGamme(operation.getId()));
            p.setDateUpdate(new Date());
            p.setDateSave(y.getDateSave());
            p.setNombre(y.getNombre());
            p.setTypeCharge(y.getTypeCharge());
            p.setUsers(currentUser);
        }
        return p;
    }

    public boolean controleFichePostePhase(PosteOperation bean) {
        if ((bean.getPoste() != null) ? bean.getPoste().getId() <= 0 : true) {
            getErrorMessage("Vous devez specifier le poste de charge");
            return false;
        }
        //Il doit avoir un poste machine et un poste MO au plus
        if (poste.getId() <= 0) {   // si on n'est pas en modification
            for (YvsProdPosteOperation po : operation.getPostes()) {
                if (po.getTypeCharge().equals(bean.getTypeCharge())) {
                    getErrorMessage("VOus avez déjà enregistré une poste ce type !");
                    return false;
                }
            }
        } else {
            int nbMachine = (bean.getTypeCharge().equals(Constantes.PROD_OP_TYPE_CHARGE_MACHINE) ? 1 : 0);
            int nbMo = (bean.getTypeCharge().equals(Constantes.PROD_OP_TYPE_CHARGE_MO) ? 1 : 0);
            for (YvsProdPosteOperation po : operation.getPostes()) {
                if (po.getTypeCharge().equals(Constantes.PROD_OP_TYPE_CHARGE_MACHINE) && !po.getId().equals(bean.getId())) {
                    nbMachine += 1;
                }
                if (po.getTypeCharge().equals(Constantes.PROD_OP_TYPE_CHARGE_MO) && !po.getId().equals(bean.getId())) {
                    nbMo += 1;
                }
            }
            if (nbMachine > 1 || nbMo > 1) {
                getErrorMessage("Le type de poste est incohérent !", "Vérifier que vous ne n'ayez deux poste de même type");
                return false;
            }

        }

        return true;
    }

    public void selectLinePostePhase(SelectEvent ev) {
        if (ev != null) {
            populateViewPostePhase(UtilProd.buildBeanPostePhase((YvsProdPosteOperation) ev.getObject()));
        }
    }

    public void populateViewPostePhase(PosteOperation bean) {
        cloneObject(poste, bean);
        update("form_view_contenu_onglet:blog_form_phase_poste");
    }

    public void resetFichePostePhase() {
        resetFiche(poste);
        poste.setPoste(new PosteCharge());
        tabIds_poste = "";
    }

    public void saveNewPostePhase() {
        if (operation.getId() > 0) {  //on est dans ce cas uniquement si on modifie la operation           
            String action = poste.getId() > 0 ? "Modification" : "Insertion";
            try {
                if (controleFichePostePhase(poste)) {
                    int idx = postes.indexOf(new YvsProdPosteCharge(poste.getPoste().getId()));
                    YvsProdPosteOperation entity = buildPostePhase(poste);
                    if (idx >= 0) {
                        entity.setPosteCharge(postes.get(idx));
                    }
                    entity.setUsers(currentUser);
                    if (poste.getId() <= 0) {
                        entity.setId(null);
                        entity = (YvsProdPosteOperation) dao.save1(entity);
                        poste.setId(entity.getId());
                        operation.getPostes().add(entity);
                    } else {
                        dao.update(entity);
                        idx = operation.getPostes().indexOf(entity);
                        if (idx >= 0) {
                            operation.getPostes().set(idx, entity);
                        }

                    }
                    resetFichePostePhase();
                    succes();
                    update("data_phase_poste");
                    update("data_gamme_phase");
                }
            } catch (Exception ex) {
                getErrorMessage(action + " Impossible !");
                Logger.getLogger(ManagedGamme.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            getErrorMessage("Vous devez enregsitrer ou modifier une operation");
        }
    }

    public void deleteBeanPostePhase(YvsProdPosteOperation po) {
        try {
            if (po != null) {
                dao.delete(po);
                resetFichePostePhase();
                succes();
                update("form_gamme_phase_poste");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Lymytz Error at...", ex);
        }
    }

    List<PosteOperation> tabPostePhase = new ArrayList<>();

    public void selectOnViewPostePhase(PosteOperation bean) {
        if (bean.isSelectActif()) {
            if (!tabPostePhase.contains(bean)) {
                tabPostePhase.add(bean);
            }
        } else {
            if (tabPostePhase.contains(bean)) {
                tabPostePhase.remove(bean);
            }
        }
        if (!tabPostePhase.isEmpty()) {
            populateViewPostePhase(tabPostePhase.get(tabPostePhase.size() - 1));
        } else {
            resetFichePostePhase();
        }
        update("blog_form_phase_poste");
    }

    public void selectOnViewPostePhase() {
        tabPostePhase.clear();
        if (isAllPoste()) {
//            tabPostePhase.addAll(operation.getPostes());
        }
        if (!tabPostePhase.isEmpty()) {
            populateViewPostePhase(tabPostePhase.get(tabPostePhase.size() - 1));
        } else {
            resetFichePostePhase();
        }
        update("blog_form_phase_poste");
    }

    public YvsProdDocumentTechnique buildDocumentTechnique(DocumentTechnique y) {
        YvsProdDocumentTechnique d = new YvsProdDocumentTechnique();
        if (y != null) {
            d.setId(y.getId());
            d.setDescription(y.getDescription());
            d.setDesignation(y.getDesignation());
            d.setFichier(y.getFichier());
            d.setReference(y.getReference());
            d.setPhaseGamme(selectedOperation);
        }
        return d;
    }

    public DocumentTechnique recopieViewDocumentTechnique() {
        DocumentTechnique d = new DocumentTechnique();
        d.setId(document.getId());
        d.setDescription(document.getDescription());
        d.setDesignation(document.getDesignation());
        d.setFichier(document.getFichier());
        d.setReference(document.getReference());
        return d;
    }

    public boolean controleFicheDocument(DocumentTechnique bean) {
        if (bean.getDesignation() == null || "".equals(bean.getDesignation())) {
            getErrorMessage("Vous devez entret la designation");
            return false;
        }
        return true;
    }

    public void populateViewDocument(DocumentTechnique bean) {
        cloneObject(document, bean);
        setUpdateDocument(true);
    }

    public void resetFicheDocument() {
        resetFiche(document);
        setUpdateDocument(false);
        resetPageDocument();
        tabIds_document = "";
    }

    public void resetPageDocument() {
//        for (DocumentTechnique d : operation.getDocuments()) {
//            d.setSelectActif(false);
//        }
    }

    public void saveNewDocument() {
        if (operation.getId() > 0) {
            if (input_reset_document.equals("reset")) {
                setUpdateDocument(false);
                input_reset_document = "";
            }
            String action = isUpdateDocument() ? "Modification" : "Insertion";
            try {
                DocumentTechnique bean = recopieViewDocumentTechnique();
                if (controleFicheDocument(bean)) {
                    YvsProdDocumentTechnique entity = buildDocumentTechnique(bean);
                    if (!isUpdateDocument()) {
                        entity.setId(null);
                        entity = (YvsProdDocumentTechnique) dao.save1(entity);
                        bean.setId(entity.getId());
                        document.setId(entity.getId());
//                        operation.getDocuments().add(bean);
                    } else {
                        dao.update(entity);
//                        operation.getDocuments().set(operation.getDocuments().indexOf(document), bean);
                    }
                    setUpdateDocument(true);
                    succes();
                    update("data_phase_document");
                }
            } catch (Exception ex) {
                getErrorMessage(action + " Impossible !");
                System.err.println("error " + action + " : " + ex.getMessage());
            }
        } else {
            getErrorMessage("Vous devez enregsitrer ou modifier une operation");
        }
    }

    public void deleteBeanDocument() {
        try {
            if ((tabIds_document != null) ? tabIds_document.length() > 0 : false) {
                String[] ids = tabIds_document.split("-");
                if ((ids != null) ? ids.length > 0 : false) {
                    for (String s : ids) {
                        int id = Integer.valueOf(s);
//                        DocumentTechnique bean = operation.getDocuments().get(id);
//                        dao.delete(new YvsProdDocumentTechnique(bean.getId()));
//                        operation.getDocuments().remove(bean);
                    }
                    resetFicheDocument();
                    succes();
                    update("form_gamme_phase_document");
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression poste operation : " + ex.getMessage());
        }
    }

    List<YvsProdDocumentTechnique> tabDocumentTechnique = new ArrayList<>();

    public void selectOnViewDocument(YvsProdDocumentTechnique bean) {
        if (bean.isSelectActif()) {
            if (!tabDocumentTechnique.contains(bean)) {
                tabDocumentTechnique.add(bean);
            }
        } else {
            if (tabDocumentTechnique.contains(bean)) {
                tabDocumentTechnique.remove(bean);
            }
        }
        if (!tabDocumentTechnique.isEmpty()) {
            populateViewDocument(UtilProd.buildBeanDocumentTechnique(tabDocumentTechnique.get(tabDocumentTechnique.size() - 1)));
        } else {
            resetFicheDocument();
        }
        update("blog_form_phase_document");
    }

    public void selectOnViewDocument() {
        tabDocumentTechnique.clear();
        if (isAllDocument()) {
            tabDocumentTechnique.addAll(operation.getDocuments());
        }
        if (!tabDocumentTechnique.isEmpty()) {
            populateViewDocument(UtilProd.buildBeanDocumentTechnique(tabDocumentTechnique.get(tabDocumentTechnique.size() - 1)));
        } else {
            resetFicheDocument();
        }
        update("blog_form_phase_document");
    }

    public void fileUploadDocument(FileUploadEvent ev) {
        if ((ev != null) ? ev.getFile() != null : false) {
            document.setFichier(ev.getFile().getFileName());
            update("txt_value_file_document");
            succes();
        }
    }

    public void buildIndicateurReussite(YvsProdIndicateurOp ind) {
        if (ind != null) {
            System.err.println(" ---- *** ---- " + ind.getValeurs().size());
            indicateur = new IndicateurReussite(ind.getId());
            indicateur.setId(ind.getId());
            indicateur.setDescription(ind.getCommentaire());
            indicateur.setType(ind.getTypeIndicateur());
            indicateur.setDateSave(ind.getDateSave());
            valeursQualitative = dao.loadNameQueries("YvsProdValeursQualitative.findByIndicateur", new String[]{"indicateur"}, new Object[]{ind});
//            Collections.sort(valeursQualitative, new YvsProdValeursQualitative());
        } else {
            indicateur = new IndicateurReussite();
            valeursQualitative.clear();
        }
    }

    private YvsProdIndicateurOp positionIndicateur(YvsProdOperationsGamme op) {
        if (op != null) {
            for (YvsProdComposantOp co : op.getComposants()) {
                if (co.getIndicateurs() != null) {
                    return co.getIndicateurs();
                }
            }
        }
        return null;
    }

    public void saveNewIndicateurReussite() {
        try {
            if (operation.getId() > 0 && selectedComposantOp != null) {
                selectedIndicateur = positionIndicateur(selectedOperation);
                if (selectedIndicateur == null) {
                    selectedIndicateur = new YvsProdIndicateurOp();
                } else {
                    this.indicateur.setId(selectedIndicateur.getId());
                }
                selectedIndicateur.setId(this.indicateur.getId());
                selectedIndicateur.setTypeIndicateur(this.indicateur.getType());
                selectedIndicateur.setCommentaire(this.indicateur.getDescription());
                selectedIndicateur.setComposantOp(selectedComposantOp);
                selectedIndicateur.setDateSave(this.indicateur.getDateSave());
                selectedIndicateur.setDateUpdate(new Date());
                selectedIndicateur.setAuthor(currentUser);
                //Modifie la position de l'indicateur
                if (selectedIndicateur.getId() != null ? selectedIndicateur.getId() <= 0 : false) {
                    selectedIndicateur.setId(null);
                    selectedIndicateur = (YvsProdIndicateurOp) dao.save1(selectedIndicateur);
                    this.indicateur.setId(selectedIndicateur.getId());
                } else {
                    dao.update(selectedIndicateur);
                }
                selectedComposantOp.setIndicateurs(selectedIndicateur);

                update("table_edit_comp_op_");
            } else {
                getErrorMessage("Formulaire incorrecte !");
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            getException("Production Error", ex);
        }
    }

    public void loadAllModels() {
        modelsQuantitatifs = dao.loadNameQueries("YvsProdModeleQuantitatif.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void chooseModel(ValueChangeEvent ev) {
        if (ev != null ? ev.getNewValue() != null : false) {
            if (valeursQualitative.isEmpty()) {
                Long id = (Long) ev.getNewValue();
                //Liste
                valeursQualitative = dao.loadNameQueries("YvsProdContentModeleQuantitatif.findValByModel", new String[]{"model"}, new Object[]{new YvsProdModeleQuantitatif(id)});
                saveNewIndicateurReussite();
                for (YvsProdValeursQualitative v : valeursQualitative) {
                    v.setId(null);
                    v.setIndicateur(selectedIndicateur);
                    v.setAuthor(currentUser);
                    v.setDateSave(new Date());
                    v.setDateUpdate(new Date());
                    v = (YvsProdValeursQualitative) dao.save1(v);
                }
                succes();
            } else {
                getErrorMessage("La liste n'est pas vide !");
            }
        }
    }

    public void saveModelQuantitatif() {
        if (!valeursQualitative.isEmpty() && (refModel != null) ? !refModel.trim().isEmpty() : false) {
            YvsProdModeleQuantitatif model = (YvsProdModeleQuantitatif) dao.loadOneByNameQueries("YvsProdModeleQuantitatif.findByRefModel", new String[]{"refModel", "societe"}, new Object[]{refModel, currentAgence.getSociete()});
            if (model == null) {
                model = new YvsProdModeleQuantitatif();
                model.setAuthor(currentUser);
                model.setDateSave(new Date());
                model.setDateUpdate(new Date());
                model.setRefModel(refModel);
                model.setSociete(currentAgence.getSociete());
                model = (YvsProdModeleQuantitatif) dao.save1(model);
                modelsQuantitatifs.add(model);
            } else {
                model.setDateUpdate(new Date());
                model.setAuthor(currentUser);
                dao.update(model);
            }
            if (model != null ? model.getId() > 0 : false) {
                YvsProdContentModeleQuantitatif cm;
                for (YvsProdValeursQualitative v : valeursQualitative) {
                    cm = (YvsProdContentModeleQuantitatif) dao.loadOneByNameQueries("YvsProdContentModeleQuantitatif.findOne", new String[]{"model", "valeur"}, new Object[]{model, v});
                    if (cm == null) {
                        cm = new YvsProdContentModeleQuantitatif();
                        cm.setAuthor(currentUser);
                        cm.setDateSave(new Date());
                        cm.setDateUpdate(new Date());
                        cm.setModel(model);
                        cm.setValeur(v);
                        cm = (YvsProdContentModeleQuantitatif) dao.save1(cm);
                    }
                }
                succes();
            }
        } else if (refModel != null ? refModel.trim().isEmpty() : true) {
            getErrorMessage("Veuiller indiquer une référence pour ce model !");
        }
    }

    public void saveValeurQualitative() {
//        if (indicateur.getId() <= 0) {
        saveNewIndicateurReussite();
//        }
        if (selectedIndicateur.getId() != null ? selectedIndicateur.getId() > 0 : false) {
            YvsProdValeursQualitative val = new YvsProdValeursQualitative(valeur.getId());
            val.setAuthor(currentUser);
            val.setCodeCouleur(valeur.getCodeCouleur());
            val.setDateSave(valeur.getDateSave());
            val.setDateUpdate(new Date());
            val.setIndicateur(selectedIndicateur);
            val.setOrdre(valeur.getOrdre());
            val.setValeurText(valeur.getCodeValeur());
            val.setValeurQuantitative(valeur.getValeurQuantitative());
            if (valeur.getId() <= 0) {
                val.setId(null);
                val = (YvsProdValeursQualitative) dao.save1(val);
                valeursQualitative.add(0, val);
            } else {
                dao.update(val);
                int idx = valeursQualitative.indexOf(val);
                if (idx >= 0) {
                    valeursQualitative.set(idx, val);
                }
            }
            valeur = new ValeurIndicateur();
            update("table_valeur_qual");
        } else {
            getErrorMessage("Formulaire incorrecte", "Aucun indicateur selectionné !");
        }

    }

    public void deleteLineValeurIndiCateur(YvsProdValeursQualitative val) {
        if (val != null) {
            try {
                dao.delete(val);
                valeursQualitative.remove(val);
                update("table_valeur_qual");
            } catch (Exception ex) {
                getErrorMessage("Suppression impossible !");
                getException("Prod Indicateur>>>", ex);
            }

        }
    }

    public boolean loadValeurIndicateurs(YvsProdComposantOp bean) {
        if (bean != null) {
            valeursQualitative.clear();
            System.err.println(" Indicateur.... " + bean.getIndicateurs());
            if (bean.getIndicateurs() != null) {
                buildIndicateurReussite(bean.getIndicateurs());
            }
            selectedComposantOp = bean;
            loadAllModels();
            update("zone_indicateur");
            update("table_valeur_qual");
        }
        return true;
    }

    public void populateViewIndicateur(ValeurIndicateur bean) {
        cloneObject(valeur, bean);
        setUpdateIndicateur(true);
    }

    public void loadValeurOnView(SelectEvent ev) {
        if (ev != null) {
            YvsProdValeursQualitative v = (YvsProdValeursQualitative) ev.getObject();
            valeur = new ValeurIndicateur(v.getId());
            valeur.setCodeCouleur(v.getCodeCouleur());
            valeur.setCodeValeur(v.getValeurText());
            valeur.setValeurQuantitative(v.getValeurQuantitative());
            valeur.setDateSave(v.getDateSave());
            valeur.setOrdre(v.getOrdre());
            update("panel_valeurs_ind");
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void selectLineOperation(SelectEvent ev) {

        selectedOperation = (YvsProdOperationsGamme) ev.getObject();
        populateViewOperation(UtilProd.buildSimpleBeanPhaseGamme(selectedOperation));
        //Charge l'indicateur de réussite
        resetFiche(indicateur);
        for (YvsProdComposantOp c : selectedOperation.getComposants()) {
            if (c.getIndicateurs() != null) {
                buildIndicateurReussite(c.getIndicateurs());
            }
        }
        openDialog("dlgAddPhase");
        update("blog_form_phase");
    }

    public void openProprietesPhases() {
        resetFicheOperation();
        openDialog("dlgAddPhase");
        update("blog_form_phase");
    }

    public String getReference(Articles y) {
        String n = "GAMME-" + y.getRefArt() + "/00";
        champ = new String[]{"reference", "societe"};
        val = new Object[]{n, currentAgence.getSociete()};
        YvsProdGammeArticle p = (YvsProdGammeArticle) dao.loadOneByNameQueries("YvsProdGammeArticle.findByReference", champ, val);
        if (p != null ? !p.getId().equals(gamme.getId()) : false) {
            for (int i = 1; i < 100; i++) {
                if (i < 10) {
                    n = "GAMME-" + y.getRefArt() + "/0" + i;
                } else {
                    n = "GAMME-" + y.getRefArt() + "/" + i;
                }
                champ = new String[]{"reference", "societe"};
                val = new Object[]{n, currentAgence.getSociete()};
                p = (YvsProdGammeArticle) dao.loadOneByNameQueries("YvsProdGammeArticle.findByReference", champ, val);
                if (p != null ? p.getId() < 1 : true) {
                    return n;
                }
            }
        }
        return n;
    }

    public void loaAllComposantsNomenclature(YvsProdOperationsGamme op, boolean open) {
        selectedOperation = op;
        selectedOperation.setComposants(dao.loadNameQueries("YvsProdComposantOp.findByOperation", new String[]{"operation"}, new Object[]{op}));;
        operation = UtilProd.buildSimpleBeanPhaseGamme(op);
        List<YvsProdComposantNomenclature> listComposant = dao.loadNameQueries("YvsProdComposantNomenclature.findComposantArticle", new String[]{"article"}, new Object[]{op.getGammeArticle().getArticle()});
        boolean trouve;
        for (YvsProdComposantNomenclature cn : listComposant) {
            trouve = false;
            for (YvsProdComposantOp cop : operation.getComposants()) {
                if (cop.getComposant().equals(cn) || cop.getComposant().getArticle().equals(cn.getArticle())) {
                    trouve = true;
                    break;
                }
            }
            if (!trouve) {
                Character sens = (cn.getType().equals(Constantes.PROD_OP_TYPE_COMPOSANT_NORMAL) ? Constantes.STOCK_SENS_SORTIE : Constantes.STOCK_SENS_ENTREE);
                Double value = (Double) dao.loadObjectByNameQueries("YvsProdComposantOp.findSumByGammeComposant", new String[]{"composant", "gamme", "sens"}, new Object[]{cn, new YvsProdGammeArticle(gamme.getId()), sens});
                double taux = 100 - (value != null ? value : 0);
                //Ajoute le composant opératoire
                YvsProdComposantOp y = new YvsProdComposantOp(-cn.getId().intValue());
                y.setAuthor(currentUser);
                y.setComposant(cn);
                y.setDateSave(new Date());
                y.setOperation(op);
                y.setSens('S');
                y.setQuantite(taux > 0 ? taux : 0);
                operation.getComposants().add(y);
            }
        }
        selectedComposantOp = new YvsProdComposantOp(0);
        if (open) {
            openDialog("dlgMoidfCompOp");
        }
        update("dia_edit_comp_op");
        update("data_gamme_phase");
        update("form_view_contenu_onglet:table_edit_comp_op_");
    }

    public void loadToViewComposant(SelectEvent ev) {
        selectedComposantOp = (YvsProdComposantOp) ev.getObject();
    }

    public void onEditQuantite(CellEditEvent event) {
        String col = event.getColumn().getHeaderText();
        if (event.getRowIndex() >= 0) {
            YvsProdComposantOp coop = operation.getComposants().get(event.getRowIndex());
            Double newValue = coop.getQuantite();
            updateQuantiteComposant(coop, newValue, event.getRowIndex());
        }
    }

    public void updateQuantiteComposant(YvsProdComposantOp coop, Double newValue, int idx) {
        YvsProdComposantOp old;
        if (coop.getId() <= 0) {
            coop = addOrDeleteComposantToOp(coop);
            if (coop == null) {
                return;
            }
        }
        if (coop.getId() > 0) {
            if (idx < 0) {
                idx = operation.getComposants().indexOf(coop);
            }
            old = (YvsProdComposantOp) dao.loadOneByNameQueries("YvsProdComposantOp.findById", new String[]{"id"}, new Object[]{coop.getId()});
            Double oldValue = (old != null) ? old.getQuantite() : 0d;
            Double value = (Double) dao.loadObjectByNameQueries("YvsProdComposantOp.findSumByGammeComposant", new String[]{"composant", "gamme", "sens"}, new Object[]{coop.getComposant(), new YvsProdGammeArticle(gamme.getId()), coop.getSens()});
            if (((value != null ? value : 0) - oldValue + newValue) > 100) {
                getErrorMessage("Vous ne pouvez pas entrer cette valeur...car la somme des taux depassera 100%");
                operation.getComposants().get(idx).setQuantite(oldValue);
                update("table_edit_comp_op");
                return;
            }
            coop.setDateUpdate(new Date());
            coop.setQuantite(newValue);
            dao.update(coop);
            succes();
        }
    }

    public void openToEditCoefficientVale(YvsProdComposantOp c) {
        selectedComposantOp = c;
    }

    public void applyCoefficientValeur() {
        if (selectedComposantOp != null) {
            selectedComposantOp.setAuthor(currentUser);
            selectedComposantOp.setDateUpdate(new Date());
            dao.update(selectedComposantOp);
        }
    }

    public void changeSensComposant(YvsProdComposantOp cop, char sens) {
        if (cop.getId() > 0) {
            cop.setSens(sens);
            cop.setAuthor(currentUser);
            cop.setDateUpdate(new Date());
            dao.update(cop);
        }
        setSelectedComposantOp(cop);
        update("table_edit_comp_op");
        update("table_edit_comp_op_");
    }

    public void addOrDeleteAllComposantToOp() {
        for (YvsProdComposantOp op : operation.getComposants()) {
            addOrDeleteComposantToOp(op);
        }
    }

    public YvsProdComposantOp addOrDeleteComposantToOp(YvsProdComposantOp op) {
        if (op.getId() <= 0) {
            Double value = (Double) dao.loadObjectByNameQueries("YvsProdComposantOp.findSumByComposant", new String[]{"composant", "gamme", "sens"}, new Object[]{op.getComposant(), op.getOperation().getGammeArticle(), op.getSens()});
            if (((value != null ? value : 0) + op.getQuantite()) > 100) {
                getErrorMessage("Vous ne pouvez pas entrer cette valeur...car la somme des coefficients depassera 100%");
                return null;
            }
            op.setDateUpdate(new Date());
            op.setAuthor(currentUser);
            op.setId(null);
//            op.setUnite(op.getComposant().getUnite());
            op.setTauxPerte(0d);
            op = (YvsProdComposantOp) dao.save1(op);
        } else {
            try {
                dao.delete(op);
                op.setId(-op.getId());
                Double value = (Double) dao.loadObjectByNameQueries("YvsProdComposantOp.findSumByGammeComposant", new String[]{"composant", "gamme", "sens"}, new Object[]{op.getComposant(), new YvsProdGammeArticle(gamme.getId()), op.getSens()});
                double taux = 100 - (value != null ? value : 0);
                op.setQuantite(taux > 0 ? taux : 0);
            } catch (Exception ex) {
                getErrorMessage("Suppression impossible !");
                getException("Error Production ", ex);
            }
        }
        update("table_edit_comp_op");

        return op;
    }

    public void toogleActiveOperation(YvsProdOperationsGamme op) {
        if (op != null) {
            op.setActif(!op.getActif());
            op.setAuthor(currentUser);
            op.setDateUpdate(new Date());
            dao.update(op);
        }
    }

    public void clearParams() {
        numSearch = null;
        artSearch = null;
        dateSeacrh = false;
        categorieSearch = null;
        siteSearch = 0;
        actifSearch = null;
        forCondSearch = null;
        paginator.getParams().clear();
        loadAll(true, true);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.reference", "reference", null);
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", numSearch.toUpperCase() + "%", "LIKE", "AND");
            switch (droit) {
                case 1:
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.codeRef)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.designation)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
                    break;
                case 2:
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.gamme.codeRef)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.gamme.designation)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
                    break;
            }
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamActif() {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", null, "=", "AND");
        switch (droit) {
            case 1:
                p = new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND");
                break;
            case 2:
                p = new ParametreRequete("y.gamme.actif", "actif", actifSearch, "=", "AND");
                break;
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamSite() {
        ParametreRequete p = new ParametreRequete("y.site", "site", null);
        updateParametreToSite(false);
        if (siteSearch > 0) {
            p = new ParametreRequete("y.site", "site", new YvsProdSiteProduction(siteSearch), "=", "AND");
            updateParametreToSite(true);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    private void updateParametreToSite(boolean bySite) {
        droit = buildDroit();
        if (droit == 1) {
            for (int i = 0; i < paginator.getParams().size(); i++) {
                ParametreRequete p = paginator.getParams().get(i);
                if (!p.getParamNome().equals("site")) {
                    if (bySite) {
                        p.setAttribut(p.getAttribut().replace("y.", "y.gamme."));
                    } else {
                        p.setAttribut(p.getAttribut().replace("y.gamme.", "y."));
                    }
                    paginator.getParams().set(i, p);
                }
            }
            if (bySite) {
                droit = 2;
            }
        }
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.debutValidite", "dates", null, "=", "AND");
        if (dateSeacrh) {
            p = new ParametreRequete(null, "dates", null, "=", "AND");
            switch (droit) {
                case 1:
                    p.getOtherExpression().add(new ParametreRequete("y.debutValidite", "dates", dateDebutSearch, dateFinSearch, "BETWEEN", "AND"));
                    p.getOtherExpression().add(new ParametreRequete("y.finValidite", "dates", dateDebutSearch, dateFinSearch, "BETWEEN", "AND"));
                    break;
                case 2:
                    p.getOtherExpression().add(new ParametreRequete("y.gamme.debutValidite", "dates", dateDebutSearch, dateFinSearch, "BETWEEN", "AND"));
                    p.getOtherExpression().add(new ParametreRequete("y.gamme.finValidite", "dates", dateDebutSearch, dateFinSearch, "BETWEEN", "AND"));
                    break;
            }
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamArticle() {
        ParametreRequete p = new ParametreRequete("y.article", "article", null);
        if (artSearch != null ? artSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "article", artSearch.toUpperCase() + "%", "LIKE", "AND");
            switch (droit) {
                case 1:
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "article", artSearch.toUpperCase() + "%", "LIKE", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "article", artSearch.toUpperCase() + "%", "LIKE", "OR"));
                    break;
                case 2:
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.gamme.article.refArt)", "article", artSearch.toUpperCase() + "%", "LIKE", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.gamme.article.designation)", "article", artSearch.toUpperCase() + "%", "LIKE", "OR"));
                    break;
            }
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamCategorie() {
        ParametreRequete p = new ParametreRequete("y.gamme.article.categorie", "categorie", null);
        if (categorieSearch != null ? categorieSearch.trim().length() > 0 : false) {
            switch (droit) {
                case 1:
                    p = new ParametreRequete("y.article.categorie", "categorie", categorieSearch, "=", "AND");
                    break;
                case 2:
                    p = new ParametreRequete("y.gamme.article.categorie", "categorie", categorieSearch, "=", "AND");
                    break;
            }
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void activeGammeSite(YvsProdGammeSite bean) {
        if (bean != null) {
            long id = bean.getId();
            if (bean.getId() > 0) {
                dao.delete(bean);
                bean.setId(-id);
            } else {
                bean.setId(null);
                bean = (YvsProdGammeSite) dao.save1(bean);
            }
            int idx = gamme.getSites().indexOf(new YvsProdGammeSite(id));
            if (idx > -1) {
                gamme.getSites().set(idx, bean);
            }
            succes();
        }
    }

    public void buildCodeAcces(YvsProdGammeArticle y) {
        if (y != null) {
            codeAcces = y.getAcces();
            entityGamme = y;
            update("data_code_acces_gamme");
        }
    }

    public void attribCodeAcces(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            codeAcces = (YvsBaseCodeAcces) ev.getObject();
            entityGamme.setAcces(codeAcces);
            dao.update(entityGamme);
            int idx = gammes.indexOf(entityGamme);
            if (idx > -1) {
                gammes.set(idx, entityGamme);
            }
            succes();
        }
    }

    public boolean toDownOperation(YvsProdOperationsGamme y) {
        int idx = gamme.getOperations().indexOf(y);
        if (idx > -1) {
            return idx < gamme.getOperations().size() - 1;
        }
        return false;
    }

    public void downOperation(YvsProdOperationsGamme y) {
        if (toDownOperation(y)) {
            int idx = gamme.getOperations().indexOf(y);
            YvsProdOperationsGamme n = gamme.getOperations().get(idx + 1);
            int ordre = n.getNumero();
            n.setNumero(y.getNumero());
            y.setNumero(ordre == 0 ? 10 : ordre);

            dao.update(n);
            dao.update(y);

            gamme.getOperations().set(idx, n);
            gamme.getOperations().set(idx + 1, y);

            succes();
        }
    }

    public boolean toUpOperation(YvsProdOperationsGamme y) {
        int idx = gamme.getOperations().indexOf(y);
        if (idx > -1) {
            return idx > 0;
        }
        return false;
    }

    public void upOperation(YvsProdOperationsGamme y) {
        if (toUpOperation(y)) {
            int idx = gamme.getOperations().indexOf(y);
            YvsProdOperationsGamme n = gamme.getOperations().get(idx - 1);
            int ordre = n.getNumero();
            n.setNumero(y.getNumero() == 0 ? 10 : y.getNumero());
            y.setNumero(ordre);

            dao.update(n);
            dao.update(y);

            gamme.getOperations().set(idx, n);
            gamme.getOperations().set(idx - 1, y);

            succes();
        }
    }

    public void fusionner(boolean fusionne) {
        try {
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                long newValue = gammes.get(ids.get(0)).getId();
                if (!fusionne) {
                    if (!autoriser("base_user_fusion")) {
                        openNotAcces();
                        return;
                    }
                    String oldValue = "0";
                    for (int i : ids) {
                        if (gammes.get(i).getId() != newValue) {
                            oldValue += "," + gammes.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_prod_gamme_article", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                gammes.remove(new YvsProdGammeArticle(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = gammes.get(idx).getCodeRef();
                    } else {
                        YvsProdGammeArticle c = (YvsProdGammeArticle) dao.loadOneByNameQueries("YvsProdGammeArticle.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? c.getId() > 0 : false) {
                            fusionneTo = c.getCodeRef();
                        }
                    }
                    YvsProdGammeArticle c;
                    for (int i : ids) {
                        long oldValue = gammes.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(gammes.get(i).getCodeRef());
                            }
                        } else {
                            c = (YvsProdGammeArticle) dao.loadOneByNameQueries("YvsProdGammeArticle.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? c.getId() > 0 : false) {
                                fusionnesBy.add(c.getCodeRef());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 gammes");
            }
        } catch (NumberFormatException ex) {
            getException("Error fusionner ", ex);
        }
    }

    public void fusionnerOperation(boolean fusionne) {
        try {
            fusionneToOperation = "";
            fusionnesByOperation.clear();
            List<Integer> ids = decomposeSelection(tabIds_phase);
            if (!ids.isEmpty()) {
                long newValue = gamme.getOperations().get(ids.get(0)).getId();
                if (!fusionne) {
                    if (!autoriser("base_user_fusion")) {
                        openNotAcces();
                        return;
                    }
                    String oldValue = "0";
                    for (int i : ids) {
                        if (gamme.getOperations().get(i).getId() != newValue) {
                            oldValue += "," + gamme.getOperations().get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_prod_operations_gamme", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                gamme.getOperations().remove(new YvsProdOperationsGamme(id.intValue()));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneToOperation = gamme.getOperations().get(idx).getCodeRef();
                    } else {
                        YvsProdOperationsGamme c = (YvsProdOperationsGamme) dao.loadOneByNameQueries("YvsProdOperationsGamme.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? c.getId() > 0 : false) {
                            fusionneToOperation = c.getCodeRef();
                        }
                    }
                    YvsProdOperationsGamme c;
                    for (int i : ids) {
                        long oldValue = gamme.getOperations().get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesByOperation.add(gamme.getOperations().get(i).getCodeRef());
                            }
                        } else {
                            c = (YvsProdOperationsGamme) dao.loadOneByNameQueries("YvsProdOperationsGamme.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? c.getId() > 0 : false) {
                                fusionnesByOperation.add(c.getCodeRef());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 phases");
            }
        } catch (NumberFormatException ex) {
            getException("Error fusionner ", ex);
        }
    }

    public void confirmMasque(YvsProdGammeArticle game) {
        entityGamme = game;
        if (!game.getMasquer()) {
            openDialog("dlgConfirmMasque");
        } else {
            masqueGamme();
        }
    }

    public void masqueGamme() {
        if (entityGamme != null) {
            entityGamme.setMasquer(!entityGamme.getMasquer());
            entityGamme.setAuthor(currentUser);
            entityGamme.setDateUpdate(new Date());
            dao.update(entityGamme);
            if (entityGamme.getMasquer()) {
                gammes.remove(entityGamme);
                closeDialog("dlgConfirmMasque");
            }
            update("data_gamme");
        }
    }

    public void maintenance() {
        List<YvsProdGammeArticle> l = dao.loadNameQueries("YvsProdGammeArticle.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        for (YvsProdGammeArticle g : l) {
            for (YvsProdOperationsGamme op : g.getOperations()) {
                if (op.getCodeRef().equals("PREPARATION")) {
                    op.setNumero(20);
                    op.setActif(false);
                    dao.update(op);
                } else {
                    op.setNumero(10);
                    dao.update(op);
                }
            }
        }
        succes();
    }

    public void activeSelectNomeclature(YvsProdSiteProduction site) {
        List<Integer> selections = decomposeSelection(tabIds);
        YvsProdGammeSite bean;
        Long NB;
        int n = 0;
        for (Integer i : selections) {
            bean = new YvsProdGammeSite();
            bean.setAuthor(currentUser);
            bean.setDateSave(new Date());
            bean.setDateUpdate(new Date());
            bean.setGamme(gammes.get(i));
            bean.setSite(site);
            NB = (Long) dao.loadObjectByNameQueries("YvsProdNomenclatureSite.findCOne", new String[]{"nomenclature", "site"}, new Object[]{gammes.get(i), site});
            if (!Util.asLong(NB)) {
                dao.save(bean);
            }
            n++;
        }
        if (n > 0) {
            succes();
        } else {
            getWarningMessage("Aucune nomenclature n'a été selectionné ");
        }
    }

    public void desactiveSelectNomeclature(YvsProdSiteProduction site) {
        List<Integer> selections = decomposeSelection(tabIds);
        int n = 0;
        String query = "DELETE FROM yvs_prod_nomenclature_site WHERE nomenclature=? AND site=?";
        for (Integer i : selections) {
            dao.requeteLibre(query, new Options[]{new Options(gammes.get(i).getId(), 1), new Options(site.getId(), 2)});
            n++;
        }
        if (n > 0) {
            succes();
        } else {
            getWarningMessage("Aucune nomenclature n'a été selectionné ");
        }
    }

}
