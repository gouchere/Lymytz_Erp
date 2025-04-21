/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.el.MethodExpression;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.InfosModeReglement;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.ModeDeReglement;
import yvs.base.compta.ModeReglementBanque;
import yvs.base.compta.ModelReglement;
import yvs.base.compta.PhasesReglements;
import yvs.base.compta.TrancheReglement;
import yvs.base.compta.UtilCompta;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseModeReglementBanque;
import yvs.entity.base.YvsBaseModeReglementInformations;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBaseTrancheReglement;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaPhaseReglement;
import yvs.entity.compta.YvsComptaPhaseReglementAutorisation;
import yvs.entity.users.YvsNiveauAcces;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedModeReglement extends Managed<ModeDeReglement, YvsBaseCaisse> implements Serializable {

    private ModeDeReglement mode = new ModeDeReglement();
    private List<YvsBaseModeReglement> modes;
    private YvsBaseModeReglement modeSelect;

    private ModelReglement model = new ModelReglement();
    private List<YvsBaseModelReglement> models;
    private YvsBaseModelReglement modelSelect = new YvsBaseModelReglement();

    private TrancheReglement tranche = new TrancheReglement();
    private YvsBaseTrancheReglement trancheSelect;

    private PhasesReglements phase = new PhasesReglements();
    private YvsComptaPhaseReglement selectPhase;
    private List<YvsNiveauAcces> listNiveauAcces, selectionNiveauAcces;
    private String tabIds, tablIds, tabIds_tranche, tabIds_modeR;

    private String fusionneTo, fusionneModePTo;
    private List<String> fusionnesBy, fusionnesModePBy;

    MethodExpression method = null;

    public ManagedModeReglement() {
        modes = new ArrayList<>();
        models = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
        fusionnesModePBy = new ArrayList<>();
        listNiveauAcces = new ArrayList<>();
        selectionNiveauAcces = new ArrayList<>();
    }

    public List<YvsNiveauAcces> getListNiveauAcces() {
        return listNiveauAcces;
    }

    public void setListNiveauAcces(List<YvsNiveauAcces> listNiveauAcces) {
        this.listNiveauAcces = listNiveauAcces;
    }

    public List<YvsNiveauAcces> getSelectionNiveauAcces() {
        return selectionNiveauAcces;
    }

    public void setSelectionNiveauAcces(List<YvsNiveauAcces> selectionNiveauAcces) {
        this.selectionNiveauAcces = selectionNiveauAcces;
    }

    public YvsComptaPhaseReglement getSelectPhase() {
        return selectPhase;
    }

    public void setSelectPhase(YvsComptaPhaseReglement selectPhase) {
        this.selectPhase = selectPhase;
    }

    public PhasesReglements getPhase() {
        return phase;
    }

    public void setPhase(PhasesReglements phase) {
        this.phase = phase;
    }

    public TrancheReglement getTranche() {
        return tranche;
    }

    public void setTranche(TrancheReglement tranche) {
        this.tranche = tranche;
    }

    public YvsBaseTrancheReglement getTrancheSelect() {
        return trancheSelect;
    }

    public void setTrancheSelect(YvsBaseTrancheReglement trancheSelect) {
        this.trancheSelect = trancheSelect;
    }

    public String getTabIds_tranche() {
        return tabIds_tranche;
    }

    public void setTabIds_tranche(String tabIds_tranche) {
        this.tabIds_tranche = tabIds_tranche;
    }

    public ModelReglement getModel() {
        return model;
    }

    public void setModel(ModelReglement model) {
        this.model = model;
    }

    public List<YvsBaseModelReglement> getModels() {
        return models;
    }

    public void setModels(List<YvsBaseModelReglement> models) {
        this.models = models;
    }

    public YvsBaseModelReglement getModelSelect() {
        return modelSelect;
    }

    public void setModelSelect(YvsBaseModelReglement modelSelect) {
        this.modelSelect = modelSelect;
    }

    public String getTablIds() {
        return tablIds;
    }

    public void setTablIds(String tablIds) {
        this.tablIds = tablIds;
    }

    public ModeDeReglement getMode() {
        return mode;
    }

    public void setMode(ModeDeReglement mode) {
        this.mode = mode;
    }

    public List<YvsBaseModeReglement> getModes() {
        return modes;
    }

    public void setModes(List<YvsBaseModeReglement> modes) {
        this.modes = modes;
    }

    public YvsBaseModeReglement getModeSelect() {
        return modeSelect;
    }

    public void setModeSelect(YvsBaseModeReglement modeSelect) {
        this.modeSelect = modeSelect;
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

    public String getFusionneModePTo() {
        return fusionneModePTo;
    }

    public void setFusionneModePTo(String fusionneModePTo) {
        this.fusionneModePTo = fusionneModePTo;
    }

    public List<String> getFusionnesBy() {
        return fusionnesBy;
    }

    public void setFusionnesBy(List<String> fusionnesBy) {
        this.fusionnesBy = fusionnesBy;
    }

    public List<String> getFusionnesUniteBy() {
        return fusionnesModePBy;
    }

    public void setFusionnesUniteBy(List<String> fusionnesUniteBy) {
        this.fusionnesModePBy = fusionnesUniteBy;
    }

    public String getTabIds_modeR() {
        return tabIds_modeR;
    }

    public void setTabIds_modeR(String tabIds_modeR) {
        this.tabIds_modeR = tabIds_modeR;
    }

    @Override
    public void loadAll() {
        loadAllMode();
        loadAllModel();
    }

    public void loadActif() {
        loadActifMode();
        loadActifModel();
    }

    public void loadAllMode() {
        nameQueri = "YvsBaseModeReglement.findAll";
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        modes = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadActifMode() {
        nameQueri = "YvsBaseModeReglement.findByActif";
        champ = new String[]{"societe", "actif"};
        val = new Object[]{currentAgence.getSociete(), true};
        modes = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadAllModel() {
        if (tranche == null) {
            tranche = new TrancheReglement();
        }
        if (tranche.getMode() != null ? tranche.getMode().getId() < 1 : true) {
            tranche.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        }
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsBaseModelReglement.findAll";
        models = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadActifModel() {
        champ = new String[]{"societe", "actif"};
        val = new Object[]{currentAgence.getSociete(), true};
        nameQueri = "YvsBaseModelReglement.findByActif";
        models = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadModels(String type) {
        loadMode(type);
        loadModel(type);
    }

    public void loadMode(String type) {
        nameQueri = "YvsBaseModeReglement.findByActif";
        champ = new String[]{"societe", "actif"};
        val = new Object[]{currentAgence.getSociete(), true};
        modes = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadModel(String type) {
        String query = "SELECT DISTINCT ua.code FROM yvs_base_model_reglement d INNER JOIN yvs_base_users_acces ua ON d.code_acces= ua.code WHERE d.actif IS TRUE AND ua.users=? AND d.societe = ?";
        List<Long> acces = dao.loadListBySqlQuery(query, new Options[]{new Options(currentUser.getUsers().getId(), 1), new Options(currentAgence.getSociete().getId(), 2)});
        if (acces != null ? acces.isEmpty() : true) {
            acces = new ArrayList<Long>() {
                {
                    add(-1L);
                }
            };
        }
        nameQueri = "YvsBaseModelReglement.findByType2ActifAcces";
        champ = new String[]{"societe", "actif", "type1", "type2", "acces"};
        val = new Object[]{currentAgence.getSociete(), true, 'M', type.charAt(0), acces};
        models = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadModeForAcompte() {
        nameQueri = "YvsBaseModeReglement.findForAcompte";
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        modes = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadAllNiveauAcces() {
        champ = new String[]{"societe", "superAdmin"};
        val = new Object[]{currentAgence.getSociete(), false};
        listNiveauAcces = dao.loadNameQueries("YvsNiveauAcces.findBySuperAdmin", champ, val);
    }

    public void verifyDefaultModeEspece() {
        YvsBaseModeReglement espece = modeEspece();
        if (espece != null ? espece.getId() < 1 : true) {
            openAction("Il n'existe pas de mode de paiement par défaut... Voulez-vous le créer?");
            CommandButton action = (CommandButton) giveObjectOnView("frm-dlgAction:btn-action_dlgAction");
            if (action != null) {
                if (method == null) {
                    method = context.getApplication().getExpressionFactory().createMethodExpression(
                            context.getELContext(), "#{managedModeReglement.createDefautMode}", null, new Class[]{ActionEvent.class});
                }
                if (method != null) {
                    MethodExpressionActionListener expression = new MethodExpressionActionListener(method);
                    action.removeActionListener(expression);
                    action.addActionListener(expression);
                }
            }
        }
    }

    public void createDefautMode(ActionEvent event) {
        createDefautMode();
    }

    public void createDefautMode() {
        try {
            YvsBaseModeReglement m = new YvsBaseModeReglement();
            m.setActif(true);
            m.setDesignation("ESPECE");
            m.setAuthor(currentUser);
            m.setDefaultMode(true);
            m.setDescription("Mode de paiement par défaut");
            m.setSociete(currentAgence.getSociete());
            m.setTypeReglement(Constantes.MODE_PAIEMENT_ESPECE);
            dao.save(m);
            succes();
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("managedModeReglement (createDefautMode)", ex);
        }
    }

    public YvsBaseTrancheReglement buildTrancheReglement(TrancheReglement y) {
        YvsBaseTrancheReglement t = UtilCom.buildTrancheReglement(y, currentUser);
        if (t != null) {
            if ((y.getMode() != null) ? y.getMode().getId() > 0 : false) {
                t.setMode(modes.get(modes.indexOf(new YvsBaseModeReglement((long) y.getMode().getId()))));
            }
            if ((y.getModel() != null) ? y.getModel().getId() > 0 : false) {
                t.setModel(models.get(models.indexOf(new YvsBaseModelReglement(y.getModel().getId()))));
            }
        }
        return t;
    }

    public boolean controleFicheTranche(TrancheReglement bean) {
        if ((bean.getModel() != null) ? bean.getModel().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le model de reglement");
            return false;
        }
        if ((bean.getMode() != null) ? bean.getMode().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le mode de reglement");
            return false;
        }
        double s = 0;
        if (bean.getNumero() > bean.getModel().getTranches().size() + 1) {
            getErrorMessage("Vous ne pouvez pas attribuer une priorité qui dépasse le nombre de tranche");
            return false;
        }
        for (YvsBaseTrancheReglement t : bean.getModel().getTranches()) {
            if (!bean.isUpdate() && t.getNumero().equals(bean.getNumero())) {
                getErrorMessage("Vous avez déja attribué cette priorité");
                return false;
            }
            if (bean.isUpdate() && t.getId().equals(bean.getId())) {
                continue;
            }
            s += t.getTaux();
        }
        if (s >= 100) {
            getErrorMessage("Vous avez déja atteint 100%");
            return false;
        }
        s += bean.getTaux();
        if (s > 100) {
            getErrorMessage("Le total des taux sera superieur a 100%");
            return false;
        }
        return true;
    }

    @Override
    public boolean controleFiche(ModeDeReglement bean) {
        if (bean == null) {
            getErrorMessage("Aucun règlement n'a été trouvé");
            return false;
        }
        if ((bean.getDesignation() == null) ? true : bean.getDesignation().trim().equals("")) {
            getErrorMessage("Vous devez preciser la désignation");
            return false;
        }
        if (bean.isDefaut()) {
            nameQueri = "YvsBaseModeReglement.findByDefault";
            champ = new String[]{"societe", "actif", "type", "defaut"};
            val = new Object[]{currentAgence.getSociete(), true, bean.getTypeReglement(), true};
            YvsBaseModeReglement y = (YvsBaseModeReglement) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (y != null ? (y.getId() != null ? bean.getId() != y.getId() : false) : false) {
                getErrorMessage("Vous avez déjà un mode par défaut pour ce type");
                return false;
            }
        }
        nameQueri = "YvsBaseModeReglement.findByDesignationEx";
        champ = new String[]{"societe", "designation", "id"};
        val = new Object[]{currentAgence.getSociete(), bean.getDesignation(), bean.getId()};
        YvsBaseModeReglement y = (YvsBaseModeReglement) dao.loadOneByNameQueries(nameQueri, champ, val);
        if (y != null) {
            getErrorMessage("Vous avez déjà un mode avec cette désignation");
            return false;
        }
        return true;
    }

    public boolean controleFiche(InfosModeReglement bean) {
        if (bean == null) {
            return false;
        }
        if (Util.asString(bean.getAuthorisationHeader())) {
            return true;
        }
        if (Util.asString(bean.getMerchantKey())) {
            return true;
        }
        if (Util.asString(bean.getCurrency())) {
            return true;
        }
        if (Util.asString(bean.getChannelUserMsisdn())) {
            return true;
        }
        return false;
    }

    public boolean controleFiche(ModeReglementBanque bean) {
        if (bean == null) {
            return false;
        }
        if (Util.asString(bean.getCodeBanque())) {
            return true;
        }
        if (Util.asString(bean.getCodeGuichet())) {
            return true;
        }
        if (Util.asString(bean.getNumero())) {
            return true;
        }
        if (Util.asString(bean.getCle())) {
            return true;
        }
        return false;
    }

    public boolean controleFiche(ModelReglement bean) {
        if (bean == null) {
            getErrorMessage("L'object ne peut pas etre null");
            return false;
        }
        if (bean.getDesignation() == null || bean.getDesignation().trim().equals("")) {
            getErrorMessage("Vous devez preciser l'indicateur");
            return false;
        }
        if (!bean.isUpdate()) {
            List<YvsBaseModelReglement> lm = dao.loadNameQueries("YvsBaseModelReglement.findByReference", new String[]{"societe", "reference"}, new Object[]{currentAgence.getSociete(), bean.getDesignation()});
            if (lm != null ? !lm.isEmpty() : false) {
                getErrorMessage("Vousa avez deja un model avec cette reference");
                return false;
            }
        }
        return true;
    }

    @Override
    public ModeDeReglement recopieView() {
        return mode;
    }

    public TrancheReglement recopieViewTranche() {
        return recopieViewTranche(tranche);
    }

    public TrancheReglement recopieViewTranche(TrancheReglement tranche) {
        TrancheReglement t = new TrancheReglement();
        t.setId(tranche.getId());
        t.setIntervalJour(tranche.getIntervalJour());
        t.setTaux(tranche.getTaux());
        t.setNumero(tranche.getNumero());
        t.setMode(tranche.getMode());
        t.setModel(tranche.getModel());
        t.setUpdate(tranche.isUpdate());
        t.setNew_(true);
        return t;
    }

    @Override
    public void populateView(ModeDeReglement bean) {
        cloneObject(mode, bean);
    }

    public void _populateView(ModelReglement bean) {
        cloneObject(model, bean);
    }

    public void populateViewTranche(TrancheReglement bean) {
        cloneObject(tranche, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(mode);
        mode.setDefaut(false);
        modeSelect = null;
        tabIds = "";
    }

    public void resetFicheModel() {
        _resetFicheModel(model);

        modelSelect = new YvsBaseModelReglement();
        tablIds = "";
    }

    public void _resetFicheModel(ModelReglement model) {
        resetFiche(model);
        model.setPayeBeforeValide(false);
        model.setTranches(new ArrayList<YvsBaseTrancheReglement>());
        model.setType('M');
        resetFicheTranche();
    }

    public void resetFicheTranche() {
        _resetFicheTranche(tranche, UtilCom.buildBeanModelReglement(modelSelect));
        trancheSelect = null;
        tabIds_tranche = "";
        update("form_tranche_model_reglement");
    }

    public void _resetFicheTranche(TrancheReglement b, ModelReglement y) {
        b = new TrancheReglement();
        b.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        b.setModel(y);

        double s = 0;
        if (b.getModel() != null ? b.getModel().getTranches() != null : false) {
            for (int i = 1; i < b.getModel().getTranches().size() + 2; i++) {
                boolean trouv = false;
                for (YvsBaseTrancheReglement t : b.getModel().getTranches()) {
                    if (t.getNumero().equals(i)) {
                        trouv = true;
                        break;
                    }
                }
                if (!trouv) {
                    b.setNumero(i);
                    break;
                }
            }
            for (YvsBaseTrancheReglement t : b.getModel().getTranches()) {
                s += t.getTaux();
            }
        }
        if (s < 100) {
            b.setTaux(100 - s);
        } else {
            b.setTaux(0);
        }
    }

    public void selectOneMode(SelectEvent ev) {
        if (ev != null) {
            YvsBaseModeReglement mod = (YvsBaseModeReglement) ev.getObject();
            mode = UtilCom.buildBeanModeReglement(mod);
            mode.setPhases(dao.loadNameQueries("YvsComptaPhaseReglement.findByMode", new String[]{"mode"}, new Object[]{mod}));
            Collections.sort(mode.getPhases(), new YvsComptaPhaseReglement());
        }
    }

    public void selectOneModeReg(YvsBaseModeReglement y) {
        modeSelect = y;
        openDialog("dlgConfirmDelModeR");
        update("tabview_param_com:table_mod_reg_actif");

    }

    public void toogleActiveOneModeReg(YvsBaseModeReglement y) {
        y.setActif(!y.getActif());
        y.setAuthor(currentUser);
        dao.update(y);
        update("tabview_param_com:table_mod_reg_actif");
    }

    public void toogleActiveReglementOk(YvsComptaPhaseReglement y) {
        y.setReglementOk(!y.getReglementOk());
        y.setAuthor(currentUser);
        y.setDateUpdate(new Date());
        dao.update(y);
        update("tabview_param_com:table_mod_reg_actif");
    }

    public void toogleActivePhase(YvsComptaPhaseReglement y) {
        y.setActif(!y.getActif());
        y.setAuthor(currentUser);
        y.setDateUpdate(new Date());
        dao.update(y);
        update("tabview_param_com:table_mod_reg_actif");
    }

    public void toogleActiveForEmission(YvsComptaPhaseReglement y) {
        y.setForEmission(!y.getForEmission());
        y.setAuthor(currentUser);
        dao.update(y);
        update("tabview_param_com:table_mod_reg_actif");
    }

    public void choixLineEtape(YvsComptaPhaseReglement etape) {
        selectPhase = etape;
        List<YvsNiveauAcces> temp = new ArrayList<>(listNiveauAcces);
        for (YvsComptaPhaseReglementAutorisation au : etape.getAutorisations()) {
            if (listNiveauAcces.contains(au.getNiveauAcces())) {
                temp.remove(au.getNiveauAcces());
            }
        }
        YvsComptaPhaseReglementAutorisation a;
        long id = -1000;
        for (YvsNiveauAcces n : temp) {
            a = new YvsComptaPhaseReglementAutorisation(id++);
            a.setCanValide(false);
            a.setNiveauAcces(n);
            a.setEtapeValide(etape);
            selectPhase.getAutorisations().add(a);
        }
        openDialog("dlgNiveauAcces");
        update("tablePhaseDroitValid");
    }

    public void toogleDroitValidDoc(SelectEvent ev) {
        if (ev != null) {
            YvsComptaPhaseReglementAutorisation au = (YvsComptaPhaseReglementAutorisation) ev.getObject();
            toogleDroitValidDoc_(au);
        }

    }

    public void toogleDroitValidDoc_(YvsComptaPhaseReglementAutorisation au) {
        if (au != null) {
            au.setCanValide(!au.getCanValide());
            au.setAuthor(currentUser);
            if (au.getId() > 0) {
                dao.update(au);
            } else {
                au.setId(null);
                au = (YvsComptaPhaseReglementAutorisation) dao.save1(au);
            }
        }

    }

    public void toogleNotifieValid(YvsComptaPhaseReglementAutorisation au) {
        if (au != null) {
            au.setCanNotify(!au.getCanNotify());
            au.setAuthor(currentUser);
            if (au.getId() > 0) {
                dao.update(au);
            } else {
                au.setId(null);
                au = (YvsComptaPhaseReglementAutorisation) dao.save1(au);
            }
        }

    }

    public void activerDesactiver(YvsBaseModeReglement y) {
        y.setActif(!y.getActif());
        y.setDateUpdate(new Date());
        y.setAuthor(currentUser);
        dao.update(y);
    }

    public void deleteBean_(YvsBaseModeReglement y) {
        modeSelect = y;
    }

    public void deleteBean_() {
        try {
            if (modeSelect != null) {
                modeSelect.setAuthor(currentUser);
                modeSelect.setDateUpdate(new Date());
                dao.delete(modeSelect);
                modes.remove(modeSelect);
                succes();
                update("tabview_param_com:table_mod_reg_actif");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            getException("Lymytz Error >>> ", ex);
        }
    }

    @Override
    public boolean saveNew() {
        YvsBaseModeReglement entity = _saveNew(mode);
        if (entity != null ? entity.getId() > 0 : false) {
            update("select_mode_tranche_model");
            return true;
        }
        return false;
    }

    public YvsBaseModeReglement _saveNew(ModeDeReglement m) {
        String action = m.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(m)) {
                YvsBaseModeReglement y = UtilCom.buildModeReglement(m, currentUser, currentAgence.getSociete());
                if (m.getId() <= 0) {
                    y.setDateSave(new Date());
                    y.setId(null);
                    y = (YvsBaseModeReglement) dao.save1(y);
                    m.setId(y.getId().intValue());
                    mode.setId(m.getId());
                    modes.add(0, y);
                } else {
                    dao.update(y);
                    int idx = modes.indexOf(y);
                    if (idx >= 0) {
                        modes.set(idx, y);
                    }
                }
                if (m.getInfos() != null ? controleFiche(m.getInfos()) : false) {
                    m.getInfos().setMode(mode);
                    m.getInfos().setDateSave(new Date());
                    YvsBaseModeReglementInformations i = UtilCom.buildInfosModeReglement(m.getInfos(), currentUser);
                    if (m.getInfos().getId() > 0) {
                        dao.update(i);
                    } else {
                        i.setId(null);
                        i = (YvsBaseModeReglementInformations) dao.save1(i);
                        m.getInfos().setId(i.getId());
                    }
                    y.setInfos(i);
                }
                if (m.getBanque() != null ? controleFiche(m.getBanque()) : false) {
                    m.getBanque().setMode(mode);
                    m.getBanque().setDateSave(new Date());
                    YvsBaseModeReglementBanque b = UtilCom.buildModeReglementBanque(m.getBanque(), currentUser);
                    if (m.getInfos().getId() > 0) {
                        dao.update(b);
                    } else {
                        b.setId(null);
                        b = (YvsBaseModeReglementBanque) dao.save1(b);
                        m.getBanque().setId(b.getId());
                    }
                    y.setBanque(b);
                }
                succes();
                return y;
            }
            return null;
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return null;
        }
    }

    public void ecouteSaisieCG() {
        //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
        ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (service != null) {
            service.findCompteByNum(phase.getCompteGeneral().getNumCompte());
            phase.getCompteGeneral().setError(service.getListComptes().isEmpty());
            if (service.getListComptes() != null) {
                if (!service.getListComptes().isEmpty()) {
                    if (service.getListComptes().size() == 1) {
                        phase.getCompteGeneral().setError(false);
                        cloneObject(phase.getCompteGeneral(), UtilCompta.buildBeanCompte(service.getListComptes().get(0)));
                    } else {
                        phase.getCompteGeneral().setError(false);
                        openDialog("dlgCmpteG");
                        update("table_cptG_modR");
                    }
                } else {
                    phase.getCompteGeneral().setError(true);
                }
            } else {
                phase.getCompteGeneral().setError(true);
            }
        }

    }

    public void choisirCompte(SelectEvent ev) {
        if (ev != null) {
            cloneObject(phase.getCompteGeneral(), UtilCompta.buildSimpleBeanCompte(((YvsBasePlanComptable) ev.getObject())));
            update("tabview_param_com:chmp_compteGen_param_modr");
        }
    }

    public void createnewPhaseReg() {
        if (mode.getId() > 0) {
            if (phase.getPhase() != null) {
                YvsComptaPhaseReglement ph = new YvsComptaPhaseReglement();
                ph.setActif(true);
                ph.setAuthor(currentUser);
                ph.setModeReglement(new YvsBaseModeReglement((long) mode.getId()));
                ph.setNumeroPhase(phase.getNumeroPhase());
                ph.setPhase(phase.getPhase());
                ph.setForEmission(phase.isForEmission());
                ph.setCodeComptable(phase.getCodeComptable());
                ph.setLibelle(phase.getLibellePhase());
                ph.setReglementOk(phase.isReglementOk());
                ph.setActionInBanque(phase.isActionInBanque());
                if (phase.getId() <= 0) {
                    ph.setId(null);
                    ph = (YvsComptaPhaseReglement) dao.save1(ph);
                    mode.getPhases().add(ph);
                } else {
                    ph.setId(phase.getId());
                    dao.update(ph);
                    int idx = mode.getPhases().indexOf(ph);
                    if (idx >= 0) {
                        mode.getPhases().set(idx, ph);
                    }
                }
                phase = new PhasesReglements();
                update("tabview_param_com:table_phase_mode_reg");
            } else {
                getErrorMessage("Veuillez entrer la désignation de cette phase");
            }
        } else {
            getErrorMessage("Aucun mode de paiement n'a été trouvé");
        }
    }

    public void selectionePhase(SelectEvent ev) {
        selectPhase = (YvsComptaPhaseReglement) ev.getObject();
        phase = new PhasesReglements();
        phase.setId(selectPhase.getId());
        phase.setLibellePhase(selectPhase.getLibelle());
        phase.setNumeroPhase(selectPhase.getNumeroPhase());
        phase.setPhase(selectPhase.getPhase());
        phase.setReglementOk(selectPhase.getReglementOk());
        phase.setForEmission(selectPhase.getForEmission());
        phase.setCodeComptable(selectPhase.getCodeComptable());
        phase.setActionInBanque(selectPhase.getActionInBanque());
        update("tabview_param_com:form_edit_phase_reg");
    }

    public void deletePhase(YvsComptaPhaseReglement ph) {
        {
            try {
                dao.delete(ph);
                mode.getPhases().remove(ph);
                update("tabview_param_com:table_phase_mode_reg");
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cet élément !");
                getException("Lymytz error >>>", ex);
            }
        }

    }

    public boolean saveNewModel() {
        String type = model != null ? String.valueOf(model.getType()) : "M";
        return saveNewModel(type);
    }

    public boolean saveNewModel(String type) {
        if (type != null ? type.trim().length() > 0 : false) {
            model.setType(type.charAt(0));
        } else {
            model.setType('M');
        }
        YvsBaseModelReglement entity = _saveNewModel(model);
        if (entity != null ? entity.getId() > 0 : false) {
            resetFicheModel();
            return true;
        }
        return false;
    }

    public YvsBaseModelReglement _saveNewModel(ModelReglement m) {
        String action = m.isUpdate() ? "Modification" : "Insertion";
        try {
            if (controleFiche(m)) {
                YvsBaseModelReglement y = UtilCom.buildModelReglement(m, currentUser, currentAgence.getSociete());
                if (m.getCodeAcces() != null ? m.getCodeAcces().trim().length() > 0 : false) {
                    y.setCodeAcces(returnCodeAcces(m.getCodeAcces()));
                } else {
                    y.setCodeAcces(null);
                }
                if (!m.isUpdate()) {
                    y.setId(null);
                    y = (YvsBaseModelReglement) dao.save1(y);
                    m.setId(y.getId().intValue());
                    models.add(0, y);
                } else {
                    dao.update(y);
                    models.set(models.indexOf(y), y);
                }
                succes();
                return y;
            }
            return null;
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return null;
        }
    }

    public boolean saveNewTranche() {
        YvsBaseTrancheReglement entity = _saveNewTranche(tranche, modelSelect);
        if (entity != null ? entity.getId() > 0 : false) {
            resetFicheTranche();
            update("data_model_reglement");
            return true;
        }
        return false;
    }

    public YvsBaseTrancheReglement _saveNewTranche(TrancheReglement t, YvsBaseModelReglement m) {
        String action = t.isUpdate() ? "Modification" : "Insertion";
        try {
            TrancheReglement b = recopieViewTranche(t);
            b.setModel(UtilCom.buildBeanModelReglement(m));
            if (controleFicheTranche(b)) {
                YvsBaseTrancheReglement y = buildTrancheReglement(b);
                y.setModel(m);
                if (!b.isUpdate()) {
                    y.setId(null);
                    y = (YvsBaseTrancheReglement) dao.save1(y);
                    m.getTranches().add(0, y);
                } else {
                    dao.update(y);
                    m.getTranches().set(m.getTranches().indexOf(y), y);
                }
                models.set(models.indexOf(m), m);
                succes();
                return y;
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return null;
        }
        return null;
    }

    @Override
    public void deleteBean() {
        if (_deleteBean(tabIds)) {
            update("data_mode_reglement");
        }
    }

    public boolean _deleteBean(String tabIds) {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                String[] tab = tabIds.split("-");
                for (String ids : tab) {
                    int id = Integer.valueOf(ids);
                    YvsBaseModeReglement bean = modes.get(modes.indexOf(new YvsBaseModeReglement((long) id)));
                    dao.delete(new YvsBaseModeReglement(bean.getId()));
                    modes.remove(bean);
                    if (id == mode.getId()) {
                        resetFiche();
                    }
                }
                succes();
                return true;
            }
            return false;
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
            return false;
        }
    }

    public void deleteBeanModel() {
        if (_deleteBeanModel(tablIds, model)) {
            update("data_model_reglement");
        }
    }

    public boolean _deleteBeanModel(String tabIds, ModelReglement model) {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                String[] tab = tabIds.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsBaseModelReglement bean = models.get(models.indexOf(new YvsBaseModelReglement(id)));
                    dao.delete(bean.getId());
                    models.remove(bean);
                    if (model.getId() == id) {
                        _resetFicheModel(model);
                    }
                }
                succes();
                return true;
            }
            return false;
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
            return false;
        }
    }

    public void deleteBeanModel_(YvsBaseModelReglement y) {
        modelSelect = y;
    }

    public void deleteBeanModel_() {
        try {
            if (modelSelect != null) {
                dao.delete(modelSelect);
                models.remove(modelSelect);
                if (model.getId() == modelSelect.getId()) {
                    resetFicheModel();
                }
                succes();
                update("data_model_reglement");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBeanTranche() {
        try {
            if ((tabIds_tranche != null) ? !tabIds_tranche.equals("") : false) {
                String[] tab = tabIds_tranche.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    dao.delete(new YvsBaseTrancheReglement(id));
                    modelSelect.getTranches().remove(new YvsBaseTrancheReglement(id));
                    if (tranche.getId() == id) {
                        resetFicheTranche();
                    }
                }
                models.set(models.indexOf(modelSelect), modelSelect);
                succes();
                update("data_model_reglement");
                update("data_tranche_model_reglement");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanTranche_(YvsBaseTrancheReglement y) {
        trancheSelect = y;
    }

    public void deleteBeanTranche_() {
        try {
            if (trancheSelect != null) {
                dao.delete(trancheSelect);
                modelSelect.getTranches().remove(trancheSelect);
                models.set(models.indexOf(modelSelect), modelSelect);
                succes();
                if (tranche.getId() == trancheSelect.getId()) {
                    resetFicheTranche();
                }
                update("data_model_reglement");
                update("data_tranche_model_reglement");
            } else {
                getErrorMessage("Vous devez selectionner la tranche");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            modeSelect = (YvsBaseModeReglement) ev.getObject();
            populateView(UtilCom.buildBeanModeReglement(modeSelect));
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void _loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            modelSelect = (YvsBaseModelReglement) ev.getObject();
            _populateView(UtilCom.buildBeanModelReglement(modelSelect));
        }
    }

    public void _unLoadOnView(UnselectEvent ev) {
        resetFicheModel();
    }

    public void selectModel(YvsBaseModelReglement y) {
        modelSelect = y;
        _populateView(UtilCom.buildBeanModelReglement(modelSelect));
        resetFicheTranche();
    }

    public void loadOnViewTranche(SelectEvent ev) {
        if (ev != null) {
            YvsBaseTrancheReglement y = (YvsBaseTrancheReglement) ev.getObject();
            populateViewTranche(UtilCom.buildBeanTrancheReglement(y));
            update("form_tranche_model_reglement");
        }
    }

    public void unLoadOnViewTranche(UnselectEvent ev) {
        resetFicheTranche();
        update("form_tranche_model_reglement");
    }

    public void initTranche(YvsBaseModelReglement y) {
        modelSelect = y;
        initTranche();
    }

    public void initTranche() {
        if ((modelSelect != null) ? modelSelect.getId() > 0 : false) {
            modelSelect.setTranches(dao.loadNameQueries("YvsBaseTrancheReglement.findByModel", new String[]{"model"}, new Object[]{modelSelect}));
            resetFicheTranche();
            update("blog_form_tranche_model_reglement");
        } else {
            getErrorMessage("Vous devez selectionner le plan");
        }
    }

    public void activeMode(YvsBaseModeReglement bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            modes.get(modes.indexOf(bean)).setActif(bean.getActif());
            String rq = "UPDATE yvs_base_mode_reglement SET actif=" + bean.getActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
        }
    }

    public void activeModel(YvsBaseModelReglement bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            models.get(models.indexOf(bean)).setActif(bean.getActif());
            String rq = "UPDATE yvs_base_model_reglement SET actif=" + bean.getActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
        }
    }

    public ModelReglement searchModel(String code, boolean open) {
        ModelReglement a = new ModelReglement();
        a.setDesignation(code);
        a.setError(true);
        if (Util.asString(code)) {
            champ = new String[]{"code", "type1", "type2", "actif", "societe"};
            val = new Object[]{code + "%", 'C', 'M', true, currentAgence.getSociete()};
            models = dao.loadNameQueries("YvsBaseModelReglement.findLikeCodeType2Actif", champ, val);
            if (models != null ? !models.isEmpty() : false) {
                if (models.size() == 1) {
                    a = UtilCom.buildBeanModelReglement(models.get(0));
                } else if (open) {
                    openDialog("dlgListModels");
                }
            }
        }
        return a;
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void fusionnerUnite(boolean fusionne) {
        try {
            fusionneModePTo = "";
            fusionnesModePBy.clear();
            List<Integer> ids = decomposeSelection(tabIds_modeR);
            if (!ids.isEmpty()) {
                long newValue = modes.get(ids.get(0)).getId();
                if (!fusionne) {
                    if (!autoriser("base_user_fusion")) {
                        openNotAcces();
                        return;
                    }
                    for (int i : ids) {
                        long oldValue = modes.get(i).getId();
                        if (oldValue != newValue) {
                            if (dao.fusionneData("yvs_base_mode_reglement", newValue, oldValue)) {
                                modes.remove(i);
                            }
                        }
                    }
                    tabIds_modeR = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneModePTo = modes.get(idx).getDesignation();
                    } else {
                        YvsBaseModeReglement c = (YvsBaseModeReglement) dao.loadOneByNameQueries("YvsBaseModeReglement.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                            fusionneModePTo = c.getDesignation();
                        }
                    }
                    YvsBaseModeReglement mr;
                    for (int i : ids) {
                        long oldValue = modes.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesModePBy.add(modes.get(i).getDesignation());
                            }
                        } else {
                            mr = (YvsBaseModeReglement) dao.loadOneByNameQueries("YvsBaseModeReglement.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (mr != null ? (mr.getId() != null ? mr.getId() > 0 : false) : false) {
                                fusionnesModePBy.add(mr.getDesignation());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 conditionnements");
            }
        } catch (NumberFormatException ex) {
        }
    }

    public void fusionner(boolean fusionne) {
        try {
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty() ? ids.size() > 1 : false) {
                long newValue = modes.get(ids.get(0)).getId();
                if (!fusionne) {
//                    if (!autoriser("base_user_fusion")) {
//                        openNotAcces();
//                        return;
//                    }
                    String oldValue = "0";
                    for (int i : ids) {
                        if (modes.get(i).getId() != newValue) {
                            oldValue += "," + modes.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_base_mode_reglement", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                modes.remove(new YvsBaseModeReglement(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = modes.get(idx).getDesignation();
                    } else {
                        YvsBaseModeReglement c = (YvsBaseModeReglement) dao.loadOneByNameQueries("YvsBaseModeReglement.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                            fusionneTo = c.getDesignation();
                        }
                    }
                    YvsBaseModeReglement c;
                    for (int i : ids) {
                        long oldValue = modes.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(modes.get(i).getDesignation());
                            }
                        } else {
                            c = (YvsBaseModeReglement) dao.loadOneByNameQueries("YvsBaseModeReglement.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                                fusionnesBy.add(c.getDesignation());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 utilisateurs");
            }
        } catch (NumberFormatException ex) {
        }
    }
}
