/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaJournauxPeriode;
import yvs.entity.mutuelle.YvsMutPeriodeExercice;
import yvs.entity.param.YvsAgences;
import yvs.mutuelle.ManagedExercice;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.agence.ManagedAgence;
import yvs.parametrage.agence.UtilAgence;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;

/**
 *
 * @author hp Elite 8300
 */
@SessionScoped
@ManagedBean
public class ManagedJournaux extends Managed<Journaux, YvsComptaJournaux> implements Serializable {

    @ManagedProperty(value = "#{journaux}")
    private Journaux journal;
    private boolean initForm = true; //permet de d'empêcher la navigation de la requête lors de l'actualisation de la page
    private List<YvsComptaJournaux> journaux;
    private YvsComptaJournaux selectJournal = new YvsComptaJournaux();

    private long exercice;
    private List<YvsComptaJournauxPeriode> periodes;

    private String codeSearch;
    private Long idAgenceSearch;
    private Boolean actifSearch;
    /*attribut pour la recherche*/
    private String tabIds;
    private String fusionneTo;
    private List<String> fusionnesBy;

    public ManagedJournaux() {
        journaux = new ArrayList<>();
        periodes = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getFusionneTo() {
        return fusionneTo;
    }

    public void setFusionneTo(String fusionneTo) {
        this.fusionneTo = fusionneTo;
    }

    public List<String> getFusionnesBy() {
        return fusionnesBy;
    }

    public void setFusionnesBy(List<String> fusionnesBy) {
        this.fusionnesBy = fusionnesBy;
    }

    public long getExercice() {
        return exercice;
    }

    public void setExercice(long exercice) {
        this.exercice = exercice;
    }

    public List<YvsComptaJournauxPeriode> getPeriodes() {
        return periodes;
    }

    public void setPeriodes(List<YvsComptaJournauxPeriode> periodes) {
        this.periodes = periodes;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public YvsComptaJournaux getSelectJournal() {
        return selectJournal;
    }

    public void setSelectJournal(YvsComptaJournaux selectJournal) {
        this.selectJournal = selectJournal;
    }

    public Journaux getJournal() {
        return journal;
    }

    public void setJournal(Journaux journal) {
        this.journal = journal;
    }

    public boolean isInitForm() {
        return initForm;
    }

    public void setInitForm(boolean initForm) {
        this.initForm = initForm;
    }

    public List<YvsComptaJournaux> getJournaux() {
        return journaux;
    }

    public void setJournaux(List<YvsComptaJournaux> journaux) {
        this.journaux = journaux;
    }

    public String getCodeSearch() {
        return codeSearch;
    }

    public void setCodeSearch(String codeSearch) {
        this.codeSearch = codeSearch;
    }

    public Long getIdAgenceSearch() {
        return idAgenceSearch;
    }

    public void setIdAgenceSearch(Long idAgenceSearch) {
        this.idAgenceSearch = idAgenceSearch;
    }

    @Override
    public boolean controleFiche(Journaux bean) {
        if (bean.getCodejournal() == null) {
            getErrorMessage("Vous devez indiquer le code journal !");
            return false;
        }
        if (bean.getIntitule() == null) {
            getErrorMessage("Vous devez indiquer l'intitulé du journal !");
            return false;
        }
        if (bean.isDefaultFor()) {
            YvsAgences agence = bean.getAgence() != null ? bean.getAgence().getId() > 0 ? new YvsAgences(bean.getAgence().getId()) : currentAgence : currentAgence;
            champ = new String[]{"agence", "type", "default"};
            val = new Object[]{agence, bean.getTypeJournal(), true};
            YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
            if (jrn != null ? (jrn.getId() != null ? !jrn.getId().equals(bean.getId()) : false) : false) {
                getErrorMessage("Il existe deja un journal par défaut pour ce type");
                return false;
            }
        }
        //le code doit être unique
        champ = new String[]{"societe", "code"};
        val = new Object[]{currentAgence.getSociete(), bean.getCodejournal()};
        Long id = (Long) dao.loadObjectByNameQueries("YvsComptaJournaux.findIdByCodejournal", champ, val);
        if (id != null ? id > 0 : false) {
            if (bean.getId() > 0) { // si on est en modification
                if (bean.getId() != id) {
                    getErrorMessage("Un journal a déjà été trouvé avec le même code !");
                    return false;
                }
            } else {
                getErrorMessage("Un journal a déjà été trouvé avec le même code !");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean saveNew() {
        if (controleFiche(journal)) {
            YvsComptaJournaux j = UtilCompta.buildBeanJournaux(journal);
            if (journal.getAgence().getId() > 0) {
                ManagedAgence w = (ManagedAgence) giveManagedBean(ManagedAgence.class);
                if (w != null) {
                    int idx = w.getAgences().indexOf(j.getAgence());
                    if (idx >= 0) {
                        j.setAgence(w.getAgences().get(idx));
                    }
                }
            }
            if (j.getAgence() != null ? j.getAgence().getId() < 1 : true) {
                j.setAgence(currentAgence);
            }
            if (journal.getCodeAcces() != null ? journal.getCodeAcces().trim().length() > 0 : false) {
                j.setCodeAcces(returnCodeAcces(journal.getCodeAcces()));
            } else {
                j.setCodeAcces(null);
            }
            j.setAuthor(currentUser);
            j.setNew_(true);
            if (journal.getId() <= 0) {
                j.setId(null);
                j.setDateSave(new Date());
                j = (YvsComptaJournaux) dao.save1(j);
                journal.setId(j.getId());
                journaux.add(j);
            } else {
                j.setId(journal.getId());
                dao.update(j);
                int idx = journaux.indexOf(j);
                if (idx > -1) {
                    journaux.set(idx, j);
                }
            }
            actionOpenOrResetAfter(this);
            succes();
            update("table_journal");
            update("chmp_journal_Caisse");
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            System.err.println("tabIds = " + tabIds);
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsComptaJournaux> list = new ArrayList<>();
                YvsComptaJournaux bean;
                for (Long ids : l) {
                    bean = journaux.get(ids.intValue());
                    list.add(bean);
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    dao.delete(bean);
                    if (bean.getId() == journal.getId()) {
                        resetFiche();
                    }
                }
                journaux.removeAll(list);
                succes();
                tabIds = "";
                update("table_journal");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Journaux recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(Journaux bean) {
        cloneObject(journal, bean);
    }

    @Override
    public void onSelectObject(YvsComptaJournaux y) {
        selectJournal = y;
        populateView(UtilCompta.buildBeanJournaux(y));
        update("form_edit_journaux");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        YvsComptaJournaux j = (YvsComptaJournaux) ev.getObject();
        onSelectObject(j);
        execute("collapseForm('journaux')");
        tabIds = journaux.indexOf(j) + "";
        selectJournal = j;
        execute("oncollapsesForm('journaux')");
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadAll() {
        loadAllJournaux(true);
        if (journal.getId() <= 0) {
            journal.setAgence(UtilAgence.buildBeanAgence(currentAgence));
        }
        if (exercice < 1 ? (currentExo != null ? currentExo.getId() > 0 : false) : false) {
            exercice = currentExo.getId();
        }
    }

    public void pagineResult_(boolean avancer) {
        initForm = false;
        loadAllJournaux(avancer);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        initForm = true;
        loadAllJournaux(true);
    }

    public void changePage(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            imax = (long) ev.getNewValue();
            initForm = true;
            loadAllJournaux(true);
        }
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComptaJournaux> re = paginator.parcoursDynamicData("YvsComptaJournaux", "y", "y.codeJournal", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void fusionner(boolean fusionne) {
        try {
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty() ? ids.size() > 1 : false) {
                long newValue = journaux.get(ids.get(0)).getId();
                if (!fusionne) {
                    String oldValue = "0";
                    for (int i : ids) {
                        if (journaux.get(i).getId() != newValue) {
                            oldValue += "," + journaux.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_compta_journaux", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                journaux.remove(new YvsComptaJournaux(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = journaux.get(idx).getIntitule();
                    } else {
                        YvsComptaJournaux c = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                            fusionneTo = c.getIntitule();
                        }
                    }
                    YvsComptaJournaux c;
                    for (int i : ids) {
                        long oldValue = journaux.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(journaux.get(i).getIntitule());
                            }
                        } else {
                            c = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                                fusionnesBy.add(c.getIntitule());
                            }
                        }
                    }
                    update("blog_fusionner_journaux");
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 journaux");
            }
        } catch (NumberFormatException ex) {
        }
    }

    @Override
    public void resetFiche() {
        resetFiche(journal);
        journal.setAgence(new Agence());
        journal.setDefaultFor(false);
        selectJournal = new YvsComptaJournaux();
        tabIds = "";
        update("form_edit_journaux");
    }

    public void loadAllJournaux() {
        PaginatorResult paginator = new PaginatorResult<YvsComptaJournaux>();
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        if (!autoriser("journaux_view_all")) {
            if (autoriser("journaux_view_only_agence")) {
                paginator.addParam(new ParametreRequete("y.agence", "agence", currentAgence, "=", "AND"));
            } else {
                String query = "SELECT DISTINCT y.id FROM yvs_compta_journaux y INNER JOIN yvs_base_users_acces a On y.code_acces = a.code WHERE a.users = ?";
                List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(currentUser.getUsers().getId(), 1)});
                if (ids != null ? ids.isEmpty() : true) {
                    ids = new ArrayList<Long>() {
                        {
                            add(-1L);
                        }
                    };
                }
                paginator.addParam(new ParametreRequete("y.id", "ids", ids, "IN", "AND"));
            }
        }
        journaux = paginator.executeDynamicQuery("YvsComptaJournaux", "y.agence.id, y.codeJournal", true, true, 0, dao);
    }

    public void loadAllJournauxBySociete() {
        PaginatorResult paginator = new PaginatorResult<YvsComptaJournaux>();
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        if (!autoriser("journaux_view_all")) {
            if (autoriser("journaux_view_only_agence")) {
                paginator.addParam(new ParametreRequete("y.agence", "agence", currentAgence, "=", "AND"));
            } else {
                String query = "SELECT DISTINCT y.id FROM yvs_compta_journaux y INNER JOIN yvs_base_users_acces a On y.code_acces = a.code WHERE a.users = ?";
                List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(currentUser.getUsers().getId(), 1)});
                if (ids != null ? ids.isEmpty() : true) {
                    ids = new ArrayList<Long>() {
                        {
                            add(-1L);
                        }
                    };
                }
                paginator.addParam(new ParametreRequete("y.id", "ids", ids, "IN", "AND"));
            }
        }
        journaux = paginator.executeDynamicQuery("YvsComptaJournaux", "y.agence.id, y.codeJournal", true, true, 0, dao);
    }

    public void loadAllActif() {
        PaginatorResult paginator = new PaginatorResult<YvsComptaJournaux>();
        paginator.addParam(new ParametreRequete("y.actif", "actif", true, "=", "AND"));
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        if (!autoriser("journaux_view_all")) {
            if (autoriser("journaux_view_only_agence")) {
                paginator.addParam(new ParametreRequete("y.agence", "agence", currentAgence, "=", "AND"));
            } else {
                String query = "SELECT DISTINCT y.id FROM yvs_compta_journaux y INNER JOIN yvs_base_users_acces a On y.code_acces = a.code WHERE a.users = ?";
                List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(currentUser.getUsers().getId(), 1)});
                if (ids != null ? ids.isEmpty() : true) {
                    ids = new ArrayList<Long>() {
                        {
                            add(-1L);
                        }
                    };
                }
                paginator.addParam(new ParametreRequete("y.id", "ids", ids, "IN", "AND"));
            }
        }
        journaux = paginator.executeDynamicQuery("YvsComptaJournaux", "y.agence.id, y.codeJournal", true, true, 0, dao);
    }

    public void loadAllActifByAgence(long agence) {
        PaginatorResult paginator = new PaginatorResult<YvsComptaJournaux>();
        paginator.addParam(new ParametreRequete("y.actif", "actif", true, "=", "AND"));
        paginator.addParam(new ParametreRequete("y.agence", "agence", new YvsAgences(agence), "=", "AND"));
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        if (!autoriser("journaux_view_all")) {
            if (autoriser("journaux_view_only_agence")) {
                paginator.addParam(new ParametreRequete("y.agence", "agence", currentAgence, "=", "AND"));
            } else {
                String query = "SELECT DISTINCT y.id FROM yvs_compta_journaux y INNER JOIN yvs_base_users_acces a On y.code_acces = a.code WHERE a.users = ?";
                List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(currentUser.getUsers().getId(), 1)});
                if (ids != null ? ids.isEmpty() : true) {
                    ids = new ArrayList<Long>() {
                        {
                            add(-1L);
                        }
                    };
                }
                paginator.addParam(new ParametreRequete("y.id", "ids", ids, "IN", "AND"));
            }
        }
        journaux = paginator.executeDynamicQuery("YvsComptaJournaux", "y.agence.id, y.codeJournal", true, true, 0, dao);
    }

    private void loadAllJournaux(boolean avancer) {
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        if (!autoriser("journaux_view_all")) {
            if (autoriser("journaux_view_only_agence")) {
                paginator.addParam(new ParametreRequete("y.agence", "agence", currentAgence, "=", "AND"));
            } else {
                String query = "SELECT DISTINCT y.id FROM yvs_compta_journaux y INNER JOIN yvs_base_users_acces a On y.code_acces = a.code WHERE a.users = ?";
                List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(currentUser.getUsers().getId(), 1)});
                if (ids != null ? ids.isEmpty() : true) {
                    ids = new ArrayList<Long>() {
                        {
                            add(-1L);
                        }
                    };
                }
                paginator.addParam(new ParametreRequete("y.id", "ids", ids, "IN", "AND"));
            }
        }
        journaux = paginator.executeDynamicQuery("YvsComptaJournaux", "y.agence.id, y.codeJournal", avancer, initForm, (int) imax, dao);
    }

    public void loadAllJournauxActif(Boolean actif) {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", actif, "=", "AND");
        paginator.addParam(p);
        initForm = true;
        loadAllJournaux(true);
    }

    public void loadAllJournauxActifTresorerie(Boolean actif) {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", actif, "=", "AND");
        paginator.addParam(p);
        p = new ParametreRequete("y.typeJournal", "typeJournal", Constantes.TRESORERIE, "=", "AND");
        paginator.addParam(p);
        initForm = true;
        loadAllJournaux(true);
    }

    public void toogleActive(YvsComptaJournaux bc) {
        if (bc != null) {
            bc.setActif(!bc.getActif());
            bc.setAuthor(currentUser);
            dao.update(bc);
        }
    }

    public void checkDefaut() {
        if (journal.isDefaultFor()) {
            champ = new String[]{"agence", "type", "default"};
            val = new Object[]{new YvsAgences(journal.getAgence().getId()), journal.getTypeJournal(), true};
            YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
            if (jrn != null ? (jrn.getId() != null ? !jrn.getId().equals(journal.getId()) : false) : false) {
                getErrorMessage("Il existe deja un journal par défaut pour ce type");
                journal.setDefaultFor(false);
                update("chk_default_journaux");
            }
        }
    }

    public void setDefault(YvsComptaJournaux y) {
        if (!y.getDefaultFor()) {
            champ = new String[]{"agence", "type", "default"};
            val = new Object[]{y.getAgence(), y.getTypeJournal(), true};
            YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
            if (jrn != null ? (jrn.getId() != null ? !jrn.getId().equals(y.getId()) : false) : false) {
                getErrorMessage("Il existe deja un journal par défaut pour ce type");
                return;
            }
        }
        y.setDefaultFor(!y.getDefaultFor());
        String rq = "UPDATE yvs_compta_journaux SET default_for=" + y.getDefaultFor() + " WHERE id=?";
        Options[] param = new Options[]{new Options(y.getId(), 1)};
        dao.requeteLibre(rq, param);
        journaux.set(journaux.indexOf(y), y);
    }

    public void clearParam() {
        paginator.getParams().clear();
        loadAllJournaux(true);
    }

    public void addParamCode(String code) {
        ParametreRequete p0 = new ParametreRequete(null, "code", "XXX", "LIKE", "AND");
        if (code == null ? code.trim().isEmpty() : false) {
            p0.setObjet(null);
        } else {
            p0.getOtherExpression().add(new ParametreRequete("UPPER(y.codeJournal)", "codeJ", "%" + code.trim().toUpperCase() + "%", "LIKE", "OR"));
            p0.getOtherExpression().add(new ParametreRequete("UPPER(y.intitule)", "intitule", "%" + code.trim().toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p0);
        loadAllJournaux(true);
    }

    public void addParamActif(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", null, "=", "AND");
        if (ev != null) {
            if (ev.getNewValue() != null) {
                p.setObjet((Boolean) ev.getNewValue());
            }
        }
        paginator.addParam(p);
        loadAllJournaux(true);
    }

    public void addParamAgence(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.agence", "agence", null, "=", "AND");
        if (ev != null) {
            if (ev.getNewValue() != null) {
                p.setObjet(new YvsAgences((Long) ev.getNewValue()));
            }
        }
        paginator.addParam(p);
        loadAllJournaux(true);
    }

    public void onOpenDialogCloture(YvsComptaJournaux y) {
        try {
            ManagedExercice w = (ManagedExercice) giveManagedBean(ManagedExercice.class);
            if (w != null) {
                if (w.getExercices().isEmpty()) {
                    w.loadAllExrecice();
                    update("select-exercice_periode");
                }
            }
            onBuildPeriode(y);
        } catch (Exception ex) {
            getException("onBuildPeriode", ex);
        }
    }

    public void onBuildPeriode(YvsComptaJournaux y) {
        try {
            periodes.clear();
            selectJournal = y;
            if (y != null ? y.getId() > 0 : false) {
                String query = "SELECT e.id, e.reference, p.id, p.reference_periode, p.date_debut, p.date_fin, p.cloture, y.id, COALESCE(y.cloture, FALSE) FROM yvs_mut_periode_exercice p INNER JOIN yvs_base_exercice e ON p.exercice = e.id LEFT JOIN yvs_compta_journaux_periode y ON (p.id = y.periode AND y.journal = ?) WHERE e.societe = ? AND e.id = ? ORDER BY p.date_debut";
                Options[] params = new Options[]{new Options(y.getId(), 1), new Options(currentAgence.getSociete().getId(), 2), new Options(exercice, 3)};
                List<Object[]> result = dao.loadListBySqlQuery(query, params);
                YvsComptaJournauxPeriode r;
                YvsBaseExercice e;
                YvsMutPeriodeExercice p;
                for (Object[] data : result) {
                    e = new YvsBaseExercice((Long) data[0], (String) data[1]);
                    p = new YvsMutPeriodeExercice((Long) data[2], (String) data[3], new Date(((java.sql.Date) data[4]).getTime()), new Date(((java.sql.Date) data[5]).getTime()), (Boolean) data[6], e);
                    r = new YvsComptaJournauxPeriode(data[7] != null ? (Long) data[7] : YvsComptaJournauxPeriode.ids--, (Boolean) data[8], y, p);
                    periodes.add(r);
                }
            }
            update("data-periode_exercices");
        } catch (Exception ex) {
            getException("onBuildPeriode", ex);
        }
    }

    public void onClotureJournal(YvsComptaJournauxPeriode y) {
        try {
            if (y != null) {
                if (y.getPeriode().getCloture()) {
                    getErrorMessage("Cette période est déjà cloturée");
                    return;
                }
                int index = periodes.indexOf(y);
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                if (y.getId() > 0) {
                    y.setCloture(!y.getCloture());
                    dao.update(y);
                } else {
                    y.setId(null);
                    y.setCloture(true);
                    y = (YvsComptaJournauxPeriode) dao.save1(y);
                }
                periodes.set(index, y);
            }
        } catch (Exception ex) {
            getException("onBuildPeriode", ex);
        }
    }
}
