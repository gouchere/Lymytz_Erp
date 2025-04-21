/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws.compta;

import java.util.ArrayList;
import java.util.Date;
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
import yvs.dao.salaire.service.ResultatAction;
import yvs.dao.services.compta.ServiceComptabilite;
import yvs.dao.services.compta.ServiceMvtCaisse;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.util.Constantes;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
@Path("services/compta")
@RequestScoped
public class GenericResource extends lymytz.ws.GenericResource {

    private YvsBaseModeReglement modeByEspece = null;

    @POST
    @Path("saveVirementCaisse")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultatAction saveVirementCaisse(@HeaderParam("caisseSource") String caisseSource, @HeaderParam("caisseCible") String caisseCible, @HeaderParam("montant") String _montant, @HeaderParam("idUser") String _idUserA, @HeaderParam("header") String _idHeader, @HeaderParam("versementAttendu") String _versementAttendu) {
        try {
            Long idScrce = Long.valueOf(caisseSource);
            Long idDestination = Long.valueOf(caisseCible);
            Long idUserA = Long.valueOf(_idUserA);
            Long idHeader = Long.valueOf(_idHeader);
            Double montant = Double.valueOf(_montant);
            Double vstAttendu = Double.valueOf(_versementAttendu);
            YvsBaseCaisse srce = (YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{idScrce});
            YvsBaseCaisse cible = (YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{idDestination});
            YvsUsersAgence userA = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findById", new String[]{"id"}, new Object[]{idUserA});
            YvsComEnteteDocVente header = (YvsComEnteteDocVente) dao.loadOneByNameQueries("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{idHeader});
            ServiceMvtCaisse service = new ServiceMvtCaisse(dao, null, userA);
            YvsComptaCaissePieceVirement vir = new YvsComptaCaissePieceVirement();
            vir.setAuthor(userA);
            vir.setCaissierSource(userA.getUsers());
            vir.setCible(cible);
            vir.setDatePaiement(null);
            vir.setDatePaimentPrevu(new Date());
            vir.setDatePiece(new Date());
            vir.setDateSave(new Date());
            vir.setDateUpdate(new Date());
            vir.setModel(service.modeEspece(userA.getAgence().getSociete()));
            vir.setMontant(montant);
            vir.setSource(srce);
            vir.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
            return service.saveNewVirement(vir, header, vstAttendu);
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("getFactures")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<YvsComContenuDocVente> getFactures(@HeaderParam("numFacture") String numFacture, @HeaderParam("societe") String societe) {
        List<YvsComContenuDocVente> list = new ArrayList<>();
        try {
            if (asString(numFacture) && asString(societe)) {
                list = dao.loadNameQueries("YvsComContenuDocVente.findByNumeroFacture", new String[]{"numDoc", "societe"}, new Object[]{numFacture, new YvsSocietes(Long.valueOf(societe))});
                YvsComDocVentes d = list.get(0).getDocVente();
                double montant = dao.setMontantTotalDoc(d, d.getEnteteDoc().getCreneau().getUsers().getAgence().getSociete().getId());
                d.setTotal(montant);
                if (list != null ? !list.isEmpty() : false) {

                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return list;
    }

    @POST
    @Path("returnCaisses")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseCaisse> returnCaisses(@HeaderParam("users") String users) {
        try {
            if (asString(users)) {
                YvsUsers y = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(users)});
                List<YvsBaseCaisse> list = dao.loadNameQueries("YvsBaseCaisseUser.findCaisseByUsers", new String[]{"users"}, new Object[]{y});
                List<YvsBaseCaisse> others = dao.loadNameQueries("YvsBaseCaisse.findByCaissier", new String[]{"caissier", "societe"}, new Object[]{y, y.getAgence().getSociete()});
                for (YvsBaseCaisse c : others) {
                    if (!list.contains(c)) {
                        list.add(c);
                    }
                }
                return list;
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("livrer")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean livrer(@HeaderParam("doc") String doc, @HeaderParam("depot") String depot, @HeaderParam("tranche") String tranche) {
        boolean ok = false;
        try {
            if (asString(doc)) {
                YvsComDocVentes docVente = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{Long.valueOf(doc)});
                if (docVente.getStatutLivre().equals("L")) {
                    ok = true;
                } else {
                    docVente.setStatut("L");
                    YvsComDocVentes bl = new YvsComDocVentes(docVente);
                    bl.setDateLivraison(new Date());
                    bl.setDateUpdate(new Date());
                    bl.setDateSave(new Date());
                    bl.setNumDoc(dao.genererReference(Constantes.TYPE_BLV_NAME, docVente.getEnteteDoc().getDateEntete(), Long.valueOf(depot), docVente.getEnteteDoc().getAgence().getSociete(), docVente.getEnteteDoc().getAgence()));
                    bl.setHeureDoc(new Date());
                    bl.setTypeDoc(Constantes.TYPE_BLV);
                    bl.setStatut(Constantes.ETAT_VALIDE);
                    bl.setId(null);
                    bl.setDepotLivrer(new YvsBaseDepots(Long.valueOf(depot)));
                    bl.setTrancheLivrer(new YvsGrhTrancheHoraire(Long.valueOf(tranche)));
                    bl.setDocumentLie(docVente);
                    bl = (YvsComDocVentes) dao.save1(bl);

                    if (bl != null ? bl.getId() > 0 : false) {

                        List<YvsComContenuDocVente> contenus = new ArrayList<>(docVente.getContenus());
                        for (YvsComContenuDocVente c : contenus) {
                            c.setId(null);
                            c.setDocVente(bl);
                            c.setDateSave(new Date());
                            c.setDateUpdate(new Date());
                            c.setDateContenu(new Date());
                            dao.save(c);

                        }

                        docVente.setStatutLivre("L");
                        dao.update(docVente);
                        ok = true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return true;
    }

    @POST
    @Path("regler")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean regler(@HeaderParam("doc") String doc, @HeaderParam("caisse") String caisse, @HeaderParam("user") String user, @HeaderParam("montant_versser") String montant_versser) {
        boolean ok = false;
        try {
            if (Util.asString(montant_versser) ? Double.valueOf(montant_versser) > 0 : false) {
                double montant_verser = Double.valueOf(montant_versser);
                YvsComDocVentes docVente = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{Long.valueOf(doc)});
                if (docVente != null ? docVente.getId() > 0 : false) {
                    YvsAgences agence = docVente.getEnteteDoc().getCreneau().getUsers().getAgence();
                    YvsSocietes societe = docVente.getEnteteDoc().getCreneau().getUsers().getAgence().getSociete();
                    dao.loadInfos(societe, agence, null, null, null, null);
                    System.out.println("societe = " + societe.getName());
                    if (societe != null ? societe.getId() > 0 : false) {
                        double net_a_payer = dao.setMontantTotalDoc(docVente, societe.getId());
                        System.out.println("montant = " + net_a_payer);
                        List<YvsComptaCaissePieceVente> pieces = dao.loadNameQueries("YvsComptaCaissePieceVente.findByFacture", new String[]{"facture"}, new Object[]{docVente});
                        if (pieces != null ? !pieces.isEmpty() : false) {
                            for (YvsComptaCaissePieceVente pi : pieces) {
                                pi.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                                dao.update(pi);
                                ok = true;
                            }
                            docVente.setStatutRegle(Constantes.ETAT_REGLE);
                            dao.update(docVente);
                        } else {
                            double montant = net_a_payer;
                            if (montant_verser < net_a_payer) {
                                montant = Double.valueOf(montant_versser);
                            }
                            YvsComptaCaissePieceVente p = new YvsComptaCaissePieceVente();
                            p.setCaisse(new YvsBaseCaisse(Long.valueOf(caisse)));
                            p.setCaissier(new YvsUsers(Long.valueOf(user)));
                            p.setDatePaiement(new Date());
                            p.setDatePiece(new Date());
                            p.setDateSave(new Date());
                            p.setDateUpdate(new Date());
                            p.setMontant(montant);
                            p.setMontantRecu(montant_verser);
                            p.setModel(modeEspece(societe));
                            p.setVente(docVente);
                            p.setDatePaiement(new Date());
                            p.setMouvement(Constantes.MOUV_CAISS_ENTREE.charAt(0));
                            p.setNumeroPiece(dao.genererReference(Constantes.TYPE_PC_VENTE_NAME, new Date(), Long.valueOf(caisse), societe, agence));
                            System.out.println(p.getNumeroPiece());
                            p.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                            p = (YvsComptaCaissePieceVente) dao.save1(p);
                            System.out.println("piece = " + p.getId());
                            if (p != null ? p.getId() > 0 : false) {
                                if (net_a_payer == montant) {
                                    docVente.setStatutRegle(Constantes.ETAT_REGLE);
                                } else {
                                    docVente.setStatutRegle(Constantes.ETAT_ENCOURS);
                                }
                                dao.update(docVente);
                                ok = true;
                            }

                        }
                    }
                }
                if (asString(doc)) {

                }
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return ok;
    }

    public YvsBaseModeReglement modeEspece(YvsSocietes currentScte) {
        if (dao != null ? modeByEspece == null : false) {
            nameQueri = "YvsBaseModeReglement.findByDefault";
            champ = new String[]{"societe", "actif", "type", "defaut"};
            val = new Object[]{currentScte, true, Constantes.MODE_PAIEMENT_ESPECE, true};
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

    @POST
    @Path("comptabiliseVente")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultatAction comptabiliserVente(@HeaderParam("doc") String doc, @HeaderParam("idUser") String idUserA) {
        if (asString(doc)) {
            Long idDoc = Long.valueOf(doc);
            Long idU = Long.valueOf(idUserA);
            if (idDoc > 0) {
                YvsUsersAgence user = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findById", new String[]{"id"}, new Object[]{idU});
                //Récupère le niveau du user dans la société
                YvsNiveauAcces n = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauUsers.findNiveauByUser", new String[]{"user", "societe"}, new Object[]{user.getUsers(), user.getAgence().getSociete()});
                ServiceComptabilite service = new ServiceComptabilite(n, user, dao);
                YvsComDocVentes y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{idDoc});
                ResultatAction re = service.comptabiliserVente(y);
                return re;
            }
        }
        return null;
    }

    @POST
    @Path("getPieceComptable")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public YvsComptaPiecesComptable getPieceComptable(@HeaderParam("id") String id) {
        return (YvsComptaPiecesComptable) dao.loadOneByNameQueries("YvsComptaPiecesComptable.findById", new String[]{"id"}, new Object[]{Long.valueOf(id)});
    }

    @POST
    @Path("getSoldeCaisses")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Double getSoldeCaisse(@HeaderParam("caisse") String caisse, @HeaderParam("table") String table, @HeaderParam("statut") String statut) {
        return dao.getSoldeCaisse(Long.valueOf(caisse), table, statut.charAt(0));
    }

    @POST
    @Path("getDepenseCaisse")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Double getDepenseCaisse(@HeaderParam("caisse") String caisse, @HeaderParam("table") String table, @HeaderParam("statut") String statut) {
        return dao.getDepenseCaisse(Long.valueOf(caisse), table, statut.charAt(0));
    }

    @POST
    @Path("getRecetteCaisse")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Double getRecetteCaisse(@HeaderParam("caisse") String caisse, @HeaderParam("table") String table, @HeaderParam("statut") String statut) {
        return dao.getRecetteCaisse(Long.valueOf(caisse), table, statut.charAt(0));
    }

}
//