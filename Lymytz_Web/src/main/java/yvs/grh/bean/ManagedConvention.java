/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.param.YvsGrhCategorieProfessionelle;
import yvs.entity.grh.param.YvsGrhConventionCollective;
import yvs.entity.grh.personnel.YvsGrhConventionEmploye;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedConvention extends Managed<ConventionEmploye, YvsGrhConventionEmploye> implements Serializable {

    @ManagedProperty(value = "#{conventionEmploye}")
    private ConventionEmploye conventionEmploye;
    private List<YvsGrhConventionEmploye> conventions, historiques;
    private YvsGrhConventionEmploye selectConv;

    private List<YvsGrhCategorieProfessionelle> categories;
    private List<YvsGrhConventionCollective> echellons;

    private String tabIds;

    private String numSearch, catSearch, echelSearch;
    private Boolean actifSearch;
    private boolean date;
    private Date dateDebut = new Date(), dateFin = new Date();

    public ManagedConvention() {
        conventions = new ArrayList<>();
        echellons = new ArrayList<>();
        categories = new ArrayList<>();
        historiques = new ArrayList<>();
    }

    public List<YvsGrhConventionEmploye> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(List<YvsGrhConventionEmploye> historiques) {
        this.historiques = historiques;
    }

    public List<YvsGrhCategorieProfessionelle> getCategories() {
        return categories;
    }

    public void setCategories(List<YvsGrhCategorieProfessionelle> categories) {
        this.categories = categories;
    }

    public List<YvsGrhConventionCollective> getEchellons() {
        return echellons;
    }

    public void setEchellons(List<YvsGrhConventionCollective> echellons) {
        this.echellons = echellons;
    }

    public String getCatSearch() {
        return catSearch;
    }

    public void setCatSearch(String catSearch) {
        this.catSearch = catSearch;
    }

    public String getEchelSearch() {
        return echelSearch;
    }

    public void setEchelSearch(String echelSearch) {
        this.echelSearch = echelSearch;
    }

    public boolean isDate() {
        return date;
    }

    public void setDate(boolean date) {
        this.date = date;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public YvsGrhConventionEmploye getSelectConv() {
        return selectConv;
    }

    public void setSelectConv(YvsGrhConventionEmploye selectConv) {
        this.selectConv = selectConv;
    }

    public ConventionEmploye getConventionEmploye() {
        return conventionEmploye;
    }

    public void setConventionEmploye(ConventionEmploye conventionEmploye) {
        this.conventionEmploye = conventionEmploye;
    }

    public List<YvsGrhConventionEmploye> getConventions() {
        return conventions;
    }

    public void setConventions(List<YvsGrhConventionEmploye> conventions) {
        this.conventions = conventions;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    @Override
    public void loadAll() {
        loadAll(true, true);
        loadCategories();
        tabIds = "";
    }

    public void loadAll(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.employe.agence", "agence", currentAgence, "=", "AND"));
        conventions = paginator.executeDynamicQuery("YvsGrhConventionEmploye", "y.dateChange DESC, y.employe.nom, y.employe.prenom", avance, init, (int) imax, dao);
        update("data_convention_emps");
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsGrhConventionEmploye> re = paginator.parcoursDynamicData("YvsGrhConventionEmploye", "y", "y.dateChange DESC, y.employe.nom, y.employe.prenom", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void loadByActif(Boolean actif) {
        actifSearch = actif;
        addParamActif();
    }

    public void loadHistoriques(YvsGrhEmployes y) {
        champ = new String[]{"employe"};
        val = new Object[]{y};
        nameQueri = "YvsConventionEmploye.findByEmps";
        historiques = dao.loadNameQueries(nameQueri, champ, val);
        update("data_historique_convention_emps");
    }

    public void loadCategories() {
        champ = new String[]{"secteur"};
        val = new Object[]{currentAgence.getSecteurActivite()};
        nameQueri = "YvsGrhConventionCollective.findCategorieBySecteur";
        categories = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadEchellons(YvsGrhCategorieProfessionelle y) {
        champ = new String[]{"categorie", "secteur"};
        val = new Object[]{y, currentAgence.getSecteurActivite()};
        nameQueri = "YvsConventionCollective.findByCategorie";
        echellons = dao.loadNameQueries(nameQueri, champ, val);
    }

    @Override
    public ConventionEmploye recopieView() {
        ConventionEmploye r = new ConventionEmploye();
        if (conventionEmploye != null) {
            r.setId(conventionEmploye.getId());
            r.setDateChange(conventionEmploye.getDateChange());
            r.setConvention(conventionEmploye.getConvention());
            r.setEmploye(conventionEmploye.getEmploye());
            r.setActif(conventionEmploye.isActif());
            r.setSupp(conventionEmploye.isSupp());
        }
        return r;
    }

    @Override
    public void populateView(ConventionEmploye bean) {
        cloneObject(conventionEmploye, bean);
    }

    @Override
    public boolean controleFiche(ConventionEmploye bean) {
        if (bean.getEmploye() != null ? bean.getEmploye().getId() < 1 : true) {
            getErrorMessage("Vous devez précisez l'employé");
            return false;
        }
        if (bean.getConvention() != null ? bean.getConvention().getId() < 1 : true) {
            getErrorMessage("Vous devez précisez la convention");
            return false;
        }
        return true;
    }

    @Override
    public void resetFiche() {
        resetFiche(conventionEmploye);
        conventionEmploye.setEmploye(new Employe());
        conventionEmploye.setConvention(new Convention());
        tabIds = "";
        echellons.clear();
        historiques.clear();
        selectConv = new YvsGrhConventionEmploye();
        update("blog_form_convention_emps");
    }

    @Override
    public boolean saveNew() {
        try {
            ConventionEmploye bean = recopieView();
            if (controleFiche(bean)) {
                YvsGrhConventionEmploye y = UtilGrh.buildConventionEmps(bean, currentUser);
                if (bean.getId() > 0) {
                    dao.update(y);
                    int idx = conventions.indexOf(y);
                    if (idx > -1) {
                        conventions.set(idx, y);
                        update("data_convention_emps");
                    }
                    idx = historiques.indexOf(y);
                    if (idx > -1) {
                        historiques.set(idx, y);
                        update("data_historique_convention_emps");
                    }
                } else {
                    y.setId(null);
                    y = (YvsGrhConventionEmploye) dao.save1(y);
                    conventions.add(0, y);
                    historiques.add(0, y);
                    update("data_historique_convention_emps");
                }
                succes();
                actionOpenOrResetAfter(this);
            }
            return true;
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            System.err.println("Error " + ex.getMessage());
            return false;
        }
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsGrhConventionEmploye> list = new ArrayList<>();
                YvsGrhConventionEmploye bean;
                for (Long ids : l) {
                    bean = conventions.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);

                }
                conventions.removeAll(list);
                resetFiche();
                succes();
                update("data_convention_emps");
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            System.err.println("Error " + ex.getMessage());
        }
    }

    public void deleteBean_(YvsGrhConventionEmploye y, boolean open) {
        selectConv = y;
        if (!open) {
            deleteBean_();
        }
    }

    public void deleteBean_() {
        try {
            if (selectConv != null) {
                dao.delete(selectConv);
                conventions.remove(selectConv);
                resetFiche();
                succes();
                update("data_convention_emps");
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            System.err.println("Error " + ex.getMessage());
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void selectOnView(YvsGrhConventionEmploye y) {
        if (y.getConvention() != null ? (y.getConvention().getCategorie() != null ? y.getConvention().getCategorie().getId() > 0 : false) : false) {
            loadEchellons(y.getConvention().getCategorie());
        }
        populateView(UtilGrh.buildBeanConventionEmps(y));
        loadHistoriques(y.getEmploye());
        update("blog_form_convention_emps");
    }

    @Override
    public void onSelectObject(YvsGrhConventionEmploye y) {
        selectOnView(y);
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsGrhConventionEmploye f = (YvsGrhConventionEmploye) ev.getObject();
            selectOnView(f);

            tabIds = conventions.indexOf(f) + "";
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void onNodeSelect(NodeSelectEvent ev) {
        YvsGrhConventionEmploye f = (YvsGrhConventionEmploye) ev.getTreeNode().getData();
        selectOnView(f);
    }

    public void loadOnViewEmploye(SelectEvent ev) {
        if (ev != null) {
            chooseEmploye((YvsGrhEmployes) ev.getObject());
        }
    }

    public void chooseEmploye(YvsGrhEmployes ev) {
        conventionEmploye.setEmploye(UtilGrh.buildBeanPartialEmploye(ev));
        loadHistoriques(ev);
    }

    public void chooseCategorie() {
        if (conventionEmploye.getConvention() != null ? (conventionEmploye.getConvention().getCategorie() != null ? conventionEmploye.getConvention().getCategorie().getId() > 0 : false) : false) {
            loadEchellons(new YvsGrhCategorieProfessionelle(conventionEmploye.getConvention().getCategorie().getId()));
        }
    }

    public void chooseEchellon() {
        if (conventionEmploye.getConvention() != null ? conventionEmploye.getConvention().getId() > 0 : false) {
            int idx = echellons.indexOf(new YvsGrhConventionCollective(conventionEmploye.getConvention().getId()));
            if (idx > -1) {
                YvsGrhConventionCollective y = echellons.get(idx);
                conventionEmploye.setConvention(UtilGrh.buildBeanConventionCollective(y));
            }
        }
    }

    public void findOneEmploye(String matricule) {
        if (matricule != null) {
            ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
            if (service != null) {
                service.findEmploye(matricule);
                if (service.getListEmployes().size() == 1) {
                    chooseEmploye(service.getListEmployes().get(0));
                    conventionEmploye.getEmploye().setError(false);
                } else if (!service.getListEmployes().isEmpty()) {
                    openDialog("dlgEmploye");
                    update("data_employe_convention");
                    conventionEmploye.getEmploye().setError(false);
                } else {
                    conventionEmploye.getEmploye().setError(true);
                }
            }
        }
    }

    public void init(boolean next) {
        loadAll(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAll(true, true);
    }

    public void active() {
        active(selectConv, true);
    }

    public void active(YvsGrhConventionEmploye art, boolean force) {
        if (art != null) {
            selectConv = force ? new YvsGrhConventionEmploye() : art;
            if (!art.getActif()) {
                champ = new String[]{"employe"};
                val = new Object[]{art.getEmploye()};
                nameQueri = "YvsConventionEmploye.findCurrentByEmploye";
                List<YvsGrhConventionEmploye> l = dao.loadNameQueries(nameQueri, champ, val);
                if (l != null ? !l.isEmpty() : false) {
                    for (YvsGrhConventionEmploye c : l) {
                        if (!c.getId().equals(art.getId())) {
                            if (!force) {
                                openDialog("dlgForceActive");
                                return;
                            } else {
                                c.setActif(false);
                                dao.update(c);
                                int idx = conventions.indexOf(c);
                                if (idx > -1) {
                                    conventions.set(idx, c);
                                }
                                idx = historiques.indexOf(c);
                                if (idx > -1) {
                                    historiques.set(idx, c);
                                }
                            }
                        }
                    }
                }
            }
            art.setActif(!art.getActif());
            art.setAuthor(currentUser);
            dao.update(art);
            int idx = conventions.indexOf(art);
            if (idx > -1) {
                conventions.set(idx, art);
            }
            idx = historiques.indexOf(art);
            if (idx > -1) {
                historiques.set(idx, art);
            }
            update("data_convention_emps");
            update("data_historique_convention_emps");
        }
    }

    public void addParamEmploye() {
        ParametreRequete p = new ParametreRequete("y.employe", "employe", null, "LIKE", "AND");
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "employe", numSearch.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.employe.matricule)", "employe", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.employe.nom)", "employe", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.employe.prenom)", "employe", numSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamEchellon() {
        ParametreRequete p = new ParametreRequete("y.convention", "echellon", null, "=", "AND");
        if (echelSearch != null ? echelSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("UPPER(y.convention.echelon.echelon)", "echellon", echelSearch.toUpperCase(), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamCategorie() {
        ParametreRequete p = new ParametreRequete("y.convention", "categorie", null, "=", "AND");
        if (catSearch != null ? catSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("UPPER(y.convention.categorie.categorie)", "categorie", catSearch.toUpperCase(), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateChange", "dates", null, "BETWEEN", "AND");
        if (date) {
            p = new ParametreRequete("y.dateChange", "dates", dateDebut, dateFin, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamActif() {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
        loadAll(true, true);
    }
}
