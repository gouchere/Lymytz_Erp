/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.production.YvsProdParametre;
import yvs.util.Managed;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedParamProd extends Managed<ParametreProd, YvsProdParametre> implements Serializable {

    private ParametreProd param = new ParametreProd();
    private YvsProdParametre currentParam;

    public ManagedParamProd() {
    }

    public ParametreProd getParam() {
        return param;
    }

    public void setParam(ParametreProd param) {
        this.param = param;
    }

    public YvsProdParametre getCurrentParam() {
        return currentParam;
    }

    public void setCurrentParam(YvsProdParametre currentParam) {
        this.currentParam = currentParam;
    }

    @Override
    public void loadAll() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsProdParametre.findAll";
        List<YvsProdParametre> l = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
        if (l != null ? !l.isEmpty() : false) {
            currentParam = l.get(0);
            param = UtilProd.buildBeanParametre(currentParam);
        }
    }

    @Override
    public boolean controleFiche(ParametreProd bean) {

        return true;
    }

    @Override
    public ParametreProd recopieView() {
        ParametreProd p = new ParametreProd();
        p.setId(param.getId());
        p.setConverter(param.getConverter());
        p.setSuiviOprequis(param.isSuiviOprequis());
        p.setDateSave(param.getDateSave());
        p.setLimiteCreateOf(param.getLimiteCreateOf());
        p.setLimiteVuOf(param.getLimiteVuOf());
        p.setValoriserBy(param.getValoriserBy());
        return p;
    }

    @Override
    public void populateView(ParametreProd bean) {
        cloneObject(param, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(param);
    }

    @Override
    public boolean saveNew() {
        String action = param.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(param)) {
                YvsProdParametre y = UtilProd.buildParametre(param, currentAgence.getSociete(), currentUser);
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                if (param.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setDateSave(new Date());
                    y.setId(null);
                    y = (YvsProdParametre) dao.save1(y);
                    param.setId(y.getId());
                }
                succes();
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException(action + " impossible", ex);
            return false;
        }
    }

    @Override
    public void deleteBean() {
        if (param != null ? param.getId() > 0 : false) {
            dao.delete(new YvsProdParametre(param.getId()));
            resetFiche();
            succes();
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsProdParametre y = (YvsProdParametre) ev.getObject();
            populateView(UtilProd.buildBeanParametre(y));
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

}
