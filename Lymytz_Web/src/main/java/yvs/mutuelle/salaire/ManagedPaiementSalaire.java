///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.mutuelle.salaire;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//import javax.faces.event.ValueChangeEvent;
//import org.primefaces.event.SelectEvent;
//import org.primefaces.event.UnselectEvent;
//import yvs.dao.Options;
//import yvs.entity.base.YvsBaseExercice;
//import yvs.entity.grh.personnel.YvsGrhContratEmps;
//import yvs.entity.grh.personnel.YvsGrhEmployes;
//import yvs.entity.grh.salaire.YvsGrhBulletins;
//import yvs.entity.grh.salaire.YvsGrhDetailBulletin;
//import yvs.entity.grh.salaire.YvsGrhOrdreCalculSalaire;
//import yvs.entity.mutuelle.base.YvsMutCompte;
//import yvs.entity.mutuelle.base.YvsMutGrilleTauxTypeCredit;
//import yvs.entity.mutuelle.base.YvsMutMutualiste;
//import yvs.entity.mutuelle.credit.YvsMutCredit;
//import yvs.entity.mutuelle.credit.YvsMutTypeCredit;
//import yvs.entity.mutuelle.echellonage.YvsMutEchellonage;
//import yvs.entity.mutuelle.echellonage.YvsMutMensualite;
//import yvs.entity.mutuelle.echellonage.YvsMutReglementMensualite;
//import yvs.entity.mutuelle.operation.YvsMutOperationCompte;
////import yvs.entity.mutuelle.salaire.YvsMutAvanceSalaire;
////import yvs.entity.mutuelle.salaire.YvsMutPaiementSalaire;
//import yvs.entity.mutuelle.salaire.YvsMutPeriodeSalaire;
////import yvs.entity.mutuelle.salaire.YvsMutReglementAvance;
//import yvs.grh.bean.Employe;
//import yvs.mutuelle.Compte;
//import yvs.mutuelle.Exercice;
//import yvs.mutuelle.ManagedMutualiste;
//import yvs.mutuelle.Mutualiste;
//import yvs.mutuelle.Parametre;
//import yvs.mutuelle.UtilMut;
//import yvs.mutuelle.base.GrilleTaux;
//import yvs.mutuelle.credit.Credit;
//import yvs.mutuelle.credit.ManagedTypeCredit;
//import yvs.mutuelle.credit.TypeCredit;
//import yvs.mutuelle.echellonage.Echellonage;
//import yvs.mutuelle.echellonage.Mensualite;
//import yvs.mutuelle.echellonage.ReglementMensualite;
//import yvs.mutuelle.operation.OperationCompte;
//import yvs.util.Constantes;
//import yvs.util.Managed;
//import yvs.util.ParametreRequete;
//import yvs.util.Util;
//
///**
// *
// * @author lymytz
// */
//@ManagedBean
//@SessionScoped
//public class ManagedPaiementSalaire extends Managed<OperationCompte, YvsMutOperationCompte> implements Serializable {
//
//    private OperationCompte paiementSalaire = new OperationCompte();
//    private List<YvsMutOperationCompte> salaires;
//    
//    
////    private List<YvsMutAvanceSalaire> avances;
//
//    private List<YvsMutOperationCompte> epargnes;
//    private List<YvsMutMensualite> mensualites;
//    private YvsMutMensualite mensualiteSelect;
//    private ReglementMensualite reglement = new ReglementMensualite();
//
//    private String tabIds, input_reset;
//    private String chaineSelect;
//    private boolean updatePaie, correct_ = false;
//
//    double montantCredit;
//    private TypeCredit type = new TypeCredit();
//    private List<YvsGrhOrdreCalculSalaire> listPeriodeSalaire;
//
//    private String codeSearch;
//    private String matricule;
//    private Parametre parametreMutuelle;
//
//    private String mutualisteSearch;
//    private boolean dateSearch;
//    private Date debutSearch = new Date(), finSearch = new Date();
//    private long typeSearch;
//
//    private String chainePeriode;
//    private List<YvsMutPeriodeSalaire> periodesSalaires;
//    private PeriodeSalaireMut newPeriode = new PeriodeSalaireMut();
//    private YvsMutPeriodeSalaire selectedPeriode = new YvsMutPeriodeSalaire();
//
//    private double resteRessource;   //contient (SalaireAPaye-Acomptes-Epargne O)
//
//    public ManagedPaiementSalaire() {
//        periodesSalaires = new ArrayList<>();
//        avances = new ArrayList<>();
//        epargnes = new ArrayList<>();
//        salaires = new ArrayList<>();
//        mensualites = new ArrayList<>(); 
//        listPeriodeSalaire = new ArrayList<>();
//    }
//
//    public TypeCredit getType() {
//        return type;
//    }
//
//    public void setType(TypeCredit type) {
//        this.type = type;
//    }
//
//    public String getMutualisteSearch() {
//        return mutualisteSearch;
//    }
//
//    public void setMutualisteSearch(String mutualisteSearch) {
//        this.mutualisteSearch = mutualisteSearch;
//    }
//
//    public boolean isDateSearch() {
//        return dateSearch;
//    }
//
//    public void setDateSearch(boolean dateSearch) {
//        this.dateSearch = dateSearch;
//    }
//
//    public Date getDebutSearch() {
//        return debutSearch;
//    }
//
//    public void setDebutSearch(Date debutSearch) {
//        this.debutSearch = debutSearch;
//    }
//
//    public Date getFinSearch() {
//        return finSearch;
//    }
//
//    public void setFinSearch(Date finSearch) {
//        this.finSearch = finSearch;
//    }
//
//    public long getTypeSearch() {
//        return typeSearch;
//    }
//
//    public void setTypeSearch(long typeSearch) {
//        this.typeSearch = typeSearch;
//    }
//
//    public List<YvsMutMutualiste> getListeMutualisteTempon() {
//        return listeMutualisteTempon;
//    }
//
//    public void setListeMutualisteTempon(List<YvsMutMutualiste> listeMutualisteTempon) {
//        this.listeMutualisteTempon = listeMutualisteTempon;
//    }
//
//    public YvsMutCompte getCompteCredit() {
//        return compteCredit;
//    }
//
//    public void setCompteCredit(YvsMutCompte compteCredit) {
//        this.compteCredit = compteCredit;
//    }
//
//    public List<YvsGrhOrdreCalculSalaire> getListHeaderBulletin() {
//        return listHeaderBulletin;
//    }
//
//    public void setListHeaderBulletin(List<YvsGrhOrdreCalculSalaire> listHeaderBulletin) {
//        this.listHeaderBulletin = listHeaderBulletin;
//    }
//
//    public String getChaineSelect() {
//        return chaineSelect;
//    }
//
//    public void setChaineSelect(String chaineSelect) {
//        this.chaineSelect = chaineSelect;
//    }
//
//    public List<YvsMutMensualite> getMensualiteCouvert() {
//        return mensualiteCouvert;
//    }
//
//    public void setMensualiteCouvert(List<YvsMutMensualite> mensualiteCouvert) {
//        this.mensualiteCouvert = mensualiteCouvert;
//    }
//
//    public double getResteRessource() {
//        return resteRessource;
//    }
//
//    public void setResteRessource(double resteRessource) {
//        this.resteRessource = resteRessource;
//    }
//
//    public String getMatricule() {
//        return matricule;
//    }
//
//    public void setMatricule(String matricule) {
//        this.matricule = matricule;
//    }
//
//    public PeriodeSalaireMut getNewPeriode() {
//        return newPeriode;
//    }
//
//    public void setNewPeriode(PeriodeSalaireMut newPeriode) {
//        this.newPeriode = newPeriode;
//    }
//
//    public List<YvsMutPeriodeSalaire> getPeriodesSalaires() {
//        return periodesSalaires;
//    }
//
//    public void setPeriodesSalaires(List<YvsMutPeriodeSalaire> periodesSalaires) {
//        this.periodesSalaires = periodesSalaires;
//    }
//
//    public YvsMutPeriodeSalaire getSelectedPeriode() {
//        return selectedPeriode;
//    }
//
//    public void setSelectedPeriode(YvsMutPeriodeSalaire selectedPeriode) {
//        this.selectedPeriode = selectedPeriode;
//    }
//
//    public List<YvsGrhOrdreCalculSalaire> getListPeriodeSalaire() {
//        return listPeriodeSalaire;
//    }
//
//    public void setListPeriodeSalaire(List<YvsGrhOrdreCalculSalaire> listPeriodeSalaire) {
//        this.listPeriodeSalaire = listPeriodeSalaire;
//    }
//
//    public double getMontantCredit() {
//        return montantCredit;
//    }
//
//    public void setMontantCredit(double montantCredit) {
//        this.montantCredit = montantCredit;
//    }
//
//    public boolean isCorrect_() {
//        return correct_;
//    }
//
//    public void setCorrect_(boolean correct_) {
//        this.correct_ = correct_;
//    }
//
//    public ReglementMensualite getReglement() {
//        return reglement;
//    }
//
//    public void setReglement(ReglementMensualite reglement) {
//        this.reglement = reglement;
//    }
//
//    public List<YvsMutMensualite> getMensualites() {
//        return mensualites;
//    }
//
//    public void setMensualites(List<YvsMutMensualite> mensualites) {
//        this.mensualites = mensualites;
//    } 
//
//    public OperationCompte getPaiementSalaire() {
//        return paiementSalaire;
//    }
//
//    public void setPaiementSalaire(OperationCompte paiementSalaire) {
//        this.paiementSalaire = paiementSalaire;
//    }
//
//    public List<YvsMutOperationCompte> getSalaires() {
//        return salaires;
//    }
//
//    public void setSalaires(List<YvsMutOperationCompte> salaires) {
//        this.salaires = salaires;
//    }
//
//    public List<YvsMutAvanceSalaire> getAvances() {
//        return avances;
//    }
//
//    public void setAvances(List<YvsMutAvanceSalaire> avances) {
//        this.avances = avances;
//    }
//
//    public List<YvsMutOperationCompte> getEpargnes() {
//        return epargnes;
//    }
//
//    public void setEpargnes(List<YvsMutOperationCompte> credits) {
//        this.epargnes = credits;
//    }
//
//    public YvsMutMensualite getMensualiteSelect() {
//        return mensualiteSelect;
//    }
//
//    public void setMensualiteSelect(YvsMutMensualite mensualiteSelect) {
//        this.mensualiteSelect = mensualiteSelect;
//    }
//
//    public String getTabIds() {
//        return tabIds;
//    }
//
//    public void setTabIds(String tabIds) {
//        this.tabIds = tabIds;
//    }
//
//    public String getInput_reset() {
//        return input_reset;
//    }
//
//    public void setInput_reset(String input_reset) {
//        this.input_reset = input_reset;
//    }
//
//    public boolean isUpdatePaie() {
//        return updatePaie;
//    }
//
//    public void setUpdatePaie(boolean updatePaie) {
//        this.updatePaie = updatePaie;
//    }
//
//    public String getCodeSearch() {
//        return codeSearch;
//    }
//
//    public void setCodeSearch(String codeSearch) {
//        this.codeSearch = codeSearch;
//    }
//
//    public void setChainePeriode(String chainePeriode) {
//        this.chainePeriode = chainePeriode;
//    }
//
//    public String getChainePeriode() {
//        return chainePeriode;
//    }
//
//    public Parametre getParametreMutuelle() {
//        return parametreMutuelle;
//    }
//
//    public void setParametreMutuelle(Parametre parametreMutuelle) {
//        this.parametreMutuelle = parametreMutuelle;
//    }
//
//    @Override
//    public void loadAll() {
//        loadAllPaiement(true, true);
//
//        loadAllMutualiste();
//        loadPeriodeSalaireNonPaye();
//        if (currentMutuel != null ? currentMutuel.getParamsMutuelle() != null : false) {
//            parametreMutuelle = UtilMut.buildBeanParametre(currentMutuel.getParamsMutuelle());
//        }
//        //charge les salaires payé ce mois
//        champ = new String[]{"mutuelle"};
//        val = new Object[]{currentMutuel};
//        //charge les salaires payés
//        salaires = dao.loadListTableByNameQueries("YvsMutPaiementSalaire.findSalairePaye", champ, val);
//        if (!salaires.isEmpty()) {
////            chainePeriode = salaires.get(0).getChainePeriode();
//        }
////        chooseOnePeriodeSalaire();
//        //charge les périodes salaires
//        champ = new String[]{"mutuelle"};
//        val = new Object[]{currentMutuel};
//        periodesSalaires = dao.loadNameQueries("YvsMutPeriodeSalaire.findAll", champ, val);
//
//        tabIds = "";
//        input_reset = "";
//    }
//
//    List<YvsMutMutualiste> listeMutualisteTempon;
//
//    //charge tous le mutualiste même non actif dont le compte employé est toujours actif
//    public void loadAllMutualiste() {
//        ManagedMutualiste w = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
//        if (w != null) {
//            listeMutualisteTempon = w.getMutualistes();
//        }
//    }
//
//    public void loadAllPaiement(Mutualiste bean) {
//        List<YvsBaseExercice> l1 = dao.loadNameQueries("YvsBaseExercice.findForLast", new String[]{"societe"}, new Object[]{currentScte});
//        Exercice e = Exercice.getDefault();
//        if ((l1 != null) ? !l1.isEmpty() : false) {
//            e = UtilMut.buildBeanExercice(l1.get(l1.size() - 1));
//        }
//        champ = new String[]{"mutualiste", "dateDebut", "dateFin"};
//        val = new Object[]{new YvsMutMutualiste(bean.getId()), e.getDateDebut(), e.getDateFin()};
////        historiquePaiement = dao.loadNameQueries("YvsMutPaiementSalaire.findHistoriquePaiement", champ, val);
//    }
//
//    public double getSalaire(Mutualiste bean) {
//        YvsMutMutualiste y = (YvsMutMutualiste) dao.loadOneByNameQueries("YvsMutMutualiste.findById", new String[]{"id"}, new Object[]{bean.getId()});
//        YvsGrhContratEmps c = getContrat(y);
//        correct_ = false;
//        if (c != null) {
//            if ((c.getSalaireMensuel() != null) ? c.getSalaireMensuel() > 0 : false) {
//                correct_ = true;
//                return c.getSalaireMensuel();
//            } else {
//                if ((c.getSalaireHoraire() != null) ? c.getSalaireHoraire() > 0 : false) {
//                    correct_ = true;
//                    return c.getSalaireHoraire() * 8 * 30;
//                } else {
//                    getErrorMessage("Le contrat du mutualiste est invalide");
//                }
//            }
//        }
//        update("txt_salaire_mutualiste");
//        return 0;
//    }
//
//    //récupère le contrat principal d'un employé
//    private YvsGrhContratEmps getContrat(YvsMutMutualiste y) {
////        List<YvsGrhContratEmps> l = y.getEmploye().getEmploye().getYvsContratEmpsList();
////        if ((l != null) ? !l.isEmpty() : false) {
////            for (YvsGrhContratEmps c : l) {
////                if (c.getContratPrincipal()) {
////                    return c;
////                }
////            }
////        } else {
////            getErrorMessage("Le mutualiste n'a pas de contrat");
////        }
//        return null;
//    }
//
//    private YvsGrhBulletins getBulletin(YvsGrhContratEmps c, Date date) {
//        String[] ch = new String[]{"contrat", "dateJour"};
//        Object[] v = new Object[]{c, date};
//        List<YvsGrhBulletins> l = dao.loadNameQueries("YvsGrhBulletins.findByContratActuel", ch, v);
//        if ((l != null) ? !l.isEmpty() : false) {
//            return l.get(0);
//        }
//        return null;
//    }
//
//    private double getMontantBulletin(YvsGrhBulletins b) {
//        List<YvsGrhDetailBulletin> l = b.getListDetails();
//        double montant = 0;
//        double retenu = 0, paye = 0, empl = 0;
//        for (YvsGrhDetailBulletin d : l) {
//            if (d.getElement().getVisibleBulletin()) {
//                retenu += d.getRetenuSalariale();
//                paye += d.getMontantPayer();
//                empl += d.getMontantEmployeur();
//            }
//        }
//        montant = paye - retenu;
//        return montant;
//    }
//
//    public YvsMutPaiementSalaire buildPaiementSalaire(PaiementSalaire y) {
//        YvsMutPaiementSalaire p = new YvsMutPaiementSalaire();
//        if (y != null) {
//            p.setId(y.getId());
//            p.setCommentaire(y.getCommentaire());
//            p.setDatePaiement((y.getDatePaiement() != null) ? y.getDatePaiement() : new Date());
//            p.setSalaire(y.getMontantAPayer());
//            p.setAvanceSalaireRetenu(y.getSoeAcompte());
//            p.setCreditRetenu(y.getSoeCredit());
//            p.setEpargneDuMois(y.getEpargneDuMois());
//            if ((y.getMutualiste() != null) ? y.getMutualiste().getId() > 0 : false) {
//                p.setMutualiste(new YvsMutMutualiste(y.getMutualiste().getId()));
//            }
//            p.setMontantPaye(y.getMontantPaye());
//            p.setPeriode(new YvsMutPeriodeSalaire(y.getPeriode().getId()));
//            p.setPayer(y.isPayer());
//            p.setAuthor(currentUser);
//        }
//        return p;
//    }
//
//    @Override
//    public OperationCompte recopieView() {
//        OperationCompte p = new OperationCompte();
//        p.setId(paiementSalaire.getId());
//        p.setCommentaire(paiementSalaire.getCommentaire());
//        p.setDateOperation((paiementSalaire.getDateOperation()!= null) ? paiementSalaire.getDateOperation(): new Date());
//        p.setMontant(paiementSalaire.getMontant());
//        p.setCompte(paiementSalaire.getCompte());
//        return p;
//    }
//
//    @Override
//    public boolean controleFiche(OperationCompte bean) {
//        if ((bean.getCompte()!= null) ? bean.getCompte().getId() == 0 : true) {
//            getErrorMessage("Vous devez specifier le mutualiste");
//            return false;
//        }
//        if (bean.getDateOperation()!= null) {
//            return existExerice(bean.getDateOperation());
//        }
//        return true;
//    }
//
//    public boolean existExerice(Date date) {
//        boolean exist = getExerciceOnDate(date);
//        if (!exist) {
//            openDialog("dlgConfirmCreate");
//        }
//        return exist;
//    }
//
//    public boolean getExerciceOnDate(Date date) {
//        String[] ch = new String[]{"societe", "dateJour"};
//        Object[] v = new Object[]{currentScte, date};
//        YvsBaseExercice exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findActifByDate", ch, v);
//        if ((exo != null) ? exo.getId() != 0 : false) {
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void populateView(OperationCompte bean) {
//        cloneObject(paiementSalaire, bean);
//        setUpdatePaie(true);
//    }
//
//    @Override
//    public void resetFiche() {
//        paiementSalaire = new OperationCompte();
//        matricule = "";
////        historiquePaiement.clear();
//        avances.clear();
//        mensualites.clear();
//        tabIds = "";
//        input_reset = "";
//        setUpdatePaie(false);
//    }
//    private double deficit = 0;
//
//    public double getDeficit() {
//        return deficit;
//    }
//
//    public void setDeficit(double deficit) {
//        this.deficit = deficit;
//    }
//
//    private boolean controleBean(OperationCompte p) {
//        if (p.getMontant() <= 0) {
//            getErrorMessage("La paye de ce Salarié semble ne pas être disponible ! Veuillez contacter le Service Responsable !");
//            return false;
//        }
//        return true;
//    }
//
//    public void saveSalaireMonth(boolean controle) {
////        YvsMutPaiementSalaire p = buildPaiementSalaire(paiementSalaire);
////        if (p != null) {
////            YvsMutCompte cpteCredit = null, cpteSalaire = null;
//////            for (YvsMutCompte cpt : paiementSalaire.getCompte()) {
//////                if (cpt.getTypeCompte().getNature().equals(Constantes.MUT_TYPE_COMPTE_COURANT)) {
//////                    cpteCredit = cpt;
//////                }
//////                if (cpt.getTypeCompte().getNature().equals(Constantes.MUT_TYPE_COMPTE_COURANT)) {
//////                    cpteSalaire = cpt;
//////                }
//////            }
////            if (controleBean(paiementSalaire)) {
////                //le traitement du déficit est déclenché si le montant à payer est <0 et l'action choisi est différent de rie
////                if (paiementSalaire.getMontant() < 0 && !parametreMutuelle.getActionDeficit().equals(Constantes.MUT_ACTION_RIEN) && !controle) {
//////                    deficit = (paiementSalaire.getSoldeCompteSalaire() + p.getMontantPaye()) - (paiementSalaire.getSoeAcompte() + currentMutuel.getMontantEpargne() + paiementSalaire.getSoeCredit());
////                    //créer un nouveau crédit 
////
////                    //vérifie que le mutualiste a bien un compte crédit
////                    boolean trouve = (cpteCredit != null);
////                    if (!trouve) {
////                        getErrorMessage("Impossible de terminer ! un compte Crédiot est nécessaire pour terminer cette action. veuillez le paramétrer! ");
////                        return;
////                    }
////                    traiterLedeficit();
////                    openDialog("dlgCreateCredit");
////                    update("dlgCreateCredit_");
////                    return;
////                }
////                /**
////                 * Traiter les retenues sur le salaire
////                 */
////                p.setCreditRetenu(paiementSalaire.getSoeCredit());
////                p.setAvanceSalaireRetenu(paiementSalaire.getSoeAcompte());
////                p.setSalaire((p.getSalaire() < 0) ? 0 : p.getSalaire());
////                p.setMontantPaye((p.getMontantPaye() < 0) ? 0 : p.getMontantPaye());
////                p.setPayer(true);
////                p.setDateSave(new Date());
////                if (enregistrerLesElementsDuDeficit()) {
////                    dao.update(p);
////                    //insérer dans la table opération dans le compte salaire
////                    insertOperationSalaire(p, cpteSalaire);
////                }
////
////                update("data_paie");
////                succes();
////            }
////
////        }
//    }
//
//    //contrôle les paramètre de report des mensualités
//    private boolean controleFormReport() {
//        //la on doit repporter toutes les mensualité à la fin de l'echéancier (selon la méthode de repot total)
//        return true;
//    }
//
////    private boolean enregistrerLesElementsDuDeficit() {
////        switch (parametreMutuelle.getActionDeficit()) {
////            case Constantes.MUT_ACTION_TRANSFERE:
////                //enregistrer le déficit coe un crédit
////                reglerAvanceSalaire(avances);
////                saveEpargneMois(paiementSalaire.getMutualiste(), paiementSalaire.getEpargneDuMois());
////                reglerMensualite(mensualites);
////                break;
////            case Constantes.MUT_ACTION_REPORT:
////                //reporter les mensualité s'il y en a
////                if (controleFormReport()) {
////                    reglerAvanceSalaire(avances);
////                    saveEpargneMois(paiementSalaire.getMutualiste(), paiementSalaire.getEpargneDuMois());
////                    reglerMensualite(mensualiteCouvert);
////                } else {
////                    return false;
////                }
////                break;
////            default:
////                break;
////        }
////        return true;
////    }
//
//    public void traiterLedeficit() {
////        resteRessource = paiementSalaire.getMontantAPayer() - paiementSalaire.getSoeAcompte() - paiementSalaire.getEpargneDuMois();
////        switch (parametreMutuelle.getActionDeficit()) {
////            case Constantes.MUT_ACTION_TRANSFERE:
////                //reporter l'ensemble des mensualité
////                deficit = resteRessource - paiementSalaire.getSoeCredit();
////                break;
////            case Constantes.MUT_ACTION_REPORT:
////                if (resteRessource > 0) {
////                    //recherche les mensualité couverte
////                    //il faut déjà que toutes les mensualités appartiennent au même crédit
////                    if (transfertMensualite(mensualites)) {
////                        //
////                        deficit = resteRessource - findMontantMensualiteCouvert(resteRessource);
////                        paiementSalaire.setMontantPaye(deficit);
////                    } else {
////                        getWarningMessage("impossible de reporter les mensualités. ils appartiennent à des crédit varier !");
////                        deficit = resteRessource - paiementSalaire.getSoeCredit();
////                    }
////                } else {
////                    //le montant à payer ne peut couvrir aucune mensualité
////                    deficit = resteRessource - paiementSalaire.getSoeCredit();
////                }
////                break;
////            default:
////                break;
////        }
//    }
//
//    private List<YvsMutMensualite> mensualiteCouvert; //représente la partie des mensualités couverte par les ressources du mois
//    //cette méthode est correcte lorsque les mensualité sont triées par montant croissant
//
//    private double findMontantMensualiteCouvert(double reste) {
//        mensualiteCouvert = new ArrayList<>();
//        double re = 0;
//        for (YvsMutMensualite m : mensualites) {
//            if (reste > m.getMontant()) {
//                re += m.getMontant();
//                reste -= m.getMontant();
//                mensualiteCouvert.add(m);
//            }
//        }
//        mensualites.removeAll(mensualiteCouvert);
//        return re;
//    }
//
//    private boolean transfertMensualite(List<YvsMutMensualite> Lmens) {
//        if (!Lmens.isEmpty()) {
//            YvsMutMensualite mens = Lmens.get(0);
//            for (YvsMutMensualite m : Lmens) {
//                if (m.getEchellonage().getCredit() != mens.getEchellonage().getCredit()) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    public void insertOperationSalaire(YvsMutPaiementSalaire p, YvsMutCompte cpt) {
//        if (p.getMontantPaye() > 0) {
//            YvsMutOperationCompte op = new YvsMutOperationCompte();
//            op.setAuthor(currentUser);
//            op.setAutomatique(false);
//            op.setCompte(new YvsMutCompte(cpt.getId()));
////            op.setDateOperation(paiementSalaire.getDatePaiement());
//            op.setHeureOperation(new Date());
//            op.setNature(Constantes.MUT_NATURE_OP_SALAIRE);
//            op.setSensOperation(Constantes.MUT_SENS_OP_RETRAIT);
//            op.setSouceReglement(p.getId());
//            op.setMontant(-p.getSalaire());
//            op.setAuthor(currentUser);
//            op.setAutomatique(true);
//            op.setTableSource("yvs_mut_paiement_salaire");
//            dao.save(op);
//        }
//    }
//
//    private boolean saveEpargneMois(Mutualiste m, double montant) {
//        YvsMutCompte c = findCompteEpargne(m.getComptes());
//        if (c != null && montant > 0) {
//            YvsMutOperationCompte op = new YvsMutOperationCompte();
//            op.setAuthor(currentUser);
//            op.setAutomatique(true);
//            op.setCompte(new YvsMutCompte(c.getId()));
//            op.setEpargneMensuel(true);
////            op.setHeureOperation(paiementSalaire.getDatePaiement());
//            op.setMontant(montant);
//            op.setNature(Constantes.MUT_NATURE_OP_EPARGNE);
//            op.setSensOperation(Constantes.MUT_SENS_OP_DEPOT);
////            op.setDateOperation(paiementSalaire.getDatePaiement());
//            op.setHeureOperation(new Date());
//            op.setSouceReglement(paiementSalaire.getId());
//            op.setTableSource("yvs_mut_paiement_salaire");
//            dao.save(op);
//            return true;
//        } else {
//            return false;
//        }
//    }
//    YvsMutCompte compteCredit;
//
//    private YvsMutCompte findCompteEpargne(List<YvsMutCompte> l) {
//        for (YvsMutCompte c : l) {
//            if (c.getTypeCompte().getNature().equals(Constantes.MUT_TYPE_COMPTE_EPARGNE)) {
//                return c;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public boolean saveNew() {
//        return true;
//    }
//
//    @Override
//    public void deleteBean() {
//        try {
//            if ((tabIds != null) ? tabIds.length() > 0 : false) {
//                String[] ids = tabIds.split("-");
//                if ((ids != null) ? ids.length > 0 : false) {
//                    for (String s : ids) {
//                        long id = Integer.valueOf(s);
////                        YvsMutPaiementSalaire bean = salaires.get(salaires.indexOf(new YvsMutPaiementSalaire(id)));
////                        if (bean.getId() > 0) {
////                            dao.delete(new YvsMutPaiementSalaire(bean.getId()));
////                            salaires.remove(bean);
////                        }
//                    }
//                    resetFiche();
//                    succes();
//                    update("data_paie");
//                    update("blog_form_paie");
//                }
//            }
//        } catch (NumberFormatException ex) {
//            getErrorMessage("Suppression Impossible !");
//            System.err.println("error suppression : " + ex.getMessage());
//        }
//    }
//
//    public void deleteBean(YvsMutPaiementSalaire y) {
//        try {
//            if (y != null) {
//                dao.delete(y);
//                salaires.remove(y);
//
//                resetFiche();
//                succes();
//                update("data_paie");
//                update("blog_form_paie");
//            }
//        } catch (NumberFormatException ex) {
//            getErrorMessage("Suppression Impossible !");
//            System.err.println("error suppression gamme article : " + ex.getMessage());
//        }
//    }
//
//    public void deleteBean_(YvsMutPeriodeSalaire y) {
//        try {
//            if (y != null) {
//                dao.delete(y);
//                periodesSalaires.remove(y);
//
//                resetFiche();
//                succes();
//                update("listePeriodeRh");
//            }
//        } catch (NumberFormatException ex) {
//            getErrorMessage("Suppression Impossible !");
//            System.err.println("error suppression gamme article : " + ex.getMessage());
//        }
//    }
//
//    @Override
//    public void updateBean() {
//        if ((tabIds != null) ? tabIds.length() > 0 : false) {
//            String[] ids = tabIds.split("-");
//            setUpdatePaie((ids != null) ? ids.length > 0 : false);
//            if (isUpdatePaie()) {
//                long id = Integer.valueOf(ids[ids.length - 1]);
////                YvsMutPaiementSalaire bean = salaires.get(salaires.indexOf(new YvsMutPaiementSalaire(id)));
////                populateView(UtilMut.buildBeanPaiementSalaire(bean));
//                tabIds = "";
//                input_reset = "";
//                update("blog_form_paie");
//            }
//        }
//    }
//
//    @Override
//    public void onSelectObject(YvsMutOperationCompte y) {
////        populateView(UtilMut.buildBeanPaiementSalaire(y));
////        matricule = y.getMutualiste().getEmploye().getMatricule();
//        update("blog_form_paie");
//    }
//
//    @Override
//    public void loadOnView(SelectEvent ev) {
//        if ((ev != null) ? ev.getObject() != null : false) {
//            YvsMutPaiementSalaire bean = (YvsMutPaiementSalaire) ev.getObject();
////            onSelectObject((bean));
//        }
//    }
//
//    public void loadOnViewMutualiste(SelectEvent ev) {
//        if ((ev != null) ? ev.getObject() != null : false) {
//            YvsMutMutualiste bean = (YvsMutMutualiste) ev.getObject();
//            chooseMutualiste(UtilMut.buildBeanMutualiste(bean));
//        }
//    }
//
//    public void chooseMutualiste(Mutualiste bean) {
//        matricule = bean.getEmploye().getMatricule();
//        update("blog_form_paie");
//    }
//
//    public void searchMutualiste() {
////        String num = paiementSalaire.getMutualiste().getMatricule();
////        paiementSalaire.getMutualiste().setId(0);
////        paiementSalaire.getMutualiste().setError(true);
////        paiementSalaire.getMutualiste().setEmploye(new Employe());
////        ManagedMutualiste m = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
////        if (m != null) {
////            Mutualiste y = m.searchMutualiste(num, true);
////            if (m.getMutualistes() != null ? !m.getMutualistes().isEmpty() : false) {
////                if (m.getMutualistes().size() > 1) {
////                    update("data_mutualiste_paie");
////                } else {
////                    chooseMutualiste(y);
////                }
////                paiementSalaire.getMutualiste().setError(false);
////            }
////        }
//    }
//
//    public void initMutualistes() {
//        ManagedMutualiste m = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
//        if (m != null) {
////            m.initMutualistes(paiementSalaire.getMutualiste());
//        }
//        update("data_mutualiste_paie");
//    }
//
//    private double giveSoldeCompteEpargne(Mutualiste m) {
//        double re = 0;
//        champ = new String[]{"compte"};
//        for (YvsMutCompte cpt : m.getComptes()) {
//            if (cpt.getTypeCompte().getNature().equals(Constantes.MUT_TYPE_COMPTE_COURANT)) {
//                val = new Object[]{new YvsMutCompte(cpt.getId())};
//                Double d = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findSoldeEpargne", champ, val);
//                re = (d != null) ? d : 0;
//            }
//        }
//        return re;
//    }
//
//    private boolean controleFormSalaire() {
//        if (matricule == null) {
//            getErrorMessage("Veuillez entrer le matricule du mutualiste !");
//            return false;
//        }
//        if (selectedPeriode == null) {
//            getErrorMessage("Vous devez choisir une session de paiement des salaires !");
//            return false;
//        }
//        //la période de traitement choisie doit être actif
//        if (!selectedPeriode.getActif()) {
//            getErrorMessage("La session de paye des salaires choisie n'est pas active !");
//            return false;
//        }
//        //la période ne doit pas être cloturé
//        if (selectedPeriode.getCloture()) {
//            getErrorMessage("La session de paye des salaires choisie à déjà été clôturé !");
//            return false;
//        }
//        return true;
//    }
//
//    public void findMutualisteByMatricule() {
//        if (controleFormSalaire()) {
//            champ = new String[]{"matricule", "periode"};
//            val = new Object[]{matricule, new YvsMutPeriodeSalaire(selectedPeriode.getId())};
//            YvsMutPaiementSalaire salaire = (YvsMutPaiementSalaire) dao.loadOneByNameQueries("YvsMutPaiementSalaire.findEmployeAndSession", champ, val);
////            if (salaire != null) {
////                paiementSalaire = UtilMut.buildBeanPaiementSalaire(salaire);
////                correct_ = false;
////                //charge les autres engagement du mutualiste
////                loadDataMutualiste(paiementSalaire.getMutualiste(), salaire.getPeriode().getPeriodeRh());
////                loadAllPaiement(paiementSalaire.getMutualiste());
////
////            } else {
////                paiementSalaire = new PaiementSalaire();
////                getErrorMessage("Aucun salaire n'a été trouvé pour ce mutualiste pour la session active !");
////            }
//        }
//    }
//
//    public void loadAllAvance(Mutualiste bean, Date dateFin) {
//        String[] ch = new String[]{"mutualiste", "dateAvance", "etat"};
//        Object[] v = new Object[]{new YvsMutMutualiste(bean.getId()), dateFin, Constantes.ETAT_VALIDE};
//        avances = dao.loadNameQueries("YvsMutAvanceSalaire.findAvanceEnCours", ch, v);
//    }
//
//    public void loadAllEpargne(Mutualiste bean, Date dateDebut, Date dateFin) {
//        String[] ch = new String[]{"mutualiste", "nature", "dateDebut", "dateFin"};
//        Object[] v = new Object[]{new YvsMutMutualiste(bean.getId()), "Depot", dateDebut, dateFin};
//        epargnes = dao.loadNameQueries("YvsMutOperationCompte.findByMutualisteDates", ch, v);
//        double montant = 0;
//        for (YvsMutOperationCompte o : epargnes) {
//            montant += o.getMontant();
//        }
////        double epargne = paiementSalaire.getMutualiste().getMontantEpargne();
////        paiementSalaire.getMutualiste().setMontantResteEpargne(((epargne - montant) < 0) ? 0 : epargne - montant);
//    }
//
//    //charge toutes les mensualites non encore réglé et validé à dessous de la date de calcul des bulletins
//    public void loadAllMensualite(Mutualiste bean, Date dateFin) {
//        mensualites.clear();
//        String[] ch = new String[]{"mutualiste", "dateFin", "etat", "etatMens"};
//        Object[] v = new Object[]{new YvsMutMutualiste(bean.getId()), dateFin, Constantes.ETAT_ENCOURS, Constantes.ETAT_REGLE};
//        List<YvsMutMensualite> l = dao.loadNameQueries("YvsMutMensualite.findMensualiteEnCour", ch, v);
//        if (l != null) {
//            double montant = 0;
//            for (YvsMutMensualite m : l) {
//                for (YvsMutReglementMensualite r : m.getReglements()) {
//                    montant += r.getMontant();
//                }
//                if (m.getMontant() > montant) {
//                    mensualites.add(m);
//                }
//            }
//        }
//    }
//
//    private void loadDataMutualiste(Mutualiste m, YvsGrhOrdreCalculSalaire order) {
//        //calcul le credit et les avances sur salaire du mois                
////        loadAllAvance(m, order.getFinMois());
////        loadAllMensualite(m, order.getFinMois());
////        paiementSalaire.setSoeAcompte(giveSoeAcompte());
////        paiementSalaire.setSoeCredit(giveSoeCredit());
////        //détermine le total des engagements
////        paiementSalaire.setSoldeCompteSalaire(giveSoldeCompteEpargne(m));
////        double totalRessource, totalRetenu;
////        if (parametreMutuelle.isRetainsEpargne() && currentMutuel != null) {
////            paiementSalaire.setEpargneDuMois(paiementSalaire.getEpargneDuMois() + currentMutuel.getMontantEpargne());
////        }
////        totalRessource = paiementSalaire.getSoldeCompteSalaire() + paiementSalaire.getMontantAPayer();
////        totalRetenu = paiementSalaire.getSoeAcompte() + paiementSalaire.getSoeCredit() + paiementSalaire.getEpargneDuMois();
////        if (totalRessource <= totalRetenu) {
////            //si le montant du salaire à payer n'est pas suffisant, on ne retient que l'epargne obligatoire
////            paiementSalaire.setEpargneDuMois(currentMutuel != null ? currentMutuel.getMontantEpargne() : 0);
////            totalRetenu = paiementSalaire.getSoeAcompte() + paiementSalaire.getSoeCredit() + paiementSalaire.getEpargneDuMois();
////        }
////        paiementSalaire.setMontantPaye(totalRessource - totalRetenu);
//    }
//
////    public void terminatedSaveSalaire() {
////        if (parametreMutuelle.getActionDeficit().equals(Constantes.MUT_ACTION_REPORT) || parametreMutuelle.getActionDeficit().equals(Constantes.MUT_ACTION_TRANSFERE)) {
////            ManagedTypeCredit w = (ManagedTypeCredit) giveManagedBean(ManagedTypeCredit.class);
////            if (w != null) {
////                if ((type != null) ? type.getId() > 0 : false) {
////                    TypeCredit t = UtilMut.buildBeanTypeCredit(w.getTypes().get(w.getTypes().indexOf(new YvsMutTypeCredit(type.getId()))));
////                    correct_ = saveNewCredit(Math.abs(deficit), t);
////                    if (correct_) {
////                        saveSalaireMonth(true);
////                        closeDialog("dlgCreateCredit");
////                    }
////                }
////            }
////        }
////    }
//
////    private boolean reportMensualite() {
////        //les mensualité à reporter sont dans la liste mensualites
////        return true;
////    }
//
////    private boolean saveNewCredit(double montant, TypeCredit typ) {
////        //Recherche de compte credit du mutualiste
////        YvsMutCompte compte = null;
//////        for (YvsMutCompte c : paiementSalaire.getMutualiste().getComptes()) {
//////            if (c.getTypeCompte().getNature().equals(Constantes.MUT_TYPE_COMPTE_COURANT)) {
//////                compte = c;
//////                break;
//////            }
//////        }
////        if (compte != null) {
////            //Creation d'un credit pour l'impayé
////            Credit credit = recopieViewCredit(UtilMut.buildBeanCompte(compte), montant, typ);
////            YvsMutCredit entCredit = buildCredit(credit);
////            entCredit.setAuthor(currentUser);
////            entCredit.setAutomatique(true);
////            entCredit.setEtat(Constantes.ETAT_VALIDE);
////            entCredit = (YvsMutCredit) dao.save1(entCredit);
////            credit.setId(entCredit.getId());
////            switch (typ.getNatureMontant()) {
////                case "Fixe":
////                    //Rechercher la grille pour le montant du credit
////                    YvsMutGrilleTauxTypeCredit grille = searchTaux(typ, credit.getMontant());
////                    if (grille == null) {
////                        grille = new YvsMutGrilleTauxTypeCredit();
////                        grille.setPeriodeMaximal(typ.getPeriodeMaximal());
////                        grille.setTaux(typ.getTauxMaximal());
////                    }
////                    //Creation de l'echeancier pour le credit
////                    createEcheancier(UtilMut.buildBeanGrilleTaux(grille), credit);
////                    entCredit.setEtat(Constantes.ETAT_REGLE);
////                    dao.update(entCredit);
////                    break;
////                case "Pourcentage":
//////                    double salaire = paiementSalaire.getMutualiste().getMontantSalaire();
//////                    double montantTotal = salaire * typ.getMontantMaximal() / 100;
//////                    double taux = (credit.getMontant() * typ.getMontantMaximal()) / montantTotal;
////
////                    //Permet de rechercher la grille pour le montant du credit
//////                    grille = searchTaux(typ, taux);
//////                    if (grille == null) {
//////                        grille = new YvsMutGrilleTauxTypeCredit();
//////                        grille.setPeriodeMaximal(typ.getPeriodeMaximal());
//////                        grille.setTaux(typ.getTauxMaximal());
//////                    }
////                    //Permet de creer l'echeancier
//////                    createEcheancier(UtilMut.buildBeanGrilleTaux(grille), credit);
////                    entCredit.setEtat(Constantes.ETAT_REGLE);
////                    dao.update(entCredit);
////                    break;
////                default:
////                    getErrorMessage("Ce w.getTypeCredit() de credit est invalide!");
////                    break;
////            }
////            if (parametreMutuelle.getActionDeficit().equals(Constantes.MUT_ACTION_REPORT)) {
////                if (!reportMensualite()) {
////                    getErrorMessage("L'opération n'est pas terminé. les mensualités n'ont pas été reporté ");
////                    return false;
////                }
////            }
////            succes();
////            return true;
////        } else {
////            getErrorMessage("Ce mutualiste n'a pas de compte credit");
////            return false;
////        }
////    }
//
//    public YvsMutCredit buildCredit(Credit y) {
//        YvsMutCredit c = new YvsMutCredit();
//        if (y != null) {
//            c.setId(y.getId());
//            c.setDateCredit((y.getDateCredit() != null) ? y.getDateCredit() : new Date());
//            c.setHeureCredit((y.getHeureCredit() != null) ? y.getHeureCredit() : new Date());
//            c.setReference(y.getReference());
//            c.setEtat(y.getEtatValidation());
//            c.setMontant(y.getMontant());
//            c.setDateEffet(y.getDateEffet());
//            c.setDuree(y.getDuree());
//            c.setMontantVerse(y.getMontantVerse());
//            if ((y.getCompte() != null) ? y.getCompte().getId() != 0 : false) {
//                c.setCompte(new YvsMutCompte(y.getCompte().getId()));
//            }
//            if ((y.getType() != null) ? y.getType().getId() != 0 : false) {
//                c.setType(new YvsMutTypeCredit(y.getType().getId()));
//            }
//        }
//        return c;
//    }
//
//    public Credit recopieViewCredit(Compte compte, double montant, TypeCredit type) {
//        Credit c = new Credit();
//        c.setId(0);
//        c.setDateCredit(new Date());
//        c.setHeureCredit(new Date());
//        Calendar cal = Util.dateToCalendar(new Date());
//        c.setReference("IMPAYE_" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR));
//        c.setEtatValidation(Constantes.ETAT_VALIDE);
//        c.setMontant(montant);
//        c.setCompte(compte);
//        c.setType(type);
//        Calendar ca = Calendar.getInstance();
//        ca.setTime(parametreMutuelle.getDebutMois());
//        Calendar ca_ = Calendar.getInstance();
//        ca.set(Calendar.MONTH, ca_.get(Calendar.MONTH));
//        ca.add(Calendar.MONTH, 1);
//        c.setDateEffet(ca.getTime());
//        c.setDuree((int) type.getPeriodeMaximal());
//        c.setMontantVerse(0);
//        return c;
//    }
//
//    //Permet de creer l'echeancier
//    private void createEcheancier(GrilleTaux g, Credit credit) {
//        if (g != null) {
//            Echellonage e = defautlEchellonage(credit, g);
//            YvsMutEchellonage ent = saveNewEchellonage(e);
//            if (ent != null) {
//                //Permet de calculer les mensualites de l'echeancier
//                for (Mensualite m : calculMensualite(e)) {
//                    saveNewMensualite(m, ent);
//                }
//            } else {
//                getErrorMessage("Information sur l'echeancier sont invalide!");
//            }
//        } else {
//            getErrorMessage("Ce w.getTypeCredit() de credit n'a pas de grille!");
//        }
//    }
//
//    public YvsMutGrilleTauxTypeCredit searchTaux(TypeCredit t, double montant) {
//        if (t != null) {
//            if (t.getGrilles() != null) {
//                if (!t.getGrilles().isEmpty()) {
//                    for (YvsMutGrilleTauxTypeCredit r : t.getGrilles()) {
//                        if (r.getMontantMinimal() > montant && r.getMontantMaximal() < montant) {
//                            return r;
//                        }
//                    }
//                }
//            }
//            YvsMutGrilleTauxTypeCredit r = new YvsMutGrilleTauxTypeCredit();
//            r.setMontantMaximal(montant);
//            r.setMontantMinimal(montant);
//            r.setTaux(t.getTauxMaximal());
//            r.setPeriodeMaximal(t.getPeriodeMaximal());
//            return r;
//        }
//        return null;
//    }
//
//    public YvsMutEchellonage buildEchellonage(Echellonage y) {
//        YvsMutEchellonage e = new YvsMutEchellonage();
//        if (y != null) {
//            if ((y.getCredit() != null) ? y.getCredit().getId() != 0 : false) {
//                e.setCredit(new YvsMutCredit(y.getCredit().getId()));
//            }
//            e.setDateEchellonage((y.getDateEchellonage() != null) ? y.getDateEchellonage() : new Date());
//            e.setDureeEcheance(y.getDureeEcheance());
//            e.setEtat(y.getEtat());
//            e.setId(y.getIdEch());
//            e.setMontant(y.getMontant());
//            e.setTaux(y.getTaux());
//            e.setEcartMensualite(y.getEcartMensualite());
//            e.setMontantVerse(0.0);
//            e.setActif(true);
//        }
//        return e;
//    }
//
//    //retour l'echeancier par defaut
//    public Echellonage defautlEchellonage(Credit credit, GrilleTaux g) {
//        Echellonage e = new Echellonage();
//        e.setDateEchellonage(new Date());
//        e.setEtat(Constantes.ETAT_ENCOURS);
//        e.setDureeEcheance(g.getPeriodeMaximal());
//        e.setMontant(credit.getMontant() / g.getPeriodeMaximal());
//        e.setTaux(g.getTaux());
//        e.setCredit(credit);
//        return e;
//    }
//
//    public YvsMutEchellonage saveNewEchellonage(Echellonage bean) {
//        try {
//            YvsMutEchellonage entity = buildEchellonage(bean);
//            entity = (YvsMutEchellonage) dao.save1(entity);
//            bean.setIdEch(entity.getId());
//            return entity;
//        } catch (Exception e) {
//            System.err.println("Error Insertion : " + e.getMessage());
//            return null;
//        }
//    }
//
//    //Permet de calculer les mensualites de l'echeancier
//    public List<Mensualite> calculMensualite(Echellonage bean) {
//        List<Mensualite> l = new ArrayList<>();
//        double capital = bean.getMontant(); //montant du crédit
//        double mensualite;
//        double duree = bean.getDureeEcheance(); //durée de remboursement
//        double taux = bean.getTaux();//Taux d'intérêt annuel
//        double periodicite = bean.getEcartMensualite();   //périodicité de remboursement
//        Calendar cal = Util.dateToCalendar(bean.getDateEchellonage());
//        try {
//            taux = (taux / 100) / (12 / periodicite);
//        } catch (Exception ex) {
//            getErrorMessage("L'échéancier n'a pas été généré !");
//            return l;
//        }
//        if (taux != 0) {
//            mensualite = (capital * taux) / (1 - (Math.pow((1 + taux), -duree)));
//        } else {
//            mensualite = capital / duree;
//        }
//        int entier = (int) duree;
//        double interet;
//        Mensualite m;
//        for (int i = 0; i < entier; i++) {
//            cal.add(Calendar.MONTH, bean.getEcartMensualite());
//            m = new Mensualite(i + 1);
//            interet = capital * taux;
//            m.setDateMensualite(cal.getTime());
//            m.setEtat(Constantes.ETAT_EDITABLE);
//            m.setMontant(Constantes.arrondi(mensualite, parametreMutuelle.getArrondiValeur()));
//            m.setInteret(Constantes.arrondi(interet, parametreMutuelle.getArrondiValeur()));
//            m.setAmortissement(Constantes.arrondi(mensualite, parametreMutuelle.getArrondiValeur()) - m.getInteret());
//            capital -= m.getAmortissement();
//            l.add(m);
//        }
//        return l;
//    }
//
//    public YvsMutMensualite buildMensualite(Mensualite y, YvsMutEchellonage entity) {
//        YvsMutMensualite m = new YvsMutMensualite();
//        if (y != null) {
//            m.setDateMensualite((y.getDateMensualite() != null) ? y.getDateMensualite() : new Date());
//            m.setId(y.getId());
//            m.setMontant(y.getMontant());
//            m.setEchellonage(entity);
//            m.setInteret(y.getInteret());
//            m.setAmortissement(y.getAmortissement());
//            m.setEtat(Constantes.ETAT_ENCOURS);
//        }
//        return m;
//    }
//
//    public Mensualite saveNewMensualite(Mensualite bean, YvsMutEchellonage entityEch) {
//        try {
//            YvsMutMensualite entity = buildMensualite(bean, entityEch);
//            entity = (YvsMutMensualite) dao.save1(entity);
//            bean.setId(entity.getId());
//            return bean;
//        } catch (Exception e) {
//            System.err.println("Error Insertion : " + e.getMessage());
//            return new Mensualite();
//        }
//    }
//
//    private List<YvsGrhOrdreCalculSalaire> listHeaderBulletin;
//
//    private void loadPeriodeSalaireNonPaye() {
//        champ = new String[]{"societe"};
//        val = new Object[]{currentScte};
//        listHeaderBulletin = dao.loadNameQueries("YvsGrhOrdreCalculSalaire.findOrderNonPaye", champ, val);
//        listPeriodeSalaire = listHeaderBulletin;
//    }
//
//    public void loadAllPaiement(boolean avance, boolean init) {
//        paginator.addParam(new ParametreRequete("y.mutualiste.mutuelle", "mutuelle", currentMutuel, "=", "AND"));
//        salaires = paginator.executeDynamicQuery("YvsMutPaiementSalaire", "y.datePaiement DESC", avance, init, (int) imax, dao);
//    }
//
//    public void parcoursInAllResult(boolean avancer) {
//        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
//        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
//            setOffset(0);
//        }
////        List<YvsMutPaiementSalaire> re = paginator.parcoursDynamicData("YvsMutPaiementSalaire", "y", "y.datePaiement DESC", getOffset(), dao);
////        if (!re.isEmpty()) {
////            onSelectObject(re.get(0));
////        }
//    }
//
//    public void paginer(boolean next) {
//        loadAllPaiement(next, false);
//    }
//
//    @Override
//    public void choosePaginator(ValueChangeEvent ev) {
//        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
//        loadAllPaiement(true, true);
//    }
//
//    public void loadAllPaiement(Date debut, Date fin) {
//        champ = new String[]{"mutuelle", "dateDebut", "dateFin"};
//        val = new Object[]{currentMutuel, debut, fin};
//        salaires = dao.loadNameQueries("YvsMutPaiementSalaire.findByMutuelle", champ, val);
//    }
//
//    public void loadAllPaiement(long idPeriode) {
//        champ = new String[]{"mutuelle", "periode"};
//        val = new Object[]{currentMutuel, new YvsGrhOrdreCalculSalaire(idPeriode)};
//        salaires = dao.loadNameQueries("YvsMutPaiementSalaire.findByPeriode", champ, val);
//    }
//
//    private YvsGrhBulletins findBulltinEmps(List<YvsGrhBulletins> l, Mutualiste m) {
//        for (YvsGrhBulletins b : l) {
//            if (b.getContrat().getEmploye().getId().equals(m.getEmploye().getId())) {
//                return b;
//            }
//        }
//        return null;
//    }
//
//    private PaiementSalaire findPaiement(List<PaiementSalaire> l, Mutualiste m) {
//        for (PaiementSalaire b : l) {
//            if (b.getMutualiste().equals(m)) {
//                return b;
//            }
//        }
//        return null;
//    }
//
//    private double giveSoeCredit() {
//        double r = 0;
//        for (YvsMutMensualite m : mensualites) {
//            r += m.getMontant();
//        }
//        return r;
//    }
//
//    private double giveSoeAcompte() {
//        double r = 0;
//        for (YvsMutAvanceSalaire a : avances) {
//            r += a.getMontant();
//        }
//        return r;
//    }
//
//    private void reglerAvanceSalaire(List<YvsMutAvanceSalaire> l) {
//        YvsMutReglementAvance a;
//        for (YvsMutAvanceSalaire y : l) {
//            a = new YvsMutReglementAvance();
//            a.setId(null);
//            a.setAvance(new YvsMutAvanceSalaire(y.getId()));
//            a.setMontant(y.getMontant());
////            a.setDateReglement(paiementSalaire.getDatePaiement());
//            a.setSouceReglement(new YvsMutPaiementSalaire(paiementSalaire.getId()));
//            dao.save(a);
//        }
//    }
//
//    private void reglerMensualite(List<YvsMutMensualite> l) {
//        YvsMutReglementMensualite a;
//        for (YvsMutMensualite y : l) {
//            a = new YvsMutReglementMensualite();
//            a.setId(null);
//            a.setMensualite(new YvsMutMensualite(y.getId()));
//            a.setMontant(y.getMontant());
////            a.setDateReglement(paiementSalaire.getDatePaiement());
//            a.setSouceReglement(new YvsMutPaiementSalaire(paiementSalaire.getId()));
//            dao.save(a);
//        }
//    }
//    /*Permet de rechercher le salaire d'un mutualiste. le salaire du mutualiste sera la moyenne des n dernier salaire perçu. si 
//     ce dernier n'a pas encore de salaire, on prendra son salaire de base (slaire contractuelle + prime)*/
//
//    private double searchSalaire(Compte cpt) {
//        double salaire = 0;
//        Calendar debut_M = Calendar.getInstance();
//        debut_M.setTime(parametreMutuelle.getDebutMois());
//        Calendar debutPeriod = Calendar.getInstance();
//        debutPeriod.set(Calendar.DAY_OF_MONTH, debut_M.get(Calendar.DAY_OF_MONTH));
//        int periodeMoy = (parametreMutuelle.getPeriodeSalaireMoyen() > 0 && parametreMutuelle.getPeriodeSalaireMoyen() < 24) ? parametreMutuelle.getPeriodeSalaireMoyen() : 3;
//        debutPeriod.set(Calendar.MONTH, -(periodeMoy + 1));
//        //recherche les salaire payé entre la date courante et celle  dans current_
//        champ = new String[]{"mutualiste", "debut", "fin"};
//        val = new Object[]{new YvsMutMutualiste(cpt.getMutualiste().getId()), debutPeriod.getTime(), new Date()};
//        List<YvsMutPaiementSalaire> ls = dao.loadNameQueries("YvsMutPaiementSalaire.findSalairePrecedent", champ, val);
//        if (!ls.isEmpty()) {
//            for (YvsMutPaiementSalaire p : ls) {
//                salaire += (p.getMontantPaye() + p.getSalaire());
//            }
//        } else {
//            salaire = findSalaireContrat(cpt);
//        }
//
//        return (salaire == 0) ? 0.01 : salaire;
//    }
//
//    private double findSalaireContrat(Compte cpt) {
//        champ = new String[]{"id"};
//        val = new Object[]{cpt.getId()};
//        YvsMutCompte y = (YvsMutCompte) dao.loadOneByNameQueries("YvsMutCompte.findById", champ, val);
//        if (y != null) {
////            List<YvsGrhContratEmps> l = y.getMutualiste().getEmploye().getEmploye().getYvsContratEmpsList();
////            YvsGrhContratEmps c = new YvsGrhContratEmps();
////            if (l != null) {
////                for (YvsGrhContratEmps co : l) {
////                    if (co.getContratPrincipal()) {
////                        c = co;
////                        break;
////                    } else {
////                        c = co;
////                    }
////                }
////            }
////            if (c.getSalaireMensuel() == null) {
////                if (c.getSalaireHoraire() != null) {
////                    c.setSalaireMensuel(c.getSalaireHoraire() * 30);
////                } else {
////                    c.setSalaireMensuel(0.0);
////                }
////            }
////            for (YvsGrhElementAdditionel e : c.getYvsGrhElementAdditionelList()) {
////                if (e.getPermanent()) {
////                    c.setSalaireMensuel(c.getSalaireMensuel() + e.getMontantElement());
////                }
////            }
////            return c.getSalaireMensuel();
//        }
//        return 0;
//    }
//
//    private void displayAllEmploye() {
//        YvsMutPaiementSalaire p;
//        int i = -1;
//        List<YvsMutMutualiste> listMutualistePaye = new ArrayList<>();
////        for (YvsMutPaiementSalaire pa : salaires) {
////            listMutualistePaye.add(pa.getMutualiste());
////        }
//        ManagedMutualiste w = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
//        if (w != null) {
//            for (YvsMutMutualiste m : w.getMutualistes()) {
//                if (!listMutualistePaye.contains(m)) {
//                    p = new YvsMutPaiementSalaire((long) i);
//                    p.setMutualiste(m);
////                    salaires.add(p);
//                    i--;
//                }
//            }
//        }
//    }
//
//    private void hideMutualisteNonPaye() {
//        List<YvsMutPaiementSalaire> l = new ArrayList<>();
////        for (YvsMutPaiementSalaire p : salaires) {
////            if (!p.getPayer()) {
////                l.add(p);
////            }
////        }
//        salaires.removeAll(l);
//    }
//
//    public void toogleAllMutualiste(ValueChangeEvent ev) {
//        if (ev != null) {
//            if ((Boolean) ev.getNewValue()) {
//                displayAllEmploye();
//            } else {
//                hideMutualisteNonPaye();
//
//            }
//        }
//    }
//
//    public void choiPeriodeSalaireRH(ValueChangeEvent ev) {
//        long id = (ev.getNewValue() != null) ? (long) ev.getNewValue() : 0;
//        if (id > 0) {
//            //construit la liste de salaire du mois
//            newPeriode.setPeriodeRh(UtilMut.buildPeriodeSalaire((listPeriodeSalaire.get(listPeriodeSalaire.indexOf(new YvsGrhOrdreCalculSalaire(id))))));
//        }
//
//    }
//
//    public void createNewPeriodeSalaireMut() {
//        if (newPeriode.getPeriodeRh().getId() > 0) {
//            YvsMutPeriodeSalaire enP = new YvsMutPeriodeSalaire();
//            //YvsGrhOrdreCalculSalaire salaire = listHeaderBulletin.get(listHeaderBulletin.indexOf(new YvsGrhOrdreCalculSalaire(newPeriode.getPeriodeRh().getId())));
//            enP.setActif(newPeriode.isActif());
//            enP.setCloture(newPeriode.isCloture());
//            enP.setMutuelle(currentMutuel);
//            enP.setAuthor(currentUser);
//            enP.setDateSave(new Date());
//            enP.setPeriodeRh(new YvsGrhOrdreCalculSalaire(newPeriode.getPeriodeRh().getId()));
//            if (newPeriode.isActif()) {
//                //désactive toutes les autres session de paiement de salaire
//                String rq = "UPDATE yvs_mut_periode_salaire SET actif=false";
//                dao.requeteLibre(rq, new Options[]{});
//            }
//            try {
//                enP = (YvsMutPeriodeSalaire) dao.save1(enP);
//            } catch (Exception ex) {
//                getErrorMessage("La session n'a pas été Enregistré. une session correspondant à cette période à été trouvé !");
//            }
//            newPeriode.setId(enP.getId());
//            periodesSalaires.add(enP);
//            //recherche et calcule le salaire à verser à chaque w.getMutualistes()
//            salaires.clear();
//            YvsMutPaiementSalaire enPs;
//            YvsMutPaiementSalaire p;
//            ManagedMutualiste w = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
//            if (w != null) {
//                for (YvsMutMutualiste mut : w.getMutualistes()) {
//                    p = loadDataMutualiste(mut, enP);
//                    enPs = p;
//                    enPs.setId(null);
//                    enPs.setDateSave(new Date());
//                    enPs = (YvsMutPaiementSalaire) dao.save1(enPs);
//                    p.setId(enPs.getId());
////                    salaires.add(p);
//                }
//            }
//            newPeriode = new PeriodeSalaireMut();
//        } else {
//            getErrorMessage("Vous devez choisir une période valide !");
//        }
//    }
//
//    private YvsMutPaiementSalaire loadDataMutualiste(YvsMutMutualiste m, YvsMutPeriodeSalaire order) {
//        YvsMutPaiementSalaire re = new YvsMutPaiementSalaire();
//        re.setMutualiste(m);
//        //recherche le bulletin du mutualiste dans la liste de bulletins contenu dans order.yvsBulletinList
//        YvsGrhBulletins b = findBulletinMutualiste(m, order.getPeriodeRh());
//        if (b != null) {
//            re.setMontantAPayer(getMontantBulletin(b));
//        }
//        //calcule l'epargne du mois
//        re.setEpargneDuMois(m.getMontantEpargne() + currentMutuel.getMontantEpargne());
//        re.setPayer(false);
//        re.setSoeAcompte(0);
//        re.setSoeCredit(0);
//        re.setMontantPaye(0.0);
//        re.setPeriode(order);
//        return re;
//    }
//
//    //récupère le bulletiin d'un mutualiste pour une période RH
//    private YvsGrhBulletins findBulletinMutualiste(YvsMutMutualiste m, YvsGrhOrdreCalculSalaire head) {
//        champ = new String[]{"head", "contrat"};
//        val = new Object[]{new YvsGrhOrdreCalculSalaire(head.getId()), new YvsGrhEmployes(m.getEmploye().getId())};
//        return (YvsGrhBulletins) dao.loadOneByNameQueries("YvsGrhBulletins.findByPeriode_", champ, val);
//    }
//
//    private List<AvanceSalaire> loadAllAvancePeriode(Mutualiste bean, Date dateFin) {
//        List<AvanceSalaire> re = new ArrayList<>();
//        String[] ch = new String[]{"mutualiste", "dateAvance", "etat"};
//        Object[] v = new Object[]{new YvsMutMutualiste(bean.getId()), dateFin, Constantes.ETAT_VALIDE};
//        List<YvsMutAvanceSalaire> l = dao.loadNameQueries("YvsMutAvanceSalaire.findAvanceEnCours", ch, v);
//        re = UtilMut.buildBeanListAvanceSalaire(l);
//        return re;
//    }
//
//    public void selectOneSessionSalaire(SelectEvent ev) {
//        selectedPeriode = (YvsMutPeriodeSalaire) ev.getObject();
////        salaires = selectedPeriode.getSalaires();
//    }
//
//    //activer /désactiver une nouvelle session
//    public void activerNewSession(PeriodeSalaireMut pe) {
//        //désactive toutes les autres session de paiement de salaire
//        String rq = "UPDATE yvs_mut_periode_salaire SET actif=false";
//        dao.requeteLibre(rq, new Options[]{});
//        rq = "UPDATE yvs_mut_periode_salaire SET actif=true WHERE id=?";
//        dao.requeteLibre(rq, new Options[]{new Options(pe.getId(), 1)});
//        for (YvsMutPeriodeSalaire p : periodesSalaires) {
//            p.setActif(false);
//        }
//        pe.setActif(true);
//        succes();
//    }
//
//    //recuperer Les Salaires dans une nouvelle session
////    public void recupererSalaireSession(YvsMutPeriodeSalaire pe) {
////        List<YvsMutMutualiste> l = new ArrayList<>();
////        for (YvsMutPaiementSalaire p : pe.getSalaires()) {
////            l.add(p.getMutualiste());
////        }
////        YvsMutPaiementSalaire enPs;
////        YvsMutPaiementSalaire p;
////        ManagedMutualiste w = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
////        if (w != null) {
////            for (YvsMutMutualiste mut : w.getMutualistes()) {
////                if (!l.contains(mut)) {
////                    p = loadDataMutualiste(mut, pe);
////                    enPs = p;
////                    enPs.setId(null);
////                    enPs.setDateSave(new Date());
////                    enPs = (YvsMutPaiementSalaire) dao.save1(enPs);
////                    p.setId(enPs.getId());
////                    pe.getSalaires().add(p);
////                }
////            }
////        }
////        succes();
////    }
//
//    //Supprimer les sessions selectionné
//    public void deleteSessions() {
//
//    }
//
//    @Override
//    public void unLoadOnView(UnselectEvent ev) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public void addParamMutualiste() {
//        ParametreRequete p = new ParametreRequete("y.mutualiste.employe", "mutualiste", null, "LIKE", "AND");
//        if (mutualisteSearch != null ? mutualisteSearch.trim().length() > 0 : false) {
//            p = new ParametreRequete(null, "mutualiste", mutualisteSearch, "LIKE", "AND");
//            p.getOtherExpression().add(new ParametreRequete("UPPER(y.mutualiste.employe.matricule)", "mutualiste", mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
//            p.getOtherExpression().add(new ParametreRequete("UPPER(y.mutualiste.employe.nom)", "mutualiste", mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
//            p.getOtherExpression().add(new ParametreRequete("UPPER(y.mutualiste.employe.prenom)", "mutualiste", mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
//        }
//        paginator.addParam(p);
//        loadAllPaiement(true, true);
//    }
//
//    public void addParamDates() {
//        ParametreRequete p = new ParametreRequete("y.datePaiement", "dates", null, "BETWEEN", "AND");
//        if (dateSearch) {
//            p = new ParametreRequete(null, "dates", dateSearch, "BETWEEN", "AND");
//            p.getOtherExpression().add(new ParametreRequete("y.dateSave", "dates", debutSearch, finSearch, "BETWEEN", "OR"));
//            p.getOtherExpression().add(new ParametreRequete("y.datePaiement", "dates", debutSearch, finSearch, "BETWEEN", "OR"));
//        }
//        paginator.addParam(p);
//        loadAllPaiement(true, true);
//    }
//
//    public void addParamType() {
//        ParametreRequete p = new ParametreRequete("y.periode", "w.periode", null, "LIKE", "AND");
//        if (typeSearch > 0) {
//            p = new ParametreRequete("y.periode", "periode", new YvsMutPeriodeSalaire(typeSearch), "=", "AND");
//        }
//        paginator.addParam(p);
//        loadAllPaiement(true, true);
//    }
//}
