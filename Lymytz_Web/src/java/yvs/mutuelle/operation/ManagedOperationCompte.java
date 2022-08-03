/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.operation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.dao.DaoInterfaceStateFull;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.mutuelle.YvsMutParametre;
import yvs.entity.mutuelle.YvsMutPeriodeExercice;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.mutuelle.credit.YvsMutReglementCredit;
import yvs.entity.mutuelle.echellonage.YvsMutMensualite;
import yvs.entity.mutuelle.echellonage.YvsMutReglementMensualite;
import yvs.entity.mutuelle.operation.YvsMutMouvementCaisse;
import yvs.entity.mutuelle.operation.YvsMutOperationCompte;
import yvs.grh.bean.Employe;
import yvs.mutuelle.Compte;
import yvs.mutuelle.ManagedMutualiste;
import yvs.mutuelle.Mutualiste;
import yvs.mutuelle.Mutuelle;
import yvs.mutuelle.Parametre;
import yvs.mutuelle.PeriodeExercice;
import yvs.mutuelle.UtilMut;
import yvs.mutuelle.credit.ManagedCredit;
import yvs.mutuelle.echellonage.ReglementMensualite;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedOperationCompte extends Managed<OperationCompte, YvsMutOperationCompte> implements Serializable {

    @EJB
    DaoInterfaceStateFull<YvsMutOperationCompte> ejb;

    @ManagedProperty(value = "#{operationCompte}")
    private OperationCompte operationCompte;

    private List<YvsMutOperationCompte> operations;
    private YvsMutOperationCompte selectedOperation;
    private List<YvsMutOperationCompte> historiques;
    private List<YvsMutCaisse> caisses;

    private Mutualiste mutualiste = new Mutualiste();
    private Compte compteEp = new Compte();
    private boolean updateOperation;
    private String tabIds, input_reset;
    private double montantEpargne, montantAssurance, montantMensualite;
    private String view;

    private long id = -11111111;
    private List<YvsMutOperationCompte> salaires;
    private List<YvsMutPeriodeExercice> periodes;
    private List<YvsBaseExercice> exercices;

    private List<YvsMutMensualite> mensualites;
    private List<YvsMutCompte> comptesEpargnes;

    public boolean init = true;
    private String sourceOp;

    private YvsMutReglementCredit reglementCredit;
    private YvsMutReglementMensualite reglementMensualite;

    public ManagedOperationCompte() {
        caisses = new ArrayList<>();
        operations = new ArrayList<>();
        salaires = new ArrayList<>();
        periodes = new ArrayList<>();
        exercices = new ArrayList<>();
        historiques = new ArrayList<>();
        mensualites = new ArrayList<>();
        comptesEpargnes = new ArrayList<>();
    }

    public YvsMutOperationCompte getSelectedOperation() {
        return selectedOperation;
    }

    public void setSelectedOperation(YvsMutOperationCompte selectedOperation) {
        this.selectedOperation = selectedOperation;
    }

    public List<YvsMutPeriodeExercice> getPeriodes() {
        return periodes;
    }

    public void setPeriodes(List<YvsMutPeriodeExercice> periodes) {
        this.periodes = periodes;
    }

    public List<YvsBaseExercice> getExercices() {
        return exercices;
    }

    public void setExercices(List<YvsBaseExercice> exercices) {
        this.exercices = exercices;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public Mutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(Mutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    public boolean isUpdateOperation() {
        return updateOperation;
    }

    public void setUpdateOperation(boolean updateOperation) {
        this.updateOperation = updateOperation;
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

    public OperationCompte getOperationCompte() {
        return operationCompte;
    }

    public void setOperationCompte(OperationCompte operationCompte) {
        this.operationCompte = operationCompte;
    }

    public List<YvsMutOperationCompte> getOperations() {
        return operations;
    }

    public void setOperations(List<YvsMutOperationCompte> operations) {
        this.operations = operations;
    }

    public List<YvsMutOperationCompte> getSalaires() {
        return salaires;
    }

    public void setSalaires(List<YvsMutOperationCompte> salaires) {
        this.salaires = salaires;
    }

    public List<YvsMutOperationCompte> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(List<YvsMutOperationCompte> historiques) {
        this.historiques = historiques;
    }

    public List<YvsMutMensualite> getMensualites() {
        return mensualites;
    }

    public void setMensualites(List<YvsMutMensualite> mensualites) {
        this.mensualites = mensualites;
    }

    public double getMontantEpargne() {
        return montantEpargne;
    }

    public void setMontantEpargne(double montantEpargne) {
        this.montantEpargne = montantEpargne;
    }

    public double getMontantAssurance() {
        return montantAssurance;
    }

    public void setMontantAssurance(double montantAssurance) {
        this.montantAssurance = montantAssurance;
    }

    public double getMontantMensualite() {
        return montantMensualite;
    }

    public void setMontantMensualite(double montantMensualite) {
        this.montantMensualite = montantMensualite;
    }

    public Compte getCompteEp() {
        return compteEp;
    }

    public void setCompteEp(Compte compteEp) {
        this.compteEp = compteEp;
    }

    public List<YvsMutCompte> getComptesEpargnes() {
        return comptesEpargnes;
    }

    public void setComptesEpargnes(List<YvsMutCompte> comptesEpargnes) {
        this.comptesEpargnes = comptesEpargnes;
    }

    public List<YvsMutCaisse> getCaisses() {
        return caisses;
    }

    public void setCaisses(List<YvsMutCaisse> caisses) {
        this.caisses = caisses;
    }

    public String getSourceOp() {
        return sourceOp;
    }

    public void setSourceOp(String sourceOp) {
        this.sourceOp = sourceOp;
    }

    public YvsMutReglementCredit getReglementCredit() {
        return reglementCredit;
    }

    public void setReglementCredit(YvsMutReglementCredit reglementCredit) {
        this.reglementCredit = reglementCredit;
    }

    public YvsMutReglementMensualite getReglementMensualite() {
        return reglementMensualite;
    }

    public void setReglementMensualite(YvsMutReglementMensualite reglementMensualite) {
        this.reglementMensualite = reglementMensualite;
    }

    @Override
    public void loadAll() {
        if (currentAgence.getSociete() != null) {
            currentMutuel = (YvsMutMutuelle) dao.loadOneByNameQueries("YvsMutMutuelle.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            if (currentMutuel != null ? currentMutuel.getParamsMutuelle() != null : false) {
                parametreMutuelle = UtilMut.buildBeanParametre(currentMutuel.getParamsMutuelle());
            } else {
                parametreMutuelle = new Parametre();
            }
            caisses = dao.loadNameQueries("YvsMutCaisse.findByMutuelle", new String[]{"mutuelle"}, new Object[]{currentMutuel});
            loadAllOperation(true);
            loadExercices();
        }

    }

    public void loadAllOperation(boolean avance) {
        addParam(new ParametreRequete("y.compte.mutualiste.mutuelle", "mutuelle", currentMutuel, "=", "AND"));
        operations = paginator.executeDynamicQuery("YvsMutOperationCompte", "y.periode.dateFin DESC, y.dateOperation DESC", avance, init, (int) imax, dao);
    }

    public void paginer(boolean next) {
        init = false;
        loadAllOperation(next);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        init = false;
        loadAllOperation(true);
    }

    public void loadExercices() {
        exercices = dao.loadNameQueries("YvsBaseExercice.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        if (currentMutuel == null) {
            currentMutuel = (YvsMutMutuelle) currentAgence.getMutuelle();
        }
    }

    public void loadPeriodes() {
        periodes = dao.loadNameQueries("YvsMutPeriodeExercice.findByExercice", new String[]{"exercice"}, new Object[]{currentExo});
        if (currentMutuel == null) {
            currentMutuel = (YvsMutMutuelle) currentAgence.getMutuelle();
        }
    }

    public void searchMutualiste(String matricule) {
        mutualiste.setId(0);
        mutualiste.setError(true);
        mutualiste.setEmploye(new Employe());
        ManagedMutualiste sevice = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
        if (sevice != null && (matricule != null) ? !matricule.isEmpty() : false) {
            Mutualiste y = sevice.searchMutualiste(matricule, true);
            if (sevice.getMutualistes() != null ? !sevice.getMutualistes().isEmpty() : false) {
                if (sevice.getMutualistes().size() > 1) {
                    update("data_mutualiste_cred");
                } else {
                    chooseMutualiste(y);
                }
                mutualiste.setError(false);
            }
        }
    }

    public void initMutualistes() {
        ManagedMutualiste service = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
        if (service != null) {
            service.initMutualistes(getMutualiste());
            update("dlg_blog_form_mutualiste_operation_op");
        }
    }

    public void loadSalaire(YvsMutPeriodeExercice p) {
        salaires.clear();
        if (p != null ? p.getId() > 0 : false) {
            List<YvsMutMutualiste> list = dao.loadNameQueries("YvsMutMutualiste.findByMutuelle", new String[]{"mutuelle"}, new Object[]{currentMutuel});
            YvsMutOperationCompte y;
            YvsMutCompte c;
            List<String> etats = new ArrayList<>();
            etats.add(Constantes.ETAT_ENCOURS);
            etats.add(Constantes.ETAT_ATTENTE);
            Double soeMens, rembMens, credit;
            List<Long> idsMens;
            List<YvsMutOperationCompte> operationsLies;
            for (YvsMutMutualiste m : list) {
                System.err.println(" Mutualiste "+m.getEmploye().getNom_prenom());
                c = (YvsMutCompte) dao.loadOneByNameQueries("YvsMutCompte.findByMutualisteType", new String[]{"mutualiste", "type"}, new Object[]{m, Constantes.MUT_TYPE_COMPTE_COURANT});
                if (c != null ? c.getId() > 0 : false) {
                    y = (YvsMutOperationCompte) dao.loadOneByNameQueries("YvsMutOperationCompte.findEpargnePeriode_", new String[]{"compte", "periode", "nature"}, new Object[]{c, p, Constantes.MUT_NATURE_OP_SALAIRE});
                    if (y != null ? y.getId() < 1 : true) {
                        y = new YvsMutOperationCompte(id++, c, p, 0D);
                        String numero = genererReference(yvs.util.Constantes.MUT_TRANSACTIONS_MUT, new Date());
                        if (numero != null ? numero.trim().length() < 1 : true) {
                            return;
                        }
                        y.setId(-m.getId());
                        y.setReferenceOperation(numero);
                        y.setAutomatique(false);
                        y.setCaisse(null);
                        y.setDateOperation(new Date());
                        y.setDateSave(new Date());
                        y.setEpargneMensuel(false);
                        y.setHeureOperation(new Date());
                        y.setNature(Constantes.MUT_NATURE_OP_SALAIRE);
                        y.setCompte(c);
                        y.setCommentaire("DEPOT DU SALAIRE DE LA PERIODE DE " + p.getReferencePeriode());
                        y.setTableSource(Constantes.MUT_TABLE_SRC_OPERATION_COMPTE);
                        y.setPeriode(p);
                        y.setSensOperation(Constantes.MUT_SENS_OP_DEPOT);
                    } else {
                        y.setSalaireSave(true);
                        if (y.getCodeOperation() != null) {
                            operationsLies = dao.loadNameQueries("YvsMutOperationCompte.findByCodeOp", new String[]{"codeOperation", "periode"}, new Object[]{y.getCodeOperation(), y.getPeriode()});
                            if (operationsLies != null) {
                                for (YvsMutOperationCompte op : operationsLies) {
                                    if (op.getNature().equals(Constantes.MUT_NATURE_OP_RETENUE_FIXE)) {
                                        y.setRetenueFixe(op.getMontant());
                                        y.setRetenuSave(true);
                                    } else if (op.getNature().equals(Constantes.MUT_NATURE_OP_EPARGNE)) {
                                        y.setEpargneSave(true);
                                        y.setMontantEpargne(op.getMontant());
                                    }
                                }
                            }
                            //Mensualité
                            credit = (Double) dao.loadObjectByNameQueries("YvsMutReglementMensualite.findTotalByCodeOp", new String[]{"codeOperation"}, new Object[]{y.getCodeOperation()});
                            y.getCompte().getMutualiste().setMontantCredit(credit != null ? credit : 0);
                            y.setMensualiteSave(credit != null ? credit > 0 : false);
                        }
                    }
                    idsMens = dao.loadNameQueries("YvsMutMensualite.findIdMensualiteEnCourPeriode_", new String[]{"etat", "mutualiste", "dateFin", "etatMens"}, new Object[]{etats, m, p.getDateFin(), Constantes.ETAT_REGLE});
                    if (idsMens.isEmpty()) {
                        idsMens.add(0L);
                    }
                    if (y.getId() <= 0) {
                        soeMens = (Double) dao.loadObjectByNameQueries("YvsMutMensualite.findSoeMensualiteFomIds", new String[]{"mensualites"}, new Object[]{idsMens});
                        rembMens = (Double) dao.loadObjectByNameQueries("YvsMutReglementMensualite.findSumVerseForMensualite", new String[]{"mensualites"}, new Object[]{idsMens});
                        soeMens = (soeMens != null) ? soeMens : 0;
                        rembMens = (rembMens != null) ? rembMens : 0;
                        y.getCompte().getMutualiste().setMontantCredit(soeMens - rembMens);
                        y.setRetenueFixe(currentMutuel.getRetenueFixe());
                        y.setMontantEpargne(m.getMontantEpargne());
                        y.setAuthor(currentUser);
                        y.setDateUpdate(new Date());
                    }
                    salaires.add(y);
                }
            }
        }
    }

    public void loadAllOperationByMutuelle(Mutuelle bean) {
        operations.clear();
        if (bean != null) {
            operations = dao.loadNameQueries("YvsMutOperationCompte.findByMutuelle", new String[]{"mutuelle"}, new Object[]{new YvsMutMutuelle(bean.getId())});
        }
    }

    public YvsMutOperationCompte buildOperationCompte(OperationCompte y) {
        YvsMutOperationCompte o = new YvsMutOperationCompte();
        if (y != null) {
            o.setDateOperation((y.getDateOperation() != null) ? y.getDateOperation() : new Date());
            o.setId(y.getId());
            o.setMontant(y.getMontant());
            o.setNature((y.getSensOperation().equals(Constantes.MUT_SENS_OP_DEPOT) ? Constantes.MUT_NATURE_OP_DEPOT : Constantes.MUT_NATURE_OP_RETRAIT));
            o.setCommentaire(y.getCommentaire());
            o.setHeureOperation((y.getHeureOperation() != null) ? y.getHeureOperation() : new Date());
            o.setAutomatique(y.isAutomatique());
            o.setDateSave(y.getDateSave());
            o.setCaisse(UtilMut.buildBeanCaisse(y.getCaisse()));
            o.setCompte(UtilMut.buildCompte(y.getCompte(), currentUser));
            o.setEpargneMensuel(false);
            o.setNew_(true);
            o.setPeriode(UtilMut.buildPeriode(y.getPeriode()));
            o.setReferenceOperation(y.getReferenceOperation());
            o.setTableSource(Constantes.MUT_TABLE_SRC_OPERATION_COMPTE);
            o.setSensOperation(y.getSensOperation());
        }
        return o;
    }

    @Override
    public OperationCompte recopieView() {
        OperationCompte o = new OperationCompte();
        o.setDateOperation((operationCompte.getDateOperation() != null) ? operationCompte.getDateOperation() : new Date());
        o.setId(operationCompte.getId());
        o.setMontant(operationCompte.getMontant());
        o.setCommentaire(operationCompte.getCommentaire());
        o.setHeureOperation((operationCompte.getHeureOperation() != null) ? operationCompte.getHeureOperation() : new Date());
        o.setAutomatique(operationCompte.isAutomatique());
        o.setCompte(operationCompte.getCompte());
        o.setCaisse(operationCompte.getCaisse());
        o.setNature(operationCompte.getNature());
        o.setSensOperation(operationCompte.getSensOperation());
        o.setPeriode(operationCompte.getPeriode());
        o.setDateSave(operationCompte.getDateSave());
        o.setNew_(true);
        return o;
    }

    private boolean autoriserRetraitCompteEpargne(OperationCompte op) {
        if (op.getSensOperation().equals(Constantes.MUT_SENS_OP_RETRAIT)) {
            // si le compte choisi est un compte epargne 
            if (op.getCompte().getType().getNature().equals(Constantes.MUT_TYPE_COMPTE_EPARGNE)) {
                if (!parametreMutuelle.isAcceptRetraitEpargne()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean controleFiche(OperationCompte bean) {
        if (bean.getCompte().getId() <= 0) {
            getErrorMessage("Vous devez specifier le compte du mutualiste !");
            return false;
        }
        if (bean.getCaisse().getId() <= 0) {
            getErrorMessage("Vous devez specifier la caisse !");
            return false;
        }
        if (bean.getMontant() <= 0) {
            getErrorMessage("Vous devez  entrer un montant !");
            return false;
        }
        if (bean.getPeriode().getId() <= 0) {
            getErrorMessage("Vous devez  spécifier la période !");
            return false;
        }
        if (!autoriserRetraitCompteEpargne(bean)) {
            getErrorMessage("Les retraits du compte epargne ne sont pas autorisés !");
            return false;
        }
        //contrôle le solde
        if (bean.getSensOperation().equals(Constantes.MUT_SENS_OP_RETRAIT)) {
            if (bean.getCompte().getSolde() < bean.getMontant()) {
                getErrorMessage("Le solde de ce compte est insuffisant");
                return false;
            }
        }
        //la référence
        String ref = genererReference(Constantes.MUT_TRANSACTIONS_MUT, bean.getDateOperation(), bean.getCaisse().getId(), Constantes.CAISSE);
        if ((ref != null) ? ref.trim().equals("") : true) {
            return false;
        }
        bean.setReferenceOperation(ref);
        return true;
    }

    @Override
    public void resetFiche() {
        operationCompte.setDateOperation(new Date());
        operationCompte.setId(0);
        operationCompte.setNature(null);
        operationCompte.setSensOperation(Constantes.MUT_SENS_OP_DEPOT);
        operationCompte.setCompte(new Compte());
        operationCompte.setCommentaire("");
        operationCompte.setHeureOperation(new Date());
        operationCompte.setAutomatique(false);
        operationCompte.setPeriode(new PeriodeExercice());
        mutualiste = new Mutualiste();
        mutualiste.setComptes(new ArrayList<YvsMutCompte>());
        historiques.clear();
        setUpdateOperation(false);
        update("blog_form_operation_compte");
    }

    @Override
    public void populateView(OperationCompte bean) {
        operationCompte.setDateOperation((bean.getDateOperation() != null) ? bean.getDateOperation() : new Date());
        operationCompte.setId(bean.getId());
        operationCompte.setMontant(bean.getMontant());
        operationCompte.setNature(bean.getNature());
        operationCompte.setSensOperation(Constantes.MUT_SENS_OP_DEPOT);
        operationCompte.setCompte(bean.getCompte());
        operationCompte.setCommentaire(bean.getCommentaire());
        operationCompte.setHeureOperation(bean.getHeureOperation());
        operationCompte.setAutomatique(bean.isAutomatique());
        operationCompte.setPeriode(bean.getPeriode());
        operationCompte.setReferenceOperation(bean.getReferenceOperation());
        Compte c = bean.getCompte();
        YvsMutCompte e = (YvsMutCompte) dao.loadOneByNameQueries("YvsMutCompte.findById", new String[]{"id"}, new Object[]{c.getId()});
        Mutualiste mut = UtilMut.buildBeanMutualiste(e.getMutualiste());
        cloneObject(mutualiste, mut);
        mutualiste.setComptes(e.getMutualiste().getComptes());
        setUpdateOperation(true);
    }
    boolean msg = true;

    @Override
    public boolean saveNew() {
        OperationCompte bean = recopieView();
        String action = bean.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(bean)) {
                YvsMutOperationCompte entity = buildOperationCompte(bean);
                entity.setDateUpdate(new Date());
                entity.setAuthor(currentUser);
                if (bean.getId() <= 0) {
                    entity.setId(null);
                    entity = (YvsMutOperationCompte) dao.save1(entity);
                    bean.setId(entity.getId());
                    operationCompte.setId(entity.getId());
                    operations.add(0, entity);
                    if (msg) {
                        if (historiques.size() < 15) {
                            historiques.add(0, entity);
                        } else if (!historiques.isEmpty()) {
                            historiques.set(0, entity);
                        }
                    }
                } else {
                    dao.update(entity);
                    int idx = operations.indexOf(entity);
                    if (idx >= 0) {
                        operations.set(idx, entity);
                    }
                    if (msg) {
                        if (!historiques.contains(entity)) {
                            historiques.add(0, entity);
                        } else if (!historiques.isEmpty()) {
                            historiques.set(historiques.indexOf(entity), entity);
                        }
                    }
                }
                if (msg) {
                    succes();
                }
                actionOpenOrResetAfter(this);
                update("data_operation_compte");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? tabIds.length() > 0 : false) {
                String[] ids = tabIds.split("-");
                if ((ids != null) ? ids.length > 0 : false) {
                    for (String s : ids) {
                        long id = Integer.valueOf(s);
                        YvsMutOperationCompte bean = operations.get(operations.indexOf(new YvsMutOperationCompte(id)));
                        dao.delete(new YvsMutOperationCompte(bean.getId()));
                        operations.remove(bean);
                    }
                    resetFiche();
                    succes();
                    update("data_operation_compte");
                    update("blog_form_operation_compte");
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Mutuelle Error...", ex);
        }
    }

    public void openToDeleteLine(YvsMutOperationCompte y, boolean details) {
        selectedOperation = y;
        onSelectObject(y);
        if (!details) {
            openDialog("dlgDelOperation");
        } else {
            switch (y.getTableSource()) {
                case Constantes.MUT_TABLE_SRC_OPERATION_COMPTE:
                    if (selectedOperation.getSensOperation().equals(Constantes.MUT_SENS_OP_DEPOT)) {
                        sourceOp = "DEPOT DANS LE COMPTE";
                    } else {
                        sourceOp = "RETRAIT DU COMPTE";
                    }
                    break;
                case Constantes.MUT_TABLE_SRC_REGLEMENT_CREDIT:
                    sourceOp = "DEPOT POUR PAIEMENT DU CREDIT ACCORDE";
                    reglementCredit = (YvsMutReglementCredit) dao.loadNameQueries("YvsMutReglementCredit.findById", new String[]{"id"}, new Object[]{selectedOperation.getSouceReglement()});
                    if (reglementCredit == null) {
                        reglementCredit = new YvsMutReglementCredit();
                    }
                    break;
                case Constantes.MUT_TABLE_SRC_REGLEMENT_MENSUALITE:
                    sourceOp = "RETRAIT POUR PAIEMENT DE MENSUALITES";
                    reglementMensualite = (YvsMutReglementMensualite) dao.loadNameQueries("YvsMutReglementMensualite.findById", new String[]{"id"}, new Object[]{selectedOperation.getSouceReglement()});
                    ;
                    if (reglementMensualite == null) {
                        reglementMensualite = new YvsMutReglementMensualite();
                    }
                    break;
            }
            openDialog("dlgDetails");
            update("pan_display_detail");
        }
    }

    public void deleteLineOperation() {
        try {
            if (selectedOperation != null) {
                if (selectedOperation.getId() <= 0) {
                    dao.delete(selectedOperation);
                    operations.remove(selectedOperation);
                    resetFiche();
                    succes();
                    update("data_operation_compte");
                    update("blog_form_operation_compte");
                } else {
                    getErrorMessage("Vous ne pouvez supprimer cette ligne d'opération", "Veillez agir sur la ligne source");
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Lymytz Mutuelle>>> ", ex);
        }
    }

    @Override
    public void updateBean() {
        tabIds = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tabIds");
        if ((tabIds != null) ? tabIds.length() > 0 : false) {
            String[] ids = tabIds.split("-");
            setUpdateOperation((ids != null) ? ids.length > 0 : false);
            if (isUpdateOperation()) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                YvsMutOperationCompte bean = operations.get(operations.indexOf(new YvsMutOperationCompte(id)));
                populateView(UtilMut.buildBeanOperationCompte(bean));
                tabIds = "";
                input_reset = "";
            } else {
                resetFiche();
            }
        } else {
            resetFiche();
        }
        update("blog_form_operation_compte");
    }

    public void chooseMutuelle() {
        update("data_mutualiste");
    }

    public void choosePeriode(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            int idx = periodes.indexOf(new YvsMutPeriodeExercice((long) ev.getNewValue()));
            if (idx > -1) {
                operationCompte.setPeriode(UtilMut.buildPeriodeMutuelle(periodes.get(idx)));
                loadSalaire(periodes.get(idx));
            }
        }
    }

    public void chooseCompteEpargne(SelectEvent ev) {
        if (ev.getObject() != null) {
            compteEp = UtilMut.buildBeanCompte((YvsMutCompte) ev.getObject());
        }
    }

    public void loadOnViewMutualiste(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutMutualiste bean = (YvsMutMutualiste) ev.getObject();
            chooseMutualiste(UtilMut.buildBeanMutualiste(bean));
        }
    }

    private YvsMutCompte giveCompteEpargne(YvsMutMutualiste m) {
        for (YvsMutCompte c : m.getComptes()) {
            if (c.getTypeCompte().getNature().equals(Constantes.MUT_TYPE_COMPTE_EPARGNE)) {
                return c;
            }
        }
        return null;
    }

    public void chooseMutualiste(Mutualiste e) {
        if (e != null) {
            cloneObject(mutualiste, e);
            historiques = dao.loadNameQueries("YvsMutOperationCompte.findByMutualiste", new String[]{"mutualiste"}, new Object[]{new YvsMutMutualiste(e.getId())}, 0, 15);
            //charge les infos utiles     
            //Solde compte Assurance
            mutualiste.setSoldeAssurance(findSoldeMutualisteByNature(e.getId(), Constantes.MUT_TYPE_COMPTE_ASSURANCE));
            setMontantAssurance(currentMutuel.getMontantAssurance() - mutualiste.getSoldeAssurance());
            comptesEpargnes.clear();
            for (YvsMutCompte c : e.getComptes()) {
                if (c.getTypeCompte().getNature().equals(Constantes.MUT_TYPE_COMPTE_EPARGNE)) {
                    comptesEpargnes.add(c);
                }
            }
            if (comptesEpargnes.size() > 1) {
                // open dialogue epargne
                openDialog("dlgCompteEp");
            } else if (comptesEpargnes.size() > 0) {
                compteEp = UtilMut.buildBeanCompte(comptesEpargnes.get(0));
            }
//charge les mensualité
            loadMontant();
            //charge la période
            YvsMutPeriodeExercice pe = loadPeriode(operationCompte.getDateOperation());
            if (pe != null) {
                operationCompte.setPeriode(UtilMut.buildPeriodeMutuelle(pe));
            }
            update("blog_form_operation_compte");
        }
    }

    public void loadMontant() {
        List<String> etats = new ArrayList<>();
        etats.add(Constantes.ETAT_ENCOURS);
        etats.add(Constantes.ETAT_ATTENTE);
        mensualites = dao.loadNameQueries("YvsMutMensualite.findMensualiteEnCour_", new String[]{"mutualiste", "dateFin", "etat", "etatMens"},
                new Object[]{new YvsMutMutualiste(mutualiste.getId()), operationCompte.getPeriode().getDateFin(), etats, Constantes.ETAT_REGLE});
        Double re = 0d;
        for (YvsMutMensualite m : mensualites) {
            re += m.getMontantReste();
        }
        mutualiste.setMontantCredit(re);
        setMontantMensualite(mutualiste.getMontantCredit());
        //montant epargne
        //récupération de l'epargne de la période
        re = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findSommeTypeOperationByNaturePeriode", new String[]{"mutualiste", "nature", "mouvement", "debut", "fin"},
                new Object[]{new YvsMutMutualiste(mutualiste.getId()), Constantes.MUT_TYPE_COMPTE_EPARGNE, Constantes.MUT_SENS_OP_DEPOT, operationCompte.getPeriode().getDateDebut(), operationCompte.getPeriode().getDateFin()});
        re = (re != null) ? re : 0;
        setMontantEpargne((mutualiste.getMontantEpargne() - re) > 0 ? (mutualiste.getMontantEpargne() - re) : 0);

        update("data_mensualite_echeancier_pa");
        update("txt_op_total_credit");
    }

//    private double soldeCompteAssurance(Mutualiste mu) {
//        double re = 0;
//        if (mu != null) {
//            YvsMutCompte cp = (YvsMutCompte) dao.loadOneByNameQueries("YvsMutCompte.findByMutualisteType", new String[]{"mutualiste", "type"}, new Object[]{new YvsMutMutualiste(mu.getId()), Constantes.MUT_TYPE_COMPTE_ASSURANCE});
//            if (cp != null) {
//                return dao.getSoldeCompteMutualiste(cp.getId());
//            }
//        }
//        return re;
//    }
    public void chooseCompte(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            Long id = (Long) ev.getNewValue();
            if (id > 0) {
                operationCompte.setCompte(UtilMut.buildBeanCompte(mutualiste.getComptes().get(mutualiste.getComptes().indexOf(new YvsMutCompte(id)))));
                //calcul solde compte
                operationCompte.getCompte().setSolde(dao.getSoldeCompteMutualiste(id));
            }
        }
    }

    public void chooseCaisse(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            Long id = (Long) ev.getNewValue();
            if (id > 0) {
                operationCompte.setCaisse(UtilMut.buildBeanCaisse(caisses.get(caisses.indexOf(new YvsMutCaisse(id)))));
                //calcul solde compte
//                operationCompte.getCompte().setSolde(dao.getSoldeCompteMutualiste(id));
            }
        }
    }

    private YvsMutPeriodeExercice loadPeriode(Date d) {
        YvsMutPeriodeExercice pe = (YvsMutPeriodeExercice) dao.loadOneByNameQueries("YvsMutPeriodeExercice.findPeriode_", new String[]{"date", "societe"}, new Object[]{d, currentAgence.getSociete()});
        return pe;
    }

    public void chooseDate(SelectEvent ev) {
        if (ev.getObject() != null) {
            Date d = (Date) ev.getObject();
            YvsMutPeriodeExercice pe = loadPeriode(d);
            if (pe != null) {
                operationCompte.setPeriode(UtilMut.buildPeriodeMutuelle(pe));
            }
            loadMontant();
        }
    }

    public void chooseOnePeriode(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            Long id = (Long) ev.getNewValue();
            int idx = giveExerciceActif(operationCompte.getDateOperation()).getPeriodesMutuelles().indexOf(new YvsMutPeriodeExercice(id));
            if (idx >= 0) {
                operationCompte.setPeriode(UtilMut.buildPeriodeMutuelle(giveExerciceActif(operationCompte.getDateOperation()).getPeriodesMutuelles().get(idx)));
            }
            loadMontant();
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutOperationCompte bean = (YvsMutOperationCompte) ev.getObject();
            populateView(UtilMut.buildBeanOperationCompte(bean));
            historiques = dao.loadNameQueries("YvsMutOperationCompte.findByMutualiste", new String[]{"mutualiste"}, new Object[]{bean.getCompte().getMutualiste()}, 0, 15);
            update("blog_form_operation_compte");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("blog_form_operation_compte");
    }

    public void updateSalaire(CellEditEvent ev) {
        if (operationCompte.getPeriode().getId() > 0) {
            int line = ev.getRowIndex();
            Double valu = 0d;
            Long idCompte = -1L;
            if (line >= 0) {
                YvsMutOperationCompte c = salaires.get(line);
                c.setCaisse(null);
                long id = c.getId();
                if (id > 0) {
                    if (c.getMontant() > 0) {
                        dao.update(c);
                    } else {
                        try {
                            dao.delete(c);
                        } catch (Exception ex) {
                            getErrorMessage("Opération impossible");
                            getException("Mutuelle error ", ex);
                            return;
                        }
                    }
                } else {
                    if (ev.getColumn().getClientId().endsWith("col_sal_montant")) {
                        valu = (Double) ev.getNewValue();
                        c.setMontant(valu);
                    } else {
                        idCompte = (Long) ev.getNewValue();
                        int idx = c.getCompte().getMutualiste().getComptes().indexOf(new YvsMutCompte(id));
                        if (idx >= 0) {
                            c.setCompte(c.getCompte().getMutualiste().getComptes().get(idx));
                        }
                    }
                    if (c.getMontant() > 0 && c.getCompte().getId() > 0) {
                        c.setId(null);
                        c = (YvsMutOperationCompte) dao.save1(c);
                    } else {
                        getWarningMessage(" !!!! ");
                    }
                }
                salaires.set(line, c);
                succes();
            }
        } else {
            getErrorMessage("Vous devez selectionner une période !");
        }
    }

    public void saveOnlyEpargne(YvsMutOperationCompte op) {
        if (op.getId() > 0 && op.getMontantEpargne() > 0) {
            operationCompte.setCompte(UtilMut.buildBeanCompte(op.getCompte()));
            compteEp = UtilMut.buildBeanCompte(giveCompteEpargne(op.getCompte().getMutualiste()));
            if ((op.getMontant() - op.getRetenueFixe() - op.getMontantEpargne() - op.getCompte().getMutualiste().getMontantCredit()) >= 0) {
                if (saveEpargneFromCompte(Constantes.MUT_NATURE_OP_EPARGNE, op.getMontantEpargne(), op.getMontantEpargne(), false, false, op.getCodeOperation())) {
                    op.setEpargneSave(true);
                    succes();
                }
            } else {
                getErrorMessage("Une valeur entrée met le net à payer à 0", "Le net à payer ne peut être négatif");
            }
        }
    }

    public void saveOnlyRetenu(YvsMutOperationCompte op) {
        if (op.getId() > 0 && op.getRetenueFixe() > 0) {
            operationCompte.setCompte(UtilMut.buildBeanCompte(op.getCompte()));
            if ((op.getMontant() - op.getRetenueFixe() - op.getMontantEpargne() - op.getCompte().getMutualiste().getMontantCredit()) >= 0) {
                if (saveRetenueFixe(op.getRetenueFixe(), op.getCompte(), op.getCodeOperation())) {
                    op.setRetenuSave(true);
                    succes();
                }
            } else {
                getErrorMessage("Une valeur entrée met le net à payer à 0", "Le net à payer ne peut être négatif");
            }
        }
    }

    public void saveOnlyMensualite(YvsMutOperationCompte op) {
        List<String> etats = new ArrayList<>();
        etats.add(Constantes.ETAT_ENCOURS);
        etats.add(Constantes.ETAT_ATTENTE);
        if (op.getId() > 0 && op.getCompte().getMutualiste().getMontantCredit() > 0) {
            operationCompte.setCompte(UtilMut.buildBeanCompte(op.getCompte()));
            compteEp = UtilMut.buildBeanCompte(giveCompteEpargne(op.getCompte().getMutualiste()));
            if ((op.getMontant() - op.getRetenueFixe() - op.getMontantEpargne() - op.getCompte().getMutualiste().getMontantCredit()) >= 0) {
                if (saveMensualite(op.getCompte().getMutualiste().getMontantCredit(), dao.loadNameQueries("YvsMutMensualite.findMensualiteEnCour_", new String[]{"mutualiste", "dateFin", "etat", "etatMens"},
                        new Object[]{op.getCompte().getMutualiste(), operationCompte.getPeriode().getDateFin(), etats, Constantes.ETAT_REGLE}), op.getCodeOperation())) {
                    op.setMensualiteSave(true);
                    succes();
                }
            } else {
                getErrorMessage("Une valeur entrée met le net à payer à 0", "Le net à payer ne peut être négatif");
            }
        }
    }

    public void saveSalaire() {
        if (operationCompte.getPeriode().getId() > 0 && !operationCompte.getPeriode().isCloture()) {
            List<String> etats = new ArrayList<>();
            etats.add(Constantes.ETAT_ENCOURS);
            etats.add(Constantes.ETAT_ATTENTE);
            double salaire;
            String codeOp;
            for (YvsMutOperationCompte c : salaires) {
                codeOp = System.currentTimeMillis() + "";
                if (c.getMontant() > 0 && c.getId() <= 0) {
                    operationCompte.setCompte(UtilMut.buildBeanCompte(c.getCompte()));
                    compteEp = UtilMut.buildBeanCompte(giveCompteEpargne(c.getCompte().getMutualiste()));
                    if (saveEpargneFromCompte(Constantes.MUT_NATURE_OP_EPARGNE, c.getMontantEpargne(), c.getMontant(), true, true, codeOp)) {
                        c.setSalaireSave(true);
                        salaire = c.getMontant();
                        if (salaire >= c.getRetenueFixe() && c.getRetenueFixe() > 0) {
                            if (saveRetenueFixe(c.getRetenueFixe(), c.getCompte(), codeOp)) {
                                c.setRetenuSave(true);
                                salaire -= c.getRetenueFixe();
                            }
                        }
                        operationCompte.setCompte(UtilMut.buildBeanCompte(c.getCompte()));
                        compteEp = UtilMut.buildBeanCompte(giveCompteEpargne(c.getCompte().getMutualiste()));
                        if (salaire >= c.getCompte().getMutualiste().getMontantCredit() && c.getCompte().getMutualiste().getMontantCredit() > 0) {
                            if (saveMensualite(c.getCompte().getMutualiste().getMontantCredit(), dao.loadNameQueries("YvsMutMensualite.findMensualiteEnCour_", new String[]{"mutualiste", "dateFin", "etat", "etatMens"},
                                    new Object[]{c.getCompte().getMutualiste(), operationCompte.getPeriode().getDateFin(), etats, Constantes.ETAT_REGLE}), codeOp)) {
                                c.setMensualiteSave(true);
                                salaire -= c.getCompte().getMutualiste().getMontantCredit();
                            }
                        } else {
                            if (c.getCompte().getMutualiste().getMontantCredit() > 0) {
                                if (saveMensualite(salaire, dao.loadNameQueries("YvsMutMensualite.findMensualiteEnCour_", new String[]{"mutualiste", "dateFin", "etat", "etatMens"},
                                        new Object[]{c.getCompte().getMutualiste(), operationCompte.getPeriode().getDateFin(), etats, Constantes.ETAT_REGLE}), codeOp)) {
                                    c.setMensualiteSave(true);
                                    salaire = 0;
                                }
                            }
                        }
                        operationCompte.setCompte(UtilMut.buildBeanCompte(c.getCompte()));
                        compteEp = UtilMut.buildBeanCompte(giveCompteEpargne(c.getCompte().getMutualiste()));
                        if (salaire > c.getMontantEpargne() && c.getMontantEpargne() > 0) {
                            if (saveEpargneFromCompte(Constantes.MUT_NATURE_OP_EPARGNE, c.getMontantEpargne(), c.getMontant(), false, false, codeOp)) {
                                c.setEpargneSave(true);
                            }
                        }
                    }
                    //Pour eviter les double enregistrement
                    c.setId(c.getId() * (-1));
                } else {
                    System.err.println(" --- Pas de salaire ou salaire déjà payé !");
                }
            }
            succes();
        } else {
            if (operationCompte.getPeriode().getId() <= 0) {
                getErrorMessage("Aucune période n'a été selectionné !");
            } else {
                getErrorMessage("Cette période a déjà été cloturé !");
            }
        }
    }

    public boolean saveRetenueFixe(double montant, YvsMutCompte compte, String codeOp) {
        // Retrait du compte courant
        YvsMutCaisse caiss = (YvsMutCaisse) dao.loadOneByNameQueries("YvsMutCaisse.findCaissePrincipale", new String[]{"mutuelle"}, new Object[]{currentMutuel});
        String ref = genererReference(Constantes.MUT_TRANSACTIONS_MUT, operationCompte.getDateOperation(), 0);
        if ((ref != null) ? ref.trim().equals("") : true) {
            ref = null;
        }
        operationCompte.setReferenceOperation(ref);
        if (operationCompte.getCompte().getId() > 0 && caiss != null && ref != null) {
            YvsMutOperationCompte retrait = new YvsMutOperationCompte();
            retrait.setAuthor(currentUser);
            retrait.setDateSave(new Date());
            retrait.setDateUpdate(new Date());
            retrait.setCommentaire("RETRAIT POUR  RETENUE FIXE MENSUELLE");
            retrait.setCaisse(null);
            retrait.setId(null);
            retrait.setAutomatique(true);
            retrait.setCompte(compte);
            retrait.setDateOperation(operationCompte.getDateOperation());
            retrait.setEpargneMensuel(false);
            retrait.setHeureOperation(new Date());
            retrait.setMontant(montant);
            retrait.setNature(Constantes.MUT_NATURE_OP_RETENUE_FIXE);
            retrait.setPeriode(new YvsMutPeriodeExercice(operationCompte.getPeriode().getId()));
            retrait.setSensOperation(Constantes.MUT_SENS_OP_RETRAIT);
            retrait.setTableSource(Constantes.MUT_TABLE_SRC_OPERATION_COMPTE);
            retrait.setReferenceOperation(null);
            retrait.setReferenceOperation(ref);
            //Le mouvement de caisse
            YvsMutMouvementCaisse mvt = new YvsMutMouvementCaisse();
            mvt.setAuthor(currentUser);
            mvt.setCaisse(caiss);
            mvt.setCaissier(currentUser.getUsers());
            mvt.setDateMvt(operationCompte.getDateOperation());
            mvt.setDateSave(new Date());
            mvt.setDateUpdate(new Date());
            mvt.setInSoldeCaisse(false);
            mvt.setMontant(montant);
            mvt.setMouvement(Constantes.MUT_SENS_OP_DEPOT);
            mvt.setNameTiers(compte.getMutualiste().getEmploye().getNom_prenom());
            mvt.setNote("PAIEMENT DE LA RETENU FIXE DE XXX");
            mvt.setStatutPiece(Constantes.STATUT_DOC_PAYER);
            mvt.setTiersInterne(compte.getMutualiste());
            mvt.setTableExterne(Constantes.MUT_TABLE_SRC_OPERATION_COMPTE);
            mvt.setReferenceExterne(ref);
            mvt.setNumero(ref);
            retrait.setCodeOperation(codeOp);
            return ejb.saveTransactionRetenue(retrait, mvt);
        } else {
            if (ref == null) {
                getErrorMessage("La référence de l'opération n'a pas pu être généré !");
            } else {
                getErrorMessage("Aucune caisse principale n'a été paramétré !");
            }

        }
        return false;
    }

    public boolean saveEpargneFromCompte(String nature, double montantRetrait, double montantDepot, boolean salaire, boolean saveOnlyDepot, String codeOp) {
        //Enregistrer la sortie du compte selectionné        
        operationCompte.setId(0);
        String texte = (nature.equals(Constantes.MUT_NATURE_OP_EPARGNE) ? "EPARGNE" : "ASSURANCE");
//        if (operationCompte.getCompte().getSolde() > montantRetrait && !salaire) {
//            getErrorMessage("Solde insuffissant !");
//            return false;
//        }
        ManagedEpargne service = (ManagedEpargne) giveManagedBean(ManagedEpargne.class);
        if (service != null) {
            service.recopieValueBean(montantRetrait, operationCompte.getCompte(), operationCompte.getDateOperation(), operationCompte.getPeriode());
            if ((service.controleFiche_(service.getOperationCompte()) && compteEp.getId() > 0) || saveOnlyDepot) {
                //retrait du compte courant
                service.getOperationCompte().setNature(Constantes.MUT_NATURE_OP_RETRAIT);
                service.getOperationCompte().setSensOperation(Constantes.MUT_SENS_OP_RETRAIT);
                YvsMutOperationCompte retrait = null;
                if (!saveOnlyDepot) {
                    retrait = service.buildOperationCompte(service.getOperationCompte());
                    retrait.setAuthor(currentUser);
                    retrait.setDateSave(new Date());
                    retrait.setDateUpdate(new Date());
                    retrait.setCommentaire("RETRAIT POUR  L'" + texte);
                    retrait.setCaisse(null);
                    retrait.setId(null);
                    retrait.setCodeOperation(codeOp);
                }
                // Dépôt dans le compte Courant
                YvsMutOperationCompte depotCC = null;
                if (salaire) {
                    service.getOperationCompte().setSensOperation(Constantes.MUT_SENS_OP_DEPOT);
                    service.getOperationCompte().setCompte(operationCompte.getCompte());
                    service.getOperationCompte().setNature(Constantes.MUT_NATURE_OP_SALAIRE);
                    service.getOperationCompte().setMontant(montantDepot);
                    depotCC = service.buildOperationCompte(service.getOperationCompte());
                    depotCC.setAuthor(currentUser);
                    depotCC.setDateSave(new Date());
                    depotCC.setDateUpdate(new Date());
                    depotCC.setCommentaire("DEPOT DU SALAIRE DE LA PERIODE DE " + operationCompte.getPeriode().getReference());
                    depotCC.setCaisse(null);
                    depotCC.setId(null);
                    depotCC.setCodeOperation(codeOp);
                }
                //Dépôt dans le compte epargne
                service.getOperationCompte().setSensOperation(Constantes.MUT_SENS_OP_DEPOT);
                service.getOperationCompte().setCompte(compteEp);
                service.getOperationCompte().setNature(nature);
                service.getOperationCompte().setMontant(montantRetrait);
                YvsMutOperationCompte epargne = null;
                if (!saveOnlyDepot) {
                    epargne = service.buildOperationCompte(service.getOperationCompte());
                    epargne.setAuthor(currentUser);
                    epargne.setDateSave(new Date());
                    epargne.setDateUpdate(new Date());
                    epargne.setCommentaire(texte + " DE LA PERIODE DE " + operationCompte.getPeriode().getReference());
                    epargne.setCaisse(null);
                    epargne.setId(null);
                    epargne.setCodeOperation(codeOp);
                }
                if (ejb.saveTransactionEpargne(retrait, epargne, depotCC)) {
                    historiques.add(0, epargne);
                    historiques.add(0, retrait);
                    if (!salaire) {
                        succes();
                    }
                    return true;
                } else {
                    getErrorMessage("Opération non réussi !");
                }
            } else {
                getErrorMessage("Formulaire incorrecte ! Assuré-vous que ce mutualiste dispose d'un compte epargne");
            }
        }
        return false;
    }

//    private YvsMutCaisse getCaisseVirtuel() {
//        for (YvsMutCaisse c : currentMutuel.getCaisses()) {
//            if (c.getTypeDeFlux().equals(Constantes.MUT_TYPE_MONAIE_SCRIPTURALE)) {
//                return c;
//            }
//        }
//        return null;
//    }
    private ReglementMensualite getReglementMensualite(ManagedCredit service, YvsMutMensualite mens) {
        service.setReglement(new ReglementMensualite());
        service.getReglement().setModePaiement(Constantes.MUT_MODE_PAIEMENT_COMPTE);
        service.getReglement().setMontant(mens.getMontantReste());
        service.getReglement().setCompte(operationCompte.getCompte());
//        service.getReglement().setCaisse(UtilMut.buildBeanCaisse(getCaisseVirtuel()));
        service.setEcheancierSelect(mens.getEchellonage());
        service.setCreditSelect(mens.getEchellonage().getCredit());
        service.setCredit(UtilMut.buildSimpleBeanCredit(service.getCreditSelect()));
        service.getCredit().setEcheancier(UtilMut.buildBeanSimpleEchellonage(mens.getEchellonage()));
        service.getCredit().getEcheancier().setMensualites(dao.loadNameQueries("YvsMutMensualite.findByEchellonage", new String[]{"echellonage"}, new Object[]{mens.getEchellonage()}));
        service.setMensualite(UtilMut.buildBeanMensualite(mens));
        return service.getReglement();
    }

    public void saveAllMensualiteFromCompte() {
        if (mutualiste.getMontantCredit() <= operationCompte.getCompte().getSolde()) {
            ManagedCredit service = (ManagedCredit) giveManagedBean(ManagedCredit.class);
            if (service != null) {
                for (YvsMutMensualite mens : mensualites) {
                    saveMensualiteFromCompte(mens, false);
                }
            }
        } else {
            getErrorMessage("Solde insuffisant !");
        }

    }

    public boolean saveMensualite(double totalSoe, List<YvsMutMensualite> mensualites, String codeOp) {
        double montant = totalSoe;
        ManagedCredit service = (ManagedCredit) giveManagedBean(ManagedCredit.class);
        if (service != null) {
            service.setParametreMutuelle(UtilMut.buildBeanParametre((YvsMutParametre) dao.loadOneByNameQueries("YvsMutParametre.findByMutuelle", new String[]{"mutuelle"}, new Object[]{currentMutuel})));
            for (YvsMutMensualite mens : mensualites) {
                if (montant > 0) {
                    ReglementMensualite reg = getReglementMensualite(service, mens);
                    reg.setCodeOperation(codeOp);
                    if (montant < mens.getMontantReste()) {
                        reg.setMontant(montant);
                    }
                    service.saveNewReglementMensualite(reg, mens, service.getMensualite(), service.getEcheancierSelect(), service.getCredit(), service.getCreditSelect());
                }
                montant -= mens.getMontantReste();
            }
        }
        return true;
    }

    public void saveMensualiteFromCompte(YvsMutMensualite men, boolean onlyLine) {
        ManagedCredit service = (ManagedCredit) giveManagedBean(ManagedCredit.class);
        if (service != null) {
            if (!service.controleFicheReglementMensualite(getReglementMensualite(service, men), UtilMut.buildBeanCredit(men.getEchellonage().getCredit()))) {
                return;
            }
            ReglementMensualite reg = getReglementMensualite(service, men);

            if (men.getMontantReste() > operationCompte.getCompte().getSolde()) {
                getWarningMessage("Votre solde disponible est de " + operationCompte.getCompte().getSolde());
            }
            double montant = (reg.getMontant() > operationCompte.getCompte().getSolde() && operationCompte.getCompte().getSolde() > 0) ? operationCompte.getCompte().getSolde() : reg.getMontant();
            reg.setMontant(montant);
            if (men.getMontantReste() <= operationCompte.getCompte().getSolde()) {
                service.saveNewReglementMensualite(men, reg);
                if (onlyLine) {
                    succes();
                }
            } else {
                getErrorMessage("Solde insuffisant !");
            }
        }
    }

    public void cancelPaiementSalaire(YvsMutOperationCompte op) {
        // Supprime les mensualités lié à cette opération
        String query = "DELETE FROM yvs_mut_reglement_mensualite WHERE code_operation=? ";
        dao.requeteLibre(query, new Options[]{new Options(op.getCodeOperation(), 1)});
        query = "DELETE FROM yvs_mut_operation_compte WHERE code_operation=? ";
        dao.requeteLibre(query, new Options[]{new Options(op.getCodeOperation(), 1)});
        op.setId(-op.getId());
        op.setSalaireSave(false);
        op.setEpargneSave(false);
        op.setRetenuSave(false);
        op.setMensualiteSave(false);
        succes();
    }
    /**
     * Méthodes de recherche dynamique
     */
    private boolean dateSearch;
    private Date debutSearch = new Date(), finSearch = new Date();
    private String mutualisteSearch;
    private String compteSearch;
    private String sensSearch;
    private long periodeSearch, exoSearch;

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

    public String getMutualisteSearch() {
        return mutualisteSearch;
    }

    public void setMutualisteSearch(String mutualisteSearch) {
        this.mutualisteSearch = mutualisteSearch;
    }

    public String getCompteSearch() {
        return compteSearch;
    }

    public void setCompteSearch(String compteSearch) {
        this.compteSearch = compteSearch;
    }

    public String getSensSearch() {
        return sensSearch;
    }

    public void setSensSearch(String sensSearch) {
        this.sensSearch = sensSearch;
    }

    public long getPeriodeSearch() {
        return periodeSearch;
    }

    public void setPeriodeSearch(long periodeSearch) {
        this.periodeSearch = periodeSearch;
    }

    public long getExoSearch() {
        return exoSearch;
    }

    public void setExoSearch(long exoSearch) {
        this.exoSearch = exoSearch;
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateOperation", "dates", null, "BETWEEN", "AND");
        if (dateSearch) {
            p = new ParametreRequete("y.dateOperation", "dates", debutSearch, finSearch, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        init = true;
        loadAllOperation(true);
    }

    public void addParamMutualiste() {
        ParametreRequete p0 = new ParametreRequete(null, "mutualiste", "XXXXX", " LIKE ", "AND");
        if ((mutualisteSearch != null) ? !mutualisteSearch.isEmpty() : false) {
            p0.getOtherExpression().add(new ParametreRequete("UPPER(y.compte.mutualiste.employe.matricule)", "matricule", mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
            p0.getOtherExpression().add(new ParametreRequete("UPPER(y.compte.mutualiste.employe.nom)", "nom", "%" + mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
            p0.getOtherExpression().add(new ParametreRequete("UPPER(y.compte.mutualiste.employe.prenom)", "prenom", "%" + mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
        } else {
            p0.setObjet(null);
        }
        paginator.addParam(p0);
        init = true;
        loadAllOperation(true);
    }

    public void addParamCompte() {
        ParametreRequete p0 = new ParametreRequete("y.compte.reference", "compte", null, " LIKE ", "AND");
        if ((compteSearch != null) ? !compteSearch.isEmpty() : false) {
            p0.setObjet(compteSearch + "%");
        }
        paginator.addParam(p0);
        init = true;
        loadAllOperation(true);
    }

    public void addParamSensOperation(ValueChangeEvent ev) {
        String st = (String) ev.getNewValue();
        ParametreRequete p0 = new ParametreRequete("y.sensOperation", "sens", st, " = ", "AND");
        paginator.addParam(p0);
        init = true;
        loadAllOperation(true);
    }

    public void addParamExercice(ValueChangeEvent ev) {
        Long st = (Long) ev.getNewValue();
        ParametreRequete p0 = new ParametreRequete("y.periode.exercice", "exercice", null, " = ", "AND");
        if (st != null ? st > 0 : false) {
            p0.setObjet(new YvsBaseExercice(st));
            periodes = dao.loadNameQueries("YvsMutPeriodeExercice.findByExercice", new String[]{"exercice"}, new Object[]{new YvsBaseExercice(st)});
        }
        paginator.addParam(p0);
        init = true;
        loadAllOperation(true);

    }

    public void addParamPeriode(ValueChangeEvent ev) {
        Long st = (Long) ev.getNewValue();
        ParametreRequete p0 = new ParametreRequete("y.periode", "periode", null, " = ", "AND");
        if (st != null ? st > 0 : false) {
            p0.setObjet(new YvsMutPeriodeExercice(st));
        }
        paginator.addParam(p0);
        init = true;
        loadAllOperation(true);
    }

    public void clearParams() {
        paginator.getParams().clear();
        init = true;
        loadAllOperation(true);
    }

    /**
     * * Gestion de la clôture des périodes
     */
    private String sensOp = Constantes.MUT_SENS_OP_RETRAIT;
    private String typeCompte;
    private String commentaire;
    private double montant;
    private boolean solde = true;

    public String getSensOp() {
        return sensOp;
    }

    public void setSensOp(String sensOp) {
        this.sensOp = sensOp;
    }

    public String getTypeCompte() {
        return typeCompte;
    }

    public void setTypeCompte(String typeCompte) {
        this.typeCompte = typeCompte;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public boolean isSolde() {
        return solde;
    }

    public void setSolde(boolean solde) {
        this.solde = solde;
    }

    public void loadObjectOperation(Compte compte, double montant) {
        operationCompte.setId(0);
        operationCompte.setMontant(montant);
        operationCompte.setCommentaire(commentaire);
        operationCompte.setCompte(compte);
        operationCompte.setSensOperation(sensOp);
        operationCompte.getCompte().setSolde(dao.getSoldeCompteMutualiste(compte.getId()));
    }

    public void applyActionsOnMutualiste() {
        ManagedMutualiste service = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
        if (service != null) {
            List<Integer> ids = decomposeSelection(service.getTabIds());
            if (!ids.isEmpty()) {
                YvsMutCompte compte = null;
                YvsMutMutualiste m;
                msg = false;
                for (Integer i : ids) {
                    if (i > 0) {
                        m = service.getMutualistes().get(i);
                        for (YvsMutCompte c : m.getComptes()) {
                            if (c.getTypeCompte().getNature().equals(typeCompte)) {
                                compte = c;
                                break;
                            }
                        }
                        if (compte != null) {
                            if (!solde) {
                                loadObjectOperation(UtilMut.buildBeanCompte(compte), montant);
                            } else {
                                loadObjectOperation(UtilMut.buildBeanCompte(compte), dao.getSoldeCompteMutualiste(compte.getId()));
                            }
                            if (operationCompte.getMontant() > 0) {
                                saveNew();
                            }
                        }
                        succes();
                        msg = true;
                    }
                }
            } else {
                getErrorMessage("Aucune selection trouvé !");
            }
        }
    }

    public void openDlgTraitement() {
        ManagedMutualiste service = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
        if (service != null) {
//            System.err.println("---- "+service.getTabIds());
        }
    }

    public void onEditCellSalaire(CellEditEvent ev) {
        if (ev != null) {
            DataTable s = (DataTable) ev.getSource();
            update(s.getClientId(FacesContext.getCurrentInstance()).concat(":" + ev.getRowIndex()).concat(":cellNet"));
        }
    }
}
