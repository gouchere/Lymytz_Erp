/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.echellonage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.mutuelle.credit.YvsMutCredit;
import yvs.entity.mutuelle.echellonage.YvsMutEchellonage;
import yvs.entity.mutuelle.echellonage.YvsMutMensualite;
import yvs.grh.bean.Employe;
import yvs.mutuelle.Compte;
import yvs.mutuelle.ManagedMutualiste;
import yvs.mutuelle.Mutualiste;
import yvs.mutuelle.UtilMut;
import yvs.mutuelle.credit.Credit;
import yvs.mutuelle.credit.ManagedCredit;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedEchellonage extends Managed<Echellonage, YvsMutEchellonage> implements Serializable {

    @ManagedProperty(value = "#{echellonage}")
    private Echellonage echellonage;
    YvsMutEchellonage entityEchellon;
    private List<YvsMutEchellonage> echellonages;

    private Mensualite mensualite = new Mensualite();
    private YvsMutMensualite selectMensualite = new YvsMutMensualite();
    private ReglementMensualite reglement = new ReglementMensualite();
    private List<YvsMutCaisse> caisses;

    private Mutualiste mutualiste = new Mutualiste();

    private List<YvsMutCredit> credits;
    private YvsMutCredit selectCredit;

    private String mutualisteSearch, statutSearch, creditSearch;
    private boolean dateSearch;
    private Date debutSearch = new Date(), finSearch = new Date();
    private String operateurStatut = "=";
    private ScheduleModel eventModel;

    private String tabIds, input_reset;

    public ManagedEchellonage() {
        echellonages = new ArrayList<>();
        credits = new ArrayList<>();
        caisses = new ArrayList<>();
        eventModel = new DefaultScheduleModel();
    }

    public String getOperateurStatut() {
        return operateurStatut;
    }

    public void setOperateurStatut(String operateurStatut) {
        this.operateurStatut = operateurStatut;
    }

    public String getCreditSearch() {
        return creditSearch;
    }

    public void setCreditSearch(String creditSearch) {
        this.creditSearch = creditSearch;
    }

    public YvsMutMensualite getSelectMensualite() {
        return selectMensualite;
    }

    public void setSelectMensualite(YvsMutMensualite selectMensualite) {
        this.selectMensualite = selectMensualite;
    }

    public List<YvsMutCaisse> getCaisses() {
        return caisses;
    }

    public void setCaisses(List<YvsMutCaisse> caisses) {
        this.caisses = caisses;
    }

    public ReglementMensualite getReglement() {
        return reglement;
    }

    public void setReglement(ReglementMensualite reglement) {
        this.reglement = reglement;
    }

    public YvsMutCredit getSelectCredit() {
        return selectCredit;
    }

    public void setSelectCredit(YvsMutCredit selectCredit) {
        this.selectCredit = selectCredit;
    }

    public String getStatutSearch() {
        return statutSearch;
    }

    public void setStatutSearch(String statutSearch) {
        this.statutSearch = statutSearch;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
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

    public YvsMutEchellonage getEntityEchellon() {
        return entityEchellon;
    }

    public void setEntityEchellon(YvsMutEchellonage entityEchellon) {
        this.entityEchellon = entityEchellon;
    }

    public List<YvsMutCredit> getCredits() {
        return credits;
    }

    public void setCredits(List<YvsMutCredit> credits) {
        this.credits = credits;
    }

    public Mensualite getMensualite() {
        return mensualite;
    }

    public void setMensualite(Mensualite mensualite) {
        this.mensualite = mensualite;
    }

    public Mutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(Mutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    public Echellonage getEchellonage() {
        return echellonage;
    }

    public void setEchellonage(Echellonage echellonage) {
        this.echellonage = echellonage;
    }

    public List<YvsMutEchellonage> getEchellonages() {
        return echellonages;
    }

    public void setEchellonages(List<YvsMutEchellonage> echellonages) {
        this.echellonages = echellonages;
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

    @Override
    public void loadAll() {
        loadAllEchellonage(true, true);
        caisses = currentMutuel != null ? currentMutuel.getCaisses() : new ArrayList<YvsMutCaisse>();
//        caisses = currentMutuel.getCaisses() != null ? currentMutuel.getCaisses() :
    }

    public void loadAllEchellonage(boolean avance, boolean init) {
        eventModel.clear();
        paginator.addParam(new ParametreRequete("y.credit.compte.mutualiste.mutuelle", "mutuelle", currentMutuel, "=", "AND"));
        paginator.addParam(new ParametreRequete("y.actif", "actif", true, "=", "AND"));
        paginator.addParam(new ParametreRequete("y.credit.etat", "etatCr", Constantes.ETAT_ANNULE, "!=", "AND"));
        echellonages = paginator.executeDynamicQuery("YvsMutEchellonage", "y.dateEchellonage DESC", avance, init, (int) imax, dao);
        Echellonage bean;
        Credit ce;
        Compte co;
        Mutualiste mu;
        for (YvsMutEchellonage c : echellonages) {
            bean = UtilMut.buildBeanEchellonage(c);
            ce = UtilMut.buildBeanCredit(c.getCredit());
            co = UtilMut.buildBeanCompte(c.getCredit().getCompte());
            mu = UtilMut.buildBeanMutualiste(c.getCredit().getCompte().getMutualiste());
            co.setMutualiste(mu);
            ce.setCompte(co);
            bean.setCredit(ce);
            eventModel.addEvent(bean);
        }
        update("data_echellonage");
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsMutEchellonage> re = paginator.parcoursDynamicData("YvsMutEchellonage", "y", "y.dateEchellonage DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void paginer(boolean next) {
        loadAllEchellonage(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAllEchellonage(true, true);
    }

    public List<Echellonage> loadAllEcheancierCredit(Credit bean) {
        List<Echellonage> l = new ArrayList<>();
        List<YvsMutEchellonage> y = dao.loadNameQueries("YvsMutEchellonage.findByCredit", new String[]{"credit"}, new Object[]{new YvsMutCredit(bean.getId())});
        l = UtilMut.buildBeanLisEchellonage(y);
        return l;
    }

    public void loadAllCreditCompte() {
        loadAllCreditCompte(echellonage.getCredit().getCompte());
    }

    public void loadAllCreditCompte(Compte c) {
        if ((c != null) ? c.getId() > 0 : false) {
            champ = new String[]{"compte", "etat"};
            val = new Object[]{new YvsMutCompte(c.getId()), new ArrayList<String>() {
                {
                    add(Constantes.ETAT_VALIDE);
                    add(Constantes.ETAT_SOUMIS);
                }
            ;
            }};
            nameQueri = "YvsMutCredit.findByCompteEtats";
        } else {
            champ = new String[]{"mutualiste", "etat"};
            val = new Object[]{new YvsMutMutualiste(mutualiste.getId()), new ArrayList<String>() {
                {
                    add(Constantes.ETAT_VALIDE);
                    add(Constantes.ETAT_SOUMIS);
                }
            ;
            }};
            nameQueri = "YvsMutCredit.findByMutualisteEtats";
        }
        credits = dao.loadNameQueries(nameQueri, champ, val);
        update("data_credit_compte");
    }

    public YvsMutEchellonage buildEchellonage(Echellonage y) {
        YvsMutEchellonage e = new YvsMutEchellonage();
        if (y != null) {
            if ((y.getCredit() != null) ? y.getCredit().getId() != 0 : false) {
                e.setCredit(UtilMut.buildCredit(y.getCredit(), currentUser));
            }
            e.setDateEchellonage((y.getDateEchellonage() != null) ? y.getDateEchellonage() : new Date());
            e.setDureeEcheance(y.getDureeEcheance());
            e.setEtat(y.getEtat());
            e.setId(y.getIdEch());
            e.setMontant(y.getMontant());
            e.setEcartMensualite(y.getEcartMensualite());
            e.setTaux(y.getTaux());
            e.setEcartMensualite((e.getEcartMensualite() == 0) ? 1 : e.getEcartMensualite());
            e.setDateSave(y.getDateSave());
            e.setDateUpdate(new Date());
            e.setAuthor(currentUser);
            e.setNew_(true);
        }
        return e;
    }

    @Override
    public boolean controleFiche(Echellonage bean) {
        if ((bean.getCredit() != null) ? bean.getCredit().getId() == 0 : true) {
            getErrorMessage("Vous devez specifier le credit");
            return false;
        }
        if (bean.getIdEch() == 0 && (!bean.getMensualites().isEmpty() ? bean.getMensualites().get(0).getId() > 0 : false)) {
            getErrorMessage("Vous ne pouvez pas modifier cet échancier. Il est en cours de reglement!");
            return false;
        }
        if (bean.getDateEchellonage() != null) {
            return existExerice(bean.getDateEchellonage());
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
        YvsBaseExercice exo_ = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findActifByDate", ch, v);
        return (exo_ != null) ? exo_.getId() != 0 : false;
    }

    @Override
    public void populateView(Echellonage bean) {
        cloneObject(echellonage, bean);
        cloneObject(mutualiste, bean.getCredit().getCompte().getMutualiste());
        loadAllCreditCompte(new Compte(echellonage.getCredit().getCompte().getId()));
        Collections.sort(echellonage.getMensualites(), new YvsMutMensualite());
        update("txt_mutualiste_echellonage");
        update("compte_mutualiste_echellonage");
    }

    @Override
    public void resetFiche() {
        resetFiche(echellonage);
        echellonage.setCredit(new Credit());
        echellonage.setMensualites(new ArrayList<YvsMutMensualite>());

        selectCredit = new YvsMutCredit();
        entityEchellon = new YvsMutEchellonage();

        tabIds = "";
        input_reset = "";
        update("blog_form_echellonage");
    }

    @Override
    public boolean saveNew() {
        if (input_reset != null && input_reset.equals("reset")) {
            input_reset = "";
        }
        String action = echellonage.getIdEch() > 0 ? "Modification" : "Insertion";
        try {
            if ((echellonage.getCredit() != null) ? echellonage.getCredit().getId() != 0 : false) {
                int idx = credits.indexOf(new YvsMutCredit(echellonage.getCredit().getId()));
                if (idx > -1) {
                    echellonage.setCredit(UtilMut.buildBeanCredit(credits.get(idx)));
                }
            }
            if (controleFiche(echellonage)) {
                entityEchellon = buildEchellonage(echellonage);
                if (echellonage.getIdEch() < 1) {
                    entityEchellon.setId(null);
                    entityEchellon = (YvsMutEchellonage) dao.save1(entityEchellon);
                    echellonage.setIdEch(entityEchellon.getId());
                } else {
                    dao.update(entityEchellon);
                }
                if (selectCredit.getEtat().equals(Constantes.ETAT_VALIDE)) {
                    if (echellonage.getMensualites() != null ? !echellonage.getMensualites().isEmpty() ? echellonage.getMensualites().get(0).getId() < 1 : false : false) {
                        saveAllmensualite();
                    }
                }
                int idx = echellonages.indexOf(entityEchellon);
                if (idx > -1) {
                    echellonages.set(idx, entityEchellon);
                } else {
                    echellonages.add(entityEchellon);
                }
                succes();
                actionOpenOrResetAfter(this);
                update("data_echellonage");
                update("data_credit_compte");
                update("txt_etat_echellonage_");
                update("data_mensualites_echellonage");
            }
        } catch (Exception ex) {
            getException("Error " + action + " : " + ex.getMessage(), ex);
            getErrorMessage(action + " Impossible !");
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? tabIds.length() > 0 : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsMutEchellonage> list = new ArrayList<>();
                YvsMutEchellonage bean;
                for (Long ids : l) {
                    bean = echellonages.get(ids.intValue());
                    bean.setDateUpdate(new Date());
                    bean.setAuthor(currentUser);
                    list.add(bean);
                    dao.delete(bean);
                }
                echellonages.removeAll(list);
                tabIds = "";
                resetFiche();
                succes();
                update("data_echellonage");

            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    public void deleteBean(YvsMutEchellonage y) {
        try {
            if (y != null) {
                dao.delete(y);
                echellonages.remove(y);

                resetFiche();
                succes();
                update("data_echellonage");
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
            if ((ids != null) ? ids.length > 0 : false) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                YvsMutEchellonage bean = echellonages.get(echellonages.indexOf(new YvsMutEchellonage(id)));
                populateView(UtilMut.buildBeanEchellonage(bean));
                tabIds = "";
                input_reset = "";
                update("blog_form_echellonage");
            }
        }
    }

    @Override
    public void onSelectObject(YvsMutEchellonage y) {
        resetFiche();
        entityEchellon = y;
        selectCredit = y.getCredit();
        populateView(UtilMut.buildBeanEchellonage(entityEchellon));
        update("blog_form_echellonage");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            onSelectObject((YvsMutEchellonage) ev.getObject());
            tabIds = echellonages.indexOf((YvsMutEchellonage) ev.getObject()) + "";
        }
    }

    public void loadOnViewMutualiste(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutMutualiste bean = (YvsMutMutualiste) ev.getObject();
            chooseMutualiste(UtilMut.buildBeanMutualiste(bean));
        }
    }

    public void chooseMutualiste(Mutualiste e) {
        if (e != null) {
            List<YvsMutCompte> comptes = new ArrayList<>();
            comptes.addAll(e.getComptes());
            cloneObject(mutualiste, e);
            boolean trouv = false;
            mutualiste.getComptes().clear();
            echellonage.getMensualites().clear();
            for (YvsMutCompte c_ : comptes) {
                Compte c = UtilMut.buildBeanCompte(c_);
                if (c.getType().getNature().equals(Constantes.MUT_TYPE_COMPTE_COURANT)) {
                    trouv = true;
                    mutualiste.getComptes().add(c_);
                    if (echellonage.getCredit() == null) {
                        echellonage.setCredit(new Credit());
                    }
                    cloneObject(echellonage.getCredit().getCompte(), c);
                }
            }
            if (!trouv) {
                resetFiche();
                getInfoMessage("Ce mutualiste n'a pas de compte credit");
            }
            loadAllCreditCompte(new Compte(echellonage.getCredit().getCompte().getId()));
            update("txt_mutualiste_echellonage");
            update("compte_mutualiste_echellonage");
            update("data_mensualites_echellonage");
        }
    }

    public void searchMutualiste() {
        String num = getMutualiste().getMatricule();
        getMutualiste().setId(0);
        getMutualiste().setError(true);
        getMutualiste().setEmploye(new Employe());
        ManagedMutualiste m = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
        if (m != null) {
            Mutualiste y = m.searchMutualiste(num, true);
            if (m.getMutualistes() != null ? !m.getMutualistes().isEmpty() : false) {
                if (m.getMutualistes().size() > 1) {
                    update("data_mutualiste_echel");
                } else {
                    chooseMutualiste(y);
                }
                getMutualiste().setError(false);
            }
        }
    }

    public void initMutualistes() {
        ManagedMutualiste m = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
        if (m != null) {
            m.initMutualistes(getMutualiste());
        }
        update("data_mutualiste_echel");
    }

    public void chooseCompte() {
        credits.clear();
        if (echellonage.getCredit() != null) {
            if ((echellonage.getCredit().getCompte() != null) ? echellonage.getCredit().getCompte().getId() != 0 : false) {
                loadAllCreditCompte(new Compte(echellonage.getCredit().getCompte().getId()));
            }
        }
        update("data_credit_compte");
    }

    public void populateViewCredit(Credit bean) {
        Credit c = new Credit();
        cloneObject(c, bean);
        if ((bean.getEcheancier() != null) ? bean.getEcheancier().getIdEch() > 0 : false) {
            cloneObject(echellonage, bean.getEcheancier());
        }
        echellonage.setCredit(c);
        Collections.sort(echellonage.getMensualites(), new YvsMutMensualite());
    }

    public void loadOnViewCredit(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectCredit = (YvsMutCredit) ev.getObject();
            entityEchellon = selectCredit.getEcheancier();
            populateViewCredit(UtilMut.buildBeanCredit(selectCredit));
        }
        update("txt_reference_credit");
        update("form_compte_mutualiste");
        update("data_mensualites_echellonage");
    }

    public void calculMensualite() {
        if (!selectCredit.getEtat().equals(Constantes.ETAT_VALIDE)) {
            getWarningMessage("Ce crédit n'est pas validé...ces mensualités ne seront pas sauvegardé");
        }
        echellonage.getMensualites().clear();
        ManagedCredit w = (ManagedCredit) giveManagedBean(ManagedCredit.class);
        if (w != null) {
            int id = -1000;
            for (Mensualite m : w.calculMensualite(echellonage, UtilMut.buildBeanCredit(selectCredit), selectCredit.getMontant())) {
                YvsMutMensualite y = w.buildMensualite(id++, m, entityEchellon);
                m.setMontantReste(m.getMontant());
                echellonage.getMensualites().add(y);
            }
        }
    }

    private YvsMutMensualite buildMensualite(Mensualite y) {
        YvsMutMensualite m = new YvsMutMensualite();
        if (y != null) {
            m.setDateMensualite((y.getDateMensualite() != null) ? y.getDateMensualite() : new Date());
            m.setId(y.getId());
            m.setMontant(y.getMontant());
            m.setEchellonage(entityEchellon);
        }
        return m;
    }

    private Mensualite recopieViewMensualite(YvsMutMensualite y) {
        Mensualite m = new Mensualite();
        m.setDateMensualite((y.getDateMensualite() != null) ? y.getDateMensualite() : new Date());
        m.setEtat(y.getEtat());
        m.setId(y.getId());
        m.setMontant(y.getMontant());
        return m;
    }

    public Mensualite saveNewMensualite(YvsMutMensualite y) {
        try {
            Mensualite bean = recopieViewMensualite(y);
            YvsMutMensualite entity = buildMensualite(bean);
            entity.setId(null);
            entity = (YvsMutMensualite) dao.save1(entity);
            bean.setId(entity.getId());
            return bean;
        } catch (Exception e) {
            System.err.println("Error Insertion : " + e.getMessage());
            return new Mensualite();
        }
    }

    public List<Mensualite> saveAllmensualite() {
        List<Mensualite> l = new ArrayList<>();
        if (echellonage.getMensualites() != null) {
            for (YvsMutMensualite m : echellonage.getMensualites()) {
                l.add(saveNewMensualite(m));
            }
        }
        return l;
    }

    public void saveNewReglementMensualite() {
        ManagedCredit w = (ManagedCredit) giveManagedBean(ManagedCredit.class);
        if (w != null) {
            reglement.setDateReglement(new Date());
            reglement.setMontant(selectMensualite.getMontantReste());
            reglement.setReglerPar(currentUser.getUsers().getNomUsers());
            if (w.saveNewReglementMensualite(reglement, selectMensualite, UtilMut.buildBeanMensualite(selectMensualite), entityEchellon, UtilMut.buildSimpleBeanCredit(selectCredit), selectCredit)) {
                succes();
                update("data_mensualites_echellonage");
                update("data_echellonage");
            }
        }
    }

    public void saveNewReglementMensualite(YvsMutMensualite mens) {
        selectMensualite = mens;
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean onGenererMensualite() {
        if (echellonage.getMensualites() != null ? !echellonage.getMensualites().isEmpty() : false) {
            return echellonage.getMensualites().get(0).getId() < 1;
        }
        return true;
    }

    public void chooseCaisse() {
        if (reglement.getCaisse() != null ? reglement.getCaisse().getId() > 0 : false) {
            int idx = caisses.indexOf(new YvsMutCaisse(reglement.getCaisse().getId()));
            if (idx > -1) {
                reglement.setCaisse(UtilMut.buildBeanCaisse(caisses.get(idx)));
            }
        }
    }

    public void deleteBeanMensualite(YvsMutMensualite y) {
        ManagedCredit w = (ManagedCredit) giveManagedBean(ManagedCredit.class);
        if (w != null) {
            w.deleteBeanMensualite(y, echellonage);
        }
    }

    public void reportEcheances() {
        ManagedCredit w = (ManagedCredit) giveManagedBean(ManagedCredit.class);
        if (w != null) {
            w.reportEcheances(selectCredit, echellonage);
        }
    }

    public void cancelEvaluerReport() {
        ManagedCredit w = (ManagedCredit) giveManagedBean(ManagedCredit.class);
        if (w != null) {
            w.cancelEvaluerReport(entityEchellon);
        }
    }

    public void openViewReport(YvsMutMensualite y) {
        if (y != null ? selectCredit != null : false) {
            ManagedCredit w = (ManagedCredit) giveManagedBean(ManagedCredit.class);
            if (w != null) {
                w.openViewReport(y, selectCredit, entityEchellon);
                update("date_mensualite_echeancier1_");
                update("blog_form_reechellonage_");
            }
        }
    }

    public void appliquerReport() {
        ManagedCredit w = (ManagedCredit) giveManagedBean(ManagedCredit.class);
        if (w != null) {
            w.appliquerReport(echellonage);
            update("data_mensualites_echellonage");
        }
    }

    public void suspendreEcheance(YvsMutEchellonage y) {
        ManagedCredit w = (ManagedCredit) giveManagedBean(ManagedCredit.class);
        if (w != null) {
            w.suspendreEcheance(y, y.getCredit());
        }
    }

    public void activeEcheance(YvsMutEchellonage y) {
        ManagedCredit w = (ManagedCredit) giveManagedBean(ManagedCredit.class);
        if (w != null) {
            w.activeEcheance(y, y.getCredit());
        }
    }

    public boolean onActiveEcheance(YvsMutEchellonage y) {
        ManagedCredit w = (ManagedCredit) giveManagedBean(ManagedCredit.class);
        if (w != null) {
            return w.onActiveEcheance(y, y.getCredit());
        }
        return false;
    }

    public void onReecheancement(YvsMutEchellonage y) {
        ManagedCredit w = (ManagedCredit) giveManagedBean(ManagedCredit.class);
        if (w != null) {
            if (w.onReecheancement(y, y.getCredit())) {
                entityEchellon = y;
                populateView(UtilMut.buildBeanEchellonage(y));
                update("blog_reechellonage_");
            }
        }
    }

    public void reechellonerEcheance() {
        ManagedCredit w = (ManagedCredit) giveManagedBean(ManagedCredit.class);
        if (w != null) {
            Credit credit = UtilMut.buildBeanCredit(entityEchellon.getCredit());
            w.reechellonerEcheance(entityEchellon, credit, entityEchellon.getCredit());
            for (YvsMutEchellonage y : credit.getEcheanciers()) {
                int idx = echellonages.indexOf(y);
                if (idx < 0) {
                    echellonages.add(y);
                }
            }
            int idx = echellonages.indexOf(entityEchellon);
            if (idx > -1) {
                echellonages.set(idx, entityEchellon);
            }
        }
    }

    public void addParamMutualiste() {
        ParametreRequete p = new ParametreRequete("y.credit.compte.mutualiste.employe", "mutualiste", null, "LIKE", "AND");
        if (mutualisteSearch != null ? mutualisteSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "mutualiste", mutualisteSearch, "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.credit.compte.mutualiste.employe.matricule)", "mutualiste", mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.credit.compte.mutualiste.employe.nom)", "mutualiste", mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.credit.compte.mutualiste.employe.prenom)", "mutualiste", mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllEchellonage(true, true);
    }

    public void addParamCredit() {
        ParametreRequete p = new ParametreRequete("y.credit.reference", "reference", null, "LIKE", "AND");
        if (creditSearch != null ? creditSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", creditSearch, "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.credit.reference)", "reference", creditSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllEchellonage(true, true);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateEchellonage", "dates", null, "BETWEEN", "AND");
        if (dateSearch) {
            p = new ParametreRequete("y.dateEchellonage", "dates", debutSearch, finSearch, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllEchellonage(true, true);
    }

    public void addParamStatus() {
        ParametreRequete p = new ParametreRequete("y.etat", "etat", null, operateurStatut, "AND");
        if (statutSearch != null ? statutSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.etat", "etat", statutSearch, operateurStatut, "AND");
        }
        paginator.addParam(p);
        loadAllEchellonage(true, true);
    }

    public void changeOperateurStatut(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            operateurStatut = (String) ev.getNewValue();
            addParamStatus();
        }
    }

    public void clearParams() {
        paginator.getParams().clear();
        loadAllEchellonage(true, true);
    }

    public Date giveNextmensualite(YvsMutEchellonage ech) {
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
        return null;
    }

}
