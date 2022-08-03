/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.ModeDeReglement;
import yvs.base.compta.UtilCompta;
import yvs.commercial.UtilCom;
import yvs.comptabilite.caisse.ManagedCaisses;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.grh.activite.YvsGrhCongeEmps;
import yvs.entity.grh.contrat.YvsGrhContratRestreint;
import yvs.entity.grh.contrat.YvsGrhContratSuspendu;
import yvs.entity.grh.contrat.YvsGrhElementsIndemnite;
import yvs.entity.grh.contrat.YvsGrhGrilleTauxFinContrat;
import yvs.entity.grh.contrat.YvsGrhLibelleDroitFinContrat;
import yvs.entity.grh.contrat.YvsGrhParamContrat;
import yvs.entity.grh.contrat.YvsGrhRubriqueIndemnite;
import yvs.entity.grh.param.YvsCalendrier;
import yvs.entity.grh.param.YvsGrhCategoriePreavis;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.grh.contrat.YvsGrhModelElementAdditionel;
import yvs.entity.grh.contrat.YvsGrhTypeElementAdditionel;
import yvs.entity.grh.param.YvsGrhCategorieProfessionelle;
import yvs.entity.grh.param.poste.YvsGrhModelContrat;
import yvs.entity.grh.param.poste.YvsGrhModelPrimePoste;
import yvs.entity.grh.param.poste.YvsGrhPosteDeTravail;
import yvs.entity.grh.personnel.YvsTypeContrat;
import yvs.entity.grh.salaire.YvsGrhBulletins;
import yvs.entity.grh.salaire.YvsGrhCategorieElement;
import yvs.entity.grh.salaire.YvsGrhDetailBulletin;
import yvs.entity.grh.salaire.YvsGrhDetailPrelevementEmps;
import yvs.entity.grh.salaire.YvsGrhElementSalaire;
import yvs.entity.grh.salaire.YvsGrhElementStructure;
import yvs.entity.grh.salaire.YvsGrhStructureSalaire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.print.YvsPrintContratEmployeHeader;
import yvs.entity.users.YvsNiveauAcces;
import yvs.grh.Calendrier;
import yvs.grh.JoursOuvres;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.contrat.ElementIndemnite;
import yvs.grh.contrat.FinDeContrats;
import yvs.grh.contrat.GrilleTauxFinContrat;
import yvs.grh.contrat.LibelleDroitFinContrat;
import yvs.grh.contrat.ParamContrat;
import yvs.grh.contrat.RubriqueIndemnite;
import yvs.grh.paie.PrelevementEmps;
import yvs.grh.paie.StructureElementSalaire;
import yvs.grh.paie.evalExpression.Lexemes;
import yvs.grh.paie.evalExpression.UtilFormules;
import yvs.init.Initialisation;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Util;
import yvs.util.Utilitaire;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class ManagedContratEmploye extends Managed<ContratEmploye, YvsGrhContratEmps> implements Serializable {

    @ManagedProperty(value = "#{contratEmploye}")
    private ContratEmploye currentContrat;
    private List<YvsGrhContratEmps> listContrat;
    YvsGrhContratEmps entityContrat;
    private List<YvsGrhStructureSalaire> listStructureSalaire;
    private List<YvsTypeContrat> listTypeContrat;
    private List<YvsBaseModeReglement> listModePaiement;
    private ModeDeReglement modeP = new ModeDeReglement();
    private String msgSearch, dureePause = "00:00", totalHeureHebdo = "00:00";
    int offsetEmps = 0, idTemp = 0;
    private String nameBtnVue = "Creation", nameBtnAddJour = "Ajouter";
    private boolean disPrevEmps = true, disNextEmps, optionUpdate, updateJoursOuvres, displayViewFile = true,
            vueListe = true, vueContractSelect = false, vue2 = false, selectModePaiement;
    private boolean skip, defautCalendar = false;
    private List<YvsGrhContratEmps> listSelectionContrat;
    private Calendrier calendrier = new Calendrier(), newCal = new Calendrier();
    private List<YvsCalendrier> listCalendrier;
    private JoursOuvres joursOuvres = new JoursOuvres();
    private String matriculeEmps;
    YvsCalendrier entityCalendrier;
    private ElementAdditionnel prime = new ElementAdditionnel();
    private String chaineSelecContrat, chaineSelectTypePrime, chaineSelectContratSus;
    private List<YvsGrhModelElementAdditionel> modelePrimes;
    private List<String> listTypesTranche;

    private boolean saveCascade = false;
    private boolean initForm = true;
    private boolean generer = false;

    private String choixSelect = "SINGLE";
    private String orderList = "y.employe.matricule";

    // constructeur du managedbean
    public ManagedContratEmploye() {
        listContrat = new ArrayList<>();
        listStructureSalaire = new ArrayList<>();
        listTypeContrat = new ArrayList<>();
        listSelectionContrat = new ArrayList<>();
        listModePaiement = new ArrayList<>();
        listCalendrier = new ArrayList<>();
        listFinContrat = new ArrayList<>();
        libelles = new ArrayList<>();
        listRubriqueIndemnite = new ArrayList<>();
        modelePrimes = new ArrayList<>();
        listTypesTranche = new ArrayList<>();
    }

    public List<String> getListTypesTranche() {
        return listTypesTranche;
    }

    public void setListTypesTranche(List<String> listTypesTranche) {
        this.listTypesTranche = listTypesTranche;
    }

    public Calendrier getNewCal() {
        return newCal;
    }

    public void setNewCal(Calendrier newCal) {
        this.newCal = newCal;
    }

    public List<YvsGrhModelElementAdditionel> getModelePrimes() {
        return modelePrimes;
    }

    public void setModelePrimes(List<YvsGrhModelElementAdditionel> modelePrimes) {
        this.modelePrimes = modelePrimes;
    }

    public String getChaineSelectContratSus() {
        return chaineSelectContratSus;
    }

    public void setChaineSelectContratSus(String chaineSelectContratSus) {
        this.chaineSelectContratSus = chaineSelectContratSus;
    }

    public String getChaineSelectTypePrime() {
        return chaineSelectTypePrime;
    }

    public void setChaineSelectTypePrime(String chaineSelectTypePrime) {
        this.chaineSelectTypePrime = chaineSelectTypePrime;
    }

    public String getChaineSelecContrat() {
        return chaineSelecContrat;
    }

    public void setChaineSelecContrat(String chaineSelecContrat) {
        this.chaineSelecContrat = chaineSelecContrat;
    }

    public ModeDeReglement getModeP() {
        return modeP;
    }

    public void setModeP(ModeDeReglement modeP) {
        this.modeP = modeP;
    }

    public boolean isSelectModePaiement() {
        return selectModePaiement;
    }

    public void setSelectModePaiement(boolean selectModePaiement) {
        this.selectModePaiement = selectModePaiement;
    }

    public boolean isDisplayViewFile() {
        return displayViewFile;
    }

    public void setDisplayViewFile(boolean displayViewFile) {
        this.displayViewFile = displayViewFile;
    }

    public String getMatriculeEmps() {
        return matriculeEmps;
    }

    public void setMatriculeEmps(String matriculeEmps) {
        this.matriculeEmps = matriculeEmps;
    }

    public String getDureePause() {
        return dureePause;
    }

    public void setDureePause(String dureePause) {
        this.dureePause = dureePause;
    }

    public String getTotalHeureHebdo() {
        return totalHeureHebdo;
    }

    public void setTotalHeureHebdo(String totalHeureHebdo) {
        this.totalHeureHebdo = totalHeureHebdo;
    }

    public boolean isDefautCalendar() {
        return defautCalendar;
    }

    public void setDefautCalendar(boolean defautCalendar) {
        this.defautCalendar = defautCalendar;
    }

    public boolean isUpdateJoursOuvres() {
        return updateJoursOuvres;
    }

    public List<YvsCalendrier> getListCalendrier() {
        return listCalendrier;
    }

    public void setListCalendrier(List<YvsCalendrier> listCalendrier) {
        this.listCalendrier = listCalendrier;
    }

    public void setUpdateJoursOuvres(boolean updateJoursOuvres) {
        this.updateJoursOuvres = updateJoursOuvres;
    }

    public String getNameBtnAddJour() {
        return nameBtnAddJour;
    }

    public void setNameBtnAddJour(String nameBtnAddJour) {
        this.nameBtnAddJour = nameBtnAddJour;
    }

    public JoursOuvres getJoursOuvres() {
        return joursOuvres;
    }

    public void setJoursOuvres(JoursOuvres joursOuvres) {
        this.joursOuvres = joursOuvres;
    }

    public YvsGrhContratEmps getEntityContrat() {
        return entityContrat;
    }

    public void setEntityContrat(YvsGrhContratEmps entityContrat) {
        this.entityContrat = entityContrat;
    }

    public Calendrier getCalendrier() {
        return calendrier;
    }

    public void setCalendrier(Calendrier calendrier) {
        this.calendrier = calendrier;
    }

    public ElementAdditionnel getPrime() {
        return prime;
    }

    public void setPrime(ElementAdditionnel prime) {
        this.prime = prime;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public boolean isOptionUpdate() {
        return optionUpdate;
    }

    public void setOptionUpdate(boolean optionUpdate) {
        this.optionUpdate = optionUpdate;
    }

    public List<YvsGrhContratEmps> getListSelectionContrat() {
        return listSelectionContrat;
    }

    public List<YvsBaseModeReglement> getListModePaiement() {
        return listModePaiement;
    }

    public void setListModePaiement(List<YvsBaseModeReglement> listModePaiement) {
        this.listModePaiement = listModePaiement;
    }

    public void setListSelectionContrat(List<YvsGrhContratEmps> listSelectionContrat) {
        this.listSelectionContrat = listSelectionContrat;
    }

    public List<YvsGrhStructureSalaire> getListStructureSalaire() {
        return listStructureSalaire;
    }

    public void setListStructureSalaire(List<YvsGrhStructureSalaire> listStructureSalaire) {
        this.listStructureSalaire = listStructureSalaire;
    }

    public String getNameBtnVue() {
        return nameBtnVue;
    }

    public void setNameBtnVue(String nameBtnVue) {
        this.nameBtnVue = nameBtnVue;
    }

    public List<YvsTypeContrat> getListTypeContrat() {
        return listTypeContrat;
    }

    public void setListTypeContrat(List<YvsTypeContrat> listTypeContrat) {
        this.listTypeContrat = listTypeContrat;
    }

    public boolean isVue2() {
        return vue2;
    }

    public void setVue2(boolean vue2) {
        this.vue2 = vue2;
    }

    public boolean isVueListe() {
        return vueListe;
    }

    public void setVueListe(boolean vueListe) {
        this.vueListe = vueListe;
    }

    public boolean isVueContractSelect() {
        return vueContractSelect;
    }

    public void setVueContractSelect(boolean vueContractSelect) {
        this.vueContractSelect = vueContractSelect;
    }

    public String getMsgSearch() {
        return msgSearch;
    }

    public void setMsgSearch(String msgSearch) {
        this.msgSearch = msgSearch;
    }

    public ContratEmploye getCurrentContrat() {
        return currentContrat;
    }

    public void setCurrentContrat(ContratEmploye currentContrat) {
        this.currentContrat = currentContrat;
    }

    public List<YvsGrhContratEmps> getListContrat() {
        return listContrat;
    }

    public void setListContrat(List<YvsGrhContratEmps> listContrat) {
        this.listContrat = listContrat;
    }

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

    public String getChoixSelect() {
        return choixSelect;
    }

    public void setChoixSelect(String choixSelect) {
        this.choixSelect = choixSelect;
    }

    public String getOrderList() {
        return orderList;
    }

    public void setOrderList(String orderList) {
        this.orderList = orderList;
    }

    private YvsJoursOuvres buildJoursOuvree(JoursOuvres j) {
        YvsJoursOuvres jour = new YvsJoursOuvres();
        if (j != null) {
            jour.setId(j.getId());
            jour.setActif(true);
            jour.setDureePause(j.getDureePause());
            jour.setHeureDebutPause((j.getHeureDebutPause() != null) ? j.getHeureDebutPause() : new Date());
            jour.setHeureDebutTravail((j.getHeureDebutTravail() != null) ? j.getHeureDebutTravail() : new Date());
            jour.setHeureFinPause((j.getHeureFinPause() != null) ? j.getHeureFinPause() : new Date());
            jour.setHeureFinTravail((j.getHeureFinTravail() != null) ? j.getHeureFinTravail() : new Date());
            jour.setJour(j.getJour());
            jour.setOuvrable(j.isOuvrable());
            jour.setJourDeRepos(j.isJourDerepos());
            jour.setCalendrier(entityCalendrier);
            jour.setDateSave(j.getDateSave());
            jour.setOrdre(UtilGrh.getOrdre(j.getJour()));
        }
        return jour;
    }

    public YvsCalendrier buildCalendrier(Calendrier c) {
        YvsCalendrier cal = new YvsCalendrier();
        if (c != null) {
            cal.setId(c.getId());
            cal.setReference(c.getReference());
            cal.setDefaut(false);
            cal.setSociete(currentAgence.getSociete());
            cal.setAuthor(currentUser);
            cal.setTempsMarge(c.getMarge());
            cal.setDateSave(c.getDateSave());
        }
        return cal;
    }

    public void loadViewEmploye(SelectEvent ev) {
        if (ev != null) {
            YvsGrhEmployes e = (YvsGrhEmployes) ev.getObject();
            choixEmploye1(e);
        }
    }

    public YvsGrhContratEmps findOneContrat(long id) {
        if (id > 0) {
            return (YvsGrhContratEmps) dao.loadOneByNameQueries("YvsGrhContratEmps.findById", new String[]{"id"}, new Object[]{id});
        }
        return null;
    }

    public void choixEmploye1(YvsGrhEmployes ev) {
        if (ev != null) {
            if (ev.getContrat() != null) {
                loadDataContrat(ev.getContrat());
            } else {
                currentContrat.setEmploye(UtilGrh.buildBeanSimplePartialEmploye(ev));
                if (ev.getConvention() != null) {
                    currentContrat.setSalaireHoraire(ev.getConvention().getSalaireHoraireMin());
                    currentContrat.setSalaireMensuel(ev.getConvention().getSalaireMin());
                } else {
                    getWarningMessage("Aucune classification catégorielle n'a été trouvé pour cet employé!", "");
                }
                currentContrat.setReference(genereReference());
                currentContrat.setDateDebut(ev.getDateEmbauche());
                currentContrat.setDateFin(ev.getDateArret());
                currentContrat.setActif(true);
                if (ev.getDateEmbauche() != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(ev.getDateEmbauche());
                    c.add(Calendar.MONTH, 3);//durée de la période d'éssaie par défaut
                    currentContrat.setFinEssai(c.getTime());
                }
                if (ev.getPosteActif() != null ? ev.getPosteActif().getId() > 0 : false) {
                    YvsGrhPosteDeTravail p = (YvsGrhPosteDeTravail) dao.loadOneByNameQueries("YvsPosteDeTravail.findById", new String[]{"id"}, new Object[]{ev.getPosteActif().getId()});
                    if (p != null ? (p.getModelContrat() != null ? p.getModelContrat().getId() > 0 : false) : false) {
                        YvsGrhModelContrat y = p.getModelContrat();
                        currentContrat.setSalaireHoraire(y.getSalaireBaseHoraire());
                        currentContrat.setSalaireMensuel(y.getSalaireBaseMensuel());
                        currentContrat.setCongeAcquis(y.getCongeAcquis());
                        currentContrat.setDureeMajoration(y.getMajorationConge());
                        if (y.getPreavis() != null ? y.getPreavis().getId() > 0 : false) {
                            currentContrat.setDureePreavie(y.getPreavis().getDuree().intValue());
                            if (y.getPreavis().getUnite() != null) {
                                currentContrat.setUnitePreavis(y.getPreavis().getUnite().getLibelle());
                                currentContrat.setUnitePreavisView(y.getPreavis().getUnite().getLibelle());
                            }
                        }
                        if (y.getPrimes() != null ? !y.getPrimes().isEmpty() : false) {
                            saveCascade = true;
                            for (YvsGrhModelPrimePoste m : y.getPrimes()) {
                                currentContrat.getPrimes().add(new YvsGrhElementAdditionel(null, m));
                            }
                        }
                    }
                }
            }
        }
    }

    public String genereReference() {
        int cpt = 0;
        String result = "CO_" + currentContrat.getEmploye().getMatricule() + "_" + cpt + "_" + new Date().getDay();
        for (YvsGrhContratEmps co : listContrat) {
            if (co.getReferenceContrat().equals(result)) {
                cpt = +1;
                result = "CO_" + currentContrat.getEmploye().getMatricule() + "_" + cpt + "_" + new Date().getDay();
            }
        }
        return result;
    }

    public void unLoadViewEmploye(SelectEvent ev) {
//        resetFiche(employe);
//        employe.setContrat(new Contrats());
    }

    public void closeDlgEmploye() {
        closeDialog("dlgEmploye");
        update("form-CE-02");
    }

    public void affichageVue() {
        if (!listSelectionContrat.isEmpty()) {
//            setVueListe(false);
//            vueContractSelect = false;
//            vue2 = true;
//            setUpdateContrat(true);
//            setNameBtnVue("Liste");
//            cloneObject(currentContrat, listSelectionContrat.get(listSelectionContrat.size() - 1));
//            entityContrat = buildContratEmps(listSelectionContrat.get(listSelectionContrat.size() - 1));
//            cloneObject(calendrier, currentContrat.getCalendrier());
//            currentContrat.setHoraireHebdo(calendrier.getTotalHeureHebdo());
//            entityCalendrier = buildCalendrier(calendrier);
//            listJoursOuvres.addAll(calendrier.getListJoursOuvres());
//            setDureePause("00:00");
//            setTotalHeureHebdo(Utilitaire.doubleToHour(currentContrat.getHoraireHebdo()));
//            update("Employes");
//            update("body_contrat");
//            update("form-CE-02");
        }
    }

    public void print(YvsGrhContratEmps y, YvsPrintContratEmployeHeader model) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (model != null ? model.getId() < 1 : true) {
                    model = (YvsPrintContratEmployeHeader) dao.loadOneByNameQueries("YvsPrintContratEmployeHeader.findByDefaut", new String[]{"societe", "defaut"}, new Object[]{currentAgence.getSociete(), true});
                    if (model != null ? model.getId() < 1 : true) {
                        getErrorMessage("Vous devez creer un model de contrat par défaut ");
                        return;
                    }
                }
                Map<String, Object> param = new HashMap<>();
                param.put("MODEL", model.getId().intValue());
                param.put("CONTRAT", y.getId().intValue());
                param.put("SUBREPORT_DIR", SUBREPORT_DIR(true));
                executeReport("contrat_employe", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedContratEmploye.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public void toogleView() {
//        vueListe = !vueListe;
//        setNameBtnVue((isVueListe()) ? "Creation" : "Liste");
//        resetFiche();
//        update("form-CE-02");
//        update("body_contrat");
//    }
    public void affichageVue1() {
        vueContractSelect = !vueContractSelect;
        update("boutton1");
    }

    public void deleteContrat() {
        try {
            if (chaineSelecContrat != null) {
                List<Long> l = decomposeIdSelection(chaineSelecContrat);
                List<YvsGrhContratEmps> list = new ArrayList<>();
                YvsGrhContratEmps y;
                for (Long ids : l) {
                    y = listContrat.get(ids.intValue());
                    list.add(y);
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.delete(y);
                    resetFiche();

                }
                succes();
                listContrat.removeAll(list);
                update("form_contrat_00");
            }

        } catch (Exception e) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + e.getMessage(), e);
        }
    }

    public void changeStatutContrat(String statut) {
        try {
            if (!autoriser("grh_contrat_change_statut")) {
                openNotAcces();
                return;
            }
            if (chaineSelecContrat != null) {
                List<Integer> l = decomposeSelection(chaineSelecContrat);
                for (Integer idx : l) {
                    YvsGrhContratEmps c = listContrat.get(idx);
                    onChangeStatut(c, statut);
                }
                update("form_contrat_00");
                succes();
            }

        } catch (Exception e) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + e.getMessage(), e);
        }
    }

    public void addContrat() {
        entityContrat = UtilGrh.buildBeanContratEmploye(currentContrat);
        entityContrat.setId(null);
        entityContrat = (YvsGrhContratEmps) dao.save1(entityContrat);
    }

    public boolean controleFicheCalendrier(Calendrier bean) {
        if (bean.getReference() == null || "".equals(bean.getReference())) {
            getMessage("Vous devez entrer la reference du calendrier", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void updateBean() {

    }

    public void saveNewCalendrier() {
        if (controleFicheCalendrier(newCal)) {
            if (!defautCalendar) {
                entityCalendrier = buildCalendrier(newCal);
                entityCalendrier.setDateUpdate(new Date());
                entityCalendrier.setId(null);
                entityCalendrier = (YvsCalendrier) dao.save1(entityCalendrier);
                listCalendrier.add(0, entityCalendrier);
                calendrier.setId(entityCalendrier.getId());
                calendrier.getListJoursOuvres().clear();
                setDefautCalendar(false);
            }
        }
    }

    public void saveNewJoursOuvree() {
        if (calendrier.getId() > 0) {
            if (controleFicheJoursOUvree(joursOuvres)) {
                YvsJoursOuvres entityJourOuvree = buildJoursOuvree(joursOuvres);
                entityJourOuvree.setDateUpdate(new Date());
                joursOuvres.setDureeService(Utilitaire.calculDureeBetweenDate(entityJourOuvree.getHeureDebutTravail(), entityJourOuvree.getHeureFinTravail()));
                joursOuvres.setDureePause(Utilitaire.calculDureeBetweenDate(entityJourOuvree.getHeureDebutPause(), entityJourOuvree.getHeureFinPause()));
                if (!isUpdateJoursOuvres()) {
                    entityJourOuvree.setAuthor(currentUser);
                    entityJourOuvree.setDateSave(new Date());
                    entityJourOuvree = (YvsJoursOuvres) dao.save1(entityJourOuvree);
                    joursOuvres.setId(entityJourOuvree.getId().intValue());
                    calendrier.getListJoursOuvres().add(entityJourOuvree);
//                listJoursOuvres.add(j);
                } else {
                    entityJourOuvree.setAuthor(currentUser);
                    dao.update(entityJourOuvree);
                    int idx = calendrier.getListJoursOuvres().indexOf(entityJourOuvree);
                    if (idx >= 0) {
                        calendrier.getListJoursOuvres().set(idx, entityJourOuvree);
                    }
//                listJoursOuvres.set(listJoursOuvres.indexOf(joursOuvres), j);                    
                }
                resetFicheJoursOuvres();
                update("detail_jour_travail_00");
                update("detail_calendier_00");
            }
        }
    }

    public void searchEmploye(String matricule) {
        if (matricule != null ? !matricule.trim().isEmpty() : false) {
            ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
            if (service != null) {
                service.addParamActif(true);
                service.findEmploye(matricule);
                if (service.getListEmployes().size() == 1) {
                    choixEmploye1(service.getListEmployes().get(0));
                } else if (service.getListEmployes().size() > 0) {
                    openDialog("dlgEmploye");
                    update("tabEmployes-contratEmps");
                }
            }
        }
    }

    @Override
    public boolean saveNew() {
        if (controleFiche(currentContrat)) {
            currentContrat.setCalendrier(calendrier);
            entityContrat = UtilGrh.buildBeanContratEmploye(currentContrat);
            int idx;
            if (currentContrat.getModePaiement().getId() > 0) {
                idx = listModePaiement.indexOf(new YvsBaseModeReglement((long) currentContrat.getModePaiement().getId()));
                if (idx >= 0) {
                    entityContrat.setModePaiement(listModePaiement.get(idx));
                }
            }
            if (currentContrat.getStructSalaire().getId() > 0) {
                idx = listStructureSalaire.indexOf(new YvsGrhStructureSalaire(currentContrat.getStructSalaire().getId()));
                if (idx >= 0) {
                    entityContrat.setStructureSalaire(listStructureSalaire.get(idx));
                }
            }
            if (currentContrat.getCalendrier().getId() > 0) {
                idx = listCalendrier.indexOf(new YvsCalendrier(currentContrat.getCalendrier().getId()));
                if (idx >= 0) {
                    entityContrat.setCalendrier(listCalendrier.get(idx));
                }
            }
            entityContrat.setAuthor(currentUser);
            entityContrat.setDateUpdate(new Date());
            if (currentContrat.getId() <= 0) {
                entityContrat.setDateSave(new Date());
                entityContrat.setId(null);
                entityContrat = (YvsGrhContratEmps) dao.save1(entityContrat);
                currentContrat.setId(entityContrat.getId());
                listContrat.add(0, entityContrat);
                if (saveCascade) {
                    for (YvsGrhElementAdditionel e : currentContrat.getPrimes()) {
                        e.setId(null);
                        e.setContrat(entityContrat);
                        e.setAuthor(currentUser);
                        e.setDateSave(new Date());
                        e.setDateUpdate(new Date());
                        e = (YvsGrhElementAdditionel) dao.save1(e);
                    }
                }
            } else {
                dao.update(entityContrat);
                if (listContrat.contains(entityContrat)) {
                    listContrat.set(listContrat.indexOf(entityContrat), entityContrat);
                }
            }
            succes();
            saveCascade = false;
            actionOpenOrResetAfter(this);
        }
        return true;
    }

    @Override
    public void resetFiche() {
        resetFiche(currentContrat);
        calendrier = new Calendrier();
        currentContrat.setEmploye(new Employe());
        currentContrat.setTypeContrat(new TypeContrat());
        currentContrat.setStructSalaire(new StructureElementSalaire());
        currentContrat.setModePaiement(new ModeDeReglement());
        currentContrat.setCalendrier(new Calendrier());
        currentContrat.setRegleTache(new RegleDeTache());
        currentContrat.getPrimes().clear();
        currentContrat.setAccesRestreint(false);
        currentContrat.setSourceFirstConge("DEX");
        saveCascade = false;
        entityContrat = new YvsGrhContratEmps();
        setDureePause("00:00");
        setTotalHeureHebdo("00:00");
        update("anel_contrat_donnee_00");
        update("footer_contrat");

    }

    public void resetFicheJoursOuvres() {
        resetFiche(joursOuvres);
        joursOuvres.setDureePause(new Date(0));
    }

    @Override
    public boolean controleFiche(ContratEmploye bean) {
        if (bean.getEmploye().getId() == 0) {
            getMessage("Vous devez choisir un employé", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getReference() == null) {
            getMessage("Vous devez spécifier la reference", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getDateDebut() == null) {
            getMessage("Vous devez spécifier la date de debut", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getDateFin() == null) {
            getMessage("Vous devez spécifier la date de fin", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getTypeContrat().getId() == 0) {
            getMessage("Vous devez spécifier le type de contrat", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getModePaiement().getId() == 0) {
            getErrorMessage("Votre formulaire est incomplet; veuillez indique le mode de paiement");
            return false;
        }
        if ((bean.getSourceFirstConge() != null) ? bean.getSourceFirstConge().equals("DF") : true && bean.getDateFirstConge() == null) {
            getErrorMessage("Le choix de la source d'evaluation du premier congé nécessite que vous entriez une date");
            return false;
        }
        return true;
    }

    public boolean controleFicheJoursOUvree(JoursOuvres bean) {
        if (bean.getHeureDebutTravail() == null) {
            getMessage("Vous devez entrer le nombre total de jours de congés permis", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getHeureFinTravail() == null) {
            getMessage("Vous devez entrer l'heure de fin du travail", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public ContratEmploye recopieView() {
        ContratEmploye c = new ContratEmploye();
        cloneObject(c, currentContrat);
        return c;
    }

    @Override
    public void populateView(ContratEmploye bean) {
        cloneObject(currentContrat, bean);
    }

    public void populateViewJours(JoursOuvres bean) {
        cloneObject(joursOuvres, bean);
        calculDureePause();
    }

    public void populateViewEmploye(Employe bean) {
//        employe.setId(bean.getId());
//        employe.setNom(bean.getNom());
//        employe.setPrenom(bean.getPrenom());
//        employe.setCodeEmploye(bean.getCodeEmploye());
//        employe.setMatricule(bean.getMatricule());
    }

    @Override
    public void onSelectObject(YvsGrhContratEmps y) {
        loadDataContrat(y); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        execute("collapseForm('contratEmp')");
        loadDataContrat((YvsGrhContratEmps) ev.getObject());
        chaineSelecContrat = "" + listContrat.indexOf((YvsGrhContratEmps) ev.getObject());
    }

    public void loadDataContrat(YvsGrhContratEmps c) {
        entityContrat = c;
        cloneObject(currentContrat, UtilGrh.buildBeanContratEmploye(c, true));
        currentContrat.setPrimes(dao.loadNameQueries("YvsGrhElementAdditionel.findPrimeByContrat", new String[]{"contrat"}, new Object[]{c}));
        if ((currentContrat != null) ? ((currentContrat.getCalendrier() != null) ? (currentContrat.getCalendrier().getId() > 0) : false) : false) {
            entityCalendrier = buildCalendrier(currentContrat.getCalendrier());
            calendrier = currentContrat.getCalendrier();
            calendrier.setListJoursOuvres(dao.loadNameQueries("YvsJoursOuvres.findByCalendrier", new String[]{"calendrier"}, new Object[]{new YvsCalendrier(currentContrat.getCalendrier().getId())}));
        } else {
            currentContrat.setCalendrier(new Calendrier());
            calendrier = new Calendrier();
        }
    }

    public void loadOnViewJours(SelectEvent ev) {
        JoursOuvres bean = (JoursOuvres) ev.getObject();
        populateViewJours(bean);
        setNameBtnAddJour("Modifier");
        setUpdateJoursOuvres(true);
        update("detail_jour_travail_00");
        update("tab_jours_ouvres");
    }

    public void unLoadOnViewJours(UnselectEvent ev) {
        resetFicheJoursOuvres();
        setUpdateJoursOuvres(false);
        setNameBtnAddJour("Ajouter");
        setDureePause("00:00");
        update("tabView_contrat:detail_jour_travail_00");
        update("tabView_contrat:tab_jours_ouvres");
    }

    @Override
    public void loadAll() {
        loadAllContrat(true);
        loadContratsRestreint();
        //charge la liste des contrats suspendu       
        listFinContrat = dao.loadNameQueries("YvsGrhContratSuspendu.findAll", new String[]{"societe"}, new Object[]{currentUser.getAgence().getSociete()});
        //charge la liste des règle des tâches
        loadParamContrat();
        loadLibelleIndemnite();
        //charge le modèle des primes
        modelePrimes = dao.loadNameQueries("YvsGrhModelElementAdditionel.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        //charger les types de tranches horaire disponible
        listTypesTranche = dao.loadNameQueries("YvsGrhTrancheHoraire.findTypeTranche", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadLibelleIndemnite() {
        libelles = UtilGrh.buildBeanLibelle(dao.loadNameQueries("YvsGrhLibelleDroitFinContrat.findAll", new String[]{"contrat"}, new Object[]{paramContrat}));
    }

    public void navigateInView(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbResult())) {
            setOffset(0);
        }
        String query = "YvsGrhContratEmps y LEFT JOIN FETCH y.contratSuspendu LEFT JOIN FETCH y.structureSalaire LEFT JOIN FETCH y.calendrier LEFT JOIN FETCH y.employe LEFT JOIN FETCH y.modePaiement LEFT JOIN FETCH y.typeContrat ";
        ParametreRequete p = new ParametreRequete("y.employe.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
        paginator.addParam(p);
        List<YvsGrhContratEmps> re = paginator.parcoursDynamicData(query, "y", "", "y.employe.matricule", getOffset(), dao);
        if (!re.isEmpty()) {
            loadDataContrat(re.get(0));
            update("employe-main-panel");
            update("delete_employe");
        }
    }

    public void loadAllContrat(boolean avancer) {
        String query = "YvsGrhContratEmps y JOIN FETCH y.employe JOIN FETCH y.employe.agence LEFT JOIN FETCH y.contratSuspendu LEFT JOIN FETCH y.structureSalaire LEFT JOIN FETCH y.calendrier LEFT JOIN FETCH y.employe LEFT JOIN FETCH y.modePaiement LEFT JOIN FETCH y.typeContrat ";
        ParametreRequete p = new ParametreRequete("y.employe.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
        paginator.addParam(p);
        listContrat = paginator.executeDynamicQuery("y", "y", query, orderList, avancer, initForm, (int) imax, dao);
    }

    public void changeOrderList(String order) {
        this.orderList = order;
        loadAllContrat(true);
    }

    public void loadAlModePaiement() {
        listModePaiement = dao.loadNameQueries("YvsBaseModeReglement.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllTypeContrat() {
        listTypeContrat.clear();
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listTypeContrat = dao.loadNameQueries("YvsTypeContrat.findAll", champ, val);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        initForm = true;
        loadAllContrat(true);
    }

    public void changeMaxResult(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            imax = (long) ev.getNewValue();
        }
        initForm = true;
        loadAllContrat(true);
    }

    public void pagineResult(boolean avancer) {
        initForm = false;
        loadAllContrat(true);
    }

    public void loadAllCalendrier() {
        champ = new String[]{"societe"};
        val = new Object[]{currentUser.getAgence().getSociete()};
        listCalendrier = dao.loadNameQueries("YvsCalendrier.findBySociete", champ, val);
    }

    public void loadAllStructureSalaire() {
        champ = new String[]{"societe"};
        val = new Object[]{currentUser.getAgence().getSociete()};
        listStructureSalaire = dao.loadNameQueries("YvsStructureSalaire.findAll", champ, val);
    }

    /**
     * Ajouter un nouveau type de contrat
     */
    private String newTypeContrat;

    public String getNewTypeContrat() {
        return newTypeContrat;
    }

    public void setNewTypeContrat(String newTypeContrat) {
        this.newTypeContrat = newTypeContrat;
    }

    public void createNewTypeContrat() {
        if (newTypeContrat != null) {
            YvsTypeContrat tc = new YvsTypeContrat();
            tc.setLibelle(newTypeContrat);
            tc.setSociete(currentAgence.getSociete());
            tc.setSupp(false);
            tc.setActif(true);
            tc.setAuthor(currentUser);
            tc.setDateSave(new Date());
            tc.setDateUpdate(new Date());
            tc = (YvsTypeContrat) dao.save1(tc);
            TypeContrat t = new TypeContrat(tc.getId(), newTypeContrat);
            listTypeContrat.add(tc);
            currentContrat.setTypeContrat(t);
            newTypeContrat = null;
        }
    }

    public void loadOnViewModePaiement(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseModeReglement mode = (YvsBaseModeReglement) ev.getObject();
            modeP = UtilCom.buildBeanModeReglement(mode);
            selectModePaiement = true;
            currentContrat.setModePaiement(modeP);
        }
    }

    public void unLoadOnViewModePaiement(UnselectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            resetFiche(modeP);
            selectModePaiement = false;
            update("body_dlg_mode_paiement");
            update("footer_dlg_mode_paiement");
        }
    }

    public void createNewModePaiement() {

    }

    public void deleteModePaiement(YvsBaseModeReglement mode) {
        try {
            mode.setAuthor(currentUser);
            dao.delete(mode);
            listModePaiement.remove(mode);
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cette ligne !");
            getException("Lymytz Error...", ex);
        }
    }

    public void closeDlgModePaiement() {
        currentContrat.setModePaiement(modeP);
        update("form_contrat_01");
    }

    public void chooseCaisse() {
        if (currentContrat.getCaisse() != null ? currentContrat.getCaisse().getId() > 0 : false) {
            ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (w != null) {
                int idx = w.getCaisses().indexOf(new YvsBaseCaisse(currentContrat.getCaisse().getId()));
                if (idx > -1) {
                    YvsBaseCaisse y = w.getCaisses().get(idx);
                    currentContrat.setCaisse(UtilCompta.buildBeanCaisse(y));
                }
            }
        }
    }

//    public void deleteBeanModePaiement(YvsModePaiement m) {
//        try {
//            m.setAuthor(currentUser);
//            dao.delete(m.getId());
//            listModePaiement.remove(m);
//        } catch (Exception ex) {
//            getErrorMessage("Impossible de supprimer cet élément !");
//            getException("Lymytz Error...", ex);
//        }
//    }
    /**
     * **********AJOUTER DES PRIMES AU CONTRAT***************
     */
    private List<YvsGrhTypeElementAdditionel> listTypePrime = new ArrayList<>(), listTypePrimeToImport = new ArrayList<>();
    private List<YvsGrhTypeElementAdditionel> selectionTypePrime;
    private TypeElementAdd newType = new TypeElementAdd();
    private YvsGrhElementAdditionel selectedElt;
    private boolean displayBtnDel;
    private boolean viewListPrime, displayBtnUpPrime, displayBtnDelPrime, updateTypePrime;
    private boolean updatePrime;
    private YvsGrhModelElementAdditionel selectedModelElt;

    public boolean isDisplayBtnDel() {
        return displayBtnDel;
    }

    public boolean isViewListPrime() {
        return viewListPrime;
    }

    public void setViewListPrime(boolean viewListPrime) {
        this.viewListPrime = viewListPrime;
    }

    public boolean isDisplayBtnUpPrime() {
        return displayBtnUpPrime;
    }

    public void setDisplayBtnUpPrime(boolean displayBtnUpPrime) {
        this.displayBtnUpPrime = displayBtnUpPrime;
    }

    public boolean isDisplayBtnDelPrime() {
        return displayBtnDelPrime;
    }

    public void setDisplayBtnDelPrime(boolean displayBtnDelPrime) {
        this.displayBtnDelPrime = displayBtnDelPrime;
    }

    public void setDisplayBtnDel(boolean displayBtnDel) {
        this.displayBtnDel = displayBtnDel;
    }

    public YvsGrhElementAdditionel getSelectedElt() {
        return selectedElt;
    }

    public void setSelectedElt(YvsGrhElementAdditionel selectedElt) {
        this.selectedElt = selectedElt;
    }

    public YvsGrhModelElementAdditionel getSelectedModelElt() {
        return selectedModelElt;
    }

    public void setSelectedModelElt(YvsGrhModelElementAdditionel selectedModelElt) {
        this.selectedModelElt = selectedModelElt;
    }

    public void setListTypePrime(List<YvsGrhTypeElementAdditionel> listTypePrime) {
        this.listTypePrime = listTypePrime;
    }

    public List<YvsGrhTypeElementAdditionel> getListTypePrime() {
        return listTypePrime;
    }

    public TypeElementAdd getNewType() {
        return newType;
    }

    public void setNewType(TypeElementAdd newType) {
        this.newType = newType;
    }

    public List<YvsGrhTypeElementAdditionel> getSelectionTypePrime() {
        return selectionTypePrime;
    }

    public void setSelectionTypePrime(List<YvsGrhTypeElementAdditionel> selectionTypePrime) {
        this.selectionTypePrime = selectionTypePrime;
    }

    public void setListTypePrimeToImport(List<YvsGrhTypeElementAdditionel> listTypePrimeToImport) {
        this.listTypePrimeToImport = listTypePrimeToImport;
    }

    public List<YvsGrhTypeElementAdditionel> getListTypePrimeToImport() {
        return listTypePrimeToImport;
    }

    public boolean isUpdatePrime() {
        return updatePrime;
    }

    public void setUpdatePrime(boolean updatePrime) {
        this.updatePrime = updatePrime;
    }

    public void saveRegleSalaire(YvsGrhTypeElementAdditionel prime) {
        try {
            YvsGrhCategorieElement cat = (YvsGrhCategorieElement) dao.loadOneByNameQueries("YvsGrhCategorieElement.findByPrime", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            if (cat != null ? cat.getId() > 0 : false) {
                YvsGrhElementSalaire element = new YvsGrhElementSalaire();
                element.setActif(true);
                element.setCode(prime.getCodeElement());
                element.setNom(prime.getLibelle());
                element.setCategorie(cat);
                element.setTypeMontant(3);
                element.setExpressionRegle("contrat." + prime.getCodeElement());
                element.setAuthor(currentUser);
                element.setDateSave(new Date());
                element.setDateUpdate(new Date());
                element.setRetenue(false);
                element.setVisibleBulletin(true);
                element = (YvsGrhElementSalaire) dao.save1(element);
                List<YvsGrhStructureSalaire> list = dao.loadNameQueries("YvsStructureSalaire.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                if (!list.isEmpty()) {
                    for (YvsGrhStructureSalaire s : list) {
                        YvsGrhElementStructure el = new YvsGrhElementStructure();
                        el.setActif(true);
                        el.setAuthor(currentUser);
                        el.setDateSave(new Date());
                        el.setDateUpdate(new Date());
                        el.setStructure(s);
                        el.setElement(element);
                        dao.save(el);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("erreur = " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addTypePrime() {
        if (newType.getCode() != null) {
            //Contrôle l'unicité du code
            YvsGrhTypeElementAdditionel old = (YvsGrhTypeElementAdditionel) dao.loadOneByNameQueries("YvsGrhTypeElementAdditionel.findByCodeElement", new String[]{"codeElement", "societe"}, new Object[]{newType.getCode(), currentAgence.getSociete()});
            YvsGrhTypeElementAdditionel ty = new YvsGrhTypeElementAdditionel();
            ty.setCodeElement(newType.getCode());
            ty.setLibelle(newType.getLibelle());
            ty.setSociete(currentAgence.getSociete());
            ty.setActif(newType.isActif());
            ty.setRetenue(false);
            ty.setAuthor(currentUser);
            ty.setDateUpdate(new Date());
            if (newType.getId() <= 0) {
                if (old != null) {
                    getErrorMessage("Une règle de prime existe déjà avec ce code !");
                    return;
                }
                ty.setDateSave(new Date());
                ty = (YvsGrhTypeElementAdditionel) dao.save1(ty);
                if (generer) {
                    saveRegleSalaire(ty);
                }
                newType.setId(ty.getId());
                listTypePrime.add(0, ty);
            } else {
                if (old != null ? old.getId() != newType.getId() : false) {
                    getErrorMessage("Une autre règle de prime existe déjà avec ce code !");
                    return;
                }
                ty.setId(newType.getId());
                dao.update(ty);
                listTypePrime.set(listTypePrime.indexOf(ty), ty);
            }
            prime.setTypeElt(newType);
            resetFicheTypePrime();
            update("form-add-prime");
        }
    }

    public void resetFicheTypePrime() {
        newType = new TypeElementAdd();

    }

    public void openSelectedTypePrime(YvsGrhTypeElementAdditionel type, boolean del) {
        newType = UtilGrh.buildTypeElt(type);
        if (del) {
            openDialog("dlgDelTypePrime");
        } else {
            updateTypePrime = true;
        }
        update("form_createTypeP");
    }

    public void toogleActiveSelectedTypePrime(YvsGrhTypeElementAdditionel type) {
        type.setActif(!type.getActif());
        type.setAuthor(currentUser);
        type.setDateUpdate(new Date());
        dao.update(type);
        update("form-add-prime");
    }

    public void deleteOneTypePrime() {
        if (newType.getId() > 0) {
            try {
                YvsGrhTypeElementAdditionel ty = new YvsGrhTypeElementAdditionel(newType.getId());
                ty.setAuthor(currentUser);
                dao.delete(ty);
                listTypePrime.remove(ty);
                newType = new TypeElementAdd();
                update("table_contrat_prime");
                succes();
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cet élément !");
                getException("Lymytz Error", ex);
            }
        }
    }

    public void loadTypePrime() {
        champ = new String[]{"societe", "retenue"};
        val = new Object[]{currentAgence.getSociete(), false};
        listTypePrime = dao.loadNameQueries("YvsGrhTypeElementAdditionel.findAll", champ, val);
    }

    public void addPrime() {
        if (currentContrat != null && prime.getTypeElt() != null) {
            if (currentContrat.getId() > 0 && prime.getTypeElt().getId() > 0 && prime.getMontant() >= 0) {
                if (!prime.isPermanent() && (prime.getDebut() == null || prime.getFin() == null)) {

                    getErrorMessage("Formulaire incorrecte ! les dates sont erronnées");
                    return;
                } else if ((prime.getDebut() != null && prime.getFin() != null && !prime.isPermanent()) ? (prime.getDebut().after(prime.getFin())) : false) {
                    getErrorMessage("Formulaire incorrecte ! les dates  sont erronnées");
                    return;
                }
                //s'assurer que la prime n'a pas éjà été attribuer
                if (!updatePrime) {
                    for (YvsGrhElementAdditionel ea : currentContrat.getPrimes()) {
                        if (Objects.equals(ea.getTypeElement().getId(), prime.getTypeElt().getId())) {
                            getErrorMessage("Cette prime a déjà été attribuer");
                            return;
                        }
                    }
                }
                YvsGrhElementAdditionel el = new YvsGrhElementAdditionel(prime.getId());
                el.setTypeElement(listTypePrime.get(listTypePrime.indexOf(new YvsGrhTypeElementAdditionel(prime.getTypeElt().getId()))));
                el.setDescription(prime.getDescription());
                el.setMontantElement(prime.getMontant());
                el.setPermanent(prime.isPermanent());
                el.setContrat(new YvsGrhContratEmps(currentContrat.getId()));
                el.setPlanifier(false);
                el.setDateElement(new Date());
                el.setAuthor(currentUser);
                if (!prime.isPermanent()) {
                    el.setDateDebut(prime.getDebut());
                    el.setDateFin(prime.getFin());
                } else {
                    prime.setDebut(null);
                    prime.setFin(null);
                }
                if (!updatePrime) {
                    el.setId(null);
                    el = (YvsGrhElementAdditionel) dao.save1(el);
                    prime.setId(el.getId());
                } else {
                    dao.update(el);
                }
//                prime.setTypeElt(UtilGrh.buildTypeElt(el));
                if (!currentContrat.getPrimes().contains(el)) {
                    currentContrat.getPrimes().add(0, el);
                } else {
                    currentContrat.getPrimes().set(currentContrat.getPrimes().indexOf(el), el);
                }
                newType = new TypeElementAdd();
                prime = new ElementAdditionnel();
                updatePrime = false;
                succes();
            } else {
                getErrorMessage("Formulaire incorrecte !");
            }
        }
    }

    public void importPrimeFromModele() {
        boolean notInsert = false;
        int nbNotInsert = 0;
        if (currentContrat != null) {
            if (currentContrat.getId() > 0) {
                for (YvsGrhModelElementAdditionel e : modelePrimes) {
                    notInsert = false;
                    //vérifi qu'un élément de même type n'est pas déjà rattaché au contrat
                    for (YvsGrhElementAdditionel el : currentContrat.getPrimes()) {
                        if (e.getTypeElement().equals(el.getTypeElement())) {
                            notInsert = true;
                            nbNotInsert++;
                        }
                    }
                    if (notInsert) {
                        continue;
                    }
                    YvsGrhElementAdditionel el = new YvsGrhElementAdditionel(null);
                    el.setTypeElement(e.getTypeElement());
                    el.setDescription(e.getDescription());
                    el.setMontantElement(e.getMontantElement());
                    el.setPermanent(e.getPermanent());
                    el.setContrat(new YvsGrhContratEmps(currentContrat.getId()));
                    el.setPlanifier(false);
                    el.setDateElement(new Date());
                    el.setDateDebut(e.getDateDebut());
                    el.setDateFin(e.getDateFin());
                    el.setAuthor(currentUser);
                    el = (YvsGrhElementAdditionel) dao.save1(el);
                    currentContrat.getPrimes().add(el);
                }
                succes();
                if (nbNotInsert > 0) {
                    getWarningMessage("Certains éléments du model n'ont pas étés insérés !");
                }
            } else {
                getErrorMessage("Formulaire incorrecte ", "Aucun contrat n'est sélectionné !");
            }
        } else {
            getErrorMessage("Formulaire incorrecte ", "Aucun contrat n'est sélectionné !");
        }
    }

    private boolean existInModel(TypeElementAdd t) {
        if (t != null) {
            for (YvsGrhModelElementAdditionel e : modelePrimes) {
                if (e.getTypeElement().equals(new YvsGrhElementAdditionel(t.getId()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void createModelePrime() {
        if (!existInModel(prime.getTypeElt())) {
            YvsGrhModelElementAdditionel el = new YvsGrhModelElementAdditionel(prime.getId());
            el.setTypeElement(listTypePrime.get(listTypePrime.indexOf(new YvsGrhTypeElementAdditionel(prime.getTypeElt().getId()))));
            el.setDescription(prime.getDescription());
            el.setMontantElement(prime.getMontant());
            el.setPermanent(prime.isPermanent());
            el.setDateElement(new Date());
            el.setAuthor(currentUser);
            if (!prime.isPermanent()) {
                el.setDateDebut(prime.getDebut());
                el.setDateFin(prime.getFin());
            } else {
                prime.setDebut(null);
                prime.setFin(null);
            }
            el.setId(null);
            el = (YvsGrhModelElementAdditionel) dao.save1(el);
            modelePrimes.add(0, el);
            prime.setId(0);
        } else {
            getErrorMessage("Cette prime existe déjà dans le modèle !");
        }
        addPrime();
    }

    public void deleteOneModelAdditionnel(YvsGrhModelElementAdditionel model) {
        if (model != null) {
            dao.delete(model);
            modelePrimes.remove(model);
            update("tabPrime_modele_contrat");
        }
    }

    public void openPrimeToUpdate(YvsGrhElementAdditionel e) {
        selectedElt = e;
        prime = UtilGrh.buildBeanElt(selectedElt, false);
        updatePrime = true;
        openDialog("prime");
    }

    public void deletePrime() {
        if (selectedElt != null && currentContrat != null) {
            YvsGrhElementAdditionel el = new YvsGrhElementAdditionel(selectedElt.getId());
            el.setTypeElement(selectedElt.getTypeElement());
            dao.delete(el);
            currentContrat.getPrimes().remove(selectedElt);
            update("tabView_contrat:tabPrime-contrat");
            displayBtnDel = false;
            succes();
        } else {
            getErrorMessage("unsuccess");
        }
    }

    public void deleteOnePrime() {
        try {
            selectedElt.setAuthor(currentUser);
            dao.delete(selectedElt);
            currentContrat.getPrimes().remove(selectedElt);
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet élément !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void openConfirmDelPrime(YvsGrhElementAdditionel e) {
        selectedElt = e;
        openDialog("dlgDelPrime");
    }

    public void selectTypePrime(SelectEvent ev) {
        displayBtnDelPrime = true;
        prime.setTypeElt(UtilGrh.buildTypeElt((YvsGrhTypeElementAdditionel) ev.getObject()));
    }

    public void selectOneTypePrimeToUpdate(SelectEvent ev) {
        newType = UtilGrh.buildTypeElt((YvsGrhTypeElementAdditionel) ev.getObject());
    }

    public void deleteTypePrime() {
        if (chaineSelectTypePrime != null) {
            List<Integer> numeros = decomposeSelection(chaineSelectTypePrime);
            try {
                for (int index : numeros) {
                    if (index >= 0) {
                        YvsGrhTypeElementAdditionel t = listTypePrime.get(index);
                        t.setAuthor(currentUser);
                        dao.delete(t);
                        listTypePrime.remove(index);
                    }
                }
                update("table_contrat_prime");
                chaineSelectTypePrime = "";
                succes();
            } catch (NumberFormatException ex) {
                chaineSelectTypePrime = "";
                getErrorMessage("Impossible de supprimer cette prime");
            }
        }
    }

    public void deleteOneTypePrime(YvsGrhTypeElementAdditionel t) {
        try {
            t.setAuthor(currentUser);
            dao.delete(t);
            listTypePrime.remove(t);
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet élément !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void disableOneTypePrime(YvsGrhTypeElementAdditionel t) {
        try {
            t.setAuthor(currentUser);
            t.setActif(!t.getActif());
            dao.update(t);
        } catch (Exception ex) {
            getErrorMessage("Impossible de modifier cet élément !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void desactiverTypePrime() {
        if (chaineSelectTypePrime != null) {

            List<Integer> numeros = decomposeSelection(chaineSelectTypePrime);
            try {
                YvsGrhTypeElementAdditionel t;
                for (Integer index : numeros) {
                    if (index >= 0) {
                        t = listTypePrime.get(index);
                        t.setActif(!t.getActif());
                        t.setAuthor(currentUser);
                        t.setDateUpdate(new Date());
                        dao.update(t);
                    }
                }
                chaineSelectTypePrime = "";
                succes();
            } catch (NumberFormatException ex) {
                chaineSelectTypePrime = "";
                getErrorMessage("Impossible de désactiver cette prime");
            }
        }
    }

    public void selectionSocieteToImport(SelectEvent ev) {
        if (ev != null) {
            //charge les prime de la société selectionnée
            champ = new String[]{"societe", "retenue"};
            val = new Object[]{(YvsSocietes) ev.getObject(), false};
            listTypePrimeToImport = dao.loadNameQueries("YvsGrhTypeElementAdditionel.findAll", champ, val);
            openDialog("dlgEltToImport");
        }
    }

    public void removeTypeEltAdd(YvsGrhTypeElementAdditionel t) {
        if (t != null) {
            listTypePrimeToImport.remove(t);
        }
    }

    public void valideTypeEltImporte() {
        for (YvsGrhTypeElementAdditionel t : listTypePrimeToImport) {
            YvsGrhTypeElementAdditionel en = new YvsGrhTypeElementAdditionel(t);
            en.setId(null);
            en.setAuthor(currentUser);
            en.setSociete(currentAgence.getSociete());
            t.setActif(true);
            en = (YvsGrhTypeElementAdditionel) dao.save1(en);
            t.setId(en.getId());
            listTypePrime.add(t);
        }
        update("table_contrat_prime");
    }
    private List<PrelevementEmps> listSuspension = new ArrayList<>();
    private YvsGrhDetailPrelevementEmps selectPrelev;

    public YvsGrhDetailPrelevementEmps getSelectPrelev() {
        return selectPrelev;
    }

    public void setSelectPrelev(YvsGrhDetailPrelevementEmps selectPrelev) {
        this.selectPrelev = selectPrelev;
    }

    public List<PrelevementEmps> getListSuspension() {
        return listSuspension;
    }

    public void setListSuspension(List<PrelevementEmps> listSuspension) {
        this.listSuspension = listSuspension;
    }

    public void confirmSuspendPrime(YvsGrhElementAdditionel e) {
        if (!saveCascade) {
            if (e != null) {
                selectedElt = e;
                prime = UtilGrh.buildBeanElt(selectedElt, false);
                update("pan-sus-prim-contrat");
                openDialog("dlgSuspension");
            }
        }
    }

    public void confirmCancelSuspension(YvsGrhDetailPrelevementEmps e) {
        e.setStatutReglement('S');
        e.setAuthor(currentUser);
        selectPrelev = e;
        dao.update(e);
    }

    public void confirmDelete(YvsGrhDetailPrelevementEmps e) {
        try {
            e.setAuthor(currentUser);
            dao.delete(e);
            selectedElt.getRetenues().remove(e);
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet élément!");
            getException("Lymytz Error...", ex);
        }
    }

    public void valideSuspension() {
        if (prime.getDebutSuspension() != null && prime.getId() > 0) {
            Calendar current = Calendar.getInstance();
            current.setTime(prime.getDebutSuspension());
            int i = 0;
            YvsGrhDetailPrelevementEmps p;
            while (i < prime.getDureeSuspension()) {
                p = new YvsGrhDetailPrelevementEmps();
                p.setRetenue(new YvsGrhElementAdditionel(prime.getId()));
                p.setDatePrelevement(current.getTime());
                p.setValeur(prime.getMontant());
                p.setStatutReglement('E');   //à true, la suspension est effective
                p.setAuthor(currentUser);
                p.setPlanPrelevement(null);
                p.setDateUpdate(new Date());
                p.setDateSave(new Date());
                p.setDatePreleve(current.getTime());
                p.setRetenuFixe(false);
                if (!contentLine(p.getDatePrelevement())) {
                    p = (YvsGrhDetailPrelevementEmps) dao.save1(p);
                    selectedElt.getRetenues().add(0, p);
                    selectedElt.setSuspendu(true);
                    currentContrat.getPrimes().set(currentContrat.getPrimes().indexOf(selectedElt), selectedElt);
                }
                current.add(Calendar.MONTH, 1);
                i++;
            }
            update("pan-sus-prim-contrat");
            update("tabPrime-contrat");
        } else {
            getErrorMessage("Formulaire incorrecte !");
        }
    }

    private boolean contentLine(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        Calendar curent = Calendar.getInstance();
        for (YvsGrhDetailPrelevementEmps e : selectedElt.getRetenues()) {
            curent.setTime(e.getDatePrelevement());
            if (c.get(Calendar.MONTH) == curent.get(Calendar.MONTH)) {
                return true;
            }
        }
        return false;
    }

    /**
     * ***Gestion des calendrier
     *
     ******
     * @param ev
     */
    public void searchCalendarByReference(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            int id = (int) ev.getNewValue();
            int idx = listCalendrier.indexOf(new YvsCalendrier(id));
            if (idx >= 0) {
                YvsCalendrier cal = listCalendrier.get(idx);
                calendrier = UtilGrh.buildBeanCalendrier(cal);
                calendrier.setListJoursOuvres(dao.loadNameQueries("YvsJoursOuvres.findByCalendrier", new String[]{"calendrier"}, new Object[]{cal}));
                if (calendrier.getId() > 0) {
                    entityCalendrier = cal;
                } else {
                    setDefautCalendar(false);
                }
            } else if (id == -1) {
                openDialog("dlgNewCal");
            }
        }
    }

    public void searchJoursOuvreeByJour() {
        for (YvsJoursOuvres j : calendrier.getListJoursOuvres()) {
            if (j.getJour().equals(joursOuvres.getJour())) {
                resetFiche();
                getMessage("Le jour " + j.getJour() + " a déja été attribué", FacesMessage.SEVERITY_ERROR);
                update("tabView_contrat:detail_jour_travail_00");
                return;
            }
        }
    }

    public void calculDureePause() {
        if (joursOuvres.getHeureFinPause() != null && joursOuvres.getHeureDebutPause() != null) {
            joursOuvres.setDureePause(Utilitaire.calculDureeBetweenDate(joursOuvres.getHeureFinPause(), joursOuvres.getHeureDebutPause()));
        } else {
            joursOuvres.setDureePause(new Date(0));
        }
        update("tabView_contrat:detail_jour_travail_00");
    }

    public void calculDureeTravailRemove(JoursOuvres bean) {
        if (bean != null) {
            if (bean.getHeureFinTravail() != null && bean.getHeureDebutTravail() != null) {
                currentContrat.setHoraireHebdo(currentContrat.getHoraireHebdo() - ((double) (bean.getHeureFinTravail().getTime() - bean.getHeureDebutTravail().getTime()) / (1000 * 60 * 60)));
            } else {
                currentContrat.setHoraireHebdo(currentContrat.getHoraireHebdo() - 0.0);
            }
            setTotalHeureHebdo(Utilitaire.doubleToHour(currentContrat.getHoraireHebdo()));
            update("tabView_contrat:detail_calendier_00");
        }
    }

    public void calculDureeTravailAdd() {
        if (joursOuvres.getHeureFinTravail() != null && joursOuvres.getHeureDebutTravail() != null) {
            currentContrat.setHoraireHebdo(currentContrat.getHoraireHebdo() + ((double) (joursOuvres.getHeureFinTravail().getTime() - joursOuvres.getHeureDebutTravail().getTime()) / (1000 * 60 * 60)));
        } else {
            currentContrat.setHoraireHebdo(currentContrat.getHoraireHebdo() + 0.0);
        }
        setTotalHeureHebdo(Utilitaire.doubleToHour(currentContrat.getHoraireHebdo()));
        update("tabView_contrat:detail_calendier_00");
    }

    public void deleteBeanJoursOuvreee() {
        if (joursOuvres != null) {
            dao.delete(new YvsJoursOuvres(joursOuvres.getId()));
//            listJoursOuvres.remove(joursOuvres);
            calendrier.getListJoursOuvres().remove(joursOuvres);
            calculDureeTravailRemove(joursOuvres);
            resetFicheJoursOuvres();
            setUpdateJoursOuvres(false);
            setNameBtnAddJour("Ajouter");
            setDureePause("00:00");
            update("tabView_contrat:detail_jour_travail_00");
            update("tabView_contrat:tab_jours_ouvres");
            update("tabView_contrat:detail_calendier_00");
        }
    }

    public void deletedayCalendar(JoursOuvres j) {
        try {
            YvsJoursOuvres jo = new YvsJoursOuvres(j.getId());
            jo.setAuthor(currentUser);
            dao.delete(jo);
            calendrier.getListJoursOuvres().remove(j);
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cette ligne !");
            getException("Lymytz error...", ex);
        }
    }

    public void changeStatutJourRepos(YvsJoursOuvres jo) {
        jo.setJourDeRepos(!jo.getJourDeRepos());
        System.err.println(" JO " + jo.getJour() + "Repos " + jo.getJourDeRepos());
        dao.update(jo);
        int idx = listCalendrier.indexOf(new YvsCalendrier(calendrier.getId()));
        if (idx >= 0) {
            YvsCalendrier c = listCalendrier.get(idx);
            c.getJoursOuvres().set(c.getJoursOuvres().indexOf(jo), jo);
        }
        succes();
    }

    public void changeStatutJourOuvrable(YvsJoursOuvres jo) {
        jo.setOuvrable(!jo.getOuvrable());

        dao.update(jo);
        int idx = listCalendrier.indexOf(new YvsCalendrier(calendrier.getId()));
        if (idx >= 0) {
            YvsCalendrier c = listCalendrier.get(idx);
            c.getJoursOuvres().set(c.getJoursOuvres().indexOf(jo), jo);
        }
        succes();
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleFileUpload(FileUploadEvent ev) {
        if (ev != null) {
            if (controleUploadFile()) {
                String repDest = Initialisation.getCheminResource() + "" + Util.getCheminDocContratEmps(currentContrat).substring(Initialisation.getCheminAllDoc().length(), Util.getCheminDocContratEmps(currentContrat).length());
                //répertoire destination de sauvegarge= C:\\lymytz\scte...
                String repDestSVG = Util.getCheminDocContratEmps(currentContrat);
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
                    currentContrat.setFichier(file);
                    setDisplayViewFile(!((file != null) ? (!file.equals("") && file.length() > 0) : false));
                    getInfoMessage("Charger !");
//                    update("tabView_contrat:fichier_contrat_00"); 
                    update("form_contrat_01");

                } catch (IOException ex) {
                    Logger.getLogger(ManagedContratEmploye.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public boolean controleUploadFile() {
        if (currentContrat != null) {
            if ((currentContrat.getReference() != null) ? !currentContrat.getReference().equals("") : false) {
                if ((currentContrat.getEmploye() != null) ? currentContrat.getEmploye().getId() != 0 : false) {
                    return true;
                } else {
                    getErrorMessage("Vous devez selectionnez un employe");
                    return false;
                }
            } else {
                getErrorMessage("Vous devez entrer la reference du contrat");
                return false;
            }
        } else {
            return false;
        }
    }

    public void handlerFileDownload() {
        String chemin = Initialisation.getCheminResource() + "" + Util.getCheminDocContratEmps(currentContrat).substring(Initialisation.getCheminAllDoc().length(), Util.getCheminDocContratEmps(currentContrat).length());
        String fichier = chemin + "" + Initialisation.FILE_SEPARATOR + currentContrat.getFichier();
        Util.getDownloadFilePDF(fichier, "Contrat_" + currentContrat.getReference());
    }

    /**
     * ********************FIN DE CONTRAT***************************
     */
    private UtilFormules formule;
    private FinDeContrats finContrat = new FinDeContrats();
    private List<YvsGrhContratSuspendu> listFinContrat;
    private Date debutMois, finMois;
    private Double indemniteConge;
    private List<YvsGrhRubriqueIndemnite> indemnites;
    private List<YvsGrhRubriqueIndemnite> listRubriqueIndemnite;
    private RubriqueIndemnite rubrique = new RubriqueIndemnite();
    YvsGrhParamContrat paramContrat;
    private ParamContrat beanParamContrat = new ParamContrat();
    private List<LibelleDroitFinContrat> libelles;
    private LibelleDroitFinContrat newLibelle = new LibelleDroitFinContrat();
    private ElementIndemnite otherIndemnite = new ElementIndemnite();
    private GrilleTauxFinContrat grille = new GrilleTauxFinContrat();
    boolean updateLibelle, updateGrille;

    public LibelleDroitFinContrat getNewLibelle() {
        return newLibelle;
    }

    public void setNewLibelle(LibelleDroitFinContrat newLibelle) {
        this.newLibelle = newLibelle;
    }

    public List<LibelleDroitFinContrat> getLibelles() {
        return libelles;
    }

    public void setLibelles(List<LibelleDroitFinContrat> libelles) {
        this.libelles = libelles;
    }

    public List<YvsGrhContratSuspendu> getListFinContrat() {
        return listFinContrat;
    }

    public void setListFinContrat(List<YvsGrhContratSuspendu> listFinContrat) {
        this.listFinContrat = listFinContrat;
    }

    public FinDeContrats getFinContrat() {
        return finContrat;
    }

    public void setFinContrat(FinDeContrats finContrat) {
        this.finContrat = finContrat;
    }

    public List<YvsGrhRubriqueIndemnite> getIndemnites() {
        return indemnites;
    }

    public void setIndemnites(List<YvsGrhRubriqueIndemnite> indemnites) {
        this.indemnites = indemnites;
    }

    public ElementIndemnite getOtherIndemnite() {
        return otherIndemnite;
    }

    public void setOtherIndemnite(ElementIndemnite otherIndemnite) {
        this.otherIndemnite = otherIndemnite;
    }

    public List<YvsGrhRubriqueIndemnite> getListRubriqueIndemnite() {
        return listRubriqueIndemnite;
    }

    public void setListRubriqueIndemnite(List<YvsGrhRubriqueIndemnite> listRubriqueIndemnite) {
        this.listRubriqueIndemnite = listRubriqueIndemnite;
    }

    public RubriqueIndemnite getRubrique() {
        return rubrique;
    }

    public void setRubrique(RubriqueIndemnite rubrique) {
        this.rubrique = rubrique;
    }

    public ParamContrat getBeanParamContrat() {
        return beanParamContrat;
    }

    public void setBeanParamContrat(ParamContrat beanParamContrat) {
        this.beanParamContrat = beanParamContrat;
    }

    public GrilleTauxFinContrat getGrille() {
        return grille;
    }

    public void setGrille(GrilleTauxFinContrat grille) {
        this.grille = grille;
    }

    //renvoie le salaire moyen en fonction de la formule présente das la table de paramétrage des contrats
    private double calculesalaireMoyen() {
        if (paramContrat == null) {
            champ = new String[]{"secteur"};
            val = new Object[]{currentAgence.getSecteurActivite()};
            paramContrat = (YvsGrhParamContrat) dao.loadOneByNameQueries("YvsGrhParamContrat.findBySecteur", champ, val);
        }
        if (paramContrat != null) {
            if (paramContrat.getFormuleSalaire() != null) {
                //fabrique un lexème
                Lexemes lexem = new Lexemes();
                lexem.nom = "Formule_salaire_moyen";
                lexem.code = "code_sm";
                lexem.typeElt = 3;
                lexem.expression = paramContrat.getFormuleSalaire();
                lexem.expresion = true;
//                YvsGrhDetailBulletin re = formule.buildElementBulletin(lexem);
                YvsGrhDetailBulletin re = new YvsGrhDetailBulletin();
                return re.getBase();
            }
        }
        return 0;
    }

    private YvsGrhRubriqueIndemnite calculesalairePreavis(String unite, double dureePreavis) {
        if (paramContrat == null) {
            champ = new String[]{"secteur"};
            val = new Object[]{currentAgence.getSecteurActivite()};
            paramContrat = (YvsGrhParamContrat) dao.loadOneByNameQueries("YvsGrhParamContrat.findBySecteur", champ, val);
        }
        YvsGrhElementsIndemnite ei = new YvsGrhElementsIndemnite();
        YvsGrhRubriqueIndemnite result = findRubrique(Constantes.RUBRIQUE_INDEMNITE_PREAVIS);
        if (paramContrat != null) {
            if (paramContrat.getFormulePreavis() != null) {
                //fabrique un lexème
                Lexemes lexem = new Lexemes();
                lexem.nom = "Formule_salaire_préavis";
                lexem.code = "code_sp";
                lexem.typeElt = 3;
                lexem.expression = paramContrat.getFormulePreavis();
                lexem.expresion = true;
//                YvsGrhDetailBulletin re = formule.buildElementBulletin(lexem);
                YvsGrhDetailBulletin re = new YvsGrhDetailBulletin();
                double montant, duree;
                if (unite.equals(Constantes.UNITE_DUREE_PREAVIS_JOUR)) {
                    duree = (dureePreavis / 30);
                    montant = (dureePreavis / 30) * re.getBase();
                } else {
                    duree = (dureePreavis);
                    montant = (dureePreavis * re.getBase());
                }
                ei.setBase(montant);
                ei.setLibelle("Indemnité de préavis sur " + duree + " Mois");
                ei.setQuantite(duree);
                ei.setTaux(1.0);
                ei.setRubrique(new YvsGrhRubriqueIndemnite((long) 0, "Indemnité de préavis", 0));
            }
        }
        if (result != null) {
            result.getIndemnites().add(ei);
        }
        return result;
    }

    public void calculPreavisAndAnciennete() {
        //la selection se trouve dans la chaine "chaineSelecContrat"
        if (chaineSelecContrat != null) {
            int index;
            String[] numroLine = chaineSelecContrat.split("-");
            List<String> l = new ArrayList<>();
            //on ne doit pouvoir traiter la suspension que d'un contrat à la fois
            if (numroLine.length > 1) {
                getErrorMessage("Vous ne pouvez traiter la suspension que d'un contrat à la fois !");
            } else {
                try {
                    YvsGrhContratEmps c;
                    for (String numroLine1 : numroLine) {
                        index = Integer.parseInt(numroLine1);
                        c = listContrat.get(index);
                        givePreavis(c);
                        update("dlgGridCS");
                        l.add(numroLine1);
                    }
                } catch (NumberFormatException ex) {
                    //pour maintenir la vue cohérente avec la selection en cours au cas où l'action est interrompu par une erreur
                    chaineSelecContrat = "";
                    for (String numroLine1 : numroLine) {
                        if (!l.contains(numroLine1)) {
                            chaineSelecContrat += numroLine1 + "-";
                        }
                    }
                }
            }

        }
    }

    //récupère la durée de préavis correspondan à l'ancienneté et à la catégorie de l'employé
    private double givePreavis(YvsGrhContratEmps contra) {
        double re = 0;
        //cherche l'ancienneté en fonction de la date d'effet de la rupture du contrat
        YvsGrhEmployes em = contra.getEmploye();
        if (em != null) {
            if (em.getDateEmbauche() != null && em.getConvention() != null) {
                int anc = Utilitaire.calculNbMonth(em.getDateEmbauche(), finContrat.getDateEffet());
                YvsGrhCategorieProfessionelle l = (YvsGrhCategorieProfessionelle) dao.loadOneByNameQueries("YvsConventionEmploye.findCatConvActif", new String[]{"employe"}, new Object[]{em});
                champ = new String[]{"anciennete", "categorie"};
                val = new Object[]{anc / 12, l}; //ramène l'aciènneté en année
                YvsGrhCategoriePreavis catP = (YvsGrhCategoriePreavis) dao.loadOneByNameQueries("YvsGrhCategoriePreavis.findByIntervalle", champ, val);
                if (catP != null) {
                    finContrat.setDurrePreavis(catP.getPreavis());
                    finContrat.setAnciennete(anc / 12);
                    finContrat.setUniteDuree(catP.getUnitePreavis());
                    finContrat.setContrat(UtilGrh.buildBeanContratEmploye(contra));
                } else {
                    getWarningMessage("La durée de préavis n'est pas donnée pour cet employé !");
                }
            } else {
                if (em.getDateEmbauche() == null) {
                    getErrorMessage("Impossible de calculer l'ancienneté de l'employé ! ", "date d'embauche indisponible");
                } else {
                    getErrorMessage("Impossible de calculer l'ancienneté de l'employé ! ", "Aucune catégorie professionnel n'est associé à cet employé");
                }
            }
        } else {
            getErrorMessage("Employé Non selectionnée !");
        }
        return re;
    }

    public void saveSuspension() {
        YvsGrhContratSuspendu contSus = new YvsGrhContratSuspendu();
        contSus.setAnciennete(finContrat.getAnciennete());
        contSus.setDateEffet(finContrat.getDateEffet());
        contSus.setDureePreavis(finContrat.getDurrePreavis());
        contSus.setId(null);
        contSus.setMotif(finContrat.getMotif());
        contSus.setUnitePeriodePreavis(finContrat.getUniteDuree());
        contSus.setAuthor(currentUser);
        contSus.setDateSave(new Date());
        contSus.setContrat(new YvsGrhContratEmps(finContrat.getContrat().getId()));
        contSus = (YvsGrhContratSuspendu) dao.save1(contSus);
        finContrat.setId(contSus.getId());
        listFinContrat.add(0, contSus);
    }

    public void calculIndemniteLicenciement() {
        YvsGrhContratSuspendu fc = giveSelectContratSuspendu();
        cloneObject(finContrat, fc);
        indemnites = new ArrayList<>();
        if (!finContrat.getIndemnites().isEmpty()) {
            indemnites.addAll(listRubriqueIndemnite);
            for (YvsGrhElementsIndemnite ed : fc.getIndemnites()) {
                indemnites.get(indemnites.indexOf(ed.getRubrique())).getIndemnites().add(ed);
            }
        } else {
            //calcule le date de début du mois

            positionneDate(fc.getDateEffet());
            formule = new UtilFormules(paramGrh, debutMois, fc.getDateEffet(), dao, currentAgence.getSociete(), fc.getContrat(), fc.getContrat().getEmploye(), null);
            //1. calcul le relicat du salaire du et l'indemnité de congés pour la période allant du dernier congé à la date d'effet du licenciement        
            indemnites.addAll(calcuDuRelicatSalaire(fc));
            //2.l'indemnité de préavis
            indemnites.add(calculesalairePreavis(fc.getUnitePeriodePreavis(), fc.getDureePreavis()));
            //3.l'indemnité de licenciement
            double salaireMoyen = calculesalaireMoyen();
            //ECHELONAGE DE l'indemnité sur la période d'ancienneté
            indemnites.add(buildIndemniteLicenciement(salaireMoyen, fc.getAnciennete()));
            //4.les autres indemnité
        }
        openDialog("dlgEltIndemnite");
        update("table_elt_indemnite");
    }

    private List<YvsGrhRubriqueIndemnite> calcuDuRelicatSalaire(YvsGrhContratSuspendu fc) {
        double re = 0;
        List<YvsGrhRubriqueIndemnite> result = new ArrayList<>();
        YvsGrhElementsIndemnite ei;
        YvsGrhContratEmps contrat = fc.getContrat();
        //charge la structure de salaire de ce contrat
        champ = new String[]{"id"};
        val = new Object[]{contrat.getId()};
        contrat = (YvsGrhContratEmps) dao.loadOneByNameQueries("YvsGrhContratEmps.findById", champ, val);
        YvsGrhBulletins bs = formule.createBulletin(contrat, debutMois, finMois);
        YvsGrhRubriqueIndemnite r1;
        for (YvsGrhDetailBulletin de : bs.getListDetails()) {
            if (de.getElement().getVisibleBulletin()) {
                if (!de.getElement().getRetenue()) {
                    re += (de.getMontantPayer());
                } else {
                    re -= (de.getRetenuSalariale());
                }
            }
            if (de.getElement().getRegleConge()) {
                //cherche la date du dernier congé Annuele
                champ = new String[]{"employe", "statut"};
                val = new Object[]{contrat.getEmploye(), 'O'};
                YvsGrhCongeEmps lastConge = (YvsGrhCongeEmps) dao.loadOneByNameQueries("YvsGrhCongeEmps.findLastCongeAPaye", champ, val);
                indemniteConge = de.getRetenuSalariale();
                ei = new YvsGrhElementsIndemnite();
                ei.setBase(indemniteConge);
                ei.setLibelle("Indemnité compensatrice de congé Période " + ((lastConge != null) ? dft.format((lastConge.getDateFin())) : " --- ") + " à " + dft.format(fc.getDateEffet()));
                ei.setQuantite(1.0);
                ei.setTaux(1.0);
                ei.setRubrique(new YvsGrhRubriqueIndemnite((long) 1, "Indemnité de congé", 1));
                r1 = findRubrique(Constantes.RUBRIQUE_INDEMNITE_CONGE);
                if (r1 != null) {
                    r1.getIndemnites().add(ei);
                }
                result.add(r1);
            }
        }
        ei = new YvsGrhElementsIndemnite();
        ei.setBase(re);
        ei.setLibelle("Relicat du salaire Du " + dft.format(debutMois) + " à " + dft.format(fc.getDateEffet()));
        ei.setQuantite(1.0);
        ei.setTaux(1.0);
        ei.setRubrique(new YvsGrhRubriqueIndemnite((long) 1, "Indemnité de congé", 2));
        r1 = findRubrique(Constantes.RUBRIQUE_RELIQUAT_SALAIRE);
        if (r1 != null) {
            r1.getIndemnites().add(ei);
        }
        result.add(r1);
        return result;
    }

    private void positionneDate(Date dateDebut) {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        if (paramGrh == null) {
            paramGrh = (YvsParametreGrh) dao.loadOneByNameQueries("YvsParametreGrh.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
        if (paramGrh != null) {
            Calendar deb = Calendar.getInstance();
            Calendar now = Calendar.getInstance();
            deb.setTime(dateDebut);
            now.setTime(dateDebut);
            if (paramGrh.getDatePaiementSalaire() != null) {
                deb.setTime(paramGrh.getDatePaiementSalaire());
                deb.set(Calendar.MONTH, now.get(Calendar.MONTH));
                deb.set(Calendar.YEAR, now.get(Calendar.YEAR));
                debutMois = deb.getTime();
                deb.add(Calendar.MONTH, 1);
                deb.add(Calendar.DAY_OF_MONTH, -1);
                finMois = deb.getTime();
            }
        }
    }

    private YvsGrhContratSuspendu giveSelectContratSuspendu() {
        //la selection se trouve dans la chaine "chaineSelecContrat"
        int index = -1;
        YvsGrhContratSuspendu c = null;
        if (chaineSelectContratSus != null) {
            String[] numroLine = chaineSelectContratSus.split("-");
            List<String> l = new ArrayList<>();
            //on ne doit pouvoir traiter la suspension que d'un contrat à la fois
            if (numroLine.length > 1) {
                getErrorMessage("Vous ne pouvez traiter la suspension que d'un contrat à la fois !");
            } else {
                try {
                    for (String numroLine1 : numroLine) {
                        index = Integer.parseInt(numroLine1);
                        c = listFinContrat.get(index);
                        l.add(numroLine1);
                    }
                } catch (NumberFormatException ex) {
                    //pour maintenir la vue cohérente avec la selection en cours au cas où l'action est interrompu par une erreur
                    chaineSelectContratSus = "";
                    for (String numroLine1 : numroLine) {
                        if (!l.contains(numroLine1)) {
                            chaineSelectContratSus += numroLine1 + "-";
                        }
                    }
                }
            }

        }
        return c;
    }

    private YvsGrhRubriqueIndemnite buildIndemniteLicenciement(double sMoyen, double ancienete) {
        YvsGrhRubriqueIndemnite result;
        //récupère la grille de tranche des taux d'indemnité
        if (paramContrat == null) {
            champ = new String[]{"secteur"};
            val = new Object[]{currentAgence.getSecteurActivite()};
            paramContrat = (YvsGrhParamContrat) dao.loadOneByNameQueries("YvsGrhParamContrat.findBySecteur", champ, val);
        }
        champ = new String[]{"contrat"};
        val = new Object[]{paramContrat};
        List<YvsGrhGrilleTauxFinContrat> lg = dao.loadNameQueries("YvsGrhGrilleTauxFinContrat.findByContrat", champ, val);
        double qte;
        YvsGrhElementsIndemnite elt;
        result = findRubrique(Constantes.RUBRIQUE_INDEMNITE_LICENCIEMENT);
        if (result != null) {
            for (YvsGrhGrilleTauxFinContrat tranche : lg) {
                if (tranche.getAncMax() < ancienete) {
                    qte = tranche.getAncMax() - (tranche.getAncMin() - 1);
                } else {
                    qte = ancienete - (tranche.getAncMin() - 1);
                }
                qte = (qte < 0) ? 0 : qte;
                elt = new YvsGrhElementsIndemnite();
                elt.setBase(qte * sMoyen * tranche.getTaux());
                elt.setLibelle(nf.format(sMoyen) + " * " + tranche.getTaux() + " * " + qte + " ans = " + nf.format(elt.getBase()));
                elt.setQuantite(qte);
                elt.setTaux(tranche.getTaux());
                result.getIndemnites().add(elt);
            }
        }
        return result;
    }

    //créer les libellé de droits d'indemnité
    public void createLibelleDroit() {
        if (newLibelle.getLibelle() != null && paramContrat != null) {
            YvsGrhLibelleDroitFinContrat lib = new YvsGrhLibelleDroitFinContrat();
            lib.setLibelle(newLibelle.getLibelle());
            lib.setActif(newLibelle.isActif());
            lib.setParamContrat(paramContrat);
            lib.setId(newLibelle.getId());
            if (!updateLibelle) {
                lib.setId(null);
                lib = (YvsGrhLibelleDroitFinContrat) dao.save1(lib);
                newLibelle.setId(lib.getId());
                libelles.add(0, newLibelle);
            } else {
                dao.update(lib);
                libelles.set(libelles.indexOf(newLibelle), newLibelle);
            }
            otherIndemnite.setLib(newLibelle);
            newLibelle = new LibelleDroitFinContrat();
            updateLibelle = false;
        } else {
            if (newLibelle.getLibelle() == null) {
                getErrorMessage("Impossible d'enregistrer", "le paramètres du contrat est inconnu");
            }
        }
    }

    public void selectOneLibelle(SelectEvent ev) {
        if (ev != null) {
            newLibelle = (LibelleDroitFinContrat) ev.getObject();
            updateLibelle = true;
        }
    }

    //Ajouter une nouvelle indemnité
    public void createNewElementIndemnite() {
        if (otherIndemnite.getLib().getId() != 0 && otherIndemnite.getBase() > 0) {
            otherIndemnite.setLibelle(libelles.get(libelles.indexOf(otherIndemnite.getLib())).getLibelle());
            otherIndemnite.setTaux(1);
            boolean find = false;
            for (YvsGrhRubriqueIndemnite r : indemnites) {
                if (r.getCode().equals(Constantes.RUBRIQUE_AUTRES)) {
                    r.getIndemnites().add(new YvsGrhElementsIndemnite(otherIndemnite.getId()));
                    find = true;
                }
            }
            if (!find) {
                YvsGrhRubriqueIndemnite r = findRubrique(Constantes.RUBRIQUE_AUTRES);
                if (r != null) {
                    r.getIndemnites().add(new YvsGrhElementsIndemnite(otherIndemnite.getId()));
                    indemnites.add(r);
                }
            }
            otherIndemnite = new ElementIndemnite();
        } else {
            if (otherIndemnite.getBase() <= 0) {
                getErrorMessage("Veuillez entrer le montant de l'indemnité !");
            }
        }
    }

    //enregistrer et charger le paramétrage des contrats pour le secteur correstondant à l'agence activé
    public void loadParamContrat() {
        if (paramContrat == null) {
            champ = new String[]{"secteur"};
            val = new Object[]{currentAgence.getSecteurActivite()};
            paramContrat = (YvsGrhParamContrat) dao.loadOneByNameQueries("YvsGrhParamContrat.findBySecteur", champ, val);
            //charge les rubriques d'indemnités enregistré
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
            listRubriqueIndemnite = dao.loadNameQueries("YvsGrhRubriqueIndemnite.findAll", champ, val);
        }
        if (paramContrat != null) {
            beanParamContrat = UtilGrh.buildBeanParamContrat(paramContrat);
            champ = new String[]{"contrat"};
            val = new Object[]{paramContrat};
            beanParamContrat.setGrillesTaux(UtilGrh.buildBeanGrilleTaux(dao.loadNameQueries("YvsGrhGrilleTauxFinContrat.findByContrat", champ, val)));
        }
    }

    public void createParamContrat() {
        YvsGrhParamContrat p = new YvsGrhParamContrat();
        p.setPeriodeReference(beanParamContrat.getPeriodeReference());
        p.setFormulePreavis(beanParamContrat.getFormulePreavis());
        p.setFormuleSalaire(beanParamContrat.getFormuleSalaireMoyen());
        p.setSecteur(currentAgence.getSecteurActivite());
        p.setAncienneteRequise(beanParamContrat.getAncienneteRequise());
        if (paramContrat != null) {
            p.setId(paramContrat.getId());
            dao.update(p);
            paramContrat = p;
        } else {
            p.setId(null);
            p = (YvsGrhParamContrat) dao.save1(p);
            beanParamContrat.setId(p.getId());
            paramContrat = p;
        }
        //enregistre les rubrique de base si elle n'exite pas
        boolean preavis = false, licen = false, conge = false, reliquat = false, other = false;
        for (YvsGrhRubriqueIndemnite r : listRubriqueIndemnite) {
            if (r.getCode().equals(Constantes.RUBRIQUE_INDEMNITE_CONGE)) {
                conge = true;
            } else if (r.getCode().equals(Constantes.RUBRIQUE_INDEMNITE_PREAVIS)) {
                preavis = true;
            } else if (r.getCode().equals(Constantes.RUBRIQUE_INDEMNITE_LICENCIEMENT)) {
                licen = true;
            } else if (r.getCode().equals(Constantes.RUBRIQUE_RELIQUAT_SALAIRE)) {
                reliquat = true;
            } else if (r.getCode().equals(Constantes.RUBRIQUE_AUTRES)) {
                other = true;
            }
        }
        YvsGrhRubriqueIndemnite ent;
        if (!conge) {
            ent = new YvsGrhRubriqueIndemnite();
            ent.setCode(Constantes.RUBRIQUE_INDEMNITE_CONGE);
            ent.setDesignation("Indemnité compensatrice de congé");
            ent.setOrdre(4);
            ent.setSociete(currentAgence.getSociete());
            ent = (YvsGrhRubriqueIndemnite) dao.save1(ent);
            listRubriqueIndemnite.add(ent);
        }
        if (!preavis) {
            ent = new YvsGrhRubriqueIndemnite();
            ent.setCode(Constantes.RUBRIQUE_INDEMNITE_PREAVIS);
            ent.setDesignation("Indemnité compensatrice de préavis");
            ent.setOrdre(2);
            ent.setSociete(currentAgence.getSociete());
            ent = (YvsGrhRubriqueIndemnite) dao.save1(ent);
            listRubriqueIndemnite.add(ent);
        }
        if (!licen) {
            ent = new YvsGrhRubriqueIndemnite();
            ent.setCode(Constantes.RUBRIQUE_INDEMNITE_LICENCIEMENT);
            ent.setDesignation("Indemnité de licenciement");
            ent.setOrdre(3);
            ent.setSociete(currentAgence.getSociete());
            ent = (YvsGrhRubriqueIndemnite) dao.save1(ent);
            listRubriqueIndemnite.add(ent);
        }
        if (!reliquat) {
            ent = new YvsGrhRubriqueIndemnite();
            ent.setCode(Constantes.RUBRIQUE_RELIQUAT_SALAIRE);
            ent.setDesignation("Reliquat Salaire du");
            ent.setOrdre(1);
            ent.setSociete(currentAgence.getSociete());
            ent = (YvsGrhRubriqueIndemnite) dao.save1(ent);
            listRubriqueIndemnite.add(ent);
        }
        if (!other) {
            ent = new YvsGrhRubriqueIndemnite();
            ent.setCode(Constantes.RUBRIQUE_AUTRES);
            ent.setDesignation("Autres indemnité");
            ent.setOrdre(5);
            ent.setSociete(currentAgence.getSociete());
            ent = (YvsGrhRubriqueIndemnite) dao.save1(ent);
            listRubriqueIndemnite.add(ent);
        }
        succes();
    }

    public void createGrilleTaux() {
        if (beanParamContrat.getId() != 0) {
            YvsGrhGrilleTauxFinContrat g = new YvsGrhGrilleTauxFinContrat();
            g.setAncMax(grille.getAncMax());
            g.setAncMin(grille.getAncMin());
            g.setTaux(grille.getTaux());
            g.setParamContrat(new YvsGrhParamContrat(beanParamContrat.getId()));
            if (!updateGrille) {
                g.setId(null);
                g = (YvsGrhGrilleTauxFinContrat) dao.save1(g);
                grille.setId(g.getId());
                beanParamContrat.getGrillesTaux().add(grille);
            } else {
                g.setId(grille.getId());
                dao.update(g);
                beanParamContrat.getGrillesTaux().set(beanParamContrat.getGrillesTaux().indexOf(grille), grille);
            }
            updateGrille = false;
            grille = new GrilleTauxFinContrat();
        } else {
            getErrorMessage("Veuillez enregistrer d'abord les paramètres de fin de contrats !");
        }
    }

    public void selectOneGrille(SelectEvent ev) {
        if (ev != null) {
            grille = (GrilleTauxFinContrat) ev.getObject();
            updateGrille = true;
        }
    }

    //recherche une rubrique de l'indemnité à partir de son code
    private YvsGrhRubriqueIndemnite findRubrique(String code) {
        for (YvsGrhRubriqueIndemnite r : listRubriqueIndemnite) {
            if (r.getCode().equals(code)) {
                return r;
            }
        }
        return null;
    }

    public void saveElementIndemnite() {
        if (finContrat.getId() != 0) {
            YvsGrhElementsIndemnite elt;
            for (YvsGrhRubriqueIndemnite r : indemnites) {
                for (YvsGrhElementsIndemnite e : r.getIndemnites()) {
                    elt = new YvsGrhElementsIndemnite();
                    elt.setBase(e.getBase());
                    elt.setContratSuspendu(new YvsGrhContratSuspendu(finContrat.getId()));
                    elt.setLibelle(e.getLibelle());
                    elt.setQuantite(e.getQuantite());
                    elt.setRetenue(e.getRetenue());
                    elt.setRubrique(new YvsGrhRubriqueIndemnite(r.getId()));
                    elt.setTaux(e.getTaux());
                    elt.setId(e.getId());
                    if (e.getId() == 0) {
                        elt.setId(null);
                        elt = (YvsGrhElementsIndemnite) dao.save1(elt);
                        e.setId(elt.getId());
                    } else {
                        dao.update(elt);
                    }
                }
            }
            getInfoMessage("Eléments d'indemnité enregistrés avec succès !");
        } else {
            getErrorMessage("Aucun contrats suspendu n'a été sélectionné !");
        }
    }
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    /*Recherche de contrat*/
    List<ParametreRequete> params = new ArrayList<>();
    private Boolean paramActif;
    private String paramStatut;

    public Boolean getParamActif() {
        return paramActif;
    }

    public void setParamActif(Boolean paramActif) {
        this.paramActif = paramActif;
    }

    public String getParamStatut() {
        return paramStatut;
    }

    public void setParamStatut(String paramStatut) {
        this.paramStatut = paramStatut;
    }

    public void findByEmploye() {
        ParametreRequete p0 = new ParametreRequete(null, "codeClient", null, " LIKE ", "AND");
        if (matriculeEmps != null) {
            p0 = new ParametreRequete(null, "codeClient", "%" + matriculeEmps.toUpperCase() + "%", " LIKE ", "AND");
            ParametreRequete p01 = new ParametreRequete("UPPER(y.employe.nom)", "nom", "%" + matriculeEmps.toUpperCase() + "%", " LIKE ", "OR");
            ParametreRequete p02 = new ParametreRequete("UPPER(y.employe.prenom)", "prenom", "%" + matriculeEmps.toUpperCase() + "%", " LIKE ", "OR");
            ParametreRequete p03 = new ParametreRequete("UPPER(y.employe.matricule)", "matricule", "%" + matriculeEmps.toUpperCase() + "%", " LIKE ", "OR");
            ParametreRequete p04 = new ParametreRequete("UPPER(concat(y.employe.nom,' ',y.employe.prenom))", "nomPrenom", "%" + matriculeEmps.toUpperCase() + "%", " LIKE ", "OR");
            ParametreRequete p05 = new ParametreRequete("UPPER(concat(y.employe.prenom,' ',y.employe.nom))", "prenomNom", "%" + matriculeEmps.toUpperCase() + "%", " LIKE ", "OR");
            p0.getOtherExpression().add(p01);
            p0.getOtherExpression().add(p02);
            p0.getOtherExpression().add(p03);
            p0.getOtherExpression().add(p04);
            p0.getOtherExpression().add(p05);
        }
        paginator.addParam(p0);
        initForm = true;
        loadAllContrat(true);
    }

    public void addParamActif() {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", paramActif, "=", "AND");
        paginator.addParam(p);
        initForm = true;
        loadAllContrat(true);
    }

    public void addParamStatut(ValueChangeEvent ev) {
        if (ev != null) {
            String value = (String) ev.getNewValue();
            ParametreRequete p = new ParametreRequete("y.statut", "statut", value, "=", "AND");
            paginator.addParam(p);
            initForm = true;
            loadAllContrat(true);
        }
    }

    public void addParamAgence(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.employe.agence", "agence", null, "=", "AND");
        if (ev != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                YvsAgences ag = new YvsAgences((long) ev.getNewValue());
                p.setObjet(ag);
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllContrat(true);
    }

//    public void executeDynamicQuery() {
//        nameQueriCount = buildDynamicQuery(params, "SELECT COUNT(y) FROM YvsGrhContratEmps y WHERE");
//        nameQueri = buildDynamicQuery(params, "SELECT y FROM YvsGrhContratEmps y WHERE");
//        if (!params.isEmpty()) {
//            listContrat = dao.loadEntity(nameQueri, champ, val);
//            nombreContrat = listContrat.size();
//            System.err.println("Résultat " + nombreContrat);
//        } else {
//            getErrorMessage("Aucun critère de recherche n'a été enregistré !");
//
//        }
//    }
    //Afficher les employé sans contrats
    public void displayEmpsWithoutContrat() {
        String query = "SELECT e FROM YvsGrhEmployes e LEFT JOIN FETCH e.contrat WHERE e.agence.societe=:societe";
        ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
        if (service != null) {
            List<YvsGrhEmployes> l = dao.loadEntity(query, new String[]{"societe"}, new Object[]{currentUser.getAgence().getSociete()});
            service.getListEmployes().clear();
            for (YvsGrhEmployes e : l) {
                if (e.getContrat() == null) {
                    service.getListEmployes().add(e);
                }
            }
            openDialog("dlgEmploye");
        }
    }

    /*PARAMETRER LA RESTRICTION D'ACCES AUX CONTRATS*/
    public void limitAccesContrats(SelectEvent ev) {
        List<Integer> l = decomposeSelection(chaineSelecContrat);
        YvsNiveauAcces nivo = (YvsNiveauAcces) ev.getObject();
        if (nivo != null) {
            YvsGrhContratEmps c;
            YvsGrhContratRestreint cr;
            for (int i : l) {
                c = listContrat.get(i);
                cr = (YvsGrhContratRestreint) dao.loadOneByNameQueries("YvsGrhContratRestreint.findOne", new String[]{"contrat", "niveau"}, new Object[]{c, nivo});
                if (cr == null) {
                    cr = new YvsGrhContratRestreint();
                    cr.setAuthor(currentUser);
                    cr.setContrat(c);
                    cr.setNiveaux(nivo);
                    dao.save(cr);
                }
                if (!c.getAccesRestreint()) {
                    c.setAuthor(currentUser);
                    c.setAccesRestreint(true);
                    dao.update(c);
                    listContrat.set(i, c);
                }
            }
            chaineSelecContrat = null;
        }
    }

    public void autoriserAccesContrats() {
        if (entityContrat != null) {
            entityContrat.setAuthor(currentUser);
            entityContrat.setAccesRestreint(false);
            dao.update(entityContrat);
            listContrat.set(listContrat.indexOf(entityContrat), entityContrat);
        } else {
            getErrorMessage("Aucun contrat n'a été selectionné !");
        }
    }

    //voir les niveaux qui ont acces à un contrat
    public void openNivoWhoAccesToOneContrat(YvsGrhContratEmps c) {
//        c.getYvsGrhContratRestreintList();
    }

    //ajouter à la liste en cours les contrats restreint si on a le droit
    public void loadContratsRestreint() {
        YvsNiveauAcces nivo = currentUser.getUsers().getNiveauAcces();
        if (nivo != null) {
            List<YvsGrhContratEmps> l = dao.loadNameQueries("YvsGrhContratRestreint.findByNiveau", new String[]{"niveau"}, new Object[]{nivo});
            listContrat.addAll(0, l);
        }
    }

    public void majInfosContrat(YvsGrhContratEmps c) {
        if (c != null) {
            if (c.getEmploye() != null) {
                c.setDateDebut(c.getEmploye().getDateEmbauche());
                c.setDateFin(c.getEmploye().getDateArret());
                dao.update(c);
            }
        }
    }

    public void changeInfosContrats() {
        if (choixSelect != null) {
            switch (choixSelect) {
                case "SINGLE":
                    majInfosContrat(entityContrat);
                    break;
                case "SELECT":
                    for (YvsGrhContratEmps c : listContrat) {
                        majInfosContrat(c);
                    }
                    break;
                case "ALL":
                    List<YvsGrhContratEmps> l = dao.loadNameQueries("YvsGrhContratEmps.findAllScte", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                    for (YvsGrhContratEmps c : l) {
                        majInfosContrat(c);
                    }
                    break;
            }
            if (currentContrat.getId() > 0 && entityContrat != null) {
                currentContrat.setDateDebut(entityContrat.getEmploye().getDateEmbauche());
                currentContrat.setDateFin(entityContrat.getEmploye().getDateArret());
                update("panel_contrat_donnee_00");
            }
            succes();
        }
    }

    public void onSelectDistantEmploye(YvsGrhContratEmps y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedEmployes s = (ManagedEmployes) giveManagedBean("MEmps");
            if (s != null) {
                s.onSelectObject(y.getEmploye());
                Navigations n = (Navigations) giveManagedBean(Navigations.class);
                if (n != null) {
                    n.naviguationView("Employés", "modRh", "smenEmploye", true);
                }
            }
        }
    }

    public void onConfirmeBean() {
        onChangeStatut(entityContrat, "C");
    }

    public void onWaitBean() {
        onChangeStatut(entityContrat, "W");
    }

    public void onConfirme(YvsGrhContratEmps y) {
        onChangeStatut(y, "C");
    }

    public void onWait(YvsGrhContratEmps y) {
        onChangeStatut(y, "W");
    }

    public void onChangeStatut(YvsGrhContratEmps y, String statut) {
        onChangeStatut(y, statut, false);
    }

    public void onChangeStatut(YvsGrhContratEmps y, String statut, Boolean many) {
        if (!many) {
            if (!autoriser("grh_contrat_change_statut")) {
                openNotAcces();
                return;
            }
        }
        if (y != null ? y.getId() > 0 : false) {
            y.setStatut(statut);
            y.setDateUpdate(new Date());
            y.setAuthor(currentUser);
            dao.update(y);
            int index = listContrat.indexOf(y);
            if (index > -1) {
                listContrat.set(index, y);
            }
            if (currentContrat.getId() == y.getId()) {
                currentContrat.setStatut(statut);
                update("grp_btn_etat_contrat_emp");
            }
            if (!many) {
                succes();
                update("form_contrat_00");
            }
        }
    }

    public boolean getGenerer() {
        return generer;
    }

    public void setGenerer(boolean generer) {
        this.generer = generer;
    }

}
