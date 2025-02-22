/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.CentreAnalytique;
import yvs.base.compta.Comptes;
import yvs.base.compta.ManagedCentreAnalytique;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.ManagedJournaux;
import yvs.base.compta.UtilCompta;
import yvs.base.tiers.ManagedTiers;
import yvs.base.tiers.Tiers;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaContentModeleSaisi;
import yvs.entity.compta.YvsComptaModeleSaisie;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.etats.Columns;
import yvs.etats.Dashboards;
import yvs.etats.Gestionnaire;
import yvs.etats.Rows;
import yvs.etats.ValeurComptable;
import yvs.etats.commercial.JournalVendeur;
import yvs.init.Initialisation;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedModeleSaisie extends Managed<ModelesSasie, YvsComptaModeleSaisie> implements Serializable {
    
    private ContentModeleSaisie contentModel = new ContentModeleSaisie();
    @ManagedProperty(value = "#{modelesSasie}")
    private ModelesSasie modeleSaisie;
    private List<YvsComptaModeleSaisie> modelesSaisies;
    private Dashboards tabBalance = new Dashboards("C");
    private Dashboards tabDashboard = new Dashboards("R");
    private Dashboards grandLivre = new Dashboards("C");
    private Dashboards journal = new Dashboards("C");

    /*Les type de saisie*/
    /* 1= Saisie manuelle
     2=repetter la frappe précédente
     3=valeur calculé ( partir d'une formule ou d'une méthode)
     4=Equilibre      
     */
    private boolean initForm = true;
    private Boolean actifSearch;

//Print Etats
    private String compteDebut, compteFin, titleDebut, titleFin, genre = "C", type = "R", periode = "A";
    private Date dateDebut, dateFin;
    private long agence;
    private double credit, debit;
    private Boolean lettrer;
    
    private Date dateDebutJrn, dateFinJrn;
    private long codeJrn;
    private double creditJrn, debitJrn;
    
    private List<YvsBasePlanComptable> comptes, selectComptes, filterComptes;
    private List<YvsBaseTiers> tiers, selectTiers;
    private List<YvsComptaCentreAnalytique> centres, selectCentres;
    
    private String rapport, nature, tiersSearch, compteSearch, centreSearch;
    
    public ManagedModeleSaisie() {
        modelesSaisies = new ArrayList<>();
        
        comptes = new ArrayList<>();
        selectComptes = new ArrayList<>();
        filterComptes = new ArrayList<>();
        tiers = new ArrayList<>();
        selectTiers = new ArrayList<>();
        centres = new ArrayList<>();
        selectCentres = new ArrayList<>();
        
        tabBalance.setSociete(currentAgence.getSociete().getId());
        tabBalance.setAgence(currentAgence.getId());
        
        grandLivre.setSociete(currentAgence.getSociete().getId());
        grandLivre.setAgence(currentAgence.getId());
        
        journal.setSociete(currentAgence.getSociete().getId());
//        journal.setAgence(currentAgence.getId());

        tabDashboard.setType("valeur");
        tabDashboard.setSociete(currentAgence.getSociete().getId());
        tabDashboard.setAgence(currentAgence.getId());
    }

    public List<YvsBasePlanComptable> getFilterComptes() {
        return filterComptes;
    }

    public void setFilterComptes(List<YvsBasePlanComptable> filterComptes) {
        this.filterComptes = filterComptes;
    }
    public String getTitleDebut() {
        return titleDebut;
    }
    
    public void setTitleDebut(String titleDebut) {
        this.titleDebut = titleDebut;
    }
    
    public String getTitleFin() {
        return titleFin;
    }
    
    public void setTitleFin(String titleFin) {
        this.titleFin = titleFin;
    }
    
    public List<YvsBasePlanComptable> getSelectComptes() {
        return selectComptes;
    }
    
    public void setSelectComptes(List<YvsBasePlanComptable> selectComptes) {
        this.selectComptes = selectComptes;
    }
    
    public List<YvsComptaCentreAnalytique> getCentres() {
        return centres;
    }
    
    public void setCentres(List<YvsComptaCentreAnalytique> centres) {
        this.centres = centres;
    }
    
    public List<YvsComptaCentreAnalytique> getSelectCentres() {
        return selectCentres;
    }
    
    public void setSelectCentres(List<YvsComptaCentreAnalytique> selectCentres) {
        this.selectCentres = selectCentres;
    }
    
    public String getCompteSearch() {
        return compteSearch;
    }
    
    public void setCompteSearch(String compteSearch) {
        this.compteSearch = compteSearch;
    }
    
    public String getCentreSearch() {
        return centreSearch;
    }
    
    public void setCentreSearch(String centreSearch) {
        this.centreSearch = centreSearch;
    }
    
    public String getTiersSearch() {
        return tiersSearch;
    }
    
    public void setTiersSearch(String tiersSearch) {
        this.tiersSearch = tiersSearch;
    }
    
    public List<YvsBaseTiers> getSelectTiers() {
        return selectTiers;
    }
    
    public void setSelectTiers(List<YvsBaseTiers> selectTiers) {
        this.selectTiers = selectTiers;
    }
    
    public String getRapport() {
        return rapport;
    }
    
    public void setRapport(String rapport) {
        this.rapport = rapport;
    }
    
    public List<YvsBaseTiers> getTiers() {
        return tiers;
    }
    
    public void setTiers(List<YvsBaseTiers> tiers) {
        this.tiers = tiers;
    }
    
    public Boolean getLettrer() {
        return lettrer;
    }
    
    public void setLettrer(Boolean lettrer) {
        this.lettrer = lettrer;
    }
    
    public Dashboards getJournal() {
        return journal;
    }
    
    public void setJournal(Dashboards journal) {
        this.journal = journal;
    }
    
    public long getCodeJrn() {
        return codeJrn;
    }
    
    public void setCodeJrn(long codeJrn) {
        this.codeJrn = codeJrn;
    }
    
    public Date getDateDebutJrn() {
        return dateDebutJrn;
    }
    
    public void setDateDebutJrn(Date dateDebutJrn) {
        this.dateDebutJrn = dateDebutJrn;
    }
    
    public Date getDateFinJrn() {
        return dateFinJrn;
    }
    
    public void setDateFinJrn(Date dateFinJrn) {
        this.dateFinJrn = dateFinJrn;
    }
    
    public double getCreditJrn() {
        return creditJrn;
    }
    
    public void setCreditJrn(double creditJrn) {
        this.creditJrn = creditJrn;
    }
    
    public double getDebitJrn() {
        return debitJrn;
    }
    
    public void setDebitJrn(double debitJrn) {
        this.debitJrn = debitJrn;
    }
    
    public double getCredit() {
        return credit;
    }
    
    public void setCredit(double credit) {
        this.credit = credit;
    }
    
    public double getDebit() {
        return debit;
    }
    
    public void setDebit(double debit) {
        this.debit = debit;
    }
    
    public Dashboards getGrandLivre() {
        return grandLivre;
    }
    
    public void setGrandLivre(Dashboards grandLivre) {
        this.grandLivre = grandLivre;
    }
    
    public Dashboards getTabBalance() {
        return tabBalance;
    }
    
    public void setTabBalance(Dashboards tabBalance) {
        this.tabBalance = tabBalance;
    }
    
    public Dashboards getTabDashboard() {
        return tabDashboard;
    }
    
    public void setTabDashboard(Dashboards tabDashboard) {
        this.tabDashboard = tabDashboard;
    }
    
    public List<YvsBasePlanComptable> getComptes() {
        return comptes;
    }
    
    public void setComptes(List<YvsBasePlanComptable> comptes) {
        this.comptes = comptes;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getPeriode() {
        return periode;
    }
    
    public void setPeriode(String periode) {
        this.periode = periode;
    }
    
    public boolean isInitForm() {
        return initForm;
    }
    
    public void setInitForm(boolean initForm) {
        this.initForm = initForm;
    }
    
    public String getCompteDebut() {
        return compteDebut;
    }
    
    public void setCompteDebut(String compteDebut) {
        this.compteDebut = compteDebut;
    }
    
    public String getCompteFin() {
        return compteFin;
    }
    
    public void setCompteFin(String compteFin) {
        this.compteFin = compteFin;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
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
    
    public long getAgence() {
        return agence;
    }
    
    public void setAgence(long agence) {
        this.agence = agence;
    }
    
    public long getIdTemp() {
        return idTemp;
    }
    
    public void setIdTemp(long idTemp) {
        this.idTemp = idTemp;
    }
    
    public Boolean getActifSearch() {
        return actifSearch;
    }
    
    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }
    
    public ContentModeleSaisie getContentModel() {
        return contentModel;
    }
    
    public void setContentModel(ContentModeleSaisie contentModel) {
        this.contentModel = contentModel;
    }
    
    public ModelesSasie getModeleSaisie() {
        return modeleSaisie;
    }
    
    public void setModeleSaisie(ModelesSasie modeleSaisie) {
        this.modeleSaisie = modeleSaisie;
    }
    
    public List<YvsComptaModeleSaisie> getModelesSaisies() {
        return modelesSaisies;
    }
    
    public void setModelesSaisies(List<YvsComptaModeleSaisie> modelesSaisies) {
        this.modelesSaisies = modelesSaisies;
    }
    
    public void setMdsJour(int type) {
        contentModel.getJour().setTypeValeur(type);
    }
    
    public void setMdsNumPiece(int type) {
        contentModel.getNumPiece().setTypeValeur(type);
    }
    
    public void setMdsNumRef(int type) {
        contentModel.getReference().setTypeValeur(type);
    }
    
    public void setMdsCompteG(int type) {
        contentModel.getCompteG().setTypeValeur(type);
    }
    
    public void setMdsCompteT(int type) {
        contentModel.getCompteT().setTypeValeur(type);
    }
    
    public void setMdsLibelle(int type) {
        contentModel.getLibelle().setTypeValeur(type);
    }
    
    public void setMdsDebit(int type) {
        if (type == contentModel.getDebit().getTypeValeur()) {
            type = 0;
        }
        contentModel.getDebit().setTypeValeur(type);
    }
    
    public void setMdsCredit(int type) {
        if (type == contentModel.getCredit().getTypeValeur()) {
            type = 0;
        }
        contentModel.getCredit().setTypeValeur(type);
    }
    
    public void setMdsEcheance(int type) {
        contentModel.getEcheance().setTypeValeur(type);
    }
    
    public String getNature() {
        return nature;
    }
    
    public void setNature(String nature) {
        this.nature = nature;
    }
    
    public void doNothing() {
        
    }
    
    public void load() {
        ManagedCompte s = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (s != null) {
            if (!s.getListComptes().isEmpty()) {
                if (compteDebut != null ? compteDebut.trim().length() < 1 : true) {
                    compteDebut = s.getListComptes().get(0).getNumCompte();
                    
                    tabBalance.setCompteDebut(compteDebut);
                    tabBalance.setCompteFin(compteDebut);
                    tabBalance.setComptes(compteDebut);
                    
                    tabDashboard.setCompteDebut(compteDebut);
                    tabDashboard.setCompteFin(compteDebut);
                    tabDashboard.setComptes(compteDebut);
                    
                    grandLivre.setCompteDebut(compteDebut);
                    grandLivre.setCompteFin(compteDebut);
                    grandLivre.setComptes(compteDebut);
                    
                }
                if (compteFin != null ? compteFin.trim().length() < 1 : true) {
                    compteFin = s.getListComptes().get(s.getListComptes().size() - 1).getNumCompte();
                }
            }
        }
        ManagedJournaux j = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
        if (j != null) {
            if (!j.getJournaux().isEmpty()) {
                if (codeJrn < 1) {
                    codeJrn = j.getJournaux().get(0).getId();
                }
            }
        }
        if (agence < 1) {
            agence = currentAgence.getId();
        }
        if (dateDebut == null) {
            YvsBaseExercice exo = null;
            if (currentExo != null ? currentExo.getId() > 0 : false) {
                exo = currentExo;
            } else {
                exo = giveExerciceActif();
            }
            if (exo != null ? exo.getId() > 0 : false) {
                dateDebut = exo.getDateDebut();
                dateFin = exo.getDateFin();
                dateDebutJrn = exo.getDateDebut();
                dateFinJrn = exo.getDateFin();
            }
        }
        ManagedTiers wt = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (wt != null ? tiers.isEmpty() : false) {
            tiers = new ArrayList<>(wt.getTiers_all());
        }
        ManagedCompte wc = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (wc != null ? comptes.isEmpty() : false) {
            comptes = new ArrayList<>(wc.getComptes_all());
        }
        ManagedCentreAnalytique wa = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
        if (wa != null ? centres.isEmpty() : false) {
            centres = new ArrayList<>(wa.getCentres_all());
        }
        load(tabBalance);
        load(tabDashboard);
    }
    
    private void load(Dashboards etat) {
        loadCompteByType(etat, false);
        if (etat.getDateDebut() == null) {
            YvsBaseExercice exo = null;
            if (currentExo != null ? currentExo.getId() > 0 : false) {
                exo = currentExo;
            } else {
                exo = giveExerciceActif();
            }
            if (exo != null ? exo.getId() > 0 : false) {
                etat.setDateDebut(exo.getDateDebut());
                etat.setDateDebut(exo.getDateFin());
            }
        }
    }
    
    public void loadCompteByType(Dashboards etat, boolean init) {
        if (etat.getNature().equals("A")) {
            ManagedCentreAnalytique s = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
            if (s != null) {
                if (!s.getCentres().isEmpty()) {
                    if (init || (etat.getCompteDebut() != null ? etat.getCompteDebut().trim().length() < 1 : true)) {
                        etat.setCompteDebut(s.getCentres().get(0).getCodeRef());
                    }
                    if (init || (etat.getCompteFin() != null ? etat.getCompteFin().trim().length() < 1 : true)) {
                        etat.setCompteFin(s.getCentres().get(s.getCentres().size() - 1).getCodeRef());
                    }
                }
            }
        } else if (etat.getNature().equals("T")) {
            ManagedTiers s = (ManagedTiers) giveManagedBean(ManagedTiers.class);
            if (s != null) {
                if (!s.getListTiers().isEmpty()) {
                    if (init || (etat.getCompteDebut() != null ? etat.getCompteDebut().trim().length() < 1 : true)) {
                        etat.setCompteDebut(s.getListTiers().get(0).getCodeTiers());
                    }
                    if (init || (etat.getCompteFin() != null ? etat.getCompteFin().trim().length() < 1 : true)) {
                        etat.setCompteFin(s.getListTiers().get(s.getListTiers().size() - 1).getCodeTiers());
                    }
                }
            }
        } else {
            ManagedCompte s = (ManagedCompte) giveManagedBean(ManagedCompte.class);
            if (s != null) {
                if (!s.getListComptes().isEmpty()) {
                    if (init || (etat.getCompteDebut() != null ? etat.getCompteDebut().trim().length() < 1 : true)) {
                        etat.setCompteDebut(s.getListComptes().get(0).getNumCompte());
                    }
                    if (init || (etat.getCompteFin() != null ? etat.getCompteFin().trim().length() < 1 : true)) {
                        etat.setCompteFin(s.getListComptes().get(s.getListComptes().size() - 1).getNumCompte());
                    }
                }
            }
        }
    }
    
    public void loadSimpleCompteByType(String genre, boolean init) {
        if (genre.equals("A")) {
            ManagedCentreAnalytique s = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
            if (s != null) {
                if (!s.getCentres().isEmpty()) {
                    if (init || (getCompteDebut() != null ? getCompteDebut().trim().length() < 1 : true)) {
                        setCompteDebut(s.getCentres().get(0).getCodeRef());
                    }
                    if (init || (getCompteFin() != null ? getCompteFin().trim().length() < 1 : true)) {
                        setCompteFin(s.getCentres().get(s.getCentres().size() - 1).getCodeRef());
                    }
                }
            }
        } else if (genre.equals("T")) {
            ManagedTiers s = (ManagedTiers) giveManagedBean(ManagedTiers.class);
            if (s != null) {
                if (!s.getListTiers().isEmpty()) {
                    if (init || (getCompteDebut() != null ? getCompteDebut().trim().length() < 1 : true)) {
                        setCompteDebut(s.getListTiers().get(0).getCodeTiers());
                    }
                    if (init || (getCompteFin() != null ? getCompteFin().trim().length() < 1 : true)) {
                        setCompteFin(s.getListTiers().get(s.getListTiers().size() - 1).getCodeTiers());
                    }
                }
            }
        } else {
            ManagedCompte s = (ManagedCompte) giveManagedBean(ManagedCompte.class);
            if (s != null) {
                if (!s.getListComptes().isEmpty()) {
                    if (init || (getCompteDebut() != null ? getCompteDebut().trim().length() < 1 : true)) {
                        setCompteDebut(s.getListComptes().get(0).getNumCompte());
                    }
                    if (init || (getCompteFin() != null ? getCompteFin().trim().length() < 1 : true)) {
                        setCompteFin(s.getListComptes().get(s.getListComptes().size() - 1).getNumCompte());
                    }
                }
            }
        }
    }
    
    public void loadSimpleCompteForEtat(Dashboards etat, boolean init) {
        String compte = "";
        if (etat.getNature().equals("A")) {
            ManagedCentreAnalytique s = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
            if (s != null) {
                if (!s.getCentres_all().isEmpty()) {
                    if (init || (getCompteDebut() != null ? getCompteDebut().trim().length() < 1 : true)) {
                        compte = s.getCentres_all().get(0).getCodeRef();
                    }
                }
            }
        } else if (etat.getNature().equals("T")) {
            ManagedTiers s = (ManagedTiers) giveManagedBean(ManagedTiers.class);
            if (s != null) {
                if (!s.getTiers_all().isEmpty()) {
                    if (init || (getCompteDebut() != null ? getCompteDebut().trim().length() < 1 : true)) {
                        compte = s.getTiers_all().get(0).getCodeTiers();
                    }
                }
            }
        } else {
            ManagedCompte s = (ManagedCompte) giveManagedBean(ManagedCompte.class);
            if (s != null) {
                if (!s.getComptes_all().isEmpty()) {
                    if (init || (getCompteDebut() != null ? getCompteDebut().trim().length() < 1 : true)) {
                        compte = s.getComptes_all().get(0).getNumCompte();
                    }
                }
            }
        }
        etat.setCompteDebut(compte);
        etat.setCompteFin(compte);
        etat.setComptes(compte);
    }
    
    @Override
    public void loadAll() {
        loadAllModel(true);
    }
    
    private void loadAllModel(boolean avancer) {
        ParametreRequete p = new ParametreRequete("y.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
        paginator.addParam(p);
        modelesSaisies = paginator.executeDynamicQuery("YvsComptaModeleSaisie", "y.typeModele, y.intitule", avancer, initForm, (int) imax, dao);
    }
    
    public void loadAllModelActif(Boolean actif) {
        actifSearch = actif;
        addParamActif();
    }
    
    private YvsComptaContentModeleSaisi buildContent(ContentModeleSaisie c) {
        YvsComptaContentModeleSaisi cm = new YvsComptaContentModeleSaisi();
        cm.setCompteGeneral((String) c.getCompteG().getValeur());
        cm.setMdsCompteGeneral(c.getCompteG().getTypeValeur());
        cm.setCompteTiers((String) c.getCompteT().getValeur());
        cm.setMdsCompteTiers(c.getCompteT().getTypeValeur());
        cm.setCredit((double) (c.getDebit().getValeur() != null ? c.getDebit().getValeur() : 0.0));
        cm.setDebit((double) (c.getDebit().getValeur() != null ? c.getDebit().getValeur() : 0.0));
        cm.setMdsCredit(c.getCredit().getTypeValeur());
        cm.setMdsDebit(c.getDebit().getTypeValeur());
        cm.setEcheance(c.getEcheance().getDate());
        cm.setMdsEcheance(c.getEcheance().getTypeValeur());
        cm.setId(c.getId());
        cm.setJour(c.getJour().getJour());
        cm.setModeSaisieJour(c.getJour().getTypeValeur());
        cm.setLibelle((String) c.getLibelle().getValeur());
        cm.setMdsLibelle(c.getLibelle().getTypeValeur());
        cm.setNumPiece((String) c.getNumPiece().getValeur());
        cm.setMdsNumPiece(c.getNumPiece().getTypeValeur());
        cm.setNumRef((String) c.getReference().getValeur());
        cm.setMdsNumRef(c.getReference().getTypeValeur());
        return cm;
    }
    
    @Override
    public boolean controleFiche(ModelesSasie bean) {
        if (bean.getIntitule() == null) {
            getErrorMessage("Veuillez entrer l'intitulé du modèle");
            return false;
        }
        if (bean.getTypeModele() == null) {
            getErrorMessage("Veuillez entrer le type du modèle");
            return false;
        }
        return true;
    }
    
    private boolean controleAddContent(ContentModeleSaisie c) {
        if (controleFiche(modeleSaisie)) {
            if (c.getJour().getTypeValeur() <= 0) {
                getErrorMessage("Le model de saisie du jour est absent");
                return false;
            }
            if (c.getNumPiece().getTypeValeur() <= 0) {
                getErrorMessage("Le model de saisie du numéro de pièce est absent");
                return false;
            }
            if (c.getCompteG().getTypeValeur() <= 0) {
                getErrorMessage("Le model de saisie du compte générale est absent");
                return false;
            }
            if (c.getCompteT().getTypeValeur() <= 0) {
                getErrorMessage("Le model de saisie du compte tiers est absent");
                return false;
            }
            if (c.getCredit().getTypeValeur() <= 0 && c.getDebit().getTypeValeur() <= 0) {
                if (c.getCredit().getTypeValeur() <= 0) {
                    getErrorMessage("Le model de saisie du crédit est absent");
                    return false;
                }
                if (c.getDebit().getTypeValeur() <= 0) {
                    getErrorMessage("Le model de saisie du débit est absent");
                    return false;
                }
            }
            if (c.getEcheance().getTypeValeur() <= 0) {
                getErrorMessage("Le model de saisie de l'echéance est absent");
                return false;
            }
            if (c.getLibelle().getTypeValeur() <= 0) {
                getErrorMessage("Le model de saisie du libellé est absent");
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
    
    public void openToDeleteContent(YvsComptaContentModeleSaisi c) {
        contentModel = UtilCompta.buildBeanContentModel(c);
        openDialog("dlgDelContent");
    }
    
    public void openToDeleteModel(YvsComptaModeleSaisie m) {
        populateView(UtilCompta.buildBeanModel(m));
        openDialog("dlgDelModel");
    }
    
    @Override
    public ModelesSasie recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void populateView(ModelesSasie bean) {
        cloneObject(modeleSaisie, bean);
    }
    
    @Override
    public void resetFiche() {
        resetFiche(modeleSaisie);
        modeleSaisie.getContenus().clear();
        resetFicheContent();
    }
    
    public void resetFicheContent() {
        contentModel = new ContentModeleSaisie();
        contentModel.getJour().setTypeValeur(1);
        contentModel.getNumPiece().setTypeValeur(1);
        contentModel.getReference().setTypeValeur(1);
        contentModel.getCompteG().setTypeValeur(1);
        contentModel.getCompteT().setTypeValeur(1);
        contentModel.getEcheance().setTypeValeur(1);
        contentModel.getLibelle().setTypeValeur(1);
        update("form_edit_contentSMPC");
    }
    
    @Override
    public boolean saveNew() {
        if (!modeleSaisie.getContenus().isEmpty()) {
            YvsComptaModeleSaisie mod = modeleSaisie.getContenus().get(0).getModeleSaisie();
            mod.setIntitule(modeleSaisie.getIntitule());
            mod.setTypeModele(modeleSaisie.getTypeModele());
            if (modeleSaisie.getId() < 1) {
                mod.setId(null);
                mod = (YvsComptaModeleSaisie) dao.save1(mod);
            } else {
                mod.setId(modeleSaisie.getId());
                dao.update(mod);
            }
            for (YvsComptaContentModeleSaisi cm : modeleSaisie.getContenus()) {
                cm.setModeleSaisie(mod);
                if (cm.getId() != null ? cm.getId() < 1 : true) {
                    cm.setId(null);
                    cm = (YvsComptaContentModeleSaisi) dao.save1(cm);
                } else {
                    dao.update(cm);
                }
            }
            mod.setContentsModel(new ArrayList<>(modeleSaisie.getContenus()));
            int idx = modelesSaisies.indexOf(mod);
            if (idx > -1) {
                modelesSaisies.set(idx, mod);
            } else {
                modelesSaisies.add(0, mod);
            }
            resetFiche();
            succes();
        } else {
            getErrorMessage("Le modèle est vide !");
        }
        return true;
    }
    long idTemp = -1000;
    
    public void addContentModel() {
        if (controleAddContent(contentModel)) {
            YvsComptaContentModeleSaisi c = buildContent(contentModel);
            if (contentModel.getId() <= 0) {
                c.setId(idTemp++);
            } else {
                c.setId(contentModel.getId());
            }
            c.setModeleSaisie(new YvsComptaModeleSaisie(null));
            c.getModeleSaisie().setActif(modeleSaisie.isActif());
            c.getModeleSaisie().setAuthor(currentUser);
            c.getModeleSaisie().setIntitule(modeleSaisie.getIntitule());
            c.getModeleSaisie().setSociete(currentAgence.getSociete());
            c.getModeleSaisie().setTypeModele(modeleSaisie.getTypeModele());
            int idx = modeleSaisie.getContenus().indexOf(c);
            if (idx >= 0) {
                modeleSaisie.getContenus().set(idx, c);
            } else {
                modeleSaisie.getContenus().add(c);
            }
            resetFicheContent();
        }
    }
    
    @Override
    public void deleteBean() {
        try {
            YvsComptaModeleSaisie ms = new YvsComptaModeleSaisie(modeleSaisie.getId());
            if (modeleSaisie.getId() > 0) {
                ms.setAuthor(currentUser);
                dao.delete(ms);
                modelesSaisies.remove(ms);
            } else {
                modelesSaisies.remove(ms);
            }
            succes();
            update("table_model_saisiPC");
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cette ligne !");
            getException("Lymytz Error :>>>", ex);
        }
    }
    
    public void deleteContent() {
        try {
            YvsComptaContentModeleSaisi ms = new YvsComptaContentModeleSaisi(contentModel.getId());
            if (contentModel.getId() > 0) {
                ms.setAuthor(currentUser);
                dao.delete(ms);
                modeleSaisie.getContenus().remove(ms);
            } else {
                modeleSaisie.getContenus().remove(ms);
            }
            succes();
            update("table_contentmodelSC");
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cette ligne !");
            getException("Lymytz Error :>>>", ex);
        }
    }
    
    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void loadOnView(SelectEvent ev) {
        YvsComptaModeleSaisie model = (YvsComptaModeleSaisie) ev.getObject();
        populateView(UtilCompta.buildBeanModel(model));
    }
    
    public void loadOnViewContent(SelectEvent ev) {
        YvsComptaContentModeleSaisi content = (YvsComptaContentModeleSaisi) ev.getObject();
        contentModel = UtilCompta.buildBeanContentModel(content);
    }
    
    public void unLoadOnViewContent(UnselectEvent ev) {
        resetFicheContent();
    }
    
    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void changeActif(YvsComptaModeleSaisie y) {
        if (y != null ? y.getId() > 0 : false) {
            y.setActif(!y.getActif());
            y.setAuthor(currentUser);
            dao.update(y);
            succes();
        }
    }
    
    public void addParamActif() {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
        loadAllModel(true);
    }
    
    public void printBalance() {
        printBalance("8c");
    }
    
    public void printBalanceXLS(String colonne) {
        try {
            if (tabBalance != null ? tabBalance.getValeurs().isEmpty() : true) {
                getErrorMessage("Vous devez charger le tableau");
                return;
            }
            String fileName = Initialisation.getCheminAllDoc() + Initialisation.FILE_SEPARATOR + "balance.xlsx";
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Balance");
            final String[] titles = new String[]{"COMPTE", "LIBELLE", "DI", "CI", "DP", "CP", "DSP", "CSP", "DSC", "CSC"};
            int numRow = 0;
            Font font = sheet.getWorkbook().createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            cellStyle.setFont(font);
            cellStyle.setWrapText(true);
            
            Row row = sheet.createRow(numRow++);
            for (int i = 0; i < titles.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(titles[i]);
                cell.setCellStyle(cellStyle);
            }
            
            for (JournalVendeur item : tabBalance.getValeurs()) {
                int numCell = 0;
                row = sheet.createRow(numRow++);
                // Numero
                Cell cell = row.createCell(numCell++);
                cell.setCellValue(item.getNumero());
                // Intitulé
                cell = row.createCell(numCell++);
                cell.setCellValue(item.getPeriode());
                // Débit Initial
                cell = row.createCell(numCell++);
                cell.setCellValue(Initialisation.Lnf.format(item.getDi()));
                // Crédit Initial
                cell = row.createCell(numCell++);
                cell.setCellValue(Initialisation.Lnf.format(item.getCi()));
                // Débit Périodique
                cell = row.createCell(numCell++);
                cell.setCellValue(Initialisation.Lnf.format(item.getDp()));
                // Crédit Périodique
                cell = row.createCell(numCell++);
                cell.setCellValue(Initialisation.Lnf.format(item.getCp()));
                // Débit Solde Periodique
                cell = row.createCell(numCell++);
                cell.setCellValue(Initialisation.Lnf.format(item.getDsp()));
                // Crédit Solde Periodique
                cell = row.createCell(numCell++);
                cell.setCellValue(Initialisation.Lnf.format(item.getCsp()));
                // Débit Solde Cumulé
                cell = row.createCell(numCell++);
                cell.setCellValue(Initialisation.Lnf.format(item.getDsc()));
                // Crédit Solde Cumulé
                cell = row.createCell(numCell++);
                cell.setCellValue(Initialisation.Lnf.format(item.getCsc()));
            }
            CellStyle cellTotal = sheet.getWorkbook().createCellStyle();
            cellTotal.setFont(font);
            cellTotal.setWrapText(true);
            // TOTAL BALANCE
            int numCell = 0;
            // Numero
            row = sheet.createRow(numRow++);
            Cell cell = row.createCell(numCell++);
            cell.setCellValue("");
            // Intitulé
            cell = row.createCell(numCell++);
            cell.setCellValue("TOTAUX");
            cell.setCellStyle(cellTotal);
            // Débit Initial
            cell = row.createCell(numCell++);
            cell.setCellValue(Initialisation.Lnf.format(tabBalance.summaryGroup("", "di")));
            cell.setCellStyle(cellTotal);
            // Crédit Initial
            cell = row.createCell(numCell++);
            cell.setCellValue(Initialisation.Lnf.format(tabBalance.summaryGroup("", "ci")));
            cell.setCellStyle(cellTotal);
            // Débit Périodique
            cell = row.createCell(numCell++);
            cell.setCellValue(Initialisation.Lnf.format(tabBalance.summaryGroup("", "dp")));
            cell.setCellStyle(cellTotal);
            // Crédit Périodique
            cell = row.createCell(numCell++);
            cell.setCellValue(Initialisation.Lnf.format(tabBalance.summaryGroup("", "cp")));
            cell.setCellStyle(cellTotal);
            // Débit Solde Periodique
            cell = row.createCell(numCell++);
            cell.setCellValue(Initialisation.Lnf.format(tabBalance.summaryGroup("", "dsp")));
            cell.setCellStyle(cellTotal);
            // Crédit Solde Periodique
            cell = row.createCell(numCell++);
            cell.setCellValue(Initialisation.Lnf.format(tabBalance.summaryGroup("", "csp")));
            cell.setCellStyle(cellTotal);
            // Débit Solde Cumulé
            cell = row.createCell(numCell++);
            cell.setCellValue(Initialisation.Lnf.format(tabBalance.summaryGroup("", "dsc")));
            cell.setCellStyle(cellTotal);
            // Crédit Solde Cumulé
            cell = row.createCell(numCell++);
            cell.setCellValue(Initialisation.Lnf.format(tabBalance.summaryGroup("", "csc")));
            cell.setCellStyle(cellTotal);
            cellTotal.setWrapText(true);

            //SOLDES
            numCell = 0;
            // Numero
            row = sheet.createRow(numRow++);
            cell = row.createCell(numCell++);
            cell.setCellValue("");
            // Intitulé
            cell = row.createCell(numCell++);
            cell.setCellValue("SOLDES");
            cell.setCellStyle(cellTotal);
            // Débit Initial
            cell = row.createCell(numCell++);
            cell.setCellValue(Initialisation.Lnf.format(tabBalance.summaryGroup("di")));
            cell.setCellStyle(cellTotal);
            // Crédit Initial
            cell = row.createCell(numCell++);
            cell.setCellValue(Initialisation.Lnf.format(tabBalance.summaryGroup("ci")));
            cell.setCellStyle(cellTotal);
            // Débit Périodique
            cell = row.createCell(numCell++);
            cell.setCellValue(Initialisation.Lnf.format(tabBalance.summaryGroup("dp")));
            cell.setCellStyle(cellTotal);
            // Crédit Périodique
            cell = row.createCell(numCell++);
            cell.setCellValue(Initialisation.Lnf.format(tabBalance.summaryGroup("cp")));
            cell.setCellStyle(cellTotal);
            // Débit Solde Periodique
            cell = row.createCell(numCell++);
            cell.setCellValue(Initialisation.Lnf.format(tabBalance.summaryGroup("dsp")));
            cell.setCellStyle(cellTotal);
            // Crédit Solde Periodique
            cell = row.createCell(numCell++);
            cell.setCellValue(Initialisation.Lnf.format(tabBalance.summaryGroup("csp")));
            cell.setCellStyle(cellTotal);
            // Débit Solde Cumulé
            cell = row.createCell(numCell++);
            cell.setCellValue(Initialisation.Lnf.format(tabBalance.summaryGroup("dsc")));
            cell.setCellStyle(cellTotal);
            // Crédit Solde Cumulé
            cell = row.createCell(numCell++);
            cell.setCellValue(Initialisation.Lnf.format(tabBalance.summaryGroup("csc")));
            cell.setCellStyle(cellTotal);
            
            try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
                workbook.write(outputStream);
            }
            Util.getDownloadFile(fileName, "balance");
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void printBalance(String colonne) {
        Map<String, Object> param = new HashMap<>();
        param.put("COMPTES", tabBalance.getComptes());
        param.put("DATE_DEBUT", tabBalance.getDateDebut());
        param.put("DATE_FIN", tabBalance.getDateFin());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", (int) tabBalance.getAgence());
        param.put("JOURNAL", (int) tabBalance.getJournal());
        param.put("TYPE", tabBalance.getNature());
        param.put("LOGO", returnLogo());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        String etat;
        switch (tabBalance.getNature()) {
            case "C":
                switch (colonne) {
                    case "2c":
                        etat = "balance_general_2c";
                        break;
                    case "6c":
                        etat = "balance_general_6c";
                        break;
                    default:
                        etat = "balance_general";
                        break;
                }
                break;
            case "T":
                param.put("NATURE", tabBalance.getOrdres());
                switch (colonne) {
                    case "2c":
                        etat = "balance_tiers_2c";
                        break;
                    case "6c":
                        etat = "balance_tiers_6c";
                        break;
                    default:
                        etat = "balance_tiers";
                        break;
                }
                break;
            default:
                switch (colonne) {
                    case "2c":
                        etat = "balance_analytique_2c";
                        break;
                    case "6c":
                        etat = "balance_analytique_6c";
                        break;
                    default:
                        etat = "balance_analytique";
                        break;
                }
                break;
        }
        executeReport(etat, param);
    }
    
    public void loadDataBalance() {
        tabBalance.returnBalance(dao);
    }
    
    public void printDashboardXLS() {
        try {
            if (tabDashboard != null ? tabDashboard.getValeurs().isEmpty() : true) {
                getErrorMessage("Vous devez charger le tableau");
                return;
            }
            String fileName = Initialisation.getCheminAllDoc() + Initialisation.FILE_SEPARATOR + "dashboard.xlsx";
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("DashBoard");
            List<String> titles = new ArrayList<>();
            titles.add("NUMERO");
            titles.add("INTITULE");
            for (Columns p : tabDashboard.getColonnes()) {
                titles.add(p.getValeur().toString());
            }
            titles.add("SOLDE");
            int numRow = 0;
            Font font = sheet.getWorkbook().createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            cellStyle.setFont(font);
            cellStyle.setWrapText(true);
            
            CellStyle cellValeur = sheet.getWorkbook().createCellStyle();
            cellValeur.setAlignment(CellStyle.ALIGN_RIGHT);
            cellValeur.setWrapText(true);
            
            CellStyle cellTotal = sheet.getWorkbook().createCellStyle();
            cellTotal.setAlignment(CellStyle.ALIGN_RIGHT);
            cellTotal.setFont(font);
            cellTotal.setWrapText(true);
            
            Row row = sheet.createRow(numRow++);
            for (int i = 0; i < titles.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(titles.get(i));
                cell.setCellStyle(cellStyle);
            }
            
            for (int ligne = 0; ligne < tabDashboard.getLignes().size(); ligne++) {
                row = sheet.createRow(numRow++);
                Rows item = (Rows) tabDashboard.getLignes().get(ligne);
                int numCell = 0;
                // Numero
                Cell cell = row.createCell(numCell++);
                cell.setCellValue(item.getValeur().toString());
                //Intitule
                cell = row.createCell(numCell++);
                cell.setCellValue(item.getLibelle());
                
                for (int colonne = 0; colonne < tabDashboard.getColonnes().size(); colonne++) {
                    // Valeur
                    JournalVendeur get = tabDashboard.get(ligne, colonne);
                    Object value = get.get(tabDashboard.getType());
                    double valeur = 0;
                    if (value != null ? value instanceof Double : false) {
                        valeur = Double.valueOf(value.toString());
                    }
                    String prefixe = "";
                    if (valeur > 0) {
                        prefixe = " D";
                    } else if (valeur < 0) {
                        prefixe = " C";
                    }
                    cell = row.createCell(numCell++);
                    cell.setCellValue(DN(Math.abs(valeur)) + "" + prefixe);
                    if (get.isOnTotal() || get.isOnHead()) {
                        cell.setCellStyle(cellTotal);
                    } else {
                        cell.setCellStyle(cellValeur);
                    }
                }
                // Solde
                double solde = tabDashboard.sumRow(ligne);
                String prefixe = "";
                if (solde > 0) {
                    prefixe = " D";
                } else if (solde < 0) {
                    prefixe = " C";
                }
                cell = row.createCell(numCell++);
                cell.setCellValue(DN(Math.abs(solde)) + "" + prefixe);
                cell.setCellStyle(cellTotal);
            }
            
            try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
                workbook.write(outputStream);
            }
            Util.getDownloadFile(fileName, "dashboard");
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void printDashboard() {
        Map<String, Object> param = new HashMap<>();
        param.put("COMPTE_DEBUT", tabDashboard.getCompteDebut());
        param.put("COMPTE_FIN", tabDashboard.getCompteFin());
        param.put("DATE_DEBUT", tabDashboard.getDateDebut());
        param.put("DATE_FIN", tabDashboard.getDateFin());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("SOCIETE", (int) tabDashboard.getSociete());
        param.put("AGENCE", (int) tabDashboard.getAgence());
        param.put("TYPE", tabDashboard.getNature());
        param.put("PERIODE", tabDashboard.getPeriode());
        param.put("IMG_LOGO", returnLogo());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        switch (tabDashboard.getPeriode()) {
            case "M":
                executeReport("dashboard_mensuel", param);
                break;
            case "T":
                executeReport("dashboard_trimestre", param);
                break;
            default:
                executeReport("dashboard_annuel", param);
                break;
        }
    }
    
    public void loadDataDashboard() {
        tabDashboard.returnDashboard(dao);
    }
    
    public void printGrandLivreXLS() {
        try {
            if (grandLivre != null ? grandLivre.getComptables().isEmpty() : true) {
                getErrorMessage("Vous devez charger le tableau");
                return;
            }
            String fileName = Initialisation.getCheminAllDoc() + Initialisation.FILE_SEPARATOR + "grand_livre.xlsx";
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Grand Livre");
            List<String> titles = new ArrayList<>();
            if (!grandLivre.getNature().equals("C")) {
                titles.add("COMPTE");
            }
            titles.add("NUMERO");
            if (!(grandLivre.getNature().equals("A") && grandLivre.isCumule())) {
                titles.add("DATE PIECE");
                titles.add("JOURNAL");
            }
            if (!grandLivre.getNature().equals("T") && !(grandLivre.getNature().equals("A") && grandLivre.isCumule())) {
                titles.add("TIERS");
            }
            titles.add("LIBELLE");
            titles.add("DEBIT");
            titles.add("CREDIT");
            titles.add("SOLDE PROG.");
            
            int numRow = 0;
            Font font = sheet.getWorkbook().createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            cellStyle.setFont(font);
            cellStyle.setWrapText(true);
            
            Row row = sheet.createRow(numRow++);
            Cell cell;
            for (int i = 0; i < titles.size(); i++) {
                cell = row.createCell(i);
                cell.setCellValue(titles.get(i));
                cell.setCellStyle(cellStyle);
            }
            
            CellStyle cellTotal = sheet.getWorkbook().createCellStyle();
            cellTotal.setFont(font);
            cellTotal.setWrapText(true);
            
            CellStyle cellSolde = sheet.getWorkbook().createCellStyle();
            cellSolde.setAlignment(CellStyle.ALIGN_RIGHT);
            cellSolde.setFont(font);
            cellSolde.setWrapText(true);
            
            for (ValeurComptable element : grandLivre.getComptables()) {
                row = sheet.createRow(numRow++);
                cell = row.createCell(0);
                cell.setCellValue("[" + element.getCode() + "] " + element.getIntitule());
                double solde = 0;
                for (ValeurComptable item : element.getSous()) {
                    int numCell = 0;
                    row = sheet.createRow(numRow++);
                    if (!grandLivre.getNature().equals("C")) {
                        // Compte
                        cell = row.createCell(numCell++);
                        cell.setCellValue(item.getCompte());
                    }
                    // Numero
                    cell = row.createCell(numCell++);
                    cell.setCellValue(item.getNumero());
                    if (!(grandLivre.getNature().equals("A") && grandLivre.isCumule())) {
                        // Date Piece
                        cell = row.createCell(numCell++);
                        cell.setCellValue(formatDate.format(item.getDate()));
                        // Journal
                        cell = row.createCell(numCell++);
                        cell.setCellValue(item.getJournal());
                    }
                    if (!grandLivre.getNature().equals("T") && !(grandLivre.getNature().equals("A") && grandLivre.isCumule())) {
                        // Tiers
                        cell = row.createCell(numCell++);
                        cell.setCellValue(nameTiers(item.getCompteTiers(), item.getTableTiers(), "N"));
                    }
                    // Libelle
                    cell = row.createCell(numCell++);
                    cell.setCellValue(grandLivre.isCumule() ? item.getDesignation() : item.getLibelle());
                    // Débit
                    cell = row.createCell(numCell++);
                    cell.setCellValue(item.getDebit());
                    // Crédit
                    cell = row.createCell(numCell++);
                    cell.setCellValue(item.getCredit());
                    // Solde
                    solde += (item.getDebit() - item.getCredit());
                    cell = row.createCell(numCell++);
                    cell.setCellValue(DN(Math.abs(solde)) + (solde > 0 ? " D" : (solde < 0 ? " C" : "")));
                    cell.setCellStyle(cellSolde);
                }

                //TOTAL PERIODIQUE
                int numCell = 0;
                row = sheet.createRow(numRow++);
                if (!grandLivre.getNature().equals("C")) {
                    // Compte
                    cell = row.createCell(numCell++);
                    cell.setCellValue("");
                    cell.setCellStyle(cellTotal);
                }
                // Numero
                cell = row.createCell(numCell++);
                cell.setCellValue("TOTAL PERIODIQUE");
                cell.setCellStyle(cellTotal);
                if (!(grandLivre.getNature().equals("A") && grandLivre.isCumule())) {
                    // Date Piece
                    cell = row.createCell(numCell++);
                    cell.setCellValue("");
                    cell.setCellStyle(cellTotal);
                    // Journal
                    cell = row.createCell(numCell++);
                    cell.setCellValue("");
                    cell.setCellStyle(cellTotal);
                }
                if (!grandLivre.getNature().equals("T") && !(grandLivre.getNature().equals("A") && grandLivre.isCumule())) {
                    // Tiers
                    cell = row.createCell(numCell++);
                    cell.setCellValue("");
                    cell.setCellStyle(cellTotal);
                }
                // Libelle
                cell = row.createCell(numCell++);
                cell.setCellValue("TOTAL PERIODE : " + fmdMy.format(grandLivre.getDateDebut()) + " - " + fmdMy.format(grandLivre.getDateFin()));
                cell.setCellStyle(cellTotal);
                // Débit
                double debit = grandLivre.summaryPeriodComptaResume(element, element.getSous(), "debit");
                cell = row.createCell(numCell++);
                cell.setCellValue(debit);
                cell.setCellStyle(cellTotal);
                // Crédit
                double credit = grandLivre.summaryPeriodComptaResume(element, element.getSous(), "credit");
                cell = row.createCell(numCell++);
                cell.setCellValue(credit);
                cell.setCellStyle(cellTotal);
                // Solde
                solde = debit - credit;
                cell = row.createCell(numCell++);
                cell.setCellValue(DN(Math.abs(solde)) + (solde > 0 ? " D" : (solde < 0 ? " C" : "")));
                cell.setCellStyle(cellSolde);

                //SOLDE FINAL
                numCell = 0;
                row = sheet.createRow(numRow++);
                if (!grandLivre.getNature().equals("C")) {
                    // Compte
                    cell = row.createCell(numCell++);
                    cell.setCellValue("");
                    cell.setCellStyle(cellTotal);
                }
                // Numero
                cell = row.createCell(numCell++);
                cell.setCellValue("SOLDE FINAL");
                cell.setCellStyle(cellTotal);
                if (!(grandLivre.getNature().equals("A") && grandLivre.isCumule())) {
                    // Date Piece
                    cell = row.createCell(numCell++);
                    cell.setCellValue("");
                    cell.setCellStyle(cellTotal);
                    // Journal
                    cell = row.createCell(numCell++);
                    cell.setCellValue("");
                    cell.setCellStyle(cellTotal);
                }
                if (!grandLivre.getNature().equals("T") && !(grandLivre.getNature().equals("A") && grandLivre.isCumule())) {
                    // Tiers
                    cell = row.createCell(numCell++);
                    cell.setCellValue("");
                    cell.setCellStyle(cellTotal);
                }
                // Libelle
                cell = row.createCell(numCell++);
                cell.setCellValue("SOLDE FINAL DU COMPTE " + element.getCode());
                cell.setCellStyle(cellTotal);
                solde = element.getSoldeDebit() - element.getSoldeCredit();
                // Débit
                cell = row.createCell(numCell++);
                cell.setCellValue(solde > 0 ? solde : 0);
                cell.setCellStyle(cellTotal);
                // Crédit
                cell = row.createCell(numCell++);
                cell.setCellValue(solde < 0 ? Math.abs(solde) : 0);
                cell.setCellStyle(cellTotal);
                // Solde
                cell = row.createCell(numCell++);
                cell.setCellValue(0);
                cell.setCellStyle(cellTotal);
            }

            //TOTAUX
            int numCell = 0;
            row = sheet.createRow(numRow++);
            if (!grandLivre.getNature().equals("C")) {
                // Compte
                cell = row.createCell(numCell++);
                cell.setCellValue("");
                cell.setCellStyle(cellTotal);
            }
            // Numero
            cell = row.createCell(numCell++);
            cell.setCellValue("TOTAUX");
            cell.setCellStyle(cellTotal);
            if (!(grandLivre.getNature().equals("A") && grandLivre.isCumule())) {
                // Date Piece
                cell = row.createCell(numCell++);
                cell.setCellValue("");
                cell.setCellStyle(cellTotal);
                // Journal
                cell = row.createCell(numCell++);
                cell.setCellValue("");
                cell.setCellStyle(cellTotal);
            }
            if (!grandLivre.getNature().equals("T") && !(grandLivre.getNature().equals("A") && grandLivre.isCumule())) {
                // Tiers
                cell = row.createCell(numCell++);
                cell.setCellValue("");
                cell.setCellStyle(cellTotal);
            }
            // Libelle
            cell = row.createCell(numCell++);
            cell.setCellValue("");
            cell.setCellStyle(cellTotal);
            // Débit
            double debit = grandLivre.summaryComptaResume("debit");
            cell = row.createCell(numCell++);
            cell.setCellValue(debit);
            cell.setCellStyle(cellTotal);
            // Crédit
            double credit = grandLivre.summaryComptaResume("credit");
            cell = row.createCell(numCell++);
            cell.setCellValue(credit);
            cell.setCellStyle(cellTotal);
            // Solde
            double solde = debit - credit;
            cell = row.createCell(numCell++);
            cell.setCellValue(DN(Math.abs(solde)) + (solde > 0 ? " D" : (solde < 0 ? " C" : "")));
            cell.setCellStyle(cellSolde);
            
            try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
                workbook.write(outputStream);
            }
            Util.getDownloadFile(fileName, "grand_livre");
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void printGrandLivre() {
        Map<String, Object> param = new HashMap<>();
        param.put("COMPTES", grandLivre.getComptes());
        param.put("DATE_DEBUT", grandLivre.getDateDebut());
        param.put("DATE_FIN", grandLivre.getDateFin());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", (int) grandLivre.getAgence());
        param.put("JOURNAL", (int) grandLivre.getJournal());
        param.put("TYPE", grandLivre.getNature());
        param.put("LETTRER", grandLivre.getLettrer());
        param.put("CUMULE", grandLivre.isCumule());
        param.put("NATURE", grandLivre.getOrdres());
        param.put("IMG_LOGO", returnLogo());
        param.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + Initialisation.FILE_SEPARATOR);
        String report = "grand_livre";
        if ("A".equals(grandLivre.getNature())) {
            report = "grand_livre_analytique";
        } else if ("T".equals(grandLivre.getNature())) {
            param.put("NATURE", grandLivre.getOrdres());
            report = "grand_livre_tiers";
        }
        executeReport(report, param);
    }
    
    public void loadDataGrandLivre() {
        try {
            String query = "SELECT COUNT(y.id) FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_content_journal y ON p.id = y.piece INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id";
            grandLivre.loadGrandLivre(dao);
        } catch (Exception ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void printJournalXLS() {
        try {
            if (journal != null ? journal.getComptables().isEmpty() : true) {
                getErrorMessage("Vous devez charger le tableau");
                return;
            }
            String fileName = Initialisation.getCheminAllDoc() + Initialisation.FILE_SEPARATOR + "journal.xlsx";
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Journal");
            List<String> titles = new ArrayList<>();
            titles.add("COMPTE");
            titles.add("NUMERO");
            titles.add("DATE PIECE");
            titles.add("TIERS");
            if (journal.getNature().equals("A")) {
                titles.add("CENTRE");
            }
            titles.add("LIBELLE");
            titles.add("DEBIT");
            titles.add("CREDIT");
            
            int numRow = 0;
            Font font = sheet.getWorkbook().createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            cellStyle.setFont(font);
            cellStyle.setWrapText(true);
            
            CellStyle cellTotal = sheet.getWorkbook().createCellStyle();
            cellTotal.setAlignment(CellStyle.ALIGN_RIGHT);
            cellTotal.setFont(font);
            cellTotal.setWrapText(true);
            
            CellStyle cellValeur = sheet.getWorkbook().createCellStyle();
            cellValeur.setAlignment(CellStyle.ALIGN_RIGHT);
            cellValeur.setWrapText(true);
            
            Row row = sheet.createRow(numRow++);
            for (int i = 0; i < titles.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(titles.get(i));
                cell.setCellStyle(cellStyle);
            }
            
            for (ValeurComptable jrn : journal.getComptables()) {
                row = sheet.createRow(numRow++);
                int numCell = 0;
                // Numero
                Cell cell = row.createCell(numCell++);
                cell.setCellValue("[" + jrn.getCode() + "] " + jrn.getIntitule());
                for (ValeurComptable sous : jrn.getSous()) {
                    row = sheet.createRow(numRow++);
                    numCell = 0;
                    //Compte
                    cell = row.createCell(numCell++);
                    cell.setCellValue(sous.getCompte());
                    //Numero
                    cell = row.createCell(numCell++);
                    cell.setCellValue(sous.getNumero());
                    //Date Piece
                    cell = row.createCell(numCell++);
                    cell.setCellValue(formatDate.format(sous.getDate()));
                    //Tiers
                    cell = row.createCell(numCell++);
                    cell.setCellValue(sous.getNomPrenom());
                    if (journal.getNature().equals("A")) {
                        //Centre
                        cell = row.createCell(numCell++);
                        cell.setCellValue(sous.getCodePlan());
                    }
                    //Libelle
                    cell = row.createCell(numCell++);
                    cell.setCellValue(sous.getDescription());
                    //Débit
                    cell = row.createCell(numCell++);
                    cell.setCellValue(DN(sous.getDebit()));
                    cell.setCellStyle(cellValeur);
                    //Crédit
                    cell = row.createCell(numCell++);
                    cell.setCellValue(DN(sous.getCredit()));
                    cell.setCellStyle(cellValeur);
                }
                numCell = 0;
                row = sheet.createRow(numRow++);
                //Compte
                cell = row.createCell(numCell++);
                cell.setCellValue("");
                //Numero
                cell = row.createCell(numCell++);
                cell.setCellValue("");
                //Date Piece
                cell = row.createCell(numCell++);
                cell.setCellValue("");
                //Tiers
                cell = row.createCell(numCell++);
                cell.setCellValue("");
                if (journal.getNature().equals("A")) {
                    //Centre
                    cell = row.createCell(numCell++);
                    cell.setCellValue("");
                }
                //Libelle
                cell = row.createCell(numCell++);
                cell.setCellValue("");
                //Débit
                cell = row.createCell(numCell++);
                cell.setCellValue(DN(journal.summaryComptaResume(jrn, jrn.getSous(), "debit")));
                cell.setCellStyle(cellTotal);
                //Crédit
                cell = row.createCell(numCell++);
                cell.setCellValue(DN(journal.summaryComptaResume(jrn, jrn.getSous(), "credit")));
                cell.setCellStyle(cellTotal);
            }
            int numCell = 0;
            row = sheet.createRow(numRow++);
            //Compte
            Cell cell = row.createCell(numCell++);
            cell.setCellValue("");
            //Numero
            cell = row.createCell(numCell++);
            cell.setCellValue("");
            //Date Piece
            cell = row.createCell(numCell++);
            cell.setCellValue("");
            //Tiers
            cell = row.createCell(numCell++);
            cell.setCellValue("");
            if (journal.getNature().equals("A")) {
                //Centre
                cell = row.createCell(numCell++);
                cell.setCellValue("");
            }
            //Libelle
            cell = row.createCell(numCell++);
            cell.setCellValue("");
            //Débit
            cell = row.createCell(numCell++);
            cell.setCellValue(DN(journal.summaryComptaResume("debit")));
            cell.setCellStyle(cellTotal);
            //Crédit
            cell = row.createCell(numCell++);
            cell.setCellValue(DN(journal.summaryComptaResume("credit")));
            cell.setCellStyle(cellTotal);
            
            try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
                workbook.write(outputStream);
            }
            Util.getDownloadFile(fileName, "journal");
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void printJournal() {
        Map<String, Object> param = new HashMap<>();
        param.put("JOURNAL", (int) journal.getJournal());
        param.put("DATE_DEBUT", journal.getDateDebut());
        param.put("DATE_FIN", journal.getDateFin());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        if (journal.getNature().equals("A")) {
            executeReport("etat_journal_analytique", param);
        } else {
            executeReport("etat_journal", param);
        }
    }
    
    private int action(String reference) {
        if (Util.asString(reference)) {
            reference = reference.trim();
            char first = reference.charAt(0);
            char last = reference.charAt(reference.length() - 1);
            if (first == '%' && last == '%') {
                return 3; // LIKE %VALUE% (CONTAINS)
            }
            if (first == '%') {
                return 2; // LIKE %VALUE (ENDWITH)
            }
            return 1; // LIKE VALUE% (STARTWITH)
        }
        return 0;
    }
    
    public void loadDataJournal() {
        if (journal.getJournal() < 1) {
            getErrorMessage("Vous devez preciser un journal");
            return;
        }
        journal.setComptes(journal.getJournal() + "");
        journal.loadJournal(dao);
    }
    
    public void findTiers() {
        findTiers(tiersSearch);
    }
    
    public void findTiers(String tiersSearch) {
        tiers.clear();
        ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (w != null) {
            int action = action(tiersSearch);
            String reference = action > 0 ? tiersSearch.replace("%", "") : "";
            switch (action) {
                case 1:// LIKE VALUE% (STARTWITH)
                    for (YvsBaseTiers t : w.getTiers_all()) {
                        if (t.getCodeTiers().toUpperCase().startsWith(reference.toUpperCase()) || t.getNom_prenom().toUpperCase().startsWith(reference.toUpperCase())) {
                            tiers.add(t);
                        }
                    }
                    break;
                case 2:// LIKE VALUE% (STARTWITH)
                    for (YvsBaseTiers t : w.getTiers_all()) {
                        if (t.getCodeTiers().toUpperCase().endsWith(reference.toUpperCase()) || t.getNom_prenom().toUpperCase().endsWith(reference.toUpperCase())) {
                            tiers.add(t);
                        }
                    }
                    break;
                case 3:// LIKE %VALUE% (CONTAINS)
                    for (YvsBaseTiers t : w.getTiers_all()) {
                        if (t.getCodeTiers().toUpperCase().contains(reference.toUpperCase()) || t.getNom_prenom().toUpperCase().contains(reference.toUpperCase())) {
                            tiers.add(t);
                        }
                    }
                    break;
                default:
                    tiers.addAll(w.getTiers_all());
                    break;
            }
        }
    }
    
    public void findCompte() {
        findCompte(compteSearch);
    }
    
    public void findCompte(String compteSearch) {
        comptes.clear();
        ManagedCompte w = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (w != null) {
            int action = action(compteSearch);
            String reference = action > 0 ? compteSearch.replace("%", "") : "";
            switch (action) {
                case 1:// LIKE VALUE% (STARTWITH)
                    for (YvsBasePlanComptable c : w.getComptes_all()) {
                        if (c.getNumCompte().toUpperCase().startsWith(reference.toUpperCase()) || c.getIntitule().toUpperCase().startsWith(reference.toUpperCase())) {
                            comptes.add(c);
                        }
                    }
                    break;
                case 2:// LIKE VALUE% (STARTWITH)
                    for (YvsBasePlanComptable c : w.getComptes_all()) {
                        if (c.getNumCompte().toUpperCase().endsWith(reference.toUpperCase()) || c.getIntitule().toUpperCase().endsWith(reference.toUpperCase())) {
                            comptes.add(c);
                        }
                    }
                    break;
                case 3:// LIKE %VALUE% (CONTAINS)
                    for (YvsBasePlanComptable c : w.getComptes_all()) {
                        if (c.getNumCompte().toUpperCase().contains(reference.toUpperCase()) || c.getIntitule().toUpperCase().contains(reference.toUpperCase())) {
                            comptes.add(c);
                        }
                    }
                    break;
                default:
                    comptes.addAll(w.getComptes_all());
                    break;
            }
        }
    }
    
    public void findCentre() {
        findCentre(centreSearch);
    }
    
    public void findCentre(String centreSearch) {
        centres.clear();
        ManagedCentreAnalytique w = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
        if (w != null) {
            int action = action(centreSearch);
            String reference = action > 0 ? centreSearch.replace("%", "") : "";
            switch (action) {
                case 1:// LIKE VALUE% (STARTWITH)
                    for (YvsComptaCentreAnalytique a : w.getCentres_all()) {
                        if (a.getCodeRef().toUpperCase().startsWith(reference.toUpperCase()) || a.getDesignation().toUpperCase().startsWith(reference.toUpperCase())) {
                            centres.add(a);
                        }
                    }
                    break;
                case 2:// LIKE VALUE% (STARTWITH)
                    for (YvsComptaCentreAnalytique a : w.getCentres_all()) {
                        if (a.getCodeRef().toUpperCase().endsWith(reference.toUpperCase()) || a.getDesignation().toUpperCase().endsWith(reference.toUpperCase())) {
                            centres.add(a);
                        }
                    }
                    break;
                case 3:// LIKE %VALUE% (CONTAINS)
                    for (YvsComptaCentreAnalytique a : w.getCentres_all()) {
                        if (a.getCodeRef().toUpperCase().contains(reference.toUpperCase()) || a.getDesignation().toUpperCase().contains(reference.toUpperCase())) {
                            centres.add(a);
                        }
                    }
                    break;
                default:
                    centres.addAll(w.getCentres_all());
                    break;
            }
        }
    }
    
    public void chooseTiers(String rapport) {
        this.rapport = rapport;
        chooseTiers();
    }
    
    public void chooseTiers() {
        ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (w != null) {
            switch (rapport) {
                case "BALANCE": {
                    tabBalance.setCompteDebut("");
                    tabBalance.setCompteFin("");
                    tabBalance.setComptes(null);
                    if (selectTiers != null ? !selectTiers.isEmpty() : false) {
                        for (YvsBaseTiers t : selectTiers) {
                            String code = w.searchCompteTiers(t, "R", tabBalance.getOrdres());
                            if (!Util.asString(tabBalance.getComptes())) {
                                tabBalance.setComptes(code);
                            } else {
                                tabBalance.setComptes(tabBalance.getComptes() + "," + code);
                            }
                        }
                        tabBalance.setCompteDebut(selectTiers.get(0).getCodeTiers());
                        tabBalance.setCompteFin(selectTiers.get(selectTiers.size() - 1).getCodeTiers());
                    }
                    update("tabRapCompta:blog_compte_rapport_balance");
                    break;
                }
                case "GRAND_LIVRE": {
                    grandLivre.setCompteDebut("");
                    grandLivre.setCompteFin("");
                    grandLivre.setComptes(null);
                    if (selectTiers != null ? !selectTiers.isEmpty() : false) {
                        for (YvsBaseTiers t : selectTiers) {
                            String code = w.searchCompteTiers(t, "R", grandLivre.getOrdres());
                            if (!Util.asString(grandLivre.getComptes())) {
                                grandLivre.setComptes(code);
                            } else {
                                grandLivre.setComptes(grandLivre.getComptes() + "," + code);
                            }
                        }
                        grandLivre.setCompteDebut(selectTiers.get(0).getCodeTiers());
                        grandLivre.setCompteFin(selectTiers.get(selectTiers.size() - 1).getCodeTiers());
                    }
                    update("tabRapCompta:blog_compte_rapport_grand_livre");
                    break;
                }
            }
        }
    }
    
    private void addParamNoIdsCompte(ManagedCompte w) {
        String value = null;
        for (YvsBasePlanComptable c : selectComptes) {
            if (value == null) {
                value = c.getId().toString();
            } else {
                value += ";" + c.getId().toString();
            }
        }
        w.addParamNoIds(value);
    }
    
    public void drollable(DragDropEvent ev) {
        YvsBasePlanComptable y = ((YvsBasePlanComptable) ev.getData());
        ManagedCompte w = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (w != null) {
            w.getListComptes().remove(y);
            selectComptes.add(0, y);
            addParamNoIdsCompte(w);
        }
    }
    
    public void drollableAll() {
        ManagedCompte w = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (w != null) {
            selectComptes.addAll(new ArrayList<>(w.getListComptes()));
            w.getListComptes().clear();
            addParamNoIdsCompte(w);
        }
    }
    
    public void removeDrollable(YvsBasePlanComptable y) {
        ManagedCompte w = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (w != null) {
            w.getListComptes().add(0, y);
            selectComptes.remove(y);
            addParamNoIdsCompte(w);
        }
    }
    
    public void removeAllDrollable() {
        ManagedCompte w = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (w != null) {
            w.getListComptes().addAll(new ArrayList<>(selectComptes));
            selectComptes.clear();
            addParamNoIdsCompte(w);
        }
    }
    
    public void chooseCompte() {
        ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (w != null) {
            switch (rapport) {
                case "BALANCE": {
                    tabBalance.setCompteDebut("");
                    tabBalance.setCompteFin("");
                    tabBalance.setComptes(null);
                    if (selectComptes != null ? !selectComptes.isEmpty() : false) {
                        for (YvsBasePlanComptable y : selectComptes) {
                            String code = y.getNumCompte();
                            if (!Util.asString(tabBalance.getComptes())) {
                                tabBalance.setComptes(code);
                            } else {
                                tabBalance.setComptes(tabBalance.getComptes() + "," + code);
                            }
                        }
                        tabBalance.setCompteDebut(selectComptes.get(0).getNumCompte());
                        tabBalance.setCompteFin(selectComptes.get(selectComptes.size() - 1).getNumCompte());
                    }
                    update("tabRapCompta:blog_compte_rapport_balance");
                    break;
                }
                case "GRAND_LIVRE": {
                    grandLivre.setCompteDebut("");
                    grandLivre.setCompteFin("");
                    grandLivre.setComptes(null);
                    if (selectComptes != null ? !selectComptes.isEmpty() : false) {
                        for (YvsBasePlanComptable y : selectComptes) {
                            String code = y.getNumCompte();
                            if (!Util.asString(grandLivre.getComptes())) {
                                grandLivre.setComptes(code);
                            } else {
                                grandLivre.setComptes(grandLivre.getComptes() + "," + code);
                            }
                        }
                        grandLivre.setCompteDebut(selectComptes.get(0).getNumCompte());
                        grandLivre.setCompteFin(selectComptes.get(selectComptes.size() - 1).getNumCompte());
                    }
                    update("tabRapCompta:blog_compte_rapport_grand_livre");
                    break;
                }
            }
        }
    }
    
    public void chooseCentre() {
        ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (w != null) {
            switch (rapport) {
                case "BALANCE": {
                    tabBalance.setCompteDebut("");
                    tabBalance.setCompteFin("");
                    tabBalance.setComptes(null);
                    if (selectCentres != null ? !selectCentres.isEmpty() : false) {
                        for (YvsComptaCentreAnalytique y : selectCentres) {
                            String code = y.getCodeRef();
                            if (!Util.asString(tabBalance.getComptes())) {
                                tabBalance.setComptes(code);
                            } else {
                                tabBalance.setComptes(tabBalance.getComptes() + "," + code);
                            }
                        }
                        tabBalance.setCompteDebut(selectCentres.get(0).getCodeRef());
                        tabBalance.setCompteFin(selectCentres.get(selectCentres.size() - 1).getCodeRef());
                    }
                    update("tabRapCompta:blog_compte_rapport_balance");
                    break;
                }
                case "GRAND_LIVRE": {
                    grandLivre.setCompteDebut("");
                    grandLivre.setCompteFin("");
                    grandLivre.setComptes(null);
                    if (selectCentres != null ? !selectCentres.isEmpty() : false) {
                        for (YvsComptaCentreAnalytique y : selectCentres) {
                            String code = y.getCodeRef();
                            if (!Util.asString(grandLivre.getComptes())) {
                                grandLivre.setComptes(code);
                            } else {
                                grandLivre.setComptes(grandLivre.getComptes() + "," + code);
                            }
                        }
                        grandLivre.setCompteDebut(selectCentres.get(0).getCodeRef());
                        grandLivre.setCompteFin(selectCentres.get(selectCentres.size() - 1).getCodeRef());
                    }
                    update("tabRapCompta:blog_compte_rapport_grand_livre");
                    break;
                }
            }
        }
    }
    
    public void onChangeValueCompteDebut(String rapport, String nature) {
        this.rapport = rapport;
        switch (rapport) {
            case "BALANCE": {
                tabBalance.setComptes("");
                if (Util.asString(tabBalance.getCompteDebut())) {
                    switch (nature) {
                        case "T": {
                            findTiers(tabBalance.getCompteDebut());
                            ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
                            if (tiers != null ? !tiers.isEmpty() : false) {
                                if (tiers.size() == 1) {
                                    String code = tiers.get(0).getCodeTiers();
                                    tabBalance.setCompteDebut(code);
                                    if (w != null) {
                                        code = w.searchCompteTiers(tiers.get(0), "R", tabBalance.getOrdres());
                                    }
                                    tabBalance.setComptes(code);
                                } else {
                                    openDialog("dlgListTiers");
                                    update("data_tiers_rapport_compta");
                                    return;
                                }
                            } else {
                                getInfoMessage("Entrez un code ou un nom unique");
                                return;
                            }
                            break;
                        }
                        case "A": {
                            findCentre(tabBalance.getCompteDebut());
                            if (centres != null ? !centres.isEmpty() : false) {
                                if (centres.size() == 1) {
                                    tabBalance.setCompteDebut(centres.get(0).getCodeRef());
                                    tabBalance.setComptes(tabBalance.getCompteDebut());
                                } else {
                                    openDialog("dlgListCentre");
                                    update("data_centre_rapport_compta");
                                    return;
                                }
                            } else {
                                getInfoMessage("Entrez un code ou une désignation unique");
                                return;
                            }
                            break;
                        }
                        case "C": {
                            findCompte(tabBalance.getCompteDebut());
                            if (comptes != null ? !comptes.isEmpty() : false) {
                                if (comptes.size() == 1) {
                                    tabBalance.setCompteDebut(comptes.get(0).getNumCompte());
                                    tabBalance.setComptes(tabBalance.getCompteDebut());
                                } else {
                                    openDialog("dlgListCompte");
                                    update("data_compte_rapport_compta");
                                    return;
                                }
                            } else {
                                getInfoMessage("Entrez un numero ou un intitulé unique");
                                return;
                            }
                            break;
                        }
                    }
                    if (Util.asString(tabBalance.getCompteFin())) {
                        selectCompte(rapport, nature);
                    }
                }
                break;
            }
            case "GRAND_LIVRE": {
                grandLivre.setComptes("");
                if (Util.asString(grandLivre.getCompteDebut())) {
                    switch (nature) {
                        case "T": {
                            findTiers(grandLivre.getCompteDebut());
                            ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
                            if (tiers != null ? !tiers.isEmpty() : false) {
                                if (tiers.size() == 1) {
                                    String code = tiers.get(0).getCodeTiers();
                                    grandLivre.setCompteDebut(code);
                                    if (w != null) {
                                        code = w.searchCompteTiers(tiers.get(0), "R", grandLivre.getOrdres());
                                    }
                                    grandLivre.setComptes(code);
                                } else {
                                    openDialog("dlgListTiers");
                                    update("data_tiers_rapport_compta");
                                    return;
                                }
                            } else {
                                getInfoMessage("Entrez un code ou un nom unique");
                                return;
                            }
                            break;
                        }
                        case "A": {
                            findCentre(grandLivre.getCompteDebut());
                            if (centres != null ? !centres.isEmpty() : false) {
                                if (centres.size() == 1) {
                                    grandLivre.setCompteDebut(centres.get(0).getCodeRef());
                                    grandLivre.setComptes(grandLivre.getCompteDebut());
                                } else {
                                    openDialog("dlgListCentre");
                                    update("data_centre_rapport_compta");
                                    return;
                                }
                            } else {
                                getInfoMessage("Entrez un code ou une désignation unique");
                                return;
                            }
                            break;
                        }
                        case "C": {
                            findCompte(grandLivre.getCompteDebut());
                            if (comptes != null ? !comptes.isEmpty() : false) {
                                if (comptes.size() == 1) {
                                    grandLivre.setCompteDebut(comptes.get(0).getNumCompte());
                                    grandLivre.setComptes(grandLivre.getCompteDebut());
                                } else {
                                    openDialog("dlgListCompte");
                                    update("data_compte_rapport_compta");
                                    return;
                                }
                            } else {
                                getInfoMessage("Entrez un numero ou un intitulé unique");
                                return;
                            }
                            break;
                        }
                    }
                    if (Util.asString(grandLivre.getCompteFin())) {
                        selectCompte(rapport, nature);
                    }
                }
                break;
            }
        }
    }
    
    public void onChangeValueCompteFin(String rapport, String nature) {
        this.rapport = rapport;
        switch (rapport) {
            case "BALANCE": {
                tabBalance.setComptes(tabBalance.getCompteDebut());
                if (Util.asString(tabBalance.getCompteFin())) {
                    switch (nature) {
                        case "T": {
                            ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
                            if (w != null) {
                                Tiers bean = w.findTiers(tabBalance.getCompteFin(), false);
                                if (w.getListTiers().size() == 1) {
                                    tabBalance.setCompteFin(bean.getCodeTiers());
                                    if (Util.asString(tabBalance.getCompteDebut())) {
                                        selectCompte(rapport, nature);
                                    } else {
                                        getInfoMessage("Vous devez entrer le code de début");
                                    }
                                } else {
                                    getInfoMessage("Entrez un code ou un nom unique");
                                }
                            }
                            break;
                        }
                        case "A": {
                            ManagedCentreAnalytique w = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
                            if (w != null) {
                                CentreAnalytique bean = w.findCentre(tabBalance.getCompteFin(), false);
                                if (w.getCentres().size() == 1) {
                                    tabBalance.setCompteFin(bean.getCodeRef());
                                    if (Util.asString(tabBalance.getCompteDebut())) {
                                        selectCompte(rapport, nature);
                                    } else {
                                        getInfoMessage("Vous devez entrer le code de début");
                                    }
                                } else {
                                    getInfoMessage("Entrez un code ou une désignation unique");
                                }
                            }
                            break;
                        }
                        case "C": {
                            ManagedCompte w = (ManagedCompte) giveManagedBean(ManagedCompte.class);
                            if (w != null) {
                                Comptes bean = w.findCompte(tabBalance.getCompteFin(), false);
                                if (w.getListComptes().size() == 1) {
                                    tabBalance.setCompteFin(bean.getNumCompte());
                                    if (Util.asString(tabBalance.getCompteDebut())) {
                                        selectCompte(rapport, nature);
                                    } else {
                                        getInfoMessage("Vous devez entrer le code de début");
                                    }
                                } else {
                                    getInfoMessage("Entrez un numero ou un intitulé unique");
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            }
            case "GRAND_LIVRE": {
                grandLivre.setComptes(grandLivre.getCompteDebut());
                if (Util.asString(grandLivre.getCompteFin())) {
                    switch (nature) {
                        case "T": {
                            ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
                            if (w != null) {
                                Tiers bean = w.findTiers(grandLivre.getCompteFin(), false);
                                if (w.getListTiers().size() == 1) {
                                    grandLivre.setCompteFin(bean.getCodeTiers());
                                    if (Util.asString(grandLivre.getCompteDebut())) {
                                        selectCompte(rapport, nature);
                                    } else {
                                        getInfoMessage("Vous devez entrer le code de début");
                                    }
                                } else {
                                    getInfoMessage("Entrez un code ou un nom unique");
                                }
                            }
                            break;
                        }
                        case "A": {
                            ManagedCentreAnalytique w = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
                            if (w != null) {
                                CentreAnalytique bean = w.findCentre(grandLivre.getCompteFin(), false);
                                if (w.getCentres().size() == 1) {
                                    grandLivre.setCompteFin(bean.getCodeRef());
                                    if (Util.asString(grandLivre.getCompteDebut())) {
                                        selectCompte(rapport, nature);
                                    } else {
                                        getInfoMessage("Vous devez entrer le code de début");
                                    }
                                } else {
                                    getInfoMessage("Entrez un code ou une désignation unique");
                                }
                            }
                            break;
                        }
                        case "C": {
                            ManagedCompte w = (ManagedCompte) giveManagedBean(ManagedCompte.class);
                            if (w != null) {
                                Comptes bean = w.findCompte(grandLivre.getCompteFin(), false);
                                if (w.getListComptes().size() == 1) {
                                    grandLivre.setCompteFin(bean.getNumCompte());
                                    if (Util.asString(grandLivre.getCompteDebut())) {
                                        selectCompte(rapport, nature);
                                    } else {
                                        getInfoMessage("Vous devez entrer le code de début");
                                    }
                                } else {
                                    getInfoMessage("Entrez un numero ou un intitulé unique");
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            }
        }
    }
    
    public void selectCompte(String rapport, String nature) {
        switch (rapport) {
            case "BALANCE": {
                if (tabBalance.getCompteFin().compareTo(tabBalance.getCompteDebut()) < 0) {
                    getErrorMessage("Les valeurs de l'interval ne sont pas bien définies");
                    return;
                }
                tabBalance.setComptes(null);
                switch (nature) {
                    case "T": {
                        ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
                        if (w != null) {
                            for (YvsBaseTiers c : w.getTiers_all()) {
                                int compareBegin = c.getCodeTiers().compareTo(tabBalance.getCompteDebut());
                                int compareEnd = c.getCodeTiers().compareTo(tabBalance.getCompteFin());
                                if (compareBegin >= 0 && compareEnd <= 0) {
                                    String code = w.searchCompteTiers(c, "R", tabBalance.getOrdres());
                                    if (!Util.asString(tabBalance.getComptes())) {
                                        tabBalance.setComptes(code);
                                    } else {
                                        tabBalance.setComptes(tabBalance.getComptes() + "," + code);
                                    }
                                }
                            }
                        }
                        break;
                    }
                    case "A": {
                        ManagedCentreAnalytique w = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
                        if (w != null) {
                            for (YvsComptaCentreAnalytique c : w.getCentres_all()) {
                                int compareBegin = c.getCodeRef().compareTo(tabBalance.getCompteDebut());
                                int compareEnd = c.getCodeRef().compareTo(tabBalance.getCompteFin());
                                if (compareBegin >= 0 && compareEnd <= 0) {
                                    if (!Util.asString(tabBalance.getComptes())) {
                                        tabBalance.setComptes(c.getCodeRef());
                                    } else {
                                        tabBalance.setComptes(tabBalance.getComptes() + "," + c.getCodeRef());
                                    }
                                }
                            }
                        }
                        break;
                    }
                    case "C": {
                        ManagedCompte w = (ManagedCompte) giveManagedBean(ManagedCompte.class);
                        if (w != null) {
                            for (YvsBasePlanComptable c : w.getComptes_all()) {
                                int compareBegin = c.getNumCompte().compareTo(tabBalance.getCompteDebut());
                                int compareEnd = c.getNumCompte().compareTo(tabBalance.getCompteFin());
                                if (compareBegin >= 0 && compareEnd <= 0) {
                                    if (!Util.asString(tabBalance.getComptes())) {
                                        tabBalance.setComptes(c.getNumCompte());
                                    } else {
                                        tabBalance.setComptes(tabBalance.getComptes() + "," + c.getNumCompte());
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case "GRAND_LIVRE": {
                if (grandLivre.getCompteFin().compareTo(grandLivre.getCompteDebut()) < 0) {
                    getErrorMessage("Les valeurs de l'interval ne sont pas bien définies");
                    return;
                }
                grandLivre.setComptes(null);
                switch (nature) {
                    case "T": {
                        ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
                        if (w != null) {
                            for (YvsBaseTiers c : w.getTiers_all()) {
                                int compareBegin = c.getCodeTiers().compareTo(grandLivre.getCompteDebut());
                                int compareEnd = c.getCodeTiers().compareTo(grandLivre.getCompteFin());
                                if (compareBegin >= 0 && compareEnd <= 0) {
                                    String code = w.searchCompteTiers(c, "R", grandLivre.getOrdres());
                                    if (!Util.asString(grandLivre.getComptes())) {
                                        grandLivre.setComptes(code);
                                    } else {
                                        grandLivre.setComptes(grandLivre.getComptes() + "," + code);
                                    }
                                }
                            }
                        }
                        break;
                    }
                    case "A": {
                        ManagedCentreAnalytique w = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
                        if (w != null) {
                            for (YvsComptaCentreAnalytique c : w.getCentres_all()) {
                                int compareBegin = c.getCodeRef().compareTo(grandLivre.getCompteDebut());
                                int compareEnd = c.getCodeRef().compareTo(grandLivre.getCompteFin());
                                if (compareBegin >= 0 && compareEnd <= 0) {
                                    if (!Util.asString(grandLivre.getComptes())) {
                                        grandLivre.setComptes(c.getCodeRef());
                                    } else {
                                        grandLivre.setComptes(grandLivre.getComptes() + "," + c.getCodeRef());
                                    }
                                }
                            }
                        }
                        break;
                    }
                    case "C": {
                        ManagedCompte w = (ManagedCompte) giveManagedBean(ManagedCompte.class);
                        if (w != null) {
                            for (YvsBasePlanComptable c : w.getComptes_all()) {
                                int compareBegin = c.getNumCompte().compareTo(grandLivre.getCompteDebut());
                                int compareEnd = c.getNumCompte().compareTo(grandLivre.getCompteFin());
                                if (compareBegin >= 0 && compareEnd <= 0) {
                                    if (!Util.asString(grandLivre.getComptes())) {
                                        grandLivre.setComptes(c.getNumCompte());
                                    } else {
                                        grandLivre.setComptes(grandLivre.getComptes() + "," + c.getNumCompte());
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
    }
    
    public void onChangeValueCompte(String value, String compte) {
        if (Util.asString(value)) {
            switch (nature) {
                case "T": {
                    ManagedTiers w = (ManagedTiers) giveManagedBean(ManagedTiers.class);
                    if (w != null) {
                        Tiers bean = w.findTiers(value, false);
                        if (w.getListTiers().size() == 1) {
                            if (compte.equals("BEGIN")) {
                                compteDebut = bean.getCodeTiers();
                                titleDebut = bean.getNom_prenom();
                            } else {
                                compteFin = bean.getCodeTiers();
                                titleFin = bean.getNom_prenom();
                            }
                        } else {
                            getInfoMessage("Entrez un code ou un nom unique");
                            return;
                        }
                    }
                    break;
                }
                case "A": {
                    ManagedCentreAnalytique w = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
                    if (w != null) {
                        CentreAnalytique bean = w.findCentre(value, false);
                        if (w.getCentres().size() == 1) {
                            if (compte.equals("BEGIN")) {
                                compteDebut = bean.getCodeRef();
                                titleDebut = bean.getIntitule();
                            } else {
                                compteFin = bean.getCodeRef();
                                titleFin = bean.getIntitule();
                            }
                        } else {
                            getInfoMessage("Entrez un code ou une désignation unique");
                            return;
                        }
                    }
                    break;
                }
                case "C": {
                    ManagedCompte w = (ManagedCompte) giveManagedBean(ManagedCompte.class);
                    if (w != null) {
                        Comptes bean = w.findCompte(value, false);
                        if (w.getListComptes().size() == 1) {
                            if (compte.equals("BEGIN")) {
                                compteDebut = bean.getNumCompte();
                                titleDebut = bean.getIntitule();
                            } else {
                                compteFin = bean.getNumCompte();
                                titleFin = bean.getIntitule();
                            }
                        } else {
                            getInfoMessage("Entrez un numero ou un intitulé unique");
                            return;
                        }
                    }
                    break;
                }
            }
        }
        if (Util.asString(compteDebut) && Util.asString(compteFin)) {
            switch (rapport) {
                case "BALANCE": {
                    tabBalance.setCompteDebut(compteDebut);
                    tabBalance.setCompteFin(compteFin);
                    tabBalance.setComptes(tabBalance.getCompteDebut());
                    break;
                }
                case "GRAND_LIVRE": {
                    grandLivre.setCompteDebut(compteDebut);
                    grandLivre.setCompteFin(compteFin);
                    grandLivre.setComptes(grandLivre.getCompteDebut());
                    break;
                }
            }
            selectCompte(rapport, nature);
        } else {
            if (!Util.asString(compteFin)) {
                getInfoMessage("Vous devez entrer le compte de fin");
            } else if (!Util.asString(compteDebut)) {
                getInfoMessage("Vous devez entrer le compte de début");
            }
        }
        
    }
    
    public void chooseMode(Dashboards tab, String rapport) {
        setRapport(rapport);
        setNature(tab.getNature());
        if (tab.getValorise_by().equals("I")) {
            openDialog("dlgIntervalCompte");
        } else {
            if (getNature().equals("C")) {
                openDialog("dlgListCompte");
            } else if (getNature().equals("T")) {
                openDialog("dlgListTiers");
            } else if (getNature().equals("A")) {
                openDialog("dlgListCentre");
            }
        }
    }
}
