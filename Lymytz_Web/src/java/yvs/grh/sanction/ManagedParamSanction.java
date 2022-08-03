/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.sanction;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.param.YvsGrhDecisionSanction;
import yvs.entity.grh.param.YvsGrhFauteSanction;
import yvs.entity.grh.param.YvsGrhSanction;
import yvs.grh.UtilGrh;
import yvs.util.Managed;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class ManagedParamSanction extends Managed<Sanction, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{sanction}")
    private Sanction sanction;
    private DecisionSanction decisionSanction = new DecisionSanction();
    private FauteSanction fauteSanction = new FauteSanction();

    private List<DecisionSanction> listDecisionSanction;
    private List<FauteSanction> listFauteSanction;
    private List<Sanction> listSanction, listSelectSanction;

    private boolean viewList = true, selectSanction, updateSanction, optionUpdate;

    public ManagedParamSanction() {
        listDecisionSanction = new ArrayList<>();
        listFauteSanction = new ArrayList<>();
        listSelectSanction = new ArrayList<>();
        listSanction = new ArrayList<>();
    }

    public List<Sanction> getListSanction() {
        return listSanction;
    }

    public void setListSanction(List<Sanction> listSanction) {
        this.listSanction = listSanction;
    }

    public List<Sanction> getListSelectSanction() {
        return listSelectSanction;
    }

    public void setListSelectSanction(List<Sanction> listSelectSanction) {
        this.listSelectSanction = listSelectSanction;
    }

    public Sanction getSanction() {
        return sanction;
    }

    public void setSanction(Sanction sanction) {
        this.sanction = sanction;
    }

    public DecisionSanction getDecisionSanction() {
        return decisionSanction;
    }

    public void setDecisionSanction(DecisionSanction decisionSanction) {
        this.decisionSanction = decisionSanction;
    }

    public FauteSanction getFauteSanction() {
        return fauteSanction;
    }

    public void setFauteSanction(FauteSanction fauteSanction) {
        this.fauteSanction = fauteSanction;
    }

    public List<DecisionSanction> getListDecisionSanction() {
        return listDecisionSanction;
    }

    public void setListDecisionSanction(List<DecisionSanction> listDecisionSanction) {
        this.listDecisionSanction = listDecisionSanction;
    }

    public List<FauteSanction> getListFauteSanction() {
        return listFauteSanction;
    }

    public void setListFauteSanction(List<FauteSanction> listFauteSanction) {
        this.listFauteSanction = listFauteSanction;
    }

    public boolean isViewList() {
        return viewList;
    }

    public void setViewList(boolean viewList) {
        this.viewList = viewList;
    }

    public boolean isSelectSanction() {
        return selectSanction;
    }

    public void setSelectSanction(boolean selectSanction) {
        this.selectSanction = selectSanction;
    }

    public boolean isUpdateSanction() {
        return updateSanction;
    }

    public void setUpdateSanction(boolean updateSanction) {
        this.updateSanction = updateSanction;
    }

    public boolean isOptionUpdate() {
        return optionUpdate;
    }

    public void setOptionUpdate(boolean optionUpdate) {
        this.optionUpdate = optionUpdate;
    }

    public YvsGrhDecisionSanction buildDecisionSanction(DecisionSanction d) {
        YvsGrhDecisionSanction r = new YvsGrhDecisionSanction();
        if (d != null) {
            r.setActif(d.isActif());
            r.setDescriptionMotif(d.getDescriptionMotif());
            r.setLibelle(d.getLibelle());
            r.setDuree(d.getDuree());
            r.setId(d.getId());
            r.setSociete(currentAgence.getSociete());
        }
        return r;
    }

    public YvsGrhFauteSanction buildFauteSanction(FauteSanction d) {
        YvsGrhFauteSanction r = new YvsGrhFauteSanction();
        if (d != null) {
            r.setLibelle(d.getLibelle());
            r.setId(d.getId());
            r.setSociete(currentAgence.getSociete());
        }
        return r;
    }

    public YvsGrhSanction buildSanction(Sanction d) {
        YvsGrhSanction r = new YvsGrhSanction();
        if (d != null) {
            r.setId(d.getId());
            r.setActif(true);
            r.setPoint(d.getPoint());
            r.setSupp(false);
            r.setDescription(d.getDescription());
            r.setCode(d.getCode());
            if ((d.getFaute() != null) ? d.getFaute().getId() != 0 : false) {
                r.setFaute(new YvsGrhFauteSanction(d.getFaute().getId()));
            }
            if ((d.getDecision() != null) ? d.getDecision().getId() != 0 : false) {
                r.setDecision(new YvsGrhDecisionSanction(d.getDecision().getId()));
            }
        }
        return r;
    }

    @Override
    public boolean controleFiche(Sanction bean) {
        if (bean.getCode() == null || bean.getCode().equals("")) {
            getErrorMessage("Vous devez entrer le code");
            return false;
        }
        if (bean.getDescription() == null || bean.getDescription().equals("")) {
            getErrorMessage("Vous devez entrer la designation");
            return false;
        }
        if (bean.getPoint() == 0) {
            getErrorMessage("Vous devez entrer le nomdre de point");
            return false;
        }
        if ((bean.getFaute() != null) ? bean.getFaute().getId() == 0 : true) {
            getErrorMessage("Vous devez indiquer la faute");
            return false;
        }
        if ((bean.getDecision() != null) ? bean.getDecision().getId() == 0 : true) {
            getErrorMessage("Vous devez indiquer la decision");
            return false;
        }
        return true;
    }

    public boolean controleFicheDecision(DecisionSanction bean) {
        if (bean.getLibelle() == null || bean.getLibelle().equals("")) {
            getErrorMessage("Vous devez entrer le libelle");
            return false;
        }
        if (bean.getDescriptionMotif() == null || bean.getDescriptionMotif().equals("")) {
            getErrorMessage("Vous devez entrer la description");
            return false;
        }
        if (bean.getDuree() == 0) {
            getErrorMessage("Vous devez entrer la duree");
            return false;
        }
        return true;
    }

    public boolean controleFicheFaute(FauteSanction bean) {
        if (bean.getLibelle() == null || bean.getLibelle().equals("")) {
            getErrorMessage("Vous devez entrer le libelle");
            return false;
        }
        return true;
    }

    @Override
    public Sanction recopieView() {
        Sanction bean = new Sanction(sanction.getId());
        bean.setActif(sanction.isActif());
        bean.setCode(sanction.getCode());
        bean.setDecision(sanction.getDecision());
        bean.setDescription(sanction.getDescription());
        bean.setFaute(sanction.getFaute());
        bean.setPoint(sanction.getPoint());
        bean.setSupp(sanction.isSupp());
        return bean;
    }

    public DecisionSanction recopieViewDecision() {
        DecisionSanction bean = new DecisionSanction(decisionSanction.getId());
        bean.setActif(decisionSanction.isActif());
        bean.setDescriptionMotif(decisionSanction.getDescriptionMotif());
        bean.setDuree(decisionSanction.getDuree());
        bean.setLibelle(decisionSanction.getLibelle());
        return bean;
    }

    public FauteSanction recopieViewFaute() {
        FauteSanction bean = new FauteSanction(fauteSanction.getId());
        bean.setLibelle(fauteSanction.getLibelle());
        return bean;
    }

    public void selectSanction(Sanction bean) {
        bean.setSelectActif(!bean.isSelectActif());
        listSanction.get(listSanction.indexOf(bean)).setSelectActif(bean.isSelectActif());
        if (listSelectSanction.contains(bean)) {
            listSelectSanction.remove(bean);
        } else {
            listSelectSanction.add(bean);
        }
        if (listSelectSanction.isEmpty()) {
            resetFiche();
        } else {
            populateView(listSelectSanction.get(listSelectSanction.size() - 1));
        }
        setUpdateSanction(!listSelectSanction.isEmpty());
        setSelectSanction(isUpdateSanction());
        setOptionUpdate(listSelectSanction.size() == 1);
        update("head_param_sanction");
        update("body_param_sanction");
    }

    @Override
    public void populateView(Sanction bean) {
        cloneObject(sanction, bean);
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            Sanction bean = (Sanction) ev.getObject();
            selectSanction(bean);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        if (ev != null) {
            Sanction bean = (Sanction) ev.getObject();
            selectSanction(bean);
        }
    }

    @Override
    public void loadAll() {
        listSanction.clear();
        String[] champ = new String[]{"actif"};
        Object[] val = new Object[]{true};
        listSanction = UtilGrh.buildBeanListSanction(dao.loadNameQueries("YvsGrhSanction.findByActif", champ, val));
    }

    public void loadAllDecision() {
        listDecisionSanction.clear();
        String[] champ = new String[]{"societe"};
        Object[] val = new Object[]{currentAgence.getSociete()};
        listDecisionSanction = UtilGrh.buildBeanListDecisionSanction(dao.loadNameQueries("YvsGrhDecisionSanction.findAll", champ, val));
    }

    public void loadAllFaute() {
        listFauteSanction.clear();
        String[] champ = new String[]{"societe"};
        Object[] val = new Object[]{currentAgence.getSociete()};
        listFauteSanction = UtilGrh.buildBeanListFauteSanction(dao.loadNameQueries("YvsGrhFauteSanction.findAll", champ, val));
    }

    @Override
    public void deleteBean() {
        try {
            if (listSelectSanction != null) {
                if (!listSelectSanction.isEmpty()) {
                    for (Sanction s : listSelectSanction) {
                        dao.delete(new YvsGrhSanction(s.getId()));
                        listSanction.remove(s);
                    }
                    listSelectSanction.clear();
                    resetFiche();
                    succes();
                    update("head_param_sanction");
                    update("body_param_sanction");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible !");
            Logger.getLogger(ManagedParamSanction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateBean() {
        setViewList(false);
        setSelectSanction(false);
        setOptionUpdate(false);
        update("head_param_sanction");
        update("body_param_sanction");
    }

    @Override
    public boolean saveNew() {
        String action = isUpdateSanction() ? "Modification" : "Insertion";
        try {
            Sanction bean = recopieView();
            if (controleFiche(bean)) {
                YvsGrhSanction entity = buildSanction(bean);
                if (!isUpdateSanction()) {
                    entity.setId(null);
                    entity = (YvsGrhSanction) dao.save1(entity);
                    bean.setId(entity.getId());
                    listSanction.add(bean);
                } else {
                    dao.update(entity);
                    listSanction.set(listSanction.indexOf(sanction), bean);
                }
                setUpdateSanction(true);
                succes();
                update("head_param_sanction");
                update("body_param_sanction");
            }
            return true;
        } catch (Exception ex) {
            getErrorMessage(action + " impossible !");
            Logger.getLogger(ManagedParamSanction.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void saveNewDecision() {
        try {
            DecisionSanction bean = recopieViewDecision();
            if (controleFicheDecision(bean)) {
                YvsGrhDecisionSanction entity = buildDecisionSanction(bean);
                entity.setId(null);
                entity = (YvsGrhDecisionSanction) dao.save1(entity);
                bean.setId(entity.getId());
                sanction.setDecision(bean);
                listDecisionSanction.add(bean);
                update("form_param_sanction_01");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Creation impossible !");
            Logger.getLogger(ManagedParamSanction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveNewFaute() {
        try {
            FauteSanction bean = recopieViewFaute();
            if (controleFicheFaute(bean)) {
                YvsGrhFauteSanction entity = buildFauteSanction(bean);
                entity.setId(null);
                entity = (YvsGrhFauteSanction) dao.save1(entity);
                bean.setId(entity.getId());
                sanction.setFaute(bean);
                listFauteSanction.add(bean);
                update("form_param_sanction_01");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Creation impossible !");
            Logger.getLogger(ManagedParamSanction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void resetFiche() {
        resetFiche(sanction);
        sanction.setDecision(new DecisionSanction());
        sanction.setFaute(new FauteSanction());
        setUpdateSanction(false);
        resetPage();
        update("form_param_sanction_01");
    }

    @Override
    public void resetPage() {
        if (listSanction != null) {
            for (Sanction s : listSanction) {
                listSanction.get(listSanction.indexOf(s)).setSelectActif(false);
            }
        }
        listSelectSanction.clear();
    }

    public void changeView() {
        setViewList(!viewList);
        resetFiche();
        update("head_param_sanction");
        update("body_param_sanction");
    }

    public void chooseDecision(ValueChangeEvent ev) {
        if (ev != null) {
            int id = (int) ev.getNewValue();
            if (id > 0) {
                sanction.getDecision().setId(id);
                sanction.getDecision().setLibelle(listDecisionSanction.get(listDecisionSanction.indexOf(sanction.getDecision())).getLibelle());
            } else if (id == -1) {
                openDialog("dlgAddDecision");
            }
        }
    }

    public void chooseFaute(ValueChangeEvent ev) {
        if (ev != null) {
            int id = (int) ev.getNewValue();
            if (id > 0) {
                sanction.getFaute().setId(id);
                sanction.getFaute().setLibelle(listFauteSanction.get(listFauteSanction.indexOf(sanction.getFaute())).getLibelle());
            } else if (id == -1) {
                openDialog("dlgAddFaute");
            }
        }
    }
}
