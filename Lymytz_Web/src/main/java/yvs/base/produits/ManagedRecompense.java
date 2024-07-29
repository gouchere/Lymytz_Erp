///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.base.produits;
//
//import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.faces.application.FacesMessage;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.SessionScoped;
//import javax.faces.event.ValueChangeEvent;
//import org.primefaces.context.RequestContext;
//import org.primefaces.event.SelectEvent;
//import org.primefaces.event.TabChangeEvent;
//import org.primefaces.event.UnselectEvent;
//import yvs.entity.produits.*;
//import yvs.entity.produits.group.YvsBorneTranches;
//import yvs.entity.produits.group.YvsTranches;
//import yvs.util.*;
//
///**
// *
// * @author GOUCHERE YVES
// */
//@ManagedBean(name = "Mrecompense")
//@SessionScoped
//public class ManagedRecompense extends Managed<Recompenses, YvsBaseCaisse> implements Serializable {
//
//    @ManagedProperty(value = "#{recompense}")
//    private Recompenses recompense;
//    private Recompenses selectRecompense;
//    @ManagedProperty(value = "#{artRecompense}")
//    private ArticleRecompense artRec;
//    private ArticleRecompense selectArticleRec;
//    private List<Recompenses> listRecompense;
//    private List<Recompenses> listComission;
//    private List<Recompenses> listFiltre;
//    private Options[] natures;
//    private boolean tranche;
//    private List<BorneTranche> listBorneTranche;
//    private BorneTranche selectBorne;
//    private String modelTranche;
//    private double borne;
//    private double taux;
//    private double valeur;
//    private boolean renderOnglet;
//    private String defaultButon = "P-rec-save";
//    private Options[] durrees;
//    private Options[] objectifs;
//    private Options[] bases;
//    private boolean disableDateDebut;
//    private boolean disableDateFin;
//    private List<ArticleRecompense> listArticle;
//
//    public ManagedRecompense() {
//        listRecompense = new ArrayList<>();
//        listArticle = new ArrayList<>();
//        listBorneTranche = new ArrayList<>();
//        listComission = new ArrayList<>();
//        natures = new Options[]{
//            new Options(Constantes.NAT_RISTOURNE, Constantes.NAT_RISTOURNE),
//            new Options(Constantes.NAT_COMISSION, Constantes.NAT_COMISSION)
//        };
//        durrees = new Options[]{
//            new Options(Constantes.DUREE_A_PARTIR, Constantes.DUREE_A_PARTIR),
//            new Options(Constantes.DUREE_JUSQUA, Constantes.DUREE_JUSQUA),
//            new Options(Constantes.DUREE_PENDANT, Constantes.DUREE_PENDANT),
//            new Options(Constantes.DUREE_LE, Constantes.DUREE_LE),
//            new Options(Constantes.DUREE_PERMANENCE, Constantes.DUREE_PERMANENCE)
//        };
//        objectifs = new Options[]{
//            new Options(Constantes.OBJECTIF_QUANTITE, Constantes.OBJECTIF_QUANTITE),
//            new Options(Constantes.OBJECTIF_VALEUR, Constantes.OBJECTIF_VALEUR)
//        };
//        bases = new Options[]{
//            new Options(Constantes.BASE_CAHT, Constantes.BASE_CAHT),
//            new Options(Constantes.BASE_MARGE, Constantes.BASE_MARGE)
//        };
//    }
//
//    public String getDefaultButon() {
//        return defaultButon;
//    }
//
//    public void setDefaultButon(String defaultButon) {
//        this.defaultButon = defaultButon;
//    }
//
//    public List<Recompenses> getListFiltre() {
//        return listFiltre;
//    }
//
//    public void setListFiltre(List<Recompenses> listFiltre) {
//        this.listFiltre = listFiltre;
//    }
//
//    public List<Recompenses> getListRecompense() {
//        return listRecompense;
//    }
//
//    public void setListRecompense(List<Recompenses> listRecompense) {
//        this.listRecompense = listRecompense;
//    }
//
//    public Recompenses getRecompense() {
//        return recompense;
//    }
//
//    public void setRecompense(Recompenses recompense) {
//        this.recompense = recompense;
//    }
//
//    public Options[] getNatures() {
//        return natures;
//    }
//
//    public void setNatures(Options[] natures) {
//        this.natures = natures;
//    }
//
//    public ArticleRecompense getArtRec() {
//        return artRec;
//    }
//
//    public void setArtRec(ArticleRecompense artRec) {
//        this.artRec = artRec;
//    }
//
//    public List<BorneTranche> getListBorneTranche() {
//        return listBorneTranche;
//    }
//
//    public void setListBorneTranche(List<BorneTranche> listBorneTranche) {
//        this.listBorneTranche = listBorneTranche;
//    }
//
//    public BorneTranche getSelectBorne() {
//        return selectBorne;
//    }
//
//    public void setSelectBorne(BorneTranche selectBorne) {
//        this.selectBorne = selectBorne;
//    }
//
//    public boolean isTranche() {
//        return tranche;
//    }
//
//    public void setTranche(boolean tranche) {
//        this.tranche = tranche;
//    }
//
//    public String getModelTranche() {
//        return modelTranche;
//    }
//
//    public void setModelTranche(String modelTranche) {
//        this.modelTranche = modelTranche;
//    }
//
//    public Recompenses getSelectRecompense() {
//        return selectRecompense;
//    }
//
//    public void setSelectRecompense(Recompenses selectRecompense) {
//        this.selectRecompense = selectRecompense;
//    }
//
//    public double getBorne() {
//        return borne;
//    }
//
//    public void setBorne(double borne) {
//        this.borne = borne;
//    }
//
//    public double getTaux() {
//        return taux;
//    }
//
//    public void setTaux(double taux) {
//        this.taux = taux;
//    }
//
//    public double getValeur() {
//        return valeur;
//    }
//
//    public void setValeur(double valeur) {
//        this.valeur = valeur;
//    }
//
//    public boolean isRenderOnglet() {
//        return renderOnglet;
//    }
//
//    public void setRenderOnglet(boolean renderOnglet) {
//        this.renderOnglet = renderOnglet;
//    }
//
//    public Options[] getBases() {
//        return bases;
//    }
//
//    public void setBases(Options[] bases) {
//        this.bases = bases;
//    }
//
//    public Options[] getDurrees() {
//        return durrees;
//    }
//
//    public void setDurrees(Options[] durrees) {
//        this.durrees = durrees;
//    }
//
//    public Options[] getObjectifs() {
//        return objectifs;
//    }
//
//    public void setObjectifs(Options[] objectifs) {
//        this.objectifs = objectifs;
//    }
//
//    public boolean isDisableDateDebut() {
//        return disableDateDebut;
//    }
//
//    public void setDisableDateDebut(boolean disableDateDebut) {
//        this.disableDateDebut = disableDateDebut;
//    }
//
//    public boolean isDisableDateFin() {
//        return disableDateFin;
//    }
//
//    public void setDisableDateFin(boolean disableDateFin) {
//        this.disableDateFin = disableDateFin;
//    }
//
//    public ArticleRecompense getSelectArticleRec() {
//        return selectArticleRec;
//    }
//
//    public void setSelectArticleRec(ArticleRecompense selectArticleRec) {
//        this.selectArticleRec = selectArticleRec;
//    }
//
//    public List<ArticleRecompense> getListArticle() {
//        return listArticle;
//    }
//
//    public void setListArticle(List<ArticleRecompense> listArticle) {
//        this.listArticle = listArticle;
//    }
//
//    public List<Recompenses> getListComission() {
//        return listComission;
//    }
//
//    public void setListComission(List<Recompenses> listComission) {
//        this.listComission = listComission;
//    }
//
//    @Override
//    public boolean controleFiche(Recompenses bean) {
//        return true;
//    }
//
//    @Override
//    public boolean saveNew() {
//        Recompenses re = recopieView();
//        if (controleFiche(re)) {
//            YvsPlanDeRecompense r = buildEntityFromBean(re);
//            r.setId(null);            
//            r = (YvsPlanDeRecompense) dao.save1(r);
//            re.setId(r.getId());
//            recompense.setId(r.getId());
//            listRecompense.add(0, re);
//            setRenderOnglet(true);
//            selectRecompense = re;
//            defaultButon = "P-rec-update";
//            setDisableSave(true);
//            update("rec-vue-group");
//            update("P-rec-main1");
//            update("P-rec-table");
//            succes();
//        }
//        return true;
//    }
//
//    @Override
//    public void updateBean() {
//        Recompenses re = recopieView();
//        if (controleFiche(re)) {
//            YvsPlanDeRecompense r = buildEntityFromBean(re);
//            r.setLastAuthor(getUserOnLine());
//            r.setLastDateSave(new Date());
//            re.setLastAuteur(getUserOnLine());
//            re.setLastDateUpdate(df.format(new Date()));
//            dao.update(r);
//            listRecompense.set(listRecompense.indexOf(re), re);
//            setRenderOnglet(true);
//            defaultButon = "P-rec-update";
//            setDisableSave(true);
//            update("rec-vue-group");
//            update("P-rec-main1");
//            update("P-rec-table");
//            succes();
//        }
//    }
//
//    @Override
//    public Recompenses recopieView() {
//        Recompenses re = new Recompenses();
//        re.setActif(recompense.isActif());
//        re.setDesignation(recompense.getDesignation());
//        re.setNature(recompense.getNature());
//        re.setDuree(recompense.getDuree());
//        re.setDebut(recompense.getDebut());
//        re.setFin(recompense.getFin());
//        re.setObjectif(recompense.getObjectif());
//        re.setBaseCalcul(recompense.getBaseCalcul());
//        if (recompense.getAuteur() != null) {
//            re.setAuteur(recompense.getAuteur());
//        } else {
//            re.setAuteur(getUserOnLine());
//        }
//        if (recompense.getLastAuteur() != null) {
//            re.setLastAuteur(recompense.getLastAuteur());
//        } else {
//            re.setLastAuteur(getUserOnLine());
//        }
//        if (recompense.getLastDateUpdate() != null) {
//            re.setLastDateUpdate(recompense.getLastDateUpdate());
//        } else {
//            re.setLastDateUpdate(df.format(new Date()));
//        }
//        re.setId(recompense.getId());
//        return re;
//    }
//
//    @Override
//    public void populateView(Recompenses bean) {
//        recompense.setActif(bean.isActif());
//        recompense.setDesignation(bean.getDesignation());
//        recompense.setNature(bean.getNature());
//        if (bean.getAuteur() != null) {
//            recompense.setAuteur(bean.getAuteur());
//        } else {
//            recompense.setAuteur(getUserOnLine());
//        }
//        if (bean.getLastAuteur() != null) {
//            recompense.setLastAuteur(bean.getLastAuteur());
//        } else {
//            recompense.setLastAuteur(getUserOnLine());
//        }
//        if (bean.getLastDateUpdate() != null) {
//            recompense.setLastDateUpdate(bean.getLastDateUpdate());
//        } else {
//            recompense.setLastDateUpdate(df.format(new Date()));
//        }
//        recompense.setId(bean.getId());
//        recompense.setListArticle(bean.getListArticle());
//        recompense.setDuree(bean.getDuree());
//        recompense.setDebut(bean.getDebut());
//        recompense.setFin(bean.getFin());
//        recompense.setObjectif(bean.getObjectif());
//        recompense.setBaseCalcul(bean.getBaseCalcul());
//    }
//
//    @Override
//    public void loadOnView(SelectEvent ev) {
//        Recompenses re = (Recompenses) ev.getObject();
//        if (re != null) {
//            populateView(re);
//            defaultButon = "P-rec-update";
//            setDisableSave(true);
//            setRenderOnglet(true);
//            update("rec-vue-group");
//            update("P-rec-main1");
//        }
//    }
//
//    @Override
//    public void loadAll() {
//        listRecompense.clear();
//        String[] ch = {};
//        Object[] val = {};
//        List<YvsPlanDeRecompense> la = dao.loadNameQueries("YvsPlanDeRecompense.findAll", ch, val);
//        for (YvsPlanDeRecompense pre : la) {
//            //récupère la liste des relations GroupeProduit-CategorieTarif contenu dans g
//            Recompenses re = buildBeanFromEntity(pre);
//            listRecompense.add(0, re);
//        }
//    }
//
//    public void loadRistourne() {
//        listRecompense.clear();
//        String[] ch = {"nature"};
//        Object[] val = {Constantes.NAT_RISTOURNE};
//        List<YvsPlanDeRecompense> la = dao.loadNameQueries("YvsPlanDeRecompense.findByNature", ch, val);
//        for (YvsPlanDeRecompense pre : la) {
//            //récupère la liste des relations GroupeProduit-CategorieTarif contenu dans g
//            Recompenses re = new Recompenses(pre.getId(), pre.getDesignation(), pre.getNature());
//            listRecompense.add(0, re);
//        }
//    }
//
//    public void loadComission() {
//        listComission.clear();
//        String[] ch = {"nature"};
//        Object[] val = {Constantes.NAT_COMISSION};
//        List<YvsPlanDeRecompense> la = dao.loadNameQueries("YvsPlanDeRecompense.findByNature", ch, val);
//        for (YvsPlanDeRecompense pre : la) {
//            //récupère la liste des relations GroupeProduit-CategorieTarif contenu dans g
//            Recompenses re = new Recompenses(pre.getId(), pre.getDesignation(), pre.getNature());
//            listComission.add(0, re);
//        }
//    }
//
//    @Override
//    public void resetFiche() {
//        resetFiche(recompense);
//        setDisableSave(false);
//        renderOnglet = false;
//        update("rec-vue-group");
//        update("P-rec-main1");
//    }
//
//    private Recompenses buildBeanFromEntity(YvsPlanDeRecompense re) {
//        Recompenses r = new Recompenses(re.getId(), re.getDesignation(), re.getNature());
//        r.setDuree(re.getDuree());
//        r.setDebut(re.getDebut());
//        r.setFin(re.getFin());
//        r.setObjectif(re.getObjectif());
//        r.setBaseCalcul(re.getBaseCalcul());
//        r.setActif(re.getActif());
//        r.setNature(re.getNature());
//        r.setAuteur(re.getAuthor());
//        r.setLastAuteur(re.getLastAuthor());
//        r.setLastDateUpdate(df.format(re.getLastDateSave()));
//        if (re.getYvsArticlesRecompenseList() != null) {
//            //récupération des articles lié au plan de récompense
//            List<ArticleRecompense> larec = new ArrayList<>();
//            if (!re.getYvsArticlesRecompenseList().isEmpty()) {
//                for (YvsArticlesRecompense pr : re.getYvsArticlesRecompenseList()) {
//                    ArticleRecompense arec = new ArticleRecompense();
//                    arec.setIdArticle(pr.getArticle().getId());
//                    arec.setRefArt(pr.getArticle().getRefArt());
//                    arec.setTaux(pr.getTaux());
//                    arec.setValeur(pr.getValeur());
//                    //si le lien implique des tranches, on les charge
//                    TrancheVal tranch = null;
//                    if (pr.getTranche() != null) {
//                        tranch = new TrancheVal();
//                        tranch.setId(pr.getTranche().getId());
//                        tranch.setModelTranche(pr.getTranche().getModelTranche());
//                        //récupération des bornes
//                        List<BorneTranche> lb = new ArrayList<>();
//                        for (YvsBorneTranches b : pr.getTranche().getYvsBorneTranchesList()) {
//                            lb.add(buildBorne(b));
//                        }
//                        tranch.setListBorne(lb);
//                        arec.setTranche(tranch);
//                    }
//                    larec.add(arec);
//                }
//            }
//            r.setListArticle(larec);
//        }
//        return r;
//    }
//
//    private BorneTranche buildBorne(YvsBorneTranches b) {
//        BorneTranche bo = new BorneTranche();
//        bo.setBorne(b.getBorne());
//        bo.setId(b.getId());
//        bo.setIdTranche(b.getTranche().getId());
//        bo.setPrix(b.getPrix());
//        bo.setRemise(b.getRemise());
//        return bo;
//    }
//
//    private YvsPlanDeRecompense buildEntityFromBean(Recompenses a) {
//        YvsPlanDeRecompense art = new YvsPlanDeRecompense(a.getId());
//        art.setActif(a.isActif());
//        art.setDesignation(a.getDesignation());
//        art.setDuree(a.getDuree());
//        art.setDebut(a.getDebut());
//        art.setFin(a.getFin());
//        art.setObjectif(a.getObjectif());
//        art.setBaseCalcul(a.getBaseCalcul());
//        art.setNature(a.getNature());
//        if (a.getAuteur() != null) {
//            art.setAuthor(a.getAuteur());
//        } else {
//            art.setAuthor(getUserOnLine());
//        }
//        if (a.getLastAuteur() != null) {
//            art.setLastAuthor(a.getLastAuteur());
//        } else {
//            art.setLastAuthor(getUserOnLine());
//        }
//        try {
//            if (a.getLastDateUpdate() != null) {
//                art.setLastDateSave(df.parse(a.getLastDateUpdate()));
//            } else {
//                art.setLastDateSave(new Date());
//            }
//        } catch (ParseException ex) {
//            Logger.getLogger(ManagedRecompense.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return art;
//    }
//
//    //ajouter un article au plan de recompense
//    public void addArticle() {
//        if (artRec.getRefArt() != null) {
//            //récupération de l'article
//            String[] ch = {"refArt"};
//            Object[] val = {artRec.getRefArt()};
//            ArticleRecompense articleRecBean = new ArticleRecompense();
//            YvsArticlesRecompense articleRecEntity = new YvsArticlesRecompense();
//            dao.setEntityClass(YvsBaseArticles.class);
//            YvsBaseArticles art = (YvsBaseArticles) dao.getOne(ch, val);
//            articleRecEntity.setYvsArticlesRecompensePK(new YvsArticlesRecompensePK(art.getId(), recompense.getId()));
//            articleRecEntity.setTaux(artRec.getTaux());
//            articleRecEntity.setValeur(artRec.getValeur());
//            articleRecBean.setTaux(artRec.getTaux());
//            articleRecBean.setValeur(artRec.getValeur());
//            articleRecBean.setIdArticle(art.getId());
//            articleRecBean.setRefArt(art.getRefArt());
//            if (recompense.getListArticle() == null) {
//                List<ArticleRecompense> l = new ArrayList<>();
//                recompense.setListArticle(l);
//            }            
//            if (!recompense.getListArticle().contains(articleRecBean)) {                
//                //s'il y a des tranche en jeu  
//                if (tranche && !listBorneTranche.isEmpty()) {
//                    TrancheVal tr = new TrancheVal(0, modelTranche);
//                    //je persiste la tranche
//                    YvsTranches tranch = buildTranche();
//                    tranch = (YvsTranches) dao.save1(tranch);
//                    tr.setId(tranch.getId());
//                    //parcour la liste des borne
//                    int pos = 0;
//                    for (BorneTranche b : listBorneTranche) {
//                        YvsBorneTranches bt = buildBorne(b);
//                        bt.setId(null);
//                        bt.setTranche(tranch);
//                        bt = (YvsBorneTranches) dao.save1(bt);
//                        b.setIdTranche(bt.getId());
//                        b.setId(bt.getId());
//                        listBorneTranche.set(pos, b);
//                    }
//                    List<BorneTranche> l = new ArrayList<>();
//                    l.addAll(listBorneTranche);
//                    tr.setListBorne(l);
//
//                    articleRecBean.setTranche(tr);
//                    articleRecEntity.setTranche(tranch);
//                } else if (tranche && listBorneTranche.isEmpty()) {
//                    getMessage("Vous avez indique que ce plan était repartie en tranche, cependant vous n'avez indiqué aucune tranche", FacesMessage.SEVERITY_WARN);
//                    return;
//                } else {
//                    articleRecBean.setTranche(null);
//                    articleRecEntity.setTranche(null);
//                }
//                //enregistre la liaison article recompense
//                dao.save(articleRecEntity);
//                recompense.getListArticle().add(0, articleRecBean);
//                if (selectRecompense.getListArticle() == null) {
//                    List<ArticleRecompense> l = new ArrayList<>();
//                    selectRecompense.setListArticle(l);
//                }
//                listRecompense.set(listRecompense.indexOf(selectRecompense), selectRecompense);
//                listBorneTranche.clear();
//                borne = 1;
//                taux = 1;
//                artRec.setTaux(1);
//                artRec.setValeur(0);
//                tranche = false;
//                update("A-ta-tarif");
//                update("A-bo-tarif");
//            } else {
//                //si l'article est déjà présent dans le plan de recompense, on modifie ce plan
//                //on vérifie si la ligne art-recompense était lié à une tranche.
//                ArticleRecompense toUpdate = recompense.getListArticle().get(recompense.getListArticle().indexOf(new ArticleRecompense(artRec.getRefArt())));
//                if (toUpdate.getTranche() != null && !tranche && !toUpdate.getTranche().getListBorne().isEmpty()) {
//                    //s'il y avait une tranche et qu'il y en a plus, on informe l'utilisateur
//
//                    getMessage("annullation de tranches", FacesMessage.SEVERITY_ERROR);
//                    updatePt(toUpdate);
//                } else {
//                    //si la forma du plan tarifaire est inchangé, 
//                    if (toUpdate.getTranche() != null) {
//                        //on modifie les tranches s'il y en a
//                        int pos = 0;
//                        for (BorneTranche b : listBorneTranche) {
//                            YvsBorneTranches bb = buildBorne(b);
//                            bb.setTranche(new YvsTranches(toUpdate.getTranche().getId()));
//                            bb = (YvsBorneTranches) dao.update(bb);
//                            long id = b.getId();
//                            b.setId(bb.getId());
//                            //modifie sur la vue
//                            listBorneTranche.set(pos, b);
//                            //modifie l'objet toUpdate
//                            if (toUpdate.getTranche().getListBorne().contains(new BorneTranche(id))) {
//                                toUpdate.getTranche().getListBorne().set(toUpdate.getTranche().getListBorne().indexOf(new BorneTranche(id)), b);
//                            } else {
//                                toUpdate.getTranche().getListBorne().add(b);
//                            }
//                        }
//                    } else {
//                        //s'il y a des tranches
//                        if (tranche && !listBorneTranche.isEmpty()) {
//                            TrancheVal tr = new TrancheVal(0, modelTranche);
//                            YvsTranches tranch = buildTranche();
//                            tranch = (YvsTranches) dao.save1(tranch);
//                            tr.setId(tranch.getId());
//                            //parcour la liste des borne
//                            int pos = 0;
//                            for (BorneTranche b : listBorneTranche) {
//                                YvsBorneTranches bt = buildBorne(b);
//                                bt.setId(null);
//                                bt.setTranche(tranch);
//                                bt = (YvsBorneTranches) dao.save1(bt);
//                                b.setIdTranche(bt.getId());
//                                b.setId(bt.getId());
//                                listBorneTranche.set(pos, b);
//                            }
//                            List<BorneTranche> l = new ArrayList<>();
//                            l.addAll(listBorneTranche);
//                            tr.setListBorne(l);
//                            toUpdate.setTranche(tr);
//                            YvsArticlesRecompense pt = new YvsArticlesRecompense(new YvsArticlesRecompensePK(toUpdate.getIdArticle(), recompense.getId()));
//                            pt.setTranche(tranch);
//                            pt.setTaux(0);
//                            pt.setValeur(0);
//                            toUpdate.setTaux(0);
//                            toUpdate.setValeur(0);
//                            dao.update(pt);
//                            recompense.getListArticle().set(recompense.getListArticle().indexOf(toUpdate), toUpdate);
//                        } else {
//                            //s'il n'y a pas de tranche, 
//                            toUpdate.setTaux(artRec.getTaux());
//                            toUpdate.setValeur(artRec.getValeur());
//                            YvsArticlesRecompense pt = new YvsArticlesRecompense(toUpdate.getIdArticle(), recompense.getId());
//                            pt.setTaux(artRec.getTaux());
//                            pt.setValeur(artRec.getValeur());
//                            dao.update(pt);
//                            recompense.getListArticle().set(recompense.getListArticle().indexOf(toUpdate), toUpdate);
//                        }
//                    }
//                    succes();
//                }
//            }
//        } else {
//            getMessage(message.getMessage("choose_catTarif"), FacesMessage.SEVERITY_ERROR);
//        }
//    }
//
//    private YvsTranches buildTranche() {
//        YvsTranches tranch = new YvsTranches();
//        tranch.setModelTranche(modelTranche);
////        tranch.setAuthor(getUserOnLine());   
//        return tranch;
//    }
//
//    private YvsBorneTranches buildBorne(BorneTranche b) {
//        YvsBorneTranches bb = new YvsBorneTranches(b.getId());
//        bb.setBorne((float) b.getBorne());
//        bb.setPrix(b.getPrix());
//        bb.setRemise(b.getRemise());
//        bb.setTranche(new YvsTranches((int) b.getIdTranche()));
//        return bb;
//    }
//
//    private void updatePt(ArticleRecompense toUpdate) {
//        if (toUpdate.getTranche() != null) {
//            //supprime les tranches
//            for (BorneTranche b : toUpdate.getTranche().getListBorne()) {
//                YvsBorneTranches bb = buildBorne(b);
//                dao.delete(bb);
//            }
//            toUpdate.setTaux(artRec.getTaux());
//            toUpdate.setValeur(artRec.getValeur());
//            YvsArticlesRecompense ar = new YvsArticlesRecompense(toUpdate.getIdArticle(), recompense.getId());
//            ar.setTaux(artRec.getTaux());
//            ar.setValeur(artRec.getValeur());
//            ar.setTranche(null);
//            dao.update(ar);
//            dao.delete(new YvsTranches(toUpdate.getTranche().getId()));
//            toUpdate.setTranche(null);
//            recompense.getListArticle().set(recompense.getListArticle().indexOf(toUpdate), toUpdate);
//        }
//    }
//    private int idBorne = 1;
//
//    public void addBorne() {
//        BorneTranche b = new BorneTranche();
//        b.setBorne(getBorne());
//        b.setId(idBorne++);
//        b.setRemise(getTaux());
//        b.setPrix(getValeur());
//        listBorneTranche.add(0, b);
//        update("rec-bo-tarif");
//    }
//    //supprime une borne
//
//    public void deleteBorne() {
//        try {
//            YvsBorneTranches b = new YvsBorneTranches(selectBorne.getId());
//            dao.delete(b);
//            listBorneTranche.remove(selectBorne);
//            update("A-bo-tarif");
//        } catch (Exception ex) {
//            listBorneTranche.remove(selectBorne);
//            update("A-bo-tarif");
//        }
//    }
//
//    public void changeDefaultButon(TabChangeEvent ev) {
//        String id = ev.getTab().getId();
//        switch (id) {
//            case "rec-tab_gen":
//                setDefaultButon("P-rec-update");
//                break;
//            case "rec-tab_tarif":
//                setDefaultButon("rec-vue-group:rec-buton-addTarif");
//                break;
//            default:
//                setDefaultButon("P-rec-save");
//                break;
//        }
//    }
//
//    public void choixDuree(ValueChangeEvent ev) {
//        if (ev.getOldValue() != ev.getNewValue()) {
//            String choix = (String) ev.getNewValue();
//            switch (choix) {
//                case Constantes.DUREE_PERMANENCE:
//                    disableDateDebut = true;
//                    disableDateFin = true;
//                    break;
//                case Constantes.DUREE_PENDANT:
//                    disableDateDebut = false;
//                    disableDateFin = false;
//                    break;
//                case Constantes.DUREE_LE:
//                    disableDateDebut = false;
//                    disableDateFin = true;
//                    break;
//                case Constantes.DUREE_A_PARTIR:
//                    disableDateDebut = false;
//                    disableDateFin = true;
//                    break;
//                case Constantes.DUREE_JUSQUA:
//                    disableDateDebut = false;
//                    disableDateFin = true;
//                    break;
//            }
//        }
//    }
//    private boolean disTaux;
//    private boolean disVal = true;
//    private boolean disTaux1;
//    private boolean disVal1 = true;
//
//    public boolean isDisTaux() {
//        return disTaux;
//    }
//
//    public void setDisTaux(boolean disTaux) {
//        this.disTaux = disTaux;
//    }
//
//    public boolean isDisVal() {
//        return disVal;
//    }
//
//    public void setDisVal(boolean disVal) {
//        this.disVal = disVal;
//    }
//
//    public boolean isDisTaux1() {
//        return disTaux1;
//    }
//
//    public void setDisTaux1(boolean disTaux1) {
//        this.disTaux1 = disTaux1;
//    }
//
//    public boolean isDisVal1() {
//        return disVal1;
//    }
//
//    public void setDisVal1(boolean disVal1) {
//        this.disVal1 = disVal1;
//    }
//
//    public void saisieTaux(String id) {
//        switch (id) {
//            case "rec-Prix":
//                if (artRec.getTaux() != 0) {
//                    disVal = true;
//                    artRec.setValeur(0);
//                    disTaux = false;
//                } else {
//                    disVal = false;
//                    disTaux = true;
//                }
//                break;
//            case "rec-rem":
//                if (artRec.getValeur() != 0) {
//                    disTaux = true;
//                    artRec.setTaux(0);
//                    disVal = false;
//                } else {
//                    disTaux = false;
//                    disVal = true;
//                }
//                break;
//            case "taux1":
//                if (taux != 0) {
//                    disTaux1 = false;
//                    disVal1 = true;
//                    valeur = 0;
//                } else {
//                    disTaux1 = true;
//                    disVal1 = false;
//                }
//                break;
//            case "val1":
//                if (valeur != 0) {
//                    disTaux1 = true;
//                    disVal1 = false;
//                    taux = 0;
//                } else {
//                    disTaux1 = false;
//                    disVal1 = true;
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    public void openBorne(ValueChangeEvent ev) {
//        boolean b = (Boolean) ev.getNewValue();
//        if (b) {
//            artRec.setTaux(0);
//            borne = 1;
//            listBorneTranche.clear();
//            taux = artRec.getTaux();
//            valeur = artRec.getValeur();
//            disTaux = true;
//            disVal = true;
//            update("rec-bo-tarif");
//            update("rec-bo-grid");
//            RequestContext.getCurrentInstance().execute("tranche.show()");
//        } else {
//            disTaux = false;
//            disVal = false;
//        }
//    }
////lorsqu'on selectionne une ligne article-recompense    
//    public void selectArtRec(SelectEvent ev) {
//        ArticleRecompense ar = (ArticleRecompense) ev.getObject();        
//        if (ar != null) {
//            artRec.setIdArticle(ar.getIdArticle());
//            artRec.setRefArt(ar.getRefArt());
//            artRec.setTaux(ar.getTaux());
//            artRec.setValeur(ar.getValeur());
//            listBorneTranche.clear();
//            if (ar.getTranche() != null) {
//                tranche = true;
//                artRec.setTaux(0);
//                artRec.setValeur(0);
//                listBorneTranche.addAll(ar.getTranche().getListBorne());
//                RequestContext.getCurrentInstance().execute("tranche.show()");
//            } else {
//                tranche = false;
//                artRec.setTaux(ar.getTaux());
//                artRec.setValeur(ar.getValeur());
//            }
//            Collections.sort(listBorneTranche, new BorneTranche());
//            update("rec-bo-tarif");
//            update("rec-vue-group:rec-ch_catT");
//            update("rec-tab_tarif");
//        }
//    }
//    
//     /**
//     * Supprimer un article d'un plan de recompense
//     */
//    public void deleteArtRec() {
//        if(artRec.getIdArticle()==0){
//            getMessage("Choisir une ligne", FacesMessage.SEVERITY_WARN);
//        }
//        YvsArticlesRecompense tar = new YvsArticlesRecompense();
//        tar.setYvsArticlesRecompensePK(new YvsArticlesRecompensePK(artRec.getIdArticle(), recompense.getId()));
//        dao.delete(tar);
//        //si le tarif est lié à une tranche, on la supprime aussi
//        if (selectArticleRec.getTranche() != null) {
//            YvsTranches t = new YvsTranches(selectArticleRec.getTranche().getId());
//            dao.delete(t);
//        }
//        ArticleRecompense t = new ArticleRecompense();
//        t.setRefArt(selectArticleRec.getRefArt());
//        selectRecompense.getListArticle().remove(selectArticleRec);
//        recompense.getListArticle().remove(selectArticleRec);
//        update("rec-groupP-table");
//        update("rec-ta-tarif");
//    }
//
//    @Override
//    public void deleteBean() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void unLoadOnView(UnselectEvent ev) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//}
