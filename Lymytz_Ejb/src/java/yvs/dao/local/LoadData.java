/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.local;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import yvs.entity.commercial.vente.YvsComDocVentes;

/**
 *
 * @author LENOVO
 */
public class LoadData {

    public LoadData() {
    }

    public void loadNameQueries(String querie, String[] champ, Object[] val) {
        try {
            try {
                querie = "SELECT * FROM yvs_com_doc_ventes d INNER JOIN yvs_com_client cl ON cl.id=d.client "
                        + "INNER JOIN yvs_com_entete_doc_vente e ON e.id=entete_doc INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau "
                        + "INNER JOIN yvs_users u ON u.id=cu.users INNER JOIN yvs_com_creneau_point cp ON cp.id=cu.creneau_point";
                ConnexionBD.getInstance().getEntityManager().getTransaction().begin();
                Query qr = ConnexionBD.getInstance().getEntityManager().
                        createNativeQuery(querie);
                ConnexionBD.getInstance().getEntityManager().getTransaction().commit();
                List<Object[]> result = qr.getResultList();
                for (Object[] line : result) {
                    System.err.println("------ " + line[0]);
                }
            } catch (Exception ex) {
                ConnexionBD.getInstance().getEntityManager().getTransaction().rollback();
                Logger.getLogger(LoadData.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {

        }
    }

    public void intrspecterClasse() {
        Class<YvsComDocVentes> classe=YvsComDocVentes.class;
//        classe.
    }
}
