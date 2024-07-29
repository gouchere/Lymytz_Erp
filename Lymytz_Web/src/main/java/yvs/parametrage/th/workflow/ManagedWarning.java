/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.th.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.ManagedArticles;
import yvs.commercial.achat.ManagedBonAvoirAchat;
import yvs.commercial.achat.ManagedFactureAchat;
import yvs.commercial.achat.ManagedFicheAppro;
import yvs.commercial.achat.ManagedLivraisonAchat;
import yvs.commercial.client.ManagedClient;
import yvs.commercial.depot.ManagedDepot;
import yvs.commercial.depot.ManagedPointVente;
import yvs.commercial.fournisseur.ManagedFournisseur;
import yvs.commercial.stock.ManagedInventaire;
import yvs.commercial.stock.ManagedOtherTransfert;
import yvs.commercial.stock.ManagedReconditionnement;
import yvs.commercial.stock.ManagedStockArticle;
import yvs.commercial.stock.ManagedTransfertStock;
import yvs.commercial.vente.ManagedBonAvoirVente;
import yvs.commercial.vente.ManagedFactureVenteV2;
import yvs.commercial.vente.ManagedLivraisonVente;
import yvs.comptabilite.caisse.ManagedCaisses;
import yvs.comptabilite.caisse.ManagedPieceCaisse;
import yvs.comptabilite.tresorerie.ManagedBonProvisoire;
import yvs.comptabilite.tresorerie.ManagedDocDivers;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseCategorieFournisseur;
import yvs.entity.base.YvsBaseClassesStat;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBaseMouvementStock;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.YvsComCategoriePersonnel;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComFicheApprovisionnement;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;
import yvs.entity.commercial.creneau.YvsComCreneauPoint;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsBaseTypeDocDivers;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaMouvementCaisse;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.grh.activite.YvsGrhCongeEmps;
import yvs.entity.grh.activite.YvsGrhFormation;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.grh.activite.YvsGrhObjetsMission;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.personnel.YvsGrhProfil;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.workflow.YvsWorkflowAlertes;
import yvs.entity.param.workflow.YvsWorkflowAlertesUsers;
import yvs.entity.param.workflow.YvsWorkflowModelDoc;
import yvs.entity.production.base.YvsProdGammeArticle;
import yvs.entity.production.base.YvsProdNomenclature;
import yvs.entity.production.pilotage.YvsProdDeclarationProduction;
import yvs.entity.production.pilotage.YvsProdOrdreFabrication;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.entity.produits.group.YvsBaseGroupesArticle;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsUserViewAlertes;
import yvs.entity.users.YvsUsers;
import yvs.grh.ManagedEmployes;
import yvs.grh.bean.ManagedConges;
import yvs.grh.bean.ManagedFormation;
import yvs.grh.bean.mission.ManagedMission;
import yvs.production.ManagedOrdresF;
import yvs.users.ManagedUser;
import yvs.util.Constantes;
import yvs.util.Managed;

/**
 *
 * @author Lymytz-pc
 */
@ManagedBean
@SessionScoped
public class ManagedWarning extends Managed<Object[], Object[]> implements Serializable {

    private Warning warning = new Warning();
    private List<YvsBaseArticles> articles;
    private List<YvsComDocAchats> achats;
    private List<YvsComDocVentes> ventes;
    private List<YvsComDocStocks> stocks;
    private List<YvsGrhCongeEmps> conges;
    private List<YvsGrhMissions> missions;
    private List<YvsGrhFormation> formations;
    private List<YvsComptaCaisseDocDivers> divers;
    private List<YvsBaseArticleDepot> articleDepots;
    private List<YvsBaseMouvementStock> mouvements;
    private List<YvsProdDeclarationProduction> declarations;
    private List<YvsProdOrdreFabrication> fabrications;
    private List<YvsComContenuDocVente> contenus;
    private List<YvsComFicheApprovisionnement> approvisionnements;
    private List<YvsComptaMouvementCaisse> piecesCaisse;
    private List<YvsBaseDepots> depots;
    private List<YvsBasePointVente> pointsVente;
    private List<YvsBaseCaisse> caisses;
    private List<YvsBaseFournisseur> fournisseurs;
    private List<YvsComClient> clients;
    private List<YvsGrhEmployes> employes;
    private List<YvsUsers> users;

    private List<YvsUserViewAlertes> currentsWarning;

    public ManagedWarning() {
        articles = new ArrayList<>();
        users = new ArrayList<>();
        achats = new ArrayList<>();
        ventes = new ArrayList<>();
        stocks = new ArrayList<>();
        divers = new ArrayList<>();
        conges = new ArrayList<>();
        missions = new ArrayList<>();
        formations = new ArrayList<>();
        articleDepots = new ArrayList<>();
        mouvements = new ArrayList<>();
        declarations = new ArrayList<>();
        fabrications = new ArrayList<>();
        contenus = new ArrayList<>();
        piecesCaisse = new ArrayList<>();
        approvisionnements = new ArrayList<>();
        currentsWarning = new ArrayList<>();
        depots = new ArrayList<>();
        pointsVente = new ArrayList<>();
        caisses = new ArrayList<>();
        fournisseurs = new ArrayList<>();
        clients = new ArrayList<>();
        employes = new ArrayList<>();
    }

    public List<YvsUsers> getUsers() {
        return users;
    }

    public void setUsers(List<YvsUsers> users) {
        this.users = users;
    }

    public List<YvsBaseDepots> getDepots() {
        return depots;
    }

    public void setDepots(List<YvsBaseDepots> depots) {
        this.depots = depots;
    }

    public List<YvsBasePointVente> getPointsVente() {
        return pointsVente;
    }

    public void setPointsVente(List<YvsBasePointVente> pointsVente) {
        this.pointsVente = pointsVente;
    }

    public List<YvsBaseCaisse> getCaisses() {
        return caisses;
    }

    public void setCaisses(List<YvsBaseCaisse> caisses) {
        this.caisses = caisses;
    }

    public List<YvsBaseFournisseur> getFournisseurs() {
        return fournisseurs;
    }

    public void setFournisseurs(List<YvsBaseFournisseur> fournisseurs) {
        this.fournisseurs = fournisseurs;
    }

    public List<YvsComClient> getClients() {
        return clients;
    }

    public void setClients(List<YvsComClient> clients) {
        this.clients = clients;
    }

    public List<YvsGrhEmployes> getEmployes() {
        return employes;
    }

    public void setEmployes(List<YvsGrhEmployes> employes) {
        this.employes = employes;
    }

    public Warning getWarning() {
        return warning;
    }

    public void setWarning(Warning warning) {
        this.warning = warning;
    }

    public List<YvsUserViewAlertes> getCurrentsWarning() {
        return currentsWarning;
    }

    public void setCurrentsWarning(List<YvsUserViewAlertes> currentsWarning) {
        this.currentsWarning = currentsWarning;
    }

    public List<YvsGrhCongeEmps> getConges() {
        return conges;
    }

    public void setConges(List<YvsGrhCongeEmps> conges) {
        this.conges = conges;
    }

    public List<YvsGrhMissions> getMissions() {
        return missions;
    }

    public void setMissions(List<YvsGrhMissions> missions) {
        this.missions = missions;
    }

    public List<YvsGrhFormation> getFormations() {
        return formations;
    }

    public void setFormations(List<YvsGrhFormation> formations) {
        this.formations = formations;
    }

    public List<YvsComptaMouvementCaisse> getPiecesCaisse() {
        return piecesCaisse;
    }

    public void setPiecesCaisse(List<YvsComptaMouvementCaisse> piecesCaisse) {
        this.piecesCaisse = piecesCaisse;
    }

    public List<YvsComDocAchats> getAchats() {
        return achats;
    }

    public List<YvsComFicheApprovisionnement> getApprovisionnements() {
        return approvisionnements;
    }

    public void setApprovisionnements(List<YvsComFicheApprovisionnement> approvisionnements) {
        this.approvisionnements = approvisionnements;
    }

    public void setAchats(List<YvsComDocAchats> achats) {
        this.achats = achats;
    }

    public List<YvsComDocVentes> getVentes() {
        return ventes;
    }

    public void setVentes(List<YvsComDocVentes> ventes) {
        this.ventes = ventes;
    }

    public List<YvsComDocStocks> getStocks() {
        return stocks;
    }

    public void setStocks(List<YvsComDocStocks> stocks) {
        this.stocks = stocks;
    }

    public List<YvsComptaCaisseDocDivers> getDivers() {
        return divers;
    }

    public void setDivers(List<YvsComptaCaisseDocDivers> divers) {
        this.divers = divers;
    }

    public List<YvsBaseArticles> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticles> articles) {
        this.articles = articles;
    }

    public List<YvsBaseArticleDepot> getArticleDepots() {
        return articleDepots;
    }

    public void setArticleDepots(List<YvsBaseArticleDepot> articles) {
        this.articleDepots = articles;
    }

    public List<YvsBaseMouvementStock> getMouvements() {
        return mouvements;
    }

    public void setMouvements(List<YvsBaseMouvementStock> mouvements) {
        this.mouvements = mouvements;
    }

    public List<YvsProdDeclarationProduction> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<YvsProdDeclarationProduction> declarations) {
        this.declarations = declarations;
    }

    public List<YvsProdOrdreFabrication> getFabrications() {
        return fabrications;
    }

    public void setFabrications(List<YvsProdOrdreFabrication> fabrications) {
        this.fabrications = fabrications;
    }

    public List<YvsComContenuDocVente> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComContenuDocVente> contenus) {
        this.contenus = contenus;
    }

    @Override
    public boolean controleFiche(Object[] bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void buildToManager(String type) {
        buildToManager("=", type);
    }

    public void buildToManager(String egalite, String type) {
        currentsWarning.clear();
        try {
            List<Object> params = new ArrayList<>();
            String query = "SELECT COALESCE(y.description, m.description), COUNT(y.id), m.titre_doc, y.nature_alerte, m.id "
                    + "FROM yvs_workflow_alertes y INNER JOIN yvs_workflow_model_doc m ON y.model_doc = m.id "
                    + "INNER JOIN yvs_agences a ON y.agence = a.id "
                    + "INNER JOIN yvs_warning_model_doc w ON w.societe = a.societe AND w.model = m.id "
                    + "LEFT JOIN yvs_workflow_alertes_users s ON s.alerte = y.id "
                    + "WHERE (COALESCE((current_date - COALESCE(y.date_doc, current_date)), 0) " + ((type.equals("I") && egalite.equals("=")) ? "<=" : ">") + " COALESCE(w.ecart, 0)) AND COALESCE(y.actif, TRUE) IS TRUE "
                    + "AND m.nature " + egalite + " ? AND m.id IN (SELECT DISTINCT u.document_type FROM yvs_user_view_alertes u WHERE u.users = ? AND u.actif = true) ";
            params.add(type);
            params.add((currentUser.getUsers() != null ? currentUser.getUsers().getId() : 0));
            if (!autoriser("param_warning_view_all")) {
                params.add(currentAgence.getId());
                query += "AND a.id = ? ";
            } else {
                params.add((currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0));
                query += "AND a.societe = ? ";
            }
            query += "GROUP BY m.id, y.nature_alerte, y.description ORDER BY m.description";
            List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
            YvsWorkflowModelDoc m;
            YvsUserViewAlertes y;

            for (Object[] r : result) {
                m = (YvsWorkflowModelDoc) dao.loadOneByNameQueries("YvsWorkflowModelDoc.findById", new String[]{"id"}, new Object[]{(Integer) r[4]});
                if (m != null ? m.getId() > 0 : false) {
                    y = (YvsUserViewAlertes) dao.loadOneByNameQueries("YvsUserViewAlertes.findOne", new String[]{"users", "document"}, new Object[]{currentUser.getUsers(), m});
                    if (y != null ? y.getId() < 1 : true) {
                        y = new YvsUserViewAlertes(YvsUserViewAlertes.ids--);
                        y.setActif(true);
                        y.setDocumentType(m);
                        y.setUsers(currentUser.getUsers());
                        y.setVoir(true);
                    }
                    y.setAuthor(currentUser);
                    y.getDocumentType().setDescription((String) r[0]);
                    y.setCount((Long) r[1]);
                    currentsWarning.add(y);
                }
            }
        } catch (Exception ex) {
            getException("buildToManager", ex);
        }
        update("data_alert_users");
    }

    public void displayContent(YvsUserViewAlertes y) {
        try {
            List<Object> params = new ArrayList<>();
            String query = "SELECT y.id, y.nature_alerte, y.id_element, y.date_doc, s.id, COALESCE(s.voir, TRUE) voir "
                    + "FROM yvs_workflow_alertes y INNER JOIN yvs_agences a ON y.agence = a.id "
                    + "INNER JOIN yvs_warning_model_doc w ON w.societe = a.societe AND w.model = y.model_doc "
                    + "LEFT JOIN yvs_workflow_alertes_users s On s.alerte = y.id "
                    + "WHERE (COALESCE((current_date - COALESCE(y.date_doc, current_date)), 0) " + (y.getDocumentType().getNature().equals("I") ? "<=" : ">") + " COALESCE(w.ecart, 0)) "
                    + "AND COALESCE(y.actif, TRUE) IS TRUE AND y.model_doc = ? ";
            params.add(y.getDocumentType().getId());
            if (!autoriser("param_warning_view_all")) {
                params.add(currentAgence.getId());
                query += " AND a.id = ?";
            } else {
                params.add((currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0));
                query += " AND a.societe = ?";
            }
            List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
            y.setAlertes(new ArrayList<YvsWorkflowAlertesUsers>());
            YvsWorkflowAlertesUsers u;
            YvsWorkflowAlertes a;
            for (Object[] r : result) {
                a = new YvsWorkflowAlertes((Long) r[0]);
                a.setNatureAlerte((String) r[1]);
                a.setIdElement((Long) r[2]);
                a.setDateDoc(new Date(((java.sql.Date) r[3]).getTime()));
                u = new YvsWorkflowAlertesUsers(r[4] != null ? (Long) r[4] : YvsWorkflowAlertesUsers.ids--);
                u.setVoir((Boolean) r[5]);
                u.setAlerte(a);
                u.setUsers(currentUser.getUsers());
                u.setAuthor(currentUser);
                y.getAlertes().add(u);
            }
            update("dt_row_ex_" + y.getId());
        } catch (Exception ex) {
            getException("displayContent", ex);
        }
    }

    public void activeOrDesactiveView(YvsUserViewAlertes y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                y.setVoir(!y.getVoir());
                y.setDateUpdate(new Date());
                dao.update(y);
                succes();
            }
        } catch (Exception ex) {
            getException("activeOrDesactiveView", ex);
        }
    }

    public void activeOrDesactiveView(YvsUserViewAlertes r, YvsWorkflowAlertesUsers y) {
        try {
            if (y != null) {
                long id = y.getId();
                y.setVoir(!y.getVoir());
                y.setDateUpdate(new Date());
                if (id > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsWorkflowAlertesUsers) dao.save1(y);
                }
                int index = r.getAlertes().indexOf(new YvsWorkflowAlertesUsers(id));
                if (index > -1) {
                    r.getAlertes().set(index, y);
                }
                update("data_alert_users:dt_row_ex_" + r.getId());
                succes();
            }
        } catch (Exception ex) {
            getException("activeOrDesactiveView", ex);
        }
    }

    public void execute_activeOrDesactiveView() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            long a = Long.valueOf(params.get("a"));
            long u = Long.valueOf(params.get("u"));
            if (a > 0) {
                int index = currentsWarning.indexOf(new YvsUserViewAlertes(a));
                if (index > -1) {
                    YvsUserViewAlertes r = currentsWarning.get(index);
                    index = r.getAlertes().indexOf(new YvsWorkflowAlertesUsers(u));
                    if (index > -1) {
                        YvsWorkflowAlertesUsers y = r.getAlertes().get(index);
                        activeOrDesactiveView(r, y);
                    }
                }
            }
        } catch (Exception ex) {
            getException("execute_activeOrDesactiveView", ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            switch (warning.getTitre()) {
                case Constantes.DOCUMENT_MISSION: {
                    YvsGrhMissions y = (YvsGrhMissions) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedMission w = (ManagedMission) giveManagedBean(ManagedMission.class);
                        if (w != null) {
                            w.onSelectDistant((YvsGrhMissions) dao.loadOneByNameQueries("YvsGrhMissions.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_FORMATION: {
                    YvsGrhFormation y = (YvsGrhFormation) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedFormation w = (ManagedFormation) giveManagedBean(ManagedFormation.class);
                        if (w != null) {
                            w.onSelectDistant((YvsGrhFormation) dao.loadOneByNameQueries("YvsGrhFormation.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_BON_DIVERS_CAISSE: {
                    YvsComptaBonProvisoire y = (YvsComptaBonProvisoire) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedBonProvisoire w = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
                        if (w != null) {
                            w.onSelectDistant((YvsComptaBonProvisoire) dao.loadOneByNameQueries("YvsComptaBonProvisoire.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_APPROVISIONNEMENT: {
                    YvsComFicheApprovisionnement y = (YvsComFicheApprovisionnement) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedFicheAppro w = (ManagedFicheAppro) giveManagedBean(ManagedFicheAppro.class);
                        if (w != null) {
                            w.onSelectDistant((YvsComFicheApprovisionnement) dao.loadOneByNameQueries("YvsComFicheApprovisionnement.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_PIECE_CAISSE: {
                    YvsComptaMouvementCaisse y = (YvsComptaMouvementCaisse) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedPieceCaisse w = (ManagedPieceCaisse) giveManagedBean(ManagedPieceCaisse.class);
                        if (w != null) {
                            w.onSelectDistant((YvsComptaMouvementCaisse) dao.loadOneByNameQueries("YvsComptaMouvementCaisse.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_HIGH_PR_ARTICLE:
                    break;
                case Constantes.DOCUMENT_STOCK_ARTICLE: {
                    YvsBaseArticleDepot y = (YvsBaseArticleDepot) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedStockArticle w = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                        if (w != null) {
                            w.onSelectDistant((YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_ARTICLE:
                case Constantes.DOCUMENT_ARTICLE_NON_MOUVEMENTE: {
                    YvsBaseArticles y = (YvsBaseArticles) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedArticles w = (ManagedArticles) giveManagedBean("Marticle");
                        if (w != null) {
                            w.onSelectDistant((YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{y.getId()}));
                            if (warning.getTitre().equals(Constantes.DOCUMENT_ARTICLE)) {
                                onMarkRead(y.getId());
                            }
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_PERMISSION_COURTE_DUREE:
                case Constantes.DOCUMENT_CONGES: {
                    YvsGrhCongeEmps y = (YvsGrhCongeEmps) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedConges w = (ManagedConges) giveManagedBean(ManagedConges.class);
                        if (w != null) {
                            w.onSelectDistant((YvsGrhCongeEmps) dao.loadOneByNameQueries("YvsGrhCongeEmps.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_CP_UPPER_PR:
                case Constantes.DOCUMENT_ORDRE_FABRICATION_TERMINE:
                case Constantes.DOCUMENT_ORDRE_FABRICATION_DECLARE: {
                    YvsProdOrdreFabrication y = (YvsProdOrdreFabrication) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedOrdresF w = (ManagedOrdresF) giveManagedBean(ManagedOrdresF.class);
                        if (w != null) {
                            w.onSelectDistant((YvsProdOrdreFabrication) dao.loadOneByNameQueries("YvsProdOrdreFabrication.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_RECONDITIONNEMENT_STOCK: {
                    YvsComDocStocks y = (YvsComDocStocks) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedReconditionnement w = (ManagedReconditionnement) giveManagedBean(ManagedReconditionnement.class);
                        if (w != null) {
                            w.onSelectDistant((YvsComDocStocks) dao.loadOneByNameQueries("YvsComDocStocks.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_INVENTAIRE_STOCK: {
                    YvsComDocStocks y = (YvsComDocStocks) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedInventaire w = (ManagedInventaire) giveManagedBean(ManagedInventaire.class);
                        if (w != null) {
                            w.onSelectDistant((YvsComDocStocks) dao.loadOneByNameQueries("YvsComDocStocks.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_TRANSFERT: {
                    YvsComDocStocks y = (YvsComDocStocks) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedTransfertStock w = (ManagedTransfertStock) giveManagedBean(ManagedTransfertStock.class);
                        if (w != null) {
                            w.onSelectDistant((YvsComDocStocks) dao.loadOneByNameQueries("YvsComDocStocks.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_SORTIE:
                case Constantes.DOCUMENT_ENTREE: {
                    YvsComDocStocks y = (YvsComDocStocks) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedOtherTransfert w = (ManagedOtherTransfert) giveManagedBean(ManagedOtherTransfert.class);
                        if (w != null) {
                            w.onSelectDistant((YvsComDocStocks) dao.loadOneByNameQueries("YvsComDocStocks.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_DOC_DIVERS_RECETTE:
                case Constantes.DOCUMENT_DOC_DIVERS_DEPENSE:
                case Constantes.DOCUMENT_DOC_DIVERS_CAISSE:
                case Constantes.DOCUMENT_OD_NON_PLANNIFIE:
                case Constantes.DOCUMENT_OD_NON_COMPTABILISE: {
                    YvsComptaCaisseDocDivers y = (YvsComptaCaisseDocDivers) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                        if (w != null) {
                            w.onSelectDistant((YvsComptaCaisseDocDivers) dao.loadOneByNameQueries("YvsComptaCaisseDocDivers.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_BON_LIVRAISON_ACHAT: {
                    YvsComDocAchats y = (YvsComDocAchats) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedLivraisonAchat w = (ManagedLivraisonAchat) giveManagedBean(ManagedLivraisonAchat.class);
                        if (w != null) {
                            w.onSelectDistant((YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_RETOUR_ACHAT:
                case Constantes.DOCUMENT_AVOIR_ACHAT: {
                    YvsComDocAchats y = (YvsComDocAchats) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedBonAvoirAchat w = (ManagedBonAvoirAchat) giveManagedBean(ManagedBonAvoirAchat.class);
                        if (w != null) {
                            w.onSelectDistant((YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_FACTURE_ACHAT_REGLE:
                case Constantes.DOCUMENT_FACTURE_ACHAT_LIVRE:
                case Constantes.DOCUMENT_FACTURE_ACHAT:
                case Constantes.DOCUMENT_ACHAT_NON_COMPTABILISE: {
                    YvsComDocAchats y = (YvsComDocAchats) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedFactureAchat w = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                        if (w != null) {
                            w.onSelectDistant((YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_AVOIR_VENTE:
                case Constantes.DOCUMENT_RETOUR_VENTE: {
                    YvsComDocVentes y = (YvsComDocVentes) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedBonAvoirVente w = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                        if (w != null) {
                            w.onSelectDistant((YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_BON_LIVRAISON_VENTE: {
                    YvsComDocVentes y = (YvsComDocVentes) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedLivraisonVente w = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                        if (w != null) {
                            w.onSelectDistant((YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_LOWER_MARGIN: {
                    YvsComContenuDocVente y = (YvsComContenuDocVente) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                        if (w != null) {
                            w.onSelectDistant((YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{y.getDocVente().getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_FACTURE_VENTE_REGLE:
                case Constantes.DOCUMENT_FACTURE_VENTE_LIVRE:
                case Constantes.DOCUMENT_FACTURE_VENTE:
                case Constantes.DOCUMENT_VENTE_NON_COMPTABILISE: {
                    YvsComDocVentes y = (YvsComDocVentes) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                        if (w != null) {
                            w.onSelectDistant((YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{y.getId()}));
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_PHASE_CAISSE_SALAIRE:
                case Constantes.DOCUMENT_PHASE_CAISSE_DIVERS:
                case Constantes.DOCUMENT_PHASE_CAISSE_VENTE:
                case Constantes.DOCUMENT_PHASE_CAISSE_ACHAT:
                case Constantes.DOCUMENT_PHASE_ACOMPTE_VENTE:
                case Constantes.DOCUMENT_PHASE_ACOMPTE_ACHAT:
                case Constantes.DOCUMENT_PHASE_CREDIT_VENTE:
                case Constantes.DOCUMENT_PHASE_CREDIT_ACHAT: {
//                    YvsComDocVentes y = (YvsComDocVentes) ev.getObject();
//                    if (y != null ? y.getId() > 0 : false) {
//                        ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
//                        if (w != null) {
//                            w.onSelectDistant((YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{y.getId()}));
//                        }
//                    }
                    break;
                }
                case Constantes.DOCUMENT_USERS: {
                    YvsUsers y = (YvsUsers) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedUser w = (ManagedUser) giveManagedBean(ManagedUser.class);
                        if (w != null) {
                            w.onSelectDistant((YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{y.getId()}));
                            onMarkRead(y.getId());
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_EMPLOYE: {
                    YvsGrhEmployes y = (YvsGrhEmployes) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedEmployes w = (ManagedEmployes) giveManagedBean("MEmps");
                        if (w != null) {
                            w.onSelectDistant((YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findById", new String[]{"id"}, new Object[]{y.getId()}));
                            onMarkRead(y.getId());
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_CLIENT: {
                    YvsComClient y = (YvsComClient) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
                        if (w != null) {
                            w.onSelectDistant((YvsComClient) dao.loadOneByNameQueries("YvsComClient.findById", new String[]{"id"}, new Object[]{y.getId()}));
                            onMarkRead(y.getId());
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_FOURNISSEUR: {
                    YvsBaseFournisseur y = (YvsBaseFournisseur) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedFournisseur w = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
                        if (w != null) {
                            w.onSelectDistant((YvsBaseFournisseur) dao.loadOneByNameQueries("YvsBaseFournisseur.findById", new String[]{"id"}, new Object[]{y.getId()}));
                            onMarkRead(y.getId());
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_CAISSE: {
                    YvsBaseCaisse y = (YvsBaseCaisse) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                        if (w != null) {
                            w.onSelectDistant((YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{y.getId()}));
                            onMarkRead(y.getId());
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_DEPOT: {
                    YvsBaseDepots y = (YvsBaseDepots) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedDepot w = (ManagedDepot) giveManagedBean(ManagedDepot.class);
                        if (w != null) {
                            w.onSelectDistant((YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{y.getId()}));
                            onMarkRead(y.getId());
                        }
                    }
                    break;
                }
                case Constantes.DOCUMENT_POINTVENTE: {
                    YvsBasePointVente y = (YvsBasePointVente) ev.getObject();
                    if (y != null ? y.getId() > 0 : false) {
                        ManagedPointVente w = (ManagedPointVente) giveManagedBean(ManagedDepot.class);
                        if (w != null) {
                            w.onSelectDistant((YvsBasePointVente) dao.loadOneByNameQueries("YvsBasePointVente.findById", new String[]{"id"}, new Object[]{y.getId()}));
                            onMarkRead(y.getId());
                        }
                    }
                    break;
                }
            }
        }
    }

    private void onMarkRead(Long id) {
        try {
            YvsWorkflowAlertes a = (YvsWorkflowAlertes) dao.loadOneByNameQueries("YvsWorkflowAlertes.findByElement", new String[]{"element", "nature", "titre"}, new Object[]{id, warning.getNature(), warning.getTitre()});
            if (a != null ? a.getId() > 0 : false) {
                YvsWorkflowAlertesUsers y = (YvsWorkflowAlertesUsers) dao.loadOneByNameQueries("YvsWorkflowAlertesUsers.findByAlerteUsers", new String[]{"alerte", "users"}, new Object[]{a, currentUser.getUsers()});
                if (y != null ? y.getId() < 1 : true) {
                    y = new YvsWorkflowAlertesUsers();
                    y.setAlerte(a);
                    y.setAuthor(currentUser);
                    y.setUsers(currentUser.getUsers());
                    y.setVoir(false);
                    dao.save(y);
                } else {
                    y.setVoir(false);
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                }
            }

        } catch (Exception ex) {
            getException("onMarkRead", ex);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSelectDistant(Object[] y) {
        if (y != null ? y.length > 0 : false) {
            warning = new Warning().build(y);
            List<Object> params = new ArrayList<>();
            params.add((currentUser.getUsers() != null ? currentUser.getUsers().getId() : 0));
            params.add(warning.getId());
            String query = "INNER JOIN yvs_workflow_alertes y ON y.id_element = _y_.id INNER JOIN yvs_workflow_model_doc m ON y.model_doc = m.id "
                    + "INNER JOIN yvs_agences a ON y.agence = a.id INNER JOIN yvs_warning_model_doc w ON w.societe = a.societe AND w.model = m.id "
                    + "INNER JOIN yvs_user_view_alertes u ON u.document_type = m.id LEFT JOIN yvs_workflow_alertes_users s ON s.alerte = y.id "
                    + "WHERE (COALESCE((current_date - COALESCE(y.date_doc, current_date)), 0) " + (warning.getType().equals("I") ? "<=" : ">") + " COALESCE(w.ecart, 0)) "
                    + "AND COALESCE(y.actif, TRUE) IS TRUE AND u.actif IS TRUE AND u.users = ? AND m.id = ? AND (s.voir IS NULL OR s.voir IS TRUE) ";
            if (!autoriser("param_warning_view_all")) {
                params.add(currentAgence.getId());
                query += "AND a.id = ? ";
            } else {
                params.add((currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0));
                query += "AND a.societe = ? ";
            }
            switch (warning.getTitre()) {
                case Constantes.DOCUMENT_MISSION: {
                    missions.clear();
                    query = "SELECT _y_.id, _y_.numero_mission, _y_.cloturer, _y_.reference_mission, _y_.duree_mission, COALESCE(_y_.statut_mission, 'E'), _y_.date_debut, _y_.date_fin, _y_.etape_valide, _y_.etape_total, "
                            + "_y_.employe, _e_.matricule, _e_.nom, _e_.prenom, _y_.objet_mission, _o_.titre, _y_.lieu, _d_.libele "
                            + "FROM yvs_com_fiche_approvisionnement _y_ LEFT JOIN yvs_base_depots _d_ ON _y_.depot = _d_.id LEFT JOIN yvs_com_creneau_depot _c_ ON _y_.creneau = _c_.id "
                            + "LEFT JOIN yvs_grh_tranche_horaire _t_ ON _c_.tranche = _t_.id "
                            + query + "ORDER BY _y_.date_approvisionnement DESC, _y_.reference DESC ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsGrhMissions r;
                    String string = null;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsGrhMissions((Long) data[i++]);
                        r.setNumeroMission((String) data[i++]);
                        r.setCloturer((Boolean) data[i++]);
                        r.setReferenceMission((String) data[i++]);
                        r.setDureeMission((Integer) data[i++]);
                        string = (String) data[i++];
                        r.setStatutMission(string.trim().length() > 0 ? string.charAt(0) : 'E');
                        r.setDateDebut(new Date(((java.sql.Date) data[i++]).getTime()));
                        r.setDateFin(new Date(((java.sql.Date) data[i++]).getTime()));
                        r.setEtapeValide((Integer) data[i++]);
                        r.setEtapeTotal((Integer) data[i++]);
                        r.setEmploye(new YvsGrhEmployes((Long) data[i++], (String) data[i++], "", (String) data[i++], (String) data[i++]));
                        r.setObjetMission(new YvsGrhObjetsMission((Integer) data[i++], (String) data[i++]));
                        r.setLieu(new YvsDictionnaire((Long) data[i++], (String) data[i++]));
                        missions.add(r);
                    }
                    break;
                }
                case Constantes.DOCUMENT_FORMATION:

                    break;
                case Constantes.DOCUMENT_BON_DIVERS_CAISSE:

                    break;
                case Constantes.DOCUMENT_APPROVISIONNEMENT: {
                    approvisionnements.clear();
                    query = "SELECT _y_.id, _y_.reference, _y_.cloturer, _y_.auto, _y_.etat, _y_.statut_terminer, _y_.date_approvisionnement, _y_.heure_approvisionnement, _y_.etape_valide, _y_.etape_total, "
                            + "_y_.depot, _d_.designation, _y_.creneau, _c_.tranche, _t_.type_journee, _t_.heure_debut, _t_.heure_fin "
                            + "FROM yvs_com_fiche_approvisionnement _y_ LEFT JOIN yvs_base_depots _d_ ON _y_.depot = _d_.id LEFT JOIN yvs_com_creneau_depot _c_ ON _y_.creneau = _c_.id "
                            + "LEFT JOIN yvs_grh_tranche_horaire _t_ ON _c_.tranche = _t_.id "
                            + query + "ORDER BY _y_.date_approvisionnement DESC, _y_.reference DESC ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsComFicheApprovisionnement r;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsComFicheApprovisionnement((Long) data[i++]);
                        r.setReference((String) data[i++]);
                        r.setCloturer((Boolean) data[i++]);
                        r.setAuto((Boolean) data[i++]);
                        r.setEtat((String) data[i++]);
                        r.setStatutTerminer((String) data[i++]);
                        r.setDateApprovisionnement(new Date(((java.sql.Date) data[i++]).getTime()));
                        r.setHeureApprovisionnement(new Date(((java.sql.Time) data[i++]).getTime()));
                        r.setEtapeValide((Integer) data[i++]);
                        r.setEtapeTotal((Integer) data[i++]);
                        r.setDepot(new YvsBaseDepots((Long) data[i++], (String) data[i++]));
                        r.setCreneau(new YvsComCreneauDepot((Long) data[i++], new YvsGrhTrancheHoraire((Long) data[i++], (String) data[i++], (java.sql.Time) data[i++], (java.sql.Time) data[i++]), r.getDepot()));
                        approvisionnements.add(r);
                    }
                    break;
                }
                case Constantes.DOCUMENT_PIECE_CAISSE: {
                    piecesCaisse.clear();
                    query = "SELECT DISTINCT _y_.id, _y_.numero, _y_.note, _y_.montant, COALESCE(_y_.statut_piece, 'W'), _y_.reference_externe, _y_.date_paiment_prevu, _y_.date_paye, _y_.caissier, _u_.nom_users, "
                            + "_y_.caisse::bigint, _c_.intitule, _y_.model::bigint, _m_.designation, _y_.name_tiers, _y_.mouvement "
                            + "FROM yvs_compta_mouvement_caisse _y_ LEFT JOIN yvs_users _u_ ON _y_.caissier = _u_.id LEFT JOIN yvs_base_mode_reglement _m_ ON _y_.model = _m_.id "
                            + "LEFT JOIN yvs_base_caisse _c_ ON _y_.caisse = _c_.id "
                            + query + "ORDER BY _y_.date_paiment_prevu, _y_.numero ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsComptaMouvementCaisse r;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsComptaMouvementCaisse((Long) data[i++]);
                        r.setNumero((String) data[i++]);
                        r.setNote((String) data[i++]);
                        r.setMontant((Double) data[i++]);
                        r.setStatutPiece(((String) data[i++]).charAt(0));
                        r.setNumeroExterne((String) data[i++]);
                        java.sql.Date date = (java.sql.Date) data[i++];
                        r.setDatePaimentPrevu(date != null ? new Date((date).getTime()) : null);
                        date = (java.sql.Date) data[i++];
                        r.setDatePaye(date != null ? new Date((date).getTime()) : null);
                        r.setCaissier(new YvsUsers((Long) data[i++], (String) data[i++]));
                        r.setCaisse(new YvsBaseCaisse((Long) data[i++], (String) data[i++]));
                        r.setModel(new YvsBaseModeReglement((Long) data[i++], (String) data[i++]));
                        r.setNameTiers((String) data[i++]);
                        r.setMouvement((String) data[i++]);
                        piecesCaisse.add(r);
                    }
                    break;
                }
                case Constantes.DOCUMENT_HIGH_PR_ARTICLE:

                    break;
                case Constantes.DOCUMENT_LOWER_MARGIN: {
                    contenus.clear();
                    query = "SELECT _y_.id, _y_.commentaire, _y_.quantite, _y_.prix, COALESCE(_y_.quantite_bonus, 0), COALESCE(_y_.remise, 0), COALESCE(_y_.rabais, 0), _y_.prix_total, COALESCE(_y_.puv_min, 0), COALESCE(_y_.pr, 0), COALESCE(_y_.statut_livree, 'W'), _y_.num_serie, "
                            + "_y_.article, _a_.ref_art, _a_.designation, _y_.conditionnement, _c_.unite::bigint, _u_.reference, _u_.libelle, "
                            + "_y_.article_bonus, _ab_.ref_art, _ab_.designation, _y_.conditionnement_bonus, _cb_.unite::bigint, _ub_.reference, _ub_.libelle, "
                            + "_y_.doc_vente, _d_.num_doc, _d_.statut, _d_.statut_livre, _d_.statut_regle "
                            + "FROM yvs_com_contenu_doc_vente _y_ LEFT JOIN yvs_com_doc_ventes _d_ ON _y_.doc_vente = _d_.id "
                            + "LEFT JOIN yvs_base_articles _a_ ON _y_.article = _a_.id LEFT JOIN yvs_base_articles _ab_ ON _y_.article_bonus = _ab_.id "
                            + "LEFT JOIN yvs_base_conditionnement _c_ ON _y_.conditionnement = _c_.id LEFT JOIN yvs_base_unite_mesure _u_ ON _c_.unite = _u_.id "
                            + "LEFT JOIN yvs_base_conditionnement _cb_ ON _y_.conditionnement_bonus = _cb_.id LEFT JOIN yvs_base_unite_mesure _ub_ ON _cb_.unite = _ub_.id "
                            + query + "ORDER BY _d_.num_doc DESC, _y_.id DESC ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsComContenuDocVente r;
                    String string = null;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsComContenuDocVente((Long) data[i++]);
                        r.setCommentaire((String) data[i++]);
                        r.setQuantite((Double) data[i++]);
                        r.setPrix((Double) data[i++]);
                        r.setQuantiteBonus((Double) data[i++]);
                        r.setRemise((Double) data[i++]);
                        r.setRabais((Double) data[i++]);
                        r.setPrixTotal((Double) data[i++]);
                        r.setPuvMin((Double) data[i++]);
                        r.setPr((Double) data[i++]);
                        string = (String) data[i++];
                        r.setStatutLivree(string.trim().length() > 0 ? string.charAt(0) : 'W');
                        r.setNumSerie((String) data[i++]);
                        r.setArticle(new YvsBaseArticles((Long) data[i++], (String) data[i++], (String) data[i++]));
                        r.setConditionnement(new YvsBaseConditionnement((Long) data[i++], new YvsBaseUniteMesure((Long) data[i++], (String) data[i++], (String) data[i++])));
                        r.setArticleBonus(new YvsBaseArticles((Long) data[i++], (String) data[i++], (String) data[i++]));
                        r.setConditionnementBonus(new YvsBaseConditionnement((Long) data[i++], new YvsBaseUniteMesure((Long) data[i++], (String) data[i++], (String) data[i++])));
                        r.setDocVente(new YvsComDocVentes((Long) data[i++], (String) data[i++], (String) data[i++], (String) data[i++], (String) data[i++]));
                        contenus.add(r);
                    }
                    break;
                }
                case Constantes.DOCUMENT_ARTICLE:
                case Constantes.DOCUMENT_STOCK_ARTICLE:
                case Constantes.DOCUMENT_ARTICLE_NON_MOUVEMENTE: {
                    articles.clear();
                    query = "SELECT DISTINCT _y_.id, _y_.ref_art, _y_.designation, _y_.categorie, COALESCE(_y_.type_service, 'C'), _y_.actif, _y_.famille::bigint, _f_.designation, _y_.classe1::bigint, _c_.designation, "
                            + "_y_.groupe::bigint, _g_.designation "
                            + "FROM yvs_base_articles _y_ LEFT JOIN yvs_base_famille_article _f_ ON _y_.famille = _f_.id LEFT JOIN yvs_base_classes_stat _c_ ON _y_.classe1 = _c_.id "
                            + "LEFT JOIN yvs_base_groupes_article _g_ ON _y_.groupe = _g_.id "
                            + query + "ORDER BY _y_.designation, _y_.ref_art ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsBaseArticles r;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsBaseArticles((Long) data[i++]);
                        r.setRefArt((String) data[i++]);
                        r.setDesignation((String) data[i++]);
                        r.setCategorie((String) data[i++]);
                        r.setTypeService(((String) data[i++]).charAt(0));
                        r.setActif((Boolean) data[i++]);
                        r.setFamille(new YvsBaseFamilleArticle((Long) data[i++], (String) data[i++]));
                        r.setClasse1(new YvsBaseClassesStat((Long) data[i++], (String) data[i++]));
                        r.setGroupe(new YvsBaseGroupesArticle((Long) data[i++], (String) data[i++]));
                        articles.add(r);
                    }
                    break;
                }
                case Constantes.DOCUMENT_PERMISSION_COURTE_DUREE:
                case Constantes.DOCUMENT_CONGES: {
                    conges.clear();
                    query = "SELECT _y_.id, _y_.reference_conge, _y_.effet, COALESCE(_y_.statut, 'E'), COALESCE(_y_.nature, 'C'), _y_.date_debut, _y_.heure_debut, _y_.date_fin, _y_.heure_fin, _y_.duree, "
                            + "_y_.duree_permission, _y_.etape_valide, _y_.etape_total, _y_.date_retour, _y_.employe, _e_.matricule, _e_.nom, _e_.prenom "
                            + "FROM yvs_grh_conge_emps _y_ INNER JOIN yvs_grh_employes _e_ ON _y_.employe = _e_.id "
                            + query + "ORDER BY _y_.date_debut DESC, _y_.reference_conge DESC ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsGrhCongeEmps r;
                    String string = null;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsGrhCongeEmps((Long) data[i++]);
                        r.setReferenceConge((String) data[i++]);
                        r.setEffet((String) data[i++]);
                        string = (String) data[i++];
                        r.setStatut(string.trim().length() > 0 ? string.charAt(0) : 'E');
                        string = (String) data[i++];
                        r.setNature(string.trim().length() > 0 ? string.charAt(0) : 'C');
                        r.setDateDebut(new Date(((java.sql.Date) data[i++]).getTime()));
                        r.setHeureDebut(new Date(((java.sql.Time) data[i++]).getTime()));
                        r.setDateFin(new Date(((java.sql.Date) data[i++]).getTime()));
                        r.setHeureFin(new Date(((java.sql.Time) data[i++]).getTime()));
                        r.setDuree((Integer) data[i++]);
                        r.setDureePermission(((String) data[i++]).charAt(0));
                        r.setEtapeValide((Integer) data[i++]);
                        r.setEtapeTotal((Integer) data[i++]);
                        r.setDateRetour(new Date(((java.sql.Date) data[i++]).getTime()));
                        r.setEmploye(new YvsGrhEmployes((Long) data[i++], (String) data[i++], "", (String) data[i++], (String) data[i++]));
                        conges.add(r);
                    }
                    break;
                }
                case Constantes.DOCUMENT_CP_UPPER_PR:
                case Constantes.DOCUMENT_ORDRE_FABRICATION_TERMINE:
                case Constantes.DOCUMENT_ORDRE_FABRICATION_DECLARE: {
                    fabrications.clear();
                    query = "SELECT _y_.id, _y_.code_ref, _y_.statut_ordre, _y_.statut_declaration, _y_.date_debut, _y_.fin_validite, _y_.cout_of, _y_.taux_evolution, _y_.quantite_fabrique, "
                            + "_y_.article, _a_.ref_art, _a_.designation, _y_.nomenclature::bigint, _n_.reference, _y_.gamme::bigint, _g_.code_ref "
                            + "FROM yvs_prod_ordre_fabrication _y_ LEFT JOIN yvs_prod_nomenclature _n_ ON _y_.nomenclature = _n_.id LEFT JOIN yvs_base_articles _a_ ON _y_.article = _a_.id "
                            + "LEFT JOIN yvs_prod_gamme_article _g_ ON _y_.gamme = _g_.id "
                            + query + "ORDER BY _y_.date_debut DESC, _y_.code_ref DESC ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsProdOrdreFabrication r;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsProdOrdreFabrication((Long) data[i++]);
                        r.setCodeRef((String) data[i++]);
                        r.setStatutOrdre((String) data[i++]);
                        r.setStatutDeclaration((String) data[i++]);
                        r.setDateDebut(new Date(((java.sql.Date) data[i++]).getTime()));
                        r.setFinValidite(new Date(((java.sql.Date) data[i++]).getTime()));
                        r.setCoutOf((Double) data[i++]);
                        r.setTauxEvolution((Double) data[i++]);
                        r.setQuantite((Double) data[i++]);
                        r.setArticle(new YvsBaseArticles((Long) data[i++], (String) data[i++], (String) data[i++]));
                        r.setNomenclature(new YvsProdNomenclature((Long) data[i++], (String) data[i++], r.getArticle()));
                        r.setGamme(new YvsProdGammeArticle((Long) data[i++], (String) data[i++]));
                        fabrications.add(r);
                    }
                    break;
                }
                case Constantes.DOCUMENT_RECONDITIONNEMENT_STOCK:
                case Constantes.DOCUMENT_INVENTAIRE_STOCK:
                case Constantes.DOCUMENT_TRANSFERT:
                case Constantes.DOCUMENT_SORTIE:
                case Constantes.DOCUMENT_ENTREE: {
                    stocks.clear();
                    query = "SELECT _y_.id, _y_.num_doc, _y_.cloturer, _y_.statut, _y_.date_doc, _y_.num_piece, _y_.type_doc, _y_.etape_valide, _y_.etape_total, "
                            + "_y_.source, _s_.designation, _y_.creneau_source, _cs_.tranche, _ts_.type_journee, _ts_.heure_debut, _ts_.heure_fin, "
                            + "_y_.destination, _d_.designation, _y_.creneau_destinataire, _cd_.tranche, _td_.type_journee, _td_.heure_debut, _td_.heure_fin "
                            + "FROM yvs_com_doc_stocks _y_ LEFT JOIN yvs_base_depots _s_ ON _y_.source = _s_.id LEFT JOIN yvs_base_depots _d_ ON _y_.destination = _d_.id "
                            + "LEFT JOIN yvs_com_creneau_depot _cs_ ON _y_.creneau_source = _cs_.id LEFT JOIN yvs_com_creneau_depot _cd_ ON _y_.creneau_destinataire = _cd_.id "
                            + "LEFT JOIN yvs_grh_tranche_horaire _ts_ ON _cs_.tranche = _ts_.id LEFT JOIN yvs_grh_tranche_horaire _td_ ON _cd_.tranche = _td_.id "
                            + query + "ORDER BY _y_.date_doc DESC, _y_.num_doc DESC ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsComDocStocks r;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsComDocStocks((Long) data[i++]);
                        r.setNumDoc((String) data[i++]);
                        r.setCloturer((Boolean) data[i++]);
                        r.setStatut((String) data[i++]);
                        r.setDateDoc(new Date(((java.sql.Date) data[i++]).getTime()));
                        r.setNumPiece((String) data[i++]);
                        r.setTypeDoc((String) data[i++]);
                        r.setEtapeValide((Integer) data[i++]);
                        r.setEtapeTotal((Integer) data[i++]);
                        r.setSource(new YvsBaseDepots((Long) data[i++], (String) data[i++]));
                        r.setCreneauSource(new YvsComCreneauDepot((Long) data[i++], new YvsGrhTrancheHoraire((Long) data[i++], (String) data[i++], (java.sql.Time) data[i++], (java.sql.Time) data[i++]), r.getSource()));
                        r.setDestination(new YvsBaseDepots((Long) data[i++], (String) data[i++]));
                        r.setCreneauDestinataire(new YvsComCreneauDepot((Long) data[i++], new YvsGrhTrancheHoraire((Long) data[i++], (String) data[i++], (java.sql.Time) data[i++], (java.sql.Time) data[i++]), r.getDestination()));
                        stocks.add(r);
                    }
                    break;
                }
                case Constantes.DOCUMENT_DOC_DIVERS_RECETTE:
                case Constantes.DOCUMENT_DOC_DIVERS_DEPENSE:
                case Constantes.DOCUMENT_DOC_DIVERS_CAISSE:
                case Constantes.DOCUMENT_OD_NON_PLANNIFIE:
                case Constantes.DOCUMENT_OD_NON_COMPTABILISE: {
                    divers.clear();
                    query = "SELECT _y_.id, _y_.num_piece, _y_.description, _y_.statut_doc, COALESCE(_y_.statut_regle, 'W'), _y_.id_tiers, _y_.date_doc, _y_.table_tiers, _y_.etape_valide, _y_.etape_total, _y_.montant, "
                            + "_y_.type_doc, _t_.libelle, _y_.mouvement, _y_.agence, _a_.designation, _y_.caisse::bigint, _m_.intitule "
                            + "FROM yvs_compta_caisse_doc_divers _y_ LEFT JOIN yvs_base_plan_comptable _c_ ON _y_.compte_general = _c_.id LEFT JOIN yvs_base_type_doc_divers _t_ ON _y_.type_doc = _t_.id "
                            + "INNER JOIN yvs_agences _a_ ON _y_.agence = _a_.id LEFT JOIN yvs_base_caisse _m_ ON _y_.caisse = _m_.id "
                            + query + "ORDER BY _y_.date_doc DESC, _y_.num_piece DESC ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsComptaCaisseDocDivers r;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsComptaCaisseDocDivers((Long) data[i++]);
                        r.setNumPiece((String) data[i++]);
                        r.setDescription((String) data[i++]);
                        r.setStatutDoc((String) data[i++]);
                        r.setStatutRegle(((String) data[i++]).charAt(0));
                        r.setIdTiers((Long) data[i++]);
                        r.setDateDoc(new Date(((java.sql.Timestamp) data[i++]).getTime()));
                        r.setTableTiers((String) data[i++]);
                        r.setEtapeValide((Integer) data[i++]);
                        r.setEtapeTotal((Integer) data[i++]);
                        r.setMontant((Double) data[i++]);
                        r.setTypeDoc(new YvsBaseTypeDocDivers((Long) data[i++], (String) data[i++]));
                        r.setMouvement((String) data[i++]);
                        r.setAgence(new YvsAgences((Long) data[i++], (String) data[i++]));
                        r.setCaisse(new YvsBaseCaisse((Long) data[i++], (String) data[i++]));
                        divers.add(r);
                    }
                    break;
                }
                case Constantes.DOCUMENT_BON_LIVRAISON_ACHAT:
                case Constantes.DOCUMENT_AVOIR_ACHAT:
                case Constantes.DOCUMENT_RETOUR_ACHAT:
                case Constantes.DOCUMENT_FACTURE_ACHAT_REGLE:
                case Constantes.DOCUMENT_FACTURE_ACHAT_LIVRE:
                case Constantes.DOCUMENT_FACTURE_ACHAT:
                case Constantes.DOCUMENT_ACHAT_NON_COMPTABILISE: {
                    achats.clear();
                    query = "SELECT _y_.id, _y_.num_doc, _y_.cloturer, _y_.statut, _y_.statut_regle, _y_.statut_livre, _y_.date_doc, _y_.num_piece, _y_.type_doc, _y_.etape_valide, _y_.etape_total, _y_.comptabilise, "
                            + "_y_.fournisseur, _f_.code_fsseur, _f_.tiers, _t_.nom, _t_.prenom, _y_.agence, _a_.designation, _y_.model_reglement::bigint, _m_.reference "
                            + "FROM yvs_com_doc_achats _y_ INNER JOIN yvs_base_fournisseur _f_ ON _y_.fournisseur = _f_.id INNER JOIN yvs_base_tiers _t_ ON _f_.tiers = _t_.id "
                            + "INNER JOIN yvs_agences _a_ ON _y_.agence = _a_.id LEFT JOIN yvs_base_model_reglement _m_ ON _y_.model_reglement = _m_.id "
                            + query + "ORDER BY _y_.date_doc DESC, _y_.num_doc DESC ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsComDocAchats r;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsComDocAchats((Long) data[i++]);
                        r.setNumDoc((String) data[i++]);
                        r.setCloturer((Boolean) data[i++]);
                        r.setStatut((String) data[i++]);
                        r.setStatutRegle((String) data[i++]);
                        r.setStatutLivre((String) data[i++]);
                        r.setDateDoc(new Date(((java.sql.Date) data[i++]).getTime()));
                        r.setNumPiece((String) data[i++]);
                        r.setTypeDoc((String) data[i++]);
                        r.setEtapeValide((Integer) data[i++]);
                        r.setEtapeTotal((Integer) data[i++]);
                        r.setComptabilise((Boolean) data[i++]);
                        r.setFournisseur(new YvsBaseFournisseur((Long) data[i++], (String) data[i++], new YvsBaseTiers((Long) data[i++], (String) data[i++], (String) data[i++])));
                        r.setAgence(new YvsAgences((Long) data[i++], (String) data[i++]));
                        r.setModelReglement(new YvsBaseModelReglement((Long) data[i++], (String) data[i++]));
                        achats.add(r);
                    }
                    break;
                }
                case Constantes.DOCUMENT_AVOIR_VENTE:
                case Constantes.DOCUMENT_RETOUR_VENTE:
                case Constantes.DOCUMENT_FACTURE_VENTE_REGLE:
                case Constantes.DOCUMENT_FACTURE_VENTE_LIVRE:
                case Constantes.DOCUMENT_FACTURE_VENTE:
                case Constantes.DOCUMENT_VENTE_NON_COMPTABILISE:
                case Constantes.DOCUMENT_BON_LIVRAISON_VENTE: {
                    ventes.clear();
                    query = "SELECT _y_.id, _y_.num_doc, _y_.cloturer, _y_.statut, _y_.statut_regle, _y_.statut_livre, _y_.livraison_auto, _y_.heure_doc, _y_.type_doc, "
                            + " _y_.entete_doc, _e_.date_entete, _y_.numero_externe, _y_.num_piece, _y_.etape_valide, _y_.etape_total, _y_.comptabilise, "
                            + " _y_.nom_client, _y_.client, _c_.code_client, _c_.nom, _c_.prenom, _c_.tiers, _t_.nom, _t_.prenom, "
                            + "_e_.creneau, _h_.creneau_point, _cp_.point::bigint, _p_.libelle, _h_.users, _u_.nom_users "
                            + "FROM yvs_com_doc_ventes _y_ INNER JOIN yvs_com_entete_doc_vente _e_ ON _y_.entete_doc = _e_.id "
                            + "INNER JOIN yvs_com_client _c_ ON _y_.client = _c_.id INNER JOIN yvs_base_tiers _t_ ON _c_.tiers = _t_.id "
                            + "INNER JOIN yvs_com_creneau_horaire_users _h_ ON _e_.creneau = _h_.id INNER JOIN yvs_users _u_ ON _h_.users = _u_.id "
                            + "INNER JOIN yvs_com_creneau_point _cp_ ON _h_.creneau_point = _cp_.id INNER JOIN yvs_base_point_vente _p_ ON _cp_.point = _p_.id "
                            + query + "ORDER BY _e_.date_entete DESC, _y_.num_doc DESC ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsComDocVentes r;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsComDocVentes((Long) data[i++]);
                        r.setNumDoc((String) data[i++]);
                        r.setCloturer((Boolean) data[i++]);
                        r.setStatut((String) data[i++]);
                        r.setStatutRegle((String) data[i++]);
                        r.setStatutLivre((String) data[i++]);
                        r.setLivraisonAuto((Boolean) data[i++]);
                        r.setHeureDoc(new Date(((java.sql.Time) data[i++]).getTime()));
                        r.setTypeDoc((String) data[i++]);
                        r.setEnteteDoc(new YvsComEnteteDocVente((Long) data[i++], new Date(((java.sql.Date) data[i++]).getTime())));
                        r.setNumeroExterne((String) data[i++]);
                        r.setNumPiece((String) data[i++]);
                        r.setEtapeValide((Integer) data[i++]);
                        r.setEtapeTotal((Integer) data[i++]);
                        r.setComptabilise((Boolean) data[i++]);
                        r.setNomClient((String) data[i++]);
                        r.setClient(new YvsComClient((Long) data[i++], (String) data[i++], (String) data[i++], (String) data[i++], new YvsBaseTiers((Long) data[i++], (String) data[i++], (String) data[i++])));
                        r.getEnteteDoc().setCreneau(new YvsComCreneauHoraireUsers((Long) data[i++]));
                        r.getEnteteDoc().getCreneau().setCreneauPoint(new YvsComCreneauPoint((Long) data[i++]));
                        r.getEnteteDoc().getCreneau().getCreneauPoint().setPoint(new YvsBasePointVente((Long) data[i++], (String) data[i++]));
                        r.getEnteteDoc().getCreneau().setUsers(new YvsUsers((Long) data[i++], (String) data[i++]));
                        ventes.add(r);
                    }
                    break;
                }
                case Constantes.DOCUMENT_PHASE_CAISSE_SALAIRE:
                case Constantes.DOCUMENT_PHASE_CAISSE_DIVERS:
                case Constantes.DOCUMENT_PHASE_CAISSE_VENTE:
                case Constantes.DOCUMENT_PHASE_CAISSE_ACHAT:
                case Constantes.DOCUMENT_PHASE_ACOMPTE_VENTE:
                case Constantes.DOCUMENT_PHASE_ACOMPTE_ACHAT:
                case Constantes.DOCUMENT_PHASE_CREDIT_VENTE:
                case Constantes.DOCUMENT_PHASE_CREDIT_ACHAT:

                    break;
                case Constantes.DOCUMENT_CLIENT: {
                    clients.clear();
                    query = "SELECT _y_.id, _y_.code_client, _y_.seuil_solde, _y_.defaut, _t_.id, _t_.code_tiers, _t_.nom, _t_.prenom, _y_.nom, _y_.prenom, "
                            + " _p_.id, _p_.num_compte, _s_.id, _s_.libele, _v_.id, _v_.libele, _z_.id, _z_.libele, _y_.actif, _y_.confirmer "
                            + "FROM yvs_com_client _y_ INNER JOIN yvs_base_tiers _t_ ON _y_.tiers = _t_.id "
                            + "LEFT JOIN yvs_base_plan_comptable _p_ ON _y_.compte = _p_.id LEFT JOIN yvs_base_categorie_comptable _c_ ON _y_.categorie_comptable = _c_.id "
                            + "LEFT JOIN yvs_dictionnaire _s_ ON _t_.pays = _s_.id LEFT JOIN yvs_dictionnaire _v_ ON _t_.ville = _v_.id "
                            + "LEFT JOIN yvs_dictionnaire _z_ ON _t_.secteur = _z_.id "
                            + query + "ORDER BY _t_.nom, _t_.prenom ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsComClient r;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsComClient((Long) data[i++]);
                        r.setCodeClient((String) data[i++]);
                        r.setSeuilSolde((Double) data[i++]);
                        r.setDefaut((Boolean) data[i++]);
                        r.setTiers(new YvsBaseTiers((Long) data[i++], (String) data[i++], (String) data[i++], (String) data[i++]));
                        r.setNom((String) data[i++]);
                        r.setPrenom((String) data[i++]);
                        r.setCompte(new YvsBasePlanComptable((Long) data[i++], (String) data[i++]));
                        r.getTiers().setPays(new YvsDictionnaire((Long) data[i++], (String) data[i++]));
                        r.getTiers().setVille(new YvsDictionnaire((Long) data[i++], (String) data[i++]));
                        r.getTiers().setSecteur(new YvsDictionnaire((Long) data[i++], (String) data[i++]));
                        r.setActif((Boolean) data[i++]);
                        r.setConfirmer((Boolean) data[i++]);
                        clients.add(r);
                    }
                    break;
                }
                case Constantes.DOCUMENT_FOURNISSEUR: {
                    fournisseurs.clear();
                    query = "SELECT _y_.id, _y_.code_fsseur, _y_.seuil_solde, _y_.defaut, _t_.id, _t_.code_tiers, _t_.nom, _t_.prenom, _y_.nom, _y_.prenom, "
                            + " _p_.id, _p_.num_compte, _s_.id, _s_.libele, _v_.id, _v_.libele, _z_.id, _z_.libele, _y_.actif, _c_.id, _c_.libelle "
                            + "FROM yvs_base_fournisseur _y_ INNER JOIN yvs_base_tiers _t_ ON _y_.tiers = _t_.id "
                            + "LEFT JOIN yvs_base_plan_comptable _p_ ON _y_.compte = _p_.id LEFT JOIN yvs_base_categorie_fournisseur _c_ ON _y_.categorie = _c_.id "
                            + "LEFT JOIN yvs_dictionnaire _s_ ON _t_.pays = _s_.id LEFT JOIN yvs_dictionnaire _v_ ON _t_.ville = _v_.id "
                            + "LEFT JOIN yvs_dictionnaire _z_ ON _t_.secteur = _z_.id "
                            + query + "ORDER BY _t_.nom, _t_.prenom ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsBaseFournisseur r;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsBaseFournisseur((Long) data[i++]);
                        r.setCodeFsseur((String) data[i++]);
                        r.setSeuilSolde((Double) data[i++]);
                        r.setDefaut((Boolean) data[i++]);
                        r.setTiers(new YvsBaseTiers((Long) data[i++], (String) data[i++], (String) data[i++], (String) data[i++]));
                        r.setNom((String) data[i++]);
                        r.setPrenom((String) data[i++]);
                        r.setCompte(new YvsBasePlanComptable((Long) data[i++], (String) data[i++]));
                        r.getTiers().setPays(new YvsDictionnaire((Long) data[i++], (String) data[i++]));
                        r.getTiers().setVille(new YvsDictionnaire((Long) data[i++], (String) data[i++]));
                        r.getTiers().setSecteur(new YvsDictionnaire((Long) data[i++], (String) data[i++]));
                        r.setActif((Boolean) data[i++]);
                        r.setCategorie(new YvsBaseCategorieFournisseur((Long) data[i++], (String) data[i++]));
                        fournisseurs.add(r);
                    }
                    break;
                }
                case Constantes.DOCUMENT_USERS: {
                    users.clear();
                    query = "SELECT _y_.id, _y_.civilite, _y_.code_users, _y_.nom_users, _c_.id, _c_.libelle, _a_.id, _a_.designation, _y_.connect_online_planning, _y_.acces_multi_agence, "
                            + " _y_.actif, _y_.connecte "
                            + "FROM yvs_users _y_ INNER JOIN yvs_agences _a_ ON _y_.agence = _a_.id LEFT JOIN yvs_com_categorie_personnel _c_ ON _y_.categorie = _c_.id "
                            + query + "ORDER BY _y_.nom_users ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsUsers r;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsUsers((Long) data[i++]);
                        r.setCivilite((String) data[i++]);
                        r.setCodeUsers((String) data[i++]);
                        r.setNomUsers((String) data[i++]);
                        r.setCategorie(new YvsComCategoriePersonnel((Long) data[i++], (String) data[i++]));
                        r.setAgence(new YvsAgences((Long) data[i++], (String) data[i++]));
                        r.setConnectOnlinePlanning((Boolean) data[i++]);
                        r.setAccesMultiAgence((Boolean) data[i++]);
                        r.setActif((Boolean) data[i++]);
                        r.setConnected((Boolean) data[i++]);
                        users.add(r);
                    }
                    break;
                }
                case Constantes.DOCUMENT_EMPLOYE: {
                    employes.clear();
                    query = "SELECT _y_.id, _y_.civilite, _y_.nom, _y_.prenom, _y_.matricule, _p_.id, _p_.intitule, _y_.telephone1, _y_.date_embauche, "
                            + " _y_.adresse1, _y_.adresse2, _a_.id, _a_.designation, _y_.matricule_cnps, _y_.actif, _o_.id, _o_.statut_profil "
                            + "FROM yvs_grh_employes _y_ INNER JOIN yvs_agences _a_ ON _y_.agence = _a_.id "
                            + "LEFT JOIN yvs_grh_poste_de_travail _p_ ON _y_.poste_actif = _p_.id LEFT JOIN yvs_grh_profil _o_ ON _y_.profil = _o_.id"
                            + query + "ORDER BY _y_.nom ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsGrhEmployes r;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsGrhEmployes((Long) data[i++]);
                        r.setCivilite((String) data[i++]);
                        r.setNom((String) data[i++]);
                        r.setPrenom((String) data[i++]);
                        r.setMatricule((String) data[i++]);
                        r.setTelephone1((String) data[i++]);
                        r.setDateEmbauche(new Date(((java.sql.Date) data[i++]).getTime()));
                        r.setAdresse1((String) data[i++]);
                        r.setAdresse2((String) data[i++]);
                        r.setAgence(new YvsAgences((Long) data[i++], (String) data[i++]));
                        r.setMatriculeCnps((String) data[i++]);
                        r.setActif((Boolean) data[i++]);
                        r.setProfil(new YvsGrhProfil((Long) data[i++], (String) data[i++]));
                        employes.add(r);
                    }
                    break;
                }
                case Constantes.DOCUMENT_CAISSE: {
                    caisses.clear();
                    query = "SELECT _y_.id, _y_.code, _y_.intitule, _y_.type_caisse, _y_.principal, _y_.actif, _y_.default_caisse, "
                            + "_u_.id, _u_.nom_users, _j_.id, _j_.intitule, _p_.id, _p_.num_compte, _g_.id, _g_.intitule, _a_.id, _a_.designation "
                            + "FROM yvs_base_caisse _y_ INNER JOIN yvs_compta_journaux _j_ ON _y_.journal = _j_.id "
                            + "INNER JOIN yvs_agences _a_ ON _y_.agence = _a_.id LEFT JOIN yvs_users _u_ ON _y_.caissier = _u_.id "
                            + "INNER JOIN yvs_base_plan_comptable _p_ ON _y_.compte = _p_.id LEFT JOIN yvs_base_caisse _g_ ON _y_.parent = _g_.id "
                            + query + "ORDER BY _y_.intitule ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsBaseCaisse r;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsBaseCaisse((Long) data[i++]);
                        r.setCode((String) data[i++]);
                        r.setIntitule((String) data[i++]);
                        r.setTypeCaisse((String) data[i++]);
                        r.setPrincipal((Boolean) data[i++]);
                        r.setActif((Boolean) data[i++]);
                        r.setDefaultCaisse((Boolean) data[i++]);
                        r.setCaissier(new YvsUsers((Long) data[i++], (String) data[i++]));
                        r.setJournal(new YvsComptaJournaux((Long) data[i++], "", (String) data[i++]));
                        r.setCompte(new YvsBasePlanComptable((Long) data[i++], (String) data[i++]));
                        r.setParent(new YvsBaseCaisse((Long) data[i++], (String) data[i++]));
                        r.getJournal().setAgence(new YvsAgences((Long) data[i++], (String) data[i++]));
                        caisses.add(r);
                    }
                    break;
                }
                case Constantes.DOCUMENT_DEPOT: {
                    depots.clear();
                    query = "SELECT _y_.id, _y_.code, _y_.abbreviation, _y_.designation, _y_.actif, _a_.id, _a_.designation, "
                            + "_y_.op_production, _y_.op_achat, _y_.op_vente, _y_.op_transit, _y_.op_retour, _y_.op_technique, _y_.op_reserv "
                            + "FROM yvs_base_depots _y_ INNER JOIN yvs_agences _a_ ON _y_.agence = _a_.id "
                            + query + "ORDER BY _y_.designation ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsBaseDepots r;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsBaseDepots((Long) data[i++]);
                        r.setCode((String) data[i++]);
                        r.setAbbreviation((String) data[i++]);
                        r.setDesignation((String) data[i++]);
                        r.setActif((Boolean) data[i++]);
                        r.setAgence(new YvsAgences((Long) data[i++], (String) data[i++]));
                        r.setOpProduction((Boolean) data[i++]);
                        r.setOpAchat((Boolean) data[i++]);
                        r.setOpVente((Boolean) data[i++]);
                        r.setOpTransit((Boolean) data[i++]);
                        r.setOpRetour((Boolean) data[i++]);
                        r.setOpTechnique((Boolean) data[i++]);
                        r.setOpReserv((Boolean) data[i++]);
                        depots.add(r);
                    }
                    break;
                }
                case Constantes.DOCUMENT_POINTVENTE: {
                    pointsVente.clear();
                    query = "SELECT _y_.id, _y_.code, _y_.libelle, _y_.adresse, _s_.id, _s_.libele, _p_.id, _p_.libelle, _y_.type, _y_.actif, _a_.id, _a_.designation "
                            + "FROM yvs_base_point_vente _y_ INNER JOIN yvs_agences _a_ ON _y_.agence = _a_.id "
                            + "LEFT JOIN yvs_base_point_vente _p_ ON _y_.parent = _p_.id "
                            + query + "ORDER BY _y_.libelle ";
                    List<Object[]> result = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
                    YvsBasePointVente r;
                    for (Object[] data : result) {
                        int i = 0;
                        r = new YvsBasePointVente((Long) data[i++]);
                        r.setCode((String) data[i++]);
                        r.setLibelle((String) data[i++]);
                        r.setAdresse((String) data[i++]);
                        r.setSecteur(new YvsDictionnaire((Long) data[i++], (String) data[i++]));
                        r.setParent(new YvsBasePointVente((Long) data[i++], (String) data[i++]));
                        r.setType(((String) data[i++]).charAt(0));
                        r.setActif((Boolean) data[i++]);
                        r.setAgence(new YvsAgences((Long) data[i++], (String) data[i++]));
                        pointsVente.add(r);
                    }
                    break;
                }
            }
            openDialog("warnings");
        }
    }

    public String getPage() {
        switch (warning.getTitre()) {
            case Constantes.DOCUMENT_MISSION:
                return "../../pages/param_general/warning/missions.xhtml";
            case Constantes.DOCUMENT_FORMATION:
                return "";
            case Constantes.DOCUMENT_BON_DIVERS_CAISSE:
                return "";
            case Constantes.DOCUMENT_APPROVISIONNEMENT:
                return "../../pages/param_general/warning/approvisionnements.xhtml";
            case Constantes.DOCUMENT_PIECE_CAISSE:
                return "../../pages/param_general/warning/pieces_caisse.xhtml";
            case Constantes.DOCUMENT_HIGH_PR_ARTICLE:
                return "";
            case Constantes.DOCUMENT_STOCK_ARTICLE:
                return "../../pages/param_general/warning/article_depots.xhtml";
            case Constantes.DOCUMENT_ARTICLE:
            case Constantes.DOCUMENT_ARTICLE_NON_MOUVEMENTE:
                return "../../pages/param_general/warning/articles.xhtml";
            case Constantes.DOCUMENT_PERMISSION_COURTE_DUREE:
            case Constantes.DOCUMENT_CONGES:
                return "../../pages/param_general/warning/conges.xhtml";
            case Constantes.DOCUMENT_CP_UPPER_PR:
            case Constantes.DOCUMENT_ORDRE_FABRICATION_TERMINE:
            case Constantes.DOCUMENT_ORDRE_FABRICATION_DECLARE:
                return "../../pages/param_general/warning/productions.xhtml";
            case Constantes.DOCUMENT_RECONDITIONNEMENT_STOCK:
            case Constantes.DOCUMENT_INVENTAIRE_STOCK:
            case Constantes.DOCUMENT_TRANSFERT:
            case Constantes.DOCUMENT_SORTIE:
            case Constantes.DOCUMENT_ENTREE:
                return "../../pages/param_general/warning/stocks.xhtml";
            case Constantes.DOCUMENT_DOC_DIVERS_RECETTE:
            case Constantes.DOCUMENT_DOC_DIVERS_DEPENSE:
            case Constantes.DOCUMENT_DOC_DIVERS_CAISSE:
            case Constantes.DOCUMENT_OD_NON_PLANNIFIE:
            case Constantes.DOCUMENT_OD_NON_COMPTABILISE:
                return "../../pages/param_general/warning/divers.xhtml";
            case Constantes.DOCUMENT_BON_LIVRAISON_ACHAT:
            case Constantes.DOCUMENT_AVOIR_ACHAT:
            case Constantes.DOCUMENT_RETOUR_ACHAT:
            case Constantes.DOCUMENT_FACTURE_ACHAT_REGLE:
            case Constantes.DOCUMENT_FACTURE_ACHAT_LIVRE:
            case Constantes.DOCUMENT_FACTURE_ACHAT:
            case Constantes.DOCUMENT_ACHAT_NON_COMPTABILISE:
                return "../../pages/param_general/warning/achats.xhtml";
            case Constantes.DOCUMENT_AVOIR_VENTE:
            case Constantes.DOCUMENT_RETOUR_VENTE:
            case Constantes.DOCUMENT_FACTURE_VENTE_REGLE:
            case Constantes.DOCUMENT_FACTURE_VENTE_LIVRE:
            case Constantes.DOCUMENT_FACTURE_VENTE:
            case Constantes.DOCUMENT_VENTE_NON_COMPTABILISE:
            case Constantes.DOCUMENT_BON_LIVRAISON_VENTE:
                return "../../pages/param_general/warning/ventes.xhtml";
            case Constantes.DOCUMENT_LOWER_MARGIN:
                return "../../pages/param_general/warning/contenus.xhtml";
            case Constantes.DOCUMENT_PHASE_CAISSE_SALAIRE:
            case Constantes.DOCUMENT_PHASE_CAISSE_DIVERS:
            case Constantes.DOCUMENT_PHASE_CAISSE_VENTE:
            case Constantes.DOCUMENT_PHASE_CAISSE_ACHAT:
            case Constantes.DOCUMENT_PHASE_ACOMPTE_VENTE:
            case Constantes.DOCUMENT_PHASE_ACOMPTE_ACHAT:
            case Constantes.DOCUMENT_PHASE_CREDIT_VENTE:
            case Constantes.DOCUMENT_PHASE_CREDIT_ACHAT:
                return "";
            case Constantes.DOCUMENT_CLIENT:
                return "../../pages/param_general/warning/clients.xhtml";
            case Constantes.DOCUMENT_FOURNISSEUR:
                return "../../pages/param_general/warning/fournisseurs.xhtml";
            case Constantes.DOCUMENT_USERS:
                return "../../pages/param_general/warning/users.xhtml";
            case Constantes.DOCUMENT_EMPLOYE:
                return "../../pages/param_general/warning/employes.xhtml";
            case Constantes.DOCUMENT_CAISSE:
                return "../../pages/param_general/warning/caisses.xhtml";
            case Constantes.DOCUMENT_DEPOT:
                return "../../pages/param_general/warning/depots.xhtml";
            case Constantes.DOCUMENT_POINTVENTE:
                return "../../pages/param_general/warning/pointsvente.xhtml";
        }
        return "";
    }

}
