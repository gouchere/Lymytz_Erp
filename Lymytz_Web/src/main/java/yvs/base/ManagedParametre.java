/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.base.YvsBaseParametre;
import yvs.parametrage.societe.UtilSte;
import yvs.util.Managed;
import yvs.util.MdpUtil;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean(name = "managedParametreBase")
@SessionScoped
public class ManagedParametre extends Managed<ParametreBase, YvsBaseParametre> implements Serializable {

    private ParametreBase parametre = new ParametreBase();
    private MdpUtil hashMdp = new MdpUtil();

    public ManagedParametre() {
    }

    public ParametreBase getParametre() {
        return parametre;
    }

    public void setParametre(ParametreBase parametre) {
        this.parametre = parametre;
    }

    @Override
    public void loadAll() {
        YvsBaseParametre y = (YvsBaseParametre) dao.loadOneByNameQueries("YvsBaseParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        onSelectObject(y);
    }

    @Override
    public boolean controleFiche(ParametreBase bean) {
        if (!bean.isGenererPassword() ? !hashMdp.correct(bean.getDefautPassword(), bean.getTaillePassword()) : false) {
            getErrorMessage(hashMdp.ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void resetFiche() {
        parametre = new ParametreBase();
    }

    @Override
    public boolean saveNew() {
        try {
            if (controleFiche(parametre)) {
                YvsBaseParametre y = UtilSte.buildParametre(parametre, currentUser, currentAgence.getSociete());
                if (parametre.getId() < 1) {
                    y.setId(null);
                    y = (YvsBaseParametre) dao.save1(y);
                    parametre.setId(y.getId());
                } else {
                    dao.update(y);
                }
                currentParam = y;
                succes();
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("ManagedParametre (saveNew)", ex);
        }
        return false;
    }

    @Override
    public void onSelectObject(YvsBaseParametre y) {
        parametre = UtilSte.buildBeanParametre(y);
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        onSelectObject((YvsBaseParametre) ev.getObject());
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void generedPassword(){
        
    }
}
