/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComCoutSupDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComMensualiteFactureAchat;
import yvs.entity.commercial.commission.YvsComCommissionVente;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.vente.YvsComCommercialVente;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComCoutSupDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComDocVentesInformations;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.commercial.vente.YvsComMensualiteFactureVente;
import yvs.entity.commercial.vente.YvsComRemiseDocVente;
import yvs.entity.commercial.vente.YvsComRistourneDocVente;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalFactureAchat;
import yvs.entity.compta.saisie.YvsComptaContentJournalFactureVente;
import yvs.entity.param.workflow.YvsWorkflowValidDocCaisse;
import yvs.entity.param.workflow.YvsWorkflowValidDocStock;
import yvs.entity.param.workflow.YvsWorkflowValidFactureAchat;
import yvs.entity.param.workflow.YvsWorkflowValidFactureVente;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.entity.produits.group.YvsBaseGroupesArticle;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
public class UtilRebuild {

    public static YvsComDocVentes reBuildDocVente(YvsComDocVentes entity) {
        return (YvsComDocVentes) rebuild(entity);
    }

    public static YvsComDocAchats reBuildDocAchat(YvsComDocAchats entity) {
        return (YvsComDocAchats) rebuild(entity);
    }

    public static YvsComContenuDocVente reBuildContenuVente(YvsComContenuDocVente entity) {
        return (YvsComContenuDocVente) rebuild(entity);
    }

    public static YvsComContenuDocAchat reBuildContenuAchat(YvsComContenuDocAchat entity) {
        return (YvsComContenuDocAchat) rebuild(entity);
    }

    public static Object rebuild(Object entity) {
        try {
            if (entity != null) {
                if (entity instanceof YvsComContenuDocVente) {
                    if (((YvsComContenuDocVente) entity).getParent() != null ? ((YvsComContenuDocVente) entity).getParent().getId() > 0 : false) {
                        ((YvsComContenuDocVente) entity).setParent(new YvsComContenuDocVente(((YvsComContenuDocVente) entity).getParent().getId()));
                    }
                    if (((YvsComContenuDocVente) entity).getDocVente() != null) {
                        YvsComDocVentes facture = new YvsComDocVentes(((YvsComContenuDocVente) entity).getDocVente().getId(), ((YvsComContenuDocVente) entity).getDocVente().getNumDoc(), ((YvsComContenuDocVente) entity).getDocVente().getStatut(), ((YvsComContenuDocVente) entity).getDocVente().getStatutLivre(), ((YvsComContenuDocVente) entity).getDocVente().getStatutRegle());
                        ((YvsComContenuDocVente) entity).setDocVente(facture);
                    }
                } else if (entity instanceof YvsComContenuDocAchat) {
                    if (((YvsComContenuDocAchat) entity).getParent() != null ? ((YvsComContenuDocAchat) entity).getParent().getId() > 0 : false) {
                        ((YvsComContenuDocAchat) entity).setParent(new YvsComContenuDocAchat(((YvsComContenuDocAchat) entity).getParent().getId()));
                    }
                    if (((YvsComContenuDocAchat) entity).getDocAchat() != null) {
                        YvsComDocAchats facture = new YvsComDocAchats(((YvsComContenuDocAchat) entity).getDocAchat().getId(), ((YvsComContenuDocAchat) entity).getDocAchat().getNumDoc(), ((YvsComContenuDocAchat) entity).getDocAchat().getStatut(), ((YvsComContenuDocAchat) entity).getDocAchat().getStatutLivre(), ((YvsComContenuDocAchat) entity).getDocAchat().getStatutRegle());
                        ((YvsComContenuDocAchat) entity).setDocAchat(facture);
                    }
                } else if (entity instanceof YvsComContenuDocStock) {
                    if (((YvsComContenuDocStock) entity).getParent() != null ? ((YvsComContenuDocStock) entity).getParent().getId() > 0 : false) {
                        ((YvsComContenuDocStock) entity).setParent(new YvsComContenuDocStock(((YvsComContenuDocStock) entity).getParent().getId()));
                    }
                    if (((YvsComContenuDocStock) entity).getDocStock() != null) {
                        YvsComDocStocks facture = new YvsComDocStocks(((YvsComContenuDocStock) entity).getDocStock().getId(), ((YvsComContenuDocStock) entity).getDocStock().getNumDoc(), ((YvsComContenuDocStock) entity).getDocStock().getStatut());
                        ((YvsComContenuDocStock) entity).setDocStock(facture);
                    }
                } else if (entity instanceof YvsComEnteteDocVente) {
                    ((YvsComEnteteDocVente) entity).setDocuments(null);
                } else if (entity instanceof YvsComDocVentesInformations) {
                    if (((YvsComDocVentesInformations) entity).getFacture() != null) {
                        YvsComDocVentes facture = new YvsComDocVentes(((YvsComDocVentesInformations) entity).getFacture().getId(), ((YvsComDocVentesInformations) entity).getFacture().getNumDoc(), ((YvsComDocVentesInformations) entity).getFacture().getStatut(), ((YvsComDocVentesInformations) entity).getFacture().getStatutLivre(), ((YvsComDocVentesInformations) entity).getFacture().getStatutRegle());
                        ((YvsComDocVentesInformations) entity).setFacture(facture);
                    }
                } else if (entity instanceof YvsComptaContentJournalFactureVente) {
                    if (((YvsComptaContentJournalFactureVente) entity).getFacture() != null) {
                        YvsComDocVentes facture = new YvsComDocVentes(((YvsComptaContentJournalFactureVente) entity).getFacture().getId(), ((YvsComptaContentJournalFactureVente) entity).getFacture().getNumDoc(), ((YvsComptaContentJournalFactureVente) entity).getFacture().getStatut(), ((YvsComptaContentJournalFactureVente) entity).getFacture().getStatutLivre(), ((YvsComptaContentJournalFactureVente) entity).getFacture().getStatutRegle());
                        ((YvsComptaContentJournalFactureVente) entity).setFacture(facture);
                    }
                } else if (entity instanceof YvsComptaContentJournalFactureAchat) {
                    if (((YvsComptaContentJournalFactureAchat) entity).getFacture() != null) {
                        YvsComDocAchats facture = new YvsComDocAchats(((YvsComptaContentJournalFactureAchat) entity).getFacture().getId(), ((YvsComptaContentJournalFactureAchat) entity).getFacture().getNumDoc(), ((YvsComptaContentJournalFactureAchat) entity).getFacture().getStatut(), ((YvsComptaContentJournalFactureAchat) entity).getFacture().getStatutLivre(), ((YvsComptaContentJournalFactureAchat) entity).getFacture().getStatutRegle());
                        ((YvsComptaContentJournalFactureAchat) entity).setFacture(facture);
                    }
                } else if (entity instanceof YvsComMensualiteFactureVente) {
                    if (((YvsComMensualiteFactureVente) entity).getFacture() != null) {
                        YvsComDocVentes facture = new YvsComDocVentes(((YvsComMensualiteFactureVente) entity).getFacture().getId(), ((YvsComMensualiteFactureVente) entity).getFacture().getNumDoc(), ((YvsComMensualiteFactureVente) entity).getFacture().getStatut(), ((YvsComMensualiteFactureVente) entity).getFacture().getStatutLivre(), ((YvsComMensualiteFactureVente) entity).getFacture().getStatutRegle());
                        ((YvsComMensualiteFactureVente) entity).setFacture(facture);
                    }
                } else if (entity instanceof YvsComMensualiteFactureAchat) {
                    if (((YvsComMensualiteFactureAchat) entity).getFacture() != null) {
                        YvsComDocAchats facture = new YvsComDocAchats(((YvsComMensualiteFactureAchat) entity).getFacture().getId(), ((YvsComMensualiteFactureAchat) entity).getFacture().getNumDoc(), ((YvsComMensualiteFactureAchat) entity).getFacture().getStatut(), ((YvsComMensualiteFactureAchat) entity).getFacture().getStatutLivre(), ((YvsComMensualiteFactureAchat) entity).getFacture().getStatutRegle());
                        ((YvsComMensualiteFactureAchat) entity).setFacture(facture);
                    }
                } else if (entity instanceof YvsComRistourneDocVente) {
                    if (((YvsComRistourneDocVente) entity).getDocVente() != null) {
                        YvsComDocVentes facture = new YvsComDocVentes(((YvsComRistourneDocVente) entity).getDocVente().getId(), ((YvsComRistourneDocVente) entity).getDocVente().getNumDoc(), ((YvsComRistourneDocVente) entity).getDocVente().getStatut(), ((YvsComRistourneDocVente) entity).getDocVente().getStatutLivre(), ((YvsComRistourneDocVente) entity).getDocVente().getStatutRegle());
                        ((YvsComRistourneDocVente) entity).setDocVente(facture);
                    }
                } else if (entity instanceof YvsComCoutSupDocVente) {
                    if (((YvsComCoutSupDocVente) entity).getDocVente() != null) {
                        YvsComDocVentes facture = new YvsComDocVentes(((YvsComCoutSupDocVente) entity).getDocVente().getId(), ((YvsComCoutSupDocVente) entity).getDocVente().getNumDoc(), ((YvsComCoutSupDocVente) entity).getDocVente().getStatut(), ((YvsComCoutSupDocVente) entity).getDocVente().getStatutLivre(), ((YvsComCoutSupDocVente) entity).getDocVente().getStatutRegle());
                        ((YvsComCoutSupDocVente) entity).setDocVente(facture);
                    }
                } else if (entity instanceof YvsComCoutSupDocAchat) {
                    if (((YvsComCoutSupDocAchat) entity).getDocAchat() != null) {
                        YvsComDocAchats facture = new YvsComDocAchats(((YvsComCoutSupDocAchat) entity).getDocAchat().getId(), ((YvsComCoutSupDocAchat) entity).getDocAchat().getNumDoc(), ((YvsComCoutSupDocAchat) entity).getDocAchat().getStatut(), ((YvsComCoutSupDocAchat) entity).getDocAchat().getStatutLivre(), ((YvsComCoutSupDocAchat) entity).getDocAchat().getStatutRegle());
                        ((YvsComCoutSupDocAchat) entity).setDocAchat(facture);
                    }
                } else if (entity instanceof YvsComRemiseDocVente) {
                    if (((YvsComRemiseDocVente) entity).getDocVente() != null) {
                        YvsComDocVentes facture = new YvsComDocVentes(((YvsComRemiseDocVente) entity).getDocVente().getId(), ((YvsComRemiseDocVente) entity).getDocVente().getNumDoc(), ((YvsComRemiseDocVente) entity).getDocVente().getStatut(), ((YvsComRemiseDocVente) entity).getDocVente().getStatutLivre(), ((YvsComRemiseDocVente) entity).getDocVente().getStatutRegle());
                        ((YvsComRemiseDocVente) entity).setDocVente(facture);
                    }
                } else if (entity instanceof YvsComptaCaissePieceDivers) {
                    if (((YvsComptaCaissePieceDivers) entity).getDocDivers() != null) {
                        ((YvsComptaCaissePieceDivers) entity).setDocDivers(new YvsComptaCaisseDocDivers(((YvsComptaCaissePieceDivers) entity).getDocDivers().getId()));
                        ((YvsComptaCaissePieceDivers) entity).setSousDivers(null);
                        ((YvsComptaCaissePieceDivers) entity).setBonsProvisoire(null);
                        ((YvsComptaCaissePieceDivers) entity).setContenus(null);
                        ((YvsComptaCaissePieceDivers) entity).setPhasesReglement(null);
                    }
                } else if (entity instanceof YvsComCommercialVente) {
                    if (((YvsComCommercialVente) entity).getFacture() != null) {
                        YvsComDocVentes facture = new YvsComDocVentes(((YvsComCommercialVente) entity).getFacture().getId(), ((YvsComCommercialVente) entity).getFacture().getNumDoc(), ((YvsComCommercialVente) entity).getFacture().getStatut(), ((YvsComCommercialVente) entity).getFacture().getStatutLivre(), ((YvsComCommercialVente) entity).getFacture().getStatutRegle());
                        ((YvsComCommercialVente) entity).setFacture(facture);
                    }
                } else if (entity instanceof YvsComCommissionVente) {
                    if (((YvsComCommissionVente) entity).getFacture() != null) {
                        YvsComDocVentes facture = new YvsComDocVentes(((YvsComCommissionVente) entity).getFacture().getId(), ((YvsComCommissionVente) entity).getFacture().getNumDoc(), ((YvsComCommissionVente) entity).getFacture().getStatut(), ((YvsComCommissionVente) entity).getFacture().getStatutLivre(), ((YvsComCommissionVente) entity).getFacture().getStatutRegle());
                        ((YvsComCommissionVente) entity).setFacture(facture);
                    }
                } else if (entity instanceof YvsWorkflowValidFactureVente) {
                    if (((YvsWorkflowValidFactureVente) entity).getFactureVente() != null) {
                        YvsComDocVentes facture = new YvsComDocVentes(((YvsWorkflowValidFactureVente) entity).getFactureVente().getId(), ((YvsWorkflowValidFactureVente) entity).getFactureVente().getNumDoc(), ((YvsWorkflowValidFactureVente) entity).getFactureVente().getStatut(), ((YvsWorkflowValidFactureVente) entity).getFactureVente().getStatutLivre(), ((YvsWorkflowValidFactureVente) entity).getFactureVente().getStatutRegle());
                        ((YvsWorkflowValidFactureVente) entity).setFactureVente(facture);
                    }
                } else if (entity instanceof YvsWorkflowValidFactureAchat) {
                    if (((YvsWorkflowValidFactureAchat) entity).getFactureAchat() != null) {
                        YvsComDocAchats facture = new YvsComDocAchats(((YvsWorkflowValidFactureAchat) entity).getFactureAchat().getId(), ((YvsWorkflowValidFactureAchat) entity).getFactureAchat().getNumDoc(), ((YvsWorkflowValidFactureAchat) entity).getFactureAchat().getStatut(), ((YvsWorkflowValidFactureAchat) entity).getFactureAchat().getStatutLivre(), ((YvsWorkflowValidFactureAchat) entity).getFactureAchat().getStatutRegle());
                        ((YvsWorkflowValidFactureAchat) entity).setFactureAchat(facture);
                    }
                } else if (entity instanceof YvsComptaCaissePieceVente) {
                    if (((YvsComptaCaissePieceVente) entity).getVente() != null) {
                        YvsComDocVentes facture = new YvsComDocVentes(((YvsComptaCaissePieceVente) entity).getVente().getId(), ((YvsComptaCaissePieceVente) entity).getVente().getNumDoc(), ((YvsComptaCaissePieceVente) entity).getVente().getStatut(), ((YvsComptaCaissePieceVente) entity).getVente().getStatutLivre(), ((YvsComptaCaissePieceVente) entity).getVente().getStatutRegle());
                        ((YvsComptaCaissePieceVente) entity).setVente(facture);
                    }
                } else if (entity instanceof YvsComptaCaissePieceAchat) {
                    if (((YvsComptaCaissePieceAchat) entity).getAchat() != null) {
                        YvsComDocAchats facture = new YvsComDocAchats(((YvsComptaCaissePieceAchat) entity).getAchat().getId(), ((YvsComptaCaissePieceAchat) entity).getAchat().getNumDoc(), ((YvsComptaCaissePieceAchat) entity).getAchat().getStatut(), ((YvsComptaCaissePieceAchat) entity).getAchat().getStatutLivre(), ((YvsComptaCaissePieceAchat) entity).getAchat().getStatutRegle());
                        ((YvsComptaCaissePieceAchat) entity).setAchat(facture);
                    }
                } else if (entity instanceof YvsWorkflowValidDocCaisse) {
                    if (((YvsWorkflowValidDocCaisse) entity).getDocCaisse() != null) {
                        YvsComptaCaisseDocDivers facture = new YvsComptaCaisseDocDivers(((YvsWorkflowValidDocCaisse) entity).getDocCaisse().getId(), ((YvsWorkflowValidDocCaisse) entity).getDocCaisse().getNumPiece(), ((YvsWorkflowValidDocCaisse) entity).getDocCaisse().getReferenceExterne(), ((YvsWorkflowValidDocCaisse) entity).getDocCaisse().getStatutDoc(), ((YvsWorkflowValidDocCaisse) entity).getDocCaisse().getStatutRegle());
                        ((YvsWorkflowValidDocCaisse) entity).setDocCaisse(facture);
                    }
                } else if (entity instanceof YvsComDocVentes) {
                    if (((YvsComDocVentes) entity).getDocumentLie() != null ? ((YvsComDocVentes) entity).getDocumentLie().getId() > 0 : false) {
                        YvsComDocVentes facture = new YvsComDocVentes(((YvsComDocVentes) entity).getDocumentLie().getId(), ((YvsComDocVentes) entity).getDocumentLie().getNumDoc(), ((YvsComDocVentes) entity).getDocumentLie().getStatut(), ((YvsComDocVentes) entity).getDocumentLie().getStatutLivre(), ((YvsComDocVentes) entity).getDocumentLie().getStatutRegle());
                        ((YvsComDocVentes) entity).setDocumentLie(facture);
                    }
                    if (((YvsComDocVentes) entity).getPieceContenu() != null ? ((YvsComDocVentes) entity).getPieceContenu().getId() > 0 : false) {
                        rebuild(((YvsComDocVentes) entity).getPieceContenu());
                    }
                    if (((YvsComDocVentes) entity).getInformation() != null) {
                        rebuild(((YvsComDocVentes) entity).getInformation());
                    }
                    if (((YvsComDocVentes) entity).getEnteteDoc() != null) {
                        rebuild(((YvsComDocVentes) entity).getEnteteDoc());
                    }
                    if (((YvsComDocVentes) entity).getDocuments() != null) {
                        for (YvsComDocVentes y : ((YvsComDocVentes) entity).getDocuments()) {
                            y.setDocumentLie(null);
                            rebuild(y);
                        }
                    }
                    if (((YvsComDocVentes) entity).getContenus() != null) {
                        for (YvsComContenuDocVente y : ((YvsComDocVentes) entity).getContenus()) {
                            if (y.getParent() != null) {
                                y.setParent(new YvsComContenuDocVente(y.getParent().getId()));
                            }
                            if (y.getDocVente() != null) {
                                y.setDocVente(new YvsComDocVentes(y.getDocVente().getId(), y.getDocVente().getNumDoc(), y.getDocVente().getStatut(), y.getDocVente().getStatutLivre(), y.getDocVente().getStatutRegle()));
                            }
                            if (y.getAuthor() != null) {
                                y.setAuthor(new YvsUsersAgence(y.getAuthor().getId()));
                            }

                            y.setEtats(null);
                            y.setContenus(null);
                            y.setTaxes(null);
                            y.setLots(null);
                        }
                    }
                    if (((YvsComDocVentes) entity).getReglements() != null) {
                        for (YvsComptaCaissePieceVente y : ((YvsComDocVentes) entity).getReglements()) {
                            if (y.getVente() != null) {
                                y.setVente(new YvsComDocVentes(y.getVente().getId(), y.getVente().getNumDoc(), y.getVente().getStatut(), y.getVente().getStatutLivre(), y.getVente().getStatutRegle()));
                            }
                            if (y.getAuthor() != null) {
                                y.setAuthor(new YvsUsersAgence(y.getAuthor().getId()));
                            }

                            y.setSousVentes(null);
                            y.setCompensations(null);
                            y.setNotifs(null);
                            y.setPhasesReglement(null);
                        }
                    }
                    if (((YvsComDocVentes) entity).getCouts() != null) {
                        for (YvsComCoutSupDocVente y : ((YvsComDocVentes) entity).getCouts()) {
                            if (y.getDocVente() != null) {
                                y.setDocVente(new YvsComDocVentes(y.getDocVente().getId(), y.getDocVente().getNumDoc(), y.getDocVente().getStatut(), y.getDocVente().getStatutLivre(), y.getDocVente().getStatutRegle()));
                            }
                            if (y.getAuthor() != null) {
                                y.setAuthor(new YvsUsersAgence(y.getAuthor().getId()));
                            }
                        }
                    }
                    if (((YvsComDocVentes) entity).getEtapesValidations() != null) {
                        for (YvsWorkflowValidFactureVente y : ((YvsComDocVentes) entity).getEtapesValidations()) {
                            if (y.getFactureVente() != null) {
                                y.setFactureVente(new YvsComDocVentes(y.getFactureVente().getId(), y.getFactureVente().getNumDoc(), y.getFactureVente().getStatut(), y.getFactureVente().getStatutLivre(), y.getFactureVente().getStatutRegle()));
                            }
                            if (y.getAuthor() != null) {
                                y.setAuthor(new YvsUsersAgence(y.getAuthor().getId()));
                            }
                        }
                    }
                    if (((YvsComDocVentes) entity).getMensualites() != null) {
                        for (YvsComMensualiteFactureVente y : ((YvsComDocVentes) entity).getMensualites()) {
                            if (y.getFacture() != null) {
                                y.setFacture(new YvsComDocVentes(y.getFacture().getId(), y.getFacture().getNumDoc(), y.getFacture().getStatut(), y.getFacture().getStatutLivre(), y.getFacture().getStatutRegle()));
                            }
                            if (y.getAuthor() != null) {
                                y.setAuthor(new YvsUsersAgence(y.getAuthor().getId()));
                            }
                        }
                    }
                    if (((YvsComDocVentes) entity).getRistournes() != null) {
                        for (YvsComRistourneDocVente y : ((YvsComDocVentes) entity).getRistournes()) {
                            if (y.getDocVente() != null) {
                                y.setDocVente(new YvsComDocVentes(y.getDocVente().getId(), y.getDocVente().getNumDoc(), y.getDocVente().getStatut(), y.getDocVente().getStatutLivre(), y.getDocVente().getStatutRegle()));
                            }
                            if (y.getAuthor() != null) {
                                y.setAuthor(new YvsUsersAgence(y.getAuthor().getId()));
                            }
                        }
                    }
                    if (((YvsComDocVentes) entity).getRemises() != null) {
                        for (YvsComRemiseDocVente y : ((YvsComDocVentes) entity).getRemises()) {
                            if (y.getDocVente() != null) {
                                y.setDocVente(new YvsComDocVentes(y.getDocVente().getId(), y.getDocVente().getNumDoc(), y.getDocVente().getStatut(), y.getDocVente().getStatutLivre(), y.getDocVente().getStatutRegle()));
                            }
                            if (y.getAuthor() != null) {
                                y.setAuthor(new YvsUsersAgence(y.getAuthor().getId()));
                            }
                        }
                    }
                    if (((YvsComDocVentes) entity).getCommerciaux() != null) {
                        for (YvsComCommercialVente y : ((YvsComDocVentes) entity).getCommerciaux()) {
                            if (y.getFacture() != null) {
                                y.setFacture(new YvsComDocVentes(y.getFacture().getId(), y.getFacture().getNumDoc(), y.getFacture().getStatut(), y.getFacture().getStatutLivre(), y.getFacture().getStatutRegle()));
                            }
                            if (y.getAuthor() != null) {
                                y.setAuthor(new YvsUsersAgence(y.getAuthor().getId()));
                            }
                        }
                    }
                    if (((YvsComDocVentes) entity).getCommissions() != null) {
                        for (YvsComCommissionVente y : ((YvsComDocVentes) entity).getCommissions()) {
                            if (y.getFacture() != null) {
                                y.setFacture(new YvsComDocVentes(y.getFacture().getId(), y.getFacture().getNumDoc(), y.getFacture().getStatut(), y.getFacture().getStatutLivre(), y.getFacture().getStatutRegle()));
                            }
                            if (y.getAuthor() != null) {
                                y.setAuthor(new YvsUsersAgence(y.getAuthor().getId()));
                            }
                        }
                    }
                } else if (entity instanceof YvsComDocAchats) {
                    if (((YvsComDocAchats) entity).getDocumentLie() != null ? ((YvsComDocAchats) entity).getDocumentLie().getId() > 0 : false) {
                        YvsComDocAchats facture = new YvsComDocAchats(((YvsComDocAchats) entity).getDocumentLie().getId(), ((YvsComDocAchats) entity).getDocumentLie().getNumDoc(), ((YvsComDocAchats) entity).getDocumentLie().getStatut(), ((YvsComDocAchats) entity).getDocumentLie().getStatutLivre(), ((YvsComDocAchats) entity).getDocumentLie().getStatutRegle());
                        ((YvsComDocAchats) entity).setDocumentLie(facture);
                    }
                    if (((YvsComDocAchats) entity).getPieceContenu() != null ? ((YvsComDocAchats) entity).getPieceContenu().getId() > 0 : false) {
                        rebuild(((YvsComDocAchats) entity).getPieceContenu());
                    }
                    if (((YvsComDocAchats) entity).getDocuments() != null) {
                        for (YvsComDocAchats y : ((YvsComDocAchats) entity).getDocuments()) {
                            y.setDocumentLie(null);
                            rebuild(y);
                        }
                    }
                    if (((YvsComDocAchats) entity).getContenus() != null) {
                        for (YvsComContenuDocAchat y : ((YvsComDocAchats) entity).getContenus()) {
                            if (y.getParent() != null) {
                                y.setParent(new YvsComContenuDocAchat(y.getParent().getId()));
                            }
                            y.setDocAchat(null);
                            if (y.getAuthor() != null) {
                                y.setAuthor(new YvsUsersAgence(y.getAuthor().getId()));
                            }

                            y.setContenus(null);
                            y.setTaxes(null);
                        }
                    }
                    if (((YvsComDocAchats) entity).getReglements() != null) {
                        for (YvsComptaCaissePieceAchat y : ((YvsComDocAchats) entity).getReglements()) {
                            y.setAchat(null);
                            if (y.getAuthor() != null) {
                                y.setAuthor(new YvsUsersAgence(y.getAuthor().getId()));
                            }

                            y.setSousAchats(null);
                            y.setCompensations(null);
                            y.setNotifs(null);
                            y.setPhasesReglement(null);
                        }
                    }
                    if (((YvsComDocAchats) entity).getCouts() != null) {
                        for (YvsComCoutSupDocAchat y : ((YvsComDocAchats) entity).getCouts()) {
                            y.setDocAchat(null);
                            if (y.getAuthor() != null) {
                                y.setAuthor(new YvsUsersAgence(y.getAuthor().getId()));
                            }
                        }
                    }
                    if (((YvsComDocAchats) entity).getEtapesValidations() != null) {
                        for (YvsWorkflowValidFactureAchat y : ((YvsComDocAchats) entity).getEtapesValidations()) {
                            y.setFactureAchat(null);
                            if (y.getAuthor() != null) {
                                y.setAuthor(new YvsUsersAgence(y.getAuthor().getId()));
                            }
                        }
                    }
                    if (((YvsComDocAchats) entity).getMensualites() != null) {
                        for (YvsComMensualiteFactureAchat y : ((YvsComDocAchats) entity).getMensualites()) {
                            y.setFacture(null);
                            if (y.getAuthor() != null) {
                                y.setAuthor(new YvsUsersAgence(y.getAuthor().getId()));
                            }
                        }
                    }
                } else if (entity instanceof YvsComDocStocks) {
                    if (((YvsComDocStocks) entity).getDocumentLie() != null ? ((YvsComDocStocks) entity).getDocumentLie().getId() > 0 : false) {
                        YvsComDocStocks facture = new YvsComDocStocks(((YvsComDocStocks) entity).getDocumentLie().getId(), ((YvsComDocStocks) entity).getDocumentLie().getNumDoc(), ((YvsComDocStocks) entity).getDocumentLie().getStatut());
                        ((YvsComDocStocks) entity).setDocumentLie(facture);
                    }
                    if (((YvsComDocStocks) entity).getDocuments() != null) {
                        for (YvsComDocStocks y : ((YvsComDocStocks) entity).getDocuments()) {
                            y.setDocumentLie(null);
                            rebuild(y);
                        }
                    }
                    if (((YvsComDocStocks) entity).getContenus() != null) {
                        for (YvsComContenuDocStock y : ((YvsComDocStocks) entity).getContenus()) {
                            y.setDocStock(null);
                            if (y.getAuthor() != null) {
                                y.setAuthor(new YvsUsersAgence(y.getAuthor().getId()));
                            }

                            y.setReceptions(null);
                        }
                    }
                    if (((YvsComDocStocks) entity).getEtapesValidations() != null) {
                        for (YvsWorkflowValidDocStock y : ((YvsComDocStocks) entity).getEtapesValidations()) {
                            y.setDocStock(null);
                            if (y.getAuthor() != null) {
                                y.setAuthor(new YvsUsersAgence(y.getAuthor().getId()));
                            }
                        }
                    }
                } else if (entity instanceof YvsBaseGroupesArticle) {
                    if (((YvsBaseGroupesArticle) entity).getGroupeParent() != null ? ((YvsBaseGroupesArticle) entity).getGroupeParent().getId() > 0 : false) {
                        YvsBaseGroupesArticle parent = new YvsBaseGroupesArticle(((YvsBaseGroupesArticle) entity).getGroupeParent().getId(), ((YvsBaseGroupesArticle) entity).getGroupeParent().getRefgroupe(), ((YvsBaseGroupesArticle) entity).getGroupeParent().getDesignation());
                        ((YvsBaseGroupesArticle) entity).setGroupeParent(parent);
                    }
                } else if (entity instanceof YvsBaseFamilleArticle) {
                    if (((YvsBaseFamilleArticle) entity).getFamilleParent() != null ? ((YvsBaseFamilleArticle) entity).getFamilleParent().getId() > 0 : false) {
                        YvsBaseFamilleArticle parent = new YvsBaseFamilleArticle(((YvsBaseFamilleArticle) entity).getFamilleParent().getId(), ((YvsBaseFamilleArticle) entity).getFamilleParent().getReferenceFamille(), ((YvsBaseFamilleArticle) entity).getFamilleParent().getDesignation());
                        ((YvsBaseFamilleArticle) entity).setFamilleParent(parent);
                    }
                }
            }
        } catch (IllegalArgumentException | SecurityException ex) {
            Logger.getLogger(UtilRebuild.class.getName()).log(Level.SEVERE, null, ex);
        }
        return entity;
    }
}
