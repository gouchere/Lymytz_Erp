/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.services.commercial;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Options;
import yvs.dao.Util;
import yvs.dao.local.UtilsBean;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.GenericService;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.YvsComParametreStock;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComContenuDocStockReception;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
public class ServiceTransfert extends GenericService {

    public ServiceTransfert(DaoInterfaceLocal dao, YvsNiveauAcces niveau, YvsUsersAgence user) {
        this.dao = dao;
        this.niveau = niveau;
        this.currentUser = user;
    }

    public ServiceTransfert(DaoInterfaceLocal dao, YvsNiveauAcces niveau, YvsUsersAgence user, YvsSocietes societe) {
        this(dao, niveau, user);
        this.currentScte = societe;
    }

    public ResultatAction valideTransfert(YvsComDocStocks doc) {
        ResultatAction r = changeStatut_(Constantes.ETAT_VALIDE, doc);
        if (r.isResult()) {
            doc.setCloturer(false);
            doc.setAnnulerBy(null);
            doc.setValiderBy(currentUser.getUsers());
            doc.setDateAnnuler(null);
            doc.setDateCloturer(null);
            doc.setDateValider(new Date());
            doc.setDateUpdate(new Date());
            doc.setStatut(Constantes.ETAT_VALIDE);
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                doc.setAuthor(currentUser);
            }
            dao.update(doc);
        } else {
            return r;
        }
        return result.succes();
    }

    public ResultatAction controleUpdateContent(YvsComContenuDocStock lineContent, YvsComDocStocks document, Date dateReception, String statutDoc, boolean allTransfert) {
        if (lineContent == null || document == null) {
            return result.emptyDoc();
        }
        if (lineContent.getId() == null || document.getId() == null) {
            return result.emptyDoc();
        }
        if (!autoriser("tr_change_statut_line")) {
            return result.operationNotAllowHere();        
        }
        if (!chechAutorisationActionOnDepot(document, 2, currentUser.getUsers())) {
            return result.userNotAbility();
        }
        if (!allTransfert) {
            if (!dao.controleInventaire(lineContent.getDocStock().getDestination().getId(), lineContent.getDateReception(), lineContent.getDocStock().getCreneauDestinataire().getTranche().getId())) {
                return result.inventaireLock(document.getSource().getDesignation());
            }
        }
        if (!checkAutorisationUpdateTransfertInPast(document)) {
            return result.timeIsPast();
        }
        return result.succes();
    }

    private ResultatAction checkDocument(YvsComDocStocks bean, boolean control_tranche_source, boolean control_tranche_destination) {
        if (bean == null) {
            return result.emptyDoc();
        }
        final ResultatAction resultDate = verifyDate(bean.getDateDoc(), -1);
        if (!resultDate.isResult()) {
            return resultDate;
        }
        if (bean.getDateReception().before(bean.getDateDoc())) {
            return result.dateNotCorrect();
        }
        if (bean.getSource() == null ? true : bean.getSource().getId() <= 0) {
            return result.emptyDepot(true);
        }
        if (bean.getDestination() == null ? true : bean.getDestination().getId() <= 0) {
            return result.emptyDepot(false);
        }
        if (control_tranche_destination) {
            if ((bean.getCreneauDestinataire() != null) ? bean.getCreneauDestinataire().getId() < 1 : true) {
                return result.emptyCreneauHoraire(false);
            }
        }
        if (control_tranche_source) {
            if ((bean.getCreneauSource() != null) ? bean.getCreneauSource().getId() < 1 : true) {
                return result.emptyCreneauHoraire(true);
            }
        }
        if (!checkTypeOperationInDepot(bean.getSource().getId(), Constantes.TRANSFERT, Constantes.TRANSFERT)) {
            return result.operationNotAllow(bean.getSource().getDesignation(), Constantes.TRANSFERT);
        }
        if (!checkTypeOperationInDepot(bean.getDestination().getId(), Constantes.TRANSFERT, Constantes.TRANSFERT)) {
            return result.operationNotAllow(bean.getDestination().getDesignation(), Constantes.TRANSFERT);
        }
        if (bean.getCloturer()) {
            return result.docIsAlreadyClose();
        }
        if (!dao.autoriser("tr_valid_all", niveau) && Constantes.ETAT_VALIDE.equals(bean.getStatut())) {
            //droit de super validation des transfert (permet de se passer des autorisations dans un dépot)
            if (!chechAutorisationActionOnDepot(bean, 2, currentUser.getUsers())) {
                return result.userNotAbility();
            }
        }
        if (bean.getNumDoc() == null || bean.getNumDoc().equals("")) {
            UtilsBean util = new UtilsBean(null, dao);
            String ref = util.genererReference(Constantes.TYPE_FT_NAME, bean.getDateDoc(), bean.getSource().getId(), Constantes.DEPOT, "", currentUser.getAgence());
            bean.setNumDoc(ref);
            if (ref == null || ref.trim().equals("")) {
                return result.numDocNotGenerated();
            }
        }
        if (!autoriser("tr_edit_all_time")) {
            int ecart = getEcartSaveOrUpdateDocStock(bean);
            ResultatAction r = verifyDate(bean.getDateDoc(), ecart);
            if (!r.isResult()) {
                return r;
            }
        }
        // Vérifié qu'aucun document d'inventaire n'exite après cette date
        Long nb = (Long) dao.loadObjectByNameQueries("YvsComDocStocks.findByDocInventaireAfter", new String[]{"statut", "depot", "date", "type", "heure", "typeJourne"}, new Object[]{Constantes.ETAT_VALIDE, bean.getSource(), bean.getDateDoc(), Constantes.TYPE_IN, bean.getCreneauSource().getTranche().getHeureFin(), bean.getCreneauSource().getTranche().getTypeJournee()});
        if (nb != null ? nb > 0 : false) {
            return result.inventaireLock(bean.getSource().getDesignation());
        }
        nb = (Long) dao.loadObjectByNameQueries("YvsComDocStocks.findByDocInventaireAfter", new String[]{"statut", "depot", "date", "type", "heure", "typeJourne"}, new Object[]{Constantes.ETAT_VALIDE, bean.getDestination(), bean.getDateDoc(), Constantes.TYPE_IN, bean.getCreneauDestinataire().getTranche().getHeureFin(), bean.getCreneauDestinataire().getTranche().getTypeJournee()});
        if (nb != null ? nb > 0 : false) {
            return result.inventaireLock(bean.getDestination().getDesignation());
        }
        return result.succes();
    }

    public ResultatAction checkBeforChangeStatus(YvsComDocStocks bean, boolean control_tranche_source, boolean control_tranche_destination) {
        return checkDocument(bean, control_tranche_source, control_tranche_destination);
    }

    public ResultatAction _controleFiche(YvsComDocStocks bean, boolean control_tranche_source, boolean control_tranche_destination) {
        if (!isEditableStatut(bean)) {
            return result.docIsNotEditable();
        }
        return checkDocument(bean, control_tranche_source, control_tranche_destination);
    }

    public boolean checkTypeOperationInDepot(long depot, String type, String operation) {
        Long nb = (Long) dao.loadObjectByNameQueries("YvsBaseDepotOperation.countByDepotTypeOperation", new String[]{"depot", "type", "operation"}, new Object[]{new YvsBaseDepots(depot), type, operation});
        return nb != null ? nb > 0 : false;
    }

    private Date hier(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    public boolean checkAutorisationUpdateTransfertInPast(YvsComDocStocks doc) {
        int limit = getEcartSaveOrUpdateDocStock(doc);
        int jourPasse = Util.countDayBetweenDates(doc.getDateDoc(), new Date());
        if (jourPasse >= limit) {
            if (!autoriser("tr_add_content_after_valide")) {
                return false;
            }
        }
        return true;
    }

    private int getEcartSaveOrUpdateDocStock(YvsComDocStocks doc) {
        if (doc == null) {
            return 0;
        }
        if (doc.getDateDoc() == null) {
            return 0;
        }
        YvsComParametreStock ps = (YvsComParametreStock) dao.loadOneByNameQueries("YvsComParametreStock.findByAgence", new String[]{"agence"}, new Object[]{currentUser.getAgence()});
        return (ps != null) ? ((ps.getDureeUpdate() != null) ? ps.getDureeUpdate() : 0) : 0;
    }

    private boolean isEditableStatut(YvsComDocStocks doc) {
        return Constantes.ETAT_EDITABLE.equals(doc.getStatut());
    }

    /**
     * Vérifie si je suis habilité à passer un movement dans un dépôt.
     *
     * @param doc documement de stock conserné
     * @param action
     * @param currentUser
     * @return
     */
    public boolean chechAutorisationActionOnDepot(YvsComDocStocks doc, int action, YvsUsers currentUser) {
        if (dao.autoriser("tr_valid_all", niveau)) {
            return true;
        }
        if (currentUser != null && doc != null) {
            switch (action) {
                case 1: { //Cas de l'emetteur du transfert
                    if (doc.getSource() != null) {
                        Long nb = (Long) dao.loadObjectByNameQueries("YvsComCreneauHoraireUsers.ifUserHasPlaning", new String[]{"hier", "date", "users", "crenoD"}, new Object[]{hier(doc.getDateDoc()), doc.getDateDoc(), currentUser, doc.getCreneauSource()});
                        final boolean correct = nb != null ? nb > 0 : false;
                        if (!correct) {
                            //contrôle le code d'accès de l'utilisateur
                            if (doc.getSource().getCodeAcces() != null ? doc.getSource().getCodeAcces().getCode() != null : false) {
                                nb = (Long) dao.loadObjectByNameQueries("YvsBaseUsersAcces.findIdAcces", new String[]{"users", "code", "societe"}, new Object[]{currentUser, doc.getSource().getCodeAcces().getCode(), currentScte});
                                if (nb != null ? nb > 0 : false) {
                                    return true;
                                }
                            }
                        } else {
                            return true;
                        }
                    }
                    break;
                }
                case 2: {   //cas du destinataire du transfert
                    if (doc.getDestination() != null) {
                        Long nb = (Long) dao.loadObjectByNameQueries("YvsComCreneauHoraireUsers.ifUserHasPlaning", new String[]{"hier", "date", "users", "crenoD"}, new Object[]{hier(doc.getDateReception()), doc.getDateReception(), currentUser, doc.getCreneauDestinataire()});
                        final boolean correct = nb != null ? nb > 0 : false;
                        if (!correct) {
                            //contrôle le code d'accès de l'utilisateur
                            if (doc.getDestination().getCodeAcces() != null ? doc.getDestination().getCodeAcces().getCode() != null : false) {
                                nb = (Long) dao.loadObjectByNameQueries("YvsBaseUsersAcces.findIdAcces", new String[]{"users", "code", "societe"}, new Object[]{currentUser, doc.getDestination().getCodeAcces().getCode(), currentScte});
                                if (nb != null ? nb > 0 : false) {
                                    return true;
                                }
                            }
                        } else {
                            return true;
                        }
                    }
                    break;
                }
                default:
                    return false;
            }
        } else {
            return false;
        }
        return false;
    }

    public ResultatAction changeStatut_(String etat, YvsComDocStocks doc) {
        if (!"".equals(etat) && doc != null) {
            ResultatAction r = _controleFiche(doc, true, true);
            if (!r.isResult()) {
                return r;
            }
            for (YvsComContenuDocStock c : doc.getContenus()) {
                changeStatutLine(c, doc.getDateReception(), etat, true);
            }
            actualiseStatutDoc(doc);
            return new ResultatAction().succes();
        }
        return new ResultatAction().fail("Le statut du document est incorrect !");
    }

    public ResultatAction changeStatutLine(YvsComContenuDocStock y, Date dateReception, String statutDoc, boolean allTransfert) {
        ResultatAction resultService = new ResultatAction();
        if (y == null) {
            return new ResultatAction().fail("Aucun documents n'a été trouvé !");
        }
        if (y.getId() > 0) {
            if (!allTransfert) {
                resultService = _controleFiche(y.getDocStock(), true, (y.getDocStock().getStatut().equals(Constantes.ETAT_SOUMIS) || y.getStatut().equals(Constantes.ETAT_ENCOURS)));
                if (!resultService.isResult()) {
                    return resultService;
                }
            }
            if (!allTransfert) {
                if (!dao.controleInventaire(y.getDocStock().getDestination().getId(), y.getDateReception(), y.getDocStock().getCreneauDestinataire().getTranche().getId())) {
                    return resultService.inventaireLock(y.getDocStock().getDestination().getDesignation());
                }
            }
            if (!y.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                //Cette verification évite de switcher sur les statuts des contenus lorsqu'on valide à partir du document
                if (!allTransfert || statutDoc.equals(Constantes.ETAT_EDITABLE)) {
                    if (!autoriser("tr_change_statut_line")) {
                        for (YvsComContenuDocStockReception r : y.getReceptions()) {
                            String re = controleStock(y.getArticle().getId(), y.getConditionnementEntree().getId(), y.getDocStock().getDestination().getId(), 0L, r.getQuantite(), r.getQuantite(), "INSERT", "S", r.getDateReception());
                            if (re != null) {
                                return new ResultatAction().missingStocks(r.getContenu().getArticle().getDesignation(), re);
                            }
                        }
                    }
                    y.setStatut(Constantes.ETAT_EDITABLE);
                    y.setAuthor(currentUser);
                    dao.update(y);
                    String delete = "DELETE FROM yvs_com_contenu_doc_stock_reception WHERE contenu = ?";
                    dao.requeteLibre(delete, new Options[]{new Options(y.getId(), 1)});
                    y.getReceptions().clear();
                    if (!allTransfert) {
                        actualiseStatutDoc(y.getDocStock());
                    }
                    return resultService.succes();
                }
            } else {
                if (statutDoc.equals(Constantes.ETAT_SOUMIS) || statutDoc.equals(Constantes.ETAT_ENCOURS) || statutDoc.equals(Constantes.ETAT_VALIDE)) {
                    YvsComContenuDocStockReception r = new YvsComContenuDocStockReception();
                    r.setAuthor(currentUser);
                    r.setContenu(y);
                    r.setDateReception(dateReception);
                    r.setQuantite(y.getQuantiteEntree() - y.getQuantiteRecu());
                    r.setCalculPr(y.getCalculPr());
                    r = (YvsComContenuDocStockReception) dao.save1(r);

                    y.setStatut(Constantes.ETAT_VALIDE);
                    y.setDateReception(dateReception);
                    y.setAuthor(currentUser);
                    dao.update(y);
                    y.getReceptions().add(r);
                    if (!allTransfert) {
                        actualiseStatutDoc(y.getDocStock());
                    }
                    return resultService.succes();
                } else {
                    return resultService.docIsNotSubmit();
                }
            }
        }
        return new ResultatAction().fail("Aucun documents n'a été trouvé !");
    }

    private void actualiseStatutDoc(YvsComDocStocks y) {
        try {
            if (y != null ? (y.getId() > 0 ? !y.getStatut().equals(Constantes.ETAT_EDITABLE) : false) : false) {
                List<YvsComContenuDocStock> list = dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{y});
                int element_valide = 0;
                int element_attente = 0;
                int element_encours = 0;
                for (YvsComContenuDocStock c : list) {
                    if (c.getStatut().equals(Constantes.ETAT_VALIDE)) {
                        element_valide++;
                    } else if (c.getStatut().equals(Constantes.ETAT_ENCOURS)) {
                        element_encours++;
                    } else {
                        element_attente++;
                    }
                }
                String statut = Constantes.ETAT_SOUMIS;
                if (element_encours > 0) {
                    statut = Constantes.ETAT_ENCOURS;
                } else if (element_valide > 0) {
                    if (element_attente > 0) {
                        statut = Constantes.ETAT_ENCOURS;
                    } else {
                        statut = Constantes.ETAT_VALIDE;
                    }
                }
                y.setStatut(statut);
                dao.update(y);
            }
        } catch (Exception ex) {
            getException("ManagedTransfertStock (actualiseStatutDoc)", ex);
        }
    }

}
