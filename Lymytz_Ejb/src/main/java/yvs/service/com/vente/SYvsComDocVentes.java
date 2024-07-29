/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.vente;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.Options;
import yvs.dao.Util;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.YvsComCommercialPoint;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.creneau.YvsComCreneauPoint;
import yvs.entity.commercial.vente.YvsComCommercialVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComDocVentesInformations;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidFactureVente;
import yvs.service.param.workflow.IYvsWorkflowValidFactureVente;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsComDocVentes extends AYvsComDocVentes implements IYvsComDocVentes {

    public SYvsComDocVentes() {
    }

    public SYvsComDocVentes(DaoInterfaceWs dao) {
        super(dao);
    }

    @Override
    public ResultatAction<YvsComDocVentes> save(YvsComDocVentes entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = null;
                if (Util.asString(entity.getAdresseServeur()) && !Util.isLocalhost(entity.getAdresseServeur())) {
                    local = getLocalCurrent(entity.getIdDistant(), "yvs_com_doc_ventes", entity.getAdresseServeur());
                }
                if (local != null ? local > 0 : false) {
                    entity = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    YvsAgences agence = (YvsAgences) dao.loadOneByNameQueries("YvsAgences.findById", new String[]{"id"}, new Object[]{entity.getEnteteDoc().getAgence().getId()});
                    String type = "FV";
                    switch (entity.getTypeDoc()) {
                        case Constantes.TYPE_FV:
                            type = Constantes.TYPE_FV_NAME;
                            break;
                        case Constantes.TYPE_BCV:
                            type = Constantes.TYPE_BCV_NAME;
                            break;
                        case Constantes.TYPE_FAV:
                            type = Constantes.TYPE_FAV_NAME;
                            break;
                        case Constantes.TYPE_BRV:
                            type = Constantes.TYPE_BRV_NAME;
                            break;
                    }
                    String reference = dao.genererReference(type, entity.getEnteteDoc().getDateEntete(), entity.getEnteteDoc().getCreneau().getCreneauPoint().getPoint().getId(), agence.getSociete(), agence);
                    if (!Util.asString(reference)) {
                        return new ResultatAction<>(false, entity, 0L, dao.getRESULT());
                    }
                    entity.setNumeroExterne(entity.getNumDoc());
                    entity.setNumDoc(reference);
                    List<YvsWorkflowEtapeValidation> etapes = new ArrayList<>();
//                if (!entity.isSynchroniser()) {
                    etapes = dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", new String[]{"titre", "societe"}, new Object[]{Constantes.DOCUMENT_FACTURE_VENTE, agence.getSociete()});
                    entity.setEtapeTotal(etapes.size());
//                }
                    entity.setId(null);
                    entity.getCouts().clear();
                    entity.getContenus().clear();
                    entity.getReglements().clear();
                    YvsComDocVentesInformations information = entity.getInformation();

                    List<YvsWorkflowValidFactureVente> etape_facture = new ArrayList<>(entity.getEtapesValidations());
                    entity.getEtapesValidations().clear();
                    entity.setInformation(null);
                    entity = (YvsComDocVentes) dao.save1(entity);
                    entity.setInformation(information);

                    saveCurrentCommercial(entity);
                    saveInformations(entity);
                    if (etape_facture != null ? !etape_facture.isEmpty() : false) {
                        IYvsWorkflowValidFactureVente impl = (IYvsWorkflowValidFactureVente) IEntitiSax.createInstance("IYvsWorkflowValidFactureVente", dao);
                        for (YvsWorkflowValidFactureVente etape : etape_facture) {
                            etape.setFactureVente(new YvsComDocVentes(entity.getId()));
                            impl.save(etape);
                        }
                    } else {
                        if (etapes != null ? !etapes.isEmpty() : false) {
                            entity.setEtapesValidations(saveEtapesValidation(entity, etapes));
                        }
                    }
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    result = new ResultatAction(true, entity, entity.getId(), "Succes", rebuildDocVente(entity));
                } else {
                    result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComDocVentes.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    @Override
    public ResultatAction<YvsComDocVentes> update(YvsComDocVentes entity) {
        ResultatAction result = controleUpdate(entity);
        try {
            if (result.isResult()) {
                entity.getCouts().clear();
                entity.getContenus().clear();
                entity.getReglements().clear();
                entity.getEtapesValidations().clear();
                entity.getDocuments().clear();
                dao.update(entity);
                saveInformations(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, new YvsComDocVentes(entity.getId(), entity.getNumDoc(), entity.getStatut(), entity.getStatutLivre(), entity.getStatutRegle()), entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, null, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComDocVentes.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, null, 0L, "Action Impossible");
        }
        return result;
    }

    @Override
    public ResultatAction<YvsComDocVentes> delete(YvsComDocVentes entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            if (!entity.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                return new ResultatAction<>(false, entity, 0L, "La facture doit être éditable pour être suppprimée");
            }
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComDocVentes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void saveInformations(YvsComDocVentes entity) {
        if (entity.getInformation() != null) {
            entity.getInformation().setDateUpdate(new Date());
            entity.getInformation().setFacture(new YvsComDocVentes(entity.getId(), entity.getNumDoc(), entity.getStatut(), entity.getStatutLivre(), entity.getStatutRegle()));
            if (entity.getInformation().getId() < 1) {
                entity.getInformation().setId(null);
                entity.getInformation().setDateSave(new Date());
                entity.setInformation((YvsComDocVentesInformations) dao.save1(entity.getInformation()));
                entity.getInformation().setFacture(new YvsComDocVentes(entity.getId(), entity.getNumDoc(), entity.getStatut(), entity.getStatutLivre(), entity.getStatutRegle()));
            } else {
                dao.update(entity.getInformation());
            }
        }
    }

    public void saveCurrentCommercial(YvsComDocVentes entity) {
        if (entity.getEnteteDoc() != null ? (entity.getEnteteDoc().getId() != null ? entity.getEnteteDoc().getId() > 0 : false) : false) {
            if (entity.getEnteteDoc().getCreneau() != null ? (entity.getEnteteDoc().getCreneau().getId() != null ? entity.getEnteteDoc().getCreneau().getId() > 0 : false) : false) {
                char commissionFor = 'C';
                YvsBasePointVente pv = null;
                YvsComCreneauPoint cr = entity.getEnteteDoc().getCreneau().getCreneauPoint();
                if (cr != null ? cr.getId() > 0 : false) {
                    pv = cr.getPoint();
                    if (pv != null ? pv.getId() > 0 : false) {
                        pv = (YvsBasePointVente) dao.loadOneByNameQueries("YvsBasePointVente.findById", new String[]{"id"}, new Object[]{pv.getId()});
                        if (pv != null ? pv.getId() > 0 : false) {
                            commissionFor = pv.getCommissionFor();
                        }
                    }
                }
                if (commissionFor == 'C') { //Commerciale est celui rattaché au user en cours
                    YvsComComerciale y = (YvsComComerciale) dao.loadOneByNameQueries("YvsComComerciale.findByUser", new String[]{"user"}, new Object[]{entity.getEnteteDoc().getCreneau().getUsers()});
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        YvsComCommercialVente bean = new YvsComCommercialVente();
                        bean.setFacture(new YvsComDocVentes(entity.getId()));
                        bean.setTaux(100.0);
                        bean.setResponsable(true);
                        bean.setCommercial(y);
                        saveNewCommercial(bean, false, false, entity);
                    } else {
//                        getErrorMessage("Le vendeur n'a pas de compte en tant que commercial");
                    }
                } else {
                    if (pv != null ? pv.getId() > 0 : false) {
                        double taux = pv.getCommerciaux().size() > 0 ? (100 / pv.getCommerciaux().size()) : 0;
                        YvsComCommercialVente bean;
                        for (YvsComCommercialPoint y : pv.getCommerciaux()) {
                            bean = new YvsComCommercialVente();
                            bean.setFacture(new YvsComDocVentes(entity.getId()));
                            bean.setTaux(taux);
                            bean.setResponsable(true);
                            bean.setCommercial(y.getCommercial());
                            saveNewCommercial(bean, false, true, entity);
                        }
                    }
                }
            } else {
//                getErrorMessage("Vous devez enregistrer le journal de vente");
            }
        } else {
//            getErrorMessage("Vous devez enregistrer le journal de vente");
        }
    }

    public void saveNewCommercial(YvsComCommercialVente bean, boolean responsable, boolean addTaux, YvsComDocVentes docVente) {
        ResultatAction result = controleFiche(bean, responsable, addTaux, docVente);
        try {
            if (result != null ? result.isResult() : false) {
                YvsComCommercialVente y = bean;
                if (y.getId() < 1) {
                    y.setId(null);
                    y = (YvsComCommercialVente) dao.save1(y);
                } else {
                    dao.update(y);
                }
                int idx = docVente.getCommerciaux().indexOf(y);
                if (idx > -1) {
                    docVente.getCommerciaux().set(idx, y);
                } else {
                    docVente.getCommerciaux().add(0, y);
                }

                if (bean.getResponsable() && !docVente.getClient().getSuiviComptable()) {
                    if (docVente != null && (bean.getFacture() != null ? bean.getCommercial().getTiers() != null : false)) {
                        if (docVente.getTiers() != null ? !docVente.getTiers().getId().equals(bean.getCommercial().getTiers().getId()) : true) {
                            YvsComClient tiers = null;
                            if (bean.getCommercial().getTiers().getId() > 0 ? bean.getCommercial().getTiers().getClients() != null ? !bean.getCommercial().getTiers().getClients().isEmpty() : false : false) {
                                tiers = bean.getCommercial().getTiers().getClients().get(0);
                            }
                            Options[] param = new Options[]{new Options(docVente.getId(), 1)};
                            String query = "update yvs_com_doc_ventes set tiers = null where id = ?";
                            if (tiers != null ? tiers.getId() > 0 : false) {
                                param = new Options[]{new Options(tiers.getId(), 1), new Options(docVente.getId(), 2)};
                                query = "update yvs_com_doc_ventes set tiers = ? where id = ?";
                            }
                            dao.requeteLibre(query, param);
                            if (tiers != null ? tiers.getId() > 0 : false) {
                                docVente.setTiers(new YvsComClient(tiers.getId()));
                            }
                            docVente.setTiers(new YvsComClient(docVente.getTiers().getId()));
                        }
                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ResultatAction controleFiche(YvsComCommercialVente bean, boolean responsable, boolean addTaux, YvsComDocVentes docVente) {
        ResultatAction result = new ResultatAction();
        if ((bean.getFacture() != null) ? bean.getFacture().getId() < 1 : true) {
            result = new ResultatAction(false, null, 0L, "Vous devez selectionner la facture!");
        }
        if (bean.getFacture().getStatut().equals(Constantes.ETAT_VALIDE) && (!autoriser("fv_save_doc"))) {
            result = new ResultatAction(false, null, 0L, "La facture sélectionnée est déjà validé!");

        }
        if ((bean.getCommercial() != null) ? bean.getCommercial().getId() < 1 : true) {
            result = new ResultatAction(false, null, 0L, "Vous devez selectionner le commercial!");

        }
        if (bean.getTaux() < 0 || bean.getTaux() > 100) {
            result = new ResultatAction(false, null, 0L, "Vous devez entrer la bonne valeur du taux");
        }
        if (bean.getId() < 1 && responsable) { //pas encore affecté
            bean.setResponsable(true);
        }
        double taux = bean.getTaux();
        for (YvsComCommercialVente y : docVente.getCommerciaux()) {
            if (bean.getCommercial().getId() == y.getCommercial().getId() ? bean.getId() != y.getId() : false) {
                result = new ResultatAction(false, null, 0L, "Vous avez deja associé ce commercial");

            }
            if (bean.getCommercial().getId() != y.getCommercial().getId()) {
                taux += y.getTaux();
            }
            if (bean.getId() < 1 && responsable && y.getResponsable()) {//empêche le changement du responsable
                bean.setResponsable(false);
            }
        }
        if (addTaux) {
            if (bean.getTaux() <= 0) {
                result = new ResultatAction(false, null, 0L, "Vous devez entrer la bonne valeur du taux");
            }
        } else {
            if (bean.getId() < 1 && responsable) {
                bean.setTaux(100 - taux);
            }
        }
        if (bean.getFacture().getClient() != null) {
            if (bean.getFacture().getClient().getSuiviComptable() != null) {
                if (!bean.getFacture().getClient().getSuiviComptable() && bean.getResponsable()) {
                    if (bean.getCommercial().getTiers().getClients() != null ? !bean.getCommercial().getTiers().getClients().isEmpty() : false) {
                        bean.getFacture().setTiers(new YvsComClient(bean.getCommercial().getTiers().getClients().get(0).getId()));
                    }
                }
            }
        }

        if (taux > 100) {
            result = new ResultatAction(false, null, 0L, "Le total des pourcentage ne peut pas exceder 100%");

        }
        result = new ResultatAction(true, null, 0L, "succèes");
        return result;
    }
}
