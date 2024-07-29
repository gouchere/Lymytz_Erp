/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.sanction;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.activite.YvsGrhSanctionEmps;
import yvs.entity.grh.param.YvsGrhFauteSanction;
import yvs.entity.grh.param.YvsGrhSanction;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.bean.Employe;
import yvs.util.Managed;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class ManagedSanctionEmps extends Managed<SanctionEmps, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{sanctionEmps}")
    SanctionEmps sanctionEmploye;
    private List<SanctionEmps> listSanctionEmps, listSelectSanctionEmps;
    private List<Sanction> listSanction;
    private FauteSanction fauteSanction = new FauteSanction();
    private List<FauteSanction> listFauteSanction;
    private boolean viewliste = true, selectSanctionEmps, updateSanctionEmps, optionUpdate;
    private Date dateSearch = new Date();

    public ManagedSanctionEmps() {
        listSanctionEmps = new ArrayList<>();
        listSelectSanctionEmps = new ArrayList<>();
        listSanction = new ArrayList<>();
        listFauteSanction = new ArrayList<>();
    }

    public FauteSanction getFauteSanction() {
        return fauteSanction;
    }

    public void setFauteSanction(FauteSanction fauteSanction) {
        this.fauteSanction = fauteSanction;
    }

    public List<FauteSanction> getListFauteSanction() {
        return listFauteSanction;
    }

    public void setListFauteSanction(List<FauteSanction> listFauteSanction) {
        this.listFauteSanction = listFauteSanction;
    }

    public Date getDateSearch() {
        return dateSearch;
    }

    public void setDateSearch(Date dateSearch) {
        this.dateSearch = dateSearch;
    }

    public SanctionEmps getSanctionEmploye() {
        return sanctionEmploye;
    }

    public void setSanctionEmploye(SanctionEmps sanctionEmploye) {
        this.sanctionEmploye = sanctionEmploye;
    }

    public List<SanctionEmps> getListSanctionEmps() {
        return listSanctionEmps;
    }

    public void setListSanctionEmps(List<SanctionEmps> listSanctionEmps) {
        this.listSanctionEmps = listSanctionEmps;
    }

    public List<SanctionEmps> getListSelectSanctionEmps() {
        return listSelectSanctionEmps;
    }

    public void setListSelectSanctionEmps(List<SanctionEmps> listSelectSanctionEmps) {
        this.listSelectSanctionEmps = listSelectSanctionEmps;
    }

    public List<Sanction> getListSanction() {
        return listSanction;
    }

    public void setListSanction(List<Sanction> listSanction) {
        this.listSanction = listSanction;
    }

    public boolean isViewliste() {
        return viewliste;
    }

    public void setViewliste(boolean viewliste) {
        this.viewliste = viewliste;
    }

    public boolean isSelectSanctionEmps() {
        return selectSanctionEmps;
    }

    public void setSelectSanctionEmps(boolean selectSanctionEmps) {
        this.selectSanctionEmps = selectSanctionEmps;
    }

    public boolean isUpdateSanctionEmps() {
        return updateSanctionEmps;
    }

    public void setUpdateSanctionEmps(boolean updateSanctionEmps) {
        this.updateSanctionEmps = updateSanctionEmps;
    }

    public boolean isOptionUpdate() {
        return optionUpdate;
    }

    public void setOptionUpdate(boolean optionUpdate) {
        this.optionUpdate = optionUpdate;
    }

    public YvsGrhSanctionEmps buildBeanSanctionEmps(SanctionEmps bean) {
        YvsGrhSanctionEmps entity = new YvsGrhSanctionEmps();
        if (bean != null) {
            entity.setId(bean.getId());
            entity.setActif(bean.isActif());
            entity.setDateDebut((bean.getDateDebut() != null) ? bean.getDateDebut() : new Date());
            entity.setDateFin((bean.getDateFin() != null) ? bean.getDateFin() : new Date());
            entity.setDateSanction((bean.getDateSanction() != null) ? bean.getDateSanction() : new Date());
            entity.setSupp(bean.isSupp());
            entity.setSanction(new YvsGrhSanction(bean.getSanction().getId()));
            entity.setEmploye(new YvsGrhEmployes(bean.getEmploye().getId()));
        }
        return entity;
    }

    @Override
    public boolean controleFiche(SanctionEmps bean) {
        if (bean.getDateDebut() == null) {
            getErrorMessage("Vous devez entrer la date de debut");
            return false;
        }
        if (bean.getDateFin() == null) {
            getErrorMessage("Vous devez entrer la date de fin");
            return false;
        }
        if ((bean.getEmploye() != null) ? bean.getEmploye().getId() == 0 : true) {
            getErrorMessage("Vous devez selectionner un employe");
            return false;
        }
        if ((bean.getSanction() != null) ? bean.getSanction().getId() == 0 : true) {
            getErrorMessage("Vous devez indiquer une sanctio");
            return false;
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if (listSelectSanctionEmps != null) {
                if (!listSelectSanctionEmps.isEmpty()) {
                    for (SanctionEmps s : listSelectSanctionEmps) {
                        dao.delete(new YvsGrhSanctionEmps(s.getId()));
                        listSanctionEmps.remove(s);
                    }
                    resetFiche();
                    succes();
                    update("body_sanction_emps");
                    update("head_sanction_emps");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            Logger.getLogger(ManagedSanctionEmps.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateBean() {
        setSelectSanctionEmps(false);
        setViewliste(false);
        setOptionUpdate(false);
        update("body_sanction_emps");
        update("head_sanction_emps");
    }

    @Override
    public SanctionEmps recopieView() {
        SanctionEmps bean = new SanctionEmps();
        bean.setActif(sanctionEmploye.isActif());
        bean.setDateDebut(sanctionEmploye.getDateDebut());
        bean.setDateFin(sanctionEmploye.getDateFin());
        bean.setDateSanction(sanctionEmploye.getDateSanction());
        bean.setEmploye(sanctionEmploye.getEmploye());
        bean.setId(sanctionEmploye.getId());
        bean.setSupp(sanctionEmploye.isSupp());
        bean.setSanction(sanctionEmploye.getSanction());
        return bean;
    }

    @Override
    public void populateView(SanctionEmps bean) {
        cloneObject(sanctionEmploye, bean);
        cloneObject(fauteSanction, sanctionEmploye.getSanction().getFaute());
        loadAllDecisionByFaute();
    }

    public void populateViewEmploye(Employe bean) {
        sanctionEmploye.getEmploye().setId(bean.getId());
        sanctionEmploye.getEmploye().setNom(bean.getNom());
        sanctionEmploye.getEmploye().setPrenom(bean.getPrenom());
        sanctionEmploye.getEmploye().setCodeEmploye(bean.getCodeEmploye());
        sanctionEmploye.getEmploye().setMatricule(bean.getMatricule());
    }

    public void selectSanctionEmploye(SanctionEmps bean) {
        bean.setSelectActif(!bean.isSelectActif());
        listSanctionEmps.get(listSanctionEmps.indexOf(bean)).setSelectActif(bean.isSelectActif());
        if (listSelectSanctionEmps.contains(bean)) {
            listSelectSanctionEmps.remove(bean);
        } else {
            listSelectSanctionEmps.add(bean);
        }
        if (listSelectSanctionEmps.isEmpty()) {
            resetFiche();
        } else {
            populateView(listSelectSanctionEmps.get(listSelectSanctionEmps.size() - 1));
        }
        setSelectSanctionEmps(!listSelectSanctionEmps.isEmpty());
        setUpdateSanctionEmps(isSelectSanctionEmps());
        setOptionUpdate(listSelectSanctionEmps.size() == 1);
        update("body_sanction_emps");
        update("head_sanction_emps");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev.getObject() != null) {
            SanctionEmps bean = (SanctionEmps) ev.getObject();
            selectSanctionEmploye(bean);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        if (ev.getObject() != null) {
            SanctionEmps bean = (SanctionEmps) ev.getObject();
            selectSanctionEmploye(bean);
        }
    }

    public void loadViewEmploye(SelectEvent ev) {
        Employe e = (Employe) ev.getObject();
        if (e != null) {
            choixEmploye1(e);
        }
    }

    public void choixEmploye1(Employe ev) {
        if (ev != null) {
            populateViewEmploye(ev);
            update("form_sanction_emps_01");
        }
    }

    @Override
    public void loadAll() {
        listSanctionEmps.clear();
        String[] champ = new String[]{"dateFin"};
        Object[] val = new Object[]{dateSearch};
        listSanctionEmps = UtilGrh.buildBeanListSanctionEmps(dao.loadNameQueries("YvsGrhSanctionEmps.findByEnCours", champ, val));
    }

    public void loadAllDecisionByFaute() {
        listSanction.clear();
        String[] champ = new String[]{"faute"};
        Object[] val = new Object[]{new YvsGrhFauteSanction(fauteSanction.getId())};
        listSanction = UtilGrh.buildBeanListSanction(dao.loadNameQueries("YvsGrhSanction.findByFaute", champ, val));
    }

    public void loadAllFaute() {
        listFauteSanction.clear();
        String[] champ = new String[]{"societe"};
        Object[] val = new Object[]{currentAgence.getSociete()};
        listFauteSanction = UtilGrh.buildBeanListFauteSanction(dao.loadNameQueries("YvsGrhFauteSanction.findAll", champ, val));
    }

    @Override
    public void changeView() {
        setViewliste(!viewliste);
        resetFiche();
        update("body_sanction_emps");
        update("head_sanction_emps");
    }

    @Override
    public void resetPage() {
        if (listSanctionEmps != null) {
            for (SanctionEmps s : listSanctionEmps) {
                listSanctionEmps.get(listSanctionEmps.indexOf(s)).setSelectActif(false);
            }
        }
        listSelectSanctionEmps.clear();
    }

    @Override
    public void resetFiche() {
        resetFiche(sanctionEmploye);
        sanctionEmploye.setSanction(new Sanction());
        sanctionEmploye.setEmploye(new Employe());
        setSelectSanctionEmps(false);
        setUpdateSanctionEmps(false);
        setOptionUpdate(false);
        resetPage();
        update("form_sanction_emps_01");
    }

    @Override
    public boolean saveNew() {
        String action = isUpdateSanctionEmps() ? "Modification" : "Insertion";
        try {
            sanctionEmploye.setDateSanction(new Date());
            sanctionEmploye.setActif(true);
            sanctionEmploye.setSupp(false);
            SanctionEmps bean = recopieView();
            if (controleFiche(bean)) {
                YvsGrhSanctionEmps entity = buildBeanSanctionEmps(bean);
                if (!isUpdateSanctionEmps()) {
                    entity.setId(null);
                    entity = (YvsGrhSanctionEmps) dao.save1(entity);
                    sanctionEmploye.setId(entity.getId());
                    bean.setId(entity.getId());
                    listSanctionEmps.add(bean);
                } else {
                    dao.update(entity);
                    listSanctionEmps.set(listSanctionEmps.indexOf(sanctionEmploye), bean);
                }
                succes();
                setUpdateSanctionEmps(true);
                update("form_sanction_emps_01");
            }
            return true;
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            Logger.getLogger(ManagedSanctionEmps.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void chooseSanction(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            int id = (int) ev.getNewValue();
            if (id > 0) {
                sanctionEmploye.getSanction().setId(id);
                sanctionEmploye.getSanction().getDecision().setLibelle(listSanction.get(listSanction.indexOf(sanctionEmploye.getSanction())).getDecision().getLibelle());
                sanctionEmploye.getSanction().getFaute().setLibelle(listSanction.get(listSanction.indexOf(sanctionEmploye.getSanction())).getFaute().getLibelle());
                sanctionEmploye.getSanction().getDecision().setDuree(listSanction.get(listSanction.indexOf(sanctionEmploye.getSanction())).getDecision().getDuree());
            }
        }
    }

    public void chooseFauteSanction(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            int id = (int) ev.getNewValue();
            if (id > 0) {
                fauteSanction.setId(id);
                loadAllDecisionByFaute();
            } else {
                listSanction.clear();
            }
            update("form_sanction_emps_01");
        }
    }
}
