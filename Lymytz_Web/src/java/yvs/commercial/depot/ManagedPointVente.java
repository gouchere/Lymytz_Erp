/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.depot;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.base.produits.ManagedArticles;
import yvs.base.tiers.Tiers;
import yvs.production.UtilProd;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.UtilCom;
import yvs.commercial.client.Client;
import yvs.commercial.client.ManagedClient;
import yvs.commercial.creneau.Creneau;
import yvs.commercial.creneau.ManagedCreneauEmploye;
import yvs.commercial.creneau.TypeCreneau;
import yvs.commercial.rrr.GrilleRabais;
import yvs.commercial.rrr.Rabais;
import yvs.entity.base.YvsBaseArticlePoint;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseConditionnementPoint;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.base.YvsBasePointVenteDepot;
import yvs.entity.base.YvsBasePointVenteUser;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.YvsComCommercialPoint;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.creneau.YvsComCreneauPoint;
import yvs.entity.commercial.rrr.YvsComGrilleRabais;
import yvs.entity.commercial.rrr.YvsComRabais;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsers;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.bean.Employe;
import yvs.grh.presence.TrancheHoraire;
import yvs.init.Initialisation;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.dico.ManagedDico;
import yvs.parametrage.entrepot.Depots;
import yvs.parametrage.societe.ManagedSociete;
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
public class ManagedPointVente extends ManagedCommercial<PointVente, YvsBasePointVente> implements Serializable {

    private PointVente pointVente = new PointVente();
    private List<YvsBasePointVente> pointsvente, pointsvente_all;
    private YvsBasePointVente selectPoint;

    private PointVenteDepot liaison = new PointVenteDepot();
    private List<YvsBasePointVenteDepot> liaisons;
    private YvsBasePointVenteDepot selectLiaison;

    private List<YvsBaseConditionnementPoint> articlesSelect;
    private ArticleDepot article = new ArticleDepot();
    private YvsBaseConditionnementPoint selectArticle = new YvsBaseConditionnementPoint();
    private PaginatorResult<YvsBaseConditionnementPoint> pa = new PaginatorResult<>();
    private long max = 10;
    private ConditionnementPoint conditionnement = new ConditionnementPoint();

    private Rabais rabais = new Rabais();
    private YvsComRabais selectRabais;
    private List<YvsComRabais> listRabais;
    private GrilleRabais grille = new GrilleRabais();

    private List<String> criteres;
    private TypeCreneau type = new TypeCreneau();
    private List<YvsGrhTrancheHoraire> types;
    private List<YvsJoursOuvres> jours;
    private Creneau creneau = new Creneau();
    private List<YvsComCreneauPoint> creneaux;
    private YvsComCreneauPoint selectCreneau;

    private List<YvsBaseDepots> depots;

    private String tabIds, tabIds_liaison, tabIds_article, tabIds_creneau;
    private boolean listArt;

    private String categorie;
    private long famille, agenceFind;

    private Boolean actifSearch;
    private String codeSearch, typeSearch;
    private long agenceSearch, lieuSearch;

    public ManagedPointVente() {
        pointsvente = new ArrayList<>();
        pointsvente_all = new ArrayList<>();
        articlesSelect = new ArrayList<>();
        liaisons = new ArrayList<>();
        depots = new ArrayList<>();
        types = new ArrayList<>();
        jours = new ArrayList<>();
        creneaux = new ArrayList<>();
        criteres = new ArrayList<>();
        listRabais = new ArrayList<>();
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public String getCodeSearch() {
        return codeSearch;
    }

    public void setCodeSearch(String codeSearch) {
        this.codeSearch = codeSearch;
    }

    public long getAgenceSearch() {
        return agenceSearch;
    }

    public void setAgenceSearch(long agenceSearch) {
        this.agenceSearch = agenceSearch;
    }

    public long getLieuSearch() {
        return lieuSearch;
    }

    public void setLieuSearch(long lieuSearch) {
        this.lieuSearch = lieuSearch;
    }

    public List<YvsBasePointVente> getPointsvente_all() {
        return pointsvente_all;
    }

    public void setPointsvente_all(List<YvsBasePointVente> pointsvente_all) {
        this.pointsvente_all = pointsvente_all;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public long getFamille() {
        return famille;
    }

    public void setFamille(long famille) {
        this.famille = famille;
    }

    public long getAgenceFind() {
        return agenceFind;
    }

    public void setAgenceFind(long agenceFind) {
        this.agenceFind = agenceFind;
    }

    public Rabais getRabais() {
        return rabais;
    }

    public void setRabais(Rabais rabais) {
        this.rabais = rabais;
    }

    public YvsComRabais getSelectRabais() {
        return selectRabais;
    }

    public void setSelectRabais(YvsComRabais selectRabais) {
        this.selectRabais = selectRabais;
    }

    public GrilleRabais getGrille() {
        return grille;
    }

    public void setGrille(GrilleRabais grille) {
        this.grille = grille;
    }

    public ConditionnementPoint getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(ConditionnementPoint conditionnement) {
        this.conditionnement = conditionnement;
    }

    public List<YvsBaseConditionnementPoint> getArticlesSelect() {
        return articlesSelect;
    }

    public void setArticlesSelect(List<YvsBaseConditionnementPoint> articlesSelect) {
        this.articlesSelect = articlesSelect;
    }

    public String getTabIds_creneau() {
        return tabIds_creneau;
    }

    public void setTabIds_creneau(String tabIds_creneau) {
        this.tabIds_creneau = tabIds_creneau;
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

    public List<YvsGrhTrancheHoraire> getTypes() {
        return types;
    }

    public void setTypes(List<YvsGrhTrancheHoraire> types) {
        this.types = types;
    }

    public List<YvsJoursOuvres> getJours() {
        return jours;
    }

    public void setJours(List<YvsJoursOuvres> jours) {
        this.jours = jours;
    }

    public Creneau getCreneau() {
        return creneau;
    }

    public void setCreneau(Creneau creneau) {
        this.creneau = creneau;
    }

    public List<YvsComCreneauPoint> getCreneaux() {
        return creneaux;
    }

    public void setCreneaux(List<YvsComCreneauPoint> creneaux) {
        this.creneaux = creneaux;
    }

    public YvsComCreneauPoint getSelectCreneau() {
        return selectCreneau;
    }

    public void setSelectCreneau(YvsComCreneauPoint selectCreneau) {
        this.selectCreneau = selectCreneau;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public PaginatorResult<YvsBaseConditionnementPoint> getPa() {
        return pa;
    }

    public void setPa(PaginatorResult<YvsBaseConditionnementPoint> pa) {
        this.pa = pa;
    }

    public boolean isListArt() {
        return listArt;
    }

    public void setListArt(boolean listArt) {
        this.listArt = listArt;
    }

    public ArticleDepot getArticle() {
        return article;
    }

    public void setArticle(ArticleDepot article) {
        this.article = article;
    }

    public YvsBaseConditionnementPoint getSelectArticle() {
        return selectArticle;
    }

    public void setSelectArticle(YvsBaseConditionnementPoint selectArticle) {
        this.selectArticle = selectArticle;
    }

    public String getTabIds_article() {
        return tabIds_article;
    }

    public void setTabIds_article(String tabIds_article) {
        this.tabIds_article = tabIds_article;
    }

    public YvsBasePointVente getSelectPoint() {
        return selectPoint;
    }

    public void setSelectPoint(YvsBasePointVente selectPoint) {
        this.selectPoint = selectPoint;
    }

    public YvsBasePointVenteDepot getSelectLiaison() {
        return selectLiaison;
    }

    public void setSelectLiaison(YvsBasePointVenteDepot selectLiaison) {
        this.selectLiaison = selectLiaison;
    }

    public List<YvsBasePointVenteDepot> getLiaisons() {
        return liaisons;
    }

    public void setLiaisons(List<YvsBasePointVenteDepot> liaisons) {
        this.liaisons = liaisons;
    }

    public PointVente getPointVente() {
        return pointVente;
    }

    public void setPointVente(PointVente pointVente) {
        this.pointVente = pointVente;
    }

    public List<YvsBasePointVente> getPointsvente() {
        return pointsvente;
    }

    public void setPointsvente(List<YvsBasePointVente> pointsvente) {
        this.pointsvente = pointsvente;
    }

    public PointVenteDepot getLiaison() {
        return liaison;
    }

    public void setLiaison(PointVenteDepot liaison) {
        this.liaison = liaison;
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

    public String getTabIds_liaison() {
        return tabIds_liaison;
    }

    public void setTabIds_liaison(String tabIds_liaison) {
        this.tabIds_liaison = tabIds_liaison;
    }

    public List<YvsComRabais> getListRabais() {
        return listRabais;
    }

    public void setListRabais(List<YvsComRabais> listRabais) {
        this.listRabais = listRabais;
    }

    @Override
    public void loadAll() {
        if (pointVente.getParent() == null) {
            pointVente.setParent(new PointVente());
        }
        loadAllDepot();
        loadAllPointVente(true, true);
        loadAllJours();
        loadAllTypes(null);
        loadCritere();
    }

    public void load() {

    }

    public void loadAllPointVente(boolean avance, boolean init) {
        ParametreRequete p = new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
        paginator.addParam(p);
        if (!autoriser("pv_view_all_societe")) {
            p = new ParametreRequete("y.agence", "agence", currentAgence, "=", "AND");
            paginator.addParam(p);
        }
//        pointsvente = paginator.executeDynamicQuery("YvsBasePointVente", "y.libelle", avance, init, (int) imax, dao);
        pointsvente = paginator.executeDynamicQuery("y", "y", "YvsBasePointVente y JOIN FETCH y.agence", "y.libelle", avance, init, (int) imax, dao);
    }

    public void addParamActif(Boolean actif) {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", actif);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        loadAllPointVente(true, true);
    }

    public void loadAllPointVente() {
        if (!autoriser("pv_view_all_societe")) {
            loadAllPointVente(currentAgence.getId());
        } else {
            loadAllPointVente(0);
        }
    }

    public void loadAllPointVente(long agence) {
        if (agence > 0) {
            pointsvente = dao.loadNameQueries("YvsBasePointVente.findByAgence", new String[]{"agence"}, new Object[]{new YvsAgences(agence)});
        } else {
            pointsvente = dao.loadNameQueries("YvsBasePointVente.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
    }

    public void loadPointVenteByMax(int imax) {
        paginator.clear();
        this.imax = imax;
        loadPointVenteByDroit(true, true);
    }

    public void loadPointVenteByDroit(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND"));
        paginator.addParam(new ParametreRequete("y.actif", "actif", true, "=", "AND"));
        ParametreRequete p;
        int action = buildDocByDroit(Constantes.TYPE_FV);
        switch (action) {
            case 1:  ////charge les points de vente de la société
                p = new ParametreRequete("y.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
                paginator.addParam(p);
                break;
            case 2: ////charge les points de vente de l'agence
                p = new ParametreRequete("y.agence", "agence", currentUser.getAgence(), "=", "AND");
                paginator.addParam(p);
                break;
            default:    //charge les points de vente où je suis planifié
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdPointByUsers_", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                if (ids.isEmpty()) {
                    ids.add(-1L);
                }
                p = new ParametreRequete(null, "id", "XX", " IN ", "AND");
                p.getOtherExpression().add(new ParametreRequete("y.id", "ids", ids, " IN ", "OR"));
                p.getOtherExpression().add(new ParametreRequete("y.responsable", "responsable", currentUser.getUsers().getEmploye(), "=", "AND"));
                paginator.addParam(p);
                break;
        }
        pointsvente = paginator.executeDynamicQuery("YvsBasePointVente", "y.code", avance, init, (int) imax, dao);
    }

    public void loadPointVenteByDroit(YvsUsers users) {
        ParametreRequete p;
        p = new ParametreRequete("y.actif", "actif", true, "=", "AND");
        paginator.addParam(p);
        switch (buildDocByDroit(Constantes.TYPE_FV)) {
            case 1:  ////charge les points de vente de la société
                p = new ParametreRequete("y.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
                paginator.addParam(p);
                break;
            case 2: ////charge les points de vente de l'agence
                p = new ParametreRequete("y.agence", "agence", currentUser.getAgence(), "=", "AND");
                paginator.addParam(p);
                break;
            case 3:    //charge les points de vente où je suis planifié
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdPointByUsers_", new String[]{"users"}, new Object[]{users});
                if (ids.isEmpty()) {
                    ids.add(-1L);
                }
                p = new ParametreRequete(null, "id", "XX", " IN ", "AND");
                p.getOtherExpression().add(new ParametreRequete("y.id", "ids", ids, " IN ", "OR"));
                p.getOtherExpression().add(new ParametreRequete("y.responsable", "responsable", users.getEmploye(), "=", "AND"));
                paginator.addParam(p);
                break;
        }
        pointsvente = paginator.executeDynamicQuery("YvsBasePointVente", "y.code", true, true, (int) imax, dao);
    }

    public void onSelectAgenceForAction(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsAgences a = (YvsAgences) ev.getObject();
            agenceFind = a.getId();
            loadPointsByAgence(agenceFind, true, false);
        }
    }

    public void onChooseAgenceForAction() {
        loadPointsByAgence(agenceFind, true, false);
    }

    public void onSelectAgenceForFind(long agence) {
        loadPointsByAgence(agence, false, true);
    }

    public void loadPointsByAgence() {
        if (agenceFind < 1) {
            agenceFind = currentAgence.getId();
        }
        loadPointsByAgence(agenceFind, true, true);
    }

    public void loadPointsByAgence(long agence, boolean forAction, boolean forFind) {
        try {
            if (forAction || forFind) {
                if (forFind) {
                    pointsvente_all.clear();
                }
                if (forAction) {
                    pointsvente.clear();
                }
                if (autoriser("fv_view_only_doc_agence")) {
                    List<YvsBasePointVente> list = dao.loadNameQueries("YvsBasePointVente.findByAgenceActif", new String[]{"agence", "actif"}, new Object[]{new YvsAgences(agence), true});
                    if (forFind) {
                        pointsvente_all.addAll(list);
                    }
                    if (forAction) {
                        pointsvente.addAll(list);
                    }
                } else {
                    //charge les points où l'utilisateurs est planifier
                    List<YvsBasePointVente> list = dao.loadNameQueries("YvsComCreneauHoraireUsers.findPointByUsersAgence", new String[]{"agence", "users", "responsable", "date", "hier"}, new Object[]{new YvsAgences(agence), currentUser.getUsers(), currentUser.getUsers().getEmploye(), new Date(), Constantes.getPreviewDate(new Date())});
                    if (forAction) {
                        pointsvente.addAll(list);
                    }
                    if (forFind) {
                        //charge les points où il a accès
                        pointsvente_all = dao.loadNameQueries("YvsBasePointVenteUser.findPointByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                        for (YvsBasePointVente d : list) {
                            if (!pointsvente_all.contains(d)) {
                                pointsvente_all.add(d);
                            }
                        }
                    }
                    //charge les points où il en est responsable   
                    if (currentUser.getUsers().getEmploye() != null ? currentUser.getUsers().getEmploye().getId() > 0 : false) {
                        list = dao.loadNameQueries("YvsBasePointVente.findByResponsableAgence", new String[]{"agence", "responsable"}, new Object[]{new YvsAgences(agence), currentUser.getUsers().getEmploye()});
                        for (YvsBasePointVente p : list) {
                            if (forAction ? !pointsvente.contains(p) : false) {
                                pointsvente.add(p);
                            }
                            if (forFind ? !pointsvente_all.contains(p) : false) {
                                pointsvente_all.add(p);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            getException("loadPointsByAgence", ex);
        }
    }

    public void loadArticle(boolean avance, boolean init) {
        if (selectPoint != null ? selectPoint.getId() > 0 : false) {
            ParametreRequete p = new ParametreRequete("y.article.point", "point", selectPoint);
            p.setOperation("=");
            p.setPredicat("AND");
            pa.addParam(p);
            pointVente.setArticles(pa.executeDynamicQuery("YvsBaseConditionnementPoint", "y.article.article.designation, y.article.article.refArt", avance, init, (int) max, dao));
        } else {
            getErrorMessage("Vous devez selectionner un point de vente");
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

    private void loadCreneaux(YvsBasePointVente y) {
        creneaux.clear();
        if (y != null ? y.getId() > 0 : false) {
            champ = new String[]{"point"};
            val = new Object[]{y};
            creneaux = dao.loadNameQueries("YvsComCreneauPoint.findByDepot", champ, val);
        }
        update("data_creneau");
    }

    public void loadCurrentCreneaux(YvsBasePointVente y, Date date) {
        creneaux = loadCreneaux(y, date);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllPointVente(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllPointVente(true, true);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbResult())) {
            setOffset(0);
        }
        List<YvsBasePointVente> re = paginator.parcoursDynamicData("YvsBasePointVente", "y", "y.code", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void _choosePaginator(ValueChangeEvent ev) {
        if ((ev != null) ? ev.getNewValue() != null : false) {
            long v = (long) ev.getNewValue();
            max = v;
            loadArticle(true, true);
        }
    }

    public void loadAllDepot() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        depots = dao.loadNameQueries("YvsBaseDepots.findAllActif", champ, val);
    }

    public void loadAllLiaison(YvsBasePointVente y) {
        liaisons.clear();
        YvsBasePointVenteDepot pd;
        long id = -1;
        for (YvsBaseDepots d : depots) {
            champ = new String[]{"pointVente", "depot"};
            val = new Object[]{y, d};
            pd = (YvsBasePointVenteDepot) dao.loadOneByNameQueries("YvsBasePointVenteDepot.findByOne", champ, val);
            if (pd != null ? (pd.getId() != null ? pd.getId() < 1 : true) : true) {
                pd = new YvsBasePointVenteDepot(id--);
                pd.setDepot(d);
                pd.setAuthor(currentUser);
                pd.setNew_(false);
                pd.setPointVente(y);
                pd.setPrincipal(false);
                pd.setActif(false);
            }
            liaisons.add(pd);
        }
        update("data_point_vente_depot");
    }

    public void loadAllCommerciaux(YvsBasePointVente y) {
        YvsComCommercialPoint pd;
        List<YvsComComerciale> list = dao.loadNameQueries("YvsComComerciale.findBySociete", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        champ = new String[]{"point", "commercial"};
        long id = -1;
        pointVente.getCommerciaux().clear();
        for (YvsComComerciale d : list) {
            val = new Object[]{y, d};
            pd = (YvsComCommercialPoint) dao.loadOneByNameQueries("YvsComCommercialPoint.findOne", champ, val);
            if (pd != null ? (pd.getId() != null ? pd.getId() < 1 : true) : true) {
                pd = new YvsComCommercialPoint(id--);
                pd.setCommercial(d);
                pd.setAuthor(currentUser);
                pd.setNew_(false);
                pd.setPoint(y);
            }
            pointVente.getCommerciaux().add(pd);
        }
        update("data_point_vente_commerciaux");
    }

    public void init(boolean next) {
        loadAllPointVente(next, false);
    }

    public void initArticle(boolean next) {
        loadArticle(next, false);
    }

    public YvsBaseArticlePoint buildArticlePoint(ArticleDepot y) {
        YvsBaseArticlePoint a = UtilCom.buildArticlePoint(y, currentUser, selectPoint);
        if (a != null) {
            if (!article.getArticle().getConditionnements().isEmpty()) {
                int idx = article.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(y.getConditionnement().getId()));
                if (idx >= 0) {
                    a.setConditionementVente(article.getArticle().getConditionnements().get(idx));
                } else {
                    a.setConditionementVente(new YvsBaseConditionnement(y.getConditionnement().getId()));
                }
            } else {
                a.setConditionementVente(new YvsBaseConditionnement(y.getConditionnement().getId()));
            }
            a.getArticle().setConditionnements(article.getArticle().getConditionnements());
        }
        return a;
    }

    @Override
    public PointVente recopieView() {
        pointVente.setAgence(pointVente.isUpdate() ? pointVente.getAgence() : new Agence(currentAgence.getId()));
        if (pointVente.getParent() != null ? pointVente.getParent().getId() > 0 : false) {
            int index = pointsvente.indexOf(new YvsBasePointVente(pointVente.getParent().getId()));
            if (index > -1) {
                YvsBasePointVente parent = pointsvente.get(index);
                pointVente.setParent(new PointVente(parent.getId(), parent.getLibelle()));
            }
        }
        pointVente.setNew_(true);
        return pointVente;
    }

    public ArticleDepot recopieViewArticle() {
        article.setDesignation(article.getArticle().getDesignation());
        article.setRefArt(article.getArticle().getRefArt());
        article.setNaturePrixMin(article.isPrixMinIsTaux() ? Constantes.NATURE_TAUX : Constantes.NATURE_MTANT);
        return article;
    }

    public PointVenteDepot recopieViewPoint(PointVente point) {
        liaison.setPointVente(point);
        liaison.setNew_(true);
        return liaison;
    }

    public Creneau recopieViewCreneau() {
        creneau.setNew_(true);
        return creneau;
    }

    public ConditionnementPoint recopieViewConditionnement(ArticleDepot article) {
        conditionnement.setArticle(article);
        return conditionnement;
    }

    public GrilleRabais recopieViewGrille() {
        if (grille.isUnique()) {
            grille.setMontantMinimal(0);
            grille.setMontantMaximal(Double.MAX_VALUE);
        }
        grille.setNew_(true);
        return grille;
    }

    @Override
    public boolean controleFiche(PointVente bean) {
        if (bean.getCode() == null || bean.getCode().trim().equals("")) {
            getErrorMessage("Vous devez entrer un code!");
            return false;
        }
        if (bean.getLibelle() == null || bean.getLibelle().trim().equals("")) {
            getErrorMessage("Vous devez entrer un libelle!");
            return false;
        }
        if (bean.getParent() != null ? bean.getParent().getId() > 0 : false) {
            if (bean.getId() == bean.getParent().getId()) {
                getErrorMessage("Un point de vente ne peut pas etre son propre parent!");
                return false;
            }
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

    public boolean controleFicheArticle(ArticleDepot bean) {
        if (selectPoint != null ? selectPoint.getId() < 1 : true) {
            getErrorMessage("Vous devez enregistrer le point de vente");
            return false;
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        if ((bean.getConditionnement() != null) ? bean.getConditionnement().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le conditionnement de vente dans ce point");
            return false;
        }
        return true;
    }

    public boolean controleFichePoint(PointVenteDepot bean) {
        if (!bean.getPointVente().isUpdate()) {
            getErrorMessage("Vous devez dabord enregistrer un point de vente");
            return false;
        }
        if ((bean.getDepot() != null) ? bean.getDepot().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner un dépot");
            return false;
        }
        for (YvsBasePointVenteDepot p : liaisons) {
            if (!bean.isUpdate()) {
                if (p.getDepot().getDesignation().equals(bean.getDepot().getDesignation())) {
                    getErrorMessage("Vous avez deja rattaché ce dépot");
                    return false;
                }
            }
            if (bean.isPrincipal() && p.getPrincipal() && (!p.getDepot().getDesignation().equals(bean.getDepot().getDesignation()))) {
                getErrorMessage("Vous avez deja attribué une liaison principal");
                return false;
            }
        }
        return true;
    }

    public boolean controleFicheCreneau(Creneau bean) {
        if ((selectPoint != null) ? selectPoint.getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le point de vente");
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

    public boolean controleFiche(ConditionnementPoint bean) {
        if (bean.getPuv() < 1) {
            getErrorMessage("Vous devez enregistrer le prix de vente");
            return false;
        }
        if (bean.getConditionnement() != null ? bean.getConditionnement().getId() < 1 : true) {
            getErrorMessage("Vous devez enregistrer le conditionnement");
            return false;
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        champ = new String[]{"article", "unite"};
        val = new Object[]{new YvsBaseArticlePoint(bean.getArticle().getId()), new YvsBaseConditionnement(bean.getConditionnement().getId())};
        nameQueri = "YvsBaseConditionnementPoint.findOne";
        YvsBaseConditionnementPoint a = (YvsBaseConditionnementPoint) dao.loadOneByNameQueries(nameQueri, champ, val);
        if (a != null ? (a.getId() > 0 ? !a.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez deja associé cet element!");
            return false;
        }
        return true;
    }

    public boolean controleFiche(Rabais bean) {
        if ((selectArticle != null) ? selectArticle.getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        if (bean.getConditionnement() != null ? bean.getConditionnement().getId() < 1 : true) {
            getErrorMessage("Vous devez enregistrer le conditionnement");
            return false;
        }
        //vérifie l'existence d'un plan de rabais dans une période qui se chevauche
        champ = new String[]{"article", "unite", "point", "debut", "fin"};
        val = new Object[]{selectArticle.getArticle(), selectArticle.getConditionnement(), selectArticle.getArticle().getPoint(), bean.getDateDebut(), bean.getDateFin()};
        YvsComRabais r = (YvsComRabais) dao.loadOneByNameQueries("YvsComRabais.findOtherByPointArticleUnit", champ, val);
        if (r != null) {
            if (r.getId() != bean.getId()) {
                getErrorMessage("Vous avez déjà planifié un rabais pour cet article dans une période semblable !");
                return false;
            }
        }
        return true;
    }

    public boolean controleFicheGrille(GrilleRabais bean) {
        if (selectRabais != null ? selectRabais.getId() < 1 : true) {
            getErrorMessage("Vous devez dabord enregistrer la remise");
        }
        if (bean.getMontantMaximal() > 0 ? bean.getMontantMaximal() < bean.getMontantMinimal() : false) {
            getErrorMessage("le montant minimal ne peut pas etre superieur au montant maximal");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(PointVente bean) {
        cloneObject(pointVente, bean);
    }

    public void populateViewArticle(ArticleDepot bean) {
        cloneObject(article, bean);
    }

    public void populateViewPoint(PointVenteDepot bean) {
        cloneObject(liaison, bean);
    }

    public void populateViewCreneau(Creneau bean) {
        cloneObject(creneau, bean);
    }

    public void populateView(ConditionnementPoint bean) {
        cloneObject(conditionnement, bean);
    }

    @Override
    public void resetFiche() {
        pointVente = new PointVente();
        pointVente.setParent(new PointVente());
        pointVente.setVenteOnline(false);
        pointVente.setAgence(new Agence());
        pointVente.setSecteur(new Dictionnaire());
        pointVente.setResponsable(new Employe());
        pointVente.setLiaisons(new ArrayList<PointVenteDepot>());
        pointVente.setArticles(new ArrayList<YvsBaseConditionnementPoint>());
        pointVente.setCommerciaux(new ArrayList<YvsComCommercialPoint>());
        pointVente.setUtilisateurs(new ArrayList<YvsUsers>());
        tabIds = "";
        selectPoint = new YvsBasePointVente();
        liaisons.clear();
        articlesSelect.clear();
        creneaux.clear();
        resetFichePoint();
        resetFicheArticle();
        resetFicheCreneau();
        update("blog_form_point_vente");
    }

    public void resetFicheArticle() {
        article = new ArticleDepot();
        tabIds_article = "";
        selectArticle = null;
        listArt = false;
        resetFicheConditionnement();
        resetFicheRabais();
    }

    public void resetFichePoint() {
        liaison = new PointVenteDepot();
        selectLiaison = null;
        tabIds_liaison = "";
    }

    public void resetFicheCreneau() {
        creneau = new Creneau();
        tabIds_creneau = "";
        selectCreneau = null;
        loadAllTypes(null);
    }

    public void resetFicheConditionnement() {
        conditionnement = new ConditionnementPoint();
    }

    public void resetFicheRabais() {
        rabais = new Rabais();
        selectRabais = new YvsComRabais();
        resetFicheGrilleRabais();
        update("form_rabais_article_point");
    }

    public void resetFicheGrilleRabais() {
        grille = new GrilleRabais();
        update("form_grille_rabais");
    }

    @Override
    public boolean saveNew() {
        String action = pointVente.isUpdate() ? "Modification" : "Insertion";
        try {
            PointVente bean = recopieView();
            if (controleFiche(bean)) {
                selectPoint = UtilCom.buildPointVente(bean, currentUser, currentUser.getAgence());
                if (!bean.isUpdate()) {
                    selectPoint.setId(null);
                    selectPoint = (YvsBasePointVente) dao.save1(selectPoint);
                    pointVente.setId(selectPoint.getId());
                    pointsvente.add(0, selectPoint);
                    loadAllLiaison(selectPoint);
                    loadAllCommerciaux(selectPoint);
                } else {
                    dao.update(selectPoint);
                    pointsvente.set(pointsvente.indexOf(selectPoint), selectPoint);
                }
                pointVente.setUpdate(true);
                succes();
                actionOpenOrResetAfter(this);
                update("data_point_vente");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public YvsBaseConditionnementPoint saveNewArticle(boolean reset) {
        String action = article.isUpdate() ? "Modification" : "Insertion";
        YvsBaseConditionnementPoint temp = null;
        try {
            ArticleDepot bean = recopieViewArticle();
            if (controleFicheArticle(bean)) {
                YvsBaseArticlePoint entity = buildArticlePoint(bean);
                entity.setDateUpdate(new Date());
                entity.setAuthor(currentUser);
                //Cherche l'article dans conditionnement point
                temp = (YvsBaseConditionnementPoint) dao.loadOneByNameQueries("YvsBaseConditionnementPoint.findArticlePointUnite",
                        new String[]{"article", "point", "unite"}, new Object[]{entity.getArticle(), entity.getPoint(), new YvsBaseConditionnement(bean.getConditionnement().getId())});
                if (temp != null ? temp.getId() < 1 : true) {
                    // verifie la relation Article_point
                    YvsBaseArticlePoint tempArt = (YvsBaseArticlePoint) dao.loadOneByNameQueries("YvsBaseArticlePoint.findByArticlePoint", new String[]{"article", "point"}, new Object[]{entity.getArticle(), entity.getPoint()});
                    if (tempArt == null) {
                        // Ajoute la relation article_point et ensuite la relation conditionnement_point
                        entity.setId(null);
                        entity.setActif(true);
                        entity = (YvsBaseArticlePoint) dao.save1(entity);
                    } else {
                        //Modifie article_point et ajoute conditionnement_point
                        entity.setId(tempArt.getId());
                        dao.update(entity);
                    }
                    //ajoute conditionnement_poit
                    temp = new YvsBaseConditionnementPoint(entity, entity.getConditionementVente());
                    temp.setPuv(bean.getPuv());
                    temp.setRemise(bean.getRemise());
                    temp.setPrixMin(bean.getPuvMin());
                    temp.setNaturePrixMin(bean.getNaturePrixMin());
                    temp.setNatureRemise(bean.getNatureRemise());
                    temp.setDateUpdate(new Date());
                    temp.setDateSave(new Date());
                    temp.setAuthor(currentUser);
                    temp.setAvanceCommance(bean.getAvanceCommance());
                    temp.setTauxPua(bean.getTauxPua());
                    temp.setProportionPua(bean.isProportionPua());
                    dao.save1(temp);
                } else {
                    entity.setAuthor(currentUser);
                    dao.update(entity);
                    //update la table cond_point
                    temp.setAuthor(currentUser);
                    temp.setDateUpdate(new Date());
                    temp.setDateSave((temp != null) ? temp.getDateSave() : new Date());
                    temp.setNaturePrixMin(bean.getNaturePrixMin());
                    temp.setNatureRemise(bean.getNatureRemise());
                    temp.setPuv(bean.getPuv());
                    temp.setRemise(bean.getRemise());
                    temp.setPrixMin(bean.getPuvMin());
                    temp.setAvanceCommance(bean.getAvanceCommance());
                    temp.setTauxPua(bean.getTauxPua());
                    temp.setProportionPua(bean.isProportionPua());
                    dao.update(temp);
                }
                int idx = pointVente.getArticles().indexOf(temp);
                if (idx > -1) {
                    pointVente.getArticles().set(idx, temp);
                } else {
                    pointVente.getArticles().add(0, temp);
                }
                succes();
                if (reset) {
                    resetFicheArticle();
                    update("data_article_point");
                }
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return null;
        }
        return temp;
    }

    public boolean saveNewPoint() {
        String action = liaison.isUpdate() ? "Modification" : "Insertion";
        try {
            PointVenteDepot bean = recopieViewPoint(pointVente);
            if (controleFichePoint(bean)) {
                YvsBasePointVenteDepot y = UtilCom.buildPointVenteDepot(bean, currentUser, selectPoint);
                if (!bean.isUpdate()) {
                    y.setId(null);
                    y = (YvsBasePointVenteDepot) dao.save1(y);
                    liaisons.add(y);
                } else {
                    dao.update(y);
                    liaisons.set(liaisons.indexOf(y), y);
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

    public boolean saveNewPoint(YvsBasePointVenteDepot y) {
        try {
            y.setNew_(!y.isNew_());
            int pos = liaisons.indexOf(y);
            y.setActif(true);
            if (y.getId() <= 0) {
                y.setId(null);
                y = (YvsBasePointVenteDepot) dao.save1(y);
            } else {
                dao.update(y);
            }
            liaisons.set(pos, y);
            succes();
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            getException("Lymytz Error>>", ex);
            return false;
        }
        return true;
    }

    public boolean saveNewCommercial(YvsComCommercialPoint y) {
        try {
            y.setNew_(!y.isNew_());
            int pos = pointVente.getCommerciaux().indexOf(y);
            if (y.getId() <= 0) {
                y.setId(null);
                y = (YvsComCommercialPoint) dao.save1(y);
            } else {
                dao.delete(y);
                y.setId(-(y.getId()));
            }
            pointVente.getCommerciaux().set(pos, y);
            succes();
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            getException("Lymytz Error>>", ex);
            return false;
        }
        return true;
    }

    public boolean saveNewCreneau() {
        String action = creneau.getId() > 0 ? "Modification" : "Insertion";
        try {
            Creneau bean = recopieViewCreneau();
            if (controleFicheCreneau(bean)) {
                YvsComCreneauPoint entity = UtilCom.buildCreneau(bean, currentUser, selectPoint);
                int idx = jours.indexOf(new YvsJoursOuvres(bean.getJour().getId()));
                if (idx >= 0) {
                    entity.setJour(jours.get(idx));
                }
                bean.setCritere(bean.getTranche().getTypeJournee());
                if (creneau.getId() <= 0) {
                    entity.setId(null);
                    entity = (YvsComCreneauPoint) dao.save1(entity);
                    creneau.setId(entity.getId());
                    creneaux.add(0, entity);
                } else {
                    dao.update(entity);
                    creneaux.set(creneaux.indexOf(entity), entity);
                }
                resetFicheCreneau();
                succes();
                update("data_creneau_point");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public void saveNewConditionnement() {
        try {
            ConditionnementPoint bean = recopieViewConditionnement(article);
            if (controleFiche(bean)) {
                YvsBaseConditionnementPoint y = UtilCom.buildConditionnement(bean, currentUser);
                if (y != null ? y.getId() > 0 : false) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsBaseConditionnementPoint) dao.save1(y);
                }
                int idx = article.getConditionnements().indexOf(y);
                if (idx > -1) {
                    article.getConditionnements().set(idx, y);
                } else {
                    article.getConditionnements().add(0, y);
                }
                resetFicheConditionnement();
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void saveNewRabais() {
        try {
            if (controleFiche(rabais)) {
                selectRabais = UtilCom.buildRabais(rabais, currentUser);
                selectRabais.setArticle(selectArticle);
                if (selectRabais != null ? selectRabais.getId() > 0 : false) {
                    dao.update(selectRabais);
                } else {
                    selectRabais.setId(null);
                    selectRabais = (YvsComRabais) dao.save1(selectRabais);
                }
                int idx = listRabais.indexOf(selectRabais);
                if (idx > -1) {
                    listRabais.set(idx, selectRabais);
                } else {
                    listRabais.add(0, selectRabais);
                }
                resetFicheRabais();
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public boolean saveNewGrille() {
        String action = grille.isUpdate() ? "Modification" : "Insertion";
        try {
            GrilleRabais bean = recopieViewGrille();
            if (controleFicheGrille(bean)) {
                YvsComGrilleRabais y = UtilCom.buildGrilleRabais(bean, currentUser);
                y.setRabais(selectRabais);
                if (y.getId() < 1) {
                    y.setId(null);
                    y = (YvsComGrilleRabais) dao.save1(y);
                } else {
                    dao.update(y);
                }
                int idx = selectRabais.getTranches().indexOf(y);
                if (idx > -1) {
                    selectRabais.getTranches().set(idx, y);
                } else {
                    selectRabais.getTranches().add(0, y);
                }
                resetFicheGrilleRabais();
                succes();
                update("data_grille_rabais");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Lymytz Error>>>> ", ex);
            return false;
        }
        return true;
    }

    public void addLiaisonPointUser(YvsUsers user) {
        if (!autoriser("base_depots_add_accessibilite")) {
            openNotAcces();
            return;
        }
        YvsBasePointVenteUser c = (YvsBasePointVenteUser) dao.loadOneByNameQueries("YvsBasePointVenteUser.findOne", new String[]{"point", "user"}, new Object[]{selectPoint, user});
        if (c == null) {
            c = new YvsBasePointVenteUser();
            c.setActif(true);
            c.setAuthor(currentUser);
            c.setDateSave(new Date());
            c.setDateUpdate(new Date());
            c.setPoint(selectPoint);
            c.setUsers(user);
            dao.save1(c);
        } else {
            c.setDateUpdate(new Date());
            c.setActif(!c.getActif());
            dao.update(c);
        }
        user.setPoint(selectPoint);
        user.setNew_(c.getActif());
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsBasePointVente> list = new ArrayList<>();
                YvsBasePointVente bean;
                for (Long ids : l) {
                    bean = pointsvente.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);

                    if (bean.getId() == pointVente.getId()) {
                        resetFiche();
                    }
                }
                pointsvente.removeAll(list);
                succes();
                update("data_point_vente");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsBasePointVente y) {
        selectPoint = y;
    }

    public void deleteBean_() {
        try {
            if (selectPoint != null) {
                dao.delete(selectPoint);
                pointsvente.remove(selectPoint);
                succes();
                if (selectPoint.getId() == pointVente.getId()) {
                    resetFiche();
                }
                update("data_point_vente");
            } else {
                getErrorMessage("Vous devez selectionner le point");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanArticle() {
        try {
            if ((tabIds_article != null) ? !tabIds_article.equals("") : false) {
                String[] tab = tabIds_article.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsBaseConditionnementPoint bean = pointVente.getArticles().get(pointVente.getArticles().indexOf(new YvsBaseConditionnementPoint(id)));
                    dao.delete(new YvsBaseArticlePoint(id));
                    pointVente.getArticles().remove(bean);
                    if (article.getId() == id) {
                        resetFicheArticle();
                    }
                }
                succes();
                update("data_article_point");
                update("data_depot");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBeanArticle_(YvsBaseConditionnementPoint y) {
        selectArticle = y;
    }

    public void deleteBeanArticle_() {
        try {
            if (selectArticle != null) {
                dao.delete(selectArticle);
                pointVente.getArticles().remove(selectArticle);
                succes();
                if (article.getId() == selectArticle.getId()) {
                    resetFicheArticle();
                }
                update("data_article_point");
                update("data_depot");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteAllBeanArticle() {
        try {
            if (articlesSelect != null ? !articlesSelect.isEmpty() : false) {
                for (YvsBaseConditionnementPoint ad : articlesSelect) {
                    ad.setAuthor(currentUser);
                    dao.delete(ad);
                    if (pointVente.getArticles().contains(ad)) {
                        pointVente.getArticles().remove(ad);
                    }
                }
                articlesSelect.clear();
                succes();
            } else {
                getErrorMessage("Vous devez selectionner des éléments");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBeanConditionnement(YvsBaseConditionnementPoint y) {
        try {
            if (y != null) {
                dao.delete(y);
                article.getConditionnements().remove(y);
                if (conditionnement.getId() == y.getId()) {
                    resetFicheConditionnement();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void toogleActiveAllBeanArticle() {
        try {
            if (articlesSelect != null ? !articlesSelect.isEmpty() : false) {
                for (YvsBaseConditionnementPoint ad : articlesSelect) {
                    ad.setAuthor(currentUser);
                    ad.getArticle().setActif(!ad.getArticle().getActif());
                    dao.update(ad.getArticle());
                    if (pointVente.getArticles().contains(ad)) {
                        pointVente.getArticles().set(pointVente.getArticles().indexOf(ad), ad);
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner des éléments");
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBeanPoint_(YvsBasePointVenteDepot y) {
        selectLiaison = y;
    }

    public boolean occurence(YvsBasePointVenteDepot y) {
        if (y != null ? y.getId() != null ? y.getId() > 0 : false : false) {
            champ = new String[]{"pointVente", "depot"};
            val = new Object[]{y.getPointVente(), y.getDepot()};
            List<YvsBasePointVenteDepot> ly = dao.loadNameQueries("YvsBasePointVenteDepot.findByOne", champ, val);
            return ly != null ? ly.size() > 1 : false;
        }
        return false;
    }

    public void deleteBeanPoint(boolean list) {
        try {
            if (list) {
                if ((tabIds_liaison != null) ? !tabIds_liaison.equals("") : false) {
                    String[] tab = tabIds_liaison.split("-");
                    for (String ids : tab) {
                        long id = Long.valueOf(ids);
                        YvsBasePointVenteDepot bean = liaisons.get(liaisons.indexOf(new YvsBasePointVenteDepot(id)));
                        bean.setActif(false);
                        dao.update(bean.getId());
                        bean.setNew_(!bean.isNew_());
                        int pos = liaisons.indexOf(bean);
                        liaisons.set(pos, bean);
                    }
                    succes();
                }
            } else {
                if (selectLiaison != null) {
                    champ = new String[]{"pointVente", "depot"};
                    val = new Object[]{selectLiaison.getPointVente(), selectLiaison.getDepot()};
                    List<YvsBasePointVenteDepot> ly = dao.loadNameQueries("YvsBasePointVenteDepot.findByOne", champ, val);
                    for (YvsBasePointVenteDepot y : ly) {
                        dao.delete(y);
                    }
                    liaisons.remove(selectLiaison);
                    succes();
                } else {
                    getErrorMessage("Vous devez selectionner la liaison");
                }
            }
            resetFichePoint();
            update("data_point_vente");
            update("data_point_vente_depot");
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
                    YvsComCreneauPoint bean = creneaux.get(creneaux.indexOf(new YvsComCreneauPoint(id)));
                    dao.delete(new YvsComCreneauPoint(bean.getId()));
                    creneaux.remove(bean);
                }
                succes();
                update("data_creneau_point");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanCreneau_(YvsComCreneauPoint y) {
        selectCreneau = y;
    }

    public void deleteBeanCreneau_() {
        try {
            if (selectCreneau != null) {
                dao.delete(selectCreneau);
                creneaux.remove(selectCreneau);
                succes();
                update("data_creneau_point");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : ", ex);
        }
    }

    public void deleteBeanRabais(YvsComRabais y) {
        try {
            if (y != null) {
                dao.delete(y);
                selectArticle.getRabais().remove(y);
                if (y.getId().equals(rabais.getId())) {
                    resetFicheRabais();
                }
                succes();
                update("data_rabais_article_point");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : ", ex);
        }
    }

    public void deleteBeanGrilleRabais(YvsComGrilleRabais y) {
        try {
            if (y != null) {
                dao.delete(y);
                selectRabais.getTranches().remove(y);
                if (y.getId().equals(grille.getId())) {
                    resetFicheGrilleRabais();
                }
                succes();
                update("data_grille_rabais");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : ", ex);
        }
    }

    @Override
    public void onSelectDistant(YvsBasePointVente y) {
        Navigations n = (Navigations) giveManagedBean(Navigations.class);
        if (n != null) {
            n.naviguationView("Points Ventes", "modGescom", "smenPointVente", true);
        }
        onSelectObject(y);
    }

    @Override
    public void onSelectObject(YvsBasePointVente y) {
        selectPoint = y;
        populateView(UtilCom.buildBeanPointVente(selectPoint));
        ManagedEmployes d = (ManagedEmployes) giveManagedBean("MEmps");
        if (d != null) {
            d.setAgence(selectPoint.getAgence());
            d.loadAllEmployesByAgence(true, true);
        }

        //charge les user        
        pointVente.setUtilisateurs(dao.loadNameQueries("YvsUsers.findAlls", new String[]{"societe", "actif"}, new Object[]{currentAgence.getSociete(), true}));
        YvsBasePointVenteUser actif;
        for (YvsUsers u : pointVente.getUtilisateurs()) {
            actif = (YvsBasePointVenteUser) dao.loadObjectByNameQueries("YvsBasePointVenteUser.findOne", new String[]{"point", "user"}, new Object[]{y, u});
            if (actif != null ? actif.getId() > 0 : false) {
                u.setPoint(y);
                u.setNew_(actif.getActif());
            } else {
                u.setPoint(null);
                u.setNew_(false);
            }
        }
        loadAllLiaison(selectPoint);
        loadAllCommerciaux(selectPoint);
        loadCreneaux(selectPoint);
        resetFichePoint();
        loadArticle(true, true);
        update("blog_form_point_vente");
        update("form-more_infos");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBasePointVente y = (YvsBasePointVente) ev.getObject();
            onSelectObject(y);
            tabIds = pointsvente.indexOf(y) + "";

        }
    }

    public void loadOnViewConditionnement(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseConditionnementPoint bean = (YvsBaseConditionnementPoint) ev.getObject();
            populateView(UtilCom.buildBeanConditionnement(bean));
            update("form_conditionnement_point");
        }
    }

    public void unLoadOnViewConditionnement(UnselectEvent ev) {
        resetFicheConditionnement();
        update("form_conditionnement_point");
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("blog_form_point_vente");
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectArticle = (YvsBaseConditionnementPoint) ev.getObject();
            if (selectArticle.getArticle().getArticle().getConditionnements().isEmpty()) {
                selectArticle.getArticle().getArticle().setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{selectArticle.getArticle().getArticle()}));
            }
            populateViewArticle(UtilCom.buildBeanArticlePoint(selectArticle));
            update("blog_form_article_point");
        }
    }

    public void unLoadOnViewArticle(UnselectEvent ev) {
        resetFicheArticle();
        update("blog_form_article_point");
    }

    public void loadOnViewArticle_(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            article.setArticle(new Articles(bean.getId()));
            chooseArticle(bean);
            listArt = false;
        }
    }

    public void loadOnViewPoint(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBasePointVenteDepot bean = (YvsBasePointVenteDepot) ev.getObject();
            populateViewPoint(UtilCom.buildBeanPointVenteDepot(bean));
            update("form_point_vente_depot0");
        }
    }

    public void unLoadOnViewPoint(UnselectEvent ev) {
        resetFichePoint();
        update("form_point_vente_depot0");
    }

    public void loadOnViewCreneau(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComCreneauPoint bean = (YvsComCreneauPoint) ev.getObject();
            populateViewCreneau(UtilCom.buildBeanCreneau(bean));
            chooseCritere();
        }
    }

    public void unLoadOnViewCreneau(UnselectEvent ev) {
        resetFicheCreneau();
        update("form_creneau_point");
    }

    public void loadOnViewRabais(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectRabais = (YvsComRabais) ev.getObject();
            rabais = UtilCom.buildBeanRabais(selectRabais);
            update("form_rabais_article_point");
        }
    }

    public void unLoadOnViewRabais(UnselectEvent ev) {
        resetFicheRabais();
    }

    public void loadOnViewGrilleRabais(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComGrilleRabais y = (YvsComGrilleRabais) ev.getObject();
            grille = UtilCom.buildBeanGrilleRabais(y);
            update("form_grille_rabais");
        }
    }

    public void unLoadOnViewGrilleRabais(UnselectEvent ev) {
        resetFicheGrilleRabais();
    }

    public void loadOnViewEmploye(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsGrhEmployes bean = (YvsGrhEmployes) ev.getObject();
            pointVente.setResponsable(UtilGrh.buildBeanSimplePartialEmploye(bean));
            pointVente.getResponsable().setNom(pointVente.getResponsable().getNom_prenom());
            update("txt_employe_point");
        }
    }

    public void chooseConditionnementRabais() {
        if (rabais.getConditionnement() != null ? rabais.getConditionnement().getId() > 0 : false) {
            if (selectArticle != null ? selectArticle.getArticle() != null : false) {
                int idx = selectArticle.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(rabais.getConditionnement().getId()));
                if (idx > -1) {
//                    YvsBaseConditionnement y = selectArticle.getArticle().getConditionnements().get(idx);
//                    rabais.setConditionnement(UtilProd.buildBeanConditionnement(y));
                }
            }
        }
    }

    public void chooseDepot() {
        if ((liaison.getDepot() != null) ? liaison.getDepot().getId() > 0 : false) {
            YvsBaseDepots d_ = depots.get(depots.indexOf(new YvsBaseDepots(liaison.getDepot().getId())));
            Depots d = UtilCom.buildBeanDepot(d_);
            cloneObject(liaison.getDepot(), d);
        }
    }

    public void chooseSecteur() {
        if ((pointVente.getSecteur() != null) ? pointVente.getSecteur().getId() > 0 : false) {
            ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
            if (m != null) {
                int idx = m.getSecteurs().indexOf(new YvsDictionnaire(pointVente.getSecteur().getId()));
                if (idx > -1) {
                    YvsDictionnaire y = m.getSecteurs().get(idx);
                    pointVente.setSecteur(UtilGrh.buildSimpleBeanDictionnaire(y));
                }
            }
        }
    }

    public void chooseArticle(YvsBaseArticles ar) {
        if ((article.getArticle() != null) ? article.getArticle().getId() > 0 : false) {
            ManagedArticles service = (ManagedArticles) giveManagedBean("Marticle");
            if (service != null) {
                cloneObject(article.getArticle(), service.chechArticleResult(ar));
            }
            article.setPr(ar.getPuv());
            article.setPuvMin(ar.getPrixMin());
            article.setRemise(ar.getRemise());
            article.setChangePrix(ar.getChangePrix());
            Long id = (article.getArticle().getConditionnements() != null) ? !article.getArticle().getConditionnements().isEmpty() ? article.getArticle().getConditionnements().get(0).getId() : 0L : 0L;
            article.setConditionnement(new Conditionnement(id));
        } else {
//            resetFicheArticle();
        }
    }

    public void chooseCritere() {
        loadAllTypes(creneau.getCritere());
        if (creneau.getTranche() == null) {
            creneau.setTranche(new TrancheHoraire());
        }
    }

    public void chooseType() {
        if ((creneau.getTranche() != null) ? creneau.getTranche().getId() > 0 : false) {
            YvsGrhTrancheHoraire d_ = types.get(types.indexOf(new YvsGrhTrancheHoraire(creneau.getTranche().getId())));
            TrancheHoraire d = UtilCom.buildBeanTrancheHoraire(d_);
            cloneObject(creneau.getTranche(), d);
        } else {
            if ((creneau.getTranche() != null) ? creneau.getTranche().getId() < 0 : true) {
                openDialog("dlgAddTrancheHoraire");
            }
        }
    }

    public void chooseAgences() {
        if (pointVente.getAgence() != null ? pointVente.getAgence().getId() > 0 : false) {
            ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
            if (service != null) {
                service.setAgence(new YvsAgences(pointVente.getAgence().getId()));
                service.addParamAgence_(pointVente.getAgence().getId());
            }
        }
    }

    public void chooseConditionnement() {
        if (conditionnement.getConditionnement() != null) {
            int idx = article.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(conditionnement.getConditionnement().getId()));
            if (idx > -1) {
                YvsBaseConditionnement y = article.getArticle().getConditionnements().get(idx);
                conditionnement.setConditionnement(UtilProd.buildBeanConditionnement(y));
                conditionnement.setPrixMin(y.getPrixMin());
                conditionnement.setNaturePrixMin(y.getNaturePrixMin());
                conditionnement.setRemise(y.getRemise());
                conditionnement.setPuv(y.getPrix());
                update("form_conditionnement_point");
            }
        }
    }

    public void activePrincipal(YvsBasePointVenteDepot bean) {
        if (bean != null) {
            if (!bean.getPrincipal()) {
                for (YvsBasePointVenteDepot p : liaisons) {
                    if (!p.equals(bean) && p.getPrincipal()) {
                        getErrorMessage("Il existe deja un dépôt principal pour ce point de vente");
                        return;
                    }
                }
            }
            bean.setPrincipal(!bean.getPrincipal());
            bean.setDateUpdate(new Date());
            bean.setAuthor(currentUser);
            dao.update(bean);
            liaisons.set(liaisons.indexOf(bean), bean);
            succes();
        }
    }

    public void activePointVente(YvsBasePointVente bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            bean.setDateUpdate(new Date());
            bean.setAuthor(currentUser);
            dao.update(bean);
            pointsvente.set(pointsvente.indexOf(bean), bean);
            succes();
        }
    }

    public void venteOnlinePointVente(YvsBasePointVente bean) {
        if (bean != null) {
            bean.setVenteOnline(!bean.getVenteOnline());
            bean.setDateUpdate(new Date());
            bean.setAuthor(currentUser);
            dao.update(bean);
            pointsvente.set(pointsvente.indexOf(bean), bean);
            succes();
        }
    }

    public void activePoint(YvsBasePointVenteDepot bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            bean.setDateUpdate(new Date());
            bean.setAuthor(currentUser);
            dao.update(bean);
            int idx = liaisons.indexOf(bean);
            if (idx > -1) {
                liaisons.set(idx, bean);
            }
            succes();
        }
    }

    public void activeChangePrix(YvsBaseConditionnementPoint bean) {
        if (bean != null) {
            if (!bean.getArticle().getArticle().getChangePrix() && !bean.getArticle().getChangePrix()) {
                getErrorMessage("Cet article n'accepte pas les modifications de prix lors de la vente");
                return;
            }
            bean.getArticle().setChangePrix(!bean.getArticle().getChangePrix());
            bean.getArticle().setDateUpdate(new Date());
            bean.getArticle().setAuthor(currentUser);
            dao.update(bean.getArticle());
            pointVente.getArticles().set(pointVente.getArticles().indexOf(bean), bean);
            succes();
        }
    }

    public void activeAllChangePrix() {
        if (articlesSelect != null ? !articlesSelect.isEmpty() : false) {
            for (YvsBaseConditionnementPoint ad : articlesSelect) {
                if (!ad.getArticle().getChangePrix() && !ad.getArticle().getChangePrix()) {
                    continue;
                }
                ad.getArticle().setChangePrix(!ad.getArticle().getChangePrix());
                ad.getArticle().setDateUpdate(new Date());
                ad.getArticle().setAuthor(currentUser);
                dao.update(ad.getArticle());
                if (pointVente.getArticles().contains(ad)) {
                    pointVente.getArticles().set(pointVente.getArticles().indexOf(ad), ad);
                }
            }
            succes();
        } else {
            getErrorMessage("Vous devez selectionner des éléments");
        }
    }

    public void activeArticle(YvsBaseConditionnementPoint bean) {
        if (bean != null ? bean.getId() > 0 : false) {
            bean.setDateUpdate(new Date());
            bean.setActif(!bean.getActif());
            bean.setAuthor(currentUser);
            dao.update(bean);
            bean.getArticle().setDateUpdate(new Date());
            bean.getArticle().setActif(bean.getActif());
            bean.getArticle().setAuthor(currentUser);
            dao.update(bean.getArticle());
            int index = pointVente.getArticles().indexOf(bean);
            if (index > -1) {
                pointVente.getArticles().set(index, bean);
            }
            succes();
        }
    }

    public void activeCreneau(YvsComCreneauPoint bean) {
        if (bean != null) {
            if (!bean.getActif() && !bean.getTranche().getActif()) {
                getErrorMessage("Vous ne pouvez pas activer ce créneau car son type est déactivé");
            } else {
                bean.setActif(!bean.getActif());
                bean.setDateUpdate(new Date());
                bean.setAuthor(currentUser);
                dao.update(bean);
                creneaux.set(creneaux.indexOf(bean), bean);
                succes();
            }
        }
    }

    public void filterArticle() {
        Articles a = new Articles();
        a.setRefArt(numSearch_);
        a.setError(true);
        if (numSearch_ != null ? numSearch_.trim().length() > 0 : false) {
            ParametreRequete p = new ParametreRequete(null, "refArt", numSearch_.toUpperCase() + "%", " LIKE ", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.article.refArt)", "refArt", numSearch_.toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.article.designation)", "designation", numSearch_.toUpperCase() + "%", " LIKE ", "OR"));
            pa.addParam(p);
            loadArticle(true, true);
        } else {
            pa.addParam(new ParametreRequete("y.article.article.refArt", "refArt", null));
            loadArticle(true, true);
        }
    }

    public void addParamActifArticle() {
        pa.addParam(new ParametreRequete("y.article.actif", "actif", actif_, "=", "AND"));
        loadArticle(true, true);
    }

    public PointVente search(String code, boolean open) {
        PointVente a = new PointVente();
        a.setCode(code);
        a.setError(true);
        ParametreRequete p = new ParametreRequete("y.code", "code", null);
        if (Util.asString(code)) {
            p = new ParametreRequete(null, "code", code.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.code)", "code", code.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.libelle)", "libelle", code.trim().toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadPointVenteByDroit(true, true);
        if (pointsvente != null ? !pointsvente.isEmpty() : false) {
            if (pointsvente.size() == 1) {
                a = UtilCom.buildBeanPointVente(pointsvente.get(0));
            } else if (open) {
                openDialog("dlgListPointVente");
            }
        }
        return a;
    }

    public void searchEmploye() {
        String num = pointVente.getResponsable().getMatricule();
        ManagedEmployes m = (ManagedEmployes) giveManagedBean("MEmps");
        if (m != null) {
            if (pointVente.getAgence() != null ? pointVente.getAgence().getId() > 0 : false) {
                m.setAgence(new YvsAgences(pointVente.getAgence().getId()));
            } else {
                m.setAgence(currentAgence);
            }
            Employe e = m.searchEmployeActif(num, true);
            pointVente.setResponsable(e);
            if (m.getListEmployes() != null ? m.getListEmployes().size() > 1 : false) {
                update("data_responsable_point");
            }
        }
    }

    public void initEmployes() {
        ManagedEmployes m = (ManagedEmployes) giveManagedBean("MEmps");
        if (m != null) {
            m.initEmployes(pointVente.getResponsable());
            update("data_responsable_point");
        }
    }

    public void searchArticle() {
        String num = article.getArticle().getRefArt();
        article.getArticle().setDesignation("");
        article.getArticle().setError(true);
        article.getArticle().setId(0);
        listArt = false;
        ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
        if (m != null) {
            Articles y = m.searchArticleActif("V", num, true);
            if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                if (m.getArticlesResult().size() > 1) {
                    update("data_articles_point");
                } else {
                    article.setArticle(y);
                    article.setPuvMin(y.getPuvMin());
                    article.setPr(y.getPuv());
                    article.setRemise(y.getRemise());
                    article.setChangePrix(y.isChangePrix());
                    Long id = (article.getArticle().getConditionnements() != null) ? !article.getArticle().getConditionnements().isEmpty() ? article.getArticle().getConditionnements().get(0).getId() : 0L : 0L;
                    article.setConditionnement(new Conditionnement(id));
                }
                article.getArticle().setError(false);
            }
            listArt = y.isListArt();
        }
    }

    public void initArticles() {
        listArt = false;
        ManagedArticles m = (ManagedArticles) giveManagedBean("");
        if (m != null) {
            m.initArticles("V", article.getArticle());
            listArt = article.getArticle().isListArt();
        }
        update("data_articles_point");
    }

    public void insertAllArticle() {
        try {
            if ((pointVente != null) ? pointVente.getId() > 0 : false) {
                boolean bien = false;
                List<YvsBaseArticles> l = dao.loadNameQueries("YvsBaseArticles.findFullByPFANDNEGOCE", new String[]{"societe", "categorie", "categorie1"}, new Object[]{currentUser.getAgence().getSociete(), Constantes.CAT_PF, Constantes.CAT_MARCHANDISE});
                YvsBaseArticlePoint y_a;
                YvsBaseConditionnementPoint y_c;
                for (YvsBaseArticles a : l) {
                    y_a = (YvsBaseArticlePoint) dao.loadOneByNameQueries("YvsBaseArticlePoint.findByArticlePoint", new String[]{"article", "point"}, new Object[]{a, selectPoint});
                    if (y_a != null ? y_a.getId() < 1 : true) {
                        y_a = new YvsBaseArticlePoint();
                        y_a.setPoint(selectPoint);
                        y_a.setArticle(a);
                        y_a.setPuv(a.getPuv());
                        y_a.setPuvMin(a.getPrixMin());
                        y_a.setAuthor(currentUser);
                        y_a.setActif(false);
                        y_a.setSupp(false);
                        y_a.setNew_(true);
                        y_a.setDateSave(new Date());
                        y_a.setDateUpdate(new Date());
                        if (!a.getConditionnements().isEmpty()) {
                            y_a.setConditionementVente(a.getConditionnements().get(0));
                            y_a.setPuv(a.getConditionnements().get(0).getPrix());
                            y_a.setPuvMin(a.getConditionnements().get(0).getPrixMin());
                        }
                        y_a = (YvsBaseArticlePoint) dao.save1(y_a);
                        bien = true;
                    }
                    if (y_a != null ? y_a.getId() < 1 : true) {
                        continue;
                    }
                    int index;
                    for (YvsBaseConditionnement c : a.getConditionnements()) {
                        y_c = (YvsBaseConditionnementPoint) dao.loadOneByNameQueries("YvsBaseConditionnementPoint.findOne", new String[]{"article", "unite"}, new Object[]{y_a, c});
                        if (y_c != null ? y_c.getId() < 1 : true) {
                            y_c = new YvsBaseConditionnementPoint();
                            y_c.setArticle(y_a);
                            y_c.setAuthor(currentUser);
                            y_c.setDateSave(new Date());
                            y_c.setDateUpdate(new Date());
                            y_c.setConditionnement(c);
                            y_c.setNaturePrixMin(c.getNaturePrixMin());
                            y_c.setNatureRemise(Constantes.NATURE_MTANT);
                            y_c.setPrixMin(c.getPrixMin());
                            y_c.setPuv(c.getPrix());
                            y_c.setRemise(c.getRemise());
                            y_c = (YvsBaseConditionnementPoint) dao.save1(y_c);
                            y_a.getConditionnements().add(y_c);
                            index = pointVente.getArticles().indexOf(y_c);
                            if (index > -1) {
                                pointVente.getArticles().set(index, y_c);
                            } else {
                                pointVente.getArticles().add(y_c);
                            }
                            bien = true;
                        }
                    }

                }
                if (bien) {
                    pointsvente.set(pointsvente.indexOf(selectPoint), selectPoint);
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Initialisation Impossible!");
            getException("Lymytz Error>>> ", ex);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static String photo(PointVente u) {
        if (u != null) {
            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
            String path = ext.getRealPath("resources/lymytz/documents/docArticle/") + Initialisation.FILE_SEPARATOR + u.getPhoto();
            if (new File(path).exists()) {
                return u.getPhoto();
            }
        }
        return "produits.png";
    }

    public void handleFileUpload(FileUploadEvent ev) {
        if (ev != null) {
            String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminArticle().substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminArticle().length());
            //répertoire destination de sauvegarge= C:\\lymytz\scte...
            String repDestSVG = Initialisation.getCheminArticle();
            String file = Util.giveFileName() + "." + Util.getExtension(ev.getFile().getFileName());
            try {
                //copie dans le dossier de l'application
                Util.copyFile(file, repDest + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                //copie dans le dossier de sauvegarde
                Util.copySVGFile(file, repDestSVG + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                File f = new File(new StringBuilder(repDest).append(file).toString());
                if (!f.exists()) {
                    File doc = new File(repDest);
                    if (!doc.exists()) {
                        doc.mkdirs();
                        f.createNewFile();
                    } else {
                        f.createNewFile();
                    }
                }
                pointVente.setPhoto(file);
                getInfoMessage("Charger !");
                update("photo_point_vente");

            } catch (IOException ex) {
                Logger.getLogger(ManagedSociete.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void loadPointDeVente(YvsBaseConditionnement cond) {
        setImax(200);
        loadAllPointVente(true, true);
        article.setArticle(UtilProd.buildSimpleBeanArticles(cond.getArticle()));
        article.setConditionnement(UtilProd.buildSimpleBeanConditionnement(cond));
        article.setNaturePrixMin(Constantes.NATURE_TAUX);
        article.setNatureRemise(Constantes.NATURE_TAUX);
        if (!pointsvente.isEmpty()) {
            selectPoint = pointsvente.get(0);
            article.setPoint(UtilCom.buildSimpleBeanPointVente(pointsvente.get(0)));
            //charge les infos de l'article
            YvsBaseConditionnementPoint temp = (YvsBaseConditionnementPoint) dao.loadOneByNameQueries("YvsBaseConditionnementPoint.findArticlePointUnite", new String[]{"article", "point", "unite"}, new Object[]{cond.getArticle(), pointsvente.get(0), cond});
            if (temp != null) {
                article.setPuv(temp.getPuv());
                article.setRemise(temp.getRemise());
            } else {
                article.setPuv(article.getConditionnement().getPrix());
                article.setRemise(article.getConditionnement().getRemise());
            }
        }
        openDialog("dlgPtarticle");
        update("form_update_art_pt");
    }

    public void loadOnViewClient(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComClient y = (YvsComClient) ev.getObject();
            chooseClient(UtilCom.buildBeanClient(y));
        }
    }

    public void chooseClient(Client d) {
        if ((d != null) ? d.getId() > 0 : false) {
            cloneObject(pointVente.getClient(), d);
            update("blog_form_point_vente:select_client_point_vente");
        }
    }

    public void searchClient() {
        String num = pointVente.getClient().getCodeClient();
        pointVente.getClient().setId(0);
        pointVente.getClient().setError(true);
        pointVente.getClient().setTiers(new Tiers());
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (m != null) {
            Client y = m.searchClient(num, true);
            if (m.getClients() != null ? !m.getClients().isEmpty() : false) {
                if (m.getClients().size() > 1) {
                    update("data_client_point_vente");
                } else {
                    chooseClient(y);
                }
                pointVente.getClient().setError(false);
            }
        }
    }

    public void selectedPoint(ValueChangeEvent ev) {
        if (ev != null) {
            Long id = (Long) ev.getNewValue();
            if (id > 0) {
                int idx = pointsvente.indexOf(new YvsBasePointVente(id));
                if (idx >= 0) {
                    selectPoint = pointsvente.get(idx);
                    article.setPoint(UtilCom.buildSimpleBeanPointVente(selectPoint));
                    //charge les infos de l'article
                    YvsBaseConditionnementPoint temp = (YvsBaseConditionnementPoint) dao.loadOneByNameQueries("YvsBaseConditionnementPoint.findArticlePointUnite", new String[]{"article", "point", "unite"}, new Object[]{new YvsBaseArticles(article.getArticle().getId()), selectPoint, new YvsBaseConditionnement(article.getConditionnement().getId())});
                    if (temp != null) {
                        article.setPuv(temp.getPuv());
                        article.setRemise(temp.getRemise());
                    } else {
                        article.setPuv(article.getConditionnement().getPrix());
                        article.setRemise(article.getConditionnement().getRemise());
                    }
                }
            }
        }
        update("form_update_art_pt");
    }

    public void addPlanning(YvsBasePointVente y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedCreneauEmploye w = (ManagedCreneauEmploye) giveManagedBean(ManagedCreneauEmploye.class);
            if (w != null) {
                w.resetFiche();
                w.setPoint(new PointVente(y.getId()));
                w.choosePoint();
                update("select_creneau_point");
                update("txt_point_vente_creneau");
                update("form_creneau_employe");
            }
        }
    }

    public void clearParams() {
        paginator.getParams().clear();
        codeSearch = "";
        typeSearch = "";
        lieuSearch = 0;
        agenceSearch = 0;
        actifSearch = null;
        loadAllPointVente(true, true);
    }

    public void addParamType() {
        ParametreRequete p = new ParametreRequete("y.type", "type", null);
        if (typeSearch != null ? typeSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.type", "type", typeSearch.charAt(0), "=", "AND");
        }
        paginator.addParam(p);
        loadAllPointVente(true, true);
    }

    public void addParamCode() {
        ParametreRequete p = new ParametreRequete("UPPER(y.libelle)", "reference", null);
        if (codeSearch != null ? codeSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", "%" + codeSearch + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.libelle)", "libelle", "%" + codeSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.code)", "code", "%" + codeSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllPointVente(true, true);
    }

    public void addParamActif() {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
        loadAllPointVente(true, true);
    }

    public void addParamAgence() {
        ParametreRequete p = new ParametreRequete("y.agence", "agence", null, "=", "AND");
        if (agenceSearch > 0) {
            p = new ParametreRequete("y.agence", "agence", new YvsAgences(agenceSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAllPointVente(true, true);
    }

    public void addParamLieu() {
        ParametreRequete p = new ParametreRequete("y.secteur", "secteur", null, "=", "AND");
        if (lieuSearch > 0) {
            p = new ParametreRequete("y.secteur", "secteur", new YvsDictionnaire(lieuSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAllPointVente(true, true);
    }

}
