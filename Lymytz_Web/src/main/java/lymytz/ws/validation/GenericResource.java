/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import yvs.entity.commercial.achat.YvsComArticleApprovisionnement;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComFicheApprovisionnement;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.grh.activite.YvsGrhCongeEmps;
import yvs.entity.grh.activite.YvsGrhFormation;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.workflow.YvsWorkflowValidApprovissionnement;
import yvs.entity.param.workflow.YvsWorkflowValidBonProvisoire;
import yvs.entity.param.workflow.YvsWorkflowValidConge;
import yvs.entity.param.workflow.YvsWorkflowValidDocCaisse;
import yvs.entity.param.workflow.YvsWorkflowValidDocStock;
import yvs.entity.param.workflow.YvsWorkflowValidFactureAchat;
import yvs.entity.param.workflow.YvsWorkflowValidFactureVente;
import yvs.entity.param.workflow.YvsWorkflowValidMission;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsers;

/**
 *
 * @author Lymytz Dowes
 */
@Path("services/validation")    
@RequestScoped
public class GenericResource extends lymytz.ws.GenericResource {

    @POST
    @Path("getWorkFlow")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    public List<Object[]> getWorkFlow(@HeaderParam("societe") String societe, @HeaderParam("niveau") String niveau) {
        try {
            if (asString(societe) && asString(niveau)) {
                String rq = "SELECT * FROM workflow(?,?)";
                return dao.loadDataByNativeQuery(rq, new Object[]{Long.valueOf(societe), Long.valueOf(niveau)});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new ArrayList<>();

    }

    @POST
    @Path("getFactureAchat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComDocAchats> getFactureAchat(@HeaderParam("ids") String ids
    ) {
        try {
            if (asString(ids)) {
                String[] chaine_id = ids.split("-");
                List<Long> val_id = new ArrayList<>();
                for (int i = 0; i < chaine_id.length; i++) {
                    Long id = Long.valueOf(chaine_id[i]);
                    val_id.add(id);
                }
                return dao.loadNameQueries("YvsComDocAchats.findByIds", new String[]{"ids"}, new Object[]{val_id});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new ArrayList<>();
    }

    @POST
    @Path("getMissions")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsGrhMissions> getMissions(@HeaderParam("ids") String ids
    ) {
        try {
            if (asString(ids)) {
                String[] chaine_id = ids.split("-");
                List<Long> val_id = new ArrayList<>();
                for (int i = 0; i < chaine_id.length; i++) {
                    Long id = Long.valueOf(chaine_id[i]);
                    val_id.add(id);
                }
                return dao.loadNameQueries("YvsMissions.findByIds", new String[]{"ids"}, new Object[]{val_id});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new ArrayList<>();
    }

    @POST
    @Path("getFormation")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsGrhFormation> getFormation(@HeaderParam("ids") String ids
    ) {
        try {
            if (asString(ids)) {
                String[] chaine_id = ids.split("-");
                List<Long> val_id = new ArrayList<>();
                for (int i = 0; i < chaine_id.length; i++) {
                    Long id = Long.valueOf(chaine_id[i]);
                    val_id.add(id);
                }
                return dao.loadNameQueries("YvsFormation.findByIds", new String[]{"ids"}, new Object[]{val_id});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new ArrayList<>();
    }

    @POST
    @Path("getFactureVente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComDocVentes> getFactureVente(@HeaderParam("ids") String ids
    ) {
        try {
            if (asString(ids)) {
                String[] chaine_id = ids.split("-");
                List<Long> val_id = new ArrayList<>();
                for (int i = 0; i < chaine_id.length; i++) {
                    Long id = Long.valueOf(chaine_id[i]);
                    val_id.add(id);
                }
                return dao.loadNameQueries("YvsComDocVentes.findByIds", new String[]{"ids"}, new Object[]{val_id});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new ArrayList<>();
    }

    @POST
    @Path("getConges")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsGrhCongeEmps> getConges(@HeaderParam("ids") String ids
    ) {
        try {
            if (asString(ids)) {
                String[] chaine_id = ids.split("-");
                List<Long> val_id = new ArrayList<>();
                for (int i = 0; i < chaine_id.length; i++) {
                    Long id = Long.valueOf(chaine_id[i]);
                    val_id.add(id);
                }
                return dao.loadNameQueries("YvsGrhCongeEmps.findByIds", new String[]{"ids"}, new Object[]{val_id});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new ArrayList<>();
    }

    @POST
    @Path("getDocStock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComDocStocks> getDocStock(@HeaderParam("ids") String ids) {
        try {

            if (asString(ids)) {
                String[] chaine_id = ids.split("-");
                List<Long> val_id = new ArrayList<>();
                for (int i = 0; i < chaine_id.length; i++) {
                    Long id = Long.valueOf(chaine_id[i]);
                    val_id.add(id);
                }
                return dao.loadNameQueries("YvsComDocStocks.findByIds", new String[]{"ids"}, new Object[]{val_id});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new ArrayList<>();
    }

    @POST
    @Path("getCaisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComptaCaisseDocDivers> getCaisse(@HeaderParam("ids") String ids) {
        try {
            if (asString(ids)) {
                String[] chaine_id = ids.split("-");
                List<Long> val_id = new ArrayList<>();
                for (int i = 0; i < chaine_id.length; i++) {
                    Long id = Long.valueOf(chaine_id[i]);
                    val_id.add(id);
                }
                return dao.loadNameQueries("YvsComptaCaisseDocDivers.findByIds", new String[]{"ids"}, new Object[]{val_id});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new ArrayList<>();
    }

    @POST
    @Path("getFicheAppro")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComFicheApprovisionnement> getFicheAppro(@HeaderParam("ids") String ids
    ) {
        try {
            if (asString(ids)) {
                String[] chaine_id = ids.split("-");
                List<Long> val_id = new ArrayList<>();
                for (int i = 0; i < chaine_id.length; i++) {
                    Long id = Long.valueOf(chaine_id[i]);
                    val_id.add(id);
                }
                return dao.loadNameQueries("YvsComFicheApprovisionnement.findByIds", new String[]{"ids"}, new Object[]{val_id});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new ArrayList<>();
    }

    @POST
    @Path("getBonProvisoire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComptaBonProvisoire> getBonProvisoire(@HeaderParam("ids") String ids
    ) {
        try {
            if (asString(ids)) {
                String[] chaine_id = ids.split("-");
                List<Long> val_id = new ArrayList<>();
                for (int i = 0; i < chaine_id.length; i++) {
                    Long id = Long.valueOf(chaine_id[i]);
                    val_id.add(id);
                }
                return dao.loadNameQueries("YvsComptaBonProvisoire.findByIds", new String[]{"ids"}, new Object[]{val_id});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @POST
    @Path("getContenuAchat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComContenuDocAchat> getContenuAchat(@HeaderParam("docAchat") String docAchat) {
        try {
            return dao.loadNameQueries("YvsComContenuDocAchat.findByDocAchat", new String[]{"docAchat"}, new Object[]{new YvsComDocAchats(Long.valueOf(docAchat))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @POST
    @Path("getContenuVente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComContenuDocVente> getContenuVente(@HeaderParam("docVente") String docVente) {
        try {
            return dao.loadNameQueries("YvsComContenuDocVente.findByFacture", new String[]{"docVente"}, new Object[]{new YvsComDocVentes(Long.valueOf(docVente))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @POST
    @Path("getContenuStock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComContenuDocStock> getContenuStock(@HeaderParam("docStock") String docStock) {
        try {
            return dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{new YvsComDocStocks(Long.valueOf(docStock))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @POST
    @Path("getContenuApprovisionnement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComArticleApprovisionnement> getContenuApprovisionnement(@HeaderParam("fiche") String fiche) {
        try {
            return dao.loadNameQueries("YvsComArticleApprovisionnement.findByFiche", new String[]{"fiche"}, new Object[]{new YvsComFicheApprovisionnement(Long.valueOf(fiche))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @POST
    @Path("getReglementDivers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComptaCaissePieceDivers> getReglementDivers(@HeaderParam("docDivers") String docDivers) {
        try {
            return dao.loadNameQueries("YvsComptaCaissePieceDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{new YvsComptaCaisseDocDivers(Long.valueOf(docDivers))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @POST
    @Path("searchAchat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsComDocAchats searchAchat(@HeaderParam("num_doc") String num_doc, @HeaderParam("societe") String societe
    ) {
        try {
            return (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findByNumDocs", new String[]{"numDoc", "societe"}, new Object[]{num_doc, new YvsSocietes(Long.valueOf(societe))});
        } catch (Exception e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("searchDocVente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsComDocVentes searchDocVente(@HeaderParam("num_doc") String num_doc, @HeaderParam("societe") String societe) {
        try {
            if (asString(societe)) {
                return (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findByNumDoc", new String[]{"numDoc", "societe"}, new Object[]{num_doc, new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("searchDocDivers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsComptaCaisseDocDivers searchDocDivers(@HeaderParam("num_doc") String num_doc, @HeaderParam("societe") String societe
    ) {
        try {
            if (asString(societe)) {
                return (YvsComptaCaisseDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseDocDivers.findByNumPieceLike", new String[]{"numPiece", "societe"}, new Object[]{num_doc, new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("searchDocStock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsComDocStocks searchDocStock(@HeaderParam("num_doc") String num_doc, @HeaderParam("societe") String societe
    ) {
        try {
            if (asString(societe)) {
                return (YvsComDocStocks) dao.loadOneByNameQueries("findByNumDoc", new String[]{"numRef", "societe"}, new Object[]{num_doc, new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("searchDocAppro")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsComFicheApprovisionnement searchDocAppro(@HeaderParam("num_doc") String num_doc, @HeaderParam("societe") String societe
    ) {
        try {
            if (asString(societe)) {
                return (YvsComFicheApprovisionnement) dao.loadOneByNameQueries("YvsComFicheApprovisionnement.findByReference", new String[]{"reference", "societe"}, new Object[]{num_doc, new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("searchDocMission")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsGrhMissions searchDocMission(@HeaderParam("num_doc") String num_doc, @HeaderParam("societe") String societe
    ) {
        try {
            if (asString(societe)) {
                return (YvsGrhMissions) dao.loadOneByNameQueries("YvsGrhMissions.findByNumeroM", new String[]{"numero", "societe"}, new Object[]{num_doc, new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("searchBonProvisoire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsComptaBonProvisoire searchBonProvisoire(@HeaderParam("num_doc") String num_doc, @HeaderParam("societe") String societe
    ) {
        try {
            //YvsSocietes societes = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{Long.valueOf(societe)});
            return (YvsComptaBonProvisoire) dao.loadOneByNameQueries("YvsComptaBonProvisoire.findByNumDoc", new String[]{"reference", "societe"}, new Object[]{num_doc, new YvsSocietes(Long.valueOf(societe))});
        } catch (NumberFormatException e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("searchDocConge")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsGrhCongeEmps searchDocConge(@HeaderParam("num_doc") String num_doc
    ) {
        try {
            //YvsSocietes societes = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{Long.valueOf(societe)});
            return (YvsGrhCongeEmps) dao.loadOneByNameQueries("YvsGrhCongeEmps.findByReference", new String[]{"referenceConge"}, new Object[]{num_doc});
        } catch (Exception e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("ValidationFactureAchat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String ValidationFactureAchat(@HeaderParam("docAchat") String docAchat, @HeaderParam("currentUser") String currentUser, @HeaderParam("niveauAcces") String niveauAcces
    ) {
        String reponse = null;
        try {
            YvsComDocAchats doc = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{Long.valueOf(docAchat)});
            if (doc != null ? doc.getId() > 0 : false) {
                YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(currentUser)});
                YvsAgences agence = doc.getAgence();
                List<YvsWorkflowValidFactureAchat> ls = dao.loadNameQueries("YvsWorkflowValidFactureAchat.findByFacture", new String[]{"facture"}, new Object[]{doc});
                YvsWorkflowValidFactureAchat workflow = ordonneEtapesAchats(ls, false);
                if (workflow != null) {
                    if (!asDroitValideEtape(workflow.getEtape(), new YvsNiveauAcces(Long.valueOf(niveauAcces)))) {
                        reponse = "Vous ne disposez pas d'autorisation nécessaire";
                    }
                    if (!asString(reponse)) {
                        reponse = dao.validEtapeFactureAchat(doc, workflow, yvsUsersAgence(user, agence));
                    }
                } else {
                    reponse = "Validation Impossible";
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            reponse = "Validation Impossible";
        }
        System.out.println("ValidationFactureAchat : " + reponse);
        return reponse;
    }

    @POST
    @Path("AnnulationFactureAchat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String AnnulationFactureAchat(@HeaderParam("docAchat") String docAchat, @HeaderParam("motif") String motif, @HeaderParam("currentUser") String currentUser, @HeaderParam("niveauAcces") String niveauAcces
    ) {
        String reponse = null;
        try {
            YvsComDocAchats doc = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{Long.valueOf(docAchat)});
            if (doc != null ? doc.getId() > 0 : false) {
                YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(currentUser)});
                YvsAgences agence = doc.getAgence();
                List<YvsWorkflowValidFactureAchat> ls = dao.loadNameQueries("YvsWorkflowValidFactureAchat.findByFacture", new String[]{"facture"}, new Object[]{doc});
                YvsWorkflowValidFactureAchat workflow = ordonneEtapesAchats(ls, true);
                if (workflow != null) {
                    if (!asDroitValideEtape(workflow.getEtape(), new YvsNiveauAcces(Long.valueOf(niveauAcces)))) {
                        reponse = "Vous ne disposez pas d'autorisation nécessaire";
                    }
                    if (!asString(reponse)) {
                        reponse = dao.annulEtapeFactureAchat(doc, workflow, motif, yvsUsersAgence(user, agence));
                    }
                } else {
                    reponse = "Annulation Impossible";
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            reponse = "Annulation Impossible";
        }
        System.out.println("AnnulationFactureAchat : " + reponse);
        return reponse;
    }

    @POST
    @Path("ValidationFactureVente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String ValidationFactureVente(@HeaderParam("docVente") String docVente, @HeaderParam("currentUser") String currentUser, @HeaderParam("niveauAcces") String niveauAcces
    ) {
        String reponse = null;
        try {
            YvsComDocVentes doc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{Long.valueOf(docVente)});
            if (doc != null ? doc.getId() > 0 : false) {
                YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(currentUser)});
                YvsAgences agence = doc.getEnteteDoc().getAgence();

                List<YvsWorkflowValidFactureVente> ls = dao.loadNameQueries("YvsWorkflowValidFactureVente.findByFacture", new String[]{"facture"}, new Object[]{doc});
                YvsWorkflowValidFactureVente workflow = ordonneEtapesVentes(ls, false);
                if (workflow != null) {
                    if (!asDroitValideEtape(workflow.getEtape(), new YvsNiveauAcces(Long.valueOf(niveauAcces)))) {
                        reponse = "Vous ne disposez pas d'autorisation nécessaire";
                    }
                    if (!asString(reponse)) {
                        reponse = dao.validEtapeFactureVente(doc, workflow, yvsUsersAgence(user, agence));
                    }
                } else {
                    reponse = "Validation Impossible";
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            reponse = "Validation Impossible";
        }
        System.out.println("ValidationFactureVente : " + reponse);
        return reponse;
    }

    @POST
    @Path("AnnulationFactureVente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String AnnulationFactureVente(@HeaderParam("docVente") String docVente, @HeaderParam("motif") String motif, @HeaderParam("currentUser") String currentUser, @HeaderParam("niveauAcces") String niveauAcces
    ) {
        String reponse = null;
        try {
            YvsComDocVentes doc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{Long.valueOf(docVente)});
            if (doc != null ? doc.getId() > 0 : false) {
                YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(currentUser)});
                YvsAgences agence = doc.getEnteteDoc() != null ? doc.getEnteteDoc().getAgence() : null;

                List<YvsWorkflowValidFactureVente> ls = dao.loadNameQueries("YvsWorkflowValidFactureVente.findByFacture", new String[]{"facture"}, new Object[]{doc});
                YvsWorkflowValidFactureVente workflow = ordonneEtapesVentes(ls, true);
                if (workflow != null) {
                    if (!asDroitValideEtape(workflow.getEtape(), new YvsNiveauAcces(Long.valueOf(niveauAcces)))) {
                        reponse = "Vous ne disposez pas d'autorisation nécessaire";
                    }
                    if (!asString(reponse)) {
                        reponse = dao.annulEtapeFactureVente(doc, workflow, motif, yvsUsersAgence(user, agence));
                    }
                } else {
                    reponse = "Annulation Impossible";
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            reponse = "Annulation Impossible";
        }
        System.out.println("AnnulationFactureVente : " + reponse);
        return reponse;
    }

    @POST
    @Path("ValidationDocDivers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String ValidationDocDivers(@HeaderParam("docDivers") String docDivers, @HeaderParam("currentUser") String currentUser, @HeaderParam("niveauAcces") String niveauAcces
    ) {
        String reponse = null;
        try {
            YvsComptaCaisseDocDivers doc = (YvsComptaCaisseDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseDocDivers.findById", new String[]{"id"}, new Object[]{Long.valueOf(docDivers)});
            if (doc != null ? doc.getId() > 0 : false) {
                YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(currentUser)});
                YvsAgences agence = user.getAgence();

                List<YvsWorkflowValidDocCaisse> ls = dao.loadNameQueries("YvsWorkflowValidDocCaisse.findByFacture", new String[]{"facture"}, new Object[]{doc});
                YvsWorkflowValidDocCaisse workflow = ordonneEtapesDivers(ls, false);
                if (workflow != null) {
                    if (!asDroitValideEtape(workflow.getEtape(), new YvsNiveauAcces(Long.valueOf(niveauAcces)))) {
                        reponse = "Vous ne disposez pas d'autorisation nécessaire";
                    }
                    if (!asString(reponse)) {
                        reponse = dao.validEtapeOrdreDivers(doc, workflow, yvsUsersAgence(user, agence));
                    }
                } else {
                    reponse = "Validation Impossible";
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            reponse = "Validation Impossible";
        }
        System.out.println("ValidationDocDivers : " + reponse);
        return reponse;
    }

    @POST
    @Path("AnnulationDocDivers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String AnnulationDocDivers(@HeaderParam("docDivers") String docDivers, @HeaderParam("motif") String motif, @HeaderParam("currentUser") String currentUser, @HeaderParam("niveauAcces") String niveauAcces
    ) {
        String reponse = null;
        try {
            YvsComptaCaisseDocDivers doc = (YvsComptaCaisseDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseDocDivers.findById", new String[]{"id"}, new Object[]{Long.valueOf(docDivers)});
            if (doc != null ? doc.getId() > 0 : false) {
                YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(currentUser)});
                YvsAgences agence = user.getAgence();

                List<YvsWorkflowValidDocCaisse> ls = dao.loadNameQueries("YvsWorkflowValidDocCaisse.findByFacture", new String[]{"facture"}, new Object[]{doc});
                YvsWorkflowValidDocCaisse workflow = ordonneEtapesDivers(ls, true);
                if (workflow != null) {
                    if (!asDroitValideEtape(workflow.getEtape(), new YvsNiveauAcces(Long.valueOf(niveauAcces)))) {
                        reponse = "Vous ne disposez pas d'autorisation nécessaire";
                    }
                    if (!asString(reponse)) {
                        reponse = dao.annulEtapeOrdreDivers(doc, workflow, motif, yvsUsersAgence(user, agence));
                    }
                } else {
                    reponse = "Annulation Impossible";
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            reponse = "Annulation Impossible";
        }
        System.out.println("AnnulationDocDivers : " + reponse);
        return reponse;
    }

    @POST
    @Path("ValidationConge")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String ValidationConge(@HeaderParam("conge") String conge, @HeaderParam("currentUser") String currentUser, @HeaderParam("niveauAcces") String niveauAcces
    ) {
        String reponse = null;
        try {
            YvsGrhCongeEmps doc = (YvsGrhCongeEmps) dao.loadOneByNameQueries("YvsGrhCongeEmps.findById", new String[]{"id"}, new Object[]{Long.valueOf(conge)});
            if (doc != null ? doc.getId() > 0 : false) {
                YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(currentUser)});
                YvsAgences agence = doc.getEmploye() != null ? doc.getEmploye().getAgence() : null;

                List<YvsWorkflowValidConge> ls = dao.loadNameQueries("YvsWorkflowValidConge.findByDocument", new String[]{"document"}, new Object[]{doc});
                YvsWorkflowValidConge workflow = ordonneEtapesConge(ls, false);
                if (workflow != null) {
                    if (!asDroitValideEtape(workflow.getEtape(), new YvsNiveauAcces(Long.valueOf(niveauAcces)))) {
                        reponse = "Vous ne disposez pas d'autorisation nécessaire";
                    }
                    if (!asString(reponse)) {
                        reponse = dao.validEtapeOrdreConge(doc, workflow, yvsUsersAgence(user, agence));
                    }
                } else {
                    reponse = "Validation Impossible";
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            reponse = "Validation Impossible";
        }
        System.out.println("ValidationConge : " + reponse);
        return reponse;
    }

    @POST
    @Path("AnnulationConge")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String AnnulationConge(@HeaderParam("conge") String conge, @HeaderParam("motif") String motif, @HeaderParam("currentUser") String currentUser, @HeaderParam("niveauAcces") String niveauAcces
    ) {
        String reponse = null;
        try {
            YvsGrhCongeEmps doc = (YvsGrhCongeEmps) dao.loadOneByNameQueries("YvsGrhCongeEmps.findById", new String[]{"id"}, new Object[]{Long.valueOf(conge)});
            if (doc != null ? doc.getId() > 0 : false) {
                YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(currentUser)});
                YvsAgences agence = doc.getEmploye() != null ? doc.getEmploye().getAgence() : null;

                List<YvsWorkflowValidConge> ls = dao.loadNameQueries("YvsWorkflowValidConge.findByDocument", new String[]{"document"}, new Object[]{doc});
                YvsWorkflowValidConge workflow = ordonneEtapesConge(ls, true);
                if (workflow != null) {
                    if (!asDroitValideEtape(workflow.getEtape(), new YvsNiveauAcces(Long.valueOf(niveauAcces)))) {
                        reponse = "Vous ne disposez pas d'autorisation nécessaire";
                    }
                    if (!asString(reponse)) {
                        reponse = dao.annulEtapeOrdreConge(doc, workflow, motif, yvsUsersAgence(user, agence));
                    }
                } else {
                    reponse = "Annulation Impossible";
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            reponse = "Annulation Impossible";
        }
        System.out.println("AnnulationConge : " + reponse);
        return reponse;
    }

    @POST
    @Path("ValidationMission")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String ValidationMission(@HeaderParam("mission") String mission, @HeaderParam("currentUser") String currentUser, @HeaderParam("niveauAcces") String niveauAcces
    ) {
        String reponse = null;
        try {
            YvsGrhMissions doc = (YvsGrhMissions) dao.loadOneByNameQueries("YvsMissions.findById", new String[]{"id"}, new Object[]{Long.valueOf(mission)});
            if (doc != null ? doc.getId() > 0 : false) {
                YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(currentUser)});
                YvsAgences agence = doc.getEmploye() != null ? doc.getEmploye().getAgence() : null;

                List<YvsWorkflowValidMission> ls = dao.loadNameQueries("YvsWorkflowValidMission.findByMission", new String[]{"mission"}, new Object[]{doc});
                YvsWorkflowValidMission workflow = ordonneEtapesMission(ls, false);
                if (workflow != null) {
                    if (!asDroitValideEtape(workflow.getEtape(), new YvsNiveauAcces(Long.valueOf(niveauAcces)))) {
                        reponse = "Vous ne disposez pas d'autorisation nécessaire";
                    }
                    if (!asString(reponse)) {
                        reponse = dao.validEtapeOrdreMission(doc, workflow, yvsUsersAgence(user, agence));
                    }
                } else {
                    reponse = "Validation Impossible";
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            reponse = "Validation Impossible";
        }
        System.out.println("ValidationMission : " + reponse);
        return reponse;
    }

    @POST
    @Path("AnnulationMission")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String AnnulationMission(@HeaderParam("mission") String mission, @HeaderParam("motif") String motif, @HeaderParam("currentUser") String currentUser, @HeaderParam("niveauAcces") String niveauAcces
    ) {
        String reponse = null;
        try {
            YvsGrhMissions doc = (YvsGrhMissions) dao.loadOneByNameQueries("YvsMissions.findById", new String[]{"id"}, new Object[]{Long.valueOf(mission)});
            if (doc != null ? doc.getId() > 0 : false) {
                YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(currentUser)});
                YvsAgences agence = doc.getEmploye() != null ? doc.getEmploye().getAgence() : null;

                List<YvsWorkflowValidMission> ls = dao.loadNameQueries("YvsWorkflowValidMission.findByMission", new String[]{"mission"}, new Object[]{doc});
                YvsWorkflowValidMission workflow = ordonneEtapesMission(ls, true);
                if (workflow != null) {
                    if (!asDroitValideEtape(workflow.getEtape(), new YvsNiveauAcces(Long.valueOf(niveauAcces)))) {
                        reponse = "Vous ne disposez pas d'autorisation nécessaire";
                    }
                    if (!asString(reponse)) {
                        reponse = dao.annulEtapeOrdreMission(doc, workflow, motif, yvsUsersAgence(user, agence));
                    }
                } else {
                    reponse = "Annulation Impossible";
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            reponse = "Annulation Impossible";
        }
        System.out.println("AnnulationMission : " + reponse);
        return reponse;
    }

    @POST
    @Path("ValidationDocStock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String ValidationDocStock(@HeaderParam("docStock") String docStock, @HeaderParam("currentUser") String currentUser, @HeaderParam("niveauAcces") String niveauAcces
    ) {
        String reponse = null;
        try {
            YvsComDocStocks doc = (YvsComDocStocks) dao.loadOneByNameQueries("YvsComDocStocks.findById", new String[]{"id"}, new Object[]{Long.valueOf(docStock)});
            if (doc != null ? doc.getId() > 0 : false) {
                YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(currentUser)});
                YvsAgences agence = doc.getDestination() != null ? doc.getDestination().getAgence() : doc.getSource().getAgence();
                List<YvsWorkflowValidDocStock> ls = dao.loadNameQueries("YvsWorkflowValidDocStock.findByFacture", new String[]{"facture"}, new Object[]{doc});
                YvsWorkflowValidDocStock workflow = ordonneEtapesStocks(ls, false);
                if (workflow != null) {
                    if (!asDroitValideEtape(workflow.getEtape(), new YvsNiveauAcces(Long.valueOf(niveauAcces)))) {
                        reponse = "Vous ne disposez pas d'autorisation nécessaire";
                    }
                    if (!asString(reponse)) {
                        reponse = dao.validEtapeDocStock(doc, workflow, yvsUsersAgence(user, agence));
                    }
                } else {
                    reponse = "Validation Impossible";
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            reponse = "Validation Impossible";
        }
        System.out.println("ValidationDocStock : " + reponse);
        return reponse;
    }

    @POST
    @Path("AnnulationDocStock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String AnnulationDocStock(@HeaderParam("docStock") String docStock, @HeaderParam("motif") String motif, @HeaderParam("currentUser") String currentUser, @HeaderParam("niveauAcces") String niveauAcces
    ) {
        String reponse = null;
        try {
            YvsComDocStocks doc = (YvsComDocStocks) dao.loadOneByNameQueries("YvsComDocStocks.findById", new String[]{"id"}, new Object[]{Long.valueOf(docStock)});
            if (doc != null ? doc.getId() > 0 : false) {
                YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(currentUser)});
                YvsAgences agence = doc.getDestination() != null ? doc.getDestination().getAgence() : doc.getSource().getAgence();

                List<YvsWorkflowValidDocStock> ls = dao.loadNameQueries("YvsWorkflowValidDocStock.findByFacture", new String[]{"facture"}, new Object[]{doc});
                YvsWorkflowValidDocStock workflow = ordonneEtapesStocks(ls, true);
                if (workflow != null) {
                    if (!asDroitValideEtape(workflow.getEtape(), new YvsNiveauAcces(Long.valueOf(niveauAcces)))) {
                        reponse = "Vous ne disposez pas d'autorisation nécessaire";
                    }
                    if (!asString(reponse)) {
                        reponse = dao.annulEtapeDocStock(doc, workflow, motif, yvsUsersAgence(user, agence));
                    }
                } else {
                    reponse = "Annulation Impossible";
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            reponse = "Annulation Impossible";
        }
        System.out.println("AnnulationDocStock : " + reponse);
        return reponse;
    }

    @POST
    @Path("ValidationApprovisionnement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String ValidationApprovisionnement(@HeaderParam("fiche") String fiche, @HeaderParam("currentUser") String currentUser, @HeaderParam("niveauAcces") String niveauAcces
    ) {
        String reponse = null;
        try {
            YvsComFicheApprovisionnement doc = (YvsComFicheApprovisionnement) dao.loadOneByNameQueries("YvsComFicheApprovisionnement.findById", new String[]{"id"}, new Object[]{Long.valueOf(fiche)});
            if (doc != null ? doc.getId() > 0 : false) {
                YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(currentUser)});
                YvsAgences agence = doc.getDepot() != null ? doc.getDepot().getAgence() : null;

                List<YvsWorkflowValidApprovissionnement> ls = dao.loadNameQueries("YvsWorkflowValidApprovissionnement.findByFacture", new String[]{"facture"}, new Object[]{doc});
                YvsWorkflowValidApprovissionnement workflow = ordonneEtapesApprovissionnement(ls, false);
                if (workflow != null) {
                    if (!asDroitValideEtape(workflow.getEtape(), new YvsNiveauAcces(Long.valueOf(niveauAcces)))) {
                        reponse = "Vous ne disposez pas d'autorisation nécessaire";
                    }
                    if (!asString(reponse)) {
                        reponse = dao.validEtapeApprovisionnement(doc, workflow, yvsUsersAgence(user, agence));
                    }
                } else {
                    reponse = "Validation Impossible";
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            reponse = "Validation Impossible";
        }
        System.out.println("ValidationApprovisionnement : " + reponse);
        return reponse;
    }

    @POST
    @Path("AnnulationApprovisionnement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String AnnulationApprovisionnement(@HeaderParam("fiche") String fiche, @HeaderParam("motif") String motif, @HeaderParam("currentUser") String currentUser, @HeaderParam("niveauAcces") String niveauAcces
    ) {
        String reponse = null;
        try {
            YvsComFicheApprovisionnement doc = (YvsComFicheApprovisionnement) dao.loadOneByNameQueries("YvsComFicheApprovisionnement.findById", new String[]{"id"}, new Object[]{Long.valueOf(fiche)});
            if (doc != null ? doc.getId() > 0 : false) {
                YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(currentUser)});
                YvsAgences agence = doc.getDepot() != null ? doc.getDepot().getAgence() : null;

                List<YvsWorkflowValidApprovissionnement> ls = dao.loadNameQueries("YvsWorkflowValidApprovissionnement.findByFacture", new String[]{"facture"}, new Object[]{doc});
                YvsWorkflowValidApprovissionnement workflow = ordonneEtapesApprovissionnement(ls, true);
                if (workflow != null) {
                    if (!asDroitValideEtape(workflow.getEtape(), new YvsNiveauAcces(Long.valueOf(niveauAcces)))) {
                        reponse = "Vous ne disposez pas d'autorisation nécessaire";
                    }
                    if (!asString(reponse)) {
                        reponse = dao.annulEtapeApprovisionnement(doc, workflow, motif, yvsUsersAgence(user, agence));
                    }
                } else {
                    reponse = "Annulation Impossible";
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            reponse = "Annulation Impossible";
        }
        System.out.println("AnnulationApprovisionnement : " + reponse);
        return reponse;
    }

    @POST
    @Path("ValidationBonProvisoire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String ValidationBonProvisoire(@HeaderParam("bon") String bon, @HeaderParam("currentUser") String currentUser, @HeaderParam("niveauAcces") String niveauAcces
    ) {
        String reponse = null;
        try {
            YvsComptaBonProvisoire doc = (YvsComptaBonProvisoire) dao.loadOneByNameQueries("YvsComptaBonProvisoire.findById", new String[]{"id"}, new Object[]{Long.valueOf(bon)});
            if (doc != null ? doc.getId() > 0 : false) {
                YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(currentUser)});
                YvsAgences agence = doc.getAgence();

                List<YvsWorkflowValidBonProvisoire> ls = dao.loadNameQueries("YvsWorkflowValidBonProvisoire.findByFacture", new String[]{"facture"}, new Object[]{doc});
                YvsWorkflowValidBonProvisoire workflow = ordonneEtapesBonProvisoire(ls, false);
                if (workflow != null) {
                    if (!asDroitValideEtape(workflow.getEtape(), new YvsNiveauAcces(Long.valueOf(niveauAcces)))) {
                        reponse = "Vous ne disposez pas d'autorisation nécessaire";
                    }
                    if (!asString(reponse)) {
                        reponse = dao.validEtapeBonProvisoire(doc, workflow, yvsUsersAgence(user, agence));
                    }
                } else {
                    reponse = "Validation Impossible";
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            reponse = "Validation Impossible";
        }
        System.out.println("ValidationApprovisionnement : " + reponse);
        return reponse;
    }

    @POST
    @Path("AnnulationBonProvisoire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String AnnulationBonProvisoire(@HeaderParam("bon") String bon, @HeaderParam("motif") String motif, @HeaderParam("currentUser") String currentUser, @HeaderParam("niveauAcces") String niveauAcces
    ) {
        String reponse = null;
        try {
            YvsComptaBonProvisoire doc = (YvsComptaBonProvisoire) dao.loadOneByNameQueries("YvsComptaBonProvisoire.findById", new String[]{"id"}, new Object[]{Long.valueOf(bon)});
            if (doc != null ? doc.getId() > 0 : false) {
                YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(currentUser)});
                YvsAgences agence = doc.getAgence();

                List<YvsWorkflowValidBonProvisoire> ls = dao.loadNameQueries("YvsWorkflowValidBonProvisoire.findByFacture", new String[]{"facture"}, new Object[]{doc});
                YvsWorkflowValidBonProvisoire workflow = ordonneEtapesBonProvisoire(ls, true);
                if (workflow != null) {
                    if (!asDroitValideEtape(workflow.getEtape(), new YvsNiveauAcces(Long.valueOf(niveauAcces)))) {
                        reponse = "Vous ne disposez pas d'autorisation nécessaire";
                    }
                    if (!asString(reponse)) {
                        reponse = dao.annulEtapeBonProvisoire(doc, workflow, motif, yvsUsersAgence(user, agence));
                    }
                } else {
                    reponse = "Annulation Impossible";
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            reponse = "Annulation Impossible";
        }
        System.out.println("AnnulationApprovisionnement : " + reponse);
        return reponse;
    }

    /*BEGIN FONCTION SECONDAIRE*/
    private YvsWorkflowValidFactureAchat ordonneEtapesAchats(List<YvsWorkflowValidFactureAchat> l, boolean current) {
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

        YvsWorkflowValidFactureAchat etape = null;
        for (YvsWorkflowValidFactureAchat w : re) {
            if (!current) {
                if (w.isEtapeActive() == true ? w.getEtapeValid() == false : false) {
                    return w;
                }
            } else {
                if (w.getEtapeValid()) {
                    etape = w;
                } else {
                    return etape;
                }
            }
        }
        return null;
    }

    private YvsWorkflowValidFactureVente ordonneEtapesVentes(List<YvsWorkflowValidFactureVente> l, boolean current) {
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
        YvsWorkflowValidFactureVente etape = null;
        for (YvsWorkflowValidFactureVente w : re) {
            if (!current) {
                if (w.isEtapeActive() == true ? w.getEtapeValid() == false : false) {
                    return w;
                }
            } else {
                if (w.getEtapeValid()) {
                    etape = w;
                } else {
                    return etape;
                }
            }
        }
        return null;
    }

    private YvsWorkflowValidDocCaisse ordonneEtapesDivers(List<YvsWorkflowValidDocCaisse> l, boolean current) {
        System.err.println("ls : " + l);
        List<YvsWorkflowValidDocCaisse> re = new ArrayList<>();
        YvsWorkflowValidDocCaisse first = null;
        //recherche la première étape       
        for (YvsWorkflowValidDocCaisse vm : l) {
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
        System.err.println("re : " + re);
        YvsWorkflowValidDocCaisse etape = null;
        for (YvsWorkflowValidDocCaisse w : re) {
            System.err.println("w : " + w.getEtape().getLabelStatut() + " (" + w.isEtapeActive() + ") (" + w.getEtapeValid() + ")");
            if (!current) {
                if (w.isEtapeActive() == true ? w.getEtapeValid() == false : false) {
                    return w;
                }
            } else {
                if (w.getEtapeValid()) {
                    etape = w;
                } else {
                    return etape;
                }
            }
        }
        return null;
    }

    private YvsWorkflowValidConge ordonneEtapesConge(List<YvsWorkflowValidConge> l, boolean current) {
        List<YvsWorkflowValidConge> re = new ArrayList<>();
        YvsWorkflowValidConge first = null;
        //recherche la première étape       
        for (YvsWorkflowValidConge vm : l) {
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
                for (YvsWorkflowValidConge vm : l) {
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
        for (YvsWorkflowValidConge vm : re) {
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
        YvsWorkflowValidConge etape = null;
        for (YvsWorkflowValidConge w : re) {
            if (!current) {
                if (w.isEtapeActive() == true ? w.getEtapeValid() == false : false) {
                    return w;
                }
            } else {
                if (w.getEtapeValid()) {
                    etape = w;
                } else {
                    return etape;
                }
            }
        }
        return null;
    }

    private YvsWorkflowValidMission ordonneEtapesMission(List<YvsWorkflowValidMission> l, boolean current) {
        List<YvsWorkflowValidMission> re = new ArrayList<>();
        YvsWorkflowValidMission first = null;
        //recherche la première étape       
        for (YvsWorkflowValidMission vm : l) {
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
                for (YvsWorkflowValidMission vm : l) {
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
        for (YvsWorkflowValidMission vm : re) {
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
        YvsWorkflowValidMission etape = null;
        for (YvsWorkflowValidMission w : re) {
            if (!current) {
                if (w.isEtapeActive() == true ? w.getEtapeValid() == false : false) {
                    return w;
                }
            } else {
                if (w.getEtapeValid()) {
                    etape = w;
                } else {
                    return etape;
                }
            }
        }
        return null;
    }

    private YvsWorkflowValidDocStock ordonneEtapesStocks(List<YvsWorkflowValidDocStock> l, boolean current) {
        List<YvsWorkflowValidDocStock> re = new ArrayList<>();
        YvsWorkflowValidDocStock first = null;
        //recherche la première étape       
        for (YvsWorkflowValidDocStock vm : l) {
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
                for (YvsWorkflowValidDocStock vm : l) {
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
        for (YvsWorkflowValidDocStock vm : re) {
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
        YvsWorkflowValidDocStock etape = null;
        for (YvsWorkflowValidDocStock w : re) {
            if (!current) {
                if (w.isEtapeActive() == true ? w.getEtapeValid() == false : false) {
                    return w;
                }
            } else {
                if (w.getEtapeValid()) {
                    etape = w;
                } else {
                    return etape;
                }
            }
        }
        return null;
    }

    private YvsWorkflowValidApprovissionnement ordonneEtapesApprovissionnement(List<YvsWorkflowValidApprovissionnement> l, boolean current) {
        List<YvsWorkflowValidApprovissionnement> re = new ArrayList<>();
        YvsWorkflowValidApprovissionnement first = null;
        //recherche la première étape       
        for (YvsWorkflowValidApprovissionnement vm : l) {
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
                for (YvsWorkflowValidApprovissionnement vm : l) {
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
        for (YvsWorkflowValidApprovissionnement vm : re) {
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
        YvsWorkflowValidApprovissionnement etape = null;
        for (YvsWorkflowValidApprovissionnement w : re) {
            if (!current) {
                if (w.isEtapeActive() == true ? w.getEtapeValid() == false : false) {
                    return w;
                }
            } else {
                if (w.getEtapeValid()) {
                    etape = w;
                } else {
                    return etape;
                }
            }
        }
        return null;
    }

    private YvsWorkflowValidBonProvisoire ordonneEtapesBonProvisoire(List<YvsWorkflowValidBonProvisoire> l, boolean current) {
        List<YvsWorkflowValidBonProvisoire> re = new ArrayList<>();
        YvsWorkflowValidBonProvisoire first = null;
        //recherche la première étape       
        for (YvsWorkflowValidBonProvisoire vm : l) {
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
                for (YvsWorkflowValidBonProvisoire vm : l) {
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
        for (YvsWorkflowValidBonProvisoire vm : re) {
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
        YvsWorkflowValidBonProvisoire etape = null;
        for (YvsWorkflowValidBonProvisoire w : re) {
            if (!current) {
                if (w.isEtapeActive() == true ? w.getEtapeValid() == false : false) {
                    return w;
                }
            } else {
                if (w.getEtapeValid()) {
                    etape = w;
                } else {
                    return etape;
                }
            }
        }
        return null;
    }
    /*END FONCTION SECONDAIRE*/
}
