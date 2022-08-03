/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws.base.v1;

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
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleAnalytique;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseArticleEquivalent;
import yvs.entity.base.YvsBaseArticleFournisseur;
import yvs.entity.base.YvsBaseArticlePoint;
import yvs.entity.base.YvsBaseArticleSubstitution;
import yvs.entity.base.YvsBaseCaisseUser;
import yvs.entity.base.YvsBaseClassesStat;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseEmplacementDepot;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseModeleReference;
import yvs.entity.base.YvsBaseParametre;
import yvs.entity.base.YvsBasePointLivraison;
import yvs.entity.base.YvsBasePointVenteDepot;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.YvsComCategoriePersonnel;
import yvs.entity.commercial.YvsComLiaisonDepot;
import yvs.entity.commercial.client.YvsBasePlanTarifaire;
import yvs.entity.commercial.client.YvsComCategorieTarifaire;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBaseComptesCaisse;
import yvs.entity.compta.YvsBaseLiaisonCaisse;
import yvs.entity.compta.YvsBaseNatureCompte;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsBaseTypeDocDivers;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.analytique.YvsComptaPlanAnalytique;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsSocietes;
import yvs.entity.produits.YvsBaseArticleCodeBarre;
import yvs.entity.produits.YvsBaseArticleDescription;
import yvs.entity.produits.YvsBaseArticlePack;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.YvsBaseArticlesAvis;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.entity.produits.group.YvsBaseGroupesArticle;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsNiveauUsers;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.service.IEntitySax;
import yvs.service.base.comptabilite.IYvsBaseArticleFournisseur;
import yvs.service.base.comptabilite.IYvsBaseCaisse;
import yvs.service.base.comptabilite.IYvsBaseCaisseUser;
import yvs.service.base.comptabilite.IYvsBaseComptesCaisse;
import yvs.service.base.comptabilite.IYvsBaseExercice;
import yvs.service.base.comptabilite.IYvsBaseLiaisonCaisse;
import yvs.service.base.comptabilite.IYvsBasePlanComptable;
import yvs.service.base.comptabilite.IYvsComptaJournaux;
import yvs.service.base.comptabilite.IYvsComptaPlanAnalytique;
import yvs.service.base.param.IYvsBaseModeReglement;
import yvs.service.base.param.IYvsBaseNatureCompte;
import yvs.service.base.param.IYvsBaseParametre;
import yvs.service.base.param.IYvsBaseTaxes;
import yvs.service.base.param.IYvsComCategoriePersonnel;
import yvs.service.base.param.IYvsGrhTrancheHoraire;
import yvs.service.base.param.IYvsGrhTypeCout;
import yvs.service.base.produit.IYvsBaseArticleAnalytique;
import yvs.service.base.produit.IYvsBaseArticleCategorieComptable;
import yvs.service.base.produit.IYvsBaseArticleCodeBarre;
import yvs.service.base.produit.IYvsBaseArticleDepot;
import yvs.service.base.produit.IYvsBaseArticleDescription;
import yvs.service.base.produit.IYvsBaseArticleEquivalent;
import yvs.service.base.produit.IYvsBaseArticlePack;
import yvs.service.base.produit.IYvsBaseArticleSubstitution;
import yvs.service.base.produit.IYvsBaseArticles;
import yvs.service.base.produit.IYvsBaseClassesStat;
import yvs.service.base.produit.IYvsBaseConditionnement;
import yvs.service.base.emplacements.IYvsBaseDepots;
import yvs.service.base.emplacements.IYvsBaseEmplacementDepot;
import yvs.service.base.emplacements.IYvsBasePointVenteDepot;
import yvs.service.base.emplacements.IYvsComLiaisonDepot;
import yvs.service.base.param.IYvsBaseArticlePoint;
import yvs.service.base.param.IYvsBasePointLivraison;
import yvs.service.base.param.IYvsBaseTypeDocDivers;
import yvs.service.base.produit.IYvsBaseArticlesAvis;
import yvs.service.base.produit.IYvsBaseFamilleArticle;
import yvs.service.base.produit.IYvsBaseGroupesArticle;
import yvs.service.base.produit.IYvsBaseModeleReference;
import yvs.service.base.produit.IYvsBasePlanTarifaire;
import yvs.service.base.produit.IYvsBaseUniteMesure;
import yvs.service.base.produit.IYvsDictionnaire;
import yvs.service.base.tiers.IYvsBaseFournisseur;
import yvs.service.base.tiers.IYvsBaseTiers;
import yvs.service.base.tiers.IYvsComCategorieTarifaire;
import yvs.service.base.tiers.IYvsComClient;
import yvs.service.base.utilisateur.IYvsNiveauAcces;
import yvs.service.base.utilisateur.IYvsUsers;

/**
 *
 * @author hp Elite 8300
 */
@Path("services/donnees_base/v1")
@RequestScoped
public class GenericResource extends lymytz.ws.base.GenericResource {

    IEntitySax IEntitiSax = new IEntitySax();

    @POST
    @Path("toString")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public ResultatAction<YvsBaseArticles> toString(@HeaderParam("value") String value) {
        try {
            IYvsBaseArticles impl = (IYvsBaseArticles) IEntitiSax.createInstance("IYvsBaseArticles", dao);
            if (impl != null) {
                return impl.toString(value);
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * BEGIN ARTICLE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_article")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticles> saveArticle(YvsBaseArticles entity) {
        try {
            IYvsBaseArticles impl = (IYvsBaseArticles) IEntitiSax.createInstance("IYvsBaseArticles", dao);
            if (impl != null) {
                ResultatAction result = rebuild(impl.save(entity));
                if (result != null ? result.isResult() : false) {
                    String repDest = getRemotePath() + "docArticle";
                    String repDestSVG = getLocalPath(entity.getFamille().getSociete()) + "docArticle";
                    if (asString(entity.getBytePhoto1())) {
                        //copie dans le dossier de l'application
                        copyImage(repDest, entity.getPhoto1(), entity.getBytePhoto1());
                        //copie dans le dossier de sauvegarde
                        copyImage(repDestSVG, entity.getPhoto1(), entity.getBytePhoto1());
                    }
                    if (asString(entity.getBytePhoto2())) {
                        //copie dans le dossier de l'application
                        copyImage(repDest, entity.getPhoto2(), entity.getBytePhoto2());
                        //copie dans le dossier de sauvegarde
                        copyImage(repDestSVG, entity.getPhoto2(), entity.getBytePhoto2());
                    }
                    if (asString(entity.getBytePhoto3())) {
                        //copie dans le dossier de l'application
                        copyImage(repDest, entity.getPhoto3(), entity.getBytePhoto3());
                        //copie dans le dossier de sauvegarde
                        copyImage(repDestSVG, entity.getPhoto3(), entity.getBytePhoto3());
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
    @Path("update_article")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticles> updateArticle(YvsBaseArticles entity) {
        try {
            IYvsBaseArticles impl = (IYvsBaseArticles) IEntitiSax.createInstance("IYvsBaseArticles", dao);
            if (impl != null) {
                ResultatAction result = rebuild(impl.update(entity));
                if (result != null ? result.isResult() : false) {
                    String repDest = getRemotePath() + "docArticle";
                    String repDestSVG = getLocalPath(entity.getFamille().getSociete()) + "docArticle";
                    if (asString(entity.getBytePhoto1())) {
                        //copie dans le dossier de l'application
                        copyImage(repDest, entity.getPhoto1(), entity.getBytePhoto1());
                        //copie dans le dossier de sauvegarde
                        copyImage(repDestSVG, entity.getPhoto1(), entity.getBytePhoto1());
                    }
                    if (asString(entity.getBytePhoto2())) {
                        //copie dans le dossier de l'application
                        copyImage(repDest, entity.getPhoto2(), entity.getBytePhoto2());
                        //copie dans le dossier de sauvegarde
                        copyImage(repDestSVG, entity.getPhoto2(), entity.getBytePhoto2());
                    }
                    if (asString(entity.getBytePhoto3())) {
                        //copie dans le dossier de l'application
                        copyImage(repDest, entity.getPhoto3(), entity.getBytePhoto3());
                        //copie dans le dossier de sauvegarde
                        copyImage(repDestSVG, entity.getPhoto3(), entity.getBytePhoto3());
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
    @Path("delete_article")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticles> deleteArticle(YvsBaseArticles entity) {
        try {
            IYvsBaseArticles impl = (IYvsBaseArticles) IEntitiSax.createInstance("IYvsBaseArticles", dao);
            if (impl != null) {
                ResultatAction result = rebuild(impl.delete(entity));
                if (result != null ? result.isResult() : false) {
                    String repDest = getRemotePath() + "docArticle";
                    String repDestSVG = getLocalPath(entity.getFamille().getSociete()) + "docArticle";
                    if (asString(entity.getPhoto1())) {
                        //supprimer dans le dossier de l'application
                        deleteImage(repDest, entity.getPhoto1());
                        //supprimer dans le dossier de sauvegarde
                        deleteImage(repDestSVG, entity.getPhoto1());
                    }
                    if (asString(entity.getPhoto2())) {
                        //supprimer dans le dossier de l'application
                        deleteImage(repDest, entity.getPhoto2());
                        //supprimer dans le dossier de sauvegarde
                        deleteImage(repDestSVG, entity.getPhoto2());
                    }
                    if (asString(entity.getPhoto3())) {
                        //supprimer dans le dossier de l'application
                        deleteImage(repDest, entity.getPhoto3());
                        //supprimer dans le dossier de sauvegarde
                        deleteImage(repDestSVG, entity.getPhoto3());
                    }
                }
                return result;
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END ARTICLE**/
    /**
     * BEGIN AVIS ARTICLE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_article_avis")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticlesAvis> saveArticleAvis(YvsBaseArticlesAvis entity) {
        try {
            IYvsBaseArticlesAvis impl = (IYvsBaseArticlesAvis) IEntitiSax.createInstance("IYvsBaseArticlesAvis", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_article_avis")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticlesAvis> updateArticleAvis(YvsBaseArticlesAvis entity) {
        try {
            IYvsBaseArticlesAvis impl = (IYvsBaseArticlesAvis) IEntitiSax.createInstance("IYvsBaseArticlesAvis", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_article_avis")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticlesAvis> deleteArticleAvis(YvsBaseArticlesAvis entity) {
        try {
            IYvsBaseArticlesAvis impl = (IYvsBaseArticlesAvis) IEntitiSax.createInstance("IYvsBaseArticlesAvis", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END AVIS ARTICLE**/
    /**
     * BEGIN GROUPE ARTICLE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_groupe_article")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseGroupesArticle> saveGroupeArticle(YvsBaseGroupesArticle entity) {
        try {
            IYvsBaseGroupesArticle impl = (IYvsBaseGroupesArticle) IEntitiSax.createInstance("IYvsBaseGroupeArticles", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_groupe_article")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseGroupesArticle> updateGroupeArticle(YvsBaseGroupesArticle entity) {
        try {
            IYvsBaseGroupesArticle impl = (IYvsBaseGroupesArticle) IEntitiSax.createInstance("IYvsBaseGroupeArticles", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_groupe_article")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseGroupesArticle> deleteGroupeArticle(YvsBaseGroupesArticle entity) {
        try {
            IYvsBaseGroupesArticle impl = (IYvsBaseGroupesArticle) IEntitiSax.createInstance("IYvsBaseGroupeArticles", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END GROUPE ARTICLE**/
    /**
     * BEGIN FAMILLE ARTICLE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_famille_article")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseFamilleArticle> saveFamilleArticle(YvsBaseFamilleArticle entity) {
        try {
            IYvsBaseFamilleArticle impl = (IYvsBaseFamilleArticle) IEntitiSax.createInstance("IYvsBaseFamilleArticle", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_famille_article")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseFamilleArticle> updateFamilleArticle(YvsBaseFamilleArticle entity) {
        try {
            IYvsBaseFamilleArticle impl = (IYvsBaseFamilleArticle) IEntitiSax.createInstance("IYvsBaseFamilleArticle", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_famille_article")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseFamilleArticle> deleteFamilleArticle(YvsBaseFamilleArticle entity) {
        try {
            IYvsBaseFamilleArticle impl = (IYvsBaseFamilleArticle) IEntitiSax.createInstance("IYvsBaseFamilleArticle", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END FAMILLE ARTICLE**/
    /**
     * BEGIN CLASSE STATISTIQUE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_classe_statistique")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseClassesStat> saveClasseStatistique(YvsBaseClassesStat entity) {
        try {
            IYvsBaseClassesStat impl = (IYvsBaseClassesStat) IEntitiSax.createInstance("IYvsBaseClassesStat", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_classe_statistique")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseClassesStat> updateClasseStatistique(YvsBaseClassesStat entity) {
        try {
            IYvsBaseClassesStat impl = (IYvsBaseClassesStat) IEntitiSax.createInstance("IYvsBaseClassesStat", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_classe_statistique")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseClassesStat> deleteClasseStatistique(YvsBaseClassesStat entity) {
        try {
            IYvsBaseClassesStat impl = (IYvsBaseClassesStat) IEntitiSax.createInstance("IYvsBaseClassesStat", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END CLASSE STATISTIQUE**/
    /*
     * BEGIN DEPOT
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_depot")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseDepots> saveDepot(YvsBaseDepots entity) {
        try {
            System.err.println("agence = " + entity.getAgence());
            IYvsBaseDepots impl = (IYvsBaseDepots) IEntitiSax.createInstance("IYvsBaseDepots", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_depot")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseDepots> updateDepot(YvsBaseDepots entity) {
        try {
            IYvsBaseDepots impl = (IYvsBaseDepots) IEntitiSax.createInstance("IYvsBaseDepots", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_depot")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseDepots> deleteDepots(YvsBaseDepots entity) {
        try {
            IYvsBaseDepots impl = (IYvsBaseDepots) IEntitiSax.createInstance("IYvsBaseDepots", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END DEPOT**/
    /*
     * BEGIN CODE BARRE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_code_barre")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleCodeBarre> saveCodeBarre(YvsBaseArticleCodeBarre entity) {
        try {
            IYvsBaseArticleCodeBarre impl = (IYvsBaseArticleCodeBarre) IEntitiSax.createInstance("IYvsBaseArticleCodeBarre", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_code_barre")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleCodeBarre> updateCodeBarre(YvsBaseArticleCodeBarre entity) {
        try {
            IYvsBaseArticleCodeBarre impl = (IYvsBaseArticleCodeBarre) IEntitiSax.createInstance("IYvsBaseArticleCodeBarre", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_code_barre")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleCodeBarre> deleteCodeBarre(YvsBaseArticleCodeBarre entity) {
        try {
            IYvsBaseArticleCodeBarre impl = (IYvsBaseArticleCodeBarre) IEntitiSax.createInstance("IYvsBaseArticleCodeBarre", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END CODE BARRE**/
    /*
     * BEGIN MODELE REFERENCE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_model_reference")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseModeleReference> saveModelReference(YvsBaseModeleReference entity) {
        try {
            IYvsBaseModeleReference impl = (IYvsBaseModeleReference) IEntitiSax.createInstance("IYvsBaseModeleReference", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_model_reference")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseModeleReference> updateModelReference(YvsBaseModeleReference entity) {
        try {
            IYvsBaseModeleReference impl = (IYvsBaseModeleReference) IEntitiSax.createInstance("IYvsBaseModeleReference", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_model_reference")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseModeleReference> deleteModelReference(YvsBaseModeleReference entity) {
        try {
            IYvsBaseModeleReference impl = (IYvsBaseModeleReference) IEntitiSax.createInstance("IYvsBaseModeleReference", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END MODELE REFERENCE**/
    /*
     * BEGIN UNITE MESURE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_unite_mesure")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseUniteMesure> saveUniteMesure(YvsBaseUniteMesure entity) {
        try {
            IYvsBaseUniteMesure impl = (IYvsBaseUniteMesure) IEntitiSax.createInstance("IYvsBaseUniteMesure", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_unite_mesure")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseUniteMesure> updateUniteMesure(YvsBaseUniteMesure entity) {
        try {
            IYvsBaseUniteMesure impl = (IYvsBaseUniteMesure) IEntitiSax.createInstance("IYvsBaseUniteMesure", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_unite_mesure")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseUniteMesure> deleteUniteMesure(YvsBaseUniteMesure entity) {
        try {
            IYvsBaseUniteMesure impl = (IYvsBaseUniteMesure) IEntitiSax.createInstance("IYvsBaseUniteMesure", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END MODELE REFERENCE**/
    /*
     * BEGIN USER
     *
     * @param entity
     * @return
     */

    @GET
    @Path("count_user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Long countUser(@HeaderParam("societe") String societe) {
        try {
            if (asNumeric(societe)) {
                return (Long) dao.loadObjectByNameQueries("YvsUsers.counAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @GET
    @Path("one_user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsUsers oneUser(@HeaderParam("id") String id) {
        try {
            if (asNumeric(id)) {
                return (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(id)});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("list_user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsUsers> listUser(@HeaderParam("societe") String societe) {
        try {
            if (asNumeric(societe)) {
                return dao.loadNameQueries("YvsUsers.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("save_user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsUsers> saveUSer(YvsUsers entity) {
        try {
            IYvsUsers impl = (IYvsUsers) IEntitiSax.createInstance("IYvsUsers", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsUsers> updateUser(YvsUsers entity) {
        try {
            IYvsUsers impl = (IYvsUsers) IEntitiSax.createInstance("IYvsUsers", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsUsers> deleteUser(YvsUsers entity) {
        try {
            IYvsUsers impl = (IYvsUsers) IEntitiSax.createInstance("IYvsUsers", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END USER**/
    /*
     * BEGIN NIVEAU ACCES
     *
     * @param entity
     * @return
     */
    @GET
    @Path("get_niveau_user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsNiveauUsers getNiveauUser(@HeaderParam("societe") String societe, @HeaderParam("user") String user) {
        try {
            if (asNumeric(societe) && asNumeric(user)) {
                return (YvsNiveauUsers) dao.loadOneByNameQueries("YvsNiveauUsers.findNivoUserInScte", new String[]{"societe", "user"}, new Object[]{new YvsSocietes(Long.valueOf(societe)), new YvsUsers(Long.valueOf(user))});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("one_niveau_acces")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public YvsNiveauAcces oneNiveauAcces(@HeaderParam("id") String id) {
        try {
            if (asNumeric(id)) {
                return (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauAcces.findById", new String[]{"id"}, new Object[]{Long.valueOf(id)});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("list_niveau_acces")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public List<YvsNiveauAcces> listNiveauAcces(@HeaderParam("societe") String societe) {
        try {
            if (asNumeric(societe)) {
                return dao.loadNameQueries("YvsNiveauAcces.findSociete", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("save_niveau_acces")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsNiveauAcces> saveNiveauAcces(YvsNiveauAcces entity) {
        try {
            IYvsNiveauAcces impl = (IYvsNiveauAcces) IEntitiSax.createInstance("IYvsNiveauAcces", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_niveau_acces")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsNiveauAcces> updateNiveauAcces(YvsNiveauAcces entity) {
        try {
            IYvsNiveauAcces impl = (IYvsNiveauAcces) IEntitiSax.createInstance("IYvsNiveauAcces", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_niveau_acces")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsNiveauAcces> deleteNiveauAcces(YvsNiveauAcces entity) {
        try {
            IYvsNiveauAcces impl = (IYvsNiveauAcces) IEntitiSax.createInstance("IYvsNiveauAcces", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END NIVEAU ACCES**/
    /*
     * BEGIN CLIENT
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_client")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComClient> saveClient(YvsComClient entity) {
        try {
            IYvsComClient impl = (IYvsComClient) IEntitiSax.createInstance("IYvsComClient", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_client")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComClient> updateClient(YvsComClient entity) {
        try {
            IYvsComClient impl = (IYvsComClient) IEntitiSax.createInstance("IYvsComClient", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_client")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComClient> deleteClient(YvsComClient entity) {
        try {
            IYvsComClient impl = (IYvsComClient) IEntitiSax.createInstance("IYvsComClient", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("find_client")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public YvsComClient findClient(@HeaderParam("noms") String noms, @HeaderParam("telephone") String telephone, @HeaderParam("societe") String societe) {
        try {
            if (!asNumeric(societe)) {
                return null;
            }
            List<YvsComClient> list = dao.loadNameQueries("YvsComClient.findByNomsPhone", new String[]{"noms", "phone", "societe"}, new Object[]{noms, telephone, new YvsSocietes(Long.valueOf(societe))}, 0, 1);
            if (list != null ? !list.isEmpty() : false) {
                return list.get(0);

            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END CLIENT**/
    /*
     * BEGIN CATEGORIE TARIFAIRE CLIENT
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_cat_tarifaire_client")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComCategorieTarifaire> saveCategorieTarifaireClient(YvsComCategorieTarifaire entity) {
        try {
            IYvsComCategorieTarifaire impl = (IYvsComCategorieTarifaire) IEntitiSax.createInstance("IYvsComCategorieTarifaire", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_cat_tarifaire_client")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComCategorieTarifaire> updateCategorieTarifaireClient(YvsComCategorieTarifaire entity) {
        try {
            IYvsComCategorieTarifaire impl = (IYvsComCategorieTarifaire) IEntitiSax.createInstance("IYvsComCategorieTarifaire", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_cat_tarifaire_client")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComCategorieTarifaire> deleteCategorieTarifaireClient(YvsComCategorieTarifaire entity) {
        try {
            IYvsComCategorieTarifaire impl = (IYvsComCategorieTarifaire) IEntitiSax.createInstance("IYvsComCategorieTarifaire", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END CATEGORIE TARIFAIRE CLIENT**/
    /*
     * BEGIN TIERS
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_tiers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseTiers> saveTiers(YvsBaseTiers entity) {
        try {
            IYvsBaseTiers impl = (IYvsBaseTiers) IEntitiSax.createInstance("IYvsBaseTiers", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_tiers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseTiers> updateTiers(YvsBaseTiers entity) {
        try {
            IYvsBaseTiers impl = (IYvsBaseTiers) IEntitiSax.createInstance("IYvsBaseTiers", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_tiers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseTiers> deleteTies(YvsBaseTiers entity) {
        try {
            IYvsBaseTiers impl = (IYvsBaseTiers) IEntitiSax.createInstance("IYvsBaseTiers", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END TIERS**/
    /*
     * BEGIN FOURNISSEURS
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_fournisseur")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseFournisseur> saveFournisseur(YvsBaseFournisseur entity) {
        try {
            IYvsBaseFournisseur impl = (IYvsBaseFournisseur) IEntitiSax.createInstance("IYvsBaseFournisseur", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_fournisseur")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseFournisseur> updateFournisseur(YvsBaseFournisseur entity) {
        try {
            IYvsBaseFournisseur impl = (IYvsBaseFournisseur) IEntitiSax.createInstance("IYvsBaseFournisseur", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_fournisseur")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseFournisseur> deleteFournisseur(YvsBaseFournisseur entity) {
        try {
            IYvsBaseFournisseur impl = (IYvsBaseFournisseur) IEntitiSax.createInstance("IYvsBaseFournisseur", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END FOURNISSEURS**/
    /*
     * BEGIN EXERCICE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_exercice")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseExercice> saveExcercice(YvsBaseExercice entity) {
        try {
            IYvsBaseExercice impl = (IYvsBaseExercice) IEntitiSax.createInstance("IYvsBaseExercice", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_exercice")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseExercice> updateExercice(YvsBaseExercice entity) {
        try {
            IYvsBaseExercice impl = (IYvsBaseExercice) IEntitiSax.createInstance("IYvsBaseExercice", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_exercice")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseExercice> deleteExercice(YvsBaseExercice entity) {
        try {
            IYvsBaseExercice impl = (IYvsBaseExercice) IEntitiSax.createInstance("IYvsBaseExercice", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END EXERCICE**/
    /*
     * BEGIN ARTICLE FOURNISSEUR
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_article_fournisseur")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleFournisseur> saveArticleFournisseur(YvsBaseArticleFournisseur entity) {
        try {
            IYvsBaseArticleFournisseur impl = (IYvsBaseArticleFournisseur) IEntitiSax.createInstance("IYvsBaseArticleFournisseur", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_article_fournisseur")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleFournisseur> updateArticleFournisseur(YvsBaseArticleFournisseur entity) {
        try {
            IYvsBaseArticleFournisseur impl = (IYvsBaseArticleFournisseur) IEntitiSax.createInstance("IYvsBaseArticleFournisseur", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_article_fournisseur")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleFournisseur> deleteArticleFournisseur(YvsBaseArticleFournisseur entity) {
        try {
            IYvsBaseArticleFournisseur impl = (IYvsBaseArticleFournisseur) IEntitiSax.createInstance("IYvsBaseArticleFournisseur", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END ARTICLE FOURNISSEUR**/
    /*
     * BEGIN PLAN COMPTABLE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_plan_comptable")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBasePlanComptable> savePlanComptable(YvsBasePlanComptable entity) {
        try {
            IYvsBasePlanComptable impl = (IYvsBasePlanComptable) IEntitiSax.createInstance("IYvsBasePlanComptable", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_plan_comptable")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBasePlanComptable> updatePlanComptable(YvsBasePlanComptable entity) {
        try {
            IYvsBasePlanComptable impl = (IYvsBasePlanComptable) IEntitiSax.createInstance("IYvsBasePlanComptable", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_plan_comptable")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBasePlanComptable> deletePlanComptable(YvsBasePlanComptable entity) {
        try {
            IYvsBasePlanComptable impl = (IYvsBasePlanComptable) IEntitiSax.createInstance("IYvsBasePlanComptable", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END PLAN COMPTABLE**/
    /*
     * BEGIN CAISSE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_caisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseCaisse> saveCaisse(YvsBaseCaisse entity) {
        try {
            IYvsBaseCaisse impl = (IYvsBaseCaisse) IEntitiSax.createInstance("IYvsBaseCaisse", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_caisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseCaisse> updateCaisse(YvsBaseCaisse entity) {
        try {
            IYvsBaseCaisse impl = (IYvsBaseCaisse) IEntitiSax.createInstance("IYvsBaseCaisse", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_caisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseCaisse> deleteCaisse(YvsBaseCaisse entity) {
        try {
            IYvsBaseCaisse impl = (IYvsBaseCaisse) IEntitiSax.createInstance("IYvsBaseCaisse", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END CAISSE**/
    /*
     * BEGIN JOURNAUX
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_journaux")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaJournaux> saveJournaux(YvsComptaJournaux entity) {
        try {
            IYvsComptaJournaux impl = (IYvsComptaJournaux) IEntitiSax.createInstance("IYvsComptaJournaux", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_journaux")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaJournaux> updateJournaux(YvsComptaJournaux entity) {
        try {
            IYvsComptaJournaux impl = (IYvsComptaJournaux) IEntitiSax.createInstance("IYvsComptaJournaux", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_journaux")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaJournaux> deleteJournaux(YvsComptaJournaux entity) {
        try {
            IYvsComptaJournaux impl = (IYvsComptaJournaux) IEntitiSax.createInstance("IYvsComptaJournaux", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END JOURNAUX**/
    /*
     * BEGIN PLAN ANALYTIQUE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_plan_analytique")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaPlanAnalytique> savePlanAnalytique(YvsComptaPlanAnalytique entity) {
        try {
            IYvsComptaPlanAnalytique impl = (IYvsComptaPlanAnalytique) IEntitiSax.createInstance("IYvsComptaPlanAnalytique", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_plan_analytique")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaPlanAnalytique> updatePlanAnalytique(YvsComptaPlanAnalytique entity) {
        try {
            IYvsComptaPlanAnalytique impl = (IYvsComptaPlanAnalytique) IEntitiSax.createInstance("IYvsComptaPlanAnalytique", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_plan_analytique")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComptaPlanAnalytique> deletePlanAnalytique(YvsComptaPlanAnalytique entity) {
        try {
            IYvsComptaPlanAnalytique impl = (IYvsComptaPlanAnalytique) IEntitiSax.createInstance("IYvsComptaPlanAnalytique", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END PLAN ANALYTIQUE**/
    /*
     * BEGIN DICTIONNAIRE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_dictionnaire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsDictionnaire> saveDictionnaire(YvsDictionnaire entity) {
        try {
            IYvsDictionnaire impl = (IYvsDictionnaire) IEntitiSax.createInstance("IYvsDictionnaire", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_dictionnaire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsDictionnaire> updateDictionnaire(YvsDictionnaire entity) {
        try {
            IYvsDictionnaire impl = (IYvsDictionnaire) IEntitiSax.createInstance("IYvsDictionnaire", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_dictionnaire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsDictionnaire> deleteDictionnaire(YvsDictionnaire entity) {
        try {
            IYvsDictionnaire impl = (IYvsDictionnaire) IEntitiSax.createInstance("IYvsDictionnaire", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END PLAN ANALYTIQUE**/
    /*
     * BEGIN PARAMETRE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_parametre")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseParametre> saveParametre(YvsBaseParametre entity) {
        try {
            IYvsBaseParametre impl = (IYvsBaseParametre) IEntitiSax.createInstance("IYvsBaseParametre", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_parametre")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseParametre> updateParametre(YvsBaseParametre entity) {
        try {
            IYvsBaseParametre impl = (IYvsBaseParametre) IEntitiSax.createInstance("IYvsBaseParametre", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_parametre")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseParametre> deleteParametre(YvsBaseParametre entity) {
        try {
            IYvsBaseParametre impl = (IYvsBaseParametre) IEntitiSax.createInstance("IYvsBaseParametre", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END PARAMETRE**/
    /*
     * BEGIN CATEGORIE PERSONNEL
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_categorie_personnel")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComCategoriePersonnel> saveCategoriePersonnel(YvsComCategoriePersonnel entity) {
        try {
            IYvsComCategoriePersonnel impl = (IYvsComCategoriePersonnel) IEntitiSax.createInstance("IYvsComCategoriePersonnel", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_categorie_personnel")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComCategoriePersonnel> updateCategoriePersonnel(YvsComCategoriePersonnel entity) {
        try {
            IYvsComCategoriePersonnel impl = (IYvsComCategoriePersonnel) IEntitiSax.createInstance("IYvsComCategoriePersonnel", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_categorie_personnel")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComCategoriePersonnel> deleteCategoriePersonnel(YvsComCategoriePersonnel entity) {
        try {
            IYvsComCategoriePersonnel impl = (IYvsComCategoriePersonnel) IEntitiSax.createInstance("IYvsComCategoriePersonnel", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END  CATEGORIE PERSONNEL**/
    /*
     * BEGIN TRANCHE HORAIRE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_tranche_horaire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsGrhTrancheHoraire> saveTrancheHoraire(YvsGrhTrancheHoraire entity) {
        try {
            IYvsGrhTrancheHoraire impl = (IYvsGrhTrancheHoraire) IEntitiSax.createInstance("IYvsGrhTrancheHoraire", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_tranche_horaire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsGrhTrancheHoraire> updateTrancheHoraire(YvsGrhTrancheHoraire entity) {
        try {
            IYvsGrhTrancheHoraire impl = (IYvsGrhTrancheHoraire) IEntitiSax.createInstance("IYvsGrhTrancheHoraire", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_tranche_horaire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsGrhTrancheHoraire> deleteTrancheHoraire(YvsGrhTrancheHoraire entity) {
        try {
            IYvsGrhTrancheHoraire impl = (IYvsGrhTrancheHoraire) IEntitiSax.createInstance("IYvsGrhTrancheHoraire", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END  TRANCHE HORAIRE**/
    /*
     * BEGIN MODE REGLEMENT
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_mode_reglement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseModeReglement> saveModeReglement(YvsBaseModeReglement entity) {
        try {
            IYvsBaseModeReglement impl = (IYvsBaseModeReglement) IEntitiSax.createInstance("IYvsBaseModeReglement", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_mode_reglement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseModeReglement> updateModeReglement(YvsBaseModeReglement entity) {
        try {
            IYvsBaseModeReglement impl = (IYvsBaseModeReglement) IEntitiSax.createInstance("IYvsBaseModeReglement", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_mode_reglement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseModeReglement> deleteModeReglement(YvsBaseModeReglement entity) {
        try {
            IYvsBaseModeReglement impl = (IYvsBaseModeReglement) IEntitiSax.createInstance("IYvsBaseModeReglement", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END  MODE REGLEMENT**/

    /*
     * BEGIN TYPE DE COUT
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_type_cout")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsGrhTypeCout> saveTypeCout(YvsGrhTypeCout entity) {
        try {
            IYvsGrhTypeCout impl = (IYvsGrhTypeCout) IEntitiSax.createInstance("IYvsGrhTypeCout", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_type_cout")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsGrhTypeCout> updateTypeCout(YvsGrhTypeCout entity) {
        try {
            IYvsGrhTypeCout impl = (IYvsGrhTypeCout) IEntitiSax.createInstance("IYvsGrhTypeCout", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_type_cout")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsGrhTypeCout> deleteTypeCout(YvsGrhTypeCout entity) {
        try {
            IYvsGrhTypeCout impl = (IYvsGrhTypeCout) IEntitiSax.createInstance("IYvsGrhTypeCout", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END  TYPE DE COUT**/
    /*
     * BEGIN TAXES
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_taxe")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseTaxes> saveTaxe(YvsBaseTaxes entity) {
        try {
            IYvsBaseTaxes impl = (IYvsBaseTaxes) IEntitiSax.createInstance("IYvsBaseTaxes", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_taxe")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseTaxes> updateTaxe(YvsBaseTaxes entity) {
        try {
            IYvsBaseTaxes impl = (IYvsBaseTaxes) IEntitiSax.createInstance("IYvsBaseTaxes", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_taxe")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseTaxes> deleteTaxe(YvsBaseTaxes entity) {
        try {
            IYvsBaseTaxes impl = (IYvsBaseTaxes) IEntitiSax.createInstance("IYvsBaseTaxes", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END TAXES**/
    /*
     * BEGIN NATURE COMPTE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_nature_compte")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseNatureCompte> saveNatureCompte(YvsBaseNatureCompte entity) {
        try {
            IYvsBaseNatureCompte impl = (IYvsBaseNatureCompte) IEntitiSax.createInstance("IYvsBaseNatureCompte", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_nature_compte")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseNatureCompte> updateNatureCompte(YvsBaseNatureCompte entity) {
        try {
            IYvsBaseNatureCompte impl = (IYvsBaseNatureCompte) IEntitiSax.createInstance("IYvsBaseNatureCompte", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_nature_compte")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseNatureCompte> deleteNatureCompte(YvsBaseNatureCompte entity) {
        try {
            IYvsBaseNatureCompte impl = (IYvsBaseNatureCompte) IEntitiSax.createInstance("IYvsBaseNatureCompte", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END NATURE COMPTE**/
    /*
     * BEGIN CONDITIONNEMENT
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_conditionnement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseConditionnement> saveConditionnement(YvsBaseConditionnement entity) {
        try {
            IYvsBaseConditionnement impl = (IYvsBaseConditionnement) IEntitiSax.createInstance("IYvsBaseConditionnement", dao);
            if (impl != null) {
                ResultatAction result = rebuild(impl.save(entity));
                if (result != null ? result.isResult() : false) {
                    String repDest = getRemotePath() + "docArticle";
                    String repDestSVG = getLocalPath(entity.getUnite().getSociete()) + "docArticle";
                    if (asString(entity.getBytePhoto())) {
                        //copie dans le dossier de l'application
                        copyImage(repDest, entity.getPhoto(), entity.getBytePhoto());
                        //copie dans le dossier de sauvegarde
                        copyImage(repDestSVG, entity.getPhoto(), entity.getBytePhoto());
                    }
                }
                return result;
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_conditionnement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseConditionnement> updateConditionnement(YvsBaseConditionnement entity) {
        try {
            IYvsBaseConditionnement impl = (IYvsBaseConditionnement) IEntitiSax.createInstance("IYvsBaseConditionnement", dao);
            if (impl != null) {
                ResultatAction result = rebuild(impl.update(entity));
                if (result != null ? result.isResult() : false) {
                    String repDest = getRemotePath() + "docArticle";
                    String repDestSVG = getLocalPath(entity.getUnite().getSociete()) + "docArticle";
                    if (asString(entity.getBytePhoto())) {
                        //copie dans le dossier de l'application
                        copyImage(repDest, entity.getPhoto(), entity.getBytePhoto());
                        //copie dans le dossier de sauvegarde
                        copyImage(repDestSVG, entity.getPhoto(), entity.getBytePhoto());
                    }
                }
                return result;
            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_conditionnement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseConditionnement> deleteConditionnement(YvsBaseConditionnement entity) {
        try {
            IYvsBaseConditionnement impl = (IYvsBaseConditionnement) IEntitiSax.createInstance("IYvsBaseConditionnement", dao);
            if (impl != null) {
                ResultatAction result = rebuild(impl.delete(entity));
                if (result != null ? result.isResult() : false) {
                    String repDest = getRemotePath() + "docArticle";
                    String repDestSVG = getLocalPath(entity.getUnite().getSociete()) + "docArticle";
                    if (asString(entity.getPhoto())) {
                        //supprimer dans le dossier de l'application
                        deleteImage(repDest, entity.getPhoto());
                        //supprimer dans le dossier de sauvegarde
                        deleteImage(repDestSVG, entity.getPhoto());
                    }
                }
                return result;
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END CONDITIONNEMENT**/
    /*
     * BEGIN ARTICLE DEPOT
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_article_depot")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleDepot> saveArticleDepot(YvsBaseArticleDepot entity) {
        try {
            IYvsBaseArticleDepot impl = (IYvsBaseArticleDepot) IEntitiSax.createInstance("IYvsBaseArticleDepot", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_article_depot")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleDepot> updateArticleDepot(YvsBaseArticleDepot entity) {
        try {
            IYvsBaseArticleDepot impl = (IYvsBaseArticleDepot) IEntitiSax.createInstance("IYvsBaseArticleDepot", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_article_depot")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleDepot> deleteArticleDepot(YvsBaseArticleDepot entity) {
        try {
            IYvsBaseArticleDepot impl = (IYvsBaseArticleDepot) IEntitiSax.createInstance("IYvsBaseArticleDepot", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END ARTICLE DEPOT**/
    /*
     * BEGIN ARTICLE PACK
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_article_pack")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticlePack> saveArticlePack(YvsBaseArticlePack entity) {
        try {
            IYvsBaseArticlePack impl = (IYvsBaseArticlePack) IEntitiSax.createInstance("IYvsBaseArticlePack", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_article_pack")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticlePack> updateArticlePack(YvsBaseArticlePack entity) {
        try {
            IYvsBaseArticlePack impl = (IYvsBaseArticlePack) IEntitiSax.createInstance("IYvsBaseArticlePack", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_article_pack")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticlePack> deleteArticlePack(YvsBaseArticlePack entity) {
        try {
            IYvsBaseArticlePack impl = (IYvsBaseArticlePack) IEntitiSax.createInstance("IYvsBaseArticlePack", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END ARTICLE PACK**/
    /*
     * BEGIN ARTICLE ANALYTIQUE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_article_analytique")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleAnalytique> saveArticleAnalytique(YvsBaseArticleAnalytique entity) {
        try {
            IYvsBaseArticleAnalytique impl = (IYvsBaseArticleAnalytique) IEntitiSax.createInstance("IYvsBaseArticleAnalytique", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_article_analytique")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleAnalytique> updateArticleAnalytique(YvsBaseArticleAnalytique entity) {
        try {
            IYvsBaseArticleAnalytique impl = (IYvsBaseArticleAnalytique) IEntitiSax.createInstance("IYvsBaseArticleAnalytique", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_article_analytique")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleAnalytique> deleteArticleAnalytique(YvsBaseArticleAnalytique entity) {
        try {
            IYvsBaseArticleAnalytique impl = (IYvsBaseArticleAnalytique) IEntitiSax.createInstance("IYvsBaseArticleAnalytique", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END ARTICLE ANALYTIQUE**/
    /*
     * BEGIN ARTICLE CATEGORIE COMPTABLE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_article_categorie_comptable")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleCategorieComptable> saveArticleCategorieComptable(YvsBaseArticleCategorieComptable entity) {
        try {
            IYvsBaseArticleCategorieComptable impl = (IYvsBaseArticleCategorieComptable) IEntitiSax.createInstance("IYvsBaseArticleCategorieComptable", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_article_categorie_comptable")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleCategorieComptable> updateArticleCategorieComptable(YvsBaseArticleCategorieComptable entity) {
        try {
            IYvsBaseArticleCategorieComptable impl = (IYvsBaseArticleCategorieComptable) IEntitiSax.createInstance("IYvsBaseArticleCategorieComptable", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_article_categorie_comptable")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleCategorieComptable> deleteArticleCategorieComptable(YvsBaseArticleCategorieComptable entity) {
        try {
            IYvsBaseArticleCategorieComptable impl = (IYvsBaseArticleCategorieComptable) IEntitiSax.createInstance("IYvsBaseArticleCategorieComptable", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END ARTICLE CATEGORIE COMPTABLE**/
    /*
     * BEGIN ARTICLE DESCRIPTION
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_article_description")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleDescription> saveArticleDescription(YvsBaseArticleDescription entity) {
        try {
            IYvsBaseArticleDescription impl = (IYvsBaseArticleDescription) IEntitiSax.createInstance("IYvsBaseArticleDescription", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_article_description")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleDescription> updateArticleDescription(YvsBaseArticleDescription entity) {
        try {
            IYvsBaseArticleDescription impl = (IYvsBaseArticleDescription) IEntitiSax.createInstance("IYvsBaseArticleDescription", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_article_description")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleDescription> deleteArticleDescription(YvsBaseArticleDescription entity) {
        try {
            IYvsBaseArticleDescription impl = (IYvsBaseArticleDescription) IEntitiSax.createInstance("IYvsBaseArticleDescription", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END ARTICLE DESCRIPTION**/
    /*
     * BEGIN ARTICLE EQUIVALENT
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_article_equivalent")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleEquivalent> saveArticleEquivalent(YvsBaseArticleEquivalent entity) {
        try {
            IYvsBaseArticleEquivalent impl = (IYvsBaseArticleEquivalent) IEntitiSax.createInstance("IYvsBaseArticleEquivalent", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_article_equivalent")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleEquivalent> updateArticleEquivalent(YvsBaseArticleEquivalent entity) {
        try {
            IYvsBaseArticleEquivalent impl = (IYvsBaseArticleEquivalent) IEntitiSax.createInstance("IYvsBaseArticleEquivalent", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_article_equivalent")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleEquivalent> deleteArticleEquivalent(YvsBaseArticleEquivalent entity) {
        try {
            IYvsBaseArticleEquivalent impl = (IYvsBaseArticleEquivalent) IEntitiSax.createInstance("IYvsBaseArticleEquivalent", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END ARTICLE EQUIVALENT**/
    /*
     * BEGIN ARTICLE SUBSTITUTION
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_article_substitution")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleSubstitution> saveArticleSubstitution(YvsBaseArticleSubstitution entity) {
        try {
            IYvsBaseArticleSubstitution impl = (IYvsBaseArticleSubstitution) IEntitiSax.createInstance("IYvsBaseArticleSubstitution", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_article_substitution")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleSubstitution> updateArticleSubstitution(YvsBaseArticleSubstitution entity) {
        try {
            IYvsBaseArticleSubstitution impl = (IYvsBaseArticleSubstitution) IEntitiSax.createInstance("IYvsBaseArticleSubstitution", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_article_substitution")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticleSubstitution> deleteArticleSubstitution(YvsBaseArticleSubstitution entity) {
        try {
            IYvsBaseArticleSubstitution impl = (IYvsBaseArticleSubstitution) IEntitiSax.createInstance("IYvsBaseArticleSubstitution", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END ARTICLE SUBSTITUTION**/
    /*
     * BEGIN PLAN TARRIFAIRE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_plan_tarrifaire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBasePlanTarifaire> savePlanTarrifaire(YvsBasePlanTarifaire entity) {
        try {
            IYvsBasePlanTarifaire impl = (IYvsBasePlanTarifaire) IEntitiSax.createInstance("IYvsBasePlanTarifaire", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_plan_tarrifaire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBasePlanTarifaire> updatePlanTarrifaire(YvsBasePlanTarifaire entity) {
        try {
            IYvsBasePlanTarifaire impl = (IYvsBasePlanTarifaire) IEntitiSax.createInstance("IYvsBasePlanTarifaire", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_plan_tarrifaire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBasePlanTarifaire> deletePlanTarrifaire(YvsBasePlanTarifaire entity) {
        try {
            IYvsBasePlanTarifaire impl = (IYvsBasePlanTarifaire) IEntitiSax.createInstance("IYvsBasePlanTarifaire", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END PLAN TARRIFAIRE**/
    /*
     * BEGIN EMPLACEMENT DEPOT
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_emplacement_depot")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseEmplacementDepot> saveEmplacementDepot(YvsBaseEmplacementDepot entity) {
        try {
            IYvsBaseEmplacementDepot impl = (IYvsBaseEmplacementDepot) IEntitiSax.createInstance("IYvsBaseEmplacementDepot", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_emplacement_depot")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseEmplacementDepot> updateEmplacementDepot(YvsBaseEmplacementDepot entity) {
        try {
            IYvsBaseEmplacementDepot impl = (IYvsBaseEmplacementDepot) IEntitiSax.createInstance("IYvsBaseEmplacementDepot", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_emplacement_depot")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseEmplacementDepot> deleteEmplacementDepot(YvsBaseEmplacementDepot entity) {
        try {
            IYvsBaseEmplacementDepot impl = (IYvsBaseEmplacementDepot) IEntitiSax.createInstance("IYvsBaseEmplacementDepot", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END EMPLACEMENTS DEPOT**/
    /*
     * BEGIN LIAISON DEPOT
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_liaison_depot")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComLiaisonDepot> saveLiaisonDepot(YvsComLiaisonDepot entity) {
        try {
            IYvsComLiaisonDepot impl = (IYvsComLiaisonDepot) IEntitiSax.createInstance("IYvsComLiaisonDepot", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_liaison_depot")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComLiaisonDepot> updateLiaisonDepot(YvsComLiaisonDepot entity) {
        try {
            IYvsComLiaisonDepot impl = (IYvsComLiaisonDepot) IEntitiSax.createInstance("IYvsComLiaisonDepot", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_liaison_depot")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsComLiaisonDepot> deleteLiaisonDepot(YvsComLiaisonDepot entity) {
        try {
            IYvsComLiaisonDepot impl = (IYvsComLiaisonDepot) IEntitiSax.createInstance("IYvsComLiaisonDepot", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END LIAISON DEPOT**/
    /*
     * BEGIN LIAISON DEPOT POINT
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_liaison_depot_point")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBasePointVenteDepot> saveLiaisonDepotPoint(YvsBasePointVenteDepot entity) {
        try {
            IYvsBasePointVenteDepot impl = (IYvsBasePointVenteDepot) IEntitiSax.createInstance("IYvsBasePointVenteDepot", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_liaison_depot_point")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBasePointVenteDepot> updateLiaisonDepotPoint(YvsBasePointVenteDepot entity) {
        try {
            IYvsBasePointVenteDepot impl = (IYvsBasePointVenteDepot) IEntitiSax.createInstance("IYvsBasePointVenteDepot", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_liaison_depot_point")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBasePointVenteDepot> deleteLiaisonDepotPoint(YvsBasePointVenteDepot entity) {
        try {
            IYvsBasePointVenteDepot impl = (IYvsBasePointVenteDepot) IEntitiSax.createInstance("IYvsBasePointVenteDepot", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END LIAISON DEPOT POINT**/
    /*
     * BEGIN LIAISON CAISSE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_liaison_caisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseLiaisonCaisse> saveLiaisonCaisse(YvsBaseLiaisonCaisse entity) {
        try {
            IYvsBaseLiaisonCaisse impl = (IYvsBaseLiaisonCaisse) IEntitiSax.createInstance("IYvsBaseLiaisonCaisse", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_liaison_caisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseLiaisonCaisse> updateLiaisonCaisse(YvsBaseLiaisonCaisse entity) {
        try {
            IYvsBaseLiaisonCaisse impl = (IYvsBaseLiaisonCaisse) IEntitiSax.createInstance("IYvsBaseLiaisonCaisse", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_liaison_caisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseLiaisonCaisse> deleteLiaisonCaisse(YvsBaseLiaisonCaisse entity) {
        try {
            IYvsBaseLiaisonCaisse impl = (IYvsBaseLiaisonCaisse) IEntitiSax.createInstance("IYvsBaseLiaisonCaisse", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END LIAISON CAISSE**/
    /*
     * BEGIN COMPTE CAISSE
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_compte_caisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseComptesCaisse> saveCompteCaisse(YvsBaseComptesCaisse entity) {
        try {
            IYvsBaseComptesCaisse impl = (IYvsBaseComptesCaisse) IEntitiSax.createInstance("IYvsBaseComptesCaisse", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_compte_caisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseComptesCaisse> updateCompteCaisse(YvsBaseComptesCaisse entity) {
        try {
            IYvsBaseComptesCaisse impl = (IYvsBaseComptesCaisse) IEntitiSax.createInstance("IYvsBaseComptesCaisse", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_compte_caisse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseComptesCaisse> deleteCompteCaisse(YvsBaseComptesCaisse entity) {
        try {
            IYvsBaseComptesCaisse impl = (IYvsBaseComptesCaisse) IEntitiSax.createInstance("IYvsBaseComptesCaisse", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END COMPTE CAISSE**/
    /*
     * BEGIN CAISSE USER
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_caisse_user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseCaisseUser> saveCaisseUser(YvsBaseCaisseUser entity) {
        try {
            System.err.println("caisse = " + entity.getIdCaisse());
            IYvsBaseCaisseUser impl = (IYvsBaseCaisseUser) IEntitiSax.createInstance("IYvsBaseCaisseUser", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_caisse_user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseCaisseUser> updateCaisseUser(YvsBaseCaisseUser entity) {
        try {
            IYvsBaseCaisseUser impl = (IYvsBaseCaisseUser) IEntitiSax.createInstance("IYvsBaseCaisseUser", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_caisse_user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseCaisseUser> deleteCaisseUser(YvsBaseCaisseUser entity) {
        try {
            IYvsBaseCaisseUser impl = (IYvsBaseCaisseUser) IEntitiSax.createInstance("IYvsBaseCaisseUser", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END CAISSE USER**/
    /*
     * BEGIN ARTICLE POINT
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_article_point")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticlePoint> saveArticlePoint(YvsBaseArticlePoint entity) {
        try {
            IYvsBaseArticlePoint impl = (IYvsBaseArticlePoint) IEntitiSax.createInstance("IYvsBaseArticlePoint", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_article_point")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticlePoint> updateArticlePoint(YvsBaseArticlePoint entity) {
        try {
            IYvsBaseArticlePoint impl = (IYvsBaseArticlePoint) IEntitiSax.createInstance("IYvsBaseArticlePoint", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_article_point")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseArticlePoint> deleteArticlePOint(YvsBaseArticlePoint entity) {
        try {
            IYvsBaseArticlePoint impl = (IYvsBaseArticlePoint) IEntitiSax.createInstance("IYvsBaseArticlePoint", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END ARTICLE POINT**/
    /*
     * BEGIN TYPE DOC
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_type_doc")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseTypeDocDivers> saveTypeDoc(YvsBaseTypeDocDivers entity) {
        try {
            IYvsBaseTypeDocDivers impl = (IYvsBaseTypeDocDivers) IEntitiSax.createInstance("IYvsBaseTypeDocDivers", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_type_doc")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseTypeDocDivers> updateTypeDoc(YvsBaseTypeDocDivers entity) {
        try {
            IYvsBaseTypeDocDivers impl = (IYvsBaseTypeDocDivers) IEntitiSax.createInstance("IYvsBaseTypeDocDivers", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_type_doc")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBaseTypeDocDivers> deleteTypeDoc(YvsBaseTypeDocDivers entity) {
        try {
            IYvsBaseTypeDocDivers impl = (IYvsBaseTypeDocDivers) IEntitiSax.createInstance("IYvsBaseTypeDocDivers", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END TYPE DOC**/
    /*
     * BEGIN POINT LIVRAISON
     *
     * @param entity
     * @return
     */
    @POST
    @Path("save_point_livraison")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBasePointLivraison> savePointLivraison(YvsBasePointLivraison entity) {
        try {
            IYvsBasePointLivraison impl = (IYvsBasePointLivraison) IEntitiSax.createInstance("IYvsBasePointLivraison", dao);
            if (impl != null) {
                return rebuild(impl.save(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("update_point_livraison")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBasePointLivraison> updatePointLivraison(YvsBasePointLivraison entity) {
        try {
            IYvsBasePointLivraison impl = (IYvsBasePointLivraison) IEntitiSax.createInstance("IYvsBasePointLivraison", dao);
            if (impl != null) {
                return rebuild(impl.update(entity));

            }
        } catch (Exception ex) {

            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("delete_point_livraison")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultatAction<YvsBasePointLivraison> deletePointLivraison(YvsBasePointLivraison entity) {
        try {
            IYvsBasePointLivraison impl = (IYvsBasePointLivraison) IEntitiSax.createInstance("IYvsBasePointLivraison", dao);
            if (impl != null) {
                return rebuild(impl.delete(entity));

            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*END POINT LIVRAISON**/
    @POST
    @Path("fusionner_article")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean fusionner(@HeaderParam("chaineSelectArt") String chaineSelectArt, @HeaderParam("fusioner") String fusioner, @HeaderParam("niveau") String niveau) {
        try {
            if (niveau != null ? niveau.trim().length() > 0 : false) {
                currentNiveau = (YvsNiveauUsers) dao.loadOneByNameQueries("YvsNiveauUsers.findById", new String[]{"id"}, new Object[]{Long.valueOf(niveau)});
            }
//            if (!autoriser("base_user_fusion")) {
//                return false;
//            }
            boolean fusionne = fusioner.equals("true") ? true : false;
            String fusionneTo = "";
            List<YvsBaseArticles> listArticle = new ArrayList<>();
            List<String> fusionnesBy = new ArrayList<>();
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(chaineSelectArt);
            if (!ids.isEmpty()) {
                for (int i : ids) {
                    YvsBaseArticles a = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{i});
                    if (a != null ? a.getId() > 0 : false) {
                        listArticle.add(a);
                    }
                }
            }
            System.err.println("list article  = " + listArticle.size());
            if (!listArticle.isEmpty()) {
                long newValue = ids.get(0);
                if (!fusionne) {
                    String oldValue = "0";
                    for (int i : ids) {
                        if (i != newValue) {
                            oldValue += "," + i;
                        }
                    }
                    boolean b = dao.fusionneData("yvs_base_articles", newValue, oldValue);
                    if (b) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                listArticle.remove(new YvsBaseArticles(id));
                            }
                        }
                    }
                    chaineSelectArt = "";
                    //Delete les doublons de article_depot
                    List<YvsBaseArticleDepot> idsDel = new ArrayList<>();
                    List<YvsBaseDepots> listIn = new ArrayList<>();
                    List<YvsBaseArticleDepot> liste = dao.loadNameQueries("YvsBaseArticleDepot.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(Long.valueOf(ids.get(0)))});
                    for (YvsBaseArticleDepot ad : liste) {
                        if (!listIn.contains(ad.getDepot())) {
                            listIn.add(ad.getDepot());
                        } else {
                            idsDel.add(ad);
                        }
                    }
                    for (YvsBaseArticleDepot ad : idsDel) {
                        dao.delete(ad);
                    }
                    return true;
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        YvsBaseArticles a = new YvsBaseArticles(Long.valueOf(idx));
                        fusionneTo = a.getDesignation();
                    } else {
                        YvsBaseArticles c = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                            fusionneTo = c.getDesignation();
                        }
                    }
                    YvsBaseArticles c;
                    for (int i : ids) {
                        long oldValue = i;
                        if (i > -1) {
                            if (oldValue != newValue) {
                                YvsBaseArticles a = new YvsBaseArticles(Long.valueOf(i));
                                fusionnesBy.add(a.getDesignation());
                            }
                        } else {
                            c = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                                fusionnesBy.add(c.getDesignation());
                            }
                        }
                    }
                }
            } else {
                return false;
            }

        } catch (Exception e) {
            System.err.println("erreur = " + e.getMessage());
            e.printStackTrace();
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
            } catch (Exception ex) {
                selection = "";
                for (String numroLine1 : numroLine) {
                    if (!l.contains(numroLine1)) {
                        selection += numroLine1 + "-";      //mémorise la selection
                    }
                }
                System.err.println("Impossible de terminer cette opération ,des élément de votre sélection doivent être encore en liaison");
            }
        }
        return re;
    }

    @GET
    @Path("save_author")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Long saveAuthor(@HeaderParam("agence") String agence, @HeaderParam("user") String user) {
        try {
            if (agence != null && user != null) {
                Long idAgence = Long.valueOf(agence);
                Long idUser = Long.valueOf(user);
                YvsUsersAgence ua = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findByUsersAgence", new String[]{"user", "agence"}, new Object[]{new YvsUsers(idUser), new YvsAgences(idAgence)});
                if (ua == null) {
                    ua = new YvsUsersAgence();
                    ua.setAgence(new YvsAgences(idAgence));
                    ua.setUsers(new YvsUsers(idUser));
                    ua.setDateUpdate(new Date());
                    ua.setDateSave(new Date());
                    ua = (YvsUsersAgence) dao.save1(ua);
                    return ua.getId();
                }
                return ua.getId();
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericResource.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
