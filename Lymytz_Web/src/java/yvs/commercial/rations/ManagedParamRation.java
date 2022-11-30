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
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.base.produits.ManagedArticles;
import yvs.production.UtilProd;
import yvs.base.tiers.ManagedTiers;
import yvs.commercial.objectifs.PeriodesObjectifs;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.commercial.ration.YvsComParamRation;
import yvs.entity.commercial.ration.YvsComParamRationSuspension;
import yvs.entity.commercial.ration.YvsComPeriodeRation;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.mutuelle.ManagedExercice;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedParamRation extends Managed<ParamRations, YvsComParamRation> implements Serializable {
    
    private ParamRations paramRation = new ParamRations();
    private List<YvsComParamRation> listParamRation;
    private YvsComParamRation selectedParamRation;
    
    private ParamRationSuspension suspension = new ParamRationSuspension();
    private YvsComParamRationSuspension selectSuspenion = new YvsComParamRationSuspension();
    
    private List<YvsComPeriodeRation> periodes;
    private YvsComPeriodeRation selectedPeriode;
    private PeriodesObjectifs periode = new PeriodesObjectifs();
    private PaginatorResult<YvsComPeriodeRation> pa = new PaginatorResult<>();
    
    private double totalRation = 0;
    
    private Long idExoSearch;
    private long agenceSearch;
    private String tabIds;
    private Conditionnement article = new Conditionnement();
    private Boolean activeSearch;
    
    public ManagedParamRation() {
        listParamRation = new ArrayList<>();
        periodes = new ArrayList<>();
    }

    public long getAgenceSearch() {
        return agenceSearch;
    }

    public void setAgenceSearch(long agenceSearch) {
        this.agenceSearch = agenceSearch;
    }
    
    public Boolean getActiveSearch() {
        return activeSearch;
    }
    
    public void setActiveSearch(Boolean activeSearch) {
        this.activeSearch = activeSearch;
    }
    
    public Conditionnement getArticle() {
        return article;
    }
    
    public void setArticle(Conditionnement article) {
        this.article = article;
    }
    
    public String getTabIds() {
        return tabIds;
    }
    
    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }
    
    public ParamRationSuspension getSuspension() {
        return suspension;
    }
    
    public void setSuspension(ParamRationSuspension suspension) {
        this.suspension = suspension;
    }
    
    public YvsComParamRationSuspension getSelectSuspenion() {
        return selectSuspenion;
    }
    
    public void setSelectSuspenion(YvsComParamRationSuspension selectSuspenion) {
        this.selectSuspenion = selectSuspenion;
    }
    
    public double getTotalRation() {
        return totalRation;
    }
    
    public void setTotalRation(double totalRation) {
        this.totalRation = totalRation;
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
    
    public YvsComParamRation getSelectedParamRation() {
        return selectedParamRation;
    }
    
    public void setSelectedParamRation(YvsComParamRation selectedParamRation) {
        this.selectedParamRation = selectedParamRation;
    }
    
    public List<YvsComPeriodeRation> getPeriodes() {
        return periodes;
    }
    
    public void setPeriodes(List<YvsComPeriodeRation> periodes) {
        this.periodes = periodes;
    }
    
    public PeriodesObjectifs getPeriode() {
        return periode;
    }
    
    public void setPeriode(PeriodesObjectifs periode) {
        this.periode = periode;
    }
    
    public PaginatorResult<YvsComPeriodeRation> getPa() {
        return pa;
    }
    
    public void setPa(PaginatorResult<YvsComPeriodeRation> pa) {
        this.pa = pa;
    }
    
    public YvsComPeriodeRation getSelectedPeriode() {
        return selectedPeriode;
    }
    
    public void setSelectedPeriode(YvsComPeriodeRation selectedPeriode) {
        this.selectedPeriode = selectedPeriode;
    }
    
    public Long getIdExoSearch() {
        return idExoSearch;
    }
    
    public void setIdExoSearch(Long idExoSearch) {
        this.idExoSearch = idExoSearch;
    }
    
    @Override
    public boolean controleFiche(ParamRations bean) {
        if (selectedParamRation == null) {
            getErrorMessage("Aucun tiers n'a été sélectionné !");
            return false;
        }
        if (bean.getPersonnel().getId() <= 0) {
            getErrorMessage("Vous devez indiquer le tiers bénéficiaire !");
            return false;
        }
        if (bean.getArticle().getId() <= 0) {
            getErrorMessage("Vous devez choisir un article !");
            return false;
        }
        if (bean.getConditionement().getId() <= 0) {
            getErrorMessage("Vous devez préciser le conditionnement !");
            return false;
        }
        if (bean.getQuantite() <= 0) {
            getErrorMessage("Vous devez entrer une quantité valide");
            return false;
        }
        champ = new String[]{"personnel", "article", "conditionnement"};
        val = new Object[]{new YvsBaseTiers(bean.getPersonnel().getId()), new YvsBaseArticles(bean.getArticle().getId()), new YvsBaseConditionnement(bean.getConditionement().getId())};
        YvsComParamRation y = (YvsComParamRation) dao.loadOneByNameQueries("YvsComParamRation.findOne", champ, val);
        if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
            getErrorMessage("Vous avez déja attribué cette ration à ce tiers");
            return false;
        }
        return true;
    }
    
    public boolean controleFiche(ParamRationSuspension bean, YvsComParamRation selectedParamRation) {
        if (selectedParamRation != null ? selectedParamRation.getId() < 1 : true) {
            getErrorMessage("Aucun tiers n'a été sélectionné !");
            return false;
        }
        return true;
    }
    
    @Override
    public void loadAll() {
        ManagedTiers service = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (service != null) {
            buildData(service.getListTiers());
            update("table_param_rations");
        }
    }
    
    @Override
    public ParamRations recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void populateView(ParamRations bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void deleteSuspension(YvsComParamRationSuspension y) {
        if (y != null ? y.getId() > 0 : false) {
            dao.delete(y);
            selectedParamRation.getSuspensions().remove(y);
            calculTotalRation();
            update("table_param_rations");
            update("data-suspension_ration");
        }
    }
    
    @Override
    public void updateBean() {
        try {
            if (article != null ? article.getId() < 1 : true) {
                getErrorMessage("Vous devez selectionner un conditionnement");
                return;
            }
            List<Integer> tabs = decomposeSelection(tabIds);
            if (tabs != null ? tabs.size() > 0 : false) {
                YvsComParamRation y;
                long succes = 0;
                for (Integer index : tabs) {
                    if (index < listParamRation.size()) {
                        y = listParamRation.get(index);
                        if (y.getId() > 0) {
                            y.setArticle(UtilProd.buildEntityArticle(article.getArticle()));
                            y.setConditionnement(UtilProd.buildConditionnement(article, currentUser));
                            y.setAuthor(currentUser);
                            y.setDateUpdate(new Date());
                            dao.update(y);
                            
                            listParamRation.set(index, y);
                            succes++;
                        }
                    }
                }
                if (succes > 0) {
                    succes();
                }
            }
        } catch (Exception ex) {
            getException("updateBean", ex);
        }
    }
    
    @Override
    public void loadOnView(SelectEvent ev) {
        selectOneLineTiers((YvsComParamRation) ev.getObject());
    }
    
    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void loadOnViewSuspension(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectSuspenion = (YvsComParamRationSuspension) ev.getObject();
            suspension = new ParamRationSuspension();
            suspension.setId(selectSuspenion.getId());
            suspension.setDateSave(selectSuspenion.getDateSave());
            suspension.setDebutSuspension(selectSuspenion.getDebutSuspension());
            suspension.setFinSuspension(selectSuspenion.getFinSuspension());
            suspension.setPersonnel(new ParamRations(selectSuspenion.getPersonnel().getId()));
        }
    }
    
    public void unLoadOnViewSuspension(UnselectEvent ev) {
        suspension = new ParamRationSuspension();
        selectSuspenion = new YvsComParamRationSuspension();
    }
    
    public void selectOneLineTiers(YvsComParamRation p) {
        selectedParamRation = p;
        
        paramRation = new ParamRations();
        paramRation.getPersonnel().setActif(p.getPersonnel().getActif());
        paramRation.getPersonnel().setCodeSecret(p.getPersonnel().getCodeTiers());
        paramRation.getPersonnel().setId(p.getPersonnel().getId());
        paramRation.getPersonnel().setNom(p.getPersonnel().getNom());
        paramRation.getPersonnel().setPrenom(p.getPersonnel().getPrenom());
        paramRation.setArticle(UtilProd.buildSimpleBeanArticles(p.getArticle()));
        paramRation.setConditionement(UtilProd.buildBeanConditionnement(p.getConditionnement()));
        paramRation.setPeriode(p.getPeriode());
        paramRation.setId(p.getId());
        paramRation.setQuantite(p.getQuantite());
        paramRation.setActif(p.getId() > 0 ? p.getActif() : true);
        paramRation.setProportionnel(p.getProportionnel());
        paramRation.setDatePriseEffet(p.getDatePriseEffet());
        update("param_ration_zone_form");
    }
    
    public void pagineDataTiers(boolean next) {
        ManagedTiers service = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (service != null) {
            service.init(next);
            buildData(service.getListTiers());
            update("table_param_rations");
        }
    }
    
    public void changePage(ValueChangeEvent ev) {
        ManagedTiers service = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (service != null) {
            if (ev.getNewValue() != null) {
                service.choosePaginator(ev);
                buildData(service.getListTiers());
            }
            update("table_param_rations");
        }
    }
    
    public void searchByNum() {
        ManagedTiers service = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (service != null) {
            service.setNumSearch_(paramRation.getPersonnel().getNom());
            service.searchByNum();
            buildData(service.getListTiers());
            update("table_param_rations");
        }
    }
    
    public void loadAllParamRation() {
        ManagedTiers service = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (service != null) {
            List<YvsBaseTiers> l = service.getListTiers();
            buildData(l);
        }
        calculTotalRation();
    }
    
    private void buildData(List<YvsBaseTiers> l) {
        listParamRation.clear();
        for (YvsBaseTiers ti : l) {
            if (ti.getAgence() != null ? (agenceSearch > 0 ? ti.getAgence().getId().equals(agenceSearch) : true) : true) {
                if (!ti.getParamsRations().isEmpty()) {
                    if (activeSearch != null ? activeSearch : true) {
                        listParamRation.addAll(addParamRation(ti.getParamsRations()));
                    }
                } else {
                    if (activeSearch != null ? !activeSearch : true) {
                        listParamRation.add(buildOneParamRation(ti));
                    }
                }
            }
        }
    }
    
    private List<YvsComParamRation> addParamRation(List<YvsComParamRation> l) {
        if (l != null) {
            for (YvsComParamRation cp : l) {
                cp.setSuspendu(currentSuspend(cp) > 0);
            }
        }
        return l;
    }
    
    private void calculTotalRation() {
        List<Long> ids = dao.loadNameQueries("YvsComParamRationSuspension.findIdPersonnelByDate", new String[]{"societe", "date"}, new Object[]{currentAgence.getSociete(), new Date()});
        if (ids.isEmpty()) {
            ids.add(-1L);
        }
        champ = new String[]{"societe", "ids"};
        val = new Object[]{currentAgence.getSociete(), ids};
        nameQueri = "YvsComParamRation.findSumDate";
        Double re = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        totalRation = re != null ? re : 0;
    }
    
    private YvsComParamRation buildOneParamRation(YvsBaseTiers t) {
        YvsComParamRation p = new YvsComParamRation();
        p.setPersonnel(t);
        p.setId(-t.getId());
        return p;
    }
    
    public void selectOneLineArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            List<YvsBaseConditionnement> unites = dao.loadNameQueries("YvsBaseConditionnement.findByActifArticle", new String[]{"article"}, new Object[]{bean});
            bean.setConditionnements(unites);
            if (bean.getConditionnements() != null ? !bean.getConditionnements().isEmpty() : false) {
                YvsBaseConditionnement c = bean.getConditionnements().get(0);
                if (update) {
                    article = UtilProd.buildBeanConditionnement(c);
                } else {
                    paramRation.setConditionement(UtilProd.buildBeanConditionnement(c));
                }
            }
            if (update) {
                article.setArticle(UtilProd.buildBeanArticles(bean));
                update("param_ration_update_article");
            } else {
                paramRation.setArticle(UtilProd.buildBeanArticles(bean));
                update("param_ration_zone_form");
            }
        }
    }
    
    public void searchArticle(String refArt) {
        searchArticle(refArt, false);
    }
    boolean update = false;
    
    public void searchArticle(String refArt, boolean update) {
        this.update = update;
        if (update) {
            article.getArticle().setDesignation("");
            article.getArticle().setError(true);
            article.getArticle().setId(0);
        } else {
            paramRation.getArticle().setDesignation("");
            paramRation.getArticle().setError(true);
            paramRation.getArticle().setId(0);
        }
        ManagedArticles service = (ManagedArticles) giveManagedBean("Marticle");
        if (service != null) {
            Articles y = service.findArticleActif(refArt, true);
            if (service.getListArticle() != null ? !service.getListArticle().isEmpty() : false) {
                if (service.getListArticle().size() > 1) {
                    update("data_articles_param_rations");
                } else {
                    List<YvsBaseConditionnement> unites = dao.loadNameQueries("YvsBaseConditionnement.findByActifArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(y.getId())});
                    y.setConditionnements(unites);
                    if (y.getConditionnements() != null ? !y.getConditionnements().isEmpty() : false) {
                        YvsBaseConditionnement c = y.getConditionnements().get(0);
                        if (update) {
                            article = UtilProd.buildBeanConditionnement(c);
                        } else {
                            paramRation.setConditionement(UtilProd.buildBeanConditionnement(c));
                        }
                    }
                    if (update) {
                        article.setArticle(y);
                    } else {
                        paramRation.setArticle(y);
                    }
                    update("zone_obj_cible_art");
                }
                if (update) {
                    article.getArticle().setError(false);
                } else {
                    paramRation.getArticle().setError(false);
                }
            }
            if (update) {
                update("param_ration_update_article");
            } else {
                update("param_ration_zone_form");
            }
        }
        
    }
    
    @Override
    public boolean saveNew() {
        if (!autoriser("ra_attribuer")) {
            openNotAcces();
            return false;
        }
        if (controleFiche(paramRation)) {
            selectedParamRation.setQuantite(paramRation.getQuantite());
            selectedParamRation.setActif(paramRation.isActif());
            selectedParamRation.setProportionnel(paramRation.isProportionnel());
            selectedParamRation.setDateUpdate(new Date());
            selectedParamRation.setPeriode(paramRation.getPeriode());
            selectedParamRation.setDatePriseEffet(paramRation.getDatePriseEffet());
            selectedParamRation.setAuthor(currentUser);
            selectedParamRation.setArticle(UtilProd.buildEntityArticle(paramRation.getArticle()));
            int idxc = paramRation.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(paramRation.getConditionement().getId()));
            if (idxc >= 0) {
                selectedParamRation.setConditionnement(paramRation.getArticle().getConditionnements().get(idxc));
            }
            int idx = listParamRation.indexOf(selectedParamRation);
            if (paramRation.getId() <= 0) {
                selectedParamRation.setActif(true);
                paramRation.setActif(true);
                selectedParamRation.setDateSave(new Date());
                selectedParamRation.setId(null);
                selectedParamRation = (YvsComParamRation) dao.save1(selectedParamRation);
            } else {
                dao.update(selectedParamRation);
            }
            if (idx >= 0) {
                listParamRation.set(idx, selectedParamRation);
            }
            calculTotalRation();
            update("table_param_rations");
        }
        return true;
    }
    
    public void saveNewSuspension() {
        YvsComParamRationSuspension y = saveNewSuspension(selectedParamRation, suspension);
        if (y != null ? y.getId() > 0 : false) {
            selectSuspenion = y;
            calculTotalRation();
            update("table_param_rations");
            succes();
        }
    }
    
    public YvsComParamRationSuspension saveNewSuspension(YvsComParamRation param, ParamRationSuspension suspension) {
        if (controleFiche(suspension, param)) {
            YvsComParamRationSuspension y = new YvsComParamRationSuspension(suspension.getId());
            y.setDateUpdate(new Date());
            y.setDateSave(suspension.getDateSave());
            y.setDebutSuspension(suspension.getDebutSuspension());
            y.setFinSuspension(suspension.getFinSuspension());
            y.setPersonnel(param);
            y.setAuthor(currentUser);
            if (suspension.getId() < 1) {
                y.setId(null);
                y = (YvsComParamRationSuspension) dao.save1(y);
                suspension.setId(y.getId());
            } else {
                dao.update(y);
            }
            int idx = param.getSuspensions().indexOf(y);
            if (idx > -1) {
                param.getSuspensions().set(idx, y);
            } else {
                param.getSuspensions().add(0, y);
            }
            return y;
        }
        return null;
    }
    
    public void toogleActiveParam(YvsComParamRation ra) {
        if (ra.getId() > 0) {
            if (!autoriser("ra_activer")) {
                openNotAcces();
                return;
            }
            ra.setActif(!ra.getActif());
            ra.setAuthor(currentUser);
            ra.setDateUpdate(new Date());
            dao.update(ra);
            calculTotalRation();
            update("table_param_rations");
        }
    }
    
    public void toogleSuspendParam(YvsComParamRation ra) {
        if (ra != null ? ra.getId() > 0 : false) {
            if (!autoriser("ra_suspendre")) {
                openNotAcces();
                return;
            }
            selectedParamRation = ra;
            selectedParamRation.setSuspensions(dao.loadNameQueries("YvsComParamRationSuspension.findByPersonnel", new String[]{"personnel"}, new Object[]{selectedParamRation}));
            suspension = new ParamRationSuspension();
            selectSuspenion = new YvsComParamRationSuspension();
            openDialog("dlgSuspendRation");
            update("form-suspension_ration");
            update("data-suspension_ration");
        }
    }
    
    public void openToDelParamRation(YvsComParamRation param) {
        selectedParamRation = param;
    }
    
    public long currentSuspend(YvsComParamRation tiers) {
        Long nb = (Long) dao.loadObjectByNameQueries("YvsComParamRationSuspension.findCByPersonnelDate", new String[]{"personnel", "date"}, new Object[]{tiers, new Date()});
        return nb != null ? nb : -1;
    }
    
    public void deleteOneParamRation() {
        if ((selectedParamRation != null) ? selectedParamRation.getId() > 0 : false) {
            try {
                YvsComParamRation p = new YvsComParamRation(selectedParamRation.getId());
                p.setAuthor(currentUser);
                dao.delete(p);
                listParamRation.remove(p);
                succes();
                update("table_param_rations");
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cette ligne !");
                getException("Lymytz Exception>>>>", ex);
            }
        } else {
            getErrorMessage("Aucune selection n'a été trouvé !");
        }
    }
    
    public void addNewLineRation(YvsComParamRation ra) {
        if (ra.getId() > 0) {
            YvsComParamRation r = new YvsComParamRation();
            r.setId(-(ra.getId() + 50000));
            r.setPersonnel(ra.getPersonnel());
            listParamRation.add(0, r);
            selectedParamRation = r;
            calculTotalRation();
            update("table_param_rations");
            selectOneLineTiers(r);
        }
    }

    //Gestion des périodes 
    public void saveNewPeriode() {
        if ((periode.getDebut() != null && periode.getFin() != null) ? getDate(periode.getDebut(), periode.getHdebut()).before(getDate(periode.getFin(), periode.getHfin())) : false) {
            YvsComPeriodeRation pe = new YvsComPeriodeRation();
            pe.setSociete(currentAgence.getSociete());
            pe.setAuthor(currentUser);
            pe.setDateDebut(periode.getDebut());
            pe.setFin(periode.getFin());
            pe.setHeureDebut(periode.getHdebut());
            pe.setHeureFin(periode.getHfin());
            pe.setCloturer(periode.isCloturer());
            pe.setId(periode.getId());
            pe.setAbbreviation(periode.getAbbreviation());
            pe.setReferencePeriode(periode.getReference());
            if (pe.getId() > 0) {
                dao.update(pe);
                int idx = periodes.indexOf(pe);
                if (idx >= 0) {
                    periodes.set(idx, pe);
                }
            } else {
                pe.setId(null);
                pe = (YvsComPeriodeRation) dao.save1(pe);
                periode.setId(pe.getId());
                periodes.add(0, pe);
            }
            succes();
            periode = new PeriodesObjectifs();
            update("table_periodes_ration");
            update("form_main_period_ration");
        } else {
            getErrorMessage("Formulaire Incorrecte !");
        }
    }
    
    public void selectOneLinePeriode(SelectEvent ev) {
        YvsComPeriodeRation pe = (YvsComPeriodeRation) ev.getObject();
        periode = buildBeanPeriod(pe);
        update("form_main_period_ration");
    }
    
    private PeriodesObjectifs buildBeanPeriod(YvsComPeriodeRation pe) {
        PeriodesObjectifs re = new PeriodesObjectifs();
        re.setAuteur((pe.getAuthor() != null) ? pe.getAuthor().getUsers().getNomUsers() : "");
        re.setCloturer(pe.getCloturer());
        re.setDebut(pe.getDateDebut());
        re.setFin(pe.getFin());
        re.setHdebut(pe.getHeureDebut());
        re.setHfin(pe.getHeureFin());
        re.setId(pe.getId());
        re.setReference(pe.getReferencePeriode());
        re.setAbbreviation(pe.getAbbreviation());
        return re;
    }
    
    private Date getDate(Date date, Date heure) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar he = Calendar.getInstance();
        heure = (heure == null) ? new Date() : heure;
        he.setTime(heure);
        cal.set(Calendar.HOUR_OF_DAY, he.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, he.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, he.get(Calendar.SECOND));
        return cal.getTime();
    }
    
    public void loadAllPeriode(boolean init, boolean next) {
        pa.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        periodes = pa.executeDynamicQuery("YvsComPeriodeRation", "y.dateDebut DESC", init, next, (int) imax, dao);
    }
    
    public void addParamCloturePeriode(boolean cloture) {
        pa.addParam(new ParametreRequete("y.cloturer", "cloturer", cloture, "=", "AND"));
        loadAllPeriode(true, true);
    }
    
    public void addParamExercice(ValueChangeEvent ev) {
        Long id = null;
        if (ev != null) {
            id = (Long) ev.getNewValue();
        }
        id = id == null ? 0L : id;
        ManagedExercice service = (ManagedExercice) giveManagedBean(ManagedExercice.class);
        if (service != null) {
            int idx = service.getExercices().indexOf(new YvsBaseExercice(id));
            if (idx >= 0) {
                YvsBaseExercice exo = service.getExercices().get(idx);
                addParamExercice(exo.getDateDebut(), exo.getDateFin());
            } else {
                addParamExercice(null, null);
            }
        }
    }
    
    private void addParamExercice(Date debut, Date fin) {
        ParametreRequete p0 = new ParametreRequete("y.dateDebut", "periode", null, null, "BETWEEN", "AND");
        if (debut != null && fin != null) {
            if (debut.before(fin)) {
                p0.setObjet(debut);
            }
            p0.setOtherObjet(fin);
        }
        pa.addParam(p0);
        loadAllPeriode(true, true);
    }
    
    public void addParamCurrentExo() {
        YvsBaseExercice exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findActifByDate", new String[]{"dateJour", "societe"}, new Object[]{new Date(), currentAgence.getSociete()});
        if (exo != null) {
            addParamExercice(exo.getDateDebut(), exo.getDateFin());
        }
    }
    
    public void choixExercice(ValueChangeEvent ev) {
        Date debut = null, fin = null;
        if (ev.getNewValue() != null) {
            Long id = (Long) ev.getNewValue();
            if (id >= 0) {
                ManagedExercice service = (ManagedExercice) giveManagedBean(ManagedExercice.class);
                if (service != null) {
                    int idx = service.getExercices().indexOf(new YvsBaseExercice(id));
                    if (idx >= 0) {
                        YvsBaseExercice exo = service.getExercices().get(idx);
                        debut = exo.getDateDebut();
                        fin = exo.getDateFin();
                    }
                }
            }
        }
        addParamExercice(debut, fin);
    }
    
    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllPeriode(true, true);
    }
    
    public void selectToDelete(YvsComPeriodeRation pe) {
        selectedPeriode = pe;
        periode = buildBeanPeriod(pe);
        openDialog("dlgDelPeriode");
        update("form_main_period_ration");
    }
    
    public void deleteLinePeriode() {
        if ((selectedPeriode != null) ? selectedPeriode.getId() > 0 : false) {
            try {
                dao.delete(new YvsComPeriodeRation(selectedPeriode.getId()));
                periodes.remove(selectedPeriode);
                periode = new PeriodesObjectifs();
                update("table_periodes_ration");
                update("form_main_period_ration");
            } catch (Exception ex) {
                getErrorMessage("Suppression impossible !");
                getException("Lymytz Error >>>", ex);
            }
        }
    }
    
    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
