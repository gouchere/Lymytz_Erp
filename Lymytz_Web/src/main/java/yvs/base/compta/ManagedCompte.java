/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.compta.YvsBaseNatureCompte;
import yvs.entity.compta.YvsComptaAffectationGenAnal;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.compta.analytique.YvsComptaRepartitionAnalytique;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsBaseRadicalCompte;
import yvs.parametrage.ManagedNatureCompte;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author hp Elite 8300
 */
@SessionScoped
@ManagedBean
public class ManagedCompte extends Managed<Comptes, YvsBasePlanComptable> implements Serializable {

    @ManagedProperty(value = "#{comptes}")
    private Comptes compte;
    private List<YvsBasePlanComptable> listComptes, comptes_all;
    private List<YvsBasePlanComptable> listComptesGeneral;
    private YvsBasePlanComptable selectCompte = new YvsBasePlanComptable();
    private CentreAnalytique centre = new CentreAnalytique();
    private CentreAnalytique centre1 = new CentreAnalytique();
    private LiaisonCentres liaisonCentre = new LiaisonCentres();
    private List<CentreAnalytique> listCentreAnal;
    private AffectationAnalytique affectation = new AffectationAnalytique();
    private boolean updateCentreA;
    private String chaineSelectCompte;
    private NatureCompte nature = new NatureCompte();

    private List<YvsComptaAffectationGenAnal> affectations;
    PaginatorResult<YvsComptaAffectationGenAnal> pa;
    private boolean initForm = true; //permet de d'empêcher la navigation de la requête lors de l'actualisation de la page
    PaginatorResult<YvsBasePlanComptable> paginator_local = new PaginatorResult<>();

    private String columns;
    private Object managedBean;

    public ManagedCompte() {
        listComptes = new ArrayList<>();
        listCentreAnal = new ArrayList<>();
        affectations = new ArrayList<>();
        listComptesGeneral = new ArrayList<>();
        comptes_all = new ArrayList<>();
    }

    public Object getManagedBean() {
        return managedBean;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public void setManagedBean(Object managedBean) {
        this.managedBean = managedBean;
    }

    public List<YvsBasePlanComptable> getComptes_all() {
        return comptes_all;
    }

    public void setComptes_all(List<YvsBasePlanComptable> comptes_all) {
        this.comptes_all = comptes_all;
    }

    public List<YvsBasePlanComptable> getListComptes() {
        return listComptes;
    }

    public void setListComptes(List<YvsBasePlanComptable> listComptes) {
        this.listComptes = listComptes;
    }

    public Comptes getCompte() {
        return compte;
    }

    public void setCompte(Comptes compte) {
        this.compte = compte;
    }

    public CentreAnalytique getCentre() {
        return centre;
    }

    public void setCentre(CentreAnalytique centre) {
        this.centre = centre;
    }

    public LiaisonCentres getLiaisonCentre() {
        return liaisonCentre;
    }

    public void setLiaisonCentre(LiaisonCentres liaisonCentre) {
        this.liaisonCentre = liaisonCentre;
    }

    public List<CentreAnalytique> getListCentreAnal() {
        return listCentreAnal;
    }

    public void setListCentreAnal(List<CentreAnalytique> listCentreAnal) {
        this.listCentreAnal = listCentreAnal;
    }

    public AffectationAnalytique getAffectation() {
        return affectation;
    }

    public void setAffectation(AffectationAnalytique affectation) {
        this.affectation = affectation;
    }

    public CentreAnalytique getCentre1() {
        return centre1;
    }

    public void setCentre1(CentreAnalytique centre1) {
        this.centre1 = centre1;
    }

    public void setChaineSelectCompte(String chaineSelectCompte) {
        this.chaineSelectCompte = chaineSelectCompte;
    }

    public String getChaineSelectCompte() {
        return chaineSelectCompte;
    }

    public NatureCompte getNature() {
        return nature;
    }

    public void setNature(NatureCompte nature) {
        this.nature = nature;
    }

    public YvsBasePlanComptable getSelectCompte() {
        return selectCompte;
    }

    public void setSelectCompte(YvsBasePlanComptable selectCompte) {
        this.selectCompte = selectCompte;
    }

    public boolean isUpdateCentreA() {
        return updateCentreA;
    }

    public void setUpdateCentreA(boolean updateCentreA) {
        this.updateCentreA = updateCentreA;
    }

    public boolean isInitForm() {
        return initForm;
    }

    public void setInitForm(boolean initForm) {
        this.initForm = initForm;
    }

    public boolean isUpdateAffectaionAnal() {
        return updateAffectaionAnal;
    }

    public void setUpdateAffectaionAnal(boolean updateAffectaionAnal) {
        this.updateAffectaionAnal = updateAffectaionAnal;
    }

    public boolean isUpdateCeSec() {
        return updateCeSec;
    }

    public void setUpdateCeSec(boolean updateCeSec) {
        this.updateCeSec = updateCeSec;
    }

    public List<YvsComptaAffectationGenAnal> getAffectations() {
        return affectations;
    }

    public void setAffectations(List<YvsComptaAffectationGenAnal> affectations) {
        this.affectations = affectations;
    }

    public List<YvsBasePlanComptable> getListComptesGeneral() {
        return listComptesGeneral;
    }

    public void setListComptesGeneral(List<YvsBasePlanComptable> listComptesGeneral) {
        this.listComptesGeneral = listComptesGeneral;
    }

    public boolean isDisplay_column_1() {
        return Util.asString(columns) ? columns.contains("1") : true;
    }

    public boolean isDisplay_column_2() {
        return Util.asString(columns) ? columns.contains("2") : true;
    }

    public boolean isDisplay_column_3() {
        return Util.asString(columns) ? columns.contains("3") : true;
    }

    public boolean isDisplay_column_4() {
        return Util.asString(columns) ? columns.contains("4") : true;
    }

    public boolean isDisplay_column_5() {
        return Util.asString(columns) ? columns.contains("5") : true;
    }

    public boolean isDisplay_column_6() {
        return Util.asString(columns) ? columns.contains("6") : true;
    }

    public boolean isDisplay_column_7() {
        return Util.asString(columns) ? columns.contains("7") : true;
    }

    public boolean isDisplay_column_8() {
        return Util.asString(columns) ? columns.contains("8") : true;
    }

    public boolean isDisplay_column_9() {
        return Util.asString(columns) ? columns.contains("9") : true;
    }

    public boolean isDisplay_column_0() {
        return Util.asString(columns) ? columns.contains("0") : true;
    }

    /**
     * GERER LES NATURES DES COMPTES**
     */
    /**
     * GERER LES NATURES DES COMPTES
     *
     **
     * @param ev
     */
    public void choisirNatCompte(SelectEvent ev) {
        YvsBaseNatureCompte nat = (YvsBaseNatureCompte) ev.getObject();
        choixNatureC(nat);
    }

    private void choixNatureC(YvsBaseNatureCompte nat) {
        if (nat != null) {
            compte.setNature(UtilCompta.buildBeanNatureCompte(nat));
            if (compte.getId() <= 0) {
                compte.setSaisieAnalytique(nat.getSaisieAnal());
                compte.setSaisieCompteTiers(nat.getSaisieCompteTier());
                compte.setSaisieEcheance(nat.getSaisieEcheance());
                compte.setLettrable(nat.getLettrable());
                compte.setTypeRepport(nat.getTypeReport());
                compte.setSensCompte(nat.getSensCompte());
            }
        }
    }

    public void selectNatCompte(ValueChangeEvent ev) {
        if (ev != null) {
            if (ev.getNewValue() != null) {
                long id = (long) ev.getNewValue();
                ManagedNatureCompte service = (ManagedNatureCompte) giveManagedBean("managedNatureCompte");
                if (id > 0) {
                    int idx = service.getListeNatures().indexOf(new YvsBaseNatureCompte(id));
                    if (idx >= 0) {
                        YvsBaseNatureCompte nat = service.getListeNatures().get(idx);
                        choixNatureC(nat);
                    }
                } else if (id == -1) {
                    openDialog("dlg_nature");
                    compte.setNature(new NatureCompte());
                } else if (id == -2) {
                    service.paginer(true);
                } else if (id == -3) {
                    service.paginer(false);
                } else {
                    compte.setNature(new NatureCompte());
                }
                update("form_edit_compte");
            }
        }
    }

    public void definedSaisieAnalytique() {
        update("data_centre_analytique_compte");
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Comptes recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(Comptes bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadAll() {
        ParametreRequete pr = new ParametreRequete("y.natureCompte.societe", "societe", currentUser.getAgence().getSociete());
        pr.setOperation("=");
        pr.setPredicat("AND");
        paginator.addParam(pr);
        loadDataCompte(initForm, true);
    }

    public void init() {
        managedBean = this;
        columns = null;
    }

    public void loadAllCompte() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsBasePlanComptable.findAll";
        comptes_all = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadAllForReport() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsBasePlanComptable.findWithReport";
        comptes_all = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadAllToLettrage() {
        lettrageSearch = true;
        addParamLettrage();
        loadAllActif(true);
    }

    public void loadAllActif(Boolean actif) {
        ParametreRequete pr = new ParametreRequete("y.natureCompte.societe", "societe", currentUser.getAgence().getSociete());
        pr.setOperation("=");
        pr.setPredicat("AND");
        paginator.addParam(pr);
        pr = new ParametreRequete("y.actif", "actif", actif);
        pr.setOperation("=");
        pr.setPredicat("AND");
        paginator.addParam(pr);
        loadDataCompte(initForm, true);
    }

    public void loadAllReport(String typeReport, String egaliteReport) {
        reportCompteF = typeReport;
        this.egaliteReport = egaliteReport;
        addParamReportCompte();
    }

    public void loadDataCompteGeneral(boolean init, boolean avancer) {
        ParametreRequete p = new ParametreRequete("y.natureCompte.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
        ParametreRequete pr = new ParametreRequete("y.actif", "actif", true);
        pr.setOperation("=");
        pr.setPredicat("AND");
        paginator_local.addParam(pr);
        paginator_local.addParam(p);
        listComptesGeneral = paginator_local.executeDynamicQuery("YvsBasePlanComptable", "y.numCompte", avancer, init, (int) imax, dao);
        update("table_cpt_G");
    }

    private void loadDataCompte(boolean init, boolean avancer) {
        ParametreRequete p = new ParametreRequete("y.natureCompte.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
        paginator.addParam(p);
        listComptes = paginator.executeDynamicQuery("y", "y", "YvsBasePlanComptable y JOIN FETCH y.natureCompte", "y.numCompte", avancer, init, (int) imax, dao);
        if (compte.getCompteGeneral() == null) {
            compte.setCompteGeneral(new Comptes());
        }
        if (listComptes.size() == 1) {
            onSelectObject(listComptes.get(0));
            execute("collapseForm('plan_compte')");
        } else {
            execute("collapseList('plan_compte')");
        }
        update("table_plan_compte");
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbResult())) {
            setOffset(0);
        }
        List<YvsBasePlanComptable> re = paginator.parcoursDynamicData("YvsBasePlanComptable", "y", "y.numCompte", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
        update("form_edit_compte");
    }

    public void pagineDataCompte(boolean avancer) {
        initForm = false;
        loadDataCompte(initForm, avancer);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadDataCompte(true, true);
    }

    public void changeMaxResult(ValueChangeEvent ev) {
        imax = (long) ev.getNewValue();
        loadDataCompte(initForm = true, false);
    }

    @Override
    public boolean controleFiche(Comptes bean) {
        if (bean.getNumCompte() == null) {
            getErrorMessage("Formulaire incorrecte !", "Veuillez indiquer le numéro de compte");
            return false;
        } else {
            try {
                int i = Integer.valueOf(bean.getNumCompte());
            } catch (NumberFormatException ex) {
                getErrorMessage("Formulaire incorrecte !", "Le numéro de compte doit être une chaine numérique");
                return false;
            }
        }
        if (bean.getIntitule() == null) {
            getErrorMessage("Formulaire incorrecte !", "Veuillez indiquer l'intitulé de compte");
            return false;
        }
        if (bean.getNature() != null ? bean.getNature().getId() < 1 : true) {
            getErrorMessage("Formulaire incorrecte !", "Veuillez indiquer la nature de compte");
            return false;
        }
        if (bean.getNature() != null ? bean.getNature().getId() > 0 : false) {
            if (!bean.getNature().getNature().equals(Constantes.NAT_AUTRE)) {
                nameQueri = "YvsBasePlanComptable.findByNature";
                champ = new String[]{"natureCompte"};
                val = new Object[]{new YvsBaseNatureCompte(bean.getNature().getId())};
                YvsBasePlanComptable y = (YvsBasePlanComptable) dao.loadOneByNameQueries(nameQueri, champ, val);
                if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
                    getErrorMessage("Vous ne pouvez pas associer deux comptes à cette nature de compte");
                    return false;
                }
            }
        }
        //controle la duplication du numérod e compte        
        Long id = (Long) dao.loadObjectByNameQueries("YvsBasePlanComptable.findIdByNumCompte", new String[]{"numCompte", "societe"}, new Object[]{bean.getNumCompte().trim().toUpperCase(), currentAgence.getSociete()});
        if (id != null ? id > 0 : false) {
            if (bean.getId() > 0) { // si on est en modification
                if (bean.getId() != id) {
                    getErrorMessage("Un compte a déjà été trouvé avec le même numéro !");
                    return false;
                }
            } else {
                getErrorMessage("Un compte a déjà été trouvé avec le même numéro !");
                return false;
            }
        }
        return true;
    }

    @Override
    public void resetFiche() {
        resetFiche(compte);
        compte.setCompteGeneral(new Comptes());
        compte.setNature(new NatureCompte());
        compte.setSaisieAnalytique(false);
        compte.setSaisieEcheance(false);
        compte.setSaisieCompteTiers(false);
        compte.setLettrable(false);
        compte.setActif(true);
        selectCompte = new YvsBasePlanComptable();
        update("form_edit_compte");
    }

    @Override
    public boolean saveNew() {
        saveCompte(compte, false);
        return true;
    }

    public void completeNumCompte() {
        if (compte.getNumCompte() != null ? !compte.getNumCompte().trim().isEmpty() : false) {
            String radical = compte.getNumCompte();
            StringBuilder sb = new StringBuilder(compte.getNumCompte());
            for (int i = compte.getNumCompte().length(); i < getLongueurCompte(); i++) {
                sb.append("0");
            }
            compte.setNumCompte(sb.toString());
            //trouve de la nature de compte
            List<YvsBaseRadicalCompte> natures = dao.loadNameQueries("YvsBaseRadicalCompte.findAllBySociete", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            if (natures != null) {
                YvsBaseNatureCompte nat = null;
                for (YvsBaseRadicalCompte n : natures) {
                    if (radical.startsWith(n.getRadical())) {
                        nat = n.getNatureCompte();
                    }
                }
                ManagedNatureCompte w = (ManagedNatureCompte) giveManagedBean(ManagedNatureCompte.class);
                if (w != null && nat != null) {
                    if (!w.getListeNatures().contains(nat)) {
                        w.getListeNatures().add(nat);
                    }
                }
                choixNatureC(nat);
                update("form_edit_compte");
            }
        }
    }

    public void saveCompte(Comptes bean, boolean cg) {
        if (controleFiche(bean)) {
            selectCompte = UtilCompta.buildEntityCompte(bean);
            selectCompte.setAuthor(currentUser);
            if (selectCompte.getCompteGeneral() != null) {
                selectCompte.setCompteGeneral(UtilCompta.buildEntityCompte(bean.getCompteGeneral()));
            }
            if (selectCompte.getNatureCompte() != null) {
                selectCompte.setNatureCompte(UtilCompta.buildNatureCompte(bean.getNature(), currentUser, currentAgence.getSociete()));
            }
            if (compte.getId() <= 0) {
                selectCompte.setId(null);
                selectCompte = (YvsBasePlanComptable) dao.save1(selectCompte);
                bean.setId(selectCompte.getId());
                compte.setId(selectCompte.getId());
                listComptes.add(0, selectCompte);
            } else {
                dao.update(selectCompte);
                if (listComptes.contains(selectCompte)) {
                    listComptes.set(listComptes.indexOf(selectCompte), selectCompte);
                }
            }
            if (selectCompte.getSaisieAnalytique()) {
                openListPlanAnal(selectCompte);
            }
            if (cg) {
                compte.setCompteGeneral(bean);
            }
            actionOpenOrResetAfter(this);
            succes();
            update("table_plan_compte");
        }
    }

    @Override
    public void onSelectObject(YvsBasePlanComptable y) {
        selectCompte = y;
        cloneObject(compte, UtilCompta.buildBeanCompte(y));
        ManagedNatureCompte w = (ManagedNatureCompte) giveManagedBean(ManagedNatureCompte.class);
        if (w != null) {
            if (!w.getListeNatures().contains(y.getNatureCompte())) {
                w.getListeNatures().add(y.getNatureCompte());
            }
        }
        openListPlanAnal(y);
        update("form_edit_compte");
    }

    public void selectionComptes(SelectEvent ev) {
        onSelectObject((YvsBasePlanComptable) ev.getObject());
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        YvsBasePlanComptable y = (YvsBasePlanComptable) ev.getObject();
        invoqueMethodeLoad(y);
        execute("oncollapsesForm('plan_compte')");
        selectCompte = y;

    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onSelectCompte(YvsBasePlanComptable y) {
        onSelectObject(y);
        execute("collapseForm('plan_compte')");
    }

    public void invoqueMethodeLoad(YvsBasePlanComptable select) {
        if (managedBean == null) {
            managedBean = this;
        }
        if (managedBean != null) {
            try {
                Method method = managedBean.getClass().getMethod("onSelectCompte", YvsBasePlanComptable.class);
                method.invoke(managedBean, select);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(ManagedCompte.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void selectionComptesG(SelectEvent ev) {
        YvsBasePlanComptable cp = (YvsBasePlanComptable) ev.getObject();
        if (cp != null) {
            if ((compte.getId() == cp.getId())) {
                getErrorMessage("Boucle cyclique sur le compte !");
                compte.getCompteGeneral().setNumCompte("");
                update("chmp_compteGen");
                return;
            }
            cloneObject(compte.getCompteGeneral(), UtilCompta.buildBeanCompte(cp));
            update("chmp_compteGen");
        }
    }

    public void ecouteSaisieCG() {
        paginator_local.addParam(new ParametreRequete("y.numCompte", "numCompte", compte.getCompteGeneral().getNumCompte() + "%", "LIKE", "AND"));
        paginator_local.addParam(new ParametreRequete("y.natureCompte.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND"));
        loadDataCompteGeneral(true, true);
        compte.getCompteGeneral().setError(listComptesGeneral.isEmpty());
        if (!listComptesGeneral.isEmpty()) {
            if (listComptesGeneral.size() == 1) {
                if (compte.getId() == listComptesGeneral.get(0).getId()) {
                    getErrorMessage("Boucle cyclique sur le compte !");
                    compte.setCompteGeneral(new Comptes());
                    return;
                }
                cloneObject(compte.getCompteGeneral(), UtilCompta.buildBeanCompte(listComptesGeneral.get(0)));
            } else if (listComptesGeneral.size() >= 1) {
                compte.setCompteGeneral(new Comptes());
                openDialog("dlgCmpteG");
                update("table_cpt_G");
            } else {
                compte.setCompteGeneral(new Comptes());
                loadDataCompteGeneral(true, true);
            }
        } else {
            compte.setCompteGeneral(new Comptes());
        }

    }

    public void findCompteByNum(String numCompte) {
        numCompteF = numCompte;
        addParamNumCompte(numCompte);
    }

    @Override
    public void deleteBean() {
        if (compte.getId() > 0) {
            try {
                YvsBasePlanComptable cpt = new YvsBasePlanComptable(compte.getId());
                dao.delete(cpt);
                resetFiche();
                if (listComptes.contains(cpt)) {
                    listComptes.remove(cpt);
                }
                update("table_plan_compte");
                succes();
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer ce compte !");
                log.log(Level.SEVERE, null, ex);
            }
        } else {
            getErrorMessage("Aucun compte n'a été séléctioné !");
        }
    }

    public void deleteAll() {
        try {
            if ((chaineSelectCompte != null) ? !chaineSelectCompte.equals("") : false) {
                List<Long> ids = decomposeIdSelection(chaineSelectCompte);
                List<YvsBasePlanComptable> temps = new ArrayList<>();
                YvsBasePlanComptable bean;
                for (Long id : ids) {
                    int index = listComptes.indexOf(new YvsBasePlanComptable(id));
                    if (index > -1) {
                        try {
                            bean = listComptes.get(index);
                            bean.setAuthor(currentUser);
                            bean.setDateUpdate(new Date());
                            dao.delete(bean);
                            temps.add(bean);
                            if (bean.getId().equals(compte.getId())) {
                                resetFiche();
                            }
                        } catch (Exception ex) {
                            getException("Error Suppression : ", ex);
                        }
                    }
                }
                if (!temps.isEmpty()) {
                    listComptes.removeAll(temps);
                    temps = null;
                    succes();
                    update("table_plan_compte");
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : ", ex);
        }
    }

    public void openCompteTodel(YvsBasePlanComptable cpte) {
        if (cpte.getId() > 0) {
            cloneObject(compte, UtilCompta.buildBeanCompte(cpte));
            update("form_edit_compte");
            openDialog("dlgConfirmDeleteCpte");
        }
    }

    public void toogleActiveCompte(YvsBasePlanComptable cpte) {
        if (cpte.getId() > 0) {
            cpte.setActif(!cpte.getActif());
            cpte.setAuthor(currentUser);
            cpte.setDateUpdate(new Date());
            dao.update(cpte);
        }
    }

    private boolean updateAffectaionAnal;

    public void affectationAnalytique() {
        if (compte.getId() > 0 && affectation.getCentreAnal().getId() > 0) {
            if (affectation.getCoefficient() > 0) {
                YvsComptaAffectationGenAnal entityAf = new YvsComptaAffectationGenAnal();
                entityAf.setCoefficient(affectation.getCoefficient());
                entityAf.setCompte(new YvsBasePlanComptable(compte.getId()));
                entityAf.setCentre(new YvsComptaCentreAnalytique(affectation.getCentreAnal().getId()));
                if (!updateAffectaionAnal) {
                    entityAf = (YvsComptaAffectationGenAnal) dao.save1(entityAf);
                    affectation.setId(entityAf.getId());
                    affectation.setCentreAnal(listCentreAnal.get(listCentreAnal.indexOf(affectation.getCentreAnal())));
//                    compte.getCentresAnalytique().add(0,affectation);
                } else {
                    entityAf.setId(affectation.getId());
                    affectation.setCentreAnal(listCentreAnal.get(listCentreAnal.indexOf(affectation.getCentreAnal())));
//                    compte.getCentresAnalytique().set(compte.getCentresAnalytique().indexOf(affectation), affectation);
                }
                updateAffectaionAnal = false;
                affectation = new AffectationAnalytique();
            }
        } else {
            if (compte.getId() <= 0) {
                getErrorMessage("Formulaire incorrecte !", "Vous devez dabord enregistrer ou selectionner un compte");
            } else {
                getErrorMessage("Formulaire incorrecte !", "Vous devez selectionner un centre analytique");
            }
        }
    }

    public void selectionLineAffectaton(SelectEvent ev) {
        cloneObject(affectation, (AffectationAnalytique) ev.getObject());
        updateAffectaionAnal = true;
    }

    /**
     * Gérer les centres analytiques
     *
     *
     * @param bean
     * @return
     */
    public boolean controleCentreA(CentreAnalytique bean) {
        if (bean.getIntitule() == null) {
            getErrorMessage("Formulaire incorrecte !", "Veuillez indiquer l'intitulé du centre");
            return false;
        }
        return true;
    }

    private CentreAnalytique recopieViewCA() {
        CentreAnalytique ce = new CentreAnalytique();
        cloneObject(ce, centre);
        return ce;
    }

    private CentreAnalytique recopieViewCA1() {
        CentreAnalytique ce = new CentreAnalytique();
        cloneObject(ce, centre1);
        return ce;
    }

    public List<CentreAnalytique> buildBeanCentreA(List<YvsComptaCentreAnalytique> lca) {
        List<CentreAnalytique> result = new ArrayList<>();
        champ = new String[]{"centre"};
        for (YvsComptaCentreAnalytique c : lca) {
            val = new Object[]{new YvsComptaCentreAnalytique(c.getId())};
            CentreAnalytique ca = UtilCompta.buildBeanCentreAnalytique(c);
            ca.setRepartitions(dao.loadNameQueries("YvsComptaRepartitionAnalytique.findByCentreP", champ, val));
            result.add(ca);
        }
        return result;
    }

    public void saveNewCentre(boolean reset) {
        CentreAnalytique ce;
        if (reset) {
            ce = recopieViewCA1();
            updateCentreA = false;
        } else {
            ce = recopieViewCA();
        }
        if (controleCentreA(ce)) {
            YvsComptaCentreAnalytique entityC = UtilCompta.buildCentreAnalytique(ce, currentUser);
            entityC.setAuthor(currentUser);
            if (!updateCentreA) {
                entityC.setId(null);
                entityC = (YvsComptaCentreAnalytique) dao.save1(entityC);
                ce.setId(entityC.getId());
                if (reset) {
                    centre1.setId(ce.getId());
                } else {
                    centre.setId(ce.getId());
                }
                listCentreAnal.add(0, ce);
            } else {
                dao.update(entityC);
                listCentreAnal.set(listCentreAnal.indexOf(ce), ce);
            }
            if (reset) {
                updateCentreA = false;
                liaisonCentre.setPrincipal(centre1);
                centre1 = new CentreAnalytique();
            } else {
                updateCentreA = true;
            }
            succes();
        }
    }

    public void saveNewCentreAux() {
        CentreAnalytique ce = recopieViewCA1();

        if (controleCentreA(ce)) {
            YvsComptaCentreAnalytique entityC = UtilCompta.buildCentreAnalytique(ce, currentUser);
            entityC.setAuthor(currentUser);
            entityC.setId(null);
            entityC = (YvsComptaCentreAnalytique) dao.save1(entityC);
            ce.setId(entityC.getId());
            centre1.setId(ce.getId());
            listCentreAnal.add(0, ce);
            liaisonCentre.setPrincipal(centre1);
            centre1 = new CentreAnalytique();

            succes();
        }
    }

    public void resetFicheCA() {
        centre = new CentreAnalytique();
        updateCentreA = false;
    }

    private boolean updateCeSec;

    public void repartitionSecondaireDesCentres() {
        if (centre.getId() > 0 && liaisonCentre.getSecondaire().getId() > 0) {
            YvsComptaRepartitionAnalytique dca = new YvsComptaRepartitionAnalytique();
            dca.setSecondaire(new YvsComptaCentreAnalytique(liaisonCentre.getSecondaire().getId()));
            dca.setPrincipal(new YvsComptaCentreAnalytique(centre.getId()));
            dca.setTaux(liaisonCentre.getCoefficient());
            if (!updateCeSec) {
                dca = (YvsComptaRepartitionAnalytique) dao.save1(dca);
                liaisonCentre.setId(dca.getId());
                centre.getRepartitions().add(dca);
            } else {
                dca.setId(liaisonCentre.getId());
                dao.update(dca);
                centre.getRepartitions().set(centre.getRepartitions().indexOf(dca), dca);
            }
            updateCeSec = false;
            liaisonCentre = new LiaisonCentres();
        } else {
            if (centre.getId() <= 0) {
                getErrorMessage("Formulaire incorrecte !", "Vous devez enregistrer le centre Analytique de niveau supérieure !");
            } else {
                getErrorMessage("Formulaire incorrecte !", "Vous devez enregistrer le centre Analytique de niveau inférieure !");
            }
        }
    }

    public void selectionLiaisonCe(SelectEvent ev) {
        liaisonCentre = (LiaisonCentres) ev.getObject();
        updateCeSec = true;
    }

    public void selectioneCA(SelectEvent ev) {
        centre = (CentreAnalytique) ev.getObject();
        updateCentreA = true;
    }

    public void openListPlanAnal(YvsBasePlanComptable cpt) {
        selectCompte = cpt;
        List<YvsComptaAffectationGenAnal> re = dao.loadNameQueries("YvsComptaAffectationGenAnal.findByCompte", new String[]{"compte"}, new Object[]{cpt});
        ManagedCentreAnalytique service = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
        if (service != null) {
            service.loadAllByNamedQuery();
            int idx;
            for (YvsComptaAffectationGenAnal ca : re) {
                idx = service.getCentres().indexOf(ca.getCentre());
                if (idx >= 0) {
                    service.getCentres().get(idx).setCoeficient(ca.getCoefficient());
                    service.getCentres().get(idx).setIdAffectation(ca.getId());
                    service.getCentres().get(idx).setDateSave_(ca.getDateSave());
                }
            }
            update("data_centre_analytique_compte");
        }
    }

    public void applyLiaison() {
        double soeCoef = 0;
        ManagedCentreAnalytique service = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
        List<YvsComptaCentreAnalytique> l = new ArrayList<>();
        if (service != null) {
            for (YvsComptaCentreAnalytique ca : service.getCentres()) {
                soeCoef += ca.getCoeficient();
                if (ca.getCoeficient() > 0 || ca.getIdAffectation() > 0) {
                    l.add(ca);
                }
            }
            if (soeCoef <= 100) {
                YvsComptaAffectationGenAnal item;
                for (YvsComptaCentreAnalytique ca : l) {
                    item = new YvsComptaAffectationGenAnal(ca.getIdAffectation());
                    item.setCentre(ca);
                    item.setCoefficient(ca.getCoeficient());
                    item.setCompte(selectCompte);
                    item.setDateSave(ca.getDateSave_());
                    item.setDateUpdate(new Date());
                    item.setAuthor(currentUser);
                    if (ca.getIdAffectation() > 0) {
                        if (ca.getCoeficient() <= 0) {
                            try {
                                dao.delete(item);
                            } catch (Exception ex) {
                                getErrorMessage("Impossible de supprimer cet élément !");
                                getException("Lymytz Error>>>", ex);
                            }
                        } else {
                            dao.update(item);
                        }
                    } else {
                        item.setId(null);
                        item.setDateSave(new Date());
                        item = (YvsComptaAffectationGenAnal) dao.save1(item);
                        ca.setIdAffectation(item.getId());
                    }
                }
                succes();
            } else {
                getErrorMessage("La somme des coefficient doit être égale à 100");
            }
        }
    }
    /**
     * * Zone de recherche**
     */
    private String numCompteF;
    private String numCompteGF;
    private String numCompteGFS;
    private String natureCompteF, reportCompteF, egaliteReport = "=";
    private Boolean statutActif;
    private long idCompteG;
    private Boolean saisieAnalSearch, saisieTiersSearch, saisieEcheaSeach, lettrageSearch;

    public String getEgaliteReport() {
        return egaliteReport;
    }

    public void setEgaliteReport(String egaliteReport) {
        this.egaliteReport = egaliteReport;
    }

    public String getReportCompteF() {
        return reportCompteF;
    }

    public void setReportCompteF(String reportCompteF) {
        this.reportCompteF = reportCompteF;
    }

    public Boolean getSaisieAnalSearch() {
        return saisieAnalSearch;
    }

    public void setSaisieAnalSearch(Boolean saisieAnalSearch) {
        this.saisieAnalSearch = saisieAnalSearch;
    }

    public Boolean getSaisieTiersSearch() {
        return saisieTiersSearch;
    }

    public void setSaisieTiersSearch(Boolean saisieTiersSearch) {
        this.saisieTiersSearch = saisieTiersSearch;
    }

    public Boolean getSaisieEcheaSeach() {
        return saisieEcheaSeach;
    }

    public void setSaisieEcheaSeach(Boolean saisieEcheaSeach) {
        this.saisieEcheaSeach = saisieEcheaSeach;
    }

    public Boolean getLettrageSearch() {
        return lettrageSearch;
    }

    public void setLettrageSearch(Boolean lettrageSearch) {
        this.lettrageSearch = lettrageSearch;
    }

    public String getNumCompteGFS() {
        return numCompteGFS;
    }

    public void setNumCompteGFS(String numCompteGFS) {
        this.numCompteGFS = numCompteGFS;
    }

    public String getNumCompteF() {
        return numCompteF;
    }

    public void setNumCompteF(String numCompteF) {
        this.numCompteF = numCompteF;
    }

    public String getNatureCompteF() {
        return natureCompteF;
    }

    public void setNatureCompteF(String natureCompteF) {
        this.natureCompteF = natureCompteF;
    }

    public Boolean getStatutActif() {
        return statutActif;
    }

    public void setStatutActif(Boolean statutActif) {
        this.statutActif = statutActif;
    }

    public long getIdCompteG() {
        return idCompteG;
    }

    public void setIdCompteG(long idCompteG) {
        this.idCompteG = idCompteG;
    }

    public String getNumCompteGF() {
        return numCompteGF;
    }

    public void setNumCompteGF(String numCompteGF) {
        this.numCompteGF = numCompteGF;
    }

    public Comptes findCompte(String num, boolean open) {
        addParamNumCompte(num);
        Comptes a = findCompteResult(open);
        if (a != null ? a.getId() < 1 : true) {
            a.setNumCompte(num);
            a.setError(true);
        }
        return a;
    }

    private Comptes findCompteResult(boolean open) {
        Comptes a = new Comptes();
        if (listComptes != null ? !listComptes.isEmpty() : false) {
            if (listComptes.size() > 1) {
                if (open) {
                    openDialog("dlgListCompte");
                }
            } else {
                YvsBasePlanComptable y = listComptes.get(0);
                a = UtilCompta.buildSimpleBeanCompte(y);
            }
            a.setError(false);
        }
        return a;
    }

    public void clearParams() {
        egaliteReport = "=";
        reportCompteF = null;
        paginator.clear();
        loadDataCompte(initForm = true, true);
    }

    public void addParamNumCompteGen(String numCompte) {
        ParametreRequete p = new ParametreRequete(null, "numCompte", "XX", " LIKE ", "AND");
        if ((numCompte != null) ? !numCompte.trim().isEmpty() : false) {
            p.getOtherExpression().add(new ParametreRequete("y.numCompte", "numero", numCompte.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.intitule)", "numero", numCompte.trim().toUpperCase() + "%", " LIKE ", "OR"));
        } else {
            p.setObjet(null);
        }
        paginator_local.addParam(p);
        loadDataCompteGeneral(initForm = true, true);
        update("table_cpt_G_dd");
    }

    public void addParamNumCompte(String numCompte) {
        ParametreRequete p = new ParametreRequete(null, "numCompte", "XX", " LIKE ", "AND");
        if ((numCompte != null) ? !numCompte.trim().isEmpty() : false) {
            p.getOtherExpression().add(new ParametreRequete("y.numCompte", "numero", numCompte.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.intitule)", "numero", numCompte.trim().toUpperCase() + "%", " LIKE ", "OR"));
        } else {
            p.setObjet(null);
        }
        paginator.addParam(p);
        loadDataCompte(initForm = true, true);
        update("table_cpt_G_dd");
    }

    public void addParamNoIds(String value) {
        ParametreRequete p = new ParametreRequete("y.id", "ids", null, "NOT IN", "AND");
        if ((value != null) ? !value.trim().isEmpty() : false) {
            String[] tab = value.split(";");
            if (tab != null ? tab.length > 0 : false) {
                List<Long> ids = new ArrayList<>();
                for (String t : tab) {
                    ids.add(Long.valueOf(t));
                }
                p = new ParametreRequete("y.id", "ids", ids, "NOT IN", "AND");
            }
        }
        paginator.addParam(p);
    }

    public void addParamCompteG(String numCompte) {
        ParametreRequete p = new ParametreRequete("y.compteGeneral.numCompte", "compteG", null, " LIKE ", "AND");
        if ((numCompte != null) ? !numCompte.trim().isEmpty() : false) {
            p.setObjet(numCompte.trim().toUpperCase() + "%");
        }
        paginator.addParam(p);
        loadDataCompte(initForm = true, true);
    }

    public void addParamReportCompte() {
        ParametreRequete p = new ParametreRequete("y.typeReport", "typeReport", null, egaliteReport, "AND");
        if ((reportCompteF != null) ? !reportCompteF.isEmpty() : false) {
            p.setObjet(reportCompteF);
        }
        paginator.addParam(p);
        loadDataCompte(initForm = true, true);
    }

    public void addParamNatureCompte(String numCompte) {
        ParametreRequete p = new ParametreRequete("y.natureCompte.designation", "natureC", null, " LIKE ", "AND");
        if ((numCompte != null) ? !numCompte.isEmpty() : false) {
            p.setObjet("%" + numCompte.trim().toUpperCase() + "%");
        }
        paginator.addParam(p);
        loadDataCompte(initForm = true, true);
    }

    public void addParamActif(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", (Boolean) ev.getNewValue(), "=", "AND");
        paginator.addParam(p);
        loadDataCompte(initForm = true, true);
    }

    public void addParamSaisieAnalytique() {
        ParametreRequete p = new ParametreRequete("y.saisieAnalytique", "saisieAnalytique", saisieAnalSearch, "=", "AND");
        paginator.addParam(p);
        loadDataCompte(initForm = true, true);
    }

    public void addParamSaisieTiers() {
        ParametreRequete p = new ParametreRequete("y.saisieCompteTiers", "saisieCompteTiers", saisieTiersSearch, "=", "AND");
        paginator.addParam(p);
        loadDataCompte(initForm = true, true);
    }

    public void addParamSaisieEcheance() {
        ParametreRequete p = new ParametreRequete("y.saisieEcheance", "saisieEcheance", saisieEcheaSeach, "=", "AND");
        paginator.addParam(p);
        loadDataCompte(initForm = true, true);
    }

    public void addParamLettrage() {
        ParametreRequete p = new ParametreRequete("y.lettrable", "lettrable", lettrageSearch, "=", "AND");
        paginator.addParam(p);
        loadDataCompte(initForm = true, true);
    }

    public void loadAllToLettrage(Boolean lettrageSearch) {
        this.lettrageSearch = lettrageSearch;
        addParamLettrage();
    }

}
