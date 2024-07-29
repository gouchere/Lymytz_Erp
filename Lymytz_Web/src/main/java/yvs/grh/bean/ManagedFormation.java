/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
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
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.activite.YvsCoutsFormation;
import yvs.entity.grh.activite.YvsCoutsFormationPK;
import yvs.entity.grh.activite.YvsGrhFormateur;
import yvs.entity.grh.activite.YvsGrhFormation;
import yvs.entity.grh.activite.YvsGrhFormationEmps;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.grh.activite.YvsGrhQualificationFormation;
import yvs.entity.grh.activite.YvsTitreFormation;
import yvs.entity.grh.param.YvsGrhDomainesQualifications;
import yvs.entity.grh.param.YvsGrhQualifications;
import yvs.entity.grh.personnel.YvsDiplomes;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.personnel.YvsSpecialiteDiplomes;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidFormation;
import yvs.grh.UtilGrh;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.poste.ManagedPosteDeTravail;
import yvs.parametrage.poste.SpecialiteDiplomes;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.UtilitaireGenerique;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class ManagedFormation extends Managed<Formation, YvsGrhFormation> implements Serializable {

    @ManagedProperty(value = "#{formation}")
    private Formation formation;

    private List<YvsGrhFormation> listFormations, selectionFormation, listchargementFormation;
    private List<Employe> employes, selectionEmployes;
    private YvsGrhFormation entityFormations;
    private Formateur formateur = new Formateur();
    private TypeCout typeCout = new TypeCout();
    private String nameVueListe = "Liste", nameListeEncour = "LISTE DES FORMATIONS EN COURS", nameBtnVue = "Nouveau";
//    private boolean displayOnglet, displayForm, displayEncours = true, displayListe = true, displayBtnOption, activBtnnouveau = true;
    private String newVille, newPays;
    private Dictionnaire pay = new Dictionnaire();
    private Diplomes newDiplome = new Diplomes();
    private SpecialiteDiplomes specialite = new SpecialiteDiplomes();
    private boolean displayBtnDelFormEmps, displayBtnAddEmp, activBtnSuppQualif, activBtnDeleteCout;
//    private boolean activVueClassique, activVueEmps, vueByGroup;
    private FormationEmps formEMps = new FormationEmps();
    private Qualification qualif = new Qualification();
    private List<QualificationFormation> selectionQualifForm;
    private List<YvsGrhFormateur> listFormateur, templateSearch;
    private List<TypeCout> listTypeCout;
    private CoutFormation cout = new CoutFormation();
    /*----------------------------*/
    public List<CoutFormation> listcout, listcoutTableau;
    private List<Qualification> listqualification;
    public List<Qualification> qualifications;
    public List<YvsDictionnaire> villes, pays;
    public List<String> titreFormations;
    private List<YvsDiplomes> diplomes;
    private List<YvsSpecialiteDiplomes> specialites;

//    private boolean disableB = true;
    private boolean activeTab = false;
    private boolean updateformation = false, updateformationEmps = false;
    private boolean activBtnSupprimer = false;
    private Formation selectedFormation;
    private String chaineSelectFormation, chaineSelectEmployeFormation, chaineSelectQualifFormation;

    YvsGrhFormateur entityFormateur;

    private String textFindFormateur;

    private List<YvsGrhDomainesQualifications> domainesQualification;
    private YvsGrhDomainesQualifications selectDomaineQ = new YvsGrhDomainesQualifications();
    private Qualification newQualif = new Qualification();

    public ManagedFormation() {
        cout = new CoutFormation();
        listFormateur = new ArrayList<>();
        selectionFormation = new ArrayList<>();
        listcout = new ArrayList<>();
        listcoutTableau = new ArrayList<>();
        listqualification = new ArrayList<>();
        qualifications = new ArrayList<>();
        villes = new ArrayList<>();
        pays = new ArrayList<>();
        diplomes = new ArrayList<>();
        titreFormations = new ArrayList<>();
        employes = new ArrayList<>();
        listFormations = new ArrayList<>();
        selectionQualifForm = new ArrayList<>();
        listTypeCout = new ArrayList<>();
        listchargementFormation = new ArrayList<>();
        specialites = new ArrayList<>();
    }

    public List<YvsGrhDomainesQualifications> getDomainesQualification() {
        return domainesQualification;
    }

    public void setDomainesQualification(List<YvsGrhDomainesQualifications> domainesQualification) {
        this.domainesQualification = domainesQualification;
    }

    public YvsGrhDomainesQualifications getSelectDomaineQ() {
        return selectDomaineQ;
    }

    public void setSelectDomaineQ(YvsGrhDomainesQualifications selectDomaineQ) {
        this.selectDomaineQ = selectDomaineQ;
    }

    public Dictionnaire getPay() {
        return pay;
    }

    public void setPay(Dictionnaire pay) {
        this.pay = pay;
    }

    public SpecialiteDiplomes getSpecialite() {
        return specialite;
    }

    public void setSpecialite(SpecialiteDiplomes specialite) {
        this.specialite = specialite;
    }

    public List<YvsSpecialiteDiplomes> getSpecialites() {
        return specialites;
    }

    public void setSpecialites(List<YvsSpecialiteDiplomes> specialites) {
        this.specialites = specialites;
    }

    public String getTextFindFormateur() {
        return textFindFormateur;
    }

    public void setTextFindFormateur(String textFindFormateur) {
        this.textFindFormateur = textFindFormateur;
    }

    public String getChaineSelectEmployeFormation() {
        return chaineSelectEmployeFormation;
    }

    public void setChaineSelectEmployeFormation(String chaineSelectEmployeFormation) {
        this.chaineSelectEmployeFormation = chaineSelectEmployeFormation;
    }

    public String getChaineSelectQualifFormation() {
        return chaineSelectQualifFormation;
    }

    public void setChaineSelectQualifFormation(String chaineSelectQualifFormation) {
        this.chaineSelectQualifFormation = chaineSelectQualifFormation;
    }

    public String getChaineSelectFormation() {
        return chaineSelectFormation;
    }

    public void setChaineSelectFormation(String chaineSelectFormation) {
        this.chaineSelectFormation = chaineSelectFormation;
    }

    public TypeCout getTypeCout() {
        return typeCout;
    }

    public void setTypeCout(TypeCout typeCout) {
        this.typeCout = typeCout;
    }

    public Formateur getFormateur() {
        return formateur;
    }

    public void setFormateur(Formateur formateur) {
        this.formateur = formateur;
    }

    public void setListFormateur(List<YvsGrhFormateur> listFormateur) {
        this.listFormateur = listFormateur;
    }

    public List<YvsGrhFormateur> getListFormateur() {
        return listFormateur;
    }

    public String getNameBtnVue() {
        return nameBtnVue;
    }

    public void setNameBtnVue(String nameBtnVue) {
        this.nameBtnVue = nameBtnVue;
    }

    public boolean isUpdateformationEmps() {
        return updateformationEmps;
    }

    public void setUpdateformationEmps(boolean updateformationEmps) {
        this.updateformationEmps = updateformationEmps;
    }

    public String getNameListeEncour() {
        return nameListeEncour;
    }

    public void setNameListeEncour(String nameListeEncour) {
        this.nameListeEncour = nameListeEncour;
    }

    public String getNameVueListe() {
        return nameVueListe;
    }

    public void setNameVueListe(String nameVueListe) {
        this.nameVueListe = nameVueListe;
    }

    public List<YvsGrhFormation> getListchargementFormation() {
        return listchargementFormation;
    }

    public void setListchargementFormation(List<YvsGrhFormation> listchargementFormation) {
        this.listchargementFormation = listchargementFormation;
    }

    public boolean isActivBtnDeleteCout() {
        return activBtnDeleteCout;
    }

    public void setActivBtnDeleteCout(boolean activBtnDeleteCout) {
        this.activBtnDeleteCout = activBtnDeleteCout;
    }

    public List<TypeCout> getListTypeCout() {
        return listTypeCout;
    }

    public void setListTypeCout(List<TypeCout> listTypeCout) {
        this.listTypeCout = listTypeCout;
    }

    public boolean isActivBtnSuppQualif() {
        return activBtnSuppQualif;
    }

    public void setActivBtnSuppQualif(boolean activBtnSuppQualif) {
        this.activBtnSuppQualif = activBtnSuppQualif;
    }

    public List<QualificationFormation> getSelectionQualifForm() {
        return selectionQualifForm;
    }

    public void setSelectionQualifForm(List<QualificationFormation> selectionQualifForm) {
        this.selectionQualifForm = selectionQualifForm;
    }

    public Qualification getNewQualif() {
        return newQualif;
    }

    public void setNewQualif(Qualification newQualif) {
        this.newQualif = newQualif;
    }

    public Qualification getQualif() {
        return qualif;
    }

    public void setQualif(Qualification qualif) {
        this.qualif = qualif;
    }

    public FormationEmps getFormEMps() {
        return formEMps;
    }

    public void setFormEMps(FormationEmps formEMps) {
        this.formEMps = formEMps;
    }

    public Diplomes getNewDiplome() {
        return newDiplome;
    }

    public void setNewDiplome(Diplomes newDiplome) {
        this.newDiplome = newDiplome;
    }

    public String getNewVille() {
        return newVille;
    }

    public void setNewVille(String newVille) {
        this.newVille = newVille;
    }

    public void setNewPays(String newPays) {
        this.newPays = newPays;
    }

    public String getNewPays() {
        return newPays;
    }

    public List<Employe> getSelectionEmployes() {
        return selectionEmployes;
    }

    public void setSelectionEmployes(List<Employe> selectionEmployes) {
        this.selectionEmployes = selectionEmployes;
    }

    public boolean isActiveTab() {
        return activeTab;
    }

    public void setActiveTab(boolean activeTab) {
        this.activeTab = activeTab;
    }

    public boolean isActivBtnSupprimer() {
        return activBtnSupprimer;
    }

    public void setActivBtnSupprimer(boolean activBtnSupprimer) {
        this.activBtnSupprimer = activBtnSupprimer;
    }

    public YvsDiplomes getCurrentDiplome() {
        return currentDiplome;
    }

    public void setCurrentDiplome(YvsDiplomes currentDiplome) {
        this.currentDiplome = currentDiplome;
    }

    public YvsGrhQualifications getCurrentQualification() {
        return currentQualification;
    }

    public void setCurrentQualification(YvsGrhQualifications currentQualification) {
        this.currentQualification = currentQualification;
    }

    public boolean isUpdateformation() {
        return updateformation;
    }

    public void setUpdateformation(boolean updateformation) {
        this.updateformation = updateformation;
    }

    public boolean isActiveTabView() {
        return activeTab;
    }

    public void setActiveTabView(boolean activeTabView) {
        this.activeTab = activeTabView;
    }

    public List<YvsGrhFormation> getListFormations() {
        return listFormations;
    }

    public void setListFormations(List<YvsGrhFormation> listFormations) {
        this.listFormations = listFormations;
    }

    public List<Employe> getEmployes() {
        return employes;
    }

    public void setEmployes(List<Employe> employes) {
        this.employes = employes;
    }

    public List<YvsDictionnaire> getVilles() {
        return villes;
    }

    public void setVilles(List<YvsDictionnaire> villes) {
        this.villes = villes;
    }

    public List<YvsDictionnaire> getPays() {
        return pays;
    }

    public void setPays(List<YvsDictionnaire> pays) {
        this.pays = pays;
    }

    public List<YvsDiplomes> getDiplomes() {
        return diplomes;
    }

    public void setDiplomes(List<YvsDiplomes> diplomes) {
        this.diplomes = diplomes;
    }

    public List<String> getTitreFormations() {
        return titreFormations;
    }

    public void setTitreFormations(List<String> titreFormations) {
        this.titreFormations = titreFormations;
    }

    public List<CoutFormation> getListcoutTableau() {
        return listcoutTableau;
    }

    public void setListcoutTableau(List<CoutFormation> listcoutTableau) {
        this.listcoutTableau = listcoutTableau;
    }

    public List<Qualification> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<Qualification> qualifications) {
        this.qualifications = qualifications;
    }

    public List<YvsGrhFormation> getSelectionFormation() {
        return selectionFormation;
    }

    public void setSelectionFormation(List<YvsGrhFormation> selectionFormation) {
        this.selectionFormation = selectionFormation;
    }

    public List<CoutFormation> getListcout() {
        return listcout;
    }

    public void setListcout(List<CoutFormation> listcout) {
        this.listcout = listcout;
    }

    public Formation getFormation() {
        return formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public CoutFormation getCout() {
        return cout;
    }

    public void setCout(CoutFormation cout) {
        this.cout = cout;
    }

    public List<Qualification> getListqualification() {
        return listqualification;
    }

    public void setListqualification(List<Qualification> listqualification) {
        this.listqualification = listqualification;
    }

    public Formation getSelectedFormation() {
        return selectedFormation;
    }

    public void setSelectedFormation(Formation selectedFormation) {
        this.selectedFormation = selectedFormation;
    }

    public boolean isDisplayBtnAddEmp() {
        return displayBtnAddEmp;
    }

    public void setDisplayBtnAddEmp(boolean displayBtnAddEmp) {
        this.displayBtnAddEmp = displayBtnAddEmp;
    }

    public boolean isDisplayBtnDelFormEmps() {
        return displayBtnDelFormEmps;
    }

    public void setDisplayBtnDelFormEmps(boolean displayBtnDelFormEmps) {
        this.displayBtnDelFormEmps = displayBtnDelFormEmps;
    }

    public YvsGrhFormateur buildFormateur(Formateur e) {
        YvsGrhFormateur r = new YvsGrhFormateur();
        if (e != null) {
            r.setId(e.getId());
            r.setAdresse(e.getAdresse());
            r.setNom(e.getNom());
            r.setTelephone(e.getTelephone());
        }
        return r;
    }

    public void validation(FacesContext context, UIComponent toValidate, Object nom) {
        if ("".equals(nom.toString())) {
            ((UIInput) toValidate).setValid(false);
            getErrorMessage("Des champs obligatoires sont vides");
        }
    }

    public void confirmation() {
        openDialog("dlgConfirmDeleteFormation");
    }

//    YvsGrhFormation currentFormation;
    public void loadListeFormationClassique() {
//        activVueClassique = true;
//        activVueEmps = false;
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listFormations = dao.loadNameQueries("YvsFormation.findAll", champ, val);
//        vueByGroup = false;
        activBtnSupprimer = false;
        setNameListeEncour("LISTE DES FORMATIONS EN COURS");
        update("bloc-en-tete-formation");
        update("panel-formation");
        update("bloc-en-tete-formation");
    }

    public void loadListeFormationEnCours() {
        champ = new String[]{"societe", "date"};
        val = new Object[]{currentAgence.getSociete(), formation.getDateFin()};
        listFormations = dao.loadNameQueries("YvsFormation.findEnCours", champ, val);
        update("form-formation-02");
        update("panel-formation");
        update("bloc-en-tete-formation");
    }

    public void loadListeFormationEmps() {
        activBtnSupprimer = false;
        update("bloc-formation");
        update("bloc-en-tete-formation");
    }

    public void loadListeFormationQualif() {
//        vueByGroup = true;
//        activVueClassique = false;
//        activVueEmps = false;
        activBtnSupprimer = false;
        update("bloc-formation");
        update("bloc-en-tete-formation");
    }

    YvsDiplomes currentDiplome;

    public void controleDiplome(String str) {
        champ = new String[]{"nom", "societe"};
        val = new Object[]{str, currentAgence.getSociete()};
        currentDiplome = (YvsDiplomes) dao.loadOneByNameQueries("YvsDiplomes.findByNom", champ, val);
        if (currentDiplome == null) {
            openDialog("dlgadddiplome");
        }
    }

    public void addDiplome() {
        YvsDiplomes di = new YvsDiplomes();
        di.setSociete(currentAgence.getSociete());
        currentDiplome = (YvsDiplomes) dao.save1(di);
        succes();
        update("tab-formation");
    }

    public List<String> findTitreFormation(String query) {
        List<String> listeType = new ArrayList<>();
        for (String str : titreFormations) {
            if (str.startsWith(query)) {
                listeType.add(str);
            }
        }

        return listeType;
    }

    public void controleTitre(String str) {
        int r = UtilitaireGenerique.triDichontomique(titreFormations, str);
        if (r < 0) {
            openDialog("dlgaddtitreformation");
        }
    }

    public void addTitreFormation() {
        YvsTitreFormation tf = new YvsTitreFormation();
        tf.setLibelle(formation.getTitreFormation());
        tf.setSociete(currentAgence.getSociete());
        dao.save(tf);
        succes();
        titreFormations.add(tf.getLibelle());
        update("tab-formation");
    }
    YvsGrhQualifications currentQualification;

    public void addNewQualification() {
        YvsGrhQualifications c = new YvsGrhQualifications();
        currentQualification = (YvsGrhQualifications) dao.save1(c);
    }

    public void saveNewFormateur() {
        try {
            if (controleFicheFormateur(formateur)) {
                entityFormateur = buildFormateur(formateur);
                entityFormateur.setId(null);
                entityFormateur = (YvsGrhFormateur) dao.save1(entityFormateur);
                formateur.setId(entityFormateur.getId());
                listFormateur.add(entityFormateur);
                templateSearch.add(entityFormateur);
                succes();
                update("form-formation-07");
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            Logger.getLogger(ManagedFormation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void findFormateur(String search) {
        if (search != null) {
            if (search.length() <= 0) {
                loadAllFormateur();
            } else {
                listFormateur.clear();
                for (YvsGrhFormateur f : templateSearch) {
                    if (f.getNom().toLowerCase().startsWith(search.toLowerCase())) {
                        listFormateur.add(f);
                    }
                }
            }
        }
    }

    public void saveNewTypeCout() {
        try {
            if (typeCout.getLibelle() != null && !typeCout.getLibelle().equals("")) {
                YvsGrhTypeCout entity = new YvsGrhTypeCout();
                entity.setLibelle(typeCout.getLibelle());
                entity.setSociete(currentAgence.getSociete());
                entity = (YvsGrhTypeCout) dao.save1(entity);
                typeCout.setId(entity.getId());
                TypeCout t = new TypeCout();
                cloneObject(t, typeCout);
                cout.setTypeCout(typeCout);
                listTypeCout.add(t);
                succes();
                update("coutFormation");
            } else {
                getErrorMessage("Vous devez entrer le libelle");
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            Logger.getLogger(ManagedFormation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void choixFormateur(SelectEvent ev) {
        YvsGrhFormateur f = (YvsGrhFormateur) ev.getObject();
        formation.setFormateur(new Formateur(f.getId(), f.getNom()));
        update("chp_select_formateur");
    }

    public void deleteFormateur(YvsGrhFormateur f) {
        try {
            f.setAuthor(currentUser);
            dao.delete(f);
            listFormateur.remove(f);
            succes();
        } catch (Exception ex) {
            Logger.getLogger(ManagedFormation.class.getName()).log(Level.SEVERE, null, ex);
            getErrorMessage("Impossible de supprimer cet élément !");
        }
    }

    @Override
    public boolean controleFiche(Formation bean) {
        if (bean.getTitreFormation() == null) {
            getMessage("Vous devez saisir le titre de cette formation !", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    public boolean controleFicheFormateur(Formateur bean) {
        if (bean.getAdresse() == null) {
            getMessage("Vous devez saisir l'adresse !", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getNom() == null) {
            getMessage("Vous devez saisir le nom!", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getTelephone() == null) {
            getMessage("Vous devez saisir le telephone !", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public Formation recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(Formation bean) {
        formation.setDateDebut(bean.getDateDebut());
        formation.setDateFin(bean.getDateFin());
        formation.setDateFormation(bean.getDateFormation());
    }

    @Override
    public void resetFiche() {
        resetFiche(formation);
        formation.setEmployes(new ArrayList<FormationEmps>());
        formation.setDiplome(new Diplomes());
        formation.setQualifications(new ArrayList<DomainesQualifications>());
        formation.setLieuParDefaut(new Dictionnaire());
        formation.setFormateur(new Formateur());
        updateformation = false;
        resetFiche(qualif);
        update("form-formation-07");
    }

    @Override
    public void resetPage() {
        if (listFormations != null) {
//            for (Formation f : listFormations) {
//                listFormations.get(listFormations.indexOf(f)).setSelectActif(false);
//            }
        }
        selectionFormation.clear();
    }

    @Override
    public void loadAll() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        //charge la liste des diplomes
        diplomes = dao.loadNameQueries("YvsDiplomes.findAll", champ, val);
        specialites = dao.loadNameQueries("YvsSpecialiteDiplomes.findAll", champ, val);
        //charge les titre de listFormations
        titreFormations.clear();
        List<YvsTitreFormation> lf = dao.loadNameQueries("YvsTitreFormation.findAll", champ, val);
        for (YvsTitreFormation f : lf) {
            titreFormations.add(f.getLibelle());
        }
        //charge les qualifications
        List<YvsGrhQualifications> lq = dao.loadNameQueries("YvsQualifications.findAll", champ, val);
        listqualification.clear();
        for (YvsGrhQualifications q : lq) {
            listqualification.add(new Qualification(q.getId(), q.getDesignation()));
        }
        //charge la liste des villes
        champ = new String[]{"titre"};
        val = new Object[]{Constantes.T_VILLES};
        villes = dao.loadNameQueries("YvsDictionnaire.findByTitre", champ, val);
        val = new Object[]{Constantes.T_PAYS};
        pays = dao.loadNameQueries("YvsDictionnaire.findByTitre", champ, val);
        loadListeFormationClassique();
//        String[] ch = new String[]{"societe"};
//        Object[] va = new Object[]{currentUser.getAgence().getSociete()};
//        domainesQualification = dao.loadNameQueries("YvsGrhDomainesQualifications.findAll", ch, va);
    }

    public void loadAllFormateur() {
        listFormateur = dao.loadNameQueries("YvsFormateur.findAll", new String[]{}, new Object[]{});
        if (templateSearch == null) {
            templateSearch = new ArrayList<>();
        }
        templateSearch.addAll(listFormateur);
    }

    public void loadAllTypeCout() {
        listTypeCout.clear();
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listTypeCout = UtilGrh.buildBeanListeTypeCout(dao.loadNameQueries("YvsGrhTypeCout.findAll", champ, val));
    }

    public void loadFormationOneEmploye() {
        formEMps.getCoutFormation().clear();
        champ = new String[]{"employe"};
        val = new Object[]{new YvsGrhEmployes(formEMps.getEmploye().getId())};
        formEMps.setCoutFormation(UtilGrh.buildListBeanCoutFormation(dao.loadNameQueries("YvsCoutsFormation.findByEmploye", champ, val)));
    }
    /**
     * *
     * CHOIX D'UN EMPLOYE
     */
    private YvsGrhEmployes seleEmployeEmploye;
    private boolean vueListe;

    public YvsGrhEmployes getSeleEmployeEmploye() {
        return seleEmployeEmploye;
    }

    public void setSeleEmployeEmploye(YvsGrhEmployes seleEmployeEmploye) {
        this.seleEmployeEmploye = seleEmployeEmploye;
    }

    public boolean isVueListe() {
        return vueListe;
    }

    public void setVueListe(boolean vueListe) {
        this.vueListe = vueListe;
    }

    public void activeVueList() {
        vueListe = false;
    }

    public void desactiveVueListe() {
        vueListe = true;
    }

    public void choixEmploye1(Employe ev) {
        if (ev != null) {
            if (selectionEmployes.contains(ev)) {
                selectionEmployes.remove(ev);
            } else {
                selectionEmployes.add(ev);
            }
        }
    }

    public void findEmps() {
        offsetEmps = 0;

    }

    int offsetEmps = 0;
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

    public void loadAllEmploye(int limit, boolean init) {
        if (!init) {
            offsetEmps = +limit;
        } else {
            offsetEmps = 0;
        }
        employes.clear();
        champ = new String[]{"agence"};
        val = new Object[]{currentAgence};
        employes.clear();
//        employes = UtilGrh.buildListEmployeBean(dao.loadNameQueries("YvsGrhEmployes.findAll", champ, val, offsetEmps, limit), null);
        int n = employes.size();
        disPrevEmps = offsetEmps <= 0;
        if (n < limit) {
            disNextEmps = true;
        } else if (n == 0) {
            disNextEmps = true;
            disPrevEmps = true;
        } else if (n > 0) {
            disNextEmps = false;
            disPrevEmps = false;
        }
    }

    public void choixDiplome(ValueChangeEvent ev) {
        if (ev != null && ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id == -1) {
                openDialog("dlgAddDiplome");
            }
        }
    }

    public void createNewSpecialite() {
        if (specialite.getCodeInterne() != null && specialite.getDesignation() != null) {
            YvsSpecialiteDiplomes sp = new YvsSpecialiteDiplomes();
            sp.setAuthor(currentUser);
            sp.setCodeInterne(specialite.getCodeInterne());
            sp.setTitreSpecialite(specialite.getDesignation());
            sp = (YvsSpecialiteDiplomes) dao.save1(sp);
            specialite.setId(sp.getId());
            specialites.add(0, sp);
            newDiplome.setSpecialite(specialite);
            specialite = new SpecialiteDiplomes();
        }
    }

//    public void choixQualif(ValueChangeEvent ev) {
//        if (ev != null && ev.getNewValue() != null) {
//            long id = (long) ev.getNewValue();
//            if (id == -1) {
//                openDialog("dlgAddQualif");
//            }
//        }
////    }
//    public void createDiplome() {
//        if ((newDiplome != null) ? !newDiplome.trim().equals("") : false) {
//            YvsDiplomes d = new YvsDiplomes();
//            d.setNom(newDiplome);
//            d.setSociete(currentAgence.getSociete());
//            d.setSupp(false);
//            d.setActif(true);
//            d = (YvsDiplomes) dao.save1(d);
//            Diplomes dd = new Diplomes(d.getId(), newDiplome);
//            diplomes.add(dd);
//            formation.setDiplome(dd);
//            newDiplome = null;
//            closeDialog("dlgAddDiplome");
//            
//        }
//    }
    public void choixDiplomes(YvsDiplomes d) {
        formation.setDiplome(new Diplomes(d.getId(), d.getCodeInterne()));
    }

    public void createNewDiplome() {
        if (newDiplome.getCodeInterne() != null && newDiplome.getSpecialite().getId() > 0) {
            YvsDiplomes d = UtilGrh.buildDiplome(newDiplome);
            d.setAuthor(currentUser);
            d.setActif(true);
            d.setSupp(false);
            d.setSociete(currentAgence.getSociete());
            d.setSpecialite(specialites.get(specialites.indexOf(d.getSpecialite())));
            d.setId(null);
            d = (YvsDiplomes) dao.save1(d);
            newDiplome.setId(d.getId());
            diplomes.add(0, d);
            formation.setDiplome(newDiplome);
            specialites.get(specialites.indexOf(d.getSpecialite())).getDiplomes().add(d);
            newDiplome = new Diplomes();
            succes();
            update("select_diplome_form");
        } else {
            if (newDiplome.getSpecialite().getId() <= 0) {
                getErrorMessage("Veuillez choisir la spécialité !");
            } else {
                getErrorMessage("Vous devez indiquer l'intitulé du diplôme");
            }
        }
    }

    public void deleteDiplome(YvsDiplomes dip) {
        try {
            dip.setAuthor(currentUser);
            dao.delete(dip);
            specialites.get(specialites.indexOf(dip.getSpecialite())).getDiplomes().remove(dip);

        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet élément !");
            Logger.getLogger(ManagedFormation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createNewQualification() {
        if ((newQualif.getIntitule() != null) ? !newQualif.getIntitule().trim().equals("") : false) {
            YvsGrhQualifications qa = new YvsGrhQualifications();
            qa.setDesignation(newQualif.getIntitule());
            qa.setCodeInterne(newQualif.getCodeInterne());
            if (newQualif.getDomaine().getId() > 0) {
                qa.setDomaine(new YvsGrhDomainesQualifications(newQualif.getDomaine().getId()));
            } else {
                getErrorMessage("Vous devez selectionner un domaine de compétence !");
                return;
            }
            qa.setAuthor(currentUser);
            //récupère les domaines
            ManagedPosteDeTravail service = (ManagedPosteDeTravail) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("managedPosteDeTravail");
            if (service != null) {
                domainesQualification = service.getDomaines();
            }
            if (true) {
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
        }
    }

    public void choixVille(ValueChangeEvent ev) {
        if (ev != null && ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id == -1) {
                openDialog("dlgAddVille");
            }
        }
    }

    public void createPays() {
        if ((newPays != null) ? !newPays.trim().equals("") : false) {
            YvsDictionnaire d = new YvsDictionnaire();
            d.setTitre(Constantes.T_PAYS);
            d.setLibele(newPays);
            d.setSociete(currentAgence.getSociete());
            d.setSupp(false);
            d.setActif(true);
            d.setAuthor(currentUser);
            d = (YvsDictionnaire) dao.save1(d);
            pays.add(d);
            pay = new Dictionnaire(d.getId(), d.getLibele());
            newPays = null;
            closeDialog("dlgAddPays");
            update("txt_select_payForm");
        }
    }

    public void createVille() {
        if ((newVille != null) ? !newVille.trim().equals("") : false) {
            if (pay.getId() > 0) {
                YvsDictionnaire d = new YvsDictionnaire();
                d.setTitre(Constantes.T_VILLES);
                d.setLibele(newVille);
                d.setParent(new YvsDictionnaire(pay.getId()));
                d.setSociete(currentAgence.getSociete());
                d.setSupp(false);
                d.setActif(true);
                d.setAuthor(currentUser);
                d = (YvsDictionnaire) dao.save1(d);
                Dictionnaire dd = new Dictionnaire(d.getId(), newVille);
                dd.setLibelle(newVille);
                villes.add(d);
                formation.setLieuParDefaut(dd);
                formEMps.getVille().setId(dd.getId());
                newVille = null;
                closeDialog("dlgAddVille");
                update("select-ville-form");
                update("select-ville1-form");
            } else {
                getErrorMessage("Vous devez indiquer le pays !");
            }
        }
    }

    private FormationEmps buildFormationToFormationEmps(Formation f) {
        FormationEmps fe = new FormationEmps();
        fe.setDiplomee((f.getDiplome().getId() != 0 && f.getDiplome().getId() != -1));
        fe.setVille(f.getLieuParDefaut());
        fe.setDateDebut(f.getDateDebut());
        fe.setDateFin(f.getDateFin());
        fe.setDateFormation(new Date());
        fe.setId(f.getId());
        return fe;
    }

    private YvsGrhFormationEmps buildFormationEmps(Formation f) {
        YvsGrhFormationEmps fe = new YvsGrhFormationEmps();
        fe.setObtentionDiplome((f.getDiplome().getId() != 0 && f.getDiplome().getId() != -1));
        fe.setActif(true);
        fe.setLieu(new YvsDictionnaire(f.getLieuParDefaut().getId()));
        fe.setDateDebut(f.getDateDebut());
        fe.setDateFin(f.getDateFin());
        fe.setDateFormation(new Date());
        fe.setFormation(new YvsGrhFormation(formation.getId()));
        fe.setId(f.getId());
        fe.setSupp(false);
        return fe;
    }

    private YvsGrhFormationEmps buildFormationEmps(FormationEmps f) {
        YvsGrhFormationEmps fe = new YvsGrhFormationEmps();
        fe.setObtentionDiplome((formation.getDiplome().getId() != 0 && formation.getDiplome().getId() != -1));
        fe.setActif(true);
        fe.setLieu(new YvsDictionnaire(formation.getLieuParDefaut().getId()));
        fe.setDateDebut(f.getDateDebut());
        fe.setDateFin(f.getDateFin());
        fe.setDateFormation(new Date());
        fe.setFormation(new YvsGrhFormation(formation.getId()));
        fe.setId(f.getId());
        fe.setValider(f.isValider());
        fe.setEmploye(new YvsGrhEmployes(f.getEmploye().getId()));
        fe.setSupp(false);
        return fe;
    }

    private YvsGrhQualificationFormation buildFormationQualif(YvsGrhQualifications q) {
        YvsGrhQualificationFormation qf = new YvsGrhQualificationFormation();
        qf.setFormation(buildFormation(formation));
        qf.setQualification(q);
        qf.setActif(true);
        return qf;
    }

    private YvsCoutsFormation buildCoutFormation(CoutFormation c) {
        YvsCoutsFormation cf = new YvsCoutsFormation();
        cf.setMontant(c.getMontant());
        cf.setYvsFormationEmps(buildFormationEmps(c.getFormationEmp()));
        cf.setYvsGrhTypeCout(buildTypeCout(c.getTypeCout()));
        cf.setYvsCoutsFormationPK(buildCoutFormationPK(c.getFormationEmp(), c.getTypeCout()));
        return cf;
    }

    private YvsCoutsFormationPK buildCoutFormationPK(FormationEmps f, TypeCout t) {
        YvsCoutsFormationPK cf = new YvsCoutsFormationPK();
        cf.setFormation((int) f.getId());
        cf.setTypeCout((int) t.getId());
        return cf;
    }

    private YvsGrhTypeCout buildTypeCout(TypeCout c) {
        YvsGrhTypeCout t = new YvsGrhTypeCout();
        t.setId(c.getId());
        t.setLibelle(c.getLibelle());
        t.setSociete(currentAgence.getSociete());
        return t;
    }

    public boolean addFormationEmploye(SelectEvent ev) {
        if (formation.getId() > 0) {
            YvsGrhEmployes emp = (YvsGrhEmployes) ev.getObject();
            choixEmploye(emp);
        } else {
            getErrorMessage("Veuillez indiquer une formation !");
            return false;
        }
        return true;
    }

    public void choixEmploye(YvsGrhEmployes emp) {
        if (findEtapeValide(formation.getEtapesValidations())) {
            getErrorMessage("Impossible de modifier ce Ordre ", " La chaine de validation est déjà active !");
            return;
        }
        if (verifEmps(formation.getEmployes(), emp)) {
            FormationEmps f = buildFormationToFormationEmps(formation);
            f.setEmploye(UtilGrh.buildBeanSimplePartialEmploye(emp));
            YvsGrhFormationEmps fe = buildFormationEmps(f);
            fe.setFormation(entityFormations);
            fe.setLieu(null);
            fe.setAuthor(currentUser);
            fe.setId(null);
            fe = (YvsGrhFormationEmps) dao.save1(fe);
            f.setId(fe.getId());
            formation.getEmployes().add(0, f);
            update("tabForm");
        } else {
            getMessage("Cet employé a déjà été assigné à cette formation", FacesMessage.SEVERITY_ERROR);
        }
    }

    private boolean verifEmps(List<FormationEmps> form, YvsGrhEmployes e) {
        for (FormationEmps f : form) {
            if (f.getEmploye().getId() == e.getId()) {
                return false;
            }
        }
        return true;
    }

    public void addQualif(SelectEvent ev) {
        if (findEtapeValide(formation.getEtapesValidations())) {
            getErrorMessage("Impossible de modifier ce Ordre ", " La chaine de validation est déjà active !");
            return;
        }
        if (formation.getId() > 0) {
            YvsGrhQualifications q = (YvsGrhQualifications) ev.getObject();
            int idx = formation.getQualifications().indexOf(new DomainesQualifications(q.getDomaine().getId()));
            if (idx > 0) {
                if (containsQualif(formation.getQualifications().get(idx), new Qualification(q.getId()))) {
                    getErrorMessage("Cette qualification à déjà été ajouter à la formation !");
                    return;
                }
            }
            YvsGrhQualificationFormation qf = buildFormationQualif(q);
            qf.setAuthor(currentUser);
            qf.setId(null);
            qf = (YvsGrhQualificationFormation) dao.save1(qf);
            DomainesQualifications dom = new DomainesQualifications(qf.getQualification().getDomaine().getId(), qf.getQualification().getDomaine().getTitreDomaine());
            Qualification qual = UtilGrh.buildBeanQualification(q);
            qual.setIdLiaison(qf.getId());
            if (formation.getQualifications().contains(dom)) {
                formation.getQualifications().get(formation.getQualifications().indexOf(dom)).getQualification().add(qual);
            } else {
                dom.getQualification().add(qual);
                formation.getQualifications().add(0, dom);
            }
            update("tabview-formation:tablQualif");
        } else {
            getErrorMessage("Aucune formation n'a été selectionné !'");
        }
    }

    private boolean containsQualif(DomainesQualifications d, Qualification q) {
        for (Qualification ql : d.getQualification()) {
            if (ql.equals(q)) {
                return true;
            }
        }
        return false;
    }

    public void choixDomaineQ(ValueChangeEvent ev) {
        if (ev != null) {
            if (ev.getNewValue() != null) {
                long id = (long) ev.getNewValue();
                if (id > 0) {
                    //récupère les domaines
                    ManagedPosteDeTravail service = (ManagedPosteDeTravail) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("managedPosteDeTravail");
                    if (service != null) {
                        domainesQualification = service.getDomaines();
                    }
                    setSelectDomaineQ(domainesQualification.get(domainesQualification.indexOf(new YvsGrhDomainesQualifications(id))));
                }
            }
        }
    }

    public void delteQualif(YvsGrhQualifications q) {
        try {
            q.setAuthor(currentUser);
            dao.delete(q);
            domainesQualification.get(domainesQualification.indexOf(q.getDomaine())).getQualifications().remove(q);
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet élément");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public boolean saveNewCout() {
        if (cout.getTypeCout().getId() != 0 && cout.getMontant() > 0) {
            cout.getTypeCout().setLibelle(listTypeCout.get(listTypeCout.indexOf(cout.getTypeCout())).getLibelle());
//            for (FormationEmps f : selectionFormEmps) {
//                cout.setFormationEmp(f);
//                YvsCoutsFormation cf = buildCoutFormation(cout);
//                dao.update(cf);
//            }
            loadFormationOneEmploye();
            resetFiche(cout);
            cout.setTypeCout(new TypeCout());
            cout.setFormationEmp(new FormationEmps());
            update("coutForm2");
            update("coutTab2");
        }
        return true;
    }

    public void modifCout(SelectEvent ev) {
        CoutFormation c = (CoutFormation) ev.getObject();
        cloneObject(cout, c);
        update("coutForm2");
        activBtnDeleteCout = true;
        update("coutTab2");
    }

    private boolean containsCout(List<CoutFormation> l, TypeCout type) {
        for (CoutFormation c : l) {
            System.err.println("c.getTypeCout().getId() : " + c.getTypeCout().getId() + "----type.getId() : " + type.getId());
            if (c.getTypeCout().getId() == type.getId()) {
                return true;
            }
        }
        return false;
    }

    public void updateEmpsForm() {
        if (formation.getId() != 0 && formEMps.getId() != 0) {
//            if (!selectionFormEmps.isEmpty()) {
//                for (FormationEmps e : selectionFormEmps) {
//                    YvsGrhFormationEmps fe = buildFormationEmps(formEMps);
//                    fe.setId(e.getId());
//                    dao.update(fe);
//                    e.setDateDebut(formEMps.getDateDebut());
//                    e.setDateFin(formEMps.getDateFin());
//                    formation.getEmployes().set(selectionFormEmps.indexOf(e), e);
//                }
//                update("tabview-formation:tab-emp-form");
//            }
            closeDialog("dlgUpdateEF");
        }
    }

    public void openFEToUpdate() {
        openDialog("dlgUpdateEF");
        update("grid-update-FE");
    }

    public void openCouts() {
        openDialog("dlgCoutFormation");
        update("coutTab2");
    }

    public void deleteEmpsFormation(FormationEmps e) {
        try {
            YvsGrhFormationEmps fe = new YvsGrhFormationEmps(e.getId());
            fe.setAuthor(currentUser);
            dao.delete(fe);
            formation.getEmployes().remove(e);
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteBean() {
        try {
            System.err.println("chaineSelectFormation = " + chaineSelectFormation);
            if ((chaineSelectFormation != null) ? chaineSelectFormation.length() > 0 : false) {
                List<Long> l = decomposeIdSelection(chaineSelectFormation);
                List<YvsGrhFormation> list = new ArrayList<>();
                YvsGrhFormation bean;
                for (Long ids : l) {
                    bean = listFormations.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);

                }
                listFormations.removeAll(list);
                succes();
                resetFiche();
                chaineSelectFormation = "";
                update("panel-formation");

            }
        } catch (Exception ex) {
            chaineSelectFormation = "";
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    public void deleteBean_() {
        try {
            System.err.println("selectionFormation = " + selectionFormation.size());
            if (selectionFormation != null ? !selectionFormation.isEmpty() : false) {
                for (YvsGrhFormation f : selectionFormation) {
                    f.setAuthor(currentUser);
                    f.setDateUpdate(new Date());
                    dao.delete(f);
                }
                listFormations.removeAll(selectionFormation);
                succes();
                selectionFormation.clear();
                update("panel-formation");
            }
        } catch (Exception ex) {
            selectionFormation.clear();
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    public void deleteQualifFormation(Qualification qual) {
        try {
            YvsGrhQualificationFormation qf = new YvsGrhQualificationFormation();
            qf.setId(qual.getIdLiaison());
            qf.setAuthor(currentUser);
            dao.delete(qf);
            formation.getQualifications().get(formation.getQualifications().indexOf(qual.getDomaine())).getQualification().remove(qual);
            if (formation.getQualifications().get(formation.getQualifications().indexOf(qual.getDomaine())).getQualification().isEmpty()) {
                formation.getQualifications().remove(qual.getDomaine());
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteCout() {
        if (!listcout.isEmpty()) {
            for (CoutFormation c : listcout) {
                try {
                    dao.delete(new YvsCoutsFormation((int) formEMps.getId(), (int) c.getTypeCout().getId()));
                    formEMps.getCoutFormation().remove(c);
                    activBtnDeleteCout = false;
                } catch (Exception ex) {
                    getMessage("Erreur de suppression !", FacesMessage.SEVERITY_ERROR);
                }
                update("coutTab2");
            }
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        execute("collapseForm('formation')");
        entityFormations = (YvsGrhFormation) ev.getObject();
        entityFormations.setEtapesValidations(dao.loadNameQueries("YvsWorkflowValidFormation.findByDocument", new String[]{"formation"}, new Object[]{entityFormations}));
        cloneObject(formation, UtilGrh.buildBeanFormation(entityFormations));
        formation.setEtapesValidations(ordonneEtapes(entityFormations.getEtapesValidations()));
        updateformation = true;
        updateformationEmps = false;
        chaineSelectFormation = listFormations.indexOf(entityFormations) + "";

    }
//
//    public void selectFormationEmps() {
//        if (selectionFormEmps.size() > 0) {
//            displayBtnOption = true;
//            activBtnSupprimer = true;
//        } else {
//            displayBtnOption = false;
//            activBtnSupprimer = false;
//        }
//    }

    public void loadOnViewfEmps(SelectEvent ev) {
        FormationEmps f = (FormationEmps) ev.getObject();
//        selectFormationEmps();
        updateformation = false;
        updateformationEmps = true;
        update("bloc-en-tete-formation");
    }

    public void choixEmployeForm() {
//        displayBtnDelFormEmps = !selectionFormEmps.isEmpty();
//        if (!selectionFormEmps.isEmpty()) {
//            cloneObject(formEMps, selectionFormEmps.get(selectionFormEmps.size() - 1));
//        }
//        update("tabview-formation:grid-emp-form");
////        loadFormationOneEmploye();
    }

//    public void selectFormation(Formation f) {
//        
////        entityFormations = buildFormation(formation);
////        if ((formation.getFormateur() != null) ? formation.getFormateur().getId() != 0 : false) {
////            cloneObject(formateur, formation.getFormateur());
////            entityFormateur = buildFormateur(formateur);
////        }
//    }
//    public void openFormToUpdate() {
//        if (updateformation) {
//            setNameBtnVue("Vue");
//            activBtnSupprimer = false;
//            updateformation = true;
//            update("bloc-formation");
//            update("bloc-en-tete-formation");
//        } else {
//            setFormation(buildFormation(formEMps));
//            openDialog("dlgFormationEmps");
//        }
//    }
    private YvsGrhFormation buildFormation(Formation f) {
        YvsGrhFormation fe = new YvsGrhFormation();
        fe.setLibelle(f.getTitreFormation());
        fe.setActif(true);
        if (f.getFormateur().getId() > 0) {
            fe.setFormateur(listFormateur.get(listFormateur.indexOf(new YvsGrhFormateur(f.getFormateur().getId()))));
        }
        fe.setCoutFormation(f.getCout());
        if (f.getLieuParDefaut().getId() > 0) {
            fe.setLieuDefaut(new YvsDictionnaire(f.getLieuParDefaut().getId()));
        }
        fe.setDiplome((f.getDiplome().getId() != 0 && f.getDiplome().getId() != -1) ? new YvsDiplomes(f.getDiplome().getId()) : null);
        fe.setId(f.getId());
        fe.setDateDebut(f.getDateDebut());
        fe.setDateFin(f.getDateFin());
        fe.setReference(f.getReference());
        fe.setSociete(currentUser.getAgence().getSociete());
        fe.setDescription(f.getDescription());
        fe.setStatutFormation(f.getStatutFormation());
        return fe;
    }

//    private Formation buildFormation(FormationEmps f) {
//        Formation fe = new Formation();
//        fe.setId(f.getId());
//        fe.setDateDebut(f.getDateDebut());
//        fe.setDateFin(f.getDateFin());
//        fe.setLieuParDefaut(f.getVille());
//        return fe;
//    }
//    public void chooseFormateur(ValueChangeEvent ev) {
//        if (ev.getNewValue() != null) {
//            int id = (int) ev.getNewValue();
//            if (id > 0) {
//                if (id == Long.MAX_VALUE) {
//                    openDialog("dlgAddFormateur");
//                } else {
//                    formateur.setId((int) id);
//                    formateur.setNom(listFormateur.get(listFormateur.indexOf(new YvsGrhFormateur(formateur.getId()))).getNom());
//                    entityFormateur = buildFormateur(formateur);
//                }
//            } else if (id == -1) {
//                openDialog("dlgAddFormateur");
//            }
//        }
//    }
    @Override
    public boolean saveNew() {
        if (controleFiche(formation)) {
            if (!updateformation) {
                entityFormations = buildFormation(formation);
                entityFormations.setAuthor(currentUser);
                entityFormations.setStatutFormation('E');
                entityFormations.setId(null);
                entityFormations = (YvsGrhFormation) dao.save1(entityFormations);
                formation.setId(entityFormations.getId());
                formation.setEtapesValidations(saveEtapesValidation(entityFormations));
                listFormations.add(entityFormations);
            } else {
                if (findEtapeValide(formation.getEtapesValidations())) {
                    getErrorMessage("Impossible de modifier ce Ordre ", " La chaine de validation est déjà active !");
                    return false;
                }
                entityFormations = buildFormation(formation);
                entityFormations.setAuthor(currentUser);
                entityFormations = (YvsGrhFormation) dao.update(entityFormations);
                listFormations.set(listFormations.indexOf(entityFormations), entityFormations);
            }
            updateformation = true;
            succes();
            actionOpenOrResetAfter(this);
        }
        return true;
    }

    private boolean findEtapeValide(List<YvsWorkflowValidFormation> l) {
        for (YvsWorkflowValidFormation vf : l) {
            if (vf.getEtapeValid()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateBean() {

    }

    public List<String> findDiplome(String str) {
        List<String> listeDiplome = new ArrayList<>();
        for (YvsDiplomes di : diplomes) {
            if (str.startsWith(di.getCodeInterne())) {
                listeDiplome.add(str);
            }
        }
        return listeDiplome;
    }

    public void activationBtnSuppQualif(SelectEvent ev) {
        QualificationFormation bean = (QualificationFormation) ev.getObject();
        cloneObject(qualif, bean.getQualif());
        update("form_qualif_formation");
    }

    public void desactivationBtnSuppQualif() {
        resetFiche(qualif);
        update("form_qualif_formation");
    }

    public void openFormEmploye() {
        System.err.println("Open-----formation.getLieuParDefaut().getId() : " + formation.getLieuParDefaut().getId());
        openDialog("dlgEmploye");
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void exporteBulletin(FormationEmps ev) {
        if (ev != null) {
            Map<String, Object> param = new HashMap<>();
            param.put("ID_FORMATION", formation.getId());
            param.put("ID_EMPLOYE", ev.getEmploye().getId());
            param.put("NOM_USER", currentUser.getUsers().getNomUsers());
            param.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report"));
            param.put("PATH", returnLogo());
            executeReport("Ordre_formation", param, ev.getEmploye().getMatricule());
        }
    }

    public void chooseTypeCout(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                if (id == Long.MAX_VALUE) {
                    ManagedTypeCout w = (ManagedTypeCout) giveManagedBean(ManagedTypeCout.class);
                    if (w != null) {
                        w.setTypeCout(new TypeCout(Constantes.COUT_FORMATION));
                    }
                    openDialog("dlgAddTypeCout");
                } else {
                    cout.getTypeCout().setId(id);
                    cout.getTypeCout().setLibelle(listTypeCout.get(listTypeCout.indexOf(cout.getTypeCout())).getLibelle());
                }
            }
        }
    }

//    public void chageEtatFormation(char etat) {
//        if (formation.getId() > 0) {
//            //si la date de début de la formation est supérieure à la date en cours, lever une exception
//            if (etat == 'E') {
//                if (formation.getDateFin().before(new Date())) {
//                    getWarningMessage("La date de fin de cette formation indique qu'elle est déjà passé !");
//                    return;
//                }
//            }
//            entityFormations = buildFormation(formation);
//            entityFormations.setAuthor(currentUser);
//            entityFormations.setStatutFormation(etat);
//            formation.setStatutFormation(etat);
//            dao.update(entityFormations);
//            getInfoMessage("Etat changé avec succès !");
//        } else {
//            getErrorMessage("Attention!!! vous devez selectionner une formation!");
//        }
//    }
    public void validEtapeOrdreFormation(YvsWorkflowValidFormation etape) {
        //vérifier que la personne qui valide l'étape a le droit 
        if (!asDroitValideEtape(etape.getEtape())) {
            openDialog("dlgNotAcces");
        } else {
            etape.setAuthor(currentUser);
            etape.setEtapeValid(true);
            etape.setEtapeActive(false);
            int idx = formation.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                if (formation.getEtapesValidations().size() > (idx + 1)) {
                    formation.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                }
                dao.update(etape);
                getInfoMessage("Validation effectué avec succès !");
            } else {
                getErrorMessage("Impossible de continuer !");
            }
            //cas de la validation de la dernière étapes
            if (etape.getEtapeSuivante() == null) {
                entityFormations.setStatutFormation('V');
                dao.update(entityFormations);
            }
        }
    }

    public void cancelOrdreMission() {
        //vérifie le droit
        //annule toute les validation acquise
        int i = 0;
        boolean update = false;
        for (YvsWorkflowValidFormation vm : formation.getEtapesValidations()) {
            if (!vm.getEtapeValid()) {
                update = true;
            }
        }
        if (update) {
            for (YvsWorkflowValidFormation vm : formation.getEtapesValidations()) {
                vm.setEtapeActive(false);
                if (i == 0) {
                    vm.setEtapeActive(true);
                }
                vm.setAuthor(currentUser);
                vm.setEtapeValid(false);
                dao.update(vm);
                i++;
            }
        } else {
            getErrorMessage("Vous ne pouvez annuler cet ordre de mission", "Il a déjà parcouru tout le cycle de validation");
            return;
        }
        //désactive la mission
        entityFormations.setAuthor(currentUser);
        entityFormations.setActif(false);
        dao.update(entityFormations);
        listFormations.set(listFormations.indexOf(entityFormations), entityFormations);
    }

    /**
     * *WORKFLOW DES Ordres de formations*****************
     */
    /**
     * ******************************************
     */
    private List<YvsWorkflowValidFormation> saveEtapesValidation(YvsGrhFormation f) {
        //charge les étape de vailidation
        List<YvsWorkflowValidFormation> re = new ArrayList<>();
        champ = new String[]{"titre", "societe"};
        val = new Object[]{Constantes.DOCUMENT_FORMATION, currentAgence.getSociete()};
        List<YvsWorkflowEtapeValidation> model = dao.loadNameQueries("YvsWorkflowEtapeValidation.findByModelActif", champ, val);
        if (!model.isEmpty()) {
            YvsWorkflowValidFormation vf;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    vf = new YvsWorkflowValidFormation();
                    vf.setAuthor(currentUser);
                    vf.setEtape(et);
                    vf.setEtapeValid(false);
                    vf.setFormation(f);
                    vf.setOrdreEtape(et.getOrdreEtape());
                    vf = (YvsWorkflowValidFormation) dao.save1(vf);
                    re.add(vf);
                }
            }
        }

        return ordonneEtapes(re);
    }

    private List<YvsWorkflowValidFormation> ordonneEtapes(List<YvsWorkflowValidFormation> l) {
        return YvsWorkflowValidFormation.ordonneEtapes(l);
    }

    public void validEtapeOrdreMission(YvsWorkflowValidFormation etape) {
        //vérifier que la personne qui valide l'étape a le droit 
        if (!asDroitValideEtape(etape.getEtape())) {
            openDialog("dlgNotAcces");
        } else {
            etape.setAuthor(currentUser);
            etape.setEtapeValid(true);
            etape.setEtapeActive(false);
            int idx = formation.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                if (formation.getEtapesValidations().size() > (idx + 1)) {
                    formation.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                }
                dao.update(etape);
                getInfoMessage("Validation effectué avec succès !");
            } else {
                getErrorMessage("Impossible de continuer !");
            }
            //cas de la validation de la dernière étapes
            if (etape.getEtapeSuivante() == null) {
                entityFormations.setStatutFormation('V');
                dao.update(entityFormations);
            }
        }
    }

    public void cancelOrdreFormation() {
        //vérifie le droit
        //annule toute les validation acquise
        int i = 0;
        boolean update = false;
        for (YvsWorkflowValidFormation vf : formation.getEtapesValidations()) {
            if (!vf.getEtapeValid()) {
                update = true;
            }
        }
        if (update) {
            for (YvsWorkflowValidFormation vf : formation.getEtapesValidations()) {
                vf.setEtapeActive(false);
                if (i == 0) {
                    vf.setEtapeActive(true);
                }
                vf.setAuthor(currentUser);
                vf.setEtapeValid(false);
                dao.update(vf);
                i++;
            }
        } else {
            getErrorMessage("Vous ne pouvez annuler cet ordre de mission", "Il a déjà parcouru tout le cycle de validation");
            return;
        }
        //désactive la mission
        entityFormations.setAuthor(currentUser);
        entityFormations.setActif(false);
        dao.update(entityFormations);
        listFormations.set(listFormations.indexOf(entityFormations), entityFormations);
    }
}
