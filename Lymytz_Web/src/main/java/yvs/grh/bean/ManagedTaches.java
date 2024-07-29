/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.taches.YvsGrhTaches;
import yvs.entity.grh.param.YvsGrhIntervalPrimeTache;
import yvs.entity.grh.param.YvsGrhPrimeTache;
import yvs.entity.grh.taches.YvsGrhMontantTache;
import yvs.entity.grh.taches.YvsGrhRegleTache;
import yvs.grh.UtilGrh;
import yvs.util.Managed;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class ManagedTaches extends Managed<RegleDeTache, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{regleDeTache}")
    public RegleDeTache regleDeTache;
    private int idTemp;
    private String searchObject, tabIds;
    private Taches newTache = new Taches(), tache = new Taches();
    private List<YvsGrhTaches> listTache, listTacheRegle;
    private List<YvsGrhRegleTache> selectionsRegles, listRegle;
    private PrimeTache prime = new PrimeTache();
    private YvsGrhPrimeTache selectedPrime;
    private List<YvsGrhPrimeTache> listPrime;

    YvsGrhMontantTache selectedMontantTache;
    YvsGrhRegleTache entityRegleTache;
    private YvsGrhTaches selectedTache;
    YvsGrhIntervalPrimeTache entityInterval;
    private boolean updateTache;
    private boolean updatePrime;
    private boolean updateInterval;
    private boolean updateRegle;

    public ManagedTaches() {
        prime = new PrimeTache();
        newTache = new Taches();
        selectionsRegles = new ArrayList<>();
        listRegle = new ArrayList<>();
        listTacheRegle = new ArrayList<>();
        listTache = new ArrayList<>();
        listPrime = new ArrayList();
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }
    
    

    public YvsGrhTaches getSelectedTache() {
        return selectedTache;
    }

    public void setSelectedTache(YvsGrhTaches selectedTache) {
        this.selectedTache = selectedTache;
    }

    public void setSelectedPrime(YvsGrhPrimeTache selectedPrime) {
        this.selectedPrime = selectedPrime;
    }

    public YvsGrhPrimeTache getSelectedPrime() {
        return selectedPrime;
    }

    public Taches getTache() {
        return tache;
    }

    public void setTache(Taches tache) {
        this.tache = tache;
    }

    public List<YvsGrhTaches> getListTache() {
        return listTache;
    }

    public void setListTache(List<YvsGrhTaches> listTache) {
        this.listTache = listTache;
    }

    public int getIdTemp() {
        return idTemp;
    }

    public void setIdTemp(int idTemp) {
        this.idTemp = idTemp;
    }

    public YvsGrhMontantTache getSelectedMontantTache() {
        return selectedMontantTache;
    }

    public void setSelectedMontantTache(YvsGrhMontantTache selectedMontantTache) {
        this.selectedMontantTache = selectedMontantTache;
    }

    public PrimeTache getPrime() {
        return prime;
    }

    public void setPrime(PrimeTache prime) {
        this.prime = prime;
    }

    public List<YvsGrhPrimeTache> getListPrime() {
        return listPrime;
    }

    public void setListPrime(List<YvsGrhPrimeTache> listPrime) {
        this.listPrime = listPrime;
    }

    public String getSearchObject() {
        return searchObject;
    }

    public void setSearchObject(String searchObject) {
        this.searchObject = searchObject;
    }

    public List<YvsGrhTaches> getListTacheRegle() {
        return listTacheRegle;
    }

    public void setListTacheRegle(List<YvsGrhTaches> listTacheRegle) {
        this.listTacheRegle = listTacheRegle;
    }

    public RegleDeTache getRegleDeTache() {
        return regleDeTache;
    }

    public void setRegleDeTache(RegleDeTache regle) {
        this.regleDeTache = regle;
    }

    public boolean isDisNextEmps() {
        return disNextEmps;
    }

    public void setDisNextEmps(boolean disNextEmps) {
        this.disNextEmps = disNextEmps;
    }

    public boolean isDisPrevEmps() {
        return disPrevEmps;
    }

    public void setDisPrevEmps(boolean disPrevEmps) {
        this.disPrevEmps = disPrevEmps;
    }

    public int getOffsetEmps() {
        return offsetEmps;
    }

    public void setOffsetEmps(int offsetEmps) {
        this.offsetEmps = offsetEmps;
    }

    public List<YvsGrhRegleTache> getSelectionsRegles() {
        return selectionsRegles;
    }

    public void setSelectionsRegles(List<YvsGrhRegleTache> selectionsRegles) {
        this.selectionsRegles = selectionsRegles;
    }

    public List<YvsGrhRegleTache> getListRegle() {
        return listRegle;
    }

    public void setListRegle(List<YvsGrhRegleTache> listRegle) {
        this.listRegle = listRegle;
    }

    public Taches getNewTache() {
        return newTache;
    }

    public void setNewTache(Taches newTache) {
        this.newTache = newTache;
    }

    public String afficheNumPrime(Taches bean) {
        if (bean != null) {
            if (bean.getPrimeTache().getId() == 0) {
                return "Pas de prime";
            } else {
                return "Prime N°" + bean.getPrimeTache().getId();
            }
        }
        return null;
    }

    public void selectMontantTache(Taches bean) {
//        if (bean != null) {
//            if (bean.isAttribuer()) {
//                if (listeSelectMontantTache.size() > 0) {
//                    if (!listeSelectMontantTache.get(0).isAttribuer()) {
//                        listeSelectMontantTache.clear();
//                        for (YvsGrhTaches t : listTacheRegle) {
//                            listTacheRegle.get(listTacheRegle.indexOf(t)).setSelectActif(false);
//                        }
//                    }
//                }
//            } else {
//                if (listeSelectMontantTache.size() > 0) {
//                    if (listeSelectMontantTache.get(0).isAttribuer()) {
//                        listeSelectMontantTache.clear();
//                        for (Taches t : listTacheRegle) {
//                            listTacheRegle.get(listTacheRegle.indexOf(t)).setSelectActif(false);
//                        }
//                    }
//                }
//            }
//            bean.setSelectActif(!bean.isSelectActif());
//            listTacheRegle.get(listTacheRegle.indexOf(bean)).setSelectActif(bean.isSelectActif());
//
//            if (!listeSelectMontantTache.contains(bean)) {
//                listeSelectMontantTache.add(bean);
//            } else {
//                listeSelectMontantTache.remove(bean);
//            }
//            if (listeSelectMontantTache.isEmpty()) {
//                updateTache = false;
//                updateTacheRegle = false;
//                resetFiche(newTache);
//                setViewSelectTacheNonRegle(false);
//            } else {
//                setViewSelectTacheNonRegle(!bean.isAttribuer());
//                if (listeSelectMontantTache.get(listeSelectMontantTache.size() - 1).isAttribuer()) {
//                    populateViewMontantTache(listeSelectMontantTache.get(listeSelectMontantTache.size() - 1));
//                } else {
//                    populateViewTache(listeSelectMontantTache.get(listeSelectMontantTache.size() - 1));
//                }
//            }
//            setNameVuePrime((bean.getPrimeTache().getId() != 0) ? "Modifier prime" : "Lier prime");
//            setOptionUpdateMontantTache(listeSelectMontantTache.size() == 1);
//            setViewSelectMontantTache(!listeSelectMontantTache.isEmpty());
//        }
    }

//    public void changeVuePrimeAll() {
//        setVueListeInterval(!isVueListeInterval());
//        setAffichPrimeOneTacheReche(!affichPrimeOneTacheReche);
//        setNameBtnPrimeTacheRegle((isAffichPrimeOneTacheReche()) ? "Toutes les primes" : "Primes lier à la newTache");
//        setViewSelectPrime(false);
//        listeSelectPrime.clear();
//        if (isAffichPrimeOneTacheReche()) {
//            listPrime.clear();
//            if (newTache.getPrimeTache().getId() != 0) {
//                listPrime.add(newTache.getPrimeTache());
//            }
//        } else {
//            loadAllPrime();
//        }
//        update("form-prime-04");
//        update("entete-prime-00");
//    }
//    public void changeVuePrime() {
//        setVueListePrime(!isVueListePrime());
//        resetFiche(prime);
//        prime.setListInterval(new ArrayList<PrimeTache>());
//        listeSelectPrime.clear();
//        listInterval.clear();
//        setViewSelectPrime(false);
//        setUpdatePrime(false);
//        setViewSelectInterval(false);
//        setDeletePrime(false);
//        entityPrime = new YvsGrhPrimeTache();
//        if (entityPrime.getTranchesPrime() != null) {
//            entityPrime.getTranchesPrime().clear();
//        }
//        if (entityPrime.getYvsMontantTacheList() != null) {
//            entityPrime.getYvsMontantTacheList().clear();
//        }
//        setNameVueInterval((isVueListePrime()) ? "Nouveau" : "Liste");
//        setDefaultBtn((isVueListePrime()) ? "btn_vue_prime" : "btn_new_interval");
//        update("blog-prime-00");
//        update("entete-prime-00");
//        update("footer-prime-01");
//    }
    public void changeVueTache() {
        resetFiche(newTache);
        updateTache = (false);
        newTache.setAttribuer(false);
        update("blog-tache-00");
        update("entete-tache-00");
        update("form-tache-02");
    }

    public YvsGrhRegleTache buildRegleTache(RegleDeTache t) {
        YvsGrhRegleTache de = new YvsGrhRegleTache();
        de.setActif(t.getActif());
        de.setSupp(t.isSupp());
        de.setAgence(currentAgence);
        de.setNom(t.getDesignation());
        de.setCode(t.getCode());
        de.setId(t.getId());
        de.setAuthor(currentUser);
        return de;
    }

    public YvsGrhPrimeTache buildPrimeTache(PrimeTache bean) {
        YvsGrhPrimeTache p = new YvsGrhPrimeTache();
        if (bean != null) {
            p.setId((int) bean.getId());
            p.setDatePrime(new Date());
            p.setActif(bean.isActif());
            p.setSociete(currentAgence.getSociete());
        }
        return p;
    }

    public List<YvsGrhIntervalPrimeTache> buildListIntervalPrime(List<PrimeTache> list) {
        List<YvsGrhIntervalPrimeTache> result = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (PrimeTache p : list) {
                    result.add(buildIntervalPrime(p));
                }
            }
        }
        return result;
    }

    public boolean controleFichePrime(PrimeTache bean) {
        if (bean.getMontant() <= 0) {
            getMessage("Vous devez entrer un montant", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getQuantite() <= 0) {
            getMessage("Vous devez entrer une quantite", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (selectedPrime == null) {
            getErrorMessage("Vous devez selectionner un model de prime !");
            return false;
        }
        return true;
    }

    public YvsGrhIntervalPrimeTache buildIntervalPrime(PrimeTache bean) {
        YvsGrhIntervalPrimeTache p = new YvsGrhIntervalPrimeTache();
        p.setId((long) bean.getId());
        p.setMontant(bean.getMontant());
        p.setQuantite(bean.getQuantite());
        p.setTaux(bean.isTaux());
        return p;
    }

    public void addInterval() {
        if (controleFichePrime(prime)) {
            YvsGrhIntervalPrimeTache in = buildIntervalPrime(prime);
            in.setAuthor(currentUser);
            in.setPrimeTache(selectedPrime);
            in.setId(null);
            in = (YvsGrhIntervalPrimeTache) dao.save1(in);
            prime.setId(in.getId().intValue());
            selectedPrime.getTranchesPrime().add(in);
        }
    }

    public void addIntervalPrime() {
        if (controleFichePrime(prime)) {
            YvsGrhPrimeTache p1 = new YvsGrhPrimeTache(idTemp);
            if (updateInterval) {
                prime.setId(idTemp);
                prime.setSelectActif(false);
                PrimeTache p = new PrimeTache();
                cloneObject(p, prime);
//                listInterval.add(p);
                resetFiche(prime);
            } else {
                YvsGrhPrimeTache p = new YvsGrhPrimeTache();
                cloneObject(p, prime);
            }
            update("form-prime-02");
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (selectionsRegles != null) {
                if (!selectionsRegles.isEmpty()) {
//                    for (RegleDeTache reg : selectionsRegles) {
//                        dao.delete(new YvsGrhRegleTache(reg.getId()));
//                        listRegle.remove(new RegleDeTache(reg.getId()));
//                    }
                    selectionsRegles.clear();
                    succes();
                    update("bloc-all-vue");
                    update("entete-regle-00");
                }
            }
        } catch (Exception ex) {
            getMessage("La regle est rattachee a d autres elements ", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void deleteBeanTache() {
//        try {
//            if (listeSelectTache != null) {
//                if (!listeSelectTache.isEmpty()) {
//                    for (Taches t : listeSelectTache) {
//                        dao.delete(new YvsGrhTaches(t.getId()));
//                        listTache.remove(t);
//                        if (listTacheRegle != null) {
//                            listTacheRegle.remove(t);
//                        }
//                    }
//                    listeSelectTache.clear();
//                    setUpdateTache(false);
//                    setResetView(false);
//                    setViewSelectTacheNonRegle(false);
//                    setViewSelect(false);
//                    setOptionUpdateMontantTache(false);
//
//                    setVueListeTache(true);
//                    setViewSelectMontantTache(false);
//                    setViewSelectTacheNonRegle(false);
//                    setNameVueTache((isVueListeTache()) ? "Nouveau" : "Liste");
//
//                    if (isOpenDialogTache()) {
//                        update("blog-newTache-00");
//                        update("entete-newTache-00");
//                        update("form-newTache-02");
//                    }
//                    update("bloc-all-vue");
//                    update("entete-regle-00");
//
//                    succes();
//                }
//            }
//        } catch (Exception ex) {
//            getMessage("La newTache est rattachee a d autres elements ", FacesMessage.SEVERITY_ERROR);
//        }
    }

    public void deleteBeanTacheRegle() {
//        try {
//            if (listeSelectMontantTache != null) {
//                if (!listeSelectMontantTache.isEmpty()) {
//                    System.err.println("regleDeTache.getMontantTotal " + regleDeTache.getMontantTotal());
//                    for (Taches reg : listeSelectMontantTache) {
//                        if (reg.isAttribuer()) {
//                            dao.delete(new YvsGrhMontantTache());
//                            regleDeTache.getListeTache().remove(new Taches(reg.getId()));
//                            listTacheRegle.remove(reg);
//                            regleDeTache.setMontantTotal(regleDeTache.getMontantTotal() - reg.getMontant());
//                        }
//                    }
//                    listeSelectMontantTache.clear();
//
//                    RegleDeTache r = new RegleDeTache();
//                    cloneObject(r, regleDeTache);
//                    listRegle.set(listRegle.indexOf(regleDeTache), r);
//
//                    setUpdateTacheRegle(false);
//                    setViewSelectMontantTache(false);
//                    loadAllTacheRegle();
//
//                    setUpdateTache(false);
//                    setViewSelectTacheNonRegle(false);
//
//                    setVueListeTache(true);
//                    setNameVueTache((isVueListeTache()) ? "Nouveau" : "Liste");
//
//                    update("blog-newTache-00");
//                    update("entete-newTache-00");
//                    update("form-newTache-00");
//                    update("form-regle-04");
//
//                    succes();
//                }
//            }
//        } catch (Exception ex) {
//            getMessage("Le montant newTache est rattachee a d autres elements ", FacesMessage.SEVERITY_ERROR);
//        }
    }

    public void deleteBeanIntervalPrime() {
        try {
//            if (listeSelectInterval != null) {
//                if (!listeSelectInterval.isEmpty()) {
//                    for (YvsGrhPrimeTache reg : listeSelectInterval) {
//                        listInterval.remove(reg);
//                    }
//                    listeSelectInterval.clear();
//                    resetFiche(prime);
//                    prime.setListInterval(new ArrayList<PrimeTache>());
//                    prime.setListTache(new ArrayList<YvsGrhTaches>());
//                    updateInterval = (false);
//                    update("form-prime-03");
//                    update("form-prime-02");
//                }
//            }
        } catch (Exception ex) {
            getMessage("L'interval est rattachee a d autres elements ", FacesMessage.SEVERITY_ERROR);
        }
    }

    public boolean controleFicheTache(Taches bean) {
        if ((bean.getCodeTache() == null) ? true : bean.getCodeTache().equals("")) {
            getMessage("Vous devez indiquer le code", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if ((bean.getDesignation() == null) ? true : bean.getDesignation().equals("")) {
            getMessage("Vous devez spécifier le module", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    public boolean controleFicheMontantTache(Taches bean) {
        if (bean.getMontant() <= 0) {
            getMessage("Vous devez entrer un montant", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void updateBean() {
//        entityRegleTache = buildRegleTache(regleDeTache);
//        entityRegleTache = (YvsGrhRegleTache) dao.update(entityRegleTache);
//        RegleDeTache r = new RegleDeTache();
//        cloneObject(r, regleDeTache);

    }

    @Override
    public RegleDeTache recopieView() {
        RegleDeTache t = new RegleDeTache(regleDeTache.getId(), regleDeTache.getDesignation());
        return t;
    }

//    public void populateViewMontantTache(Taches bean) {
//        cloneObject(newTache, bean);
//        selectedMontantTache = buildMontantTache(newTache);
//    }
    public void populateViewInterval(PrimeTache bean) {
        prime.setId(bean.getId());
        prime.setMontant(bean.getMontant());
        prime.setQuantite(bean.getQuantite());
        prime.setTaux(bean.isTaux());
        if (updatePrime) {
            entityInterval = buildIntervalPrime(prime);
        }
    }

    @Override
    public void populateView(RegleDeTache bean) {
        regleDeTache.setActif(bean.isActif());
        regleDeTache.setCode(bean.getCode());
        regleDeTache.setSupp(bean.isSupp());
        regleDeTache.setId(bean.getId());
        regleDeTache.setDesignation(bean.getDesignation());
        regleDeTache.setListeTache(bean.getListeTache());
        entityRegleTache = buildRegleTache(regleDeTache);
    }

    public void loadOnViewPrime(SelectEvent ev) {
        YvsGrhPrimeTache bean = (YvsGrhPrimeTache) ev.getObject();
        update("form-prime-01");
        update("entete-regle-00");
    }

    public void loadDataPrime(YvsGrhPrimeTache bean) {
        prime = UtilGrh.buildBeanPrimeTache(bean);
    }

    public void loadPrime(SelectEvent ev) {
        YvsGrhPrimeTache bean = (YvsGrhPrimeTache) ev.getObject();
        prime = UtilGrh.buildBeanPrimeTache(bean);
    }

    public void loadOnViewIntervalPrime(SelectEvent ev) {
        YvsGrhPrimeTache bean = (YvsGrhPrimeTache) ev.getObject();
        update("tab-intervalPrime-01");
        update("form-prime-02");
    }

    public void affectePrime() {
        if (selectedMontantTache != null) {
            affectePrime_(listPrime.get(listPrime.indexOf(selectedPrime)));
        } else {
            getErrorMessage("Vous devez selectionner une tâche !");
        }
    }

    public void affectePrime_(YvsGrhPrimeTache pr) {
        if (selectedMontantTache != null) {
            selectedMontantTache.setPrimeTache(pr);
            selectedMontantTache.setActif(true);
            dao.update(selectedMontantTache);
            update("tableRegle_Tache");
            update("titre_prime_t");
        } else {
            getErrorMessage("Vous devez selectionner une tâche !");
        }
    }

    public void createNewPrime() {
        if (prime.getReference() != null) {
            YvsGrhPrimeTache prim = new YvsGrhPrimeTache();
            prim.setActif(true);
            prim.setAuthor(currentUser);
            prim.setReferencePrime(prime.getReference());
            prim.setDatePrime(new Date());
            prim.setDateSave(new Date());
            prim.setDateUpdate(new Date());
            prim = (YvsGrhPrimeTache) dao.save1(prim);
            prime.setId(prim.getId());
            listPrime.add(0, prim);
            selectedPrime = prim;
        } else {
            getErrorMessage("Veillez entrer la référence de la prime !");
        }
    }

    public void removePrime(YvsGrhPrimeTache prim) {
        try {
            if (prim != null) {
                prim.setAuthor(currentUser);
                dao.delete(prim);
                listPrime.remove(prim);
                succes();
            }
        } catch (Exception ex) {
            getMessage("La prime est rattachee a d autres elements ", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void loadOnViewInterval(SelectEvent ev) {
        populateViewInterval((PrimeTache) ev.getObject());
        updateInterval = true;
        update("form-prime-03");
        update("footer-prime-01");
    }

    public void loadOnViewInterval2(SelectEvent ev) {
    }

    public void loadOnViewMontantTache(SelectEvent ev) {
        Taches bean = (Taches) ev.getObject();
        selectMontantTache(bean);
        update("form-tache-04");
        update("form-tache-02");
        update("entete-tache-00");
    }

    public void loadDataTache(SelectEvent ev) {
        YvsGrhMontantTache bean = (YvsGrhMontantTache) ev.getObject();
        openTacheToLinkPrime(bean);
    }

    public void openTacheToLinkPrime(YvsGrhMontantTache t) {
        newTache = UtilGrh.buildBeanTaches(t.getTaches());
        selectedPrime = selectedMontantTache.getPrimeTache();
    }

    public void toogleActiveTache(YvsGrhMontantTache mt) {
        selectedMontantTache = mt;
        selectedMontantTache.setActif(!selectedMontantTache.isActif());
        dao.update(selectedMontantTache);
    }

    public void selectRegle(YvsGrhRegleTache bean) {
        populateView(UtilGrh.buildBeanRegleDeTache(bean));
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        YvsGrhRegleTache regle = (YvsGrhRegleTache) ev.getObject();
        selectRegle(regle);
    }

    public void searchTache() {
        if (getSearchObject().equals("")) {
            loadAllTaches();
        } else {
            listTache.clear();
           champ = new String[]{"codeTache", "agence"};
           val = new Object[]{"%" + getSearchObject() + "%", currentAgence};
            listTache = dao.loadNameQueries("YvsTaches.findByCodeModule", champ, val);
        }
        update("form-tache-01");
    }

    public void searchRegleTache() {
        if (getSearchObject().equals("")) {
            loadAll();
        } else {
            champ = new String[]{"agence", "code"};
            val = new Object[]{currentAgence, "%" + getSearchObject() + "%"};
            listRegle = dao.loadNameQueries("YvsRegleTache.findByCodeNom", champ, val);
        }
        update("form-regle-04");
    }

    @Override
    public void loadAll() {
        champ = new String[]{"societe"};
        val = new Object[]{currentUser.getAgence().getSociete()};
        listRegle = dao.loadNameQueries("YvsRegleTache.findAll", champ, val);
        loadAllTaches();
        listPrime = dao.loadNameQueries("YvsGrhPrimeTache.findAll", champ, val);
    }

    public void loadAllTaches() {
        champ = new String[]{"societe"};
        val = new Object[]{currentUser.getAgence().getSociete()};
        listTache = dao.loadNameQueries("YvsTaches.findAll", champ, val);
    }

    public void loadAllMontantTache() {
        champ = new String[]{"regleTache"};
        val = new Object[]{entityRegleTache.getId()};
        regleDeTache.setListeTache(dao.loadNameQueries("YvsMontantTache.findByRegleTache", champ, val));
    }

    public void loadAllTacheRegle() {
        listTacheRegle.clear();
        champ = new String[]{"agence"};
        val = new Object[]{currentAgence};
        List<Taches> list1 = UtilGrh.buildBeanListTaches(dao.loadNameQueries("YvsTaches.findAll", champ, val));

        String[] champ1 = new String[]{"regleTache"};
        Object[] val1 = new Object[]{entityRegleTache.getId()};
        List<Taches> list2 = UtilGrh.buildBeanListMontantTache(dao.loadNameQueries("YvsMontantTache.findByRegleTache", champ1, val1));
        for (Taches t1 : list1) {
            for (Taches t2 : list2) {
                if (t1.getId() == t2.getId()) {
                    t1.setMontant(t2.getMontant());
                    t1.setAttribuer(true);
                    if (t2.getPrimeTache() != null) {
                        t1.setPrimeTache(t2.getPrimeTache());
                    }
                    break;
                } else {
                    t1.setMontant(0.0);
                    t1.setAttribuer(false);
                }
            }
        }
    }
    private boolean disNextEmps, disPrevEmps;
    private int offsetEmps;

//    public List<Employe> buildListEmployeBean(List<YvsGrhEmployes> l) {
//        List<Employe> result = new ArrayList<>();
//        for (YvsGrhEmployes e : l) {
//            champ = new String[]{"employe"};
//             val = new Object[]{e};
//            YvsGrhContratEmps c = (YvsGrhContratEmps) dao.loadOneByNameQueries("YvsGrhContratEmps.findByPrincipalEmploye", champ, val);
//            if ((c != null) ? c.getId() != 0 : false) {
//                if (((c.getRegleTache() != null) ? c.getRegleTache().getId() != 0 : false)) {
//                    if ((!c.getRegleTache().equals(new YvsGrhRegleTache(regleDeTache.getId())))) {
//                        Employe ee = UtilGrh.buildBeanPartialEmploye(e);
//                        result.add(ee);
//                    }
//                } else {
//                    Employe ee = UtilGrh.buildBeanPartialEmploye(e);
//                    result.add(ee);
//                }
//            }
//        }
//        return result;
//    }

    @Override
    public boolean controleFiche(RegleDeTache bean) {
        if ((bean.getCode() == null) ? true : bean.getCode().equals("")) {
            getMessage("Vous devez indiquer le code !", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if ((bean.getDesignation() == null) ? true : bean.getDesignation().equals("")) {
            getMessage("Vous devez spécifier la designation", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;

    }

    @Override
    public boolean saveNew() {
        if (controleFiche(regleDeTache)) {
            if (!updateRegle) {
                regleDeTache.setActif(true);
                regleDeTache.setSupp(false);
                entityRegleTache = buildRegleTache(regleDeTache);
                entityRegleTache.setId(null);
                entityRegleTache = (YvsGrhRegleTache) dao.save1(entityRegleTache);
                regleDeTache.setId(entityRegleTache.getId());
                RegleDeTache r = new RegleDeTache();
                cloneObject(r, regleDeTache);
                listRegle.add(0, entityRegleTache);
            } else {
                RegleDeTache r = new RegleDeTache();
                cloneObject(r, regleDeTache);
                dao.update(entityRegleTache);
                listRegle.set(listRegle.indexOf(regleDeTache), entityRegleTache);
            }
            updateRegle = true;
            succes();
        }
        return true;
    }

    public void openTache(SelectEvent ev) {
        if (ev != null) {
            YvsGrhTaches t = (YvsGrhTaches) ev.getObject();
            newTache = UtilGrh.buildBeanTaches(t);
            updateTache = true;
        }
    }

    public void choisirLaTache(YvsGrhTaches t) {
        tache = UtilGrh.buildBeanTaches(t);
    }

    public YvsGrhTaches buildTache(Taches t) {
        YvsGrhTaches de = new YvsGrhTaches();
        de.setActif(t.isActif());
        de.setSupp(t.isSupp());
        de.setAgence(currentAgence);
        de.setModuleTache(t.getDesignation());
        de.setCodeTache(t.getCodeTache());
        de.setDescription(t.getDescription());
        de.setId(t.getId());
        de.setAuthor(currentUser);
        return de;
    }

    public boolean saveNewTache() {
        if (controleFicheTache(newTache)) {
            YvsGrhTaches entityTache;
            if (!updateTache) {
                entityTache = buildTache(newTache);
                entityTache.setId(null);
                entityTache = (YvsGrhTaches) dao.save1(entityTache);
                newTache.setId(entityTache.getId());
                newTache.setAttribuer(false);
                listTache.add(0, entityTache);
            } else {
                entityTache = buildTache(newTache);
                dao.update(entityTache);
                listTache.set(listTache.indexOf(newTache), entityTache);
            }
            newTache = new Taches();
            updateRegle = false;
        }
        return true;
    }

    public YvsGrhMontantTache buildMontantTache(Taches t) {
        YvsGrhMontantTache mtnT = new YvsGrhMontantTache();
        if ((t != null) ? t.getId() > 0 : false) {
            mtnT.setMontant(t.getMontant());
            mtnT.setActif(t.isActif());
            mtnT.setSupp(t.isSupp());
            mtnT.setPrimeTache(buildPrimeTache(t.getPrimeTache()));
            mtnT.setId(t.getId());
            mtnT.setRegleTache(new YvsGrhRegleTache(regleDeTache.getId()));
            mtnT.setTaches(new YvsGrhTaches(newTache.getId()));
            return mtnT;
        } else {
            getErrorMessage("Vous devez selectionner une tâche ");
        }
        return null;
    }

    public boolean saveNewMontantTache() {
        if (controleFicheMontantTache(tache)) {
            selectedMontantTache = new YvsGrhMontantTache();
            if (tache.getId() > 0) {
                selectedMontantTache.setTaches(listTache.get(listTache.indexOf(new YvsGrhTaches(tache.getId()))));
            }
            if (regleDeTache.getId() > 0) {
                selectedMontantTache.setRegleTache(new YvsGrhRegleTache(regleDeTache.getId()));
            }
            selectedMontantTache.setMontant(tache.getMontant());
            selectedMontantTache.setAuthor(currentUser);
            if (selectedMontantTache != null) {
                selectedMontantTache.setPrimeTache(null);
                selectedMontantTache.setId(null);
                selectedMontantTache = (YvsGrhMontantTache) dao.save1(selectedMontantTache);
                tache.setId(selectedMontantTache.getId());
                regleDeTache.getListeTache().add(0, selectedMontantTache);
                tache = new Taches();
            }
        }
        return true;
    }

    public boolean savePrimeMontantTache() {
        if (prime != null) {
//            prime = UtilGrh.buildBeanPrimeTache(entityPrime);
//            if (listeSelectMontantTache != null) {
//                if (!listeSelectMontantTache.isEmpty()) {
//                    for (Taches t : listeSelectMontantTache) {
//                        if (t.getPrimeTache().equals(prime)) {
//                            selectedMontantTache = buildMontantTache(t);
//                            String rq = "UPDATE yvs_montant_tache SET prime_tache = null WHERE regle_tache=? AND newTache=?";
//                            Options[] param = new Options[]{new Options(selectedMontantTache.getYvsRegleTache().getId(), 1),
//                                new Options(selectedMontantTache.getYvsTaches().getId(), 2)};
//                            dao.requeteLibre(rq, param);
//                        } else {
//                            t.setPrimeTache(prime);
//                            selectedMontantTache = buildMontantTache(t);
//                            selectedMontantTache = (YvsGrhMontantTache) dao.update(selectedMontantTache);
//                        }
//                    }
//                }
//            }
//            listeSelectMontantTache.clear();
//            loadAllTacheRegle();
//            setOptionUpdateMontantTache(listeSelectMontantTache.size() == 1);
//            setViewSelectMontantTache(!listeSelectMontantTache.isEmpty());
//            viewSelectTacheNonRegle = false;

            update("blog-tache-00");
            update("form-tache-02");
            update("entete-tache-00");
            closeDialog("dlgListePrime");
            succes();
        }
        return true;
    }

    @Override
    public void resetFiche() {
        resetFiche(regleDeTache);
        regleDeTache.getListeTache().clear();
        updateRegle = false;
    }

    public void resetFicheTache() {
        newTache = new Taches();
        updateTache = false;
    }

    public void resetFichePrime() {
        prime = new PrimeTache();
        updatePrime = false;
    }

    public void resetFicheIntervalPrime() {
        resetFiche(prime);
        resetPageIntervalPrime();
        update("form-prime-02");
    }

    public void resetPageIntervalPrime() {
//        for (PrimeTache i : listInterval) {
//            listInterval.get(listInterval.indexOf(i)).setSelectActif(false);
//        }
        update("tab-intervalPrime-01");
    }

    @Override
    public void resetPage() {

    }

    public void resetVue() {
//        if (isActiveVueRegle()) {
//            selectionsRegles.clear();
//            for (RegleDeTache r : listRegle) {
//                listRegle.get(listRegle.indexOf(r)).setSelectActif(false);
//            }
//            update("bloc-vue-regle");
//        } else if (isActiveVueTache()) {
//            listeSelectTache.clear();
//            for (Taches t : listTache) {
//                listTache.get(listTache.indexOf(t)).setSelectActif(false);
//            }
//            update("bloc-vue-newTache");
//        } else if (isActiveVuePrime()) {
//            listeSelectPrime.clear();
//            for (PrimeTache p : listPrime) {
//                listPrime.get(listPrime.indexOf(p)).setSelectActif(false);
//            }
//            update("bloc-vue-prime");
//        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
