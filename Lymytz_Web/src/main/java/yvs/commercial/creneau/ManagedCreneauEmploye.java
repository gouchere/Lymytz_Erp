/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.creneau;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import yvs.commercial.UtilCom;
import yvs.commercial.depot.ManagedDepot;
import yvs.commercial.depot.ManagedPointVente;
import yvs.commercial.depot.PointVente;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;
import yvs.entity.commercial.creneau.YvsComCreneauPoint;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.personnel.YvsGrhPlanningEmploye;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.production.equipe.YvsProdEquipeProduction;
import yvs.entity.users.YvsUsers;
import yvs.grh.JoursOuvres;
import yvs.grh.presence.TrancheHoraire;
import yvs.parametrage.entrepot.Depots;
import yvs.production.ManagedOrdresF;
import yvs.production.UtilProd;
import yvs.production.base.EquipeProduction;
import yvs.users.ManagedUser;
import yvs.users.Users;
import yvs.users.UtilUsers;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedCreneauEmploye extends Managed<CreneauUsers, YvsComCreneauHoraireUsers> implements Serializable {

    @ManagedProperty(value = "#{creneauUsers}")
    private CreneauUsers creneauUsers;
    private List<YvsComCreneauHoraireUsers> creneauxEmpls, calendriers;
    private YvsComCreneauHoraireUsers selectCreneauUser;

    private Creneau creneau = new Creneau();
    private List<YvsComCreneauDepot> creneauxDepot;
    private List<YvsComCreneauPoint> creneauxPoint;

    private Depots depot = new Depots();
    private PointVente point = new PointVente();
    private EquipeProduction equipe = new EquipeProduction();

    private long agence;

    private ScheduleModel eventModel;

    private String tabIds;
    private boolean updateCreneau;
    private Date dateDebut = new Date(), dateFin = new Date();

    private boolean addDateSearch;
    private Boolean actifSearch, permanentSearch;
    private String typeSearch = "V", vendeurSearch;
    private long pointSearch, trancheSearch, depotSearch, equipeSearch;
    private Date dateSearch = new Date();

    private String fusionneTo;
    private List<String> fusionnesBy;

    public ManagedCreneauEmploye() {
        eventModel = new DefaultScheduleModel();
        creneauxEmpls = new ArrayList<>();
        calendriers = new ArrayList<>();
        creneauxDepot = new ArrayList<>();
        creneauxPoint = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
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

    public long getEquipeSearch() {
        return equipeSearch;
    }

    public void setEquipeSearch(long equipeSearch) {
        this.equipeSearch = equipeSearch;
    }

    public boolean isAddDateSearch() {
        return addDateSearch;
    }

    public void setAddDateSearch(boolean addDateSearch) {
        this.addDateSearch = addDateSearch;
    }

    public Date getDateSearch() {
        return dateSearch;
    }

    public void setDateSearch(Date dateSearch) {
        this.dateSearch = dateSearch;
    }

    public Boolean getPermanentSearch() {
        return permanentSearch;
    }

    public void setPermanentSearch(Boolean permanentSearch) {
        this.permanentSearch = permanentSearch;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public String getVendeurSearch() {
        return vendeurSearch;
    }

    public void setVendeurSearch(String vendeurSearch) {
        this.vendeurSearch = vendeurSearch;
    }

    public long getDepotSearch() {
        return depotSearch;
    }

    public void setDepotSearch(long depotSearch) {
        this.depotSearch = depotSearch;
    }

    public long getPointSearch() {
        return pointSearch;
    }

    public void setPointSearch(long pointSearch) {
        this.pointSearch = pointSearch;
    }

    public long getTrancheSearch() {
        return trancheSearch;
    }

    public void setTrancheSearch(long trancheSearch) {
        this.trancheSearch = trancheSearch;
    }

    public List<YvsComCreneauPoint> getCreneauxPoint() {
        return creneauxPoint;
    }

    public void setCreneauxPoint(List<YvsComCreneauPoint> creneauxPoint) {
        this.creneauxPoint = creneauxPoint;
    }

    public PointVente getPoint() {
        return point;
    }

    public void setPoint(PointVente point) {
        this.point = point;
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

    public long getAgence() {
        return agence;
    }

    public void setAgence(long agence) {
        this.agence = agence;
    }

    public YvsComCreneauHoraireUsers getSelectCreneauUser() {
        return selectCreneauUser;
    }

    public void setSelectCreneauUser(YvsComCreneauHoraireUsers selectCreneauUser) {
        this.selectCreneauUser = selectCreneauUser;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public List<YvsComCreneauHoraireUsers> getCalendriers() {
        return calendriers;
    }

    public void setCalendriers(List<YvsComCreneauHoraireUsers> calendriers) {
        this.calendriers = calendriers;
    }

    public Depots getDepot() {
        return depot;
    }

    public void setDepot(Depots depot) {
        this.depot = depot;
    }

    public CreneauUsers getCreneauUsers() {
        return creneauUsers;
    }

    public void setCreneauUsers(CreneauUsers creneauUsers) {
        this.creneauUsers = creneauUsers;
    }

    public List<YvsComCreneauHoraireUsers> getCreneauxEmpls() {
        return creneauxEmpls;
    }

    public void setCreneauxEmpls(List<YvsComCreneauHoraireUsers> creneauxEmpls) {
        this.creneauxEmpls = creneauxEmpls;
    }

    public Creneau getCreneau() {
        return creneau;
    }

    public void setCreneau(Creneau creneau) {
        this.creneau = creneau;
    }

    public List<YvsComCreneauDepot> getCreneauxDepot() {
        return creneauxDepot;
    }

    public void setCreneauxDepot(List<YvsComCreneauDepot> creneauxDepot) {
        this.creneauxDepot = creneauxDepot;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public boolean isUpdateCreneau() {
        return updateCreneau;
    }

    public void setUpdateCreneau(boolean updateCreneau) {
        this.updateCreneau = updateCreneau;
    }

    public EquipeProduction getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeProduction equipe) {
        this.equipe = equipe;
    }

    @Override
    public void loadAll() {
        init();
        loadAllCreneauEmpls(true, true);
    }

    public void load(boolean depot, boolean point) {
        if (agence < 1) {
            agence = currentAgence.getId();
        }

        Date[] date = Util.getDayFirstEndWeek((creneauUsers.getDateTravail() != null) ? creneauUsers.getDateTravail() : new Date());
        dateDebut = date[0];
        dateFin = date[1];
        if (depot) {
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null) {
                m.loadDepotActifByAgence(autoriser("view_all_depot_societe") ? -1 : agence);
            }
        }
        if (point) {
            ManagedPointVente p = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
            if (p != null) {
                p.loadAllPointVente(autoriser("pv_view_all_societe") ? -1 : agence);
            }
        }
    }

    public void init() {
        load(true, true);
    }

    public void loadCalendrierByPers(Date dateDebut, Date dateFin) {
        calendriers.clear();
        if ((creneauUsers.getPersonnel() != null) ? creneauUsers.getPersonnel().getId() > 0 : false) {
            champ = new String[]{"users", "dateDebut", "dateFin"};
            val = new Object[]{new YvsUsers(creneauUsers.getPersonnel().getId()), dateDebut, dateFin};
            calendriers = dao.loadNameQueries("YvsComCreneauHoraireUsers.findByUsersDates", champ, val);

            champ = new String[]{"users", "permanent"};
            val = new Object[]{new YvsUsers(creneauUsers.getPersonnel().getId()), true};
            List<YvsComCreneauHoraireUsers> l = dao.loadNameQueries("YvsComCreneauHoraireUsers.findByUsersPermanent", champ, val);
            for (YvsComCreneauHoraireUsers c : l) {
                if (!calendriers.contains(c)) {
                    calendriers.add(c);
                }
            }
        }
        update("data_planing_employe");
    }

    public void loadAllCreneauEmpls(boolean avance, boolean init) {
        ParametreRequete p = new ParametreRequete("y.users.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
        paginator.addParam(p);
        if (agence > 0) {
            paginator.addParam(new ParametreRequete("y.users.agence.id", "agence", agence, "=", "AND"));
        }
        creneauxEmpls = paginator.executeDynamicQuery("y", "y", "YvsComCreneauHoraireUsers y", "y.dateTravail DESC", avance, init, (int) imax, "id", dao);
        eventModel.clear();
        for (YvsComCreneauHoraireUsers c : creneauxEmpls) {
            eventModel.addEvent(UtilCom.buildSimpleBeanCreneauUsers(c));
        }
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComCreneauHoraireUsers> re = paginator.parcoursDynamicData("YvsComCreneauHoraireUsers", "y", "y.dateTravail DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void init(boolean init) {
        loadAllCreneauEmpls(init, false);
    }

    public void loadAllCreneauxDepot(YvsBaseDepots depot, Date date) {
        champ = new String[]{"depot", "jour"};
        val = new Object[]{depot, Util.getDay(date)};
        nameQueri = "YvsComCreneauDepot.findByJourDepot_";
        creneauxDepot = dao.loadNameQueries(nameQueri, champ, val);

        champ = new String[]{"depot", "permanent"};
        val = new Object[]{depot, true};
        nameQueri = "YvsComCreneauDepot.findByDepotPermanent";
        List<YvsComCreneauDepot> lc = dao.loadNameQueries(nameQueri, champ, val);
        for (YvsComCreneauDepot c : lc) {
            if (!creneauxDepot.contains(c)) {
                creneauxDepot.add(c);
            }
        }
    }

    public void loadAllCreneauxPoint(YvsBasePointVente y, Date date) {
        champ = new String[]{"point", "jour"};
        val = new Object[]{y, Util.getDay(date)};
        nameQueri = "YvsComCreneauPoint.findByJourDepot_";
        creneauxPoint = dao.loadNameQueries(nameQueri, champ, val);

        champ = new String[]{"point", "permanent"};
        val = new Object[]{y, true};
        nameQueri = "YvsComCreneauPoint.findByDepotPermanent";
        List<YvsComCreneauPoint> lc = dao.loadNameQueries(nameQueri, champ, val);
        for (YvsComCreneauPoint c : lc) {
            if (!creneauxPoint.contains(c)) {
                creneauxPoint.add(c);
            }
        }
    }

    public YvsComCreneauHoraireUsers buildCreneauEmploye(CreneauUsers y) {
        YvsComCreneauHoraireUsers c = new YvsComCreneauHoraireUsers();
        if (y != null) {
            c.setActif(y.isActif());
            c.setId(y.getId_());
            if ((y.getCreneauPoint() != null) ? y.getCreneauPoint().getId() > 0 : false) {
                if (creneauxPoint.contains(new YvsComCreneauPoint(y.getCreneauPoint().getId()))) {
                    c.setCreneauPoint(creneauxPoint.get(creneauxPoint.indexOf(new YvsComCreneauPoint(y.getCreneauPoint().getId()))));
                } else {
                    c.setCreneauPoint(new YvsComCreneauPoint(y.getCreneauPoint().getId()));
                }
            }
            if ((y.getCreneauDepot() != null) ? y.getCreneauDepot().getId() > 0 : false) {
                if (creneauxDepot.contains(new YvsComCreneauDepot(y.getCreneauDepot().getId()))) {
                    c.setCreneauDepot(creneauxDepot.get(creneauxDepot.indexOf(new YvsComCreneauDepot(y.getCreneauDepot().getId()))));
                } else {
                    c.setCreneauDepot(new YvsComCreneauDepot(y.getCreneauDepot().getId()));
                }
            }
            if ((y.getPersonnel() != null) ? y.getPersonnel().getId() > 0 : false) {
                c.setUsers(new YvsUsers(y.getPersonnel().getId(), y.getPersonnel().getNomUsers()));
            }
            c.setType(y.getType());
            c.setDateTravail((y.getDateTravail() != null) ? y.getDateTravail() : new Date());
            c.setPermanent(y.isPermanent());
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(new Date());
            if (equipe.getId() > 0) {
                c.setEquipe(UtilProd.buildEquipeProduction(equipe, currentUser));
            }
            c.setAuthor(currentUser);
        }
        return c;
    }

    public YvsComCreneauDepot buildCreneau(Creneau y) {
        YvsComCreneauDepot c = new YvsComCreneauDepot();
        if (y != null) {
            c.setId(y.getId());
            c.setActif(y.isActif());
            if ((y.getDepot() != null) ? y.getDepot().getId() > 0 : false) {
                c.setDepot(new YvsBaseDepots(y.getDepot().getId()));
            }
            if ((y.getTranche() != null) ? y.getTranche().getId() > 0 : false) {
                c.setTranche(new YvsGrhTrancheHoraire(y.getTranche().getId()));
            }
            if ((y.getJour() != null) ? y.getJour().getId() > 0 : false) {
                c.setJour(new YvsJoursOuvres(y.getJour().getId()));
            }
            c.setAuthor(currentUser);
        }
        return c;
    }

    @Override
    public CreneauUsers recopieView() {
        creneauUsers.setNew_(true);
        return creneauUsers;
    }

    public Creneau recopieViewCreneau() {
        Creneau c = new Creneau();
        c.setId(creneau.getId());
        c.setActif(creneau.getTranche().isActif());
        c.setDepot(creneau.getDepot());
        c.setTranche(creneau.getTranche());
        c.setJour(creneau.getJour());
        c.setActif(true);
        c.setNew_(true);
        String lib = c.getDepot().getDesignation() + " - " + c.getJour().getJour() + " : " + Util.getTimeToString(c.getTranche().getHeureDebut()) + " à " + Util.getTimeToString(c.getTranche().getHeureFin());
        c.setReference(lib);
        return c;
    }

    @Override
    public boolean controleFiche(CreneauUsers bean) {
        if ((bean.getPersonnel() != null) ? bean.getPersonnel().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner l'employe");
            return false;
        }
        if (((bean.getCreneauDepot() != null) ? bean.getCreneauDepot().getId() < 1 : true) && ((bean.getCreneauPoint() != null) ? bean.getCreneauPoint().getId() < 1 : true)) {
            getErrorMessage("Vous devez specifier le creneau");
            return false;
        }
        if (bean.getCreneauDepot() != null ? bean.getCreneauDepot().getId() > 0 : false) {
            if (bean.isActif() && !bean.getCreneauDepot().isActif()) {
                getErrorMessage("Vous ne pouvez pas créer ce créneau car son type est désactivé");
                return false;
            }
        }
        if (bean.getCreneauPoint() != null ? bean.getCreneauPoint().getId() > 0 : false) {
            if (bean.isActif() && !bean.getCreneauPoint().isActif()) {
                getErrorMessage("Vous ne pouvez pas créer ce créneau car son type est désactivé");
                return false;
            }
        }
        if (!bean.isPermanent()) {
            champ = new String[]{"users", "dateTravail", "creneau"};
            val = new Object[]{new YvsUsers(bean.getPersonnel().getId()), bean.getDateTravail(), new YvsComCreneauDepot(bean.getCreneauDepot().getId())};
            nameQueri = "YvsComCreneauHoraireUsers.findByUsersDateCreneau";
            List<YvsComCreneauHoraireUsers> lc = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
            if (lc != null ? !lc.isEmpty() : false) {
                if (!lc.get(0).getId().equals(bean.getId_())) {
                    getErrorMessage("Vous avez déja programmé ce planning");
                    return false;
                }
            }
        } else {
            champ = new String[]{"users", "permanent", "creneau"};
            val = new Object[]{new YvsUsers(bean.getPersonnel().getId()), true, new YvsComCreneauDepot(bean.getCreneauDepot().getId())};
            nameQueri = "YvsComCreneauHoraireUsers.findByUsersPermanentCreneau";
            List<YvsComCreneauHoraireUsers> lc = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
            if (lc != null ? !lc.isEmpty() : false) {
                if (!lc.get(0).getId().equals(bean.getId_())) {
                    getErrorMessage("Vous avez déja programmé ce planning");
                    return false;
                }
            }

            champ = new String[]{"users", "depot"};
            val = new Object[]{new YvsUsers(bean.getPersonnel().getId()), new YvsBaseDepots(bean.getCreneauDepot().getDepot().getId())};
            nameQueri = "YvsComCreneauHoraireUsers.findByUsersPermActifDepot";
            lc = dao.loadNameQueries(nameQueri, champ, val);
            for (YvsComCreneauHoraireUsers c : lc) {
                if (!c.getId().equals(bean.getId_())) {
                    getErrorMessage("Vous ne pouvez pas activer ce créneau car cette employé a deja un créneau permanent pour ce depot");
                    return false;
                }
            }
        }
        if (selectCreneauUser != null ? selectCreneauUser.getId() > 0 : false) {
            Long count = (Long) dao.loadObjectByNameQueries("YvsComEnteteDocVente.countByCreneau", new String[]{"creneau"}, new Object[]{selectCreneauUser});
            if (count != null ? count > 0 : false) {
                if (!autoriser("gescom_change_creneau_use")) {
                    getErrorMessage("Vous ne pouvez pas modifier ce créneau... Il a deja été utilisé");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean controleFicheCreneau(Creneau bean) {
        if ((bean.getDepot() != null) ? bean.getDepot().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le dépot");
            return false;
        }
        if ((bean.getJour() != null) ? bean.getJour().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le jour");
            return false;
        }
        if ((bean.getTranche() != null) ? bean.getTranche().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le type");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(CreneauUsers bean) {
        if (bean.getCreneauDepot() != null) {
            cloneObject(depot, bean.getCreneauDepot().getDepot());
        }
        if (bean.getCreneauPoint() != null) {
            cloneObject(point, bean.getCreneauPoint().getPoint());
        }
        cloneObject(creneauUsers, bean);
        creneauxDepot.clear();
        creneauxPoint.clear();
        setUpdateCreneau(true);
    }

    @Override
    public void resetFiche() {
        _resetFiche();
        calendriers.clear();
        update("txt_agence");
        update("blog_form_creneau_employe");
    }

    public void _resetFiche() {
        resetFiche(creneauUsers);
        creneauUsers.setPermanent(false);
        creneauUsers.setPersonnel(new Users());
        creneauUsers.setCreneauDepot(new Creneau());
        creneauUsers.setCreneauPoint(new Creneau());
        creneauUsers.getCreneauDepot().setTranche(new TrancheHoraire());
        selectCreneauUser = null;

        calendriers.clear();
        depot = new Depots();
        creneauxDepot.clear();
        point = new PointVente();
        creneauxPoint.clear();

        selectCreneauUser = new YvsComCreneauHoraireUsers();

        tabIds = "";
        setUpdateCreneau(false);
        chooseAgence();
    }

    public void resetFicheCreneau() {
        resetFiche(creneau);
        creneau.setDepot(new Depots());
        creneau.setJour(new JoursOuvres());
        creneau.setTranche(new TrancheHoraire());
    }

    @Override
    public boolean saveNew() {
        String action = isUpdateCreneau() ? "Modification" : "Insertion";
        try {
            YvsComCreneauHoraireUsers y = saveNew(recopieView());
            if (y != null ? y.getId() != null : false) {
                this.selectCreneauUser = y;
                succes();
                actionOpenOrResetAfter(this);
                update("data_creneau_employe");
                update("blog_employe_creneau");
            } else {
                return false;
            }
            if (y.getEquipe() != null) {
                // Enregistrer la session de production
                ManagedOrdresF service = (ManagedOrdresF) giveManagedBean(ManagedOrdresF.class);
                if (service != null) {
//                    service.saveNewSessionProduction(y.getDateTravail(), y.getUsers(), y.getCreneauDepot().getTranche(), y.getCreneauDepot().getDepot(), y.getEquipe());
                }
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            log.log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public YvsComCreneauHoraireUsers saveNew(CreneauUsers bean) {
        String action = isUpdateCreneau() ? "Modification" : "Insertion";
        try {
            if (controleFiche(bean)) {
                YvsComCreneauHoraireUsers entity = buildCreneauEmploye(bean);
                if (entity.getId() < 1) {
                    entity.setId(null);
                    entity = (YvsComCreneauHoraireUsers) dao.save1(entity);
                    creneauUsers.setId_(entity.getId());
                    creneauxEmpls.add(0, entity);
                    calendriers.add(0, entity);
                } else {
                    dao.update(entity);
                    if (creneauxEmpls.contains(entity)) {
                        creneauxEmpls.set(creneauxEmpls.indexOf(entity), entity);
                    }
                    if (bean.isPermanent()) {
                        if (calendriers.contains(entity)) {
                            calendriers.set(calendriers.indexOf(entity), entity);
                        }
                    } else {
                        if (dateDebut.compareTo(bean.getDateTravail()) >= 0 && bean.getDateTravail().compareTo(dateFin) >= 0) {
                            if (calendriers.contains(entity)) {
                                calendriers.set(calendriers.indexOf(entity), entity);
                            }
                        } else {
                            calendriers.remove(entity);
                        }
                    }
                }
                return entity;
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean saveNewCreneau() {
        try {
            Creneau bean = recopieViewCreneau();
            if (controleFicheCreneau(bean)) {
                YvsComCreneauDepot entity = buildCreneau(bean);
                entity.setId(null);
                entity = (YvsComCreneauDepot) dao.save1(entity);
                bean.setId(entity.getId());
                creneauxDepot.add(0, entity);
                creneauUsers.setCreneauDepot(bean);
                cloneObject(depot, bean.getDepot());
                succes();
                resetFicheCreneau();
                update("blog_form_creneau_employe");
                update("blog_form_creneau");
            }
        } catch (Exception ex) {
            getErrorMessage("Inserton Impossible !");
            System.err.println("Error Insertion : " + ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsComCreneauHoraireUsers> list = new ArrayList<>();
                YvsComCreneauHoraireUsers bean;
                for (Long ids : l) {

                    bean = creneauxEmpls.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);

                    if (calendriers.contains(bean)) {
                        calendriers.remove(bean);
                        update("data_planing_employe");
                    }
//                    eventModel.deleteEvent(bean);
                }
                resetFiche();
                creneauxEmpls.removeAll(list);
                succes();
                tabIds = "";

                update("data_creneau_employe");
//                update("schedule");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsComCreneauHoraireUsers y) {
        selectCreneauUser = y;
    }

    public void deleteBean_() {
        try {
            if (selectCreneauUser != null) {
                dao.delete(selectCreneauUser);
                creneauxEmpls.remove(selectCreneauUser);
//                eventModel.deleteEvent(selectCreneauUser);
                if (calendriers.contains(selectCreneauUser)) {
                    calendriers.remove(selectCreneauUser);
                    update("data_planing_employe");
                }
                selectCreneauUser = null;
                succes();
                update("data_creneau_employe");
//                update("schedule");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void onSelectObject(YvsComCreneauHoraireUsers bean) {
        onSelectObject(bean, true);
    }

    public void onSelectObject(YvsComCreneauHoraireUsers bean, boolean load) {
        selectCreneauUser = bean;
        populateView(UtilCom.buildBeanCreneauUsers(bean));
        if (bean.getCreneauDepot() != null ? bean.getCreneauDepot().getDepot() != null : false) {
            if (bean.getCreneauDepot().getDepot().getAgence() != null) {
                agence = bean.getCreneauDepot().getDepot().getAgence().getId();
            }
            loadAllCreneauxDepot(bean.getCreneauDepot().getDepot(), bean.getDateTravail());
        }
        if (bean.getCreneauPoint() != null ? bean.getCreneauPoint().getPoint() != null : false) {
            if (bean.getCreneauPoint().getPoint().getAgence() != null) {
                agence = bean.getCreneauPoint().getPoint().getAgence().getId();
            }
            loadAllCreneauxPoint(bean.getCreneauPoint().getPoint(), bean.getDateTravail());
        }
        ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (m != null) {
            m.loadDepotActifByAgence(agence);
        }
        ManagedPointVente p = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
        if (p != null) {
            p.loadAllPointVente(agence);
        }
        ManagedUser u = (ManagedUser) giveManagedBean(ManagedUser.class);
        if (u != null) {
            u.loadAgence(agence);
        }
        if (load) {
            Date[] date = Util.getDayFirstEndWeek((creneauUsers.getDateTravail() != null) ? creneauUsers.getDateTravail() : new Date());
            dateDebut = date[0];
            dateFin = date[1];
            loadCalendrierByPers(dateDebut, dateFin);
        }
        update("blog_form_creneau_employe");
        update("txt_agence");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComCreneauHoraireUsers bean = (YvsComCreneauHoraireUsers) ev.getObject();
            onSelectObject(bean);
            tabIds = creneauxEmpls.indexOf(bean) + "";
        }
    }

    public void loadOnView_(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComCreneauHoraireUsers bean = (YvsComCreneauHoraireUsers) ev.getObject();
            onSelectObject(bean, false);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        _resetFiche();
        update("form_creneau_employe");
    }

    public void loadOnViewUsers(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsUsers bean = (YvsUsers) ev.getObject();
            creneauUsers.setPersonnel(UtilUsers.buildBeanUsers(bean));
            loadCalendrierByPers(dateDebut, dateFin);
            update("txt_employe_creneau");
        }
    }

    public void chooseAgence() {
        if (agence > 0) {
            loadAllCreneauEmpls(true, true);
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null) {
                m.loadDepotActifByAgence(agence);
            }
            ManagedPointVente p = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
            if (p != null) {
                p.loadAllPointVente(agence);
            }
            ManagedUser u = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (u != null) {
                u.loadAgence(agence);
            }
            update("blog_form_creneau_employe");
            update("data_creneau_employe");
            update("schedule");
            update("data_employe_creneau");
        }
    }

    public void chooseDepot() {
        creneauxDepot.clear();
        if ((depot != null) ? depot.getId() > 0 : false) {
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null) {
                YvsBaseDepots y = m.getDepots().get(m.getDepots().indexOf(new YvsBaseDepots(depot.getId())));
                depot = UtilCom.buildBeanDepot(y);
                loadAllCreneauxDepot(y, creneauUsers.getDateTravail());
                if (creneauxDepot != null ? !creneauxDepot.isEmpty() : false) {
                    creneauUsers.setCreneauDepot(UtilCom.buildBeanCreneau(creneauxDepot.get(0)));
                }
                if (point != null ? point.getId() < 1 : true) {
                    loadPointsByDepot();
                } else if (creneauUsers.getCreneauPoint() != null ? creneauUsers.getCreneauPoint().getId() < 1 : true) {
                    loadCreneauByPoint();
                }
            }
        }
    }

    public void loadPointsByDepot() {
        try {
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null ? depot.getId() > 0 : false) {
                int index = m.getDepots().indexOf(new YvsBaseDepots(depot.getId()));
                if (index > -1) {
                    YvsBaseDepots y = m.getDepots().get(index);
                    ManagedPointVente w = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                    List<YvsBasePointVente> list = dao.loadNameQueries("YvsBasePointVenteDepot.findPointByDepot", new String[]{"depot"}, new Object[]{y});
                    if (list != null ? !list.isEmpty() : false) {
                        point = UtilCom.buildBeanPointVente(list.get(0));
                        loadCreneauByPoint();
                    }
                    if (w != null) {
                        w.setPointsvente(list);
                    }
                }
            }
        } catch (Exception ex) {
            getException("loadPointsByDepot", ex);
        }
    }

    public void loadCreneauByPoint() {
        try {
            ManagedPointVente w = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
            if (w != null ? point.getId() > 0 : false) {
                int index = w.getPointsvente().indexOf(new YvsBasePointVente(point.getId()));
                if (index > -1) {
                    loadAllCreneauxPoint(w.getPointsvente().get(index), creneauUsers.getDateTravail());
                    if (creneauxPoint != null ? !creneauxPoint.isEmpty() : false) {
                        creneauUsers.setCreneauPoint(UtilCom.buildBeanCreneau(creneauxPoint.get(0)));
                    }
                }
            }
        } catch (Exception ex) {
            getException("loadCreneauByPoint", ex);
        }
    }

    public void choosePoint() {
        creneauxPoint.clear();
        if ((point != null) ? point.getId() > 0 : false) {
            ManagedPointVente m = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
            if (m != null) {
                YvsBasePointVente y = m.getPointsvente().get(m.getPointsvente().indexOf(new YvsBasePointVente(point.getId())));
                point = UtilCom.buildBeanPointVente(y);
                loadAllCreneauxPoint(y, creneauUsers.getDateTravail());
                if (creneauxPoint != null ? !creneauxPoint.isEmpty() : false) {
                    creneauUsers.setCreneauPoint(UtilCom.buildBeanCreneau(creneauxPoint.get(0)));
                }
                if (depot != null ? depot.getId() < 1 : true) {
                    loadDepotsByPoint();
                } else if (creneauUsers.getCreneauDepot() != null ? creneauUsers.getCreneauDepot().getId() < 1 : true) {
                    loadCreneauByDepot();
                }
            }
        }
    }

    public void loadDepotsByPoint() {
        try {
            ManagedPointVente m = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
            if (m != null ? point.getId() > 0 : false) {
                int index = m.getPointsvente().indexOf(new YvsBasePointVente(point.getId()));
                if (index > -1) {
                    YvsBasePointVente y = m.getPointsvente().get(index);
                    ManagedDepot w = (ManagedDepot) giveManagedBean(ManagedDepot.class);
                    List<YvsBaseDepots> list = dao.loadNameQueries("YvsBasePointVenteDepot.findDepotByPoint", new String[]{"pointVente"}, new Object[]{y});
                    if (list != null ? !list.isEmpty() : false) {
                        depot = UtilCom.buildSimpleBeanDepot(list.get(0));
                        loadCreneauByDepot();
                    }
                    if (w != null) {
                        w.setDepots(list);
                    }
                }
            }
        } catch (Exception ex) {
            getException("loadDepotsByPoint", ex);
        }
    }

    public void loadCreneauByDepot() {
        try {
            ManagedDepot w = (ManagedDepot) giveManagedBean(ManagedPointVente.class);
            if (w != null ? depot.getId() > 0 : false) {
                int index = w.getDepots().indexOf(new YvsBaseDepots(depot.getId()));
                if (index > -1) {
                    loadAllCreneauxDepot(w.getDepots().get(0), creneauUsers.getDateTravail());
                    if (creneauxDepot != null ? !creneauxDepot.isEmpty() : false) {
                        creneauUsers.setCreneauDepot(UtilCom.buildBeanCreneau(creneauxDepot.get(0)));
                    }
                }
            }
        } catch (Exception ex) {
            getException("loadCreneauByPoint", ex);
        }
    }

    public void chooseCreneauDepot() {
        if ((creneauUsers.getCreneauDepot() != null) ? creneauUsers.getCreneauDepot().getId() > 0 : false) {
            int idx = creneauxDepot.indexOf(new YvsComCreneauDepot(creneauUsers.getCreneauDepot().getId()));
            if (idx > -1) {
                YvsComCreneauDepot y = creneauxDepot.get(idx);
                creneauUsers.setCreneauDepot(UtilCom.buildBeanCreneau(y));
            }
        }
    }

    public void chooseCreneauPoint() {
        if ((creneauUsers.getCreneauPoint() != null) ? creneauUsers.getCreneauPoint().getId() > 0 : false) {
            int idx = creneauxPoint.indexOf(new YvsComCreneauPoint(creneauUsers.getCreneauPoint().getId()));
            if (idx > -1) {
                YvsComCreneauPoint y = creneauxPoint.get(idx);
                creneauUsers.setCreneauPoint(UtilCom.buildBeanCreneau(y));
            }
        }
    }

    public void chooseDate() {
        creneauxDepot.clear();
        if (creneauUsers.getDateTravail() != null) {
            loadAllCreneauxDepot(new YvsBaseDepots(depot.getId()), creneauUsers.getDateTravail());
            loadAllCreneauxPoint(new YvsBasePointVente(point.getId()), creneauUsers.getDateTravail());
            Date[] date = Util.getDayFirstEndWeek((creneauUsers.getDateTravail() != null) ? creneauUsers.getDateTravail() : new Date());
            dateDebut = date[0];
            dateFin = date[1];
            loadCalendrierByPers(dateDebut, dateFin);
            update("blog_employe_creneau");
        }
        update("select_creneau");
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllCreneauEmpls(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllCreneauEmpls(true, true);
    }

    public void onEventSelect(SelectEvent ev) {
        YvsComCreneauHoraireUsers bean = (YvsComCreneauHoraireUsers) ev.getObject();
        populateView(UtilCom.buildBeanCreneauUsers(bean));
        update("view_detail_creneau");
    }

    public void activeCreneau(YvsComCreneauHoraireUsers bean) {
        if (bean != null) {
            if (!bean.getActif()) {
                if (bean.getCreneauPoint() != null ? bean.getCreneauPoint().getId() > 0 : false) {
                    if (!bean.getCreneauPoint().getActif()) {
                        getErrorMessage("Vous ne pouvez pas activer ce créneau car son type est désactivé");
                        return;
                    } else if (bean.getPermanent()) {
                        champ = new String[]{"users", "point"};
                        val = new Object[]{bean.getUsers(), bean.getCreneauPoint().getPoint()};
                        nameQueri = "YvsComCreneauHoraireUsers.findByUsersPermActifPoint";
                        List<YvsComCreneauHoraireUsers> l = dao.loadNameQueries(nameQueri, champ, val);
                        for (YvsComCreneauHoraireUsers c : l) {
                            if (!c.getId().equals(bean.getId())) {
                                getErrorMessage("Vous ne pouvez pas activer ce créneau car cette employé a deja un créneau permanent dans ce point");
                                return;
                            }
                        }
                    }
                }

                if (bean.getCreneauDepot() != null ? bean.getCreneauDepot().getId() > 0 : false) {
                    if (!bean.getCreneauDepot().getActif()) {
                        getErrorMessage("Vous ne pouvez pas activer ce créneau car son type est désactivé");
                        return;
                    } else if (bean.getPermanent()) {
                        champ = new String[]{"users", "depot"};
                        val = new Object[]{bean.getUsers(), bean.getCreneauDepot().getDepot()};
                        nameQueri = "YvsComCreneauHoraireUsers.findByUsersPermActifDepot";
                        List<YvsComCreneauHoraireUsers> l = dao.loadNameQueries(nameQueri, champ, val);
                        for (YvsComCreneauHoraireUsers c : l) {
                            if (!c.getId().equals(bean.getId())) {
                                getErrorMessage("Vous ne pouvez pas activer ce créneau car cette employé a deja un créneau permanent dans ce depot");
                                return;
                            }
                        }
                    }
                }
            }
            bean.setActif(!bean.getActif());
            String rq = "UPDATE yvs_com_creneau_horaire_users SET actif=" + bean.getActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            if (creneauxEmpls.contains(bean)) {
                creneauxEmpls.set(creneauxEmpls.indexOf(bean), bean);
            }
            if (calendriers.contains(bean)) {
                calendriers.set(calendriers.indexOf(bean), bean);
                update("data_planing_employe");
            }
            update("data_creneau_employe");
        }
    }

    public void searchPersonnel() {
        String num = creneauUsers.getPersonnel().getCodeUsers();
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedUser m = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (m != null) {
                Users e = m.searchUsersActif(num, true);
                creneauUsers.setPersonnel(e);
                if (m.getListUser() != null ? m.getListUser().size() > 1 : false) {
                    update("data_employe_creneau");
                }
                loadCalendrierByPers(dateDebut, dateFin);
            }
        }
    }

    public void initPersonnels() {
        ManagedUser m = (ManagedUser) giveManagedBean(ManagedUser.class);
        if (m != null) {
            m.initUsers(creneauUsers.getPersonnel());
            update("data_employe_creneau");
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void clearParam() {
        paginator.getParams().clear();
        loadAllCreneauEmpls(true, true);
    }

    public void addParamType() {
        ParametreRequete p = new ParametreRequete("y.type", "type", null, "=", "AND");
        if (typeSearch != null ? typeSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.type", "type", typeSearch, "=", "AND");
        }
        paginator.addParam(p);
        depotSearch = 0;
        addParamDepot();
        pointSearch = 0;
        addParamPoint();
        loadAllCreneauEmpls(true, true);
    }

    public void addParamActif() {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
        loadAllCreneauEmpls(true, true);
    }

    public void addParamPermanent() {
        paginator.addParam(new ParametreRequete("y.permanent", "permanent", permanentSearch, "=", "AND"));
        loadAllCreneauEmpls(true, true);
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("y.dateTravail", "dateTravail", null, "=", "AND");
        if (addDateSearch ? dateSearch != null : false) {
            p = new ParametreRequete("y.dateTravail", "dateTravail", dateSearch, "= ", "AND");
        }
        paginator.addParam(p);
        loadAllCreneauEmpls(true, true);
    }

    public void addParamUsers() {
        ParametreRequete p = new ParametreRequete("y.users.codeUsers", "codeUsers", null, "=", "AND");
        if (vendeurSearch != null ? vendeurSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "codeUsers", vendeurSearch.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.users.codeUsers)", "codeUsers", vendeurSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.users.nomUsers)", "codeUsers", vendeurSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllCreneauEmpls(true, true);
    }

    public void addParamDepot() {
        ParametreRequete p = new ParametreRequete("y.creneauDepot.depot", "depot", null, "=", "AND");
        if (depotSearch > 0) {
            p = new ParametreRequete("y.creneauDepot.depot", "depot", new YvsBaseDepots(depotSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAllCreneauEmpls(true, true);
    }

    public void addParamPoint() {
        ParametreRequete p = new ParametreRequete("y.creneauPoint.point", "point", null, "=", "AND");
        if (pointSearch > 0) {
            p = new ParametreRequete("y.creneauPoint.point", "point", new YvsBasePointVente(pointSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAllCreneauEmpls(true, true);
    }

    public void addParamEquipe() {
        ParametreRequete p = new ParametreRequete("y.equipe", "equipe", null, "=", "AND");
        if (equipeSearch > 0) {
            p = new ParametreRequete("y.equipe", "equipe", new YvsProdEquipeProduction(equipeSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAllCreneauEmpls(true, true);
    }

    public void addParamTranche() {
        ParametreRequete p = new ParametreRequete("y.creneauPoint.tranche", "tranche", null, "=", "AND");
        if (trancheSearch > 0) {
            p = new ParametreRequete(null, "tranche", new YvsGrhTrancheHoraire(trancheSearch), "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.creneauPoint.tranche", "tranche", new YvsGrhTrancheHoraire(trancheSearch), "=", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.creneauDepot.tranche", "tranche", new YvsGrhTrancheHoraire(trancheSearch), "=", "OR"));
        }
        paginator.addParam(p);
        loadAllCreneauEmpls(true, true);
    }

    public void fusionner(boolean fusionne) {
        try {
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                long newValue = creneauxEmpls.get(ids.get(0)).getId();
                if (!fusionne) {
                    if (!autoriser("base_user_fusion")) {
                        openNotAcces();
                        return;
                    }
                    String oldValue = "0";
                    for (int i : ids) {
                        if (creneauxEmpls.get(i).getId() != newValue) {
                            oldValue += "," + creneauxEmpls.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_com_creneau_horaire_users", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                creneauxEmpls.remove(new YvsComCreneauHoraireUsers(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = creneauxEmpls.get(idx).getTitle();
                    } else {
                        YvsComCreneauHoraireUsers c = (YvsComCreneauHoraireUsers) dao.loadOneByNameQueries("YvsComCreneauHoraireUsers.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? c.getId() > 0 : false) {
                            fusionneTo = c.getTitle();
                        }
                    }
                    YvsComCreneauHoraireUsers c;
                    for (int i : ids) {
                        long oldValue = creneauxEmpls.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(creneauxEmpls.get(i).getTitle());
                            }
                        } else {
                            c = (YvsComCreneauHoraireUsers) dao.loadOneByNameQueries("YvsComCreneauHoraireUsers.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? c.getId() > 0 : false) {
                                fusionnesBy.add(c.getTitle());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 plannings");
            }
        } catch (NumberFormatException ex) {
        }
    }

    public void buildPlanning(YvsComCreneauHoraireUsers y, boolean msg) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Date dateDebut = y.getPermanent() ? new Date() : y.getDateTravail();
                if (y.getCreneauDepot() != null ? (y.getCreneauDepot().getTranche() != null ? y.getCreneauDepot().getTranche().getId() < 1 : true) : true) {
                    if (msg) {
                        getErrorMessage("Le planning doit etre généré à partir de la plannification dans un dépot");
                    }
                    return;
                }
                YvsGrhTrancheHoraire tranche = y.getCreneauDepot().getTranche();
                YvsGrhEmployes employe = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByUsers", new String[]{"user"}, new Object[]{y.getUsers()});
                if (employe != null ? employe.getId() < 1 : true) {
                    if (msg) {
                        getErrorMessage("Cet utilisateur n'a pas de compte employé");
                    }
                    return;
                }
                YvsGrhPlanningEmploye planning = (YvsGrhPlanningEmploye) dao.loadOneByNameQueries("YvsGrhPlanningEmploye.findOnePlan", new String[]{"employe", "tranche", "date"}, new Object[]{employe, tranche, dateDebut});
                if (planning != null ? planning.getId() > 0 : false) {
                    if (msg) {
                        getErrorMessage("Cet utilisateur a déjà un planning ce jour à cette tranche");
                    }
                    return;
                }
                planning = new YvsGrhPlanningEmploye();
                planning.setActif(true);
                planning.setAuthor(currentUser);
                planning.setDateDebut(dateDebut);
                Date dateFin = dateDebut;
                Date debut = Util.buildTimeStamp(dateDebut, tranche.getHeureDebut(), false);
                Date fin = Util.buildTimeStamp(dateDebut, tranche.getHeureFin(), false);
                if (fin.before(debut)) {
                    Calendar date = Calendar.getInstance();
                    date.setTime(dateFin);
                    date.add(Calendar.DATE, 1);
                    dateFin = date.getTime();
                }
                planning.setDateFin(dateFin);
                planning.setEmploye(employe);
                planning.setTranche(tranche);
                planning.setHeureDebut(tranche.getHeureDebut());
                planning.setHeureFin(tranche.getHeureFin());
                planning.setRepos(false);
                dao.save(planning);
                if (msg) {
                    succes();
                }
            }
        } catch (Exception ex) {
            if (msg) {
                getErrorMessage("Action impossible");
            }
            getException("buildPlanning", ex);
        }
    }
}
