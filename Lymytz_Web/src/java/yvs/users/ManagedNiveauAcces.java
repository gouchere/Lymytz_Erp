/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.dao.Options;
import yvs.dao.services.bases.Autorisations;
import yvs.dao.services.bases.ServiceAutorisation;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsAutorisationModule;
import yvs.entity.users.YvsAutorisationPageModule;
import yvs.entity.users.YvsAutorisationRessourcesPage;
import yvs.entity.users.YvsModule;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsNiveauUsers;
import yvs.entity.users.YvsPageModule;
import yvs.entity.users.YvsResourcePageGroup;
import yvs.entity.users.YvsRessourcesPage;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersGrade;
import yvs.parametrage.societe.ManagedSociete;
import yvs.parametrage.societe.Societe;
import yvs.util.Managed;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedNiveauAcces extends Managed<NiveauAcces, YvsNiveauAcces> implements Serializable {

    @ManagedProperty(value = "#{niveauAcces}")
    private NiveauAcces niveauAcces;
    private NiveauAcces niveau = new NiveauAcces();
    private NiveauAcces parent = new NiveauAcces();
    private YvsNiveauAcces selectedNiveau;
    private List<YvsNiveauAcces> listNiveauAcces, listSelectNiveauAcces;
    private List<YvsUsers> usersList;

    private Modules module = new Modules();
    private PageModule pageModule = new PageModule();
    private RessourcesPage ressourcePage = new RessourcesPage();
    private boolean updateNiveau, updateModule, updatePage, updateRessource;
    private List<YvsModule> listModules;
    private List<YvsPageModule> listPages;
    private List<YvsResourcePageGroup> groupes;

    private List<YvsUsersGrade> grades;
    private List<Autorisations> autorisations, autorisationsSave;
    ServiceAutorisation service;
    Autorisations acces;

    private boolean vueListeNiveau = true, selectNiveau, optionUpdate;
    private String nameTabPage = "Module", nameTabRessource = "Pages";

    private YvsModule entityModule;
    private YvsPageModule entityPage;

    private long societe, agenceNiveau, niveauCopy;
    private int moduleSearch, pageSearch;
    private String codeSearch, typeSearch, numSearch, usersSearch, oneSearch;
    private Boolean accesSearch;
    private boolean onlyModuleNiveau, onlyPageNiveau;

    private String tabIds;
    private String fusionneTo;
    private List<String> fusionnesBy;

    public ManagedNiveauAcces() {
        grades = new ArrayList<>();
        groupes = new ArrayList<>();
        listNiveauAcces = new ArrayList<>();
        listSelectNiveauAcces = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
        autorisations = new ArrayList<>();
        autorisationsSave = new ArrayList<>();
        listPages = new ArrayList<>();
        listModules = new ArrayList<>();
        usersList = new ArrayList<>();
    }

    public long getNiveauCopy() {
        return niveauCopy;
    }

    public void setNiveauCopy(long niveauCopy) {
        this.niveauCopy = niveauCopy;
    }

    public long getAgenceNiveau() {
        return agenceNiveau;
    }

    public void setAgenceNiveau(long agenceNiveau) {
        this.agenceNiveau = agenceNiveau;
    }

    public List<YvsUsers> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<YvsUsers> usersList) {
        this.usersList = usersList;
    }

    public List<YvsResourcePageGroup> getGroupes() {
        return groupes;
    }

    public void setGroupes(List<YvsResourcePageGroup> groupes) {
        this.groupes = groupes;
    }

    public boolean isOnlyModuleNiveau() {
        return onlyModuleNiveau;
    }

    public void setOnlyModuleNiveau(boolean onlyModuleNiveau) {
        this.onlyModuleNiveau = onlyModuleNiveau;
    }

    public boolean isOnlyPageNiveau() {
        return onlyPageNiveau;
    }

    public void setOnlyPageNiveau(boolean onlyPageNiveau) {
        this.onlyPageNiveau = onlyPageNiveau;
    }

    public String getOneSearch() {
        return oneSearch;
    }

    public void setOneSearch(String oneSearch) {
        this.oneSearch = oneSearch;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public String getUsersSearch() {
        return usersSearch;
    }

    public void setUsersSearch(String usersSearch) {
        this.usersSearch = usersSearch;
    }

    public NiveauAcces getNiveau() {
        return niveau;
    }

    public void setNiveau(NiveauAcces niveau) {
        this.niveau = niveau;
    }

    public Boolean getAccesSearch() {
        return accesSearch;
    }

    public void setAccesSearch(Boolean accesSearch) {
        this.accesSearch = accesSearch;
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public int getModuleSearch() {
        return moduleSearch;
    }

    public void setModuleSearch(int moduleSearch) {
        this.moduleSearch = moduleSearch;
    }

    public int getPageSearch() {
        return pageSearch;
    }

    public void setPageSearch(int pageSearch) {
        this.pageSearch = pageSearch;
    }

    public List<YvsModule> getListModules() {
        return listModules;
    }

    public void setListModules(List<YvsModule> listModules) {
        this.listModules = listModules;
    }

    public List<YvsPageModule> getListPages() {
        return listPages;
    }

    public void setListPages(List<YvsPageModule> listPages) {
        this.listPages = listPages;
    }

    public List<Autorisations> getAutorisationsSave() {
        return autorisationsSave;
    }

    public void setAutorisationsSave(List<Autorisations> autorisationsSave) {
        this.autorisationsSave = autorisationsSave;
    }

    public ServiceAutorisation getService() {
        return service;
    }

    public void setService(ServiceAutorisation service) {
        this.service = service;
    }

    public Autorisations getAcces() {
        return acces;
    }

    public void setAcces(Autorisations acces) {
        this.acces = acces;
    }

    public String getCodeSearch() {
        return codeSearch;
    }

    public void setCodeSearch(String codeSearch) {
        this.codeSearch = codeSearch;
    }

    public List<Autorisations> getAutorisations() {
        return autorisations;
    }

    public void setAutorisations(List<Autorisations> autorisations) {
        this.autorisations = autorisations;
    }

    public List<YvsUsersGrade> getGrades() {
        return grades;
    }

    public void setGrades(List<YvsUsersGrade> grades) {
        this.grades = grades;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
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

    public NiveauAcces getParent() {
        return parent;
    }

    public void setParent(NiveauAcces parent) {
        this.parent = parent;
    }

    public long getSociete() {
        return societe;
    }

    public void setSociete(long societe) {
        this.societe = societe;
    }

    public void setNameTabRessource(String nameTabRessource) {
        this.nameTabRessource = nameTabRessource;
    }

    public void setNameTabPage(String nameTabPage) {
        this.nameTabPage = nameTabPage;
    }

    public String getNameTabRessource() {
        return nameTabRessource;
    }

    public String getNameTabPage() {
        return nameTabPage;
    }

    public List<YvsNiveauAcces> getListSelectNiveauAcces() {
        return listSelectNiveauAcces;
    }

    public void setListSelectNiveauAcces(List<YvsNiveauAcces> listSelectNiveauAcces) {
        this.listSelectNiveauAcces = listSelectNiveauAcces;
    }

    public boolean isVueListeNiveau() {
        return vueListeNiveau;
    }

    public void setVueListeNiveau(boolean vueListeNiveau) {
        this.vueListeNiveau = vueListeNiveau;
    }

    public boolean isSelectNiveau() {
        return selectNiveau;
    }

    public void setSelectNiveau(boolean selectNiveau) {
        this.selectNiveau = selectNiveau;
    }

    public boolean isOptionUpdate() {
        return optionUpdate;
    }

    public void setOptionUpdate(boolean optionUpdate) {
        this.optionUpdate = optionUpdate;
    }

    public boolean isUpdateNiveau() {
        return updateNiveau;
    }

    public void setUpdateNiveau(boolean updateNiveau) {
        this.updateNiveau = updateNiveau;
    }

    public boolean isUpdateModule() {
        return updateModule;
    }

    public void setUpdateModule(boolean updateModule) {
        this.updateModule = updateModule;
    }

    public boolean isUpdatePage() {
        return updatePage;
    }

    public void setUpdatePage(boolean updatePage) {
        this.updatePage = updatePage;
    }

    public boolean isUpdateRessource() {
        return updateRessource;
    }

    public void setUpdateRessource(boolean updateRessource) {
        this.updateRessource = updateRessource;
    }

    public List<YvsNiveauAcces> getListNiveauAcces() {
        return listNiveauAcces;
    }

    public void setListNiveauAcces(List<YvsNiveauAcces> listNiveauAcces) {
        this.listNiveauAcces = listNiveauAcces;
    }

    public void setNiveauAcces(NiveauAcces niveauAcces) {
        this.niveauAcces = niveauAcces;
    }

    public NiveauAcces getNiveauAcces() {
        return niveauAcces;
    }

    public YvsModule getEntityModule() {
        return entityModule;
    }

    public void setEntityModule(YvsModule entityModule) {
        this.entityModule = entityModule;
    }

    public YvsPageModule getEntityPage() {
        return entityPage;
    }

    public void setEntityPage(YvsPageModule entityPage) {
        this.entityPage = entityPage;
    }

    public PageModule getPageModule() {
        return pageModule;
    }

    public void setPageModule(PageModule pageModule) {
        this.pageModule = pageModule;
    }

    public RessourcesPage getRessourcePage() {
        return ressourcePage;
    }

    public void setRessourcePage(RessourcesPage ressourcePage) {
        this.ressourcePage = ressourcePage;
    }

    public Modules getModule() {
        return module;
    }

    public void setModule(Modules module) {
        this.module = module;
    }

    public YvsNiveauAcces getSelectedNiveau() {
        return selectedNiveau;
    }

    public void setSelectedNiveau(YvsNiveauAcces selectedNiveau) {
        this.selectedNiveau = selectedNiveau;
    }

    @Override
    public void loadAll() {
        service = new ServiceAutorisation(dao, currentNiveau, currentUser, currentAgence.getSociete());
        if (societe < 1) {
            societe = currentAgence.getSociete().getId();
        }
        if (agenceNiveau < 1) {
            agenceNiveau = currentAgence.getId();
        }
        ManagedSociete w = (ManagedSociete) giveManagedBean(ManagedSociete.class);
        if (w != null) {
            int idx = w.getListSociete().indexOf(new YvsSocietes(societe));
            if (idx > -1) {
                YvsSocietes s = w.getListSociete().get(idx);
                loadAll(s);
            }
        }
        loadOthers();
        groupes = dao.loadNameQueries("YvsResourcePageGroup.findAll", new String[]{}, new Object[]{});
    }

    public List<YvsNiveauAcces> loadBySociete(YvsSocietes scte) {
        champ = new String[]{"societe"};
        val = new Object[]{scte};
        nameQueri = "YvsNiveauAcces.findBySociete";
        if (!currentUser.getUsers().getNiveauAcces().getSuperAdmin()) {
            champ = new String[]{"societe"};
            val = new Object[]{scte};
            nameQueri = "YvsNiveauAcces.findSociete";
        }
        return dao.loadNameQueries(nameQueri, champ, val);
    }

    private void loadOthers() {
        loadAllGrade();
        loadAllModules();
    }

    public void loadAutorisation(YvsNiveauAcces niveau) {
        autorisations = service.loadAutorisation(niveau);
        autorisationsSave = new ArrayList<>(autorisations);
    }

    public void loadAll(YvsSocietes scte) {
        listNiveauAcces = loadBySociete(scte);
        update("table_niveauA");
    }

    public void loadAllGrade() {
        grades = dao.loadNameQueries("YvsUsersGrade.findAll", new String[]{}, new Object[]{});
    }

    public void loadAllModules() {
        listModules = dao.loadNameQueries("YvsModule.findAll", new String[]{}, new Object[]{});
    }

    public void loadAllPages(YvsModule module) {
        listPages = dao.loadNameQueries("YvsPageModule.findByModule", new String[]{"module"}, new Object[]{module});
    }

    public YvsModule buildModule(Modules m) {
        YvsModule r = new YvsModule();
        if (m != null) {
            r.setId(m.getId());
            r.setDescription(m.getDescription());
            r.setLibelle(m.getLibelle());
            r.setReference(m.getReference());
            r.setAuthor(currentUser);
        }
        return r;
    }

    public YvsPageModule buildPageModule(PageModule m) {
        YvsPageModule r = new YvsPageModule();
        if (m != null) {
            r.setDescription(m.getDescription());
            r.setId(m.getId());
            r.setLibelle(m.getLibelle());
            r.setReference(m.getReference());
            r.setAuthor(currentUser);
            r.setModule(entityModule);
        }
        return r;
    }

    public YvsRessourcesPage buildRessourcesPage(RessourcesPage m) {
        YvsRessourcesPage r = new YvsRessourcesPage();
        if (m != null) {
            r.setDescription(m.getDescription());
            r.setId(m.getId());
            r.setLibelle(m.getLibelle());
            r.setReferenceRessource(m.getReference());
            r.setPageModule(entityPage);
        }
        return r;
    }

    @Override
    public boolean controleFiche(NiveauAcces bean) {
        if (bean.getDesignation() == null || bean.getDesignation().equals("")) {
            getErrorMessage("Vous devez entre la designation");
            return false;
        }
        if (bean.getGrade() < 1) {
            getErrorMessage("Vous devez entre le grade");
            return false;
        }
        return true;
    }

    public boolean controleFicheModule(Modules bean) {
        if (bean.getDescription() == null || bean.getDescription().equals("")) {
            getErrorMessage("Vous devez entre la description");
            return false;
        }
        if (bean.getLibelle() == null || bean.getLibelle().equals("")) {
            getErrorMessage("Vous devez entre lae libelle");
            return false;
        }
        if (bean.getReference() == null || bean.getReference().equals("")) {
            getErrorMessage("Vous devez entre la reference");
        }
        return true;
    }

    public boolean controleFichePage(PageModule bean) {
        if (bean.getDescription() == null || bean.getDescription().equals("")) {
            getErrorMessage("Vous devez entre la description");
            return false;
        }
        if (bean.getLibelle() == null || bean.getLibelle().equals("")) {
            getErrorMessage("Vous devez entre lae libelle");
            return false;
        }
        if (bean.getReference() == null || bean.getReference().equals("")) {
            getErrorMessage("Vous devez entre la reference");
        }
        return true;
    }

    public boolean controleFicheRessource(RessourcesPage bean) {
        if (bean.getDescription() == null || bean.getDescription().equals("")) {
            getErrorMessage("Vous devez entre la description");
            return false;
        }
        if (bean.getLibelle() == null || bean.getLibelle().equals("")) {
            getErrorMessage("Vous devez entre lae libelle");
            return false;
        }
        if (bean.getReference() == null || bean.getReference().equals("")) {
            getErrorMessage("Vous devez entre la reference");
        }
        return true;
    }

    @Override
    public void deleteBean() {
    }

    public void deleteBean(YvsNiveauAcces nv) {
        try {
            nv.setAuthor(currentUser);
            dao.delete(nv);
            listNiveauAcces.remove(nv);
            succes();
        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
            getErrorMessage("Impossible de supprimer cette ligne !");
        }
    }

    @Override
    public void updateBean() {
        setVueListeNiveau(false);
        setSelectNiveau(false);
        setOptionUpdate(false);
        update("head_dlg_niveau_acces");
        update("body_dlg_niveau_acces");
        update("footer_dlg_niveau_acces");
    }

    @Override
    public boolean saveNew() {
        try {
            NiveauAcces bean = recopieView();
            if (controleFiche(bean)) {
                YvsNiveauAcces entity = UtilUsers.buildNiveauAcces(bean, currentUser, currentAgence.getSociete());
                int idx = grades.indexOf(new YvsUsersGrade(bean.getGrade()));
                if (idx > -1) {
                    entity.setGrade(grades.get(idx));
                }
                if (!updateNiveau) {
                    entity.setDateSave(new Date());
                    entity.setId(null);
                    entity = (YvsNiveauAcces) dao.save1(entity);
                    niveauAcces.setId(entity.getId());
                    niveau.setId(entity.getId());
                    copyDroit(entity);
                } else {
                    dao.update(entity);
                }
                idx = listNiveauAcces.indexOf(entity);
                if (idx >= 0) {
                    listNiveauAcces.set(idx, entity);
                } else {
                    listNiveauAcces.add(0, entity);
                }
                actionOpenOrResetAfter(this);
                succes();
                setUpdateNiveau(true);
                update("table_niveauA");
                update("body_dlg_niveau_acces");
                update("select_niveau_acces_niv");
            }
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ManagedNiveauAcces.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean saveNewModule() {
        try {
            Modules bean = recopieViewModule();
            if (controleFicheModule(bean)) {
                entityModule = buildModule(bean);
                if (!isUpdateModule()) {
                    entityModule.setDateSave(new Date());
                    entityModule.setDateUpdate(new Date());
                    entityModule.setId(null);
                    entityModule = (YvsModule) dao.save1(entityModule);
                    module.setId(entityModule.getId());
                    bean.setId(entityModule.getId());
                } else {
                    entityModule.setDateUpdate(new Date());
                    dao.update(entityModule);
                }
                succes();
                resetFicheModule();
                update("body_niveau_01:tabl_niveau_module");
            }
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ManagedNiveauAcces.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean saveNewPage() {
        try {
            PageModule bean = recopieViewPage();
            if (controleFichePage(bean)) {
                entityPage = buildPageModule(bean);
                if (!isUpdatePage()) {
                    entityPage.setDateSave(new Date());
                    entityPage.setDateUpdate(new Date());
                    entityPage.setId(null);
                    entityPage = (YvsPageModule) dao.save1(entityPage);
                    pageModule.setId(entityPage.getId());
                    bean.setId(entityPage.getId());
                    module.getPageModuleList().add(bean);
                } else {
                    entityPage.setDateUpdate(new Date());
                    dao.update(entityPage);
                    module.getPageModuleList().set(module.getPageModuleList().indexOf(pageModule), bean);
                }
                succes();
                resetFichePage();
                update("body_niveau_01:tabl_niveau_page");
            }
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ManagedNiveauAcces.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean saveNewRessource() {
        try {
            RessourcesPage bean = recopieViewRessource();
            if (controleFicheRessource(bean)) {
                YvsRessourcesPage entity = buildRessourcesPage(bean);
                entity.setAuthor(currentUser);
                if (!isUpdateRessource()) {
                    entity.setDateSave(new Date());
                    entity.setDateUpdate(new Date());
                    entity.setId(null);
                    entity = (YvsRessourcesPage) dao.save1(entity);
                    ressourcePage.setId(entity.getId());
                    bean.setId(entity.getId());
                    pageModule.getRessourcesPageList().add(bean);
                } else {
                    entity.setDateUpdate(new Date());
                    dao.update(entity);
                    pageModule.getRessourcesPageList().set(pageModule.getRessourcesPageList().indexOf(ressourcePage), bean);
                }
                succes();
                resetFicheRessource();
                update("body_niveau_01:tabl_niveau_ressource");
            }
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ManagedNiveauAcces.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void onSelectDistant(YvsUsers users) {
        if (users != null ? users.getId() > 0 : false) {
            service = new ServiceAutorisation(dao, currentNiveau, currentUser, currentAgence.getSociete());
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Gestion des niveaux d\\'acces", "modDonneBase", "smenNiveauAcces", true);
            }
            societe = users.getAgence().getSociete().getId();
            oneSearch = users.getCodeUsers();
            selectOneByUsers();
        }
    }

    private void copyDroit(YvsNiveauAcces entity) {
        if (parent != null ? parent.getId() > 0 : false) {
            List<YvsAutorisationModule> lm = dao.loadNameQueries("YvsAutorisationModule.findByNiveauAcces", new String[]{"niveauAcces"}, new Object[]{new YvsNiveauAcces(parent.getId())});
            if (lm != null ? !lm.isEmpty() : false) {
                YvsAutorisationModule y;
                for (YvsAutorisationModule a : lm) {
                    y = new YvsAutorisationModule();
                    y.setAcces(a.getAcces());
                    y.setModule(a.getModule());
                    y.setNiveauAcces(entity);
                    y.setDateUpdate(new Date());
                    y.setAuthor(currentUser);
                    y.setId(null);
                    dao.save(y);
                }
            }
            List<YvsAutorisationPageModule> lp = dao.loadNameQueries("YvsAutorisationPageModule.findByNiveauAcces", new String[]{"niveauAcces"}, new Object[]{new YvsNiveauAcces(parent.getId())});
            if (lp != null ? !lp.isEmpty() : false) {
                YvsAutorisationPageModule y;
                for (YvsAutorisationPageModule a : lp) {
                    y = new YvsAutorisationPageModule();
                    y.setAcces(a.getAcces());
                    y.setPageModule(a.getPageModule());
                    y.setNiveauAcces(entity);
                    y.setDateUpdate(new Date());
                    y.setAuthor(currentUser);
                    y.setId(null);
                    dao.save(y);
                }
            }
            List<YvsAutorisationRessourcesPage> lr = dao.loadNameQueries("YvsAutorisationRessourcesPage.findByNiveauAcces", new String[]{"niveauAcces"}, new Object[]{new YvsNiveauAcces(parent.getId())});
            if (lr != null ? !lr.isEmpty() : false) {
                YvsAutorisationRessourcesPage y;
                for (YvsAutorisationRessourcesPage a : lr) {
                    y = new YvsAutorisationRessourcesPage();
                    y.setAcces(a.getAcces());
                    y.setRessourcePage(a.getRessourcePage());
                    y.setNiveauAcces(entity);
                    y.setDateUpdate(new Date());
                    y.setAuthor(currentUser);
                    y.setId(null);
                    dao.save(y);
                }
            }
        }
    }

    @Override
    public NiveauAcces recopieView() {
        return niveauAcces;
    }

    public Modules recopieViewModule() {
        return module;
    }

    public PageModule recopieViewPage() {
        return pageModule;
    }

    public RessourcesPage recopieViewRessource() {
        return ressourcePage;
    }

    @Override
    public void populateView(NiveauAcces bean) {
        cloneObject(niveauAcces, bean);
        niveauAcces.setUsersList(dao.loadNameQueries("YvsNiveauUsers.findUserByNiveau", new String[]{"niveau"}, new Object[]{new YvsNiveauAcces(bean.getId())}));
    }

    @Override
    public void onSelectObject(YvsNiveauAcces y) {
//        bean.setSelectActif(!bean.isSelectActif());
//        listNiveauAcces.get(listNiveauAcces.indexOf(bean)).setSelectActif(bean.isSelectActif());
        loadAutorisation(y);
        if (listSelectNiveauAcces.contains(y)) {
            listSelectNiveauAcces.remove(y);
        } else {
            listSelectNiveauAcces.add(y);
        }
        if (listSelectNiveauAcces.isEmpty()) {
            resetFiche();
        } else {
            populateView(UtilUsers.buildBeanNiveauAcces(listSelectNiveauAcces.get(listSelectNiveauAcces.size() - 1)));
        }
        setSelectNiveau(!listSelectNiveauAcces.isEmpty());
        setUpdateNiveau(isSelectNiveau());
        setOptionUpdate(listSelectNiveauAcces.size() == 1);
        update("head_dlg_niveau_acces");
        update("body_dlg_niveau_acces");
        update("footer_dlg_niveau_acces");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            YvsNiveauAcces bean = (YvsNiveauAcces) ev.getObject();
            onSelectObject(bean);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        if (ev != null) {
            YvsNiveauAcces bean = (YvsNiveauAcces) ev.getObject();
            onSelectObject(bean);
        }
    }

    @Override
    public void resetFiche() {
        resetFiche(niveauAcces);
        niveauAcces.setSociete(new Societe(currentAgence.getSociete().getId(), currentAgence.getSociete().getCodeAbreviation()));
        niveauAcces.setUsersList(new ArrayList<Users>());
        niveauAcces.setSuperAdmin(false);
        setUpdateNiveau(false);
        resetPage();
        update("head_dlg_niveau_acces");
        update("body_dlg_niveau_acces");
        update("footer_dlg_niveau_acces");
    }

    public void resetFicheModule() {
        resetFiche(module);
        module.setPageModuleList(new ArrayList<PageModule>());
        setUpdateModule(false);
    }

    public void resetFichePage() {
        resetFiche(pageModule);
        pageModule.setModule(new Modules());
        pageModule.setRessourcesPageList(new ArrayList<RessourcesPage>());
        setUpdatePage(false);
    }

    public void resetFicheRessource() {
        resetFiche(ressourcePage);
        ressourcePage.setPageModule(new PageModule());
        setUpdateRessource(false);
    }

    public void chooseSocietes(ValueChangeEvent ev) {
        if (ev != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                societe = id;
                ManagedSociete w = (ManagedSociete) giveManagedBean(ManagedSociete.class);
                if (w != null) {
                    int idx = w.getListSociete().indexOf(new YvsSocietes(id));
                    if (idx > -1) {
                        YvsSocietes s = w.getListSociete().get(idx);
                        loadAll(s);
                    }
                }
            }
        }
    }

    public void chooseNiveauAcces(ValueChangeEvent ev) {
        if (ev != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                int idx = listNiveauAcces.indexOf(new YvsNiveauAcces(id));
                if (idx > -1) {
                    YvsNiveauAcces y = listNiveauAcces.get(idx);
                    niveau = new NiveauAcces(id, y.getDesignation());
                    actualiseAcces();
                }
            } else {
                if (id == -1) {
                    openDialog("dlgNiveauAcces");
                }
                niveau.setId(0);
                autorisations.clear();
                update("body_niveau_01");
            }
        }
    }

    public void findByNumero() {
        if (Util.asString(numSearch)) {
            champ = new String[]{"societe", "designation"};
            val = new Object[]{currentAgence.getSociete(), numSearch + "%"};
            nameQueri = "YvsNiveauAcces.findLikeDesignation";
            if (!currentUser.getUsers().getNiveauAcces().getSuperAdmin()) {
                nameQueri = "YvsNiveauAcces.findLikeDesignationNotSuper";
            }
            listNiveauAcces = dao.loadNameQueries(nameQueri, champ, val);
            update("table_niveauA");
        } else {
            loadAll(currentAgence.getSociete());
        }
    }

    public void findByUsers() {
        if (Util.asString(usersSearch)) {
            champ = new String[]{"societe", "codeUsers"};
            val = new Object[]{currentAgence.getSociete(), usersSearch + "%"};
            nameQueri = "YvsNiveauUsers.findNiveauByCodeUsers";
            if (!currentUser.getUsers().getNiveauAcces().getSuperAdmin()) {
                nameQueri = "YvsNiveauUsers.findNiveauByCodeUsersNotSuper";
            }
            listNiveauAcces = dao.loadNameQueries(nameQueri, champ, val);
            if (listNiveauAcces.size() == 1) {
                chooseOneNiveauAcces(listNiveauAcces.get(0));
            }
            update("table_niveauA");
        } else {
            loadAll(currentAgence.getSociete());
        }
    }

    public void selectOneByUsers() {
        if (Util.asString(oneSearch)) {
            champ = new String[]{"societe", "codeUsers"};
            val = new Object[]{currentAgence.getSociete(), oneSearch + "%"};
            nameQueri = "YvsNiveauUsers.findNiveauByCodeUsers";
            if (!currentUser.getUsers().getNiveauAcces().getSuperAdmin()) {
                nameQueri = "YvsNiveauUsers.findNiveauByCodeUsersNotSuper";
            }
            List<YvsNiveauAcces> list = dao.loadNameQueries(nameQueri, champ, val);
            if (list.size() == 1) {
                chooseOneNiveauAcces(list.get(0));
            }
        }
    }

    public void actualiseAcces() {
        autorisations.clear();
        if (niveau != null ? niveau.getId() > 0 : false) {
            YvsNiveauAcces y = new YvsNiveauAcces(niveau.getId(), niveau.getDesignation());
            loadAutorisation(y);
        }
        update("body_niveau_01");
    }

    public void changeVueNiveau() {
        setVueListeNiveau(!vueListeNiveau);
        resetFiche();
    }

    public void chooseOneNiveauAcces(YvsNiveauAcces y) {
        niveau = UtilUsers.buildBeanNiveauAcces(y);
        loadAutorisation(y);
        update("body_niveau_00");
    }

    public void newBeanModule() {
        resetFicheModule();
        openDialog("dlgModule");
        update("dlg_niveau_module");
    }

    public void newBeanPage() {
        resetFichePage();
        openDialog("dlgPage");
        update("dlg_niveau_page");
    }

    public void newBeanRessource() {
        resetFicheRessource();
        openDialog("dlgRessource");
        update("dlg_niveau_ressource");
    }

    public void closeDlgModule() {
        resetFicheModule();
    }

    public void closeDlgPage() {
        resetFichePage();
    }

    public void closeDlgRessource() {
        resetFicheRessource();
    }

    public void fusionner(boolean fusionne) {
        try {
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                long newValue = listNiveauAcces.get(ids.get(0)).getId();
                if (!fusionne) {
                    if (!autoriser("base_user_fusion")) {
                        openNotAcces();
                        return;
                    }
                    String oldValue = "0";
                    for (int i : ids) {
                        if (listNiveauAcces.get(i).getId() != newValue) {
                            oldValue += "," + listNiveauAcces.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_niveau_acces", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                listNiveauAcces.remove(new YvsNiveauAcces(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = listNiveauAcces.get(idx).getDesignation();
                    } else {
                        YvsNiveauAcces c = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauAcces.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? c.getId() > 0 : false) {
                            fusionneTo = c.getDesignation();
                        }
                    }
                    YvsNiveauAcces c;
                    for (int i : ids) {
                        long oldValue = listNiveauAcces.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(listNiveauAcces.get(i).getDesignation());
                            }
                        } else {
                            c = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauAcces.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? c.getId() > 0 : false) {
                                fusionnesBy.add(c.getDesignation());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 zones");
            }
        } catch (NumberFormatException ex) {
        }
    }

    public void buildForCopyAcces(Autorisations bean) {
        this.acces = bean;
        listSelectNiveauAcces.clear();
        for (YvsNiveauAcces n : listNiveauAcces) {
            switch (bean.getCode()) {
                case "M": {
                    YvsAutorisationModule a = (YvsAutorisationModule) dao.loadOneByNameQueries("YvsAutorisationModule.findByModuleNiveau", new String[]{"module", "niveau"}, new Object[]{new YvsModule(bean.getId()), n});
                    if (a != null ? (a.getId() > 0 ? a.getAcces() : false) : false) {
                        listSelectNiveauAcces.add(n);
                    }
                    break;
                }
                case "P": {
                    YvsAutorisationPageModule a = (YvsAutorisationPageModule) dao.loadOneByNameQueries("YvsAutorisationPageModule.findByPageNiveau", new String[]{"page", "niveau"}, new Object[]{new YvsPageModule(bean.getId()), n});
                    if (a != null ? (a.getId() > 0 ? a.getAcces() : false) : false) {
                        listSelectNiveauAcces.add(n);
                    }
                    break;
                }
                case "R": {
                    YvsAutorisationRessourcesPage a = (YvsAutorisationRessourcesPage) dao.loadOneByNameQueries("YvsAutorisationRessourcesPage.findByRessourceNiveau", new String[]{"ressource", "niveau"}, new Object[]{new YvsRessourcesPage(bean.getId()), n});
                    if (a != null ? (a.getId() > 0 ? a.getAcces() : false) : false) {
                        listSelectNiveauAcces.add(n);
                    }
                    break;
                }
            }
        }
        update("table-select_niveau_acces");
    }

    public void copyAcces() {
        try {
            if (listSelectNiveauAcces != null ? !listSelectNiveauAcces.isEmpty() : false) {
                for (YvsNiveauAcces n : listNiveauAcces) {
                    boolean acces = listSelectNiveauAcces.contains(n);
                    switch (this.acces.getCode()) {
                        case "M": {
                            Modules y = new Modules(this.acces.getId());
                            accesModule(y, n, acces);
                            break;
                        }
                        case "P": {
                            PageModule y = new PageModule(this.acces.getId());
                            accesPage(y, n, acces);
                            break;
                        }
                        case "R": {
                            RessourcesPage y = new RessourcesPage(this.acces.getId());
                            accesRessource(y, n, acces);
                            break;
                        }
                    }
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible!");
            getException("copyAcces", ex);
        }
    }

    public void activeAllPage(Modules module, boolean active) {
        if (niveau != null ? niveau.getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner un niveau d'accès");
            return;
        }
        YvsNiveauAcces n = new YvsNiveauAcces(niveau.getId());
        if (module != null ? module.getId() > 0 : false) {
            accesModule(module, n, active);

            for (PageModule p : module.getPageModuleList()) {
                accesPage(p, n, active);
            }
        }
    }

    public void activeAllRessource(PageModule page, boolean active) {
        if (niveau != null ? niveau.getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner un niveau d'accès");
            return;
        }
        if (page != null ? page.getId() > 0 : false) {
            YvsNiveauAcces n = new YvsNiveauAcces(niveau.getId());
            accesPage(page, n, active);

            for (RessourcesPage p : page.getRessourcesPageList()) {
                accesRessource(p, n, active);
            }
        }
    }

    private void accesModule(Modules m, YvsNiveauAcces n, boolean active) {
        YvsAutorisationModule auto = (YvsAutorisationModule) dao.loadOneByNameQueries("YvsAutorisationModule.findByModuleNiveau", new String[]{"module", "niveau"}, new Object[]{new YvsModule(m.getId()), n});
        if (auto != null ? auto.getId() > 0 : false) {
            auto.setAcces(active);
            auto.setDateUpdate(new Date());
            auto.setAuthor(currentUser);
            dao.update(auto);
        } else {
            auto = new YvsAutorisationModule(n, new YvsModule(m.getId()));
            auto.setAcces(active);
            auto.setDateUpdate(new Date());
            auto.setAuthor(currentUser);
            auto.setId(null);
            dao.save(auto);
        }
        m.setAcces(active);
    }

    private void accesPage(PageModule p, YvsNiveauAcces n, boolean active) {
        YvsAutorisationPageModule auto = (YvsAutorisationPageModule) dao.loadOneByNameQueries("YvsAutorisationPageModule.findByPageNiveau", new String[]{"page", "niveau"}, new Object[]{new YvsPageModule(p.getId()), n});
        if (auto != null ? auto.getId() > 0 : false) {
            auto.setAcces(active);
            auto.setDateUpdate(new Date());
            auto.setAuthor(currentUser);
            dao.update(auto);
        } else {
            auto = new YvsAutorisationPageModule(n, new YvsPageModule(p.getId()));
            auto.setAcces(active);
            auto.setDateUpdate(new Date());
            auto.setAuthor(currentUser);
            auto.setId(null);
            dao.save(auto);
        }
        p.setAcces(active);
    }

    private void accesRessource(RessourcesPage r, YvsNiveauAcces n, boolean active) {
        YvsAutorisationRessourcesPage auto = (YvsAutorisationRessourcesPage) dao.loadOneByNameQueries("YvsAutorisationRessourcesPage.findByRessourceNiveau", new String[]{"ressource", "niveau"}, new Object[]{new YvsRessourcesPage(r.getId()), n});
        if (auto != null ? auto.getId() > 0 : false) {
            auto.setAcces(active);
            auto.setDateUpdate(new Date());
            auto.setAuthor(currentUser);
            dao.update(auto);
        } else {
            auto = new YvsAutorisationRessourcesPage(n, new YvsRessourcesPage(r.getId()));
            auto.setAcces(active);
            auto.setDateUpdate(new Date());
            auto.setAuthor(currentUser);
            auto.setId(null);
            dao.save(auto);
        }
        r.setAcces(active);
    }

    public void checkAllRessourceModule(Autorisations bean, boolean acces) {
        if (bean != null ? bean.getId() > 0 : false) {
            List<Autorisations> list = new ArrayList<>();
            list.add(bean);
            for (Autorisations a : autorisationsSave) {
                if (Objects.equals(a.getModule(), bean)) {
                    list.add(a);
                }
            }
            for (Autorisations a : list) {
                checkAllAutorisation(a, acces, false);
            }
            succes();
        }
    }

    public void checkAllAutorisation(Autorisations bean, boolean acces) {
        checkAllAutorisation(bean, acces, true);
    }

    public void checkAllAutorisation(Autorisations bean, boolean acces, boolean msg) {
        if (bean != null ? bean.getId() > 0 : false) {
            List<Autorisations> list = new ArrayList<>();
            list.add(bean);
            switch (bean.getCode()) {
                case "M": {
                    for (Autorisations a : autorisationsSave) {
                        if (Objects.equals(a.getModule(), bean)) {
                            list.add(a);
                        }
                    }
                    break;
                }
                case "P": {
                    for (Autorisations a : autorisationsSave) {
                        if (Objects.equals(a.getPage(), bean)) {
                            list.add(a);
                        }
                    }
                    break;
                }
                default: {
                    break;
                }
            }
            for (Autorisations a : list) {
                a.setAcces(!acces);
                checkAutorisation(a, false);
            }
            if (msg) {
                succes();
            }
        }
    }

    public void checkAutorisation(Autorisations bean, boolean msg) {
        if (bean != null ? bean.getId() > 0 : false) {
            YvsNiveauAcces n = new YvsNiveauAcces(niveau.getId());
            bean.setAcces(!bean.isAcces());
            switch (bean.getCode()) {
                case "M": {
                    Modules y = new Modules(bean.getId());
                    accesModule(y, n, bean.isAcces());
                    break;
                }
                case "P": {
                    PageModule y = new PageModule(bean.getId());
                    accesPage(y, n, bean.isAcces());
                    break;
                }
                default: {
                    RessourcesPage y = new RessourcesPage(bean.getId());
                    accesRessource(y, n, bean.isAcces());
                    break;
                }
            }
            bean.setDateUpdate(new Date());
            bean.setAuthor(currentUser);
            int idx = autorisations.indexOf(bean);
            if (idx > -1) {
                autorisations.set(idx, bean);
            }
            idx = autorisationsSave.indexOf(bean);
            if (idx > -1) {
                autorisationsSave.set(idx, bean);
            }
            if (msg) {
                succes();
            }
        }
    }

    public void findByClick(Autorisations bean) {
        if (bean != null ? bean.getId() > 0 : false) {
            codeSearch = bean.getReference();
            findAcces();
            update("zone_find-niveau_acces");
        }
    }

    public void filtreByModule() {
        autorisations.clear();
        listPages.clear();
        pageSearch = 0;
        if (moduleSearch > 0) {
            loadAllPages(new YvsModule(moduleSearch));
        }
        filter();
        update("body_niveau_01");
    }

    public void filtreByPage() {
        autorisations.clear();
        filter();
        update("body_niveau_01");
    }

    public void filterByType() {
        autorisations.clear();
        filter();
        update("body_niveau_01");
    }

    public void filterByAcces() {
        autorisations.clear();
        filter();
        update("body_niveau_01");
    }

    public void findAcces() {
        autorisations.clear();
        filter();
        update("body_niveau_01");
    }

    private void filter() {
        List<Long> modules = new ArrayList<>();
        List<Long> pages = new ArrayList<>();
        if (onlyModuleNiveau) {
            modules = (List<Long>) dao.loadListByNameQueries("YvsAutorisationModule.findIdModuleByAcces", new String[]{"niveau", "acces"}, new Object[]{new YvsNiveauAcces(niveau.getId()), true});
        }
        if (onlyPageNiveau) {
            pages = (List<Long>) dao.loadListByNameQueries("YvsAutorisationPageModule.findIdPageByAcces", new String[]{"niveau", "acces"}, new Object[]{new YvsNiveauAcces(niveau.getId()), true});
        }
        for (Autorisations a : autorisationsSave) {
            if (moduleSearch > 0) {
                Autorisations bean = new Autorisations(moduleSearch, "YvsModule");
                if (!Objects.equals(a.getModule(), bean)) {
                    continue;
                }
            }
            if (pageSearch > 0) {
                Autorisations bean = new Autorisations(pageSearch, "YvsPageModule");
                if (!Objects.equals(a.getPage(), bean)) {
                    continue;
                }
            }
            if (Util.asString(typeSearch)) {
                if (!a.getCode().equals(typeSearch)) {
                    continue;
                }
            }
            if (accesSearch != null) {
                if (!Objects.equals(a.isAcces(), accesSearch)) {
                    continue;
                }
            }
            if (onlyModuleNiveau) {
                if (a.getType().equals("YvsModule")) {
                    if (!modules.contains(a.getId())) {
                        continue;
                    }
                } else if (a.getType().equals("YvsPageModule")) {
                    if (a.getModule() == null ? true : !modules.contains(a.getModule().getId())) {
                        continue;
                    }
                } else if (a.getType().equals("YvsRessourcesPage")) {
                    if (a.getPage() == null ? true : (a.getPage().getModule() == null ? true : !modules.contains(a.getPage().getModule().getId()))) {
                        continue;
                    }
                }
            }
            if (onlyPageNiveau) {
                if (a.getType().equals("YvsModule")) {
                    if (!onlyModuleNiveau) {
                        continue;
                    }
                } else if (a.getType().equals("YvsPageModule")) {
                    if (!pages.contains(a.getId())) {
                        continue;
                    }
                } else if (a.getType().equals("YvsRessourcesPage")) {
                    if (a.getPage() == null ? true : !pages.contains(a.getPage().getId())) {
                        continue;
                    }
                }
            }
            if (!(Util.asString(codeSearch) ? (a.getReference().toUpperCase().contains(codeSearch.toUpperCase()) || a.getDesignation().toUpperCase().contains(codeSearch.toUpperCase())
                    || (a.getPage() != null ? a.getPage().getReference().toUpperCase().contains(codeSearch.toUpperCase()) || a.getPage().getDesignation().toUpperCase().contains(codeSearch.toUpperCase()) : false)
                    || (a.getModule() != null ? a.getModule().getReference().toUpperCase().contains(codeSearch.toUpperCase()) || a.getModule().getDesignation().toUpperCase().contains(codeSearch.toUpperCase()) : false)) : true)) {
                continue;
            }
            autorisations.add(a);
        }
    }

    public void maintenance() {
        try {
            String query = "SELECT niveau_acces, module, COUNT(id) FROM yvs_autorisation_module GROUP BY niveau_acces, module HAVING (COUNT(id)) > 1 ORDER BY niveau_acces, module";
            List<Object[]> result = dao.loadListBySqlQuery(query, new Options[]{});
            if (result != null ? !result.isEmpty() : false) {
                int count = 0;
                for (Object[] val : result) {
                    query = "SELECT id FROM yvs_autorisation_module WHERE niveau_acces = ? AND module = ? LIMIT 1";
                    Long id = (Long) dao.loadObjectBySqlQuery(query, new Options[]{new Options((int) val[0], 1), new Options((int) val[1], 2)});
                    if (id != null ? id > 0 : false) {
                        System.out.println("(" + (++count) + ") ID MODULE : " + id);
                        query = "DELETE FROM yvs_autorisation_module WHERE id = ?";
                        dao.requeteLibre(query, new Options[]{new Options(id, 1)});
                    }
                }
            }
            query = "SELECT niveau_acces, page_module, COUNT(id) FROM yvs_autorisation_page_module GROUP BY niveau_acces, page_module HAVING (COUNT(id)) > 1 ORDER BY niveau_acces, page_module";
            result = dao.loadListBySqlQuery(query, new Options[]{});
            if (result != null ? !result.isEmpty() : false) {
                int count = 0;
                for (Object[] val : result) {
                    query = "SELECT id FROM yvs_autorisation_page_module WHERE niveau_acces = ? AND page_module = ? LIMIT 1";
                    Long id = (Long) dao.loadObjectBySqlQuery(query, new Options[]{new Options((int) val[0], 1), new Options((int) val[1], 2)});
                    if (id != null ? id > 0 : false) {
                        System.out.println("(" + (++count) + ") ID PAGE : " + id);
                        query = "DELETE FROM yvs_autorisation_page_module WHERE id = ?";
                        dao.requeteLibre(query, new Options[]{new Options(id, 1)});
                    }
                }
            }
            query = "SELECT niveau_acces, ressource_page, COUNT(id) FROM yvs_autorisation_ressources_page GROUP BY niveau_acces, ressource_page HAVING (COUNT(id)) > 1 ORDER BY niveau_acces, ressource_page";
            result = dao.loadListBySqlQuery(query, new Options[]{});
            if (result != null ? !result.isEmpty() : false) {
                int count = 0;
                for (Object[] val : result) {
                    query = "SELECT id FROM yvs_autorisation_ressources_page WHERE niveau_acces = ? AND ressource_page = ? LIMIT 1";
                    Long id = (Long) dao.loadObjectBySqlQuery(query, new Options[]{new Options((int) val[0], 1), new Options((int) val[1], 2)});
                    if (id != null ? id > 0 : false) {
                        System.out.println("(" + (++count) + ") ID RESSOURCE : " + id);
                        query = "DELETE FROM yvs_autorisation_ressources_page WHERE id = ?";
                        dao.requeteLibre(query, new Options[]{new Options(id, 1)});
                    }
                }
            }
            succes();
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedNiveauAcces (maintenance)", ex);
        }
    }

    public void loadPpteRessource(Autorisations auth) {
        if (auth != null) {
            if (auth.getCode().equals("R")) {
                ressourcePage.setId(auth.getId());
                ressourcePage.setReference(auth.getReference());
                ressourcePage.setDescription(auth.getDescription());
                ressourcePage.setLibelle(auth.getDesignation());
                ressourcePage.setPageModule(new PageModule(auth.getPage().getId()));
                if (auth.getGroupe() != null) {
                    ressourcePage.setGroupe(auth.getGroupe());
                }
            }
        }
    }

    public void applyPpteResource() {
        if (ressourcePage.getId() != null ? ressourcePage.getId() > 0 : false) {
            YvsRessourcesPage res = new YvsRessourcesPage(ressourcePage.getId());
            res.setDateSave(ressourcePage.getDateSave());
            res.setDateUpdate(new Date());
            res.setDescription(ressourcePage.getDescription());
            if (ressourcePage.getGroupe() != null ? ressourcePage.getGroupe().getId() != null ? ressourcePage.getGroupe().getId() > 0 : false : false) {
                res.setGroupe(new YvsResourcePageGroup(ressourcePage.getGroupe().getId()));
            }
            res.setLibelle(ressourcePage.getLibelle());
            res.setPageModule(new YvsPageModule(ressourcePage.getPageModule().getId()));
            res.setReferenceRessource(ressourcePage.getReference());
            res.setAuthor(currentUser);
            dao.update(res);
            succes();
        }
    }

    public void copyAccesFromNiveau() {
        try {
            if (selectedNiveau != null ? selectedNiveau.getId() < 1 : true) {
                getErrorMessage("Vous devez choisir un niveau pour la cible");
                return;
            }
            if (niveauCopy < 1) {
                getErrorMessage("Vous devez choisir un niveau pour la source");
                return;
            }
            if (selectedNiveau.getId().equals(niveauCopy)) {
                getErrorMessage("Vous devez choisir un niveau source different du niveau cible");
                return;
            }
            String query = "DELETE FROM yvs_autorisation_module WHERE niveau_acces = ?";
            dao.requeteLibre(query, new Options[]{new Options(selectedNiveau.getId(), 1)});
            query = "DELETE FROM yvs_autorisation_page_module WHERE niveau_acces = ?";
            dao.requeteLibre(query, new Options[]{new Options(selectedNiveau.getId(), 1)});
            query = "DELETE FROM yvs_autorisation_ressources_page WHERE niveau_acces = ?";
            dao.requeteLibre(query, new Options[]{new Options(selectedNiveau.getId(), 1)});

            query = "INSERT INTO yvs_autorisation_module (niveau_acces, module, acces, author) SELECT ?, y.module, y.acces, ? FROM yvs_autorisation_module y WHERE y.niveau_acces = ?";
            dao.requeteLibre(query, new Options[]{new Options(selectedNiveau.getId(), 1), new Options(currentUser.getId(), 2), new Options(niveauCopy, 3)});
            query = "INSERT INTO yvs_autorisation_page_module (niveau_acces, page_module, acces, author) SELECT ?, y.page_module, y.acces, ? FROM yvs_autorisation_page_module y WHERE y.niveau_acces = ?";
            dao.requeteLibre(query, new Options[]{new Options(selectedNiveau.getId(), 1), new Options(currentUser.getId(), 2), new Options(niveauCopy, 3)});
            query = "INSERT INTO yvs_autorisation_ressources_page (niveau_acces, ressource_page, acces, author) SELECT ?, y.ressource_page, y.acces, ? FROM yvs_autorisation_ressources_page y WHERE y.niveau_acces = ?";
            dao.requeteLibre(query, new Options[]{new Options(selectedNiveau.getId(), 1), new Options(currentUser.getId(), 2), new Options(niveauCopy, 3)});

            succes();
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("ManagedNiveauAcces (copyAccesFromNiveau)", ex);
        }
    }

    public void loadUsers() {
        try {
            if (agenceNiveau < 1) {
                agenceNiveau = currentAgence.getId();
            }
            String query = "SELECT DISTINCT u.id, u.code_users, u.nom_users FROM yvs_users u LEFT JOIN yvs_niveau_users n ON (n.id_user = u.id AND n.id_niveau = ?) WHERE u.actif IS TRUE AND u.agence = ? AND n.id IS NULL";
            List<Object[]> result = dao.loadListBySqlQuery(query, new Options[]{new Options(niveauAcces.getId(), 1), new Options(agenceNiveau, 2)});
            usersList.clear();
            YvsUsers u;
            for (Object[] lect : result) {
                u = new YvsUsers((Long) lect[0], (String) lect[1], (String) lect[2]);
                usersList.add(u);
            }
            update("data_add_users_niveau");
        } catch (Exception ex) {
            getException("ManagedNiveauAcces (loadUsers)", ex);
        }
    }

    public void addUsers(YvsUsers u) {
        try {
            if (u != null ? u.getId() > 0 : false) {
                List<YvsNiveauUsers> list = dao.loadNameQueries("YvsNiveauUsers.findNivoUserInScte", new String[]{"user", "societe"}, new Object[]{u, currentAgence.getSociete()});
                if (list != null ? list.size() > 0 : false) {
                    for (YvsNiveauUsers y : list) {
                        y.setIdNiveau(new YvsNiveauAcces(niveauAcces.getId()));
                        y.setDateUpdate(new Date());
                        dao.update(y);
                    }
                } else {
                    YvsNiveauUsers y = new YvsNiveauUsers(null);
                    y.setIdNiveau(new YvsNiveauAcces(niveauAcces.getId()));
                    y.setIdUser(u);
                    y.setDateUpdate(new Date());
                    y.setDateSave(new Date());
                    y = (YvsNiveauUsers) dao.save1(y);
                }
                usersList.remove(u);
                niveauAcces.getUsersList().add(UtilUsers.buildSimpleBeanUsers(u));
                update("data_users_niveau");
                update("data_add_users_niveau");
                succes();
            }
        } catch (Exception ex) {
            getException("ManagedNiveauAcces (addUsers)", ex);
        }
    }

}
