/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws.param;

import java.util.List;
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
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsModuleActive;
import yvs.entity.param.YvsSocietes;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsModule;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsNiveauUsers;
import yvs.entity.users.YvsUsers;
import yvs.parametrage.Souscrire;
import yvs.util.Constantes;
import yvs.util.MdpUtil;

/**
 *
 * @author hp Elite 8300
 */
@Path("services/parametrage")
@RequestScoped
public class GenericResource extends lymytz.ws.GenericResource {

    private MdpUtil hashMdp = new MdpUtil();

    @GET
    @Path("get_module_actif")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsModuleActive> getModuleActif(@HeaderParam("societe") String societe) {
        try {
            if (asNumeric(societe)) {
                return dao.loadNameQueries("YvsModuleActive.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("create_customer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultatAction<Souscrire> createCustomer(Souscrire entry) {
        try {
            if (entry != null && dao != null) {
                if (!asString(entry.getNomSociete())) {
                    return new ResultatAction<>(false, null, 0L, "Vous devez indiquer le nom de la socièté");
                }
                YvsBaseTiers tiers = (YvsBaseTiers) dao.loadOneByNameQueries("YvsBaseTiers.findByNom", new String[]{"nom"}, new Object[]{entry.getNomSociete()});
                if (tiers != null ? tiers.getId() < 1 : true) {
                    tiers = new YvsBaseTiers();
                    tiers.setActif(true);
                    tiers.setClient(true);
                    tiers.setNom(entry.getNomSociete());
                    tiers.setStSociete(true);
                    tiers.setEmail(entry.getEmail());
                    tiers.setTel(entry.getTelephone());
                    YvsDictionnaire ville = (YvsDictionnaire) dao.loadOneByNameQueries("YvsDictionnaire.findLikelibele", new String[]{"libelle"}, new Object[]{entry.getVille()});
                    if (ville != null ? ville.getId() < 1 : true) {
                        YvsDictionnaire pays = (YvsDictionnaire) dao.loadOneByNameQueries("YvsDictionnaire.findLikelibele", new String[]{"libelle"}, new Object[]{entry.getPays()});
                        if (pays != null ? pays.getId() < 1 : true) {
                            pays = new YvsDictionnaire();
                            pays.setLibele(entry.getPays());
                            pays.setAbreviation(entry.getPays().length() > 3 ? entry.getPays().substring(0, 3) : entry.getPays());
                            pays.setTitre(Constantes.PAYS);
                            pays.setActif(true);
                            pays = (YvsDictionnaire) dao.save1(pays);
                        }
                        ville = new YvsDictionnaire();
                        ville.setLibele(entry.getVille());
                        ville.setAbreviation(entry.getVille().length() > 3 ? entry.getVille().substring(0, 3) : entry.getVille());
                        ville.setTitre(Constantes.VILLES);
                        ville.setActif(true);
                        ville.setParent(pays);
                        ville = (YvsDictionnaire) dao.save1(ville);
                    }
                    tiers.setVille(ville);
                    tiers.setPays(ville.getParent());
                    tiers.setCodeTiers(ville.getAbreviation() + "_" + (entry.getNomSociete().length() > 3 ? entry.getNomSociete().substring(0, 3) : entry.getNomSociete()));
                    tiers.setSociete(new YvsSocietes(entry.getLocalSociete()));
                    tiers.setNew_(true);
                    tiers = (YvsBaseTiers) dao.save1(tiers);
                }
                if (tiers != null ? tiers.getId() > 0 : false) {
                    YvsComClient client = null;
                    if (!tiers.isNew_()) {
                        client = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findByTiers", new String[]{"tiers"}, new Object[]{tiers});
                    }
                    if (client != null ? client.getId() < 1 : true) {
                        client = new YvsComClient();
                        client.setActif(true);
                        client.setTiers(tiers);
                        client.setCodeClient(tiers.getCodeTiers());
                        client.setNom(entry.getNomSociete());
                        client = (YvsComClient) dao.save1(client);
                    }
                    if (client != null ? client.getId() > 0 : false) {
                        entry.setId(client.getId());
                        return new ResultatAction<>(true, entry, entry.getId(), "Succès", entry);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>().fail("Echec!!!");
    }

    @POST
    @Path("souscrire")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultatAction<Souscrire> souscrire(Souscrire entry) {
        try {
            if (entry != null && dao != null) {
                if (!asString(entry.getNomSociete())) {
                    return new ResultatAction<>(false, null, 0L, "Vous devez indiquer le nom de la socièté");
                }
                if (!asString(entry.getNomUsers())) {
                    return new ResultatAction<>(false, null, 0L, "Vous devez indiquer le nom de l'utilisateur");
                }
                if (!asString(entry.getIdentifiant())) {
                    return new ResultatAction<>(false, null, 0L, "Vous devez indiquer l'identifian");
                }
                if (!asString(entry.getPassword())) {
                    return new ResultatAction<>(false, null, 0L, "Vous devez indiquer le mot de passe");
                }
                YvsSocietes societe = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findByName", new String[]{"name"}, new Object[]{entry.getNomSociete()});
                if (societe != null ? societe.getId() < 1 : true) {
                    societe = new YvsSocietes(null, entry.getNomSociete());
                    societe = (YvsSocietes) dao.save1(societe);
                    if (societe != null ? societe.getId() < 1 : true) {
                        return new ResultatAction<>(false, null, 0L, "Impossible de générer la socièté!!!");
                    }
                }
                YvsAgences agence = (YvsAgences) dao.loadOneByNameQueries("YvsAgences.findByCodeagence", new String[]{"codeagence", "societe"}, new Object[]{"SIEGE", societe});
                if (agence != null ? agence.getId() < 1 : true) {
                    agence = new YvsAgences(null, "SIEGE", "SIEGE");
                    agence.setSociete(societe);
                    agence = (YvsAgences) dao.save1(agence);
                    if (agence != null ? agence.getId() < 1 : true) {
                        return new ResultatAction<>(false, null, 0L, "Impossible de générer l'agence!!!");
                    }
                }
                YvsNiveauAcces niveau = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauAcces.findLikeDesignation", new String[]{"societe", "designation"}, new Object[]{societe, "ADMINISTRATEUR"});
                if (niveau != null ? niveau.getId() < 1 : true) {
                    niveau = new YvsNiveauAcces(null, "ADMINISTRATEUR");
                    niveau.setSociete(societe);
                    niveau.setSuperAdmin(true);
                    niveau = (YvsNiveauAcces) dao.save1(niveau);
                    if (niveau != null ? niveau.getId() < 1 : true) {
                        return new ResultatAction<>(false, null, 0L, "Impossible de générer le niveau d'accès");
                    }
                }
                YvsUsers users = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findByCode", new String[]{"codeUsers"}, new Object[]{societe, entry.getIdentifiant()});
                if (users != null ? users.getId() < 1 : true) {
                    users = new YvsUsers(null, entry.getIdentifiant(), entry.getNomUsers());
                    users.setAgence(agence);
                    users.setPasswordUser(entry.getPassword());
                    users.setSuperAdmin(true);
                    users.setAleaMdp(hashMdp.randomString(15));
                    users.setPasswordUser(hashMdp.hashString(users.getCodeUsers() + "" + users.getAleaMdp() + "" + users.getPasswordUser()));
                    users.setNiveauAcces(niveau);
                    users = (YvsUsers) dao.save1(users);
                    if (users != null ? users.getId() < 1 : true) {
                        return new ResultatAction<>(false, null, 0L, "Impossible de générer l'utilisateur");
                    }
                } else {
                    if (!users.getAgence().getSociete().equals(societe)) {
                        return new ResultatAction<>(false, null, 0L, "Identifiant indisponible");
                    }
                }
                YvsNiveauUsers acces = (YvsNiveauUsers) dao.loadOneByNameQueries("YvsNiveauUsers.findNivoUser", new String[]{"user", "niveau"}, new Object[]{users, niveau});
                if (acces != null ? acces.getId() < 1 : true) {
                    acces = new YvsNiveauUsers(null);
                    acces.setIdNiveau(niveau);
                    acces.setIdUser(users);
                    acces = (YvsNiveauUsers) dao.save1(acces);
                    if (acces != null ? acces.getId() < 1 : true) {
                        return new ResultatAction<>(false, null, 0L, "Impossible de générer l'accès de l'utilisateur");
                    }
                }
                if (entry.getModules() != null) {
                    String query = "SELECT m.id, m.libelle, m.description, a.id, COALESCE(a.actif, FALSE) as actif FROM yvs_module m LEFT JOIN yvs_module_active a ON m.id = a.module AND a.societe = ? WHERE m.reference = ? ";
                    List<Object[]> result = null;
                    for (String module : entry.getModules()) {
                        result = dao.loadListBySqlQuery(query, new Options[]{new Options(societe.getId(), 1), new Options(module, 2)});
                        if (result != null ? !result.isEmpty() : false) {
                            YvsModule m;
                            YvsModuleActive a;
                            for (Object[] data : result) {
                                m = new YvsModule((Integer) data[0]);
                                m.setLibelle((String) data[1]);
                                m.setDescription((String) data[2]);
                                a = new YvsModuleActive(data[3] != null ? (Integer) data[3] : YvsModuleActive.ids--);
                                a.setActif((Boolean) data[4]);
                                a.setModule(m);
                                a.setSociete(societe);

                                if (a.getId() > 0) {
                                    if (!a.getActif()) {
                                        a.setActif(true);
                                        dao.update(a);
                                    }
                                } else {
                                    a.setActif(true);
                                    a.setId(null);
                                    dao.save1(a);
                                }

                                String insert = "INSERT INTO yvs_autorisation_module(module, niveau_acces, acces, date_save) "
                                        + "SELECT m.id, ?, true, current_timestamp FROM yvs_module m LEFT JOIN yvs_autorisation_module a ON (a.module = m.id AND a.niveau_acces = ?) "
                                        + "WHERE a.id IS NULL AND m.id = ?";
                                dao.requeteLibre(insert, new Options[]{new Options(niveau.getId(), 1), new Options(niveau.getId(), 2), new Options(m.getId(), 3)});

                                insert = "INSERT INTO yvs_autorisation_page_module(page_module, niveau_acces, acces, date_save) "
                                        + "SELECT p.id, ?, true, current_timestamp FROM yvs_page_module p INNER JOIN yvs_module m ON p.module = m.id LEFT JOIN yvs_autorisation_page_module a ON (a.page_module = p.id AND a.niveau_acces = ?) "
                                        + "WHERE a.id IS NULL AND m.id = ?";
                                dao.requeteLibre(insert, new Options[]{new Options(niveau.getId(), 1), new Options(niveau.getId(), 2), new Options(m.getId(), 3)});

                                insert = "INSERT INTO yvs_autorisation_ressources_page(ressource_page, niveau_acces, acces, date_save) "
                                        + "SELECT r.id, ?, true, current_timestamp FROM yvs_ressources_page r INNER JOIN yvs_page_module p ON r.page_module = p.id INNER JOIN yvs_module m ON p.module = m.id LEFT JOIN yvs_autorisation_ressources_page a ON (a.ressource_page = r.id AND a.niveau_acces = ?) "
                                        + "WHERE a.id IS NULL AND m.id = ?";
                                dao.requeteLibre(insert, new Options[]{new Options(niveau.getId(), 1), new Options(niveau.getId(), 2), new Options(m.getId(), 3)});
                            }
                        }
                    }
                }
                entry.setId(societe.getId());
                return new ResultatAction<>(true, entry, entry.getId(), "Succès", entry);
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>().fail("Echec!!!");
    }
}
