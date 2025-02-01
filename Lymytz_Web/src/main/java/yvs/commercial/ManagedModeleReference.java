/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.base.YvsBaseElementReference;
import yvs.entity.base.YvsBaseModeleReference;
import yvs.entity.production.pilotage.YvsProdOrdreFabrication;
import yvs.util.Constantes;
import yvs.util.Managed;

/**
 *
 * @author Lymytz-Invité
 */
@ManagedBean
@SessionScoped
public class ManagedModeleReference extends Managed<ModeleReference, YvsBaseModeleReference> implements Serializable {

    @ManagedProperty(value = "#{modeleReference}")
    private ModeleReference modeleReference;
    private List<YvsBaseModeleReference> modeles;
    private YvsBaseModeleReference selectModele;

    private List<YvsBaseElementReference> elements;

    private String tabIds;

    private boolean updateModele;

    public ManagedModeleReference() {
        modeles = new ArrayList<>();
        elements = new ArrayList<>();
    }

    public YvsBaseModeleReference getSelectModele() {
        return selectModele;
    }

    public void setSelectModele(YvsBaseModeleReference selectModele) {
        this.selectModele = selectModele;
    }

    public boolean isUpdateModele() {
        return modeleReference != null ? modeleReference.getId() > 0 : false;
    }

    public void setUpdateModele(boolean updateModele) {
        this.updateModele = updateModele;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public ModeleReference getModeleReference() {
        return modeleReference;
    }

    public void setModeleReference(ModeleReference modeleReference) {
        this.modeleReference = modeleReference;
    }

    public List<YvsBaseModeleReference> getModeles() {
        return modeles;
    }

    public void setModeles(List<YvsBaseModeleReference> modeles) {
        this.modeles = modeles;
    }

    public List<YvsBaseElementReference> getElements() {
        return elements;
    }

    public void setElements(List<YvsBaseElementReference> elements) {
        this.elements = elements;
    }

    @Override
    public void loadAll() {
        loadAll(null);
    }

    public void loadAll(String module) {
        loadModele(module);
        loadElement(module);
    }

    public void loadModele(String module) {
        if (module != null ? module.trim().length() > 0 : false) {
            champ = new String[]{"societe", "module"};
            val = new Object[]{currentAgence.getSociete(), Constantes.MOD_COM};
            nameQueri = "YvsBaseModeleReference.findByModule";
        } else {
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
            nameQueri = "YvsBaseModeleReference.findAll";
        }
        modeles = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadElement(String module) {
        if (module != null ? module.trim().length() > 0 : false) {
            champ = new String[]{"module"};
            val = new Object[]{Constantes.MOD_COM};
            nameQueri = "YvsBaseElementReference.findByModule";
        } else {
            champ = new String[]{};
            val = new Object[]{};
            nameQueri = "YvsBaseElementReference.findAll";
        }
        elements = dao.loadNameQueries(nameQueri, champ, val);
    }

    @Override
    public ModeleReference recopieView() {
        modeleReference.setNew_(true);
        return modeleReference;
    }

    @Override
    public boolean controleFiche(ModeleReference bean) {
        if ((bean.getElement() != null) ? bean.getElement().getId() < 1 : true) {
            getErrorMessage("Vous devez renseigner l'élément correspondant à référence");
            return false;
        }
        if (bean.getPrefix() == null || bean.getPrefix().equals("")) {
            getErrorMessage("Vous devez entrer le préfixe");
            return false;
        }
        if (bean.getPrefix().contains("/") || bean.getPrefix().contains("-") || bean.getPrefix().contains("_")) {
            getErrorMessage("Vous ne pouvez pas mettre un séparateur sur le prefix");
            return false;
        }
        if ("".equals(bean.getSeparateur())) {
            getErrorMessage("Vous devez indiquer le séparateur");
            return false;
        }
        if (bean.getTaille() < 2) {
            getErrorMessage("Vous devez entrer une taille superieur a 1");
            return false;
        }
        if (bean.getId() > 0 && selectModele != null) {
            if (!bean.getPrefix().equals(selectModele.getPrefix())) {
                Object[] result = returnInfosReference(selectModele, null);
                if (result != null ? result.length > 0 : false) {
                    Long count = (Long) dao.loadObjectByNameQueries(result[0].toString(), (String[]) result[1], (Object[]) result[2]);
                    if (count != null ? count > 0 : false) {
                        getErrorMessage("Vous ne pouvez pas modifier ce prefix. Il est déjà utilisé");
                        return false;
                    }
                }
            }
        }
        YvsBaseModeleReference y = (YvsBaseModeleReference) dao.loadOneByNameQueries("YvsBaseModeleReference.findByPrefix", new String[]{"prefix", "societe"}, new Object[]{bean.getPrefix(), currentAgence.getSociete()});
        if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
            getErrorMessage("Vous avez déjà crée un model de reference avec ce prefix");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(ModeleReference bean) {
        cloneObject(modeleReference, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(modeleReference);
        modeleReference.setElement(new ElementReference());
        modeleReference.setSeparateur('-');
        tabIds = "";
        setUpdateModele(false);
        update("form_modele_reference");
        update("txt_apercu_reference");
    }

    @Override
    public boolean saveNew() {
        String action = isUpdateModele() ? "Modification" : "Insertion";
        try {
            ModeleReference bean = recopieView();
            if (controleFiche(bean)) {
                saveNewModelRef(bean);
                succes();
            }
            return true;
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible!");
            getException("ManagedModeleReference (saveNew)", ex);
            return false;
        }
    }

    private boolean saveNewModelRef(ModeleReference bean) {
        selectModele = UtilCom.buildModele(bean, currentUser, currentAgence.getSociete());
        selectModele.setDateUpdate(new Date());
        if (!isUpdateModele()) {
            selectModele.setDateSave(new Date());
            selectModele.setId(null);
            selectModele = (YvsBaseModeleReference) dao.save1(selectModele);
            modeleReference.setId(selectModele.getId());
        } else {
            dao.update(selectModele);
        }
        int idx = modeles.indexOf(selectModele);
        if (idx > -1) {
            modeles.set(idx, selectModele);
        } else {
            modeles.add(0, selectModele);
        }
        bean.setUpdate(true);
        actionOpenOrResetAfter(this);
        update("data_modele_reference");
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsBaseModeleReference> list = new ArrayList<>();
                YvsBaseModeleReference bean;
                for (Long ids : l) {
                    if (ids >= 0) {
                        bean = modeles.get(ids.intValue());
                        list.add(bean);
                        bean.setAuthor(currentUser);
                        bean.setDateUpdate(new Date());
                        dao.delete(bean);
                        if (bean.getId() == modeleReference.getId()) {
                            resetFiche();
                        }
                    }
                }
                
                modeles.removeAll(list);
                succes();
                resetFiche();
                update("form_modele_reference");
                update("data_modele_reference");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible!");
            System.err.println("system " + ex.getMessage() + " de modele");
        }
    }

    public void deleteBean_(YvsBaseModeleReference y) {
        selectModele = y;
    }

    public void deleteBean_() {
        try {
            if (selectModele != null) {
                dao.delete(selectModele);
                modeles.remove(selectModele);
                succes();
                resetFiche();
                update("form_modele_reference");
                update("data_modele_reference");
            } else {
                getErrorMessage("Vous devez selectionner le modèle");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible!");
            System.err.println("system " + ex.getMessage() + " de modele");
        }
    }

    @Override
    public void onSelectObject(YvsBaseModeleReference y) {
        selectModele = y;
        tabIds = modeles.indexOf(selectModele) + "";
        populateView(UtilCom.buildBeanModele(y));
        setUpdateModele(true);
        update("form_modele_reference");
        execute("onselectLine(" + tabIds + ",'modele_reference')");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            onSelectObject((YvsBaseModeleReference) ev.getObject());
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("form_modele_reference");
    }

    public void chooseElement() {
        if ((modeleReference.getElement() != null) ? modeleReference.getElement().getId() > 0 : false) {
            YvsBaseElementReference elt = elements.get(elements.indexOf(new YvsBaseElementReference(modeleReference.getElement().getId())));
            for (YvsBaseModeleReference m : modeles) {
                if (m.getElement().getDesignation().equals(elt.getDesignation())) {
                    populateView(UtilCom.buildBeanModele(m));
                    getWarningMessage("Une modele de reference existe pour cet élément!Vous etes en mode modification");
                    update("form_modele_reference");
                    return;
                }
            }
            if (!isUpdateModele()) {
                resetFiche();
            }
            modeleReference.setElement(UtilCom.buildBeanElement(elt));
            update("form_modele_reference");
        }
    }

    public void genereApercu() {
        modeleReference.setApercu("");
        Calendar cal = Calendar.getInstance();
        String inter = ((modeleReference.getPrefix() != null) ? modeleReference.getPrefix() : "");
        if (modeleReference.isCodePoint() ? modeleReference.getElementCode() != null : false) {
            String code = ".X";
            for (int i = 1; i < modeleReference.getLongueurCodePoint(); i++) {
                code += "X";
            }
            inter += code;
        }
        inter += modeleReference.getSeparateur();
        if (modeleReference.isJour()) {
            if (cal.get(Calendar.DATE) > 9) {
                inter += Integer.toString(cal.get(Calendar.DATE));
            }
            if (cal.get(Calendar.DATE) < 10) {
                inter += ("0" + Integer.toString(cal.get(Calendar.DATE)));
            }
        }
        if (modeleReference.isMois()) {
            if (cal.get(Calendar.MONTH) + 1 > 9) {
                inter += Integer.toString(cal.get(Calendar.MONTH) + 1);
            }
            if (cal.get(Calendar.MONTH) + 1 < 10) {
                inter += ("0" + Integer.toString(cal.get(Calendar.MONTH) + 1));
            }
        }
        if (modeleReference.isAnnee()) {
            inter += Integer.toString(cal.get(Calendar.YEAR)).substring(2);
        }
        inter += modeleReference.getSeparateur();
        for (int i = 0; i < modeleReference.getTaille(); i++) {
            inter += "0";
        }
        modeleReference.setApercu(inter);
    }

    public void initialiseDefaultModelNum() {
        ModeleReference bean;
        Long nb;
        for (YvsBaseElementReference el : elements) {
            //vérifie que la règle n'ai pas encore été enregistré
            nb = (Long) dao.loadObjectByNameQueries("YvsBaseModeleReference.findByElement_", new String[]{"document", "societe"}, new Object[]{el, currentAgence.getSociete()});
            if ((nb != null ? nb <= 0 : true) && el.getModelCourant()) {
                bean = new ModeleReference();
                bean.setAnnee(true);
                bean.setCodePoint(false);
                bean.setDateSave(new Date());
                bean.setElement(UtilCom.buildBeanElement(el));
                bean.setElementCode("SOCIETE");
                bean.setJour(true);
                bean.setLongueurCodePoint(3);
                bean.setModule(el.getModule());
                bean.setMois(true);
                bean.setPrefix(el.getDefaultPrefix());
                bean.setSeparateur('/');
                bean.setTaille(4);
                saveNewModelRef(bean);
            }
        }
        succes();
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
