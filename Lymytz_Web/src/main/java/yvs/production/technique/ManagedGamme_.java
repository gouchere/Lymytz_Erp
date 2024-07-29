///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.production.technique;
//
//import java.io.Serializable;
//import yvs.entity.compta.YvsBaseCaisse;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.SessionScoped;
//import org.primefaces.event.FileUploadEvent;
//import org.primefaces.event.SelectEvent;
//import org.primefaces.event.UnselectEvent;
//import yvs.base.produits.Articles;
//import yvs.base.produits.UniteMesure;
//import yvs.base.produits.UtilProd;
//import yvs.dao.Options;
//import yvs.entity.base.YvsBaseUniteMesure;
//import yvs.entity.production.base.YvsBaseIndicateurReussite;
//import yvs.entity.production.base.YvsProdDocumentTechnique;
//import yvs.entity.production.base.YvsProdGammeArticle;
//import yvs.entity.production.base.YvsProdIndicateurOp;
//import yvs.entity.production.base.YvsProdOperationsGamme;
//import yvs.entity.production.base.YvsProdPosteCharge;
//import yvs.entity.production.base.YvsProdPostePhase;
//import yvs.entity.produits.YvsBaseArticles;
//import yvs.production.DocumentTechnique;
//import yvs.production.base.IndicateurPhase;
//import yvs.production.base.IndicateurReussite;
//import yvs.production.base.OperationsGamme;
//import yvs.util.Managed;
//
///**
// *
// * @author lymytz
// */
//@ManagedBean
//@SessionScoped
//public class ManagedGamme_ extends Managed<GammeArticle, YvsBaseCaisse> implements Serializable {
//
//    @ManagedProperty(value = "#{gammeArticle}")
//    private GammeArticle gamme;
//    private OperationsGamme phase = new OperationsGamme(), parent;
//    private OperationsGamme operation = new OperationsGamme();
//    private List<GammeArticle> gammes, listGamme;
//    private List<Articles> articles;
//    private PostePhase poste = new PostePhase();
//    private List<PosteCharge> postes;
//    private DocumentTechnique document = new DocumentTechnique();
//    private IndicateurReussite reussite = new IndicateurReussite();
//    private List<IndicateurReussite> indicateurs;
//    private IndicateurPhase indicateur = new IndicateurPhase();
//    private List<UniteMesure> unites;
//    private UniteMesure unite = new UniteMesure();
//    private String txt_reslut, tabIds, tabIds_phase, tabIds_poste, tabIds_document, tabIds_indicateur,
//            input_reset, input_reset_phase, input_reset_poste, input_reset_document, input_reset_indicateur;
//    private boolean updateGamme, updatePhase, updatePoste, updateDocument, updateIndicateur,
//            allPhase, allPoste, allDocument, allIndicateur;
//
//    YvsProdGammeArticle entityGamme;
//    YvsProdOperationsGamme entityPhase;
//    private List<OperationsGamme> phases;
//
//    private boolean formPhase;
//
//    public ManagedGamme_() {
//        unites = new ArrayList<>();
//        indicateurs = new ArrayList<>();
//        postes = new ArrayList<>();
//        gammes = new ArrayList<>();
//        listGamme = new ArrayList<>();
//        articles = new ArrayList<>();
//        phases = new ArrayList<>();
//    }
//
//    public OperationsGamme getParent() {
//        return parent;
//    }
//
//    public void setParent(OperationsGamme parent) {
//        this.parent = parent;
//    }
//
//    public boolean isFormPhase() {
//        return formPhase;
//    }
//
//    public void setFormPhase(boolean formPhase) {
//        this.formPhase = formPhase;
//    }
//
//    public OperationsGamme getOperation() {
//        return operation;
//    }
//
//    public void setOperation(OperationsGamme operation) {
//        this.operation = operation;
//    }
//
//    public List<OperationsGamme> getPhases() {
//        return phases;
//    }
//
//    public void setPhases(List<OperationsGamme> phases) {
//        this.phases = phases;
//    }
//
//    public UniteMesure getUnite() {
//        return unite;
//    }
//
//    public void setUnite(UniteMesure unite) {
//        this.unite = unite;
//    }
//
//    public String getTxt_reslut() {
//        return txt_reslut;
//    }
//
//    public void setTxt_reslut(String txt_reslut) {
//        this.txt_reslut = txt_reslut;
//    }
//
//    public IndicateurReussite getReussite() {
//        return reussite;
//    }
//
//    public void setReussite(IndicateurReussite reussite) {
//        this.reussite = reussite;
//    }
//
//    public List<IndicateurReussite> getIndicateurs() {
//        return indicateurs;
//    }
//
//    public void setIndicateurs(List<IndicateurReussite> indicateurs) {
//        this.indicateurs = indicateurs;
//    }
//
//    public IndicateurPhase getIndicateur() {
//        return indicateur;
//    }
//
//    public void setIndicateur(IndicateurPhase indicateur) {
//        this.indicateur = indicateur;
//    }
//
//    public String getTabIds_indicateur() {
//        return tabIds_indicateur;
//    }
//
//    public void setTabIds_indicateur(String tabIds_indicateur) {
//        this.tabIds_indicateur = tabIds_indicateur;
//    }
//
//    public String getInput_reset_indicateur() {
//        return input_reset_indicateur;
//    }
//
//    public void setInput_reset_indicateur(String input_reset_indicateur) {
//        this.input_reset_indicateur = input_reset_indicateur;
//    }
//
//    public boolean isUpdateIndicateur() {
//        return updateIndicateur;
//    }
//
//    public void setUpdateIndicateur(boolean updateIndicateur) {
//        this.updateIndicateur = updateIndicateur;
//    }
//
//    public boolean isAllIndicateur() {
//        return allIndicateur;
//    }
//
//    public void setAllIndicateur(boolean allIndicateur) {
//        this.allIndicateur = allIndicateur;
//    }
//
//    public DocumentTechnique getDocument() {
//        return document;
//    }
//
//    public void setDocument(DocumentTechnique document) {
//        this.document = document;
//    }
//
//    public String getTabIds_document() {
//        return tabIds_document;
//    }
//
//    public void setTabIds_document(String tabIds_document) {
//        this.tabIds_document = tabIds_document;
//    }
//
//    public String getInput_reset_document() {
//        return input_reset_document;
//    }
//
//    public void setInput_reset_document(String input_reset_document) {
//        this.input_reset_document = input_reset_document;
//    }
//
//    public boolean isUpdateDocument() {
//        return updateDocument;
//    }
//
//    public void setUpdateDocument(boolean updateDocument) {
//        this.updateDocument = updateDocument;
//    }
//
//    public boolean isAllDocument() {
//        return allDocument;
//    }
//
//    public void setAllDocument(boolean allDocument) {
//        this.allDocument = allDocument;
//    }
//
//    public boolean isAllPoste() {
//        return allPoste;
//    }
//
//    public void setAllPoste(boolean allPoste) {
//        this.allPoste = allPoste;
//    }
//
//    public String getTabIds_poste() {
//        return tabIds_poste;
//    }
//
//    public void setTabIds_poste(String tabIds_poste) {
//        this.tabIds_poste = tabIds_poste;
//    }
//
//    public String getInput_reset_poste() {
//        return input_reset_poste;
//    }
//
//    public void setInput_reset_poste(String input_reset_poste) {
//        this.input_reset_poste = input_reset_poste;
//    }
//
//    public boolean isUpdatePoste() {
//        return updatePoste;
//    }
//
//    public void setUpdatePoste(boolean updatePoste) {
//        this.updatePoste = updatePoste;
//    }
//
//    public List<PosteCharge> getPostes() {
//        return postes;
//    }
//
//    public void setPostes(List<PosteCharge> postes) {
//        this.postes = postes;
//    }
//
//    public PostePhase getPoste() {
//        return poste;
//    }
//
//    public void setPoste(PostePhase poste) {
//        this.poste = poste;
//    }
//
//    public boolean isAllPhase() {
//        return allPhase;
//    }
//
//    public void setAllPhase(boolean allPhase) {
//        this.allPhase = allPhase;
//    }
//
//    public boolean isUpdatePhase() {
//        return updatePhase;
//    }
//
//    public void setUpdatePhase(boolean updatePhase) {
//        this.updatePhase = updatePhase;
//    }
//
//    public List<GammeArticle> getListGamme() {
//        return listGamme;
//    }
//
//    public void setListGamme(List<GammeArticle> listGamme) {
//        this.listGamme = listGamme;
//    }
//
//    public OperationsGamme getPhase() {
//        return phase;
//    }
//
//    public void setPhase(OperationsGamme phase) {
//        this.phase = phase;
//    }
//
//    public String getInput_reset_phase() {
//        return input_reset_phase;
//    }
//
//    public void setInput_reset_phase(String input_reset_phase) {
//        this.input_reset_phase = input_reset_phase;
//    }
//
//    public String getTabIds_phase() {
//        return tabIds_phase;
//    }
//
//    public void setTabIds_phase(String tabIds_phase) {
//        this.tabIds_phase = tabIds_phase;
//    }
//
//    public List<Articles> getArticles() {
//        return articles;
//    }
//
//    public void setArticles(List<Articles> articles) {
//        this.articles = articles;
//    }
//
//    public List<GammeArticle> getGammes() {
//        return gammes;
//    }
//
//    public void setGammes(List<GammeArticle> gammes) {
//        this.gammes = gammes;
//    }
//
//    public String getInput_reset() {
//        return input_reset;
//    }
//
//    public void setInput_reset(String input_reset) {
//        this.input_reset = input_reset;
//    }
//
//    public String getTabIds() {
//        return tabIds;
//    }
//
//    public void setTabIds(String tabIds) {
//        this.tabIds = tabIds;
//    }
//
//    public boolean isUpdateGamme() {
//        return updateGamme;
//    }
//
//    public void setUpdateGamme(boolean updateGamme) {
//        this.updateGamme = updateGamme;
//    }
//
//    public GammeArticle getGamme() {
//        return gamme;
//    }
//
//    public void setGamme(GammeArticle gamme) {
//        this.gamme = gamme;
//    }
//
//    public List<UniteMesure> getUnites() {
//        return unites;
//    }
//
//    public void setUnites(List<UniteMesure> unites) {
//        this.unites = unites;
//    }
//
//    @Override
//    public void loadAll() {
////        checkCurrentSte();
//        loadAllUniteTemps();
//        loadAllGammeArticle();
//        loadAllArticle();
////        loadAllGammeRefere();
//        loadAllPosteCharge();
//        loadAllIndicateur();
//    }
//
//    public void loadAllUniteTemps() {
//        unites.clear();
//        List<YvsBaseUniteMesure> l = dao.loadNameQueries("YvsBaseUniteMesure.findByType", new String[]{"type", "societe"}, new Object[]{"T", currentScte});
//        unites = UtilProd.buildBeanListUniteMasse(l);
//    }
//
//    public void loadAllGammeArticle() {
//        champ = new String[]{"societe"};
//        val = new Object[]{currentScte};
//        gammes.clear();
//        List<YvsProdGammeArticle> l = dao.loadNameQueries("YvsProdGammeArticle.findAll", champ, val);
//        gammes = UtilProd.buildBeanListGammeArticle(l, null, null);
//    }
//
//    public void loadAllGammeRefere() {
//        champ = new String[]{"societe"};
//        val = new Object[]{currentScte};
//        List<YvsProdGammeArticle> l = dao.loadNameQueries("YvsProdGammeArticle.findAll", champ, val);
//        listGamme = UtilProd.buildBeanListGammeArticle(l, null, null);
//    }
//
//    public void loadAllArticle() {
//        articles.clear();
//        champ = new String[]{"societe"};
//        val = new Object[]{currentScte};
//        List<YvsBaseArticles> l = dao.loadNameQueries("YvsBaseArticles.findAll", champ, val);
////        articles = UtilProd.buildBeanListArticleProduction(l);
//    }
//
//    public void loadAllPosteCharge() {
//        postes.clear();
//        List<YvsProdPosteCharge> l = dao.loadNameQueries("YvsProdPosteCharge.findAll", new String[]{}, new Object[]{});
//        postes = UtilProd.buildBeanListPosteCharge(l, null, null);
//    }
//
//    public void loadAllIndicateur() {
//        indicateurs.clear();
//        List<YvsBaseIndicateurReussite> l = dao.loadNameQueries("YvsBaseIndicateurReussite.findAll", new String[]{}, new Object[]{});
//        indicateurs = UtilProd.buildBeanListIndicateurReussite(l);
//    }
//
//    public YvsBaseUniteMesure buildUniteMasse(UniteMesure u) {
//        YvsBaseUniteMesure r = new YvsBaseUniteMesure();
//        if (u != null) {
//            r.setId(u.getId());
//            r.setReference(u.getReference());
//            r.setDescription(u.getDescription());
//            r.setType("T");
//            r.setLibelle(u.getLibelle());
//            r.setSociete(currentScte);
//        }
//        return r;
//    }
//
//    public UniteMesure recopiewUnite() {
//        UniteMesure r = new UniteMesure();
//        r.setId(unite.getId());
//        r.setReference(unite.getReference());
//        r.setDescription(unite.getDescription());
//        r.setType(unite.getType());
//        r.setLibelle(unite.getLibelle());
//        return r;
//    }
//
//    public boolean controleFicheUnite(UniteMesure bean) {
//        if (bean.getLibelle() == null || bean.getLibelle().equals("")) {
//            getErrorMessage("Entrer le libelle!");
//            return false;
//        }
//        return true;
//    }
//
//    public void saveNewUnite() {
//        UniteMesure bean = recopiewUnite();
//        if (controleFicheUnite(bean)) {
//            YvsBaseUniteMesure entity = buildUniteMasse(bean);
//            entity = (YvsBaseUniteMesure) dao.save1(entity);
//            bean.setId(entity.getId());
//            unites.add(bean);
//            resetFiche(unite);
//        }
//        update("unite_duree");
//    }
//
//    public YvsProdGammeArticle buildGammeArticle(GammeArticle y) {
//        YvsProdGammeArticle g = new YvsProdGammeArticle();
//        if (y != null) {
//            g.setId(y.getId());
//            if ((y.getArticle() != null) ? y.getArticle().getId() != 0 : false) {
//                g.setArticle(new YvsBaseArticles(y.getArticle().getId()));
//            }
//            g.setDescription(y.getDescription());
//            g.setDesignation(y.getDesignation());
//            g.setCodeRef(y.getReference());
//            g.setPrincipal(y.isPrincipal());
//            g.setActif(true);
//        }
//        return g;
//    }
//
//    @Override
//    public GammeArticle recopieView() {
//        GammeArticle g = new GammeArticle();
//        g.setId(gamme.getId());
//        Articles bean = new Articles();
//        cloneObject(bean, articles.get(articles.indexOf(gamme.getArticle())));
//        gamme.setArticle(bean);
//        g.setArticle(gamme.getArticle());
//        g.setDescription(gamme.getDescription());
//        g.setDesignation(gamme.getDesignation());
//        g.setReference(gamme.getReference());
//        g.setActif(gamme.isActif());
//        g.setPrincipal(gamme.isPrincipal());
//        return g;
//    }
//
//    @Override
//    public boolean controleFiche(GammeArticle bean) {
//        return true;
//    }
//
//    public boolean controleFiche(GammeArticle bean, boolean control) {
//        if (control) {
//            if (bean.getReference().equals("") || bean.getReference() == null) {
//                getErrorMessage("Vous devez entrer la reference");
//                return false;
//            }
//            if (bean.isPrincipal()) {
//                //vérifie si l'article a déjà une gamme principal
//                champ = new String[]{"article"};
//                val = new Object[]{new YvsBaseArticles(gamme.getArticle().getId())};
//                Long re = (Long) dao.loadObjectByNameQueries("YvsProdGammeArticle.CountByPrincipalArticle", champ, val);
//                if (re != null) {
//                    if (re != 0) {
//                        if (updateGamme && re == 1) {
//                            return true;
//                        }
//                        openDialog("dlgConfirmSave");
//                        return false;
//                    }
//                }
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public boolean saveNew() {
//
//        return false;
//    }
//
//    public boolean saveNewGamme(boolean main, boolean control) {
//        String action = isUpdateGamme() ? "Modification" : "Insertion";
//        try {
//            GammeArticle bean = recopieView();
//            if (controleFiche(bean, control)) {
//                entityGamme = buildGammeArticle(bean);
//                entityGamme.setAuthor(currentUser);
//                if (main) {
//                    changeStateGamme();
//                    entityGamme.setPrincipal(true);
//                } else {
//                    entityGamme.setPrincipal(false);
//                }
//                if (!isUpdateGamme()) {
//                    entityGamme = (YvsProdGammeArticle) dao.save1(entityGamme);
//                    bean.setId(entityGamme.getId());
//                    gamme.setId(entityGamme.getId());
//                    bean.setArticle(articles.get(articles.indexOf(bean.getArticle())));
//                    gammes.add(bean);
//                } else {
//                    dao.update(entityGamme);
//                    gammes.set(gammes.indexOf(gamme), bean);
//                }
//                setUpdateGamme(true);
//                succes();
//                update("data_gamme");
//                input_reset = "";
//                return true;
//            }
//        } catch (Exception ex) {
//            getErrorMessage(action + " Impossible !");
//            Logger.getLogger(ManagedGamme.class.getName()).log(Level.SEVERE, null, ex);
//            return false;
//        }
//        return false;
//    }
//
//    private void changeStateGamme() {
//        String rq = "UPDATE yvs_prod_gamme_article SET principal=false WHERE article=?";
//        Options[] op = new Options[]{new Options(gamme.getArticle().getId(), 1)};
//        dao.requeteLibre(rq, op);
//    }
//
//    @Override
//    public void deleteBean() {
//        try {
//            if ((tabIds != null) ? tabIds.length() > 0 : false) {
//                String[] ids = tabIds.split("-");
//                if ((ids != null) ? ids.length > 0 : false) {
//                    for (String s : ids) {
//                        int id = Integer.valueOf(s);
//                        GammeArticle bean = gammes.get(id);
//                        dao.delete(new YvsProdGammeArticle(bean.getId()));
//                        gammes.remove(bean);
//                    }
//                    resetFiche();
//                    succes();
//                    update("data_gamme");
//                }
//            }
//        } catch (NumberFormatException ex) {
//            getErrorMessage("Suppression Impossible !");
//            System.err.println("error suppression gamme article : " + ex.getMessage());
//        }
//    }
//
//    @Override
//    public void updateBean() {
////        if ((tabIds != null) ? tabIds.length() > 0 : false) {
////            String[] ids = tabIds.split("-");
////            setUpdateGamme((ids != null) ? ids.length > 0 : false);
////            if (isUpdateGamme()) {
////                int id = Integer.valueOf(ids[ids.length - 1]);
////                GammeArticle bean = gammes.get(id);
////                populateView(bean);
////                update("blog_form_gamme");
////            }
////        }
//    }
//
//    public void openGammeForUpdate(SelectEvent ev) {
//        populateView((GammeArticle) ev.getObject());
//        update("blog_form_gamme");
//    }
//
//    @Override
//    public void populateView(GammeArticle bean) {
//        cloneObject(gamme, bean);
//        entityGamme = buildGammeArticle(bean);
//        setUpdateGamme(true);
//    }
//
//    @Override
//    public void resetFiche() {
//        resetFiche(gamme);
//        gamme.setArticle(new Articles());
//        gamme.getOperations().clear();
//        setUpdateGamme(false);
//        tabIds = "";
////        resetPage();
//    }
//
//    @Override
//    public void resetPage() {
//        for (GammeArticle g : gammes) {
//            g.setSelectActif(false);
//        }
//    }
//
//    @Override
//    public void selectOnView(GammeArticle bean) {
//        if ((bean != null) ? bean.getId() != 0 : false) {
//            System.err.println("bean : " + bean.getReference());
//        }
//    }
//
//    public YvsProdOperationsGamme buildPhaseGamme(OperationsGamme y) {
//        YvsProdOperationsGamme p = new YvsProdOperationsGamme();
//        if (y != null) {
//            p.setId(y.getId());
//            p.setDescription(y.getDescription());
//            p.setNumero(y.getNumero());
//            p.setPhase(formPhase);
//            p.setGammeArticle(entityGamme);
//        }
//        return p;
//    }
//
//    public OperationsGamme recopieViewPhase() {
//        OperationsGamme p = new OperationsGamme();
//        p.setId(phase.getId());
//        p.setDescription(phase.getDescription());
//        p.setReference(phase.getReference());
//        p.setNumero(phase.getNumero());
//        return p;
//    }
//
//    public OperationsGamme recopieViewOperation() {
//        OperationsGamme p = new OperationsGamme();
//        p.setId(operation.getId());
//        p.setDescription(operation.getDescription());
//         p.setReference(operation.getReference());
//         p.setNumero(operation.getNumero());
//         return p;
//    }
//
//    public boolean controleFichePhase(OperationsGamme bean) {        
//        if (bean.getNumero() <= 0) {
//            getErrorMessage("Veuillez préciser un numéro de phase valide !");
//            return false;
//        }
//        return true;
//    }
//
//    public void resetFichePhase() {
//        resetFiche(phase);
//        phase.setDocuments(new ArrayList<YvsProdDocumentTechnique>());
//        phase.setIndicateurs(new ArrayList<YvsProdIndicateurOp>());
//        phase.setPostes(new ArrayList<YvsProdPosteCharge>());
//        tabIds_phase = "";
//        setUpdatePhase(false);
//        resetPagePhase();
//    }
//
//    public void resetPagePhase() {
////        for (OperationsGamme p : gamme.getPhases()) {
////            p.setSelectActif(false);
////        }
//
//    }
//
//    public void populateViewPhase(OperationsGamme bean) {
//        cloneObject(phase, bean);
//        entityPhase = buildPhaseGamme(bean);
//        setUpdatePhase(true);
//        update("blog_form_phase_poste");
//        update("data_phase_poste");
//
//        update("blog_form_phase_document");
//        update("data_phase_document");
//
//        update("blog_form_phase_indicateur");
//        update("data_phase_indicateur");
//    }
//
//    private boolean updateOperation;
//    private boolean isPropertyPhase;    //permet de gérér aussi la modification d'une phase
//
//    public void saveNewPhase(boolean reset) {
//        if (isUpdateGamme()) {
//            String action = isUpdatePhase() ? "Modification" : "Insertion";
//            try {
//                OperationsGamme bean = recopieViewPhase();
//                if (controleFichePhase(bean)) {
//                    entityPhase = buildPhaseGamme(bean);
//                    entityPhase.setAuthor(currentUser);
//                    if (formPhase) {  //s'il s'agit de la manipulation d'une phase
//                        if (!isUpdatePhase()) {
//                            entityPhase = (YvsProdOperationsGamme) dao.save1(entityPhase);
//                            bean.setId(entityPhase.getId());
//                            phase.setId(entityPhase.getId());
////                            gamme.getPhases().add(bean);
//                        } else {
//                            dao.update(entityPhase);
////                            gamme.getPhases().set(gamme.getPhases().indexOf(phase), bean);
//                        }
//                        if (reset && !isPropertyPhase) {
//                            phase = new OperationsGamme();
//                            updatePhase = false;
//                        } else {
//                            updatePhase = true;
//                        }
//                    } else {  //s'il s'agit de l'enregistrement d'une opération d'une phase
//                        if (!updateOperation) {
//                            entityPhase.setParent(new YvsProdOperationsGamme(parent.getId()));
//                            entityPhase = (YvsProdOperationsGamme) dao.save1(entityPhase);
//                            bean.setId(entityPhase.getId());
//                            phase.setId(entityPhase.getId());
//                            int idxP = gamme.getOperations().indexOf(parent);
//                            if (idxP >= 0) {
////                                gamme.getPhases().get(idxP).getOperations().add(bean);
//                            }
//                            updateOperation = true;
//                        } else {
//                            entityPhase.setParent(new YvsProdOperationsGamme(parent.getId()));
//                            dao.update(entityPhase);
//                            int idxP = gamme.getOperations().indexOf(parent);
//                            if (idxP >= 0) {
//                                int idxOp = gamme.getOperations().get(idxP).getOperations().indexOf(bean);
////                                gamme.getPhases().get(idxP).getOperations().set(idxOp, bean);
//                            }
//                            phase = new OperationsGamme();
//                            updateOperation = false;
//                        }
//                    }
//                    update("data_gamme_phase");
//
////                    txt_reslut = "true";
////                    update("txt_result");
//                }
//            } catch (Exception ex) {
//                Logger.getLogger(ManagedGamme.class.getName()).log(Level.SEVERE, null, ex);
//                getErrorMessage(action + " Impossible !");
//            }
//        } else {
//            getErrorMessage("Vous devez enregistrer ou modifier une gamme");
//            txt_reslut = "false";
//        }
//    }
//
//    public void saveNeOperation(boolean reset) {
//        if (isUpdateGamme()) {
//            String action = isUpdatePhase() ? "Modification" : "Insertion";
//            try {
//                OperationsGamme bean = recopieViewOperation();
//                if (controleFichePhase(bean)) {
//                    entityPhase = buildPhaseGamme(bean);
//                    entityPhase.setAuthor(currentUser);
//                    //s'il s'agit de l'enregistrement d'une opération d'une phase
//                    if (!updateOperation) {
//                        entityPhase.setParent(new YvsProdOperationsGamme(phase.getId()));
//                        entityPhase = (YvsProdOperationsGamme) dao.save1(entityPhase);
//                        bean.setId(entityPhase.getId());
//                        phase.getOperations().add(bean);
//                        operation.setId(bean.getId());
//                    } else {
//                        entityPhase.setParent(new YvsProdOperationsGamme(phase.getId()));
//                        dao.update(entityPhase);
//                        int idxP = gamme.getOperations().indexOf(parent);
//                        if (idxP >= 0) {
//                            int idxOp = gamme.getOperations().get(idxP).getOperations().indexOf(operation);
////                            gamme.getPhases().get(idxP).getOperations().set(idxOp, operation);
//                        }
//                    }
//                    operation = new OperationsGamme();
//                    updateOperation = false;
//                    update("row_expension_op");
//                    txt_reslut = "true";
//                    update("form_add_operation");
//                }
//            } catch (Exception ex) {
//                Logger.getLogger(ManagedGamme.class.getName()).log(Level.SEVERE, null, ex);
//                getErrorMessage(action + " Impossible !");
//            }
//        } else {
//            getErrorMessage("Vous devez enregistrer ou modifier une gamme");
//            txt_reslut = "false";
//        }
//    }
//
//    public void selectOneOperation(SelectEvent ev) {
//        OperationsGamme op = (OperationsGamme) ev.getObject();
//        cloneObject(operation, op);
//        operation.setUniteDuree(unites.get(unites.indexOf(op.getUniteDuree())));
//        updateOperation = true;
//        update("form_add_operation");
//    }
//
//    public void deleteBeanPhase() {
//        try {
//            if ((tabIds_phase != null) ? tabIds_phase.length() > 0 : false) {
//                String[] ids = tabIds_phase.split("-");
//                if ((ids != null) ? ids.length > 0 : false) {
//                    for (String s : ids) {
//                        int id = Integer.valueOf(s);
////                        OperationsGamme bean = gamme.getPhases().get(id);
////                        dao.delete(new YvsProdOperationsGamme(bean.getId()));
////                        gamme.getPhases().remove(bean);
//                    }
//                    resetFichePhase();
//                    succes();
//                    update("form_gamme_phase");
//                }
//            }
//        } catch (NumberFormatException ex) {
//            getErrorMessage("Suppression Impossible !");
//            System.err.println("error suppression gamme article : " + ex.getMessage());
//        }
//    }
//
//    public void deleteBeanPhase(OperationsGamme bean) {
//        try {
//            dao.delete(new YvsProdOperationsGamme(bean.getId()));
//            gamme.getOperations().remove(bean);
//            phase.getOperations().remove(bean);
//            resetFichePhase();
//            succes();
//            update("table_operationsGamme");
//        } catch (NumberFormatException ex) {
//            getErrorMessage("Suppression Impossible !");
//            Logger.getLogger(ManagedGamme.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void updateBeanPhase() {
//
//        update("blog_form_phase");
//    }
//
//    List<OperationsGamme> tabPhase = new ArrayList<>();
//
//    public void selectOnViewPhase(OperationsGamme bean) {
//        parent = bean;
//        if (bean.isSelectActif()) {
//            if (!tabPhase.contains(bean)) {
//                tabPhase.add(bean);
//            }
//        } else {
//            if (tabPhase.contains(bean)) {
//                tabPhase.remove(bean);
//            }
//        }
//        if (!tabPhase.isEmpty()) {
//            populateViewPhase(tabPhase.get(tabPhase.size() - 1));
//        } else {
//            resetFichePhase();
//        }
//        update("blog_form_phase");
//    }
//
//    public void selectOnViewPhase() {
//        tabPhase.clear();
//        if (isAllPoste()) {
////            tabPhase.addAll(gamme.getPhases());
//        }
//        if (!tabPhase.isEmpty()) {
//            populateViewPhase(tabPhase.get(tabPhase.size() - 1));
//        } else {
//            resetFichePhase();
//        }
//        update("blog_form_phase");
//    }
//
//    public YvsProdPostePhase buildPostePhase(PostePhase y) {
//        YvsProdPostePhase p = new YvsProdPostePhase();
//        if (y != null) {
//            p.setId(y.getId());
//            p.setCapaciteH(y.getCapacite_h());
//            p.setCapaciteQ(y.getCapacite_q());
//            p.setMainOeuvreH(y.getMods_h());
//            p.setMainOeuvreQ(y.getMods_q());
//            p.setNiveau(y.getNiveau());
//            p.setNombre(y.getNombre());
//            p.setTempsRebus(y.getTemps_rebus());
//            p.setPourcentageUtilisation(y.getUtilisation());
//            if ((y.getPoste() != null) ? y.getPoste().getId() != 0 : false) {
//                p.setPosteCharge(new YvsProdPosteCharge(y.getPoste().getId()));
//            }
//            p.setPhaseGamme(new YvsProdOperationsGamme(phase.getId()));
//        }
//        return p;
//    }
//
//    public PostePhase recopieViewPostePhase() {
//        PostePhase p = new PostePhase();
//        p.setId(poste.getId());
//        p.setCapacite_h(poste.getCapacite_h());
//        p.setCapacite_q(poste.getCapacite_q());
//        p.setMods_h(poste.getMods_h());
//        p.setMods_q(poste.getMods_q());
//        p.setNiveau(poste.getNiveau());
//        p.setNombre(poste.getNombre());
//        p.setTemps_rebus(poste.getTemps_rebus());
//        p.setUtilisation(poste.getUtilisation());
//        p.setPoste(poste.getPoste());
//        return p;
//    }
//
//    public boolean controleFichePostePhase(PostePhase bean) {
//        if ((bean.getPoste() != null) ? bean.getPoste().getId() == 0 : true) {
//            getErrorMessage("Vous devez specifier le poste de charge");
//            return false;
//        }
//        //controle que le poste n'existe pas déjà dans la liste des poste associé
//        for (PostePhase p : phase.getPostes()) {
//            if (p.getPoste().equals(bean.getPoste())) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public void populateViewPostePhase(PostePhase bean) {
//        cloneObject(poste, bean);
//        setUpdatePoste(true);
//    }
//
//    public void resetFichePostePhase() {
//        resetFiche(poste);
//        poste.setPoste(new PosteCharge());
//        tabIds_poste = "";
//        resetPagePostePhase();
//        setUpdatePoste(false);
//    }
//
//    public void resetPagePostePhase() {
//        for (PostePhase p : phase.getPostes()) {
//            p.setSelectActif(false);
//        }
//    }
//
//    public void saveNewPostePhase() {
//        if (isUpdatePhase()) {  //on est dans ce cas uniquement si on modifie la phase           
//            String action = isUpdatePoste() ? "Modification" : "Insertion";
//            try {
//                PostePhase bean = recopieViewPostePhase();
//                bean.setPoste(postes.get(postes.indexOf(bean.getPoste())));
//                if (controleFichePostePhase(bean)) {
//                    YvsProdPostePhase entity = buildPostePhase(bean);
//                    entity.setUsers(currentUser);
//                    if (!updatePoste) {
//                        entity = (YvsProdPostePhase) dao.save1(entity);
//                        bean.setId(entity.getId());
//                        poste.setId(entity.getId());
//                        phase.getPostes().add(bean);
//                        if (formPhase) {
////                            gamme.getPhases().set(gamme.getPhases().indexOf(phase), phase);
//                        } else {
//                            int idxPh = gamme.getOperations().indexOf(parent);
//                            int idxOp = gamme.getOperations().get(idxPh).getOperations().indexOf(phase);  //le bean phase à ce niveau contient les données de l'opération
////                            gamme.getPhases().get(idxPh).getOperations().set(idxOp, phase);
//                        }
//                    } else {
//                        dao.update(entity);
//                        phase.getPostes().set(phase.getPostes().indexOf(poste), bean);
//                        if (formPhase) {
////                            gamme.getPhases().set(gamme.getPhases().indexOf(phase), phase);
//                        } else {
//                            int idxOp = gamme.getOperations().get(gamme.getOperations().indexOf(parent)).getOperations().indexOf(phase);
////                            gamme.getPhases().get(gamme.getPhases().indexOf(parent)).getOperations().set(idxOp, phase);
//                        }
//                    }
//                    updatePoste = false;
//                    poste = new PostePhase();
//                    succes();
//                    update("data_phase_poste");
//                    update("data_gamme_phase");
//                }
//            } catch (Exception ex) {
//                getErrorMessage(action + " Impossible !");
//                Logger.getLogger(ManagedGamme.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else {
//            getErrorMessage("Vous devez enregsitrer ou modifier une phase");
//        }
//    }
//
//    public void deleteBeanPostePhase() {
//        try {
//            if ((tabIds_poste != null) ? tabIds_poste.length() > 0 : false) {
//                String[] ids = tabIds_poste.split("-");
//                if ((ids != null) ? ids.length > 0 : false) {
//                    for (String s : ids) {
//                        int id = Integer.valueOf(s);
//                        PostePhase bean = phase.getPostes().get(id);
//                        dao.delete(new YvsProdPostePhase(bean.getId()));
//                        phase.getPostes().remove(bean);
//                    }
//                    resetFichePostePhase();
//                    succes();
//                    update("form_gamme_phase_poste");
//                }
//            }
//        } catch (NumberFormatException ex) {
//            getErrorMessage("Suppression Impossible !");
//            Logger.getLogger(ManagedGamme.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    List<PostePhase> tabPostePhase = new ArrayList<>();
//
//    public void selectOnViewPostePhase(PostePhase bean) {
//        if (bean.isSelectActif()) {
//            if (!tabPostePhase.contains(bean)) {
//                tabPostePhase.add(bean);
//            }
//        } else {
//            if (tabPostePhase.contains(bean)) {
//                tabPostePhase.remove(bean);
//            }
//        }
//        if (!tabPostePhase.isEmpty()) {
//            populateViewPostePhase(tabPostePhase.get(tabPostePhase.size() - 1));
//        } else {
//            resetFichePostePhase();
//        }
//        update("blog_form_phase_poste");
//    }
//
//    public void selectOnViewPostePhase() {
//        tabPostePhase.clear();
//        if (isAllPoste()) {
//            tabPostePhase.addAll(phase.getPostes());
//        }
//        if (!tabPostePhase.isEmpty()) {
//            populateViewPostePhase(tabPostePhase.get(tabPostePhase.size() - 1));
//        } else {
//            resetFichePostePhase();
//        }
//        update("blog_form_phase_poste");
//    }
//
//    public YvsProdDocumentTechnique buildDocumentTechnique(DocumentTechnique y) {
//        YvsProdDocumentTechnique d = new YvsProdDocumentTechnique();
//        if (y != null) {
//            d.setId(y.getId());
//            d.setDescription(y.getDescription());
//            d.setDesignation(y.getDesignation());
//            d.setFichier(y.getFichier());
//            d.setReference(y.getReference());
//            d.setPhaseGamme(entityPhase);
//        }
//        return d;
//    }
//
//    public DocumentTechnique recopieViewDocumentTechnique() {
//        DocumentTechnique d = new DocumentTechnique();
//        d.setId(document.getId());
//        d.setDescription(document.getDescription());
//        d.setDesignation(document.getDesignation());
//        d.setFichier(document.getFichier());
//        d.setReference(document.getReference());
//        return d;
//    }
//
//    public boolean controleFicheDocument(DocumentTechnique bean) {
//        if (bean.getDesignation() == null || "".equals(bean.getDesignation())) {
//            getErrorMessage("Vous devez entret la designation");
//            return false;
//        }
//        return true;
//    }
//
//    public void populateViewDocument(DocumentTechnique bean) {
//        cloneObject(document, bean);
//        setUpdateDocument(true);
//    }
//
//    public void resetFicheDocument() {
//        resetFiche(document);
//        setUpdateDocument(false);
//        resetPageDocument();
//        tabIds_document = "";
//    }
//
//    public void resetPageDocument() {
//        for (DocumentTechnique d : phase.getDocuments()) {
//            d.setSelectActif(false);
//        }
//    }
//
//    public void saveNewDocument() {
//        if (isUpdatePhase()) {
//            if (input_reset_document.equals("reset")) {
//                setUpdateDocument(false);
//                input_reset_document = "";
//            }
//            String action = isUpdateDocument() ? "Modification" : "Insertion";
//            try {
//                DocumentTechnique bean = recopieViewDocumentTechnique();
//                if (controleFicheDocument(bean)) {
//                    YvsProdDocumentTechnique entity = buildDocumentTechnique(bean);
//                    if (!isUpdateDocument()) {
//                        entity = (YvsProdDocumentTechnique) dao.save1(entity);
//                        bean.setId(entity.getId());
//                        document.setId(entity.getId());
//                        phase.getDocuments().add(bean);
//                    } else {
//                        dao.update(entity);
//                        phase.getDocuments().set(phase.getDocuments().indexOf(document), bean);
//                    }
//                    setUpdateDocument(true);
//                    succes();
//                    update("data_phase_document");
//                }
//            } catch (Exception ex) {
//                getErrorMessage(action + " Impossible !");
//                System.err.println("error " + action + " : " + ex.getMessage());
//            }
//        } else {
//            getErrorMessage("Vous devez enregsitrer ou modifier une phase");
//        }
//    }
//
//    public void deleteBeanDocument() {
//        try {
//            if ((tabIds_document != null) ? tabIds_document.length() > 0 : false) {
//                String[] ids = tabIds_document.split("-");
//                if ((ids != null) ? ids.length > 0 : false) {
//                    for (String s : ids) {
//                        int id = Integer.valueOf(s);
//                        DocumentTechnique bean = phase.getDocuments().get(id);
//                        dao.delete(new YvsProdDocumentTechnique(bean.getId()));
//                        phase.getDocuments().remove(bean);
//                    }
//                    resetFicheDocument();
//                    succes();
//                    update("form_gamme_phase_document");
//                }
//            }
//        } catch (NumberFormatException ex) {
//            getErrorMessage("Suppression Impossible !");
//            System.err.println("error suppression poste phase : " + ex.getMessage());
//        }
//    }
//
//    List<DocumentTechnique> tabDocumentTechnique = new ArrayList<>();
//
//    public void selectOnViewDocument(DocumentTechnique bean) {
//        if (bean.isSelectActif()) {
//            if (!tabDocumentTechnique.contains(bean)) {
//                tabDocumentTechnique.add(bean);
//            }
//        } else {
//            if (tabDocumentTechnique.contains(bean)) {
//                tabDocumentTechnique.remove(bean);
//            }
//        }
//        if (!tabDocumentTechnique.isEmpty()) {
//            populateViewDocument(tabDocumentTechnique.get(tabDocumentTechnique.size() - 1));
//        } else {
//            resetFicheDocument();
//        }
//        update("blog_form_phase_document");
//    }
//
//    public void selectOnViewDocument() {
//        tabDocumentTechnique.clear();
//        if (isAllDocument()) {
//            tabDocumentTechnique.addAll(phase.getDocuments());
//        }
//        if (!tabDocumentTechnique.isEmpty()) {
//            populateViewDocument(tabDocumentTechnique.get(tabDocumentTechnique.size() - 1));
//        } else {
//            resetFicheDocument();
//        }
//        update("blog_form_phase_document");
//    }
//
//    public void fileUploadDocument(FileUploadEvent ev) {
//        if ((ev != null) ? ev.getFile() != null : false) {
//            document.setFichier(ev.getFile().getFileName());
//            update("txt_value_file_document");
//            succes();
//        }
//    }
//
//    public YvsBaseIndicateurReussite buildIndicateurReussite(IndicateurReussite y) {
//        YvsBaseIndicateurReussite i = new YvsBaseIndicateurReussite();
//        if (y != null) {
//            i.setId(y.getId());
//            i.setDescription(y.getDescription());
//            i.setDesignation(y.getDesignation());
//            i.setType(y.getType());
//            i.setValeur(y.getValeur());
//        }
//        return i;
//    }
//
//    public IndicateurReussite recopieViewIndicateurReussite() {
//        IndicateurReussite i = new IndicateurReussite();
//        i.setId(reussite.getId());
//        i.setDescription(reussite.getDescription());
//        i.setDesignation(reussite.getDesignation());
//        i.setType(reussite.getType());
//        i.setValeur(reussite.getValeur());
//        return i;
//    }
//
//    public boolean controleFicheIndicateurReussite(IndicateurReussite bean) {
//        if (bean.getDesignation() == null || "".equals(bean.getDesignation())) {
//            getErrorMessage("Vous devez entrer la designation");
//            return false;
//        }
//        return true;
//    }
//
//    public void saveNewIndicateurReussite() {
//        try {
//            IndicateurReussite bean = recopieViewIndicateurReussite();
//            if (controleFicheIndicateurReussite(bean)) {
//                YvsBaseIndicateurReussite entity = buildIndicateurReussite(bean);
//                entity = (YvsBaseIndicateurReussite) dao.save1(entity);
//                bean.setId(entity.getId());
//                reussite.setId(entity.getId());
//                indicateurs.add(bean);
//                indicateur.setIndicateurReussite(bean);
//                succes();
//                update("gamme_indic_reussite");
//            }
//        } catch (NumberFormatException ex) {
//            getErrorMessage("Insertion Impossible !");
//            System.err.println("error insertion poste phase : " + ex.getMessage());
//        }
//    }
//
//    public YvsProdIndicateurOp buildIndicateurPhase(IndicateurPhase y) {
//        YvsProdIndicateurOp i = new YvsProdIndicateurOp();
//        if (y != null) {
//            i.setId(y.getId());
//            i.setDescription(y.getDescription());
//            i.setReference(y.getReference());
//            i.setValeur(y.getValeur());
//            if ((y.getIndicateurReussite() != null) ? y.getIndicateurReussite().getId() != 0 : false) {
//                i.setIndicateurReussite(new YvsBaseIndicateurReussite(y.getIndicateurReussite().getId()));
//            }
//            i.setPhaseGamme(entityPhase);
//        }
//        return i;
//    }
//
//    public IndicateurPhase recopieViewIndicateurPhase() {
//        IndicateurPhase i = new IndicateurPhase();
//        i.setId(indicateur.getId());
//        i.setDescription(indicateur.getDescription());
//        i.setReference(indicateur.getReference());
//        i.setValeur(indicateur.getValeur());
//        indicateur.setIndicateurReussite(indicateurs.get(indicateurs.indexOf(indicateur.getIndicateurReussite())));
//        i.setIndicateurReussite(indicateur.getIndicateurReussite());
//        return i;
//    }
//
//    public boolean controleFicheIndicateurPhase(IndicateurPhase bean) {
//        if ((bean.getIndicateurReussite() != null) ? bean.getIndicateurReussite().getId() == 0 : true) {
//            getErrorMessage("Vous devez specifier l'indicateur");
//            return false;
//        }
//        return true;
//    }
//
//    public void populateViewIndicateur(IndicateurPhase bean) {
//        cloneObject(indicateur, bean);
//        setUpdateIndicateur(true);
//    }
//
//    public void resetFicheIndicateur() {
//        resetFiche(indicateur);
//        indicateur.setIndicateurReussite(new IndicateurReussite());
//        setUpdateIndicateur(false);
//        tabIds_indicateur = "";
//        resetPageIndicateur();
//    }
//
//    public void resetPageIndicateur() {
//        for (IndicateurPhase i : phase.getIndicateurs()) {
//            i.setSelectActif(false);
//        }
//    }
//
//    public void saveNewIndicateur() {
//        if (isUpdatePhase()) {
//            if (input_reset_indicateur.equals("reset")) {
//                setUpdateIndicateur(false);
//                input_reset_indicateur = "";
//            }
//            String action = isUpdateIndicateur() ? "Modification" : "Insertion";
//            try {
//                IndicateurPhase bean = recopieViewIndicateurPhase();
//                if (controleFicheIndicateurPhase(bean)) {
//                    YvsProdIndicateurOp entity = buildIndicateurPhase(bean);
//                    if (!isUpdateIndicateur()) {
//                        entity = (YvsProdIndicateurOp) dao.save1(entity);
//                        bean.setId(entity.getId());
//                        indicateur.setId(entity.getId());
//                        phase.getIndicateurs().add(bean);
//                    } else {
//                        dao.update(entity);
//                        phase.getIndicateurs().set(phase.getIndicateurs().indexOf(indicateur), bean);
//                    }
//                    setUpdateIndicateur(true);
//                    succes();
//                    update("data_phase_indicateur");
//                }
//            } catch (Exception ex) {
//                getErrorMessage(action + " Impossible !");
//                System.err.println("error " + action + " : " + ex.getMessage());
//            }
//        } else {
//            getErrorMessage("Vous devez enregsitrer ou modifier une phase");
//        }
//    }
//
//    public void deleteBeanIndicateur() {
//        try {
//            if ((tabIds_indicateur != null) ? tabIds_indicateur.length() > 0 : false) {
//                String[] ids = tabIds_indicateur.split("-");
//                if ((ids != null) ? ids.length > 0 : false) {
//                    for (String s : ids) {
//                        int id = Integer.valueOf(s);
//                        IndicateurPhase bean = phase.getIndicateurs().get(id);
//                        dao.delete(new YvsProdIndicateurOp(bean.getId()));
//                        phase.getIndicateurs().remove(bean);
//                    }
//                    resetFicheIndicateur();
//                    succes();
//                    update("form_gamme_phase_indicateur");
//                }
//            }
//        } catch (NumberFormatException ex) {
//            getErrorMessage("Suppression Impossible !");
//            System.err.println("error suppression poste phase : " + ex.getMessage());
//        }
//    }
//
//    List<IndicateurPhase> tabIndicateurPhase = new ArrayList<>();
//
//    public void selectOnViewIndicateur(IndicateurPhase bean) {
//        if (bean.isSelectActif()) {
//            if (!tabIndicateurPhase.contains(bean)) {
//                tabIndicateurPhase.add(bean);
//            }
//        } else {
//            if (tabIndicateurPhase.contains(bean)) {
//                tabIndicateurPhase.remove(bean);
//            }
//        }
//        if (!tabIndicateurPhase.isEmpty()) {
//            populateViewIndicateur(tabIndicateurPhase.get(tabIndicateurPhase.size() - 1));
//        } else {
//            resetFicheIndicateur();
//        }
//        update("blog_form_phase_indicateur");
//    }
//
//    public void selectOnViewIndicateur() {
//        tabIndicateurPhase.clear();
//        if (isAllIndicateur()) {
//            tabIndicateurPhase.addAll(phase.getIndicateurs());
//        }
//        if (!tabIndicateurPhase.isEmpty()) {
//            populateViewIndicateur(tabIndicateurPhase.get(tabIndicateurPhase.size() - 1));
//        } else {
//            resetFicheIndicateur();
//        }
//        update("blog_form_phase_indicateur");
//    }
//
//    @Override
//    public void loadOnView(SelectEvent ev) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void unLoadOnView(UnselectEvent ev) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public void openProprietesPhases(OperationsGamme ph, boolean addOperation) {
//        if (ph == null) {
//            phase = new OperationsGamme();
//            formPhase = true;
//            isPropertyPhase = false;
//            updatePhase = false;
//            openDialog("dlgAddPhase");
//        } else {
//            parent = ph;
//            if (addOperation) {
//                phase = new OperationsGamme();
//                formPhase = false; //précise qu'on veux modifier
//                openDialog("dlgAddPhase");
//            } else {
//                cloneObject(phase, ph);
//                formPhase = true; //précise qu'on veux enregistrer les opérations de la phase
//                isPropertyPhase = updatePhase = true;   //cette ligne signifie qu'on peut modifier les propriétés de la phase                
//                openDialog("dlgAddPhase");
//            }
//        }
//        update("blog_form_phase");
//        update("header_dlg_phase");
//    }
//
//    public void openProprietesOperation(OperationsGamme parent_, OperationsGamme op, boolean addOperation) {
//        parent = parent_;
//        phase = new OperationsGamme();
//        cloneObject(phase, op);
//        System.err.println("---- Poste " + op.getPostes().size());
//        formPhase = false; //précise qu'on veux enregistrer les opérations de la phase
//        isPropertyPhase = updatePhase = false;   //cette ligne signifie qu'on peut modifier les propriétés de la phase                
//        openDialog("dlgAddPhase");
//        update("blog_form_phase");
//        update("header_dlg_phase");
//        updatePhase = true;
//    }
//
//}
