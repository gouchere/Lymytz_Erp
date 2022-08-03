/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.credit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.dao.Options;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.mutuelle.YvsMutDefaultUseFor;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.mutuelle.YvsMutParametre;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.mutuelle.base.YvsMutGrilleTauxTypeCredit;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.mutuelle.base.YvsMutPosteEmploye;
import yvs.entity.mutuelle.credit.YvsMutTypeCredit;
import yvs.entity.mutuelle.credit.YvsMutAvaliseCredit;
import yvs.entity.mutuelle.credit.YvsMutConditionCredit;
import yvs.entity.mutuelle.credit.YvsMutCredit;
import yvs.entity.mutuelle.credit.YvsMutReglementCredit;
import yvs.entity.mutuelle.credit.YvsMutVoteValidationCredit;
import yvs.entity.mutuelle.echellonage.YvsMutEchellonage;
import yvs.entity.mutuelle.echellonage.YvsMutMensualite;
import yvs.entity.mutuelle.echellonage.YvsMutReglementMensualite;
import yvs.entity.mutuelle.operation.YvsMutOperationCompte;
//import yvs.entity.mutuelle.salaire.YvsMutAvanceSalaire;
//import yvs.entity.mutuelle.salaire.YvsMutPaiementSalaire;
import yvs.grh.bean.Employe;
import yvs.mutuelle.CaisseMutuelle;
import yvs.mutuelle.Compte;
import yvs.mutuelle.Mutualiste;
import yvs.mutuelle.Parametre;
import yvs.mutuelle.UtilMut;
import yvs.mutuelle.base.GrilleTaux;
import yvs.mutuelle.echellonage.Echellonage;
import yvs.mutuelle.echellonage.Mensualite;
import yvs.mutuelle.echellonage.Reechellonage;
import yvs.mutuelle.echellonage.ReglementMensualite;
import yvs.mutuelle.Condition;
import yvs.mutuelle.ManagedMutualiste;
import yvs.mutuelle.echellonage.ManagedEchellonage;
import yvs.mutuelle.operation.ManagedEpargne;
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
public class ManagedCredit extends Managed<Credit, YvsMutCredit> implements Serializable {

//    @ManagedProperty(value = "#{credit}")
    private Credit credit = new Credit();
    private YvsMutCredit entityFusion = new YvsMutCredit();
    private YvsMutCredit creditSelect;
    private List<YvsMutCredit> credits;
    private List<YvsMutCredit> creditsCompte;
    private String commentaire;
    private Avalise avalise = new Avalise();

    private Mutualiste mutualiste = new Mutualiste();
    private List<YvsMutMutualiste> mutualistesAvalise;

    private Reechellonage report = new Reechellonage();
//    private Echellonage echeancier = new Echellonage();
    private YvsMutEchellonage echeancierSelect;

    private Mensualite mensualite = new Mensualite();
    private YvsMutMensualite mensualiteSelect;

    private ReglementCreditMut reglementc = new ReglementCreditMut();
    private ReglementMensualite reglement = new ReglementMensualite();
    private List<YvsMutCaisse> caisses;

    private String etatCredit;
    private Credit simulerCredit = new Credit();
    private Echellonage simulerEcheance = new Echellonage();

    private String numSearch, statutSearch;
    private boolean dateSearch;
    private Date debutSearch = new Date(), finSearch = new Date();
    private long typeSearch;

    private String tabIds, input_reset, tabIds_avalise, input_reset_avalise;
    private boolean updateAvalise;

    //Boolean pour gerer l'etat
//    private boolean soumis, refuser, accorder, annuler, relancer, payer;
    private long caisse;
    private double amortissementA, interetA, forcage, interetRestant, gardeReste, gardeCapital;
    private String chaineSelectionMensualite;

    private List<YvsMutMensualite> lisAnticipation, listeMensualiteAPayer;

    public ManagedCredit() {
        credits = new ArrayList<>();
        caisses = new ArrayList<>();
        creditsCompte = new ArrayList<>();
        mutualistesAvalise = new ArrayList<>();
        lisAnticipation = new ArrayList<>();
        listeMensualiteAPayer = new ArrayList<>();
        reglementc.setCompte(credit.getCompte());
    }

    public long getCaisse() {
        return caisse;
    }

    public void setCaisse(long caisse) {
        this.caisse = caisse;
    }

    public double getGardeCapital() {
        return gardeCapital;
    }

    public void setGardeCapital(double gardeCapital) {
        this.gardeCapital = gardeCapital;
    }

    public YvsMutCredit getEntityFusion() {
        return entityFusion;
    }

    public void setEntityFusion(YvsMutCredit entityFusion) {
        this.entityFusion = entityFusion;
    }

    public Echellonage getSimulerEcheance() {
        return simulerEcheance;
    }

    public void setSimulerEcheance(Echellonage simulerEcheance) {
        this.simulerEcheance = simulerEcheance;
    }

    public Credit getSimulerCredit() {
        return simulerCredit;
    }

    public void setSimulerCredit(Credit simulerCredit) {
        this.simulerCredit = simulerCredit;
    }

    public ReglementCreditMut getReglementc() {
        return reglementc;
    }

    public void setReglementc(ReglementCreditMut reglementc) {
        this.reglementc = reglementc;
    }

    public List<YvsMutCaisse> getCaisses() {
        return caisses;
    }

    public void setCaisses(List<YvsMutCaisse> caisses) {
        this.caisses = caisses;
    }

    public double getGardeReste() {
        return gardeReste;
    }

    public void setGardeReste(double gardeReste) {
        this.gardeReste = gardeReste;
    }

    public Mensualite getMensualite() {
        return mensualite;
    }

    public void setMensualite(Mensualite mensualite) {
        this.mensualite = mensualite;
    }

    public Condition getActiveCondition() {
        return activeCondition;
    }

    public void setActiveCondition(Condition activeCondition) {
        this.activeCondition = activeCondition;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public String getStatutSearch() {
        return statutSearch;
    }

    public void setStatutSearch(String statutSearch) {
        this.statutSearch = statutSearch;
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

    public long getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(long typeSearch) {
        this.typeSearch = typeSearch;
    }

//    public Date getDateMinEcheance() {
//        return dateMinEcheance;
//    }
//
//    public void setDateMinEcheance(Date dateMinEcheance) {
//        this.dateMinEcheance = dateMinEcheance;
//    }
    public double getCapitalRestant() {
        return capitalRestant;
    }

    public void setCapitalRestant(double capitalRestant) {
        this.capitalRestant = capitalRestant;
    }

    public double getInteretPeriode() {
        return interetPeriode;
    }

    public void setInteretPeriode(double interetPeriode) {
        this.interetPeriode = interetPeriode;
    }

    public double getAmortissementPeriode() {
        return amortissementPeriode;
    }

    public void setAmortissementPeriode(double amortissementPeriode) {
        this.amortissementPeriode = amortissementPeriode;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public List<YvsMutMensualite> getListeMensualiteAPayer() {
        return listeMensualiteAPayer;
    }

    public void setListeMensualiteAPayer(List<YvsMutMensualite> listeMensualiteAPayer) {
        this.listeMensualiteAPayer = listeMensualiteAPayer;
    }

    public List<YvsMutMensualite> getLisAnticipation() {
        return lisAnticipation;
    }

    public void setLisAnticipation(List<YvsMutMensualite> lisAnticipation) {
        this.lisAnticipation = lisAnticipation;
    }

    public double getAmortissementA() {
        return amortissementA;
    }

    public void setAmortissementA(double amortissementA) {
        this.amortissementA = amortissementA;
    }

    public double getInteretRestant() {
        return interetRestant;
    }

    public void setInteretRestant(double interetRestant) {
        this.interetRestant = interetRestant;
    }

    public double getInteretA() {
        return interetA;
    }

    public void setInteretA(double interetA) {
        this.interetA = interetA;
    }

    public double getForcage() {
        return forcage;
    }

    public void setForcage(double forcage) {
        this.forcage = forcage;
    }

    public void setEtatCredit(String etat) {
        this.etatCredit = etat;
    }

    public String getEtatCredit() {
        return etatCredit;
    }

    public List<YvsMutMutualiste> getMutualistesAvalise() {
        return mutualistesAvalise;
    }

    public void setMutualistesAvalise(List<YvsMutMutualiste> mutualistesAvalise) {
        this.mutualistesAvalise = mutualistesAvalise;
    }

    public YvsMutEchellonage getEcheancierSelect() {
        return echeancierSelect;
    }

    public void setEcheancierSelect(YvsMutEchellonage echeancierSelect) {
        this.echeancierSelect = echeancierSelect;
    }

    public Reechellonage getReport() {
        return report;
    }

    public void setReport(Reechellonage report) {
        this.report = report;
    }

    public YvsMutMensualite getMensualiteSelect() {
        return mensualiteSelect;
    }

    public void setMensualiteSelect(YvsMutMensualite mensualiteSelect) {
        this.mensualiteSelect = mensualiteSelect;
    }

    public ReglementMensualite getReglement() {
        return reglement;
    }

    public void setReglement(ReglementMensualite reglement) {
        this.reglement = reglement;
    }

//    public Echellonage getEcheancier() {
//        return echeancier;
//    }
//
//    public void setEcheancier(Echellonage echeancier) {
//        this.echeancier = echeancier;
//    }
    public Avalise getAvalise() {
        return avalise;
    }

    public void setAvalise(Avalise avalise) {
        this.avalise = avalise;
    }

    public String getTabIds_avalise() {
        return tabIds_avalise;
    }

    public List<YvsMutCredit> getCreditsCompte() {
        return creditsCompte;
    }

    public void setCreditsCompte(List<YvsMutCredit> creditsCompte) {
        this.creditsCompte = creditsCompte;
    }

    public void setTabIds_avalise(String tabIds_avalise) {
        this.tabIds_avalise = tabIds_avalise;
    }

    public String getInput_reset_avalise() {
        return input_reset_avalise;
    }

    public void setInput_reset_avalise(String input_reset_avalise) {
        this.input_reset_avalise = input_reset_avalise;
    }

    public boolean isUpdateAvalise() {
        return updateAvalise;
    }

    public void setUpdateAvalise(boolean updateAvalise) {
        this.updateAvalise = updateAvalise;
    }

    public YvsMutCredit getCreditSelect() {
        return creditSelect;
    }

    public void setCreditSelect(YvsMutCredit creditSelect) {
        this.creditSelect = creditSelect;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public List<YvsMutCredit> getCredits() {
        return credits;
    }

    public void setCredits(List<YvsMutCredit> credits) {
        this.credits = credits;
    }

    public Mutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(Mutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getInput_reset() {
        return input_reset;
    }

    public void setInput_reset(String input_reset) {
        this.input_reset = input_reset;
    }

    public String getChaineSelectionMensualite() {
        return chaineSelectionMensualite;
    }

    public void setChaineSelectionMensualite(String chaineSelectionMensualite) {
        this.chaineSelectionMensualite = chaineSelectionMensualite;
    }

    public double getResteCapital(boolean simuler, double montant, boolean first) {
        if (first) {
            gardeCapital = simuler ? simulerCredit.getMontant() : credit.getMontant();
        }
        if (gardeCapital > montant) {
            gardeCapital -= montant;
            return gardeCapital;
        }
        return 0;
    }

    public double getReste(boolean simuler, double montant, double interet, boolean first) {
        if (first) {
            gardeReste = simuler ? simulerEcheance.getMontant() : credit.getEcheancier().getMontantReste();
        }
        montant = montant - (credit.getEcheancier().isCreditRetainsInteret() ? interet : 0);
        if (gardeReste > montant) {
            gardeReste -= montant;
            return gardeReste;
        }
        return 0;
    }

    @Override
    public void loadAll() {
        loadAllCredit(true, true);
        load(false);
        tabIds = "";
        tabIds_avalise = "";

        input_reset = "";
        input_reset_avalise = "";
    }

    public void load(boolean simuler) {
        if (credit.getEcheancier() == null) {
            credit.setEcheancier(new Echellonage());
        }
        if (simuler) {
            gardeReste = simulerEcheance.getMontant();
            gardeCapital = simulerCredit.getMontant();
        } else {
            gardeReste = credit.getEcheancier().getMontant();
            gardeCapital = credit.getMontant();
        }
        //recharge la mutuelle courante
        if (currentMutuel != null) {
            currentMutuel = (YvsMutMutuelle) dao.loadOneByNameQueries("YvsMutMutuelle.findById", new String[]{"id"}, new Object[]{currentMutuel.getId()});
        }
        if (currentMutuel != null ? currentMutuel.getParamsMutuelle() != null : false) {
            parametreMutuelle = UtilMut.buildBeanParametre(currentMutuel.getParamsMutuelle());
            Long nbVoteRequis = (Long) dao.loadObjectByNameQueries("YvsMutPosteEmploye.findSizeCanVote", new String[]{"mutuelle"}, new Object[]{currentMutuel});
            parametreMutuelle.setNombreVoteRequis((nbVoteRequis != null) ? nbVoteRequis : 0);
            if (parametreMutuelle.isPaiementParCompteStrict()) {
                reglementc.setModePaiement(Constantes.MUT_MODE_PAIEMENT_COMPTE);
            }
            credit.getEcheancier().setCreditRetainsInteret(parametreMutuelle.isCreditRetainsInteret());
            //charge la caisse par défaut de l'epargne
            YvsMutDefaultUseFor df = (YvsMutDefaultUseFor) dao.loadOneByNameQueries("YvsMutDefaultUseFor.findByActiviteM", new String[]{"activite", "mutuelle"}, new Object[]{Constantes.MUT_ACTIVITE_CREDIT, currentMutuel});
            if (df != null) {
                parametreMutuelle.setCaisseCredit(df.getCaisse());
                credit.setCaisse(UtilMut.buildBeanCaisse(df.getCaisse()));
            }
            caisses = currentMutuel.getCaisses();
        } else {
            parametreMutuelle = new Parametre();
        }
        if (reglementc.getReglerPar() != null ? reglementc.getReglerPar().trim().length() < 1 : true) {
            reglementc.setReglerPar(currentUser.getUsers().getNomUsers());
        }
        if (reglement.getReglerPar() != null ? reglement.getReglerPar().trim().length() < 1 : true) {
            reglement.setReglerPar(currentUser.getUsers().getNomUsers());
        }
    }

    public void loadCaisseByTypeOp(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            String typeOp = (String) ev.getNewValue();
            if (Constantes.MUT_MODE_PAIEMENT_ESPECE.equals(typeOp)) {
                caisses = dao.loadNameQueries("YvsMutCaisse.findByMutuelle", new String[]{"mutuelle"}, new Object[]{currentMutuel});
            } else {
                reglementc.setCompte(credit.getCompte());
            }
        }
    }

    public void changeCaisse(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                int idx = currentMutuel.getCaisses().indexOf(new YvsMutCaisse(id));
                if (idx >= 0) {
                    parametreMutuelle.setCaisseCredit(currentMutuel.getCaisses().get(idx));
                    credit.setCaisse(UtilMut.buildBeanCaisse(parametreMutuelle.getCaisseCredit()));
                }
            } else {
                credit.getCaisse().setId(-1);
            }
        }
    }

    public void loadAllCredit(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.compte.mutualiste.mutuelle", "mutuelle", currentMutuel, "=", "AND"));
        credits = paginator.executeDynamicQuery("YvsMutCredit", "y.dateCredit DESC", avance, init, (int) imax, dao);
        update("data_credit");
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsMutCredit> re = paginator.parcoursDynamicData("YvsMutCredit", "y", "y.dateCredit DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void paginer(boolean next) {
        loadAllCredit(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAllCredit(true, true);
    }

//    public List<YvsMutAvanceSalaire> loadAllAvancetMutualiste(Mutualiste mut, Date dateDebut) {
//        String[] ch = new String[]{"mutualiste", "dateAvance"};
//        Object[] v = new Object[]{new YvsMutMutualiste(mut.getId()), dateDebut};
//        List<YvsMutAvanceSalaire> l = dao.loadNameQueries("YvsMutAvanceSalaire.findAvanceEnCours", ch, v);
//        return l;
//    }
    public void loadMutualisteAvalise(YvsMutMutualiste bean) {
        mutualistesAvalise.clear();
    }

    public YvsMutCompte loadOneCompte(long id) {
        return (YvsMutCompte) dao.loadOneByNameQueries("YvsMutCompte.findById", new String[]{"id"}, new Object[]{id});
    }

    public void voirEchenacier(YvsMutCredit bean) {
        creditSelect = bean;
        voirEchenacier();
    }

    public void voirEchenacier() {
        if ((creditSelect != null) ? (creditSelect.getId() != 0 ? creditSelect.getRemboursements() != null : false) : false) {
            if (creditSelect.getEcheancier() != null ? creditSelect.getEcheancier().getId() > 0 : false) {
                if (creditSelect.getEcheancier().getEtat().equals(Constantes.ETAT_REGLE)) {
                    getErrorMessage("L'écheance en cours est déjà remboursé");
                }
                populateView(UtilMut.buildBeanCredit(creditSelect));
                resetFicheEcheancier();
//                echeancier = UtilMut.buildBeanEchellonage(creditSelect.getEcheancier());
                if (credit.getCompte().getId() == 0) {
                    Compte c = UtilMut.buildBeanCompte(creditSelect.getEcheancier().getCredit().getCompte());
                    credit.setCompte(c);
                }
                credit.setUpdate(creditSelect.getEcheancier().isUpdate());
                echeancierSelect = creditSelect.getEcheancier();
                update("date_mensualite_echeancier");
            } else {
                getErrorMessage("Ce crédit n'a pas d'écheance en cours");
            }
        }
    }

    public void loadMensualiteImpaye(YvsMutCredit bean) {
        if (!bean.getType().getAnticipationPossible()) {
            getErrorMessage("Ce type de crédit ne permet pas des anticipations");
            return;
        }
        if (!bean.getStatutPaiement().equals(Constantes.STATUT_DOC_PAYER)) {
            getErrorMessage("Vous ne pouvez pas anticiper un crédit qui n'est pas encore payé");
            return;
        }
        creditSelect = bean;
        loadMensualiteImpaye();
    }

    public void loadMensualiteImpaye() {
        if ((creditSelect != null) ? (creditSelect.getId() != 0 ? creditSelect.getRemboursements() != null : false) : false) {
            if (!creditSelect.getEtat().equals(Constantes.ETAT_VALIDE)) {
                getErrorMessage("Ce crédit n'est pas encore validé");
                return;
            }
            if (creditSelect.getStatutCredit().equals(Constantes.STATUT_DOC_PAYER)) {
                getErrorMessage("Ce crédit est déjà remboursé");
                return;
            }
            voirEchenacier();
            if (echeancierSelect != null ? echeancierSelect.getId() > 0 : false) {
                if (echeancierSelect.getEtat().equals(Constantes.ETAT_REGLE)) {
                    getErrorMessage("L'écheance en cours est déjà remboursé");
                    return;
                }
            }
            lisAnticipation.clear();
            cancelEvaluerAnticipation();

            openDialog("dlgAnticipation");
            update("FormMensualiteImpaye");
            update("footerTextAnticipation");
            update("date_mensualite_echeancier");
        }
    }

    public void cancelEvaluerAnticipation() {
        amortissementA = interetA = forcage = interetRestant = 0;
        listeMensualiteAPayer.clear();
        for (YvsMutMensualite m : credit.getEcheancier().getMensualites()) {
            if (m.getMontant() != m.getMontantVerse()) {
                m.setSelectActif(false);
                listeMensualiteAPayer.add(new YvsMutMensualite(m.getId(), m));
            }
        }
        Collections.sort(listeMensualiteAPayer, new YvsMutMensualite());
        update("listMensualiteImpaye");
    }

    public void evaluerAnticipation() {
        if (forcage <= 0) {
            getErrorMessage("Vous devez entrer le montant de l'anticipation");
            return;
        }
        interetA = 0;
        amortissementA = 0;
        if (creditSelect.getType() != null ? creditSelect.getType().getId() > 0 : false) {
            YvsMutTypeCredit typ = creditSelect.getType();
            interetRestant = forcage;
            int i = listeMensualiteAPayer.size() - 1;
            YvsMutMensualite m;
            while (interetRestant > 0 && i > 0) {
                m = listeMensualiteAPayer.get(i--);
                if (m.getEtat().equals(Constantes.ETAT_REGLE) || m.getMontantReste() > interetRestant) {
                    continue;
                }
                double penalite = 0, taux_penalite = typ.getTauxPenaliteAnticipation();
                if (typ.getPenaliteSuspension()) {
                    switch (typ.getNaturePenaliteSuspension()) {
                        case Constantes.NATURE_MONTANT: {
                            penalite = taux_penalite;
                            break;
                        }
                        default: {
                            switch (typ.getBasePenaliteSuspension()) {
                                case Constantes.PENALITE_BASE_INTERET: {
                                    penalite = m.getInteret() * taux_penalite / 100;
                                    break;
                                }
                                default: {
                                    penalite = m.getMontant() * taux_penalite / 100;
                                    break;
                                }
                            }
                        }
                    }
                }
                m.setMontantPenalite(penalite);
                m.setMarquer(true);
                int idx = listeMensualiteAPayer.indexOf(m);
                if (idx > -1) {
                    listeMensualiteAPayer.set(idx, m);
                }
                interetA += penalite;
                amortissementA += m.getMontantReste();
                interetRestant -= m.getMontantReste();
            }
        }
    }

    public void viewAllEcheanciers() {
        resetFicheEcheancier();
        update("blog_echeancier_credit");
    }

    public YvsMutCredit buildCredit(Credit y) {
        YvsMutTypeCredit type = null;
        if (y.getType() != null ? y.getType().getId() > 0 : false) {
            type = (YvsMutTypeCredit) dao.loadOneByNameQueries("YvsMutTypeCredit.findById", new String[]{"id"}, new Object[]{y.getType().getId()});
        }
        return UtilMut.buildCredit(y, type, currentUser);
    }

    @Override
    public Credit recopieView() {
        Credit c = new Credit();
        c.setId(credit.getId());
        c.setDateCredit((credit.getDateCredit() != null) ? credit.getDateCredit() : new Date());
        c.setHeureCredit((credit.getHeureCredit() != null) ? credit.getHeureCredit() : new Date());
        c.setReference(credit.getReference());
        c.setEtatValidation(credit.getEtatValidation());
        c.setMontant(credit.getMontant());
        c.setDateEffet(credit.getDateEffet());
        if ((credit.getCompte() != null) ? credit.getCompte().getId() > 0 : false) {
            if (credit.getCompte().getMutualiste().getEmploye().getPrenom() == null) {
                credit.setCompte(UtilMut.buildBeanCompte(mutualiste.getComptes().get(mutualiste.getComptes().indexOf(new YvsMutCompte(credit.getCompte().getId())))));
            }
        }
        c.setFraisAdditionnel(credit.getFraisAdditionnel());
        c.setDateSoumission(credit.getDateSoumission());
        c.setStatutRemboursement(credit.getStatutRemboursement());
        c.setConditions(credit.getConditions());
        c.setDuree(credit.getDuree());
        c.setCompte(credit.getCompte());
        c.setMontantDispo(credit.getMontantDispo());
        TypeCredit typ = new TypeCredit();
        cloneObject(typ, credit.getType());
        if (typ.getId() != 0) {
            ManagedTypeCredit w = (ManagedTypeCredit) giveManagedBean(ManagedTypeCredit.class);
            if (w != null) {
                typ = UtilMut.buildBeanTypeCredit(w.getTypes().get(w.getTypes().indexOf(new YvsMutTypeCredit(typ.getId()))));
            }
        }
        c.setType(typ);
        return c;
    }

    @Override
    public boolean controleFiche(Credit bean) {
        if ((bean.getCompte() != null) ? bean.getCompte().getId() <= 0 : true) {
            getErrorMessage("Vous devez entrer le compte!");
            return false;
        }
        if ((bean.getType() != null) ? bean.getType().getId() <= 0 : true) {
            getErrorMessage("Vous devez specifier le type!");
            return false;
        }
        if (credit.getEcheancier().getEcartMensualite() <= 0) {
            getWarningMessage("La périodicité de remboursement du crédit n'est pas correcte");
            return false;
        }
        if (bean.getStatutRemboursement() != Constantes.STATUT_DOC_ATTENTE) {
            getErrorMessage("Vous ne pouvez pas modifier ce crédit. Il est en cours de remboursement!");
            return false;
        }
        if (bean.getStatutPaiement() != Constantes.STATUT_DOC_ATTENTE) {
            getErrorMessage("Vous ne pouvez pas modifier ce crédit. Il est en cours de paiement!");
            return false;
        }
        if (!bean.getEtatValidation().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Vous ne pouvez pas modifier ce crédit. Il est déjà en cours de traitement");
            return false;
        }
        String ref = genererReference(Constantes.MUT_ACTIVITE_CREDIT, bean.getDateCredit(), bean.getType().getId(), Constantes.TYPECREDIT);
        if ((ref != null) ? ref.trim().equals("") : true) {
            return false;
        }
        if ((creditSelect != null ? (creditSelect.getId() > 0 ? !creditSelect.getDateCredit().equals(bean.getDateCredit()) : false) : false)
                || (bean.getReference() == null || bean.getReference().trim().length() < 1)) {
            bean.setReference(ref);
        }
        return true;
    }

    @Override
    public void populateView(Credit bean) {
        cloneObject(credit, bean);
        gardeCapital = credit.getMontant();
    }

    @Override
    public void resetFiche() {
        credit = new Credit();
        resetFiche(mutualiste);
        creditsCompte.clear();
        creditSelect = new YvsMutCredit();
        gardeCapital = 0;
        tabIds = "";
        input_reset = "";
        resetFicheEcheancier();
        resetFicheAvalise();
        update("blog_form_credit");
    }

    public void resetFicheOLD() {
        resetFiche(credit);
        credit.setAvalises(new ArrayList<YvsMutAvaliseCredit>());
        credit.setVotes(new ArrayList<YvsMutVoteValidationCredit>());
        credit.setVotesApprouve(new ArrayList<YvsMutVoteValidationCredit>());
        credit.setVotesDeapprouve(new ArrayList<YvsMutVoteValidationCredit>());
        credit.setCompte(new Compte());
        credit.setDateCredit(new Date());
        credit.setHeureCredit(new Date());
        credit.setType(new TypeCredit());
        credit.setUpdate(true);
        credit.setVisibleVote(false);
        credit.setCaisse(new CaisseMutuelle());
        resetFiche(mutualiste);
        mutualiste.setComptes(new ArrayList<YvsMutCompte>());
        creditsCompte.clear();
        credit.getConditions().clear();

        creditSelect = new YvsMutCredit();
        gardeCapital = 0;

        tabIds = "";
        input_reset = "";
        resetFicheEcheancier();
        resetFicheAvalise();
        update("blog_form_credit");
    }

    @Override
    public boolean saveNew() {
        String action = credit.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(credit)) {
                creditSelect = buildCredit(credit);
                if (credit != null ? credit.getId() < 1 : true) {
                    creditSelect.getConditions().clear();
                    creditSelect = (YvsMutCredit) dao.save1(creditSelect);
                    credit.setId(creditSelect.getId());
                    definedIfVoteVisible();
                } else {
                    dao.update(creditSelect);
                }
                //enregiste les condition d'acceptation  
                credit.setConditions(saveCondition(credit));
                creditSelect.setConditions(new ArrayList<>(credit.getConditions()));
                gardeCapital = credit.getMontant();
                if (credit.getEcheanciers() != null ? credit.getEcheanciers().isEmpty() : false) {
                    credit.getEcheancier().setDateEchellonage(credit.getDateEffet());
                    if (!genererEcheance(creditSelect, credit, credit.getMontant(), false, false)) {
                        getWarningMessage("L'Echéancier n'a été généré !");
                    }
                } else {
                    //Modifie au besoin l'échéancier actif
                    YvsMutEchellonage ech = (YvsMutEchellonage) dao.loadOneByNameQueries("YvsMutEchellonage.findCurrentByCredit_", new String[]{"credit", "etat"}, new Object[]{creditSelect, Constantes.ETAT_ATTENTE});
                    if (ech != null) {
                        ech.setAuthor(currentUser);
                        ech.setDateEchellonage(credit.getDateEffet());
                        ech.setMontant(credit.getMontant());
                        ech.setTaux(credit.getType().getTauxMaximal());
                        ech.setDureeEcheance((double) credit.getDuree());
                        ech.setEcartMensualite(credit.getType().getPeriodicite());
                        ech.setDateUpdate(new Date());
                        ech.setCreditRetainsInteret(parametreMutuelle.isCreditRetainsInteret());
                        dao.update(ech);
                        int idx = credit.getEcheanciers().indexOf(ech);
                        if (idx >= 0) {
                            credit.getEcheanciers().set(idx, ech);
                            update("data_echeancier");
                        }
                    }
                }
                ManagedTypeCredit w = (ManagedTypeCredit) giveManagedBean(ManagedTypeCredit.class);
                if (w != null) {
                    credit.setType(UtilMut.buildBeanTypeCredit(w.getTypes().get(w.getTypes().indexOf(new YvsMutTypeCredit(credit.getType().getId())))));
                }

                int idx = credits.indexOf(creditSelect);
                if (idx > -1) {
                    credits.set(idx, creditSelect);
                } else {
                    credits.add(0, creditSelect);
                }
                idx = creditsCompte.indexOf(creditSelect);
                if (idx > -1) {
                    creditsCompte.set(idx, creditSelect);
                } else {
                    creditsCompte.add(0, creditSelect);
                }
                actionOpenOrResetAfter(this);
                succes();
                update("data_credit");
                update("tabview_credit:data_credit_compte");
                update("txt_etat_credit_");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            Logger.getLogger(ManagedCredit.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    private List<YvsMutConditionCredit> saveCondition(Credit cr) {

        List<YvsMutConditionCredit> result = new ArrayList<>();
        if (cr != null ? cr.getId() > 0 : false) {
            for (YvsMutConditionCredit c : cr.getConditions()) {
                c.setCredit(new YvsMutCredit(cr.getId()));
                c.setDateUpdate(new Date());
                c.setAuthor(currentUser);
                if (c.getId() > 0) {
                    dao.update(c);
                } else {
                    c.setId(null);
                    c.setDateSave(new Date());
                    c = (YvsMutConditionCredit) dao.save1(c);
                }
                c.setId(c.getId());
                result.add(c);
            }
        }
        return result;
    }

    private YvsMutConditionCredit buildCondition(Condition c) {
        YvsMutConditionCredit enC = new YvsMutConditionCredit();
        enC.setId(c.getId());
        enC.setCommentaire(c.getCommentaire());
        enC.setCorrect(c.isCorrect());
        enC.setDateModification(new Date());
        enC.setLibelle(c.getLibelle());
        enC.setValeurEntree(c.getValeurEntree());
        enC.setValeurRequise(c.getValeurRequise());
        enC.setUnite(c.getUnite());
        enC.setCode(c.getCode());
        enC.setDateSave(c.getDateSave());
        return enC;
    }

    private Condition buildCondition(YvsMutConditionCredit c) {
        Condition enC = new Condition();
        enC.setCommentaire(c.getCommentaire());
        enC.setCorrect(c.getCorrect());
        enC.setDateModification(new Date());
        enC.setLibelle(c.getLibelle());
        enC.setValeurEntree(c.getValeurEntree());
        enC.setValeurRequise(c.getValeurRequise());
        enC.setId(c.getId());
        enC.setUnite(c.getUnite());
        enC.setCode(c.getCode());
        enC.setDateSave(c.getDateSave());
        return enC;
    }

    private List<Condition> buildCondition(List<YvsMutConditionCredit> l) {
        List<Condition> re = new ArrayList<>();
        for (YvsMutConditionCredit c : l) {
            re.add(buildCondition(c));
        }
        Collections.sort(re, new Condition());
        return re;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? tabIds.length() > 0 : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsMutCredit> list = new ArrayList<>();
                YvsMutCredit bean;
                for (Long ids : l) {
                    if (ids >= 0) {
                        bean = credits.get(ids.intValue());
                        bean.setDateUpdate(new Date());
                        bean.setAuthor(currentUser);
                        list.add(bean);
                        dao.delete(bean);

                    } else {
                        getErrorMessage("Impossible de supprimer !");
                    }
                }
                credits.removeAll(list);
                creditsCompte.removeAll(list);
                tabIds = "";
                resetFiche();
                succes();
                update("data_credit");
                update("tabview_credit:data_credit_compte");

            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            Logger.getLogger(ManagedCredit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBean(YvsMutCredit y) {
        try {
            if (y != null) {
                dao.delete(y);
                credits.remove(y);
                creditsCompte.remove(y);
                if (credit.getId() == y.getId()) {
                    resetFiche();
                }
                succes();
                update("data_credit");
                update("blog_form_mutuelle");
                update("tabview_credit:data_credit_compte");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    public void deleteBeanMensualite(YvsMutMensualite y) {
        deleteBeanMensualite(y, credit.getEcheancier());
    }

    public void deleteBeanMensualite(YvsMutMensualite y, Echellonage echeancier) {
        try {
            if (echeancier != null ? credit.getEcheancier().getIdEch() > 0 : false) {
                if (!credit.getEcheancier().isActif()) {
                    getErrorMessage("Vous ne pouvez pas supprimer cette mensualité... car l'echéance n'est pa celui en cours");
                    return;
                }
                if (credit.getEcheancier().getEtat().equals(Constantes.ETAT_ENCOURS)) {
                    getErrorMessage("Vous ne pouvez pas supprimer cette mensualité... car l'echéance est en cours de reglement");
                    return;
                }
                if (credit.getEcheancier().getEtat().equals(Constantes.ETAT_REGLE)) {
                    getErrorMessage("Vous ne pouvez pas supprimer cette mensualité... car l'echéance est déjà reglé");
                    return;
                }
                if (y != null) {
                    if (y.getEtat().equals(Constantes.ETAT_ENCOURS)) {
                        getErrorMessage("Cette mensualité est en cours de reglement");
                        return;
                    }
                    if (y.getEtat().equals(Constantes.ETAT_REGLE)) {
                        getErrorMessage("Cette mensualité est déjà reglée");
                        return;
                    }
                    if (y.getEtat().equals(Constantes.ETAT_SUSPENDU)) {
                        getErrorMessage("Cette mensualité est suspendue");
                        return;
                    }
                    dao.delete(y);
                    credit.getEcheancier().getMensualites().remove(y);
                    if (mensualite.getId() == y.getId()) {
                        resetFicheMensualite();
                    }
                    succes();
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    public void deleteBeanReglement(YvsMutReglementMensualite y) {
        try {
            if (y != null) {
                dao.delete(y);
                equilibre(y.getMensualite().getEchellonage().getCredit());
                onselectEcheancier((YvsMutEchellonage) dao.loadOneByNameQueries("YvsMutEchellonage.findById", new String[]{"id"}, new Object[]{y.getMensualite().getEchellonage().getCredit().getEcheancier().getId()}));
//                if (y.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
//                    y.getMensualite().setMontantVerse(y.getMensualite().getMontantVerse() - y.getMontant());
//                    int idx = credit.getEcheancier().getMensualites().indexOf(y.getMensualite());
//                    if (idx > -1) {
//                        credit.getEcheancier().getMensualites().get(idx).setMontantVerse(y.getMensualite().getMontantVerse());
//                    }
//                    credit.getEcheancier().setMontantVerse(credit.getEcheancier().getMontantVerse() - y.getMontant());
//                    echeancierSelect.setMontantVerse(echeancierSelect.getMontantVerse() - y.getMontant());
//                }
//                mensualite.getReglements().remove(y);
//                credit.getEcheancier().getReglements().remove(y);
//                if (reglement.getId() == y.getId()) {
//                    resetFicheReglement();
//                }
                succes();
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Mut Error>>> : ", ex);
        }
    }

    public void deleteBeanReglementC(YvsMutReglementCredit y) {
        try {
            if (y != null) {
                dao.delete(y);
                credit.getReglements().remove(y);
                creditSelect.getReglements().remove(y);
                if (y.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                    if (credit.getMontantPaye() > 0) {
                        creditSelect.setStatutPaiement(Constantes.STATUT_DOC_ENCOUR);
                    } else {
                        creditSelect.setStatutPaiement(Constantes.STATUT_DOC_ATTENTE);
                    }
                    dao.update(creditSelect);
                }
                int idx = credits.indexOf(creditSelect);
                if (idx > -1) {
                    credits.set(idx, creditSelect);
                    update("data_credit");
                }
                credit.setMontantVerse(creditSelect.getMontantVerse());
                credit.setStatutPaiement(creditSelect.getStatutPaiement());
                update("blog_statut_credit");
                if (reglementc.getId() == y.getId()) {
                    resetFicheReglementC();
                }
                succes();
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    @Override
    public void updateBean() {
        if ((tabIds != null) ? tabIds.length() > 0 : false) {
            String[] ids = tabIds.split("-");
            if (((ids != null) ? ids.length > 0 : false)) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                creditSelect = credits.get(credits.indexOf(new YvsMutCredit(id)));
                populateView(UtilMut.buildBeanCredit(creditSelect));
                tabIds = "";
                input_reset = "";
                update("blog_form_credit");
            }
        }
    }

    public void updateView(YvsMutCredit y) {
        if (y != null ? y.getId() > 0 : false) {
            int idx = credits.indexOf(y);
            if (idx > -1) {
                credits.set(idx, y);
                update("data_credit");
            }
            if (credit.getId() == y.getId()) {
                onSelectObject(y);
            }
        }
    }

    public void onSelectEcheancier(YvsMutCredit bean) {
        ManagedEchellonage s = (ManagedEchellonage) giveManagedBean(ManagedEchellonage.class);
        if (s != null) {
            s.onSelectObject(bean.getEcheancier());
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Echéancier", "modMutuelle", "smenEcheancier", true);
            }
        }
    }

    public void onSelectSituationCompte(YvsMutCredit y) {
        ManagedEpargne w = (ManagedEpargne) giveManagedBean(ManagedEpargne.class);
        if (w != null) {
            w.onSelectMutualiste(y.getCompte().getMutualiste());
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Situation Compte", "modMutuelle", "smenSituationCompte", true);
            }
        }
    }

    @Override
    public void onSelectObject(YvsMutCredit bean) {
        creditSelect = bean;
        populateView(UtilMut.buildBeanCredit(bean));
        populateViewEcheancier(UtilMut.buildBeanEchellonage(bean.getEcheancier()));
        credit.setMensualite(credit.getEcheancier().getMontant());
        YvsMutCompte y = bean.getCompte();
        if (y != null ? y.getMutualiste() != null : false) {
            YvsMutMutualiste m = y.getMutualiste();
            m.setCouvertureAvalise_(dao);
            cloneObject(mutualiste, UtilMut.buildBeanMutualiste(m));
            cloneObject(credit.getCompte().getMutualiste(), mutualiste);
            mutualiste.setComptes(dao.loadNameQueries("YvsMutCompte.findByMutualiste", new String[]{"mutualiste"}, new Object[]{bean.getCompte().getMutualiste()}));
        }
        credit.loadVotes();
        definedIfVoteVisible();
        if (!credits.contains(creditSelect)) {
            credits.add(creditSelect);
        }
        update("blog_form_credit");
        update("data_credit");

    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutCredit y = (YvsMutCredit) ev.getObject();
            onSelectObject((y));
            tabIds = credits.indexOf(y) + "";
        }
    }

    public void loadOnViewMutualiste(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutMutualiste bean = (YvsMutMutualiste) ev.getObject();
            bean.setCouvertureAvalise_(dao);
            chooseMutualiste(UtilMut.buildBeanMutualiste(bean), false);
        }
    }

    public void loadOnViewSimulerMutualiste(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutMutualiste bean = (YvsMutMutualiste) ev.getObject();
            bean.setCouvertureAvalise_(dao);
            chooseMutualiste(UtilMut.buildBeanMutualiste(bean), true);
        }
    }

    public void loadOnViewMutualisteAvalise(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutMutualiste bean = (YvsMutMutualiste) ev.getObject();
            chooseMutualisteAvalise(UtilMut.buildBeanMutualiste(bean));
        }
    }

    public YvsMutAvaliseCredit buildAvalise(Avalise y) {
        YvsMutAvaliseCredit a = new YvsMutAvaliseCredit();
        if (y != null) {
            a.setId(y.getId());
            a.setMontant(y.getMontant());
            if ((y.getMutualiste() != null) ? y.getMutualiste().getId() != 0 : false) {
                YvsGrhEmployes e = new YvsGrhEmployes(y.getMutualiste().getEmploye().getId(), y.getMutualiste().getEmploye().getCivilite(), y.getMutualiste().getEmploye().getMatricule(), y.getMutualiste().getEmploye().getNom(), y.getMutualiste().getEmploye().getPrenom());
                a.setMutualiste(new YvsMutMutualiste(y.getMutualiste().getId(), e));
            }
            a.setMontantLibere(y.getMontantLibere());
            a.setCredit(creditSelect);
        }
        return a;
    }

    public Avalise recopieViewAvalise() {
        Avalise a = new Avalise();
        a.setId(avalise.getId());
        a.setMontant(avalise.getMontant());
        a.setMutualiste(avalise.getMutualiste());
        a.setMontantLibere(avalise.getMontantLibere());
        return a;
    }

    public boolean controleFicheAvalise(Avalise bean) {
        if ((bean.getMutualiste() != null) ? bean.getMutualiste().getId() == 0 : true) {
            getErrorMessage("Vous devez selectionner le mutualiste");
            return false;
        } else {
            if (bean.getMutualiste().getCurrentCouvertureAvalise() < bean.getMontant()) {
                getErrorMessage("Votre couverture sécuritaire ne vous permet pas d'avaliser ce crédit !");
                return false;
            }
        }
        return true;
    }

    public void populateviewAvalise(Avalise bean) {
        cloneObject(avalise, bean);
        setUpdateAvalise(true);
    }

    public void resetFicheAvalise() {
        resetFiche(avalise);
        avalise.setMutualiste(new Mutualiste());
        tabIds_avalise = "";
        input_reset_avalise = "";
        setUpdateAvalise(false);
    }

    public void saveNewAvalise() {
        if (credit.getId() > 0) {
            if (input_reset_avalise != null && input_reset_avalise.equals("reset")) {
                setUpdateAvalise(false);
                input_reset_avalise = "";
            }
            String action = isUpdateAvalise() ? "Modification" : "Insertion";
            try {
                Avalise bean = recopieViewAvalise();
                if (controleFicheAvalise(bean)) {
                    YvsMutAvaliseCredit entity = buildAvalise(bean);
                    if (!isUpdateAvalise()) {
                        entity = (YvsMutAvaliseCredit) dao.save1(entity);
                        bean.setId(entity.getId());
                        avalise.setId(entity.getId());
                        credit.setMontantDispo(credit.getMontantDispo() + bean.getMontant());
                        credit.getAvalises().add(entity);
                        setConditionCredit();
                    } else {
                        dao.update(entity);
                        YvsMutAvaliseCredit a = credit.getAvalises().get(credit.getAvalises().indexOf(entity));
                        credit.setMontantDispo(credit.getMontantDispo() - a.getMontant() + bean.getMontant());
                        credit.getAvalises().set(credit.getAvalises().indexOf(entity), entity);
                    }
                    setConditionCredit(entity.getCredit(), false);
                    YvsMutConditionCredit c = findCondition(credit.getConditions(), Constantes.MUT_CODE_CONDITION_MONTANT_COUVERTURE);
                    c.setCredit(creditSelect);
                    dao.update(c);
                    c = findCondition(credit.getConditions(), Constantes.MUT_CODE_CONDITION_NBRE_AVALISE);
                    c.setCredit(creditSelect);
                    dao.update(c);

                    succes();
                    resetFicheAvalise();
                    update("data_avalise_ce");
                    update("data_credit_condition");
                    saveCondition(credit);
                }
            } catch (Exception ex) {
                getErrorMessage(action + " Impossible !");
                Logger.getLogger(ManagedCredit.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            getErrorMessage("Vous devez dabord enregsitrer le credit");
        }
    }

    public void deleteBeanAvalise() {
        try {
            if ((tabIds_avalise != null) ? tabIds_avalise.length() > 0 : false) {
                String[] ids = tabIds_avalise.split("-");
                if ((ids != null) ? ids.length > 0 : false) {
                    for (String s : ids) {
                        long id = Integer.valueOf(s);
                        YvsMutAvaliseCredit bean = credit.getAvalises().get(credit.getAvalises().indexOf(new YvsMutAvaliseCredit(id)));
                        dao.delete(new YvsMutAvaliseCredit(bean.getId()));
                        credit.getAvalises().remove(bean);
                        if (avalise.getId() == bean.getId()) {
                            resetFicheAvalise();
                        }
                    }
                    setConditionCredit();

                    YvsMutConditionCredit c = findCondition(credit.getConditions(), Constantes.MUT_CODE_CONDITION_MONTANT_COUVERTURE);
                    c.setCredit(creditSelect);
                    dao.update(c);
                    c = findCondition(credit.getConditions(), Constantes.MUT_CODE_CONDITION_NBRE_AVALISE);
                    c.setCredit(creditSelect);
                    dao.update(c);

                    succes();
                    update("data_avalise");
                    update("blog_form_avalise");
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            Logger.getLogger(ManagedCredit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBeanAvalise(YvsMutAvaliseCredit bean) {
        try {
            if ((bean != null) ? bean.getId() > 0 : false) {
                dao.delete(new YvsMutAvaliseCredit(bean.getId()));
                credit.getAvalises().remove(bean);
                setConditionCredit();

                YvsMutConditionCredit c = findCondition(credit.getConditions(), Constantes.MUT_CODE_CONDITION_MONTANT_COUVERTURE);
                c.setCredit(creditSelect);
                dao.update(c);
                c = findCondition(credit.getConditions(), Constantes.MUT_CODE_CONDITION_NBRE_AVALISE);
                c.setCredit(creditSelect);
                dao.update(c);

                if (avalise.getId() == bean.getId()) {
                    resetFicheAvalise();
                }
                succes();
                update("data_avalise");
                update("blog_form_avalise");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            Logger.getLogger(ManagedCredit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public void updateBeanAvalise() {
//        tabIds_avalise = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tabIds");
//        if ((tabIds_avalise != null) ? tabIds_avalise.length() > 0 : false) {
//            String[] ids = tabIds_avalise.split("-");
//            setUpdateAvalise((ids != null) ? ids.length > 0 : false);
//            if (isUpdateAvalise()) {
//                long id = Integer.valueOf(ids[ids.length - 1]);
//                YvsMutAvaliseCredit bean = credit.getAvalises().get(credit.getAvalises().indexOf(new YvsMutAvaliseCredit(id)));
//                populateviewAvalise(UtilMut.buildBeanAvalise(bean));
//            } else {
//                resetFicheAvalise();
//            }
//        } else {
//            resetFicheAvalise();
//        }
//        update("blog_form_avalise");
//    }
    public void chooseMutualiste(Mutualiste e, boolean simuler) {
        if (e != null) {
            cloneObject(mutualiste, e);
            Mutualiste mu = new Mutualiste();
            cloneObject(mu, e);
            boolean trouv = false;
            for (YvsMutCompte c_ : e.getComptes()) {
                Compte c = UtilMut.buildBeanCompte(c_);
                if (c.getType().getNature().equals(Constantes.MUT_TYPE_COMPTE_COURANT)) {
                    trouv = true;
                    c.setMutualiste(mu);
                    c.getMutualiste().setMontantSalaire(searchSalaire(c_));
                    if (simuler) {
                        cloneObject(simulerCredit.getCompte(), c);
                    } else {
                        cloneObject(credit.getCompte(), c);
                    }
                }
            }
            if (!trouv) {
                resetFiche();
                getErrorMessage("Ce mutualiste n'a pas de compte credit");
                return;
            }
            if (simuler) {
                if ((simulerCredit.getCompte() != null)) {
                    setConditionSimuler();
                }
                simulerCredit.setMontantDispo(e.getMontantTotalEpargne());
                update("txt_mutualiste_simuler_credit");
                update("compte_mutualiste_simuler_credit");
                update("photos_mut_credit");
                update("situation_mut_simuler_credit");
            } else {
                if ((credit.getCompte() != null)) {
                    setConditionCredit();
                }
                credit.setMontantDispo(e.getMontantTotalEpargne());
                update("txt_mutualiste_credit");
                update("compte_mutualiste_credit");
                update("photos_mut_credit");
                update("situation_mut_credit");
                update("photos_mutualiste_");
            }
        }
    }

    public void searchMutualiste(boolean simuler) {
        String num = getMutualiste().getMatricule();
        getMutualiste().setId(0);
        getMutualiste().setError(true);
        getMutualiste().setEmploye(new Employe());
        ManagedMutualiste sevice = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
        if (sevice != null && (num != null) ? !num.isEmpty() : false) {
            Mutualiste y = sevice.searchMutualiste(num, true);
            if (sevice.getMutualistes() != null ? !sevice.getMutualistes().isEmpty() : false) {
                if (sevice.getMutualistes().size() > 1) {
                    update("data_mutualiste_cred");
                } else {
                    chooseMutualiste(y, simuler);
                }
                getMutualiste().setError(false);
            }
        }
    }

    public void searchAvaliste() {
        String num = avalise.getMutualiste().getMatricule();
        avalise.getMutualiste().setId(0);
        avalise.getMutualiste().setError(true);
        avalise.getMutualiste().setEmploye(new Employe());
        ManagedMutualiste m = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
        if (m != null) {
            Mutualiste y = m.searchMutualiste(num, false);
            if (m.getMutualistes() != null ? !m.getMutualistes().isEmpty() : false) {
                if (m.getMutualistes().size() > 1) {
                    openDialog("dlgMutualisteAvalise");
                    update("data_mutualiste_avalise_cre");
                } else {
                    chooseMutualisteAvalise(y);
                }
                avalise.getMutualiste().setError(false);
            }
        }
    }

    public void initMutualistes(boolean avalise) {
        ManagedMutualiste m = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
        if (m != null) {
            if (avalise) {
                m.initMutualistes(getAvalise().getMutualiste());
            } else {
                m.initMutualistes(getMutualiste());
            }
        }
        if (avalise) {
            update("data_mutualiste_avalise_cre");
        } else {
            update("data_mutualiste_cred");
        }
    }

    public void chooseCompte(ValueChangeEvent ev) {
        if (ev != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                int idx = mutualiste.getComptes().indexOf(new YvsMutCompte(id));
                if (idx >= 0) {
                    credit.setCompte(UtilMut.buildBeanCompte(mutualiste.getComptes().get(idx)));
                    credit.setMontantDispo(mutualiste.getMontantTotalEpargne());
                    credit.getCompte().setMutualiste(mutualiste);
                }
            }
        }
    }

    public void chooseMutualisteAvalise(Mutualiste e) {
        if (e != null) {
            e.setCurrentCouvertureAvalise(findMontantCouverture(e));
            cloneObject(avalise.getMutualiste(), e);
            if (!credit.getConditions().isEmpty()) {
                YvsMutConditionCredit c = findCondition(credit.getConditions(), Constantes.MUT_CODE_CONDITION_MONTANT_COUVERTURE);
                double sav = soeAvalise();
                avalise.setMontant(((c.getValeurEntree() - c.getValeurRequise()) > 0) ? c.getValeurEntree() - c.getValeurRequise() - sav : 0);
            }
        }
    }

    private double soeAvalise() {
        double av = 0;
        if (!credit.getAvalises().isEmpty()) {
            for (YvsMutAvaliseCredit a : credit.getAvalises()) {
                av += a.getMontant();
            }
        }
        return av;
    }

    private void initTaux(YvsMutTypeCredit type) {
        double taux = 0, frais = 0;
        int periode = 1, duree = 0;
        if (!type.getGrilles().isEmpty() && credit.getMontant() > 0) {
            YvsMutGrilleTauxTypeCredit g = searchTaux(type, credit.getMontant());
            if (g != null) {
                taux = g.getTaux();
                duree = (credit.getDuree() <= 0) ? g.getPeriodeMaximal().intValue() : credit.getDuree();
            }
        } else {
            taux = type.getTauxMaximal();
            duree = (credit.getDuree() <= 0) ? type.getPeriodeMaximal().intValue() : credit.getDuree();
            periode = type.getPeriodicite();
            frais = type.getTotalFrais();
        }
        credit.getEcheancier().setTaux(taux);
        credit.getEcheancier().setEcartMensualite(periode);
        credit.setDuree(duree);
        credit.setFraisAdditionnel(frais);
    }

    public void chooseTypeCredit(ValueChangeEvent ev) {
        if (ev != null ? ev.getNewValue() != null : false) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                credit.getType().setId(id);
                ManagedTypeCredit w = (ManagedTypeCredit) giveManagedBean(ManagedTypeCredit.class);
                if (w != null) {
                    int idx = w.getTypes().indexOf(new YvsMutTypeCredit(id));
                    if (idx > -1) {
                        YvsMutTypeCredit y = w.getTypes().get(idx);
                        credit.setType(UtilMut.buildBeanTypeCredit(y));
                        initTaux(y);
                    }
                }
                credit.setReference(genererReference(Constantes.MUT_TRANSACTIONS_MUT, credit.getDateCredit(), id, Constantes.TYPECREDIT));
                setConditionCredit();
            } else if (id == -1) {
                openDialog("dlgTypeCredit");
            }
        }
    }

    public void setConditionCredit() {
        setConditionCredit(recopieView(), false);
        if (credit.getType().getId() > 0 && credit.getMontant() > 0) {
            ManagedTypeCredit w = (ManagedTypeCredit) giveManagedBean(ManagedTypeCredit.class);
            if (w != null) {
                int idx = w.getTypes().indexOf(new YvsMutTypeCredit(credit.getType().getId()));
                if (idx > -1) {
                    YvsMutTypeCredit y = w.getTypes().get(idx);
                    credit.setType(UtilMut.buildBeanTypeCredit(y));
                    initTaux(y);
                }
            }
        }
        update("data_credit_condition");
    }

    public void setConditionCredit(Credit bean, boolean simuler) {
        if (currentMutuel != null) {
            if (currentMutuel.getParamsMutuelle() != null) {
                if ((bean.getType() != null) ? bean.getType().getId() > 0 : false) {
                    setConditionCredit(buildCredit(bean), simuler);
                }
            } else {
                getErrorMessage("Les paramètres de base de cette mutuelle n'existe pas !!");
            }
        } else {
            getErrorMessage("Aucune mutuelle n'a été trouvé dans cette socité !!");
        }
        update("data_credit_condition");
    }

    public List<YvsMutConditionCredit> setConditionCredit(YvsMutCredit bean, boolean simuler) {
        YvsMutParametre paramMut = (YvsMutParametre) dao.loadOneByNameQueries("YvsMutParametre.findByMutuelle", new String[]{"mutuelle"}, new Object[]{currentMutuel});
        double salaire = searchSalaire(bean.getCompte());  //salaire contractuel
        YvsMutTypeCredit typ = bean.getType();
        List<YvsMutConditionCredit> re = new ArrayList<>();
        if ((typ != null) ? typ.getId() == 0 : false) {
            ManagedTypeCredit service = (ManagedTypeCredit) giveManagedBean(ManagedTypeCredit.class);
            if (service != null) {
                int idx = service.getTypes().indexOf(bean.getType());
                if (idx >= 0) {
                    typ = service.getTypes().get(idx);
                }
            }
        }
        //1.condition portant sur la durée d'adhésion (Récupère la duré d'adhésion)
        int ecart = 0;
        if (bean.getCompte() != null) {
            ecart = Util.countNumberMonth(mutualiste.getDateAdhesion(), credit.getDateEffet());
        }
        double taux = 0;
        //récupère toutes les avances de salaire faite au mutualiste durant le mois en cours
        //afin de trouver le pourcentage de mon salaire déjà prit        
        //récupère le total des mensualités non encore payé
        Double soeDette = (Double) dao.loadObjectByNameQueries("YvsMutMensualite.findSoeMensualiteEnCour", new String[]{"mutualiste", "etat", "etatMens"}, new Object[]{bean.getCompte().getMutualiste(), Constantes.ETAT_VALIDE, Constantes.ETAT_ATTENTE});
//        //cherche la fraction de salaire correspondant à la mensualité du crédit courant
        soeDette = ((soeDette != null) ? soeDette : 0) + bean.getMontant();
        //calcul de la mensualité du crédit en cours
        int duree = (int) ((bean.getDuree() == 0) ? (double) typ.getPeriodeMaximal() : (double) bean.getDuree());
        double mensualiteEncours = calculMensualite(bean.getMontant(), typ, duree, 0, 1);
//        taux += (mensualiteEncours * 100 / salaire);
        if (simuler) {
            simulerCredit.setMensualite(mensualiteEncours);
        } else {
            credit.setMensualite(mensualiteEncours);
        }
        if (currentMutuel != null) {
            if (simuler) {
                simulerCredit.getConditions().clear();
            } else {
                if (credit.getId() <= 0) {
                    credit.getConditions().clear();
                }
            }
            if (paramMut != null) {
//                Parametre p = UtilMut.buildBeanParametre(currentMutuel.getYvsMutParametreList().get(0));
                YvsMutConditionCredit c = new YvsMutConditionCredit(0, Constantes.MUT_CODE_CONDITION_DUREE_EVALUATION, Constantes.MUT_LIBELLE_CONDITION_DUREE_EVALUATION, paramMut.getDureeMembre(), ecart, "mois", ecart >= paramMut.getDureeMembre());
                c.setId(findCondition(bean, Constantes.MUT_CODE_CONDITION_DUREE_EVALUATION));
                c.setAuthor(currentUser);
                if (c.getId() == 0) {
                    if (simuler) {
                        simulerCredit.getConditions().add(c);
                    } else {
                        credit.getConditions().add(c);
                    }
                } else {
                    if (simuler) {
                        int idx = simulerCredit.getConditions().indexOf(c);
                        if (idx > -1) {
                            simulerCredit.getConditions().set(idx, c);
                        } else {
                            simulerCredit.getConditions().add(c);
                        }
                    } else {
                        int idx = credit.getConditions().indexOf(c);
                        if (idx > -1) {
                            credit.getConditions().set(idx, c);
                        } else {
                            credit.getConditions().add(c);
                        }
                    }
                }
                double limite = (parametreMutuelle.getBaseCapaciteEndettement().equals(Constantes.MUT_NATURE_OP_EPARGNE) ? findMontantTotalEpargne(mutualiste.getId()) : salaire) * parametreMutuelle.getCapaciteEndettement();
                c = new YvsMutConditionCredit(0, Constantes.MUT_CODE_CONDITION_CAPACITE_ENDETTEMENT, Constantes.MUT_LIBELLE_CAPACITE_ENDETTEMENT, limite, soeDette, " F ", soeDette < limite);
                c.setId(findCondition(bean, Constantes.MUT_CODE_CONDITION_CAPACITE_ENDETTEMENT));
                if (c.getId() <= 0) {
                    if (simuler) {
                        simulerCredit.getConditions().add(c);
                    } else {
                        credit.getConditions().add(c);
                    }
                } else {
                    if (simuler) {
                        int idx = simulerCredit.getConditions().indexOf(c);
                        if (idx > -1) {
                            simulerCredit.getConditions().set(idx, c);
                        } else {
                            simulerCredit.getConditions().add(c);
                        }
                    } else {
                        int idx = credit.getConditions().indexOf(c);
                        if (idx > -1) {
                            credit.getConditions().set(idx, c);
                        } else {
                            credit.getConditions().add(c);
                        }
                    }
                }
                long nbre = simuler ? simulerCredit.getAvalises().size() : credit.getAvalises().size();
                c = new YvsMutConditionCredit(0, Constantes.MUT_CODE_CONDITION_NBRE_AVALISE, Constantes.MUT_LIBELE_CONDITION_NBRE_AVALISE, typ.getNbreAvalise(), nbre, "avaliste(s)", nbre >= typ.getNbreAvalise());
                c.setId(findCondition(bean, Constantes.MUT_CODE_CONDITION_NBRE_AVALISE));
                if (c.getId() == 0) {
                    if (simuler) {
                        simulerCredit.getConditions().add(c);
                    } else {
                        credit.getConditions().add(c);
                    }
                } else {
                    if (simuler) {
                        int idx = simulerCredit.getConditions().indexOf(c);
                        if (idx > -1) {
                            simulerCredit.getConditions().set(idx, c);
                        } else {
                            simulerCredit.getConditions().add(c);
                        }
                    } else {
                        int idx = credit.getConditions().indexOf(c);
                        if (idx > -1) {
                            credit.getConditions().set(idx, c);
                        } else {
                            credit.getConditions().add(c);
                        }
                    }
                }
            } else {
                getErrorMessage("Les paramètre de base de cette mutuelle n'existe pas !!");
            }
        } else {
            getErrorMessage("Les paramètre de base de cette mutuelle n'existe pas !!");
        }
        /**
         * *evalue la limite imposé par le type de crédit**
         */
        if ((typ != null) ? (typ.getId() != null) ? typ.getId() > 0 : false : false) {
            switch (typ.getNatureMontant()) {
                case Constantes.MUT_TYPE_MONTANT_FIXE:
                    YvsMutConditionCredit c = new YvsMutConditionCredit(0, Constantes.MUT_CODE_CONDITION_MONTANT_MAX, Constantes.MUT_LIBELLE_CONDITION_MONTANT_MAX, typ.getMontantMaximal(), bean.getMontant(), " F ", bean.getMontant() <= typ.getMontantMaximal());
                    c.setAuthor(currentUser);
                    c.setId(findCondition(bean, Constantes.MUT_CODE_CONDITION_MONTANT_MAX));
                    if (c.getId() == 0) {
                        if (simuler) {
                            simulerCredit.getConditions().add(c);
                        } else {
                            credit.getConditions().add(c);
                        }
                    } else {
                        if (simuler) {
                            int idx = simulerCredit.getConditions().indexOf(c);
                            if (idx > -1) {
                                simulerCredit.getConditions().set(idx, c);
                            } else {
                                simulerCredit.getConditions().add(c);
                            }
                        } else {
                            int idx = credit.getConditions().indexOf(c);
                            if (idx > -1) {
                                credit.getConditions().set(idx, c);
                            } else {
                                credit.getConditions().add(c);
                            }
                        }
                    }
                    break;
                case Constantes.MUT_TYPE_MONTANT_POURCENTAGE:
                    //Permet de rechercher le salaire d'un mutualiste
                    double montant = salaire * typ.getMontantMaximal() / 100;
                    c = new YvsMutConditionCredit(0, Constantes.MUT_CODE_CONDITION_MONTANT_MAX, Constantes.MUT_LIBELLE_CONDITION_MONTANT_MAX, montant, bean.getMontant(), " Fcfa", bean.getMontant() <= montant);
                    c.setAuthor(currentUser);
                    c.setId(findCondition(bean, Constantes.MUT_CODE_CONDITION_MONTANT_MAX));
                    if (c.getId() == 0) {
                        if (simuler) {
                            simulerCredit.getConditions().add(c);
                        } else {
                            credit.getConditions().add(c);
                        }
                    } else {
                        if (simuler) {
                            int idx = simulerCredit.getConditions().indexOf(c);
                            if (idx > -1) {
                                simulerCredit.getConditions().set(idx, c);
                            } else {
                                simulerCredit.getConditions().add(c);
                            }
                        } else {
                            int idx = credit.getConditions().indexOf(c);
                            if (idx > -1) {
                                credit.getConditions().set(idx, c);
                            } else {
                                credit.getConditions().add(c);
                            }
                        }
                    }
                    break;
                default:
                    c = new YvsMutConditionCredit(0, Constantes.MUT_CODE_CONDITION_MONTANT_MAX, Constantes.MUT_LIBELLE_CONDITION_MONTANT_MAX, 0, bean.getMontant(), " Fcfa", false);
                    c.setAuthor(currentUser);
                    c.setId(findCondition(bean, Constantes.MUT_CODE_CONDITION_MONTANT_MAX));
                    if (c.getId() == 0) {
                        if (simuler) {
                            simulerCredit.getConditions().add(c);
                        } else {
                            credit.getConditions().add(c);
                        }
                    } else {
                        if (simuler) {
                            int idx = simulerCredit.getConditions().indexOf(c);
                            if (idx > -1) {
                                simulerCredit.getConditions().set(idx, c);
                            } else {
                                simulerCredit.getConditions().add(c);
                            }
                        } else {
                            int idx = credit.getConditions().indexOf(c);
                            if (idx > -1) {
                                credit.getConditions().set(idx, c);
                            } else {
                                credit.getConditions().add(c);
                            }
                        }
                    }
                    break;
            }
            /*Calcul la capacité de remboursement*/
            double veleurRemb = typ.getCoefficientRemboursement() * salaire * bean.getDuree();
            double limite = bean.getMontant();
            YvsMutConditionCredit c = new YvsMutConditionCredit(0, Constantes.MUT_CODE_CONDITION_CAPACITE_REMBOURSEMENT, Constantes.MUT_LIBELLE_CONDITION_CAPACITE_REMBOURSEMENT, limite, veleurRemb, " F ", veleurRemb >= limite);
            c.setId(findCondition(bean, Constantes.MUT_CODE_CONDITION_CAPACITE_REMBOURSEMENT));
            if (c.getId() <= 0) {
                if (simuler) {
                    simulerCredit.getConditions().add(c);
                } else {
                    credit.getConditions().add(c);
                }
            } else {
                if (simuler) {
                    int idx = simulerCredit.getConditions().indexOf(c);
                    if (idx > -1) {
                        simulerCredit.getConditions().set(idx, c);
                    } else {
                        simulerCredit.getConditions().add(c);
                    }
                } else {
                    int idx = credit.getConditions().indexOf(c);
                    if (idx > -1) {
                        credit.getConditions().set(idx, c);
                    } else {
                        credit.getConditions().add(c);
                    }
                }
            }
            /*End capacité de remborsement*/
            /**
             * Durée du crédit
             */
            c = new YvsMutConditionCredit(0, Constantes.MUT_CODE_CONDITION_DUREE_CREDIT, Constantes.MUT_LIBELLE_CONDITION_DUREE_CREDIT, typ.getPeriodeMaximal(), credit.getDuree(), " Mois", credit.getDuree() <= typ.getPeriodeMaximal());
            c.setAuthor(currentUser);
            c.setId(findCondition(bean, Constantes.MUT_CODE_CONDITION_DUREE_CREDIT));
            if (c.getId() == 0) {
                if (simuler) {
                    simulerCredit.getConditions().add(c);
                } else {
                    credit.getConditions().add(c);
                }
            } else {
                if (simuler) {
                    int idx = simulerCredit.getConditions().indexOf(c);
                    if (idx > -1) {
                        simulerCredit.getConditions().set(idx, c);
                    } else {
                        simulerCredit.getConditions().add(c);
                    }
                } else {
                    int idx = credit.getConditions().indexOf(c);
                    if (idx > -1) {
                        credit.getConditions().set(idx, c);
                    } else {
                        credit.getConditions().add(c);
                    }
                }
            }

        } else {
            YvsMutConditionCredit c = new YvsMutConditionCredit(0, Constantes.MUT_CODE_CONDITION_MONTANT_MAX, Constantes.MUT_LIBELLE_CONDITION_MONTANT_MAX, 0, bean.getMontant(), " Fcfa", false);
            c.setAuthor(currentUser);
            c.setId(findCondition(bean, Constantes.MUT_CODE_CONDITION_MONTANT_MAX));
            if (c.getId() == 0) {
                if (simuler) {
                    simulerCredit.getConditions().add(c);
                } else {
                    credit.getConditions().add(c);
                }
            } else {
                if (simuler) {
                    int idx = simulerCredit.getConditions().indexOf(c);
                    if (idx > -1) {
                        simulerCredit.getConditions().set(idx, c);
                    } else {
                        simulerCredit.getConditions().add(c);
                    }
                } else {
                    int idx = credit.getConditions().indexOf(c);
                    if (idx > -1) {
                        credit.getConditions().set(idx, c);
                    } else {
                        credit.getConditions().add(c);
                    }
                }
            }
        }
        /**
         * *Vérifie si le total de l'epargne + le montant des avalises couvre
         * le crédit***
         */
        double couverture = findMontantCouverture(mutualiste);
        couverture += soeAvalise();
        YvsMutConditionCredit c = new YvsMutConditionCredit(0, Constantes.MUT_CODE_CONDITION_MONTANT_COUVERTURE, Constantes.MUT_LIBELLE_CONDITION_MONTANT_COUVERTURE, bean.getMontant() * (paramMut.getTauxCouvertureCredit() > 0 ? (paramMut.getTauxCouvertureCredit() / 100) : 1), couverture, " F", couverture >= (bean.getMontant() * (paramMut.getTauxCouvertureCredit() > 0 ? (paramMut.getTauxCouvertureCredit() / 100) : 1)));
        c.setAuthor(currentUser);
        c.setId(findCondition(bean, Constantes.MUT_CODE_CONDITION_MONTANT_COUVERTURE));
        if (c.getId() == 0) {
            if (simuler) {
                simulerCredit.getConditions().add(c);
            } else {
                credit.getConditions().add(c);
            }
        } else {
            if (simuler) {
                int idx = simulerCredit.getConditions().indexOf(c);
                if (idx > -1) {
                    simulerCredit.getConditions().set(idx, c);
                } else {
                    simulerCredit.getConditions().add(c);
                }
            } else {
                int idx = credit.getConditions().indexOf(c);
                if (idx > -1) {
                    credit.getConditions().set(idx, c);
                } else {
                    credit.getConditions().add(c);
                }
            }
        }
        definedIfVoteVisible();
        update("data_credit_condition");
        if (simuler) {
            re.addAll(simulerCredit.getConditions());
        } else {
            re.addAll(credit.getConditions());
        }
        return re;
    }

    private double findSoeRemboursementEnCours(Mutualiste mu, Date date) {
        List etats = Arrays.asList("W", "R");
        List<Object[]> re = dao.loadNameQueries("YvsMutMensualite.findDistinctMensualiteEnCours", new String[]{"etats", "mutualiste", "date"}, new Object[]{etats, new YvsMutMutualiste(mu.getId()), date});
        double result = 0;
        if (re != null) {
            for (Object[] o : re) {
                if (o[0] != null) {
                    result += (Double) o[0];
                }
            }
        }
        return result;
    }

    private double findMontantCouverture(Mutualiste m) {
        //1.Total epargne
        //2.Avalise - ( personnes avalisées par le mutualiste)
        //2.Avalise + ( personnes qui avalisent le crédit en cours)
        //3.crédit en cours
        m.setMontantTotalEpargne(findMontantTotalEpargne(m.getId()));
        return m.getMontantTotalEpargne() + credit.getSoeAvalise() - (m.getCouvertureAvalise() + m.getMontantCredit());
    }

    private long findCondition(YvsMutCredit cr, String libelle) {
        for (YvsMutConditionCredit c : cr.getConditions()) {
            if (c.getCode().equals(libelle)) {
                return c.getId();
            }
        }
        return 0;
    }

    private YvsMutConditionCredit findCondition(List<YvsMutConditionCredit> l, String libelle) {
        for (YvsMutConditionCredit c : l) {
            if (c.getCode().equals(libelle)) {
                return c;
            }
        }
        return new YvsMutConditionCredit();
    }

    /*Permet de rechercher le salaire d'un mutualiste. le salaire du mutualiste sera la moyenne des n dernier salaire perçu. si 
     ce dernier n'a pas encore de salaire, on prendra son salaire de base (slaire contractuelle + prime)*/
    private double searchSalaire(YvsMutCompte cpt) {
        if (cpt != null ? cpt.getId() != null ? cpt.getId() > 0 : false : false) {
            cpt = (YvsMutCompte) dao.loadOneByNameQueries("YvsMutCompte.findById", new String[]{"id"}, new Object[]{cpt.getId()});
            double salaire = 0;
            champ = new String[]{"mutualiste", "natureCpte", "date", "mouvement", "natureOp"};
            val = new Object[]{cpt.getMutualiste(), Constantes.MUT_TYPE_COMPTE_COURANT, new Date(), Constantes.MUT_SENS_OP_DEPOT, Constantes.MUT_NATURE_OP_SALAIRE};
            List<YvsMutOperationCompte> ls = dao.loadNameQueries("YvsMutOperationCompte.findSalaireMoyen", champ, val, 0, parametreMutuelle.getPeriodeSalaireMoyen());
            if (!ls.isEmpty()) {
                int i = 0;
                for (YvsMutOperationCompte p : ls) {
                    salaire += (p.getMontant());
                    i++;
                }
                salaire = salaire / i;
            }
            if (salaire == 0) {
                salaire = findSalaireContrat(cpt);
            }
            return (salaire == 0) ? 1 : salaire;
        }
        return 0;
    }

    private double findSalaireContrat(YvsMutCompte cpt) {
        champ = new String[]{"id"};
        val = new Object[]{cpt.getId()};
        YvsMutCompte y = (YvsMutCompte) dao.loadOneByNameQueries("YvsMutCompte.findById", champ, val);
        if (y != null ? y.getMutualiste().getEmploye().getContrat() != null : false) {
            YvsGrhContratEmps c = y.getMutualiste().getEmploye().getContrat();
            if (c.getSalaireMensuel() == null) {
                if (c.getSalaireHoraire() != null) {
                    c.setSalaireMensuel(c.getSalaireHoraire() * 30);
                } else {
                    c.setSalaireMensuel(0.0);
                }
            }
            for (YvsGrhElementAdditionel e : c.getGains()) {
                if (e.getPermanent()) {
                    c.setSalaireMensuel(c.getSalaireMensuel() + e.getMontantElement());
                }
            }
            return c.getSalaireMensuel();
        }
        return 0;
    }

    public Echellonage recopieViewEchellonage() {
        Echellonage e = new Echellonage();
        e.setDateEchellonage(echeancierSelect.getDateEchellonage());
        e.setIdEch(echeancierSelect.getId());
        e.setEtat(echeancierSelect.getEtat());
        e.setDureeEcheance(echeancierSelect.getDureeEcheance());
        e.setMontant(echeancierSelect.getMontant());
        e.setTaux(echeancierSelect.getTaux());
        e.setCredit(UtilMut.buildBeanCredit(echeancierSelect.getCredit()));
        return e;
    }

    //retourne l'echeancier par defaut
    public Echellonage defaultEchellonage(GrilleTaux g, double montant) {
        Echellonage e = new Echellonage();
        e.setDateEchellonage((credit.getEcheancier().getDateEchellonage() != null) ? credit.getEcheancier().getDateEchellonage() : new Date());
        e.setEcartMensualite((credit.getEcheancier().getEcartMensualite() > 0) ? credit.getEcheancier().getEcartMensualite() : 1);
        e.setEtat(Constantes.ETAT_ATTENTE);
        e.setDureeEcheance(g.getPeriodeMaximal());
        e.setMontant(montant); //montant total à remboursser (Capital + Intérêt)
        e.setTaux(g.getTaux());
        e.setCreditRetainsInteret(parametreMutuelle.isCreditRetainsInteret());
        e.setCredit(UtilMut.buildBeanCredit(creditSelect));
        return e;
    }

    //Permet de rechercher la grille pour le montant du credit
    public YvsMutGrilleTauxTypeCredit searchTaux(YvsMutTypeCredit t, double montant) {
        if (t != null) {
            if ((t.getGrilles() != null) ? !t.getGrilles().isEmpty() : false) {
                for (YvsMutGrilleTauxTypeCredit r : t.getGrilles()) {
                    if (r.getMontantMinimal() <= montant && r.getMontantMaximal() >= montant) {
                        return r;
                    }
                }
            }
            YvsMutGrilleTauxTypeCredit r = new YvsMutGrilleTauxTypeCredit(-1L);
            r.setMontantMaximal(montant);
            r.setMontantMinimal(montant);
            r.setTaux(t.getTauxMaximal());
            r.setPeriodeMaximal(t.getPeriodeMaximal());
            return r;
        }
        return null;
    }

    public YvsMutEchellonage buildEchellonage(Echellonage y) {
        return buildEchellonage(y, creditSelect);
    }

    public YvsMutEchellonage buildEchellonage(Echellonage y, YvsMutCredit entityCredit) {
        YvsMutEchellonage e = new YvsMutEchellonage();
        if (y != null) {
            e.setCredit(entityCredit);
            e.setDateEchellonage((y.getDateEchellonage() != null) ? y.getDateEchellonage() : new Date());
            e.setDureeEcheance(y.getDureeEcheance());
            e.setEcartMensualite(y.getEcartMensualite());
            e.setEtat(y.getEtat());
            e.setId(y.getIdEch());
            e.setMontant(y.getMontant());
            e.setTaux(y.getTaux());
            e.setDateUpdate(new Date());
            e.setAuthor(currentUser);
            e.setActif(true);
            e.setCreditRetainsInteret(y.isCreditRetainsInteret());
        }
        return e;
    }

    public YvsMutEchellonage saveNewEchellonage(Echellonage bean) {
        return saveNewEchellonage(bean, creditSelect);
    }

    public YvsMutEchellonage saveNewEchellonage(Echellonage bean, YvsMutCredit y) {
        try {
            YvsMutEchellonage entity = buildEchellonage(bean, y);
            if (entity.getId() != null ? entity.getId() < 1 : true) {
                entity.setDateSave(new Date());
                entity.setId(null);
                entity = (YvsMutEchellonage) dao.save1(entity);
            } else {
                dao.update(entity);
            }
            bean.setIdEch(entity.getId());
            int idx = credit.getEcheanciers().indexOf(entity);
            if (idx < 0) {
                credit.getEcheanciers().add(entity);
            } else {
                credit.getEcheanciers().set(idx, entity);
            }
            update("tabview_credit:data_echeancier");
            return entity;
        } catch (Exception e) {
            Logger.getLogger(ManagedCredit.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    //Permet de calculer les mensualites de l'echeancier
    public List<Mensualite> calculMensualite(Echellonage bean, Credit y, double montant) {
        List<Mensualite> l = new ArrayList<>();
        double capital = montant; //montant du crédit
        double mensualite = 0;
        int duree = (int) bean.getDureeEcheance(); //durée de remboursement
        double taux = bean.getTaux();//Taux d'intérêt annuel
        double periodicite = bean.getEcartMensualite();   //périodicité de remboursement

        Calendar cal = Util.dateToCalendar(bean.getDateEchellonage());
        double interet;
        Mensualite m;
        taux = (taux / 100);
        char formule_interet = y.getType().getFormuleInteret();
        if (formule_interet == Constantes.FORMULE_INTERET_SIMPLE) {
            interet = capital * taux;
            mensualite = calculMensualite(capital, bean.getTaux(), duree, 0, (int) periodicite, ' ');
            for (int i = 0; i < duree; i++) {
                m = new Mensualite(i + 1);
                m.setDateMensualite(cal.getTime());
                m.setEtat(Constantes.ETAT_ATTENTE);
                m.setAmortissement(Constantes.arrondi(mensualite, parametreMutuelle.getArrondiValeur()));   //capital restant du
                m.setMontant(Constantes.arrondi(mensualite + interet, parametreMutuelle.getArrondiValeur()));
                m.setInteret(Constantes.arrondi(interet, parametreMutuelle.getArrondiValeur()));
                l.add(m);
                cal.add(Calendar.MONTH, bean.getEcartMensualite());
            }
        } else {
            char type_mensualite = y.getType().getTypeMensualite();
            switch (type_mensualite) {
                case Constantes.TYPE_MENSUALITE_AMORTISSEMENT_FIXE: {
                    double amortissement = capital / duree;
                    for (int i = 0; i < duree; i++) {
                        m = new Mensualite(i + 1);

                        mensualite = calculMensualite(capital, bean.getTaux(), duree, i + 1, (int) periodicite, Constantes.TYPE_MENSUALITE_AMORTISSEMENT_FIXE);
                        interet = mensualite - amortissement;

                        m.setDateMensualite(cal.getTime());
                        m.setEtat(Constantes.ETAT_ATTENTE);
                        m.setMontant(Constantes.arrondi(mensualite, parametreMutuelle.getArrondiValeur()));
                        m.setInteret(Constantes.arrondi(interet, parametreMutuelle.getArrondiValeur()));
                        m.setAmortissement(amortissement);   //capital restant du
                        l.add(m);

                        cal.add(Calendar.MONTH, bean.getEcartMensualite());
                    }
                    break;
                }
                case Constantes.TYPE_MENSUALITE_ANNUITE_FIXE: {
                    //taux = (taux / 100) / (12 / periodicite);
                    mensualite = calculMensualite(capital, bean.getTaux(), duree, 0, (int) periodicite, Constantes.TYPE_MENSUALITE_ANNUITE_FIXE);
                    for (int i = 0; i < duree; i++) {
                        m = new Mensualite(i + 1);
                        interet = capital * taux;
                        m.setDateMensualite(cal.getTime());
                        m.setEtat(Constantes.ETAT_ATTENTE);
                        m.setMontant(Constantes.arrondi(mensualite, parametreMutuelle.getArrondiValeur()));
                        m.setInteret(Constantes.arrondi(interet, parametreMutuelle.getArrondiValeur()));
                        m.setAmortissement(m.getMontant() - m.getInteret());   //capital restant du
                        capital = capital - m.getAmortissement();
                        l.add(m);
                        cal.add(Calendar.MONTH, bean.getEcartMensualite());
                    }
                    break;
                }
                case Constantes.TYPE_MENSUALITE_INFINE: {
                    //taux = (taux / 100) / (12 / periodicite);
                    for (int i = 0; i < duree; i++) {
                        m = new Mensualite(i + 1);
                        interet = capital * taux;
                        m.setDateMensualite(cal.getTime());
                        m.setEtat(Constantes.ETAT_ATTENTE);
                        if (i < duree - 1) {
                            m.setMontant(0);
                            m.setInteret(Constantes.arrondi(interet, parametreMutuelle.getArrondiValeur()));
                        } else {
                            m.setMontant(capital);
                            m.setInteret(0);
                        }
                        m.setAmortissement((i < duree - 1) ? capital : 0);   //capital restant du
                        l.add(m);
                        cal.add(Calendar.MONTH, bean.getEcartMensualite());
                    }
                    break;
                }
                default:
                    break;
            }
        }
        return l;
    }

    public YvsMutMensualite buildMensualite(Mensualite y, YvsMutEchellonage entity) {
        return buildMensualite(y.getId(), y, entity);
    }

    public YvsMutMensualite buildMensualite(long id, Mensualite y, YvsMutEchellonage entity) {
        YvsMutMensualite m = new YvsMutMensualite();
        if (y != null) {
            m.setDateMensualite((y.getDateMensualite() != null) ? y.getDateMensualite() : new Date());
            m.setId(id);
            m.setMontant(Constantes.arrondi(y.getMontant(), parametreMutuelle.getArrondiValeur()));
            m.setEchellonage(entity);
            m.setInteret(Constantes.arrondi(y.getInteret(), parametreMutuelle.getArrondiValeur()));
            m.setAmortissement(Constantes.arrondi(y.getAmortissement(), 2));
            m.setMontantVerse(0.0);
            m.setEtat(y.getEtat());
            m.setAuthor(currentUser);
        }
        return m;
    }

    public YvsMutMensualite saveNewMensualite(Mensualite bean, YvsMutEchellonage entityEch) {
        try {
            YvsMutMensualite entity = buildMensualite(bean, entityEch);
            entity.setDateSave(new Date());
            entity.setDateUpdate(new Date());
            entity.setId(null);
            entity = (YvsMutMensualite) dao.save1(entity);
            bean.setId(entity.getId());
            return entity;
        } catch (Exception e) {
            Logger.getLogger(ManagedCredit.class.getName()).log(Level.SEVERE, null, e);
            return new YvsMutMensualite();
        }
    }

    public void populateViewEcheancier(Echellonage bean) {
        cloneObject(credit.getEcheancier(), bean);
        gardeReste = bean.getMontant();
        resetFicheMensualite();
    }

    public void resetFicheEcheancier() {
        credit.setEcheancier(new Echellonage());
        credit.getEcheancier().setCreditRetainsInteret(parametreMutuelle.isCreditRetainsInteret());
        gardeReste = 0;
        credit.getEcheancier().setCredit(new Credit());
        credit.getEcheancier().setDateEchellonage(new Date());
        credit.getEcheancier().setMensualites(new ArrayList<YvsMutMensualite>());
        update("blog_echeancier_credit");
        resetFicheMensualite();
    }

    public void populateViewMensualite(Mensualite bean) {
        cloneObject(mensualite, bean);
    }

    public void populateViewReglement(ReglementMensualite bean) {
        cloneObject(reglement, bean);
    }

    public void populateViewReglementC(ReglementCreditMut bean) {
        cloneObject(reglementc, bean);
    }

    public void resetFicheMensualite() {
        mensualite = new Mensualite();
        mensualiteSelect = new YvsMutMensualite();
        credit.getEcheancier().setReglements(dao.loadNameQueries("YvsMutReglementMensualite.findByEcheancier", new String[]{"echeancier"}, new Object[]{new YvsMutEchellonage(credit.getEcheancier().getIdEch())}));
        update("date_reglement_mensualite_credit");
        resetFicheReglement();
    }

    public void resetFicheReglementC() {
        reglementc = new ReglementCreditMut();
        reglementc.setCompte(credit.getCompte());
        if (parametreMutuelle.isPaiementParCompteStrict()) {
            reglementc.setModePaiement(Constantes.MUT_MODE_PAIEMENT_COMPTE);
        }
        reglementc.setReglerPar(currentUser.getUsers().getNomUsers());
        update("form_reglementc_credit");
    }

    public void resetFicheReglement() {
        reglement = new ReglementMensualite();
        reglement.setReglerPar(currentUser.getUsers().getNomUsers());
        update("form_reglement_credit");
    }

    public void loadOnViewEcheancier(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            echeancierSelect = (YvsMutEchellonage) ev.getObject();
            onselectEcheancier(echeancierSelect);
        }
    }

    public void onselectEcheancier(YvsMutEchellonage e) {
        if (e != null) {
            echeancierSelect = e;
            echeancierSelect.setMensualites(dao.loadNameQueries("YvsMutMensualite.findByEchellonage", new String[]{"echellonage"}, new Object[]{echeancierSelect}));
            populateViewEcheancier(UtilMut.buildBeanEchellonage(echeancierSelect));
            if (echeancierSelect.getMensualites() != null ? echeancierSelect.getMensualites().isEmpty() : true) {
                genereMensualite(echeancierSelect, credit.getEcheancier());
            }
            gardeReste = credit.getEcheancier().getMontant();
            update("blog_echeancier_credit");
        }
    }

    public void unLoadOnViewEcheancier(UnselectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
//            Echellonage bean = (Echellonage) ev.getObject();
            resetFicheEcheancier();
            update("blog_echeancier_credit");
        }
    }

    public void loadOnViewMensualite(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            mensualiteSelect = (YvsMutMensualite) ev.getObject();
            populateViewMensualite(UtilMut.buildBeanMensualite(mensualiteSelect));
            credit.getEcheancier().getReglements().clear();
            credit.getEcheancier().getReglements().addAll(mensualiteSelect.getReglements());
            update("date_reglement_mensualite_credit");
        }
    }

    public void unLoadOnViewMensualite(UnselectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            resetFicheMensualite();
        }
    }

    public void loadOnViewReglement(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutReglementMensualite y = (YvsMutReglementMensualite) ev.getObject();
            populateViewReglement(UtilMut.buildBeanReglementMensualite(y));
            update("form_reglement_credit");
        }
    }

    public void unLoadOnViewReglement(UnselectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            resetFicheReglement();
        }
    }

    public void loadOnViewReglementC(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutReglementCredit y = (YvsMutReglementCredit) ev.getObject();
            populateViewReglementC(UtilMut.buildBeanReglementCredit(y));
            update("tabview_credit:form_reglementc_credit");
        }
    }

    public void unLoadOnViewReglementC(UnselectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            resetFicheReglementC();
        }
    }

    public YvsMutReglementMensualite buildReglementMensualite(ReglementMensualite y, YvsMutMensualite mensualiteSelect) {
        YvsMutReglementMensualite r = new YvsMutReglementMensualite();
        if (y != null) {
            r.setDateReglement((y.getDateReglement() != null) ? y.getDateReglement() : new Date());
            r.setId(y.getId());
            r.setMontant(Constantes.arrondi(y.getMontant(), parametreMutuelle.getArrondiValeur()));
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setReglePar(y.getReglerPar());
            r.setStatutPiece(Constantes.STATUT_DOC_PAYER);
            r.setCaisse(new YvsMutCaisse(y.getCaisse().getId(), y.getCaisse().getReference()));
            r.setAuthor(currentUser);
            r.setModePaiement(y.getModePaiement());
            r.setCodeOperation(y.getCodeOperation());
            if (y.getModePaiement().equals(Constantes.MUT_MODE_PAIEMENT_COMPTE)) {
                r.setCompte(UtilMut.buildCompte(y.getCompte(), currentUser));
            } else {
                r.setCompte(null);
            }
            if ((mensualiteSelect != null) ? (mensualiteSelect.getId() != null ? mensualiteSelect.getId() != 0 : false) : false) {
                r.setMensualite(new YvsMutMensualite(mensualiteSelect.getId()));
            }
        }
        return r;
    }

    public ReglementMensualite recopieViewReglementMensualite(ReglementMensualite bean) {
        ReglementMensualite r = new ReglementMensualite();
        r.setDateReglement((bean.getDateReglement() != null) ? bean.getDateReglement() : new Date());
        r.setId(bean.getId());
        r.setMontant(bean.getMontant());
        return r;
    }

    public boolean controleFicheReglementMensualite(ReglementMensualite bean) {
        return controleFicheReglementMensualite(bean, credit);
    }

    public boolean controleFicheReglementMensualite(ReglementMensualite bean, Credit credit) {
        if (bean.getMontant() <= 0) {
            getErrorMessage("Vous devez entrer le montant");
            return false;
        }
        if (!credit.getEtatValidation().equals(Constantes.ETAT_VALIDE)) {
            getErrorMessage("Ce crédit n'est pas encore validé");
            return false;
        }
        if (credit.getStatutPaiement() != Constantes.STATUT_DOC_PAYER) {
            getErrorMessage("Ce crédit n'est pas encore payé");
            return false;
        }
        if (!credit.getEcheancier().getEtat().equals(Constantes.ETAT_ENCOURS) && !credit.getEcheancier().getEtat().equals(Constantes.ETAT_ATTENTE)) {
            getErrorMessage("Le plan d'échéance pour cette mensualité à été suspendu ou annulé", "Veuillez vous référe à ce dernier");
            return false;
        }
        if (bean.getModePaiement().equals(Constantes.MUT_MODE_PAIEMENT_ESPECE)) {
            if (bean.getCaisse() != null ? bean.getCaisse().getId() < 1 : true) {
                getErrorMessage("Vous devez precisez la caisse");
                return false;
            }
        }
        if (bean.getModePaiement().equals(Constantes.MUT_MODE_PAIEMENT_COMPTE)) {
            if (bean.getCaisse() != null ? bean.getCompte().getId() < 1 : true) {
                getErrorMessage("Vous devez precisez la caisse");
                return false;
            }
        }
//        if (bean.getMontant() > bean.getMontant()) {
//            getErrorMessage("Vous ne pouvez pas enregistrer ce montant");
//            return false;
//        }
        return true;
    }

    public ReglementMensualite recopieViewReglementMensualite() {
        ReglementMensualite r = new ReglementMensualite();
        r.setDateReglement((reglement.getDateReglement() != null) ? reglement.getDateReglement() : new Date());
        r.setId(reglement.getId());
        r.setMontant(reglement.getMontant());
        r.setReglerPar(reglement.getReglerPar());
        r.setStatutPiece(reglement.getStatutPiece());
        r.setCaisse(reglement.getCaisse());
        r.setModePaiement(reglement.getModePaiement());
        r.setCompte(reglement.getCompte());
        return r;
    }

    public void saveNewReglementMensualite() {
        if (creditSelect.getType().getModelRemboursement().equals(Constantes.MODEL_REMBOURSEMENT_STRICT)) {
            getErrorMessage("Le mode de reglement est stricte... vous ne pouvez pas enregistrer un reglement manuel");
            return;
        }
        ReglementMensualite bean = recopieViewReglementMensualite();
        if (controleFicheReglementMensualite(bean)) {
            List<YvsMutMensualite> list = dao.loadNameQueries("YvsMutMensualite.findByEchellonageNotRegle", new String[]{"echellonage"}, new Object[]{echeancierSelect});
            if (list != null ? !list.isEmpty() : false) {
                saveNewReglementMensualite(list.get(0), bean);
                YvsMutCredit y = new YvsMutCredit(credit.getId());
                equilibre(y, false);
            }
        }
    }

    //Appelé depuis le tableau des mensualités
    public boolean saveNewReglementMensualite(YvsMutMensualite mens) {
        ReglementMensualite bean = new ReglementMensualite();
        bean.setDateReglement(new Date());
        bean.setMontant(mens.getMontantReste());
        bean.setReglerPar(currentUser.getUsers().getNomUsers());
        bean.setCaisse(reglement.getCaisse());
        bean.setModePaiement(reglement.getModePaiement());
        bean.setCompte(reglement.getCompte());
        int index = credit.getEcheancier().getMensualites().indexOf(mens);
        //vérifie si la mensualité précédente a été déjà réglé
        boolean re = false;
        if (index > 0) {
            if (credit.getEcheancier().getMensualites().get(index - 1).getEtat().equals(Constantes.ETAT_REGLE)) {
                re = saveNewReglementMensualite(bean, mens, mensualite, echeancierSelect, credit, creditSelect);
            } else {
                getErrorMessage("Vous devez d'abord régler la mensualité précédente !");
            }
        } else {
            re = saveNewReglementMensualite(bean, mens, mensualite, echeancierSelect, credit, creditSelect);
        }
        if (re) {
            succes();
            update("date_mensualite_echeancier");
            update("date_reglement_mensualite_credit");
        }
        return re;
    }

    public void saveNewReglementMensualite(YvsMutMensualite mens, double montant) {
        ReglementMensualite bean = new ReglementMensualite();
        bean.setDateReglement(new Date());
        bean.setMontant(montant);
        if (saveNewReglementMensualite(bean, mens, mensualite, echeancierSelect, credit, creditSelect)) {
            succes();
        }
    }

    //Cette méthode distribue un montant libre sur plusieurs mensualités
    public void saveNewReglementMensualite(YvsMutMensualite mens, ReglementMensualite reg) {
        double reste = mens.getMontantReste();
        if (reg.getMontant() > reste) {
            double montant = reg.getMontant();
            if (reste > 0) {
                reg.setMontant(reste);
                saveNewReglementMensualite(mens, reg);
            }
            montant -= reste;
            if (montant > 0) {
                //si le montantant versé est supérieure à la mensualité, on paye les prochaines mensualité
                List<YvsMutMensualite> list = dao.loadNameQueries("YvsMutMensualite.findByEchellonageNotRegle", new String[]{"echellonage"}, new Object[]{new YvsMutEchellonage(credit.getEcheancier().getIdEch())});
                for (YvsMutMensualite m : list) {
                    if (montant > m.getMontantReste()) {
                        reste = m.getMontantReste();
                    } else {
                        reste = montant;
                    }
                    if (reste > 0) {
                        reg.setMontant(reste);
                        saveNewReglementMensualite(m, reg);
                    }
                    montant -= reste;
                    if (montant <= 0) {
                        break;
                    }
                }
            }
        } else {
            if (saveNewReglementMensualite(reg, mens, mensualite, echeancierSelect, credit, creditSelect)) {
                succes();
            }
        }
    }

    public void saveNewReglementMensualite(YvsMutMensualite mens, ReglementMensualite reg, List<YvsMutMensualite> list) {
        if (reg.getMontant() > mens.getMontantReste()) {
            double montant = reg.getMontant();
            double reste = mens.getMontantReste();
            if (reste > 0) {
                reg.setMontant(reste);
                saveNewReglementMensualite(mens, reg);
            }
            montant -= reste;
            if (montant > 0 && list != null) {
                for (YvsMutMensualite m : list) {
                    if (montant > m.getMontantReste()) {
                        reste = m.getMontantReste();
                    } else {
                        reste = montant;
                    }
                    if (reste > 0) {
                        reg.setMontant(reste);
                        saveNewReglementMensualite(m, reg);
                    }
                    montant -= reste;
                    if (montant <= 0) {
                        break;
                    }
                }
            }
        } else {
            if (saveNewReglementMensualite(reg, mens, mensualite, echeancierSelect, credit, creditSelect)) {
                succes();
            }
        }
    }

    public boolean saveNewReglementMensualite(ReglementMensualite bean, YvsMutMensualite mens, Mensualite mensualite, YvsMutEchellonage echeancierSelect, Credit credit, YvsMutCredit creditSelect) {
        try {
            if (controleFicheReglementMensualite(bean, credit)) {
                YvsMutReglementMensualite entity = buildReglementMensualite(bean, mens);
                if (bean.getModePaiement().equals(Constantes.MUT_MODE_PAIEMENT_ESPECE)) {
                    entity.setCompte(null);
                }
                if (bean.getModePaiement().equals(Constantes.MUT_MODE_PAIEMENT_COMPTE)) {
                    entity.setCaisse(null);
                }
                entity.setMensualite(mens);
                entity.setId(null);
                entity = (YvsMutReglementMensualite) dao.save1(entity);
                bean.setId(entity.getId());
                mens.getReglements().add(entity);
                if (!mensualite.getReglements().contains(entity)) {
                    mensualite.getReglements().add(entity);
                }
                if (!credit.getEcheancier().getReglements().contains(entity)) {
                    credit.getEcheancier().getReglements().add(entity);
                }
                String etat = Constantes.ETAT_REGLE;
                if (mens.getMontantReste() > bean.getMontant()) {
                    etat = Constantes.ETAT_ENCOURS;
                }
                mens.setNew_(true);
                mens.setEtat(etat);
                //modifie les propriétés de la mensualité
                YvsMutMensualite update = new YvsMutMensualite(mens.getId(), mens);
                update.setEchellonage(echeancierSelect);
                update.setAuthor(currentUser);
                update.setDateUpdate(new Date());
                update.getReglements().clear();
                update.setDateReglement(bean.getDateReglement());
                update.setReglePar(bean.getReglerPar());
                dao.update(update);
                //modifie les propriétés de l'echeancier
                echeancierSelect.setEtat(Constantes.ETAT_ENCOURS);
                if ((credit.getEcheancier().getMontant() - (credit.getEcheancier().isCreditRetainsInteret() ? credit.getEcheancier().getMontantInteret() : 0)) <= credit.getEcheancier().getMontantVerse()) {
                    echeancierSelect.setEtat(Constantes.ETAT_REGLE);
                }
                credit.getEcheancier().setEtat(echeancierSelect.getEtat());
                creditSelect.setStatutCredit(echeancierSelect.getEtat().charAt(0));
                credit.setStatutRemboursement(creditSelect.getStatutCredit());
                dao.update(echeancierSelect);
                dao.update(creditSelect);

                int idx = credit.getEcheancier().getMensualites().indexOf(mens);
                if (idx > -1) {
                    credit.getEcheancier().getMensualites().set(idx, mens);
                }
                idx = echeancierSelect.getMensualites().indexOf(mens);
                if (idx > -1) {
                    echeancierSelect.getMensualites().set(idx, mens);
                }
                idx = credit.getEcheanciers().indexOf(echeancierSelect);
                if (idx > -1) {
                    credit.getEcheanciers().set(idx, echeancierSelect);
                }
                idx = creditSelect.getRemboursements().indexOf(echeancierSelect);
                if (idx > -1) {
                    creditSelect.getRemboursements().set(idx, echeancierSelect);
                }
                idx = credits.indexOf(creditSelect);
                if (idx > -1) {
                    credits.set(idx, creditSelect);
                    update("data_credit");
                }
                update("date_mensualite_echeancier");
                update("date_mensualite_echeancier_");
                update("tabview_credit:data_echeancier");
                return true;
            }
            return false;
        } catch (Exception ex) {
            Logger.getLogger(ManagedCredit.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean controleFicheReglementCredit(ReglementCreditMut bean) {
        if (credit.getId() <= 0) {
            getErrorMessage("Vous n'avez selectionné aucun crédit !");
            return false;
        }
        if (bean.getMontant() <= 0) {
            getErrorMessage("Vous devez entrer le montant");
            return false;
        }
        if (bean.getMontant() > credit.getMontantReste()) {
            getErrorMessage("Vous ne pouvez pas enregistrer ce montant");
            return false;
        }
        if (!credit.getEtatValidation().equals(Constantes.ETAT_VALIDE)) {
            getErrorMessage("Ce crédit n'est pas encore validé");
            return false;
        }
        if (credit.getStatutPaiement() == Constantes.STATUT_DOC_PAYER) {
            getErrorMessage("Ce crédit est déja payé");
            return false;
        }
        if (bean.getModePaiement().equals(Constantes.MUT_MODE_PAIEMENT_ESPECE)) {
            if (bean.getCaisse() != null ? bean.getCaisse().getId() < 1 : true) {
                getErrorMessage("Vous devez precisez la caisse");
                return false;
            }
        } else {
            if (bean.getCompte() != null ? bean.getCompte().getId() < 1 : true) {
                getErrorMessage("Vous devez precisez le compte de destination");
                return false;
            }
        }
        return true;
    }

    public boolean saveNewReglementCredit() {
        try {
            if (controleFicheReglementCredit(reglementc)) {
                if (reglementc.getModePaiement().equals(Constantes.MUT_MODE_PAIEMENT_ESPECE)) {
                    reglementc.setCompte(new Compte());
                } else {
                    reglementc.setCaisse(new CaisseMutuelle());
                }
                YvsMutReglementCredit entity = UtilMut.buildReglementCredit(reglementc, new YvsMutCredit(credit.getId()), currentUser);
                if (saveNewReglementCredit(entity)) {
                    succes();
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            Logger.getLogger(ManagedCredit.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean saveNewReglementCredit(double montant) {
        reglementc.setMontant(montant);
        saveNewReglementCredit();
        return true;
    }

    public boolean saveNewReglementCredit(YvsMutReglementCredit entity) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                dao.update(entity);
            } else {
                entity.setId(null);
                entity = (YvsMutReglementCredit) dao.save1(entity);
                reglementc.setId(entity.getId());
            }
            int idx = credit.getReglements().indexOf(entity);
            if (idx > -1) {
                credit.getReglements().set(idx, entity);
            } else {
                credit.getReglements().add(0, entity);
            }
            List<YvsMutReglementCredit> temps = new ArrayList<>(credit.getReglements());
            if (credit.getMontantReste() <= 0) {
                creditSelect.setAuthor(currentUser);
                creditSelect.setDateUpdate(new Date());
                creditSelect.setStatutPaiement(Constantes.STATUT_DOC_PAYER);
                creditSelect.getReglements().clear();
                dao.update(creditSelect);
                credit.setStatutPaiement(Constantes.STATUT_DOC_PAYER);
            } else if (creditSelect.getMontant() > creditSelect.getMontantVerse()) {
                creditSelect.setStatutPaiement(Constantes.STATUT_DOC_ATTENTE);
                creditSelect.getReglements().clear();
                creditSelect.setAuthor(currentUser);
                creditSelect.setDateUpdate(new Date());
                dao.update(creditSelect);
                credit.setStatutPaiement(Constantes.STATUT_DOC_ATTENTE);
            }
            credit.setReglements(new ArrayList<YvsMutReglementCredit>(temps));
            creditSelect.setReglements(new ArrayList<YvsMutReglementCredit>(temps));
            temps = null;
            idx = credits.indexOf(creditSelect);
            if (idx > -1) {
                credits.set(idx, creditSelect);
            }
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ManagedCredit.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    Condition activeCondition;

    public void askActiveCondition(YvsMutConditionCredit bean) {
        activeCondition = buildCondition(bean);
    }

    public void buildForContrainte(YvsMutCredit y) {
        y = (YvsMutCredit) dao.loadOneByNameQueries("YvsMutCredit.findById", new String[]{"id"}, new Object[]{y.getId()});
        creditSelect = y;
        credit = UtilMut.buildBeanCredit(y);
        credit.setNombreVoteRequis((long) dao.loadObjectByNameQueries("YvsMutPosteEmploye.findSizeCanVote", new String[]{"mutuelle"}, new Object[]{currentMutuel}));
    }

    public void activeConditionCredit() {
        if (activeCondition != null && commentaire != null) {
            if (commentaire.trim().length() > 2) {
                activeCondition.setCommentaire(commentaire);
                YvsMutConditionCredit c = buildCondition(activeCondition);
                c.setDateUpdate(new Date());
                c.setAuthor(currentUser);
                c.setCredit(new YvsMutCredit(creditSelect.getId()));
                activeCondition.setCorrect(!activeCondition.isCorrect());
                c.setCorrect(activeCondition.isCorrect());
                if (c.getId() > 0) {
                    dao.update(c);
                }
                int idx = credit.getConditions().indexOf(c);
                if (idx > -1) {
                    credit.getConditions().get(idx).setCorrect(activeCondition.isCorrect());
                }
                idx = creditSelect.getConditions().indexOf(c);
                if (idx > -1) {
                    creditSelect.getConditions().get(idx).setCorrect(activeCondition.isCorrect());
//                    idx=credits.indexOf(creditSelect);
//                    if(idx>=0){
//                        credits.get(idx).setConditions(null);
//                    }
                }
                if (changeStatut(Constantes.ETAT_ENCOURS)) {
                    succes();
                }
                update("data_credit_condition_");
                update("data_credit_condition");
            } else {
                getErrorMessage("Vous devez préciser la raison de cette validation !");
            }
        } else if (commentaire == null) {
            getErrorMessage("Vous devez préciser la raison de cette validation !");
        }
    }

//    private List<YvsMutMensualite> loadMensualiteEncours(YvsMutMutualiste m) {
//        champ = new String[]{"mutualiste", "date", "idCredit"};
//        val = new Object[]{new YvsMutMutualiste(m.getId()), credit.getDateEffet(), credit.getId()};
//        List<YvsMutMensualite> l = new ArrayList<>();
//        /*recherche les écheancier en cours des credits non encore réglé et dont la date d'effet est supériere ou egale à la d"effet du crédit en cours
//         à l'exception du crédit en cours si on est en modification*/
//        List<YvsMutEchellonage> lr = dao.loadListTableByNameQueries("YvsMutEchellonage.findEncours", champ, val);
//        for (YvsMutEchellonage e : lr) {
//            if (!e.getMensualites().isEmpty()) {
//                l.add(e.getMensualites().get(0));
//            }
//        }
//        return l;
//    }
    public void chooseCaisse(boolean credit) {
        if (credit) {
            if (reglementc.getCaisse() != null ? reglementc.getCaisse().getId() > 0 : false) {
                int idx = caisses.indexOf(new YvsMutCaisse(reglementc.getCaisse().getId()));
                if (idx > -1) {
                    reglementc.setCaisse(UtilMut.buildBeanCaisse(caisses.get(idx)));
                }
            }
        } else {
            if (reglement.getCaisse() != null ? reglement.getCaisse().getId() > 0 : false) {
                int idx = caisses.indexOf(new YvsMutCaisse(reglement.getCaisse().getId()));
                if (idx > -1) {
                    reglement.setCaisse(UtilMut.buildBeanCaisse(caisses.get(idx)));
                }
            }
        }
    }

    private double calculMensualite(double capital, YvsMutTypeCredit t, int duree, int periode, int periodicite) {
        double taux = t.getTauxMaximal();
        return calculMensualite(capital, taux, duree, periode, periodicite, t.getTypeMensualite());
    }

    private double calculMensualite(double capital, double taux, int duree, int periode, int periodicite, char type) {
        double mensualite = 0;
        if (taux != 0) {
            taux = (taux / 100);
            switch (type) {
                case Constantes.TYPE_MENSUALITE_ANNUITE_FIXE:
                    mensualite = (capital * taux) / (1 - (Math.pow((1 + taux), -duree)));
                    break;
                case Constantes.TYPE_MENSUALITE_AMORTISSEMENT_FIXE:
                    mensualite = (capital / duree) * (1 + (taux * (duree - periode + 1)));
                    break;
                default:
                    mensualite = capital / duree;
                    break;
            }
        } else {
            mensualite = capital / duree;
        }
        return Constantes.arrondi(mensualite, parametreMutuelle.getArrondiValeur());
    }
// traiter l'report des mensualités

    public void anticipeMensualite() {
        List<YvsMutMensualite> listAnticipation = giveSelectionMensualite();
        if (!listAnticipation.isEmpty()) {
            //On créée un nouveau plan de règlement (Echelonage)
//            List<Echellonage> lm = credits.get(indexCreditSelectionne).getEcheanciers();
            Echellonage currentE = credit.getEcheancier();
            if (currentE != null) {
                //crée le nouveau echellonage 
                YvsMutEchellonage newE = buildEchellonage(currentE);
                newE.setActif(true);
                newE.setDureeEcheance(currentE.getDureeEcheance() - (listAnticipation.size()));//modifie la duré des mensualité               
                newE.setId(null);
                newE.setMontantVerse(amortissementA + forcage + interetA);
                newE.setMontant(newE.getMontant() - interetRestant + forcage + interetA);
                changeStateEchelonage(credit.getEcheancier().getIdEch());
                newE = (YvsMutEchellonage) dao.save1(newE);
//                Collections.sort(listAnticipation, new Mensualite());
                Collections.sort(currentE.getMensualites(), new YvsMutMensualite());
                //divise la liste en deux
                List<YvsMutMensualite> l1 = new ArrayList<>(), l2 = new ArrayList<>();
//                boolean after = false;
                int indexMiddle = currentE.getMensualites().indexOf(listAnticipation.get(0));
                if (indexMiddle >= 0 && (indexMiddle + 1) < currentE.getMensualites().size()) {
                    l1 = currentE.getMensualites().subList(indexMiddle + 1, currentE.getMensualites().size() - 1);
                }
                if (listAnticipation.size() < currentE.getMensualites().size()) {
                    l2 = currentE.getMensualites().subList((listAnticipation.size()), currentE.getMensualites().size() - 1);
                }
                //recplace les date de l2
                for (YvsMutMensualite m : l1) {
                    saveMensualite(UtilMut.buildBeanMensualite(m), newE);
                }
                //save la mensualité du mois anticipé
                YvsMutMensualite enM = listAnticipation.get(0);
                enM.setEchellonage(newE);
                enM.setMontant(amortissementA + interetA + forcage);
                enM.setInteret(interetA + forcage);
                enM.setId(null);
                enM = (YvsMutMensualite) dao.save1(enM);
                //enregistre le règlement de cette mensualité
                YvsMutReglementMensualite rr = new YvsMutReglementMensualite();
                rr.setDateReglement(new Date());
                rr.setMensualite(enM);
                rr.setMontant(enM.getMontant());
                rr.setId(null);
                dao.save(rr);
                //enregistre la deuxième partie de la liste
                Calendar c_ = Calendar.getInstance();
                c_.setTime(listAnticipation.get(0).getDateMensualite());
                for (YvsMutMensualite m : l2) {
                    c_.add(Calendar.MONTH, credit.getEcheancier().getEcartMensualite());
                    m.setDateMensualite(c_.getTime());
                    saveMensualite(UtilMut.buildBeanMensualite(m), newE);
                }
            }
            succes();
        } else {
            getErrorMessage("Vous n'avez selectionné ausune mesnsualité !");
        }
    }

    public void appliquerAnticipation() {
        try {
            if (listeMensualiteAPayer != null) {
                int idx = caisses.indexOf(new YvsMutCaisse(caisse));
                if (caisse < 1 || idx < 0) {
                    getErrorMessage("Vous devez selectionner la caisse");
                    return;
                }
                YvsMutCaisse c = caisses.get(idx);
                List<YvsMutMensualite> list = new ArrayList<>();
                for (YvsMutMensualite m : listeMensualiteAPayer) {
                    m.setDateUpdate(new Date());
                    m.setAuthor(currentUser);
                    if (m.isMarquer()) {
                        m.setCommentaire("Mensualité anticipée");
                        dao.update(m);
                        idx = credit.getEcheancier().getMensualites().indexOf(m);
                        if (idx > -1) {
                            credit.getEcheancier().getMensualites().set(idx, m);
                        } else {
                            credit.getEcheancier().getMensualites().add(m);
                        }
                        list.add(m);
                    }
                }
                if (!list.isEmpty()) {
                    Collections.sort(list, new YvsMutMensualite());

                    ReglementMensualite bean = new ReglementMensualite();
                    bean.setDateReglement(new Date());
                    bean.setMontant(amortissementA);
                    bean.setCaisse(UtilMut.buildBeanCaisse(c));
                    bean.setReglerPar(currentUser.getUsers().getNomUsers());
                    saveNewReglementMensualite(list.get(0), bean, list.size() > 1 ? list.subList(1, list.size()) : null);
                    succes();
                    update("date_mensualite_echeancier");
                    closeDialog("dlgAnticipation");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Anticipation impossible");
            Logger.getLogger(ManagedCredit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void changeStateEchelonage(long idE) {
        String rq = "UPDATE yvs_mut_echellonage SET etat = '" + Constantes.ETAT_SOUMIS + "' WHERE id = ?";
        Options[] param = new Options[]{new Options(idE, 1)};
        dao.requeteLibre(rq, param);
    }

    private void saveMensualite(Mensualite m, YvsMutEchellonage e) {
        YvsMutMensualite entitymM = buildMensualite(m, e);
        entitymM.setId(null);
        entitymM = (YvsMutMensualite) dao.save1(entitymM);
        if (!m.getReglements().isEmpty()) {
            for (YvsMutReglementMensualite r : m.getReglements()) {
                r.setMensualite(entitymM);
                r.setId(null);
                dao.save(r);
            }
        }
    }

    private boolean controleCoherencePeriodicite(List<Mensualite> l) {
        boolean coherent = true;
        Calendar c = Calendar.getInstance();
        int num = l.size();
        if (num > 0) {
            Mensualite m1 = l.get(0);
            c.setTime(m1.getDateMensualite());
            l.remove(0);
            for (Mensualite m : l) {
                c.add(Calendar.MONTH, credit.getEcheancier().getEcartMensualite());
                if (!c.getTime().equals(m.getDateMensualite())) {
                    coherent = false;
                    break;
                } else {
                    c.setTime(m.getDateMensualite());
                }
            }
        } else {
            getErrorMessage("Vous n'avez selectionné ausune mesnsualité !");
            coherent = false;

        }
        return coherent;
    }

    private List<YvsMutMensualite> giveSelectionMensualite() {
        List<YvsMutMensualite> l = new ArrayList<>();
        for (YvsMutMensualite m : lisAnticipation) {
            if (m.isSelectActif()) {
                l.add(m);
            }
        }
//        if (chaineSelectionMensualite != null) {
//            String numroLine[] = chaineSelectionMensualite.split("-");
//            try {
//                int index;
//                for (String numroLine1 : numroLine) {
//                    index = Integer.parseInt(numroLine1);
//                    l.add(credit.getEcheancier().getMensualites().get(index));
//                }
//                chaineSelectionMensualite = "";
//                succes();
//            } catch (NumberFormatException ex) {
//                chaineSelectionMensualite = "";
//                for (String numroLine1 : numroLine) {
//                    if (!l.contains(numroLine1)) {
//                        chaineSelectionMensualite += numroLine1 + "-";
//                    }
//                }
//                getErrorMessage("Impossible de terminer cette opération !", "des élément de votre sélection doivent être encore en liaison");
//            }
//        }
        return l;
    }

//    public void selectionLineAnticipation() {
//        String idLineSelect = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("line");
//        String[] t_ = idLineSelect.trim().split("-");
//        int num = 0;
//        for (String s_ : t_) {
//            if (!s_.trim().equals("")) {
//                num = Integer.parseInt(s_);
//            }
//        }
//        traiteSelectMensualitet(num);
//
//    }
//    private void traiteSelectMensualitet(int num) {
//        List<YvsMutMensualite> removeLines = new ArrayList<>();
//        int size = lisAnticipation.size();
//        YvsMutMensualite m;
//        if (size > num) { //la liste doit contenir l'index de l'élément selectioné
//            m = lisAnticipation.get(num);
//            if (m.isSelectActif()) {  //si m.selectActif est true c'est qu'on est en train de déselectioner la ligne
//                if ((size - 1) != num) //s'il y a des élément à la suite de cette ligne
//                {
//                    for (int i = num + 1; i < size; i++) {
//                        m.setSelectActif(false);
//                        m = lisAnticipation.get(i);
//                        removeLines.add(m);
//                        if (i < size - 1) {
//                            amortissementA -= lisAnticipation.get(i).getAmortissement();
//                            interetRestant -= lisAnticipation.get(i).getInteret();
//                        }
//                    }
//                } else {
//                    m.setSelectActif(false);
//                }
//                amortissementA -= lisAnticipation.get(num).getAmortissement();
//                interetRestant -= lisAnticipation.get(num).getInteret();
//                double baseF = (parametreMutuelle.getBaseForcage().equals(Constantes.MUT_BASE_FORCAGE_AMORTISSEMENT)) ? amortissementA : interetRestant;
//                forcage = baseF * parametreMutuelle.getPourcentageForcage() / 100;
//            } else {
//                if (listeMensualiteAPayer.size() > num + 1) {
//                    YvsMutMensualite mm = listeMensualiteAPayer.get(num + 1);
//                    if (!lisAnticipation.contains(mm) && !mm.getEtat().equals(Constantes.ETAT_REGLE)) {
//                        amortissementA += lisAnticipation.get(num).getAmortissement();
//                        interetRestant += lisAnticipation.get(num).getInteret();
//                        interetA = lisAnticipation.get(0).getInteret();
//                        double baseF = (parametreMutuelle.getBaseForcage().equals(Constantes.MUT_BASE_FORCAGE_AMORTISSEMENT)) ? amortissementA : interetRestant;
//                        forcage = baseF * parametreMutuelle.getPourcentageForcage() / 100;
//                        lisAnticipation.add(mm);
//                    } else {
//                        getErrorMessage("Impossible de continuer !");
//                    }
//                }
//                lisAnticipation.get(num).setSelectActif(true);
//            }
//            lisAnticipation.removeAll(removeLines);
//        }
//        update("listMensualiteImpaye");
//        update("FormMensualiteImpaye");
//        update("footerTextAnticipation");
//
//    }
    public void suspendreEcheance(YvsMutEchellonage y) {
        suspendreEcheance(y, creditSelect);
    }

    public void suspendreEcheance(YvsMutEchellonage y, YvsMutCredit creditSelect) {
        if (y != null ? y.getId() > 0 : false) {
            if (creditSelect != null ? creditSelect.getId() > 0 : false) {
                if (!creditSelect.getEtat().equals(Constantes.ETAT_VALIDE)) {
                    getErrorMessage("Vous ne pouvez pas suspendre cet écheancier.. car le crédit n'est pas validé");
                    return;
                }
                y.setEtat(Constantes.ETAT_SUSPENDU);
                y.setActif(false);
                dao.update(y);
                succes();
            }
        }
    }

    public void activeEcheance(YvsMutEchellonage y) {
        activeEcheance(y, creditSelect);
    }

    public void activeEcheance(YvsMutEchellonage y, YvsMutCredit creditSelect) {
        if (y != null ? y.getId() > 0 : false) {
            if (creditSelect != null ? creditSelect.getId() > 0 : false) {
                y.setEtat(creditSelect.getEtat().equals(Constantes.ETAT_VALIDE) ? y.getMontantVerse() > 0 ? Constantes.ETAT_ENCOURS : Constantes.ETAT_ATTENTE : Constantes.ETAT_ATTENTE);
                y.setActif(true);
                dao.update(y);
                succes();
            }
        }
    }

    public boolean onActiveEcheance(YvsMutEchellonage y) {
        return onActiveEcheance(y, creditSelect);
    }

    public boolean onActiveEcheance(YvsMutEchellonage y, YvsMutCredit creditSelect) {
        if (y != null ? y.getId() > 0 : false) {
            if (!y.getActif()) {
                if (creditSelect != null ? creditSelect.getId() > 0 : false) {
                    YvsMutEchellonage e = (YvsMutEchellonage) dao.loadOneByNameQueries("YvsMutEchellonage.findByCredit", new String[]{"credit"}, new Object[]{creditSelect});
                    return e != null ? e.equals(y) : false;
                }
            }
        }
        return false;
    }

    public void onReecheancement() {
        echeancierSelect.setMensualites(dao.loadNameQueries("YvsMutMensualite.findActifByEcheancier", new String[]{"echellonage"}, new Object[]{echeancierSelect}));
        onReecheancement(echeancierSelect);
    }

    public void onReecheancement(YvsMutEchellonage y) {
        if (onReecheancement(y, creditSelect)) {
            echeancierSelect = y;
            populateViewEcheancier(UtilMut.buildBeanEchellonage(y));
            update("blog_reechellonage");
        }
    }

    public boolean onReecheancement(YvsMutEchellonage y, YvsMutCredit creditSelect) {
        if (creditSelect != null ? creditSelect.getId() > 0 : false) {
            if (!creditSelect.getEtat().equals(Constantes.ETAT_VALIDE)) {
                getErrorMessage("Ce crédit n'est pas encore validé");
                return false;
            }
            if (creditSelect.getStatutCredit().equals(Constantes.STATUT_DOC_PAYER)) {
                getErrorMessage("Ce crédit est dèja totalement remboursé");
                return false;
            }
            if (creditSelect.getType() != null ? creditSelect.getType().getReechellonagePossible() : false) {
                if ((y != null) ? y.getId() != 0 : false) {
                    if (y.getActif()) {
                        if (y.getMontantReste() <= 0 || y.getEtat().equals(Constantes.ETAT_REGLE)) {
                            getErrorMessage("Cet écheancier est déjà réglé");
                            return false;
                        }
                        openDialog("dlgReechellonage");
                        return true;
                    } else {
                        getErrorMessage("Cette échéancier n'est plus actif !");
                    }
                }
            } else {
                getErrorMessage("Ce type de crédit ne permet pas les rééchellonements d'écheance");
            }
        }
        return false;
    }

    public void openViewReport(YvsMutMensualite y) {
        if (y != null ? creditSelect != null : false) {
            openViewReport(y, creditSelect, echeancierSelect);
            update("date_mensualite_echeancier0_");
            update("blog_form_reechellonage");
        }
    }

    public void openViewReport(YvsMutMensualite y, YvsMutCredit creditSelect, YvsMutEchellonage echeancierSelect) {
        if (y != null ? creditSelect != null : false) {
            if (!creditSelect.getType().getSuspensionPossible()) {
                getErrorMessage("Impossible de reporter cette mensualité...car le credit ne peut être reporté");
                return;
            }
            if (y.getEtat().equals(Constantes.ETAT_ENCOURS)) {
                getErrorMessage("Cette mensualité est en cours de reglement");
                return;
            }
            if (y.getEtat().equals(Constantes.ETAT_REGLE)) {
                getErrorMessage("Cette mensualité est déjà reglée");
                return;
            }
            if (y.getEtat().equals(Constantes.ETAT_SUSPENDU)) {
                getErrorMessage("Cette mensualité est suspendue");
                return;
            }
            report = new Reechellonage();
            report.setDateEcheance(y.getDateMensualite());
            report.setNombreEcheance(1);
            report.setMensualites(dao.loadNameQueries("YvsMutMensualite.findActifByEcheancier", new String[]{"echellonage"}, new Object[]{echeancierSelect}));
            openDialog("dlgAvancement");
        }
    }

    public void openViewReport() {
        openViewReport(mensualiteSelect);
    }

//    public void deplacerMensualite() {
//        int init = credit.getEcheancier().getMensualites().indexOf(mensualiteSelect);
//        mensualiteSelect.setDateMensualite(dateDeplace);
//        mensualiteSelect.setNew_(true);
//        YvsMutMensualite entity = buildMensualite(mensualiteSelect, entityEcheancier);
//        dao.update(entity);
//        credit.getEcheancier().getMensualites().set(init, mensualiteSelect);
//        if (init < credit.getEcheancier().getMensualites().size()) {
//            Mensualite m1 = credit.getEcheancier().getMensualites().get(init + 1);
//            if (!Util.dateToCalendar(mensualiteSelect.getDateMensualite()).before(Util.dateToCalendar(m1.getDateMensualite()))) {
//                for (int i = init + 1; i < credit.getEcheancier().getMensualites().size(); i++) {
//                    Mensualite bean = credit.getEcheancier().getMensualites().get(i);
//                    Calendar cal = Util.dateToCalendar(bean.getDateMensualite());
//                    cal.add(Calendar.MONTH, 1);
//                    bean.setDateMensualite(cal.getTime());
//                    bean.setNew_(true);
//                    entity = buildMensualite(bean, entityEcheancier);
//                    dao.update(entity);
//                    credit.getEcheancier().getMensualites().set(i, bean);
//                }
//            }
//        }
//        closeDialog("dlgAvanceMensualite");
//        update("date_mensualite_echeancier_");
//        update("date_mensualite_echeancier");
//    }
    /*Reporter des echéances*/
    //Deplacement de la mensualite
//    public void confirmDeplacement() {
//        if ((mensualiteSelect != null) ? mensualiteSelect.getId() != 0 : false) {
//            if (dateDeplace != null) {
//                //recuperation de l'index de la mensualité selectionnée
//                int init = credit.getEcheancier().getMensualites().indexOf(mensualiteSelect);
//                //Teste si la date entré est superieur a celle de la  mensualité
//                if (Util.dateToCalendar(mensualiteSelect.getDateMensualite()).before(Util.dateToCalendar(dateDeplace))) {
//                    if (init < credit.getEcheancier().getMensualites().size()) {
//                        Mensualite m1 = credit.getEcheancier().getMensualites().get(init + 1);
//                        if (!mensualiteSelect.getEtat().equals(Constantes.ETAT_REGLE)) { //si la mensualité sélectionné n'est pas encore réglé
//                            if (Util.dateToCalendar(dateDeplace).before(Util.dateToCalendar(m1.getDateMensualite()))) {
//                                deplacerMensualite();
//                            } else {
//                                openDialog("dlgConfirmDeplacement");
//                            }
//                        } else {
//                            getErrorMessage("Vous ne pouvez déplacer un crédit déjà réglé !");
//                        }
//                    }
//                } else {
//                    getWarningMessage("Vous devez entrer une date supérieure a celle de la mensualité");
//                }
//            } else {
//                getWarningMessage("Vous devez entrer une date");
//            }
//        } else {
//            getWarningMessage("Mensualité invalide");
//        }
//    }
    private boolean controleFormReport(Reechellonage r) {
        //le nombre de mensualité à reporter doit être inférieure ou égale à 12  et au nombre de mensualité non réglé restant
        if (r.getNombreEcheance() > r.getMensualites().size()) {
            getErrorMessage("Le nombre de mensualité ne doit pas dépasser le total des mensualités restante !");
            return false;
        }
        if (r.getTypeReport().equals(Constantes.MUT_TYPE_REPORT_TOTAL)) {
            if (r.getDureeSuspension() < 1) {
                getErrorMessage("Vous devez entrer la durée de suspension");
                return false;
            }
        }
//la date de début de la mensualité doit correspondre à l'une des date de mensualité non réglé
        if (r.getDateEcheance() != null) {
            int n = 0;
            for (YvsMutMensualite m : r.getMensualites()) {
                if (m.getDateMensualite().equals(r.getDateEcheance())) {
                    if (r.getNombreEcheance() > (r.getMensualites().size() - n)) {
                        getErrorMessage("Le nombre de mensualité à reporter ne coïncide pas avec la date de début !");
                        return false;
                    }
                    return true;
                }
                n++;
            }
            getErrorMessage("La date de début du report doit coïncider avec l'une des mensualité en cours");
            return false;
        } else {
            getErrorMessage("Vous devez préciser la date de début du report !");
        }
        return true;
    }
    private double capitalRestant = 0;
    private double interetPeriode = 0;
    private double amortissementPeriode = 0;

    public void reportEcheances() {
        reportEcheances(creditSelect, credit.getEcheancier());
    }

    public void reportEcheances(YvsMutCredit creditSelect, Echellonage echeancier) {
        YvsMutTypeCredit typ = creditSelect.getType();
        if (typ != null ? typ.getId() > 0 : false) {
            if (controleFormReport(report)) {
                if (report.getMensualites() != null ? !report.getMensualites().isEmpty() : false) {
                    int n = report.getNombreEcheance();
                    double taux_penalite = typ.getTauxPenaliteSuspension();
                    List<YvsMutMensualite> list = new ArrayList<>();
                    int size = report.getMensualites().size();
                    if (report.getTypeReport().equals(Constantes.MUT_TYPE_REPORT_PARTIEL)) {
                        for (YvsMutMensualite m : report.getMensualites()) {
                            if (m.getDateMensualite().after(report.getDateEcheance()) || m.getDateMensualite().equals(report.getDateEcheance())) {
                                //Aucune mensualité participant au calcul du nouvel echéancier ne doit être déjà réglé
                                if (m.getEtat().equals(Constantes.ETAT_REGLE)) {
                                    getErrorMessage("L'intervale de repport est incorrecte. il contient des mensualités déjà réglé !");
                                    return;
                                }
                                if (n > 0) {
                                    list.add(new YvsMutMensualite((long) -(list.size() + 1), m));
                                    m.setInteret(0.0);
                                    m.setAmortissement(0.0);
                                    m.setMontant(0);
                                    m.setMarquer(true);
                                } else {
                                    break;
                                }
                                n--;
                            }
                        }
                        YvsMutMensualite last = report.getMensualites().get(report.getMensualites().size() - 1);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(last.getDateMensualite());
                        for (YvsMutMensualite y : list) {
                            cal.add(Calendar.MONTH, credit.getEcheancier().getEcartMensualite());
                            double penalite = 0;
                            if (typ.getPenaliteSuspension()) {
                                switch (typ.getNaturePenaliteSuspension()) {
                                    case Constantes.NATURE_MONTANT: {
                                        penalite = taux_penalite;
                                        break;
                                    }
                                    default: {
                                        switch (typ.getBasePenaliteSuspension()) {
                                            case Constantes.PENALITE_BASE_INTERET: {
                                                penalite = y.getInteret() * taux_penalite / 100;
                                                break;
                                            }
                                            default: {
                                                penalite = y.getMontant() * taux_penalite / 100;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            y.setMontantPenalite(penalite);
                            y.setDateMensualite(cal.getTime());
                            report.getMensualites().add(y);
                        }
                    } else {
                        YvsMutMensualite m;
                        list.addAll(report.getMensualites());

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(report.getDateEcheance());
                        cal.add(Calendar.MONTH, report.getDureeSuspension() - 1);
                        Date dateFin = cal.getTime();

                        for (int i = 0; i < size; i++) {
                            m = list.get(i);
                            if (m.getDateMensualite().after(report.getDateEcheance()) || m.getDateMensualite().equals(report.getDateEcheance())) {
                                //Aucune mensualité participant au calcul du nouvel echéancier ne doit être déjà réglé
                                if (m.getEtat().equals(Constantes.ETAT_REGLE)) {
                                    getErrorMessage("L'intervale de repport est incorrecte. il contient des mensualités déjà réglé !");
                                    return;
                                }
                                if ((m.getDateMensualite().after(report.getDateEcheance()) && m.getDateMensualite().before(dateFin))
                                        || (m.getDateMensualite().equals(dateFin) || m.getDateMensualite().equals(report.getDateEcheance()))) {
                                    YvsMutMensualite y = new YvsMutMensualite((long) -(report.getMensualites().size() + 1), m);
                                    y.setInteret(0.0);
                                    y.setAmortissement(0.0);
                                    y.setMontant(0);
                                    y.setMarquer(true);
                                    report.getMensualites().add(i, y);

                                    double penalite = 0;
                                    if (typ.getPenaliteSuspension()) {
                                        switch (typ.getNaturePenaliteSuspension()) {
                                            case Constantes.NATURE_MONTANT: {
                                                penalite = taux_penalite;
                                                break;
                                            }
                                            default: {
                                                switch (typ.getBasePenaliteSuspension()) {
                                                    case Constantes.PENALITE_BASE_INTERET: {
                                                        penalite = m.getInteret() * taux_penalite / 100;
                                                        break;
                                                    }
                                                    default: {
                                                        penalite = m.getMontant() * taux_penalite / 100;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    penalite = penalite * report.getDureeSuspension();
                                    m.setMontantPenalite(penalite);
                                }
                                cal.setTime(m.getDateMensualite());
                                cal.add(Calendar.MONTH, report.getDureeSuspension() - 1);
                                cal.add(Calendar.MONTH, credit.getEcheancier().getEcartMensualite());

                                m.setDateMensualite(cal.getTime());
                                int idx = report.getMensualites().indexOf(m);
                                if (idx > -1) {
                                    report.getMensualites().set(idx, m);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void cancelEvaluerReport() {
        cancelEvaluerReport(echeancierSelect);
    }

    public void cancelEvaluerReport(YvsMutEchellonage echeancierSelect) {
        report.setMensualites(dao.loadNameQueries("YvsMutMensualite.findActifByEcheancier", new String[]{"echellonage"}, new Object[]{echeancierSelect}));
    }

    public void appliquerReport() {
        if (report != null ? report.getMensualites() != null : false) {
            appliquerReport(credit.getEcheancier());
            update("date_mensualite_echeancier");
        }
    }

    public void appliquerReport(Echellonage echeancier) {
        if (report != null ? report.getMensualites() != null : false) {
            for (YvsMutMensualite m : report.getMensualites()) {
                m.setDateUpdate(new Date());
                m.setAuthor(currentUser);
                if (m.isMarquer()) {
                    m.setEtat(Constantes.ETAT_SUSPENDU);
                    m.setCommentaire("Mensualité reportée");
                    dao.update(m);
                } else {
                    if (m.getId() < 1) {
                        m.setId(null);
                        m.setDateSave(new Date());
                        m = (YvsMutMensualite) dao.save1(m);
                    } else {
                        dao.update(m);
                    }
                }
                int idx = credit.getEcheancier().getMensualites().indexOf(m);
                if (idx > -1) {
                    credit.getEcheancier().getMensualites().set(idx, m);
                } else {
                    credit.getEcheancier().getMensualites().add(m);
                }
            }
            Collections.sort(credit.getEcheancier().getMensualites(), new YvsMutMensualite());
            succes();
        }
    }

    public void calculReportEcheances() {
        if (controleFormReport(report)) {
            List<YvsMutMensualite> listReport = mensualiteAreporter(report);
        }
    }

    private Date giveDateProchaineEcheance() {
        Calendar c = Calendar.getInstance();
        c.setTime(report.getDateEcheance());
        c.add(Calendar.MONTH, credit.getEcheancier().getEcartMensualite() * (report.getNombreEcheance()));
        return c.getTime();
    }

    //récupère les mensualité à reporter (les mensualité contenue dans l'objet réechelonnage doivent être trié)
    private List<YvsMutMensualite> mensualiteAreporter(Reechellonage r) {
        int n = r.getNombreEcheance();
        List<YvsMutMensualite> listeARecalculer = new ArrayList<>();
        amortissementPeriode = interetPeriode = capitalRestant = 0;
        double mens = 0;
        for (YvsMutMensualite m : r.getMensualites()) {
            if (m.getDateMensualite().after(r.getDateEcheance()) || m.getDateMensualite().equals(r.getDateEcheance())) {
                //Aucune mensualité participant au calcul du nouvel echéancier ne doit être déjà réglé
                if (m.getEtat().equals(Constantes.ETAT_REGLE)) {
                    getErrorMessage("L'intervale de repport est incorrecte. il contient des mensualités déjà réglé !");
                    return null;
                }
                if (n > 0) {
                    mens = m.getMontant();
                    interetPeriode += m.getInteret();
                    amortissementPeriode += m.getAmortissement();
                    if (r.getTypeReport().equals(Constantes.MUT_TYPE_REPORT_PARTIEL)) {
                        m.setAmortissement(0.0);
                        m.setMontant(m.getInteret());
                        m.setMontantReste(m.getInteret());
                        m.setMarquer(true);
                    } else {
                        m.setAmortissement(0.0);
                        m.setMontant(0.0);
                        m.setMontantReste(0);
                        m.setMarquer(true);
                    }
                } else {
                    listeARecalculer.add(m);
                    capitalRestant += m.getMontant();
                }
                n--;
            }
        }
        //reéchelonnage contient les mensualité non réglé jusqu'a la mensualité qu'on veut suspendre.  
        if (r.getTypeReport().equals(Constantes.MUT_TYPE_REPORT_PARTIEL)) {
            capitalRestant += amortissementPeriode;
        } else {
            capitalRestant += (amortissementPeriode + interetPeriode);
        }
        double taux = credit.getEcheancier().getTaux();
        try {
            taux = (taux / 100) / (12 / credit.getEcheancier().getEcartMensualite());
        } catch (ArithmeticException e) {
            getErrorMessage("Division par zéro ! vérifié la valeur de la périodicité !");
            return null;
        }
        if (taux != 0) {
            List<YvsMutMensualite> nouveau = recalculEcheance(capitalRestant, mens, taux, giveDateProchaineEcheance());
            r.getMensualites().removeAll(listeARecalculer);
            r.getMensualites().addAll(r.getMensualites().size(), nouveau);
        }
        return r.getMensualites();
    }

    private List<YvsMutMensualite> recalculEcheance(double capital, double mensualite, double taux, Date debut) {
        List<YvsMutMensualite> re = new ArrayList<>();
//calcule le nombre de mensualité
        int n = 0;
        if (mensualite == 0) {
            getErrorMessage("Impossible de continuer la valeur de la mensualité est incorrecte !");
            return re;
        }
        double num = Math.log(1 - ((capital * taux) / mensualite));
        double denom = Math.log(1 + taux);
        double r = 0;
        if (denom != 0) {
            r = -num / denom;
        }
        if (r != Double.NaN) {
            n = (int) Math.round(r);
            if (r > n) {
                n = n + 1;
            }
            YvsMutMensualite me;
            Calendar c = Calendar.getInstance();
            c.setTime(debut);
            int id = -1000;
            while (capital >= mensualite) {
                me = new YvsMutMensualite((long) id);
                me.setInteret(capital * taux);
                me.setAmortissement(mensualite - me.getInteret());
                me.setDateMensualite(c.getTime());
                me.setMontant(mensualite);
                me.setMontantReste(mensualite);
                me.setEtat(Constantes.ETAT_ENCOURS);
                c.add(Calendar.MONTH, credit.getEcheancier().getEcartMensualite());
                re.add(me);
                id++;
                capital = capital - me.getAmortissement();
            }
            if (capital > 0) {
                me = new YvsMutMensualite((long) id);
                me.setInteret(capital * taux);
                me.setAmortissement(capital - me.getInteret());
                me.setDateMensualite(c.getTime());
                me.setMontant(capital);
                me.setMontantReste(capital);
                me.setEtat(Constantes.ETAT_ENCOURS);
                re.add(me);
            }
        }
        return re;
    }

    public void saveNewPlanDeReglement() {
        YvsMutEchellonage enE = buildEchellonage(credit.getEcheancier());
        enE.setEtat(Constantes.ETAT_ENCOURS);
        enE.setActif(true);
        enE.setDureeEcheance(report.getMensualites().size() + 0.0);
        changeStateEchelonage(credit.getEcheancier().getIdEch());
        int idx = creditSelect.getRemboursements().indexOf(new YvsMutEchellonage(credit.getEcheancier().getIdEch()));
        if (idx > -1) {
            creditSelect.getRemboursements().get(idx).setEtat(Constantes.MUT_ETAT_CREDIT_TRANSFERE);
        }
        enE.setDureeEcheance((double) report.getMensualites().size());
        enE.setId(null);
        enE = (YvsMutEchellonage) dao.save1(enE);

        Echellonage newE = recopieViewEchellonage();
        newE.setIdEch(enE.getId());
        YvsMutMensualite enM;
        for (YvsMutMensualite m : report.getMensualites()) {
            enM = m;
            enM.setEchellonage(enE);
            if (m.getMontant() == 0) {
                enM.setEtat(Constantes.ETAT_REGLE);
            }
            enM.setId(null);
            enM = (YvsMutMensualite) dao.save1(enM);
            m.setId(enM.getId());
        }
        //modifie le nombre de mensualité du crédit
        String rq = "UPDATE yvs_mut_credit SET duree=" + report.getMensualites().size() + " WHERE id=" + enE.getCredit().getId();
        dao.requeteLibre(rq, new Options[]{});
        newE.getMensualites().addAll(report.getMensualites());
        creditSelect.getRemboursements().add(0, enE);
        update("blog_echeancier_credit");
        succes();
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.reference", "reference", null, "LIKE", "AND");
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "mutualiste", numSearch, "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.reference)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.compte.mutualiste.employe.matricule)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.compte.mutualiste.employe.nom)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.compte.mutualiste.employe.prenom)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllCredit(true, true);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateCredit", "dates", null, "BETWEEN", "AND");
        if (dateSearch) {
            p = new ParametreRequete("y.dateCredit", "dates", debutSearch, finSearch, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllCredit(true, true);
    }

    public void addParamType() {
        ParametreRequete p = new ParametreRequete("y.type", "type", null, "LIKE", "AND");
        if (typeSearch > 0) {
            p = new ParametreRequete("y.type", "type", new YvsMutTypeCredit(typeSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAllCredit(true, true);
    }

    public void addParamStatut() {
        ParametreRequete p = new ParametreRequete("y.etat", "etat", null, "=", "AND");
        if (statutSearch != null ? statutSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.etat", "etat", statutSearch, "=", "AND");
        }
        paginator.addParam(p);
        loadAllCredit(true, true);
    }

    public void clearParams() {
        idsSearch = "";
        paginator.getParams().clear();
        loadAllCredit(true, true);
    }

    public void voteCredit(boolean approuve) {
        if (credit != null ? credit.getId() > 0 : false) {
            YvsMutMutualiste u = (YvsMutMutualiste) dao.loadOneByNameQueries("YvsMutMutualiste.findByOneUsers", new String[]{"users", "mutuelle"}, new Object[]{currentUser.getUsers(), currentMutuel});
            if (u != null ? u.getId() > 0 : false) {
                //vérifie si le poste mutualiste peut voter
                Boolean canVote;
                YvsMutPosteEmploye poste = (YvsMutPosteEmploye) dao.loadObjectByNameQueries("YvsMutPosteEmploye.findByMutualisteActif", new String[]{"mutualiste"}, new Object[]{u});
//                canVote=poste.getPoste().getCanVoteCredit();
                canVote = (poste != null) ? poste.getPoste() != null ? poste.getPoste().getCanVoteCredit() : false : false;
                if (canVote) {
                    YvsMutVoteValidationCredit y = (YvsMutVoteValidationCredit) dao.loadOneByNameQueries("YvsMutVoteValidationCredit.findByCreditMutualise", new String[]{"credit", "mutualiste"}, new Object[]{new YvsMutCredit(credit.getId()), u});
                    if (y != null ? y.getId() > 0 : false) {
                        y.setDateUpdate(new Date());
                        y.setAuthor(currentUser);
                        y.setAccepte(approuve);
                        y.setDateValidation(new Date());
                        dao.update(y);
                    } else {
                        y = new YvsMutVoteValidationCredit(0, new YvsMutCredit(credit.getId()), u, approuve);
                        y.setDateUpdate(new Date());
                        y.setDateSave(new Date());
                        y.setAuthor(currentUser);
                        y.setId(null);
                        y = (YvsMutVoteValidationCredit) dao.save1(y);
                    }
                    int idx = credit.getVotes().indexOf(y);
                    if (idx > -1) {
                        credit.getVotes().set(idx, y);
                    } else {
                        credit.getVotes().add(0, y);
                    }
                    credit.loadVotes();
                    //contrôle le taux de vote
                    double tauxAccept = (100 * credit.getVotesApprouve().size() / parametreMutuelle.getNombreVoteRequis());
                    if (parametreMutuelle.getTauxVoteValidCreditIncorrect() > tauxAccept) {
                        changeStatut(Constantes.ETAT_ENCOURS);
                    } else if (parametreMutuelle.getTauxVoteValidCreditCorrect() > tauxAccept) {
                        changeStatut(Constantes.ETAT_ENCOURS);
                    }
                    getInfoMessage("Vote prix en compte");
                } else {
                    getErrorMessage("Vous n'êtes pas autorisé à participer au vote");
                }
//                valideCredit();
            } else {
                getErrorMessage("Vous n'êtes pas membre de cette mutuelle");
            }
        }
    }

    private void definedIfVoteVisible() {
        boolean re = false;
        //Definir si le vote est visible
        if (currentMutuel != null) {
            credit.setNombreVoteRequis(parametreMutuelle.getNombreVoteRequis());
            //On verifi le parametre de validation par vote
            re = parametreMutuelle.isValidCreditByVote() && parametreMutuelle.getNombreVoteRequis() > 0 && (parametreMutuelle.getTauxVoteValidCreditIncorrect() > 0 || parametreMutuelle.getTauxVoteValidCreditCorrect() > 0);
            credit.setVisibleVote(re);
        }
    }
//    private Date dateMinEcheance = new Date();

    public void accorderCredit() {
        accorderCredit(creditSelect, credit, false);
    }

    public void reechellonerEcheance() {
        reechellonerEcheance(echeancierSelect, credit, creditSelect);
    }

    public void reechellonerEcheance(YvsMutEchellonage echeancierSelect, Credit credit, YvsMutCredit creditSelect) {
        try {
            if (credit.getEcheancier().getEcartMensualite() <= 0) {
                getErrorMessage("Vous devez entrer une périodicité valide !");
                return;
            }
            if (credit.getEcheancier().getTaux() <= 0) {
                getErrorMessage("Vous devez entrer un taux valide !");
                return;
            }
            if (credit.getEcheancier().getDureeEcheance() <= 0) {
                getErrorMessage("Vous devez entrer une duréé valide !");
                return;
            }
            credit.getEcheancier().setIdEch(0);
            credit.getEcheancier().setEtat(Constantes.ETAT_ATTENTE);
            if (genererEcheance(creditSelect, credit, credit.getEcheancier().getMontantReste(), true, true)) {
                echeancierSelect.setActif(false);
                echeancierSelect.setDateUpdate(new Date());
                echeancierSelect.setAuthor(currentUser);
                dao.update(echeancierSelect);
                closeDialog("dlgReechellonage");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Réechellonage impossible!!!");
            getException("Réechellonage >>>> ", ex);
        }
    }

    public boolean genererEcheance(YvsMutCredit creditSelect, Credit credit, double montant, boolean existe, boolean saveMensualite) {
        try {
            if (credit.getEcheancier().getEcartMensualite() <= 0) {
                getErrorMessage("Vous devez entrer une périodicité valide !");
                return false;
            }
            YvsMutTypeCredit typ = creditSelect.getType();
            if ((typ != null) ? typ.getId() <= 0 : false) {
                YvsMutTypeCredit y = (YvsMutTypeCredit) dao.loadOneByNameQueries("YvsMutTypeCredit.findByDesignation", new String[]{"designation"}, new Object[]{typ.getDesignation()});
                typ = y;
            }
            if ((typ != null) ? typ.getId() > 0 : false) {
                YvsMutGrilleTauxTypeCredit g;
                creditSelect.setType(typ);
                switch (typ.getNatureMontant()) {
                    case Constantes.MUT_TYPE_MONTANT_FIXE:
                        //Permet de rechercher la grille pour le montant du credit
                        if (!typ.getGrilles().isEmpty()) {
                            g = searchTaux(typ, creditSelect.getMontant());
                        } else {
                            g = new YvsMutGrilleTauxTypeCredit();
                            g.setPeriodeMaximal((creditSelect.getDuree() <= 0) ? typ.getPeriodeMaximal() : creditSelect.getDuree());
                            g.setTaux(typ.getTauxMaximal());
                        }
                        return createEcheancier(UtilMut.buildBeanGrilleTaux(g), creditSelect, credit, montant, existe, saveMensualite);
                    case Constantes.MUT_TYPE_MONTANT_POURCENTAGE:
                        //Permet de rechercher le salaire d'un mutualiste
                        double salaire = searchSalaire(creditSelect.getCompte());
                        if (salaire != 0) {
                            double taux = (creditSelect.getMontant() * 100) / salaire; //pourcentage d'endèttement
                            //Permet de rechercher la grille pour le montant du credit
                            if (!typ.getGrilles().isEmpty()) {
                                g = searchTaux(typ, taux);
                            } else {
                                g = new YvsMutGrilleTauxTypeCredit();
                                g.setPeriodeMaximal((creditSelect.getDuree() <= 0) ? typ.getPeriodeMaximal() : creditSelect.getDuree());
                                g.setTaux(typ.getTauxMaximal());
                            }
                            return createEcheancier(UtilMut.buildBeanGrilleTaux(g), creditSelect, credit, montant, existe, saveMensualite);
                        } else {
                            getErrorMessage("Impossible de continuer !", "Certains paramètres requis du mutualiste n'ont pas été trouvés");
                        }
                        break;
                    default:
                        getErrorMessage("Ce type de credit est invalide!");
                        changeStatut(Constantes.ETAT_ATTENTE, "Le type de crédit est invalide...veuillez contacter votre administrateur!");
                        break;
                }
                update("date_mensualite_echeancier");
            } else {
                getErrorMessage("Ce credit est invalide!");
                changeStatut(Constantes.ETAT_ATTENTE, "Le crédit est invalide...veuillez contacter votre administrateur!");
            }
        } catch (Exception ex) {
            getErrorMessage("Le plan de remboursement n'a pas pu être généré !");
            getException("Mutuelle Error>>>> ", ex);
        }
        return false;
    }

    public boolean accorderCredit(YvsMutCredit creditSelect, Credit credit, boolean existe) {
        String etat = Constantes.ETAT_ENCOURS;
        if (parametreMutuelle.isValidCreditByVote() && (parametreMutuelle.getNombreVoteRequis() > 0) && (parametreMutuelle.getTauxVoteValidCreditCorrect() > 0 || parametreMutuelle.getTauxVoteValidCreditIncorrect() > 0)) {
            if (parametreMutuelle.getTauxVoteValidCreditIncorrect() > 0 && (100 * credit.getNombreVoteApprouve() / credit.getNombreVoteRequis() >= parametreMutuelle.getTauxVoteValidCreditIncorrect())) {
                etat = Constantes.ETAT_VALIDE;
            } else if (parametreMutuelle.getTauxVoteValidCreditCorrect() <= 0) {
                etat = Constantes.ETAT_ENCOURS;
            }
            if (parametreMutuelle.getTauxVoteValidCreditCorrect() > 0 && (100 * credit.getNombreVoteApprouve() / credit.getNombreVoteRequis() >= parametreMutuelle.getTauxVoteValidCreditCorrect())) {
                etat = Constantes.ETAT_VALIDE;
            }
        }
        if (changeStatut(credit, etat)) {
            succes();
            return true;
        }
        return false;
    }

    /**
     * Simulateur de mensualités
     */
    private void genereMensualite(YvsMutEchellonage ent, Echellonage echeancier) {
        if (ent != null ? ent.getId() != null ? ent.getId() > 0 : false : false) {
            Echellonage e = UtilMut.buildBeanEchellonage(ent);
            e.setIdEch(ent.getId());
            cloneObject(echeancier, e);
            credit.getEcheancier().getMensualites().clear();
            //Permet de calculer les mensualites de l'echeancier
            int id = -1000;
            for (Mensualite m : calculMensualite(e, UtilMut.buildBeanCredit(creditSelect), creditSelect.getMontant())) {
                YvsMutMensualite y = buildMensualite(id++, m, ent);
                m.setMontantReste(m.getMontant());
                credit.getEcheancier().getMensualites().add(y);
            }
        }
    }

    //Enregistre l'echeancier
    private boolean createEcheancier(GrilleTaux g, YvsMutCredit creditSelect, Credit credit, double montantCredit, boolean existe, boolean saveMensualite) {
        if (g != null) {
            double montant = 0;
            Echellonage e = new Echellonage();
            if (!existe) {
                e = defaultEchellonage(g, 0);
            } else {
                cloneObject(e, credit.getEcheancier());
            }
            YvsMutEchellonage ent = saveNewEchellonage(e, creditSelect);
            if (ent != null) {
                e.setIdEch(ent.getId());
                cloneObject(credit.getEcheancier(), e);
                credit.getEcheancier().getMensualites().clear();
                //Permet de calculer les mensualites de l'echeancier
                List<YvsMutMensualite> list = new ArrayList<>();
                if (saveMensualite) {
                    for (Mensualite m : calculMensualite(e, UtilMut.buildBeanCredit(creditSelect), montantCredit)) {

                        YvsMutMensualite m_ = saveNewMensualite(m, ent);
                        credit.getEcheancier().getMensualites().add(m_);
                        list.add(m_);

                        m.setMontantReste(m.getMontant());
                        montant += m.getMontant();
                    }
                }
                ent.setMontant(montant);
                ent.setDateUpdate(new Date());
                ent.setDateSave(new Date());
                dao.update(ent);
                ent.getMensualites().addAll(list);
                int idx = credit.getEcheanciers().indexOf(ent);
                if (idx > -1) {
                    credit.getEcheanciers().set(idx, ent);
                } else {
                    credit.getEcheanciers().add(ent);
                }
                credit.setEcheancier(UtilMut.buildBeanSimpleEchellonage(ent));
                idx = creditSelect.getRemboursements().indexOf(ent);
                if (idx > -1) {
                    creditSelect.getRemboursements().set(idx, ent);
                } else {
                    creditSelect.getRemboursements().add(ent);
                }
                idx = credits.indexOf(creditSelect);
                if (idx > -1) {
                    credits.set(idx, creditSelect);
                }
                update("tabview_credit:data_echeancier");
                return true;
            } else {
                getErrorMessage("Information sur l'echeancier sont invalide!");
                changeStatut(Constantes.ETAT_ATTENTE, "Les informations sur cet écheancier sont invalides....veuillez contacter votre administrateur!");
            }
        } else {
            getErrorMessage("Ce type de credit n'a pas de grille!");
            changeStatut(Constantes.ETAT_ATTENTE, "Ce type de credit ne posséde pas de grille....veuillez contacter votre administrateur!");
        }
        return false;
    }

//    public void valideCredit() {
//        valideCredit(credit);
//    }
//
//    public void valideCredit(Credit credit) {
//        if (credit != null ? credit.getId() > 0 : false) {
//            if (credit.getEtatValidation().equals(Constantes.ETAT_ANNULE)) {
//                return;
//            }
//            if (credit.getEtatValidation().equals(Constantes.ETAT_ATTENTE)) {
//                return;
//            }
//            boolean by_vote = false;
//            boolean valider = false;
//            boolean condition_correct = true;
//            double tauxValidIfCondCorrect = 0;
//            double tauxValidIfCondIncorect = 0;
//            if (currentMutuel != null) {
//                //On determine si le credit est validée par le taux de vote
//                if (currentMutuel.getParamsMutuelle() != null) {
//                    YvsMutParametre p = currentMutuel.getParamsMutuelle();
//                    if (p.getValidCreditByVote()) {
//                        by_vote = true;
//                        tauxValidIfCondCorrect = p.getTauxVoteValidCreditCorrect();
//                        tauxValidIfCondIncorect = p.getTauxVoteValidCreditIncorrect();
//                    }
//                }
//            }
//            for (YvsMutConditionCredit c : credit.getConditions()) {
//                if (!c.getCorrect()) {
//                    condition_correct = false;
//                    break;
//                }
//            }
//            credit.setNombreVoteRequis(parametreMutuelle.getNombreVoteRequis());
//            long total_vote = credit.getNombreVoteRequis();
//            int total_vote_approuve = credit.getVotesApprouve().size();
//            if (condition_correct) {
//                if (tauxValidIfCondCorrect > 0 && by_vote) {
//                    if (total_vote > 0) {
//                        valider = (tauxValidIfCondCorrect <= ((total_vote_approuve / total_vote) * 100));
//                    } else {
//                        valider = false;
//                    }
//                } else {
//                    valider = true;
//                }
//            } else {
//                if (by_vote && tauxValidIfCondIncorect > 0) {
//                    if (total_vote > 0) {
//                        valider = (tauxValidIfCondIncorect <= ((total_vote_approuve / total_vote) * 100));
//                    } else {
//                        valider = false;
//                    }
//                } else {
//                    valider = false;
//                }
//            }
//            if (valider) {
//                if (credit.getEcheanciers() != null ? credit.getEcheanciers().isEmpty() : true) {
//                    openDialog("dlgEchancier");
//                } else {
//                    if (credit.getEcheancier() != null ? credit.getEcheancier().getMensualites().isEmpty() : false) {
//                        accorderCredit(creditSelect, credit, true);
//                    } else {
//                        changeStatut(credit, Constantes.ETAT_ENCOURS);
//                    }
//                }
//            } else {
////                if (changeStatut(Constantes.ETAT_ENCOURS)) {
////
////                }
//            }
//        }
//
//    }
    public void editableCredit() {
        if (credit != null ? credit.getId() > 0 : false) {
            //un crédit en cours de remboursement ou déjà payé ne peut plus changer de statut
            if (credit.getStatutRemboursement() == Constantes.STATUT_DOC_ENCOUR) {
                getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà en cours de remboursement");
                return;
            }
            if (credit.getStatutRemboursement() == Constantes.STATUT_DOC_PAYER) {
                getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà remboursé");
                return;
            }
            if (credit.getStatutPaiement() == Constantes.STATUT_DOC_ENCOUR) {
                getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà en cours de paiement");
                return;
            }
            if (credit.getStatutPaiement() == Constantes.STATUT_DOC_PAYER) {
                getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà payé");
                return;
            }
            if (credit.getEtatValidation().equals(Constantes.ETAT_VALIDE)) {
                openDialog("dlgConfirmEditbaleValide");
                return;
            }
            if (credit.getEtatValidation().equals(Constantes.ETAT_ANNULE)) {
                openDialog("dlgConfirmEditbaleAnnule");
                return;
            }
            if (changeStatut(Constantes.ETAT_EDITABLE)) {
                succes();
            }
        }
    }

    public void annulerCredit() {
        if (credit != null ? credit.getId() > 0 : false) {
            if (credit.getStatutRemboursement() == Constantes.STATUT_DOC_ENCOUR) {
                getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà en cours de remboursement");
                return;
            }
            if (credit.getStatutRemboursement() == Constantes.STATUT_DOC_PAYER) {
                getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà remboursé");
                return;
            }
            if (credit.getStatutPaiement() == Constantes.STATUT_DOC_ENCOUR) {
                getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà en cours de paiement");
                return;
            }
            if (credit.getStatutPaiement() == Constantes.STATUT_DOC_PAYER) {
                getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà payé");
                return;
            }
            if (credit.getEtatValidation().equals(Constantes.ETAT_VALIDE)) {
                getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà validé");
                return;
            }
            if (credit.getEtatValidation().equals(Constantes.ETAT_ANNULE)) {
                return;
            }
            try {
                changeStatut(Constantes.ETAT_ATTENTE, "Ce crédit a été annulé....veuillez contacter votre administrateur!");
                String rq = "UPDATE yvs_mut_echellonage SET etat = 'W' WHERE credit = ?";
                Options[] param = new Options[]{new Options(creditSelect.getId(), 1)};
                dao.requeteLibre(rq, param);
                succes();
            } catch (Exception ex) {
                Logger.getLogger(ManagedCredit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void soumisCredit() {
        if (credit != null ? credit.getId() > 0 : false) {
            if (credit.getEtatValidation().equals(Constantes.ETAT_EDITABLE)) {
                String etat;
                if (canRunCredit()) {
                    etat = Constantes.ETAT_ENCOURS;
                } else {
                    etat = Constantes.ETAT_SOUMIS;
                }
                if (changeStatut(etat)) {
                    creditSelect.setDateSoumission(new Date());
                    creditSelect.setDateUpdate(new Date());
                    creditSelect.setAuthor(currentUser);
                    dao.update(creditSelect);
                    if (credit.getEcheanciers().isEmpty()) {
                        if (!genererEcheance(creditSelect, credit, credit.getMontant(), false, false)) {
                            getWarningMessage("Le plan de remboursement n'a pas pu être généré !");
                        }
                    }
                }

            }
        } else {
            getErrorMessage("Le credit doit être dans l'Etat Editable !");
        }
    }

    public boolean canAccordeCredit() {
        return canRunCredit() && Constantes.ETAT_ENCOURS.equals(credit.getEtatValidation());
    }

    public boolean canRunCredit() {
        if (credit.getId() > 0) {
            // si toutes les conditions sont rempli
            //Si les condtion ne sont pas rempli et le niveau de vote atteint
            //si le contion sont rempli et le niveau de vote avec conditions rempli est atteint
            boolean display = false;
            boolean conditionOk = true;
            Long nbVoteRequis = parametreMutuelle.getNombreVoteRequis();
            int nbVoteReel = credit.getVotesApprouve().size();
            for (YvsMutConditionCredit c : credit.getConditions()) {
                if (!c.getCorrect()) {
                    conditionOk = false;
                    break;
                }
            }
            if (parametreMutuelle.isValidCreditByVote()) {
                if (parametreMutuelle.getTauxVoteValidCreditIncorrect() > 0 && nbVoteRequis > 0) {
                    display = (100 * nbVoteReel / nbVoteRequis >= parametreMutuelle.getTauxVoteValidCreditIncorrect());
                    if (display) {
                        conditionOk = true; //ne dépend plus des conditions
                    }
                } else if (parametreMutuelle.getTauxVoteValidCreditCorrect() <= 0) {
                    display = true;
                }
                if (parametreMutuelle.getTauxVoteValidCreditCorrect() > 0 && nbVoteRequis > 0) {
                    display = (100 * nbVoteReel / nbVoteRequis >= parametreMutuelle.getTauxVoteValidCreditCorrect());
                }
            } else {
                display = true;
            }
            return conditionOk && display;
        }
        return false;
    }

    public String displayProgressionVote() {
        String re = "";
        boolean conditionOk = true;
        if (credit.getId() > 0) {
            for (YvsMutConditionCredit c : credit.getConditions()) {
                if (!c.getCorrect()) {
                    conditionOk = false;
                    break;
                }
            }
        }
        double taux = (100 * credit.getVotesApprouve().size() / parametreMutuelle.getNombreVoteRequis());
        taux = Constantes.arrondiA2Chiffre(taux);
        if (!conditionOk) {
            if (parametreMutuelle.getTauxVoteValidCreditIncorrect() > 0) {
                return taux + "% Approuvé Sur " + parametreMutuelle.getTauxVoteValidCreditIncorrect() + "% Attendu";
            }
        } else {
            if (parametreMutuelle.getTauxVoteValidCreditCorrect() > 0) {
                return taux + "% Approuvé Sur " + parametreMutuelle.getTauxVoteValidCreditCorrect() + "% Attendu";
            }
        }
        return re;
    }

    public void changeStatut(String statut, String motif) {
        if (credit != null ? credit.getId() > 0 : false) {
            if (statut != null ? statut.trim().length() > 0 : false) {
                String rq = "UPDATE yvs_mut_credit SET etat='" + statut + "', motif_refus = '" + motif + "' WHERE id=?";
                Options[] param = new Options[]{new Options(credit.getId(), 1)};
                dao.requeteLibre(rq, param);
                credit.setEtatValidation(statut);
                credit.setMotifRefus(motif);
                creditSelect.setEtat(statut);
                creditSelect.setMotifRefus(motif);
                int idx = credits.indexOf(creditSelect);
                if (idx >= 0) {
                    credits.set(idx, creditSelect);
                }
                onInValide();
                if (statut.equals(Constantes.ETAT_VALIDE)) {
                    genererEcheance(creditSelect, credit, credit.getMontant(), (credit.getEcheancier().getIdEch() > 0), true);
                }
                update("data_credit");
                update("txt_etat_credit");
            }
        }
    }

    public boolean changeStatut(String statut) {
        return changeStatut(credit, statut);
    }

    public boolean changeStatut(Credit credit, String statut) {
        if (credit != null ? credit.getId() > 0 : false) {
            if (statut != null ? statut.trim().length() > 0 : false) {
                String query = "update yvs_mut_credit set etat = '" + statut + "' where id = ?";
                dao.requeteLibre(query, new Options[]{new Options(credit.getId(), 1)});
                credit.setEtatValidation(statut);
                creditSelect.setEtat(statut);
                int idx = credits.indexOf(creditSelect);
                if (idx > -1) {
//                    credits.get(idx).setEtat(statut);
                    credits.set(idx, creditSelect);
                }
                if (statut.equals(Constantes.ETAT_ATTENTE) || statut.equals(Constantes.ETAT_EDITABLE)) {
                    onInValide();
                }
                if (statut.equals(Constantes.ETAT_VALIDE)) {
                    credit.getEcheancier().setCreditRetainsInteret(parametreMutuelle.isCreditRetainsInteret());
                    if (genererEcheance(creditSelect, credit, credit.getMontant(), (credit.getEcheancier().getIdEch() > 0), true)) {
                        //dépôser le crédit dans le compte du mutualiste
                        reglementc.setModePaiement(Constantes.MUT_MODE_PAIEMENT_COMPTE);
                        reglementc.setMontant(credit.getMontantReste());
                        reglementc.setDateReglement(credit.getDateEffet());
                        reglementc.setCompte(credit.getCompte());
                        update("date_reglementc_credit");
                        saveNewReglementCredit();
                        update("tabview_credit:data_echeancier");
                        update("tabview_credit:date_reglementc_credit");
                    }
                }
                update("data_credit");
                update("txt_etat_credit");
                return true;
            }
        }
        return false;
    }

    public void genereEcheancier() {
        if (credit != null ? credit.getId() > 0 : false) {
            if (credit.getEcheanciers().isEmpty()) {
                if (genererEcheance(creditSelect, credit, credit.getMontant(), false, credit.getEtatValidation().equals(Constantes.ETAT_VALIDE))) {
                    succes();
                }
            }
        } else {
            getErrorMessage("Vous devez entregistrer le crédit!");
        }
    }

    public void regenereEcheancier(YvsMutEchellonage y) {
        try {
            if (credit != null ? credit.getId() > 0 : false) {
                if (credit.getStatutRemboursement() == Constantes.STATUT_DOC_ENCOUR) {
                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà en cours de remboursement");
                    return;
                }
                if (credit.getStatutRemboursement() == Constantes.STATUT_DOC_PAYER) {
                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà remboursé");
                    return;
                }
                if (credit.getStatutPaiement() == Constantes.STATUT_DOC_ENCOUR) {
                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà en cours de paiement");
                    return;
                }
                if (credit.getStatutPaiement() == Constantes.STATUT_DOC_PAYER) {
                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà payé");
                    return;
                }
                if (credit.getEtatValidation().equals(Constantes.ETAT_VALIDE)) {
                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà validé");
                    return;
                }
                if (credit.getEtatValidation().equals(Constantes.ETAT_ANNULE)) {
                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est annulé");
                    return;
                }
                dao.delete(y);
                credit.setEcheancier(UtilMut.buildBeanEchellonage(y));
                if (genererEcheance(creditSelect, credit, credit.getMontant(), true, false)) {
                    succes();
                }
            } else {
                getErrorMessage("Vous devez entregistrer le crédit!");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
        }
    }

    public void deleteEcheancier(YvsMutEchellonage y) {
        try {
            if (credit != null ? credit.getId() > 0 : false) {
                if (credit.getStatutRemboursement() == Constantes.STATUT_DOC_ENCOUR) {
                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà en cours de remboursement");
                    return;
                }
                if (credit.getStatutRemboursement() == Constantes.STATUT_DOC_PAYER) {
                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà remboursé");
                    return;
                }
                if (credit.getStatutPaiement() == Constantes.STATUT_DOC_ENCOUR) {
                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà en cours de paiement");
                    return;
                }
                if (credit.getStatutPaiement() == Constantes.STATUT_DOC_PAYER) {
                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà payé");
                    return;
                }
                if (credit.getEtatValidation().equals(Constantes.ETAT_VALIDE)) {
                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà validé");
                    return;
                }
                if (credit.getEtatValidation().equals(Constantes.ETAT_ANNULE)) {
                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est annulé");
                    return;
                }
                if (y.getMensualites().isEmpty()) {
                    dao.delete(y);
                    credit.getEcheanciers().remove(y);
                    creditSelect.getRemboursements().remove(y);
                    int idx = credits.indexOf(creditSelect);
                    if (idx > -1) {
                        credits.get(idx).getRemboursements().remove(y);
                    }
                    succes();
                } else {
                    getErrorMessage("Cet écheance à déja des mensualités... suspendé le");
                }
            } else {
                getErrorMessage("Vous devez entregistrer le crédit!");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
        }
    }

    /**
     * Si on invalide un ordre de credit déjà validé, Il faut veiller à
     * supprimer les règlment eventuellement effectué dessus les mensualité déjà
     * générée
     */
    private void onInValide() {
        definedIfVoteVisible();
        if (credit != null ? credit.getId() > 0 : false) {
            if (credit.getEtatValidation().equals(Constantes.ETAT_VALIDE)) {
                return;
            }
            if (credit.getStatutPaiement() == Constantes.STATUT_DOC_ATTENTE) {
                String query = "delete from yvs_mut_reglement_credit where credit = ?";
                dao.requeteLibre(query, new Options[]{new Options(credit.getId(), 1)});
                credit.getReglements().clear();
                creditSelect.getReglements().clear();
                update("tabview_credit:data_echeancier");
            }
            if (credit.getStatutRemboursement() == Constantes.STATUT_DOC_ATTENTE) {
                String query = "delete from yvs_mut_mensualite where echellonage = ?";
                dao.requeteLibre(query, new Options[]{new Options(credit.getEcheancier().getIdEch(), 1)});
                credit.getEcheancier().getMensualites().clear();
                if (echeancierSelect != null) {
                    echeancierSelect.getMensualites().clear();
                    int idx = credit.getEcheanciers().indexOf(echeancierSelect);
                    if (idx > -1) {
                        credit.getEcheanciers().set(idx, echeancierSelect);
                    }
                    idx = creditSelect.getRemboursements().indexOf(echeancierSelect);
                    if (idx > -1) {
                        creditSelect.getRemboursements().set(idx, echeancierSelect);
                    }
                }
                update("tabview_credit:data_reglementc_credit");
            }
            int idx = credits.indexOf(creditSelect);
            if (idx > -1) {
                credits.set(idx, creditSelect);
                update("data_credit");
            }
        }
    }

    public void buildForFusion() {
        try {
            if (tabIds != null ? tabIds.trim().length() > 0 : false) {
                String[] ids = tabIds.split("-");
                YvsMutCredit y = null;
                YvsMutCredit m;
                YvsMutCompte compte = null;
                for (String o : ids) {
                    long id = Long.valueOf(o);
                    int idx = credits.indexOf(new YvsMutCredit(id));
                    if (idx > -1) {
                        m = credits.get(idx);
                        if (!m.getType().getFusionPossible()) {
                            getErrorMessage("Vous avez selectionner un crédit qui ne peut etre fusionner");
                            return;
                        }
                        if (!(m.getEtat().equals(Constantes.ETAT_VALIDE) || m.getEtat().equals(Constantes.ETAT_REGLE))) {
                            getErrorMessage("Vous avez selectionner un crédit qui ne sont pas validés");
                            return;
                        }
                        if (compte == null) {
                            compte = m.getCompte();
                        } else {
                            if (!compte.equals(m.getCompte())) {
                                getErrorMessage("Vous essayer de fusionner des credits de compte different!");
                                return;
                            }
                        }
                        YvsMutEchellonage e = (YvsMutEchellonage) dao.loadOneByNameQueries("YvsMutEchellonage.findCurrentByCredit", new String[]{"credit"}, new Object[]{m});
                        if (y == null) {
                            y = new YvsMutCredit(null, m);
                            y.setMontant(e.getMontantReste());
                        } else {
                            y.setMontant(y.getMontant() + e.getMontantReste());
                            if (y.getDateEffet().after(m.getDateEffet())) {
                                y.setDateEffet(m.getDateEffet());
                            }
                        }
                    }
                }
                entityFusion = y;
                update("form_fusion_credit");
                openDialog("dlgFusion");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Fusion impossible !!");
            getException("Error", ex);
        }
    }

    public void fusionnerCredit() {
        try {
            if (entityFusion != null) {
                if (entityFusion.getMontant() < 1) {
                    getErrorMessage("Vous ne pouvez générer un crédit sans montant");
                    return;
                }
                YvsMutTypeCredit typ = null;
                ManagedTypeCredit w = (ManagedTypeCredit) giveManagedBean(ManagedTypeCredit.class);
                if (w != null) {
                    w.getTypeCredit().setByFusion(true);
                    w.getTypeCredit().setCode(w.getTypeCredit().getDesignation());
                    typ = w.saveOne();
                    if (typ != null ? (typ.getId() != null ? typ.getId() > 0 : false) : false) {
                        w.resetFiche();
                    }
                }
                if (typ != null ? (typ.getId() != null ? typ.getId() > 0 : false) : false) {
                    entityFusion.setType(typ);
                    entityFusion.setId(null);
                    entityFusion = (YvsMutCredit) dao.save1(entityFusion);
                    if (entityFusion != null ? (entityFusion.getId() != null ? entityFusion.getId() > 0 : false) : false) {
                        Credit y = UtilMut.buildBeanCredit(entityFusion);
                        if (accorderCredit(entityFusion, y, false)) {
                            closeDialog("dlgFusion");
                            credits.add(entityFusion);
                            update("data_credit");
                        } else {
                            dao.delete(entityFusion);
                            dao.delete(typ);
                        }
                    } else {
                        dao.delete(typ);
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Fusion impossible!!!");
            getException("Error", ex);
        }
    }

    public void resetSimulation() {
        simulerCredit = new Credit();
    }

    public void simulationCredit() {
        if (simulerCredit != null ? simulerEcheance != null : false) {
            simulerCredit(simulerCredit, simulerEcheance.getDateEchellonage(), simulerEcheance.getEcartMensualite());
        }
        update("date_mensualite_simule");
    }

    public void chooseTypeSimuler() {
        if (simulerCredit.getType() != null ? simulerCredit.getType().getId() > 0 : false) {
            ManagedTypeCredit w = (ManagedTypeCredit) giveManagedBean(ManagedTypeCredit.class);
            if (w != null) {
                int idx = w.getTypes().indexOf(new YvsMutTypeCredit(simulerCredit.getType().getId()));
                if (idx > -1) {
                    simulerCredit.setType(UtilMut.buildBeanTypeCredit(w.getTypes().get(idx)));
                }
            }
            if (simulerEcheance.getEcartMensualite() < 1) {
                simulerEcheance.setEcartMensualite(simulerCredit.getType().getPeriodicite());
            }
            if (simulerCredit.getDuree() < 1) {
                simulerCredit.setDuree((int) simulerCredit.getType().getPeriodeMaximal());
            }
            gardeReste = simulerEcheance.getMontant();
            gardeCapital = simulerCredit.getMontant();
            setConditionSimuler();
        }
    }

    public void chooseCompteSimuler(ValueChangeEvent ev) {
        if (ev != null) {
            long id = (long) ev.getNewValue();
            if (id != 0) {
                simulerCredit.setMontantDispo(mutualiste.getMontantTotalEpargne());
                gardeReste = simulerEcheance.getMontant();
                gardeCapital = simulerCredit.getMontant();
            }
        }
    }

    public void setConditionSimuler() {
        setConditionCredit(simulerCredit, true);
        update("data_simuler_credit_condition");
    }

    public void simulerCredit(Credit y, Date date, int ecart) {
        if (ecart <= 0) {
            getErrorMessage("Vous devez entrer une périodicité valide !");
            return;
        }
        YvsMutTypeCredit typ = (YvsMutTypeCredit) dao.loadOneByNameQueries("YvsMutTypeCredit.findById", new String[]{"id"}, new Object[]{y.getType().getId()});
        if ((typ != null) ? typ.getId() != 0 : false) {
            gardeCapital = y.getMontant();
            YvsMutGrilleTauxTypeCredit g = null;
            switch (typ.getNatureMontant()) {
                case Constantes.MUT_TYPE_MONTANT_FIXE:
                    //Permet de rechercher la grille pour le montant du credit
                    if (!typ.getGrilles().isEmpty()) {
                        g = searchTaux(typ, y.getMontant());
                    } else {
                        g = new YvsMutGrilleTauxTypeCredit();
                        g.setPeriodeMaximal((y.getDuree() == 0) ? typ.getPeriodeMaximal() : y.getDuree());
                        g.setTaux(typ.getTauxMaximal());
                    }
                    break;
                case Constantes.MUT_TYPE_MONTANT_POURCENTAGE:
                    //Permet de rechercher le salaire d'un mutualiste
                    double salaire = searchSalaire(new YvsMutCompte(y.getCompte().getId()));
                    if (salaire != 0) {
//                    double montant = salaire * typ.getMontantMaximal() / 100;
                        double taux = (y.getMontant() * 100) / salaire; //pourcentage d'endèttement
                        //Permet de rechercher la grille pour le montant du credit
                        if (!typ.getGrilles().isEmpty()) {
                            g = searchTaux(typ, taux);
                        } else {
                            g = new YvsMutGrilleTauxTypeCredit();
                            g.setPeriodeMaximal((y.getDuree() == 0) ? typ.getPeriodeMaximal() : y.getDuree());
                            g.setTaux(typ.getTauxMaximal());
                        }
                    } else {
                        getErrorMessage("Impossible de continuer !", "Certains paramètres requis du mutualiste n'ont pas été trouvés");
                    }
                    break;
                default:
                    getErrorMessage("Ce type de credit est invalide!");
                    break;
            }
            if (g != null) {
                double montant = 0;
                Echellonage e = defaultEchellonage(UtilMut.buildBeanGrilleTaux(g), 0);
                YvsMutEchellonage ent = buildEchellonage(e);
                if (ent != null) {
                    cloneObject(simulerEcheance, e);
                    y.getMensualites().clear();
                    //Permet de calculer les mensualites de l'echeancier
                    double interet = 0;
                    for (Mensualite m : calculMensualite(e, y, y.getMontant())) {
                        YvsMutMensualite m_ = buildMensualite(m, ent);
                        m.setMontantReste(m.getMontant());
                        y.getMensualites().add(m_);
                        ent.getMensualites().add(m_);
                        montant += m.getMontant();

                        interet += m.getInteret();
                    }
                    ent.setMontant(montant);
                    gardeReste = ent.getMontant();

                    simulerEcheance.setMontant(montant);
                    simulerEcheance.setMontantInteret(interet);
                    ent.setMontantInteret(interet);
                    int idx = y.getEcheanciers().indexOf(ent);
                    if (idx > -1) {
                        y.getEcheanciers().set(idx, ent);
                    } else {
                        y.getEcheanciers().add(ent);
                    }
                    update("tabview_credit:data_echeancier");
                } else {
                    getErrorMessage("Information sur l'echeancier sont invalide!");
                }
            } else {
                getErrorMessage("Ce type de credit n'a pas de grille!");
            }
            update("date_mensualite_echeancier");
        } else {
            getErrorMessage("Ce credit est invalide!");
        }
    }

    public YvsMutCredit equilibre(YvsMutCredit y) {
        return equilibre(y, true);
    }

    public YvsMutCredit equilibre(YvsMutCredit y, boolean msg) {
        if (y != null ? y.getId() > 0 : false) {
            dao.getEquilibreCredit(y.getId());
            y = (YvsMutCredit) dao.loadOneByNameQueries("YvsMutCredit.findById", new String[]{"id"}, new Object[]{y.getId()});
            int idx = credits.indexOf(y);
            if (idx >= 0) {
                credits.set(idx, y);
            }
            if (msg) {
                succes();
            }
        }
        return y;
    }

    public void print(YvsMutEchellonage y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Double ca = dao.loadCaVente(y.getId());
                Map<String, Object> param = new HashMap<>();
                param.put("ID", y.getId().intValue());
                param.put("NAME_AGENCE", currentAgence.getDesignation());
                param.put("NAME_SOCIETE", currentAgence.getSociete().getCodeAbreviation());
                executeReport("echeancier", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedCredit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Date giveNextmensualite(YvsMutCredit cr) {
        if (cr != null) {
            YvsMutEchellonage ech = cr.getEcheancier();
            if (ech != null) {
                Collections.sort(ech.getMensualites(), new YvsMutMensualite());
                for (YvsMutMensualite mens : ech.getMensualites()) {
                    if (mens.getEtat().equals(Constantes.ETAT_REGLE)) {
                        continue;
                    } else {
                        return mens.getDateMensualite();
                    }
                }
            }
        }
        return null;
    }

}
