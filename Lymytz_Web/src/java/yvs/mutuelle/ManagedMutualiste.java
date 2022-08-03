/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.dao.Options;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.mutuelle.base.YvsMutPoste;
import yvs.entity.mutuelle.base.YvsMutPosteEmploye;
import yvs.entity.mutuelle.base.YvsMutTypeCompte;
import yvs.entity.mutuelle.credit.YvsMutAvaliseCredit;
import yvs.entity.mutuelle.credit.YvsMutCredit;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.bean.Employe;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.mutuelle.base.ManagedTypeCompte;
import yvs.mutuelle.base.TypeCompte;
import yvs.mutuelle.operation.ManagedEpargne;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedMutualiste extends Managed<Mutualiste, YvsMutMutualiste> implements Serializable {

    @ManagedProperty(value = "#{mutualiste}")
    private Mutualiste mutualiste;
    private YvsMutMutualiste mutualisteSelect;
    private List<YvsMutMutualiste> mutualistes;
//    private Poste poste = new Poste();

    private Compte compte = new Compte();
    private List<YvsMutCompte> comptes;
    private Long[] choixComptes;

    private Parametre parametre = new Parametre();

    private List<YvsMutAvaliseCredit> avalises;
    private Date dateJour = new Date();

    private Boolean actifSearch;
    private String numSearch;

    private String tabIds, input_reset, tabIds_poste, input_reset_poste, tabIds_compte, input_reset_compte;
    private boolean updateMutualiste, updatePoste;

    YvsMutMutualiste entityMutualiste;

    public ManagedMutualiste() {
        mutualistes = new ArrayList<>();
        avalises = new ArrayList<>();
        comptes = new ArrayList<>();
    }

    public List<YvsMutCompte> getComptes() {
        return comptes;
    }

    public void setComptes(List<YvsMutCompte> comptes) {
        this.comptes = comptes;
    }

//    public Poste getPoste() {
//        return poste;
//    }
//
//    public void setPoste(Poste poste) {
//        this.poste = poste;
//    }
    public YvsMutMutualiste getEntityMutualiste() {
        return entityMutualiste;
    }

    public void setEntityMutualiste(YvsMutMutualiste entityMutualiste) {
        this.entityMutualiste = entityMutualiste;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public Parametre getParametre() {
        return parametre;
    }

    public void setParametre(Parametre parametre) {
        this.parametre = parametre;
    }

    public Date getDateJour() {
        return dateJour;
    }

    public void setDateJour(Date dateJour) {
        this.dateJour = dateJour;
    }

    public YvsMutMutualiste getMutualisteSelect() {
        return mutualisteSelect;
    }

    public void setMutualisteSelect(YvsMutMutualiste mutualisteSelect) {
        this.mutualisteSelect = mutualisteSelect;
    }

    public List<YvsMutAvaliseCredit> getAvalises() {
        return avalises;
    }

    public void setAvalises(List<YvsMutAvaliseCredit> avalises) {
        this.avalises = avalises;
    }

    public Long[] getChoixComptes() {
        return choixComptes;
    }

    public void setChoixComptes(Long[] choixComptes) {
        this.choixComptes = choixComptes;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public String getTabIds_compte() {
        return tabIds_compte;
    }

    public void setTabIds_compte(String tabIds_compte) {
        this.tabIds_compte = tabIds_compte;
    }

    public String getInput_reset_compte() {
        return input_reset_compte;
    }

    public void setInput_reset_compte(String input_reset_compte) {
        this.input_reset_compte = input_reset_compte;
    }

    public String getTabIds_poste() {
        return tabIds_poste;
    }

    public void setTabIds_poste(String tabIds_poste) {
        this.tabIds_poste = tabIds_poste;
    }

    public String getInput_reset_poste() {
        return input_reset_poste;
    }

    public void setInput_reset_poste(String input_reset_poste) {
        this.input_reset_poste = input_reset_poste;
    }

    public boolean isUpdatePoste() {
        return updatePoste;
    }

    public void setUpdatePoste(boolean updatePoste) {
        this.updatePoste = updatePoste;
    }

    public Mutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(Mutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    public List<YvsMutMutualiste> getMutualistes() {
        return mutualistes;
    }

    public void setMutualistes(List<YvsMutMutualiste> mutualistes) {
        this.mutualistes = mutualistes;
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

    public boolean isUpdateMutualiste() {
        return updateMutualiste;
    }

    public void setUpdateMutualiste(boolean updateMutualiste) {
        this.updateMutualiste = updateMutualiste;
    }

    @Override
    public void loadAll() {
        loadAllParametre();
        loadAllMutualiste(true, true);
        if (currentMutuel != null) {
            mutualiste.setMontantEpargne((currentMutuel.getMontantEpargne() != null) ? currentMutuel.getMontantEpargne() : 0);
        }

        tabIds = "";
        tabIds_compte = "";
        tabIds_poste = "";

        input_reset = "";
        input_reset_compte = "";
        input_reset_poste = "";
    }

    public void loadAllParametre() {
        if (currentMutuel != null) {
            if (currentMutuel.getParamsMutuelle() != null) {
                parametre = UtilMut.buildBeanParametre(currentMutuel.getParamsMutuelle());
            }
        }
    }

    public void loadAllMutualiste(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.mutuelle", "mutuelle", currentMutuel, "=", "AND"));
        mutualistes = paginator.executeDynamicQuery("YvsMutMutualiste", "y.employe.matricule,y.employe.nom, y.employe.prenom", avance, init, (int) imax, dao);
        for (YvsMutMutualiste m : mutualistes) {
            //Cherche le solde du compte credit (détermine ce qu'un mutualiste doit encore à la mutuelle)
            m.setMontantCredit(giveSoldeCredit(m, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS));
            //récupère le montant total de l'épargne
            m.setMontantTotalEpargne(findSoldeMutualisteByNature(m.getId(), Constantes.MUT_NATURE_OP_EPARGNE));
            //récupère le montant total de l'assurance
            m.setMontantTotalAssurance(findSoldeMutualisteByNature(m.getId(), Constantes.MUT_NATURE_OP_ASSURANCE));
            //récupère le montant des avalise
            m.setCouvertureAvalise(giveCouvertureAvalise(m, Constantes.ETAT_VALIDE));
        }
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsMutMutualiste> re = paginator.parcoursDynamicData("YvsMutMutualiste", "y", "y.employe.nom, y.employe.prenom", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void loadCompte(String type) {
        if (type != null ? type.trim().length() > 0 : false) {
            nameQueri = "YvsMutCompte.findByType";
            champ = new String[]{"mutuelle", "type"};
            val = new Object[]{currentMutuel, type};
        } else {
            nameQueri = "YvsMutCompte.findByMutuelle";
            champ = new String[]{"mutuelle"};
            val = new Object[]{currentMutuel};
        }
        comptes = dao.loadNameQueries(nameQueri, champ, val);
    }

//    public double giveSoldeEpargne(YvsMutMutualiste m) {
//        return giveSoldeCompte(m, Constantes.MUT_TYPE_COMPTE_EPARGNE);
//    }
//    public double giveSoldeCompte(long idCompte) {
//        return dao.getSoldeCompteMutualiste(idCompte);
//    }
//    public double giveSoldeCompte(YvsMutMutualiste m, String compte) {
//        champ = new String[]{"mutualiste", "nature"};
//        val = new Object[]{m, compte};
//        Double d = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findSoldeCompte", champ, val);
//        return (d != null) ? d : 0;
//    }
    public double giveSoldeCredit(YvsMutMutualiste m, String etatCredit, String etatEchelonage) {
        champ = new String[]{"mutualiste", "etat", "etatEch"};
        val = new Object[]{m, etatCredit, etatEchelonage};
        Double d = (Double) dao.loadObjectByNameQueries("YvsMutMensualite.findSoldeCredit", champ, val);
        Double r = (Double) dao.loadObjectByNameQueries("YvsMutReglementMensualite.findSoldeCredit", champ, val);
        return (((d != null) ? d : 0) - ((r != null) ? r : 0));
    }

    public double giveCouvertureAvalise(YvsMutMutualiste m, String etatCredit) {
        champ = new String[]{"mutualiste", "etat"};
        val = new Object[]{m, etatCredit};
        Double d = (Double) dao.loadObjectByNameQueries("YvsMutAvaliseCredit.findMontantAvalise", champ, val);
        return (d != null) ? d : 0;
    }

    public void loadActif(Boolean actif) {
        actifSearch = actif;
        addParamActif();
    }

    public void paginer(boolean next) {
        loadAllMutualiste(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAllMutualiste(true, true);
    }

    public void loadAllAvalise(Mutualiste bean) {
        Exercice e;
        if ((currentExo != null) ? currentExo.getId() > 0 : false) {
            e = UtilMut.buildBeanExercice(currentExo);
        } else {
            e = Exercice.getDefault();
        }
        String[] ch = new String[]{"mutualiste", "dateDebut", "dateFin"};
        Object[] v = new Object[]{new YvsMutMutualiste(bean.getId()), e.getDateDebut(), e.getDateFin()};
        avalises = dao.loadNameQueries("YvsMutAvaliseCredit.findByMutualisteDates", ch, v);
    }

    public YvsMutMutualiste buildMutualiste(Mutualiste y) {
        YvsMutMutualiste m = new YvsMutMutualiste();
        if (y != null) {
            m.setId(y.getId());
            m.setDateAdhesion((y.getDateAdhesion() != null) ? y.getDateAdhesion() : new Date());
            if ((y.getEmploye() != null) ? y.getEmploye().getId() != 0 : false) {
                m.setEmploye(UtilGrh.buildEmployeEntity(y.getEmploye()));
            }
            m.setMutuelle(currentMutuel);
            m.setActif(y.isActif());
            m.setMontantEpargne(y.getMontantEpargne());
            m.setAssistance(y.isAssistance());
            m.setActif(y.isActif());
            m.setAuthor(currentUser);
            m.setDateSave(y.getDateSave());
            m.setDateUpdate(new Date());
        }
        return m;
    }

    @Override
    public Mutualiste recopieView() {
        Mutualiste m = new Mutualiste();
        m.setId(mutualiste.getId());
        m.setDateAdhesion((mutualiste.getDateAdhesion() != null) ? mutualiste.getDateAdhesion() : new Date());
        if ((mutualiste.getEmploye() != null) ? mutualiste.getEmploye().getId() != 0 : false) {
            ManagedEmployes s = (ManagedEmployes) giveManagedBean("MEmps");
            if (s != null) {
                int idx = s.getListEmployes().indexOf(new YvsGrhEmployes(mutualiste.getEmploye().getId()));
                if (idx > -1) {
                    mutualiste.setEmploye(UtilMut.buildBeanEmploye(s.getListEmployes().get(idx)));
                }
            }
        }
        m.setEmploye(mutualiste.getEmploye());
        m.setPosteEmploye(mutualiste.getPosteEmploye());
        mutualiste.setMutuelle(UtilMut.buildBeanMutuelle(currentMutuel));
        m.setMutuelle(mutualiste.getMutuelle());
        m.setActif(mutualiste.isActif());
        m.setMontantEpargne(mutualiste.getMontantEpargne());
        m.setAssistance(mutualiste.isAssistance());
        return m;
    }

    @Override
    public boolean controleFiche(Mutualiste bean) {
        if (bean.getEmploye() == null || bean.getEmploye().getId() == 0) {
            getErrorMessage("Vous devez specifier l'employé");
            return false;
        }
        if (bean.getMutuelle() == null || bean.getMutuelle().getId() == 0) {
            getErrorMessage("Vous devez indiquer la mutuelle");
            return false;
        }
        return true;
    }

    @Override
    public boolean saveNew() {
        if (input_reset != null && input_reset.equals("reset")) {
            setUpdateMutualiste(false);
            input_reset = "";
        }
        String action = isUpdateMutualiste() ? "Modification" : "Insertion";
        try {
            Mutualiste bean = recopieView();
            if (controleFiche(bean)) {
                entityMutualiste = buildMutualiste(bean);
                if (!isUpdateMutualiste()) {
                    entityMutualiste.setId(null);
                    entityMutualiste = (YvsMutMutualiste) dao.save1(entityMutualiste);
                    bean.setId(entityMutualiste.getId());
                    mutualiste.setId(entityMutualiste.getId());
                    mutualistes.add(entityMutualiste);
                } else {
                    dao.update(entityMutualiste);
                    int idx = mutualistes.indexOf(entityMutualiste);
                    if (idx >= 0) {
                        mutualistes.set(idx, entityMutualiste);
                    } else {
                        mutualistes.add(0, entityMutualiste);
                    }
                }
                if (mutualiste.getPosteEmploye().getPoste().getId() > 0) {
                    saveNewPosteMutualiste();
                } else {
                    String query = "delete from yvs_mut_poste_employe where mutualiste = ? and poste = ?";
                    dao.requeteLibre(query, new Options[]{new Options(mutualiste.getId(), 1), new Options(mutualiste.getPosteEmploye().getId(), 2)});
                    mutualiste.setPosteEmploye(null);
                }
                if (mutualiste.isAssistance()) {
                    //Crée un compte assurance pour ce mutualiste
                    savedCompteMutualiste(entityMutualiste);
                }
                succes();
                setUpdateMutualiste(true);
                update("data_mutualiste");
                actionOpenOrResetAfter(this);
            }
        } catch (Exception ex) {
            getException("Error " + action + " : " + ex.getMessage(), ex);
            getErrorMessage(action + " Impossible !");
        }
        return true;
    }

    private void savedCompteMutualiste(YvsMutMutualiste mutualiste) {
        YvsMutCompte cpt = (YvsMutCompte) dao.loadOneByNameQueries("YvsMutCompte.findByMutualisteType", new String[]{"mutualiste", "type"}, new Object[]{entityMutualiste, Constantes.MUT_TYPE_COMPTE_ASSURANCE});
        YvsMutTypeCompte type = (YvsMutTypeCompte) dao.loadOneByNameQueries("YvsMutTypeCompte.findByType", new String[]{"type", "mutuelle"}, new Object[]{Constantes.MUT_TYPE_COMPTE_ASSURANCE, currentMutuel});
        if (cpt == null && type != null) {
            cpt = new YvsMutCompte();
            cpt.setReference("CA-" + mutualiste.getEmploye().getMatricule());
            cpt.setActif(true);
            cpt.setAuthor(currentUser);
            cpt.setDateSave(new Date());
            cpt.setDateUpdate(new Date());
            cpt.setMutualiste(entityMutualiste);
            cpt.setNew_(true);
            cpt.setTypeCompte(type);
            cpt.setId(null);
            dao.save(cpt);
            mutualiste.getComptes().add(cpt);
            update("data_compte_mutualiste");
        } else {
            if (type == null) {
                getWarningMessage("Le type de compte assurance n'a pas été trouvé !");
            }
        }
    }

    public YvsMutPosteEmploye buildPosteEmploye(PosteEmploye y) {
        YvsMutPosteEmploye p = new YvsMutPosteEmploye();
        if (y != null) {
            p.setActif(true);
            p.setDateOccupation((y.getDateOccupation() != null) ? y.getDateOccupation() : new Date());
            p.setId(y.getId());
            p.setPoste(UtilMut.buildEntityPoste(y.getPoste()));
//            if ((y.getPoste() != null) ? y.getPoste().getId() > 0 : false) {
//                p.setPoste(new YvsMutPoste(y.getPoste().getId()));
//            }
            p.setMutualiste(entityMutualiste);
            p.setAuthor(currentUser);
            p.setDateSave(new Date());
            p.setDateUpdate(new Date());
        }
        return p;
    }

    public void saveNewPosteMutualiste() {
        YvsMutPosteEmploye entity = buildPosteEmploye(mutualiste.getPosteEmploye());
        if (entity.getPoste() != null ? entity.getPoste().getId() > 0 : false) {
            String rq = "UPDATE yvs_mut_poste_employe SET actif= false WHERE mutualiste=?";
            Options[] param = new Options[]{new Options(mutualiste.getId(), 1)};
            dao.requeteLibre(rq, param);
            YvsMutPosteEmploye e = (YvsMutPosteEmploye) dao.loadOneByNameQueries("YvsMutPosteEmploye.findByMutualistePoste", new String[]{"mutualiste", "poste"}, new Object[]{entity.getMutualiste(), entity.getPoste()});
            if (e != null ? e.getId() > 0 : false) {
                e.setAuthor(currentUser);
                e.setDateUpdate(new Date());
                e.setActif(true);
                dao.update(e);
            } else {
                entity.setId(null);
                entity = (YvsMutPosteEmploye) dao.save1(entity);
            }
            int idx = mutualistes.indexOf(entity.getMutualiste());
            if (idx > -1) {
//                mutualistes.get(idx).setPosteEmploye(entity);
                mutualistes.get(idx).getPostes().add(entity);
                update("data_mutualiste");
            }
        }
    }

    @Override
    public void resetFiche() {
        resetFiche(mutualiste);
        mutualiste.setMutuelle(new Mutuelle());
        mutualiste.setEmploye(new Employe());
        mutualiste.setPosteEmploye(new PosteEmploye());
        mutualiste.setMontantEpargne(currentMutuel != null ? currentMutuel.getMontantEpargne() : 0);
        mutualiste.setComptes(new ArrayList<YvsMutCompte>());
        avalises.clear();
        setUpdateMutualiste(false);
        input_reset = "";
        tabIds = "";
        resetFicheCompte();
        update("blog_form_mutualiste");
    }

    @Override
    public void deleteBean() {
        try {
            System.err.println("tabIds = " + tabIds);
            if ((tabIds != null) ? tabIds.length() > 0 : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsMutMutualiste> list = new ArrayList<>();
                YvsMutMutualiste bean;
                for (Long ids : l) {
                    bean = mutualistes.get(ids.intValue());
                    bean.setDateUpdate(new Date());
                    bean.setAuthor(currentUser);
                    list.add(bean);
                    dao.delete(bean);
                }
                mutualistes.removeAll(list);
                resetFiche();
                succes();
                tabIds = "";
                update("data_mutualiste");

            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Lymytz Error>> ", ex);
        }
    }

    public void openTodeleteBean(YvsMutMutualiste y) {
        if (y != null ? y.getId() > 0 : false) {
            int index = mutualistes.indexOf(y);
            if (index > 1) {
                tabIds = "" + index;
            }

        }
    }

    public void deleteBean(YvsMutMutualiste y) {
        try {
            if (y != null) {
                dao.delete(y);
                mutualistes.remove(y);
                resetFiche();
                succes();
                update("data_mutualiste");
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
            setUpdateMutualiste((ids != null) ? ids.length > 0 : false);
            if (isUpdateMutualiste()) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                YvsMutMutualiste bean = mutualistes.get(mutualistes.indexOf(new YvsMutMutualiste(id)));
                populateView(UtilMut.buildBeanMutualiste(bean));
                tabIds = "";
                input_reset = "";
                update("blog_form_mutualiste");
            }
        }
    }

    @Override
    public void onSelectObject(YvsMutMutualiste y) {
        entityMutualiste = y;
        populateView(UtilMut.buildBeanMutualiste(entityMutualiste));
        resetFicheCompte();
        update("blog_form_mutualiste");
    }

    public void onSelectSituationCompte(YvsMutMutualiste y) {
        ManagedEpargne w = (ManagedEpargne) giveManagedBean(ManagedEpargne.class);
        if (w != null) {
            w.onSelectMutualiste(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Situation Compte", "modMutuelle", "smenSituationCompte", true);
            }
        }
    }

    public void loadOnViewMutualiste(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutMutualiste y = (YvsMutMutualiste) ev.getObject();
            onSelectObject((y));
            tabIds = "" + mutualistes.indexOf(y);
        }
    }

    @Override
    public void populateView(Mutualiste bean) {
        cloneObject(mutualiste, bean);
//        if (bean.getPosteEmploye() != null) {
//            setPoste(bean.getPosteEmploye().getPoste());
//        }
        loadAllAvalise(bean);
        setUpdateMutualiste(true);
    }

    public void activeMutualiste(YvsMutMutualiste bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            mutualistes.get(mutualistes.indexOf(bean)).setActif(bean.getActif());
            String rq = "UPDATE yvs_mut_mutualiste SET actif=" + bean.getActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
        }
    }

    public void assistanceMutualiste(Mutualiste bean) {
        if (bean != null) {
            if (bean.isAssistance() && parametre.isSouscriptionGeneral()) {
                getErrorMessage("Vous ne pouvez pas annuler sa souscription.", "Car la mutuelle impose une assistance générale");
            } else {
                bean.setAssistance(!bean.isAssistance());
                mutualistes.get(mutualistes.indexOf(bean)).setAssistance(bean.isAssistance());
                String rq = "UPDATE yvs_mut_mutualiste SET assistance=" + bean.isActif() + " WHERE id=?";
                Options[] param = new Options[]{new Options(bean.getId(), 1)};
                dao.requeteLibre(rq, param);
            }
        }
    }

    public void changeActif() {
        if ((tabIds != null) ? tabIds.length() > 0 : false) {
            String[] ids = tabIds.split("-");
            for (String i : ids) {
                int id = Integer.valueOf(i);
                activeMutualiste(mutualistes.get(id));
            }
            update("data_mutualiste");
        }
    }

    public void activeMutualisteAssistance(int idx, YvsMutMutualiste bean) {
        if (bean != null) {
            bean.setAssistance(!bean.getAssistance());
            dao.update(bean);
            mutualistes.set(idx, bean);
        }
    }

    public void changeActifAssistance() {
        if ((tabIds != null) ? tabIds.length() > 0 : false) {
            String[] ids = tabIds.split("-");
            for (String i : ids) {
                int id = Integer.valueOf(i);
                activeMutualisteAssistance(id, mutualistes.get(id));
            }
            update("data_mutualiste");
        }
    }

    public void changeAssistance() {
        if (!mutualiste.isAssistance() && parametre.isSouscriptionGeneral()) {
            getErrorMessage("Vous ne pouvez pas annuler sa souscription."
                    + "Car la mutuelle est paramètrée pour une souscription générale");
            mutualiste.setAssistance(true);
        }
    }

    public void loadOnViewEmploye(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsGrhEmployes bean = (YvsGrhEmployes) ev.getObject();
            chooseEmploye(UtilMut.buildBeanEmploye(bean));
        }
    }

    public void loadOneEmploye(YvsGrhEmployes y) {
        chooseEmploye(UtilMut.buildBeanEmploye(y));
    }

    public void chooseEmploye(Employe e) {
        if (e != null) {
            resetFiche();
            mutualiste.setEmploye(e);
            setUpdateMutualiste(false);
            for (YvsMutMutualiste m : mutualistes) {
                Mutualiste m_ = UtilMut.buildBeanMutualiste(m);
                if (m_.getEmploye().equals(e)) {
                    loadAllAvalise(m_);
                    cloneObject(mutualiste, m_);
                    ManagedPoste s = (ManagedPoste) giveManagedBean(ManagedPoste.class);
                    if (s != null) {
                        cloneObject(s.getPoste(), m_.getPosteEmploye().getPoste());
                    }
                    entityMutualiste = buildMutualiste(m_);
                    getInfoMessage("Cet employé est deja mutualiste! Modifier?");
                    setUpdateMutualiste(true);
                    break;
                }
            }
        }
        update("txt_employe_mutuelle");
        update("txt_montant_total_epargne");
        update("data_compte");
        update("date_adhesion_mutualiste");
        update("poste_employe_mutualiste");
        update("data_avalise");
        update("photos_mut");
    }

    public void findOneEmploye(String matricule) {
        if (matricule != null) {
            ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
            if (service != null) {
                service.findEmploye(matricule);
                if (service.getListEmployes().size() == 1) {
                    loadOneEmploye(service.getListEmployes().get(0));
                    mutualiste.getEmploye().setError(false);
                } else if (!service.getListEmployes().isEmpty()) {
                    openDialog("dlgEmployeMutuelle");
                    update("data_employe_mutuelle");
                    mutualiste.getEmploye().setError(false);
                } else {
                    mutualiste.getEmploye().setError(true);
                }
            }
        }
    }

    public double getMontantCredit(Mutualiste bean) {
        double montant = 0;
        List<YvsMutCredit> l = dao.loadNameQueries("YvsMutCredit.findByMutualisteEtat", new String[]{"mutualiste", "etat"}, new Object[]{new YvsMutMutualiste(bean.getId()), "Accordé"});
        if (l != null) {
            for (YvsMutCredit c : l) {
                montant += (c.getMontantReste());
            }
        }
        return montant;
    }

    public YvsMutCompte buildCompte(Compte y) {
        YvsMutCompte c = new YvsMutCompte();
        if (y != null) {
            c.setId(y.getId());
            c.setReference(y.getReference());
            c.setSolde(y.getSolde());
            if ((y.getType() != null) ? y.getType().getId() != 0 : false) {
                c.setTypeCompte(new YvsMutTypeCompte(y.getType().getId(), y.getType().getLibelle()));
            }
            c.setMutualiste(entityMutualiste);
            c.setAuthor(currentUser);
            c.setInteret(y.isInteret());
            c.setSalaire(y.isSalaire());
            c.setPrime(y.isPrimes());
            c.setActif(y.isActif());
            c.setDateSave(y.getDateSave());
        }
        return c;
    }

    public YvsMutCompte buildCompte_(Compte y) {
        YvsMutCompte c = new YvsMutCompte();
        if (y != null) {
            c.setId(y.getId());
            c.setReference(y.getReference());
            c.setSolde(y.getSolde());
            if ((y.getType() != null) ? y.getType().getId() != 0 : false) {
                c.setTypeCompte(new YvsMutTypeCompte(y.getType().getId()));
            }
            if ((y.getMutualiste() != null) ? y.getMutualiste().getId() > 0 : false) {
                c.setMutualiste(new YvsMutMutualiste(y.getMutualiste().getId()));
            }
            c.setDateSave(new Date());
            c.setDateUpdate(new Date());
            c.setActif(true);
            c.setAuthor(currentUser);
        }
        return c;
    }

    public Compte recopieViewCompte() {
        Compte c = new Compte();
        c.setId(compte.getId());
        c.setReference(compte.getReference());
        c.setSolde(compte.getSolde());
        c.setInteret(compte.isInteret());
        c.setSalaire(compte.isSalaire());
        c.setPrimes(compte.isPrimes());
        c.setDateSave(compte.getDateSave());
        ManagedTypeCompte s = (ManagedTypeCompte) giveManagedBean(ManagedTypeCompte.class);
        if (s != null) {
            int idx = s.getTypes().indexOf(new YvsMutTypeCompte(compte.getType().getId()));
            if (idx > 0) {
                compte.setType(UtilMut.buildBeanTypeCompte(s.getTypes().get(idx)));
            }
        }
        c.setType(compte.getType());
        c.setActif(compte.isActif());
        c.setNew_(true);
        return c;
    }

    public Compte defaultCompte(TypeCompte type, YvsMutMutualiste mut) {
        Compte c = new Compte();
        c.setId(0);
        if ((type != null) ? !type.getLibelle().isEmpty() : false) {
            c.setReference(type.getLibelle().substring(0, 1).toUpperCase() + "-" + mut.getEmploye().getMatricule() + "-01");
        } else {
            c.setReference("-" + mut.getEmploye().getNom() + "-01");
        }
        c.setSolde(0);
        c.setType(type);
        c.setNew_(true);
        c.setMutualiste(UtilMut.buildSimpleBeanMutualiste(mut));
        return c;
    }

    public boolean controleFicheCompte(Compte bean) {
        if ((bean.getType() != null) ? bean.getType().getId() == 0 : true) {
            getErrorMessage("Vous devez indiquer le type!");
            return false;
        }
        return true;
    }

    public void resetFicheCompte() {
        resetFiche(compte);
        compte.setType(new TypeCompte());
        compte.setActif(true);
        tabIds_compte = "";
        input_reset_compte = "";
        update("blog_form_compte_mutualiste");
    }

    public void populateViewCompte(Compte bean) {
        cloneObject(compte, bean);
    }

    public void saveNewCompte() {
        if (isUpdateMutualiste()) {
            if (input_reset_compte != null && input_reset_compte.equals("reset")) {
                input_reset_compte = "";
            }
            String action = compte.getId() > 0 ? "Modification" : "Insertion";
            try {
                Compte bean = recopieViewCompte();
                if (controleFicheCompte(bean)) {
                    YvsMutCompte entity = buildCompte(bean);
                    entity.setDateUpdate(new Date());
                    if (compte.getId() < 1) {
                        entity.setId(null);
                        entity = (YvsMutCompte) dao.save1(entity);
                        bean.setId(entity.getId());
                        compte.setId(entity.getId());
                        mutualiste.getComptes().add(entity);
                    } else {
                        dao.update(entity);
                        int idx = mutualiste.getComptes().indexOf(entity);
                        if (idx > -1) {
                            mutualiste.getComptes().set(idx, entity);
                        }
                    }
                    succes();
                    resetFicheCompte();
                    update("blog_form_compte_mutualiste");
                    update("data_compte");
                }
            } catch (Exception ex) {
                getException("Error " + action + " : " + ex.getMessage(), ex);
                getErrorMessage(action + " Impossible !");
            }
        } else {
            getErrorMessage("Vous devez dabord enregsitrer le mutualiste");
        }
    }

    public void deleteBeanCompte() {
        try {
            if ((tabIds_compte != null) ? tabIds_compte.length() > 0 : false) {
                String[] ids = tabIds_compte.split("-");
                if ((ids != null) ? ids.length > 0 : false) {
                    for (String s : ids) {
                        long id = Integer.valueOf(s);
                        YvsMutCompte bean = mutualiste.getComptes().get(mutualiste.getComptes().indexOf(new YvsMutCompte(id)));
                        dao.delete(new YvsMutCompte(bean.getId()));
                        mutualiste.getComptes().remove(bean);
                    }
                    resetFicheCompte();
                    succes();
                    update("data_compte");
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Mutuelle Error...", ex);
        }
    }

    public void deleteBeanCompte(YvsMutCompte y) {
        try {
            if (y != null) {
                dao.delete(y);
                mutualiste.getComptes().remove(y);
                resetFicheCompte();
                succes();
                update("data_compte");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Mutuelle Error...", ex);
        }
    }

    public void toogleActiveCompte(YvsMutCompte y) {
        try {
            if (y != null) {
                y.setActif(!y.getActif());
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                dao.update(y);
            }
        } catch (Exception ex) {
            getErrorMessage("Modification Impossible !");
            getException("Mutuelle Error...", ex);
        }
    }

    public void updateBeanCompte() {
        tabIds_compte = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tabIds");
        if ((tabIds_compte != null) ? tabIds_compte.length() > 0 : false) {
            String[] ids = tabIds_compte.split("-");
            if (compte.getId() > 0) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                YvsMutCompte bean = mutualiste.getComptes().get(mutualiste.getComptes().indexOf(new YvsMutCompte(id)));
                populateViewCompte(UtilMut.buildBeanCompte(bean));
            } else {
                resetFicheCompte();
            }
        } else {
            resetFicheCompte();
        }
        update("blog_form_compte_mutualiste");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutCompte bean = (YvsMutCompte) ev.getObject();
            populateViewCompte(UtilMut.buildBeanCompte(bean));
            update("blog_form_compte_mutualiste");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFicheCompte();
        update("blog_form_compte_mutualiste");
    }

    public void initialiserCompte() {
        try {
            if ((tabIds != null) ? tabIds.length() > 0 : false) {
                String[] ids = tabIds.split("-");
                YvsMutMutualiste bean;
                String[] ch;
                Object[] v;
                YvsMutTypeCompte typeC;
                for (String i : ids) {
                    int id = Integer.valueOf(i);
                    bean = mutualistes.get(id);
                    ch = new String[]{"id"};
                    for (Long idTc : choixComptes) {
                        v = new Object[]{idTc};
                        typeC = (YvsMutTypeCompte) dao.loadOneByNameQueries("YvsMutTypeCompte.findById", ch, v); //récupère un type de compte dans la mutuelle
                        createCompteInit(typeC, bean);
                    }
                    mutualistes.set(mutualistes.indexOf(bean), bean);
                }
                succes();
                choixComptes = new Long[]{};
                update("data_mutualiste");
                update("type_type_initialisation");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Initialisation impossible !");
            getException("Lymytz Error>>> ", ex);
        }
    }

    private void createCompteInit(YvsMutTypeCompte s, YvsMutMutualiste bean) {
        TypeCompte t;
        if ((s != null) ? s.getId() > 0 : false) {
            t = UtilMut.buildBeanTypeCompte(s);
            List<YvsMutCompte> l = bean.getComptes();
            boolean trouv = false;
            for (YvsMutCompte c : l) {//évite de dupliquer les comptes de même type chez un membre
                if (c.getTypeCompte().getId().equals(t.getId())) {
                    trouv = true;
                    break;
                }
            }
            if (!trouv) {
                Compte cpt = defaultCompte(t, bean);
                YvsMutCompte en = buildCompte_(cpt);
                en.setId(null);
                en = (YvsMutCompte) dao.save1(en);
                cpt.setId(en.getId());
                bean.getComptes().add(en);
            }
        }
    }

    public void chooseTypeCompte() {
        if (compte.getType() != null ? compte.getType().getId() > 0 : false) {
            ManagedTypeCompte s = (ManagedTypeCompte) giveManagedBean(ManagedTypeCompte.class);
            if (s != null) {
                int idx = s.getTypes().indexOf(new YvsMutTypeCompte(compte.getType().getId()));
                if (idx > 0) {
                    compte.setType(UtilMut.buildBeanTypeCompte(s.getTypes().get(idx)));
                }
            }
        } else {
            if (compte.getType() != null ? compte.getType().getId() < 0 : true) {
                openDialog("dlgAddTypeCompte");
            }
        }
    }

    public void chooseTypePoste(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            ManagedPoste s = (ManagedPoste) giveManagedBean(ManagedPoste.class);
            if (s != null && ((long) ev.getNewValue()) > 0) {
                int idx = s.getPostes().indexOf(new YvsMutPoste((long) ev.getNewValue()));
                if (idx > 0) {
                    mutualiste.getPosteEmploye().setPoste(UtilMut.buildBeanPoste(s.getPostes().get(idx)));
                }

            } else {
                if ((long) ev.getNewValue() < 0) {
                    openDialog("dlgPosteMutualiste");
                }
            }
        }
    }

    public void addParamActif() {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND");
        paginator.addParam(p);
        loadAllMutualiste(true, true);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.employe", "employe", null, "LIKE", "AND");
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "employe", numSearch.toUpperCase(), "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.employe.matricule)", "matricule", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.employe.nom)", "nom", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.employe.prenom)", "prenom", numSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllMutualiste(true, true);
    }

    public Mutualiste searchMutualiste(boolean open) {
        return searchMutualiste(mutualiste.getMatricule(), open);
    }

    public Mutualiste searchMutualiste(String num, boolean open) {
        Mutualiste a = new Mutualiste();
        a.setMatricule(num);
        a.setError(true);
        numSearch = num;
        addParamReference();
        a = chechMutualisteResult(open);
        if (a != null ? a.getId() < 1 : true) {
            a.setMatricule(num);
            a.setError(true);
        }
        return a;
    }

    private Mutualiste chechMutualisteResult(boolean open) {
        Mutualiste a = new Mutualiste();
        if (mutualistes != null ? !mutualistes.isEmpty() : false) {
            if (mutualistes.size() > 1) {
                if (open) {
                    openDialog("dlgListMutualistes");
                }
                a.setList(true);
            } else {
                YvsMutMutualiste c = mutualistes.get(0);
                c.setCouvertureAvalise_(dao);
                a = UtilMut.buildBeanMutualiste(c);
            }
            a.setError(false);
        }
        return a;
    }

    public void initMutualistes(Mutualiste a) {
        if (a == null) {
            a = new Mutualiste();
        }
        paginator.addParam(new ParametreRequete("y.employe.matricule", "employe", null));
        loadAllMutualiste(true, true);
        a.setList(true);
    }

    public void print() {
        try {
            Map<String, Object> param = new HashMap<>();
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
            param.put("AGENCE", currentAgence.getId().intValue());
            param.put("NAME_AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
            executeReport("mutualiste", param);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedMutualiste.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
