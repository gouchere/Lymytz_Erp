/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.param.YvsGrhPlanPrelevement;
import yvs.grh.UtilGrh;
import yvs.util.Managed;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class ManagedPlanPrelevement extends Managed<PlanPrelevement, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{planPrelevement}")
    private PlanPrelevement planPrelevement;
    private PlanPrelevement detailPlan = new PlanPrelevement();
    private List<PlanPrelevement> listPlanPrelevement, listDetailPlanPrelevement, listSelectPlanPrelevement;

    private boolean selectView, selectViewDetail, vueListe = true, optionUpdate, updatePlanPrelevement, activebaseTaux = false;
    private String nameBtnVue = "Nouveau", nameBtnVueDetail = "Ajouter";
    private Integer idTemp = 0;

    private YvsGrhPlanPrelevement entityPlan;
//    private YvsGrhDetailPlanPrelevement entityDetail;

    private String chaineSelectPlan;

    public ManagedPlanPrelevement() {
        listPlanPrelevement = new ArrayList<>();
        listSelectPlanPrelevement = new ArrayList<>();
        listDetailPlanPrelevement = new ArrayList<>();
    }

    public String getChaineSelectPlan() {
        return chaineSelectPlan;
    }

    public void setChaineSelectPlan(String chaineSelectPlan) {
        this.chaineSelectPlan = chaineSelectPlan;
    }

    public Integer getIdTemp() {
        return idTemp;
    }

    public void setIdTemp(Integer idTemp) {
        this.idTemp = idTemp;
    }

//    public YvsGrhDetailPlanPrelevement getEntityDetail() {
//        return entityDetail;
//    }
//
//    public void setEntityDetail(YvsGrhDetailPlanPrelevement entityDetail) {
//        this.entityDetail = entityDetail;
//    }
    public PlanPrelevement getDetailPlan() {
        return detailPlan;
    }

    public void setDetailPlan(PlanPrelevement detailPlan) {
        this.detailPlan = detailPlan;
    }

    public boolean isSelectViewDetail() {
        return selectViewDetail;
    }

    public void setSelectViewDetail(boolean selectViewDetail) {
        this.selectViewDetail = selectViewDetail;
    }

    public String getNameBtnVueDetail() {
        return nameBtnVueDetail;
    }

    public void setNameBtnVueDetail(String nameBtnVueDetail) {
        this.nameBtnVueDetail = nameBtnVueDetail;
    }

    public List<PlanPrelevement> getListDetailPlanPrelevement() {
        return listDetailPlanPrelevement;
    }

    public void setListDetailPlanPrelevement(List<PlanPrelevement> listDetailPlanPrelevement) {
        this.listDetailPlanPrelevement = listDetailPlanPrelevement;
    }

    public boolean isActivebaseTaux() {
        return activebaseTaux;
    }

    public void setActivebaseTaux(boolean activebaseTaux) {
        this.activebaseTaux = activebaseTaux;
    }

    public String getNameBtnVue() {
        return nameBtnVue;
    }

    public void setNameBtnVue(String nameBtnVue) {
        this.nameBtnVue = nameBtnVue;
    }

    public boolean isUpdatePlanPrelevement() {
        return updatePlanPrelevement;
    }

    public void setUpdatePlanPrelevement(boolean updatePlanPrelevement) {
        this.updatePlanPrelevement = updatePlanPrelevement;
    }

    public boolean isOptionUpdate() {
        return optionUpdate;
    }

    public void setOptionUpdate(boolean optionUpdate) {
        this.optionUpdate = optionUpdate;
    }

    public YvsGrhPlanPrelevement getEntityPlan() {
        return entityPlan;
    }

    public void setEntityPlan(YvsGrhPlanPrelevement entityPlan) {
        this.entityPlan = entityPlan;
    }

    public List<PlanPrelevement> getListPlanPrelevement() {
        return listPlanPrelevement;
    }

    public void setListPlanPrelevement(List<PlanPrelevement> listPlanPrelevement) {
        this.listPlanPrelevement = listPlanPrelevement;
    }

    public List<PlanPrelevement> getListSelectPlanPrelevement() {
        return listSelectPlanPrelevement;
    }

    public void setListSelectPlanPrelevement(List<PlanPrelevement> listSelectPlanPrelevement) {
        this.listSelectPlanPrelevement = listSelectPlanPrelevement;
    }

    public boolean isSelectView() {
        return selectView;
    }

    public void setSelectView(boolean selectView) {
        this.selectView = selectView;
    }

    public boolean isVueListe() {
        return vueListe;
    }

    public void setVueListe(boolean vueListe) {
        this.vueListe = vueListe;
    }

    public PlanPrelevement getPlanPrelevement() {
        return planPrelevement;
    }

    public void setPlanPrelevement(PlanPrelevement planPrelevement) {
        this.planPrelevement = planPrelevement;
    }

//    public YvsGrhDetailPlanPrelevement buildDetailPlanPrelevement(PlanPrelevement p) {
//        YvsGrhDetailPlanPrelevement pp = new YvsGrhDetailPlanPrelevement();
//        if (p != null) {
//            pp.setId(p.getId());
//            pp.setTaux(p.getBaseTaux());
//            pp.setBorneMaximal(p.getBorneMaximal());
//            pp.setBorneMinimal(p.getBorneMinimal());
//            pp.setPlanPrelevement(entityPlan);
//        }
//        return pp;
//    }
    public YvsGrhPlanPrelevement buildPlanPrelevement(PlanPrelevement p) {
        YvsGrhPlanPrelevement pp = new YvsGrhPlanPrelevement();
        if (p != null) {
            pp.setId(p.getId());
            pp.setReferencePlan(p.getReference());
            pp.setDateSave(p.getDateSave());
            pp.setDateUpdate(new Date());
            pp.setAuthor(currentUser);
            pp.setSociete(currentAgence.getSociete());
//            pp.setBaseInterval(p.getBaseInterval());
//            pp.setSociete(currentAgence.getSociete());
        }
        return pp;
    }

//    public void changeVue() {
//        resetFiche();
//        setSelectView(false);
//        setUpdatePlanPrelevement(false);
//        setVueListe(!isVueListe());
//        setNameBtnVue((isVueListe()) ? "Nouveau" : "Liste");
//        for (PlanPrelevement p : listPlanPrelevement) {
//            listPlanPrelevement.get(listPlanPrelevement.indexOf(p)).setSelectActif(false);
//        }
//        update("form-plan-prelevement-00");
//        update("entete-plan-prelevement-00");
//    }
    @Override
    public PlanPrelevement recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(PlanPrelevement bean) {
        cloneObject(planPrelevement, bean);
        listDetailPlanPrelevement.clear();

    }

    public void populateViewDetail(PlanPrelevement bean) {
        cloneObject(detailPlan, bean);
        setSelectViewDetail(true);
        setNameBtnVueDetail((isSelectViewDetail() ? "Modifier" : "Ajouter"));
        update("panel-detail-plan-add");
        update("tab-detail-plan");
    }

    public void selectPlanPrelevement(PlanPrelevement bean) {
//        bean.setSelectActif(!bean.isSelectActif());
//        listPlanPrelevement.get(listPlanPrelevement.indexOf(bean)).setSelectActif(bean.isSelectActif());
//        if (!listSelectPlanPrelevement.contains(bean)) {
//            listSelectPlanPrelevement.add(bean);
//        } else {
//            listSelectPlanPrelevement.remove(bean);
//        }
//        if (listSelectPlanPrelevement.isEmpty()) {
//            resetFiche();
//        } else {
        populateView(listSelectPlanPrelevement.get(listSelectPlanPrelevement.size() - 1));
        setUpdatePlanPrelevement(true);
//        }
//        setSelectView(!listSelectPlanPrelevement.isEmpty());
//        setOptionUpdate(listSelectPlanPrelevement.size() == 1);        
//        update("form-plan-prelevement-00");
//        update("entete-plan-prelevement-00");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            PlanPrelevement bean = (PlanPrelevement) ev.getObject();
            selectPlanPrelevement(bean);
        }
    }

//    public void unLoadOnView(UnselectEvent ev) {
//        if (ev != null) {
//            PlanPrelevement bean = (PlanPrelevement) ev.getObject();
//            selectPlanPrelevement(bean);
//        }
//    }
    public void loadOnViewDetail(SelectEvent ev) {
        if (ev != null) {
            PlanPrelevement bean = (PlanPrelevement) ev.getObject();
            populateViewDetail(bean);
        }
    }

    public void unLoadOnViewDetail(UnselectEvent ev) {
        resetFicheDetail();
        setSelectViewDetail(false);
        setNameBtnVueDetail((isSelectViewDetail() ? "Modifier" : "Ajouter"));
        update("panel-detail-plan-add");
        update("tab-detail-plan");
    }

    @Override
    public void loadAll() {
        listPlanPrelevement.clear();
        listPlanPrelevement = UtilGrh.buildBeanListPlanPrelevement(dao.loadNameQueries("YvsPlanPrelevement.findBySociete",
                new String[]{"societe"}, new Object[]{currentAgence.getSociete()}));
    }

    @Override
    public void resetFiche() {
        resetFiche(planPrelevement);
        setUpdatePlanPrelevement(false);
        setActivebaseTaux(false);
        listDetailPlanPrelevement.clear();
        resetFicheDetail();
        update("form_prelevementG");
    }

    public void resetFicheDetail() {
        resetFiche(detailPlan);
        setSelectViewDetail(false);
        update("panel-detail-plan-add");
    }

    @Override
    public void resetPage() {
        listSelectPlanPrelevement.clear();
        listDetailPlanPrelevement.clear();
        resetFiche();
    }

    @Override
    public boolean controleFiche(PlanPrelevement bean) {
//        if (bean.getBaseInterval() == 0) {
//            getMessage("Vous devez renseigner la base d'interval", FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
        return true;
    }

    public boolean controleFicheDetail(PlanPrelevement bean) {
//        if (bean.getValeur() < 0) {
//            getMessage("Vous devez renseigner la base de taux", FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
//        if (bean.getBorneMaximal() <= 0) {
//            getMessage("Vous devez renseigner la borne maximal", FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
//        if (bean.getBorneMinimal() < 0) {
//            getMessage("Vous devez renseigner la borne minimal", FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
        return true;
    }

    @Override
    public void updateBean() {
//        setSelectView(false);
//        setVueListe(false);
//        setActivebaseTaux(true);
//        setNameBtnVue((isVueListe()) ? "Nouveau" : "Liste");
//        update("form-plan-prelevement-00");
//        update("entete-plan-prelevement-00");
    }

    @Override
    public boolean saveNew() {
        if (controleFiche(planPrelevement)) {
            if (!isUpdatePlanPrelevement()) {
                entityPlan = buildPlanPrelevement(planPrelevement);
                entityPlan.setAuthor(currentUser);
                entityPlan.setId(null);
                entityPlan = (YvsGrhPlanPrelevement) dao.save1(entityPlan);
                planPrelevement.setId(entityPlan.getId());
                for (PlanPrelevement d : listDetailPlanPrelevement) {
//                    entityDetail = buildDetailPlanPrelevement(d);                    
//                    entityDetail = (YvsGrhDetailPlanPrelevement) dao.save1(entityDetail);
//                    d.setId(entityDetail.getId());
                }
                PlanPrelevement p = new PlanPrelevement();
                cloneObject(p, planPrelevement);
                listPlanPrelevement.add(p);
            } else {
                entityPlan = buildPlanPrelevement(planPrelevement);
                entityPlan = (YvsGrhPlanPrelevement) dao.update(entityPlan);
                planPrelevement.setId(entityPlan.getId());
//                for (PlanPrelevement d : list) {
//                    if (!listDetailPlanPrelevement.contains(d)) {
//                        entityDetail = buildDetailPlanPrelevement(d);
//                        dao.delete(entityDetail);
//                        planPrelevement.getListDetail().remove(d);
//                    }
//                }
//                for (PlanPrelevement d : listDetailPlanPrelevement) {
//                    entityDetail = buildDetailPlanPrelevement(d);
//                    if (planPrelevement.getListDetail().contains(d)) {
//                        entityDetail = (YvsGrhDetailPlanPrelevement) dao.update(entityDetail);
//                        d.setId(entityDetail.getId());
//                        planPrelevement.getListDetail().set(planPrelevement.getListDetail().indexOf(d), d);
//                    } else {
//                        entityDetail = (YvsGrhDetailPlanPrelevement) dao.save1(entityDetail);
//                        d.setId(entityDetail.getId());
//                        planPrelevement.getListDetail().add(d);
//                    }
//                }
                PlanPrelevement p = new PlanPrelevement();
                cloneObject(p, planPrelevement);
                listPlanPrelevement.set(listPlanPrelevement.indexOf(planPrelevement), p);
            }
            setUpdatePlanPrelevement(true);
            succes();
            actionOpenOrResetAfter(this);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteBean() {

        if (chaineSelectPlan != null) {
            String numroLine[] = chaineSelectPlan.split("-");
            List<String> l = new ArrayList<>();
            try {
                int index;
                for (String numroLine1 : numroLine) {
                    index = Integer.parseInt(numroLine1);
                    dao.delete(new YvsGrhPlanPrelevement(listPlanPrelevement.get(index).getId()));
                    listPlanPrelevement.remove(index);
                    l.add(numroLine1);
                }
                chaineSelectPlan = "";
                succes();
            } catch (NumberFormatException ex) {
                chaineSelectPlan = "";
                for (String numroLine1 : numroLine) {
                    if (!l.contains(numroLine1)) {
                        chaineSelectPlan += numroLine1 + "-";
                    }
                }
                getErrorMessage("Impossible de terminer cette opération ! des élément de votre sélection doivent être encore en liaison");
            }
        }
//        if (listSelectPlanPrelevement != null) {
//            if (!listSelectPlanPrelevement.isEmpty()) {
//                for (PlanPrelevement p : listSelectPlanPrelevement) {
//                    entityPlan = buildPlanPrelevement(p);
//                    dao.delete(entityPlan);
//                    listPlanPrelevement.remove(p);
//                }
//                listSelectPlanPrelevement.clear();
//            }
//        }
//        setSelectView(false);
//        resetFiche();
//        update("form-plan-prelevement-00");
//        update("entete-plan-prelevement-00");
//        succes();
    }

    public void choixBase(ValueChangeEvent ev) {
        if (ev != null) {
            planPrelevement.setValeur(0);
            Short id = (short) ev.getNewValue();
            if (id == 0) {
                setActivebaseTaux(false);
            } else {
                setActivebaseTaux(true);
                planPrelevement.setNameBaseInterval((id == 1) ? "Salaire" : "Dette");
            }
        }
    }

    public void addDetailPlan() {
        if (controleFicheDetail(detailPlan)) {
            if (!isSelectViewDetail()) {
                detailPlan.setId(idTemp);
                while (listDetailPlanPrelevement.contains(detailPlan)) {
                    idTemp += 1;
                    detailPlan.setId(idTemp);
                }
                PlanPrelevement d = new PlanPrelevement();
                cloneObject(d, detailPlan);
                listDetailPlanPrelevement.add(d);
            } else {
                PlanPrelevement d = new PlanPrelevement();
                cloneObject(d, detailPlan);
                listDetailPlanPrelevement.set(listDetailPlanPrelevement.indexOf(detailPlan), d);
            }
            resetFicheDetail();
        }
    }

    public void removeDetailPlan() {
        if (detailPlan != null) {
            listDetailPlanPrelevement.remove(detailPlan);
            resetFicheDetail();
            update("tab-detail-plan");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
    }
}
