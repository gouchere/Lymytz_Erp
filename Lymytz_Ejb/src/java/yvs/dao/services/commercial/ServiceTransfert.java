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
import yvs.dao.local.UtilsBean;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.GenericService;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseCodeAcces;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComContenuDocStockReception;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.grh.personnel.YvsGrhEmployes;
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
//        String re = null;
//        for (YvsComContenuDocStock c : doc.getContenus()) {
//            re = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), doc.getSource().getId(), 0L, c.getQuantite(), 0, "INSERT", "S", doc.getDateDoc());
//            if (re != null) {
//                return result.missingStocks(c.getArticle().getDesignation(), re);
//            }
//        }
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

    public ResultatAction _controleFiche(YvsComDocStocks bean, boolean control_tranche_source, boolean control_tranche_destination) {
        if (bean == null) {
            return result.emptyDoc();
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
        if (!verifyOperation(bean.getSource(), Constantes.TRANSFERT, Constantes.TRANSFERT)) {
            return result.operationNotAllow(bean.getSource().getDesignation(), Constantes.TRANSFERT);
        }
        if (!verifyOperation(bean.getDestination(), Constantes.TRANSFERT, Constantes.TRANSFERT)) {
            return result.operationNotAllow(bean.getDestination().getDesignation(), Constantes.TRANSFERT);
        }
        if (!dao.autoriser("tr_valid_all", niveau)) {
            System.err.println(" Aucune autorisation " + niveau);
            //droit de super validation des transfert
            if (!chechAutorisationAction(bean, 2)) {
                return result.userNotAbility();
            }
        }
        if (bean.getCloturer()) {
            return result.docIsAlreadyClose();
        }
        if (bean.getNumDoc() == null || bean.getNumDoc().equals("")) {
            UtilsBean util = new UtilsBean(null, dao);
            String ref = util.genererReference(Constantes.TYPE_FT_NAME, bean.getDateDoc(), bean.getSource().getId(), Constantes.DEPOT, "", currentUser.getAgence());
            bean.setNumDoc(ref);
            if (ref == null || ref.trim().equals("")) {
                return result.numDocNotGenerated();
            }
        }
        if ((bean.getDestination() != null) ? bean.getDestination().getId() < 1 : true) {
            return result.emptyDepot(false);
        }
        if (control_tranche_destination) {
            if ((bean.getCreneauDestinataire() != null) ? bean.getCreneauDestinataire().getId() < 1 : true) {
                return result.emptyCreneauHoraire(false);
            }
        }
        if ((bean.getSource() != null) ? bean.getSource().getId() < 1 : true) {
            return result.emptyDepot(true);
        }
        if (control_tranche_source) {
            if ((bean.getCreneauSource() != null) ? bean.getCreneauSource().getId() < 1 : true) {
                return result.emptyCreneauHoraire(true);
            }
        }
        if (!autoriser("tr_edit_all_time")) {
            ResultatAction r = verifyDateStock(bean.getDateDoc());
            if (!r.isResult()) {
                return r;
            }
        }
        //Vérifier les inventaires dans les dépôts source et destination

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

    public boolean verifyOperation(YvsBaseDepots d, String type, String operation) {
        if (d != null ? d.getId() > 0 : false) {
            return checkOperationDepot(d.getId(), type, operation);
        }
        return true;
    }

    public boolean checkOperationDepot(long depot, String type, String operation) {
        Long nb = (Long) dao.loadObjectByNameQueries("YvsBaseDepotOperation.countByDepotTypeOperation", new String[]{"depot", "type", "operation"}, new Object[]{new YvsBaseDepots(depot), type, operation});
        return nb != null ? nb > 0 : false;
    }

    private Date hier(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    public boolean chechAutorisationAction(YvsComDocStocks doc, int action) {
        if (currentUser != null ? currentUser.getId() < 1 : true) {
            return false;
        }
        return chechAutorisationAction(doc, action, currentUser.getUsers());
    }

    public boolean chechAutorisationAction(YvsComDocStocks doc, int action, YvsUsers currentUser) {
        if (currentUser != null ? (currentUser != null) : false) {
            switch (action) {
                case 1: { //Cas de l'emetteur du transfert
                    if (doc != null ? doc.getSource() != null : false) {
                        boolean correct = false;
                        Long nb = (Long) dao.loadObjectByNameQueries("YvsComCreneauHoraireUsers.ifUserHasPlaning", new String[]{"hier", "date", "users", "crenoD"}, new Object[]{hier(doc.getDateDoc()), doc.getDateDoc(), currentUser, doc.getCreneauSource()});
                        correct = nb != null ? nb > 0 : false;
                        if (!correct) {
                            //contrôle le code d'accès de l'utilisateur
                            if (doc.getSource().getCodeAcces() != null ? doc.getSource().getCodeAcces().getCode() != null : false) {
                                nb = (Long) dao.loadObjectByNameQueries("YvsBaseUsersAcces.findIdAcces", new String[]{"users", "code", "societe"}, new Object[]{currentUser, doc.getSource().getCodeAcces().getCode(), currentScte});
                                if (nb != null ? nb > 0 : false) {
                                    break;
                                }
                            }
                            YvsGrhEmployes employe = doc.getSource().getResponsable();
                            if (employe != null ? employe.getId() < 1 : true) {
                                employe = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsBaseDepots.findResponsableById", new String[]{"id"}, new Object[]{doc.getSource().getId()});
                            }
                            if (currentUser.getEmploye() != null && employe != null) {
                                // Controle si le employé courant est le responsable du depot destinataire
                                if (!employe.equals(currentUser.getEmploye())) {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        }
                    }
                    break;
                }
                case 2: {   //cas du destinataire du transfert
                    if (doc != null ? doc.getDestination() != null : false) {
                        boolean correct = false;
                        Long nb = (Long) dao.loadObjectByNameQueries("YvsComCreneauHoraireUsers.ifUserHasPlaning", new String[]{"hier", "date", "users", "crenoD"}, new Object[]{hier(doc.getDateReception()), doc.getDateReception(), currentUser, doc.getCreneauDestinataire()});
                        correct = nb != null ? nb > 0 : false;
                        if (!correct) {
                            //contrôle le code d'accès de l'utilisateur
                            if (doc.getDestination().getCodeAcces() != null ? doc.getDestination().getCodeAcces().getCode() != null : false) {
                                nb = (Long) dao.loadObjectByNameQueries("YvsBaseUsersAcces.findIdAcces", new String[]{"users", "code", "societe"}, new Object[]{currentUser, doc.getDestination().getCodeAcces().getCode(), currentScte});
                                if (nb != null ? nb > 0 : false) {
                                    break;
                                }
                            }
                            YvsGrhEmployes employe = doc.getSource().getResponsable();
                            if (employe != null ? employe.getId() < 1 : true) {
                                employe = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsBaseDepots.findResponsableById", new String[]{"id"}, new Object[]{doc.getDestination().getId()});
                            }
                            if (currentUser.getEmploye() != null && employe != null) {
                                // Controle si le employé courant est le responsable du depot destinataire
                                if (!employe.equals(currentUser.getEmploye())) {
                                    return false;
                                }
                            } else {
                                return false;
                            }
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
        return true;
    }

    public ResultatAction changeStatut_(String etat, YvsComDocStocks doc) {
        if (!etat.equals("") && doc != null) {
            ResultatAction r = _controleFiche(doc, true, true);
            if (!r.isResult()) {
                return r;
            }
//            if (etat.equals(Constantes.ETAT_VALIDE)) {
//                for (YvsComContenuDocStock c : doc.getContenus()) {
//                    c.setStatut(Constantes.ETAT_VALIDE);
//                    c.setAuthor(currentUser);
//                    c.setDateUpdate(new Date());
//                    dao.update(c);
//                }
//            } else {
            for (YvsComContenuDocStock c : doc.getContenus()) {
//                    c.setStatut(Constantes.ETAT_EDITABLE);
//                    c.setAuthor(currentUser);
//                    c.setDateUpdate(new Date());
//                    dao.update(c);
                changeStatutLine(c, doc.getDateReception(), etat, true);
            }
            actualiseStatutDoc(doc);
//            }
            return new ResultatAction().succes();
        }
        return new ResultatAction().fail("Le statut du document est incorrect !");
    }

    public ResultatAction changeStatutLine(YvsComContenuDocStock y, Date dateReception, String statutDoc, boolean allTransfert) {
        ResultatAction result = new ResultatAction();
        if (y != null ? y.getId() > 0 : false) {
            if (!allTransfert) {
                result = _controleFiche(y.getDocStock(), true, (y.getDocStock().getStatut().equals(Constantes.ETAT_SOUMIS) || y.getStatut().equals(Constantes.ETAT_ENCOURS)));
                if (!result.isResult()) {
                    return result;
                }
//                boolean acces = autoriser("tr_valid_all");
//                if (!acces ? !chechAutorisationAction(y.getDocStock(), 2) : false) {
//                    return result.userNotAbility();
//                }
            }
            if (!allTransfert) {
                if (!dao.controleInventaire(y.getDocStock().getDestination().getId(), y.getDateReception(), y.getDocStock().getCreneauDestinataire().getTranche().getId())) {
                    return result.inventaireLock(y.getDocStock().getDestination().getDesignation());
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
                    return result.succes();
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
                    return result.succes();
                } else {
                    return result.docIsNotSubmit();
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
