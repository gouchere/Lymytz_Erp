/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean.mission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.ManagedCentreAnalytique;
import yvs.base.compta.ModeDeReglement;
import yvs.base.compta.UtilCompta;
import yvs.base.produits.Articles;
import yvs.comptabilite.analytique.CentreMission;
import yvs.comptabilite.caisse.Caisses;
import yvs.comptabilite.caisse.ManagedCaisses;
import yvs.comptabilite.caisse.ManagedReglementMission;
import yvs.comptabilite.tresorerie.ManagedBonProvisoire;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseDepartement;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaCaissePieceMission;
import yvs.entity.compta.YvsComptaJustifBonMission;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.compta.analytique.YvsComptaCentreMission;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.grh.activite.YvsGrhDetailGrilleFraiMission;
import yvs.entity.grh.activite.YvsGrhFraisMission;
import yvs.entity.grh.activite.YvsGrhGrilleMission;
import yvs.entity.grh.activite.YvsGrhMissionRessource;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.grh.activite.YvsGrhObjetsMission;
import yvs.entity.grh.activite.YvsGrhObjetsMissionAnalytique;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.grh.param.YvsJoursFeries;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidBonProvisoire;
import yvs.entity.param.workflow.YvsWorkflowValidMission;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.proj.YvsProjDepartement;
import yvs.entity.proj.projet.YvsProjProjet;
import yvs.entity.proj.projet.YvsProjProjetMissions;
import yvs.entity.proj.projet.YvsProjProjetService;
import yvs.entity.users.YvsUsersAgence;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.bean.Employe;
import yvs.grh.bean.ManagedTypeCout;
import yvs.grh.bean.MissionRessource;
import yvs.grh.bean.TypeCout;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.dico.ManagedDico;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Utilitaire;
import yvs.util.enume.Nombre;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class ManagedMission extends Managed<Mission, YvsGrhMissions> implements Serializable {

    @ManagedProperty(value = "#{mission}")
    private Mission mission;
    private Date currentDate = new Date();
    private MissionRessource ressource = new MissionRessource();
    private Articles article = new Articles();
    private DetailFraisMission cout = new DetailFraisMission();
    private GrilleFraisMission grilleM = new GrilleFraisMission();
    private long idParentGrilleFM;
    public List<YvsGrhMissions> listMission, selectionMissions;
    public YvsGrhMissions selectMission = new YvsGrhMissions();
    private List<YvsBaseArticles> listArticles;
    private List<YvsGrhTypeCout> listTypeCout;
    private long projet;
//    private List<YvsGrhGrilleMission> grillesMissions, grillesMissionActif;
//    private boolean updateMission = false;
    private boolean deleteMission = false;
    private boolean activVue = true, activVueListeGroupEmps = false, activVueListeGroup = false, activVueAll = false;
    private boolean activBtnSuppEmps = false, activBtnSuppRess = false;
    private boolean selection = false, toValideLoad = true;
    private String objet, nameListeEncour = "LISTE DES MISSIONS EN COURS";
    private Date dateSearch;
    private TypeCout typeCout = new TypeCout();
    private String chaineSelectMission, chaineSelectEmployeMission, chaineSelectRessMission;
    private int currentNombre = 0;
    private String lieuEscale, egaliteStatut = "=";
    private List<String> lieuxEscale;

    private CentreMission analytique = new CentreMission();
    private YvsComptaCentreMission selectAnalytique = new YvsComptaCentreMission();

    private String motifEtape;
    YvsWorkflowValidMission etape;
    private boolean lastEtape;

    private List<YvsGrhMissions> missionsClasse;

    private YvsComptaBonProvisoire piece = new YvsComptaBonProvisoire();

    private boolean initForm = true;

    private String commentaireLineFrais;

    private YvsGrhFraisMission selectedLineFrais;

    public ManagedMission() {
        lieuxEscale = new ArrayList<>();
        listMission = new ArrayList<>();
        listArticles = new ArrayList<>();
        listTypeCout = new ArrayList<>();
        selectionMissions = new ArrayList<>();
        missionsClasse = new ArrayList<>();
    }

    public long getProjet() {
        return projet;
    }

    public void setProjet(long projet) {
        this.projet = projet;
    }

    public CentreMission getAnalytique() {
        return analytique;
    }

    public void setAnalytique(CentreMission analytique) {
        this.analytique = analytique;
    }

    public YvsComptaCentreMission getSelectAnalytique() {
        return selectAnalytique;
    }

    public void setSelectAnalytique(YvsComptaCentreMission selectAnalytique) {
        this.selectAnalytique = selectAnalytique;
    }

    public YvsWorkflowValidMission getEtape() {
        return etape;
    }

    public void setEtape(YvsWorkflowValidMission etape) {
        this.etape = etape;
    }

    public boolean isForceGeneretedPc() {
        return forceGeneretedPc;
    }

    public void setForceGeneretedPc(boolean forceGeneretedPc) {
        this.forceGeneretedPc = forceGeneretedPc;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getMotifEtape() {
        return motifEtape;
    }

    public void setMotifEtape(String motifEtape) {
        this.motifEtape = motifEtape;
    }

    public boolean isLastEtape() {
        return lastEtape;
    }

    public void setLastEtape(boolean lastEtape) {
        this.lastEtape = lastEtape;
    }

    public String getEgaliteStatut() {
        return egaliteStatut;
    }

    public void setEgaliteStatut(String egaliteStatut) {
        this.egaliteStatut = egaliteStatut;
    }

    public boolean isToValideLoad() {
        return toValideLoad;
    }

    public void setToValideLoad(boolean toValideLoad) {
        this.toValideLoad = toValideLoad;
    }

    public YvsComptaBonProvisoire getPiece() {
        return piece;
    }

    public void setPiece(YvsComptaBonProvisoire piece) {
        this.piece = piece;
    }

    public YvsGrhMissions getSelectMission() {
        return selectMission;
    }

    public void setSelectMission(YvsGrhMissions selectMission) {
        this.selectMission = selectMission;
    }

    public boolean isInitForm() {
        return initForm;
    }

    public void setInitForm(boolean initForm) {
        this.initForm = initForm;
    }

    public int getOffsetEmps() {
        return offsetEmps;
    }

    public void setOffsetEmps(int offsetEmps) {
        this.offsetEmps = offsetEmps;
    }

    public boolean isUpdateTypeCout() {
        return updateTypeCout;
    }

    public void setUpdateTypeCout(boolean updateTypeCout) {
        this.updateTypeCout = updateTypeCout;
    }

    public boolean isUpdateGrille() {
        return updateGrille;
    }

    public void setUpdateGrille(boolean updateGrille) {
        this.updateGrille = updateGrille;
    }

    public long getIdTemp() {
        return idTemp;
    }

    public void setIdTemp(long idTemp) {
        this.idTemp = idTemp;
    }

    public YvsWorkflowValidMission getCurrentEtape() {
        return currentEtape;
    }

    public void setCurrentEtape(YvsWorkflowValidMission currentEtape) {
        this.currentEtape = currentEtape;
    }

    public YvsGrhFraisMission getSelectedLineFrais() {
        return selectedLineFrais;
    }

    public void setSelectedLineFrais(YvsGrhFraisMission selectedLineFrais) {
        this.selectedLineFrais = selectedLineFrais;
    }

    public List<String> getLieuxEscale() {
        return lieuxEscale;
    }

    public void setLieuxEscale(List<String> lieuxEscale) {
        this.lieuxEscale = lieuxEscale;
    }

    public int getCurrentNombre() {
        return currentNombre;
    }

    public void setCurrentNombre(int currentNombre) {
        this.currentNombre = currentNombre;
    }

    public long getIdParentGrilleFM() {
        return idParentGrilleFM;
    }

    public void setIdParentGrilleFM(long idParentGrilleFM) {
        this.idParentGrilleFM = idParentGrilleFM;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getCommentaireLineFrais() {
        return commentaireLineFrais;
    }

    public void setCommentaireLineFrais(String commentaireLineFrais) {
        this.commentaireLineFrais = commentaireLineFrais;
    }

    public String getChaineSelectRessMission() {
        return chaineSelectRessMission;
    }

    public void setChaineSelectRessMission(String chaineSelectRessMission) {
        this.chaineSelectRessMission = chaineSelectRessMission;
    }

    public String getChaineSelectEmployeMission() {
        return chaineSelectEmployeMission;
    }

    public void setChaineSelectEmployeMission(String chaineSelectEmployeMission) {
        this.chaineSelectEmployeMission = chaineSelectEmployeMission;
    }

    public String getChaineSelectMission() {
        return chaineSelectMission;
    }

    public void setChaineSelectMission(String chaineSelectMission) {
        this.chaineSelectMission = chaineSelectMission;
    }

    public TypeCout getTypeCout() {
        return typeCout;
    }

    public void setTypeCout(TypeCout typeCout) {
        this.typeCout = typeCout;
    }

    public String getNameListeEncour() {
        return nameListeEncour;
    }

    public void setNameListeEncour(String nameListeEncour) {
        this.nameListeEncour = nameListeEncour;
    }

    public boolean isActivVueAll() {
        return activVueAll;
    }

    public void setActivVueAll(boolean activVueAll) {
        this.activVueAll = activVueAll;
    }

    public List<YvsGrhTypeCout> getListTypeCout() {
        return listTypeCout;
    }

    public List<YvsGrhMissions> getSelectionMissions() {
        return selectionMissions;
    }

    public void setSelectionMissions(List<YvsGrhMissions> selectionMissions) {
        this.selectionMissions = selectionMissions;
    }

    public void setListTypeCout(List<YvsGrhTypeCout> listTypeCout) {
        this.listTypeCout = listTypeCout;
    }

    public DetailFraisMission getCout() {
        return cout;
    }

    public void setCout(DetailFraisMission cout) {
        this.cout = cout;
    }

    public GrilleFraisMission getGrilleM() {
        return grilleM;
    }

    public void setGrilleM(GrilleFraisMission grilleM) {
        this.grilleM = grilleM;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getLieuEscale() {
        return lieuEscale;
    }

    public void setLieuEscale(String lieuEscale) {
        this.lieuEscale = lieuEscale;
    }

    public Date getDateSearch() {
        return dateSearch;
    }

    public void setDateSearch(Date dateSearch) {
        this.dateSearch = dateSearch;
    }

    public boolean isActivBtnSuppEmps() {
        return activBtnSuppEmps;
    }

    public void setActivBtnSuppEmps(boolean activBtnSuppEmps) {
        this.activBtnSuppEmps = activBtnSuppEmps;
    }

    public boolean isActivBtnSuppRess() {
        return activBtnSuppRess;
    }

    public void setActivBtnSuppRess(boolean activBtnSuppRess) {
        this.activBtnSuppRess = activBtnSuppRess;
    }

    public boolean isActivVueListeGroupEmps() {
        return activVueListeGroupEmps;
    }

    public void setActivVueListeGroupEmps(boolean activVueListeGroupEmps) {
        this.activVueListeGroupEmps = activVueListeGroupEmps;
    }

    public boolean isActivVueListeGroup() {
        return activVueListeGroup;
    }

    public void setActivVueListeGroup(boolean activVueListeGroup) {
        this.activVueListeGroup = activVueListeGroup;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public MissionRessource getRessource() {
        return ressource;
    }

    public void setRessource(MissionRessource ressource) {
        this.ressource = ressource;
    }

    public List<YvsBaseArticles> getListArticles() {
        return listArticles;
    }

    public void setListArticles(List<YvsBaseArticles> listArticles) {
        this.listArticles = listArticles;
    }

    public boolean isSelection() {
        return selection;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }

    public boolean isActivVue() {
        return activVue;
    }

    public void setActivVue(boolean activVue) {
        this.activVue = activVue;
    }

    public boolean isDeleteMission() {
        return deleteMission;
    }

    public void setDeleteMission(boolean deleteMission) {
        this.deleteMission = deleteMission;
    }

    public List<YvsGrhMissions> getListMission() {
        return listMission;
    }

    public void setListMission(List<YvsGrhMissions> listMission) {
        this.listMission = listMission;
    }
//
//    public boolean isUpdateMission() {
//        return updateMission;
//    }
//
//    public void setUpdateMission(boolean updateMission) {
//        this.updateMission = updateMission;
//    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public List<YvsGrhMissions> getMissionsClasse() {
        return missionsClasse;
    }

    public void setMissionsClasse(List<YvsGrhMissions> missionsClasse) {
        this.missionsClasse = missionsClasse;
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

    private boolean vueListeDemande;

    public boolean isVueListeDemande() {
        return vueListeDemande;
    }

    public void setVueListeDemande(boolean vueListeDemande) {
        this.vueListeDemande = vueListeDemande;
    }

    public void findOneEmploye() {
        ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
        if (service != null) {
            if (mission.getEmploye().getNom() != null ? !mission.getEmploye().getNom().trim().isEmpty() : false) {
                service.addParamActif(true);
                service.findEmploye(mission.getEmploye().getNom());
                if (!service.getListEmployes().isEmpty()) {
                    if (service.getListEmployes().size() == 1) {
                        choixEmploye1(service.getListEmployes().get(0));
                    } else {
                        openDialog("dlgEmploye");
                        update("tabEmployes_mission");
                    }
                }
            }
        }
    }

    public void selectOneObjet(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            ManagedObjetMission service = (ManagedObjetMission) giveManagedBean(ManagedObjetMission.class);
            if (service != null) {
                int idx = service.getObjetsMissions().indexOf(new YvsGrhObjetsMission((int) ev.getNewValue()));
                if (idx >= 0) {
                    if (mission.getOrdre() == null) {
                        mission.setOrdre(service.getObjetsMissions().get(idx).getTitre());
                    }
                    if (mission.getObjet().getId() > 0) {
                        if ((int) ev.getNewValue() != mission.getObjet().getId() && mission.getId() > 0) {
                            //demande une confirmation de la mise à jour de l'objet de la mission
                            openDialog("dlgUpdateObjetM");
                        } else {
                            if (mission.getId() > 0) {
                                updateObjetMission(false);
                            }
                        }
                    } else {
                        if (mission.getId() > 0) {
                            updateObjetMission(false);
                        }
                    }
                    mission.setObjet(UtilGrh.buildBeanObjetMission(service.getObjetsMissions().get(idx)));
                }
            }
        }
    }

    public void updateObjetMission(boolean ok) {
        if (mission.getId() > 0) {
            YvsGrhMissions m = (YvsGrhMissions) dao.loadOneByNameQueries("YvsMissions.findById", new String[]{"id"}, new Object[]{mission.getId()});
            if (m != null) {
                m.setAuthor(currentUser);
                m.setObjetMission(UtilGrh.buildBeanObjetMission(mission.getObjet(), currentUser));
                dao.update(m);
                if (ok) {
                    succes();
                }
            }
        }
    }
//    public void findEmploye() {
//        champ = new String[]{"codeUsers", "agence"};
//        val = new Object[]{"%" + mission.getEmploye().getNom() + "%", currentUser.getAgence().getSociete()};
//        List<YvsGrhEmployes> l = dao.loadNameQueries("YvsGrhEmployes.findByCodeUsers", champ, val, 0, 10);
//        if (l.size() > 1) {
//            ManagedEmployes me = (ManagedEmployes) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("MEmps");
//            if (me != null) {
//                me.setListEmployes(l);
//                openDialog("dlgEmploye");
//                update("tabEmployes_mission");
//                update("gridEmployes_mission");
//            }
//        } else if (l.size() == 1) {
//            choixEmploye1(l.get(0));
//        }
//    }

    public void choixEmploye(SelectEvent ev) {
        if (ev != null) {
            choixEmploye1((YvsGrhEmployes) ev.getObject());
        }
    }

    public void choixEmploye1(YvsGrhEmployes ev) {
        if (ev != null) {
            mission.setEmploye(UtilGrh.buildBeanSimplePartialEmploye(ev));
            if (ev.getPosteActif() != null) {
                mission.setFraisMission(UtilGrh.buildGrilleMission(ev.getPosteActif().getFraisMission()));
            }
        }
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

    public void loadMissionStatut(String statut) {
        ParametreRequete p = new ParametreRequete("y.statutMission", "statut", null, "=", "AND");
        if (statut != null ? statut.trim().length() > 0 : false) {
            statutF = statut.charAt(0);
            p = new ParametreRequete("y.statutMission", "statut", statutF, "=", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllMission(true);
    }

//    YvsGrhMissions currentMission;
//    YvsGrhMissionRessource currentMissionRess;
    public void confirmation() {
        openDialog("dlgConfirmationDeleteMission");
    }

    public void loadMissionVue() {
        activVue = !activVue;
        if (selection) {
            deleteMission = !deleteMission;
        }
        resetFiche();

        update("bloc-en-tete-mission");
        update("tabview-mission");
    }

    public void afficheListeMissionEmps() {
        if (activVue) {
            activVueListeGroupEmps = true;
            activVueListeGroup = true;
            vueListeDemande = true;
            deleteMission = false;

            update("form-mission-00");
        }
    }

    public void afficheListeMissionRess() {
        if (activVue) {
            activVueListeGroup = true;
            activVueListeGroupEmps = false;
            vueListeDemande = true;
            deleteMission = false;

            update("form-mission-00");
        }
    }
//
//    public void afficheListeAllMission() {
//        vueListeDemande = true;
//        activVueAll = true;
//        deleteMission = false;
//        activVueListeGroup = false;
//        activVueListeGroupEmps = false;
//        setNameListeEncour("LISTE DE TOUTES LES MISSIONS");
//        loadAllMission();
//        update("liste-mission");
//        update("tab-mission");
//    }

//    public void afficheVueListMissionEncours() {
//        vueListeDemande = false;
//        activVueListeGroup = false;
//        activVueAll = false;
//        deleteMission = false;
//        activVueListeGroupEmps = false;
//        loadAllMissionEnCours();
//        setNameListeEncour("LISTE DES MISSIONS EN COURS");
//        update("liste-mission");
//        update("tab-mission");
//    }
    public void researchObjet() {
        openDialog("dlgRecherchObjet");
    }

    public void researchLieu() {
        openDialog("dlgRecherchLieu");
    }

    public void researchDate() {
        openDialog("dlgRecherchDate");
    }

    public void confirmAjout() {
        openDialog("dlgAttributionCout");
    }

    public void plusDeRessource(ValueChangeEvent ev) {
        if (ev != null) {
            if (ev.getNewValue() != null) {
                long id = (long) ev.getNewValue();
                if (id == -1) {
                    //ouvre la boîte de dialogue de choix d'un utilisateurs                
                    openDialog("dglListeRess");
                }
            }
        }
    }

    public boolean update() {
        activVue = !activVue;
        deleteMission = false;
        update("bloc-en-tete-mission");
        return true;
    }

    private YvsGrhMissions buildMission(Mission mi) {
        YvsGrhMissions entityM = new YvsGrhMissions();
        entityM.setDateDebut(mi.getDateDebut());
        entityM.setDateFin(mi.getDateFin());
        entityM.setDateMission(mi.getDateRetour());
        if ((mi.getLieu() != null) ? mi.getLieu().getId() >= 0 : false) {
            ManagedDico service = (ManagedDico) giveManagedBean("Mdico");
            if (service != null) {
                int idx = service.getListVille().indexOf(new YvsDictionnaire(mi.getLieu().getId()));
                if (idx >= 0) {
                    entityM.setLieu(service.getListVille().get(idx));
                }
            }
        }
        entityM.setStatutPaiement(mi.getStatutPaiement());
        entityM.setOrdre(mi.getOrdre());
        entityM.setEmploye(UtilGrh.buildEmployeEntity(mi.getEmploye()));
        entityM.setId(mi.getId());
        entityM.setActif(mi.isActif());
        entityM.setStatutMission(mi.getStatutMission());
        entityM.setCloturer(mi.isCloturer());
        entityM.setDateRetour(mi.getDateRetour());
        entityM.setReferenceMission(mi.getReference());
        entityM.setNumeroMission(mi.getNumeroMission());
        entityM.setTransport(mi.getTransport());
        entityM.setHeureArrive(mi.getHeureArrive());
        entityM.setHeureDepart(mi.getHeureDepart());
        entityM.setDateSave(mi.getDateSave());
        entityM.setDateUpdate(new Date());
        entityM.setDureeMission(mi.getDureeMission());
        if (mi.getObjet().getId() > 0) {
            entityM.setObjetMission(new YvsGrhObjetsMission(mi.getObjet().getId()));
            ManagedObjetMission service = (ManagedObjetMission) giveManagedBean(ManagedObjetMission.class);
            if (service != null) {
                int idx = service.getObjetsMissions().indexOf(new YvsGrhObjetsMission(mi.getObjet().getId()));
                if (idx >= 0) {
                    entityM.setObjetMission(service.getObjetsMissions().get(idx));
                }
            }
        }
        if (mi.getFraisMission().getId() > 0) {
            entityM.setFraisMission(new YvsGrhGrilleMission(mi.getFraisMission().getId()));
            ManagedFraisMission service = (ManagedFraisMission) giveManagedBean(ManagedFraisMission.class);
            if (service != null) {
                int idx = service.getGrillesMissions().indexOf(new YvsGrhGrilleMission(mi.getFraisMission().getId()));
                if (idx >= 0) {
                    entityM.setFraisMission(service.getGrillesMissions().get(idx));
                }
            }
        }
        entityM.setTotalRegle(mi.getTotalPaye());
        entityM.setTotalPiece(mi.getTotalPiece());
        entityM.setTotalReste(mi.getRestPlanifier());
        entityM.setTotalFraisMission(mi.getTotalFraisMission());
        entityM.setFraisMissions(mi.getFraisMissions());
        entityM.setRoleMission(mi.getRole());
        entityM.setEtapeValide(mi.getEtapeValide());
        entityM.setEtapeTotal(mi.getEtapeTotal());
        return entityM;
    }

    private String fabriqueReference(YvsDictionnaire lieu) {
        if (mission.getEmploye().getPosteDeTravail() != null && lieu != null && mission.getDateDebut() != null) {
            //find last mission
            Long n = (Long) dao.loadObjectByNameQueries("YvsGrhMissions.findCountInExercice", new String[]{"societe", "debut", "fin"}, new Object[]{currentUser.getAgence().getSociete(), giveExerciceActif(new Date()).getDateDebut(), giveExerciceActif(new Date()).getDateFin()});
            if (n == null) {
                n = (long) 0;
            }
            if (mission.getEmploye().getMatricule() != null) {
                try {
                    if (mission.getEmploye().getPosteDeTravail().getDepartement().getAbreviation() != null) {
                        StringBuilder sb = new StringBuilder((mission.getEmploye().getPosteDeTravail().getDepartement().getAbreviation() != null) ? mission.getEmploye().getPosteDeTravail().getDepartement().getAbreviation() : mission.getEmploye().getPosteDeTravail().getDepartement().getIntitule());
                        sb.append("_").append((lieu.getAbreviation() != null) ? lieu.getAbreviation() : lieu.getLibele()).append("/").append(buildNumMission(n + 1));
                        return sb.toString();
                    }
                } catch (NullPointerException ex) {
                    getErrorMessage("Erreur de génération de la référence !");
                    getException("ManagedMission (fabriqueReference)", ex);
                    return null;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    private String buildNumMission(long nbr) {
        if (nbr < 10) {
            return "00000" + nbr;
        } else if (nbr < 100) {
            return "0000" + nbr;
        } else if (nbr < 1000) {
            return "000" + nbr;
        } else if (nbr < 10000) {
            return "00" + nbr;
        } else if (nbr <= 100000) {
            return "0" + nbr;
        } else {
            return "" + nbr;
        }
    }

    private YvsGrhTypeCout buildTypeCout(TypeCout c) {
        YvsGrhTypeCout t = new YvsGrhTypeCout();
        t.setId(c.getId());
        t.setLibelle(c.getLibelle());
        t.setSociete(currentAgence.getSociete());
        return t;
    }

    private YvsGrhDetailGrilleFraiMission buildCoutMission(DetailFraisMission c) {
        YvsGrhDetailGrilleFraiMission cm = new YvsGrhDetailGrilleFraiMission();
        cm.setId(c.getId());
        cm.setGrilleMission(new YvsGrhGrilleMission(grilleM.getId()));
        cm.setMontantPrevu(c.getMontant());
        cm.setAuthor(currentUser);
        cm.setTypeCout(buildTypeCout(c.getTypeCout()));
        cm.setProportionelDuree(c.isProportionelDuree());
        return cm;
    }

    public void loadDemandeVue() {
        openDialog("dlgMission");
    }

    private void addParamInit() {
        ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
        if (service != null) {
            ParametreRequete p;
            switch (service.giveNumDroitAccesDossierEmp()) {
                case 0: //voir le dossier de tous les employés de la société//                    
                    p = new ParametreRequete("y.employe.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
                    paginator.addParam(p);
                    break;
                case 1://voir les dossiers des employés de l'agence de connexion seulement//                   
                    p = new ParametreRequete("y.employe.agence", "agence", currentUser.getAgence(), "=", "AND");
                    paginator.addParam(p);
                    break;
                case 2://voir seulement les dossiers des employés de son service
                    if (currentUser.getUsers().getEmploye() != null) {
                        if (currentUser.getUsers().getEmploye().getPosteActif() != null) {
                            List<Integer> l = service.giveAllSupDepartement(currentUser.getUsers().getEmploye().getPosteActif().getDepartement());
                            if (!l.isEmpty()) {
                                p = new ParametreRequete("y.employe.posteActif.departement.id", "agence", l, " IN ", "AND");
                                paginator.addParam(p);
                            } else {
                                return;
                            }
                        } else {
                            getErrorMessage("Aucun Département n'est spécifié pour l'employé de ce code utilisateur !");
                            return;
                        }
                    } else {
                        getErrorMessage("Aucun Employé n'est rattaché à ce compte utilisateur!");
                        return;
                    }
                    break;
                case 3://voir seulement les dossiers des employés de son equipe
                    break;
                case 4://voir seulement ses dossiers propre
                    if (currentUser.getUsers().getEmploye() != null) {
                        if (currentUser.getUsers().getEmploye().getPosteActif() != null) {
                            List<Integer> l = new ArrayList<>();
                            l.add(currentUser.getUsers().getEmploye().getPosteActif().getDepartement().getId());
                            if (!l.isEmpty()) {
                                p = new ParametreRequete("y.employe.posteActif.departement.id", "agence", l, " IN ", "AND");
                                paginator.addParam(p);
                            } else {
                                return;
                            }
                        } else {
                            getErrorMessage("Aucun Département n'est spécifié pour l'employé de ce code utilisateur !");
                            return;
                        }
                    } else {
                        getErrorMessage("Aucun Employé n'est rattaché à ce compte utilisateur!");
                        return;
                    }
                    break;
                default:
                    break;
            }
        }
        if (!includeDocCloture) {
            ParametreRequete p = new ParametreRequete("y.statutMission", "statutMission", 'C', "!=", "AND");
            paginator.addParam(p);
        }
    }

    public void loadAllMission(boolean avancer) {
        addParamInit();
        String query = "YvsGrhMissions y JOIN FETCH y.employe LEFT JOIN FETCH y.fraisMission LEFT JOIN FETCH y.lieu LEFT JOIN FETCH y.objetMission LEFT JOIN FETCH y.lieu LEFT JOIN FETCH y.validerBy";
        listMission = paginator.executeDynamicQuery("y", "y", query, "y.dateDebut DESC", avancer, initForm, (int) imax, dao);
        update("part-conge-btn");
        update("form_main_conge");
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        initForm = true;
        loadAllMission(true);
    }

    public void changePage(ValueChangeEvent ev) {
        imax = (long) ev.getNewValue();
        initForm = true;
        loadAllMission(true);
    }

    public void loadAllArticles() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listArticles = dao.loadNameQueries("YvsBaseArticles.findAll", champ, val);
    }

    public boolean saveNew1() {
        if (controleFicheMission(mission)) {
            if (mission.getId() <= 0) {
                selectMission = buildMission(mission);
                selectMission.setStatutMission(Constantes.STATUT_DOC_ATTENTE);
                selectMission.setAuthor(currentUser);
                selectMission.setId(null);
                selectMission.setFraisMissions(new ArrayList<YvsGrhFraisMission>());
                selectMission.setActif(true);
                selectMission = (YvsGrhMissions) dao.save1(selectMission);
                mission.setId(selectMission.getId());
                selectMission.setEtapesValidations(saveEtapesValidation(selectMission));
                mission.setEtapesValidations(new ArrayList<>(selectMission.getEtapesValidations()));
                mission.setEtapeTotal(selectMission.getEtapeTotal());
                saveNewLieuxEscale();
                initialiseAnalytique(selectMission, false);
                listMission.add(0, selectMission);
                succes();
                //sauvegarde les étapes de validation nécessaire                
            } else {
                //ne pas modifier si le processus de validation & déjà été enclanché ou si l'ordre de mission est clôturé
                if (!canUpdateOrdreM(mission)) {
                    //si j'ai le droit de modifier la date de la mission
                    return false;
                }
                selectMission = buildMission(mission);
                selectMission.setAuthor(currentUser);
                selectMission = (YvsGrhMissions) dao.update(selectMission);
                selectMission.setEtapesValidations(new ArrayList<>(mission.getEtapesValidations()));
                selectMission.setFraisMissions(new ArrayList<>(mission.getFraisMissions()));
                ManagedDico service = (ManagedDico) giveManagedBean("Mdico");
                if (service != null && selectMission.getLieu() != null) {
                    selectMission.setLieu(service.getListVille().get(service.getListVille().indexOf(selectMission.getLieu())));
                }
                dao.update(selectMission);
                if (listMission.contains(selectMission)) {
                    listMission.set(listMission.indexOf(selectMission), selectMission);
                } else {
                    listMission.add(0, selectMission);
                }
                actionOpenOrResetAfter(this);
                succes();
            }
        }
        return true;
    }
    private boolean addAllLine = true;

    public void saveNewAnalytique() {
        try {
            analytique.setMission(mission);
            if (controleFiche(analytique)) {
                selectAnalytique = UtilCompta.buildCentreMission(analytique, currentUser);
                if (analytique.getId() < 1) {
                    selectAnalytique = (YvsComptaCentreMission) dao.save1(selectAnalytique);
                } else {
                    dao.update(selectAnalytique);
                }
                int idx = mission.getAnalytiques().indexOf(selectAnalytique);
                if (idx < 0) {
                    mission.getAnalytiques().add(0, selectAnalytique);
                } else {
                    mission.getAnalytiques().set(idx, selectAnalytique);
                }
                resetFicheAnalytique();
                succes();
                update("zone_recap_mission:data-analytique_mi");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedMission (saveNewAnalytique)", ex);
        }
    }

    public boolean isAddAllLine() {
        return addAllLine;
    }

    public void setAddAllLine(boolean addAllLine) {
        this.addAllLine = addAllLine;
    }

    public void saveFraisMission() {
        if (mission.getStatutMission() != Constantes.STATUT_DOC_VALIDE && mission.getStatutMission() != Constantes.STATUT_DOC_CLOTURE) {
            if (mission.getFraisMission().getId() > 0) {
                ManagedFraisMission service = (ManagedFraisMission) giveManagedBean(ManagedFraisMission.class);
                if (service != null) {
                    int idx = service.getGrillesMissions().indexOf(new YvsGrhGrilleMission(mission.getFraisMission().getId()));
                    if (idx >= 0) {
                        mission.setFraisMission(UtilGrh.buildGrilleMission(service.getGrillesMissions().get(idx)));
                    }
                }
                buildFraisMission(mission.getFraisMission());
                if (mission.getId() > 0) {
                    deleteFrais();
                    YvsGrhMissions miss = (YvsGrhMissions) dao.loadOneByNameQueries("YvsMissions.findById", new String[]{"id"}, new Object[]{mission.getId()});
                    updateFrais(mission.getFraisMission().getId());
                    for (YvsGrhFraisMission fm : mission.getFraisMissions()) {
                        fm.setMission(miss);
                        if (fm.getId() <= 0) {
                            fm.setId(null);
                            fm = (YvsGrhFraisMission) dao.save1(fm);
                        } else {
                            dao.update(fm);
                        }
                    }
                    miss.getFraisMissions().addAll(mission.getFraisMissions());
                    int idx = listMission.indexOf(miss);
                    if (idx >= 0) {
                        listMission.set(idx, miss);
                    }
                    succes();
                } else {
                    getErrorMessage("Votre formulaire est incorrecte !");
                }
            } else {
                getErrorMessage("Aucune grille n'a été selectionné !");
            }
        } else {
            getErrorMessage("La mission est déjà validé !");
        }
    }

    private boolean canUpdateOrdreM(Mission m) {
        if (m.getId() > 0) {
            if (!autoriser("mission_update_date_mission")) {
                for (YvsWorkflowValidMission e : m.getEtapesValidations()) {
                    if (e.getEtapeValid()) {
                        if (!asDroitValideEtape(e.getEtape(), currentUser.getUsers())) {
                            getErrorMessage("Cet ordre de mission ne peut plus être modifier ", "le processus de validation a déjà été enclanché !");
                        }
                        return false;
                    }
                }
            }
            if (mission.isCloturer()) {
                getErrorMessage("Cet ordre de mission ne peut plus être modifier ", "L'ordre de mission a été clôturé !");
                return false;
            }
        }
        return true;
    }

    public void saveNewType() {
        YvsGrhTypeCout tc = buildTypeCout(cout.getTypeCout());
        tc.setId(null);
        dao.save1(tc);
//        loadGrilleMission();
        update("cout");
    }

    public boolean researchLieuFct() {
        champ = new String[]{"lieu", "societe"};
        val = new Object[]{lieuEscale, currentAgence.getSociete()};
        listMission = dao.loadNameQueries("YvsMissions.findAll3", champ, val);
        update("liste-mission");
        return true;
    }

    public boolean researchObjetFct() {
        champ = new String[]{"ordre", "societe"};
        val = new Object[]{"%" + objet + "%", currentAgence.getSociete()};
        listMission = dao.loadNameQueries("YvsMissions.findAll4", champ, val);
        update("liste-mission");
        return true;
    }

    public boolean researchDateFct() {
        champ = new String[]{"date", "societe"};
        val = new Object[]{dateSearch, currentAgence.getSociete()};
        listMission = dao.loadNameQueries("YvsMissions.findAll5", champ, val);
        update("liste-mission");
        return true;
    }

    @Override
    public void resetFiche() {
        resetFiche(mission);
        mission.setId(-1);
        mission.setObjet(new ObjetMission());
        mission.getRessources().clear();
        mission.setLieu(new Dictionnaire());
        mission.setFraisMission(new GrilleFraisMission());
        article = new Articles();
        selection = false;
        selectionMissions.clear();
        mission.getPiecesCaisses().clear();
        mission.getEtapesValidations().clear();
        mission.setEmploye(new Employe());
        mission.setCloturer(false);
        mission.setStatutMission(Constantes.STATUT_DOC_ATTENTE);
        mission.getFraisMissions().clear();
        lieuEscale = null;
        selectMission = new YvsGrhMissions();
        lieuxEscale.clear();
        update("bloc-en-tete-mission");
        update("etapes_valide_mission");
        update("zone_recap_mission");
        update("zone_frai_miss");
        update("form_zone_empsMission");
        update("blog_form_mission");
        update("miss_options_m");
    }

    public void resetFicheAnalytique() {
        analytique = new CentreMission();
        selectAnalytique = new YvsComptaCentreMission();
        update("zone_recap_mission:form-analytique_mi");
    }

    public boolean controleFicheMission(Mission bean) {
        if (bean.getLieu().getId() <= 0) {
            getErrorMessage("Vous devez entrer un lieu");
            return false;
        }
        if (bean.getOrdre() == null) {
            getErrorMessage("Vous devez entrer un ordre de mission");
            return false;
        }
        if (bean.getDateDebut() == null || bean.getDateFin() == null) {
            getErrorMessage("Vous devez entrer une date");
            return false;
        }
        if (bean.getEmploye().getId() <= 0) {
            getErrorMessage("Vous devez choisir un employé !");
            return false;
        }
        //contrôle la coherence des date et heures
        if (bean.getHeureArrive() == null || bean.getHeureDepart() == null) {
            getErrorMessage("Les heures estimative de départ et de retours sont obligatoire !");
            return false;
        }
        if (Utilitaire.fabriqueTimeStamp(bean.getDateDebut(), bean.getHeureDepart()).after(Utilitaire.fabriqueTimeStamp(bean.getDateFin(), bean.getHeureArrive())) || Utilitaire.fabriqueTimeStamp(bean.getDateDebut(), bean.getHeureDepart()).equals(Utilitaire.fabriqueTimeStamp(bean.getDateFin(), bean.getHeureArrive()))) {
            getErrorMessage("Les heures estimative de départ et de retours sont incorrectes ou mal renseigné !");
            return false;
        }
        if (bean.getNumeroMission() == null || bean.getNumeroMission().trim().length() < 1) {
            String ref = genererReference(Constantes.TYPE_DOC_MISSION_NAME, bean.getDateDebut());
            bean.setNumeroMission(ref);
        }
        if (bean.getReference() == null || bean.getReference().trim().length() < 1) {
            bean.setReference(fabriqueReference(UtilGrh.buildDictionnaire(bean.getLieu(), currentUser, currentAgence.getSociete())));
        }
        return true;
    }

    public boolean controleFicheCout(DetailFraisMission bean) {
        if ("".equals(bean.getTypeCout().getLibelle())) {
            getMessage("Vous devez entrer un libellé", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getMontant() == 0) {
            getMessage("Vous devez entrer un montant", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    public void ajoutNewRessource() {
        openDialog("dlgNewRessource");
    }

    public void coutmission() {
        update("cout");
        openDialog("dlgCoutMission");
    }

    @Override
    public Mission recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
//        ressource.setArticle((Articles) ev.getObject());
//        update("FormMRess");
    }

    public void loadMissionOnView(Mission m) {
        cloneObject(mission, m);
        lieuxEscale.clear();
        if ((mission.getLieuEscale() != null) ? !mission.getLieuEscale().isEmpty() : false) {
            lieuxEscale.addAll(Arrays.asList(mission.getLieuEscale().trim().split(";")));
        }
        if (mission.getId() <= 0) {
            getWarningMessage("Error id");
        }
        PieceTresorerie pt;
        //chargement des pièces de trésorerie
        mission.getPiecesCaisses().clear();
        List<YvsComptaCaissePieceMission> lp = dao.loadNameQueries("YvsComptaCaissePieceMission.findByMission", new String[]{"mission"}, new Object[]{new YvsGrhMissions(m.getId())});
        for (YvsComptaCaissePieceMission c : lp) {
            pt = new PieceTresorerie(c.getId());
            pt.setMontant(c.getMontant());
            pt.setCaisse(UtilCompta.buildBeanCaisse(c.getCaisse()));
            pt.setCaissier(UtilUsers.buildBeanUsers(c.getCaissier()));
            pt.setDatePaiement(c.getDatePaiement());
            pt.setDatePiece(c.getDatePiece());
            pt.setDescription("PIECE DE CAISSE");
            mission.getPiecesCaisses().add(pt);
        }
        //chargement des bons
        List<YvsComptaJustifBonMission> lbp = dao.loadNameQueries("YvsComptaJustifBonMission.findByMission", new String[]{"mission"}, new Object[]{new YvsGrhMissions(m.getId())});
        for (YvsComptaJustifBonMission c : lbp) {
            pt = new PieceTresorerie(c.getPiece().getId());
            pt.setMontant(c.getPiece().getMontant());
            pt.setCaisse(UtilCompta.buildBeanCaisse(c.getPiece().getCaisse()));
            pt.setCaissier(UtilUsers.buildBeanUsers(c.getPiece().getCaissier()));
            pt.setDatePaiement(c.getPiece().getDateBon());
            pt.setDatePiece(c.getPiece().getDateBon());
            pt.setDescription("BON PROVISOIRE");
            mission.getPiecesCaisses().add(pt);
        }
    }

    public void loadOnViewAnalytique(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectAnalytique = (YvsComptaCentreMission) ev.getObject();
            analytique = UtilCompta.buildBeanCentreMission(selectAnalytique);
            update("zone_recap_mission:form-analytique_mi");
        }
    }

    public void unLoadOnViewAnalytique(UnselectEvent ev) {
        resetFicheAnalytique();
    }

    @Override
    public void onSelectDistant(YvsGrhMissions da) {
        if (da != null ? da.getId() > 0 : false) {
            onSelectObject(da);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Missions", "modRh", "smenMission", true);
            }
        }
    }

    @Override
    public void onSelectObject(YvsGrhMissions y) {
        selectMission = setMontantTotalMission(y);
        selectMission.setBonsProvisoire(dao.loadNameQueries("YvsComptaJustifBonMission.findByMission", new String[]{"mission"}, new Object[]{y}));
        loadMissionOnView(UtilGrh.buildBeanMission(y));
        mission.setEtapesValidations(ordonneEtapes(dao.loadNameQueries("YvsWorkflowValidMission.findByMission", new String[]{"mission"}, new Object[]{y})));
        mission.setFraisMissions(dao.loadNameQueries("YvsGrhFraisMission.findByMission", new String[]{"mission"}, new Object[]{y}));
        mission.setReglements(dao.loadNameQueries("YvsComptaCaissePieceMission.findByMission", new String[]{"mission"}, new Object[]{y}));
        currentNombre = listMission.indexOf(y) + 1;
        update("blog_form_mission");
    }

    public void loadEntityMission(SelectEvent ev) {
        onSelectObject((YvsGrhMissions) ev.getObject());
        selectionMissions.clear();
        selectionMissions.add((YvsGrhMissions) ev.getObject());
    }

    //suppression d'une mission
    @Override
    public void deleteBean() {
        System.err.println("chaineSelectMission = " + chaineSelectMission);
        if (autoriser("mission_del_mission_new") || autoriser("mission_del_mission")) {
            try {
                YvsGrhMissions mi;
                for (int idx : decomposeSelection(chaineSelectMission)) {
                    if (idx >= 0 && idx < listMission.size()) {
                        mi = listMission.get(idx);
                        if (mi.getStatutMission() == Constantes.STATUT_DOC_ATTENTE || mi.getStatutMission() == Constantes.STATUT_DOC_ANNULE) {
                            dao.delete(mi);
                            listMission.remove(mi);
                        } else {
                            getErrorMessage("La mission est déjà en cours de validation !");
                        }
                    }
                }
                resetFiche();
                succes();
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cette mission ! Elle est liée à d'autres ressources");
                log.log(Level.SEVERE, null, ex);
            }
        } else {
            openNotAcces();
        }
    }

    public void deleteBean_() {
        try {
            if (selectionMissions != null ? !selectionMissions.isEmpty() : false) {
                for (YvsGrhMissions mi : selectionMissions) {
                    mi.setAuthor(currentUser);
                    mi.setDateUpdate(new Date());
                    dao.delete(mi);
                }

            }
            listMission.removeAll(selectionMissions);
            resetFiche();
            succes();
        } catch (Exception e) {
            getErrorMessage("Impossible de supprimer cette mission ! Elle est liée à d'autres ressources");
            log.log(Level.SEVERE, null, e);
        }
    }

    public void askDeleteOneMission(YvsGrhMissions mi) {
        loadMissionOnView(UtilGrh.buildBeanMission(mi));
        currentNombre = listMission.indexOf(mi) + 1;
        openDialog("dlgDelMission1");
    }

    public void deleteOneMission() {
        if (mission.getId() > 0) {
            if (mission.getStatutMission() == Constantes.STATUT_DOC_ATTENTE) {
                if (autoriser("mission_del_mission_new") || autoriser("mission_del_mission")) {
                    try {
                        YvsGrhMissions mi = new YvsGrhMissions(mission.getId());
                        dao.delete(mi);
                        if (listMission.contains(mi)) {
                            listMission.remove(mi);
                        }
                        resetFiche();
                        succes();
                    } catch (Exception ex) {
                        getErrorMessage("Impossible de supprimer cette mission ! Elle est liée à d'autres ressources");
                        log.log(Level.SEVERE, null, ex);
                    }
                } else {
                    openNotAcces();
                }
            } else if (mission.getStatutMission() == Constantes.STATUT_DOC_EDITABLE || mission.getStatutMission() == Constantes.STATUT_DOC_ANNULE) {
                if (autoriser("mission_del_mission")) {
                    try {
                        YvsGrhMissions mi = new YvsGrhMissions(mission.getId());
                        dao.delete(mi);
                        if (listMission.contains(mi)) {
                            listMission.remove(mi);
                        }
                        resetFiche();
                        succes();
                    } catch (Exception ex) {
                        getErrorMessage("Impossible de supprimer cette mission ! Elle est liée à d'autres ressources");
                        log.log(Level.SEVERE, null, ex);
                    }
                } else {
                    openNotAcces();
                }
            } else {
                getErrorMessage("La mission est déjà en cours de validation !");
            }
        }
    }

    public void deleteAnalytique(YvsComptaCentreMission y, boolean delete) {
        try {
            if (y != null) {
                if (!delete) {
                    selectAnalytique = y;
                    openDialog("dlgConfirmDeleteAnalytique");
                    return;
                }
                dao.delete(y);
                mission.getAnalytiques().remove(y);
                if (analytique.getId() == y.getId()) {
                    resetFicheAnalytique();
                }
                update("zone_recap_mission:data-analytique_mi");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedMission (deleteAnalytique)", ex);
        }
    }

    public void initialiseAnalytique(YvsGrhMissions y, boolean msg) {
        if (y != null ? y.getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez selectionner une mission");
            }
            return;
        }
        if (y.getObjetMission() != null ? y.getObjetMission().getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Cette mission n'a pas d'objetif de mission");
            }
            return;
        }
        List<YvsGrhObjetsMissionAnalytique> list = dao.loadNameQueries("YvsGrhObjetsMissionAnalytique.findByObject", new String[]{"objetMission"}, new Object[]{y.getObjetMission()});
        YvsComptaCentreMission c;
        for (YvsGrhObjetsMissionAnalytique a : list) {
            c = (YvsComptaCentreMission) dao.loadOneByNameQueries("YvsComptaCentreMission.findByMissionCentre", new String[]{"mission", "centre"}, new Object[]{y, a.getCentre()});
            if (c != null ? c.getId() < 1 : true) {
                c = new YvsComptaCentreMission(y, a.getCentre());
                c.setCoefficient(a.getCoefficient());
                c.setAuthor(currentUser);
                c = (YvsComptaCentreMission) dao.save1(c);
                y.getAnalytiques().add(c);
                if (y.getId().equals(mission.getId())) {
                    int idx = mission.getAnalytiques().indexOf(c);
                    if (idx < 0) {
                        mission.getAnalytiques().add(c);
                    }
                }
            }
        }
        if (msg) {
            succes();
        }
    }

    public void clotureOrdreMission() {
        //l'ordre de mission doit être valide et la date de fin doit être atteint        
        boolean update = true;
        for (YvsGrhMissions mi : selectionMissions) {
            if (mi.getStatutMission() != 'V' && mi.getStatutMission() != 'C') {
                getErrorMessage("L'ordre de mission n'a pas été validé !");
                update = false;
                return;
            } else if (mi.getDateFin().after(new Date())) {
                getErrorMessage("La mission est encore en cours. voulez-vous vraiment la clôturer? ");
                update = false;
                return;
            }
        }
        if (update) {
            for (YvsGrhMissions mi : selectionMissions) {
                mi.setAuthor(currentUser);
                mi.setStatutMission(Constantes.STATUT_DOC_CLOTURE);
                listMission.get(listMission.indexOf(mi)).setStatutMission('C');
                dao.update(mi);
            }
        }
        succes();
    }

    @Override
    public void loadAll() {
        loadInfosWarning(false);
        if (isWarning != null ? isWarning : false) {
            loadByWarning();
        } else {
            addParamToValide();
        }
    }

    private void loadByWarning() {
        paginator.clear();
        loadInfosWarning(true);
        addParamIds(true);
        loadAllMission(true);
    }

    public void addParamToValide() {
        ParametreRequete p = new ParametreRequete("(y.etapeValide+1)", "etape", null, "IN", "AND");
        if (toValideLoad) {
            List<Integer> lnum = dao.loadNameQueries("YvsWorkflowAutorisationValidDoc.findOrdreStepe", new String[]{"document", "niveau"}, new Object[]{Constantes.DOCUMENT_MISSION, currentUser.getUsers().getNiveauAcces()});
            if ((lnum != null) ? !lnum.isEmpty() : false) {
                p = new ParametreRequete("(y.etapeValide+1)", "etape", lnum, "IN", "AND");
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllMission(true);
    }

    @Override
    public boolean controleFiche(Mission bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(Mission bean) {

    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void chooseCentre() {
        if (analytique.getCentre() != null ? analytique.getCentre().getId() > 0 : false) {
            ManagedCentreAnalytique service = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
            if (service != null) {
                int idx = service.getCentres().indexOf(new YvsComptaCentreAnalytique(analytique.getCentre().getId()));
                if (idx > -1) {
                    YvsComptaCentreAnalytique y = service.getCentres().get(idx);
                    analytique.setCentre(UtilCompta.buildBeanCentreAnalytique(y));
                }
            }
        }
    }

    public void chooseville(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            Dictionnaire id = (Dictionnaire) ev.getNewValue();
        }
    }

    boolean updateTypeCout;

    public void saveNewTypeCout() {
        try {
            if (typeCout.getLibelle() != null && !typeCout.getLibelle().equals("")) {
                YvsGrhTypeCout entity = new YvsGrhTypeCout();
                entity.setLibelle(typeCout.getLibelle());
                entity.setSociete(currentUser.getAgence().getSociete());
                entity.setAuthor(currentUser);
                entity.setId(typeCout.getId());
                if (!updateTypeCout) {
                    entity.setId(null);
                    entity = (YvsGrhTypeCout) dao.save1(entity);
                    typeCout.setId(entity.getId());
                    cout.setTypeCout(typeCout);
                    listTypeCout.add(0, entity);
                } else {
                    dao.update(entity);
                    listTypeCout.set(listTypeCout.indexOf(entity), entity);
                }
                typeCout = new TypeCout();
                updateTypeCout = false;
                update("coutForm");
            } else {
                getErrorMessage("Vous devez entrer le libelle");
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            Logger.getLogger(ManagedMission.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void choixTypeCout(SelectEvent ev) {
        if (ev != null) {
            YvsGrhTypeCout tc = (YvsGrhTypeCout) ev.getObject();
            typeCout = UtilGrh.buildBeanTypeCout(tc);
            updateTypeCout = true;
            update("coutForm");
        }
    }

    public void deleteTypeCout(YvsGrhTypeCout ev) {
        if (ev != null) {
            try {
                ev.setAuthor(currentUser);
                dao.delete(ev);
                listTypeCout.remove(ev);
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer !");
                log.log(Level.SEVERE, null, ex);
            }
        }
    }
    boolean updateGrille;

    public boolean saveElementGrille() {
        if (grilleM.getId() <= 0) {
            getErrorMessage("Aucune grille n'a été trouvé !");
            return false;
        }
        if (cout.getTypeCout().getId() > 0) {
            YvsGrhDetailGrilleFraiMission cm = buildCoutMission(cout);
            cm.setTypeCout(listTypeCout.get(listTypeCout.indexOf(cm.getTypeCout())));
            YvsGrhDetailGrilleFraiMission d = containsCout(grilleM.getDetailsFrais(), cout.getTypeCout());
            if (d == null) {
                cm.setId(null);
                cm = (YvsGrhDetailGrilleFraiMission) dao.save1(cm);
                cout.setId(cm.getId());
                cout.getTypeCout().setLibelle((listTypeCout.get(listTypeCout.indexOf(new YvsGrhTypeCout(cout.getTypeCout().getId())))).getLibelle());
                grilleM.getDetailsFrais().add(0, cm);
            } else {
                cm.setId(d.getId());
                dao.update(cm);
                grilleM.getDetailsFrais().set(grilleM.getDetailsFrais().indexOf(cm), cm);
            }
            cout = new DetailFraisMission();
            update("coutTab");
            update("coutTab2");
        }
        return true;
    }

    public void addAllCout() {
        if (grilleM.getId() > 0) {
            YvsGrhDetailGrilleFraiMission cm;
            for (YvsGrhTypeCout tc : listTypeCout) {
                if (containsCout(grilleM.getDetailsFrais(), new TypeCout(tc.getId(), tc.getLibelle())) == null) {
                    cm = new YvsGrhDetailGrilleFraiMission();
                    cm.setId(null);
                    cm.setGrilleMission(new YvsGrhGrilleMission(grilleM.getId()));
                    cm.setMontantPrevu(0.0);
                    cm.setAuthor(currentUser);
                    cm.setTypeCout(tc);
                    cm.setProportionelDuree(false);
                    cm = (YvsGrhDetailGrilleFraiMission) dao.save1(cm);
                    grilleM.getDetailsFrais().add(0, cm);
                }
            }
        } else {
            getErrorMessage("Aucune grille n'a été selectionné !");
        }
    }

    private YvsGrhDetailGrilleFraiMission containsCout(List<YvsGrhDetailGrilleFraiMission> l, TypeCout idType) {
        for (YvsGrhDetailGrilleFraiMission c : l) {
            if (c.getTypeCout().equals(new YvsGrhTypeCout(idType.getId()))) {
                return c;
            }
        }
        return null;
    }

    public void resetGrilleFM() {
        grilleM = new GrilleFraisMission();
        updateGrille = false;
    }

    public void deleteLineCout(YvsGrhDetailGrilleFraiMission de) {
        try {
            de.setAuthor(currentUser);
            dao.delete(de);
            mission.getFraisMission().getDetailsFrais().remove(de);
            grilleM.getDetailsFrais().remove(de);
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cette ligne ");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void updateCoutMission(RowEditEvent ev) {
        if (ev != null) {
            if (canUpdateOrdreM(mission)) {
                YvsGrhFraisMission d = (YvsGrhFraisMission) ev.getObject();
                d.setAuthor(currentUser);
                int idx = mission.getFraisMissions().indexOf(d);
                if (d.getId() > 0) {
                    dao.update(d);
                } else {
                    ManagedTypeCout serv = (ManagedTypeCout) giveManagedBean(ManagedTypeCout.class);
                    if (serv != null) {
                        int i = serv.getTypes().indexOf(d.getTypeCout());
                        if (i >= 0) {
                            d.setTypeCout(serv.getTypes().get(i));
                        }
                    }
                    d.setId(null);
                    d = (YvsGrhFraisMission) dao.save1(d);
                }
                if (idx >= 0) {
                    mission.getFraisMissions().set(idx, d);
                }
            }
            update("table_grilleFMM");
        }
    }
    long idTemp = 1000;

    public void addLineFraisMission() {
        if (mission.getId() > 0) {
            if (mission.getStatutMission() != Constantes.STATUT_DOC_CLOTURE && mission.getStatutMission() != Constantes.STATUT_DOC_VALIDE) {
                YvsGrhFraisMission d = new YvsGrhFraisMission(-idTemp++);
                d.setAuthor(currentUser);
                d.setMission(new YvsGrhMissions(mission.getId()));
                d.setMontant(0);
                d.setProportionelDuree(false);
                d.setTypeCout(new YvsGrhTypeCout((long) 0));
                mission.getFraisMissions().add(0, d);
                update("table_grilleFMM");
            } else {
                getErrorMessage("L'ordre de mission n'est plus Editable !");
            }
        } else {
            getErrorMessage("Aucune mission n'a été selectionné !");
        }
    }

    public void deleteLineFrais(YvsGrhFraisMission d) {
        if (d != null) {
            if (autoriser("mission_add_frais_mission")) {
                if ((mission.getStatutMission() != 'V' && mission.getStatutMission() != 'C') || d.getMontant() == 0) {
                    if (d.getId() > 0) {
                        d.setMontant(d.getMontant());
                        if ((d.getMission().getStatutMission() != 'V' && d.getMission().getStatutMission() != 'C') || d.getMontant() == 0) {
                            try {
                                d.setAuthor(currentUser);
                                dao.delete(d);
                                mission.getFraisMissions().remove(d);
                            } catch (Exception ex) {
                                getErrorMessage("Impossible de supprimer !");
                            }
                        } else {
                            getErrorMessage("Vous ne pouvez soustraire ces frais la mission est déjà clôturé !");
                        }
                    } else {
                        mission.getFraisMissions().remove(d);
                    }
                } else {
                    getErrorMessage("Impossible de modifier cette ligne car la mission est déjà validé !");

                }
            } else {
                openNotAcces();
            }
        }
    }

    public void openDlgaddCommentFrais(YvsGrhFraisMission lf) {
        selectedLineFrais = lf;
        commentaireLineFrais = lf.getNote();
        openDialog("dlgAddComment");
        update("zone_recap_mission:table_grilleFMM");
    }

    public void addCommentFrais() {
        if (selectedLineFrais != null) {
            selectedLineFrais.setNote(commentaireLineFrais);
            selectedLineFrais.setAuthor(currentUser);
            dao.update(selectedLineFrais);
            succes();
            update("zone_recap_mission:table_grilleFMM");
        }
    }

    public void addNewFrais() {
        if (autoriser("mission_add_frais_mission")) {
        } else {
            openNotAcces();
        }
    }

    public void findBonProvisoire() {
        ManagedBonProvisoire service = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
        if (service != null) {
            this.select = false;
            String num = piece.getNumero();
            if ((num != null) ? !num.isEmpty() : false) {
                piece = service.findBonProvisoire(num, true);
            }
        }
    }

    public void forceOnViewMission() {
        if (selectMission != null ? selectMission.getId() > 0 : false) {
//            forceOnViewMission(piece, selectMission);
        }
    }

    public void forceOnViewMission(YvsComptaBonProvisoire piece, YvsComptaCaissePieceMission select) {
        if (piece != null ? piece.getId() > 0 : false) {
            if (piece.getBonMission() != null) {
                piece.getBonMission().setMission(select);
            }
            this.piece = piece;
            if (select != null ? select.getId() > 0 : false) {
                addBonProvisoire(piece, select, true, true);
            }
        }
    }

    public void addBonProvisoire() {
        if (selectMission != null ? selectMission.getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner une mission!!!");
            return;
        }
        ManagedBonProvisoire w = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
        ManagedReglementMission w1 = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class);
        if (w != null) {
            if (piece != null ? piece.getId() < 1 : false) {
                if (piece.getMontant() > selectMission.getTotalResteAPlanifier()) {
                    getErrorMessage("Le total des paiement planifié pour cet ordre de mission sera supérieur au montant des frais");
                    return;
                }
                piece.setDescription("BON PROVISOIRE POUR FRAIS DE MISSION N°" + selectMission.getNumeroMission() + " De " + selectMission.getEmploye().getNom_prenom());
                piece.setTiers(selectMission.getEmploye().getCompteTiers());
                piece.setBeneficiaire(piece.getTiers() != null ? piece.getTiers().getNom_prenom() : null);
                piece.setOrdonnateur(currentUser.getUsers());
                piece.setStatut(Constantes.ETAT_EDITABLE);
                piece.setStatutPaiement(Constantes.ETAT_ATTENTE);
                piece.setStatutJustify(Constantes.ETAT_ATTENTE);
                piece.setAgence(currentAgence);
                piece.setAuthor(currentUser);
                if (w.saveNew(piece)) {
                    w.setBonProvisoire(UtilCompta.buildBeanBonProvisoire(piece));
                    //valide implicitement les étapes de validation du bp
                    for (YvsWorkflowValidBonProvisoire e : piece.getEtapesValidations()) {
                        //valider les étapes du bon
                        w.validEtapeOrdre(e, false, false);
                    }
                    //Génération de la pièce mission
                     /*Construit la pièce de trésorerie*/
                    PieceTresorerie pt = new PieceTresorerie();
                    pt.setMontant(piece.getMontant());
                    pt.setDatePaiementPrevu(piece.getDateBon());
                    pt.setDatePiece(piece.getDateBon());
                    pt.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
                    pt.setCaisse(UtilCompta.buildBeanCaisse(piece.getCaisse()));
                    YvsComptaCaissePieceMission pc = w1.createOnePieceCaisse(selectMission, pt, true, true);
                    if (pc != null) {
                        //liaison du bon provisoire à la mission
                        YvsComptaJustifBonMission y = addBonProvisoire(piece, pc, true, true);
                        if (y != null ? y.getId() > 0 : false) {
                            resetBonProvisoire();
                        }
                    }
                }
            }
        }
    }

    public void addBonProvisoireAndValide() {
        /*Construit la pièce de trésorerie*/
        ManagedReglementMission w1 = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class);
        PieceTresorerie pt = new PieceTresorerie();
        pt.setMontant(piece.getMontant());
        pt.setDatePaiementPrevu(piece.getDateBon());
        pt.setDatePiece(piece.getDateBon());
        pt.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        pt.setCaisse(UtilCompta.buildBeanCaisse(piece.getCaisse()));
        YvsComptaCaissePieceMission pc = w1.createOnePieceCaisse(selectMission, pt, true, true);
        if (pc != null) {
            YvsComptaJustifBonMission y = addBonProvisoire(piece, pc, true, false);
            if (y != null ? y.getId() > 0 : false) {
                validEtapeOrdreMission(currentEtape, true);
                closeDialog("dlgValidOM");
            }
        }
    }

    public YvsComptaJustifBonMission addBonProvisoire(YvsComptaBonProvisoire bon, YvsComptaCaissePieceMission piece, boolean msg, boolean succes) {
        if (piece != null ? piece.getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez selectionner une mission!!!");
            }
            return null;
        }
        if (bon != null ? bon.getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez enregistrer un bon provisoire!!!");
            }
            return null;
        }
        if (bon.getBonMission() != null ? (bon.getBonMission().getId() > 0 ? !bon.getBonMission().getMission().equals(piece) : false) : false) {
            openDialog("dlgConfirmBonProvisoire");
            update("cfm_bon_mission");
            return null;
        }
        if (bon.getMontant() <= 0) {
            if (msg) {
                getErrorMessage("Vous devez entrer le montant du bon");
            }
            return null;
        }
        if (piece.getMontant() > bon.getMontant()) {
            if (msg) {
                getErrorMessage("Impossible d'associer une piece de reglement supérieur à la valeur du bon");
            }
            return null;
        }
        YvsComptaJustifBonMission y = new YvsComptaJustifBonMission(piece, bon);
        y.setAuthor(currentUser);
        if (bon.getBonMission() != null ? bon.getBonMission().getId() < 1 : true) {
            champ = new String[]{"mission", "piece"};
            val = new Object[]{piece, bon};
            YvsComptaJustifBonMission old = (YvsComptaJustifBonMission) dao.loadOneByNameQueries("YvsComptaJustifBonMission.findOne", champ, val);
            if (old != null ? old.getId() < 1 : true) {
                y.setDateSave(new Date());
                y.setDateUpdate(new Date());
                y.setId(null);
                y = (YvsComptaJustifBonMission) dao.save1(y);
            } else {
                y = old;
            }
        } else {
            y.setId(bon.getBonMission().getId());
            dao.update(y);
        }
        bon.setBonMission(y);
        piece.setJustify(y);
        int idx = selectMission.getBonsProvisoire().indexOf(y);
        if (idx > 0) {
            selectMission.getBonsProvisoire().set(idx, y);
        } else {
            selectMission.getBonsProvisoire().add(0, y);
        }
        if (succes) {
            succes();
        }
        return y;
    }

    public void resetBonProvisoire() {
        piece = new YvsComptaBonProvisoire();
        piece.setDateSave(new Date());
        piece.setDateUpdate(new Date());
        ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (!service.getCaisses().isEmpty()) {
            piece.setCaisse(service.getCaisses().get(0));
        } else {
            piece.setCaisse(new YvsBaseCaisse());
        }
        piece.setAuthor(currentUser);
        piece.setStatut(Constantes.ETAT_EDITABLE);
        piece.setStatutPaiement(Constantes.ETAT_ATTENTE);
        piece.setStatutJustify(Constantes.ETAT_ATTENTE);
        piece.setAgence(currentAgence);
        piece.setMontant(selectMission != null ? selectMission.getTotalReste() > 0 ? selectMission.getTotalReste() : 0 : 0);
    }

    public void deleteBonProvisoire(YvsComptaJustifBonMission y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (selectMission.getStatutMission().equals(Constantes.STATUT_DOC_VALIDE)) {
                    getErrorMessage("Vous ne pouvez pas supprimer ce bon provisoire car la mission est déjà validée");
                    return;
                }
                dao.delete(y);
                if (y.getPiece().canDelete()) {
                    dao.delete(y.getPiece());
                }
                selectMission.getBonsProvisoire().remove(y);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("ManagedMission (deleteBonProvisoire) ", ex);
        }
    }

    public void loadDetailCout(SelectEvent ev) {
        YvsGrhDetailGrilleFraiMission c = (YvsGrhDetailGrilleFraiMission) ev.getObject();
        cout = new DetailFraisMission(c.getMontantPrevu());
        cout.setId(c.getId());
        cout.setProportionelDuree(c.getProportionelDuree());
        cout.setSave(true);
        cout.setTypeCout(UtilGrh.buildBeanTypeCout(c.getTypeCout()));
        update("coutForm");
        openDialog("dlgAdddetailFrais");
    }

    public void selectionGrille(SelectEvent ev) {
        long idSelectedFrais = ((YvsGrhGrilleMission) ev.getObject()).getId();
        mission.getFraisMission().setId(idSelectedFrais);
        openDialog("dlgConfirmAddFrais");
    }

    public void openGrilleToUpdate(YvsGrhGrilleMission c) {
        if (c != null) {
            grilleM = UtilGrh.buildGrilleMission(c);
            updateGrille = true;
        }
    }

    public void openGrilleToUpdate_() {
        openDialog("dlgCoutMission");
    }

    public void saveGrilleFrais() {
//        if (grilleM.getCategorie() != null) {
//            YvsGrhGrilleMission gm = new YvsGrhGrilleMission();
//            gm.setActif(grilleM.isActif());
//            gm.setAuthor(currentUser);
//            gm.setCategorie(grilleM.getCategorie());
//            gm.setSociete(currentUser.getAgence().getSociete());
//            gm.setId(grilleM.getId());
//            gm.setNumCompte((grilleM.getCompteCharge().getId() > 0) ? new YvsBasePlanComptable(grilleM.getCompteCharge().getId()) : null);
//            grilleM.setId(gm.getId());
//            gm.setTitre(grilleM.getTitre());
//            if (!updateGrille) {
//                gm = (YvsGrhGrilleMission) dao.save1(gm);
//                grilleM.setId(gm.getId());
//                grillesMissions.add(0, gm);
//            } else {
//                dao.update(gm);
//                grillesMissions.set(grillesMissions.indexOf(gm), gm);
//            }
//            updateGrille = true;
//            succes();
//        } else {
//            getErrorMessage("Veuillez entrer la catégorie !");
//        }
        getFatalMessage("Action déplacé !");
    }

    public void notApplyNewFrais() {
        chooseGrilleFraisMission(new ValueChangeEvent(new SelectOneMenu(), (long) 0, (long) 0));
    }
//    long idSelectedFrais;

    public void chooseGrilleFraisMission(ValueChangeEvent ev) {
        if (ev != null) {
            if (ev.getNewValue() != null) {
                long idSelectedFrais = (long) ev.getNewValue();
                if (idSelectedFrais > 0) {
                    //si la mission à déjà été enregistré avec une grille différente
                    if (autoriser("mission_add_frais_mission")) {
                        openDialog("dlgConfirmAddFrais");
                    } else {
                        getErrorMessage("Vous n'avez pas accès à cette fonction !");
                        mission.getFraisMission().setId(0);
                        openNotAcces();
                    }
                } else if (idSelectedFrais == -1) {
                    openDialog("dlgCoutMission");
                } else {
//                    if (currentMission.getFraisMissions() != null) {
//                        if (currentMission.getFraisMissions().isEmpty()) {
//                            mission.getFraisMissions().clear();
//                        }
//                    }
                }
            }
        }
    }

    private void deleteFrais() {
        if (mission.getId() > 0) {
            //supprimez le frais de la mission en cous 
            String rq = "DELETE FROM Yvs_grh_frais_mission WHERE mission=?";
            Options[] param = new Options[]{new Options(mission.getId(), 1)};
            dao.requeteLibre(rq, param);
        }
    }

    private void updateFrais(long idFrais) {
        if (mission.getId() > 0) {
            //Modifier les frais
            String rq = "UPDATE yvs_grh_missions SET frais_mission=? WHERE id=?";
            Options[] param = new Options[]{new Options(idFrais, 1), new Options(mission.getId(), 2)};
            dao.requeteLibre(rq, param);
        }
    }

    private void buildFraisMission(GrilleFraisMission gf) {
        mission.getFraisMissions().clear();
        for (YvsGrhDetailGrilleFraiMission d : gf.getDetailsFrais()) {
            if (mission.getFraisMissions() == null) {
                mission.setFraisMissions(new ArrayList<YvsGrhFraisMission>());
            }
            YvsGrhFraisMission fm = containsFrais(d.getTypeCout());
            if (fm == null) {
                fm = new YvsGrhFraisMission();
                fm.setAuthor(currentUser);
                fm.setMission(new YvsGrhMissions(mission.getId()));
                if (!d.getProportionelDuree()) {
                    fm.setMontant(d.getMontantPrevu());
                } else {
                    fm.setMontant(d.getMontantPrevu() * countDayMission(mission.getDateDebut(), mission.getDateFin()));
                }
                fm.setTypeCout(d.getTypeCout());
                fm.setId(-d.getId());
                if (fm.getMontant() <= 0 && !addAllLine) {
                    continue;
                }
                mission.getFraisMissions().add(fm);
            } else {
                fm.setProportionelDuree(d.getProportionelDuree());
                if (!fm.isProportionelDuree()) {
                    fm.setMontant(d.getMontantPrevu());
                } else {
                    fm.setMontant(d.getMontantPrevu() * countDayMission(mission.getDateDebut(), mission.getDateFin()));
                }
                fm.setAuthor(currentUser);
                if (fm.getMontant() <= 0 && !addAllLine) {
                    mission.getFraisMissions().remove(mission.getFraisMissions().indexOf(fm));
                    continue;
                }
                mission.getFraisMissions().set(mission.getFraisMissions().indexOf(fm), fm);
            }
            mission.setTotalFraisMission(mission.getTotalFraisMission() + fm.getMontant());
        }
    }

    private YvsGrhFraisMission containsFrais(YvsGrhTypeCout tc) {
        for (YvsGrhFraisMission fm : mission.getFraisMissions()) {
            if (fm.getTypeCout().equals(tc)) {
                return fm;
            }
        }
        return null;
    }

    public void evalueTotalFraisMission() {
        calculDateRetour();
        if (mission.getDateDebut() != null && mission.getDateFin() != null && mission.getFraisMissions() != null) {
            int dure = countDayMission(mission.getDateDebut(), mission.getDateFin());
            mission.setTotalFraisMission(0);
            for (YvsGrhFraisMission fm : mission.getFraisMissions()) {
                if (fm.isProportionelDuree()) {
                    mission.setTotalFraisMission(mission.getTotalFraisMission() + fm.getMontant() * dure);
                    fm.setMontant(fm.getMontant() * dure);
                } else {
                    mission.setTotalFraisMission(mission.getTotalFraisMission() + fm.getMontant());
                }
            }
        }
    }

    /**
     * ******************************************
     */
    private List<YvsWorkflowValidMission> saveEtapesValidation(YvsGrhMissions m) {
        //charge les étape de vailidation
        List<YvsWorkflowValidMission> re = new ArrayList<>();
        champ = new String[]{"titre", "societe"};
        val = new Object[]{Constantes.DOCUMENT_MISSION, currentAgence.getSociete()};
        List<YvsWorkflowEtapeValidation> model = dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", champ, val);
        if (!model.isEmpty()) {
            YvsWorkflowValidMission vm;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    vm = new YvsWorkflowValidMission();
                    vm.setAuthor(currentUser);
                    vm.setEtape(et);
                    vm.setEtapeValid(false);
                    vm.setMission(m);
                    vm.setOrdreEtape(et.getOrdreEtape());
                    vm = (YvsWorkflowValidMission) dao.save1(vm);
                    re.add(vm);
                }
            }
            m.setStatutMission(Constantes.STATUT_DOC_ATTENTE);
        } else {
//            m.setStatutMission(Constantes.STATUT_DOC_VALIDE);
        }
        m.setEtapeTotal(model.size() > 0 ? model.size() : 1);
        m.setEtapeValide(0);
        dao.update(m);
        return ordonneEtapes(re);
    }

    private List<YvsWorkflowValidMission> ordonneEtapes(List<YvsWorkflowValidMission> l) {
        return YvsWorkflowValidMission.ordonneEtapes(l);
    }

    private YvsWorkflowValidMission currentEtape;

    public void validEtapeOrdreMission(YvsWorkflowValidMission etape, boolean lastEtape) {
        //vérifier que la personne qui valide l'étape a le droit 
        currentEtape = etape;
        if (!asDroitValideEtape(etape.getEtape())) {
            openNotAcces();
        } else {
            //contrôle la cohérence des dates
//            if (selectMission == null) {
            selectMission = (YvsGrhMissions) dao.loadOneByNameQueries("YvsMissions.findById", new String[]{"id"}, new Object[]{mission.getId()});
            selectMission = setMontantTotalMission(selectMission);
//            }
            Date curr = Utilitaire.giveOnlyDate(new Date());
            if (!selectMission.getDateDebut().after(curr)) {
                getWarningMessage("La mission est déjà en cours d'exécuttion !", "");
            }
            if (selectMission.getDateFin().before(curr)) {
                getWarningMessage("La mission est déjà terminé !", "");
            }
            int idx = mission.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                if (etape.getEtapeSuivante() == null && !lastEtape) {
                    if (mission.getTotalFraisMission() > 0 && (selectMission.getBonsProvisoire() != null ? selectMission.getBonsProvisoire().isEmpty() : true)) {
                        //ouvre une bôite de dialogue de validation                        
                        if (mission.getRestPlanifier() > 0) {
                            ManagedReglementMission service = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class);
                            ManagedCaisses service1 = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                            if (service != null && service1 != null) {
                                service1.loadAllCaisseActif(true);
                                service.getPieceAvance().setId(-1);
                                service.getPieceAvance().setMontant(mission.getRestPlanifier());
                                if (!service1.getCaisses().isEmpty()) {
                                    service.getPieceAvance().setCaisse(UtilCompta.buildBeanCaisse(service1.getCaisses().get(0)));
                                }
                            }
                            forceGeneretedPc = true;
                            openDialog("dlgValidOM");
                            update("form_valid_pcMiss");
                            update("btn_control_valid_pv");
                            return;
                        }
                    }
                }
                selectMission.setStatutMission(Constantes.STATUT_DOC_EDITABLE);
                //cas de la validation de la dernière étapes
                boolean succes = true;
                if (etape.getEtapeSuivante() == null) {
                    valideMission(etape);
                } else {
                    etape.setAuthor(currentUser);
                    etape.setEtapeValid(true);
                    etape.setEtapeActive(false);
                    etape.setMotif(null);
                    etape.setDateUpdate(new Date());
                    dao.update(etape);
                    selectMission.setEtapeValide(selectMission.getEtapeValide() + 1);
                    dao.update(selectMission);
                    mission.setStatutMission(selectMission.getStatutMission());
                    idx = mission.getEtapesValidations().indexOf(etape);
                    if (mission.getEtapesValidations().size() > (idx + 1)) {
                        mission.getEtapesValidations().get(idx).setEtapeActive(false);
                        mission.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                    }
                    if (listMission.contains(selectMission)) {
                        int idx_ = listMission.indexOf(selectMission);
                        listMission.get(idx_).setEtapeValide(idx + 1);
                        listMission.get(idx_).setStatutMission(selectMission.getStatutMission());
                        idx = listMission.get(idx_).getEtapesValidations().indexOf(etape);
                        if (idx >= 0) {
                            listMission.get(idx_).getEtapesValidations().set(idx, etape);
                        }
                    }
                    getInfoMessage("Validation effectué avec succès !");
                }
            }
        }
    }

    public void generatedPiece(YvsGrhMissions y) {
        if (y != null ? (y.getStatutMission().equals(Constantes.STATUT_DOC_VALIDE) && !y.getStatutPaiement().equals(Constantes.STATUT_DOC_PAYER)) : false) {
            onSelectObject(y);
//            y = setMontantTotalMission(y);
//            selectMission=y;
            if (mission.getTotalFraisMission() > 0 && (selectMission.getBonsProvisoire() != null ? selectMission.getBonsProvisoire().isEmpty() : true)) {
                //ouvre une bôite de dialogue de validation                        
                if (mission.getRestPlanifier() > 0) {
                    ManagedReglementMission service = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class);
                    ManagedCaisses service1 = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                    if (service != null && service1 != null) {
                        service1.loadAllCaisseActif(true);
                        service.getPieceAvance().setId(-1);
                        service.getPieceAvance().setMontant(mission.getRestPlanifier());
                        if (!service1.getCaisses().isEmpty()) {
                            service.getPieceAvance().setCaisse(UtilCompta.buildBeanCaisse(service1.getCaisses().get(0)));
                        }
                    }
                    forceGeneretedPc = true;
                    generetedPc = true;
                    openDialog("dlgValidOM");
                    update("form_valid_pcMiss");
                    update("btn_control_valid_pv");
                    return;
                }
            }
        } else {
            getErrorMessage("Vous devez choisir un ordre de mission validé !");
        }

    }

    public boolean valideMission(YvsWorkflowValidMission etape) {
        if (autoriser("grh_valid_mission")) {
            selectMission.setStatutMission(Constantes.STATUT_DOC_VALIDE);
            selectMission.setValiderBy(currentUser.getUsers());
            selectMission.setDateValider(new Date());
            if (etape == null) {
                selectMission.setDateUpdate(new Date());
                selectMission.setAuthor(currentUser);
                selectMission.setEtapeValide(1);
            } else {
                selectMission.setEtapeValide(selectMission.getEtapeValide() + 1);
            }
            if (valideBonProvisoire(selectMission, mission)) {
                if (etape != null) {
                    etape.setAuthor(currentUser);
                    etape.setEtapeValid(true);
                    etape.setEtapeActive(false);
                    etape.setMotif(null);
                    etape.setDateUpdate(new Date());
                    dao.update(etape);
                }
                int ids = listMission.indexOf(selectMission);
                dao.update(selectMission);
                mission.setStatutMission(selectMission.getStatutMission());
                if (etape != null) {
                    int idx = mission.getEtapesValidations().indexOf(etape);
                    if (mission.getEtapesValidations().size() > (idx + 1)) {
                        mission.getEtapesValidations().get(idx).setEtapeActive(false);
                        mission.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                    }
                    if (listMission.contains(selectMission)) {
                        int idx_ = listMission.indexOf(selectMission);
                        listMission.get(idx_).setEtapeValide(idx + 1);
                        listMission.get(idx_).setStatutMission(selectMission.getStatutMission());
                        idx = listMission.get(idx_).getEtapesValidations().indexOf(etape);
                        if (idx >= 0) {
                            listMission.get(idx_).getEtapesValidations().set(idx, etape);
                        }
                    }
                }
                if (ids >= 0) {
                    listMission.set(ids, selectMission);
                }
                getInfoMessage("Validation effectué avec succès !");
                generatedPiece(selectMission);
            } else {
                getErrorMessage("Impossible de continuer !");
            }
        } else {
            openNotAcces();
        }
        return false;
    }

    public boolean valideBonProvisoire(YvsGrhMissions entity, Mission bean) {
        try {
            if (entity != null ? (entity.getId() > 0 ? !entity.getBonsProvisoire().isEmpty() : false) : false) {
                double reste = entity.getTotalFraisMission() - entity.getTotalPiece() - entity.getTotalBonPaye();
                if (reste <= 0) {
                    System.out.println("reste : " + reste);
//                    return false;
                }
                YvsBaseModeReglement modeP = modeEspece();
                if (modeP == null) {
                    getErrorMessage("Impossible de valider le bon provisoire", "Aucun mode de paiement n'a été trouvé; veuillez effectuer votre paramétrage !");
                    return false;
                }
                // Enregistre une pièce de règlement mission pour le reste des frais non encore réglé
                ManagedReglementMission wr = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class);
                if (wr != null) {
                    if (reste > 0) {
                        PieceTresorerie y = new PieceTresorerie();
                        y.setMontant(reste);
                        y.setCaisse(wr.getPieceAvance().getCaisse());
                        y.setMode(new ModeDeReglement(modeP.getId().intValue()));
                        y.setDatePiece(new Date());
                        y.setDatePaiement(new Date());
                        y.setDatePaiementPrevu(new Date());
                        y.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                        y.setMission(bean);
                        y.setDescription("REGLEMENT MISSION N°" + entity.getNumeroMission());
                        wr.createOnePieceCaisse(entity, y, false, true);
                    }

                    //Enregistre une pièce correspondant au total des BP payé et justifier ces bon
                    PieceTresorerie y;
                    YvsComptaCaissePieceMission pcm;
                    for (YvsComptaJustifBonMission p : entity.getBonsProvisoire()) {
                        if (p.getPiece() != null ? p.getPiece().getId() > 0 : false) {
                            if (p.getPiece().getStatutPaiement().equals(Constantes.STATUT_DOC_PAYER)) {
                                y = new PieceTresorerie();
                                y.setMontant(p.getPiece().getMontant());
                                y.setCaisse(new Caisses(p.getPiece().getCaisse().getId(), p.getPiece().getCaisse().getIntitule()));
                                y.setMode(new ModeDeReglement(modeP.getId().intValue()));
                                y.setDatePiece(new Date());
                                y.setDatePaiement(p.getPiece().getDateBon());
                                y.setDatePaiementPrevu(p.getPiece().getDateBon());
                                y.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                                y.setMission(bean);
                                y.setDescription("REGLEMENT MISSION N°" + entity.getNumeroMission() + "BP: " + p.getPiece().getNumero());
                                pcm = wr.createOnePieceCaisse(entity, y, true, false, true, p.getPiece().getCaissier());
                                if (pcm != null ? (pcm.getId() != null ? pcm.getId() > 0 : false) : false) {
                                    ManagedBonProvisoire serviceBp = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
                                    if (serviceBp != null) {
                                        pcm.setMission(entity);
                                        p.getPiece().setStatutJustify(Constantes.ETAT_JUSTIFIE);
                                        p.getPiece().setAuthor(currentUser);
                                        p.getPiece().setDateUpdate(new Date());
                                        dao.update(p.getPiece());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            Logger.getLogger(ManagedMission.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public void motifEtapeOrdre(String motifEtape, boolean lastEtape) {
        this.motifEtape = motifEtape;
        this.lastEtape = lastEtape;
    }

    public void annulEtapeOrdre(YvsWorkflowValidMission etape, boolean lastEtape) {
        this.etape = etape;
        this.lastEtape = lastEtape;
        this.motifEtape = null;
    }

    public boolean annulEtapeOrdre() {
        return annulEtapeOrdre(selectMission, mission, currentUser, etape, lastEtape, motifEtape);
    }

    public boolean annulEtapeOrdre(YvsGrhMissions current, Mission mission, YvsUsersAgence users, YvsWorkflowValidMission etape, boolean lastEtape, String motif) {
        if (!asDroitValideEtape(etape.getEtape(), users.getUsers())) {
            openNotAcces();
        } else {
            //contrôle la cohérence des dates
            if (motif != null ? motif.trim().isEmpty() : true) {
                getErrorMessage("Vous devez précisez le motif");
                return false;
            }
            if (current != null ? (current.getId() != null ? current.getId() < 1 : true) : true) {
                current = (YvsGrhMissions) dao.loadOneByNameQueries("YvsMissions.findById", new String[]{"id"}, new Object[]{mission.getId()});
            }
            if (mission != null ? mission.getId() < 1 : true) {
                mission = UtilGrh.buildBeanMission(current);
            }
            int idx = mission.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                //cas de la validation de la dernière étapes
                if (etape.getEtapeSuivante() != null) {
                    champ = new String[]{"etape", "mission"};
                    val = new Object[]{etape.getEtapeSuivante().getEtape(), current};
                    YvsWorkflowValidMission y = (YvsWorkflowValidMission) dao.loadOneByNameQueries("YvsWorkflowValidMission.findByEtapeMission", champ, val);
                    if (y != null ? (y.getId() > 0 ? y.getEtapeValid() : false) : false) {
                        getErrorMessage("Vous devez respecter l'ordre d'annulation");
                        return false;
                    }
                }
                etape.setAuthor(users);
                etape.setMotif(motif);
                etape.setEtapeValid(false);
                etape.setEtapeActive(true);
                dao.update(etape);

                current.setEtapeValide((current.getEtapeValide() - 1) > 0 ? current.getEtapeValide() - 1 : 0);
                current.setStatutMission(current.getEtapeValide() <= 1 ? Constantes.STATUT_DOC_ATTENTE : Constantes.STATUT_DOC_ENCOUR);
                dao.update(current);
                mission.setStatutMission(current.getStatutMission());
                mission.setEtapeValide(current.getEtapeValide());
                if (listMission != null ? listMission.contains(current) : false) {
                    int idx_ = listMission.indexOf(current);
                    listMission.get(idx_).setEtapeValide(current.getEtapeValide());
                    listMission.get(idx_).setStatutMission(current.getStatutMission());
                    update("data_bon_provisoire");
                }
                getInfoMessage("Annulation effectué avec succès !");
                return true;
            } else {
                getErrorMessage("Impossible de continuer !");
            }
        }
        return false;
    }

    public void annulerValidationBon() {
        if (mission.getId() > 0) {
            if (!mission.getPiecesCaisses().isEmpty()) {
                getErrorMessage("Impossible d'annuler cette ordre de mission", "Une pièce de caisse y a été rattaché !");
            } else {
                selectMission.setDateUpdate(new Date());
                selectMission.setStatutMission(Constantes.STATUT_DOC_ATTENTE);
                selectMission.setAuthor(currentUser);
                dao.update(selectMission);
                mission.setStatutMission(selectMission.getStatutMission());
                int idx = listMission.indexOf(selectMission);
                if (idx >= 0) {
                    listMission.set(idx, selectMission);
                }
            }
        }
    }

//    public void findMissionByNum(String numero) {
//        champ = new String[]{"societe", "numero"};
//        val = new Object[]{currentUser.getAgence().getSociete(), "%" + numero + "%"};
//        nameQueriCount = "YvsGrhMissions.findByNumeroMC";
//        nameQueri = "YvsGrhMissions.findByNumeroM";
//        pagineResult_(true, true);
//    }
    /**
     * Valide la mission et génère une piece de caisse*
     */
    private boolean generetedPc = true;    //active la génération d'une pièce de caisse
    private boolean forceGeneretedPc = true;

    public boolean isGeneretedPc() {
        return generetedPc;
    }

    public void setGeneretedPc(boolean generetedPc) {
        this.generetedPc = generetedPc;
    }

    public void chooseVille() {
        if (mission.getLieu() != null) {
            if (mission.getLieu().getId() > 0) {
                ManagedDico w = (ManagedDico) giveManagedBean("Mdico");
                if (w != null) {
                    int idx = w.getListVille().indexOf(new YvsDictionnaire(mission.getLieu().getId()));
                    if (idx > -1) {
                        YvsDictionnaire y = w.getListVille().get(idx);
                        mission.setLieu(UtilGrh.buildBeanDictionnaire(y));
                    }
                }
            } else if (mission.getLieu().getId() == -1) {
                openDialog("dlgAddVille");
            }
        }
    }

    public void termineValideMission() {
        if (mission.getId() > 0) {
            //valide l'etape
            if (currentEtape != null) {
                validEtapeOrdreMission(currentEtape, true);
            }
            if (generetedPc && forceGeneretedPc) {
                ManagedReglementMission service = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class);
                if (service != null) {
                    YvsGrhMissions mi = buildMission(mission);
                    YvsComptaCaissePieceMission pcm = service.createOnePieceCaisse(mi);
                    if (pcm != null ? (pcm.getId() != null ? pcm.getId() > 0 : false) : false) {
                        service.getPieceAvance().setId(pcm.getId());
                        closeDialog("dlgValidOM");
                        getInfoMessage("La pièce de caisse a été généré avec succès !");
                    }
                }
            }
        }
    }

    public void cloturerOrdreMission() {
        //contrôle les droit
        if (!autoriser("mission_cloturer")) {
            openNotAcces();
            return;
        }
        //toutes les autre étape doivent déjà être validé ou non configuré
        for (YvsWorkflowValidMission e : mission.getEtapesValidations()) {
            if (!e.getEtapeValid()) {
                getErrorMessage("Impossible de clôturer l'ordre de mission ", "Toutes les étapes n'ont pas été validé !");
                return;
            }
        }
        if (mission.getDateFin().after(new Date())) {
            getErrorMessage("La mission est encore en cours. voulez-vous vraiment la clôturer? ");
            return;
        }
        YvsGrhMissions currentMission = (YvsGrhMissions) dao.loadOneByNameQueries("YvsMissions.findById", new String[]{"id"}, new Object[]{mission.getId()});
        currentMission.setAuthor(currentUser);
        currentMission.setStatutMission('C');
        mission.setStatutMission('C');
        currentMission.setCloturer(true);
        dao.update(currentMission);
        succes();
    }

    public void cancelOrdreMission() {
        //vérifie le droit
        if (mission.getStatutMission() == Constantes.STATUT_DOC_CLOTURE) {
            //il fo le droit d'annuler un ordre déjà clôturé
            if (!autoriser("mission_cancel_cloturer")) {
                openNotAcces();
                return;
            }
        } else {
            if (!autoriser("mission_cancel")) {
                openNotAcces();
                return;
            }
        }
        YvsGrhMissions currentMission = (YvsGrhMissions) dao.loadOneByNameQueries("YvsMissions.findById", new String[]{"id"}, new Object[]{mission.getId()});
        if (!canCancelMission(currentMission)) {
            return;
        }
        //annule toute les validation acquise
        int i = 0;
        boolean update = false;
        for (YvsWorkflowValidMission vm : mission.getEtapesValidations()) {
            //si on trouve une étape non valide (on ne peut annuler un ordre de mission complètement valide)
            if (!vm.getEtapeValid()) {
                update = true;
            } else {
                //ais-je un droit de validation pour cet étape?
                if (!asDroitValideEtape(vm.getEtape().getAutorisations(), currentUser.getUsers().getNiveauAcces())) {
                    getErrorMessage("Vous ne pouvez annuler cette mission ! Elle requière un niveau suppérieur");
                    return;
                }
            }
        }
        if (update) {
            for (YvsWorkflowValidMission vm : mission.getEtapesValidations()) {
                vm.setEtapeActive(false);
                if (i == 0) {
                    vm.setEtapeActive(true);
                }
                vm.setAuthor(currentUser);
                vm.setEtapeValid(false);
                dao.update(vm);
                i++;
            }
        } else if (!mission.getEtapesValidations().isEmpty()) {
            getErrorMessage("Vous ne pouvez annuler cet ordre de mission", "Il a déjà parcouru tout le cycle de validation");
            return;
        }
        //désactive la mission        
        currentMission.setAuthor(currentUser);
        currentMission.setDateUpdate(new Date());
        currentMission.setEtapeValide(0);
        currentMission.setStatutMission(Constantes.STATUT_DOC_ANNULE);
        mission.setStatutMission(Constantes.STATUT_DOC_ANNULE);
        dao.update(currentMission);
        listMission.set(listMission.indexOf(currentMission), currentMission);
        succes();
    }

    public void suspendreOrdreMission() {
        //vérifie le droit
        if (mission.getStatutMission() == Constantes.STATUT_DOC_CLOTURE) {
            //il fo le droit d'annuler un ordre déjà clôturé
            if (!autoriser("mission_cancel_cloturer")) {
                openNotAcces();
                return;
            }
        }
        //annule toute les validation acquise
        YvsGrhMissions currentMission = (YvsGrhMissions) dao.loadOneByNameQueries("YvsMissions.findById", new String[]{"id"}, new Object[]{mission.getId()});
        int i = 0;
        if (autoriser("mission_suspendre")) {
            //les pièces de caisses lié à la mission doivent être encore en état impayé
            if (!canCancelMission(currentMission)) {
                return;
            }
            if (mission.getEtapesValidations() != null) {
                for (YvsWorkflowValidMission vm : mission.getEtapesValidations()) {
                    vm.setEtapeActive(false);
                    if (i == 0) {
                        vm.setEtapeActive(true);
                    }
                    vm.setAuthor(currentUser);
                    vm.setEtapeValid(false);
                    dao.update(vm);
                    i++;
                }
            }
        } else {
            openNotAcces();
            return;
        }
        //désactive la mission        
        currentMission.setAuthor(currentUser);
        currentMission.setStatutMission('S');
        mission.setStatutMission('S');
        dao.update(currentMission);
        if (listMission.contains(currentMission)) {
            listMission.get(listMission.indexOf(currentMission)).setStatutMission('S');
        }
        succes();
    }

    private boolean canCancelMission(YvsGrhMissions m) {
        for (YvsComptaCaissePieceMission pm : m.getReglements()) {
            if (pm.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                getErrorMessage("Des pièces de caisses liées à cette mission ont déjà été payé !");
                return false;
            }
        }
        return true;
    }

    public void calculDateRetour() {
        //la date de retour est 
        if (mission.getEmploye().getId() > 0) {
            YvsGrhContratEmps contrat = (YvsGrhContratEmps) dao.loadOneByNameQueries("YvsGrhContratEmps.findByEmploye_", new String[]{"employe"}, new Object[]{new YvsGrhEmployes(mission.getEmploye().getId())});
            if (contrat != null && mission.getDateDebut() != null && mission.getDateFin() != null) {
                //récupère le calendrier de travail de l'employé
                if (contrat.getCalendrier() != null && (mission.getDateDebut().before(mission.getDateFin()) || mission.getDateDebut().equals(mission.getDateFin()))) {
                    //initialise la date de depart en congé
                    Calendar fin = Calendar.getInstance();
                    fin.setTime(mission.getDateFin());
                    fin.add(Calendar.DAY_OF_MONTH, 1);
                    //date de retour
                    //s'il y a un jour férié entre les date de début et de fin différent du dimanche, on ajoute une journée à la date de retour
                    champ = new String[]{"societe", "debut", "fin"};
                    val = new Object[]{currentAgence.getSociete(), mission.getDateDebut(), mission.getDateFin()};
                    List<YvsJoursFeries> l = dao.loadNameQueries("YvsJoursFeries.findByJourFBetweenDates", champ, val);
                    Calendar calJF = Calendar.getInstance();
                    for (YvsJoursFeries jf : l) {
                        calJF.setTime(jf.getJour());
                        if (jf.isAllYear()) {
                            calJF.set(Calendar.YEAR, fin.get(Calendar.YEAR));
                        }
                        boolean continu = ((calJF.getTime().after(mission.getDateDebut()) && calJF.getTime().before(mission.getDateFin())) || calJF.getTime().equals(mission.getDateDebut()) || calJF.getTime().equals(mission.getDateFin()));
                        if (!continu) {
                            continue;
                        }
                        if (!"dimanche".equals(Utilitaire.getDay(fin).trim().toLowerCase())) {
                            fin.add(Calendar.DAY_OF_MONTH, 1);
                        }
                    }
                    //la date de retour prévu est-elle un jour ouvré?     
                    Calendar dateDebut = Calendar.getInstance();
                    dateDebut.setTime(mission.getDateDebut());
                    for (YvsJoursOuvres jo : contrat.getCalendrier().getJoursOuvres()) {
                        if (jo.getJour().toLowerCase().equals(Utilitaire.getDay(dateDebut).trim().toLowerCase()) && !jo.getOuvrable()) {
                            getWarningMessage("Attention,", "Le congé ou permission ne commence pas un jour ouvrable !");
                        }
                    }
                    YvsJoursOuvres dateFin_ = giveJourFin(contrat.getCalendrier().getJoursOuvres(), fin);
                    if (dateFin_ != null) {
                        mission.setDateRetour(dateFin_.getDate_());
                    }
                } else {
                    if (contrat.getCalendrier() == null) {
                        getErrorMessage("Vous devez paramétrer un calendrier de travail pour l'employé !");
                    } else {
                        getErrorMessage("La période de la mission est mal défini !");
                    }
                }
            } else if (contrat == null) {
                getErrorMessage("Impossible de continuer", "aucun contrat n'a été touvé pour cet employé !");
            } else {
                getErrorMessage("La période de la mission est mal défini !");
            }
        } else {
            getErrorMessage("Aucun employé n'a été selectionné !");
        }
    }
//cherche le jour Ouvré ou le prochain jour ouvrée qui correspond à cette date

    private YvsJoursOuvres giveJourFin(List<YvsJoursOuvres> l, Calendar c) {
        YvsJoursOuvres re = null;
        while (re == null && !l.isEmpty()) {
            for (YvsJoursOuvres j : l) {
                j.setDate_(c.getTime());
                if (j.getJour().trim().toLowerCase().equals(Utilitaire.getDay(c).toLowerCase()) && j.getOuvrable()) {
                    re = j;
                    return re;
                }
            }
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        return re;
        //si on arrive à la fin de la boucle, c'est que la journée du calendrier n'est pas un jour Ouvrée        
    }

    private YvsJoursOuvres findDayInCalendrier(YvsGrhContratEmps co, Date date) {
        if ((co != null) ? co.getCalendrier() != null : false) {
            if (date != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                for (YvsJoursOuvres jo : co.getCalendrier().getJoursOuvres()) {
                    if (jo.getJour().trim().toUpperCase().equals(Utilitaire.getDay(cal))) {
                        return jo;
                    }
                }
            }
        } else {
            getErrorMessage("Aucun calendrier n'a été trouvé !");
        }
        return null;
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        ParametreRequete p = new ParametreRequete("(y.etapeValide+1)", "etape", null, "IN", "AND");
        if (toValideLoad) {
            List<Integer> lnum = dao.loadNameQueries("YvsWorkflowAutorisationValidDoc.findOrdreStepe", new String[]{"document", "niveau"}, new Object[]{Constantes.DOCUMENT_MISSION, currentUser.getUsers().getNiveauAcces()});
            if ((lnum != null) ? !lnum.isEmpty() : false) {
                p = new ParametreRequete("(y.etapeValide+1)", "etape", lnum, "IN", "AND");
            }
        }
        paginator.addParam(p);
        addParamInit();
        List<YvsGrhMissions> re = paginator.parcoursDynamicData("YvsGrhMissions", "y", "y.dateDebut DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void navigueInResult(int avancer) {
        currentNombre += avancer;
        if (currentNombre >= 0 && currentNombre <= listMission.size() - 1) {
//            YvsGrhMissions currentMission = (YvsGrhMissions) dao.loadOneByNameQueries("YvsMissions.findById", new String[]{"id"}, new Object[]{listMission.get(currentNombre - 1).getId()});
//            if (currentMission != null) {
            loadMissionOnView(UtilGrh.buildBeanMission(listMission.get(currentNombre)));
//            }
        } else {
            currentNombre = 0;
            navigueInResult(1);
        }
    }

    public void sendMission(YvsGrhMissions m) {
        getInfoMessage(m.getId() + "");
    }

    /**
     * Gestion des mission*
     */
    private Date heureArrive = new Date(), heureDepart = new Date(), dateFin = new Date();

    public Date getHeureArrive() {
        return heureArrive;
    }

    public void setHeureArrive(Date heureArrive) {
        this.heureArrive = heureArrive;
    }

    public Date getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(Date heureDepart) {
        this.heureDepart = heureDepart;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date DateFin) {
        this.dateFin = DateFin;
    }

    private boolean canEditTime() {
        //teste le droit de modification de l'heure
        if (!autoriser("mission_set_time")) {
            openNotAcces();
            return false;
        }
        return true;
    }

    private boolean canReportDateMission(boolean reporter) { //le booleen report précise si l'action est un report à une date ulterieur ou antérieur
        //teste le droit de report
        if (!autoriser("mission_report_date")) {
            openNotAcces();
            return false;
        }
//        if (reporter) {
//            if (mission.getDateFin().after(dateFin)) {
//                getErrorMessage("La nouvelle date de fin doit être ulterieur à la date de fin courante !");
//                return false;
//            }
//        } else if (mission.getDateFin().before(dateFin)) {
//            getErrorMessage("La nouvelle date de fin doit être antérieure à la date de fin courante !");
//            return false;
//        }
        //teste la coherence de la date
        return true;
    }

    public void openHeureEditTime(YvsGrhMissions m, boolean depart) {
        if (m != null) {
            if (canEditTime() && m.getStatutMission() != 'C') {
                loadMissionOnView(UtilGrh.buildBeanMission(m));
                heureDepart = m.getHeureDepart();
                heureArrive = m.getHeureArrive();
                openDialog((depart) ? "dlgSetHeureD" : "dlgSetHeureF");
                update((depart) ? "miss_update_HD" : "miss_update_HF");
            } else if (m.getStatutMission() == 'C') {
                getErrorMessage("Impossible de modifier ", "La mission est terminé depuis le " + dfML.format(mission.getDateFin()));
            }
        } else {
            getWarningMessage("Erreur de la selection !");
        }
    }

    public void editHeureDepartMission(boolean depart) {
        YvsGrhMissions currentMission = (YvsGrhMissions) dao.loadOneByNameQueries("YvsMissions.findById", new String[]{"id"}, new Object[]{mission.getId()});
        currentMission.setAuthor(currentUser);
        if (depart) {
            if (heureDepart != null) {
                currentMission.setHeureDepart(heureDepart);
            } else {
                getErrorMessage("Vous devez indiquer une heure !");
                return;
            }
        } else {
            if (heureArrive != null) {
                currentMission.setHeureArrive(heureArrive);
            } else {
                getErrorMessage("Vous devez indiquer une heure !");
                return;
            }
        }
        currentMission.setDateSave(new Date());
        dao.update(currentMission);
        succes();

    }

    public void openEditDateFin(YvsGrhMissions m, boolean report) {
        if (m != null) {
            loadMissionOnView(UtilGrh.buildBeanMission(m));
            if (canReportDateMission(report)) {
                dateFin = m.getDateFin();
                openDialog((report) ? "dlgReportDate" : "dlgAvanceDate");
                update((report) ? "miss_date_report" : "miss_date_avance");
            } else if (m.getStatutMission() == 'C') {
                getErrorMessage("Impossible de modifier ", "La mission est terminé depuis le " + dfML.format(mission.getDateFin()));
            }
        } else {
            getErrorMessage("Erreur lors de la selection");
        }
    }

    public void openEditFraisMiss(YvsGrhMissions m) {
        if (m != null) {
            loadMissionOnView(UtilGrh.buildBeanMission(m));
            openDialog("dlgCoutMission");
        }
    }

    boolean select;

    public void openEditBonProvisoire(YvsGrhMissions miss, boolean select) {
        if (miss != null) {
            onSelectObject(miss);
            if (onEtapeValidEditBp(ordonneEtapes(miss.getEtapesValidations()))) {
                this.select = select;
                setMontantTotalMission(miss);
                if (miss.getTotalReste() <= 0) {
                    if (selectMission.getBonsProvisoire() != null ? selectMission.getBonsProvisoire().isEmpty() : false) {
                        if (!miss.getFraisMissions().isEmpty()) {
                            getErrorMessage("Cette mission est déja soldée");
                            return;
                        }
                    }
                }
//                selectMission = miss;
                resetBonProvisoire();
                openDialog("dlgListBons");
            } else {
                getErrorMessage("L'edition d'un BP pour un ordre de mission n'est pas possible à cette étape!", "veuillez consulter votre administrateur pour plus d'informations");
            }
        }
    }

    public void loadOnBonProvisoire(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            piece = (YvsComptaBonProvisoire) ev.getObject();
            if (select) {
                /*Construit la pièce de trésorerie*/
                ManagedReglementMission w1 = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class);
                PieceTresorerie pt = new PieceTresorerie();
                pt.setMontant(piece.getMontant());
                pt.setDatePaiementPrevu(piece.getDateBon());
                pt.setDatePiece(piece.getDateBon());
                pt.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
                pt.setCaisse(UtilCompta.buildBeanCaisse(piece.getCaisse()));
                YvsComptaCaissePieceMission pc = w1.createOnePieceCaisse(selectMission, pt, true, true);
                if (pc != null) {
                    addBonProvisoire(piece, pc, true, true);
                }
            }
            select = false;
            update("chmp_montant_dd_mission");
        }
    }

    public void editDateFinMission(int dlg) {
        if (dateFin != null && mission.getMotifReport() != null) {
            YvsGrhMissions currentMission = (YvsGrhMissions) dao.loadOneByNameQueries("YvsMissions.findById", new String[]{"id"}, new Object[]{mission.getId()});
            currentMission.setDateFinPrevu(currentMission.getDateFin());
            currentMission.setDateFin(dateFin);
            currentMission.setDateSave(new Date());
            currentMission.setAuthor(currentUser);
            currentMission.setDateRetour(mission.getDateRetour());
            currentMission.setMotifReport(mission.getMotifReport());
            mission.setDateFin(dateFin);
            dao.update(currentMission);
            succes();
            closeDialog((dlg == 1) ? "dlgReportDate" : "dlgAvanceDate");
        } else {
            getErrorMessage("Formulaire incorrect !");
        }
    }

    public void changeDateFin(SelectEvent ev) {
        dateFin = (Date) ev.getObject();
        mission.setDateFin(dateFin);
        calculDateRetour();
    }

    public void exportOrdreMission() {
        //YvsGrhBulletins b = giveBulletin();
        if (mission.getId() > 0) {
            try {
                if (autoriser("mission_print_report")) {
                    //controle qu'on est au moins à une étape de validation                   
                    if (onEtapeValide(mission.getEtapesValidations())) {
                        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                        Map<String, Object> param_ = new HashMap<>();
                        param_.put("ID", (int) mission.getId());
                        param_.put("AUTEUR", currentUser.getUsers().getNomUsers());
                        param_.put("IMG_LOGO", returnLogo());
                        param_.put("IMG_SIEGE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "siege.png"));
                        param_.put("IMG_PHONE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "phone.png"));
                        param_.put("MONTANT_CHIFFRE", Nombre.CALCULATE.getValue(mission.getTotalFraisMission()));
                        param_.put("IMG_CACHET", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (mission.getStatutMission() == Constantes.STATUT_DOC_VALIDE ? "cachet_approuv.png" : null));
                        param_.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report" + FILE_SEPARATOR + ((true) ? "full" : "semi")) + FILE_SEPARATOR);
                        executeReport("ordre_mission", param_, (int) mission.getId() + "");
                    } else {
                        getErrorMessage("L'ordre ne peut être imprimé pour l'instant !");
                    }
                } else {
                    openNotAcces();
                }
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(ManagedMission.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            getErrorMessage("Impossible d'imprimer, votre selection est ambigue !");
        }
    }

    public void exportOneOrdreMission(YvsGrhMissions m) {
        //YvsGrhBulletins b = giveBulletin();
        if (m != null) {
            try {
                if (autoriser("mission_print_report")) {
                    //controle qu'on est au moins à une étape de validation                    
                    if (onEtapeValide(ordonneEtapes(m.getEtapesValidations()))) {
                        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                        Map<String, Object> param_ = new HashMap<>();
                        param_.put("ID", m.getId().intValue());
                        param_.put("AUTEUR", currentUser.getUsers().getNomUsers());
                        param_.put("IMG_LOGO", returnLogo());
                        param_.put("IMG_SIEGE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "siege.png"));
                        param_.put("IMG_PHONE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "phone.png"));
                        param_.put("MONTANT_CHIFFRE", Nombre.CALCULATE.getValue(giveTotalFraisM(m.getFraisMissions())));
                        param_.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report" + FILE_SEPARATOR + ((true) ? "full" : "semi")) + FILE_SEPARATOR);
                        param_.put("IMG_CACHET", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (m.getStatutMission().equals(Constantes.STATUT_DOC_VALIDE) ? "cachet_approuv.png" : null));
                        executeReport("no_frais_ordre_mission", param_, m.getId() + "");
                    } else {
                        getErrorMessage("L'ordre ne peut être imprimé pour l'instant !");
                    }
                } else {
                    openNotAcces();
                }
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(ManagedMission.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            getErrorMessage("Impossible d'imprimer, votre selection est ambigue !");
        }
    }

    private boolean onEtapeValide(List<YvsWorkflowValidMission> etapes) {
        if (!etapes.isEmpty()) {
            YvsWorkflowValidMission vm = findActualEtape(etapes);
            if (vm != null) {
                return vm.getEtape().getPrintHere();
            }
        } else {
            return true;
        }
        return false;
//        if (!etapes.isEmpty()) {
//            for (YvsWorkflowValidMission vm : etapes) {
//                if (vm.isEtapeActive() && vm.getEtape().getPrintHere()) {
//                    return true;
//                }
//            }
//
//            return false;
//        } else {
//            return true;
//        }
    }

    //l'étape en cours c'est l'étape validé dont l'etape suivante est non validé
    private YvsWorkflowValidMission findActualEtape(List<YvsWorkflowValidMission> etapes) {
        YvsWorkflowValidMission re = null;
        for (int id = 0; id < etapes.size(); id++) {
            if (etapes.get(id).getEtapeValid() && etapes.get(id).getEtape().getEtapeSuivante() != null) {
                if ((etapes.size() >= id + 1) ? !etapes.get(id + 1).getEtapeValid() : true) {
                    return etapes.get(id);
                }
            } else if (etapes.get(id).getEtapeValid() && etapes.get(id).getEtape().getEtapeSuivante() == null) {
                return etapes.get(id);
            }
        }
        return re;
    }

    private boolean onEtapeValidEditBp(List<YvsWorkflowValidMission> etapes) {
        if (!etapes.isEmpty()) {
            YvsWorkflowValidMission vm = findActualEtape(etapes);
            if (vm != null) {
                System.err.println("------ " + vm.getEtape().getTitreEtape());
                return vm.getEtape().getCanEditBpHere();
            }
        }
        return false;
//            for (YvsWorkflowValidMission vm : etapes) {
//                if (vm.getEtapeValid() && vm.getEtape().getEtapeSuivante() != null) {
//                    if (!etapes.get(i + 1).getEtapeValid()) {
//
//                    }
//                }
//                if (vm.isEtapeActive() && vm.getEtape().getCanEditBpHere()) {
//                    return true;
//                }
//                i++;
//            }
//            return false;
//        } else {
//            return true;
//        }
    }
    /*Gerer le clônage d'une mission*/
    private String employeClone;
    private List<YvsGrhEmployes> listEmploye;

    public String getEmployeClone() {
        return employeClone;
    }

    public void setEmployeClone(String employeClone) {
        this.employeClone = employeClone;
    }

    public List<YvsGrhEmployes> getListEmploye() {
        return listEmploye;
    }

    public void setListEmploye(List<YvsGrhEmployes> listEmploye) {
        this.listEmploye = listEmploye;
    }

    public void openDlgToClone() {
        if (mission.getId() > 0) {
            openDialog("dlgConfirmCloneMission");
        }
    }

    public void openDlgToClone(YvsGrhMissions m) {
        if (m != null) {
            loadMissionOnView(UtilGrh.buildBeanMission(m));
            openDialog("dlgConfirmCloneMission");
        }
    }

    public void findOneEmploye(String matricule) {
        if (matricule != null) {
            listEmploye = dao.loadNameQueries("YvsGrhEmployes.findByCodeUsers", new String[]{"codeUsers", "agence"},
                    new Object[]{"%" + matricule + "%", currentUser.getAgence().getSociete()});
            if (!listEmploye.isEmpty()) {
                mission.setEmploye(UtilGrh.buildBeanSimplePartialEmploye(listEmploye.get(0)));
            }
        }
    }

    public void choisirEmploye() {
        if (listEmploye != null) {
            if (listEmploye.size() <= 0) {
                employeClone = null;
                listEmploye.clear();
                getErrorMessage("Votre choix est imprécis !");
            } else {
                if (listEmploye.size() == 1) {
                    mission.setEmploye(UtilGrh.buildBeanSimplePartialEmploye(listEmploye.get(0)));
                } else {
                    getErrorMessage("Votre choix est imprécis !");
                }
            }
        }
    }

    public void cloneMission() {
        if (mission.getId() > 0 && listEmploye != null) {
            if (listEmploye.size() == 1) {
                mission.setId(-1);
                mission.setEmploye(UtilGrh.buildBeanSimplePartialEmploye(listEmploye.get(0)));
                mission.setFraisMissions(new ArrayList<YvsGrhFraisMission>());
                mission.setFraisMission(new GrilleFraisMission());
                saveNew1();
            } else {
                getErrorMessage("Votre selection est ambigue !");
            }
        }
    }

    public int countDayMission(Date begin, Date end) {
        if (begin != null && end != null) {
            Calendar d1 = Calendar.getInstance();
            d1.setTime(begin);
            d1.set(Calendar.HOUR, 0);
            d1.set(Calendar.MINUTE, 0);
            d1.set(Calendar.SECOND, 0);
            d1.set(Calendar.MILLISECOND, 0);
            Calendar d2 = Calendar.getInstance();
            d2.setTime(end);
            d2.set(Calendar.HOUR, 0);
            d2.set(Calendar.MINUTE, 0);
            d2.set(Calendar.SECOND, 0);
            d2.set(Calendar.MILLISECOND, 0);
            //si l'heure de départ est renseigné:
            int precision = 0;
            if (mission.getHeureDepart() != null) {
                //récupère le jour donnée dans le calendrier de l'employé
                if (mission.getEmploye().getId() > 0) {
                    YvsGrhContratEmps co = (YvsGrhContratEmps) dao.loadOneByNameQueries("YvsGrhContratEmps.findByEmploye", new String[]{"employe"}, new Object[]{new YvsGrhEmployes(mission.getEmploye().getId())});
                    YvsJoursOuvres jo = findDayInCalendrier(co, mission.getDateDebut());
                    if (paramGrh == null) {
                        paramGrh = (YvsParametreGrh) dao.loadOneByNameQueries("YvsParametreGrh.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                    }
                    if (jo != null && paramGrh != null) {
                        if (jo.getHeureDebutTravail().before(mission.getHeureDepart()) || jo.getHeureDebutTravail().equals(mission.getHeureDepart())) {
                            double duree = Utilitaire.countHourBetweenDate(jo.getHeureDebutTravail(), mission.getHeureDepart());
                            if (duree >= paramGrh.getHeureMinimaleRequise()) {
                                precision = -1;
                            }
                        }
                    }

                }
            }
            mission.setDureeMission((int) TimeUnit.MILLISECONDS.toDays(d2.getTimeInMillis() - d1.getTimeInMillis()) + 1 + precision);
            return mission.getDureeMission();
        }
        return 0;
    }

    public double giveTotalFraisM(List<YvsGrhFraisMission> l) {
        double r = 0;
        for (YvsGrhFraisMission g : l) {
            r += g.getMontant();
        }
        mission.setTotalFraisMission(r);
        return mission.getTotalFraisMission();
    }

    public void addLieuEscale() {
        if ((lieuEscale != null) ? !lieuEscale.isEmpty() : false) {
            lieuxEscale.add(lieuEscale);
            saveNewLieuxEscale();
        }
    }

    private void saveNewLieuxEscale() {
        if (mission.getId() > 0) {
            //save directement le lieux
            StringBuilder sb = new StringBuilder();
            for (String s : lieuxEscale) {
                sb.append(s).append(";");
            }
            YvsGrhMissions currentMission = (YvsGrhMissions) dao.loadOneByNameQueries("YvsMissions.findById", new String[]{"id"}, new Object[]{mission.getId()});
            currentMission.setLieuEscale(sb.toString());
            dao.update(currentMission);
        }
    }

    public void removeLieuxEscale(String lieu) {
        lieuxEscale.remove(lieu);
        if (mission.getId() > 0) {
            saveNewLieuxEscale();
        }
    }
    /*Gérer les ressources d'une mission*/

    public void resetFiche3() {
        ressource = new MissionRessource();
        article = new Articles();
    }

    public boolean controleFicheRessourceMission(MissionRessource bean) {
        if (bean.getArticle().getId() == 0) {
            getMessage("Vous devez choisir une ressource", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getQuantite() == 0) {
            getMessage("Vous devez entrer une quantité", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    public boolean controleFiche(CentreMission bean) {
        if (bean.getMission() != null ? bean.getMission().getId() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez selectionner la mission");
            return false;
        }
        if (bean.getCentre() != null ? bean.getCentre().getId() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez selectionner le centre analytique");
            return false;
        }
        double taux = bean.getTaux();
        for (YvsComptaCentreMission a : mission.getAnalytiques()) {
            if (!a.getId().equals(bean.getId()) ? a.getCentre().getId().equals(bean.getCentre().getId()) : false) {
                getErrorMessage("Formulaire invalide", "Vous avez deja associé ce centre analytique");
                return false;
            }
            if (!a.getId().equals(bean.getId())) {
                taux += a.getCoefficient();
            }
        }
        if (taux > 100) {
            getErrorMessage("Formulaire invalide", "La repartition analytique ne peut pas etre supérieur à 100%");
            return false;
        }
        return true;
    }

    public boolean saveMissionRessource(YvsBaseArticles art) {
        if (controleFicheRessourceMission(ressource)) {
            if (art.getId() > 0) {
                YvsGrhMissionRessource mr = new YvsGrhMissionRessource();
                mr.setQuantite(ressource.getQuantite());
                mr.setRessource(art);
                if (mission.getId() > 0) {
//                    mr.setMission(currentMission);
                    mr = (YvsGrhMissionRessource) dao.save1(mr);
                } else {
                    mr.setId(-imax++);
                }
                mission.getRessources().add(mr);
            } else {
                getMessage("Veuillez selectionner une ressource", FacesMessage.SEVERITY_ERROR);
            }
        }
        return true;
    }

    public boolean saveAllMissionRessource() {
        if (mission.getId() > 0) {
            for (YvsGrhMissionRessource mr : mission.getRessources()) {
                if (mr.getId() <= 0) {
//                    mr.setMission(currentMission);
                    mr.setId(null);
                    mr = (YvsGrhMissionRessource) dao.save1(mr);
                }
            }
        }
        return true;
    }

    public void deleteMissionRess() {

    }

    /*Zone de recherche des missions*/
    private String matriculeF, objetF, referenceF;
    private long lieuF;
    private Date dateDF, dateF;
    private Character statutF;
    private boolean includeDocCloture;
    private boolean paramDate;

    public String getMatriculeF() {
        return matriculeF;
    }

    public void setMatriculeF(String matriculeF) {
        this.matriculeF = matriculeF;
    }

    public String getObjetF() {
        return objetF;
    }

    public void setObjetF(String objetF) {
        this.objetF = objetF;
    }

    public String getReferenceF() {
        return referenceF;
    }

    public void setReferenceF(String referenceF) {
        this.referenceF = referenceF;
    }

    public long getLieuF() {
        return lieuF;
    }

    public void setLieuF(long lieuF) {
        this.lieuF = lieuF;
    }

    public Date getDateF() {
        return dateF;
    }

    public void setDateF(Date dateF) {
        this.dateF = dateF;
    }

    public Date getDateDF() {
        return dateDF;
    }

    public void setDateDF(Date dateDF) {
        this.dateDF = dateDF;
    }

    public Character getStatutF() {
        return statutF;
    }

    public void setStatutF(Character statutF) {
        this.statutF = statutF;
    }

    public boolean isIncludeDocCloture() {
        return includeDocCloture;
    }

    public void setIncludeDocCloture(boolean includeDocCloture) {
        this.includeDocCloture = includeDocCloture;
    }

    public boolean isParamDate() {
        return paramDate;
    }

    public void setParamDate(boolean paramDate) {
        this.paramDate = paramDate;
    }

//    public void classeOneDocuMission(YvsGrhMissions m, boolean all) {
//        if (m != null) {
//            //la mission doit être validé et terminé au sens des dates
//            if ((m.getStatutMission() == 'V' || m.getStatutMission() == 'C')) {
//                YvsGrhMissionClasse mc = new YvsGrhMissionClasse();
//                mc.setUsers(currentUser.getUsers());
//                mc.setMission(m);
//                dao.save1(mc);
//                missionsClasse.add(m);
//                listMission.remove(m);
//            } else {
//                if (!all) {
//                    getErrorMessage("Vous ne pouvez classer ce document ! il est encore en cours !");
//                }
//            }
//        }
//    }
//
//    public void classeDocuMission() {
//        YvsGrhMissions mi;
//        for (int idx : decomposeSelection(chaineSelectMission)) {
//            mi = listMission.get(idx);
//            classeOneDocuMission(mi, true);
//        }
//    }
//
//    public void deleteMissionClasse(YvsGrhMissions m) {
//        String rq = "DELETE FROM yvs_grh_mission_classe WHERE   mission=? AND users=?";
//        dao.requeteLibre(rq, new Options[]{new Options(m.getId(), 1), new Options(currentUser.getUsers().getId(), 2)});
//        listMission.add(m);
//        missionsClasse.remove(m);
//    }
    /*Recherche dans les missions*/
    public void clearParams() {
        clearParams(true);
    }

    public void clearParams(boolean load) {
        toValideLoad = false;
        idsSearch = "";
        paginator.clear();
        initForm = true;
        if (load) {
            loadAllMission(true);
        }
    }

    public void addParamEmploye() {
        if ((matriculeF != null) ? !matriculeF.isEmpty() : false) {
            //trouve si le matricule entree correspond à un employé
            YvsGrhEmployes e = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByCodeUsersS", new String[]{"codeUsers", "agence"}, new Object[]{"%" + matriculeF + "%", currentUser.getAgence().getSociete()});
            if (e != null) {
                paginator.addParam(new ParametreRequete("employe", "employe", e, "=", "AND"));
            } else {
                getWarningMessage("Aucun employé trouvé avec ce matricule !");
                paginator.addParam(new ParametreRequete("employe"));
            }
        } else {
            paginator.addParam(new ParametreRequete("employe"));
        }
    }

    public void findByEmploye() {
        ParametreRequete p0 = new ParametreRequete(null, "reference", null, " LIKE ", "AND");
        if ((matriculeF != null) ? !matriculeF.isEmpty() : false) {
            p0 = new ParametreRequete(null, "reference", "%" + matriculeF.toUpperCase() + "%", " LIKE ", "AND");
            ParametreRequete p01 = new ParametreRequete("UPPER(y.employe.nom)", "nom", "%" + matriculeF.toUpperCase() + "%", " LIKE ", "OR");
            ParametreRequete p02 = new ParametreRequete("UPPER(y.employe.prenom)", "prenom", "%" + matriculeF.toUpperCase() + "%", " LIKE ", "OR");
            ParametreRequete p03 = new ParametreRequete("UPPER(y.employe.matricule)", "matricule", "%" + matriculeF.toUpperCase() + "%", " LIKE ", "OR");
            ParametreRequete p04 = new ParametreRequete("UPPER(concat(y.employe.nom,' ',y.employe.prenom))", "nomPrenom", "%" + matriculeF.toUpperCase() + "%", " LIKE ", "OR");
            ParametreRequete p05 = new ParametreRequete("UPPER(concat(y.employe.prenom,' ',y.employe.nom))", "prenomNom", "%" + matriculeF.toUpperCase() + "%", " LIKE ", "OR");
            p0.getOtherExpression().add(p01);
            p0.getOtherExpression().add(p02);
            p0.getOtherExpression().add(p03);
            p0.getOtherExpression().add(p04);
            p0.getOtherExpression().add(p05);
        }
        paginator.addParam(p0);
        initForm = true;
        loadAllMission(true);
    }

    public void addParamDate(boolean b) {
        ParametreRequete p = new ParametreRequete("y.dateDebut", "dateDebut", null, " BETWEEN ", "AND");
        if (b) {
            if (dateDF != null && dateF != null) {
                if (dateDF.before(dateF) || dateDF.equals(dateF)) {
                    p.setObjet(dateDF);
                    p.setOtherObjet(dateF);
                }
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllMission(true);
    }

    public void addParamDate1(SelectEvent ev) {
        addParamDate(paramDate);
    }

    public void addParamObjet() {
        ParametreRequete p = new ParametreRequete("y.ordre", "ordre", null, "LIKE", "AND");
        if ((objetF != null) ? !objetF.isEmpty() : false) {
            p.setObjet("%" + objetF + "%");
            paginator.addParam(p);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllMission(true);
    }

    public void addParamStatut() {
        addParamStatut(true);
    }

    public void addParamStatut(boolean load) {
        ParametreRequete p = new ParametreRequete("y.statutMission", "statut", null);
        if (statutF != null ? statutF != ' ' : false) {
            p = new ParametreRequete("y.statutMission", "statut", statutF, egaliteStatut, "AND");
        }
        paginator.addParam(p);
        if (load) {
            initForm = true;
            loadAllMission(true);
        }
    }

    public void addParamSatatut(ValueChangeEvent ev) {
        statutF = (Character) ev.getNewValue();
        addParamStatut();
    }

    public void addParamLieu(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.lieu", "lieu", null, "=", "AND");
        if (ev != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                p.setObjet(new YvsDictionnaire(id));
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllMission(true);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.referenceMission", "referenceMission", null, "LIKE", "AND");
        if ((referenceF != null) ? !referenceF.isEmpty() : false) {
            p = new ParametreRequete(null, "referenceMission", "%" + referenceF + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.referenceMission", "referenceMission", "%" + referenceF + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.numeroMission", "referenceMission", "%" + referenceF + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        initForm = true;
        loadAllMission(true);
    }

    public void addParamDuree() {
        if (statutF > 0) {
            ParametreRequete p = new ParametreRequete("dureeMission", "dureeMission", "%" + statutF + "%", "=", "AND");
            paginator.addParam(p);
        } else {
            paginator.addParam(new ParametreRequete("dureeMission"));
        }
    }

    public void addParamAgence(ValueChangeEvent ev) {
        if (ev != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                YvsAgences ag = new YvsAgences((long) ev.getNewValue());
                ParametreRequete p = new ParametreRequete("employe.agence", "agence", ag);
                p.setOperation("=");
                p.setPredicat("AND");
                paginator.addParam(p);
            }
        } else {
            paginator.addParam(new ParametreRequete("agence"));
        }
    }

    public void addParamSatatutC(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.statutMission", "statutMission", null, "=", "AND");
        boolean b = (boolean) ev.getNewValue();
        includeDocCloture = true;
        if (!b) {
            p.setObjet(Constantes.STATUT_DOC_CLOTURE);
            p.setOperation("!=");
        } else {
            p.setOperation("=");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllMission(true);
    }

    public void addParamIds() {
        addParamIds(true);
        loadAllMission(true);
    }

    public void addParamDepartements() {
        ParametreRequete p = new ParametreRequete("employe.posteActif.departement.id", "departements", listIdSubDepartement);
        p.setOperation(" IN ");
        p.setPredicat("AND");
        paginator.addParam(p);
    }

    public void addParamSociete() {
        ParametreRequete p = new ParametreRequete("employe.agence.societe", "societe", currentUser.getAgence().getSociete());
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
    }

//    private boolean addParamDroit(int plage) {
//        ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
//        int numdroit = -1;
//        if (service != null) {
//            numdroit = service.giveNumDroitAccesDossierEmp();
//        }
//        switch (numdroit) {
//            case 0:
//                //droit sur tous les dossiers de la société
//                addParamSociete();
//                break;
//            case 1:
//                // doit sur les dossier de son agence selement
//                addParamAgence(new ValueChangeEvent(null, (long) 0, currentUser.getAgence().getId()));
//                break;
//            case 2:
//                //droit sur les dossiers de même du département
//                if (currentUser.getUsers().getEmploye() != null) {
//                    if (currentUser.getUsers().getEmploye().getPosteActif() != null) {
//                        if (plage == 0 || plage == -1) {
//                            listIdSubDepartement.clear();
//                            giveAllSupDepartement(currentUser.getUsers().getEmploye().getPosteActif().getDepartement());
//                            if (!listIdSubDepartement.isEmpty()) {
//                                addParamDepartements();
//                            } else {
//                                return false;
//                            }
//                        }
//                    }
//                }
//                break;
//            default:
//                return false;
//        }
//        return true;
//    }
    //ici la requete est basé sur les nameQuery
    public void pagineResult_(boolean avancer) {
        initForm = false;
        loadAllMission(avancer);
    }

    /**
     * Notification
     *
     *
     * @param mi
     * @return
     */
    public YvsGrhMissions setMontantTotalMission(YvsGrhMissions mi) {
        setMontantTotalMission(mi, mission);
        return mi;
    }

    private double getTotalFrais(YvsGrhMissions mi) {
        double r = 0;
        for (YvsGrhFraisMission f : mi.getFraisMissions()) {
            r += f.getMontant();
        }
        return r;
    }

    public void onLoadMissionProjet(YvsGrhMissions y) {
        selectMission = y;
        Double montant = (Double) dao.loadObjectByNameQueries("YvsGrhFraisMission.sumByMission", new String[]{"mission"}, new Object[]{y});
        selectMission.setTotalFraisMission(montant != null ? montant : 0d);
        chooseProjet();
    }

    public void chooseProjet() {
        try {
            if (selectMission != null ? selectMission.getId() > 0 : false) {
                nameQueri = "YvsProjProjetMissions.findByMission";
                champ = new String[]{"contenu"};
                val = new Object[]{selectMission};

                String query = "SELECT p.id, p.code, p.libelle, d.id, d.code_departement, d.intitule, s.id, y.id FROM yvs_proj_projet p INNER JOIN yvs_proj_projet_service y ON y.projet = p.id "
                        + " INNER JOIN yvs_proj_departement s ON y.service = s.id INNER JOIN yvs_base_departement d ON s.service = d.id"
                        + " LEFT JOIN yvs_proj_projet_missions c ON (c.projet = y.id AND c.mission = ?) WHERE c.id IS NULL AND p.societe = ?";

                if (projet > 0) {
                    nameQueri = "YvsProjProjetMissions.findByProjetMission";
                    champ = new String[]{"mission", "projet"};
                    val = new Object[]{selectMission, new YvsProjProjet(projet)};
                    query += " AND p.id = " + projet;
                }
                selectMission.setProjets(dao.loadNameQueries(nameQueri, champ, val));

                List<Object[]> result = dao.loadListBySqlQuery(query, new Options[]{new Options(selectMission.getId(), 1), new Options(currentAgence.getSociete().getId(), 2)});
                YvsProjProjet p;
                YvsBaseDepartement d;
                YvsProjDepartement s;
                YvsProjProjetService y;
                YvsProjProjetMissions c;
                for (Object[] data : result) {
                    p = new YvsProjProjet((Long) data[0]);
                    p.setCode((String) data[1]);
                    p.setLibelle((String) data[2]);

                    d = new YvsBaseDepartement((Long) data[3]);
                    d.setCodeDepartement((String) data[4]);
                    d.setIntitule((String) data[5]);

                    s = new YvsProjDepartement((Long) data[6]);
                    s.setService(d);

                    y = new YvsProjProjetService((Long) data[7]);
                    y.setProjet(p);
                    y.setService(s);

                    c = new YvsProjProjetMissions(YvsProjProjetMissions.ids--);
                    c.setAuthor(currentUser);
                    c.setMission(selectMission);
                    c.setProjet(y);

                    selectMission.getProjets().add(c);
                }
                if (projet > 0) {
                    query = "SELECT p.id, p.code, p.libelle, d.id, d.code_departement, d.intitule, s.id FROM yvs_proj_projet p, yvs_proj_departement s INNER JOIN yvs_base_departement d ON s.service = d.id"
                            + " LEFT JOIN yvs_proj_projet_service y ON (y.service = s.id AND y.projet = ?) WHERE y.id IS NULL AND p.id = ? AND d.societe = ?";
                    result = dao.loadListBySqlQuery(query, new Options[]{new Options(projet, 1), new Options(projet, 2), new Options(currentAgence.getSociete().getId(), 3)});
                    for (Object[] data : result) {
                        p = new YvsProjProjet((Long) data[0]);
                        p.setCode((String) data[1]);
                        p.setLibelle((String) data[2]);

                        d = new YvsBaseDepartement((Long) data[3]);
                        d.setCodeDepartement((String) data[4]);
                        d.setIntitule((String) data[5]);

                        s = new YvsProjDepartement((Long) data[6]);
                        s.setService(d);

                        y = new YvsProjProjetService(YvsProjProjetService.ids--);
                        y.setAuthor(currentUser);
                        y.setProjet(p);
                        y.setService(s);

                        c = new YvsProjProjetMissions(YvsProjProjetMissions.ids--);
                        c.setAuthor(currentUser);
                        c.setMission(selectMission);
                        c.setProjet(y);

                        selectMission.getProjets().add(c);
                    }
                }
                update("data-mission_projet");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cellEditProjet(CellEditEvent ev) {
        try {
            if (ev != null ? ev.getRowIndex() > -1 : false) {
                int index = ev.getRowIndex();
                YvsProjProjetMissions y = selectMission.getProjets().get(index);
                if ((Double) ev.getNewValue() <= 0) {
                    if (y.getId() > 0) {
                        dao.delete(y);
                        y.setId(YvsProjProjetMissions.ids--);
                    }
                } else {
                    Double montant = (Double) dao.loadObjectByNameQueries("YvsProjProjetMissions.sumByMissionNotId", new String[]{"mission", "id"}, new Object[]{selectMission, y.getId()});
                    if (((montant != null ? montant : 0) + (Double) ev.getNewValue()) > selectMission.getTotalFraisMission()) {
                        y.setMontant((Double) ev.getOldValue());
                        if (index > -1) {
                            selectMission.getProjets().set(index, y);
                        }
                        update("data-mission_projet");
                        getErrorMessage("Vous ne pouvez pas exceder le montant des frais de mission");
                        return;
                    }
                    if (y.getProjet().getId() < 1) {
                        y.getProjet().setId(null);
                        y.setProjet((YvsProjProjetService) dao.save1(y.getProjet()));
                    }
                    y.setMontant((Double) ev.getNewValue());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    if (y.getId() > 0) {
                        dao.update(y);
                    } else {
                        y.setId(null);
                        y = (YvsProjProjetMissions) dao.save1(y);
                    }
                }
                if (index > -1) {
                    selectMission.getProjets().set(index, y);
                }
                succes();
                update("data-mission_projet");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}
