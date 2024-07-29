

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean.mission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.Comptes;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.grh.activite.YvsGrhDetailGrilleFraiMission;
import yvs.entity.grh.activite.YvsGrhGrilleMission;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.grh.UtilGrh;
import yvs.grh.bean.ManagedTypeCout;
import yvs.grh.bean.TypeCout;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedFraisMission extends Managed<GrilleFraisMission, YvsGrhGrilleMission> implements Serializable {

    private DetailFraisMission cout = new DetailFraisMission();
    @ManagedProperty(value = "#{grilleFraisMission}")
    private GrilleFraisMission grilleM;
//    private DetailFraisMission detailFrais = new DetailFraisMission();
    private List<YvsGrhGrilleMission> grillesMissions;

    public ManagedFraisMission() {
        grillesMissions = new ArrayList<>();
    }

    public DetailFraisMission getCout() {
        return cout;
    }

    public void setCout(DetailFraisMission cout) {
        this.cout = cout;
    }

    public GrilleFraisMission getGrilleM() {
        return grilleM;
    }

    public void setGrilleM(GrilleFraisMission grilleM) {
        this.grilleM = grilleM;
    }

    public List<YvsGrhGrilleMission> getGrillesMissions() {
        return grillesMissions;
    }

    public void setGrillesMissions(List<YvsGrhGrilleMission> grillesMissions) {
        this.grillesMissions = grillesMissions;
    }

//    public DetailFraisMission getDetailFrais() {
//        return detailFrais;
//    }
//
//    public void setDetailFrais(DetailFraisMission detailFrais) {
//        this.detailFrais = detailFrais;
//    }
    boolean initForm = true;

    public void loadAll(boolean next) {
        ParametreRequete p = new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND");
        paginator.addParam(p);
        grillesMissions = paginator.executeDynamicQuery("YvsGrhGrilleMission", "y.categorie, y.titre", initForm, next, (int) imax, dao);
    }

    public void saveGrilleFrais() {
        if (grilleM.getCategorie() != null && grilleM.getTitre() != null) {
            if (!grilleM.getTitre().isEmpty() && !grilleM.getCategorie().isEmpty()) {
                YvsGrhGrilleMission gm = new YvsGrhGrilleMission();
                gm.setActif(grilleM.isActif());
                gm.setAuthor(currentUser);
                gm.setDateUpdate(new Date());
                gm.setCategorie(grilleM.getCategorie());
                gm.setSociete(currentUser.getAgence().getSociete());
                gm.setId(grilleM.getId());
                gm.setDateSave(grilleM.getDateSave());
                gm.setNumCompte((grilleM.getCompteCharge().getId() > 0) ? new YvsBasePlanComptable(grilleM.getCompteCharge().getId()) : null);
                grilleM.setId(gm.getId());
                gm.setTitre(grilleM.getTitre());
                if (grilleM.getId() <= 0) {
                    gm.setId(null);
                    gm = (YvsGrhGrilleMission) dao.save1(gm);
                    grilleM.setId(gm.getId());
                    grillesMissions.add(0, gm);
                } else {
                    dao.update(gm);
                    grillesMissions.set(grillesMissions.indexOf(gm), gm);
                }
                succes();
            } else {
                getErrorMessage("Veuillez entrer la catégorie et/ou le titre!");
            }
        } else {
            getErrorMessage("Veuillez entrer la catégorie et/ou le titre!");
        }
    }

    public void loadDetailCout(SelectEvent ev) {
        YvsGrhDetailGrilleFraiMission c = (YvsGrhDetailGrilleFraiMission) ev.getObject();
        cout = new DetailFraisMission(c.getMontantPrevu());
        cout.setId(c.getId());
        cout.setProportionelDuree(c.getProportionelDuree());
        cout.setSave(true);
        cout.setTypeCout(UtilGrh.buildBeanTypeCout(c.getTypeCout()));
        cout.setDateSave(c.getDateSave());
        update("bloc-param-01:form_edit_detail_grfM");
    }

    public void deleteLineCout(YvsGrhDetailGrilleFraiMission de) {
        try {
            de.setAuthor(currentUser);
            dao.delete(de);
            grilleM.getDetailsFrais().remove(de);
            grilleM.getDetailsFrais().remove(de);
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cette ligne ");
            getException("Lymytz Error >>>", ex);
        }
    }

    private YvsGrhTypeCout buildTypeCout(TypeCout c) {
        YvsGrhTypeCout t = new YvsGrhTypeCout();
        t.setId(c.getId());
        t.setLibelle(c.getLibelle());
        t.setSociete(currentAgence.getSociete());
        return t;
    }

    private YvsGrhDetailGrilleFraiMission buildCoutMission(DetailFraisMission c) {
        YvsGrhDetailGrilleFraiMission cm = new YvsGrhDetailGrilleFraiMission();
        cm.setId(c.getId());
        cm.setGrilleMission(new YvsGrhGrilleMission(grilleM.getId()));
        cm.setMontantPrevu(c.getMontant());
        cm.setAuthor(currentUser);
        cm.setTypeCout(buildTypeCout(c.getTypeCout()));
        cm.setProportionelDuree(c.isProportionelDuree());
        cm.setDateUpdate(new Date());
        cm.setDateSave(c.getDateSave());
        return cm;
    }

    public boolean saveElementGrille() {
        if (grilleM.getId() <= 0) {
            getErrorMessage("Aucune grille n'a été trouvé !");
            return false;
        }
        if (cout.getTypeCout().getId() > 0) {
            ManagedTypeCout service = (ManagedTypeCout) giveManagedBean(ManagedTypeCout.class);
            YvsGrhDetailGrilleFraiMission cm = buildCoutMission(cout);
            if (service != null) {
                int idx = service.getTypes().indexOf(cm.getTypeCout());
                if (idx >= 0) {
                    cm.setTypeCout(service.getTypes().get(idx));
                }
            }
            YvsGrhDetailGrilleFraiMission d = (YvsGrhDetailGrilleFraiMission) dao.loadOneByNameQueries("YvsGrhDetailGrilleFraiMission.findByIntituleCout", new String[]{"libelle", "grille"}, new Object[]{cm.getTypeCout().getLibelle(), new YvsGrhGrilleMission(grilleM.getId())});
            if (d == null) {
                cm.setId(null);
                cm = (YvsGrhDetailGrilleFraiMission) dao.save1(cm);
                cout.setId(cm.getId());
                cout.setTypeCout(UtilGrh.buildBeanTypeCout(cm.getTypeCout()));
//                cout.getTypeCout().setLibelle((listTypeCout.get(listTypeCout.indexOf(new YvsGrhTypeCout(cout.getTypeCout().getId())))).getLibelle());
                grilleM.getDetailsFrais().add(0, cm);
            } else {
                cm.setId(d.getId());
                dao.update(cm);
                int idx = grilleM.getDetailsFrais().indexOf(cm);
                if (idx >= 0) {
                    grilleM.getDetailsFrais().set(idx, cm);
                }
            }
            cout = new DetailFraisMission();
        }
        return true;
    }

    public void deleteGrille(YvsGrhGrilleMission gr) {
        try {
            gr.setAuthor(currentUser);
            dao.delete(gr);
            grillesMissions.remove(gr);
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cette ligne ");
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void resetFiche() {
        resetFiche(grilleM);
        grilleM.setCompteCharge(new Comptes());
        grilleM.getDetailsFrais().clear();
    }

    public void addParamActif(boolean actif) {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", actif, "=", "AND");
        paginator.addParam(p);
        loadAll(true);
    }

    public void addParamActifNoLimit(boolean actif) {
        grillesMissions = dao.loadNameQueries("YvsGrilleMission.findByActif", new String[]{"societe", "actif"}, new Object[]{currentAgence.getSociete(), actif});
    }

    public void selectionGrille(SelectEvent ev) {
        cloneObject(grilleM, UtilGrh.buildGrilleMission((YvsGrhGrilleMission) ev.getObject()));
    }

    @Override
    public boolean controleFiche(GrilleFraisMission bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GrilleFraisMission recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(GrilleFraisMission bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
