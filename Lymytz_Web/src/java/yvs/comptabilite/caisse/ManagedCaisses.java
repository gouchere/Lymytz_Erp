/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.caisse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.Comptes;
import yvs.base.compta.Journaux;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.ManagedJournaux;
import yvs.base.compta.ModeDeReglement;
import yvs.base.compta.UtilCompta;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseCaisseUser;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBaseComptesCaisse;
import yvs.entity.compta.YvsBaseLiaisonCaisse;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsers;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.bean.Employe;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.parametrage.ManagedNatureCompte;
import yvs.users.ManagedUser;
import yvs.users.Users;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedCaisses extends Managed<Caisses, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{caisses}")
    private Caisses caisse;
    private List<YvsBaseCaisse> caisses, allCaisses;
    private List<YvsBaseCaisse> banques, internes;
    private YvsBaseCaisse selectCaisse = new YvsBaseCaisse();
    private Caisses caisseLie = new Caisses();
    private Comptes compteLie = new Comptes();
    private Date dateDebut = new Date(), dateFin = new Date();
    private Date dateSolde = new Date();

    private Comptes otherCompte = new Comptes();

    private boolean disableJournal, disableCompte;

    private Long agenceSearch, journalSearch;
    private String numeroSearch, typeSearch;
    private Boolean principalSearch, actifSearch;
    private String tabIds, fusionneTo;
    private List<String> fusionnesBy;

    private List<YvsUsers> users;
    private long agenceFind;   

    public ManagedCaisses() {
        caisses = new ArrayList<>();
        allCaisses = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
        banques = new ArrayList<>();
        internes = new ArrayList<>();
        users = new ArrayList<>();
    }

    public Long getAgenceSearch() {
        return agenceSearch;
    }

    public void setAgenceSearch(Long agenceSearch) {
        this.agenceSearch = agenceSearch;
    }

    public Long getJournalSearch() {
        return journalSearch;
    }

    public void setJournalSearch(Long journalSearch) {
        this.journalSearch = journalSearch;
    }

    public long getAgenceFind() {
        return agenceFind;
    }

    public void setAgenceFind(long agenceFind) {
        this.agenceFind = agenceFind;
    }

    public boolean isDisableJournal() {
        return disableJournal;
    }

    public void setDisableJournal(boolean disableJournal) {
        this.disableJournal = disableJournal;
    }

    public boolean isDisableCompte() {
        return disableCompte;
    }

    public void setDisableCompte(boolean disableCompte) {
        this.disableCompte = disableCompte;
    }

    public List<YvsBaseCaisse> getAllCaisses() {
        return allCaisses;
    }

    public void setAllCaisses(List<YvsBaseCaisse> allCaisses) {
        this.allCaisses = allCaisses;
    }

    public Comptes getCompteLie() {
        return compteLie;
    }

    public void setCompteLie(Comptes compteLie) {
        this.compteLie = compteLie;
    }

    public List<YvsBaseCaisse> getBanques() {
        return banques;
    }

    public void setBanques(List<YvsBaseCaisse> banques) {
        this.banques = banques;
    }

    public List<YvsBaseCaisse> getInternes() {
        return internes;
    }

    public void setInternes(List<YvsBaseCaisse> internes) {
        this.internes = internes;
    }

    public Date getDateSolde() {
        return dateSolde;
    }

    public void setDateSolde(Date dateSolde) {
        this.dateSolde = dateSolde;
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

    public String getNumeroSearch() {
        return numeroSearch;
    }

    public void setNumeroSearch(String numeroSearch) {
        this.numeroSearch = numeroSearch;
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public Boolean getPrincipalSearch() {
        return principalSearch;
    }

    public void setPrincipalSearch(Boolean principalSearch) {
        this.principalSearch = principalSearch;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public String getSourceChoixCompte() {
        return sourceChoixCompte;
    }

    public void setSourceChoixCompte(String sourceChoixCompte) {
        this.sourceChoixCompte = sourceChoixCompte;
    }

    public YvsBaseCaisse getSelectCaisse() {
        return selectCaisse;
    }

    public void setSelectCaisse(YvsBaseCaisse selectCaisse) {
        this.selectCaisse = selectCaisse;
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

    public Caisses getCaisse() {
        return caisse;
    }

    public void setCaisse(Caisses caisse) {
        this.caisse = caisse;
    }

    public List<YvsBaseCaisse> getCaisses() {
        return caisses;
    }

    public void setCaisses(List<YvsBaseCaisse> caisses) {
        this.caisses = caisses;
    }

    public Caisses getCaisseLie() {
        return caisseLie;
    }

    public void setCaisseLie(Caisses caisseLie) {
        this.caisseLie = caisseLie;
    }

    public Comptes getOtherCompte() {
        return otherCompte;
    }

    public void setOtherCompte(Comptes otherCompte) {
        this.otherCompte = otherCompte;
    }

    public List<YvsUsers> getUsers() {
        return users;
    }

    public void setUsers(List<YvsUsers> users) {
        this.users = users;
    }

    @Override
    public boolean controleFiche(Caisses bean) {
        if (bean.getCode() != null ? bean.getCode().trim().length() < 1 : true) {
            getErrorMessage("Vous devez préciser le code !");
            return false;
        }
        if (bean.getIntitule() != null ? bean.getIntitule().trim().length() < 1 : true) {
            getErrorMessage("Vous devez préciser l'intitulé !");
            return false;
        }
        if (bean.getJournal().getId() <= 0) {
            getErrorMessage("Veuillez indiquer un journal");
            return false;
        }
        if (bean.getParent() != null) {
            if ((bean.getParent().getId() == bean.getId()) && bean.getId() > 0) {
                getErrorMessage("Erreur de référence cyclique entre les caisses !");
                return false;
            }
        }
//        //caisse par défaut
        if (bean.isDefaultCaisse()) {
            YvsBaseCaisse c = (YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findDefault", new String[]{"agence"}, new Object[]{new YvsAgences(bean.getJournal().getAgence().getId())});
            if (c != null ? (c.getId() != bean.getId()) : false) {
                getErrorMessage("Vous ne pouvez définir une autre caisse par défaut");
                return false;
            }
        }
        YvsBaseCaisse a = (YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findByCode", new String[]{"code"}, new Object[]{bean.getCode()});
        if (a != null ? (a.getId() > 0 ? !a.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Cette caisse existe déjà, veuillez entrer un autre code");
            return false;

        }
        return true;
    }

    @Override
    public void deleteBean() {
        System.err.println("");
        if (selectCaisse != null ? selectCaisse.getId() > 0 : false) {
            try {
                dao.delete(selectCaisse);
                caisses.remove(selectCaisse);
                if (caisse.getId() > 0) {
                    for (int i = 0; i < caisse.getSubCaisses().size(); i++) {
                        if (caisse.getSubCaisses().get(i).getCaisseCible().equals(selectCaisse)) {
                            caisse.getSubCaisses().remove(i);
                            break;
                        }
                    }
                }
                resetFiche();
                update("table_subcaisses");
                update("table_caisses");
                succes();
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cette caisse !");
            }
        }
    }

    public void deleteBean_() {
        try {
            System.err.println("tabIds = " + tabIds);
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsBaseCaisse> list = new ArrayList<>();
                YvsBaseCaisse bean;
                for (Long ids : l) {
                    long id = Long.valueOf(ids);
                    bean = caisses.get(ids.intValue());
                    list.add(bean);
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    dao.delete(bean);
                    if (bean.getId() == caisse.getId()) {
                        resetFiche();
                    }
                    succes();
                    tabIds = "";
                    update("table_caisses");
                }
            }

        } catch (Exception e) {
            getErrorMessage("Impossible de supprimer cette caisse !");
        }

    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Caisses recopieView() {
        Caisses ca = new Caisses();
        cloneObject(ca, caisse);
        return ca;
    }

    @Override
    public void populateView(Caisses bean) {
        bean.setRecette(dao.getRecetteCaisse(bean.getId(), null, Constantes.STATUT_DOC_PAYER));
        bean.setDepense(dao.getDepenseCaisse(bean.getId(), null, Constantes.STATUT_DOC_PAYER));
        bean.setSolde(bean.getRecette() - bean.getDepense());
        bean.setSoldeMission(dao.getSoldeCaisse(bean.getId(), Constantes.SCR_MISSIONS, Constantes.STATUT_DOC_PAYER));
        bean.setSoldeAchat(dao.getSoldeCaisse(bean.getId(), Constantes.SCR_ACHAT, Constantes.STATUT_DOC_PAYER));
        bean.setSoldeVente(dao.getSoldeCaisse(bean.getId(), Constantes.SCR_VENTE, Constantes.STATUT_DOC_PAYER));
        bean.setSoldeDivers(dao.getSoldeCaisse(bean.getId(), Constantes.SCR_DIVERS, Constantes.STATUT_DOC_PAYER));
        bean.setSoldeVirement(dao.getSoldeCaisse(bean.getId(), Constantes.SCR_VIREMENT, Constantes.STATUT_DOC_PAYER));
        bean.setSoldeOther(dao.getSoldeCaisse(bean.getId(), Constantes.SCR_AUTRES, Constantes.STATUT_DOC_PAYER));
        bean.setSoldeNote(dao.getSoldeCaisse(bean.getId(), Constantes.SCR_NOTE_FRAIS, Constantes.STATUT_DOC_PAYER));
        cloneObject(caisse, bean);
        update("btnSoldeCaisse");
    }

    @Override
    public void onSelectObject(YvsBaseCaisse y) {
        selectCaisse = y;
        populateView(UtilCompta.buildBeanCaisse(y));
        //charge les user
        users = dao.loadNameQueries("YvsUsers.findAlls", new String[]{"societe", "actif"}, new Object[]{currentAgence.getSociete(), true});
        YvsBaseCaisseUser cu;
        for (YvsUsers u : users) {
            cu = (YvsBaseCaisseUser) dao.loadOneByNameQueries("YvsBaseCaisseUser.findOne", new String[]{"caisse", "user"}, new Object[]{y, u});
            if (cu != null ? cu.getId() > 0 : false) {
                u.setCaisse(y);
                u.setNew_(cu.getActif());
            }
        }
        update("form_edit_caisses");
    }

    public void addLiaisonCaisseUser(YvsUsers user) {
        YvsBaseCaisseUser c = (YvsBaseCaisseUser) dao.loadOneByNameQueries("YvsBaseCaisseUser.findOne", new String[]{"caisse", "user"}, new Object[]{selectCaisse, user});
        if (c == null) {
            c = new YvsBaseCaisseUser();
            c.setActif(true);
            c.setAuthor(currentUser);
            c.setDateSave(new Date());
            c.setDateUpdate(new Date());
            c.setIdCaisse(selectCaisse);
            c.setIdUser(user);
            dao.save1(c);
            user.setCaisse(selectCaisse);
        } else {
            c.setDateUpdate(new Date());
            c.setActif(!c.getActif());
            dao.update(c);
        }
        user.setNew_(c.getActif());
    }

    public List<YvsUsers> loadCaissiers(YvsBaseCaisse y) {
        List<YvsUsers> caissiers = new ArrayList<>();
        if (y != null ? y.getId() > 0 : false) {
            caissiers = dao.loadNameQueries("YvsBaseUsersAcces.findUsersByCode", new String[]{"code"}, new Object[]{y.getCodeAcces()});
            if (caissiers == null) {
                caissiers = new ArrayList<>();
            }
            if (y.getResponsable() != null ? y.getResponsable().getCodeUsers() != null : false) {
                if (!caissiers.contains(y.getResponsable().getCodeUsers())) {
                    caissiers.add(y.getResponsable().getCodeUsers());
                }
            }
            if (y.getCaissier() != null) {
                if (!caissiers.contains(y.getCaissier())) {
                    caissiers.add(y.getCaissier());
                }
            }
        }
        return caissiers;
    }

    public List<YvsBaseCaisse> loadCaisses(YvsUsers y) {
        List<YvsBaseCaisse> list = new ArrayList<>();
        if (y != null ? y.getId() > 0 : false) {
            List<Long> codes = dao.loadNameQueries("YvsBaseUsersAcces.findIdAccesByUsers", new String[]{"users", "societe"}, new Object[]{currentUser.getUsers(), currentAgence.getSociete()});
            if (codes != null ? codes.isEmpty() : true) {
                codes.add(-1L);
            }
            List<Long> ids = dao.loadNameQueries("YvsBaseCaisseUser.findIdCaisseByUser", new String[]{"user"}, new Object[]{currentUser.getUsers()});
            if (ids != null ? ids.isEmpty() : true) {
                ids.add(-1L);
            }
            nameQueri = "YvsBaseCaisse.findAllForUsers";
            champ = new String[]{"codes", "ids", "caissier", "responsable"};
            val = new Object[]{codes, ids, y, (y.getEmploye() != null ? y.getEmploye().getId() : 0)};
            list = dao.loadNameQueries(nameQueri, champ, val);
        }
        return list;
    }

    public List<Long> loadIdCaisses(YvsUsers y) {
        List<Long> list = new ArrayList<>();
        if (y != null ? y.getId() > 0 : false) {
            List<Long> codes = dao.loadNameQueries("YvsBaseUsersAcces.findIdAccesByUsers", new String[]{"users", "societe"}, new Object[]{currentUser.getUsers(), currentAgence.getSociete()});
            if (codes != null ? codes.isEmpty() : true) {
                codes.add(-1L);
            }
            List<Long> ids = dao.loadNameQueries("YvsBaseCaisseUser.findIdCaisseByUser", new String[]{"user"}, new Object[]{currentUser.getUsers()});
            if (ids != null ? ids.isEmpty() : true) {
                ids.add(-1L);
            }
            nameQueri = "YvsBaseCaisse.findIdAllForUsers";
            champ = new String[]{"codes", "ids", "caissier", "responsable"};
            val = new Object[]{codes, ids, y, (y.getEmploye() != null ? y.getEmploye().getId() : 0)};
            list = dao.loadNameQueries(nameQueri, champ, val);
        }
        return list;
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        //selectionne une ligne de caisse
        YvsBaseCaisse y = (YvsBaseCaisse) ev.getObject();
        onSelectObject(y);
        execute("collapseForm('caisses')");
        update("table_caisses");
        tabIds = caisses.indexOf(y) + "";

    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void addParamInit() {
        if (!autoriser("caiss_view_all")) {
            List<Long> ids = dao.loadNameQueries("YvsBaseUsersAcces.findIdAccesByUsers", new String[]{"users", "societe"}, new Object[]{currentUser.getUsers(), currentAgence.getSociete()});
            if (ids != null ? ids.isEmpty() : true) {
                ids.add(-1L);
            }
            System.err.println("ids :" + ids);
            List<Long> idCaisse = dao.loadNameQueries("YvsBaseCaisseUser.findIdCaisseByUser", new String[]{"user"}, new Object[]{currentUser.getUsers()});
            if (idCaisse != null ? idCaisse.isEmpty() : true) {
                idCaisse.add(-1L);
            }
            System.err.println("idCaisse :" + idCaisse);
            ParametreRequete p = new ParametreRequete(null, "responsable", currentUser.getUsers(), "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.caissier", "caissier", currentUser.getUsers(), "=", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.codeAcces.id", "codeAcces", ids, "IN", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.id", "idCaisse", idCaisse, "IN", "OR"));
            if (currentUser.getUsers().getEmploye() != null) {
                p.getOtherExpression().add(new ParametreRequete("y.responsable", "responsable", currentUser.getUsers().getEmploye(), "=", "OR"));
            }
            paginator.addParam(p);
        }
        ParametreRequete p = new ParametreRequete("y.journal.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
        paginator.addParam(p);
    }
    boolean initForm = true;

    public void init() {
        if (caisse.getParent() == null) {
            caisse.setParent(new Caisses());
        }
    }

    @Override
    public void loadAll() {
        YvsBaseExercice exo = giveExerciceActif(new Date());
        if (exo != null ? exo.getId() > 0 : false) {
            dateDebut = exo.getDateDebut();
            dateFin = exo.getDateFin();
        }
        loadAll(true);
    }

    public void loadAll(boolean next) {
        imax = 100;
        loadAll(next, imax);
    }

    public void loadAll(boolean next, long imax) {
        //charge les caisses
        init();
        addParamInit();
        String query = "YvsBaseCaisse y JOIN FETCH y.journal LEFT JOIN FETCH y.caissier LEFT JOIN FETCH y.author.users";
        caisses = paginator.executeDynamicQuery("y", "y", query, "y.defaultCaisse DESC, y.intitule", next, initForm, (int) imax, dao);
    }

    public void paginer(boolean next) {
        initForm = false;
        loadAll(next);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        String query = "YvsBaseCaisse y LEFT JOIN FETCH y.caissier "
                + "LEFT JOIN FETCH y.responsable "
                + "LEFT JOIN FETCH y.codeAcces "
                + "JOIN FETCH y.author.users";
        List<YvsBaseCaisse> re = paginator.parcoursDynamicData(query, "y", "y.defaultCaisse DESC, y.intitule", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        imax = (long) ev.getNewValue();
        initForm = true;
        loadAll(true);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        initForm = true;
        loadAll(true);
    }

    public void loadAllActif() {
        caisses = dao.loadNameQueries("YvsBaseCaisse.findAllActif", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllActifP() {
        caisses = dao.loadNameQueries("YvsBaseCaisse.findAllPrincipalActif", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllCaisses() {
        allCaisses = dao.loadNameQueries("YvsBaseCaisse.findAllActif", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllCaisseActif(Boolean actif) {
        actifSearch = actif;
        addParamActif();
    }

    public void onSelectAgenceForAction(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsAgences a = (YvsAgences) ev.getObject();
            agenceFind = a.getId();
            loadCaissesByAgence(agenceFind);
        }
    }

    public void loadCaissesByAgence() {
        if (agenceFind < 1) {
            agenceFind = currentAgence.getId();
        }
        loadCaissesByAgence(agenceFind);
    }

    public void loadCaissesByAgence(long agence) {
        try {
            if (autoriser("caiss_view_all")) {
                caisses = dao.loadNameQueries("YvsBaseCaisse.findByAgence", new String[]{"agence"}, new Object[]{new YvsAgences(agence)});
            } else {
                caisses = dao.loadNameQueries("YvsBaseCaisseUser.findCaisseByUsersAgence", new String[]{"agence", "users"}, new Object[]{new YvsAgences(agence), currentUser.getUsers()});
                List<Long> acces = dao.loadNameQueries("YvsBaseUsersAcces.findIdAccesByUsers", new String[]{"users", "societe"}, new Object[]{currentUser.getUsers(), currentAgence.getSociete()});
                if (acces.isEmpty()) {
                    acces.add(-1L);
                }
                List<YvsBaseCaisse> list = dao.loadNameQueries("YvsBaseCaisse.findByAccesAgenceCaissier", new String[]{"agence", "acces", "caissier"}, new Object[]{new YvsAgences(agence), acces, currentUser.getUsers()});
                for (YvsBaseCaisse p : list) {
                    if (!caisses.contains(p)) {
                        caisses.add(p);
                    }
                }
            }
        } catch (Exception ex) {
            getException("loadCaissesByAgence", ex);
        }
    }

    public YvsBaseCaisse findOneCaisse(long id) {
        return (YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{id});
    }

    @Override
    public boolean saveNew() {
        YvsBaseCaisse caiss = saveNew(caisse);
        if (caiss != null ? caiss.getId() > 0 : false) {
            succes();
            actionOpenOrResetAfter(this);
            return true;
        }
        return false;
    }

    public YvsBaseCaisse saveNew(Caisses bean) {
        YvsBaseCaisse caiss = null;
        if (controleFiche(bean)) {
            caiss = UtilCompta.buildBeanCaisse(bean);
            if (caiss != null) {
                if (bean.getCodeAcces() != null ? bean.getCodeAcces().trim().length() > 0 : false) {
                    caiss.setCodeAcces(returnCodeAcces(bean.getCodeAcces()));
                } else {
                    caiss.setCodeAcces(null);
                }
                if (caiss.getId() <= 0) {
                    caiss.setAuthor(currentUser);
                    caiss.setId(null);
                    caiss = (YvsBaseCaisse) dao.save1(caiss);
                    bean.setId(caiss.getId());
                    caisses.add(0, caiss);
                } else {
                    caiss.setAuthor(currentUser);
                    dao.update(caiss);
                    if (caisses.contains(caiss)) {
                        caisses.set(caisses.indexOf(caiss), caiss);
                    }
                }
            }
        }
        return caiss;
    }

    @Override
    public void resetFiche() {
        resetFiche(caisse);
        caisse.setType("CAISSE");
        caisse.setParent(new Caisses());
        caisse.setCompte(new Comptes());
        caisse.setResponsable(new Employe());
        caisse.setJournal(new Journaux());
        caisse.setActif(true);
        caisse.setPrincipal(false);
        caisse.setVenteOnline(false);
        caisse.setModeRegDefaut(new ModeDeReglement());
        caisse.getOthersComptes().clear();
        caisse.getSubCaisses().clear();
        caisse.setCaissier(new Users());
        activeModeWizard(false);
    }
    /**/

    private String sourceChoixCompte;   //OC ou CG

    /**
     *
     * @param numCompte
     * @param source: peut être CG, lorqu'on veux modifier le compte Générale ou
     * OC l'orsqu'on veut modifier un autre compte
     */
    public void ecouteSaisieCG(String numCompte, String source) {
        sourceChoixCompte = source;
        //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
        ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (service != null) {
            service.findCompteByNum(numCompte);
            if (service.getListComptes() != null) {
                if (!service.getListComptes().isEmpty()) {
                    if (service.getListComptes().size() == 1) {
                        if (source.equals("CG")) {
                            caisse.getCompte().setError(false);
                            cloneObject(caisse.getCompte(), UtilCompta.buildBeanCompte(service.getListComptes().get(0)));
                        } else if (source.equals("CL")) {
                            compteLie.setError(false);
                            cloneObject(compteLie, UtilCompta.buildBeanCompte(service.getListComptes().get(0)));
                        } else {
                            otherCompte.setError(false);
                            cloneObject(otherCompte, UtilCompta.buildBeanCompte(service.getListComptes().get(0)));
                        }
                    } else {
                        if (source.equals("CG")) {
                            caisse.getCompte().setError(true);
                        } else if (source.equals("CL")) {
                            compteLie.setError(true);
                        } else {
                            otherCompte.setError(true);
                        }
                        service.setManagedBean(this);
                        openDialog("dlgCmpteG");
                        update("table_cptG_caiss");
                        update("table_cptG_caiss_");
                    }
                } else {
                    caisse.getCompte().setError(true);
                }
            } else {
                caisse.getCompte().setError(true);
            }
        }

    }

    public void openAllCompte(String source) {
        sourceChoixCompte = source;
        ManagedCompte w = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (w != null) {
            w.setManagedBean(this);
            w.resetFiche();
        }
        ManagedNatureCompte s = (ManagedNatureCompte) giveManagedBean(ManagedNatureCompte.class);
        if (s != null) {
            s.loadAll(true, true);
        }
        openDialog("dlgCmpteG");
        update("form_edit_compte");
    }

    public void choisirCompte(SelectEvent ev) {
        if (ev != null) {
            onSelectCompte((YvsBasePlanComptable) ev.getObject());
        }
    }

    @Override
    public void onSelectDistant(YvsBaseCaisse y) {
        Navigations n = (Navigations) giveManagedBean(Navigations.class);
        if (n != null) {
            n.naviguationView("Caisses", "modDonneBase", "smenCaisses", true);
        }
        onSelectObject(y);
    }

    public void onSelectCompte(YvsBasePlanComptable y) {
        if (y != null) {
            if (sourceChoixCompte.equals("CG")) {
                cloneObject(caisse.getCompte(), UtilCompta.buildSimpleBeanCompte(y));
            } else if (sourceChoixCompte.equals("CL")) {
                cloneObject(compteLie, UtilCompta.buildSimpleBeanCompte(y));
                update("tabview_others_info_caiss:chmp_compteLie_Caisse");
            } else {
                cloneObject(otherCompte, UtilCompta.buildSimpleBeanCompte(y));
            }
            closeDialog("dlgCmpteG");
            update("chmp_compteGen_Caisse");
        }
    }

    public void ecouteSaisieEmps() {
        //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
        ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
        if (service != null) {
            service.findEmploye(caisse.getResponsable().getMatricule());
            caisse.getResponsable().setError(service.getListEmployes().isEmpty());
            if (service.getListEmployes() != null) {
                if (!service.getListEmployes().isEmpty()) {
                    if (service.getListEmployes().size() == 1) {
                        caisse.getResponsable().setError(false);
                        YvsGrhEmployes y = service.getListEmployes().get(0);
                        cloneObject(caisse.getResponsable(), UtilGrh.buildBeanSimplePartialEmploye(y));
                        if (caisse.getCaissier() != null ? caisse.getCaissier().getId() < 1 : true) {
                            cloneObject(caisse.getCaissier(), UtilUsers.buildBeanUsers(y.getCodeUsers()));
                        }
                    } else {
                        caisse.getResponsable().setError(false);
                        openDialog("dlgEmploye");
                        update("tabEmployes_caisses");
                    }
                } else {
                    caisse.getResponsable().setError(true);
                }
            } else {
                caisse.getResponsable().setError(true);
            }
        }

    }

    public void chooseJournal() {
        if (caisse.getJournal() != null) {
            if (caisse.getJournal().getId() > 0) {
                ManagedJournaux w = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                if (w != null) {
                    int idx = w.getJournaux().indexOf(new YvsComptaJournaux(caisse.getJournal().getId()));
                    if (idx > -1) {
                        YvsComptaJournaux y = w.getJournaux().get(idx);
                        caisse.setJournal(UtilCompta.buildBeanJournaux(y));
                    }
                }
            } else if (caisse.getJournal().getId() == -1) {
                openDialog("dlgCreateJournal");
                caisse.getJournal().setId(0);
            }
        }
    }

    public void chooseCaissier() {
        if (caisse.getCaissier() != null ? caisse.getCaissier().getId() > 0 : false) {
            ManagedUser w = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (w != null) {
                int idx = w.getListAllUser().indexOf(new YvsUsers(caisse.getCaissier().getId()));
                if (idx > -1) {
                    YvsUsers y = w.getListAllUser().get(idx);
                    caisse.setCaissier(UtilUsers.buildSimpleBeanUsers(y));
                }
            }
        }
    }

    public void chooseLiaisonCaisse(SelectEvent ev) {
        if (ev != null) {
            YvsBaseLiaisonCaisse c = (YvsBaseLiaisonCaisse) ev.getObject();
            if (c != null) {
                caisseLie.setIdLiaison(c.getId());
                caisseLie.setId(c.getCaisseCible().getId());
                compteLie = UtilCompta.buildBeanCompte(c.getCompte());
                caisseLie.setDateSave(c.getDateSave());
            }
        }
    }

    public void addLiaisonCaisse() {
        if (caisseLie.getId() > 0 && caisse.getId() > 0) {
            if (caisseLie.getId() != caisse.getId()) {
//                boolean continu = true;
//                for (YvsBaseLiaisonCaisse c : caisse.getSubCaisses()) {
//                    if (c.getCaisseCible().getId().equals(caisseLie.getId())) {
//                        continu = false;
//                    }
//                }
                YvsBaseLiaisonCaisse bl = new YvsBaseLiaisonCaisse(caisseLie.getIdLiaison());
                bl.setActif(true);
                bl.setAuthor(currentUser);
                YvsBaseCaisse c1 = new YvsBaseCaisse(caisseLie.getId());
                int index = allCaisses.indexOf(c1);
                if (index > -1) {
                    c1 = allCaisses.get(index);
                }
                bl.setCaisseCible(c1);
                YvsBaseCaisse c2 = new YvsBaseCaisse(caisse.getId());
                index = allCaisses.indexOf(c2);
                if (index > -1) {
                    c2 = allCaisses.get(index);
                }
                bl.setCaisseSource(c2);
                bl.setDateSave(caisseLie.getDateSave());
                bl.setDateUpdate(new Date());
                if (compteLie != null ? compteLie.getId() > 0 : false) {
                    bl.setCompte(UtilCompta.buildEntityCompte(compteLie));
                }
                if (caisseLie.getIdLiaison() <= 0) {
                    bl = (YvsBaseLiaisonCaisse) dao.save1(bl);
                    caisse.getSubCaisses().add(0, bl);
                } else {
                    dao.update(bl);
                    int idx = caisse.getSubCaisses().indexOf(bl);
                    if (idx >= 0) {
                        caisse.getSubCaisses().set(idx, bl);
                    }
                }
                compteLie = new Comptes();
            } else {
                getErrorMessage("Laison cyclique non autorisé");
            }
        } else {
            getErrorMessage("Formulaire incorrecte !");
        }
    }

    public void chooseType() {
        update("tabview_others_info_caiss");
    }

    public void choisirEmploye(SelectEvent ev) {
        if (ev != null) {
            cloneObject(caisse.getResponsable(), UtilGrh.buildBeanSimplePartialEmploye((YvsGrhEmployes) ev.getObject()));
            update("chmp_responsable_Caisse");
            update("chmp_caissier_Caisse");
        }
    }

    public void toogleActiveCaisse(YvsBaseCaisse bc) {
        if (bc != null) {
            bc.setActif(!bc.getActif());
            bc.setAuthor(currentUser);
            dao.update(bc);
        }
    }

    public void deleteLiaison(YvsBaseLiaisonCaisse y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                dao.delete(y);
                caisse.getSubCaisses().remove(y);
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
        }
    }

    public void openToDelete(YvsBaseCaisse bc) {
        selectCaisse = bc;
        openDialog("dlgConfirmDelCaiss");
    }

    public void saveOthersComptes() {
        if (caisse.getId() > 0) {
            if (otherCompte.getId() > 0) {
                YvsBaseComptesCaisse cc = new YvsBaseComptesCaisse();
                cc.setAuthor(currentUser);
                cc.setCaisse(new YvsBaseCaisse(caisse.getId()));
                cc.setCodeComptable(otherCompte.getCodeComptable());
                cc.setCompteGeneral(UtilCompta.buildEntityCompte(otherCompte));
                cc.setId((otherCompte.getIdCompteCaisse() > 0) ? otherCompte.getIdCompteCaisse() : null);
                if (otherCompte.getIdCompteCaisse() > 0) {
                    dao.update(cc);
                } else {
                    cc = (YvsBaseComptesCaisse) dao.save1(cc);
                }
                int idx = indexOf(caisse.getOthersComptes(), otherCompte.getIdCompteCaisse());
                if (idx >= 0) {
                    caisse.getOthersComptes().set(idx, cc);
                } else {
                    cc.setNew_(true);
                    caisse.getOthersComptes().add(0, cc);
                }
                otherCompte = new Comptes(-1);
                update("tabview_others_info_caiss:form_zone_add_others_compte");
            } else {
                getErrorMessage("Vous devez entrer un compte");
            }
        } //        else if (!caisse.getType().equals("BANQUE")) {
        //            getErrorMessage("L'enregistrement de compte supplémentaire n'est possible que pour de caisses de type banques !");
        //        } 
        else {
            getErrorMessage("Aucune caisse n'a été trouvé !");
        }
    }

    public void selectLineOthersCompte(SelectEvent ev) {
        if (ev != null) {
            YvsBaseComptesCaisse cc = (YvsBaseComptesCaisse) ev.getObject();
            cloneObject(otherCompte, UtilCompta.buildSimpleBeanCompte(cc.getCompteGeneral()));
            otherCompte.setCodeComptable(cc.getCodeComptable());
            otherCompte.setIdCompteCaisse(cc.getId());
            update("tabview_others_info_caiss:form_zone_add_others_compte");
        }
    }

    private int indexOf(List<YvsBaseComptesCaisse> l, long id) {
        int r = 0;
        for (YvsBaseComptesCaisse c : l) {
            if (c.getId() == id) {
                return r;
            }
            r++;
        }
        return -1;
    }

    public void deleteOneCompteCaisse(YvsBaseComptesCaisse cc) {
        cloneObject(otherCompte, UtilCompta.buildSimpleBeanCompte(cc.getCompteGeneral()));
        otherCompte.setCodeComptable(cc.getCodeComptable());
        otherCompte.setIdCompteCaisse(cc.getId());
        openDialog("dlgConfirmDelCompteC");
        update("tabview_others_info_caiss:form_zone_add_others_compte");
    }

    public void confirmDeleteCompteCaiss() {
        if (otherCompte.getId() > 0) {
            try {
                YvsBaseComptesCaisse cc = new YvsBaseComptesCaisse(otherCompte.getIdCompteCaisse());
                cc.setAuthor(currentUser);
                dao.delete(cc);
                caisse.getOthersComptes().remove(cc);
                otherCompte = new Comptes();
                update(":main_form_caisses:tabview_others_info_caiss:chmp_compteGen_Caisse_");
                update(":tabview_others_info_caiss:form_zone_add_others_compte");
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cette ligne !");
                getException("Lymytz Error>>>>", ex);
            }
        }
    }

    public void changeActifAll(boolean actif) {
        try {
            YvsBaseCaisse y;
            List<Integer> ids = decomposeSelection(tabIds);
            boolean succes = false;
            for (int i : ids) {
                y = caisses.get(i);
                y.setActif(actif);
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                dao.update(y);
                caisses.set(i, y);
                succes = true;
            }
            if (succes) {
                succes();
                update("table_caisses");
            }
        } catch (Exception ex) {
            getException("changeActifAll", ex);
        }
    }

    public void fusionner(boolean fusionne) {
        try {
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty() ? ids.size() > 1 : false) {
                long newValue = caisses.get(ids.get(0)).getId();
                if (!fusionne) {
                    if (!autoriser("base_user_fusion")) {
                        openNotAcces();
                        return;
                    }
                    String oldValue = "0";
                    for (int i : ids) {
                        if (caisses.get(i).getId() != newValue) {
                            oldValue += "," + caisses.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_base_caisse", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                caisses.remove(new YvsBaseCaisse(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = caisses.get(idx).getIntitule();
                    } else {
                        YvsBaseCaisse c = (YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                            fusionneTo = c.getIntitule();
                        }
                    }
                    YvsBaseCaisse c;
                    for (int i : ids) {
                        long oldValue = caisses.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(caisses.get(i).getIntitule());
                            }
                        } else {
                            c = (YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                                fusionnesBy.add(c.getIntitule());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 caisses");
            }
        } catch (NumberFormatException ex) {
        }
    }

    public void printBrouillard() {
        printBrouillard(selectCaisse, getDateDebut(), getDateFin());
    }

    public void printBrouillard(String type) {
        printBrouillard(selectCaisse, getDateDebut(), getDateFin(), type);
    }

    public void printBrouillard(YvsBaseCaisse selectCaisse, Date dateDebut, Date dateFin) {
        printBrouillard(selectCaisse, dateDebut, dateFin, "defaut");
    }

    public void printBrouillard(YvsBaseCaisse selectCaisse, Date dateDebut, Date dateFin, String type) {
        if (selectCaisse != null ? selectCaisse.getId() > 0 : false) {
            Map<String, Object> param = new HashMap<>();
            param.put("CAISSE", selectCaisse.getId().intValue());
            param.put("DATE_DEBUT", dateDebut);
            param.put("DATE_FIN", dateFin);
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("AGENCE", currentAgence.getId().intValue());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR);
            String report = "brouillard_caisse";
            if (type.equals("2_colonne")) {
                report = "brouillard_caisse_2_colonne";
            }
            executeReport(report, param);
        }
    }

    public void addParamAgence() {
        ParametreRequete p = new ParametreRequete("y.journal.agence", "agence", null, "=", "AND");
        if (agenceSearch != null ? agenceSearch > 0 : false) {
            p = new ParametreRequete("y.journal.agence", "agence", new YvsAgences(agenceSearch), "=", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAll(true);
    }

    public void addParamJournal() {
        ParametreRequete p = new ParametreRequete("y.journal", "journal", null, "=", "AND");
        if (journalSearch != null ? journalSearch > 0 : false) {
            p = new ParametreRequete("y.journal", "journal", new YvsComptaJournaux(journalSearch), "=", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAll(true);
    }

    public void addParamNumero() {
        ParametreRequete p = new ParametreRequete("y.code", "code", null, "=", "AND");
        if (numeroSearch != null ? numeroSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "code", numeroSearch + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.code", "code", numeroSearch + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.intitule", "code", numeroSearch + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        initForm = true;
        loadAll(true);
    }

    public void addParamType() {
        addParamType_(typeSearch);
    }

    public void addParamType_(String typeSearch) {
        ParametreRequete p = new ParametreRequete("y.typeCaisse", "typeCaisse", null, "=", "AND");
        if (typeSearch != null ? typeSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.typeCaisse", "typeCaisse", typeSearch + "%", "LIKE", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAll(true);
    }

    public void loadSoldeCaisse(boolean avance, boolean init) {
        loadSoldeCaisse(avance, init, null);
    }

    public void loadSoldeCaisse(boolean avance, boolean init, Boolean caisse) {
        initForm = init;
        if (caisse == null) {
            loadAll(avance);
        } else {
            addParamType_(caisse ? "CAISSE" : "BANQUE");
        }
        internes.clear();
        banques.clear();
        YvsBaseModeReglement m;
        for (YvsBaseCaisse c : caisses) {
            if (c.getTypeCaisse().equals("BANQUE")) {
                c.setSolde(dao.getTotalCaisse(currentAgence.getSociete().getId(), c.getId(), 0, "", "", Constantes.STATUT_DOC_PAYER, dateSolde));
                banques.add(c);
            } else {
                c.setModes(new ArrayList<YvsBaseModeReglement>());
                //SOLDE ESPECE
                m = new YvsBaseModeReglement(0l, Constantes.MODE_PAIEMENT_ESPECE, Constantes.MODE_PAIEMENT_ESPECE);
                m.setSolde(dao.getTotalCaisse(currentAgence.getSociete().getId(), c.getId(), 0, "", Constantes.MODE_PAIEMENT_ESPECE, Constantes.STATUT_DOC_PAYER, dateSolde));
                c.getModes().add(m);
                //SOLDE CHEQUE
                m = new YvsBaseModeReglement(0l, Constantes.MODE_PAIEMENT_BANQUE, Constantes.MODE_PAIEMENT_BANQUE);
                m.setSolde(dao.getTotalCaisse(currentAgence.getSociete().getId(), c.getId(), 0, "", Constantes.MODE_PAIEMENT_BANQUE, Constantes.STATUT_DOC_PAYER, dateSolde));
                c.getModes().add(m);
                internes.add(c);
            }
        }
    }

    public void addParamPrincipal() {
        ParametreRequete p = new ParametreRequete("y.principal", "principal", principalSearch, "=", "AND");
        paginator.addParam(p);
        initForm = true;
        loadAll(true);
    }

    public void addParamActif() {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND");
        paginator.addParam(p);
        initForm = true;
        loadAll(true);
    }

    public void choosePaginatorSolde(ValueChangeEvent ev) {
        imax = (long) ev.getNewValue();
        loadSoldeCaisse(true, true);
    }

    public void activeDefaut(YvsBaseCaisse bean) {
        if (bean != null) {
            if (!bean.getDefaultCaisse()) {
                YvsBaseCaisse c = (YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findDefault", new String[]{"agence"}, new Object[]{bean.getJournal().getAgence()});
                if (c != null ? (!Objects.equals(c.getId(), bean.getId())) : false) {
                    getErrorMessage("Vous ne pouvez définir une autre caisse par défaut");
                    return;
                }
            }
            bean.setDefaultCaisse(!bean.getDefaultCaisse());
            String rq = "UPDATE yvs_base_caisse SET default_caisse=" + bean.getDefaultCaisse() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            caisses.set(caisses.indexOf(bean), bean);
            succes();
        }
    }

    public void activeModeWizard(boolean active) {
        disableCompte = disableJournal = active;
        update("chmp_compteGen_Caisse");
        update("chmp_journal_Caisse");
    }

    public String onFlowProcess(FlowEvent event) {
        switch (event.getNewStep()) {
            case "comptes":
                break;
            case "journal":
                break;
            case "confirm":
                ManagedCompte wc = (ManagedCompte) giveManagedBean(ManagedCompte.class);
                if (wc != null ? Util.asString(wc.getCompte().getNumCompte()) : false) {
                    caisse.setCompte(wc.getCompte());
                    update("chmp_compteGen_Caisse");
                }
                ManagedJournaux wj = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
                if (wj != null ? Util.asString(wj.getJournal().getIntitule()) : false) {
                    YvsComptaJournaux y = UtilCompta.buildBeanJournaux(wj.getJournal());
                    y.setId(-100L);
                    wj.getJournaux().add(0, y);

                    caisse.setJournal(wj.getJournal());
                    caisse.getJournal().setId(-100L);
                    update("chmp_journal_Caisse");
                }
                break;
            default:
                break;
        }
        return event.getNewStep();
    }

    public void reset() {

    }

    public void save() {
        try {
            ManagedCompte wc = (ManagedCompte) giveManagedBean(ManagedCompte.class);
            if (wc != null) {

            }
            ManagedJournaux wj = (ManagedJournaux) giveManagedBean(ManagedCompte.class);
            if (wj != null) {

            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("save", ex);
        }
    }
}
