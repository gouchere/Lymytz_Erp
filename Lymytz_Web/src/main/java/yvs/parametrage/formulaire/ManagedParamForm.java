/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.formulaire;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.param.formulaire.YvsParamChampFormulaire;
import yvs.entity.param.formulaire.YvsParamFormulaire;
import yvs.entity.param.formulaire.YvsParamModelFormulaire;
import yvs.entity.param.formulaire.YvsParamProprieteChamp;
import yvs.parametrage.UtilParam;
import yvs.util.Managed;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedParamForm extends Managed<ChampFormulaire, YvsParamProprieteChamp> implements Serializable {

    @ManagedProperty(value = "#{composantsEditable}")
    private ComposantsEditable compEditable;
    @ManagedProperty(value = "#{composantsVisible}")
    private ComposantsVisible compVisible;
    @ManagedProperty(value = "#{composantsObligatoire}")
    private ComposantsObligatoire compOblig;

    private ChampFormulaire composant = new ChampFormulaire();
    private Formulaire formulaire = new Formulaire();
    private ModelFormulaire modele = new ModelFormulaire();
    private List<YvsParamFormulaire> formulaires;
    private List<YvsParamModelFormulaire> modeles;
    private YvsParamModelFormulaire selectModel = new YvsParamModelFormulaire();
    private YvsParamChampFormulaire selectChamp = new YvsParamChampFormulaire();

    public ManagedParamForm() {
        formulaires = new ArrayList<>();
        modeles = new ArrayList<>();
    }

    public ModelFormulaire getModele() {
        return modele;
    }

    public void setModele(ModelFormulaire modele) {
        this.modele = modele;
    }

    public YvsParamModelFormulaire getSelectModel() {
        return selectModel;
    }

    public void setSelectModel(YvsParamModelFormulaire selectModel) {
        this.selectModel = selectModel;
    }

    public YvsParamChampFormulaire getSelectChamp() {
        return selectChamp;
    }

    public void setSelectChamp(YvsParamChampFormulaire selectChamp) {
        this.selectChamp = selectChamp;
    }

    public ChampFormulaire getComposant() {
        return composant;
    }

    public void setComposant(ChampFormulaire composant) {
        this.composant = composant;
    }

    public List<YvsParamFormulaire> getFormulaires() {
        return formulaires;
    }

    public void setFormulaires(List<YvsParamFormulaire> formulaires) {
        this.formulaires = formulaires;
    }

    public Formulaire getFormulaire() {
        return formulaire;
    }

    public void setFormulaire(Formulaire formulaire) {
        this.formulaire = formulaire;
    }

    public List<YvsParamModelFormulaire> getModeles() {
        return modeles;
    }

    public void setModeles(List<YvsParamModelFormulaire> modeles) {
        this.modeles = modeles;
    }

    public ComposantsEditable getCompEditable() {
        return compEditable;
    }

    public void setCompEditable(ComposantsEditable compEditable) {
        this.compEditable = compEditable;
    }

    public ComposantsVisible getCompVisible() {
        return compVisible;
    }

    public void setCompVisible(ComposantsVisible compVisible) {
        this.compVisible = compVisible;
    }

    public ComposantsObligatoire getCompOblig() {
        return compOblig;
    }

    public void setCompOblig(ComposantsObligatoire compOblig) {
        this.compOblig = compOblig;
    }

    @Override
    public void loadAll() {
        formulaires = dao.loadNameQueries("YvsParamFormulaire.findAll", new String[]{}, new Object[]{});
        loadModel(null);
        if (formulaire != null ? formulaire.getId() < 0 : true) {
            formulaire = new Formulaire();
        }
    }

    public void loadModel(YvsParamFormulaire y) {
        nameQueri = "YvsParamModelFormulaire.findAll";
        champ = new String[]{};
        val = new Object[]{};
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            nameQueri = "YvsParamModelFormulaire.findByForm";
            champ = new String[]{"formulaire"};
            val = new Object[]{y};
        }
        modeles = dao.loadNameQueries(nameQueri, champ, val);
        update("data_model_formulaire");
    }

    public void loadComposant(YvsParamModelFormulaire y) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            y.setComposants(new ArrayList<YvsParamProprieteChamp>());
            nameQueri = "YvsParamChampFormulaire.findByForm";
            champ = new String[]{"formulaire"};
            val = new Object[]{y.getFormulaire()};
            List<YvsParamChampFormulaire> list = dao.loadNameQueries(nameQueri, champ, val);
            YvsParamProprieteChamp p;
            for (YvsParamChampFormulaire ch : list) {
                p = (YvsParamProprieteChamp) dao.loadOneByNameQueries("YvsParamProprieteChamp.findByOne", new String[]{"champ", "modele"}, new Object[]{ch, y});
                if (p != null ? (p.getId() != null ? p.getId() < 1 : true) : true) {
                    p = new YvsParamProprieteChamp((long) -(y.getComposants().size() + 1));
                    p.setChamp(ch);
                    p.setModele(y);
                }
                p.setAuthor(currentUser);
                p.setDateUpdate(new Date());
                y.getComposants().add(p);
            }
        }
    }

    public boolean controleFiche(Formulaire bean) {
        if (bean == null) {
            return false;
        }
        if (bean.getIntitule() != null ? bean.getIntitule().trim().length() < 1 : true) {
            getErrorMessage("Vous devez precisez le nom");
            return false;
        }
        YvsParamFormulaire y = (YvsParamFormulaire) dao.loadOneByNameQueries("YvsParamFormulaire.findByIntitule", new String[]{"intitule"}, new Object[]{bean.getIntitule()});
        if (y != null ? (y.getId() != null ? y.getId() != bean.getId() : false) : false) {
            getErrorMessage("Vous ave deja créer ce formulaire");
            return false;
        }
        return true;
    }

    public boolean controleFiche(ModelFormulaire bean) {
        if (bean == null) {
            return false;
        }
        if (bean.getIntitule() != null ? bean.getIntitule().trim().length() < 1 : true) {
            getErrorMessage("Vous devez precisez le nom");
            return false;
        }
        if (bean.getFormulaire() != null ? bean.getFormulaire().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner le formulaire");
            return false;
        }
        YvsParamModelFormulaire y = (YvsParamModelFormulaire) dao.loadOneByNameQueries("YvsParamModelFormulaire.findByIntitule", new String[]{"intitule"}, new Object[]{bean.getIntitule()});
        if (y != null ? (y.getId() != null ? y.getId() != bean.getId() : false) : false) {
            getErrorMessage("Vous ave deja créer ce modèle de formulaire");
            return false;
        }
        return true;
    }

    @Override
    public boolean controleFiche(ChampFormulaire bean) {
        if (bean == null) {
            return false;
        }
        if (bean.getChamp() != null ? bean.getChamp().trim().length() < 1 : true) {
            getErrorMessage("Vous devez precisez le champ");
            return false;
        }
        if (bean.getCode() != null ? bean.getCode().trim().length() < 1 : true) {
            getErrorMessage("Vous devez precisez le code");
            return false;
        }
        if (bean.getFormulaire() != null ? bean.getFormulaire().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner le formulaire");
            return false;
        }
        YvsParamChampFormulaire y = (YvsParamChampFormulaire) dao.loadOneByNameQueries("YvsParamChampFormulaire.findByCode", new String[]{"code"}, new Object[]{bean.getCode()});
        if (y != null ? (y.getId() != null ? y.getId() != bean.getId() : false) : false) {
            getErrorMessage("Vous ave deja créer ce champ");
            return false;
        }
        return true;
    }

    @Override
    public ChampFormulaire recopieView() {
        ChampFormulaire r = new ChampFormulaire();
        r.setChamp(composant.getChamp());
        r.setCode(composant.getCode());
        r.setFormulaire(formulaire);
        r.setDateSave(composant.getDateSave());
        r.setDateUpdate(composant.getDateUpdate());
        return r;
    }

    @Override
    public void populateView(ChampFormulaire bean) {
        cloneObject(composant, bean);
    }

    public void populateView(Formulaire bean) {
        cloneObject(formulaire, bean);
    }

    @Override
    public void resetFiche() {
        composant = new ChampFormulaire();
        selectChamp = new YvsParamChampFormulaire();
    }

    public void resetFicheFormulaire() {
        formulaire = new Formulaire();
    }

    public void resetFicheModel() {
        modele = new ModelFormulaire();
        selectModel = new YvsParamModelFormulaire();
    }

    @Override
    public boolean saveNew() {
        String action = composant.getId() > 0 ? "Modification" : "Insertion";
        try {
            ChampFormulaire bean = recopieView();
            if (controleFiche(bean)) {
                YvsParamChampFormulaire y = UtilParam.buildChampFormulaire(bean, currentUser);
                if (bean.getId() < 1) {
                    y.setId(null);
                    y = (YvsParamChampFormulaire) dao.save1(y);
                    bean.setId(y.getId());
                } else {
                    dao.update(y);
                }
                int idx = formulaire.getComposants().indexOf(y);
                if (idx > -1) {
                    formulaire.getComposants().set(idx, y);
                } else {
                    formulaire.getComposants().add(y);
                }
                int idy = formulaires.indexOf(new YvsParamFormulaire(formulaire.getId()));
                if (idy > -1) {
                    idx = formulaires.get(idy).getComposants().indexOf(y);
                    if (idx > -1) {
                        formulaires.get(idy).getComposants().set(idx, y);
                    } else {
                        formulaires.get(idy).getComposants().add(y);
                    }
                }
                resetFiche();
                succes();
                update("data_composant_formulaire");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewFormulaire() {
        String action = formulaire.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(formulaire)) {
                YvsParamFormulaire y = UtilParam.buildFormulaire(formulaire, currentUser);
                if (formulaire.getId() < 1) {
                    y.setId(null);
                    y = (YvsParamFormulaire) dao.save1(y);
                } else {
                    dao.update(y);
                }
                int idx = formulaires.indexOf(y);
                if (idx > -1) {
                    formulaires.set(idx, y);
                } else {
                    formulaires.add(y);
                }
                succes();
                resetFicheFormulaire();
                update("data_formulaire");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewModel() {
        String action = modele.getId() > 0 ? "Modification" : "Insertion";
        try {
            cloneObject(modele.getFormulaire(), formulaire);
            if (controleFiche(modele)) {
                YvsParamModelFormulaire y = UtilParam.buildModelFormulaire(modele, currentUser);
                if (modele.getId() < 1) {
                    y.setId(null);
                    y = (YvsParamModelFormulaire) dao.save1(y);
                    modele.setId(y.getId());
                } else {
                    dao.update(y);
                }
                int idx = modeles.indexOf(y);
                if (idx > -1) {
                    modeles.set(idx, y);
                } else {
                    modeles.add(y);
                }
                succes();
                update("data_model_formulaire");
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
            if (selectChamp != null ? selectChamp.getId() > 0 : false) {
                dao.delete(selectChamp);
                int idx = formulaire.getComposants().indexOf(selectChamp);
                if (idx > -1) {
                    formulaire.getComposants().remove(idx);
                }
                idx = formulaires.indexOf(new YvsParamFormulaire(formulaire.getId()));
                if (idx > -1) {
                    formulaires.get(idx).getComposants().remove(selectChamp);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean(YvsParamChampFormulaire y) {
        selectChamp = y;
    }

    public void deleteBeanModel(YvsParamModelFormulaire y) {
        selectModel = y;
    }

    public void deleteBeanModel() {
        try {
            if (selectModel != null ? selectModel.getId() > 0 : false) {
                dao.delete(selectModel);
                modeles.remove(selectModel);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsParamChampFormulaire y = (YvsParamChampFormulaire) ev.getObject();
            populateView(UtilParam.buildBeanChampFormulaire(y));
        }
    }

    public void loadOnViewFormulaire(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsParamFormulaire y = (YvsParamFormulaire) ev.getObject();
            populateView(UtilParam.buildBeanFormulaire(y));
            loadModel(y);
        }
    }

    public void loadOnViewModel(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectModel = (YvsParamModelFormulaire) ev.getObject();
            loadComposant(selectModel);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void unLoadOnViewFormulaire(UnselectEvent ev) {
        resetFicheFormulaire();
    }

    public void unLoadOnViewModel(UnselectEvent ev) {
        selectModel = new YvsParamModelFormulaire();
    }

    public void updateModele(YvsParamModelFormulaire ev) {
        if (ev != null ? ev.getId() > 0 : false) {
            modele = UtilParam.buildBeanModelFormulaire(ev);
            openDialog("dlgAddModelFormulaire");
        }
    }

    public void newModele() {
        modele = new ModelFormulaire();
        update("txt_intitule_model_form");
        openDialog("dlgAddModelFormulaire");
    }

    public void chooseFormulaire() {
        if (formulaire != null) {
            if (formulaire.getId() > 0) {
                int idx = formulaires.indexOf(new YvsParamFormulaire(formulaire.getId()));
                if (idx > -1) {
                    loadModel(formulaires.get(idx));
                }
            } else if (formulaire.getId() == -1) {
                openDialog("dlgFormulaire");
            } else {
                loadModel(null);
            }
        } else {
            loadModel(null);
        }
    }

    public void activeObligatoire(YvsParamProprieteChamp y) {
        if (y != null) {
            long id = y.getId() != null ? y.getId() : 0;
            y.setObligatoire(!y.getObligatoire());
            if (id < 1) {
                y.setDateSave(new Date());
                y.setId(null);
                y = (YvsParamProprieteChamp) dao.save1(y);
            } else {
                dao.update(y);
            }
            int idx = selectModel.getComposants().indexOf(new YvsParamProprieteChamp(id));
            if (idx > -1) {
                selectModel.getComposants().set(idx, y);
            }
            idx = modeles.indexOf(selectModel);
            if (idx > -1) {
                modeles.set(idx, selectModel);
            }
            succes();
        }
    }

    public void activeVisible(YvsParamProprieteChamp y) {
        if (y != null) {
            long id = y.getId() != null ? y.getId() : 0;
            y.setVisible(!y.getVisible());
            if (id < 1) {
                y.setDateSave(new Date());
                y.setId(null);
                y = (YvsParamProprieteChamp) dao.save1(y);
            } else {
                dao.update(y);
            }
            int idx = selectModel.getComposants().indexOf(new YvsParamProprieteChamp(id));
            if (idx > -1) {
                selectModel.getComposants().set(idx, y);
            }
            idx = modeles.indexOf(selectModel);
            if (idx > -1) {
                modeles.set(idx, selectModel);
            }
            succes();
        }
    }

    public void activeEditable(YvsParamProprieteChamp y) {
        if (y != null) {
            long id = y.getId() != null ? y.getId() : 0;
            y.setEditable(!y.getEditable());
            if (id < 1) {
                y.setDateSave(new Date());
                y.setId(null);
                y = (YvsParamProprieteChamp) dao.save1(y);
            } else {
                dao.update(y);
            }
            int idx = selectModel.getComposants().indexOf(new YvsParamProprieteChamp(id));
            if (idx > -1) {
                selectModel.getComposants().set(idx, y);
            }
            idx = modeles.indexOf(selectModel);
            if (idx > -1) {
                modeles.set(idx, selectModel);
            }
            succes();
        }
    }

    public void loadParamFormulaire(String formulaire) {
        if (currentUser != null) {
            YvsParamModelFormulaire form = null;
            if (currentUser.getUsers() != null) {
                form = (YvsParamModelFormulaire) dao.loadOneByNameQueries("YvsParamUserFormulaire.findByOne", new String[]{"utilisateur", "intitule"}, new Object[]{currentUser.getUsers(), formulaire});
                List<YvsParamProprieteChamp> modelesForm = dao.loadNameQueries("YvsParamProprieteChamp.findByModele", new String[]{"modele"}, new Object[]{form});
                Field chVisible;
                Field chOblig;
                Field chEdit;
                Class<ComposantsEditable> classEdit = ComposantsEditable.class;
                Class<ComposantsVisible> classVis = ComposantsVisible.class;
                Class<ComposantsObligatoire> classOb = ComposantsObligatoire.class;
                for (YvsParamProprieteChamp md : modelesForm) {
                    try {
                        //cherche le champ de même nom que le code du module
                        chVisible = classVis.getDeclaredField(md.getChamp().getCode());
                        chVisible.setAccessible(true);
                        chVisible.set(compVisible, md.getVisible());

                        chOblig = classOb.getDeclaredField(md.getChamp().getCode());
                        chOblig.setAccessible(true);
                        chOblig.set(compOblig, md.getObligatoire());

                        chEdit = classEdit.getDeclaredField(md.getChamp().getCode());
                        chEdit.setAccessible(true);
                        chEdit.set(compEditable, md.getEditable());
                    } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
                        Logger.getLogger(ManagedParamForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

}
