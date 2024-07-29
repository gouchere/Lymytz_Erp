/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.vente;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.Options;
import yvs.dao.Util;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBaseModeleReference;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.base.YvsBaseTrancheReglement;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.YvsComParametreVente;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComFicheApprovisionnement;
import yvs.entity.commercial.ration.YvsComDocRation;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.stock.YvsComReservationStock;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.commercial.vente.YvsComMensualiteFactureVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaAcompteClient;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceCompensation;
import yvs.entity.compta.YvsComptaCaissePieceMission;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaMouvementCaisse;
import yvs.entity.compta.YvsComptaPhasePiece;
import yvs.entity.compta.YvsComptaPhasePieceAchat;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.compta.achat.YvsComptaAcompteFournisseur;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.saisie.YvsComptaContentAnalytique;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapePieceAchat;
import yvs.entity.compta.saisie.YvsComptaContentJournalFactureVente;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceAchat;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceVente;
import yvs.entity.compta.salaire.YvsComptaCaissePieceSalaire;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.grh.contrat.YvsGrhTypeElementAdditionel;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.grh.salaire.YvsGrhDetailPrelevementEmps;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.mutuelle.credit.YvsMutCredit;
import yvs.entity.mutuelle.operation.YvsMutOperationCompte;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsGrhPlanPrelevement;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.workflow.YvsWorkflowAutorisationValidDoc;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidFactureVente;
import yvs.entity.production.pilotage.YvsProdOrdreFabrication;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsBaseUsersAcces;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.service.AbstractEntity;
import yvs.service.IEntitySax;
import yvs.service.UtilRebuild;
import yvs.service.compta.doc.divers.AYvsComptaAcompteClient;
import yvs.service.param.workflow.IYvsWorkflowValidFactureVente;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComDocVentes extends AbstractEntity {

    public AYvsComDocVentes(DaoInterfaceWs dao) {
        this.dao = dao;
    }
    public static DateFormat ldf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
    public static DateFormat formatHeure = new SimpleDateFormat("HH:mm:ss");
    public static DateFormat time = new SimpleDateFormat("HH:mm");
    private YvsBaseModeReglement modeByEspece = null;
    private YvsComptaCaissePieceVente pieceReglement = new YvsComptaCaissePieceVente();
    private YvsComptaCaissePieceVente pieceAvance = new YvsComptaCaissePieceVente();
    private String tabIds, model, docIds, rowIds, groupBy = "C";
    YvsComptaCaissePieceVente reglement = new YvsComptaCaissePieceVente();
    private List<YvsComptaCaissePieceVente> reglements = new ArrayList<YvsComptaCaissePieceVente>();
    private YvsComptaCaissePieceVente selectCaisseVente = new YvsComptaCaissePieceVente();
    private List<YvsAgences> listAgence = new ArrayList<YvsAgences>();
    private List<YvsComptaPiecesComptable> listePiece, piecesSelect;
    private YvsComptaPhasePiece selectPhaseVente = new YvsComptaPhasePiece();
    private List<YvsComptaPhasePiece> phases = new ArrayList<>();
    private YvsComptaCaissePieceAchat selectCaisseAchat = new YvsComptaCaissePieceAchat();
    public List<YvsComptaCaissePieceAchat> piecesCaisses = new ArrayList<>();
    private List<YvsComptaAcompteClient> acomptes = new ArrayList<YvsComptaAcompteClient>();
    private YvsComptaCaissePieceVente pieceVente = new YvsComptaCaissePieceVente();
    private YvsComParametreVente currentParam = new YvsComParametreVente();
    private List<YvsComDocVentes> documents = new ArrayList<>();
    private List<YvsGrhPlanPrelevement> plansRetenues = new ArrayList<>();
    private List<YvsGrhTypeElementAdditionel> listTypesElts = new ArrayList<>();
    IEntitySax IEntitiSax = new IEntitySax();

    public AYvsComDocVentes() {
    }

    public void setAgence(YvsAgences agence) {
        this.currentAgence = agence;
    }

    public void setNiveauAcces(YvsNiveauAcces niveau) {
        this.niveau = niveau;
    }

    public ResultatAction<YvsWorkflowValidFactureVente> valideEtape(YvsWorkflowValidFactureVente entity) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                Long id = (Long) dao.loadOneByNameQueries("YvsWorkflowValidFactureVente.findByIDEtapeFacture", new String[]{"facture", "etape"}, new Object[]{entity.getFactureVente(), entity.getEtape()});
                entity.setId(id);
                entity.getFactureVente().getEtapesValidations().clear();
                ResultatAction<YvsWorkflowValidFactureVente> result = validEtapeFacture(entity.getFactureVente(), entity.getAuthor(), entity);

                entity.getFactureVente().getCommerciaux().clear();
                entity.getFactureVente().getContenus().clear();
                entity.getFactureVente().getCouts().clear();
//                entity.getFactureVente().getYvsComCommissionVenteList().clear();
                entity.getFactureVente().getReglements().clear();
                entity.getFactureVente().getEtapesValidations().clear();
                if (result.isResult()) {
                    return new ResultatAction(true, entity, entity.getId(), "Succes");
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Action Impossible");
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AYvsComDocVentes.class.getName()).log(Level.SEVERE, null, e);

        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComDocVentes> controleUpdate(YvsComDocVentes entity) {
        try {
            if (entity.getId() < 1) {
                return new ResultatAction<>(false, entity, 0L, "Le document n'existe pas");
            }
            return controle(entity);
        } catch (Exception e) {
            Logger.getLogger(AYvsComDocVentes.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComDocVentes> controle(YvsComDocVentes entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner un document");
            }
            YvsComDocVentes current = null;
            if (Util.asLong(entity.getId())) {
                current = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{entity.getId()});
            }
            if (!entity.isUpdate()) {
                if (!entity.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                    return new ResultatAction<>(false, entity, 0L, "Le document doit etre éditable pour pouvoir etre modifié");
                }
                if (!entity.isSynchroniser()) {
                    if (!autoriser("fv_save_doc", niveau)) {
                        return new ResultatAction<>(false, entity, 0L, "Vous ne disposez pas de privillège pour réaliser cette action");
                    }
                }
            } else {
                if (entity.getStatut().equals(Constantes.ETAT_VALIDE) && (current != null ? current.getStatut().equals(Constantes.ETAT_VALIDE) : false)) {
                    return new ResultatAction<YvsComDocVentes>(false, entity, entity.getId(), "Vous ne pouvez pas modifer cette facture ! Elle est déja validée", false);
                }
                if (entity.getStatut().equals(Constantes.ETAT_ANNULE) && (current != null ? current.getStatut().equals(Constantes.ETAT_ANNULE) : false)) {
                    return new ResultatAction<YvsComDocVentes>(false, entity, entity.getId(), "Vous ne pouvez pas modifer cette facture ! Elle a été annulée", false);
                }
                if (entity.getStatutLivre().equals(Constantes.ETAT_LIVRE) && (current != null ? current.getStatutLivre().equals(Constantes.ETAT_LIVRE) : false)) {
                    return new ResultatAction<YvsComDocVentes>(false, entity, entity.getId(), "Vous ne pouvez pas modifer cette facture ! Elle est déja livrée", false);
                }
//                if (entity.getStatut().equals(Constantes.ETAT_VALIDE) && entity.getContenus().isEmpty()) {
//                    return new ResultatAction<YvsComDocVentes>(false, entity, 0L, "Vous ne pouvez valider une facture sans contenue", false);
//                }
            }
            if (entity.getCloturer() && (current != null ? current.getCloturer() : false)) {
                return new ResultatAction<>(false, entity, 0L, "Ce document est vérouillé");
            }
            if (!entity.isSynchroniser() && Util.asLong(entity.getId())) {
                boolean comptabilise = dao.isComptabilise(entity.getId(), Constantes.SCR_VENTE);
                if (comptabilise) {
                    return new ResultatAction<>(false, entity, 0L, "Cette facture est déja comptabilisé");
                }
            }
            if ((entity.getEnteteDoc() != null) ? entity.getEnteteDoc().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous ne disposé pas d'une entête");

            }
            if ((entity.getClient() != null) ? entity.getClient().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier le client!");
            }
//            if (entity.getTiers() != null ? entity.getTiers().getId() < 1 : true) {
//                return new ResultatAction<>(false, entity, 0L, "Aucun code tiers n'a été trouvé pour cette facture");
//            }
            if (entity.getClient().getDefaut()) {
                if (entity.getEnteteDoc() != null ? entity.getEnteteDoc().getId() < 1 : true) {
                    if (entity.getEnteteDoc().getCreneau() != null
                            ? entity.getEnteteDoc().getCreneau().getCreneauPoint().getPoint().getAcceptClientNoName() : true) {
                        if (entity.getNomClient() != null ? (entity.getNomClient().trim().length() < 1 || entity.getNomClient().equals(entity.getClient().getNom())) : true) {
                            return new ResultatAction<>(false, entity, 0L, "Vous devez specifier le nom du client!");
                        }
                    }
                }
            }
//            if ((entity.getAdresse() != null) ? entity.getAdresse().getId() < 1 : true) {
//                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier l'adresse de la vente!");
//            }
            if ((entity.getCategorieComptable() != null) ? entity.getCategorieComptable().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier la catégorie comptable!");
            }
            if (!dao.controleEcartVente(entity.getEnteteDoc().getCreneau().getUsers().getId(), entity.getEnteteDoc().getDateEntete())) {
                return new ResultatAction<>(false, entity, 0L, "Vous ne pouvez créer une fiche de vente à cette date car un ou plusieurs manquants ont déjà été réalisés après pour ce vendeur");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComDocVentes.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");

    }

    //Entity est le bon de livraison
    public ResultatAction<YvsComDocVentes> livraison(YvsComDocVentes entity) {
        try {
            if (entity != null) {
                if (!entity.isSynchroniser()) {
                    if (!autoriser("blv_valide_doc")) {
                        return new ResultatAction<>(false, null, 0L, "Vous n'avez pas les privilèges requis pour cette action");
                    }
                }
                if (!entity.getTypeDoc().equals(Constantes.TYPE_BLV)) {
                    return new ResultatAction<>(false, null, 0L, "Ce type de document ne peut être livré");
                }
                if (entity.getContenus() != null ? entity.getContenus().isEmpty() : true) {
                    return new ResultatAction<>(false, null, 0L, "Ce de livraison est vide");
                }
                if (entity.getDocumentLie() != null ? entity.getDocumentLie().getId() < 1 : true) {
                    return new ResultatAction<>(false, null, 0L, "Ce bon de livraison n'est pas rattaché à une facture");
                }
                if (entity.getDepotLivrer() != null ? entity.getDepotLivrer().getId() < 1 : true) {
                    return new ResultatAction<>(false, null, 0L, "Ce bon de livraison n'est pas rattaché à un dépot de livraison");
                }
                if (entity.getTrancheLivrer() != null ? entity.getTrancheLivrer().getId() < 1 : true) {
                    return new ResultatAction<>(false, null, 0L, "Ce bon de livraison n'est pas rattaché à une tranche de livraison");
                }
                if (entity.getLivreur() != null ? entity.getLivreur().getId() < 1 : true) {
                    return new ResultatAction<>(false, null, 0L, "Ce bon de livraison n'est pas rattaché à un livreur");
                }
                if (entity.getDateLivraison() == null) {
                    return new ResultatAction<>(false, null, 0L, "Ce bon de livraison n'a pas de date de livraison");
                }
                YvsComDocVentes facture = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{entity.getDocumentLie().getId()});
                if (facture != null ? facture.getId() < 1 : true) {
                    return new ResultatAction<>(false, null, 0L, "Ce bon de livraison n'est pas rattaché à une facture");
                }
                if (facture.getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
                    entity = facture.getDocuments().get(0);
                    return new ResultatAction<>(true, entity, entity.getId(), "Succès");
                }
                YvsComEnteteDocVente header = facture.getEnteteDoc();
                if (header != null ? header.getId() < 1 : true) {
                    return new ResultatAction<>(false, null, 0L, "Ce bon de livraison est rattaché à une facture qui n'a pas de journal de vente");
                }
                if (!verifyOperation(entity.getDepotLivrer(), Constantes.SORTIE, Constantes.VENTE, false)) {
                    return new ResultatAction<>(false, null, 0L, "Ce dépot n'autorise pas les ventes");
                }
                //contrôle la cohérence avec les inventaires
                if (!verifyInventaire(entity.getDepotLivrer(), entity.getTrancheLivrer(), entity.getDateLivraison())) {
                    return new ResultatAction<>(false, null, 0L, "Il existe déja des inventaires anterieurs a cette date");
                }
                if (header != null ? (header.getCreneau() != null ? (header.getCreneau().getCreneauPoint() != null ? (header.getCreneau().getCreneauPoint().getPoint() != null) : false) : false) : false) {
                    switch (header.getCreneau().getCreneauPoint().getPoint().getLivraisonOn()) {
                        case 'R': {
                            if (!facture.getStatut().equals(Constantes.ETAT_REGLE)) {
                                return new ResultatAction<>(false, null, 0L, "La facture doit etre reglée avant de pouvoir générer une livraison");
                            }
                        }
                        case 'V': {
                            if (!facture.getStatut().equals(Constantes.ETAT_VALIDE)) {
                                return new ResultatAction<>(false, null, 0L, "La facture doit etre validée avant de pouvoir générer une livraison");
                            }
                        }
                    }
                }
                List<YvsComContenuDocVente> contenus = new ArrayList<>();
                for (YvsComContenuDocVente contenu : entity.getContenus()) {
                    if (contenu.getQuantite() > 0) {
                        String[] champ = new String[]{"article", "depot"};
                        Object[] val = new Object[]{contenu.getArticle(), entity.getDepotLivrer()};
                        YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
                        if (y != null ? y.getId() < 1 : true) {
                            return new ResultatAction<>(false, null, 0L, "Impossible d'effectuer cette action... Car le depot " + entity.getDepotLivrer().getDesignation() + " ne possède pas l'article " + contenu.getArticle().getDesignation());
                        }
                        String controle = controleStock(contenu.getArticle().getId(), contenu.getConditionnement().getId(), entity.getDepotLivrer().getId(), 0L, contenu.getQuantite(), 0, "INSERT", "S", entity.getDateLivraison(), (contenu.getLot() != null ? contenu.getLot().getId() : 0));
                        if (controle != null) {
                            return new ResultatAction<>(false, null, 0L, "La ligne d'article " + contenu.getArticle().getDesignation() + " engendrera une incohérence dans le stock à la date du " + controle);
                        }
                        //controle les quantités déjà livré
                        Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findByDocLierTypeStatutArticleS", new String[]{"docVente", "statut", "typeDoc", "article", "unite"}, new Object[]{facture, Constantes.ETAT_VALIDE, Constantes.TYPE_BLV, contenu.getArticle(), contenu.getConditionnement()});
                        qteLivre = (qteLivre != null) ? qteLivre : 0;
                        //trouve la quantité d'article facturé 
                        Double qteFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByArticle", new String[]{"docVente", "article", "unite"}, new Object[]{facture, contenu.getArticle(), contenu.getConditionnement()});
                        qteFacture = (qteFacture != null) ? qteFacture : 0;
                        //trouve la quantité d'article facturé 
                        Double qteBonusFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteBonusByFacture", new String[]{"docVente", "article", "unite"}, new Object[]{facture, contenu.getArticle(), contenu.getConditionnement()});
                        qteBonusFacture = (qteBonusFacture != null) ? qteBonusFacture : 0;
                        if (facture != null ? !facture.getStatutRegle().equals(Constantes.ETAT_REGLE) : true) {
                            //si la facture n'est pas encore réglé, on ne dois pas inclure la quantité bonus dans la quantité à livrer
                            if (contenu.getQuantite() > (qteFacture - qteLivre)) {
                                return new ResultatAction<>(false, null, 0L, "Vous ne pouvez livrer l'article " + contenu.getArticle().getRefArt() + " au delà de la quantité facturée !");
                            }
                        } else {
                            if (contenu.getQuantite() > ((qteFacture + qteBonusFacture) - qteLivre)) {
                                return new ResultatAction<>(false, null, 0L, "Vous ne pouvez livrer l'article " + contenu.getArticle().getRefArt() + " au delà de la quantité facturée !");
                            }
                        }
                        contenus.add(contenu);
                    }
                }
                if (contenus.isEmpty()) {
                    return new ResultatAction<>(false, null, 0L, "Ce bon de livraison est vide");
                }
                String reference = dao.genererReference(Constantes.TYPE_BLV_NAME, entity.getDateLivraison(), entity.getDepotLivrer().getId(), header.getAgence().getSociete(), header.getAgence());
                if (!Util.asString(reference)) {
                    return new ResultatAction<>(false, null, 0L, dao.getRESULT());
                }
                facture = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{facture.getId()});
                entity.setEnteteDoc(facture.getEnteteDoc());
                entity.setId(null);
                entity.setNumDoc(reference);
                entity.setDateSave(new Date());
                entity.setDateUpdate(new Date());
                entity.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_LIVRER));
                entity.setStatut(String.valueOf(Constantes.STATUT_DOC_VALIDE));
                entity.getContenus().clear();
                entity.setInformation(null);
                entity = (YvsComDocVentes) dao.save1(entity);

                for (YvsComContenuDocVente contenu : contenus) {
                    contenu.setId(null);
                    contenu.setDateSave(new Date());
                    contenu.setDateUpdate(new Date());
                    contenu.setDocVente(new YvsComDocVentes(entity.getId()));
                    contenu.setStatut(String.valueOf(Constantes.STATUT_DOC_VALIDE));
                    contenu.setStatutLivree(Constantes.STATUT_DOC_LIVRER);
                    contenu = (YvsComContenuDocVente) dao.save1(contenu);
                    entity.getContenus().add(contenu);
                }

                dao.getEquilibreVente(facture.getId());
                facture = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{facture.getId()});
                entity.setDocumentLie(new YvsComDocVentes(facture.getId(), facture.getNumDoc(), facture.getStatut(), facture.getStatutLivre(), facture.getStatutRegle()));
                return new ResultatAction<>(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, null, 0L, "Action Impossible");
    }

    public YvsComDocVentes rebuildDocVente(YvsComDocVentes d) {
        return UtilRebuild.reBuildDocVente(d);
    }

    public ResultatAction<YvsComDocVentes> livraison(List<YvsComDocVentes> lv, YvsComDocVentes selectDoc, YvsUsersAgence currentUser, YvsSocietes currentScte, YvsAgences currentAgence, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint) {
        if (lv.isEmpty()) {
            ResultatAction<YvsComDocVentes> result = transmisOrder(selectDoc, selectDoc.getDateLivraisonPrevu(), Constantes.ETAT_VALIDE, true, currentUser, currentScte);
            if (result.isResult()) {
                selectDoc.setStatutLivre(Constantes.ETAT_LIVRE);
                selectDoc.setConsigner(false);
                selectDoc.setDateConsigner(null);
                result.setEntity(selectDoc);
            }
            return result;
        }
        boolean livrer = false;
        for (YvsComDocVentes dLiv : lv) {
            if (!dLiv.getStatut().equals(Constantes.ETAT_ANNULE) && !dLiv.getStatut().equals(Constantes.ETAT_VALIDE)) {
                ResultatAction<YvsComDocVentes> reullt = validerLivraison(selectDoc, dLiv, currentUser);
                livrer = reullt.getMessage().equals("Succès");
            }
        }
        if (livrer) {
            selectDoc.setStatutLivre(Constantes.ETAT_LIVRE);
            selectDoc.setConsigner(false);
            selectDoc.setDateConsigner(null);
            return new ResultatAction(true, selectDoc, selectDoc.getId(), "Succès");
        }
        return new ResultatAction<>(false, null, 0L, "Action Impossible");
    }

    public void reglement(YvsComDocVentes selectDoc, YvsUsersAgence currentUser, YvsSocietes currentScte, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint, YvsAgences currenAgence) {
        List<YvsBaseCaisse> list = dao.loadNameQueries("YvsBaseCaisse.findByUsers", new String[]{"caissier"}, new Object[]{selectDoc.getEnteteDoc().getCreneau().getUsers()});
        YvsBaseCaisse caisse = new YvsBaseCaisse();
        if (list != null ? !list.isEmpty() : false) {
            caisse = list.get(0);
        }

        if (caisse != null ? caisse.getId() < 1 : true) {
            caisse = selectDoc.getEnteteDoc().getCreneau().getUsers().getCaisse();
            reglement.setCaisse(caisse);
        }
        if (caisse != null ? caisse.getId() < 1 : false) {
            List<YvsBaseCaisse> caisses = loadCaisses(selectDoc.getEnteteDoc().getCreneau().getUsers(), currentUser, currentScte);
            if (caisses != null ? !caisses.isEmpty() : false) {
                caisse = caisses.get(0);
                reglement.setCaisse(caisse);
            }
        }

        if (caisse != null ? caisse.getId() > 0 : false) {
            List<YvsComptaCaissePieceVente> pieces = new ArrayList<>();
            double montant = selectDoc.getMontantResteAPlanifier();
            if (montant > 0) {
                if (selectDoc.getModelReglement() != null) {
                    pieces = generetedPiecesFromModel(new YvsBaseModelReglement(selectDoc.getModelReglement().getId(), selectDoc.getModelReglement().getNameType()), selectDoc, caisse, currentScte, currentUser, currenAgence, currentDepot, currentPoint);
                } else {
                    Calendar cal = Calendar.getInstance();
                    YvsBaseModeReglement espece = modeEspece(currentScte), mode = null;
                    if (mode != null ? mode.getId() != null ? mode.getId() < 1 : true : true) {
                        mode = espece;
                    }
                    YvsComptaCaissePieceVente p = buildPieceFromModel(0L, mode, selectDoc, caisse, cal.getTime(), montant, ' ', currentUser, currentScte, currenAgence, currentDepot, currentPoint);
                    pieces.add(p);
                }
                for (YvsComptaCaissePieceVente y : pieces) {
                    if (!y.getStatutPiece().equals(Constantes.ETAT_REGLE)) {
                        y.setVente(new YvsComDocVentes(selectDoc.getId()));
                        if (y.getId() < 1 && ((y.getCaisse() != null) ? y.getCaisse().getId() > 0 : false) && ((y.getModel() != null) ? y.getModel().getId() > 0 : false) && y.getMontant() > 0) {
                            y.setId(null);
                            y = (YvsComptaCaissePieceVente) dao.save1(y);
                            selectDoc.getReglements().add(y);
                            if (!selectDoc.getReglements().contains(y)) {
                                selectDoc.getReglements().add(y);
                            }
                        }
                    }
                }
            }
        }
    }

    public ResultatAction<YvsComDocVentes> validerLivraison(YvsComDocVentes facture, YvsComDocVentes entity, YvsUsersAgence currentUser) {
        if (entity == null) {
            return new ResultatAction<>(false, null, 0L, "Action Impossible");
        }
        if (!verifyOperation(entity.getDepotLivrer(), Constantes.SORTIE, Constantes.VENTE, false)) {
            return new ResultatAction<>(false, null, 0L, "Cen depot n'autorise pas de sortie");
        }
        //contrôle la cohérence avec les inventaires
        if (!verifyInventaire(entity.getDepotLivrer(), entity.getTrancheLivrer(), entity.getDateLivraison())) {
            return new ResultatAction<>(false, null, 0L, "Un inventaire est deja passé");
        }
        if (livrer(entity, true)) {
            entity.setCloturer(false);
            entity.setAnnulerBy(null);
            entity.setValiderBy(currentUser.getUsers());
            entity.setDateAnnuler(null);
            entity.setDateCloturer(null);
            entity.setDateValider(entity.getDateLivraison());
            entity.setStatut(Constantes.ETAT_VALIDE);
            entity.setDateUpdate(new Date());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                entity.setAuthor(currentUser);
            }
            entity.setStatut(Constantes.ETAT_VALIDE);
            YvsComDocVentes y = new YvsComDocVentes(entity);
            y.getContenus().clear();
            dao.update(y);
            dao.getEquilibreVente(facture.getId());
            return new ResultatAction<>(true, entity, entity.getId(), "Succès");
        }
        return new ResultatAction<>(false, null, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComDocVentes> validerFacture(YvsComDocVentes selectDoc, YvsUsersAgence currentUser, YvsSocietes currentScte, YvsAgences currentAgence, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint) {
        ResultatAction<YvsComDocVentes> resul_control = controleValidation(selectDoc);
        if (selectDoc.getTypeDoc().equals(Constantes.TYPE_FV)) {
            if (!resul_control.isResult()) {
                return new ResultatAction<>(false, null, 0L, "Action Impossible");
            }
            System.err.println("Générère l'echéancier prévu de règlement");
            // Générère l'echéancier prévu de règlement
            generatedEcheancierReg(selectDoc, false, currentUser, currentScte);
//        if (changeStatut(Constantes.ETAT_VALIDE, selectDoc)) {
            if (!selectDoc.getCloturer()) {
                selectDoc.setCloturer(false);
                selectDoc.setAnnulerBy(null);
                selectDoc.setValiderBy(currentUser.getUsers());
                selectDoc.setCloturerBy(null);
                selectDoc.setDateAnnuler(null);
                selectDoc.setDateCloturer(null);
                selectDoc.setDateValider(new Date());
                selectDoc.setStatut(Constantes.ETAT_VALIDE);
                selectDoc.setDateUpdate(new Date());
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    selectDoc.setAuthor(currentUser);
                }
                dao.update(selectDoc);
                //valider aussi les BL liés
                List<YvsComDocVentes> lv = dao.loadNameQueries("YvsComDocVentes.findByParentTypeDoc", new String[]{"documentLie", "typeDoc"}, new Object[]{selectDoc, Constantes.TYPE_BLV});
                if (selectDoc.getLivraisonAuto()) {
                    if (lv != null ? lv.isEmpty() : true) {
                        resul_control = transmisOrder(selectDoc, selectDoc.getDateLivraisonPrevu(), Constantes.ETAT_VALIDE, true, currentUser, currentScte);

                        selectDoc.setStatutLivre(Constantes.ETAT_LIVRE);
                        selectDoc.setConsigner(false);
                        selectDoc.setDateConsigner(null);
                    }
                    for (YvsComDocVentes dLiv : lv) {
                        if (!dLiv.getStatut().equals(Constantes.ETAT_ANNULE) && !dLiv.getStatut().equals(Constantes.ETAT_VALIDE)) {
                            dLiv.setLivreur(selectDoc.getEnteteDoc().getCreneau().getUsers());
                            dLiv.setDocumentLie(selectDoc);
                            dLiv.setDocumentLie(selectDoc);
                            validerLivraison(selectDoc, dLiv, currentUser);
                        }
                    }
                }
                if (selectDoc.getEnteteDoc().getCreneau().getCreneauPoint().getPoint().getReglementAuto()) {
                    YvsBaseCaisse caisse = reglement.getCaisse();
                    if (caisse != null ? caisse.getId() < 1 : true) {
                        caisse = selectDoc.getEnteteDoc().getCreneau().getUsers().getCaisse();
                        reglement.setCaisse(caisse);
                    }
                    if (caisse != null ? caisse.getId() < 1 : false) {
                        List<YvsBaseCaisse> caisses = loadCaisses(selectDoc.getEnteteDoc().getCreneau().getUsers(), currentUser, currentScte);
                        if (caisses != null ? !caisses.isEmpty() : false) {
                            caisse = caisses.get(0);
                            reglement.setCaisse(caisse);
                        }
                    }
                    if (caisse != null ? caisse.getId() > 0 : false) {
                        List<YvsComptaCaissePieceVente> pieces = generetedPiecesFromModel(new YvsBaseModelReglement(selectDoc.getModelReglement().getId(), selectDoc.getModelReglement().getNameType()), selectDoc, caisse, currentScte, currentUser, currentAgence, currentDepot, currentPoint);
                        for (YvsComptaCaissePieceVente y : pieces) {
                            if (!y.getStatutPiece().equals(Constantes.ETAT_REGLE)) {
                                y.setVente(selectDoc);
                                if (y.getId() < 1 && ((y.getCaisse() != null) ? y.getCaisse().getId() > 0 : false) && ((y.getModel() != null) ? y.getModel().getId() > 0 : false) && y.getMontant() > 0) {
                                    y.setId(null);
                                    y = (YvsComptaCaissePieceVente) dao.save1(y);
                                    selectDoc.getReglements().add(y);
                                    if (!selectDoc.getReglements().contains(y)) {
                                        selectDoc.getReglements().add(y);
                                    }
                                }
                            }
                        }
                    }
                }
                if (!selectDoc.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
//                if (docVente.getReglements() != null ? docVente.getReglements().isEmpty() : true) { //Si le reste à payer est positif   

                    if (selectDoc.getReglements() != null ? !selectDoc.getReglements().isEmpty() : false) {
//                    ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);

                        for (YvsComptaCaissePieceVente y : selectDoc.getReglements()) {
                            if (!y.getStatutPiece().equals(Constantes.ETAT_REGLE)) {
                                y.setVente(selectDoc);
                                if (y.getId() < 1 && ((y.getCaisse() != null) ? y.getCaisse().getId() > 0 : false) && ((y.getModel() != null) ? y.getModel().getId() > 0 : false) && y.getMontant() > 0) {
                                    y.setId(null);
                                    y = (YvsComptaCaissePieceVente) dao.save1(y);
                                    if (!selectDoc.getReglements().contains(y)) {
                                        selectDoc.getReglements().add(y);
                                    }
                                }
                                if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE)) {
//                                    w.openConfirmPaiement(y, "F", false, false, false);
                                    reglerPieceTresorerie(selectDoc, y, "", true, currentUser, currentScte);
                                } else if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_SALAIRE)) {
                                    YvsGrhEmployes emp = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByCompteTiers", new String[]{"tiers", "societe"}, new Object[]{y.getVente().getClient().getTiers(), currentScte});
                                    if (emp != null) {

                                        loadAllTypeElementAddActif(currentUser);
                                        YvsGrhElementAdditionel ela = new YvsGrhElementAdditionel();

                                        if ((listTypesElts != null ? listTypesElts.size() == 1 : false) && (plansRetenues != null ? plansRetenues.size() == 1 : false)) {
                                            ela.setContrat(emp.getContrat());
                                            //charge les retenue déjà rattaché à cette facture
                                            ela.setListPrelevement(dao.loadNameQueries("YvsGrhDetailPrelevementEmps.findByDocVente", new String[]{"docVente"}, new Object[]{y.getVente()}));
                                            ela.setDateDebut(selectDoc.getEnteteDoc().getDateEntete());
                                            ela.setDateElement(selectDoc.getEnteteDoc().getDateEntete());
                                            ela.setTypeElement(new YvsGrhTypeElementAdditionel(listTypesElts.get(0).getId()));
                                            ela.setPlanPrelevement(new YvsGrhPlanPrelevement(plansRetenues.get(0).getId()));
//                                            placerRetenu(y.getMontant());
                                            valideReglementFacture(y, false, currentUser);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (currentParam == null) {
                currentParam = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if (currentParam != null) {
                if (currentParam.getComptabilisationAuto()) {
                    comptabiliserVente(selectDoc, false, false, currentAgence, currentUser, currentScte);
                }
            }
            selectDoc.setStatut(Constantes.ETAT_VALIDE);
            dao.update(selectDoc);
        } else if (selectDoc.getTypeDoc().equals(Constantes.TYPE_BCV)) {
            validerOrder(selectDoc, currentUser, selectDoc.getContenus());
        }
        return new ResultatAction(true, selectDoc, selectDoc.getId(), "Succes");

    }

    public void afterChangeStatut(String etat, YvsComDocVentes bean, YvsComDocVentes entity) {
        dao.getEquilibreVente(entity.getId());
        YvsComDocVentes y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{entity.getId()});
        if (y != null ? y.getId() > 0 : false) {
            entity.setStatut(y.getStatut());
            entity.setStatutLivre(y.getStatutLivre());
            entity.setStatutRegle(y.getStatutRegle());

            bean.setStatut(y.getStatut());
            bean.setStatutLivre(y.getStatutLivre());
            bean.setStatutRegle(y.getStatutRegle());
        }
        //Si la méthode est appelé avec un document autre que la facture
        if (entity.getDocumentLie() != null ? entity.getDocumentLie().getId() > 0 : false) {
            dao.getEquilibreVente(entity.getDocumentLie().getId());
            y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{entity.getDocumentLie().getId()});
            entity.getDocumentLie().setStatut(y.getStatut());
            entity.getDocumentLie().setStatutLivre(y.getStatutLivre());
            entity.getDocumentLie().setStatutRegle(y.getStatutRegle());
        }
        if (documents.contains(entity)) {
            documents.set(documents.indexOf(entity), entity);
        }

    }

    public void loadAllTypeElementAddActif(YvsUsersAgence currentUser) {
        String[] champ = new String[]{"societe"};
        Object[] val = new Object[]{currentUser.getAgence().getSociete()};
        plansRetenues = dao.loadNameQueries("YvsGrhPlanPrelevement.findAllCom", champ, val);
        champ = new String[]{"societe", "retenue"};
        val = new Object[]{currentUser.getAgence().getSociete(), true};
        listTypesElts = dao.loadNameQueries("YvsGrhTypeElementAdditionel.findAllCom", champ, val);
    }

    public boolean comptabiliserVente(YvsComDocVentes y, YvsAgences currentAgence, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        return comptabiliserVente(y, true, true, currentAgence, currentUser, currentScte);
    }

    public boolean comptabiliserVente(YvsComDocVentes y, boolean msg, boolean succes, YvsAgences currentAgence, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        if (y != null) {
            return comptabiliserVente(y, buildVenteToComptabilise(y.getId(), msg, currentUser, currentScte), msg, succes, currentAgence, currentUser, currentScte);
        }
        return false;
    }

    private List<YvsComptaContentJournal> buildVenteToComptabilise(long id, boolean msg, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        List<YvsComptaContentJournal> list = new ArrayList<>();
        YvsComDocVentes y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{id});
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            // Verification si les articles de la facture sont parametré avec la categorie comptable de la facture
            String req = "select distinct(co.article) from yvs_com_contenu_doc_vente co inner join yvs_com_doc_ventes dv on co.doc_vente = dv.id "
                    + "WHERE dv.id = ? and co.article not in (select distinct(ca.article) "
                    + "from yvs_base_article_categorie_comptable ca where dv.categorie_comptable = ca.categorie)";
            List<Object> ids = dao.loadListBySqlQuery(req, new Options[]{new Options(id, 1)});
            if (ids != null ? !ids.isEmpty() : false) {
                Object o = ids.get(0);
                if (o != null) {
                    YvsBaseArticles a = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{(Long) o});
                    if (a != null ? (a.getId() != null ? a.getId() > 0 : false) : false) {
                        if (msg) {

                        }
                    }
                }
                return null;
            }
            list = buildContentJournal(id, y.getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_AVOIR_VENTE : Constantes.SCR_VENTE, msg, currentUser);
        } else {

            return null;
        }
        return list;
    }

    private boolean comptabiliserVente(YvsComDocVentes y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes, YvsAgences currentAgence, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatut().equals(Constantes.ETAT_VALIDE)) {
                    boolean comptabilise = isComptabilise(y.getId(), y.getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_AVOIR_VENTE : Constantes.SCR_VENTE);
                    if (comptabilise) {
                        if (msg) {

                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaVente(y.getId(), contenus, msg, currentAgence, currentUser, currentScte);
                    int idx = listePiece.indexOf(p);
                    if (idx > -1) {
                        listePiece.set(idx, p);
                    } else {
                        listePiece.add(0, p);
                    }
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);

                    }
                    return reponse;
                } else {
                    if (msg) {

                    }
                }
            }
        }
        return false;
    }

    private YvsComptaPiecesComptable majComptaVente(long id, List<YvsComptaContentJournal> contenus, boolean msg, YvsAgences currentAgence, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {

                }
                return null;
            }
            YvsComDocVentes y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                YvsComptaJournaux jrn = giveJournalVente(currentAgence);
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getEnteteDoc().getDateEntete(), jrn, contenus, msg, currentScte, currentUser);
                    if (p != null ? p.getId() < 1 : true) {
                        return p;
                    }
                    YvsComptaContentJournalFactureVente c = new YvsComptaContentJournalFactureVente(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);

                    List<YvsComptaCaissePieceVente> pieces = dao.loadNameQueries("YvsComptaCaissePieceVente.findByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                    for (YvsComptaCaissePieceVente r : pieces) {
                        comptabiliserCaisseVente(r);
                    }

                    //Debut du lettrage
                    lettrerVente(y, pieces, currentUser, currentScte);
                    y.setComptabilise(true);
                    return p;
                } else {
                    if (msg) {

                    }
                }
            }
        }
        return null;
    }

    public List<YvsComptaContentJournal> lettrerVente(YvsComDocVentes y, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        List<YvsComptaCaissePieceVente> pieces = dao.loadNameQueries("YvsComptaCaissePieceVente.findByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
        return lettrerVente(y, pieces, currentUser, currentScte);
    }

    public List<YvsComptaContentJournal> lettrerVente(YvsComDocVentes y, List<YvsComptaCaissePieceVente> pieces, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        if (pieces != null ? !pieces.isEmpty() : false) {
            String isLettrer = null;
            List<YvsComptaContentJournal> credits, debits = null;
            YvsComptaAcompteClient acompte = (YvsComptaAcompteClient) dao.loadOneByNameQueries("YvsComptaNotifReglementVente.findAcompteByFactureNature", new String[]{"facture", "nature"}, new Object[]{y, 'D'});
            if ((acompte != null ? acompte.getId() < 1 : true)) {
                List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceVente.findIdByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_VENTE});
                if (credits != null ? !credits.isEmpty() : false) {
                    if (asString(credits.get(0).getLettrage())) {
                        isLettrer = credits.get(0).getLettrage();
                    } else {
                        debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExterne", new String[]{"id", "table"}, new Object[]{y.getId(), y.getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_AVOIR_VENTE : Constantes.SCR_VENTE});
                    }
                }
            } else {
                credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{acompte.getId(), Constantes.SCR_ACOMPTE_VENTE});
                if (credits != null ? !credits.isEmpty() : false) {
                    if (asString(credits.get(0).getLettrage())) {
                        isLettrer = credits.get(0).getLettrage();
                    } else {
                        List<Long> ids = dao.loadNameQueries("YvsComptaNotifReglementVente.findIdFactureByAcompte", new String[]{"acompte"}, new Object[]{acompte});
                        debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_VENTE});
                    }
                }
            }
            if (debits != null ? !debits.isEmpty() : false) {
                if (asString(debits.get(0).getLettrage())) {
                    isLettrer = debits.get(0).getLettrage();
                } else {
                    List<YvsComptaContentJournal> list = new ArrayList<>();
                    list.addAll(debits);
                    list.addAll(credits);
                    if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                        lettrageCompte(list, currentUser, currentScte);
                        result.addAll(list);
                    }
                }
            }
            if (asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentScte, isLettrer});
            }
        }
        return result;
    }

    private boolean comptabiliserVente(List<YvsComptaContentJournal> contenus, boolean msg, YvsAgences currentAgence, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;
        if (ids != null ? !ids.isEmpty() : false) {
//            ManagedFactureVente w = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
            boolean error = false;
            List<YvsComptaContentJournal> list;
            for (int idx : ids) {
                YvsComDocVentes y = getDocuments().get(idx);
                list = new ArrayList<>();
                for (YvsComptaContentJournal c : contenus) {
                    if (c.getRefExterne().equals(y.getId())) {
                        list.add(c);
                    }
                }
                if (!list.isEmpty()) {
                    if (comptabiliserVente(list, false, currentAgence, currentUser, currentScte)) {

                        succes = true;
                    } else {
                        error = true;
                    }
                }
            }
            docIds = "";

        } else {
            succes = comptabiliserVente(getDocuments().get(0), contenus, msg, false, currentAgence, currentUser, currentScte);
        }
        return succes;
    }

    public void valideReglementFacture(YvsUsersAgence currentUser) {
        valideReglementFacture(pieceVente, true, currentUser);
    }

    public void valideReglementFacture(YvsComptaCaissePieceVente pieceVente, boolean msg, YvsUsersAgence currentUser) {
        try {

            //enretistre la retenu
            YvsGrhElementAdditionel ela = new YvsGrhElementAdditionel();
            if (ela != null) {
                ela.setId(null);
                ela.setPiceReglement(pieceVente);
                ela.setPlanifier(true);
                ela.setContrat(ela.getContrat());
                ela.setPlanPrelevement(new YvsGrhPlanPrelevement(ela.getPlanPrelevement().getId()));
                ela.setTypeElement(new YvsGrhTypeElementAdditionel(ela.getTypeElement().getId()));
                ela.setAuthor(currentUser);
                ela.setPermanent(false);
                ela.setDateUpdate(new Date());
                ela = (YvsGrhElementAdditionel) dao.save1(ela);
                for (YvsGrhDetailPrelevementEmps d : ela.getListPrelevement()) {
                    d.setAuthor(currentUser);
                    d.setId(null);
                    d.setRetenue(ela);
                    d.setRetenuFixe(false);
                    d = (YvsGrhDetailPrelevementEmps) dao.save1(d);
                }
                //marque la pièce réglé
                pieceVente.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                if (pieceVente.getCaissier() != null ? pieceVente.getCaissier().getId() < 1 : true) {
                    pieceVente.setCaissier(currentUser.getUsers());
                }
                pieceVente.setDatePaiement(new Date());
                pieceVente.setCaisse(null);
                dao.update(pieceVente);
                if (pieceVente.getVente() != null) {
                    dao.getEquilibreVente(pieceVente.getVente().getId());

                }

            } else {

            }

        } catch (Exception ex) {

        }
    }

    public boolean generatedEcheancierReg(YvsComDocVentes y, boolean addList, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        if (y != null) {
            List<YvsComMensualiteFactureVente> list = generatedEcheancierReg(y, currentUser, currentScte);
            if (list != null ? list.isEmpty() : true) {
                return false;
            }
            for (YvsComMensualiteFactureVente e : list) {
                e.setId(null);
                e = (YvsComMensualiteFactureVente) dao.save1(e);
            }
            if (addList) {
                y.setMensualites(list);
            }

        }
        return true;
    }

    public List<YvsComMensualiteFactureVente> generatedEcheancierReg(YvsComDocVentes y, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        double ttc = dao.loadCaVente(y.getId());
        if (ttc > 0) {
            double total = (ttc - y.getMontantMensualite());
            return dao.generatedEcheancierReg(y, y.getModelReglement(), total, currentUser, currentScte);
        } else {

            return new ArrayList<>();
        }
    }

    public boolean changeStatut(String etat, YvsComDocVentes entity) {
        if (changeStatut_(etat, entity)) {
            return true;
        }
        return false;
    }

    public boolean changeStatut_(String etat, YvsComDocVentes entity) {
        if (!etat.equals("")) {
            if (entity.getCloturer()) {
                return false;
            }
            String rq = "UPDATE yvs_com_doc_ventes SET statut = '" + etat + "'  WHERE id=?";
            Options[] param = new Options[]{new Options(entity.getId(), 1)};
            dao.requeteLibre(rq, param);
            if (entity != null) {
                entity.setStatut(etat);
            }
            entity.setStatut(etat);
            return true;
        }
        return false;
    }

    public ResultatAction<YvsComDocVentes> transmisOrder(YvsComDocVentes facture, Date dateLivraison, String statut, boolean message, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        if (facture == null) {
            return new ResultatAction(false, 0, "Vous devez selectionner la facture");
        }
        String query = "SELECT action FROM yvs_com_doc_ventes WHERE id = ?";
        String action = (String) dao.loadObjectBySqlQuery(query, new Options[]{new Options(facture.getId(), 1)});
        if (Util.asString(action) ? action.equals("L") : false) {
            return new ResultatAction(false, 0, "L'operation de livraison est déja en cours d'execution!!!");
        }
        query = "UPDATE yvs_com_doc_ventes SET action = 'L' WHERE id = ?";
        dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
        try {
            statut = statut != null ? statut.trim().length() > 0 ? statut : Constantes.ETAT_VALIDE : Constantes.ETAT_VALIDE;
            if (facture.getEnteteDoc() != null ? (facture.getEnteteDoc().getCreneau() != null ? (facture.getEnteteDoc().getCreneau().getCreneauPoint() != null ? (facture.getEnteteDoc().getCreneau().getCreneauPoint().getPoint() != null) : false) : false) : false) {
                switch (facture.getEnteteDoc().getCreneau().getCreneauPoint().getPoint().getLivraisonOn()) {
                    case 'R': {
                        if (!facture.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                            query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                            dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                            return new ResultatAction(false, 0, "Cette facture doit etre reglée avant de pouvoir générer une livraison");
                        }
                    }
                    case 'V': {
                        if (!facture.getStatut().equals(Constantes.ETAT_VALIDE)) {
                            query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                            dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                            return new ResultatAction(false, 0, "Cette facture doit etre validée avant de pouvoir générer une livraison");
                        }
                    }
                }
            }
            if (dateLivraison != null ? dateLivraison.after(new Date()) : true) {
                query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                return new ResultatAction(false, 0, "La date de livraison est incorrecte !");
            }
            if (facture.getDepotLivrer() != null ? facture.getDepotLivrer().getId() < 1 : true) {
                query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                return new ResultatAction(false, 0, "Aucun dépôt de livraison n'a été trouvé !");
            }

            if (facture.getTrancheLivrer() != null ? facture.getTrancheLivrer().getId() < 1 : true) {
                List<YvsGrhTrancheHoraire> list = loadTranche(facture.getDepotLivrer(), dateLivraison);

                if (list != null ? list.size() == 1 : false) {
                    facture.setTrancheLivrer(list.get(0));
                } else {
                    query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                    dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                    return new ResultatAction(false, 0, "Aucune tranche de livraison n'a été trouvé !");
                }
            } else if (asString(facture.getTrancheLivrer().getTypeJournee())) {
                facture.setTrancheLivrer((YvsGrhTrancheHoraire) dao.loadOneByNameQueries("YvsGrhTrancheHoraire.findById", new String[]{"id"}, new Object[]{facture.getTrancheLivrer().getId()}));
            }
            if (!verifyOperation(new YvsBaseDepots(facture.getDepotLivrer().getId(), facture.getDepotLivrer().getDesignation()), Constantes.SORTIE, Constantes.VENTE, false)) {
                query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                return new ResultatAction(false, 0, "Le dépôt n'est pas paramétré pour les opérations de vente !");
            }

            if (!verifyInventaire(facture.getDepotLivrer(), facture.getTrancheLivrer(), dateLivraison)) {
                query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                return null;
            }
            YvsBaseDepots depots = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{facture.getDepotLivrer().getId()});
            String num = dao.genererReference(Constantes.TYPE_BLV_NAME, dateLivraison, facture.getDepotLivrer().getId(), depots.getAgence().getSociete(), depots.getAgence());
            if (num != null ? num.trim().length() > 0 : false) {
                List<YvsComContenuDocVente> contenus;
                if (facture.isReload()) {
                    contenus = loadContenusStay(facture, Constantes.TYPE_BLV);
                } else {
                    contenus = loadContenusStay(facture, facture.getContenus(), Constantes.TYPE_BLV);
                }
                facture.getContenus().clear();
                if (contenus != null ? !contenus.isEmpty() : false) {
                    List<YvsBaseDepots> depotsLivraison = new ArrayList<>();
                    List<YvsComContenuDocVente> list = new ArrayList<>();
                    YvsBaseDepots depot;
                    for (YvsComContenuDocVente c : contenus) {
                        depot = facture.getDepotLivrer();
                        if (c.getDepoLivraisonPrevu() != null ? (c.getDepoLivraisonPrevu().getId() != null ? c.getDepoLivraisonPrevu().getId() > 0 : false) : false) {
                            depot = c.getDepoLivraisonPrevu();
                        }
                        if (!depotsLivraison.contains(depot)) {
                            boolean verif_tranche = !verifyTranche(facture.getTrancheLivrer(), depot, dateLivraison);
                            if (verif_tranche) {
                                query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                                dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                                return new ResultatAction(false, 0, "Aucune tranche de livraison trouvée!");
                            }
                            depotsLivraison.add(depot);
                        }
                        if (!controlContentForTransmis(c, depot, dateLivraison, statut, message)) {
                            query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                            dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                            return new ResultatAction(false, 0, "Cette livraison provoquera une incoherence de stock !");
                        }
                        list.add(c);
                        if (c.getQuantiteBonus() > 0) {
                            YvsComContenuDocVente a = buildContentByBonus(c);
                            if (!controlContentForTransmis(a, depot, dateLivraison, statut, message)) {
                                query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                                dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                                return new ResultatAction(false, 0, "Cette livraison provoquera une incoherence de stock !");
                            }
                            list.add(a);
                        }
                    }

                    if (facture.getEnteteDoc() != null) {
                        YvsComDocVentes y = null;
                        boolean continu = true;
                        for (YvsBaseDepots d : depotsLivraison) {
                            num = dao.genererReference(Constantes.TYPE_BLV_NAME, dateLivraison, d.getId(), depots.getAgence().getSociete(), depots.getAgence());
                            y = buildLivraison(y, facture, num, d, dateLivraison, currentUser);
                            y = (YvsComDocVentes) dao.save1(y);
                            YvsComContenuDocVente c = null;
                            for (YvsComContenuDocVente content : list) {
                                if (content.getDepoLivraisonPrevu() != null ? content.getDepoLivraisonPrevu().getId() > 0 : false) {
                                    if (!d.equals(content.getDepoLivraisonPrevu())) {
                                        continue;
                                    }
                                }
                                c = buildContentLivraison(c, content, y, currentUser);
                                dao.save(c);
                                y.getContenus().add(c);
                            }
                            y.setDocumentLie(new YvsComDocVentes(facture.getId()));
                            facture.getDocuments().add(y);
                        }
                        if (continu) {
                            String rq = "UPDATE yvs_com_doc_ventes SET action = null, statut_livre = ? WHERE id=?";
                            Options[] param = new Options[]{new Options(statut.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE, 1), new Options(facture.getId(), 2)};
                            dao.requeteLibre(rq, param);
                            if (statut.equals(Constantes.ETAT_VALIDE)) {
                                rq = "UPDATE yvs_com_reservation_stock SET statut = 'L' WHERE id IN (SELECT id_reservation FROM yvs_com_contenu_doc_vente WHERE doc_vente = ?)";
                                param = new Options[]{new Options(facture.getId(), 1)};
                                dao.requeteLibre(rq, param);
                            }
                            facture.setDateConsigner(statut.equals(Constantes.ETAT_VALIDE) ? null : facture.getDateConsigner());
                            if (statut.equals(Constantes.ETAT_VALIDE)) {
                                for (YvsComContenuDocVente c : facture.getContenus()) {
                                    if (c.getIdReservation() != null ? c.getIdReservation().getId() != null ? c.getIdReservation().getId() > 0 : false : false) {
                                        long id = c.getIdReservation().getId();
                                        c.setIdReservation(null);
                                        rq = "UPDATE yvs_com_contenu_doc_vente SET id_reservation = null WHERE id = ?";
                                        param = new Options[]{new Options(c.getId(), 1)};
                                        dao.requeteLibre(rq, param);
                                        rq = "DELETE FROM yvs_com_reservation_stock WHERE id = ? AND id NOT IN (SELECT id_reservation FROM yvs_com_contenu_doc_vente WHERE id_reservation IS NOT NULL)";
                                        param = new Options[]{new Options(id, 1)};
                                        dao.requeteLibre(rq, param);
                                    }
                                }
                            }
                        }
                        facture.setConsigner(statut.equals(Constantes.ETAT_VALIDE) ? false : facture.getConsigner());
                        facture.setStatutLivre(statut.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE);
                        dao.update(facture);
                        facture.getDocuments().clear();
                        facture.getCommerciaux().clear();
                        facture.getContenus().clear();
                        facture.getCommissions().clear();
                        facture.getCouts().clear();
                        facture.getMensualites().clear();
                        facture.getReglements().clear();
                        facture.getRemises().clear();
                        facture.getReservations().clear();
                        facture.getRistournes().clear();
                        if (facture.getDepotLivrer() != null ? facture.getDepotLivrer().getArticles() != null : false) {
                            facture.getDepotLivrer().getArticles().clear();
                        }
                        dao.getEquilibreVente(facture.getId());
                        return new ResultatAction(true, facture, facture.getId(), "Succès");
                    }
                } else {
                    if (!facture.getContenus().isEmpty()) {
                        facture.setConsigner(false);
                        facture.setDateConsigner(null);
                        facture.setStatutLivre(Constantes.ETAT_LIVRE);
                        query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                        dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                        return new ResultatAction(true, null, null, "Succès");
                    } else {
                        query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                        dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                        return new ResultatAction(false, 0, "Vous ne pouvez pas livrer cette facture car elle est vide");
                    }
                }
            } else {
                query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                return new ResultatAction(false, 0, "Aucun modèle de reférence trouver");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
        dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
        return new ResultatAction(false, 0, "Action impossible");
    }

    private YvsComDocVentes buildLivraison(YvsComDocVentes result, YvsComDocVentes facture, String numDoc, YvsBaseDepots depot, Date dateLivraison, YvsUsersAgence currentUser) {
        result = new YvsComDocVentes(facture);
        result.setEnteteDoc(facture.getEnteteDoc());
        result.setClient(facture.getClient());
        result.setDateSave(new Date());
        result.setAuthor(currentUser);
        result.setTypeDoc(Constantes.TYPE_BLV);
        result.setNumDoc(numDoc);
        result.setNumPiece("BL N° " + facture.getNumDoc());
        result.setDepotLivrer(depot);
        result.setTrancheLivrer(facture.getTrancheLivrer());
        result.setLivreur(currentUser.getUsers());
        result.setDateLivraison(dateLivraison);
        result.setDateLivraisonPrevu(facture.getDateLivraisonPrevu());
        result.setDocumentLie(new YvsComDocVentes(facture.getId()));
        result.setHeureDoc(new Date());
        result.setStatut(Constantes.ETAT_VALIDE);
        result.setStatutLivre(Constantes.ETAT_ATTENTE);
        result.setStatutRegle(Constantes.ETAT_ATTENTE);
        result.setValiderBy(currentUser.getUsers());
        result.setDateValider(facture.getDateLivraisonPrevu());
        result.setDateSave(new Date());
        result.setDateUpdate(new Date());
        result.setCloturer(false);
        result.setCategorieComptable(facture.getCategorieComptable());
        result.setOperateur(facture.getOperateur());
        result.setAdresse(facture.getAdresse());
        result.setValiderBy(facture.getValiderBy());
        result.setDescription("Livraison de la facture N° " + facture.getNumDoc() + " le " + ldf.format(dateLivraison) + " à " + time.format(dateLivraison));
        result.setId(null);
        return result;
    }

    private YvsComContenuDocVente buildContentLivraison(YvsComContenuDocVente result, YvsComContenuDocVente contenu, YvsComDocVentes livraison, YvsUsersAgence currentUser) {
        result = new YvsComContenuDocVente();
        result.setDocVente(new YvsComDocVentes(livraison.getId()));
        result.setStatut(Constantes.ETAT_VALIDE);
        result.setAuthor(currentUser);
        result.setArticle(contenu.getArticle());
        result.setConditionnement(contenu.getConditionnement());
        result.setAuthor(contenu.getAuthor());
        result.setArticleBonus(null);
        result.setConditionnementBonus(null);
        result.setQuantiteBonus(contenu.getQuantiteBonus());
        result.setPrix(contenu.getPrix());
        result.setPrixTotal(contenu.getPrixTotal());
        result.setComission(contenu.getComission());
        result.setRabais(contenu.getRabais());
        result.setRemise(contenu.getRemise());
        result.setRistourne(contenu.getRistourne());
        result.setTaxe(contenu.getTaxe());
        result.setQuantite(contenu.getQuantite());
        result.setQteAttente(contenu.getQteAttente());
        result.setQteLivree(contenu.getQteLivree());
        result.setDepoLivraisonPrevu(contenu.getDepoLivraisonPrevu());
        result.setDateContenu(new Date());
        result.setParent(new YvsComContenuDocVente(contenu.getId()));

        result.setId(null);
        return result;
    }

    private YvsComContenuDocVente buildContentByBonus(YvsComContenuDocVente contenuBonus) {
        YvsComContenuDocVente result = new YvsComContenuDocVente(contenuBonus);
        result.setArticle(new YvsBaseArticles(contenuBonus.getArticleBonus().getId(), contenuBonus.getArticleBonus().getRefArt(), contenuBonus.getArticleBonus().getDesignation()));
        result.setConditionnement(new YvsBaseConditionnement(contenuBonus.getConditionnementBonus().getId(), new YvsBaseUniteMesure(contenuBonus.getConditionnementBonus().getUnite().getId(), contenuBonus.getConditionnementBonus().getUnite().getReference(), contenuBonus.getConditionnementBonus().getUnite().getLibelle())));
        result.setQuantite(contenuBonus.getQuantiteBonus());
        result.setArticleBonus(null);
        result.setConditionnementBonus(null);
        result.setQuantiteBonus(0.0);
        result.setPrix(0.0);
        result.setPrixTotal(0.0);
        result.setComission(0.0);
        result.setRabais(0.0);
        result.setRemise(0.0);
        result.setRistourne(0.0);
        result.setTaxe(0.0);
        return result;
    }

    public boolean validerOrder(YvsComDocVentes y, YvsComDocVentes facture, boolean save, boolean load, boolean msg) {
        return validerOrder(y, facture, save, load);
    }

    public boolean validerOrder(YvsComDocVentes entity, YvsComDocVentes facture, boolean save, boolean load) {
        if (entity == null) {
            return false;
        }

        if (!verifyOperation(entity.getEnteteDoc().getCreneau().getCreneauDepot().getDepot(), Constantes.SORTIE, Constantes.VENTE, true)) {
            return false;
        }
        //contrôle la cohérence avec les inventaires
        if (!verifyInventaire(entity.getEnteteDoc().getCreneau().getCreneauDepot().getDepot(), entity.getTrancheLivrer(), entity.getDateLivraison())) {
            return false;
        }
        boolean continu = true;

        if (continu) {
            if (livrer(entity, true)) {
                if (changeStatut(Constantes.ETAT_VALIDE, entity)) {
                    entity.setCloturer(false);
                    entity.setAnnulerBy(null);
                    entity.setValiderBy(entity.getAuthor().getUsers());
                    entity.setDateAnnuler(null);
                    entity.setDateCloturer(null);
                    entity.setDateValider(entity.getDateLivraison());
                    entity.setDateLivraison(entity.getDateLivraison());
                    entity.setDateUpdate(new Date());
                    if (entity.getAuthor() != null ? entity.getAuthor().getId() > 0 : false) {
                        entity.setAuthor(entity.getAuthor());
                    }
                    entity.setStatut(Constantes.ETAT_VALIDE);
                    YvsComDocVentes y = new YvsComDocVentes(entity);
                    y.getContenus().clear();
                    dao.update(y);
                    dao.getEquilibreVente(facture.getId());
                    return true;
                }
            }
        }
        return false;
    }

    public boolean changeStatutLivraison(String etat, YvsComDocVentes entity, boolean isBon, boolean load) {
        if (!etat.equals("")) {
            if (entity.getCloturer()) {
                return false;
            }
            String rq = "UPDATE yvs_com_doc_ventes SET statut = '" + etat + "' WHERE id=?";
            Options[] param = new Options[]{new Options(entity.getId(), 1)};
            dao.requeteLibre(rq, param);
            entity.setStatut(etat);
            entity.setStatut(etat);

            if (entity.getDocumentLie() != null ? (entity.getDocumentLie().getId() != null ? entity.getDocumentLie().getId() > 0 : false) : false) {
                dao.getEquilibreVente(entity.getDocumentLie().getId());
            }
            return true;
        }
        return false;
    }

    public boolean validerOrderLivraison(YvsComDocVentes entity, boolean save, boolean load, boolean msg, YvsUsersAgence currentUser) {
        if (entity == null) {
            return false;
        }

        if (!verifyOperation(entity.getDepotLivrer(), Constantes.SORTIE, Constantes.VENTE, msg)) {
            return false;
        }
        //contrôle la cohérence avec les inventaires
        if (!verifyInventaire(entity.getDepotLivrer(), entity.getTrancheLivrer(), entity.getDateLivraison())) {
            return false;
        }
        boolean continu = !save;
        if (save) {
            continu = true;
        }
        if (continu) {
            if (livrer(entity, msg)) {
                if (changeStatutLivraison(Constantes.ETAT_VALIDE, entity, load, false)) {
                    entity.setCloturer(false);
                    entity.setAnnulerBy(null);
                    entity.setValiderBy(currentUser.getUsers());
                    entity.setDateAnnuler(null);
                    entity.setDateCloturer(null);
                    entity.setDateValider(entity.getDateLivraison());
                    entity.setDateLivraison(entity.getDateLivraison());
                    entity.setDateUpdate(new Date());
                    if (currentUser != null ? currentUser.getId() > 0 : false) {
                        entity.setAuthor(currentUser);
                    }
                    entity.setStatut(Constantes.ETAT_VALIDE);
                    YvsComDocVentes y = new YvsComDocVentes(entity);
                    y.getContenus().clear();
                    dao.update(y);
                    dao.getEquilibreVente(entity.getId());
                    return true;
                }
            }
        }
        return false;
    }

    public boolean livrer(YvsComDocVentes entity, boolean msg) {

        if (entity == null) {
            return false;
        }
        if (entity.getDepotLivrer() != null ? entity.getDepotLivrer().getId() < 1 : true) {

            return false;
        }
        if (entity.getTrancheLivrer() != null ? entity.getTrancheLivrer().getId() < 1 : true) {

            return false;
        }
        if (entity.getLivreur() != null ? entity.getLivreur().getId() < 1 : true) {

            return false;
        }//verifier la cohérence avec les inventaires
        if (!controleInventaire(entity.getDepotLivrer().getId(), entity.getDateLivraison(), entity.getTrancheLivrer().getId())) {
            return false;
        }
        YvsComDocVentes facture = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{entity.getDocumentLie().getId()});
        if (facture.getEnteteDoc() != null ? (facture.getEnteteDoc().getCreneau() != null ? (facture.getEnteteDoc().getCreneau().getCreneauPoint() != null ? (facture.getEnteteDoc().getCreneau().getCreneauPoint().getPoint() != null) : false) : false) : false) {
            switch (facture.getEnteteDoc().getCreneau().getCreneauPoint().getPoint().getLivraisonOn()) {
                case 'R': {
                    if (!facture.getStatutRegle().equals(Constantes.ETAT_REGLE)) {

                        return false;
                    }
                }
                case 'V': {
                    if (!facture.getStatut().equals(Constantes.ETAT_VALIDE)) {

                        return false;
                    }
                }
            }
        }
        if (entity.getContenus() != null ? !entity.getContenus().isEmpty() : false) {
            String result = null;
            YvsComContenuDocVente cc = null;
            for (YvsComContenuDocVente c : entity.getContenus()) {
                cc = c;
                result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), entity.getDepotLivrer().getId(), 0L, c.getQuantite(), 0, "INSERT", "S", entity.getDateLivraison(), (c.getLot() != null ? c.getLot().getId() : 0));
                if (result != null) {
                    break;
                }
                //controle les quantités déjà livré
                Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findByDocLierTypeStatutArticleS", new String[]{"docVente", "statut", "typeDoc", "article", "unite"}, new Object[]{facture, Constantes.ETAT_VALIDE, Constantes.TYPE_BLV, c.getArticle(), c.getConditionnement()});
                qteLivre = (qteLivre != null) ? qteLivre : 0;
                //trouve la quantité d'article facturé 
                Double qteFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByArticle", new String[]{"docVente", "article", "unite"}, new Object[]{facture, c.getArticle(), c.getConditionnement()});
                qteFacture = (qteFacture != null) ? qteFacture : 0;
                //trouve la quantité d'article facturé 
                Double qteBonusFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteBonusByFacture", new String[]{"docVente", "article", "unite"}, new Object[]{facture, c.getArticle(), c.getConditionnement()});
                qteBonusFacture = (qteBonusFacture != null) ? qteBonusFacture : 0;
                if (facture != null ? !facture.getStatutRegle().equals(Constantes.ETAT_REGLE) : true) {
                    //si la facture n'est pas encore réglé, on ne dois pas inclure la quantité bonus dans la quantité à livrer
                    if (c.getQuantite() > (qteFacture - qteLivre)) {

                        return false;
                    }
                } else {
                    if (c.getQuantite() > ((qteFacture + qteBonusFacture) - qteLivre)) {

                        return false;
                    }
                }

                String[] champ = new String[]{"article", "depot"};
                Object[] val = new Object[]{c.getArticle(), new YvsBaseDepots(entity.getDepotLivrer().getId())};
                YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
                if (y != null ? y.getId() < 1 : true) {
                    return false;
                }
            }
            if (result != null) {
                return false;
            }
            entity.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_LIVRER));
            entity.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_LIVRER));
            entity.setDateLivraison(entity.getStatutLivre().equals(String.valueOf(Constantes.STATUT_DOC_LIVRER)) ? new Date() : null);
            return true;
        } else {

        }
        return false;
    }

    public String controleStock(long article, long conditionnement, long depot, long tranche, double newQte, double oldQte, String action, String mouvement, Date date, long lot) {
        return dao.controleStock(article, conditionnement, depot, tranche, newQte, oldQte, action, mouvement, date, lot);
    }

    public String controleStock(long article, long conditionnement, long oldCond, long depot, long tranche, double newQte, double oldQte, String action, String mouvement, Date date, long lot) {
        return dao.controleStock(article, conditionnement, oldCond, depot, tranche, newQte, oldQte, action, mouvement, date, lot);
    }

    public List<YvsGrhTrancheHoraire> loadTranche(YvsBaseDepots y, Date date) {
        return loadTranche(null, y, date);
    }

    public List<YvsGrhTrancheHoraire> loadTranche(YvsUsers u, YvsBaseDepots y, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date debut = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 2);
        Date fin = cal.getTime();

        String nameQueri = "YvsComCreneauHoraireUsers.findTrancheByDepotDates";
        String[] champ = new String[]{"depot", "debut", "fin"};
        Object[] val = new Object[]{y, debut, fin};
        if (u != null ? u.getId() > 0 : false) {
            nameQueri = "YvsComCreneauHoraireUsers.findTrancheByUsersDepotDates";
            champ = new String[]{"users", "depot", "debut", "fin"};
            val = new Object[]{u, y, debut, fin};
        }
        List<YvsGrhTrancheHoraire> l = dao.loadNameQueries(nameQueri, champ, val);
        return l != null ? l : new ArrayList<YvsGrhTrancheHoraire>();
    }

    private List<YvsComContenuDocVente> loadContenusStay(YvsComDocVentes y, String type) {
        String nameQueri = "YvsComContenuDocVente.findByDocVente";
        String[] champ = new String[]{"docVente"};
        Object[] val = new Object[]{y};
        List<YvsComContenuDocVente> contenus = dao.loadNameQueries(nameQueri, champ, val);
        return loadContenusStay(y, contenus, type);
    }

    private List<YvsComContenuDocVente> loadContenusStay(YvsComDocVentes y, List<YvsComContenuDocVente> contenus, String type) {
        List<YvsComContenuDocVente> list = new ArrayList<>();
        y.setInt_(false);
        String[] ch;
        Object[] v;
        Double qte;
        for (YvsComContenuDocVente c : contenus) {
            ch = new String[]{"parent", "typeDoc", "statut"};
            v = new Object[]{c, type, Constantes.ETAT_VALIDE};
            qte = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByTypeStatutParent", ch, v);
            if (c.getQuantite() > (qte != null ? qte : 0)) {
                c.setQuantite_(c.getQuantite());
                c.setQuantite(c.getQuantite() - (qte != null ? qte : 0));
                c.setPrixTotal(c.getPrix() * c.getQuantite());
                c.setParent(new YvsComContenuDocVente(c.getId()));
                list.add(c);
            }
        }
        return list;
    }

    public boolean verifyTranche(YvsGrhTrancheHoraire tranche, YvsBaseDepots depot, Date date) {
        if (date == null) {
            return false;
        }
        if (depot != null ? depot.getId() < 1 : true) {
            return false;
        }
        tranche = (YvsGrhTrancheHoraire) dao.loadOneByNameQueries("YvsGrhTrancheHoraire.findById", new String[]{"id"}, new Object[]{tranche.getId()});
        if (tranche != null ? tranche.getId() > 0 : false) {
            Long y = (Long) dao.loadOneByNameQueries("YvsBaseMouvementStock.countMvtOtherTypeJ", new String[]{"depot", "dateDoc", "typeJ"}, new Object[]{depot, date, tranche.getTypeJournee()});
            if (y != null ? y > 0 : false) {
                return false;
            }
        }
        return true;
    }

    private boolean controlContentForTransmis(YvsComContenuDocVente c, YvsBaseDepots depot, Date dateLivraison, String statut, boolean message) {
        String[] champ = new String[]{"article", "depot"};
        Object[] val = new Object[]{c.getArticle(), depot};
        YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
        if (y != null ? y.getId() < 1 : true) {
            return false;
        }
        if (statut.equals(Constantes.ETAT_VALIDE)) {
            String result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), depot.getId(), 0L, c.getQuantite(), 0, "INSERT", "S", dateLivraison, (c.getLot() != null ? c.getLot().getId() : 0));
            if (result != null) {
                return false;
            }
        }
        return true;
    }

    public List<YvsBaseCaisse> loadCaisses(YvsUsers y, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        List<YvsBaseCaisse> list = new ArrayList<>();
        if (y != null ? y.getId() > 0 : false) {
            List<Long> codes = dao.loadNameQueries("YvsBaseUsersAcces.findIdAccesByUsers", new String[]{"users", "societe"}, new Object[]{currentUser.getUsers(), currentScte});
            if (codes != null ? codes.isEmpty() : true) {
                codes.add(-1L);
            }
            List<Long> ids = dao.loadNameQueries("YvsBaseCaisseUser.findIdCaisseByUser", new String[]{"user"}, new Object[]{currentUser.getUsers()});
            if (ids != null ? ids.isEmpty() : true) {
                ids.add(-1L);
            }
            String nameQueri = "YvsBaseCaisse.findAllForUsers";
            String[] champ = new String[]{"codes", "ids", "caissier", "responsable"};
            Object[] val = new Object[]{codes, ids, y, (y.getEmploye() != null ? y.getEmploye().getId() : null)};
            list = dao.loadNameQueries(nameQueri, champ, val);
        }
        return list;
    }

    public List<YvsComptaCaissePieceVente> generetedPiecesFromModel(YvsBaseModelReglement model, YvsComDocVentes d, YvsBaseCaisse caisse, YvsSocietes currentScte, YvsUsersAgence currentUser, YvsAgences currentAgence, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint) {
        List<YvsComptaCaissePieceVente> re = new ArrayList<>();
        List<YvsBaseTrancheReglement> lt = dao.loadNameQueries("YvsBaseTrancheReglement.findByModel", new String[]{"model"}, new Object[]{model});
        if (d.getMontantResteAPlanifier() > 0) {
            long id = -1000;
            YvsComptaCaissePieceVente piece;
            Calendar cal = Calendar.getInstance();
            cal.setTime(d.getEnteteDoc().getDateEntete());  //date de la facturation
            double totalTaux = 0, sommeMontant = 0;
            YvsBaseModeReglement espece = modeEspece(currentScte), mode = null;
            YvsBaseTrancheReglement trch;
            if (lt != null ? !lt.isEmpty() : false) {
                for (int i = 0; i < lt.size() - 1; i++) {
                    trch = lt.get(i);
                    cal.add(Calendar.DAY_OF_MONTH, trch.getIntervalJour());
                    double montant = arrondi(d.getMontantResteAPlanifier() * trch.getTaux() / 100, currentScte);
                    sommeMontant += montant;
                    if (trch.getMode() != null ? trch.getMode().getId() != null ? trch.getMode().getId() > 0 : false : false) {
                        mode = trch.getMode();
                    } else {
                        mode = espece;
                    }
                    piece = buildPieceFromModel(id++, mode, d, caisse, cal.getTime(), montant, ' ', currentUser, currentScte, currentAgence, currentDepot, currentPoint);
                    re.add(piece);
                    totalTaux += trch.getTaux();
                }

                trch = lt.get(lt.size() - 1);
                if (trch.getMode() != null ? trch.getMode().getId() != null ? trch.getMode().getId() > 0 : false : false) {
                    mode = trch.getMode();
                } else {
                    mode = espece;
                }
                totalTaux += trch.getTaux();
                cal.add(Calendar.DAY_OF_MONTH, trch.getIntervalJour());

            }
            if (mode != null ? mode.getId() != null ? mode.getId() < 1 : true : true) {
                mode = espece;
            }
            double montant = d.getMontantResteAPlanifier() - sommeMontant;
            piece = buildPieceFromModel(id++, mode, d, caisse, cal.getTime(), montant, ' ', currentUser, currentScte, currentAgence, currentDepot, currentPoint);
            re.add(piece);
        } else {
        }
        return re;
    }

    public YvsBaseModeReglement modeEspece(YvsSocietes currentScte) {
        if (dao != null ? modeByEspece == null : false) {
            String nameQueri = "YvsBaseModeReglement.findByDefault";
            String[] champ = new String[]{"societe", "actif", "type", "defaut"};
            Object[] val = new Object[]{currentScte, true, Constantes.MODE_PAIEMENT_ESPECE, true};
            modeByEspece = (YvsBaseModeReglement) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (modeByEspece != null ? (modeByEspece.getId() != null ? modeByEspece.getId() < 1 : true) : true) {
                nameQueri = "YvsBaseModeReglement.findByTypeActif";
                champ = new String[]{"societe", "actif", "type"};
                val = new Object[]{currentScte, true, Constantes.MODE_PAIEMENT_ESPECE};
                modeByEspece = (YvsBaseModeReglement) dao.loadOneByNameQueries(nameQueri, champ, val);
            }
        }
        return modeByEspece;
    }

    public YvsComptaCaissePieceVente buildPieceFromModel(long id, YvsBaseModeReglement mode, YvsComDocVentes d, YvsBaseCaisse caisse, Date date, double montant) {
        return buildPieceFromModel(id, mode, d, caisse, date, montant, Constantes.MOUV_CAISS_ENTREE.charAt(0));
    }

    public YvsComptaCaissePieceVente buildPieceFromModel(long id, YvsBaseModeReglement mode, YvsComDocVentes d, YvsBaseCaisse caisse, Date date, double montant, char mouvement) {
        return buildPieceFromModel(id, mode, d, caisse, date, montant, mouvement);
    }

    public YvsComptaCaissePieceVente buildPieceFromModel(long id, YvsBaseModeReglement mode, YvsComDocVentes d, YvsBaseCaisse caisse, Date date, double montant, char mouvement, YvsUsersAgence currentUser, YvsSocietes currentScte, YvsAgences currentAgence, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint) {
        YvsComptaCaissePieceVente piece = new YvsComptaCaissePieceVente(id);
        piece.setAuthor(currentUser);
        piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        piece.setMontant(montant);
        piece.setMontantRecu(montant);
        piece.setDatePaimentPrevu(date);
        piece.setMouvement(mouvement);
        piece.setDatePiece(new Date());
        String ref = genererReference(Constantes.TYPE_PC_VENTE_NAME, piece.getDatePiece(), 0, currentScte, currentAgence, null, currentDepot, currentPoint);
        if (ref != null ? ref.trim().length() < 1 : true) {
            return null;
        }
        piece.setNumeroPiece(ref);
        if (asString(piece.getReferenceExterne())) {
            piece.setReferenceExterne(d.getNumDoc());
        }
        piece.setVente(d);
        if ((mode != null) ? mode.getId() > 0 : false) {
            piece.setModel(mode);
        }
        if ((caisse != null) ? caisse.getId() > 0 : false) {
            piece.setCaisse(caisse);
        }
        piece.setComptabilise(false);
        return piece;
    }

    public static double arrondi(double d, int l) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(l, BigDecimal.ROUND_DOWN);
        double r = bd.doubleValue();
        return r;
    }

    public double arrondi(double d, YvsSocietes currentScte) {
        return arrondi(d, currentScte != null ? currentScte.getId() : 0, dao);
    }

    public double arrondi(double d, long societe, DaoInterfaceLocal dao) {
        try {
            return dao.arrondi(societe, d);
        } catch (Exception ex) {
            return 0;
        }
    }

    public ResultatAction<YvsComDocVentes> controleValidation(YvsComDocVentes selectDoc) {
        if (selectDoc == null) {
            return new ResultatAction<>(false, null, 0L, "Le document ne peut pas être null");
        }
        if (selectDoc.getCloturer()) {
            return new ResultatAction<>(false, null, 0L, "Le document est déjà cloturer ");
        }

        if (selectDoc.getContenus().isEmpty()) {
            return new ResultatAction<>(false, null, 0L, "Le contenu de la facture ne peut pas être vide ");
        }

        for (YvsComContenuDocVente c : selectDoc.getContenus()) {
            //controle les quantités déjà facturé
            Double qteFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findByDocLierTypeStatutArticleS", new String[]{"docVente", "statut", "typeDoc", "article", "unite"}, new Object[]{selectDoc, Constantes.ETAT_VALIDE, Constantes.TYPE_FV, c.getArticle(), c.getConditionnement()});
            qteFacture = (qteFacture != null) ? qteFacture : 0;
            //trouve la livré
            Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByDocLivre", new String[]{"facture", "article", "typeDoc", "statut", "unite"}, new Object[]{selectDoc, c.getArticle(), Constantes.TYPE_BLV, Constantes.ETAT_VALIDE, c.getConditionnement()});
            qteLivre = (qteLivre != null) ? qteLivre : 0;
            if (qteFacture < (qteLivre)) {
                return new ResultatAction<>(false, null, 0L, "Vérifiez la quantité ! ");
            }
        }
        return new ResultatAction<>(true, null, 0L, "Succès");
    }

    public boolean reglerPieceTresorerie(YvsComDocVentes vente, YvsComptaCaissePieceVente pc, String source, boolean msg, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        if (pc != null ? pc.getId() > 0 : false) {//si c'est une suspension, on controle juste le droit
            if (controleAccesCaisse(pc.getCaisse(), true, currentUser)) {
                if (pc.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU) { //la pièce ne doit pas être déjà payé
                    //la pièce de caisse doit avoir une caisse
                    int action = giveAction(pc);
                    pc.setAuthor(currentUser);
                    pc.setNew_(true);

                    boolean update = false;
                    if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                        //Vérifie s'il s'agit d'une compensation de la cohérence des montants
                        if (pc.getNotifs() != null) {
                            Double reste = AYvsComptaAcompteClient.findResteForAcompte(pc.getNotifs().getAcompte(), dao);
                            if ((reste != null ? reste : 0) < pc.getMontant()) {
                                if (msg) {
                                    return false;
                                }
                            }
                        }
                    }

                    switch (action) {
                        case 1://valider la pièce de vente
                            pc.setDatePaiement(null);
                            pc.setStatutPiece(Constantes.STATUT_DOC_VALIDE);
                            pc.setValideBy(currentUser.getUsers());
                            pc.setDateValide(new Date());
                            update = true;
                            break;
                        case 3://Mettre la pièce en attente                           
                            if (dao.isComptabilise(pc.getId(), Constantes.SCR_CAISSE_VENTE)) {
                                if (!unComptabiliserCaisseVente(pc, false, currentScte)) {
                                    return false;
                                }
                            }
                            pc.setDatePaiement(null);
                            pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                            pc.setValideBy(null);
                            pc.setDateValide(null);
                            update = true;
                            break;
                        case 2://Encaisser la pièce
                            if ((pc.getCaisse() != null) ? (pc.getCaisse().getId() > 0 && pc.getVente() != null) : false) {
                                YvsComDocVentes d = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{pc.getVente().getId()});
                                if ("F".equals(source)) {
                                    if ((vente != null && pc.getVente() != null) ? vente.getId() != pc.getVente().getId() : true) {
                                        vente = (d);
                                        setMontantTotalDoc(vente, currentScte);
                                    }
                                }
                                if (controleAddPiece(("F".equals(source)) ? vente : null, pc)) {
                                    if (pc.getCaissier() != null ? pc.getCaissier().getId() < 1 : true) {
                                        pc.setCaissier(currentUser.getUsers());
                                    }
                                    pc.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                                    pc.setValideBy(currentUser.getUsers());
                                    pc.setDateValide(new Date());
                                    pc.setDatePaiement(pc.getDatePaimentPrevu());
                                    update = true;
                                } else {
                                    update = false;
                                }
                            } else if (msg) {
                                if (pc.getVente() == null) {

                                } else {

                                }
                            }
                            break;
                        default:
                            break;
                    }
                    if (update) {
                        if (pieceReglement != null ? pc.getId().equals(pieceReglement.getId()) : false) {
                            pieceReglement.setStatutPiece(pc.getStatutPiece());
                        }
                        dao.update(pc);
                        if (pc.getVente() != null ? pc.getVente().getId() > 0 : false) {
                            if (pc.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                if (isComptabilise(pc.getVente().getId(), Constantes.SCR_VENTE)) {
                                    comptabiliserCaisseVente(pc);
                                    comptabiliserCaisseVente(pc);
                                }
                            }
                            dao.getEquilibreVente(pc.getVente().getId());

                        }
                        if (vente != null ? vente.getReglements() != null : false) {
                            int idx = vente.getReglements().indexOf(pc);
                            if (idx >= 0) {
                                vente.getReglements().set(idx, pc);

                            }
                        }
                        if (reglements != null) {
                            int idx = reglements.indexOf(pc);
                            if (idx >= 0) {
                                reglements.set(idx, pc);

                            }
                        }
                        if (pieceAvance != null ? pc.getId().equals(pieceAvance.getId()) : false) {
                            pieceAvance.setStatutPiece(pc.getStatutPiece());
                        }
                        if (pc.getNotifs() != null ? pc.getNotifs().getAcompte() != null : false) {

                            equilibre(pc.getNotifs().getAcompte());

                        }

                        return true;
                    }

                }
            }
        }
        return false;
    }

    public void equilibre(YvsComptaAcompteClient y) {
        if (y != null ? y.getId() > 0 : false) {
            y.setStatutNotif(equilibreAcompte(y.getId()));
            int idx = acomptes.indexOf(y);
            if (idx > -1) {
                acomptes.set(idx, y);

            }
        }
    }

    public char equilibreAcompte(long id) {
        char statut = Constantes.STATUT_DOC_ATTENTE;
        if (id > 0) {
            String query = "SELECT equilibre_acompte_client(?)";
            String result = (String) dao.loadObjectBySqlQuery(query, new Options[]{new Options(id, 1)});
            if (asString(result)) {
                statut = result.charAt(0);
            }
        }
        return statut;
    }

    public boolean controleAccesCaisse(YvsBaseCaisse caisse, boolean msg, YvsUsersAgence currentUser) {
        if (caisse != null ? caisse.getId() < 1 : true) {
            if (msg) {

            }
            return false;
        }
        caisse = (YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{caisse.getId()});
        if (currentUser.getUsers() != null ? currentUser.getUsers().getId() < 1 : true) {
            if (msg) {

            }
            return false;
        }
        String error = null;
        if (currentUser.getUsers().getEmploye() != null ? currentUser.getUsers().getEmploye().getId() > 0 : false) {
            if (caisse.getResponsable() != null ? caisse.getResponsable().getId() > 0 : false) {
                if (!caisse.getResponsable().equals(currentUser.getUsers().getEmploye())) {
                    error = "car vous n'etes pas responsable de cette caisse";
                } else {
                    return true;
                }
            }
        }
        if (caisse.getCaissier() != null ? caisse.getCaissier().getId() > 0 : false) {
            if (!caisse.getCaissier().equals(currentUser.getUsers())) {
                error = "car vous n'etes pas le caissier de cette caisse";
            } else {
                return true;
            }
        }
        if (caisse.getCodeAcces() != null ? caisse.getCodeAcces().getId() < 1 : true) {

        } else {
            YvsBaseUsersAcces acces = (YvsBaseUsersAcces) dao.loadOneByNameQueries("YvsBaseUsersAcces.findOne", new String[]{"users", "code"}, new Object[]{currentUser.getUsers(), caisse.getCodeAcces()});
            if (acces != null ? acces.getId() < 1 : true) {
                if (msg) {

                }
                return false;
            }
        }
        return true;
    }

    private int giveAction(YvsComptaCaissePieceVente pc) {
        if (pc != null) {
            if (pc.getStatutPiece() == Constantes.STATUT_DOC_ATTENTE) {
                return 2;   //valider
            } else if (pc.getStatutPiece() == Constantes.STATUT_DOC_VALIDE) {
                return 2;//Encaisser
            } else {
                return 3; //En attente
            }
        }
        return 0;
    }

    public boolean unComptabiliserCaisseVente(YvsComptaCaissePieceVente y, boolean msg, YvsSocietes currentScte) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            //Vérifié avant tout que la période n'est pas clôturé            

            if (y.getVerouille()) {

                return false;
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), y.getVente().getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_CAISSE_AVOIR_VENTE : Constantes.SCR_CAISSE_VENTE};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (deletePieceComptable(list, msg, currentScte)) {
                String query = "DELETE FROM yvs_compta_pieces_comptable WHERE id IN "
                        + "(SELECT DISTINCT p.piece FROM yvs_compta_content_journal_piece_vente p LEFT JOIN yvs_compta_content_journal c ON p.piece = c.piece WHERE c.id IS NULL AND p.reglement = ?)";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setPieceContenu(null);
                y.setComptabilised(false);
                y.setComptabilise(false);
                //Comptabilisation des compensation
                if (y.getCompensations() != null ? !y.getCompensations().isEmpty() : false) {
                    for (YvsComptaCaissePieceCompensation o : y.getCompensations()) {
                        boolean comptabilise = isComptabilise(o.getAchat().getId(), Constantes.SCR_CAISSE_ACHAT);
                        if (!comptabilise) {
                            continue;
                        }
                        unComptabiliserCaisseAchat(o.getAchat(), false, currentScte);
                    }
                }

                return true;
            }
        }
        return false;
    }

    public boolean unComptabiliserCaisseAchat(YvsComptaCaissePieceAchat y, boolean msg, YvsSocietes currentScte) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {

            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_CAISSE_ACHAT};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (deletePieceComptable(list, msg, currentScte)) {
                String query = "DELETE FROM yvs_compta_pieces_comptable WHERE id IN "
                        + "(SELECT DISTINCT p.piece FROM yvs_compta_content_journal_piece_achat p LEFT JOIN yvs_compta_content_journal c ON p.piece = c.piece WHERE c.id IS NULL AND p.reglement = ?)";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
                //Comptabilisation des compensation
                if (y.getCompensations() != null ? !y.getCompensations().isEmpty() : false) {
                    for (YvsComptaCaissePieceCompensation o : y.getCompensations()) {
                        boolean comptabilise = isComptabilise(o.getVente().getId(), Constantes.SCR_CAISSE_VENTE);
                        if (!comptabilise) {
                            continue;
                        }
                        unComptabiliserCaisseVente(o.getVente(), false, currentScte);
                    }
                }

                return true;
            }
        }
        return false;
    }

    public boolean isComptabilise(long id, String table) {
        return dao.isComptabilise(id, table);
    }

    private boolean deletePieceComptable(List<YvsComptaPiecesComptable> list, boolean msg, YvsSocietes currentScte) {
        try {
            if (list != null ? !list.isEmpty() : false) {
                for (YvsComptaPiecesComptable d : list) {
                    if (d.getStatutPiece().equals(Constantes.STATUT_DOC_VALIDE)) {
                        if (msg) {

                        }
                        return false;
                    }
                    YvsBaseExercice exo = controleExercice(d.getDatePiece(), msg, currentScte);
                    if (exo != null ? exo.getId() < 1 : true) {
                        return false;
                    }
                }
                for (YvsComptaPiecesComptable d : list) {
                    dao.delete(d);
                }
            }
            return true;
        } catch (Exception ex) {

        }
        return false;
    }

    private YvsBaseExercice controleExercice(Date date, boolean msg, YvsSocietes currentScte) {
        YvsBaseExercice exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByDate", new String[]{"societe", "date"}, new Object[]{currentScte, date});
        if (exo != null ? exo.getId() < 1 : true) {
            if (msg) {

            }
            return null;
        }
        if (!exo.getActif()) {
            if (msg) {

            }
            return null;
        }
        if (exo.getCloturer()) {
            if (msg) {

            }
            return null;
        }
        return exo;
    }

    public double setMontantTotalDoc(YvsComDocVentes doc, YvsSocietes currentScte) {
        long societe = currentScte != null ? currentScte.getId() : 0;
        return setMontantTotalDoc(doc, societe, null, null, dao);
    }

    public double setMontantTotalDoc(YvsComDocVentes doc, Date dateDebut, Date dateFin, YvsSocietes currentScte) {
        long societe = currentScte != null ? currentScte.getId() : 0;
        return setMontantTotalDoc(doc, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(YvsComDocVentes doc, long societe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        String[] champ = new String[]{"docVente"};
        Object[] val = new Object[]{doc};
        List<YvsComContenuDocVente> lc = dao.loadNameQueries("YvsComContenuDocVente.findByDocVente", champ, val);
        return setMontantTotalDoc(doc, lc, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(YvsComDocVentes doc, List<YvsComContenuDocVente> lc, Date dateDebut, Date dateFin, YvsSocietes currentScte) {
        long societe = currentScte != null ? currentScte.getId() : 0;
        return setMontantTotalDoc(doc, lc, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(YvsComDocVentes doc, List<YvsComContenuDocVente> lc, long societe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        YvsComDocVentes d = new YvsComDocVentes(doc.getId());
        setMontantTotalDoc(d, lc, societe, dateDebut, dateFin, dao);

        doc.setMontantRemise(d.getMontantRemise());
        doc.setMontantTaxe(d.getMontantTaxe());
        doc.setMontantRistourne(d.getMontantRistourne());
        doc.setMontantCommission(d.getMontantCommission());
        doc.setMontantHT(d.getMontantHT());
        doc.setMontantTTC(d.getMontantTTC());
        doc.setMontantRemises(d.getMontantRemises());
        doc.setMontantCS(d.getMontantCS());
        doc.setMontantAvance(d.getMontantAvance());
        doc.setMontantAvoir(d.getMontantAvoir());
        doc.setMontantAvanceAvoir(d.getMontantAvanceAvoir());
        doc.setMontantTaxeR(d.getMontantTaxeR());
        doc.setMontantResteApayer(d.getMontantResteApayer());

        return doc.getMontantTotal();
    }

    public double setMontantTotalDoc(YvsComDocVentes doc, List<YvsComContenuDocVente> lc, long societe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao, YvsSocietes currentScte) {
        if (doc != null) {
            doc.setMontantRemise(0);
            doc.setMontantTaxe(0);
            doc.setMontantRistourne(0);
            doc.setMontantCommission(0);
            doc.setMontantHT(0);
            doc.setMontantTTC(0);
            doc.setMontantRemises(0);
            doc.setMontantCS(0);
            doc.setMontantAvance(0.0);
            doc.setMontantTaxeR(0);
            doc.setMontantResteApayer(0);
            doc.setMontantPlanifier(0);
            if (lc != null ? !lc.isEmpty() : false) {
                for (YvsComContenuDocVente c : lc) {
                    doc.setMontantRemise(doc.getMontantRemise() + c.getRemise());
                    doc.setMontantRistourne(doc.getMontantRistourne() + c.getRistourne());
                    doc.setMontantCommission(doc.getMontantCommission() + c.getComission());
                    doc.setMontantTTC(doc.getMontantTTC() + c.getPrixTotal());
                    doc.setMontantTaxe(doc.getMontantTaxe() + c.getTaxe());
                    doc.setMontantTaxeR(doc.getMontantTaxeR() + ((c.getArticle().getPuvTtc()) ? (c.getTaxe()) : 0));
                }
            }

            String[] champ = new String[]{"facture", "statut"};
            Object[] val = new Object[]{doc, Constantes.STATUT_DOC_SUSPENDU};
            String nameQueri = "YvsComptaCaissePieceVente.findByFactureStatutSDiff";
            Double a = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            doc.setMontantPlanifier(a != null ? a : 0);

            if (dateFin == null) {
                champ = new String[]{"facture", "statut"};
                val = new Object[]{doc, Constantes.STATUT_DOC_PAYER};
//                nameQueri = "YvsComptaCaissePieceVente.findDepenseByFactureStatutS";
//                Double d = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                nameQueri = "YvsComptaCaissePieceVente.findByFactureStatutS";
                a = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                doc.setMontantAvance((a != null ? a : 0));
//                doc.setMontantAvance((a != null ? a : 0) - (d != null ? d : 0));
            } else {
                champ = new String[]{"facture", "statut", "date"};
                val = new Object[]{doc, Constantes.STATUT_DOC_PAYER, dateFin};
//                nameQueri = "YvsComptaCaissePieceVente.findDepenseByMensualiteStatutS";
//                Double d = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                nameQueri = "YvsComptaCaissePieceVente.findByMensualiteStatutS";
                a = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
//                doc.setMontantAvance((a != null ? a : 0) - (d != null ? d : 0));
                doc.setMontantAvance(a != null ? a : 0);
            }

            champ = new String[]{"docVente", "sens", "service"};
            val = new Object[]{doc, true, true};
            Double p = (Double) dao.loadObjectByNameQueries("YvsComCoutSupDocVente.findSumServiceByDocVente", champ, val);
            val = new Object[]{doc, false, true};
            Double m = (Double) dao.loadObjectByNameQueries("YvsComCoutSupDocVente.findSumServiceByDocVente", champ, val);
            double s = (p != null ? p : 0) - (m != null ? m : 0);
            doc.setMontantCS(s);

            double caAvoir = 0, avanceAvoir = 0;
            List<YvsComDocVentes> avoirs = dao.loadNameQueries("YvsComDocVentes.findByParentTypeDocStatut", new String[]{"documentLie", "typeDoc", "statut"}, new Object[]{doc, Constantes.TYPE_FAV, Constantes.ETAT_VALIDE});;
            for (YvsComDocVentes av : avoirs) {
                caAvoir += setMontantTotalDoc(av, currentScte);
                avanceAvoir += av.getMontantAvance();
            }
            doc.setMontantAvoir(caAvoir);
            doc.setMontantAvanceAvoir(avanceAvoir);

            doc.setMontantRemise(arrondi(doc.getMontantRemise(), societe, dao));
            doc.setMontantTaxe(arrondi(doc.getMontantTaxe(), societe, dao));
            doc.setMontantRistourne(arrondi(doc.getMontantRistourne(), societe, dao));
            doc.setMontantCommission(arrondi(doc.getMontantCommission(), societe, dao));
            doc.setMontantHT(arrondi(doc.getMontantHT(), societe, dao));
            doc.setMontantTTC(arrondi(doc.getMontantTTC(), societe, dao));
            doc.setMontantRemises(arrondi(doc.getMontantRemises(), societe, dao));
            doc.setMontantCS(arrondi(doc.getMontantCS(), societe, dao));
            doc.setMontantAvance(arrondi(doc.getMontantAvance(), societe, dao));
            doc.setMontantTaxeR(arrondi(doc.getMontantTaxeR(), societe, dao));
            doc.setMontantResteApayer(arrondi(doc.getMontantResteApayer(), societe, dao));
            return doc.getMontantTotal();
        }
        return 0;
    }

    private boolean controleAddPiece(YvsComDocVentes d, YvsComptaCaissePieceVente pt) {
        if (pt.getMontant() <= 0) {

            return false;
        }
        if (pt.getId() > 0 && pt.getVerouille()) {

            return false;
        }
        if (pt.getId() > 0 && pt.getStatutPiece().equals(Constantes.STATUT_DOC_ANNULE)) {

            return false;
        }
        if (d != null) {
            double mtn;
            if (d.getReglements().contains(pt)) {//en cas de modification d'une pièce(isoler le montantF à comparer)
                mtn = (-d.getReglements().get(d.getReglements().indexOf(pt)).getMontant() + pt.getMontant());
            } else {
                mtn = pt.getMontant();
            }
            if (d.getMontantNetAPayer() < (giveTotalPT(d.getReglements()) + mtn) && pt.getVente() != null) {

                return false;
            }
        }
        if (pt.getDatePaimentPrevu() == null) {

            return false;
        }
        if (pt.getStatutPiece() == Constantes.STATUT_DOC_PAYER && (pt.getId() != null) ? pt.getId() > 0 : false) {

            return false;
        }
        if ((pt.getModel() != null) ? (pt.getModel().getId() != null ? pt.getModel().getId() < 1 : true) : true) {

            return false;
        }
        if (pt.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
            if (pt.getReferenceExterne() != null ? pt.getReferenceExterne().trim().length() < 1 : true) {

                return false;
            }
        }
        return true;
    }

    public double giveTotalPT(List<YvsComptaCaissePieceVente> l) {
        double sum = 0;
        if (l != null) {
            for (YvsComptaCaissePieceVente p : l) {
                if (p.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU && p.getStatutPiece() != Constantes.STATUT_DOC_ANNULE) {
                    sum += p.getMontant();
                }
            }
        }
        return sum;
    }

    private boolean comptabiliserCaisseVente(List<YvsComptaContentJournal> contenus, boolean msg, YvsUsersAgence currentUser, YvsSocietes currentScte, YvsAgences currentAgence) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;
        if (ids != null ? !ids.isEmpty() : false) {

            boolean error = false;
            List<YvsComptaContentJournal> list;
            for (int idx : ids) {
                YvsComptaCaissePieceVente y = getReglements().get(idx);
                list = new ArrayList<>();
                for (YvsComptaContentJournal c : contenus) {
                    if (c.getRefExterne().equals(y.getId())) {
                        list.add(c);
                    }
                }
                if (!list.isEmpty()) {
                    if (comptabiliserCaisseVente(y, list, false, false, currentUser, currentScte, currentAgence)) {
                        succes = true;
                    } else {
                        error = true;
                    }
                }
            }
            docIds = "";

        } else {
            succes = comptabiliserCaisseVente(selectCaisseVente, contenus, msg, false, currentUser, currentScte, currentAgence);
        }
        return succes;
    }

    public boolean comptabiliserCaisseVente(YvsComptaCaissePieceVente y) {
        return comptabiliserCaisseVente(y);
    }

//    public boolean comptabiliserCaisseVente(YvsComptaCaissePieceVente y, boolean msg) {
//        return comptabiliserCaisseVente(y, msg, true);
//    }
//
//    public boolean comptabiliserCaisseVente(YvsComptaCaissePieceVente y, boolean msg, boolean succes) {
//        return comptabiliserCaisseVente(y, buildCaisseVenteToComptabilise(y.getId(), msg), msg, succes);
//    }
    private boolean comptabiliserCaisseVente(YvsComptaCaissePieceVente y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes, YvsUsersAgence currentUser, YvsSocietes currentScte, YvsAgences currentAgence) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = isComptabilise(y.getId(), y.getVente().getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_CAISSE_AVOIR_VENTE : Constantes.SCR_CAISSE_VENTE);
                    if (comptabilise) {
                        if (msg) {

                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaCaisseVente(y.getId(), contenus, msg, currentUser, currentScte, currentAgence);
                    int idx = listePiece.indexOf(p);
                    if (idx > -1) {
                        listePiece.set(idx, p);
                    } else {
                        listePiece.add(0, p);
                    }
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);

                    }
                    return reponse;
                } else {

                }
            }
        }
        return false;
    }

    public List<Integer> decomposeSelection(String selection) {
        List<Integer> re = new ArrayList<>();
        List<String> l = new ArrayList<>();
        if (selection != null) {
            String numroLine[] = selection.split("-");
            try {
                int index;
                for (String numroLine1 : numroLine) {
                    index = Integer.parseInt(numroLine1);
                    re.add(index);
                    l.add(numroLine1);
                }
            } catch (NumberFormatException ex) {
                selection = "";
                for (String numroLine1 : numroLine) {
                    if (!l.contains(numroLine1)) {
                        selection += numroLine1 + "-";      //mémorise la selection
                    }
                }

            }
        }
        return re;
    }

    public YvsComptaCaissePieceVente getReglement() {
        return reglement;
    }

    public List<YvsComptaCaissePieceVente> getReglements() {
        return reglements;
    }

    private List<YvsComptaContentJournal> buildCaisseVenteToComptabilise(long id, boolean msg, YvsSocietes currentScte, YvsUsersAgence currentUser) {
        return buildContentJournal(id, Constantes.SCR_CAISSE_VENTE, msg, currentScte, currentUser);
    }

    private YvsComptaPiecesComptable majComptaCaisseVente(long id, List<YvsComptaContentJournal> contenus, boolean msg, YvsUsersAgence currentUser, YvsSocietes currentScte, YvsAgences currentAgence) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {

                }
                return null;
            }
            YvsComptaCaissePieceVente y = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getNotifs() != null ? y.getNotifs().getId() > 0 : false) {// Verification si le reglement est rattaché a un acompre
                    if (y.getNotifs().getAcompte() != null ? y.getNotifs().getAcompte().getId() > 0 : false) {
                        if (y.getNotifs().getAcompte().getNature().equals('D')) {// Verification si l'acompte est un dépot
                            boolean comptabilise = isComptabilise(y.getNotifs().getAcompte().getId(), Constantes.SCR_ACOMPTE_VENTE);
                            if (!comptabilise) {
                                if (msg) {

                                }
                                return null;
                            }
                            if (y.getNotifs().getAcompte().getPieceContenu() != null ? y.getNotifs().getAcompte().getPieceContenu().getId() > 0 : false) {
                                YvsComptaPiecesComptable p = y.getNotifs().getAcompte().getPieceContenu().getPiece();
                                YvsComptaContentJournalPieceVente c = new YvsComptaContentJournalPieceVente(y, p);
                                c.setAuthor(currentUser);
                                c.setId(null);
                                dao.save(c);
                                y.setComptabilise(true);
                                y.setDateUpdate(new Date());
                                y.setAuthor(currentUser);
                                dao.update(y);
                                return p;
                            }
                            return null;
                        }
                    }
                }
                boolean deja = false;
                if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                    List<YvsComptaPhasePiece> phases = dao.loadNameQueries("YvsComptaPhasePiece.findByPiece", new String[]{"piece"}, new Object[]{y});
                    if (phases != null ? !phases.isEmpty() : false) {
                        int correct = 0;
                        for (YvsComptaPhasePiece r : phases) {
                            comptabiliserPhaseCaisseVente(r, false);
                        }
                        deja = true;
                        if (correct == phases.size()) {
                            return new YvsComptaPiecesComptable(1L);
                        }
                    }
                }
                if (!deja) {
                    if ((y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_COMPENSATION)) ? true : (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false)) {
                        YvsComptaJournaux jrn = (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_COMPENSATION)) ? giveJournalVente(currentAgence) : y.getCaisse().getJournal();
                        YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDatePaiement(), jrn, contenus, msg, currentScte, currentUser);
                        if (p != null ? p.getId() < 1 : true) {
                            return p;
                        }
                        YvsComptaContentJournalPieceVente c = new YvsComptaContentJournalPieceVente(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);

                        //Debut du lettrage
                        lettrerCaisseVente(y, currentScte, currentUser);
                        y.setComptabilise(true);
                        //Comptabilisation des compensation
                        if (y.getCompensations() != null ? !y.getCompensations().isEmpty() : false) {
                            for (YvsComptaCaissePieceCompensation o : y.getCompensations()) {
                                comptabiliserCaisseAchat(o.getAchat(), false, false, currentScte, currentUser, currentAgence);
                            }
                        }
                        return p;
                    } else {
                        if (msg) {

                        }
                    }
                }
            }
        }
        return null;
    }

    public boolean unComptabiliserPhaseCaisseVente(YvsComptaPhasePiece y, boolean msg, boolean controle, boolean extourne, YvsSocietes currentScte) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {

            //l'étape suivante ne doit pas être validé
            int idx = y.getPieceVente().getPhasesReglement().indexOf(y);
            YvsComptaPhasePiece pSvt = null;
            if (idx >= 0 && (idx + 1) < y.getPieceVente().getPhasesReglement().size()) {
                pSvt = y.getPieceVente().getPhasesReglement().get(idx + 1);
            } else if (idx == (y.getPieceVente().getPhasesReglement().size() - 1) || idx == 0) {
                pSvt = y.getPieceVente().getPhasesReglement().get(idx);
            }
            if (pSvt != null ? (!pSvt.equals(y) && !extourne) : false) {
                if (isComptabilise(pSvt.getId(), Constantes.SCR_PHASE_CAISSE_VENTE)) {
                    if (msg) {

                    }
                    return false;
                }
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_PHASE_CAISSE_VENTE};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterneOrder";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (!extourne) {
                if (deletePieceComptable(list, msg, currentScte)) {
                    String query = "DELETE FROM yvs_compta_pieces_comptable WHERE id IN "
                            + "(SELECT DISTINCT p.piece FROM yvs_compta_content_journal_etape_piece_vente p LEFT JOIN yvs_compta_content_journal c ON p.piece = c.piece WHERE c.id IS NULL AND p.etape = ?)";
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                    y.setComptabilised(false);
                    y.setComptabilise(false);
                    y.setPieceContenu(null);

                    return true;
                }
            } else {
                if (list != null ? !list.isEmpty() : false) {
                    YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) dao.loadOneByNameQueries("YvsComptaPiecesComptable.findById", new String[]{"id"}, new Object[]{list.get(0).getId()});
//                    extournePiece(p, dateExtourne);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean unComptabiliserPhaseCaisseVente(YvsComptaPhasePiece y, boolean msg, boolean controle, YvsSocietes currentScte) {
        return unComptabiliserPhaseCaisseVente(y, true, true, false, currentScte);
    }

//    public boolean comptabiliserPhaseCaisseVente(YvsComptaPhasePiece y, boolean msg, boolean succes, YvsSocietes currentScte) {
//        return comptabiliserPhaseCaisseVente(y, msg, succes, false, currentScte);
//    }
    public void comptabiliserPhaseCaisseVente(YvsComptaPhasePiece pp, boolean comptabilise) {
        selectPhaseVente = pp;
        if (pp.getPieceVente().getVerouille()) {

            return;
        }
        if (pieceReglement.getPhasesReglement() != null ? !pieceReglement.getPhasesReglement().isEmpty() : false) {
            int idx = pieceReglement.getPhasesReglement().indexOf(pp);
            if (idx > -1) {
                if (idx == 0) {
                    if (comptabilise) {
                        comptabiliserPhaseCaisseVente(pp, true);
                    }
                } else {
                    YvsComptaPhasePiece prec = pieceReglement.getPhasesReglement().get(idx - 1);
                    if (dao.isComptabilise(prec.getId(), Constantes.SCR_PHASE_CAISSE_VENTE)) {
                        if (comptabilise) {
                            comptabiliserPhaseCaisseVente(pp, true);
                        }

                    }
                }
            }

        }
    }

    private YvsComptaJournaux giveJournalVente(YvsAgences currentAgence) {
        YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComParametreVente.findJournalByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        if (jrn != null ? jrn.getId() < 1 : true) {
            String[] champ = new String[]{"agence", "type", "default"};
            Object[] val = new Object[]{currentAgence, Constantes.VENTE, true};
            jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
        }
        return jrn;
    }

    private YvsComptaPiecesComptable saveNewPieceComptable(Date dateDoc, YvsComptaJournaux jrn, List<YvsComptaContentJournal> contenus, boolean msg, YvsSocietes currentScte, YvsUsersAgence currentUser) {
        try {
            if (contenus != null ? !contenus.isEmpty() : false) {
                if (jrn != null ? jrn.getId() < 1 : true) {
                    if (msg) {

                    }
                    return null;
                }
                dateDoc = dateDoc != null ? dateDoc : new Date();
                YvsBaseExercice exo = controleExercice(dateDoc, msg, currentScte);
                if (exo != null ? exo.getId() < 1 : true) {
                    return null;
                }
                String num = dao.genererReference(Constantes.TYPE_PIECE_COMPTABLE_NAME, dateDoc, jrn.getId(), currentScte, jrn.getAgence());
                if (num == null || num.trim().length() < 1) {
                    return null;
                }
                YvsComptaPiecesComptable p = new YvsComptaPiecesComptable();
                p.setAuthor(currentUser);
                p.setDatePiece(dateDoc);
                p.setDateSaisie(new Date());
                p.setExercice(exo);
                p.setJournal(jrn);
                p.setNumPiece(num);
                p.setStatutPiece(Constantes.STATUT_DOC_EDITABLE);
                p.setExtourne(false);
                p = (YvsComptaPiecesComptable) dao.save1(p);
                for (YvsComptaContentJournal c : contenus) {
                    List<YvsComptaContentAnalytique> lista = new ArrayList<>();
                    lista.addAll(c.getAnalytiques());
                    c.getAnalytiques().clear();

                    c.setId(null);
                    c.setAuthor(currentUser);
                    c.setPiece(p);
                    c = (YvsComptaContentJournal) dao.save1(c);

                    for (YvsComptaContentAnalytique a : lista) {
                        if (a.getCentre() != null ? a.getCentre().getId() > 0 : false) {
                            a.setId(null);
                            a.setContenu(c);
                            a = (YvsComptaContentAnalytique) dao.save1(a);
                            c.getAnalytiques().add(a);
                        }
                    }
                    p.getContentsPiece().add(c);
                }
                return p;
            }
        } catch (Exception ex) {

        }
        return null;
    }

    public List<YvsComptaContentJournal> lettrerCaisseVente(YvsComptaCaissePieceVente y, YvsSocietes currentScte, YvsUsersAgence currentUser) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        String isLettrer = null;
        List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceVente.findIdByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y.getVente(), Constantes.STATUT_DOC_PAYER});
        if (ids != null ? !ids.isEmpty() : false) {
            List<YvsComptaContentJournal> credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExternes", new String[]{"ids", "table"}, new Object[]{ids, y.getVente().getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_CAISSE_AVOIR_VENTE : Constantes.SCR_CAISSE_VENTE});
            if (credits != null ? !credits.isEmpty() : false) {
                if (asString(credits.get(0).getLettrage())) {
                    isLettrer = credits.get(0).getLettrage();
                } else {
                    List<YvsComptaContentJournal> debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExterne", new String[]{"id", "table"}, new Object[]{y.getVente().getId(), y.getVente().getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_AVOIR_VENTE : Constantes.SCR_VENTE});
                    if (!debits.isEmpty()) {
                        if (asString(debits.get(0).getLettrage())) {
                            isLettrer = debits.get(0).getLettrage();
                        } else {
                            List<YvsComptaContentJournal> list = new ArrayList<>();
                            list.addAll(debits);
                            list.addAll(credits);
                            if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                                lettrageCompte(list, currentUser, currentScte);
                            }
                        }
                    }
                }
            }
            if (asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentScte, isLettrer});
            }
        }
        return result;
    }

    public String lettrageCompte(List<YvsComptaContentJournal> contenus, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        String lettrage = "";
        try {
            if (contenus != null ? !contenus.isEmpty() : false) {
                lettrage = nextLettre(currentScte);
                for (YvsComptaContentJournal c : contenus) {
                    c.setLettrage(lettrage);
                    c.setAuthor(currentUser);
                    c.setDateUpdate(new Date());
                    dao.update(c);
                }
            }
        } catch (Exception ex) {

        }
        return lettrage;
    }

    private String nextLettre(YvsSocietes currentScte) {
        String value = "A";
        try {
            Integer length = (Integer) dao.loadObjectByNameQueries("YvsComptaContentJournal.findMaxLength", new String[]{"societe"}, new Object[]{currentScte});
            if (length != null ? length > 0 : false) {
                String lettre = (String) dao.loadObjectByNameQueries("YvsComptaContentJournal.findMaxLetter", new String[]{"societe", "length"}, new Object[]{currentScte, length});
                value = nextValue(lettre);
            }
        } catch (Exception ex) {

        }
        return value;
    }

    public static String nextValue(String oldValue) {
        // TODO code application logic here
        String newValue = "A";
        if (oldValue != null ? oldValue.trim().length() > 0 : false) {
            newValue = oldValue.toUpperCase();
            String temp = newValue;
            boolean continu = true;
            boolean increment = false;
            int length = newValue.length() - 1;//Recuperer la taille de la chaine entrée
            for (int i = length; i > -1; i--) {//Boucle inverse sur la taille
                char ch = newValue.charAt(i);//Recuperer le caracter a la position i
                int index = Constantes.ALPHABET.indexOf(ch);//Recupere l'index dans l'alphabet
                char rs = 'A';
                if (index == Constantes.ALPHABET.length() - 1) {//Verifie si on est a la dernier lettre de l'alphabet
                    increment = true;//On precise On va revenir a la lettre A
                } else {
                    rs = Constantes.ALPHABET.charAt(index + 1);//Recupere la lettre suivante
                    if (!continu) {//Verifie si on est passé a la lettre A
                        break;
                    }
                    if (increment) {//Definie si on est passé a la lettre A
                        continu = false;
                    }
                    index = Constantes.ALPHABET.indexOf(rs);//Recupere l'index dans l'alphabet
                    if (index != Constantes.ALPHABET.length() - 1) {//Verifie si on n'est pas a la dernier lettre de l'alphabet
                        continu = false;
                    }
                }
                newValue = newValue.substring(0, i) + String.valueOf(rs);//Change la lettre courante en sa nouvelle valeur
                if (i < length) {//Verifie si on n'est plus a la 1ere lettre a modifier
                    newValue += temp.substring(i + 1, temp.length());//Ajoute la suite des lettres a la nouvelle chaine
                }
                temp = newValue;//Sauvegarde la nouvelle valeur
            }
            increment = true;//Cas ou on a est passé a la lettre A pour toutes les lettres
            for (int i = 0; i < newValue.length(); i++) {
                if (newValue.charAt(i) != 'A') {
                    increment = false;
                    break;
                }
            }
            if (increment) {//On ajoute une nouvelle lettre A
                newValue += "A";
            }
        }
        return newValue;
    }

    public boolean comptabiliserCaisseAchat(YvsComptaCaissePieceAchat y, boolean msg, boolean succes, YvsSocietes currentScte, YvsUsersAgence currentUser, YvsAgences currentAgence) {
        if (y != null) {
            return comptabiliserCaisseAchat(y, buildCaisseAchatToComptabilise(y.getId(), msg, currentScte, currentUser), msg, succes, currentUser, currentScte, currentAgence);
        }
        return false;
    }

    private List<YvsComptaContentJournal> buildCaisseAchatToComptabilise(long id, boolean msg, YvsSocietes currentScte, YvsUsersAgence currentUser) {
        return buildContentJournal(id, Constantes.SCR_CAISSE_ACHAT, msg, currentScte, currentUser);
    }

    private boolean comptabiliserCaisseAchat(List<YvsComptaContentJournal> contenus, boolean msg, YvsUsersAgence currentUser, YvsSocietes currentScte, YvsAgences currentAgence) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;
        if (ids != null ? !ids.isEmpty() : false) {

            boolean error = false;
            List<YvsComptaContentJournal> list;
            for (int idx : ids) {
                YvsComptaCaissePieceAchat y = getPiecesCaisses().get(idx);
                list = new ArrayList<>();
                for (YvsComptaContentJournal c : contenus) {
                    if (c.getRefExterne().equals(y.getId())) {
                        list.add(c);
                    }
                }
                if (!list.isEmpty()) {
                    if (comptabiliserCaisseAchat(y, list, false, false, currentUser, currentScte, currentAgence)) {
                        succes = true;
                    } else {
                        error = true;
                    }
                }
            }
            docIds = "";
            if (error) {

            }

        } else {
            succes = comptabiliserCaisseAchat(selectCaisseAchat, contenus, msg, false, currentUser, currentScte, currentAgence);
        }
        return succes;
    }

    private boolean comptabiliserCaisseAchat(YvsComptaCaissePieceAchat y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes, YvsUsersAgence currentUser, YvsSocietes currentScte, YvsAgences currentAgence) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_CAISSE_ACHAT);
                    if (comptabilise) {
                        if (msg) {

                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaCaisseAchat(y.getId(), contenus, msg, currentUser, currentScte, currentAgence);
                    int idx = listePiece.indexOf(p);
                    if (idx > -1) {
                        listePiece.set(idx, p);
                    } else {
                        listePiece.add(0, p);
                    }
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);

                    }
                    return reponse;
                }
            }
        }
        return false;
    }

    private YvsComptaPiecesComptable majComptaCaisseAchat(long id, List<YvsComptaContentJournal> contenus, boolean msg, YvsUsersAgence currentUser, YvsSocietes currentScte, YvsAgences currentAgence) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {

                }
                return null;
            }
            YvsComptaCaissePieceAchat y = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getNotifs() != null ? y.getNotifs().getId() > 0 : false) {// Verification si le reglement est rattaché a un acompre
                    if (y.getNotifs().getAcompte() != null ? y.getNotifs().getAcompte().getId() > 0 : false) {
                        if (y.getNotifs().getAcompte().getNature().equals('D')) {// Verification si l'acompte est un dépot
                            boolean comptabilise = isComptabilise(y.getNotifs().getAcompte().getId(), Constantes.SCR_ACOMPTE_ACHAT);
                            if (!comptabilise) {
                                if (msg) {

                                }
                                return null;
                            }
                            if (y.getNotifs().getAcompte().getPieceContenu() != null ? y.getNotifs().getAcompte().getPieceContenu().getId() > 0 : false) {
                                YvsComptaPiecesComptable p = y.getNotifs().getAcompte().getPieceContenu().getPiece();
                                YvsComptaContentJournalPieceAchat c = new YvsComptaContentJournalPieceAchat(y, p);
                                c.setAuthor(currentUser);
                                c.setId(null);
                                dao.save(c);
                                y.setComptabilise(true);
                                return p;
                            }
                            return null;
                        }
                    }
                }
                boolean deja = false;
                if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                    List<YvsComptaPhasePieceAchat> phases = dao.loadNameQueries("YvsComptaPhasePieceAchat.findByPiece", new String[]{"piece"}, new Object[]{y});
                    if (phases != null ? !phases.isEmpty() : false) {
                        int correct = 0;
                        for (YvsComptaPhasePieceAchat r : phases) {
                            comptabiliserPhaseCaisseAchat(r, false, false, currentUser, currentScte);
                        }
                        deja = true;
                        if (correct == phases.size()) {
                            return new YvsComptaPiecesComptable(1L);
                        }
                    }
                }
                if (!deja) {
                    if ((y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_COMPENSATION)) ? true : (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false)) {
                        YvsComptaJournaux jrn = (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_COMPENSATION)) ? giveJournalAchat(currentAgence) : y.getCaisse().getJournal();
                        YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDatePaiement(), jrn, contenus, msg, currentScte, currentUser);
                        if (p != null ? p.getId() < 1 : true) {
                            return p;
                        }
                        YvsComptaContentJournalPieceAchat c = new YvsComptaContentJournalPieceAchat(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);

                        //Debut du lettrage
                        lettrerCaisseAchat(y, currentUser, currentScte);
                        y.setComptabilise(true);
                        //Comptabilisation des compensation
                        if (y.getCompensations() != null ? !y.getCompensations().isEmpty() : false) {
                            for (YvsComptaCaissePieceCompensation o : y.getCompensations()) {
                                comptabiliserCaisseVente(o.getVente());
                            }
                        }
                        return p;
                    } else {
                        if (msg) {

                        }
                    }
                }
            }
        }
        return null;
    }

    public boolean comptabiliserPhaseCaisseAchat(YvsComptaPhasePieceAchat y, boolean msg, boolean succes, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPhaseOk()) {
                boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_PHASE_CAISSE_ACHAT);
                if (comptabilise) {
                    if (msg) {

                    }
                    return false;
                }
                YvsComptaPiecesComptable p = majComptaEtapeCaisseAchat(y.getId(), msg, currentScte, currentUser);
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (reponse) {
                    y.setComptabilised(true);

                }
                return reponse;
            } else {
                if (msg) {

                }
            }
        }
        return false;
    }

    public List<YvsComptaContentJournal> lettrerCaisseAchat(YvsComptaCaissePieceAchat y, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        String isLettrer = null;
        List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceAchat.findIdByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y.getAchat(), Constantes.STATUT_DOC_PAYER});
        if (ids != null ? !ids.isEmpty() : false) {
            List<YvsComptaContentJournal> debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_ACHAT});
            if (debits != null ? !debits.isEmpty() : false) {
                if (asString(debits.get(0).getLettrage())) {
                    isLettrer = debits.get(0).getLettrage();
                } else {
                    List<YvsComptaContentJournal> credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{y.getAchat().getId(), Constantes.SCR_ACHAT});
                    if (!credits.isEmpty()) {
                        if (asString(credits.get(0).getLettrage())) {
                            isLettrer = credits.get(0).getLettrage();
                        } else {
                            List<YvsComptaContentJournal> list = new ArrayList<>();
                            list.addAll(debits);
                            list.addAll(credits);
                            if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                                lettrageCompte(list, currentUser, currentScte);
                            }
                        }
                    }
                }
            }
            if (asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentScte, isLettrer});
            }
        }
        return result;
    }

    private YvsComptaJournaux giveJournalAchat(YvsAgences currentAgence) {
        YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComParametreAchat.findJournalByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        if (jrn != null ? jrn.getId() < 1 : true) {
            String[] champ = new String[]{"agence", "type", "default"};
            Object[] val = new Object[]{currentAgence, Constantes.ACHAT, true};
            jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
        }
        return jrn;
    }

    private YvsComptaPiecesComptable majComptaEtapeCaisseAchat(long id, boolean msg, YvsSocietes currentScte, YvsUsersAgence currentUser) {
        return majComptaEtapeCaisseAchat(id, msg, currentScte, currentUser);
    }

    private List<YvsComptaContentJournal> buildEtapeCaisseAchatToComptabilise(long id, boolean msg, YvsSocietes currentScte, YvsUsersAgence currentUser) {
        return buildContentJournal(id, Constantes.SCR_PHASE_CAISSE_ACHAT, msg, currentScte, currentUser);
    }

//    private List<YvsComptaContentJournal> buildContentJournal(long id, String table, List<YvsAgences> agences, boolean action, boolean msg,YvsSocietes currentScte,YvsUsersAgence currentUser) {
//        List<YvsComptaContentJournal> list = new ArrayList<>();
//        dao.getEntityManager().clear();
//        String query = "select y.* from public.yvs_compta_content_journal(?,?,?,?,?) y";
//        String ids = "";
//        if (agences != null ? !agences.isEmpty() : false) {
//            ids = "0";
//            if (!action) {
//
//                for (YvsAgences a : listAgence) {
//                    if (!agences.contains(a)) {
//                        ids += "," + a.getId();
//                    }
//                }
//
//            } else {
//                for (YvsAgences a : agences) {
//                    ids += "," + a.getId();
//                }
//            }
//        }
//        Options[] param = new Options[]{new Options(currentScte.getId(), 1), new Options(ids, 2), new Options(id, 3), new Options(table, 4), new Options(true, 5)};
//        Query qr = dao.getEntityManager().createNativeQuery(query);
//        for (Options o : param) {
//            qr.setParameter(o.getPosition(), o.getValeur());
//        }
//        YvsComptaContentJournal line;
//        for (Object o : qr.getResultList()) {
//            line = convertTabToContenu((Object[]) o, id, msg,currentUser);
//            if (!controleContenu(line, msg)) {
//                return null;
//            }
//            int idx = list.indexOf(line);
//            if (idx > -1) {
//                list.get(idx).getAnalytiques().addAll(line.getAnalytiques());
//            } else {
//                list.add(line);
//            }
//        }
//        return list;
//    }
    private YvsComptaPiecesComptable majComptaEtapeCaisseAchat(long id, List<YvsComptaContentJournal> contenus, boolean msg, YvsSocietes currentScte, YvsUsersAgence currentUser) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {

                }
                return null;
            }
            YvsComptaPhasePieceAchat y = (YvsComptaPhasePieceAchat) dao.loadOneByNameQueries("YvsComptaPhasePieceAchat.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus, msg, currentScte, currentUser);
                    if (p != null ? p.getId() > 0 : false) {
                        //Recherche de la prochaine etape apres celle en cours
                        YvsComptaPhasePieceAchat n = (YvsComptaPhasePieceAchat) dao.loadOneByNameQueries("YvsComptaPhasePieceAchat.findNextByPiece", new String[]{"piece", "numero"}, new Object[]{y.getPieceAchat(), y.getPhaseReg().getNumeroPhase()});
                        if (n != null ? n.getId() < 1 : true) {
                            YvsComptaContentJournalPieceAchat c = new YvsComptaContentJournalPieceAchat(new YvsComptaCaissePieceAchat(y.getPieceAchat().getId()), p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);
                        }
                        YvsComptaContentJournalEtapePieceAchat c = new YvsComptaContentJournalEtapePieceAchat(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                        y.setComptabilise(true);
                    }
                    return p;
                } else {
                    if (msg) {

                    }
                }
            }
        }
        return null;
    }

    public List<YvsComptaCaissePieceAchat> getPiecesCaisses() {
        return piecesCaisses;
    }

    public List<YvsComDocVentes> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComDocVentes> documents) {
        this.documents = documents;
    }

    public String genererReference(String element, Date date, YvsSocietes currentScte, YvsAgences currentAgence, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint) {
        return genererReference(element, date, 0, currentScte, currentAgence, null, currentDepot, currentPoint);
    }

    public String genererReference(String element, Date date, long id, String type, YvsSocietes currentScte, YvsAgences currentAgence, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint) {
        return genererReference(element, date, id, currentScte, currentAgence, null, currentDepot, currentPoint);
    }

    public String genererReference(String element, Date date, String code, YvsSocietes currentScte, YvsAgences currentAgence, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint) {
        return genererReference(element, date, 0, currentScte, currentAgence, null, currentDepot, currentPoint);
    }

    public String genererReference(String element, Date date, long id, YvsSocietes currentScte, YvsAgences currentAgence, YvsMutMutuelle currentMutuelle, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint) {
        YvsBaseModeleReference model = rechercheModeleReference(element, currentScte);
        if ((model != null) ? model.getId() > 0 : false) {
            return getReferenceElement(model, date, id, currentScte, currentAgence, currentMutuelle, currentDepot, currentPoint);
        } else {

            return "";
        }
    }

    public String genererPrefixeComplet(YvsBaseModeleReference modele, Date date, long id, YvsSocietes currentScte, YvsAgences currentAgence, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint) {
        String prefixe = genererPrefixe(modele, id, currentScte, currentAgence, currentDepot, currentPoint);
        if (prefixe != null ? prefixe.trim().length() > 0 : false) {
            Calendar cal = dateToCalendar(date);
            if (modele.getJour()) {
                if (cal.get(Calendar.DATE) > 9) {
                    prefixe += Integer.toString(cal.get(Calendar.DATE));
                }
                if (cal.get(Calendar.DATE) < 10) {
                    prefixe += ("0" + Integer.toString(cal.get(Calendar.DATE)));
                }
            }
            if (modele.getMois()) {
                if (cal.get(Calendar.MONTH) + 1 > 9) {
                    prefixe += Integer.toString(cal.get(Calendar.MONTH) + 1);
                }
                if (cal.get(Calendar.MONTH) + 1 < 10) {
                    prefixe += ("0" + Integer.toString(cal.get(Calendar.MONTH) + 1));
                }
            }
            if (modele.getAnnee()) {
                prefixe += Integer.toString(cal.get(Calendar.YEAR)).substring(2);
            }
            prefixe += modele.getSeparateur();
        }
        return prefixe != null ? prefixe : "";
    }

    private String getReferenceElement(YvsBaseModeleReference modele, Date date, long id, YvsSocietes currentScte, YvsAgences currentAgence, YvsMutMutuelle currentMutuel, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint) {
        String motRefTable = "";
        String inter = genererPrefixeComplet(modele, date, id, currentScte, currentAgence, currentDepot, currentPoint);
        switch (modele.getElement().getDesignation()) {
            case "Employe": {
                break;
            }
            case Constantes.TYPE_BP_NAME: {
                String[] ch = new String[]{"reference", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsComptaBonProvisoire.findByReference";
                List<YvsComptaBonProvisoire> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumero();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_FiA_NAME: {
                String[] ch = new String[]{"reference", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsComFicheApprovisionnement.findByReference";
                List<YvsComFicheApprovisionnement> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getReference();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_BLA_NAME:
            case Constantes.TYPE_BRA_NAME:
            case Constantes.TYPE_BAA_NAME:
            case Constantes.TYPE_BCA_NAME:
            case Constantes.TYPE_FRA_NAME:
            case Constantes.TYPE_FAA_NAME:
            case Constantes.TYPE_FA_NAME: {
                String[] ch = new String[]{"numRefDoc", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsComDocAchats.findByReference";
                List<YvsComDocAchats> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumDoc();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_BLV_NAME:
            case Constantes.TYPE_BRV_NAME:
            case Constantes.TYPE_BAV_NAME:
            case Constantes.TYPE_BCV_NAME:
            case Constantes.TYPE_FRV_NAME:
            case Constantes.TYPE_FAV_NAME:
            case Constantes.TYPE_FV_NAME: {
                String[] ch = new String[]{"numDoc", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsComDocVentes.findByReference";
                List<YvsComDocVentes> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumDoc();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_FT_NAME:
            case Constantes.TYPE_SS_NAME:
            case Constantes.TYPE_ES_NAME:
            case Constantes.TYPE_IN_NAME:
            case Constantes.TYPE_RE_NAME:
            case Constantes.TYPE_OT_NAME: {
                String[] ch = new String[]{"numDoc", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsComDocStocks.findByReference";
                List<YvsComDocStocks> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumDoc();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_RS_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsComReservationStock.findByReference";
                List<YvsComReservationStock> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumReference();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_RA_NAME: {
                String[] ch = new String[]{"numDoc", "agence"};
                Object[] v = new Object[]{inter + "%", currentAgence};
                String query = "YvsComDocRation.findByNumDocL";
                List<YvsComDocRation> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumDoc();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_OD_NAME: {
                String[] ch = new String[]{"numPiece", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsComptaCaisseDocDivers.findByNumPieceLike_";
                String ref = (String) dao.loadObjectByNameQueries(query, ch, v);
                motRefTable = ref != null ? ref : "";
                break;
            }
            case Constantes.TYPE_OD_RECETTE_NAME: {
                String[] ch = new String[]{"numPiece", "societe", "mouvement"};
                Object[] v = new Object[]{inter + "%", currentScte, "R"};
                String query = "YvsComptaCaisseDocDivers.findByNumPieceLikeMouv";
                String ref = (String) dao.loadObjectByNameQueries(query, ch, v);
                motRefTable = ref != null ? ref : "";
                break;
            }
            case Constantes.TYPE_OD_DEPENSE_NAME: {
                String[] ch = new String[]{"numPiece", "societe", "mouvement"};
                Object[] v = new Object[]{inter + "%", currentScte, "D"};
                String query = "YvsComptaCaisseDocDivers.findByNumPieceLikeMouv";
                String ref = (String) dao.loadObjectByNameQueries(query, ch, v);
                motRefTable = ref != null ? ref : "";
                break;
            }
            case Constantes.TYPE_PT_NAME: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsComptaCaissePieceVirement.findByNumeroPiece";
                List<YvsComptaCaissePieceVirement> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsComptaMouvementCaisse.findByNumeroPiece";
                List<YvsComptaMouvementCaisse> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumero();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_ACHAT_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsComptaCaissePieceAchat.findByNumPiece";
                List<YvsComptaCaissePieceAchat> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_VENTE_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsComptaCaissePieceVente.findByNumPiece";
                List<YvsComptaCaissePieceVente> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_DIVERS_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsComptaCaissePieceDivers.findByNumPiece";
                List<YvsComptaCaissePieceDivers> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_MISSION_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsComptaCaissePieceMission.findByNumPiece";
                List<YvsComptaCaissePieceMission> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_SALAIRE_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsComptaCaissePieceSalaire.findByNumPiece";
                List<YvsComptaCaissePieceSalaire> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PT_AVANCE_VENTE: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsComptaAcompteClient.findByNumPiece";
                List<YvsComptaAcompteClient> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumRefrence();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PT_AVANCE_ACHAT: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsComptaAcompteFournisseur.findByNumPiece";
                List<YvsComptaAcompteFournisseur> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumRefrence();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PIECE_COMPTABLE_NAME: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsComptaPiecesComptable.findByNumPiece";
                List<YvsComptaPiecesComptable> l = dao.loadNameQueries(query, ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_DOC_MISSION_NAME:
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", currentScte};
                String query = "YvsGrhMissions.findByNumPiece";
                List<YvsGrhMissions> lm = dao.loadNameQueries(query, ch, v);
                if ((lm != null) ? !lm.isEmpty() : false) {
                    motRefTable = lm.get(0).getNumeroMission();
                } else {
                    motRefTable = "";
                }
                break;
            case Constantes.MUT_TRANSACTIONS_MUT: {
                ch = new String[]{"numeroPiece", "mutuelle"};
                v = new Object[]{inter + "%", currentMutuel};
                query = "YvsMutOperationCompte.findByNumOp";
                List<YvsMutOperationCompte> lop = dao.loadNameQueries(query, ch, v);
                if ((lop != null) ? !lop.isEmpty() : false) {
                    motRefTable = lop.get(0).getReferenceOperation();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.MUT_ACTIVITE_CREDIT: {
                ch = new String[]{"numeroPiece", "mutuelle"};
                v = new Object[]{inter + "%", currentMutuel};
                query = "YvsMutCredit.findByNumOp";
                List<YvsMutCredit> lop = dao.loadNameQueries(query, ch, v);
                if ((lop != null) ? !lop.isEmpty() : false) {
                    motRefTable = lop.get(0).getReference();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.PROD_TYPE_PROD_NAME: {
                ch = new String[]{"codeRef", "societe"};
                v = new Object[]{inter + "%", currentScte};
                query = "YvsProdOrdreFabrication.findByReference";
                List<YvsProdOrdreFabrication> lop = dao.loadNameQueries(query, ch, v);
                if ((lop != null) ? !lop.isEmpty() : false) {
                    motRefTable = lop.get(0).getCodeRef();
                } else {
                    motRefTable = "";
                }
                break;
            }
            default: {
                break;
            }
        }
        String partieNum = motRefTable.replaceFirst(inter, "");
        if (partieNum != null ? partieNum.trim().length() > 0 : false) {
            int num = Integer.valueOf(partieNum.trim().replace("°", ""));
            if (Integer.toString(num + 1).length() > modele.getTaille()) {

                return "";
            } else {
                for (int i = 0; i < (modele.getTaille() - Integer.toString(num + 1).length()); i++) {
                    inter += "0";
                }
            }
            inter += Long.toString(Long.valueOf(partieNum.trim().replace("°", "")) + 1);
        } else {
            for (int i = 0; i < modele.getTaille() - 1; i++) {
                inter += "0";
            }
            inter += "1";
        }
        return inter;
    }

    public String genererPrefixe(String element, long id, YvsSocietes currentScte, YvsAgences currentAgence, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint) {
        YvsBaseModeleReference modele = rechercheModeleReference(element, currentScte);
        if ((modele != null) ? modele.getId() > 0 : false) {
            return genererPrefixe(modele, id, currentScte, currentAgence, currentDepot, currentPoint);
        } else {

            return "";
        }
    }

    private YvsBaseModeleReference rechercheModeleReference(String mot, YvsSocietes currentScte) {
        if (!mot.toLowerCase().equals("")) {
            String[] ch = new String[]{"designation", "societe"};
            Object[] v = new Object[]{mot, currentScte};
            String query = "YvsBaseModeleReference.findByElement";
            List<YvsBaseModeleReference> l = dao.loadNameQueries(query, ch, v);
            if ((l != null) ? !l.isEmpty() : false) {
                return l.get(0);
            } else {
                if (mot.equals(Constantes.TYPE_OD_RECETTE_NAME) || mot.equals(Constantes.TYPE_OD_DEPENSE_NAME)) {
                    return rechercheModeleReference(Constantes.TYPE_OD_NAME, currentScte);
                } else if (mot.equals(Constantes.TYPE_PC_ACHAT_NAME)
                        || mot.equals(Constantes.TYPE_PC_VENTE_NAME)
                        || mot.equals(Constantes.TYPE_PC_DIVERS_NAME)
                        || mot.equals(Constantes.TYPE_PC_MISSION_NAME)
                        || mot.equals(Constantes.TYPE_PC_SALAIRE_NAME)) {
                    return rechercheModeleReference(Constantes.TYPE_PC_NAME, currentScte);
                }
            }
        }
        return null;
    }

    public String genererPrefixe(YvsBaseModeleReference modele, long id, YvsSocietes currentScte, YvsAgences currentAgence, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint) {
        String inter = modele.getPrefix();
        String code = null;
        if (modele.getCodePoint()) {
            code = genererPrefixeCodeElement(modele, id, currentScte, currentAgence, currentDepot, currentPoint);
        }
        if (code != null ? code.trim().length() > 0 : false) {
            inter += modele.getSeparateur() + code;
        }
        inter += modele.getSeparateur();
        return inter != null ? inter : "";
    }

    public String genererPrefixeCodeElement(YvsBaseModeleReference modele, long id, YvsSocietes currentScte, YvsAgences currentAgence, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint) {
        if (modele.getCodePoint()) {
            String code = "";
            switch (modele.getElementCode()) {
                case Constantes.SOCIETE: {
                    if (currentScte != null ? currentScte.getCodeAbreviation().trim().length() > 0 : false) {
                        code = currentScte.getCodeAbreviation();
                    }
                    break;
                }
                case Constantes.AGENCE: {
                    if (currentAgence != null ? currentAgence.getAbbreviation().trim().length() > 0 : false) {
                        code = currentAgence.getAbbreviation();
                    }
                    break;
                }
                case Constantes.AUTRE: {
                    switch (modele.getElement().getDesignation()) {
                        case Constantes.TYPE_FA_NAME:
                        case Constantes.TYPE_BLA_NAME:
                        case Constantes.TYPE_BLV_NAME:
                        case Constantes.PROD_TYPE_PROD_NAME:
                        case Constantes.TYPE_RA_NAME:
                        case Constantes.TYPE_RS_NAME:
                        case Constantes.TYPE_FiA_NAME:
                        case Constantes.TYPE_FT_NAME:
                        case Constantes.TYPE_SS_NAME:
                        case Constantes.TYPE_ES_NAME:
                        case Constantes.TYPE_IN_NAME:
                        case Constantes.TYPE_RE_NAME:
                        case Constantes.TYPE_OT_NAME:
                        case Constantes.TYPE_BRV_NAME:
                        case Constantes.TYPE_BAV_NAME:
                        case Constantes.TYPE_BCV_NAME:
                        case Constantes.TYPE_BRA_NAME:
                        case Constantes.TYPE_BAA_NAME:
                        case Constantes.TYPE_BCA_NAME:
                            YvsBaseDepots d = null;
                            if (id > 0) {
                                d = (YvsBaseDepots) dao.loadObjectByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{id});
                            }
                            if (d != null ? d.getId() > 0 : false) {
                                code = d.getAbbreviation();
                            } else {
                                if (currentDepot != null ? currentDepot.getAbbreviation() != null : false) {
                                    code = currentDepot.getAbbreviation();
                                }
                            }
                            break;
                        case Constantes.TYPE_FV_NAME:
                            YvsBasePointVente p = null;
                            if (id > 0) {
                                p = (YvsBasePointVente) dao.loadObjectByNameQueries("YvsBasePointVente.findById", new String[]{"id"}, new Object[]{id});
                            }
                            if (p != null ? p.getId() > 0 : false) {
                                code = p.getCode();
                            } else {
                                if (currentPoint != null ? currentPoint.getCode() != null : false) {
                                    code = currentPoint.getCode();
                                }
                            }
                            break;
                        case Constantes.TYPE_OD_NAME:
                        case Constantes.TYPE_PT_NAME:
                        case Constantes.TYPE_BP_NAME:
                        case Constantes.TYPE_PC_NAME:
                        case Constantes.TYPE_PC_ACHAT_NAME:
                        case Constantes.TYPE_PC_VENTE_NAME:
                        case Constantes.TYPE_PC_DIVERS_NAME:
                        case Constantes.TYPE_PC_MISSION_NAME:
                        case Constantes.TYPE_PC_SALAIRE_NAME:
                        case Constantes.TYPE_DOC_MISSION_NAME:
                        case Constantes.TYPE_PT_AVANCE_ACHAT:
                        case Constantes.TYPE_PT_AVANCE_VENTE:
                            YvsBaseCaisse c = null;
                            if (id > 0) {
                                c = (YvsBaseCaisse) dao.loadObjectByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{id});
                            }
                            if (c != null ? c.getId() > 0 : false) {
                                code = c.getCode();
                            }
                            break;
                        case Constantes.MUT_ACTIVITE_CREDIT:
                        case Constantes.MUT_TRANSACTIONS_MUT:
                            YvsMutCaisse en = null;
                            if (id > 0) {
                                en = (YvsMutCaisse) dao.loadObjectByNameQueries("YvsMutCaisse.findById", new String[]{"id"}, new Object[]{id});
                            }
                            if (en != null ? en.getId() > 0 : false) {
                                code = en.getReferenceCaisse();
                            }
                            break;
                        case Constantes.TYPE_PIECE_COMPTABLE_NAME:
                        case Constantes.TYPE_FRV_NAME:
                        case Constantes.TYPE_FAV_NAME:
                        case Constantes.TYPE_FRA_NAME:
                        case Constantes.TYPE_FAA_NAME:
                            YvsComptaJournaux j = null;
                            if (id > 0) {
                                j = (YvsComptaJournaux) dao.loadObjectByNameQueries("YvsComptaJournaux.findById", new String[]{"id"}, new Object[]{id});
                            }
                            if (j != null ? j.getId() > 0 : false) {
                                code = j.getCodeJournal();
                            }
                            break;
                    }
                    break;
                }
                default: {
                    break;
                }
            }
            if (code != null) {
                if (code.length() > modele.getLongueurCodePoint()) {
                    modele.setCode(code.substring(0, modele.getLongueurCodePoint()));
                } else {
                    modele.setCode(code);
                }
            }
        }
        return modele.getCode();
    }

    public List<YvsWorkflowValidFactureVente> saveEtapesValidation(YvsComDocVentes m, List<YvsWorkflowEtapeValidation> model) {
        //charge les étape de vailidation
        List<YvsWorkflowValidFactureVente> re = new ArrayList<>();
        if (model != null ? !model.isEmpty() : false) {
            YvsWorkflowValidFactureVente vm;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    YvsWorkflowValidFactureVente w = (YvsWorkflowValidFactureVente) dao.loadOneByNameQueries("YvsWorkflowValidFactureVente.findByEtapeFacture", new String[]{"facture", "etape"}, new Object[]{m, et});
                    if (w != null ? w.getId() < 1 : true) {
                        vm = new YvsWorkflowValidFactureVente();
                        vm.setAuthor(new YvsUsersAgence(m.getAuthor().getId()));
                        vm.setEtape(et);
                        vm.setEtapeValid(false);
                        vm.setFactureVente(new YvsComDocVentes(m.getId()));
                        vm.setDateSave(new Date());
                        vm.setDateUpdate(new Date());
                        vm.setId(null);
                        IYvsWorkflowValidFactureVente impl = (IYvsWorkflowValidFactureVente) IEntitiSax.createInstance("IYvsWorkflowValidFactureVente", dao);
                        ResultatAction<YvsWorkflowValidFactureVente> wo = impl.save(vm);
                        vm = (YvsWorkflowValidFactureVente) wo.getData();
                        re.add(vm);
                    }
                }
            }
        }

        return ordonneEtapes(re);
    }

    private List<YvsWorkflowValidFactureVente> ordonneEtapes(List<YvsWorkflowValidFactureVente> l) {
        List<YvsWorkflowValidFactureVente> re = new ArrayList<>();
        YvsWorkflowValidFactureVente first = null;
        //recherche la première étape      

        for (YvsWorkflowValidFactureVente vm : l) {
            if (vm.getEtape().getFirstEtape()) {
                first = vm;
                break;
            }
        }
        if (first != null) {
            re.add(first);  //ajoute la première étapes au résultat
            boolean find;
            //tant qu'il existe une étape suivante active
            while ((first.getEtape().getEtapeSuivante() != null) ? (first.getEtape().getEtapeSuivante().getActif()) : false) {
                find = false;
                for (YvsWorkflowValidFactureVente vm : l) {
                    if (first.getEtape().getEtapeSuivante().equals(vm.getEtape())) {
                        re.add(vm); //Ajoute l'étape
                        first = vm;
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    break;
                }
            }
        }
        int i = 0;
        //toutes les étapes ont été construite
        if (re.size() > 0) {
            first = re.get(0);
            //si la première étape n'est pas validé, on l'active
            if (!first.getEtapeValid()) {
                re.get(0).setEtapeActive(true);
            }
        }
        for (YvsWorkflowValidFactureVente vm : re) {
            if (first.getEtape().getEtapeSuivante() != null && vm.getEtapeValid()) {
                i++;
                if (re.size() > i) {
                    //active l'etape suivante
                    re.get(i).setEtapeActive(true);
                    first = re.get(i);
                }
            }
            if (vm.getEtape().equals(re.get(re.size() - 1).getEtape()) && !vm.getEtapeValid()) {
                if ((re.size() - 2) >= 0) {
                    if (re.get(re.size() - 2).getEtapeValid()) {
                        vm.setEtapeActive(true);
                    }
                }
            }
        }
        return re;
    }

    public boolean asDroitValideEtape(YvsWorkflowEtapeValidation etape, YvsUsers users) {
        return asDroitValideEtape(etape.getAutorisations(), users.getNiveauAcces());
    }

    public boolean asDroitValideEtape(List<YvsWorkflowAutorisationValidDoc> lau, YvsNiveauAcces n) {
        if (lau != null ? !lau.isEmpty() : false) {
            for (YvsWorkflowAutorisationValidDoc au : lau) {
                if (au.getNiveauAcces().equals(n)) {
                    return au.getCanValide();
                }
            }
            return false;
        }
        return true;
    }

    public ResultatAction<YvsWorkflowValidFactureVente> validEtapeFacture(YvsComDocVentes current, YvsUsersAgence users, YvsWorkflowValidFactureVente etape) {
        return validEtapeFacture(current, users, etape, dao);
    }

    public ResultatAction<YvsWorkflowValidFactureVente> validEtapeFacture(YvsComDocVentes current, YvsUsersAgence users, YvsWorkflowValidFactureVente etape, DaoInterfaceLocal dao) {
        List<YvsWorkflowValidFactureVente> list = new ArrayList<>();
        if (etape != null) {
            //contrôle la cohérence des dates
            if (current != null ? (current.getId() != null ? current.getId() > 0 : true) : true) {
                current = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{current.getId()});
            }
            etape.setFactureVente(new YvsComDocVentes(current.getId()));
            list = dao.loadNameQueries("YvsWorkflowValidFactureVente.findByFacture", new String[]{"facture"}, new Object[]{current});
            list = ordonneEtapes(list);

            int idx = list.indexOf(etape);
            if (idx >= 0) {
                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() == null) {
                    YvsUsersAgence author = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findById", new String[]{"id"}, new Object[]{users.getId()});
                    YvsComEnteteDocVente entet = (YvsComEnteteDocVente) dao.loadOneByNameQueries("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{current.getEnteteDoc().getId()});
                    ResultatAction<YvsComDocVentes> result = validerFacture(current, users, author.getAgence().getSociete(), users.getAgence(), entet.getCreneau().getCreneauDepot().getDepot(), entet.getCreneau().getCreneauPoint().getPoint());
                    if (result.isResult()) {
                        etape.setAuthor(users);
                        etape.setEtapeValid(true);
                        etape.setMotif(null);
                        etape.setEtapeActive(false);
                        etape.setDateUpdate(new Date());
                        if (list.size() > (idx + 1)) {
                            list.get(idx + 1).setEtapeActive(true);
                        }
                        dao.update(etape);
                        if (documents != null ? documents.contains(current) : false) {
                            int idx_ = documents.indexOf(current);
                            documents.get(idx_).setEtapeValide(current.getEtapeValide());
                        }
                        return new ResultatAction(true, etape, etape.getId(), "Succes");
                    }
                } else {
                    etape.setAuthor(users);
                    etape.setEtapeValid(true);
                    etape.setEtapeActive(false);
                    etape.setMotif(null);
                    etape.setDateUpdate(new Date());
                    if (list.size() > (idx + 1)) {
                        list.get(idx + 1).setEtapeActive(true);
                    }
                    dao.update(etape);

                    current.setStatut(Constantes.ETAT_ENCOURS);
                    current.setAuthor(users);
                    current.setDateUpdate(new Date());
                    current.setEtapeValide(current.getEtapeValide() + 1);
                    dao.update(current);
                    if (documents != null ? documents.contains(current) : false) {
                        int idx_ = documents.indexOf(current);
                        documents.get(idx_).setEtapeValide(current.getEtapeValide());
                        documents.get(idx_).setStatut(current.getStatut());
                    }
                    return new ResultatAction(true, etape, etape.getId(), "Succes");
                }
            }
        }
        return new ResultatAction<>(false, etape, 0L, "Action impossible ");
    }

    public ResultatAction<YvsComDocVentes> annulerOrder(YvsComDocVentes entity) {
        return annulerOrder(entity, false, true, true);
    }

    public ResultatAction<YvsComDocVentes> annulerOrder(YvsComDocVentes entity, boolean load, boolean msg, boolean open) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                if (dao.isComptabilise(entity.getId(), Constantes.SCR_VENTE)) {
                    if (!autoriser("compta_od_annul_comptabilite")) {
                        return new ResultatAction<>(false, null, 0L, "Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                    }
                }
                List<YvsComDocVentes> l = dao.loadNameQueries("YvsComDocVentes.findByParent", new String[]{"documentLie"}, new Object[]{entity});

                List<YvsComDocVentes> list = new ArrayList<>();
                list.addAll(l);
                for (YvsComDocVentes d : list) {
                    dao.delete(d);
                    l.remove(d);
                }
                YvsComDocVentes livraison = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{entity.getId()});
                entity.setEnteteDoc(livraison.getEnteteDoc());

                //verifier la cohérence avec les inventaires
                if (!controleInventaire(livraison.getDepotLivrer().getId(), livraison.getEnteteDoc().getDateEntete(), livraison.getTrancheLivrer().getId())) {
                    return new ResultatAction<>(false, null, 0L, "Incoherence avec les inventaires");
                }
                if (l != null ? l.isEmpty() : true) {
                    dao.delete(entity);
                    livraison.getDocuments().clear();
                    if (livraison != null) {
                        dao.getEquilibreVente(livraison.getDocumentLie().getId());
                    }
                    return new ResultatAction<>(true, new YvsComDocVentes(entity.getId()), entity.getId(), "Succès");
//                    }
                } else {
                    for (YvsComDocVentes d : l) {
                        if (!d.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                            return new ResultatAction<>(false, null, 0L, "Impossible d'annuler cet ordre car il possède un transfert déja valide");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResultatAction<>(false, null, 0L, "Operation Impossible");

        }
        return null;
    }

    public List<YvsWorkflowEtapeValidation> getAllEtapeValidation() {
        return getAllEtapeValidation(currentAgence.getSociete());
    }

    public List<YvsWorkflowEtapeValidation> getAllEtapeValidation(YvsSocietes societe) {
        champ = new String[]{"titre", "societe"};
        val = new Object[]{Constantes.DOCUMENT_FACTURE_VENTE, societe};
        return dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", champ, val);
    }

    /**
     * GERER LES BONS DE COMMANDES
     *
     *
     * @param selectDoc
     * @param currentUser
     * @param contents
     * @return
     */
    public ResultatAction validerOrder(YvsComDocVentes selectDoc, YvsUsersAgence currentUser, List<YvsComContenuDocVente> contents) {
        YvsComDocVentes y = null;
        try {
            if (selectDoc == null) {
                return new ResultatAction<>(false, null, 0L, "Vous devez selectionner la facture");
            }
            if (selectDoc.getClient() != null ? selectDoc.getClient().getId() < 1 : true) {
                return new ResultatAction<>(false, null, 0L, "Aucun client n'a été trouvé !");
            }
            //vérifier la tranche
            if (selectDoc.getTrancheLivrer().getId() <= 0) {
                return new ResultatAction<>(false, null, 0L, "Vous devez selectionner la tranche de livraison !");
            }
            if (selectDoc.getOnfacture().equals(Constantes.ETAT_VALIDE)) {
                for (YvsComDocVentes d : selectDoc.getDocuments()) {
                    if (d.getStatut().equals(Constantes.ETAT_VALIDE)) {
                        y = d;
                        break;
                    }
                }
            }
            if (y != null ? y.getId() < 1 : true) {
                if (selectDoc.getOnfacture().equals(Constantes.ETAT_ENCOURS)) {
                    for (YvsComDocVentes d : selectDoc.getDocuments()) {
                        y = d;
                        break;
                    }
                }
            }
            if (y != null ? y.getId() < 1 : true) {
                if (selectDoc.getCloturer()) {
                    return new ResultatAction<>(false, null, 0L, "Ce document est vérouillé");
                }
                List<YvsComContenuDocVente> contenus = new ArrayList<>(contents);
                if (contenus != null ? contenus.isEmpty() : true) {
                    return new ResultatAction<>(false, null, 0L, "Le contenu ne peut pas etre vide");
                }
                if (selectDoc.getEnteteDoc() != null ? selectDoc.getEnteteDoc().getId() < 1 : true) {
                    return new ResultatAction<>(false, null, 0L, "Aucun journal de vente trouvé pour cette commande");
                }
                String num = dao.genererReference(Constantes.TYPE_FV_NAME, selectDoc.getEnteteDoc().getDateEntete(), selectDoc.getEnteteDoc().getCreneau().getCreneauPoint().getPoint().getId(), currentAgence.getSociete(), currentAgence);
                if (num != null ? num.trim().length() < 1 : true) {
                    return new ResultatAction<>(false, null, 0L, "Le numéro de référence de la facture n'as pas pu être généré !");
                }
                List<YvsWorkflowEtapeValidation> etapes = null;

                y = new YvsComDocVentes(null, selectDoc);
                y.setEnteteDoc(selectDoc.getEnteteDoc());
                y.setDateSave(new Date());
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                y.setTypeDoc(Constantes.TYPE_FV);
                y.setNumDoc(num);
                if (selectDoc.getEnteteDoc().getCreneau().getCreneauDepot().getDepot() != null ? selectDoc.getEnteteDoc().getCreneau().getCreneauDepot().getDepot().getId() > 0 : false) {
                    y.setDepotLivrer(new YvsBaseDepots(selectDoc.getEnteteDoc().getCreneau().getCreneauDepot().getDepot().getId()));
                }
                if (selectDoc.getTrancheLivrer() != null ? selectDoc.getTrancheLivrer().getId() > 0 : false) {
                    y.setTrancheLivrer(new YvsGrhTrancheHoraire(selectDoc.getTrancheLivrer().getId()));
                }
                y.setLivreur(currentUser.getUsers());
                y.setDateLivraison(new Date());
                y.setDocumentLie(new YvsComDocVentes(selectDoc.getId()));
                y.setHeureDoc(new Date());
                y.setStatut(Constantes.ETAT_VALIDE);
                y.setStatutLivre(Constantes.ETAT_ATTENTE);
                y.setStatutRegle(selectDoc.getStatutRegle());
                y.setValiderBy(currentUser.getUsers());
                y.setDateValider(new Date());
                y.setDescription("Facturation de la commande N° " + selectDoc.getNumDoc() + " le " + ldf.format(new Date()) + " à " + time.format(y.getHeureDoc()));

//                etapes = getAllEtapeValidation(currentUser.getAgence().getSociete());
                y.setEtapeTotal(etapes != null ? etapes.size() : 0);

                y.getContenus().clear();
                y.setId(null);
                y = (YvsComDocVentes) dao.save1(y);
                if (y != null ? y.getId() < 1 : true) {
                    return new ResultatAction<>(false, null, 0L, "Impossible de créer la facture de vente");
                }
                YvsComContenuDocVente c;
                int lenght = contenus.size();
                for (int i = 0; i < lenght; i++) {
                    c = new YvsComContenuDocVente(null, contenus.get(i));
                    c.setDocVente(y);
                    c.setStatut(Constantes.ETAT_VALIDE);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    c = (YvsComContenuDocVente) dao.save1(c);
                    if (!y.getContenus().contains(c)) {
                        y.getContenus().add(c);
                    }
                }
                YvsComptaCaissePieceVente p;
                lenght = selectDoc.getReglements().size();
                for (int i = 0; i < lenght; i++) {
                    p = selectDoc.getReglements().get(i);
                    if (p.getMouvement().equals(Constantes.MOUV_CAISS_SORTIE.charAt(0))) {
                        dao.delete(p);
                        selectDoc.getReglements().remove(p);
                    } else {
                        p.setValideBy(currentUser.getUsers());
                        p.setVente(y);
                        p.setAuthor(currentUser);
                        dao.update(p);
                        if (!y.getReglements().contains(p)) {
                            y.getReglements().add(p);
                        }
                    }
                }
                if (etapes != null) {
                    saveEtapesValidation(y, etapes);
                }
                selectDoc.getDocuments().add(y);
                int idx = selectDoc.getDocuments().indexOf(y);
                if (idx < 0) {
                    selectDoc.getDocuments().add(0, y);
                }
            }
            if (y != null ? y.getId() > 0 : false) {
                selectDoc.setCloturer(false);
                selectDoc.setAnnulerBy(null);
                selectDoc.setValiderBy(currentUser.getUsers());
                selectDoc.setCloturerBy(null);
                selectDoc.setDateAnnuler(null);
                selectDoc.setDateCloturer(null);
                selectDoc.setDateValider(new Date());
                selectDoc.setDateUpdate(new Date());
                selectDoc.setStatut(Constantes.ETAT_VALIDE);
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    selectDoc.setAuthor(currentUser);
                }
                dao.update(selectDoc);
                //ensuite on livre la facture.
                livraison(y.getDocuments(), y, currentUser, currentAgence.getSociete(), currentAgence, y.getDepotLivrer(), selectDoc.getEnteteDoc().getCreneau().getCreneauPoint().getPoint());
                dao.getEquilibreVente(selectDoc.getId());
                return new ResultatAction<>(true, y, y.getId(), "Succes");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComDocVentes.class.getName()).log(Level.SEVERE, "Erreur à la génération du bon", ex);
        }
        return new ResultatAction<>(false, null, 0L, "Action impossible!!!");
    }
}
