/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.evenement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.mutuelle.credit.YvsMutCredit;
import yvs.entity.mutuelle.credit.YvsMutTypeCredit;
import yvs.entity.mutuelle.evenement.YvsMutActivite;
import yvs.entity.mutuelle.evenement.YvsMutContributionEvenement;
import yvs.entity.mutuelle.evenement.YvsMutCoutEvenement;
import yvs.entity.mutuelle.evenement.YvsMutEvenement;
import yvs.entity.mutuelle.evenement.YvsMutFinancementActivite;
import yvs.entity.mutuelle.evenement.YvsMutParticipantEvenement;
import yvs.entity.mutuelle.evenement.YvsMutTauxContribution;
import yvs.entity.mutuelle.evenement.YvsMutTypeEvenement;
import yvs.entity.param.YvsDictionnaire;
import yvs.grh.UtilGrh;
import yvs.grh.bean.Employe;
import yvs.mutuelle.CaisseMutuelle;
import yvs.mutuelle.ManagedMutualiste;
import yvs.mutuelle.Mutualiste;
import yvs.mutuelle.UtilMut;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.dico.ManagedDico;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedEvenement extends Managed<Evenement, YvsMutEvenement> implements Serializable {

    @ManagedProperty(value = "#{evenement}")
    private Evenement evenement;
    private List<YvsMutEvenement> evenements;

    private Activite activite = new Activite();
    private ParticipantEvenement participant = new ParticipantEvenement();
    private ContributionEvenement contribution = new ContributionEvenement();
    private FinancementActivite finance = new FinancementActivite();

    private ScheduleModel eventModel;

    private String tabIds, tabIds_contribution, tabIds_mutualiste, tabIds_participant;

    private String mutualisteSearch;
    private boolean dateSearch;
    private Date debutSearch = new Date(), finSearch = new Date();
    private long lieuSearch, typeSearch;

    YvsMutEvenement entityEvenement;

    public ManagedEvenement() {
        eventModel = new DefaultScheduleModel();
        evenements = new ArrayList<>();
    }

    public FinancementActivite getFinance() {
        return finance;
    }

    public void setFinance(FinancementActivite finance) {
        this.finance = finance;
    }

    public Activite getActivite() {
        return activite;
    }

    public void setActivite(Activite activite) {
        this.activite = activite;
    }

    public String getMutualisteSearch() {
        return mutualisteSearch;
    }

    public void setMutualisteSearch(String mutualisteSearch) {
        this.mutualisteSearch = mutualisteSearch;
    }

    public boolean isDateSearch() {
        return dateSearch;
    }

    public void setDateSearch(boolean dateSearch) {
        this.dateSearch = dateSearch;
    }

    public Date getDebutSearch() {
        return debutSearch;
    }

    public void setDebutSearch(Date debutSearch) {
        this.debutSearch = debutSearch;
    }

    public Date getFinSearch() {
        return finSearch;
    }

    public void setFinSearch(Date finSearch) {
        this.finSearch = finSearch;
    }

    public long getLieuSearch() {
        return lieuSearch;
    }

    public void setLieuSearch(long lieuSearch) {
        this.lieuSearch = lieuSearch;
    }

    public long getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(long typeSearch) {
        this.typeSearch = typeSearch;
    }

    public YvsMutEvenement getEntityEvenement() {
        return entityEvenement;
    }

    public void setEntityEvenement(YvsMutEvenement entityEvenement) {
        this.entityEvenement = entityEvenement;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public String getTabIds_contribution() {
        return tabIds_contribution;
    }

    public void setTabIds_contribution(String tabIds_contribution) {
        this.tabIds_contribution = tabIds_contribution;
    }

    public ContributionEvenement getContribution() {
        return contribution;
    }

    public void setContribution(ContributionEvenement contribution) {
        this.contribution = contribution;
    }

    public String getTabIds_participant() {
        return tabIds_participant;
    }

    public void setTabIds_participant(String tabIds_participant) {
        this.tabIds_participant = tabIds_participant;
    }

    public String getTabIds_mutualiste() {
        return tabIds_mutualiste;
    }

    public void setTabIds_mutualiste(String tabIds_mutualiste) {
        this.tabIds_mutualiste = tabIds_mutualiste;
    }

    public ParticipantEvenement getParticipant() {
        return participant;
    }

    public void setParticipant(ParticipantEvenement participant) {
        this.participant = participant;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public List<YvsMutEvenement> getEvenements() {
        return evenements;
    }

    public void setEvenements(List<YvsMutEvenement> evenements) {
        this.evenements = evenements;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        loadAllEvenement(true, true);
        tabIds = "";
        tabIds_contribution = "";
        tabIds_mutualiste = "";
        tabIds_participant = "";
    }

    public void loadAllEvenement(boolean avance, boolean init) {
        eventModel.clear();
        paginator.addParam(new ParametreRequete("y.mutualiste.mutuelle", "mutuelle", currentMutuel, "=", "AND"));
        evenements = paginator.executeDynamicQuery("YvsMutEvenement", "y.dateDebut DESC, y.dateFin DESC", avance, init, (int) imax, dao);
        for (YvsMutEvenement e : evenements) {
            Evenement evt = UtilMut.buildBeanEvenement(e);
            eventModel.addEvent(evt);
        }
        update("data_evenement");
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsMutEvenement> re = paginator.parcoursDynamicData("YvsMutEvenement", "y", "y.dateDebut DESC, y.dateFin DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void paginer(boolean next) {
        loadAllEvenement(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAllEvenement(true, true);
    }

//    public void loadActivites(YvsMutTypeEvenement type) {
//        if (type != null ? (type.getId() != null ? type.getId() > 0 : false) : false) {
//            evenement.getActivites_all().clear();
//            List<YvsMutTauxContribution> taux = dao.loadNameQueries("YvsMutTauxContribution.findByType", new String[]{"type"}, new Object[]{type});
//            if (taux != null ? !taux.isEmpty() : false) {
//                YvsMutActivite y;
//                for (YvsMutTauxContribution t : taux) {
//                    y = (YvsMutActivite) dao.loadOneByNameQueries("YvsMutActivite.findOne", new String[]{"type", "evenement"}, new Object[]{t.getTypeContibution(), entityEvenement});
//                    if (y != null ? (y.getId() != null ? y.getId() < 1 : true) : true) {
//                        y = new YvsMutActivite(-(long) (evenement.getActivites_all().size() + 1));
//                        y.setAuthor(currentUser);
//                        y.setEvenement(entityEvenement);
//                        y.setTypeActivite(t.getTypeContibution());
//                        y.setMontantRequis(t.getMontantMin());
//                        y.setNew_(true);
//                    }
//                    evenement.getActivites_all().add(y);
//                }
//                update("data_activite_evenement");
//            }
//        }
//    }
//    public void loadContribution() {
//        evenement.setContributions(dao.loadNameQueries("YvsMutContributionEvenement.findByEvenement", new String[]{"evenement"}, new Object[]{entityEvenement}));
////        Double montant = (Double) dao.loadObjectByNameQueries("YvsMutContributionEvenement.findContribution", new String[]{"evenement", "compte"}, new Object[]{y, c});
////                    cpt.setMontantVerse(montant != null ? montant : 0);
////                    cpt.setMontant(cpt.getMontantVerse());
//    }
//    public void loadContribution(YvsMutEvenement y) {
//        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
//            evenement.getContributions().clear();
//            ManagedMutualiste w = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
//            if (w != null) {
//                YvsMutContributionEvenement cpt;
//                for (YvsMutCompte c : w.getComptes()) {
//                    boolean add = true;
//                    cpt = new YvsMutContributionEvenement((long) evenement.getContributions().size() + 1);
//                    cpt.setAuthor(currentUser);
//                    cpt.setCompte(c);
//                    cpt.setDateContribution(new Date());
//                    cpt.setEvenement(y);
//
//                    Double montant = (Double) dao.loadObjectByNameQueries("YvsMutContributionEvenement.findContribution", new String[]{"evenement", "compte"}, new Object[]{y, c});
//                    cpt.setMontantVerse(montant != null ? montant : 0);
//                    cpt.setMontant(cpt.getMontantVerse());
//                    if (cpt.getMontantVerse() >= y.getMontantObligatoire()) {
//                        add = contribution.isAfficheAll();
//                    } else if (cpt.getMontantVerse() > 0) {
//                        cpt.setEtat(Constantes.ETAT_ENCOURS);
//                    } else {
//                        cpt.setEtat(Constantes.ETAT_ATTENTE);
//                    }
//                    if (add) {
//                        evenement.getContributions().add(cpt);
//                    }
//                }
//            }
//        }
//    }
//    public void loadParticipant(YvsMutEvenement y, YvsMutActivite a) {
//        if (a != null ? (a.getId() != null ? a.getId() > 0 : false) : false) {
//            evenement.setParticipants(dao.loadNameQueries("YvsMutParticipantEvenement.findByActivite", new String[]{"activite"}, new Object[]{a}));
//        } else if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
//            evenement.setParticipants(dao.loadNameQueries("YvsMutParticipantEvenement.findByEvenement", new String[]{"evenement"}, new Object[]{y}));
//        }
//        update("tabview_evt:data_participant_evenement");
//    }
    public void loadFinancement(YvsMutEvenement y, YvsMutActivite a) {
        if (a != null ? (a.getId() != null ? a.getId() > 0 : false) : false) {
            evenement.setFinancements(dao.loadNameQueries("YvsMutFinancementActivite.findByActivite", new String[]{"activite"}, new Object[]{a}));
        } else if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            evenement.setFinancements(dao.loadNameQueries("YvsMutFinancementActivite.findByEvenement", new String[]{"evenement"}, new Object[]{y}));
        }
        update("tabview_evt:data_financement_evenement");
    }

    @Override
    public Evenement recopieView() {
        Evenement e = new Evenement();
        e.setDateEvenement((evenement.getDateEvenement() != null) ? evenement.getDateEvenement() : new Date());
        e.setDateDebut((evenement.getDateDebut() != null) ? evenement.getDateDebut() : new Date());
        e.setDateFin((evenement.getDateFin() != null) ? evenement.getDateFin() : new Date());
        e.setHeureFin((evenement.getHeureFin() != null) ? evenement.getHeureFin() : new Date());
        e.setDescriptionEvt(evenement.getDescriptionEvt());
        e.setIdEvt(evenement.getIdEvt());
        e.setEtat(evenement.getEtat());
        e.setType(evenement.getType());
        e.setMontantObligatoire(evenement.getMontantObligatoire());
        e.setMutualiste(evenement.getMutualiste());
        e.setHeureDebut((evenement.getHeureDebut() != null) ? evenement.getHeureDebut() : new Date());
        e.setDateClotureContribution((evenement.getDateClotureContribution() != null) ? evenement.getDateClotureContribution() : new Date());
        e.setDateOuvertureContribution((evenement.getDateOuvertureContribution() != null) ? evenement.getDateOuvertureContribution() : new Date());
        e.setLieu(evenement.getLieu());

        e.setStartDate(e.getDateDebut());
        e.setEndDate(e.getDateFin());
        e.setDescription(e.getDescriptionEvt());
        String titre = " " + e.getMutualiste().getEmploye().getPrenom() + " " + e.getMutualiste().getEmploye().getNom() + " : " + e.getType().getDesignation();
        e.setTitle(titre);
        e.setCaisse(evenement.getCaisse());
        return e;
    }

    @Override
    public boolean controleFiche(Evenement bean) {
        if (bean.getCaisse() != null ? bean.getCaisse().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner une caisse de financement !");
            return false;
        }
        if ((bean.getType() != null) ? bean.getType().getId() < 1 : true) {
            getErrorMessage("Vous devez indiquer le type d'evenement");
            return false;
        }
        if (bean.getType().isLierMutualiste()) {
            if ((bean.getMutualiste() != null) ? bean.getMutualiste().getId() < 1 : true) {
                getErrorMessage("Vous devez specifier le mutualiste");
                return false;
            }
        }
        if (bean.getDateEvenement() != null) {
            return existExerice(bean.getDateEvenement());
        }
        return true;
    }

    public boolean controleFiche(ContributionEvenement bean) {
        if ((bean.getCompte() != null) ? bean.getCompte().getId() < 1 : true) {
            getErrorMessage("Vous devez indiquer le compte");
            return false;
        }
        if (bean.getMontant() < 1) {
            getErrorMessage("Vous devez precisez le montant");
            return false;
        }
        return true;
    }

    public boolean controleFiche(ParticipantEvenement bean) {
        if ((bean.getActivite() != null) ? bean.getActivite().getId() < 1 : true) {
            getErrorMessage("Vous devez indiquer l' activité");
            return false;
        }
        if ((bean.getMutualiste() != null) ? bean.getMutualiste().getId() < 1 : true) {
            getErrorMessage("Vous devez indiquer le mutualiste");
            return false;
        }
        return true;
    }

    public boolean controleFiche(FinancementActivite bean) {
        if ((bean.getActivite() != null) ? bean.getActivite().getId() < 1 : true) {
            getErrorMessage("Vous devez indiquer l' activité");
            return false;
        }
        if ((bean.getCaisse() != null) ? bean.getCaisse().getId() < 1 : true) {
            getErrorMessage("Vous devez indiquer la caisse");
            return false;
        }
        return true;
    }

    public boolean existExerice(Date date) {
        boolean exist = getExerciceOnDate(date);
        if (!exist) {
            openDialog("dlgConfirmCreate");
        }
        return exist;
    }

    public boolean getExerciceOnDate(Date date) {
        String[] ch = new String[]{"societe", "dateJour"};
        Object[] v = new Object[]{currentAgence.getSociete(), date};
        YvsBaseExercice exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findActifByDate", ch, v);
        if ((exo != null) ? exo.getId() != 0 : false) {
            return true;
        }
        return false;
    }

    @Override
    public void populateView(Evenement bean) {
        cloneObject(evenement, bean);
        evenement.setStartDate(bean.getStartDate());
        evenement.setEndDate(bean.getEndDate());
        evenement.setTitle(bean.getTitle());
        evenement.setDescription(bean.getDescription());

        update("type_contibution_contribution");
        update("type_contibution_contribution_");
        update("data_contribution_evenement");
    }

    @Override
    public void resetFiche() {
        resetFiche(evenement);
        evenement.setEtat(Constantes.ETAT_EDITABLE);

        evenement.setDateClotureContribution(new Date());
        evenement.setDateEvenement(new Date());
        evenement.setDateOuvertureContribution(new Date());
        evenement.setHeureDebut(new Date());
        evenement.setHeureFin(new Date());
        evenement.setDateDebut(new Date());
        evenement.setDateFin(new Date());
        evenement.setMutualiste(new Mutualiste());
        evenement.setType(new TypeEvenement());
        evenement.setLieu(new Dictionnaire());

        evenement.setActivites_all(new ArrayList<YvsMutActivite>());
        evenement.setActivites(new ArrayList<YvsMutActivite>());
        evenement.setParticipants(new ArrayList<YvsMutParticipantEvenement>());
        evenement.setFinancements(new ArrayList<YvsMutFinancementActivite>());
        evenement.setContributions(new ArrayList<YvsMutContributionEvenement>());
        evenement.setCouts(new ArrayList<YvsMutCoutEvenement>());

        evenement.setStartDate(new Date());
        evenement.setEndDate(new Date());
        evenement.setTitle("");
        evenement.setDescription("");
        evenement.setCaisse(new CaisseMutuelle());

        tabIds = "";
        entityEvenement = new YvsMutEvenement();

        resetFicheParticipant();
        resetFicheFinance();
        update("blog_form_evenement");
    }

    public void resetFicheParticipant() {
        participant = new ParticipantEvenement();
//        loadParticipant(entityEvenement, null);
        update("tabview_evt:form_participant_evenement");
    }

    public void resetFicheFinance() {
        finance = new FinancementActivite();
        loadFinancement(entityEvenement, null);
        update("tabview_evt:form_financement_evenement");
    }

    @Override
    public boolean saveNew() {
        String action = evenement.getIdEvt() > 0 ? "Modification" : "Insertion";
        try {
            Evenement bean = recopieView();
            if (controleFiche(bean)) {
                entityEvenement = UtilMut.buildEvenement(bean, currentUser);
                if (entityEvenement.getId() < 1) {
                    entityEvenement.setId(null);
                    entityEvenement = (YvsMutEvenement) dao.save1(entityEvenement);
                    bean.setIdEvt(entityEvenement.getId());
                    evenement.setIdEvt(entityEvenement.getId());
                    evenements.add(entityEvenement);
                    eventModel.addEvent(bean);
                    saveAllContribution();
                } else {
                    dao.update(entityEvenement);
                    evenements.set(evenements.indexOf(entityEvenement), entityEvenement);
                    eventModel.updateEvent(bean);
                    updateAllContrib();
                }
                succes();
                actionOpenOrResetAfter(this);
                update("data_evenement");
                update("schedule");
            }
        } catch (Exception ex) {
            getException("Error " + action + " : " + ex.getMessage(), ex);
            getErrorMessage(action + " Impossible !");
        }
        return true;
    }

    public boolean saveNewContribution() {
        String action = contribution.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(contribution)) {
                YvsMutContributionEvenement y = UtilMut.buildContributionEvenement(contribution, currentUser);
                y.setId(null);
                dao.save1(y);
                y.setId(contribution.getId());

                if ((y.getMontant() + y.getMontantVerse()) >= evenement.getMontantObligatoire()) {
                    evenement.getContributions().add(y);
                } else {
                    y.setMontantVerse(y.getMontantVerse() + y.getMontant());
                    int idx = evenement.getContributions().indexOf(y);
                    if (idx > -1) {
                        evenement.getContributions().get(idx).setMontantVerse(y.getMontantVerse());
                        evenement.getContributions().get(idx).setEtat(Constantes.ETAT_ENCOURS);
                    }
                }
                int idx = evenements.indexOf(entityEvenement);
                if (idx > -1) {
                    evenements.get(idx).getContributions().add(y);
                }
                succes();
            }
        } catch (Exception ex) {
            getException("Error " + action + " : " + ex.getMessage(), ex);
            getErrorMessage(action + " Impossible !");
        }
        return true;
    }

    public boolean saveNewParticipant() {
        String action = participant.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(participant)) {
                YvsMutParticipantEvenement y = UtilMut.buildParticipantEvenement(participant, currentUser);
                if (y.getId() < 1) {
                    y.setId(null);
                    y = (YvsMutParticipantEvenement) dao.save1(y);
                } else {
                    dao.update(y);
                }
                int id = activite.getParticipants().indexOf(y);
                if (id > -1) {
                    activite.getParticipants().set(id, y);
                } else {
                    activite.getParticipants().add(0, y);
                }
                id = evenements.indexOf(entityEvenement);
                if (id > -1) {
                    int idx = evenements.get(id).getActivites().indexOf(new YvsMutActivite(activite.getId()));
                    if (idx > -1) {
                        int ind = evenements.get(id).getActivites().get(idx).getParticipants().indexOf(y);
                        if (ind > -1) {
                            evenements.get(id).getActivites().get(idx).getParticipants().set(ind, y);
                        } else {
                            evenements.get(id).getActivites().get(idx).getParticipants().add(0, y);
                        }
                    }
                }
                resetFicheParticipant();
                succes();
                update("tabview_evt:data_participant_evenement");
            }
        } catch (Exception ex) {
            getException("Error " + action + " : " + ex.getMessage(), ex);
            getErrorMessage(action + " Impossible !");
        }
        return true;
    }

    public boolean saveNewFinance() {
        String action = finance.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(finance)) {
                YvsMutFinancementActivite y = UtilMut.buildFinancementActivite(finance, currentUser);
                if (y.getId() < 1) {
                    y.setId(null);
                    y = (YvsMutFinancementActivite) dao.save1(y);
                } else {
                    dao.update(y);
                }
                int id = activite.getFinancements().indexOf(y);
                if (id > -1) {
                    activite.getFinancements().set(id, y);
                } else {
                    activite.getFinancements().add(0, y);
                }
                id = evenements.indexOf(entityEvenement);
                if (id > -1) {
                    int idx = evenements.get(id).getActivites().indexOf(new YvsMutActivite(activite.getId()));
                    if (idx > -1) {
                        int ind = evenements.get(id).getActivites().get(idx).getFinancements().indexOf(y);
                        if (ind > -1) {
                            evenements.get(id).getActivites().get(idx).getFinancements().set(ind, y);
                        } else {
                            evenements.get(id).getActivites().get(idx).getFinancements().add(0, y);
                        }
                    }
                }
                resetFicheFinance();
                succes();
                update("tabview_evt:data_financement_evenement");
            }
        } catch (Exception ex) {
            getException("Error " + action + " : " + ex.getMessage(), ex);
            getErrorMessage(action + " Impossible !");
        }
        return true;
    }

    public void saveNewActivite(YvsMutActivite y) {
        try {
            if (y != null ? y.getId() != null : false) {
                YvsMutEvenement evt = entityEvenement;
                if (evt != null ? (evt.getId() != null ? evt.getId() > 0 : false) : false) {
                    y.setEvenement(evt);
                    if (!y.isNew_()) {
                        dao.update(y);
                    } else {
                        long id = y.getId();
                        y.setId(null);
                        y = (YvsMutActivite) dao.save1(y);
                        y.setNew_(false);
                        int idx = evenement.getActivites_all().indexOf(new YvsMutActivite(id));
                        if (idx > -1) {
                            evenement.getActivites_all().set(idx, y);
                        }
                    }
                    int idx = evenement.getActivites().indexOf(y);
                    if (idx > -1) {
                        evenement.getActivites().set(idx, y);
                    } else {
                        evenement.getActivites().set(0, y);
                    }
                    int id = evenements.indexOf(entityEvenement);
                    if (id > -1) {
                        idx = evenements.get(id).getActivites().indexOf(y);
                        if (idx > -1) {
                            evenements.get(id).getActivites().set(idx, y);
                        } else {
                            evenements.get(id).getActivites().set(0, y);
                        }
                    }
                    succes();
                    update("data_activite_evenement");
                    update("tabview_evt:select_activite_evenement");
                    update("tabview_evt:select_activite_finance");
                }
            }
        } catch (Exception ex) {

        }
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? tabIds.length() > 0 : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsMutEvenement> list = new ArrayList<>();
                YvsMutEvenement bean;
                for (Long ids : l) {
                    bean = evenements.get(ids.intValue());
                    bean.setDateUpdate(new Date());
                    bean.setAuthor(currentUser);
                    list.add(bean);
                    dao.delete(bean);
                }
                evenements.removeAll(list);
                tabIds = "";
                resetFiche();
                succes();
                update("data_evenement");

            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression : " + ex.getMessage());
        }
    }

    public void deleteBean(YvsMutEvenement y) {
        try {
            if (y != null) {
                dao.delete(y);
                evenements.remove(y);
                eventModel.deleteEvent(new Evenement(y.getId()));
                if (y.getId() == evenement.getIdEvt()) {
                    resetFiche();
                }
                succes();
                update("data_evenement");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression : " + ex.getMessage());
        }
    }

    public void deleteBeanActivite(YvsMutActivite y) {
        try {
            if (y != null) {
                dao.delete(y);
                y.setNew_(true);
                int idx = evenement.getActivites_all().indexOf(y);
                if (idx > -1) {
                    y.setId((long) y.getId() - 1000);
                    evenement.getActivites_all().set(idx, y);
                }
                evenement.getActivites().remove(y);
                int id = evenements.indexOf(entityEvenement);
                if (id > -1) {
                    evenements.get(id).getActivites().remove(y);
                }
                succes();
                update("data_activite_evenement");
                update("tabview_evt:select_activite_evenement");
                update("tabview_evt:select_activite_finance");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression : " + ex.getMessage());
        }
    }

    public void deleteBeanParticipant(YvsMutParticipantEvenement y) {
        try {
            if (y != null) {
                dao.delete(y);
                activite.getParticipants().remove(y);
                int id = evenements.indexOf(entityEvenement);
                if (id > -1) {
                    int idx = evenements.get(id).getActivites().indexOf(new YvsMutActivite(activite.getId()));
                    if (idx > -1) {
                        evenements.get(id).getActivites().get(idx).getParticipants().remove(y);
                    }
                }
                if (y.getId() == participant.getId()) {
                    resetFicheParticipant();
                }
                succes();
                update("tabview_evt:data_participant_evenement");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression : " + ex.getMessage());
        }
    }

    public void deleteBeanFinance(YvsMutFinancementActivite y) {
        try {
            if (y != null) {
                dao.delete(y);
                activite.getFinancements().remove(y);
                int id = evenements.indexOf(entityEvenement);
                if (id > -1) {
                    int idx = evenements.get(id).getActivites().indexOf(new YvsMutActivite(activite.getId()));
                    if (idx > -1) {
                        evenements.get(id).getActivites().get(idx).getFinancements().remove(y);
                    }
                }
                if (y.getId() == finance.getId()) {
                    resetFicheFinance();
                }
                succes();
                update("tabview_evt:data_financement_evenement");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression : " + ex.getMessage());
        }
    }

    @Override
    public void updateBean() {

    }

//    public void addContribution(YvsMutContributionEvenement y) {
//        if (y != null) {
//            ContributionEvenement bean = UtilMut.buildBeanContributionEvenement(y);
//            cloneObject(contribution, bean);
//            contribution.setMontant(evenement.getMontantObligatoire() - y.getMontantVerse());
//            if (y.getCompte().getSolde() < contribution.getMontant()) {
//                openDialog("dlgSetMontant");
//            }
//        }
//    }
    @Override
    public void onSelectObject(YvsMutEvenement y) {
        entityEvenement = y;
        populateView(UtilMut.buildBeanEvenement(entityEvenement));
        evenement.setContributions(dao.loadNameQueries("YvsMutContributionEvenement.findByEvenement", new String[]{"evenement"}, new Object[]{entityEvenement}));
//        loadActivites(entityEvenement.getType());        
//        loadParticipant(entityEvenement, null);
        update("blog_form_evenement");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutEvenement y = (YvsMutEvenement) ev.getObject();
            onSelectObject((y));
            tabIds = evenements.indexOf(y) + "";
        }
    }

    public void loadOnViewParticipant(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutParticipantEvenement y = (YvsMutParticipantEvenement) ev.getObject();
            participant = UtilMut.buildBeanParticipantEvenement(y);
            update("tabview_evt:form_participant_evenement");
        }
    }

    public void loadOnViewFinance(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutFinancementActivite y = (YvsMutFinancementActivite) ev.getObject();
            finance = UtilMut.buildBeanFinancementActivite(y);
            update("tabview_evt:form_financement_evenement");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void unLoadOnViewParticipant(UnselectEvent ev) {
        resetFicheParticipant();
    }

    public void unLoadOnViewFinance(UnselectEvent ev) {
        resetFicheFinance();
    }

    public void loadOnViewMutualiste(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutMutualiste bean = (YvsMutMutualiste) ev.getObject();
            chooseMutualiste(UtilMut.buildBeanMutualiste(bean));
        }
    }

    public void loadOnViewMutualisteParticipant(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutMutualiste bean = (YvsMutMutualiste) ev.getObject();
            chooseParticipant(bean);
        }
    }

    public void loadOnViewActivity(RowEditEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsMutActivite y = (YvsMutActivite) ev.getObject();
            if (y != null ? y.getId() != null : false) {

            }
        }
    }

    public void searchMutualiste() {
        String num = evenement.getMutualiste().getMatricule();
        evenement.getMutualiste().setId(0);
        evenement.getMutualiste().setError(true);
        evenement.getMutualiste().setEmploye(new Employe());
        ManagedMutualiste m = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
        if (m != null) {
            Mutualiste y = m.searchMutualiste(num, true);
            if (m.getMutualistes() != null ? !m.getMutualistes().isEmpty() : false) {
                if (m.getMutualistes().size() > 1) {
                    update("data_mutualiste_evt");
                } else {
                    chooseMutualiste(y);
                }
                evenement.getMutualiste().setError(false);
            }
        }
    }

    public void searchParticipant() {
        String num = participant.getMutualiste().getMatricule();
        participant.getMutualiste().setId(0);
        participant.getMutualiste().setError(true);
        participant.getMutualiste().setEmploye(new Employe());
        ManagedMutualiste m = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
        if (m != null) {
            Mutualiste y = m.searchMutualiste(num, false);
            if (m.getMutualistes() != null ? !m.getMutualistes().isEmpty() : false) {
                if (m.getMutualistes().size() > 1) {
                    update("data_mutualiste_participant");
                    openDialog("dlgListParticipant");
                } else {
//                    chooseParticipant(y);
                }
                participant.getMutualiste().setError(false);
            }
        }
    }

    public void initMutualistes() {
        ManagedMutualiste m = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
        if (m != null) {
            m.initMutualistes(evenement.getMutualiste());
        }
        update("data_mutualiste_evt");
    }

    public void initParticipants() {
        ManagedMutualiste m = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
        if (m != null) {
            m.initMutualistes(participant.getMutualiste());
        }
        update("data_mutualiste_participant");
    }

    public void chooseMutualiste(Mutualiste bean) {
        evenement.setMutualiste(bean);
        update("txt_mutualiste_evenement");
        update("txt_mutualiste_evenement_");
    }

    public void chooseParticipant(YvsMutMutualiste bean) {
        YvsMutContributionEvenement con = (YvsMutContributionEvenement) dao.loadOneByNameQueries("YvsMutContributionEvenement.findByMutualiste", new String[]{"mutualiste"}, new Object[]{bean});
        if (con == null) {
            YvsMutCompte cpt = getComptesByType(bean.getComptes(), Constantes.MUT_TYPE_COMPTE_ASSURANCE);
            if (!containsCompte(cpt)) {
                if (cpt != null) {
                    con = new YvsMutContributionEvenement(-bean.getId());
                    con.setCompte(cpt);
                    con.setDateContribution(new Date());
                    con.setAuthor(currentUser);
                    con.setDateSave(new Date());
                    con.setDateUpdate(new Date());
                    con.setEtat(Constantes.ETAT_VALIDE);
                    con.setEvenement(new YvsMutEvenement(evenement.getIdEvt()));
                    con.setMontant(evenement.getMontantObligatoire());
                    con.setMontantVerse(evenement.getMontantObligatoire());
                    con.setRegle(false);
                    con.setNew_(true);
                    con.setId(null);
                    con = (YvsMutContributionEvenement) dao.save1(con);
                    evenement.getContributions().add(0, con);
                } else {
                    getErrorMessage("Ce mutualiste ne dispose d'aucun compte Assurance !");
                }
            }
        } else {
            int idx = evenement.getContributions().indexOf(con);
            if (idx >= 0) {
                con.setNew_(true);
                evenement.getContributions().add(0, con);
                evenement.getContributions().remove(idx);
            }
            //selectionne la ligne
        }
        update("data_tab_contribution");
    }

    public void saveAllContribution() {
        //Compte des mutualistes ayant un comptes assurance 
        String query = "SELECT c.id FROM yvs_mut_compte c INNER JOIN yvs_mut_mutualiste m ON m.id=c.mutualiste "
                + "INNER JOIN yvs_mut_type_compte tc ON tc.id=c.type_compte "
                + "WHERE m.assistance IS TRUE AND tc.mutuelle=? AND tc.type_compte=?";
        List<Long> re = dao.loadListBySqlQuery(query, new Options[]{new Options(currentMutuel.getId(), 1), new Options(Constantes.MUT_TYPE_COMPTE_ASSURANCE, 2)});
        saveAllContrib(re);
    }

//    public void addAllOthersParticipant() {
//        String query = "SELECT c.id FROM yvs_mut_contribution_evenement ce RIGHT JOIN yvs_mut_compte c ON c.id=ce.compte INNER JOIN yvs_mut_mutualiste m ON m.id=c.mutualiste "
//                + "INNER JOIN yvs_mut_type_compte tc ON tc.id=c.type_compte "
//                + "WHERE m.assistance IS TRUE AND ce.id IS NULL AND tc.mutuelle=? AND tc.type_compte=?";
//        List<Long> re = dao.loadListBySqlQuery(query, new Options[]{new Options(currentMutuel.getId(), 1), new Options(Constantes.MUT_TYPE_COMPTE_ASSURANCE, 2)});
//        System.err.println(" --- Build result---- "+re.size());
//        saveAllContrib(re);
//    }
    private void saveAllContrib(List<Long> ids) {
        if (!ids.isEmpty()) {
            List<YvsMutCompte> comptes = dao.loadNameQueries("YvsMutCompte.findByIds", new String[]{"ids"}, new Object[]{ids});
            YvsMutContributionEvenement con;
            for (YvsMutCompte c : comptes) {
                if (!containsCompte(c)) {
                    con = new YvsMutContributionEvenement(-c.getId());
                    con.setCompte(c);
                    con.setDateContribution(new Date());
                    con.setDateSave(new Date());
                    con.setDateUpdate(new Date());
                    con.setAuthor(currentUser);
                    con.setEtat(Constantes.ETAT_VALIDE);
                    con.setEvenement(new YvsMutEvenement(evenement.getIdEvt()));
                    con.setMontant(evenement.getMontantObligatoire());
                    con.setMontantVerse(evenement.getMontantObligatoire());
                    con.setRegle(false);
                    con.setNew_(true);
                    con.setId(null);
                    con = (YvsMutContributionEvenement) dao.save1(con);
                    evenement.getContributions().add(0, con);
                }
            }
        }
        update("data_tab_contribution");
    }

    public void updateAllContrib() {
        if (evenement.getIdEvt() > 0) {
            for (YvsMutContributionEvenement c : evenement.getContributions()) {
                if (c.getMontant() != evenement.getMontantObligatoire()) {
                    c.setMontantVerse(evenement.getMontantObligatoire());
                    c.setDateSave(new Date());
                    c.setDateUpdate(new Date());
                    c.setAuthor(currentUser);
                    if (c.getId() <= 0) {
                        c.setId(null);
                        c = (YvsMutContributionEvenement) dao.save1(c);
                    } else {
                        dao.update(c);
                    }
                }
            }
        }
    }

    private boolean containsCompte(YvsMutCompte c) {
        for (YvsMutContributionEvenement cc : evenement.getContributions()) {
            if (cc.getCompte().equals(c)) {
                return true;
            }
        }
        return false;
    }

//    public void saveAllContribution() {
//        if (evenement.getIdEvt() > 0) {
//            boolean notSaveAll = false;
//            for (YvsMutContributionEvenement c : evenement.getContributions()) {
//                if (c.getCompte() != null ? c.getCompte().getId() != null ? c.getCompte().getId() > 0 : false : false) {
//                    c.setDateUpdate(new Date());
//                    c.setAuthor(currentUser);
//                    if (c.getId() > 0) {
//                        dao.update(c);
//                    } else {
//                        c = (YvsMutContributionEvenement) dao.save1(c);
//                    }
//                } else {
//                    notSaveAll = true;
//                }
//            }
//            if (notSaveAll) {
//                getErrorMessage("Certaines ligne n'ont pas été enregistré car le compte assurance du mutualiste n'a pas été trouvé !");
//            }
//            succes();
//        } else {
//            getErrorMessage("Aucun évènement n'a été selectionné !");
//        }
//    }
    public void chooseType() {
        if (evenement.getType() != null ? evenement.getType().getId() > 0 : false) {
            ManagedTypeEvenement w = (ManagedTypeEvenement) giveManagedBean(ManagedTypeEvenement.class);
            if (w != null) {
                int idx = w.getTypes().indexOf(new YvsMutTypeEvenement(evenement.getType().getId()));
                if (idx > -1) {
                    YvsMutTypeEvenement t = w.getTypes().get(idx);
                    cloneObject(evenement.getType(), UtilMut.buildBeanTypeEvenement(t));
                }
            }
        } else {
            if (evenement.getType() != null ? evenement.getType().getId() == -1 : true) {
                openDialog("dlgTypeEvenement");
            }
        }
    }

//    public void chooseActivity(boolean financement) {
//        if (financement) {
//            if (finance.getActivite() != null ? finance.getActivite().getId() > 0 : false) {
//                int idx = evenement.getActivites().indexOf(new YvsMutActivite(finance.getActivite().getId()));
//                if (idx > -1) {
//                    YvsMutActivite t = evenement.getActivites().get(idx);
//                    cloneObject(finance.getActivite(), UtilMut.buildBeanActivite(t));
//                    loadFinancement(null, t);
//                }
//            } else {
//                loadFinancement(entityEvenement, null);
//            }
//        } else {
//            if (participant.getActivite() != null ? participant.getActivite().getId() > 0 : false) {
//                int idx = evenement.getActivites().indexOf(new YvsMutActivite(participant.getActivite().getId()));
//                if (idx > -1) {
//                    YvsMutActivite t = evenement.getActivites().get(idx);
//                    cloneObject(participant.getActivite(), UtilMut.buildBeanActivite(t));
//                    loadParticipant(null, t);
//                }
//            } else {
//                loadParticipant(entityEvenement, null);
//            }
//        }
//    }
    public void chooseVille() {
        if (evenement.getLieu() != null ? evenement.getLieu().getId() > 0 : false) {
            ManagedDico s = (ManagedDico) giveManagedBean("Mdico");
            if (s != null) {
                int idx = s.getVilles().indexOf(new YvsDictionnaire(evenement.getLieu().getId()));
                if (idx > -1) {
                    YvsDictionnaire d_ = s.getVilles().get(idx);
                    Dictionnaire d = UtilGrh.buildBeanDictionnaire(d_);
                    evenement.setLieu(d);
                }
            }
        } else {
            if (evenement.getLieu() != null ? evenement.getLieu().getId() == -1 : false) {
                openDialog("dlgAddVille");
            }
        }
    }

    public double montantAttendu() {
        if (evenement.getIdEvt() > 0) {
            Double soe = (Double) dao.loadObjectByNameQueries("YvsMutContributionEvenement.findSumByEvent", new String[]{"evenement"}, new Object[]{new YvsMutEvenement(evenement.getIdEvt())});
            return soe != null ? soe : 0;
        }
        return 0;
    }

    public double montantFinancement(YvsMutEvenement y) {
        Double montant = (Double) dao.loadObjectByNameQueries("YvsMutFinancementActivite.findFinancement", new String[]{"evenement"}, new Object[]{y});
        return montant != null ? montant : 0;
    }

    public void onEventSelect(SelectEvent selectEvent) {
        YvsMutEvenement bean = (YvsMutEvenement) selectEvent.getObject();
        populateView(UtilMut.buildBeanEvenement(bean));
        update("view_detail_evenement");
    }

    public void onDateSelect(SelectEvent selectEvent) {
        resetFiche();
        evenement.setDateDebut((Date) selectEvent.getObject());
        evenement.setDateFin((Date) selectEvent.getObject());
        update("view_detail_evenement");
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        Evenement bean = (Evenement) event.getScheduleEvent();
        if ((bean != null) ? bean.getIdEvt() > 0 : false) {
            int day = (int) event.getDayDelta();
            if (day != 0) {
                Calendar cal = Util.dateToCalendar(bean.getDateDebut());
                cal.add(Calendar.DATE, day);
                bean.setDateDebut(cal.getTime());
                cal = Util.dateToCalendar(bean.getDateFin());
                cal.add(Calendar.DATE, day);
                bean.setDateFin(cal.getTime());
                populateView(bean);
                saveNew();
            }
        }
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        Evenement bean = (Evenement) event.getScheduleEvent();
        if ((bean != null) ? bean.getIdEvt() > 0 : false) {
            int day = (int) event.getDayDelta();
            if (day != 0) {
                Calendar cal = Util.dateToCalendar(bean.getDateFin());
                cal.add(Calendar.DATE, day);
                bean.setDateFin(cal.getTime());
                populateView(bean);
                saveNew();
            }
        }
    }

    public void changeStatut(String etat) {
        if (!etat.equals("")) {
            String rq = "UPDATE yvs_mut_evenement SET etat = '" + etat + "' WHERE id=?";
            Options[] param = new Options[]{new Options(evenement.getIdEvt(), 1)};
            dao.requeteLibre(rq, param);
            evenement.setEtat(etat);
            entityEvenement.setEtat(etat);
            evenements.set(evenements.indexOf(entityEvenement), entityEvenement);
            succes();
            update("data_evenement");
        }
    }

    public void clotureEvt() {
        double total = evenement.getCoutTotal();
        if (total > 0) {
            boolean correct = true;
            ManagedMutualiste w = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
            if (w != null) {
                int nbr = w.getMutualistes().size();
                double montant = total / nbr;
                List<YvsMutTypeCredit> lTc = dao.loadNameQueries("YvsMutTypeCredit.findByMutuelleAssistance", new String[]{"mutuelle", "assistance"}, new Object[]{currentMutuel, true});
                if ((lTc != null) ? !lTc.isEmpty() : false) {
                    for (YvsMutMutualiste m : w.getMutualistes()) {
                        List<YvsMutCompte> lCp = dao.loadNameQueries("YvsMutCompte.findByMutualisteType", new String[]{"mutualiste", "type"}, new Object[]{currentMutuel, "Assistance"});
                        if ((lCp != null) ? !lCp.isEmpty() : false) {
                            YvsMutCredit eC = new YvsMutCredit();
                            eC.setCompte(lCp.get(0));
                            eC.setType(lTc.get(0));
                            eC.setDateCredit(new Date());
                            eC.setEtat(Constantes.ETAT_EDITABLE);
                            eC.setHeureCredit(new Date());
                            eC.setMontant(montant);
                            eC.setDateEffet(new Date());
                            eC.setId(null);
                            dao.save(eC);
                        } else {
                            String name = m.getEmploye().getPrenom() + " " + m.getEmploye().getNom();
                            getWarningMessage("Le mutualiste " + name + " n'a pas de compte assistance!");
                            correct = false;
                        }
                    }
                    if (correct) {
                        changeStatut(Constantes.ETAT_CLOTURE);
                    } else {
                        openDialog("dlgErrorCloture");
                        update("data_error_cloture");
                    }
                } else {
                    getErrorMessage("Le type de credit assistance n'est pas configuré...");
                }
            }
        } else {
            getWarningMessage("Aucun crédit possitionné...");
            changeStatut(Constantes.ETAT_CLOTURE);
        }
    }

    public void addParamMutualiste() {
        ParametreRequete p = new ParametreRequete("y.mutualiste.employe", "mutualiste", null, "LIKE", "AND");
        if (mutualisteSearch != null ? mutualisteSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "mutualiste", mutualisteSearch, "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.mutualiste.employe.matricule)", "mutualiste", mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.mutualiste.employe.nom)", "mutualiste", mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.mutualiste.employe.prenom)", "mutualiste", mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllEvenement(true, true);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateEvenement", "dates", null, "BETWEEN", "AND");
        if (dateSearch) {
            paginator.addParam(new ParametreRequete("y.dateEvenement", "dates", debutSearch, finSearch, "BETWEEN", "OR"));
            p = new ParametreRequete(null, "dates", dateSearch, "BETWEEN", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.dateDebut", "dates", debutSearch, finSearch, "BETWEEN", "AND"));
            p.getOtherExpression().add(new ParametreRequete("y.dateFin", "dates", debutSearch, finSearch, "BETWEEN", "AND"));
        }
        paginator.addParam(p);
        loadAllEvenement(true, true);
    }

    public void addParamType() {
        ParametreRequete p = new ParametreRequete("y.type", "type", null, "LIKE", "AND");
        if (typeSearch > 0) {
            p = new ParametreRequete("y.type", "type", new YvsMutTypeEvenement(typeSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAllEvenement(true, true);
    }

    public void addParamLieu() {
        ParametreRequete p = new ParametreRequete("y.lieu", "lieu", null, "=", "AND");
        if (lieuSearch > 0) {
            p = new ParametreRequete("y.lieu", "lieu", new YvsDictionnaire(lieuSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAllEvenement(true, true);
    }

}
