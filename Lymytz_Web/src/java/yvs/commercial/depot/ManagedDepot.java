/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.depot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.production.UtilProd;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.UtilCom;
import yvs.commercial.creneau.Creneau;
import yvs.commercial.creneau.ManagedCreneauEmploye;
import yvs.commercial.creneau.TypeCreneau;
import yvs.commercial.stock.ManagedInventaire;
import yvs.commercial.stock.ManagedStockArticle;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseArticleEmplacement;
import yvs.entity.base.YvsBaseDepotOperation;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseDepotsUser;
import yvs.entity.base.YvsBaseEmplacementDepot;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.base.YvsBasePointVenteDepot;
import yvs.entity.commercial.YvsComLiaisonDepot;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.entity.users.YvsUsers;
import yvs.grh.JoursOuvres;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.bean.Employe;
import yvs.grh.presence.TrancheHoraire;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.agence.ManagedAgence;
import yvs.parametrage.entrepot.Depots;
import yvs.parametrage.entrepot.LiaisonDepot;
import yvs.util.Constantes;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedDepot extends ManagedCommercial<Depots, YvsBaseDepots> implements Serializable {

//    @ManagedProperty(value = "#{depot}")
    private Depots depot = new Depots();
    private List<YvsBaseDepots> depots, depotsSelect, depots_agence, depots_all;
    private YvsBaseDepots selectDepot;

    private long agenceDepot, agencePoint, agenceFind;
    private LiaisonDepot liaison = new LiaisonDepot();
    private List<YvsComLiaisonDepot> liaisons_depot;
    private YvsComLiaisonDepot selectLiaison;

    private Emplacement emplacement = new Emplacement();
    private List<YvsBaseArticleDepot> articlesEmplacement;
    private List<YvsBaseEmplacementDepot> emplacements, emplacementsArticle, emplacementsParents;
    private YvsBaseEmplacementDepot selectEmplacement;

    private PointVente point = new PointVente();
    private List<YvsBasePointVente> pointsvente;
    private PointVenteDepot pointDepot = new PointVenteDepot();
    private List<YvsBasePointVenteDepot> points_depot;
    private YvsBasePointVenteDepot selectPoint;

    private List<String> criteres;
    private TypeCreneau type = new TypeCreneau();
    private List<YvsGrhTrancheHoraire> types;
    private List<YvsJoursOuvres> jours;
    private Creneau creneau = new Creneau();
    private List<YvsComCreneauDepot> creneaux;
    private YvsComCreneauDepot selectCreneau;

    private DepotOperation ope = new DepotOperation();
    private List<YvsBaseDepotOperation> operations;
    private YvsBaseDepotOperation selectOperation;
    private List<String> typesOpe;

    private Long agenceSearch;
    private String codeSearch, typeSearch;
    private Boolean actifSearch;

    private List<YvsBaseArticleEmplacement> articles, selectionsArticles;
    private List<YvsUsers> users;

    private String nom_depot_select;
    private String tabIds, tabIds_article, tabIds_liaison, tabIds_emplacement, tabIds_point, tabIds_creneau, tabIds_lot, tabIds_operation;
    private boolean updateDepot, updateLiaison, updateCrenau, updateLot, memoriseDeletepoint = false;

    public ManagedDepot() {
        articles = new ArrayList<>();
        selectionsArticles = new ArrayList<>();
        criteres = new ArrayList<>();
        articlesEmplacement = new ArrayList<>();
        depots_all = new ArrayList<>();
        emplacementsArticle = new ArrayList<>();
        types = new ArrayList<>();
        depotsSelect = new ArrayList<>();
        jours = new ArrayList<>();
        emplacementsParents = new ArrayList<>();
        depots = new ArrayList<>();
        depots_agence = new ArrayList<>();
        emplacements = new ArrayList<>();
        liaisons_depot = new ArrayList<>();
        points_depot = new ArrayList<>();
        creneaux = new ArrayList<>();
        operations = new ArrayList<>();
        typesOpe = new ArrayList<>();
        users = new ArrayList<>();
        pointsvente = new ArrayList<>();
    }

    public long getAgenceFind() {
        return agenceFind;
    }

    public void setAgenceFind(long agenceFind) {
        this.agenceFind = agenceFind;
    }

    public List<YvsBaseArticleEmplacement> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticleEmplacement> articles) {
        this.articles = articles;
    }

    public List<YvsBaseArticleEmplacement> getSelectionsArticles() {
        return selectionsArticles;
    }

    public void setSelectionsArticles(List<YvsBaseArticleEmplacement> selectionsArticles) {
        this.selectionsArticles = selectionsArticles;
    }

    public List<YvsBaseArticleDepot> getArticlesEmplacement() {
        return articlesEmplacement;
    }

    public void setArticlesEmplacement(List<YvsBaseArticleDepot> articlesEmplacement) {
        this.articlesEmplacement = articlesEmplacement;
    }

    @Override
    public List<YvsUsers> getUsers() {
        return users;
    }

    @Override
    public void setUsers(List<YvsUsers> users) {
        this.users = users;
    }

    public boolean isMemoriseDeletepoint() {
        return memoriseDeletepoint;
    }

    public void setMemoriseDeletepoint(boolean memoriseDeletepoint) {
        this.memoriseDeletepoint = memoriseDeletepoint;
    }

    public long getAgencePoint() {
        return agencePoint;
    }

    public void setAgencePoint(long agencePoint) {
        this.agencePoint = agencePoint;
    }

    public List<YvsBaseDepots> getDepots_all() {
        return depots_all;
    }

    public void setDepots_all(List<YvsBaseDepots> depots_all) {
        this.depots_all = depots_all;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public Long getAgenceSearch() {
        return agenceSearch;
    }

    public void setAgenceSearch(Long agenceSearch) {
        this.agenceSearch = agenceSearch;
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public String getCodeSearch() {
        return codeSearch;
    }

    public void setCodeSearch(String codeSearch) {
        this.codeSearch = codeSearch;
    }

    public List<YvsBaseEmplacementDepot> getEmplacementsArticle() {
        return emplacementsArticle;
    }

    public void setEmplacementsArticle(List<YvsBaseEmplacementDepot> emplacementsArticle) {
        this.emplacementsArticle = emplacementsArticle;
    }

    public String getNom_depot_select() {
        return nom_depot_select;
    }

    public void setNom_depot_select(String nom_depot_select) {
        this.nom_depot_select = nom_depot_select;
    }

    public List<YvsBaseDepots> getDepotsSelect() {
        return depotsSelect;
    }

    public void setDepotsSelect(List<YvsBaseDepots> depotsSelect) {
        this.depotsSelect = depotsSelect;
    }

    public List<String> getTypesOpe() {
        return typesOpe;
    }

    public void setTypesOpe(List<String> typesOpe) {
        this.typesOpe = typesOpe;
    }

    public DepotOperation getOpe() {
        return ope;
    }

    public void setOpe(DepotOperation ope) {
        this.ope = ope;
    }

    public List<YvsBaseDepotOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<YvsBaseDepotOperation> operations) {
        this.operations = operations;
    }

    public YvsBaseDepotOperation getSelectOperation() {
        return selectOperation;
    }

    public void setSelectOperation(YvsBaseDepotOperation selectOperation) {
        this.selectOperation = selectOperation;
    }

    public String getTabIds_operation() {
        return tabIds_operation;
    }

    public void setTabIds_operation(String tabIds_operation) {
        this.tabIds_operation = tabIds_operation;
    }

    public String getTabIds_lot() {
        return tabIds_lot;
    }

    public void setTabIds_lot(String tabIds_lot) {
        this.tabIds_lot = tabIds_lot;
    }

    public boolean isUpdateLot() {
        return updateLot;
    }

    public void setUpdateLot(boolean updateLot) {
        this.updateLot = updateLot;
    }

    public String getTabIds_creneau() {
        return tabIds_creneau;
    }

    public void setTabIds_creneau(String tabIds_creneau) {
        this.tabIds_creneau = tabIds_creneau;
    }

    public boolean isUpdateCrenau() {
        return updateCrenau;
    }

    public void setUpdateCrenau(boolean updateCrenau) {
        this.updateCrenau = updateCrenau;
    }

    public List<String> getCriteres() {
        return criteres;
    }

    public void setCriteres(List<String> criteres) {
        this.criteres = criteres;
    }

    public TypeCreneau getType() {
        return type;
    }

    public void setType(TypeCreneau type) {
        this.type = type;
    }

    public List<YvsJoursOuvres> getJours() {
        return jours;
    }

    public void setJours(List<YvsJoursOuvres> jours) {
        this.jours = jours;
    }

    public List<YvsGrhTrancheHoraire> getTypes() {
        return types;
    }

    public void setTypes(List<YvsGrhTrancheHoraire> types) {
        this.types = types;
    }

    public Creneau getCreneau() {
        return creneau;
    }

    public void setCreneau(Creneau creneau) {
        this.creneau = creneau;
    }

    public List<YvsComCreneauDepot> getCreneaux() {
        return creneaux;
    }

    public void setCreneaux(List<YvsComCreneauDepot> creneaux) {
        this.creneaux = creneaux;
    }

    public YvsComCreneauDepot getSelectCreneau() {
        return selectCreneau;
    }

    public void setSelectCreneau(YvsComCreneauDepot selectCreneau) {
        this.selectCreneau = selectCreneau;
    }

    public List<YvsBasePointVenteDepot> getPoints_depot() {
        return points_depot;
    }

    public void setPoints_depot(List<YvsBasePointVenteDepot> points_depot) {
        this.points_depot = points_depot;
    }

    public YvsBasePointVenteDepot getSelectPoint() {
        return selectPoint;
    }

    public void setSelectPoint(YvsBasePointVenteDepot selectPoint) {
        this.selectPoint = selectPoint;
    }

    public YvsComLiaisonDepot getSelectLiaison() {
        return selectLiaison;
    }

    public void setSelectLiaison(YvsComLiaisonDepot selectLiaison) {
        this.selectLiaison = selectLiaison;
    }

    public YvsBaseEmplacementDepot getSelectEmplacement() {
        return selectEmplacement;
    }

    public void setSelectEmplacement(YvsBaseEmplacementDepot selectEmplacement) {
        this.selectEmplacement = selectEmplacement;
    }

    public List<YvsBaseDepots> getDepots_agence() {
        return depots_agence;
    }

    public void setDepots_agence(List<YvsBaseDepots> depots_agence) {
        this.depots_agence = depots_agence;
    }

    public List<YvsComLiaisonDepot> getLiaisons_depot() {
        return liaisons_depot;
    }

    public void setLiaisons_depot(List<YvsComLiaisonDepot> liaisons_depot) {
        this.liaisons_depot = liaisons_depot;
    }

    public List<YvsBaseEmplacementDepot> getEmplacements() {
        return emplacements;
    }

    public void setEmplacements(List<YvsBaseEmplacementDepot> emplacements) {
        this.emplacements = emplacements;
    }

    public List<YvsBasePointVente> getPointsvente() {
        return pointsvente;
    }

    public void setPointsvente(List<YvsBasePointVente> pointsvente) {
        this.pointsvente = pointsvente;
    }

    public PointVenteDepot getPointDepot() {
        return pointDepot;
    }

    public void setPointDepot(PointVenteDepot pointDepot) {
        this.pointDepot = pointDepot;
    }

    public PointVente getPoint() {
        return point;
    }

    public void setPoint(PointVente point) {
        this.point = point;
    }

    public String getTabIds_point() {
        return tabIds_point;
    }

    public void setTabIds_point(String tabIds_point) {
        this.tabIds_point = tabIds_point;
    }

    public List<YvsBaseEmplacementDepot> getEmplacementsParents() {
        return emplacementsParents;
    }

    public void setEmplacementsParents(List<YvsBaseEmplacementDepot> emplacementsParents) {
        this.emplacementsParents = emplacementsParents;
    }

    public String getTabIds_emplacement() {
        return tabIds_emplacement;
    }

    public void setTabIds_emplacement(String tabIds_emplacement) {
        this.tabIds_emplacement = tabIds_emplacement;
    }

    public Emplacement getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(Emplacement emplacement) {
        this.emplacement = emplacement;
    }

    public long getAgenceDepot() {
        return agenceDepot;
    }

    public void setAgenceDepot(long agenceDepot) {
        this.agenceDepot = agenceDepot;
    }

    public String getTabIds_liaison() {
        return tabIds_liaison;
    }

    public void setTabIds_liaison(String tabIds_liaison) {
        this.tabIds_liaison = tabIds_liaison;
    }

    public boolean isUpdateLiaison() {
        return updateLiaison;
    }

    public void setUpdateLiaison(boolean updateLiaison) {
        this.updateLiaison = updateLiaison;
    }

    public LiaisonDepot getLiaison() {
        return liaison;
    }

    public void setLiaison(LiaisonDepot liaison) {
        this.liaison = liaison;
    }

    public YvsBaseDepots getSelectDepot() {
        return selectDepot;
    }

    public void setSelectDepot(YvsBaseDepots selectDepot) {
        this.selectDepot = selectDepot;
    }

    public Depots getDepot() {
        return depot;
    }

    public void setDepot(Depots depot) {
        this.depot = depot;
    }

    public List<YvsBaseDepots> getDepots() {
        return depots;
    }

    public void setDepots(List<YvsBaseDepots> depots) {
        this.depots = depots;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getTabIds_article() {
        return tabIds_article;
    }

    public void setTabIds_article(String tabIds_article) {
        this.tabIds_article = tabIds_article;
    }

    public boolean isUpdateDepot() {
        return updateDepot;
    }

    public void setUpdateDepot(boolean updateDepot) {
        this.updateDepot = updateDepot;
    }

    @Override
    public void loadAll() {
        loadAllDepot(true, true);
        loadAllPointVente();
        loadAllJours();
        loadAllTypes(null);
        loadCritere();
        chooseTypeOpe();
        loadOthers(selectDepot, false);
    }

    public void load() {
        if (agenceSearch != null ? agenceSearch < 1 : true) {
            agenceSearch = currentAgence.getId();
        }
    }

    public void loadAllDepot(boolean avance, boolean init) {
        addParamDroit();
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        depots = paginator.executeDynamicQuery("YvsBaseDepots", "y.agence.id, y.designation", avance, init, (int) imax, dao);
        //charge les dépôts visible par le users
        depots_all = new ArrayList<>(depots);
        //charge les dépôts accessible par le user
        if ((currentUser != null)) {
            List<YvsBaseDepots> l = dao.loadNameQueries("YvsBaseDepotsUser.findDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
            depots_all.addAll(l);
        }
        update("data_depot");
    }

    public void loadAllDepotActif(Boolean actif) {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", actif, "=", "AND");
        paginator.addParam(p);
        loadAllDepot(true, true);
    }

    public void init(boolean next) {
        //To change body of generated methods, choose Tools | Templates.
        loadAllDepot(next, false);
    }

    public void loadAllDepotActifByOperation(String type, String operation) {
        List<Long> ids = dao.loadNameQueries("YvsBaseDepotOperation.findIdsDepotActifByOperation", new String[]{"type", "operation", "societe"}, new Object[]{type, operation, currentAgence.getSociete()});
        if (ids.isEmpty()) {
            ids.add(0L);
        }
        paginator.addParam(new ParametreRequete("y.id", "ids", ids, "IN", "AND"));
        loadAllDepot(true, true);
    }

    public void loadDepotActifByAgence(long agence) {
        if (agence > 0) {
            depots = dao.loadNameQueries("YvsBaseDepots.findByAgenceActif", new String[]{"agence", "actif"}, new Object[]{new YvsAgences(agence), true});
        } else {
            depots = dao.loadNameQueries("YvsBaseDepots.findAllActif", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
    }

    public void loadAllDepotActif() {
        loadAllDepotActif(true);
    }

    public void loadAllDepotActif(boolean all) {
        loadAllDepots();
    }

    public void loadAllDepot() {
        loadAllDepots();
    }

    public void loadAllDepots() {
        try {
            depots_all.clear();
            depots.clear();
            if (autoriser("view_all_depot_societe")) {
                depots_all = dao.loadNameQueries("YvsBaseDepots.findAllActif", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                depots.addAll(depots_all);
            } else {
                if (autoriser("view_all_depot")) {
                    depots_all = dao.loadNameQueries("YvsBaseDepots.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
                    depots.addAll(depots_all);
                } else if (currentUser != null) {
                    //charge les dépôts où l'utilisateurs est planifier
                    depots = dao.loadNameQueries("YvsComCreneauHoraireUsers.findDepotByUsers", new String[]{"users", "responsable", "date", "hier"}, new Object[]{currentUser.getUsers(), currentUser.getUsers().getEmploye(), new Date(), Constantes.getPreviewDate(new Date())});
                    depots_all = dao.loadNameQueries("YvsBaseDepotsUser.findDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                    for (YvsBaseDepots d : depots) {
                        if (!depots_all.contains(d)) {
                            depots_all.add(d);
                        }
                    }
                    //charge les dépôts où il en est responsable        
                    if (currentUser.getUsers().getEmploye() != null) {
                        List<YvsBaseDepots> list = dao.loadNameQueries("YvsBaseDepots.findByActifResponsable", new String[]{"responsable"}, new Object[]{currentUser.getUsers().getEmploye()});
                        for (YvsBaseDepots d : list) {
                            if (!depots.contains(d)) {
                                depots.add(d);
                            }
                            if (!depots_all.contains(d)) {
                                depots_all.add(d);
                            }
                        }
                    }
                    //charge les dépôts où il en est responsable ou les dépôt ayant le même code d'accès que le user
                    String query = "SELECT d.id FROM yvs_base_depots d INNER JOIN yvs_base_users_acces ua ON d.code_acces=ua.code WHERE ua.users=?";
                    //id dépôt avec code d'accès
                    List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(currentUser.getUsers().getId(), 1)});
                    if (ids != null ? !ids.isEmpty() : false) {
                        List<YvsBaseDepots> list = dao.loadNameQueries("YvsBaseDepots.findByIds", new String[]{"ids"}, new Object[]{ids});
                        for (YvsBaseDepots d : list) {
                            if (!depots.contains(d)) {
                                depots.add(d);
                            }
                            if (!depots_all.contains(d)) {
                                depots_all.add(d);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            getException("loadAllDepots", ex);
        }
    }

    public void onSelectAgenceForAction(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsAgences a = (YvsAgences) ev.getObject();
            agenceFind = a.getId();
            loadDepotsByAgence(agenceFind, true, false);
        }
    }

    public void onSelectAgenceForFind(long agence) {
        loadDepotsByAgence(agence, false, true);
    }

    public void loadDepotsByAgence() {
        if (agenceFind < 1) {
            agenceFind = currentAgence.getId();
        }
        loadDepotsByAgence(agenceFind, true, true);
    }

    public void loadDepotsByAgence(long agence, boolean forAction, boolean forFind) {
        try {
            if (forAction || forFind) {
                if (forFind) {
                    depots_all.clear();
                }
                if (forAction) {
                    depots.clear();
                }
                if (autoriser("view_all_depot")) {
                    List<YvsBaseDepots> list = dao.loadNameQueries("YvsBaseDepots.findByAgenceActif", new String[]{"agence", "actif"}, new Object[]{new YvsAgences(agence), true});
                    if (forFind) {
                        depots_all.addAll(list);
                    }
                    if (forAction) {
                        depots.addAll(list);
                    }
                } else if (currentUser != null) {
                    //charge les dépôts où l'utilisateurs est planifier
                    List<YvsBaseDepots> list = dao.loadNameQueries("YvsComCreneauHoraireUsers.findDepotByUsersAgence", new String[]{"agence", "users", "date", "hier"}, new Object[]{new YvsAgences(agence), currentUser.getUsers(), new Date(), Constantes.getPreviewDate(new Date())});
                    if (forAction) {
                        depots.addAll(list);
                    }
                    if (forFind) {
                        //charge les depots où il a accès
                        depots_all = dao.loadNameQueries("YvsBaseDepotsUser.findDepotByUsersAgence", new String[]{"agence", "users"}, new Object[]{new YvsAgences(agence), currentUser.getUsers()});
                        for (YvsBaseDepots d : list) {
                            if (!depots_all.contains(d)) {
                                depots_all.add(d);
                            }
                        }
                    }
                    //charge les dépôts où il en  est responsable ou les dépôt ayant le même code d'accès que le user
                    String query = "SELECT d.id FROM yvs_base_depots d "
                            + "INNER JOIN yvs_base_users_acces ua ON d.code_acces=ua.code WHERE d.actif IS TRUE AND ua.users=? AND d.agence = ?";
                    //id dépôt avec code d'accès
                    List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(currentUser.getUsers().getId(), 1), new Options(agence, 2)});
                    if (ids != null ? !ids.isEmpty() : false) {
                        list = dao.loadNameQueries("YvsBaseDepots.findByIds", new String[]{"ids"}, new Object[]{ids});
                        for (YvsBaseDepots d : list) {
                            if (forAction ? !depots.contains(d) : false) {
                                depots.add(d);
                            }
                            if (forFind ? !depots_all.contains(d) : false) {
                                depots_all.add(d);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            getException("loadAllDepots", ex);
        }
    }

    public void loadAllJours() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        jours = dao.loadNameQueries("YvsJoursOuvres.findDefaut", champ, val);
    }

    public void loadAllTypes(String critere) {
        if (critere != null ? critere.trim().length() > 0 : false) {
            champ = new String[]{"societe", "critere"};
            val = new Object[]{currentAgence.getSociete(), critere};
            nameQueri = "YvsGrhTrancheHoraire.findByCritere";
        } else {
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
            nameQueri = "YvsGrhTrancheHoraire.findAll";
        }
        types = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadAllPointVente() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        pointsvente = dao.loadNameQueries("YvsBasePointVente.findAll", champ, val);
    }

    public void loadOthers(YvsBaseDepots y, boolean select) {
        loadEmplacement(y);
        loadLiaisonDepot(y);
        loadPointDepot(y);
        loadCreneaux(y);
        loadOperation(y);
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            if (select) {
                m.paginator.clear();
            }
            m.setOnArticle(false);
//            m.loadArticle(y, null, true, true);
        }
    }

    private void loadEmplacement(YvsBaseDepots y) {
        emplacements.clear();
        emplacementsParents.clear();
        emplacementsArticle.clear();
        if (y != null ? y.getId() > 0 : false) {
            champ = new String[]{"depot"};
            val = new Object[]{y};
            emplacements = dao.loadNameQueries("YvsBaseEmplacementDepot.findByDepot", champ, val);
            emplacementsParents.addAll(emplacements);
            emplacementsArticle.addAll(emplacements);
        }
        if (emplacement.getParent() == null) {
            emplacement.setParent(new Emplacement());
        }
        update("data_emplacement_depot");
    }

    private void loadLiaisonDepot(YvsBaseDepots y) {
        liaisons_depot.clear();
        if (y != null ? y.getId() > 0 : false) {
            champ = new String[]{"depot"};
            val = new Object[]{y};
            liaisons_depot = dao.loadNameQueries("YvsComLiaisonDepot.findByDepot", champ, val);
        }
        update("data_liaison_depot");
    }

    private void loadPointDepot(YvsBaseDepots y) {
        points_depot.clear();
        if (y != null ? y.getId() > 0 : false) {
            champ = new String[]{"depot"};
            val = new Object[]{y};
            points_depot = dao.loadNameQueries("YvsBasePointVenteDepot.findByDepot", champ, val);
        }
        update("data_point_vente_depot0");
    }

    public void loadCreneaux(YvsBaseDepots y) {
        creneaux.clear();
        if (y != null ? y.getId() > 0 : false) {
            champ = new String[]{"depot"};
            val = new Object[]{y};
            creneaux = dao.loadNameQueries("YvsComCreneauDepot.findByDepot", champ, val);
        }
        update("data_creneau");
    }

    public void loadCreneauxByDate(YvsBaseDepots y, Date date) {
        creneaux = loadCreneaux(y, date);
    }

    public void loadUsers(YvsComCreneauDepot y, Date date) {
        users.clear();
        if (y != null ? y.getId() > 0 : false) {
            users = dao.loadNameQueries("YvsComCreneauHoraireUsers.findUsersByCreneauDepot", new String[]{"creneau", "date"}, new Object[]{y, date});
        }
    }

    private void loadOperation(YvsBaseDepots y) {
        operations.clear();
        if (y != null ? y.getId() > 0 : false) {
            champ = new String[]{"depot"};
            val = new Object[]{y};
            operations = dao.loadNameQueries("YvsBaseDepotOperation.findByDepot", champ, val);
        }
        update("data_operation");
    }

    public void loadCritere() {
        criteres.clear();
        for (YvsGrhTrancheHoraire bean : types) {
            boolean deja_ = false;
            if ((bean.getTypeJournee() != null) ? !bean.getTypeJournee().trim().equals("") : false) {
                for (String s : criteres) {
                    if (s.toLowerCase().equals(bean.getTypeJournee().toLowerCase())) {
                        deja_ = true;
                        break;
                    }
                }
                if (!deja_) {
                    criteres.add(bean.getTypeJournee());
                }
            }
        }
    }

    @Override
    public Depots recopieView() {
        depot.setAgence((depot.getAgence() != null ? (depot.getAgence().getId() > 0 ? depot.getAgence() : new Agence(currentAgence.getId())) : new Agence(currentAgence.getId())));
        depot.setSupp(false);
        depot.setVerifyAppro(depot.isVerifyAppro());
        return depot;
    }

    public TrancheHoraire recopieViewType() {
        TrancheHoraire t = new TrancheHoraire();
        t.setActif(true);
        t.setHeureDebut((type.getHeureDebut() != null) ? type.getHeureDebut() : new Date());
        t.setHeureFin((type.getHeureFin() != null) ? type.getHeureFin() : new Date());
        t.setId(type.getId());
        t.setTitre(type.getCritere() + "_" + Util.getTimeToString(type.getHeureDebut()) + "-" + Util.getTimeToString(type.getHeureFin()));
        t.setTypeJournee(type.getCritere());
        t.setNew_(true);
        return t;
    }

    public PointVente recopieViewPoint() {
        if (point.getAgence() != null ? point.getAgence().getId() < 1 : true) {
            point.setAgence(new Agence(currentAgence.getId(), currentAgence.getCodeagence(), currentAgence.getDesignation()));
        }
        point.setNew_(true);
        return point;
    }

    public PointVenteDepot recopieViewPointDepot() {
        pointDepot.setDepot(depot);
        pointDepot.setPrincipal(pointDepot.isUpdate() ? pointDepot.isPrincipal() : false);
        pointDepot.setNew_(true);
        return pointDepot;
    }

    public Emplacement recopieViewEmplacement() {
        return emplacement;
    }

    public Creneau recopieViewCreneau() {
        creneau.setNew_(true);
        return creneau;
    }

    public LiaisonDepot recopieViewLiaisonDepot() {
        liaison.setNew_(true);
        return liaison;
    }

    public DepotOperation recopieViewOperation() {
        return ope;
    }

    public Emplacement defautEmplacement() {
        Emplacement e = new Emplacement();
        e.setId(0);
        e.setActif(true);
        e.setCode("Defaut");
        e.setDefaut(true);
        e.setDescription("Fourre Tout");
        e.setDesignation("Defaut");
        e.setParent(new Emplacement());
        e.setUpdate(true);
        return e;
    }

    @Override
    public boolean controleFiche(Depots bean) {
        if (bean.getCode() == null || bean.getCode().equals("")) {
            getErrorMessage("Vous devez entrer le code");
            return false;
        }
        if (bean.getDesignation() == null || bean.getDesignation().equals("")) {
            getErrorMessage("Vous devez entrer la designation");
            return false;
        }
        if (bean.getAbbreviation() == null || bean.getAbbreviation().equals("")) {
            getErrorMessage("Vous devez entrer l'abbreviation");
            return false;
        }
        YvsBaseDepots d = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findByCurrentCode", new String[]{"code", "societe"}, new Object[]{bean.getCode(), currentAgence.getSociete()});
        if (d != null ? (d.getId() > 0 && !d.getId().equals(bean.getId())) : false) {
            getErrorMessage("Vous avez deja associé ce code a un autre dépot");
            return false;
        }
        d = (YvsBaseDepots) dao.loadObjectByNameQueries("YvsBaseDepots.findByAbbreviation", new String[]{"abbreviation", "societe"}, new Object[]{depot.getAbbreviation(), currentAgence.getSociete()});
        if (d != null ? (d.getId() > 0 && !d.getId().equals(bean.getId())) : false) {
            getErrorMessage("Vous avez deja associé cette abbreviation a un autre dépot");
            return false;
        }
        return true;
    }

    public boolean controleFicheEmplacement(Emplacement bean) {
//        if (!isUpdateDepot()) {
//            getErrorMessage("Vous devez enregistrer le depot");
//            return false;
//        }
        if (bean.getCode() == null || bean.getCode().equals("")) {
            getErrorMessage("Vous devez entrer le code");
            return false;
        }
        if (bean.getDesignation() == null || bean.getDesignation().equals("")) {
            getErrorMessage("Vous devez entrer la designation");
            return false;
        }
        if (!bean.isUpdate()) {
            YvsBaseEmplacementDepot t = (YvsBaseEmplacementDepot) dao.loadOneByNameQueries("YvsBaseEmplacementDepot.findByCodeDepot", new String[]{"code", "depot"}, new Object[]{bean.getCode(), selectDepot});
            if (t != null ? t.getId() > 0 : false) {
                getErrorMessage("Vous avez déja crée cet emplacement");
                return false;
            }
        }
        return true;
    }

    public boolean controleFicheLiasonDepot(LiaisonDepot bean) {
        if (selectDepot != null ? selectDepot.getId() < 1 : true) {
            getErrorMessage("Vous devez enregistrer le depot");
            return false;
        }
        if ((bean.getDepot() != null) ? bean.getDepot().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le depot lié");
            return false;
        }
        if (!isUpdateLiaison()) {
            for (YvsComLiaisonDepot a : liaisons_depot) {
                if (a.getDepotLier().getDesignation().equals(bean.getDepot().getDesignation())) {
                    getErrorMessage("Vous avez deja associé ce dépot!");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean controleFichePoint(PointVente bean) {
        if (bean.getCode() == null || bean.getCode().trim().equals("")) {
            getErrorMessage("Vous devez entrer un code!");
            return false;
        }
        if (bean.getLibelle() == null || bean.getLibelle().trim().equals("")) {
            getErrorMessage("Vous devez entrer un libelle!");
            return false;
        }
        if (!bean.isUpdate()) {
            YvsBasePointVente t = (YvsBasePointVente) dao.loadOneByNameQueries("YvsBasePointVente.findByCode", new String[]{"code", "societe"}, new Object[]{bean.getCode(), currentAgence.getSociete()});
            if (t != null ? t.getId() > 0 : false) {
                getErrorMessage("Vous avez déja crée ce point de vente");
                return false;
            }
        }
        return true;
    }

    public boolean controleFichePointDepot(PointVenteDepot bean) {
        if (!isUpdateDepot()) {
            getErrorMessage("Vous devez dabord enregistrer un dépot");
            return false;
        }
        if ((bean.getPointVente() != null) ? bean.getPointVente().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner un point de vente");
            return false;
        }
        if (!bean.isUpdate()) {
            for (YvsBasePointVenteDepot p : points_depot) {
                if (p.getPointVente().getLibelle().equals(bean.getPointVente().getLibelle())) {
                    getErrorMessage("Vous avez deja rattaché ce point de vente");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean controleFicheType(TrancheHoraire bean) {
        if (bean.getTitre() == null || bean.getTitre().trim().equals("")) {
            getErrorMessage("Vous devez entrer la reference");
            return false;
        }
        if (bean.getHeureDebut() == null) {
            getErrorMessage("Vous devez entrer l'heure de debut");
            return false;
        }
        if (bean.getHeureFin() == null) {
            getErrorMessage("Vous devez entrer l'heure de fin");
            return false;
        }
        return true;
    }

    public boolean controleFicheCreneau(Creneau bean) {
        if ((selectDepot != null) ? selectDepot.getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le dépot");
            return false;
        }
        if (!bean.isPermanent()) {
            if ((bean.getJour() != null) ? bean.getJour().getId() < 1 : true) {
                getErrorMessage("Vous devez specifier le jour");
                return false;
            }
        }
        if ((bean.getTranche() != null) ? bean.getTranche().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le type");
            return false;
        }
        if ((bean.getCritere_() != null) ? ((!bean.getCritere_().trim().equals("")) ? !bean.getCritere_().equals(bean.getCritere()) : false) : false) {
            getErrorMessage("Vous devez pas entrer un type dont le critére est different!");
            return false;
        }
        return true;
    }

    public boolean controleFicheOpe(DepotOperation bean) {
        if ((selectDepot != null) ? selectDepot.getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner le depot");
            return false;
        }
        if (bean.getType() == null || bean.getType().trim().equals("")) {
            getErrorMessage("Vous devez preciser le type!");
            return false;
        }
        if (bean.getOperation() == null || bean.getOperation().trim().equals("")) {
            getErrorMessage("Vous devez preciser l'opération!");
            return false;
        }
        for (YvsBaseDepotOperation d : operations) {
            if (d.getOperation().equals(bean.getOperation()) && d.getType().equals(bean.getType())) {
                getErrorMessage("Vous avez deja ajouter cette opération");
                return false;
            }
        }
        return true;
    }

    @Override
    public void populateView(Depots bean) {
        cloneObject(depot, bean);
        setUpdateDepot(true);
    }

    public void populateViewEmplacement(Emplacement bean) {
        cloneObject(emplacement, bean);
    }

    public void populateViewLiaisonDepot(LiaisonDepot bean) {
        cloneObject(liaison, bean);
        setUpdateLiaison(true);
    }

    public void populateViewPoint(PointVenteDepot bean) {
        cloneObject(pointDepot, bean);
    }

    public void populateViewCreneau(Creneau bean) {
        cloneObject(creneau, bean);
        setUpdateCrenau(true);
    }

    public void populateViewOpe(DepotOperation bean) {
        cloneObject(ope, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(depot);
        depot.setResponsable(new Employe());
        depot.setAgence(new Agence());
        depot.setArticles(new ArrayList<YvsBaseArticleDepot>());
        depot.setEmplacements(new ArrayList<Emplacement>());
        depot.setLiaisons(new ArrayList<LiaisonDepot>());
        depot.setPoints(new ArrayList<PointVenteDepot>());
        depot.setOpAchat(false);
        depot.setOpVente(false);
        depot.setOpReserv(false);
        depot.setOpRetour(false);
        depot.setOpProduction(false);
        depot.setOpTechnique(false);
        depot.setOpTransit(false);
        depot.setRequiereLot(false);

        tabIds = "";
        selectDepot = null;
        setUpdateDepot(false);

        emplacements.clear();
        emplacementsParents.clear();
        emplacementsArticle.clear();
        liaisons_depot.clear();
        depots_agence.clear();
        points_depot.clear();
        creneaux.clear();
        operations.clear();

        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            m.getArticles_depot().clear();
            m.getArticlesSelect().clear();
            m.resetFicheArticle();
            m.setEntityDepot(null);
        }
        resetFicheOpe();
        resetFicheLiaisonDepot();
        resetFicheEmplacement();
        resetFichePoint();
        resetFicheCreneau();

        chooseTypeOpe();
        update("blog_form_depot");
    }

    public void resetFicheEmplacement() {
        resetFiche(emplacement);
        emplacement.setDepot(new Depots());
        emplacement.setParent(new Emplacement());
        emplacement.setFils(new ArrayList<Emplacement>());
        selectEmplacement = null;
        tabIds_emplacement = "";
        update("form_emplacement_depot");
    }

    public void resetFicheLiaisonDepot() {
        resetFiche(liaison);
        agenceDepot = 0;
        liaison.setDepot(new Depots());
        tabIds_liaison = "";
        selectLiaison = null;
        depots_agence.clear();
        setUpdateLiaison(false);
    }

    public void resetFichePoint() {
        resetFiche(pointDepot);
        pointDepot.setDepot(new Depots());
        pointDepot.setPointVente(new PointVente());
        pointDepot.setPrincipal(false);
        selectPoint = null;
        tabIds_point = "";
    }

    public void resetFicheType() {
        resetFiche(type);
    }

    public void resetFicheCreneau() {
        resetFiche(creneau);
        creneau.setDepot(new Depots());
        creneau.setJour(new JoursOuvres());
        creneau.setTranche(new TrancheHoraire());
        creneau.setCritere("");
        tabIds_creneau = "";
        loadAllTypes(null);
        setUpdateCrenau(false);
    }

    public void resetFicheOpe() {
        resetFiche(ope);
        ope.setType("TRANSFERT");
        ope.setOperation("TRANSFERT");
        tabIds_operation = "";
        selectOperation = null;
        chooseTypeOpe();
    }

    @Override
    public boolean saveNew() {
        String action = isUpdateDepot() ? "Modification" : "Insertion";
        try {
            Depots bean = recopieView();
            if (controleFiche(bean)) {
                selectDepot = UtilCom.buildDepot(bean, currentUser, currentAgence);
                if (bean.getCodeAcces() != null ? !bean.getCodeAcces().isEmpty() : false) {
                    //trouve ce code d'accès
                    selectDepot.setCodeAcces(returnCodeAcces(bean.getCodeAcces()));
                }
                if (!isUpdateDepot()) {
                    if (!autoriser("base_depots_save")) {
                        openNotAcces();
                        return false;
                    }
                    selectDepot.setId(null);
                    selectDepot = (YvsBaseDepots) dao.save1(selectDepot);
                    depot.setId(selectDepot.getId());
                    depots.add(0, selectDepot);
                } else {
                    if (!autoriser("base_depots_update")) {
                        openNotAcces();
                        return false;
                    }
                    dao.update(selectDepot);
                    int idx = depots.indexOf(selectDepot);
                    if (idx >= 0) {
                        depots.set(idx, selectDepot);
                    } else {
                        depots.add(selectDepot);
                    }
                }
                setUpdateDepot(true);
                update("data_depot");
                ManagedStockArticle service = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                if (service != null) {
                    service.setEntityDepot(selectDepot);
                }
                actionOpenOrResetAfter(this);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public void saveNewArticle() {
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            m.setEntityDepot(selectDepot);
            m.saveNewArticle();
        }
    }

    public YvsBaseEmplacementDepot saveDefautEmplacement() {
        try {
            for (YvsBaseEmplacementDepot e : emplacements) {
                if (e.getDefaut()) {
                    return e;
                }
            }
            Emplacement bean = defautEmplacement();
            YvsBaseEmplacementDepot entity = UtilCom.buildEmplacement(bean, currentUser, selectDepot);
            entity.setId(null);
            entity = (YvsBaseEmplacementDepot) dao.save1(entity);
            emplacement.setId(entity.getId());
            emplacements.add(0, entity);
            update("data_emplacement_depot");
            update("data_depot");
            update("select_emplacement_article");
            return entity;
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            System.err.println("Error Insertion : " + ex.getMessage());
            return null;
        }
    }

    public boolean saveNewEmplacement(boolean viewDepot) {
        String action = emplacement.isUpdate() ? "Modification" : "Insertion";
        try {
            if (controleFicheEmplacement(emplacement)) {
                YvsBaseEmplacementDepot entity = UtilCom.buildEmplacement(emplacement, currentUser, selectDepot);
                if (!viewDepot) {
                    int idx = depots_all.indexOf(new YvsBaseDepots(emplacement.getDepot().getId()));
                    if (idx >= 0) {
                        entity.setDepot(depots_all.get(idx));
                    }
                }
                entity.setNew_(true);
                if (emplacement.getId() <= 0) {
                    entity.setId(null);
                    entity = (YvsBaseEmplacementDepot) dao.save1(entity);
                    emplacement.setId(entity.getId());
                    emplacements.add(0, entity);
                    emplacementsParents.add(0, entity);
                    emplacementsArticle.add(0, entity);
                } else {
                    dao.update(entity);
                    int idx = emplacements.indexOf(entity);
                    if (idx >= 0) {
                        emplacements.set(idx, entity);
                    }
//                    emplacementsArticle.set(emplacementsArticle.indexOf(entity), entity);
//                    if (emplacementsParents.contains(entity)) {
//                        emplacementsParents.set(emplacementsParents.indexOf(entity), entity);
//                    } else {
//                        emplacementsParents.add(0, entity);
//                    }
                }
                succes();
                resetFicheEmplacement();
                update("select_emplacement_article");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public void saveArticleEmplacement(YvsBaseArticleDepot a) {
        try {
            YvsBaseArticleEmplacement y = (YvsBaseArticleEmplacement) dao.loadOneByNameQueries("YvsBaseArticleEmplacement.findByArticleEmplacement", new String[]{"article", "emplacement"}, new Object[]{a, selectEmplacement});
            if (y != null ? y.getId() < 1 : true) {
                y = new YvsBaseArticleEmplacement();
                y.setArticle(a);
                y.setAuthor(currentUser);
                y.setEmplacement(selectEmplacement);
                dao.save1(y);
                articlesEmplacement.add(a);
            } else {
                dao.delete(y);
                articlesEmplacement.remove(a);
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            getException("Error (saveAllArticleEmplacement) : " + ex.getMessage(), ex);
        }
    }

    public void saveNewArticleEmplacement(YvsBaseArticleEmplacement y) {
        try {
            if (y != null) {
                if (y.getId() == null ? true : y.getId() <= 0) {
                    addNewArticleEmplacement(y);
                } else {
                    removeArticleEmplacement(y);
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            getException("Error (saveAllArticleEmplacement) : " + ex.getMessage(), ex);
        }
    }

    public void addNewArticleEmplacement(YvsBaseArticleEmplacement y) {
        if (y != null) {
            if (y.getId() == null ? true : y.getId() <= 0) {
                y.setId(null);
                y.setAuthor(currentUser);
                y.setDateSave(new Date());
                y.setDateUpdate(new Date());
                y.setEmplacement(selectEmplacement);
                y = (YvsBaseArticleEmplacement) dao.save1(y);
            }
        }
    }

    public void removeArticleEmplacement(YvsBaseArticleEmplacement y) {
        if (y != null) {
            if (y.getId() == null ? true : y.getId() <= 0) {

            } else {
                y.setAuthor(currentUser);
                y.setDateUpdate(new Date());
                dao.delete(y);
            }
        }
    }

    public boolean saveNewLiasonDepot() {
        String action = isUpdateLiaison() ? "Modification" : "Insertion";
        try {
            if (!autoriser("base_depots_lie_depot")) {
                openNotAcces();
                return false;
            }
            LiaisonDepot bean = recopieViewLiaisonDepot();
            ManagedAgence w = (ManagedAgence) giveManagedBean(ManagedAgence.class);
            if (controleFicheLiasonDepot(bean) && w != null) {
                YvsAgences agence = w.getListAgence().get(w.getListAgence().indexOf(new YvsAgences((Long) agenceDepot)));
                YvsComLiaisonDepot entity = UtilCom.buildLiaisonDepot(bean, currentUser, selectDepot, agence);
                if (!isUpdateLiaison()) {
                    entity.setId(null);
                    entity = (YvsComLiaisonDepot) dao.save1(entity);
                    liaisons_depot.add(0, entity);
                } else {
                    dao.update(entity);
                    liaisons_depot.set(liaisons_depot.indexOf(entity), entity);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewPoint() {
        try {
            PointVente bean = recopieViewPoint();
            if (controleFichePoint(bean)) {
                YvsBasePointVente y = UtilCom.buildPointVente(bean, currentUser, currentAgence);
                y.setId(null);
                y = (YvsBasePointVente) dao.save1(y);
                bean.setId(y.getId());
                pointDepot.setPointVente(bean);
                pointsvente.add(0, y);
                point = new PointVente();
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            System.err.println("Error Insertion : " + ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean saveNewPointDepot() {
        String action = pointDepot.isUpdate() ? "Modification" : "Insertion";
        try {
            if (!autoriser("base_depots_lie_point")) {
                openNotAcces();
                return false;
            }
            PointVenteDepot bean = recopieViewPointDepot();
            if (controleFichePointDepot(bean)) {
                YvsBasePointVente point = pointsvente.get(pointsvente.indexOf(new YvsBasePointVente(bean.getPointVente().getId())));
                YvsBasePointVenteDepot y = UtilCom.buildPointVenteDepot(bean, currentUser, selectDepot, point);
                if (!bean.isUpdate()) {
                    y.setId(null);
                    y = (YvsBasePointVenteDepot) dao.save1(y);
                    points_depot.add(y);
                } else {
                    dao.update(y);
                    bean.setUpdate(true);
                    points_depot.set(points_depot.indexOf(y), y);
                }
                succes();
                resetFichePoint();
                update("data_point_vente");
                update("data_point_vente_depot");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewType() {
        try {
            TrancheHoraire bean = recopieViewType();
            if (controleFicheType(bean)) {
                YvsGrhTrancheHoraire entity = UtilGrh.buildTrancheHoraire(bean, currentUser);
                entity.setId(null);
                entity = (YvsGrhTrancheHoraire) dao.save1(entity);
                bean.setId(entity.getId());
                type.setId(entity.getId());
                creneau.setTranche(bean);
                types.add(0, entity);
                succes();
                loadCritere();
                resetFicheType();
                update("select_type_creneau");
                update("select_critere");
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            System.err.println("Error Insertion : " + ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean saveNewCreneau() {
        String action = isUpdateCrenau() ? "Modification" : "Insertion";
        try {
            if (!autoriser("base_depots_add_creneau")) {
                openNotAcces();
                return false;
            }
            Creneau bean = recopieViewCreneau();
            if (controleFicheCreneau(bean)) {
                YvsComCreneauDepot entity = UtilCom.buildCreneau(bean, currentUser, selectDepot);
                bean.setCritere(bean.getTranche().getTypeJournee());
                if (!isUpdateCrenau()) {
                    entity.setId(null);
                    entity = (YvsComCreneauDepot) dao.save1(entity);
                    creneau.setId(entity.getId());
                    creneaux.add(0, entity);
                } else {
                    dao.update(entity);
                    creneaux.set(creneaux.indexOf(entity), entity);
                }
                resetFicheCreneau();
                succes();
                update("data_creneau");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewOpe() {
        String action = ope.isUpdate() ? "Modification" : "Insertion";
        try {
            DepotOperation bean = recopieViewOperation();
            if (controleFicheOpe(bean)) {
                YvsBaseDepotOperation entity = UtilCom.buildBeanDepotOperation(ope, currentUser, selectDepot);
                if (!bean.isUpdate()) {
                    entity.setId(null);
                    entity = (YvsBaseDepotOperation) dao.save1(entity);
                    ope.setId(entity.getId());
                    operations.add(0, entity);
                    checkOperation(true);
                } else {
                    dao.update(entity);
                    operations.set(operations.indexOf(entity), entity);
                }
                succes();
                update("data_operation");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if (!autoriser("base_depots_delete")) {
                openNotAcces();
                return;
            }
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> ids = decomposeIdSelection(tabIds);
                tabIds = "";
                List<YvsBaseDepots> list = new ArrayList<>();
                YvsBaseDepots bean;
                for (Long id : ids) {
                    int index = depots.indexOf(new YvsBaseDepots(id));
                    if (index > -1) {
                        bean = depots.get(index);
                        bean.setAuthor(currentUser);
                        bean.setDateUpdate(new Date());
                        list.add(bean);
                        dao.delete(bean);
                    }
                }
                depots.removeAll(list);
                succes();
                resetFiche();
                update("data_depot");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsBaseDepots y) {
        selectDepot = y;
    }

    public void deleteBean_() {
        if (!autoriser("base_depots_delete")) {
            openNotAcces();
            return;
        }
        try {
            if (selectDepot != null) {
                dao.delete(selectDepot);
                depots.remove(selectDepot);
                succes();
                resetFiche();
                update("data_depot");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanEmplacement() {
        try {
            if (!autoriser("base_depots_update")) {
                openNotAcces();
                return;
            }
            if ((tabIds_emplacement != null) ? !tabIds_emplacement.equals("") : false) {
                String[] tab = tabIds_emplacement.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsBaseEmplacementDepot bean = emplacements.get(emplacements.indexOf(new YvsBaseEmplacementDepot(id)));
                    dao.delete(new YvsBaseEmplacementDepot(bean.getId()));
                    emplacements.remove(bean);
                }
                succes();
                resetFicheEmplacement();
                update("data_emplacement_depot");
                update("data_depot");
                update("select_emplacement_article");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanEmplacement_(YvsBaseEmplacementDepot y) {
        selectEmplacement = y;
    }

    public void deleteBeanEmplacement_() {
        try {
            if (selectEmplacement != null) {
                selectEmplacement.setAuthor(currentUser);
                selectEmplacement.setDateUpdate(new Date());
                dao.delete(selectEmplacement);
                emplacements.remove(selectEmplacement);
                succes();
                resetFicheEmplacement();
                update("data_emplacement_depot");
                update("data_depot");
                update("select_emplacement_article");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanLiaisonDepot() {
        try {
            if (!autoriser("base_depots_lie_depot")) {
                openNotAcces();
                return;
            }
            if ((tabIds_liaison != null) ? !tabIds_liaison.equals("") : false) {
                String[] tab = tabIds_liaison.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComLiaisonDepot bean = liaisons_depot.get(liaisons_depot.indexOf(new YvsComLiaisonDepot(id)));
                    dao.delete(new YvsComLiaisonDepot(bean.getId()));
                    liaisons_depot.remove(bean);
                }
                succes();
                resetFicheLiaisonDepot();
                update("data_liaison_depot");
                update("data_depot");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanLiaisonDepot_(YvsComLiaisonDepot y) {
        if (!autoriser("base_depots_lie_depot")) {
            openNotAcces();
            return;
        }
        selectLiaison = y;
    }

    public void deleteBeanLiaisonDepot_() {
        try {
            if (!autoriser("base_depots_lie_depot")) {
                openNotAcces();
                return;
            }
            if (selectLiaison != null) {
                dao.delete(selectLiaison);
                liaisons_depot.remove(selectLiaison);
                succes();
                resetFicheLiaisonDepot();
                update("data_liaison_depot");
                update("data_depot");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanPoint() {
        try {
            if (!autoriser("base_depots_lie_point")) {
                openNotAcces();
                return;
            }
            if ((tabIds_point != null) ? !tabIds_point.equals("") : false) {
                String[] tab = tabIds_point.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsBasePointVenteDepot bean = points_depot.get(points_depot.indexOf(new YvsBasePointVenteDepot(id)));
                    dao.delete(new YvsBasePointVenteDepot(bean.getId()));
                    points_depot.remove(bean);
                }
                succes();
                resetFichePoint();
                update("data_depot");
                update("data_point_vente_depot");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanPoint_(YvsBasePointVenteDepot y) {
        if (!autoriser("base_depots_lie_point")) {
            openNotAcces();
            return;
        }
        selectPoint = y;
    }

    public void deleteBeanPoint_() {
        try {
            if (!autoriser("base_depots_lie_point")) {
                openNotAcces();
                return;
            }
            if (selectPoint != null) {
                dao.delete(selectPoint);
                points_depot.remove(selectPoint);
                succes();
                resetFichePoint();
                update("data_depot");
                update("data_point_vente_depot");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanCreneau() {
        try {
            if ((tabIds_creneau != null) ? !tabIds_creneau.equals("") : false) {
                String[] tab = tabIds_creneau.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComCreneauDepot bean = creneaux.get(creneaux.indexOf(new YvsComCreneauDepot(id)));
                    dao.delete(new YvsComCreneauDepot(bean.getId()));
                    creneaux.remove(bean);
                }
                succes();
                update("data_creneau");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanCreneau_(YvsComCreneauDepot y) {
        selectCreneau = y;
    }

    public void deleteBeanCreneau_() {
        try {
            if (!autoriser("base_depots_add_creneau")) {
                openNotAcces();
                return;
            }
            if (selectCreneau != null) {
                dao.delete(selectCreneau);
                creneaux.remove(selectCreneau);
                succes();
                update("data_creneau");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanOpe() {
        try {
            if ((tabIds_operation != null) ? !tabIds_operation.equals("") : false) {
                String[] tab = tabIds_operation.split("-");
                for (String ids : tab) {
                    int id = Integer.valueOf(ids);
                    YvsBaseDepotOperation bean = operations.get(operations.indexOf(new YvsBaseDepotOperation(id)));
                    dao.delete(bean);
                    operations.remove(bean);
                }
                checkOperation(true);
                succes();
                update("data_operation");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBeanOpe_(YvsBaseDepotOperation y) {
        selectOperation = y;
        if (!memoriseDeletepoint) {
            openDialog("dlgConfirmDeleteOpe_");
        } else {
            deleteBeanOpe_();
        }
    }

    public void deleteBeanOpe_() {
        try {
            if (selectOperation != null) {
                dao.delete(selectOperation);
                operations.remove(selectOperation);
                checkOperation(true);
                succes();
                update("data_operation");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    @Override
    public void onSelectObject(YvsBaseDepots bean) {
        selectDepot = bean;
        populateView(UtilCom.buildBeanDepot(selectDepot));
        ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
        if (service != null) {
            service.setAgence(selectDepot.getAgence());
//            service.loadAllEmployesByAgence(true, true);
        }
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            m.resetFicheArticle();
            m.setEntityDepot(selectDepot);
            m.setEntityArticle(null);
            m.getArticles_depot().clear();
        }
        loadOthers(selectDepot, true);
        checkOperation(false);

        //charge les user        
        users = dao.loadNameQueries("YvsUsers.findAlls", new String[]{"societe", "actif"}, new Object[]{currentAgence.getSociete(), true});
        YvsBaseDepotsUser actif;
        for (YvsUsers u : users) {
            actif = (YvsBaseDepotsUser) dao.loadObjectByNameQueries("YvsBaseDepotsUser.findOne", new String[]{"depot", "user"}, new Object[]{bean, u});
            if (actif != null ? actif.getId() > 0 : false) {
                u.setDepot(bean);
                u.setNew_(actif.getActif());
            } else {
                u.setDepot(null);
                u.setNew_(false);
            }
        }

        resetFicheCreneau();
        resetFicheEmplacement();
        resetFicheLiaisonDepot();
        resetFichePoint();
        resetFicheOpe();
        update("blog_form_depot");
    }

    public void addLiaisonDepotUser(YvsUsers user) {
        if (!autoriser("base_depots_add_accessibilite")) {
            openNotAcces();
            return;
        }
        YvsBaseDepotsUser c = (YvsBaseDepotsUser) dao.loadOneByNameQueries("YvsBaseDepotsUser.findOne", new String[]{"depot", "user"}, new Object[]{selectDepot, user});
        if (c == null) {
            c = new YvsBaseDepotsUser();
            c.setActif(true);
            c.setAuthor(currentUser);
            c.setDateSave(new Date());
            c.setDateUpdate(new Date());
            c.setDepot(selectDepot);
            c.setUsers(user);
            dao.save1(c);
        } else {
            c.setDateUpdate(new Date());
            c.setActif(!c.getActif());
            dao.update(c);
        }
        user.setDepot(selectDepot);
        user.setNew_(c.getActif());
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseDepots bean = (YvsBaseDepots) ev.getObject();
            onSelectObject(bean);
            execute("collapseForm('depot')");
            execute("collapseForm('article_depot')");
            execute("collapseForm('liaison_depot')");
            execute("collapseForm('emplacement_depot')");
            execute("collapseForm('creneau')");
            execute("collapseForm('operation')");
            tabIds = depots.indexOf(bean) + "";

        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("blog_form_depot");
    }

    @Override
    public void onSelectDistant(YvsBaseDepots y) {
        Navigations n = (Navigations) giveManagedBean(Navigations.class);
        if (n != null) {
            n.naviguationView("Dépôts", "modGescom", "smenDepotCom", true);
        }
        onSelectObject(y);
    }

    public void loadOnViewEmploye(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsGrhEmployes bean = (YvsGrhEmployes) ev.getObject();
            depot.setResponsable(UtilGrh.buildBeanSimplePartialEmploye(bean));
            depot.getResponsable().setNom(depot.getResponsable().getNom_prenom());
            update("txt_employe_depot");
        }
    }

    public void loadOnViewLiaison(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComLiaisonDepot bean = (YvsComLiaisonDepot) ev.getObject();
            if (bean.getDepotLier() != null ? bean.getDepotLier().getAgence() != null : false) {
                agenceDepot = bean.getDepotLier().getAgence().getId();
                chooseAgence(true);
            }
            populateViewLiaisonDepot(UtilCom.buildBeanLiaisonDepot(bean));
            update("blog_form_liaison_depot");
        }
    }

    public void unLoadOnViewLiaison(UnselectEvent ev) {
        resetFicheLiaisonDepot();
        update("blog_form_liaison_depot");
    }

    public void loadOnViewEmplacement(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseEmplacementDepot bean = (YvsBaseEmplacementDepot) ev.getObject();
            populateViewEmplacement(UtilCom.buildBeanEmplacement(bean));
            emplacementsParents.clear();
            emplacementsParents.addAll(emplacements);
            if (emplacementsParents.contains(bean)) {
                emplacementsParents.remove(bean);
            }
            update("form_emplacement_depot");
        }
    }

    public void unLoadOnViewEmplacement(UnselectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseEmplacementDepot bean = (YvsBaseEmplacementDepot) ev.getObject();
            emplacementsParents.clear();
            emplacementsParents.addAll(emplacements);
        }
        resetFicheEmplacement();
        update("form_emplacement_depot");
    }

    public void loadOnViewPoint(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBasePointVenteDepot bean = (YvsBasePointVenteDepot) ev.getObject();
            populateViewPoint(UtilCom.buildBeanPointVenteDepot(bean));
            update("form_point_vente_depot");
        }
    }

    public void unLoadOnViewPoint(UnselectEvent ev) {
        resetFichePoint();
        update("blog_form_point_vente_depot");
    }

    public void loadOnViewCreneau(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComCreneauDepot bean = (YvsComCreneauDepot) ev.getObject();
            populateViewCreneau(UtilCom.buildBeanCreneau(bean));
            chooseCritere();
        }
    }

    public void unLoadOnViewCreneau(UnselectEvent ev) {
        resetFicheCreneau();
        update("form_creneau");
    }

    public void loadOnViewOpe(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseDepotOperation bean = (YvsBaseDepotOperation) ev.getObject();
            populateViewOpe(UtilCom.buildBeanDepotOperation(bean));
            chooseTypeOpe();
            ope.setOperation(bean.getOperation());
            update("form_operation");
        }
    }

    public void unLoadOnViewOpe(UnselectEvent ev) {
        resetFicheOpe();
        update("blog_form_operation");
    }

    public void onArticleSelect(SelectEvent ev) {
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles t = (YvsBaseArticles) ev.getObject();
            if (m != null) {
                cloneObject(m.getArticle().getArticle(), UtilCom.buildBeanArticle(t));
            }
        } else {
            if (m != null) {
                m.resetFicheArticle();
            }
        }
    }

    public void onArticleEmplacement(YvsBaseEmplacementDepot y) {
        if (!autoriser("base_depots_add_article_emplacement")) {
            openNotAcces();
            return;
        }
        selectEmplacement = y;
        if (y != null ? y.getId() > 0 : false) {
            articlesEmplacement = dao.loadNameQueries("YvsBaseArticleEmplacement.findArticleByEmplacement", new String[]{"emplacement"}, new Object[]{y});
            openDialog("dlgAddArticleEmplacement");
            update("data_article_emplacement_depot");
        }
    }

    public boolean verifyArticleEmplacement(YvsBaseArticleDepot y) {
        return articlesEmplacement.contains(y);
    }

    public void activeDepot(YvsBaseDepots bean) {
        if (!autoriser("base_depots_update")) {
            openNotAcces();
            return;
        }
        if (bean != null) {
            bean.setActif(!bean.getActif());
            String rq = "UPDATE yvs_base_depots SET actif=" + bean.getActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            depots.set(depots.indexOf(bean), bean);
            succes();
        }
    }

    public void setActifDepots(boolean actif) {
        if (!autoriser("base_depots_update")) {
            openNotAcces();
            return;
        }
        List<Long> ids = decomposeIdSelection(tabIds);
        tabIds = "";
        YvsBaseDepots bean;
        for (Long id : ids) {
            int index = depots.indexOf(new YvsBaseDepots(id));
            if (index > -1) {
                bean = depots.get(index);
                bean.setAuthor(currentUser);
                bean.setDateUpdate(new Date());
                bean.setActif(actif);
                dao.delete(bean);
                depots.set(index, bean);
            }
        }
        succes();
    }

    public void activeDepots() {
        setActifDepots(true);
    }

    public void desactiveDepots() {
        setActifDepots(false);
    }

    public void addPlanning(YvsBaseDepots y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedCreneauEmploye w = (ManagedCreneauEmploye) giveManagedBean(ManagedCreneauEmploye.class);
            if (w != null) {
                w.resetFiche();
                w.setDepot(new Depots(y.getId()));
                w.chooseDepot();
                update("select_creneau_point");
                update("txt_point_vente_creneau");
                update("form_creneau_employe");
            }
        }
    }

    public void chooseEmplacementParent() {
        if ((emplacement.getParent() != null) ? emplacement.getParent().getId() > 0 : false) {
            YvsBaseEmplacementDepot d_ = emplacementsParents.get(emplacementsParents.indexOf(new YvsBaseEmplacementDepot(emplacement.getParent().getId())));
            Emplacement d = UtilCom.buildBeanEmplacement(d_);
            cloneObject(emplacement.getParent(), d);
        }
    }

    public void chooseDepot() {
        if ((liaison.getDepot() != null) ? liaison.getDepot().getId() > 0 : false) {
            YvsBaseDepots d_ = depots_agence.get(depots_agence.indexOf(new YvsBaseDepots(liaison.getDepot().getId())));
            Depots d = UtilCom.buildBeanDepot(d_);
            cloneObject(liaison.getDepot(), d);
        }
    }

    public void chooseAgence(boolean depot) {
        ManagedAgence w = (ManagedAgence) giveManagedBean(ManagedAgence.class);
        if (depot && w != null) {
            depots_agence.clear();
            if (agenceDepot > 0) {
                int idx = w.getListAgence().indexOf(new YvsAgences(agenceDepot));
                if (idx > -1) {
                    YvsAgences a = w.getListAgence().get(idx);
                    champ = new String[]{"agence", "actif"};
                    val = new Object[]{a, true};
                    depots_agence.addAll(dao.loadNameQueries("YvsBaseDepots.findByAgenceActif", champ, val));
                    if (depots_agence.contains(selectDepot)) {
                        depots_agence.remove(selectDepot);
                    }
                }
            }
        } else if (w != null) {
            pointsvente.clear();
            if (agencePoint > 0) {
                int idx = w.getListAgence().indexOf(new YvsAgences(agencePoint));
                if (idx > -1) {
                    YvsAgences a = w.getListAgence().get(idx);
                    champ = new String[]{"agence", "actif"};
                    val = new Object[]{a, true};
                    pointsvente = dao.loadNameQueries("YvsBasePointVente.findByAgenceActif", champ, val);
                    return;
                }
            }
//            loadAllPointVente();
        }
    }

    public void chooseAgences() {
        if (depot.getAgence() != null ? depot.getAgence().getId() > 0 : false) {
            ManagedEmployes m = (ManagedEmployes) giveManagedBean("MEmps");
            if (m != null) {
                m.setAgence(new YvsAgences(depot.getAgence().getId()));
                m.loadAllEmployesByAgence(true, true);
            }
        }
    }

    public void chooseCritere() {
        loadAllTypes(creneau.getCritere());
        if (creneau.getTranche() == null) {
            creneau.setTranche(new TrancheHoraire());
        }
    }

    public void chooseJour() {
        if ((creneau.getJour() != null) ? creneau.getJour().getId() > 0 : false) {
            YvsJoursOuvres d_ = jours.get(jours.indexOf(new YvsJoursOuvres(creneau.getJour().getId())));
            JoursOuvres d = UtilGrh.buildBeanJoursOuvree(d_);
            cloneObject(creneau.getJour(), d);
        }
    }

    public void chooseType() {
        if ((creneau.getTranche() != null) ? creneau.getTranche().getId() > 0 : false) {
            YvsGrhTrancheHoraire Y = types.get(types.indexOf(new YvsGrhTrancheHoraire(creneau.getTranche().getId())));
            TrancheHoraire d = UtilCom.buildBeanTrancheHoraire(Y);
            cloneObject(creneau.getTranche(), d);
        } else {
            if ((creneau.getTranche() != null) ? creneau.getTranche().getId() < 0 : true) {
                openDialog("dlgAddTrancheHoraire");
            }
        }
    }

    public void chooseTypeOpe() {
        ope.setOperation("");
        typesOpe = _chooseTypeOpe(ope.getType());
        update("select_operation");
    }

    public List<String> _chooseTypeOpe(String type) {
        List<String> ops = new ArrayList<>();
        switch (type) {
            case Constantes.TRANSFERT: {
                ops.add(Constantes.TRANSFERT);
                ops.add(Constantes.TRANSIT);
                break;
            }
            case Constantes.ENTREE: {
                ops.add(Constantes.OP_DONS);
                ops.add(Constantes.OP_AJUSTEMENT_STOCK);
                ops.add(Constantes.OP_INITIALISATION);
                ops.add(Constantes.PRODUCTION);
                ops.add(Constantes.RETOUR);
                ops.add(Constantes.ACHAT);
                break;
            }
            case Constantes.SORTIE: {
                ops.add(Constantes.OP_DONS);
                ops.add(Constantes.OP_AJUSTEMENT_STOCK);
                ops.add(Constantes.OP_DEPRECIATION);
                ops.add(Constantes.PRODUCTION);
                ops.add(Constantes.RETOUR);
                ops.add(Constantes.VENTE);
                ops.add(Constantes.OP_RATIONS);
                break;
            }
            case Constantes.AUTRE: {
                ops.add(Constantes.RESERVATION);
                ops.add(Constantes.TECHNIQUE);
                break;
            }
            default:
                break;
        }
        return ops;
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllDepot(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllDepot(true, true);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsBaseDepots> re = paginator.parcoursDynamicData("YvsBaseDepots", "y", "y.designation", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    private void checkOperation(boolean save) {
        depot.setOpAchat(false);
        depot.setOpProduction(false);
        depot.setOpReserv(false);
        depot.setOpRetour(false);
        depot.setOpTechnique(false);
        depot.setOpTransit(false);
        depot.setOpVente(false);
        for (YvsBaseDepotOperation o : operations) {
            if (!depot.isOpProduction()) {
                depot.setOpProduction(o.getOperation().equals(Constantes.PRODUCTION));
            }
            if (!depot.isOpAchat()) {
                depot.setOpAchat(o.getOperation().equals(Constantes.ACHAT));
            }
            if (!depot.isOpVente()) {
                depot.setOpVente(o.getOperation().equals(Constantes.VENTE));
            }
            if (!depot.isOpTechnique()) {
                depot.setOpTechnique(o.getOperation().equals(Constantes.TECHNIQUE));
            }
            if (!depot.isOpReserv()) {
                depot.setOpReserv(o.getOperation().equals(Constantes.RESERVATION));
            }
            if (!depot.isOpTransit()) {
                depot.setOpTransit(o.getOperation().equals(Constantes.TRANSFERT));
            }
            if (!depot.isOpRetour()) {
                depot.setOpRetour(o.getOperation().equals(Constantes.RETOUR));
            }
        }
        if (save) {
            selectDepot.setOpAchat(depot.isOpAchat());
            selectDepot.setOpProduction(depot.isOpProduction());
            selectDepot.setOpReserv(depot.isOpReserv());
            selectDepot.setOpRetour(depot.isOpRetour());
            selectDepot.setOpTechnique(depot.isOpTechnique());
            selectDepot.setOpTransit(depot.isOpTransit());
            selectDepot.setOpVente(depot.isOpVente());

            dao.update(selectDepot);
            depots.set(depots.indexOf(selectDepot), selectDepot);
            update("data_depot");
        }
        update("blog_operation_check");
    }

    public void onCritereSelect(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            String d = (String) ev.getObject();
            type.setCritere(d);
        }
    }

    public void putOpProd(YvsBaseDepots bean) {
        if (bean != null) {
            bean.setOpProduction(!bean.getOpProduction());
            String rq = "UPDATE yvs_base_depots SET op_production=" + bean.getOpProduction() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            depots.set(depots.indexOf(bean), bean);
            if (depotsSelect.contains(bean)) {
                depotsSelect.set(depotsSelect.indexOf(bean), bean);
            }
        }
    }

    public void putOpAchat(YvsBaseDepots bean) {
        if (bean != null) {
            bean.setOpAchat(!bean.getOpAchat());
            String rq = "UPDATE yvs_base_depots SET op_achat=" + bean.getOpAchat() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            depots.set(depots.indexOf(bean), bean);
            if (depotsSelect.contains(bean)) {
                depotsSelect.set(depotsSelect.indexOf(bean), bean);
            }
        }
    }

    public void putOpVente(YvsBaseDepots bean) {
        if (bean != null) {
            bean.setOpVente(!bean.getOpVente());
            String rq = "UPDATE yvs_base_depots SET op_vente=" + bean.getOpVente() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            depots.set(depots.indexOf(bean), bean);
            if (depotsSelect.contains(bean)) {
                depotsSelect.set(depotsSelect.indexOf(bean), bean);
            }
        }
    }

    public void putOpTransit(YvsBaseDepots bean) {
        if (bean != null) {
            bean.setOpTransit(!bean.getOpTransit());
            String rq = "UPDATE yvs_base_depots SET op_transit=" + bean.getOpTransit() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            depots.set(depots.indexOf(bean), bean);
            if (depotsSelect.contains(bean)) {
                depotsSelect.set(depotsSelect.indexOf(bean), bean);
            }
        }
    }

    public void putOpRetour(YvsBaseDepots bean) {
        if (bean != null) {
            bean.setOpRetour(!bean.getOpRetour());
            String rq = "UPDATE yvs_base_depots SET op_retour=" + bean.getOpRetour() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            depots.set(depots.indexOf(bean), bean);
            if (depotsSelect.contains(bean)) {
                depotsSelect.set(depotsSelect.indexOf(bean), bean);
            }
        }
    }

    public void putOpReserv(YvsBaseDepots bean) {
        if (bean != null) {
            bean.setOpReserv(!bean.getOpReserv());
            String rq = "UPDATE yvs_base_depots SET op_reserv=" + bean.getOpReserv() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            depots.set(depots.indexOf(bean), bean);
            if (depotsSelect.contains(bean)) {
                depotsSelect.set(depotsSelect.indexOf(bean), bean);
            }
        }
    }

    public void putOpTechnique(YvsBaseDepots bean) {
        if (bean != null) {
            bean.setOpTechnique(!bean.getOpTechnique());
            String rq = "UPDATE yvs_base_depots SET op_technique=" + bean.getOpTechnique() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            depots.set(depots.indexOf(bean), bean);
            if (depotsSelect.contains(bean)) {
                depotsSelect.set(depotsSelect.indexOf(bean), bean);
            }
        }
    }

    public void activeEmplacement(YvsBaseEmplacementDepot bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            String rq = "UPDATE yvs_base_emplacement_depot SET actif=" + bean.getActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            emplacements.set(emplacements.indexOf(bean), bean);
            succes();
        }
    }

    public void activeTransit(YvsComLiaisonDepot bean) {
        if (bean != null) {
            bean.setTransit(!bean.getTransit());
            bean.setDateUpdate(new Date());
            bean.setAuthor(currentUser);
            dao.update(bean);
            liaisons_depot.set(liaisons_depot.indexOf(bean), bean);
            succes();
        }
    }

    public void activePrincipal(YvsBasePointVenteDepot bean) {
        if (bean != null) {
            if (!bean.getPrincipal()) {
                List<YvsBasePointVenteDepot> p = dao.loadNameQueries("YvsBasePointVenteDepot.findByPointPrincipal", new String[]{"point"}, new Object[]{bean.getPointVente()}, 0, 1);
                if (p != null ? !p.isEmpty() : false) {
                    getErrorMessage("Ce point de vente a deja un dépôt principal");
                    return;
                }
            }
            bean.setPrincipal(!bean.getPrincipal());
            String rq = "UPDATE yvs_base_point_vente_depot SET principal=" + bean.getPrincipal() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            points_depot.set(points_depot.indexOf(bean), bean);
            succes();
        }
    }

    public void activePoint(YvsBasePointVenteDepot bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            String rq = "UPDATE yvs_base_point_vente_depot SET actif=" + bean.getActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            points_depot.set(points_depot.indexOf(bean), bean);
            succes();
        }
    }

    public void activeCreneau(YvsComCreneauDepot bean) {
        if (bean != null) {
            if (!autoriser("base_depots_add_creneau")) {
                openNotAcces();
                return;
            }
            if (!bean.getActif() && !bean.getTranche().getActif()) {
                getErrorMessage("Vous ne pouvez pas activer ce créneau car son type est déactivé");
            } else {
                bean.setActif(!bean.getActif());
                String rq = "UPDATE yvs_com_creneau_depot SET actif=" + bean.getActif() + " WHERE id=?";
                Options[] param = new Options[]{new Options(bean.getId(), 1)};
                dao.requeteLibre(rq, param);
                creneaux.set(creneaux.indexOf(bean), bean);
                succes();
            }
        }
    }

    public void defautEmplacement(YvsBaseEmplacementDepot bean) {
        if (bean != null) {
            bean.setDefaut(!bean.getDefaut());
            if (bean.getDefaut()) {
                for (YvsBaseEmplacementDepot c : emplacements) {
                    if (!c.equals(bean) && c.getDefaut()) {
                        getErrorMessage("Vous avez deja crée un emplacement par défaut");
                        return;
                    }
                }
            }
            String rq = "UPDATE yvs_base_emplacement_depot SET defaut=" + bean.getDefaut() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            emplacements.set(emplacements.indexOf(bean), bean);
            succes();
            update("data_depot");
        }
    }

    public List<String> completeText(String query) {
        List<String> results = new ArrayList<>();
        for (String s : criteres) {
            if (s.toLowerCase().startsWith(query.toLowerCase())) {
                results.add(s);
            }
        }
        return results;
    }

    public void insertAllArticle() {
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            m.insertAllArticle(selectDepot, null);
        }
    }

    public void insertAllOperation() {
        try {
            if ((depot != null) ? depot.getId() > 0 : false) {
                boolean bien = false;
                List<String> typs = new ArrayList<String>() {
                    {
                        add(Constantes.TRANSFERT);
                        add(Constantes.ENTREE);
                        add(Constantes.SORTIE);
                        add(Constantes.AUTRE);
                    }
                };
                for (String t : typs) {
                    List<String> ops = _chooseTypeOpe(t);
                    for (String o : ops) {
                        champ = new String[]{"depot", "type", "operation"};
                        val = new Object[]{new YvsBaseDepots(depot.getId()), t, o};
                        List<YvsBaseDepotOperation> lo = dao.loadNameQueries("YvsBaseDepotOperation.findByDepotTypeOperation", champ, val, 0, 1);
                        if (lo != null ? lo.isEmpty() : true) {
                            YvsBaseDepotOperation y = new YvsBaseDepotOperation();
                            y.setAuthor(currentUser);
                            y.setDepot(new YvsBaseDepots(depot.getId()));
                            y.setOperation(o);
                            y.setType(t);
                            y.setNew_(true);
                            y.setDateSave(new Date());
                            y.setDateUpdate(new Date());
                            y = (YvsBaseDepotOperation) dao.save1(y);
                            operations.add(y);
                            bien = true;
                        }
                    }
                }
                if (bien) {
                    checkOperation(true);
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Initialisation Impossible!");
            System.err.println("Error initialisation : " + ex.getMessage());
        }
    }

    public void changeDesignation() {
        String nom = depot.getDesignation() != null ? depot.getDesignation() : "";
        depot.setAbbreviation(nom.length() < 4 ? nom : nom.substring(0, 3));
        changeAbbreviation();
    }

    public void changeAbbreviation() {
        if (depot.getCode() != null ? depot.getCode().startsWith("DPT_") : true) {
            depot.setCode("DPT_" + depot.getAbbreviation());
        }
    }

    public void changeOperation(YvsBaseDepots y) {
        depotsSelect.clear();
        if (y != null) {
            depotsSelect.add(y);
            nom_depot_select = y.getDesignation();
            openDialog("dlgChangeOpeDepot");
        }
        update("data_operation_depot");
    }

    public void searchEmploye() {
        String num = depot.getResponsable().getNom();
        ManagedEmployes m = (ManagedEmployes) giveManagedBean("MEmps");
        if (m != null) {
            m.findEmployeInSociete(num);
//            Employe e = m.searchEmployeActif(num, true);
            if (!m.getListEmployes().isEmpty()) {
                if (m.getListEmployes().size() == 1) {
                    depot.getResponsable().setError(false);
                    depot.setResponsable(UtilGrh.buildBeanSimplePartialEmploye(m.getListEmployes().get(0)));
                } else {
                    depot.getResponsable().setError(false);
                    openDialog("dlgListEmployes");
                }
            } else {
                depot.getResponsable().setError(true);
            }
            if (m.getListEmployes() != null ? m.getListEmployes().size() > 1 : false) {
                update("data_responsable_depot");
            }
        }
    }

    public void initEmployes() {
        ManagedEmployes m = (ManagedEmployes) giveManagedBean("MEmps");
        if (m != null) {
            m.initEmployes(depot.getResponsable());
            update("data_responsable_depot");
        }
    }

    public void addParamCode(String codeSearch) {
        ParametreRequete p;
        if (codeSearch != null ? codeSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", "%" + codeSearch + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.designation)", "reference", "%" + codeSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.code)", "reference1", "%" + codeSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.abbreviation)", "reference2", "%" + codeSearch.toUpperCase() + "%", "LIKE", "OR"));
        } else {
            p = new ParametreRequete("UPPER(y.designation)", "reference", null);
        }
        paginator.addParam(p);
        loadAllDepot(true, true);
    }

    public Depots searchDepotByCode(String codeSearch, boolean open) {
        paginator.addParam(new ParametreRequete("y.actif", "actif", true, "=", "AND"));
        addParamCode(codeSearch);
        return chekDepotInResult(true);
    }

    private Depots chekDepotInResult(boolean open) {
        Depots d = new Depots();
        if (depots != null ? !depots.isEmpty() : false) {
            if (depots.size() > 1) {
                if (open) {
                    openDialog("dlgDepot");
                }
//                a.setListArt(true);
            } else {
                YvsBaseDepots ed = depots.get(0);
                d = UtilProd.buildBeanDepot(ed);
            }
            d.setError(false);
        }
        return d;
    }

    public void addParamAgence(ValueChangeEvent ev) {
        agenceSearch = ((Long) ev.getNewValue());
        addParamAgence_();
    }

    public void addParamAgence_() {
        ParametreRequete p;
        if (agenceSearch != null ? agenceSearch > 0 : false) {
            p = new ParametreRequete("y.agence", "agence", new YvsAgences(agenceSearch), "=", "AND");
        } else {
            p = new ParametreRequete("y.agence", "agence", null, "=", "AND");
        }
        paginator.addParam(p);
        loadAllDepot(true, true);
    }

    public void addParamActif(ValueChangeEvent ev) {
        actifSearch = ((Boolean) ev.getNewValue());
        addParamActif_(actifSearch);
    }

    public void addParamActif_(Boolean actif) {
        addParamActif_(actif, true);
    }

    public void addParamActif_(Boolean actif, boolean load) {
        this.actifSearch = actif;
        paginator.addParam(new ParametreRequete("y.actif", "actif", actif, "=", "AND"));
        if (load) {
            loadAllDepot(true, true);
        }
    }

    public void clearParams() {
        paginator.getParams().clear();
        agenceSearch = 0L;
        typeSearch = null;
        actifSearch = null;
        codeSearch = null;
        loadAllDepot(true, true);
    }

    public void addParamType(ValueChangeEvent ev) {
        typeSearch = ((String) ev.getNewValue());
        if (typeSearch != null ? typeSearch.trim().length() > 0 : false) {
            switch (typeSearch) {
                case Constantes.PRODUCTION: {
                    addParamProd(true);
                    addParamAchat(null);
                    addParamVente(null);
                    addParamTrans(null);
                    addParamRetour(null);
                    addParamTech(null);
                    addParamReserv(null);
                    break;
                }
                case Constantes.ACHAT: {
                    addParamProd(null);
                    addParamAchat(true);
                    addParamVente(null);
                    addParamTrans(null);
                    addParamRetour(null);
                    addParamTech(null);
                    addParamReserv(null);
                    break;
                }
                case Constantes.TRANSFERT: {
                    addParamProd(null);
                    addParamAchat(null);
                    addParamVente(null);
                    addParamTrans(true);
                    addParamRetour(null);
                    addParamTech(null);
                    addParamReserv(null);
                    break;
                }
                case Constantes.VENTE: {
                    addParamProd(null);
                    addParamAchat(null);
                    addParamVente(true);
                    addParamTrans(null);
                    addParamRetour(null);
                    addParamTech(null);
                    addParamReserv(null);
                    break;
                }
                case Constantes.RESERVATION: {
                    addParamProd(null);
                    addParamAchat(null);
                    addParamVente(null);
                    addParamTrans(null);
                    addParamRetour(null);
                    addParamTech(null);
                    addParamReserv(true);
                    break;
                }
                case Constantes.TECHNIQUE: {
                    addParamProd(null);
                    addParamAchat(null);
                    addParamVente(null);
                    addParamTrans(null);
                    addParamRetour(null);
                    addParamTech(true);
                    addParamReserv(null);
                    break;
                }
                case Constantes.RETOUR: {
                    addParamProd(null);
                    addParamAchat(null);
                    addParamVente(null);
                    addParamTrans(null);
                    addParamRetour(true);
                    addParamTech(null);
                    addParamReserv(null);
                    break;
                }
                default: {
                    addParamProd(null);
                    addParamAchat(null);
                    addParamVente(null);
                    addParamTrans(null);
                    addParamRetour(null);
                    addParamTech(null);
                    addParamReserv(null);
                    break;
                }
            }
        } else {
            addParamProd(null);
            addParamAchat(null);
            addParamVente(null);
            addParamTrans(null);
            addParamRetour(null);
            addParamTech(null);
            addParamReserv(null);
        }
        loadAllDepot(true, true);
    }

    private void addParamProd(Boolean add) {
        ParametreRequete p;
        if (add != null) {
            p = new ParametreRequete("y.opProduction", "opProduction", add, "=", "AND");
        } else {
            p = new ParametreRequete("y.opProduction", "opProduction", null, "=", "AND");
        }
        paginator.addParam(p);
    }

    private void addParamAchat(Boolean add) {
        ParametreRequete p;
        if (add != null) {
            p = new ParametreRequete("y.opAchat", "opAchat", add, "=", "AND");
        } else {
            p = new ParametreRequete("y.opAchat", "opAchat", null, "=", "AND");
        }
        paginator.addParam(p);
    }

    private void addParamTrans(Boolean add) {
        ParametreRequete p;
        if (add != null) {
            p = new ParametreRequete("y.opTransit", "opTransit", add, "=", "AND");
        } else {
            p = new ParametreRequete("y.opTransit", "opTransit", null, "=", "AND");
        }
        paginator.addParam(p);
    }

    private void addParamVente(Boolean add) {
        ParametreRequete p;
        if (add != null) {
            p = new ParametreRequete("y.opVente", "opVente", add, "=", "AND");
        } else {
            p = new ParametreRequete("y.opVente", "opVente", null, "=", "AND");
        }
        paginator.addParam(p);
    }

    private void addParamTech(Boolean add) {
        ParametreRequete p;
        if (add != null) {
            p = new ParametreRequete("y.opTechnique", "opTechnique", add, "=", "AND");
        } else {
            p = new ParametreRequete("y.opTechnique", "opTechnique", null, "=", "AND");
        }
        paginator.addParam(p);
    }

    private void addParamReserv(Boolean add) {
        ParametreRequete p;
        if (add != null) {
            p = new ParametreRequete("y.opReserv", "opReserv", add, "=", "AND");
        } else {
            p = new ParametreRequete("y.opReserv", "opReserv", null, "=", "AND");
        }
        paginator.addParam(p);
    }

    private void addParamRetour(Boolean add) {
        ParametreRequete p;
        if (add != null) {
            p = new ParametreRequete("y.opRetour", "opRetour", add, "=", "AND");
        } else {
            p = new ParametreRequete("y.opRetour", "opRetour", null, "=", "AND");
        }
        paginator.addParam(p);
    }

    public void addParamDroit() {
        if (!autoriser("view_all_depot_societe")) {
            controlListAgence();
            paginator.addParam(new ParametreRequete("y.agence.id", "agences", listIdAgences, "IN", "AND"));
        }
        if (!autoriser("view_all_depot")) {
            if (currentUser != null) {
                //récupère les id des dépôts où je suis planifié            
                List<Long> l = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                if (l == null) {
                    l = new ArrayList<>();
                }
                //charge les dépôts où il en est responsable ou les dépôt ayant le même code d'accès que le user
                String query = "SELECT d.id FROM yvs_base_depots d INNER JOIN yvs_base_users_acces ua ON d.code_acces=ua.code WHERE ua.users=?";
                //id dépôt avec code d'accès
                List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(currentUser.getUsers().getId(), 1)});
                if (ids != null ? !ids.isEmpty() : false) {
                    l.addAll(ids);
                }
                //depôt ou le user est responsable
                if (currentUser.getUsers().getEmploye() != null || (ids != null ? !ids.isEmpty() : false)) {
                    ids = dao.loadNameQueries("YvsBaseDepots.findIdByResponsable", new String[]{"responsable"}, new Object[]{currentUser.getUsers().getEmploye()});
                    if (ids != null ? !ids.isEmpty() : false) {
                        l.addAll(ids);
                    }
                }
                l.add(0L);
                ParametreRequete p0 = new ParametreRequete(null, "responsable", "XXX", "=", "AND");
                p0.getOtherExpression().add(new ParametreRequete("y.responsable", "responsable", (currentUser != null) ? currentUser.getUsers().getEmploye() : null, "=", "OR"));
                p0.getOtherExpression().add(new ParametreRequete("y.id", "ids", l, " IN ", "OR"));
                paginator.addParam(p0);
            }
        }
    }

    public void printInventaire() {
        if (depot != null ? depot.getId() < 1 : true) {
            getErrorMessage("Vous devez indiquer le depot");
            return;
        }
        ManagedInventaire w = (ManagedInventaire) giveManagedBean(ManagedInventaire.class);
        if (w != null) {
            w.printInventaire(depot.getId(), 0, 0, "", 0, "V", new Date(), true, "", false, "code");
        }
    }

    public void printInventaire(YvsBaseEmplacementDepot y, boolean preparatoire) {
        if (depot != null ? depot.getId() < 1 : true) {
            getErrorMessage("Vous devez indiquer le depot");
            return;
        }
        if (y != null ? y.getId() < 1 : true) {
            getErrorMessage("Vous devez indiquer l' emplacement");
            return;
        }
        ManagedInventaire w = (ManagedInventaire) giveManagedBean(ManagedInventaire.class);
        if (w != null) {
            if (preparatoire) {
                w.printInventairePreparatoire(depot.getId(), y.getId(), 0, "", 0, "V", new Date(), true, "", false, "code");
            } else {
                w.printInventaire(depot.getId(), y.getId(), 0, "", 0, "V", new Date(), true, "", false, "code");
            }
        }
    }

    /**
     * Gestion des emplacements*
     */
    PaginatorResult<YvsBaseEmplacementDepot> paginEmp = new PaginatorResult<>();
    List<Long> idsDepot = new ArrayList();
    private String refArt, refFamille;

    public String getRefArt() {
        return refArt;
    }

    public void setRefArt(String refArt) {
        this.refArt = refArt;
    }

    public String getRefFamille() {
        return refFamille;
    }

    public void setRefFamille(String refFamille) {
        this.refFamille = refFamille;
    }

    public void loadAllEmplacement(boolean avancer, boolean init) {
        loadAllDepotActif(true);
        idsDepot.clear();
        for (YvsBaseDepots d : depots_all) {
            idsDepot.add(d.getId());
        }
        paginEmp.addParam(new ParametreRequete("y.depot.id", "depot", idsDepot, "IN", "AND"));
        emplacements = paginEmp.executeDynamicQuery("y", "y", "YvsBaseEmplacementDepot y JOIN FETCH y.depot", "y.depot.id, y.code", avancer, init, (int) imax, dao);
    }

    public void loadArticleEmplacement(SelectEvent ev) {
        if (ev.getObject() != null) {
            selectEmplacement = (YvsBaseEmplacementDepot) ev.getObject();
            loadAllArticleEmplacement(selectEmplacement, selectEmplacement.getDepot());
        }
    }

    public void loadAllArticleEmplacement(YvsBaseEmplacementDepot em, YvsBaseDepots d) {
        String query = "SELECT em.id,em.emplacement,ad.id, a.id, a.ref_art, a.designation,a.categorie,f.id, f.designation FROM yvs_base_article_depot ad "
                + "INNER JOIN yvs_base_articles a ON a.id=ad.article "
                + "INNER JOIN yvs_base_famille_article f ON f.id=a.famille "
                + "LEFT JOIN yvs_base_article_emplacement em ON (ad.id=em.article AND em.emplacement=?) "
                + "WHERE ad.depot=? and ad.actif IS true AND a.actif is true "
                + "ORDER BY em.emplacement, f.id, a.ref_art ";
        List<Object[]> result = dao.loadListBySqlQuery(query, new Options[]{new Options(em.getId(), 1), new Options(d.getId(), 2)});
        articles = buildEmplacement(result);
    }

    public void loadAFilterByArticle(String refart, String famille) {
        String art = (refart != null ? !refart.isEmpty() ? refart : null : null);
        String fart = (famille != null ? !famille.isEmpty() ? famille : null : null);
        String query = null;
        List<Object[]> result = null;
        if (art != null && fart != null) {
            System.err.println("cas 1...");
            query = "SELECT em.id,em.emplacement,ad.id, a.id, a.ref_art, a.designation,a.categorie,f.id, f.designation FROM yvs_base_article_depot ad "
                    + "INNER JOIN yvs_base_articles a ON a.id=ad.article "
                    + "INNER JOIN yvs_base_famille_article f ON f.id=a.famille "
                    + "LEFT JOIN yvs_base_article_emplacement em ON (ad.id=em.article AND em.emplacement=?) "
                    + "WHERE ad.depot=? and ad.actif IS true AND a.actif is true AND ((UPPER(a.ref_art) LIKE ? OR UPPER(a.designation) LIKE ?)) AND "
                    + "((UPPER(f.designation) LIKE ? OR UPPER(f.reference_famille) LIKE ?)) "
                    + "ORDER BY em.emplacement, f.id, a.ref_art ";
            result = dao.loadListBySqlQuery(query, new Options[]{new Options(selectEmplacement.getId(), 1), new Options(selectEmplacement.getDepot().getId(), 2), new Options(refart.trim().toUpperCase(), 3), new Options(refart.trim().toUpperCase(), 4),
                new Options(famille.trim().toUpperCase(), 5), new Options(famille.trim().toUpperCase(), 6)});
        } else if (art != null) {
            query = "SELECT em.id,em.emplacement,ad.id, a.id, a.ref_art, a.designation,a.categorie,f.id, f.designation FROM yvs_base_article_depot ad "
                    + "INNER JOIN yvs_base_articles a ON a.id=ad.article "
                    + "INNER JOIN yvs_base_famille_article f ON f.id=a.famille "
                    + "LEFT JOIN yvs_base_article_emplacement em ON (ad.id=em.article AND em.emplacement=?) "
                    + "WHERE ad.depot=? and ad.actif IS true AND a.actif is true AND (UPPER(a.ref_art) LIKE ? OR UPPER(a.designation) LIKE ?) "
                    + "ORDER BY em.emplacement, f.id, a.ref_art ";
            result = dao.loadListBySqlQuery(query, new Options[]{new Options(selectEmplacement.getId(), 1), new Options(selectEmplacement.getDepot().getId(), 2), new Options(refart.trim().toUpperCase(), 3), new Options(refart.trim().toUpperCase(), 4)});

        } else if (fart != null) {
            System.err.println("cas 3...");
            query = "SELECT em.id,em.emplacement,ad.id, a.id, a.ref_art, a.designation,a.categorie,f.id, f.designation FROM yvs_base_article_depot ad "
                    + "INNER JOIN yvs_base_articles a ON a.id=ad.article "
                    + "INNER JOIN yvs_base_famille_article f ON f.id=a.famille "
                    + "LEFT JOIN yvs_base_article_emplacement em ON (ad.id=em.article AND em.emplacement=?) "
                    + "WHERE ad.depot=? and ad.actif IS true AND a.actif is true AND ((UPPER(f.designation) LIKE ? OR UPPER(f.reference_famille) LIKE ?)) "
                    + "ORDER BY em.emplacement, f.id, a.ref_art ";
            result = dao.loadListBySqlQuery(query, new Options[]{new Options(selectEmplacement.getId(), 1), new Options(selectEmplacement.getDepot().getId(), 2), new Options(famille.trim().toUpperCase(), 3), new Options(famille.trim().toUpperCase(), 4)});
        } else {
            if (selectEmplacement != null) {
                loadAllArticleEmplacement(selectEmplacement, selectEmplacement.getDepot());
            }
        }
        articles = buildEmplacement(result);
    }

    private List<YvsBaseArticleEmplacement> buildEmplacement(List<Object[]> list) {
        List<YvsBaseArticleEmplacement> re = new ArrayList<>();
        if (list != null) {
            YvsBaseArticleEmplacement y;
            long id = -10000;
            for (Object[] o : list) {
                y = new YvsBaseArticleEmplacement();
                y.setId((o[0] != null) ? (Long) o[0] : id++);
                y.setEmplacement(new YvsBaseEmplacementDepot((o[1] != null) ? (Long) o[1] : null));
                y.setArticle(new YvsBaseArticleDepot((Long) o[2]));
                y.getArticle().setArticle(new YvsBaseArticles((Long) o[3], (String) o[4], (String) o[5]));
                y.getArticle().getArticle().setCategorie((String) o[6]);
                y.getArticle().getArticle().setFamille(new YvsBaseFamilleArticle((Long) o[7], (String) o[8], (String) o[8]));
                re.add(y);
            }
        }
        return re;
    }

    public void toogleActiveEmplacement(YvsBaseEmplacementDepot emp) {
        if (emp != null) {
            emp.setActif(!emp.getActif());
            emp.setAuthor(currentUser);
            emp.setDateUpdate(new Date());
            dao.update(emp);
        }
    }

    public void applyArticleEmplacement(boolean add) {
        for (YvsBaseArticleEmplacement a : selectionsArticles) {
            if (add) {
                if (a.getId() <= 0) {
                    addNewArticleEmplacement(a);
                }
            } else {
                removeArticleEmplacement(a);
            }
        }
    }

    @Override
    public void updateBean() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doNothing() {
    }

}
