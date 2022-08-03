/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.dashboards.commercial;

import java.util.List;
import yvs.entity.produits.group.YvsBaseFamilleArticle;

/**
 *
 * @author Lymytz-pc
 */
public interface IDashBoardCom {

    public List<Object[]> getTableauBordCA(String societe, String agence, String date_debut, String date_fin);

    public List<Object[]> getTableauBordCAGeneral(String societe, String agence, String date_debut, String date_fin);

    List<Object[]> getTableauBordPoint(String agence_, String point_, String date_debut_, String date_fin_, String periode_);

    List<Object[]> getTableauBordPoints(String societe, String agence, String date_debut_, String date_fin_, String reference, String periode_);

    List<Object[]> getTableauBordArticle(String agence_, String point_, String articles_, String date_debut_, String date_fin_, String periode_);

    List<Object[]> getTableauBordArticle_(String agence_, String articles_, String date_debut_, String date_fin_, String periode_);

    List<Object[]> getTableauBordStock(String date_, String categorie_, String depots_, String groupe_by_, String societe_);

    List<Object[]> getTableauBordVendeur(String societe, String agence, String debut, String fin, String reference, String periode);

    List<Object[]> getInventaire(String societe, String agence_, String depot_, String famille_, String categorie_, String groupe_, String date_, String print_all_, String option_print_, String valoriser_, String articles_, String offset, String limit);

    List<YvsBaseFamilleArticle> getFamille(String societe);

    List<Object[]> getTableauBordClient(String date_debut, String date_fin, boolean all, String societe);

    public List<Object[]> getTableauBordArticles(String societe, String agence, String point, String vendeur, String commercial, String client, String famille, String groupe, String article, String categorie, String type, String date_debut, String date_fin, String periode, String offset, String limit);

    public void save();

    List<Object[]> getJournalVente(String societe, String agence, String date_debut, String date_fin, String famille);

}
