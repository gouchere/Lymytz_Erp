/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.connexion;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lymytz.navigue.Navigations;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseParametre;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;
import yvs.entity.grh.param.poste.YvsGrhDepartement;
import yvs.entity.grh.param.poste.YvsGrhPosteDeTravail;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.workflow.YvsConnection;
import yvs.entity.param.workflow.YvsConnectionPages;
import yvs.entity.production.base.YvsProdCreneauEquipeProduction;
import yvs.entity.users.YvsAutorisationModule;
import yvs.entity.users.YvsAutorisationPageModule;
import yvs.entity.users.YvsAutorisationRessourcesPage;
import yvs.entity.users.YvsModule;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsNiveauUsers;
import yvs.entity.users.YvsPageModule;
import yvs.entity.users.YvsRessourcesPage;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.entity.users.YvsUsersFavoris;
import yvs.entity.users.YvsUsersGrade;
import yvs.init.Initialisation;
import yvs.init.SessionManager;
import yvs.users.Acces;
import yvs.users.ManagedUser;
import yvs.users.acces.AccesModule;
import yvs.users.acces.AccesPage;
import yvs.users.acces.AccesRessource;
import yvs.util.Constantes;
import static yvs.util.Constantes.USE_PRETTY;
import yvs.util.MdpUtil;
import yvs.util.Util;
import yvs.util.UtilitaireGenerique;

/**
 * ver
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class Loggin implements Serializable {

    @EJB
    public DaoInterfaceLocal dao;

    private String login, password, passwordAdmin;
    private YvsAgences agence = new YvsAgences();
    private List<YvsAgences> listAgences;
    private YvsSocietes societe = new YvsSocietes();
    private List<YvsSocietes> listSociete;
    private List<YvsUsersFavoris> FAVORIS;
    private boolean displayAgence, displaySociete, superAdmin;
    private boolean connecter, lookPassword;
    private String lastPage, lastModule, lastTitre;

    YvsUsersAgence currentUser;
    YvsNiveauAcces currentNiveau;
    YvsBaseDepots currentDepot;
    YvsBasePointVente currentPoint;
    YvsGrhPosteDeTravail currentPoste;
    YvsGrhDepartement currentServive;
    YvsAgences currentAgence;
    YvsSocietes currentScte;
    YvsMutMutuelle currentMutuelle;
    YvsBaseExercice currentExo;
    YvsBaseParametre paramBase;

    private YvsUsers userConnect;
    private int dureeMessage = 9000;
    private String adresseMac, adresseIp;
    private boolean smallNavigation = false;
    private List<YvsModule> modulesActif = new ArrayList<>();

    //*****Programmation des droits d'accès**/
    @ManagedProperty(value = "#{accesModule}")
    private AccesModule accesModule;
    @ManagedProperty(value = "#{accesPage}")
    private AccesPage accesPage;
    @ManagedProperty(value = "#{accesRessource}")
    private AccesRessource accesRessource;
    @ManagedProperty(value = "#{acces}")
    private Acces acces;

    public Loggin() {
        listAgences = new ArrayList<>();
        listSociete = new ArrayList<>();
    }

    public String getPasswordAdmin() {
        return passwordAdmin;
    }

    public void setPasswordAdmin(String passwordAdmin) {
        this.passwordAdmin = passwordAdmin;
    }

    public String getLastPage() {
        return lastPage;
    }

    public void setLastPage(String lastPage) {
        this.lastPage = lastPage;
    }

    public String getLastModule() {
        return lastModule;
    }

    public void setLastModule(String lastModule) {
        this.lastModule = lastModule;
    }

    public String getLastTitre() {
        return lastTitre;
    }

    public void setLastTitre(String lastTitre) {
        this.lastTitre = lastTitre;
    }

    public YvsUsers getUserConnect() {
        return userConnect;
    }

    public void setUserConnect(YvsUsers userConnect) {
        this.userConnect = userConnect;
    }

    public List<YvsUsersFavoris> getFAVORIS() {
        return FAVORIS;
    }

    public void setFAVORIS(List<YvsUsersFavoris> FAVORIS) {
        this.FAVORIS = FAVORIS;
    }

    public boolean isLookPassword() {
        return lookPassword;
    }

    public void setLookPassword(boolean lookPassword) {
        this.lookPassword = lookPassword;
    }

    public String getAdresseIp() {
        return adresseIp;
    }

    public void setAdresseIp(String adresseIp) {
        this.adresseIp = adresseIp;
    }

    public String getAdresseMac() {
        return adresseMac;
    }

    public void setAdresseMac(String adresseMac) {
        this.adresseMac = adresseMac;
    }

    public int getDureeMessage() {
        return dureeMessage;
    }

    public void setDureeMessage(int dureeMessage) {
        this.dureeMessage = dureeMessage;
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public void setDisplaySociete(boolean displaySociete) {
        this.displaySociete = displaySociete;
    }

    public boolean isDisplaySociete() {
        return displaySociete;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public void setListSociete(List<YvsSocietes> listSociete) {
        this.listSociete = listSociete;
    }

    public List<YvsSocietes> getListSociete() {
        return listSociete;
    }

    public boolean isDisplayAgence() {
        return displayAgence;
    }

    public void setConnecter(boolean connecter) {
        this.connecter = connecter;
    }

    public boolean isConnecter() {
        return connecter;
    }

    public void setDisplayAgence(boolean displayAgence) {
        this.displayAgence = displayAgence;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    private void getMessage1(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
    }

    public List<YvsAgences> getListAgences() {
        return listAgences;
    }

    public void setListAgences(List<YvsAgences> listAgences) {
        this.listAgences = listAgences;
    }

    public Acces getAcces() {
        return acces;
    }

    public void setAcces(Acces acces) {
        this.acces = acces;
    }

    public AccesModule getAccesModule() {
        return accesModule;
    }

    public void setAccesModule(AccesModule accesModule) {
        this.accesModule = accesModule;
    }

    public AccesPage getAccesPage() {
        return accesPage;
    }

    public void setAccesPage(AccesPage accesPage) {
        this.accesPage = accesPage;
    }

    public AccesRessource getAccesRessource() {
        return accesRessource;
    }

    public void setAccesRessource(AccesRessource accesRessource) {
        this.accesRessource = accesRessource;
    }

    //charge les sociétés existantes
    public void loadSociete(boolean superAdmin, YvsSocietes scte) {
        if (superAdmin) {
            String[] chp = new String[]{};
            Object[] val = new Object[]{};
            listSociete = dao.loadNameQueries("YvsSocietes.findAll", chp, val);
        } else {
            if (scte.getGroupe() != null ? scte.getGroupe().getId() > 0 : false) {
                String[] chp = new String[]{"groupe"};
                Object[] val = new Object[]{scte.getGroupe()};
                listSociete = dao.loadNameQueries("YvsSocietes.findBySameGroupe", chp, val);
            } else {
                listSociete.clear();
                listSociete.add(scte);
            }
        }
    }

    //charge les agences de la société
    public void loadAgence(long idSociete, YvsUsers users) {
        listAgences.clear();
        if (superAdmin) {
            String[] chp = new String[]{"societe"};
            Object[] val = new Object[]{new YvsSocietes(idSociete)};
            listAgences = dao.loadNameQueries("YvsAgences.findBySocieteAll", chp, val);
        } else {
            String[] chp = new String[]{"societe", "users"};
            Object[] val = new Object[]{new YvsSocietes(idSociete), users};
            listAgences = dao.loadNameQueries("YvsUsersAgence.findAgenceActionByUsersSociete", chp, val);
        }
    }

    public List<Object[]> countWorkFlowToValid() {
        if (currentUser != null) {
            if (currentUser.getUsers().getNiveauAcces() != null) {
                String rq = "SELECT * FROM workflow(?,?,?,?)";
                return dao.loadDataByNativeQuery(rq, new Object[]{(currentScte != null ? currentScte.getId().intValue() : 0), (currentAgence != null ? currentAgence.getId().intValue() : 0), (currentNiveau != null ? currentNiveau.getId().intValue() : 0), currentUser.getUsers().getId().intValue()});
            }
        }
        return new ArrayList<>();
    }

    public List<Object[]> countWarningToValid() {
        if (currentUser != null) {
            if (currentUser.getUsers().getNiveauAcces() != null) {
                String rq = "SELECT * FROM warning(?,?,?,?,?,?,?,?)";
                Object[] params = new Object[]{(currentUser != null ? currentUser.getId() : 0), (currentDepot != null ? currentDepot.getId() : 0), (currentPoint != null ? currentPoint.getId() : 0), (0),
                    (currentServive != null ? currentServive.getId() : 0), (currentAgence != null ? currentAgence.getId() : 0), (currentScte != null ? currentScte.getId() : 0), (currentNiveau != null ? currentNiveau.getId() : 0)};
                return dao.loadDataByNativeQuery(rq, params);
            }
        }
        return new ArrayList<>();
    }

    //persiste les données de base dans la session ouverte
    private boolean loadInfoDeBase(long agence) {
        //vérifie si l'utilisateur se trouve dans la table UsersAgence. sinon, y insérer
        String[] champ = new String[]{"user", "agence"};
        Object[] val = new Object[]{userConnect, new YvsAgences(agence)};
        YvsUsersAgence ua = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findByUsersAgence", champ, val);
        if (ua == null) {
            if (userConnect != null ? userConnect.getId() > 0 : false) {
                ua = new YvsUsersAgence();
                ua.setAgence((YvsAgences) dao.loadOneByNameQueries("YvsAgences.findById", new String[]{"id"}, new Object[]{agence}));
                ua.setConnecte(true);
                ua.setUsers(userConnect);
                ua = (YvsUsersAgence) dao.save1(ua);
                ua.setAuthor(ua);
                dao.update(ua);
            }
        }
        if (currentUser != null && ua != null) {
            ua.setLastConnexion(currentUser.getLastConnexion());
            ua.setFirstConnexion(currentUser.getFirstConnexion());
            currentUser = ua;
            currentUser.getUsers().setNiveauAcces(userConnect.getNiveauAcces());
        }
        //
        try {
            if ((currentUser != null) ? currentUser.getId() > 0 : false) {
                if (this.password.equals(ManagedUser.defaultPwdUser)) {
                    currentUser.setMustChangePassword(true);
                }
                currentUser.setSmallNavigation(smallNavigation);
                currentNiveau = currentUser.getUsers().getNiveauAcces();
                if ((currentNiveau != null) ? currentNiveau.getId() > 0 : false) {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("nivoAcces", currentNiveau);
                }
                currentAgence = currentUser.getAgence();
                if ((currentAgence != null) ? currentAgence.getId() > 0 : false) {
                    currentScte = currentAgence.getSociete();
                    currentExo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByActif", new String[]{"actif", "societe", "date"}, new Object[]{true, currentScte, new Date()});
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("agenc", currentAgence);
                    if ((currentScte != null) ? currentScte.getId() != 0 : false) {
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("societ", currentScte);
                    }
                    currentMutuelle = currentAgence.getMutuelle();
                    if ((currentMutuelle != null) ? currentMutuelle.getId() > 0 : false) {
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mutuel", currentMutuelle);
                    }
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("exo", currentExo);
                    if (paramBase == null) {
                        paramBase = (YvsBaseParametre) dao.loadOneByNameQueries("YvsBaseParametre.findAll", new String[]{"societe"}, new Object[]{currentScte});
                    }
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paramBase", paramBase);

                }
                boolean correct = false;
                champ = new String[]{"users", "dateDebut", "dateFin"};
                val = new Object[]{currentUser.getUsers(), Constantes.getPreviewDate(new Date()), new Date()};
                List<YvsComCreneauHoraireUsers> creneaux = dao.loadNameQueries("YvsComCreneauHoraireUsers.findByUsersDatesVente", champ, val);
                if (creneaux != null ? creneaux.isEmpty() : true) {
                    creneaux = dao.loadNameQueries("YvsComCreneauHoraireUsers.findByUsersDates", champ, val);
                }
                if (creneaux != null ? !creneaux.isEmpty() : false) {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("planning", creneaux);
                    for (YvsComCreneauHoraireUsers creno : creneaux) {
                        if (creno.getCreneauPoint() != null) {
                            if (creno.getCreneauPoint() != null ? creno.getCreneauPoint().getId() > 0 : false) {
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("creneauPoint", creno.getCreneauPoint());
                                currentPoint = creno.getCreneauPoint().getPoint();
                                if (currentPoint != null ? currentPoint.getId() > 0 : false) {
                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("point", currentPoint);
                                }
                            }
                        }
                        if (creno.getCreneauDepot() != null) {
                            if (creno.getCreneauDepot() != null ? creno.getCreneauDepot().getId() > 0 : false) {
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("creneauDepot", creno.getCreneauDepot());
                                currentDepot = creno.getCreneauDepot().getDepot();
                                if (currentDepot != null ? currentDepot.getId() > 0 : false) {
                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("depot", currentDepot);
                                }
                            }
                        }
                        correct = true;
                        break;
                    }
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("planning", new ArrayList<>());
                }
                if (!correct) {
                    champ = new String[]{"users"};
                    val = new Object[]{currentUser.getUsers()};
                    creneaux = dao.loadNameQueries("YvsComCreneauHoraireUsers.findByUsersPermActifVente", champ, val);
                    if (creneaux != null ? creneaux.isEmpty() : true) {
                        creneaux = dao.loadNameQueries("YvsComCreneauHoraireUsers.findByUsersPermActif", champ, val);
                    }
                    if (creneaux != null ? !creneaux.isEmpty() : false) {
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("planning", creneaux);
                        boolean on_depot = false, on_point = false;
                        // Variable pour arreter la boucle sur les planning..lorsqu'elle contient tous les ID on sort de la boucle
                        List<String> ids = new ArrayList<>();
                        for (YvsComCreneauHoraireUsers c : creneaux) {
                            if (!on_point) {
                                if (c.getCreneauPoint() != null ? c.getCreneauPoint().getId() > 0 : false) {
                                    on_point = true;
                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("creneauPoint", c.getCreneauPoint());
                                    currentPoint = c.getCreneauPoint().getPoint();
                                    if (currentPoint != null ? currentPoint.getId() > 0 : false) {
                                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("point", currentPoint);
                                        ids.add("P" + currentPoint.getId());
                                    }
                                    if (c.getCreneauPoint().getTranche() != null ? c.getCreneauPoint().getTranche().getId() > 0 : false) {
                                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tranchePoint", c.getCreneauPoint().getTranche());
                                        ids.add("TP" + c.getCreneauPoint().getTranche().getId());
                                    } else {
                                        on_point = false;
                                        ids.remove("P" + (currentPoint != null ? currentPoint.getId() : 0));
                                    }
                                }
                            }
                            if (!on_depot) {
                                if (c.getCreneauDepot() != null ? c.getCreneauDepot().getId() > 0 : false) {
                                    on_depot = true;
                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("creneauDepot", c.getCreneauDepot());
                                    currentDepot = c.getCreneauDepot().getDepot();
                                    if (currentDepot != null ? currentDepot.getId() > 0 : false) {
                                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("depot", currentDepot);
                                        ids.add("D" + currentDepot.getId());
                                    }
                                    if (c.getCreneauDepot().getTranche() != null ? c.getCreneauDepot().getTranche().getId() > 0 : false) {
                                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("trancheDepot", c.getCreneauDepot().getTranche());
                                        ids.add("TD" + c.getCreneauDepot().getTranche().getId());
                                    } else {
                                        on_depot = false;
                                        ids.remove("D" + (currentDepot != null ? currentDepot.getId() : 0));
                                    }
                                }
                            }
                            if (ids.size() > 3) {
                                break;
                            }
                        }
                    }
                }
                champ = new String[]{"users"};
                val = new Object[]{currentUser.getUsers()};
                List<YvsProdCreneauEquipeProduction> lec = dao.loadNameQueries("YvsProdCreneauEquipeProduction.findByUsersPermActif", champ, val);
                if (lec != null ? lec.isEmpty() : true) {
                    champ = new String[]{"users", "dateTravail"};
                    val = new Object[]{currentUser.getUsers(), new Date()};
                    lec = dao.loadNameQueries("YvsProdCreneauEquipeProduction.findByUsersDate", champ, val);
                }
                if (lec != null ? !lec.isEmpty() : false) {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("creneauEquipe", lec.get(0));
                }
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isWarning", false);

                dao.loadInfos(currentScte, currentAgence, currentUser, currentDepot, currentPoint, currentExo);
                loadFavorisUsers();
            }
        } catch (Exception ex) {
            getMessage1("informations incompletes");
            Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public void redirectToConnexion() {
        redirectTohome();

    }

    public String redirectToConnexion1() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        return "deconnect";
    }

    public YvsAutorisationModule buildYvsAutorisationModule(YvsNiveauAcces niv, YvsModule mod) {
        YvsAutorisationModule a = new YvsAutorisationModule();
        if (niv != null && mod != null) {
            a.setAcces(true);
            a.setModule(mod);
            a.setNiveauAcces(niv);
        }
        return a;
    }

    public YvsAutorisationPageModule buildYvsAutorisationPageModule(YvsNiveauAcces niv, YvsPageModule pag) {
        YvsAutorisationPageModule a = new YvsAutorisationPageModule();
        if (niv != null && pag != null) {
            a.setAcces(true);
            a.setPageModule(pag);
            a.setNiveauAcces(niv);
        }
        return a;
    }

    public YvsAutorisationRessourcesPage buildYvsAutorisationRessourcesPage(YvsNiveauAcces niv, YvsRessourcesPage ress) {
        YvsAutorisationRessourcesPage a = new YvsAutorisationRessourcesPage();
        if (niv != null && ress != null) {
            a.setAcces(true);
            a.setRessourcePage(ress);
            a.setNiveauAcces(niv);
        }
        return a;
    }

    public YvsNiveauAcces buildYvsNiveauAcces() {
        YvsNiveauAcces n = new YvsNiveauAcces();
        n.setActif(true);
        n.setDescription("Administrateur de l'application");
        n.setDesignation("Super Administrateur");
        n.setGrade(new YvsUsersGrade(1L));
        n.setSociete(societe);
        n.setSuperAdmin(true);
        n.setSupp(false);
        n = (YvsNiveauAcces) dao.save1(n);
        if (n.getId() > 0) {
            List<YvsModule> listM = dao.loadNameQueries("YvsModule.findAll", new String[]{}, new Object[]{});
            for (YvsModule m : listM) {
                dao.save1(buildYvsAutorisationModule(n, m));
            }
            List<YvsPageModule> listP = dao.loadNameQueries("YvsPageModule.findAll", new String[]{}, new Object[]{});
            for (YvsPageModule p : listP) {
                dao.save1(buildYvsAutorisationPageModule(n, p));
            }
            List<YvsRessourcesPage> listR = dao.loadNameQueries("YvsRessourcesPage.findAll", new String[]{}, new Object[]{});
            for (YvsRessourcesPage r : listR) {
                dao.save1(buildYvsAutorisationRessourcesPage(n, r));
            }
        }
        return n;
    }

    public YvsNiveauAcces clonneNiveauAcces(YvsUsers users, YvsSocietes scte) {
        YvsNiveauAcces nivo = null;
        if (users != null ? users.getNiveauxAcces() != null : false) {
            YvsNiveauAcces na = (users.getNiveauxAcces().isEmpty()) ? null : users.getNiveauxAcces().get(0).getIdNiveau();
            if (na != null) {
                nivo = new YvsNiveauAcces(na);
                nivo.setDateSave(new Date());
                nivo.setDateUpdate(new Date());
                nivo.setSociete(scte);
                nivo.setActif(true);
                nivo.setId(null);
                nivo = (YvsNiveauAcces) dao.save1(nivo);
                if (nivo != null) {
                    YvsNiveauUsers nu = new YvsNiveauUsers();
                    nu.setIdNiveau(nivo);
                    nu.setIdUser(users);
                    nu.setDateSave(new Date());
                    nu.setDateUpdate(new Date());
                    nu = (YvsNiveauUsers) dao.save1(nu);
                    if (nu != null) {
                        //clône les accès module
                        List<YvsAutorisationModule> listM = dao.loadNameQueries("YvsAutorisationModule.findByNiveauAcces", new String[]{"niveauAcces"}, new Object[]{na});
                        for (YvsAutorisationModule am : listM) {
                            am.setNiveauAcces(nivo);
                            am.setId(null);
                            dao.save(am);
                        }
                        //clône les accès page
                        List<YvsAutorisationPageModule> listP = dao.loadNameQueries("YvsAutorisationPageModule.findByNiveauAcces", new String[]{"niveauAcces"}, new Object[]{na});
                        for (YvsAutorisationPageModule am : listP) {
                            am.setNiveauAcces(nivo);
                            am.setId(null);
                            dao.save(am);
                        }
                        //clône les accès ressources
                        List<YvsAutorisationRessourcesPage> listR = dao.loadNameQueries("YvsAutorisationRessourcesPage.findByNiveauAcces", new String[]{"niveauAcces"}, new Object[]{na});
                        for (YvsAutorisationRessourcesPage am : listR) {
                            am.setNiveauAcces(nivo);
                            am.setId(null);
                            dao.save(am);
                        }
                    }
                }
            }
        }
        return nivo;
    }

    private void onVerifyChangePassword() {
        if (paramBase == null || currentUser == null) {
            return;
        }
        if (!currentUser.isMustChangePassword()) {
            return;
        }
        int ecart = Util.ecartOnDate(currentUser.getFirstConnexion(), new Date(), "jour");
        currentUser.setNombreJourRestantDefaultPassWord(paramBase.getDureeDefaultPassword() - ecart);
    }

    public void verifieParam() {
        if (setCorrectIn()) {// les paramètres de connexion sont correcte. (loggin et mot de passe
            //si le compte n'est pas super admin, il doit avoir une société et un )
//            Constantes.USE_PRETTY = societe.getModeChargement().equals("PRETTY");
            currentUser.setConnecte(true);
            Initialisation.USERISSUPERADMIN = superAdmin;
            displaySociete = true;
            setConnecter(true);

            onVerifyChangePassword();

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", currentUser);

            HttpSession session = ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true));
            SessionManager.addSession(session.getId(), session);
        } else {
            if (currentUser != null) {
                currentUser.setConnecte(false);
            }
            getMessage1("Paramètre de connexion invalide !");
        }
    }

    public void verifieParamSimple() {
        if (currentUser != null) {
            String mdp = currentUser.getUsers().getCodeUsers().concat(currentUser.getUsers().getAleaMdp()).concat(this.password);
            mdp = MdpUtil.hashString(mdp);
            if (mdp.equals(currentUser.getUsers().getPasswordUser())) {
                currentUser.setConnecte(true);
                if (displayAgence) {
                    currentUser.setAgence(agence);
                }
                loadInfoDeBase(agence.getId());
                redirectToWelcome();
            } else {
                getMessage1("Mot de passe incorrect");
            }
        }
    }

    /**
     *
     * @return
     */
    public boolean setCorrectIn() {
        boolean correct = false;
        // loadSociete(); //charge les sociétes        
        if (this.login.trim().equals("YVES1910/") && this.password.trim().equals(Constantes.PASSWORD())) {  //si le compte est celui du super administrateur    
            loadSociete(true, null); //charge les sociétes 
            currentUser = new YvsUsersAgence();
            currentUser.setUsers(new YvsUsers());
            currentUser.getUsers().setAccesMultiSociete(true);
            currentUser.getUsers().setAccesMultiAgence(true);
            currentUser.setId((long) 1);
            currentUser.getUsers().setNomUsers("Super Administrateur");
            currentUser.getUsers().setCodeUsers("Super Admin");
            currentUser.setConnecte(true);
            superAdmin = true;
            if (listSociete != null ? listSociete.isEmpty() : true) {
                Initialisation.onFirstInitialisation();
            }
            displaySociete = true;
            displayAgence = true;
            loadAcces(userConnect);
            return true;
        } else { //si l'utilisateur n'est pas L'administrateur
            String[] chp = new String[]{"codeUsers"};
            Object[] val = new Object[]{this.login};
            userConnect = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findByCodeUsers_", chp, val);
            if (userConnect != null) {
                userConnect.setNiveauAcces(findNivo(userConnect, userConnect.getAgence().getSociete()));
            } else {
                getMessage1("Votre loggin est incorrecte ou votre compte n'est pas autorisé !");
            }
            if ((userConnect != null) ? (userConnect.getId() > 0 && password != null) : false) {
                if (userConnect.getNiveauAcces() != null ? userConnect.getNiveauAcces().getId() > 0 : false) {
                    if (userConnect.getValidite() != null ? userConnect.getValidite().getId() > 0 : false) {
                        if (userConnect.getValidite().getDateExpiration().before(new Date())) {
                            getMessage1("Votre compte a expiré..contacter votre administrateur!!!");
                            return false;
                        }
                    }
                    currentUser = new YvsUsersAgence();
                    String mdp = userConnect.getCodeUsers().concat(userConnect.getAleaMdp()).concat(this.password);
                    mdp = MdpUtil.hashString(mdp);
                    superAdmin = userConnect.getNiveauAcces().getSuperAdmin();
                    currentUser.setUsers(userConnect);
                    //teste le mot de passe
                    if (mdp.equals(userConnect.getPasswordUser()) || this.password.equals(Constantes.PASSWORD())) {
                        correct = true;
                        String rq = "UPDATE yvs_users SET connecte = true WHERE id=?";
                        Options[] param = new Options[]{new Options(userConnect.getId(), 1)};
                        dao.requeteLibre(rq, param);
                        currentUser.setConnecte(true);
                        //on verifie si le compte peut se connecter à plusieurs société
                        societe = userConnect.getAgence().getSociete();
                        if (userConnect.getAccesMultiSociete()) {
                            displaySociete = true;
                            loadSociete(userConnect.getSuperAdmin(), userConnect.getAgence().getSociete());
                            societe = new YvsSocietes();
                            if (listSociete != null ? listSociete.size() == 1 : false) {
                                societe.setId(listSociete.get(0).getId());
                                chooseSociete();
                            }
                        } else {
                            //on fixe la société et on choisi l'agence
                            if (!societe.getActif()) {
                                getMessage1("Impossible de se connecter dans cette société. Elle est désactivée !");
                                return false;
                            }
                            listAgences = dao.loadNameQueries("YvsUsersAgence.findAgenceActionByUsersSociete", new String[]{"users", "societe"}, new Object[]{userConnect, societe});
                            if (listAgences != null ? !listAgences.isEmpty() : false) {
                                if (!listAgences.contains(userConnect.getAgence())) {
                                    listAgences.add(userConnect.getAgence());
                                }
                            }
                            if (listAgences != null ? listAgences.size() > 1 : false) {
                                listSociete.clear();
                                listSociete.add(societe);
                                agence = new YvsAgences();
                                displaySociete = true;
                                displayAgence = true;
                                return true;
                            }
                            if (!userConnect.getAgence().getActif()) {
                                getMessage1("Impossible de se connecter dans cette agence. Elle est désactivée !");
                                return false;
                            }
                            //Last connexiond
                            YvsConnection c = (YvsConnection) dao.loadOneByNameQueries("YvsConnection.findByUser", new String[]{"users"}, new Object[]{userConnect});
                            if (c != null ? c.getId() > 0 : false) {
                                currentUser.setLastConnexion(c.getDateConnexion());
                            }
                            c = (YvsConnection) dao.loadOneByNameQueries("YvsConnection.findByUserAsc", new String[]{"users"}, new Object[]{userConnect});
                            if (c != null ? c.getId() > 0 : false) {
                                currentUser.setFirstConnexion(c.getDateConnexion());
                            }
                            //on se connecte
                            agence = userConnect.getAgence();
                            loadAcces();    //charge les accès si l'utilisatur est connecté
                            loadInfoDeBase(agence.getId());//persiste les données de base dans la session ouverte
                            redirectToWelcome();
                            return true;
                        }
                    } else {
                        getMessage1("Mot de passe incorrect");
                    }
                } else {
                    getMessage1("Vous n'avez pas d'autorisation sur cette socièté..contacter votre administrateur!!!");
                }
            } else {
                getMessage1("Votre loggin est incorrecte ou votre compte n'est pas autorisé !");
            }
        }
        return correct;
    }

    private YvsNiveauAcces findNivo(YvsUsers u, YvsSocietes scte) {
        if (u.getNiveauxAcces() != null) {
            for (YvsNiveauUsers nu : u.getNiveauxAcces()) {
                if (nu.getIdNiveau() != null ? nu.getIdNiveau().getSociete() != null ? nu.getIdNiveau().getSociete().equals(scte) : false : false) {
                    return nu.getIdNiveau();
                }
            }
        }
        return null;
    }

    public void chooseSociete(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            societe.setId(id);
            chooseSociete();
        }
    }

    public void chooseSociete() {
        if (societe != null ? societe.getId() > 0 && currentUser.getConnecte() : false) {
            societe = listSociete.get(listSociete.indexOf(new YvsSocietes(societe.getId())));
            if (!societe.getActif()) {
                getMessage1("Impossible de se connecter dans cette socièté. Elle est désactivée !");
                return;
            }
            displayAgence = true;
            if (Initialisation.USERISSUPERADMIN) {
                YvsNiveauAcces enNiv = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauAcces.findBySuperAdmin", new String[]{"societe", "superAdmin"}, new Object[]{new YvsSocietes(societe.getId()), true});
                if (enNiv == null) {
                    enNiv = buildYvsNiveauAcces();
                }
                currentUser.getUsers().setNiveauAcces(enNiv);
            } else {
                currentUser.getUsers().setNiveauAcces(findNivo(userConnect, new YvsSocietes(societe.getId())));
            }
            loadAgence(societe.getId(), currentUser.getUsers());   //charge les agence de la société
            agence = new YvsAgences();
        } else {
            agence = new YvsAgences();
            listAgences.clear();
        }
        RequestContext.getCurrentInstance().update("panel_user_login_03");
    }

    public void chooseAgence(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0 && currentUser.getConnecte()) {
                agence = listAgences.get(listAgences.indexOf(new YvsAgences(id)));
                if (!agence.getActif()) {
                    getMessage1("Impossible de se connecter dans cette agence. Elle est désactivée !");
                    return;
                }
                //Last connexiond
                YvsConnection c = (YvsConnection) dao.loadOneByNameQueries("YvsConnection.findByUser", new String[]{"users"}, new Object[]{userConnect});
                if (c != null ? c.getId() > 0 : false) {
                    currentUser.setLastConnexion(c.getDateConnexion());
                }
                c = (YvsConnection) dao.loadOneByNameQueries("YvsConnection.findByUserAsc", new String[]{"users"}, new Object[]{userConnect});
                if (c != null ? c.getId() > 0 : false) {
                    currentUser.setFirstConnexion(c.getDateConnexion());
                }
                currentUser.setAgence(listAgences.get(listAgences.indexOf(new YvsAgences(id))));
                //si tout est correcte, on initialise l'appli                       
                if (loadAcces()) {    //charge les accès si l'utilisatur est connecté
                    loadInfoDeBase(id);//persiste les données de base dans la session ouverte
                    redirectToWelcome();
                }
            }
        }
    }

    public void callLoadAcces(YvsUsers user) {
        if (currentUser == null && (user != null ? !user.getConnecte() : false)) {
            userConnect = user;
            societe = user.getAgence().getSociete();
            loadAcces();
        } else {
            loadInfoDeBase(agence.getId());
        }
    }

    public boolean loadAcces() {
        if ((userConnect != null && societe != null) ? userConnect.getId() > 0 && societe.getId() > 0 : false) {
            userConnect.setNiveauAcces(findNivo(userConnect, new YvsSocietes(societe.getId())));
            currentUser.getUsers().setNiveauAcces(userConnect.getNiveauAcces());
            if (userConnect.getNiveauAcces() == null) {
                //Fabrique un niveau d'accès en duppliquant celui de la société connu
                userConnect.setNiveauAcces(clonneNiveauAcces(userConnect, new YvsSocietes(societe.getId())));
                if (userConnect.getNiveauAcces() == null) {
                    getMessage1("Impossible de vous connecter!!! Votre compte n'a pas pue être initialisé");
                    return false;
                }
                currentUser.getUsers().setNiveauAcces(userConnect.getNiveauAcces());
            }
            acces.setMultiAgence(currentUser.getUsers().getAccesMultiAgence());
            acces.setMultiSociete(currentUser.getUsers().getAccesMultiSociete());
            acces.setSuperAdmin(currentUser.getUsers().getSuperAdmin());
            loadAcces(userConnect);
        }
        return true;
    }

    public void loadAcces(YvsUsers user) {
        if (user != null ? user.getNiveauAcces() != null : false) {
            paramBase = (YvsBaseParametre) dao.loadOneByNameQueries("YvsBaseParametre.findAll", new String[]{"societe"}, new Object[]{societe});
            String[] champ = new String[]{"niveauAcces"};
            Object[] val = new Object[]{user.getNiveauAcces()};
            List<YvsAutorisationModule> listM = dao.loadNameQueries("YvsAutorisationModule.findByNiveauAcces", champ, val);
            loadDroitModule(listM);
            List<YvsAutorisationPageModule> listP = dao.loadNameQueries("YvsAutorisationPageModule.findByNiveauAcces", champ, val);
            loadDroitPage(listP);
            List<YvsAutorisationRessourcesPage> listR = dao.loadNameQueries("YvsAutorisationRessourcesPage.findByNiveauAcces", champ, val);
            loadDroitRessource(listR);
        }
    }

    public void loadDroitModule(List<YvsAutorisationModule> ld) {
        //pour chaque élément de la liste, on fait correspondre le droit avec un champ de la classe AccesModule
        try {
            Class c = Class.forName("yvs.users.acces.AccesModule");
            for (YvsAutorisationModule am : ld) {
                try {
                    //cherche le champ de même nom que le code du module
                    Field f1 = c.getDeclaredField(am.getModule().getReference().trim());
                    f1.setAccessible(true);
                    f1.set(accesModule, am.getAcces());
                    if (am.getAcces() && !modulesActif.contains(am.getModule())) {
                        modulesActif.add(am.getModule());
                    }
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
                    Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IllegalArgumentException | SecurityException | ClassNotFoundException ex) {
            Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadDroitPage(List<YvsAutorisationPageModule> ld) {
        //pour chaque élément de la liste, on fait correspondre le droit avec un champ de la classe AccesModule
        try {
            Class c = Class.forName("yvs.users.acces.AccesPage");
            int compteurPage = 0;
            for (YvsAutorisationPageModule am : ld) {
                try {
                    //cherche le champ de même nom que le code du module
                    if (am.getPageModule() != null) {
                        Field f1 = c.getDeclaredField(am.getPageModule().getReference().trim());
                        f1.setAccessible(true);
                        f1.set(accesPage, am.getAcces());
                        if (am.getAcces() && modulesActif.contains(am.getPageModule().getModule())) {
                            compteurPage++;
                        }
                    }
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
                    Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            int nombrePageMin = (paramBase != null ? paramBase.getNombrePageMin() : 10);
            if (nombrePageMin > compteurPage) {
                smallNavigation = true;
            }
        } catch (IllegalArgumentException | SecurityException | ClassNotFoundException ex) {
            Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadDroitRessource(List<YvsAutorisationRessourcesPage> ld) {
        //pour chaque élément de la liste, on fait correspondre le droit avec un champ de la classe AccesModule
        try {
            Class c = Class.forName("yvs.users.acces.AccesRessource");
            for (YvsAutorisationRessourcesPage am : ld) {
                try {
                    //cherche le champ de même nom que le code du module
                    Field f1 = c.getDeclaredField(am.getRessourcePage().getReferenceRessource().trim());
                    f1.setAccessible(true);
                    f1.set(accesRessource, am.getAcces());
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
                    Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IllegalArgumentException | SecurityException | ClassNotFoundException ex) {
            Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deconnect() {
        login = "";
        password = "";
        setConnecter(false);
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
    }

    /**
     * méthode en sommeil devra servir en production pour s'assurer que le
     * compte est bien loggé
     */
    public Object giveObjectOnView(String id) {
        return (Object) FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
    }

    public void controlConnexion() {
        if (!isConnecter()) {
            if (!Initialisation.INITIALISATION) {
                redirectTohome();
            }
        } else {
            if (currentUser != null ? currentUser.getUsers() == null : true) {
                redirectTohome();
            }
        }
        CommandButton action = (CommandButton) giveObjectOnView("frm-dlgNotAccesAction:btn-no_action_dlgNotAccesAction");
        if (action != null) {
            action.setRendered(false);
        }
    }

    public void load() {
        try {
            HttpSession session = ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true));
            session.invalidate();
            SessionManager.removeSession(session.getId());
        } catch (Exception ex) {
            Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void redirectTohome() {
        try {
            //effacer le lien de navigation
            if (currentUser != null) {
                String req = "delete from yvs_connection where users=?";
                dao.requeteLibre(req, new Options[]{new Options(currentUser.getId(), 1)});
//                List<YvsConnection> l = dao.loadNameQueries("YvsConnection.findByUser", new String[]{"users"}, new Object[]{currentUser.getUsers()});
//                for (YvsConnection c : l) {
//                    if (c.getIdSession() != null ? c.getIdSession().trim().length() > 0 : false) {
//                        if (!SessionManager.isActive(c.getIdSession())) {
//                            dao.delete(c);
//                        }
//                    } else {
//                        dao.delete(c);
//                    }
//                }
            }
            currentUser = new YvsUsersAgence();
            FacesContext.getCurrentInstance().getExternalContext().redirect("/" + Constantes.ADRESSE + "/");
            HttpSession session = ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true));
            session.invalidate();
            SessionManager.removeSession(session.getId());
        } catch (IOException ex) {
            Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void redirectToWelcome() {
        try {
            if (USE_PRETTY) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + Constantes.ADRESSE + "/Welcome");
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + Constantes.ADRESSE + "/lymytz/pages/welcome.xhtml");
            }
            Navigations service = (Navigations) giveManagedBean(Navigations.class);
            if (service != null) {
                service.naviguationApps(" ", " ", "welcome", true);
            }
        } catch (IOException ex) {
            Logger.getLogger(Initialisation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int sessionActive;
//    private List<UserConnect> usersConnect = new ArrayList<>();
//

    public int getSessionActive() {
        return sessionActive;
    }

    public void setSessionActive(int sessionActive) {
        this.sessionActive = sessionActive;
    }

    public void saveDataNavigation(String pageVisite) {
        HttpServletRequest req = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
        String id = ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).getId();
        saveOrUpdateNewUser(currentUser, adresseMac, req.getRemoteAddr(), pageVisite, id);
    }

    //supprime les connection de plus de 5min
    private void deleteOldConnection() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, -5);
        String req = "delete from yvs_connection where date_connexion < ?";
        dao.requeteLibre(req, new Options[]{new Options(c.getTime(), 1)});
//        List<YvsConnection> l = dao.loadNameQueries("YvsConnection.findAll", new String[]{"societe"}, new Object[]{currentUser.getAgence().getSociete()});
//        for (YvsConnection c : l) {
//            if (c.getIdSession() != null ? c.getIdSession().trim().length() > 0 : false) {
//                if (!SessionManager.isActive(c.getIdSession())) {
//                    dao.delete(c);
//                }
//            } else {
//                dao.delete(c);
//            }
//        }
    }

    private void saveOrUpdateNewUser(YvsUsersAgence user, String adresseMac, String adresseIp, String page, String idSession) {
        try {
            YvsConnection cn = (YvsConnection) dao.loadOneByNameQueries("YvsConnection.findByAuthorAdresse", new String[]{"adresse", "author"}, new Object[]{localhost(adresseIp), user});
            if (cn == null) {
                cn = new YvsConnection();
                cn.setAdresseIp(localhost(adresseIp));
                if ((currentUser != null) ? currentUser.getId() > 0 : false) {
                    cn.setAuthor(currentUser);
                } else {
                    cn.setAuthor(null);
                }
                cn.setDateConnexion(new Date());
                cn.setDebutNavigation(new Date());
                cn.setUsers(user.getUsers());
                cn.setAgence(user.getAgence());
                cn.setIdSession(idSession);
                cn.setAdresseMac(adresseMac);
                cn.setDateSave(new Date());
                cn.setDateUpdate(new Date());
                if (user.getUsers() != null) {
                    if ((user.getUsers().getId() != null) ? user.getUsers().getId() > 0 : false) {
                        dao.save(cn);
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                cn.setDateConnexion(new Date());
                cn.setIdSession(idSession);
                dao.update(cn);
            }
            if (page != null ? page.trim().length() > 0 : false) {
                YvsConnectionPages cp = new YvsConnectionPages();
                cp.setAuteurPage(cn);
                cp.setAuthor(currentUser != null ? currentUser.getId() > 0 ? currentUser : null : null);
                cp.setDateOuverture(new Date());
                cp.setTitrePage(page);
                dao.save(cp);
            }
        } catch (Exception ex) {
            Logger.getLogger(Initialisation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public long countUsersConnect() {
        Long re = (Long) dao.loadObjectByNameQueries("YvsConnection.findNBUser", new String[]{}, new Object[]{});
        return (re != null) ? (re > 0) ? re - 1 : re : 0;
    }

    public void onOpenConnected() {
        if (accesPage.isBase_user_()) {
            ManagedUser s = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (s != null) {
                s.loadConnected(true);
                Navigations n = (Navigations) giveManagedBean(Navigations.class);
                if (n != null) {
                    n.naviguationView("Gestion des utilisateurs", "modDonneBase", "smenUser", true);
                }
            }
        }
    }

    public Object giveManagedBean(Class classe) {
        String st = classe.getSimpleName();
        String f = st.substring(0, 1);
        if (st != null) {
            return (Object) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(f.toLowerCase().concat(st.substring(1)));
        }
        return null;
    }

    public static String localhost(String ip) {
        if (ip != null ? ip.trim().length() > 0 : false) {
            if (ip.equals("0:0:0:0:0:0:0:1")) {
                return "127.0.0.1";
            }
        }
        return ip;
    }

    public void loadFavorisUsers() {
        FAVORIS = dao.loadNameQueries("YvsUsersFavoris.findByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
    }

    public void initPages(String lastTitre, String lastModule, String lastPage) {
        this.lastPage = lastPage;
        this.lastModule = lastModule;
        this.lastTitre = lastTitre;
    }

    public YvsUsersFavoris getFavoris(String page) {
//        YvsUsersFavoris y = (YvsUsersFavoris) dao.loadOneByNameQueries("YvsUsersFavoris.findByPageUsers", new String[]{"page", "users"}, new Object[]{page, currentUser.getUsers()});
//        if (y != null ? y.getId() > 0 : false) {
//            return y;
//        }
//        // recherche dichotomique sur la liste des favoris
//        int idx=!FAVORIS.isEmpty()?((int)FAVORIS.size()/2):0;
//        if(idx<=1){
//        
//        }
//        return null;
        return UtilitaireGenerique.findInListFavoris(FAVORIS, page);
    }

    public boolean inToFavoris(String page) {
        YvsUsersFavoris y = UtilitaireGenerique.findInListFavoris(FAVORIS, page);
        if (y != null ? y.getId() > 0 : false) {
            return true;
        }
        return false;
    }

    public void addFavoris(String title, String module, String page) {
        YvsUsersFavoris y = getFavoris(page);
        if (y != null ? y.getId() < 1 : true) {
            y = new YvsUsersFavoris();
            y.setTitre(title);
            y.setModule(module);
            y.setPage(page);
            y.setAuthor(currentUser);
            y.setUsers(currentUser.getUsers());
            y = (YvsUsersFavoris) dao.save1(y);
        }
        FAVORIS.add(y);
    }

    public void removeFavoris(String title, String module, String page) {
        YvsUsersFavoris y = getFavoris(page);
        if (y != null ? y.getId() > 0 : false) {
            dao.delete(y);
            FAVORIS.remove(y);
        }
    }

    public void removeFavoris(YvsUsersFavoris y) {
        if (y != null ? y.getId() > 0 : false) {
            dao.delete(y);
        }
        FAVORIS.remove(y);
        RequestContext.getCurrentInstance().update(":main_pincipal:action_favoris");
    }

    public void loadOnFavoris(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsUsersFavoris y = (YvsUsersFavoris) ev.getObject();
            Navigations w = (Navigations) giveManagedBean(Navigations.class);
            if (w != null) {
                w.naviguationView(y.getTitre(), y.getModule(), y.getPage(), true);
            }
        }
    }

    private boolean initSession = false;

    public boolean isInitSession() {
        return initSession;
    }

    public void setInitSession(boolean initSession) {
        this.initSession = initSession;
    }

    public void openInit() {
        if (!initSession) {
            loadAcces();    //charge les accès si l'utilisatur est connecté
            loadInfoDeBase(agence.getId());//persiste les données de base dans la session ouverte
            initSession = true;
        }
    }

    public void createNewWay() {
        if (passwordAdmin != null ? passwordAdmin.trim().length() < 1 : true) {
            getMessage1("Vous devez entrer le mot de passe");
            return;
        }
        if (!passwordAdmin.equals(Constantes.PASSWORD())) {
            getMessage1("Mot de passe incorrect");
            return;
        }
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/" + Constantes.ADRESSE + "/Initialisation");
        } catch (IOException ex) {
            Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
