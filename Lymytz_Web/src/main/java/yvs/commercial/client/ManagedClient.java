/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.client;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.UploadedFile;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.Comptes;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.ModelReglement;
import yvs.base.compta.UtilCompta;
import yvs.base.produits.Articles;
import yvs.base.produits.ManagedArticles;
import yvs.base.tiers.Contact;
import yvs.base.tiers.Tiers;
import yvs.base.tiers.UtilTiers;
import yvs.base.tiers.ManagedTiers;
import yvs.base.tiers.ManagedTiersDocument;
import yvs.commercial.ManagedCatCompt;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.ManagedModeReglement;
import yvs.commercial.UtilCom;
import yvs.commercial.depot.ManagedPointLivraison;
import yvs.commercial.depot.PointLivraison;
import yvs.commercial.depot.PointVente;
import yvs.commercial.rrr.ManagedRistourne;
import yvs.commercial.rrr.PlanRistourne;
import yvs.commercial.stock.ManagedStockArticle;
import yvs.commercial.vente.ManagedFactureVente;
import yvs.dao.Options;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBasePointLivraison;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsComCategorieTarifaire;
import yvs.entity.commercial.client.YvsComContratsClient;
import yvs.entity.commercial.client.YvsComElementAddContratsClient;
import yvs.entity.commercial.client.YvsComElementContratsClient;
import yvs.entity.commercial.rrr.YvsComPlanRistourne;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.ext.YvsExtClients;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.tiers.YvsBaseTiersTemplate;
import yvs.entity.users.YvsUsers;
import yvs.grh.UtilGrh;
import yvs.grh.bean.ManagedTypeCout;
import yvs.grh.presence.TrancheHoraire;
import static yvs.init.Initialisation.USER_DOWNLOAD;
import static yvs.init.Initialisation.USER_DOWNLOAD_LINUX;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.dico.ManagedDico;
import yvs.production.UtilProd;
import yvs.service.IEntitySax;
import yvs.service.base.tiers.client.IYvsComContratsClient;
import yvs.service.base.tiers.client.IYvsComElementAddContratsClient;
import yvs.service.base.tiers.client.IYvsComElementContratsClient;
import yvs.users.ManagedUser;
import yvs.users.Users;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
import static yvs.util.Managed.isWindows;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedClient extends ManagedCommercial<Client, YvsComClient> implements Serializable {

    @ManagedProperty(value = "#{client}")
    private Client client;
    private List<YvsComClient> clients;
    private List<Long> ids_clients;
    private YvsComClient selectClient;

    private long idCategorie;
    private CategorieTarifaire tarifaire = new CategorieTarifaire();
    private List<YvsComCategorieTarifaire> tarifaires;
    private YvsComCategorieTarifaire selectTarifaire;

    private List<YvsBaseTiers> tiersLoad;
    private List<YvsComParametre> parametres;

    private List<YvsBasePointVente> points;
    private Date dateFacture = new Date();
    private PointVente point = new PointVente();
    private TrancheHoraire tranche = new TrancheHoraire();
    private Users vendeur = new Users();
    private ContratsClient contrat = new ContratsClient();
    private YvsComContratsClient selectContrat;
    private ElementContratsClient element = new ElementContratsClient();
    private YvsComElementContratsClient selectElement;
    private ElementAddContratsClient additionnel = new ElementAddContratsClient();
    private YvsComElementAddContratsClient selectAdditionnel;

    private YvsBaseTiersTemplate selectTemplate;
    private boolean updateClient, generation;
    private String tabIds, tabIds_tarifaire;
    private String fusionneTo;
    private List<String> fusionnesBy;

    private UploadedFile file;
    private long max = 15;

    private String typeSearch, compteSearch, reprSearch;
    private long paysSearch, villeSearch, secteurSearch, categorieSearch, groupeSearch, planRSearch;
    private Boolean actifSearch, defautSearch, confirmerSearch;
    private boolean _first = true;
    private Date debutAcompte = new Date(), finAcompte = new Date(), debutCreance = new Date(), finCreance = new Date();

    private String codeExterne, report;
    private boolean displayParamExt;
    private Date dateDebutPrint = new Date(), dateFinPrint = new Date();

    private boolean actionEmploye = true, actionTiers = true, actionFournisseur = true, actionCommercial = true;

    private String prefixe;
    private Comptes compte = new Comptes();
    private boolean reIncrementer, suiviComptable = true;

    IEntitySax IEntitiSax = new IEntitySax();

    public ManagedClient() {
        tiersLoad = new ArrayList<>();
        clients = new ArrayList<>();
        tarifaires = new ArrayList<>();
        parametres = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
        ids_clients = new ArrayList<>();
        points = new ArrayList<>();
    }

    public long getPlanRSearch() {
        return planRSearch;
    }

    public void setPlanRSearch(long planRSearch) {
        this.planRSearch = planRSearch;
    }

    public Date getDateDebutPrint() {
        return dateDebutPrint;
    }

    public void setDateDebutPrint(Date dateDebutPrint) {
        this.dateDebutPrint = dateDebutPrint;
    }

    public Date getDateFinPrint() {
        return dateFinPrint;
    }

    public void setDateFinPrint(Date dateFinPrint) {
        this.dateFinPrint = dateFinPrint;
    }

    public boolean isActionEmploye() {
        return actionEmploye;
    }

    public void setActionEmploye(boolean actionEmploye) {
        this.actionEmploye = actionEmploye;
    }

    public boolean isActionTiers() {
        return actionTiers;
    }

    public void setActionTiers(boolean actionTiers) {
        this.actionTiers = actionTiers;
    }

    public boolean isActionFournisseur() {
        return actionFournisseur;
    }

    public void setActionFournisseur(boolean actionFournisseur) {
        this.actionFournisseur = actionFournisseur;
    }

    public boolean isActionCommercial() {
        return actionCommercial;
    }

    public void setActionCommercial(boolean actionCommercial) {
        this.actionCommercial = actionCommercial;
    }

    public Date getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(Date dateFacture) {
        this.dateFacture = dateFacture;
    }

    public List<YvsBasePointVente> getPoints() {
        return points;
    }

    public void setPoints(List<YvsBasePointVente> points) {
        this.points = points;
    }

    public PointVente getPoint() {
        return point;
    }

    public void setPoint(PointVente point) {
        this.point = point;
    }

    public TrancheHoraire getTranche() {
        return tranche;
    }

    public void setTranche(TrancheHoraire tranche) {
        this.tranche = tranche;
    }

    public Users getVendeur() {
        return vendeur;
    }

    public void setVendeur(Users vendeur) {
        this.vendeur = vendeur;
    }

    public ContratsClient getContrat() {
        return contrat;
    }

    public void setContrat(ContratsClient contrat) {
        this.contrat = contrat;
    }

    public YvsComContratsClient getSelectContrat() {
        return selectContrat;
    }

    public void setSelectContrat(YvsComContratsClient selectContrat) {
        this.selectContrat = selectContrat;
    }

    public ElementContratsClient getElement() {
        return element;
    }

    public void setElement(ElementContratsClient element) {
        this.element = element;
    }

    public YvsComElementContratsClient getSelectElement() {
        return selectElement;
    }

    public void setSelectElement(YvsComElementContratsClient selectElement) {
        this.selectElement = selectElement;
    }

    public ElementAddContratsClient getAdditionnel() {
        return additionnel;
    }

    public void setAdditionnel(ElementAddContratsClient additionnel) {
        this.additionnel = additionnel;
    }

    public YvsComElementAddContratsClient getSelectAdditionnel() {
        return selectAdditionnel;
    }

    public void setSelectAdditionnel(YvsComElementAddContratsClient selectAdditionnel) {
        this.selectAdditionnel = selectAdditionnel;
    }

    public List<Long> getIds_clients() {
        return ids_clients;
    }

    public void setIds_clients(List<Long> ids_clients) {
        this.ids_clients = ids_clients;
    }

    public boolean isSuiviComptable() {
        return suiviComptable;
    }

    public void setSuiviComptable(boolean suiviComptable) {
        this.suiviComptable = suiviComptable;
    }

    public boolean isReIncrementer() {
        return reIncrementer;
    }

    public void setReIncrementer(boolean reIncrementer) {
        this.reIncrementer = reIncrementer;
    }

    public String getPrefixe() {
        return prefixe;
    }

    public void setPrefixe(String prefixe) {
        this.prefixe = prefixe;
    }

    public Comptes getCompte() {
        return compte;
    }

    public void setCompte(Comptes compte) {
        this.compte = compte;
    }

    public Boolean getConfirmerSearch() {
        return confirmerSearch;
    }

    public void setConfirmerSearch(Boolean confirmerSearch) {
        this.confirmerSearch = confirmerSearch;
    }

    public List<YvsComParametre> getParametres() {
        return parametres;
    }

    public void setParametres(List<YvsComParametre> parametres) {
        this.parametres = parametres;
    }

    public String getCodeExterne() {
        return codeExterne;
    }

    public void setCodeExterne(String codeExterne) {
        this.codeExterne = codeExterne;
    }

    public boolean isDisplayParamExt() {
        return displayParamExt;
    }

    public void setDisplayParamExt(boolean displayParamExt) {
        this.displayParamExt = displayParamExt;
    }

    public Date getDebutAcompte() {
        return debutAcompte;
    }

    public void setDebutAcompte(Date debutAcompte) {
        this.debutAcompte = debutAcompte;
    }

    public Date getFinAcompte() {
        return finAcompte;
    }

    public void setFinAcompte(Date finAcompte) {
        this.finAcompte = finAcompte;
    }

    public Date getDebutCreance() {
        return debutCreance;
    }

    public void setDebutCreance(Date debutCreance) {
        this.debutCreance = debutCreance;
    }

    public Date getFinCreance() {
        return finCreance;
    }

    public void setFinCreance(Date finCreance) {
        this.finCreance = finCreance;
    }

    public String getReprSearch() {
        return reprSearch;
    }

    public void setReprSearch(String reprSearch) {
        this.reprSearch = reprSearch;
    }

    public boolean isFirst() {
        return _first;
    }

    public void setFirst(boolean _first) {
        this._first = _first;
    }

    public String getFusionneTo() {
        return fusionneTo;
    }

    public void setFusionneTo(String fusionneTo) {
        this.fusionneTo = fusionneTo;
    }

    public List<String> getFusionnesBy() {
        return fusionnesBy;
    }

    public void setFusionnesBy(List<String> fusionnesBy) {
        this.fusionnesBy = fusionnesBy;
    }

    public String getCompteSearch() {
        return compteSearch;
    }

    public void setCompteSearch(String compteSearch) {
        this.compteSearch = compteSearch;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public YvsBaseTiersTemplate getSelectTemplate() {
        return selectTemplate;
    }

    public void setSelectTemplate(YvsBaseTiersTemplate selectTemplate) {
        this.selectTemplate = selectTemplate;
    }

    public List<YvsComClient> getClients() {
        return clients;
    }

    public void setClients(List<YvsComClient> clients) {
        this.clients = clients;
    }

    public long getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(long idCategorie) {
        this.idCategorie = idCategorie;
    }

    public CategorieTarifaire getTarifaire() {
        return tarifaire;
    }

    public void setTarifaire(CategorieTarifaire tarifaire) {
        this.tarifaire = tarifaire;
    }

    public List<YvsComCategorieTarifaire> getTarifaires() {
        return tarifaires;
    }

    public void setTarifaires(List<YvsComCategorieTarifaire> tarifaires) {
        this.tarifaires = tarifaires;
    }

    public YvsComCategorieTarifaire getSelectTarifaire() {
        return selectTarifaire;
    }

    public void setSelectTarifaire(YvsComCategorieTarifaire selectTarifaire) {
        this.selectTarifaire = selectTarifaire;
    }

    public String getTabIds_tarifaire() {
        return tabIds_tarifaire;
    }

    public void setTabIds_tarifaire(String tabIds_tarifaire) {
        this.tabIds_tarifaire = tabIds_tarifaire;
    }

    public boolean isGeneration() {
        return generation;
    }

    public void setGeneration(boolean generation) {
        this.generation = generation;
    }

    public Boolean getDefautSearch() {
        return defautSearch;
    }

    public void setDefautSearch(Boolean defautSearch) {
        this.defautSearch = defautSearch;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public long getPaysSearch() {
        return paysSearch;
    }

    public void setPaysSearch(long paysSearch) {
        this.paysSearch = paysSearch;
    }

    public long getVilleSearch() {
        return villeSearch;
    }

    public void setVilleSearch(long villeSearch) {
        this.villeSearch = villeSearch;
    }

    public long getSecteurSearch() {
        return secteurSearch;
    }

    public void setSecteurSearch(long secteurSearch) {
        this.secteurSearch = secteurSearch;
    }

    public long getCategorieSearch() {
        return categorieSearch;
    }

    public void setCategorieSearch(long categorieSearch) {
        this.categorieSearch = categorieSearch;
    }

    public long getGroupeSearch() {
        return groupeSearch;
    }

    public void setGroupeSearch(long groupeSearch) {
        this.groupeSearch = groupeSearch;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<YvsBaseTiers> getTiersLoad() {
        return tiersLoad;
    }

    public void setTiersLoad(List<YvsBaseTiers> tiersLoad) {
        this.tiersLoad = tiersLoad;
    }

    public YvsComClient getSelectClient() {
        return selectClient;
    }

    public void setSelectClient(YvsComClient selectClient) {
        this.selectClient = selectClient;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean isUpdateClient() {
        return updateClient;
    }

    public void setUpdateClient(boolean updateClient) {
        this.updateClient = updateClient;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        loadAllClient(true, true);
        loadComptes();
        _first = true;

        if (client != null ? client.getModel() != null ? client.getModel().getId() < 0 : false : false) {
            client.getModel().setId(0);
        }
        if (client != null ? client.getCategorieComptable() != null ? client.getCategorieComptable().getId() < 0 : false : false) {
            client.getCategorieComptable().setId(0);
        }
        if (client != null ? client.getId() < 1 ? client.getSeuilSolde() <= 0 : false : false) {
            parametres = dao.loadNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            if (parametres != null ? !parametres.isEmpty() : false) {
                client.setSeuilSolde(parametres.get(0).getSeuilClient());
            }
        }
    }

    public void loadAllClient(boolean avance, boolean init) {
        String table = "YvsComClient y JOIN FETCH y.tiers "
                + "LEFT JOIN FETCH y.tiers.secteur "
                + "LEFT JOIN FETCH y.planRistourne "
                + "LEFT JOIN FETCH y.representant "
                + "LEFT JOIN FETCH y.model "
                + "LEFT JOIN FETCH y.categorieComptable "
                + "LEFT JOIN FETCH y.createBy "
                + "LEFT JOIN FETCH y.compte "
                + "LEFT JOIN FETCH y.tiers.pays "
                + "LEFT JOIN FETCH y.tiers.ville ";
        String field = "DISTINCT y";
        String order = "y.tiers.ville.id, y.codeClient";
        if (groupeSearch > 0) {
            table = "YvsComCategorieTarifaire y LEFT JOIN FETCH y.client "
                    + "LEFT JOIN FETCH y.client.tiers "
                    + "LEFT JOIN FETCH y.client.tiers.secteur "
                    + "LEFT JOIN FETCH y.client.planRistourne "
                    + "LEFT JOIN FETCH y.client.representant "
                    + "LEFT JOIN FETCH y.client.model "
                    + "LEFT JOIN FETCH y.client.categorieComptable "
                    + "LEFT JOIN FETCH y.client.createBy "
                    + "LEFT JOIN FETCH y.client.compte "
                    + "LEFT JOIN FETCH y.client.tiers.pays "
                    + "LEFT JOIN FETCH y.client.tiers.ville ";
            field = "DISTINCT y.client";
            order = "y.client.tiers.ville.id, y.client.codeClient";
            paginator.addParam(new ParametreRequete("y.client.tiers.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        } else {
            paginator.addParam(new ParametreRequete("y.tiers.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        }
        clients = paginator.executeDynamicQuery(field, field, table, order, avance, init, (int) imax, dao);

        field = "y.id";
        champ = paginator.getChamp();
        val = paginator.getVal();
        nameQueri = paginator.buildDynamicQuery(paginator.getParams(), "SELECT " + field + " FROM " + table + " WHERE") + " ORDER BY " + order;
        ids_clients = dao.loadEntity(nameQueri, champ, val);

        if (clients != null ? clients.size() == 1 : false) {
            onSelectObject(clients.get(0));
            execute("collapseForm('client')");
        } else {
            execute("collapseList('client')");
        }
        update("data_client");
    }

    public void loadAllClient(long imax) {
        if (_first) {
            clearParams(true);
        }
        this.imax = imax;
        _first = false;
        addParamActif();
    }

    public void loadActifClient(Boolean actif) {
        if (_first) {
            clearParams(true);
        }
        actifSearch = actif;
        _first = false;
        addParamActif();
    }

    private void loadTarifaire(YvsComClient y) {
        champ = new String[]{"client"};
        val = new Object[]{y};
        tarifaires = dao.loadNameQueries("YvsComCategorieTarifaire.findByClient", champ, val);
    }

    public void loadCreanceOrAcompte(boolean acompte) {
        champ = new String[]{"client", "dateDebut", "dateFin"};
        if (acompte) {
            val = new Object[]{new YvsComClient(client.getId()), debutAcompte, finAcompte};
            nameQueri = "YvsComptaCaissePieceVente.findByAcompteClientDates";
            client.setAcomptes(dao.loadNameQueries(nameQueri, champ, val));
        } else {
            val = new Object[]{new YvsComClient(client.getId()), debutCreance, finCreance};
            nameQueri = "YvsComptaCaissePieceVente.findByCreanceClientDates";
            client.setCreances(dao.loadNameQueries(nameQueri, champ, val));
        }
    }

    public YvsComClient buildClient(Client y, YvsBaseTiers t) {
        YvsComClient f = new YvsComClient();
        if (y != null) {
            f.setId(y.getId());
            if ((t != null) ? t.getId() > 0 : false) {
                f.setTiers(t);
            }
            if ((y.getCategorieComptable() != null) ? y.getCategorieComptable().getId() > 0 : false) {
                ManagedCatCompt m = (ManagedCatCompt) giveManagedBean(ManagedCatCompt.class);
                if (m != null) {
                    if (m.getCategories().contains(new YvsBaseCategorieComptable(y.getCategorieComptable().getId()))) {
                        f.setCategorieComptable(m.getCategories().get(m.getCategories().indexOf(new YvsBaseCategorieComptable(y.getCategorieComptable().getId()))));
                    } else {
                        f.setCategorieComptable(new YvsBaseCategorieComptable(y.getCategorieComptable().getId()));
                    }
                } else {
                    f.setCategorieComptable(new YvsBaseCategorieComptable(y.getCategorieComptable().getId()));
                }
            }
            f.setDefaut(y.isDefaut());
            f.setCodeClient(y.getCodeClient());
            if (y.getCompteCollectif() != null ? y.getCompteCollectif().getId() > 0 : false) {
                f.setCompte(new YvsBasePlanComptable(y.getCompteCollectif().getId(), y.getCompteCollectif().getNumCompte(), y.getCompteCollectif().getIntitule()));
            }
            if (y.getRepresentant() != null ? y.getRepresentant().getId() > 0 : false) {
                f.setRepresentant(new YvsUsers(y.getRepresentant().getId()));
            }
            if (y.getModel() != null ? y.getModel().getId() > 0 : false) {
                ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
                if (m != null) {
                    if (m.getModels().contains(new YvsBaseModelReglement(y.getModel().getId()))) {
                        f.setModel(m.getModels().get(m.getModels().indexOf(new YvsBaseModelReglement(y.getModel().getId()))));
                    } else {
                        f.setModel(new YvsBaseModelReglement(y.getModel().getId()));
                    }
                } else {
                    f.setModel(new YvsBaseModelReglement(y.getModel().getId()));
                }
            }
            if (y.getRistourne() != null ? y.getRistourne().getId() > 0 : false) {
                ManagedRistourne m = (ManagedRistourne) giveManagedBean(ManagedRistourne.class);
                if (m != null) {
                    if (m.getPlans().contains(new YvsComPlanRistourne(y.getRistourne().getId()))) {
                        f.setPlanRistourne(m.getPlans().get(m.getPlans().indexOf(new YvsComPlanRistourne(y.getRistourne().getId()))));
                    } else {
                        f.setPlanRistourne(new YvsComPlanRistourne(y.getRistourne().getId()));
                    }
                } else {
                    f.setPlanRistourne(new YvsComPlanRistourne(y.getRistourne().getId()));
                }
            }
            f.setNom(y.getNom());
            f.setConfirmer(y.isConfirmer());
            f.setPrenom(y.getPrenom());
            f.setActif(y.isActif());
            f.setSuiviComptable(y.isSuiviComptable());
            f.setSeuilSolde(y.getSeuilSolde());
            f.setVenteOnline(y.isVenteOnline());
            f.setExclusForHome(y.isExclusForHome());
            f.setAuthor(currentUser);
            f.setCreateBy(UtilUsers.buildSimpleBeanUsers(y.getCreateBy()));
            f.setDateCreation(y.getDateCreation());
            f.setDateUpdate(new Date());
            YvsBasePointLivraison pl = UtilCom.buildPointLivraison(client.getLigne(), currentAgence.getSociete(), currentUser);
            f.setLigne(pl != null ? pl.getId() > 0 ? pl : null : null);
        }
        return f;
    }

    public YvsComCategorieTarifaire buildTarifaire(CategorieTarifaire y) {
        YvsComCategorieTarifaire c = UtilCom.buildTarifaire(y, currentUser);
        c.setClient(selectClient);
        if (y.getCategorie() != null ? y.getCategorie().getId() > 0 : false) {
            ManagedCategorieClt w = (ManagedCategorieClt) giveManagedBean(ManagedCategorieClt.class);
            if (w != null) {
                int idx = w.getCategories().indexOf(new YvsBaseCategorieClient(y.getCategorie().getId()));
                if (idx > -1) {
                    c.setCategorie(w.getCategories().get(idx));
                }
            }
        }
        return c;
    }

    @Override
    public Client recopieView() {
        Client r = new Client();
        r.setId(client.getId());
        r.setCategorieComptable(client.getCategorieComptable());
        r.setDefaut(client.isDefaut());
        r.setNom(client.getNom());
        r.setPrenom(client.getPrenom());
        r.setCodeClient(client.getCodeClient());
        r.setCompteCollectif(client.getCompteCollectif());
        r.setActif(client.isActif());
        r.setModel(client.getModel());
        r.setRistourne(client.getRistourne());
        r.setSuiviComptable(client.isSuiviComptable());
        r.setSeuilSolde(client.getSeuilSolde());
        r.setRepresentant(client.getRepresentant());
        r.setCreateBy(client.getCreateBy());
        r.setDateCreation(client.getDateCreation());
        r.setDateUpdate(client.getDateUpdate());
        return r;
    }

    public CategorieTarifaire recopieViewTarifaire() {
        CategorieTarifaire c = new CategorieTarifaire();
        c.setId(tarifaire.getId());
        c.setPriorite(tarifaire.getPriorite());
        c.setDateDebut(tarifaire.getDateDebut());
        c.setDateFin(tarifaire.getDateFin());
        c.setActif(tarifaire.isActif());
        c.setClient(tarifaire.getClient());
        if (tarifaire.getCategorie() != null ? tarifaire.getCategorie().getId() > 0 : false) {
            c.setCategorie(tarifaire.getCategorie());
        } else {
            c.setCategorie(new CategorieClient(idCategorie));
        }
        c.setPermanent(tarifaire.isPermanent());
        c.setUpdate(tarifaire.isUpdate());
        return c;
    }

    public Tiers recopieViewTiers() {
        return recopieViewTiers(client);
    }

    public Tiers recopieViewTiers(Client client) {
        Tiers r = new Tiers();
        r.setId(client.getTiers().getId());
        if (!client.getTiers().isUpdate()) {
            r.setCodeTiers(client.getTiers().getCodeTiers().equals("") ? client.getCodeClient() : client.getTiers().getCodeTiers());
        } else {
            r.setCodeTiers(client.getTiers().getCodeTiers());
        }
        r.setAgence(client.getTiers().getAgence());
        r.setNom(client.getTiers().isUpdate() ? client.getTiers().getNom() : client.getNom());
        r.setPrenom(client.getTiers().isUpdate() ? client.getTiers().getPrenom() : client.getPrenom());
        if (!(r.getNom() == null || r.getNom().trim().equals(""))) {
            if (!(r.getPrenom() == null || r.getPrenom().trim().equals(""))) {
                r.setNom_prenom(r.getPrenom() + " " + r.getNom());
            } else {
                r.setNom_prenom(r.getNom());
            }
        } else {
            if (!(r.getPrenom() == null || r.getPrenom().trim().equals(""))) {
                r.setNom_prenom(r.getPrenom());
            } else {
                r.setNom_prenom("");
            }
        }
        r.setCodePostal(client.getTiers().getCodePostal());
        r.setCivilite(client.getTiers().getCivilite());
        r.setAdresse(client.getTiers().getAdresse());
        r.setEmail(client.getTiers().getEmail());
        r.setSecteur(client.getTiers().getSecteur());
        r.setVille(client.getTiers().getVille());
        r.setPays(client.getTiers().getPays());
        r.setBp(client.getTiers().getBp());
        r.setLogo(client.getTiers().getLogo());
        r.setResponsable(client.getTiers().getResponsable());
        r.setTelephone(client.getTiers().getTelephone());
        r.setActif(client.getTiers().isUpdate() ? client.getTiers().isActif() : true);

        r.setCompte(client.getTiers().getCompte());
        r.setCompteCollectif(client.getTiers().getCompteCollectif());
        r.setCategorieComptable(isUpdateClient() ? client.getTiers().getCategorieComptable() : client.getCategorieComptable());
        r.setModelDeRglt(client.getTiers().getModelDeRglt());
        r.setPlanComission(client.getTiers().getPlanComission());
        r.setPlanRistourne(client.getTiers().getPlanRistourne());

        r.setSociete(client.getTiers().isSociete());
        r.setClient(true);
        r.setFournisseur(client.getTiers().isFournisseur());
        r.setRepresentant(client.getTiers().isRepresentant());
        r.setFabricant(client.getTiers().isFabricant());
        r.setSite(client.getTiers().getSite());

        if (r.isSociete()) {
            r.setType("(Societe)");
        }
        if (r.isFournisseur()) {
            if ((r.getType() != null) ? r.getType().trim().equals("") : true) {
                r.setType("Fournisseur");
            } else {
                r.setType(r.getType() + " Fournisseur");
            }
        }
        if (r.isClient()) {
            if ((r.getType() != null) ? r.getType().trim().equals("") : true) {
                r.setType("Client");
            } else {
                r.setType(r.getType() + " Client");
            }
        }
        if (r.isRepresentant()) {
            if ((r.getType() != null) ? r.getType().trim().equals("") : true) {
                r.setType("Representant");
            } else {
                r.setType(r.getType() + " Representant");
            }
        }
        if (r.isFabricant()) {
            if ((r.getType() != null) ? r.getType().trim().equals("") : true) {
                r.setType("Fabriquant");
            } else {
                r.setType(r.getType() + " Fabriquant");
            }
        }
        r.setUpdate(client.getTiers().isUpdate());
        return r;
    }

    @Override
    public boolean controleFiche(Client bean) {
        return controleFiche(bean, true);
    }

    public boolean controleFiche(Client bean, boolean controle) {
        if ((bean.getTiers() != null) ? bean.getTiers().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le tiers");
            return false;
        }
        if (bean.getCodeClient() == null || bean.getCodeClient().trim().equals("")) {
            getErrorMessage("Vous devez entrer le code client");
            return false;
        }
        if (controle) {
            if ((bean.getCategorieComptable() != null) ? bean.getCategorieComptable().getId() < 1 : false) {
                getErrorMessage("Vous devez entrer la catégorie comptable");
                return false;
            }
        }
        if (bean.isDefaut()) {
            YvsComClient y = searchClientDefaut(bean);
            if (y != null ? y.getId() > 0 : false) {
                return false;
            }
        }
        YvsComClient t = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findByCode", new String[]{"code", "societe"}, new Object[]{bean.getCodeClient(), currentAgence.getSociete()});
        if (t != null ? (t.getId() > 0 ? !t.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez déja crée un client avec ce code");
            return false;
        }
        return true;
    }

    public boolean controleFicheTarifaire(CategorieTarifaire bean) {
        if ((selectClient != null) ? selectClient.getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le client");
            return false;
        }
        if ((bean.getCategorie() != null) ? bean.getCategorie().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier la catégorie");
            return false;
        }
        if (bean.getPriorite() < 1) {
            getErrorMessage("Vous precisez la priorité");
            return false;
        }
        champ = new String[]{"client", "categorie"};
        val = new Object[]{selectClient, new YvsBaseCategorieClient(bean.getCategorie().getId())};
        nameQueri = "YvsComCategorieTarifaire.findByClientCategorie";
        List<YvsComCategorieTarifaire> l = dao.loadNameQueries(nameQueri, champ, val);
        if (l != null ? !l.isEmpty() : false) {
            for (YvsComCategorieTarifaire c : l) {
                if (!c.getId().equals(bean.getId())) {
                    getErrorMessage("Vous avez deja attribuez cette catégorie");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void populateView(Client bean) {
        cloneObject(client, bean);
        setUpdateClient(true);
    }

    public void populateViewTarifaire(CategorieTarifaire bean) {
        cloneObject(tarifaire, bean);
        idCategorie = tarifaire.getCategorie() != null ? tarifaire.getCategorie().getId() : 0;
    }

    @Override
    public void resetFiche() {
        Tiers tiers = new Tiers();
        ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (w != null) {
            tiers = w.resetTiers(client.getTiers());
        }
        resetFiche(client);
        client.setCategorieComptable(new CategorieComptable());
        client.setCompteCollectif(new Comptes());
        client.setModel(new ModelReglement());
        client.setRistourne(new PlanRistourne());
        client.setDefaut(false);
        client.setRepresentant(new Users());
        client.setSuiviComptable(true);
        client.setTiers(tiers);
        client.setLigne(new PointLivraison());
        client.getTiers().setCivilite("M.");
        if (parametres != null ? !parametres.isEmpty() : false) {
            client.setSeuilSolde(parametres.get(0).getSeuilClient());
        }
        if (w != null) {
            w.resetFiche(false);
            w.resetFicheContact();
        }
        tabIds = "";
        selectClient = null;
        setUpdateClient(false);
        tarifaires.clear();
        generation = false;
        resetFicheTarifaire();
        resetFicheContrat();
        update("blog_form_client");
    }

    public void resetFicheTarifaire() {
        resetFiche(tarifaire);
        tarifaire.setClient(new Client());
        tarifaire.setCategorie(new CategorieClient());
        tarifaire.setPriorite(tarifaires.size() + 1);
        idCategorie = 0;
        selectTarifaire = new YvsComCategorieTarifaire();
        tabIds_tarifaire = "";
    }

    public void resetFicheContrat() {
        contrat = new ContratsClient();
        selectContrat = new YvsComContratsClient();
        resetFicheAdditionnel();
        resetFicheElement();
        update("tab_view_client:form-contrat_client");
    }

    public void resetFicheElement() {
        element = new ElementContratsClient();
        selectElement = new YvsComElementContratsClient();
        update("tabview-contrat_client:form-element_contrat_client");
    }

    public void resetFicheAdditionnel() {
        additionnel = new ElementAddContratsClient();
        selectAdditionnel = new YvsComElementAddContratsClient();
        update("tabview-contrat_client:form-additionnel_contrat_client");
    }

    @Override
    public boolean saveNew() {
        selectClient = saveNew(client);
        if (selectClient != null ? selectClient.getId() > 0 : false) {
            succes();
            actionOpenOrResetAfter(this);
            update("data_client");
            update("data_tiers_client");
            return true;
        }
        return false;
    }

    public YvsComClient saveNew(Client client) {
        YvsBaseTiers y = null;
        ManagedTiers service = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (service != null) {
            y = service.saveNew(recopieViewTiers(client));
        }
        return saveNew(client, y, true);
    }

    public YvsComClient saveNew(Client client, YvsBaseTiers y, boolean controle) {
        String action = isUpdateClient() ? "Modification" : "Insertion";
        try {
            if (y != null ? y.getId() > 0 : false) {
                Tiers tiers = UtilTiers.buildBeanTiers(y);
                client.setTiers(tiers);
                if (controleFiche(client, controle)) {
                    YvsComClient en = buildClient(client, y);
                    if (!isUpdateClient()) {
                        if (!autoriser("base_client_save")) {
                            openNotAcces();
                            return null;
                        }
                        en.setCreateBy(currentUser.getUsers());
                        en.setDateCreation(new Date());
                        en.setDateUpdate(new Date());
                        en.setId(null);
                        en = (YvsComClient) dao.save1(en);
                        client.setId(en.getId());
                        clients.add(0, en);
                    } else {
                        if (!autoriser("base_client_update")) {
                            openNotAcces();
                            return null;
                        }
                        en.setDateUpdate(new Date());
                        dao.update(en);
                        int idx = clients.indexOf(en);
                        if (idx >= 0) {
                            clients.set(idx, en);
                        }
                    }
                    setUpdateClient(true);
                    client.setTiers(tiers);
                    return en;
                }
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return null;
        }
        return null;
    }

    public void saveNewAll() {
        selectClient = saveNew(client);
        if (selectClient != null ? selectClient.getId() > 0 : false) {
            String telephone = client.getTiers().getTelephone();
            if (telephone != null ? telephone.trim().length() > 0 : false) {
                String[] telephones = telephone.split("/");
                for (String t : telephones) {
                    ManagedTiers m = (ManagedTiers) giveManagedBean(ManagedTiers.class);
                    if (m != null) {
                        m.setSelectTiers(selectClient.getTiers());
                        m.saveNewContact(new Contact(t.trim()));
                    }
                }
            }
            succes();
            resetFiche();
        }
    }

    public YvsComClient saveDefautClient() {
        try {
            YvsBaseTiers t = new YvsBaseTiers();
            t.setActif(true);
            t.setSociete(currentAgence.getSociete());
            t.setClient(true);
            t.setCodeTiers("CLTDIV");
            t.setCivilite("M.");
            t.setNom("CLIENT");
            t.setPrenom("DIVERS");
            t.setAuthor(currentUser);
            if (currentAgence != null ? currentAgence.getId() > 0 : false) {
                if (currentAgence.getVille() != null ? currentAgence.getVille().getId() > 0 : false) {
                    t.setVille(currentAgence.getVille());
                    t.setCodeTiers(currentAgence.getVille().getAbreviation() + "DIV");
                    t.setPays(currentAgence.getVille().getParent());
                } else {
                    getWarningMessage("Cette agence n'a pas de ville");
                    return null;
                }
            } else {
                getWarningMessage("Vous n'etes pas connecté à une agence");
                return null;
            }
            ManagedTiers st = (ManagedTiers) giveManagedBean(ManagedTiers.class);
            if (st != null) {
                t.setCodeTiers(st.checkCode(t.getCodeTiers(), 0));
            }
            t = (YvsBaseTiers) dao.save1(t);

            YvsComClient e = new YvsComClient();
            e.setTiers(t);
            e.setNom("CLIENT");
            e.setPrenom("DIVERS");
            e.setCodeClient(t.getCodeTiers());
            e.setDefaut(true);
            e.setActif(true);
            e.setAuthor(currentUser);
            ManagedClient sc = (ManagedClient) giveManagedBean(ManagedClient.class);
            if (sc != null) {
                e.setCodeClient(sc.checkCode(t.getCodeTiers(), 0, false));
            }
            e = (YvsComClient) dao.save1(e);
            if (sc != null) {
                sc.getClients().add(0, e);
            }

            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
            nameQueri = "YvsBaseCategorieClient.findDefault";
            YvsBaseCategorieClient c = (YvsBaseCategorieClient) dao.loadOneByNameQueries(nameQueri, champ, val);
            if ((c != null) ? c.getId() > 0 : false) {
                YvsComCategorieTarifaire y = new YvsComCategorieTarifaire();
                y.setActif(true);
                y.setAuthor(currentUser);
                y.setCategorie(c);
                y.setClient(e);
                y.setPermanent(true);
                y.setPriorite(1);
                dao.save(y);
            }
            return e;
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            System.err.println("Error Insertion : " + ex.getMessage());
            return null;
        }
    }

    public boolean saveNewTarifaire() {
        String action = tarifaire.isUpdate() ? "Modification" : "Insertion";
        try {
            if (!autoriser("base_client_associer_tarifaire")) {
                openNotAcces();
                return false;
            }
            CategorieTarifaire bean = recopieViewTarifaire();
            if (controleFicheTarifaire(bean)) {
                YvsComCategorieTarifaire entity = buildTarifaire(bean);
                if (!bean.isUpdate()) {
                    entity.setId(null);
                    dao.save1(entity);
                    tarifaires.add(0, entity);
                    selectClient.getCategories().add(0, entity);
                } else {
                    dao.update(entity);
                    tarifaires.set(tarifaires.indexOf(entity), entity);
                }
                int idx = clients.indexOf(selectClient);
                if (idx > -1) {
                    clients.set(idx, selectClient);
                }
                succes();
                resetFicheTarifaire();
                update("data_client_tarifaire");
                update("data_client");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewContrat() {
        String action = contrat.getId() > 0 ? "Modification" : "Insertion";
        try {
            IYvsComContratsClient impl = (IYvsComContratsClient) IEntitiSax.createInstance("IYvsComContratsClient", dao);
            if (impl != null) {
                contrat.setClient(client);
                selectContrat = UtilCom.buildContratsClient(contrat, currentUser);
                ResultatAction result;
                if (contrat.getId() < 1) {
                    result = impl.save(selectContrat);
                } else {
                    result = impl.update(selectContrat);
                }
                if (result != null) {
                    if (result.isResult()) {
                        contrat.setId(selectContrat.getId());
                        int idx = client.getContrats().indexOf(selectContrat);
                        if (idx > -1) {
                            client.getContrats().set(idx, selectContrat);
                        } else {
                            client.getContrats().add(0, selectContrat);
                        }
                        succes();
                        update("tab_view_client:data-contrat_client");
                    } else {
                        getErrorMessage(result.getMessage());
                    }
                } else {
                    getErrorMessage(action + " Impossible !");
                }
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewElementContrat() {
        String action = element.getId() > 0 ? "Modification" : "Insertion";
        try {
            IYvsComElementContratsClient impl = (IYvsComElementContratsClient) IEntitiSax.createInstance("IYvsComElementContratsClient", dao);
            if (impl != null) {
                element.setContrat(new ContratsClient(selectContrat.getId()));
                selectElement = UtilCom.buildElementContratsClient(element, currentUser);
                ResultatAction result;
                if (element.getId() < 1) {
                    result = impl.save(selectElement);
                } else {
                    result = impl.update(selectElement);
                }
                if (result != null) {
                    if (result.isResult()) {
                        element.setId(selectElement.getId());
                        int idx = selectContrat.getElements().indexOf(selectElement);
                        if (idx > -1) {
                            selectContrat.getElements().set(idx, selectElement);
                        } else {
                            selectContrat.getElements().add(0, selectElement);
                        }
                        idx = client.getContrats().indexOf(selectContrat);
                        if (idx > -1) {
                            client.getContrats().set(idx, selectContrat);
                        }
                        resetFicheElement();
                        update("tabview-contrat_client:data-element_contrat_client");
                        succes();
                    } else {
                        getErrorMessage(result.getMessage());
                    }
                } else {
                    getErrorMessage(action + " Impossible !");
                }
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewAdditionnelContrat() {
        String action = additionnel.getId() > 0 ? "Modification" : "Insertion";
        try {
            IYvsComElementAddContratsClient impl = (IYvsComElementAddContratsClient) IEntitiSax.createInstance("IYvsComElementAddContratsClient", dao);
            if (impl != null) {
                additionnel.setContrat(new ContratsClient(selectContrat.getId()));
                selectAdditionnel = UtilCom.buildElementAddContratsClient(additionnel, currentUser);
                ResultatAction result;
                if (additionnel.getId() < 1) {
                    result = impl.save(selectAdditionnel);
                } else {
                    result = impl.update(selectAdditionnel);
                }
                if (result != null) {
                    if (result.isResult()) {
                        additionnel.setId(selectAdditionnel.getId());
                        int idx = selectContrat.getAdditionnels().indexOf(selectAdditionnel);
                        if (idx > -1) {
                            selectContrat.getAdditionnels().set(idx, selectAdditionnel);
                        } else {
                            selectContrat.getAdditionnels().add(0, selectAdditionnel);
                        }
                        idx = client.getContrats().indexOf(selectContrat);
                        if (idx > -1) {
                            client.getContrats().set(idx, selectContrat);
                        }
                        resetFicheAdditionnel();
                        update("tabview-contrat_client:data-additionnel_contrat_client");
                        succes();
                    } else {
                        getErrorMessage(result.getMessage());
                    }
                } else {
                    getErrorMessage(action + " Impossible !");
                }
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
        deleteBean(false);
    }

    public void deleteBean(boolean deleteTiers) {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsComClient> list = new ArrayList<>();
                YvsComClient bean;
                for (Long ids : l) {
                    try {
                        bean = clients.get(ids.intValue());
                        bean.setAuthor(currentUser);
                        bean.setDateUpdate(new Date());
                        boolean succes = dao.delete(bean);
                        if (succes) {
                            String rq = "UPDATE yvs_base_tiers SET client = false WHERE id=?";
                            if (deleteTiers) {
                                rq = "DELETE FROM yvs_base_tiers WHERE id=?";
                            }
                            Options[] param = new Options[]{new Options(bean.getTiers().getId(), 1)};
                            dao.requeteLibre(rq, param);
                            if (bean.getId() == client.getId()) {
                                resetFiche();
                            }
                            list.add(bean);
                        }
                    } catch (Exception ex) {
                        getException("Error Suppression : " + ex.getMessage(), ex);
                    }
                }
                clients.removeAll(list);
                succes();
                update("data_client");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsComClient y) {
        selectClient = y;
    }

    public void deleteBean_() {
        _deleteBean(false);
    }

    public void _deleteBean(boolean deleteTiers) {
        try {
            if (!autoriser("base_client_delete")) {
                openNotAcces();
                return;
            }
            if (selectClient != null) {
                dao.delete(selectClient);
                clients.remove(selectClient);
                String rq = "UPDATE yvs_base_tiers SET client = false WHERE id=?";
                if (deleteTiers && autoriser("base_tiers_delete")) {
                    rq = "DELETE FROM yvs_base_tiers WHERE id=?";
                }
                Options[] param = new Options[]{new Options(selectClient.getTiers().getId(), 1)};
                dao.requeteLibre(rq, param);
                if (selectClient.getId() == client.getId()) {
                    resetFiche();
                }
                succes();
                update("data_client");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanTarifaire() {
        try {
            if ((tabIds_tarifaire != null) ? !tabIds_tarifaire.equals("") : false) {
                String[] tab = tabIds_tarifaire.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComCategorieTarifaire bean = tarifaires.get(tarifaires.indexOf(new YvsComCategorieTarifaire(id)));
                    dao.delete(bean);
                    tarifaires.remove(bean);
                    selectClient.getCategories().remove(bean);
                    if (tarifaire.getId() == id) {
                        resetFicheTarifaire();
                    }
                }
                int idx = clients.indexOf(selectClient);
                if (idx > -1) {
                    clients.set(idx, selectClient);
                }
                succes();
                resetFicheTarifaire();
                update("data_client");
                update("data_client_tarifaire");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanTarifaire_(YvsComCategorieTarifaire y) {
        selectTarifaire = y;
    }

    public void deleteBeanTarifaire_() {
        try {
            if (!autoriser("base_client_associer_tarifaire")) {
                openNotAcces();
                return;
            }
            if (selectTarifaire != null) {
                dao.delete(selectTarifaire);
                tarifaires.remove(selectTarifaire);
                selectClient.getCategories().remove(selectTarifaire);
                int idx = clients.indexOf(selectClient);
                if (idx > -1) {
                    clients.set(idx, selectClient);
                }
                if (tarifaire.getId() == selectTarifaire.getId()) {
                    resetFicheTarifaire();
                }
                resetFicheTarifaire();
                succes();
                update("data_client");
                update("data_client_tarifaire");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanContrat(YvsComContratsClient y, boolean delete) {
        try {
            if (y != null) {
                if (!delete) {
                    selectContrat = y;
                } else {
                    dao.delete(y);
                    client.getContrats().remove(y);
                    if (contrat.getId() == y.getId()) {
                        resetFicheContrat();
                    }
                    succes();
                    update("tab_view_client:data-contrat_client");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanElement(YvsComElementContratsClient y, boolean delete) {
        try {
            if (y != null) {
                if (!delete) {
                    selectElement = y;
                } else {
                    dao.delete(y);
                    selectContrat.getElements().remove(y);
                    int idx = client.getContrats().indexOf(selectContrat);
                    if (idx > -1) {
                        client.getContrats().set(idx, selectContrat);
                    }
                    if (element.getId() == y.getId()) {
                        resetFicheElement();
                    }
                    succes();
                    update("tabview-contrat_client:data-element_contrat_client");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanAdditionnel(YvsComElementAddContratsClient y, boolean delete) {
        try {
            if (y != null) {
                if (!delete) {
                    selectAdditionnel = y;
                } else {
                    dao.delete(y);
                    selectContrat.getAdditionnels().remove(y);
                    int idx = client.getContrats().indexOf(selectContrat);
                    if (idx > -1) {
                        client.getContrats().set(idx, selectContrat);
                    }
                    if (additionnel.getId() == y.getId()) {
                        resetFicheAdditionnel();
                    }
                    succes();
                    update("tabview-contrat_client:data-additionnel_contrat_client");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void onSelectObject(YvsComClient bean) {
        selectClient = bean;
        populateView(UtilCom.buildBeanClient(bean));
        if (bean.getTiers() != null) {
            ManagedTiers m = (ManagedTiers) giveManagedBean(ManagedTiers.class);
            if (m != null) {
                m.setSelectTiers(bean.getTiers());
                m.loadContact(bean.getTiers());
                m.resetFicheContact();
            }
            if (client.getNom() != null ? client.getNom().trim().length() < 1 : true) {
                client.setNom(bean.getTiers().getNom());
            }
            if (client.getPrenom() != null ? client.getPrenom().trim().length() < 1 : true) {
                client.setPrenom(bean.getTiers().getPrenom());
            }
            ManagedDico d = (ManagedDico) giveManagedBean("Mdico");
            if (d != null) {
                d.loadVilles(bean.getTiers().getPays());
                d.loadSecteurs(bean.getTiers().getVille());
            }
            ManagedTiersDocument w = (ManagedTiersDocument) giveManagedBean(ManagedTiersDocument.class);
            if (w != null) {
                w.loadAll(client.getTiers());
            }
        }
        codeExterne = (bean.getCodeExterne() != null) ? bean.getCodeExterne().getCodeExterne() : null;
        generation = false;
        selectTemplate = null;
        changeDefaut();
        loadTarifaire(bean);
        resetFicheTarifaire();

        champ = new String[]{"client"};
        val = new Object[]{bean};
        nameQueri = "YvsComContratsClient.findByClient";
        client.setContrats(dao.loadNameQueries(nameQueri, champ, val));

        nameQueri = "YvsComDocVentes.findFactureImpayeByClient";
        client.setFactures(dao.loadNameQueries(nameQueri, champ, val));

        nameQueri = "YvsComptaCaissePieceVente.findByAcompteClient";
        client.setAcomptes(dao.loadNameQueries(nameQueri, champ, val));

        nameQueri = "YvsComptaCaissePieceVente.findByCreanceClient";
        client.setCreances(dao.loadNameQueries(nameQueri, champ, val));

        champ = new String[]{"client", "statut", "typeDoc"};
        val = new Object[]{bean, Constantes.ETAT_VALIDE, Constantes.TYPE_FV};
        nameQueri = "YvsComContenuDocVente.findByClientTypeStatut";
        client.setTransactions(dao.loadNameQueries(nameQueri, champ, val, 0, 5));

        creance(bean);
        client.setCredit(bean.getCredit());
        client.setDebit(bean.getDebit());
        client.setSolde(bean.getSolde());
        client.setCreance(bean.getCreance());
        client.setAcompte(bean.getAcompte());
        if (bean.getDebit() > 0) {
            client.setAfficheSolde(true);
        }

        client.setSoldes(dao.loadNameQueries("YvsBaseExercice.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()}));
        for (YvsBaseExercice e : client.getSoldes()) {
            champ = new String[]{"client", "dateDebut", "dateFin"};
            val = new Object[]{bean, e.getDateDebut(), e.getDateFin()};
            nameQueri = "YvsComContenuDocVente.findTTCByClientDates";
            Double d = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            e.setCa((d != null ? d : 0));
            nameQueri = "YvsComptaCaissePieceVente.findAvanceByClientDates";
            Double c = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            e.setAvance((c != null ? c : 0));
            nameQueri = "YvsComptaCaissePieceVente.findCreanceByClientDates";
            Double s = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            e.setCreance(s != null ? s : 0);
            nameQueri = "YvsComptaCaissePieceVente.findAcompteByClientDates";
            s = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            e.setAcompte(s != null ? s : 0);
            e.setValeur((e.getAcompte() + e.getAvance()) - (e.getCa() + e.getCreance()));
        }
        champ = new String[]{"client", "typeDoc"};
        val = new Object[]{bean, Constantes.TYPE_FV};
        nameQueri = "YvsComDocVentes.findByClientTypeDoc";
        List<YvsComDocVentes> l = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
        if (l != null ? !l.isEmpty() : false) {
            client.setLastFacture(l.get(0));
        }
        update("blog_form_client");
    }

    @Override
    public void onSelectDistant(YvsComClient y) {
        Navigations n = (Navigations) giveManagedBean(Navigations.class);
        if (n != null) {
            n.naviguationView("Clients", "modDonneBase", "smenClientCom", true);
        }
        onSelectObject(y);
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComClient bean = (YvsComClient) ev.getObject();
            onSelectObject(bean);
            execute("collapseForm('client')");
            execute("collapseForm('client_contact')");
            execute("collapseForm('client_tarif')");
            execute("collapseForm('client_tarif')");
            execute("collapseForm('client_tarifaire')");
            tabIds = clients.indexOf(bean) + "";

        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("blog_form_client");
    }

    public void loadOnViewTemplate(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectTemplate = (YvsBaseTiersTemplate) ev.getObject();
            ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
            if (m != null) {
                m.loadVilles(selectTemplate.getPays());
                m.loadSecteurs(selectTemplate.getVille());
            }
            copie(selectTemplate);
            update("infos_tiers_client");
        }
    }

    public void onSelectDistantFacture(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectVente = (YvsComDocVentes) ev.getObject();
        }
    }
    YvsComDocVentes selectVente;

    public void _onSelectDistantFacture() {
        if (selectVente != null ? selectVente.getId() > 0 : false) {
            ManagedFactureVente s = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
            if (s != null) {
                s.onSelectDistant(selectVente);
            }
        }
    }

    public void unLoadOnViewTemplate(UnselectEvent ev) {
        client.getTiers().setTemplate(null);
        selectTemplate = null;
        update("txt_code_tiers");
    }

    public void loadOnViewTarifaire(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComCategorieTarifaire bean = (YvsComCategorieTarifaire) ev.getObject();
            selectTarifaire = bean;
            populateViewTarifaire(UtilCom.buildBeanCategorieTarifaire(bean));
            update("form_client_tarifaire");
        }
    }

    public void unLoadOnViewTarifaire(UnselectEvent ev) {
        resetFicheTarifaire();
        update("form_client_tarifaire");
    }

    public void loadOnViewContrat(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectContrat = (YvsComContratsClient) ev.getObject();
            contrat = UtilCom.buildBeanContratsClient(selectContrat);
        }
    }

    public void unLoadOnViewContrat(UnselectEvent ev) {
        resetFicheContrat();
    }

    public void loadOnViewElement(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectElement = (YvsComElementContratsClient) ev.getObject();
            element = UtilCom.buildBeanElementContratsClient(selectElement);
            if (element.getArticle().getArticle().getConditionnements() != null ? element.getArticle().getArticle().getConditionnements().isEmpty() : true) {
                element.getArticle().getArticle().setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(element.getArticle().getArticle().getId())}));
            }
        }
    }

    public void unLoadOnViewElement(UnselectEvent ev) {
        resetFicheElement();
    }

    public void loadOnViewAdditionnel(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectAdditionnel = (YvsComElementAddContratsClient) ev.getObject();
            additionnel = UtilCom.buildBeanElementAddContratsClient(selectAdditionnel);
        }
    }

    public void unLoadOnViewAdditionnel(UnselectEvent ev) {
        resetFicheAdditionnel();
    }

    public void loadOnViewTiers(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseTiers y = (YvsBaseTiers) ev.getObject();
            Tiers bean = UtilTiers.buildBeanTiers(y);
            chooseTiers(bean);
        }
    }

    public void loadOnViewCompte(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBasePlanComptable bean = (YvsBasePlanComptable) ev.getObject();
            client.setCompteCollectif(UtilCompta.buildBeanCompte(bean));
            update("txt_compte_client");
        }
    }

    public void loadOnViewCategorie(SelectEvent ev) {
        if (ev != null) {
            YvsBaseCategorieClient y = (YvsBaseCategorieClient) ev.getObject();
            CategorieClient bean = UtilCom.buildBeanCategorieClient(y);
            cloneObject(tarifaire.getCategorie(), bean);
            idCategorie = y.getId();
            update("txt_categorie_client_client");
        }
    }

    public void loadOnViewModel(SelectEvent ev) {
        if (ev != null) {
            YvsBaseModelReglement y = (YvsBaseModelReglement) ev.getObject();
            ModelReglement m = UtilCom.buildBeanModelReglement(y);
            client.setModel(m);
            update("txt_model_client");
        }
    }

    public void loadOnViewRepr(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsUsers bean = (YvsUsers) ev.getObject();
            client.setRepresentant(UtilUsers.buildBeanUsers(bean));
            update("txt_repr_client");
        }
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            chooseArticle(UtilProd.buildBeanArticles(bean));
        }
    }

    public void searchTiers() {
        String num = client.getTiers().getCodeTiers();
        client.getTiers().setId(0);
        client.getTiers().setError(true);
        ManagedTiers m = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (m != null) {
            Tiers y = m.findTiers(num, false);
            if (m.getListTiers() != null ? !m.getListTiers().isEmpty() : false) {
                if (m.getListTiers().size() > 1) {
                    update("data_tiers_client");
                } else {
                    chooseTiers(y);
                }
                client.getTiers().setError(false);
            }
        }
        if (!Util.asString(client.getCodeClient())) {
            client.setCodeClient(num);
            update("txt_code_client");
        }
    }

    public void searchRepresentant() {
        String num = client.getRepresentant().getCodeUsers();
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedUser m = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (m != null) {
                Users e = m.searchUsersActif(num, true);
                client.setRepresentant(e);
                if (m.getListUser() != null ? m.getListUser().size() > 1 : false) {
                    update("data_repr_client");
                }
            }
        }
    }

    public void initRepresentants() {
        ManagedUser m = (ManagedUser) giveManagedBean(ManagedUser.class);
        if (m != null) {
            m.initUsers(client.getRepresentant());
            update("data_repr_client");
        }
    }

    public void chooseTiers(Tiers bean) {
        client.setCodeClient(bean.getCodeTiers());
        cloneObject(client.getTiers(), bean);
        cloneObject(client.getCompteCollectif(), bean.getCompte());
        if (client.getNom() != null ? client.getNom().trim().length() < 1 : true) {
            client.setNom(bean.getNom());
        }
        if (client.getPrenom() != null ? client.getPrenom().trim().length() < 1 : true) {
            client.setPrenom(bean.getPrenom());
        }
        ManagedTiers m = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (m != null) {
            YvsBaseTiers y = new YvsBaseTiers(bean.getId());
            m.setSelectTiers(y);
            ManagedDico d = (ManagedDico) giveManagedBean("Mdico");
            if (d != null) {
                d.loadVilles(new YvsDictionnaire(bean.getPays().getId()));
                d.loadSecteurs(new YvsDictionnaire(bean.getVille().getId()));
            }
            m.loadContact(y);
        }
        update("chk_societe_client");
        update("txt_code_tiers_client");
        update("infos_tiers_client");
    }

    public void choosePays() {
        update("txt_pays_ville_0");
        update("txt_pays_secteur_0");
        client.getTiers().setVille(new Dictionnaire());
        client.getTiers().setSecteur(new Dictionnaire());
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            Dictionnaire d = m.choosePays(client.getTiers().getPays().getId());
            if (d != null ? d.getId() > 0 : false) {
                cloneObject(client.getTiers().getPays(), d);
            }
        }
    }

    public void chooseVille() {
        update("txt_ville_secteur_0");
        client.getTiers().setSecteur(new Dictionnaire());
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            Dictionnaire d = m.chooseVille(client.getTiers().getPays(), client.getTiers().getVille().getId());
            if (d != null ? d.getId() > 0 : false) {
                cloneObject(client.getTiers().getVille(), d);
            }
        }
    }

    public void chooseSecteur() {
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            Dictionnaire d = m.chooseSecteur(client.getTiers().getVille(), client.getTiers().getSecteur().getId());
            if (d != null ? d.getId() > 0 : false) {
                cloneObject(client.getTiers().getSecteur(), d);
            }
        }
    }

    public void chooseConditionnement() {
        if (element.getArticle() != null ? element.getArticle().getId() > 0 : false) {
            int index = element.getArticle().getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(element.getArticle().getId()));
            if (index > -1) {
                element.setArticle(UtilProd.buildBeanConditionnement(element.getArticle().getArticle().getConditionnements().get(index)));
                element.setPrix(element.getArticle().getPrix());
            }
        }
    }

    public void chooseTypeCout() {
        if (additionnel.getTypeCout() != null ? additionnel.getTypeCout().getId() > 0 : false) {
            ManagedTypeCout w = (ManagedTypeCout) giveManagedBean(ManagedTypeCout.class);
            if (w != null) {
                int index = w.getTypes().indexOf(new YvsGrhTypeCout(additionnel.getTypeCout().getId()));
                if (index > -1) {
                    additionnel.setTypeCout(UtilGrh.buildBeanTypeCout(w.getTypes().get(index)));
                }
            }
        }
    }

    public void choosePoint() {
        if (point != null ? point.getId() > 0 : false) {
            int idx = points.indexOf(new YvsBasePointVente(point.getId()));
            if (idx > -1) {
                point = UtilCom.buildBeanPointVente(points.get(idx));
                point.setVendeurs(dao.loadNameQueries("YvsComCreneauHoraireUsers.findUsersByPoint", new String[]{"point"}, new Object[]{new YvsBasePointVente(point.getId())}));
            }
        }
    }

    public void chooseModel() {
        if (client.getModel() != null) {
            if (client.getModel().getId() > 0) {
                ManagedModeReglement w = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
                if (w != null) {
                    int index = w.getModels().indexOf(new YvsBaseModelReglement(client.getModel().getId()));
                    if (index > -1) {
                        client.setModel(UtilCom.buildBeanModelReglement(w.getModels().get(index)));
                    }
                }
            } else if (client.getModel().getId() == -1) {
                ManagedModeReglement w = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
                if (w != null) {
                    w.loadMode("C");
                    update("select_mode_tranche_model_client");
                }
                openDialog("dlgAddModelReglement");
            }
        }
    }

    public void chooseCategorieClt() {
        if (idCategorie == -1) {
            ManagedCategorieClt w = (ManagedCategorieClt) giveManagedBean(ManagedCategorieClt.class);
            if (w != null) {
                w.loadParents(null);
                update("parent_categorie");
            }
            openDialog("dlgAddCategorie");
        }
    }

    public void chooseArticle(Articles y) {
        if (y.getConditionnements() != null ? !y.getConditionnements().isEmpty() : false) {
            element.setArticle(UtilProd.buildBeanConditionnement(y.getConditionnements().get(0)));
            element.setPrix(element.getArticle().getPrix());
        }
        update("tabview-contrat_client:select-article_element_contrat_client");
        update("tabview-contrat_client:select-unite_element_contrat_client");
        update("tabview-contrat_client:value-prix_element_contrat_client");
    }

    public void searchArticle() {
        String num = element.getArticle().getArticle().getRefArt();
        element.getArticle().getArticle().setDesignation("");
        element.getArticle().getArticle().setError(true);
        element.getArticle().getArticle().setId(0);
        ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
        if (m != null) {
            Articles y = m.searchArticleActif(null, num, false);
            if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                if (m.getArticlesResult().size() > 1) {
                    openDialog("dlgListArticleElement");
                    update("data-articles_element_contrat_client");
                } else {
                    chooseArticle(y);
                }
                element.getArticle().getArticle().setError(false);
            }
        }
    }

    public void activeTarifaire(YvsComCategorieTarifaire bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            bean.setAuthor(currentUser);
            bean.setDateUpdate(new Date());
            dao.update(bean);
            int index = tarifaires.indexOf(bean);
            if (index > -1) {
                tarifaires.set(index, bean);
            }
        }
    }

    public void activeContrat(YvsComContratsClient y) {
        if (y != null) {
            y.setActif(!y.getActif());
            y.setDateUpdate(new Date());
            y.setAuthor(currentUser);
            dao.update(y);
            int index = client.getContrats().indexOf(y);
            if (index > -1) {
                client.getContrats().set(index, y);
            }
        }
    }

    public void activeTiers(YvsComClient bean) {
        if (bean != null) {
            if (!autoriser("base_client_update")) {
                openNotAcces();
                return;
            }
            bean.setActif(!bean.getActif());
            bean.setAuthor(currentUser);
            bean.setDateUpdate(new Date());
            dao.update(bean);
            if (actionTiers) {
                bean.getTiers().setActif(bean.getActif());
                bean.getTiers().setAuthor(currentUser);
                bean.getTiers().setDateUpdate(new Date());
                dao.update(bean.getTiers());
            }
            if (actionFournisseur) {
                for (YvsBaseFournisseur y : bean.getTiers().getFournisseurs()) {
                    y.setActif(bean.getActif());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                }
            }
            if (actionEmploye) {
                for (YvsGrhEmployes y : bean.getTiers().getEmployes()) {
                    y.setActif(bean.getActif());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                }
            }
            if (actionCommercial) {
                for (YvsComComerciale y : bean.getTiers().getCommerciaux()) {
                    y.setActif(bean.getActif());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                }
            }
            int index = clients.indexOf(bean);
            if (index > -1) {
                clients.set(index, bean);
                update("data_client");
            }
        }
    }

    public void confirmTiers(YvsComClient bean) {
        if (bean != null) {
            bean.setConfirmer(!bean.getConfirmer());
            bean.setAuthor(currentUser);
            bean.setDateUpdate(new Date());
            dao.update(bean);
            int index = clients.indexOf(bean);
            if (index > -1) {
                clients.set(index, bean);
                update("data_client");
            }
        }
    }

    public void activeGenerationCode() {
        if (!isGeneration() && (selectTemplate != null ? selectTemplate.getId() < 1 : true)) {
            getErrorMessage("Vous devez au préalable selectionner le template");
            return;
        }
        setGeneration(!isGeneration());
    }

    public void activeDefaut(YvsComClient bean) {
        if (!autoriser("base_client_update")) {
            openNotAcces();
            return;
        }
        if (bean != null) {
            if (!bean.getDefaut()) {
                YvsComClient y = searchClientDefaut(UtilCom.buildBeanClient(bean));
                if (y != null ? y.getId() > 0 : false) {
                    if (!y.getId().equals(bean.getId())) {
                        return;
                    }
                }
            }
            bean.setDefaut(!bean.getDefaut());
            clients.set(clients.indexOf(bean), bean);
            String rq = "UPDATE yvs_com_client SET defaut=" + bean.getDefaut() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
        }
    }

    public void changeDefaut() {
        if (client.isDefaut()) {
            YvsComClient y = searchClientDefaut(client);
            if (y != null ? y.getId() > 0 : false) {
                client.setDefaut(false);
                return;
            }
            if (client.isDefaut()) {
                client.getTiers().setSociete(false);
            }
        }
    }

    public YvsComClient searchClientDefaut(Client bean) {
        boolean in = false;
        if (bean.getTiers().getSecteur() != null ? bean.getTiers().getSecteur().getId() > 0 : false) {
            champ = new String[]{"societe", "secteur"};
            val = new Object[]{currentAgence.getSociete(), new YvsDictionnaire(bean.getTiers().getSecteur().getId())};
            nameQueri = "YvsComClient.findDefautSecteur";
            in = true;
        } else if (bean.getTiers().getVille() != null ? bean.getTiers().getVille().getId() > 0 : false) {
            champ = new String[]{"societe", "ville"};
            val = new Object[]{currentAgence.getSociete(), new YvsDictionnaire(bean.getTiers().getVille().getId())};
            nameQueri = "YvsComClient.findDefautVille";
            in = true;
        } else if (bean.getTiers().getPays() != null ? bean.getTiers().getPays().getId() > 0 : false) {
            champ = new String[]{"societe", "pays"};
            val = new Object[]{currentAgence.getSociete(), new YvsDictionnaire(bean.getTiers().getPays().getId())};
            nameQueri = "YvsComClient.findDefautPays";
            in = true;
        }
        if (in) {
            List<YvsComClient> list = dao.loadNameQueries(nameQueri, champ, val);
            if (list != null ? !list.isEmpty() : false) {
                for (YvsComClient c : list) {
                    if (!c.getId().equals(bean.getId())) {
                        getErrorMessage("Il y'a deja un client par défaut pour ce secteur");
                        return c;
                    }
                }
            }
        }
        return null;
    }

    public void initComptes() {
        comptesResult.clear();
        comptesResult.addAll(comptes);
        update("data_comptes_client");
    }

    public void initArticles() {
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            m.initArticles();
        }
        update("data_articles_client");
    }

    public void init(boolean next) {
        loadAllClient(next, false);
    }

    public void initGenererFacture(YvsComContratsClient y) {
        try {
            points = dao.loadNameQueries("YvsBasePointVente.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            selectContrat = y;
        } catch (Exception ex) {
            Logger.getLogger(ManagedClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void genererFacture() {
        try {
            if (selectContrat != null ? selectContrat.getId() > 0 : false) {
                IYvsComContratsClient impl = (IYvsComContratsClient) IEntitiSax.createInstance("IYvsComContratsClient", dao);
                if (impl != null) {
                    additionnel.setContrat(new ContratsClient(selectContrat.getId()));
                    selectAdditionnel = UtilCom.buildElementAddContratsClient(additionnel, currentUser);
                    ResultatAction result = impl.genereFacture(selectContrat, dateFacture, new YvsUsers(vendeur.getId()), new YvsBasePointVente(point.getId()), new YvsGrhTrancheHoraire(tranche.getId()), currentNiveau, currentAgence, currentUser);
                    if (result != null ? result.isResult() : false) {
                        succes();
                    } else {
                        getErrorMessage(result.getMessage());
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllClient(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllClient(true, true);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComClient> re = paginator.parcoursDynamicData("YvsComClient", "y", "y.tiers.ville.id, y.codeClient", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void saveLiaison() {
        if (client.getId() > 0) {
            if ((codeExterne != null) ? codeExterne.length() > 2 : false) {
                //il ne dois pas déjà exister un mappage pour ce client
                YvsExtClients y = (YvsExtClients) dao.loadOneByNameQueries("YvsExtClients.findByClient", new String[]{"client"}, new Object[]{new YvsComClient(client.getId())});
                if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                    y.setCodeExterne(codeExterne);
                    y.setDateSave(y.getDateSave());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                } else {
                    y = new YvsExtClients();
                    y.setClient(new YvsComClient(client.getId()));
                    y.setDateSave(new Date());
                    y.setAuthor(currentUser);
                    y.setCodeExterne(codeExterne);
                    y.setDateUpdate(new Date());
                    dao.save(y);
                }
                succes();
            } else {
                getErrorMessage("Veuillez entrer le code de liaison externe !");
            }
        } else {
            getErrorMessage("Aucun client n'est selectionné !");
        }
    }

    public void searchByNum() {
        addParamCode(numSearch_);
    }

    public void searchCompte() {
        String num = client.getCompteCollectif().getNumCompte();
        client.getCompteCollectif().setId(0);
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
            if (service != null) {
                service.findCompteByNum(num);
                client.getCompteCollectif().setError(service.getListComptes().isEmpty());
                if (service.getListComptes() != null ? !service.getListComptes().isEmpty() : false) {
                    if (service.getListComptes().size() > 1) {
                        openDialog("dlgListComptes");
                        update("data_comptes_client");
                    } else {
                        YvsBasePlanComptable c = service.getListComptes().get(0);
                        client.setCompteCollectif(UtilCompta.buildBeanCompte(c));
                    }
                    client.getCompteCollectif().setError(false);
                } else {
                    client.getCompteCollectif().setError(true);
                }
            }
        }
    }

    public void clearParams(boolean load) {
        typeSearch = null;
        paysSearch = 0;
        villeSearch = 0;
        secteurSearch = 0;
        numSearch_ = null;
        actifSearch = null;
        compteSearch = null;
        typeSearch = null;
        categorieSearch = 0;
        groupeSearch = 0;
        paginator.getParams().clear();
        _first = true;
        if (!load) {
            loadAllClient(true, true);
        }
        update("blog_plus_option_client");
    }

    public void addParamCode(String num) {
        ParametreRequete p = new ParametreRequete(null, "code", "client", "=", "AND");
        if (num != null ? num.trim().length() > 0 : false) {
            boolean _client = groupeSearch < 1;
            if (_client) {
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.codeClient)", "code", num.trim().toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.nom)", "nom", num.trim().toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.prenom)", "prenom", num.trim().toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.tiers.codeTiers)", "codeT", num.trim().toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(CONCAT(y.nom, ' ', y.prenom))", "nom_pre", num.trim().toUpperCase() + "%", "LIKE", "OR"));
            } else {
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.codeClient)", "code", num.trim().toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.nom)", "nom", num.trim().toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.prenom)", "prenom", num.trim().toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.tiers.codeTiers)", "codeT", num.trim().toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(CONCAT(y.client.nom, ' ', y.client.prenom))", "nom_pre", num.trim().toUpperCase() + "%", "LIKE", "OR"));
            }
        } else {
            p.setObjet(null);
        }
        paginator.addParam(p);
        loadAllClient(true, true);
    }

    public void addParamActif() {
        ParametreRequete p;
        boolean _client = groupeSearch < 1;
        if (_client) {
            p = new ParametreRequete("y.actif", "actif", actifSearch);
        } else {
            p = new ParametreRequete("y.client.actif", "actif", actifSearch);
        }
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        loadAllClient(true, true);
    }

    public void addParamConfirmer() {
        ParametreRequete p;
        boolean _client = groupeSearch < 1;
        if (_client) {
            p = new ParametreRequete("y.confirmer", "confirmer", confirmerSearch);
        } else {
            p = new ParametreRequete("y.client.confirmer", "confirmer", confirmerSearch);
        }
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        loadAllClient(true, true);
    }

    public void addParamDefaut() {
        ParametreRequete p;
        boolean _client = groupeSearch < 1;
        if (_client) {
            p = new ParametreRequete("y.defaut", "defaut", defautSearch);
        } else {
            p = new ParametreRequete("y.client.actif", "defaut", defautSearch);
        }
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        loadAllClient(true, true);
    }

    public void addParamCompte() {
        ParametreRequete p;
        if (compteSearch != null ? compteSearch.trim().length() > 0 : false) {
            boolean _client = groupeSearch < 1;
            if (_client) {
                p = new ParametreRequete("y.compte.numCompte", "numCompte", compteSearch + "%");
            } else {
                p = new ParametreRequete("y.client.compte.numCompte", "numCompte", compteSearch + "%");
            }
            p.setOperation(" LIKE ");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.compte.numCompte", "numCompte", null);
        }
        paginator.addParam(p);
        loadAllClient(true, true);
    }

    public void addParamPays() {
        ParametreRequete p;
        if (paysSearch > 0) {
            boolean _client = groupeSearch < 1;
            if (_client) {
                p = new ParametreRequete("y.tiers.pays", "pays", new YvsDictionnaire(paysSearch));
            } else {
                p = new ParametreRequete("y.client.tiers.pays", "pays", new YvsDictionnaire(paysSearch));
            }
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.tiers.pays", "pays", null);
            villeSearch = 0;
            addParamVille();
            secteurSearch = 0;
            addParamSecteur();
        }
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            m.loadVilles(new YvsDictionnaire(paysSearch));
        }
        paginator.addParam(p);
        loadAllClient(true, true);
    }

    public void addParamVille() {
        ParametreRequete p;
        if (villeSearch > 0) {
            boolean _client = groupeSearch < 1;
            if (_client) {
                p = new ParametreRequete("y.tiers.ville", "ville", new YvsDictionnaire(villeSearch));
            } else {
                p = new ParametreRequete("y.client.tiers.ville", "ville", new YvsDictionnaire(villeSearch));
            }
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.tiers.ville", "ville", null);
            secteurSearch = 0;
            addParamSecteur();;
        }
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            m.loadSecteurs(new YvsDictionnaire(villeSearch));
        }
        paginator.addParam(p);
        loadAllClient(true, true);
    }

    public void addParamSecteur() {
        ParametreRequete p;
        if (secteurSearch > 0) {
            boolean _client = groupeSearch < 1;
            if (_client) {
                p = new ParametreRequete("y.tiers.secteur", "secteur", new YvsDictionnaire(secteurSearch));
            } else {
                p = new ParametreRequete("y.client.tiers.secteur", "secteur", new YvsDictionnaire(secteurSearch));
            }
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.tiers.secteur", "secteur", null);
        }
        paginator.addParam(p);
        loadAllClient(true, true);
    }

    public void addParamPlanRistourne() {
        ParametreRequete p;
        if (planRSearch > 0) {
            boolean _client = groupeSearch < 1;
            if (_client) {
                p = new ParametreRequete("y.planRistourne", "plan", new YvsComPlanRistourne(planRSearch));
            } else {
                p = new ParametreRequete("y.client.planRistourne", "plan", new YvsComPlanRistourne(planRSearch));
            }
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.planRistourne", "plan", null);
        }
        paginator.addParam(p);
        loadAllClient(true, true);
    }

    public void addParamCategorie() {
        ParametreRequete p;
        if (categorieSearch > 0) {
            boolean _client = groupeSearch < 1;
            if (_client) {
                p = new ParametreRequete("y.categorieComptable", "categorieComptable", new YvsBaseCategorieComptable(categorieSearch));
            } else {
                p = new ParametreRequete("y.client.categorieComptable", "categorieComptable", new YvsBaseCategorieComptable(categorieSearch));
            }
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.categorieComptable", "categorieComptable", null);
        }
        paginator.addParam(p);
        loadAllClient(true, true);
    }

    public void addParamRepresentant() {
        ParametreRequete p;
        if (reprSearch != null ? reprSearch.trim().length() > 0 : false) {
            boolean _client = groupeSearch < 1;
            if (_client) {
                p = new ParametreRequete(null, "representant", reprSearch + "%");
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.representant.codeUsers)", "representant", reprSearch.trim().toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.representant.nomUsers)", "representant", reprSearch.trim().toUpperCase() + "%", "LIKE", "OR"));
            } else {
                p = new ParametreRequete(null, "representant", reprSearch + "%");
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.representant.codeUsers)", "representant", reprSearch.trim().toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.representant.nomUsers)", "representant", reprSearch.trim().toUpperCase() + "%", "LIKE", "OR"));
            }
        } else {
            p = new ParametreRequete("y.representant", "representant", null);
        }
        paginator.addParam(p);
        loadAllClient(true, true);
    }

    public void addParamGroupe() {
        ParametreRequete p;
        if (groupeSearch > 0) {
            p = new ParametreRequete("y.categorie", "categorie", new YvsBaseCategorieClient(groupeSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.categorie", "categorie", null);
        }
        paginator.addParam(p);
        loadAllClient(true, true);
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("y.dateCreation", "dateSave", null);
        if (date_) {
            p = new ParametreRequete("y.dateCreation", "dateSave", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllClient(true, true);
    }

    public void addParamType() {
        boolean _client = groupeSearch < 1;
        switch (typeSearch) {
            case "SC": {
                addParamSte(true, _client);
                break;
            }
            default: {
                addParamSte(null, _client);
                break;
            }
        }
        loadAllClient(true, true);
    }

    private void addParamSte(Boolean steSearch, boolean client) {
        ParametreRequete p;
        if (client) {
            p = new ParametreRequete("y.tiers.stSociete", "stSociete", steSearch);
        } else {
            p = new ParametreRequete("y.client.tiers.stSociete", "stSociete", steSearch);
        }
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
    }

    public void changeName() {
        changeName(client, selectTemplate, isGeneration());
    }

    public void changeName(Client client, YvsBaseTiersTemplate y, boolean generation) {
        if (generation) {
            if (!client.getTiers().isUpdate()) {
                copie(client, y);
            } else {
                ManagedTiers service = (ManagedTiers) giveManagedBean(ManagedTiers.class);
                if (service != null) {
                    client.setCodeClient(service.getCodeTiers(y, client.getTiers().getSecteur_(), client.getNom(), client.getPrenom()));
                }
                checkCode(client, true);
            }
            update("txt_code_client");
        }
        update("txt_code_tiers_client");
    }

    public void changeRistourne() {
        if (selectClient != null ? selectClient.getId() > 0 : false) {
            if (!autoriser("base_client_change_ristourne")) {
                client.setRistourne(selectClient.getPlanRistourne() != null ? new PlanRistourne(selectClient.getPlanRistourne().getId()) : new PlanRistourne());
                update("txt_ristourne_client");
                return;
            }
        }
        ManagedRistourne w = (ManagedRistourne) giveManagedBean(ManagedRistourne.class);
        if (w != null) {
            int idx = w.getPlans().indexOf(new YvsComPlanRistourne(client.getRistourne().getId()));
            if (idx > -1) {
                PlanRistourne p = UtilCom.buildBeanPlanRistourne(w.getPlans().get(idx));
                client.setRistourne(p);
            }
        }
    }

    public void repositionnerTarifaire(YvsComCategorieTarifaire tar, boolean up) {
        int pos = tarifaires.indexOf(tar);
        if (up) {
            if (tar.getPriorite() < tarifaires.size()) {
                champ = new String[]{"client", "priorite"};
                val = new Object[]{selectClient, tar.getPriorite() + 1};
                List<YvsComCategorieTarifaire> lc = dao.loadNameQueries("YvsComCategorieTarifaire.findByClientPriorite", champ, val, 0, 1);
                if (lc != null ? !lc.isEmpty() : false) {
                    YvsComCategorieTarifaire c = lc.get(0);
                    c.setPriorite(tar.getPriorite());
                    dao.update(c);
                    int pos_ = tarifaires.indexOf(c);
                    tarifaires.set(pos, c);
                    pos = pos_;
                }
                tar.setPriorite(tar.getPriorite() + 1);
                dao.update(tar);
                tarifaires.set(pos, tar);
                succes();
            }
        } else {
            if (tar.getPriorite() > 1) {
                champ = new String[]{"client", "priorite"};
                val = new Object[]{selectClient, tar.getPriorite() - 1};
                List<YvsComCategorieTarifaire> lc = dao.loadNameQueries("YvsComCategorieTarifaire.findByClientPriorite", champ, val, 0, 1);
                if (lc != null ? !lc.isEmpty() : false) {
                    YvsComCategorieTarifaire c = lc.get(0);
                    c.setPriorite(tar.getPriorite());
                    dao.update(c);
                    int pos_ = tarifaires.indexOf(c);
                    tarifaires.set(pos, c);
                    pos = pos_;
                }
                tar.setPriorite(tar.getPriorite() - 1);
                dao.update(tar);
                tarifaires.set(pos, tar);
                succes();
            }
        }
    }

    public void uploadFile(FileUploadEvent event) {
        if (event != null) {
            String path = (isWindows() ? USER_DOWNLOAD : USER_DOWNLOAD_LINUX);
            try {
                file = event.getFile();
                File f = Util.createRessource(file);
                if (f != null) {
                    try {
                        buildData(f.getAbsolutePath());
                    } finally {
                        f.delete();
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(ManagedClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        update("txt_name_file");
    }

    private void buildData(String path) {
        tiersLoad.clear();
        if (new File(path).exists()) {
            List<List<Object>> data = readFileXLS(path);
            if (data != null) {
                if (!data.isEmpty()) {
                    List<Object> head = data.get(0);
                    if (head != null) {
                        if (!head.isEmpty()) {
                            for (int i = 1; i < data.size(); i++) {
                                YvsBaseTiers c = new YvsBaseTiers((long) tiersLoad.size());
                                if (data.get(i).size() > 0) {
                                    c.setCodeTiers((String) data.get(i).get(0));
                                    if (data.get(i).size() > 1) {
                                        c.setNom((String) data.get(i).get(1));
                                        if (data.get(i).size() > 3) {
                                            Double d = (Double) data.get(i).get(3);
                                            String str = new DecimalFormat("#").format(d);
                                            YvsBasePlanComptable p = new YvsBasePlanComptable(str);
                                            c.setCompteCollectif(p);
                                        }
                                    }
                                    champ = new String[]{"societe", "code"};
                                    val = new Object[]{currentAgence.getSociete(), c.getCodeTiers()};
                                    nameQueri = "YvsBaseTiers.findByCode";
                                    List<YvsBaseTiers> l = dao.loadNameQueries(nameQueri, champ, val);
                                    c.setNew_(l != null ? !l.isEmpty() : false);
                                    tiersLoad.add(c);
                                }
                            }
                            openDialog("dlgLoadTiers");
                            update("form_tiers_client_load");
                        } else {
                            getErrorMessage("Fichier Vide");
                        }
                    } else {
                        getErrorMessage("Fichier Incorrect");
                    }
                } else {
                    getErrorMessage("Fichier Vide");
                }
            } else {
                getErrorMessage("Fichier Incorrect");
            }
        } else {
            getErrorMessage("Le fichier n'existe pas");
        }
    }

    public void fullTiers() {
        if (!tiersLoad.isEmpty()) {
            for (YvsBaseTiers t : tiersLoad) {
                if (!t.isNew_()) {
                    if (t.getCompteCollectif() != null ? !t.getCompteCollectif().getNumCompte().equals("") : false) {
                        YvsBasePlanComptable c = t.getCompteCollectif();
                        champ = new String[]{"societe", "numCompte"};
                        val = new Object[]{currentAgence.getSociete(), c.getNumCompte()};
                        YvsBasePlanComptable c_ = (YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findByNum", champ, val);
                        if (c_ != null ? c_.getId() < 1 : true) {
                            c.setActif(true);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            c = (YvsBasePlanComptable) dao.save1(c);
                        } else {
                            c = c_;
                        }
                        t.setCompteCollectif(c);
                    }
                    t.setSociete(currentAgence.getSociete());
                    t.setActif(true);
                    t.setAuthor(currentUser);
                    t.setClient(true);
                    champ = new String[]{"societe", "code"};
                    val = new Object[]{currentAgence.getSociete(), t.getCodeTiers()};
                    List<YvsBaseTiers> l = dao.loadNameQueries("YvsBaseTiers.findByCode", champ, val);
                    if (l != null ? l.isEmpty() : true) {
                        t.setId(null);
                        t = (YvsBaseTiers) dao.save1(t);
                    } else {
                        t = l.get(0);
                        t.setClient(true);
                        dao.update(t);
                    }
                    YvsComClient c = new YvsComClient((long) 0, t);
                    c.setCodeClient(t.getCodeTiers());

                    champ = new String[]{"societe", "code"};
                    val = new Object[]{currentAgence.getSociete(), c.getCodeClient()};
                    YvsComClient c_ = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findByCode", champ, val);
                    if (c_ != null ? c_.getId() < 1 : true) {
                        if (t.getCompteCollectif() != null ? t.getCompteCollectif().getId() > 0 : false) {
                            c.setCompte(t.getCompteCollectif());
                        }
                        c.setAuthor(currentUser);
                        c.setId(null);
                        c = (YvsComClient) dao.save1(c);
                        if (clients.size() < getNbMax()) {
                            clients.add(c);
                        }
                    }
                }
            }
            update("data_client");
            succes();
        }
    }

    public void copie(YvsBaseTiersTemplate y) {
        copie(client, y);
    }

    public void copie(Client client, YvsBaseTiersTemplate y) {
        ManagedTiers service = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (service == null) {
            service = new ManagedTiers();
        }
        if (y != null ? y.getId() > 0 : false) {
            if (!client.getTiers().isUpdate()) {
                client.getTiers().setNom(client.getNom());
                client.getTiers().setPrenom(client.getPrenom());
                service.copie(y, client.getTiers());
                if (y != null ? y.getType() == 'F' : false) {
                    client.setCompteCollectif(UtilCompta.buildBeanCompte(y.getCompteCollectif()));
                    client.setCategorieComptable(UtilCom.buildBeanCategorieComptable(y.getCategorieComptable()));
                }
                client.setCodeClient(client.getTiers().getCodeTiers());
            } else {
                String code = service.getCodeTiers(y, client.getTiers().getSecteur_(), client.getNom(), client.getPrenom());
                if (code != null ? code.trim().length() > 0 : false) {
                    client.setCodeClient(code);
                } else {
                    client.setCodeClient(client.getTiers().getCodeTiers());
                }
            }
            checkCode(client, true);
        }
    }

    public void checkCode(boolean by_tiers) {
        checkCode(client, by_tiers);
    }

    public void checkCode(Client client, boolean by_tiers) {
        client.setCodeClient(checkCode(client.getCodeClient(), client.getId(), by_tiers));
    }

    public String checkCode(String code, long id, boolean by_tiers) {
        List<YvsComClient> lt = dao.loadNameQueries("YvsComClient.findByCode", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), code}, 0, 1);
        if (lt != null ? !lt.isEmpty() : false) {
            if (!lt.get(0).getId().equals(id)) {
                for (int i = 1; i < 1000; i++) {
                    String num = code + (i < 10 ? "00" : (i < 100 ? "0" + i : i)) + i;
                    lt = dao.loadNameQueries("YvsComClient.findByCode", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), num}, 0, 1);
                    if (lt != null ? (lt.isEmpty() ? true : (lt.get(0).getId().equals(id))) : true) {
                        code = num;
                        break;
                    }
                }
            } else if (!by_tiers) {
                code += "001";
            }
        } else if (!by_tiers) {
            code += "001";
        }
        return code;
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Client searchClient(boolean open) {
        return searchClient(client.getCodeClient(), open);
    }

    public Client searchClient(String num, boolean open) {
        Client a = new Client();
        a.setCodeClient(num);
        a.setError(true);
        paginator.addParam(new ParametreRequete("y.codeClient", "code", null));
        if (num != null ? num.trim().length() > 0 : false) {
            ParametreRequete p = new ParametreRequete(null, "code", "%" + num.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.codeClient)", "code", "%" + num.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nom)", "code", "%" + num.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.prenom)", "code", "%" + num.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(CONCAT(y.nom, ' ', y.prenom))", "code", "%" + num.trim().toUpperCase() + "%", "LIKE", "OR"));
            paginator.addParam(p);
        }
        loadAllClient(true, true);
        a = chechClientResult(open);
        if (a != null ? a.getId() < 1 : true) {
            a.setCodeClient(num);
            a.setError(true);
        }
        return a;
    }

    private Client chechClientResult(boolean open) {
        Client a = new Client();
        if (clients != null ? !clients.isEmpty() : false) {
            if (clients.size() > 1) {
                if (open) {
                    openDialog("dlgListClients");
                }
                a.setListClient(true);
            } else {
                YvsComClient c = clients.get(0);
                creance(c);
                a = UtilCom.buildBeanClient(c);
            }
            a.setError(false);
        }
        return a;
    }

    public void initClients(Client a) {
        if (a == null) {
            a = new Client();
        }
        paginator.addParam(new ParametreRequete("y.codeClient", "codeClient", null));
        loadAllClient(true, true);
        a.setListClient(true);
    }

    public long countClient() {
        Long l = (Long) dao.loadObjectByNameQueries("YvsComClient.findAllCount", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        return (l != null) ? l : 0;
    }

    public void fusionner(boolean fusionne) {
        try {
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                long newValue = clients.get(ids.get(0)).getId();
                if (!fusionne) {
                    if (!autoriser("base_user_fusion")) {
                        openNotAcces();
                        return;
                    }
                    String oldValue = "0";
                    for (int i : ids) {
                        if (clients.get(i).getId() != newValue) {
                            oldValue += "," + clients.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_com_client", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                clients.remove(new YvsComClient(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = clients.get(idx).getNom_prenom();
                    } else {
                        YvsComClient c = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                            fusionneTo = c.getNom_prenom();
                        }
                    }
                    YvsComClient c;
                    for (int i : ids) {
                        long oldValue = clients.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(clients.get(i).getNom_prenom());
                            }
                        } else {
                            c = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                                fusionnesBy.add(c.getNom_prenom());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 clients");
            }
        } catch (NumberFormatException ex) {
        }
    }

    public void export() {
        List<Long> ids = new ArrayList<>();
        if (tabIds != null ? tabIds.trim().length() > 0 : false) {
            List<Integer> indexs = decomposeSelection(tabIds);
            for (Integer index : indexs) {
                if (clients.size() > index) {
                    ids.add(clients.get(index).getId());
                }
            }
        }
        if (ids != null ? !ids.isEmpty() : false) {
            executeExport(Constantes.EXPORT_CLIENT, ids);
        } else {
            getErrorMessage("Vous devez selectionner des clients");
        }
    }

    public double creance(YvsComClient bean) {
        try {
            champ = new String[]{"client", "sens", "service"};
            val = new Object[]{bean, true, true};
            Double cp = (Double) dao.loadObjectByNameQueries("YvsComCoutSupDocVente.findSumServiceByClient", champ, val);
            val = new Object[]{bean, false, true};
            Double cm = (Double) dao.loadObjectByNameQueries("YvsComCoutSupDocVente.findSumServiceByClient", champ, val);
            double cs = (cp != null ? cp : 0) - (cm != null ? cm : 0);

            nameQueri = "YvsComContenuDocVente.findTTCAvoirByClient";
            champ = new String[]{"client"};
            val = new Object[]{bean};
            Double avoir = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);

            nameQueri = "YvsComptaCaissePieceVente.findSumAvoirByClient";
            champ = new String[]{"client"};
            val = new Object[]{bean};
            Double aaa = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            avoir = (avoir != null ? avoir : 0) - (aaa != null ? aaa : 0);

            champ = new String[]{"client"};
            val = new Object[]{bean};
            nameQueri = "YvsComContenuDocVente.findTTCByClient";
            Double valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            bean.setDebit((valeur != null ? valeur : 0) + cs - (avoir != null ? avoir : 0));

            nameQueri = "YvsComptaCaissePieceVente.findCreanceByClient";
            valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            bean.setCreance(valeur != null ? valeur : 0);
            nameQueri = "YvsComptaReglementCreditClient.findSumByClient";
            valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            bean.setCreance(bean.getCreance() - (valeur != null ? valeur : 0));

            nameQueri = "YvsComptaCaissePieceVente.findAcompteByClient";
            valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            bean.setAcompte(valeur != null ? valeur : 0);

            nameQueri = "YvsComptaCaissePieceVente.findSumByClient";
            valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            bean.setCredit(valeur != null ? valeur : 0);
            bean.setSolde((bean.getCredit() + bean.getAcompte()) - (bean.getDebit() + bean.getCreance()));
            return bean.getSolde();
        } catch (Exception ex) {
            getException("ManagedClient (creance)", ex);
        }
        return 0;
    }

    public double creanceByAgence(long agence, YvsComClient bean, Date dateDebut, Date dateFin) {
        return creance(new YvsAgences(agence), bean, dateDebut, dateFin);
    }

    public double creance(YvsComClient bean, Date dateDebut, Date dateFin) {
        return creance(currentAgence, bean, dateDebut, dateFin);
    }

    public double creance(YvsAgences agence, YvsComClient bean, Date dateDebut, Date dateFin) {
        return creance(agence, bean, dateDebut, dateFin, true);
    }

    public double creance(YvsComClient bean, Date dateDebut, Date dateFin, boolean solde) {
        return creance(currentAgence, bean, dateDebut, dateFin, solde);
    }

    public double creance(YvsAgences agence, YvsComClient bean, Date dateDebut, Date dateFin, boolean solde) {
        double soldeInitial = 0;
        if (solde) {
            YvsBaseExercice exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findForLast", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            if (exo != null ? exo.getId() < 1 : true) {
                return 0;
            }
            Calendar dd = Calendar.getInstance();
            dd.setTime(exo.getDateDebut());
            Calendar df = Calendar.getInstance();
            df.setTime(dateDebut);
            df.add(Calendar.DATE, -1);
            soldeInitial = creance(agence, bean, dd.getTime(), df.getTime(), false);
        }
        bean.setSoldeInitial(soldeInitial);
        if (agence != null ? agence.getId() > 0 : false) {
            nameQueri = "YvsComCoutSupDocVente.findSumServiceByClientDatesAgence";
            champ = new String[]{"agence", "client", "sens", "service", "dateDebut", "dateFin"};
            val = new Object[]{agence, bean, true, true, dateDebut, dateFin};
        } else {
            nameQueri = "YvsComCoutSupDocVente.findSumServiceByClientDates";
            champ = new String[]{"client", "sens", "service", "dateDebut", "dateFin"};
            val = new Object[]{bean, true, true, dateDebut, dateFin};
        }
        Double cp = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        if (agence != null ? agence.getId() > 0 : false) {
            val = new Object[]{agence, bean, false, true, dateDebut, dateFin};
        } else {
            val = new Object[]{bean, false, true, dateDebut, dateFin};
        }
        Double cm = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        double cs = (cp != null ? cp : 0) - (cm != null ? cm : 0);

        if (agence != null ? agence.getId() > 0 : false) {
            nameQueri = "YvsComContenuDocVente.findTTCAvoirByClientDatesAgence";
            champ = new String[]{"agence", "client", "dateDebut", "dateFin"};
            val = new Object[]{agence, bean, dateDebut, dateFin};
        } else {
            nameQueri = "YvsComContenuDocVente.findTTCAvoirByClientDates";
            champ = new String[]{"client", "dateDebut", "dateFin"};
            val = new Object[]{bean, dateDebut, dateFin};
        }
        Double avoir = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);

        if (agence != null ? agence.getId() > 0 : false) {
            nameQueri = "YvsComptaCaissePieceVente.findSumAvoirByClientDatesAgence";
            champ = new String[]{"agence", "client", "dateDebut", "dateFin", "mouvement"};
            val = new Object[]{agence, bean, dateDebut, dateFin, Constantes.MOUV_CAISS_SORTIE.charAt(0)};
        } else {
            nameQueri = "YvsComptaCaissePieceVente.findSumAvoirByClientDates";
            champ = new String[]{"client", "dateDebut", "dateFin", "mouvement"};
            val = new Object[]{bean, dateDebut, dateFin, Constantes.MOUV_CAISS_SORTIE.charAt(0)};
        }
        Double aad = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        if (agence != null ? agence.getId() > 0 : false) {
            val = new Object[]{agence, bean, dateDebut, dateFin, Constantes.MOUV_CAISS_ENTREE.charAt(0)};
        } else {
            val = new Object[]{bean, dateDebut, dateFin, Constantes.MOUV_CAISS_ENTREE.charAt(0)};
        }
        Double aar = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        avoir = (avoir != null ? avoir : 0) - ((aar != null ? aar : 0) - (aad != null ? aad : 0));

        if (agence != null ? agence.getId() > 0 : false) {
            champ = new String[]{"agence", "client", "dateDebut", "dateFin"};
            val = new Object[]{agence, bean, dateDebut, dateFin};
            nameQueri = "YvsComContenuDocVente.findTTCByClientDatesAgence";
        } else {
            champ = new String[]{"client", "dateDebut", "dateFin"};
            val = new Object[]{bean, dateDebut, dateFin};
            nameQueri = "YvsComContenuDocVente.findTTCByClientDates";
        }
        Double valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setDebit((valeur != null ? valeur : 0) + cs - (avoir != null ? avoir : 0));

        if (agence != null ? agence.getId() > 0 : false) {
            champ = new String[]{"agence", "client", "dateDebut", "dateFin"};
            val = new Object[]{agence, bean, dateDebut, dateFin};
            nameQueri = "YvsComptaCaissePieceVente.findCreanceByClientDatesAgence";
        } else {
            champ = new String[]{"client", "dateDebut", "dateFin"};
            val = new Object[]{bean, dateDebut, dateFin};
            nameQueri = "YvsComptaCaissePieceVente.findCreanceByClientDates";
        }
        valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setCreance(valeur != null ? valeur : 0);

        if (agence != null ? agence.getId() > 0 : false) {
            champ = new String[]{"agence", "client", "dateDebut", "dateFin"};
            val = new Object[]{agence, bean, dateDebut, dateFin};
            nameQueri = "YvsComptaReglementCreditClient.findSumByClientDatesAgence";
        } else {
            champ = new String[]{"client", "dateDebut", "dateFin"};
            val = new Object[]{bean, dateDebut, dateFin};
            nameQueri = "YvsComptaReglementCreditClient.findSumByClientDates";
        }
        valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setCreance(bean.getCreance() - (valeur != null ? valeur : 0));

        if (agence != null ? agence.getId() > 0 : false) {
            champ = new String[]{"agence", "client", "dateDebut", "dateFin"};
            val = new Object[]{agence, bean, dateDebut, dateFin};
            nameQueri = "YvsComptaCaissePieceVente.findAcompteByClientDatesAgence";
        } else {
            champ = new String[]{"client", "dateDebut", "dateFin"};
            val = new Object[]{bean, dateDebut, dateFin};
            nameQueri = "YvsComptaCaissePieceVente.findAcompteByClientDates";
        }
        valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setAcompte(valeur != null ? valeur : 0);

        if (agence != null ? agence.getId() > 0 : false) {
            champ = new String[]{"agence", "client", "dateDebut", "dateFin"};
            val = new Object[]{agence, bean, dateDebut, dateFin};
            nameQueri = "YvsComptaNotifReglementVente.findSumByClientDatesAgence";
        } else {
            champ = new String[]{"client", "dateDebut", "dateFin"};
            val = new Object[]{bean, dateDebut, dateFin};
            nameQueri = "YvsComptaNotifReglementVente.findSumByClientDates";
        }
        valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setAcompte(bean.getAcompte() - (valeur != null ? valeur : 0));

        if (agence != null ? agence.getId() > 0 : false) {
            champ = new String[]{"agence", "client", "dateDebut", "dateFin", "mouvement"};
            val = new Object[]{agence, bean, dateDebut, dateFin, Constantes.MOUV_CAISS_ENTREE.charAt(0)};
            nameQueri = "YvsComptaCaissePieceVente.findSumByClientDatesAgence";
        } else {
            champ = new String[]{"client", "dateDebut", "dateFin", "mouvement"};
            val = new Object[]{bean, dateDebut, dateFin, Constantes.MOUV_CAISS_ENTREE.charAt(0)};
            nameQueri = "YvsComptaCaissePieceVente.findSumByClientDates";
        }
        valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        bean.setCredit(valeur != null ? valeur : 0);

        bean.setSolde(bean.getSoldeInitial() + (bean.getCredit() + bean.getAcompte()) - (bean.getDebit() + bean.getCreance()));
        return bean.getSolde();
    }

    public void onSelectDistantTiers(long id) {
        if (id > 0) {
            YvsBaseTiers y = (YvsBaseTiers) dao.loadOneByNameQueries("YvsBaseTiers.findById", new String[]{"id"}, new Object[]{id});
            if (y != null ? y.getId() > 0 : false) {
                ManagedTiers s = (ManagedTiers) giveManagedBean(ManagedTiers.class);
                if (s != null) {
                    s.onSelectObject(y);
                    Navigations n = (Navigations) giveManagedBean(Navigations.class);
                    if (n != null) {
                        n.naviguationView("Tiers", "modDonneBase", "smenTiersCom", true);
                    }
                }
            }
        } else {
            getErrorMessage("Aucun code tiers n'a été selectionné avec ce critère !");
        }
    }

    /*UPDATE CODE*/
    public void searchCompteUpdate() {
        String num = compte.getNumCompte();
        compte.setId(0);
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
            if (service != null) {
                service.findCompteByNum(num);
                if (service.getListComptes() != null ? !service.getListComptes().isEmpty() : false) {
                    if (service.getListComptes().size() > 1) {
                        compte.setError(true);
                    } else {
                        YvsBasePlanComptable c = service.getListComptes().get(0);
                        compte = UtilCompta.buildBeanCompte(c);
                        compte.setError(false);
                    }
                } else {
                    compte.setError(true);
                }
            }
        }
    }

    public void updateCode() {
        try {
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                YvsComClient y;
                List<YvsComClient> list = new ArrayList<>();
                YvsComClient one;
                YvsComClient two;
                for (int i : ids) {
                    y = clients.get(i);
                    if (y != null ? y.getId() > 0 : false) {
                        String nom = y.getNom() != null ? !y.getNom().trim().isEmpty() ? y.getNom() : y.getTiers().getNom() : y.getTiers().getNom();
                        if (nom.length() > 3) {
                            nom = nom.substring(0, 3);
                        }
                        String code = prefixe + nom + "001";
                        one = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findByCode", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), code});
                        if (one != null ? !one.getId().equals(y.getId()) : false) {
                            int numero = 1;
                            while (true) {
                                numero++;
                                code = prefixe + nom + (numero < 10 ? ("00" + numero) : (numero < 100) ? ("0" + numero) : ("" + numero));
                                one = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findByCode", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), code});
                                if (one != null ? one.getId() < 1 : true) {
                                    break;
                                }
                            }
                        }
                        // UPDATE DU CODE ET DU SUIVI EN COMPTABILITE DU CLIENT
                        String query = "UPDATE yvs_com_client SET code_client = ?, suivi_comptable = ? WHERE id = ?";
                        dao.requeteLibre(query, new Options[]{new Options(code, 1), new Options(suiviComptable, 2), new Options(y.getId(), 3)});
                        y.setCodeClient(code);
                        y.setSuiviComptable(true);
                        if (compte != null ? compte.getId() > 0 : false) {
                            // UPDATE DU COMPTE COLLECTIF DU CLIENT
                            y.setCompte(new YvsBasePlanComptable(compte.getId(), compte.getNumCompte()));
                            query = "UPDATE yvs_com_client SET compte = ? WHERE id = ?";
                            dao.requeteLibre(query, new Options[]{new Options(compte.getId(), 1), new Options(y.getId(), 2)});
                        }
                        // UPDATE DU CODE DU TIERS
                        y.getTiers().setCodeTiers(code);
                        query = "UPDATE yvs_base_tiers SET code_tiers = ? WHERE id = ?";
                        dao.requeteLibre(query, new Options[]{new Options(code, 1), new Options(y.getTiers().getId(), 2)});
                        list.add(y);
                    }
                }
                succes();
            }
        } catch (Exception ex) {
            getException("Action impossible", ex);
            getErrorMessage("Action impossible");
        }
    }

    public void chooseLigne(ValueChangeEvent ev) {
        if (ev != null) {
            Long id = (Long) ev.getNewValue();
            ManagedPointLivraison service = (ManagedPointLivraison) giveManagedBean(ManagedPointLivraison.class);
            if (service != null) {
                int idx = service.getPoints().indexOf(new YvsBasePointLivraison(id));
                if (idx >= 0) {
                    YvsBasePointLivraison pl = service.getPoints().get(idx);
                    client.getLigne().setId(pl.getId());
                    client.getLigne().setLibelle(pl.getLibelle());
                }
            }
        }
    }

    public void gotoDateForPrint(String report) {
        this.report = report;
    }

    public void print() {
        print(report);
    }

    public void print(String report) {
        if (autoriser("base_client_print_actif")) {
            try {
                Map<String, Object> param = new HashMap<>();
                param.put("AGENCE", currentAgence.getId().intValue());
                param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
                param.put("NAME_AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("DATE_DEBUT", dateDebutPrint);
                param.put("DATE_FIN", dateFinPrint);
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", SUBREPORT_DIR());
                executeReport(report, param);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(ManagedFactureVente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
