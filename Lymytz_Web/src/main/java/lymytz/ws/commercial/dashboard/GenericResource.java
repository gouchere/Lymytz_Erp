/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws.commercial.dashboard;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.service.dashboards.commercial.DashBoadComImplV1;
import yvs.service.dashboards.commercial.IDashBoardCom;

/**
 *
 * @author Lymytz Dowes
 */
@Path("services/commercial/dashboard")
@RequestScoped
public class GenericResource extends lymytz.ws.dashboard.GenericResource {

    @POST
    @Path("getTableauBordCA")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    public List<Object[]> getTableauBordCA(@HeaderParam("societe") String societe, @HeaderParam("agence") String agence, @HeaderParam("date_debut") String date_debut, @HeaderParam("date_fin") String date_fin) {
        IDashBoardCom da = new DashBoadComImplV1(dao);
        return da.getTableauBordCA(societe, agence, date_debut, date_fin);
    }

    @POST
    @Path("getTableauBordCAGeneral")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    public List<Object[]> getTableauBordCAGeneral(@HeaderParam("societe") String societe, @HeaderParam("agence") String agence, @HeaderParam("date_debut") String date_debut, @HeaderParam("date_fin") String date_fin) {
        System.err.println("CA general ");
        IDashBoardCom da = new DashBoadComImplV1(dao);
        return da.getTableauBordCAGeneral(societe, agence, date_debut, date_fin);
    }

    @POST
    @Path("getTableauBordPoint")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    @Override
    public List<Object[]> getTableauBordPoint(@HeaderParam("agence") String agence, @HeaderParam("point") String point, @HeaderParam("date_debut") String date_debut, @HeaderParam("date_fin") String date_fin, @HeaderParam("periode") String periode
    ) {
        IDashBoardCom da = new DashBoadComImplV1(dao);
        return da.getTableauBordPoint(agence, point, date_debut, date_fin, periode);
    }

    @POST
    @Path("getTableauBordPoints")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    public List<Object[]> getTableauBordPoints(@HeaderParam("societe") String societe, @HeaderParam("agence") String agence, @HeaderParam("date_debut") String date_debut, @HeaderParam("date_fin") String date_fin, @HeaderParam("reference") String reference, @HeaderParam("periode") String periode){
        IDashBoardCom da = new DashBoadComImplV1(dao);
        return da.getTableauBordPoints(societe, agence, date_debut, date_fin, reference, periode);
    }

    @POST
    @Path("getTableauBordArticle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    @Override
    public List<Object[]> getTableauBordArticle(@HeaderParam("agence_") String agence_, @HeaderParam("point_") String point_, @HeaderParam("articles_") String articles_, @HeaderParam("date_debut_") String date_debut_, @HeaderParam("date_fin_") String date_fin_, @HeaderParam("periode_") String periode_
    ) {
        IDashBoardCom da = new DashBoadComImplV1(dao);
        return da.getTableauBordArticle(agence_, point_, articles_, date_debut_, date_fin_, periode_);
    }

    @POST
    @Path("getTableauBordArticle_")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    @Override
    public List<Object[]> getTableauBordArticle_(@HeaderParam("agence_") String agence_, @HeaderParam("articles_") String articles_, @HeaderParam("date_debut_") String date_debut_, @HeaderParam("date_fin_") String date_fin_, @HeaderParam("periode_") String periode_) {
        IDashBoardCom da = new DashBoadComImplV1(dao);
        return da.getTableauBordArticle_(agence_, articles_, date_debut_, date_fin_, periode_);
    }

    @POST
    @Path("getTableauBordStock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    @Override
    public List<Object[]> getTableauBordStock(@HeaderParam("date_") String date_, @HeaderParam("categorie_") String categorie_, @HeaderParam("depots_") String depots_, @HeaderParam("groupe_by_") String groupe_by_, @HeaderParam("societe_") String societe_
    ) {
        IDashBoardCom da = new DashBoadComImplV1(dao);
        return da.getTableauBordStock(date_, categorie_, depots_, groupe_by_, societe_);
    }

    @POST
    @Path("getTableauBordVendeur")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    @Override
    public List<Object[]> getTableauBordVendeur(@HeaderParam("societe") String societe, @HeaderParam("agence") String agence, @HeaderParam("date_debut") String debut, @HeaderParam("date_fin") String fin, @HeaderParam("periode") String periode) {
        IDashBoardCom da = new DashBoadComImplV1(dao);
        return da.getTableauBordVendeur(societe, agence, debut, fin, "", periode);
    }

    @POST
    @Path("getTableauBordClient")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    public List<Object[]> getTableauBordClient(@HeaderParam("societe") String societe, @HeaderParam("date_debut") String date_debut, @HeaderParam("date_fin") String date_fin, @HeaderParam("all") String all) {
        IDashBoardCom da = new DashBoadComImplV1(dao);
        boolean al = false;
        if (all.equals("true")) {
            al = true;
        }
        return da.getTableauBordClient(date_debut, date_fin, al, societe);
    }

    @POST
    @Path("getInventaire")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    public List<Object[]> getInventaire(@HeaderParam("societe") String societe, @HeaderParam("agence") String agence, @HeaderParam("depot") String depot, @HeaderParam("famille") String famille,
            @HeaderParam("categorie") String categorie, @HeaderParam("groupe") String groupe, @HeaderParam("date_stock") String date_stock,
            @HeaderParam("print_all") String print_all, @HeaderParam("option_print") String option_print, @HeaderParam("valoriser") String valoriser, @HeaderParam("articles") String articles,
            @HeaderParam("offset") String offset, @HeaderParam("limit") String limit) {
        
        System.out.println("societe = " + societe);
        System.out.println("agence = " + agence);
        System.out.println("depot = " + depot);
        System.out.println("famille = " + famille);
        System.out.println("categorie = " + categorie);
        System.out.println("groupe = " + groupe);
        System.out.println("date_stock = " + date_stock);
        System.out.println("print_all = " + print_all);
        System.out.println("option_print = " + option_print);
        System.out.println("valoriser = " + valoriser);
        System.out.println("articles = " + articles);
        System.out.println("offset = " + offset);
        System.out.println("limit = " + limit);
        IDashBoardCom da = new DashBoadComImplV1(dao);
        return da.getInventaire(societe, agence, depot, famille, categorie, groupe, date_stock, print_all, option_print, valoriser, articles, offset, limit);
    }

    @POST
    @Path("getTableauBordArticles")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    public List<Object[]> getTableauBordArticles(@HeaderParam("societe") String societe, @HeaderParam("agence") String agence, @HeaderParam("point") String point, @HeaderParam("vendeur") String vendeur, @HeaderParam("commercial") String commercial, @HeaderParam("client") String client, @HeaderParam("famille") String famille, @HeaderParam("groupe") String groupe, @HeaderParam("article") String article, @HeaderParam("categorie") String categorie, @HeaderParam("type") String type, @HeaderParam("date_debut") String date_debut, @HeaderParam("date_fin") String date_fin, @HeaderParam("periode") String periode, @HeaderParam("offset") String offset, @HeaderParam("limit") String limit) {
        IDashBoardCom da = new DashBoadComImplV1(dao);
        return da.getTableauBordArticles(societe, agence, point, vendeur, commercial, client, famille, groupe, article, categorie, type, date_debut, date_fin, periode, offset, limit);
    }

    @POST
    @Path("getJournalVente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    public List<Object[]> getJournalVente(@HeaderParam("societe") String societe, @HeaderParam("agence") String agence, @HeaderParam("date_debut") String date_debut, @HeaderParam("date_fin") String date_fin, @HeaderParam("famille") String famille) {
        IDashBoardCom da = new DashBoadComImplV1(dao);
        return da.getJournalVente(societe, agence, date_debut, date_fin, famille);
    }

    @POST
    @Path("getFamille")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.TEXT_PLAIN})
    public List<YvsBaseFamilleArticle> getFamille(@HeaderParam("societe") String societe) {
        IDashBoardCom da = new DashBoadComImplV1(dao);
        return da.getFamille(societe);
    }
}
