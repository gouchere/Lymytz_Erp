/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.UploadedFile;
import yvs.dao.Options;
import yvs.entity.grh.activite.YvsContactsEmps;
import yvs.entity.grh.param.YvsGrhCategorieProfessionelle;
import yvs.entity.grh.param.YvsGrhConventionCollective;
import yvs.entity.grh.param.YvsGrhDomainesQualifications;
import yvs.entity.grh.param.YvsGrhEchelons;
import yvs.entity.grh.param.poste.YvsGrhDepartement;
import yvs.entity.grh.param.YvsGrhGradeEmploye;
import yvs.entity.grh.param.poste.YvsGrhPosteDeTravail;
import yvs.entity.grh.param.YvsGrhQualifications;
import yvs.entity.grh.personnel.YvsBanques;
import yvs.entity.grh.personnel.YvsCategorieProEmploye;
import yvs.entity.grh.personnel.YvsCompteBancaire;
import yvs.entity.grh.personnel.YvsGrhConventionEmploye;
import yvs.entity.grh.personnel.YvsDiplomeEmploye;
import yvs.entity.grh.personnel.YvsDiplomeEmployePK;
import yvs.entity.grh.personnel.YvsEchelonEmploye;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.personnel.YvsGrhDocumentEmps;
import yvs.entity.grh.personnel.YvsGrhPersonneEnCharge;
import yvs.entity.grh.personnel.YvsLangueEmps;
import yvs.entity.grh.personnel.YvsLangueEmpsPK;
import yvs.entity.grh.personnel.YvsLangues;
import yvs.entity.grh.personnel.YvsPermisDeCoduire;
import yvs.entity.grh.param.poste.YvsGrhPosteEmployes;
import yvs.entity.grh.personnel.YvsGrhProfilEmps;
import yvs.entity.grh.personnel.YvsGrhQualificationEmploye;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsGrhSecteurs;
import yvs.entity.users.YvsUsers;
import yvs.grh.bean.Banques;
import yvs.grh.bean.CompteBancaire;
import yvs.grh.bean.ContactEmps;
import yvs.grh.bean.Convention;
import yvs.grh.bean.Diplomes;
import yvs.init.Initialisation;
import yvs.grh.bean.Employe;
import yvs.grh.bean.Langues;
import yvs.parametrage.poste.PosteDeTravail;
import yvs.grh.bean.Qualification;
import yvs.parametrage.poste.Departements;
import yvs.grh.bean.DocumentsEmps;
import yvs.grh.bean.DomainesQualifications;
import yvs.grh.bean.GradeEmploye;
import yvs.grh.bean.PermisDeConduire;
import yvs.grh.bean.PersonneEnCharge;
import yvs.grh.bean.Profils;
import yvs.base.compta.Comptes;
import yvs.base.compta.ManagedCentreAnalytique;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.UtilCompta;
import yvs.base.tiers.ManagedTiers;
import yvs.base.tiers.Tiers;
import yvs.base.tiers.UtilTiers;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaAffecAnalEmp;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.ext.YvsExtEmploye;
import yvs.entity.grh.param.YvsGrhHistoriqueStatutEmploye;
import yvs.entity.grh.param.YvsGrhStatutEmploye;
import yvs.entity.grh.personnel.YvsGrhProfil;
import yvs.entity.grh.presence.YvsGrhEquipeEmploye;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.grh.bean.EquipeEmploye;
import yvs.grh.bean.ManagedContratEmploye;
import yvs.grh.bean.ManagedDepartement;
import yvs.grh.bean.StatutEmps;
import yvs.grh.statistique.ManagedTableauBord;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.poste.ManagedPosteDeTravail;
import yvs.parametrage.societe.UtilSte;
import yvs.users.Users;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "MEmps")
@SessionScoped
public class ManagedEmployes extends Managed<Employe, YvsGrhEmployes> implements Serializable {

    private boolean update; //lorsque ce booléen est à true, la fiche est plutôt modifié. il passe à true après une recherche fructueuse ou le choix d'un employé dans la liste    
//    private boolean activBtnForm = true;
//    private boolean displayAvancement; //permet d'attribuer une seule personne une nouvelle convention
    /**
     * gestion des langues
     */
    private Langues langue = new Langues();
//    private boolean lu, ecrit, parle;
    private String newLangue;
    /**
     * end...
     */
    /**
     * Bilan social
     */
    private Date dateVisite;
    private String aptitude;
    /**
     * end...
     */
    @ManagedProperty(value = "#{employe}")
    private Employe employe;
    YvsGrhEmployes employes;
    private List<YvsGrhEmployes> listEmployes, listSelection;
    private PaginatorResult<YvsGrhEmployes> pa = new PaginatorResult<>();
    private YvsAgences agence;
    private String codeExterne;
    private boolean displayParamExt;

    /*
     * personnes en charge
     */
    private PersonneEnCharge personne = new PersonneEnCharge();
    private ContactEmps contactEmps = new ContactEmps();
    private CompteBancaire compteBancaire = new CompteBancaire();
    /**
     * Données externe
     */
    private YvsGrhDomainesQualifications selectDomaineQ = new YvsGrhDomainesQualifications();
    private DomainesQualifications newDomaine = new DomainesQualifications();

    private List<Langues> listLangues;
    private List<YvsGrhPosteDeTravail> posteDeTravails;

    private long idAgenceSearch;
    private long profilF;
    private String searchStatut;
    private String depSearch;
    private String codeTiesrSearch;
    private String posteSearch;
    private Boolean searchActif;
    /**
     * Users
     */
    private List<YvsUsers> listUsers;
    private List<YvsDictionnaire> listPays, listVille;
    private List<YvsGrhDomainesQualifications> domainesQualification;
    private List<GradeEmploye> listGradeEmploye;
    private List<YvsGrhCategorieProfessionelle> listCategoriePro;

    /**
     * end Users
     */
    /**/
    //diplomes
    /**/
    private Diplomes diplome = new Diplomes();
    private String chaineSelectEmploye, numSearch, fusionneTo;
//    private short classement;
    private List<Diplomes> diplomes;
    private PermisDeConduire permis = new PermisDeConduire();
    private GradeEmploye grade = new GradeEmploye();
    private List<YvsBanques> listBanques;
    private List<String> fusionnesBy;

    private List<YvsGrhEquipeEmploye> equipesEmployes, equipesEmployeActif;
    private EquipeEmploye equipe = new EquipeEmploye();

    private Profils profil = new Profils();
    private List<YvsGrhProfil> profils;
    private List<YvsGrhProfil> profilsActif;
    private List<YvsGrhStatutEmploye> statutsEmployes;

    private Qualification qualification = new Qualification();
    private Qualification newQualif = new Qualification();
    private boolean displayDelQualif, updateQualif, displayBtnRvQualif; //permset de supprimer définitivement une qualifiquation

    private String chaineSelectQualification;

    private boolean paramDate;
    private Date dateDebut = new Date(), dateFin = new Date(), dateDebut_ = new Date(), dateFin_ = new Date();
    private boolean date_up = false;

    private String orderList = "y.nom, y.prenom";

    private boolean actionFournisseur = true, actionTiers = true, actionClient = true, actionCommercial = true;

    private String orderPrint = "matricule";
    private List<yvs.util.Options> colonnesPrint = new ArrayList<>();
    private Integer statutSocialFilter = null;

    public ManagedEmployes() {
        profils = new ArrayList<>();
        profilsActif = new ArrayList<>();
        listEmployes = new ArrayList<>();
        listSelection = new ArrayList<>();
        listPays = new ArrayList<>();
        listVille = new ArrayList<>();
        listUsers = new ArrayList<>();
        listCategoriePro = new ArrayList<>();
        listLangues = new ArrayList<>();
        posteDeTravails = new ArrayList<>();
        diplomes = new ArrayList<>();
        listGradeEmploye = new ArrayList<>();
        listBanques = new ArrayList<>();
        domainesQualification = new ArrayList<>();
        equipesEmployes = new ArrayList<>();
        equipesEmployeActif = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
        statutsEmployes = new ArrayList<>();
        statutsEmployes.add(new YvsGrhStatutEmploye(1L, Constantes.GRH_STATUT_PERMANANT, Constantes.GRH_STATUT_LIB_PERMANANT));
        statutsEmployes.add(new YvsGrhStatutEmploye(2L, Constantes.GRH_STATUT_TEMPORAIRE, Constantes.GRH_STATUT_LIB_TEMPORAIRE));
        statutsEmployes.add(new YvsGrhStatutEmploye(3L, Constantes.GRH_STATUT_STAGIAIRE, Constantes.GRH_STATUT_LIB_STAGIAIRE));
        statutsEmployes.add(new YvsGrhStatutEmploye(4L, Constantes.GRH_STATUT_TACHERON, Constantes.GRH_STATUT_LIB_TACHERON));
        colonnesPrint.add(new yvs.util.Options("matricule", "Matricule"));
        colonnesPrint.add(new yvs.util.Options("nom", "Nom"));
        colonnesPrint.add(new yvs.util.Options("prenom", "Prenom"));
        colonnesPrint.add(new yvs.util.Options("nom, prenom", "Prenom & Prénom"));
        colonnesPrint.add(new yvs.util.Options("date_embauche", "Date d'embauche"));
        colonnesPrint.add(new yvs.util.Options("date_naissance", "Date de naissance"));
        colonnesPrint.add(new yvs.util.Options("matricule_cnps", "Matricule CNPS"));
    }

    public Integer getStatutSocialFilter() {
        return statutSocialFilter;
    }

    public void setStatutSocialFilter(Integer statutSocialFilter) {
        this.statutSocialFilter = statutSocialFilter;
    }

    public String getOrderList() {
        return orderList;
    }

    public void setOrderList(String orderList) {
        this.orderList = orderList;
    }

    public String getOrderPrint() {
        return orderPrint;
    }

    public void setOrderPrint(String orderPrint) {
        this.orderPrint = orderPrint;
    }

    public List<yvs.util.Options> getColonnesPrint() {
        return colonnesPrint;
    }

    public void setColonnesPrint(List<yvs.util.Options> colonnesPrint) {
        this.colonnesPrint = colonnesPrint;
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

    public boolean isActionFournisseur() {
        return actionFournisseur;
    }

    public void setActionFournisseur(boolean actionFournisseur) {
        this.actionFournisseur = actionFournisseur;
    }

    public boolean isActionTiers() {
        return actionTiers;
    }

    public void setActionTiers(boolean actionTiers) {
        this.actionTiers = actionTiers;
    }

    public boolean isActionClient() {
        return actionClient;
    }

    public void setActionClient(boolean actionClient) {
        this.actionClient = actionClient;
    }

    public boolean isActionCommercial() {
        return actionCommercial;
    }

    public void setActionCommercial(boolean actionCommercial) {
        this.actionCommercial = actionCommercial;
    }

    public boolean isDate_up() {
        return date_up;
    }

    public void setDate_up(boolean date_up) {
        this.date_up = date_up;
    }

    public Date getDateDebut_() {
        return dateDebut_;
    }

    public void setDateDebut_(Date dateDebut_) {
        this.dateDebut_ = dateDebut_;
    }

    public Date getDateFin_() {
        return dateFin_;
    }

    public void setDateFin_(Date dateFin_) {
        this.dateFin_ = dateFin_;
    }

    public boolean isParamDate() {
        return paramDate;
    }

    public void setParamDate(boolean paramDate) {
        this.paramDate = paramDate;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public boolean isDisplayParamExt() {

        return displayParamExt;
    }

    public void setDisplayParamExt(boolean displayParamExt) {
        this.displayParamExt = displayParamExt;
    }

    public String getCodeExterne() {
        return codeExterne;
    }

    public void setCodeExterne(String codeExterne) {
        this.codeExterne = codeExterne;
    }

    public List<YvsGrhStatutEmploye> getStatutsEmployes() {
        return statutsEmployes;
    }

    public void setStatutsEmployes(List<YvsGrhStatutEmploye> statutsEmployes) {
        this.statutsEmployes = statutsEmployes;
    }

    public Profils getProfil() {
        return profil;
    }

    public void setProfil(Profils profil) {
        this.profil = profil;
    }

    public List<YvsGrhProfil> getProfils() {
        return profils;
    }

    public void setProfils(List<YvsGrhProfil> profils) {
        this.profils = profils;
    }

    public List<YvsGrhProfil> getProfilsActif() {
        return profilsActif;
    }

    public void setProfilsActif(List<YvsGrhProfil> profilsActif) {
        this.profilsActif = profilsActif;
    }

    public String getDepSearch() {
        return depSearch;
    }

    public void setDepSearch(String depSearch) {
        this.depSearch = depSearch;
    }

    public String getCodeTiesrSearch() {
        return codeTiesrSearch;
    }

    public void setCodeTiesrSearch(String codeTiesrSearch) {
        this.codeTiesrSearch = codeTiesrSearch;
    }

    public String getPosteSearch() {
        return this.posteSearch;
    }

    public void setPosteSearch(String poste) {
        this.posteSearch = poste;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public PaginatorResult<YvsGrhEmployes> getPa() {
        return pa;
    }

    public void setPa(PaginatorResult<YvsGrhEmployes> pa) {
        this.pa = pa;
    }

    public EquipeEmploye getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeEmploye equipe) {
        this.equipe = equipe;
    }

    public YvsGrhDomainesQualifications getSelectDomaineQ() {
        return selectDomaineQ;
    }

    public void setSelectDomaineQ(YvsGrhDomainesQualifications selectDomaineQ) {
        this.selectDomaineQ = selectDomaineQ;
    }

    public DomainesQualifications getNewDomaine() {
        return newDomaine;
    }

    public void setNewDomaine(DomainesQualifications newDomaine) {
        this.newDomaine = newDomaine;
    }

    public List<YvsGrhDomainesQualifications> getDomainesQualification() {
        return domainesQualification;
    }

    public void setDomainesQualification(List<YvsGrhDomainesQualifications> domainesQualification) {
        this.domainesQualification = domainesQualification;
    }

    public long getIdAgenceSearch() {
        return idAgenceSearch;
    }

    public void setIdAgenceSearch(long idAgenceSearch) {
        this.idAgenceSearch = idAgenceSearch;
    }

    public List<YvsBanques> getListBanques() {
        return listBanques;
    }

    public void setListBanques(List<YvsBanques> listBanques) {
        this.listBanques = listBanques;
    }

    public ContactEmps getContactEmps() {
        return contactEmps;
    }

    public void setContactEmps(ContactEmps contactEmps) {
        this.contactEmps = contactEmps;
    }

    public CompteBancaire getCompteBancaire() {
        return compteBancaire;
    }

    public void setCompteBancaire(CompteBancaire compteBancaire) {
        this.compteBancaire = compteBancaire;
    }

    public String getChaineSelectEmploye() {
        return chaineSelectEmploye;
    }

    public void setChaineSelectEmploye(String chaineSelectEmploye) {
        this.chaineSelectEmploye = chaineSelectEmploye;
    }

    public PermisDeConduire getPermis() {
        return permis;
    }

    public void setPermis(PermisDeConduire permis) {
        this.permis = permis;
    }

    public String getNewLangue() {
        return newLangue;
    }

    public void setNewLangue(String newLangue) {
        this.newLangue = newLangue;
    }

    public Diplomes getDiplome() {
        return diplome;
    }

    public void setDiplome(Diplomes diplome) {
        this.diplome = diplome;
    }

    public List<Diplomes> getDiplomes() {
        return diplomes;
    }

    public void setDiplomes(List<Diplomes> diplomes) {
        this.diplomes = diplomes;
    }

    public GradeEmploye getGrade() {
        return grade;
    }

    public void setGrade(GradeEmploye grade) {
        this.grade = grade;
    }

    public List<GradeEmploye> getListGradeEmploye() {
        return listGradeEmploye;
    }

    public void setListGradeEmploye(List<GradeEmploye> listGradeEmploye) {
        this.listGradeEmploye = listGradeEmploye;
    }

    public List<YvsGrhPosteDeTravail> getPosteDeTravails() {
        return posteDeTravails;
    }

    public void setPosteDeTravails(List<YvsGrhPosteDeTravail> posteDeTravails) {
        this.posteDeTravails = posteDeTravails;
    }

    public List<Langues> getListLangues() {
        return listLangues;
    }

    public void setListLangues(List<Langues> listLangues) {
        this.listLangues = listLangues;
    }

    public List<YvsGrhCategorieProfessionelle> getListCategoriePro() {
        return listCategoriePro;
    }

    public void setListCategoriePro(List<YvsGrhCategorieProfessionelle> listCategoriePro) {
        this.listCategoriePro = listCategoriePro;
    }

    public List<YvsGrhEmployes> getListEmployes() {
        return listEmployes;
    }

    public void setListEmployes(List<YvsGrhEmployes> listEmployes) {
        this.listEmployes = listEmployes;
    }

    public String getMatriculeToUpdate() {
        return matriculeToUpdate;
    }

    public void setMatriculeToUpdate(String matriculeToUpdate) {
        this.matriculeToUpdate = matriculeToUpdate;
    }

    public PersonneEnCharge getPersonne() {
        return personne;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Langues getLangue() {
        return langue;
    }

    public void setLangue(Langues langue) {
        this.langue = langue;
    }

    public Date getDateVisite() {
        return dateVisite;
    }

    public void setDateVisite(Date dateVisite) {
        this.dateVisite = dateVisite;
    }

    public String getAptitude() {
        return aptitude;
    }

    public void setAptitude(String aptitude) {
        this.aptitude = aptitude;
    }

    public List<YvsUsers> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<YvsUsers> listUsers) {
        this.listUsers = listUsers;
    }

    public List<YvsDictionnaire> getListVille() {
        return listVille;
    }

    public void setListVille(List<YvsDictionnaire> listVille) {
        this.listVille = listVille;
    }

    public List<YvsDictionnaire> getListPays() {
        return listPays;
    }

    public void setListPays(List<YvsDictionnaire> listPays) {
        this.listPays = listPays;
    }

    public List<YvsGrhEmployes> getListSelection() {
        return listSelection;
    }

    public void setListSelection(List<YvsGrhEmployes> listSelection) {
        this.listSelection = listSelection;
    }

    public long getNomberEmploye() {
        return nombreEmploye;
    }

    public void setNomberEmploye(long nomberEmploye) {
        this.nombreEmploye = nomberEmploye;
    }

    public String getSearchStatut() {
        return searchStatut;
    }

    public void setSearchStatut(String searchStatut) {
        this.searchStatut = searchStatut;
    }

    public long getProfilF() {
        return profilF;
    }

    public void setProfilF(long profilF) {
        this.profilF = profilF;
    }

    public Boolean getSearchActif() {
        return searchActif;
    }

    public void setSearchActif(Boolean searchActif) {
        this.searchActif = searchActif;
    }

    public List<YvsGrhEquipeEmploye> getEquipesEmployes() {
        return equipesEmployes;
    }

    public void setEquipesEmployes(List<YvsGrhEquipeEmploye> equipesEmployes) {
        this.equipesEmployes = equipesEmployes;
    }

    public List<YvsGrhEquipeEmploye> getEquipesEmployeActif() {
        return equipesEmployeActif;
    }

    public void setEquipesEmployeActif(List<YvsGrhEquipeEmploye> equipesEmployeActif) {
        this.equipesEmployeActif = equipesEmployeActif;
    }

    public YvsGrhEmployes getEmployes() {
        return employes;
    }

    public void setEmployes(YvsGrhEmployes employes) {
        this.employes = employes;
    }

    /**
     * Managed *
     */
    private boolean displayName = false; //affiche le champ de saisi du nom de jeune fille en cas de choix de civilité = Mme

    public boolean isDisplayName() {
        return displayName;
    }

    public void setDisplayName(boolean displayName) {
        this.displayName = displayName;
    }

    /**
     *
     * @param ev
     */
    public void handleFileUpload_OLD(FileUploadEvent ev) {
        String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminPhotoEmps().substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminPhotoEmps().length());
        //répertoire destination de sauvegarge= C:\\lymytz\scte...
        String repDestSVG = Initialisation.getCheminPhotoEmps();
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
            employe.setPhotos(file);
            FacesMessage msg = new FacesMessage("Chargé ! ", ev.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (IOException ex) {
            Logger.getLogger(ManagedEmployes.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            InputStream is = event.getFile().getInputstream();
            String extension = Util.getExtension(event.getFile().getFileName());
            byte[] bytes = IOUtils.toByteArray(is);
            String file = new String(Base64.encodeBase64(bytes));
            employe.setPhotos(file);
            employe.setPhotoExtension(extension);
            getInfoMessage("Charger !");
        } catch (IOException ex) {
            getErrorMessage("Action impossible!!!");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void choixUser(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id == -1) {
                openDialog("dlgListUsers");
            }
        }
    }

    public void selectUser(SelectEvent ev) {
        YvsUsers usr = (YvsUsers) ev.getObject();
        if (usr != null) {
            if (!listUsers.contains(usr)) {
                listUsers.add(usr);
            }
            employe.setUser(UtilUsers.buildSimpleBeanUsers(usr));
        }
    }
    //ville pour le lieu de naissance et ville 1 pour le lieu de délivrance de na CNI
    private String pays, ville, ville1;
    private long idPays, idPays1;

    public long getIdPays() {
        return idPays;
    }

    public void setIdPays(long idPays) {
        this.idPays = idPays;
    }

    public long getIdPays1() {
        return idPays1;
    }

    public void setIdPays1(long idPays1) {
        this.idPays1 = idPays1;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getVille1() {
        return ville1;
    }

    public void setVille1(String ville1) {
        this.ville1 = ville1;
    }

    public void loadAllEmploye() {
        listEmployes = dao.loadNameQueries("YvsGrhEmployes.findAlls", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllEmploye(long agence) {
        listEmployes = dao.loadNameQueries("YvsGrhEmployes.findAll", new String[]{"agence"}, new Object[]{new YvsAgences(agence)});
    }

    public void choixCivilite(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            String str = (String) ev.getNewValue();
            displayName = str.trim().equals("Mme");
            if (employe.getId() < 1) {
                if (str.trim().equals("M.")) {
                    employe.setPhotos(Constantes.DEFAULT_PHOTO_EMPLOYE_MAN());
                } else {
                    employe.setPhotos(Constantes.DEFAULT_PHOTO_EMPLOYE_WOMAN());
                }
                update("photos");
            }
        }
    }

    public void choixVille(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id == -1) {
                openDialog("dlgVille");
            }
        }
    }

    public void selectVille(SelectEvent ev) {
        YvsDictionnaire dd = (YvsDictionnaire) ev.getObject();
        if (dd != null) {
            if (!listVille.contains(dd)) {
                listVille.add(dd);
            }
            employe.setVilleNaissance(UtilSte.buildBeanDictionnaire(dd));
        }
    }

    public void createNewVille() {
        if (ville != null && idPays != 0) {
            YvsDictionnaire v = new YvsDictionnaire();
            v.setLibele(ville);
            v.setTitre(Constantes.T_VILLES);
            v.setSociete(currentAgence.getSociete());
            v.setActif(true);
            v.setSupp(false);
            v.setParent(new YvsDictionnaire(idPays));
            v.setAuthor(currentUser);
            v.setDateSave(new Date());
            v.setDateUpdate(new Date());
            v = (YvsDictionnaire) dao.save1(v);
            Dictionnaire d = new Dictionnaire(v.getId(), v.getLibele());
            listVille.add(v);
            employe.setVilleNaissance(d);
        } else if (idPays == 0) {
            getErrorMessage("Veuillez choisir un le pays !");
        }
    }

    public void selectVille1(SelectEvent ev) {
        YvsDictionnaire dd = (YvsDictionnaire) ev.getObject();
        if (dd != null) {
            if (!listVille.contains(dd)) {
                listVille.add(dd);
            }
            employe.setLieuDelivCni(UtilSte.buildBeanDictionnaire(dd));
            newBanque.setVille(UtilSte.buildBeanDictionnaire(dd));
        }
    }

    public void createNewVille1() {
        if (ville1 != null && idPays1 != 0) {
            YvsDictionnaire v = new YvsDictionnaire();
            v.setLibele(ville1);
            v.setTitre(Constantes.T_VILLES);
            v.setSociete(currentAgence.getSociete());
            v.setActif(true);
            v.setSupp(false);
            v.setAuthor(currentUser);
            v.setParent(new YvsDictionnaire(idPays1));
            v = (YvsDictionnaire) dao.save1(v);
            Dictionnaire d = new Dictionnaire(v.getId(), v.getLibele());
            listVille.add(v);
            employe.setLieuDelivCni(d);
            newBanque.setVille(d);
        } else if (idPays == 0) {
            getErrorMessage("Veuillez choisir un le pays !");
        }
    }

    public void choixPay(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id == -1) {
                openDialog("dlgPays");
            } else {
                //charge les ville du pays selectionné
                loadVillePays(id);
                update("id-emp-vill-banq");
            }
        }
    }

    private void loadVillePays(long pays) {
        champ = new String[]{"parent"};
        val = new Object[]{new YvsDictionnaire(pays)};
        listVille = dao.loadNameQueries("YvsDictionnaire.findVilleOnePays", champ, val);

    }

    public void selectPay(SelectEvent ev) {
        YvsDictionnaire dd = (YvsDictionnaire) ev.getObject();
        if (dd != null) {
            if (!listPays.contains(dd)) {
                listPays.add(dd);
            }
            employe.setPaysDorigine(UtilSte.buildBeanDictionnaire(dd));
            newBanque.setPays(UtilSte.buildBeanDictionnaire(dd));
            loadVillePays(dd.getId());
        }
    }

    public void createNewPAys() {
        if (pays != null) {
            YvsDictionnaire p = new YvsDictionnaire();
            p.setLibele(pays);
            p.setTitre(Constantes.T_PAYS);
            p.setSociete(currentAgence.getSociete());
            p.setActif(true);
            p.setSupp(false);
            p.setAuthor(currentUser);
            p = (YvsDictionnaire) dao.save1(p);
            employe.setPaysDorigine(UtilSte.buildBeanDictionnaire(p));
            listPays.add(p);
            listVille.clear();
        }
    }

    public void loadPays(int limit, boolean all) {
        listPays.clear();
        champ = new String[]{"titre"};
        val = new Object[]{Constantes.T_PAYS};
        listPays = dao.loadNameQueries("YvsDictionnaire.findByTitre", champ, val);
    }

    public void loadVille(int limit, boolean all) {
        champ = new String[]{"titre"};
        val = new Object[]{Constantes.T_VILLES};
        listVille = dao.loadNameQueries("YvsDictionnaire.findByTitre", champ, val);
    }
    /*--- Poste de travail---*/

    public void choixPoste(ValueChangeEvent ev) {
        if (ev != null) {
            long id = (long) ev.getNewValue();
            if (id == Long.MAX_VALUE) {
                openDialog("dlgPosteDeTravail");
            }
        }
    }

    public void selectPoste(SelectEvent ev) {
        YvsGrhPosteDeTravail post = (YvsGrhPosteDeTravail) ev.getObject();
        if (post != null) {
            posteDeTravails.add(post);
            employe.setPosteDeTravail(UtilGrh.builSimpledBeanPoste(post));
        }
    }

    public void createNewPosteDeTravail() {
        if (employe.getPosteDeTravail().getIntitule() != null && employe.getPosteDeTravail().getDepartement().getId() != 0) {
            YvsGrhPosteDeTravail pt = new YvsGrhPosteDeTravail();
            pt.setIntitule(employe.getPosteDeTravail().getIntitule());
            pt.setDepartement(new YvsGrhDepartement(employe.getPosteDeTravail().getDepartement().getId()));
            pt.setNombrePlace(employe.getPosteDeTravail().getNombrePlace());
            pt.setActif(true);
            pt.setSupp(false);
            pt = (YvsGrhPosteDeTravail) dao.save1(pt);
            posteDeTravails.add(0, pt);
            employe.getPosteDeTravail().setId(pt.getId());
            employe.getPosteDeTravail().setDepartement(UtilGrh.buildBeanDepartement(pt.getDepartement()));
        } else if (employe.getPosteDeTravail().getDepartement().getId() == 0) {
            getErrorMessage("Vous devez préciser le département où est référencer ce poste !");
        }
    }

    public void loadPoste() {
        ManagedDepartement service = (ManagedDepartement) giveManagedBean("managedDepartement");
        List<YvsGrhPosteDeTravail> lp = dao.loadNameQueries("YvsPosteDeTravail.findAllActif", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        if (service != null) {
            posteDeTravails = UtilGrh.buildBeanPoste(lp, service.getListValue());
        }
    }

    @Override
    public boolean controleFiche(Employe bean) {
//contrôle les champs vide        
        if (employe.getMatricule() != null) {
            Long nbre;
            //contrôle que l'employé qu'on veut enregistrer ne possède pas: un code utilisateur déjà utilisé, ni le matricule, ni le compte auxiliaire
            if (employe.getUser().getId() > 0) {
                champ = new String[]{"user", "matricule", "societe"};
                val = new Object[]{employe.getUser().getId(), employe.getMatricule(), currentAgence.getSociete()};
                nbre = (Long) dao.loadObjectByNameQueries("YvsGrhEmployes.count", champ, val);
            } else {
                champ = new String[]{"matricule", "societe"};
                val = new Object[]{employe.getMatricule(), currentAgence.getSociete()};
                nbre = (Long) dao.loadObjectByNameQueries("YvsGrhEmployes.count1", champ, val);
            }
            long d = (nbre != null) ? nbre : 0;
            if (d > 0) {
                getMessage("Un employé est déjà enregistré avec le même code utilisateur ou le même matricule", FacesMessage.SEVERITY_ERROR);
                return false;
            }
// la date de délivrance de la cni doit être plus recente que la date d'expiration
            if (employe.getDateDelivCni() != null && employe.getDateExpCni() != null) {
                if (employe.getDateDelivCni().after(employe.getDateExpCni())) {
                    getMessage("La date de délivrance de votre CNI est plus grande que la date d'expiration", FacesMessage.SEVERITY_ERROR);
                    return false;
                }
            }
            if (employe.getDateEmbauche() != null && employe.getDateArret() != null) {
                if (employe.getDateEmbauche().after(employe.getDateArret())) {
                    getMessage("La date d'embauche est plus grande que la date d'arrêt", FacesMessage.SEVERITY_ERROR);
                    return false;
                }
            }
            if (employe.getStatutEmps().getId() <= 0) {
                getErrorMessage("Vous devez précisé le statut de l'employé !");
                return false;
            }
            //ne pas modifier un employé dont l'agence de connexion est différente de l'agence en cours.
//            if ((bean.getAgence() != null) ? bean.getAgence().getId() > 0 : false) {
//                if (bean.getAgence().getId() != currentAgence.getId()) {
//                    getErrorMessage("Impossible de modifier cet employé!", "Veuillez vous connecter à son agence d'enregistrement !");
//                    return false;
//                }
//            }
        }
        return true;
    }

    @Override
    public boolean saveNew() {
        if (!update) {
            if (update = saveNewEmps()) {
                actionOpenOrResetAfter(this);
                succes();
            }
        } else {
            updateBean();
            actionOpenOrResetAfter(this);
            succes();
        }
        reloadUser();
        return true;
    }

    public boolean saveNewEmps() {
        if (controleFiche(employe)) {
            //enregistre un nouvel employé
            employes = UtilGrh.buildEmployeEntity(employe);
            employes.setAgence(currentAgence);
            employes.setId(null);
            employes.setAuthor(currentUser);
            employes = (YvsGrhEmployes) dao.save1(employes);
            employe.setId(employes.getId());
            employes.setStatutEmps(saveStatut(employe.getStatutEmps().getId()));
            //si le poste de travail est donnée
            if (employe.getPosteDeTravail().getId() > 0) {
                employes.setPosteActif(savePosteEmps());
            }
            //si la catégorie et l'echelon sont données on enegistre la convention collective
            if (employe.getConvention().getCategorie().getId() != 0 && employe.getConvention().getEchelon().getId() != 0 && currentAgence.getSecteurActivite() != null) {
                //cherche la convetion collective rattaché
                champ = new String[]{"categorie", "echelon", "secteur"};
                val = new Object[]{new YvsGrhCategorieProfessionelle(employe.getConvention().getCategorie().getId()), new YvsGrhEchelons(employe.getConvention().getEchelon().getId()), new YvsGrhSecteurs(currentAgence.getSecteurActivite().getId())};
                YvsGrhConventionCollective cc = (YvsGrhConventionCollective) dao.loadOneByNameQueries("YvsConventionCollective.findByCE", champ, val);
                if (cc != null) {
                    //sauvegarde la nouvelle convetion d'un employé
                    saveConventionEMps(cc);
                }
            }

            //enregistre le profil
            if ((employe.getProfil().getId() > 0) || employe.getProfil().getStatut() != null) {
                saveProfil();
            }
            listEmployes.add(0, employes);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateBean() {
        if (employe.getId() > 0) {
            //si le matricule à modifier est différent de l'ancien, on s'assure qu'il n'existe pas encore
            if ((employes.getMatricule() != null) ? !employes.getMatricule().equals(employe.getMatricule()) : false) {
                if (controleFiche(employe)) {
                    updateEmploye();
                }
            } else {
                if (employe.getStatutEmps().getId() > 0) {
                    updateEmploye();
                } else {
                    getErrorMessage("Vous devez indiquer le Statut de l'employé !");

                }
            }
        }
    }

    public void updateEmploye() {
        //enregistre un nouvel employé
        employes = UtilGrh.buildEmployeEntity(employe);
        if (employe.getAgence().getId() > 0) {
            employes.setAgence(new YvsAgences(employe.getAgence().getId()));
        } else {
            employes.setAgence(currentAgence);
        }
        employes.setAuthor(currentUser);
        if (employe.getPosteDeTravail().getId() > 0) {
            employes.setPosteActif(posteDeTravails.get(posteDeTravails.indexOf(new YvsGrhPosteDeTravail(employe.getPosteDeTravail().getId()))));
        }
        employes.setStatutEmps(saveStatut(employe.getStatutEmps().getId()));
        dao.update(employes);
        //si le poste de travail est donnée
        if (employe.getPosteDeTravail().getId() > 0) {
            employes.setPosteActif(savePosteEmps());
        }
        //si la catégorie et l'echelon sont données on enegistre la convention collective
        if (employe.getConvention().getCategorie().getId() != 0 && employe.getConvention().getEchelon().getId() != 0) {
            if (currentAgence.getSecteurActivite() != null) {
                //cherche la convetion collective rattaché
                champ = new String[]{"categorie", "echelon", "secteur"};
                val = new Object[]{new YvsGrhCategorieProfessionelle(employe.getConvention().getCategorie().getId()), new YvsGrhEchelons(employe.getConvention().getEchelon().getId()), new YvsGrhSecteurs(currentAgence.getSecteurActivite().getId())};
                YvsGrhConventionCollective cc = (YvsGrhConventionCollective) dao.loadOneByNameQueries("YvsConventionCollective.findByCE", champ, val);
                if (cc != null) {
                    //sauvegarde la nouvelle convetion d'un employé
                    saveConventionEMps(cc);
                }
            } else {
                getErrorMessage("Impossible d'enregistrer la catégorie de cet employé ! Aucun secteur n'a été trouvé pour cette agence !");
            }
        }

        //enregistre le profil
        if (employe.getProfil().getGrade() != null || employe.getProfil().getStatut() != null) {
            saveProfil();
        }
        int idx = listEmployes.indexOf(employes);
        if (idx >= 0) {
            listEmployes.set(idx, employes);
        }
        //Si l'employé est désactivé, désactiver aussi son contrat
        if (!employes.getActif()) {
            if (employes.getContrat() != null) {
                employes.getContrat().setActif(false);
                employes.getContrat().setAuthor(currentUser);
                dao.update(employes.getContrat());
                getInfoMessage("Le contrat de l'employé a été désactivé !");
            }
        }
    }

    public YvsGrhPosteDeTravail savePosteEmps() {
        //si le poste choisit est déjà enregistrer, on passe
        champ = new String[]{"employe", "poste"};
        val = new Object[]{employes, new YvsGrhPosteDeTravail(employe.getPosteDeTravail().getId())};
        YvsGrhPosteEmployes pe = (YvsGrhPosteEmployes) dao.loadOneByNameQueries("YvsPosteEmployes.findByIds", champ, val);
        if (pe == null) {
            YvsGrhPosteEmployes cpe = buildPosteDeTravail(employe);
            cpe.setId(null);
            String rq = "UPDATE yvs_grh_poste_employes SET actif=false WHERE employe=?";
            Options[] param = new Options[]{new Options(employes.getId(), 1)};
            dao.requeteLibre(rq, param);
            cpe.setStatut('C');
            cpe = (YvsGrhPosteEmployes) dao.save1(cpe);
            employe.getPosteDeTravail().setPosteActif(cpe.getId().intValue());
            employes.setPosteActif(posteDeTravails.get(posteDeTravails.indexOf(new YvsGrhPosteDeTravail(employe.getPosteDeTravail().getId()))));
            return employes.getPosteActif();
        }
        return pe.getPoste();
    }

    private YvsGrhPosteEmployes buildPosteDeTravail(Employe e) {
        YvsGrhPosteEmployes pe = new YvsGrhPosteEmployes();
        pe.setActif(true);
        pe.setDateAcquisition(new Date());
        pe.setEmploye(new YvsGrhEmployes(e.getId()));
        pe.setPoste(new YvsGrhPosteDeTravail(e.getPosteDeTravail().getId()));
        pe.setValider(true);
        pe.setDateConfirmation(new Date());
        pe.setDateDebut(new Date());
        pe.setIndemnisable(false);
        pe.setPostePrecedent(null);
        pe.setAgence(currentAgence);
        pe.setStatut('C');
        return pe;
    }

    public void saveConventionEMps(YvsGrhConventionCollective c) {
        //si le poste choisit est déjà enregistrer, on passe
        champ = new String[]{"employe"};
        val = new Object[]{employes};
        YvsGrhConventionEmploye cce = (YvsGrhConventionEmploye) dao.loadOneByNameQueries("YvsConventionEmploye.findByEmploye", champ, val);
        if (cce == null || ((cce != null)
                ? !(cce.getConvention().getEchelon().getId() == employe.getConvention().getEchelon().getId() && cce.getConvention().getCategorie().getId() == employe.getConvention().getCategorie().getId()) : false)) {
            YvsGrhConventionEmploye ce = new YvsGrhConventionEmploye();
            ce.setActif(true);
            ce.setConvention(c);
            ce.setEmploye(employes);
            ce.setSupp(false);
            ce.setDateChange(new Date());
            ce.setAuthor(currentUser);
            String rq = "UPDATE yvs_grh_convention_employe SET actif=false WHERE employe=?";
            Options[] param = new Options[]{new Options(employes.getId(), 1)};
            dao.requeteLibre(rq, param);
            ce = (YvsGrhConventionEmploye) dao.save1(ce);
            employe.getConvention().setId(ce.getId());
            employes.setConvention(c);
        }
    }

    public void chooseGradeEmploye(ValueChangeEvent ev) {
        if ((ev != null) ? ev.getNewValue() != null : false) {
            int id = (int) ev.getNewValue();
            if (id == -1) {
                openDialog("dlgAddGrade");
            } else if (id > 0) {
                grade = listGradeEmploye.get(listGradeEmploye.indexOf(new GradeEmploye(id)));
            }
        }
    }

    private YvsGrhStatutEmploye saveStatut(long id) {
        if (id > 0) {
            YvsGrhStatutEmploye st = new YvsGrhStatutEmploye(id);
            int idx = statutsEmployes.indexOf(st);
            boolean findHist;
            if (idx >= 0) {
                st = (YvsGrhStatutEmploye) dao.loadOneByNameQueries("YvsGrhStatutEmploye.findByStatut", new String[]{"statut"}, new Object[]{statutsEmployes.get(idx).getStatut()});
                if (st == null) {
                    st = new YvsGrhStatutEmploye(null, statutsEmployes.get(idx).getStatut(), statutsEmployes.get(idx).getLibelleStatut());
                    st.setAuthor(currentUser);
                    st = (YvsGrhStatutEmploye) dao.save1(st);
                    findHist = false;
                } else {
                    findHist = true;
                }
                //sauvegarde l'historique
                if (findHist) {
                    YvsGrhHistoriqueStatutEmploye hst = (YvsGrhHistoriqueStatutEmploye) dao.loadOneByNameQueries("YvsGrhHistoriqueStatutEmploye.findByStatEmps", new String[]{"statut", "employe"}, new Object[]{st, new YvsGrhEmployes(employe.getId())});
                    if (hst == null) {
                        hst = new YvsGrhHistoriqueStatutEmploye();
                        hst.setAuthor(currentUser);
                        hst.setDateChange(new Date());
                        hst.setEmploye(employes);
                        hst.setStatut(st);
                        dao.save(hst);
                    }
                }
            }
            return st;
        }
        return null;
    }

    public void saveProfil() {
        champ = new String[]{"employe", "profil"};
        val = new Object[]{employes, new YvsGrhProfil(employe.getProfil().getId())};
        YvsGrhProfilEmps pe = (YvsGrhProfilEmps) dao.loadOneByNameQueries("YvsGrhProfilEmps.findByEmploye", champ, val);
        if (pe == null) {
            //désactive d'abord tous les autres profils
            String rq = "UPDATE yvs_grh_profil_emps SET actif=false WHERE employe=?";
            Options[] param = new Options[]{new Options(employes.getId(), 1)};
            dao.requeteLibre(rq, param);
            YvsGrhProfilEmps proe = new YvsGrhProfilEmps();
            proe.setActif(true);
            proe.setDateChange(new Date());
            proe.setEmploye(employes);
            proe.setProfil((employe.getProfil().getId() > 0) ? new YvsGrhProfil(employe.getProfil().getId()) : null);
            if ((employe.getProfil().getGrade() != null) ? employe.getProfil().getGrade().getId() != 0 : false) {
                proe.setGrade(new YvsGrhGradeEmploye(employe.getProfil().getGrade().getId()));
            }
            proe.setAuthor(currentUser);
            proe = (YvsGrhProfilEmps) dao.save1(proe);
            employe.getProfil().setId(proe.getId());
        }
    }

    public void saveNewGrade() {
        if ((grade.getLibelle() != null) ? !"".equals(grade.getLibelle()) : false) {
            YvsGrhGradeEmploye entity = new YvsGrhGradeEmploye();
            entity.setLibelle(grade.getLibelle());
            entity.setSociete(currentAgence.getSociete());
            entity.setAuthor(currentUser);
            entity = (YvsGrhGradeEmploye) dao.save1(entity);
            grade.setId(entity.getId());
            employe.getProfil().setGrade(grade);
            listGradeEmploye.add(0, grade);
            grade = new GradeEmploye();
        } else {
            getErrorMessage("Vous devez entrer un libelle");
        }
    }

    @Override
    public Employe recopieView() {
        Employe emp = new Employe();
        cloneObject(emp, employe);
        emp.setConvention(employe.getConvention());
        emp.setCompteTiers(employe.getCompteTiers());
        emp.setCompteCollectif(employe.getCompteCollectif());
        emp.setLieuDelivCni(employe.getLieuDelivCni());
        emp.setPaysDorigine(employe.getPaysDorigine());
        emp.setVilleNaissance(employe.getVilleNaissance());
        emp.setUser(employe.getUser());
        emp.setProfil(employe.getProfil());
        if (employe.getPosteDeTravail().getId() > 0) {
            emp.setPosteDeTravail(UtilGrh.builSimpledBeanPoste(posteDeTravails.get(posteDeTravails.indexOf(employe.getPosteDeTravail()))));
        }
        emp.setPermis(employe.getPermis());
        return emp;
    }

    /**
     * Qualifications
     */
//    private String labBtnNew = "Créer";
    public String getChaineSelectQualification() {
        return chaineSelectQualification;
    }

    public void setChaineSelectQualification(String chaineSelectQualification) {
        this.chaineSelectQualification = chaineSelectQualification;
    }

    public boolean isDisplayDelQualif() {
        return displayDelQualif;
    }

    public void setDisplayDelQualif(boolean displayDelQualif) {
        this.displayDelQualif = displayDelQualif;
    }

    public Qualification getNewQualif() {
        return newQualif;
    }

    public void setNewQualif(Qualification newQualif) {
        this.newQualif = newQualif;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    public boolean isDisplayBtnRvQualif() {
        return displayBtnRvQualif;
    }

    public void setDisplayBtnRvQualif(boolean displayBtnRvQualif) {
        this.displayBtnRvQualif = displayBtnRvQualif;
    }

    public void selectQualification(SelectEvent ev) {
        qualification = UtilGrh.buildBeanQualification((YvsGrhQualifications) ev.getObject());
        YvsGrhQualifications q = (YvsGrhQualifications) ev.getObject();
        YvsGrhDomainesQualifications d = q.getDomaine();
        if (employe.getQualifications().contains(d)) {
            employe.getQualifications().get(employe.getQualifications().indexOf(d)).getQualifications().add(0, q);
        } else {
            YvsGrhDomainesQualifications nd = new YvsGrhDomainesQualifications();
            nd.setId(d.getId());
            nd.setQualifications(new ArrayList<YvsGrhQualifications>());
            nd.setTitreDomaine(d.getTitreDomaine());
            nd.setSociete(d.getSociete());
            nd.getQualifications().add(0, q);
            employe.getQualifications().add(0, nd);
        }
        update("emp-tab-qualif");
//            closeDialog("dlgQualification");            
//        }
    }

    public void unSelectQualification(UnselectEvent ev) {
        Qualification qa = (Qualification) ev.getObject();
        if (qa != null) {
            setQualification(new Qualification());
//            labBtnNew = "Créer";
            updateQualif = false;
            displayBtnRvQualif = false;
            update("mainEmps:employe-select-qualification");
//            closeDialog("dlgQualification");            
        }
    }

    public void RemoveOneDomaine() {
        if (newQualif.getDomaine().getId() > 0) {
            try {
                YvsGrhDomainesQualifications q = new YvsGrhDomainesQualifications(newQualif.getDomaine().getId());
                q.setAuthor(currentUser);
                dao.delete(q);
                domainesQualification.remove(q);
                update("txt_domaine_qualifC");

            } catch (Exception ex) {
                Logger.getLogger(ManagedPosteDeTravail.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void createNewQualification() {
        if ((newQualif.getIntitule() != null) ? !newQualif.getIntitule().trim().equals("") : false) {
            YvsGrhQualifications qa = new YvsGrhQualifications();
            qa.setDesignation(newQualif.getIntitule());
            qa.setCodeInterne(newQualif.getCodeInterne());
            qa.setDomaine(new YvsGrhDomainesQualifications(newQualif.getDomaine().getId()));
            qa.setSupp(false);
            qa.setAuthor(currentUser);
            if (!updateQualif) {
                qa = (YvsGrhQualifications) dao.save1(qa);
                if (!domainesQualification.contains(qa.getDomaine())) {
                    qa.getDomaine().getQualifications().add(qa);
                    domainesQualification.add(0, qa.getDomaine());
                } else {
                    domainesQualification.get(domainesQualification.indexOf(qa.getDomaine())).getQualifications().add(qa);
                }
            } else {
                qa.setId(newQualif.getId());
                dao.update(qa);
                domainesQualification.get(domainesQualification.indexOf(qa.getDomaine())).getQualifications().set(domainesQualification.get(domainesQualification.indexOf(qa.getDomaine())).getQualifications().indexOf(qa), qa);
            }
            newQualif = new Qualification();
            qualification.setId(qa.getId());
            updateQualif = false;
        }
    }

    public void delteQualif(YvsGrhQualifications q) {
        try {
            q.setAuthor(currentUser);
            dao.delete(q);
            domainesQualification.get(domainesQualification.indexOf(q.getDomaine())).getQualifications().remove(q);
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet élément");
        }
    }

    //ajoute une qualification à la liste des qualifications d'un employé
    public void addQualification() {
        if (employes != null) {
            if ((qualification != null) ? (qualification.getId() > 0 && qualification.getDomaine().getId() > 0) : false) {
                try {
                    YvsGrhQualificationEmploye qe = new YvsGrhQualificationEmploye();
                    qe.setDateAcquisition(new Date());
                    qe.setQualification(new YvsGrhQualifications(qualification.getId()));
                    qe.setEmploye(employes);
                    qe.setAuthor(currentUser);
                    qe = (YvsGrhQualificationEmploye) dao.save1(qe);
                    qe.getQualification().setId(qe.getId());
                    YvsGrhDomainesQualifications dq = new YvsGrhDomainesQualifications(qualification.getDomaine().getId());
                    if (!employe.getQualifications().contains(dq)) {
                        dq.getQualifications().add(qe.getQualification());
                        employe.getQualifications().add(dq);
                    } else {
                        employe.getQualifications().get(employe.getQualifications().indexOf(new YvsGrhDomainesQualifications(qualification.getId()))).getQualifications().add(qe.getQualification());
                    }
                    update("mainEmps:tabQual");
                } catch (Exception ex) {
                    getMessage("Erreur ! Cette qualification est déja attribué !", FacesMessage.SEVERITY_ERROR);
                    Logger.getLogger(ManagedEmployes.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                getErrorMessage("Le formulaire d'enregistrement d'une nouvelle qualification est erronné !");
            }
        } else {
            getErrorMessage("Formulaire incomplet");
        }
    }

    public void selectedQualif(SelectEvent ev) {
        Qualification p = (Qualification) ev.getObject();
        cloneObject(qualification, p);
//        update("mainEmps:emp-panel-qualif");
    }

    public void deleteQualif(Qualification p) {
        try {
            YvsGrhQualificationEmploye q = new YvsGrhQualificationEmploye();
//            dao.delete(q);
//            employe.getQualifications().remove(q);
            update("tabQual");
        } catch (Exception e) {
            getMessage("Erreur de suppression !", FacesMessage.SEVERITY_ERROR);
            Logger.getLogger(ManagedEmployes.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * End Qualification
     */
    /**
     * Diplômes
     */
    private List<Diplomes> selectionDiplome;
    private boolean displayDelDiplome;

    public List<Diplomes> getSelectionDiplome() {
        return selectionDiplome;
    }

    public void setSelectionDiplome(List<Diplomes> selectionDiplome) {
        this.selectionDiplome = selectionDiplome;
    }

    public boolean isDisplayDelDiplome() {
        return displayDelDiplome;
    }

    public void setDisplayDelDiplome(boolean displayDelDiplome) {
        this.displayDelDiplome = displayDelDiplome;
    }

    public void addDiplome() {
        if (employes != null) {
            if (diplome.getId() != 0) {
                try {
                    YvsDiplomeEmploye dipe = new YvsDiplomeEmploye(new YvsDiplomeEmployePK((int) diplome.getId(), employes.getId().intValue()));
                    dipe.setClassement((short) diplome.getNiveau());
                    dipe.setDateObtention(diplome.getDateObtention());
                    dipe.setEcole(diplome.getEcole());
                    dipe.setMention(diplome.getMention());
                    dipe.setEmploye(employes);
                    dao.update(dipe);
                    Diplomes d = diplomes.get(diplomes.indexOf(diplome));
                    d.setNiveau(diplome.getNiveau());
                    d.setDateObtention(diplome.getDateObtention());
                    d.setEcole(diplome.getEcole());
                    d.setMention(diplome.getMention());
                    if (!employe.getDiplomes().contains(dipe)) {
                        employe.getDiplomes().add(dipe);
                    } else {
                        employe.getDiplomes().set(employe.getDiplomes().indexOf(dipe), dipe);
                    }
                    update("tabDiplome");
                } catch (Exception e) {
                    getMessage("Non enregistré. ce diplôme est déjà affecté à l'employé", FacesMessage.SEVERITY_ERROR);
                }
            } else {
                getMessage("Formulaire Incomplet, Veuillez choisir le diplôme", FacesMessage.SEVERITY_ERROR);
            }
        } else {
            getErrorMessage("Formulaire Incomplet !");
        }

    }

    public void selectedDiplome(SelectEvent ev) {
        Diplomes p = (Diplomes) ev.getObject();
        cloneObject(diplome, p);
        update("mainEmps:emp-panel-diplome");
        displayDelDiplome = !selectionDiplome.isEmpty();
    }

    public void unSelectDiplome(UnselectEvent ev) {
        displayDelDiplome = !selectionDiplome.isEmpty();
        update("mainEmps:emp-panel-diplome");
    }

    public void resetFormDiplome() {
        diplome = new Diplomes();
        update("mainEmps:emp-panel-diplome");
    }

    public void deleteDiplome(Diplomes d) {
        try {
            for (Diplomes p : selectionDiplome) {
                dao.delete(new YvsDiplomeEmploye(new YvsDiplomeEmployePK((int) p.getId(), employes.getId().intValue())));
                employe.getDiplomes().remove(p);
            }
            update("mainEmps:tabDiplome");
            resetFormDiplome();
            update("mainEmps:emp-panel-diplome");
        } catch (Exception e) {
            getMessage("Erreur de suppression !", FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * End Diplômes
     *
     * @param ev
     */
    /*--- Langues---*/
    private List<Langues> seletionLangue;
    private boolean displayDelLangue;

    public List<Langues> getSeletionLangue() {
        return seletionLangue;
    }

    public void setSeletionLangue(List<Langues> seletionLangue) {
        this.seletionLangue = seletionLangue;
    }

    public boolean isDisplayDelLangue() {
        return displayDelLangue;
    }

    public void setDisplayDelLangue(boolean displayDelLangue) {
        this.displayDelLangue = displayDelLangue;
    }

//    public void choixLangues(ValueChangeEvent ev) {
//        if (ev.getNewValue() != null) {
//            int id = (int) ev.getNewValue();
//            if (id == -1) {
//                openDialog("dlgLangue");
//            } else if (id > 0) {
//                langue = listLangues.get(listLangues.indexOf(new Langues(id)));
//            }
//        }
//    }
    public void selectLangue(SelectEvent ev) {
        Langues la = (Langues) ev.getObject();
        if (la != null) {
//            if (!listLangues.contains(la)) {
//                listLangues.add(la);
//            }
            langue = la;
            update("mainEmps:employe-select-langue");
            closeDialog("dlgLangue");
        }
    }

    public void createNewLangue() {
        if (newLangue != null) {
            YvsLangues l = new YvsLangues();
            l.setNom(newLangue);
            l.setSociete(currentAgence.getSociete());
            l = (YvsLangues) dao.save1(l);
            langue.setId(l.getId());
            langue.setLangue(newLangue);
            listLangues.add(0, langue);
            langue = new Langues();
        }
    }

    public void addLangue() {
        if (employes != null) {
            if (langue.getId() != 0) {
                try {
                    langue.setLangue(listLangues.get(listLangues.indexOf(langue)).getLangue());
                    YvsLangueEmps le = new YvsLangueEmps(new YvsLangueEmpsPK(langue.getId(), employes.getId().intValue()));
                    le.setEcrit(langue.isEcrit());
                    le.setLu(langue.isLu());
                    le.setParle(langue.isParle());
                    le.setEmploye(employes);
                    le.setLangue(new YvsLangues(langue.getId(), langue.getLangue()));
                    dao.update(le);
                    if (employe.getLangues().contains(le)) {
                        employe.getLangues().set(employe.getLangues().indexOf(le), le);
                    } else {
                        employe.getLangues().add(le);
                    }
                    langue = new Langues();
                } catch (Exception e) {
                    getMessage("Non enregistré. cette langue est déjà affecté à l'employé", FacesMessage.SEVERITY_ERROR);
                }
            }
        }
    }

    public void deleteLangue(YvsLangueEmps p) {
        try {
            dao.delete(p);
            employe.getLangues().remove(p);
            update("mainEmps:tabLangue");
        } catch (Exception e) {
            getMessage("Erreur de suppression !", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void selectedLangue(SelectEvent ev) {
        YvsLangueEmps p = (YvsLangueEmps) ev.getObject();
        cloneObject(langue, UtilGrh.buildBeanLangue(p));
        displayDelLangue = !seletionLangue.isEmpty();
        update("mainEmps:emp-panel-langue");
    }

    public void unSelectLangue(UnselectEvent ev) {
        displayDelLangue = !seletionLangue.isEmpty();
        update("mainEmps:emp-panel-langue");
    }

    public void loadLangue() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listLangues.clear();
        List<YvsLangues> lL = dao.loadNameQueries("YvsLangues.findAll", champ, val);
        for (YvsLangues c : lL) {
            listLangues.add(new Langues(c.getId(), c.getNom()));
        }
    }

    public void choixEmploye(SelectEvent ev) {
        YvsGrhEmployes e = (YvsGrhEmployes) ev.getObject();
        choixEmploye1(e);
        listSelection.clear();
        listSelection.add(e);

    }

    @Override
    public void onSelectObject(YvsGrhEmployes y) {
        employes = y;
        Employe e = UtilGrh.buildAllOnlyEMploye(employes);
        e.setUser((employes.getCodeUsers() != null) ? UtilUsers.buildSimpleBeanUsers(employes.getCodeUsers()) : new Users());
        populateView(e);
        loadCollectionsEmploye(y);
        employe.setSectionsAnalytiques(dao.loadNameQueries("YvsComptaAffecAnalEmp.findByEmploye", new String[]{"employe"}, new Object[]{employes}));
        if (employes.getStatutEmps() != null) {
            for (YvsGrhStatutEmploye st : statutsEmployes) {
                if (employes.getStatutEmps().getStatut().equals(st.getStatut())) {
                    employe.setStatutEmps(new StatutEmps(st.getId(), st.getStatut()));
                }
            }
        }
        codeExterne = y.getCodeExterne() != null ? y.getCodeExterne().getCodeExterne() : "";
        update = true;
    }

    public void choixEmploye1(YvsGrhEmployes ev) {
        onSelectObject(ev);
    }

    @Override
    public void populateView(Employe bean) {
        cloneObject(employe, bean);
    }

    private void loadCollectionsEmploye(YvsGrhEmployes y) {
        employe.setComptesBancaires(dao.loadNameQueries("YvsCompteBancaire.findByEmploye", new String[]{"employe"}, new Object[]{y}));
        employe.setContacts(dao.loadNameQueries("YvsContactsEmps.findByEmploye", new String[]{"employe"}, new Object[]{y}));
        employe.setConvention(UtilGrh.buildConvention((YvsGrhConventionCollective) dao.loadOneByNameQueries("YvsConventionEmploye.findOneCByEmploye", new String[]{"employe"}, new Object[]{y})));
        if (employe.getConvention().getCategorie().getId() > 0) {
            employe.getConvention().getCategorie().setListEchelons(UtilGrh.buildBeanEchelonsByEch(dao.loadNameQueries("YvsConventionCollective.findEchelon", new String[]{"categorie"}, new Object[]{new YvsGrhCategorieProfessionelle(employe.getConvention().getCategorie().getId())})));
        }
        employe.setPersonneEnCharge(dao.loadNameQueries("YvsGrhPersonneEnCharge.findByEmploye", new String[]{"employe"}, new Object[]{y}));
        employe.setNombreEnfant(employe.getPersonneEnCharge().size());
        employe.setPermis(dao.loadNameQueries("YvsPermisDeCoduire.findByEmploye", new String[]{"employe"}, new Object[]{y}));
        employe.setLangues(dao.loadNameQueries("YvsLangueEmps.findByEmploye", new String[]{"employe"}, new Object[]{y.getId()}));
        employe.setDiplomes(dao.loadNameQueries("YvsDiplomeEmploye.findByEmploye", new String[]{"employe"}, new Object[]{y.getId()}));
//            //charge les qualifications
        employe.setQualifications(UtilGrh.ordonneQualificationEmploye(dao.loadNameQueries("YvsQualificationEmploye.findByEmploye", new String[]{"employe"}, new Object[]{y})));
//            em.setDocuments(e.getYvsGrhDocumentEmpsList());
    }
    /**
     * Employé charge les employé en gérant la pagination
     */
    int offsetEmps;
    private boolean disPrevEmps = true, disNextEmps;

    public boolean isDisNextEmps() {
        return disNextEmps;
    }

    public boolean isDisPrevEmps() {
        return disPrevEmps;
    }

    public void setDisPrevEmps(boolean disPrevEmps) {
        this.disPrevEmps = disPrevEmps;
    }

    public void setDisNextEmps(boolean disNextEmps) {
        this.disNextEmps = disNextEmps;
    }
    int numroDroit;

    public int giveNumDroitAccesDossierEmp() {
        if (autoriser("point_validFicheAllScte")) {
            numroDroit = 0;
        } else if (autoriser("point_validFicheAgence")) {
            numroDroit = 1;
        } else if (autoriser("point_validFicheDepartement")) {
            numroDroit = 2;
        } else if (autoriser("point_validFicheEquipe")) {
            numroDroit = 3;
        } else {
            numroDroit = 4;
        }
        return numroDroit;
    }

    private void buildParamSubDep(long idDepartement) {
        if (idDepartement > 0) {
            //récupère le département donné en paramètre
            listIdSubDepartement.clear();
            giveAllSupDepartement((YvsGrhDepartement) dao.loadOneByNameQueries("YvsGrhDepartement.findById", new String[]{"id"}, new Object[]{idDepartement}));
            if (!listIdSubDepartement.isEmpty()) {
                champ = new String[]{"departement"};
                val = new Object[]{listIdSubDepartement};
                nameQueriCount = "YvsGrhEmployes.findByServiceINC";
                nameQueri = "YvsGrhEmployes.findByServiceIN";
            }
        }

    }

    public long countAllEmploye(long plage) {    //la plage précise si on fait un appel basé sur les droit natif ou basé sur un filtre  (0 ou -1=droit natif, !=0 id du département parent)
        giveNumDroitAccesDossierEmp();
        long nb;
        switch (numroDroit) {
            case 0:
                if (plage == 0 || plage == -1) {
                    champ = new String[]{"societe"};
                    val = new Object[]{currentUser.getAgence().getSociete()};
                    nameQueriCount = "YvsGrhEmployes.findCountAlls";
                    nameQueri = "YvsGrhEmployes.findAlls";
                } else {
                    buildParamSubDep(plage);
                }
                break;
            case 1:
                if (plage == 0 || plage == -1) {
                    champ = new String[]{"agence"};
                    val = new Object[]{currentUser.getAgence()};
                    nameQueriCount = "YvsGrhEmployes.findCountAll";
                    nameQueri = "YvsGrhEmployes.findAll";
                } else {
                    buildParamSubDep(plage);
                }
                break;
            case 2://départements
                if (currentUser.getUsers().getEmploye() != null) {
                    if (currentUser.getUsers().getEmploye().getPosteActif() != null) {
                        if (plage == 0 || plage == -1) {
                            listIdSubDepartement.clear();
                            giveAllSupDepartement(currentUser.getUsers().getEmploye().getPosteActif().getDepartement());
                            if (!listIdSubDepartement.isEmpty()) {
                                champ = new String[]{"departement"};
                                val = new Object[]{listIdSubDepartement};
                                nameQueriCount = "YvsGrhEmployes.findByServiceINC";
                                nameQueri = "YvsGrhEmployes.findByServiceIN";
                            } else {
                                return 0;
                            }
                        } else {
                            buildParamSubDep(plage);
                        }
                    } else {
                        getErrorMessage("Aucun Département n'est spécifié pour l'employé de ce code utilisateur !");
                        return 0;
                    }
                } else {
                    getErrorMessage("Aucun Employé n'est rattaché à ce compte utilisateur!");
                    return 0;
                }
                break;
            case 3:
                if (currentUser.getUsers().getEmploye() != null) {
                    champ = new String[]{"equipe"};
                    val = new Object[]{currentUser.getUsers().getEmploye().getEquipe()};
                    nameQueriCount = "YvsGrhEmployes.findByEquipeC";
                    nameQueri = "YvsGrhEmployes.findByEquipe";
                } else {
                    getErrorMessage("Aucun Département n'est spécifié pour l'employé de ce code utilisateur !");
                    return 0;
                }
                break;
            case 4:
                champ = new String[]{"user"};
                val = new Object[]{currentUser.getUsers()};
                nameQueriCount = "YvsGrhEmployes.findByUsersC";
                nameQueri = "YvsGrhEmployes.findByUsers";
                break;
            default:
                return 0;
        }
        nb = (Long) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
        return nb;
    }

    private void buildParamRequete() {
        giveNumDroitAccesDossierEmp();
        ParametreRequete p;
        switch (numroDroit) {
            case 0:
                p = new ParametreRequete("y.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
                paginator.addParam(p);
                break;
            case 1:
                p = new ParametreRequete("y.agence", "agence", currentUser.getAgence(), "=", "AND");
                paginator.addParam(p);
                break;
            case 2://départements
                if (currentUser.getUsers().getEmploye() != null) {
                    if (currentUser.getUsers().getEmploye().getPosteActif() != null) {
                        listIdSubDepartement.clear();
                        giveAllSupDepartement(currentUser.getUsers().getEmploye().getPosteActif().getDepartement());
                        if (!listIdSubDepartement.isEmpty()) {
                            p = new ParametreRequete("y.posteActif.departement.id", "departements", listIdSubDepartement, " IN ", "AND");
                            paginator.addParam(p);
                        }
                    } else {
                        getErrorMessage("Aucun Département n'est spécifié pour l'employé de ce code utilisateur !");
                    }
                } else {
                    getErrorMessage("Aucun Employé n'est rattaché à ce compte utilisateur!");
                }
                break;
            case 3:
                if (currentUser.getUsers().getEmploye() != null) {
                    p = new ParametreRequete("y.equipe", "equipe", currentUser.getUsers().getEmploye().getEquipe(), "=", "AND");
                    paginator.addParam(p);

                } else {
                    getErrorMessage("Aucun Département n'est spécifié pour l'employé de ce code utilisateur !");
                }
                break;
            case 4:
                p = new ParametreRequete("y.codeUsers", "users", currentUser.getUsers(), "=", "AND");
                paginator.addParam(p);
                break;
            default:
                break;
        }
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbResult())) {
            setOffset(0);
        }
        String query = "YvsGrhEmployes y JOIN FETCH y.agence LEFT JOIN FETCH y.posteActif LEFT JOIN FETCH y.contrat LEFT JOIN FETCH y.profil "
                + "LEFT JOIN FETCH y.pays LEFT JOIN FETCH y.lieuNaissance LEFT JOIN FETCH y.lieuDelivCni LEFT JOIN FETCH y.codeExterne LEFT JOIN FETCH y.codeUsers LEFT JOIN FETCH y.equipe "
                + "LEFT JOIN FETCH y.compteCollectif LEFT JOIN FETCH y.compteTiers";

        List<YvsGrhEmployes> re = paginator.parcoursDynamicData(query, "y", "", "y.nom ASC, y.prenom", getOffset(), dao);
        if (!re.isEmpty()) {
            choixEmploye1(re.get(0));
            update("employe-main-panel");
            update("delete_employe");
        }
    }

    public void loadAllEmployesByAgence(boolean avancer, boolean init) {
        buildParamRequete();
        paginator.addParam(new ParametreRequete("y.agence.mutuelle", "mutuelle", null, "=", "AND"));
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        String query = "YvsGrhEmployes y JOIN FETCH y.agence LEFT JOIN FETCH y.posteActif LEFT JOIN FETCH y.profil "
                + "LEFT JOIN FETCH y.pays LEFT JOIN FETCH y.lieuNaissance LEFT JOIN FETCH y.lieuDelivCni LEFT JOIN FETCH y.codeExterne LEFT JOIN FETCH y.codeUsers LEFT JOIN FETCH y.equipe "
                + "LEFT JOIN FETCH y.compteCollectif LEFT JOIN FETCH y.compteTiers";
        listEmployes = paginator.executeDynamicQuery("DISTINCT y", "DISTINCT y", query, orderList, avancer, init, (int) imax, dao);
        optionSearch = 1;
    }

    public void changeOrderList(String order) {
        this.orderList = order;
        loadAllEmployesByAgence(false, false);
    }

    public void loadSimpleAllEmployes(boolean avancer, boolean init) {
        buildParamRequete();
        paginator.addParam(new ParametreRequete("y.agence.mutuelle", "mutuelle", null, "=", "AND"));
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        String query = "YvsGrhEmployes y JOIN FETCH y.agence LEFT JOIN FETCH y.contrat LEFT JOIN FETCH y.posteActif ";
        listEmployes = paginator.executeDynamicQuery("y", "y", query, orderList, avancer, init, (int) imax, dao);
        optionSearch = 1;
    }

    public void addParamMutuelle() {
        if (currentMutuel != null ? currentMutuel.getId() > 0 : false) {
            paginator.addParam(new ParametreRequete("y.agence.mutuelle", "mutuelle", currentMutuel, "=", "AND"));
            addParamActif(true);
        }
    }

    public void pagineResult(boolean avancer) {
        loadAllEmployesByAgence(avancer, false);
        optionSearch = 1;
    }

    public void pagineSimpleResult(boolean avancer) {
        loadSimpleAllEmployes(avancer, false);
        optionSearch = 1;
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllEmployesByAgence(true, true);
    }

    public void changeMaxResult(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            imax = (long) ev.getNewValue();
            loadAllEmployesByAgence(true, true);
        }
    }

    public void changeSimpleMaxResult(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            imax = (long) ev.getNewValue();
            loadSimpleAllEmployes(true, true);
        }
    }

    public List<YvsGrhEmployes> buildListEmployeBean(List<YvsGrhEmployes> l) {
//        champ = new String[]{"employe", "date", "statut"};
//        for (YvsGrhEmployes e : l) {
//            val = new Object[]{e, new Date(), 'V'};
//            YvsGrhCongeEmps c = (YvsGrhCongeEmps) dao.loadOneByNameQueries("YvsGrhCongeEmps.findByEmpsAndDate", champ, val);
//            if (c != null) {
//                e.setConge(new YvsGrhCongeEmps());
//                e.getConge().setLibelle(c.getLibelle());
//                e.getConge().setEffet(Utilitaire.countDayBetweenDate(new Date(), c.getDateFin()) + "");
//            }
//            List<YvsGrhConventionEmploye> list = dao.loadNameQueries("YvsConventionEmploye.findByEmploye", new String[]{"employe"}, new Object[]{e});
//            for (YvsGrhConventionEmploye cc : list) {
//                if (cc.getActif()) {
//                    e.setConvention(cc.getConvention());
//                }
//            }
//        }
        return l;
    }

    public void addParamAgence(ValueChangeEvent ev) {
        addParamAgence_((Long) ev.getNewValue());
    }

    public void addSimpleParamAgence(ValueChangeEvent ev) {
        addSimpleParamAgence_((Long) ev.getNewValue());
    }

    public void addParamAgence_(Long idAgence) {
        ParametreRequete p;
        if (idAgence != null) {
            p = new ParametreRequete("y.agence", "agence", (idAgence > 0) ? new YvsAgences(idAgence) : null, "=", "AND");

        } else {
            p = new ParametreRequete("y.agence", "agence", null, "=", "AND");
        }
        paginator.addParam(p);
        loadAllEmployesByAgence(true, true);
    }

    public void addSimpleParamAgence_(Long idAgence) {
        ParametreRequete p;
        if (idAgence != null) {
            p = new ParametreRequete("y.agence", "agence", (idAgence > 0) ? new YvsAgences(idAgence) : null, "=", "AND");

        } else {
            p = new ParametreRequete("y.agence", "agence", null, "=", "AND");
        }
        paginator.addParam(p);
        loadSimpleAllEmployes(true, true);
    }

    public void addParamActif_(ValueChangeEvent ev) {
        addParamActif((Boolean) ev.getNewValue());
    }

    public void addParamActif(Boolean actif) {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", actif, "=", "AND");
        paginator.addParam(p);
        loadAllEmployesByAgence(true, true);
    }

    public void addParamActifSimple(Boolean actif) {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", actif, "=", "AND");
        paginator.addParam(p);
        loadSimpleAllEmployes(true, true);
    }

    public void loadEmployeByProfil(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.profil", "profil", null, "=", "AND");
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                p.setObjet(new YvsGrhProfil(id));
            }
        }
        paginator.addParam(p);
        loadAllEmployesByAgence(true, true);
    }

    public void addParamPoste(String intitule) {
        ParametreRequete p = new ParametreRequete("UPPER(y.posteActif.intitule)", "posteA", "%" + intitule.toUpperCase() + "%", " LIKE ", "AND");
        if (Util.asString(intitule)) {

        } else {
            p.setObjet(null);
        }
        paginator.addParam(p);
        loadAllEmployesByAgence(true, true);
    }

    public void loadSimpleEmployeByProfil(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.profil", "profil", null, "=", "AND");
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                p.setObjet(new YvsGrhProfil(id));
            }
        }
        paginator.addParam(p);
        loadSimpleAllEmployes(true, true);
    }

    public void chooseDateSearch(ValueChangeEvent ev) {
        paramDate = (Boolean) ev.getNewValue();
        addParaDate(paramDate);
    }

    public void addParamDate1(SelectEvent ev) {
        addParaDate(paramDate);
    }

    public void addParamDate2() {
        addParaDate(paramDate);
    }

    private void addParaDate(boolean b) {
        ParametreRequete p = new ParametreRequete("y.dateSave", "dateSave", null, " BETWEEN ", "AND");
        if (b) {
            if (dateDebut != null && dateFin != null) {
                if (dateDebut.before(dateFin) || dateDebut.equals(dateFin)) {
                    p.setObjet(dateDebut);
                    p.setOtherObjet(dateFin);
                }
            }
        }
        paginator.addParam(p);
        loadAllEmployesByAgence(true, true);

    }

    public void findEmployeByDepartement(String service) {
        ParametreRequete p = new ParametreRequete("y.posteActif", "service", null, "LIKE", "AND");
        if (Util.asString(service)) {
            p = new ParametreRequete(null, "service", "service", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.posteActif.departement.intitule)", "service", "%" + service.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.posteActif.departement.description)", "service", "%" + service.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllEmployesByAgence(true, true);
    }

    public void findEmployeByCodeTiers(String service) {
        ParametreRequete p = new ParametreRequete("y.compteTiers.codeTiers", "tiers", null, "LIKE", "AND");
        if (Util.asString(service)) {
            p = new ParametreRequete(null, "tiers", "tiers", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.compteTiers.codeTiers)", "codeTiers", "%" + service.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.compteTiers.nom)", "nomT", "%" + service.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.compteTiers.prenom)", "prenomT", "%" + service.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllEmployesByAgence(true, true);
    }

    public void loadEmployeWithoutContrat(ValueChangeEvent ev) {
        if (ev != null) {
            int v = (int) ev.getNewValue();
            switch (v) {
                case 2:
                    //employés ne disposant pas de contrat
                    champ = new String[]{"societe"};
                    val = new Object[]{currentUser.getAgence().getSociete()};
                    String rq = "SELECT e.id FROM yvs_grh_employes e LEFT JOIN yvs_grh_contrat_emps c ON e.id=c.employe INNER JOIN"
                            + " yvs_agences a on a.id=e.agence "
                            + " WHERE c.id IS NULL AND a.societe=?";
                    List<Long> ids = dao.loadListBySqlQuery(rq, new Options[]{new Options(currentUser.getAgence().getSociete().getId(), 1)});
                    if (ids.isEmpty()) {
                        ids.add(0L);
                    }
                    paginator.getParams().clear();
                    paginator.addParam(new ParametreRequete("y.id", "ids", ids, "IN", "AND"));
                    loadAllEmployesByAgence(true, true);
                    break;
                default:
                    paginator.addParam(new ParametreRequete("y.id", "ids", null, "IN", "AND"));
                    loadAllEmployesByAgence(true, true);
            }
        }
    }

    /**
     * Permis de conduire
     */
    private boolean updatePermi;
    private boolean displayDelPermis;

    public boolean isDisplayDelPermis() {
        return displayDelPermis;
    }

    public void setDisplayDelPermis(boolean displayDelPermis) {
        this.displayDelPermis = displayDelPermis;
    }

    public void addPermisDeConduire() {
        if (employes != null) {
            if (permis.getCategorie() != null && permis.getNumero() != null) {
                YvsPermisDeCoduire permi = new YvsPermisDeCoduire();
                permi.setCategorie(permis.getCategorie());
                permi.setDateExpiration(permis.getDateExp());
                permi.setDateObtention(permis.getDateObtention());
                permi.setNumero(permis.getNumero());
                permi.setEmploye(employes);
                PermisDeConduire p = UtilGrh.buildBeanPermis(permi);
                p.setExpire(permi.getDateExpiration().before(new Date()));
                if (!updatePermi) {
                    permi = (YvsPermisDeCoduire) dao.save1(permi);
                    permis.setId(permi.getId());
                    p.setId(permi.getId());
                    employe.getPermis().add(permi);
                } else {
                    permi.setId(permis.getId());
                    dao.update(permi);
                    employe.getPermis().set(employe.getPermis().indexOf(permi), permi);
                }
                update("mainEmps:emp-tab-permi");
                resetFormPermis();
                update("mainEmps:emp-panel-permi");
            } else {
                getMessage("Formulaire Incomplet, Veuillez reseigner la catégorie et le numéro de permis", FacesMessage.SEVERITY_ERROR);
            }
        } else {
            getErrorMessage("Formulaire incomplet");
        }

    }

    public void selectPermi(SelectEvent ev) {
        YvsPermisDeCoduire p = (YvsPermisDeCoduire) ev.getObject();
        cloneObject(permis, UtilGrh.buildBeanPermis(p));
        updatePermi = true;
        update("mainEmps:emp-panel-permi");
    }

    public void resetFormPermis() {
        permis = new PermisDeConduire();
        update("mainEmps:emp-panel-permi");
        updatePermi = false;
    }

    public void deletePermis(YvsPermisDeCoduire p) {
        try {
            dao.delete(p);
            employe.getPermis().remove(p);
            update("mainEmps:emp-tab-permi");
        } catch (Exception e) {
            getMessage("Erreur de suppression !", FacesMessage.SEVERITY_ERROR);
        }
    }

    /**/
    //personnes en charge et contact
    /**/
    private boolean updatePersonne, displayDelPersonne, updateContact;
    private List<CompteBancaire> selectionCompteBancaire;

    public boolean isDisplayDelPersonne() {
        return displayDelPersonne;
    }

    public void setUpdateContact(boolean updateContact) {
        this.updateContact = updateContact;
    }

    public boolean isUpdateContact() {
        return updateContact;
    }

    public List<CompteBancaire> getSelectionCompteBancaire() {
        return selectionCompteBancaire;
    }

    public void setSelectionCompteBancaire(List<CompteBancaire> selectionCompteBancaire) {
        this.selectionCompteBancaire = selectionCompteBancaire;
    }

    public void setDisplayDelPersonne(boolean displayDelPersonne) {
        this.displayDelPersonne = displayDelPersonne;
    }

    public void selectPersonne(SelectEvent ev) {
        PersonneEnCharge p = (PersonneEnCharge) ev.getObject();
        cloneObject(personne, p);
        updatePersonne = true;
        update("mainEmps:tab_pers_tiers:tab_pers_charg");
    }

    public void selectContact(SelectEvent ev) {
        ContactEmps p = (ContactEmps) ev.getObject();
        updateContact = true;
        cloneObject(contactEmps, p);
        update("mainEmps:tab_pers_tiers:tab_contact_emps");
    }

    public void resetFormPersonne() {
        personne = new PersonneEnCharge();
        updatePersonne = false;
    }

    public void resetFormContact() {
        contactEmps = new ContactEmps();
        update("mainEmps:tab_pers_tiers:emp-panel-contac");
        update("mainEmps:tab_pers_tiers:tab-contact-emps");
        updateContact = false;
    }

    public void deletePersonne(PersonneEnCharge p) {
        try {
            dao.delete(new YvsGrhPersonneEnCharge(p.getId()));
//            employe.getPersonneEnCharge().remove(p);
        } catch (Exception e) {
            getMessage("Erreur de suppression !", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void addPersonneEnCharge() {
        if (employes != null) {
            if (personne.getNom() != null) {
                YvsGrhPersonneEnCharge p = UtilGrh.getPersonne(personne);
                p.setEmploye(employes);
                if (!updatePersonne) {
                    p.setId(null);
                    p = (YvsGrhPersonneEnCharge) dao.save1(p);
                    personne.setId(p.getId());
                    employe.getPersonneEnCharge().add(p);
                } else {
                    dao.update(p);
                    employe.getPersonneEnCharge().set(employe.getPersonneEnCharge().indexOf(p), p);
                }
                resetFormPersonne();
            }
        }
    }

    public void deleteContact(YvsContactsEmps p) {
        try {
            p.setDateUpdate(new Date());
            p.setAuthor(currentUser);
            dao.delete(p);
            employe.getContacts().remove(p);
        } catch (Exception e) {
            getMessage("Erreur de suppression !", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void addContact() {
        if (employes != null) {
            if (contactEmps.getNom() != null) {
                YvsContactsEmps p = UtilGrh.getContact(contactEmps);
                p.setEmploye(employes);
                p.setDateUpdate(new Date());
                p.setAuthor(currentUser);
                if (!updateContact) {
                    p.setDateSave(new Date());
                    p.setId(null);
                    p = (YvsContactsEmps) dao.save1(p);
                    contactEmps.setId(p.getId());
                    employe.getContacts().add(p);
                } else {
                    dao.update(p);
                    employe.getContacts().set(employe.getContacts().indexOf(p), p);
                }
                resetFormContact();
            } else {
                getErrorMessage("Vous devez entrer le nom");
            }
        } else {
            getErrorMessage("Vous devez dabor enregistrer l'employe");
        }
    }

    //charge les données de base util pour l'enregistrement d'un nouvel employé
    @Override
    public void loadAll() {
        loadVille(100, false);
        loadPays(100, false);
        //liste des catégories professionnelles
        loadAllCategorie();
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        //liste des langues
//        listLangues.clear();
//        List<YvsLangues> lL = dao.loadNameQueries("YvsLangues.findAll", champ, val, 0, 50);
//        for (YvsLangues c : lL) {
//            listLangues.add(new Langues(c.getId(), c.getNom()));
//        }
        //liste des diplômes
//        diplomes = UtilGrh.buildDiplome(dao.loadNameQueries("YvsDiplomes.findAll", champ, val));

//charge la liste des poste de travail
        loadPoste();
        loadAllUsers();
        loadAllGrade();
        initRepDoc();
        domainesQualification = dao.loadNameQueries("YvsGrhDomainesQualifications.findAll", new String[]{"societe"}, new Object[]{currentUser.getAgence().getSociete()});
        // Les profils
        profils = dao.loadNameQueries("YvsGrhProfil.findAllActif", new String[]{"societe"}, new Object[]{currentUser.getAgence().getSociete()});
    }

    public void loadAllGrade() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listGradeEmploye = UtilGrh.buildBeanListGradeEmploye(dao.loadNameQueries("YvsGrhGradeEmploye.findBySociete", champ, val));
    }

    public void loadAllCategorie() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listCategoriePro = dao.loadNameQueries("YvsCategorieProfessionelle.findAll", champ, val);
    }

    public void loadAllUsers() {
        champ = new String[]{"agence", "actif"};
        val = new Object[]{currentAgence, true};
        nameQueri = "YvsUsers.findByAgence";
        if (autoriser("base_view_all_user")) {
            champ = new String[]{"societe", "actif"};
            val = new Object[]{currentAgence.getSociete(), true};
            nameQueri = "YvsUsers.findAlls";
        }
        listUsers = dao.loadNameQueries(nameQueri, champ, val);
    }

    private void initRepDoc() {
        repDestDoc = Initialisation.getCheminResource() + "" + Initialisation.getCheminDocPersoEmps().substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminDocPersoEmps().length());
        repSvgDoc = Initialisation.getCheminDocPersoEmps();
    }
    /**/
    //Documents
    /**/
    private DocumentsEmps document = new DocumentsEmps();
    private boolean displayDelDoc;
    private YvsGrhDocumentEmps selectionDoc;

    public DocumentsEmps getDocument() {
        return document;
    }

    public void setDocument(DocumentsEmps document) {
        this.document = document;
    }

    public boolean isDisplayDelDoc() {
        return displayDelDoc;
    }

    public void setDisplayDelDoc(boolean displayDelDoc) {
        this.displayDelDoc = displayDelDoc;
    }

    public YvsGrhDocumentEmps getSelectionDoc() {
        return selectionDoc;
    }

    public void setSelectionDoc(YvsGrhDocumentEmps selectionDoc) {
        this.selectionDoc = selectionDoc;
    }

    String repDestDoc, repSvgDoc = "";
    UploadedFile fileDoc, fileDocSvg;

    public void handleUploadDocEmps(FileUploadEvent ev) {
        if (employes != null) {
            if (!Util.asString(repDestDoc)) {
                repDestDoc = Initialisation.getCheminResource() + "" + Initialisation.getCheminDocPersoEmps().substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminDocPersoEmps().length());
            }
            if (!Util.asString(repSvgDoc)) {
                repSvgDoc = Initialisation.getCheminDocPersoEmps();
            }
            String file = employes.getId() + "_" + Util.giveFileName() + "." + Util.getExtension(ev.getFile().getFileName());
            document.setChemin(file);
            fileDoc = ev.getFile();
            fileDocSvg = ev.getFile();
            getInfoMessage("Chargé ! ", ev.getFile().getFileName() + " is uploaded.");
        } else {
            getErrorMessage("Formulaire incomplet !");
        }

    }

    private boolean saveDoc() {
        if (fileDoc != null && repDestDoc != null && repSvgDoc != null) {
            try {
                //copie dans le dossier de sauvegarde
                Util.copySVGFile(document.getChemin(), repSvgDoc + "" + Initialisation.FILE_SEPARATOR, fileDocSvg.getInputstream());
                //copie dans le dossier de l'application
                Util.copyFile(document.getChemin(), repDestDoc + "" + Initialisation.FILE_SEPARATOR, fileDoc.getInputstream());
            } catch (IOException ex) {
                Logger.getLogger(ManagedEmployes.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true;
    }

    public void addDocument() {
        if (!Util.asString(document.getChemin())) {
            getErrorMessage("Fichier du document introuvable!!!");
            return;
        }
        if (!Util.asString(document.getTitre())) {
            getErrorMessage("Vous devez entrer un titre au document");
            return;
        }
        if (document.getId() > 0 || saveDoc()) {
            YvsGrhDocumentEmps doc = new YvsGrhDocumentEmps((int) document.getId());
            doc.setActif(true);
            doc.setTitre(document.getTitre());
            doc.setDescription(document.getDescription());
            doc.setChemin(document.getChemin());
            doc.setDateSave(document.getDateSave());
            doc.setEmploye(employes);
            doc.setAuthor(currentUser);
            if (doc.getId() < 1) {
                doc.setId(null);
                doc = (YvsGrhDocumentEmps) dao.save1(doc);
                document.setId(doc.getId());
                employe.getDocuments().add(doc);
            } else {
                dao.update(doc);
                int index = employe.getDocuments().indexOf(doc);
                if (index > -1) {
                    employe.getDocuments().set(index, doc);
                }
            }
        }
        resetFormDoc();
        update("mainEmps:emp-tab-doc");
    }

    public void resetFormDoc() {
        document = new DocumentsEmps();
        update("mainEmps:emp-grid-doc");
    }

    public void deleteDocument(YvsGrhDocumentEmps y) {
        if (y != null ? y.getId() > 0 : false) {
            dao.delete(y);
            employe.getDocuments().remove(y);
            //supprimer le fichier dand les repertoires 
            Util.delFileOnApps(repDestDoc + "" + Initialisation.FILE_SEPARATOR, y.getChemin());
            Util.delFile(repSvgDoc + "" + Initialisation.FILE_SEPARATOR, y.getChemin());
            update("mainEmps:emp-tab-doc");
        }
    }

    public void openDocument(YvsGrhDocumentEmps y) {
        if (y != null ? y.getId() > 0 : false) {
            try {
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(repDestDoc) + Initialisation.FILE_SEPARATOR + y.getChemin();
                if (new File(path).exists()) {
                    path = (repDestDoc + Initialisation.FILE_SEPARATOR + y.getChemin()).replace("\\", "/");
                    y.setPrintPath(path);
                    selectionDoc = y;
                    openDialog("dlgView");
                    update("media-print");
                }
            } catch (Exception ex) {
                getException("openDocument", ex);
            }
        }
    }

    public void selectDoc(SelectEvent ev) {
        YvsGrhDocumentEmps y = (YvsGrhDocumentEmps) ev.getObject();
        document = new DocumentsEmps();
        document.setId(y.getId());
        document.setTitre(y.getTitre());
        document.setChemin(y.getChemin());
        document.setDescription(y.getDescription());
        document.setDateSave(y.getDateSave());
        update("mainEmps:emp-tab-doc");
        update("mainEmps:emp-grid-doc");
    }

    public void unSelectDoc(UnselectEvent ev) {
        document = new DocumentsEmps();
        update("mainEmps:emp-tab-doc");
        update("mainEmps:emp-grid-doc");
    }

    @Override
    public void resetFiche() {
        resetFiche(employe);
        //initialisation de la date de naissance
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1990);
        employe.setDateNaissance(cal.getTime());
        employe.setDateArret(new Date());
        employe.setDateDelivCni(new Date());
        employe.setConvention(new Convention());
        employe.setLieuDelivCni(new Dictionnaire());
        employe.setVilleNaissance(new Dictionnaire());
        employe.setPaysDorigine(new Dictionnaire());
        employe.setPosteDeTravail(new PosteDeTravail());
        employe.setUser(new Users());
        employe.setProfil(new Profils());
        employe.getDiplomes().clear();
        employe.getDocuments().clear();
        employe.getLangues().clear();
        employe.getPermis().clear();
        employe.getPersonneEnCharge().clear();
        employe.getQualifications().clear();
        employe.setAgence(new Agence());
        employe.setCompteCollectif(new Comptes());
        employe.setCompteTiers(new Tiers());
        employe.setEquipe(new EquipeEmploye());
        employe.setStatutEmps(new StatutEmps());
        employe.getSectionsAnalytiques().clear();

        qualification = new Qualification();
        diplome = new Diplomes();

        displayDelQualif = false;
        displayDelDiplome = false;
        employes = new YvsGrhEmployes();
        update = false;
        codeExterne = "";
        update("employe-main-panel");
    }

    /**/
    //RECHERCHE
    /**/
    private String matriculeToUpdate;
    private long idQualif, idDepartement;

    public long getIdQualif() {
        return idQualif;
    }

    public void setIdQualif(long idQualif) {
        this.idQualif = idQualif;
    }

    public long getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(long idDepartement) {
        this.idDepartement = idDepartement;
    }

    //recherche un employé en fonction de son nom, matricule, code utilisateur
    public void findEmploye(String matricule) {
        ParametreRequete p0 = new ParametreRequete(null, "employe", "Emps", "LIKE", "AND");
        if (Util.asString(matricule)) {
            ParametreRequete p01 = new ParametreRequete("UPPER(y.nom)", "nom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            ParametreRequete p02 = new ParametreRequete("UPPER(y.prenom)", "prenom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            ParametreRequete p03 = new ParametreRequete("UPPER(y.matricule)", "matricule", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            ParametreRequete p04 = new ParametreRequete("UPPER(concat(y.nom,' ',y.prenom))", "nomPrenom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            ParametreRequete p05 = new ParametreRequete("UPPER(concat(y.prenom,' ',y.nom))", "prenomNom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            p0.getOtherExpression().add(p01);
            p0.getOtherExpression().add(p02);
            p0.getOtherExpression().add(p03);
            p0.getOtherExpression().add(p04);
            p0.getOtherExpression().add(p05);
        } else {
            p0.setObjet(null);
        }
        paginator.addParam(p0);
        loadAllEmployesByAgence(true, true);
    }

    public void findSimpleEmploye(String matricule) {
        ParametreRequete p0 = new ParametreRequete(null, "employe", "Emps", "LIKE", "AND");
        if (Util.asString(matricule)) {
            ParametreRequete p01 = new ParametreRequete("UPPER(y.nom)", "nom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            ParametreRequete p02 = new ParametreRequete("UPPER(y.prenom)", "prenom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            ParametreRequete p03 = new ParametreRequete("UPPER(y.matricule)", "matricule", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            ParametreRequete p04 = new ParametreRequete("UPPER(concat(y.nom,' ',y.prenom))", "nomPrenom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            ParametreRequete p05 = new ParametreRequete("UPPER(concat(y.prenom,' ',y.nom))", "prenomNom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            p0.getOtherExpression().add(p01);
            p0.getOtherExpression().add(p02);
            p0.getOtherExpression().add(p03);
            p0.getOtherExpression().add(p04);
            p0.getOtherExpression().add(p05);
        } else {
            p0.setObjet(null);
        }
        paginator.addParam(p0);
        loadSimpleAllEmployes(true, true);
    }

    public void findEmployeInSociete(String matricule) {
        ParametreRequete p0 = new ParametreRequete(null, "employe", "Emps", "LIKE", "AND");
        if (Util.asString(matricule)) {
            ParametreRequete p01 = new ParametreRequete("UPPER(y.nom)", "nom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            ParametreRequete p02 = new ParametreRequete("UPPER(y.prenom)", "prenom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            ParametreRequete p03 = new ParametreRequete("UPPER(y.matricule)", "matricule", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            ParametreRequete p04 = new ParametreRequete("UPPER(concat(y.nom,' ',y.prenom))", "nomPrenom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            ParametreRequete p05 = new ParametreRequete("UPPER(concat(y.prenom,' ',y.nom))", "prenomNom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            p0.getOtherExpression().add(p01);
            p0.getOtherExpression().add(p02);
            p0.getOtherExpression().add(p03);
            p0.getOtherExpression().add(p04);
            p0.getOtherExpression().add(p05);
        } else {
            p0.setObjet(null);
        }
        addParam(p0);
        loadAllEmployesByAgence(true, true);
    }

    public void findEmploye() {
        findEmploye(matriculeToUpdate);
    }

    public YvsGrhEmployes findOneEmploye(long id) {
        YvsGrhEmployes l = null;
        if (id > 0) {
            l = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findById", new String[]{"id"}, new Object[]{id});
        }
        return l;
    }

    public YvsGrhEmployes findOneEmploye(String matricule) {
        if (matricule != null) {
            List<YvsGrhEmployes> l = dao.loadNameQueries("YvsGrhEmployes.findByCodeUsers", new String[]{"codeUsers", "agence"}, new Object[]{"%" + matricule + "%", currentUser.getAgence().getSociete()}, 0, 1);
            if (!l.isEmpty()) {
                return l.get(0);
            }
        }
        return null;
    }

    //recherche un employé par qualification
    public void findByQualification(ValueChangeEvent ev) {
        if ((ev != null) ? ev.getNewValue() != null : false) {
            champ = new String[]{"qualification", "societe"};
            int id = (int) ev.getNewValue();
            val = new Object[]{new YvsGrhDomainesQualifications((long) id), currentUser.getAgence().getSociete()};
            List<YvsGrhEmployes> l = dao.loadNameQueries("YvsGrhQualificationEmploye.findEmployeByEmploye", champ, val, offsetEmps, 500);
            listEmployes = buildListEmployeBean(l);
            update("employe-main-dgrid");
            update("empps-dgrig");
        }
    }

    public void findByDepartement(ValueChangeEvent ev) {
        if (ev != null && ev.getNewValue() != null) {
            int id = (int) ev.getNewValue();
            champ = new String[]{"dep"};
            val = new Object[]{new YvsGrhDepartement(id)};
            listEmployes = dao.loadNameQueries("YvsPosteEmployes.findByDepartement", champ, val, 0, 500);
//            for (YvsGrhPosteEmployes qe : lq) {
//                listEmployes.add(UtilGrh.buildBeanPartialEmploye(qe.getEmploye()));
//            }
            update("employe-main-dgrid");
            update("empps-dgrig");
        }
    }
    /*--------------------------------------------------------------------------*/

    /*-----------------------------GERER LES EQUIPE--------------------------------------*/
    public void loadAllEquipe() {
        champ = new String[]{"societe"};
        val = new Object[]{currentUser.getAgence().getSociete()};
        equipesEmployes = dao.loadNameQueries("YvsGrhEquipeEmploye.findAll", champ, val);
    }

    private boolean updateEquipe;

    public void createNewEquipe() {
        if (equipe.getTitreEquipe() != null && equipe.getGroupeService() != null) {
            if (!equipe.getTitreEquipe().trim().equals("") && !equipe.getGroupeService().trim().equals("")) {
                YvsGrhEquipeEmploye entity = new YvsGrhEquipeEmploye();
                entity.setActif(equipe.isActif());
                entity.setAuthor(currentUser);
                entity.setGroupeService(equipe.getGroupeService());
                entity.setTitreEquipe(equipe.getTitreEquipe());
                if (updateEquipe) {
                    entity.setId(equipe.getId());
                    dao.update(entity);
                    equipesEmployes.set(equipesEmployes.indexOf(entity), entity);
                    if (!entity.getActif()) {
                        equipesEmployeActif.remove(entity);
                    } else {
                        if (!equipesEmployeActif.contains(entity)) {
                            equipesEmployeActif.add(0, entity);
                        } else {
                            equipesEmployeActif.set(equipesEmployeActif.indexOf(entity), entity);
                        }
                    }
                } else {
                    entity.setId(null);
                    entity = (YvsGrhEquipeEmploye) dao.save1(entity);
                    equipesEmployes.add(0, entity);
                    if (entity.getActif()) {
                        equipesEmployeActif.add(0, entity);
                    }
                }
            } else {
                getErrorMessage("Formulaire incorrecte !");
            }
        } else {
            getErrorMessage("Formulaire incorrecte !");
        }
    }

    public void deleteEquipe(YvsGrhEquipeEmploye eq) {
        try {
            eq.setAuthor(currentUser);
            dao.delete(eq);
            equipesEmployes.remove(eq);
            equipesEmployeActif.remove(eq);
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cette equipe !");
        }
    }

    public void toggleActifEquipe(YvsGrhEquipeEmploye eq) {
        try {
            eq.setAuthor(currentUser);
            eq.setActif(!eq.getActif());
            dao.update(eq);
            if (!eq.getActif()) {
                equipesEmployeActif.remove(eq);
            } else {
                if (!equipesEmployeActif.contains(eq)) {
                    equipesEmployeActif.add(0, eq);
                } else {
                    equipesEmployeActif.set(equipesEmployeActif.indexOf(eq), eq);
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cette equipe !");
        }
    }

    public void loadEquipeOnView(SelectEvent ev) {
        if (ev != null) {
            YvsGrhEquipeEmploye emp = (YvsGrhEquipeEmploye) ev.getObject();
            employe.setEquipe(new EquipeEmploye(emp.getId(), emp.getTitreEquipe()));
            equipe = new EquipeEmploye(emp.getId(), emp.getTitreEquipe());
            equipe.setGroupeService(emp.getGroupeService());
            equipe.setActif(emp.getActif());
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * charge la liste des pays pour l'autocompletion
     *
     * @param str
     * @return
     */
    /**
     * Users
     *
     */
    int offsetUser = 0;
    private boolean disPrevUser = true, disNextUser;
    YvsUsers user = new YvsUsers();

    public boolean isDisNextUser() {
        return disNextUser;
    }

    public void setDisNextUser(boolean disNextUser) {
        this.disNextUser = disNextUser;
    }

    public boolean isDisPrevUser() {
        return disPrevUser;
    }

    public void setDisPrevUser(boolean disPrevUser) {
        this.disPrevUser = disPrevUser;
    }

    public void findUsers() {
        champ = new String[]{"codeUsers", "societe"};
        val = new Object[]{employe.getUser(), currentAgence.getSociete()};
        if (employe.getUser() != null) {
            user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findByCodeUsers", champ, val);
            if (user == null) {
                loadAllUsers();
//                openDialog("dlgListUsers");
//                update(":formUser:bNav");
                setStyleError();
            } else {
                clearStyleError();
            }
        } else {
            setStyleError();
        }
    }

    /**
     * end
     */
    /**
     * contrat
     *
     * @param ev
     */
    /*
     * gère l'ajout d'un fichier description de la qualification
     */
    public void handleFileUploadContrat(FileUploadEvent ev) {
        String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminDocEmps().substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminDocEmps().length());
//répertoire destination de sauvegarge= C:\\users\lymytz...
        String repDestSVG = Initialisation.getCheminDocEmps();
        String file = Util.giveFileName() + "." + Util.getExtension(ev.getFile().getFileName());
        try {
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
//            employe.getContrat().setFile(file);
            FacesMessage msg = new FacesMessage("Success! ", ev.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (IOException ex) {
            Logger.getLogger(ManagedEmployes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void downloadFile() {
//        if (employe.getContrat().getFile() != null) {
//            String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminDocEmps().substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminDocEmps().length());
//            String file = FacesContext.getCurrentInstance().getExternalContext().getRealPath(repDest + "" + Initialisation.FILE_SEPARATOR + employe.getContrat().getFile());
//            File f = new File(file);
//            byte[] bytes = Util.read(f);
//            Util.openFile(FacesContext.getCurrentInstance(), bytes, Util.getExtension(employe.getContrat().getFile()), employe.getContrat().getFile());
//        }
    }

    /**
     * End
     */
    public void save() {
        if (!update) {
            saveNew();
        } else {
            updateEmploye();
        }
    }

//    private void controleCatPro() {
//        champ = new String[]{"employe", "categorie"};
//        val = new Object[]{employes, employe.getConvention().getCategorie().getId()};
//        boolean exist = (0 == ((Long) (dao.loadObjectByNameQueries("YvsCategorieProEmploye.countC", champ, val))));
//        if (exist) {
//            openDialog("WdlgUpdateCategoriePro");
//        } else {
//            controleEchPro();
//        }
//    }
    public void updateCatPro() {
        YvsCategorieProEmploye cpe = buildCatProEmp(employe);
        dao.update(cpe);
        controleEchPro();
    }

    public void saveNewCatPro() {
        YvsCategorieProEmploye cpe = buildCatProEmp(employe);
        cpe.setId(null);
        String rq = "UPDATE yvs_categorie_pro_employe SET actif=false WHERE employe=?";
        Options[] param = new Options[]{new Options(employes.getId(), 1)};
        dao.requeteLibre(rq, param);
        cpe = (YvsCategorieProEmploye) dao.save1(cpe);
//        employe.getCategoriePro().setId(cpe.getId());
        controleEchPro();
    }

    private void controleEchPro() {
        String[] str = new String[]{"employe", "echelon"};
        Object[] val = new Object[]{employes, employe.getConvention().getEchelon().getId()};
        boolean exist = (0 == ((Long) (dao.loadObjectByNameQueries("YvsEchelonEmploye.countE", str, val))));
        if (exist) {
            openDialog("WdlgUpdateEch");
        } else {
            controleProfil();
        }
    }

    public void updateEchPro() {
        YvsEchelonEmploye cpe = buildEchelonEmp(employe);
        dao.update(cpe);
        controleProfil();
    }

    public void saveNewEchPro() {
        YvsEchelonEmploye cpe = buildEchelonEmp(employe);
        cpe.setId(null);
        String rq = "UPDATE yvs_echelon_employe SET actif=false WHERE employe=?";
        Options[] param = new Options[]{new Options(employes.getId(), 1)};
        dao.requeteLibre(rq, param);
        cpe = (YvsEchelonEmploye) dao.save1(cpe);
//        employe.getEchelon().setId(cpe.getId().intValue());
        controleProfil();
    }

    private void controleProfil() {
        String[] str = new String[]{"employe", "grade", "statut"};
        Object[] val = new Object[]{employes, employe.getProfil().getGrade(), employe.getProfil().getStatut()};
        boolean exist = (0 == ((Long) (dao.loadObjectByNameQueries("YvsProfilEmps.countP", str, val))));
        if (exist) {
            openDialog("WdlgUpdateProfil");
        } else {
//            controleContrat();
        }
    }

    public void updateProfil() {
        YvsGrhProfilEmps cpe = buildProfilEmps(employe);
        dao.update(cpe);
//        controleContrat();
    }

    public void saveNewProfil() {
        YvsGrhProfilEmps cpe = buildProfilEmps(employe);
        cpe.setId(null);
        String rq = "UPDATE yvs_profil_emps SET actif=false WHERE employe=?";
        Options[] param = new Options[]{new Options(employes.getId(), 1)};
        dao.requeteLibre(rq, param);
        cpe = (YvsGrhProfilEmps) dao.save1(cpe);
        employe.getProfil().setId(cpe.getId().intValue());
//        controleContrat();
    }

    public void updateContrat() {
//        YvsContratEmps cpe = buildContrat(employe.getContrat());
//        dao.update(cpe);
        controlePoste();
    }

    public void saveNewContrat() {
//        YvsContratEmps cpe = buildContrat(employe.getContrat());
//        cpe.setId(null);
        String rq = "UPDATE yvs_contrat_emps SET actif=false WHERE employe=?";
        Options[] param = new Options[]{new Options(employes.getId(), 1)};
        dao.requeteLibre(rq, param);
//        cpe = (YvsContratEmps) dao.save1(cpe);
//        employe.getContrat().setId(cpe.getId().intValue());
        controlePoste();
    }

    public void cancelContrat() {
        controlePoste();
    }
    /*le contrôle du poste doit consister dans un premier temps à s'assurer que l'intitulé du poste qui est saisie existe bien!
     si non, le créer
     */
    YvsGrhPosteDeTravail poste = null;

    private void controlePoste() {
        if ((employe.getPosteDeTravail().getIntitule() != null) ? !employe.getPosteDeTravail().getIntitule().trim().equals("") : false) {
            poste = findPoste(employe);
            if (poste != null) {
                champ = new String[]{"employe"};
                val = new Object[]{employes};
                boolean exist = (0 == ((Long) (dao.loadObjectByNameQueries("YvsPosteEmploye.countP", champ, val))));
                if (exist) {
                    openDialog("WdlgUpdatePoste");
                    update(":formPoste:gridPoste");
                } else {
                    updateEmploye();
                }
            } else {
                openDialog("dlgAddPoste");
                update(":formPoste:gridPoste");
            }
        } else {
            updateEmploye();
        }
    }

    public void updatePoste() {
//        YvsPosteEmploye cpe = buildPosteDeTravail(employe);
//        dao.update(cpe);
//        updateEmploye();
    }

    public void cancelPoste() {
        updateEmploye();
    }

    private YvsCategorieProEmploye buildCatProEmp(Employe e) {
        if (e.getConvention().getCategorie().getId() != 0) {
            YvsCategorieProEmploye ce = new YvsCategorieProEmploye(e.getConvention().getCategorie().getCategorieActif());
//            ce.setCategorie(new YvsGrhCategorieProfessionelle((int) e.getCategoriePro().getId()));
            ce.setEmploye(employes);
            ce.setActif(true);
            ce.setDateChange(new Date());
            return ce;
        } else {
            return null;
        }
    }

    private YvsEchelonEmploye buildEchelonEmp(Employe e) {
        if (e.getConvention().getEchelon().getId() != 0) {
            YvsEchelonEmploye ce = new YvsEchelonEmploye(e.getConvention().getEchelon().getEchelonActif());
//            ce.setEchelon(new YvsGrhEchelons(e.getEchelon().getId()));
//            ce.setEmploye(employes);
//            ce.setActif(true);
//            ce.setDateChange(new Date());
            return ce;
        } else {
            return null;
        }
    }

    private YvsGrhProfilEmps buildProfilEmps(Employe e) {
        YvsGrhProfilEmps p = new YvsGrhProfilEmps();
        p.setActif(true);
        p.setDateChange(new Date());
        p.setEmploye(employes);
        if ((e.getProfil().getGrade() != null) ? e.getProfil().getGrade().getId() != 0 : false) {
            p.setGrade(new YvsGrhGradeEmploye(e.getProfil().getGrade().getId()));
        }
        p.setStatut(e.getProfil().getStatut());
        return p;
    }

//    private YvsPosteEmploye buildPosteDeTravail(Employe e) {
//        if (e.getPosteDeTravail().getIntitule() != null) {
//            YvsGrhPosteDeTravail poste = findPosteDeTravail(e.getPosteDeTravail().getIntitule());
//            if (poste != null) {
//                YvsPosteEmploye pt = new YvsPosteEmploye();
//                pt.setActif(true);
//                pt.setDateAffectation(new Date());
//                pt.setEmploye(employes);
//                pt.setPosteDeTravail(poste);
//                return pt;
//            } else {
//                //save new poste de travail
//                openDialog("dlgAddPoste");
//            }
//        }
//        return null;
//    }    
    YvsPermisDeCoduire permiActif = null;

    /**
     * End
     */
    /**
     * Langues
     */
//    private Langues selectLangue;
    //vérifie l'existance d'une qualification dans la liste des qualifications
//    private YvsLangues findLangue(String qualif) {
////        UtilitaireGenerique<LangueEmps> ug = new UtilitaireGenerique<>();
////        int pos = ug.triDichontomique(listLangues, "langue", qualif);
//        String[] champ = new String[]{"nom", "societe"};
//        Object[] val = new Object[]{qualif, currentAgence.getSociete()};
//        YvsLangues r = (YvsLangues) dao.loadObjectByNameQueries("YvsLangues.findByNom", champ, val);
//        return r;
//    }
//    private YvsLangueEmps saveLangueEmps(YvsLangueEmps le) {
//        return (YvsLangueEmps) dao.save1(le);
//    }
    /**
     * End Langues
     */
    /**
     * End Permis de conduire
     */
    /**
     * Adresses
     */
    /**
     * End Adresses
     */
    /**
     * POSTE DE TRAVAIL
     *
     * @param str
     * @return
     */
    //pour l'objet d'autocompletion des postes de travail
    private Departements departement = new Departements();

    public Departements getDepartement() {
        return departement;
    }

    public void setDepartement(Departements departement) {
        this.departement = departement;
    }

    public List<String> loadPoste(String str) {
        List<String> result = new ArrayList<>();
        for (YvsGrhPosteDeTravail d : posteDeTravails) {
            if (d.getIntitule().startsWith(str)) {
                result.add(d.getIntitule());
            }
        }
        return result;
    }
//l'intitulé du poste de travail doit être unique par agence

    private YvsGrhPosteDeTravail findPoste(Employe e) {
        String[] str = new String[]{"intitule", "societe"};
        Object[] val = new Object[]{e.getPosteDeTravail().getIntitule(), currentAgence.getSociete()};
        return (YvsGrhPosteDeTravail) dao.loadOneByNameQueries("YvsPosteDeTravail.findByIntitule", str, val);
    }

    public void findServices(ValueChangeEvent ev) {
        if (ev != null) {
            if (ev.getNewValue() != null) {
                long dep = (Long) ev.getNewValue();
                if (dep != 0) {
                }
            }
        }
    }

    /*Selection à partir de la boite de dialogue*/
    public void choixEmps(SelectEvent ev) {

    }

//    public void loadAllEchelonByCategorie() {
//        listEchelon.clear();
//        champ = new String[]{"secteur", "categorie"};
//        val = new Object[]{currentAgence.getSecteurActivite(), new YvsGrhCategorieProfessionelle(employe.getConvention().getCategorie().getId())};
//        List<YvsConventionCollective> list = (List<YvsConventionCollective>) dao.loadNameQueries("YvsGrhConventionCollective.findByCategorie", champ, val);
//        for (YvsGrhConventionCollective co : list) {
//            listEchelon.add(UtilGrh.buildBeanEchelon(co.getYvsEchelons()));
//        }
//        update("panel-avacement-emps");
//    }
//
//    public void searchConventionByElement() {
//        champ = new String[]{"secteur", "categorie", "echelon"};
//        val = new Object[]{currentAgence.getSecteurActivite().getId(), employe.getConvention().getCategorie().getId(), employe.getConvention().getEchelon().getId()};
//        YvsGrhConventionCollective c = (YvsGrhConventionCollective) dao.loadOneByNameQueries("YvsGrhConventionCollective.findByCE", champ, val);
//        Categories cat = listCategoriePro.get(listCategoriePro.indexOf(employe.getConvention().getCategorie()));
//        Echelons ech = listEchelon.get(listEchelon.indexOf(employe.getConvention().getEchelon()));
//        if (cat.getDegre() == employe.getConvention().getCategorie().getDegre()) {
//            if (ech.getDegre() < employe.getConvention().getEchelon().getDegre()) {
//                openDialog("dlgConfirmationR");
//            }
//        } else if (cat.getDegre() < employe.getConvention().getCategorie().getDegre()) {
//            openDialog("dlgConfirmationR");
//        }
//        employe.setConvention(new Convention(c.getId()));
//    }
    public void saveNewConvention() {
        YvsGrhConventionEmploye c = new YvsGrhConventionEmploye();
        c.setEmploye(new YvsGrhEmployes(employe.getId()));
        c.setActif(true);
        c.setDateChange(new Date());
        c.setSupp(false);
        c.setConvention(new YvsGrhConventionCollective(employe.getConvention().getId()));
        String rq = "UPDATE yvs_grh_convention_employe SET actif=false WHERE employe=?";
        Options[] param = new Options[]{new Options(employe.getId(), 1)};
        dao.requeteLibre(rq, param);
        c = (YvsGrhConventionEmploye) dao.save1(c);
        employe.getConvention().setId(c.getId());
        employe.getConvention().setCategorie(UtilGrh.buildBeanCategoriePro(c.getConvention().getCategorie()));
        employe.getConvention().setEchelon(UtilGrh.buildBeanEchelon(c.getConvention().getEchelon()));
        employe.getConvention().setActif(true);
        employe.getConvention().setDateChange(new Date());
        succes();
        closeDialog("dlgAvancement");
    }

    public void closeBtAvancement() {
        closeDialog("dlgConfirmationR");
        closeDialog("dlgAvancement");
    }

    @Override
    public void deleteBean() {
        try {
            System.err.println("employe= " + listSelection.size());
            if (listSelection != null) {
                YvsGrhEmployes emps;
                for (YvsGrhEmployes e : listSelection) {
                    System.err.println("employe= " + e.getNom_prenom());
                    emps = new YvsGrhEmployes(e.getId());
                    emps.setAuthor(currentUser);
                    dao.delete(emps);

                }
                listEmployes.removeAll(listSelection);
                listSelection.clear();

                succes();
                update("empps-dgrig");
                update("employe-main-dgrid");
                update("tabEmployes");
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible d'effectuer cette opération");
            Logger.getLogger(ManagedEmployes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteEmploye(YvsGrhEmployes emp) {
        try {
            if (emp != null ? emp.getId() > 0 : false) {
                emp.setAuthor(currentUser);

                List<YvsGrhPosteEmployes> postes = dao.loadNameQueries("YvsPosteEmployes.findByEmploye", new String[]{"employe"}, new Object[]{emp});
                for (YvsGrhPosteEmployes p : postes) {
                    dao.delete(p);
                }
                dao.delete(emp);
                listEmployes.remove(emp);
                succes();
                update("empps-dgrig");
                update("employe-main-dgrid");
                update("tabEmployes");
            }
        } catch (Exception e) {
            getErrorMessage("Impossible d'effectuer cette opération");
            Logger.getLogger(ManagedEmployes.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void updateEmploye(YvsGrhEmployes emp) {
        try {
            if (emp != null ? emp.getId() > 0 : false) {
                emp.setActif(!emp.getActif());

                int index = listEmployes.indexOf(emp);
                if (index > -1) {
                    dao.update(emp);
                    listEmployes.set(index, emp);
                }
                succes();
                update("empps-dgrig");
                update("employe-main-dgrid");
                update("tabEmployes");
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
    }

    public void chechActiver() {
        if (employe.getId() > 0) {
            Boolean actif = !employe.isActif();
            String rq = "UPDATE yvs_grh_employes SET actif=" + actif + " WHERE id=?";
            Options[] param = new Options[]{new Options(employe.getId(), 1)};
            dao.requeteLibre(rq, param);
            listEmployes.get(listEmployes.indexOf(new YvsGrhEmployes(employe.getId()))).setActif(actif);
            employe.setActif(actif);
            succes();
            resetPage();
            update("E-f");
            update("employe-main-bloc");
        }
    }

    @Override
    public void resetPage() {
        if (listEmployes != null) {
            if (!listEmployes.isEmpty()) {
                for (YvsGrhEmployes e : listEmployes) {
//                    listEmployes.get(listEmployes.indexOf(e)).setSelectActif(false);
                }
            }
        }
        listSelection.clear();
    }

    public void calculDateexpCNI() {
        Calendar c = Calendar.getInstance();
        c.setTime(employe.getDateDelivCni());
        c.add(Calendar.YEAR, 10);
        employe.setDateExpCni(c.getTime());
    }

    public void choixCategoriePro() {
        if (employe.getConvention().getCategorie().getId() != 0 && employe.getConvention().getEchelon().getId() != 0 && currentAgence.getSecteurActivite() != null) {
            //cherche la convetion collective rattaché
            champ = new String[]{"categorie", "echelon", "secteur"};
            val = new Object[]{employe.getConvention().getCategorie().getId(), employe.getConvention().getEchelon().getId(), currentAgence.getSecteurActivite().getId()};
            YvsGrhConventionCollective cc = (YvsGrhConventionCollective) dao.loadOneByNameQueries("YvsConventionCollective.findByCE", champ, val);
            if (cc == null) {
                getMessage("Cette convention n'existe pas ! veuillez la paramétrer", FacesMessage.SEVERITY_WARN);
                employe.setConvention(new Convention());
            }
            update("emp-conv-echp");
        }
    }

    public void chooseCategorie(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            int id = (int) ev.getNewValue();
            if (id > 0) {
                employe.getConvention().setCategorie(UtilGrh.buildBeanCategoriePro(listCategoriePro.get(listCategoriePro.indexOf(new YvsGrhCategorieProfessionelle(id)))));
                if (employe.getConvention().getCategorie().getId() > 0) {
                    employe.getConvention().getCategorie().setListEchelons(UtilGrh.buildBeanEchelonsByEch(dao.loadNameQueries("YvsConventionCollective.findEchelon", new String[]{"categorie"}, new Object[]{new YvsGrhCategorieProfessionelle(employe.getConvention().getCategorie().getId())})));
                }
            }
            update("emp-conv-echp");
        }
    }
    /*Gérér les profils*/

    public void createNewProfils() {
        if ((profil.getStatut() != null) ? !profil.getStatut().isEmpty() : false) {
            YvsGrhProfil pro = new YvsGrhProfil();
            pro.setActif(pro.getActif());
            pro.setAuthor(currentUser);
            int idx = listGradeEmploye.indexOf(profil.getGrade());
            if (idx >= 0) {
                profil.setGrade(listGradeEmploye.get(idx));
            }
            pro.setGrade((profil.getGrade().getId() > 0) ? new YvsGrhGradeEmploye(profil.getGrade().getId(), profil.getGrade().getLibelle()) : null);
            pro.setStatutProfil(profil.getStatut());
            pro.setActif(profil.isActif());
            if (profil.getId() > 0) {
                pro.setId(profil.getId());
                dao.update(pro);
            } else {
                pro.setId(null);
                pro = (YvsGrhProfil) dao.save1(pro);
                profils.add(0, pro);
                if (pro.getActif()) {
                    profilsActif.add(0, pro);
                }
            }
            profil = new Profils();
            update("mainEmps:slect_profils_emps");
        } else {
            getErrorMessage("Formulaire incorrecte !");
        }
    }

    public void deleteProfil(YvsGrhProfil p) {
        try {
            p.setAuthor(currentUser);
            dao.delete(p);
            profils.remove(p);
            profilsActif.remove(p);
            succes();
            profil = new Profils();
            update("form_profil_0");
            update("table_form_profil_0");
            update("mainEmps:slect_profils_emps");
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet élément !");
            getException("Lymytz Error>>>> ", ex);
        }
    }

    public void toogleActiveProfil(YvsGrhProfil p) {
        p.setActif(!p.getActif());
        p.setAuthor(currentUser);
        dao.update(p);
        if (!p.getActif()) {
            profilsActif.remove(p);
        } else {
            profilsActif.add(0, p);
        }
        update("mainEmps:slect_profils_emps");
        update("table_form_profil_0");
    }
    /**
     * Gérer les comptes bancaires*
     */
    private Banques newBanque = new Banques();
    private boolean displayBtnUpBq, displayBtnDelBq, updateBanq, updateCB,
            displayBtnDelCB;

    public void setDisplayBtnDelCB(boolean displayBtnDelCB) {
        this.displayBtnDelCB = displayBtnDelCB;
    }

    public boolean isDisplayBtnDelCB() {
        return displayBtnDelCB;
    }

    public boolean isDisplayBtnDelBq() {
        return displayBtnDelBq;
    }

    public void setDisplayBtnDelBq(boolean displayBtnDelBq) {
        this.displayBtnDelBq = displayBtnDelBq;
    }

    public boolean isDisplayBtnUpBq() {
        return displayBtnUpBq;
    }

    public void setDisplayBtnUpBq(boolean displayBtnUpBq) {
        this.displayBtnUpBq = displayBtnUpBq;
    }

    public Banques getNewBanque() {
        return newBanque;
    }

    public void setNewBanque(Banques newBanque) {
        this.newBanque = newBanque;
    }

    public void loadAllBanques() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listBanques = dao.loadNameQueries("YvsBanques.findAll", champ, val);
    }

    public void choixBanque(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            int id = (int) ev.getNewValue();
            if (id == -1) {
                openDialog("dlgAddBanque");
            } else if (id > 0) {
                int index = listBanques.indexOf(new YvsBanques(id));
                if (index > -1) {
                    compteBancaire.setBanque(UtilGrh.buildBeanBanque(listBanques.get(index)));
                }
            }
        } else {
            newBanque = new Banques();
            update("pan-dlg-emp-banq");
        }
    }

    public void selectBanque(SelectEvent ev) {
        displayBtnDelBq = true;
        YvsBanques b = (YvsBanques) ev.getObject();
        newBanque = UtilGrh.buildBeanBanque(b);
        compteBancaire.setBanque(newBanque);
    }

    public void resetFicheBanque() {
        newBanque = new Banques();
        update("form_baques");
    }

    public void saveNewBanque() {
        if (newBanque.getNom() != null) {
            YvsBanques b = UtilGrh.buildEntityBanque(newBanque);
            b.setSociete(currentAgence.getSociete());
            b.setSupp(false);
            if (newBanque.getVille().getId() > 0) {
                int index = listVille.indexOf(new YvsDictionnaire(newBanque.getVille().getId()));
                if (index > -1) {
                    b.setVille(listVille.get(index));
                }
            }
            if (newBanque.getPays().getId() > 0) {
                int index = listPays.indexOf(new YvsDictionnaire(newBanque.getPays().getId()));
                if (index > -1) {
                    b.setPays(listPays.get(index));
                }
            }
            if (b.getId() < 1) {
                b.setId(null);
                b = (YvsBanques) dao.save1(b);
                newBanque.setId(b.getId());
            } else {
                dao.update(b);
            }
            int index = listBanques.indexOf(b);
            if (index > -1) {
                listBanques.set(index, b);
            } else {
                listBanques.add(0, b);
            }
            Banques bb = new Banques();
            cloneObject(bb, newBanque);
            compteBancaire.setBanque(bb);
            closeDialog("dlgAddBanque");
            newBanque = new Banques();
            update("pan-dlg-emp-banq");
            update("mainEmps:slt-banque-compte-B");
        } else {
            getErrorMessage("formulaire incorrecte !");
        }
    }

    public void openBanqUp(YvsBanques b) {
        try {
            newBanque = UtilGrh.buildBeanBanque(b);
        } catch (Exception e) {
            getErrorMessage("Impossible de supprimer ces éléments");
            getException("openBanqUp", e);
        }
        update("form_baques");
    }

    public void deleteBanque(YvsBanques b) {
        try {
            dao.delete(b);
            listBanques.remove(b);
            if (newBanque.getId() == b.getId()) {
                resetFicheBanque();
            }
            displayBtnDelBq = false;
        } catch (Exception e) {
            getErrorMessage("Impossible de supprimer ces éléments");
            getException("deleteBanque", e);
        }
        update("pan-dlg-emp-banq");
    }

    public void saveCompteB() {
        if (employe.getId() != 0 && compteBancaire.getBanque().getId() != 0 && compteBancaire.getBanque().getId() != 10000) {
            YvsCompteBancaire cb = UtilGrh.getCompteBancaire(compteBancaire);
            cb.setEmploye(employes);
            int index = listBanques.indexOf(new YvsBanques(compteBancaire.getBanque().getId()));
            if (index > -1) {
                cb.setBanque(listBanques.get(index));
            }
            if (cb.getId() < 1) {
                cb.setDateSave(new Date());
                cb.setId(null);
                cb = (YvsCompteBancaire) dao.save1(cb);
                compteBancaire.setId(cb.getId());
            } else {
                dao.update(cb);
            }
            index = employe.getComptesBancaires().indexOf(cb);
            if (index > -1) {
                employe.getComptesBancaires().set(index, cb);
            } else {
                employe.getComptesBancaires().add(0, cb);
            }
            compteBancaire = new CompteBancaire();
        } else {
            getErrorMessage("Impossible de continuer, formulaire incomplet !");
        }
    }

    public void removeCompteB(YvsCompteBancaire c) {
        try {
            if (c != null) {
                dao.delete(c);
                employe.getComptesBancaires().remove(c);
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer ces éléments !");
        }
        update("mainEmps:grid-cb-emp");
    }

    public void resetFormCB() {
        compteBancaire = new CompteBancaire();
        updateCB = false;
        update("mainEmps:grid-cb-emp");
    }

    public void selectCompteB(SelectEvent ev) {
        YvsCompteBancaire c = (YvsCompteBancaire) ev.getObject();
        cloneObject(compteBancaire, UtilGrh.buildBeanCompteBancaire(c));
        update("mainEmps:grid-cb-emp");
    }

    public void unSelectCompteB(UnselectEvent ev) {
        displayBtnDelCB = !selectionCompteBancaire.isEmpty();
        if (selectionCompteBancaire.size() > 1) {
            compteBancaire = new CompteBancaire();
        } else {
            if (selectionCompteBancaire.size() == 1) {
                cloneObject(compteBancaire, selectionCompteBancaire.get(0));
                updateCB = true;
            }
        }
        update("mainEmps:grid-cb-emp");
    }

    private int currentEmploye = 0;

    public void setCurrentEmploye(int currentEmploye) {
        this.currentEmploye = currentEmploye;
    }

    public int getCurrentEmploye() {
        return currentEmploye;
    }

//    public void navigateInView(int pas) {
//        if (employe.getId() != 0) {
//            currentEmploye = listEmployes.indexOf(employes);
//            if (pas > 0) {  //avance         
//                currentEmploye += pas;
//                if (listEmployes.size() > currentEmploye && currentEmploye >= 0) {
//                    choixEmploye1(listEmployes.get(currentEmploye));
//                }
//            } else {//recule
//                currentEmploye -= 1;
//                if (currentEmploye >= 0) {
//                    choixEmploye1(listEmployes.get(currentEmploye));
//                }
//            }
//        } else {
//            currentEmploye = 0;
//            if (listEmployes.size() > currentEmploye) {
//                choixEmploye1(listEmployes.get(0));
//            }
//        }
//    }
    public void addNewDomaine() {
        if ((newDomaine.getTitreDomaine() != null) ? !newDomaine.getTitreDomaine().trim().equals("") : false) {
            YvsGrhDomainesQualifications q = new YvsGrhDomainesQualifications();
            q.setTitreDomaine(newDomaine.getTitreDomaine());
            q.setAuthor(currentUser);
            q.setSociete(currentAgence.getSociete());
            q = (YvsGrhDomainesQualifications) dao.save1(q);
            newDomaine.setId(q.getId());
            newQualif.setDomaine(newDomaine);
            domainesQualification.add(0, q);
//            qualifications.add(new DomainesQualificationPoste(q.getId(), q.getTitreDomaine()));
            newDomaine = new DomainesQualifications();
        }
    }

    public void selectDomaineQ(ValueChangeEvent ev) {
        if (ev != null) {
            if (ev.getNewValue() != null) {
                long id = (long) ev.getNewValue();
                if (id > 0) {
                    setSelectDomaineQ(domainesQualification.get(domainesQualification.indexOf(new YvsGrhDomainesQualifications(id))));
                }
            }
        }
    }

    public void maintenancePhotos() {
        //cherche dans le dossier de sauvegarde des photos avec le nom se traouvant dans la bd
        String repDestSVG = Initialisation.getCheminPhotoEmps();
        for (YvsGrhEmployes e : listEmployes) {
            if (e.getPhotos() != null) {
                File f = new File(new StringBuilder(repDestSVG).append(Initialisation.FILE_SEPARATOR).append(e.getPhotos()).toString());
                if (!f.exists()) {
                    e.setAuthor(currentUser);
                    e.setPhotos(null);
                    dao.update(e);
                    loadAllEmployesByAgence(true, true);
                }
            }
        }

    }

    /*Filtrer les postes de travails */
    private long idDepartSearch;

    public long getIdDepartSearch() {
        return idDepartSearch;
    }

    public void setIdDepartSearch(long idDepartSearch) {
        this.idDepartSearch = idDepartSearch;
    }

    public void filterPoste(ValueChangeEvent ev) {
        if (ev != null) {
            int id = (int) ev.getNewValue();
            if (id > 0) {
                //poste par département
                ManagedDepartement service = (ManagedDepartement) giveManagedBean("managedDepartement");
                List<YvsGrhPosteDeTravail> lp = dao.loadNameQueries("YvsPosteDeTravail.findByDepartement", new String[]{"departement"}, new Object[]{new YvsGrhDepartement(id)});
                if (service != null) {
                    posteDeTravails = UtilGrh.buildBeanPoste(lp, service.getListValue());
                }
            } else if (id == -1) {
                loadPoste();
            }
        }
    }

    public void filterPosteLike(String chaine) {
        if (chaine != null) {
            if (!chaine.isEmpty()) {
                //poste par département
                ManagedDepartement service = (ManagedDepartement) giveManagedBean("managedDepartement");
                List<YvsGrhPosteDeTravail> lp = dao.loadNameQueries("YvsPosteDeTravail.findByIntituleLike", new String[]{"codePoste", "societe"}, new Object[]{chaine, currentAgence.getSociete()});
                if (service != null) {
                    posteDeTravails = UtilGrh.buildBeanPoste(lp, service.getListValue());
                } else {
                    loadPoste();
                }

            } else {
                loadPoste();
            }
        }
    }

    public double dureeCongePris(YvsGrhEmployes emp) {
        YvsBaseExercice exo = giveExerciceActif(new Date());
        int d = (int) dao.getNbreJourCongePris(emp.getId(), new Date(), (exo != null) ? exo.getId() : 0)
                + (int) dao.getNbreJourPermissionPris(emp.getId(), new Date(), (exo != null) ? exo.getId() : 0, Constantes.GRH_PERMISSION_SUR_CONGE);
        return d;
    }

    public double dureeCongeRestant(YvsGrhEmployes emp) {
        YvsBaseExercice exo = giveExerciceActif(new Date());
        int d = (int) dao.getNbreCongeSuppDu(emp.getId(), new Date(), (exo != null) ? exo.getId() : -1)
                + (int) dao.getNbreCongePrincipalDu(emp.getId(), new Date(), (exo != null) ? exo.getId() : 0);
        return d - dureeCongePris(emp);
    }

    public Employe searchEmployeActif(boolean open) {
        return searchEmployeActif(numSearch, open);
    }

    public void load() {
        if (agence != null ? agence.getId() < 1 : true) {
            agence = currentAgence;
        }
        addParamAgence_((agence != null) ? agence.getId() : null);
    }

//    public void loadAllByAgence(boolean avancer, boolean init) {
//        pa.addParam(new ParametreRequete("y.agence", "agence", agence, "=", "AND"));
//        pa.addParam(new ParametreRequete("y.actif", "actif", true, "=", "AND"));
//        listEmployes = buildListEmployeBean(pa.executeDynamicQuery("YvsGrhEmployes", "y.nom, y.prenom", avancer, init, (int) imax, dao));
//    }
//    public void loadAllBySociete(boolean avancer, boolean init) {
//        pa.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
//        pa.addParam(new ParametreRequete("y.actif", "actif", true, "=", "AND"));
//        listEmployes = buildListEmployeBean(pa.executeDynamicQuery("YvsGrhEmployes", "y.nom, y.prenom", avancer, init, (int) imax, dao));
//    }
    public Employe searchEmployeActif(String num, boolean open) {
        Employe a = new Employe();
        a.setMatricule(num);
        a.setError(true);
        addParam(new ParametreRequete("y.matricule", "matricule", null));
        if (num != null ? num.trim().length() > 0 : false) {
            ParametreRequete p = new ParametreRequete(null, "matricule", num.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.matricule)", "matricule", num.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nom)", "nom", num.toUpperCase() + "%", "LIKE", "OR"));
            addParam(p);
            loadAllEmployesByAgence(true, true);
            if (listEmployes != null ? !listEmployes.isEmpty() : false) {
                if (listEmployes.size() > 1) {
                    if (open) {
                        openDialog("dlgListEmployes");
                    }
                    a.setViewList(true);
                } else {
                    YvsGrhEmployes c = listEmployes.get(0);
                    a = UtilGrh.buildBeanSimplePartialEmploye(c);
                    a.setNom(a.getNom_prenom());
                }
                a.setError(false);
            } else {
                a.setNom(num);
            }
        }
        return a;
    }

    public void initEmployes(Employe a) {
        if (a == null) {
            a = new Employe();
        }
        addParam(new ParametreRequete("y.matricule", "matricule", null));
        loadAllEmployesByAgence(true, true);
        a.setViewList(true);
    }

    // Choosepaginator pour les articles actifs
    public void _choosePaginator(ValueChangeEvent ev) {
        if ((ev != null) ? ev.getNewValue() != null : false) {
            long v = (long) ev.getNewValue();
            imax = v;
        }
        loadAllEmployesByAgence(true, true);
    }

    /*Ajoute les paramètres de comptes*/
    public void ecouteSaisieCG(String numCmpte) {
        //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
        ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (service != null && (numCmpte != null) ? !numCmpte.isEmpty() : false) {
            service.findCompteByNum(numCmpte);
            employe.getCompteCollectif().setError(service.getListComptes().isEmpty());
            if (service.getListComptes() != null) {
                if (!service.getListComptes().isEmpty()) {
                    if (service.getListComptes().size() == 1) {
                        employe.getCompteCollectif().setError(false);
                        cloneObject(employe.getCompteCollectif(), UtilCompta.buildBeanCompte(service.getListComptes().get(0)));
                    } else {
                        employe.getCompteCollectif().setError(false);
                        openDialog("dlgCmpteG");
                        update("table_cptG_employe_s");
                    }
                } else {
                    employe.getCompteCollectif().setError(false);

                }
            } else {
                employe.getCompteCollectif().setError(false);

            }
        }

    }

    public void choisirCompte(SelectEvent ev) {
        if (ev != null) {
            cloneObject(employe.getCompteCollectif(), UtilCompta.buildSimpleBeanCompte(((YvsBasePlanComptable) ev.getObject())));

        }
    }

    public void ecouteSaisieCT(String numCmpte) {
        //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
        ManagedTiers service = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (service != null && (numCmpte != null) ? !numCmpte.isEmpty() : false) {
            service.addParamCode(numCmpte);
            employe.getCompteTiers().setError(service.getListTiers().isEmpty());
            System.err.println(" Error >>> " + service.getListTiers().isEmpty());
            if (service.getListTiers() != null) {
                if (!service.getListTiers().isEmpty()) {
                    if (service.getListTiers().size() == 1) {
                        employe.getCompteTiers().setError(false);
                        cloneObject(employe.getCompteTiers(), UtilTiers.buildBeanTiers(service.getListTiers().get(0)));
                    } else {
                        employe.getCompteTiers().setError(false);
                        openDialog("dlgListTiers");
                        update("data_tiers_employe");
                    }
                } else {
                    employe.getCompteTiers().setError(false);

                }
            } else {
                employe.getCompteTiers().setError(false);

            }
        }

    }

    public void choisirCompteTiers(SelectEvent ev) {
        if (ev != null) {
            cloneObject(employe.getCompteTiers(), UtilTiers.buildBeanTiers((YvsBaseTiers) ev.getObject()));
        }
    }

    public void generatedCompteTiers() {
        if (employe.getId() > 0) {
            ManagedTiers service = (ManagedTiers) giveManagedBean(ManagedTiers.class);
            if (service != null) {
                Tiers te = buildTiers();
                YvsBaseTiers tier = service.saveNew(te);
                if (tier != null) {
                    te.setId(tier.getId());
                    employe.setCompteTiers(te);
                    succes();
                }
            }
        } else {
            getErrorMessage("Aucun employé n'a été sélectionné !");
        }
    }

    public void saveLiaison() {
        if (employe.getId() > 0) {
            if ((codeExterne != null) ? codeExterne.trim().length() > 2 : false) {
                //il ne dois pas déjà exister un mappage pour ce client
                YvsExtEmploye y = (YvsExtEmploye) dao.loadOneByNameQueries("YvsExtEmploye.findByEmploye", new String[]{"employe"}, new Object[]{new YvsGrhEmployes(employe.getId())});
                if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                    y.setCodeExterne(codeExterne);
                    y.setDateSave(y.getDateSave());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                } else {
                    y = new YvsExtEmploye();
                    y.setEmploye(new YvsGrhEmployes(employe.getId()));
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
            getErrorMessage("Aucun employe n'est selectionné !");
        }
    }

    private Tiers buildTiers() {
        Tiers te = new Tiers();
        te.setUpdate(false);
        te.setActif(employe.isActif());
        te.setAdresse(employe.getAdresse1());
        te.setAgence(employe.getAgence());
        te.setBp(employe.getAdresse2());
        te.setCivilite(employe.getCivilite());
        te.setClient(false);
        te.setCodePersonel(employe.getMatricule());
        te.setCompte(employe.getCompteCollectif());
        te.setEmploye(true);
        te.setIdAgence(employe.getAgence().getId());
        te.setNom(employe.getNom());
        te.setPrenom(employe.getPrenom());
        te.setPays(employe.getPaysDorigine());
        te.setCodeTiers("P-" + employe.getNom() + "" + ((employe.getPrenom() != null) ? "_" + employe.getPrenom() : ""));
        return te;
    }

    public void loadRecapitulatif() {
        if (employe != null ? employe.getId() > 0 : false) {
            ManagedTableauBord s = (ManagedTableauBord) giveManagedBean(ManagedTableauBord.class);
            if (s != null) {
                s.getTableauBord().setEmploye(employe);
                s.recolteAllInfos();
            }
        } else {
            getWarningMessage("Vous devez selectionner l'employé");
        }
    }

    public void openListPlanAnal() {
        if (employe.getId() > 0) {
            ManagedCentreAnalytique service = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
            if (service != null) {
                service.loadAllByNamedQuery();
                int idx;
                for (YvsComptaAffecAnalEmp ca : employe.getSectionsAnalytiques()) {
                    idx = service.getCentres().indexOf(ca.getCentre());
                    if (idx >= 0) {
                        service.getCentres().get(idx).setIdAffectation(ca.getId());
                        service.getCentres().get(idx).setCoeficient(ca.getCoeficient());
                        service.getCentres().get(idx).setDateSave_(ca.getDateSave());
                    }
                }
                update("data_centre_analytique_emps");
                openDialog("dlgAffec");
            }
        } else {
            getErrorMessage("Veuillez selectionner un employé !");

        }
    }

    public void saveNewSectionEmploye(YvsComptaCentreAnalytique ca, boolean all) {
        if (employe.getId() > 0) {
            YvsComptaAffecAnalEmp ae;
            ae = new YvsComptaAffecAnalEmp();
            ae.setAuthor(currentUser);
            ae.setCentre(ca);
            ae.setCoeficient(ca.getCoeficient());
            ae.setDateSave(ca.getDateSave_());
            ae.setDateUpdate(new Date());
            ae.setEmploye(employes);
            if (ca.getIdAffectation() <= 0) {
                ae = (YvsComptaAffecAnalEmp) dao.save1(ae);
                ca.setIdAffectation(ae.getId());
                employe.getSectionsAnalytiques().add(0, ae);
            } else {
                ae.setDateUpdate(new Date());
                ae.setId(ca.getIdAffectation());
                dao.update(ae);
            }
            if (!all) {
                succes();
                update("data_centre_analytique_emps");
                update("tableSectionAnal");
            }
        } else {
            if (!all) {
                getErrorMessage("Aucun employé n'a été selectionné !");
            }
        }
    }

    public void saveNewSectionsEmployes() {
        ManagedCentreAnalytique service = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
        if (service != null && employe.getId() > 0) {
            for (YvsComptaCentreAnalytique ca : service.getCentres()) {
                if (ca.getIdAffectation() > 0 || ca.getCoeficient() > 0) {
                    saveNewSectionEmploye(ca, true);
                }
            }
            succes();
            update("data_centre_analytique_emps");
            update("tableSectionAnal");
        } else {
            if (employe.getId() <= 0) {
                getErrorMessage("Aucun employé n'a été selectionné.", "veuiller en selectionner un !");
            }
        }
    }

    public void removeLineAffecAnal(YvsComptaAffecAnalEmp ef) {
        try {
            ef.setAuthor(currentUser);
            ef.setDateUpdate(new Date());
            dao.delete(ef);
            employe.getSectionsAnalytiques().remove(ef);
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cette ligne !");
            getException("Lymytz Error...", ex);
        }
    }

    public void onSelectDistantContrat(YvsGrhEmployes y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedContratEmploye s = (ManagedContratEmploye) giveManagedBean(ManagedContratEmploye.class);
            if (s != null ? y.getContrat() != null : false) {
                s.onSelectObject(y.getContrat());
                Navigations n = (Navigations) giveManagedBean(Navigations.class);
                if (n != null) {
                    n.naviguationView("Contrats", "modRh", "smenContratEmps", true);
                }
            }
        }
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllEmployesByAgence(true, true);
    }

    public void addParamStatutSociel(ValueChangeEvent ev) {
        if (ev != null) {
            ParametreRequete p = new ParametreRequete(null, "matSocial", "x", "=", "AND");
            Integer val = (Integer) ev.getNewValue();
            if (val != null) {
                if (val == 1) { //avec matricule
                    p.getOtherExpression().add(new ParametreRequete("y.matriculeCnps", "matSocial", "x", "IS NOT NULL", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("y.matriculeCnps", "matSocial", "", "!=", "OR"));
                } else {//sans matricule
                    p.getOtherExpression().add(new ParametreRequete("y.matriculeCnps", "matSocial", "x", "IS NULL", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("y.matriculeCnps", "matSocial", "", "=", "OR"));
                }
            }else p.setObjet(null);
            paginator.addParam(p);
            loadAllEmployesByAgence(true, true);
        }
    }

    public void activeTiers(YvsGrhEmployes bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            bean.setAuthor(currentUser);
            bean.setDateUpdate(new Date());
            dao.update(bean);
            if (actionTiers) {
                bean.getCompteTiers().setActif(bean.getActif());
                bean.getCompteTiers().setAuthor(currentUser);
                bean.getCompteTiers().setDateUpdate(new Date());
                dao.update(bean.getCompteTiers());
            }
            if (actionClient) {
                for (YvsComClient y : bean.getCompteTiers().getClients()) {
                    y.setActif(bean.getActif());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                }
            }
            if (actionFournisseur) {
                for (YvsBaseFournisseur y : bean.getCompteTiers().getFournisseurs()) {
                    y.setActif(bean.getActif());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                }
            }
            if (actionCommercial) {
                for (YvsComComerciale y : bean.getCompteTiers().getCommerciaux()) {
                    y.setActif(bean.getActif());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                }
            }
            int index = listEmployes.indexOf(bean);
            if (index > -1) {
                listEmployes.set(index, bean);
                update("tabEmployes");
                update("empps-dgrig");
            }
        }
    }

    @Override
    public void onSelectDistant(YvsGrhEmployes y) {
        Navigations n = (Navigations) giveManagedBean(Navigations.class);
        if (n != null) {
            n.naviguationView("Employés", "modRh", "smenEmploye", true);
        }
        onSelectObject(y);
    }

    public String getPhoto(YvsGrhEmployes y) {
        try {
            if (y != null) {
                String photos = y.getPhotos();
                String photo = y.getCivilite().trim().equals("M.") ? Constantes.DEFAULT_PHOTO_EMPLOYE_MAN() : Constantes.DEFAULT_PHOTO_EMPLOYE_WOMAN();
                return photos != null ? photos.trim().length() > 0 ? photos : photo : photo;
            }
        } catch (Exception ex) {
            getException("getPhoto", ex);
        }
        return Constantes.DEFAULT_PHOTO_EMPLOYE_MAN();
    }

    public void fusionner(boolean fusionne) {
        try {
            if (!autoriser("base_user_fusion")) {
                openNotAcces();
                return;
            }
            fusionneTo = "";
            fusionnesBy.clear();
            if (!listSelection.isEmpty() ? listSelection.size() > 1 : false) {
                long newValue = listSelection.get(0).getId();
                if (!fusionne) {
                    String oldValue = "0";
                    for (YvsGrhEmployes i : listSelection) {
                        if (i.getId() != newValue) {
                            oldValue += "," + i.getId();
                        }
                    }
                    if (dao.fusionneData("yvs_grh_employes", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                listEmployes.remove(new YvsGrhEmployes(id));
                            }
                        }
                    }
                    listSelection.clear();
                    succes();
                } else {
                    fusionneTo = listSelection.get(0).getNom_prenom();
                    for (YvsGrhEmployes i : listSelection) {
                        long oldValue = i.getId();
                        if (oldValue != newValue) {
                            fusionnesBy.add(i.getNom_prenom());
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 employé");
            }
        } catch (NumberFormatException ex) {
        }
    }

    public void print() {
        print("pdf");
    }

    public void print(String format) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("AGENCE", (int) idAgenceSearch);
            param.put("NAME_AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
            param.put("DEPARTEMENT", 0);
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR());
            param.put("ORDRE", orderPrint);
            executeReport(format.equals("xls") ? "employes_no_header" : "employes", param, "", format, false);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedEmployes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
