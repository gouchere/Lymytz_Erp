/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.vente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.ManagedArticles;
import yvs.commercial.UtilCom;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComContenuDocVenteEtat;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComDocVentesInformations;
import yvs.entity.produits.YvsBaseArticles;
import yvs.parametrage.entrepot.Depots;
import yvs.service.IEntitySax;
import yvs.service.com.vente.IYvsComContenuDocVenteEtat;
import yvs.service.com.vente.IYvsComDocVentes;
import yvs.service.com.vente.IYvsComDocVentesInformations;
import yvs.util.Constantes;
import static yvs.util.Managed.ldf;
import static yvs.util.Managed.time;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedLocationVente extends ManagedFactureVenteV2 implements Serializable {

    private YvsComDocVentes retour = new YvsComDocVentes();
    private YvsComContenuDocVenteEtat selectEtat;
    private ContenuDocVenteEtat etat = new ContenuDocVenteEtat();
    private YvsComContenuDocVente selectContenuRetour = new YvsComContenuDocVente();
    private PaginatorResult<YvsBaseArticles> paginatorArticle = new PaginatorResult<>();
    private List<YvsBaseArticles> articles = new ArrayList<>();

    public ManagedLocationVente() {
        super();
        this.natureVente = yvs.util.Constantes.LOCATION;
    }

    public YvsComContenuDocVente getSelectContenuRetour() {
        return selectContenuRetour;
    }

    public void setSelectContenuRetour(YvsComContenuDocVente selectContenuRetour) {
        this.selectContenuRetour = selectContenuRetour;
    }

    public YvsComContenuDocVenteEtat getSelectEtat() {
        return selectEtat;
    }

    public void setSelectEtat(YvsComContenuDocVenteEtat selectEtat) {
        this.selectEtat = selectEtat;
    }

    public ContenuDocVenteEtat getEtat() {
        return etat;
    }

    public void setEtat(ContenuDocVenteEtat etat) {
        this.etat = etat;
    }

    public YvsComDocVentes getRetour() {
        return retour;
    }

    public void setRetour(YvsComDocVentes retour) {
        this.retour = retour;
    }

    private boolean controleFiche(DocVenteInformation bean) {
        if (bean == null) {
            getErrorMessage("L'élément ne peut pas etre null");
            return false;
        }
        if (getDocVente().getNature().equals(Constantes.LOCATION)) {
            if ((bean.getAdresseLivraison() != null) ? bean.getAdresseLivraison().getId() < 1 : true) {
                getErrorMessage("L'adresse de livraison ne peut pas etre null");
                return false;
            }
        }
        if (!getDocVente().getNature().equals(Constantes.VENTE)) {
            if (bean.getDateFin() == null) {
                getErrorMessage("La date de fin ne peut pas etre null");
                return false;
            }
        }
        return true;
    }

    public void loadActifArticle(boolean avancer, boolean init) {
        String queryFrom = "YvsBaseArticles y JOIN FETCH y.famille ";
        paginatorArticle.addParam(new ParametreRequete("y.actif", "actif", true, "=", "AND"));
        articles = paginatorArticle.executeDynamicQuery("y", "y", queryFrom, "y.refArt", avancer, init, (int) imax, dao);

    }

    public Articles searchArticleActif(String num, boolean open) {
        Articles a = new Articles();
        a.setRefArt(num);
        a.setError(true);
        if (num != null ? num.trim().length() > 0 : false) {
            ParametreRequete p = new ParametreRequete(null, "refArt", num + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.designation)", "designation", num.toUpperCase(), "LIKE", " OR "));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.refArt)", "refArt", num.toUpperCase(), "LIKE", " OR "));
            paginatorArticle.addParam(p);
            loadActifArticle(true, true);
            a = chechArticleResult(open, true);
            if (a != null ? a.getId() < 1 : true) {
                a.setRefArt(num);
                a.setError(true);
            }
        }
        return a;
    }

    public void searchArticle() {
        String num = getContenu().getArticle().getRefArt();
        getContenu().getArticle().setDesignation("");
        getContenu().getArticle().setError(true);
        getContenu().getArticle().setId(0);
        Articles y = searchArticleActif(num, true);
//            listArt = y.isListArt();
//            selectArt = y.isSelectArt();
        if (articles.size() > 1) {
            update("data_articles_facture_vente");
        } else {
            chooseArticle(y);
        }
        getContenu().getArticle().setError(false);

    }

    private Articles chechArticleResult(boolean open, boolean art) {
        Articles a = new Articles();
        ManagedArticles service = (ManagedArticles) giveManagedBean("Marticle");
        if (!art) {
            if (articles != null ? !articles.isEmpty() : false) {
                if (articles.size() > 1) {
                    if (open) {
                        openDialog("dlgListArticle");
                    }
                    a.setListArt(true);
                } else {
                    if (service != null) {
                        YvsBaseArticles y = articles.get(0);
                        a = service.chechArticleResult(y);
                        a.setRequiereLot(false);
                        a.setSellWithOutStock(true);
                        a.setSelectArt(true);
                    }
                }
                a.setError(false);
            }
        } else {
            if (articles != null ? !articles.isEmpty() : false) {
                if (articles.size() > 1) {
                    if (open) {
                        openDialog("dlgListArticle");
                    }
                    a.setListArt(true);
                } else {
                    YvsBaseArticles c = articles.get(0);
                    a = service.chechArticleResult(c);
                    a.setSelectArt(true);
                }
                a.setError(false);
            }
        }
        return a;
    }

    @Override
    public void initView(boolean venteDirecte) {
        super.initView(venteDirecte, Constantes.SERVICE); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void openDlgFactures(boolean listing) {
        super.openDlgFactures(listing, Constantes.SERVICE);
    }

    @Override
    public boolean saveNew() {
        //To change body of generated methods, choose Tools | Templates.$
        try {
            if (controleFiche(getDocVente().getInformation())) {
                boolean succes = super._saveNew(true);
                if (succes) {
                    IYvsComDocVentesInformations impl = (IYvsComDocVentesInformations) (new IEntitySax()).createInstance("IYvsComDocVentesInformations", dao);
                    if (impl != null) {
                        impl.setNiveauAcces(currentNiveau);
                        impl.setAgence(currentAgence);
                        getDocVente().getInformation().setFacture(getDocVente());
                        YvsComDocVentesInformations entity = UtilCom.buildDocVenteInformation(getDocVente().getInformation(), currentUser);
                        if (entity != null) {
                            ResultatAction<YvsComDocVentesInformations> result;
                            if (entity.getId() < 1) {
                                result = impl.save(entity);
                            } else {
                                result = impl.update(entity);
                            }
                            if (result != null ? result.isResult() : false) {
                                getDocVente().getInformation().setId(result.getIdEntity());
                                succes();
                                actionOpenOrResetAfter(this);
                                return true;
                            } else {
                                getErrorMessage(result != null ? result.getMessage() : "Action Impossible!!!");
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean saveNewEtat() {
        //To change body of generated methods, choose Tools | Templates.$
        try {
            etat.setContenu(new ContenuDocVente(getSelectContenu().getId()));
            YvsComContenuDocVenteEtat entity = UtilCom.buildContenuDocVenteEtat(etat, currentUser);
            IYvsComContenuDocVenteEtat impl = (IYvsComContenuDocVenteEtat) (new IEntitySax()).createInstance("IYvsComContenuDocVenteEtat", dao);
            if (impl != null) {
                impl.setNiveauAcces(currentNiveau);
                impl.setAgence(currentAgence);
                if (entity != null) {
                    entity.setContenu(getSelectContenu());
                    ResultatAction<YvsComContenuDocVenteEtat> result;
                    if (entity.getId() < 1) {
                        if (getSelectContenu() != null ? getSelectContenu().getId() > 0 : false) {
                            result = impl.save(entity);
                        } else {
                            result = new ResultatAction<>(true, entity, entity.getId(), "");
                        }
                    } else {
                        if (getSelectContenu() != null ? getSelectContenu().getId() > 0 : false) {
                            result = impl.update(entity);
                        } else {
                            result = new ResultatAction<>(true, entity, entity.getId(), "");
                        }
                    }
                    if (result != null ? result.isResult() : false) {
                        entity.setId(result.getIdEntity());
                        int idx = getSelectContenu().getEtats().indexOf(entity);
                        if (idx < 0) {
                            getSelectContenu().getEtats().add(entity);
                        } else {
                            getSelectContenu().getEtats().set(idx, entity);
                        }
                        if (getSelectContenu() != null ? getSelectContenu().getId() > 0 : false) {
                            idx = getDocVente().getContenus().indexOf(getSelectContenu());
                            if (idx > -1) {
                                getDocVente().getContenus().set(idx, getSelectContenu());
                            }
                        } else {
                            idx = retour.getContenus().indexOf(getSelectContenu());
                            if (idx > -1) {
                                retour.getContenus().set(idx, getSelectContenu());
                            }
                        }
                        etat = new ContenuDocVenteEtat();
                        succes();
                        return true;
                    } else {
                        getErrorMessage(result != null ? result.getMessage() : "Action Impossible!!!");
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void saveNewRetour() {
        try {
            if (retour != null) {
                if (retour.getDepotLivrer() != null ? retour.getDepotLivrer().getId() < 1 : true) {
                    getErrorMessage("Aucun dépôt de livraison n'a été trouvé !");
                    return;
                }
                if (retour.getTrancheLivrer() != null ? retour.getTrancheLivrer().getId() < 1 : true) {
                    getErrorMessage("Aucune tranche de livraison n'a été trouvé !");
                    return;
                }
                if (retour.getDateLivraison() != null ? retour.getDateLivraison().after(new Date()) : true) {
                    getErrorMessage("La date de livraison est incorrecte !");
                    return;
                }
                if (retour.getDocumentLie() != null ? retour.getDocumentLie().getId() < 1 : true) {
                    getErrorMessage("Le retour doit etre rattaché a une facture !");
                    return;
                }
                if (retour.getDocumentLie().getStatutLivre().equals(Constantes.ETAT_ATTENTE)) {
                    getErrorMessage("La facture doit etre livrée ou en cours de livraison");
                    return;
                }
                if (!verifyOperation(new Depots(retour.getDepotLivrer().getId(), retour.getDepotLivrer().getDesignation()), Constantes.ENTREE, Constantes.RETOUR, false)) {
                    return;
                }
                if (!verifyInventaire(retour.getDepotLivrer(), retour.getTrancheLivrer(), retour.getDateLivraison())) {
                    return;
                }
                String num = genererReference(Constantes.TYPE_BRL_NAME, retour.getDateLivraison(), retour.getDepotLivrer().getId(), Constantes.DEPOT);
                if (!Util.asString(num)) {
                    return;
                }
                IYvsComDocVentes impl = (IYvsComDocVentes) (new IEntitySax()).createInstance("IYvsComDocVentes", dao);
                if (impl != null) {
                    retour.setNumDoc(num);
                    retour.setDescription("Retour de la location N° " + getDocVente().getNumDoc() + " le " + ldf.format(retour.getDateLivraison()) + " à " + time.format(retour.getDateLivraison()));
                    if (retour.getId() > 0 ? retour.getId().equals(getSelectDoc().getId()) : true) {
                        List<YvsComContenuDocVente> contenus = new ArrayList<>(retour.getContenus());
                        retour.getContenus().clear();

                        retour.setId(null);
                        retour = (YvsComDocVentes) dao.save1(retour);
                        for (int i = 0; i < contenus.size(); i++) {
                            YvsComContenuDocVente c = contenus.get(i);
                            if (c.getQuantite() > 0) {
                                List<YvsComContenuDocVenteEtat> etats = new ArrayList<>(c.getEtats());
                                c.getEtats().clear();

                                c.setId(null);
                                c.setDateSave(new Date());
                                c.setDateUpdate(new Date());
                                c.setAuthor(currentUser);
                                c.setDocVente(retour);
                                c = (YvsComContenuDocVente) dao.save1(c);
                                for (int j = 0; j < etats.size(); j++) {
                                    YvsComContenuDocVenteEtat e = etats.get(j);
                                    e.setId(null);
                                    e.setContenu(c);
                                }
                                retour.getContenus().add(c);
                            }
                        }
                    } else {
                        retour.setDateSave(new Date());
                        retour.setDateUpdate(new Date());
                        retour.setAuthor(currentUser);
                        dao.update(retour);
                    }
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBeanEtat(YvsComContenuDocVenteEtat y, boolean delete) {
        selectEtat = y;
        if (delete) {
            try {
                dao.delete(y);
                if (Objects.equals(etat.getId(), y.getId())) {
                    etat = new ContenuDocVenteEtat();
                }
                getSelectContenu().getEtats().remove(y);
                succes();
            } catch (Exception ex) {
                getErrorMessage("Operation Impossible !");
                log.log(Level.SEVERE, null, ex);
            }
        }
    }

    public void loadOnViewEtat(SelectEvent ev) {
        try {
            if (ev != null ? ev.getObject() != null : false) {
                etat = UtilCom.buildBeanContenuDocVenteEtat((YvsComContenuDocVenteEtat) ev.getObject());
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void unLoadOnViewEtat(UnselectEvent ev) {
        try {
            etat = new ContenuDocVenteEtat();
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void loadOnViewRetour(SelectEvent ev) {
        try {
            if (ev != null ? ev.getObject() != null : false) {
                retour = (YvsComDocVentes) ev.getObject();
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void unLoadOnViewRetour(UnselectEvent ev) {
        try {
            retour = new YvsComDocVentes();
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void onSelectFacture(YvsComDocVentes y) {
        ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
        if (w != null) {
            w.onSelectObject(y);
            cloneObject(getDocVente(), w.getDocVente());
        }
    }

    public void deleteContenuRetour(YvsComContenuDocVente y, boolean delete) {
        if (y != null) {
            if (!retour.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Le document doit etre editable");
                return;
            }
            if (y.getId() < 1 || (y.getParent() != null ? y.getParent().getId().equals(y.getId()) : false)) {
                retour.getContenus().remove(y);
            } else {
                selectContenuRetour = y;
                if (!delete) {
                    openDialog("dlgConfirmDeleteArticleRetour");
                } else {
                    dao.delete(y);
                    retour.getContenus().remove(y);
                }
            }
        }
    }

    public void changeQuantite(CellEditEvent ev) {
        int idx = ev.getRowIndex();
        Double oldValue = (Double) ev.getOldValue();
        Double newValue = (Double) ev.getNewValue();
        if (idx >= 0 && newValue > 0) {
            if (newValue > oldValue) {
                retour.getContenus().get(idx).setQuantite(oldValue);
                getErrorMessage("Vous ne pouvez pas faire un retour de plus de " + DNA(oldValue));
                update("table-contenu_retour");
            }
        }
    }

    public void openDlgRetour() {
        getDocVente().setDocuments(dao.loadNameQueries("YvsComDocVentes.findBRLByParent", new String[]{"documentLie"}, new Object[]{getSelectDoc()}));
        retour = genererRetour(getSelectDoc(), true);
        update("data_retour_location_vente");
    }

    @Override
    public void openDlgDocument() {
        getDocVente().setDocuments(dao.loadNameQueries("YvsComDocVentes.findNotBLVBRLByParent", new String[]{"documentLie"}, new Object[]{getSelectDoc()}));
        update("data_livraison_facture_vente");
    }

    @Override
    public void onSelectObject(YvsComDocVentes y, boolean complet) {
        super.onSelectObject(y, complet); //To change body of generated methods, choose Tools | Templates.
        Long count = (Long) dao.loadObjectByNameQueries("YvsComDocVentes.countNotBLVBRLByParent", new String[]{"documentLie"}, new Object[]{getSelectDoc()});
        getDocVente().setNbreDocLie(count != null ? count : 0);
    }

    public List<YvsComContenuDocVente> loadContenusStay(YvsComDocVentes y) {
        List<YvsComContenuDocVente> list = new ArrayList<>();
        y.setInt_(false);
        nameQueri = "YvsComContenuDocVente.findByDocVente";
        champ = new String[]{"docVente"};
        val = new Object[]{y};
        List<YvsComContenuDocVente> contenus = dao.loadNameQueries(nameQueri, champ, val);
        Double qteRetour;
        Double qteLivre;
        for (YvsComContenuDocVente c : contenus) {
            champ = new String[]{"parent", "typeDoc", "statut"};
            val = new Object[]{c, Constantes.TYPE_BLV, Constantes.ETAT_VALIDE};
            qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByTypeStatutParent", champ, val);
            val = new Object[]{c, Constantes.TYPE_BRL, Constantes.ETAT_VALIDE};
            qteRetour = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByTypeStatutParent", champ, val);
            if (c.getQuantite() > (qteRetour != null ? qteRetour : 0)) {
                c.setQuantite_(c.getQuantite());
                c.setQteLivree(qteLivre != null ? qteLivre : 0);
                c.setQuantite(c.getQteLivree() - (qteRetour != null ? qteRetour : 0));
                c.setPrixTotal(c.getPrix() * c.getQuantite());
                c.setParent(new YvsComContenuDocVente(c.getId()));
                c.setId(Long.valueOf(-(list.size() + 1)));
                c.getEtats().clear();
                list.add(c);
            }
        }
        return list;
    }

    public YvsComDocVentes genererRetour(YvsComDocVentes facture, boolean message) {
        YvsComDocVentes retour = new YvsComDocVentes();
        try {
            if (!autoriser("fv_livrer")) {
                if (message) {
                    openNotAcces();
                }
                return retour;
            }
            if (facture == null) {
                if (message) {
                    getErrorMessage("Vous devez selectionner la facture");
                }
                return retour;
            }

            if (facture.getEnteteDoc() == null) {
                if (message) {
                    getErrorMessage("La facture n'a pas de journal de vente");
                }
                return retour;
            }
            List<YvsComContenuDocVente> contenus = loadContenusStay(facture);
            if (contenus != null ? !contenus.isEmpty() : false) {
                retour = new YvsComDocVentes(facture);
                retour.setId(facture.getId());
                retour.setEnteteDoc(facture.getEnteteDoc());
                retour.setAuthor(currentUser);
                retour.setTypeDoc(Constantes.TYPE_BRL);
                retour.setNumPiece("BRL N° " + facture.getNumDoc());
                retour.setDepotLivrer(facture.getDepotLivrer());
                retour.setTrancheLivrer(facture.getTrancheLivrer());
                retour.setLivreur(currentUser.getUsers());
                retour.setDocumentLie(new YvsComDocVentes(facture.getId(), facture.getNumDoc(), facture.getStatut(), facture.getStatutLivre(), facture.getStatutRegle()));
                retour.setHeureDoc(new Date());
                retour.setStatut(Constantes.ETAT_EDITABLE);
                retour.setStatutLivre(Constantes.ETAT_ATTENTE);
                retour.setStatutRegle(Constantes.ETAT_ATTENTE);
                retour.setValiderBy(currentUser.getUsers());
                retour.setDateValider(facture.getDateLivraisonPrevu());
                retour.setDateSave(new Date());
                retour.setDateUpdate(new Date());
                retour.setCloturer(false);
                retour.setContenus(contenus);
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
        return retour;
    }

    public void chooseDepotRetour() {
        getTranches().clear();
        if (retour.getDepotLivrer() != null ? retour.getDepotLivrer().getId() > 0 : false) {
            setTranches(loadTranche(retour.getDepotLivrer(), isLoadOnlyDepotPoint() ? retour.getDateLivraison() : null));
        }
    }

    public boolean annulerRetour(YvsComDocVentes entity) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                entity.setCloturer(false);
                entity.setAnnulerBy(null);
                entity.setCloturerBy(null);
                entity.setValiderBy(null);
                entity.setDateAnnuler(null);
                entity.setDateCloturer(null);
                entity.setDateValider(null);
                entity.setStatut(Constantes.ETAT_EDITABLE);
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    entity.setAuthor(currentUser);
                }
                List<YvsComContenuDocVente> contenus = new ArrayList<>(entity.getContenus());
                entity.getContenus().clear();
                dao.update(entity);
                entity.setContenus(contenus);
                if (getDocVente().getDocuments() != null) {
                    int idx = getDocVente().getDocuments().indexOf(entity);
                    if (idx > -1) {
                        getDocVente().getDocuments().set(idx, entity);
                    }
                }
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("ManagedLocationVente (annulerRetour)", ex);
        }
        return false;
    }

    public boolean refuserRetour(YvsComDocVentes entity) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                entity.setCloturer(false);
                entity.setAnnulerBy(null);
                entity.setCloturerBy(null);
                entity.setValiderBy(null);
                entity.setDateAnnuler(null);
                entity.setDateCloturer(null);
                entity.setDateValider(null);
                entity.setStatut(Constantes.ETAT_ANNULE);
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    entity.setAuthor(currentUser);
                }
                List<YvsComContenuDocVente> contenus = new ArrayList<>(entity.getContenus());
                entity.getContenus().clear();
                dao.update(entity);
                entity.setContenus(contenus);
                if (getDocVente().getDocuments() != null) {
                    int idx = getDocVente().getDocuments().indexOf(entity);
                    if (idx > -1) {
                        getDocVente().getDocuments().set(idx, entity);
                    }
                }
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("ManagedLocationVente (refuserRetour)", ex);
        }
        return false;
    }

    public boolean validerRetour(YvsComDocVentes entity) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                entity.setCloturer(false);
                entity.setCloturerBy(null);
                entity.setDateCloturer(null);
                entity.setAnnulerBy(null);
                entity.setDateAnnuler(null);
                entity.setValiderBy(currentUser.getUsers());
                entity.setDateValider(new Date());
                entity.setStatut(Constantes.ETAT_VALIDE);
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    entity.setAuthor(currentUser);
                }
                List<YvsComContenuDocVente> contenus = new ArrayList<>(entity.getContenus());
                entity.getContenus().clear();
                dao.update(entity);
                entity.setContenus(contenus);
                if (getDocVente().getDocuments() != null) {
                    int idx = getDocVente().getDocuments().indexOf(entity);
                    if (idx > -1) {
                        getDocVente().getDocuments().set(idx, entity);
                    }
                }
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("ManagedLocationVente (validerRetour)", ex);
        }
        return false;
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllFacture(true);
    }
}
