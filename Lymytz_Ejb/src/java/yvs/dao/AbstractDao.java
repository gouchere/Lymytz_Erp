/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ManagedSalaire;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBaseModeleReference;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.base.YvsBaseTrancheReglement;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComFicheApprovisionnement;
import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.commercial.vente.YvsComMensualiteFactureVente;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaParametre;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.saisie.YvsComptaContentAnalytique;
import yvs.entity.compta.saisie.YvsComptaContentJournalAbonnementDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalAcompteClient;
import yvs.entity.compta.saisie.YvsComptaContentJournalAcompteFournisseur;
import yvs.entity.compta.saisie.YvsComptaContentJournalBulletin;
import yvs.entity.compta.saisie.YvsComptaContentJournalCreditClient;
import yvs.entity.compta.saisie.YvsComptaContentJournalCreditFournisseur;
import yvs.entity.compta.saisie.YvsComptaContentJournalDocDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalEnteteBulletin;
import yvs.entity.compta.saisie.YvsComptaContentJournalEnteteFactureVente;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapeAcompteAchat;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapeAcompteVente;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapePieceAchat;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapePieceDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapePieceVente;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapePieceVirement;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapeReglementCreditClient;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapeReglementCreditFournisseur;
import yvs.entity.compta.saisie.YvsComptaContentJournalFactureAchat;
import yvs.entity.compta.saisie.YvsComptaContentJournalFactureVente;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceAchat;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceVente;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceVirement;
import yvs.entity.compta.saisie.YvsComptaContentJournalReglementCreditClient;
import yvs.entity.compta.saisie.YvsComptaContentJournalReglementCreditFournisseur;
import yvs.entity.grh.activite.YvsGrhCongeEmps;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.grh.param.YvsGrhConventionCollective;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.grh.salaire.YvsGrhBulletins;
import yvs.entity.grh.salaire.YvsGrhOrdreCalculSalaire;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.workflow.YvsWorkflowValidApprovissionnement;
import yvs.entity.param.workflow.YvsWorkflowValidBonProvisoire;
import yvs.entity.param.workflow.YvsWorkflowValidConge;
import yvs.entity.param.workflow.YvsWorkflowValidDocCaisse;
import yvs.entity.param.workflow.YvsWorkflowValidDocStock;
import yvs.entity.param.workflow.YvsWorkflowValidFactureAchat;
import yvs.entity.param.workflow.YvsWorkflowValidFactureVente;
import yvs.entity.param.workflow.YvsWorkflowValidMission;
import yvs.entity.synchro.YvsSynchroDataSynchro;
import yvs.entity.synchro.YvsSynchroListenTable;
import yvs.entity.synchro.YvsSynchroServeurs;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.service.IEntitySax;
import yvs.service.com.vente.IYvsComEnteteDocVente;

/**
 *
 * @author GOUCHERE YVES
 * @param <T>
 */
public abstract class AbstractDao<T extends Serializable> {

    @PersistenceContext(unitName = "LYMYTZ-ERP-ejbPU")
    public EntityManager em;

//    @PersistenceContext(unitName = "LYMYTZ-MESSAGERIE-EJBPU")
    public EntityManager emM;
    Class<T> entityClass;
    String EM;

    public String RESULT;
    public String[] champ;
    public Object[] val;
    public String nameQueriCount, nameQueri;

    public Fonctions fonction = new Fonctions();
    ManagedSalaire managedS;
    public static DateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");

    public YvsAgences currentAgence = null;
    public YvsSocietes currentScte = null;
    public YvsUsersAgence currentUser = null;
    public YvsBasePointVente currentPoint = null;
    public YvsBaseDepots currentDepot = null;
    public YvsMutMutuelle currentMutuel = null;
    public YvsBaseExercice currentExo = null;

    public abstract EntityManager getEntityManager();

    public abstract EntityManager getEntityManager(String EM);

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public EntityManager giveEntityManager() {
        return em;
    }

    public CriteriaBuilder giveCriteriaBuilder() {
        return em.getCriteriaBuilder();
    }

    public List<T> executeCriteriaQuery(CriteriaQuery<T> cq) {
        return em.createQuery(cq).getResultList();
    }

    public void setEM(String EM) {
        this.EM = EM;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }

    public String getRESULT() {
        return RESULT;
    }

    public YvsSocietes getCurrentScte() {
        return currentScte;
    }

    public boolean autoriser(String ressource, YvsNiveauAcces niveau) {
        try {
            String query = "SELECT a.acces FROM yvs_autorisation_ressources_page a INNER JOIN yvs_ressources_page r ON a.ressource_page = r.id WHERE r.reference_ressource = ? AND a.niveau_acces = ?";
            Boolean b = (Boolean) loadObjectBySqlQuery(query, new yvs.dao.Options[]{new yvs.dao.Options(ressource, 1), new yvs.dao.Options(niveau.getId(), 2)});
            return b != null ? b : false;
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    //sauvegarder une entité persistente Et retourner l'instence persisté
    //sauvegarder une entité persistente
    //TEST
    public void save(T en, boolean synchronise) {
        if (EM == null) {
            EM = "ERP";
        }
        getEntityManager().persist(en);
        getEntityManager().flush();
        if (synchronise) {
            afterCRUD(en, "INSERT");
        }
        en = null;
    }

    public void save(T en) {
        save(en, true);
    }

    //sauvegarder une entité persistente Et retourner l'instence persisté
    public T save1(T en, boolean synchronise) {
        getEntityManager().persist(en);
//        en = getEntityManager().merge(en);
        getEntityManager().flush();
        if (synchronise) {
            afterCRUD(en, "INSERT");
        }
        return en;
    }

    public T save1(T en) {
        return save1(en, true);
    }

    public T update(T en, boolean synchronise) {
        getEntityManager().clear();
        T re = ((T) getEntityManager().merge(en));
        try {
            getEntityManager().flush();
        } catch (Exception e) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, e);
        }
        if (synchronise) {
            afterCRUD(re, "UPDATE");
        }
        return re;
    }

    public T update(T en) {
        return update(en, true);
    }

    public boolean delete(T en, boolean synchronise) {
        T r = getEntityManager().merge(en);
        getEntityManager().remove(r);
        getEntityManager().flush();
        if (synchronise) {
            afterCRUD(en, "DELETE");
        }
        return true;
    }

    public boolean delete(T en) {
        return delete(en, true);
    }
//cette requete interroge le contexte de persistence. ici, on ne verra que les modification apporté dans le contexte (dans l'entity manager)
//et pas celle appporté par une requête sql native  un ordre update par exemple qui change directement la bd

    public List<T> loadAllEntity() {
        List<T> result = null;
        result = getEntityManager().createQuery("SELECT d From " + entityClass.getSimpleName() + " d").getResultList();
//        for (T t : result) {
//            getEntityManager().refresh(t);
//        }
        return result;
    }

    /**
     * Sauvegarde dans une nouvelle transaction
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveInNewTransaction() {
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveInCurrentTransaction() {
    }

    public List<T> loadNameQueries(String querie, String[] champ, Object[] val) {
//        getEntityManager().clear();
        Query qr = getEntityManager().createNamedQuery(querie);
        int i = 0;
        for (String st : champ) {
            qr.setParameter(st, val[i]);
            i++;
        }
        return qr.getResultList();
    }

    public List<T> loadNameQueries(String querie, String[] champ, Object[] val, int offset, int limit) {
        getEntityManager().clear();
        Query qr = getEntityManager().createNamedQuery(querie);
        int i = 0;
        for (String st : champ) {
            qr.setParameter(st, val[i]);
            i++;
        }
        qr.setFirstResult(offset);
        qr.setMaxResults(limit);
        return qr.getResultList();
    }
    /*
     * retourne un tableau contenant les champs d'une table identique à la
     * méthode précédente, elle permet de ne pas charger tout les attributs
     * d'une entité
     */

    public List<Object[]> loadListTableByNameQueries(String querie, String[] champ, Object[] val) {
        getEntityManager().clear();
        Query qr = getEntityManager().createNamedQuery(querie);
        int i = 0;
        for (String st : champ) {
            qr.setParameter(st, val[i]);
            i++;
        }
        try {
            return qr.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public T loadOneByNameQueries(String querie, String[] champ, Object[] val) {
        getEntityManager().clear();
        Query qr = getEntityManager().createNamedQuery(querie);
        int i = 0;
        for (String st : champ) {
            qr.setParameter(st, val[i]);
            i++;
        }
        qr.setFirstResult(0);
        qr.setMaxResults(1);
        try {
            return (T) qr.getSingleResult();
        } catch (NoResultException e) {
            try {
                List<T> result = qr.getResultList();
                return result != null ? !result.isEmpty() ? result.get(0) : null : null;
            } catch (NoResultException ex) {
                return null;
            }
        }
    }

    public Object loadObjectByNameQueries(String querie, String[] champ, Object[] val) {
        getEntityManager().clear();
        Query qr = getEntityManager().createNamedQuery(querie);
        int i = 0;
        for (String st : champ) {
            qr.setParameter(st, val[i]);
            i++;
        }
        qr.setMaxResults(1);
        try {
            return qr.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    /*
     * retourne un tableau contenant les champs d'une table identique à la
     * méthode précédente, elle permet de ne pas charger tout les attributs
     * d'une entité
     */

    public Object[] loadTableByNameQueries(String querie, String[] champ, Object[] val) {
        getEntityManager().clear();
        Query qr = getEntityManager().createNamedQuery(querie);
        int i = 0;
        for (String st : champ) {
            qr.setParameter(st, val[i]);
            i++;
        }
        qr.setMaxResults(1);
        try {
            return (Object[]) qr.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Object> loadDataByNativeQuery(String querie, Object[] val) {
        getEntityManager().clear();
        Query qr = getEntityManager().createNativeQuery(querie);
        int i = 1;
        for (Object st : val) {
            qr.setParameter(i, st);
            i++;
        }
        try {
            return qr.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Object> loadListByNameQueries(String querie, String[] champ, Object[] val) {
        getEntityManager().clear();
        Query qr = getEntityManager().createNamedQuery(querie);
        int i = 0;
        for (String st : champ) {
            qr.setParameter(st, val[i]);
            i++;
        }
        try {
            return qr.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * cette opération va synchroniser l'état d'une entité dans le contexte de
     * persistence avec son état dans la bd au cas ou un changement a été opéré
     * sur la ligne de la base de donné par un ordre sql ou de façon directe
     *
     * @param en
     */
    public void refresh(T en) {
        getEntityManager().refresh(en);
    }

    public T getOne(Object id) {
        T result;
        Serializable sb = (Serializable) id;
        result = (T) getEntityManager().find(entityClass, sb);
        getEntityManager().refresh(result);
        return result;
    }

    public T getOne(String[] ch, Object[] val) {
        T result;
        int i = 0, j = 1;
        StringBuilder query = new StringBuilder("SELECT d FROM " + entityClass.getSimpleName() + " d WHERE ");
        for (String st : ch) {
            query.append(" d.").append(st).append(" =:").append(st);
            if (ch.length != j) {
                query.append(" AND ");
            }
            j++;
        }
        Query q = getEntityManager().createQuery(query.toString());
        for (String st : ch) {
            q.setParameter(st, val[i]);
            i++;
        }
        try {
            result = (T) q.getSingleResult();
        } catch (NoResultException ex) {
            result = null;
        }
        return result;
    }

    public boolean contains(T en) {
        return getEntityManager().contains(en);
    }

    //récupération par jointure
    public List<T> loadEntity(String requeteLibre) {
        Query q = getEntityManager().createQuery(requeteLibre);
        q.setFlushMode(FlushModeType.COMMIT);
        return q.getResultList();
    }

    public List<T> loadEntity(String requeteLibre, String[] champ, Object[] val) {
        Query q = getEntityManager().createQuery(requeteLibre);
        int i = 0;
        for (String st : champ) {
            q.setParameter(st, val[i]);
            i++;
        }
        return q.getResultList();
    }

    public List<T> loadEntity(String requeteLibre, String[] champ, Object[] val, int debut, int nbResult) {
        Query q = getEntityManager().createQuery(requeteLibre);
        int i = 0;
        for (String st : champ) {
            q.setParameter(st, val[i]);
            i++;
        }
        q.setFirstResult(debut);
        q.setMaxResults(nbResult);
        return q.getResultList();
    }

    public Object loadObjectByEntity(String querie, String[] champ, Object[] val) {
        getEntityManager().clear();
        Query qr = getEntityManager().createQuery(querie);
        int i = 0;
        for (String st : champ) {
            qr.setParameter(st, val[i]);
            i++;
        }
        qr.setMaxResults(1);
        try {
            return qr.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Object loadObjectBySqlQuery(String querie, Options[] lp) {
        getEntityManager().clear();
        Query qr = getEntityManager().createNativeQuery(querie);
        int i = 0;
        for (Options o : lp) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        qr.setMaxResults(1);
        try {
            return qr.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Object> loadListBySqlQuery(String querie, Options[] lp) {
        getEntityManager().clear();
        Query qr = getEntityManager().createNativeQuery(querie);
        for (Options o : lp) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            return qr.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Object> loadSerieDate(Date debut, Date fin, String periode) {
        String rq = "SELECT jour FROM generate_series(?,?,?) jour";
        getEntityManager().clear();
        Query qr = getEntityManager().createNativeQuery(rq);
        qr.setParameter(1, debut);
        qr.setParameter(2, fin);
        qr.setParameter(3, periode);
        try {
            return qr.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void requeteLibre(String rq, Options[] lp) {
        Query q = getEntityManager().createNativeQuery(rq);
        for (Options o : lp) {
            q.setParameter(o.getPosition(), o.getValeur());
        }
        q.executeUpdate();
    }

    public void updateVueNotif(long user, long notif) {
        String query = "UPDATE yvs_notification_users SET vue=true WHERE users=? AND notification=?";
        Query q = getEntityManager().createNativeQuery(query);
        q.setParameter("1", user);
        q.setParameter("2", notif);
        q.executeUpdate();
    }

//    public double getLastCout(long art, long depot, double qte) {
//        double re;
//        Query query = getEntityManager().createNativeQuery(
//                "SELECT get_cout_stock_article(?,?,?)");
//        query.setParameter(1, art).setParameter(2, depot).setParameter(3, qte);
//        Double dr = (Double) query.getSingleResult();
//        re = (dr != null) ? dr : 0;
//        return re;
//    }
    public double callFonction(String rq, Options[] lp) {
        double re;
        Query q = getEntityManager().createNativeQuery(rq);
        for (Options o : lp) {
            q.setParameter(o.getPosition(), o.getValeur());
        }
        Double dr = (Double) q.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public Boolean callFonctionBool(String rq, Options[] lp) {
        Boolean re;
        Query q = getEntityManager().createNativeQuery(rq);
        for (Options o : lp) {
            q.setParameter(o.getPosition(), o.getValeur());
        }
        re = (Boolean) q.getSingleResult();
        return re != null ? re : false;
    }

    public int callFonctionInt(String rq, Options[] lp) {
        int re;
        Query q = getEntityManager().createNativeQuery(rq);
        for (Options o : lp) {
            q.setParameter(o.getPosition(), o.getValeur());
        }
        Integer dr = (Integer) q.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public long callFonction1(String rq, Options[] lp) {
        long re;
        Query q = getEntityManager().createNativeQuery(rq);
        for (Options o : lp) {
            q.setParameter(o.getPosition(), o.getValeur());
        }
        Long dr = (Long) q.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    /**
     * PROCEDURE DE CALCUL DES ELEMENTS DE SALAIR
     *
     * @param idEmploye
     * @param date
     * @param idExercice
     * @return
     */
    //calcul le nombre de jour de congé pris jusqu'a la date donné
    public double getNbreJourCongePris(long idEmploye, Date date, long idExercice) {
        Query query = getEntityManager().createNativeQuery(
                "select public.conge_jour_pris_annuel(?,?,?)");
        query.setParameter(1, idEmploye);
        query.setParameter(2, date);
        query.setParameter(3, idExercice);
        Double dr = (Double) query.getSingleResult();
        double pris = (dr != null) ? dr : 0;
        return (pris);
    }

    //calcul le nombre de jour de permission autorisée pris par l'employé sur une période
    public double getNbreJourPermissionPris(long matricule, Date debut, long idExercice, String effet) {
        double re;
        Query query = getEntityManager().createNativeQuery(
                "select public.conge_jour_perm_pris(?,?,?,?)");
        query.setParameter(1, matricule);
        query.setParameter(2, debut);
        query.setParameter(3, idExercice);
        query.setParameter(4, effet);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    //compte le nombre congé principale du par un employé
    public double getNbreCongePrincipalDu(long idEmploye, Date date, long idExercice) {
        Query query = getEntityManager().createNativeQuery(
                "select public.conge_jour_avoir(?,?,?)");
        query.setParameter(1, idEmploye);
        query.setParameter(2, date);
        query.setParameter(3, idExercice);
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    //compte le nombre congé supplémentaire du par un employé
    public double getNbreCongeSuppDu(long idEmploye, Date date, long idExo) {
        Query query = getEntityManager().createNativeQuery(
                "select public.conge_jour_supplementaire(?,?,?)");
        query.setParameter("1", idEmploye);
        query.setParameter("2", date);
        query.setParameter("3", idExo);
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    //contrôle qu'une ocurence d'une entité est présente dans la bd
    public boolean controleOccur(T entity, String propriete, String val) {
        String requete = "Select count(*) From ";
        StringBuilder sb = new StringBuilder(requete);
        sb.append(entity.getClass().getName()).append("Where ").append(propriete).append(" = ").append(val);
        long nb = (Long) getEntityManager().createQuery(sb.toString()).getSingleResult();
        return nb == 0;
    }

    public List<Object[]> findDataPresence(long employe, Date debut, Date fin) {
        Query query = getEntityManager().createNativeQuery("SELECT * FROM public.grh_presence_durees(?,0,0,?,?)");
        query.setParameter(1, (Long) employe);
        query.setParameter(2, (Date) debut);
        query.setParameter(3, (Date) fin);
        List<Object[]> re = (List<Object[]>) query.getResultList();
        return re;
    }

    public List<YvsGrhBulletins> calculSalaire(YvsGrhOrdreCalculSalaire ord, List<YvsGrhContratEmps> contrats, boolean deleteDoublon) {
        List<YvsGrhBulletins> re = new ArrayList<>();
        YvsGrhConventionCollective cc;
        YvsParametreGrh paramGrh = (YvsParametreGrh) loadOneByNameQueries("YvsParametreGrh.findAll", new String[]{"societe"}, new Object[]{currentScte});
        for (YvsGrhContratEmps ce : contrats) {
            //lance le calcul des salaires pour les contrats rattachés à cet ordre dans la periode spécifié.
            if (ce.getStatut().equals("C") && ce.getEmploye().getPosteActif() != null) {
                //récupère la convention collective
                cc = (YvsGrhConventionCollective) loadOneByNameQueries("YvsConventionEmploye.findOneCByEmploye", new String[]{"employe"}, new Object[]{ce.getEmploye()});
                if (cc != null) {
                    ce.getEmploye().setConvention(cc);
                    managedS = new ManagedSalaire(paramGrh, (DaoInterfaceLocal) this, ord.getAuthor(), ord.getSociete(), ord);
                    YvsGrhBulletins b = managedS.createBulletin(ce, ord.getDebutMois(), ord.getFinMois());
                    if (b != null) {
                        re.add(b);
                        if (deleteDoublon) {
                            String rq = "DELETE FROM Yvs_Grh_Bulletins WHERE contrat=? AND entete=? AND id!=?";
                            requeteLibre(rq, new Options[]{new Options(ce.getId(), 1), new Options(ord.getId(), 2), new Options(b.getId(), 3)});
                        }
                    }
                }
            }
        }
        return re;
    }

    public List<YvsGrhBulletins> calculSalaire(YvsGrhOrdreCalculSalaire ord, List<YvsGrhContratEmps> contrats) {
        return calculSalaire(ord, contrats, false);
    }

    public boolean getEquilibreEnteteVente(long vente) {
        boolean re;
        Query query = getEntityManager().createNativeQuery("select public.equilibre_entete_vente(?)");
        query.setParameter(1, vente);
        Boolean dr = (Boolean) query.getSingleResult();
        re = (dr != null) ? dr : false;
        return re;
    }

    public boolean getEquilibreVente(long societe, Date dateDebut, Date dateFin) {
        boolean re;
        Query query = getEntityManager().createNativeQuery("select public.equilibre_vente(?,?,?)");
        query.setParameter(1, societe).setParameter(2, dateDebut).setParameter(3, dateFin);
        Boolean dr = (Boolean) query.getSingleResult();
        re = (dr != null) ? dr : false;
        return re;
    }

    public String getEquilibreVenteRegle(long vente) {
        Query query = getEntityManager().createNativeQuery("select public.equilibre_vente_regle(?,?)");
        query.setParameter(1, vente).setParameter(2, true);
        Object re = query.getSingleResult();
        return re != null ? re.toString() : "W";
    }

    public String getEquilibreVenteLivre(long vente) {
        Query query = getEntityManager().createNativeQuery("select public.equilibre_vente_livre(?,?)");
        query.setParameter(1, vente).setParameter(2, true);
        Object re = query.getSingleResult();
        return re != null ? re.toString() : "W";
    }

    public Map<String, String> getEquilibreVente(long vente) {
        Map<String, String> re = new HashMap<>();
        Query query = getEntityManager().createNativeQuery("SELECT * FROM public.equilibre_vente(?)");
        query.setParameter(1, vente);
        Object[] dr = (Object[]) query.getSingleResult();
        re.put("statut_livre", (dr != null ? dr.length > 0 ? dr[0].toString() : "W" : "W"));
        re.put("statut_regle", (dr != null ? dr.length > 1 ? dr[1].toString() : "W" : "W"));
        return re;
    }

    public boolean getEquilibreDocDivers(long societe, Date dateDebut, Date dateFin) {
        boolean re;
        Query query = getEntityManager().createNativeQuery("select public.equilibre_doc_divers(?,?,?)");
        query.setParameter(1, societe).setParameter(2, dateDebut).setParameter(3, dateFin);
        Boolean dr = (Boolean) query.getSingleResult();
        re = (dr != null) ? dr : false;
        return re;
    }

    public boolean getEquilibreDocDivers(long vente) {
        boolean re;
        Query query = getEntityManager().createNativeQuery("select public.equilibre_doc_divers(?)");
        query.setParameter(1, vente);
        Boolean dr = (Boolean) query.getSingleResult();
        re = (dr != null) ? dr : false;
        return re;
    }

    public boolean getEquilibreAchat(long societe, Date dateDebut, Date dateFin) {
        boolean re;
        Query query = getEntityManager().createNativeQuery("select public.equilibre_achat(?,?,?)");
        query.setParameter(1, societe).setParameter(2, dateDebut).setParameter(3, dateFin);
        Boolean dr = (Boolean) query.getSingleResult();
        re = (dr != null) ? dr : false;
        return re;
    }

    public Map<String, String> getEquilibreAchat(long vente) {
        Map<String, String> re = new HashMap<>();
        Query query = getEntityManager().createNativeQuery("SELECT * FROM public.equilibre_achat(?)");
        query.setParameter(1, vente);
        Object[] dr = (Object[]) query.getSingleResult();
        re.put("statut_livre", (dr != null ? dr.length > 0 ? dr[0].toString() : "W" : "W"));
        re.put("statut_regle", (dr != null ? dr.length > 1 ? dr[1].toString() : "W" : "W"));
        return re;
    }

    public boolean getEquilibreApprovision(long fiche) {
        boolean re;
        Query query = getEntityManager().createNativeQuery("select public.equilibre_approvision(?)");
        query.setParameter(1, fiche);
        Boolean dr = (Boolean) query.getSingleResult();
        re = (dr != null) ? dr : false;
        return re;
    }

    public boolean getEquilibreCredit(long credit) {
        boolean re;
        Query query = getEntityManager().createNativeQuery("select public.mut_equilibre_credit(?)");
        query.setParameter(1, credit);
        Boolean dr = (Boolean) query.getSingleResult();
        re = (dr != null) ? dr : false;
        return re;
    }

    public List<Object[]> getTaxeVente(long vente) {
        Query query = getEntityManager().createNativeQuery("select taxe, montant from public.get_taxe_vente(?)");
        query.setParameter(1, vente);
        List<Object[]> re = (List<Object[]>) query.getResultList();
        return re;
    }

    public List<Object[]> getTaxeAchat(long achat) {
        Query query = getEntityManager().createNativeQuery("select taxe, montant from public.get_taxe_achat(?)");
        query.setParameter(1, achat);
        List<Object[]> re = (List<Object[]>) query.getResultList();
        return re;
    }

    public double getTaxe(long art, long cat, long cpt, double rem, double qte, double prix, boolean is_vente, long fournisseur) {
        double re;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_taxe(?,?,?,?,?,?,?,?)");
        query.setParameter(1, art).setParameter(2, cat).setParameter(3, cpt).setParameter(4, rem).setParameter(5, qte).setParameter(6, prix).setParameter(7, is_vente).setParameter(8, fournisseur);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double getCommission(long art, double qte, double prix, long vend, Date date) {
        double re;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_commission(?,?,?,?,?)");
        query.setParameter(1, art).setParameter(2, qte).setParameter(3, prix).setParameter(4, vend).setParameter(5, date);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double getRistourne(long conditionnement, double qte, double prix, long clt, Date date) {
        double re;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_ristourne(?,?,?,?,?)");
        query.setParameter(1, conditionnement).setParameter(2, qte).setParameter(3, prix).setParameter(4, clt).setParameter(5, date);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double getRemiseVente(long art, double qte, double prix, long clt, long point, Date date) {
        double re;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_remise_vente(?,?,?,?,?,?)");
        query.setParameter(1, art).setParameter(2, qte).setParameter(3, prix).setParameter(4, clt).setParameter(5, point).setParameter(6, date);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double getRemiseVente(long art, double qte, double prix, long clt, long point, Date date, long unite) {
        double re;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_remise_vente(?,?,?,?,?,?,?)");
        query.setParameter(1, art).setParameter(2, qte).setParameter(3, prix).setParameter(4, clt).setParameter(5, point).setParameter(6, date).setParameter(7, unite);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double getRemiseAchat(long art, double qte, double prix, long fsseur) {
        double re;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_remise_achat(?,?,?,?)");
        query.setParameter(1, art).setParameter(2, qte).setParameter(3, prix).setParameter(4, fsseur);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double getPr(long art, long depot, long tranche, Date date) {
        double re;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_pr(?,?,?,?)");
        query.setParameter(1, art).setParameter(2, depot).setParameter(3, tranche).setParameter(4, date);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double getPr(long art, long depot, long tranche, Date date, long unite) {
        double re;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_pr(?,?,?,?,?)");
        query.setParameter(1, art).setParameter(2, depot).setParameter(3, tranche).setParameter(4, date).setParameter(5, unite);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double getPr(long agence, long art, long depot, long tranche, Date date, long unite) {
        double re;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_pr(?,?,?,?,?,?,?)");
        query.setParameter(1, agence).setParameter(2, art).setParameter(3, depot).setParameter(4, tranche).setParameter(5, date).setParameter(6, unite).setParameter(7, 0);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double getPuv(long art, double qte, double prix, long clt, long depot, long point, Date date, long unite, boolean min) {
        double re;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_puv(?,?,?,?,?,?,?,?,?)");
        query.setParameter(1, art).setParameter(2, qte).setParameter(3, prix).setParameter(4, clt).setParameter(5, depot).setParameter(6, point).setParameter(7, date).setParameter(8, unite).setParameter(9, min);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double getPuv(long art, double qte, double prix, long clt, long depot, long point, Date date, boolean min) {
        double re;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_puv(?,?,?,?,?,?,?,?)");
        query.setParameter(1, art).setParameter(2, qte).setParameter(3, prix).setParameter(4, clt).setParameter(5, depot).setParameter(6, point).setParameter(7, date).setParameter(8, min);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double getPuv(long art, double qte, double prix, long clt, long depot, long point, Date date) {
        return getPuv(art, qte, prix, clt, depot, point, date, false);
    }

    public double getPuvMin(long art, double qte, double prix, long clt, long depot, long point, Date date) {
        return getPuv(art, qte, prix, clt, depot, point, date, true);
    }

    public double getPuv(long art, double qte, double prix, long clt, long depot, long point, Date date, long unite) {
        return getPuv(art, qte, prix, clt, depot, point, date, unite, false);
    }

    public double getPuvMin(long art, double qte, double prix, long clt, long depot, long point, Date date, long unite) {
        return getPuv(art, qte, prix, clt, depot, point, date, unite, true);
    }

    public double getPua(long art, long fsseur) {
        double re;
        Query query = getEntityManager().createNativeQuery("select public.get_pua(?,?)");
        query.setParameter(1, art).setParameter(2, fsseur);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double getPua(long art, long fsseur, long unite) {
        double re;
        Query query = getEntityManager().createNativeQuery("select public.get_pua(?,?,?)");
        query.setParameter(1, art).setParameter(2, fsseur).setParameter(3, unite);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double getPua(long art, long fsseur, long depot, long unite, Date date, long agence) {
        double re;
        Query query = getEntityManager().createNativeQuery("select public.get_pua(?,?,?,?,?,?)");
        query.setParameter(1, art).setParameter(2, fsseur).setParameter(3, depot).setParameter(4, unite).setParameter(5, date).setParameter(6, agence);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double stocks(long art, long depot, Date dat) {
        double re;
        Query query = getEntityManager().createNativeQuery("select public.get_stock(?,?,?)");
        query.setParameter(1, art).setParameter(2, depot).setParameter(3, dat);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double stocks(long art, long depot, Date dat, long unite) {
        return stocks(art, 0, depot, 0, 0, dat, unite, 0);
    }

    public double stocks(long art, long depot, Date dat, long unite, long lot) {
        return stocks(art, 0, depot, 0, 0, dat, unite, lot);
    }

    public double stocks(long art, long trh, long depot, long ag, long ste, Date dat) {
        double re = 0;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_stock(?,?,?,?,?,?::date)");
        query.setParameter(1, art).setParameter(2, trh).setParameter(3, depot).setParameter(4, ag).setParameter(5, ste).setParameter(6, dat);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double stocks(long art, long trh, long depot, long ag, long ste, Date dat, long unite) {
        double re = 0;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_stock(?,?,?,?,?,?::date,?)");
        query.setParameter(1, art).setParameter(2, trh).setParameter(3, depot).setParameter(4, ag).setParameter(5, ste).setParameter(6, dat).setParameter(7, unite);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double stocks(long art, long trh, long depot, long ag, long ste, Date dat, long unite, long lot) {
        double re = 0;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_stock(?,?,?,?,?,?::date,?,?)");
        query.setParameter(1, art).setParameter(2, trh).setParameter(3, depot).setParameter(4, ag).setParameter(5, ste).setParameter(6, dat).setParameter(7, unite).setParameter(8, lot);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double stocksReel(long art, long trh, long depot, long ag, long ste, Date dat) {
        double re = 0;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_stock_reel(?,?,?,?,?,?::date)");
        query.setParameter(1, art).setParameter(2, trh).setParameter(3, depot).setParameter(4, ag).setParameter(5, ste).setParameter(6, dat);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double stocksReel(long art, long trh, long depot, long ag, long ste, Date dat, long unite, long lot) {
        double re = 0;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_stock_reel(?,?,?,?,?,?::date,?,?)");
        query.setParameter(1, art).setParameter(2, trh).setParameter(3, depot).setParameter(4, ag).setParameter(5, ste).setParameter(6, dat).setParameter(7, unite).setParameter(8, lot);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double stocksConsigner(long art, long trh, long depot, long ag, long ste, Date dat) {
        double re;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_stock_consigne(?,?,?,?,?,?::date)");
        query.setParameter(1, art).setParameter(2, trh).setParameter(3, depot).setParameter(4, ag).setParameter(5, ste).setParameter(6, dat);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double stocksConsigner(long art, long trh, long depot, long ag, long ste, Date dat, long unite, long lot) {
        double re;
        Query query = getEntityManager().createNativeQuery(
                "select public.get_stock_consigne(?,?,?,?,?,?::date,?)");
        query.setParameter(1, art).setParameter(2, trh).setParameter(3, depot).setParameter(4, ag).setParameter(5, ste).setParameter(6, dat).setParameter(7, unite).setParameter(8, lot);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double loadTTCVente(long vente) {
        String req = "select get_ttc_vente(?)";
        Query query = getEntityManager().createNativeQuery(req).setParameter(1, vente);
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    public double loadTTCAchat(long achat) {
        String req = "select get_ttc_achat(?)";
        Query query = getEntityManager().createNativeQuery(req).setParameter(1, achat);
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    public double loadNetAPayerVente(long vente) {
        String req = "select get_net_a_payer_vente(?)";
        Query query = getEntityManager().createNativeQuery(req).setParameter(1, vente);
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    public double loadNetAPayerAchat(long achat) {
        String req = "select get_net_a_payer_achat(?)";
        Query query = getEntityManager().createNativeQuery(req).setParameter(1, achat);
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    public double loadVersementAttendu(long users, Date dateDebut, Date dateFin) {
        String req = "select com_get_versement_attendu(?,?,?)";
        Query query = getEntityManager().createNativeQuery(req).setParameter(1, users).setParameter(2, dateDebut).setParameter(3, dateFin);
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    public double loadVersementReel(long users, Date dateDebut, Date dateFin) {
        String req = "SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_virement y WHERE y.caissier_source = ? AND y.statut_piece = 'P' AND y.date_paiement BETWEEN ? AND ?";
        Query query = getEntityManager().createNativeQuery(req).setParameter(1, users).setParameter(2, dateDebut).setParameter(3, dateFin);
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    public double loadCaEnteteVente(long vente) {
        String req = "select get_ca_entete_vente(?)";
        Query query = getEntityManager().createNativeQuery(req).setParameter(1, vente);
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    public double loadTaxeVente(long vente) {
        String req = "select sum(montant) from public.get_taxe_vente(?)";
        Query query = getEntityManager().createNativeQuery(req).setParameter(1, vente);
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    public double loadCaVente(long vente) {
        String req = "select get_ca_vente(?)";
        Query query = getEntityManager().createNativeQuery(req).setParameter(1, vente);
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    public double loadCaCommercial(long commercial, Date dateDebut, Date dateFin) {
        String req = "select get_ca_commercial(?,?,?)";
        Query query = getEntityManager().createNativeQuery(req).setParameter(1, commercial).setParameter(2, dateDebut).setParameter(3, dateFin);
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    public double loadCaAchat(long achat) {
        String req = "select get_ttc_achat(?)";
        Query query = getEntityManager().createNativeQuery(req).setParameter(1, achat);
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    public double loadCaVente(boolean agence, long idAgenceOrSct, Date dateDebut, Date dateFin, String statut, boolean ca) {
        return loadCaVente(agence, idAgenceOrSct, dateDebut, dateFin, statut, ca, "FV");
    }

    public double loadCaVente(boolean agence, long idAgenceOrSct, Date dateDebut, Date dateFin, String statut, boolean ca, String type) {
        String req;
        if (!agence) {
            if (statut != null ? statut.trim().length() > 0 : false) {
                if (ca) { //renvoie le CA pour toute la société
                    req = "select sum(get_ca_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.societe = ? and (e.date_entete between ? and ?) and (y.statut in (?))";
                } else {//renvoie le reste à payer pour toute la société
                    req = "select sum(get_net_a_payer_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.societe = ? and (e.date_entete between ? and ?) and (y.statut in (?))";
                }
            } else {
                if (ca) {
                    req = "select sum(get_ca_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.societe = ? and (e.date_entete between ? and ?)";
                } else {
                    req = "select sum(get_net_a_payer_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.societe = ? and (e.date_entete between ? and ?)";
                }
            }
        } else {
            if (statut != null ? statut.trim().length() > 0 : false) {
                if (ca) {
                    req = "select sum(get_ca_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.id = ? and (e.date_entete between ? and ?) and (y.statut in (?))";
                } else {
                    req = "select sum(get_net_a_payer_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.id = ? and (e.date_entete between ? and ?) and (y.statut in (?))";
                }
            } else {
                if (ca) {
                    req = "select sum(get_ca_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.id = ? and (e.date_entete between ? and ?)";
                } else {
                    req = "select sum(get_net_a_payer_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.id = ? and (e.date_entete between ? and ?)";
                }
            }
        }
        Query query;
        if (statut != null ? statut.trim().length() > 0 : false) {
            query = getEntityManager().createNativeQuery(req).setParameter(1, type).setParameter(2, idAgenceOrSct).setParameter(3, dateDebut).setParameter(4, dateFin).setParameter(5, statut).setParameter(6, statut).setParameter(7, statut);
        } else {
            query = getEntityManager().createNativeQuery(req).setParameter(1, type).setParameter(2, idAgenceOrSct).setParameter(3, dateDebut).setParameter(4, dateFin);
        }
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    public double loadCaAchat(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca) {
        return loadCaAchat(agence, id, dateDebut, dateFin, statut, ca, "FA");
    }

    public double loadCaAchat(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca, String type) {
        String req;
        if (!agence) {
            if (statut != null ? statut.trim().length() > 0 : false) {
                if (ca) {
                    req = "select sum(get_ttc_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.societe = ? and (y.date_doc between ? and ?) and (y.statut in (?))";
                } else {
                    req = "select sum(get_net_a_payer_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.societe = ? and (y.date_doc between ? and ?) and (y.statut in (?))";
                }
            } else {
                if (ca) {
                    req = "select sum(get_ttc_achat(y.id)) from yvs_com_doc_achats y inner yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.societe = ? and (y.date_doc between ? and ?)";
                } else {
                    req = "select sum(get_net_a_payer_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.societe = ? and (y.date_doc between ? and ?)";
                }
            }
        } else {
            if (statut != null ? statut.trim().length() > 0 : false) {
                if (ca) {
                    req = "select sum(get_ttc_achat(y.id)) from yvs_com_doc_achats y inner join inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.id = ? and (y.date_doc between ? and ?) and (y.statut in (?))";
                } else {
                    req = "select sum(get_net_a_payer_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.id = ? and (y.date_doc between ? and ?) and (y.statut in (?))";
                }
            } else {
                if (ca) {
                    req = "select sum(get_ttc_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.id = ? and (y.date_doc between ? and ?)";
                } else {
                    req = "select sum(get_net_a_payer_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.id = ? and (e.date_entete between ? and ?)";
                }
            }
        }
        Query query;
        if (statut != null ? statut.trim().length() > 0 : false) {
            query = getEntityManager().createNativeQuery(req).setParameter(1, type).setParameter(2, id).setParameter(3, dateDebut).setParameter(4, dateFin).setParameter(5, statut).setParameter(6, statut).setParameter(7, statut);
        } else {
            query = getEntityManager().createNativeQuery(req).setParameter(1, type).setParameter(2, id).setParameter(3, dateDebut).setParameter(4, dateFin);
        }
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    public double loadCaRVente(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca) {
        return loadCaRVente(agence, id, dateDebut, dateFin, statut, ca, "FV");
    }

    public double loadCaRVente(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca, String type) {
        String req;
        if (!agence) {
            if (statut != null ? statut.trim().length() > 0 : false) {
                if (ca) {
                    req = "select sum(get_ca_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.societe = ? and (e.date_entete between ? and ?) and (y.statut_regle in (?))";
                } else {
                    req = "select sum(get_net_a_payer_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.societe = ? and (e.date_entete between ? and ?) and (y.statut_regle in (?))";
                }
            } else {
                if (ca) {
                    req = "select sum(get_ca_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.societe = ? and (e.date_entete between ? and ?)";
                } else {
                    req = "select sum(get_net_a_payer_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.societe = ? and (e.date_entete between ? and ?)";
                }
            }
        } else {
            if (statut != null ? statut.trim().length() > 0 : false) {
                if (ca) {
                    req = "select sum(get_ca_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.id = ? and (e.date_entete between ? and ?) and (y.statut_regle in (?))";
                } else {
                    req = "select sum(get_net_a_payer_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.id = ? and (e.date_entete between ? and ?) and (y.statut_regle in (?))";
                }
            } else {
                if (ca) {
                    req = "select sum(get_ca_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.id = ? and (e.date_entete between ? and ?)";
                } else {
                    req = "select sum(get_net_a_payer_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.id = ? and (e.date_entete between ? and ?)";
                }
            }
        }
        Query query;
        if (statut != null ? statut.trim().length() > 0 : false) {
            query = getEntityManager().createNativeQuery(req).setParameter(1, type).setParameter(2, id).setParameter(3, dateDebut).setParameter(4, dateFin).setParameter(5, statut).setParameter(6, statut).setParameter(7, statut);
        } else {
            query = getEntityManager().createNativeQuery(req).setParameter(1, type).setParameter(2, id).setParameter(3, dateDebut).setParameter(4, dateFin);
        }
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    public double loadCaRAchat(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca) {
        return loadCaRAchat(agence, id, dateDebut, dateFin, statut, ca, "FA");
    }

    public double loadCaRAchat(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca, String type) {
        String req;
        if (!agence) {
            if (statut != null ? statut.trim().length() > 0 : false) {
                if (ca) {
                    req = "select sum(get_ttc_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.societe = ? and (y.date_doc between ? and ?) and (y.statut_regle in (?))";
                } else {
                    req = "select sum(get_net_a_payer_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.societe = ? and (y.date_doc between ? and ?) and (y.statut_regle in (?))";
                }
            } else {
                if (ca) {
                    req = "select sum(get_ttc_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.societe = ? and (y.date_doc between ? and ?)";
                } else {
                    req = "select sum(get_net_a_payer_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.societe = ? and (y.date_doc between ? and ?)";
                }
            }
        } else {
            if (statut != null ? statut.trim().length() > 0 : false) {
                if (ca) {
                    req = "select sum(get_ttc_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.id = ? and (y.date_doc between ? and ?) and (y.statut_regle in (?))";
                } else {
                    req = "select sum(get_net_a_payer_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.id = ? and (y.date_doc between ? and ?) and (y.statut_regle in (?))";
                }
            } else {
                if (ca) {
                    req = "select sum(get_ttc_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.id = ? and (y.date_doc between ? and ?)";
                } else {
                    req = "select sum(get_net_a_payer_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.id = ? and (y.date_doc between ? and ?)";
                }
            }
        }
        Query query;
        if (statut != null ? statut.trim().length() > 0 : false) {
            query = getEntityManager().createNativeQuery(req).setParameter(1, type).setParameter(2, id).setParameter(3, dateDebut).setParameter(4, dateFin).setParameter(5, statut).setParameter(6, statut).setParameter(7, statut);
        } else {
            query = getEntityManager().createNativeQuery(req).setParameter(1, type).setParameter(2, id).setParameter(3, dateDebut).setParameter(4, dateFin);
        }
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    public double loadCaLVente(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca) {
        return loadCaLVente(agence, id, dateDebut, dateFin, statut, ca, "FV");
    }

    public double loadCaLVente(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca, String type) {
        String req;
        if (!agence) {
            if (statut != null ? statut.trim().length() > 0 : false) {
                if (ca) {
                    req = "select sum(get_ca_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.societe = ? and (e.date_entete between ? and ?) and (y.statut_livre in (?))";
                } else {
                    req = "select sum(get_net_a_payer_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.societe = ? and (e.date_entete between ? and ?) and (y.statut_livre in (?))";
                }
            } else {
                if (ca) {
                    req = "select sum(get_ca_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.societe = ? and (e.date_entete between ? and ?)";
                } else {
                    req = "select sum(get_net_a_payer_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.societe = ? and (e.date_entete between ? and ?)";
                }
            }
        } else {
            if (statut != null ? statut.trim().length() > 0 : false) {
                if (ca) {
                    req = "select sum(get_ca_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.id = ? and (e.date_entete between ? and ?) and (y.statut_livre in (?))";
                } else {
                    req = "select sum(get_net_a_payer_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.id = ? and (e.date_entete between ? and ?) and (y.statut_livre in (?))";
                }
            } else {
                if (ca) {
                    req = "select sum(get_ca_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.id = ? and (e.date_entete between ? and ?)";
                } else {
                    req = "select sum(get_net_a_payer_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
                            + " inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
                            + " inner join yvs_com_creneau_point c on c.id = u.creneau_point"
                            + " inner join yvs_base_point_vente d on d.id = c.point"
                            + " inner join yvs_agences a on a.id = d.agence"
                            + " where y.type_doc = ? and a.id = ? and (e.date_entete between ? and ?)";
                }
            }
        }
        Query query;
        if (statut != null ? statut.trim().length() > 0 : false) {
            query = getEntityManager().createNativeQuery(req).setParameter(1, type).setParameter(2, id).setParameter(3, dateDebut).setParameter(4, dateFin).setParameter(5, statut).setParameter(6, statut).setParameter(7, statut);
        } else {
            query = getEntityManager().createNativeQuery(req).setParameter(1, type).setParameter(2, id).setParameter(3, dateDebut).setParameter(4, dateFin);
        }
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    public double loadCaLAchat(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca) {
        return loadCaLAchat(agence, id, dateDebut, dateFin, statut, ca, "FV");
    }

    public double loadCaLAchat(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca, String type) {
        String req;
        if (!agence) {
            if (statut != null ? statut.trim().length() > 0 : false) {
                if (ca) {
                    req = "select sum(get_ttc_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.societe = ? and (y.date_doc between ? and ?) and (y.statut_livre in (?))";
                } else {
                    req = "select sum(get_net_a_payer_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.societe = ? and (y.date_doc between ? and ?) and (y.statut_livre in (?))";
                }
            } else {
                if (ca) {
                    req = "select sum(get_ttc_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.societe = ? and (y.date_doc between ? and ?)";
                } else {
                    req = "select sum(get_net_a_payer_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.societe = ? and (y.date_doc between ? and ?)";
                }
            }
        } else {
            if (statut != null ? statut.trim().length() > 0 : false) {
                if (ca) {
                    req = "select sum(get_ttc_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.id = ? and (y.date_doc between ? and ?) and (y.statut_livre in (?))";
                } else {
                    req = "select sum(get_net_a_payer_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.id = ? and (y.date_doc between ? and ?) and (y.statut_livre in (?))";
                }
            } else {
                if (ca) {
                    req = "select sum(get_ttc_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.id = ? and (y.date_doc between ? and ?)";
                } else {
                    req = "select sum(get_net_a_payer_achat(y.id)) from yvs_com_doc_achats y inner join yvs_agences a on a.id = y.agence"
                            + " where y.type_doc = ? and a.id = ? and (y.date_doc between ? and ?)";
                }
            }
        }
        Query query;
        if (statut != null ? statut.trim().length() > 0 : false) {
            query = getEntityManager().createNativeQuery(req).setParameter(1, type).setParameter(2, id).setParameter(3, dateDebut).setParameter(4, dateFin).setParameter(5, statut).setParameter(6, statut).setParameter(7, statut);
        } else {
            query = getEntityManager().createNativeQuery(req).setParameter(1, type).setParameter(2, id).setParameter(3, dateDebut).setParameter(4, dateFin);
        }
        Double dr = (Double) query.getSingleResult();
        return (dr != null) ? dr : 0;
    }

    public double getTotalCaisse(long societe, long caisse, long mode, String mouvement, String type, Character statut, Date date) {
        return getTotalCaisse(societe, caisse, mode, "", mouvement, type, statut, date);
    }

    public double getTotalCaisse(long caisse, long mode, String mouvement, String type, Character statut, Date date) {
        return getTotalCaisse(0, caisse, mode, "", mouvement, type, statut, date);
    }

    public double getTotalCaisse(long societe, long caisse, long mode, String table, String mouvement, String type, Character statut, Date date) {
        Query query = getEntityManager().createNativeQuery("select public.compta_total_caisse(?,?,?,?,?,?,?,?)");
        query.setParameter(1, societe).setParameter(2, caisse).setParameter(3, mode).setParameter(4, table).setParameter(5, mouvement).setParameter(6, type).setParameter(7, statut).setParameter(8, date);
        Double dr = (Double) query.getSingleResult();
        return ((dr != null) ? dr : 0);
    }

    public double getDepenseCaisse(long caisse, String table, Character statut) {
        return getTotalCaisse(0, caisse, 0, table, "D", "ESPECE", statut, new Date());
    }

    public double getRecetteCaisse(long caisse, String table, Character statut) {
        return getTotalCaisse(0, caisse, 0, table, "R", "ESPECE", statut, new Date());
    }

    public double getSoldeCaisse(long caisse, String table, Character statut) {
        return getTotalCaisse(0, caisse, 0, table, "", "ESPECE,BANQUE", statut, new Date());
    }

    public double getRetraitCaisseMut(long caisse, String table, Character statut) {
        double re;
        Query query = getEntityManager().createNativeQuery("select public.mut_total_caisse(?,?,?,?)");
        query.setParameter(1, caisse).setParameter(2, table).setParameter(3, "R").setParameter(4, statut);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double getDepotCaisseMut(long caisse, String table, Character statut) {
        double re;
        Query query = getEntityManager().createNativeQuery("select public.mut_total_caisse(?,?,?,?)");
        query.setParameter(1, caisse).setParameter(2, table).setParameter(3, "D").setParameter(4, statut);
        Double dr = (Double) query.getSingleResult();
        re = (dr != null) ? dr : 0;
        return re;
    }

    public double getSoldeCaisseMut(long caisse, String table, Character statut) {
        return getDepotCaisseMut(caisse, table, statut) - getRetraitCaisseMut(caisse, table, statut);
    }

    public double getSoldeCaisseMut(Date debut, Date fin) {
        String reqInteret = "SELECT COALESCE((SELECT sum(m.interet) FROM yvs_mut_mensualite m "
                + "INNER JOIN  yvs_mut_echellonage e ON e.id=m.echellonage "
                + "INNER JOIN  yvs_mut_credit c ON c.id=e.credit "
                + "WHERE e.credit_retains_interet IS true AND c.statut_paiement='P' AND c.etat='V' AND c.date_effet BETWEEN ? and ?),0)";
        String reqInteret2 = "SELECT COALESCE((SELECT sum(m.interet) FROM  yvs_mut_reglement_mensualite rm INNER JOIN yvs_mut_mensualite m ON m.id=rm.mensualite "
                + "INNER JOIN  yvs_mut_echellonage e ON e.id=m.echellonage "
                + "INNER JOIN  yvs_mut_credit c ON c.id=e.credit "
                + "WHERE e.credit_retains_interet IS FALSE AND c.etat='V' "
                + "AND rm.date_reglement BETWEEN ? AND ? AND m.etat='R'),0)";
        String reqSolde = "SELECT ((COALESCE((SELECT sum(m.montant) FROM  yvs_mut_mouvement_caisse m "
                + "WHERE m.in_solde_caisse IS TRUE "
                + "AND m.date_mvt BETWEEN ? AND ? AND m.statut_piece='P' AND m.mouvement='D'),0))- "
                + "(COALESCE((SELECT sum(m.montant) FROM  yvs_mut_mouvement_caisse m "
                + "WHERE m.in_solde_caisse IS TRUE "
                + "AND m.date_mvt BETWEEN ? AND ? AND m.statut_piece='P' AND m.mouvement='R'),0))) AS solde ";

        Query query = getEntityManager().createNativeQuery(reqInteret);
        query.setParameter(1, debut);
        query.setParameter(2, fin);
        Double solde1 = (Double) query.getSingleResult();
        query = getEntityManager().createNativeQuery(reqInteret2);
        query.setParameter(1, debut);
        query.setParameter(2, fin);
        Double solde2 = (Double) query.getSingleResult();
        query = getEntityManager().createNativeQuery(reqSolde);
        query.setParameter(1, debut);
        query.setParameter(2, fin);
        query.setParameter(3, debut);
        query.setParameter(4, fin);
        Double solde3 = (Double) query.getSingleResult();
        return ((solde1 != null ? solde1 : 0) + (solde2 != null ? solde2 : 0) + (solde3 != null ? solde3 : 0));
    }

    public double getResteALivrer(long article, long depot, Date date, long conditionnement) {
        Double re;
        String rq = "SELECT((COALESCE"
                + "((select sum(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on a.id=c.article "
                + "inner join yvs_com_doc_ventes d on d.id=c.doc_vente  inner join yvs_com_entete_doc_vente en on en.id=d.entete_doc "
                + "where d.type_doc='FV' and d.statut_livre != 'L' and d.statut='V' and en.date_entete<=? AND c.article=? AND d.depot_livrer=? AND c.conditionnement = ? limit 1),0)) "
                + "-"
                + "(COALESCE("
                + "(select sum(c1.quantite) from yvs_com_contenu_doc_vente c1 inner join yvs_base_articles a1 on a1.id=c1.article "
                + "inner join yvs_com_doc_ventes d1 on d1.id=c1.doc_vente "
                + "where d1.type_doc='BLV' and d1.statut='V' and d1.date_livraison<=? AND c1.article=? AND d1.depot_livrer=? AND c1.conditionnement = ? limit 1),0))) ";
        re = (Double) getEntityManager().createNativeQuery(rq)
                .setParameter(1, date)
                .setParameter(2, article)
                .setParameter(3, depot)
                .setParameter(4, conditionnement)
                .setParameter(5, date)
                .setParameter(6, article)
                .setParameter(7, depot)
                .setParameter(8, conditionnement)
                .getSingleResult();
        return re != null ? re > 0 ? re : 0 : 0;
    }

    public String controleStock(long article, long conditionnement, long depot, long tranche, double newQte, double oldQte, String action, String mouvement, Date date, long lot) {
        return controleStock(article, conditionnement, conditionnement, depot, tranche, newQte, oldQte, action, mouvement, date, lot);
    }

    public String controleStock(long article, long conditionnement, long oldConditionnement, long depot, long tranche, double newQte, double oldQte, String action, String mouvement, Date date, long lot) {
        String query = "SELECT COALESCE(y.suivi_stock, true) FROM yvs_base_article_depot y WHERE y.article = ? AND y.depot = ?";
        Boolean suivi = (Boolean) loadObjectBySqlQuery(query, new Options[]{new Options(article, 1), new Options(depot, 2)});
        suivi = suivi != null ? suivi : true;
        if (!suivi || (newQte == oldQte && action.equals("UPDATE"))) {
            return null;
        }
        if (oldConditionnement != conditionnement) {
            return "NULL";
        }
        query = "SELECT jour::date FROM generate_series(?::date, current_date, interval '1 day') as jour WHERE round((get_stock(?, ?, ?, 0, 0, jour::date, ?, ?)- ?)::decimal, 10) < 0 limit 1";
        Date re;
        switch (action) {
            case "INSERT":
                switch (mouvement) {
                    case "E":
                        //pas de contrôle necessaire
                        return null;
                    case "S":
                        re = (Date) loadObjectBySqlQuery(query, new yvs.dao.Options[]{new yvs.dao.Options(date, 1), new yvs.dao.Options(article, 2),
                            new yvs.dao.Options(tranche, 3), new yvs.dao.Options(depot, 4),
                            new yvs.dao.Options(conditionnement, 5), new yvs.dao.Options(lot, 6), new yvs.dao.Options(newQte, 7)});
                        return (re != null) ? (suivi ? formatDate.format(re) : null) : null;
                }
                break;
            case "UPDATE":
                switch (mouvement) {
                    case "E":
                        if (newQte - oldQte >= 0) {//augmentation du stock
                            return null;
                        } else {//diminution du stock
                            re = (Date) loadObjectBySqlQuery(query, new yvs.dao.Options[]{new yvs.dao.Options(date, 1), new yvs.dao.Options(article, 2),
                                new yvs.dao.Options(tranche, 3), new yvs.dao.Options(depot, 4),
                                new yvs.dao.Options(conditionnement, 5), new yvs.dao.Options(lot, 6), new yvs.dao.Options((oldQte - newQte), 7)});
                            return (re != null) ? (suivi ? formatDate.format(re) : null) : null;
                        }
                    case "S":
                        if (newQte - oldQte <= 0) { //augmentation du stock
                            return null;
                        } else {                    // réduction du stock de (new-old)
                            re = (Date) loadObjectBySqlQuery(query, new yvs.dao.Options[]{new yvs.dao.Options(date, 1), new yvs.dao.Options(article, 2),
                                new yvs.dao.Options(tranche, 3), new yvs.dao.Options(depot, 4),
                                new yvs.dao.Options(conditionnement, 5), new yvs.dao.Options(lot, 6), new yvs.dao.Options((newQte - oldQte), 7)});
                            return (re != null) ? (suivi ? formatDate.format(re) : null) : null;
                        }
                }
                break;
            case "DELETE":
                switch (mouvement) {
                    case "E":
                        re = (Date) loadObjectBySqlQuery(query, new yvs.dao.Options[]{new yvs.dao.Options(date, 1), new yvs.dao.Options(article, 2),
                            new yvs.dao.Options(tranche, 3), new yvs.dao.Options(depot, 4),
                            new yvs.dao.Options(conditionnement, 5), new yvs.dao.Options(lot, 6), new yvs.dao.Options(newQte, 7)});
                        return (re != null) ? (suivi ? formatDate.format(re) : null) : null;

                    case "S":
                        return null;
                }
                break;
        }
        return null;
    }

    public Double getSoldeCompteMutualiste(long compte) {
        Double r1 = (Double) loadObjectByNameQueries("YvsMutOperationCompte.findTotalCompte", new String[]{"compte", "mouvement"}, new Object[]{new YvsMutCompte(compte), Constantes.MUT_SENS_OP_DEPOT});
        Double r2 = (Double) loadObjectByNameQueries("YvsMutOperationCompte.findTotalCompte", new String[]{"compte", "mouvement"}, new Object[]{new YvsMutCompte(compte), Constantes.MUT_SENS_OP_RETRAIT});
        return ((r1 != null) ? r1 : 0) - ((r2 != null) ? r2 : 0);
    }

    public boolean updateDataForClient(long client) {
        boolean re;
        Query query = getEntityManager().createNativeQuery("select public.com_update_all_data_for_client(?)");
        query.setParameter(1, client);
        Boolean dr = (Boolean) query.getSingleResult();
        re = (dr != null) ? dr : false;
        return re;
    }

    public boolean fusionneData(String table, long newValue, long oldValue) {
        boolean re;
        Query query = getEntityManager().createNativeQuery("select public.fusion_data_for_table(?,?,?)");
        query.setParameter(1, table).setParameter(2, newValue).setParameter(3, oldValue);
        Boolean dr = (Boolean) query.getSingleResult();
        re = (dr != null) ? dr : false;
        return re;
    }

    public boolean fusionneData(String table, long newValue, String oldValue) {
        boolean re;
        Query query = getEntityManager().createNativeQuery("select public.fusion_data_for_table(?,?,?)");
        query.setParameter(1, table).setParameter(2, newValue).setParameter(3, oldValue);
        Boolean dr = (Boolean) query.getSingleResult();
        re = (dr != null) ? dr : false;
        return re;
    }

    public String nameTiers(long id, String table, String type) {
        Query query = getEntityManager().createNativeQuery("select public.name_tiers(?,?,?)");
        query.setParameter(1, id).setParameter(2, table).setParameter(3, type);
        return (String) query.getSingleResult();
    }

    public double arrondi(long societe, double valeur) {
        try {
            double re;
            Query query = getEntityManager().createNativeQuery("select public.arrondi(?,?)");
            query.setParameter(1, societe).setParameter(2, valeur);
            Double dr = (Double) query.getSingleResult();
            re = (dr != null) ? dr : 0.0;
            return re;
        } catch (Exception ex) {
            return 0;
        }
    }

    public List<String> field(String table, String colonne, String tableLiee) {
        Query query = getEntityManager().createNativeQuery("select public.return_field(?, ?, ?)");
        query.setParameter(1, table).setParameter(2, colonne).setParameter(3, tableLiee);
        List<String> re = (List<String>) query.getResultList();
        return re;
    }

    public List<YvsComMensualiteFactureVente> generatedEcheancierReg(YvsComDocVentes y, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        double total = (loadCaVente(y.getId()) - y.getMontantMensualite());
        return generatedEcheancierReg(y, y.getModelReglement(), total, currentUser, currentScte);
    }

    public boolean generatedEcheancierReg(YvsComDocVentes y, boolean addList, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        if (y != null) {
            if (currentUser != null) {
                List<YvsComMensualiteFactureVente> list = generatedEcheancierReg(y, currentUser, currentScte);
                for (YvsComMensualiteFactureVente e : list) {
                    e = (YvsComMensualiteFactureVente) save1((T) e);
                }
                if (addList) {
                    y.setMensualites(list);
                }
            }
        }
        return true;
    }

    public List<YvsComMensualiteFactureVente> generatedEcheancierReg(YvsComDocVentes y, YvsBaseModelReglement m, double total, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        List<YvsComMensualiteFactureVente> list = new ArrayList<>();
        try {
            if (y != null) {
                if (total > 0) {
                    champ = new String[]{"facture", "statut"};
                    val = new Object[]{y, Constantes.STATUT_DOC_PAYER};
                    nameQueri = "YvsComptaCaissePieceVente.findByFactureStatutS";
                    Double a = (Double) loadObjectByNameQueries(nameQueri, champ, val);

                    Calendar c = Calendar.getInstance();
                    Date date = y.getDateValider();
                    if (y.getEnteteDoc() != null ? y.getEnteteDoc().getDateEntete() == null : true) {
                        y = (YvsComDocVentes) loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{y.getId()});
                    }
                    if (y.getEnteteDoc() != null ? y.getEnteteDoc().getDateEntete() != null : false) {
                        date = y.getEnteteDoc().getDateEntete();
                    }
                    c.setTime(date);
                    //Comptabilise echéancier
                    YvsComMensualiteFactureVente e;
                    if (m != null ? m.getId() > 0 : false) {
                        m.setTranches((List) loadNameQueries("YvsBaseTrancheReglement.findByModel", new String[]{"model"}, new Object[]{m}));
                        if (m.getTranches() != null ? !m.getTranches().isEmpty() : false) {
                            double somme = 0;
                            YvsBaseTrancheReglement t;
                            for (int i = 0; i < m.getTranches().size(); i++) {
                                t = m.getTranches().get(i);
                                double montant = fonction.arrondi(total * t.getTaux() / 100, (DaoInterfaceLocal) this);
                                c.add(Calendar.DAY_OF_MONTH, t.getIntervalJour());

                                e = fonction.definedAvance(list, a, montant);
                                e.setAuthor(currentUser);
                                e.setDateMensualite(c.getTime());
                                e.setFacture(y);
                                e.setModeReglement(t.getMode());
                                e.setDateSave(new Date());
                                e.setDateUpdate(new Date());
                                list.add(e);

                                somme += montant;
                            }
                            if (total > somme) {
                                double montant = total - somme;
                                e = fonction.definedAvance(list, a, montant);
                                e.setAuthor(currentUser);
                                e.setDateMensualite(c.getTime());
                                e.setFacture(y);
                                e.setModeReglement(null);
                                e.setDateSave(new Date());
                                e.setDateUpdate(new Date());
                                list.add(e);
                            }
                        } else {
                            e = fonction.definedAvance(list, a, total);
                            e.setAuthor(currentUser);
                            e.setDateMensualite(c.getTime());
                            e.setFacture(y);
                            e.setModeReglement(null);
                            e.setDateSave(new Date());
                            e.setDateUpdate(new Date());
                            list.add(e);
                        }
                    } else {
                        e = fonction.definedAvance(list, a, total);
                        e.setAuthor(currentUser);
                        e.setDateMensualite(c.getTime());
                        e.setFacture(y);
                        e.setModeReglement(null);
                        e.setDateSave(new Date());
                        e.setDateUpdate(new Date());
                        list.add(e);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public double setMontantTotalDoc(YvsComDocAchats doc, long societe) {
        champ = new String[]{"docAchat"};
        val = new Object[]{doc};
        List<YvsComContenuDocAchat> lc = (List) loadNameQueries("YvsComContenuDocAchat.findByDocAchat", champ, val);
        return setMontantTotalDoc(doc, lc, societe);
    }

    public double setMontantTotalDoc(YvsComDocAchats doc, List<YvsComContenuDocAchat> lc, long societe) {
        doc.setMontantRemise(0);
        doc.setMontantTaxe(0);
        doc.setMontantHT(0);
        doc.setMontantTTC(0);
        doc.setMontantCS(0);
        doc.setMontantAvance(0.0);
        doc.setMontantTaxeR(0);
        doc.setMontantNetApayer(0);

        if (lc != null ? !lc.isEmpty() : false) {
            for (YvsComContenuDocAchat c : lc) {
                doc.setMontantTTC(doc.getMontantTTC() + (c.getPrixTotal()));
                doc.setMontantTaxe(doc.getMontantTaxe() + c.getTaxe());
                doc.setMontantRemise(doc.getMontantRemise() + c.getRemise());
                doc.setMontantTaxeR(doc.getMontantTaxeR() + ((c.getArticle().getPuaTtc()) ? (c.getTaxe()) : 0));
            }
        }

        champ = new String[]{"docAchat", "sens"};
        val = new Object[]{doc, true};
        Double p = (Double) loadObjectByNameQueries("YvsComCoutSupDocAchat.findSumByDocAchat", champ, val);
        val = new Object[]{doc, false};
        Double m = (Double) loadObjectByNameQueries("YvsComCoutSupDocAchat.findSumByDocAchat", champ, val);
        double s = (p != null ? p : 0) - (m != null ? m : 0);
        doc.setMontantCS(s);

        champ = new String[]{"facture", "statut"};
        val = new Object[]{doc, Constantes.STATUT_DOC_PAYER};
        Double r = (Double) loadObjectByNameQueries("YvsComptaCaissePieceAchat.findByFactureStatutS", champ, val);
        doc.setMontantAvance(r != null ? r : 0);

        doc.setMontantRemise(doc.getMontantRemise());
        doc.setMontantTaxe(doc.getMontantTaxe());
        doc.setMontantHT(doc.getMontantHT());
        doc.setMontantTTC(doc.getMontantTTC());
        doc.setMontantCS(doc.getMontantCS());
        doc.setMontantAvance(doc.getMontantAvance());
        doc.setMontantTaxeR(doc.getMontantTaxeR());
        doc.setMontantNetApayer(doc.getMontantNetApayer());
        return doc.getMontantTotal();
    }

    public double setMontantTotalDoc(YvsComDocVentes doc, long societe) {
        champ = new String[]{"docVente"};
        val = new Object[]{doc};
        List<YvsComContenuDocVente> lc = (List) loadNameQueries("YvsComContenuDocVente.findByDocVente", champ, val);
        return setMontantTotalDoc(doc, lc, societe);
    }

    public double setMontantTotalDoc(YvsComDocVentes doc, List<YvsComContenuDocVente> lc, long societe) {

        doc.setMontantRemise(0);
        doc.setMontantTaxe(0);
        doc.setMontantRistourne(0);
        doc.setMontantCommission(0);
        doc.setMontantHT(0);
        doc.setMontantTTC(0);
        doc.setMontantRemises(0);
        doc.setMontantCS(0);
        doc.setMontantAvance(0.0);
        doc.setMontantTaxeR(0);
        doc.setMontantResteApayer(0);
        doc.setMontantPlanifier(0);
        if (lc != null ? !lc.isEmpty() : false) {
            for (YvsComContenuDocVente c : lc) {
                doc.setMontantRemise(doc.getMontantRemise() + c.getRemise());
                doc.setMontantRistourne(doc.getMontantRistourne() + c.getRistourne());
                doc.setMontantCommission(doc.getMontantCommission() + c.getComission());
                doc.setMontantTTC(doc.getMontantTTC() + c.getPrixTotal());
                doc.setMontantTaxe(doc.getMontantTaxe() + c.getTaxe());
                doc.setMontantTaxeR(doc.getMontantTaxeR() + ((c.getArticle().getPuvTtc()) ? (c.getTaxe()) : 0));
            }
        }

        champ = new String[]{"facture", "statut"};
        val = new Object[]{doc, Constantes.STATUT_DOC_PAYER};
        nameQueri = "YvsComptaCaissePieceVente.findByFactureStatutS";
        Double a = (Double) loadObjectByNameQueries(nameQueri, champ, val);
        doc.setMontantAvance(a != null ? a : 0);
        val = new Object[]{doc, Constantes.STATUT_DOC_SUSPENDU};
        nameQueri = "YvsComptaCaissePieceVente.findByFactureStatutSDiff";
        a = (Double) loadObjectByNameQueries(nameQueri, champ, val);
        doc.setMontantPlanifier(a != null ? a : 0);

        champ = new String[]{"docVente", "sens"};
        val = new Object[]{doc, true};
        Double p = (Double) loadObjectByNameQueries("YvsComCoutSupDocVente.findSumByDocVente", champ, val);
        val = new Object[]{doc, false};
        Double m = (Double) loadObjectByNameQueries("YvsComCoutSupDocVente.findSumByDocVente", champ, val);
        double s = (p != null ? p : 0) - (m != null ? m : 0);
        doc.setMontantCS(s);

        doc.setMontantRemise(arrondi(societe, doc.getMontantRemise()));
        doc.setMontantTaxe(arrondi(societe, doc.getMontantTaxe()));
        doc.setMontantRistourne(arrondi(societe, doc.getMontantRistourne()));
        doc.setMontantCommission(arrondi(societe, doc.getMontantCommission()));
        doc.setMontantHT(arrondi(societe, doc.getMontantHT()));
        doc.setMontantTTC(arrondi(societe, doc.getMontantTTC()));
        doc.setMontantRemises(arrondi(societe, doc.getMontantRemises()));
        doc.setMontantCS(arrondi(societe, doc.getMontantCS()));
        doc.setMontantAvance(arrondi(societe, doc.getMontantAvance()));
        doc.setMontantTaxeR(arrondi(societe, doc.getMontantTaxeR()));
        doc.setMontantResteApayer(arrondi(societe, doc.getMontantResteApayer()));
        return doc.getMontantTotal();
    }

    public YvsGrhMissions setMontantTotalMission(YvsGrhMissions mission) {
        //total des bons planifié rattaché à la mission
        Double montant = (Double) loadObjectByNameQueries("YvsComptaJustifBonMission.getTotalBon", new String[]{"mission", "statut"}, new Object[]{mission, Constantes.ETAT_ANNULE});
        mission.setTotalBon(montant != null ? montant : 0d);
//total des bons déjà payé rattaché à la mission
        montant = (Double) loadObjectByNameQueries("YvsComptaJustifBonMission.getTotalBonPaye", new String[]{"mission", "statut", "statutP"}, new Object[]{mission, Constantes.ETAT_ANNULE, Constantes.ETAT_REGLE});
        mission.setTotalBonPaye(montant != null ? montant : 0d);
        //total des pièce de caisse mission
        montant = (Double) loadObjectByNameQueries("YvsComptaCaissePieceMission.findTotalPieceByMission", new String[]{"mission", "statut"}, new Object[]{mission, Constantes.STATUT_DOC_ANNULE});
        mission.setTotalPiece(montant != null ? montant : 0d);
        //total des pièce de caisse mission dejà payé
        montant = (Double) loadObjectByNameQueries("YvsComptaCaissePieceMission.findTotalPiecePayeByMission", new String[]{"mission", "statut"}, new Object[]{mission, Constantes.STATUT_DOC_PAYER});
        mission.setTotalFraisMission(montant != null ? montant : 0d);
        mission.setTotalRegle(mission.getTotalBonPaye() + mission.getTotalFraisMission());
        montant = (Double) loadObjectByNameQueries("YvsGrhFraisMission.sumByMission", new String[]{"mission"}, new Object[]{mission});
        mission.setTotalFraisMission(montant != null ? montant : 0d);
        mission.setTotalBon(mission.getTotalBon());
        mission.setTotalFraisMission(mission.getTotalFraisMission());
        mission.setTotalPiece(mission.getTotalPiece());
        mission.setTotalReste(mission.getTotalFraisMission() - mission.getTotalRegle());
        mission.setTotalResteAPlanifier(mission.getTotalFraisMission() - mission.getTotalPiece() - mission.getTotalBon());
        mission.setTotalBonPaye(mission.getTotalBonPaye());
        return mission;
    }

    public List<YvsComptaContentJournal> buildContentJournal(long id, String table, YvsSocietes societe, String agence) {
        List<YvsComptaContentJournal> list = new ArrayList<>();
        getEntityManager().clear();
        String query = "select * from public.yvs_compta_content_journal(?,?,?,?,?)";
        Options[] param = new Options[]{new Options(societe.getId(), 1), new Options(agence, 2), new Options(id, 3), new Options(table, 4), new Options(true, 5)};
        Query qr = getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        YvsComptaContentJournal line;
        for (Object o : qr.getResultList()) {
            line = fonction.convertTabToContenu((Object[]) o, id, (DaoInterfaceLocal) this);
            if (!fonction.controleContenu(line, (DaoInterfaceLocal) this)) {
                return null;
            }
            int idx = list.indexOf(line);
            if (idx > -1) {
                list.get(idx).getAnalytiques().addAll(line.getAnalytiques());
            } else {
                list.add(line);
            }
        }
        return list;
    }

    public String genererReference(String element, Date date, long id, YvsSocietes societes, YvsAgences agences) {
        YvsBaseModeleReference model = fonction.rechercheModeleReference(element, societes, (DaoInterfaceLocal) this);
        if ((model != null) ? model.getId() > 0 : false) {
            return fonction.getReferenceElement(model, date, id, currentUser, (DaoInterfaceLocal) this, agences, societes);
        } else {
            setRESULT("Cette ressource n'a pas de model de référence! Veuillez en créer");
            return "";
        }
    }

//    public String genererReference(String element, Date date, long id) {
//        return genererReference(element, date, id, currentScte);
//    }
//
//    public String genererReference(String element, Date date, long id, YvsSocietes societes) {
//        YvsBaseModeleReference model = fonction.rechercheModeleReference(element, societes, dao);
//        if ((model != null) ? model.getId() > 0 : false) {
//            return fonction.getReferenceElement(model, date, id, societes, dao);
//        } else {
//            dao.setRESULT("Cette ressource n'a pas de model de référence! Veuillez en créer");
//            return "";
//        }
//    }
    public void loadInfos(YvsSocietes currentScte, YvsAgences currentAgence, YvsUsersAgence currentUser, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint, YvsBaseExercice currentExo) {
        this.currentScte = currentScte;
        this.currentAgence = currentAgence;
        this.currentUser = currentUser;
        this.currentDepot = currentDepot;
        this.currentPoint = currentPoint;
        this.currentExo = currentExo;

        fonction.loadInfos(currentScte, currentAgence, currentUser, currentDepot, currentPoint, currentExo);
    }

    public boolean isComptabilise(long id, String table) {
        return isComptabilise(id, table, true);
    }

    public boolean isComptabilise(long id, String table, boolean all) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        return isComptabilise(ids, table, all);
    }

    public boolean isComptabilise(List<Long> ids, String table) {
        return isComptabilise(ids, table, true);
    }

    public boolean isComptabilise(List<Long> ids, String table, boolean all) {
        return isComptabilise(ids, table, all, null);
    }

    public boolean isComptabilise(Long id, String table, boolean all, Character source) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        return isComptabilise(ids, table, all, source);
    }

    public boolean isComptabilise(List<Long> ids, String table, boolean all, Character source) {
        if (all) {
            switch (table) {
                case Constantes.SCR_SALAIRE: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalEnteteBulletin.findByEntetes";
                    YvsComptaContentJournalEnteteBulletin y = (YvsComptaContentJournalEnteteBulletin) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getEntete().getComptabilise()) {
                            y.getEntete().setComptabilise(true);
                            update((T) y.getEntete());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_BULLETIN: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalBulletin.findByBulletins";
                    YvsComptaContentJournalBulletin y = (YvsComptaContentJournalBulletin) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getBulletin().getComptabilise()) {
                            y.getBulletin().setComptabilise(true);
                            update((T) y.getBulletin());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_HEAD_VENTE: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalEnteteFactureVente.findByEntetes";
                    YvsComptaContentJournalEnteteFactureVente y = (YvsComptaContentJournalEnteteFactureVente) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getEntete().getComptabilise()) {
                            y.getEntete().setComptabilise(true);
                            update((T) y.getEntete());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_AVOIR_VENTE:
                case Constantes.SCR_VENTE: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalFactureVente.findByFactures";
                    YvsComptaContentJournalFactureVente y = (YvsComptaContentJournalFactureVente) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        //instruction de précaution pour assurer la cohérence des données    
                        if (!y.getFacture().getComptabilise()) {
                            y.getFacture().setComptabilise(true);
                            update((T) y.getFacture());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_AVOIR_ACHAT:
                case Constantes.SCR_ACHAT: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalFactureAchat.findByFactures";
                    YvsComptaContentJournalFactureAchat y = (YvsComptaContentJournalFactureAchat) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getFacture().getComptabilise()) {
                            y.getFacture().setComptabilise(true);
                            update((T) y.getFacture());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_DIVERS: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalDocDivers.findByDocDiverss";
                    YvsComptaContentJournalDocDivers y = (YvsComptaContentJournalDocDivers) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getDivers().getComptabilise()) {
                            y.getDivers().setComptabilise(true);
                            update((T) y.getDivers());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_CAISSE_VENTE: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalPieceVente.findByFactures";
                    YvsComptaContentJournalPieceVente y = (YvsComptaContentJournalPieceVente) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getReglement().getComptabilise()) {
                            y.getReglement().setComptabilise(true);
                            update((T) y.getReglement());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_CAISSE_ACHAT: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalPieceAchat.findByFactures";
                    YvsComptaContentJournalPieceAchat y = (YvsComptaContentJournalPieceAchat) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getReglement().getComptabilise()) {
                            y.getReglement().setComptabilise(true);
                            update((T) y.getReglement());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_CAISSE_DIVERS: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalPieceDivers.findByFactures";
                    YvsComptaContentJournalPieceDivers y = (YvsComptaContentJournalPieceDivers) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getReglement().getComptabilise()) {
                            y.getReglement().setComptabilise(true);
                            update((T) y.getReglement());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_CAISSE_CREDIT_ACHAT: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalReglementCreditFournisseur.findByReglements";
                    YvsComptaContentJournalReglementCreditFournisseur y = (YvsComptaContentJournalReglementCreditFournisseur) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getReglement().getComptabilise()) {
                            y.getReglement().setComptabilise(true);
                            update((T) y.getReglement());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_CAISSE_CREDIT_VENTE: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalReglementCreditClient.findByReglements";
                    YvsComptaContentJournalReglementCreditClient y = (YvsComptaContentJournalReglementCreditClient) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getReglement().getComptabilise()) {
                            y.getReglement().setComptabilise(true);
                            update((T) y.getReglement());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_ACOMPTE_ACHAT: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalAcompteFournisseur.findByAcomptes";
                    YvsComptaContentJournalAcompteFournisseur y = (YvsComptaContentJournalAcompteFournisseur) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getAcompte().getComptabilise()) {
                            y.getAcompte().setComptabilise(true);
                            update((T) y.getAcompte());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_ACOMPTE_VENTE: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalAcompteClient.findByAcomptes";
                    YvsComptaContentJournalAcompteClient y = (YvsComptaContentJournalAcompteClient) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getAcompte().getComptabilise()) {
                            y.getAcompte().setComptabilise(true);
                            update((T) y.getAcompte());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_CREDIT_ACHAT: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalCreditFournisseur.findByCredits";
                    YvsComptaContentJournalCreditFournisseur y = (YvsComptaContentJournalCreditFournisseur) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getCredit().getComptabilise()) {
                            y.getCredit().setComptabilise(true);
                            update((T) y.getCredit());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_CREDIT_VENTE: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalCreditClient.findByCredits";
                    YvsComptaContentJournalCreditClient y = (YvsComptaContentJournalCreditClient) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getCredit().getComptabilise()) {
                            y.getCredit().setComptabilise(true);
                            update((T) y.getCredit());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_VIREMENT: {
                    if (source == null) {
                        source = Constantes.MOUV_CAISS_ENTREE.charAt(0);
                    }
                    if (source.equals(Constantes.MOUV_CAISS_SORTIE.charAt(0))) {
                        champ = new String[]{"ids", "source"};
                        val = new Object[]{ids, Constantes.MOUV_CAISS_SORTIE.charAt(0)};
                        nameQueri = "YvsComptaContentJournalPieceVirement.findByFacturesSource";
                    } else if (source.equals(Constantes.MOUV_CAISS_ENTREE.charAt(0))) {
                        champ = new String[]{"ids", "source"};
                        val = new Object[]{ids, Constantes.MOUV_CAISS_ENTREE.charAt(0)};
                        nameQueri = "YvsComptaContentJournalPieceVirement.findByFacturesSource";
                    }
                    YvsComptaContentJournalPieceVirement y = (YvsComptaContentJournalPieceVirement) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getReglement().getComptabilise()) {
                            update((T) y.getReglement());
                        }
                        return true;
                    }

                    break;
                }
                case Constantes.SCR_PHASE_CAISSE_VENTE: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalEtapePieceVente.findByEtapes";
                    YvsComptaContentJournalEtapePieceVente y = (YvsComptaContentJournalEtapePieceVente) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getEtape().getComptabilise()) {
                            y.getEtape().setComptabilise(true);
                            update((T) y.getEtape());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_PHASE_CAISSE_ACHAT: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalEtapePieceAchat.findByEtapes";
                    YvsComptaContentJournalEtapePieceAchat y = (YvsComptaContentJournalEtapePieceAchat) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getEtape().getComptabilise()) {
                            y.getEtape().setComptabilise(true);
                            update((T) y.getEtape());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_PHASE_CAISSE_DIVERS: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalEtapePieceDivers.findByEtapes";
                    YvsComptaContentJournalEtapePieceDivers y = (YvsComptaContentJournalEtapePieceDivers) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getEtape().getComptabilise()) {
                            y.getEtape().setComptabilise(true);
                            update((T) y.getEtape());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_PHASE_ACOMPTE_ACHAT: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalEtapeAcompteAchat.findByEtapes";
                    YvsComptaContentJournalEtapeAcompteAchat y = (YvsComptaContentJournalEtapeAcompteAchat) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getEtape().getComptabilise()) {
                            y.getEtape().setComptabilise(true);
                            update((T) y.getEtape());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_PHASE_ACOMPTE_VENTE: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalEtapeAcompteVente.findByEtapes";
                    YvsComptaContentJournalEtapeAcompteVente y = (YvsComptaContentJournalEtapeAcompteVente) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getEtape().getComptabilise()) {
                            y.getEtape().setComptabilise(true);
                            update((T) y.getEtape());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_PHASE_CREDIT_ACHAT: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalEtapeReglementCreditFournisseur.findByEtapes";
                    YvsComptaContentJournalEtapeReglementCreditFournisseur y = (YvsComptaContentJournalEtapeReglementCreditFournisseur) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getEtape().getComptabilise()) {
                            y.getEtape().setComptabilise(true);
                            update((T) y.getEtape());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_PHASE_CREDIT_VENTE: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalEtapeReglementCreditClient.findByEtapes";
                    YvsComptaContentJournalEtapeReglementCreditClient y = (YvsComptaContentJournalEtapeReglementCreditClient) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getEtape().getComptabilise()) {
                            y.getEtape().setComptabilise(true);
                            update((T) y.getEtape());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_PHASE_VIREMENT: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalEtapePieceVirement.findByEtapes";
                    YvsComptaContentJournalEtapePieceVirement y = (YvsComptaContentJournalEtapePieceVirement) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getEtape().getComptabilise()) {
                            y.getEtape().setComptabilise(true);
                            update((T) y.getEtape());
                        }
                        return true;
                    }
                    break;
                }
                case Constantes.SCR_ABONNEMENT_DIVERS: {
                    champ = new String[]{"ids"};
                    val = new Object[]{ids};
                    nameQueri = "YvsComptaContentJournalAbonnementDivers.findByAbonnements";
                    YvsComptaContentJournalAbonnementDivers y = (YvsComptaContentJournalAbonnementDivers) loadOneByNameQueries(nameQueri, champ, val);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        if (!y.getAbonnement().getComptabilise()) {
                            y.getAbonnement().setComptabilise(true);
                            update((T) y.getAbonnement());
                        }
                        return true;
                    }
                    break;
                }
            }
        }
        if (!table.equals(Constantes.SCR_VIREMENT)) {
            champ = new String[]{"ids", "table"};
            val = new Object[]{ids, table};
            nameQueri = "YvsComptaContentJournal.countPieceByExternes";
//            YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) dao.loadOneByNameQueries(nameQueri, champ, val);
            Long p = (Long) loadObjectByNameQueries(nameQueri, champ, val);
            if (p != null ? p > 0 : false) {
                return true;
            }
        }
        return false;
    }

    public YvsComptaPiecesComptable saveNewPieceComptable(Date dateDoc, YvsComptaJournaux jrn, List<YvsComptaContentJournal> contenus, YvsUsersAgence currentUser) {
        try {
            YvsBaseExercice exo = fonction.giveExerciceActif(dateDoc, (DaoInterfaceLocal) this);
            if (exo != null ? (exo.getId() != null ? exo.getId() > 0 : false) : false) {
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    String num = genererReference(Constantes.TYPE_PIECE_COMPTABLE_NAME, dateDoc, jrn.getId(), currentUser.getAgence().getSociete(), currentUser.getAgence());
                    if (num == null || num.trim().length() < 1) {
                        return null;
                    }
                    YvsComptaPiecesComptable p = new YvsComptaPiecesComptable();
                    p.setAuthor(currentUser);
                    p.setDatePiece(dateDoc);
                    p.setDateSaisie(new Date());
                    p.setExercice(exo);
                    p.setJournal(jrn);
                    p.setNumPiece(num);
                    p.setStatutPiece(Constantes.STATUT_DOC_EDITABLE);
                    p = (YvsComptaPiecesComptable) save1((T) p);
                    for (YvsComptaContentJournal c : contenus) {
                        c.setAuthor(currentUser);
                        c.setPiece(p);
                        save1((T) c);

                        List<YvsComptaContentAnalytique> lista = new ArrayList<>();
                        lista.addAll(c.getAnalytiques());
                        c.getAnalytiques().clear();
                        for (YvsComptaContentAnalytique a : lista) {
                            a.setContenu(c);
                            a = (YvsComptaContentAnalytique) save1((T) a);
                            c.getAnalytiques().add(a);
                        }
                        p.getContentsPiece().add(c);
                    }
                    return p;
                } else {
                    setRESULT("Comptabilisation impossible...car ce reglement est rattaché à une caisse qui n'a pas de journal");
                }
            } else {
                setRESULT("Comptabilisation impossible...car l'exercice à cette date n'existe pas ou n'est pas actif");
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*BEGIN WORKFLOW*/
    public String validEtapeFactureAchat(YvsComDocAchats current, YvsWorkflowValidFactureAchat etape, YvsUsersAgence currentUser) {
        try {
            int idx = current.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                if (currentUser != null) {
                    this.currentUser = currentUser;
                    this.currentAgence = currentUser.getAgence();
                    this.currentScte = currentAgence.getSociete();
                    loadInfos(currentScte, currentAgence, currentUser, currentDepot, currentPoint, currentExo);
                }
                if (etape.getEtape().getLivraisonHere()) {
                    current.setLivraisonDo(true);
                }
                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() == null) {
                    if (fonction.validerFactureAchat(current, (DaoInterfaceLocal) this)) {
                        if (currentUser != null) {
                            etape.setAuthor(currentUser);
                        }
                        etape.setEtapeValid(true);
                        etape.setEtapeActive(false);
                        etape.setDateUpdate(new Date());
                        if (current.getEtapesValidations().size() > (idx + 1)) {
                            current.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                        }
                        // dao.update(etape);
                        String requete = "UPDATE yvs_workflow_valid_facture_achat SET etape_valid = ?, author = ? , date_update = ? WHERE id = ?";
                        requeteLibre(requete, new Options[]{new Options(true, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(etape.getId(), 4)});
                        current.setEtapeValide(current.getEtapeValide() + 1);
                        update((T) current);
                        return "succes";
                    }
                } else {
                    if (currentUser != null) {
                        etape.setAuthor(currentUser);
                    }
                    etape.setEtapeValid(true);
                    etape.setEtapeActive(false);
                    etape.setDateUpdate(new Date());
                    if (current.getEtapesValidations().size() > (idx + 1)) {
                        current.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                    }
                    String requete = "UPDATE yvs_workflow_valid_facture_achat SET etape_valid = ?, author = ? , date_update = ? WHERE id = ?";
                    requeteLibre(requete, new Options[]{new Options(true, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(etape.getId(), 4)});

                    current.setStatut(Constantes.ETAT_ENCOURS);
                    if (currentUser != null) {
                        current.setAuthor(currentUser);
                    }
                    current.setDateUpdate(new Date());
                    current.setEtapeValide(current.getEtapeValide() + 1);
                    update((T) current);
                    return "succes";
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Validation Impossible";
    }

    public String annulEtapeFactureAchat(YvsComDocAchats current, YvsWorkflowValidFactureAchat etape, String motif, YvsUsersAgence currentUser) {
        try {
            //contrôle la cohérence des dates
            if (motif != null ? motif.trim().isEmpty() : true) {
                return "Vous devez précisez le motif";
            }
            int idx = current.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                if (currentUser != null) {
                    this.currentUser = currentUser;
                    this.currentAgence = currentUser.getAgence();
                    this.currentScte = currentAgence.getSociete();
                    loadInfos(currentScte, currentAgence, currentUser, currentDepot, currentPoint, currentExo);
                }
                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() != null) {
                    champ = new String[]{"etape", "facture"};
                    val = new Object[]{etape.getEtape().getEtapeSuivante(), current};
                    YvsWorkflowValidFactureAchat y = (YvsWorkflowValidFactureAchat) loadOneByNameQueries("YvsWorkflowValidFactureAchat.findByEtapeFacture", champ, val);
                    if (y != null ? (y.getId() > 0 ? y.getEtapeValid() : false) : false) {
                        return "Vous devez au préalable annuler l'étape suivante";
                    }
                }
                etape.setAuthor(currentUser);
                etape.setEtapeValid(false);
                etape.setEtapeActive(true);
                etape.setMotif(motif);
                String requete = "UPDATE yvs_workflow_valid_facture_achat SET etape_valid = ?, author = ? , date_update = ?, motif = ? WHERE id = ?";
                requeteLibre(requete, new Options[]{new Options(false, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(motif, 4), new Options(etape.getId(), 5)});

                current.setEtapeValide(current.getEtapeValide() - 1);
                current.setStatut(current.getEtapeValide() < 1 ? Constantes.ETAT_EDITABLE : Constantes.ETAT_ENCOURS);
                current.setAuthor(currentUser);
                update((T) current);
                return "succes";
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Annulation Impossible";
    }

    public String validEtapeFactureVente(YvsComDocVentes current, YvsWorkflowValidFactureVente etape, YvsUsersAgence currentUser) {
        try {
            int idx = current.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                if (currentUser != null) {
                    this.currentUser = currentUser;
                    this.currentAgence = currentUser.getAgence();
                    this.currentScte = currentAgence.getSociete();
                    loadInfos(currentScte, currentAgence, currentUser, currentDepot, currentPoint, currentExo);
                }
                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() == null) {
                    if (fonction.validerFactureVente(current, currentScte, (DaoInterfaceLocal) this)) {
                        if (currentUser != null) {
                            current.setAuthor(currentUser);
                        }
                        etape.setEtapeValid(true);
                        etape.setEtapeActive(false);
                        etape.setDateUpdate(new Date());
                        if (current.getEtapesValidations().size() > (idx + 1)) {
                            current.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                        }
                        String requete = "UPDATE yvs_workflow_valid_facture_vente SET etape_valid = ?, author = ? , date_update = ? WHERE id = ?";
                        requeteLibre(requete, new Options[]{new Options(true, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(etape.getId(), 4)});
                        current.setEtapeValide(current.getEtapeValide() + 1);
                        update((T) current);
                        return "succes";
                    }
                } else {
                    if (currentUser != null) {
                        current.setAuthor(currentUser);
                    }
                    etape.setEtapeValid(true);
                    etape.setEtapeActive(false);
                    etape.setDateUpdate(new Date());
                    if (current.getEtapesValidations().size() > (idx + 1)) {
                        current.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                    }
                    String requete = "UPDATE yvs_workflow_valid_facture_vente SET etape_valid = ?, author = ? , date_update = ? WHERE id = ?";
                    requeteLibre(requete, new Options[]{new Options(true, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(etape.getId(), 4)});

                    current.setStatut(Constantes.ETAT_ENCOURS);
                    if (currentUser != null) {
                        current.setAuthor(currentUser);
                    }
                    current.setDateUpdate(new Date());
                    current.setEtapeValide(current.getEtapeValide() + 1);
                    update((T) current);
                    return "succes";
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Validation Impossible";
    }

    public String annulEtapeFactureVente(YvsComDocVentes current, YvsWorkflowValidFactureVente etape, String motif, YvsUsersAgence currentUser) {
        try {
            //contrôle la cohérence des dates
            if (motif != null ? motif.trim().isEmpty() : true) {
                return "Vous devez précisez le motif";
            }
            int idx = current.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                if (currentUser != null) {
                    this.currentUser = currentUser;
                    this.currentAgence = currentUser.getAgence();
                    this.currentScte = currentAgence.getSociete();
                    loadInfos(currentScte, currentAgence, currentUser, currentDepot, currentPoint, currentExo);
                }
                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() != null) {
                    champ = new String[]{"etape", "facture"};
                    val = new Object[]{etape.getEtape().getEtapeSuivante(), current};
                    YvsWorkflowValidFactureVente y = (YvsWorkflowValidFactureVente) loadOneByNameQueries("YvsWorkflowValidFactureVente.findByEtapeFacture", champ, val);
                    if (y != null ? (y.getId() > 0 ? y.getEtapeValid() : false) : false) {
                        return "Vous devez au préalable annuler l'étape suivante";
                    }
                }
                etape.setAuthor(currentUser);
                etape.setEtapeValid(false);
                etape.setEtapeActive(true);
                etape.setMotif(motif);
                String requete = "UPDATE yvs_workflow_valid_facture_vente SET etape_valid = ?, author = ? , date_update = ?, motif = ? WHERE id = ?";
                requeteLibre(requete, new Options[]{new Options(false, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(motif, 4), new Options(etape.getId(), 5)});

                current.setEtapeValide(current.getEtapeValide() - 1);
                current.setStatut(current.getEtapeValide() < 1 ? Constantes.ETAT_EDITABLE : Constantes.ETAT_ENCOURS);
                current.setAuthor(currentUser);
                update((T) current);
                return "succes";
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Validation Impossible";
    }

    public String validEtapeOrdreDivers(YvsComptaCaisseDocDivers current, YvsWorkflowValidDocCaisse etape, YvsUsersAgence currentUser) {
        try {
            //contrôle la cohérence des dates
            YvsComptaParametre currentParam = (YvsComptaParametre) loadObjectByNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{currentScte});
            if (etape.getEtape().getEtapeSuivante() == null && currentParam != null) {
                if (currentParam.getMontantSeuilDepenseOd() > 0 ? current.getMontant() > currentParam.getMontantSeuilDepenseOd() : false) {
                    return "Vous ne disposez pas de privillège pour valider un OD pour le montant est superieur à " + currentParam.getMontantSeuilDepenseOd() + " !";
                }
            }
            int idx = current.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                if (currentUser != null) {
                    this.currentUser = currentUser;
                    this.currentAgence = currentUser.getAgence();
                    this.currentScte = currentAgence.getSociete();
                    loadInfos(currentScte, currentAgence, currentUser, currentDepot, currentPoint, currentExo);
                }
                etape.setAuthor(currentUser);
                etape.setDateUpdate(new Date());
                etape.setEtapeValid(true);
                etape.setEtapeActive(false);
                etape.setMotif(null);
                if (current.getEtapesValidations().size() > (idx + 1)) {
                    current.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                }
                String requete = "UPDATE yvs_workflow_valid_doc_caisse SET etape_valid = ?, author = ? , date_update = ? WHERE id = ?";
                requeteLibre(requete, new Options[]{new Options(true, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(etape.getId(), 4)});
                current.setStatutDoc(Constantes.ETAT_ENCOURS);
                current.setAuthor(currentUser);
                current.setEtapeValide(current.getEtapeValide() + 1);
                update((T) current);
            } else {
                return "Etape introuvée";
            }
            //cas de la validation de la dernière étapes
            if (etape.getEtape().getEtapeSuivante() == null) {
                current.setStatutDoc(Constantes.ETAT_VALIDE);
                current.setDateValider(new Date());
                current.setValiderBy(currentUser.getUsers());
                current.setEtapeValide(current.getEtapeTotal());
                etape.setDateUpdate(new Date());
                update((T) current);
                if (currentParam != null) {
                    if (currentParam.getMajComptaAutoDivers() ? currentParam.getMajComptaStatutDivers().equals(Constantes.STATUT_DOC_VALIDE) : false) {
                        fonction.comptabiliserDivers(current, (DaoInterfaceLocal) this);
                    }
                }
                Collections.sort(current.getReglements(), new YvsComptaCaissePieceDivers());
                for (YvsComptaCaissePieceDivers p : current.getReglements()) {
                    if (!p.getStatutPiece().equals(Constantes.STATUT_DOC_ANNULE)) {
                        if (p.isJustificatif()) {
                            YvsComptaBonProvisoire b = p.getJustify().getBon();
                            if (b.getStatutPaiement().equals(Constantes.STATUT_DOC_PAYER) && !b.getStatutJustify().equals(Constantes.STATUT_DOC_JUSTIFIER)) {
                                fonction.validePieceDivers(p, current, (DaoInterfaceLocal) this);
                            }
                        }
                    }
                }
                return "succes";
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Validation Impossible";
    }

    public String annulEtapeOrdreDivers(YvsComptaCaisseDocDivers current, YvsWorkflowValidDocCaisse etape, String motif, YvsUsersAgence currentUser) {
        try {
            //contrôle la cohérence des dates
            if (motif != null ? motif.trim().isEmpty() : true) {
                return "Vous devez précisez le motif";
            }
            int idx = current.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                if (currentUser != null) {
                    this.currentUser = currentUser;
                    this.currentAgence = currentUser.getAgence();
                    this.currentScte = currentAgence.getSociete();
                    loadInfos(currentScte, currentAgence, currentUser, currentDepot, currentPoint, currentExo);
                }
                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() != null) {
                    champ = new String[]{"etape", "facture"};
                    val = new Object[]{etape.getEtape().getEtapeSuivante(), current};
                    YvsWorkflowValidDocCaisse y = (YvsWorkflowValidDocCaisse) loadOneByNameQueries("YvsWorkflowValidDocCaisse.findByEtapeFacture", champ, val);
                    if (y != null ? (y.getId() > 0 ? y.getEtapeValid() : false) : false) {
                        return "Vous devez au préalable annuler l'étape suivante";
                    }
                }
                etape.setAuthor(currentUser);
                etape.setEtapeValid(false);
                etape.setEtapeActive(true);
                etape.setMotif(motif);
                String requete = "UPDATE yvs_workflow_valid_doc_caisse SET etape_valid = ?, author = ? , date_update = ?, motif = ? WHERE id = ?";
                requeteLibre(requete, new Options[]{new Options(false, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(motif, 4), new Options(etape.getId(), 5)});

                current.setEtapeValide(current.getEtapeValide() - 1);
                current.setStatutDoc(current.getEtapeValide() < 1 ? Constantes.ETAT_EDITABLE : Constantes.ETAT_ENCOURS);
                current.setAuthor(currentUser);
                update((T) current);
                if (isComptabilise(current.getId(), Constantes.SCR_DIVERS)) {
//                       unComptabiliserDivers(current, false);
                }
                return "succes";
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Annulation Impossible";
    }

    public String validEtapeOrdreConge(YvsGrhCongeEmps current, YvsWorkflowValidConge etape, YvsUsersAgence currentUser) {
        //vérifier que la personne qui valide l'étape a le droit 
        try {
            if (current.getStatut() == 'S') {
                return "Cet ordre de congé a déjà été suspendu";
            }
            if (currentUser != null) {
                this.currentUser = currentUser;
                this.currentAgence = currentUser.getAgence();
                this.currentScte = currentAgence.getSociete();
                loadInfos(currentScte, currentAgence, currentUser, currentDepot, currentPoint, currentExo);
            }
            YvsParametreGrh paramGrh = (YvsParametreGrh) loadOneByNameQueries("YvsParametreGrh.findAll", new String[]{"societe"}, new Object[]{currentScte});
            if (etape.getEtape().getEtapeSuivante() != null) {  //valide les étapes intermediaire
                etape.setAuthor(currentUser);
                etape.setEtapeValid(true);
                etape.setEtapeActive(false);
                etape.setMotif(null);
                etape.setDateUpdate(new Date());
                int idx = current.getEtapesValidations().indexOf(etape);
                if (idx >= 0) {
                    if (current.getEtapesValidations().size() > (idx + 1)) {
                        current.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                    }
                    current.setStatut(Constantes.STATUT_DOC_EDITABLE);
                    String requete = "UPDATE yvs_workflow_valid_conge SET etape_valid = ?, author = ? , date_update = ? WHERE id = ?";
                    requeteLibre(requete, new Options[]{new Options(true, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(etape.getId(), 4)});
                    current.setEtapeValide(current.getEtapeValide() + 1);
                    update((T) current);
                    return "succes";
                }
            } else {
                etape.setAuthor(currentUser);
                etape.setEtapeValid(true);
                etape.setEtapeActive(false);
            }
            //cas de la validation de la dernière étapes
            if (etape.getEtape().getEtapeSuivante() == null) {
                //vérifie que le nombre de jours pris est cohérent avec le nombre de jour disponible
                fonction.applyDureeConge(current, currentExo, current.getDateDebut(), (DaoInterfaceLocal) this);
                switch (current.getEffet()) {
                    case Constantes.GRH_PERMISSION_SUR_CONGE:
                        int ecart = fonction.countDayBetweenDate(current.getDateDebut(), current.getDateFin());
                        if (ecart > current.getCongePrincipalDu()) {
                            etape.setEtapeValid(false);
                            etape.setEtapeActive(true);
                            return "Vous ne disposé plus d'assez de congé !";
                        }
                        break;
                    case Constantes.GRH_PERMISSION_AUTORISE:
                        if (paramGrh.getTotalCongePermis() <= current.getDureePermissionAutorisePris()) {
                            etape.setEtapeValid(false);
                            etape.setEtapeActive(true);
                            return "Vous avez épuisé votre quotas de permission !";
                        }
                        break;
                }
                int idx = current.getEtapesValidations().indexOf(etape);
                if (idx >= 0) {
                    if (current.getEtapesValidations().size() > (idx + 1)) {
                        current.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                    }
                    current.setEtapeValide(current.getEtapeValide() + 1);
                    current.setStatut(Constantes.STATUT_DOC_VALIDE);
                    current.setDatePaiementAlloc(current.getDatePaiementAlloc());
                    current.setStatut(Constantes.STATUT_DOC_VALIDE);
                    update((T) current);
                    String requete = "UPDATE yvs_workflow_valid_conge SET etape_valid = ?, author = ? , date_update = ? WHERE id = ?";
                    requeteLibre(requete, new Options[]{new Options(true, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(etape.getId(), 4)});
                    current.setEtapeValide(current.getEtapeValide() + 1);
                    return "succes";
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Validation Impossible";
    }

    public String annulEtapeOrdreConge(YvsGrhCongeEmps current, YvsWorkflowValidConge etape, String motif, YvsUsersAgence currentUser) {
        try {
            //contrôle la cohérence des dates
            if (motif != null ? motif.trim().isEmpty() : true) {
                return "Vous devez précisez le motif";
            }
            int idx = current.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                if (currentUser != null) {
                    this.currentUser = currentUser;
                    this.currentAgence = currentUser.getAgence();
                    this.currentScte = currentAgence.getSociete();
                    loadInfos(currentScte, currentAgence, currentUser, currentDepot, currentPoint, currentExo);
                }
                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() != null) {
                    champ = new String[]{"etape", "document"};
                    val = new Object[]{etape.getEtape().getEtapeSuivante(), current};
                    YvsWorkflowValidConge y = (YvsWorkflowValidConge) loadOneByNameQueries("YvsWorkflowValidConge.findByEtapeDocument", champ, val);
                    if (y != null ? (y.getId() > 0 ? y.getEtapeValid() : false) : false) {
                        return "Vous devez au préalable annuler l'étape suivante";
                    }
                }
                etape.setAuthor(currentUser);
                etape.setEtapeValid(false);
                etape.setEtapeActive(true);
                etape.setMotif(motif);
                String requete = "UPDATE yvs_workflow_valid_conge SET etape_valid = ?, author = ? , date_update = ?, motif = ? WHERE id = ?";
                requeteLibre(requete, new Options[]{new Options(false, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(motif, 4), new Options(etape.getId(), 5)});

                current.setEtapeValide(current.getEtapeValide() - 1);
                current.setStatut(current.getEtapeValide() < 1 ? Constantes.STATUT_DOC_EDITABLE : Constantes.STATUT_DOC_ENCOUR);
                current.setAuthor(currentUser);
                update((T) current);
                if (isComptabilise(current.getId(), Constantes.SCR_DIVERS)) {
//                       unComptabiliserDivers(current, false);
                }
                return "succes";
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Annulation Impossible";
    }

    public String validEtapeOrdreMission(YvsGrhMissions current, YvsWorkflowValidMission etape, YvsUsersAgence currentUser) {
        //vérifier que la personne qui valide l'étape a le droit 
        try {
            //contrôle la cohérence des dates
            current = setMontantTotalMission(current);
            Date curr = fonction.giveOnlyDate(new Date());
            if (!current.getDateDebut().after(curr)) {
                System.err.println("La mission est déjà en cours d'exécuttion !");
            }
            if (current.getDateFin().before(curr)) {
                System.err.println("La mission est déjà terminé !");
            }
            int idx = current.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                if (currentUser != null) {
                    this.currentUser = currentUser;
                    this.currentAgence = currentUser.getAgence();
                    this.currentScte = currentAgence.getSociete();
                    loadInfos(currentScte, currentAgence, currentUser, currentDepot, currentPoint, currentExo);
                }
                etape.setAuthor(currentUser);
                etape.setDateUpdate(new Date());
                etape.setEtapeValid(true);
                etape.setEtapeActive(false);
                etape.setMotif(null);
                if (current.getEtapesValidations().size() > (idx + 1)) {
                    current.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                }
                String requete = "UPDATE yvs_workflow_valid_mission SET etape_valid = ?, author = ? , date_update = ? WHERE id = ?";
                requeteLibre(requete, new Options[]{new Options(true, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(etape.getId(), 4)});
                current.setStatutMission(Constantes.STATUT_DOC_ENCOUR);
                current.setAuthor(currentUser);
                current.setEtapeValide(current.getEtapeValide() + 1);
                update((T) current);
            } else {
                return "Validation Impossible";
            }
            //cas de la validation de la dernière étapes
            if (etape.getEtape().getEtapeSuivante() == null) {
                current.setStatutMission(Constantes.STATUT_DOC_VALIDE);
                current.setDateValider(new Date());
                current.setValiderBy(currentUser.getUsers());
                etape.setDateUpdate(new Date());
                update((T) current);
                boolean succes = fonction.valideOrdreMission(current, currentUser, (DaoInterfaceLocal) this);
                if (!succes) {
                    annulEtapeOrdreMission(current, etape, "Impossible de générer la pièce de reglement", currentUser);
                    return "Validation Impossible";
                }
                succes = fonction.valideBonProvisoire(current, currentUser, (DaoInterfaceLocal) this);
                if (!succes) {
                    annulEtapeOrdreMission(current, etape, "Impossible de valider le bon provisoire de cette mission", currentUser);
                    return "Validation Impossible";
                }
            }
            return "succes";
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Validation Impossible";
    }

    public String annulEtapeOrdreMission(YvsGrhMissions current, YvsWorkflowValidMission etape, String motif, YvsUsersAgence currentUser) {
        try {
            //contrôle la cohérence des dates
            if (motif != null ? motif.trim().isEmpty() : true) {
                return "Vous devez précisez le motif";
            }
            int idx = current.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                if (currentUser != null) {
                    this.currentUser = currentUser;
                    this.currentAgence = currentUser.getAgence();
                    this.currentScte = currentAgence.getSociete();
                    loadInfos(currentScte, currentAgence, currentUser, currentDepot, currentPoint, currentExo);
                }
                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() != null) {
                    champ = new String[]{"etape", "mission"};
                    val = new Object[]{etape.getEtape().getEtapeSuivante(), current};
                    YvsWorkflowValidMission y = (YvsWorkflowValidMission) loadOneByNameQueries("YvsWorkflowValidMission.findByEtapeMission", champ, val);
                    if (y != null ? (y.getId() > 0 ? y.getEtapeValid() : false) : false) {
                        return "Vous devez au préalable annuler l'étape suivante";
                    }
                }
                etape.setAuthor(currentUser);
                etape.setEtapeValid(false);
                etape.setEtapeActive(true);
                etape.setMotif(motif);
                String requete = "UPDATE yvs_workflow_valid_mission SET etape_valid = ?, author = ? , date_update = ?, motif = ? WHERE id = ?";
                requeteLibre(requete, new Options[]{new Options(false, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(motif, 4), new Options(etape.getId(), 5)});

                current.setEtapeValide(current.getEtapeValide() - 1);
                current.setStatutMission(current.getEtapeValide() < 1 ? Constantes.STATUT_DOC_EDITABLE : Constantes.STATUT_DOC_ENCOUR);
                current.setAuthor(currentUser);
                update((T) current);
                if (isComptabilise(current.getId(), Constantes.SCR_DIVERS)) {
//                       unComptabiliserDivers(current, false);
                }
                return "succes";
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Annulation Impossible";
    }

    public String validEtapeDocStock(YvsComDocStocks current, YvsWorkflowValidDocStock etape, YvsUsersAgence currentUser) {
        //vérifier que la personne qui valide l'étape a le droit 
        try {
            //contrôle la cohérence des dates
            int idx = current.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                if (currentUser != null) {
                    this.currentUser = currentUser;
                    this.currentAgence = currentUser.getAgence();
                    this.currentScte = currentAgence.getSociete();
                    loadInfos(currentScte, currentAgence, currentUser, currentDepot, currentPoint, currentExo);
                }
                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() == null) {
                    if (fonction.validerStock(current, (DaoInterfaceLocal) this)) {
                        etape.setAuthor(currentUser);
                        etape.setEtapeValid(true);
                        etape.setEtapeActive(false);
                        etape.setMotif(null);
                        if (current.getEtapesValidations().size() > (idx + 1)) {
                            current.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                        }
                        String requete = "UPDATE yvs_workflow_valid_doc_stock SET etape_valid = ?, author = ? , date_update = ? WHERE id = ?";
                        requeteLibre(requete, new Options[]{new Options(true, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(etape.getId(), 4)});

                        current.setEtapeValide(current.getEtapeValide() + 1);
                        update((T) current);
                        return "succes";
                    }
                } else {
                    etape.setAuthor(currentUser);
                    etape.setEtapeValid(true);
                    etape.setEtapeActive(false);
                    etape.setMotif(null);
                    if (current.getEtapesValidations().size() > (idx + 1)) {
                        current.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                    }
                    String requete = "UPDATE yvs_workflow_valid_doc_stock SET etape_valid = ?, author = ? , date_update = ? WHERE id = ?";
                    requeteLibre(requete, new Options[]{new Options(true, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(etape.getId(), 4)});
                    current.setStatut(Constantes.ETAT_ENCOURS);
                    current.setAuthor(currentUser);
                    current.setEtapeValide(current.getEtapeValide() + 1);
                    update((T) current);
                    current.setStatut(Constantes.ETAT_ENCOURS);
                    return "succes";
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Validation Impossible";
    }

    public String annulEtapeDocStock(YvsComDocStocks current, YvsWorkflowValidDocStock etape, String motif, YvsUsersAgence currentUser) {
        try {
            //contrôle la cohérence des dates
            if (motif != null ? motif.trim().isEmpty() : true) {
                return "Vous devez précisez le motif";
            }
            int idx = current.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                if (currentUser != null) {
                    this.currentUser = currentUser;
                    this.currentAgence = currentUser.getAgence();
                    this.currentScte = currentAgence.getSociete();
                    loadInfos(currentScte, currentAgence, currentUser, currentDepot, currentPoint, currentExo);
                }
                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() != null) {
                    champ = new String[]{"etape", "facture"};
                    val = new Object[]{etape.getEtape().getEtapeSuivante(), current};
                    YvsWorkflowValidDocStock y = (YvsWorkflowValidDocStock) loadOneByNameQueries("YvsWorkflowValidDocStock.findByEtapeFacture", champ, val);
                    if (y != null ? (y.getId() > 0 ? y.getEtapeValid() : false) : false) {
                        return "Vous devez au préalable annuler l'étape suivante";
                    }
                }
                etape.setAuthor(currentUser);
                etape.setEtapeValid(false);
                etape.setEtapeActive(true);
                etape.setMotif(motif);
                String requete = "UPDATE yvs_workflow_valid_doc_stock SET etape_valid = ?, author = ? , date_update = ?, motif = ? WHERE id = ?";
                requeteLibre(requete, new Options[]{new Options(false, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(motif, 4), new Options(etape.getId(), 5)});

                current.setEtapeValide(current.getEtapeValide() - 1);
                current.setStatut(current.getEtapeValide() < 1 ? Constantes.ETAT_EDITABLE : Constantes.ETAT_ENCOURS);
                current.setAuthor(currentUser);
                update((T) current);
                return "succes";
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Annulation Impossible";
    }

    public String validEtapeApprovisionnement(YvsComFicheApprovisionnement current, YvsWorkflowValidApprovissionnement etape, YvsUsersAgence currentUser) {
        try {
            //contrôle la cohérence des dates
            int idx = current.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                current.setAuthor(currentUser);
                current.setDateUpdate(new Date());
                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() == null) {
                    if (fonction.changeStatutAppro(current, Constantes.ETAT_VALIDE, (DaoInterfaceLocal) this)) {
                        etape.setAuthor(currentUser);
                        etape.setEtapeValid(true);
                        etape.setMotif(null);
                        etape.setEtapeActive(false);
                        if (current.getEtapesValidations().size() > (idx + 1)) {
                            current.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                        }
                        String requete = "UPDATE yvs_workflow_valid_approvissionnement SET etape_valid = ?, author = ? , date_update = ? WHERE id = ?";
                        requeteLibre(requete, new Options[]{new Options(true, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(etape.getId(), 4)});

                        current.setEtat(Constantes.ETAT_VALIDE);
                        current.setEtapeValide(current.getEtapeValide() + 1);
                        current.setEtapeTotal(current.getEtapesValidations().size());
                        current.setEtat(current.getEtat());
                        current.setEtapeValide(current.getEtapeValide());
                        current.setEtapeTotal(current.getEtapeTotal());
                        update((T) current);
                        return "succes";
                    }
                } else {
                    etape.setAuthor(currentUser);
                    etape.setEtapeValid(true);
                    etape.setMotif(null);
                    etape.setEtapeActive(false);
                    if (current.getEtapesValidations().size() > (idx + 1)) {
                        current.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                    }
                    String requete = "UPDATE yvs_workflow_approvissionnement SET etape_valid = ?, author = ? , date_update = ? WHERE id = ?";
                    requeteLibre(requete, new Options[]{new Options(true, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(etape.getId(), 4)});
                    current.setEtat(Constantes.ETAT_ENCOURS);
                    current.setAuthor(currentUser);
                    current.setEtapeValide(current.getEtapeValide() + 1);
                    current.setEtapeTotal(current.getEtapesValidations().size());
                    update((T) current);

                    current.setEtat(Constantes.ETAT_ENCOURS);
                    current.setEtapeValide(current.getEtapeValide());
                    current.setEtapeTotal(current.getEtapeTotal());
                    return "succes";
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Validation Impossible";
    }

    public String annulEtapeApprovisionnement(YvsComFicheApprovisionnement current, YvsWorkflowValidApprovissionnement etape, String motif, YvsUsersAgence currentUser) {
        try {
            //contrôle la cohérence des dates
            if (motif != null ? motif.trim().isEmpty() : true) {
                return "Vous devez précisez le motif";
            }
            int idx = current.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                if (currentUser != null) {
                    this.currentUser = currentUser;
                    this.currentAgence = currentUser.getAgence();
                    this.currentScte = currentAgence.getSociete();
                    loadInfos(currentScte, currentAgence, currentUser, currentDepot, currentPoint, currentExo);
                }
                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() != null) {
                    champ = new String[]{"etape", "facture"};
                    val = new Object[]{etape.getEtape().getEtapeSuivante(), current};
                    YvsWorkflowValidApprovissionnement y = (YvsWorkflowValidApprovissionnement) loadOneByNameQueries("YvsWorkflowValidApprovissionnement.findByEtapeFacture", champ, val);
                    if (y != null ? (y.getId() > 0 ? y.getEtapeValid() : false) : false) {
                        return "Vous devez au préalable annuler l'étape suivante";
                    }
                }
                etape.setAuthor(currentUser);
                etape.setEtapeValid(false);
                etape.setEtapeActive(true);
                etape.setMotif(motif);
                String requete = "UPDATE yvs_workflow_valid_approvissionnement SET etape_valid = ?, author = ? , date_update = ?, motif = ? WHERE id = ?";
                requeteLibre(requete, new Options[]{new Options(false, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(motif, 4), new Options(etape.getId(), 5)});

                current.setEtapeValide(current.getEtapeValide() - 1);
                current.setEtat(current.getEtapeValide() < 1 ? Constantes.ETAT_EDITABLE : Constantes.ETAT_ENCOURS);
                current.setAuthor(currentUser);
                update((T) current);
                return "succes";
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Annulation Impossible";
    }

    public String validEtapeBonProvisoire(YvsComptaBonProvisoire current, YvsWorkflowValidBonProvisoire etape, YvsUsersAgence currentUser) {
        try {
            int idx = current.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                if (currentUser != null) {
                    this.currentUser = currentUser;
                    this.currentAgence = currentUser.getAgence();
                    this.currentScte = currentAgence.getSociete();
                    loadInfos(currentScte, currentAgence, currentUser, currentDepot, currentPoint, currentExo);
                }
                etape.setAuthor(currentUser);
                etape.setDateUpdate(new Date());
                etape.setEtapeValid(true);
                etape.setEtapeActive(false);
                etape.setMotif(null);
                String requete = "UPDATE yvs_workflow_valid_bon_provisoire SET etape_valid = ?, author = ? , date_update = ? WHERE id = ?";
                requeteLibre(requete, new Options[]{new Options(true, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(etape.getId(), 4)});

                current.setStatut(Constantes.ETAT_ENCOURS);
                current.setAuthor(currentUser);
                current.setEtapeValide(current.getEtapeValide() + 1);
                update((T) current);

                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() == null) {
                    current.setStatut(Constantes.ETAT_VALIDE);
                    current.setDateValider(new Date());
                    current.setValiderBy(currentUser.getUsers());
                    current.setOrdonnateur(currentUser.getUsers());
                    current.setEtapeValide(current.getEtapeTotal());
                    etape.setDateUpdate(new Date());
                    update((T) current);
                }
                return "succes";
            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Validation Impossible";
    }

    public String annulEtapeBonProvisoire(YvsComptaBonProvisoire current, YvsWorkflowValidBonProvisoire etape, String motif, YvsUsersAgence currentUser) {
        try {
            //contrôle la cohérence des dates
            if (motif != null ? motif.trim().isEmpty() : true) {
                return "Vous devez précisez le motif";
            }
            int idx = current.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                if (currentUser != null) {
                    this.currentUser = currentUser;
                    this.currentAgence = currentUser.getAgence();
                    this.currentScte = currentAgence.getSociete();
                    loadInfos(currentScte, currentAgence, currentUser, currentDepot, currentPoint, currentExo);
                }
                //cas de la validation de la dernière étapes
                if (etape.getEtape().getEtapeSuivante() != null) {
                    champ = new String[]{"etape", "facture"};
                    val = new Object[]{etape.getEtape().getEtapeSuivante(), current};
                    YvsWorkflowValidBonProvisoire y = (YvsWorkflowValidBonProvisoire) loadOneByNameQueries("YvsWorkflowValidBonProvisoire.findByEtapeFacture", champ, val);
                    if (y != null ? (y.getId() > 0 ? y.getEtapeValid() : false) : false) {
                        return "Vous devez au préalable annuler l'étape suivante";
                    }
                }
                etape.setAuthor(currentUser);
                etape.setEtapeValid(false);
                etape.setEtapeActive(true);
                etape.setMotif(motif);
                String requete = "UPDATE yvs_workflow_valid_bon_provisoire SET etape_valid = ?, author = ? , date_update = ?, motif = ? WHERE id = ?";
                requeteLibre(requete, new Options[]{new Options(false, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3), new Options(motif, 4), new Options(etape.getId(), 5)});

                current.setEtapeValide(current.getEtapeValide() - 1);
                current.setStatut(current.getEtapeValide() < 1 ? Constantes.ETAT_EDITABLE : Constantes.ETAT_ENCOURS);
                current.setAuthor(currentUser);
                update((T) current);
                return "succes";

            }
        } catch (Exception ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Annulation Impossible";
    }
    /*END WORKFLOW*/

    public YvsComDocStocks lastInventaire(long depot, Date date, long tranche) {
        YvsGrhTrancheHoraire creno = (YvsGrhTrancheHoraire) loadOneByNameQueries("YvsGrhTrancheHoraire.findById", new String[]{"id"}, new Object[]{tranche});
        List<Long> ids = new ArrayList<>();
        if (creno != null) {
            ids = (List) loadNameQueries("YvsComCreneauDepot.findIdTrancheOrderByDepot", new String[]{"depot", "heure"}, new Object[]{new YvsBaseDepots(depot), creno.getHeureDebut()});
        }
        if (ids.isEmpty()) {
            ids.add(-1L);
        }
        return (YvsComDocStocks) loadOneByNameQueries("YvsComDocStocks.findLastByDocInventaireAfter", new String[]{"statut", "depot", "date", "type", "tranche"},
                new Object[]{Constantes.ETAT_VALIDE, new YvsBaseDepots(depot), date, Constantes.TYPE_IN, ids});
    }

    public boolean controleInventaire(long depot, Date date, long tranche) {
        YvsComDocStocks y = lastInventaire(depot, date, tranche);
        return (y != null ? y.getId() < 1 : true);
    }

    public boolean controleEcartVente(long users, Date date) {
        Long nb = (Long) loadObjectByNameQueries("YvsComEcartEnteteVente.counByUsersAfter", new String[]{"users", "date"}, new Object[]{new YvsUsers(users), date});
        if (nb != null ? nb > 0 : false) {
            return false;
        }
        return true;
    }

    //retrouver un header
    public YvsComEnteteDocVente getEntete(YvsUsers u, YvsBasePointVente p, YvsGrhTrancheHoraire t, Date date, YvsNiveauAcces currentNiveau, YvsAgences currentAgence, YvsUsersAgence currentUser) {
        try {
            setRESULT(null);
            if ((u != null) ? u.getId() <= 0 : true) {
                setRESULT("L'utilisateur n'a pas été trouvé !");
                return null;
            }
            if ((p != null) ? p.getId() <= 0 : true) {
                setRESULT("Le point de vente n'a pas été trouvé !");
                return null;
            }
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            now.set(Calendar.HOUR_OF_DAY, 0);
            now.set(Calendar.MINUTE, 0);
            now.set(Calendar.SECOND, 0);
            now.set(Calendar.MILLISECOND, 0);
            now.add(Calendar.DATE, 1);

            Calendar d = Calendar.getInstance();
            d.setTime(date);
            d.set(Calendar.HOUR_OF_DAY, 0);
            d.set(Calendar.MINUTE, 0);
            d.set(Calendar.SECOND, 0);
            d.set(Calendar.MILLISECOND, 0);

            if ((date != null) ? (d.after(now)) : true) {
                setRESULT("la date n'a pas été trouvé, ou est mal configuré");
                return null;
            }
            //trouve un crénau
            YvsComCreneauHoraireUsers cru = (YvsComCreneauHoraireUsers) loadOneByNameQueries("YvsComCreneauHoraireUsers.findOneCompletCreno2", new String[]{"users", "date", "point"}, new Object[]{u, date, p});
            if (cru != null) {
                YvsComEnteteDocVente en = (YvsComEnteteDocVente) loadOneByNameQueries("YvsComEnteteDocVente.findByHeader", new String[]{"creno", "date", "etat"}, new Object[]{cru, date, Constantes.ETAT_TERMINE});
                if (en == null) {
                    //aucun header nexiste pour l'instant: il faut le créer
                    en = new YvsComEnteteDocVente();
                    en.setAuthor(currentUser);
                    en.setCreneau(cru);
                    en.setDateEntete(date);
                    en.setEtat(Constantes.ETAT_EDITABLE);
                    en.setStatutLivre(Constantes.ETAT_ATTENTE);
                    en.setStatutRegle(Constantes.ETAT_ATTENTE);
                    en.setAgence(currentAgence);
                    en.setDateSave(new Date());
                    en.setDateUpdate(new Date());
                    en.setComptabilise(false);
                    en.setCloturer(false);
                    en.setNew_(true);
                    IYvsComEnteteDocVente impl = (IYvsComEnteteDocVente) (new IEntitySax()).createInstance("IYvsComEnteteDocVente", (DaoInterfaceLocal) this);
                    if (impl != null) {
                        impl.setNiveauAcces(currentNiveau);
                        impl.setAgence(currentAgence);
                        ResultatAction<YvsComEnteteDocVente> re = impl.save(en);
                        if (re != null ? re.isResult() : false) {
                            return re.getEntity();
                        } else {
                            setRESULT(re.getMessage());
                        }
                    } else {
                        setRESULT("Erreur Système !");
                    }
                } else {
                    return en;
                }
            } else {
                setRESULT("Aucune planification n'a été trouvé pour cet utilisateur !");
            }
            return null;
        } catch (Exception ex) {
            setRESULT("Création Journal de vente impossible");
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean asString(String valeur) {
        if (valeur != null ? valeur.trim().length() > 0 : false) {
            return true;
        }
        return false;
    }

    public T afterCRUD(T entity, String action) {
        try {
            if (entity != null && asString(action)) {
                if (entity.getClass().isAnnotationPresent(Table.class) && (entity instanceof YvsEntity)) {
                    YvsEntity instance = (YvsEntity) entity;
                    String name = ((Table) instance.getClass().getAnnotation(Table.class)).name();
                    String serverName = "127.0.0.1";
                    if (instance.getAdresseServeur() != null ? !instance.getAdresseServeur().isEmpty() : false) {
                        serverName = instance.getAdresseServeur();
                    }
                    if (asString(name) && asString(serverName)) {
                        if (instance.getId() != null ? instance.getId() > 0 : false) {
                            YvsSynchroServeurs serveur = (YvsSynchroServeurs) loadOneByNameQueries("YvsSynchroServeurs.findByAdresseIp", new String[]{"adresseIp"}, new Object[]{serverName});
                            if (serveur != null ? serveur.getId() < 1 : true) {
                                serveur = null;
                            }
                            YvsSynchroListenTable listen = (YvsSynchroListenTable) loadOneByNameQueries("YvsSynchroListenTable.findByActionSource", new String[]{"idSource", "nameTable", "action"}, new Object[]{instance.getId(), name, action});
                            if (listen != null ? listen.getId() < 1 : true) {
                                listen = new YvsSynchroListenTable();
                                listen.setActionName(action);
                                listen.setIdSource(instance.getId());
                                listen.setNameTable(name);
                                listen.setToListen(true);
                                listen.setServeur(serveur);
                                listen.setAuthor(instance.getAuthor() != null ? instance.getAuthor().getId() > 0 ? instance.getAuthor().getId() : null : null);
                                listen = (YvsSynchroListenTable) save1((T) listen, false);
                            } else {
                                listen.setToListen(true);
                                listen.setServeur(serveur);
                                listen.setDateSave(new Date());
                                listen.setAuthor(instance.getAuthor() != null ? instance.getAuthor().getId() > 0 ? instance.getAuthor().getId() : null : null);
                                update((T) listen, false);
                                String query = "DELETE FROM yvs_synchro_data_synchro WHERE id_listen = ?";
                                requeteLibre(query, new Options[]{new Options(listen.getId(), 1)});
                            }
//                            if (action.equals("DELETE")) {
//                                String query = "DELETE FROM yvs_synchro_listen_table WHERE id_source = ? AND name_table = ? AND id != ?";
//                                dao.requeteLibre(query, new Options[]{new Options(listen.getIdSource(), 1), new Options(listen.getNameTable(), 2), new Options(listen.getId(), 3)});
//                            }
                            if (serveur != null ? serveur.getId() > 0 : false) {
                                YvsSynchroDataSynchro synchro = (YvsSynchroDataSynchro) loadOneByNameQueries("YvsSynchroDataSynchro.findOne", new String[]{"listen", "distant", "serveur"}, new Object[]{listen, instance.getIdDistant(), serveur});
                                if (synchro != null ? synchro.getId() < 1 : true) {
                                    synchro = new YvsSynchroDataSynchro();
                                    synchro.setIdListen(listen);
                                    synchro.setServeur(serveur);
                                    synchro.setIdDistant(instance.getIdDistant());
                                    save((T) synchro, false);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IllegalArgumentException | SecurityException ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return entity;
    }

    public void afterCRUD_OLD(String action, T entity) {
        if (entity != null ? entity instanceof YvsEntity : false) {
//            Class c = entity.getClass();
//            String tableName = null, query;
//            String serverName = "127.0.0.1";
//            Long serverId = null;
//            Long author = null;
//            //Trouver le nom de la table
//            Annotation ann = c.getAnnotation(Table.class);
//            if (ann != null ? ann instanceof Table : false) {
//                Table table = (Table) ann;
//                tableName = table.name();
//            }
//            YvsEntity en = (YvsEntity) entity;
//            if (en.getAdresseServeur() != null ? !en.getAdresseServeur().isEmpty() : false) {
//                serverName = en.getAdresseServeur();
//            }
//            serverId = (Long) loadObjectByNameQueries("YvsSynchroServeurs.findIdByNomServeur", new String[]{"nomServeur"}, new Object[]{serverName});
//            author = (en.getAuthor() != null) ? en.getAuthor().getId() : null;
//            if (action.equals("UPDATE")) {
//                query = "SELECT id FROM yvs_synchro_listen_table WHERE name_table=? AND id_source=? AND action_name=? ";
//                Long id = (Long) loadObjectBySqlQuery(query, new Options[]{new Options(tableName, 1), new Options(en.getId(), 2), new Options(action, 3)});
//                if (id != null ? id > 0 : false) {
//                    query = "UPDATE yvs_synchro_listen_table SET to_listen=true, author=?::bigint, serveur=?::bigint WHERE id=?";
//                    requeteLibre(query, new Options[]{new Options(author, 1), new Options(serverId, 2), new Options(id, 3)});
//                    return;
//                }
//            }
//            query = "INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, to_listen, action_name, author, serveur) "
//                    + "VALUES (?, ?, ?, ?, ?, ?::bigint, ?::bigint)";
//            requeteLibre(query, new Options[]{new Options(tableName, 1), new Options(en.getId(), 2), new Options(new Date(), 3),
//                new Options(true, 4), new Options(action, 5), new Options(author, 6), new Options(serverId, 7)});
        }
    }
}
