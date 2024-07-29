/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.Fonctions;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaAbonementDocDivers;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaJustifBonMission;
import yvs.entity.compta.YvsComptaParametre;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.divers.YvsComptaCentreDocDivers;
import yvs.entity.compta.divers.YvsComptaCoutSupDocDivers;
import yvs.entity.compta.divers.YvsComptaTaxeDocDivers;
import yvs.entity.compta.saisie.YvsComptaContentAnalytique;
import yvs.entity.compta.saisie.YvsComptaContentJournalAbonnementDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalDocDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceDivers;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidDocCaisse;
import yvs.entity.users.YvsBaseUsersAcces;
import yvs.entity.users.YvsUsersAgence;
import yvs.service.AbstractEntity;
import yvs.service.IEntitySax;
import yvs.service.com.stocks.AYvsComDocStocks;
import yvs.service.param.workflow.IYvsWorkflowValidDocCaisse;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComptaCaisseDocDivers extends AbstractEntity {

    public AYvsComptaCaisseDocDivers() {
    }

    public AYvsComptaCaisseDocDivers(DaoInterfaceWs dao) {
        this.dao = dao;
    }
    IEntitySax IEntitiSax = new IEntitySax();
    String[] champ;
    Object[] val;
    YvsComptaParametre currentParam = new YvsComptaParametre();
    private String tabIds, model, docIds, rowIds, groupBy = "C";
    private List<YvsComptaPiecesComptable> listePiece;
    public static DateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
    public Fonctions fonction = new Fonctions();
    private YvsComptaCaissePieceDivers pieceCD = new YvsComptaCaissePieceDivers();
    private YvsBaseModeReglement modeByEspece = null;

    public ResultatAction<YvsComptaCaisseDocDivers> controle(YvsComptaCaisseDocDivers entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getAgence().getId() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Aucune agence n'a été trouvé !");
            }
            if (entity.getMontant() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le montant !");
            }
            if (!asString(entity.getDescription())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le motif de ce document de caisse!");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComptaCaisseDocDivers.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComptaCaisseDocDivers> save(YvsComptaCaisseDocDivers entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                List<YvsWorkflowEtapeValidation> etapes = new ArrayList<>();
                List<YvsComptaCaissePieceDivers> reglements = new ArrayList<>(entity.getReglements());
                List<YvsWorkflowValidDocCaisse> etape_facture = new ArrayList<>(entity.getEtapesValidations());
                YvsAgences agence = (YvsAgences) dao.loadOneByNameQueries("YvsAgences.findById", new String[]{"id"}, new Object[]{entity.getAgence().getId()});
                String nature = "";
                if (entity.getMouvement().equals("D")) {
                    nature = "DEPENSE";
                } else {
                    nature = "RECETTE";
                }
                if (entity.getTypeDoc() != null ? entity.getTypeDoc().getId() > 0 : false) {
                    etapes = dao.loadNameQueries("YvsWorkflowEtapeValidation.findByModelNatureAndType", new String[]{"titre", "nature", "typeDoc", "societe"}, new Object[]{Constantes.DOCUMENT_DOC_DIVERS_CAISSE, nature, entity.getTypeDoc(), agence.getSociete()});
                } else {
                    etapes = dao.loadNameQueries("YvsWorkflowEtapeValidation.findByModelNature", new String[]{"titre", "nature", "societe"}, new Object[]{Constantes.DOCUMENT_DOC_DIVERS_CAISSE, nature, agence.getSociete()});
                }

                entity.setEtapeTotal(etape_facture.isEmpty() ? etapes.size() : etape_facture.size());
                entity.setReferenceExterne(entity.getNumPiece());
                String num = "";
                if (entity.getMouvement().equals("D")) {
                    num = dao.genererReference(Constantes.TYPE_OD_DEPENSE_NAME, entity.getDateDoc(), 0, agence.getSociete(), agence);
                } else {
                    num = dao.genererReference(Constantes.TYPE_OD_RECETTE_NAME, entity.getDateDoc(), 0, agence.getSociete(), agence);
                }
                if (num.trim().length() > 0) {
                    entity.setNumPiece(num);
                }
                entity.setId(null);

                entity.setEtapesValidations(null);
                entity.setReglements(null);
                entity.setDocuments(null);
                entity.setAbonnements(null);
                entity.setCouts(null);
                entity.setTaxes(null);
                entity.setSections(null);
                entity.setTiers(null);
                entity.setRetenues(null);
                entity.setBonsProvisoire(null);
                entity = (YvsComptaCaisseDocDivers) dao.save1(entity);

                if (etape_facture != null ? !etape_facture.isEmpty() : false) {
                    IYvsWorkflowValidDocCaisse impl = (IYvsWorkflowValidDocCaisse) IEntitiSax.createInstance("IYvsWorkflowValidDocCaisse", dao);
                    for (YvsWorkflowValidDocCaisse etape : etape_facture) {
                        etape.setDocCaisse(new YvsComptaCaisseDocDivers(entity.getId()));
                        ResultatAction res = impl.save(etape);
                        if (res != null) {
                            etape = (YvsWorkflowValidDocCaisse) res.getData();
                        }
                    }
                    entity.setEtapesValidations(etape_facture);
                } else {
                    if (etapes != null ? !etapes.isEmpty() : false) {
                        entity.setEtapesValidations(saveEtapesValidation(entity, etapes));
                        for (YvsWorkflowValidDocCaisse etap : entity.getEtapesValidations()) {
                            etap.setEtape(new YvsWorkflowEtapeValidation(etap.getEtape().getId()));
                        }
                    }
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaCaisseDocDivers.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaCaisseDocDivers> update(YvsComptaCaisseDocDivers entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                YvsUsersAgence author = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findById", new String[]{"id"}, new Object[]{entity.getAuthor().getId()});
                System.err.println("user = " + author.getUsers().getId());
                System.err.println("author = " + entity.getAuthor().getId());
                entity.setAuthor(author);
                dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, result.getMessage());
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaCaisseDocDivers.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaCaisseDocDivers> delete(YvsComptaCaisseDocDivers entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaCaisseDocDivers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private List<YvsWorkflowEtapeValidation> saveEtapesValidation(YvsComptaCaisseDocDivers doc) {
        if (doc.getTypeDoc() == null) {
            String[] champ = new String[]{"titre", "nature", "societe"};
            Object[] val = new Object[]{Constantes.DOCUMENT_DOC_DIVERS_CAISSE, doc.getMouvement().equals("D") ? Constantes.DEPENSE : Constantes.RECETTE, currentAgence.getSociete()};
            return dao.loadNameQueries("YvsWorkflowEtapeValidation.findByModelNature", champ, val);
        } else {
            champ = new String[]{"titre", "nature", "societe", "typeDoc"};
            val = new Object[]{Constantes.DOCUMENT_DOC_DIVERS_CAISSE, doc.getMouvement().equals("D") ? Constantes.DEPENSE : Constantes.RECETTE, currentAgence.getSociete(), doc.getTypeDoc()};
            return dao.loadNameQueries("YvsWorkflowEtapeValidation.findByModelNatureAndType", champ, val);
        }
    }

    private List<YvsWorkflowValidDocCaisse> saveEtapesValidation(YvsComptaCaisseDocDivers doc, List<YvsWorkflowEtapeValidation> model) {
        //charge les étape de vailidation
        List<YvsWorkflowValidDocCaisse> re = new ArrayList<>();
        if (!model.isEmpty()) {
            YvsWorkflowValidDocCaisse vd;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    vd = new YvsWorkflowValidDocCaisse();
                    vd.setDateSave(new Date());
                    vd.setDateUpdate(new Date());
                    vd.setAuthor(new YvsUsersAgence(doc.getAuthor().getId()));
                    vd.setEtape(et);
                    vd.setEtapeValid(false);
                    vd.setDocCaisse(new YvsComptaCaisseDocDivers(doc.getId()));
                    IYvsWorkflowValidDocCaisse impl = (IYvsWorkflowValidDocCaisse) IEntitiSax.createInstance("IYvsWorkflowValidDocCaisse", dao);
                    ResultatAction<YvsWorkflowValidDocCaisse> wo = impl.save(vd);
                    vd = (YvsWorkflowValidDocCaisse) wo.getData();
                    re.add(vd);
                }
            }
        }
        doc.setStatutDoc(Constantes.ETAT_EDITABLE);
        dao.update(doc);
        return ordonneEtapes(re);
    }

    private List<YvsWorkflowValidDocCaisse> ordonneEtapes(List<YvsWorkflowValidDocCaisse> l) {
        List<YvsWorkflowValidDocCaisse> re = new ArrayList<>();
        YvsWorkflowValidDocCaisse first = null;
        //recherche la première étape       
        for (YvsWorkflowValidDocCaisse vm : l) {
            if (vm.getEtape().getFirstEtape() && vm.getEtape().getActif()) {
                first = vm;
                break;
            }
        }
        if (first == null ? l != null ? !l.isEmpty() : false : false) {
            Collections.sort(l, new YvsWorkflowValidDocCaisse());
            first = l.get(0);
        }

        if (first != null) {
            re.add(first);  //ajoute la première étapes au résultat
            boolean find;
            //tant qu'il existe une étape suivante active
            while ((first.getEtape().getEtapeSuivante() != null) ? (first.getEtape().getEtapeSuivante().getActif()) : false) {
                find = false;
                for (YvsWorkflowValidDocCaisse vm : l) {
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
        for (YvsWorkflowValidDocCaisse vm : re) {
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

    public ResultatAction<YvsWorkflowValidDocCaisse> validEtapeOrdre(YvsWorkflowValidDocCaisse entity) {
        ResultatAction<YvsWorkflowValidDocCaisse> result = new ResultatAction<>(false, null, null, "Action impossible!!!");
        try {
            entity = (YvsWorkflowValidDocCaisse) dao.loadOneByNameQueries("YvsWorkflowValidDocCaisse.findById", new String[]{"id"}, new Object[]{entity.getId()});
            currentAgence = entity.getDocCaisse().getAgence();
            entity.getDocCaisse().getEtapesValidations().clear();
            entity.setDocCaisse(new YvsComptaCaisseDocDivers(entity.getDocCaisse().getId()));
            result = validEtapeOrdre(entity.getDocCaisse(), entity, !entity.getEtape().getFirstEtape(), false);
        } catch (Exception e) {
            Logger.getLogger(AYvsComDocStocks.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    public ResultatAction<YvsWorkflowValidDocCaisse> validEtapeOrdre(YvsComptaCaisseDocDivers selectDoc, YvsWorkflowValidDocCaisse etape, boolean lastEtape, boolean save) {
        //vérifier que la personne qui valide l'étape a le droit 
        ResultatAction<YvsWorkflowValidDocCaisse> result = new ResultatAction<>(false, null, null, "Action Impossible !!");
        List<YvsWorkflowValidDocCaisse> list = new ArrayList<>();
        boolean continu = true;
        loadParam();
        selectDoc = (YvsComptaCaisseDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseDocDivers.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
        etape.setDocCaisse(new YvsComptaCaisseDocDivers(selectDoc.getId()));
        currentAgence = selectDoc.getAgence();
        list = dao.loadNameQueries("YvsWorkflowValidDocCaisse.findByFacture", new String[]{"facture"}, new Object[]{selectDoc});
        list = ordonneEtapes(list);
        if (continu) {
            //contrôle la cohérence des dates
            if (etape.getEtape().getEtapeSuivante() == null) {
                if (currentParam.getMontantSeuilDepenseOd() > 0 ? selectDoc.getMontant() > currentParam.getMontantSeuilDepenseOd() : false) {
                    if (!selectDoc.isSynchroniser()) {
                        if (!autoriser("compta_od_valid_max_seuil_montant")) {
                            result = new ResultatAction<>(false, null, null, "Action Impossible !!");
                        }
                    }
                }
            }
            int idx = list.indexOf(etape);
            if (idx >= 0) {
                etape.setAuthor(new YvsUsersAgence(selectDoc.getAuthor().getId()));
                etape.setDateUpdate(new Date());
                etape.setEtapeValid(true);
                etape.setEtapeActive(false);
                etape.setMotif(null);
                if (list.size() > (idx + 1)) {
                    list.get(idx + 1).setEtapeActive(true);
                }
                dao.update(etape);
                selectDoc.setStatutDoc(Constantes.ETAT_ENCOURS);
                selectDoc.setAuthor(new YvsUsersAgence(selectDoc.getAuthor().getId()));
                selectDoc.setEtapeValide(selectDoc.getEtapeValide() + 1);
                dao.update(selectDoc);

                result = new ResultatAction(true, null, null, "Succes");

                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() == null) {
                    if (validerOrder(selectDoc, false, selectDoc.getAuthor())) {
//                    this.selectDoc = selectDoc;
                    }
                }
                if (etape.getEtape().getReglementHere()) {
                    if (selectDoc.getReglements() != null ? selectDoc.getReglements().isEmpty() : true) {
                        if (selectDoc.getCaisse() != null ? selectDoc.getCaisse().getId() > 0 : false) {
                            YvsBaseModeReglement espece = modeEspece();
                            YvsComptaCaissePieceDivers piece = getPiece(null, selectDoc.getCaisse(), selectDoc, espece, selectDoc.getMontant(), selectDoc.getDateDoc());
                            if (selectDoc.getBeneficiaire() != null ? selectDoc.getBeneficiaire().trim().length() > 0 : false) {
                                piece.setBeneficiaire(selectDoc.getBeneficiaire());
                            } else {
//                            if (selectDoc.gett() != null ? docDivers.getTiers().getId() > 0 : false) {
//                                piece.setBeneficiaire(docDivers.getTiers().getNom_prenom());
//                            }
                            }
                            if (piece != null) {
                                piece.setNumeroExterne(selectDoc.getReferenceExterne());
                                piece = (YvsComptaCaissePieceDivers) dao.save1(piece);
                            }
//                            selectDoc.getReglements().add(piece);
//                            idx = selectDoc.getReglements().indexOf(piece);
//                            if (idx < 0) {
//                                selectDoc.getReglements().add(piece);
//                            }
                        }
                    }
                }
            } else {
                result = new ResultatAction<>(false, null, 0L, "Action impossible ");
            }
        }
        return result;
    }

    public void loadParam() {
        currentParam = (YvsComptaParametre) dao.loadOneByNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        if (currentParam == null) {
            currentParam = new YvsComptaParametre();
        }
    }

    public boolean validerOrder(YvsComptaCaisseDocDivers selectDoc, boolean succes, YvsUsersAgence currentUser) {
        boolean canEditable = true;
        if (canEditable ? selectDoc != null : false) {
            if (currentParam == null) {
                currentParam = (YvsComptaParametre) dao.loadOneByNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            }
            if (selectDoc.getEtapesValidations() != null ? selectDoc.getEtapesValidations().isEmpty() : true) {
                if (selectDoc.getMouvement().equals(Constantes.COMPTA_DEPENSE)) {
                    if (!selectDoc.isSynchroniser()) {
                        if (!autoriser("compta_od_valide_depense")) {
                            return false;
                        }
                    }
                    if (currentParam != null ? currentParam.getMontantSeuilDepenseOd() > 0 ? selectDoc.getMontant() > currentParam.getMontantSeuilDepenseOd() : false : false) {
                        if (!selectDoc.isSynchroniser()) {
                            if (!autoriser("compta_od_valid_max_seuil_montant")) {
                                return false;
                            }
                        }
                    }
                } else {
                    if (!selectDoc.isSynchroniser()) {
                        if (!autoriser("compta_od_valide_recette")) {
                            return false;
                        }
                    }
                    if (currentParam != null ? currentParam.getMontantSeuilRecetteOd() > 0 ? selectDoc.getMontant() > currentParam.getMontantSeuilRecetteOd() : false : false) {
                        if (!selectDoc.isSynchroniser()) {
                            if (!autoriser("compta_od_valid_max_seuil_montant")) {
                                return false;
                            }
                        }
                    }
                }
            }
            selectDoc.setDateValider(new Date());
            selectDoc.setValiderBy(currentUser.getUsers());
            selectDoc.setStatutDoc(Constantes.ETAT_VALIDE);
            selectDoc.setDateUpdate(new Date());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                selectDoc.setAuthor(currentUser);
            }
            dao.update(selectDoc);
//            if (documents.contains(selectDoc)) {
//                documents.set(documents.indexOf(selectDoc), selectDoc);
//            }
            selectDoc.setTotalPlanifie(giveSoePlanifie(selectDoc.getReglements()));
            if (selectDoc != null) {
                selectDoc.setTotalPlanifie(selectDoc.getTotalPlanifie());
                selectDoc.setStatutDoc(Constantes.ETAT_VALIDE);
            }
            if (currentParam.getMajComptaAutoDivers() ? currentParam.getMajComptaStatutDivers().equals(Constantes.STATUT_DOC_VALIDE) : false) {
                comptabiliserDivers(selectDoc, true, false, currentUser);

            }
//                equilibreOne(selectDoc);
            Collections.sort(selectDoc.getReglements(), new YvsComptaCaissePieceDivers());
            //Si la pièce est le justificatif d'un bon provisoire
            for (YvsComptaCaissePieceDivers p : selectDoc.getReglements()) {
                if (!p.getStatutPiece().equals(Constantes.STATUT_DOC_ANNULE)) {
                    if (p.isJustificatif() ? p.getJustify().getBon().getStatutPaiement().equals(Constantes.STATUT_DOC_PAYER) : false) {
                        if (!p.getJustify().getBon().getStatutJustify().equals(Constantes.STATUT_DOC_JUSTIFIER)) {
                            validePc(p, selectDoc, false, currentUser);
                        }
                    }
                }
            }
//            update("zone_txt_resteDD");
//            if (succes) {
//                succes();
//            }
            return true;
        }

        return false;
    }

    public double giveSoePlanifie(List<YvsComptaCaissePieceDivers> lp) {
        double re = 0;
        if (lp != null) {
            for (YvsComptaCaissePieceDivers c : lp) {
                if (c.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU && c.getStatutPiece() != Constantes.STATUT_DOC_ANNULE) {
                    re += c.getMontant();
                }
            }
        }
        return re;
    }

    public boolean comptabiliserDivers(YvsComptaCaisseDocDivers y, boolean msg, boolean succes, YvsUsersAgence currentUser) {
        List<YvsComptaAbonementDocDivers> abonnements = dao.loadNameQueries("YvsComptaAbonementDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaCentreDocDivers> sectionsAnals = dao.loadNameQueries("YvsComptaCentreDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaTaxeDocDivers> taxes = dao.loadNameQueries("YvsComptaTaxeDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaCoutSupDocDivers> couts = dao.loadNameQueries("YvsComptaCoutSupDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        if (y.getCompteGeneral() != null ? y.getCompteGeneral().getSaisieAnalytique() && sectionsAnals.isEmpty() : false) {
//            getErrorMessage("Vous devez effectuer la répartition analytique !");
            return false;
        }
        List<YvsComptaContentJournal> contenus = fonction.buildDiversToComptabilise(y.getId(), abonnements, sectionsAnals, taxes, couts, dao);
        if (contenus != null ? contenus.isEmpty() : true) {
            if (msg) {
//                getErrorMessage(dao.getRESULT());
            }
            return false;
        }
        return comptabiliserDivers(y, contenus, abonnements, sectionsAnals, taxes, couts, msg, succes, currentUser);
    }

    public boolean comptabiliserDivers(YvsComptaCaisseDocDivers y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes, YvsUsersAgence currentUser) {
        List<YvsComptaAbonementDocDivers> abs = dao.loadNameQueries("YvsComptaAbonementDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaCentreDocDivers> secs = dao.loadNameQueries("YvsComptaCentreDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaTaxeDocDivers> taxs = dao.loadNameQueries("YvsComptaTaxeDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaCoutSupDocDivers> couts = dao.loadNameQueries("YvsComptaCoutSupDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        return comptabiliserDivers(y, contenus, abs, secs, taxs, couts, msg, succes, currentUser);
    }

    private boolean comptabiliserDivers(List<YvsComptaContentJournal> contenus, boolean msg, YvsComptaCaisseDocDivers y, YvsUsersAgence currentUser) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;
        if (ids != null ? !ids.isEmpty() : false) {
            boolean error = false;
            List<YvsComptaContentJournal> list;
            for (int idx : ids) {
                list = new ArrayList<>();
                for (YvsComptaContentJournal c : contenus) {
                    if (c.getRefExterne().equals(y.getId())) {
                        list.add(c);
                    }
                }
                if (!list.isEmpty()) {
                    if (comptabiliserDivers(y, list, msg, false, currentUser)) {
                        succes = true;
                    } else {
                        error = true;
                    }
                }
            }
            docIds = "";
            if (error) {
//                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
            }
//                update("data_doc_piece_divers");

        } else {
            succes = comptabiliserDivers(y, contenus, msg, false, currentUser);
        }
        return succes;
    }

    private boolean comptabiliserDivers(YvsComptaCaisseDocDivers y, List<YvsComptaContentJournal> contenus, List<YvsComptaAbonementDocDivers> abs, List<YvsComptaCentreDocDivers> secs, List<YvsComptaTaxeDocDivers> taxs, List<YvsComptaCoutSupDocDivers> couts, boolean msg, boolean succes, YvsUsersAgence currentUser) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
                    boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_DIVERS);
                    if (comptabilise) {
                        if (msg) {
//                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaDivers(y.getId(), contenus, abs, secs, taxs, couts, msg, currentUser);
                    int idx = listePiece.indexOf(p);
                    if (idx > -1) {
                        listePiece.set(idx, p);
                    } else {
                        listePiece.add(0, p);
                    }
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);
                        if (succes) {
//                            succes();
                        }
                    }
                    return reponse;
                } else {
                    System.err.println(" Système error here");
//                    getErrorMessage("Comptabilisation impossible... car cette opération n'est pas validée");
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

    private YvsComptaPiecesComptable majComptaDivers(long id, List<YvsComptaContentJournal> contenus, List<YvsComptaAbonementDocDivers> abs, List<YvsComptaCentreDocDivers> secs, List<YvsComptaTaxeDocDivers> taxs, List<YvsComptaCoutSupDocDivers> couts, boolean msg, YvsUsersAgence currentUser) {
        YvsComptaPiecesComptable p = null;
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (msg) {
//                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaCaisseDocDivers y = (YvsComptaCaisseDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseDocDivers.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                champ = new String[]{"agence", "type", "default"};
                val = new Object[]{y.getAgence(), Constantes.TRESORERIE, true};
                YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    p = saveNewPieceComptable(y.getDateDoc(), jrn, contenus, msg, currentUser);
                    if (p != null ? p.getId() < 1 : true) {
                        return p;
                    }
                    YvsComptaContentJournalDocDivers c = new YvsComptaContentJournalDocDivers(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);

                    List<YvsComptaCaissePieceDivers> pieces = dao.loadNameQueries("YvsComptaCaissePieceDivers.findByDocDiversStatut", new String[]{"docDivers", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                    for (YvsComptaCaissePieceDivers r : pieces) {
                        comptabiliserCaisseDivers(r, contenus, false, false, currentUser);
                    }
                    for (YvsComptaAbonementDocDivers r : abs) {
                        comptabiliserAbonnementDivers(r, fonction.buildDiversToComptabilise(r, secs, taxs, couts, dao), false, false, currentUser);
                    }

                    //Debut du lettrage
                    lettrerDivers(y, pieces, currentUser);
                    y.setComptabilise(true);
                } else {
                    if (msg) {
//                        getErrorMessage("Comptabilisation impossible...car le journal par defaut de trésorerie n'existe pas");
                    }
                }
            }
        }
        return p;
    }

    public double giveSoePieces(List<YvsComptaContentJournal> contenus) {
        double re, cr = 0, db = 0;
        for (YvsComptaContentJournal cc : contenus) {
            db += cc.getDebit();
            cr += cc.getCredit();
        }
        re = db - cr;
        return arrondi(re, 3);
    }

    public static double arrondi(double d, int l) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(l, BigDecimal.ROUND_DOWN);
        double r = bd.doubleValue();
        return r;
    }

    private YvsComptaPiecesComptable saveNewPieceComptable(Date dateDoc, YvsComptaJournaux jrn, List<YvsComptaContentJournal> contenus, boolean msg, YvsUsersAgence currentUser) {
        try {
            if (contenus != null ? !contenus.isEmpty() : false) {
                if (jrn != null ? jrn.getId() < 1 : true) {
                    if (msg) {
//                        getErrorMessage("Comptabilisation impossible...car le journal de comptabilisation n'existe pas");
                    }
                    return null;
                }
                dateDoc = dateDoc != null ? dateDoc : new Date();
                YvsBaseExercice exo = controleExercice(dateDoc, msg, currentAgence);
                if (exo != null ? exo.getId() < 1 : true) {
                    return null;
                }
                String num = dao.genererReference(Constantes.TYPE_PIECE_COMPTABLE_NAME, dateDoc, jrn.getId(), currentUser.getAgence().getSociete(), currentUser.getAgence());
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
//            getException("saveNewPieceComptable Error..", ex);
        }
        return null;
    }

    private boolean comptabiliserCaisseDivers(List<YvsComptaContentJournal> contenus, boolean msg, List<YvsComptaCaissePieceDivers> listPieces, YvsComptaCaissePieceDivers p, YvsUsersAgence currentUser) {
        List<Integer> ids = decomposeSelection(docIds);
        boolean succes = false;
        if (ids != null ? !ids.isEmpty() : false) {
            boolean error = false;
            List<YvsComptaContentJournal> list;
            for (int idx : ids) {
                YvsComptaCaissePieceDivers y = listPieces.get(idx);
                list = new ArrayList<>();
                for (YvsComptaContentJournal c : contenus) {
                    if (c.getRefExterne().equals(y.getId())) {
                        list.add(c);
                    }
                }
                if (!list.isEmpty()) {
                    if (comptabiliserCaisseDivers(y, list, false, false, currentUser)) {
                        succes = true;
                    } else {
                        error = true;
                    }
                }
            }
            docIds = "";
            if (error) {
//                    getWarningMessage("Certains documents n'ont pas pu etre comptabilisés");
            }
//                update("data_doc_piece_caisse_divers");

        } else {
            succes = comptabiliserCaisseDivers(p, contenus, msg, false, currentUser);
        }
        return succes;
    }

    private boolean comptabiliserCaisseDivers(YvsComptaCaissePieceDivers y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes, YvsUsersAgence currentUser) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_CAISSE_DIVERS);
                    if (comptabilise) {
                        if (msg) {
//                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaCaisseDivers(y.getId(), contenus, currentUser);
                    int idx = listePiece.indexOf(p);
                    if (idx > -1) {
                        listePiece.set(idx, p);
                    } else {
                        listePiece.add(0, p);
                    }
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);
//                        if (succes) {
//                            succes();
//                        }
                    }
                    return reponse;
                } else {
//                    getErrorMessage("Comptabilisation impossible... car cette pièce de caisse n'est pas validée");
                }
            }
        }
        return false;
    }

    private YvsComptaPiecesComptable majComptaCaisseDivers(long id, List<YvsComptaContentJournal> contenus, YvsUsersAgence currentUser) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (true) {
//                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaCaissePieceDivers y = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus, true, currentUser);
                    if (p != null ? p.getId() < 1 : true) {
                        return p;
                    }
                    YvsComptaContentJournalPieceDivers c = new YvsComptaContentJournalPieceDivers(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);

                    //Debut du lettrage
                    lettrerCaisseDivers(y, currentUser);
                    y.setComptabilise(true);
                    return p;
                } else {
//                    getErrorMessage("Comptabilisation impossible...car ce reglement n'est associé à aucune caisse");
                }
            }
        }
        return null;
    }

    public List<YvsComptaContentJournal> lettrerCaisseDivers(YvsComptaCaissePieceDivers y, YvsUsersAgence currentUser) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        String isLettrer = null;
        List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceDivers.findIdByDocDiversStatut", new String[]{"docDivers", "statut"}, new Object[]{y.getDocDivers(), Constantes.STATUT_DOC_PAYER});
        if (ids != null ? !ids.isEmpty() : false) {
            List<YvsComptaContentJournal> debits = new ArrayList<>();
            List<YvsComptaContentJournal> credits = new ArrayList<>();
            if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_DIVERS});
            } else {
                debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_DIVERS});
            }
            if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE) ? (credits != null ? !credits.isEmpty() : false) : (debits != null ? !debits.isEmpty() : false)) {
                if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                    debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExterne", new String[]{"id", "table"}, new Object[]{y.getDocDivers().getId(), Constantes.SCR_DIVERS});
                    if (asString(credits.get(0).getLettrage())) {
                        isLettrer = credits.get(0).getLettrage();
                    }
                } else {
                    credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{y.getDocDivers().getId(), Constantes.SCR_DIVERS});
                    if (asString(debits.get(0).getLettrage())) {
                        isLettrer = debits.get(0).getLettrage();
                    }
                }
                if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE) ? !debits.isEmpty() : !credits.isEmpty()) {
                    if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                        if (asString(debits.get(0).getLettrage())) {
                            isLettrer = debits.get(0).getLettrage();
                        }
                    } else {
                        if (asString(credits.get(0).getLettrage())) {
                            isLettrer = credits.get(0).getLettrage();
                        }
                    }
                    if (!asString(isLettrer)) {
                        List<YvsComptaContentJournal> list = new ArrayList<>();
                        list.addAll(debits);
                        list.addAll(credits);
                        YvsComptaPiecesComptable x = new YvsComptaPiecesComptable(list);
                        if (x.getSolde() == 0) {
                            lettrageCompte(list, currentUser);
                        }
                    }
                }
            }
            if (asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentAgence.getSociete(), isLettrer});
            }
        }
        return result;
    }

    private boolean comptabiliserAbonnementDivers(YvsComptaAbonementDocDivers y, List<YvsComptaContentJournal> contenus, boolean msg, boolean succes, YvsUsersAgence currentUser) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getDocDivers().getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
                    boolean comptabilise = dao.isComptabilise(y.getDocDivers().getId(), Constantes.SCR_DIVERS);
                    if (!comptabilise) {
                        if (msg) {
//                            getErrorMessage("Comptabilisation impossible... car l'opération diverse n'est pas encore comptabilisée");
                        }
                        return false;
                    }

                    comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_ABONNEMENT_DIVERS);
                    if (comptabilise) {
                        if (msg) {
//                            getErrorMessage("Comptabilisation impossible... car ce document est déjà comptabilisée");
                        }
                        return false;
                    }
                    YvsComptaPiecesComptable p = majComptaAbonnementDivers(y.getId(), contenus, currentUser);
                    int idx = listePiece.indexOf(p);
                    if (idx > -1) {
                        listePiece.set(idx, p);
                    } else {
                        listePiece.add(0, p);
                    }
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (reponse) {
                        y.setComptabilised(true);
//                        if (succes) {
//                            succes();
//                        }
                    }
                    return reponse;
                } else {
//                    getErrorMessage("Comptabilisation impossible... car cet abonnement est rattaché à un document qui n'est pas validée");
                }
            }
        }
        return false;
    }

    private YvsComptaPiecesComptable majComptaAbonnementDivers(long id, List<YvsComptaContentJournal> contenus, YvsUsersAgence currentUser) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                if (true) {
//                    getErrorMessage("Comptabilisation impossible...", "La piece n'est pas équilibrée");
                }
                return null;
            }
            YvsComptaAbonementDocDivers y = (YvsComptaAbonementDocDivers) dao.loadOneByNameQueries("YvsComptaAbonementDocDivers.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                champ = new String[]{"agence", "type", "default"};
                val = new Object[]{y.getDocDivers().getAgence(), Constantes.TRESORERIE, true};
                YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    YvsComptaPiecesComptable p = saveNewPieceComptable(y.getEcheance(), jrn, contenus, true, currentUser);
                    if (p != null ? p.getId() < 1 : true) {
                        return p;
                    }
                    YvsComptaContentJournalAbonnementDivers c = new YvsComptaContentJournalAbonnementDivers(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);
                    y.setComptabilise(true);
                    return p;
                } else {
//                    getErrorMessage("Comptabilisation impossible... le journal de tresorerie n'est pas paramétré");
                }
            }
        }
        return null;
    }

    public List<YvsComptaContentJournal> lettrerDivers(YvsComptaCaisseDocDivers y, YvsUsersAgence currentUser) {
        List<YvsComptaCaissePieceDivers> pieces = dao.loadNameQueries("YvsComptaCaissePieceDivers.findByDocDiversStatut", new String[]{"docDivers", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
        return lettrerDivers(y, pieces, currentUser);
    }

    public List<YvsComptaContentJournal> lettrerDivers(YvsComptaCaisseDocDivers y, List<YvsComptaCaissePieceDivers> pieces, YvsUsersAgence currentUser) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        if (pieces != null ? !pieces.isEmpty() : false) {
            String isLettrer = null;
            List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceDivers.findIdByDocDiversStatut", new String[]{"docDivers", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
            List<YvsComptaContentJournal> debits = new ArrayList<>();
            List<YvsComptaContentJournal> credits = new ArrayList<>();
            if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_DIVERS});
            } else {
                debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_DIVERS});
            }
            if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE) ? (credits != null ? !credits.isEmpty() : false) : (debits != null ? !debits.isEmpty() : false)) {
                if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                    debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExterne", new String[]{"id", "table"}, new Object[]{y.getId(), Constantes.SCR_DIVERS});
                    if (asString(credits.get(0).getLettrage())) {
                        isLettrer = credits.get(0).getLettrage();
                    }
                } else {
                    credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{y.getId(), Constantes.SCR_DIVERS});
                    if (asString(debits.get(0).getLettrage())) {
                        isLettrer = debits.get(0).getLettrage();
                    }
                }
                if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE) ? !debits.isEmpty() : !credits.isEmpty()) {
                    if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                        if (asString(debits.get(0).getLettrage())) {
                            isLettrer = debits.get(0).getLettrage();
                        }
                    } else {
                        if (asString(credits.get(0).getLettrage())) {
                            isLettrer = credits.get(0).getLettrage();
                        }
                    }
                    if (!asString(isLettrer)) {
                        List<YvsComptaContentJournal> list = new ArrayList<>();
                        list.addAll(debits);
                        list.addAll(credits);
                        YvsComptaPiecesComptable x = new YvsComptaPiecesComptable(list);
                        if (x.getSolde() == 0) {
                            lettrageCompte(list, currentUser);
                        }
                    }
                }
            }
            if (asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentAgence.getSociete(), isLettrer});
            }
        }
        return result;
    }

    public boolean validePc(YvsComptaCaissePieceDivers y, YvsComptaCaisseDocDivers selectDoc, boolean msg, YvsUsersAgence currentUser) {
        pieceCD = y;
        if (controleValeurMensualite(y, selectDoc, msg, currentUser)) {
            //le document divers doit être valide
//            int idx = documents.indexOf(selectDoc);
//            if (idx > -1) {
//                selectDoc.setStatutDoc(documents.get(idx).getStatutDoc());
//            }
            if (selectDoc.getStatutDoc().equals(Constantes.ETAT_VALIDE) || selectDoc.getStatutDoc().equals(Constantes.ETAT_CLOTURE)) {
                if (y.getCaisse() != null ? y.getCaisse().getId() < 1 : true) {
                    if (msg) {
//                        getErrorMessage("Vous devez preciser la caisse");
                    }
                    return false;
                }
                if (!controleAccesCaisse(y.getCaisse(), msg, currentUser)) {
                    return false;
                }
                if (pieceCD.getJustify() != null ? (pieceCD.getJustify().getId() > 0 ? pieceCD.getJustify().getBon() != null : false) : false) {
                    if (!pieceCD.getJustify().getBon().getStatutPaiement().equals(Constantes.ETAT_REGLE)) {
                        if (msg) {
//                            getErrorMessage("Le bon provisoire rattaché n'est pas encore payé");
                        }
                        return false;
                    }
                }
                pieceCD.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                pieceCD.setValiderBy(currentUser.getUsers());
                pieceCD.setDateUpdate(new Date());
                pieceCD.setDateAnnuler(null);
                pieceCD.setAnnulerBy(null);
                if (pieceCD.getModePaiement() == null) {
                    pieceCD.setModePaiement(modeEspece());
                }
                dao.update(pieceCD);
                selectDoc.setTotalPlanifie(giveSoePlanifie(selectDoc.getReglements()));
                if (dao.isComptabilise(y.getDocDivers().getId(), Constantes.SCR_DIVERS)) {
                    comptabiliserCaisseDivers(y, null, msg, msg, currentUser);
                    if (pieceCD.getJustify() != null ? pieceCD.getJustify().getId() > 0 : false) {
                        verifyToJustify(pieceCD.getJustify().getBon(), currentUser);

                    }

                    equilibreOne(y.getDocDivers(), false);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public void equilibreOne(YvsComptaCaisseDocDivers doc) {
        equilibreOne(doc, true);
    }

    public void equilibreOne(YvsComptaCaisseDocDivers doc, boolean msg) {
        if ((doc != null) ? (doc.getId() != null ? doc.getId() > 0 : false) : false) {
            dao.getEquilibreDocDivers(doc.getId());
            doc = (YvsComptaCaisseDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseDocDivers.findById", new String[]{"id"}, new Object[]{doc.getId()});

        }

    }

    public void verifyToJustify(YvsComptaBonProvisoire y, YvsUsersAgence currentUser) {
        if (y != null ? y.getId() > 0 : false) {
            double total = 0;
            List<YvsComptaCaissePieceDivers> list = dao.loadNameQueries("YvsComptaJustificatifBon.findPieceBybon", new String[]{"bon"}, new Object[]{y});
            for (YvsComptaCaissePieceDivers c : list) {
                total += c.getMontant();
            }
            if (y.getMontant() <= total) {
                y.setStatutJustify(Constantes.ETAT_JUSTIFIE);
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                dao.update(y);
            }
        }
    }

    public void verifyToJustify(YvsGrhMissions m, YvsUsersAgence currentUser) {
        if (m != null ? m.getId() > 0 : false) {
            if (m.getStatutMission().equals(Constantes.STATUT_DOC_VALIDE)) {
                setMontantTotalMission(m);
                Double montant = (Double) dao.loadObjectByNameQueries("YvsComptaCaissePieceMission.findByMissPaye", new String[]{"mission", "statut"}, new Object[]{m, Constantes.STATUT_DOC_PAYER});
                double total = montant != null ? montant : 0;
                if (m.getTotalBon() <= total) {
                    m.setBonsProvisoire(dao.loadNameQueries("YvsComptaJustifBonMission.findByMission", new String[]{"mission"}, new Object[]{m}));
                    for (YvsComptaJustifBonMission y : m.getBonsProvisoire()) {
                        y.getPiece().setStatutJustify(Constantes.ETAT_JUSTIFIE);
                        y.getPiece().setDateUpdate(new Date());
                        y.getPiece().setAuthor(currentUser);
                        dao.update(y.getPiece());
                    }
                }
            }
        }
    }

    public YvsBaseModeReglement modeEspece() {
        if (dao != null ? modeByEspece == null : false) {
            String nameQueri = "YvsBaseModeReglement.findByDefault";
            champ = new String[]{"societe", "actif", "type", "defaut"};
            val = new Object[]{currentAgence.getSociete(), true, Constantes.MODE_PAIEMENT_ESPECE, true};
            modeByEspece = (YvsBaseModeReglement) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (modeByEspece != null ? (modeByEspece.getId() != null ? modeByEspece.getId() < 1 : true) : true) {
                nameQueri = "YvsBaseModeReglement.findByTypeActif";
                champ = new String[]{"societe", "actif", "type"};
                val = new Object[]{currentAgence.getSociete(), true, Constantes.MODE_PAIEMENT_ESPECE};
                modeByEspece = (YvsBaseModeReglement) dao.loadOneByNameQueries(nameQueri, champ, val);
            }
        }
        return modeByEspece;
    }

    public boolean controleAccesCaisse(YvsBaseCaisse caisse, boolean msg, YvsUsersAgence currentUser) {
        if (caisse != null ? caisse.getId() < 1 : true) {
//            if (msg) {
//                getErrorMessage("Vous devez précisez la caisse");
//            }
            return false;
        }
        caisse = (YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{caisse.getId()});
        if (currentUser.getUsers() != null ? currentUser.getUsers().getId() < 1 : true) {
            if (msg) {
//                getErrorMessage("Vous ne pouvez pas effectuer cette opération.. car vous n'etes pas utilisateur");
            }
            return false;
        }
        String error = null;
//        if (currentUser.getUsers().getEmploye() != null ? currentUser.getUsers().getEmploye().getId() > 0 : false) {
//            if (caisse.getResponsable() != null ? caisse.getResponsable().getId() > 0 : false) {
//                if (!caisse.getResponsable().equals(currentUser.getUsers().getEmploye())) {
//                    error = "car vous n'etes pas responsable de cette caisse";
//                } else {
//                    return true;
//                }
//            }
//        }
        if (caisse.getCaissier() != null ? caisse.getCaissier().getId() > 0 : false) {
            if (!caisse.getCaissier().equals(currentUser.getUsers())) {
                error = "car vous n'etes pas le caissier de cette caisse";
            } else {
                return true;
            }
        }
        if (caisse.getCodeAcces() != null ? caisse.getCodeAcces().getId() < 1 : true) {
            if (!autoriser("caiss_create_piece")) {
                if (msg) {
                    error = error != null ? error : "car vous n'avez pas le droit";
//                    getErrorMessage("Vous ne pouvez pas effectuer cette opération.. " + error);
                }
                return false;
            }
        } else {
            YvsBaseUsersAcces acces = (YvsBaseUsersAcces) dao.loadOneByNameQueries("YvsBaseUsersAcces.findOne", new String[]{"users", "code"}, new Object[]{currentUser.getUsers(), caisse.getCodeAcces()});
            if (acces != null ? acces.getId() < 1 : true) {
                if (msg) {
//                    getErrorMessage("Vous ne pouvez pas effectuer cette opération.. car vous n'avez pas le droit d'action sur cette caisse");
                }
                return false;
            }
        }
        return true;
    }

    public boolean controleValeurMensualite(YvsComptaCaissePieceDivers bean, YvsComptaCaisseDocDivers docDivers, YvsUsersAgence currentUser) {
        return controleValeurMensualite(bean, docDivers, true, currentUser);
    }

    public boolean controleValeurMensualite(YvsComptaCaissePieceDivers bean, YvsComptaCaisseDocDivers docDivers, boolean msg, YvsUsersAgence currentUser) {
        if (bean.getModePaiement() != null ? bean.getModePaiement().getId() <= 0 : true) {
//            getErrorMessage("Aucun mode de paiement n'a été renseigné pour cette pièce");
            return false;
        }
        if ((bean.getDocDivers() != null) ? ((bean.getDocDivers().getId() != null) ? bean.getDocDivers().getId() <= 0 : false) : true) {
            if (msg) {
//                getErrorMessage("Vous devez Enregistrer la pièce source");
            }
            return false;
        }
        if (bean.getMontant() < 1) {
            if (msg) {
//                getErrorMessage("Vous devez entrer un montant");
            }
            return false;
        }
        double mtant = 0;
        for (YvsComptaCaissePieceDivers m : docDivers.getReglements()) {
            if (m.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU) {
                mtant += m.getMontant();
            }
        }
        if (mtant > (docDivers.getMontantTotal() + docDivers.getCout())) {
            if (msg) {
//                getErrorMessage("La somme des montants des mensualités doit être égale au montant du document!");
            }
            return false;
        }
        return (giveExerciceActif(bean.getDatePiece(), currentUser) != null);
    }

    public YvsBaseExercice giveExerciceActif(Date date, YvsUsersAgence currentUser) {
        YvsBaseExercice currentExo = new YvsBaseExercice();
        if (date == null) {
            date = new Date();
        }
        if (currentExo != null) {
            if ((currentExo.getDateDebut().before(date) || currentExo.getDateDebut().equals(giveOnlyDate(date)))
                    && (currentExo.getDateFin().after(date) || currentExo.getDateFin().equals(giveOnlyDate(date)))) {
                return currentExo;
            }
        }
        if (currentUser != null) {
            currentExo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByActif", new String[]{"actif", "societe", "date"}, new Object[]{true, currentUser.getAgence().getSociete(), date});
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("exo", currentExo);
            return currentExo;
        } else {
            return null;
        }

    }

    public static Date giveOnlyDate(Date begin) {
        Calendar d1 = Calendar.getInstance();
        if (begin != null) {
            d1.setTime(begin);
        }
        d1.set(Calendar.HOUR, 0);
        d1.set(Calendar.MINUTE, 0);
        d1.set(Calendar.SECOND, 0);
        d1.set(Calendar.MILLISECOND, 0);
        return d1.getTime();
    }

    public YvsGrhMissions setMontantTotalMission(YvsGrhMissions mi) {
        //total des bons planifié rattaché à la mission
        Double montant = (Double) dao.loadObjectByNameQueries("YvsComptaJustifBonMission.getTotalBon", new String[]{"mission", "statut"}, new Object[]{mi, Constantes.ETAT_ANNULE});
        mi.setTotalBon(montant != null ? montant : 0d);
//total des bons déjà payé rattaché à la mission
        montant = (Double) dao.loadObjectByNameQueries("YvsComptaJustifBonMission.getTotalBonPaye", new String[]{"mission", "statut", "statutP"}, new Object[]{mi, Constantes.ETAT_ANNULE, Constantes.ETAT_REGLE});
        mi.setTotalBonPaye(montant != null ? montant : 0d);
        //total des pièce de caisse mission
        montant = (Double) dao.loadObjectByNameQueries("YvsComptaCaissePieceMission.findTotalPieceByMission", new String[]{"mission", "statut"}, new Object[]{mi, Constantes.STATUT_DOC_ANNULE});
        mi.setTotalPiece(montant != null ? montant : 0d);
        //total des pièce de caisse mission dejà payé
        montant = (Double) dao.loadObjectByNameQueries("YvsComptaCaissePieceMission.findTotalPiecePayeByMission", new String[]{"mission", "statut"}, new Object[]{mi, Constantes.STATUT_DOC_PAYER});
        mi.setTotalPiecePaye(montant != null ? montant : 0d);
        mi.setTotalRegle(mi.getTotalBonPaye() + mi.getTotalPiecePaye());
        montant = (Double) dao.loadObjectByNameQueries("YvsGrhFraisMission.sumByMission", new String[]{"mission"}, new Object[]{mi});
        mi.setTotalFraisMission(montant != null ? montant : 0d);
        mi.setTotalResteAPlanifier(mi.getTotalFraisMission() - mi.getTotalPiece() - mi.getTotalBon());
        mi.setTotalReste(mi.getTotalFraisMission() - mi.getTotalRegle());

        return mi;
    }

    public YvsComptaCaissePieceDivers getPiece(Long id, YvsBaseCaisse caisse, YvsComptaCaisseDocDivers selectDoc, YvsBaseModeReglement mode, double montant, Date date) {
        YvsComptaCaissePieceDivers piece = new YvsComptaCaissePieceDivers(id);
        piece.setModePaiement(mode);
        piece.setAuthor(selectDoc.getAuthor());
        piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        piece.setMontant(montant);
        piece.setDatePiece(selectDoc.getDateDoc());
        piece.setDatePaimentPrevu(date);
        piece.setMouvement(selectDoc.getMouvement());
        String numero = dao.genererReference(Constantes.TYPE_PC_DIVERS_NAME, piece.getDatePiece(), selectDoc.getSociete().getId(), selectDoc.getSociete(), currentAgence);
        if (numero != null ? numero.trim().length() < 1 : true) {
            return null;
        }
        piece.setNumPiece(numero);
        piece.setDocDivers(selectDoc);
        piece.setDateSave(new Date());
        piece.setDateUpdate(new Date());
        piece.setNote("REGLEMENT OD N° " + selectDoc.getNumPiece() + " DU " + formatDate.format(piece.getDatePiece()));
        if (caisse != null ? caisse.getId() > 0 : false) {
            piece.setCaisse(new YvsBaseCaisse(caisse.getId(), caisse.getIntitule()));
        }
        piece.setBeneficiaire(selectDoc.getBeneficiaire());
        return piece;
    }

}
