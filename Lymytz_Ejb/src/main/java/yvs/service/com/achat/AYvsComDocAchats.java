/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.achat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Options;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.YvsComParametreAchat;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.compta.YvsComptaAcompteClient;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.compta.saisie.YvsComptaContentJournalFactureAchat;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.workflow.YvsWorkflowAutorisationValidDoc;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidFactureAchat;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.service.AbstractEntity;
import yvs.service.IEntitySax;
import yvs.service.UtilRebuild;
import static yvs.service.com.vente.AYvsComDocVentes.ldf;
import static yvs.service.com.vente.AYvsComDocVentes.time;
import yvs.service.param.workflow.IYvsWorkflowValidFactureAchat;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComDocAchats extends AbstractEntity {
 
    private YvsComParametreAchat currentParam;
    private String tabIds, model, docIds, rowIds, groupBy = "C";
    private List<YvsComDocAchats> documents;

    IEntitySax IEntitiSax = new IEntitySax();

    public AYvsComDocAchats() {
    }

    public AYvsComDocAchats(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsWorkflowValidFactureAchat> valideEtape(YvsWorkflowValidFactureAchat entity) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                Long id = (Long) dao.loadOneByNameQueries("YvsWorkflowValidFactureAchat.findByIDEtapeFacture", new String[]{"facture", "etape"}, new Object[]{entity.getFactureAchat(), entity.getEtape()});
                entity.setId(id);

                entity.getFactureAchat().getEtapesValidations().clear();
                ResultatAction<YvsWorkflowValidFactureAchat> result = validEtapeFacture(entity.getFactureAchat(), entity, entity.getEtape().getFirstEtape());

                if (result.isResult()) {
                    entity.getFactureAchat().getBonsProvisoire().clear();
                    entity.getFactureAchat().getContenus().clear();
                    entity.getFactureAchat().getCouts().clear();
                    entity.getFactureAchat().getMensualites().clear();
                    entity.getFactureAchat().getReglements().clear();
                    entity.getFactureAchat().getEtapesValidations().clear();

                    return new ResultatAction(true, null, null, "Succes");
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Action Impossible");
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AYvsComDocAchats.class.getName()).log(Level.SEVERE, null, e);

        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComDocAchats> livraison(YvsComDocAchats entity) {
        try {
            ResultatAction<YvsComDocAchats> result = transmisOrder(entity, Constantes.ETAT_LIVRE, entity.getAuthor());
            if (result.isResult()) {
                return result;
            } else {
                return new ResultatAction<>(false, null, 0L, result.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultatAction<>(false, null, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComDocAchats> controle(YvsComDocAchats entity) {
        try {
            YvsUsersAgence authors = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findById", new String[]{"id"}, new Object[]{entity.getAuthor().getId()});
            YvsUsers user = authors.getUsers();
            YvsNiveauAcces acces = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauUsers.findNiveauByUser", new String[]{"user", "societe"}, new Object[]{user, user.getAgence().getSociete()});

            if (entity == null) {
                return new ResultatAction<>(false, rebuildDocAchat(entity), 0L, "L'élément ne peut pas etre null");
            }
            if ((entity.getFournisseur() != null) ? entity.getFournisseur().getId() < 1 : true) {
                return new ResultatAction<>(false, rebuildDocAchat(entity), 0L, "Vous devez indiquer le fournisseur");
            }
            if ((entity.getCategorieComptable() != null) ? entity.getCategorieComptable().getId() < 1 : true) {
                return new ResultatAction<>(false, rebuildDocAchat(entity), 0L, "Vous devez indiquer la catégorie comptable");
            }
            if (new Date().before(entity.getDateDoc())) {
                return new ResultatAction<>(false, rebuildDocAchat(entity), 0L, "Vous ne pouvez pas enregistrer une facture dans le future");
            }
            if (entity.getId() < 1) {
                if (!entity.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                    return new ResultatAction<>(false, rebuildDocAchat(entity), 0L, "Le document doit etre éditable pour pouvoir etre modifié");
                }
            } else {
                if (!entity.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                    for (YvsWorkflowValidFactureAchat e : ordonneEtapes(entity.getEtapesValidations())) {
                        if (!e.getEtapeValid()) {
                            YvsWorkflowValidFactureAchat v = giveEtapeActuelle(entity.getEtapesValidations());

                            if (acces != null ? acces.getId() > 0 : false) {
                                if (!asDroitValideEtape(v.getEtape().getAutorisations(), acces)) {
                                    return new ResultatAction<>(false, rebuildDocAchat(entity), 0L, "Vous ne pouvez modifier cette facture ! Elle requière un niveau suppérieur");
                                }
                            }

                        }
                    }
                }

            }
            return new ResultatAction<>(true, rebuildDocAchat(entity), 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComDocAchats.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, rebuildDocAchat(entity), 0L, "Action Impossible");

    }

    public ResultatAction<YvsComDocAchats> save(YvsComDocAchats entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_doc_achats", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    List<YvsWorkflowEtapeValidation> etapes = new ArrayList<>();
                    YvsAgences agence = new YvsAgences();
                    if (entity.getAgence() != null ? entity.getAgence().getId() > 0 : false) {
                        agence = entity.getAgence();
                    } else {
                        agence = (YvsAgences) dao.loadOneByNameQueries("YvsAgences.findById", new String[]{"id"}, new Object[]{entity.getAgence().getId()});

                    }
                    etapes = dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", new String[]{"titre", "societe"}, new Object[]{Constantes.DOCUMENT_FACTURE_ACHAT, agence.getSociete()});
                    entity.setEtapeTotal(etapes.size());
                    entity.setId(null);

                    entity.getContenus().clear();
                    entity.getReglements().clear();

                    List<YvsWorkflowValidFactureAchat> etape_facture = new ArrayList<>(entity.getEtapesValidations());
                    entity.getEtapesValidations().clear();
                    entity = (YvsComDocAchats) dao.save1(entity);

                    if (etape_facture != null ? !etape_facture.isEmpty() : false) {
                        IYvsWorkflowValidFactureAchat impl = (IYvsWorkflowValidFactureAchat) IEntitiSax.createInstance("IYvsWorkflowValidFactureAchat", dao);
                        for (YvsWorkflowValidFactureAchat etape : etape_facture) {
                            etape.setFactureAchat(new YvsComDocAchats(entity.getId()));
                            impl.save(etape);
                        }
                    } else {
                        if (etapes != null ? !etapes.isEmpty() : false) {
                            entity.setEtapesValidations(saveEtapesValidation(entity, etapes));
                            for (YvsWorkflowValidFactureAchat etap : entity.getEtapesValidations()) {
                                etap.setEtape(new YvsWorkflowEtapeValidation(etap.getEtape().getId()));
                            }
                        }
                    }
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, rebuildDocAchat(entity), entity.getId(), "Succès", rebuildDocAchat(entity));
                }
                result = new ResultatAction<>(false, rebuildDocAchat(entity), 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComDocAchats.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, rebuildDocAchat(entity), 0L, "Action Impossible");
        }
        return result;
    }

    public YvsComDocAchats rebuildDocAchat(YvsComDocAchats d) {
        return UtilRebuild.reBuildDocAchat(d);
    }

    public ResultatAction<YvsComDocAchats> update(YvsComDocAchats entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity.getContenus().clear();
                entity.getReglements().clear();
                entity = (YvsComDocAchats) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, rebuildDocAchat(entity), entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, rebuildDocAchat(entity), 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComDocAchats.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, rebuildDocAchat(entity), 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComDocAchats> delete(YvsComDocAchats entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, rebuildDocAchat(entity), entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComDocAchats.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ResultatAction<YvsComDocAchats> validerOrder(YvsComDocAchats entity, YvsUsersAgence currentUser) {
        YvsUsersAgence author = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findById", new String[]{"id"}, new Object[]{currentUser.getAuthor().getId()});
        entity.setAuthor(currentUser);
        YvsUsers user = author.getUsers();
        if (entity == null) {
            return new ResultatAction<>(false, null, 0L, "La facture ne peut pas être vide ! ");
        }
        if (!verifyOperation(entity.getDepotReception(), Constantes.ENTREE, Constantes.ACHAT, true)) {
            return new ResultatAction<>(false, null, 0L, "Le depot '" + entity.getDepotReception().getDesignation() + "' n'est pas paramètré pour les opérations 'ACHAT'");
        }
        if (entity.getMontantNetApayer() < 0) {
            return new ResultatAction<>(false, null, 0L, "La facture ne doit pas avoir un net à payer égal à zéro");
        }
        if (entity.getContenus().isEmpty()) {
            return new ResultatAction<>(false, null, 0L, "La facture ne peut pas être vide");
        }
        for (YvsComContenuDocAchat c : entity.getContenus()) {
            if (c.getPrixAchat() <= 0) {
                return new ResultatAction<>(false, null, 0L, "Aucune ligne ne doit avoir un prix d'achat égal à zéro");
            }
        }
        entity.setLivraisonDo(true);
        if (changeStatut_(Constantes.ETAT_VALIDE, entity)) {
            entity.setCloturer(false);
            entity.setAnnulerBy(null);
            entity.setValiderBy(user);
            entity.setDateAnnuler(null);
            entity.setCloturerBy(null);
            entity.setDateCloturer(null);
            entity.setDateValider(new Date());
            entity.setAuthor(entity.getAuthor());
            dao.update(entity);
            for (YvsComContenuDocAchat c : entity.getContenus()) {
                c.setAuthor(entity.getAuthor());
                c.setStatut(Constantes.ETAT_VALIDE);
                if (entity.getContenus().contains(c)) {
                    entity.getContenus().set(entity.getContenus().indexOf(c), c);
                }
                dao.update(c);
            }
            if (currentParam == null) {
                currentParam = (YvsComParametreAchat) dao.loadOneByNameQueries("YvsComParametreAchat.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if (currentParam != null) {
                if (currentParam.getComptabilisationAuto()) {
                    comptabiliserAchat(entity, false, false, currentUser);
                }
            }
            return new ResultatAction(true, rebuildDocAchat(entity), entity.getId(), "Succes");
        }
        return new ResultatAction(false, null, 0L, "Action impossible ");
    }

    public boolean comptabiliserAchat(YvsComDocAchats y, boolean msg, boolean succes, YvsUsersAgence currentUser) {
        if (y != null) {
            return comptabiliserAchat(y, buildAchatToComptabilise(y.getId(), msg, currentUser), msg, succes, currentUser);
        }
        return false;
    }

    private List<YvsComptaContentJournal> buildAchatToComptabilise(long id, boolean msg, YvsUsersAgence currentUser) {
        List<YvsComptaContentJournal> list = new ArrayList<>();
        YvsComDocAchats y = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{id});
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            // Verification si les articles de la facture sont parametré avec la categorie comptable de la facture
            String req = "select distinct(co.article) from yvs_com_contenu_doc_achat co "
                    + "inner join yvs_com_doc_achats dv on co.doc_achat = dv.id "
                    + "where dv.id = ? and co.article not in (select distinct(ca.article) "
                    + "from yvs_base_article_categorie_comptable ca where dv.categorie_comptable = ca.categorie)";
            List<Object> ids = dao.loadListBySqlQuery(req, new Options[]{new Options(id, 1)});
            if (ids != null ? !ids.isEmpty() : false) {
                Object o = ids.get(0);
                if (o != null) {
                    YvsBaseArticles a = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{(Long) o});
                    if (a != null ? (a.getId() != null ? a.getId() > 0 : false) : false) {
                        if (msg) {
//                            getErrorMessage("Cette facture est rattachée à l'article " + a.getDesignation() + " qui n'est pas rattaché à la catégorie de cette facture");
                        }
                    }
                }
                return null;
            }
            list = buildContentJournal(id, y.getTypeDoc().equals(Constantes.TYPE_FAA) ? Constantes.SCR_AVOIR_ACHAT : Constantes.SCR_ACHAT, msg, currentUser);
        } else {
            if (msg) {
//                getErrorMessage("Comptabilisation impossible... Car cette facture d'achat n'existe pas");
            }
            return null;
        }
        return list;
    }

    public boolean changeStatut_(String etat, YvsComDocAchats doc_) {
        if (!etat.equals("")) {
            if (doc_.getCloturer()) {
                return false;
            }
            String rq = "UPDATE yvs_com_doc_achats SET statut = '" + etat + "' WHERE id=?";
            Options[] param = new Options[]{new Options(doc_.getId(), 1)};
            dao.requeteLibre(rq, param);
            doc_.setStatut(etat);
            doc_.setStatut(etat);
            return true;
        }
        return false;
    }

    private boolean comptabiliserAchat(List<YvsComptaContentJournal> contenus, boolean msg, YvsComDocAchats selectAchat, YvsUsersAgence currentUserç) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;
        if (ids != null ? !ids.isEmpty() : false) {
            boolean error = false;
            List<YvsComptaContentJournal> list;
            for (int idx : ids) {
                YvsComDocAchats y = documents.get(idx);
                list = new ArrayList<>();
                for (YvsComptaContentJournal c : contenus) {
                    if (c.getRefExterne().equals(y.getId())) {
                        list.add(c);
                    }
                }
                if (!list.isEmpty()) {
                    if (comptabiliserAchat(y, list, msg, false, currentUserç)) {
                        succes = true;
                    } else {
                        error = true;
                    }
                }
            }
            docIds = "";

        } else {
            succes = comptabiliserAchat(selectAchat, contenus, msg, false, currentUserç);
        }
        return succes;
    }

    private boolean comptabiliserAchat(YvsComDocAchats y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes, YvsUsersAgence currentUser) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatut().equals(Constantes.ETAT_VALIDE)) {
                    boolean comptabilise = isComptabilise(y.getId(), Constantes.SCR_ACHAT);
                    if (comptabilise) {

                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaAchat(y.getId(), contenus, msg, currentUser);
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

    private YvsComptaPiecesComptable majComptaAchat(long id, List<YvsComptaContentJournal> contenus, boolean msg, YvsUsersAgence currentUser) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return null;
            }
            YvsComDocAchats y = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                YvsComptaJournaux jrn = giveJournalAchat(currentUser.getAgence());
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDateDoc(), jrn, contenus, msg, currentUser, currentUser.getAgence());
                    if (p != null ? p.getId() < 1 : true) {
                        return p;
                    }
                    YvsComptaContentJournalFactureAchat c = new YvsComptaContentJournalFactureAchat(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);

                    List<YvsComptaCaissePieceAchat> pieces = dao.loadNameQueries("YvsComptaCaissePieceAchat.findByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                    for (YvsComptaCaissePieceAchat r : pieces) {
//                        comptabiliserCaisseAchat(r, false, false);
                    }

                    //Debut du lettrage
                    lettrerAchat(y, pieces, currentUser);
                    y.setComptabilise(true);
                    return p;
                }
            }
        }
        return null;
    }

    public List<YvsComptaContentJournal> lettrerAchat(YvsComDocAchats y, YvsUsersAgence currentUser) {
        List<YvsComptaCaissePieceAchat> pieces = dao.loadNameQueries("YvsComptaCaissePieceAchat.findByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
        return lettrerAchat(y, pieces, currentUser);
    }

    public List<YvsComptaContentJournal> lettrerAchat(YvsComDocAchats y, List<YvsComptaCaissePieceAchat> pieces, YvsUsersAgence currentUser) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        if (pieces != null ? !pieces.isEmpty() : false) {
            String isLettrer = null;
            List<YvsComptaContentJournal> credits = null, debits = null;
            YvsComptaAcompteClient acompte = (YvsComptaAcompteClient) dao.loadOneByNameQueries("YvsComptaNotifReglementAchat.findAcompteByFactureNature", new String[]{"facture", "nature"}, new Object[]{y, 'D'});
            if ((acompte != null ? acompte.getId() < 1 : true) || true) {
                List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceAchat.findIdByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_ACHAT});
                if (debits != null ? !debits.isEmpty() : false) {
                    if (asString(debits.get(0).getLettrage())) {
                        isLettrer = debits.get(0).getLettrage();
                    } else {
                        credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{y.getId(), Constantes.SCR_ACHAT});
                    }
                }
            } else {
                debits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{acompte.getId(), Constantes.SCR_ACOMPTE_ACHAT});
                if (debits != null ? !debits.isEmpty() : false) {
                    if (asString(debits.get(0).getLettrage())) {
                        isLettrer = debits.get(0).getLettrage();
                    } else {
                        List<Long> ids = dao.loadNameQueries("YvsComptaNotifReglementAchat.findIdFactureByAcompte", new String[]{"acompte"}, new Object[]{acompte});
                        credits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_ACHAT});
                    }
                }
            }
            if (credits != null ? !credits.isEmpty() : false) {
                if (asString(credits.get(0).getLettrage())) {
                    isLettrer = credits.get(0).getLettrage();
                } else {
                    List<YvsComptaContentJournal> list = new ArrayList<>();
                    list.addAll(debits);
                    list.addAll(credits);
                    YvsComptaPiecesComptable x = new YvsComptaPiecesComptable(list);
                    if (x.getSolde() == 0) {
                        lettrageCompte(list, currentUser);
                    }
                }
            }
            if (asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentAgence.getSociete(), isLettrer});
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

    public List<YvsWorkflowValidFactureAchat> ordonneEtapes(List<YvsWorkflowValidFactureAchat> l) {
        List<YvsWorkflowValidFactureAchat> re = new ArrayList<>();
        YvsWorkflowValidFactureAchat first = null;
        //recherche la première étape       
        for (YvsWorkflowValidFactureAchat vm : l) {
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
                for (YvsWorkflowValidFactureAchat vm : l) {
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
        for (YvsWorkflowValidFactureAchat vm : re) {
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

    private YvsWorkflowValidFactureAchat giveEtapeActuelle(List<YvsWorkflowValidFactureAchat> etapes) {
        if (!etapes.isEmpty()) {
            List<YvsWorkflowValidFactureAchat> l = ordonneEtapes(etapes);
            for (YvsWorkflowValidFactureAchat e : l) {
                if (!e.getEtapeValid()) {
                    return e;
                }
            }
            return l.get(l.size() - 1);
        } else {
            return null;
        }
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

    public ResultatAction<YvsWorkflowValidFactureAchat> validEtapeFacture(YvsComDocAchats selectDoc, YvsWorkflowValidFactureAchat etape, boolean lastEtape) {
        return validEtapeFacture(selectDoc, etape, lastEtape, true, dao);
    }

    public void validEtapeFacture(YvsComDocAchats selectDoc, YvsWorkflowValidFactureAchat etape, boolean lastEtape, DaoInterfaceLocal dao) {
        validEtapeFacture(selectDoc, etape, lastEtape, false, dao);
    }

    public ResultatAction<YvsWorkflowValidFactureAchat> validEtapeFacture(YvsComDocAchats current, YvsWorkflowValidFactureAchat etape, boolean lastEtape, boolean save, DaoInterfaceLocal dao) {
        //vérifier que la personne qui valide l'étape a le droit 
//        if (!asDroitValideEtape(etape.getEtape())) {
//            openNotAcces();
//        } else {
        boolean continu = true;
        List<YvsWorkflowValidFactureAchat> list = new ArrayList<>();
        if (continu) {
            //contrôle la cohérence des dates
            current = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{current.getId()});
            System.err.println("current = " + current);

            etape.setFactureAchat(current);

            list = dao.loadNameQueries("YvsWorkflowValidFactureAchat.findByFacture", new String[]{"facture"}, new Object[]{current});
            list = ordonneEtapes(list);
            System.err.println("liste etape current = " + list);
            int idx = list.indexOf(etape);
            System.err.println("idx = " + idx);
            if (idx >= 0) {
                if (etape.getEtape().getLivraisonHere()) {
                    current.setLivraisonDo(true);
                }
                //cas de la validation de la dernière étapes
                etape.setFactureAchat(current);
                if (etape.getEtape().getEtapeSuivante() == null) {
                    ResultatAction<YvsComDocAchats> result = validerOrder(current, current.getAuthor());
                    if (result.isResult()) {
                        if (current.getAuthor() != null) {
                            etape.setAuthor(current.getAuthor());
                        }
                        etape.setEtapeValid(true);
                        etape.setEtapeActive(false);
                        etape.setMotif(null);
                        etape.setDateUpdate(new Date());
                        if (list.size() > (idx + 1)) {
                            list.get(idx + 1).setEtapeActive(true);
                        }
                        dao.update(etape);
                        current.setEtapeValide(current.getEtapeValide() + 1);
                        if (documents != null ? documents.contains(current) : false) {
                            int idx_ = documents.indexOf(current);
                            documents.get(idx_).setEtapeValide(current.getEtapeValide());
                        }
                        return new ResultatAction(true, etape, etape.getId(), "Succes");
                    }
                } else {
                    if (current.getAuthor() != null) {
                        etape.setAuthor(current.getAuthor());
                    }
                    etape.setEtapeValid(true);
                    etape.setEtapeActive(false);
                    etape.setMotif(null);
                    etape.setDateUpdate(new Date());
                    if (current.getEtapesValidations().size() > (idx + 1)) {
                        current.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                    }
                    dao.update(etape);
                    current.setStatut(Constantes.ETAT_ENCOURS);
                    if (current.getAuthor() != null) {
                        current.setAuthor(current.getAuthor());
                    }
                    current.setDateUpdate(new Date());
                    current.setEtapeValide(current.getEtapeValide() + 1);
                    dao.update(current);
                    current.setStatut(Constantes.ETAT_ENCOURS);
                    current.setEtapeValide(current.getEtapeValide());
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

    public List<YvsWorkflowEtapeValidation> saveEtapesValidation() {
        String[] champ = new String[]{"titre", "societe"};
        Object[] val = new Object[]{Constantes.DOCUMENT_FACTURE_ACHAT, currentAgence.getSociete()};
        return dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", champ, val);
    }

    public List<YvsWorkflowValidFactureAchat> saveEtapesValidation(YvsComDocAchats m, List<YvsWorkflowEtapeValidation> model) {
        //charge les étape de vailidation
        List<YvsWorkflowValidFactureAchat> re = new ArrayList<>();
        if (!model.isEmpty()) {
            YvsWorkflowValidFactureAchat vm;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    String[] champ = new String[]{"facture", "etape"};
                    Object[] val = new Object[]{m, et};
                    YvsWorkflowValidFactureAchat w = (YvsWorkflowValidFactureAchat) dao.loadOneByNameQueries("YvsWorkflowValidFactureAchat.findByEtapeFacture", champ, val);
                    if (w != null ? w.getId() < 1 : true) {
                        vm = new YvsWorkflowValidFactureAchat();
                        vm.setAuthor(new YvsUsersAgence(m.getAuthor().getId()));
                        vm.setEtape(et);
                        vm.setEtapeValid(false);
                        vm.setFactureAchat(new YvsComDocAchats(m.getId()));
                        vm.setDateSave(new Date());
                        vm.setId(null);
                        IYvsWorkflowValidFactureAchat impl = (IYvsWorkflowValidFactureAchat) IEntitiSax.createInstance("IYvsWorkflowValidFactureAchat", dao);
                        ResultatAction<YvsWorkflowValidFactureAchat> wo = impl.save(vm);
                        vm = (YvsWorkflowValidFactureAchat) wo.getData();
                        re.add(vm);
                    }
                }
            }
        }
        return ordonneEtapes(re);
    }

    public ResultatAction<YvsComDocAchats> transmisOrder(YvsComDocAchats selectDoc, String statutLivraison, YvsUsersAgence currentUser) {
        ResultatAction<YvsComDocAchats> result = transmisOrder(selectDoc, selectDoc.getDateLivraison(), statutLivraison, true, currentUser);
//        selectDoc.setEtapesValidations(ordonneEtapes(selectDoc.getEtapesValidations()));
        return result;
    }

    public ResultatAction<YvsComDocAchats> transmisOrder(YvsComDocAchats facture, Date dateLivraison, String statut, boolean message, YvsUsersAgence currentUser) {
        if (facture == null) {
            return new ResultatAction<>(false, facture, 0L, "Vous devez selectionner la facture");

        }
        if (facture.getDepotReception() != null ? facture.getDepotReception().getId() < 1 : true) {
            return new ResultatAction<>(false, facture, 0L, "Aucun dépôt de reception n'a été trouvé !");

        }
        if (facture.getTranche() != null ? facture.getTranche().getId() < 1 : true) {
            List<YvsGrhTrancheHoraire> list = loadTranche(facture.getDepotReception(), dateLivraison);
            if (list != null ? list.size() == 1 : false) {
                facture.setTranche(list.get(0));
            } else {
                return new ResultatAction<>(false, facture, 0L, "Aucune tranche de livraison n'a été trouvé !");
            }
        } else if (!asString(facture.getTranche().getTypeJournee())) {
            facture.setTranche((YvsGrhTrancheHoraire) dao.loadOneByNameQueries("YvsGrhTrancheHoraire.findById", new String[]{"id"}, new Object[]{facture.getTranche().getId()}));
        }
        if (dateLivraison != null ? dateLivraison.after(new Date()) : true) {
            return new ResultatAction<>(false, facture, 0L, "La date de livraison est incorrecte !");
        }

        if (!verifyOperation(new YvsBaseDepots(facture.getDepotReception().getId(), facture.getDepotReception().getDesignation()), Constantes.ENTREE, Constantes.ACHAT, false)) {
            return new ResultatAction<>(false, facture, 0L, "Action impossible,Ce dépot n'autorise pas cette action ");
        }
        if (!verifyDateAchat(dateLivraison, false, currentUser.getAgence())) {
            return new ResultatAction<>(false, facture, 0L, "Action impossible");
        }
        if (!verifyInventaire(facture.getDepotReception(), facture.getTranche(), dateLivraison)) {
            return new ResultatAction<>(false, facture, 0L, "Action impossible");
        }

        String num = dao.genererReference(Constantes.TYPE_BLA_NAME, dateLivraison, facture.getDepotReception().getId(), facture.getAgence().getSociete(), facture.getAgence());
        if (num != null ? num.trim().length() > 0 : false) {
            List<YvsComContenuDocAchat> contenus = loadContenusStay(facture, Constantes.TYPE_BLA);
            if (contenus != null ? !contenus.isEmpty() : false) {
                if (!verifyTranche(facture.getTranche(), facture.getDepotReception(), dateLivraison)) {
                    return new ResultatAction<>(false, rebuildDocAchat(facture), 0L, "Action impossible");
                }
                List<YvsComContenuDocAchat> list = new ArrayList<>();
                for (YvsComContenuDocAchat c : contenus) {
                    if (!controlContentForTransmis(c, facture.getDepotReception(), message)) {
                        return new ResultatAction<>(false, facture, 0L, "Action impossible");
                    }
                    list.add(c);
                    if (c.getQuantiteBonus() > 0) {
                        YvsComContenuDocAchat a = new YvsComContenuDocAchat(c);
                        a.setArticle(new YvsBaseArticles(c.getArticleBonus().getId(), c.getArticleBonus().getRefArt(), c.getArticleBonus().getDesignation()));
                        a.setConditionnement(new YvsBaseConditionnement(c.getConditionnementBonus().getId(), new YvsBaseUniteMesure(c.getConditionnementBonus().getUnite().getId(), c.getConditionnementBonus().getUnite().getReference(), c.getConditionnementBonus().getUnite().getLibelle())));
                        a.setQuantiteRecu(c.getQuantiteBonus());

                        a.setArticleBonus(null);
                        a.setConditionnementBonus(null);
                        a.setQuantiteBonus(0.0);
                        a.setPrixAchat(0.0);
                        a.setPrixTotal(0.0);
                        a.setTaxe(0.0);
                        if (!controlContentForTransmis(a, facture.getDepotReception(), message)) {
                            return new ResultatAction<>(false, facture, 0L, "Action impossible");
                        }
                        list.add(a);
                    }
                }

                YvsComDocAchats y = new YvsComDocAchats(facture);
                y.setDateSave(new Date());
                y.setAuthor(new YvsUsersAgence(currentUser.getId()));
                y.setAgence(facture.getAgence());
                y.setValiderBy(currentUser.getUsers());
                y.setTypeDoc(Constantes.TYPE_BLA);
                y.setNumDoc(num);
                y.setNumPiece("BL N° " + facture.getNumDoc());
                y.setDepotReception(new YvsBaseDepots(facture.getDepotReception().getId()));
                y.setTranche(new YvsGrhTrancheHoraire(facture.getTranche().getId()));
                y.setDateLivraison(dateLivraison);
                y.setDocumentLie(new YvsComDocAchats(facture.getId()));
                y.setCloturer(false);
                y.setStatut(statut);
                y.setStatutLivre(statut.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE);
                y.setStatutRegle(Constantes.ETAT_ATTENTE);
                y.setDescription("Reception de la facture N° " + facture.getNumDoc() + " le " + ldf.format(dateLivraison) + " à " + time.format(dateLivraison));
                y.getContenus().clear();
                y.setId(null);
                y = (YvsComDocAchats) dao.save1(y);
                for (YvsComContenuDocAchat c : list) {
                    long id = c.getId();
                    c.setExterne(null);
                    c.setDocAchat(y);
                    c.setStatut(Constantes.ETAT_VALIDE);
                    c.setParent(new YvsComContenuDocAchat(c.getId()));
                    c.setAuthor(currentUser);
                    c.setId(null);
                    c = (YvsComContenuDocAchat) dao.save1(c);
                    y.getContenus().add(c);

                    int idx = facture.getContenus().indexOf(new YvsComContenuDocAchat(id));
                    if (idx > -1) {
                        facture.getContenus().get(idx).getContenus().add(c);
                    }
                }
                y.setDocumentLie(facture);
                if (statut.equals(Constantes.ETAT_VALIDE)) {
                    if (validerOrder(y, false, false, true, currentUser)) {
                        String rq = "UPDATE yvs_com_doc_achats SET statut_livre = '" + (statut.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE) + "' WHERE id=?";
                        Options[] param = new Options[]{new Options(facture.getId(), 1)};
                        dao.requeteLibre(rq, param);
                        facture.setStatutLivre(statut.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE);
                    }

                    int idx = facture.getDocuments().indexOf(y);
                    if (idx < 0) {
                        facture.getDocuments().add(y);
                    }
//                    if (documents.contains(facture)) {
//                        documents.set(documents.indexOf(facture), facture);
////                    update("data_facture_achat");
//                    }
                    return new ResultatAction(true, null, null, "Succès");
                } else {
                    if (!facture.getContenus().isEmpty()) {
                        String rq = "UPDATE yvs_com_doc_achats SET statut_livre = 'L' WHERE id=?";
                        Options[] param = new Options[]{new Options(facture.getId(), 1)};
                        dao.requeteLibre(rq, param);
                        rq = "UPDATE yvs_com_doc_achats SET statut_livre = 'L' WHERE document_lie=?";
                        param = new Options[]{new Options(facture.getId(), 1)};
                        dao.requeteLibre(rq, param);

                        facture.setStatutLivre(Constantes.ETAT_LIVRE);
//                        if (documents.contains(facture)) {
//                            documents.set(documents.indexOf(facture), facture);
////                        update("data_facture_achat");
//                        }
                        return new ResultatAction(true, rebuildDocAchat(facture), facture.getId(), "Succès");
                    } else {
                        return new ResultatAction(false, 0, "Vous ne pouvez pas livrer cette facture car elle est vide");
                    }
                }
//                if (docAchat.getContenus().isEmpty()) {
//                    docAchat.getContenus().addAll(contenus);
//                }
            }

        }
        return new ResultatAction(false, 0, "Action impossible");
    }

    private List<YvsComContenuDocAchat> loadContenusStay(YvsComDocAchats y, String type) {
        List<YvsComContenuDocAchat> list = new ArrayList<>();
        y.setInt_(false);
        nameQueri = "YvsComContenuDocAchat.findByDocAchat";
        champ = new String[]{"docAchat"};
        val = new Object[]{y};
        List<YvsComContenuDocAchat> contenus = dao.loadNameQueries(nameQueri, champ, val);
        for (YvsComContenuDocAchat c : contenus) {
            String[] ch = new String[]{"parent", "typeDoc", "statut"};
            Object[] v = new Object[]{c, type, Constantes.ETAT_VALIDE};
            Double qte = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteByTypeStatutParent", ch, v);
            if (c.getQuantiteCommande() > (qte != null ? qte : 0)) {
                c.setQuantiteCommande(c.getQuantiteRecu());
                c.setQuantiteRecu(c.getQuantiteRecu() - (qte != null ? qte : 0));
                c.setPrixTotal(c.getPrixAchat() * c.getQuantiteRecu());
                c.setParent(new YvsComContenuDocAchat(c.getId()));
                list.add(c);
            }
        }
        return list;
    }

    public boolean verifyDateAchat(Date date, boolean update, YvsAgences agence) {
        int ecart = -1;
        if (!update) {
            List<YvsComParametreAchat> lp = dao.loadNameQueries("YvsComParametreAchat.findByAgence", new String[]{"agence"}, new Object[]{agence}, 0, 1);
            if ((lp != null) ? !lp.isEmpty() : false) {
                ecart = lp.get(0).getJourAnterieur();
            }
        }
        return verifyDates(date, ecart, agence);
    }

    public boolean verifyDates(Date date, int ecart, YvsAgences agence) {
        date = date != null ? date : new Date();
        String[] champ = new String[]{"dateJour", "societe"};
        Object[] val = new Object[]{date, agence.getSociete()};
        YvsBaseExercice exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findActifByDate", champ, val);
        if (exo != null ? exo.getId() < 1 : true) {
//            getErrorMessage("Le document doit etre enregistré dans un exercice actif");
            return false;
        }
        if (exo.getCloturer()) {
//            getErrorMessage("Le document ne peut pas etre enregistré dans un exercice cloturé");
            return false;
        }
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        now.add(Calendar.DATE, 1);

        Calendar d = Calendar.getInstance();
        d.setTime(date);
        d.set(Calendar.HOUR_OF_DAY, 0);
        d.set(Calendar.MINUTE, 0);
        d.set(Calendar.SECOND, 0);
        d.set(Calendar.MILLISECOND, 0);
        if (d.after(now)) {
//            getErrorMessage("La date ne doit pas superieur à la date du jour");
            return false;
        }
//        if (autoriser("com_save_hors_limit")) {
//            return true;
//        }
        if (ecart > 0) {
            now.add(Calendar.DATE, -ecart);
            if (d.before(now)) {
//                getErrorMessage("La date ne doit pas excedé le nombre de jour de retrait prévu");
                return false;
            }
        }
        return true;
    }

    private boolean controlContentForTransmis(YvsComContenuDocAchat c, YvsBaseDepots depot, boolean message) {
        champ = new String[]{"article", "depot"};
        val = new Object[]{c.getArticle(), depot};
        YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
        if (y != null ? y.getId() < 1 : true) {
            if (message) {
//                getErrorMessage("Impossible d'effectuer cette action... Car le depot " + depot.getDesignation() + " ne possède pas l'article " + c.getArticle().getDesignation());
            }
            return false;
        }
        if (!checkOperationArticle(c.getArticle().getId(), depot.getId(), Constantes.ACHAT)) {
            if (message) {
//                getErrorMessage("L'article '" + c.getArticle().getDesignation() + "' ne fait pas d'achat dans le depot '" + docAchat.getDepotReception().getDesignation() + "'");
            }
            return false;
        }
        return true;
    }

    public boolean validerOrder(YvsComDocAchats entity, boolean save, boolean load, boolean msg, YvsUsersAgence currentUser) {
        if (entity == null) {
            return false;
        }

        if (!verifyOperation(entity.getDepotReception(), Constantes.ENTREE, Constantes.ACHAT, msg)) {
            return false;
        }
        //contrôle la cohérence avec les inventaires
        if (!controleInventaire(entity.getDepotReception().getId(), entity.getDateDoc(), entity.getTranche().getId())) {
            return false;
        }
        boolean continu = !save;
//        if (save) {
//            continu = _saveNew(entity);
//        }
        if (continu) {
            if (livrer(entity, msg)) {
                if (changeStatutAchat(Constantes.ETAT_VALIDE, entity, load)) {
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
                    YvsComDocAchats y = new YvsComDocAchats(entity);
                    List<YvsComContenuDocAchat> temps = new ArrayList<>(y.getContenus());
                    y.getContenus().clear();
                    dao.update(y);
                    y.setContenus(temps);
                    entity.setContenus(temps);
                    entity.setContenus(temps);
                    for (YvsComContenuDocAchat c : temps) {
                        c.setStatut(Constantes.ETAT_EDITABLE);
                        changeStatutLine(c, false, currentUser);
                    }

                    if (entity.getGenererFactureAuto()) {
                        YvsComDocAchats d = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findLieById", new String[]{"id"}, new Object[]{entity.getId()});
                        if (d != null ? d.getId() < 1 : true) {
                            genereFactureAchat(entity, true, currentUser);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean livrer(YvsComDocAchats entity, boolean msg) {
        if (entity == null) {
            return false;
        }
        if (entity.getDepotReception() != null ? entity.getDepotReception().getId() < 1 : true) {
            if (msg) {
//                getErrorMessage("Vous devez specifier le dépot de livraison");
            }
            return false;
        }
        if (entity.getTranche() != null ? entity.getTranche().getId() < 1 : true) {
            if (msg) {
//                getErrorMessage("Vous devez specifier la tranche de livraison");
            }
            return false;
        }
        //verifier la cohérence avec les inventaires
        if (!controleInventaire(entity.getDepotReception().getId(), entity.getDateLivraison(), entity.getTranche().getId())) {
            return false;
        }
        if (entity.getDocumentLie() != null) {
            entity.getDocumentLie().setEtapesValidations(dao.loadNameQueries("YvsWorkflowValidFactureAchat.findByFacture", new String[]{"facture"}, new Object[]{entity.getDocumentLie()}));
            entity.getDocumentLie().setEtapesValidations(ordonneEtapes(entity.getDocumentLie().getEtapesValidations()));
            for (YvsWorkflowValidFactureAchat etape : entity.getDocumentLie().getEtapesValidations()) {
                if (etape.getEtape().getLivraisonHere() && etape.getEtapeValid()) {
                    entity.getDocumentLie().setLivraisonDo(true);
                }
            }

        }
        if (entity.getDocumentLie() != null ? entity.getDocumentLie().getId() > 0 : false) {
            if (!entity.getDocumentLie().isLivraisonDo() && !entity.getDocumentLie().getStatut().equals(Constantes.ETAT_VALIDE)) {
                if (msg) {
//                    getErrorMessage("La facture n'est pas encore au niveau possible de livraison");
                }
                return false;
            }
        }
        if (entity.getContenus() != null ? !entity.getContenus().isEmpty() : false) {
            for (YvsComContenuDocAchat c : entity.getContenus()) {
                //controle les quantités déjà livré
                Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findByDocLierTypeStatutArticleS", new String[]{"docAchat", "statut", "typeDoc", "article", "unite"}, new Object[]{entity.getDocumentLie(), Constantes.ETAT_VALIDE, Constantes.TYPE_BLV, c.getArticle(), c.getConditionnement()});
                qteLivre = (qteLivre != null) ? qteLivre : 0;
                //trouve la quantité d'article facturé 
                Double qteFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteByArticleFacture", new String[]{"docAchat", "article", "unite"}, new Object[]{entity.getDocumentLie(), c.getArticle(), c.getConditionnement()});
                qteFacture = (qteFacture != null) ? qteFacture : 0;
                //récupère la quantité de l'article dans le document de livraison en cours. (Le pb viens du fait que la ref d'un article peut se trouver plusieurs fois dans la liste d'un bl non encore livré)
                Double qteEncour = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteByArticle", new String[]{"docAchat", "article", "unite"}, new Object[]{entity, c.getArticle(), c.getConditionnement()});
                qteEncour = (qteEncour != null) ? qteEncour : 0;
                if (qteEncour > (qteFacture - qteLivre)) {
                }
                champ = new String[]{"article", "depot"};
                val = new Object[]{c.getArticle(), new YvsBaseDepots(entity.getDepotReception().getId())};
                YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
                if (y != null ? y.getId() < 1 : true) {
                    if (msg) {
//                        getErrorMessage("Impossible d'effectuer cette action... Car le depot " + entity.getDepotReception().getDesignation() + " ne possède pas l'article " + c.getArticle().getDesignation());
                    }
                    return false;
                }
                if (!checkOperationArticle(c.getArticle().getId(), entity.getDepotReception().getId(), Constantes.ACHAT)) {
                    if (msg) {
//                        getErrorMessage("L'article '" + c.getArticle().getDesignation() + "' ne fait pas d'achat dans le depot '" + bean.getDepotReception().getDesignation() + "'");
                    }
                    return false;
                }
            }
            entity.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_LIVRER));
            entity.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_LIVRER));
            entity.setDateLivraison(entity.getStatutLivre().equals(String.valueOf(Constantes.STATUT_DOC_LIVRER)) ? new Date() : null);
//            update("data_livraison_achat");
            return true;
        } else {
            if (msg) {
//                getErrorMessage("Vous ne pouvez pas valider un document vide");
            }
        }
        return false;
    }

    public boolean changeStatutAchat(String etat, YvsComDocAchats entity, boolean load) {
        if (!etat.equals("")) {
            if (entity.getCloturer()) {
//                getErrorMessage("Ce document est vérouillé");
                return false;
            }
            String rq = "UPDATE yvs_com_doc_achats SET statut = '" + etat + "' WHERE id=?";
            Options[] param = new Options[]{new Options(entity.getId(), 1)};
            dao.requeteLibre(rq, param);
            entity.setStatut(etat);

            int idx = documents.indexOf(entity);
            if (idx > -1) {
                documents.set(idx, entity);
            }
            if (entity.getDocumentLie() != null ? (entity.getDocumentLie().getId() != null ? entity.getDocumentLie().getId() > 0 : false) : false) {
                dao.getEquilibreAchat(entity.getDocumentLie().getId());
            }
//            if (load) {
//                ManagedFactureAchat m = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
//                if (m != null) {
//                    m.loadFactureNonLivre(true, true);
//                    update("data_fa_livraison");
//                }
//            }
//            update("infos_document_livraison_achat");
//            update("data_livraison_achat");
//            update("data_contenu_livraison_achat");
            return true;
        }
        return false;
    }

    public void changeStatutLine(YvsComContenuDocAchat y, YvsUsersAgence currentUser) {
        changeStatutLine(y, true, currentUser);
    }

    public void changeStatutLine(YvsComContenuDocAchat y, boolean msg, YvsUsersAgence currentUser) {
        if (y != null ? y.getId() > 0 : false) {
            if (!y.getStatut().equals(Constantes.ETAT_EDITABLE)) {
//                if (!autoriser("bla_editer_doc")) {
//                    openNotAcces();
//                    return;
//                }
                y.setStatut(Constantes.ETAT_EDITABLE);
                y.setAuthor(currentUser);
                dao.update(y);
                int idx = y.getDocAchat().getContenus().indexOf(y);
                if (idx > -1) {
                    y.getDocAchat().getContenus().set(idx, y);
//                    update("data_contenu_livraison_achat");
                }
                idx = documents.indexOf(new YvsComDocAchats(y.getDocAchat().getId()));
                if (idx > -1) {
                    int i = documents.get(idx).getContenus().indexOf(y);
                    if (i > -1) {
                        documents.get(idx).getContenus().set(i, y);
                    }
                }
//                if (msg) {
//                    succes();
//                }
            } else {
                if (y.getDocAchat().getStatut().equals(Constantes.ETAT_VALIDE)) {
                    y.setStatut(Constantes.ETAT_VALIDE);
                    y.setAuthor(currentUser);
                    dao.update(y);
                    int idx = y.getDocAchat().getContenus().indexOf(y);
                    if (idx > -1) {
                        y.getDocAchat().getContenus().set(idx, y);
//                        update("data_contenu_livraison_achat");
                    }
                    idx = documents.indexOf(new YvsComDocAchats(y.getDocAchat().getId()));
                    if (idx > -1) {
                        int i = documents.get(idx).getContenus().indexOf(y);
                        if (i > -1) {
                            documents.get(idx).getContenus().set(i, y);
                        }
                    }
//                    if (msg) {
//                        succes();
//                    }
                } else {
                    if (msg) {
//                        getErrorMessage("Impossible de valider cette ligne car le document n'est pas valide");
                    }
                }
            }
            equilibre(y.getDocAchat());
        }
    }

    public void equilibre(YvsComDocAchats selectDoc) {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (selectDoc.getDocumentLie() != null ? selectDoc.getDocumentLie().getId() > 0 : false) {
                dao.getEquilibreAchat(selectDoc.getDocumentLie().getId());
            }
        }
    }

    public void genereFactureAchat(YvsComDocAchats bean, YvsUsersAgence currentUser) {
        genereFactureAchat(bean, true, currentUser);
    }

    public void genereFactureAchat(YvsComDocAchats bean, boolean msg, YvsUsersAgence currentUser) {
        if (bean != null ? bean.getId() > 0 : false) {
            Long id = bean.getId();
            if (bean.getDocumentLie() != null ? bean.getDocumentLie().getId() > 0 : false) {
                if (msg) {
//                    getErrorMessage("Cette commande est déjà liée à une facture");
                }
                return;
            }
            String num = dao.genererReference(Constantes.TYPE_FA_NAME, bean.getDateLivraison(), bean.getId(), currentUser.getAgence().getSociete(), currentUser.getAgence());
            if (num != null ? num.trim().length() > 0 : false) {
                List<YvsComContenuDocAchat> list = new ArrayList<>();
                if (bean.getContenus() != null ? !bean.getContenus().isEmpty() : false) {
                    list.addAll(bean.getContenus());

                    YvsComDocAchats y = new YvsComDocAchats(bean);
                    y.setAgence(currentAgence);
                    y.setDateSave(new Date());
                    y.setDateUpdate(new Date());
                    y.setAuthor(currentUser);
                    y.setValiderBy(currentUser.getUsers());
                    y.setTypeDoc(Constantes.TYPE_FA);
                    y.setNumDoc(num);
                    y.setNumPiece("FA N° " + bean.getNumDoc());
                    y.setDateDoc(bean.getDateLivraison());
                    y.setStatut(Constantes.ETAT_EDITABLE);
                    y.setStatutLivre(Constantes.ETAT_ATTENTE);
                    y.setStatutRegle(Constantes.ETAT_ATTENTE);
                    y.setDescription("Facture d'achat N° " + bean.getNumDoc() + " le " + ldf.format(bean.getDateLivraison()));
                    y.getContenus().clear();
                    y.setId(null);
                    y = (YvsComDocAchats) dao.save1(y);
                    YvsComContenuDocAchat c;
                    for (YvsComContenuDocAchat c_ : list) {
                        c = new YvsComContenuDocAchat(null, c_);
                        c.setArticle(c_.getArticle());
                        c.setConditionnement(c_.getConditionnement());
                        c.setExterne(null);
                        c.setDocAchat(y);
                        c.setStatut(Constantes.ETAT_VALIDE);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        c = (YvsComContenuDocAchat) dao.save1(c);
                        y.getContenus().add(c);

                        c_.setParent(new YvsComContenuDocAchat(c.getId()));
                        c_.setDocAchat(new YvsComDocAchats(id));
                        dao.update(c_);
                    }

                    List<YvsWorkflowEtapeValidation> etapes = saveEtapesValidation();
                    y.setEtapeTotal(etapes != null ? etapes.size() : 0);
                    y.setEtapesValidations(saveEtapesValidation(y, etapes));

                    bean.setDocumentLie(new YvsComDocAchats(y.getId()));
                    dao.update(bean);
                    bean.setContenus(list);
                    if (bean.getId() == id) {

//                        bean.setDocumentLie(y);
//                        update("zone_facture_bla");
                        if (bean.getContenus().isEmpty()) {
                            bean.setContenus(list);
//                            update("data_contenu_livraison_achat");
                        }
                    }
                    if (documents.contains(bean)) {
                        documents.set(documents.indexOf(bean), bean);
//                        update("data_livraison_achat");
                    }
//                    if (msg) {
//                        succes();
//                    }
                    dao.getEquilibreAchat(y.getId());
                }
            }
        }
    }

    //Entity est le bon de livraison
    public ResultatAction<YvsComDocAchats> reception(YvsComDocAchats entity) {
        try {
            if (entity != null) {
                if (!entity.isSynchroniser()) {
                    if (!autoriser("blv_valide_doc")) {
                        return new ResultatAction<>(false, null, 0L, "Vous n'avez pas les privilèges requis pour cette action");
                    }
                }
                if (entity.getContenus() != null ? entity.getContenus().isEmpty() : true) {
                    return new ResultatAction<>(false, null, 0L, "Ce bon de livraison est vide");
                }
                if (entity.getDocumentLie() != null ? entity.getDocumentLie().getId() < 1 : true) {
                    return new ResultatAction<>(false, null, 0L, "Ce bon de livraison n'est pas rattaché à une facture");
                }
                if (entity.getDepotReception() != null ? entity.getDepotReception().getId() < 1 : true) {
                    return new ResultatAction<>(false, null, 0L, "Ce bon de reception n'est pas rattaché à un dépot ");
                }
                if (entity.getTranche() != null ? entity.getTranche().getId() < 1 : true) {
                    return new ResultatAction<>(false, null, 0L, "Ce bon de livraison n'est pas rattaché à une tranche de livraison");
                }

                if (entity.getDateLivraison() == null) {
                    return new ResultatAction<>(false, null, 0L, "Ce bon de livraison n'a pas de date de livraison");
                }
                YvsComDocAchats facture = entity.getDocumentLie();
                if (!verifyOperation(entity.getDepotReception(), Constantes.ENTREE, Constantes.ACHAT, false)) {
                    return new ResultatAction<>(false, null, 0L, "Ce dépot n'autorise pas les achats");
                }
                //contrôle la cohérence avec les inventaires
                if (!verifyInventaire(entity.getDepotReception(), entity.getTranche(), entity.getDateLivraison())) {
                    return new ResultatAction<>(false, null, 0L, "Il existe déja des inventaires anterieurs a cette date");
                }

                List<YvsComContenuDocAchat> contenus = new ArrayList<>();
                for (YvsComContenuDocAchat contenu : entity.getContenus()) {
                    if (contenu.getQuantiteRecu() > 0) {
                        String[] champ = new String[]{"article", "depot"};
                        Object[] val = new Object[]{contenu.getArticle(), entity.getDepotReception()};
                        YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
                        if (y != null ? y.getId() < 1 : true) {
                            return new ResultatAction<>(false, null, 0L, "Impossible d'effectuer cette action... Car le depot " + entity.getDepotReception().getDesignation() + " ne possède pas l'article " + contenu.getArticle().getDesignation());
                        }
//                        String controle = controleStock(contenu.getArticle().getId(), contenu.getConditionnement().getId(), entity.getDepotReception().getId(), 0L, contenu.getQuantiteCommande(), 0, "INSERT", "S", entity.getDateLivraison());
//                        if (controle != null) {
//                            return new ResultatAction<>(false, null, 0L, "La ligne d'article " + contenu.getArticle().getDesignation() + " engendrera une incohérence dans le stock à la date du " + controle);
//                        }
                        //controle les quantités déjà livré
                        Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findByDocLierTypeStatutArticleS", new String[]{"docAchat", "statut", "typeDoc", "article", "unite"}, new Object[]{facture, Constantes.ETAT_VALIDE, Constantes.TYPE_BLV, contenu.getArticle(), contenu.getConditionnement()});
                        qteLivre = (qteLivre != null) ? qteLivre : 0;
                        //trouve la quantité d'article facturé 
                        Double qteFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteByArticles", new String[]{"docAchat", "article", "unite"}, new Object[]{facture, contenu.getArticle(), contenu.getConditionnement()});
                        qteFacture = (qteFacture != null) ? qteFacture : 0;
                        //trouve la quantité d'article facturé 
                        Double qteBonusFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteBonusByFacture", new String[]{"docAchat", "article", "unite"}, new Object[]{facture, contenu.getArticle(), contenu.getConditionnement()});
                        qteBonusFacture = (qteBonusFacture != null) ? qteBonusFacture : 0;
                        if (facture != null ? !facture.getStatutRegle().equals(Constantes.ETAT_REGLE) : true) {
                            //si la facture n'est pas encore réglé, on ne dois pas inclure la quantité bonus dans la quantité à livrer
                            if (contenu.getQuantiteRecu() > (qteFacture - qteLivre)) {
                                return new ResultatAction<>(false, null, 0L, "Vous ne pouvez recevoir  l'article " + contenu.getArticle().getRefArt() + " au delà de la quantité facturée !");
                            }
                        } else {
                            if (contenu.getQuantiteRecu() > ((qteFacture + qteBonusFacture) - qteLivre)) {
                                return new ResultatAction<>(false, null, 0L, "Vous ne pouvez recevoir l'article " + contenu.getArticle().getRefArt() + " au delà de la quantité facturée !");
                            }
                        }
                        contenus.add(contenu);
                    }
                }
                if (contenus.isEmpty()) {
                    return new ResultatAction<>(false, null, 0L, "Ce bon de livraison est vide");
                }
                String reference = dao.genererReference(Constantes.TYPE_BLA_NAME, entity.getDateLivraison(), entity.getDepotReception().getId(), entity.getAgence().getSociete(), entity.getAgence());
                if (!asString(reference)) {
                    return new ResultatAction<>(false, null, 0L, "La reference ne peut pas être vide");
                }
                entity.setId(null);
                entity.setNumDoc(reference);
                entity.setDateSave(new Date());
                entity.setDateUpdate(new Date());
                entity.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_LIVRER));
                entity.setStatut(String.valueOf(Constantes.STATUT_DOC_VALIDE));
                entity.getContenus().clear();
                entity = (YvsComDocAchats) dao.save1(entity);

                for (YvsComContenuDocAchat contenu : contenus) {
                    contenu.setId(null);
                    contenu.setDateSave(new Date());
                    contenu.setDateUpdate(new Date());
                    contenu.setDocAchat(new YvsComDocAchats(entity.getId()));
                    contenu = (YvsComContenuDocAchat) dao.save1(contenu);
                    entity.getContenus().add(contenu);
                }

                dao.getEquilibreAchat(facture.getId());
                facture = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{facture.getId()});
                entity.setDocumentLie(new YvsComDocAchats(facture.getId(), facture.getNumDoc(), facture.getStatut(), facture.getStatutLivre(), facture.getStatutRegle()));
                return new ResultatAction<>(true, rebuildDocAchat(entity), entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, null, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComDocAchats> annulerReception(YvsComDocAchats entity, YvsNiveauAcces niveau) {
        try {
            if (!autoriser("bla_editer_doc", niveau)) {
                return new ResultatAction<>(false, null, 0L, "Vous n'avez pas les privilèges requis pour cette action");
            }
            if (entity != null ? entity.getId() > 0 : false) {
                if (entity.getCloturer()) {
                    return new ResultatAction<>(false, null, 0L, "Ce document est vérouillé");
                }
                List<YvsComDocAchats> documents = dao.loadNameQueries("YvsComDocAchats.findByParent", new String[]{"documentLie"}, new Object[]{entity});
                List<YvsComDocAchats> list = new ArrayList<>();
                list.addAll(documents);
                for (YvsComDocAchats d : list) {
                    dao.delete(d);
                    documents.remove(d);
                }
                //verifier la cohérence avec les inventaires
                if (!controleInventaire(entity.getDepotReception().getId(), entity.getDateDoc(), entity.getTranche().getId())) {
                    return new ResultatAction<>(false, null, 0L, "Vous ne pouvez créer une fiche de stock à cette date car un ou plusieurs inventaires ont déjà été réalisés après dans ce dépot");
                }
                //controle la cohérence des stocks
                String result;
                for (YvsComContenuDocAchat c : entity.getContenus()) {
                    result = controleStock(c.getArticle().getId(), (c.getConditionnement() != null ? c.getConditionnement().getId() : 0), entity.getDepotReception().getId(), 0L, c.getQuantiteRecu(), 0, "INSERT", "S", entity.getDateLivraison(), (c.getLot() != null ? c.getLot().getId() : 0));
                    if (result != null) {
                        return new ResultatAction<>(false, null, 0L, "L'article '" + c.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou entrainera un stock négatif à la date " + result);
                    }
                }
                if (documents != null ? documents.isEmpty() : true) {
                    entity.setStatut(Constantes.ETAT_EDITABLE);
                    entity.setCloturer(false);
                    entity.setAnnulerBy(null);
                    entity.setValiderBy(null);
                    entity.setCloturerBy(null);
                    entity.setDateAnnuler(null);
                    entity.setDateCloturer(null);
                    entity.setDateValider(null);
                    entity.setStatutLivre(Constantes.ETAT_ATTENTE);
                    entity.setDateLivraison(null);
                    entity.setAuthor(niveau.getAuthor());
                    entity.setDateUpdate(new Date());

                    YvsComDocAchats y = new YvsComDocAchats(entity);
                    List<YvsComContenuDocAchat> temps = new ArrayList<>(y.getContenus());
                    y.getContenus().clear();

                    dao.update(y);
                    y.setContenus(temps);
                    entity.setContenus(temps);

                    for (YvsComContenuDocAchat c : temps) {
                        c.setStatut(Constantes.ETAT_EDITABLE);
                        c.setAuthor(niveau.getAuthor());
                        c.setDateUpdate(new Date());
                        dao.update(c);
                    }
                    if (entity.getDocumentLie() != null ? entity.getDocumentLie().getId() > 0 : false) {
                        dao.getEquilibreAchat(entity.getDocumentLie().getId());
                    }
                    return new ResultatAction<>(true, rebuildDocAchat(entity), entity.getId(), "Succès");
                } else {
                    return new ResultatAction<>(false, null, 0L, "Impossible d'annuler cet ordre car il possède un transfert déja valide");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, null, 0L, "Operation Impossible");
    }

    public String controleStock(long article, long conditionnement, long depot, long tranche, double newQte, double oldQte, String action, String mouvement, Date date, long lot) {
        return dao.controleStock(article, conditionnement, depot, tranche, newQte, oldQte, action, mouvement, date, lot);
    }

    public String controleStock(long article, long conditionnement, long oldCond, long depot, long tranche, double newQte, double oldQte, String action, String mouvement, Date date, long lot) {
        return dao.controleStock(article, conditionnement, oldCond, depot, tranche, newQte, oldQte, action, mouvement, date, lot);
    }

}
