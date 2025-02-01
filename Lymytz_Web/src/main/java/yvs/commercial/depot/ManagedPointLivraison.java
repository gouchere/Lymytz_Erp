/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.depot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.tiers.Tiers;
import yvs.commercial.UtilCom;
import yvs.commercial.client.Client;
import yvs.commercial.client.ManagedClient;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBasePointLivraison;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.param.YvsDictionnaire;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.dico.ManagedDico;
import yvs.service.base.param.IYvsBasePointLivraison;
import yvs.util.Managed;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedPointLivraison extends Managed<PointLivraison, YvsBasePointLivraison> implements Serializable {
    
    private PointLivraison point = new PointLivraison();
    private YvsBasePointLivraison selectPoint;
    private List<YvsBasePointLivraison> points;
    
    private String tabIds;
    private String fusionneTo;
    private List<String> fusionnesBy;
    
    IYvsBasePointLivraison service;
    
    public ManagedPointLivraison() {
        points = new ArrayList<>();
    }
    
    public PointLivraison getPoint() {
        return point;
    }
    
    public void setPoint(PointLivraison point) {
        this.point = point;
    }
    
    public YvsBasePointLivraison getSelectPoint() {
        return selectPoint;
    }
    
    public void setSelectPoint(YvsBasePointLivraison selectPoint) {
        this.selectPoint = selectPoint;
    }
    
    public List<YvsBasePointLivraison> getPoints() {
        return points;
    }
    
    public void setPoints(List<YvsBasePointLivraison> points) {
        this.points = points;
    }
    
    public String getTabIds() {
        return tabIds;
    }
    
    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }
    
    public String getFusionneTo() {
        return fusionneTo;
    }
    
    public void setFusionneTo(String fusionneTo) {
        this.fusionneTo = fusionneTo;
    }
    
    public List<String> getFusionnesBy() {
        return fusionnesBy;
    }
    
    public void setFusionnesBy(List<String> fusionnesBy) {
        this.fusionnesBy = fusionnesBy;
    }
    
    @Override
    public void loadAll() {
        points = dao.loadNameQueries("YvsBasePointLivraison.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        service = (IYvsBasePointLivraison) IEntitiSax.createInstance("IYvsBasePointLivraison", dao);
        if (point.getClient() == null) {
            point.setClient(new Client());
        }
    }
    
    
    public void loadByVille(YvsDictionnaire ville){
        points = dao.loadNameQueries("YvsBasePointLivraison.findBySocieteVille", new String[]{"societe", "ville"}, new Object[]{currentAgence.getSociete(), ville});
    }
    @Override
    public boolean controleFiche(PointLivraison bean) {
        if (bean == null) {
            return false;
        }
        if (bean.getVille() != null ? bean.getVille().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez la ville;");
            return false;
        }
        if (!Util.asString(bean.getLibelle())) {
            getErrorMessage("Vus devez precisez le libelle");
            return false;
        }
        return true;
    }
    
    @Override
    public void resetFiche() {
        point = new PointLivraison();
        point.setClient(new Client());
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            m.getVilles().clear();
        }
        update("form-point_livraison");
    }
    
    @Override
    public boolean saveNew() {
        try {
            if (controleFiche(point)) {
                selectPoint = UtilCom.buildPointLivraison(point, currentAgence.getSociete(), currentUser);
                ResultatAction<YvsBasePointLivraison> result;
                if (point.getId() < 1) {
                    result = service.save(selectPoint);
                } else {
                    result = service.update(selectPoint);
                }
                if (result == null) {
                    getErrorMessage("Action Impossible");
                    return false;
                }
                if (!result.isResult()) {
                    getErrorMessage(result.getMessage());
                    return false;
                }
                selectPoint = (YvsBasePointLivraison) result.getData();
                point.setId(selectPoint.getId());
                int idx = points.indexOf(selectPoint);
                if (idx > -1) {
                    points.set(idx, selectPoint);
                } else {
                    points.add(selectPoint);
                }
                update("data-point_livraison");
                succes();
                actionOpenOrResetAfter(this);
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("saveNew", ex);
        }
        return false;
    }
    
    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsBasePointLivraison> list = new ArrayList<>();
                YvsBasePointLivraison bean;
                for (Long ids : l) {
                    bean = points.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);
                    
                    if (bean.getId() == point.getId()) {
                        resetFiche();
                    }
                }
                points.removeAll(list);
                succes();
                update("data-point_livraison");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }
    
    public void deleteBean_(YvsBasePointLivraison y) {
        selectPoint = y;
    }
    
    public void deleteBean_() {
        try {
            if (selectPoint != null) {
                dao.delete(selectPoint);
                points.remove(selectPoint);
                succes();
                if (selectPoint.getId() == point.getId()) {
                    resetFiche();
                }
                update("data-point_livraison");
            } else {
                getErrorMessage("Vous devez selectionner le point");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }
    
    @Override
    public void onSelectObject(YvsBasePointLivraison y) {
        selectPoint = y;
        point = UtilCom.buildBeanPointLivraison(y);
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null && (point.getPays() != null ? point.getPays().getId() > 0 : false)) {
            Dictionnaire d = m.choosePays(point.getPays().getId());
        }
        update("form-point_livraison");
    }
    
    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsBasePointLivraison) ev.getObject());
            tabIds = points.indexOf((YvsBasePointLivraison) ev.getObject()) + "";
        }
    }
    
    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }
    
    public void loadOnViewClient(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComClient y = (YvsComClient) ev.getObject();
            chooseClient(UtilCom.buildBeanClient(y));
        }
    }
    
    public void choosePays() {
        point.setVille(new Dictionnaire());
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            Dictionnaire d = m.choosePays(point.getPays().getId());
            if (d != null ? d.getId() > 0 : false) {
                cloneObject(point.getPays(), d);
            }
        }
    }
    
    public void chooseVille() {
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            int idx = m.getVilles().indexOf(new YvsDictionnaire(point.getVille().getId()));
            if (idx > -1) {
                YvsDictionnaire d = m.getVilles().get(idx);
                point.setVille(new Dictionnaire(d.getId(), d.getLibele(), d.getAbreviation()));
            }
        }
    }
    
    public void chooseClient(Client d) {
        if (d != null ? d.getId() > 0 : false) {
            cloneObject(point.getClient(), d);
            update("select_client_point_livraison");
        }
    }
    
    public void fusionner(boolean fusionne) {
        try {
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                long newValue = points.get(ids.get(0)).getId();
                if (!fusionne) {
                    if (!autoriser("base_user_fusion")) {
                        openNotAcces();
                        return;
                    }
                    String oldValue = "0";
                    for (int i : ids) {
                        if (points.get(i).getId() != newValue) {
                            oldValue += "," + points.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_base_point_livraison", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                points.remove(new YvsBasePointLivraison(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = points.get(idx).getLibelle();
                    } else {
                        YvsBasePointLivraison c = (YvsBasePointLivraison) dao.loadOneByNameQueries("YvsBasePointLivraison.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? c.getId() > 0 : false) {
                            fusionneTo = c.getLibelle();
                        }
                    }
                    YvsBasePointLivraison c;
                    for (int i : ids) {
                        long oldValue = points.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(points.get(i).getLibelle());
                            }
                        } else {
                            c = (YvsBasePointLivraison) dao.loadOneByNameQueries("YvsBasePointLivraison.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? c.getId() > 0 : false) {
                                fusionnesBy.add(c.getLibelle());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 zones");
            }
        } catch (NumberFormatException ex) {
        }
    }
    
    public void searchClient() {
        String num = point.getClient().getCodeClient();
        point.getClient().setId(0);
        point.getClient().setError(true);
        point.getClient().setTiers(new Tiers());
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (m != null) {
            if (num != null ? !num.isEmpty() : false) {
                Client y = m.searchClient(num, true);
                if (m.getClients() != null ? !m.getClients().isEmpty() : false) {
                    if (m.getClients().size() > 1) {
                        update("data_client_point_livraison");
                    } else {
                        chooseClient(y);
                    }
                    point.getClient().setError(false);
                }
            }
        }
    }
    
}
