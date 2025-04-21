/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.salaire;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.mutuelle.credit.YvsMutTypeCredit;
import yvs.mutuelle.UtilMut;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedTypeAvance extends Managed<TypeAvance, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{typeAvance}")
    private TypeAvance typeAvance;
    private List<YvsMutTypeCredit> types;

    private String tabIds, input_reset;
    private boolean updateTypeCredit;

    public ManagedTypeAvance() {
        types = new ArrayList<>();
    }

    public TypeAvance getTypeAvance() {
        return typeAvance;
    }

    public void setTypeAvance(TypeAvance typeAvance) {
        this.typeAvance = typeAvance;
    }

    public List<YvsMutTypeCredit> getTypes() {
        return types;
    }

    public void setTypes(List<YvsMutTypeCredit> types) {
        this.types = types;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getInput_reset() {
        return input_reset;
    }

    public void setInput_reset(String input_reset) {
        this.input_reset = input_reset;
    }

    public boolean isUpdateTypeCredit() {
        return updateTypeCredit;
    }

    public void setUpdateTypeCredit(boolean updateTypeCredit) {
        this.updateTypeCredit = updateTypeCredit;
    }

    @Override
    public void loadAll() {
        
        loadAllTypeCredit();
        tabIds = "";
    }

    public void loadAllTypeCredit() {
        types  = dao.loadNameQueries("YvsMutTypeCredit.findByMutuelle", new String[]{"mutuelle", "typeAvance"}, new Object[]{currentMutuel, true});
    }

    public YvsMutTypeCredit buildTypeCredit(TypeAvance y) {
        YvsMutTypeCredit t = new YvsMutTypeCredit();
        if (y != null) {
            t.setDesignation(y.getDesignation());
            t.setId(y.getId());
            t.setNatureMontant(y.getNatureMontant());
            t.setMontantMaximal(y.getMontantMaximal());
            t.setPeriodeMaximal(y.getPeriodeMaximal());
            t.setTauxMaximal(y.getTauxMaximal());
            t.setMutuelle(currentMutuel);
            t.setTypeAvance(true);
            t.setImpayeDette(false);
            t.setJourDebutAvance(y.getJourDebutAvance());
            t.setJourFinAvance(y.getJourFinAvance());
            t.setAuthor(currentUser);
            t.setDateSave(y.getDateSave());
            t.setDateUpdate(new Date());
        }
        return t;
    }

    @Override
    public TypeAvance recopieView() {
        TypeAvance t = new TypeAvance();
        t.setDesignation(typeAvance.getDesignation());
        t.setId(typeAvance.getId());
        t.setNatureMontant(typeAvance.getNatureMontant());
        t.setMontantMaximal(typeAvance.getMontantMaximal());
        t.setPeriodeMaximal(typeAvance.getPeriodeMaximal());
        t.setJourDebutAvance(typeAvance.getJourDebutAvance());
        t.setJourFinAvance(typeAvance.getJourFinAvance());
        t.setTauxMaximal(typeAvance.getTauxMaximal());
        t.setMutuelle(UtilMut.buildBeanMutuelle(currentMutuel));
        return t;
    }

    @Override
    public boolean controleFiche(TypeAvance bean) {
        if (bean.getDesignation() == null || bean.getDesignation().equals("")) {
            getErrorMessage("Vous devez entrer la designation");
            return false;
        }
        if (currentMutuel != null ? currentMutuel.getId() < 1 : true) {
            getErrorMessage("Vous devez etre connecté dans une mutuelle");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(TypeAvance bean) {
        typeAvance.setDesignation(bean.getDesignation());
        typeAvance.setId(bean.getId());
        typeAvance.setNatureMontant(bean.getNatureMontant());
        typeAvance.setMontantMaximal(bean.getMontantMaximal());
        typeAvance.setPeriodeMaximal(bean.getPeriodeMaximal());
        typeAvance.setTauxMaximal(bean.getTauxMaximal());
        setUpdateTypeCredit(true);
    }

    @Override
    public void resetFiche() {
        typeAvance.setDesignation("");
        typeAvance.setId(0);
        typeAvance.setNatureMontant("");
        typeAvance.setMontantMaximal(0.0);
        typeAvance.setPeriodeMaximal(1);
        typeAvance.setTauxMaximal(0);
        typeAvance.setJourDebutAvance(1);
        typeAvance.setJourFinAvance(15);
        tabIds = "";
        input_reset = "";
        setUpdateTypeCredit(false);
    }

    @Override
    public boolean saveNew() {
        if (input_reset != null && input_reset.equals("reset")) {
            setUpdateTypeCredit(false);
            input_reset = "";
        }
        String action = isUpdateTypeCredit() ? "Modification" : "Insertion";
        try {
            TypeAvance bean = recopieView();
            bean.setNew_(true);
            if (controleFiche(bean)) {
                YvsMutTypeCredit entity = buildTypeCredit(bean);
                if (!isUpdateTypeCredit()) {
                    entity.setId(null);
                    entity = (YvsMutTypeCredit) dao.save1(entity);
                    bean.setId(entity.getId());
                    typeAvance.setId(entity.getId());
                    types.add(0, entity);
                } else {
                    dao.update(entity);
                    types.set(types.indexOf(entity), entity);
                }
                succes();
                resetFiche();
            }
        } catch (Exception ex) {
            getException("Error " + action + " : " + ex.getMessage(), ex);
            getErrorMessage(action + " Impossible !");
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? tabIds.length() > 0 : false) {
                String[] ids = tabIds.split("-");
                if ((ids != null) ? ids.length > 0 : false) {
                    for (String s : ids) {
                        long id = Integer.valueOf(s);
                        YvsMutTypeCredit bean = types.get(types.indexOf(new YvsMutTypeCredit(id)));
                        dao.delete(new YvsMutTypeCredit(bean.getId()));
                        types.remove(bean);
                    }
                    resetFiche();
                    succes();
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    public void deleteBean(YvsMutTypeCredit y) {
        try {
            if (y != null) {
                dao.delete(y);
                types.remove(y);

                resetFiche();
                succes();
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    @Override
    public void updateBean() {
        if ((tabIds != null) ? tabIds.length() > 0 : false) {
            String[] ids = tabIds.split("-");
            setUpdateTypeCredit((ids != null) ? ids.length > 0 : false);
            if (isUpdateTypeCredit()) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                YvsMutTypeCredit bean = types.get(types.indexOf(new YvsMutTypeCredit(id)));
                populateView(UtilMut.buildBeanTypeAvance(bean));
                tabIds = "";
                input_reset = "";
                update("blog_form_type_avance_");
            }
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutTypeCredit bean = (YvsMutTypeCredit) ev.getObject();
            populateView(UtilMut.buildBeanTypeAvance(bean));
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void chooseNatureMontant() {
        if ((typeAvance != null) ? typeAvance.getNatureMontant() != null : false) {
            switch (typeAvance.getNatureMontant()) {
                case "Fixe":
                    typeAvance.setSuffixeMontant("Fcfa");
                    break;
                case "Pourcentage":
                    typeAvance.setSuffixeMontant("% Salaire");
                    break;
                default:
                    break;
            }
        }
    }

}
