/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws.compta.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaAcompteClient;
import yvs.entity.compta.YvsComptaBielletage;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.YvsComptaCoutSupPieceVirement;
import yvs.entity.compta.YvsComptaMouvementCaisse;
import yvs.entity.compta.YvsComptaNotifReglementVente;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.divers.YvsComptaTaxeDocDivers;
import yvs.entity.compta.vente.YvsComptaPhaseAcompteVente;
import yvs.entity.param.workflow.YvsWorkflowValidDocCaisse;
import yvs.service.IEntitySax;
import yvs.service.compta.doc.divers.IYvsComptaAcompteClient;
import yvs.service.compta.doc.divers.IYvsComptaBielletage;
import yvs.service.compta.doc.divers.IYvsComptaCaisseDocDivers;
import yvs.service.compta.doc.divers.IYvsComptaCaissePieceDivers;
import yvs.service.compta.doc.divers.IYvsComptaCaissePieceVirement;
import yvs.service.compta.doc.divers.IYvsComptaCoutSupPieceVirement;
import yvs.service.compta.doc.divers.IYvsComptaMouvementCaisse;
import yvs.service.compta.doc.divers.IYvsComptaNotifRegVente;
import yvs.service.compta.doc.divers.IYvsComptaTaxeDocDivers;
import yvs.service.param.workflow.IYvsWorkflowValidDocCaisse;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz-pc
 */
@Path("services/compta/v1")
public class GenericResource extends lymytz.ws.compta.GenericResource {

    IEntitySax IEntitiSax = new IEntitySax();

    /**
     * BEGIN DOC DIVERS
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_doc_divers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaCaisseDocDivers> saveDocDivers(YvsComptaCaisseDocDivers entity) {
        try {
            IYvsComptaCaisseDocDivers impl = (IYvsComptaCaisseDocDivers) IEntitiSax.createInstance("IYvsComptaCaisseDocDivers", dao);
            if (impl != null) {
                List<YvsComptaCaissePieceDivers> reglements = new ArrayList<>(entity.getReglements());
                List<YvsComptaTaxeDocDivers> taxes = new ArrayList<>(entity.getTaxes());
                entity.getReglements().clear();
                entity.getTaxes().clear();
                ResultatAction<YvsComptaCaisseDocDivers> result = impl.save(entity);
                if (result != null ? result.isResult() : false) {
                    // save reglements
                    if (reglements != null ? !reglements.isEmpty() : false) {
                        for (YvsComptaCaissePieceDivers p : reglements) {
                            p.setDocDivers(new YvsComptaCaisseDocDivers(entity.getId()));
                            saveReglementDocDivers(p);
                        }
                    }
                    // save taxes
                    if (taxes != null ? !taxes.isEmpty() : false) {
                        for (YvsComptaTaxeDocDivers t : taxes) {
                            t.setDocDivers(new YvsComptaCaisseDocDivers(entity.getId()));
                            saveTaxeDocDivers(t);
                        }
                    }

                }
                return rebuild(result);
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_doc_divers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaCaisseDocDivers> updateDocDivers(YvsComptaCaisseDocDivers entity) {
        try {
            IYvsComptaCaisseDocDivers impl = (IYvsComptaCaisseDocDivers) IEntitiSax.createInstance("IYvsComptaCaisseDocDivers", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_doc_divers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaCaisseDocDivers> deleteDocDivers(YvsComptaCaisseDocDivers entity) {
        try {
            IYvsComptaCaisseDocDivers impl = (IYvsComptaCaisseDocDivers) IEntitiSax.createInstance("IYvsComptaCaisseDocDivers", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END DOC DIVERS**/
    /**
     * BEGIN REGLEMENTS DOC DIVERS
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_reglement_doc_divres")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaCaissePieceDivers> saveReglementDocDivers(YvsComptaCaissePieceDivers entity) {
        try {
            IYvsComptaCaissePieceDivers impl = (IYvsComptaCaissePieceDivers) IEntitiSax.createInstance("IYvsComptaCaissePieceDivers", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_reglement_doc_divres")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public ResultatAction<YvsComptaCaissePieceDivers> updateReglementDocDivers(YvsComptaCaissePieceDivers entity) {
        try {
            IYvsComptaCaissePieceDivers impl = (IYvsComptaCaissePieceDivers) IEntitiSax.createInstance("IYvsComptaCaissePieceDivers", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_reglement_doc_divres")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaCaissePieceDivers> deleteReglementDocDivers(YvsComptaCaissePieceDivers entity) {
        try {
            IYvsComptaCaissePieceDivers impl = (IYvsComptaCaissePieceDivers) IEntitiSax.createInstance("IYvsComptaCaissePieceDivers", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END REGLEMENTS DOC DIVERS**/
    /**
     * BEGIN TAXES DOC DIVERS
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_taxe_doc_divres")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaTaxeDocDivers> saveTaxeDocDivers(YvsComptaTaxeDocDivers entity) {
        try {
            IYvsComptaTaxeDocDivers impl = (IYvsComptaTaxeDocDivers) IEntitiSax.createInstance("IYvsComptaTaxeDocDivers", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_taxe_doc_divres")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public ResultatAction<YvsComptaTaxeDocDivers> updateTaxeDocDivers(YvsComptaTaxeDocDivers entity) {
        try {
            IYvsComptaTaxeDocDivers impl = (IYvsComptaTaxeDocDivers) IEntitiSax.createInstance("IYvsComptaTaxeDocDivers", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_taxe_doc_divres")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaTaxeDocDivers> deleteTaxeDocDivers(YvsComptaTaxeDocDivers entity) {
        try {
            IYvsComptaTaxeDocDivers impl = (IYvsComptaTaxeDocDivers) IEntitiSax.createInstance("IYvsComptaTaxeDocDivers", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END TAXES DOC DIVERS**/
    @POST
    @Path("save_etape_doc_divers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsWorkflowValidDocCaisse> saveEtapeDocDivers(YvsWorkflowValidDocCaisse entity) {
        try {
            IYvsWorkflowValidDocCaisse impl = (IYvsWorkflowValidDocCaisse) IEntitiSax.createInstance("IYvsWorkflowValidDocCaisse", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_etape_doc_divers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsWorkflowValidDocCaisse> updateEtapeDocDivers(YvsWorkflowValidDocCaisse entity) {
        try {
            IYvsWorkflowValidDocCaisse impl = (IYvsWorkflowValidDocCaisse) IEntitiSax.createInstance("IYvsWorkflowValidDocCaisse", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_etape_doc_divers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsWorkflowValidDocCaisse> deleteEtapeDocDivers(YvsWorkflowValidDocCaisse entity) {
        try {
            IYvsWorkflowValidDocCaisse impl = (IYvsWorkflowValidDocCaisse) IEntitiSax.createInstance("IYvsWorkflowValidDocCaisse", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("valide_etape_doc_divers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsWorkflowValidDocCaisse> valideEtapeDocDivers(YvsWorkflowValidDocCaisse entity) {
        try {
            IYvsComptaCaisseDocDivers impl = (IYvsComptaCaisseDocDivers) IEntitiSax.createInstance("IYvsComptaCaisseDocDivers", dao);
            if (impl != null) {
                return impl.validEtapeOrdre(entity);
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * BEGIN VIREMENT CAISSE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_virement_caisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaCaissePieceVirement> saveVirementCaisse(YvsComptaCaissePieceVirement entity) {
        try {
            IYvsComptaCaissePieceVirement impl = (IYvsComptaCaissePieceVirement) IEntitiSax.createInstance("IYvsComptaCaissePieceVirement", dao);
            if (impl != null) {
                if (entity.getSource().getJournal() == null) {
                    entity.setSource((YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{entity.getSource().getId()}));
                }
                if (entity.getCible().getJournal() == null) {
                    entity.setCible((YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{entity.getCible().getId()}));
                }
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("save_virement_caisse_with_dependence")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaCaissePieceVirement> saveVirementCaisseWith_dependence(YvsComptaCaissePieceVirement entity) {
        try {
            IYvsComptaCaissePieceVirement impl = (IYvsComptaCaissePieceVirement) IEntitiSax.createInstance("IYvsComptaCaissePieceVirement", dao);
            if (impl != null) {
                ResultatAction<YvsComptaCaissePieceVirement> re = impl.save(entity);
                //Change le statut (mettre au statut transmis)
                //notifie la liaison avec point de vente
                return rebuild(re);
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_virement_caisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public ResultatAction<YvsComptaCaissePieceVirement> updateVirementCaisse(YvsComptaCaissePieceVirement entity) {
        try {
            IYvsComptaCaissePieceVirement impl = (IYvsComptaCaissePieceVirement) IEntitiSax.createInstance("IYvsComptaCaissePieceVirement", dao);
            if (impl != null) {
                ResultatAction result = rebuild(impl.update(entity));
                if (entity.getId() > 0) {
                    entity = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{entity.getId()});
                    if (entity.getStatutPiece().equals(Constantes.STATUT_DOC_SOUMIS) || entity.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                        comptabiliserCaisseVirement(entity);
                    } else {
                        unComptabiliserCaisseVirement(entity);
                    }
                }
                return result;
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_virement_caisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaCaissePieceVirement> deleteVirementCaisse(YvsComptaCaissePieceVirement entity) {
        try {
            IYvsComptaCaissePieceVirement impl = (IYvsComptaCaissePieceVirement) IEntitiSax.createInstance("IYvsComptaCaissePieceVirement", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END VIREMENT CAISSE**/
    /**
     * BEGIN BILLETAGE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_billetage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaBielletage> saveBilletage(YvsComptaBielletage entity) {
        try {
            IYvsComptaBielletage impl = (IYvsComptaBielletage) IEntitiSax.createInstance("IYvsComptaBielletage", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_billetage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public ResultatAction<YvsComptaBielletage> updateBilletage(YvsComptaBielletage entity) {
        try {
            IYvsComptaBielletage impl = (IYvsComptaBielletage) IEntitiSax.createInstance("IYvsComptaBielletage", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_billetage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaBielletage> deleteBilletage(YvsComptaBielletage entity) {
        try {
            IYvsComptaBielletage impl = (IYvsComptaBielletage) IEntitiSax.createInstance("IYvsComptaBielletage", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END BILLETAGE**/
    /**
     * BEGIN COUT SUP VIREMENT
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_cout_sup_virement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaCoutSupPieceVirement> saveCoutSupVirement(YvsComptaCoutSupPieceVirement entity) {
        try {
            IYvsComptaCoutSupPieceVirement impl = (IYvsComptaCoutSupPieceVirement) IEntitiSax.createInstance("IYvsComptaCoutSupPieceVirement", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_cout_sup_virement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public ResultatAction<YvsComptaCoutSupPieceVirement> updateCoutSupVirement(YvsComptaCoutSupPieceVirement entity) {
        try {
            IYvsComptaCoutSupPieceVirement impl = (IYvsComptaCoutSupPieceVirement) IEntitiSax.createInstance("IYvsComptaCoutSupPieceVirement", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_cout_sup_virement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaCoutSupPieceVirement> deleteCoutSupVirement(YvsComptaCoutSupPieceVirement entity) {
        try {
            IYvsComptaCoutSupPieceVirement impl = (IYvsComptaCoutSupPieceVirement) IEntitiSax.createInstance("IYvsComptaCoutSupPieceVirement", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END COUT SUP VIREMENT**/
    /*BEGIN ACOMPTE CLIENTS**/
    @POST
    @Path("save_acompte_client")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaAcompteClient> saveAcompteClient(YvsComptaAcompteClient entity) {
        try {
            if (entity != null) {
                IYvsComptaAcompteClient impl = (IYvsComptaAcompteClient) IEntitiSax.createInstance("IYvsComptaAcompteClient", dao);
                if (impl != null) {
                    List<YvsComptaNotifReglementVente> notifications = new ArrayList<>(entity.getNotifs());
                    List<YvsComptaPhaseAcompteVente> phases = new ArrayList<>(entity.getPhasesReglement());
                    ResultatAction<YvsComptaAcompteClient> result = rebuild(impl.save(entity));
                    if (result != null ? result.isResult() : false) {
                        for (YvsComptaNotifReglementVente n : notifications) {
                            saveNotifRegVente(n);
                        }
                        entity.setNotifs(notifications);
                    }
                    return result;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("save_notif_reg_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaNotifReglementVente> saveNotifRegVente(YvsComptaNotifReglementVente entity) {
        try {
            IYvsComptaNotifRegVente impl = (IYvsComptaNotifRegVente) IEntitiSax.createInstance("IYvsComptaNotifRegVente", dao);
            if (impl != null) {
                ResultatAction<YvsComptaNotifReglementVente> result = rebuild(impl.save(entity));
                return result;
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_notif_reg_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaNotifReglementVente> updateNotifRegVente(YvsComptaNotifReglementVente entity) {
        try {
            IYvsComptaNotifRegVente impl = (IYvsComptaNotifRegVente) IEntitiSax.createInstance("IYvsComptaNotifRegVente", dao);
            if (impl != null) {
                ResultatAction<YvsComptaNotifReglementVente> result = rebuild(impl.update(entity));
                return result;
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    /*END ACOMPTE CLIENTS**/

    /*BEGIN MOUVEMENT CAISSE**/
    @POST
    @Path("save_mouvement_caisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaMouvementCaisse> saveMouvementCaisse(YvsComptaMouvementCaisse entity) {
        try {
            if (entity != null) {
                IYvsComptaMouvementCaisse impl = (IYvsComptaMouvementCaisse) IEntitiSax.createInstance("IYvsComptaMouvementCaisse", dao);
                if (impl != null) {
                    ResultatAction<YvsComptaMouvementCaisse> result = rebuild(impl.save(entity));
                    return result;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_mouvement_caisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaMouvementCaisse> updateMouvementCaisse(YvsComptaMouvementCaisse entity) {
        try {
            IYvsComptaMouvementCaisse impl = (IYvsComptaMouvementCaisse) IEntitiSax.createInstance("IYvsComptaMouvementCaisse", dao);
            if (impl != null) {
                ResultatAction<YvsComptaMouvementCaisse> result = rebuild(impl.update(entity));
                return result;
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_mouvement_caisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaMouvementCaisse> deleteMouvementCaisse(YvsComptaMouvementCaisse entity) {
        try {
            IYvsComptaMouvementCaisse impl = (IYvsComptaMouvementCaisse) IEntitiSax.createInstance("IYvsComptaMouvementCaisse", dao);
            if (impl != null) {
                ResultatAction<YvsComptaMouvementCaisse> result = rebuild(impl.delete(entity));
                return result;
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    /*END ACOMPTE CLIENTS**/
}
