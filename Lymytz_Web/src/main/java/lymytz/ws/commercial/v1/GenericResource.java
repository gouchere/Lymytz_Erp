/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws.commercial.v1;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.codec.binary.Base64;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.YvsComParametreVente;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComTaxeContenuAchat;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;
import yvs.entity.commercial.creneau.YvsComCreneauPoint;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComCoutSupDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComDocVentesInformations;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.commercial.vente.YvsComTaxeContenuVente;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.workflow.YvsWorkflowValidDocStock;
import yvs.entity.param.workflow.YvsWorkflowValidFactureAchat;
import yvs.entity.param.workflow.YvsWorkflowValidFactureVente;
import yvs.entity.users.YvsUsersAgence;
import yvs.service.IEntitySax;
import yvs.service.com.achat.IYvsComContenuDocAchat;
import yvs.service.com.achat.IYvsComDocAchats;
import yvs.service.com.achat.IYvsComTaxeContenuAchat;
import yvs.service.com.achat.IYvsComptaCaissePieceAchat;
import yvs.service.com.param.IYvsBasePointVente;
import yvs.service.com.param.IYvsComCreneauHoraireUsers;
import yvs.service.com.stocks.IYvsComContenuDocStock;
import yvs.service.com.stocks.IYvsComDocStocks;
import yvs.service.com.vente.IYvsComContenuDocVente;
import yvs.service.com.vente.IYvsComCoutSupDocVente;
import yvs.service.com.vente.IYvsComDocVentes;
import yvs.service.com.vente.IYvsComDocVentesInformations;
import yvs.service.com.vente.IYvsComEnteteDocVente;
import yvs.service.com.vente.IYvsComTaxeContenuVente;
import yvs.service.com.vente.IYvsComptaCaissePieceVente;
import yvs.service.param.workflow.IYvsWorkflowValidDocStock;
import yvs.service.param.workflow.IYvsWorkflowValidFactureAchat;
import yvs.service.param.workflow.IYvsWorkflowValidFactureVente;
import yvs.util.enume.Nombre;

/**
 *
 * @author Lymytz-pc
 */
@Path("services/commercial/v1")
@RequestScoped
public class GenericResource extends lymytz.ws.commercial.GenericResource {

    IEntitySax IEntitiSax = new IEntitySax();

    /**
     * BEGIN CONTENU VENTE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_contenu_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComContenuDocVente> saveContenuVente(YvsComContenuDocVente entity) {
        try {
            IYvsComContenuDocVente impl = (IYvsComContenuDocVente) IEntitiSax.createInstance("IYvsComContenuDocVente", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_contenu_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComContenuDocVente> updateContenu(YvsComContenuDocVente entity) {
        try {
            IYvsComContenuDocVente impl = (IYvsComContenuDocVente) IEntitiSax.createInstance("IYvsComContenuDocVente", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_contenu_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComContenuDocVente> deleteContenuVente(YvsComContenuDocVente entity) {
        try {
            IYvsComContenuDocVente impl = (IYvsComContenuDocVente) IEntitiSax.createInstance("IYvsComContenuDocVente", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END CONTENU VENTE**/
    /**
     * BEGIN COU VENTE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_cout_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComCoutSupDocVente> saveCoutVente(YvsComCoutSupDocVente entity) {
        try {
            IYvsComCoutSupDocVente impl = (IYvsComCoutSupDocVente) IEntitiSax.createInstance("IYvsComCoutSupDocVente", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_cout_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComCoutSupDocVente> updateCoutVente(YvsComCoutSupDocVente entity) {
        try {
            IYvsComCoutSupDocVente impl = (IYvsComCoutSupDocVente) IEntitiSax.createInstance("IYvsComCoutSupDocVente", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_cout_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComCoutSupDocVente> deleteCoutVente(YvsComCoutSupDocVente entity) {
        try {
            IYvsComCoutSupDocVente impl = (IYvsComCoutSupDocVente) IEntitiSax.createInstance("IYvsComCoutSupDocVente", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END CONTENU VENTE**/
    /**
     * BEGIN ENTETE VENTE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_entete_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComEnteteDocVente> saveEnteteVente(YvsComEnteteDocVente entity) {
        try {
            IYvsComEnteteDocVente impl = (IYvsComEnteteDocVente) IEntitiSax.createInstance("IYvsComEnteteDocVente", dao);
            if (impl != null && entity != null) {
                impl.setAgence(entity.getAgence());
                entity.setCreneau((YvsComCreneauHoraireUsers) dao.loadOneByNameQueries("YvsComCreneauHoraireUsers.findById", new String[]{"id"}, new Object[]{entity.getCreneau().getId()}));
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_entete_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComEnteteDocVente> updateEnteteVente(YvsComEnteteDocVente entity) {
        try {
            IYvsComEnteteDocVente impl = (IYvsComEnteteDocVente) IEntitiSax.createInstance("IYvsComEnteteDocVente", dao);
            if (impl != null && entity != null) {
                entity.setCreneau((YvsComCreneauHoraireUsers) dao.loadOneByNameQueries("YvsComCreneauHoraireUsers.findById", new String[]{"id"}, new Object[]{entity.getCreneau().getId()}));
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_entete_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComEnteteDocVente> deleteEnteteVente(YvsComEnteteDocVente entity) {
        try {
            IYvsComEnteteDocVente impl = (IYvsComEnteteDocVente) IEntitiSax.createInstance("IYvsComEnteteDocVente", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    /*END ENTETE VENTE**/

    /**
     * BEGIN DOC VENTE
     *
     * @param id
     * @param entity
     * @return
     */
    @POST
    @Path("one_doc_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsComDocVentes oneDocVente(@HeaderParam("id") String id) {
        try {
            if (asNumeric(id)) {
                YvsComDocVentes y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{Long.valueOf(id)});
                if (y != null ? y.getId() > 0 : false) {
                    rebuild(y);
                    dao.setMontantTotalDoc(y, y.getContenus(), y.getEnteteDoc().getAgence().getSociete().getId());
                }
                return y;
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("list_doc_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComDocVentes> listDocVente(@HeaderParam("client") String client) {
        try {
            if (asNumeric(client)) {
                return dao.loadNameQueries("YvsComDocVentes.findByClient", new String[]{"client"}, new Object[]{new YvsComClient(Long.valueOf(client))});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("print_facture_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String printVente(@HeaderParam("facture") String facture, @HeaderParam("auteur") String auteur) {
        try {
            if (asString(facture)) {
                YvsComDocVentes y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{Long.valueOf(facture)});
                if (y != null ? y.getId() > 0 : false) {
                    YvsComParametreVente currentParamVente = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{y.getEnteteDoc().getAgence()});
                    Double ca = dao.loadCaVente(y.getId());
                    Double tx = dao.loadTaxeVente(y.getId());
                    Map<String, Object> param = new HashMap<>();
                    String path = resource.getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                    param.put("ID", y.getId().intValue());
                    param.put("IMG_PAYE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getStatutRegle().equals(Constantes.ETAT_REGLE) ? "solde.png" : "empty.png"));
                    param.put("IMG_LIVRE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getStatutLivre().equals(Constantes.ETAT_LIVRE) ? "livre.png" : "empty.png"));
                    param.put("MONTANT", Nombre.CALCULATE.getValue(ca));
                    String report = currentParamVente != null ? currentParamVente.getModelFactureVente() : "facture_vente";
                    param.put("AUTEUR", auteur);
                    param.put("TAXE", tx != null ? tx > 0 : false);
                    param.put("LOGO", returnLogo(y.getEnteteDoc().getAgence().getSociete()));
                    String IMG_QRC = createQRCode(y.getNumDoc());
                    if (asString(IMG_QRC)) {
                        param.put("IMG_QRC", IMG_QRC);
                    }
                    param.put("SUBREPORT_DIR", SUBREPORT_DIR(true));
                    byte[] bytes = executeReport(report, param, "pdf");
                    if (bytes != null) {
                        return new String(Base64.encodeBase64(bytes));
                    }
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("save_doc_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocVentes> saveDocVente(YvsComDocVentes entity) {
        try {
            if (entity != null) {
                String statut = entity.getStatut();
                String statutL = entity.getStatutLivre();
                String statutR = entity.getStatutRegle();
                IYvsComDocVentes impl = (IYvsComDocVentes) IEntitiSax.createInstance("IYvsComDocVentes", dao);
                if (impl != null) {
                    impl.setAgence(entity.getEnteteDoc().getAgence());
                    Long id_distant = entity.getEnteteDoc().getIdDistant();
                    List<YvsComCoutSupDocVente> couts = new ArrayList<>(entity.getCouts());
                    List<YvsComContenuDocVente> contenus = new ArrayList<>(entity.getContenus());
                    List<YvsComptaCaissePieceVente> reglements = new ArrayList<>(entity.getReglements());
                    if (entity.getEnteteDoc().getCreneau() == null) {
                        return new ResultatAction<>(false, entity, 0L, "Enregistrement du journal impossible car le créneau n'existe pas!!!");
                    }
                    YvsComCreneauHoraireUsers creneau;
                    if (entity.getEnteteDoc().getCreneau() != null ? entity.getEnteteDoc().getCreneau().getId() < 1 : true) {
                        champ = new String[]{"users", "point", "date"};
                        val = new Object[]{entity.getEnteteDoc().getCreneau().getUsers(), entity.getEnteteDoc().getCreneau().getCreneauPoint().getPoint(), entity.getEnteteDoc().getDateEntete()};
                        creneau = (YvsComCreneauHoraireUsers) dao.loadOneByNameQueries("YvsComCreneauHoraireUsers.findOneCompletCreno2", champ, val);
                    } else {
                        creneau = entity.getEnteteDoc().getCreneau();
                    }
                    if (creneau != null ? creneau.getId() > 0 ? creneau.getPermanent() : false : false) {
                        creneau.setDateTravail(entity.getEnteteDoc().getDateEntete());
                    }
                    if (creneau != null ? creneau.getId() < 1 : true) {
                        return new ResultatAction<>(false, entity, 0L, "Enregistrement du journal impossible car le créneau n'existe pas!!!");
                    }
                    //retrouve ou enregistre l' header de la facture s'il n'existe pas
                    YvsComEnteteDocVente entete = (YvsComEnteteDocVente) dao.loadOneByNameQueries("YvsComEnteteDocVente.findByCrenauDate", new String[]{"date", "creanau"}, new Object[]{entity.getEnteteDoc().getDateEntete(), creneau.getId()});
                    if (entete == null) {
                        entete = entity.getEnteteDoc();
                        entete.setCreneau(creneau);
                        entete.setAdresseServeur(entity.getAdresseServeur());
                        entete.setAgence(entity.getEnteteDoc().getAgence());
                        ResultatAction result = saveEnteteVente(entete);
                        if (result != null ? !result.isResult() : true) {
                            return new ResultatAction<>(false, entity, 0L, "Enregistrement du journal impossible!!!");
                        }
                        entete = (YvsComEnteteDocVente) result.getData();
                    }
                    //enregistre l'auteur si'il n'existe pas
                    if (entete.getAuthor() != null ? entete.getAuthor().getId() < 1 : true) {
                        YvsUsersAgence author = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findByUsersAgence", new String[]{"user", "agence"}, new Object[]{creneau.getUsers(), entete.getAgence()});
                        if (author != null ? author.getId() < 1 : true) {
                            author = new YvsUsersAgence(creneau.getUsers(), entete.getAgence());
                            author = (YvsUsersAgence) dao.save1(author);
                        }
                        entete.setAuthor(author);
                    }
                    if (entity.getAuthor() != null ? entity.getAuthor().getId() < 1 : true) {
                        entity.setAuthor(entete.getAuthor());
                    }
                    entete.setIdDistant(id_distant);

                    entity.setEnteteDoc(entete);
                    entity.setStatut(Constantes.ETAT_EDITABLE);
                    entity.setStatutLivre(Constantes.ETAT_ATTENTE);
                    entity.setStatutRegle(Constantes.ETAT_ATTENTE);
                    //Enregistre la facture et son contenu
                    ResultatAction<YvsComDocVentes> result = impl.save(entity);
                    if (result != null ? result.isResult() : false) {
                        entity.setId(result.getIdEntity());
                        entity.setEnteteDoc(entete);
                        if (!contenus.isEmpty()) {
                            List<YvsComContenuDocVente> list = new ArrayList<>();
                            for (YvsComContenuDocVente c : contenus) {
                                c.setAdresseServeur(entity.getAdresseServeur());
                                c.setDocVente(new YvsComDocVentes(entity.getId()));
                                ResultatAction r = saveContenuVente(c);
                                if (r != null ? r.isResult() : false) {
                                    list.add(c);
                                }
                            }
                            entity.setContenus(list);
                        }
                        //si la facture contient des règlements, on les enregistre aussi
                        if (!reglements.isEmpty() && entity.getTypeDoc().equals(Constantes.TYPE_FV)) {
                            List<YvsComptaCaissePieceVente> list = new ArrayList<>();
                            for (YvsComptaCaissePieceVente p : reglements) {
                                p.setAdresseServeur(entity.getAdresseServeur());
                                p.setVente(new YvsComDocVentes(entity.getId()));
                                ResultatAction r = saveCaissePieceVente(p);
                                if (r != null ? r.isResult() : false) {
                                    list.add(p);
                                }
                            }
                            entity.setReglements(list);
                        }
                        //si la facture contient des coût sup., on les enregistre aussi
                        if (!couts.isEmpty()) {
                            List<YvsComCoutSupDocVente> list = new ArrayList<>();
                            for (YvsComCoutSupDocVente c : couts) {
                                c.setAdresseServeur(entity.getAdresseServeur());
                                c.setDocVente(new YvsComDocVentes(entity.getId()));
                                ResultatAction r = saveCoutVente(c);
                                if (r != null ? r.isResult() : false) {
                                    list.add(c);
                                }
                            }
                            entity.setCouts(list);
                        }

                        YvsUsersAgence author = entity.getAuthor();
                        YvsAgences agence = entity.getEnteteDoc().getAgence();
                        YvsSocietes societe = null;
                        if (agence != null ? agence.getId() > 0 : false) {
                            societe = agence.getSociete();
                        }
                        creneau = entete.getCreneau();
                        YvsComCreneauDepot creneauDepot = null;
                        if (creneau != null ? creneau.getId() > 0 : false) {
                            creneauDepot = creneau.getCreneauDepot();
                        }
                        YvsBaseDepots depot = null;
                        if (creneauDepot != null ? creneauDepot.getId() > 0 : false) {
                            depot = creneauDepot.getDepot();
                        }
                        YvsComCreneauPoint creneauPoint = null;
                        if (creneau != null ? creneau.getId() > 0 : false) {
                            creneauPoint = creneau.getCreneauPoint();
                        }
                        YvsBasePointVente point = null;
                        if (creneauPoint != null ? creneauPoint.getId() > 0 : false) {
                            point = creneauPoint.getPoint();
                        }

                        if (statut.equals(Constantes.ETAT_VALIDE) || statut.equals(Constantes.ETAT_ENCOURS)) {
                            impl.validerFacture(entity, author, societe, agence, depot, point);
                        }
                        if ((statutL.equals(Constantes.ETAT_LIVRE) || statutL.equals(Constantes.ETAT_ENCOURS)) && entity.getTypeDoc().equals(Constantes.TYPE_FV)) {
                            List<YvsComDocVentes> list = new ArrayList<>(entity.getDocuments());
                            impl.livraison(list, entity, author, societe, agence, depot, point);
                        }
                        if ((statutR.equals(Constantes.ETAT_REGLE) || statutR.equals(Constantes.ETAT_ENCOURS)) && entity.getTypeDoc().equals(Constantes.TYPE_FV)) {
                            impl.reglement(entity, author, societe, depot, point, agence);
                        }
                        Map<String, String> statuts = dao.getEquilibreVente(entity.getId());
                        if (statuts != null) {
                            entity.setStatutLivre(statuts.get("statut_livre"));
                            entity.setStatutRegle(statuts.get("statut_regle"));
                            if (result.getData() != null ? result.getData() instanceof YvsComDocVentes : false) {
                                ((YvsComDocVentes) result.getData()).setStatutLivre(entity.getStatutLivre());
                                ((YvsComDocVentes) result.getData()).setStatutRegle(entity.getStatutRegle());
                            }
                        }
                    }
                    result = rebuild(result);
                    result.setEntity(null);
                    return result;
                }
            }
            return new ResultatAction<>(false, entity, 0L, "La facture que vous souhaité synchroniser est nulle");
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_doc_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocVentes> updateDocVente(YvsComDocVentes entity) {
        String statut = entity.getStatut();
        Long idDistant = entity.getIdDistant();
        List<YvsComDocVentes> list = new ArrayList<>();
        try {
            IYvsComDocVentes impl = (IYvsComDocVentes) IEntitiSax.createInstance("IYvsComDocVentes", dao);
            if (impl != null) {
                entity.setEnteteDoc((YvsComEnteteDocVente) dao.loadOneByNameQueries("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{entity.getEnteteDoc().getId()}));
                ResultatAction result = rebuild(impl.update(entity));
                if (entity.getId() > 0) {
                    entity = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{entity.getId()});
                    if (entity != null ? statut.equals(Constantes.ETAT_VALIDE) : false) {
                        if (entity.getStatutLivre().equals(Constantes.ETAT_ATTENTE) && entity.getLivraisonAuto()) {
                            impl.livraison(list, entity, entity.getAuthor(), entity.getDepotLivrer().getAgence().getSociete(), entity.getDepotLivrer().getAgence(), entity.getDepotLivrer(), entity.getEnteteDoc().getCreneau().getCreneauPoint().getPoint());
                            entity.setIdDistant(idDistant);
                        }
                        if (entity.getTypeDoc().equals(Constantes.TYPE_FV)) {
                            comptabiliserVente(entity);
                        }
                        result.setData(entity);
                    } else if (entity != null ? !statut.equals(Constantes.ETAT_VALIDE) : false) {
                        if (entity.getTypeDoc().equals(Constantes.TYPE_FV)) {
                            unComptabiliserVente(entity);
                        }
                        result.setData(entity);
                    }
                }
                if (result != null ? result.getData() != null ? result.getData() instanceof YvsComDocVentes : false : false) {
                    ((YvsComDocVentes) result.getData()).setCouts(null);
                    ((YvsComDocVentes) result.getData()).setDocuments(null);
                    ((YvsComDocVentes) result.getData()).setContenus(null);
                    ((YvsComDocVentes) result.getData()).setReglements(null);
                    ((YvsComDocVentes) result.getData()).setEtapesValidations(null);
                    ((YvsComDocVentes) result.getData()).setMensualites(null);
                    ((YvsComDocVentes) result.getData()).setRemises(null);
                    ((YvsComDocVentes) result.getData()).setRistournes(null);
                    ((YvsComDocVentes) result.getData()).setCommissions(null);
                }
                result = rebuild(result);
                return result;
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_doc_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocVentes> deleteDocVente(YvsComDocVentes entity) {
        try {
            IYvsComDocVentes impl = (IYvsComDocVentes) IEntitiSax.createInstance("IYvsComDocVentes", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("save_doc_vente_information")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocVentesInformations> saveDocVenteInformation(YvsComDocVentesInformations entity) {
        try {
            IYvsComDocVentesInformations impl = (IYvsComDocVentesInformations) IEntitiSax.createInstance("IYvsComDocVentesInformations", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_doc_vente_information")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocVentesInformations> updateDocVenteInformation(YvsComDocVentesInformations entity) {
        try {
            IYvsComDocVentesInformations impl = (IYvsComDocVentesInformations) IEntitiSax.createInstance("IYvsComDocVentesInformations", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_doc_vente_information")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocVentesInformations> deleteDocVenteInformation(YvsComDocVentesInformations entity) {
        try {
            IYvsComDocVentesInformations impl = (IYvsComDocVentesInformations) IEntitiSax.createInstance("IYvsComDocVentesInformations", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END DOC VENTE**/
    /**
     * BEGIN PIECE CAISSE VENTE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_caisse_piece_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaCaissePieceVente> saveCaissePieceVente(YvsComptaCaissePieceVente entity) {
        try {
            IYvsComptaCaissePieceVente impl = (IYvsComptaCaissePieceVente) IEntitiSax.createInstance("IYvsComptaCaissePieceVente", dao);
            if (impl != null) {
                dao.setMontantTotalDoc(entity.getVente(), 0);
                if (entity.getVente().getMontantResteAPlanifier() < entity.getMontant()) {
                    String message = (entity.getVente().getMontantResteAPlanifier() > 0 ? "Le montant du reglement est supérieur au reste à planifé sur la facture " + entity.getVente().getNumDoc() : "La facture " + entity.getVente().getNumDoc() + " est déja réglée à sa totalité");
                    ResultatAction result = new ResultatAction<>(false, entity, 0L, message);
                    result.setContinu(false);
                    return result;
                }
                ResultatAction result = rebuild(impl.save(entity));
                if (entity.getId() > 0) {
                    entity = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{entity.getId()});
                    if (entity.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                        comptabiliserCaisseVente(entity);
                    }
                }
                return result;
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_caisse_piece_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaCaissePieceVente> updateCaissePieceVente(YvsComptaCaissePieceVente entity) {
        try {
            IYvsComptaCaissePieceVente impl = (IYvsComptaCaissePieceVente) IEntitiSax.createInstance("IYvsComptaCaissePieceVente", dao);
            if (impl != null) {
                ResultatAction result = rebuild(impl.update(entity));
                if (entity.getId() > 0) {
                    entity = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{entity.getId()});
                    if (entity.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                        comptabiliserCaisseVente(entity);
                    } else {
                        unComptabiliserCaisseVente(entity);
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
    @Path("delete_caisse_piece_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaCaissePieceVente> deleteCaissePieceVente(YvsComptaCaissePieceVente entity) {
        try {
            IYvsComptaCaissePieceVente impl = (IYvsComptaCaissePieceVente) IEntitiSax.createInstance("IYvsComptaCaissePieceVente", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END PIECE CAISSE VENTE**/
    /**
     *
     * BEGIN TAXES
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_taxe_contenu")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComTaxeContenuVente> saveTaxeContenu(YvsComTaxeContenuVente entity) {
        try {
            IYvsComTaxeContenuVente impl = (IYvsComTaxeContenuVente) IEntitiSax.createInstance("IYvsComTaxeContenuVente", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_taxe_contenu")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComTaxeContenuVente> updateTaxeContenu(YvsComTaxeContenuVente entity) {
        try {
            IYvsComTaxeContenuVente impl = (IYvsComTaxeContenuVente) IEntitiSax.createInstance("IYvsComTaxeContenuVente", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_taxe_contenu")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComTaxeContenuVente> deleteTaxeContenu(YvsComTaxeContenuVente entity) {
        try {
            IYvsComTaxeContenuVente impl = (IYvsComTaxeContenuVente) IEntitiSax.createInstance("IYvsComTaxeContenuVente", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    /*END TAXES**/

    /**
     * BEGIN CONTENU ACHAT
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_contenu_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComContenuDocAchat> saveContenuAchat(YvsComContenuDocAchat entity) {
        try {
            IYvsComContenuDocAchat impl = (IYvsComContenuDocAchat) IEntitiSax.createInstance("IYvsComContenuDocAchat", dao);
            if (impl != null) {
                System.err.println(" save contenu achat ");
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_contenu_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComContenuDocAchat> updateContenuAchat(YvsComContenuDocAchat entity) {
        try {
            IYvsComContenuDocAchat impl = (IYvsComContenuDocAchat) IEntitiSax.createInstance("IYvsComContenuDocAchat", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_contenu_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComContenuDocAchat> deleteContenuAchat(YvsComContenuDocAchat entity) {
        try {
            IYvsComContenuDocAchat impl = (IYvsComContenuDocAchat) IEntitiSax.createInstance("IYvsComContenuDocAchat", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END CONTENU ACHAT**/
    /**
     * BEGIN DOC ACHAT
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_doc_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocAchats> saveDocAchat(YvsComDocAchats entity) {
        try {
            if (entity != null) {
                String statut = entity.getStatut();
                String statutL = entity.getStatutLivre();
                String statutR = entity.getStatutRegle();
                IYvsComDocAchats impl = (IYvsComDocAchats) IEntitiSax.createInstance("IYvsComDocAchats", dao);
                if (impl != null) {
                    entity.setStatut(Constantes.ETAT_EDITABLE);
                    entity.setStatutLivre(Constantes.ETAT_ATTENTE);
                    entity.setStatutRegle(Constantes.ETAT_ATTENTE);
                    List<YvsComContenuDocAchat> contenus = new ArrayList<>(entity.getContenus());
                    List<YvsComptaCaissePieceAchat> reglements = new ArrayList<>(entity.getReglements());

                    ResultatAction<YvsComDocAchats> result = impl.save(entity);
                    if (result != null ? result.isResult() : false) {
                        for (YvsComContenuDocAchat c : contenus) {
                            c.setDocAchat(new YvsComDocAchats(entity.getId()));
                            saveContenuAchat(c);
                        }
                        for (YvsComptaCaissePieceAchat p : reglements) {
                            p.setAchat(new YvsComDocAchats(entity.getId()));
                            saveCaissePieceAchat(p);
                        }
                        if (statut.equals(Constantes.ETAT_VALIDE) || statut.equals(Constantes.ETAT_ENCOURS)) {
//                            impl.validerOrder(entity, entity.getEnteteDoc().getAgence(), entity.getAuthor(), entity.getEnteteDoc().getAgence().getSociete(), entity.getEnteteDoc().getCreneau().getCreneauDepot().getDepot(), entete.getCreneau().getCreneauPoint().getPoint());
                        }
                        if (statutL.equals(Constantes.ETAT_LIVRE) || statutL.equals(Constantes.ETAT_ENCOURS)) {
                            List<YvsComDocAchats> list = new ArrayList<>();
                            list.add(entity);
//                            impl.livraison(list, entity, entity.getAuthor(), entity.getEnteteDoc().getAgence().getSociete(), entity.getEnteteDoc().getAgence(), entete.getCreneau().getCreneauDepot().getDepot(), entete.getCreneau().getCreneauPoint().getPoint());
                        }
                        if (statutR.equals(Constantes.ETAT_REGLE) || statutR.equals(Constantes.ETAT_ENCOURS)) {
//                            impl.reglement(entity, entity.getAuthor(), entete.getAgence().getSociete(), entete.getCreneau().getCreneauDepot().getDepot(), entete.getCreneau().getCreneauPoint().getPoint(), entete.getAgence());
                        }
                        entity.setContenus(contenus);
                        entity.setReglements(reglements);
                    }
                    if (reglements != null ? !reglements.isEmpty() : false) {
                        entity.setStatutRegle(statutR);
                        dao.update(entity);
                    }
                    result = rebuild(result);
                    return result;
                }
            }
            return new ResultatAction<>(false, rebuild(entity), 0L, "La facture que vous souhaité synchroniser est nulle");
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_doc_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocAchats> updateDocAchat(YvsComDocAchats entity) {
        try {
            IYvsComDocAchats impl = (IYvsComDocAchats) IEntitiSax.createInstance("IYvsComDocAchats", dao);
            if (impl != null) {
                ResultatAction result = rebuild(impl.update(entity));
                if (result != null ? result.getData() != null ? result.getData() instanceof YvsComDocAchats : false : false) {
                    ((YvsComDocAchats) result.getData()).setCouts(null);
                    ((YvsComDocAchats) result.getData()).setDocuments(null);
                    ((YvsComDocAchats) result.getData()).setContenus(null);
                    ((YvsComDocAchats) result.getData()).setReglements(null);
                    ((YvsComDocAchats) result.getData()).setEtapesValidations(null);
                    ((YvsComDocAchats) result.getData()).setMensualites(null);
                }
                return rebuild(result);
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_doc_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocAchats> deleteDocAchat(YvsComDocAchats entity) {
        try {
            IYvsComDocAchats impl = (IYvsComDocAchats) IEntitiSax.createInstance("IYvsComDocAchats", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END DOC ACHAT**/
    /**
     * BEGIN PIECE CAISSE ACHAT
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_caisse_piece_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaCaissePieceAchat> saveCaissePieceAchat(YvsComptaCaissePieceAchat entity) {
        try {
            IYvsComptaCaissePieceAchat impl = (IYvsComptaCaissePieceAchat) IEntitiSax.createInstance("IYvsComptaCaissePieceAchat", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_caisse_piece_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaCaissePieceAchat> updateCaissePieceAchat(YvsComptaCaissePieceAchat entity) {
        try {
            IYvsComptaCaissePieceAchat impl = (IYvsComptaCaissePieceAchat) IEntitiSax.createInstance("IYvsComptaCaissePieceAchat", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_caisse_piece_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaCaissePieceAchat> deleteCaissePieceVente(YvsComptaCaissePieceAchat entity) {
        try {
            IYvsComptaCaissePieceAchat impl = (IYvsComptaCaissePieceAchat) IEntitiSax.createInstance("IYvsComptaCaissePieceAchat", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END PIECE CAISSE ACHAT**/
    /**
     *
     * BEGIN TAXES
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_taxe_contenu_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComTaxeContenuAchat> saveTaxeContenuAchat(YvsComTaxeContenuAchat entity) {
        try {
            IYvsComTaxeContenuAchat impl = (IYvsComTaxeContenuAchat) IEntitiSax.createInstance("IYvsComTaxeContenuAchat", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_taxe_contenu_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComTaxeContenuAchat> updateTaxeContenuAchat(YvsComTaxeContenuAchat entity) {
        try {
            IYvsComTaxeContenuAchat impl = (IYvsComTaxeContenuAchat) IEntitiSax.createInstance("IYvsComTaxeContenuAchat", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_taxe_contenu_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComTaxeContenuAchat> deleteTaxeContenuAchat(YvsComTaxeContenuAchat entity) {
        try {
            IYvsComTaxeContenuAchat impl = (IYvsComTaxeContenuAchat) IEntitiSax.createInstance("IYvsComTaxeContenuAchat", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    /*END TAXES**/

    @POST
    @Path("save_etape_doc_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsWorkflowValidFactureVente> saveEtapeDocVente(YvsWorkflowValidFactureVente entity) {
        try {
            IYvsWorkflowValidFactureVente impl = (IYvsWorkflowValidFactureVente) IEntitiSax.createInstance("IYvsWorkflowValidFactureVente", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_etape_doc_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public ResultatAction<YvsWorkflowValidFactureVente> updateEtapeDocVente(YvsWorkflowValidFactureVente entity) {
        try {
            IYvsWorkflowValidFactureVente impl = (IYvsWorkflowValidFactureVente) IEntitiSax.createInstance("IYvsWorkflowValidFactureVente", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_etape_doc_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsWorkflowValidFactureVente> deleteEtapeDocVente(YvsWorkflowValidFactureVente entity) {
        try {
            IYvsWorkflowValidFactureVente impl = (IYvsWorkflowValidFactureVente) IEntitiSax.createInstance("IYvsWorkflowValidFactureVente", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("valide_etape_facture_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsWorkflowValidFactureVente> valideEtapeFactureVente(YvsWorkflowValidFactureVente entity) {
        try {
            IYvsComDocVentes impl = (IYvsComDocVentes) IEntitiSax.createInstance("IYvsComDocVentes", dao);
            if (impl != null) {
                return impl.valideEtape(entity);
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("save_etape_doc_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsWorkflowValidFactureAchat> saveEtapeDocAchat(YvsWorkflowValidFactureAchat entity) {
        try {
            IYvsWorkflowValidFactureAchat impl = (IYvsWorkflowValidFactureAchat) IEntitiSax.createInstance("IYvsWorkflowValidFactureAchat", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_etape_doc_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public ResultatAction<YvsWorkflowValidFactureAchat> updateEtapeDocAchat(YvsWorkflowValidFactureAchat entity) {
        try {
            IYvsWorkflowValidFactureAchat impl = (IYvsWorkflowValidFactureAchat) IEntitiSax.createInstance("IYvsWorkflowValidFactureAchat", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_etape_doc_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsWorkflowValidFactureAchat> deleteEtapeDocAchat(YvsWorkflowValidFactureAchat entity) {
        try {
            IYvsWorkflowValidFactureAchat impl = (IYvsWorkflowValidFactureAchat) IEntitiSax.createInstance("IYvsWorkflowValidFactureAchat", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("valide_etape_facture_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsWorkflowValidFactureAchat> valideEtapeFactureAchat(YvsWorkflowValidFactureAchat entity) {
        try {
            IYvsComDocAchats impl = (IYvsComDocAchats) IEntitiSax.createInstance("IYvsComDocAchats", dao);
            if (impl != null) {
                return impl.valideEtape(entity);
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("save_etape_doc_stock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsWorkflowValidDocStock> saveEtapeDocStock(YvsWorkflowValidDocStock entity) {
        try {
            IYvsWorkflowValidDocStock impl = (IYvsWorkflowValidDocStock) IEntitiSax.createInstance("IYvsWorkflowValidDocStock", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_etape_doc_stock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public ResultatAction<YvsWorkflowValidDocStock> updateEtapeDocStock(YvsWorkflowValidDocStock entity) {
        try {
            IYvsWorkflowValidDocStock impl = (IYvsWorkflowValidDocStock) IEntitiSax.createInstance("IYvsWorkflowValidDocStock", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_etape_doc_stock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsWorkflowValidDocStock> deleteEtapeDocStock(YvsWorkflowValidDocStock entity) {
        try {
            IYvsWorkflowValidDocStock impl = (IYvsWorkflowValidDocStock) IEntitiSax.createInstance("IYvsWorkflowValidDocStock", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("valide_etape_doc_stock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsWorkflowValidDocStock> valideEtapeDocStock(YvsWorkflowValidDocStock entity) {
        try {
            IYvsComDocStocks impl = (IYvsComDocStocks) IEntitiSax.createInstance("IYvsComDocStocks", dao);
            if (impl != null) {
                return impl.valideEtape(entity);
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("return_quantite_livre")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComContenuDocVente> returnQuantiteLivre(YvsComContenuDocVente contenu) {
        ResultatAction<YvsComContenuDocVente> result = new ResultatAction(false, null, 0L, "Action Impossible");
        try {
            IYvsComContenuDocVente impl = (IYvsComContenuDocVente) IEntitiSax.createInstance("IYvsComContenuDocVente", dao);
            if (impl != null) {
                result = impl.returnQuantiteLivre(contenu);
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rebuild(result);
    }

    @POST
    @Path("return_quantite_recu")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComContenuDocAchat> returnQuantiteRecu(YvsComContenuDocAchat contenu) {
        System.out.println("quantite reçu");
        ResultatAction<YvsComContenuDocAchat> result = new ResultatAction(false, null, 0L, "Action Impossible");
        try {
            IYvsComContenuDocAchat impl = (IYvsComContenuDocAchat) IEntitiSax.createInstance("IYvsComContenuDocAchat", dao);
            if (impl != null) {
                result = impl.returnQuantiteLivre(contenu);
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rebuild(result);
    }

    @POST
    @Path("livrer_bon_livraison")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //Entity est le bon de livraison
    public ResultatAction<YvsComDocVentes> livrerBonLivraison(YvsComDocVentes entity) {
        ResultatAction<YvsComDocVentes> result = new ResultatAction(false, null, 0L, "Action Impossible");
        try {
            IYvsComDocVentes impl = (IYvsComDocVentes) IEntitiSax.createInstance("IYvsComDocVentes", dao);
            if (impl != null) {
                result = impl.livraison(entity);
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rebuild(result);
    }

    @POST
    @Path("annuler_livraison_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //Entity est le bon de livraison
    public ResultatAction<YvsComDocVentes> annulerLivraisonVente(YvsComDocVentes entity) {
        ResultatAction<YvsComDocVentes> result = new ResultatAction(false, null, 0L, "Action Impossible");
        try {
            IYvsComDocVentes impl = (IYvsComDocVentes) IEntitiSax.createInstance("IYvsComDocVentes", dao);
            if (impl != null) {
                return impl.annulerOrder(entity);
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rebuild(result);
    }

    @POST
    @Path("livrer_bon_reception")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocAchats> livrerBonReception(YvsComDocAchats entity) {
        ResultatAction<YvsComDocAchats> result = new ResultatAction(false, null, 0L, "Action Impossible");
        try {
            IYvsComDocAchats impl = (IYvsComDocAchats) IEntitiSax.createInstance("IYvsComDocAchats", dao);
            if (impl != null) {
                result = impl.reception(entity);
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rebuild(result);
    }

    @POST
    @Path("annuler_reception_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocAchats> annulerReceptionAchat(YvsComDocAchats entity) {
        ResultatAction<YvsComDocAchats> result = new ResultatAction(false, null, 0L, "Action Impossible");
        try {
            IYvsComDocAchats impl = (IYvsComDocAchats) IEntitiSax.createInstance("IYvsComDocAchats", dao);
            if (impl != null) {
                result = impl.annulerReception(entity, entity.getNiveau());
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rebuild(result);
    }

    @POST
    @Path("livrer_facture_vente_caisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocVentes> livrerFactureVenteFromAppCaisse(YvsComDocVentes entity) {
        try {
            IYvsComDocVentes impl = (IYvsComDocVentes) IEntitiSax.createInstance("IYvsComDocVentes", dao);
            if (impl != null) {
                if (entity != null ? entity.getId() != null : false) {
                    entity = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{entity.getId()});
                    return impl.livraison(new ArrayList(), entity, entity.getAuthor(), entity.getEnteteDoc().getAgence().getSociete(), entity.getEnteteDoc().getAgence(), entity.getEnteteDoc().getCreneau().getCreneauDepot().getDepot(), entity.getEnteteDoc().getCreneau().getCreneauPoint().getPoint());
                } else {
                    return null;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("livrer_facture_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocVentes> livrerFactureVente(YvsComDocVentes entity) {
        try {
            IYvsComDocVentes impl = (IYvsComDocVentes) IEntitiSax.createInstance("IYvsComDocVentes", dao);
            if (impl != null) {
                List<YvsComContenuDocVente> contenus = new ArrayList<>(entity.getContenus());
                Date dateLivraisonPrevu = entity.getDateLivraisonPrevu();
                YvsBaseDepots depot = entity.getDepotLivrer() != null ? new YvsBaseDepots(entity.getDepotLivrer().getId()) : null;
                YvsGrhTrancheHoraire tranche = entity.getTrancheLivrer() != null ? new YvsGrhTrancheHoraire(entity.getTrancheLivrer().getId()) : null;
                entity = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{entity.getId()});
                if (entity != null) {
                    if (!entity.isReload()) {
                        entity.setContenus(contenus);
                    }
                    entity.setDateLivraisonPrevu(dateLivraisonPrevu);
                    entity.setDepotLivrer(depot);
                    entity.setTrancheLivrer(tranche);
                    YvsAgences agence = new YvsAgences();
                    YvsUsersAgence author = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findById", new String[]{"id"}, new Object[]{entity.getAuthor().getId()});
                    if (entity.getEnteteDoc().getAgence() != null ? entity.getEnteteDoc().getAgence().getId() < 1 : true) {
                        if (author != null ? author.getId() > 0 : false) {
                            agence = (YvsAgences) dao.loadOneByNameQueries("YvsAgences.findById", new String[]{"id"}, new Object[]{author.getAgence().getId()});
                        }
                    } else {
                        agence = (YvsAgences) dao.loadOneByNameQueries("YvsAgences.findById", new String[]{"id"}, new Object[]{entity.getEnteteDoc().getAgence().getId()});
                    }
                    YvsComEnteteDocVente entete = (YvsComEnteteDocVente) dao.loadOneByNameQueries("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{entity.getEnteteDoc().getId()});
                    entity.setEnteteDoc(entete);
                    entity.setAuthor(author);
                    return impl.livraison(new ArrayList(), entity, author, agence.getSociete(), agence, entete.getCreneau().getCreneauDepot().getDepot(), entete.getCreneau().getCreneauPoint().getPoint());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("livrer_facture_achat")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocAchats> livrerFactureAchat(YvsComDocAchats entity) {
        try {
            IYvsComDocAchats impl = (IYvsComDocAchats) IEntitiSax.createInstance("IYvsComDocAchats", dao);
            if (impl != null) {
                entity = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{entity.getId()});
                List<YvsComDocVentes> ls = new ArrayList();

                YvsAgences agence = new YvsAgences();
                YvsUsersAgence author = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findById", new String[]{"id"}, new Object[]{entity.getAuthor().getId()});
                if (author != null ? author.getId() > 0 : false) {
                    agence = (YvsAgences) dao.loadOneByNameQueries("YvsAgences.findById", new String[]{"id"}, new Object[]{author.getAgence().getId()});
                }
                System.err.println("tranche livrer  = " + entity.getTranche());
                entity.setAuthor(author);
                return impl.livraison(entity);
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * BEGIN WORKFLOW VENTE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_workflow_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsWorkflowValidFactureVente> saveWorkFlowVente(YvsWorkflowValidFactureVente entity) {
        try {
            IYvsWorkflowValidFactureVente impl = (IYvsWorkflowValidFactureVente) IEntitiSax.createInstance("IYvsWorkflowValidFactureVente", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_workflow_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsWorkflowValidFactureVente> updateWorkFlowVente(YvsWorkflowValidFactureVente entity) {
        try {
            IYvsWorkflowValidFactureVente impl = (IYvsWorkflowValidFactureVente) IEntitiSax.createInstance("IYvsWorkflowValidFactureVente", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_workflow_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsWorkflowValidFactureVente> deleteWorkFlowVente(YvsWorkflowValidFactureVente entity) {
        try {
            IYvsWorkflowValidFactureVente impl = (IYvsWorkflowValidFactureVente) IEntitiSax.createInstance("IYvsWorkflowValidFactureVente", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    /*END WORKFLOW VENTE**/

    /*
     * BEGIN DOC STOCKS
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_doc_stock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocStocks> saveDocStock(YvsComDocStocks entity) {
        try {
            IYvsComDocStocks impl = (IYvsComDocStocks) IEntitiSax.createInstance("IYvsComDocStocks", dao);
            if (impl != null) {
                List<YvsComContenuDocStock> contenus = new ArrayList<>(entity.getContenus());
                entity.getContenus().clear();

                ResultatAction<YvsComDocStocks> result = impl.save(entity);
                if (result != null ? result.isResult() : false) {
                    entity.setId(result.getIdEntity());
                    for (YvsComContenuDocStock c : contenus) {
                        System.err.println(" ...Save contenu....");
                        c.setDocStock(new YvsComDocStocks(entity.getId()));
                        saveContenuDocStock(c);
                    }
                    System.err.println(" ...fin contenu....");
                    entity.setContenus(contenus);
                }
                System.err.println(" fin fonction");
                return rebuild(result);
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_doc_stock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocStocks> updateDocStock(YvsComDocStocks entity) {
        try {
            IYvsComDocStocks impl = (IYvsComDocStocks) IEntitiSax.createInstance("IYvsComDocStocks", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {

            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_doc_stock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocStocks> deleteDocStock(YvsComDocStocks entity) {
        try {
            IYvsComDocStocks impl = (IYvsComDocStocks) IEntitiSax.createInstance("IYvsComDocStocks", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END DOC STOCK**/
    /*
     * BEGIN CONTENU STOCKS
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_contenu_doc_stock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComContenuDocStock> saveContenuDocStock(YvsComContenuDocStock entity) {
        try {
            IYvsComContenuDocStock impl = (IYvsComContenuDocStock) IEntitiSax.createInstance("IYvsComContenuDocStock", dao);
            if (impl != null) {
                System.err.println("enregistrement contenu stock");
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_contenu_doc_stock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComContenuDocStock> updateContenuDocStock(YvsComContenuDocStock entity) {
        try {
            IYvsComContenuDocStock impl = (IYvsComContenuDocStock) IEntitiSax.createInstance("IYvsComContenuDocStock", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {

            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_contenu_doc_stock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComContenuDocStock> deleteContenuDocStock(YvsComContenuDocStock entity) {
        try {
            IYvsComContenuDocStock impl = (IYvsComContenuDocStock) IEntitiSax.createInstance("IYvsComContenuDocStock", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END CONTENU STOCK**/
    @POST
    @Path("getPr")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Double getPr(@HeaderParam("article_") String article_, @HeaderParam("depot_") String depot_, @HeaderParam("unite_") String unite_) {
        Long art = Long.valueOf(article_);
        Long depot = Long.valueOf(depot_);
        Long unite = Long.valueOf(unite_);

        return dao.getPr(art != null ? art : 0L, depot != null ? depot : 0, 0L, new Date(), unite != null ? unite : 0);
    }

    @POST
    @Path("save_inventaire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocStocks> saveInventaire(YvsComDocStocks entity) {
        try {
            IYvsComDocStocks impl = (IYvsComDocStocks) IEntitiSax.createInstance("IYvsComDocStocks", dao);
            if (impl != null) {
                return impl.saveInventaire(entity);
            }
        } catch (Exception e) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
    /*
     * BEGIN CRENEAU HORAIRE USERS
     *
     * @param entity
     * @return
     */

    @POST
    @Path("save_creneau_horaire_users")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComCreneauHoraireUsers> saveCreneauHoraireUsers(YvsComCreneauHoraireUsers entity) {
        try {
            IYvsComCreneauHoraireUsers impl = (IYvsComCreneauHoraireUsers) IEntitiSax.createInstance("IYvsComCreneauHoraireUsers", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_creneau_horaire_users")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComCreneauHoraireUsers> updateCreneauHoraireUsers(YvsComCreneauHoraireUsers entity) {
        try {
            IYvsComCreneauHoraireUsers impl = (IYvsComCreneauHoraireUsers) IEntitiSax.createInstance("IYvsComCreneauHoraireUsers", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {

            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_creneau_horaire_users")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComCreneauHoraireUsers> deleteCreneauHoraireUsers(YvsComCreneauHoraireUsers entity) {
        try {
            IYvsComCreneauHoraireUsers impl = (IYvsComCreneauHoraireUsers) IEntitiSax.createInstance("IYvsComCreneauHoraireUsers", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END CRENEAU HORAIRE USERS**/
    /*
     * BEGIN CRENEAU HORAIRE USERS
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_point_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBasePointVente> savePointVente(YvsBasePointVente entity) {
        try {
            IYvsBasePointVente impl = (IYvsBasePointVente) IEntitiSax.createInstance("IYvsBasePointVente", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_point_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBasePointVente> updatePointVente(YvsBasePointVente entity) {
        try {
            IYvsBasePointVente impl = (IYvsBasePointVente) IEntitiSax.createInstance("IYvsBasePointVente", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {

            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_point_vente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBasePointVente> deletePointVente(YvsBasePointVente entity) {
        try {
            IYvsBasePointVente impl = (IYvsBasePointVente) IEntitiSax.createInstance("IYvsBasePointVente", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Gestion des bons de commandes*
     */
    @POST
    @Path("valide_doc_commande")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComDocVentes> valideCommande(YvsComDocVentes entity) {
        try {
            if (entity != null) {
                String statut = entity.getStatut();
                String statutL = entity.getStatutLivre();
                String statutR = entity.getStatutRegle();
                IYvsComDocVentes impl = (IYvsComDocVentes) IEntitiSax.createInstance("IYvsComDocVentes", dao);
                if (impl != null) {
                    impl.setAgence(entity.getEnteteDoc().getAgence());
                    Long id_distant = entity.getEnteteDoc().getIdDistant();
                    entity = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{entity.getId()});
                    List<YvsComContenuDocVente> l = dao.loadNameQueries("YvsComContenuDocVente.findByFacture", new String[]{"docVente"}, new Object[]{entity});
                    return rebuild(impl.validerOrder(entity, entity.getAuthor(), l));
//                    List<YvsComCoutSupDocVente> couts = new ArrayList<>(entity.getCouts());
//                    List<YvsComContenuDocVente> contenus = new ArrayList<>(entity.getContenus());
//                    List<YvsComptaCaissePieceVente> reglements = new ArrayList<>(entity.getReglements());
//                    if (entity.getEnteteDoc().getCreneau() == null) {
//                        return new ResultatAction<>(false, entity, 0L, "Enregistrement du journal impossible car le créneau n'existe pas!!!");
//                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.base.v1.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
