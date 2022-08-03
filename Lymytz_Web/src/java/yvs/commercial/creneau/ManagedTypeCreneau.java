/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.creneau;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.commercial.UtilCom;
import yvs.dao.Options;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.util.Managed;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedTypeCreneau extends Managed<TypeCreneau, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{typeCreneau}")
    private TypeCreneau typeCreneau;
    private List<YvsGrhTrancheHoraire> tranches, selectTranches;
    private YvsGrhTrancheHoraire typeSelect;

    private List<String> criteres;

    private String tabIds;
    private boolean updateTypeCrenau;

    private String fusionneTo;
    private List<String> fusionnesBy;

    public ManagedTypeCreneau() {
        tranches = new ArrayList<>();
        criteres = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
        selectTranches = new ArrayList<>();
    }

    public List<YvsGrhTrancheHoraire> getSelectTranches() {
        return selectTranches;
    }

    public void setSelectTranches(List<YvsGrhTrancheHoraire> selectTranches) {
        this.selectTranches = selectTranches;
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

    public YvsGrhTrancheHoraire getTypeSelect() {
        return typeSelect;
    }

    public void setTypeSelect(YvsGrhTrancheHoraire typeSelect) {
        this.typeSelect = typeSelect;
    }

    public List<String> getCriteres() {
        return criteres;
    }

    public void setCriteres(List<String> criteres) {
        this.criteres = criteres;
    }

    public TypeCreneau getTypeCreneau() {
        return typeCreneau;
    }

    public void setTypeCreneau(TypeCreneau typeCreneau) {
        this.typeCreneau = typeCreneau;
    }

    public List<YvsGrhTrancheHoraire> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsGrhTrancheHoraire> tranches) {
        this.tranches = tranches;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public boolean isUpdateTypeCrenau() {
        return updateTypeCrenau;
    }

    public void setUpdateTypeCrenau(boolean updateTypeCrenau) {
        this.updateTypeCrenau = updateTypeCrenau;
    }

    @Override
    public void loadAll() {
        loadAllTypeCrenau();
    }

    public void loadAllTrancheProduction() {
        if (autoriser("prod_view_all_equipe")) {
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
            tranches = dao.loadNameQueries("YvsGrhTrancheHoraire.findAll", champ, val);
        } else {
            //charge seulement les tranches où l'utilisateur est planifié
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date hier = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 2);
            tranches = dao.loadNameQueries("YvsComCreneauHoraireUsers.findTrancheByUsersDates", new String[]{"users", "debut", "fin"}, new Object[]{currentUser.getUsers(), hier, cal.getTime()});
        }
    }

    public void loadAllTranche() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        tranches = dao.loadNameQueries("YvsGrhTrancheHoraire.findAll", champ, val);
    }

    public void loadAllTypeCrenau() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        tranches = dao.loadNameQueries("YvsGrhTrancheHoraire.findAll", champ, val);
        loadAllCritere();
    }

    public void loadAllCritere() {
        criteres.clear();
        for (YvsGrhTrancheHoraire bean : tranches) {
            boolean deja_ = false;
            if ((bean.getTypeJournee() != null) ? !bean.getTypeJournee().trim().equals("") : false) {
                for (String s : criteres) {
                    if (s.toLowerCase().equals(bean.getTypeJournee().toLowerCase())) {
                        deja_ = true;
                        break;
                    }
                }
                if (!deja_) {
                    criteres.add(bean.getTypeJournee());
                }
            }
        }
        update("event1");
    }

    @Override
    public TypeCreneau recopieView() {
        TypeCreneau t = new TypeCreneau();
        t.setActif(typeCreneau.isActif());
        t.setHeureDebut((typeCreneau.getHeureDebut() != null) ? typeCreneau.getHeureDebut() : new Date());
        t.setHeureFin((typeCreneau.getHeureFin() != null) ? typeCreneau.getHeureFin() : new Date());
        t.setId(typeCreneau.getId());
        t.setReference(typeCreneau.getCritere() + "_" + Util.getTimeToString(typeCreneau.getHeureDebut()) + "-" + Util.getTimeToString(typeCreneau.getHeureFin()));
        t.setCritere(typeCreneau.getCritere());
        t.setNew_(true);
        return t;
    }

    @Override
    public boolean controleFiche(TypeCreneau bean) {
        if (bean.getHeureDebut() == null) {
            getErrorMessage("Vous devez entrer l'heure de debut");
            return false;
        }
        if (bean.getHeureFin() == null) {
            getErrorMessage("Vous devez entrer l'heure de fin");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(TypeCreneau bean) {
        cloneObject(typeCreneau, bean);
        setUpdateTypeCrenau(true);
    }

    @Override
    public void resetFiche() {
        resetFiche(typeCreneau);
        typeCreneau.setHeureDebut(new Date());
        typeCreneau.setHeureFin(new Date());
        tabIds = "";
        setUpdateTypeCrenau(false);
    }

    @Override
    public boolean saveNew() {
        String action = isUpdateTypeCrenau() ? "Modification" : "Insertion";
        try {
            TypeCreneau bean = recopieView();
            if (controleFiche(bean)) {
                YvsGrhTrancheHoraire entity = UtilCom.buildTypeCrenau(bean, currentUser, currentAgence.getSociete());
                if (!isUpdateTypeCrenau()) {
                    entity.setId(null);
                    entity.setDateSave(new Date());
                    entity = (YvsGrhTrancheHoraire) dao.save1(entity);
                    typeCreneau.setId(entity.getId());
                    typeCreneau.setReference(bean.getReference());
                    tranches.add(0, entity);
                } else {
                    dao.update(entity);
                    tranches.set(tranches.indexOf(entity), entity);
                }
                loadAllCritere();
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
                    YvsGrhTrancheHoraire bean = tranches.get(tranches.indexOf(new YvsGrhTrancheHoraire(id)));
                    dao.delete(bean);
                    tranches.remove(bean);
                }
                succes();
                update("data_type_crenau");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void activerDesactiverTranche(YvsGrhTrancheHoraire y) {
        y.setActif(!y.getActif());
        y.setDateUpdate(new Date());
        y.setAuthor(currentUser);
        dao.update(y);
    }

    public void deleteBean_(YvsGrhTrancheHoraire y) {
        typeSelect = y;
    }

    public void deleteBean_() {
        try {
            if (typeSelect != null) {
                typeSelect.setAuthor(currentUser);
                typeSelect.setDateUpdate(new Date());
                dao.delete(typeSelect);
                tranches.remove(typeSelect);
                succes();
                update("data_type_crenau");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            typeSelect = (YvsGrhTrancheHoraire) ev.getObject();
            populateView(UtilCom.buildBeanTypeCrenau(typeSelect));
            update("form_type_crenau");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("form_type_crenau");
    }

    public List<String> completeText(String query) {
        List<String> results = new ArrayList<String>();
        for (String s : criteres) {
            if (s.toLowerCase().startsWith(query.toLowerCase())) {
                results.add(s);
            }
        }
        return results;
    }

    public void onCritereSelect(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            String d = (String) ev.getObject();
            typeCreneau.setCritere(d);
        }
    }

    public void activeType(YvsGrhTrancheHoraire bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            String rq = "UPDATE yvs_grh_tranche_horaire SET actif=" + bean.getActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            tranches.set(tranches.indexOf(bean), bean);
        }
    }

    public void fusionner(boolean fusionne) {
        try {
            fusionneTo = "";
            fusionnesBy.clear();
            if (!selectTranches.isEmpty() ? selectTranches.size() > 1 : false) {
                long newValue = selectTranches.get(0).getId();
                if (!fusionne) {
                    if (!autoriser("base_user_fusion")) {
                        openNotAcces();
                        return;
                    }
                    String oldValue = "0";
                    for (YvsGrhTrancheHoraire i : selectTranches) {
                        if (i.getId() != newValue) {
                            oldValue += "," + i.getId();
                        }
                    }
                    if (dao.fusionneData("yvs_grh_tranche_horaire", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                tranches.remove(new YvsGrhTrancheHoraire(id));
                            }
                        }
                    }
                    selectTranches.clear();
                    succes();
                } else {
                    fusionneTo = selectTranches.get(0).getTitre();
                    for (YvsGrhTrancheHoraire c : selectTranches) {
                        long oldValue = c.getId();
                        if (oldValue != newValue) {
                            fusionnesBy.add(c.getTitre());
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 tranches horaire");
            }
        } catch (NumberFormatException ex) {
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
