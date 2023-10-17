/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.timer.workflow;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.*;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import yvs.MyLog;
import yvs.timer.InterfaceTimerLocal;

/**
 *
 * @author LYMYTZ-PC
 */
@Singleton
public class TimerWarning implements InterfaceTimerLocal {

    @EJB
    public DaoInterfaceLocal dao;

//    @Schedule(dayOfMonth = "*", month = "*", year = "*", hour = "23", dayOfWeek = "*", minute = "59", persistent = false)
    @Override
    public void myTimer() {
//        execute();
    }

    @Timeout
    public void timeout() {

    }

    @Override
    public void avancement() {

    }

    private void execute() {
        MyLog.Write(getClass(), "execute", true, true);
        try {
            String query = "SELECT y.model, y.ecart, m.titre_doc, y.societe FROM yvs_warning_model_doc y INNER JOIN yvs_workflow_model_doc m ON y.model = m.id WHERE COALESCE(y.ecart, 0) > 0 AND m.titre_doc NOT IN ('ARTICLE_NON_MOUVEMENTE', 'STOCK_ARTICLE')";
            List<Object[]> models = dao.loadListBySqlQuery(query, new Options[]{});
            for (Object[] data : models) {
                Integer model = (Integer) data[0];
                Integer ecart = (Integer) data[1];
                String titre = (String) data[2];
                Integer societe = (Integer) data[3];
                query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_agences a WHERE y.agence = a.id AND y.actif IS TRUE AND y.silence IS TRUE AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                switch (titre) {
                    case "MISSIONS": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_grh_missions m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.id_element = m.id AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut_mission NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "FORMATIONS": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_grh_formation_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.id_element = m.id AND y.actif IS TRUE AND y.silence IS TRUE AND m.valider IS FALSE AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "CONGES": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.id_element = m.id AND m.nature != 'P' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "PERMISSION_LD":
                    case "PERMISSION_CD": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_grh_conge_emps m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.id_element = m.id AND m.nature = 'P' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "APPROVISIONNEMENT": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_fiche_approvisionnement m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE y.id_element = m.id AND y.actif IS TRUE AND y.silence IS TRUE AND m.etat NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "FACTURE_ACHAT": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_doc_achats m INNER JOIN yvs_agences a ON m.agence = a.id WHERE y.id_element = m.id AND m.type_doc = 'FA' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "RETOUR_ACHAT": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_doc_achats m INNER JOIN yvs_agences a ON m.agence = a.id WHERE y.id_element = m.id AND m.type_doc = 'BRA' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "AVOIR_ACHAT": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_doc_achats m INNER JOIN yvs_agences a ON m.agence = a.id WHERE y.id_element = m.id AND m.type_doc = 'FAA' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "BON_LIVRAISON_ACHAT": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_doc_achats m INNER JOIN yvs_agences a ON m.agence = a.id WHERE y.id_element = m.id AND m.type_doc = 'BLA' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "FACTURE_ACHAT_LIVRE": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_doc_achats m INNER JOIN yvs_agences a ON m.agence = a.id WHERE y.id_element = m.id AND m.type_doc = 'FA' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut = 'V' AND m.statut_livre NOT IN ('L', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "FACTURE_ACHAT_REGLE": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_doc_achats m INNER JOIN yvs_agences a ON m.agence = a.id WHERE y.id_element = m.id AND m.type_doc = 'FA' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut = 'V' AND m.statut_regle NOT IN ('P', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "FACTURE_VENTE": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.id_element = m.id AND m.type_doc = 'FV' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "RETOUR_VENTE": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.id_element = m.id AND m.type_doc = 'BRV' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "AVOIR_VENTE": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.id_element = m.id AND m.type_doc = 'FAV' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "BON_LIVRAISON_VENTE": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.id_element = m.id AND m.type_doc = 'BLV' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "FACTURE_VENTE_LIVRE": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.id_element = m.id AND m.type_doc = 'FV' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut = 'V' AND m.statut_livre NOT IN ('L', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "FACTURE_VENTE_REGLE": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_doc_ventes m INNER JOIN yvs_com_entete_doc_vente e ON m.entete_doc = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.id_element = m.id AND m.type_doc = 'FV' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut = 'V' AND m.statut_regle NOT IN ('P', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "SORTIE_STOCK": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_doc_stocks m INNER JOIN yvs_base_depots e ON m.source = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.id_element = m.id AND m.type_doc = 'SS' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "ENTREE_STOCK": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_doc_stocks m INNER JOIN yvs_base_depots e ON m.source = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.id_element = m.id AND m.type_doc = 'ES' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "RECONDITIONNEMENT_STOCK": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_doc_stocks m INNER JOIN yvs_base_depots e ON m.source = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.id_element = m.id AND m.type_doc = 'TR' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "INVENTAIRE_STOCK": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_doc_stocks m INNER JOIN yvs_base_depots e ON m.source = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.id_element = m.id AND m.type_doc = 'IN' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "TRANSFERT_STOCK": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_com_doc_stocks m INNER JOIN yvs_base_depots e ON m.source = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.id_element = m.id AND m.type_doc = 'FT' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "BON_OPERATION_DIVERS": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_compta_bon_provisoire m INNER JOIN yvs_agences a ON m.agence = a.id WHERE y.id_element = m.id AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "OPERATION_DIVERS": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_compta_caisse_doc_divers m INNER JOIN yvs_agences a ON m.agence = a.id WHERE y.id_element = m.id AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut_doc NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "DOC_DIVERS_DEPENSE": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_compta_caisse_doc_divers m INNER JOIN yvs_agences a ON m.agence = a.id WHERE y.id_element = m.id AND m.mouvement = 'D' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut_doc NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "DOC_DIVERS_RECETTE": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_compta_caisse_doc_divers m INNER JOIN yvs_agences a ON m.agence = a.id WHERE y.id_element = m.id AND m.mouvement = 'R' AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut_doc NOT IN ('V', 'T', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "PIECE_CAISSE": {
                        query = "UPDATE yvs_workflow_alertes y SET silence = FALSE FROM yvs_compta_mouvement_caisse m INNER JOIN yvs_agences a ON m.agence = a.id WHERE y.id_element = m.id AND y.actif IS TRUE AND y.silence IS TRUE AND m.statut_piece NOT IN ('P', 'C', 'A') AND a.societe = ? AND y.model_doc = ? AND ((current_date - COALESCE(y.date_doc, current_date)) > COALESCE(?, 0))";
                        break;
                    }
                    case "STOCK_ARTICLE": {

                        break;
                    }
                    case "ARTICLE_NON_MOUVEMENTE": {

                        break;
                    }
                    case "OD_NON_PLANNIFIE": {

                        break;
                    }
                    case "HIGH_PR_ARTICLE": {

                        break;
                    }
                }
                dao.requeteLibre(query, new Options[]{new Options(societe, 1), new Options(model, 2), new Options(ecart, 3)});
            }
        } catch (Exception ex) {
            Logger.getLogger(TimerWarning.class.getName()).log(Level.SEVERE, null, ex);
        }
        MyLog.Write(getClass(), "execute", true, false);
    }
}
