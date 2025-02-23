/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.credit;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.mutuelle.base.YvsMutGrilleTauxTypeCredit;
import yvs.entity.mutuelle.credit.YvsMutFraisTypeCredit;
import yvs.entity.mutuelle.credit.YvsMutTypeCredit;
import yvs.grh.UtilGrh;
import yvs.grh.bean.ManagedTypeCout;
import yvs.grh.bean.TypeCout;
import yvs.mutuelle.UtilMut;
import yvs.mutuelle.base.GrilleTaux;
import yvs.util.Constantes;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedTypeCredit extends Managed<TypeCredit, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{typeCredit}")
    private TypeCredit typeCredit;
    private List<YvsMutTypeCredit> types;

    private GrilleTaux grille = new GrilleTaux();
    private FraisTypeCredit frais = new FraisTypeCredit();

    private String tabIds, input_reset, tabIds_grille, input_reset_grille;
    private boolean updateType, updateGrille;

    YvsMutTypeCredit entityCredit;

    public ManagedTypeCredit() {
        types = new ArrayList<>();
    }

    public FraisTypeCredit getFrais() {
        return frais;
    }

    public void setFrais(FraisTypeCredit frais) {
        this.frais = frais;
    }

    public String getTabIds_grille() {
        return tabIds_grille;
    }

    public void setTabIds_grille(String tabIds_grille) {
        this.tabIds_grille = tabIds_grille;
    }

    public String getInput_reset_grille() {
        return input_reset_grille;
    }

    public void setInput_reset_grille(String input_reset_grille) {
        this.input_reset_grille = input_reset_grille;
    }

    public boolean isUpdateGrille() {
        return updateGrille;
    }

    public void setUpdateGrille(boolean updateGrille) {
        this.updateGrille = updateGrille;
    }

    public GrilleTaux getGrille() {
        return grille;
    }

    public void setGrille(GrilleTaux grille) {
        this.grille = grille;
    }

    public TypeCredit getTypeCredit() {
        return typeCredit;
    }

    public void setTypeCredit(TypeCredit typeCredit) {
        this.typeCredit = typeCredit;
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

    public boolean isUpdateType() {
        return updateType;
    }

    public void setUpdateType(boolean updateType) {
        this.updateType = updateType;
    }

    @Override
    public void loadAll() {
        loadAllTypeCredit();
        tabIds = "";
        tabIds_grille = "";
    }

    public void loadAllTypeCredit() {
        types = dao.loadNameQueries("YvsMutTypeCredit.findByMutuelleType", new String[]{"mutuelle", "typeAvance"}, new Object[]{currentMutuel, false});
    }

    public void loadByReport() {
        champ = new String[]{"mutuelle", "report"};
        val = new Object[]{currentMutuel, true};
        types = dao.loadNameQueries("YvsMutTypeCredit.findByReport", champ, val);
    }

    @Override
    public TypeCredit recopieView() {
        TypeCredit t = new TypeCredit();
        t.setCode(typeCredit.getCode());
        t.setDesignation(typeCredit.getDesignation());
        t.setId(typeCredit.getId());
        t.setNatureMontant(typeCredit.getNatureMontant());
        t.setMontantMaximal(typeCredit.getMontantMaximal());
        t.setPeriodeMaximal(typeCredit.getPeriodeMaximal());
        t.setAssistance(typeCredit.isAssistance());
        t.setTauxMaximal(typeCredit.getTauxMaximal());
        t.setImpayeDette(typeCredit.isImpayeDette());
        t.setNbreAvalise(typeCredit.getNbreAvalise());
        t.setPeriodicite(typeCredit.getPeriodicite());
        t.setTypeMensualite(typeCredit.getTypeMensualite());
        t.setFormuleInteret(typeCredit.getFormuleInteret());
        t.setModelRemboursement(typeCredit.getModelRemboursement());
        t.setReechellonagePossible(typeCredit.isReechellonagePossible());
        t.setFusionPossible(typeCredit.isFusionPossible());
        t.setAnticipationPossible(typeCredit.isAnticipationPossible());
        t.setPenaliteAnticipation(typeCredit.isPenaliteAnticipation());
        t.setTauxPenaliteAnticipation(typeCredit.getTauxPenaliteAnticipation());
        t.setBasePenaliteAnticipation(typeCredit.getBasePenaliteAnticipation());
        t.setSuspensionPossible(typeCredit.isSuspensionPossible());
        t.setPenaliteSuspension(typeCredit.isPenaliteSuspension());
        t.setTauxPenaliteSuspension(typeCredit.getTauxPenaliteSuspension());
        t.setBasePenaliteSuspension(typeCredit.getBasePenaliteSuspension());
        t.setPenaliteRetard(typeCredit.isPenaliteRetard());
        t.setTauxPenaliteRetard(typeCredit.getTauxPenaliteRetard());
        t.setBasePenaliteRetard(typeCredit.getBasePenaliteRetard());
        t.setByFusion(typeCredit.isByFusion());
        t.setCoefficientRemboursement(typeCredit.getCoefficientRemboursement());
        t.setSuffixeMontant((t.getNatureMontant().equals("Pourcentage") ? "% Salaire" : " Fcfa"));
        return t;
    }

    @Override
    public boolean controleFiche(TypeCredit bean) {
        if (bean.getCode() == null || bean.getCode().equals("")) {
            getErrorMessage("Vous devez entrer le code!");
            return false;
        }
        if (bean.getDesignation() == null || bean.getDesignation().equals("")) {
            getErrorMessage("Vous devez entrer la designation!");
            return false;
        }
        if (currentMutuel != null ? currentMutuel.getId() < 1 : true) {
            getErrorMessage("Vous devez etre connecté dans une mutuelle");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(TypeCredit bean) {
        cloneObject(typeCredit, bean);
        setUpdateType(true);
    }

    @Override
    public void resetFiche() {
        resetFiche(typeCredit);
        typeCredit.setGrilles(new ArrayList<YvsMutGrilleTauxTypeCredit>());
        typeCredit.setFrais(new ArrayList<YvsMutFraisTypeCredit>());
        typeCredit.setImpayeDette(false);
        typeCredit.setNatureMontant("Fixe");
        typeCredit.setSuffixeMontant("Fcfa");
        typeCredit.setPenaliteAnticipation(false);
        typeCredit.setPenaliteSuspension(false);
        typeCredit.setPenaliteRetard(false);
        typeCredit.setTypeMensualite(Constantes.TYPE_MENSUALITE_AMORTISSEMENT_FIXE);
        typeCredit.setFormuleInteret(Constantes.FORMULE_INTERET_SIMPLE);
        typeCredit.setModelRemboursement(Constantes.MODEL_REMBOURSEMENT_MODULABLE);
        typeCredit.setBasePenaliteAnticipation(Constantes.PENALITE_BASE_MENSUALITE);
        typeCredit.setBasePenaliteSuspension(Constantes.PENALITE_BASE_MENSUALITE);
        typeCredit.setBasePenaliteRetard(Constantes.PENALITE_BASE_MENSUALITE);

        setUpdateType(false);
        tabIds = "";
        input_reset = "";
        update("blog_form_type_credit_");
    }

    @Override
    public boolean saveNew() {
        if (input_reset != null && input_reset.equals("reset")) {
            setUpdateType(false);
            input_reset = "";
        }
        String action = isUpdateType() ? "Modification" : "Insertion";
        try {
            TypeCredit bean = recopieView();
            bean.setNew_(true);
            if (controleFiche(bean)) {
                entityCredit = UtilMut.buildTypeCredit(bean, currentUser, currentMutuel);
                if (!isUpdateType()) {
                    entityCredit.setId(null);
                    entityCredit = (YvsMutTypeCredit) dao.save1(entityCredit);
                    bean.setId(entityCredit.getId());
                    typeCredit.setId(entityCredit.getId());
                    types.add(0, entityCredit);
                } else {
                    dao.update(entityCredit);
                    types.set(types.indexOf(entityCredit), entityCredit);
                }
                succes();
                actionOpenOrResetAfter(this);
            }
        } catch (Exception ex) {
            getException("Error " + action + " : " + ex.getMessage(), ex);
            getErrorMessage(action + " Impossible !");
        }
        return true;
    }

    public YvsMutTypeCredit saveOne() {
        return saveNew(recopieView());
    }

    public YvsMutTypeCredit saveNew(TypeCredit bean) {
        String action = bean.getId() > 0 ? "Modification" : "Insertion";
        try {
            bean.setNew_(true);
            if (controleFiche(bean)) {
                YvsMutTypeCredit y = UtilMut.buildTypeCredit(bean, currentUser, currentMutuel);
                if (!isUpdateType()) {
                    y.setId(null);
                    y = (YvsMutTypeCredit) dao.save1(y);
                    bean.setId(y.getId());
                    types.add(0, y);
                } else {
                    dao.update(y);
                    types.set(types.indexOf(y), y);
                }
                return y;
            }
        } catch (Exception ex) {
            getException("Error " + action + " : " + ex.getMessage(), ex);
            getErrorMessage(action + " Impossible !");
        }
        return null;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? tabIds.length() > 0 : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsMutTypeCredit> list = new ArrayList<>();
                YvsMutTypeCredit bean;
                for (Long ids : l) {
                    bean = types.get(ids.intValue());
                    bean.setDateUpdate(new Date());
                    bean.setAuthor(currentUser);
                    list.add(bean);
                    dao.delete(bean);
                }
                types.removeAll(list);
                resetFiche();
                tabIds = "";
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression : " + ex.getMessage());
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
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    @Override
    public void updateBean() {
        if ((tabIds != null) ? tabIds.length() > 0 : false) {
            String[] ids = tabIds.split("-");
            setUpdateType((ids != null) ? ids.length > 0 : false);
            if (isUpdateType()) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                entityCredit = types.get(types.indexOf(new YvsMutTypeCredit(id)));
                populateView(UtilMut.buildBeanTypeCredit(entityCredit));
                tabIds = "";
                input_reset = "";
                update("blog_form_type_credit_");
            }
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            entityCredit = (YvsMutTypeCredit) ev.getObject();
            tabIds = types.indexOf(entityCredit) + "";
            populateView(UtilMut.buildBeanTypeCredit(entityCredit));
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public YvsMutGrilleTauxTypeCredit buildGrilleTaux(GrilleTaux y) {
        YvsMutGrilleTauxTypeCredit g = new YvsMutGrilleTauxTypeCredit();
        if (y != null) {
            g.setId(y.getId());
            g.setMontantMinimal(y.getMontantMinimal());
            g.setMontantMaximal(y.getMontantMaximal());
            g.setPeriodeMaximal(y.getPeriodeMaximal());
            g.setTaux(y.getTaux());
            g.setTypeCredit(entityCredit);
            g.setAuthor(currentUser);
        }
        return g;
    }

    public GrilleTaux recopieViewGrille() {
        GrilleTaux g = new GrilleTaux();
        g.setId(grille.getId());
        g.setMontantMinimal(grille.getMontantMinimal());
        g.setMontantMaximal(grille.getMontantMaximal());
        g.setPeriodeMaximal(grille.getPeriodeMaximal());
        g.setTaux(grille.getTaux());
        return g;
    }

    public boolean controleFicheGrille(GrilleTaux bean) {
        if (bean.getTaux() == 0) {
            getErrorMessage("Vous devez entrer le taux");
            return false;
        }
        if (bean.getMontantMinimal() == 0) {
            getErrorMessage("Vous devez entrer le montant minimal");
            return false;
        }
        if (bean.getMontantMaximal() == 0) {
            getErrorMessage("Vous devez entrer le montant maximal");
            return false;
        }
        if (bean.getPeriodeMaximal() == 0) {
            getErrorMessage("Vous devez entrer la durée");
            return false;
        }
        if (bean.getMontantMaximal() < bean.getMontantMinimal()) {
            getErrorMessage("le montant maximal ne peut etre inferieur au montant minimal!");
            return false;
        }
        if (bean.getMontantMaximal() > typeCredit.getMontantMaximal()) {
            getErrorMessage("le montant maximal depasse le plafond du type de credit!");
            return false;
        }
        if (bean.getMontantMinimal() > typeCredit.getMontantMaximal()) {
            getErrorMessage("le montant minimal depasse le plafond du type de credit!");
            return false;
        }
        return true;
    }

    public void populateViewGrille(GrilleTaux bean) {
        cloneObject(grille, bean);
        setUpdateGrille(true);
    }

    public void resetFicheGrille() {
        grille = new GrilleTaux();
        setUpdateGrille(false);
        tabIds_grille = "";
        input_reset_grille = "";
    }

    public void saveNewGrille() {
        if (input_reset_grille != null && input_reset_grille.equals("reset")) {
            setUpdateType(false);
            input_reset_grille = "";
        }
        String action = isUpdateType() ? "Modification" : "Insertion";
        try {
            GrilleTaux bean = recopieViewGrille();
            bean.setNew_(true);
            if (controleFicheGrille(bean)) {
                YvsMutGrilleTauxTypeCredit entity = buildGrilleTaux(bean);
                if (!isUpdateGrille()) {
                    entity.setId(null);
                    entity = (YvsMutGrilleTauxTypeCredit) dao.save1(entity);
                    bean.setId(entity.getId());
                    grille.setId(entity.getId());
                    typeCredit.getGrilles().add(0, entity);
                } else {
                    dao.update(entity);
                    typeCredit.getGrilles().set(typeCredit.getGrilles().indexOf(entity), entity);
                }
                resetFicheGrille();
                succes();
                update("data_grille_type_credit_");
            }
        } catch (Exception ex) {
            getException("Error " + action + " : " + ex.getMessage(), ex);
            getErrorMessage(action + " Impossible !");
        }
    }

    public void deleteBeanGrille() {
        try {
            if ((tabIds_grille != null) ? tabIds_grille.length() > 0 : false) {
                String[] ids = tabIds.split("-");
                if ((ids != null) ? ids.length > 0 : false) {
                    for (String s : ids) {
                        long id = Integer.valueOf(s);
                        YvsMutGrilleTauxTypeCredit bean = typeCredit.getGrilles().get(typeCredit.getGrilles().indexOf(new YvsMutGrilleTauxTypeCredit(id)));
                        dao.delete(new YvsMutGrilleTauxTypeCredit(bean.getId()));
                        typeCredit.getGrilles().remove(bean);
                    }
                    resetFicheGrille();
                    succes();
                    update("data_grille_type_credit_");
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression : " + ex.getMessage());
        }
    }

    public void deleteBeanGrille(YvsMutGrilleTauxTypeCredit y) {
        try {
            if (y != null) {
                dao.delete(y);
                typeCredit.getGrilles().remove(y);
                if (grille.getId() == y.getId()) {
                    resetFicheGrille();
                }
                succes();
                update("data_grille_type_credit_");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression : " + ex.getMessage());
        }
    }

    public void loadOnViewGrille(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsMutGrilleTauxTypeCredit y = (YvsMutGrilleTauxTypeCredit) ev.getObject();
            populateViewGrille(UtilMut.buildBeanGrilleTaux(y));
        }
    }

    public void unLoadOnViewGrille(UnselectEvent ev) {
        resetFicheGrille();
    }

    public void updateBeanGrille() {
        tabIds_grille = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tabIds");
        if ((tabIds_grille != null) ? tabIds_grille.length() > 0 : false) {
            String[] ids = tabIds_grille.split("-");
            setUpdateType((ids != null) ? ids.length > 0 : false);
            if (isUpdateType()) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                YvsMutGrilleTauxTypeCredit bean = typeCredit.getGrilles().get(typeCredit.getGrilles().indexOf(new YvsMutGrilleTauxTypeCredit(id)));
                populateViewGrille(UtilMut.buildBeanGrilleTaux(bean));
                tabIds_grille = "";
            } else {
                resetFicheGrille();
            }
        } else {
            resetFicheGrille();
        }
        update("blog_form_grille_type_credit_");
    }

    public void initGrilleTaux() {
        populateView(UtilMut.buildBeanTypeCredit(entityCredit));
        update("data_grille_type_credit_");
        update("txt_suffixe_montant");
    }

    public void initGrilleTaux(YvsMutTypeCredit y) {
        entityCredit = y;
        initGrilleTaux();
    }

    public void chooseNatureMontant() {
        if ((typeCredit != null) ? typeCredit.getNatureMontant() != null : false) {
            switch (typeCredit.getNatureMontant()) {
                case "Fixe":
                    typeCredit.setSuffixeMontant("Fcfa");
                    break;
                case "Pourcentage":
                    typeCredit.setSuffixeMontant("% Salaire");
                    break;
                default:
                    break;
            }
        }
    }

    public void chooseFrais(ValueChangeEvent ev) {
        if (ev != null ? ev.getNewValue() != null : false) {
            long id = (long) ev.getNewValue();
            ManagedTypeCout w = (ManagedTypeCout) giveManagedBean(ManagedTypeCout.class);
            if (w != null) {
                if (id > 0) {
                    frais.getType().setId(id);
                    int idx = w.getTypes().indexOf(new YvsGrhTypeCout(frais.getType().getId()));
                    if (idx > -1) {
                        frais.setType(UtilGrh.buildBeanTypeCout(w.getTypes().get(idx)));
                    }
                } else {
                    boolean paginer = true;
                    boolean next = false;
                    if (id == -1) {
                        w.setTypeCout(new TypeCout(Constantes.COUT_CREDIT));
                        openDialog("dlgAddTypeCout");
                    } else if (id == -2) {
                        next = false;
                    } else if (id == -3) {
                        next = true;
                    } else {
                        paginer = false;
                    }
                    if (paginer) {
                        w.loadAllType(next, false);
                    }
                    frais.getType().setId(0);
                }
            } else {
                frais.getType().setId(id);
            }
        }
    }

    public FraisTypeCredit recopiewFrais(TypeCredit y) {
        FraisTypeCredit r = new FraisTypeCredit();
        r.setId(frais.getId());
        r.setMontant(frais.getMontant());
        r.setType(frais.getType());
        r.setCredit(y);
        return r;
    }

    public boolean controleFiche(FraisTypeCredit bean) {
        if (bean == null) {
            getErrorMessage("Action impossible");
            return false;
        }
        if (bean.getType() != null ? bean.getType().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le frais");
            return false;
        }
        if (bean.getCredit() != null ? bean.getCredit().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le type");
            return false;
        }
        return true;
    }

    public void saveNewFrais() {
        try {
            FraisTypeCredit bean = recopiewFrais(typeCredit);
            if (controleFiche(bean)) {
                YvsMutFraisTypeCredit y = UtilMut.buildFraisTypeCredit(bean, currentUser);
                if (bean != null ? bean.getId() < 1 : true) {
                    y.setId(null);
                    y = (YvsMutFraisTypeCredit) dao.save1(y);
                } else {
                    dao.update(y);
                }
                int idx = typeCredit.getFrais().indexOf(y);
                if (idx > -1) {
                    typeCredit.getFrais().set(idx, y);
                } else {
                    typeCredit.getFrais().add(0, y);
                }
                idx = entityCredit.getFrais().indexOf(y);
                if (idx > -1) {
                    entityCredit.getFrais().set(idx, y);
                } else {
                    entityCredit.getFrais().add(0, y);
                }
                idx = types.indexOf(entityCredit);
                if (idx > -1) {
                    types.set(idx, entityCredit);
                }
                frais = new FraisTypeCredit();
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            System.err.println("error Action : " + ex.getMessage());
        }
    }

    public void deleteBeanFrais(YvsMutFraisTypeCredit y) {
        try {
            if (y != null) {
                dao.delete(y);
                typeCredit.getFrais().remove(y);
                entityCredit.getFrais().remove(y);
                int idx = types.indexOf(entityCredit);
                if (idx > -1) {
                    types.set(idx, entityCredit);
                }
                if (frais.getId() == y.getId()) {
                    frais = new FraisTypeCredit();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression : " + ex.getMessage());
        }
    }

    public void loadOnViewFrais(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsMutFraisTypeCredit y = (YvsMutFraisTypeCredit) ev.getObject();
            cloneObject(frais, UtilMut.buildBeanFraisTypeCredit(y));
        }
    }

    public void unLoadOnViewFrais(UnselectEvent ev) {
        resetFicheFrais();
    }

    public void resetFicheFrais() {
        frais = new FraisTypeCredit();
    }
}
