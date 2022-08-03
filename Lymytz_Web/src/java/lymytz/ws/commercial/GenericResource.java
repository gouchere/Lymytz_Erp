/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws.commercial;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import yvs.dao.Options;
import yvs.dao.salaire.service.ResultatAction;
import yvs.dao.services.commercial.ServiceTransfert;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseMouvementStock;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsBasePlanTarifaire;
import yvs.entity.commercial.client.YvsComCategorieTarifaire;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;
import yvs.entity.commercial.creneau.YvsComCreneauPoint;
import yvs.entity.commercial.rrr.YvsComPlanRistourne;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.vente.YvsComCommercialVente;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.commercial.vente.YvsComMensualiteFactureVente;
import yvs.entity.commercial.vente.YvsComTaxeContenuVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidFactureVente;
import yvs.entity.produits.YvsBaseArticleCodeBarre;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsNiveauUsers;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.util.Constantes;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;
import yvs.util.Utilitaire;

/**
 *
 * @author Lymytz Dowes
 */
@Path("services/commercial")
@RequestScoped
public class GenericResource extends lymytz.ws.market.GenericResource {

    private void initDroit(PaginatorResult<YvsComDocVentes> paginator) {
        ParametreRequete p;
        switch (buildDocByDroit(Constantes.TYPE_FV)) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.agence.societe", "societe", currentNiveau.getIdNiveau().getSociete(), "=", "AND");
                paginator.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.agence", "agence", currentNiveau.getIdUser().getAgence(), "=", "AND");
                paginator.addParam(p);
                break;
            case 3: { //charge tous les document des points de vente où l'utilisateurs est responsable
                //cherche les points de vente de l'utilisateur
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdPointByUsers", new String[]{"users", "date", "hier"}, new Object[]{currentNiveau.getIdUser(), (Utilitaire.getIniTializeDate(new Date()).getTime()), Constantes.getPreviewDate(new Date())});
                if (ids.isEmpty()) {
                    ids.add(-1L);
                }
                p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.id", "ids", ids, " IN ", "AND");
                paginator.addParam(p);
                break;
            }
            case 4: {//charge tous les document des depots où l'utilisateurs est responsable
                //cherche les points de vente de l'utilisateur rattaché au depot
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentNiveau.getIdUser()});
                if (currentNiveau.getIdUser() != null) {
                    ids.addAll(dao.loadNameQueries("YvsBaseDepots.findIdByResponsable", new String[]{"responsable"}, new Object[]{currentNiveau.getIdUser().getEmploye()}));
                }
                if (!ids.isEmpty()) {
                    ids = dao.loadNameQueries("YvsBasePointVenteDepot.findIdPointByDepot", new String[]{"ids"}, new Object[]{ids});
                    if (ids.isEmpty()) {
                        ids.add(-1L);
                    }
                } else {
                    ids.add(-1L);
                }
                p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.id", "ids", ids, " IN ", "AND");
                paginator.addParam(p);
                break;
            }
            default:    //charge les document de l'utilisateur connecté dans les restriction de paramDate données
                p = new ParametreRequete("y.enteteDoc.creneau.users ", "users", currentNiveau.getIdUser(), "=", "AND");
                paginator.addParam(p);
                break;
        }
    }

    @POST
    @Path("defaultClient")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public YvsComClient defaultClient(@HeaderParam("societe") String societe, @HeaderParam("adresse") String adresse) {
        try {
            YvsComClient y = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findDefautVille", new String[]{"societe", "ville"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), new YvsDictionnaire(Long.valueOf(adresse))});
            if (y != null ? y.getId() < 1 : true) {
                y = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findDefautPays", new String[]{"societe", "pays"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), new YvsDictionnaire(Long.valueOf(adresse))});
            }
            return y;
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("stock")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    //stocks(long art, long trh, long depot, long ag, long ste, Date dat, long unite, long lot)
    public Double stock(@HeaderParam("societe") String societe, @HeaderParam("agence") String agence, @HeaderParam("depot") String depot, @HeaderParam("tranche") String tranche, @HeaderParam("article") String article, @HeaderParam("unite") String unite, @HeaderParam("lot") String lot, @HeaderParam("date") String date) {
        try {
            return dao.stocks(Long.valueOf(article), Long.valueOf(tranche), Long.valueOf(depot), Long.valueOf(agence), Long.valueOf(societe), df.parse(date), Long.valueOf(unite), Long.valueOf(lot));
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("get_stock")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    //stocks(long art, long trh, long depot, long ag, long ste, Date dat, long unite, long lot)
    public Double getStock(@HeaderParam("societe") String societe, @HeaderParam("agence") String agence, @HeaderParam("depot") String depot, @HeaderParam("tranche") String tranche, @HeaderParam("article") String article, @HeaderParam("unite") String unite, @HeaderParam("lot") String lot, @HeaderParam("date") String date) {
        try {
            return dao.stocks(Long.valueOf(article), Long.valueOf(tranche), Long.valueOf(depot), Long.valueOf(agence), Long.valueOf(societe), df.parse(date), Long.valueOf(unite), Long.valueOf(lot));
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("get_ristourne")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    //stocks(long art, long trh, long depot, long ag, long ste, Date dat, long unite, long lot)
    public Double getRistourne(@HeaderParam("unite") String unite, @HeaderParam("quantite") String quantite, @HeaderParam("prix") String prix, @HeaderParam("client") String client, @HeaderParam("date") String date) {
        try {
            return dao.getRistourne(Long.valueOf(unite), Double.valueOf(quantite), Double.valueOf(prix), Long.valueOf(client), df.parse(date));
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("equilibre_vente_livre")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public String getEquilibreVenteLivre(@HeaderParam("facture") String facture) {
        try {
            return dao.getEquilibreVenteLivre(Long.valueOf(facture));
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("equilibre_vente_regle")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public String getEquilibreVenteRegle(@HeaderParam("facture") String facture) {
        try {
            return dao.getEquilibreVenteRegle(Long.valueOf(facture));
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("equilibre_vente")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> getEquilibreVente(@HeaderParam("facture") String facture) {
        try {
            return dao.getEquilibreVente(Long.valueOf(facture));
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnClients")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<YvsComClient> returnClients(@HeaderParam("societe") String societe) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                return dao.loadNameQueries("YvsComClient.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnClientsNotIds")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<YvsComClient> returnClientsNotIds(@HeaderParam("societe") String societe, @HeaderParam("ids") String ids) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                List<Long> value = new ArrayList<>();
                if (asString(ids)) {
                    for (String id : ids.split(",")) {
                        value.add(Long.valueOf(id));
                    }
                }
                if (value.isEmpty()) {
                    value.add(-1L);
                }
                return dao.loadNameQueries("YvsComClient.findAllNotIds", new String[]{"societe", "ids"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), value});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnPlanRistourneNotIds")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<YvsComPlanRistourne> returnPlanRistourneNotIds(@HeaderParam("societe") String societe, @HeaderParam("ids") String ids) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                List<Long> value = new ArrayList<>();
                if (asString(ids)) {
                    for (String id : ids.split(",")) {
                        value.add(Long.valueOf(id));
                    }
                }
                if (value.isEmpty()) {
                    value.add(-1L);
                }
                return dao.loadNameQueries("YvsComPlanRistourne.findAllNotIds", new String[]{"societe", "ids"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), value});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnCategoriesClient")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<YvsBaseCategorieClient> returnCategoriesClient(@HeaderParam("societe") String societe) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                return dao.loadNameQueries("YvsBaseCategorieClient.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("getPlanTarifaire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBasePlanTarifaire> getPlanTarifaire(@HeaderParam("article") String article, @HeaderParam("categorie") String categorie
    ) {
        try {
            return dao.loadNameQueries("YvsBasePlanTarifaire.findByArticleCategorie", new String[]{"article", "categorie"}, new Object[]{new YvsBaseArticles(Long.valueOf(article)), new YvsBaseCategorieClient(Long.valueOf(categorie))});
        } catch (Exception e) {
            System.out.println("erreur = " + e.getMessage());
        }
        return null;
    }

    @POST
    @Path("returnPlanTarifaire")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<YvsBasePlanTarifaire> returnPlanTarifaire(@HeaderParam("societe") String societe) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                return dao.loadNameQueries("YvsBasePlanTarifaire.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnPieceCaisse")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<YvsComptaCaissePieceVente> returnPieceCaisse(@HeaderParam("docVente") String docVente) {
        System.out.println("docVente = " + docVente);
        try {
            if (asString(docVente) ? Long.valueOf(docVente) > 0 : false) {
                return dao.loadNameQueries("YvsComptaCaissePieceVente.findByFacture", new String[]{"facture"}, new Object[]{new YvsComDocVentes(Long.valueOf(docVente))});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("getCategorie")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseCategorieClient> getCategorie(@HeaderParam("societe") String societe
    ) {
        try {
            return dao.loadNameQueries("YvsBaseCategorieClient.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
        } catch (NumberFormatException e) {
            System.out.println("erreur = " + e.getMessage());
        }
        return null;
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
    @Path("returnCategoriesTarifaire")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<YvsComCategorieTarifaire> returnCategoriesTarifaire(@HeaderParam("societe") String societe) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                return dao.loadNameQueries("YvsComCategorieTarifaire.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("returnPointsVente")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_JSON})
    public List<YvsBasePointVente> returnPointsVente(@HeaderParam("societe") String societe) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                return dao.loadNameQueries("YvsBasePointVente.findByActif", new String[]{"societe", "actif"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), true});
            } else {
                return dao.loadNameQueries("YvsBasePointVente.findByVenteOnlineActif", new String[]{"venteOnline", "actif"}, new Object[]{true, true});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("saveEntete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsComEnteteDocVente saveEntete(@HeaderParam("agence") String agence, @HeaderParam("user") String user, @HeaderParam("creneau") String creneau, @HeaderParam("dates") String dates, @HeaderParam("statutLivre") String statutLivre, @HeaderParam("statutRegle") String statutRegle) {
        YvsComEnteteDocVente entete = new YvsComEnteteDocVente();
        try {

            YvsComCreneauHoraireUsers creno = (YvsComCreneauHoraireUsers) dao.loadOneByNameQueries("YvsComCreneauHoraireUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(creneau)});
            if (creno.getActif()) {
                entete.setCreneau(creno);
                Date date = df.parse(dates);
                entete.setAgence(new YvsAgences(Long.valueOf(agence)));
                YvsUsersAgence author = yvsUsersAgence(new YvsUsers(Long.valueOf(user)), new YvsAgences(Long.valueOf(agence)));
                entete.setAuthor(author);

                entete.setDateEntete(date);
                entete.setDateSave(new Date());
                entete.setEtat(Constantes.ETAT_EDITABLE);
                entete.setStatutLivre(statutLivre);
                entete.setStatutRegle(statutRegle);
                entete = (YvsComEnteteDocVente) dao.save1(entete);
                entete.setMessage("Enregistrement effectué avec succès");
                return entete;
            } else {
                entete.setMessage("Vous n'êtes pas planifier");
                entete.setId(0L);
                return entete;
            }

        } catch (NumberFormatException | ParseException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            entete.setMessage("Echec de l'enregistrement");
            entete.setId(0L);
            return entete;
        }

    }

    @POST
    @Path("saveDocVente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsComDocVentes saveDocVente(@HeaderParam("entete") String entete, @HeaderParam("adresse") String adresse, @HeaderParam("categorie") String categorie,
            @HeaderParam("client") String client, @HeaderParam("nom_client") String nom_client, @HeaderParam("statutLivre") String statutLivre, @HeaderParam("statutRegle") String statutRegle, @HeaderParam("depot") String depot) {
        try {
            YvsComDocVentes doc = new YvsComDocVentes();
            YvsComEnteteDocVente entetes = (YvsComEnteteDocVente) dao.loadOneByNameQueries("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{Long.valueOf(entete)});
            YvsComComerciale commercial = (YvsComComerciale) dao.loadOneByNameQueries("YvsComComerciale.findByUser", new String[]{"user"}, new Object[]{entetes.getAuthor().getUsers()});
            YvsUsersAgence author = yvsUsersAgence(entetes.getAuthor().getUsers(), entetes.getAgence());
            doc.setAuthor(author);
            if (Util.asString(adresse)) {
                doc.setAdresse(new YvsDictionnaire(Long.valueOf(adresse)));
            }
            if (Util.asString(categorie)) {
                doc.setCategorieComptable(new YvsBaseCategorieComptable(Long.valueOf(categorie)));
            }

            doc.setEnteteDoc(entetes);
            YvsComClient clients = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findById", new String[]{"id"}, new Object[]{Long.valueOf(client)});
            doc.setClient(clients);
            dao.loadInfos(author.getAgence().getSociete(), author.getAgence(), author, null, null, null);
            doc.setNumDoc(dao.genererReference(Constantes.TYPE_FV_NAME, entetes.getDateEntete(), entetes.getCreneau().getCreneauPoint().getPoint().getId(), author.getAgence().getSociete(), author.getAgence()));

            if (asString(depot)) {
                YvsBaseDepots depots = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{Long.valueOf(depot)});
                doc.setDepotLivrer(depots);
            }
            doc.setDateSave(new Date());
            doc.setDateUpdate(new Date());
            doc.setHeureDoc(new Date());
            doc.setTypeDoc(Constantes.TYPE_FV);
            doc.setStatutLivre(statutLivre);
            doc.setStatutRegle(statutRegle);
            doc.setStatut(Constantes.ETAT_EDITABLE);
            if (clients.getSuiviComptable()) {
                doc.setTiers(clients);
            } else if (commercial != null ? commercial.getTiers() != null ? (commercial.getTiers().getClients() != null ? !commercial.getTiers().getClients().isEmpty() : false) : false : false) {
                doc.setTiers(commercial.getTiers().getClients().get(0));
            }
            doc.setNomClient(nom_client);
            doc.setOperateur(author.getUsers());
            doc = (YvsComDocVentes) dao.save1(doc);

            if (doc.getStatutLivre().equals("L")) {
                YvsComDocVentes bl = new YvsComDocVentes();
                bl.setTypeDoc(Constantes.TYPE_BLV);
                bl.setDocumentLie(doc);
                bl.setAuthor(author);
                if (Util.asString(adresse)) {
                    bl.setAdresse(new YvsDictionnaire(Long.valueOf(adresse)));
                }
                if (Util.asString(categorie)) {
                    bl.setCategorieComptable(new YvsBaseCategorieComptable(Long.valueOf(categorie)));
                }
                bl.setEnteteDoc(entetes);
                bl.setClient(clients);
                dao.loadInfos(author.getAgence().getSociete(), author.getAgence(), author, null, null, null);
                bl.setNumDoc(dao.genererReference(Constantes.TYPE_BLV_NAME, entetes.getDateEntete(), Long.valueOf(depot), entetes.getAgence().getSociete(), entetes.getAgence()));
                if (asString(depot)) {
                    YvsBaseDepots depots = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{Long.valueOf(depot)});
                    bl.setDepotLivrer(depots);
                }
                bl.setDateSave(new Date());
                bl.setDateUpdate(new Date());
                bl.setHeureDoc(new Date());
                bl.setTypeDoc(Constantes.TYPE_BLV);
                bl.setStatutLivre(statutLivre);
                bl.setStatutRegle(statutRegle);
                bl.setStatut(Constantes.ETAT_VALIDE);
                if (clients.getSuiviComptable()) {
                    bl.setTiers(clients);
                } else if (commercial != null ? commercial.getTiers() != null ? (commercial.getTiers().getClients() != null ? !commercial.getTiers().getClients().isEmpty() : false) : false : false) {
                    bl.setTiers(commercial.getTiers().getClients().get(0));
                }
                bl.setNomClient(nom_client);
                bl.setOperateur(author.getUsers());
                dao.save(bl);
            }

            YvsComCommercialVente comCommercialVente = new YvsComCommercialVente();
            comCommercialVente.setCommercial(commercial);
            comCommercialVente.setDateSave(new Date());
            comCommercialVente.setDateUpdate(new Date());
            comCommercialVente.setResponsable(Boolean.TRUE);
            comCommercialVente.setTaux(100.0);
            comCommercialVente.setFacture(doc);
            comCommercialVente.setAuthor(author);

            dao.save1(comCommercialVente);

            ValidationFacture(doc.getId().toString());

            System.out.println("document enregistrer = " + doc.getId());
            return doc;

        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("savePieceCaisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsComptaCaissePieceVente savePieceCaisse(@HeaderParam("docVente") String docVente, @HeaderParam("montant") String montant, @HeaderParam("mode") String mode, @HeaderParam("caisse") String caisse) {
        try {
            YvsComDocVentes vente = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{Long.valueOf(docVente)});

            YvsComEnteteDocVente ente = (YvsComEnteteDocVente) dao.loadOneByNameQueries("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{vente.getEnteteDoc().getId()});
            YvsComptaCaissePieceVente piece = new YvsComptaCaissePieceVente();
            if (Util.asString(montant) ? Double.valueOf(montant) > 0 : false) {
                piece.setMontant(Double.valueOf(montant));
                piece.setCaissier(ente.getAuthor().getUsers());
                piece.setDatePaiement(ente.getDateEntete());
                piece.setDatePaimentPrevu(ente.getDateEntete());
                piece.setDatePiece(ente.getDateEntete());
                piece.setMouvement(Constantes.MOUV_CAISS_ENTREE.charAt(0));
                if (Util.asString(caisse)) {
                    piece.setCaisse(new YvsBaseCaisse(Long.valueOf(caisse)));
                }
                piece.setDateUpdate(new Date());
                if (Util.asString(mode)) {
                    piece.setModel(new YvsBaseModeReglement(Long.valueOf(mode)));
                }

                piece.setNumeroPiece(dao.genererReference(Constantes.TYPE_PC_VENTE_NAME, ente.getDateEntete(), Long.valueOf(caisse), ente.getAgence().getSociete(), ente.getAgence()));
                piece.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                piece.setVente(vente);

                return (YvsComptaCaissePieceVente) dao.save1(piece);

            }
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);

        }
        return null;
    }

    @POST
    @Path("ValidationFacture")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public boolean ValidationFacture(@HeaderParam("docVente") String docVente) {
        boolean ok = false;
        try {
            YvsComDocVentes doc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{Long.valueOf(docVente)});
            if (doc != null ? doc.getId() > 0 : false) {

                YvsSocietes currentSt = doc.getEnteteDoc().getAgence().getSociete();
                String[] champ = new String[]{"titre", "societe"};
                Object[] val = new Object[]{Constantes.DOCUMENT_FACTURE_VENTE, currentSt};
                List<YvsWorkflowEtapeValidation> list = dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", champ, val);

                if (list != null ? list.isEmpty() : true) {
                    doc.setStatut("V");
                    dao.update(doc);
                    ok = true;
                } else {
                    List<YvsWorkflowValidFactureVente> list2 = saveEtapesValidation(doc, list, doc.getAuthor());
                    YvsWorkflowValidFactureVente etape = list2.get(0);
                    etape.setAuthor(doc.getAuthor());
                    etape.setEtapeValid(true);
                    etape.setEtapeActive(false);
                    etape.setMotif(null);
                    etape.setDateUpdate(new Date());

                    dao.update(etape);

                    doc.setStatut(Constantes.ETAT_ENCOURS);
                    doc.setAuthor(doc.getAuthor());
                    doc.setDateUpdate(new Date());
                    doc.setEtapeValide(doc.getEtapeValide() + 1);
                    dao.update(doc);
                    ok = true;
                }
            }

        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            ok = false;
        }
        return ok;
    }

    @POST
    @Path("getPieceCaisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComptaCaissePieceVente> getPieceCaisse(@HeaderParam("docVente") String docVente) {
        try {
            return dao.loadNameQueries("YvsComptaCaissePieceVente.findByFacture", new String[]{"facture"}, new Object[]{new YvsComDocVentes(Long.valueOf(docVente))});

        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("saveContenu")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsComContenuDocVente saveContenu(@HeaderParam("docVente") String docVente, @HeaderParam("article") String article, @HeaderParam("commission") String commission, @HeaderParam("condi") String condi, @HeaderParam("prix") String prix, @HeaderParam("prix_total") String prix_total, @HeaderParam("remise") String remise, @HeaderParam("ristourne") String ristourne, @HeaderParam("qte") String qte) {
        try {
            YvsComContenuDocVente contenu = new YvsComContenuDocVente();

            YvsComDocVentes doc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{Long.valueOf(docVente)});
            if (doc != null ? doc.getId() != null : false) {
                if (Util.asString(article)) {
                    contenu.setArticle(new YvsBaseArticles(Long.valueOf(article)));
                }
                contenu.setAuthor(doc.getAuthor());

                contenu.setComission(Double.valueOf(commission));

                if (Util.asString(condi)) {
                    contenu.setConditionnement(new YvsBaseConditionnement(Long.valueOf(condi)));
                }
                contenu.setDateContenu(doc.getEnteteDoc().getDateEntete());
                contenu.setDateSave(doc.getDateSave());
                contenu.setDateUpdate(new Date());

                if (doc.getEnteteDoc().getCreneau().getCreneauDepot() != null) {
                    contenu.setDepoLivraisonPrevu(doc.getDepotLivrer());
                }
                contenu.setDocVente(doc);

                contenu.setPrix(Double.valueOf(prix));
                contenu.setPrixTotal(Double.valueOf(prix_total));
                contenu.setRemise(Double.valueOf(remise));
                contenu.setRistourne(Double.valueOf(ristourne));
                contenu.setQuantite(Double.valueOf(qte));

                List<YvsComTaxeContenuVente> taxes = buildTaxes(Long.valueOf(article), Double.valueOf(qte), Double.valueOf(prix), Double.valueOf(remise), doc.getCategorieComptable().getId());
                contenu = (YvsComContenuDocVente) dao.save1(contenu);

                if (contenu.getDocVente().getStatutLivre().equals("L")) {
                    YvsComDocVentes bl = contenu.getDocVente().getDocumentLie();
                    YvsComContenuDocVente contenu2 = new YvsComContenuDocVente();
                    if (Util.asString(article)) {
                        contenu2.setArticle(new YvsBaseArticles(Long.valueOf(article)));
                    }
                    contenu2.setAuthor(doc.getAuthor());
                    contenu2.setComission(Double.valueOf(commission));

                    if (Util.asString(condi)) {
                        contenu2.setConditionnement(new YvsBaseConditionnement(Long.valueOf(condi)));
                    }
                    contenu2.setDateContenu(doc.getEnteteDoc().getDateEntete());
                    contenu2.setDateSave(doc.getDateSave());
                    contenu2.setDateUpdate(new Date());

                    if (doc.getEnteteDoc().getCreneau().getCreneauDepot() != null) {
                        contenu2.setDepoLivraisonPrevu(doc.getDepotLivrer());
                    }
                    YvsComDocVentes bon = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findByParent", new String[]{"documentLie"}, new Object[]{doc});
                    contenu2.setDocVente(bon);
                    contenu2.setPrix(Double.valueOf(prix));
                    contenu2.setPrixTotal(Double.valueOf(prix_total));
                    contenu2.setRemise(Double.valueOf(remise));
                    contenu2.setRistourne(Double.valueOf(ristourne));

                    contenu2.setQuantite(Double.valueOf(qte));
                    dao.save(contenu2);
                }

                for (YvsComTaxeContenuVente t : taxes) {
                    t.setContenu(contenu);
                    t.setDateSave(contenu.getDateSave());
                    t.setDateUpdate(new Date());
                    dao.save(t);
                }
                return contenu;
            } else {
                return null;
            }

        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("valideDocTransfert")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public ResultatAction valideDocTransfert(@HeaderParam("idDoc") String idDoc, @HeaderParam("idUser") String idUserA) {
        try {
            Long id = Long.valueOf(idDoc);
            Long idU = Long.valueOf(idUserA);
            if (id > 0) {
                YvsComDocStocks doc = (YvsComDocStocks) dao.loadOneByNameQueries("YvsComDocStocks.findById", new String[]{"id"}, new Object[]{id});
                YvsUsersAgence user = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findById", new String[]{"id"}, new Object[]{idU});
                //Récupère le niveau du user dans la société
                YvsNiveauAcces n = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauUsers.findNiveauByUser", new String[]{"user", "societe"}, new Object[]{user.getUsers(), user.getAgence().getSociete()});
                ServiceTransfert servic = new ServiceTransfert(dao, n, user);
                ResultatAction re = servic.valideTransfert(doc);
                if (re.isResult()) {
                    re.setMessage(Constantes.ETAT_VALIDE);
                }
                return re;
            } else {
                return new ResultatAction().emptyDoc();
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return new ResultatAction(false, 9999, ex.getMessage(), Constantes.MOD_COM, Constantes.TRANSFERT);
        }
    }

    @POST
    @Path("valideLineTransfert")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public ResultatAction valideLineTransfert(@HeaderParam("idLine") String idLine, @HeaderParam("idUser") String idUserA) {
        try {
            Long id = Long.valueOf(idLine);
            Long idU = Long.valueOf(idUserA);
            if (id > 0) {
                YvsComContenuDocStock doc = (YvsComContenuDocStock) dao.loadOneByNameQueries("YvsComContenuDocStock.findById", new String[]{"id"}, new Object[]{id});
                YvsUsersAgence user = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findById", new String[]{"id"}, new Object[]{idU});
                //Récupère le niveau du user dans la société
                YvsNiveauAcces n = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauUsers.findNiveauByUser", new String[]{"user", "societe"}, new Object[]{user.getUsers(), user.getAgence().getSociete()});
                ServiceTransfert servic = new ServiceTransfert(dao, n, user);
                ResultatAction re = servic.changeStatutLine(doc, doc.getDateReception(), doc.getDocStock().getStatut(), false);
                if (re.isResult()) {
                    re.setMessage(Constantes.ETAT_VALIDE);
                }
                return re;
            } else {
                return new ResultatAction().emptyDoc();
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return new ResultatAction(false, 9999, ex.getMessage(), Constantes.MOD_COM, Constantes.TRANSFERT);
        }
    }

    @POST
    @Path("returnCodeBarre")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseArticleCodeBarre> returnCodeBarre(@HeaderParam("societe") String societe
    ) {
        try {
            if (asString(societe) ? Long.valueOf(societe) > 0 : false) {
                return dao.loadNameQueries("YvsBaseArticleCodeBarre.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("getFactureByClient")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComDocVentes> getFactureByClient(@HeaderParam("client") String client, @HeaderParam("societe") String societe, @HeaderParam("numero") String numero, @HeaderParam("date") String date, @HeaderParam("statut") String statut) {
        return getFactureByClientWithLimit(client, societe, numero, date, statut, "0");
    }

    @GET
    @Path("getFactureByClientWithLimit")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComDocVentes> getFactureByClientWithLimit(@HeaderParam("client") String client, @HeaderParam("societe") String societe, @HeaderParam("numero") String numero, @HeaderParam("dates") String date, @HeaderParam("statut") String statut, @HeaderParam("limit") String limit) {
        List<YvsComDocVentes> list = new ArrayList<>();
        System.err.println(" Client :" + client + " Societe :" + societe + " Numéro :" + numero + " Statut :" + statut + "Date :" + date);
        if (Util.asString(societe)) {
            try {
                PaginatorResult<YvsComDocVentes> paginator = new PaginatorResult<>();
                paginator.addParam(new ParametreRequete("y.enteteDoc.agence.societe", "societe", new YvsSocietes(Long.valueOf(societe)), "=", "AND"));
                if (Util.asString(client)) {
                    ParametreRequete p0 = new ParametreRequete(null, "client", "XXX", "LIKE", "AND");
                    if (asNumeric(client)) {
                        p0.getOtherExpression().add(new ParametreRequete("y.client.id", "id", Long.valueOf(client), "=", "OR"));
                    }
                    p0.getOtherExpression().add(new ParametreRequete("UPPER(y.client.codeClient)", "code", client.trim().toUpperCase() + "%", "LIKE", "OR"));
                    p0.getOtherExpression().add(new ParametreRequete("UPPER(y.client.nom)", "nom", client.trim().toUpperCase() + "%", "LIKE", "OR"));
                    p0.getOtherExpression().add(new ParametreRequete("UPPER(y.nomClient)", "nomC", client.trim().toUpperCase() + "%", "LIKE", "OR"));
                    paginator.addParam(p0);
                }
                if (Util.asString(numero)) {
                    paginator.addParam(new ParametreRequete("UPPER(y.numDoc)", "numDoc", numero.trim().toUpperCase() + "%", "LIKE", "AND"));
                }
                if (Util.asString(date)) {
                    paginator.addParam(new ParametreRequete("y.enteteDoc.dateEntete", "date", df.parse(date), "=", "AND"));
                }

                if (Util.asString(statut)) {
                    paginator.addParam(new ParametreRequete("UPPER(y.statut)", "statut", statut.trim().toUpperCase(), "=", "AND"));
                }
                paginator.addParam(new ParametreRequete("UPPER(y.typeDoc)", "typeDoc", "FV", "=", "AND"));
                list = paginator.executeDynamicQuery("YvsComDocVentes", "y.enteteDoc.dateEntete, y.numDoc", true, true, asNumeric(limit) ? Integer.valueOf(limit) : 0, dao);
                for (YvsComDocVentes d : list) {
                    rebuild(d);
                    dao.setMontantTotalDoc(d, d.getContenus(), Long.valueOf(societe));
                }
            } catch (NumberFormatException | ParseException e) {
                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return list;
    }

    
    @POST
    @Path("getContenuFacture")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComContenuDocVente> getContenuFacture(@HeaderParam("docVente") String docVente
    ) {
        try {
            List<YvsComContenuDocVente> list = dao.loadNameQueries("YvsComContenuDocVente.findByFacture", new String[]{"docVente"}, new Object[]{new YvsComDocVentes(Long.valueOf(docVente))});
            for (YvsComContenuDocVente d : list) {
                rebuild(d);
            }
            return list;
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);

        }
        return null;
    }

    @POST
    @Path("getComptaCaissePieceVente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComptaCaissePieceVente> getComptaCaissePieceVente(@HeaderParam("docVente") String docVente
    ) {
        try {
            List<YvsComptaCaissePieceVente> result = dao.loadNameQueries("YvsComptaCaissePieceVente.findByFacture", new String[]{"facture"}, new Object[]{new YvsComDocVentes(Long.valueOf(docVente))});
            for (YvsComptaCaissePieceVente r : result) {
                rebuild(r);
            }
            return result;
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);

        }
        return null;
    }

    public List<YvsWorkflowValidFactureVente> saveEtapesValidation(YvsComDocVentes m, List<YvsWorkflowEtapeValidation> model, YvsUsersAgence currentUser) {
        //charge les étape de vailidation
        List<YvsWorkflowValidFactureVente> re = new ArrayList<>();
        if (!model.isEmpty()) {
            YvsWorkflowValidFactureVente vm;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    champ = new String[]{"facture", "etape"};
                    val = new Object[]{m, et};
                    YvsWorkflowValidFactureVente w = (YvsWorkflowValidFactureVente) dao.loadOneByNameQueries("YvsWorkflowValidFactureVente.findByEtapeFacture", champ, val);
                    if (w != null ? w.getId() < 1 : true) {
                        vm = new YvsWorkflowValidFactureVente();
                        vm.setAuthor(currentUser);
                        vm.setEtape(et);
                        vm.setEtapeValid(false);
                        vm.setFactureVente(m);
                        vm.setOrdreEtape(et.getOrdreEtape());
                        vm.setId(null);
                        vm = (YvsWorkflowValidFactureVente) dao.save1(vm);
                        re.add(vm);
                    }
                }
            }
        }
        return ordonneEtapes(re);
    }

    private List<YvsWorkflowValidFactureVente> ordonneEtapes(List<YvsWorkflowValidFactureVente> l) {
        return YvsWorkflowValidFactureVente.ordonneEtapes(l);
    }

    @POST
    @Path("getMensualite")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComMensualiteFactureVente> getMensualite(@HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsComMensualiteFactureVente.findByDay", new String[]{"societe", "dateMensualite"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), new Date()});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("returnPointVenteUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBasePointVente> returnPointVenteUser(@HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsBasePointVente.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
        } catch (NumberFormatException e) {
            System.out.println("erreur = " + e.getMessage());
        }
        return null;
    }

    @POST
    @Path("returnCreneau")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComCreneauPoint> returnCreneau(@HeaderParam("point") String point) {
        try {
            return dao.loadNameQueries("YvsComCreneauPoint.findByDepot", new String[]{"point"}, new Object[]{new YvsBasePointVente(Long.valueOf(point))});
        } catch (NumberFormatException e) {
            System.out.println("erreur = " + e.getMessage());
        }
        return null;
    }

    @POST
    @Path("returnPlaning")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComCreneauHoraireUsers> returnPlaning(@HeaderParam("user") String user) {
        try {

            return dao.loadNameQueries("YvsComCreneauHoraireUsers.findByActif", new String[]{"users", "actif"}, new Object[]{new YvsUsers(Long.valueOf(user)), Boolean.TRUE});
        } catch (NumberFormatException e) {
            System.out.println("erreur = " + e.getMessage());
        }
        return null;
    }

    @POST
    @Path("returnListFacture")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComDocVentes> returnListFacture(@HeaderParam("niveau") String niveau, @HeaderParam("date") String date) {
        try {
            currentNiveau = (YvsNiveauUsers) dao.loadOneByNameQueries("YvsNiveauUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(niveau)});
            if (currentNiveau != null ? currentNiveau.getId() < 1 : true) {
                return new ArrayList<>();
            }
            PaginatorResult<YvsComDocVentes> paginator = new PaginatorResult<>();
            initDroit(paginator);
            if (asString(date)) {
                try {
                    paginator.addParam(new ParametreRequete("y.enteteDoc.dateEntete", "dateEntete", df.parse(date), "=", "AND"));
                } catch (ParseException ex) {
                    Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String query = "YvsComDocVentes y "
                    + "LEFT JOIN FETCH y.categorieComptable "
                    + "LEFT JOIN FETCH y.adresse "
                    + "LEFT JOIN FETCH y.depotLivrer "
                    + "LEFT JOIN FETCH y.trancheLivrer "
                    + "JOIN FETCH y.client "
                    + "JOIN FETCH y.enteteDoc "
                    + "JOIN FETCH y.author "
                    + "JOIN FETCH y.author.users "
                    + "JOIN FETCH y.enteteDoc.creneau  "
                    + "JOIN FETCH y.enteteDoc.creneau.users  "
                    + "JOIN FETCH y.enteteDoc.creneau.creneauPoint "
                    + "JOIN FETCH y.enteteDoc.creneau.creneauPoint.point ";
            return paginator.executeDynamicQuery("y", "y", query, "y.enteteDoc.dateEntete DESC, y.id, y.heureDoc DESC, y.numDoc DESC", true, true, 0, dao);
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @POST
    @Path("returnOneFacture")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComDocVentes> returnListFacture(@HeaderParam("id") String id) {
        try {

            PaginatorResult<YvsComDocVentes> paginator = new PaginatorResult<>();
            initDroit(paginator);
            if (asString(id)) {
                try {
                    paginator.addParam(new ParametreRequete("y.id", "id", Long.getLong(id), "=", "AND"));
                } catch (Exception ex) {
                    Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String query = "YvsComDocVentes y "
                    + "LEFT JOIN FETCH y.categorieComptable "
                    + "LEFT JOIN FETCH y.adresse "
                    + "LEFT JOIN FETCH y.depotLivrer "
                    + "LEFT JOIN FETCH y.trancheLivrer "
                    + "JOIN FETCH y.client "
                    + "JOIN FETCH y.enteteDoc "
                    + "JOIN FETCH y.author "
                    + "JOIN FETCH y.author.users "
                    + "JOIN FETCH y.enteteDoc.creneau  "
                    + "JOIN FETCH y.enteteDoc.creneau.users  "
                    + "JOIN FETCH y.enteteDoc.creneau.creneauPoint "
                    + "JOIN FETCH y.enteteDoc.creneau.creneauPoint.point ";
            return paginator.executeDynamicQuery("y", "y", query, "y.enteteDoc.dateEntete DESC, y.id, y.heureDoc DESC, y.numDoc DESC", true, true, 0, dao);
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @POST
    @Path("get_infos_mouvement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<String> getInfosMouvement(@HeaderParam("id_source") String id_source, @HeaderParam("table_source") String table_source) {
        List<String> result = new ArrayList<>();
        try {
            Options[] params = new Options[]{new Options(Long.valueOf(id_source), 1), new Options(table_source, 2)};
            String query = "SELECT com_et_mouvement_stock_num(?,?)";
            Object num = dao.loadObjectBySqlQuery(query, params);
            result.add(num != null ? num.toString() : "");
            query = "SELECT com_et_mouvement_stock_desc(?,?)";
            Object desc = dao.loadObjectBySqlQuery(query, params);
            result.add(desc != null ? desc.toString() : "");
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @POST
    @Path("return_mouvement_stock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseMouvementStock> returnMouvementStock(@HeaderParam("depot") String depot, @HeaderParam("article") String article, @HeaderParam("unite") String unite, @HeaderParam("date_debut") String date_debut, @HeaderParam("date_fin") String date_fin) {
        try {
            if (!asString(depot) || !asString(article) || !asString(date_debut) || !asString(date_fin)) {
                return new ArrayList<>();
            }
            PaginatorResult<YvsBaseMouvementStock> paginator = new PaginatorResult<>();
            paginator.addParam(new ParametreRequete("y.dateDoc", "dateDoc", df.parse(date_debut), df.parse(date_fin), "BETWEEN", "AND"));
            paginator.addParam(new ParametreRequete("y.depot", "depot", new YvsBaseDepots(Long.valueOf(depot)), "=", "AND"));
            paginator.addParam(new ParametreRequete("y.article", "article", new YvsBaseArticles(Long.valueOf(article)), "=", "AND"));
            if (asNumeric(unite) ? Long.valueOf(unite) > 0 : false) {
                paginator.addParam(new ParametreRequete("y.conditionnement", "unite", new YvsBaseConditionnement(Long.valueOf(unite)), "=", "AND"));
            }

            System.err.println("depot = " + depot);
            System.err.println("article = " + article);
            System.err.println("unite = " + unite);
            System.err.println("date_debut = " + date_debut);
            System.err.println("date_fin = " + date_fin);
            return paginator.executeDynamicQuery("y", "y", "YvsBaseMouvementStock y JOIN FETCH y.article JOIN FETCH y.conditionnement JOIN FETCH y.depot", "y.dateDoc, y.id", true, true, 0, dao);
        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @POST
    @Path("return_reception_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComDocAchats> returnReceptionAchat(@HeaderParam("facture") String facture) {
        try {
            List<YvsComDocAchats> list = dao.loadNameQueries("YvsComDocAchats.findByParentStatut", new String[]{"documentLie", "statut"}, new Object[]{new YvsComDocAchats(Long.valueOf(facture)), Constantes.ETAT_VALIDE});
            for (YvsComDocAchats d : list) {
                rebuild(d);
            }
            return list;
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @POST
    @Path("return_livraison_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComDocVentes> returnLivraisonVente(@HeaderParam("facture") String facture) {
        try {
            List<YvsComDocVentes> list = dao.loadNameQueries("YvsComDocVentes.findByParentStatut", new String[]{"documentLie", "statut"}, new Object[]{new YvsComDocVentes(Long.valueOf(facture)), Constantes.ETAT_VALIDE});
            for (YvsComDocVentes d : list) {
                rebuild(d);
            }
            return list;
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

}
