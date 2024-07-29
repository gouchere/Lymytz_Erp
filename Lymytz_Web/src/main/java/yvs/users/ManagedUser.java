/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.CodeAcces;
import yvs.commercial.CategoriePerso;
import yvs.commercial.ManagedCategoriePerso;
import yvs.commercial.UtilCom;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseCodeAcces;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseParametre;
import yvs.entity.commercial.YvsComCategoriePersonnel;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.workflow.YvsWarningModelDoc;
import yvs.entity.param.workflow.YvsWorkflowModelDoc;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsBaseUsersAcces;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsNiveauUsers;
import yvs.entity.users.YvsUserViewAlertes;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.entity.users.YvsUsersValidite;
import yvs.etats.Dashboards;
import yvs.init.Initialisation;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.parametrage.societe.Societe;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.agence.ManagedAgence;
import yvs.parametrage.agence.UtilAgence;
import yvs.parametrage.societe.ManagedSociete;
import yvs.parametrage.societe.UtilSte;
import yvs.users.messagerie.GroupeMessage;
import yvs.util.Managed;
import yvs.util.MdpUtil;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class ManagedUser extends Managed<Users, YvsUsers> implements Serializable {

    @ManagedProperty(value = "#{users}")
    private Users users;
    private Users usersTemp = new Users();
    private Agence agence = new Agence();
    private Societe societe = new Societe();
    private List<YvsUsers> listUser, listSelectUser, listAllUser;
    private YvsUsers userSelect = new YvsUsers();
    private String[] chec;
    private MdpUtil hashMdp = new MdpUtil();
    private boolean vueListe = true, updateUser, selectUser, optionUpdate, errorConfirm, updatePwd, updateRation, superAdmin;
    private String fusionneTo, tabIds;
    private List<String> fusionnesBy;

    private Boolean actifSearch, connectSearch, planifierSearch;
    private long agenceSearch = -1, societeSearch = -1, groupeSearch, niveauSearch;

    private String oldCodeRation, newCodeRation, rembCodeRation;
    private String rembPwdUser, oldPwdUser, newPwdUser, etat;
    private String numSearch;

    public static String defaultPwdUser = "ADMIN";
    private String accesSearch;
    private CodeAcces code = new CodeAcces();
    public PaginatorResult<YvsBaseCodeAcces> p_acces = new PaginatorResult<>();
    private List<YvsBaseUsersAcces> listAcces;
    private List<YvsNiveauAcces> listNiveaux;
    private YvsBaseUsersAcces selectAcces = new YvsBaseUsersAcces();
    private String descriptionAcces, codeAcces;

    // Données des vente
    private long nbreVenteAnnule, nbreVenteAttence, nbreVenteEnCours, nbreVenteValide,
            nbreVenteLivre, nbreVenteNotLivre, nbreVenteEnCoursLivre, nbreVenteRetardLivr,
            nbreVenteRegle, nbreVenteEnCoursRegle, nbreVenteNotRegle, nbreVente, nbreVenteAvoir;
    private double caVenteAnnule, caVenteAttence, caVenteEnCours, caVenteValide, caVenteValideCS, caVenteValideSS,
            caVenteLivre, caVenteEnCoursLivre, caVenteNotLivre, caVenteRetardLivr,
            caVenteRegle, caVenteEnCoursRegle, caVenteNotRegle, caVente, caVenteAvoir;
    private double versementAttendu;
    private Date dateDebutVente = new Date(), dateFinVente = new Date();
    private Dashboards tabArticleVendeur = new Dashboards();
    private String periode;

    private List<YvsBaseModeReglement> models;
    private List<YvsUserViewAlertes> documents;
    private List<YvsUsersAgence> agences;

    YvsBaseParametre currentParam = new YvsBaseParametre();

    public ManagedUser() {
        listAllUser = new ArrayList<>();
        listUser = new ArrayList<>();
        listSelectUser = new ArrayList<>();
        listAcces = new ArrayList<>();
        models = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
        documents = new ArrayList<>();
        listNiveaux = new ArrayList<>();
        agences = new ArrayList<>();
    }

    public List<YvsUsersAgence> getAgences() {
        return agences;
    }

    public void setAgences(List<YvsUsersAgence> agences) {
        this.agences = agences;
    }

    public long getNiveauSearch() {
        return niveauSearch;
    }

    public void setNiveauSearch(long niveauSearch) {
        this.niveauSearch = niveauSearch;
    }

    public List<YvsNiveauAcces> getListNiveaux() {
        return listNiveaux;
    }

    public void setListNiveaux(List<YvsNiveauAcces> listNiveaux) {
        this.listNiveaux = listNiveaux;
    }

    public String getFusionneTo() {
        return fusionneTo;
    }

    public void setFusionneTo(String fusionneTo) {
        this.fusionneTo = fusionneTo;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public List<String> getFusionnesBy() {
        return fusionnesBy;
    }

    public void setFusionnesBy(List<String> fusionnesBy) {
        this.fusionnesBy = fusionnesBy;
    }

    public Boolean getPlanifierSearch() {
        return planifierSearch;
    }

    public void setPlanifierSearch(Boolean planifierSearch) {
        this.planifierSearch = planifierSearch;
    }

    public long getGroupeSearch() {
        return groupeSearch;
    }

    public void setGroupeSearch(long groupeSearch) {
        this.groupeSearch = groupeSearch;
    }

    public double getVersementAttendu() {
        return versementAttendu;
    }

    public void setVersementAttendu(double versementAttendu) {
        this.versementAttendu = versementAttendu;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public Dashboards getTabArticleVendeur() {
        return tabArticleVendeur;
    }

    public void setTabArticleVendeur(Dashboards tabArticleVendeur) {
        this.tabArticleVendeur = tabArticleVendeur;
    }

    public List<YvsBaseModeReglement> getModels() {
        return models;
    }

    public void setModels(List<YvsBaseModeReglement> models) {
        this.models = models;
    }

    public long getNbreVenteAnnule() {
        return nbreVenteAnnule;
    }

    public void setNbreVenteAnnule(long nbreVenteAnnule) {
        this.nbreVenteAnnule = nbreVenteAnnule;
    }

    public long getNbreVenteAttence() {
        return nbreVenteAttence;
    }

    public void setNbreVenteAttence(long nbreVenteAttence) {
        this.nbreVenteAttence = nbreVenteAttence;
    }

    public long getNbreVenteEnCours() {
        return nbreVenteEnCours;
    }

    public void setNbreVenteEnCours(long nbreVenteEnCours) {
        this.nbreVenteEnCours = nbreVenteEnCours;
    }

    public long getNbreVenteValide() {
        return nbreVenteValide;
    }

    public void setNbreVenteValide(long nbreVenteValide) {
        this.nbreVenteValide = nbreVenteValide;
    }

    public long getNbreVenteLivre() {
        return nbreVenteLivre;
    }

    public void setNbreVenteLivre(long nbreVenteLivre) {
        this.nbreVenteLivre = nbreVenteLivre;
    }

    public long getNbreVenteNotLivre() {
        return nbreVenteNotLivre;
    }

    public void setNbreVenteNotLivre(long nbreVenteNotLivre) {
        this.nbreVenteNotLivre = nbreVenteNotLivre;
    }

    public long getNbreVenteEnCoursLivre() {
        return nbreVenteEnCoursLivre;
    }

    public void setNbreVenteEnCoursLivre(long nbreVenteEnCoursLivre) {
        this.nbreVenteEnCoursLivre = nbreVenteEnCoursLivre;
    }

    public long getNbreVenteRetardLivr() {
        return nbreVenteRetardLivr;
    }

    public void setNbreVenteRetardLivr(long nbreVenteRetardLivr) {
        this.nbreVenteRetardLivr = nbreVenteRetardLivr;
    }

    public long getNbreVenteRegle() {
        return nbreVenteRegle;
    }

    public void setNbreVenteRegle(long nbreVenteRegle) {
        this.nbreVenteRegle = nbreVenteRegle;
    }

    public long getNbreVenteEnCoursRegle() {
        return nbreVenteEnCoursRegle;
    }

    public void setNbreVenteEnCoursRegle(long nbreVenteEnCoursRegle) {
        this.nbreVenteEnCoursRegle = nbreVenteEnCoursRegle;
    }

    public long getNbreVenteNotRegle() {
        return nbreVenteNotRegle;
    }

    public void setNbreVenteNotRegle(long nbreVenteNotRegle) {
        this.nbreVenteNotRegle = nbreVenteNotRegle;
    }

    public long getNbreVente() {
        return nbreVente;
    }

    public void setNbreVente(long nbreVente) {
        this.nbreVente = nbreVente;
    }

    public long getNbreVenteAvoir() {
        return nbreVenteAvoir;
    }

    public void setNbreVenteAvoir(long nbreVenteAvoir) {
        this.nbreVenteAvoir = nbreVenteAvoir;
    }

    public double getCaVenteAnnule() {
        return caVenteAnnule;
    }

    public void setCaVenteAnnule(double caVenteAnnule) {
        this.caVenteAnnule = caVenteAnnule;
    }

    public double getCaVenteAttence() {
        return caVenteAttence;
    }

    public void setCaVenteAttence(double caVenteAttence) {
        this.caVenteAttence = caVenteAttence;
    }

    public double getCaVenteEnCours() {
        return caVenteEnCours;
    }

    public void setCaVenteEnCours(double caVenteEnCours) {
        this.caVenteEnCours = caVenteEnCours;
    }

    public double getCaVenteValide() {
        return caVenteValide;
    }

    public void setCaVenteValide(double caVenteValide) {
        this.caVenteValide = caVenteValide;
    }

    public double getCaVenteValideCS() {
        return caVenteValideCS;
    }

    public void setCaVenteValideCS(double caVenteValideCS) {
        this.caVenteValideCS = caVenteValideCS;
    }

    public double getCaVenteValideSS() {
        return caVenteValideSS;
    }

    public void setCaVenteValideSS(double caVenteValideSS) {
        this.caVenteValideSS = caVenteValideSS;
    }

    public double getCaVenteLivre() {
        return caVenteLivre;
    }

    public void setCaVenteLivre(double caVenteLivre) {
        this.caVenteLivre = caVenteLivre;
    }

    public double getCaVenteEnCoursLivre() {
        return caVenteEnCoursLivre;
    }

    public void setCaVenteEnCoursLivre(double caVenteEnCoursLivre) {
        this.caVenteEnCoursLivre = caVenteEnCoursLivre;
    }

    public double getCaVenteNotLivre() {
        return caVenteNotLivre;
    }

    public void setCaVenteNotLivre(double caVenteNotLivre) {
        this.caVenteNotLivre = caVenteNotLivre;
    }

    public double getCaVenteRetardLivr() {
        return caVenteRetardLivr;
    }

    public void setCaVenteRetardLivr(double caVenteRetardLivr) {
        this.caVenteRetardLivr = caVenteRetardLivr;
    }

    public double getCaVenteRegle() {
        return caVenteRegle;
    }

    public void setCaVenteRegle(double caVenteRegle) {
        this.caVenteRegle = caVenteRegle;
    }

    public double getCaVenteEnCoursRegle() {
        return caVenteEnCoursRegle;
    }

    public void setCaVenteEnCoursRegle(double caVenteEnCoursRegle) {
        this.caVenteEnCoursRegle = caVenteEnCoursRegle;
    }

    public double getCaVenteNotRegle() {
        return caVenteNotRegle;
    }

    public void setCaVenteNotRegle(double caVenteNotRegle) {
        this.caVenteNotRegle = caVenteNotRegle;
    }

    public double getCaVente() {
        return caVente;
    }

    public void setCaVente(double caVente) {
        this.caVente = caVente;
    }

    public double getCaVenteAvoir() {
        return caVenteAvoir;
    }

    public void setCaVenteAvoir(double caVenteAvoir) {
        this.caVenteAvoir = caVenteAvoir;
    }

    public Date getDateDebutVente() {
        return dateDebutVente;
    }

    public void setDateDebutVente(Date dateDebutVente) {
        this.dateDebutVente = dateDebutVente;
    }

    public Date getDateFinVente() {
        return dateFinVente;
    }

    public void setDateFinVente(Date dateFinVente) {
        this.dateFinVente = dateFinVente;
    }

    public String getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(String codeAcces) {
        this.codeAcces = codeAcces;
    }

    public String getDescriptionAcces() {
        return descriptionAcces;
    }

    public void setDescriptionAcces(String descriptionAcces) {
        this.descriptionAcces = descriptionAcces;
    }

    public YvsBaseUsersAcces getSelectAcces() {
        return selectAcces;
    }

    public List<YvsUserViewAlertes> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsUserViewAlertes> documents) {
        this.documents = documents;
    }

    public void setSelectAcces(YvsBaseUsersAcces selectAcces) {
        this.selectAcces = selectAcces;
        if (selectAcces != null) {
            setDescriptionAcces(selectAcces.getCode().getDescription());
            setCodeAcces(selectAcces.getCode().getCode());
        } else {
            setDescriptionAcces("");
            setCodeAcces("");
        }
    }

    public String getAccesSearch() {
        return accesSearch;
    }

    public void setAccesSearch(String accesSearch) {
        this.accesSearch = accesSearch;
    }

    public List<YvsBaseUsersAcces> getListAcces() {
        return listAcces;
    }

    public void setListAcces(List<YvsBaseUsersAcces> listAcces) {
        this.listAcces = listAcces;
    }

    public PaginatorResult<YvsBaseCodeAcces> getP_acces() {
        return p_acces;
    }

    public void setP_acces(PaginatorResult<YvsBaseCodeAcces> p_acces) {
        this.p_acces = p_acces;
    }

    public List<YvsUsers> getListAllUser() {
        return listAllUser;
    }

    public void setListAllUser(List<YvsUsers> listAllUser) {
        this.listAllUser = listAllUser;
    }

    public Boolean getConnectSearch() {
        return connectSearch;
    }

    public void setConnectSearch(Boolean connectSearch) {
        this.connectSearch = connectSearch;
    }

    public long getAgenceSearch() {
        return agenceSearch;
    }

    public void setAgenceSearch(long agenceSearch) {
        this.agenceSearch = agenceSearch;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public String getNewPwdUser() {
        return newPwdUser;
    }

    public Societe getSociete() {
        return societe;
    }

    public YvsUsers getUserSelect() {
        return userSelect;
    }

    public void setUserSelect(YvsUsers userSelect) {
        this.userSelect = userSelect;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public void setNewPwdUser(String newPwdUser) {
        this.newPwdUser = newPwdUser;
    }

    public String getOldCodeRation() {
        return oldCodeRation;
    }

    public void setOldCodeRation(String oldCodeRation) {
        this.oldCodeRation = oldCodeRation;
    }

    public String getNewCodeRation() {
        return newCodeRation;
    }

    public void setNewCodeRation(String newCodeRation) {
        this.newCodeRation = newCodeRation;
    }

    public String getRembCodeRation() {
        return rembCodeRation;
    }

    public void setRembCodeRation(String rembCodeRation) {
        this.rembCodeRation = rembCodeRation;
    }

    public CodeAcces getCode() {
        return code;
    }

    public void setCode(CodeAcces code) {
        this.code = code;
    }

    public boolean isUpdateRation() {
        return updateRation;
    }

    public void setUpdateRation(boolean updateRation) {
        this.updateRation = updateRation;
    }

    public boolean isUpdatePwd() {
        return updatePwd;
    }

    public void setUpdatePwd(boolean updatePwd) {
        this.updatePwd = updatePwd;
    }

    public String getOldPwdUser() {
        return oldPwdUser;
    }

    public void setOldPwdUser(String oldPwdUser) {
        this.oldPwdUser = oldPwdUser;
    }

    public void setRembPwdUser(String rembPwdUser) {
        this.rembPwdUser = rembPwdUser;
    }

    public String getRembPwdUser() {
        return rembPwdUser;
    }

    public void setUsersTemp(Users usersTemp) {
        this.usersTemp = usersTemp;
    }

    public Users getUsersTemp() {
        return usersTemp;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setErrorConfirm(boolean errorConfirm) {
        this.errorConfirm = errorConfirm;
    }

    public boolean isErrorConfirm() {
        return errorConfirm;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public Users getUsers() {
        return users;
    }

    public String getDefaultPwdUser() {
        return defaultPwdUser;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public List<YvsUsers> getListUser() {
        return listUser;
    }

    public void setListUser(List<YvsUsers> listUser) {
        this.listUser = listUser;
    }

    public List<YvsUsers> getListSelectUser() {
        return listSelectUser;
    }

    public void setListSelectUser(List<YvsUsers> listSelectUser) {
        this.listSelectUser = listSelectUser;
    }

    public String[] getChec() {
        return chec;
    }

    public void setChec(String[] chec) {
        this.chec = chec;
    }

    public MdpUtil getHashMdp() {
        return hashMdp;
    }

    public void setHashMdp(MdpUtil hashMdp) {
        this.hashMdp = hashMdp;
    }

    public boolean isVueListe() {
        return vueListe;
    }

    public void setVueListe(boolean vueListe) {
        this.vueListe = vueListe;
    }

    public boolean isUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(boolean updateUser) {
        this.updateUser = updateUser;
    }

    public boolean isSelectUser() {
        return selectUser;
    }

    public void setSelectUser(boolean selectUser) {
        this.selectUser = selectUser;
    }

    public boolean isOptionUpdate() {
        return optionUpdate;
    }

    public void setOptionUpdate(boolean optionUpdate) {
        this.optionUpdate = optionUpdate;
    }

    public long getSocieteSearch() {
        return societeSearch;
    }

    public void setSocieteSearch(long societeSearch) {
        this.societeSearch = societeSearch;
    }

    @Override
    public void doNothing() {

    }

    public YvsUsers buildUsers(Users e) {
        YvsUsers b = new YvsUsers();
        if (e != null) {
            b.setId(e.getId());
            b.setAccesMultiAgence(e.isAccesMultiAgence());
            b.setAccesMultiSociete(e.isAccesMultiSociete());
            b.setActif(e.isActif());
            b.setNomUsers(e.getNomUsers());
            b.setAleaMdp(hashMdp.randomString(15));
            b.setPasswordUser(hashMdp.hashString(e.getCodeUsers() + "" + b.getAleaMdp() + "" + e.getPasswordUser()));
            b.setCodeUsers(e.getCodeUsers());
            b.setConnecte(e.isConnecte());
            b.setPhoto(e.getPhoto());
            b.setCivilite(e.getCivilite());
            b.setVenteOnline(e.isVenteOnline());
            b.setConnectOnlinePlanning(e.isConnectOnlinePlanning());
            b.setSupp(e.isSupp());
            if ((e.getAgence() != null) ? e.getAgence().getId() != 0 : false) {
                b.setAgence(new YvsAgences(e.getAgence().getId(), e.getAgence().getDesignation()));
            }
            if ((e.getCategorie() != null) ? e.getCategorie().getId() != 0 : false) {
                b.setCategorie(new YvsComCategoriePersonnel(e.getCategorie().getId(), e.getCategorie().getLibelle()));
            }
            if ((e.getNiveauAcces() != null) ? e.getNiveauAcces().getId() > 0 : false) {
                ManagedNiveauAcces w = (ManagedNiveauAcces) giveManagedBean(ManagedNiveauAcces.class);
                if (w != null) {
                    b.setNiveauAcces(w.getListNiveauAcces().get(w.getListNiveauAcces().indexOf(new YvsNiveauAcces(e.getNiveauAcces().getId()))));
                }
            }
            b.setAuthor(currentUser);
        }
        return b;
    }

    @Override
    public boolean controleFiche(Users bean) {
        if (bean.getCodeUsers() == null || "".equals(bean.getCodeUsers())) {
            getErrorMessage("Vous devez entrer le code");
            return false;
        }
        if (bean.getNomUsers() == null || "".equals(bean.getNomUsers())) {
            getErrorMessage("Vous devez entrer le nom");
            return false;
        }
        if (bean.getNiveauAcces().getId() == 0) {
            getErrorMessage("Vous devez specifier le niveau d'acces");
            return false;
        }
        if (bean.isTemporaire() && bean.getDateExpiration() == null) {
            getErrorMessage("Vous devez indiquer la date d'expiration");
            return false;
        }
        YvsUsers y = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findByCode", new String[]{"codeUsers"}, new Object[]{bean.getCodeUsers()});
        if (y != null ? y.getId() > 0 : false) {
            if (!y.getId().equals(bean.getId())) {
                getErrorMessage("Vous ne pouvez pas utiliser ce code", "il est déja utilisé par un autre utilisateur");
                return false;
            }
            if (y.getActif() != bean.isActif() && !autoriser("base_user_active")) {
                openNotAcces();
                return false;
            }
        }
        return true;
    }

    public boolean controleFicheProfil(Users bean) {
        if (bean.getCodeUsers() == null || "".equals(bean.getCodeUsers())) {
            getErrorMessage("Vous devez entrer le code");
            return false;
        }
        if (bean.getNomUsers() == null || "".equals(bean.getNomUsers())) {
            getErrorMessage("Vous devez entrer le nom");
            return false;
        }
        if (updatePwd) {
            if (oldPwdUser == null || oldPwdUser.equals("")) {
                getErrorMessage("Vous devez entrer l'ancien mot de passe");
                return false;
            }
            if (newPwdUser == null || newPwdUser.equals("")) {
                getErrorMessage("Vous devez entrer le nouveau mot de passe");
                return false;
            }
            if (rembPwdUser == null || rembPwdUser.equals("")) {
                getErrorMessage("Vous devez retaper le mot de passe");
                return false;
            }
            if (!rembPwdUser.trim().equals(newPwdUser.trim())) {
                getErrorMessage("Vous devez vous assurer que les deux mot de passe soient identique!");
                return false;
            }
            if (!hashMdp.correct(newPwdUser, (currentParam != null ? currentParam.getTaillePassword() : 6))) {
                getErrorMessage(hashMdp.ERROR);
                return false;
            }
        }
        if (updateRation) {
            if (newCodeRation == null || newCodeRation.equals("")) {
                getErrorMessage("Vous devez entrer le nouveau code de ration");
                return false;
            }
            if (rembCodeRation == null || rembCodeRation.equals("")) {
                getErrorMessage("Vous devez retaper le code de ration");
                return false;
            }
            if (!rembCodeRation.trim().equals(rembCodeRation.trim())) {
                getErrorMessage("Vous devez vous assurer que les deux code de ration soient identique!");
                return false;
            }
        }
        return true;
    }

    @Override
    public void deleteBean() {
        if (!autoriser("base_user_del_")) {
            openNotAcces();
            return;
        }
        System.err.println("tabIds = " + tabIds);
        if (tabIds != null) {
            List<Long> l = decomposeIdSelection(tabIds);
            List<YvsUsers> list = new ArrayList<>();
            try {
                YvsUsers y;
                for (Long ids : l) {
                    y = listUser.get(ids.intValue());
                    list.add(y);
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.delete(y);
                    if (y.getId() == users.getId()) {
                        resetFiche();
                    }
                }
                succes();
                listUser.removeAll(list);
                update("tab_user_00");
                update("input_hide_user");
            } catch (Exception ex) {
                getErrorMessage("Suppression Impossible ! Cet élement est déjà utiliser ");
                getMessage("Impossible de terminer cette opération ! des élément de votre sélection doivent être encore en liaison", FacesMessage.SEVERITY_ERROR);
            }

            tabIds = "";
        }
    }

    @Override
    public void updateBean() {
        setSelectUser(false);
        setVueListe(false);
        usersTemp = recopieView();
        users.setPasswordUser(null);
        update("body_user_00");
        update("entete_user_00");
    }

    @Override
    public Users recopieView() {
        users.setConnecte(false);
        users.setSupp(false);
        return users;
    }

    public Users recopieView(Users bean) {
        Users u = new Users(bean.getId());
        u.setId(bean.getId());
        u.setNomUsers(bean.getNomUsers());
        u.setPasswordUser(bean.getPasswordUser());
        u.setAleaMdp(bean.getAleaMdp());
        u.setCodeUsers(bean.getCodeUsers());
        u.setPhoto(bean.getPhoto());
        return u;
    }

    @Override
    public void populateView(Users bean) {
        cloneObject(users, bean);
    }

    @Override
    public void onSelectObject(YvsUsers bean) {
        populateView(UtilUsers.buildBeanUsers(bean));
        chooseSocietes();
        users.setNiveauAcces(UtilUsers.buildBeanNiveauAcces(findNiveauAcces(bean)));
        updateUser = true;
        userSelect = bean;
        champ = new String[]{"vendeur"};
        val = new Object[]{bean};
        nameQueri = "YvsComCommercialVente.findFactureImpayeByVendeur";
        users.setFactures(dao.loadNameQueries(nameQueri, champ, val));

        Double credit = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findTTCByVendeur", champ, val);
        Double debit = (Double) dao.loadObjectByNameQueries("YvsComptaCaissePieceVente.findSumByVendeur", champ, val);
        users.setCredit(credit != null ? credit : 0);
        users.setDebit(debit != null ? debit : 0);

        users.setSolde(users.getDebit() - users.getCredit());
        users.setSoldes(dao.loadNameQueries("YvsBaseExercice.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()}));
        champ = new String[]{"vendeur", "dateDebut", "dateFin"};
        for (YvsBaseExercice e : users.getSoldes()) {
            val = new Object[]{bean, e.getDateDebut(), e.getDateFin()};
            credit = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findTTCByVendeurDates", champ, val);
            debit = (Double) dao.loadObjectByNameQueries("YvsComptaCaissePieceVente.findSumByVendeurDatesVente", champ, val);
            e.setValeur((debit != null ? debit : 0) - (credit != null ? credit : 0));
            if (credit != null ? credit > 0 : false) {
                users.setAfficheSolde(true);
            }
        }
        loadModelDoc();
        loadAgence();
        update("data_alert_users");
        update("form_user_01");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            execute("collapseForm('user')");
            YvsUsers bean = (YvsUsers) ev.getObject();
            onSelectObject(bean);
            execute("onselectLine(" + tabIds + ",'user')");
//            execute("oncollapsesForm('user')");
            tabIds = listUser.indexOf(bean) + "";

        }
    }

    public void activeAlertUserDocAll(boolean active) {
        YvsUserViewAlertes ua;
        for (int index = 0; index < documents.size(); index++) {
            ua = documents.get(index);
            long current = ua.getId();
            ua.setActif(active);
            ua.setAuthor(currentUser);
            ua.setDateSave(new Date());
            ua.setDateUpdate(new Date());
            ua.setUsers(userSelect);
            if (current < 1) {
                ua.setId(null);
                ua = (YvsUserViewAlertes) dao.save1(ua);
            } else {
                dao.update(ua);
            }
            documents.set(index, ua);
        }
    }

    public void activeAlertUserDoc(YvsUserViewAlertes ua) {
        try {
            if (ua != null ? (ua.getDocumentType() != null ? ua.getDocumentType().getId() > 0 : false) : false) {
                long current = ua.getId();
                ua.setActif(!ua.getActif());
                ua.setAuthor(currentUser);
                ua.setDateSave(new Date());
                ua.setDateUpdate(new Date());
                ua.setUsers(userSelect);
                if (current < 1) {
                    ua.setId(null);
                    ua = (YvsUserViewAlertes) dao.save1(ua);
                } else {
                    dao.update(ua);
                }
                int index = documents.indexOf(ua);
                if (index > -1) {
                    documents.set(index, ua);
                }
                if (ua.getActif()) {
                    YvsWarningModelDoc y = (YvsWarningModelDoc) dao.loadOneByNameQueries("YvsWarningModelDoc.findOne", new String[]{"model", "societe"}, new Object[]{ua.getDocumentType(), currentAgence.getSociete()});
                    if (y != null ? y.getId() < 1 : true) {
                        y = new YvsWarningModelDoc();
                        y.setAuthor(currentUser);
                        y.setEcart(30);
                        y.setModel(ua.getDocumentType());
                        y.setSociete(currentAgence.getSociete());
                        y.setId(null);
                        dao.save(y);
                    }
                }
            } else {
                getErrorMessage("Erreur lors de l'enregistrement !");
            }
        } catch (Exception ex) {
            getException("activeAlertUserDoc", ex);
        }
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
                ua.setUsers(userSelect);
                if (current < 1) {
                    ua.setId(null);
                    ua = (YvsUsersAgence) dao.save1(ua);
                } else {
                    dao.update(ua);
                }
                int index = agences.indexOf(ua);
                if (index > -1) {
                    agences.set(index, ua);
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
                ua.setUsers(userSelect);
                if (current < 1) {
                    ua.setId(null);
                    ua = (YvsUsersAgence) dao.save1(ua);
                } else {
                    dao.update(ua);
                }
                int index = agences.indexOf(ua);
                if (index > -1) {
                    agences.set(index, ua);
                }
            } else {
                getErrorMessage("Erreur lors de l'enregistrement !");
            }
        } catch (Exception ex) {
            getException("autoriseActionAgence", ex);
        }
    }

    private YvsNiveauAcces findNiveauAcces(YvsUsers u) {
        if (u.getNiveauxAcces() != null) {
            for (YvsNiveauUsers nu : u.getNiveauxAcces()) {
                if (nu.getIdNiveau() != null ? nu.getIdNiveau().getSociete() != null ? nu.getIdNiveau().getSociete().equals(currentAgence.getSociete()) : false : false) {
                    return nu.getIdNiveau();
                }
            }
        }
        return null;
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        if (ev != null) {
            Users bean = (Users) ev.getObject();
//            selectUser(bean);
        }
    }

    @Override
    public void loadAll() {
        loadAll(true, true);
        setSuperAdmin(currentUser.getId() == 0);
        if (users.getAgence() != null ? users.getAgence().getId() < 1 : true) {
//            users.set(new Societe(currentAgence.getSociete().getId()));
            chooseAgence();
        }
        initView();
        users.getDossiers().add(new GroupeMessage());
        users.getDossiers().add(new GroupeMessage());
        users.getDossiers().add(new GroupeMessage());
        users.getDossiers().add(new GroupeMessage());
        users.getDossiers().add(new GroupeMessage());
    }

    public void initView() {
        if (currentParam == null) {
            currentParam = (YvsBaseParametre) dao.loadOneByNameQueries("YvsBaseParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
        if (currentParam != null) {
            defaultPwdUser = currentParam.getDefautPassword();
        }
        if (societeSearch < 0) {
            societeSearch = currentAgence.getSociete().getId();
            paginator.addParam(new ParametreRequete("y.agence.societe", "societe", new YvsSocietes(societeSearch), "=", "AND"));
            ManagedAgence service = (ManagedAgence) giveManagedBean(ManagedAgence.class);
            if (service != null) {
                service.loadAllUsable();
            }
        }
        if (agenceSearch < 0) {
            agenceSearch = currentAgence.getId();
            paginator.addParam(new ParametreRequete("y.agence", "agence", new YvsAgences(agenceSearch), "=", "AND"));
        }
    }

    private long ids = -10000;

    public void loadModelDoc() {
        String query = "SELECT m.id,m.titre_doc, m.name_table, m.defined_livraison, m.defined_update, m.defined_reglement, m.description,a.id, a.actif "
                + "FROM yvs_workflow_model_doc m LEFT JOIN  yvs_user_view_alertes a ON (m.id=a.document_type AND a.users=?) ORDER BY a.id, m.titre_doc";
        YvsUserViewAlertes va;
        YvsWorkflowModelDoc doc;
        List<Object[]> result = dao.loadListBySqlQuery(query, new Options[]{new Options(users.getId(), 1)});
        documents.clear();
        for (Object[] line : result) {
            va = new YvsUserViewAlertes();
            doc = new YvsWorkflowModelDoc();
            doc.setId((line[0]) != null ? ((Integer) line[0]) : 0);
            doc.setTitreDoc(line[1] != null ? ((String) line[1]) : null);
            doc.setNameTable(line[2] != null ? ((String) line[2]) : null);
            doc.setDefinedLivraison(line[3] != null ? ((Boolean) line[3]) : false);
            doc.setDefinedUpdate(line[4] != null ? ((Boolean) line[4]) : false);
            doc.setDefinedReglement(line[5] != null ? ((Boolean) line[5]) : false);
            doc.setDescription(line[6] != null ? ((String) line[6]) : null);
            long current = (line[7]) != null ? ((Long) line[7]) : 0;
            va.setId(current != 0 ? current : ids--);
            va.setActif(line[8] != null ? ((Boolean) line[8]) : false);
            va.setDocumentType(doc);
            documents.add(va);
        }
        update("data_alert_users");

    }

    public void loadAgence() {
        String query = "SELECT a.id, a.codeagence, a.designation, ua.id, ua.actif, ua.can_action "
                + "FROM yvs_agences a LEFT JOIN yvs_users_agence ua ON (ua.agence = a.id AND ua.users = ?) WHERE a.societe = ? ORDER BY a.designation";
        YvsUsersAgence ua;
        YvsAgences a;
        List<Object[]> result = dao.loadListBySqlQuery(query, new Options[]{new Options(users.getId(), 1), new Options(currentAgence.getSociete().getId(), 2)});
        agences.clear();
        for (Object[] line : result) {
            ua = new YvsUsersAgence();
            a = new YvsAgences();
            a.setId((line[0]) != null ? ((Long) line[0]) : 0);
            a.setCodeagence((line[1]) != null ? ((String) line[1]) : "");
            a.setDesignation((line[2]) != null ? ((String) line[2]) : "");
            long current = (line[3]) != null ? ((Long) line[3]) : 0;
            ua.setId(current != 0 ? current : YvsUsersAgence.ids--);
            ua.setAgence(a);
            ua.setActif(line[4] != null ? ((Boolean) line[4]) : false);
            ua.setCanAction(line[5] != null ? ((Boolean) line[5]) : false);
            ua.setUsers(new YvsUsers(users.getId()));
            ua.setAuthor(currentUser);
            agences.add(ua);
        }
        update("data_agence_users");
    }

    public void loadAll(boolean avance, boolean init) {
        if (currentAgence.getSociete().getGroupe() != null) {
            paginator.addParam(new ParametreRequete("y.agence.societe.groupe", "groupe", currentAgence.getSociete().getGroupe(), "=", "AND"));
        } else {
            paginator.addParam(new ParametreRequete("y.agence.societe.id", "id", currentAgence.getSociete().getId(), "=", "AND"));
        }
        if (!autoriser("base_view_all_user_groupe")) {
            paginator.addParam(new ParametreRequete("y.agence.societe.groupe", "groupe", null, "=", "AND"));
            paginator.addParam(new ParametreRequete("y.agence.societe.id", "id", currentAgence.getSociete().getId(), "=", "AND"));
        }
        if (!autoriser("base_view_all_user")) {
            paginator.addParam(new ParametreRequete("y.agence.societe.id", "id", null, "=", "AND"));
            paginator.addParam(new ParametreRequete("y.agence", "agence", currentAgence, "=", "AND"));
        }
        listUser = paginator.executeDynamicQuery("YvsUsers", "y.nomUsers, y.codeUsers", avance, init, (int) imax, dao);
        setSuperAdmin(currentUser.getId() == 0);
        if (listUser.size() == 1) {
            onSelectObject(listUser.get(0));
            execute("collapseForm('user')");
        } else {
            execute("collapseList('user')");
        }
        update("tab_user_00");
    }

    public void loadAllBySociete(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        listUser = paginator.executeDynamicQuery("YvsUsers", "y.nomUsers, y.codeUsers", avance, init, (int) imax, dao);
        setSuperAdmin(currentUser.getId() == 0);
        update("tab_user_00");
    }

    public void loadAllUserSociete() {
        //trouve le niveau d'accès des personnes pouvant modifier les hs
        List<Long> lau = dao.loadNameQueries("YvsAutorisationRessourcesPage.findByrefRessourcePage", new String[]{"reference"}, new Object[]{"point_update_hs"});
        if (lau != null ? !lau.isEmpty() : false) {
            listUser = dao.loadNameQueries("YvsNiveauUsers.findUserByNiveaux", new String[]{"niveau"}, new Object[]{lau});
        }
    }

    public void loadAllUserSociete_() {
        //trouve le niveau d'accès des personnes pouvant modifier les hs
        listAllUser = dao.loadNameQueries("YvsUsers.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadActif(Boolean actif) {
        actifSearch = actif;
        addParamActif();
    }

    public void loadConnected(Boolean connecte) {
        paginator.getParams().clear();
        connectSearch = connecte;
        addParamConnected();
    }

    public void loadAgence(Long agence) {
        agenceSearch = agence != null ? (agence > 0 ? agence : currentAgence.getId()) : currentAgence.getId();
        addParamAgence();
    }

    public void loadAllEtat(boolean etat) {
        listUser.clear();
        champ = new String[]{"agence", "actif"};
        val = new Object[]{currentAgence, etat};
        listUser = dao.loadNameQueries("YvsUsers.findBySociete", champ, val);
        setSuperAdmin(currentUser.getId() == 0);
    }

    @Override
    public void resetFiche() {
        resetFiche(users);
        users.setTemporaire(false);
        users.setAgence(new Agence());
        users.setSociete(new Societe());
        users.setNiveauAcces(new NiveauAcces());
        users.setCategorie(new CategoriePerso());
        setUpdateUser(false);
        userSelect = new YvsUsers();
        resetPage();
        update("form_user_01");
    }

    @Override
    public void resetPage() {
//        for (Users u : listUser) {
//            listUser.get(listUser.indexOf(u)).setSelectActif(false);
//        }
//        listSelectUser.clear();
    }

    @Override
    public boolean saveNew() {
        try {
            Users user = recopieView();
            YvsUsers y = saveNew(user);
            if (y != null ? y.getId() > 0 : false) {
                setUpdateUser(true);
                actionOpenOrResetAfter(this);
                succes();
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public YvsUsers saveNew(Users user) {
        try {
            if (controleFiche(user)) {
                user.setPasswordUser(defaultPwdUser);
                YvsUsers y = buildUsers(user);
                boolean succes = false;
                if (user.getId() < 1) {
                    if (!autoriser("base_user_add_")) {
                        openNotAccesAction("Vous ne disposer pas des droits suffisants pour sauvegarder un utilisateur");
                        return null;
                    }
                    y.setDateSave(new Date());
                    y.setDateUpdate(new Date());
                    y.setId(null);
                    y = (YvsUsers) dao.save1(y);
                    users.setId(y.getId());
                    succes = true;
                } else {
                    if (!autoriser("base_user_up_")) {
                        openNotAccesAction("Vous ne disposer pas des droits suffisants pour modifier un utilisateur");
                        return null;
                    }
                    if (!user.isInitPassword()) {
                        YvsUsers u = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{y.getId()});
                        y.setPasswordUser(u.getPasswordUser());
                        y.setAleaMdp(u.getAleaMdp());
                    }
                    y.setDateUpdate(new Date());
                    dao.update(y);
                    succes = true;
                }
                if (succes) {
                    try {
                        YvsUsersValidite validite = (YvsUsersValidite) dao.loadOneByNameQueries("YvsUsersValidite.findAll", new String[]{"users"}, new Object[]{y});
                        if (user.isTemporaire()) {
                            if (validite != null ? validite.getId() > 0 : false) {
                                if (!validite.getDateExpiration().equals(user.getDateExpiration())) {
                                    validite.setAuthor(currentUser);
                                    validite.setDateExpiration(user.getDateExpiration());
                                    dao.update(validite);
                                }
                            } else {
                                validite = new YvsUsersValidite(null, y, user.getDateExpiration());
                                validite.setAuthor(currentUser);
                                validite = (YvsUsersValidite) dao.save1(validite);
                            }
                        } else if (validite != null ? validite.getId() > 0 : false) {
                            dao.delete(validite);
                            validite = null;
                        }
                        y.setValidite(validite);
                    } catch (Exception ex) {
                        Logger.getLogger(ManagedUser.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    saveNiveauAcces(y, y.getNiveauAcces());
                }
                int idx = listUser.indexOf(y);
                if (idx > -1) {
                    listUser.set(idx, y);
                } else {
                    listUser.add(0, y);
                }
                return y;
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private YvsNiveauUsers saveNiveauAcces(YvsUsers user, YvsNiveauAcces nivo) {
        YvsNiveauUsers re;
        if (user.getId() < 1) {
            re = new YvsNiveauUsers();
            re.setIdUser(user);
            re.setIdNiveau(nivo);
            re.setId(null);
            re = (YvsNiveauUsers) dao.save1(re);
            return re;
        } else {
            champ = new String[]{"user", "societe"};
            val = new Object[]{user, currentAgence.getSociete()};
            re = (YvsNiveauUsers) dao.loadOneByNameQueries("YvsNiveauUsers.findNivoUserInScte", champ, val);
            if (re != null) {
                re.setIdNiveau(nivo);
                re.setDateSave(new Date());
                re.setDateUpdate(new Date());
                dao.update(re);
            } else {
                re = new YvsNiveauUsers();
                re.setIdUser(user);
                re.setIdNiveau(nivo);
                re.setId(null);
                re = (YvsNiveauUsers) dao.save1(re);
            }
        }
        return re;
    }

    public void chooseSociete(ValueChangeEvent ev) {
        if (ev != null) {
            long id = (long) ev.getNewValue();
            if (id == 0) {
                ManagedAgence w = (ManagedAgence) giveManagedBean(ManagedAgence.class);
                if (w != null) {
                    w.getAgences().clear();
                }
            } else {
                ManagedSociete w = (ManagedSociete) giveManagedBean(ManagedSociete.class);
                if (w != null) {
                    int idx = w.getListSociete().indexOf(new YvsSocietes(id));
                    if (idx > -1) {
                        societe = UtilSte.buildSimpleBeanSociete(w.getListSociete().get(idx));
                        w.loadAllAgence(id);
                    }
                }
            }
        }
    }

    public void chooseAgence(ValueChangeEvent ev) {
        if (ev != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                ManagedAgence w = (ManagedAgence) giveManagedBean(ManagedAgence.class);
                if (w != null) {
                    int idx = w.getAgences().indexOf(new YvsAgences(id));
                    if (idx > -1) {
                        agence = UtilAgence.buildBeanAgence(w.getAgences().get(idx));
                    }
                }
            }
        }
    }

    public void chooseGroupe() {
        if (users.getCategorie() != null ? users.getCategorie().getId() > 0 : false) {
            ManagedCategoriePerso w = (ManagedCategoriePerso) giveManagedBean(ManagedCategoriePerso.class);
            if (w != null) {
                int idx = w.getCategories().indexOf(new YvsComCategoriePersonnel(users.getCategorie().getId()));
                if (idx > -1) {
                    YvsComCategoriePersonnel y = w.getCategories().get(idx);
                    users.setCategorie(UtilCom.buildBeanCategoriePerso(y));
                }
            }
        }
    }

//    public void chooseNiveauAcces(ValueChangeEvent ev) {
//        if (ev != null) {
//            long id = (long) ev.getNewValue();
//            if (id > 0) {
//                NiveauAcces niveau = new NiveauAcces(id);
//                niveau.setDesignation(listNiveauAcces.get(listNiveauAcces.indexOf(niveau)).getDesignation());
//                users.getNiveauAcces().setDesignation(niveau.getDesignation());
//            }
//        }
//    }
    public void handleFileUpload(FileUploadEvent ev) {
        if (ev != null) {
            String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminDocUsers().substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminDocUsers().length());
            //répertoire destination de sauvegarge= C:\\lymytz\scte...
            String repDestSVG = Initialisation.getCheminDocUsers();
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
                users.setPhoto(file);
                getInfoMessage("Charger !");
                update("image_user_01");
                update("body_dlg_profil_user");

            } catch (IOException ex) {
                Logger.getLogger(ManagedUser.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void changeAcces(YvsUsers bean, boolean societe) {
        if (!societe) {
            bean.setAccesMultiAgence(!bean.getAccesMultiAgence());
            listUser.get(listUser.indexOf(bean)).setAccesMultiAgence(bean.getAccesMultiAgence());
        } else {
            bean.setAccesMultiSociete(!bean.getAccesMultiSociete());
            listUser.get(listUser.indexOf(bean)).setAccesMultiSociete(bean.getAccesMultiSociete());
        }
        dao.update(bean);
        update("form_user_00");
    }

    public void changeEtat() {
        if (!autoriser("base_user_active")) {
            openNotAcces();
            return;
        }
        userSelect.setActif(!userSelect.getActif());
        listUser.get(listUser.indexOf(userSelect)).setActif(userSelect.getActif());
        String rq = "UPDATE yvs_users SET actif=" + userSelect.getActif() + " WHERE id=?";
        Options[] param = new Options[]{new Options(userSelect.getId(), 1)};
        dao.requeteLibre(rq, param);
        if (actifSearch != null ? actifSearch : false) {
            if (userSelect.getActif()) {
                listUser.add(userSelect);
            } else {
                listUser.remove(userSelect);
            }
        } else {
            if (!userSelect.getActif()) {
                listUser.add(userSelect);
            } else {
                listUser.remove(userSelect);
            }
        }
        update("form_user_00");
        update("tab_user_00");
    }

    public void reInitialedPassword() {
        if (userSelect != null ? userSelect.getId() > 0 : false) {
            userSelect.setPasswordUser(defaultPwdUser);
            userSelect.setAleaMdp(hashMdp.randomString(15));
            userSelect.setPasswordUser(hashMdp.hashString(userSelect.getCodeUsers() + "" + userSelect.getAleaMdp() + "" + defaultPwdUser));
            dao.update(userSelect);

            int idx = listUser.indexOf(userSelect);
            if (idx > -1) {
                listUser.set(idx, userSelect);
            }
            succes();
        }
    }

    public void chooseEtat(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            etat = (String) ev.getNewValue();
            if (etat.equals("all")) {
                loadAll();
            } else if (etat.equals("true")) {
                loadAllEtat(true);
            } else {
                loadAllEtat(false);
            }
        }
    }

    //utilisateur connecte
    public void loadProfilUser() {
        currentParam = (YvsBaseParametre) dao.loadOneByNameQueries("YvsBaseParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        if ((currentUser != null) ? currentUser.getId() > 0 : false) {
            populateView(getUsersOnLine());
        }
    }

    public void updateProfilUser() {
        try {
            Users bean = getUsersOnLine();
            if (bean.getId() != 0) {
                Users user = recopieView();
                if (controleFicheProfil(user)) {
                    user.setConnecte(true);
                    YvsUsers y = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{user.getId()});
                    y.setCodeUsers(user.getCodeUsers());
                    y.setNomUsers(user.getNomUsers());
                    if (isUpdatePwd()) {
                        String pwd = "";
                        if (Util.asString(oldPwdUser)) {
                            pwd = hashMdp.hashString(bean.getCodeUsers() + "" + bean.getAleaMdp() + "" + oldPwdUser.trim());
                        }
                        if (!pwd.equals(bean.getPasswordUser().trim())) {
                            getErrorMessage("L'ancient mot de passe est invalide! Si vous voulez réinitialiser votre compte, veuillez contacter votre Administrateur!");
                            return;
                        }
                        user.setPasswordUser(newPwdUser);
                        y.setAleaMdp(hashMdp.randomString(15));
                        y.setPasswordUser(hashMdp.hashString(user.getCodeUsers() + "" + y.getAleaMdp() + "" + user.getPasswordUser()));
                    }
                    dao.update(y);
                    if (updateRation ? (y.getEmploye() != null ? y.getEmploye().getCompteTiers() != null : false) : false) {
                        YvsBaseTiers tiers = y.getEmploye().getCompteTiers();
                        if (tiers != null ? tiers.getId() > 0 : false) {
                            System.out.println("tiers.getCodeRation() : " + tiers.getCodeRation());
                            System.out.println("oldCodeRation : " + oldCodeRation);
                            if (Util.asString(tiers.getCodeRation()) ? tiers.getCodeRation().trim().equals(oldCodeRation.trim()) : true) {
                                tiers.setCodeRation(newCodeRation);
                                dao.update(tiers);
                            } else {
                                getErrorMessage("L'ancient code ration est invalide! Impossible de modifier le code ration!");
                            }
                        }
                    }
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Modification impossible !");
            Logger.getLogger(ManagedUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onCloseDialogProfil() {
        setUpdatePwd(false);
        oldPwdUser = null;
        newPwdUser = null;
        rembPwdUser = null;
        resetFiche();
    }

    public void changePwdUser() {
        updatePwd = !updatePwd;
    }

    public void changeRationUser() {
        updateRation = !updateRation;
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAll(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAll(true, true);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsUsers> re = paginator.parcoursDynamicData("YvsUsers", "y", "y.codeUsers", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void saveNewAcces() {
        try {
            if (codeAcces != null ? codeAcces.trim().length() < 1 : true) {
                getErrorMessage("Vous devez entrer le code");
                return;
            }
            if (selectAcces != null ? (selectAcces.getCode() != null ? selectAcces.getCode().getId() > 0 : false) : false) {
                selectAcces.getCode().setDescription(descriptionAcces);
                selectAcces.getCode().setCode(codeAcces);
                selectAcces.getCode().setAuthor(currentUser);
                selectAcces.getCode().setDateUpdate(new Date());
                dao.update(selectAcces.getCode());
                int idx = listAcces.indexOf(selectAcces);
                if (idx > -1) {
                    listAcces.set(idx, selectAcces);
                }
            } else {
                YvsBaseCodeAcces y = new YvsBaseCodeAcces();
                y.setAuthor(currentUser);
                y.setDescription(descriptionAcces);
                y.setCode(codeAcces);
                y.setSociete(currentAgence.getSociete());
                y.setId(null);
                y = (YvsBaseCodeAcces) dao.save1(y);
                if (users != null ? users.getId() > 0 : false) {
                    YvsBaseUsersAcces a = new YvsBaseUsersAcces((long) -(listAcces.size() + 1));
                    a.setAuthor(currentUser);
                    a.setCode(y);
                    a.setUsers(new YvsUsers(users.getId()));
                    listAcces.indexOf(a);
                }
            }
            update("tabview_users:data_code_acces");
            succes();
        } catch (Exception ex) {
            getErrorMessage("Action impossible!");
            Logger.getLogger(ManagedUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadAcces() {
        if (users != null ? users.getId() > 0 : false) {
            loadAcces(true, true);
        } else {
            getErrorMessage("Selectionner un utilisateur");
        }
    }

    public void loadAcces(boolean avance, boolean init) {
        listAcces.clear();
        if (users != null ? users.getId() > 0 : false) {
            p_acces.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
            List<YvsBaseCodeAcces> list = p_acces.executeDynamicQuery("YvsBaseCodeAcces", "y.code ", avance, init, dao);
            YvsBaseUsersAcces y;
            YvsUsers u = new YvsUsers(users.getId());
            for (YvsBaseCodeAcces a : list) {
                y = (YvsBaseUsersAcces) dao.loadOneByNameQueries("YvsBaseUsersAcces.findOne", new String[]{"users", "code"}, new Object[]{u, a});
                if (y != null ? (y.getId() != null ? y.getId() < 1 : true) : true) {
                    y = new YvsBaseUsersAcces((long) (-(listAcces.size() + 1)));
                    y.setAuthor(currentUser);
                    y.setCode(a);
                    y.setDateSave(new Date());
                    y.setDateUpdate(new Date());
                    y.setUsers(u);
                }
                listAcces.add(y);
            }
        }
        update("tabview_users:data_code_acces");
    }

    public void choosePaginatorAcces(ValueChangeEvent ev) {
        if ((ev != null) ? ev.getNewValue() != null : false) {
            long v;
            try {
                v = (long) ev.getNewValue();
            } catch (Exception ex) {
                v = (int) ev.getNewValue();
            }
            p_acces.setRows((int) v);
            loadAcces(true, true);
        }
    }

    public void loadOnViewUsers(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsUsers bean = (YvsUsers) ev.getObject();
            users = UtilUsers.buildBeanUsers(bean);
            update("select_users_acces");
        }
    }

    public void updateAcces(YvsBaseUsersAcces y) {
        if (!autoriser("base_user_attrib_code_acces")) {
            openNotAcces();
            return;
        }
        if (y != null) {
            long id = y.getId();
            if (id > 0) {
                dao.delete(y);
                y.setId(-(id * 1000));
            } else {
                y.setId(null);
                y = (YvsBaseUsersAcces) dao.save1(y);
            }
            int idx = listAcces.indexOf(new YvsBaseUsersAcces(id));
            if (idx > -1) {
                listAcces.set(idx, y);
            }
        }
    }

    public void searchUsers() {
        String num = users.getCodeUsers();
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedUser m = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (m != null) {
                Users e = m.searchUsersActif(num, true);
                users = e;
                if (m.getListUser() != null ? m.getListUser().size() > 1 : false) {
                    update("data_users_acces");
                }
            }
        }
    }

    public Users searchUsersActif(boolean open) {
        return searchUsersActif(numSearch, open);
    }

    public Users searchUsersActif(String num, boolean open) {
        Users a = new Users();
        a.setCodeUsers(num);
        a.setError(true);
        paginator.addParam(new ParametreRequete("y.actif", "actif", true, "=", "AND"));
        if (num != null ? num.trim().length() > 0 : false) {
            ParametreRequete p = new ParametreRequete(null, "codeUsers", num.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.codeUsers)", "codeUsers", num.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nomUsers)", "nomUsers", num.toUpperCase() + "%", "LIKE", "OR"));
            paginator.addParam(p);
            loadAllBySociete(true, true);
            if (listUser != null ? !listUser.isEmpty() : false) {
                if (listUser.size() > 1) {
                    if (open) {
                        openDialog("dlgListUsers");
                    }
                    a.setViewList(true);
                } else {
                    YvsUsers c = listUser.get(0);
                    a = UtilUsers.buildBeanUsers(c);
                }
                a.setError(false);
            } else {
                a.setNomUsers(num);
            }
        } else {
            paginator.addParam(new ParametreRequete("y.codeUsers", "codeUsers", null));
            loadAllBySociete(true, true);
        }
        return a;
    }

    public void initUsers() {
        initUsers(users);
    }

    public void initUsers(Users a) {
        if (a == null) {
            a = new Users();
        }
        paginator.addParam(new ParametreRequete("y.codeUsers", "codeUsers", null));
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        loadAll(true, true);
        a.setViewList(true);
    }

    public void addParamAcces() {
        ParametreRequete p = new ParametreRequete("y.code", "code", null, "LIKE", "AND");
        if (accesSearch != null ? accesSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("UPPER(y.code)", "code", accesSearch.toUpperCase() + "%", "LIKE", "AND");
        }
        p_acces.addParam(p);
        loadAcces(true, true);
    }

    public void addParamUsers() {
        ParametreRequete p = new ParametreRequete("y.codeUsers", "codeUsers", null, "LIKE", "AND");
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "codeUsers", numSearch.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.codeUsers)", "codeUsers", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nomUsers)", "codeUsers", numSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamActif(Boolean actifSearch) {
        this.actifSearch = actifSearch;
        paginator.addParam(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
        loadAll(true, true);
    }

    public void addParamActif() {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
        loadAll(true, true);
    }

    public void addParamPlanifier() {
        paginator.addParam(new ParametreRequete("y.connectOnlinePlanning", "connectOnlinePlanning", planifierSearch, "=", "AND"));
        loadAll(true, true);
    }

    public void addParamActifOnSociete() {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        loadAll(true, true);
    }

    public void addParamActif_Order() {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
        loadAll(true, true);
    }

    public void addParamConnected() {
        ParametreRequete p = new ParametreRequete("y.id", "connected", null, "=", "AND");
        if (connectSearch != null) {
            List<Long> ids = dao.loadListByNameQueries("YvsConnection.findIdUsers", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            ids.add(new Long(0));
            if (connectSearch) {
                p = new ParametreRequete("y.id", "connected", ids, "IN", "AND");
            } else {
                p = new ParametreRequete("y.id", "connected", ids, "NOT IN", "AND");
            }
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamSociete() {
        ParametreRequete p = new ParametreRequete("y.agence.societe", "societe", null, "=", "AND");
        if (societeSearch > 0) {
            p.setObjet(new YvsSocietes(societeSearch));
            //charge les agences
            ManagedAgence service = (ManagedAgence) giveManagedBean(ManagedAgence.class);
            if (service != null) {
                service.loadAllAgenceSociete(societeSearch);
            }
        }
        paginator.addParam(p);
        loadAll(true, true);
        ManagedNiveauAcces w = (ManagedNiveauAcces) giveManagedBean(ManagedNiveauAcces.class);
        if (w != null) {
            listNiveaux = w.loadBySociete(new YvsSocietes(societeSearch));
        }
    }

    public void addParamAgence() {
        ParametreRequete p = new ParametreRequete("y.agence", "agence", null, "=", "AND");
        if (agenceSearch > 0) {
            p = new ParametreRequete("y.agence", "agence", new YvsAgences(agenceSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamNiveau() {
        ParametreRequete p = new ParametreRequete("y.id", "users", null, "=", "AND");
        if (niveauSearch > 0) {
            String query = "SELECT y.id_user FROM yvs_niveau_users y WHERE y.id_niveau = ?";
            List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(niveauSearch, 1)});
            if (ids != null ? ids.isEmpty() : true) {
                ids = new ArrayList<Long>() {
                    {
                        add(-1L);
                    }
                };
            }
            p = new ParametreRequete("y.id", "users", ids, "IN", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamGroupe() {
        ParametreRequete p = new ParametreRequete("y.categorie", "categorie", null, "=", "AND");
        if (groupeSearch > 0) {
            p = new ParametreRequete("y.categorie", "categorie", new YvsComCategoriePersonnel(groupeSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void deconnectUsers(YvsUsers u) {
        userSelect = u;
    }

    public void deconnectUsers() {
        if (userSelect != null) {
            if (deconnectUsers_(userSelect)) {
                succes();
                update("tabRep");
            }
        } else {
            getErrorMessage("Veuillez selectionner l'utilisateur");
        }
    }

    public void deleteCodeAcces() {
        try {
            if (selectAcces != null) {
                selectAcces.getCode().setAuthor(currentUser);
                selectAcces.getCode().setDateUpdate(new Date());
                dao.delete(selectAcces.getCode());
                listAcces.remove(selectAcces);
                update("tabview_users:data_code_acces");
                succes();
            } else {
                getErrorMessage("Veuillez selectionner l'accès");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible!!!");
            getException("deleteCodeAcces", ex);
        }
    }

    public void chooseAgence() {
        if (users.getAgence() != null ? users.getAgence().getId() > 0 : false) {
            ManagedAgence service = (ManagedAgence) giveManagedBean(ManagedAgence.class);
            int idx = service.getAgences().indexOf(new YvsAgences(users.getAgence().getId()));
            if (idx >= 0) {
                ManagedNiveauAcces w = (ManagedNiveauAcces) giveManagedBean(ManagedNiveauAcces.class);
                if (w != null) {
                    w.loadAll(service.getAgences().get(idx).getSociete());
                }
            }
        }
    }

    public void chooseSocietes() {
        ManagedNiveauAcces w = (ManagedNiveauAcces) giveManagedBean(ManagedNiveauAcces.class);
        if (w != null) {
            w.getListNiveauAcces().clear();
            if (users.getAgence() != null ? users.getAgence().getId() > 0 : false) {
                ManagedAgence wa = (ManagedAgence) giveManagedBean(ManagedAgence.class);
                if (wa != null) {
                    int idx = wa.getAgences().indexOf(new YvsAgences(users.getAgence().getId()));
                    if (idx > -1) {
                        YvsAgences y = wa.getAgences().get(idx);
                        w.loadAll(y.getSociete());
                    }
                }
            }
        }
    }

    public boolean deconnectUsers_(YvsUsers u) {
//        try {
//            dao.requeteLibre("delete from yvs_connection where users = ?", new Options[]{new Options(u.getId(), 1)});
//            if (SessionManager.invalidate(u.getIdSession())) {
//
//            }
//            u.getConnections().clear();
//            if (listUser.contains(u)) {
//                listUser.remove(u);
//                update("tab_user_00");
//            }
//            return true;
//        } catch (Exception ex) {
//            getErrorMessage("Opération Impossible");
//            System.err.println("Erreur : " + ex.getMessage());
//            return false;
//        }
        return false;
    }

    public void initDataVente() {
        if (users.getCommerciaux() != null ? users.getCommerciaux().getId() > 0 : false) {
            if (dateDebutVente.toString().equals(dateFinVente.toString()) && dateDebutVente.equals(new Date())) {
                Calendar c = Calendar.getInstance();
                c.set(c.get(Calendar.YEAR), 0, 1);
                dateDebutVente = c.getTime();
                c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                dateFinVente = c.getTime();
            }
            models = dao.loadNameQueries("YvsBaseModeReglement.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            loadDataVente(dateDebutVente, dateFinVente);
        }
    }

    public void loadDateVente() {
        loadDataVente(dateDebutVente, dateFinVente);
    }

    public void loadDataVente(Date dateDebut, Date dateFin) {
//        champ = new String[]{"vendeur", "dateDebut", "dateFin", "type"};
//        val = new Object[]{new YvsUsers(users.getId()), dateDebut, dateFin, Constantes.TYPE_FV};
//        nameQueriCount = "YvsComCommercialVente.findIdFactureByDates";
//        List<Long> factures = dao.loadNameQueries(nameQueriCount, champ, val);
//        if (factures.isEmpty()) {
//            factures.add(-1L);
//        }
//        for (YvsBaseModeReglement m : models) {
//            champ = new String[]{"facture", "model", "dateDebut", "dateFin"};
//            val = new Object[]{factures, m, dateDebut, dateFin};
//            nameQueriCount = "YvsComptaCaissePieceVente.findAvanceByModeFacturesDates";
//            Double c = (Double) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
//            m.setCa(c != null ? c : 0);
//        }
//
//        // Chargement de toutes les factures de la societe
//        champ = new String[]{"vendeur", "dateDebut", "dateFin", "type"};
//        val = new Object[]{new YvsUsers(users.getId()), dateDebut, dateFin, Constantes.TYPE_FV};
//        nameQueriCount = "YvsComCommercialVente.countByDates";
//        Long m = (Long) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
//        nbreVente = m != null ? m : 0;
//
//        // Chargement des toutes les factures avoir de la societe validees
//        champ = new String[]{"vendeur", "statut", "dateDebut", "dateFin", "type1", "type2"};
//        val = new Object[]{new YvsUsers(users.getId()), Constantes.ETAT_VALIDE, dateDebut, dateFin, Constantes.TYPE_FAV, Constantes.TYPE_BRV};
//        nameQueriCount = "YvsComCommercialVente.countBy2StatutPDatesLivre";
//        Long mr = (Long) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
//        nameQueriCount = "YvsComCommercialVente.countBy2StatutPDates";
//        m = (Long) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
//        nbreVenteAvoir = (m != null ? m : 0) + (mr != null ? mr : 0);
//
//        champ = new String[]{"vendeur", "statut", "dateDebut", "dateFin", "type"};
//        nameQueriCount = "YvsComCommercialVente.findIdFactureByDatesLivre";
//        val = new Object[]{new YvsUsers(users.getId()), Constantes.ETAT_VALIDE, dateDebut, dateFin, Constantes.TYPE_BRV};
//        List<Long> ids = dao.loadNameQueries(nameQueriCount, champ, val);
//        if (ids.isEmpty()) {
//            ids.add(-1L);
//        }
//        Double d = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findTTCByFactures", new String[]{"docVente"}, new Object[]{ids});
//        double retour = (d != null ? d : 0);
//        val = new Object[]{new YvsUsers(users.getId()), Constantes.ETAT_VALIDE, dateDebut, dateFin, Constantes.TYPE_FAV};
//        ids = dao.loadNameQueries(nameQueriCount, champ, val);
//        if (ids.isEmpty()) {
//            ids.add(-1L);
//        }
//        d = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findTTCByFactures", new String[]{"docVente"}, new Object[]{ids});
//        double avoir = (d != null ? d : 0);
//        caVenteAvoir = (avoir + retour);
//
//        // Chargement des toutes les factures de la societe validees
//        champ = new String[]{"vendeur", "statut", "dateDebut", "dateFin", "type"};
//        val = new Object[]{new YvsUsers(users.getId()), Constantes.ETAT_VALIDE, dateDebut, dateFin, Constantes.TYPE_FV};
//        nameQueriCount = "YvsComCommercialVente.countByStatutPDates";
//        m = (Long) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
//        nbreVenteValide = m != null ? m : 0;
//
//        champ = new String[]{"vendeur", "statut", "dateDebut", "dateFin", "type"};
//        val = new Object[]{new YvsUsers(users.getId()), Constantes.ETAT_VALIDE, dateDebut, dateFin, Constantes.TYPE_FV};
//        nameQueriCount = "YvsComCommercialVente.findIdFactureByDatesStatut";
//        ids = dao.loadNameQueries(nameQueriCount, champ, val);
//        if (ids.isEmpty()) {
//            ids.add(-1L);
//        }
//        d = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findTTCByFactures", new String[]{"docVente"}, new Object[]{ids});
//        caVenteValide = d != null ? d : 0;
//
//        Double a = (Double) dao.loadObjectByNameQueries("YvsComCoutSupDocVente.findSumServiceByFactures", new String[]{"docVente", "sens", "service"}, new Object[]{ids, true, false});
//        d = (Double) dao.loadObjectByNameQueries("YvsComCoutSupDocVente.findSumServiceByFactures", new String[]{"docVente", "sens", "service"}, new Object[]{ids, false, false});
//        caVenteValideCS = (a != null ? a : 0) - (d != null ? d : 0);
//
//        a = (Double) dao.loadObjectByNameQueries("YvsComCoutSupDocVente.findSumServiceByFactures", new String[]{"docVente", "sens", "service"}, new Object[]{ids, true, true});
//        d = (Double) dao.loadObjectByNameQueries("YvsComCoutSupDocVente.findSumServiceByFactures", new String[]{"docVente", "sens", "service"}, new Object[]{ids, false, true});
//        caVenteValideSS = (a != null ? a : 0) - (d != null ? d : 0);
//        caVente = (caVenteValide + caVenteValideSS) - caVenteAvoir;
//
//        // Chargement des toutes les factures de la societe en attentes
//        champ = new String[]{"vendeur", "statut", "dateDebut", "dateFin", "type"};
//        val = new Object[]{new YvsUsers(users.getId()), Constantes.ETAT_EDITABLE, dateDebut, dateFin, Constantes.TYPE_FV};
//        nameQueriCount = "YvsComCommercialVente.countByStatutPDates";
//        m = (Long) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
//        nbreVenteAttence = m != null ? m : 0;
//        caVenteAttence = dao.loadCaVente(false, currentAgence.getSociete().getId(), dateDebut, dateFin, Constantes.ETAT_EDITABLE, true);
//
//        // Chargement des toutes les factures de la societe en cours de validation
//        champ = new String[]{"vendeur", "statut", "dateDebut", "dateFin", "type"};
//        val = new Object[]{new YvsUsers(users.getId()), Constantes.ETAT_ENCOURS, dateDebut, dateFin, Constantes.TYPE_FV};
//        nameQueriCount = "YvsComCommercialVente.countByStatutPDates";
//        m = (Long) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
//        nbreVenteEnCours = m != null ? m : 0;
//        caVenteEnCours = dao.loadCaVente(false, currentAgence.getSociete().getId(), dateDebut, dateFin, Constantes.ETAT_ENCOURS, true);
//
//        // Chargement des toutes les factures de la societe annulees
//        champ = new String[]{"vendeur", "statut", "dateDebut", "dateFin", "type"};
//        val = new Object[]{new YvsUsers(users.getId()), Constantes.ETAT_ANNULE, dateDebut, dateFin, Constantes.TYPE_FV};
//        nameQueriCount = "YvsComCommercialVente.countByStatutPDates";
//        m = (Long) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
//        nbreVenteAnnule = m != null ? m : 0;
//        caVenteAnnule = dao.loadCaVente(false, currentAgence.getSociete().getId(), dateDebut, dateFin, Constantes.ETAT_ANNULE, true);
//
//        // Chargement des toutes les factures de la societe livrées
//        champ = new String[]{"vendeur", "statut", "dateDebut", "dateFin", "type"};
//        val = new Object[]{new YvsUsers(users.getId()), Constantes.ETAT_LIVRE, dateDebut, dateFin, Constantes.TYPE_FV};
//        nameQueriCount = "YvsComCommercialVente.countByStatutLDates";
//        m = (Long) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
//        nbreVenteLivre = m != null ? m : 0;
//        caVenteLivre = dao.loadCaLVente(false, currentAgence.getSociete().getId(), dateDebut, dateFin, Constantes.ETAT_LIVRE, true);
//
//        // Chargement des toutes les factures de la societe livrées
//        champ = new String[]{"vendeur", "statut", "dateDebut", "dateFin", "type"};
//        val = new Object[]{new YvsUsers(users.getId()), Constantes.ETAT_ENCOURS, dateDebut, dateFin, Constantes.TYPE_FV};
//        nameQueriCount = "YvsComCommercialVente.countByStatutLDates";
//        m = (Long) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
//        nbreVenteEnCoursLivre = m != null ? m : 0;
//        caVenteEnCoursLivre = dao.loadCaLVente(false, currentAgence.getSociete().getId(), dateDebut, dateFin, Constantes.ETAT_ENCOURS, true);
//
//        // Chargement des toutes les factures de la societe encours livrées
//        champ = new String[]{"vendeur", "statut", "dateDebut", "dateFin", "type"};
//        val = new Object[]{new YvsUsers(users.getId()), Constantes.ETAT_ATTENTE, dateDebut, dateFin, Constantes.TYPE_FV};
//        nameQueriCount = "YvsComCommercialVente.countByStatutLDates";
//        m = (Long) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
//        nbreVenteNotLivre = m != null ? m : 0;
//        caVenteNotLivre = dao.loadCaLVente(false, currentAgence.getSociete().getId(), dateDebut, dateFin, Constantes.ETAT_ATTENTE, true);
//
//        // Chargement des toutes les factures de la societe en retard de livraison
//        champ = new String[]{"vendeur", "dateDebut", "dateFin", "type"};
//        val = new Object[]{new YvsUsers(users.getId()), dateDebut, dateFin, Constantes.TYPE_FV};
//        nameQueriCount = "YvsComCommercialVente.countByRetardLDates";
//        m = (Long) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
//        nbreVenteRetardLivr = m != null ? m : 0;
//        String rq = "select sum(get_ttc_vente(y.id)) from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente e on y.entete_doc = e.id"
//                + " inner join yvs_com_commercial_vente o on o.facture = y.id inner join yvs_com_creneau_horaire_users u on u.id = e.creneau"
//                + " inner join yvs_com_creneau_point c on c.id = u.creneau_point inner join yvs_base_point_vente d on d.id = c.point"
//                + " inner join yvs_com_comerciale m on o.commercial = m.id inner join yvs_agences a on a.id = d.agence"
//                + " where y.type_doc = 'FV' and m.utilisateur = ? and (e.date_entete between ? and ?) and y.statut_livre != 'L' and y.date_livraison_prevu < ?";
//        caVenteRetardLivr = dao.callFonction(rq, new Options[]{new Options(users.getId(), 1), new Options(dateDebut, 2), new Options(dateFin, 3), new Options(dateFin, 4)});
//
//        // Chargement des toutes les factures de la societe reglées
//        champ = new String[]{"vendeur", "statut", "dateDebut", "dateFin", "type"};
//        val = new Object[]{new YvsUsers(users.getId()), Constantes.ETAT_REGLE, dateDebut, dateFin, Constantes.TYPE_FV};
//        nameQueriCount = "YvsComCommercialVente.countByStatutRDates";
//        m = (Long) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
//        nbreVenteRegle = m != null ? m : 0;
//        caVenteRegle = dao.loadCaRVente(false, currentAgence.getSociete().getId(), dateDebut, dateFin, Constantes.ETAT_REGLE, true);
//
//        // Chargement des toutes les factures de la societe encours reglées
//        champ = new String[]{"vendeur", "statut", "dateDebut", "dateFin", "type"};
//        val = new Object[]{new YvsUsers(users.getId()), Constantes.ETAT_ENCOURS, dateDebut, dateFin, Constantes.TYPE_FV};
//        nameQueriCount = "YvsComCommercialVente.countByStatutRDates";
//        m = (Long) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
//        nbreVenteEnCoursRegle = m != null ? m : 0;
//
//        PaginatorResult<YvsComptaCaissePieceVente> pa = new PaginatorResult<>();
//        pa.addParam(new ParametreRequete("y.vente.id", "facture", factures, "IN", "AND"));
//        pa.addParam(new ParametreRequete("y.vente.enteteDoc.dateEntete", "dateEntete", dateDebut, dateFin, "BETWEEN", "AND"));
//        pa.addParam(new ParametreRequete("y.vente.statutRegle", "statut", Constantes.ETAT_ENCOURS, "=", "AND"));
//        nameQueri = pa.buildDynamicQuery(pa.getParams(), "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE ");
//        Double s = (Double) dao.loadObjectByEntity(nameQueri, pa.getChamp(), pa.getVal());
//        caVenteEnCoursRegle = s != null ? s : 0;
//
//        // Chargement des toutes les factures de la societe non reglées
//        champ = new String[]{"vendeur", "statut", "dateDebut", "dateFin", "type"};
//        val = new Object[]{new YvsUsers(users.getId()), Constantes.ETAT_ATTENTE, dateDebut, dateFin, Constantes.TYPE_FV};
//        nameQueriCount = "YvsComCommercialVente.countByStatutRDates";
//        m = (Long) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
//        nbreVenteNotRegle = m != null ? m : 0;
//        caVenteNotRegle = dao.loadCaRVente(false, currentAgence.getSociete().getId(), dateDebut, dateFin, Constantes.ETAT_ATTENTE, true);
//
//        // Versement attendus
//        champ = new String[]{"caissier", "dateDebut", "dateFin"};
//        val = new Object[]{new YvsUsers(users.getId()), dateDebut, dateFin};
//        nameQueriCount = "YvsComptaCaissePieceVente.findSumByCaissierDates";
//        s = (Double) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
//        versementAttendu = s != null ? s : 0;
    }

    public void loadDataArticelByVendeur() {
        tabArticleVendeur.returnArticles(currentAgence.getSociete().getId(), 0, 0, users.getId(), 0, 0, 0, 0, dateDebutVente, dateFinVente, periode, "", "", "", 0, 0, dao);;
    }

    public void downloadArticelByVendeur() {
        try {
            Map<String, Object> param = new HashMap<>();
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
            param.put("VENDEUR", (int) users.getId());
            param.put("AGENCE", currentAgence.getId().intValue());
            param.put("DATE_DEBUT", dateDebutVente);
            param.put("DATE_FIN", dateFinVente);
            param.put("PERIODE", periode);

            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
            executeReport("dashboard_article_vendeurs", param);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void changeDates(String m) {
        switch (m) {
            case "week": {
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.set(Calendar.DAY_OF_WEEK_IN_MONTH, c.getActualMinimum(Calendar.DAY_OF_WEEK_IN_MONTH));
                dateDebutVente = c.getTime();
                c.set(Calendar.DAY_OF_WEEK_IN_MONTH, c.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH));
                dateFinVente = c.getTime();
                break;
            }
            case "month": {
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
                dateDebutVente = c.getTime();
                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                dateFinVente = c.getTime();
                break;
            }
            case "year": {
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.set(Calendar.DAY_OF_YEAR, c.getActualMinimum(Calendar.DAY_OF_YEAR));
                dateDebutVente = c.getTime();
                c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));
                dateFinVente = c.getTime();
                break;
            }
            default: {
                dateDebutVente = new Date();
                dateFinVente = new Date();
            }
        }
    }

    public void fusionner(boolean fusionne) {
        try {
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty() ? ids.size() > 1 : false) {
                long newValue = listUser.get(ids.get(0)).getId();
                if (!fusionne) {
                    if (!autoriser("base_user_fusion")) {
                        openNotAcces();
                        return;
                    }
                    String oldValue = "0";
                    for (int i : ids) {
                        if (listUser.get(i).getId() != newValue) {
                            oldValue += "," + listUser.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_users", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                listUser.remove(new YvsUsers(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = listUser.get(idx).getNomUsers();
                    } else {
                        YvsUsers c = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                            fusionneTo = c.getNomUsers();
                        }
                    }
                    YvsUsers c;
                    for (int i : ids) {
                        long oldValue = listUser.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(listUser.get(i).getNomUsers());
                            }
                        } else {
                            c = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                                fusionnesBy.add(c.getNomUsers());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 utilisateurs");
            }
        } catch (NumberFormatException ex) {
        }
    }

    public void applyUserSysteme(YvsUsers user) {
        //vérifie si le user_agence existe
        if (!autoriser("base_user_add_")) {
            openNotAccesAction("Vous ne disposer pas des droits suffisants pour sauvegarder un utilisateur");
            return;
        }
        YvsUsersAgence ua = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findByUsersSysteme", new String[]{"societe", "users"}, new Object[]{currentAgence.getSociete(), user});
        String query = "UPDATE yvs_users_agence SET user_systeme=FALSE FROM yvs_agences a WHERE (a.societe=? AND a.id=agence) ";
        boolean save = false;
        if (ua == null) {
            ua = new YvsUsersAgence();
            ua.setAgence(currentAgence);
            ua.setAuthor(currentUser);
            ua.setConnecte(false);
            ua.setDateSave(new Date());
            ua.setDateUpdate(new Date());
            ua.setUsers(user);
            save = true;
        }
        dao.requeteLibre(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1)});
        if (save) {
            dao.save(ua);
        } else {
            ua.setDateUpdate(new Date());
            ua.setAuthor(currentUser);
            dao.update(ua);
        }
        succes();
    }

    public void onManagedCodeAcces(YvsUsers user) {
        users = UtilUsers.buildSimpleBeanUsers(user);
        loadAcces();
    }

    @Override
    public void onSelectDistant(YvsUsers y) {
        Navigations n = (Navigations) giveManagedBean(Navigations.class);
        if (n != null) {
            n.naviguationView("Gestion des utilisateurs", "modDonneBase", "smenUser", true);
        }
        onSelectObject(y);
    }

    public void onManagedDroitAcces(YvsUsers user) {
        ManagedNiveauAcces w = (ManagedNiveauAcces) giveManagedBean(ManagedNiveauAcces.class);
        if (w != null) {
            w.onSelectDistant(user);
        }
    }

    public void displayUsersAcces(YvsBaseUsersAcces acc) {
        try {
            acc.setUtilisateurs(dao.loadNameQueries("YvsBaseUsersAcces.findUsersByCodeNotUsers", new String[]{"users", "code"}, new Object[]{new YvsUsers(users.getId()), acc.getCode()}));
            update("dt_row_ex_" + acc.getId());
        } catch (Exception ex) {
            getException("displayUsersAcces", ex);
        }
    }
}
