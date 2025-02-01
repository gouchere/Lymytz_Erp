/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.operation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.mutuelle.YvsMutDefaultUseFor;
import yvs.entity.mutuelle.YvsMutParametre;
import yvs.entity.mutuelle.YvsMutPeriodeExercice;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.mutuelle.credit.YvsMutTypeCredit;
import yvs.entity.mutuelle.operation.YvsMutOperationCompte;
import yvs.mutuelle.Compte;
import yvs.mutuelle.ManagedMutualiste;
import yvs.mutuelle.Mutualiste;
import yvs.mutuelle.PeriodeExercice;
import yvs.mutuelle.UtilMut;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Utilitaire;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedEpargne extends Managed<OperationCompte, YvsMutOperationCompte> implements Serializable {

    @ManagedProperty(value = "#{operationCompte}")
    private OperationCompte operationCompte;
    private List<YvsMutOperationCompte> epargnes;
    private List<YvsMutOperationCompte> operationComptes;

    private List<RecapOperation> listOperationEpargne;

    private Mutualiste mutualiste = new Mutualiste();
    private List<YvsMutMutualiste> epargnesActuel;

    private YvsMutParametre params;

    private String tabIds, input_reset;
//    private boolean updateOperationCompte;
//    private List<YvsMutCompte> comptesEpargnes;

    private String mutualisteSearch;
    private boolean dateSearch;
    private Date debutSearch = new Date(), finSearch = new Date();
    private long periodeSearch;
    private String defaultAction = Constantes.MUT_NATURE_OP_EPARGNE;

    PaginatorResult<YvsMutOperationCompte> paginatorHist = new PaginatorResult<>();
    public PaginatorResult<YvsMutMutualiste> paginatorMut = new PaginatorResult<>();

    private long typeCompteSearch;

    boolean isSituation = false;
    private Mutualiste situation = new Mutualiste();

    public ManagedEpargne() {
        epargnes = new ArrayList<>();
        operationComptes = new ArrayList<>();
        epargnesActuel = new ArrayList<>();
        listOperationEpargne = new ArrayList<>();
    }

    public PaginatorResult<YvsMutMutualiste> getPaginatorMut() {
        return paginatorMut;
    }

    public void setPaginatorMut(PaginatorResult<YvsMutMutualiste> paginatorMut) {
        this.paginatorMut = paginatorMut;
    }

    public YvsMutParametre getParams() {
        return params;
    }

    public void setParams(YvsMutParametre params) {
        this.params = params;
    }

    public Mutualiste getSituation() {
        return situation;
    }

    public void setSituation(Mutualiste situation) {
        this.situation = situation;
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

    public long getPeriodeSearch() {
        return periodeSearch;
    }

    public void setPeriodeSearch(long periodeSearch) {
        this.periodeSearch = periodeSearch;
    }

    public List<RecapOperation> getListOperationEpargne() {
        return listOperationEpargne;
    }

    public void setListOperationEpargne(List<RecapOperation> listOperationEpargne) {
        this.listOperationEpargne = listOperationEpargne;
    }

    public List<YvsMutMutualiste> getEpargnesActuel() {
        return epargnesActuel;
    }

    public void setEpargnesActuel(List<YvsMutMutualiste> epargnesActuel) {
        this.epargnesActuel = epargnesActuel;
    }

    public List<YvsMutOperationCompte> getOperationComptes() {
        return operationComptes;
    }

    public void setOperationComptes(List<YvsMutOperationCompte> operationComptes) {
        this.operationComptes = operationComptes;
    }

    public OperationCompte getOperationCompte() {
        return operationCompte;
    }

    public void setOperationCompte(OperationCompte operationComptes) {
        this.operationCompte = operationComptes;
    }

    public List<YvsMutOperationCompte> getEpargnes() {
        return epargnes;
    }

    public void setEpargnes(List<YvsMutOperationCompte> operationComptess) {
        this.epargnes = operationComptess;
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

//    public boolean isUpdateOperationCompte() {
//        return updateOperationCompte;
//    }
//
//    public void setUpdateOperationCompte(boolean updateOperationCompte) {
//        this.updateOperationCompte = updateOperationCompte;
//    }
    public Mutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(Mutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    public String getDefaultAction() {
        return defaultAction;
    }

    public void setDefaultAction(String defaultAction) {
        this.defaultAction = defaultAction;
    }

    public long getTypeCompteSearch() {
        return typeCompteSearch;
    }

    public void setTypeCompteSearch(long typeCompteSearch) {
        this.typeCompteSearch = typeCompteSearch;
    }

    @Override
    public void loadAll() {
        loadAllOperation(true, true);
        if (currentMutuel != null ? currentMutuel.getParamsMutuelle() != null : false) {
            parametreMutuelle = UtilMut.buildBeanParametre(currentMutuel.getParamsMutuelle());
            //charge la caisse par défaut de l'epargne
            YvsMutDefaultUseFor df = (YvsMutDefaultUseFor) dao.loadOneByNameQueries("YvsMutDefaultUseFor.findByActiviteM", new String[]{"activite", "mutuelle"}, new Object[]{Constantes.MUT_NATURE_OP_EPARGNE, currentMutuel});
            if (df != null) {
                parametreMutuelle.setCaisseEpargne(df.getCaisse());
                operationCompte.setCaisse(UtilMut.buildBeanCaisse(df.getCaisse()));
            }
            buildPeriodeByDate(new Date());
        }
    }

    public void loadAllOperation(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.compte.mutualiste.mutuelle", "mutuelle", currentMutuel, "=", "AND"));
        ParametreRequete p = new ParametreRequete(null, "nature", "XX", "=", "AND");
        p.getOtherExpression().add(new ParametreRequete("y.nature", "nature", Constantes.MUT_NATURE_OP_EPARGNE, "=", "OR"));
        p.getOtherExpression().add(new ParametreRequete("y.nature", "nature1", Constantes.MUT_NATURE_OP_ASSURANCE, "=", "OR"));
        paginator.addParam(p);
        epargnes = paginator.executeDynamicQuery("YvsMutOperationCompte", "y.periode.dateFin DESC, y.compte.mutualiste.employe.nom, y.compte.mutualiste.employe.prenom", avance, init, (int) imax, dao);

    }

    public void init() {
        params = (YvsMutParametre) dao.loadOneByNameQueries("YvsMutParametre.findByMutuelle", new String[]{"mutuelle"}, new Object[]{currentMutuel});
    }

    public void selectLineEpargne(SelectEvent ev) {
        if (ev != null) {
            onSelectObject((YvsMutOperationCompte) ev.getObject());
            tabIds = epargnes.indexOf((YvsMutOperationCompte) ev.getObject()) + "";

        }
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsMutOperationCompte> re = paginator.parcoursDynamicData("YvsMutOperationCompte", "y", "y.periode.dateFin DESC, y.compte.mutualiste.employe.nom, y.compte.mutualiste.employe.prenom", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void choixPeriode(ValueChangeEvent ev) {
        try {
            if (ev.getNewValue() != null) {
                long id = (long) ev.getNewValue();
                if (id > 0) {
                    int idx = giveExerciceActif().getPeriodesMutuelles().indexOf(new YvsMutPeriodeExercice(id));
                    if (idx >= 0) {
                        operationCompte.setPeriode(UtilMut.buildPeriodeMutuelle(giveExerciceActif().getPeriodesMutuelles().get(idx)));
                    }
                } else {
                    operationCompte.getPeriode().setReference("");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paginer(boolean next) {
        loadAllOperation(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAllOperation(true, true);
    }

    public void ordonneEpargne() {
        epargnes = paginator.executeDynamicQuery("YvsMutOperationCompte", "y.periode.dateFin DESC, y.compte.mutualiste.employe.nom, y.compte.mutualiste.employe.prenom", true, true, (int) imax, dao);
        String mois = "";
        RecapOperation item = null;
        listOperationEpargne.clear();
        for (YvsMutOperationCompte op : epargnes) {
            if (!mois.equals(op.getPeriode().getReferencePeriode())) {
                item = new RecapOperation(op.getDateOperation());
                item.setRefperiode(op.getPeriode().getReferencePeriode());
                mois = op.getPeriode().getReferencePeriode();
                item.getListOperation().add(op);
                listOperationEpargne.add(item);
            } else {
                int idx = listOperationEpargne.indexOf(item);
                if (idx >= 0) {
                    item.getListOperation().add(op);
                    listOperationEpargne.set(idx, item);
                }
            }
        }
    }

    public void loadAllOperationCompte(boolean avancer, boolean init) {
        ParametreRequete p = new ParametreRequete("y.compte.mutualiste", "mutualiste", (mutualiste.getId() > 0) ? new YvsMutMutualiste(mutualiste.getId()) : null, "=", "AND");
        paginatorHist.addParam(p);
        ParametreRequete p0 = new ParametreRequete(null, "nature", (mutualiste.getId() > 0) ? Constantes.MUT_TYPE_COMPTE_EPARGNE : null, "=", "AND");
        p0.getOtherExpression().add(new ParametreRequete("y.compte.typeCompte.nature", "nature", (mutualiste.getId() > 0) ? Constantes.MUT_TYPE_COMPTE_EPARGNE : null, "=", "OR"));
        p0.getOtherExpression().add(new ParametreRequete("y.compte.typeCompte.nature", "nature1", (mutualiste.getId() > 0) ? Constantes.MUT_TYPE_COMPTE_ASSURANCE : null, "=", "OR"));
        paginatorHist.addParam(p0);
        operationComptes = paginatorHist.executeDynamicQuery("YvsMutOperationCompte", "y.dateOperation DESC", avancer, init, (int) imax, dao);
    }

    public void changeTypeCompte(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.compte", "compte", null, "=", "AND");
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                p.setObjet(new YvsMutCompte(id));
            }
        }
        paginatorHist.addParam(p);
        loadAllOperationCompte(true, true);
        update("data_epargne_compte");
    }

    public YvsMutOperationCompte buildOperationCompte(OperationCompte y) {
        YvsMutOperationCompte o = new YvsMutOperationCompte();
        if (y != null) {
            o.setDateOperation((y.getDateOperation() != null) ? y.getDateOperation() : new Date());
            o.setId(y.getId());
            o.setMontant(y.getMontant());
            o.setSensOperation(y.getSensOperation());
            o.setNature(y.getNature());
            if ((y.getCompte() != null) ? y.getCompte().getId() > 0 : false) {
                o.setCompte(UtilMut.buildCompte(y.getCompte(), currentUser));
            } else {
                o.setCompte(null);
            }
            o.setCommentaire(y.getCommentaire());
            o.setHeureOperation((y.getHeureOperation() != null) ? y.getHeureOperation() : new Date());
            o.setAutomatique(y.isAutomatique());
            o.setReferenceOperation(y.getReferenceOperation());
            o.setCaisse((y.getCaisse().getId() > 0) ? new YvsMutCaisse(y.getCaisse().getId()) : null);
            o.setPeriode(UtilMut.buildPeriode(y.getPeriode()));
            o.setDateSave(y.getDateSave());
            o.setDateUpdate(new Date());
            o.setEpargneMensuel(false);
            o.setAuthor(currentUser);
            o.setTableSource(Constantes.MUT_TABLE_SRC_OPERATION_COMPTE);
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
        o.setReferenceOperation(operationCompte.getReferenceOperation());
        o.setSensOperation((operationCompte.getSensOperation() != null) ? operationCompte.getSensOperation() : "Depot");
        if ((operationCompte.getCompte() != null) ? operationCompte.getCompte().getId() > 0 : false) {
            operationCompte.setCompte(UtilMut.buildBeanCompte(mutualiste.getComptes().get(mutualiste.getComptes().indexOf(new YvsMutCompte(operationCompte.getCompte().getId())))));
            operationCompte.getCompte().setSolde(operationCompte.getCompte().getSolde() + operationCompte.getMontant());
            mutualiste.getComptes().get(mutualiste.getComptes().indexOf(new YvsMutCompte(operationCompte.getCompte().getId()))).setSolde(operationCompte.getCompte().getSolde() + operationCompte.getMontant());
        }
        String name = mutualiste.getEmploye().getPrenom() + " " + mutualiste.getEmploye().getNom();
        operationCompte.getCompte().setProprietaire(name);
        o.setCompte(operationCompte.getCompte());
        o.setNew_(true);
        return o;
    }

    public OperationCompte defautlEpargne(double montant, YvsMutCompte compte, YvsMutMutualiste mutualiste) {
        OperationCompte o = new OperationCompte();
        o.setDateOperation(new Date());
        o.setId(0);
        o.setMontant(montant);
        o.setCommentaire("Epargne de la période de " + operationCompte.getPeriode().getReference());
        o.setHeureOperation(new Date());
        o.setAutomatique(true);
        String name = mutualiste.getEmploye().getPrenom() + " " + mutualiste.getEmploye().getNom();
        compte.setProprietaire(name);
        o.setCompte(UtilMut.buildBeanCompte(compte));
        o.setSensOperation(Constantes.MUT_SENS_OP_DEPOT);
        o.setNew_(true);
        return o;
    }

    @Override
    public boolean controleFiche(OperationCompte bean) {
        if (!controleFiche_(bean)) {
            return false;
        }
        if (bean.getMontant() <= 0) {
            getErrorMessage("Formulaire invalide !", "Vous devez entrer un montant correcte");
            return false;
        }
        //la référence
        String ref = genererReference(Constantes.MUT_TRANSACTIONS_MUT, bean.getDateOperation(), 0);
        if ((ref != null) ? ref.trim().equals("") : true) {
            return false;
        }
        operationCompte.setReferenceOperation(ref);
        return true;
    }

    public boolean controleFiche_(OperationCompte bean) {
        if ((bean.getCompte() != null) ? bean.getCompte().getId() < 1 : true) {
            getErrorMessage("Vous devez entrer le compte");
            return false;
        }
        if (bean.getMontant() < currentMutuel.getMontantEpargne() && currentMutuel.getMontantEpargne() > 0) {
            getErrorMessage("Le montant minimale de l'epargne est de " + currentMutuel.getMontantEpargne());
            return false;
        }
        if (bean.getPeriode().getId() <= 0) {
            getErrorMessage("Formulaire invalide !", "Vous devez préciser la période d'épargne");
            return false;
        }
        if (bean.getModePaiement().equals(Constantes.MUT_MODE_PAIEMENT_ESPECE)) {
            if (bean.getCaisse().getId() <= 0) {
                getErrorMessage("Formulaire invalide !", "Vous n'avez indiqué aucune caisse");
                return false;
            }
        }
        if (defaultAction.equals(Constantes.MUT_NATURE_OP_ASSURANCE)) {
            if (!bean.getCompte().getType().getNature().equals(Constantes.MUT_TYPE_COMPTE_ASSURANCE)) {
                getErrorMessage("Vous devez choisir un compte de type assurance !");
                return false;
            }
        }
        if (bean.getDateOperation().after(new Date())) { //on ne devrait pas enregistrer des épargnes dans le futur
            getErrorMessage("Formulaire invalide !", "la date entrée est incorrecte");
            return false;
        }
        return true;
    }

    @Override
    public void resetFiche() {
//        resetFiche(mutualiste);
        mutualiste = new Mutualiste();
//        operationCompte.setDateOperation(new Date());
        operationCompte.setId(0);
        operationCompte.setMontant(0);
        operationCompte.setSensOperation(Constantes.MUT_SENS_OP_DEPOT);
        operationCompte.setCompte(new Compte());
        operationCompte.setCommentaire(null);
        operationCompte.setReferenceOperation(null);
        operationCompte.setHeureOperation(new Date());
        operationCompte.setAutomatique(false);
//        operationCompte.setPeriode(new PeriodeExercice());
        mutualiste.setComptes(new ArrayList<YvsMutCompte>());
        update("blog_form_epargne");
    }

    @Override
    public void populateView(OperationCompte bean) {
        operationCompte.setDateOperation((bean.getDateOperation() != null) ? bean.getDateOperation() : new Date());
        operationCompte.setId(bean.getId());
        operationCompte.setMontant(bean.getMontant());
        operationCompte.setSensOperation((bean.getSensOperation() != null) ? bean.getSensOperation() : "Depot");
        operationCompte.setCompte(bean.getCompte());
        operationCompte.setCommentaire(bean.getCommentaire());
        operationCompte.setHeureOperation(bean.getHeureOperation());
        operationCompte.setAutomatique(bean.isAutomatique());
        operationCompte.setPeriode(bean.getPeriode());
        operationCompte.setReferenceOperation(bean.getReferenceOperation());
        operationCompte.setDateSave(bean.getDateSave());
    }

    @Override
    public void onSelectObject(YvsMutOperationCompte y) {
        if (y != null) {
            OperationCompte bean = UtilMut.buildBeanOperationCompte(y);
            populateView(bean);
            YvsMutCompte e = y.getCompte();
            if (e != null ? e.getMutualiste() != null : false) {
                Mutualiste mut = UtilMut.buildBeanMutualiste(e.getMutualiste());
                chooseMutualiste(mut);
                if (mutualiste.getComptes().isEmpty()) {
                    mutualiste.getComptes().add(y.getCompte());
                }
            }
            update("blog_form_epargne");
            update("select_periode_epargne_list");
        }
    }

    @Override
    public boolean saveNew() {
        String action = operationCompte.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(operationCompte)) {
                operationCompte.setNature((defaultAction.equals(Constantes.MUT_NATURE_OP_EPARGNE)) ? Constantes.MUT_NATURE_OP_EPARGNE : Constantes.MUT_NATURE_OP_ASSURANCE);
                YvsMutOperationCompte entity = buildOperationCompte(operationCompte);
                entity.setDateUpdate(new Date());
                entity.setEpargneMensuel(false);
                entity.setAuthor(currentUser);
                if (operationCompte.getId() <= 0) {
                    entity.setDateSave(new Date());
                    entity.setId(null);
                    entity = (YvsMutOperationCompte) dao.save1(entity);
                    operationCompte.setId(entity.getId());
                    entity.setNew_(true);
                    operationComptes.add(0, entity);
                    epargnes.add(0, entity);
                } else {
                    YvsMutOperationCompte e = (YvsMutOperationCompte) dao.loadOneByNameQueries("YvsMutOperationCompte.findById", new String[]{"id"}, new Object[]{entity.getId()});
                    if (e != null) {
                        if (!e.getCompte().getMutualiste().equals(entity.getCompte().getMutualiste())) {
                            operationCompte.setId(-1);
                            saveNew();
                        }
                    }
                    dao.update(entity);
                    int idx = operationComptes.indexOf(entity);
                    if (idx >= 0) {
                        operationComptes.set(idx, entity);
                    }
                    idx = epargnes.indexOf(entity);
                    if (idx >= 0) {
                        epargnes.set(idx, entity);
                    }
                }
                succes();
                actionOpenOrResetAfter(this);
                update("data_epargne");
                update("data_epargne_compte");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
        }
        return true;
    }

    public boolean recopieValueBean(double montant, Compte epargne, Date date, PeriodeExercice pe) {
        // construit l'objet opération
        operationCompte.setId(-1);
        operationCompte.setAutomatique(false);
        operationCompte.setCommentaire("Epargne mensuelle");
        operationCompte.setCompte(epargne);
        operationCompte.setDateOperation(date);
        operationCompte.setDateSave(new Date());
        operationCompte.setHeureOperation(new Date());
        operationCompte.setMontant(montant);
        operationCompte.setNew_(true);
        operationCompte.setPeriode(pe);
        operationCompte.setSensOperation(Constantes.MUT_SENS_OP_DEPOT);
        operationCompte.setModePaiement(Constantes.MUT_MODE_PAIEMENT_COMPTE);
//        return saveNew();
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? tabIds.length() > 0 : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsMutOperationCompte> list = new ArrayList<>();
                YvsMutOperationCompte bean;
                for (Long ids : l) {
                    bean = epargnes.get(ids.intValue());
                    bean.setDateUpdate(new Date());
                    bean.setAuthor(currentUser);
                    list.add(bean);
                    dao.delete(bean);
                }
                epargnes.removeAll(list);
                resetFiche();
                succes();
                update("data_epargne");
            }

        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    public void deleteEpargne() {
        try {
            if (operationCompte.getId() > 0) {
                YvsMutOperationCompte op = new YvsMutOperationCompte(operationCompte.getId());
                op.setAuthor(currentUser);
                dao.delete(op);
                update("data_mutualiste_actuel_ep");
                succes();
                resetFiche();
            } else {
                getErrorMessage("Aucune Epargne n'a été trouvé !");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Lymytz Error >>> ", ex);
        }
    }

    public void deleteBean(YvsMutOperationCompte y) {
        try {
            if (y != null) {
                dao.delete(y);
                epargnes.remove(y);

                resetFiche();
                succes();
                update("data_epargne");
                update("blog_form_epargne");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Lymytz Error...", ex);
        }
    }

    @Override
    public void updateBean() {
        if ((tabIds != null) ? tabIds.length() > 0 : false) {
            String[] ids = tabIds.split("-");
            if (operationCompte.getId() > 0) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                YvsMutOperationCompte bean = epargnes.get(epargnes.indexOf(new YvsMutOperationCompte(id)));
                populateView(UtilMut.buildBeanOperationCompte(bean));
                tabIds = "";
                input_reset = "";
                update("blog_form_epargne");
            }
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutOperationCompte bean = (YvsMutOperationCompte) ev.getObject();
            populateView(UtilMut.buildBeanOperationCompte(bean));
            update("blog_form_epargne");
        }
    }

    public void loadOnViewMutualiste(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            onSelectMutualiste((YvsMutMutualiste) ev.getObject());
        }
    }

    public void onSelectMutualiste(YvsMutMutualiste bean) {
        chooseMutualiste(UtilMut.buildBeanMutualiste(bean));
    }

    public void onSelectSituationCompte(YvsMutOperationCompte bean) {
        onSelectMutualiste(bean.getCompte().getMutualiste());
        Navigations n = (Navigations) giveManagedBean(Navigations.class);
        if (n != null) {
            n.naviguationView("Situation Compte", "modMutuelle", "smenSituationCompte", true);
        }
    }

    public void chooseMutualiste(Mutualiste e) {
        if (e != null) {
            List<YvsMutCompte> comptes = new ArrayList<>(e.getComptes()); //important
            if (!isSituation) {
                cloneObject(mutualiste, e);
                boolean trouv = false;
                mutualiste.getComptes().clear();
                for (YvsMutCompte c_ : comptes) {
                    if (c_.getTypeCompte().getNature().equals(Constantes.MUT_TYPE_COMPTE_EPARGNE) && defaultAction.equals(Constantes.MUT_NATURE_OP_EPARGNE)) {
                        trouv = true;
                        mutualiste.getComptes().add(c_);
                        operationCompte.setCompte(UtilMut.buildBeanCompte(c_));
                    } else if (c_.getTypeCompte().getNature().equals(Constantes.MUT_TYPE_COMPTE_ASSURANCE) && defaultAction.equals(Constantes.MUT_NATURE_OP_ASSURANCE)) {
                        trouv = true;
                        mutualiste.getComptes().add(c_);
                        operationCompte.setCompte(UtilMut.buildBeanCompte(c_));
                    }
                }
                if (!trouv) {
                    resetFiche();
                    getInfoMessage("Ce mutualiste n'a pas de compte epargne");
                }
                //charge l'historique du compte épargne            
                loadAllOperationCompte(true, true);
                update("txt_mutualiste_epargne");
                update("compte_mutualiste_epargne");
                update("data_epargne_compte");
            } else {
                cloneObject(situation, e);
                situation.getRetards().clear();
                situation.getEncours().clear();
                situation.getCredits().clear();

                champ = new String[]{"mutualiste", "dateDebut", "dateFin"};
                List<YvsMutPeriodeExercice> periodes = dao.loadNameQueries("YvsMutPeriodeExercice.findPeriodeSuivant", new String[]{"societe", "date"}, new Object[]{currentAgence.getSociete(), new Date()});
                for (YvsMutPeriodeExercice p : periodes) {
                    val = new Object[]{new YvsMutMutualiste(e.getId()), p.getDateDebut(), p.getDateFin()};
                    Double m = (Double) dao.loadObjectByNameQueries("YvsMutMensualite.findMontantDates", champ, val);
                    if (m != null ? m > 0 : false) {
                        p.setMontantTotal(m != null ? m : 0);
                        m = (Double) dao.loadObjectByNameQueries("YvsMutMensualite.findInteretDates", champ, val);
                        p.setInteretTotal(m != null ? m : 0);
                        m = (Double) dao.loadObjectByNameQueries("YvsMutMensualite.findAmortissementDates", champ, val);
                        p.setAmortissementTotal(m != null ? m : 0);
                        m = (Double) dao.loadObjectByNameQueries("YvsMutMensualite.findPenaliteDates", champ, val);
                        p.setPenaliteTotal(m != null ? m : 0);
                        situation.getEncours().add(p);

                        situation.setMontantRetrait(situation.getMontantRetrait() + p.getMontantTotal());
                    }
                }

                periodes = dao.loadNameQueries("YvsMutPeriodeExercice.findPeriodePrecedent", new String[]{"societe", "date"}, new Object[]{currentAgence.getSociete(), new Date()});
                for (YvsMutPeriodeExercice p : periodes) {
                    val = new Object[]{new YvsMutMutualiste(e.getId()), p.getDateDebut(), p.getDateFin()};
                    Double m = (Double) dao.loadObjectByNameQueries("YvsMutMensualite.findMontantDates", champ, val);
                    if (m != null ? m > 0 : false) {
                        p.setMontantTotal(m != null ? m : 0);
                        m = (Double) dao.loadObjectByNameQueries("YvsMutMensualite.findInteretDates", champ, val);
                        p.setInteretTotal(m != null ? m : 0);
                        m = (Double) dao.loadObjectByNameQueries("YvsMutMensualite.findAmortissementDates", champ, val);
                        p.setAmortissementTotal(m != null ? m : 0);
                        m = (Double) dao.loadObjectByNameQueries("YvsMutMensualite.findPenaliteDates", champ, val);
                        p.setPenaliteTotal(m != null ? m : 0);
                        situation.getRetards().add(p);

                        situation.setMontantCredit(situation.getMontantCredit() + p.getMontantTotal());
                    }
                }
                //Solde des compte

                for (YvsMutCompte c : situation.getComptes()) {
                    c.setSolde(findSoldeCompte(c.getId()));
                }
                //Total épargne
//                champ = new String[]{"mutualiste", "nature"};
//                val = new Object[]{new YvsMutMutualiste(e.getId()), Constantes.MUT_NATURE_OP_EPARGNE};
//                Double m = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findSoldeMutualiste", champ, val);
                Object[] epargne = new Object[]{Constantes.MUT_NATURE_OP_EPARGNE, findSoldeMutualisteByNature(e.getId(), Constantes.MUT_NATURE_OP_EPARGNE)};
                situation.getMontants().add(epargne);
                //Solde assurance
                Object[] assurance = new Object[]{Constantes.MUT_NATURE_OP_ASSURANCE, findSoldeMutualisteByNature(e.getId(), Constantes.MUT_NATURE_OP_ASSURANCE)};
                situation.getMontants().add(assurance);
                //Salaire Contactuelle
                assurance = new Object[]{Constantes.MUT_LIBELLE_SALAIRE_CONTRAT, findSalaireContrat(new YvsMutMutualiste(mutualiste.getId()))};
                situation.getMontants().add(assurance);
                assurance = new Object[]{Constantes.MUT_LIBELLE_SALAIRE_MOYEN, searchSalaireMoyen(new YvsMutMutualiste(mutualiste.getId()))};
                situation.getMontants().add(assurance);
//                val = new Object[]{new YvsMutMutualiste(e.getId()), Constantes.MUT_NATURE_OP_ASSISTANCE};
//                m = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findSoldeMutualiste", champ, val);
//                Object[] assistance = new Object[]{Constantes.MUT_NATURE_OP_ASSISTANCE, (m != null ? m : 0)};
//                situation.getMontants().add(assistance);
                Calendar date = Calendar.getInstance();
                date.add(Calendar.DATE, -params.getPeriodeSalaireMoyen());
                date.set(Calendar.DATE, date.getActualMinimum(Calendar.DAY_OF_MONTH));

//                champ = new String[]{"mutualiste", "nature", "date"};
//                val = new Object[]{new YvsMutMutualiste(e.getId()), Constantes.MUT_NATURE_OP_SALAIRE, date.getTime()};
//                m = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findSoldeMutualisteDate", champ, val);
//                situation.setMontantPaie(m != null ? (m / (params.getPeriodeSalaireMoyen() > 0 ? params.getPeriodeSalaireMoyen() : 1)) : 0);
                List<YvsMutTypeCredit> types = dao.loadNameQueries("YvsMutTypeCredit.findByMutuelle", new String[]{"mutuelle", "typeAvance"}, new Object[]{currentMutuel, false});
                Double m;
                for (YvsMutTypeCredit t : types) {
                    Long s = (Long) dao.loadOneByNameQueries("YvsMutCredit.countByMutualisteType", new String[]{"mutualiste", "type"}, new Object[]{new YvsMutMutualiste(e.getId()), t});
                    if (s != null ? s > 0 : false) {
                        t.setTotalPris(s != null ? s : 0);
                        s = (Long) dao.loadOneByNameQueries("YvsMutCredit.CountByMutualisteTypeStatut", new String[]{"mutualiste", "type", "statut"}, new Object[]{new YvsMutMutualiste(e.getId()), t, Constantes.STATUT_DOC_ENCOUR});
                        t.setTotalEncours(s != null ? s : 0);
                        s = (Long) dao.loadOneByNameQueries("YvsMutCredit.CountByMutualisteTypeStatut", new String[]{"mutualiste", "type", "statut"}, new Object[]{new YvsMutMutualiste(e.getId()), t, Constantes.STATUT_DOC_PAYER});
                        t.setTotalPaye(s != null ? s : 0);

                        m = (Double) dao.loadOneByNameQueries("YvsMutCredit.sumByMutualisteType", new String[]{"mutualiste", "type"}, new Object[]{new YvsMutMutualiste(e.getId()), t});
                        t.setMontantTotal(m != null ? m : 0);
                        situation.getCredits().add(t);

                        situation.setMontantResteEpargne(situation.getMontantResteEpargne() + t.getMontantTotal());
                    }
                }
                update("txt_mutualiste_situation");
                update("blog_situation");
                update("data_situation_compte");
            }
        }
    }

    private double searchSalaireMoyen(YvsMutMutualiste mut) {
        double salaire = 0;
        if (mut != null ? mut.getId() != null ? mut.getId() > 0 : false : false) {
            champ = new String[]{"mutualiste", "natureCpte", "date", "mouvement", "natureOp"};
            val = new Object[]{mut, Constantes.MUT_TYPE_COMPTE_COURANT, new Date(), Constantes.MUT_SENS_OP_DEPOT, Constantes.MUT_NATURE_OP_SALAIRE};
            List<YvsMutOperationCompte> ls = dao.loadNameQueries("YvsMutOperationCompte.findSalaireMoyen", champ, val, 0, parametreMutuelle.getPeriodeSalaireMoyen());
            if (!ls.isEmpty()) {
                int i = 0;
                for (YvsMutOperationCompte p : ls) {
                    salaire += (p.getMontant());
                    i++;
                }
                salaire = salaire / i;
            }
        }
        return salaire;
    }

    private double findSalaireContrat(YvsMutMutualiste mut) {
        champ = new String[]{"id"};
        val = new Object[]{mut.getId()};
        YvsMutMutualiste y = (YvsMutMutualiste) dao.loadOneByNameQueries("YvsMutMutualiste.findById", champ, val);
        if (y != null ? y.getEmploye().getContrat() != null : false) {
            YvsGrhContratEmps c = y.getEmploye().getContrat();
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

    public void changeDefaultAction(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            if (mutualiste.getId() > 0) {
                switch ((String) ev.getNewValue()) {
                    case Constantes.MUT_NATURE_OP_EPARGNE:
                        mutualiste.setComptes(dao.loadNameQueries("YvsMutCompte.findByMutualisteType", new String[]{"mutualiste", "type"}, new Object[]{new YvsMutMutualiste(mutualiste.getId()), Constantes.MUT_TYPE_COMPTE_EPARGNE}));
                        break;
                    case Constantes.MUT_NATURE_OP_ASSURANCE:
                        mutualiste.setComptes(dao.loadNameQueries("YvsMutCompte.findByMutualisteType", new String[]{"mutualiste", "type"}, new Object[]{new YvsMutMutualiste(mutualiste.getId()), Constantes.MUT_TYPE_COMPTE_ASSURANCE}));
                        break;
                    default:
                        break;
                }
                update("blog_form_epargne");
                update("data_mutualiste_actuel_ep");
            }
        }
    }

    public void searchMutualiste(boolean isSituation) {
        this.isSituation = isSituation;
        ManagedMutualiste service = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
        if (service != null) {
            if (!isSituation) {
                String num = mutualiste.getMatricule();
                mutualiste = new Mutualiste(num, true);
                if ((num != null) ? !num.isEmpty() : false) {
                    Mutualiste y = service.searchMutualiste(num, true);
                    if (service.getMutualistes() != null ? !service.getMutualistes().isEmpty() : false) {
                        if (service.getMutualistes().size() > 1) {
                            update("data_mutualiste_epa");
                        } else {
                            chooseMutualiste(y);
                        }
                        mutualiste.setError(false);
                    }
                }
            } else {
                String num = situation.getMatricule();
                situation = new Mutualiste(num, true);
                if ((num != null) ? !num.isEmpty() : false) {
                    Mutualiste y = service.searchMutualiste(num, true);
                    if (service.getMutualistes() != null ? !service.getMutualistes().isEmpty() : false) {
                        if (service.getMutualistes().size() > 1) {
                            update("data_mutualiste_situation");
                        } else {
                            chooseMutualiste(y);
                        }
                        situation.setError(false);
                    }
                }
            }
        }
    }

    public void initMutualistes(boolean isSituation) {
        this.isSituation = isSituation;
        ManagedMutualiste m = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
        if (m != null) {
            if (!isSituation) {
                m.initMutualistes(getMutualiste());
                update("data_mutualiste_epa");
            } else {
                m.initMutualistes(getSituation());
                update("data_mutualiste_situation");
            }
        }
    }

    public void chooseDate(SelectEvent ev) {
        if (ev.getObject() != null) {
            Date d = (Date) ev.getObject();
            //vérifie le droit d'atidater 
            Calendar dlimiteInf = Calendar.getInstance();
            dlimiteInf.add(Calendar.DAY_OF_MONTH, -parametreMutuelle.getRetardSaisieEpargne());
            if (d.before(Utilitaire.giveOnlyDate(new Date()))) {
                if (d.before(dlimiteInf.getTime())) {
                    if (autoriser("mut_antidate_epargne_out_periode")) {
                        buildPeriodeByDate(d);
                    } else {
                        getWarningMessage("Vous ne disposez pas d'autorisation pour antidater l'enregistrement d'une epargne");
                        buildPeriodeByDate(new Date());
                    }
                } else {
                    if (autoriser("mut_atidate_epargne")) {
                        buildPeriodeByDate(d);
                    } else {
                        getWarningMessage("Vous ne disposez pas d'autorisation pour antidater l'enregistrement d'une epargne");
                        buildPeriodeByDate(new Date());
                    }
                }
            } else {
                buildPeriodeByDate(d);
            }
            update("select_periode_epargne_form");
        }
    }

    private void buildPeriodeByDate(Date date) {
        Calendar debut = Calendar.getInstance();
        debut.setTime(date);
        debut.set(Calendar.DAY_OF_MONTH, parametreMutuelle.getDebutEpargne());
        Calendar fin = Calendar.getInstance();
        fin.setTime(date);
        fin.set(Calendar.DAY_OF_MONTH, parametreMutuelle.getFinEpargne());
        Calendar finMois = Calendar.getInstance();
        finMois.set(Calendar.DAY_OF_MONTH, finMois.getActualMaximum(Calendar.DAY_OF_MONTH));
        if (date.after(fin.getTime())) {
            fin.add(Calendar.MONTH, 1);
            operationCompte.setPeriode(UtilMut.buildPeriodeMutuelle((YvsMutPeriodeExercice) dao.loadOneByNameQueries("YvsMutPeriodeExercice.findPeriode_", new String[]{"societe", "date"}, new Object[]{currentAgence.getSociete(), fin.getTime()})));
        } else {
            operationCompte.setPeriode(UtilMut.buildPeriodeMutuelle((YvsMutPeriodeExercice) dao.loadOneByNameQueries("YvsMutPeriodeExercice.findPeriode_", new String[]{"societe", "date"}, new Object[]{currentAgence.getSociete(), date})));
        }
    }

    public void chooseCompte(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
//        cloneObject(bean, operationCompte.getCompte());
            if (id != 0) {
//                loadAllEpargneCompte(new Compte(id));
                update("data_epargne_compte");
            }
        }
    }

    public boolean contributionPeriodeOk(YvsMutCompte c, PeriodeExercice p) {
        if (p.getId() > 0) {
            Long re = (Long) dao.loadObjectByNameQueries("YvsMutOperationCompte.findEpargnePeriode", new String[]{"periode", "compte", "nature"}, new Object[]{new YvsMutPeriodeExercice(p.getId()), c, defaultAction});
            return (re != null) ? re > 0 : false;
        }
        return false;
    }

    private boolean controleFicheEpargne() {
        if (parametreMutuelle.getCaisseEpargne() == null) {
            getErrorMessage("Aucune caisse d'epargne n'a été trouvé pour cette mutuelle !");
            return false;
        }
        if (operationCompte.getPeriode().getId() <= 0) {
            getErrorMessage("Vous devez préciser la période dépargne concerné!");
            return false;
        }
        String ref = genererReference(Constantes.MUT_TRANSACTIONS_MUT, operationCompte.getDateOperation(), operationCompte.getCaisse().getId(), Constantes.CAISSE);
        if ((ref != null) ? ref.trim().equals("") : true) {
            return false;
        }
        operationCompte.setReferenceOperation(ref);
        return true;
    }

    public void directEpargne(YvsMutMutualiste mut, YvsMutCompte compte) {
        try {
            if ((compte != null) ? (compte.getId() > 0 && mut.getMontantEpargne() > 0) : false) {
                if (controleFicheEpargne()) {
                    if (!defaultAction.equals(Constantes.MUT_NATURE_OP_EPARGNE) && !compte.getTypeCompte().getNature().equals(Constantes.MUT_TYPE_COMPTE_ASSURANCE)) {
                        getErrorMessage("Vous devez enregistrer cette opération dans un compte de type assurance !");
                        return;
                    }
                    YvsMutOperationCompte entity = buildOperationCompte(defautlEpargne(mut.getMontantEpargne(), compte, mut));
                    entity.setAuthor(currentUser);
                    entity.setEpargneMensuel(true);
                    entity.setCaisse(parametreMutuelle.getCaisseEpargne());
                    entity.setPeriode(new YvsMutPeriodeExercice(operationCompte.getPeriode().getId()));
                    if (!defaultAction.equals(Constantes.MUT_NATURE_OP_EPARGNE)) {
                        entity.setMontant(giveResteAssurance(mut.getMontantTotalAssurance()));
                    }
                    entity.setNature((defaultAction));
                    entity.setReferenceOperation(operationCompte.getReferenceOperation());
                    entity.setId(null);
                    entity = (YvsMutOperationCompte) dao.save1(entity);
                    entity.setNew_(true);
                    operationCompte.setId(entity.getId());
                    epargnes.add(0, entity);
                    if (epargnesActuel.contains(mut)) {
                        epargnesActuel.remove(mut);
                    }
                    succes();
                    update("data_epargne");
                    update("data_mutualiste_actuel_ep");
                }
            } else {
                if (mut.getMontantEpargne() <= 0) {
                    getErrorMessage("Le montant de l'épargne de ce membre n'est pas encore renseigné !");
                } else {
                    getErrorMessage("Ce membre n'a pas de compte épargne");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            getException("Lymytz Error>> ", ex);
        }
    }

    public void directSaveAssurance(YvsMutMutualiste mut, YvsMutCompte compte) {
        if (compte != null) {
            operationCompte.setAutomatique(false);
            mutualiste.getComptes().add(compte);
            operationCompte.setCompte(UtilMut.buildBeanCompte(compte));
            operationCompte.setDateOperation(new Date());
            operationCompte.setHeureOperation(new Date());
            operationCompte.setId(0);
            operationCompte.setMontant(0);
            operationCompte.setNature((defaultAction.equals(Constantes.MUT_NATURE_OP_EPARGNE) ? Constantes.MUT_NATURE_OP_EPARGNE : Constantes.MUT_NATURE_OP_ASSURANCE));
            operationCompte.setNew_(true);
            operationCompte.setSensOperation(Constantes.MUT_SENS_OP_DEPOT);
            mutualiste.getComptes().add(compte);
            openDialog("dlgSetMontant");
        } else {
            getErrorMessage("Aucun comptes d'assurance n'a été trouvé pour ce membre!");
        }
    }

    public void selectLineToDel(YvsMutOperationCompte op) {
        onSelectObject(op);
        openDialog("dlgDelEpargne");
    }

    public void calcelEpargneDirecte(YvsMutCompte compte) {
        if (operationCompte.getPeriode().getId() > 0) {
            YvsMutOperationCompte op = (YvsMutOperationCompte) dao.loadOneByNameQueries("YvsMutOperationCompte.findEpargnePeriode_", new String[]{"compte", "periode"}, new Object[]{compte, new YvsMutPeriodeExercice(operationCompte.getPeriode().getId())});
            if (op != null) {
                operationCompte.setId(op.getId());
                openDialog("dlgCancelEpargne");
            } else {
                operationCompte.setId(0);
            }
        }
    }

    public String printPeriode(Date d) {
        if (d != null) {
            return ldf.format(d);
        }
        return "";
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateOperation", "dates", null, "BETWEEN", "AND");
        if (dateSearch) {
            p = new ParametreRequete("y.dateOperation", "dates", dateSearch, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllOperation(true, true);
    }

    public void addParamMutualiste() {
        ParametreRequete p = new ParametreRequete("y.compte.mutualiste.employe", "mutualiste", null, "LIKE", "AND");
        if (mutualisteSearch != null ? mutualisteSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "mutualiste", mutualisteSearch, "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.compte.mutualiste.employe.matricule)", "mutualiste", mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.compte.mutualiste.employe.nom)", "mutualiste", mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.compte.mutualiste.employe.prenom)", "mutualiste", mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllOperation(true, true);
    }

    public void addParamPeriode(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.periode", "periode", null, "=", "AND");
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                p.setObjet(new YvsMutPeriodeExercice(id));
            }
        }
        paginator.addParam(p);
        loadAllOperation(true, true);
    }

    public double giveResteAssurance(double total) {
        double re = currentMutuel.getMontantAssurance() - total;
        return (re > 0) ? re : 0;
    }

    /*BEGIN SITUATION COMPTE*/
    public void parcoursInAllMutualise(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        isSituation = true;
        List<YvsMutMutualiste> re = paginatorMut.parcoursDynamicData("YvsMutMutualiste", "y", "y.employe.nom, y.employe.prenom", getOffset(), dao);
        if (!re.isEmpty()) {
            chooseMutualiste(UtilMut.buildBeanMutualiste(re.get(0)));
        }
    }
    /*END SITUATION COMPTE*/
}
