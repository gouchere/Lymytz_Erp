/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import yvs.grh.bean.mission.ManagedMission;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.grh.activite.YvsGrhCongeEmps;
import yvs.entity.grh.param.YvsCalendrier;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidConge;
import yvs.entity.print.YvsPrintContratEmployeHeader;
import yvs.entity.print.YvsPrintDecisionCongeHeader;
import yvs.entity.users.YvsUsersAgence;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.statistique.ContentDuree;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.mutuelle.ManagedExercice;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Util;
import yvs.util.Utilitaire;
import yvs.util.enume.Nombre;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class ManagedConges extends Managed<Conges, YvsGrhCongeEmps> implements Serializable {

    @ManagedProperty(value = "#{conges}")
    private Conges conges;
//    private YvsGrhCongeEmps congeEmps;
    private YvsGrhCongeEmps currentConge;
    private boolean updateConge; //permet de contrôler la modification d'un congé
//    private boolean compteJourRepos = false;
    private ScheduleModel eventModel;
//    private boolean displayCongeAllSociete = true;
    private String chaineSelect;
    private long congeAcloture;
    private boolean displayBtnCloture;
    private boolean displayId, toValideLoad = true;

    private int currentNombre = 0;
    //compte le congé validé dont la date de fin est passé et le statu n'est pas clôturé
    private long idExoSelect;
    private YvsBaseExercice limitExercice;

    private String motifEtape;
    YvsWorkflowValidConge etape;
    private boolean lastEtape;

    //Paramètre de la durée des congés
    private int dureeP, dureeSup, dureePerm;
    private List<String> listeEffect;
    private List<YvsGrhCongeEmps> listConges;
    private List<Character> statuts;

    private Date dateDebutCorrection = new Date(), dateFinCorrection = new Date();

    public ManagedConges() {
        limitExercice = new YvsBaseExercice();
        listeEffect = new ArrayList<>();
        eventModel = new DefaultScheduleModel();
        statuts = new ArrayList<>();
        listeEffect.add(Constantes.GRH_PERMISSION_SUR_CONGE);
        listeEffect.add(Constantes.GRH_PERMISSION_SUR_SALAIRE);
        listeEffect.add(Constantes.GRH_PERMISSION_AUTORISE);
        listeEffect.add(Constantes.GRH_PERMISSION_SPECIALE);
        listConges = new ArrayList<>();
//        listRecapConge = new ArrayList<>();
    }

    public Date getDateDebutCorrection() {
        return dateDebutCorrection;
    }

    public void setDateDebutCorrection(Date dateDebutCorrection) {
        this.dateDebutCorrection = dateDebutCorrection;
    }

    public Date getDateFinCorrection() {
        return dateFinCorrection;
    }

    public void setDateFinCorrection(Date dateFinCorrection) {
        this.dateFinCorrection = dateFinCorrection;
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

    public List<Character> getStatuts() {
        return statuts;
    }

    public void setStatuts(List<Character> statuts) {
        this.statuts = statuts;
    }

    public boolean isToValideLoad() {
        return toValideLoad;
    }

    public void setToValideLoad(boolean toValideLoad) {
        this.toValideLoad = toValideLoad;
    }

    public boolean isUpdateConge() {
        return updateConge;
    }

    public void setUpdateConge(boolean updateConge) {
        this.updateConge = updateConge;
    }

    public YvsGrhCongeEmps getCurrentConge() {
        return currentConge;
    }

    public void setCurrentConge(YvsGrhCongeEmps currentConge) {
        this.currentConge = currentConge;
    }

    public boolean isDisplayId() {
        return displayId;
    }

    public void setDisplayId(boolean displayId) {
        this.displayId = displayId;
    }

    public int getDureeP() {
        return dureeP;
    }

    public void setDureeP(int dureeP) {
        this.dureeP = dureeP;
    }

    public int getDureeSup() {
        return dureeSup;
    }

    public void setDureeSup(int dureeSup) {
        this.dureeSup = dureeSup;
    }

    public int getDureePerm() {
        return dureePerm;
    }

    public void setDureePerm(int dureePerm) {
        this.dureePerm = dureePerm;
    }

    public long getIdExoSelect() {
        return idExoSelect;
    }

    public void setIdExoSelect(long idExoSelect) {
        this.idExoSelect = idExoSelect;
    }

    public int getCurrentNombre() {
        return currentNombre;
    }

    public void setCurrentNombre(int currentNombre) {
        this.currentNombre = currentNombre;
    }

    public boolean isDisplayBtnCloture() {
        return displayBtnCloture;
    }

    public void setDisplayBtnCloture(boolean displayBtnCloture) {
        this.displayBtnCloture = displayBtnCloture;
    }

    public String getChaineSelect() {
        return chaineSelect;
    }

    public void setChaineSelect(String chaineSelect) {
        this.chaineSelect = chaineSelect;
    }

    public String getMatriculeF() {
        return matriculeF;
    }

    public void setMatriculeF(String matriculeF) {
        this.matriculeF = matriculeF;
    }

    public long getIdParamAgence() {
        return idParamAgence;
    }

    public void setIdParamAgence(long idParamAgence) {
        this.idParamAgence = idParamAgence;
    }

    public String getAttributDate() {
        return attributDate;
    }

    public void setAttributDate(String attributDate) {
        this.attributDate = attributDate;
    }

    public String getFilterStatut() {
        return filterStatut;
    }

    public void setFilterStatut(String filterStatut) {
        this.filterStatut = filterStatut;
    }

    public Character getFilterNature() {
        return filterNature;
    }

    public void setFilterNature(Character filterNature) {
        this.filterNature = filterNature;
    }

    public int getFiltre() {
        return filtre;
    }

    public void setFiltre(int filtre) {
        this.filtre = filtre;
    }

    public List<YvsGrhCongeEmps> getListConges() {
        return listConges;
    }

    public void setListConges(List<YvsGrhCongeEmps> listConges) {
        this.listConges = listConges;
    }

    public List<String> getListeEffect() {
        return listeEffect;
    }

    public void setListeEffect(List<String> listeEffect) {
        this.listeEffect = listeEffect;
    }

    public Conges getConges() {
        return conges;
    }

    public void setConges(Conges conges) {
        this.conges = conges;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public Date getDateFind() {
        return dateFind;
    }

    public void setDateFind(Date dateFind) {
        this.dateFind = dateFind;
    }

    public Date getDateFind1() {
        return dateFind1;
    }

    public void setDateFind1(Date dateFind1) {
        this.dateFind1 = dateFind1;
    }

    public long getCongeAcloture() {
        return congeAcloture;
    }

    public void setCongeAcloture(long congeAcloture) {
        this.congeAcloture = congeAcloture;
    }

    public YvsBaseExercice getLimitExercice() {
        return limitExercice;
    }

    public void setLimitExercice(YvsBaseExercice limitExercice) {
        this.limitExercice = limitExercice;
    }

    @Override
    public void resetFiche() {
        resetFiche(conges);
        conges.setTypeDureePermission('L');
        conges.setNature('C');
        conges.setStatut(Constantes.STATUT_DOC_ATTENTE);
        conges.setTitle(null);
        conges.setEmploye(new Employe());
        conges.setStartDate(new Date());
        update("etapes_valide_conges");
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public boolean isPermissionCD(Conges c) {
        return (c.getNature() == 'P' && c.getTypeDureePermission() == 'C');
    }

    @Override
    public boolean controleFiche(Conges bean) {
        //contrôle s'il exite déjà un congé pour l'employé dans le même intervalle. de plus, on ne peut cumuler deux congé
        if (conges.getEmploye().getId() > 0) {
            List<YvsGrhCongeEmps> l = dao.loadNameQueries("YvsGrhCongeEmps.findTrue", new String[]{"employe", "debut", "fin"}, new Object[]{conges.getEmploye().getId(), conges.getStartDate(), conges.getEndDate()});
            for (YvsGrhCongeEmps c_ : l) {
                if (c_.getId() != bean.getIdConge()) {
                    getMessage("Il y a chevauchement entre les périodes de congés de cet employé !", FacesMessage.SEVERITY_ERROR);
                    openDialog("dlgnotsave");
                    return false;
                }
            }
        } else {
            getMessage("Vous devez selectionner un employé !", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (conges.getStartDate() == null || conges.getEndDate() == null) {
            getErrorMessage("Dates incorrect !");
            return false;
        } else {
            if (conges.getStartDate().after(conges.getEndDate())) {
                getErrorMessage("l'intervalle de date est incohérent !");
                return false;
            }
        }
        if (conges.getDureeAbsence() <= 0) {
            getErrorMessage("Vous n'avez pas indiquer la durée d'absence !");
            return false;
        }
        if (conges.getNature() == 'P' && conges.getTypeDureePermission() == 'C') {
            if (conges.getHeureDebut().equals(conges.getHeureFin())) {
                getErrorMessage("L'heure de depart ne doit pas être identique à l'heure de fin!");
                return false;
            }
        }
        Date date = conges.getStartDate() != null ? conges.getStartDate() : new Date();
        if ((currentConge != null ? (currentConge.getDateDebut() != null ? !currentConge.getDateDebut().equals(date) : false) : false) || (conges.getReferenceConge() == null || conges.getReferenceConge().trim().length() < 1)) {
            String ref = genererReference(Constantes.TYPE_CGE_NAME, date, conges.getEmploye().getId());
            conges.setReferenceConge(ref);
            if (ref == null || ref.trim().equals("")) {
                return false;
            }
        }
        return true;
    }

//    public int controleFiche() {
//        congeEmps = null;
//        //contrôle s'il exite djà un congé pour l'employé dans le même intervalle. de plus, on ne peut cumuler deux congé
//        if (conges.getEmploye().getId() > 0) {
//            List<YvsGrhCongeEmps> l = dao.loadListTableByNameQueries("YvsGrhCongeEmps.findTrue", new String[]{"employe", "debut", "fin"}, new Object[]{conges.getEmploye().getId(), conges.getStartDate(), conges.getEndDate()});
//            /**
//             * *si la liste retournée contient 1 seul élément, on considère
//             * qu'il s'agit de la ligne même qu'on veut modifier si il y a plus
//             * d'un élément, c'est qu'il y a chevauchement avec d'autre congé,
//             * on annulle directement
//             */
//            if (!l.isEmpty()) {
//                if (l.size() == 1) {
//                    congeEmps = l.get(0);
//                } else {
//                    return l.size();
//                }
//            }
//        } else {
//            getErrorMessage("Vous devez selectionner un employé !");
//            return 0;
//        }
//        return 0;
//    }
    @Override
    public void updateBean() {
        if (conges.getEmploye().getId() != 0) {
            champ = new String[]{"employe", "debut", "fin"};
            val = new Object[]{conges.getEmploye().getId(), conges.getStartDate(), conges.getEndDate()};
            YvsGrhCongeEmps cong_ = (YvsGrhCongeEmps) dao.loadOneByNameQueries("YvsGrhCongeEmps.findTrue", champ, val);
            if (cong_ != null) {
                if (cong_.getId() != conges.getIdConge()) {
                    getErrorMessage("Il y a chevauchement entre les périodes de congés de cet employé !");
                    return;
                }
            }
            currentConge = buildConge();
            dao.update(currentConge);
            int idx = listConges.indexOf(currentConge);
            if (idx >= 0) {
                listConges.set(idx, currentConge);
                update("table_listConge");
            }
            eventModel.updateEvent(conges);
            succes();

        }
    }

    @Override
    public Conges recopieView() {
        return new Conges(conges);
    }

    private void applyDureeConge(YvsBaseExercice exo, Date date) {
        if (conges.getEmploye().getId() > 0 && date != null) {
//            exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findById", new String[]{"id"}, new Object[]{exo != null ? exo.getId() : 0});
            Options[] param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(0, 2), new Options(0, 3), new Options(conges.getEmploye().getId(), 4), new Options((exo != null) ? exo.getDateDebut() : new Date(), 5), new Options(date, 6)};
            List<Object[]> durees = dao.loadListBySqlQuery("select agence, employe, valeur, element from grh_conges_employes(?, ?, ?, ?, ?, ?) order by element", param);
            List<ContentDuree> list = new ArrayList<>();
            for (Object[] data : durees) {
                list.add(new ContentDuree(data));
            }
            conges.setCongePrincipalDu((int) valueContentDuree(list, conges.getEmploye().getId(), ContentDuree.CONGE_PRINCIPAL));
            conges.setCongePrincipalPris((int) valueContentDuree(list, conges.getEmploye().getId(), ContentDuree.CONGE_ANNUEL_PRIS));
            conges.setCongeSupp((int) valueContentDuree(list, conges.getEmploye().getId(), ContentDuree.CONGE_SUPPLEMENTAIRE));
            conges.setDureePermissionAutorise((int) valueContentDuree(list, conges.getEmploye().getId(), ContentDuree.PERMISSION_DU));
            conges.setDureePermissionAutorisePris((int) valueContentDuree(list, conges.getEmploye().getId(), ContentDuree.PERMISSION_LONG_AUTORISE));
            conges.setDureePermPrisSpeciale((int) valueContentDuree(list, conges.getEmploye().getId(), ContentDuree.PERMISSION_LONG_SPECIAL));
            conges.setDureePermCD_AN((int) valueContentDuree(list, conges.getEmploye().getId(), ContentDuree.PERMISSION_COURT_ANNUEL));
            conges.setDureePermCD_AU((int) valueContentDuree(list, conges.getEmploye().getId(), ContentDuree.PERMISSION_COURT_AUTORISE));
            conges.setDureePermCD_SAL((int) valueContentDuree(list, conges.getEmploye().getId(), ContentDuree.PERMISSION_COURT_SALAIRE));
            conges.setDureePermCD_SP((int) valueContentDuree(list, conges.getEmploye().getId(), ContentDuree.PERMISSION_COURT_SPECIAL));
            conges.setDureePermCD(conges.getDureePermCD_AN() + conges.getDureePermCD_AU() + conges.getDureePermCD_SAL() + conges.getDureePermCD_SP());
        } else {
            //getErrorMessage("Aucun employé n'a été trouvé !");
        }
    }

    public void changeExercice(ValueChangeEvent ev) {
        if (ev != null) {
            long idExo = (long) ev.getNewValue();
            ManagedExercice service = (ManagedExercice) giveManagedBean(ManagedExercice.class);
            if (service != null) {
                int idx = service.getExercices().indexOf(new YvsBaseExercice(idExo));
                if (idx >= 0) {
                    YvsBaseExercice exo = service.getExercices().get(idx);
                    applyDureeConge(exo, conges.getStartDate());
                }
            }
        }
    }

    private double getDureeCongeRestant() {
        return conges.getCongePrincipalDu() + conges.getCongeSupp()
                - conges.getCongePrincipalPris() - conges.getDureePermPrisSpeciale();
    }

    @Override
    public void populateView(Conges bean) {
        cloneObject(conges, bean);
        conges.setDureeAbsence(bean.getDureeAbsence());
        conges.setStartDate(bean.getStartDate());
        conges.setEndDate(bean.getEndDate());
        conges.setDateRetourConge(bean.getDateRetourConge());
        conges.setTitle(bean.getTitle());
        conges.setDescription(bean.getDescription());
        conges.setEtapesValidations(ordonneEtapes(bean.getEtapesValidations()));
        updateConge = true;
        ManagedExercice service = (ManagedExercice) giveManagedBean(ManagedExercice.class);
        if (service != null) {
            int idx = service.getExercices().indexOf(new YvsBaseExercice(idExoSelect));
            if (idx >= 0) {
                YvsBaseExercice exo = service.getExercices().get(idx);
                applyDureeConge(exo, conges.getStartDate());
            } else {
                applyDureeConge(giveExerciceActif(), conges.getStartDate());
            }
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     */
    @Override
    public void loadAll() {
        loadInfosWarning(false);
        if (!Util.asString(filterStatut)) {
            this.egaliteStatut = "!=";
            this.filterStatut = Constantes.ETAT_CLOTURE;
            addParamStatuts(false);
        }
        if (isWarning != null ? isWarning : false) {
            loadByWarning();
        } else {
            addParamToValide();
        }
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        if (paramGrh == null) {
            paramGrh = (YvsParametreGrh) dao.loadOneByNameQueries("YvsParametreGrh.findAll", champ, val);
        }
        //positionne l'exercice  en fonction de la durée de cumul
        if (paramGrh != null ? paramGrh.getDureeCumuleConge() > 0 : false) {
            Date d = yvs.dao.salaire.service.Constantes.givePrevOrNextDate((conges.getStartDate() != null) ? conges.getStartDate() : new Date(), Calendar.MONTH, -paramGrh.getDureeCumuleConge() * 12);
            if (d != null) {
                limitExercice = getExerciceActif(d);
                idExoSelect = (limitExercice != null) ? limitExercice.getId() : 0;
            }
        }
    }

    private void loadByWarning() {
        paginator.clear();
        loadInfosWarning(true);
        addParamIds(true);
        loadAllConge(true, true);
    }

    public void addParamIds() {
        addParamIds(true);
        loadAllConge(true, true);
    }

    public void addParamToValide() {
        ParametreRequete p = new ParametreRequete("(y.etapeValide+1)", "etape", null, "IN", "AND");
        if (toValideLoad) {
            List<Integer> lnum = dao.loadNameQueries("YvsWorkflowAutorisationValidDoc.findOrdreStepe", new String[]{"document", "niveau"}, new Object[]{Constantes.DOCUMENT_CONGES, currentUser.getUsers().getNiveauAcces()});
            if ((lnum != null) ? !lnum.isEmpty() : false) {
                p = new ParametreRequete("(y.etapeValide+1)", "etape", lnum, "IN", "AND");
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllConge(true, true);
    }

    public void loadCongeAcloturer() {
        paginator.getParams().clear();
        ParametreRequete p = new ParametreRequete("y.dateFin", "dateFin", new Date(), ">", "AND");
        paginator.addParam(p);
        p = new ParametreRequete("y.statut", "statut", Constantes.STATUT_DOC_VALIDE, "=", "AND");
        paginator.addParam(p);
        p = new ParametreRequete("y.employe.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
        paginator.addParam(p);
        p = new ParametreRequete("y.actif", "actif", true, "=", "AND");
        paginator.addParam(p);
        loadAllConge(true, true);
        displayBtnCloture = !listConges.isEmpty();
    }

    public void resetFilterRequete() {
        toValideLoad = false;
        paginator.getParams().clear();
        loadAllConge(true, true);
    }

    public void cloturerCongeTermine() {
        if (!autoriser("conge_cloturer")) {
            openNotAcces();
            return;
        }
        List<YvsGrhCongeEmps> l = dao.loadNameQueries("YvsGrhCongeEmps.FindACloture", new String[]{"date", "statut", "societe"}, new Object[]{new Date(), 'V', currentUser.getAgence().getSociete()});
        for (YvsGrhCongeEmps ce : l) {
            clotureConge(ce);
        }
        succes();
    }

    public void navigueInResult(int avancer) {
        currentNombre += avancer;
        if (currentNombre >= 0 && currentNombre < listConges.size()) {
            currentConge = (YvsGrhCongeEmps) dao.loadOneByNameQueries("YvsGrhCongeEmps.findById", new String[]{"id"}, new Object[]{listConges.get(currentNombre - 1).getId(), 0, 1});
            if (currentConge != null) {
                populateView(UtilGrh.buildBeanCongeEmps(currentConge));
            }
        } else {
            currentNombre = 0;
            navigueInResult(1);
        }
        update("part-conge-btn");
        update("all-view-conge-emps");
    }
    private boolean initForm = true; //permet d'empêcher qu'au rafraichissement de la page, la requête en cours soit modifié

    public void loadAllConge(boolean all, boolean avancer) {
        ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
        if (service != null) {
            ParametreRequete p;
            switch (service.giveNumDroitAccesDossierEmp()) {
                case 0: //voir le dossier de tous les employés de la société                    
                    p = new ParametreRequete("y.employe.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
                    paginator.addParam(p);
                    break;
                case 1://voir les dossiers des employés de l'agence de connexion seulement                   
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
        listConges = paginator.executeDynamicQuery("y", "y", "YvsGrhCongeEmps y JOIN FETCH y.employe ", "y.dateDebut DESC", avancer, initForm, (int) imax, dao);
        eventModel.clear();
        for (YvsGrhCongeEmps d : listConges) {
            Conges c = UtilGrh.buildBeanCongeEmps(d);
            eventModel.addEvent(c);
        }
        update("part-conge-btn");
        update("form_main_conge");
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsGrhCongeEmps> re = paginator.parcoursDynamicData("YvsGrhCongeEmps", "y", "y.dateDebut DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void navigueInConge(boolean avance) {
        initForm = false;
        loadAllConge(true, avance);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllConge(true, true);
    }

    public void changeMaxResult(ValueChangeEvent ev) {
        imax = (long) ev.getNewValue();
        loadAllConge(true, false);
    }

//    int offsetEmps = 0;
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

    public void findOneEmploye(String matricule) {
        if (matricule != null) {
            ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
            if (service != null) {
                service.findEmploye(matricule);
                if (service.getListEmployes().size() == 1) {
                    choixEmploye1(service.getListEmployes().get(0));
                    conges.getEmploye().setError(false);
                } else if (!service.getListEmployes().isEmpty()) {
                    openDialog("dlgEmploye");
                    update("tabEmployes-conge");
                    conges.getEmploye().setError(false);
                } else {
                    conges.getEmploye().setError(true);
                }
            }
        }
    }

    public void findEmployeByUser(ValueChangeEvent ev) {
        if (ev != null) {
            if ((Boolean) ev.getNewValue()) {
                YvsGrhEmployes e = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByUsers", new String[]{"user"}, new Object[]{currentUser.getUsers()});
                if (e != null) {
                    choixEmploye1(e);
                } else {
                    getErrorMessage("Votre profil n'est rttaché à aucun employé !");
                }
            }
        }
    }

    public void choixEmploye(SelectEvent ev) {
        if (ev != null) {
            choixEmploye1((YvsGrhEmployes) ev.getObject());
        }
    }

    public void choixEmploye1(YvsGrhEmployes ev) {
        if (conges.getIdConge() > 0 && currentConge.getEmploye() != null) {
            if (!currentConge.getEmploye().equals(ev)) {
                resetFiche();
            }
        }
        if (conges.getStartDate() == null) {
            conges.setStartDate(new Date());
        }
        conges.setEmploye(UtilGrh.buildBeanPartialEmploye(ev));
        applyDureeConge((idExoSelect > 0) ? new YvsBaseExercice(idExoSelect) : giveExerciceActif(), conges.getStartDate());
        calculDateRetour();
    }

    public boolean saveNew(boolean reset) {
        if (controleFiche(conges)) {
            if (paramGrh == null) {
                paramGrh = (YvsParametreGrh) dao.loadOneByNameQueries("YvsParametreGrh.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            }
            calculDateRetour();
            switch (conges.getEffet()) {
                case Constantes.GRH_PERMISSION_SUR_CONGE:
                    if (Utilitaire.countDayBetweenDate(conges.getStartDate(), conges.getEndDate()) > (getDureeCongeRestant())) {
                        getErrorMessage("Vous ne disposé plus d'assez de congé !");
                        return false;
                    }
                    break;
                case Constantes.GRH_PERMISSION_AUTORISE:
                    if (paramGrh.getTotalCongePermis() <= conges.getDureePermissionAutorisePris()) {
                        getErrorMessage("Vous avez épuisé votre quotas de permission !");
                        return false;
                    }
                    break;
            }
            if (!updateConge) {
                currentConge = (YvsGrhCongeEmps) dao.save1(buildConge());
                conges.setIdConge(currentConge.getId());
                currentConge.setEtapesValidations(saveEtapesValidation(currentConge));
                conges.setEtapesValidations(currentConge.getEtapesValidations());
                eventModel.addEvent(recopieView());
                updateConge = true;
                listConges.add(0, currentConge);
                applyDureeConge(limitExercice, conges.getStartDate());
                succes();
                if (reset) {
                    resetView();
                }
                return true;
            } else {
                //modifier seulement si aucune étapes n'est validé
                if (canUpdateConge()) {
                    openDialog("dlgupdateconge");
                } else {
                    getErrorMessage("L'ordre de congé ne peux plus être modifié ", "La chaîne de validation est déjà en cours !");
                }
            }
        }
        return false;
    }

    public void addConge(ActionEvent ev) {
        saveNew(false);
    }

    private boolean canUpdateConge() {
        if (conges.getEtapesValidations() != null) {
            for (YvsWorkflowValidConge ec : conges.getEtapesValidations()) {
                if (ec.getEtapeValid()) {
                    return false;
                }
            }
        }
        return conges.getStatut() != Constantes.STATUT_DOC_VALIDE && conges.getStatut() != Constantes.STATUT_DOC_CLOTURE;
    }

    private YvsGrhCongeEmps buildConge() {
        YvsGrhCongeEmps c = new YvsGrhCongeEmps();
        c.setDateConge(new Date());
        c.setDateDebut(conges.getStartDate());
        c.setLibelle(conges.getTitle());
        c.setMotif(conges.getDescription());
        c.setEffet(Constantes.GRH_PERMISSION_SUR_SALAIRE);
        c.setDateFin(conges.getEndDate());
        c.setDatePaiementAlloc(conges.getDatePaiementAlloc());
        c.setEffet(conges.getEffet());
        ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
        if (service != null) {
            int idx = service.getListEmployes().indexOf(new YvsGrhEmployes(conges.getEmploye().getId()));
            if (idx >= 0) {
                c.setEmploye(service.getListEmployes().get(idx));
            } else {
                c.setEmploye(service.findOneEmploye(conges.getEmploye().getId()));
            }
        }
        c.setReferenceConge(conges.getReferenceConge());
        c.setCompterJourRepos(conges.isCompteJourRepos());
        c.setMotif(conges.getDescription());
        c.setDateRetour(conges.getDateRetourConge());
        c.setStatut(conges.getStatut());
        c.setId(null);
        c.setHeureDebut(conges.getHeureDebut());
        c.setHeureFin(conges.getHeureFin());
        c.setNature(conges.getNature());
        c.setDureePermission(conges.getTypeDureePermission());
        c.setDuree((int) conges.getDureeAbsence());
        c.setActif(true);
        c.setTypeConge(conges.getTypeConge());
        c.setAuthor(currentUser);
        c.setDureeSup(conges.getCongeSupp());
        c.setDureePermissionPrise((double) conges.getDureePermPrisSpeciale());
        c.setDateSave(conges.getDateSave());
        c.setDateUpdate(new Date());
        if (updateConge) {
            String[] st = conges.getTitle().split(":-");
            if (st.length == 2) {
                c.setLibelle(st[1].trim());
            }
            c.setId(conges.getIdConge());
        } else {
            c.setEtapeTotal(1);
            c.setEtapeValide(0);
            c.setStatut(Constantes.STATUT_DOC_ATTENTE);
        }
        if (conges.getNature() == 'P' && conges.getTypeDureePermission() == 'C' && c.getDateFin() == null) {
            c.setDateFin(c.getDateDebut());
        }
        return c;
    }

//    public void updateConge() {
//        int r = controleFiche();
//        if (r != 0) {
//            getErrorMessage("Il y a chevauchement entre les périodes de congés de cet employé !");
//        } else {
//            if (currentConge.equals(congeEmps)) {
//                if (currentConge.equals(congeEmps)) {
//                    currentConge = buildConge();
//                    currentConge.setId(congeEmps.getId());
//                    dao.update(currentConge);
//                    eventModel.updateEvent(recopieView());
//                    succes();
//                }
//            }
//        }
//    }
    public void onDateSelect(SelectEvent selectEvent) {
        updateConge = false;
//        conges = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        conges.setStartDate((Date) selectEvent.getObject());
        conges.setEndDate((Date) selectEvent.getObject());
        conges.setDureeAbsence(1);
    }

    public void onEventSelect(SelectEvent ev) {
        updateConge = true;
        ScheduleEvent e = (ScheduleEvent) ev.getObject();
        currentConge = (YvsGrhCongeEmps) dao.loadOneByNameQueries("YvsGrhCongeEmps.findById", new String[]{"id"}, new Object[]{((Conges) e).getIdConge(), 0, 1});
        currentConge.setEtapesValidations(dao.loadNameQueries("YvsWorkflowValidConge.findByDocument", new String[]{"document"}, new Object[]{currentConge}));
        populateView(UtilGrh.buildBeanCongeEmps(currentConge));
        update("conge-tab");
        update("all-view-conge-emps");
        update("part-conge-btn");
    }

    public void onEventMove(ScheduleEntryMoveEvent ev) {
        int ecart = ev.getDayDelta();
        Calendar cdf = Calendar.getInstance();
        Conges g = (Conges) ev.getScheduleEvent();
        cdf.setTime(g.getEndDate());
        cdf.add(Calendar.DAY_OF_YEAR, ecart);
        updateConge = true;
        currentConge = (YvsGrhCongeEmps) dao.loadOneByNameQueries("YvsGrhCongeEmps.findById", new String[]{"id"}, new Object[]{g.getIdConge(), 0, 1});
        populateView(UtilGrh.buildBeanCongeEmps(currentConge));
        update("conge-tab");
        update("all-view-conge-emps");
        update("part-conge-btn");
        openDialog("dlgupdateconge");
    }

    public void onEventResize(ScheduleEntryResizeEvent ev) {
        int ecart = ev.getDayDelta();
        Calendar cdf = Calendar.getInstance();
        Conges g = (Conges) ev.getScheduleEvent();
        cdf.setTime(g.getEndDate());
        cdf.add(Calendar.DAY_OF_YEAR, ecart);
        updateConge = true;
        currentConge = (YvsGrhCongeEmps) dao.loadOneByNameQueries("YvsGrhCongeEmps.findById", new String[]{"id"}, new Object[]{g.getIdConge(), 0, 1});
        currentConge.setEtapesValidations(dao.loadNameQueries("YvsWorkflowValidConge.findByDocument", new String[]{"document"}, new Object[]{currentConge}));
        populateView(UtilGrh.buildBeanCongeEmps(currentConge));
        update("conge-tab");
        update("all-view-conge-emps");
        update("part-conge-btn");
        openDialog("dlgupdateconge");
    }

    @Override
    public void onSelectDistant(YvsGrhCongeEmps y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Congés", "modRh", "smenConge", true);
            }
        }
    }

    @Override
    public void onSelectObject(YvsGrhCongeEmps y) {
        y.setEtapesValidations(dao.loadNameQueries("YvsWorkflowValidConge.findByDocument", new String[]{"document"}, new Object[]{y}));
        currentConge = y;
        populateView(UtilGrh.buildBeanCongeEmps(currentConge));
        currentNombre = listConges.indexOf(currentConge) + 1;
        //positionne l'exercice  en fonction de la durée de cumul
        if (paramGrh != null ? paramGrh.getDureeCumuleConge() > 0 : false) {
            Date d = yvs.dao.salaire.service.Constantes.givePrevOrNextDate((conges.getStartDate() != null) ? conges.getStartDate() : new Date(), Calendar.MONTH, -paramGrh.getDureeCumuleConge() * 12);
            if (d != null) {
                limitExercice = giveExerciceActif(d);
                idExoSelect = (limitExercice != null) ? limitExercice.getId() : 0;
                applyDureeConge(limitExercice, conges.getStartDate());
            }
        }
        update("all-view-conge-emps");
        update("part-conge-btn");
        update("form_main_conge");
        updateConge = true;
    }

    public void choixConge(SelectEvent ev) {
//        YvsGrhCongeEmps y = (YvsGrhCongeEmps) dao.loadOneByNameQueries("YvsGrhCongeEmps.findById", new String[]{"id"}, new Object[]{((YvsGrhCongeEmps) ev.getObject()).getId(), 0, 1});
        YvsGrhCongeEmps y = (YvsGrhCongeEmps) ev.getObject();
        onSelectObject(y);
        chaineSelect = listConges.indexOf(y) + "";
    }

    public void deleteConge() {
        List<Integer> l = decomposeSelection(chaineSelect);
        if (!autoriser("conge_delete_conge_suspendu")) {
            openNotAcces();
            return;
        }
        if (!l.isEmpty()) {
            try {
                YvsGrhCongeEmps c;
                for (int i : l) {
                    c = listConges.get(i);
                    if (c.getStatut() == Constantes.STATUT_DOC_ATTENTE || c.getStatut() == 'S' || c.getStatut() == 'A') {
                        c.setAuthor(currentUser);
                        dao.delete(c);
                        if (listConges.contains(c)) {
                            listConges.remove(c);
                        }
                        if (c.getId().equals(conges.getIdConge())) {
                            resetFiche();
                            update("form_main_conge");
                            update("etapes_valide_conges");
                        }
                    } else {
                        Constantes.message = "L'ordre de mission doit se trouver dans l'Etat annuler ou Suspendu !";
                        openDialog("dlgNotExecute");
                        return;
                    }
                }
            } catch (Exception ex) {
                log.log(Level.SEVERE, null, ex);
                getErrorMessage("Impossible de supprimer ce congé");
            } finally {
                chaineSelect = "";
            }
        }
        resetView();
        update("conge-tab");
    }

    public void openCongeToDel(YvsGrhCongeEmps cong) {
        if (cong != null) {
            populateView(UtilGrh.buildBeanCongeEmps(cong));
            openDialog("dlgDelConge_");
        }
    }

    public void resetView() {
        updateConge = false;
        conges.setStartDate(new Date());
        conges.setTitle(null);
        conges.setDescription(null);
        conges.setStatut(Constantes.STATUT_DOC_ATTENTE);
        conges.setNature('P');
        conges.setTypeDureePermission('L');
        conges.setEmploye(new Employe());
        conges.setIdConge(0);
        conges.setDureeAbsence(0);
        conges.setCongePrincipalPris(0);
        conges.setCongePrincipalDu(0);
        conges.setDateRetourConge(null);
        conges.setEndDate(null);
        conges.setTypeConge("Autres");
        conges.setEtapesValidations(new ArrayList<YvsWorkflowValidConge>());

        currentConge = new YvsGrhCongeEmps();
    }

    @Override
    public void deleteBean() {
        if (!autoriser("conge_delete_conge_suspendu")) {
            openNotAcces();
            return;
        }
        if (conges.getIdConge() > 0) {
            YvsGrhCongeEmps co = new YvsGrhCongeEmps(conges.getIdConge());
            try {
                if (conges.getStatut() == Constantes.STATUT_DOC_ATTENTE || conges.getStatut() == 'S' || conges.getStatut() == 'A') {
                    dao.delete(co);
                    if (listConges.contains(co)) {
                        listConges.remove(co);
                    }
                    if (co.getId().equals(conges.getIdConge())) {
                        resetFiche();
                        update("form_main_conge");
                        update("etapes_valide_conges");
                    }
                    succes();
                } else {
                    getErrorMessage("Le congé doit être dans l'état suspendu ou annulé !");
                }
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer !");
                log.log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void calculDureePermission() {
        if (isPermissionCD(conges)) {
            if (conges.getHeureDebut() != null && conges.getHeureFin() != null) {
                if (conges.getHeureDebut().before(conges.getHeureFin())) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(conges.getHeureFin());
                    Calendar c1 = Calendar.getInstance();
                    c1.setTime(conges.getHeureDebut());
                    c.add(Calendar.HOUR_OF_DAY, -c1.get(Calendar.HOUR_OF_DAY));
                    c.add(Calendar.MINUTE, -c1.get(Calendar.MINUTE));
                    c.add(Calendar.SECOND, -c1.get(Calendar.SECOND));
                    double time_ = Utilitaire.timeToDouble(c.getTime());
                    conges.setDureeAbsence(time_);
                    conges.setDateRetourConge(conges.getStartDate());
                    conges.setEndDate(conges.getStartDate());
                    conges.setHeureRetourEffectif(conges.getHeureFin());
                    update("txt_duree_conge");
                    update("dateEndR");
                    return;
                }
            }
            conges.setDureeAbsence(0);
            update("txt_duree_conge");
        } else {
            calculDateRetour();
            update("hourEndC");
            update("dateEndR");
        }
    }

    public void openDlgParamDuree() {
        System.err.println(" **** " + conges.getCongePrincipalDu() + " *** " + conges.getCongeSupp() + " ***** " + conges.getDureePermPrisSpeciale());
//        dureeP = conges.getCongePrincipalDu();
//        dureeSup = (int) conges.getCongeSupp();
//        dureePerm = conges.getDureePermPrisSurCongP();
        openDialog("dlgParamDuree");
    }

    public void applyParamDuree() {
        double maxAprendre = conges.getCongePrincipalDu() + conges.getCongeSupp() - conges.getDureePermPrisSpeciale() - conges.getDureePermissionAutorisePris();
        if (dureeP <= conges.getCongePrincipalDu()) {
            conges.setCongePrincipalDu(dureeP);
        }
        if (dureeSup <= conges.getCongeSupp()) {
            conges.setCongeSupp(dureeSup);
        }
        if (dureePerm <= conges.getDureePermissionAutorisePris()) {
            conges.setDureePermissionAutorisePris(dureePerm);
        }
        if ((dureeP + dureePerm + dureeSup) > maxAprendre) {
            getErrorMessage("La durée indiqué est suppérieur au qupotas requis !");
            return;
        }
        conges.setDureeAbsence(dureeP + dureePerm + dureeSup);
        calculDateRetour();
        update("txt_duree_conge");
        closeDialog("dlgParamDuree");
    }

    private boolean dayIsferie(Calendar debut) {
        if (debut != null) {
            int jour = debut.get(Calendar.DAY_OF_MONTH);
            int mois = debut.get(Calendar.MONTH);
            String rq = "SELECT COUNT(*) FROM Yvs_Jours_Feries WHERE ("
                    + "(SELECT EXTRACT(day FROM jour))=? AND (SELECT EXTRACT(month FROM jour))=? AND all_year IS true) "
                    + "OR (jour=? AND all_year IS false)";
            Long nb = (Long) dao.loadObjectBySqlQuery(rq, new Options[]{new Options(jour, 1), new Options(mois, 2), new Options(debut.getTime(), 3)});
            if (nb != null) {
                return nb != 0;
            }
        }
        return false;
    }
//ie la date passé en paramètre est un jour ouvrable et ouvéé et non férié

    private boolean dateDepartOk(List<YvsJoursOuvres> ljo, Calendar debut) {
        if (ljo != null) {
            String jour = Utilitaire.getDay(debut);
            for (YvsJoursOuvres jo : ljo) {
                if (jo.getJour().toUpperCase().equals(jour.toUpperCase())) {
                    //si non férié (alors jour ok)
                    if (dayIsferie(debut) && !conges.isCompteJourRepos()) {
                        return false;
                    } else if (!jo.getJourDeRepos()) //si ce n'est pas un jour de repos alors ok
                    {
                        return true;
                    } else {
                        return conges.isCompteJourRepos();
                    }
                }
            }
        }
        return false;
    }

    private boolean dateDepartIsOuvrable(List<YvsJoursOuvres> ljo, Calendar debut) {
        if (ljo != null) {
            String jour = Utilitaire.getDay(debut);
            for (YvsJoursOuvres jo : ljo) {
                if (jo.getJour().toUpperCase().equals(jour.toUpperCase())) {
                    //si non férié (alors jour ok)                    
                    if (dayIsferie(debut)) {
                        return false;
                    } else {
                        return !jo.getJourDeRepos();
                    }
                }
            }
        }
        return false;
    }

    public void calculDateRetour() {
        if (isPermissionCD(conges)) {
            conges.setDateRetourConge(conges.getStartDate());
            conges.setEndDate(conges.getStartDate());
            return;
        }
        //la date de retour est 
        if (!canUpdateConge() && updateConge) {
            return;
        }
        if (conges.getEmploye().getId() > 0) {
            YvsGrhContratEmps contrat = (YvsGrhContratEmps) dao.loadOneByNameQueries("YvsGrhContratEmps.findByEmploye_", new String[]{"employe"}, new Object[]{new YvsGrhEmployes(conges.getEmploye().getId())});
            if (contrat != null && conges.getStartDate() != null && conges.getHeureDebut() != null) {
                //récupère le calendrier de travail de l'employé
                if (contrat.getCalendrier() != null) {
                    if (!isPermissionCD(conges)) {    //si on es en mode permission longue gurée ou congé
                        //la date de départ en congé ne doit pas être un jour de repos pour l'employé (doit être un jour ouvrée)
                        Calendar debut = Calendar.getInstance();
                        debut.setTime(conges.getStartDate());
                        if (!dateDepartIsOuvrable(contrat.getCalendrier().getJoursOuvres(), debut)) {
                            getWarningMessage("La date de départ doit être un jour ouvré pour l'employé !");
                        }
                        if (conges.getDureeAbsence() > 0) {
                            int nbJour = (int) conges.getDureeAbsence();
                            while (nbJour > 1) {
                                if (dateDepartOk(contrat.getCalendrier().getJoursOuvres(), debut)) {
                                    nbJour--;
                                }
                                debut.add(Calendar.DAY_OF_MONTH, 1);
                            }
                            conges.setEndDate(debut.getTime());
                            debut.add(Calendar.DAY_OF_MONTH, 1);
                            while (!dateDepartOk(contrat.getCalendrier().getJoursOuvres(), debut)) {
                                debut.add(Calendar.DAY_OF_MONTH, 1);
                            }
                            conges.setDateRetourConge(debut.getTime());
                            if (contrat.getCalendrier().getJoursOuvres() != null) {
                                conges.setHeureRetourEffectif(contrat.getCalendrier().getJoursOuvres().get(0).getHeureDebutTravail());
                            }
                        } else {
                            conges.setEndDate(conges.getStartDate());
                            conges.setDateRetourConge(conges.getStartDate());
                        }
                    }

                } else {
                    getErrorMessage("Vous devez paramétrer un calendrier de travail pour l'employé !");
                }
            } else if (contrat == null) {
                getErrorMessage("Impossible de continuer", "aucun contrat n'a été touvé pour cet employé !");
            } else {
                getErrorMessage("La date et l'heure de début de congé doit être indiqué !");
            }
        }
        update("dateEndC");
    }

//    //cherche le jour Ouvré ou le prochain jour ouvrée qui correspond à cette date
//    private YvsJoursOuvres giveJourFin(List<YvsJoursOuvres> l, Calendar c) {
//        YvsJoursOuvres re = null;
//        while (re == null && !l.isEmpty()) {
//            for (YvsJoursOuvres j : l) {
//                j.setDate_(c.getTime());
//                if (j.getJour().trim().toLowerCase().equals(Utilitaire.getDay(c).toLowerCase()) && j.getOuvrable()) {
//                    re = j;
//                    return re;
//                }
//            }
//            c.add(Calendar.DAY_OF_MONTH, 1);
//
//        }
//        return re;
//        //si on arrive à la fin de la boucle, c'est que la journée du calendrier n'est pas un jour Ouvrée        
//    }
    public void choixTypeConge(ValueChangeEvent ev) {
        if (ev != null) {
            if (ev.getNewValue().equals(Constantes.GRH_TYPE_CONGE_ANNUELLE)) {
                conges.setEffet(Constantes.GRH_PERMISSION_SUR_CONGE);
                if (conges.getStartDate() == null) {
                    conges.setStartDate(new Date());
                }
                //récupère le nombre de jour de congé restant
                ManagedExercice service = (ManagedExercice) giveManagedBean(ManagedExercice.class);
                if (service != null) {
                    int idx = service.getExercices().indexOf(new YvsBaseExercice(idExoSelect));
                    if (idx >= 0) {
                        YvsBaseExercice exo = service.getExercices().get(idx);
                        applyDureeConge(exo, conges.getStartDate());
                    } else {
                        applyDureeConge(giveExerciceActif(), conges.getStartDate());
                    }
                }
                if (conges.getDureeAbsence() <= 0) {
                    conges.setDureeAbsence(conges.getCongePrincipalDu() + conges.getCongeSupp() - conges.getCongePrincipalPris() - conges.getDureePermPrisSpeciale());
                    conges.setDureeAbsence((conges.getDureeAbsence() < 0) ? 0 : conges.getDureeAbsence());
                }
                calculDateRetour();
            } else {
                conges.setCompteJourRepos(false);
            }
        }
    }

    //constitue un cumul mensuel du temps d'absence d'un employé dans le mois
    private List<String> months = new ArrayList<>();

    public List<String> getMonths() {
        return months;
    }

    public void setMonths(List<String> months) {
        this.months = months;
    }

    /**
     * ******************************************
     */
    private List<YvsWorkflowValidConge> saveEtapesValidation(YvsGrhCongeEmps ce) {
        //charge les étape de vailidation
        List<YvsWorkflowValidConge> re = new ArrayList<>();
        List<YvsWorkflowEtapeValidation> model;
        if (ce.getNature() == 'P' && ce.getDureePermission() == 'C') {
            model = dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", new String[]{"titre", "societe"}, new Object[]{Constantes.DOCUMENT_PERMISSION_COURTE_DUREE, currentAgence.getSociete()});
        } else {
            model = dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", new String[]{"titre", "societe"}, new Object[]{Constantes.DOCUMENT_CONGES, currentAgence.getSociete()});
        }
        if (!model.isEmpty()) {
            YvsWorkflowValidConge vc;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    vc = new YvsWorkflowValidConge();
                    vc.setAuthor(currentUser);
                    vc.setEtape(et);
                    vc.setEtapeValid(false);
                    vc.setConge(ce);
                    vc.setDateSave(new Date());
                    vc.setDateUpdate(new Date());
                    vc = (YvsWorkflowValidConge) dao.save1(vc);
                    re.add(vc);
//                    ce.setStatut('E');
//                    dao.update(ce);
                }
            }
        }
        ce.setEtapeTotal(model.size() == 0 ? 1 : model.size());
        ce.setEtapeValide(0);
        dao.update(ce);
        return ordonneEtapes(re);
    }

    private List<YvsWorkflowValidConge> ordonneEtapes(List<YvsWorkflowValidConge> l) {
        return YvsWorkflowValidConge.ordonneEtapes(l);
    }
    YvsWorkflowValidConge currentEtape;

    public void motifEtapeOrdre(String motifEtape, boolean lastEtape) {
        this.motifEtape = motifEtape;
        this.lastEtape = lastEtape;
    }

    public void annulEtapeOrdre(YvsWorkflowValidConge etape, boolean lastEtape) {
        this.etape = etape;
        this.lastEtape = lastEtape;
        this.motifEtape = null;
    }

    public boolean annulEtapeOrdre() {
        return annulEtapeOrdre(currentConge, conges, currentUser, etape, lastEtape, motifEtape);
    }

    public boolean annulEtapeOrdre(YvsGrhCongeEmps current, Conges fiche, YvsUsersAgence users, YvsWorkflowValidConge etape, boolean lastEtape, String motif) {
        if (!asDroitValideEtape(etape.getEtape(), users.getUsers())) {
            openNotAcces();
        } else {
            //contrôle la cohérence des dates
            if (motif != null ? motif.trim().isEmpty() : true) {
                getErrorMessage("Vous devez précisez le motif");
                return false;
            }
            if (current != null ? (current.getId() != null ? current.getId() < 1 : true) : true) {
                current = (YvsGrhCongeEmps) dao.loadOneByNameQueries("YvsGrhCongeEmps.findById", new String[]{"id"}, new Object[]{fiche.getId()});
            }
            if (fiche != null ? fiche.getIdConge() < 1 : true) {
                fiche = UtilGrh.buildBeanCongeEmps(current);
            }
            int idx = fiche.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                //cas de la validation de la dernière étapes
                if (etape.getEtapeSuivante() != null) {
                    champ = new String[]{"etape", "document"};
                    val = new Object[]{etape.getEtapeSuivante().getEtape(), current};
                    YvsWorkflowValidConge y = (YvsWorkflowValidConge) dao.loadOneByNameQueries("YvsWorkflowValidConge.findByEtapeDocument", champ, val);
                    if (y != null ? (y.getId() > 0 ? y.getEtapeValid() : false) : false) {
                        getErrorMessage("Vous devez au préalable annuler l'étape suivante");
                        return false;
                    }
                }
                etape.setAuthor(users);
                etape.setEtapeValid(false);
                etape.setEtapeActive(true);
                etape.setMotif(motif);
                dao.update(etape);

                current.setEtapeValide(current.getEtapeValide() - 1);
                current.setStatut(current.getEtapeValide() < 1 ? Constantes.STATUT_DOC_EDITABLE : Constantes.STATUT_DOC_ENCOUR);
                dao.update(current);

                fiche.setStatut(current.getStatut());
                fiche.setEtapeValide(current.getEtapeValide());
                if (listConges != null ? listConges.contains(current) : false) {
                    int idx_ = listConges.indexOf(current);
                    listConges.get(idx_).setEtapeValide(current.getEtapeValide());
                    listConges.get(idx_).setStatut(current.getStatut());
                }
                getInfoMessage("Annulation effectué avec succès !");
                return true;
            } else {
                getErrorMessage("Impossible de continuer !");
            }
        }
        return false;
    }

    public void annulerOrdreConge() {
        if (conges.getIdConge() > 0) {
            currentConge.setDateUpdate(new Date());
            currentConge.setAuthor(currentUser);
            currentConge.setStatut(Constantes.STATUT_DOC_ATTENTE);
            dao.update(currentConge);
            conges.setStatut(currentConge.getStatut());
            int idx = listConges.indexOf(currentConge);
            if (idx > 0) {
                listConges.set(idx, currentConge);
            }
        }
    }

    public void validEtapeOrdreConge(YvsWorkflowValidConge etape, boolean continu) {
        //vérifier que la personne qui valide l'étape a le droit 
        etape = (etape == null) ? currentEtape : etape;
        if (!asDroitValideEtape(etape.getEtape())) {
            openDialog("dlgNotAcces");
            return;
        } else {
            if (conges.getStatut() == 'S') {
                getErrorMessage("Cet ordre de congé a déjà été suspendu", "veuillez annuler la suspension avant de continuer !");
                return;
            }
            if (etape.getEtapeSuivante() != null) {  //valide les étapes intermediaire
                currentEtape = null;
                etape.setAuthor(currentUser);
                etape.setEtapeValid(true);
                etape.setEtapeActive(false);
                etape.setMotif(null);
                etape.setDateUpdate(new Date());
                int idx = conges.getEtapesValidations().indexOf(etape);
                if (idx >= 0) {
                    if (conges.getEtapesValidations().size() > (idx + 1)) {
                        conges.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                    }
                    currentConge.setStatut(Constantes.STATUT_DOC_EDITABLE);
                    conges.setStatut(Constantes.STATUT_DOC_EDITABLE);
                    dao.update(currentConge);
                    dao.update(etape);
                    conges.setEtapeValide(conges.getEtapeValide() + 1);
                    currentConge.setEtapeValide(currentConge.getEtapeValide() + 1);
                    listConges.set(listConges.indexOf(currentConge), currentConge);
                    getInfoMessage("Validation effectué avec succès !");
                } else {
                    getErrorMessage("Impossible de continuer !");
                }
            } else {
                if (!continu) {
                    etape.setAuthor(currentUser);
                }
                etape.setEtapeValid(true);
                etape.setEtapeActive(false);
                currentEtape = etape;
            }
        }
        //cas de la validation de la dernière étapes
        if (etape.getEtapeSuivante() == null) {
            valideConges(currentEtape, etape);
        }
    }

    public void valideConges(YvsWorkflowValidConge currentEtape, YvsWorkflowValidConge etape) {
        //vérifie que le nombre de jours pris est cohérent avec le nombre de jour disponible
        if (autoriser("grh_valid_conge")) {
            switch (conges.getEffet()) {
                case Constantes.GRH_PERMISSION_SUR_CONGE:
                    if (Utilitaire.countDayBetweenDate(conges.getStartDate(), conges.getEndDate()) > conges.getCongePrincipalDu()) {
                        getMessage("Vous ne disposé plus d'assez de congé !", FacesMessage.SEVERITY_ERROR);
                        if (etape != null) {
                            etape.setEtapeValid(false);
                            etape.setEtapeActive(true);
                        }
                        return;
                    }
                    break;
                case Constantes.GRH_PERMISSION_AUTORISE:
                    if (paramGrh.getTotalCongePermis() <= conges.getDureePermissionAutorisePris()) {
                        getErrorMessage("Vous avez épuisé votre quotas de permission !");
                        if (etape != null) {
                            etape.setEtapeValid(false);
                            etape.setEtapeActive(true);
                        }
                        return;
                    }
                    break;
            }
            if (currentEtape != null) {
                int idx = conges.getEtapesValidations().indexOf(currentEtape);
                if (idx >= 0) {
                    if (conges.getEtapesValidations().size() > (idx + 1)) {
                        conges.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                    }
                } else {
                    getErrorMessage("Impossible de continuer !");
                    return;
                }
            }
            conges.setEtapeValide(conges.getEtapeValide() + 1);
            currentConge.setStatut(Constantes.STATUT_DOC_VALIDE);
            currentConge.setDatePaiementAlloc(conges.getDatePaiementAlloc());
            conges.setStatut(Constantes.STATUT_DOC_VALIDE);
            dao.update(currentConge);
            if (currentEtape != null) {
                dao.update(currentEtape);
                currentConge.setEtapeValide(currentConge.getEtapeValide() + 1);
            }
            int idx = listConges.indexOf(currentConge);
            if (idx >= 0) {
                listConges.set(listConges.indexOf(currentConge), currentConge);
                getInfoMessage("Validation effectué avec succès !");
            }
        } else {
            openNotAcces();
        }
    }

    public String giveTextTypeConge(String str) {
        switch (str) {
            case Constantes.GRH_TYPE_CONGE_CT:
                return "Congé Technique";
            case Constantes.GRH_TYPE_CONGE_MALADI:
                return "Conge Maladie";
            case Constantes.GRH_TYPE_CONGE_ANNUELLE:
                return "Conge Annuel";
            default:
                return "Autres";
        }
    }
    private boolean onlyReinit;

    public boolean isOnlyReinit() {
        return onlyReinit;
    }

    public void setOnlyReinit(boolean onlyReinit) {
        this.onlyReinit = onlyReinit;
    }

    public void cancelOrdreMission(boolean continuAction) {
        //vérifie le droit
        if (!autoriser("conge_cancel")) {
            openNotAcces();
            return;
        }
        //annule toute les validation acquise
        int i = 0;
        boolean update = false;
        if (!continuAction) {
            for (YvsWorkflowValidConge vc : conges.getEtapesValidations()) {
                if (!vc.getEtapeValid()) {
                    update = true;
                }
            }
        } else {
            update = true;
            if (!autoriser("conge_cancel_valide")) {
                openNotAcces();
                return;
            }
        }
        if (update) {
            if (conges.getStatut() == 'V' && !autoriser("conge_cancel_valide")) {
                openNotAcces();
                return;
            } else if (conges.getStatut() == 'C' && !autoriser("conge_cancel_cloturer")) {
                openNotAcces();
                return;
            }
            for (YvsWorkflowValidConge vc : conges.getEtapesValidations()) {
                vc.setEtapeActive(false);
                if (i == 0) {
                    vc.setEtapeActive(true);
                }
                vc.setAuthor(currentUser);
                vc.setEtapeValid(false);
                dao.update(vc);
                i++;
            }
            currentConge.setAuthor(currentUser);
            if (conges.getEtapesValidations().isEmpty()) {  //si les étapes des validations ne sont pas paramétrés, on considère directement l'état validé
                currentConge.setStatut('V');
                conges.setStatut('V');
            } else {
                currentConge.setStatut(onlyReinit ? 'W' : 'A');
                conges.setStatut(onlyReinit ? 'W' : 'A');
                currentConge.setEtapeValide(0);
            }
            dao.update(currentConge);
            listConges.set(listConges.indexOf(currentConge), currentConge);
        } else {
            getWarningMessage("Vous souhaitez annuler toutes les étapes de validations déjà validé?");
            openDialog("dlgCancelValidC");
        }

    }

    /*suspend*/
    public void suspendreOrdreConge(boolean cloturer) {
        //vérifie le droit
        boolean valide = true;
        if (autoriser("conge_suspendre")) {
            //le congé doit déjà être validé
            if (cloturer && !autoriser("conge_cloturer")) {
                Constantes.message = "Vous ne disposez pas des privilège de clôture !";
                openDialog("dlgNotExecute");
                return;
            }
            for (YvsWorkflowValidConge vc : conges.getEtapesValidations()) {
                if (!vc.getEtapeValid()) {
                    valide = false;
                    break;
                }
            }
            if (valide && conges.getStatut() == 'V') {
                //controle les infos dateSuspension, Motif, 
                //la date de suspension doit tjrs être inférieure à la date de fin
                if (conges.getDateSuspension() != null && conges.getDateSuspension().before(conges.getEndDate()) && conges.getDateSuspension().after(conges.getStartDate())) {
                    //la date de suspension doit être un jour ouvrable
                    if (!ouvrable(conges.getDateSuspension(), currentConge.getEmploye().getContrat().getCalendrier())) {
                        getErrorMessage(dfML.format(conges.getDateSuspension()) + " n'est pas un jour ouvrable pour " + currentConge.getEmploye().getNom());
                        return;
                    }
//                    if (cloturer && (conges.getTypeConge().equals(Constantes.GRH_TYPE_CONGE_ANNUELLE)) && conges.getDatePaiementAlloc() == null) {
//                        getErrorMessage("Vous devez renseigner la date de paiement de l'allocation !");
//                        return;
//                    }
                    if (cloturer) {
                        currentConge.setStatut('C');
                        conges.setStatut('C');
                    }
                    currentConge.setDateFinPrevu(currentConge.getDateFin());
                    currentConge.setDateSuspension(conges.getDateSuspension());
                    currentConge.setMotifSuspension(conges.getMotifSuspension());
                    currentConge.setDateFin(conges.getDateSuspension());
                    conges.setEndDate(conges.getDateSuspension());
                    currentConge.setDateSave(new Date());
                    currentConge.setAuthor(currentUser);
                    currentConge.setDateRetour(conges.getDateSuspension());
                    conges.setDateRetourConge(conges.getDateSuspension());
                    currentConge.setDuree(Utilitaire.countDayBetweenDate(currentConge.getDateDebut(), currentConge.getDateFin()));
                    conges.setDureeAbsence(currentConge.getDuree());
                    dao.update(currentConge);
                    if (listConges.contains(currentConge)) {
                        listConges.set(listConges.indexOf(currentConge), currentConge);
                    }
                    succes();
                    closeDialog("dlgSuspenConge");
                } else {
                    if (conges.getDateSuspension() == null) {
                        getErrorMessage("Vous devez indiquer la nouvelle date de fin !");
                    } else {
                        getErrorMessage("La date de suspension doit être antérieure à la date de fin et ultérieure à la date de début du congé ou permission!");
                    }
                }
            } else {
                if (conges.getStatut() == 'C') {
                    getErrorMessage("Cet ordre de congé est déjà clôturé !");
                } else {
                    getErrorMessage("Le Congé n'est même pas validé !");
                }
            }
        } else {
            openNotAcces();
            return;
        }
    }

    public void cloturerConge() {
        //contrôle les droit
        if (!autoriser("conge_cloturer")) {
            openNotAcces();
            return;
        }
        clotureConge(currentConge);

    }

    private void clotureConge(YvsGrhCongeEmps c) {
        //toutes les autre étape doivent déjà être validé ou non configuré
        for (YvsWorkflowValidConge e : c.getEtapesValidations()) {
            if (!e.getEtapeValid()) {
                getErrorMessage("Impossible de clôturer l'ordre de congé ", "Toutes les étapes n'ont pas été validé !");
                return;
            }
        }
        if ((c.getStatut() != null) ? c.getStatut() != Constantes.STATUT_DOC_VALIDE : true) {
            getErrorMessage("Le statut du congé doit être validé", "Veullez valider d'abord l'ordre de congé !");
            return;
        }
        //clôturer seulement si on la mission est fini
        if (c.getDateFin().after(new Date())) {
            getErrorMessage("Le congé est encore en cours ");
            return;
        }
        c.setStatut('C');
        c.setAuthor(currentUser);
        dao.update(c);
        if (listConges.contains(c)) {
            listConges.set(listConges.indexOf(c), c);
        }
        succes();
        conges.setStatut('C');
        getInfoMessage("Congé clôturé avec succès !");
    }

    private boolean ouvrable(Date day, YvsCalendrier cal) {
        Calendar c = Calendar.getInstance();
        c.setTime(day);
        for (YvsJoursOuvres jo : cal.getJoursOuvres()) {
            if (Utilitaire.getDay(c).toLowerCase().equals(jo.getJour().toLowerCase()) && jo.getOuvrable()) {
                return true;
            }
        }
        return false;
    }

    public long countCongeToValid() {
        String rq = "select count(distinct c.*) from yvs_grh_conge_emps c inner join yvs_workflow_valid_conge vc on vc.conge=c.id inner join \n"
                + "yvs_workflow_etape_validation et on et.id=vc.etape inner join yvs_workflow_autorisation_valid_doc au on au.etape_valide=et.id\n"
                + "where c.statut='E' and vc.etape_valid is false and au.niveau_acces=?";
        if (currentUser != null) {
            if (currentUser.getUsers().getNiveauAcces() != null) {
                return (long) dao.loadObjectBySqlQuery(rq, new Options[]{new Options(currentUser.getUsers().getNiveauAcces().getId(), 1)});
            }
        }
        return 0;
    }

    /**
     * ************Recherche par le traitement dynamic de la
     * requête*****************
     */
    /*Pour la recherche*/
    private Date dateFind, dateFind1;
    private int filtre;
    private String filterStatut;
    private Character filterNature;
    private String attributDate = "dateDebut", egaliteStatut = "!=";
    private String effetF;
    private long idParamAgence;
    private String matriculeF;

    public String getEgaliteStatut() {
        return egaliteStatut;
    }

    public void setEgaliteStatut(String egaliteStatut) {
        this.egaliteStatut = egaliteStatut;
    }

    public String getEffetF() {
        return effetF;
    }

    public void setEffetF(String effetF) {
        this.effetF = effetF;
    }

    //choisi sur quelle date faire la comparaison: debut ou fin
    public void changeAttributDate(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            attributDate = (String) ev.getNewValue();
            findByDate(true);
        } else {
            findByDate(false);
        }
    }

    private void findByDate(boolean find) {
        ParametreRequete p = new ParametreRequete("y." + attributDate, attributDate, null, " BETWEEN ", "AND");
        if (find) {
            if (dateFind != null && dateFind1 != null) {
                if (dateFind.before(dateFind1) || dateFind.equals(dateFind1)) {
                    p.setObjet(dateFind);
                    p.setOtherObjet(dateFind1);
                }
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllConge(true, true);
    }

    public void addParamDate1(SelectEvent ev) {
        findByDate(true);
    }

    public void addParamDate2() {
        findByDate(true);
    }

    public void addParamStatuts() {
        addParamStatuts(true);
    }

    public void addParamStatuts(boolean load) {
        ParametreRequete p = new ParametreRequete("y.statut", "statut", null, egaliteStatut, "AND");
        if (Util.asString(filterStatut)) {
            p.setObjet(filterStatut.charAt(0));
        }
        paginator.addParam(p);
        if (load) {
            initForm = true;
            loadAllConge(true, true);
        }
    }

    public void addParamStatut(ValueChangeEvent ev) {
        String statut = ((String) ev.getNewValue());
        if (statut != null ? statut.trim().equals("Z") : false) {
            openDialog("dlgMoreStatuts");
        } else {
            filterStatut = (String) ev.getNewValue();
            addParamStatuts();
        }
    }

    public void chooseStatuts() {
        ParametreRequete p = new ParametreRequete("y.statut", "statut", null);
        if (statuts != null ? !statuts.isEmpty() : false) {
            boolean add = true;
            for (Character s : statuts) {
                if (s != null ? s.toString().trim().length() < 1 : true) {
                    add = false;
                    break;
                }
            }
            if (add) {
                p = new ParametreRequete("y.statut", "statut", statuts, "IN", "AND");
            }
        }
        paginator.addParam(p);
        loadAllConge(true, true);
    }

    public void addParamNature(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.nature", "nature", (Character) ev.getNewValue(), "=", "AND");
        paginator.addParam(p);
        initForm = true;
        loadAllConge(true, true);
    }

    public void addParamEffet(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.effet", "effet", (String) ev.getNewValue(), "=", "AND");
        paginator.addParam(p);
        initForm = true;
        loadAllConge(true, true);
    }

//    public void addParamSociete(ValueChangeEvent ev) {
//        ParametreRequete p = new ParametreRequete("y.employe.agence.societe", "societe", currentUser.getAgence().getSociete());
//        if (displayCongeAllSociete) {
//            p.setOperation("=");
//            p.setPredicat("AND");
//        } else {
//            p.setObjet(null);
//        }
//        paginator.addParam(p);
//        initForm = true;
//        loadAllConge(true, true);
//    }
//    public void addParamAgence(ValueChangeEvent ev) {
//        ParametreRequete p = new ParametreRequete("y.employe.agence", "agence", null);
//        if (ev != null) {
//            long id = (long) ev.getNewValue();
//            if (id > 0) {
//                YvsAgences ag = new YvsAgences((long) ev.getNewValue());
//                p.setObjet(ag);
//                p.setOperation("=");
//                p.setPredicat("AND");
//            } else if (id == -2) {
//                p.setAttribut("y.employe.agence.societe");
//                p.setParamNome("societe");
//                p.setObjet(currentUser.getAgence().getSociete());
//                p.setOperation("=");
//                p.setPredicat("AND");
//            }
//        }
//        paginator.addParam(p);
//        initForm = true;
//        loadAllConge(true, true);  
//    }
    public void addParamEmploye(String matricule) {
        ParametreRequete p = new ParametreRequete(null, "employe", "XX", "LIKE", "AND");
        if ((matricule != null) ? !matricule.isEmpty() : false) {
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.employe.nom)", "emps", "%" + matricule.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.employe.prenom)", "emps", "%" + matricule.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.employe.matricule)", "emps", matricule.trim().toUpperCase() + "%", " LIKE ", "OR"));
        } else {
            p.setObjet(null);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllConge(true, true);
    }

    public void exportOrdreMission() {
        exportOrdreMission(null);
    }

    public void exportOrdreMission(YvsPrintDecisionCongeHeader model) {
        //choisir le rapport à imprimer
        String fileReport = null;
        if (conges.getNature() == 'P' && conges.getTypeDureePermission() == 'C') {
            fileReport = "permission_court";
        } else if (conges.getNature() == 'P' && conges.getTypeDureePermission() == 'L') {
            fileReport = "permission_long";
        } else {
            fileReport = "decision_conge";
        }
        if (conges.getIdConge() > 0) {
            try {
                Map<String, Object> param_ = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param_.put("ID", (int) conges.getIdConge());
                param_.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param_.put("IMG_LOGO", returnLogo());
                param_.put("IMG_SIEGE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "siege.png"));
                param_.put("IMG_PHONE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "phone.png"));
                param_.put("IMG_CACHET", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (conges.getStatut() == Constantes.STATUT_DOC_VALIDE ? "cachet_approuv.png" : null));
                param_.put("SUBREPORT_DIR", SUBREPORT_DIR(true));
                if ("decision_conge".equals(fileReport)) {
                    if (model != null ? model.getId() < 1 : true) {
                        model = (YvsPrintDecisionCongeHeader) dao.loadOneByNameQueries("YvsPrintDecisionCongeHeader.findByDefaut", new String[]{"societe", "defaut"}, new Object[]{currentAgence.getSociete(), true});
                        if (model != null ? model.getId() < 1 : true) {
                            getErrorMessage("Vous devez creer un model de decision de congé par défaut ");
                            return;
                        }
                    }
                    param_.put("MODEL", model.getId().intValue());
                    param_.put("TEXT_DUREE_CONGE", Nombre.CALCULATE.getValue(conges.getDureeAbsence()));
                    int yvs_grh_conge_emps_duree_principal = conges.getCongePrincipalPris();
                    int yvs_grh_conge_emps_duree_permission_prise = conges.getDureePermPrisSpeciale();
                    double yvs_grh_conge_emps_duree_sup = conges.getCongeSupp();
                    param_.put("yvs_grh_conge_emps_duree_principal", yvs_grh_conge_emps_duree_principal);
                    param_.put("TEXT_CONGE_PRINCIPAL", Nombre.CALCULATE.getValue(yvs_grh_conge_emps_duree_principal));
                    param_.put("yvs_grh_conge_emps_duree_sup", yvs_grh_conge_emps_duree_sup);
                    param_.put("TEXT_MAJORATION", Nombre.CALCULATE.getValue(yvs_grh_conge_emps_duree_sup));
                    param_.put("yvs_grh_conge_emps_duree_permission_prise", yvs_grh_conge_emps_duree_permission_prise);
                    param_.put("TEXT_PERMISSION", Nombre.CALCULATE.getValue(yvs_grh_conge_emps_duree_permission_prise));
                }
                executeReport(fileReport, param_, (int) conges.getIdConge() + "");

            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(ManagedMission.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            getErrorMessage("Impossible d'imprimer, votre selection est ambigue !");
        }
    }

    public void correctionNumero() {
        try {
            List<YvsGrhCongeEmps> list = dao.loadNameQueries("YvsGrhCongeEmps.findByNotNumero", new String[]{"societe", "debut", "fin"}, new Object[]{currentAgence.getSociete(), dateDebutCorrection, dateFinCorrection});
            if (list != null ? !list.isEmpty() : false) {
                for (YvsGrhCongeEmps y : list) {
                    String numero = genererReference(Constantes.TYPE_CGE_NAME, y.getDateDebut(), y.getEmploye().getId());
                    if (numero != null ? numero.trim().length() < 1 : true) {
                        return;
                    }
                    y.setReferenceConge(numero);
                    dao.update(y);
                    int index = listConges.indexOf(y);
                    if (index > -1) {
                        listConges.get(index).setReferenceConge(numero);
                    }
                }
                update("table_listConge");
                succes();
            }
        } catch (Exception ex) {
            getException("correctionNumero", ex);
        }
    }

}
