/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.evenement;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.mutuelle.evenement.YvsMutTauxContribution;
import yvs.entity.mutuelle.evenement.YvsMutActiviteType;
import yvs.entity.mutuelle.evenement.YvsMutTypeEvenement;
import yvs.mutuelle.UtilMut;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedTypeEvenement extends Managed<TypeEvenement, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{typeEvenement}")
    private TypeEvenement typeEvenement;
    private List<YvsMutTypeEvenement> types;

    private Contribution contribution = new Contribution();

    private String tabIds, input_reset, tabIds_contribution, input_reset_contribution;
    private boolean updateTypeEvenement, updateContribution;

    YvsMutTypeEvenement entityTypeEvt;

    public ManagedTypeEvenement() {
        types = new ArrayList<>();
    }

    public String getTabIds_contribution() {
        return tabIds_contribution;
    }

    public void setTabIds_contribution(String tabIds_contribution) {
        this.tabIds_contribution = tabIds_contribution;
    }

    public String getInput_reset_contribution() {
        return input_reset_contribution;
    }

    public void setInput_reset_contribution(String input_reset_contribution) {
        this.input_reset_contribution = input_reset_contribution;
    }

    public boolean isUpdateContribution() {
        return updateContribution;
    }

    public void setUpdateContribution(boolean updateContribution) {
        this.updateContribution = updateContribution;
    }

    public TypeEvenement getTypeEvenement() {
        return typeEvenement;
    }

    public void setTypeEvenement(TypeEvenement typeEvenement) {
        this.typeEvenement = typeEvenement;
    }

    public List<YvsMutTypeEvenement> getTypes() {
        return types;
    }

    public void setTypes(List<YvsMutTypeEvenement> types) {
        this.types = types;
    }

    public Contribution getContribution() {
        return contribution;
    }

    public void setContribution(Contribution contribution) {
        this.contribution = contribution;
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

    public boolean isUpdateTypeEvenement() {
        return updateTypeEvenement;
    }

    public void setUpdateTypeEvenement(boolean updateTypeEvenement) {
        this.updateTypeEvenement = updateTypeEvenement;
    }

    @Override
    public void loadAll() {
        loadAllTypeEvenement();
        tabIds = "";
        tabIds_contribution = "";
    }

    public void loadAllTypeEvenement() {
        types = dao.loadNameQueries("YvsMutTypeEvenement.findByMutuelle", new String[]{"mutuelle"}, new Object[]{currentMutuel});
    }

    public YvsMutTypeEvenement buildTypeEvenement(TypeEvenement y) {
        YvsMutTypeEvenement t = new YvsMutTypeEvenement();
        if (y != null) {
            t.setDescription(y.getDescription());
            t.setDesignation(y.getDesignation());
            t.setId(y.getId());
            t.setNombreParticipant(y.getNombreRepresentant());
            t.setLierMutualiste(y.isLierMutualiste());
            t.setMutuelle(currentMutuel);
            t.setAuthor(currentUser);
            t.setDateSave(y.getDateSave());
            t.setDateUpdate(new Date());            
        }
        return t;
    }

    @Override
    public TypeEvenement recopieView() {
        TypeEvenement t = new TypeEvenement();
        t.setDescription(typeEvenement.getDescription());
        t.setDesignation(typeEvenement.getDesignation());
        t.setId(typeEvenement.getId());
        t.setNombreRepresentant(typeEvenement.getNombreRepresentant());
        t.setLierMutualiste(typeEvenement.isLierMutualiste());
        if (t.getNombreRepresentant() < 1) {
            t.setNombreParticipant("Tous");
        } else {
            t.setNombreParticipant("" + t.getNombreRepresentant());
        }
        return t;
    }

    @Override
    public boolean controleFiche(TypeEvenement bean) {
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
    public void populateView(TypeEvenement bean) {
        cloneObject(typeEvenement, bean);
        setUpdateTypeEvenement(true);
    }

    @Override
    public void resetFiche() {
        resetFiche(typeEvenement);
        typeEvenement.setLierMutualiste(false);
        typeEvenement.setContributions(new ArrayList<YvsMutTauxContribution>());

        tabIds = "";
        input_reset_contribution = "";
        setUpdateTypeEvenement(false);

        resetFicheContribution();
        entityTypeEvt = new YvsMutTypeEvenement();
    }

    @Override
    public boolean saveNew() {
        if (input_reset != null && input_reset.equals("reset")) {
            setUpdateTypeEvenement(false);
            input_reset = "";
        }
        String action = isUpdateTypeEvenement() ? "Modification" : "Insertion";
        try {
            TypeEvenement bean = recopieView();
            bean.setNew_(true);
            if (controleFiche(bean)) {
                entityTypeEvt = buildTypeEvenement(bean);
                if (!isUpdateTypeEvenement()) {
                    entityTypeEvt.setId(null);
                    entityTypeEvt = (YvsMutTypeEvenement) dao.save1(entityTypeEvt);
                    bean.setId(entityTypeEvt.getId());
                    typeEvenement.setId(entityTypeEvt.getId());
                    types.add(0, entityTypeEvt);
                } else {
                    dao.update(entityTypeEvt);
                    types.set(types.indexOf(entityTypeEvt), entityTypeEvt);
                }
                succes();
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
                        YvsMutTypeEvenement bean = types.get(types.indexOf(new YvsMutTypeEvenement(id)));
                        dao.delete(new YvsMutTypeEvenement(bean.getId()));
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

    public void deleteBean(YvsMutTypeEvenement y) {
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
            setUpdateTypeEvenement((ids != null) ? ids.length > 0 : false);
            if (isUpdateTypeEvenement()) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                YvsMutTypeEvenement bean = types.get(types.indexOf(new YvsMutTypeEvenement(id)));
                populateView(UtilMut.buildBeanTypeEvenement(bean));
                tabIds = "";
                input_reset = "";
                update("blog_form_type_evenement_");
            }
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            entityTypeEvt = (YvsMutTypeEvenement) ev.getObject();
            populateView(UtilMut.buildBeanTypeEvenement(entityTypeEvt));
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public YvsMutTauxContribution buildContribution(Contribution y) {
        YvsMutTauxContribution c = new YvsMutTauxContribution();
        if (y != null) {
            c.setId(y.getId());
            c.setMontantMin(y.getMontant());
            if ((y.getTypeContibution() != null) ? y.getTypeContibution().getId() != 0 : false) {
                c.setTypeContibution(new YvsMutActiviteType(y.getTypeContibution().getId()));
                ManagedTypeContribution w = (ManagedTypeContribution) giveManagedBean(ManagedTypeContribution.class);
                if (w != null) {
                    int idx = w.getTypes().indexOf(new YvsMutActiviteType(c.getTypeContibution().getId()));
                    if (idx > -1) {
                        c.setTypeContibution(w.getTypes().get(idx));
                    }
                }
            }
            c.setTypeEvenement(entityTypeEvt);
            c.setAuthor(currentUser);
        }
        return c;
    }

    public Contribution recopieViewContribution() {
        Contribution c = new Contribution();
        c.setId(contribution.getId());
        c.setMontant(contribution.getMontant());
        c.setTypeContibution(contribution.getTypeContibution());
        return c;
    }

    public boolean controleFicheContribution(Contribution bean) {
        if (bean.getMontant() == 0) {
            getErrorMessage("Vous devez entrer le montant");
            return false;
        }
        if ((bean.getTypeContibution() != null) ? bean.getTypeContibution().getId() == 0 : true) {
            getErrorMessage("Vous devez selectionner une type");
            return false;
        }
        return true;
    }

    public void populateViewContribution(Contribution bean) {
        cloneObject(contribution, bean);
        setUpdateContribution(true);
    }

    public void resetFicheContribution() {
        resetFiche(contribution);
        contribution.setTypeContibution(new TypeContribution());

        tabIds_contribution = "";
        input_reset_contribution = "";
        setUpdateContribution(false);
    }

    public boolean saveNewContribution() {
        if (input_reset_contribution != null && input_reset_contribution.equals("reset")) {
            setUpdateContribution(false);
            input_reset_contribution = "";
        }
        String action = isUpdateContribution() ? "Modification" : "Insertion";
        try {
            Contribution bean = recopieViewContribution();
            bean.setNew_(true);
            if (controleFicheContribution(bean)) {
                YvsMutTauxContribution entity = buildContribution(bean);
                if (!isUpdateContribution()) {
                    entity.setId(null);
                    entity = (YvsMutTauxContribution) dao.save1(entity);
                    bean.setId(entity.getId());
                    contribution.setId(entity.getId());
                } else {
                    dao.update(entity);
                }
                int idx = typeEvenement.getContributions().indexOf(entity);
                if (idx > -1) {
                    typeEvenement.getContributions().set(idx, entity);
                } else {
                    typeEvenement.getContributions().add(0, entity);
                }
                int idy = types.indexOf(new YvsMutTypeEvenement(typeEvenement.getId()));
                if (idy > -1) {
                    idx = types.get(idy).getContributions().indexOf(entity);
                    if (idx > -1) {
                        types.get(idy).getContributions().set(idx, entity);
                    } else {
                        types.get(idy).getContributions().add(0, entity);
                    }
                }

                succes();
                resetFicheContribution();
                update("data_contribution");
                update("blog_form_contribution");
            }
        } catch (Exception ex) {
            getException("Error " + action + " : " + ex.getMessage(), ex);
            getErrorMessage(action + " Impossible !");
        }
        return true;
    }

    public void deleteBeanContribution() {
        try {
            if ((tabIds_contribution != null) ? tabIds_contribution.length() > 0 : false) {
                String[] ids = tabIds_contribution.split("-");
                if ((ids != null) ? ids.length > 0 : false) {
                    for (String s : ids) {
                        long id = Integer.valueOf(s);
                        YvsMutTauxContribution bean = typeEvenement.getContributions().get(typeEvenement.getContributions().indexOf(new YvsMutTauxContribution(id)));
                        dao.delete(new YvsMutTauxContribution(bean.getId()));
                        typeEvenement.getContributions().remove(bean);
                        int idy = types.indexOf(new YvsMutTypeEvenement(typeEvenement.getId()));
                        if (idy > -1) {
                            types.get(idy).getContributions().remove(bean);
                        }
                    }
                    resetFicheContribution();
                    succes();
                    update("data_contribution");
                    update("blog_form_contribution");
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    public void deleteBeanContribution(YvsMutTauxContribution y) {
        try {
            if ((y != null) ? y.getId() > 0 : false) {
                dao.delete(y);
                typeEvenement.getContributions().remove(y);
                int idy = types.indexOf(new YvsMutTypeEvenement(typeEvenement.getId()));
                if (idy > -1) {
                    types.get(idy).getContributions().remove(y);
                }
                if (y.getId() == contribution.getId()) {
                    resetFicheContribution();
                }
                succes();
                update("data_contribution");
                update("blog_form_contribution");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    public void updateBeanContribution() {
        if ((tabIds_contribution != null) ? tabIds_contribution.length() > 0 : false) {
            String[] ids = tabIds_contribution.split("-");
            setUpdateContribution((ids != null) ? ids.length > 0 : false);
            if (isUpdateContribution()) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                YvsMutTauxContribution bean = typeEvenement.getContributions().get(typeEvenement.getContributions().indexOf(new YvsMutTauxContribution(id)));
                populateViewContribution(UtilMut.buildBeanContribution(bean));
                tabIds_contribution = "";
                update("data_contribution");
            }
        }
    }

    public void loadOnViewContribution(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutTauxContribution bean = (YvsMutTauxContribution) ev.getObject();
            populateViewContribution(UtilMut.buildBeanContribution(bean));
            update("blog_form_contribution");
        }
    }

    public void unLoadOnViewContribution(UnselectEvent ev) {
        resetFicheContribution();
        update("blog_form_contribution");
    }

}
