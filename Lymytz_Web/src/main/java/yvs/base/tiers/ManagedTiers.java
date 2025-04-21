/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.tiers;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.UploadedFile;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.Comptes;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.UtilCompta;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.ManagedCommerciaux;
import yvs.commercial.UtilCom;
import yvs.commercial.client.Client;
import yvs.commercial.client.ManagedClient;
import yvs.commercial.fournisseur.Fournisseur;
import yvs.commercial.fournisseur.ManagedFournisseur;
import yvs.dao.Options;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.YvsComParametreStock;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.commission.YvsComPlanCommission;
import yvs.entity.commercial.rrr.YvsComPlanRistourne;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.ext.YvsExtTiers;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.tiers.YvsBaseTiersTelephone;
import yvs.entity.tiers.YvsBaseTiersTemplate;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.dico.ManagedDico;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author Lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedTiers extends ManagedCommercial<Tiers, YvsBaseTiers> implements Serializable {

    @ManagedProperty(value = "#{tiers}")
    private Tiers tiers;
    private List<YvsBaseTiers> listTiers, tiersLoad, tiers_all;
    private YvsBaseTiers selectTiers;

    private List<String> listes;

    private YvsBaseTiersTemplate selectTemplate;

    private Contact contact = new Contact();
    private List<YvsBaseTiersTelephone> contacts;
    private YvsBaseTiersTelephone selectContact;

    private Client client = new Client();
    private Fournisseur fournisseur = new Fournisseur();
    private yvs.grh.bean.Employe employe = new yvs.grh.bean.Employe();

    private long id_pays;

    private List<YvsComPlanRistourne> ristournes;
    private List<YvsComPlanCommission> commissions;

    private boolean generation, displayParamExt;
    private String tabIds, tabIds_contact, type = "o";
    private String fusionneTo, codeExterne;
    private List<String> fusionnesBy;

    private UploadedFile file;

    private Profil selectProfil;
//    private List<Profil> profiles;

    private String typeSearch, compteSearch;
    private long paysSearch, villeSearch, secteurSearch, categorieSearch;
    private Boolean actifSearch, rationSearch;
    private boolean _first = true;

    private YvsComParametreStock currentStock;

    private boolean actionEmploye = true, actionClient = true, actionFournisseur = true, actionCommercial = true;

    private int ecartInactif = 30;
    private String actionExecute = "I";
    private List<YvsBaseTiers> tiersRemove, tiersRemoveSelect;

    private String columns;
    private Object managedBean;

    public ManagedTiers() {
//        profiles = new ArrayList<>();
        commissions = new ArrayList<>();
        ristournes = new ArrayList<>();
        listTiers = new ArrayList<>();
        contacts = new ArrayList<>();
        tiersLoad = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
        listes = new ArrayList<>();
        tiers_all = new ArrayList<>();
        tiersRemove = new ArrayList<>();
        tiersRemoveSelect = new ArrayList<>();
    }

    public boolean isActionEmploye() {
        return actionEmploye;
    }

    public void setActionEmploye(boolean actionEmploye) {
        this.actionEmploye = actionEmploye;
    }

    public boolean isActionClient() {
        return actionClient;
    }

    public void setActionClient(boolean actionClient) {
        this.actionClient = actionClient;
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

    public List<YvsBaseTiers> getTiersRemoveSelect() {
        return tiersRemoveSelect;
    }

    public void setTiersRemoveSelect(List<YvsBaseTiers> tiersRemoveSelect) {
        this.tiersRemoveSelect = tiersRemoveSelect;
    }

    public int getEcartInactif() {
        return ecartInactif;
    }

    public void setEcartInactif(int ecartInactif) {
        this.ecartInactif = ecartInactif;
    }

    public String getActionExecute() {
        return actionExecute;
    }

    public void setActionExecute(String actionExecute) {
        this.actionExecute = actionExecute;
    }

    public List<YvsBaseTiers> getTiersRemove() {
        return tiersRemove;
    }

    public void setTiersRemove(List<YvsBaseTiers> tiersRemove) {
        this.tiersRemove = tiersRemove;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public Object getManagedBean() {
        return managedBean;
    }

    public void setManagedBean(Object managedBean) {
        this.managedBean = managedBean;
    }

    public YvsComParametreStock getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(YvsComParametreStock currentStock) {
        this.currentStock = currentStock;
    }

    public List<YvsBaseTiers> getTiers_all() {
        return tiers_all;
    }

    public void setTiers_all(List<YvsBaseTiers> tiers_all) {
        this.tiers_all = tiers_all;
    }

    public List<String> getListes() {
        return listes;
    }

    public void setListes(List<String> listes) {
        this.listes = listes;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public yvs.grh.bean.Employe getEmploye() {
        return employe;
    }

    public void setEmploye(yvs.grh.bean.Employe employe) {
        this.employe = employe;
    }

    public boolean isDisplayParamExt() {
        return displayParamExt;
    }

    public void setDisplayParamExt(boolean displayParamExt) {
        this.displayParamExt = displayParamExt;
    }

    public Boolean getRationSearch() {
        return rationSearch;
    }

    public void setRationSearch(Boolean rationSearch) {
        this.rationSearch = rationSearch;
    }

    public String getCodeExterne() {
        return codeExterne;
    }

    public void setCodeExterne(String codeExterne) {
        this.codeExterne = codeExterne;
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

    public boolean isGeneration() {
        return generation;
    }

    public void setGeneration(boolean generation) {
        this.generation = generation;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public List<YvsBaseTiers> getTiersLoad() {
        return tiersLoad;
    }

    public void setTiersLoad(List<YvsBaseTiers> tiersLoad) {
        this.tiersLoad = tiersLoad;
    }

    public long getCategorieSearch() {
        return categorieSearch;
    }

    public void setCategorieSearch(long categorieSearch) {
        this.categorieSearch = categorieSearch;
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

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public YvsBaseTiersTemplate getSelectTemplate() {
        return selectTemplate;
    }

    public void setSelectTemplate(YvsBaseTiersTemplate selectTemplate) {
        this.selectTemplate = selectTemplate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<YvsBaseTiersTelephone> getContacts() {
        return contacts;
    }

    public void setContacts(List<YvsBaseTiersTelephone> contacts) {
        this.contacts = contacts;
    }

    public YvsBaseTiersTelephone getSelectContact() {
        return selectContact;
    }

    public void setSelectContact(YvsBaseTiersTelephone selectContact) {
        this.selectContact = selectContact;
    }

    public String getTabIds_contact() {
        return tabIds_contact;
    }

    public void setTabIds_contact(String tabIds_contact) {
        this.tabIds_contact = tabIds_contact;
    }

    public long getId_pays() {
        return id_pays;
    }

    public void setId_pays(long id_pays) {
        this.id_pays = id_pays;
    }

    public Tiers getTiers() {
        return tiers;
    }

    public void setTiers(Tiers tiers) {
        this.tiers = tiers;
    }

    public List<YvsBaseTiers> getListTiers() {
        return listTiers;
    }

    public void setListTiers(List<YvsBaseTiers> listTiers) {
        this.listTiers = listTiers;
    }

    public YvsBaseTiers getSelectTiers() {
        return selectTiers;
    }

    public void setSelectTiers(YvsBaseTiers selectTiers) {
        this.selectTiers = selectTiers;
    }

    public List<YvsComPlanRistourne> getRistournes() {
        return ristournes;
    }

    public void setRistournes(List<YvsComPlanRistourne> ristournes) {
        this.ristournes = ristournes;
    }

    public List<YvsComPlanCommission> getCommissions() {
        return commissions;
    }

    public void setCommissions(List<YvsComPlanCommission> commissions) {
        this.commissions = commissions;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public boolean isDisplay_column_1() {
        return Util.asString(columns) ? columns.contains("1") : true;
    }

    public boolean isDisplay_column_2() {
        return Util.asString(columns) ? columns.contains("2") : true;
    }

    public boolean isDisplay_column_3() {
        return Util.asString(columns) ? columns.contains("3") : true;
    }

    public boolean isDisplay_column_4() {
        return Util.asString(columns) ? columns.contains("4") : true;
    }

    public boolean isDisplay_column_5() {
        return Util.asString(columns) ? columns.contains("5") : true;
    }

    public boolean isDisplay_column_6() {
        return Util.asString(columns) ? columns.contains("6") : true;
    }

    public boolean isDisplay_column_7() {
        return Util.asString(columns) ? columns.contains("7") : true;
    }

    public boolean isDisplay_column_8() {
        return Util.asString(columns) ? columns.contains("8") : true;
    }

    public boolean isDisplay_column_9() {
        return Util.asString(columns) ? columns.contains("9") : true;
    }

    public boolean isDisplay_column_0() {
        return Util.asString(columns) ? columns.contains("0") : true;
    }

//    public List<Profil> getProfiles() {
//        return profiles;
//    }
//
//    public void setProfiles(List<Profil> profiles) {
//        this.profiles = profiles;
//    }
    @Override
    public void loadAll() {
        _first = true;
        managedBean = this;
        columns = null;
        loadComptes();
        loadTiers(initForm = true, true);
        loadSetting();
    }

    public void loadSetting() {
        currentStock = (YvsComParametreStock) dao.loadOneByNameQueries("YvsComParametreStock.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
    }

    public void view(Boolean client) {

    }

    public void loadList() {
        listes = dao.loadListByNameQueries("YvsBaseTiers.findNoms", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllTiers() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsBaseTiers.findAlls";
        tiers_all = dao.loadNameQueries(nameQueri, champ, val);
    }
    boolean initForm = true;

    public void loadTiers(boolean init, boolean avance) {
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        String query = "YvsBaseTiers y LEFT JOIN FETCH y.telephones LEFT JOIN FETCH y.secteur LEFT JOIN FETCH y.codeExterne "
                + "LEFT JOIN FETCH y.mdr LEFT JOIN FETCH y.categorieComptable "
                + "JOIN FETCH y.pays LEFT JOIN FETCH y.ville LEFT JOIN FETCH y.compteCollectif ";
        listTiers = paginator.executeDynamicQuery("DISTINCT y", "DISTINCT y", query, "y.codeTiers, y.nom, y.prenom", avance, initForm, (int) imax, dao);
        if (listTiers != null ? listTiers.size() == 1 : false) {
            onSelectObject(listTiers.get(0));
            execute("collapseForm('tiers')");
        } else {
            execute("collapseList('tiers')");
        }
        update("data_tiers");
    }

    public void loadTiersActif(boolean actif) {
        actifSearch = true;
        addParamActif();
        loadTiers(false, true);
    }

    public void loadActif(Boolean actif) {
        if (_first) {
            clearParams(true);
        }
        actifSearch = actif;
        _first = false;
        addParamActif();
    }

    public void loadTiersByType(String type, Boolean actif) {
        loadTiersByType(type, actif, true);
    }

    public void loadTiersByType(String type, Boolean actif, boolean load) {
        if (_first) {
            clearParams(true);
        }
        typeSearch = type;
        actifSearch = actif;
        paginator.addParam(new ParametreRequete("y.actif", "actif", actif, "=", "AND"));
        _first = false;
        addParamType(load);
    }

    private void loadOthers() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsBasePlanComptable.findAll";
        ristournes = dao.loadNameQueries("YvsComPlanRistourne.findAll", champ, val);

        commissions = dao.loadNameQueries("YvsComPlanCommission.findAll", champ, val);
    }

    public void loadContact(YvsBaseTiers t) {
        champ = new String[]{"tiers"};
        val = new Object[]{t};
        contacts = dao.loadNameQueries("YvsBaseTiersTelephone.findByTiers", champ, val);
        update("data_tiers_contact");
    }

    public YvsDictionnaire buildDictionnaire(Dictionnaire d) {
        YvsDictionnaire r = new YvsDictionnaire();
        if (d != null) {
            r.setId(d.getId());
            r.setLibele(d.getLibelle());
            r.setTitre(d.getTitre());
            r.setActif(true);
            r.setSupp(false);
            if (d.getParent() != null ? d.getParent().getId() > 0 : false) {
                r.setParent(new YvsDictionnaire(d.getParent().getId()));
            }
            r.setSociete(currentAgence.getSociete());
            r.setAuthor(currentUser);
        }
        return r;
    }

    @Override
    public Tiers recopieView() {
        Tiers r = new Tiers();
        r.setId(tiers.getId());
        r.setCodeTiers(tiers.getCodeTiers());
        r.setAgence(tiers.getAgence());
        r.setNom(tiers.getNom());
        r.setPrenom(tiers.getPrenom());
        r.setResponsable(tiers.getResponsable());
        r.setCodePostal(tiers.getCodePostal());
        r.setCivilite(tiers.getCivilite());
        r.setAdresse(tiers.getAdresse());
        r.setEmail(tiers.getEmail());
        r.setSecteur(tiers.getSecteur());
        r.setVille(tiers.getVille());
        r.setPays(tiers.getPays());
        r.setBp(tiers.getBp());
        r.setLogo(tiers.getLogo());
        r.setTelephone(tiers.getTelephone());
        r.setDateSave(tiers.getDateSave());
        r.setDateUpdate(tiers.getDateUpdate());
        r.setCategorieComptable(tiers.getCategorieComptable());
        r.setModelDeRglt(tiers.getModelDeRglt());
        r.setPlanComission(tiers.getPlanComission());
        r.setPlanRistourne(tiers.getPlanRistourne());
        r.setCompteCollectif(tiers.getCompteCollectif());
        r.setCompte(tiers.getCompte());
        r.setCodeRation(tiers.getCodeRation());

        r.setSociete(tiers.isSociete());
        r.setClient(tiers.isClient());
        r.setEmploye(tiers.isEmploye());
        r.setPersonnel(tiers.isPersonnel());
        r.setFournisseur(tiers.isFournisseur());
        r.setRepresentant(tiers.isRepresentant());
        r.setFabricant(tiers.isFabricant());
        r.setSite(tiers.getSite());
        r.setUpdate(tiers.isUpdate());
        r.setActif(tiers.isUpdate() ? tiers.isActif() : true);
        return r;
    }

    public Dictionnaire recopie(Dictionnaire y, Dictionnaire p, String Titre) {
        Dictionnaire d = new Dictionnaire();
        d.setId(y.getId());
        d.setLibelle(y.getLibelle());
        d.setParent(p);
        d.setTitre(Titre);
        return d;
    }

    @Override
    public boolean controleFiche(Tiers bean) {
        if (bean.getCodeTiers() == null || bean.getCodeTiers().trim().equals("")) {
            getErrorMessage("Vous devez entrer le code");
            return false;
        }
        if (bean.getNom() == null || bean.getNom().trim().equals("")) {
            getErrorMessage("Vous devez entrer le nom");
            return false;
        }
        if (bean.getPays() != null ? bean.getPays().getId() < 1 : true) {
            getErrorMessage("Vous devez entrer le pays");
            return false;
        }
        if (bean.getVille() != null ? bean.getVille().getId() < 1 : true) {
            getErrorMessage("Vous devez entrer la ville");
            return false;
        }
        if (bean.isPersonnel() ? bean.getAgence() != null ? bean.getAgence().getId() < 1 : true : false) {
            getErrorMessage("Vous devez indiquer l'agence");
            return false;
        }
        YvsBaseTiers y = (YvsBaseTiers) dao.loadOneByNameQueries("YvsBaseTiers.findByCode", new String[]{"code", "societe"}, new Object[]{bean.getCodeTiers(), currentAgence.getSociete()});
        if (y != null ? y.getId() > 0 : false) {
            if (!y.getId().equals(bean.getId())) {
                getErrorMessage("Vous avez déja crée un tiers avec ce code");
                return false;
            }
            if (!y.getCodeRation().equals(bean.getCodeRation()) && !autoriser("base_tiers_attrib_ration")) {
                openNotAcces();
                return false;
            }
        }
        return true;
    }

    public boolean controleFicheNumero(Contact bean) {
        if (bean.getNumero() == null || bean.getNumero().equals("")) {
            getErrorMessage("Vous devez entrer le numero");
            return false;
        }
        YvsBaseTiersTelephone y = (YvsBaseTiersTelephone) dao.loadOneByNameQueries("YvsBaseTiersTelephone.findByNumero", new String[]{"numero", "tiers"}, new Object[]{bean.getNumero(), selectTiers});
        if (y != null ? !y.getId().equals(bean.getId()) : false) {
            getErrorMessage("Ce numero est deja attribué à ce tiers");
            return false;
        }
        if (bean.isPrincipal()) {
            y = (YvsBaseTiersTelephone) dao.loadOneByNameQueries("YvsBaseTiersTelephone.findByPrincipal", new String[]{"principal", "tiers"}, new Object[]{true, selectTiers});
            if (y != null ? !y.getId().equals(bean.getId()) : false) {
                getErrorMessage("Vous avez déja ajouter un numéro principal");
                return false;
            }
        }
        return true;
    }

    public boolean controleFicheCategorie(CategorieComptable bean) {
        if (bean.getCodeCategorie() == null || bean.getCodeCategorie().equals("")) {
            getErrorMessage("vous devez entrer le code de la categorie");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(Tiers bean) {
        cloneObject(tiers, bean);
        update("form_tiers");
    }

    public void populateViewContact(Contact bean) {
        cloneObject(contact, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(true);
    }

    public void resetFiche(boolean all) {
        Dictionnaire pays = new Dictionnaire();
        cloneObject(pays, tiers.getPays());
        Dictionnaire ville = new Dictionnaire();
        cloneObject(ville, tiers.getVille());
        Dictionnaire secteur = new Dictionnaire();
        cloneObject(secteur, tiers.getSecteur());
        YvsBaseTiersTemplate template = tiers.getTemplate();

        resetFiche(tiers);
        tiers.setAgence(new Agence());
        tiers.setVille(new Dictionnaire());
        tiers.setPays(new Dictionnaire());
        tiers.setSecteur(new Dictionnaire());
        tiers.setTemplate(new YvsBaseTiersTemplate());
        if (!all) {
            tiers.setVille(ville);
            tiers.setPays(pays);
            tiers.setSecteur(secteur);
            tiers.setTemplate(template);
        }
        tiers.setContact(new Contact());
        tiers.setCategorieComptable(new CategorieComptable());
        tiers.setCompte(new Comptes());
        tiers.setProfils(new ArrayList<Profil>());
        tiers.setSociete(false);
        tiers.setFournisseur(false);
        tiers.setClient(false);
        tiers.setEmploye(false);
        tiers.setRepresentant(false);
        tiers.setFabricant(false);
        contacts.clear();
        selectTiers = null;
        selectTemplate = null;
        generation = false;
        codeExterne = "";
        tabIds = "";
        type = "o";
        ManagedTiersDocument w = (ManagedTiersDocument) giveManagedBean(ManagedTiersDocument.class);
        if (w != null) {
            w.setTiers(tiers);
        }
        update("blog_form_tiers");
    }

    public Tiers resetTiers(Tiers tiers) {
        Dictionnaire pays = new Dictionnaire();
        cloneObject(pays, tiers.getPays());
        Dictionnaire ville = new Dictionnaire();
        cloneObject(ville, tiers.getVille());
        Dictionnaire secteur = new Dictionnaire();
        cloneObject(secteur, tiers.getSecteur());
        YvsBaseTiersTemplate template = tiers.getTemplate();

        tiers = new Tiers();
        tiers.setVille(ville);
        tiers.setPays(pays);
        tiers.setSecteur(secteur);
        tiers.setTemplate(template);
        return tiers;
    }

    public void resetFicheContact() {
        contact = new Contact();
        tabIds_contact = "";
        selectContact = null;
        update("form_tiers_contact");
    }

    public void reset(Dictionnaire d) {
        resetFiche(d);
        d.setParent(new Dictionnaire());
    }

    @Override
    public boolean saveNew() {
        selectTiers = saveNew(recopieView());
        if (selectTiers != null ? selectTiers.getId() > 0 : false) {
            tiers.setId(selectTiers.getId());
            succes();
            actionOpenOrResetAfter(this);
            update("data_tiers");
            return true;
        }
        return false;
    }

    public YvsBaseTiers saveNew(Tiers bean) {
        String action = bean.isUpdate() ? "Modification" : "Insertion";
        try {
            if (controleFiche(bean)) {
                YvsBaseTiers y = UtilCom.buildTiers(bean);
                ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
                if (bean.getSecteur() != null ? bean.getSecteur().getId() > 0 : false) {
                    if (m != null) {
                        y.setSecteur(m.getSecteurs().get(m.getSecteurs().indexOf(new YvsDictionnaire(bean.getSecteur().getId()))));
                    } else {
                        y.setSecteur(new YvsDictionnaire(bean.getSecteur().getId()));
                    }
                }
                if (bean.getVille() != null ? bean.getVille().getId() > 0 : false) {
                    if (m != null) {
                        y.setVille(m.getVilles().get(m.getVilles().indexOf(new YvsDictionnaire(bean.getVille().getId()))));
                    } else {
                        y.setVille(new YvsDictionnaire(bean.getVille().getId()));
                    }
                }
                if (bean.getPays() != null ? bean.getPays().getId() > 0 : false) {
                    if (m != null) {
                        y.setPays(m.getPays().get(m.getPays().indexOf(new YvsDictionnaire(bean.getPays().getId()))));
                    } else {
                        y.setPays(new YvsDictionnaire(bean.getPays().getId()));
                    }
                }
                y.setSociete(currentAgence.getSociete());
                y.setAuthor(currentUser);
                if (bean.getId() <= 0) {
                    if (!autoriser("base_tiers_save")) {
                        openNotAcces();
                        return null;
                    }
                    y.setDateSave(new Date());
                    y.setDateUpdate(new Date());
                    y.setId(null);
                    y = (YvsBaseTiers) dao.save1(y);
                    bean.setId(y.getId());
                    listTiers.add(0, y);
                } else {
                    if (!autoriser("base_tiers_update")) {
                        openNotAcces();
                        return null;
                    }
                    y.setDateUpdate(new Date());
                    dao.update(y);
                    int idx = listTiers.indexOf(y);
                    if (idx >= 0) {
                        listTiers.set(idx, y);
                    }
                }
                bean.setUpdate(true);
                ManagedTiersDocument w = (ManagedTiersDocument) giveManagedBean(ManagedTiersDocument.class);
                if (w != null) {
                    w.setTiers(bean);
                }
                return y;
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Lymytz Error >>>" + action + " : " + ex.getMessage(), ex);
            return null;
        }
        return null;
    }

    public void saveNewContact() {
        if (saveNewContact(contact)) {
            succes();
            update("data_tiers_contact");
            update("data_tiers");
        }
    }

    public boolean saveNewContact(Contact contact) {
        String action = contact.isUpdate() ? "Modification" : "Insertion";
        try {
            Contact bean = new Contact();
            cloneObject(bean, contact);
            if (controleFicheNumero(bean)) {
                YvsBaseTiersTelephone entity = new YvsBaseTiersTelephone(bean.getId());
                entity.setNumero(bean.getNumero());
                entity.setPrincipal(bean.isPrincipal());
                entity.setNew_(true);
                entity.setTiers(selectTiers);
                entity.setAuthor(currentUser);
                if (!bean.isUpdate()) {
                    entity.setId(null);
                    entity = (YvsBaseTiersTelephone) dao.save1(entity);
                    bean.setId(entity.getId());
                    contacts.add(0, entity);
                    if (selectTiers != null ? selectTiers.getTelephones() != null ? selectTiers.getTelephones().isEmpty() : false : false) {
                        selectTiers.getTelephones().add(0, entity);
                        int idx = listTiers.indexOf(selectTiers);
                        if (idx > -1) {
                            listTiers.set(idx, selectTiers);
                        }
                    }
                } else {
                    dao.update(entity);
                    contacts.set(contacts.indexOf(entity), entity);
                    if (selectTiers != null ? selectTiers.getTelephones() != null : false) {
                        int idx = selectTiers.getTelephones().indexOf(entity);
                        if (idx > -1) {
                            selectTiers.getTelephones().set(idx, entity);
                            idx = listTiers.indexOf(selectTiers);
                            if (idx > -1) {
                                listTiers.set(idx, selectTiers);
                            }
                        }
                    }
                }
                if (selectTiers != null ? selectTiers.getTelephones() != null : false) {
                    for (int i = 0; i < selectTiers.getTelephones().size(); i++) {
                        if (i == 0) {
                            selectTiers.setTel(selectTiers.getTelephones().get(i).getNumero());
                        } else {
                            selectTiers.setTel(selectTiers.getTel() + " / " + selectTiers.getTelephones().get(i).getNumero());
                        }
                    }
                    dao.update(selectTiers);
                    int idx = listTiers.indexOf(selectTiers);
                    if (idx > -1) {
                        listTiers.set(idx, selectTiers);
                    }
                }
                resetFicheContact();
                update("data_tiers_contact");
                update("data_tiers");
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
        }
        return false;
    }

    @Override
    public void deleteBean() {
        try {
            if (!autoriser("base_tiers_delete")) {
                openNotAcces();
                return;
            }
            System.err.println("tabIds = " + tabIds);
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsBaseTiers> list = new ArrayList<>();
                YvsBaseTiers bean;
                for (Long ids : l) {
                    bean = listTiers.get(ids.intValue());
                    list.add(bean);
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    dao.delete(bean);
                    if (bean.getId() == tiers.getId()) {
                        resetFiche();
                    }
                }
                listTiers.removeAll(list);
                succes();
                tabIds = "";
                update("data_tiers");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsBaseTiers y) {
        selectTiers = y;
    }

    public void deleteBean_() {
        if (!autoriser("base_tiers_delete")) {
            openNotAcces();
            return;
        }
        try {
            if (selectTiers != null) {
                dao.delete(selectTiers);
                listTiers.remove(selectTiers);
                if (selectTiers.getId() == tiers.getId()) {
                    resetFiche();
                }
                succes();
                update("data_tiers");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanContact() {
        try {
            if ((tabIds_contact != null) ? !tabIds_contact.equals("") : false) {
                String[] tab = tabIds_contact.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsBaseTiersTelephone bean = contacts.get(contacts.indexOf(new YvsBaseTiersTelephone(id)));
                    dao.delete(bean);
                    contacts.remove(bean);
                    if (contact.getId() == selectContact.getId()) {
                        resetFicheContact();
                    }
                }
                succes();
                update("data_tiers");
                update("data_tiers_contact");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanContact_(YvsBaseTiersTelephone y) {
        selectContact = y;
    }

    public void deleteBeanContact_() {
        try {
            if (selectContact != null) {
                dao.delete(selectContact);
                contacts.remove(selectContact);
                selectTiers.getTelephones().remove(selectContact);
                int idx = listTiers.indexOf(selectTiers);
                if (idx > -1) {
                    listTiers.set(idx, selectTiers);
                }
                if (contact.getId() == selectContact.getId()) {
                    resetFicheContact();
                }
                succes();
                update("data_tiers");
                update("data_tiers_contact");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void openToDeleteProfil(Profil p) {
        selectProfil = p;
    }

    public void deleteProfil() {
        deleteProfil(selectProfil);
    }

    public void deleteProfil(Profil y) {
        try {
            if (y != null) {
                String query = "";
                switch (y.getType()) {
                    case Constantes.BASE_TIERS_EMPLOYE:
                        query = "update yvs_grh_employes set compte_tiers = null where id = ?";
                        selectTiers.getEmployes().remove(new YvsGrhEmployes(y.getId()));
                        break;
                    case Constantes.BASE_TIERS_COMMERCIAL:
                        query = "update yvs_com_comerciale set tiers = null where id = ?";
                        selectTiers.getCommerciaux().remove(new YvsComComerciale(y.getId()));
                        break;
                }
                if (query != null ? query.trim().length() > 0 : false) {
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                    tiers.getProfils().remove(y);
                    int idx = listTiers.indexOf(selectTiers);
                    if (idx > -1) {
                        listTiers.set(idx, selectTiers);
                    }
                    succes();
                }
                update("blog_profil_tiers");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void onSelectObject(YvsBaseTiers y) {
        selectTiers = y;
        loadContact(selectTiers);
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            m.loadVilles(selectTiers.getPays());
            m.loadSecteurs(selectTiers.getVille());
        }
        populateView(UtilTiers.buildBeanTiers(selectTiers));
        ManagedTiersDocument w = (ManagedTiersDocument) giveManagedBean(ManagedTiersDocument.class);
        if (w != null) {
            w.loadAll(tiers);
        }
        codeExterne = (y.getCodeExterne() != null) ? y.getCodeExterne().getCodeExterne() : null;
        selectTemplate = null;
        generation = false;
        update("blog_form_tiers");
    }

    public void onSelectObjectDistant(Profil p) {
        if (p != null) {
            switch (p.getType()) {
                case Constantes.BASE_TIERS_FOURNISSEUR: {
                    ManagedFournisseur w = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
                    if (w != null) {
                        YvsBaseFournisseur y = (YvsBaseFournisseur) dao.loadOneByNameQueries("YvsBaseFournisseur.findById", new String[]{"id"}, new Object[]{p.getId()});
                        w.onSelectObject(y);
                        Navigations n = (Navigations) giveManagedBean(Navigations.class);
                        if (n != null) {
                            n.naviguationView("Fournisseurs", "modDonneBase", "smenFournisseurCom", true);
                        }
                    }
                    break;
                }
                case Constantes.BASE_TIERS_COMMERCIAL: {
                    ManagedCommerciaux w = (ManagedCommerciaux) giveManagedBean(ManagedCommerciaux.class);
                    if (w != null) {
                        YvsComComerciale y = (YvsComComerciale) dao.loadOneByNameQueries("YvsComComerciale.findById", new String[]{"id"}, new Object[]{p.getId()});
                        w.onSelectObject(y);
                        Navigations n = (Navigations) giveManagedBean(Navigations.class);
                        if (n != null) {
                            n.naviguationView("Commerciaux", "modGescom", "smenPersonnel", true);
                        }
                    }
                    break;
                }
                case Constantes.BASE_TIERS_EMPLOYE: {
                    ManagedEmployes w = (ManagedEmployes) giveManagedBean("MEmps");
                    if (w != null) {
                        YvsGrhEmployes y = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findById", new String[]{"id"}, new Object[]{p.getId()});
                        w.onSelectObject(y);
                        Navigations n = (Navigations) giveManagedBean(Navigations.class);
                        if (n != null) {
                            n.naviguationView("Employés", "modRh", "smenEmploye", true);
                        }
                    }
                    break;
                }
                case Constantes.BASE_TIERS_CLIENT: {
                    ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
                    if (w != null) {
                        YvsComClient y = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findById", new String[]{"id"}, new Object[]{p.getId()});
                        w.onSelectObject(y);
                        Navigations n = (Navigations) giveManagedBean(Navigations.class);
                        if (n != null) {
                            n.naviguationView("Clients", "modDonneBase", "smenClientCom", true);
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseTiers y = (YvsBaseTiers) ev.getObject();
            invoqueMethodeLoad(y);
            tabIds = listTiers.indexOf(y) + "";
            update("input_hide_tiers");
        }
    }

    public void onSelectTiers(YvsBaseTiers y) {
        onSelectObject(y);
        execute("collapseForm('tiers')");
        execute("collapseForm('tiers_contact')");
        execute("collapseForm('tiers_tarif')");
    }

    public void invoqueMethodeLoad(YvsBaseTiers select) {
        if (managedBean != null) {
            try {
                Method method = managedBean.getClass().getMethod("onSelectTiers", YvsBaseTiers.class);
                method.invoke(managedBean, select);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(ManagedTiers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("form_tiers");
        update("data_tiers");
    }

    public void loadOnViewTemplate(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBaseTiersTemplate bean = (YvsBaseTiersTemplate) ev.getObject();
            copie(bean);
            ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
            if (m != null) {
                m.loadVilles(bean.getPays());
                m.loadSecteurs(bean.getVille());
            }
            update("infos_tiers");
            update("txt_pays_ville_");
            update("txt_pays_secteur_");
            update("txt_ville_secteur_");
        }
    }

    public void unLoadOnViewTemplate(UnselectEvent ev) {
        tiers.setTemplate(null);
        update("txt_code_tiers");
    }

    public void loadOnViewContact(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseTiersTelephone bean = (YvsBaseTiersTelephone) ev.getObject();
            populateViewContact(UtilTiers.buildBeanContact(bean));
            update("blog_form_tiers_contact");
        }
    }

    public void unLoadOnViewContact(UnselectEvent ev) {
        resetFicheContact();
        update("blog_form_tiers_contact");
    }

    public void loadOnViewCompte(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBasePlanComptable bean = (YvsBasePlanComptable) ev.getObject();
            tiers.setCompteCollectif(bean.getId());
            tiers.setCompte(UtilCompta.buildBeanCompte(bean));
            update("txt_compte_tiers");
        }
    }

    public void choosePays() {
        update("txt_pays_ville_");
        update("txt_pays_secteur_");
        tiers.setVille(new Dictionnaire());
        tiers.setSecteur(new Dictionnaire());
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            Dictionnaire d = m.choosePays(tiers.getPays().getId());
            if (d != null ? d.getId() > 0 : false) {
                cloneObject(tiers.getPays(), d);
            }
        }
    }

    public void chooseVille(ValueChangeEvent ev) {
        if (ev != null) {
            update("txt_ville_secteur_");
            tiers.setSecteur(new Dictionnaire());
            long id = (long) ev.getNewValue();
            ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
            if (m != null) {
                Dictionnaire d = m.chooseVille(tiers.getPays(), id);
                if (d != null ? d.getId() > 0 : false) {
                    cloneObject(tiers.getVille(), d);
                }
            }
        }
    }

    public void chooseSecteur(ValueChangeEvent ev) {
        if (ev != null) {
            long id = (long) ev.getNewValue();
            ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
            if (m != null) {
                Dictionnaire d = m.chooseSecteur(tiers.getVille(), id);
                if (d != null ? d.getId() > 0 : false) {
                    cloneObject(tiers.getSecteur(), d);
                }
            }
        }
    }

    public void activeContact(YvsBaseTiersTelephone bean) {
        if (bean != null) {
            if (!bean.getPrincipal()) {
                for (YvsBaseTiersTelephone c : contacts) {
                    if (c.getPrincipal()) {
                        getErrorMessage("Il y'a déja un numéro principal");
                        return;
                    }
                }
            }
            bean.setPrincipal(!bean.getPrincipal());
            bean.setAuthor(currentUser);
            bean.setDateUpdate(new Date());
            dao.update(bean);
            int index = contacts.indexOf(bean);
            if (index > -1) {
                contacts.set(index, bean);
            }
            update("data_tiers");
        }
    }

    public void activeTiers(YvsBaseTiers bean) {
        if (bean != null) {
            if (!autoriser("base_tiers_update")) {
                openNotAcces();
                return;
            }
            bean.setActif(!bean.getActif());
            bean.setAuthor(currentUser);
            bean.setDateUpdate(new Date());
            dao.update(bean);
            if (actionClient) {
                for (YvsComClient y : bean.getClients()) {
                    y.setActif(bean.getActif());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                }
            }
            if (actionFournisseur) {
                for (YvsBaseFournisseur y : bean.getFournisseurs()) {
                    y.setActif(bean.getActif());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                }
            }
            if (actionEmploye) {
                for (YvsGrhEmployes y : bean.getEmployes()) {
                    y.setActif(bean.getActif());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                }
            }
            if (actionCommercial) {
                for (YvsComComerciale y : bean.getCommerciaux()) {
                    y.setActif(bean.getActif());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                }
            }
            int index = listTiers.indexOf(bean);
            if (index > -1) {
                listTiers.set(index, bean);
                update("data_tiers");
            }
        }
    }

    public void activeGenerationCode() {
        setGeneration(!isGeneration());
    }

    public void changeName() {
        if (isGeneration()) {
            tiers.setCodeTiers(getCodeTiers(selectTemplate, tiers.getSecteur_(), tiers.getNom(), tiers.getPrenom()));
            checkCode(tiers);
        }
    }

    public void init(boolean next) {
        loadTiers(initForm = false, next);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadTiers(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadTiers(initForm = true, true);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsBaseTiers> re = paginator.parcoursDynamicData("YvsBaseTiers", "y", "y.codeTiers,y.nom, y.prenom", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void initComptes() {
        comptesResult.clear();
        comptesResult.addAll(comptes);
        update("data_comptes_tiers");
    }

    public void searchByNum() {
        addParamCode(numSearch_);
    }

    public void findTiersByCode(String codeTiers) {
        addParamCode(codeTiers);
    }

    public Tiers findTiers(String num, boolean open) {
        findTiersByCode(num);
        Tiers a = findTiersResult(open);
        if (a != null ? a.getId() < 1 : true) {
            a.setCodeTiers(num);
            a.setError(true);
        }
        return a;
    }

    private Tiers findTiersResult(boolean open) {
        Tiers a = new Tiers();
        if (listTiers != null ? !listTiers.isEmpty() : false) {
            if (listTiers.size() > 1) {
                if (open) {
                    openDialog("dlgListTiers");
                }
            } else {
                YvsBaseTiers y = listTiers.get(0);
                a = UtilTiers.buildBeanTiers(y);
                a.setSelectActif(true);
            }
            a.setError(false);
        }
        return a;
    }

    public void searchCompte() {
        String num = tiers.getCompte().getNumCompte();
        tiers.setCompteCollectif(0);
        tiers.getCompte().setId(0);
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
            if (service != null) {
                service.findCompteByNum(num);
                tiers.getCompte().setError(service.getListComptes().isEmpty());
                if (service.getListComptes() != null ? !service.getListComptes().isEmpty() : false) {
                    if (service.getListComptes().size() > 1) {
                        openDialog("dlgListComptes");
                        update("data_comptes_tiers");
                    } else {
                        YvsBasePlanComptable c = service.getListComptes().get(0);
                        tiers.setCompteCollectif(c.getId());
                        tiers.setCompte(UtilCompta.buildBeanCompte(c));
                    }
                    tiers.getCompte().setError(false);
                } else {
                    tiers.getCompte().setError(true);
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
        paginator.getParams().clear();
        _first = true;
        if (!load) {
            loadTiers(initForm = true, true);
        }
        update("blog_plus_option_tiers");
    }

    public void addParamCode(String code) {
        ParametreRequete p;
        if (code != null ? code.trim().length() > 0 : false) {
            code = code.trim();
            p = new ParametreRequete(null, "codeTiers", code.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.codeTiers)", "codeTiers", code.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nom)", "codeTiers", "%" + code.toUpperCase() + "%", "LIKE", "OR"));
        } else {
            p = new ParametreRequete("y.codeTiers", "codeTiers", null);
        }
        paginator.addParam(p);
        loadTiers(initForm = true, true);
    }

    public void addParamPays() {
        ParametreRequete p;
        if (paysSearch > 0) {
            p = new ParametreRequete("y.pays", "pays", new YvsDictionnaire(paysSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.pays", "pays", null);
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
        loadTiers(initForm = true, true);
    }

    public void addParamVille() {
        ParametreRequete p;
        if (villeSearch > 0) {
            p = new ParametreRequete("y.ville", "ville", new YvsDictionnaire(villeSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.ville", "ville", null);
            secteurSearch = 0;
            addParamSecteur();
        }
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            m.loadSecteurs(new YvsDictionnaire(villeSearch));
        }
        paginator.addParam(p);
        loadTiers(initForm = true, true);
    }

    public void addParamSecteur() {
        ParametreRequete p;
        if (secteurSearch > 0) {
            p = new ParametreRequete("y.secteur", "secteur", new YvsDictionnaire(secteurSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.secteur", "secteur", null);
        }
        paginator.addParam(p);
        loadTiers(initForm = true, true);
    }

    public void addParamCategorie() {
        ParametreRequete p;
        if (categorieSearch > 0) {
            p = new ParametreRequete("y.categorieComptable", "categorieComptable", new YvsBaseCategorieComptable(categorieSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.categorieComptable", "categorieComptable", null);
        }
        paginator.addParam(p);
        loadTiers(initForm = true, true);
    }

    public void addParamActif() {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", actifSearch);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        loadTiers(initForm = true, true);
    }

    public void addParamRation() {
        ParametreRequete p = new ParametreRequete("y.personnel", "personnel", rationSearch);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        loadTiers(initForm = true, true);
    }

    public void addParamPersonnel() {
        ParametreRequete p = new ParametreRequete("y.personnel", "personnel", true);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        loadTiersActif(true);
    }

    public void addParamCompte() {
        ParametreRequete p;
        if (compteSearch != null ? compteSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.compteCollectif.numCompte", "numCompte", compteSearch + "%");
            p.setOperation(" LIKE ");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.compteCollectif.numCompte", "numCompte", null);
        }
        paginator.addParam(p);
        loadTiers(initForm = true, true);
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("y.dateSave", "dateSave", null);
        if (date_) {
            p = new ParametreRequete("y.dateSave", "dateSave", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadTiers(initForm = true, true);
    }

    public void addParamType() {
        addParamType(true);
    }

    public void addParamType(boolean load) {
        if (typeSearch != null ? typeSearch.trim().length() > 0 : false) {
            switch (typeSearch) {
                case "S": {
                    addParamFsseur(null);
                    addParamSte(true);
                    addParamClt(null);
                    addParamFab(null);
                    addParamRep(null);
                    addParamEmploye(null);
                    break;
                }
                case "F": {
                    addParamFsseur(true);
                    addParamSte(null);
                    addParamClt(null);
                    addParamFab(null);
                    addParamRep(null);
                    addParamEmploye(null);
                    break;
                }
                case "C": {
                    addParamFsseur(null);
                    addParamSte(null);
                    addParamClt(true);
                    addParamFab(null);
                    addParamRep(null);
                    addParamEmploye(null);
                    break;
                }
                case "B": {
                    addParamFsseur(null);
                    addParamSte(null);
                    addParamClt(null);
                    addParamFab(true);
                    addParamRep(null);
                    addParamEmploye(null);
                    break;
                }
                case "R": {
                    addParamFsseur(null);
                    addParamSte(null);
                    addParamClt(null);
                    addParamFab(null);
                    addParamRep(true);
                    addParamEmploye(null);
                    break;
                }
                case "SF": {
                    addParamFsseur(true);
                    addParamSte(true);
                    addParamClt(null);
                    addParamFab(null);
                    addParamRep(null);
                    addParamEmploye(null);
                    break;
                }
                case "SC": {
                    addParamFsseur(null);
                    addParamSte(true);
                    addParamClt(true);
                    addParamFab(null);
                    addParamRep(null);
                    addParamEmploye(null);
                    break;
                }
                case "SB": {
                    addParamFsseur(null);
                    addParamSte(true);
                    addParamClt(null);
                    addParamFab(true);
                    addParamRep(null);
                    addParamEmploye(null);
                    break;
                }
                case "SR": {
                    addParamFsseur(null);
                    addParamSte(true);
                    addParamClt(null);
                    addParamFab(null);
                    addParamRep(true);
                    addParamEmploye(null);
                    break;
                }
                case "SFB": {
                    addParamFsseur(true);
                    addParamSte(true);
                    addParamClt(null);
                    addParamFab(true);
                    addParamRep(null);
                    addParamEmploye(null);
                    break;
                }
                case "E": {
                    addParamFsseur(null);
                    addParamSte(null);
                    addParamClt(null);
                    addParamFab(null);
                    addParamRep(null);
                    addParamEmploye(true);
                    break;
                }
                default: {
                    addParamEmploye(null);
                    addParamFsseur(null);
                    addParamSte(null);
                    addParamClt(null);
                    addParamFab(null);
                    addParamRep(null);
                    break;
                }
            }
        } else {
            addParamEmploye(null);
            addParamFsseur(null);
            addParamSte(null);
            addParamClt(null);
            addParamFab(null);
            addParamRep(null);
        }
        if (load) {
            loadTiers(initForm = true, true);
        }
    }

    public void addParamSte(Boolean steSearch) {
        ParametreRequete p = new ParametreRequete("y.stSociete", "stSociete", steSearch);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
    }

    public void addParamFsseur(Boolean fsseurSearch) {
        ParametreRequete p = new ParametreRequete("y.fournisseur", "fournisseur", fsseurSearch);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
    }

    public void addParamClt(Boolean cltSearch) {
        ParametreRequete p = new ParametreRequete("y.client", "client", cltSearch);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
    }

    public void addParamFab(Boolean fabSearch) {
        ParametreRequete p = new ParametreRequete("y.fabriquant", "fabriquant", fabSearch);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
    }

    public void addParamRep(Boolean repSearch) {
        ParametreRequete p = new ParametreRequete("y.representant", "representant", repSearch);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
    }

    public void addParamEmploye(Boolean repSearch) {
        ParametreRequete p = new ParametreRequete("y.employe", "employe", repSearch);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
    }

    public void selectCategorie(YvsBaseCategorieComptable bean) {
        CategorieComptable c = UtilCom.buildBeanCategorieComptable(bean);
        cloneObject(tiers.getCategorieComptable(), c);
    }

    public void uploadFile(FileUploadEvent event) {
        if (event != null) {
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
                Logger.getLogger(ManagedTiers.class.getName()).log(Level.SEVERE, null, ex);
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
                            update("form_tiers_load");
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
                    champ = new String[]{"societe", "code"};
                    val = new Object[]{currentAgence.getSociete(), t.getCodeTiers()};
                    List<YvsBaseTiers> l = dao.loadNameQueries("YvsBaseTiers.findByCode", champ, val);
                    if (l != null ? l.isEmpty() : true) {
                        t.setId(null);
                        t.setDateSave(new Date());
                        t.setDateUpdate(new Date());
                        t = (YvsBaseTiers) dao.save1(t);
                        if (listTiers.size() < getNbMax()) {
                            listTiers.add(0, t);
                        }
                    }
                }
            }
            update("data_tiers");
            succes();
        }
    }

    public void copie(YvsBaseTiersTemplate y) {
        copie(y, tiers);
        update("blog_secteur_tiers");
    }

    public YvsBaseTiersTemplate copie(YvsBaseTiers y) {
        if (y != null ? y.getId() > 0 : false) {
            YvsBaseTiersTemplate t = new YvsBaseTiersTemplate();
            t.setPays(y.getPays());
            t.setVille(y.getVille());
            t.setSecteur(y.getSecteur());
            t.setCompteCollectif(new YvsBasePlanComptable(y.getCompteCollectif().getId()));
            t.setCompteCollectif(y.getCompteCollectif());
            t.setCategorieComptable(y.getCategorieComptable());
            t.setMdr(y.getMdr());
//            t.setComission(y.getComission());
//            t.setRistourne(y.getRistourne());
        }
        return null;
    }

    public void copie(YvsBaseTiersTemplate y, Tiers tiers) {
        if (y != null ? y.getId() > 0 : false) {
            if (tiers == null) {
                tiers = new Tiers();
            }
            tiers.setTemplate(y);
            tiers.setPays(y.getPays() != null ? new Dictionnaire(y.getPays().getId(), y.getPays().getLibele(), y.getPays().getAbreviation()) : new Dictionnaire());
            tiers.setVille(y.getVille() != null ? new Dictionnaire(y.getVille().getId(), y.getVille().getLibele(), y.getVille().getAbreviation()) : new Dictionnaire());
            tiers.setSecteur(y.getSecteur() != null ? new Dictionnaire(y.getSecteur().getId(), y.getSecteur().getLibele(), y.getSecteur().getAbreviation()) : new Dictionnaire());
            tiers.setCompteCollectif(y.getCompteCollectif() != null ? y.getCompteCollectif().getId() : 0);
            tiers.setCompte(y.getCompteCollectif() != null ? UtilCompta.buildBeanCompte(y.getCompteCollectif()) : new Comptes());
            if (y.getCategorieComptable() != null) {
                tiers.setCategorieComptable(new CategorieComptable(y.getCategorieComptable().getId(), y.getCategorieComptable().getCode(), y.getCategorieComptable().getNature()));
            }
//            categorieTarifaire = y.getCategorieTarifaire() != null ? y.getCategorieTarifaire().getId() : 0;
            tiers.setModelDeRglt(y.getMdr() != null ? y.getMdr().getId().intValue() : 0);
            tiers.setPlanComission(y.getComission() != null ? y.getComission().getId() : 0);
            tiers.setPlanRistourne(y.getRistourne() != null ? y.getRistourne().getId() : 0);
            tiers.setCodeTiers(getCodeTiers(tiers));
            checkCode(tiers);
        }
    }

    public void checkCode(Tiers tiers) {
        tiers.setCodeTiers(checkCode(tiers.getCodeTiers(), tiers.getId()));
    }

    public String checkCode(String code, long id) {
        List<YvsBaseTiers> lt = dao.loadNameQueries("YvsBaseTiers.findByCode", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), code}, 0, 1);
        if (lt != null ? !lt.isEmpty() : false) {
            if (!lt.get(0).getId().equals(id)) {
                for (int i = 1; i < 1000; i++) {
                    String num = code + (i < 10 ? "00" : (i < 100 ? "0" + i : i)) + i;
                    lt = dao.loadNameQueries("YvsBaseTiers.findByCode", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), num}, 0, 1);
                    if (lt != null ? (lt.isEmpty() ? true : (lt.get(0).getId().equals(id))) : true) {
                        code = num;
                        break;
                    }
                }
            } else {
                code += "001";
            }
        } else {
            code += "001";
        }
        return code;
    }

    public String getCodeTiers(Tiers tiers) {
        return getCodeTiers(tiers.getTemplate(), tiers.getSecteur_(), tiers.getNom(), tiers.getPrenom());
    }

    public String getCodeTiers(YvsBaseTiersTemplate template, Dictionnaire secteur, String nom, String prenom) {
        String code = "";
        if (nom != null && (template != null ? template.getId() > 0 : false)) {
            if (template.getAddSecteur() && (secteur != null ? secteur.getId() > 0 : false)) {
                code = secteur.getAbreviation().length() > template.getTailleSecteur() ? secteur.getAbreviation().substring(0, template.getTailleSecteur()) : secteur.getAbreviation();
            }
            if (template.getAddSeparateur()) {
                if (template.getAddNom()) {
                    code += template.getSeparateur() + ((nom.length() > template.getTailleNom()) ? nom.substring(0, template.getTailleNom()) : nom);
                    if (template.getAddPrenom()) {
                        code += template.getSeparateur() + ((prenom.length() > template.getTaillePrenom()) ? prenom.substring(0, template.getTaillePrenom()) : prenom);
                    }
                } else {
                    if (template.getAddPrenom()) {
                        code += template.getSeparateur() + ((prenom.length() > template.getTaillePrenom()) ? prenom.substring(0, template.getTaillePrenom()) : prenom);
                    }
                }
            } else {
                if (template.getAddNom()) {
                    code += ((nom.length() > template.getTailleNom()) ? nom.substring(0, template.getTailleNom()) : nom);
                    if (template.getAddPrenom()) {
                        code += ((prenom.length() > template.getTaillePrenom()) ? prenom.substring(0, template.getTaillePrenom()) : prenom);
                    }
                } else {
                    if (template.getAddPrenom()) {
                        code += ((prenom.length() > template.getTaillePrenom()) ? prenom.substring(0, template.getTaillePrenom()) : prenom);
                    }
                }
            }
        }
        code = (code != null) ? code.trim().length() > 0 ? code.toUpperCase() : "" : "";
        code = (code.length() > 2) ? ((code.trim().charAt(code.length() - 1) == '-') ? code.substring(0, code.length() - 1) : code) : code;
        return code;
    }

    public void initTiers(Tiers bean) {
        if (bean == null) {
            bean = new Tiers();
        }
        paginator.addParam(new ParametreRequete("y.codeTiers", "codeTiers", null));
        loadTiers(true, true);
        bean.setSelectActif(false);
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void saveLiaison() {
        if (tiers.getId() > 0) {
            if ((codeExterne != null) ? codeExterne.trim().length() > 2 : false) {
                //il ne dois pas déjà exister un mappage pour ce client
                YvsExtTiers y = (YvsExtTiers) dao.loadOneByNameQueries("YvsExtTiers.findByTiers", new String[]{"tiers"}, new Object[]{new YvsBaseTiers(tiers.getId())});
                if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                    y.setCodeExterne(codeExterne);
                    y.setDateSave(y.getDateSave());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                } else {
                    y = new YvsExtTiers();
                    y.setTiers(new YvsBaseTiers(tiers.getId()));
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
            getErrorMessage("Aucun tiers n'est selectionné !");
        }
    }

    public void addClient() {
        if (tiers != null ? tiers.getId() > 0 : false) {
            ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
            if (w != null) {
                YvsComClient y = w.saveNew(client, selectTiers, false);
                if (y != null ? y.getId() > 0 : false) {
                    tiers.getProfils().add(new Profil(y.getId(), y.getCodeClient(), y.getNom(), y.getPrenom(), y.getCompte(), Constantes.BASE_TIERS_CLIENT, y.getActif()));
                    if (!tiers.isClient()) {
                        tiers.setClient(true);
                        selectTiers.setClient(true);
                        dao.update(selectTiers);
                        int idx = listTiers.indexOf(selectTiers);
                        if (idx > -1) {
                            listTiers.set(idx, selectTiers);
                            update("data_tiers");
                        }
                        update("blog_type_tiers");
                    }
                    succes();
                }
            }
            update("blog_profil_tiers:data_profil_tiers");
        } else {
            getErrorMessage("Vous devez selectionner un tiers");
        }
    }

    public void addFournisseur() {
        if (tiers != null ? tiers.getId() > 0 : false) {
            ManagedFournisseur w = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
            if (w != null) {
                YvsBaseFournisseur y = w.saveNew(fournisseur, selectTiers, false);
                if (y != null ? y.getId() > 0 : false) {
                    tiers.getProfils().add(new Profil(y.getId(), y.getCodeFsseur(), y.getNom(), y.getPrenom(), y.getCompte(), Constantes.BASE_TIERS_FOURNISSEUR, y.getActif()));
                    if (!tiers.isFournisseur()) {
                        tiers.setFournisseur(true);
                        selectTiers.setFournisseur(true);
                        dao.update(selectTiers);
                        int idx = listTiers.indexOf(selectTiers);
                        if (idx > -1) {
                            listTiers.set(idx, selectTiers);
                            update("data_tiers");
                        }
                        update("blog_type_tiers");
                    }
                    succes();
                }
            }
            update("blog_profil_tiers:data_profil_tiers");
        } else {
            getErrorMessage("Vous devez selectionner un tiers");
        }
    }

    public void addEmploye() {
        if (tiers != null ? tiers.getId() > 0 : false) {
            if (employe != null ? employe.getId() > 0 : false) {
                YvsGrhEmployes y = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findById", new String[]{"id"}, new Object[]{employe.getId()});
                if (y != null ? (y.getId() > 0 ? y.getCompteTiers() != null ? y.getCompteTiers().getId() < 1 : true : true) : false) {
                    y.setCompteTiers(selectTiers);
                    dao.update(y);
                    tiers.getProfils().add(new Profil(y.getId(), y.getMatricule(), y.getNom(), y.getPrenom(), y.getCompteCollectif(), Constantes.BASE_TIERS_EMPLOYE, y.getActif()));
                    if (!tiers.isEmploye()) {
                        tiers.setEmploye(true);
                        selectTiers.setEmploye(true);
                        dao.update(selectTiers);
                        int idx = listTiers.indexOf(selectTiers);
                        if (idx > -1) {
                            listTiers.set(idx, selectTiers);
                            update("data_tiers");
                        }
                        update("blog_type_tiers");
                    }
                    succes();
                    update("blog_profil_tiers:data_profil_tiers");
                } else {
                    getErrorMessage("Cet employé est déja associé à un compte tiers!");
                }
            }
        } else {
            getErrorMessage("Vous devez selectionner un tiers");
        }
    }

    public void initProfil(String type) {
        switch (type) {
            case "Client": {
                client = new Client(0L, tiers);
                ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
                if (w != null) {
                    client.setCodeClient(w.checkCode(client.getCodeClient(), client.getId(), true));
                }
                update("blog_tiers_client");
                break;
            }
            case "Fournisseur": {
                fournisseur = new Fournisseur(0L, tiers);
                ManagedFournisseur w = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
                if (w != null) {
                    fournisseur.setCodeFsseur(w.checkCode(fournisseur.getCodeFsseur(), fournisseur.getId(), true));
                }
                update("blog_tiers_fournisseur");
                break;
            }
            case "Employe":
                YvsGrhEmployes y = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByCompteTiers", new String[]{"tiers", "societe"}, new Object[]{new YvsBaseTiers(tiers.getId()), currentAgence.getSociete()});
                if (y != null ? y.getId() > 0 : false) {
                    employe = UtilGrh.buildBeanSimpleEmploye(y);
                } else {
                    employe = new yvs.grh.bean.Employe(tiers.getCodeTiers());
                }
                update("blog_tiers_employe");
                break;
        }
    }

    public boolean chechProfil(String type) {
        if (tiers != null ? tiers.getId() < 1 : true) {
            return true;
        }
        for (Profil p : tiers.getProfils()) {
            if (type.equals(p.getType())) {
                return true;
            }
        }
        return false;
    }

    public void findOneEmploye() {
        employe.setId(0);
        if (getEmploye().getMatricule() != null) {
            YvsGrhEmployes e = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByCodeUsersS", new String[]{"codeUsers", "agence"}, new Object[]{"%" + getEmploye().getMatricule() + "%", currentUser.getAgence().getSociete()});
            employe = UtilGrh.buildBeanEmploye(e);
        }
    }

    public void fusionner(boolean fusionne) {
        try {
            if (!autoriser("base_user_fusion")) {
                openNotAcces();
                return;
            }
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty() ? ids.size() > 1 : false) {
                long newValue = listTiers.get(ids.get(0)).getId();
                if (!fusionne) {
                    String oldValue = "0";
                    for (int i : ids) {
                        if (listTiers.get(i).getId() != newValue) {
                            oldValue += "," + listTiers.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_base_tiers", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                listTiers.remove(new YvsBaseTiers(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = listTiers.get(idx).getNom_prenom();
                    } else {
                        YvsBaseTiers c = (YvsBaseTiers) dao.loadOneByNameQueries("YvsBaseTiers.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                            fusionneTo = c.getNom_prenom();
                        }
                    }
                    YvsBaseTiers c;
                    for (int i : ids) {
                        long oldValue = listTiers.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(listTiers.get(i).getNom_prenom());
                            }
                        } else {
                            c = (YvsBaseTiers) dao.loadOneByNameQueries("YvsBaseTiers.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                                fusionnesBy.add(c.getNom_prenom());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 tiers");
            }
        } catch (NumberFormatException ex) {
        }
    }

//    public List<Profil> loadProfilsTiers(YvsBaseTiers tiers) {
//        if (tiers != null ? tiers.getId() > 0 : false) {
//            // trouve le fournisseur
//            YvsBaseFournisseur fseur = (YvsBaseFournisseur) dao.loadOneByNameQueries("YvsBaseFournisseur.findByTiers", new String[]{"tiers"}, new Object[]{tiers});
//            if (fseur != null) {
//                profiles.add(new Profil(fseur.getId(), fseur.getCodeFsseur(), fseur.getNom(), fseur.getPrenom(), fseur.getCompte(), "Fournisseur", fseur.getActif(), 1));
//            }
//            YvsComClient client = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findByTiers", new String[]{"tiers"}, new Object[]{tiers});
//            if (client != null) {
//                profiles.add(new Profil(client.getId(), client.getCodeClient(), client.getNom(), client.getPrenom(), client.getCompte(), "Client", client.getActif(), 2));
//            }
//            YvsGrhEmployes emps = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByCompteTiersActif", new String[]{"tiers", "societe"}, new Object[]{tiers, currentAgence.getSociete()});
//            if (emps != null) {
//                profiles.add(new Profil(emps.getId(), emps.getMatricule(), emps.getNom(), emps.getPrenom(), emps.getCompteCollectif(), "Employe", emps.getActif(), 3));
//            }
//            YvsComComerciale comm = (YvsComComerciale) dao.loadOneByNameQueries("YvsComComerciale.findByTiers", new String[]{"tiers"}, new Object[]{tiers});
//            if (comm != null) {
//                profiles.add(new Profil(comm.getId(), comm.getCodeRef(), comm.getNom(), comm.getPrenom(), null, "Commercial", comm.getActif(), 4));
//            }
//        }
//        return profiles;
//    }
    public Tiers buildTiersByProfil(Long id, String table) {
        Tiers tiers = new Tiers();
        if (id != null ? id > 0 : false) {
            YvsBaseTiers entity;
            switch (table) {
                case Constantes.BASE_TIERS_FOURNISSEUR:
                    entity = (YvsBaseTiers) dao.loadOneByNameQueries("YvsBaseFournisseur.findTiersById", new String[]{"id"}, new Object[]{id});
                    break;
                case Constantes.BASE_TIERS_COMMERCIAL:
                    entity = (YvsBaseTiers) dao.loadOneByNameQueries("YvsComComerciale.findTiersById", new String[]{"id"}, new Object[]{id});
                    break;
                case Constantes.BASE_TIERS_CLIENT:
                    entity = (YvsBaseTiers) dao.loadOneByNameQueries("YvsComClient.findTiersById", new String[]{"id"}, new Object[]{id});
                    break;
                case Constantes.BASE_TIERS_EMPLOYE:
                    entity = (YvsBaseTiers) dao.loadOneByNameQueries("YvsGrhEmployes.findTiersById", new String[]{"id"}, new Object[]{id});
                    break;
                default:
                    entity = (YvsBaseTiers) dao.loadOneByNameQueries("YvsBaseTiers.findById", new String[]{"id"}, new Object[]{id});
                    break;
            }
            if (entity != null) {
                tiers = UtilTiers.buildBeanTiers(entity);
                for (Profil p : tiers.getProfils()) {
                    if (p.getType().equals(table) && p.getId() == id) {
                        tiers.setSelectProfil(p);
                        break;
                    }
                }
                if (tiers.getSelectProfil() != null ? tiers.getSelectProfil().getId() < 1 : true) {
                    tiers.setSelectProfil(new Profil(entity.getId(), entity.getCodeTiers(), entity.getNom(), entity.getPrenom(), entity.getCompteCollectif(), Constantes.BASE_TIERS_TIERS, entity.getActif(), 5, entity.getId(), Constantes.BASE_TIERS_TIERS));
                }
            }
        }
        return tiers;
    }

    public void buildCodeRation(boolean force) {
        if (tiers.isPersonnel() ? force || !Util.asString(tiers.getCodeRation()) : false) {
            int taille = currentStock != null ? currentStock.getTailleCodeRation() : 5;
            tiers.setCodeRation(getRandomString(taille));
        }
    }

    private String getClientTiers(YvsBaseTiers tiers, String nature) {
        String compte = "";
        if (tiers != null ? tiers.getId() > 0 : false) {
            YvsComClient y = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findByTiers", new String[]{"tiers"}, new Object[]{tiers});
            if (y != null ? y.getId() > 0 : false) {
                switch (nature) {
                    case "I":
                        compte = y.getId() + "";
                    case "R":
                        compte = y.getCodeClient();
                        break;
                    case "N":
                        compte = y.getNom_prenom();
                    case "C":
                        compte = y.getCompte().getNumCompte();
                        break;
                }
            }
        }
        return compte;
    }

    private String getFournisseurTiers(YvsBaseTiers tiers, String nature) {
        String compte = "";
        if (tiers != null ? tiers.getId() > 0 : false) {
            YvsBaseFournisseur y = (YvsBaseFournisseur) dao.loadOneByNameQueries("YvsBaseFournisseur.findByTiers", new String[]{"tiers"}, new Object[]{tiers});
            if (y != null ? y.getId() > 0 : false) {
                switch (nature) {
                    case "I":
                        compte = y.getId() + "";
                    case "R":
                        compte = y.getCodeFsseur();
                        break;
                    case "N":
                        compte = y.getNom_prenom();
                    case "C":
                        compte = y.getCompte().getNumCompte();
                        break;
                }
            }
        }
        return compte;
    }

    private String getEmployeTiers(YvsBaseTiers tiers, String nature) {
        String compte = "";
        if (tiers != null ? tiers.getId() > 0 : false) {
            YvsGrhEmployes y = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByTiers", new String[]{"tiers"}, new Object[]{tiers});
            if (y != null ? y.getId() > 0 : false) {
                switch (nature) {
                    case "I":
                        compte = y.getId() + "";
                    case "R":
                        compte = y.getMatricule();
                        break;
                    case "N":
                        compte = y.getNom_prenom();
                    case "C":
                        compte = y.getCompteCollectif().getNumCompte();
                        break;
                }
            }
        }
        return compte;
    }

    private String getCurrentTiers(YvsBaseTiers tiers, String nature) {
        String compte = "";
        if (tiers != null ? tiers.getId() > 0 : false) {
            switch (nature) {
                case "I":
                    compte = tiers.getId() + "";
                case "R":
                    compte = tiers.getCodeTiers();
                    break;
                case "N":
                    compte = tiers.getNom_prenom();
                case "C":
                    compte = tiers.getCompteCollectif().getNumCompte();
                    break;
            }
        }
        return compte;
    }

    public String searchCompteTiers(YvsBaseTiers tiers, String nature, String type) {
        String compte = "";
        if (type != null) {
            switch (type) {
                case "C": {
                    compte = getClientTiers(tiers, nature);
                    break;
                }
                case "F": {
                    compte = getFournisseurTiers(tiers, nature);
                    break;
                }
                case "E": {
                    compte = getEmployeTiers(tiers, nature);
                    break;
                }
                default: {
                    compte = getCurrentTiers(tiers, nature);
                    break;
                }
            }
        } else {
            compte = getClientTiers(tiers, nature) + "," + getFournisseurTiers(tiers, nature) + "," + getEmployeTiers(tiers, nature) + "," + getCurrentTiers(tiers, nature);
        }
        return compte;
    }

    public void print() {
        try {
            Map<String, Object> param = new HashMap<>();
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
            param.put("AGENCE", currentAgence.getId().intValue());
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
            param.put("TYPE", typeSearch);
            param.put("PAYS", (int) paysSearch);
            param.put("VILLE", (int) villeSearch);
            param.put("SECTEUR", (int) secteurSearch);
            executeReport("tiers", param);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedTiers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void auditTiers() {
        try {
            Calendar time = Calendar.getInstance();
            time.add(Calendar.DATE, -ecartInactif);
            Date dateFin = time.getTime();
            tiersRemove.clear();

            //Tiers
            String query = "SELECT t.id FROM yvs_base_tiers t WHERE t.societe = ? AND t.date_save < ? AND t.actif IS TRUE";
            List<Long> tiers = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateFin, 2)});
            if (tiers != null ? !tiers.isEmpty() : false) {
                if (!tiers.isEmpty()) {
                    query = "SELECT DISTINCT a.compte_tiers FROM yvs_compta_content_journal a INNER JOIN yvs_compta_pieces_comptable p ON a.piece = p.id INNER JOIN yvs_base_tiers t ON (a.compte_tiers = t.id AND a.table_tiers = 'TIERS') WHERE t.societe = ? AND p.date_piece >= ? ";
                    List<Long> contents = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateFin, 2)});
                    if (contents != null ? !contents.isEmpty() : false) {
                        tiers.removeAll(contents);
                    }
                }
                if (!tiers.isEmpty()) {
                    query = "SELECT DISTINCT a.id_tiers FROM yvs_compta_caisse_doc_divers a INNER JOIN yvs_base_tiers t ON (a.id_tiers = t.id AND a.table_tiers = 'TIERS') WHERE t.societe = ? AND a.date_doc >= ? ";
                    List<Long> divers = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateFin, 2)});
                    if (divers != null ? !divers.isEmpty() : false) {
                        tiers.removeAll(divers);
                    }
                }
                if (!tiers.isEmpty()) {
                    query = "SELECT DISTINCT e.compte_tiers FROM yvs_grh_missions m INNER JOIN yvs_grh_employes e ON m.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE a.societe = ? AND m.date_mission >= ? ";
                    List<Long> missions = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateFin, 2)});
                    if (missions != null ? !missions.isEmpty() : false) {
                        tiers.removeAll(missions);
                    }
                }
                if (!tiers.isEmpty()) {
                    query = "SELECT DISTINCT e.compte_tiers FROM yvs_grh_bulletins b INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id INNER JOIN yvs_grh_employes e ON c.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE a.societe = ? AND b.date_calcul >= ? ";
                    List<Long> bulletins = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateFin, 2)});
                    if (bulletins != null ? !bulletins.isEmpty() : false) {
                        tiers.removeAll(bulletins);
                    }
                }
                if (!tiers.isEmpty()) {
                    query = "SELECT DISTINCT c.tiers FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id INNER JOIN yvs_agences a ON e.agence = a.id INNER JOIN yvs_com_client c ON d.client = c.id WHERE a.societe = ? AND e.date_entete >= ? ";
                    List<Long> ventes = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateFin, 2)});
                    if (ventes != null ? !ventes.isEmpty() : false) {
                        tiers.removeAll(ventes);
                    }
                }
                if (!tiers.isEmpty()) {
                    query = "SELECT DISTINCT c.tiers FROM yvs_compta_acompte_client a INNER JOIN yvs_com_client c ON a.client = c.id INNER JOIN yvs_base_tiers t ON c.tiers = t.id WHERE t.societe = ? AND a.date_acompte >= ? ";
                    List<Long> acomptes = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateFin, 2)});
                    if (acomptes != null ? !acomptes.isEmpty() : false) {
                        tiers.removeAll(acomptes);
                    }
                }
                if (!tiers.isEmpty()) {
                    query = "SELECT DISTINCT c.tiers FROM yvs_compta_credit_client a INNER JOIN yvs_com_client c ON a.client = c.id INNER JOIN yvs_base_tiers t ON c.tiers = t.id WHERE t.societe = ? AND a.date_credit >= ? ";
                    List<Long> credits = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateFin, 2)});
                    if (credits != null ? !credits.isEmpty() : false) {
                        tiers.removeAll(credits);
                    }
                }
                if (!tiers.isEmpty()) {
                    query = "SELECT DISTINCT c.tiers FROM yvs_compta_content_journal a INNER JOIN yvs_compta_pieces_comptable p ON a.piece = p.id INNER JOIN yvs_com_client c ON (a.compte_tiers = c.id AND a.table_tiers = 'CLIENT') INNER JOIN yvs_base_tiers t ON c.tiers = t.id WHERE t.societe = ? AND p.date_piece >= ? ";
                    List<Long> contents = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateFin, 2)});
                    if (contents != null ? !contents.isEmpty() : false) {
                        tiers.removeAll(contents);
                    }
                }
                if (!tiers.isEmpty()) {
                    query = "SELECT DISTINCT c.tiers FROM yvs_compta_content_journal a INNER JOIN yvs_compta_pieces_comptable p ON a.piece = p.id INNER JOIN yvs_com_client c ON (a.compte_tiers = c.id AND a.table_tiers = 'CLIENT') INNER JOIN yvs_base_tiers t ON c.tiers = t.id WHERE t.societe = ? AND p.date_piece >= ? ";
                    List<Long> divers = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateFin, 2)});
                    if (divers != null ? !divers.isEmpty() : false) {
                        tiers.removeAll(divers);
                    }
                }
                if (!tiers.isEmpty()) {
                    query = "SELECT DISTINCT f.tiers FROM yvs_com_doc_achats d INNER JOIN yvs_agences a ON d.agence = a.id INNER JOIN yvs_base_fournisseur f ON d.fournisseur = f.id WHERE a.societe = ? AND d.date_doc >= ? ";
                    List<Long> achat = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateFin, 2)});
                    if (achat != null ? !achat.isEmpty() : false) {
                        tiers.removeAll(achat);
                    }
                }
                if (!tiers.isEmpty()) {
                    query = "SELECT DISTINCT c.tiers FROM yvs_compta_acompte_fournisseur a INNER JOIN yvs_base_fournisseur c ON a.fournisseur = c.id INNER JOIN yvs_base_tiers t ON c.tiers = t.id WHERE t.societe = ? AND a.date_acompte >= ? ";
                    List<Long> acomptes = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateFin, 2)});
                    if (acomptes != null ? !acomptes.isEmpty() : false) {
                        tiers.removeAll(acomptes);
                    }
                }
                if (!tiers.isEmpty()) {
                    query = "SELECT DISTINCT c.tiers FROM yvs_compta_credit_fournisseur a INNER JOIN yvs_base_fournisseur c ON a.fournisseur = c.id INNER JOIN yvs_base_tiers t ON c.tiers = t.id WHERE t.societe = ? AND a.date_credit >= ? ";
                    List<Long> credits = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateFin, 2)});
                    if (credits != null ? !credits.isEmpty() : false) {
                        tiers.removeAll(credits);
                    }
                }
                if (!tiers.isEmpty()) {
                    query = "SELECT DISTINCT c.tiers FROM yvs_compta_content_journal a INNER JOIN yvs_compta_pieces_comptable p ON a.piece = p.id INNER JOIN yvs_base_fournisseur c ON (a.compte_tiers = c.id AND a.table_tiers = 'FOURNISSEUR') INNER JOIN yvs_base_tiers t ON c.tiers = t.id WHERE t.societe = ? AND p.date_piece >= ? ";
                    List<Long> contents = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateFin, 2)});
                    if (contents != null ? !contents.isEmpty() : false) {
                        tiers.removeAll(contents);
                    }
                }
                if (!tiers.isEmpty()) {
                    query = "SELECT DISTINCT c.tiers FROM yvs_compta_caisse_doc_divers a INNER JOIN yvs_base_fournisseur c ON (a.id_tiers = c.id AND a.table_tiers = 'FOURNISSEUR') INNER JOIN yvs_base_tiers t ON c.tiers = t.id WHERE t.societe = ? AND a.date_doc >= ? ";
                    List<Long> divers = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateFin, 2)});
                    if (divers != null ? !divers.isEmpty() : false) {
                        tiers.removeAll(divers);
                    }
                }
            }
            if (!tiers.isEmpty()) {
                tiersRemove = dao.loadNameQueries("YvsBaseTiers.findByIds", new String[]{"ids"}, new Object[]{tiers});
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedTiers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desactive(YvsBaseTiers y) {
        try {
            desactiveTiers(y);
            desactiveClient(y);
            desactiveFournisseur(y);
            desactiveCommercial(y);
            desactiveEmploye(y);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedTiers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desactiveTiers(YvsBaseTiers y) {
        try {
            y.setActif(false);
            y.setDateUpdate(new Date());
            y.setAuthor(currentUser);
            dao.update(y);
            int index = tiersRemove.indexOf(y);
            if (index > -1) {
                tiersRemove.set(index, y);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedTiers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desactiveClient(YvsBaseTiers tiers) {
        try {
            for (YvsComClient y : tiers.getClients()) {
                y.setActif(false);
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                dao.update(y);
            }
            int index = tiersRemove.indexOf(tiers);
            if (index > -1) {
                tiersRemove.set(index, tiers);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedTiers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desactiveFournisseur(YvsBaseTiers tiers) {
        try {
            for (YvsBaseFournisseur y : tiers.getFournisseurs()) {
                y.setActif(false);
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                dao.update(y);
            }
            int index = tiersRemove.indexOf(tiers);
            if (index > -1) {
                tiersRemove.set(index, tiers);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedTiers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desactiveEmploye(YvsBaseTiers tiers) {
        try {
            for (YvsGrhEmployes y : tiers.getEmployes()) {
                y.setActif(false);
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                dao.update(y);
            }
            int index = tiersRemove.indexOf(tiers);
            if (index > -1) {
                tiersRemove.set(index, tiers);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedTiers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desactiveCommercial(YvsBaseTiers tiers) {
        try {
            for (YvsComComerciale y : tiers.getCommerciaux()) {
                y.setActif(false);
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                dao.update(y);
            }
            int index = tiersRemove.indexOf(tiers);
            if (index > -1) {
                tiersRemove.set(index, tiers);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedTiers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desactive() {
        try {
            for (YvsBaseTiers y : tiersRemoveSelect) {
                desactive(y);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedTiers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desactiveTiers() {
        try {
            for (YvsBaseTiers y : tiersRemoveSelect) {
                desactiveTiers(y);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedTiers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desactiveClient() {
        try {
            for (YvsBaseTiers y : tiersRemoveSelect) {
                desactiveClient(y);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedTiers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desactiveFournisseur() {
        try {
            for (YvsBaseTiers y : tiersRemoveSelect) {
                desactiveFournisseur(y);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedTiers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desactiveEmploye() {
        try {
            for (YvsBaseTiers y : tiersRemoveSelect) {
                desactiveEmploye(y);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedTiers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desactiveCommercial() {
        try {
            for (YvsBaseTiers y : tiersRemoveSelect) {
                desactiveCommercial(y);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedTiers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
