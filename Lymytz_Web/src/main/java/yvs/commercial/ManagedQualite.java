/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.commercial.YvsComQualite;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedQualite extends Managed<Qualite, YvsComQualite> implements Serializable {

    @ManagedProperty(value = "#{qualite}")
    private Qualite qualite;
    private List<YvsComQualite> qualites;
    private YvsComQualite qualiteSelect;
    private String tabIds;

    public ManagedQualite() {
        qualites = new ArrayList<>();
    }

    public YvsComQualite getQualiteSelect() {
        return qualiteSelect;
    }

    public void setQualiteSelect(YvsComQualite qualiteSelect) {
        this.qualiteSelect = qualiteSelect;
    }

    public List<YvsComQualite> getQualites() {
        return qualites;
    }

    public void setQualites(List<YvsComQualite> qualites) {
        this.qualites = qualites;
    }

    public Qualite getQualite() {
        return qualite;
    }

    public void setQualite(Qualite planRemise) {
        this.qualite = planRemise;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }
    
    public void doNothing(){
        
    }

    @Override
    public void loadAll() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        qualites = dao.loadNameQueries("YvsComQualite.findAll", champ, val);
    }

    public YvsComQualite buildQualite(Qualite y) {
        YvsComQualite c = new YvsComQualite();
        if (y != null) {
            c.setActif(y.isActif());
            c.setCode(y.getCode());
            c.setId(y.getId());
            c.setLibelle(y.getLibelle());
            c.setSociete(currentAgence.getSociete());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                c.setAuthor(currentUser);
            }
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(new Date());
            c.setNew_(true);
        }
        return c;
    }

    @Override
    public Qualite recopieView() {
        Qualite c = new Qualite();
        c.setActif(qualite.isActif());
        c.setCode(qualite.getCode());
        c.setId(qualite.getId());
        c.setLibelle(qualite.getLibelle());
        return c;
    }

    @Override
    public boolean controleFiche(Qualite bean) {
        if (bean.getCode() == null || bean.getCode().equals("")) {
            getErrorMessage("vous devez entrer le code de la categorie");
            return false;
        }
        YvsComQualite t = (YvsComQualite) dao.loadOneByNameQueries("YvsComQualite.findByCode", new String[]{"code", "societe"}, new Object[]{bean.getCode(), currentUser.getAgence().getSociete()});
        if (t != null ? (t.getId() != null ? !t.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez déja crée cette catégorie");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(Qualite bean) {
        cloneObject(qualite, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(qualite);
        tabIds = "";
    }

    @Override
    public boolean saveNew() {
        String action = qualite.getId() > 0 ? "Modification" : "Insertion";
        try {
            Qualite bean = recopieView();
            if (controleFiche(bean)) {
                YvsComQualite entity = buildQualite(bean);
                if (bean.getId() < 1) {
                    entity.setId(null);
                    entity = (YvsComQualite) dao.save1(entity);
                    qualite.setId(entity.getId());
                    qualites.add(0, entity);
                } else {
                    dao.update(entity);
                    qualites.set(qualites.indexOf(entity), entity);
                }
                succes();
                resetFiche();
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
                    YvsComQualite bean = qualites.get(qualites.indexOf(new YvsComQualite(id)));
                    dao.delete(bean);
                    qualites.remove(bean);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBean_(YvsComQualite y) {
        qualiteSelect = y;
    }

    public void deleteBean_() {
        try {
            if (qualiteSelect != null) {
                dao.delete(qualiteSelect);
                qualites.remove(qualiteSelect);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComQualite bean = (YvsComQualite) ev.getObject();
            populateView(UtilCom.buildBeanQualite(bean));
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void active(YvsComQualite bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            dao.update(bean);
            qualites.set(qualites.indexOf(bean), bean);
            update("data_qualite");
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
