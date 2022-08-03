/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.salaire.YvsGrhDetailPrelevementEmps;
import yvs.entity.param.YvsGrhPlanPrelevement;
import yvs.grh.bean.Employe;
import yvs.util.Managed;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class ManagedPrevelementEmps extends Managed<PrelevementEmps, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{prelevementEmps}")
    private PrelevementEmps prelevementEmps;
    private PrelevementEmps detailPrelevement = new PrelevementEmps();
    private List<PrelevementEmps> listPrelevementEmps, listSelectPrelevementEmps;
    private Employe employe = new Employe();

    private boolean selectView, vueListe = true, optionUpdate, updatePlanPrelevement,
            activebaseTaux = false, activebaseMontant = false, activebaseNombreMois = false;
    private String nameBtnVue = "Nouveau";
    private double dette = 0;

    private YvsGrhPlanPrelevement entityPrevelement;
    private YvsGrhDetailPrelevementEmps entityDetail;

    public ManagedPrevelementEmps() {
        listPrelevementEmps = new ArrayList<>();
        listSelectPrelevementEmps = new ArrayList<>();
    }

    public PrelevementEmps getDetailPrelevement() {
        return detailPrelevement;
    }

    public double getDette() {
        return dette;
    }

    public void setDette(double dette) {
        this.dette = dette;
    }

    public void setDetailPrelevement(PrelevementEmps detailPrelevement) {
        this.detailPrelevement = detailPrelevement;
    }

    public YvsGrhDetailPrelevementEmps getEntityDetail() {
        return entityDetail;
    }

    public void setEntityDetail(YvsGrhDetailPrelevementEmps entityDetail) {
        this.entityDetail = entityDetail;
    }

    public boolean isActivebaseMontant() {
        return activebaseMontant;
    }

    public void setActivebaseMontant(boolean activebaseMontant) {
        this.activebaseMontant = activebaseMontant;
    }

    public boolean isActivebaseNombreMois() {
        return activebaseNombreMois;
    }

    public void setActivebaseNombreMois(boolean activebaseNombreMois) {
        this.activebaseNombreMois = activebaseNombreMois;
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

    public boolean isOptionUpdate() {
        return optionUpdate;
    }

    public void setOptionUpdate(boolean optionUpdate) {
        this.optionUpdate = optionUpdate;
    }

    public boolean isUpdatePlanPrelevement() {
        return updatePlanPrelevement;
    }

    public void setUpdatePlanPrelevement(boolean updatePlanPrelevement) {
        this.updatePlanPrelevement = updatePlanPrelevement;
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

    public PrelevementEmps getPrelevementEmps() {
        return prelevementEmps;
    }

    public List<PrelevementEmps> getListPrelevementEmps() {
        return listPrelevementEmps;
    }

    public void setListPrelevementEmps(List<PrelevementEmps> listPrelevementEmps) {
        this.listPrelevementEmps = listPrelevementEmps;
    }

    public List<PrelevementEmps> getListSelectPrelevementEmps() {
        return listSelectPrelevementEmps;
    }

    public void setListSelectPrelevementEmps(List<PrelevementEmps> listSelectPrelevementEmps) {
        this.listSelectPrelevementEmps = listSelectPrelevementEmps;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public YvsGrhPlanPrelevement getEntityPrevelement() {
        return entityPrevelement;
    }

    public void setEntityPrevelement(YvsGrhPlanPrelevement entityPrevelement) {
        this.entityPrevelement = entityPrevelement;
    }

    public void setPrelevementEmps(PrelevementEmps prelevementEmps) {
        this.prelevementEmps = prelevementEmps;
    }

    public YvsGrhDetailPrelevementEmps buildDetailPrelevementEmploye(PrelevementEmps p) {
        YvsGrhDetailPrelevementEmps pp = new YvsGrhDetailPrelevementEmps();
        if (p != null) {
            pp.setId(p.getId());
            pp.setValeur(p.getValeur());
            pp.setDatePrelevement(p.getDatePrelevement());
            pp.setStatutReglement(p.getStatut());
            pp.setPlanPrelevement(entityPrevelement);
        }
        return pp;
    }

    public YvsGrhPlanPrelevement buildPrelevementEmps(PrelevementEmps p) {
        YvsGrhPlanPrelevement pp = new YvsGrhPlanPrelevement();
        if (p != null) {
            pp.setId(p.getId());
//            pp.setBaseRetenue(p.getBase());
//            pp.setTaux(p.getTaux());
//            pp.setMontantPrelevement(p.getMontantPrelevement());
//            pp.setDatePrelevement(p.getDatePrelevement());
//            pp.setNombreMois(p.getNombreMois());
//            pp.setEmploye(new YvsEmployes(p.getEmploye().getId()));
        }
        return pp;
    }

    public void changeVue() {
        resetFiche();
        setSelectView(false);
        setUpdatePlanPrelevement(false);
        setVueListe(!isVueListe());
        setNameBtnVue((isVueListe()) ? "Nouveau" : "Liste");
        for (PrelevementEmps p : listPrelevementEmps) {
        }
        listSelectPrelevementEmps.clear();
        setOptionUpdate(false);
        update("form-prelevement-emps-00");
        update("entete-prelevement-emps-00");
    }

    @Override
    public PrelevementEmps recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(PrelevementEmps bean) {
        cloneObject(prelevementEmps, bean);
    }

    public void populateViewEmploye(Employe bean) {
        employe.setId(bean.getId());
        employe.setNom(bean.getNom());
        employe.setPrenom(bean.getPrenom());
        employe.setCodeEmploye(bean.getCodeEmploye());
        employe.setMatricule(bean.getMatricule());
        employe.setSolde(bean.getSolde());
        employe.setContrat(bean.getContrat());
    }

    public void selectPrelevementEmps(PrelevementEmps bean) {
        if (!listSelectPrelevementEmps.contains(bean)) {
            listSelectPrelevementEmps.add(bean);
        } else {
            listSelectPrelevementEmps.remove(bean);
        }
        if (listSelectPrelevementEmps.isEmpty()) {
            resetFiche();
        } else {
            populateView(listSelectPrelevementEmps.get(listSelectPrelevementEmps.size() - 1));
        }
        setSelectView(!listSelectPrelevementEmps.isEmpty());
        setOptionUpdate(listSelectPrelevementEmps.size() == 1);
        setUpdatePlanPrelevement(isOptionUpdate());
        update("form-prelevement-emps-00");
        update("entete-prelevement-emps-00");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            PrelevementEmps bean = (PrelevementEmps) ev.getObject();
            selectPrelevementEmps(bean);
        }
    }

    public void unLoadOnView(SelectEvent ev) {
        if (ev != null) {
            PrelevementEmps bean = (PrelevementEmps) ev.getObject();
            selectPrelevementEmps(bean);
        }
    }

    public void loadViewEmploye(SelectEvent ev) {
        Employe e = (Employe) ev.getObject();
        if (e != null) {
            populateViewEmploye(e);
        }
        closeDialog("dlgEmploye");
        update("blog-prelevement-emps-02");
    }
    

    @Override
    public void loadAll() {
        listPrelevementEmps.clear();
        String[] champ = new String[]{"agence"};
        Object[] val = new Object[]{currentAgence};
//        listPrelevementEmps = UtilGrh.buildBeanListPrelevementEmploye(dao.loadNameQueries("YvsGrhPrelevementEmps.findByAgence", champ, val));
    }

    @Override
    public boolean controleFiche(PrelevementEmps bean) {
//        if (bean.getBase() == 0) {
//            getMessage("Vous devez informer la base de prelevement", FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
//        if (bean.getTaux() == 0 && bean.getNombreMois() == 0){
//            getMessage("Vous devez informer le montant de prelevement", FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
//        if (bean.getDatePrelevement() == null) {
//            getMessage("Vous devez informer la date de prelevement", FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
//        if (bean.getEmploye().getId() == 0) {
//            getMessage("Vous devez selectionner l'employe", FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
        return true;
    }

    @Override
    public void updateBean() {
        setSelectView(false);
        setVueListe(false);
//        setActivebaseTaux(true);
        setNameBtnVue((isVueListe()) ? "Nouveau" : "Liste");
        update("form-prelevement-emps-00");
        update("entete-prelevement-emps-00");
    }

    @Override
    public boolean saveNew() {
        if (controleFiche(prelevementEmps)) {
            if (!isUpdatePlanPrelevement()) {
                entityPrevelement = buildPrelevementEmps(prelevementEmps);
                prelevementEmps.setId(entityPrevelement.getId());
                saveNewDetail();
                PrelevementEmps p = new PrelevementEmps();
                cloneObject(p, prelevementEmps);
                listPrelevementEmps.add(p);
            } else {
                entityPrevelement = buildPrelevementEmps(prelevementEmps);
//                entityPrevelement = (YvsGrhPrelevementEmps) dao.update(entityPrevelement);
                prelevementEmps.setId(entityPrevelement.getId());
                saveNewDetail();
                PrelevementEmps p = new PrelevementEmps();
                cloneObject(p, prelevementEmps);
                listPrelevementEmps.set(listPrelevementEmps.indexOf(prelevementEmps), p);
            }
            setUpdatePlanPrelevement(true);
            succes();
            return true;
        } else {
            return false;
        }
    }

    public boolean saveNewDetail() {
        Calendar dateSuivant = Calendar.getInstance();
        dateSuivant.setTime(prelevementEmps.getDatePrelevement());
        cloneObject(detailPrelevement, prelevementEmps);
//        if (!isUpdatePlanPrelevement()) {
//            for (int i = 0; i < prelevementEmps.getNombreMois(); i++) {
//                dateSuivant.add(Calendar.MONTH, 1);
//                if (i == (prelevementEmps.getNombreMois() - 1)) {
//                    if (prelevementEmps.getBase() == 1) {
////                        detailPrelevement.setMontantPrelevement((prelevementEmsetValeurntrat().getSalaireMensuel() - (prelevementEmps.getMontantPrelevement() * i)));
////    getValeure {
////                        detailPrelevement.setMontantPrelevement((prelevementEmps.getEmploye().getSoldesetValeur.getMontantPrelevement() * i)));
////                getValeur }
//                detailPrelevement.setDatePrelevement(dateSuivant.getTime());
//                detailPrelevement.setPrelevementRegle(false);
//                entityDetail = buildDetailPrelevementEmploye(detailPrelevement);
//                entityDetail = (YvsGrhDetailPrelevementEmps) dao.save1(entityDetail);
//                detailPrelevement.setId(entityDetail.getId());
//                PrelevementEmps p = new PrelevementEmps();
//                cloneObject(p, detailPrelevement);
//                prelevementEmps.getListDetails().add(p);
//            }
//        } else {
//            if (!prelevementEmps.getListDetails().isEmpty()) {
//                List<PrelevementEmps> list = new ArrayList<>();
//                for (PrelevementEmps p : prelevementEmps.getListDetails()) {
//                    list.add(p);
//                }
//                for (PrelevementEmps p : list) {
//                    entityDetail = buildDetailPrelevementEmploye(p);
//                    dao.delete(entityDetail);
//                    prelevementEmps.getListDetails().remove(p);
//                }
//            }
////            for (int i = 0; i < prelevementEmps.getNombreMois(); i++) {
////                dateSuivant.add(Calendar.MONTH, 1);
////                if (i == (prelevementEmps.getNombreMois() - 1)) {
////                    if (prelevementEmps.getBase() == 1) {
////                        detailPrelevement.setMontantPrelevement((prelevementEmps.getEmploye().getContrat().getSalaireMensuel()setValeuretMontantPrelevement() * i)));
////                    } else {getValeur    detailPrelevement.setMontantPrelevement((prelevementEmps.getEmploye().getSolde() - (prelevementEmps.getMontantPrelevement() * setValeur     }
////                }
////getValeurlPrelevement.setDatePrelevement(dateSuivant.getTime());
////                entityDetail = buildDetailPrelevementEmploye(detailPrelevement);
////                entityDetail = (YvsGrhDetailPrelevementEmps) dao.save1(entityDetail);
////                detailPrelevement.setId(entityDetail.getId());
////                PrelevementEmps p = new PrelevementEmps();
////                cloneObject(p, detailPrelevement);
////                prelevementEmps.getListDetails().add(p);
////            }
//        }}
//        }
//        
//        update("panel-prelevement-details");
        return true;
    }

    @Override
    public void deleteBean() {
        if (listSelectPrelevementEmps != null) {
            if (!listSelectPrelevementEmps.isEmpty()) {
                for (PrelevementEmps p : listSelectPrelevementEmps) {
                    entityPrevelement = buildPrelevementEmps(p);
                    dao.delete(entityPrevelement);
                    listPrelevementEmps.remove(p);
                }
                listSelectPrelevementEmps.clear();
            }
        }
        setSelectView(false);
        resetFiche();
        update("form-prelevement-emps-00");
        update("entete-prelevement-emps-00");
        succes();
    }

    @Override
    public void resetFiche() {
        resetFiche(prelevementEmps);
        prelevementEmps.setDatePrelevement(new Date());
        setUpdatePlanPrelevement(false);
        setActivebaseTaux(false);
        update("form-prelevement-emps-00");
        update("entete-prelevement-emps-00");
    }

    @Override
    public void resetPage() {
        setVueListe(true);
        setSelectView(false);
        setNameBtnVue((isVueListe()) ? "Nouveau" : "Liste");
        listSelectPrelevementEmps.clear();
        resetFiche();
    }

    public void choixBase(ValueChangeEvent ev) {
        if (ev != null) {
//            prelevementEmps.setTaux(0);
//            Short id = (short) ev.getNewValue();
//            if (id != 0) {
//                prelevementEmps.setNameBase((id == 1) ? "Salaire" : "Dette");
//                if (id == 2) {
//                    dette = prelevementEmps.getEmploye().getSolde();
//                } else if (id == 1) {
//                    dette = prelevementEmps.getEmploye().getContrat().getSalaireMensuel();
//                }
//            }
//            System.err.println("dette " + dette);
            update("blog-prelevement-emps-02");
            update("txt_montant_base");
        }
    }

    public void choixEmploye1(Employe ev) {
        if (ev != null) {
            populateViewEmploye(ev);
//            prelevementEmps.setEmploye(employe);
            update("blog-prelevement-emps-02");
        }
    }

    public void changeToBaseTaux() {
        if (isActivebaseTaux()) {
            setActivebaseMontant(false);
            setActivebaseNombreMois(false);
        }
        update("panel-prelevement-choix");
    }

    public void changeToBaseMontant() {
        if (isActivebaseMontant()) {
            setActivebaseTaux(false);
            setActivebaseNombreMois(false);
        }
        update("panel-prelevement-choix");
    }

    public void changeToBaseNombreMois() {
        if (isActivebaseNombreMois()) {
            setActivebaseMontant(false);
            setActivebaseTaux(false);
        }
        update("panel-prelevement-choix");
    }

    public void otherElementByTaux() {
//        double taux = 0, montant = 0;
//        Integer duree = 0;
//        if (dette != 0) {
//            taux = prelevementEmps.getTaux() / 100;
//            montant = dette * taux;
//            duree = (((dette / montant) - (new Double(dette / montant).intValue()) == 0) ? new Double(dette / montant).intValue() : new Double(dette / montant).intValue() + 1);
//        }
//        prelevementEmps.setMontantPrelevement(montant);
//        prelevementEmps.setNombreMois(duree);
//        update("panel-prelevement-choix");
//    }
//    
//setValeurElementByMontant() {
//        double taux = 0, montant = 0;
//        Integer duree = 0;
//        if (dette != 0) {
//            montant = prelevementEmps.getMontantPrelevement();
//            taux = montant / dette;
//            duree = (((getValeurew Double(dette / montant).intValue()) == 0) ? new Double(dette / montant).intValue() : new Double(dette / montant).intValue() + 1);
//        }
//        prelevementEmps.setMontantPrelevement(montant);
//        prelevementEmps.setNombreMois(duree);
//        prelevementEmps.setTaux(taux);
//        update("panel-prelevement-choixsetValeurblic void otherElementByDuree() {
//        double taux = 0, montant = 0, duree;
//        if (dette != 0) {
//            duree = prelevementEmps.getNombreMois();
//            montant = dette / duree;
//            taux = montant / dette;
//        }
//        prelevementEmps.setMontantPrelevement(montant);
//        prelevementEmps.setTaux(taux);
//        update("panel-prelevement-choix");
//    }
//    
//    public void resetInfos() {
//        dettsetValeurementEmps.setBase((short) 0);
//        prelevementEmps.setTaux(0);
//        prelevementEmps.setMontantPrelevement(0);
//        prelevementEmps.setNombreMois(0);
//        setActivebaseMontant(false);
//        setActivebaseNombreMois(false);
//        setActivebaseTaux(false);
// setValeurrelevement-emps-02");
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
