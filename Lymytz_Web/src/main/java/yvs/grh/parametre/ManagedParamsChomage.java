/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.parametre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.param.YvsGrhParamsTauxChomageTech;
//import yvs.entity.grh.param.YvsGrhDetailTypeChomage;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedParamsChomage extends Managed<ParamsTauxChomageTechnique, YvsGrhParamsTauxChomageTech> implements Serializable {

    private ParamsTauxChomageTechnique paramCT = new ParamsTauxChomageTechnique();
    private List<YvsGrhParamsTauxChomageTech> paramsChomages;

    public ManagedParamsChomage() {
        paramsChomages = new ArrayList<>();
    }

    public ParamsTauxChomageTechnique getParamCT() {
        return paramCT;
    }

    public void setParamCT(ParamsTauxChomageTechnique paramCT) {
        this.paramCT = paramCT;
    }

    public List<YvsGrhParamsTauxChomageTech> getParamsChomages() {
        return paramsChomages;
    }

    public void setParamsChomages(List<YvsGrhParamsTauxChomageTech> paramsChomages) {
        this.paramsChomages = paramsChomages;
    }

    public boolean controleFicheDetail(ParamsTauxChomageTechnique bean) {

        return true;
    }

    @Override
    public boolean controleFiche(ParamsTauxChomageTechnique bean) {
        if ((bean.getCode() == null) ? true : "".equals(bean.getCode())) {
            getErrorMessage("Vous devez entrer le code");
            return false;
        }
        if (bean.getTaux() < 0) {
            getErrorMessage("Le taux doit être une valeur positive");
            return false;
        }
        return true;
    }

    @Override
    public void updateBean() {

    }

    @Override
    public ParamsTauxChomageTechnique recopieView() {
        ParamsTauxChomageTechnique bean = new ParamsTauxChomageTechnique();

        return bean;
    }

    @Override
    public void populateView(ParamsTauxChomageTechnique bean) {
        cloneObject(paramCT, bean);
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev.getObject() != null) {
            YvsGrhParamsTauxChomageTech bean = (YvsGrhParamsTauxChomageTech) ev.getObject();
            buildBean(bean);
        }
    }

    public void buildBean(YvsGrhParamsTauxChomageTech bean) {
        paramCT.setActif(bean.getActif());
        paramCT.setCode(bean.getCodeTaux());
        paramCT.setNumero(bean.getNumMois());
        paramCT.setTaux(bean.getTaux());
        paramCT.setId(bean.getId());
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetPage();
    }

    @Override
    public void loadAll() {
        paramsChomages = dao.loadNameQueries("YvsGrhParamsTauxChomageTech.findAll", new String[]{"societe"}, new Object[]{currentUser.getAgence().getSociete()});
    }

    @Override
    public void resetFiche() {

    }

    @Override
    public void resetPage() {
        paramCT = new ParamsTauxChomageTechnique();

    }

    @Override
    public boolean saveNew() {
        try {
            if (controleFiche(paramCT)) {
                YvsGrhParamsTauxChomageTech p = new YvsGrhParamsTauxChomageTech();
                p.setActif(paramCT.isActif());
                p.setAuthor(currentUser);
                p.setCodeTaux(paramCT.getCode());
                p.setNumMois(paramsChomages.size() + 1);
                p.setTaux(paramCT.getTaux());
                p.setSociete(currentUser.getAgence().getSociete());
                if (paramCT.getId() <= 0) {
                    p = (YvsGrhParamsTauxChomageTech) dao.save1(p);
                    paramsChomages.add(p);
                } else {
                    p.setId(paramCT.getId());
                    p.setNumMois(paramCT.getNumero());
                    dao.update(p);
                    int idx = paramsChomages.indexOf(p);
                    if (idx >= 0) {
                        paramsChomages.set(idx, p);
                    }
                }
                paramCT = new ParamsTauxChomageTechnique();
                paramCT.setNumero(paramsChomages.size() + 1);
                succes();

            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedParamsChomage.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public void avancerNumParam(boolean avancer, YvsGrhParamsTauxChomageTech pa) {
        String rq = (avancer) ? "YvsGrhParamsTauxChomageTech.findByNumMoisSup" : "YvsGrhParamsTauxChomageTech.findByNumMoisInf";
        List<YvsGrhParamsTauxChomageTech> lp = dao.loadNameQueries(rq, new String[]{"numMois", "societe"}, new Object[]{pa.getNumMois(), currentUser.getAgence().getSociete()});
        if (lp != null) {
            for (YvsGrhParamsTauxChomageTech p : lp) {
                p.setNumMois(p.getNumMois() + 1);
                dao.update(p);
                paramsChomages.set(paramsChomages.indexOf(p), p);
            }
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (paramCT.getId() > 0) {
                YvsGrhParamsTauxChomageTech p = new YvsGrhParamsTauxChomageTech(paramCT.getId());
                p.setAuthor(currentUser);
                dao.delete(p);
                paramsChomages.remove(p);
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible!");
            Logger.getLogger(ManagedParamsChomage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void openDlgToDel(YvsGrhParamsTauxChomageTech p) {
        buildBean(p);
        openDialog("dlgConfirmDelParamCt");
    }

}
