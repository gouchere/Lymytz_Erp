/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean.mission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.Comptes;
import yvs.base.compta.ManagedCentreAnalytique;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.UtilCompta;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.grh.activite.YvsGrhObjetsMission;
import yvs.entity.grh.activite.YvsGrhObjetsMissionAnalytique;
import yvs.grh.UtilGrh;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedObjetMission extends Managed<ObjetMission, YvsGrhObjetsMission> implements Serializable {

    @ManagedProperty(value = "#{objetMission}")
    private ObjetMission objetM;
    private List<YvsGrhObjetsMission> objetsMissions;
    boolean initForm = true;

    private ObjetMissionAnalytique analytique = new ObjetMissionAnalytique();
    private YvsGrhObjetsMissionAnalytique selectAnalytique;

    public ManagedObjetMission() {
        objetsMissions = new ArrayList<>();
    }

    public ObjetMissionAnalytique getAnalytique() {
        return analytique;
    }

    public void setAnalytique(ObjetMissionAnalytique analytique) {
        this.analytique = analytique;
    }

    public YvsGrhObjetsMissionAnalytique getSelectAnalytique() {
        return selectAnalytique;
    }

    public void setSelectAnalytique(YvsGrhObjetsMissionAnalytique selectAnalytique) {
        this.selectAnalytique = selectAnalytique;
    }

    public ObjetMission getObjetM() {
        return objetM;
    }

    public void setObjetM(ObjetMission objetM) {
        this.objetM = objetM;
    }

    public List<YvsGrhObjetsMission> getObjetsMissions() {
        return objetsMissions;
    }

    public void setObjetsMissions(List<YvsGrhObjetsMission> objetsMissions) {
        this.objetsMissions = objetsMissions;
    }

    @Override
    public boolean controleFiche(ObjetMission bean) {
        if ((bean.getTitre() != null) ? bean.getTitre().trim().isEmpty() : true) {
            getErrorMessage("Vous devez entrer le titre de cet objet !");
            return false;
        }
        return true;
    }

    public boolean controleFiche(ObjetMissionAnalytique bean) {
        if (bean.getObjetMission() != null ? bean.getObjetMission().getId() < 1 : true) {
            saveNewOne();
            bean.setObjetMission(objetM);
            if (bean.getObjetMission() != null ? bean.getObjetMission().getId() < 1 : true) {
                return false;
            }
        }
        if (bean.getCentre() != null ? bean.getCentre().getId() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez selectionner le centre analytique");
            return false;
        }
        double taux = bean.getTaux();
        for (YvsGrhObjetsMissionAnalytique a : objetM.getAnalytiques()) {
            if (!a.getId().equals(bean.getId()) ? a.getCentre().getId().equals(bean.getCentre().getId()) : false) {
                getErrorMessage("Formulaire invalide", "Vous avez deja associé ce centre analytique");
                return false;
            }
            if (!a.getId().equals(bean.getId())) {
                taux += a.getCoefficient();
            }
        }
        if (taux > 100) {
            getErrorMessage("Formulaire invalide", "La repartition analytique ne peut pas etre supérieur à 100%");
            return false;
        }
        return true;
    }

    @Override
    public void loadAll() {
        loadAllObjet(true);
    }

    private void loadAllObjet(boolean next) {
        ParametreRequete p = new ParametreRequete("y.author.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
        paginator.addParam(p);
        objetsMissions = paginator.executeDynamicQuery("y", "y", "YvsGrhObjetsMission y LEFT JOIN FETCH y.compteCharge ", "y.titre", next, initForm, (int) imax, dao);
    }

    public void addParamActif(boolean actif) {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", actif, "=", "AND");
        initForm = true;
        loadAllObjet(true);
    }

    @Override
    public ObjetMission recopieView() {
        ObjetMission ob = new ObjetMission();
        ob.setCompteCharge(objetM.getCompteCharge());
        ob.setDescription(objetM.getDescription());
        ob.setId(objetM.getId());
        ob.setTitre(objetM.getTitre());
        ob.setActif(objetM.isActif());
        return ob;
    }

    @Override
    public void populateView(ObjetMission bean) {
        cloneObject(objetM, bean);
    }

    @Override
    public void deleteBean() {

    }

    @Override
    public void updateBean() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void resetFicheAnalytique() {
        analytique = new ObjetMissionAnalytique();
        selectAnalytique = new YvsGrhObjetsMissionAnalytique();
        update("bloc-param-01:form-objet_mission_analytique");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        populateView(UtilGrh.buildBeanObjetMission((YvsGrhObjetsMission) ev.getObject()));
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {

    }

    public void loadOnViewAnalytique(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectAnalytique = (YvsGrhObjetsMissionAnalytique) ev.getObject();
            analytique = UtilGrh.buildBeanObjetMissionAnalytique(selectAnalytique);
            update("bloc-param-01:form-objet_mission_analytique");
        }
    }

    public void unLoadOnViewAnalytique(UnselectEvent ev) {
        resetFicheAnalytique();
    }

    @Override
    public boolean saveNew() {
        YvsGrhObjetsMission ob = saveNewOne();
        if (ob != null ? ob.getId() > 0 : false) {
            actionOpenOrResetAfter(this);
            return true;
        }
        return true;
    }

    public YvsGrhObjetsMission saveNewOne() {
        YvsGrhObjetsMission ob = null;
        if (controleFiche(objetM)) {
            ob = UtilGrh.buildBeanObjetMission(objetM, currentUser);
            ob.setAuthor(currentUser);
            if (objetM.getId() <= 0) {
                ob.setId(null);
                ob = (YvsGrhObjetsMission) dao.save1(ob);
                objetsMissions.add(ob);
                objetM.setId(ob.getId());
            } else {
                dao.update(ob);
                int idx = objetsMissions.indexOf(ob);
                if (idx >= 0) {
                    objetsMissions.set(idx, ob);
                }
            }
            update("bloc-param-01:table_param_objetM");
            update("bloc-param-01:form_edit_objectM");
        }
        return ob;
    }

    public void saveNewAnalytique() {
        try {
            analytique.setObjetMission(objetM);
            if (controleFiche(analytique)) {
                selectAnalytique = UtilGrh.buildObjetMissionAnalytique(analytique, currentUser);
                if (analytique.getId() < 1) {
                    selectAnalytique = (YvsGrhObjetsMissionAnalytique) dao.save1(selectAnalytique);
                } else {
                    dao.update(selectAnalytique);
                }
                int idx = objetM.getAnalytiques().indexOf(selectAnalytique);
                if (idx < 0) {
                    objetM.getAnalytiques().add(0, selectAnalytique);
                } else {
                    objetM.getAnalytiques().set(idx, selectAnalytique);
                }
                resetFicheAnalytique();
                succes();
                update("bloc-param-01:data-objet_mission_analytique");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedObjetMission (saveNewAnalytique)", ex);
        }
    }

    public void toogleActive(YvsGrhObjetsMission ob) {
        ob.setActif(!ob.getActif());
        ob.setAuthor(currentUser);
        dao.update(ob);
    }

    public void deleteOneObjet(YvsGrhObjetsMission ob) {
        try {
            ob.setAuthor(currentUser);
            dao.delete(ob);
            objetsMissions.remove(ob);
            succes();
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet élément !");
            getException("Lymytz Error >>> ", ex);
        }
    }

    public void deleteAnalytique(YvsGrhObjetsMissionAnalytique y, boolean delete) {
        try {
            if (y != null) {
                if (!delete) {
                    selectAnalytique = y;
                    openDialog("dlgConfirmDeleteAnalytique");
                    return;
                }
                dao.delete(y);
                objetM.getAnalytiques().remove(y);
                if (analytique.getId() == y.getId()) {
                    resetFicheAnalytique();
                }
                update("bloc-param-01:data-objet_mission_analytique");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedArticles (deleteAnalytique)", ex);
        }
    }

    public void ecouteSaisieCG() {
        //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
        ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (service != null) {
            service.findCompteByNum(objetM.getCompteCharge().getNumCompte());
            objetM.getCompteCharge().setError(service.getListComptes().isEmpty());
            if (service.getListComptes() != null) {
                if (!service.getListComptes().isEmpty()) {
                    if (service.getListComptes().size() == 1) {
                        objetM.getCompteCharge().setError(false);
                        cloneObject(objetM.getCompteCharge(), UtilCompta.buildBeanCompte(service.getListComptes().get(0)));
                    } else {
                        objetM.getCompteCharge().setError(false);
                        openDialog("dlgCmpteG");
                        update("table_cptG_paramGRH");
                    }
                } else {
                    objetM.getCompteCharge().setError(true);
                }
            } else {
                objetM.getCompteCharge().setError(true);
            }
        }

    }

    public void choisirCompte(SelectEvent ev) {
        if (ev != null) {
            cloneObject(objetM.getCompteCharge(), UtilCompta.buildSimpleBeanCompte(((YvsBasePlanComptable) ev.getObject())));
            update("bloc-param-01:form_edit_objectM_cg");
        }
    }

    public void chooseCentre() {
        if (analytique.getCentre() != null ? analytique.getCentre().getId() > 0 : false) {
            ManagedCentreAnalytique service = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
            if (service != null) {
                int idx = service.getCentres().indexOf(new YvsComptaCentreAnalytique(analytique.getCentre().getId()));
                if (idx > -1) {
                    YvsComptaCentreAnalytique y = service.getCentres().get(idx);
                    analytique.setCentre(UtilCompta.buildBeanCentreAnalytique(y));
                }
            }
        }
    }

    @Override
    public void resetFiche() {
        resetFiche(objetM);
        objetM.setCompteCharge(new Comptes());
        update("bloc-param-01:form_edit_objectM");
    }
}
