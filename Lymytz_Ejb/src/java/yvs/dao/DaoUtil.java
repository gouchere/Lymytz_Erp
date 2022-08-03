/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseModeleReference;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComFicheApprovisionnement;
import yvs.entity.commercial.ration.YvsComDocRation;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.stock.YvsComReservationStock;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaMouvementCaisse;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.mutuelle.credit.YvsMutCredit;
import yvs.entity.mutuelle.credit.YvsMutTypeCredit;
import yvs.entity.mutuelle.operation.YvsMutOperationCompte;
import yvs.entity.param.YvsSocietes;
import yvs.entity.production.pilotage.YvsProdComposantOF;
import yvs.entity.production.pilotage.YvsProdOrdreFabrication;
import yvs.entity.production.pilotage.YvsProdOperationsOF;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 * @param <T>
 *
 */
@Stateless
public class DaoUtil<T extends Serializable> extends AbstractDao<T> implements DaoInterfaceUtil<T> {

    String entityName;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public DaoUtil() {
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public EntityManager getEntityManager(String EM) {
        if (null != EM) {
            switch (EM) {
                case "MSG":
                    return emM;
                case "ERP":
                    return em;
            }
        }
        return em;
    }

    @Override
    public YvsProdOrdreFabrication saveOF(YvsProdOrdreFabrication of, List<YvsProdComposantOF> lof, List<YvsProdOperationsOF> lph, YvsUsersAgence author) {
        of.setId(null);
        getEntityManager().persist(of);
        getEntityManager().flush();
        for (YvsProdComposantOF cc : lof) {
            cc.setId(null);
            cc.setOrdreFabrication(of);
            cc.setAuthor(author);
            getEntityManager().persist(cc);
        }
        for (YvsProdOperationsOF c : lph) {
            c.setId(null);
            c.setOrdreFabrication(of);
            c.setAuthor(author);
            getEntityManager().persist(c);
        }
        of.setComposants(lof);
        of.setOperations(lph);
        return of;
    }

    @Override
    public boolean changeStateOF(String state, long id) {
        String query = "UPDATE yvs_prod_ordre_fabrication SET statut_ordre=? WHERE id=?";
        Query qu = getEntityManager().createNativeQuery(query);
        qu.setParameter("1", state);
        qu.setParameter("2", id);
        qu.executeUpdate();
        return true;
    }

    @Override
    public boolean updateQuatiteNomenclature(double qte, long id) {
        String query = "UPDATE yvs_prod_nomenclature SET quantite=? WHERE id=?";
        Query qu = getEntityManager().createNativeQuery(query);
        qu.setParameter("1", qte);
        qu.setParameter("2", id);
        qu.executeUpdate();
        return true;
    }
}
