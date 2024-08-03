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
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.SessionScoped;
//import javax.faces.event.ValueChangeEvent;
//import org.primefaces.event.SelectEvent;
//import org.primefaces.event.UnselectEvent;
//import yvs.dao.Options;
//import yvs.entity.base.YvsBaseExercice;
//import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
//import yvs.entity.mutuelle.base.YvsMutMutualiste;
//import yvs.entity.mutuelle.credit.YvsMutTypeCredit;
//import yvs.entity.mutuelle.echellonage.YvsMutMensualite;
//import yvs.entity.mutuelle.salaire.YvsMutAvanceSalaire;
//import yvs.entity.mutuelle.salaire.YvsMutConditionAvance;
//import yvs.entity.mutuelle.salaire.YvsMutPaiementSalaire;
//import yvs.entity.mutuelle.salaire.YvsMutReglementAvance;
//import yvs.grh.bean.Employe;
//import yvs.mutuelle.Mutualiste;
//import yvs.mutuelle.Parametre;
//import yvs.mutuelle.UtilMut;
//import yvs.mutuelle.Condition;
//import yvs.mutuelle.ManagedMutualiste;
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
//public class ManagedAvanceSalaire extends Managed<AvanceSalaire, YvsMutAvanceSalaire> implements Serializable {
//
//    @ManagedProperty(value = "#{avanceSalaire}")
//    private AvanceSalaire avanceSalaire;
//    private YvsMutAvanceSalaire avanceSelect;
//    private List<YvsMutAvanceSalaire> avances, avancesMut;
//
//    private List<YvsMutMensualite> mensualites;
//
//    private ReglementAvance reglement = new ReglementAvance();
//
//    private String mutualisteSearch, statutSearch;
//    private boolean dateSearch;
//    private Date debutSearch = new Date(), finSearch = new Date();
//    private long typeSearch;
//
//    private String tabIds, input_reset;
//
//    YvsMutAvanceSalaire entityAvance;
//    Parametre parametreMutuelle;
//
//    public ManagedAvanceSalaire() {
//        mensualites = new ArrayList<>();
//        avances = new ArrayList<>();
//        avancesMut = new ArrayList<>();
//    }
//
//    public String getStatutSearch() {
//        return statutSearch;
//    }
//
//    public void setStatutSearch(String statutSearch) {
//        this.statutSearch = statutSearch;
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
//    public YvsMutAvanceSalaire getEntityAvance() {
//        return entityAvance;
//    }
//
//    public void setEntityAvance(YvsMutAvanceSalaire entityAvance) {
//        this.entityAvance = entityAvance;
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
//    public YvsMutAvanceSalaire getAvanceSelect() {
//        return avanceSelect;
//    }
//
//    public void setAvanceSelect(YvsMutAvanceSalaire avanceSelect) {
//        this.avanceSelect = avanceSelect;
//    }
//
//    public ReglementAvance getReglement() {
//        return reglement;
//    }
//
//    public void setReglement(ReglementAvance reglement) {
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
//    public List<YvsMutAvanceSalaire> getAvancesMut() {
//        return avancesMut;
//    }
//
//    public void setAvancesMut(List<YvsMutAvanceSalaire> avancesMut) {
//        this.avancesMut = avancesMut;
//    }
//
//    public AvanceSalaire getAvanceSalaire() {
//        return avanceSalaire;
//    }
//
//    public void setAvanceSalaire(AvanceSalaire avanceSalaire) {
//        this.avanceSalaire = avanceSalaire;
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
//    @Override
//    public void loadAll() {
//        loadAllAvance(true, true);
//        //charge les paramètre de la mutuelle
//        if (currentMutuel != null ? currentMutuel.getParamsMutuelle() != null : false) {
//            parametreMutuelle = UtilMut.buildBeanParametre(currentMutuel.getParamsMutuelle());
//        }
//
//        tabIds = "";
//        input_reset = "";
//    }
//
//    public void loadAllAvance(boolean avance, boolean init) {
//        paginator.addParam(new ParametreRequete("y.mutualiste.mutuelle", "mutuelle", currentMutuel, "=", "AND"));
//        avances = paginator.executeDynamicQuery("YvsMutAvanceSalaire", "y.dateAvance DESC", avance, init, (int) imax, dao);
//    }
//
//    public void parcoursInAllResult(boolean avancer) {
//        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
//        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
//            setOffset(0);
//        }
//        List<YvsMutAvanceSalaire> re = paginator.parcoursDynamicData("YvsMutAvanceSalaire", "y", "y.dateAvance DESC", getOffset(), dao);
//        if (!re.isEmpty()) {
//            onSelectObject(re.get(0));
//        }
//    }
//
//    public void paginer(boolean next) {
//        loadAllAvance(next, false);
//    }
//
//    @Override
//    public void choosePaginator(ValueChangeEvent ev) {
//        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
//        loadAllAvance(true, true);
//    }
//
//    public void loadAllAvanceMutualiste(Mutualiste bean, Date dateDebut, Date dateFin) {
//        champ = new String[]{"mutualiste", "dateDebut", "dateFin"};
//        val = new Object[]{new YvsMutMutualiste(bean.getId()), dateDebut, dateFin};
//        avancesMut = dao.loadNameQueries("YvsMutAvanceSalaire.findByMutualisteDates", champ, val);
//    }
//
//    public List<YvsMutAvanceSalaire> loadAllAvanceMutualiste_(Mutualiste mut, Date dateDebut, Date dateFin) {
//        champ = new String[]{"mutualiste", "dateDebut", "dateFin"};
//        val = new Object[]{new YvsMutMutualiste(mut.getId()), dateDebut, dateFin};
//        List<YvsMutAvanceSalaire> l = dao.loadNameQueries("YvsMutAvanceSalaire.findByMutualisteDates", champ, val);
//        return l;
//    }
//
//    public void loadAllMensualite(Mutualiste bean, Date dateDebut, Date dateFin) {
//        mensualites.clear();
//        champ = new String[]{"mutualiste", "dateDebut", "dateFin"};
//        val = new Object[]{new YvsMutMutualiste(bean.getId()), dateDebut, dateFin};
//        List<YvsMutMensualite> l = dao.loadNameQueries("YvsMutMensualite.findByMutualisteDate", champ, val);
//        if (l != null) {
//            for (YvsMutMensualite m : l) {
//                if (m.getMontantReste() > 0) {
//                    mensualites.add(m);
//                }
//            }
//        }
//    }
//
//    public YvsMutAvanceSalaire buildAvanceSalaire(AvanceSalaire y) {
//        YvsMutAvanceSalaire a = new YvsMutAvanceSalaire();
//        if (y != null) {
//            a.setId(y.getId());
//            a.setDateAvance((y.getDateAvance() != null) ? y.getDateAvance() : new Date());
//            a.setMontant(y.getMontant());
//            a.setReference(y.getReference());
//            a.setEtat("En Attente");
//            if ((y.getMutualiste() != null) ? y.getMutualiste().getId() > 0 : false) {
//                a.setMutualiste(new YvsMutMutualiste(y.getMutualiste().getId()));
//            }
//            if ((y.getType() != null) ? y.getType().getId() > 0 : false) {
//                a.setType(new YvsMutTypeCredit(y.getType().getId()));
//            }
//        }
//        return a;
//    }
//
//    @Override
//    public AvanceSalaire recopieView() {
//        if ((avanceSalaire.getType() != null) ? avanceSalaire.getType().getId() > 0 : false) {
//            ManagedTypeAvance w = (ManagedTypeAvance) giveManagedBean(ManagedTypeAvance.class);
//            if (w != null) {
//                int idx = w.getTypes().indexOf(new YvsMutTypeCredit(avanceSalaire.getType().getId()));
//                if (idx > -1) {
//                    avanceSalaire.setType(UtilMut.buildBeanTypeAvance(w.getTypes().get(idx)));
//                }
//            }
//        }
//        return avanceSalaire;
//    }
//
//    @Override
//    public boolean controleFiche(AvanceSalaire bean) {
//        if ((bean.getType() != null) ? bean.getType().getId() == 0 : true) {
//            getErrorMessage("Vous devez specifier le type");
//            return false;
//        }
//        if ((bean.getMutualiste() != null) ? bean.getMutualiste().getId() == 0 : true) {
//            getErrorMessage("Vous devez indiquer le mutualiste");
//            return false;
//        }
//        if (currentMutuel != null) {
//            if ((currentMutuel.getParamsMutuelle() != null)) {
//                Parametre p = UtilMut.buildBeanParametre(currentMutuel.getParamsMutuelle());
//                double taux = 0;
//                List<YvsMutAvanceSalaire> l = loadAllAvanceMutualiste_(bean.getMutualiste(), Util.getFirstDayMonth(new Date()), Util.getLastDayMonth(new Date()));
//                if ((l != null) ? !l.isEmpty() : false) {
//                    for (YvsMutAvanceSalaire c : l) {
//                        if (c.getType().getNatureMontant().equals("Pourcentage")) {
//                            taux += c.getType().getMontantMaximal();
//                        }
//                    }
//                }
////                if (taux > p.getTauxGlobalAvanceSalaire()) {
////                    getErrorMessage("Vous ne pouvez pas prendre ce type d'avance. Car son taux hausse la somme de vos taux, qui depasse le taux maximal prévu");
////                    return false;
////                }
//            } else {
//                getErrorMessage("Cette mutuelle n'a pas de paramètres");
//                return false;
//            }
//        } else {
//            getErrorMessage("Impossible de continuer car cette agence n'a pas de mutuelle");
//            return false;
//        }
//        String ref = genererReference(Constantes.MUT_ACTIVITE_CREDIT, bean.getDateAvance(), bean.getType().getId(), Constantes.TYPECREDIT);
//        if ((ref != null) ? ref.trim().equals("") : true) {
//            return false;
//        }
//        if ((avanceSelect != null ? (avanceSelect.getId() > 0 ? !avanceSelect.getDateAvance().equals(bean.getDateAvance()) : false) : false)
//                || (bean.getReference() == null || bean.getReference().trim().length() < 1)) {
//            bean.setReference(ref);
//        }
//        if (bean.getDateAvance() != null) {
//            return existExerice(bean.getDateAvance());
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
//        YvsBaseExercice exo_ = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findActifByDate", ch, v);
//        if ((exo_ != null) ? exo_.getId() != 0 : false) {
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void populateView(AvanceSalaire bean) {
//        cloneObject(avanceSalaire, bean);
//        loadAllAvanceMutualiste(bean.getMutualiste(), Util.getFirstDayMonth(new Date()), Util.getLastDayMonth(new Date()));
//        double montant = 0;
//        avanceSalaire.getMutualiste().setMontantTotalEpargne(montant);
//        avanceSalaire.getMutualiste().setMontantResteEpargne(avanceSalaire.getMutualiste().getMontantSalaire() - montant);
//        setConditionAvance(bean);
//        entityAvance = buildAvanceSalaire(bean);
//    }
//
//    @Override
//    public void resetFiche() {
//        resetFiche(avanceSalaire);
//        avanceSalaire.setType(new TypeAvance());
//        avanceSalaire.setMutualiste(new Mutualiste());
//        avanceSalaire.getConditions().clear();
//        avanceSalaire.setDateAvance(new Date());
//        tabIds = "";
//        input_reset = "";
//    }
//
//    @Override
//    public boolean saveNew() {
//        if (input_reset != null && input_reset.equals("reset")) {
//            input_reset = "";
//        }
//        String action = avanceSalaire.getId() > 0 ? "Modification" : "Insertion";
//        try {
//            AvanceSalaire bean = recopieView();
//            bean.setNew_(true);
//            if (controleFiche(bean)) {
//                YvsMutAvanceSalaire entity = buildAvanceSalaire(bean);
//                if (avanceSalaire.getId() < 1) {
//                    entity = (YvsMutAvanceSalaire) dao.save1(entity);
//                    avanceSalaire.setId(entity.getId());
//                    avances.add(0, entity);
//                    avancesMut.add(0, entity);
//                } else {
//                    dao.update(entity);
//                    avances.set(avances.indexOf(entity), entity);
//                    avancesMut.set(avancesMut.indexOf(entity), entity);
//                }
//                succes();
//                update("data_avance");
//                update("data_avance_mutualiste_avance");
//            }
//        } catch (Exception ex) {
//            getErrorMessage(action + " Impossible !");
//        }
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
//                        YvsMutAvanceSalaire bean = avances.get(avances.indexOf(new YvsMutAvanceSalaire(id)));
//                        dao.delete(new YvsMutAvanceSalaire(bean.getId()));
//                        avances.remove(bean);
//                    }
//                    resetFiche();
//                    succes();
//                    update("data_avance");
//                    update("blog_form_avance");
//                }
//            }
//        } catch (NumberFormatException ex) {
//            getErrorMessage("Suppression Impossible !");
//            System.err.println("error suppression gamme article : " + ex.getMessage());
//        }
//    }
//
//    public void deleteBean(YvsMutAvanceSalaire y) {
//        try {
//            if (y != null) {
//                dao.delete(y);
//                avances.remove(y);
//
//                resetFiche();
//                succes();
//                update("data_avance");
//                update("blog_form_avance");
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
//            if ((ids != null) ? ids.length > 0 : false) {
//                long id = Integer.valueOf(ids[ids.length - 1]);
//                YvsMutAvanceSalaire bean = avances.get(avances.indexOf(new YvsMutAvanceSalaire(id)));
//                populateView(UtilMut.buildBeanAvanceSalaire(bean));
//                tabIds = "";
//                input_reset = "";
//                update("blog_form_avance");
//            }
//        }
//    }
//
//    @Override
//    public void onSelectObject(YvsMutAvanceSalaire y) {
//        populateView(UtilMut.buildBeanAvanceSalaire(y));
//        update("blog_form_avance");
//    }
//
//    @Override
//    public void loadOnView(SelectEvent ev) {
//        if ((ev != null) ? ev.getObject() != null : false) {
//            YvsMutAvanceSalaire bean = (YvsMutAvanceSalaire) ev.getObject();
//            onSelectObject(bean);
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
//        avanceSalaire.setMutualiste(bean);
//        loadAllAvanceMutualiste(bean, Util.getFirstDayMonth(new Date()), Util.getLastDayMonth(new Date()));
//        double montant = 0, salaire = searchSalaire(bean);
//        for (YvsMutAvanceSalaire a : avancesMut) {
//            if (a.getEtat().equals(Constantes.ETAT_EDITABLE)) {
//                montant += a.getMontantReste();
//            }
//        }
//        avanceSalaire.getMutualiste().setMontantTotalEpargne(montant);
//        avanceSalaire.getMutualiste().setMontantResteEpargne(salaire - montant);
//        avanceSalaire.getMutualiste().setMontantSalaire(salaire);
//        loadAllMensualite(bean, Util.getFirstDayMonth(new Date()), Util.getLastDayMonth(new Date()));
//        update("blog_form_avance");
//    }
//
//    public void searchMutualiste() {
//        String num = avanceSalaire.getMutualiste().getMatricule();
//        avanceSalaire.getMutualiste().setId(0);
//        avanceSalaire.getMutualiste().setError(true);
//        avanceSalaire.getMutualiste().setEmploye(new Employe());
//        ManagedMutualiste m = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
//        if (m != null) {
//            Mutualiste y = m.searchMutualiste(num, true);
//            if (m.getMutualistes() != null ? !m.getMutualistes().isEmpty() : false) {
//                if (m.getMutualistes().size() > 1) {
//                    update("data_mutualiste_ava");
//                } else {
//                    chooseMutualiste(y);
//                }
//                avanceSalaire.getMutualiste().setError(false);
//            }
//        }
//    }
//
//    public void initMutualistes() {
//        ManagedMutualiste m = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
//        if (m != null) {
//            m.initMutualistes(avanceSalaire.getMutualiste());
//        }
//        update("data_mutualiste_ava");
//    }
//
//    public YvsMutReglementAvance buildReglementAvance(ReglementAvance y) {
//        YvsMutReglementAvance r = new YvsMutReglementAvance();
//        if (y != null) {
//            r.setDateReglement((y.getDateReglement() != null) ? y.getDateReglement() : new Date());
//            r.setId(y.getId());
//            r.setMontant(y.getMontant());
//            r.setAvance(entityAvance);
//        }
//        return r;
//    }
//
//    public boolean controleFicheReglement(ReglementAvance bean) {
//        if (bean.getMontant() == 0) {
//            getErrorMessage("Vous devez entrer le montant!");
//            return false;
//        }
//        return true;
//    }
//
//    public ReglementAvance recopieViewReglement() {
//        ReglementAvance r = new ReglementAvance();
//        r.setDateReglement((reglement.getDateReglement() != null) ? reglement.getDateReglement() : new Date());
//        r.setId(reglement.getId());
//        r.setMontant(reglement.getMontant());
//        return r;
//    }
//
//    public void saveNewReglement() {
//        entityAvance = avanceSelect;
//        ReglementAvance bean = recopieViewReglement();
//        if (controleFicheReglement(bean)) {
//            YvsMutReglementAvance en = buildReglementAvance(bean);
//            en = (YvsMutReglementAvance) dao.save1(en);
//            bean.setId(en.getId());
//            reglement.setId(en.getId());
//            avanceSelect.getReglements().add(en);
//            avances.set(avances.indexOf(avanceSelect), avanceSelect);
//            succes();
//            update("dlgListReglement");
//        }
//    }
//
//    public void chooseTypeAvance() {
//        if (avanceSalaire.getType() != null ? avanceSalaire.getType().getId() > 0 : false) {
//            setConditionAvance();
//        } else if (avanceSalaire.getType() != null ? avanceSalaire.getType().getId() == -1 : false) {
//            openDialog("dlgAddTypeAvance");
//        }
//    }
//
//    public void setConditionAvance() {
//        AvanceSalaire bean = recopieView();
//        if ((bean.getType() != null) ? bean.getType().getId() > 0 : false) {
//            setConditionAvance(bean);
//        }
//    }
//
//    public void setConditionAvance(AvanceSalaire bean) {
//        double salaire = searchSalaire(bean.getMutualiste());  //salaire contractuel
//        TypeAvance typ = bean.getType();
//        if ((typ != null) ? typ.getId() == 0 : false) {
//            ManagedTypeAvance w = (ManagedTypeAvance) giveManagedBean(ManagedTypeAvance.class);
//            if (w != null) {
//                for (YvsMutTypeCredit t : w.getTypes()) {
//                    if (t.getDesignation().equals(typ.getDesignation())) {
//                        typ = UtilMut.buildBeanTypeAvance(t);
//                        break;
//                    }
//                }
//            }
//        }
//        if (avanceSalaire.getId() < 1) {
//            avanceSalaire.getConditions().clear();
//        }
//        double taux, montant_ = 0; //montant total des avances sur salaire
//        double montantMe = 0; //montant total des mensualités
//        int nbreFois = 0;
//        //1. Condition portant sur la periode de demande de l'avance
//        Calendar ca = Calendar.getInstance();
//        ca.setTime(avanceSalaire.getDateAvance());
//        int jour = ca.get(Calendar.DAY_OF_MONTH);
//        YvsMutConditionAvance c = new YvsMutConditionAvance(0, Constantes.MUT_CODE_CONDITION_DATE, Constantes.MUT_LIBELLE_CONDITION_DATE, typ.getJourDebutAvance(), typ.getJourFinAvance(), jour, " ", (typ.getJourDebutAvance() <= jour && typ.getJourFinAvance() > jour));
//        c.setId(findCondition(bean, Constantes.MUT_CODE_CONDITION_DATE));
//        if (c.getId() == 0) {
//            avanceSalaire.getConditions().add(c);
//        } else {
//            avanceSalaire.getConditions().set(avanceSalaire.getConditions().indexOf(c), c);
//        }
//        //2. avance du même type
//        if ((avancesMut != null) ? !avancesMut.isEmpty() : false) {
//            for (YvsMutAvanceSalaire av : avancesMut) {
//                if (av.getType().getId().equals(typ.getId()) && !av.equals(bean) && av.getEtat().equals(Constantes.ETAT_VALIDE)) {
//                    nbreFois++;
//                }
//                montant_ += av.getMontantReste();
//            }
//        }
//        c = new YvsMutConditionAvance(0, Constantes.MUT_CODE_CONDITION_TYPE_AVANCE, Constantes.MUT_LIBELE_CONDITION_TYPE_AVANCE, 1, nbreFois, " ", nbreFois == 0);
//        c.setId(findCondition(bean, Constantes.MUT_CODE_CONDITION_TYPE_AVANCE));
//        if (c.getId() == 0) {
//            avanceSalaire.getConditions().add(c);
//        } else {
//            avanceSalaire.getConditions().set(avanceSalaire.getConditions().indexOf(c), c);
//        }
//        //3.Montant de couverture
//        if ((mensualites != null) ? !mensualites.isEmpty() : false) {
//            for (YvsMutMensualite m : mensualites) {
//                montantMe += m.getMontantReste();
//            }
//        }
//        double couverture = ((salaire - montant_) < 0) ? 0 : (salaire - montant_);
//        c = new YvsMutConditionAvance(0, Constantes.MUT_CODE_CONDITION_MONTANT_COUVERTURE, Constantes.MUT_LIBELLE_CONDITION_MONTANT_COUVERTURE, couverture, avanceSalaire.getMontant(), " Fcfa", couverture > avanceSalaire.getMontant());
//        c.setId(findCondition(bean, Constantes.MUT_CODE_CONDITION_MONTANT_COUVERTURE));
//        if (c.getId() == 0) {
//            avanceSalaire.getConditions().add(c);
//        } else {
//            avanceSalaire.getConditions().set(avanceSalaire.getConditions().indexOf(c), c);
//        }
//        //4. Taux maximale des acompte dans un mois
//        taux = ((montant_) / salaire) * 100;
////        c = new YvsMutConditionAvance(0, Constantes.MUT_CODE_CONDITION_TAUX_MENSUEL, Constantes.MUT_LIBELLE_CONDITION_TAUX_MENSUEL, parametreMutuelle.getTauxGlobalAvanceSalaire(), (taux + (avanceSalaire.getMontant() / salaire * 100)), " %", (parametreMutuelle.getTauxGlobalAvanceSalaire()) >= (taux + (avanceSalaire.getMontant() / salaire * 100)));
//        c.setId(findCondition(bean, Constantes.MUT_CODE_CONDITION_TAUX_MENSUEL));
//        if (c.getId() == 0) {
//            avanceSalaire.getConditions().add(c);
//        } else {
//            avanceSalaire.getConditions().set(avanceSalaire.getConditions().indexOf(c), c);
//        }
//        //5.Montant maximale permis 
//        boolean accorde = false;
//        double mMax = 0;
//        if ((typ != null) ? typ.getId() != 0 : false) {
//            switch (typ.getNatureMontant()) {
//                case "Fixe":
//                    mMax = typ.getMontantMaximal();
//                    accorde = bean.getMontant() < mMax;
//                    break;
//                case "Pourcentage":
//                    mMax = salaire * typ.getMontantMaximal() / 100;
//                    accorde = bean.getMontant() < mMax;
//                    break;
//                default:
//                    break;
//            }
//            c = new YvsMutConditionAvance(0, Constantes.MUT_CODE_CONDITION_MONTANT_MAX, Constantes.MUT_LIBELLE_CONDITION_MONTANT_MAX, mMax, bean.getMontant(), " Fcfa", accorde);
//            c.setId(findCondition(bean, Constantes.MUT_CODE_CONDITION_MONTANT_MAX));
//            if (c.getId() == 0) {
//                avanceSalaire.getConditions().add(c);
//            } else {
//                avanceSalaire.getConditions().set(avanceSalaire.getConditions().indexOf(c), c);
//            }
//        }
//        update("tabview_avance:data_avance_condition");
//    }
//
//    private long findCondition(AvanceSalaire av, String libelle) {
//        for (YvsMutConditionAvance c : av.getConditions()) {
//            if (c.getCode().equals(libelle)) {
//                return c.getId();
//            }
//        }
//        return 0;
//    }
//
//    public void updateEtatCredit(String etat, String motif) {
//        String rq = "UPDATE yvs_mut_avance_salaire SET etat='" + etat + "', motif_refus = '" + motif + "' WHERE id=?";
//        Options[] param = new Options[]{new Options(avanceSalaire.getId(), 1)};
//        dao.requeteLibre(rq, param);
//        avanceSalaire.setEtat(etat);
//        avanceSelect.setMotifRefus(motif);
//        avanceSelect.setEtat(etat);
//        avanceSelect.setMotifRefus(motif);
//        avances.set(avances.indexOf(avanceSelect), avanceSelect);
//        update("data_avance");
//        update("txt_etat_avance");
//        update("view_etat_avance");
//    }
//
//    public void soumettreCredit() {
//        try {
//            for (YvsMutConditionAvance c : avanceSalaire.getConditions()) {
//                if (!c.getCorrect()) {
//                    String message = "La condition suivante n'est pas réalisée : " + c.getLibelle();
//                    getErrorMessage(message);
//                    updateEtatCredit(Constantes.ETAT_ANNULE, message);
//                    return;
//                }
//            }
//            updateEtatCredit(Constantes.ETAT_SOUMIS, "");
//        } catch (Exception ex) {
//            System.err.println("Error Opération " + ex.getMessage());
//            Logger.getLogger(ManagedAvanceSalaire.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void accorderCredit() {
//        try {
//            if (avanceSalaire != null ? avanceSalaire.getId() > 0 : false) {
//                if (!avanceSalaire.getEtat().equals(Constantes.ETAT_SOUMIS)) {
//                    getErrorMessage("Impossible de valider ce crédit!, il est déjà soumis");
//                    return;
//                }
//                YvsMutTypeCredit typ = avanceSelect.getType();
//                if ((typ != null) ? typ.getId() == 0 : false) {
//                    YvsMutTypeCredit y = (YvsMutTypeCredit) dao.loadOneByNameQueries("YvsMutTypeCredit.findByDesignation", new String[]{"designation"}, new Object[]{typ.getDesignation()});
//                    typ = y;
//                }
//                if ((typ != null) ? typ.getId() != 0 : false) {
//                    if (avanceSalaire.getMontant() >= avanceSalaire.getMutualiste().getMontantResteEpargne()) {
//                        getErrorMessage("Le montant demandé ne peut pas etre supérieur au salaire restant");
//                        return;
//                    }
//                    switch (typ.getNatureMontant()) {
//                        case "Fixe":
//                            if (avanceSalaire.getMontant() > typ.getMontantMaximal()) {
//                                getErrorMessage("Le montant demandé ne peut pas etre supérieur au montant prevu");
//                                return;
//                            }
//                            break;
//                        case "Pourcentage":
//                            if (avanceSalaire.getMontant() > (avanceSalaire.getMutualiste().getMontantSalaire() * typ.getMontantMaximal() / 100)) {
//                                getErrorMessage("Le montant demandé ne peut pas etre supérieur au montant prevu");
//                                return;
//                            }
//                            break;
//                        default:
//                            return;
//                    }
//                    updateEtatCredit(Constantes.ETAT_VALIDE, "");
//                    update("date_mensualite_echeancier_avance");
//                } else {
//                    getErrorMessage("Ce credit est invalide!");
//                    updateEtatCredit(Constantes.ETAT_ANNULE, "Le crédit est invalide...veuillez contacter votre administrateur!");
//                }
//            }
//        } catch (Exception ex) {
//            System.err.println("Error Opération " + ex.getMessage());
//            Logger.getLogger(ManagedAvanceSalaire.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void editerCredit() {
//        try {
//            if (avanceSalaire != null ? avanceSalaire.getId() > 0 : false) {
//                if (avanceSalaire.getStatutPaiement() == Constantes.STATUT_DOC_ENCOUR) {
//                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà en cours de paiement");
//                    return;
//                }
//                if (avanceSalaire.getStatutPaiement() == Constantes.STATUT_DOC_PAYER) {
//                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà payé");
//                    return;
//                }
//                if (avanceSalaire.getEtat().equals(Constantes.ETAT_VALIDE)) {
//                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà validé");
//                    return;
//                }
//                if (avanceSalaire.getEtat().equals(Constantes.ETAT_ANNULE)) {
//                    return;
//                }
//                updateEtatCredit(Constantes.ETAT_EDITABLE, "Ce crédit a été annulé....veuillez contacter votre administrateur!");
//            }
//        } catch (Exception ex) {
//            System.err.println("Error Opération " + ex.getMessage());
//            Logger.getLogger(ManagedAvanceSalaire.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void annulerCredit() {
//        try {
//            if (avanceSalaire != null ? avanceSalaire.getId() > 0 : false) {
//                if (avanceSalaire.getStatutPaiement() == Constantes.STATUT_DOC_ENCOUR) {
//                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà en cours de paiement");
//                    return;
//                }
//                if (avanceSalaire.getStatutPaiement() == Constantes.STATUT_DOC_PAYER) {
//                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà payé");
//                    return;
//                }
//                if (avanceSalaire.getEtat().equals(Constantes.ETAT_VALIDE)) {
//                    getErrorMessage("Impossible de modifier le statut de ce crédit!, il est déjà validé");
//                    return;
//                }
//                if (avanceSalaire.getEtat().equals(Constantes.ETAT_ANNULE)) {
//                    return;
//                }
//                updateEtatCredit(Constantes.ETAT_ANNULE, "Ce crédit a été refusé...veuillez contacter votre supérieur!");
//            }
//        } catch (Exception ex) {
//            System.err.println("Error Opération " + ex.getMessage());
//            Logger.getLogger(ManagedAvanceSalaire.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void activeCondition(YvsMutConditionAvance bean) {
//        if (bean != null) {
//            bean.setCorrect(!bean.getCorrect());
//            int idx = avanceSelect.getConditions().indexOf(bean);
//            if (idx > -1) {
//                avanceSelect.getConditions().get(idx).setCorrect(bean.getCorrect());
//            }
//        }
//        update("data_avance_condition_ev");
//    }
//
//    public void initSuivreCredit(YvsMutAvanceSalaire bean) {
//        avanceSelect = bean;
//        initSuivreCredit();
//    }
//
//    public void initSuivreCredit() {
//        if ((avanceSelect != null) ? avanceSelect.getId() != 0 : false) {
//            populateView(UtilMut.buildBeanAvanceSalaire(avanceSelect));
//        }
//        update("view_detail_credit");
//    }
//
//    public void buildForContrainte(YvsMutAvanceSalaire y) {
//        y = (YvsMutAvanceSalaire) dao.loadOneByNameQueries("YvsMutCredit.findById", new String[]{"id"}, new Object[]{y.getId()});
//        avanceSelect = y;
//    }
//
//    /*Permet de rechercher le salaire d'un mutualiste. le salaire du mutualiste sera la moyenne des n dernier salaire perçu. si 
//     ce dernier n'a pas encore de salaire, on prendra son salaire de base (slaire contractuelle + prime)*/
//    private double searchSalaire(Mutualiste mu) {
//        double salaire = 0;
//        Calendar debut_M = Calendar.getInstance();
//        debut_M.setTime(parametreMutuelle.getDebutMois());
//        Calendar debutPeriod = Calendar.getInstance();
//        debutPeriod.set(Calendar.DAY_OF_MONTH, debut_M.get(Calendar.DAY_OF_MONTH));
//        int periodeMoy = (parametreMutuelle.getPeriodeSalaireMoyen() > 0 && parametreMutuelle.getPeriodeSalaireMoyen() < 24) ? parametreMutuelle.getPeriodeSalaireMoyen() : 3;
//        debutPeriod.set(Calendar.MONTH, -(periodeMoy + 1));
//        //recherche les salaire payé entre la date courante et celle  dans current_
//        champ = new String[]{"mutualiste", "debut", "fin"};
//        val = new Object[]{new YvsMutMutualiste(mu.getId()), debutPeriod.getTime(), new Date()};
//        List<YvsMutPaiementSalaire> ls = dao.loadNameQueries("YvsMutPaiementSalaire.findSalairePrecedent", champ, val);
//        if (!ls.isEmpty()) {
//            for (YvsMutPaiementSalaire p : ls) {
//                salaire += (p.getMontantPaye() + p.getSalaire());
//            }
//        } else {
//            salaire = findSalaireContrat(mu);
//        }
//
//        return (salaire == 0) ? 0.01 : salaire;
//    }
//
//    private double findSalaireContrat(Mutualiste mu) {
//        champ = new String[]{"id"};
//        val = new Object[]{mu.getId()};
//        YvsMutMutualiste y = (YvsMutMutualiste) dao.loadOneByNameQueries("YvsMutMutualiste.findById", champ, val);
//        if (y != null) {
//            if (y.getEmploye().getContrat().getSalaireHoraire() > 0 && y.getEmploye().getContrat().getSalaireMensuel() <= 0) {
//                y.getEmploye().getContrat().setSalaireMensuel(y.getEmploye().getContrat().getSalaireHoraire() * 30);
//            }
//            for (YvsGrhElementAdditionel e : y.getEmploye().getContrat().getGainsCumule()) {
//                if (e.getPermanent()) {
//                    y.getEmploye().getContrat().setSalaireMensuel(y.getEmploye().getContrat().getSalaireMensuel() + e.getMontantElement());
//                }
//            }
//            return y.getEmploye().getContrat().getSalaireMensuel();
//        }
//        return 0;
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
//            p.getOtherExpression().add(new ParametreRequete("UPPER(y.reference)", "mutualiste", mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
//            p.getOtherExpression().add(new ParametreRequete("UPPER(y.mutualiste.employe.matricule)", "mutualiste", mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
//            p.getOtherExpression().add(new ParametreRequete("UPPER(y.mutualiste.employe.nom)", "mutualiste", mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
//            p.getOtherExpression().add(new ParametreRequete("UPPER(y.mutualiste.employe.prenom)", "mutualiste", mutualisteSearch.toUpperCase() + "%", "LIKE", "OR"));
//        }
//        paginator.addParam(p);
//        loadAllAvance(true, true);
//    }
//
//    public void addParamDates() {
//        ParametreRequete p = new ParametreRequete("y.dateAvance", "dates", null, "BETWEEN", "AND");
//        if (dateSearch) {
//            p = new ParametreRequete("y.dateAvance", "dates", debutSearch, finSearch, "BETWEEN", "AND");
//        }
//        paginator.addParam(p);
//        loadAllAvance(true, true);
//    }
//
//    public void addParamType() {
//        ParametreRequete p = new ParametreRequete("y.type", "type", null, "LIKE", "AND");
//        if (typeSearch > 0) {
//            p = new ParametreRequete("y.type", "type", new YvsMutTypeCredit(typeSearch), "=", "AND");
//        }
//        paginator.addParam(p);
//        loadAllAvance(true, true);
//    }
//
//    public void addParamStatut() {
//        ParametreRequete p = new ParametreRequete("y.etat", "etat", null, "=", "AND");
//        if (statutSearch != null ? statutSearch.trim().length() > 0 : false) {
//            p = new ParametreRequete("y.etat", "etat", statutSearch, "=", "AND");
//        }
//        paginator.addParam(p);
//        loadAllAvance(true, true);
//    }
//}
