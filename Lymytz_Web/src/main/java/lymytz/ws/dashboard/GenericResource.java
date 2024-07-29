/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws.dashboard;

import java.text.ParseException;
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
import yvs.dao.Options;
import yvs.entity.base.YvsBaseArticlePoint;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseConditionnementPoint;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsBasePlanTarifaire;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Path("services/dashboard")
@RequestScoped
public class GenericResource extends lymytz.ws.GenericResource {

    @POST
    @Path("getTableauBordPoint")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    public List<Object[]> getTableauBordPoint(@HeaderParam("agence_") String agence_, @HeaderParam("point_") String point_, @HeaderParam("date_debut_") String date_debut_, @HeaderParam("date_fin_") String date_fin_, @HeaderParam("periode_") String periode_
    ) {
        try {
            Date debut = df.parse(date_debut_);
            Date fin = df.parse(date_fin_);
            String rq = "SELECT * FROM et_total_one_pt_vente(?,?,?,?,?)";
            return dao.loadDataByNativeQuery(rq, new Object[]{Long.valueOf(agence_), point_, debut, fin, periode_.charAt(0),});

        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("getTableauBordArticle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    public List<Object[]> getTableauBordArticle(@HeaderParam("agence_") String agence_, @HeaderParam("point_") String point_, @HeaderParam("articles_") String articles_, @HeaderParam("date_debut_") String date_debut_, @HeaderParam("date_fin_") String date_fin_, @HeaderParam("periode_") String periode_
    ) {
        try {
            Long societe = (Long) dao.loadObjectBySqlQuery("SELECT societe FROM yvs_agences WHERE id = ?", new Options[]{new Options(Long.valueOf(agence_), 1)});
            Date debut = df.parse(date_debut_);
            Date fin = df.parse(date_fin_);
            String rq = "SELECT * FROM com_et_total_articles(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            System.out.println(" getTableauBordArticle");
            return dao.loadDataByNativeQuery(rq, new Object[]{societe, Long.valueOf(agence_), Long.valueOf(point_), 0, 0, 0, 0, 0, debut, fin, articles_, "", periode_, "", 0, 0});
        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("getTableauBordArticle_")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    public List<Object[]> getTableauBordArticle_(@HeaderParam("agence_") String agence_, @HeaderParam("articles_") String articles_, @HeaderParam("date_debut_") String date_debut_, @HeaderParam("date_fin_") String date_fin_, @HeaderParam("periode_") String periode_
    ) {
        try {Long societe = (Long) dao.loadObjectBySqlQuery("SELECT societe FROM yvs_agences WHERE id = ?", new Options[]{new Options(Long.valueOf(agence_), 1)});
            Date debut = df.parse(date_debut_);
            Date fin = df.parse(date_fin_);
            String rq = "SELECT * FROM com_et_total_articles(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            return dao.loadDataByNativeQuery(rq, new Object[]{societe, Long.valueOf(agence_), 0, 0, 0, 0, 0, 0, debut, fin, articles_, "", periode_, "", 0, 0});
        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("getTableauBordStock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    public List<Object[]> getTableauBordStock(@HeaderParam("date_") String date_, @HeaderParam("categorie_") String categorie_, @HeaderParam("depots_") String depots_, @HeaderParam("groupe_by_") String groupe_by_, @HeaderParam("societe_") String societe_
    ) {
        try {
            Date debut = df.parse(date_);
            // Date fin = df.parse(date_fin_);
            String rq = "SELECT * FROM com_et_valorise_stock(?,?,?,?,?)";
            System.out.println(" getTableauBordStock");
            return dao.loadDataByNativeQuery(rq, new Object[]{debut, "", depots_, groupe_by_, Long.valueOf(societe_)});
        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("ModifPrixConditionnement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public boolean ModifPrixConditionnement(@HeaderParam("id") String id, @HeaderParam("prix") String prix, @HeaderParam("prix_min") String prix_min, @HeaderParam("prix_achat") String prix_achat, @HeaderParam("remise") String remise
    ) {
        try {
            YvsBaseConditionnement conditionnement = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findById", new String[]{"id"}, new Object[]{Long.valueOf(id)});
            conditionnement.setPrix(asString(prix) ? Double.valueOf(prix) : 0);
            conditionnement.setPrixMin(asString(prix_min) ? Double.valueOf(prix_min) : 0);
            conditionnement.setPrixAchat(asString(prix_achat) ? Double.valueOf(prix_achat) : 0);
            conditionnement.setRemise(asString(remise) ? Double.valueOf(remise) : 0);
            dao.update(conditionnement);
            return true;
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    @POST
    @Path("ModifPrixConditionnementPoint")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public boolean ModifPrixConditionnementPoint(@HeaderParam("id") String id, @HeaderParam("prix") String prix, @HeaderParam("prix_min") String prix_min
    ) {
        try {
            YvsBaseConditionnementPoint conditionnement = (YvsBaseConditionnementPoint) dao.loadOneByNameQueries("YvsBaseConditionnementPoint.findById", new String[]{"id"}, new Object[]{Long.valueOf(id)});
            conditionnement.setPuv(asString(prix) ? Double.valueOf(prix) : 0);
            conditionnement.setPrixMin(asString(prix_min) ? Double.valueOf(prix_min) : 0);
            dao.update(conditionnement);
            return true;
        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    @POST
    @Path("saveConditionnemntPoint")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public boolean saveConditionnemntPoint(@HeaderParam("article") String article, @HeaderParam("point") String point, @HeaderParam("conditionnnement") String conditionnement, @HeaderParam("prix") String prix,
            @HeaderParam("prix_min") String prix_min, @HeaderParam("user") String user
    ) {
        try {
            YvsBaseArticlePoint articlePoint = (YvsBaseArticlePoint) dao.loadOneByNameQueries("YvsBaseArticlePoint.findByArticleEmplacement", new String[]{"article", "emplacement"},
                    new Object[]{new YvsBaseArticles(Long.valueOf(article)), new YvsBasePointVente(Long.valueOf(point))});
            YvsUsers users = new YvsUsers(Long.valueOf(user));
            YvsUsersAgence author = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findByUser", new String[]{"users"}, new Object[]{users});
            if (articlePoint != null ? articlePoint.getId() < 1 : true) {
                articlePoint = new YvsBaseArticlePoint(null);
                articlePoint.setPoint(new YvsBasePointVente(Long.valueOf(point)));
                articlePoint.setArticle(new YvsBaseArticles(Long.valueOf(article)));
                articlePoint.setAuthor(author);
                articlePoint.setChangePrix(true);
                articlePoint.setDateSave(new Date());
                articlePoint.setPuv(Double.valueOf(prix));
                articlePoint.setPuvMin(Double.valueOf(prix_min));
                articlePoint = (YvsBaseArticlePoint) dao.save1(articlePoint);
            }
            if (articlePoint != null ? articlePoint.getId() > 0 : false) {
                if (!asString(conditionnement)) {
                    return false;
                }
                YvsBaseConditionnement con = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findById", new String[]{"id"}, new Object[]{Long.valueOf(conditionnement)});
                if (con != null ? con.getId() > 0 : false) {
                    YvsBaseConditionnementPoint point1 = new YvsBaseConditionnementPoint();
                    point1.setArticle(articlePoint);
                    point1.setConditionnement(con);
                    point1.setDateSave(new Date());
                    point1.setPrixMin(Double.valueOf(prix_min));
                    point1.setPuv(Double.valueOf(prix));
                    point1.setAuthor(author);
                    dao.save(point1);
                    return true;
                }
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    @POST
    @Path("modifPlanTarrifaire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public boolean modifPlanTarrifaire(@HeaderParam("doc") String doc, @HeaderParam("prix") String prix, @HeaderParam("prix_min") String prix_min
    ) {
        try {
            if (!prix.equals("") ? prix != null : false) {
                if (!prix_min.equals("") ? prix_min != null : false) {
                    YvsBasePlanTarifaire plan = (YvsBasePlanTarifaire) dao.loadOneByNameQueries("YvsBasePlanTarifaire.findById", new String[]{"id"}, new Object[]{Long.valueOf(doc)});
                    plan.setPuv(Double.valueOf(prix));
                    plan.setPuvMin(Double.valueOf(prix_min));

                    dao.update(plan);
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("erreur = " + e.getMessage());
        }
        return false;
    }

    @POST
    @Path("savePlanTarifaire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public boolean savePlanTarifaire(@HeaderParam("categorie") String categorie, @HeaderParam("article") String article, @HeaderParam("conditionnnement") String conditionnnement,
            @HeaderParam("user") String user, @HeaderParam("puv") String puv, @HeaderParam("puv_min") String puv_min
    ) {
        try {

            YvsBaseCategorieClient cat = new YvsBaseCategorieClient(Long.valueOf(categorie));
            YvsBaseArticles art = new YvsBaseArticles(Long.valueOf(article));
            YvsBaseConditionnement con = new YvsBaseConditionnement(Long.valueOf(conditionnnement));
            YvsUsers users = new YvsUsers(Long.valueOf(user));
            YvsUsersAgence author = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findByUser", new String[]{"users"}, new Object[]{users});

            YvsBasePlanTarifaire plan = new YvsBasePlanTarifaire();
            plan.setArticle(art);
            plan.setAuthor(author);
            plan.setCategorie(cat);
            plan.setConditionnement(con);
            plan.setActif(true);
            plan.setDateSave(new Date());
            plan.setPuv(Double.valueOf(puv));
            plan.setPuvMin(Double.valueOf(puv_min));
            dao.save(plan);
            return true;

        } catch (Exception e) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    @POST
    @Path("getTableauBordVendeur")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    public List<Object[]> getTableauBordVendeur(@HeaderParam("societe") String societe, @HeaderParam("agence") String agence, @HeaderParam("debut") String debut, @HeaderParam("fin") String fin, @HeaderParam("periode") String periode) {
        try {
            Date debuts = df.parse(debut);
            Date fins = df.parse(fin);
            String rq = "SELECT * FROM et_total_vendeurs(?,?,?,?,?,?)";
            System.out.println(" getTableauBordArticle_");
            return dao.loadDataByNativeQuery(rq, new Object[]{Long.valueOf(societe), Long.valueOf(agence), debuts, fins, "", periode});
        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
