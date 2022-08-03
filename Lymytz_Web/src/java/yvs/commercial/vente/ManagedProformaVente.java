/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.vente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.ManagedArticles;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.UtilCom;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.commercial.vente.YvsComProformaVente;
import yvs.entity.commercial.vente.YvsComProformaVenteContenu;
import yvs.entity.produits.YvsBaseArticles;
import yvs.production.UtilProd;
import yvs.service.com.vente.IYvsComProformaVente;
import yvs.service.com.vente.IYvsComProformaVenteContenu;
import yvs.util.Constantes;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz-pc
 */
@ManagedBean
@SessionScoped
public class ManagedProformaVente extends ManagedCommercial<ProformaVente, YvsComProformaVente> implements Serializable {

    private ProformaVente proforma = new ProformaVente();
    private YvsComProformaVente entity = new YvsComProformaVente();
    private List<YvsComProformaVente> proformas;

    private ProformaVenteContenu contenu = new ProformaVenteContenu();
    private YvsComProformaVenteContenu entityContenu = new YvsComProformaVenteContenu();

    private String tabIds;
    private IYvsComProformaVente service;
    private IYvsComProformaVenteContenu serviceC;

    private boolean displayAuteur = false;

    private Boolean valideSearch = null;
    private boolean addDate = false;
    private Date dateDebutSearch = new Date(), dateFinSearch = new Date();
    private String clientSearch, numeroSearch, statutSearch;

    public ManagedProformaVente() {
        proformas = new ArrayList<>();
    }

    public boolean isDisplayAuteur() {
        return displayAuteur;
    }

    public void setDisplayAuteur(boolean displayAuteur) {
        this.displayAuteur = displayAuteur;
    }

    public ProformaVenteContenu getContenu() {
        return contenu;
    }

    public void setContenu(ProformaVenteContenu contenu) {
        this.contenu = contenu;
    }

    public YvsComProformaVenteContenu getEntityContenu() {
        return entityContenu;
    }

    public void setEntityContenu(YvsComProformaVenteContenu entityContenu) {
        this.entityContenu = entityContenu;
    }

    public Boolean getValideSearch() {
        return valideSearch;
    }

    public void setValideSearch(Boolean valideSearch) {
        this.valideSearch = valideSearch;
    }

    public boolean isAddDate() {
        return addDate;
    }

    public void setAddDate(boolean addDate) {
        this.addDate = addDate;
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

    public String getClientSearch() {
        return clientSearch;
    }

    public void setClientSearch(String clientSearch) {
        this.clientSearch = clientSearch;
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

    public ProformaVente getProforma() {
        return proforma;
    }

    public void setProforma(ProformaVente proformat) {
        this.proforma = proformat;
    }

    public YvsComProformaVente getEntity() {
        return entity;
    }

    public void setEntity(YvsComProformaVente entity) {
        this.entity = entity;
    }

    public List<YvsComProformaVente> getProformas() {
        return proformas;
    }

    public void setProformas(List<YvsComProformaVente> proformas) {
        this.proformas = proformas;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public IYvsComProformaVente getService() {
        return service;
    }

    public void setService(IYvsComProformaVente service) {
        this.service = service;
    }

    @Override
    public void loadAll() {
        try {
            service = (IYvsComProformaVente) IEntitiSax.createInstance("IYvsComProformaVente", dao);
            serviceC = (IYvsComProformaVenteContenu) IEntitiSax.createInstance("IYvsComProformaVenteContenu", dao);
            loadAll(true, true);
        } catch (Exception ex) {
            getErrorMessage("loadAll Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void loadAll(boolean avance, boolean init) {
        try {
            paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
            proformas = paginator.executeDynamicQuery("y", "y", "YvsComProformaVente y", "y.dateDoc DESC, y.numero DESC", avance, init, (int) imax, dao);
        } catch (Exception ex) {
            getErrorMessage("loadAll Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void gotoPagePaginator() {
        try {
            paginator.gotoPage((int) imax);
            loadAll(true, true);
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        try {
            super.choosePaginator(ev);
            loadAll(true, true);
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void parcoursInAllResult(boolean avancer) {
        try {
            setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
            if (getOffset() < 0 || getOffset() >= (paginator.getNbResult())) {
                setOffset(0);
            }
            List<YvsComProformaVente> re = paginator.parcoursDynamicData("YvsComProformaVente", "y", "y.dateDoc DESC, y.numero DESCt", getOffset(), dao);
            if (!re.isEmpty()) {
                onSelectObject(re.get(0));
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean controleFiche(ProformaVente bean) {
        try {
            if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Le proformat doit etre editable pour etre modifier");
                return false;
            }
            if (bean.getDateDoc() == null) {
                getErrorMessage("Vous devez indiquer une date");
                return false;
            }
            if (bean.getDateDoc() == null) {
                getErrorMessage("Vous devez indiquer une date d'expiration");
                return false;
            }
            if (bean.getClient() != null ? bean.getClient().trim().length() < 1 : true) {
                getErrorMessage("Vous devez selectionner un client");
                return false;
            }
            return true;
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean controleFiche(ProformaVenteContenu bean) {
        try {
            if (!proforma.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Le proformat doit etre editable pour etre modifier");
                return false;
            }
            if (bean.getConditionnement().getArticle() == null) {
                getErrorMessage("Vous devez selectionner un article");
                return false;
            }
            if (bean.getQuantite() <= 0) {
                getErrorMessage("Vous devez indiquer une quantitée");
                return false;
            }
            if (bean.getPrix() <= 0) {
                getErrorMessage("Vous devez indiquer un prix");
                return false;
            }
            if (bean.getConditionnement() != null ? bean.getConditionnement().getId() < 1 : true) {
                getErrorMessage("Vous devez selectionner un conditionnement");
                return false;
            }
            return true;
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public void resetFiche() {
        try {
            proforma = new ProformaVente();
            entity = new YvsComProformaVente();
            resetFicheContenu();
            update("blog_form_proforma_vente");
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void resetFicheContenu() {
        try {
            contenu = new ProformaVenteContenu();
            entityContenu = new YvsComProformaVenteContenu();
            update("desc_artcile_proforma_vente");
            update("tabview-proforma_vente_contenu:form-proforma_vente_contenu");
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean saveNew() {
        try {
            if (controleFiche(proforma) && service != null) {
                entity = UtilCom.buildProformaVente(proforma, currentAgence, currentUser);
                ResultatAction<YvsComProformaVente> result = null;
                if (proforma.getId() < 1) {
                    result = service.save(entity);
                } else {
                    result = service.update(entity);
                }
                if (result != null ? result.isResult() : false) {
                    entity = (YvsComProformaVente) result.getData();
                    proforma.setId(entity.getId());
                    proforma.setNumero(entity.getNumero());
                    int index = proformas.indexOf(entity);
                    if (index > -1) {
                        proformas.set(index, entity);
                    } else {
                        proformas.add(entity);
                    }
                    actionOpenOrResetAfter(this);
                    update("data-proforma_vente");
                    update("form_entete_proforma_vente");
                    succes();
                } else {
                    getErrorMessage(result != null ? result.getMessage() : "Action impossible!!!");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean saveNewContenu() {
        try {
            contenu.setProforma(proforma);
            if (controleFiche(contenu) && serviceC != null) {
                entityContenu = UtilCom.buildProformaVenteContenu(contenu, currentUser);
                ResultatAction<YvsComProformaVenteContenu> result = null;
                if (contenu.getId() < 1) {
                    result = serviceC.save(entityContenu);
                } else {
                    result = serviceC.update(entityContenu);
                }
                if (result != null ? result.isResult() : false) {
                    entityContenu = (YvsComProformaVenteContenu) result.getData();
                    int index = proforma.getContenus().indexOf(entityContenu);
                    if (index > -1) {
                        proforma.getContenus().set(index, entityContenu);
                    } else {
                        proforma.getContenus().add(entityContenu);
                    }
                    resetFicheContenu();
                    update("tabview-proforma_vente_contenu:data-proforma_vente_contenu");
                    succes();
                } else {
                    getErrorMessage(result != null ? result.getMessage() : "Action impossible!!!");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("saveNewContenu Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public void deleteBean() {
        try {
            if (!autoriser("gescom_pfv_delete")) {
                openNotAcces();
                return;
            }
            if ((entity != null ? entity.getId() > 0 : false) && service != null) {
                if (entity.getStatut().equals(Constantes.ETAT_VALIDE)) {
                    getErrorMessage("Le document doit etre editable");
                    return;
                }
                entity.setAuthor(currentUser);
                entity.setDateUpdate(new Date());
                ResultatAction<YvsComProformaVente> result = service.delete(entity);
                if (result != null ? result.isResult() : false) {
                    proformas.remove(entity);
                    if (entity.getId() == proforma.getId()) {
                        resetFiche();
                    }
                    succes();
                    update("data-proforma_vente");
                } else {
                    getErrorMessage(result != null ? result.getMessage() : "Action impossible!!!");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteAll() {
        try {
            if (!autoriser("gescom_pfv_delete")) {
                openNotAcces();
                return;
            }
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> ids = decomposeIdSelection(tabIds);
                List<YvsComProformaVente> list = new ArrayList<>();
                YvsComProformaVente entity;
                ResultatAction<YvsComProformaVente> result;
                for (Long id : ids) {
                    int index = proformas.indexOf(new YvsComProformaVente(id));
                    if (index > -1) {
                        entity = proformas.get(index);
                        if (entity.getStatut().equals(Constantes.ETAT_VALIDE)) {
                            getErrorMessage("Le document doit etre editable");
                            continue;
                        }
                        entity.setAuthor(currentUser);
                        entity.setDateUpdate(new Date());
                        result = service.delete(entity);
                        if (result != null ? result.isResult() : false) {
                            list.add(entity);
                            if (entity.getId() == proforma.getId()) {
                                resetFiche();
                            }
                        } else {
                            getErrorMessage(result != null ? result.getMessage() : "Action impossible!!!");
                        }
                    }
                }
                if (!list.isEmpty()) {
                    proformas.removeAll(list);
                    update("data-proforma_vente");
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteContenu() {
        try {
            if ((entityContenu != null ? entityContenu.getId() > 0 : false) && serviceC != null) {
                if (proforma.getStatut().equals(Constantes.ETAT_VALIDE)) {
                    getErrorMessage("Le document doit etre editable");
                    return;
                }
                entityContenu.setAuthor(currentUser);
                entityContenu.setDateUpdate(new Date());
                ResultatAction<YvsComProformaVenteContenu> result = serviceC.delete(entityContenu);
                if (result != null ? result.isResult() : false) {
                    proforma.getContenus().remove(entity);
                    if (entityContenu.getId() == contenu.getId()) {
                        resetFicheContenu();
                    }
                    succes();
                    update("tabview-proforma_vente_contenu:data-proforma_vente_contenu");
                } else {
                    getErrorMessage(result != null ? result.getMessage() : "Action impossible!!!");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onSelectObject(YvsComProformaVente y) {
        try {
            entity = y;
            proforma = UtilCom.buildBeanProformaVente(y);
            proforma.setContenus(dao.loadNameQueries("YvsComProformaVenteContenu.findByProformat", new String[]{"proformat"}, new Object[]{y}));
            resetFicheContenu();
            update("form-proforma_vente");
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void onSelectObjectContenu(YvsComProformaVenteContenu y) {
        try {
            entityContenu = y;
            contenu = UtilCom.buildBeanProformaVenteContenu(y);
            contenu.getConditionnement().getArticle().setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{y.getConditionnement().getArticle()}));
            update("desc_artcile_proforma_vente");
            update("tabview-proforma_vente_contenu:form-proforma_vente_contenu");
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        try {
            if (ev != null ? ev.getObject() != null : false) {
                onSelectObject((YvsComProformaVente) ev.getObject());
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        try {
            resetFiche();
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void loadOnViewContenu(SelectEvent ev) {
        try {
            if (ev != null ? ev.getObject() != null : false) {
                onSelectObjectContenu((YvsComProformaVenteContenu) ev.getObject());
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void unLoadOnViewContenu(UnselectEvent ev) {
        try {
            resetFicheContenu();
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void loadOnViewArticle(SelectEvent ev) {
        try {
            if ((ev != null) ? ev.getObject() != null : false) {
                chooseArticle(UtilProd.buildSimpleBeanArticles((YvsBaseArticles) ev.getObject()));
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void changeStatut(String statut) {
        try {
            if (service != null) {
                proforma.setStatut(statut);
                entity.setStatut(statut);
                entity.setDateUpdate(new Date());
                entity.setAuthor(currentUser);
                ResultatAction<YvsComProformaVente> result = service.update(entity);
                if (result != null ? result.isResult() : false) {
                    entity = (YvsComProformaVente) result.getData();
                    int index = proformas.indexOf(entity);
                    if (index > -1) {
                        proformas.set(index, entity);
                    } else {
                        proformas.add(entity);
                    }
                    update("data-proforma_vente");
                    succes();
                } else {
                    getErrorMessage(result != null ? result.getMessage() : "Action impossible!!!");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    private void chooseArticle(Articles article) {
        try {
            if (article != null ? article.getId() > 0 : false) {
                article.setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(article.getId())}));
                contenu.getConditionnement().setArticle(article);
                if (article.getConditionnements() != null ? !article.getConditionnements().isEmpty() : false) {
                    contenu.setConditionnement(UtilProd.buildBeanConditionnement(article.getConditionnements().get(0)));
                }
                contenu.getConditionnement().setArticle(article);
                update("desc_artcile_proforma_vente");
                update("tabview-proforma_vente_contenu:blog_form_proforma_vente_contenu");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void chooseConditionnement() {
        try {
            if (contenu.getConditionnement() != null ? contenu.getConditionnement().getId() > 0 : false) {
                int index = contenu.getConditionnement().getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(contenu.getConditionnement().getId()));
                if (index > -1) {
                    YvsBaseConditionnement y = contenu.getConditionnement().getArticle().getConditionnements().get(index);
                    List<YvsBaseConditionnement> list = new ArrayList<>(y.getArticle().getConditionnements());
                    contenu.setConditionnement(UtilProd.buildBeanConditionnement(y));
                    contenu.getConditionnement().getArticle().setConditionnements(list);
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void searchArticle() {
        try {
            String num = contenu.getConditionnement().getArticle().getRefArt();
            contenu.getConditionnement().getArticle().setDesignation("");
            contenu.getConditionnement().getArticle().setError(true);
            contenu.getConditionnement().getArticle().setId(0);
            ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
            if (m != null) {
                Articles y = m.searchArticleActif("V", num, true);
                if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                    if (m.getArticlesResult().size() > 1) {
                        update("data-articles_proforma_vente_contenu");
                    } else {
                        chooseArticle(y);
                    }
                    contenu.getConditionnement().getArticle().setError(false);
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void print(YvsComProformaVente y) {
        print(y, true);
    }

    public void print(YvsComProformaVente y, boolean withHeader) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Map<String, Object> param = new HashMap<>();
                param.put("ID", y.getId().intValue());
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", SUBREPORT_DIR(withHeader));
                executeReport("proforma_vente", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearParams() {
        try {
            numeroSearch = "";
            statutSearch = "";
            addDate = false;
            dateDebutSearch = new Date();
            dateFinSearch = new Date();
            valideSearch = null;
            clientSearch = null;
            paginator.clear();
            loadAll(false, true);
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void addParamClient() {
        try {
            ParametreRequete p = new ParametreRequete("y.client.codeClient", "client", null);
            if (clientSearch != null ? clientSearch.trim().length() > 0 : false) {
                p = new ParametreRequete("UPPER(y.client)", "client", clientSearch.toUpperCase() + "%", "LIKE", "AND");
            }
            paginator.addParam(p);
            loadAll(false, true);
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void addParamNumero() {
        try {
            ParametreRequete p = new ParametreRequete("y.numero", "numero", null);
            if (numeroSearch != null ? numeroSearch.trim().length() > 0 : false) {
                p = new ParametreRequete("UPPER(y.numero)", "numero", numeroSearch.toUpperCase() + "%", "LIKE", "AND");
            }
            paginator.addParam(p);
            loadAll(false, true);
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void addParamStatut() {
        try {
            ParametreRequete p = new ParametreRequete("y.statut", "statut", null);
            if (statutSearch != null ? statutSearch.trim().length() > 0 : false) {
                String egalite = "=";
                if (statutSearch.startsWith("=")) {
                    egalite = "=";
                } else {
                    egalite = "!=";
                }
                String statut = statutSearch.replace(egalite, "");
                p = new ParametreRequete("y.statut", "statut", statut, egalite, "AND");
            }
            paginator.addParam(p);
            loadAll(false, true);
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void addParamDates() {
        try {
            ParametreRequete p = new ParametreRequete("y.dateDoc", "dateDoc", null);
            if (addDate) {
                p = new ParametreRequete("y.dateDoc", "dateDoc", dateDebutSearch, dateFinSearch, "BETWEEN", "AND");
            }
            paginator.addParam(p);
            loadAll(false, true);
        } catch (Exception ex) {
            getErrorMessage("addParamDates Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void addParamValide() {
        try {
            ParametreRequete p = new ParametreRequete("y.dateExpiration", "dateExpiration", null);
            if (valideSearch != null) {
                p = new ParametreRequete("y.dateExpiration", "dateExpiration", new Date(), valideSearch ? ">" : "<=", "AND");
            }
            paginator.addParam(p);
            loadAll(false, true);
        } catch (Exception ex) {
            getErrorMessage("addParamDates Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

}
