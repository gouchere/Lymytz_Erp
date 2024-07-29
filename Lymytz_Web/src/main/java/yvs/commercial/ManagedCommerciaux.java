/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial;

import yvs.commercial.commission.PlanCommission;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.tiers.ManagedTiers;
import yvs.base.tiers.Tiers;
import yvs.base.tiers.UtilTiers;
import yvs.commercial.commission.ManagedCommission;
import yvs.commercial.creneau.ManagedCreneau;
import yvs.commercial.creneau.ManagedCreneauEmploye;
import yvs.commercial.creneau.ManagedTypeCreneau;
import yvs.commercial.depot.ManagedDepot;
import yvs.commercial.depot.ManagedPointVente;
import yvs.comptabilite.caisse.ManagedCaisses;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.base.YvsBasePointVenteDepot;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.users.YvsUsers;
import yvs.entity.commercial.commission.YvsComPlanCommission;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;
import yvs.entity.commercial.creneau.YvsComCreneauPoint;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBaseLiaisonCaisse;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.parametrage.agence.UtilAgence;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.dico.ManagedDico;
import yvs.parametrage.societe.UtilSte;
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
public class ManagedCommerciaux extends Managed<Commerciales, YvsComComerciale> implements Serializable {

    private Commerciales commercial = new Commerciales();
    private List<YvsComComerciale> commerciaux;
    private YvsComComerciale selectCommercial = new YvsComComerciale();
    private boolean initForm = true;
    private ProfilCommercial profil = new ProfilCommercial();
    private Long firstTranche = 0L;

    private String tabIds, numSearch;
    private String step = "profil";
    boolean wizard = false;

    private boolean actionEmploye = true, actionTiers = true, actionClient = true, actionFournisseur = true;

    public ManagedCommerciaux() {
        commerciaux = new ArrayList<>();
    }

    public boolean isActionEmploye() {
        return actionEmploye;
    }

    public void setActionEmploye(boolean actionEmploye) {
        this.actionEmploye = actionEmploye;
    }

    public boolean isActionTiers() {
        return actionTiers;
    }

    public void setActionTiers(boolean actionTiers) {
        this.actionTiers = actionTiers;
    }

    public boolean isActionClient() {
        return actionClient;
    }

    public void setActionClient(boolean actionClient) {
        this.actionClient = actionClient;
    }

    public boolean isActionFournisseur() {
        return actionFournisseur;
    }

    public void setActionFournisseur(boolean actionFournisseur) {
        this.actionFournisseur = actionFournisseur;
    }

    public boolean isWizard() {
        return wizard;
    }

    public void setWizard(boolean wizard) {
        this.wizard = wizard;
    }

    public ProfilCommercial getProfil() {
        return profil;
    }

    public void setProfil(ProfilCommercial profil) {
        this.profil = profil;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public YvsComComerciale getSelectCommercial() {
        return selectCommercial;
    }

    public void setSelectCommercial(YvsComComerciale selectCommercial) {
        this.selectCommercial = selectCommercial;
    }

    public Commerciales getCommercial() {
        return commercial;
    }

    public void setCommercial(Commerciales commercial) {
        this.commercial = commercial;
    }

    public boolean isInitForm() {
        return initForm;
    }

    public void setInitForm(boolean initForm) {
        this.initForm = initForm;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public List<YvsComComerciale> getCommerciaux() {
        return commerciaux;
    }

    public void setCommerciaux(List<YvsComComerciale> commerciaux) {
        this.commerciaux = commerciaux;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        loadAllCommerciaux(true);
    }

    public void loadAllCommerciaux(boolean avancer) {
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        commerciaux = paginator.executeDynamicQuery("y", "y", "YvsComComerciale y JOIN FETCH y.agence JOIN FETCH y.utilisateur", "y.codeRef", avancer, initForm, (int) imax, dao);
        YvsComComerciale y = (YvsComComerciale) dao.loadOneByNameQueries("YvsComComerciale.findDefaut", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        if (y != null ? y.getId() > 0 : false) {
            YvsUsers u = new YvsUsers(-1L, y.getCodeRef(), y.getNom_prenom(), y.getAgence(), y.getActif());
            u.setCommercial(y);
            ManagedUser w = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (w != null) {
                if (!w.getListUser().contains(u)) {
                    w.getListUser().add(0, u);
                }
            }
        }
    }

    public void loadAllByAgence(long agence) {
        if (agence > 0) {
            commerciaux = dao.loadNameQueries("YvsComComerciale.findByAgence", new String[]{"agence"}, new Object[]{new YvsAgences(agence)});
        } else {
            commerciaux = dao.loadNameQueries("YvsComComerciale.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
    }

    public void loadAllCommerciaux(boolean avancer, boolean initForm) {
        this.initForm = initForm;
        loadAllCommerciaux(avancer);
    }

    public void addParamActif(boolean actif) {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actif, "=", "AND"));
        initForm = true;
        loadAllCommerciaux(true);
    }

    public void addParamCode() {
        ParametreRequete p = new ParametreRequete("y.codeRef", "code", null, "LIKE", "AND");
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "code", numSearch.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.codeRef)", "code", numSearch.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nom)", "code", numSearch.trim().toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        initForm = true;
        loadAllCommerciaux(true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAllCommerciaux(true, true);
    }

    public void saveNewOrRemoveCommerciale(YvsUsers user, boolean add) {
        if (user != null) {
            ManagedUser service = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (user.getId() > 0) {
                YvsComComerciale c = (YvsComComerciale) dao.loadOneByNameQueries("YvsComComerciale.findByUser", new String[]{"user"}, new Object[]{user});
                if (add) {
                    //vérifie si le user existe dans la table commerciale
                    if (c == null) {
                        //chereche l'employé correspondant à ce user                
                        c = new YvsComComerciale();
                        c.setCodeRef(user.getCodeUsers());
                        c.setAgence(currentAgence);
                        c.setAuthor(currentUser);
                        c.setDateSave(new Date());
                        c.setDateUpdate(new Date());
                        c.setUtilisateur(user);
                        c.setActif(true);
                        c.setNew_(true);
                        if (user.getEmploye() != null) {
                            c.setNom(user.getEmploye().getNom());
                            c.setPrenom(user.getEmploye().getPrenom());
                            c.setTelephone(user.getEmploye().getTelephone1());
                        } else {
                            c.setNom(user.getNom());
                        }
                        c = (YvsComComerciale) dao.save1(c);
                        commerciaux.add(0, c);
                    } else {
                        c.setActif(true);
                        c.setNew_(true);
                        dao.update(c);
                        int idx = commerciaux.indexOf(c);
                        if (idx >= 0) {
                            commerciaux.set(idx, c);
                        }
                    }
                } else {
                    if (c != null) {
                        c.setActif(!c.getActif());
                        c.setNew_(true);
                        c.setDateUpdate(new Date());
                        c.setAuthor(currentUser);
                        dao.update(c);
                        int idx = commerciaux.indexOf(c);
                        if (idx >= 0) {
                            commerciaux.set(idx, c);
                        }
                    }
                }
                if (service != null) {
                    int idx = service.getListUser().indexOf(user);
                    if (idx >= 0) {
                        service.getListUser().get(idx).setCommercial(c);
                    }
                }
            } else {
                dao.delete(user.getCommercial());
                if (service != null) {
                    service.getListUser().remove(user);
                }
            }
            succes();
        } else {
            getErrorMessage("Aucun utilisateur trouvé !");
        }
    }

    @Override
    public Commerciales recopieView() {
        return commercial;
    }

    @Override
    public boolean controleFiche(Commerciales bean) {
        if (!bean.isDefaut() && bean.getId() < 1) {
            getErrorMessage("Impossible de créer un commercial ici...!");
            return false;
        }
        if (!Util.asString(bean.getCode())) {
            getErrorMessage("Vous devez indiquer un code!");
            return false;
        }
        if (!Util.asString(bean.getNom())) {
            getErrorMessage("Vous devez indiquer un nom!");
            return false;
        }
        if (bean.isDefaut()) {
            YvsComComerciale y = (YvsComComerciale) dao.loadOneByNameQueries("YvsComComerciale.findDefaut", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
                getErrorMessage("Vous ne pouvez pas definir ce commercial comme par défaut car il existe déjà un commercial par défaut");
                return false;
            }
        }
        return true;
    }

    @Override
    public void populateView(Commerciales bean) {
        cloneObject(commercial, bean);
    }

    @Override
    public void resetFiche() {
        commercial = new Commerciales();
        commercial.setDefaut(false);
        selectCommercial = new YvsComComerciale();
        tabIds = "";
        update("blog_form_personnel");
    }

    @Override
    public boolean saveNew() {
        String action = commercial.getId() > 0 ? "Modification" : "Insertion";
        try {
            Commerciales bean = recopieView();
            if (controleFiche(bean)) {
                selectCommercial = UtilCom.buildBeanCommerciales(bean, currentUser, currentAgence);
                if (bean.getId() > 0) {
                    dao.update(selectCommercial);
                } else {
                    selectCommercial = (YvsComComerciale) dao.save1(selectCommercial);
                }
                int idx = commerciaux.indexOf(selectCommercial);
                if (idx > -1) {
                    commerciaux.set(idx, selectCommercial);
                } else {
                    commerciaux.add(0, selectCommercial);
                }
                if (bean.isDefaut() && (bean.getUser() != null ? bean.getUser().getId() < 1 : true)) {
                    ManagedUser w = (ManagedUser) giveManagedBean(ManagedUser.class);
                    if (w != null) {
                        YvsUsers u = new YvsUsers(-1L, selectCommercial.getCodeRef(), selectCommercial.getNom_prenom(), currentAgence, selectCommercial.getActif());
                        u.setCommercial(selectCommercial);
                        idx = w.getListUser().indexOf(u);
                        if (idx > -1) {
                            w.getListUser().set(idx, u);
                        } else {
                            w.getListUser().add(0, u);
                        }
                        update("tab_user_00_commerciaux");
                    }
                }
                succes();
                actionOpenOrResetAfter(this);
                update("data_personnel");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                String[] tab = tabIds.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComComerciale bean = commerciaux.get(commerciaux.indexOf(new YvsUsers(id)));
                    dao.delete(bean);
                    commerciaux.remove(bean);
                }
                succes();
                update("data_personnel");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void onSelectObject(YvsComComerciale y) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            selectCommercial = y;
            populateView(UtilCom.buildBeanCommerciales(y));
            update("blog_form_personnel");
        } else {
            getErrorMessage("Vous devez d'abord associer un utilisateur à ce commercial");
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsUsers y = (YvsUsers) ev.getObject();
            YvsComComerciale bean = null;
            if (y != null ? y.getId() > 0 : false) {
                bean = (YvsComComerciale) dao.loadOneByNameQueries("YvsComComerciale.findByUser", new String[]{"user"}, new Object[]{y});
            } else {
                bean = y.getCommercial();
            }
            onSelectObject(bean);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("blog_form_personnel");
    }

    public void loadOnViewTiers(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseTiers y = (YvsBaseTiers) ev.getObject();
            if (!wizard) {
                commercial.setTiers(UtilTiers.buildBeanTiers(y));
                update("select_tiers_commercial");
            } else {
                ManagedTiers wt = (ManagedTiers) giveManagedBean(ManagedTiers.class);
                if (wt != null) {
                    wt.onSelectObject(y);
                    cloneObject(profil.getTiers(), wt.getTiers());
                }
                update("infos_tiers");
            }
        }
    }

    public void loadOnViewUsers(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsUsers y = (YvsUsers) ev.getObject();
            ManagedUser wu = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (wu != null) {
                wu.onSelectObject(y);
                update("blog-form_users");
            }
        }
    }

    public void chooseCategorie() {

    }

    public void chooseParent() {

    }

    public void choosePlanCommission() {
        if ((commercial.getCommission() != null) ? commercial.getCommission().getId() > 0 : false) {
            ManagedCommission w = (ManagedCommission) giveManagedBean(ManagedCommission.class);
            if (w != null) {
                YvsComPlanCommission d_ = w.getPlans().get(w.getPlans().indexOf(new YvsComPlanCommission(commercial.getCommission().getId())));
                PlanCommission d = UtilCom.buildBeanPlanCommission(d_);
                cloneObject(commercial.getCommission(), d);
            }
        } else {
            if ((commercial.getCommission() != null) ? commercial.getCommission().getId() < 0 : true) {
                openDialog("dlgAddPlanCommission");
            }
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void importPersonnel() {

    }

    public void searchTiers() {
        searchTiers(false);
    }

    public void searchTiers(boolean wizard) {
        this.wizard = wizard;
        String num;
        if (!wizard) {
            num = commercial.getTiers().getCodeTiers();
            commercial.getTiers().setId(0);
            commercial.getTiers().setError(true);
        } else {
            num = profil.getTiers().getCodeTiers();
            profil.getTiers().setId(0);
            profil.getTiers().setError(true);
        }
        ManagedTiers m = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (m != null) {
            Tiers y = m.findTiers(num, true);
            if (m.getListTiers() != null ? !m.getListTiers().isEmpty() : false) {
                if (m.getListTiers().size() > 1) {
                    update("data_tiers_commercial");
                } else {
                    if (!wizard) {
                        commercial.setTiers(y);
                    } else {
                        profil.setTiers(y);
                    }
                }
                if (!wizard) {
                    commercial.getTiers().setError(false);
                } else {
                    profil.getTiers().setError(false);
                }
            }
        }
    }

    public void initTiers() {
        ManagedTiers m = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (m != null) {
            m.initTiers(commercial.getTiers());
        }
        update("data_tiers_commercial");
    }

    public Commerciales searchCommercial(String num, boolean open) {
        Commerciales a = new Commerciales();
        a.setCode(num);
        a.setError(true);
        if (num != null ? num.trim().length() > 0 : false) {
            ParametreRequete p = new ParametreRequete(null, "code", num.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.codeRef)", "code", num.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nom)", "code", num.trim().toUpperCase() + "%", "LIKE", "OR"));
            paginator.addParam(p);
            loadAllCommerciaux(true, true);
            a = chechCommercialResult(open);
            if (a != null ? a.getId() < 1 : true) {
                a.setCode(num);
                a.setError(true);
            }
        } else {
            paginator.addParam(new ParametreRequete("y.codeRef", "code", null));
            loadAllCommerciaux(true, true);
            a = chechCommercialResult(open);
        }
        return a;
    }

    public void removeParamCode() {
        paginator.getParams().remove(new ParametreRequete("y.codeRef", "code", null));
    }

    private Commerciales chechCommercialResult(boolean open) {
        Commerciales a = new Commerciales();
        if (commerciaux != null ? !commerciaux.isEmpty() : false) {
            if (commerciaux.size() > 1) {
                if (open) {
                    openDialog("dlgListCommerciaux");
                }
                a.setList(true);
            } else {
                YvsComComerciale c = commerciaux.get(0);
                a = UtilCom.buildBeanCommerciales(c);
            }
            a.setError(false);
        }
        return a;
    }

    public void initCommerciaux(Commerciales a) {
        if (a == null) {
            a = new Commerciales();
        }
        paginator.addParam(new ParametreRequete("y.codeRef", "code", null));
        loadAllCommerciaux(true, true);
        a.setList(true);
    }

    public void initView() {
        String query = "SELECT y.id FROM yvs_grh_tranche_horaire y WHERE y.societe = ? ORDER BY date_part('hour', heure_fin - heure_debut) DESC LIMIT 1";
        if (profil.getTrancheDepot() < 1) {
            if (firstTranche < 1) {
                firstTranche = (Long) dao.loadObjectBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1)});
            }
            profil.setTrancheDepot(firstTranche != null ? firstTranche : 0);
        }
        if (profil.getTranchePoint() < 1) { 
            if (firstTranche==null?true: firstTranche < 1) {
                firstTranche = (Long) dao.loadObjectBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1)});
            }
            profil.setTranchePoint(firstTranche != null ? firstTranche : 0);
        }

        ManagedUser wu = (ManagedUser) giveManagedBean(ManagedUser.class);
        if (wu != null ? (!Util.asString(wu.getUsers().getNomUsers())) : false) {
            wu.resetFiche();
        }
        ManagedTiers wt = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (wt != null ? (!Util.asString(wt.getTiers().getNom())) : false) {
            wt.resetFiche();
        }
        ManagedCreneauEmploye wc = (ManagedCreneauEmploye) giveManagedBean(ManagedCreneauEmploye.class);
        if (wc != null ? (wc.getCreneauUsers().getId_() > 0 || !Util.asString(wc.getCreneauUsers().getPersonnel().getNomUsers())) : false) {
            wc.resetFiche();
        }
        ManagedDepot wd = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (wd != null ? (wd.getDepot().getId() > 0 || !Util.asString(wd.getDepot().getDesignation())) : false) {
            wd.resetFiche();
        }
        ManagedPointVente wp = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
        if (wp != null ? (wp.getPointVente().getId() > 0 || !Util.asString(wp.getPointVente().getCode())) : false) {
            wp.resetFiche();
        }
        ManagedCaisses wa = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (wa != null ? (wa.getCaisse().getId() > 0 || !Util.asString(wa.getCaisse().getIntitule())) : false) {
            wa.resetFiche();
        }
    }

    public String onFlowProcess(FlowEvent event) {
        if (profil.isSkip()) {
            profil.setSkip(false);
            return "confirm";
        }
        step = event.getNewStep();
        String continu = "";
        switch (step) {
            case "utilisateur":
                continu = onLoadUsers();
                break;
            case "tiers":
                continu = onLoadTiers();
                break;
            case "depot":
                continu = onLoadDepot();
                break;
            case "point_vente":
                continu = onLoadPointVente();
                break;
            case "caisse":
                continu = onLoadCaisse();
                break;
            case "planning":
                continu = onLoadPlanning();
                break;
            case "confirm":
                continu = onLoadCommercial();
                break;
            default:
                break;
        }
        step = !Util.asString(continu) ? event.getNewStep() : continu;
        return step;
    }

    private String onLoadUsers() {
        try {
            ManagedUser wu = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (wu != null) {
                wu.getUsers().setAccesMultiAgence(false);
                wu.getUsers().setAccesMultiSociete(false);
                wu.getUsers().setConnectOnlinePlanning(false);
                wu.getUsers().setInitPassword(false);
            }
            update("blog-form_users");
        } catch (Exception ex) {
            getException("onLoadUsers", ex);
        }
        return "";
    }

    private String onLoadTiers() {
        try {
            ManagedUser wu = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (wu != null ? !wu.controleFiche(wu.getUsers()) : false) {
                return "utilisateur";
            }
            ManagedTiers wt = (ManagedTiers) giveManagedBean(ManagedTiers.class);
            if (wt != null) {
                wt.getTiers().setSociete(false);
                wt.getTiers().setPersonnel(false);
                wt.getTiers().setEmploye(true);
                if (wt != null) {
                    wt.getTiers().setAgence(wu.getUsers().getAgence());
                    if (profil.isGeneredTiers()) {
                        wt.getTiers().setCodeTiers(wu.getUsers().getCodeUsers().trim().replace(" ", "_"));
                        String nom = wu.getUsers().getNomUsers();
                        String[] tab = nom.split(" ");
                        if (tab.length > 1) {
                            wt.getTiers().setPrenom(tab[tab.length - 1]);
                            nom = tab[0];
                            for (int i = 1; i < tab.length - 1; i++) {
                                nom += " " + tab[i];
                            }
                        }
                        wt.getTiers().setNom(nom);
                        ManagedDico w = (ManagedDico) giveManagedBean("Mdico");
                        if (wt.getTiers().getPays().getId() < 1) {
                            wt.getTiers().setPays(UtilSte.buildBeanDictionnaire(currentAgence.getSociete().getPays()));
                            if (w != null) {
                                w.choosePays(wt.getTiers().getPays().getId());
                            }
                        }
                        if (wt.getTiers().getVille().getId() < 1) {
                            wt.getTiers().setVille(UtilSte.buildBeanDictionnaire(currentAgence.getSociete().getVille()));
                            if (w != null) {
                                w.chooseVille(wt.getTiers().getPays(), wt.getTiers().getVille().getId());
                            }
                        }
                    }
                }
            }
            update("infos_tiers");
        } catch (Exception ex) {
            getException("onLoadTiers", ex);
        }
        return "";
    }

    private String onLoadDepot() {
        try {
            if (profil.isGeneredTiers()) {
                ManagedTiers wt = (ManagedTiers) giveManagedBean(ManagedTiers.class);
                if (wt != null ? !wt.controleFiche(wt.getTiers()) : false) {
                    return "tiers";
                }
            }
            if (profil.isGeneredDepot()) {
                ManagedUser wu = (ManagedUser) giveManagedBean(ManagedUser.class);
                ManagedDepot wd = (ManagedDepot) giveManagedBean(ManagedDepot.class);
                if (wu != null && wd != null) {
                    wd.getDepot().setCode(wu.getUsers().getCodeUsers().trim().replace(" ", "_"));
                    wd.getDepot().setAbbreviation(wu.getUsers().getNomUsers().trim().replace(" ", "_"));
                    wd.getDepot().setDesignation(profil.getModelDepot() + wu.getUsers().getNomUsers().trim().replace(" ", "_"));
                    wd.getDepot().setAgence(wu.getUsers().getAgence());
                    wd.getDepot().setTypeMp(false);
                    wd.getDepot().setTypeNegoce(false);
                    wd.getDepot().setTypePf(false);
                    wd.getDepot().setTypePsf(false);
                }
                update("blog-form_depot");
            }
        } catch (Exception ex) {
            getException("onLoadDepot", ex);
        }
        return "";
    }

    private String onLoadPointVente() {
        try {
            if (profil.isGeneredDepot()) {
                if (profil.isGeneredPlanning()) {
                    if (profil.getTrancheDepot() < 1) {
                        getErrorMessage("Vous devez choisir une tranche pour le dépôt");
                        return "depot";
                    }
                }
                ManagedDepot wd = (ManagedDepot) giveManagedBean(ManagedDepot.class);
                if (wd != null ? !wd.controleFiche(wd.getDepot()) : false) {
                    return "depot";
                }
            }
            if (profil.isGeneredPoint()) {
                ManagedUser wu = (ManagedUser) giveManagedBean(ManagedUser.class);
                ManagedPointVente wp = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                if (wu != null && wp != null) {
                    wp.getPointVente().setCode(wu.getUsers().getNomUsers().trim().replace(" ", "_"));
                    wp.getPointVente().setLibelle(profil.getModelPoint() + wu.getUsers().getNomUsers().trim().replace(" ", "_"));
                    wp.getPointVente().setAgence(wu.getUsers().getAgence());
                }
                update("blog-form_point_vente");
            }
        } catch (Exception ex) {
            getException("onLoadPointVente", ex);
        }
        return "";
    }

    private String onLoadCaisse() {
        try {
            if (profil.isGeneredPoint()) {
                if (profil.isGeneredPlanning()) {
                    if (profil.getTranchePoint() < 1) {
                        getErrorMessage("Vous devez choisir une tranche pour le point de vente");
                        return "depot";
                    }
                }
                ManagedPointVente wp = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                if (wp != null ? !wp.controleFiche(wp.getPointVente()) : false) {
                    return "point_vente";
                }
            }
            if (profil.isGeneredCaisse()) {
                ManagedUser wu = (ManagedUser) giveManagedBean(ManagedUser.class);
                ManagedCaisses wa = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                if (wu != null && wa != null) {
                    wa.getCaisse().setDefaultCaisse(false);
                    wa.getCaisse().setCaissier(wu.getUsers());
                    wa.getCaisse().setCode(wu.getUsers().getCodeUsers().trim().replace(" ", "_"));
                    wa.getCaisse().setIntitule(profil.getModelCaisse() + wu.getUsers().getNomUsers().trim().replace(" ", "_"));
                    wa.getCaisse().setCanBeNegative(false);

                    YvsUsers users = UtilUsers.buildSimpleBeanUsers(wu.getUsers());
                    int index = wu.getListAllUser().indexOf(new YvsUsers(wu.getUsers().getId()));
                    if (index < 0 && users != null) {
                        wu.getListAllUser().add(users);
                    }
                }
                update("wblog-form_caisse");
            }
        } catch (Exception ex) {
            getException("onLoadCaisse", ex);
        }
        return "";
    }

    private String onLoadPlanning() {
        try {
            if (profil.isGeneredCaisse()) {
                ManagedCaisses wa = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                if (wa != null ? !wa.controleFiche(wa.getCaisse()) : false) {
                    return "caisse";
                }
            }
            if (profil.isGeneredPlanning()) {
                if (profil.isGeneredDepot()) {
                    if (profil.getTrancheDepot() < 1) {
                        getErrorMessage("Vous devez choisir une tranche pour le dépôt");
                        return "depot";
                    }
                }
                if (profil.isGeneredPoint()) {
                    if (profil.getTranchePoint() < 1) {
                        getErrorMessage("Vous devez choisir une tranche pour le point de vente");
                        return "point_vente";
                    }
                }
                ManagedUser wu = (ManagedUser) giveManagedBean(ManagedUser.class);
                ManagedDepot wd = (ManagedDepot) giveManagedBean(ManagedDepot.class);
                ManagedPointVente wp = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                ManagedTypeCreneau wc = (ManagedTypeCreneau) giveManagedBean(ManagedTypeCreneau.class);
                ManagedCreneauEmploye we = (ManagedCreneauEmploye) giveManagedBean(ManagedCreneauEmploye.class);
                YvsComCreneauDepot depot = null;
                YvsComCreneauPoint point = null;
                if (wd != null) {
                    if (wd.getDepots().isEmpty()) {
                        wd.loadDepotActifByAgence(wu.getUsers().getAgence().getId());
                    }
                    if (profil.isGeneredDepot()) {
                        YvsBaseDepots y = UtilCom.buildDepot(wd.getDepot(), currentUser, currentAgence);
                        if (wc != null) {
                            int index = wc.getTranches().indexOf(new YvsGrhTrancheHoraire(profil.getTrancheDepot()));
                            if (index > -1) {
                                depot = new YvsComCreneauDepot(-1L, wc.getTranches().get(index), y);
                            }
                        }
                        wd.getDepots().add(0, y);
                    }
                }
                if (wp != null) {
                    wp.loadAllPointVente(wu.getUsers().getAgence().getId());
                    if (profil.isGeneredPoint()) {
                        YvsBasePointVente y = UtilCom.buildPointVente(wp.getPointVente(), currentUser, currentAgence);
                        if (wc != null) {
                            int index = wc.getTranches().indexOf(new YvsGrhTrancheHoraire(profil.getTranchePoint()));
                            if (index > -1) {
                                point = new YvsComCreneauPoint(-1L, wc.getTranches().get(index), y);
                            }
                        }
                        wp.getPointsvente().add(0, y);
                    }
                }
                if (we != null && wu != null) {
                    we.getCreneauUsers().setPersonnel(wu.getUsers());
                    we.getCreneauUsers().getPersonnel().setError(false);
                    if (depot != null) {
                        we.setDepot(wd.getDepot());
                        we.getCreneauUsers().setCreneauDepot(UtilCom.buildBeanCreneau(depot));
                        we.getCreneauxDepot().add(depot);
                    }
                    if (point != null) {
                        we.setPoint(wp.getPointVente());
                        we.getCreneauUsers().setCreneauPoint(UtilCom.buildBeanCreneau(point));
                        we.getCreneauxPoint().add(point);
                    }
                }
                update("form_creneau_employe");
            }
        } catch (Exception ex) {
            getException("onLoadPlanning", ex);
        }
        return "";
    }

    private String onLoadCommercial() {
        try {
            if (profil.isGeneredPlanning()) {
                if (profil.isGeneredPoint()) {
                    if (profil.getTranchePoint() < 1) {
                        getErrorMessage("Vous devez choisir une tranche pour le point de vente");
                        return "point_vente";
                    }
                }
                if (profil.isGeneredDepot()) {
                    if (profil.getTrancheDepot() < 1) {
                        getErrorMessage("Vous devez choisir une tranche pour le dépôt");
                        return "depot";
                    }
                }
            }
            if (profil.isGeneredCommercial()) {
                ManagedUser wu = (ManagedUser) giveManagedBean(ManagedUser.class);
                ManagedTiers wt = (ManagedTiers) giveManagedBean(ManagedTiers.class);
                if (wt != null && wu != null) {
                    cloneObject(commercial.getUser(), wu.getUsers());
                    cloneObject(commercial.getTiers(), wt.getTiers());
                    commercial.setCode(wt.getTiers().getCodeTiers());
                    commercial.setNom(wt.getTiers().getNom());
                    commercial.setPrenom(wt.getTiers().getPrenom());
                    commercial.setActif(true);
                    commercial.getUser().setError(false);
                    commercial.getTiers().setError(false);
                }
                update("view:blog_form_personnel");
            }
        } catch (Exception ex) {
            getException("onLoadCommercial", ex);
        }
        return "";
    }

    public void reset() {
        ManagedUser wu = (ManagedUser) giveManagedBean(ManagedUser.class);
        if (wu != null) {
            wu.resetFiche();
        }
        ManagedTiers wt = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (wt != null) {
            wt.resetFiche();
        }
        ManagedDepot wd = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (wd != null) {
            wd.resetFiche();
        }
        ManagedPointVente wp = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
        if (wp != null) {
            wp.resetFiche();
        }
        ManagedCreneauEmploye wc = (ManagedCreneauEmploye) giveManagedBean(ManagedCreneauEmploye.class);
        if (wc != null) {
            wc.resetFiche();
        }
        ManagedCaisses wa = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (wa != null) {
            wa.resetFiche();
        }
        update("wizard-personnel");
    }

    public void save() {
        try {
            ManagedUser wu = (ManagedUser) giveManagedBean(ManagedUser.class);
            ManagedTiers wt = (ManagedTiers) giveManagedBean(ManagedTiers.class);
            ManagedCreneauEmploye wc = (ManagedCreneauEmploye) giveManagedBean(ManagedCreneauEmploye.class);
            ManagedCaisses wa = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);

            if (wu != null) {
                YvsUsers users = wu.saveNew(wu.getUsers());
                if (users != null ? users.getId() > 0 : false) {
                    YvsBaseTiers tiers = null;
                    if (profil.isGeneredTiers()) {
                        tiers = wt.saveNew(wt.getTiers());
                        if (tiers != null ? tiers.getId() < 1 : true) {
                            return;
                        }
                    } else {
                        tiers = UtilCom.buildTiers(wt.getTiers());
                    }
                    YvsComCreneauDepot depot = null;
                    if (profil.isGeneredDepot()) {
                        depot = onSaveCreneauDepot(wc);
                    }
                    YvsComCreneauPoint point = null;
                    if (profil.isGeneredPoint()) {
                        point = onSaveCreneauPoint(wc, depot);
                    }
                    if (profil.isGeneredPlanning()) {
                        if (depot != null ? depot.getId() > 0 : false) {
                            wc.getCreneauUsers().setCreneauDepot(UtilCom.buildBeanCreneau(depot));
                        }
                        if (point != null ? point.getId() > 0 : false) {
                            wc.getCreneauUsers().setCreneauPoint(UtilCom.buildBeanCreneau(point));
                        }
                        YvsComCreneauHoraireUsers creneau = null;
                        if (wc != null) {
                            if (wc.getCreneauUsers().getCreneauDepot().getId() > 0 || wc.getCreneauUsers().getCreneauPoint().getId() > 0) {
                                wc.getCreneauUsers().setPersonnel(new Users(users.getId()));
                                creneau = wc.saveNew(wc.getCreneauUsers());
                                if (creneau == null) {
                                    return;
                                }
                            }
                        }
                    }
                    if (profil.isGeneredCaisse()) {
                        YvsBaseCaisse caisse = null;
                        if (wa != null ? Util.asString(wa.getCaisse().getIntitule()) : false) {
                            wa.getCaisse().setCaissier(new Users(users.getId()));
                            caisse = wa.saveNew(wa.getCaisse());
                            if (caisse != null ? caisse.getId() < 1 : true) {
                                return;
                            }
                            if (wa.getCaisse().getCaisseLie() > 0) {
                                YvsBaseLiaisonCaisse liee = new YvsBaseLiaisonCaisse(null);
                                liee.setCaisseSource(caisse);
                                liee.setCaisseCible(new YvsBaseCaisse(wa.getCaisse().getCaisseLie()));
                                liee.setActif(true);
                                liee.setAuthor(currentUser);
                                dao.save1(liee);
                            }
                        }
                    }
                    if (profil.isGeneredCommercial()) {
                        YvsComComerciale y = UtilCom.buildBeanCommerciales(commercial, currentUser, currentAgence);
                        y.setUtilisateur(users);
                        y.setTiers(tiers);
                        y = (YvsComComerciale) dao.save1(y);
                        commerciaux.add(y);
                    }
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("save", ex);
        }
    }

    private YvsComCreneauDepot onSaveCreneauDepot(ManagedCreneauEmploye wc) {
        YvsComCreneauDepot result = null;
        try {
            YvsBaseDepots depot = UtilCom.buildDepot(wc.getDepot(), currentUser, currentAgence);
            depot = (YvsBaseDepots) dao.save1(depot);

            result = new YvsComCreneauDepot(null, UtilCom.buildTrancheHoraire(wc.getCreneauUsers().getCreneauDepot().getTranche(), currentUser), depot);
            result.setActif(true);
            result.setPermanent(true);
            result.setAuthor(currentUser);
            result = (YvsComCreneauDepot) dao.save1(result);

            if (depot.getTypeMp() || depot.getTypePf()) {
                String query = "SELECT y.id FROM yvs_base_articles y INNER JOIN yvs_base_famille_article f ON y.famille = f.id WHERE y.actif IS TRUE AND f.societe = ? AND y.categorie IN (:categories)";
                String categories = "";
                if (depot.getTypeMp()) {
                    categories = "'MARCHANDISE'";
                }
                if (depot.getTypePf()) {
                    categories += (Util.asString(categories) ? "," : "") + "'PF'";
                }
                query = query.replace(":categories", categories);
                List<Long> articles = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1)});
                YvsBaseArticleDepot y;
                for (Long id : articles) {
                    y = new YvsBaseArticleDepot(null, new YvsBaseArticles(id), depot);
                    y.setAuthor(currentUser);
                    dao.save(y);
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("onSaveCreneauDepot", ex);
        }
        return result;
    }

    private YvsComCreneauPoint onSaveCreneauPoint(ManagedCreneauEmploye wc, YvsComCreneauDepot depot) {
        YvsComCreneauPoint result = null;
        try {
            YvsBasePointVente point = UtilCom.buildPointVente(wc.getPoint(), currentUser, currentAgence);
            point = (YvsBasePointVente) dao.save1(point);

            result = new YvsComCreneauPoint(null, UtilCom.buildTrancheHoraire(wc.getCreneauUsers().getCreneauDepot().getTranche(), currentUser), point);
            result.setActif(true);
            result.setPermanent(true);
            result.setAuthor(currentUser);
            result = (YvsComCreneauPoint) dao.save1(result);

            if (depot != null ? depot.getId() > 0 : false) {
                YvsBasePointVenteDepot y = new YvsBasePointVenteDepot(null, true, true, depot.getDepot());
                y.setPointVente(point);
                dao.save(y);
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("onSaveCreneauPoint", ex);
        }
        return result;
    }

    public void activeTiers(YvsComComerciale bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            bean.setAuthor(currentUser);
            bean.setDateUpdate(new Date());
            dao.update(bean);
            if (actionTiers) {
                bean.getTiers().setActif(bean.getActif());
                bean.getTiers().setAuthor(currentUser);
                bean.getTiers().setDateUpdate(new Date());
                dao.update(bean.getTiers());
            }
            if (actionClient) {
                for (YvsComClient y : bean.getTiers().getClients()) {
                    y.setActif(bean.getActif());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                }
            }
            if (actionEmploye) {
                for (YvsGrhEmployes y : bean.getTiers().getEmployes()) {
                    y.setActif(bean.getActif());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                }
            }
            if (actionFournisseur) {
                for (YvsBaseFournisseur y : bean.getTiers().getFournisseurs()) {
                    y.setActif(bean.getActif());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                }
            }
        }
    }

}
