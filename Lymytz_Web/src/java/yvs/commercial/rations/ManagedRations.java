/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.rations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.production.UtilProd;
import yvs.commercial.UtilCom;
import yvs.commercial.achat.LotReception;
import yvs.commercial.achat.ManagedLotReception;
import yvs.commercial.creneau.Creneau;
import yvs.commercial.depot.ManagedDepot;
import yvs.commercial.objectifs.PeriodesObjectifs;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.YvsComParametreStock;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.commercial.ration.YvsComDocRation;
import yvs.entity.commercial.ration.YvsComParamRation;
import yvs.entity.commercial.ration.YvsComParamRationSuspension;
import yvs.entity.commercial.ration.YvsComPeriodeRation;
import yvs.entity.commercial.ration.YvsComRation;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.produits.YvsBaseArticles;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.parametrage.entrepot.Depots;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedRations extends Managed<DocRations, YvsComDocRation> implements Serializable {

    @ManagedProperty(value = "#{docRations}")
    private DocRations document = new DocRations();
    private ParamRations paramRation = new ParamRations();
    private List<YvsComDocRation> documents;
    private YvsComDocRation selectedDoc;
    private YvsComRation selectedRation = new YvsComRation();
    private Rations ration = new Rations();
    private List<YvsComParamRation> listParamRation;
    private List<YvsBaseArticles> articlesRation;
    private String[] libellesStock = new String[]{"Rations", "Paramètres"};
    private Date dateRation = new Date();

    private PeriodesObjectifs periode = new PeriodesObjectifs();
    private YvsComPeriodeRation selectedPeriode;
    private PaginatorResult<YvsComParamRation> pa = new PaginatorResult<>();

    private boolean initForm = true, addDateSearch;
    private long depotSearch, trancheSearch, periodeSearch;
    private String tiersSearch, articleSearch, numeroSearch, statutSearch, usersSearch;
    private String egaliteStatut = "=";
    private Date debutSearch = new Date(), finSearch = new Date();
    private Boolean clotureSearch;

    private List<YvsComRation> listDetailsRationsTiers;

    YvsComParametre currentParam;
    YvsComParametreStock currentParamStock;

    boolean force = false;

    public ManagedRations() {
        listParamRation = new ArrayList<>();
        documents = new ArrayList<>();
        articlesRation = new ArrayList<>();
        listDetailsRationsTiers = new ArrayList<>();
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public boolean isInitForm() {
        return initForm;
    }

    public String getEgaliteStatut() {
        return egaliteStatut;
    }

    public void setEgaliteStatut(String egaliteStatut) {
        this.egaliteStatut = egaliteStatut;
    }

    public void setInitForm(boolean initForm) {
        this.initForm = initForm;
    }

    public boolean isAddDateSearch() {
        return addDateSearch;
    }

    public void setAddDateSearch(boolean addDateSearch) {
        this.addDateSearch = addDateSearch;
    }

    public long getDepotSearch() {
        return depotSearch;
    }

    public void setDepotSearch(long depotSearch) {
        this.depotSearch = depotSearch;
    }

    public long getTrancheSearch() {
        return trancheSearch;
    }

    public void setTrancheSearch(long trancheSearch) {
        this.trancheSearch = trancheSearch;
    }

    public long getPeriodeSearch() {
        return periodeSearch;
    }

    public void setPeriodeSearch(long periodeSearch) {
        this.periodeSearch = periodeSearch;
    }

    public String getNumeroSearch() {
        return numeroSearch;
    }

    public void setNumeroSearch(String numeroSearch) {
        this.numeroSearch = numeroSearch;
    }

    public String getStatutSearch() {
        return statutSearch;
    }

    public void setStatutSearch(String statutSearch) {
        this.statutSearch = statutSearch;
    }

    public String getUsersSearch() {
        return usersSearch;
    }

    public void setUsersSearch(String usersSearch) {
        this.usersSearch = usersSearch;
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

    public Boolean getClotureSearch() {
        return clotureSearch;
    }

    public void setClotureSearch(Boolean clotureSearch) {
        this.clotureSearch = clotureSearch;
    }

    public List<YvsComDocRation> getTempList() {
        return tempList;
    }

    public void setTempList(List<YvsComDocRation> tempList) {
        this.tempList = tempList;
    }

    public YvsComPeriodeRation getSelectedPeriode() {
        return selectedPeriode;
    }

    public void setSelectedPeriode(YvsComPeriodeRation selectedPeriode) {
        this.selectedPeriode = selectedPeriode;
    }

    public String getArticleSearch() {
        return articleSearch;
    }

    public void setArticleSearch(String articleSearch) {
        this.articleSearch = articleSearch;
    }

    public String getTiersSearch() {
        return tiersSearch;
    }

    public void setTiersSearch(String tiersSearch) {
        this.tiersSearch = tiersSearch;
    }

    public Rations getRation() {
        return ration;
    }

    public void setRation(Rations ration) {
        this.ration = ration;
    }

    public String[] getLibellesStock() {
        return libellesStock;
    }

    public void setLibellesStock(String[] libellesStock) {
        this.libellesStock = libellesStock;
    }

    public List<YvsBaseArticles> getArticlesRation() {
        return articlesRation;
    }

    public void setArticlesRation(List<YvsBaseArticles> articlesRation) {
        this.articlesRation = articlesRation;
    }

    public ParamRations getParamRation() {
        return paramRation;
    }

    public void setParamRation(ParamRations paramRation) {
        this.paramRation = paramRation;
    }

    public List<YvsComParamRation> getListParamRation() {
        return listParamRation;
    }

    public void setListParamRation(List<YvsComParamRation> listParamRation) {
        this.listParamRation = listParamRation;
    }

    public YvsComRation getSelectedRation() {
        return selectedRation;
    }

    public void setSelectedRation(YvsComRation selectedRation) {
        this.selectedRation = selectedRation;
    }

    public PeriodesObjectifs getPeriode() {
        return periode;
    }

    public void setPeriode(PeriodesObjectifs periode) {
        this.periode = periode;
    }

    public List<YvsComDocRation> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComDocRation> documents) {
        this.documents = documents;
    }

    public YvsComDocRation getSelectedDoc() {
        return selectedDoc;
    }

    public void setSelectedDoc(YvsComDocRation selectedDoc) {
        this.selectedDoc = selectedDoc;
    }

    public PaginatorResult<YvsComParamRation> getPa() {
        return pa;
    }

    public void setPa(PaginatorResult<YvsComParamRation> pa) {
        this.pa = pa;
    }

    public DocRations getDocument() {
        return document;
    }

    public void setDocument(DocRations document) {
        this.document = document;
    }

    public List<YvsComRation> getListDetailsRationsTiers() {
        return listDetailsRationsTiers;
    }

    public void setListDetailsRationsTiers(List<YvsComRation> listDetailsRationsTiers) {
        this.listDetailsRationsTiers = listDetailsRationsTiers;
    }

    @Override
    public void resetFiche() {
        resetFiche(document);
        document.setNbrJrUsine(currentParam.getJourUsine());
        document.setDateFiche(new Date());
        document.setPeriode(new PeriodesObjectifs());
        document.setDepot(new Depots());
        document.setStatut(Constantes.STATUT_DOC_EDITABLE);
        document.setCreneauHoraire(new Creneau());
        document.setCloturer(false);
        chooseDate(new Date());
    }

    public Date getDateRation() {
        return dateRation;
    }

    public void setDateRation(Date dateRation) {
        this.dateRation = dateRation;
    }

    @Override
    public boolean controleFiche(DocRations bean) {
        if (bean.getDepot().getId() <= 0) {
            getErrorMessage("Vous devez Choisir un dépôt !");
            return false;
        }
        if (bean.getDateFiche() == null) {
            getErrorMessage("Vous devez entrer la date de la fiche !");
            return false;
        }
        if (bean.getCreneauHoraire().getId() <= 0) {
            getErrorMessage("Vous devez Choisir un créneau horaire !");
            return false;
        }
        if (bean.getPeriode().getId() <= 0) {
            getErrorMessage("Vous devez choisir une période !");
            return false;
        }
        //vérifie duplicate        
        String[] chp = new String[]{"date", "idCreneau", "idDepot"};
        Object[] val = new Object[]{bean.getDateFiche(), bean.getCreneauHoraire().getId(), bean.getDepot().getId()};
        Long id = (Long) dao.loadObjectByNameQueries("YvsComDocRation.findCOne", chp, val);
        id = (id == null) ? 0 : id;
        if (bean.getId() <= 0) {
            if (id != null ? id > 0 : false) {
                getErrorMessage("Une fiche à déjà été trouvé pour ce créneau horaire");
                return false;
            }
        } else {
            if (id != bean.getId()) {
                getErrorMessage("Une fiche à déjà été trouvé pour ce créneau horaire");
                return false;
            }
        }
        int ecart = currentParamStock.getDureeSaveRation();
        if (!verifyDate(bean.getDateFiche(), ecart, "")) {
            return false;
        }
        if (bean.getNumDoc() != null ? bean.getNumDoc().trim().length() < 1 : true) {
            String ref = genererReference(Constantes.TYPE_RA_NAME, bean.getDateFiche(), bean.getDepot().getId());
            if ((ref != null) ? ref.trim().equals("") : true) {
                return false;
            }
            bean.setNumDoc(ref);
        }
        return true;
    }

    @Override
    public void loadAll() {
        if (currentParam == null) {
            currentParam = (YvsComParametre) dao.loadOneByNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
        if (currentParamStock == null) {
            currentParamStock = (YvsComParametreStock) dao.loadOneByNameQueries("YvsComParametreStock.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        }
        if (document != null ? (document.getId() < 1 && document.getNbrJrUsine() < 1 && currentParam != null) : false) {
            document.setNbrJrUsine(currentParam.getJourUsine());
        }
        loadAllDocRation(true);
    }

    @Override
    public DocRations recopieView() {
        DocRations d = new DocRations();
        cloneObject(d, document);
        return d;
    }

    @Override
    public void populateView(DocRations bean) {
        cloneObject(document, bean);
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void updateBean(YvsComDocRation bean) {
        if (bean != null ? bean.getId() != null : false) {
            document = UtilCom.buildBeanDocRation(bean);
            document.getDepot().setTranches(dao.loadNameQueries("YvsComCreneauDepot.findByDepot", new String[]{"depot"}, new Object[]{new YvsBaseDepots(document.getDepot().getId())}));
            update("form_edit_doc_ration");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void selectOneLineDoc(YvsComDocRation d) {
        selectedDoc = d;
        selectedDoc.setListRations(new ArrayList<>(d.getListRations()));
        loadParamRation(true, true);
        calculQuantite(null, d);
        ration = new Rations();
        update("param_ration_zone_attrib_form");
    }

    public void chooseDate(SelectEvent ev) {
        if (ev != null) {
            Date d = (Date) ev.getObject();
            chooseDate(d);
        }
    }

    private void chooseDate(Date d) {
        if (d != null) {
            //trouve la période ration qui contient cette date
            YvsComPeriodeRation p = (YvsComPeriodeRation) dao.loadOneByNameQueries("YvsComPeriodeRation.findByContentDate", new String[]{"date", "societe"}, new Object[]{d, currentAgence.getSociete()});
            if (p != null) {
                document.setPeriode(UtilCom.buildBeanPeriode(p));
                ManagedParamRation service = (ManagedParamRation) giveManagedBean(ManagedParamRation.class);
                if (service != null) {
                    if (!service.getPeriodes().contains(p)) {
                        service.getPeriodes().add(0, p);
                    }
                }
            }
        }
    }

    public void chooseTranche() {
        if (document.getCreneauHoraire() != null ? document.getCreneauHoraire().getId() > 0 : false) {
            int idx = document.getDepot().getTranches().indexOf(new YvsComCreneauDepot(document.getCreneauHoraire().getId()));
            if (idx > -1) {
                YvsComCreneauDepot y = document.getDepot().getTranches().get(idx);
                document.setCreneauHoraire(UtilCom.buildBeanCreneau(y));
            }
        }
    }

    public void openViewPeriode() {
        ManagedParamRation service = (ManagedParamRation) giveManagedBean(ManagedParamRation.class);
        if (service != null) {
            selectedPeriode = new YvsComPeriodeRation();
            if (document.getPeriode().getId() == -1) {
                update("data_periode_param_ration");
                openDialog("dlgChoosePeriode");
            } else {
                if (document.getPeriode().getId() > 0) {
                    int idx = service.getPeriodes().indexOf(new YvsComPeriodeRation(document.getPeriode().getId()));
                    if (idx >= 0) {
                        document.setPeriode(UtilCom.buildBeanPeriode(service.getPeriodes().get(idx)));
                    }
                }
            }
        }
    }

    public void paginePeriodeOthers(ValueChangeEvent ev) {
        ManagedParamRation service = (ManagedParamRation) giveManagedBean(ManagedParamRation.class);
        if (service != null) {
            if (ev.getNewValue() != null) {
                long id = (long) ev.getNewValue();
                if (id > 0) {
                    int idx = service.getPeriodes().indexOf(new YvsComPeriodeRation(id));
                    if (idx >= 0) {
                        document.setPeriode(UtilCom.buildBeanPeriode(service.getPeriodes().get(idx)));
                    }
                } else if (id == -1) {
                    //précédent
                    service.loadAllPeriode(false, false);
                } else if (id == -2) {
                    //suivant
                    service.loadAllPeriode(false, true);
                }
            }
        }
    }

    public void pagineDepotOthers(ValueChangeEvent ev) {
        ManagedDepot service = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (service != null) {
            if (ev.getNewValue() != null) {
                long id = (long) ev.getNewValue();
                if (id > 0) {
                    int idx = service.getDepots().indexOf(new YvsBaseDepots(id));
                    if (idx >= 0) {
                        document.setDepot(UtilCom.buildBeanDepot(service.getDepots().get(idx)));
                        document.getDepot().setTranches(dao.loadNameQueries("YvsComCreneauDepot.findByDepot", new String[]{"depot"}, new Object[]{service.getDepots().get(idx)}));
                    }
                } else if (id == -1) {
                    //précédent
                    service.loadAllDepot(false, false);
                } else if (id == -2) {
                    //suivant
                    service.loadAllDepot(true, false);
                }
            }
        }
    }

    public void addParamTiers() {
        ParametreRequete p = new ParametreRequete("UPPER(y.personnel.codeTiers)", "code", null, "=", "AND");
        if (tiersSearch != null ? tiersSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "code", tiersSearch.toUpperCase(), "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.personnel.codeTiers)", "code", tiersSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.personnel.nom)", "code", tiersSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.personnel.prenom)", "code", tiersSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        pa.addParam(p);
        loadParamRation(true, true);
    }

    public void addParamArticle() {
        ParametreRequete p = new ParametreRequete("UPPER(y.article.refArt)", "article", null, "=", "AND");
        if (articleSearch != null ? articleSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "code", articleSearch.toUpperCase(), "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "article", articleSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "article", articleSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        pa.addParam(p);
        loadParamRation(true, true);
    }

    public List<YvsComParamRation> loadParamRation(boolean init, boolean avancer) {
        if (selectedDoc != null ? selectedDoc.getId() > 0 : false) {
            Calendar c = Calendar.getInstance();
            c.setTime(selectedDoc.getDateFiche());
            c.add(Calendar.DATE, selectedDoc.getNbrJrUsine() - 1);
            List<Long> ids = dao.loadNameQueries("YvsComParamRationSuspension.findIdPersonnelByDates", new String[]{"societe", "date"}, new Object[]{currentAgence.getSociete(), selectedDoc.getDateFiche()});
            if (ids.isEmpty()) {
                ids.add(-1L);
            }
            pa.addParam(new ParametreRequete("y.datePriseEffet", "dateEffet", selectedDoc.getDateFiche(), "<=", "AND"));
            pa.addParam(new ParametreRequete("y.id", "ids", ids, "NOT IN", "AND"));
        }
        pa.addParam(new ParametreRequete("y.actif", "actif", true, "=", "AND"));
        pa.addParam(new ParametreRequete("y.personnel.actif", "isPersonnel", true, "=", "AND"));
        pa.addParam(new ParametreRequete("y.personnel.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        listParamRation = pa.executeDynamicQuery("YvsComParamRation", "y.personnel.codeTiers, y.personnel.nom, y.personnel.prenom", avancer, init, pa.getRows(), dao);
        loadDataRation(selectedDoc);
        if (selectedDoc != null ? !selectedDoc.getListRations().isEmpty() : false) {
            selectedRation = selectedDoc.getListRations().get(0);
        } else {
            selectedRation = new YvsComRation();
        }
        return listParamRation;
    }

    private void loadDataRation(YvsComDocRation doc) {
        //charge les rations
        if (doc != null) {
            YvsComRation ra;
            doc.setQteDistribue(0);
            doc.getListRations().clear();
            for (YvsComParamRation pRation : listParamRation) {
                ra = (YvsComRation) dao.loadOneByNameQueries("YvsComRation.findOneDay", new String[]{"personnel", "periode", "article", "jour"}, new Object[]{pRation.getPersonnel(), doc.getPeriode(), pRation.getArticle(), doc.getDateFiche()});
                //calculer la quantité total de la période et la quantité djà prise    
                if (ra != null ? ra.getId() > 0 : false) {
                    ra.setLast(ra.getDocRation());
                    doc.getListRations().add(0, ra);
                    doc.setQteDistribue(doc.getQteDistribue() + ra.getQuantite());
                } else {
                    ra = buildOneRation(pRation);
                    if (pRation.getProportionnel()) {
                        double taux = 1;
                        if (doc.getNbrJrUsine() < pRation.getPeriode()) {
                            taux = (double) doc.getNbrJrUsine() / (double) pRation.getPeriode();
                        }
                        ra.setQuantite(Constantes.arrondi((double) pRation.getQuantite() * taux, 0));
                    }
                    doc.getListRations().add(ra);
                }
                ra.setQtePrise(ra.getQtePeriode(dao));
//                ra.setQteMax(ra.getMaxPeriode(dao));
                ra.setQteMax(getQuantiteMax(pRation));
            }
        }
    }

    private double getQuantiteMax(YvsComParamRation pRation) {
        if (pRation != null ? pRation.getId() > 0 : false) {
            //Nombre de jour usine
//            Integer jourUsine = (Integer) dao.loadObjectByNameQueries("YvsComParametre.findJourUsine", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            Integer jourUsine = currentParam.getJourUsine();
            jourUsine = (jourUsine != null) ? jourUsine : 30;
            Double re = (Double) dao.loadObjectByNameQueries("YvsComParamRation.findQteByTiersArticle", new String[]{"personnel", "article"}, new Object[]{pRation.getPersonnel(), pRation.getArticle()});
            re = (re != null) ? re : 0;
            //Récupère la durée des suspension du tiers
            List<YvsComParamRationSuspension> list = dao.loadNameQueries("YvsComParamRationSuspension.findByPersonnelDates", new String[]{"personnel", "debutSuspension", "finSuspension"}, new Object[]{pRation, selectedDoc.getPeriode().getDateDebut(), selectedDoc.getPeriode().getFin()});
            if (list != null ? (!list.isEmpty() && jourUsine == 30) : false) {
                //ration de la période suspendu
                return 0;
            }
            if (list != null ? (!list.isEmpty()) : false) {
                //qte proportionnelle 
                Double qte = yvs.dao.salaire.service.Constantes.arrondiA0Chiffre(re * jourUsine / 30);
                for (YvsComParamRationSuspension cr : list) {
                    System.err.println(" Nombre jour suspension " + yvs.dao.salaire.service.Constantes.calculNbDay(cr.getDebutSuspension(), cr.getFinSuspension()));
                    re = re - ((yvs.dao.salaire.service.Constantes.calculNbDay(cr.getDebutSuspension(), cr.getFinSuspension()) + 1) * qte);
                }
            }
            return re;
        }
        return 0;
    }

    private YvsComRation buildOneRation(YvsComParamRation p) {
        YvsComRation re = new YvsComRation(-p.getId());
        re.setArticle(p.getArticle());
        re.setAuthor(currentUser);
        re.setConditionnement(p.getConditionnement());
        re.setDocRation(selectedDoc);
        re.setQuantite(p.getQuantite());
        re.setPersonnel(p.getPersonnel());
        re.setActif(p.getActif());
        return re;
    }

    public void selectOneLineTiers(YvsComParamRation p) {
        paramRation = new ParamRations();
        paramRation.getPersonnel().setActif(p.getPersonnel().getActif());
        paramRation.getPersonnel().setCodeSecret(p.getPersonnel().getCodeTiers());
        paramRation.getPersonnel().setId(p.getPersonnel().getId());
        paramRation.getPersonnel().setNom(p.getPersonnel().getNom());
        paramRation.getPersonnel().setPrenom(p.getPersonnel().getPrenom());
        paramRation.setArticle(UtilProd.buildSimpleBeanArticles(p.getArticle()));
        paramRation.setId(p.getId());
        paramRation.setQuantite(p.getQuantite());
        paramRation.setActif(p.getActif());
        update("param_ration_zone_form");
    }

    public void selectPeriode() {
        if (selectedPeriode != null ? selectedPeriode.getId() > 0 : false) {
            document.setPeriode(UtilCom.buildBeanPeriode(selectedPeriode));
        } else {
            document.setPeriode(new PeriodesObjectifs());
        }
        update("select_periode_dpc_ration");
    }

//    private YvsComParamRation buildOneParamRation(YvsBaseTiers t) {
//        YvsComParamRation p = new YvsComParamRation();
//        p.setPersonnel(t);
//        p.setId(-t.getId());
//        return p;
//    }
    @Override
    public boolean saveNew() {
        if (!autoriser("ra_save_fiche")) {
            openNotAcces();
            return false;
        }
        if (controleFiche(document)) {
            selectedDoc = UtilCom.buildDocRation(document, currentUser);
            selectedDoc.setDateUpdate(new Date());
            selectedDoc.setNew_(true);
            if (selectedDoc.getId() > 0) {
                dao.update(selectedDoc);
                int idx = documents.indexOf(selectedDoc);
                if (idx > -1) {
                    documents.set(idx, selectedDoc);
                }
            } else {
                selectedDoc.setDateSave(new Date());
                selectedDoc.setId(null);
                selectedDoc = (YvsComDocRation) dao.save1(selectedDoc);
                document.setId(selectedDoc.getId());
                documents.add(0, selectedDoc);
                resetFiche();
            }
            succes();
            selectOneLineDoc(selectedDoc);
            update("form_edit_doc_ration");
            update("table_documents_rations");
        }
        return true;
    }

    public void chooseConditionnement() {
        if (selectedRation != null ? selectedRation.getId() > 0 : false) {
            int idx = selectedRation.getArticle().getConditionnements().indexOf(selectedRation.getConditionnement());
            if (idx > -1) {
                YvsBaseConditionnement y = selectedRation.getArticle().getConditionnements().get(idx);
                selectedRation.setConditionnement(y);
            }
        }
    }

    public void updateConditionnement() {
        if (selectedRation != null ? selectedRation.getId() > 0 : false) {
            dao.update(selectedRation);
            int idx = selectedDoc.getListRations().indexOf(selectedRation);
            if (idx > -1) {
                selectedDoc.getListRations().set(idx, selectedRation);
                update("table_param_attrib_rations");
            }
            succes();
        }
    }

    @Override
    public void onSelectObject(YvsComDocRation y) {
        selectOneLineDoc(y);
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        onSelectObject((YvsComDocRation) ev.getObject());
    }
    List<YvsComDocRation> tempList;

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        tempList = paginator.parcoursDynamicData("YvsComDocRation", "y", "y.dateFiche DESC, y.numDoc DESC", getOffset(), dao);
        if (!tempList.isEmpty()) {
            selectOneLineDoc(tempList.get(0));
        }
    }

    private void addParamDoit() {
        if (!autoriser("ra_view_cloturer")) {
            paginator.addParam(new ParametreRequete("y.cloturer", "cloturer", false, "=", "AND"));
        }
        if (!autoriser("ra_view_all_societe")) {
            controlListAgence();
            paginator.addParam(new ParametreRequete("y.depot.agence.id", "agences", listIdAgences, "IN", "AND"));
        } else {
            paginator.addParam(new ParametreRequete("y.depot.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        }
        if (!autoriser("ra_view_depot")) {
            //dépôt où je suis planifié
            List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdCrenoDepotByUsersAndPlanActif", new String[]{"users"}, new Object[]{currentUser.getUsers()});
            ManagedDepot s = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            ids.add(-1L);
            paginator.addParam(new ParametreRequete("y.creneauHoraire.id", "crenos", ids, "IN", "AND"));
        }
        if (!autoriser("ra_view_all")) {
            int limit = (currentParamStock != null) ? ((currentParamStock.getJourAnterieur() != null) ? currentParamStock.getJourAnterieur() : 0) : 0;
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, -limit);
            Date dateDebut = c.getTime();
            Date dateFin = new Date();
            paginator.addParam(new ParametreRequete("y.dateFiche", "date", dateDebut, dateFin, "BETWEEN", "AND"));
        }
        if (!autoriser("ra_view_all_historique")) {
            //restriction de date où je suis planifié
            Date date = new Date();
            Date ier = Constantes.getPreviewDate(date);
            paginator.addParam(new ParametreRequete("y.dateFiche", "date", ier, date, "BETWEEN", "AND"));
        }
    }

    public void loadAllDocRation(boolean avancer) {
        //charge les droits    
        addParamDoit();
        documents = paginator.executeDynamicQuery("YvsComDocRation", "y.dateFiche DESC, y.numDoc DESC", avancer, initForm, (int) imax, dao);
    }

    public void calculQuantite(YvsBaseArticles art, YvsComDocRation y) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPrecedent() != null ? y.getPrecedent().getId() < 1 : true) {
                Date date = new Date();
                if (y != null ? y.getPeriode() != null : false) {
                    date = y.getPeriode().getDateDebut();
                }
                champ = new String[]{"date"};
                val = new Object[]{date};
                nameQueri = "YvsComDocRation.findPrecedent";
                y.setPrecedent((YvsComDocRation) dao.loadOneByNameQueries(nameQueri, champ, val));
            }
            if (art != null ? (art.getId() != null ? art.getId() < 1 : true) : true) {
                articlesRation = dao.loadNameQueries("YvsComParamRation.findArticle", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                for (YvsBaseArticles a : articlesRation) {
                    calculQuantite(a, y);
                }
            } else {
                Double re = (Double) dao.loadObjectByNameQueries("YvsComRation.findSumByFicheArticle", new String[]{"article", "docRation"}, new Object[]{art, y});
                art.setQuantite(re != null ? re : 0);
                List<Long> ids = dao.loadNameQueries("YvsComParamRationSuspension.findIdPersonnelByDate", new String[]{"societe", "date"}, new Object[]{currentAgence.getSociete(), y.getPeriode().getFin()});
                if (ids.isEmpty()) {
                    ids.add(-1L);
                }
                champ = new String[]{"societe", "article", "date", "ids"};
                val = new Object[]{currentAgence.getSociete(), art, y.getPeriode().getFin(), ids};
                nameQueri = "YvsComParamRation.findByArticleDates";
                List<YvsComParamRation> list = dao.loadNameQueries(nameQueri, champ, val);
                for (YvsComParamRation a : list) {
                    if (a.getActif()) {
                        double quantite = a.getQuantite();
                        if (a.getProportionnel()) {
                            double taux = 1;
                            if (y.getNbrJrUsine() < a.getPeriode()) {
                                taux = (double) y.getNbrJrUsine() / (double) a.getPeriode();
                            }
                            quantite = Constantes.arrondi((double) a.getQuantite() * taux, 0);
                        }
                        art.setQuantitePrevu(art.getQuantitePrevu() + quantite);
                    } else {
                        YvsComRation ra = (YvsComRation) dao.loadOneByNameQueries("YvsComRation.findOne", new String[]{"personnel", "periode", "article"}, new Object[]{a.getPersonnel(), y.getPeriode(), a.getArticle()});
                        if (ra != null ? ra.getId() > 0 : false) {
                            double quantite = a.getQuantite();
                            if (a.getProportionnel()) {
                                double taux = 1;
                                if (y.getNbrJrUsine() < a.getPeriode()) {
                                    taux = (double) y.getNbrJrUsine() / (double) a.getPeriode();
                                }
                                quantite = Constantes.arrondi((double) a.getQuantite() * taux, 0);
                            }
                            art.setQuantitePrevu(art.getQuantitePrevu() + quantite);
                        }
                    }
                }
                double quantitePrec = 0, quantitePrevuPrec = 0;
                if (y.getPrecedent() != null ? y.getPrecedent().getId() > 0 : false) {
                    re = (Double) dao.loadObjectByNameQueries("YvsComRation.findSumByFicheArticle", new String[]{"article", "docRation"}, new Object[]{art, y.getPrecedent()});
                    quantitePrec = (re != null ? re : 0);
                    ids = dao.loadNameQueries("YvsComParamRationSuspension.findIdPersonnelByDate", new String[]{"societe", "date"}, new Object[]{currentAgence.getSociete(), y.getPrecedent().getPeriode().getFin()});
                    if (ids.isEmpty()) {
                        ids.add(-1L);
                    }
                    val = new Object[]{currentAgence.getSociete(), art, y.getPrecedent().getPeriode().getFin(), ids};
                    list = dao.loadNameQueries(nameQueri, champ, val);
                    for (YvsComParamRation a : list) {
                        if (a.getActif()) {
                            double quantite = a.getQuantite();
                            if (a.getProportionnel()) {
                                double taux = 1;
                                if (y.getPrecedent().getNbrJrUsine() < a.getPeriode()) {
                                    taux = (double) y.getPrecedent().getNbrJrUsine() / (double) a.getPeriode();
                                }
                                quantite = Constantes.arrondi((double) a.getQuantite() * taux, 0);
                            }
                            quantitePrevuPrec = quantitePrevuPrec + quantite;
                        } else {
                            YvsComRation ra = (YvsComRation) dao.loadOneByNameQueries("YvsComRation.findOne", new String[]{"personnel", "periode", "article"}, new Object[]{a.getPersonnel(), y.getPrecedent().getPeriode(), a.getArticle()});
                            if (ra != null ? ra.getId() > 0 : false) {
                                double quantite = a.getQuantite();
                                if (a.getProportionnel()) {
                                    double taux = 1;
                                    if (y.getPrecedent().getNbrJrUsine() < a.getPeriode()) {
                                        taux = (double) y.getPrecedent().getNbrJrUsine() / (double) a.getPeriode();
                                    }
                                    quantite = Constantes.arrondi((double) a.getQuantite() * taux, 0);
                                }
                                quantitePrevuPrec = quantitePrevuPrec + quantite;
                            }
                        }
                    }
                }
                art.setQuantitePrec(quantitePrec);
                art.setQuantitePrevuPrec(quantitePrevuPrec);

                int idx = articlesRation.indexOf(art);
                if (idx > -1) {
                    articlesRation.set(idx, art);
                }
            }
            update("data_article_doc_ration");
        }
    }

    public void paginer(boolean avance) {
        initForm = false;
        loadAllDocRation(avance);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        initForm = true;
        loadAllDocRation(true);
    }

    public void choosePaginatorRation(ValueChangeEvent ev) {
        long v;
        try {
            v = (long) ev.getNewValue();
        } catch (Exception ex) {
            v = (int) ev.getNewValue();
        }
        pa.setRows((int) v);
        loadParamRation(true, true);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        initForm = true;
        loadAllDocRation(true);
    }

    public void gotoPagePaginatorRation() {
        pa.gotoPage((int) pa.getRows());
        loadParamRation(true, true);
    }

    private boolean controleSaveRation() {
        if (selectedDoc == null) {
            getErrorMessage("Aucune fiche de ration n'a été sélectionné !");
            return false;
        }
        if (selectedRation == null) {
            getErrorMessage("Aucune ligne de ration n'a été selectionné !");
            return false;
        }
        if (selectedRation.getId() != null ? selectedRation.getId() > 0 : false) {
            getErrorMessage("Cette ration est déja attribuée !");
            return false;
        }
        if (selectedDoc.getStatut() != Constantes.STATUT_DOC_VALIDE) {
            getErrorMessage("La fiche de ration n'a pas encore été validé !");
            return false;
        }
        if (selectedDoc.getCloturer()) {
            getErrorMessage("La fiche de ration est cloturée !");
            return false;
        }
        if (selectedRation.getLast() != null ? selectedRation.getLast().getId() > 0 : false) {
            if (!selectedDoc.getId().equals(selectedRation.getLast().getId())) {
                getErrorMessage("Ce personnel a deja pris la ration dans cette journée!");
                return false;
            }
        }
        if ((dateRation != null) ? (dateRation.after(new Date())) : true) {
            getErrorMessage("Cette date est invalide !");
            return false;
        }
        int ecart = currentParamStock != null ? currentParamStock.getMargeTimeFicheRation() : -1;
        if (ecart >= 0) {
            Date begin = timestampToDate(selectedDoc.getDateFiche(), selectedDoc.getCreneauHoraire().getTranche().getHeureDebut());
            Date end = timestampToDate(selectedDoc.getDateFiche(), selectedDoc.getCreneauHoraire().getTranche().getHeureFin());
            if (end.before(begin)) {
                Calendar debut = dateToCalendar(end);
                debut.add(Calendar.DATE, 1);
                begin = debut.getTime();
            } else {
                begin = end;
            }
            Calendar debut = dateToCalendar(begin);
            debut.add(Calendar.HOUR_OF_DAY, currentParamStock.getMargeTimeFicheRation());
            begin = debut.getTime();
            if ((begin != null) ? (begin.before(new Date())) : true) {
                getErrorMessage("Vous ne pouvez plus prendre dans cette fiche! (delai dépassé)");
                return false;
            }
        }
        if (selectedRation.getPersonnel() != null ? (selectedRation.getPersonnel().getCodeRation() != null
                ? selectedRation.getPersonnel().getCodeRation().trim().length() < 1 : true) : true) {
            getErrorMessage("Ce personnel n'a pas de code ration !");
            return false;
        }
        //controle le stock
        String result = controleStock(selectedRation.getArticle().getId(), selectedRation.getConditionnement().getId(), selectedRation.getDocRation().getDepot().getId(), 0L, selectedRation.getQuantite(), selectedRation.getQuantite(), "INSERT", "S", dateRation, (selectedRation.getLot() != null ? selectedRation.getLot().getId() : 0));
        if (result != null) {
            getErrorMessage("L'article '" + selectedRation.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou entrainera un stock négatif à la date " + result);
            return false;
        }
        //contrôle la quantité déjà prise par le tiers sur la période
        Double qte = (Double) dao.loadObjectByNameQueries("YvsComRation.findSumByPeriode", new String[]{"periode", "tiers", "article"}, new Object[]{selectedDoc.getPeriode(), selectedRation.getPersonnel(), selectedRation.getArticle()});
        qte = (qte != null) ? qte : 0;
//            if (selectedRation.getMaxPeriode(dao) <= qte && qte > 0) {
//                getErrorMessage("Vous avez atteint votre quotas périodique de rations !");
//                return;
//            }
        if (selectedRation.getQteMax() < (qte + selectedRation.getQuantite())) {
            getErrorMessage("Vous avez atteint votre quotas périodique de rations !");
            return false;
        }
        if (ration != null ? (ration.getCodeRation() != null ? ration.getCodeRation().trim().length() < 1 : true) : true) {
            getErrorMessage("Vous devez precisez le code de ration du tiers !");
            return false;
        }
        if (!ration.getCodeRation().equals(selectedRation.getPersonnel().getCodeRation())) {
            getErrorMessage("Ce code de ration ne correspond pas à celui du tiers spécifier !");
            return false;
        }
        if (ration.getArticle().isRequiereLot()) {
            if ((ration.getLot() != null ? ration.getLot().getId() < 1 : true) && (ration.getLots() != null ? ration.getLots().isEmpty() : true)) {
                getErrorMessage("Un numéro de lot est requis pour cet article dans le dépôt");
                return false;
            }
        }
        if (ration.getArticle().isRequiereLot()) {
            if ((ration.getLot() != null ? ration.getLot().getId() < 1 : true) && (ration.getLots() != null ? ration.getLots().isEmpty() : true)) {
                getErrorMessage("Un numéro de lot est requis pour cet article dans le dépôt");
                return false;
            }
        }
//        if (selectedRation.getQuantite() > ration.getStock()) {
//            getErrorMessage("Stock insuffisant !");
//            return false;
//        }
        return verifyTranche(selectedDoc.getCreneauHoraire().getTranche(), selectedDoc.getDepot(), dateRation);
    }

    public void validerOrderByForce() {
        this.force = true;
        if (ration.getLots().size() > 1) {
            openDialog("dlgListLotReception");
        } else {
            validAndSaveRation(this.force);
        }
    }

    public void validAndSaveRation() {
        this.force = false;
        if (ration.getLots().size() > 1) {
            openDialog("dlgListLotReception");
        } else {
            validAndSaveRation(this.force);
        }
    }

    public void validAndSaveRation(boolean force) {
        if (!autoriser("ra_distribuer")) {
            openNotAcces();
            return;
        }
        if (controleSaveRation()) {
            boolean exist_inventaire = false;
            try {
                // Vérifié qu'aucun document d'inventaire n'exite après cette date
                boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
                exist_inventaire = !controleInventaire(selectedRation.getDocRation().getDepot().getId(), dateRation, selectedRation.getDocRation().getCreneauHoraire().getTranche().getId(), !gescom_update_stock_after_valide);
                if (exist_inventaire) {
                    if (!gescom_update_stock_after_valide) {
                        return;
                    } else if (!force) {
                        openDialog("dlgConfirmChangeInventaireByValid");
                        return;
                    }
                }
            } catch (Exception ex) {
                getException("validAndSaveRation", ex);
            }
            selectedRation.setStock_(ration.getStock());
            double quantite = 0;
            selectedRation.setAuthor(currentUser);
            selectedRation.setDocRation(selectedDoc);
            selectedRation.setDateRation(dateRation);
            if (ration.getLots().size() > 1) {
                for (YvsComLotReception l : ration.getLots()) {
                    selectedRation.setLot(l);
                    selectedRation.setQuantite(l.getQuantitee());
                    selectedRation.setDateSave(new Date());
                    selectedRation.setDateUpdate(new Date());
                    selectedRation.setId(null);
                    selectedRation = (YvsComRation) dao.save1(selectedRation);
                    quantite += selectedRation.getQuantite();
                }
                selectedRation.setQuantite(quantite);
                succes();
            } else {
                quantite = selectedRation.getQuantite();
                if (ration.getLot() != null ? ration.getLot().getId() > 0 : false) {
                    selectedRation.setLot(UtilCom.buildLotReception(ration.getLot(), currentAgence, currentUser));
                }
                selectedRation.setDateSave(new Date());
                selectedRation.setDateUpdate(new Date());
                selectedRation.setId(null);
                selectedRation = (YvsComRation) dao.save1(selectedRation);
                succes();
            }
            calculQuantite(selectedRation.getArticle(), selectedDoc);
            try {
                if (exist_inventaire) {
                    YvsComDocStocks inventaire = dao.lastInventaire(selectedRation.getDocRation().getDepot().getId(), dateRation, selectedRation.getDocRation().getCreneauHoraire().getTranche().getId());
                    if (inventaire != null ? inventaire.getId() > 0 : false) {
                        majInventaire(inventaire, selectedRation.getArticle(), selectedRation.getConditionnement(), quantite, Constantes.MOUV_SORTIE);
                    }
                }
            } catch (Exception ex) {
                getException("validAndSaveRation", ex);
            }
            update("table_param_attrib_rations");
        }
    }

    public void chooseLot() {
        if ((ration.getLot() != null) ? ration.getLot().getId() > 0 : false) {
            ManagedLotReception m = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
            if (m != null) {
                int idx = m.getLots().indexOf(new YvsComLotReception(ration.getLot().getId()));
                if (idx > -1) {
                    ration.setLot(UtilCom.buildBeanLotReception(m.getLots().get(idx)));
                }
            }
        }
    }

    public void selectLineRation(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectedRation = (YvsComRation) ev.getObject();
            if (selectedDoc != null) {
                if (dateRation != null) {
                    YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", new String[]{"article", "depot"}, new Object[]{selectedRation.getArticle(), selectedDoc.getDepot()});
                    if (y != null ? y.getId() < 1 : true) {
                        getErrorMessage("Ce dépot ne possède pas cet article... Contactez votre administrateur");
                        return;
                    }
                    ration = UtilCom.buildBeanRations(selectedRation);
                    ration.getArticle().setRequiereLot(y.getRequiereLot());
                    double stock = dao.stocksReel(selectedRation.getArticle().getId(), 0, selectedDoc.getDepot().getId(), 0, 0, dateRation, selectedRation.getConditionnement().getId(), (selectedRation.getLot() != null ? selectedRation.getLot().getId() : 0));
                    ration.setStock(stock);
                    if (y.getRequiereLot()) {
                        ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                        if (w != null) {
                            ration.setLots(w.loadList(selectedDoc.getDepot().getId(), ration.getConditionnement().getId(), selectedDoc.getDateFiche(), ration.getQuantite(), stock));
                            if (ration.getLots().size() == 1) {
                                ration.setLot(UtilCom.buildBeanLotReception(ration.getLots().get(0)));
                            } else {
                                ration.setLot(new LotReception(0, ration.getLots().size() + " Lots"));
                            }
                            update("data-ration_require_lot");
                        }
                    } else {
                    }
                } else {
                    getErrorMessage("Aucune date n'a été selectionné !");
                }
            } else {
                getErrorMessage("Veullez selectionné une fiche de ration !");
            }
        }
    }

    public void openToAnnulReceptionRation(YvsComRation ra) {
        if (!autoriser("ra_attribuer")) {
            openNotAcces();
            return;
        }
        selectedRation = ra;
        openDialog("dlgDelRation");
        update("table_param_attrib_rations");
    }

    public void annulReceptionRation() {
        if (selectedRation != null) {
            if (selectedRation.getId() > 0) {
                try {
                    // Vérifié qu'aucun document d'inventaire n'exite après cette date
                    boolean exist_inventaire = !controleInventaire(selectedDoc.getDepot().getId(), selectedDoc.getDateFiche(), selectedDoc.getCreneauHoraire().getTranche().getId());
                    if (exist_inventaire) {
                        return;
                    }
                } catch (Exception ex) {
                    getException("annulReceptionRation", ex);
                }
                try {
                    dao.delete(new YvsComRation(selectedRation.getId()));
                    selectedRation.setId(-selectedRation.getId());
                    calculQuantite(selectedRation.getArticle(), selectedDoc);
                    update("table_param_attrib_rations");
                } catch (Exception ex) {
                    getErrorMessage("Impossible de supprimer !");
                    getException("Lymytz Error >>> ", ex);
                }
            }
        }
    }

    public void openToSuspendRationTiers(YvsComRation ra) {
        if (!autoriser("ra_suspendre")) {
            openNotAcces();
            return;
        }
        selectedRation = ra;
        openDialog("dlgConfirmSuspendRation");
    }

    public void openToDisableRationTiers(YvsComRation ra) {
        if (!autoriser("ra_activer")) {
            openNotAcces();
            return;
        }
        selectedRation = ra;
        if (ra.isActif()) {
            openDialog("dlgDisRation");
        } else {
            disableRationTiers();
        }
    }

    public void suspensionRationTiers() {
        ManagedParamRation w = (ManagedParamRation) giveManagedBean(ManagedParamRation.class);
        if (w != null ? (selectedRation != null ? selectedRation.getPersonnel() != null : false) : false) {
            YvsComParamRation pra = (YvsComParamRation) dao.loadOneByNameQueries("YvsComParamRation.findOne", new String[]{"personnel", "article", "conditionnement"}, new Object[]{selectedRation.getPersonnel(), selectedRation.getArticle(), selectedRation.getConditionnement()});
            if (pra != null) {
                Calendar c = Calendar.getInstance();
                c.setTime(selectedDoc.getDateFiche());
                c.add(Calendar.DATE, selectedDoc.getNbrJrUsine() - 1);
                ParamRationSuspension suspension = new ParamRationSuspension(selectedDoc.getDateFiche(), c.getTime());
                YvsComParamRationSuspension y = w.saveNewSuspension(pra, suspension);
                if (y != null ? y.getId() > 0 : false) {
                    selectedDoc.getListRations().remove(selectedRation);
                    if (ration.getId() == selectedRation.getId()) {
                        ration = new Rations();
                        update("form_ration_zone_attrib_form");
                    }
                    selectedRation = new YvsComRation();
                    update("table_param_attrib_rations");
                    succes();
                }
            }
        }
    }

    public void disableRationTiers() {
        YvsComParamRation pra = (YvsComParamRation) dao.loadOneByNameQueries("YvsComParamRation.findOne", new String[]{"personnel", "article", "conditionnement"}, new Object[]{selectedRation.getPersonnel(), selectedRation.getArticle(), selectedRation.getConditionnement()});
        if (pra != null) {
            pra.setActif(!pra.getActif());
            selectedRation.setActif(pra.getActif());
            dao.update(pra);
            int idx = selectedDoc.getListRations().indexOf(selectedRation);
            if (idx > -1) {
                selectedDoc.getListRations().set(idx, selectedRation);
            }
            calculQuantite(pra.getArticle(), selectedDoc);
            update("table_param_attrib_rations");
        }
    }

    public void changeCalculPr(YvsComRation y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (!autoriser("recalcul_pr")) {
                    openNotAcces();;
                    return;
                }
                y.setCalculPr(!y.getCalculPr());
                y.setAuthor(currentUser);
                y.setDateUpdate(new Date());
                dao.update(y);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!");
            getException("ManagedRations (changeCalculPr)", ex);
        }
    }

    public void changeStatutDocRation() {
        if (selectedDoc != null) {
            if (!autoriser(selectedDoc.getStatut().equals(Constantes.STATUT_DOC_VALIDE) ? "ra_annuler_fiche" : (selectedDoc.getStatut().equals(Constantes.STATUT_DOC_ENCOUR) ? "ra_valider_fiche" : "ra_controle_fiche"))) {
                openNotAcces();
                return;
            }
            try {
                // Vérifié qu'aucun document d'inventaire n'exite après cette date
                boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
                boolean exist_inventaire = !controleInventaire(selectedDoc.getDepot().getId(), selectedDoc.getDateFiche(), selectedDoc.getCreneauHoraire().getTranche().getId(), !gescom_update_stock_after_valide);
                if (exist_inventaire && !gescom_update_stock_after_valide) {
                    return;
                }
            } catch (Exception ex) {
                getException("changeStatutDocRation", ex);
            }
            if (selectedDoc.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                Long nb = (Long) dao.loadObjectByNameQueries("YvsComRation.findByFicheC", new String[]{"docRation"}, new Object[]{selectedDoc});
                if (nb != null ? nb > 0 : false) {
                    getErrorMessage("Impossible d'annuler cette fiche de ration", "Des tiers ont déjà été servi !");
                    return;
                }
            }
            List<YvsComRation> list = new ArrayList<>(selectedDoc.getListRations());
            selectedDoc.setAuthor(currentUser);
            selectedDoc.setDateUpdate(new Date());
            selectedDoc.setStatut(selectedDoc.getStatut().equals(Constantes.STATUT_DOC_VALIDE) ? Constantes.STATUT_DOC_EDITABLE : (selectedDoc.getStatut().equals(Constantes.STATUT_DOC_ENCOUR) ? Constantes.STATUT_DOC_VALIDE : Constantes.STATUT_DOC_ENCOUR));
            YvsComDocRation y = new YvsComDocRation(selectedDoc);
            y.getListRations().clear();
            dao.update(y);
            selectedDoc.setListRations(list);
            int idx = documents.indexOf(selectedDoc);
            if (idx > -1) {
                documents.set(idx, selectedDoc);
            }
            if (selectedDoc.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                String query = "UPDATE yvs_com_doc_ration SET cloturer = TRUE WHERE depot = ? AND id != ? AND cloturer = FALSE";
                dao.requeteLibre(query, new Options[]{new Options(selectedDoc.getDepot().getId(), 1), new Options(selectedDoc.getId(), 2)});
                for (YvsComDocRation o : documents) {
                    if (o.getDepot().equals(selectedDoc.getDepot()) && !o.getCloturer() && !o.getId().equals(selectedDoc.getId())) {
                        o.setCloturer(true);
                    }
                }
            }
            update("param_ration_zone_form");
            update("blog_periode_ration");
            succes();
        } else {
            getErrorMessage("Aucune fiche n'a été selectionné !");
        }
    }

    public void loadAllRationTiersPeriode(YvsComRation ra) {
        if (ra != null ? ra.getDocRation().getPeriode() != null : false) {
            listDetailsRationsTiers.clear();
            YvsComRation current;
            Calendar cal = Calendar.getInstance();
            cal.setTime(ra.getDocRation().getPeriode().getDateDebut());
            Date cdate = cal.getTime();
            long i = -100;
            while (cdate.before(ra.getDocRation().getPeriode().getFin())) {
                current = new YvsComRation(i++);
                current.setDateRation(cdate);
                current.setArticle(ra.getArticle());
                current.setQuantite((Double) dao.loadObjectByNameQueries("YvsComRation.findByQuantitePris", new String[]{"personnel", "article", "date"}, new Object[]{ra.getPersonnel(), ra.getArticle(), cdate}));
                listDetailsRationsTiers.add(current);
                cal.add(Calendar.DAY_OF_MONTH, 1);
                cdate = cal.getTime();
            }
            openDialog("dlgDetailsRation");
            update("table_dlgDetailsRation");
        }
    }

    public void deleteRation(YvsComDocRation ra) {
        try {
            if (!autoriser("ra_delete_fiche")) {
                openNotAcces();
                return;
            }
            if (ra != null) {
                ra.getListRations().clear();
                dao.delete(ra);
                documents.remove(ra);
                if (ra.getId().equals(document.getId())) {
                    resetFiche();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible");
            getException("Error suppression", ex);
        }
    }

    public void print(YvsComDocRation ra) {
        try {
            if (ra != null ? ra.getId() > 0 : false) {
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", ra.getId().intValue());
                param.put("AGENCE", currentAgence.getId().intValue());
                param.put("NAME_AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
                executeReport("ration", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedRations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cloturerFiche(YvsComDocRation ra) {
        if (ra != null) {
            if (ra.getCloturer()) {
                if (!autoriser("ra_unluck_fiche")) {
                    openNotAcces();
                    return;
                } else {
                    ra.setCloturer(!ra.getCloturer());
                    ra.setAuthor(currentUser);
                    ra.setDateUpdate(new Date());
                    dao.update(ra);
                }
            } else {
                if (!autoriser("ra_valider_fiche")) {
                    openNotAcces();
                    return;
                } else {
                    ra.setCloturer(!ra.getCloturer());
                    ra.setAuthor(currentUser);
                    ra.setDateUpdate(new Date());
                    dao.update(ra);
                }
            }
        }
    }

    public void printSituation(YvsComDocRation ra) {
        try {
            if (ra != null ? ra.getId() > 0 : false) {
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", ra.getId().intValue());
                param.put("AGENCE", currentAgence.getId().intValue());
                param.put("NAME_AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
                executeReport("ration_situation", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedRations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearParams() {
        addDateSearch = false;
        clotureSearch = null;
        debutSearch = finSearch = new Date();
        depotSearch = 0;
        trancheSearch = 0;
        periodeSearch = 0;
        statutSearch = numeroSearch = null;
        usersSearch = null;
        egaliteStatut = "=";
        paginator.getParams().clear();
        initForm = true;
        loadAllDocRation(true);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("UPPER(y.numDoc)", "numDoc", null, "=", "AND");
        if (Util.asString(numeroSearch)) {
            p = new ParametreRequete(null, "numDoc", numeroSearch.toUpperCase(), "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numDoc)", "numDoc", numeroSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(COALESCE(y.numReference, ''))", "numDoc", numeroSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        initForm = true;
        loadAllDocRation(true);
    }

    public void addParamStatut() {
        ParametreRequete p = new ParametreRequete("y.statut", "statut", null, "=", "AND");
        if (Util.asString(statutSearch)) {
            p = new ParametreRequete("y.statut", "statut", statutSearch.charAt(0), egaliteStatut, "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllDocRation(true);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateFiche", "dateFiche", null, "=", "AND");
        if (addDateSearch ? (debutSearch != null && finSearch != null) : false) {
            p = new ParametreRequete("y.dateFiche", "dateFiche", debutSearch, finSearch, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllDocRation(true);
    }

    public void addParamUsers() {
        ParametreRequete p = new ParametreRequete("y.magasinier", "magasinier", null, "=", "AND");
        if (Util.asString(usersSearch)) {
            p = new ParametreRequete(null, "magasinier", usersSearch.toUpperCase(), "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.magasinier.codeUsers)", "magasinier", usersSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.magasinier.nomUsers)", "magasinier", usersSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        initForm = true;
        loadAllDocRation(true);
    }

    public void addParamPeriode() {
        ParametreRequete p = new ParametreRequete("y.periode", "periode", null, "=", "AND");
        if (periodeSearch > 0) {
            p = new ParametreRequete("y.periode", "periode", new YvsComPeriodeRation(periodeSearch), "=", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllDocRation(true);
    }

    public void addParamDepot() {
        ParametreRequete p = new ParametreRequete("y.depot", "depot", null, "=", "AND");
        if (depotSearch > 0) {
            p = new ParametreRequete("y.depot", "depot", new YvsBaseDepots(depotSearch), "=", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllDocRation(true);
    }

    public void addParamTranche() {
        ParametreRequete p = new ParametreRequete("y.creneauHoraire", "tranche", null, "=", "AND");
        if (trancheSearch > 0) {
            p = new ParametreRequete("y.creneauHoraire.tranche", "tranche", new YvsGrhTrancheHoraire(trancheSearch), "=", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllDocRation(true);
    }

    public void addParamCloturer() {
        ParametreRequete p = new ParametreRequete("y.cloturer", "cloturer", null, "=", "AND");
        if (clotureSearch != null) {
            p = new ParametreRequete("COALESCE(y.cloturer, false)", "cloturer", clotureSearch, "=", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllDocRation(true);
    }

    public void onRowLotEdit(RowEditEvent ev) {
        if (ev != null) {
            YvsComLotReception y = (YvsComLotReception) ev.getObject();
            if (y != null) {
                onBlurQuantiteeLot(y);
            }
        }
    }

    public void onBlurQuantiteeLot(YvsComLotReception y) {
        try {
            double quantite = y.getQuantitee();
            if (quantite > y.getStock()) {
                y.setQuantitee(0);
                getErrorMessage("La quantitée entrée ne peut pas dépasser le stock total du lot");
                update("data-ration_require_lot");
                return;
            }
            for (YvsComLotReception r : ration.getLots()) {
                if (!r.equals(y)) {
                    quantite += r.getQuantitee();
                }
            }
            if (quantite > ration.getQuantite()) {
                y.setQuantitee(0);
                getErrorMessage("Vous ne pouvez pas entrer cette quantitée car la somme des quantitées de lot depasse la quantité prevue");
                update("data-ration_require_lot");
                return;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onBlurQuantiteeLot", ex);
        }
    }
}
