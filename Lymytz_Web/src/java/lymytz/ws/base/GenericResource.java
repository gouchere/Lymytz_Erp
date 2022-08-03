/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws.base;

import java.util.ArrayList;
import java.util.Date;
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
import yvs.dao.services.bases.Autorisations;
import yvs.dao.services.bases.ServiceAutorisation;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.base.YvsBaseArticleCategorieComptableTaxe;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseArticleFournisseur;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseClassesStat;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.YvsComCategoriePersonnel;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsBasePlanTarifaire;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsBaseGroupeSociete;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsGrhSecteurs;
import yvs.entity.param.YvsSocietes;
import yvs.entity.produits.YvsBaseArticleCodeBarre;
import yvs.entity.produits.YvsBaseArticleDescription;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.entity.produits.group.YvsBaseGroupesArticle;
import yvs.entity.users.*;
import yvs.entity.users.YvsModule;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsPageModule;
import yvs.entity.users.YvsRessourcesPage;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.entity.users.YvsUsersGrade;
import yvs.util.MdpUtil;

/**
 *
 * @author hp Elite 8300
 */
@Path("services/donnees_base")
@RequestScoped
public class GenericResource extends lymytz.ws.GenericResource {

    private MdpUtil hashMdp = new MdpUtil();

    @POST
    @Path("returnGroupesNotIds")
    @Produces({MediaType.APPLICATION_JSON})
    public List<YvsBaseGroupesArticle> returnGroupesNotIds(@HeaderParam("ids") String ids) {
        try {
            List<Long> value = new ArrayList<>();
            if (asString(ids)) {
                for (String id : ids.split(",")) {
                    value.add(Long.valueOf(id));
                }
            }
            if (value.isEmpty()) {
                value.add(-1L);
            }
            return dao.loadNameQueries("YvsBaseGroupesArticle.findNotIds", new String[]{"ids"}, new Object[]{value});
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("findByConditionnement")
    @Produces(MediaType.APPLICATION_JSON)
    public List<YvsBaseConditionnement> findByConditionnement(@HeaderParam("refArt") String refArt, @HeaderParam("societe") String societe) {
        List<YvsBaseConditionnement> baseConditionnements = new ArrayList<>();
        try {
//            YvsBaseArticles art = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findByRefArt", new String[]{"refArt"}, new Object[]{refArt});
            List<YvsBaseArticles> articles = dao.loadNameQueries("YvsBaseArticles.findByCodeL", new String[]{"societe", "code"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), "%" + refArt + "%"});
            for (YvsBaseArticles art : articles) {
                baseConditionnements.addAll(dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{art}));
            }
            return baseConditionnements;

        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("findByArticle")
    @Produces(MediaType.APPLICATION_JSON)
    public List<YvsBaseArticles> findByArticle(@HeaderParam("refArt") String refArt, @HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsBaseArticles.findByCodeL", new String[]{"societe", "code"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), "%" + refArt + "%"});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("findArticleByCodeBarre")
    @Produces(MediaType.APPLICATION_JSON)
    public YvsBaseConditionnement findArticleByCodeBarre(@HeaderParam("codeBarre") String codeBarre, @HeaderParam("societe") String societe) {
        try {
            return (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseArticleCodeBarre.findArticleByCodeBarre", new String[]{"societe", "codeBarre"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), codeBarre});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("SaveCodeBarre")
    @Produces(MediaType.APPLICATION_JSON)
    public YvsBaseArticleCodeBarre SaveCodeBarre(@HeaderParam("conditionnement") String conditionnement, @HeaderParam("codebarre") String codeBarre, @HeaderParam("user") String user) {
        try {
            YvsBaseArticleCodeBarre articleCodeBarre = new YvsBaseArticleCodeBarre();
            YvsBaseConditionnement condi = new YvsBaseConditionnement(Long.valueOf(conditionnement));
            articleCodeBarre.setConditionnement(condi);
            articleCodeBarre.setCodeBarre(codeBarre);
            articleCodeBarre.setDateSave(new Date());
            articleCodeBarre.setDateUpdate(new Date());
            YvsUsersAgence author = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findByUser", new String[]{"users"}, new Object[]{new YvsUsers(Long.valueOf(user))});

            articleCodeBarre.setAuthor(author);
            return (YvsBaseArticleCodeBarre) dao.save1(articleCodeBarre);
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("findByCodeBarre")
    @Produces(MediaType.APPLICATION_JSON)
    public List<YvsBaseArticleCodeBarre> findByCodeBarre(@HeaderParam("conditionnement") String conditionnement) {
        try {
            return dao.loadNameQueries("YvsBaseArticleCodeBarre.findByConditionnement", new String[]{"conditionnement"}, new Object[]{new YvsBaseConditionnement(Long.valueOf(conditionnement))});
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("deleteCodeBarre")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean deleteCodeBarre(@HeaderParam("codeBarre") String codeBarre) {
        try {
            YvsBaseArticleCodeBarre codeBarres = (YvsBaseArticleCodeBarre) dao.loadOneByNameQueries("YvsBaseArticleCodeBarre.findById", new String[]{"id"}, new Object[]{Long.valueOf(codeBarre)});
            if (codeBarres != null) {
                dao.delete(codeBarres);
                return true;
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @POST
    @Path("SaveSociete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsSocietes SaveSociete(@HeaderParam("adress_siege") String adress_siege, @HeaderParam("capital") String capital, @HeaderParam("code") String code, @HeaderParam("code_postal") String code_postal, @HeaderParam("devise") String devise,
            @HeaderParam("email") String email, @HeaderParam("forme_juridique") String forme_juridique, @HeaderParam("name") String name,
            @HeaderParam("registre") String registre, @HeaderParam("siege") String siege, @HeaderParam("site_web") String site_web, @HeaderParam("tel") String tel, @HeaderParam("umonaie") String umonaie, @HeaderParam("ville") String ville, @HeaderParam("pays") String pays) {
        YvsSocietes societe = new YvsSocietes();
        try {

            YvsDictionnaire vil = new YvsDictionnaire(Long.valueOf(ville));
            YvsDictionnaire pay = new YvsDictionnaire(Long.valueOf(pays));

            if (vil != null ? vil.getId() > 0 : false) {
                societe.setVille(vil);
            }
            if (pay != null ? pay.getId() > 0 : false) {
                societe.setPays(pay);
            }

            societe.setAdressSiege(siege);
            societe.setCapital(Double.valueOf(capital));
            societe.setCodeAbreviation(code);
            societe.setCodePostal(code_postal);
            societe.setDevise(devise);
            societe.setEmail(email);
            societe.setFormeJuridique(forme_juridique);
            societe.setName(name);
            societe.setNumeroRegistreComerce(registre);
            societe.setSiege(siege);
            societe.setSiteWeb(site_web);
            societe.setTel(tel);
            societe.setUmonaie(umonaie);
            societe.setActif(Boolean.TRUE);
            societe.setUmonaie("false");
            societe.setDateSave(new Date());
            societe.setDateUpdate(new Date());

            return (YvsSocietes) dao.save1(societe);

        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @POST
    @Path("updateSociete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsSocietes updateSociete(@HeaderParam("id") String id, @HeaderParam("adress_siege") String adress_siege, @HeaderParam("capital") String capital, @HeaderParam("code") String code, @HeaderParam("code_postal") String code_postal, @HeaderParam("devise") String devise,
            @HeaderParam("email") String email, @HeaderParam("forme_juridique") String forme_juridique, @HeaderParam("name") String name,
            @HeaderParam("registre") String registre, @HeaderParam("siege") String siege, @HeaderParam("site_web") String site_web, @HeaderParam("tel") String tel, @HeaderParam("umonaie") String umonaie, @HeaderParam("ville") String ville, @HeaderParam("pays") String pays) {

        try {
            YvsSocietes societe = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{Long.valueOf(id)});
            YvsDictionnaire vil = (YvsDictionnaire) dao.loadOneByNameQueries("YvsDictionnaire.findById", new String[]{"id"}, new Object[]{Long.valueOf(ville)});
            YvsDictionnaire pay = (YvsDictionnaire) dao.loadOneByNameQueries("YvsDictionnaire.findById", new String[]{"id"}, new Object[]{Long.valueOf(pays)});

            if (vil != null ? vil.getId() > 0 : false) {
                societe.setVille(vil);
            }
            if (pay != null ? pay.getId() > 0 : false) {
                societe.setPays(pay);
            }

            societe.setAdressSiege(siege);
            societe.setCapital(Double.valueOf(capital));
            societe.setCodeAbreviation(code);
            societe.setCodePostal(code_postal);
            societe.setDevise(devise);
            societe.setEmail(email);
            societe.setFormeJuridique(forme_juridique);
            societe.setName(name);
            societe.setNumeroRegistreComerce(registre);
            societe.setSiege(vil.getLibele());
            societe.setSiteWeb(site_web);
            societe.setTel(tel);
            societe.setUmonaie(umonaie);
            societe.setActif(Boolean.TRUE);
            societe.setUmonaie("false");
            societe.setDateSave(new Date());
            societe.setDateUpdate(new Date());

            return (YvsSocietes) dao.update(societe);

        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @POST
    @Path("saveAgence")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsAgences saveAgence(@HeaderParam("abbreviation") String abbreviation, @HeaderParam("adresse") String adresse, @HeaderParam("codeAgence") String codeAgence,
            @HeaderParam("designation") String designation, @HeaderParam("region") String region, @HeaderParam("secteur") String secteur,
            @HeaderParam("societe") String societe, @HeaderParam("ville") String ville, @HeaderParam("email") String email,
            @HeaderParam("telephone") String telephone, @HeaderParam("code_postal") String code_postal, @HeaderParam("user") String user, @HeaderParam("agence") String agence_user) {
        YvsAgences agence = new YvsAgences();
        try {

            agence.setAbbreviation(abbreviation);
            agence.setAdresse(adresse);
            agence.setCodeagence(codeAgence);
            agence.setDesignation(designation);
            agence.setRegion(region);
            if (!secteur.equals("")) {
                int id = 0;
                id = Integer.valueOf(secteur);
                if (id > 0) {
                    agence.setSecteurActivite((YvsGrhSecteurs) dao.loadOneByNameQueries("YvsSecteurs.findById", new String[]{"id"}, new Object[]{Integer.valueOf(id)}));

                }
            }

            agence.setSociete(new YvsSocietes(Long.valueOf(societe)));
            agence.setVille(new YvsDictionnaire(Long.valueOf(ville)));
            agence.setEmail(email);
            agence.setTelephone(telephone);
            agence.setCodePostal(code_postal);
            agence.setDateSave(new Date());
            agence.setDateUpdate(new Date());
            agence.setActif(Boolean.TRUE);
            agence.setSupp(Boolean.FALSE);

            agence.setAuthor(yvsUsersAgence(new YvsUsers(Long.valueOf(user)), new YvsAgences(Long.valueOf(agence_user))));

            return (YvsAgences) dao.save1(agence);

        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }

    }

    @POST
    @Path("updateAgence")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsAgences updateAgence(@HeaderParam("agences") String agences, @HeaderParam("abbreviation") String abbreviation, @HeaderParam("adresse") String adresse, @HeaderParam("codeAgence") String codeAgence,
            @HeaderParam("designation") String designation, @HeaderParam("region") String region, @HeaderParam("secteur") String secteur,
            @HeaderParam("societe") String societe, @HeaderParam("ville") String ville, @HeaderParam("email") String email,
            @HeaderParam("telephone") String telephone, @HeaderParam("code_postal") String code_postal, @HeaderParam("user") String user, @HeaderParam("agence") String agence_user) {

        try {
            YvsAgences agence = (YvsAgences) dao.loadOneByNameQueries("YvsAgences.findById", new String[]{"id"}, new Object[]{Long.valueOf(agences)});
            agence.setAbbreviation(abbreviation);
            agence.setAdresse(adresse);
            agence.setCodeagence(codeAgence);
            agence.setDesignation(designation);
            agence.setRegion(region);
            if (!secteur.equals("")) {
                int id = 0;
                id = Integer.valueOf(secteur);
                if (id > 0) {
                    agence.setSecteurActivite((YvsGrhSecteurs) dao.loadOneByNameQueries("YvsSecteurs.findById", new String[]{"id"}, new Object[]{Integer.valueOf(id)}));
                }
            }

            agence.setVille((YvsDictionnaire) dao.loadOneByNameQueries("YvsDictionnaire.findById", new String[]{"id"}, new Object[]{Long.valueOf(ville)}));
            agence.setEmail(email);
            agence.setTelephone(telephone);
            agence.setCodePostal(code_postal);
            agence.setDateUpdate(new Date());
            agence.setActif(Boolean.TRUE);

            agence.setAuthor(yvsUsersAgence(new YvsUsers(Long.valueOf(user)), new YvsAgences(Long.valueOf(agence_user))));

            return (YvsAgences) dao.update(agence);

        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }

    }

    @POST
    @Path("saveNiveauAcces")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsNiveauAcces saveNiveauAcces(@HeaderParam("designation") String designation, @HeaderParam("description") String description, @HeaderParam("grade") String grade,
            @HeaderParam("societe") String societe, @HeaderParam("agence") String agence, @HeaderParam("user") String user) {
        YvsNiveauAcces niveauAcces = new YvsNiveauAcces();
        try {
            niveauAcces.setDesignation(designation);
            niveauAcces.setDescription(description);
            System.out.println("grade =  " + grade);
            niveauAcces.setGrade((YvsUsersGrade) dao.loadOneByNameQueries("YvsUsersGrade.findById", new String[]{"id"}, new Object[]{Long.valueOf(grade)}));
            niveauAcces.setSociete((YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{Long.valueOf(societe)}));
            niveauAcces.setDateSave(new Date());
            niveauAcces.setDateUpdate(new Date());
            niveauAcces.setActif(Boolean.TRUE);
            niveauAcces.setAuthor(yvsUsersAgence((YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(user)}), (YvsAgences) dao.loadOneByNameQueries("YvsAgences.findById", new String[]{"id"}, new Object[]{Long.valueOf(agence)})));
            niveauAcces.setSuperAdmin(Boolean.FALSE);
            return (YvsNiveauAcces) dao.save1(niveauAcces);

        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }

    }

    @POST
    @Path("saveUsers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsUsers saveUsers(@HeaderParam("nom_users") String nom_users, @HeaderParam("code_user") String code_user, @HeaderParam("niveau") String niveau,
            @HeaderParam("groupe") String groupe, @HeaderParam("actif") String actif, @HeaderParam("planing") String planing, @HeaderParam("multi_s") String multi_s,
            @HeaderParam("multi_a") String multi_a, @HeaderParam("agence") String agence) {
        YvsUsers user = new YvsUsers();
        try {
            if (asString(code_user)) {
                YvsUsers use = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findByCode", new String[]{"codeUsers"}, new Object[]{code_user});
                if (use != null ? use.getId() > 0 : false) {
                    user.setId(0L);
                    user.setMessage("Ce login est déjà utiisé");
                    return user;
                } else {
                    user.setNom(nom_users);
                    user.setNomUsers(nom_users);
                    user.setCodeUsers(code_user);
                    user.setAleaMdp(hashMdp.randomString(15));
                    user.setPasswordUser(hashMdp.hashString(user.getCodeUsers() + "" + user.getAleaMdp() + "" + "ADMIN"));

                    if (!groupe.equals("")) {
                        user.setCategorie(new YvsComCategoriePersonnel(Long.valueOf(groupe)));
                    } else {

                    }

                    user.setNiveauAcces(new YvsNiveauAcces(Long.valueOf(niveau)));
                    if (!actif.equals("") ? actif.equals("oui") : false) {
                        user.setActif(Boolean.TRUE);
                    } else {
                        user.setActif(Boolean.FALSE);
                    }

                    if (!planing.equals("") ? planing.equals("oui") : false) {
                        user.setConnectOnlinePlanning(Boolean.TRUE);
                    } else {
                        user.setConnectOnlinePlanning(Boolean.FALSE);
                    }
                    if (!multi_s.equals("") ? multi_s.equals("oui") : false) {
                        user.setAccesMultiSociete(Boolean.TRUE);
                    } else {
                        user.setAccesMultiSociete(Boolean.FALSE);
                    }

                    if (!multi_a.equals("") ? multi_a.equals("oui") : false) {
                        user.setAccesMultiAgence(Boolean.TRUE);
                    } else {
                        user.setAccesMultiAgence(Boolean.FALSE);
                    }
                    user.setDateSave(new Date());
                    user.setDateUpdate(new Date());
                    user.setAgence(new YvsAgences(Long.valueOf(agence)));
                    dao.save(user);

                    YvsNiveauUsers niveauUsers = new YvsNiveauUsers();
                    niveauUsers.setDateSave(new Date());
                    niveauUsers.setDateUpdate(new Date());
                    niveauUsers.setIdNiveau(user.getNiveauAcces());
                    niveauUsers.setIdUser(user);

                    dao.save(niveauUsers);
                    return user;
                }

            } else {
                user.setId(0L);
                user.setMessage("Vous devez renseigner un login pour l'utilisateur");
                return user;
            }

        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("modifUsers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsUsers modifUsers(@HeaderParam("users") String users, @HeaderParam("nom_users") String nom_users, @HeaderParam("code_user") String code_user, @HeaderParam("niveau") String niveau,
            @HeaderParam("groupe") String groupe, @HeaderParam("actif") String actif, @HeaderParam("planing") String planing, @HeaderParam("multi_s") String multi_s,
            @HeaderParam("multi_a") String multi_a, @HeaderParam("agence") String agence
    ) {
        System.out.println("users = " + users);
        System.out.println("nom_users = " + nom_users);
        System.out.println("code_user = " + code_user);
        System.out.println("niveau = " + niveau);
        System.out.println("groupe = " + groupe);
        System.out.println("actif = " + actif);
        System.out.println("planing = " + planing);
        System.out.println("multi_s = " + multi_s);
        System.out.println("multi_a = " + multi_a);
        System.out.println("agence = " + agence);
        try {
            YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(users)});

            user.setNom(nom_users);
            user.setNomUsers(nom_users);
            user.setCodeUsers(code_user);
            user.setAleaMdp(hashMdp.randomString(15));
            user.setPasswordUser(hashMdp.hashString(user.getCodeUsers() + "" + user.getAleaMdp() + "" + "ADMIN"));

            if (!groupe.equals("")) {
                user.setCategorie((YvsComCategoriePersonnel) dao.loadOneByNameQueries("YvsComCategoriePersonnel.findById", new String[]{"id"}, new Object[]{Long.valueOf(groupe)}));
            }
            user.setNiveauAcces((YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauAcces.findById", new String[]{"id"}, new Object[]{Long.valueOf(niveau)}));
            if (!actif.equals("") ? actif.equals("oui") : false) {
                user.setActif(Boolean.TRUE);
            } else {
                user.setActif(Boolean.FALSE);
            }

            if (!planing.equals("") ? planing.equals("oui") : false) {
                user.setConnectOnlinePlanning(Boolean.TRUE);
            } else {
                user.setConnectOnlinePlanning(Boolean.FALSE);
            }
            if (!multi_s.equals("") ? multi_s.equals("oui") : false) {
                user.setAccesMultiSociete(Boolean.TRUE);
            } else {
                user.setAccesMultiSociete(Boolean.FALSE);
            }

            if (!multi_a.equals("") ? multi_a.equals("oui") : false) {
                user.setAccesMultiAgence(Boolean.TRUE);
            } else {
                user.setAccesMultiAgence(Boolean.FALSE);
            }
//            user.setDateSave(new Date());
            user.setDateUpdate(new Date());
            user.setAgence((YvsAgences) dao.loadOneByNameQueries("YvsAgences.findById", new String[]{"id"}, new Object[]{Long.valueOf(agence)}));
            dao.update(user);

            YvsNiveauUsers niveauUsers = (YvsNiveauUsers) dao.loadOneByNameQueries("YvsNiveauUsers.findNiveauByUserS", new String[]{"user", "societe"}, new Object[]{user, user.getAgence().getSociete()});
            niveauUsers.setIdNiveau((YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauAcces.findById", new String[]{"id"}, new Object[]{Long.valueOf(niveau)}));
            niveauUsers.setDateUpdate(new Date());
            dao.update(niveauUsers);

            return user;
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @GET
    @Path("returnVille")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_JSON})
    public List<YvsDictionnaire> returnVille() {
        try {
            return dao.loadNameQueries("YvsDictionnaire.findVilles", new String[]{}, new Object[]{});
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.
                    getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("returnPays")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_JSON})
    public List<YvsDictionnaire> returnPays() {
        try {
            return dao.loadNameQueries("YvsDictionnaire.findPays", new String[]{}, new Object[]{});
        } catch (NumberFormatException ex) {
            Logger.getLogger(lymytz.ws.GenericResource.class.
                    getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("returnGrade")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsUsersGrade> returnGrade() {
        try {
            return dao.loadNameQueries("YvsUsersGrade.findAll", new String[]{}, new Object[]{});
        } catch (Exception e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("returnGradeNotIds")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsUsersGrade> returnGradeNotIds(@HeaderParam("ids") String ids) {
        try {
            List<Long> value = new ArrayList<>();
            if (asString(ids)) {
                for (String id : ids.split(",")) {
                    value.add(Long.valueOf(id));
                }
            }
            if (value.isEmpty()) {
                value.add(-1L);
            }
            return dao.loadNameQueries("YvsUsersGrade.findNotIds", new String[]{"ids"}, new Object[]{value});
        } catch (NumberFormatException e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @GET
    @Path("returnSociete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsSocietes> returnSociete(@HeaderParam("user") String user
    ) {
        List<YvsSocietes> lis = new ArrayList<>();
        try {

            YvsUsers users = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(user)});
            if (users.getSuperAdmin()) {
                return dao.loadNameQueries("YvsSocietes.findAll", new String[]{}, new Object[]{});
            } else if (users.getAccesMultiSociete()) {
                int groupe = users.getAgence().getSociete().getGroupe().getId();
                return dao.loadNameQueries("YvsSocietes.findBySameGroupe", new String[]{"groupe"}, new Object[]{new YvsBaseGroupeSociete(groupe)});
            } else {
                lis.add(users.getAgence().getSociete());
                return lis;
            }

        } catch (Exception e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.
                    getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("returnAgence")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsAgences> returnAgence(@HeaderParam("user") String user
    ) {
        List<YvsAgences> agenceses = new ArrayList<>();
        try {
            YvsUsers users = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(user)});
            YvsSocietes societe = users.getAgence().getSociete();
            System.err.println("societe = " + societe);
            if (users != null ? users.getId() > 0 : false) {
                if (users.getSuperAdmin()) {
                    return dao.loadNameQueries("YvsAgences.findAll", new String[]{}, new Object[]{});
                } else if (users.getAccesMultiSociete()) {
                    return dao.loadNameQueries("YvsAgences.findUsableByGroupe", new String[]{"groupe"}, new Object[]{users.getAgence().getSociete().getGroupe()});
                } else if (users.getAccesMultiAgence()) {
                    return dao.loadNameQueries("YvsAgences.findBySociete", new String[]{"societe"}, new Object[]{users.getAgence().getSociete()});
                } else {
                    agenceses.add(users.getAgence());
                    return agenceses;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.
                    getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @GET
    @Path("getAgences")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsAgences> getAgences(@HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsAgences.findBySociete", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});

        } catch (Exception e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.
                    getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("getAgencesAcces")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsAgences> getAgences(@HeaderParam("societe") String societe, @HeaderParam("users") String users) {
        try {
            return dao.loadNameQueries("YvsUsersAgence.findAgenceActionByUsersSociete", new String[]{"users", "societe"}, new Object[]{new YvsUsers(Long.valueOf(users)), new YvsSocietes(Long.valueOf(societe))});
        } catch (Exception e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("returnAgenceSociete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsAgences> returnAgenceSociete(@HeaderParam("societe") String societe
    ) {
        List<YvsAgences> agenceses = new ArrayList<>();
        try {
            YvsSocietes societes = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{Long.valueOf(societe)});
            System.err.println("societe = " + societe);
            return dao.loadNameQueries("YvsAgences.findBySociete", new String[]{"societe"}, new Object[]{societes});

        } catch (Exception e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.
                    getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("saveSecteur")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsGrhSecteurs saveSecteur(@HeaderParam("societe") String societe, @HeaderParam("nom") String nom, @HeaderParam("description") String description, @HeaderParam("user") String user,
            @HeaderParam("agence") String agence
    ) {
        YvsGrhSecteurs secteur = new YvsGrhSecteurs();
        try {
            secteur.setDateSave(new Date());
            secteur.setDateUpdate(new Date());
            secteur.setNom(nom);
            secteur.setDescription(description);
            secteur.setSociete(new YvsSocietes(Long.valueOf(societe)));
            YvsUsersAgence author = yvsUsersAgence(new YvsUsers(Long.valueOf(user)), new YvsAgences(Long.valueOf(agence)));
            secteur.setAuthor(author);
            secteur.setMessage("Enregistrement effectué avec succès !");
            return (YvsGrhSecteurs) dao.save1(secteur);
        } catch (Exception e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.
                    getName()).log(Level.SEVERE, null, e);
            secteur.setId(0);
            secteur.setMessage("Erreur " + e.getMessage());
            return secteur;
        }
    }

    @POST
    @Path("saveGroupUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsComCategoriePersonnel saveGroupUser(@HeaderParam("societe") String societe, @HeaderParam("code") String code, @HeaderParam("description") String description, @HeaderParam("libelle") String libelle,
            @HeaderParam("agence") String agence, @HeaderParam("user") String user
    ) {
        YvsComCategoriePersonnel item = new YvsComCategoriePersonnel();
        try {
            item.setDateSave(new Date());
            item.setDateUpdate(new Date());
            item.setSociete(new YvsSocietes(Long.valueOf(societe)));
            item.setCode(code);
            item.setDescription(description);
            item.setLibelle(libelle);
            item.setAuthor(yvsUsersAgence(new YvsUsers(Long.valueOf(user)), new YvsAgences(Long.valueOf(agence))));
            item.setMessage("Enregistrement effectué avec succès ");
            return (YvsComCategoriePersonnel) dao.save1(item);
        } catch (Exception e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.
                    getName()).log(Level.SEVERE, null, e);

            item.setId(0L);
            item.setMessage("Erreur " + e.getMessage());
            return item;
        }

    }

    @POST
    @Path("returnSecteur")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsGrhSecteurs> returnSecteur(@HeaderParam("user") String user
    ) {

        try {
            YvsUsers users = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(user)});
            YvsSocietes societe = users.getAgence().getSociete();
            System.err.println("societe = " + societe);
            if (users != null ? users.getId() > 0 : false) {
                return dao.loadNameQueries("YvsSecteurs.findAll", new String[]{"societe"}, new Object[]{societe});
            } else {

                return null;
            }
        } catch (Exception e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.
                    getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("returnSecteurSociete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsGrhSecteurs> returnSecteurSociete(@HeaderParam("societe") String societe
    ) {
        List<YvsGrhSecteurs> agenceses = new ArrayList<>();
        try {
            System.err.println("societe = " + societe);
            return dao.loadNameQueries("YvsSecteurs.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});

        } catch (Exception e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.
                    getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("returnGroupUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsComCategoriePersonnel> returnGroupUser(@HeaderParam("user") String user) {
        List<YvsComCategoriePersonnel> agenceses = new ArrayList<>();
        try {
            YvsUsers users = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(user)});
            YvsSocietes societe = users.getAgence().getSociete();
            System.err.println("societe = " + societe);
            if (users != null ? users.getId() > 0 : false) {
                System.out.println("userSuper = " + users.getSuperAdmin());
                if (users.getSuperAdmin()) {

                    System.out.println("tous les groupes");
                    return dao.loadNameQueries("YvsComCategoriePersonnel.findAlls", new String[]{}, new Object[]{});
                } else {
                    System.out.println("tous les groupes de la societe");
                    return dao.loadNameQueries("YvsComCategoriePersonnel.findAll", new String[]{"societe"}, new Object[]{societe});
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.
                    getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("returnUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsUsers> returnUser(@HeaderParam("user") String user
    ) {
        try {
            YvsUsers users = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(user)});
            if (users != null ? users.getSuperAdmin() : false) {
                return dao.loadNameQueries("YvsUsers.findAllUser", new String[]{}, new Object[]{});
            } else if (users != null ? users.getAccesMultiSociete() : false) {
                return dao.loadNameQueries("YvsUsers.findByGroupeSociete", new String[]{"groupe", "actif"}, new Object[]{users.getAgence().getSociete().getGroupe(), Boolean.TRUE});
            } else {
                return dao.loadNameQueries("YvsUsers.findAll", new String[]{"societe"}, new Object[]{users.getAgence().getSociete()});
            }

        } catch (Exception e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.
                    getName()).log(Level.SEVERE, null, e);
            return null;
        }

    }

    @POST
    @Path("getUsers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsUsers> getUsers(@HeaderParam("societe") String societe
    ) {
        try {
            return dao.loadNameQueries("YvsUsers.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});

        } catch (Exception e) {
            Logger.getLogger(lymytz.ws.GenericResource.class.
                    getName()).log(Level.SEVERE, null, e);
            return null;
        }

    }

    @POST
    @Path("returnNiveau")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsNiveauAcces> returnNiveau(@HeaderParam("user") String user
    ) {
        try {
            YvsUsers users = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(user)});
            if (users.getSuperAdmin()) {
                return dao.loadNameQueries("YvsNiveauAcces.findAll", new String[]{}, new Object[]{});
            } else if (users.getAccesMultiSociete()) {
                return dao.loadNameQueries("YvsNiveauAcces.findByGroupeSociete", new String[]{"groupe"}, new Object[]{users.getAgence().getSociete().getGroupe()});
            } else {
                return dao.loadNameQueries("YvsNiveauAcces.findBySociete", new String[]{"societe"}, new Object[]{users.getAgence().getSociete()});
            }
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @GET
    @Path("returnNiveauSociete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsNiveauAcces> returnNiveauSociete(@HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsNiveauAcces.findBySociete", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

//    @GET
//    @Path("returnRessources")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.TEXT_PLAIN)
//    public List<YvsRessourcesPage> returnRessources() {
//        System.out.println("Liste des ressources");
//        try {
//            return dao.loadNameQueries("YvsRessourcesPage.findAll", new String[]{}, new Object[]{});
//
//        } catch (Exception e) {
//            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
//            return null;
//        }
//    }
//
//    @GET
//    @Path("returnModules")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.TEXT_PLAIN)
//    public List<YvsModule> returnModules() {
//        System.out.println("Liste des modules");
//        try {
//            return dao.loadNameQueries("YvsModule.findAll", new String[]{}, new Object[]{});
//
//        } catch (Exception e) {
//            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
//            return null;
//        }
//    }
//
//    @GET
//    @Path("returnPages")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.TEXT_PLAIN)
//    public List<YvsPageModule> returnPages() {
//        System.out.println("Liste des pages");
//        try {
//            return dao.loadNameQueries("YvsPageModule.findAll", new String[]{}, new Object[]{});
//
//        } catch (Exception e) {
//            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
//            return null;
//        }
//    }
    @POST
    @Path("saveAuttorisations")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String saveAttorisations(@HeaderParam("niveauA") String niveauA, @HeaderParam("ressource") String ressource, @HeaderParam("type") String type
    ) {
        String result = "";
        YvsAutorisationPageModule auto_page;
        YvsAutorisationModule auto_module;
        YvsAutorisationRessourcesPage auto_ressource;
        System.out.println("niveau acces = " + niveauA);
        System.out.println("ressource = " + ressource);
        System.out.println("type = " + type);
        try {
            YvsModule module = new YvsModule();
            YvsPageModule page = new YvsPageModule();
            YvsRessourcesPage res = new YvsRessourcesPage();
            YvsNiveauAcces niveau = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauAcces.findById", new String[]{"id"}, new Object[]{Long.valueOf(niveauA)});
            if (type.equals("YvsModule")) {
                // autorisation sur un module
                module = (YvsModule) dao.loadOneByNameQueries("YvsModule.findById", new String[]{"id"}, new Object[]{Integer.valueOf(ressource)});
                auto_module = (YvsAutorisationModule) dao.loadOneByNameQueries("YvsAutorisationModule.findByModuleNiveau", new String[]{"module", "niveau"}, new Object[]{module, niveau});
                if (auto_module != null ? auto_module.getId() > 0 : false) {
//                    result = "Ce niveau d'accès possède déjà ce droit";
                    auto_module.setAcces(!auto_module.getAcces());
                    dao.update(auto_module);
                    result = "Le droit a bien été ajouter au niveau d'accès";
                } else {
                    auto_module = new YvsAutorisationModule();
                    auto_module.setModule(module);
                    auto_module.setNiveauAcces(niveau);
                    auto_module.setAcces(Boolean.TRUE);
                    auto_module = (YvsAutorisationModule) dao.save1(auto_module);
                    result = "Le droit a bien été ajouter au niveau d'accès";
                }

            } else if (type.equals("YvsPageModule")) {
                // autorisation sur une page

                page = (YvsPageModule) dao.loadOneByNameQueries("YvsPageModule.findById", new String[]{"id"}, new Object[]{Integer.valueOf(ressource)});
                module = page.getModule();
                auto_module = (YvsAutorisationModule) dao.loadOneByNameQueries("YvsAutorisationModule.findByModuleNiveau", new String[]{"module", "niveau"}, new Object[]{module, niveau});
                if (auto_module != null ? auto_module.getAcces() : false) {

                } else {

                    saveAttorisations(niveau.getId().toString(), module.getId().toString(), "YvsModule");
                }

                auto_page = (YvsAutorisationPageModule) dao.loadOneByNameQueries("YvsAutorisationPageModule.findByPageNiveau", new String[]{"page", "niveau"}, new Object[]{page, niveau});
                if (auto_page != null ? auto_page.getId() > 0 : false) {
                    auto_page.setAcces(!auto_page.getAcces());
                    dao.update(auto_page);
                    result = "Le droit a bien été ajouter au niveau d'accès";
                } else {
                    auto_page = new YvsAutorisationPageModule();
                    auto_page.setNiveauAcces(niveau);
                    auto_page.setPageModule(page);
                    auto_page.setAcces(Boolean.TRUE);

                    auto_page = (YvsAutorisationPageModule) dao.save1(auto_page);
                    result = "Le droit a bien été ajouter au niveau d'accès";
                }

                if (!auto_page.getAcces()) {
                    Long nbre = (Long) dao.loadObjectByNameQueries("YvsAutorisationPageModule.findModule", new String[]{"module", "niveau"}, new Object[]{module, niveau});
                    if (nbre != null ? nbre < 1 : true) {
                        saveAttorisations(niveau.getId().toString(), module.getId().toString(), "YvsModule");

                    }
                }
            } else if (type.equals("YvsRessourcesPage")) {
                // autorisation sur une ressource
                res = (YvsRessourcesPage) dao.loadOneByNameQueries("YvsRessourcesPage.findById", new String[]{"id"}, new Object[]{Integer.valueOf(ressource)});
                page = res.getPageModule();
                module = page.getModule();

                auto_page = (YvsAutorisationPageModule) dao.loadOneByNameQueries("YvsAutorisationPageModule.findByPageNiveau", new String[]{"page", "niveau"}, new Object[]{page, niveau});

                if (auto_page != null ? auto_page.getId() > 0 : false) {

                } else {
                    // ajoute de la page
                    saveAttorisations(niveau.getId().toString(), page.getId().toString(), "YvsPageModule");

                }

                auto_ressource = (YvsAutorisationRessourcesPage) dao.loadOneByNameQueries("YvsAutorisationRessourcesPage.findByRessourceNiveau", new String[]{
                    "ressource", "niveau"}, new Object[]{res, niveau});
                if (auto_ressource != null ? auto_ressource.getId() > 0 : false) {
                    auto_ressource.setAcces(!auto_ressource.getAcces());
                    dao.update(auto_ressource);
                    result = "Le droit a bien été ajouter au niveau d'accès";

                } else {
                    auto_ressource = new YvsAutorisationRessourcesPage();
                    auto_ressource.setNiveauAcces(niveau);
                    auto_ressource.setRessourcePage(res);
                    auto_ressource.setAcces(Boolean.TRUE);

                    auto_ressource = (YvsAutorisationRessourcesPage) dao.save1(auto_ressource);
                    result = "Le droit a bien été ajouter au niveau d'accès";
                }

                if (!auto_ressource.getAcces()) {
                    Long nbre = (Long) dao.loadObjectByNameQueries("YvsAutorisationRessourcesPage.countRessource", new String[]{"page", "niveau"}, new Object[]{page, niveau});
                    if (nbre != null ? nbre < 1 : true) {
                        saveAttorisations(niveau.getId().toString(), page.getId().toString(), "YvsPageModule");

                    }

                }
            }
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return "erreur";
        }

        return result;
    }

    @POST
    @Path("getAutorisaions")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<Autorisations> getAutorisaions(@HeaderParam("niveau") String niveau) {
        List<Autorisations> autorisationses = new ArrayList<>();
        try {
            ServiceAutorisation service = new ServiceAutorisation(dao, new YvsNiveauAcces(Long.valueOf(niveau)), null, null);
            autorisationses = service.loadAutorisation();
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return autorisationses;
    }

    @POST
    @Path("deleteSociete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String deleteSociete(@HeaderParam("societe") String societe
    ) {
        String result = "";
        try {
            YvsSocietes bean = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{Long.valueOf(societe)});
            if (bean != null ? bean.getId() > 0 : false) {
                // desactiver les agences
                List<YvsAgences> agences = dao.loadNameQueries("YvsAgences.findBySocieteAll", new String[]{"societe"}, new Object[]{bean});
                for (YvsAgences a : agences) {
                    a.setActif(Boolean.FALSE);
                    dao.update(a);
                }
                // desactiver les users
                List<YvsUsers> users = dao.loadNameQueries("YvsUsers.findBySociete", new String[]{"societe", "actif"}, new Object[]{bean, Boolean.FALSE});
                for (YvsUsers a : users) {
                    a.setActif(Boolean.FALSE);
                    dao.update(a);
                }
                //descativer les niveaux d'acces
                List<YvsNiveauAcces> niveauAcceses = dao.loadNameQueries("YvsNiveauAcces.findBySociete", new String[]{"societe"}, new Object[]{bean});
                for (YvsNiveauAcces a : niveauAcceses) {
                    a.setActif(Boolean.FALSE);
                    dao.update(a);
                }

                bean.setActif(Boolean.FALSE);
                dao.update(bean);

                result = "Société supprimée avec succès !";
            } else {
                result = "Impossible de trouver cette société !";
            }

        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            result = "Echec lors de la suppression ! ";
        }
        return result;
    }

    @POST
    @Path("getAutorisaionsModules")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<Autorisations> getAutorisaionsModules(@HeaderParam("niveau") String niveau) {
        List<Autorisations> autorisationses = new ArrayList<>();
        try {

            String query = "SELECT y.id, y.reference,y.libelle,y.description,coalesce((select a.acces from yvs_autorisation_module a where a.module = y.id and a.niveau_acces = ?),false) acces from yvs_module y";
            List<Object> list = dao.loadListBySqlQuery(query, new Options[]{new Options(Long.valueOf(niveau), 1)});
            if (list != null) {
                Object[] tab;
                for (Object o : list) {
                    tab = (Object[]) o;
                    autorisationses.add(new Autorisations((Integer) tab[0], (String) tab[1], (String) tab[2], (String) tab[3], (Boolean) tab[4], "YvsModule", "", null, null));
                }
            }

        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }

        return autorisationses;
    }

    @POST
    @Path("deleteAgence")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String deleteAgence(@HeaderParam("agence") String agence) {
        String result = "";
        try {
            YvsAgences agences = (YvsAgences) dao.loadOneByNameQueries("YvsAgences.findById", new String[]{"id"}, new Object[]{Long.valueOf(agence)});

            List<YvsUsers> listUser = dao.loadNameQueries("YvsUsers.findByAgence", new String[]{"agence", "actif"}, new Object[]{agences, Boolean.TRUE});

            if (listUser != null ? !listUser.isEmpty() : false) {
                for (YvsUsers u : listUser) {
                    u.setActif(Boolean.FALSE);
                    dao.update(u);
                }
            }
            dao.update(agences);

            result = "La suppression a été éffectuée avec succès !";
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            result = "Echec de la suprressio ! ";
        }
        System.out.println(result);
        return result;
    }

    @POST
    @Path("deleteUSer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String deleteUSer(@HeaderParam("user") String user) {
        String result = "";
        System.out.println("log 1");
        try {
            YvsUsers users = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(user)});
            users.setActif(Boolean.FALSE);
            dao.update(users);
            System.out.println("log 2");
            result = "La suppression a été éffectuée avec succès !";
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            result = "Echec de la suprressio ! ";
        }
        System.out.println(result);
        return result;
    }

    @POST
    @Path("findByFamilleParentArticle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseFamilleArticle> findByFamilleParentArticle(@HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsBaseFamilleArticle.findParent", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("findByFamilleArticle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseFamilleArticle> findByFamilleArticle(@HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsBaseFamilleArticle.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("findByGroupeArticle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseGroupesArticle> findByGroupeArticle(@HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsBaseGroupesArticle.findBySociete", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("findByDepot")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseDepots> findByDepot(@HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsBaseDepots.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }

    }

    @POST
    @Path("findByCategorieClient")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseCategorieClient> findByCategorieClient(@HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsBaseCategorieClient.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("findByFournisseur")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseFournisseur> findByFournisseur(@HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsBaseFournisseur.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("findByCategorieComptable")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseCategorieComptable> findByCategorieComptable(@HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsBaseCategorieComptable.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("findByTaxes")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseTaxes> findByTaxes(@HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsBaseTaxes.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("findByClasse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseClassesStat> findByClasse(@HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsBaseClassesStat.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("returnClasseNotIds")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseClassesStat> returnClasseNotIds(@HeaderParam("societe") String societe, @HeaderParam("ids") String ids) {
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
                return dao.loadNameQueries("YvsBaseClassesStat.findNotIds", new String[]{"societe", "ids"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), value});
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @POST
    @Path("findByUniteMesure")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseUniteMesure> findByUniteMesure(@HeaderParam("societe") String societe) {
        try {
            return dao.loadNameQueries("YvsBaseUniteMesure.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("saveArticle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsBaseArticles saveArticle(@HeaderParam("designation") String designation, @HeaderParam("refArt") String refArt, @HeaderParam("categorie") String categorie, @HeaderParam("famille") String famille, @HeaderParam("groupe") String groupe, @HeaderParam("type_service") String type_service, @HeaderParam("valorisation") String valorisation, @HeaderParam("classe1") String classe1, @HeaderParam("classe2") String classe2) {
        YvsBaseArticles article = new YvsBaseArticles();
        System.out.println("designation = " + designation);
        System.out.println("refArt = " + refArt);
        System.out.println("categorie = " + categorie);
        System.out.println("famille = " + famille);
        System.out.println("groupe = " + groupe);
        System.out.println("type_service = " + type_service + "");
        System.out.println("valorisation = " + valorisation);
        System.out.println("classe1 = " + classe1);
        System.out.println("classe2 = " + classe2);

        try {
            if (asString(refArt) ? asString(designation) : false) {
                YvsBaseFamilleArticle fam = (YvsBaseFamilleArticle) dao.loadOneByNameQueries("YvsBaseFamilleArticle.findById", new String[]{"id"}, new Object[]{Long.valueOf(famille)});
                if (fam != null) {
                    Long existe = (Long) dao.loadOneByNameQueries("YvsBaseArticles.findCByRefArt", new String[]{"refArt", "societe"}, new Object[]{refArt, fam.getSociete()});
                    if (existe != null ? existe > 0 : false) {
                        article.setMessage_service("Cette Référence existe déjà !");
                        article.setId(-1L);
                        return article;
                    } else {
                        article.setRefArt(refArt);
                        article.setDesignation(designation);
                        if (fam != null ? fam.getId() > 0 : false) {
                            article.setFamille(fam);
                        } else {
                            article.setId(-1L);
                            article.setMessage_service("Vous devez choisir un famille d'article");
                            return article;
                        }
                        article.setCategorie(categorie);
                        article.setTypeService(type_service.charAt(0));
                        article.setGroupe((YvsBaseGroupesArticle) dao.loadOneByNameQueries("YvsBaseGroupesArticle.findById", new String[]{"id"}, new Object[]{Long.valueOf(groupe)}));
                        article.setActif(Boolean.TRUE);
                        article.setMethodeVal(valorisation);
                        if (asString(classe1)) {
                            article.setClasse1((YvsBaseClassesStat) dao.loadOneByNameQueries("YvsBaseClassesStat.findById", new String[]{"id"}, new Object[]{Long.valueOf(classe1)}));
                        }
                        if (asString(classe2)) {
                            article.setClasse2((YvsBaseClassesStat) dao.loadOneByNameQueries("YvsBaseClassesStat.findById", new String[]{"id"}, new Object[]{Long.valueOf(classe2)}));

                        }
                        article.setMessage_service("Enregistrement éffectué avec succès");
                        article = (YvsBaseArticles) dao.save1(article);
                    }
                } else {
                    article.setMessage_service("Vous devez choisir la famille!");
                    article.setId(-1L);
                    return article;
                }
            }

        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);

        }
        return article;
    }

    @POST
    @Path("updateArticle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsBaseArticles updateArticle(@HeaderParam("articles") String idArticle, @HeaderParam("designation") String designation, @HeaderParam("refArt") String refArt, @HeaderParam("categorie") String categorie, @HeaderParam("famille") String famille, @HeaderParam("groupe") String groupe, @HeaderParam("type_service") String type_service, @HeaderParam("valorisation") String valorisation, @HeaderParam("classe1") String classe1, @HeaderParam("classe2") String classe2, @HeaderParam("societe") String societe) {
        YvsBaseArticles article = new YvsBaseArticles();
        System.out.println("article = " + idArticle);
        System.out.println("designation = " + designation);
        System.out.println("refArt = " + refArt);
        System.out.println("categorie = " + categorie);
        System.out.println("famille = " + famille);
        System.out.println("groupe = " + groupe);
        System.out.println("type_service = " + type_service + "");
        System.out.println("valorisation = " + valorisation);
        System.out.println("classe1 = " + classe1);
        System.out.println("classe2 = " + classe2);

        try {
            if (asString(refArt) ? asString(designation) : false) {
                article = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{Long.valueOf(idArticle)});
                Long existe = (Long) dao.loadOneByNameQueries("YvsBaseArticles.findCByRefArtEx", new String[]{"refArt", "id", "societe"}, new Object[]{refArt, Long.valueOf(idArticle), Long.valueOf(idArticle)});
                if (existe != null ? existe > 0 : false) {
                    System.err.println(" ---- Id" + article.getId() + " ");
                    article.setMessage_service("Cette Référence existe déjà !");
                    article.setId(-1L);
                    return article;
                } else {
                    article.setRefArt(refArt);
                    article.setDesignation(designation);
                    YvsBaseFamilleArticle fam = (YvsBaseFamilleArticle) dao.loadOneByNameQueries("YvsBaseFamilleArticle.findById", new String[]{"id"}, new Object[]{Long.valueOf(famille)});
                    if (fam != null ? fam.getId() > 0 : false) {
                        article.setFamille(fam);
                    } else {
                        article.setId(-1L);
                        article.setMessage_service("Vous devez choisir un famille d'article");
                        return article;
                    }
                    article.setCategorie(categorie);
                    article.setTypeService(type_service.charAt(0));
                    article.setGroupe((YvsBaseGroupesArticle) dao.loadOneByNameQueries("YvsBaseGroupesArticle.findById", new String[]{"id"}, new Object[]{Long.valueOf(groupe)}));
                    article.setActif(Boolean.TRUE);
                    article.setMethodeVal(valorisation);
                    if (asString(classe1)) {
                        article.setClasse1((YvsBaseClassesStat) dao.loadOneByNameQueries("YvsBaseClassesStat.findById", new String[]{"id"}, new Object[]{Long.valueOf(classe1)}));
                    }
                    if (asString(classe2)) {
                        article.setClasse2((YvsBaseClassesStat) dao.loadOneByNameQueries("YvsBaseClassesStat.findById", new String[]{"id"}, new Object[]{Long.valueOf(classe2)}));

                    }

                    article = (YvsBaseArticles) dao.update(article);
                    article.setMessage_service("Modification éffectué avec succès");
                }
            }

        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);

        }
        return article;
    }

    @POST
    @Path("saveConditionnement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)

    public YvsBaseConditionnement saveConditionnement(@HeaderParam("article") String article, @HeaderParam("unite") String unite, @HeaderParam("prix") String prix, @HeaderParam("prix_min") String prix_min, @HeaderParam("remise") String remise, @HeaderParam("prix_achat") String prix_achat, @HeaderParam("by_achat") String by_achat, @HeaderParam("by_vente") String by_vente, @HeaderParam("by_prod") String by_prod, @HeaderParam("defaut") String defaut) {
        YvsBaseConditionnement bean = new YvsBaseConditionnement();
        try {
            if (asString(article)) {
                bean.setArticle((YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{Long.valueOf(article)}));
                if (asString(unite)) {
                    bean.setUnite((YvsBaseUniteMesure) dao.loadOneByNameQueries("YvsBaseUniteMesure.findById", new String[]{"id"}, new Object[]{Long.valueOf(unite)}));
                } else {
                    bean.setId(-1L);
                    bean.setMessage_service("Vous devez choisir une unité");
                    return bean;
                }
                bean.setPrix(Double.valueOf(prix));
                bean.setPrixMin(Double.valueOf(prix_min));
                bean.setPrixAchat(Double.valueOf(prix_achat));
                bean.setRemise(Double.valueOf(remise));
                if (by_achat.equals("true")) {
                    bean.setByAchat(Boolean.TRUE);
                } else {
                    bean.setByAchat(Boolean.FALSE);
                }

                if (by_prod.equals("true")) {
                    bean.setByProd(Boolean.TRUE);
                } else {
                    bean.setByProd(Boolean.FALSE);
                }
                if (by_vente.equals("true")) {
                    bean.setByVente(Boolean.TRUE);
                } else {
                    bean.setByVente(Boolean.FALSE);
                }
                if (defaut.equals("true")) {
                    bean.setDefaut(Boolean.TRUE);
                } else {
                    bean.setDefaut(Boolean.FALSE);
                }

                bean.setDateSave(new Date());
                bean.setDateUpdate(new Date());
                bean = (YvsBaseConditionnement) dao.save1(bean);
                bean.setMessage_service("Enregistrement effectué avec succès");
            }

        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }

        return bean;
    }

    @POST
    @Path("updateConditionnement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)

    public YvsBaseConditionnement updateConditionnement(@HeaderParam("condi") String condi, @HeaderParam("article") String article, @HeaderParam("unite") String unite, @HeaderParam("prix") String prix, @HeaderParam("prix_min") String prix_min, @HeaderParam("remise") String remise, @HeaderParam("prix_achat") String prix_achat, @HeaderParam("by_achat") String by_achat, @HeaderParam("by_vente") String by_vente, @HeaderParam("by_prod") String by_prod, @HeaderParam("defaut") String defaut) {
        YvsBaseConditionnement bean = new YvsBaseConditionnement();
        try {
            if (asString(article)) {
                bean = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findById", new String[]{"id"}, new Object[]{Long.valueOf(condi)});
                bean.setArticle((YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{Long.valueOf(article)}));
                if (asString(unite)) {
                    bean.setUnite((YvsBaseUniteMesure) dao.loadOneByNameQueries("YvsBaseUniteMesure.findById", new String[]{"id"}, new Object[]{Long.valueOf(unite)}));
                } else {
                    bean.setId(-1L);
                    bean.setMessage_service("Vous devez choisir une unité");
                    return bean;
                }
                bean.setPrix(Double.valueOf(prix));
                bean.setPrixMin(Double.valueOf(prix_min));
                bean.setPrixAchat(Double.valueOf(prix_achat));
                bean.setRemise(Double.valueOf(remise));
                if (by_achat.equals("true")) {
                    bean.setByAchat(Boolean.TRUE);
                } else {
                    bean.setByAchat(Boolean.FALSE);
                }

                if (by_prod.equals("true")) {
                    bean.setByProd(Boolean.TRUE);
                } else {
                    bean.setByProd(Boolean.FALSE);
                }
                if (by_vente.equals("true")) {
                    bean.setByVente(Boolean.TRUE);
                } else {
                    bean.setByVente(Boolean.FALSE);
                }
                if (defaut.equals("true")) {
                    bean.setDefaut(Boolean.TRUE);
                } else {
                    bean.setDefaut(Boolean.FALSE);
                }

                bean.setDateSave(new Date());
                bean.setDateUpdate(new Date());

                bean = (YvsBaseConditionnement) dao.update(bean);
                bean.setMessage_service("Modification effectué avec succès");
            }

        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }

        return bean;
    }

    @POST
    @Path("returnCondiByArticle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseConditionnement> returnCondiByArticle(@HeaderParam("article") String article) {
        try {
            return dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("saveArticleDepot")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsBaseArticleDepot saveArticleDepot(@HeaderParam("article") String article, @HeaderParam("depot") String depot, @HeaderParam("stock_max") String stock_max, @HeaderParam("stock_min") String stock_min, @HeaderParam("mode_appro") String mode_appro, @HeaderParam("stock_alerte") String stock_alerte, @HeaderParam("mode_reappro") String mode_reappro,
            @HeaderParam("type_doc") String type_doc, @HeaderParam("generer_doc") String generer_doc, @HeaderParam("lot") String lot, @HeaderParam("interval") String interval,
            @HeaderParam("defaultCond") String defaultCond, @HeaderParam("suivi") String suivi) {
        YvsBaseArticleDepot articleDepot = new YvsBaseArticleDepot();
        try {
            if (asString(article)) {
                articleDepot.setArticle((YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{Long.valueOf(article)}));
                if (asString(depot)) {

                } else {
                    articleDepot.setMessage_service("Vous devez choisir un depot");
                    articleDepot.setId(-1L);
                    return articleDepot;
                }
                if (asString(defaultCond)) {
                    articleDepot.setDefaultCond((YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findById", new String[]{"id"}, new Object[]{Long.valueOf(defaultCond)}));
                }
                if (generer_doc.equals("true")) {
                    articleDepot.setGenererDocument(Boolean.TRUE);
                } else {
                    articleDepot.setGenererDocument(Boolean.FALSE);
                }

                YvsBaseDepots dep = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{Long.valueOf(depot)});
                YvsBaseArticleDepot artDep = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleEmplacement", new String[]{"article", "emplacement"}, new Object[]{new YvsBaseArticles(Long.valueOf(article)), dep});
                if (artDep != null ? artDep.getId() != null : false) {
                    articleDepot.setMessage_service("Ce dépot est déjà enregistrer");
                    articleDepot.setId(-1L);
                    return articleDepot;
                } else {
                    articleDepot.setDepot(dep);
                    articleDepot.setTypeDocumentGenerer(type_doc);
                    articleDepot.setStockMax(Double.valueOf(stock_max));
                    articleDepot.setStockMin(Double.valueOf(stock_min));
                    articleDepot.setStockAlert(Double.valueOf(stock_alerte));
                    articleDepot.setModeAppro(mode_appro);
                    articleDepot.setModeReappro(mode_reappro);
                    articleDepot.setLotLivraison(Integer.valueOf(lot));
                    articleDepot.setIntervalApprov(Integer.valueOf(interval));

                    articleDepot.setDateSave(new Date());
                    articleDepot.setDateUpdate(new Date());
                    articleDepot.setActif(Boolean.TRUE);
                    if (suivi.equals("true")) {
                        articleDepot.setSuiviStock(Boolean.TRUE);
                    } else {
                        articleDepot.setSuiviStock(Boolean.FALSE);
                    }
                    articleDepot = (YvsBaseArticleDepot) dao.save1(articleDepot);
                    articleDepot.setMessage_service("Enregistrement effectué avec succès");
                }
            } else {
                articleDepot.setId(-1L);
                articleDepot.setMessage_service("Vous devez choisir un article");
                return articleDepot;
            }

        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return articleDepot;
    }

    @POST
    @Path("returnArticleDepot")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseArticleDepot> returnArticleDepot(@HeaderParam("article") String article) {
        try {
            return dao.loadNameQueries("YvsBaseArticleDepot.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("savePlanTarifaire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsBasePlanTarifaire savePlanTarifaire(@HeaderParam("article") String article, @HeaderParam("categorie") String categorie, @HeaderParam("conditionnement") String conditionnement, @HeaderParam("puv") String puv, @HeaderParam("puvMin") String puvMin, @HeaderParam("remise") String remise, @HeaderParam("coef") String coef, @HeaderParam("nature") String nature, @HeaderParam("nature_remise") String nature_remise, @HeaderParam("nature_prix") String nature_prix) {
        YvsBasePlanTarifaire plan = new YvsBasePlanTarifaire();
        try {
            YvsBaseArticles art = new YvsBaseArticles();
            if (asString(article)) {
                art = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{Long.valueOf(article)});
                plan.setArticle(art);
            } else {
                plan.setId(-1L);
                plan.setMessage_service("Vous devez choisir un article");
                return plan;
            }
            if (asString(conditionnement)) {
                plan.setConditionnement((YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findById", new String[]{"id"}, new Object[]{Long.valueOf(conditionnement)}));
            } else {
                plan.setId(-1L);
                plan.setMessage_service("Vous devez choisir un conditionnement");
                return plan;
            }
            if (asString(categorie)) {
                plan.setCategorie((YvsBaseCategorieClient) dao.loadOneByNameQueries("YvsBaseCategorieClient.findById", new String[]{"id"}, new Object[]{Long.valueOf(categorie)}));
            } else {
                plan.setId(-1L);
                plan.setMessage_service("Vous devez choisir une catégorie");
                return plan;
            }
            plan.setPuv(Double.valueOf(puv));
            plan.setPuvMin(Double.valueOf(puvMin));
            plan.setRemise(Double.valueOf(remise));
            plan.setCoefAugmentation(Double.valueOf(coef));
            plan.setNatureCoefAugmentation(nature);
            plan.setNaturePrixMin(nature_prix);
            plan.setNatureRemise(nature_remise);
            plan.setDateSave(new Date());
            plan.setDateUpdate(new Date());
            plan.setActif(Boolean.TRUE);
            plan.setFamille(art.getFamille());

            plan = (YvsBasePlanTarifaire) dao.save1(plan);
            plan.setMessage_service("Enregistrement effectué avec succès");
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return plan;
    }

    @POST
    @Path("returnPlanByArticle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBasePlanTarifaire> returnPlanByArticle(@HeaderParam("article") String article) {
        try {
            return dao.loadNameQueries("YvsBasePlanTarifaire.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("saveFournisseur")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsBaseArticleFournisseur saveFournisseur(@HeaderParam("article") String article, @HeaderParam("fournisseur") String fournisseur, @HeaderParam("delaiLivraison") String delaiLivraison, @HeaderParam("dureeGarantie") String dureeGarantie, @HeaderParam("dureeVie") String dureeVie, @HeaderParam("remise") String remise,
            @HeaderParam("ref_externe") String ref_externe, @HeaderParam("des_externe") String des_externe, @HeaderParam("unite_delai") String unite_delai, @HeaderParam("unite_garan") String unite_garan, @HeaderParam("unite_vie") String unite_vie) {
        YvsBaseArticleFournisseur four = new YvsBaseArticleFournisseur();
        try {
            YvsBaseArticles art = new YvsBaseArticles();
            if (asString(article)) {
                art = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{Long.valueOf(article)});
                four.setArticle(art);
            } else {
                four.setId(-1L);
                four.setMessage_service("Vous devez choisir un article");
                return four;
            }
            if (asString(fournisseur)) {
                four.setFournisseur((YvsBaseFournisseur) dao.loadOneByNameQueries("YvsBaseFournisseur.findById", new String[]{"id"}, new Object[]{Long.valueOf(fournisseur)}));
            } else {
                four.setId(-1L);
                four.setMessage_service("Vous devez choisir un fournisseur");
                return four;
            }
            four.setDelaiLivraison(Double.valueOf(delaiLivraison));
            four.setDureeGarantie(Double.valueOf(dureeGarantie));
            four.setDureeVie(Double.valueOf(dureeVie));
            four.setRemise(Double.valueOf(remise));
            four.setRefArtExterne(ref_externe);
            four.setDesArtExterne(des_externe);
            four.setUniteDelai(unite_delai);
            four.setUniteGarantie(unite_garan);
            four.setUniteVie(unite_vie);

            four = (YvsBaseArticleFournisseur) dao.save1(four);
            four.setMessage_service("Enregistrement effectué avec succès !");
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return four;
    }

    @POST
    @Path("returnFournisseur")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseArticleFournisseur> returnFournisseur(@HeaderParam("article") String article) {
        try {
            return dao.loadNameQueries("YvsBaseArticleFournisseur.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("saveComptabilite")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsBaseArticleCategorieComptable saveComptabilite(@HeaderParam("article") String article, @HeaderParam("compte") String compte, @HeaderParam("categorie") String categorie, @HeaderParam("taxe") String taxe) {
        YvsBaseArticleCategorieComptable compta = new YvsBaseArticleCategorieComptable();
        try {
            YvsBaseArticles art = new YvsBaseArticles();
            if (!asString(article)) {
                compta.setId(-1L);
                compta.setMessage("Vous devez choisir un article");
                return compta;
            } else if (!asString(compte)) {
                compta.setId(-1L);
                compta.setMessage("Vous devez entrer un numéro de compte");
                return compta;
            }
            if (!asString(categorie)) {
                compta.setId(-1L);
                compta.setMessage("Vous devez choisir une catégorie comptable");
                return compta;
            }
            art = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{Long.valueOf(article)});
            compta.setArticle(art);
            YvsBasePlanComptable c = (YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findByNumCompte", new String[]{"numCompte", "societe"}, new Object[]{compte, art.getFamille().getSociete()});
            if (c == null ? c.getId() < 1 : false) {
                compta.setId(-1L);
                compta.setMessage("Le numéro de compte ne correspond à aucun compte ");
                return compta;
            }
            compta.setCompte(c);
            YvsBaseCategorieComptable cat = (YvsBaseCategorieComptable) dao.loadOneByNameQueries("YvsBaseCategorieComptable.findById", new String[]{"id"}, new Object[]{Long.valueOf(categorie)});

            YvsBaseArticleCategorieComptable catCom = (YvsBaseArticleCategorieComptable) dao.loadOneByNameQueries("YvsBaseArticleCategorieComptable.findByCategorie", new String[]{"categorie"}, new Object[]{cat});
            if (catCom != null ? catCom.getId() != null : false) {
                compta.setId(-1L);
                compta.setMessage("Cette catégorie comptable existe déjà pour cet article ");
                return compta;
            } else {
                compta.setCategorie(cat);
                compta.setActif(Boolean.TRUE);
                compta.setDateSave(new Date());
                compta.setDateUpdate(new Date());

                compta = (YvsBaseArticleCategorieComptable) dao.save1(compta);
                compta.setMessage("Enregistrement effectué avec succès");
                if (asString(taxe)) {
                    String t[] = taxe.split("-");
                    for (int i = 0; i < t.length; i++) {
                        YvsBaseArticleCategorieComptableTaxe ct = (YvsBaseArticleCategorieComptableTaxe) dao.loadOneByNameQueries("YvsBaseArticleCategorieComptableTaxe.findByArticleTaxe", new String[]{"article", "taxe"}, new Object[]{compta, new YvsBaseTaxes(Long.valueOf(t[i]))});
                        if (ct != null ? ct.getId() > 0 : false) {
                            compta.setMessage("Cette taxe existe déjà !");
                        } else {
                            ct = new YvsBaseArticleCategorieComptableTaxe();
                            ct.setArticleCategorie(compta);
                            ct.setTaxe((YvsBaseTaxes) dao.loadOneByNameQueries("YvsBaseTaxes.findById", new String[]{"id"}, new Object[]{Long.valueOf(t[i])}));
                            ct.setAppRemise(Boolean.FALSE);
                            ct.setActif(Boolean.TRUE);
                            ct.setDateSave(new Date());
                            ct.setDateUpdate(new Date());
                            compta.setMessage("Enregistrement effectué avec succès");
                            dao.save(ct);
                        }
                    }
                } else {
                    compta.setMessage("Vous devez choisir au moins une taxe ");
                }
            }

        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        System.err.println("compta.getMessage() : " + compta.getMessage());
        return compta;
    }

    @POST
    @Path("returnComptabilite")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseArticleCategorieComptable> returnComptabilite(@HeaderParam("article") String article) {
        try {
            return dao.loadNameQueries("YvsBaseArticleCategorieComptable.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("saveDescription")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsBaseArticleDescription saveDescription(@HeaderParam("titre") String titre, @HeaderParam("description") String description, @HeaderParam("article") String article) {
        YvsBaseArticleDescription des = new YvsBaseArticleDescription();
        try {
            if (!asString(article)) {
                des.setId(-1L);
                des.setMessage("Vous devez choisir un article");
                return des;
            } else {
                YvsBaseArticles art = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{Long.valueOf(article)});
                des.setArticle(art);
                des.setTitre(titre);
                des.setDescription(description);
                des.setDateSave(new Date());
                des.setDateUpdate(new Date());

                des = (YvsBaseArticleDescription) dao.save1(des);
                des.setMessage("Enregistrement effectué avec succès");
                System.out.println("description = " + des.getId());
            }

        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return des;
    }

    @POST
    @Path("returnDescription")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseArticleDescription> returnDescription(@HeaderParam("article") String article) {
        try {
            return dao.loadNameQueries("YvsBaseArticleDescription.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(article))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("returnArticle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseArticles> returnArticle(@HeaderParam("societe") String societe) {
        List<YvsBaseArticles> list = new ArrayList<>();
        try {
            return dao.loadNameQueries("YvsBaseArticles.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("returnPointVenteOnline")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsBasePointVente returnPointVenteOnline(@HeaderParam("societe") String societe) {
        try {
            return (YvsBasePointVente) dao.loadOneByNameQueries("YvsBasePointVente.findByVenteOnline", new String[]{"societe", "venteOnline"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), true});
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("returnConditionnementsByPoint")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsBaseConditionnement> returnConditionnementsByPoint(@HeaderParam("point") String point) {
        try {
            return dao.loadNameQueries("YvsBaseConditionnementPoint.findConditionnementByPoint", new String[]{"point"}, new Object[]{new YvsBasePointVente(Long.valueOf(point))});
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("saveFamille")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsBaseFamilleArticle saveFamille(@HeaderParam("societe") String societe, @HeaderParam("ref") String ref,
            @HeaderParam("designation") String designation, @HeaderParam("actif") String actif, @HeaderParam("desc") String desc) {
        YvsBaseFamilleArticle famille = new YvsBaseFamilleArticle();
        try {
            if (!asString(societe)) {
                famille.setId(-1L);
                famille.setMessage("Veuillez choisir un article");
                return famille;
            } else {
                famille.setSociete((YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{Long.valueOf(societe)}));
                famille.setReferenceFamille(ref);
                famille.setDesignation(designation);
                famille.setDescription(desc);
                if (actif.equals("true")) {
                    famille.setActif(Boolean.TRUE);
                } else {
                    famille.setActif(Boolean.FALSE);
                }

                famille.setDateSave(new Date());
                famille.setDateUpdate(new Date());
                famille.setMessage("Enregistrement effectué avec succès");
                return (YvsBaseFamilleArticle) dao.save1(famille);
            }

        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);

            return null;
        }

    }

    @POST
    @Path("saveUnite")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsBaseUniteMesure saveUnite(@HeaderParam("libelle") String libelle, @HeaderParam("desc") String des, @HeaderParam("type") String type, @HeaderParam("societe") String societe) {
        YvsBaseUniteMesure unite = new YvsBaseUniteMesure();
        try {
            if (asString(societe)) {
                unite.setDateSave(new Date());
                unite.setDateUpdate(new Date());
                unite.setLibelle(libelle);
                unite.setDescription(des);
                unite.setType(type);
                unite.setSociete((YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{Long.valueOf(societe)}));
                unite.setMessage("Enregistrement effectué avec succès !");
                return (YvsBaseUniteMesure) dao.save1(unite);
            } else {
                unite.setId(-1L);
                unite.setMessage("Vous devez choisir un societe");
                return unite;
            }
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("UpdateActif")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsBaseArticles UpdateActif(@HeaderParam("article") String article) {
        YvsBaseArticles articles = new YvsBaseArticles();
        try {
            if (asString(article)) {
                articles = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{Long.valueOf(article)});
                if (articles != null ? articles.getId() != null : false) {
                    articles.setActif(!articles.getActif());

                    articles = (YvsBaseArticles) dao.update(articles);
                    articles.setMessage_service("Article modifié");
                }
            } else {
                articles.setId(-1L);
                articles.setMessage_service("Vous devez choisir un article");
                return articles;
            }
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return articles;
    }

}
