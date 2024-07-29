/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.commercial.creneau.ManagedTypeCreneau;
import yvs.dao.Options;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.production.base.YvsProdCreneauEquipeProduction;
import yvs.entity.production.base.YvsProdSiteProduction;
import yvs.entity.production.equipe.YvsProdEquipeProduction;
import yvs.entity.users.YvsUsers;
import yvs.grh.UtilGrh;
import yvs.parametrage.agence.ManagedAgence;
import yvs.production.UtilProd;
import yvs.users.ManagedUser;
import yvs.users.Users;
import yvs.users.UtilUsers;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedCreneauEquipe extends Managed<CrenauxHoraireEquipe, YvsProdCreneauEquipeProduction> implements Serializable {

    private CrenauxHoraireEquipe creneau = new CrenauxHoraireEquipe();
    private YvsProdCreneauEquipeProduction entity;
    private List<YvsProdCreneauEquipeProduction> creneaux, historiques;
    private long agence, niveau;
    private Date dateDebut, dateFin;
    private String tabIds;

    private boolean addDate;
    private Boolean actifSearch, permanentSearch;
    private String usersSearch;
    private long trancheSearch, equipeSearch;
    private int siteSearch;
    private Date debutSearch, finSearch;

    public ManagedCreneauEquipe() {
        creneaux = new ArrayList<>();
        historiques = new ArrayList<>();
    }

    public Boolean getPermanentSearch() {
        return permanentSearch;
    }

    public void setPermanentSearch(Boolean permanentSearch) {
        this.permanentSearch = permanentSearch;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
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

    public long getNiveau() {
        return niveau;
    }

    public void setNiveau(long niveau) {
        this.niveau = niveau;
    }

    public long getAgence() {
        return agence;
    }

    public void setAgence(long agence) {
        this.agence = agence;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public YvsProdCreneauEquipeProduction getEntity() {
        return entity;
    }

    public void setEntity(YvsProdCreneauEquipeProduction entity) {
        this.entity = entity;
    }

    public CrenauxHoraireEquipe getCreneau() {
        return creneau;
    }

    public void setCreneau(CrenauxHoraireEquipe creneau) {
        this.creneau = creneau;
    }

    public List<YvsProdCreneauEquipeProduction> getCreneaux() {
        return creneaux;
    }

    public void setCreneaux(List<YvsProdCreneauEquipeProduction> creneaux) {
        this.creneaux = creneaux;
    }

    public List<YvsProdCreneauEquipeProduction> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(List<YvsProdCreneauEquipeProduction> historiques) {
        this.historiques = historiques;
    }

    public boolean isAddDate() {
        return addDate;
    }

    public void setAddDate(boolean addDate) {
        this.addDate = addDate;
    }

    public String getUsersSearch() {
        return usersSearch;
    }

    public void setUsersSearch(String usersSearch) {
        this.usersSearch = usersSearch;
    }

    public long getTrancheSearch() {
        return trancheSearch;
    }

    public void setTrancheSearch(long trancheSearch) {
        this.trancheSearch = trancheSearch;
    }

    public int getSiteSearch() {
        return siteSearch;
    }

    public void setSiteSearch(int siteSearch) {
        this.siteSearch = siteSearch;
    }

    public long getEquipeSearch() {
        return equipeSearch;
    }

    public void setEquipeSearch(long equipeSearch) {
        this.equipeSearch = equipeSearch;
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

    @Override
    public void loadAll() {
        if (agence < 1) {
            agence = currentAgence.getId();
        }
        loadAll(true, true);
    }

    public void loadAll(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.site.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        creneaux = paginator.executeDynamicQuery("YvsProdCreneauEquipeProduction", "y.dateTravail DESC", avance, init, dao);
    }

    public void loadCalendrierByPers(Date dateDebut, Date dateFin) {
        historiques.clear();
        if ((creneau.getUsers() != null) ? creneau.getUsers().getId() > 0 : false) {
            champ = new String[]{"users", "dateDebut", "dateFin"};
            val = new Object[]{new YvsUsers(creneau.getUsers().getId()), dateDebut, dateFin};
            historiques = dao.loadNameQueries("YvsProdCreneauEquipeProduction.findByUsersDates", champ, val);

            champ = new String[]{"users", "permanent"};
            val = new Object[]{new YvsUsers(creneau.getUsers().getId()), true};
            List<YvsProdCreneauEquipeProduction> l = dao.loadNameQueries("YvsProdCreneauEquipeProduction.findByUsersPermanent", champ, val);
            for (YvsProdCreneauEquipeProduction c : l) {
                if (!historiques.contains(c)) {
                    historiques.add(c);
                }
            }
        }
        update("historique_creneau_equipe");
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsProdCreneauEquipeProduction> re = paginator.parcoursDynamicData("YvsProdCreneauEquipeProduction", "y", "y.dateTravail DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void init(boolean init) {
        loadAll(init, false);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAll(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAll(true, true);
    }

    @Override
    public boolean controleFiche(CrenauxHoraireEquipe bean) {
        if (bean == null) {
            return false;
        }
        if (bean.getEquipe() != null ? bean.getEquipe().getId() < 1 : true) {
            getErrorMessage("Vuos devez preciser l'equipe");
            return false;
        }
        if (bean.getUsers() != null ? bean.getUsers().getId() < 1 : true) {
            getErrorMessage("Vuos devez preciser l'utilisateur");
            return false;
        }
        if (bean.getSite() != null ? bean.getSite().getId() < 1 : true) {
            getErrorMessage("Vuos devez preciser le site de production");
            return false;
        }
        if (bean.getTranche() != null ? bean.getTranche().getId() < 1 : true) {
            getErrorMessage("Vuos devez preciser la tranche horaire");
            return false;
        }
        return true;
    }

    @Override
    public void resetFiche() {
        creneau = new CrenauxHoraireEquipe();
        historiques.clear();
        update("blog_form_creneau_equipe");
    }

    @Override
    public boolean saveNew() {
        try {
            if (controleFiche(creneau)) {
                entity = UtilProd.buildCrenauxEquipe(creneau, currentUser);
                if (creneau.getId() < 1) {
                    entity.setId(null);
                    entity = (YvsProdCreneauEquipeProduction) dao.save1(entity);
                } else {
                    dao.update(entity);
                }
                int idx = creneaux.indexOf(entity);
                if (idx > -1) {
                    creneaux.set(idx, entity);
                } else {
                    creneaux.add(0, entity);
                }
                idx = historiques.indexOf(entity);
                if (idx > -1) {
                    historiques.set(idx, entity);
                } else {
                    historiques.add(0, entity);
                }
                update("data_creneau_equipe");
                update("historique_creneau_equipe");
                succes();
                actionOpenOrResetAfter(this);
            }
        } catch (Exception ex) {
        }
        return false;
    }

    @Override
    public void onSelectObject(YvsProdCreneauEquipeProduction y) {
        entity = y;
        creneau = UtilProd.buildBeanCrenauxEquipe(y);
        chooseDate();
        update("blog_form_creneau_equipe");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsProdCreneauEquipeProduction) ev.getObject());
            tabIds = creneaux.indexOf((YvsProdCreneauEquipeProduction) ev.getObject()) + "";
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewUsers(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsUsers bean = (YvsUsers) ev.getObject();
            creneau.setUsers(UtilUsers.buildBeanUsers(bean));
            loadCalendrierByPers(dateDebut, dateFin);
            update("txt_employe_creneau_equipe");
        }
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsProdCreneauEquipeProduction> list = new ArrayList<>();
                YvsProdCreneauEquipeProduction bean;
                for (Long ids : l) {
                    bean = creneaux.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);
                    if (historiques.contains(bean)) {
                        historiques.remove(bean);
                        update("historique_creneau_equipe");
                    }
                }
                creneaux.removeAll(list);
                resetFiche();
                succes();
                update("data_creneau_equipe");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_() {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                dao.delete(entity);
                creneaux.remove(entity);
                historiques.remove(entity);
                if (entity.getId().equals(creneau.getId())) {
                    resetFiche();
                }
                update("data_creneau_equipe");
                update("historique_creneau_equipe");
                succes();
            }
        } catch (Exception ex) {
        }
    }

    public void deleteBean(YvsProdCreneauEquipeProduction y) {
        entity = y;
    }

    public void chooseDate() {
        if (creneau.getDateTravail() != null) {
            Date[] date = Util.getDayFirstEndWeek((creneau.getDateTravail() != null) ? creneau.getDateTravail() : new Date());
            dateDebut = date[0];
            dateFin = date[1];
            loadCalendrierByPers(dateDebut, dateFin);
        }
        update("select_creneau_equipe");
    }

    public void chooseAgence() {
        if (agence > 0) {
            ManagedAgence w = (ManagedAgence) giveManagedBean(ManagedAgence.class);
            if (w != null) {
                int idx = w.getListAgence().indexOf(new YvsAgences(agence));
                if (idx > -1) {
                    ManagedUser u = (ManagedUser) giveManagedBean(ManagedUser.class);
                    if (u != null) {
                        u.loadAgence(agence);
                    }
                }
            }
        }
    }

    public void chooseTranche() {
        if (creneau != null ? creneau.getTranche() != null : false) {
            ManagedTypeCreneau w = (ManagedTypeCreneau) giveManagedBean(ManagedTypeCreneau.class);
            if (w != null) {
                int idx = w.getTranches().indexOf(new YvsGrhTrancheHoraire(creneau.getTranche().getId()));
                if (idx > -1) {
                    YvsGrhTrancheHoraire y = w.getTranches().get(idx);
                    creneau.setTranche(UtilGrh.buildTrancheHoraire(y));
                }
            }
        }
    }

    public void chooseSite() {
        ManagedEquipeProduction service = (ManagedEquipeProduction) giveManagedBean(ManagedEquipeProduction.class);
        if (service != null) {
            service.getEquipes().clear();
        }
        if (creneau != null ? creneau.getSite() != null : false) {
            ManagedSiteProduction w = (ManagedSiteProduction) giveManagedBean(ManagedSiteProduction.class);
            if (w != null) {
                int idx = w.getSites().indexOf(new YvsProdSiteProduction(creneau.getSite().getId()));
                if (idx > -1) {
                    YvsProdSiteProduction y = w.getSites().get(idx);
                    creneau.setSite(UtilProd.buildBeanSiteProduction(y));
                    if (service != null) {
                        service.setSiteSearch(y.getId());
                        service.addParamSite();
                    }
                }
            }
        }
    }

    public void chooseEquipe() {
        if (creneau != null ? creneau.getEquipe() != null : false) {
            ManagedEquipeProduction w = (ManagedEquipeProduction) giveManagedBean(ManagedEquipeProduction.class);
            if (w != null) {
                int idx = w.getEquipes().indexOf(new YvsProdEquipeProduction(creneau.getEquipe().getId()));
                if (idx > -1) {
                    YvsProdEquipeProduction y = w.getEquipes().get(idx);
                    creneau.setEquipe(UtilProd.buildBeanEquipeProduction(y));
                }
            }
        }
    }

    public void chooseUsers() {
        if (creneau != null ? creneau.getUsers() != null : false) {
            ManagedUser w = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (w != null) {
                int idx = w.getListUser().indexOf(new YvsUsers(creneau.getUsers().getId()));
                if (idx > -1) {
                    YvsUsers y = w.getListUser().get(idx);
                    creneau.setUsers(UtilUsers.buildSimpleBeanUsers(y));
                }
            }
        }
    }

    public void searchPersonnel() {
        String num = creneau.getUsers().getCodeUsers();
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedUser m = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (m != null) {
                Users e = m.searchUsersActif(num, true);
                creneau.setUsers(e);
                if (m.getListUser() != null ? m.getListUser().size() > 1 : false) {
                    update("data_employe_creneau_equipe");
                }
                loadCalendrierByPers(dateDebut, dateFin);
            }
        }
    }

    public void initPersonnels() {
        ManagedUser m = (ManagedUser) giveManagedBean(ManagedUser.class);
        if (m != null) {
            m.initUsers(creneau.getUsers());
            update("data_employe_creneau_equipe");
        }
    }

    public void activeCreneau(YvsProdCreneauEquipeProduction bean) {
        if (bean != null) {
            if (!bean.getActif() && !bean.getTranche().getActif()) {
                getErrorMessage("Vous ne pouvez pas activer ce créneau car son type est désactivé");
            } else {
                bean.setActif(!bean.getActif());
                String rq = "UPDATE yvs_prod_creneau_equipe_production SET actif=" + bean.getActif() + " WHERE id=?";
                Options[] param = new Options[]{new Options(bean.getId(), 1)};
                dao.requeteLibre(rq, param);
                int idx = creneaux.indexOf(bean);
                if (idx > -1) {
                    creneaux.set(idx, bean);
                    update("data_creneau_equipe");
                }
                idx = historiques.indexOf(bean);
                if (idx > -1) {
                    historiques.set(idx, bean);
                    update("historique_creneau_equipe");
                }
            }
        }
    }

    public void clearParams() {
        usersSearch = null;
        addDate = false;
        actifSearch = null;
        debutSearch = new Date();
        finSearch = new Date();
        trancheSearch = 0;
        equipeSearch = 0;
        siteSearch = 0;
        paginator.getParams().clear();
        loadAll(true, true);
    }

    public void addParamUsers() {
        ParametreRequete p = new ParametreRequete("y.users", "users", null);
        if (usersSearch != null ? usersSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "users", usersSearch.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.users.nomUsers)", "users", usersSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.users.codeUsers)", "users", usersSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamActif() {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND");
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamPermanent() {
        ParametreRequete p = new ParametreRequete("y.permanent", "permanent", permanentSearch, "=", "AND");
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamSite() {
        ParametreRequete p = new ParametreRequete("y.site", "site", null);
        if (siteSearch > 0) {
            p = new ParametreRequete("y.site", "site", new YvsProdSiteProduction(siteSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamTranche() {
        ParametreRequete p = new ParametreRequete("y.tranche", "tranche", null);
        if (siteSearch > 0) {
            p = new ParametreRequete("y.tranche", "tranche", new YvsGrhTrancheHoraire(trancheSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamEquipe() {
        ParametreRequete p = new ParametreRequete("y.equipe", "equipe", null);
        if (siteSearch > 0) {
            p = new ParametreRequete("y.equipe", "equipe", new YvsProdEquipeProduction(equipeSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.debutValidite", "dates", null, "=", "AND");
        if (addDate) {
            p = new ParametreRequete("y.dateTravail", "dates", debutSearch, finSearch, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

}
