/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.commercial.param.ManagedTypeDocDivers;
import yvs.commercial.stock.ManagedOtherTransfert;
import yvs.dao.Options;
import yvs.dao.Util;
import yvs.entity.commercial.stock.YvsComNatureDoc;
import yvs.entity.compta.YvsBaseTypeDocDivers;
import yvs.entity.param.workflow.YvsWarningModelDoc;
import yvs.entity.param.workflow.YvsWorkflowAutorisationValidDoc;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowModelDoc;
import yvs.entity.stat.dashboard.YvsWorkflowEtatsSignatures;
import yvs.entity.users.YvsNiveauAcces;
import yvs.stat.SignatureRapport;
import yvs.util.Constantes;
import yvs.util.Managed;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedWorkflow extends Managed<ModelDocuments, YvsWorkflowEtapeValidation> implements Serializable {

    private ModelDocuments modelDoc = new ModelDocuments();
    private List<YvsWorkflowModelDoc> listModel;
    private EtapesValidation etapeValidation = new EtapesValidation();
    private List<YvsNiveauAcces> listNiveauAcces, selectionNiveauAcces;
    private YvsWorkflowEtapeValidation selectionEtapeValidation;
    private List<YvsWorkflowEtapeValidation> etapesValidation;

    private List<YvsWarningModelDoc> warnings;

    private List<YvsWorkflowEtapeValidation> etapesSuivantes;
    private List<String> natures;
    private String nature;
    private long typeDoc;

    private int idSelectEtape;
    private boolean updateEtape;
    private boolean displayTypeDocDiv = false;
    private boolean displayTypeDocSortie = false;
    private boolean displayNature = false;
    private boolean generatedPieceCAisse;

    private SignatureRapport signature = new SignatureRapport();
    private YvsWorkflowModelDoc selectModelDoc = new YvsWorkflowModelDoc();

    public ManagedWorkflow() {
        etapesValidation = new ArrayList<>();
        listModel = new ArrayList<>();
        listNiveauAcces = new ArrayList<>();
        etapesSuivantes = new ArrayList<>();
        natures = new ArrayList<>();
        warnings = new ArrayList<>();

        natures.add(Constantes.OP_DONS);
        natures.add(Constantes.OP_RATIONS);
        natures.add(Constantes.OP_AJUSTEMENT_STOCK);
        natures.add(Constantes.OP_DEPRECIATION);
    }

    public List<YvsWarningModelDoc> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<YvsWarningModelDoc> warnings) {
        this.warnings = warnings;
    }

    public String getNature() {
        return nature != null ? nature.trim().length() > 0 ? nature : Constantes.OP_DONS : Constantes.OP_DONS;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public List<String> getNatures() {
        return natures;
    }

    public void setNatures(List<String> natures) {
        this.natures = natures;
    }

    public boolean isUpdateEtape() {
        return updateEtape;
    }

    public void setUpdateEtape(boolean updateEtape) {
        this.updateEtape = updateEtape;
    }

    public List<YvsNiveauAcces> getListNiveauAcces() {
        return listNiveauAcces;
    }

    public void setListNiveauAcces(List<YvsNiveauAcces> listNiveauAcces) {
        this.listNiveauAcces = listNiveauAcces;
    }

    public int getIdSelectEtape() {
        return idSelectEtape;
    }

    public void setIdSelectEtape(int idSelectEtape) {
        this.idSelectEtape = idSelectEtape;
    }

    public List<YvsWorkflowModelDoc> getListModel() {
        return listModel;
    }

    public void setListModel(List<YvsWorkflowModelDoc> listModel) {
        this.listModel = listModel;
    }

    public ModelDocuments getModelDoc() {
        return modelDoc;
    }

    public void setModelDoc(ModelDocuments modelDoc) {
        this.modelDoc = modelDoc;
    }

    public EtapesValidation getEtapeValidation() {
        return etapeValidation;
    }

    public void setEtapeValidation(EtapesValidation etapeValidation) {
        this.etapeValidation = etapeValidation;
    }

    public List<YvsNiveauAcces> getSelectionNiveauAcces() {
        return selectionNiveauAcces;
    }

    public void setSelectionNiveauAcces(List<YvsNiveauAcces> selectionNiveauAcces) {
        this.selectionNiveauAcces = selectionNiveauAcces;
    }

    public YvsWorkflowEtapeValidation getSelectionEtapeValidation() {
        return selectionEtapeValidation;
    }

    public void setSelectionEtapeValidation(YvsWorkflowEtapeValidation selectionEtapeValidation) {
        this.selectionEtapeValidation = selectionEtapeValidation;
    }

    public List<YvsWorkflowEtapeValidation> getEtapesSuivantes() {
        return etapesSuivantes;
    }

    public void setEtapesSuivantes(List<YvsWorkflowEtapeValidation> etapesSuivantes) {
        this.etapesSuivantes = etapesSuivantes;
    }

    public List<YvsWorkflowEtapeValidation> getEtapesValidation() {
        return etapesValidation;
    }

    public void setEtapesValidation(List<YvsWorkflowEtapeValidation> etapesValidation) {
        this.etapesValidation = etapesValidation;
    }

    public boolean isDisplayTypeDocDiv() {
        return displayTypeDocDiv;
    }

    public void setDisplayTypeDocDiv(boolean displayTypeDocDiv) {
        this.displayTypeDocDiv = displayTypeDocDiv;
    }

    public boolean isDisplayTypeDocSortie() {
        return displayTypeDocSortie;
    }

    public void setDisplayTypeDocSortie(boolean displayTypeDocSortie) {
        this.displayTypeDocSortie = displayTypeDocSortie;
    }

    public boolean isGeneratedPieceCAisse() {
        return generatedPieceCAisse;
    }

    public void setGeneratedPieceCAisse(boolean generatedPieceCAisse) {
        this.generatedPieceCAisse = generatedPieceCAisse;
    }

    public long getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(long typeDoc) {
        this.typeDoc = typeDoc;
    }

    public boolean isDisplayNature() {
        displayNature = modelDoc != null ? (modelDoc.getTitreDocument().equals(Constantes.DOCUMENT_SORTIE) || modelDoc.getTitreDocument().equals(Constantes.DOCUMENT_DOC_DIVERS_CAISSE)) : false;
        return displayNature;
    }

    public void setDisplayNature(boolean displayNature) {
        this.displayNature = displayNature;
    }

    public SignatureRapport getSignature() {
        return signature;
    }

    public void setSignature(SignatureRapport signature) {
        this.signature = signature;
    }

    public YvsWorkflowModelDoc getSelectModelDoc() {
        return selectModelDoc;
    }

    public void setSelectModelDoc(YvsWorkflowModelDoc selectModelDoc) {
        this.selectModelDoc = selectModelDoc;
    }

    public void initModelDocument() {
        champ = new String[]{};
        val = new Object[]{};
        listModel = dao.loadNameQueries("YvsWorkflowModelDoc.findAllW", champ, val);
        if (listModel.isEmpty()) {
            YvsWorkflowModelDoc model = new YvsWorkflowModelDoc(numero, Constantes.DOCUMENT_MISSION, "yvs_grh_missions");
            model.setAuthor(currentUser);
            model.setId(null);
            model = (YvsWorkflowModelDoc) dao.save1(model);
            listModel.add(model);
            model = new YvsWorkflowModelDoc(numero, Constantes.DOCUMENT_FORMATION, "yvs_grh_formations");
            model.setAuthor(currentUser);
            model.setId(null);
            model = (YvsWorkflowModelDoc) dao.save1(model);
            listModel.add(model);
            model = new YvsWorkflowModelDoc(numero, Constantes.DOCUMENT_CONGES, "yvs_grh_conges");
            model.setAuthor(currentUser);
            model.setId(null);
            model = (YvsWorkflowModelDoc) dao.save1(model);
            listModel.add(model);
        }
    }

    public void choixModelDocument(ValueChangeEvent ev) {
        if (ev != null) {
            int id = (int) ev.getNewValue();
            if (id > 0) {
                modelDoc = buildModelDoc(listModel.get(listModel.indexOf(new YvsWorkflowModelDoc(id))));
                etapesSuivantes.clear();
                natures.clear();
                boolean load = true;
                if (modelDoc.getTitreDocument().equals(Constantes.DOCUMENT_SORTIE)) {
                    load = true;
                    ManagedOtherTransfert service = (ManagedOtherTransfert) giveManagedBean(ManagedOtherTransfert.class);
                    if (service != null) {
                        service.loadAllNature();
                        update("select_type_doc_sortie_workflow");
                        if (!service.getNatures().isEmpty()) {
                            modelDoc.getEtapesValidation().clear();
                            load = false;
                        }
                    }
                    displayTypeDocSortie = true;
                } else {
                    nature = Constantes.DEPENSE;
                    natures.add(Constantes.DEPENSE);
                    natures.add(Constantes.RECETTE);
                    displayTypeDocSortie = false;
                }
                if (modelDoc.getTitreDocument().equals(Constantes.DOCUMENT_DOC_DIVERS_CAISSE) || modelDoc.getTitreDocument().equals(Constantes.DOCUMENT_BON_DIVERS_CAISSE) || modelDoc.getTitreDocument().equals(Constantes.DOCUMENT_FACTURE_ACHAT)) {
                    //display les type de doc divers
                    displayTypeDocDiv = true;
                } else {
                    displayTypeDocDiv = false;
                }
                if (load) {
                    etapesSuivantes.addAll(modelDoc.getEtapesValidation());
                }
            }
        }
    }

    public void chooseNature() {
        if (typeDoc <= 0) {
            modelDoc.setEtapesValidation(dao.loadNameQueries("YvsWorkflowEtapeValidation.findByModelNature", new String[]{"titre", "nature", "societe"}, new Object[]{modelDoc.getTitreDocument(), nature, currentAgence.getSociete()}));
        } else {
            modelDoc.setEtapesValidation(dao.loadNameQueries("YvsWorkflowEtapeValidation.findByModelNatureAndType", new String[]{"titre", "nature", "typeDoc", "societe"}, new Object[]{modelDoc.getTitreDocument(), nature, new YvsBaseTypeDocDivers(typeDoc), currentAgence.getSociete()}));
        }
        etapesSuivantes.clear();
        etapesSuivantes.addAll(modelDoc.getEtapesValidation());
    }

    public void chooseTypeDocDivers() {
        champ = new String[]{"titre", "typeDoc", "societe"};
        val = new Object[]{modelDoc.getTitreDocument(), new YvsBaseTypeDocDivers(typeDoc), currentAgence.getSociete()};
        nameQueri = "YvsWorkflowEtapeValidation.findByModelType";
        switch (modelDoc.getTitreDocument()) {
            case Constantes.DOCUMENT_DOC_DIVERS_CAISSE:
                champ = new String[]{"titre", "nature", "typeDoc", "societe"};
                val = new Object[]{modelDoc.getTitreDocument(), nature, new YvsBaseTypeDocDivers(typeDoc), currentAgence.getSociete()};
                nameQueri = "YvsWorkflowEtapeValidation.findByModelNatureAndType";
                break;
        }
        modelDoc.setEtapesValidation(dao.loadNameQueries(nameQueri, champ, val));
        etapesSuivantes.clear();
        etapesSuivantes.addAll(modelDoc.getEtapesValidation());
    }

    public void chooseTypeDocSorties() {
        champ = new String[]{"titre", "nature", "societe"};
        val = new Object[]{modelDoc.getTitreDocument(), new YvsComNatureDoc(typeDoc), currentAgence.getSociete()};
        nameQueri = "YvsWorkflowEtapeValidation.findByModelNatureDocSortieAll";
        modelDoc.setEtapesValidation(dao.loadNameQueries(nameQueri, champ, val));
        etapesSuivantes.clear();
        etapesSuivantes.addAll(modelDoc.getEtapesValidation());
    }

    public void loadEtapeValidMission() {
        etapesValidation = dao.loadNameQueries("YvsWorkflowEtapeValidation.findByModelActif", new String[]{"titre", "societe"}, new Object[]{Constantes.DOCUMENT_MISSION, currentAgence.getSociete()});
        System.err.println(Constantes.DOCUMENT_MISSION + " Etapes ---- " + etapesValidation.size());
    }

    private ModelDocuments buildModelDoc(YvsWorkflowModelDoc m) {
        ModelDocuments re = new ModelDocuments();
        if (m != null) {
            re.setEtapesValidation(dao.loadNameQueries("YvsWorkflowEtapeValidation.findByModelDoc", new String[]{"model", "societe"}, new Object[]{m, currentAgence.getSociete()}));
            re.setId(m.getId());
            re.setTableDoc(m.getNameTable());
            re.setTitreDocument(m.getTitreDoc());
            re.setDefinedLivraison(m.getDefinedLivraison());
            re.setDefinedUpdate(m.getDefinedUpdate());
            re.setDefinedReglement(m.getDefinedReglement());
        }
        return re;
    }

    public void createStepeValidation(boolean fixFirstStepe) {
        if (modelDoc.getId() > 0) {
            YvsWorkflowEtapeValidation et = new YvsWorkflowEtapeValidation();
            et.setId(etapeValidation.getId());
            et.setLabelStatut(etapeValidation.getLabelStatu());
            et.setTitreEtape(etapeValidation.getTitreEtape());
            et.setDocumentModel(new YvsWorkflowModelDoc(modelDoc.getId()));
            et.setAuthor(currentUser);
            et.setActif(etapeValidation.isActif());
            et.setNature(getNature());
            et.setLivraisonHere(etapeValidation.isLivraisonHere());
            et.setPrintHere(etapeValidation.isPrintHere());
            et.setReglementHere(etapeValidation.isReglementHere());
            et.setDateUpdate(new Date());
            et.setCanUpdateHere(etapeValidation.isCanUpdateHere());
            et.setSociete(currentAgence.getSociete());
            et.setOrdreEtape(etapeValidation.getOrdre());
            if (typeDoc > 0) {
                if (displayTypeDocDiv) {
                    et.setTypeDocDivers(new YvsBaseTypeDocDivers(typeDoc));
                    ManagedTypeDocDivers w = (ManagedTypeDocDivers) giveManagedBean(ManagedTypeDocDivers.class);
                    if (w != null) {
                        int idx = w.getTypesDocDivers().indexOf(et.getTypeDocDivers());
                        if (idx > -1) {
                            et.setTypeDocDivers(w.getTypesDocDivers().get(idx));
                        }
                    }
                } else {
                    et.setNatureDoc(new YvsComNatureDoc(typeDoc));
                    ManagedOtherTransfert w = (ManagedOtherTransfert) giveManagedBean(ManagedOtherTransfert.class);
                    if (w != null) {
                        int idx = w.getNatures().indexOf(et.getNatureDoc());
                        if (idx > -1) {
                            et.setNatureDoc(w.getNatures().get(idx));
                        }
                    }

                }
            } else {
                et.setTypeDocDivers(null);
                et.setNatureDoc(null);
            }
            YvsWorkflowEtapeValidation first = findFirstStape(modelDoc.getEtapesValidation());
            if (etapeValidation.isFirstEtape()) {
                if (!fixFirstStepe) {
                    if (first == null) {
                        et.setFirstEtape(etapeValidation.isFirstEtape());
                    } else {
                        if (!first.getId().equals(et.getId())) {
                            getWarningMessage("Attention !!", "une étape de départ a déjà été défini !");
                            openDialog("dlgMoveFirstStepe");
                            return;
                        } else {
                            et.setFirstEtape(etapeValidation.isFirstEtape());
                        }
                    }
                } else {
                    //modifie les étapes enregistré pour mettre à false l'attribut firstStepe
                    updateAllStapes(modelDoc.getEtapesValidation());
                    et.setFirstEtape(etapeValidation.isFirstEtape());
                }
            }
            if (etapeValidation.getIdEtapeSuivante() > 0) {
                et.setEtapeSuivante(etapesSuivantes.get(etapesSuivantes.indexOf(new YvsWorkflowEtapeValidation(etapeValidation.getIdEtapeSuivante()))));
            }
            if (!updateEtape) {
                et.setDateSave(new Date());
                et.setId(null);
                et = (YvsWorkflowEtapeValidation) dao.save1(et);
            } else {
                dao.update(et);
            }
            int idx = modelDoc.getEtapesValidation().indexOf(et);
            if (idx > -1) {
                modelDoc.getEtapesValidation().set(idx, et);
            } else {
                modelDoc.getEtapesValidation().add(0, et);
            }
            idx = etapesSuivantes.indexOf(et);
            if (idx > -1) {
                etapesSuivantes.set(idx, et);
            } else {
                etapesSuivantes.add(0, et);
            }

            updateEtape = false;
            etapeValidation = new EtapesValidation();
//            if (first != null ? !et.getId().equals(first.getId()) : false) {
//                ordonneStepe(first, 1);
//            }
            succes();
        } else {
            getErrorMessage("Vous devez choisir un model de document !");
        }
    }

    private void ordonneStepe(YvsWorkflowEtapeValidation et, int numero) {
        if (et != null) {
            if (et.getFirstEtape()) {
                et.setOrdreEtape(1);
                dao.update(et);
            } else {
                et.setOrdreEtape(numero);
                dao.update(et);
            }
            ordonneStepe(et.getEtapeSuivante(), (numero + 1));
        }
    }

    //retourne vrai si pour un model de document, la première étape est déjà défini
    private YvsWorkflowEtapeValidation findFirstStape(List<YvsWorkflowEtapeValidation> l) {
        for (YvsWorkflowEtapeValidation e : l) {
            if (e.getFirstEtape()) {
                return e;
            }
        }
        return null;
    }

    private boolean updateAllStapes(List<YvsWorkflowEtapeValidation> l) {
        for (YvsWorkflowEtapeValidation e : l) {
            e.setFirstEtape(false);
            e.setAuthor(currentUser);
            dao.update(e);
        }
        return false;
    }

    public void fixFirstStepe(boolean fixFist) {
        List<YvsWorkflowEtapeValidation> l = new ArrayList<>(modelDoc.getEtapesValidation());
        l.remove(selectionEtapeValidation);
        if (fixFist) {
            if (findFirstStape(l) != null) {
                getErrorMessage("Attention !!", "une première étape a déjà été défini !");
                openDialog("dlgMoveFirstStepe_");
                return;
            }
        }
        updateAllStapes(l);
        selectionEtapeValidation.setFirstEtape(true);
        selectionEtapeValidation.setAuthor(currentUser);
        dao.update(selectionEtapeValidation);
    }

    public void fixLastStepe(boolean fixFist) {
        selectionEtapeValidation.setEtapeSuivante(null);
        selectionEtapeValidation.setAuthor(currentUser);
        dao.update(selectionEtapeValidation);
    }

    public void selectionneEtapes(SelectEvent ev) {
        if (ev != null) {
            YvsWorkflowEtapeValidation et = (YvsWorkflowEtapeValidation) ev.getObject();
            etapeValidation = buildBeanEtapeValidation(et);
            updateEtape = true;
        }
    }

    private EtapesValidation buildBeanEtapeValidation(YvsWorkflowEtapeValidation e) {
        EtapesValidation re = new EtapesValidation();
        if (e != null) {
            re.setActif(e.getActif());
            re.setDocument(new ModelDocuments(e.getDocumentModel().getId(), e.getDocumentModel().getTitreDoc(), e.getDocumentModel().getNameTable()));
            re.setEtapeSuivante((e.getEtapeSuivante() != null) ? new EtapesValidation(e.getEtapeSuivante().getId(), e.getEtapeSuivante().getLabelStatut(), false, re.getDocument()) : null);
            re.setFirstEtape(e.getFirstEtape());
            re.setId(e.getId());
            re.setIdEtapeSuivante((e.getEtapeSuivante() != null) ? e.getEtapeSuivante().getId() : 0);
            re.setLabelStatu(e.getLabelStatut());
            re.setTitreEtape(e.getTitreEtape());
            re.setNature(e.getNature());
            re.setLivraisonHere(e.getLivraisonHere());
            re.setCanUpdateHere(e.getCanUpdateHere());
            re.setReglementHere(e.getReglementHere());
            re.setOrdre(e.getOrdreEtape());
            nature = e.getNature();
        }
        return re;
    }

    public void deleteEtapeValidation(YvsWorkflowEtapeValidation et) {
        try {
            et.setAuthor(currentUser);
            dao.delete(et);
            modelDoc.getEtapesValidation().remove(et);
        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
            getErrorMessage("Impossible de supprimer cet élément !");
        }
    }

    public void toggleActivEtapeValidation(YvsWorkflowEtapeValidation et) {
        try {
            et.setActif(!et.getActif());
            et.setAuthor(currentUser);
            dao.update(et);
        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
            getErrorMessage("Impossible de modifier cet élément !");
        }
    }

    public void loadAllNiveauAcces() {
        champ = new String[]{"societe", "superAdmin"};
        val = new Object[]{currentAgence.getSociete(), false};
        listNiveauAcces = dao.loadNameQueries("YvsNiveauAcces.findBySuperAdmin", champ, val);
    }

    public void choixLineEtape(YvsWorkflowEtapeValidation etape) {
        selectionEtapeValidation = etape;
        List<YvsNiveauAcces> temp = new ArrayList<>(listNiveauAcces);
        for (YvsWorkflowAutorisationValidDoc au : etape.getAutorisations()) {
            if (listNiveauAcces.contains(au.getNiveauAcces())) {
                temp.remove(au.getNiveauAcces());
            }
        }
        YvsWorkflowAutorisationValidDoc a;
        long id = -1000;
        for (YvsNiveauAcces n : temp) {
            a = new YvsWorkflowAutorisationValidDoc(id++);
            a.setCanValide(false);
            a.setNiveauAcces(n);
            a.setEtapeValide(etape);
            selectionEtapeValidation.getAutorisations().add(a);
        }
        openDialog("dlgNiveauAcces");
        update("tableEtapeDroitValid");
    }

    public void toogleDroitValidDoc(SelectEvent ev) {
        if (ev != null) {
            YvsWorkflowAutorisationValidDoc au = (YvsWorkflowAutorisationValidDoc) ev.getObject();
            toogleDroitValidDoc_(au);
        }

    }

    public void toogleDroitValidDoc_(YvsWorkflowAutorisationValidDoc au) {
        if (au != null) {
            au.setCanValide(!au.getCanValide());
            au.setAuthor(currentUser);
            if (au.getId() > 0) {
                dao.update(au);
            } else {
                au.setId(null);
                au = (YvsWorkflowAutorisationValidDoc) dao.save1(au);
            }
        }

    }

    public void toogleNotifieValid(YvsWorkflowAutorisationValidDoc au) {
        if (au != null) {
            au.setCanNotify(!au.getCanNotify());
            au.setAuthor(currentUser);
            if (au.getId() > 0) {
                dao.update(au);
            } else {
                au.setId(null);
                au = (YvsWorkflowAutorisationValidDoc) dao.save1(au);
            }
        }

    }

    public void openDlgChoixEtapeSvte(YvsWorkflowEtapeValidation etape) {
        selectionEtapeValidation = etape;
        etapesSuivantes.remove(etape);
        openDialog("dlgDefineEtapeSvte");
        update("tableEtapeSvte");
    }

    public void affecteEtapeSuivante(SelectEvent ev) {
        YvsWorkflowEtapeValidation e = (YvsWorkflowEtapeValidation) ev.getObject();
        selectionEtapeValidation.setEtapeSuivante(e);
        selectionEtapeValidation.setAuthor(currentUser);
        dao.update(selectionEtapeValidation);
        update("tableEtapeWorkflow");
    }

    public void printElementHere(SelectEvent ev) {
        printLineElementHere((YvsWorkflowEtapeValidation) ev.getObject());
    }

    public void printLineElementHere(YvsWorkflowEtapeValidation e) {
        if (autoriser("mission_param_avance")) {
            if (e != null) {
                e.setPrintHere(!e.getPrintHere());
                dao.update(e);
            }
        } else {
            openNotAcces();
        }
    }

    public void editBonProvisoireHere(YvsWorkflowEtapeValidation e) {
        if (autoriser("mission_param_avance")) {
            if (e != null) {
                e.setCanEditBpHere(!e.getCanEditBpHere());
                dao.update(e);
            }
        } else {
            openNotAcces();
        }
    }

    @Override
    public boolean controleFiche(ModelDocuments bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ModelDocuments recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(ModelDocuments bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int ids = -100000;

    @Override
    public void loadAll() {
        loadAll(null);
    }

    public void loadAll(String nature) {
        if (nature != null ? nature.trim().length() > 0 : false) {
            champ = new String[]{"natures"};
            val = new Object[]{Arrays.asList(nature.split(","))};
            listModel = dao.loadNameQueries("YvsWorkflowModelDoc.findByNatures", champ, val);
        } else {
            champ = new String[]{};
            val = new Object[]{};
            listModel = dao.loadNameQueries("YvsWorkflowModelDoc.findAll", champ, val);
        }
        warnings.clear();
        YvsWarningModelDoc w;
        for (YvsWorkflowModelDoc m : listModel) {
            w = (YvsWarningModelDoc) dao.loadOneByNameQueries("YvsWarningModelDoc.findOne", new String[]{"model", "societe"}, new Object[]{m, currentAgence.getSociete()});
            if (w != null ? w.getId() < 1 : true) {
                w = new YvsWarningModelDoc(ids--);
                w.setEcart(currentAgence.getSociete().getEcartDocument());
                w.setModel(m);
                w.setSociete(currentAgence.getSociete());
            }
            w.setDateUpdate(new Date());
            w.setDateSave(new Date());
            w.setAuthor(currentUser);
            warnings.add(w);
        }
    }

    public void onRowWarningEdit(RowEditEvent ev) {
        try {
            if (ev != null) {
                YvsWarningModelDoc y = (YvsWarningModelDoc) ev.getObject();
                if (y != null) {
                    int index = warnings.indexOf(y);
                    if (y.getEcart() > 0) {
                        if (y.getId() > 0) {
                            dao.update(y);
                        } else {
                            y.setId(null);
                            y = (YvsWarningModelDoc) dao.save1(y);
                            warnings.set(index, y);
                            initWarningTable(y);
                        }
                    } else if (y.getId() > 0) {
                        y.setEcart(30);
                        dao.delete(y);
                        y.setId(ids--);
                        warnings.set(index, y);
                    }
                    succes();
                    update("data_warning_doc");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("ERROR (onRowWarningEdit)", ex);
        }
    }

    public void addAddWarning() {
        try {
            boolean succes = false;
            YvsWarningModelDoc w;
            for (int i = 0; i < warnings.size(); i++) {
                w = warnings.get(i);
                if (w.getId() < 1) {
                    w.setId(null);
                    w = (YvsWarningModelDoc) dao.save1(w);
                    warnings.set(i, w);
                    succes = true;
                }
            }
            if (succes) {
                succes();
                update("data_warning_doc");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("ERROR (onRowWarningEdit)", ex);
        }
    }

    public void initWarningTable(YvsWarningModelDoc y) {
        try {
            if (y != null) {
                String query = null;
                String nature_alerte = "VALIDATION";
                switch (y.getModel().getTitreDoc()) {
                    case "FACTURE_ACHAT": {
                        query = "SELECT y.id, y.agence, y.date_doc FROM yvs_com_doc_achats y INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.type_doc = 'FA' AND y.statut NOT IN ('V', 'T', 'C', 'A') AND y.date_doc < ? AND a.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "FACTURE_ACHAT_REGLE": {
                        query = "SELECT y.id, y.agence, y.date_doc FROM yvs_com_doc_achats y INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.type_doc = 'FA' AND y.statut_regle NOT IN ('P', 'T', 'C', 'A') AND y.date_doc < ? AND a.societe = ?";
                        nature_alerte = "REGLEMENT";
                        break;
                    }
                    case "FACTURE_ACHAT_LIVRE": {
                        query = "SELECT y.id, y.agence, COALESCE(y.date_livraison, y.date_doc) FROM yvs_com_doc_achats y INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.type_doc = 'FA' AND y.statut_livre NOT IN ('L', 'T', 'C', 'A') AND COALESCE(y.date_livraison, y.date_doc) < ? AND a.societe = ?";
                        nature_alerte = "LIVRAISON";
                        break;
                    }
                    case "BON_LIVRAISON_ACHAT": {
                        query = "SELECT y.id, y.agence, y.date_doc FROM yvs_com_doc_achats y INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.type_doc = 'BLA' AND y.statut NOT IN ('V', 'T', 'C', 'A') AND y.date_doc < ? AND a.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "RETOUR_ACHAT": {
                        query = "SELECT y.id, y.agence, y.date_doc FROM yvs_com_doc_achats y INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.type_doc = 'BRA' AND y.statut NOT IN ('V', 'T', 'C', 'A') AND y.date_doc < ? AND a.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "AVOIR_ACHAT": {
                        query = "SELECT y.id, y.agence, y.date_doc FROM yvs_com_doc_achats y INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.type_doc = 'FRA' AND y.statut NOT IN ('V', 'T', 'C', 'A') AND y.date_doc < ? AND a.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "ENTREE_STOCK": {
                        query = "SELECT y.id, d.agence, y.date_doc FROM yvs_com_doc_stocks y INNER JOIN yvs_base_depots d ON y.destination = d.id WHERE y.type_doc = 'ES' AND y.statut NOT IN ('V', 'T', 'C', 'A') AND y.date_doc < ? AND y.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "SORTIE_STOCK": {
                        query = "SELECT y.id, d.agence, y.date_doc FROM yvs_com_doc_stocks y INNER JOIN yvs_base_depots d ON y.source = d.id WHERE y.type_doc = 'SS' AND y.statut NOT IN ('V', 'T', 'C', 'A') AND y.date_doc < ? AND y.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "TRANSFERT_STOCK": {
                        query = "SELECT y.id, d.agence, y.date_doc FROM yvs_com_doc_stocks y INNER JOIN yvs_base_depots d ON y.source = d.id WHERE y.type_doc = 'FT' AND y.statut NOT IN ('V', 'T', 'C', 'A') AND y.date_doc < ? AND y.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "RECONDITIONNEMENT_STOCK": {
                        query = "SELECT y.id, d.agence, y.date_doc FROM yvs_com_doc_stocks y INNER JOIN yvs_base_depots d ON y.source = d.id WHERE y.type_doc = 'TR' AND y.statut NOT IN ('V', 'T', 'C', 'A') AND y.date_doc < ? AND y.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "INVENTAIRE_STOCK": {
                        query = "SELECT y.id, d.agence, y.date_doc FROM yvs_com_doc_stocks y INNER JOIN yvs_base_depots d ON y.source = d.id WHERE y.type_doc = 'IN' AND y.statut NOT IN ('V', 'T', 'C', 'A') AND y.date_doc < ? AND y.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "FACTURE_VENTE": {
                        query = "SELECT y.id, e.agence, e.date_entete FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = y.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.type_doc = 'FV' AND y.statut NOT IN ('V', 'T', 'C', 'A') AND e.date_entete < ? AND a.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "FACTURE_VENTE_REGLE": {
                        query = "SELECT y.id, e.agence, e.date_entete FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = y.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.type_doc = 'FV' AND y.statut_regle NOT IN ('P', 'T', 'C', 'A') AND e.date_entete < ? AND a.societe = ?";
                        nature_alerte = "REGLEMENT";
                        break;
                    }
                    case "FACTURE_VENTE_LIVRE": {
                        query = "SELECT y.id, e.agence, COALESCE(y.date_livraison_prevu, e.date_entete) FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = y.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.type_doc = 'FV' AND y.statut_livre NOT IN ('L', 'T', 'C', 'A') AND COALESCE(y.date_livraison_prevu, e.date_entete) < ? AND a.societe = ?";
                        nature_alerte = "REGLEMENT";
                        break;
                    }
                    case "RETOUR_VENTE": {
                        query = "SELECT y.id, e.agence, e.date_entete FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = y.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.type_doc = 'BRV' AND y.statut NOT IN ('V', 'T', 'C', 'A') AND e.date_entete < ? AND a.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "AVOIR_VENTE": {
                        query = "SELECT y.id, e.agence, e.date_entete FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = y.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.type_doc = 'FRV' AND y.statut NOT IN ('V', 'T', 'C', 'A') AND e.date_entete < ? AND a.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "APPROVISIONNEMENT": {
                        query = "SELECT y.id, d.agence, y.date_approvisionnement FROM yvs_com_fiche_approvisionnement y INNER JOIN yvs_base_depots d ON y.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE y.etat NOT IN ('V', 'T', 'C', 'A') AND y.date_approvisionnement < ? AND a.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "BON_OPERATION_DIVERS": {
                        query = "SELECT y.id, y.agence, y.date_bon FROM yvs_compta_bon_provisoire y INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.statut NOT IN ('V', 'T', 'C', 'A') AND y.date_bon < ? AND a.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "DOC_DIVERS_RECETTE": {
                        query = "SELECT y.id, y.agence, y.date_doc FROM yvs_compta_caisse_doc_divers y INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.mouvement = 'R' AND y.statut_doc NOT IN ('V', 'T', 'C', 'A') AND y.date_doc < ? AND a.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "DOC_DIVERS_DEPENSE": {
                        query = "SELECT y.id, y.agence, y.date_doc FROM yvs_compta_caisse_doc_divers y INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.mouvement = 'D' AND y.statut_doc NOT IN ('V', 'T', 'C', 'A') AND y.date_doc < ? AND a.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "OD_NON_PLANNIFIE": {
                        query = "SELECT y.id, y.agence, y.date_doc FROM yvs_compta_caisse_doc_divers y INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.statut_doc IN ('V', 'T', 'C') AND (SELECT COUNT(p.id) FROM yvs_compta_caisse_piece_divers p WHERE p.doc_divers = y.id) < 1 AND y.date_doc < ? AND a.societe = ?";
                        nature_alerte = "REGLEMENT";
                        break;
                    }
                    case "PIECE_CAISSE": {
                        query = "SELECT y.id, y.agence, COALESCE(y.date_paiment_prevu, y.date_mvt) FROM yvs_compta_mouvement_caisse y INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.statut_piece NOT IN ('P', 'T', 'C', 'A') AND COALESCE(y.date_paiment_prevu, y.date_mvt) < ? AND a.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "PERMISSION_CD": {
                        query = "SELECT y.id, e.agence, y.date_conge FROM yvs_grh_conge_emps y INNER JOIN yvs_grh_employes e ON y.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.nature = 'P' AND y.statut NOT IN ('V', 'T', 'C', 'A') AND y.date_conge < ? AND a.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "CONGES": {
                        query = "SELECT y.id, e.agence, y.date_conge FROM yvs_grh_conge_emps y INNER JOIN yvs_grh_employes e ON y.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.nature = 'C' AND y.statut NOT IN ('V', 'T', 'C', 'A') AND y.date_conge < ? AND a.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "FORMATIONS": {
                        query = "SELECT y.id, e.agence, y.date_formation FROM yvs_grh_formation_emps y INNER JOIN yvs_grh_employes e ON y.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE COALESCE(y.valider, FALSE) IS FALSE AND y.date_formation < ? AND a.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                    case "MISSIONS": {
                        query = "SELECT y.id, e.agence, y.date_mission FROM yvs_grh_missions y INNER JOIN yvs_grh_employes e ON y.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE y.statut_mission NOT IN ('V', 'T', 'C', 'A') IS FALSE AND y.date_mission < ? AND a.societe = ?";
                        nature_alerte = "VALIDATION";
                        break;
                    }
                }
                if (Util.asString(query)) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, -y.getEcart());
                    List<Object[]> result = dao.loadListBySqlQuery(query, new Options[]{new Options(calendar.getTime(), 1), new Options(currentAgence.getSociete().getId(), 2)});
                    Options[] params;
                    Long id;
                    for (Object[] line : result) {
                        query = "SELECT id FROM yvs_workflow_alertes WHERE id_element = ? AND nature_alerte = ? AND model_doc = ? LIMIT 1";
                        id = (Long) dao.loadObjectBySqlQuery(query, new Options[]{new Options(line[0], 1), new Options(nature_alerte, 2), new Options(y.getModel().getId(), 3)});
                        if (id != null ? id < 1 : true) {
                            query = "INSERT INTO yvs_workflow_alertes(model_doc, nature_alerte, id_element, author, agence, date_doc, niveau, date_save, date_update) "
                                    + " VALUES(?,?,?,?,?,?,?,current_timestamp,current_timestamp)";
                            params = new Options[]{new Options(y.getModel().getId(), 1), new Options(nature_alerte, 2), new Options(line[0], 3), new Options(currentUser.getId(), 4), new Options(line[1], 5), new Options(line[2], 6), new Options(0, 7)};
                            dao.requeteLibre(query, params);
                        } else {
//                            query = "UPDATE yvs_workflow_alertes SET silence = FALSE WHERE id = ?";
//                            params = new Options[]{new Options(id, 1)};
                        }
//                        dao.requeteLibre(query, params);
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("ERROR (initWarningTable)", ex);
        }
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*Gestion des signatures*/
    public void selectModelDocToAdd(YvsWorkflowModelDoc y) {
        YvsWorkflowEtatsSignatures sign = (YvsWorkflowEtatsSignatures) dao.loadOneByNameQueries("YvsWorkflowEtatsSignatures.findByModelDoc", new String[]{"modelDoc", "societe"}, new Object[]{y, currentAgence.getSociete()});
//        selectModelDoc.setSignatures(dao.loadNameQueries("YvsWorkflowEtatsSignatures.findByModelDoc", new String[]{"modelDoc"}, new Object[]{y}));
        buildBeanSignature(sign);
        openDialog("dlgSignature");
        update("form-save-signature");
    }

    public void addSignature() {
        if (signature != null) {
            if (signature.getTitre1() != null || signature.getTitre2() != null) {
                YvsWorkflowEtatsSignatures y = new YvsWorkflowEtatsSignatures(signature.getId());
                y.setModelDoc(selectModelDoc);
                y.setTitre1(signature.getTitre1());
                y.setTitre2(signature.getTitre2());
                y.setTitre3(signature.getTitre3());
                y.setTitre4(signature.getTitre4());
                y.setTitre5(signature.getTitre5());
                y.setSociete(currentAgence.getSociete());
                y.setAuthor(currentUser);
                if (y.getId() != null ? y.getId() > 0 : false) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsWorkflowEtatsSignatures) dao.save1(y);
                }
                succes();
                signature = new SignatureRapport();
            } else {
                getErrorMessage("Vous devez préciser le titre du signataire !");
            }
        }
    }

    public void buildBeanSignature(YvsWorkflowEtatsSignatures y) {
        if (y != null) {
            signature.setTitre1(y.getTitre1());
            signature.setTitre2(y.getTitre2());
            signature.setTitre3(y.getTitre3());
            signature.setTitre4(y.getTitre4());
            signature.setTitre5(y.getTitre5());
            update("form-save-signature");
        } else {
            signature = new SignatureRapport();
        }
    }

    public void resetForm() {
        signature = new SignatureRapport();
        update("form-save-signature");
    }
}
