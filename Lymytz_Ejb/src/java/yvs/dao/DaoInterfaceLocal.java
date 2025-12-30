/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComFicheApprovisionnement;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.commercial.vente.YvsComMensualiteFactureVente;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.grh.activite.YvsGrhCongeEmps;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.grh.salaire.YvsGrhBulletins;
import yvs.entity.grh.salaire.YvsGrhOrdreCalculSalaire;
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
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 * @param <T>
 */
@Local
public interface DaoInterfaceLocal<T extends Serializable> {

    public void setEntityClass(Class<T> entity);

    public void setEM(String EM); //change l'unité de persistence

    public void setRESULT(String RESULT);

    public String getRESULT();

    public YvsSocietes getCurrentScte();

    public EntityManager getEntityManager();

    public EntityManager giveEntityManager();

//    public CriteriaBuilder giveCriteriaBuilder();
//
//    public List<T> executeCriteriaQuery(CriteriaQuery<T> cq);

    public void save(T en);

    public void save(T en, boolean synchronise);

    public T save1(T en);

    public T save1(T en, boolean synchronise);

    public T update(T en);

    public T update(T en, boolean synchronise);

    public boolean delete(T en);

    public boolean delete(T en, boolean synchronise);

    public List<T> loadAllEntity();
    //charge les données via les namequeries

    public List<T> loadNameQueries(String querie, String[] champ, Object[] val);

    public List<T> loadNameQueries(String querie, String[] champ, Object[] val, int offset, int limit);

    public boolean autoriser(String ressource, YvsNiveauAcces niveau);

    public T getOne(Object id);

    public T loadOneByNameQueries(String querie, String[] champ, Object[] val);

    public Object[] loadTableByNameQueries(String querie, String[] champ, Object[] val);

    public List<Object[]> loadListTableByNameQueries(String querie, String[] champ, Object[] val);

    public List<Object> loadListByNameQueries(String querie, String[] champ, Object[] val);

    public T getOne(String[] ch, Object[] val);

    public Object loadObjectByNameQueries(String querie, String[] champ, Object[] val);

    public boolean contains(T en);

    public List<T> loadEntity(String requeteLibre);

    public List<Object> loadDataByNativeQuery(String querie, Object[] val);

    public List<T> loadEntity(String requeteLibre, String[] champ, Object[] val);

    public List<T> loadEntity(String requeteLibre, String[] champ, Object[] val, int debut, int nbResult);

    public Object loadObjectByEntity(String querie, String[] champ, Object[] val);

    public Object loadObjectBySqlQuery(String querie, Options[] lp);

    public List<Object> loadListBySqlQuery(String querie, Options[] lp);

    public List<Object> loadSerieDate(Date debut, Date fin, String periode);

    public void requeteLibre(String rq, Options[] lp);

    public void updateVueNotif(long user, long notif);

    public double getResteALivrer(long article, long depot, Date date, long conditionnement);

//    public double getLastCout(long art, long depot, double qte);
    public double getNbreCongePrincipalDu(long idEmploye, Date date, long idExo);

    public double getNbreCongeSuppDu(long idEmploye, Date date, long idExo);

    //public double getNbreJourPermissionAutorie(long matricule, Date debut, Date fin);
    public double getNbreJourPermissionPris(long matricule, Date debut, long idExercice, String effet);

    public double getNbreJourCongePris(long idEmploye, Date date, long idExo);

    public double callFonction(String rq, Options[] lp);

    public int callFonctionInt(String rq, Options[] lp);

    public long callFonction1(String rq, Options[] lp);

    public Boolean callFonctionBool(String rq, Options[] lp);

//    public double getNbreHeureTravail(long matricule, Date dateD, Date dateF, boolean heure_compens, boolean sup);
    public void refresh(T en);

//    public String genererReference(String element, Date date, long id, YvsSocietes societes);
//
//    public String genererReference(String element, Date date, long id);
    public String genererReference(String element, Date date, long id, YvsSocietes societes, YvsAgences agences);

    public List<YvsGrhBulletins> calculSalaire(YvsGrhOrdreCalculSalaire od, List<YvsGrhContratEmps> contrats);

    public List<YvsGrhBulletins> calculSalaire(YvsGrhOrdreCalculSalaire ord, List<YvsGrhContratEmps> contrats, boolean deleteDoublon);

    public List<Object[]> findDataPresence(long employe, Date debut, Date fin);

    public boolean getEquilibreEnteteVente(long vente);

    public boolean getEquilibreVente(long societe, Date dateDebut, Date dateFin);

    public String getEquilibreVenteLivre(long vente);
    
    public String getEquilibreVenteRegle(long vente);
    
    public Map<String, String> getEquilibreVente(long vente);

    public boolean getEquilibreDocDivers(long document);

    public boolean getEquilibreDocDivers(long societe, Date dateDebut, Date dateFin);

    public boolean getEquilibreAchat(long societe, Date dateDebut, Date dateFin);

    public Map<String, String> getEquilibreAchat(long achat);

    public boolean getEquilibreApprovision(long fiche);

    public boolean getEquilibreCredit(long credit);

    public List<Object[]> getTaxeVente(long vente);

    public List<Object[]> getTaxeAchat(long achat);

    public double loadCaCommercial(long commercial, Date dateDebut, Date dateFin);
//    public double getPr(long art, long depot, long tranche, Date date);

    public double getPr(long art, long depot, long tranche, Date date, long unite);

    public double getPr(long agence, long art, long depot, long tranche, Date date, long unite);

    public double getTaxe(long art, long cat, long cpt, double rem, double qte, double prix, boolean is_vente, long fournisseur);

    public double getPua(long art, long fsseur);

    public double getPua(long art, long fsseur, long unite);

    public double getPua(long art, long fsseur, long depot, long unite, Date date, long agence);

    public double getPuv(long art, double qte, double prix, long clt, long depot, long point, Date date, boolean min);

    public double getPuv(long art, double qte, double prix, long clt, long depot, long point, Date date, long unite, boolean min);

    public double getPuv(long art, double qte, double prix, long clt, long depot, long point, Date date);

    public double getPuv(long art, double qte, double prix, long clt, long depot, long point, Date date, long unite);

    public double getPuvMin(long art, double qte, double prix, long clt, long depot, long point, Date date);

    public double getPuvMin(long art, double qte, double prix, long clt, long depot, long point, Date date, long unite);

    public double getRemiseVente(long art, double qte, double prix, long clt, long point, Date date);

    public double getRemiseVente(long art, double qte, double prix, long clt, long point, Date date, long unite);

    public double getRemiseAchat(long art, double qte, double prix, long fsseur);

    public double getRistourne(long unite, double qte, double prix, long clt, Date date);

    public double getCommission(long art, double qte, double prix, long vend, Date date);

    public double stocks(long art, long depot, Date dat);

    public double stocks(long art, long depot, Date dat, long unite);

    public double stocks(long art, long depot, Date dat, long unite, long lot);

    public double stocks(long art, long trh, long depot, long ag, long ste, Date dat);

    public double stocks(long art, long trh, long depot, long ag, long ste, Date dat, long unite);
    
    public double stocks(long art, long trh, long depot, long ag, long ste, Date dat, long unite, long lot);

    public double stocksReel(long art, long trh, long depot, long ag, long ste, Date dat);

    public double stocksReel(long art, long trh, long depot, long ag, long ste, Date dat, long unite, long lot);

    public double stocksConsigner(long art, long trh, long depot, long ag, long ste, Date dat);

    public double stocksConsigner(long art, long trh, long depot, long ag, long ste, Date dat, long unite, long lot);
    
    public String controleStock(long article, long conditionnement, long depot, long tranche, double newQte, double oldQte, String action, String mouvement, Date date, long lot);

    public String controleStock(long article, long conditionnement, long oldConditionnement, long depot, long tranche, double newQte, double oldQte, String action, String mouvement, Date date, long lot) ;
    
    public double loadVersementAttendu(long users, Date dateDebut, Date dateFin);

    public double loadVersementReel(long users, Date dateDebut, Date dateFin);

    public double loadCaEnteteVente(long vente);

    public double loadCaVente(long vente);

    public double loadTaxeVente(long vente);

    public double loadCaAchat(long achat);

    public double loadTTCVente(long vente);

    public double loadTTCAchat(long achat);

    public double loadNetAPayerVente(long vente);

    public double loadNetAPayerAchat(long achat);

    public double loadCaVente(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca);

    public double loadCaVente(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca, String type);

    public double loadCaRVente(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca);

    public double loadCaRVente(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca, String type);

    public double loadCaLVente(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca);

    public double loadCaLVente(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca, String type);

    public double loadCaAchat(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca);

    public double loadCaAchat(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca, String type);

    public double loadCaRAchat(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca);

    public double loadCaRAchat(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca, String type);

    public double loadCaLAchat(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca);

    public double loadCaLAchat(boolean agence, long id, Date dateDebut, Date dateFin, String statut, boolean ca, String type);

    public double getTotalCaisse(long societe, long caisse, long mode, String table, String mouvement, String type, Character statut, Date date);

    public double getTotalCaisse(long societe, long caisse, long mode, String mouvement, String type, Character statut, Date date);

    public double getTotalCaisse(long caisse, long mode, String mouvement, String type, Character statut, Date date);

    public double getDepenseCaisse(long caisse, String table, Character statut);

    public double getRecetteCaisse(long caisse, String table, Character statut);

    public double getSoldeCaisse(long caisse, String table, Character statut);

    public double getRetraitCaisseMut(long caisse, String table, Character statut);

    public double getDepotCaisseMut(long caisse, String table, Character statut);

    public double getSoldeCaisseMut(long caisse, String table, Character statut);

    public double getSoldeCaisseMut(Date debut, Date fin);

    public Double getSoldeCompteMutualiste(long compte);

    public boolean updateDataForClient(long client);

    public boolean fusionneData(String table, long newValue, long oldValue);

    public boolean fusionneData(String table, long newValue, String oldValue);

    public String nameTiers(long id, String table, String type);

    public double arrondi(long societe, double valeur);

    public List<String> field(String table, String colonne, String tableLiee);

    public void loadInfos(YvsSocietes currentScte, YvsAgences currentAgence, YvsUsersAgence currentUser, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint, YvsBaseExercice currentExo);

    public List<YvsComMensualiteFactureVente> generatedEcheancierReg(YvsComDocVentes y, YvsUsersAgence currentUser, YvsSocietes currentScte);

    public List<YvsComMensualiteFactureVente> generatedEcheancierReg(YvsComDocVentes y, YvsBaseModelReglement m, double total, YvsUsersAgence currentUser, YvsSocietes currentScte);

    public boolean generatedEcheancierReg(YvsComDocVentes y, boolean addList, YvsUsersAgence currentUser, YvsSocietes currentScte);

    public double setMontantTotalDoc(YvsComDocAchats doc, long societe);

    public double setMontantTotalDoc(YvsComDocAchats doc, List<YvsComContenuDocAchat> lc, long societe);

    public double setMontantTotalDoc(YvsComDocVentes doc, long societe);

    public double setMontantTotalDoc(YvsComDocVentes doc, List<YvsComContenuDocVente> lc, long societe);

    public String validEtapeFactureAchat(YvsComDocAchats selectDoc, YvsWorkflowValidFactureAchat etape, YvsUsersAgence currentUser);

    public String annulEtapeFactureAchat(YvsComDocAchats current, YvsWorkflowValidFactureAchat etape, String motif, YvsUsersAgence currentUser);

    public String validEtapeFactureVente(YvsComDocVentes current, YvsWorkflowValidFactureVente etape, YvsUsersAgence currentUser);

    public String annulEtapeFactureVente(YvsComDocVentes current, YvsWorkflowValidFactureVente etape, String motif, YvsUsersAgence currentUser);

    public String validEtapeOrdreDivers(YvsComptaCaisseDocDivers current, YvsWorkflowValidDocCaisse etape, YvsUsersAgence currentUser);

    public String annulEtapeOrdreDivers(YvsComptaCaisseDocDivers current, YvsWorkflowValidDocCaisse etape, String motif, YvsUsersAgence currentUser);

    public String validEtapeOrdreConge(YvsGrhCongeEmps current, YvsWorkflowValidConge etape, YvsUsersAgence currentUser);

    public String annulEtapeOrdreConge(YvsGrhCongeEmps current, YvsWorkflowValidConge etape, String motif, YvsUsersAgence currentUser);

    public String validEtapeOrdreMission(YvsGrhMissions current, YvsWorkflowValidMission etape, YvsUsersAgence currentUser);

    public String annulEtapeOrdreMission(YvsGrhMissions current, YvsWorkflowValidMission etape, String motif, YvsUsersAgence currentUser);

    public String validEtapeDocStock(YvsComDocStocks current, YvsWorkflowValidDocStock etape, YvsUsersAgence currentUser);

    public String annulEtapeDocStock(YvsComDocStocks current, YvsWorkflowValidDocStock etape, String motif, YvsUsersAgence currentUser);

    public String validEtapeApprovisionnement(YvsComFicheApprovisionnement current, YvsWorkflowValidApprovissionnement etape, YvsUsersAgence currentUser);

    public String annulEtapeApprovisionnement(YvsComFicheApprovisionnement current, YvsWorkflowValidApprovissionnement etape, String motif, YvsUsersAgence currentUser);

    public String validEtapeBonProvisoire(YvsComptaBonProvisoire current, YvsWorkflowValidBonProvisoire etape, YvsUsersAgence currentUser);

    public String annulEtapeBonProvisoire(YvsComptaBonProvisoire current, YvsWorkflowValidBonProvisoire etape, String motif, YvsUsersAgence currentUser);

    public List<YvsComptaContentJournal> buildContentJournal(long id, String table, YvsSocietes societe, String agence);

    public boolean isComptabilise(long id, String table);

    public boolean isComptabilise(List<Long> ids, String table);

    public boolean isComptabilise(long id, String table, boolean all);

    public boolean isComptabilise(List<Long> ids, String table, boolean all);

    public boolean isComptabilise(Long id, String table, boolean all, Character source);

    public boolean isComptabilise(List<Long> ids, String table, boolean all, Character source);

    public YvsComptaPiecesComptable saveNewPieceComptable(Date dateDoc, YvsComptaJournaux jrn, List<YvsComptaContentJournal> contenus, YvsUsersAgence currentUser);

    public boolean controleInventaire(long depot, Date date, long tranche);

    public YvsComDocStocks lastInventaire(long depot, Date date, long tranche);

    public boolean controleEcartVente(long users, Date date);

    public YvsComEnteteDocVente getEntete(YvsUsers u, YvsBasePointVente p, YvsGrhTrancheHoraire t, Date date, YvsNiveauAcces currentNiveau, YvsAgences currentAgence, YvsUsersAgence currentUser);
}
