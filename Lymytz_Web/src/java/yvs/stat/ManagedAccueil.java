/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.commercial.UtilCom;
import yvs.commercial.statistique.SyntheseClient;
import yvs.commercial.stock.ManagedStockArticle;
import yvs.commercial.stock.ManagedTransfertStock;
import yvs.dao.Options;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseParametre;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.client.YvsBasePlanTarifaire;
import yvs.entity.commercial.client.YvsBasePlanTarifaireTranche;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.objectifs.YvsComObjectifsAgence;
import yvs.entity.commercial.objectifs.YvsComObjectifsComercial;
import yvs.entity.commercial.objectifs.YvsComObjectifsPointVente;
import yvs.entity.commercial.objectifs.YvsComPeriodeObjectif;
import yvs.entity.commercial.rrr.YvsComGrilleRistourne;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.compta.YvsComptaCreditClient;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.production.pilotage.YvsProdDeclarationProduction;
import yvs.entity.produits.YvsBaseArticles;
import yvs.init.Initialisation;
import yvs.util.Managed;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedAccueil extends Managed implements Serializable {

    private List<YvsComObjectifsComercial> objectifs;
    private List<YvsComObjectifsAgence> objectifsAgence;
    private List<YvsComObjectifsPointVente> objectifsPoint;
    private String val_Element;
    private List<YvsBaseConditionnement> conditionnements;
    private YvsBaseArticles articles = new YvsBaseArticles();
    private List<YvsBasePlanTarifaireTranche> grilles;
    private List<YvsComGrilleRistourne> grilleRistourne;
    private List<YvsBaseArticleDepot> stockages;
    private String searchReference;
    private Articles bean_articles = new Articles();
    private YvsBaseArticles a = new YvsBaseArticles();
    private Double marges = 0.0;
    private Double qte_vendu = 0.0;
    private Double qte_achat = 0.0;
    private Double qte_prod = 0.0;
    private double ca_v = 0.0;
    private double ca_a = 0.0;
    private double ca_prod = 0.0;
    private Long nbr_ventes = 0L;
    private Long nbr_achats = 0L;
    private Long nbr_declars = 0L;
    private List<Object[]> listArticle;
    private YvsComContenuDocVente content = new YvsComContenuDocVente();
    private YvsProdDeclarationProduction declaration = new YvsProdDeclarationProduction();
    private YvsComContenuDocAchat content_achat = new YvsComContenuDocAchat();
    private long periodeObjectif, periodeObjectifAgence, periodeObjectifPoint;

    private boolean load = false;
    private static boolean loadNbreOnline = false, loadWorkflow = false, loadWarning = false, loadInfos = false;

    private int nombreTransfertIncoherentAccueil = 5;

    private String query;
    private String resultat;
    private String path = "";
    private String logs;

    private SyntheseClient synthese = new SyntheseClient();
    private Date dateDebut = new Date(), dateFin = new Date();
    private List<YvsComClient> listClient;
    YvsComClient client = new YvsComClient();
    private String code_client;

    MethodExpression method = null;

    public ManagedAccueil() {
        objectifs = new ArrayList<>();
        objectifsAgence = new ArrayList<>();
        objectifsPoint = new ArrayList<>();
        grilles = new ArrayList<>();
        grilleRistourne = new ArrayList<>();
        stockages = new ArrayList<>();
        listArticle = new ArrayList<>();
        conditionnements = new ArrayList<>();
    }

    public int getNombreTransfertIncoherentAccueil() {
        return nombreTransfertIncoherentAccueil;
    }

    public void setNombreTransfertIncoherentAccueil(int nombreTransfertIncoherentAccueil) {
        this.nombreTransfertIncoherentAccueil = nombreTransfertIncoherentAccueil;
    }

    public SyntheseClient getSynthese() {
        return synthese;
    }

    public void setSynthese(SyntheseClient synthese) {
        this.synthese = synthese;
    }

    public String getCodeClient() {
        return code_client;
    }

    public void setCodeClient(String code_client) {
        this.code_client = code_client;
    }

    public List<YvsComClient> getListClient() {
        return listClient;
    }

    public void setListClient(List<YvsComClient> listClient) {
        this.listClient = listClient;
    }

    public YvsComClient getClient() {
        return client;
    }

    public void setClient(YvsComClient client) {
        this.client = client;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public boolean isDisplayPool() {
        return loadNbreOnline && loadWorkflow && loadWarning && loadInfos;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public List<YvsComObjectifsAgence> getObjectifsAgence() {
        return objectifsAgence;
    }

    public void setObjectifsAgence(List<YvsComObjectifsAgence> objectifsAgence) {
        this.objectifsAgence = objectifsAgence;
    }

    public List<YvsComObjectifsPointVente> getObjectifsPoint() {
        return objectifsPoint;
    }

    public void setObjectifsPoint(List<YvsComObjectifsPointVente> objectifsPoint) {
        this.objectifsPoint = objectifsPoint;
    }

    public long getPeriodeObjectifAgence() {
        return periodeObjectifAgence;
    }

    public void setPeriodeObjectifAgence(long periodeObjectifAgence) {
        this.periodeObjectifAgence = periodeObjectifAgence;
    }

    public long getPeriodeObjectifPoint() {
        return periodeObjectifPoint;
    }

    public void setPeriodeObjectifPoint(long periodeObjectifPoint) {
        this.periodeObjectifPoint = periodeObjectifPoint;
    }

    public List<YvsComObjectifsComercial> getObjectifs() {
        return objectifs;
    }

    public void setObjectifs(List<YvsComObjectifsComercial> objectifs) {
        this.objectifs = objectifs;
    }

    public long getPeriodeObjectif() {
        return periodeObjectif;
    }

    public void setPeriodeObjectif(long periodeObjectif) {
        this.periodeObjectif = periodeObjectif;
    }

    public YvsBaseParametre getCurrentParam() {
        return currentParam;
    }

    public void setCurrentParam(YvsBaseParametre currentParam) {
        this.currentParam = currentParam;
    }

    @Override
    public void loadAll() {
        if (!load) {
            YvsComPeriodeObjectif y = loadCurrentPeriode();
            periodeObjectif = y != null ? y.getId() : 0;
            periodeObjectifAgence = y != null ? y.getId() : 0;
            periodeObjectifPoint = y != null ? y.getId() : 0;
        }
        load = true;
        if (currentParam == null) {
            currentParam = (YvsBaseParametre) dao.loadOneByNameQueries("YvsBaseParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
    }

    public void loadNotification() {
        if (!loadNbreOnline) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("nbreOnline", countUsersConnect());
            loadNbreOnline = true;
        }
        if (!loadWorkflow) {
            countWorkFlowToValid();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("workflow", WORKFLOWS);
            loadWorkflow = true;
        }
        if (!loadWarning) {
            countWarningToValid();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("warning", WARNINGS);
            loadWarning = true;
        }
        if (!loadInfos) {
            countInfosToValid();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("information", INFORMATIONS);
            loadInfos = true;
        }
    }

    public void rappelForPassword() {
        if (currentUser != null ? currentUser.isMustChangePassword() : false) {
            openDialog("dlgRappelForPassword");
        }
    }

    public void gotoViewMyCompte(ActionEvent event) {
        gotoViewMyCompte();
    }

    public void onChooseNombreTransfertIncoherent() {
        if (nombreTransfertIncoherentAccueil == -1) {
            Navigations w = (Navigations) giveManagedBean(Navigations.class);
            if (w != null) {
                ManagedTransfertStock mt = (ManagedTransfertStock) giveManagedBean(ManagedTransfertStock.class);
                if (mt != null) {
                    mt.setWithIncoherence(true);
                    mt.addParamIncoherence();
                }
                w.naviguationApps("Transferts Stock", "modGescom", "smenTransfert", true);
            }
            nombreTransfertIncoherentAccueil = 5;
        }
    }

    public void gotoViewMyCompte() {
        try {
            Navigations w = (Navigations) giveManagedBean(Navigations.class);
            if (w != null) {
                w.naviguationApps("Mon profils", "modDonneBase", "smenProfilUser", true);
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("managedAccueil (gotoViewMyCompte)", ex);
        }
    }

    public YvsComPeriodeObjectif loadCurrentPeriode() {
        return loadCurrentPeriode(new Date());
    }

    public YvsComPeriodeObjectif loadCurrentPeriode(Date date) {
        YvsComPeriodeObjectif y = null;
        if (currentAgence != null) {
            y = (YvsComPeriodeObjectif) dao.loadOneByNameQueries("YvsComPeriodeObjectif.findByDates", new String[]{"societe", "date"}, new Object[]{currentAgence.getSociete(), date});
            if (y != null ? y.getId() > 0 : false) {
                loadCurrentObjectifs(y);
                loadCurrentObjectifsAgence(y);
                loadCurrentObjectifsPoint(y);
            }
        }
        return y;
    }

    public void loadCurrentObjectifs(YvsComPeriodeObjectif periode) {
        if (currentUser != null ? currentUser.getUsers() != null : false) {
            YvsComComerciale y = (YvsComComerciale) dao.loadOneByNameQueries("YvsComComerciale.findByUser", new String[]{"user"}, new Object[]{currentUser.getUsers()});
            if (y != null ? y.getId() > 0 : false) {
                loadObjectifs(periode, y);
            }
        }
    }

    public void loadObjectifs(YvsComPeriodeObjectif periode, YvsComComerciale commercial) {
        objectifs = dao.loadNameQueries("YvsComObjectifsComercial.findByCommercial", new String[]{"periode", "commercial"}, new Object[]{periode, commercial});
        for (YvsComObjectifsComercial y : objectifs) {
            y.setRealise(dao.callFonction("SELECT com_get_valeur_objectif(?,?,?,?)", new Options[]{new Options(commercial.getId(), 1), new Options(periode.getId(), 2), new Options(y.getObjectif().getId(), 3), new Options("", 4)}));
        }
    }

    public void loadCurrentObjectifsAgence(YvsComPeriodeObjectif periode) {
        if (currentAgence != null ? currentAgence.getId() > 0 : false) {
            loadObjectifsAgence(periode, currentAgence);
        }
    }

    public void loadObjectifsAgence(YvsComPeriodeObjectif periode, YvsAgences agence) {
        objectifsAgence = dao.loadNameQueries("YvsComObjectifsAgence.findByAgence", new String[]{"periode", "agence"}, new Object[]{periode, agence});
        for (YvsComObjectifsAgence y : objectifsAgence) {
            y.setRealise(dao.callFonction("SELECT com_get_valeur_objectif(?,?,?,?)", new Options[]{new Options(agence.getId(), 1), new Options(periode.getId(), 2), new Options(y.getObjectif().getId(), 3), new Options("A", 4)}));
        }
    }

    public void loadCurrentObjectifsPoint(YvsComPeriodeObjectif periode) {
        if (currentPoint != null ? currentPoint.getId() > 0 : false) {
            loadObjectifsPoint(periode, currentPoint);
        }
    }

    public void loadObjectifsPoint(YvsComPeriodeObjectif periode, YvsBasePointVente point) {
        objectifsPoint = dao.loadNameQueries("YvsComObjectifsPointVente.findByPointVente", new String[]{"periode", "pointVente"}, new Object[]{periode, point});
        for (YvsComObjectifsPointVente y : objectifsPoint) {
            y.setRealise(dao.callFonction("SELECT com_get_valeur_objectif(?,?,?,?)", new Options[]{new Options(point.getId(), 1), new Options(periode.getId(), 2), new Options(y.getObjectif().getId(), 3), new Options("P", 4)}));
        }
    }

    public void choosePeriodeCommercial() {
        loadCurrentObjectifs(new YvsComPeriodeObjectif(periodeObjectif));
    }

    public void choosePeriodeAgence() {
        loadCurrentObjectifsAgence(new YvsComPeriodeObjectif(periodeObjectifAgence));
    }

    public void choosePeriodePoint() {
        loadCurrentObjectifsPoint(new YvsComPeriodeObjectif(periodeObjectifPoint));
    }

    @Override
    public boolean controleFiche(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean saveNew() {
        resultat = "";
        try {
            if (Util.asString(query)) {
                File file = new File("execute_query.sql");
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                try (PrintWriter out = new PrintWriter(file, "UTF-8")) {
                    out.println(query);
                    out.flush();
                }
                int reponse = Util.executeSqlFile(ds, file.getAbsolutePath());
                if (reponse < 0) {
                    resultat = "Echèc sur l'éxecution de la requête";
                } else {
                    resultat = "Opération effectuée avec succès";
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ManagedAccueil.class.getName()).log(Level.SEVERE, null, ex);
            resultat = "Echèc sur l'éxecution de la requête";
        }
        update("panel-resultat");
        return true;
    }

    public String getVal_Element() {
        return val_Element;
    }

    public void setVal_Element(String val_Element) {
        this.val_Element = val_Element;
    }

    public String loadElement(String element) {
        String path = "";
        if (element.equals("articles")) {
            path = "../../pages/donnee_de_base/articles/details_articles.xhtml";
        } else if (element.equals("clients")) {
            path = "../../pages/stat/com/clients/detail_client.xhtml";
        }
        return path;
    }

    public List<YvsBaseConditionnement> getConditionnements() {
        return conditionnements;
    }

    public void setConditionnements(List<YvsBaseConditionnement> conditionnements) {
        this.conditionnements = conditionnements;
    }

    public YvsBaseArticles getArticles() {
        return articles;
    }

    public void setArticles(YvsBaseArticles articles) {
        this.articles = articles;
    }

    public List<YvsBasePlanTarifaireTranche> getGrilles() {
        return grilles;
    }

    public void setGrilles(List<YvsBasePlanTarifaireTranche> grilles) {
        this.grilles = grilles;
    }

    public List<YvsComGrilleRistourne> getGrilleRistourne() {
        return grilleRistourne;
    }

    public void setGrilleRistourne(List<YvsComGrilleRistourne> grilleRistourne) {
        this.grilleRistourne = grilleRistourne;
    }

    public List<YvsBaseArticleDepot> getStockages() {
        return stockages;
    }

    public void setStockages(List<YvsBaseArticleDepot> stockages) {
        this.stockages = stockages;
    }

    public String getSearchReference() {
        return searchReference;
    }

    public void setSearchReference(String searchReference) {
        this.searchReference = searchReference;
    }

    public double getMarges() {
        return marges;
    }

    public void setMarges(double marges) {
        this.marges = marges;
    }

//    public double getQte_vendu() {
//        return qte_vendu;
//    }
//    
//    public void setQte_vendu(double qte_vendu) {
//        this.qte_vendu = qte_vendu;
//    }
    public double getCa_v() {
        return ca_v;
    }

    public void setCa_v(double ca_v) {
        this.ca_v = ca_v;
    }

    public YvsComContenuDocVente getContent() {
        return content;
    }

    public void setContent(YvsComContenuDocVente content) {
        this.content = content;
    }

//    public double getQte_achat() {
//        return qte_achat;
//    }
//    
//    public void setQte_achat(double qte_achat) {
//        this.qte_achat = qte_achat;
//    }
    public double getCa_a() {
        return ca_a;
    }

    public void setCa_a(double ca_a) {
        this.ca_a = ca_a;
    }

    public YvsComContenuDocAchat getContent_achat() {
        return content_achat;
    }

    public void setContent_achat(YvsComContenuDocAchat content_achat) {
        this.content_achat = content_achat;
    }

    public Double getQte_vendu() {
        return qte_vendu;
    }

    public void setQte_vendu(Double qte_vendu) {
        this.qte_vendu = qte_vendu;
    }

    public Double getQte_achat() {
        return qte_achat;
    }

    public void setQte_achat(Double qte_achat) {
        this.qte_achat = qte_achat;
    }

    public Double getQte_prod() {
        return qte_prod;
    }

    public void setQte_prod(Double qte_prod) {
        this.qte_prod = qte_prod;
    }

    public double getCa_prod() {
        return ca_prod;
    }

    public void setCa_prod(double ca_prod) {
        this.ca_prod = ca_prod;
    }

    public YvsProdDeclarationProduction getDeclaration() {
        return declaration;
    }

    public void setDeclaration(YvsProdDeclarationProduction declaration) {
        this.declaration = declaration;
    }

    public Long getNbr_ventes() {
        return nbr_ventes;
    }

    public void setNbr_ventes(Long nbr_ventes) {
        this.nbr_ventes = nbr_ventes;
    }

    public Long getNbr_achats() {
        return nbr_achats;
    }

    public void setNbr_achats(Long nbr_achats) {
        this.nbr_achats = nbr_achats;
    }

    public Long getNbr_declars() {
        return nbr_declars;
    }

    public void setNbr_declars(Long nbr_declars) {
        this.nbr_declars = nbr_declars;
    }

    public void search() {
        if (searchReference != null ? !searchReference.isEmpty() : false) {
            ManagedStockArticle s = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if (s != null) {
                s.getArticles_stock_accueil().clear();
                s.getLots().clear();
            }
            listArticle = dao.loadNameQueries("YvsBaseArticles.findSimpleByCodeL", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), searchReference + "%"});
            if (listArticle != null ? listArticle.size() == 1 : false) {
                try {
                    conditionnements.clear();
                    grilles.clear();
                    grilleRistourne.clear();
                } catch (Exception e) {
                    getException("", e);
                }
//                articles = listArticle.get(0);
                articles = new YvsBaseArticles((Long) listArticle.get(0)[0], (String) listArticle.get(0)[1], (String) listArticle.get(0)[2]);
                find(articles);
            }
        } else {
            getMessage("Veuillez entrer la referece de l'article", FacesMessage.SEVERITY_WARN);
        }
    }

    public void choixArticle() {
        if (articles != null ? articles.getId() > 0 : false) {
            int index = 0;
            for (Object[] o : listArticle) {
                if (articles.getId().equals((Long) o[0])) {
                    break;
                }
                index++;
            }
            if (index > -1) {
//                articles = listArticle.get(index);
                articles = new YvsBaseArticles((Long) listArticle.get(index)[0], (String) listArticle.get(index)[1], (String) listArticle.get(index)[2]);
//                bean_articles = UtilProd.buildBeanArticles(a)
                try {
                    conditionnements.clear();
                    grilles.clear();
                    grilleRistourne.clear();
                } catch (Exception e) {
                    getException("", e);
                }
                find(articles);
            }
        }
    }

    public void find(YvsBaseArticles art) {
        try {
            articles = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findAllById", new String[]{"id"}, new Object[]{art.getId()});
            if (articles != null ? articles.getId() > 0 : false) {
                articles.setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle1", new String[]{"article"}, new Object[]{articles}));
                marges = getMarge(articles);
                marges = marges != null ? marges : 0;

                Double pr = 0.0;
                YvsBaseDepots depot = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticlePr", new String[]{"article", "agence"}, new Object[]{articles, currentAgence});
                for (YvsBaseConditionnement c : articles.getConditionnements()) {
//                    if (depot != null ? depot.getId() > 0 : false) {
//                        pr = dao.getPr(articles.getId(), depot.getId(), 0L, new Date(), c.getId());
//                        pr = pr != null ? pr : 0;
//                        c.getArticle().setPrixMin(pr);
//                    }
                    conditionnements.add(c);
                }
                // List<YvsBasePlanTarifaire> plans = dao.loadNameQueries("YvsBasePlanTarifaire.findByArticle", new String[]{"article"}, new Object[]{articles});
                List<YvsBasePlanTarifaire> plans = articles.getPlans_tarifaires();
                if (plans != null ? !plans.isEmpty() : false) {
                    for (YvsBasePlanTarifaire plan : plans) {
                        grilles.addAll(dao.loadNameQueries("YvsBasePlanTarifaireTranche.findByPlan", new String[]{"plan"}, new Object[]{plan}));
                    }
                }
//                stockages = dao.loadNameQueries("YvsBaseArticleDepot.findByArticleActif", new String[]{"article"}, new Object[]{articles});
                getTransactions(articles);
            } else {
                articles = new YvsBaseArticles();
                getMessage("Aucun article trouvé", FacesMessage.SEVERITY_ERROR);
            }
        } catch (Exception e) {
            getException("", e);
        }
    }

    public double getMarge(YvsBaseArticles articles) {
        try {
            Double marge = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findMargeByArticle", new String[]{"article"}, new Object[]{articles});
            return Math.round(marge != null ? marge : 0);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void getTransactions(YvsBaseArticles articles) {

        nbr_ventes = (Long) dao.loadObjectByNameQueries("YvsComContenuDocVente.findNbreDocByArticle", new String[]{"article", "typeDoc"}, new Object[]{articles, Constantes.TYPE_FV});
        nbr_ventes = nbr_ventes != null ? nbr_ventes : 0;

        nbr_declars = (Long) dao.loadObjectByNameQueries("YvsProdDeclarationProduction.findNbreDocByArticle", new String[]{"article"}, new Object[]{articles});
        nbr_declars = nbr_declars != null ? nbr_declars : 0;

        nbr_achats = (Long) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findNbreDocByArticle", new String[]{"article", "typeDoc"}, new Object[]{articles, Constantes.TYPE_FA});
        nbr_achats = nbr_achats != null ? nbr_achats : 0;

        Double montant = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findCAByArticle", new String[]{"article", "type_doc"}, new Object[]{articles, Constantes.TYPE_FV});
        Double montant_avoir = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findCAByArticle", new String[]{"article", "type_doc"}, new Object[]{articles, Constantes.TYPE_FAV});

        ca_v = Math.round((montant != null ? montant : 0) - (montant_avoir != null ? montant_avoir : 0));
        content = (YvsComContenuDocVente) dao.loadOneByNameQueries("YvsComContenuDocVente.findLastByArticle", new String[]{"article", "typeDoc"}, new Object[]{articles, Constantes.TYPE_FV});
        content = content != null ? content : new YvsComContenuDocVente();
        montant = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findCAByArticle", new String[]{"article", "type_doc"}, new Object[]{articles, Constantes.TYPE_FA});
        montant_avoir = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findCAByArticle", new String[]{"article", "type_doc"}, new Object[]{articles, Constantes.TYPE_FAA});

        ca_a = Math.round((montant != null ? montant : 0) - (montant_avoir != null ? montant_avoir : 0));
        content_achat = (YvsComContenuDocAchat) dao.loadOneByNameQueries("YvsComContenuDocAchat.findLastByArticle", new String[]{"article", "typeDoc"}, new Object[]{articles, Constantes.TYPE_FA});
        content_achat = content_achat != null ? content_achat : new YvsComContenuDocAchat();

        montant = (Double) dao.loadObjectByNameQueries("YvsProdDeclarationProduction.findCAByArticle", new String[]{"article"}, new Object[]{articles});

        ca_prod = Math.round(montant != null ? montant : 0);
        declaration = (YvsProdDeclarationProduction) dao.loadOneByNameQueries("YvsProdDeclarationProduction.findLastByArticle", new String[]{"article"}, new Object[]{articles});
        declaration = declaration != null ? declaration : new YvsProdDeclarationProduction();

    }

    public double getsockt(YvsBaseArticles a, YvsBaseDepots d) {
        return dao.stocks(a.getId(), 0, d.getId(), currentAgence.getId(), currentAgence.getSociete().getId(), new Date(), 0, 0);
    }

    public List<Object[]> getListArticle() {
        return listArticle;
    }

    public void setListArticle(List<Object[]> listArticle) {
        this.listArticle = listArticle;
    }

    public double getQteAchatByCondi(YvsBaseConditionnement condi) {
        Double qte_achat = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteVenduByArticle", new String[]{"conditionnement", "type_doc"}, new Object[]{condi, Constantes.TYPE_FA});
        qte_achat = qte_achat != null ? qte_achat : 0;
        return qte_achat;
    }

    public double getQteVenteByCondi(YvsBaseConditionnement condi) {
        Double qte_vendu = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteVenduByArticle", new String[]{"conditionnement", "type_doc"}, new Object[]{condi, Constantes.TYPE_FV});
        qte_vendu = qte_vendu != null ? qte_vendu : 0;
        return qte_vendu;
    }

    public double getQteProdByCondi(YvsBaseConditionnement condi) {
        Double qte_prod = (Double) dao.loadObjectByNameQueries("YvsProdDeclarationProduction.findQteProdByArticle", new String[]{"conditionnement"}, new Object[]{condi});
        qte_prod = qte_prod != null ? qte_prod : 0;
        return qte_vendu;
    }

    public long getQuantiteVenteByCondi(YvsBaseConditionnement condi) {
        Long qte = (Long) dao.loadObjectByNameQueries("YvsComContenuDocVente.findNbreDocByConditionnement", new String[]{"conditionnement"}, new Object[]{condi});
        qte = qte != null ? qte : 0;
        return qte;
    }

    public long getQuantiteAchatByCondi(YvsBaseConditionnement condi) {
        Long qte = (Long) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findNbreDocByConditionnement", new String[]{"conditionnement"}, new Object[]{condi});
        qte = qte != null ? qte : 0;
        return qte;
    }

    public long getQuantiteProdByCondi(YvsBaseConditionnement condi) {
        Long qte = (Long) dao.loadObjectByNameQueries("YvsProdDeclarationProduction.findNbreDocByConditionnement", new String[]{"conditionnement"}, new Object[]{condi});
        qte = qte != null ? qte : 0;
        return qte;
    }

    public void searchClient() {
        System.err.println("recherche du client, code =  " + code_client);
        if (code_client != null ? !code_client.isEmpty() : false) {
            listClient = dao.loadNameQueries("YvsComClient.findByName", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), code_client + "%"});
            if (listClient != null ? listClient.size() == 1 : false) {
                client = listClient.get(0);
                System.err.println("client=  " + client.getNom_prenom());
                ManagedBordStatistique m = (ManagedBordStatistique) giveManagedBean(ManagedBordStatistique.class);
                m.loadDataClient(client);
                synthese.setClient(UtilCom.buildSimpleBeanClient(client));
                getInfosClient();
                update("infos");
                update("liste");

            }
        } else {
            getErrorMessage("Veuillez entrer le code client");

        }
    }

    public void choixClient() {
        if (client != null ? client.getId() > 0 : false) {
            int index = listClient.indexOf(client);
            if (index > -1) {
                client = listClient.get(index);
                ManagedBordStatistique m = (ManagedBordStatistique) giveManagedBean(ManagedBordStatistique.class);
                m.loadDataClient(client);
                synthese.setClient(UtilCom.buildSimpleBeanClient(client));
                getInfosClient();
                update("infos");

            }
        }
    }

    public void loadOtherPrix() {
        if (articles != null ? articles.getId() > 0 : false) {
            articles.setArticlesPoints(dao.loadNameQueries("YvsBaseArticlePoint.findByArticle_", new String[]{"article"}, new Object[]{articles}));
        } else {
            getWarningMessage("Selectionner un article");
        }
    }

    public void getInfosClient() {
        champ = new String[]{"client"};
        val = new Object[]{new YvsComClient(synthese.getClient().getId())};
        nameQueri = "YvsComDocVentes.findFactureImpayeByClient";
        List<YvsComDocVentes> factures = dao.loadNameQueries(nameQueri, champ, val);

        synthese.setFactures(factures);
        synthese.setSoldeImpaye(0);
        for (YvsComDocVentes f : synthese.getFactures()) {
            setMontantTotalDoc(f);
            synthese.setSoldeImpaye(synthese.getSoldeImpaye() + f.getMontantResteApayer());
        }

        nameQueri = "YvsComContenuDocVente.findTTCByClient";
        champ = new String[]{"client"};
        val = new Object[]{new YvsComClient(synthese.getClient().getId())};
        Double ttc = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        synthese.setTtc(ttc);

        Long nbre = (Long) dao.loadObjectByNameQueries("YvsComDocVentes.countByClient", new String[]{"client"}, new Object[]{new YvsComClient(synthese.getClient().getId())});
        nbre = nbre != null ? nbre : 0;
        System.err.println("nombre  factures = " + nbre);
        synthese.setNombre_factures(nbre);

        nameQueri = "YvsComContenuDocVente.findTTCAvoirByClient";
        champ = new String[]{"client"};
        val = new Object[]{new YvsComClient(synthese.getClient().getId())};
        Double avoir = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);

        nameQueri = "YvsComptaCaissePieceVente.findSumAvoirByClient";
        champ = new String[]{"client"};
        val = new Object[]{new YvsComClient(synthese.getClient().getId())};
        Double aad = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        val = new Object[]{new YvsComClient(synthese.getClient().getId()), dateDebut, dateFin, yvs.util.Constantes.MOUV_CAISS_ENTREE.charAt(0)};
        Double aar = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        synthese.setSoldeAvoir((avoir != null ? avoir : 0) - ((aar != null ? aar : 0) - (aad != null ? aad : 0)));

        champ = new String[]{"client", "sens", "service"};
        val = new Object[]{new YvsComClient(synthese.getClient().getId()), true, true};
        Double cp = (Double) dao.loadObjectByNameQueries("YvsComCoutSupDocVente.findSumServiceByClient", champ, val);
        val = new Object[]{new YvsComClient(synthese.getClient().getId()), false, true};
        Double cm = (Double) dao.loadObjectByNameQueries("YvsComCoutSupDocVente.findSumServiceByClient", champ, val);
        double cs = (cp != null ? cp : 0) - (cm != null ? cm : 0);
        synthese.setSoldeImpayes((ttc != null ? ttc : 0) + cs);

        nameQueri = "YvsComptaCaissePieceVente.findSumByClientDate";
        champ = new String[]{"client", "dateFin", "mouvement"};
        val = new Object[]{new YvsComClient(synthese.getClient().getId()), dateFin, Constantes.MOUV_CAISS_SORTIE.charAt(0)};
        Double avd = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        val = new Object[]{new YvsComClient(synthese.getClient().getId()), dateFin, Constantes.MOUV_CAISS_ENTREE.charAt(0)};
        Double avr = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        synthese.setSoldeAvance((avr != null ? avr : 0) - (avd != null ? avd : 0));

        synthese.getCredits().clear();
        String query = "SELECT y.id FROM yvs_compta_credit_client y WHERE y.montant > COALESCE((SELECT SUM(r.valeur) FROM yvs_compta_reglement_credit_client r WHERE r.credit = y.id AND r.statut = 'P'), 0) AND y.statut = 'V' AND y.client = ? ";
        List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(synthese.getClient().getId(), 1)});
        if (ids != null ? !ids.isEmpty() : false) {
            synthese.setCredits(dao.loadNameQueries("YvsComptaCreditClient.findByIds", new String[]{"ids"}, new Object[]{ids}));
        }
        synthese.setSoldeCredit(0);
        for (YvsComptaCreditClient f : synthese.getCredits()) {
            synthese.setSoldeCredit(synthese.getSoldeCredit() + f.getReste());
        }

        YvsComDocVentes l = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.lastByClient", new String[]{"client"}, new Object[]{new YvsComClient(synthese.getClient().getId())});
        if (l != null ? l.getId() > 0 : false) {
            synthese.setLast(l);
            Double montant = 0.0;
            List<YvsComContenuDocVente> contenus = dao.loadNameQueries("YvsComContenuDocVente.findByFacture", new String[]{"docVente"}, new Object[]{l});
            for (YvsComContenuDocVente c : contenus) {
                montant += c.getPrixTotal();
            }

            System.err.println("montant total = " + montant);
            System.err.println("dernière facture = " + l.getEnteteDoc().getDateEntete());
            synthese.setMontant_last(montant);

        }

        update("table_client_imp_dash");
        update("zone_impayes");
        update("zone_detail");
    }

    public static String photo(YvsSocietes u) {
        if (u != null) {
            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
            String path = ext.getRealPath("resources/lymytz/documents/logos_doc/") + Initialisation.FILE_SEPARATOR + u.getLogo();
            if (new File(path).exists()) {
                return u.getLogo();
            }
        }
        return "default.png";
    }

    public void loadPath() {
        if (path != null ? path.trim().length() < 1 : true) {
            System.err.println("Initialisation.OS : " + Initialisation.OS);
            if (Initialisation.OS.contains("windows")) {
                path = "C:\\glassfish3\\glassfish\\domains\\domain1\\logs\\server.log";
            } else {
                path = "/opt/glassfish3/glassfish/domains/domain1/logs/server.log";
            }
        }
    }

    public void readFileLog() {
        try {
            logs = "";
            if (path != null ? path.trim().length() > 0 : false) {
                File file = new File(path.trim());
                if (file.exists()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        StringBuilder sb = new StringBuilder();
                        String line = br.readLine();
                        while (line != null) {
                            sb.append(line);
                            sb.append(System.lineSeparator());
                            line = br.readLine();
                        }
                        logs = sb.toString();
                    }
                }
            }
            update("txt_logs");
        } catch (IOException ex) {
            Logger.getLogger(ManagedAccueil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
