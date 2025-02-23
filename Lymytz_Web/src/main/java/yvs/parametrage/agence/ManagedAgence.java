/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.agence;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.chart.PieChartModel;
import yvs.dao.Options;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsGrhSecteurs;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.bean.EmployePartial;
import yvs.parametrage.societe.Societe;
import yvs.parametrage.societe.UtilSte;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.dico.ManagedDico;
import yvs.parametrage.societe.ManagedSociete;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class ManagedAgence extends Managed<Agence, YvsAgences> implements Serializable {

    @ManagedProperty(value = "#{agence}")
    private Agence agence;
    private List<YvsAgences> listAgence, agences;

    private PieChartModel pieModel;
    private Societe societe = new Societe();

    private SecteurActivite secteur = new SecteurActivite();
    private List<YvsGrhSecteurs> listSecteurActivite;
    private List<YvsUsersAgence> users;
    private List<YvsUsersAgence> utilisateurs;

    private boolean vueListe = true, vueListeEmpls = true, updateAgence, selectAgence, optionUpdate, viewSociete;

    private String tabIds;
    private String fusionneTo;
    private List<String> fusionnesBy;

    YvsAgences entityAgence;
    YvsSocietes entitySociete;

    public ManagedAgence() {
        listAgence = new ArrayList<>();
        agences = new ArrayList<>();
        listSecteurActivite = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
        utilisateurs = new ArrayList<>();
        users = new ArrayList<>();
    }

    public List<YvsAgences> getAgences() {
        return agences;
    }

    public void setAgences(List<YvsAgences> agences) {
        this.agences = agences;
    }

    public List<YvsUsersAgence> getUsers() {
        return users;
    }

    public void setUsers(List<YvsUsersAgence> users) {
        this.users = users;
    }

    public List<YvsUsersAgence> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<YvsUsersAgence> utilisateurs) {
        this.utilisateurs = utilisateurs;
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

    public YvsSocietes getEntitySociete() {
        return entitySociete;
    }

    public void setEntitySociete(YvsSocietes entitySociete) {
        this.entitySociete = entitySociete;
    }

    public boolean isVueListeEmpls() {
        return vueListeEmpls;
    }

    public void setVueListeEmpls(boolean vueListeEmpls) {
        this.vueListeEmpls = vueListeEmpls;
    }

    public void setViewSociete(boolean viewSociete) {
        this.viewSociete = viewSociete;
    }

    public boolean isViewSociete() {
        return viewSociete;
    }

    public SecteurActivite getSecteur() {
        return secteur;
    }

    public void setSecteur(SecteurActivite secteur) {
        this.secteur = secteur;
    }

    public List<YvsGrhSecteurs> getListSecteurActivite() {
        return listSecteurActivite;
    }

    public void setListSecteurActivite(List<YvsGrhSecteurs> listSecteurActivite) {
        this.listSecteurActivite = listSecteurActivite;
    }

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public YvsAgences getEntityAgence() {
        return entityAgence;
    }

    public void setEntityAgence(YvsAgences entityAgence) {
        this.entityAgence = entityAgence;
    }

    public boolean isOptionUpdate() {
        return optionUpdate;
    }

    public void setOptionUpdate(boolean optionUpdate) {
        this.optionUpdate = optionUpdate;
    }

    public boolean isUpdateAgence() {
        return updateAgence;
    }

    public void setUpdateAgence(boolean updateAgence) {
        this.updateAgence = updateAgence;
    }

    public boolean isSelectAgence() {
        return selectAgence;
    }

    public void setSelectAgence(boolean selectAgence) {
        this.selectAgence = selectAgence;
    }

    public boolean isVueListe() {
        return vueListe;
    }

    public void setVueListe(boolean vueListe) {
        this.vueListe = vueListe;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public List<YvsAgences> getListAgence() {
        return listAgence;
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    public void setPieModel(PieChartModel pieModel) {
        this.pieModel = pieModel;
    }

    public void setListAgence(List<YvsAgences> listAgence) {
        this.listAgence = listAgence;
    }

    private YvsAgences buildAgence(Agence bean) {
        YvsAgences r = new YvsAgences();
        if (bean != null) {
            r.setId(bean.getId());
            r.setAbbreviation(bean.getAbbreviation());
            r.setSupp(bean.isSup());
            r.setActif(bean.isActif());
            r.setAdresse(bean.getAdresse());
            r.setAuthor(currentUser);
            r.setDesignation(bean.getDesignation());
            r.setLastAuthor(bean.getLastAuteur());
            r.setRegion(bean.getRegion());
            r.setEmail(bean.getEmail());
            r.setCodeagence(bean.getCodeAgence());
            r.setTelephone(bean.getTelephone());
            r.setDateUpdate(new Date());
            r.setDateSave(bean.getDateSave());
            if (isViewSociete()) {
                if ((bean.getSociete() != null) ? bean.getSociete().getId() != 0 : false) {
                    r.setSociete(new YvsSocietes(bean.getSociete().getId()));
                }
            } else {
                r.setSociete(currentAgence.getSociete());
            }
            if ((bean.getSecteur() != null) ? bean.getSecteur().getId() > 0 : false) {
                r.setSecteurActivite(new YvsGrhSecteurs(bean.getSecteur().getId(), bean.getSecteur().getNom()));
            }
            if ((bean.getResponsableAgence() != null) ? bean.getResponsableAgence().getId() > 0 : false) {
                r.setChefAgence(new YvsGrhEmployes(bean.getResponsableAgence().getId(), bean.getResponsableAgence().getNom(), bean.getResponsableAgence().getPrenom()));
            }
            if ((bean.getVille() != null) ? bean.getVille().getId() != 0 : false) {
                r.setVille(new YvsDictionnaire(bean.getVille().getId(), bean.getVille().getLibelle()));
            }
            try {
                if (bean.getLastDateUpdate() != null) {
                    r.setLastDateSave(dft.parse(bean.getLastDateUpdate()));
                } else {
                    r.setLastDateSave(new Date());
                }
            } catch (ParseException ex) {
                Logger.getLogger(ManagedAgence.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return r;
    }

    @Override
    public void populateView(Agence bean) {
        cloneObject(agence, bean);
        loadAllSelecteurActivite();
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null ? bean.getPays() != null : false) {
            m.loadVilles(new YvsDictionnaire(bean.getPays().getId()));
        }
        loadUtilisateurs();
    }

    public void selectAgence(Agence bean) {
        bean.setSelectActif(!bean.isSelectActif());
        populateView(bean);
        setUpdateAgence(true);
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            YvsAgences bean = (YvsAgences) ev.getObject();
            selectAgence(UtilAgence.buildBeanAgence(bean));
            tabIds = listAgence.indexOf(bean) + "";
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewSecteur(SelectEvent ev) {
        if (ev != null) {
            YvsGrhSecteurs bean = (YvsGrhSecteurs) ev.getObject();
            cloneObject(secteur, UtilAgence.buildBeanSecteurActivite(bean));
        }
    }

    public void unLoadOnViewSecteur(UnselectEvent ev) {
        resetFicheSecteur();
    }

    public void loadOnViewEmploye(SelectEvent ev) {
        if (ev != null) {
            YvsGrhEmployes empl = (YvsGrhEmployes) ev.getObject();
            loadOneEmploye(empl);
        }
    }

    public void loadUsers() {
        String query = "SELECT u.id, u.code_users, u.nom_users, ua.id, ua.actif, ua.can_action "
                + "FROM yvs_users u INNER JOIN yvs_agences a ON u.agence = a.id LEFT JOIN yvs_users_agence ua ON (ua.agence = ? AND ua.users = u.id) "
                + "WHERE a.societe = ? ORDER BY u.nom_users";
        YvsUsersAgence ua;
        YvsUsers u;
        List<Object[]> result = dao.loadListBySqlQuery(query, new Options[]{new Options(agence.getId(), 1), new Options(currentAgence.getSociete().getId(), 2)});
        users.clear();
        for (Object[] line : result) {
            ua = new YvsUsersAgence();
            u = new YvsUsers();
            u.setId((line[0]) != null ? ((Long) line[0]) : 0);
            u.setCodeUsers((line[1]) != null ? ((String) line[1]) : "");
            u.setNomUsers((line[2]) != null ? ((String) line[2]) : "");
            long current = (line[3]) != null ? ((Long) line[3]) : 0;
            ua.setId(current != 0 ? current : YvsUsersAgence.ids--);
            ua.setAgence(new YvsAgences(agence.getId()));
            ua.setActif(line[4] != null ? ((Boolean) line[4]) : false);
            ua.setCanAction(line[5] != null ? ((Boolean) line[5]) : false);
            ua.setUsers(u);
            ua.setAuthor(currentUser);
            users.add(ua);
        }
        update("data_agence_users_all");
    }

    public void loadUtilisateurs() {
        String query = "SELECT u.id, u.code_users, u.nom_users, ua.id, ua.actif, ua.can_action "
                + "FROM yvs_users u INNER JOIN yvs_agences a ON u.agence = a.id INNER JOIN yvs_users_agence ua ON (ua.agence = ? AND ua.users = u.id) "
                + "WHERE a.societe = ? AND ua.actif ORDER BY u.nom_users";
        YvsUsersAgence ua;
        YvsUsers u;
        List<Object[]> result = dao.loadListBySqlQuery(query, new Options[]{new Options(agence.getId(), 1), new Options(currentAgence.getSociete().getId(), 2)});
        utilisateurs.clear();
        for (Object[] line : result) {
            ua = new YvsUsersAgence();
            u = new YvsUsers();
            u.setId((line[0]) != null ? ((Long) line[0]) : 0);
            u.setCodeUsers((line[1]) != null ? ((String) line[1]) : "");
            u.setNomUsers((line[2]) != null ? ((String) line[2]) : "");
            long current = (line[3]) != null ? ((Long) line[3]) : 0;
            ua.setId(current != 0 ? current : YvsUsersAgence.ids--);
            ua.setAgence(new YvsAgences(agence.getId()));
            ua.setActif(line[4] != null ? ((Boolean) line[4]) : false);
            ua.setCanAction(line[5] != null ? ((Boolean) line[5]) : false);
            ua.setUsers(u);
            ua.setAuthor(currentUser);
            utilisateurs.add(ua);
        }
        update("data_agence_users");
    }

    public void loadOneEmploye(YvsGrhEmployes y) {
        agence.setResponsableAgence(UtilGrh.buildBeanSimpleEmployePartial(y));
        update("txt_matricule_emp_agence");
    }

    public void loadOneSecteur(YvsGrhSecteurs y) {
        agence.setSecteur(UtilAgence.buildBeanSecteurActivite(y));
        update("txt_secteur_agence");
    }

    @Override
    public void loadAll() {
        loadAgence();

        setViewSociete((currentUser.getUsers().getAccesMultiAgence() != null) ? currentUser.getUsers().getAccesMultiAgence() : false);
        entitySociete = currentAgence.getSociete();
        agence.setSociete(UtilSte.buildBeanSociete(currentAgence.getSociete()));
        loadAllSelecteurActivite();
    }

    public void loadAllAgence(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        listAgence = paginator.executeDynamicQuery("YvsAgences", "y.designation", avance, init, (int) imax, dao);
    }

    public void addParamActifAgence(Boolean actif) {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actif, "=", "AND"));
        loadAllAgence(true, true);
    }

    public void loadAgence() {
        loadAllAgence(currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0);
    }

    public void loadAllAgence(long societe) {
        agences.clear();
        listAgence.clear();
        champ = new String[]{"societe", "users"};
        val = new Object[]{new YvsSocietes(societe), currentUser.getUsers()};
        List<YvsUsersAgence> list = dao.loadNameQueries("YvsUsersAgence.findUsageByUsersSociete", champ, val);
        for (YvsUsersAgence a : list) {
            if (a.getCanAction()) {
                agences.add(a.getAgence());
            }
            listAgence.add(a.getAgence());
        }
        if (!listAgence.contains(currentAgence)) {
            listAgence.add(currentAgence);
        }
        if (!agences.contains(currentAgence)) {
            agences.add(currentAgence);
        }
    }

    public void loadAllUsable() {
        listIdAgences.clear();
        if (currentUser.getUsers().getAccesMultiSociete() && currentAgence.getSociete().getGroupe() != null) {
            champ = new String[]{"groupe"};
            val = new Object[]{currentAgence.getSociete().getGroupe()};
            nameQueri = "YvsAgences.findUsableByGroupe";
            listAgence = dao.loadNameQueries(nameQueri, champ, val);
            agences = new ArrayList<>(listAgence);
        } else {
            agences.clear();
            listAgence.clear();
            champ = new String[]{"societe", "users"};
            val = new Object[]{currentAgence.getSociete(), currentUser.getUsers()};
            List<YvsUsersAgence> list = dao.loadNameQueries("YvsUsersAgence.findUsageByUsersSociete", champ, val);
            for (YvsUsersAgence a : list) {
                if (a.getCanAction()) {
                    agences.add(a.getAgence());
                }
                listAgence.add(a.getAgence());
            }
        }
        if (!listAgence.contains(currentAgence)) {
            listAgence.add(currentAgence);
        }
        if (!agences.contains(currentAgence)) {
            agences.add(currentAgence);
        }
        for (YvsAgences a : listAgence) {
            listIdAgences.add(a.getId());
        }
    }

    public void loadAllAgenceSociete() {
        if ((societe != null) ? societe.getId() != 0 : false) {
            loadAllAgenceSociete(societe.getId());
        } else {
            loadAllAgenceSociete(currentAgence.getSociete().getId());
        }
    }

    public void loadAllAgenceSociete(long societe) {
        champ = new String[]{"societe", "users"};
        val = new Object[]{new YvsSocietes(societe), currentUser.getUsers()};
        nameQueri = "YvsUsersAgence.findAgenceByUsersSociete";
        listAgence = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadAllSelecteurActivite() {
        champ = new String[]{"societe"};
        val = new Object[]{new YvsSocietes(agence.getSociete().getId())};
        listSecteurActivite = dao.loadNameQueries("YvsSecteurs.findAll", champ, val);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAllAgence(true, true);
    }

    public void findBySociete(ValueChangeEvent ev) {
        if (ev != null) {
            long id = (long) ev.getNewValue();
            listAgence.clear();
            if (id > 0) {
                loadAllAgence(id);
            } else {
                resetFiche(societe);
            }
        }
    }

    public void chooseSociete(ValueChangeEvent ev) {
        if (ev != null) {
            long id = (long) ev.getNewValue();
            listSecteurActivite.clear();
            if (id > 0) {
                ManagedSociete w = (ManagedSociete) giveManagedBean(ManagedSociete.class);
                if (w != null) {
                    int idx = w.getListSociete().indexOf(new YvsSocietes(id));
                    if (idx > -1) {
                        entitySociete = w.getListSociete().get(idx);
                        agence.setSociete(UtilSte.buildSimpleBeanSociete(entitySociete));
                    }
                }
                loadAllSelecteurActivite();
            }
        }
    }

    public void chooseSecteur() {
        if (agence.getSecteur() != null ? agence.getSecteur().getId() > 0 : false) {
            int idx = listSecteurActivite.indexOf(new YvsGrhSecteurs(agence.getSecteur().getId()));
            if (idx > -1) {
                agence.setSecteur(UtilAgence.buildBeanSecteurActivite(listSecteurActivite.get(idx)));
            }
        } else if (agence.getSecteur() != null ? agence.getSecteur().getId() == -1 : false) {
            openDialog("dlgSecteurActivite");
        }
    }

    public void chooseVille() {
        if (agence.getVille() != null ? agence.getVille().getId() > 0 : false) {
            ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
            if (m != null) {
                Dictionnaire d = m.chooseVille(agence.getPays(), agence.getVille().getId());
                if (d != null ? d.getId() > 0 : false) {
                    cloneObject(agence.getVille(), d);
                }
            }
        } else if (agence.getVille() != null ? agence.getVille().getId() == -1 : false) {
            openDialog("dlgVille");
        }
    }

    public void choosePays() {
        if (agence.getPays() != null ? agence.getPays().getId() > 0 : false) {
            update("txt_pays_ville_");
            agence.setVille(new Dictionnaire());
            ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
            if (m != null) {
                Dictionnaire d = m.choosePays(agence.getPays().getId());
                if (d != null ? d.getId() > 0 : false) {
                    cloneObject(agence.getPays(), d);
                }
            }
        } else if (agence.getPays() != null ? agence.getPays().getId() == -1 : false) {
            openDialog("dlgPays");
        }
    }

    @Override
    public Agence recopieView() {
        return agence;
    }

    @Override
    public boolean controleFiche(Agence bean) {
        if (bean.getCodeAgence() == null || "".equals(bean.getCodeAgence())) {
            getErrorMessage("Vous devez entrer le code");
            return false;
        }
        if (bean.getDesignation() == null || "".equals(bean.getDesignation())) {
            getErrorMessage("Vous devez entrer la designation");
            return false;
        }
        if (bean.getSecteur().getId() <= 0) {
            getErrorMessage("Vous devez préciser le secteur d'activité !");
            return false;
        }
        return true;
    }

    public boolean controleFicheSecteur(SecteurActivite bean) {
        if (bean.getNom() == null || "".equals(bean.getNom())) {
            getErrorMessage("Vous devez entrer le nom");
            return false;
        }
        return true;
    }

    @Override
    public boolean saveNew() {
        try {
            Agence agenc = recopieView();
            if (controleFiche(agenc)) {
                entityAgence = buildAgence(agenc);
                if (!updateAgence) {
                    entityAgence.setId(null);
                    entityAgence = (YvsAgences) dao.save1(entityAgence);
                    agence.setId(entityAgence.getId());
                    listAgence.add(0, entityAgence);
                } else {
                    dao.update(entityAgence);
                    listAgence.set(listAgence.indexOf(entityAgence), entityAgence);
                }
                setUpdateAgence(true);
                succes();
                actionOpenOrResetAfter(this);
            }
            return true;
        } catch (Exception ex) {
            getErrorMessage(ex + " sans succes");
            return false;
        }
    }

    public boolean saveNewSecteur() {
        try {
            if (controleFicheSecteur(secteur)) {
                YvsGrhSecteurs sec = new YvsGrhSecteurs(secteur.getId());
                sec.setDescription(secteur.getDescription());
                sec.setNom(secteur.getNom());
                sec.setSociete((agence.getSociete().getId() != 0) ? new YvsSocietes(agence.getSociete().getId()) : currentAgence.getSociete());
                sec.setAuthor(currentUser);
                if (secteur.getId() < 1) {
                    sec.setId(null);
                    sec = (YvsGrhSecteurs) dao.save1(sec);
                    secteur.setId(sec.getId());
                    listSecteurActivite.add(sec);
                    cloneObject(agence.getSecteur(), secteur);
                } else {
                    dao.update(sec);
                    int idx = listSecteurActivite.indexOf(sec);
                    if (idx > -1) {
                        listSecteurActivite.set(idx, sec);
                    }
                }
                resetFicheSecteur();
                succes();
                update("txt_secteur_agence");
            }
            return true;
        } catch (Exception ex) {
            System.err.println("++++++ Error : " + ex.getMessage());
            return false;
        }
    }

    public void resetFicheSecteur() {
        secteur = new SecteurActivite();
    }

    @Override
    public void resetFiche() {
        resetFiche(agence);
        agence.setResponsableAgence(new EmployePartial());
        agence.setSecteur(new SecteurActivite());
        agence.setVille(new Dictionnaire());
        agence.setPays(new Dictionnaire());
        entitySociete = currentAgence.getSociete();
        if (!isViewSociete()) {
            agence.setSociete(UtilSte.buildBeanSociete(currentAgence.getSociete()));
        } else {
            agence.setSociete(new Societe());
        }
        setUpdateAgence(false);
        entityAgence = new YvsAgences();
        resetPage();
        update("form_agence_01");
    }

    @Override
    public void resetPage() {
        setSelectAgence(false);
        updateAgence = false;
        setOptionUpdate(false);
    }

    public void deleteBean_(YvsAgences y) {
        entityAgence = y;
    }

    @Override
    public void deleteBean() {
        try {
            if (entityAgence != null) {
                dao.delete(entityAgence);
                listAgence.remove(entityAgence);
                if (entityAgence.getId().equals(agence.getId())) {
                    resetFiche();
                }
                succes();
                update("entete_agence");
                update("body_agence");
            }
        } catch (Exception ex) {
            System.err.println("++++++ Error : " + ex.getMessage());
            getErrorMessage("Erreur suppression");
        }
    }

    public void deleteBeanSecteur(YvsGrhSecteurs y) {
        try {
            if (y != null) {
                dao.delete(y);
                listSecteurActivite.remove(y);
                if (y.getId().equals(secteur.getId())) {
                    resetFicheSecteur();
                }
                succes();
            }
        } catch (Exception ex) {
            System.err.println("++++++ Error : " + ex.getMessage());
            getErrorMessage("Erreur suppression");
        }
    }

    public void changeVueEmps() {
        boolean ok = isUpdateAgence();
        if (!isUpdateAgence()) {
            openDialog("dlgConfirmSave");
        }
        if (ok) {
            setVueListeEmpls(!vueListeEmpls);
            update("head_employe_agence");
            update("dgl_employe_agence");
        }
    }

    public void closeAlldlgEmploye() {
        closeDialog("dlgEmploye");
    }

    public void confirmSave() {
        saveNew();
        changeVueEmps();
    }

    public void autoriseAccesAgence(YvsUsersAgence ua) {
        try {
            if (ua != null) {
                if (!autoriser("base_user_up_")) {
                    openNotAcces();
                    return;
                }
                long current = ua.getId();
                ua.setActif(!ua.getActif());
                ua.setAuthor(currentUser);
                ua.setDateSave(new Date());
                ua.setDateUpdate(new Date());
                ua.setAgence(new YvsAgences(agence.getId()));
                if (current < 1) {
                    ua.setId(null);
                    ua = (YvsUsersAgence) dao.save1(ua);
                } else {
                    dao.update(ua);
                }
                int index = utilisateurs.indexOf(ua);
                if (index > -1) {
                    utilisateurs.set(index, ua);
                } else if (ua.getActif()) {
                    utilisateurs.add(ua);
                    update(":tabview_agence:data_agence_users");
                }
                index = users.indexOf(ua);
                if (index > -1) {
                    users.set(index, ua);
                }
            } else {
                getErrorMessage("Erreur lors de l'enregistrement !");
            }
        } catch (Exception ex) {
            getException("autoriseAccesAgence", ex);
        }
    }

    public void autoriseActionAgence(YvsUsersAgence ua) {
        try {
            if (ua != null) {
                if (!autoriser("base_user_up_")) {
                    openNotAcces();
                    return;
                }
                long current = ua.getId();
                ua.setCanAction(!ua.getCanAction());
                ua.setAuthor(currentUser);
                ua.setDateSave(new Date());
                ua.setDateUpdate(new Date());
                ua.setAgence(new YvsAgences(agence.getId()));
                if (current < 1) {
                    ua.setId(null);
                    ua = (YvsUsersAgence) dao.save1(ua);
                } else {
                    dao.update(ua);
                }
                int index = utilisateurs.indexOf(ua);
                if (index > -1) {
                    utilisateurs.set(index, ua);
                } else if (ua.getActif()) {
                    utilisateurs.add(ua);
                    update(":tabview_agence:data_agence_users");
                }
                index = users.indexOf(ua);
                if (index > -1) {
                    users.set(index, ua);
                }
            } else {
                getErrorMessage("Erreur lors de l'enregistrement !");
            }
        } catch (Exception ex) {
            getException("autoriseActionAgence", ex);
        }
    }

    public void findOneEmploye(String matricule) {
        if (matricule != null) {
            ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
            if (service != null) {
                service.findEmploye(matricule);
                if (service.getListEmployes().size() == 1) {
                    loadOneEmploye(service.getListEmployes().get(0));
                    agence.getResponsableAgence().setError(false);
                } else if (!service.getListEmployes().isEmpty()) {
                    openDialog("dlgEmployes");
                    update("data_responsable");
                    agence.getResponsableAgence().setError(false);
                } else {
                    agence.getResponsableAgence().setError(true);
                }
            }
        }
    }

    public void fusionner(boolean fusionne) {
        try {
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty() ? ids.size() > 1 : false) {
                long newValue = listAgence.get(ids.get(0)).getId();
                if (!fusionne) {
                    if (!autoriser("base_user_fusion")) {
                        openNotAcces();
                        return;
                    }
                    String oldValue = "0";
                    for (int i : ids) {
                        if (listAgence.get(i).getId() != newValue) {
                            oldValue += "," + listAgence.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_agences", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                listAgence.remove(new YvsAgences(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = listAgence.get(idx).getDesignation();
                    } else {
                        YvsAgences c = (YvsAgences) dao.loadOneByNameQueries("YvsAgences.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                            fusionneTo = c.getDesignation();
                        }
                    }
                    YvsAgences c;
                    for (int i : ids) {
                        long oldValue = listAgence.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(listAgence.get(i).getDesignation());
                            }
                        } else {
                            c = (YvsAgences) dao.loadOneByNameQueries("YvsAgences.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                                fusionnesBy.add(c.getDesignation());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 agences");
            }
        } catch (NumberFormatException ex) {
        }
    }
}
