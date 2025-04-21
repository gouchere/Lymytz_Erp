/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.objectifs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.base.produits.ManagedArticles;
import yvs.production.UtilProd;
import yvs.base.tiers.Tiers;
import yvs.commercial.Commerciales;
import yvs.commercial.ManagedCommerciaux;
import yvs.commercial.UtilCom;
import yvs.commercial.client.Client;
import yvs.commercial.client.ManagedClient;
import yvs.commercial.depot.ManagedPointVente;
import yvs.commercial.depot.PointVente;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.objectifs.YvsComCibleObjectif;
import yvs.entity.commercial.objectifs.YvsComModeleObjectif;
import yvs.entity.commercial.objectifs.YvsComObjectifsAgence;
import yvs.entity.commercial.objectifs.YvsComObjectifsComercial;
import yvs.entity.commercial.objectifs.YvsComObjectifsPointVente;
import yvs.entity.commercial.objectifs.YvsComPeriodeObjectif;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.produits.YvsBaseArticles;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.agence.ManagedAgence;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.dico.ManagedDico;
import yvs.parametrage.societe.UtilSte;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedObjectifVente extends Managed<ModelObjectif, YvsComModeleObjectif> implements Serializable {

    @ManagedProperty(value = "#{modelObjectif}")
    private ModelObjectif modelObjectif;
    private List<YvsComModeleObjectif> modelesObjectifs;
    private YvsComModeleObjectif selectModel;
    private YvsComCibleObjectif selectCible;
    private String tabIds;
    private ObjectifsCom objectif = new ObjectifsCom();
    private CibleObjectif cible = new CibleObjectif();

    private List<YvsComComerciale> selectedCommeciaux;
    private YvsComObjectifsComercial selectedObjectif;
    private List<YvsAgences> selectedAgences;
    private YvsComObjectifsAgence selectedObjAgence;
    private List<YvsBasePointVente> selectedPoints;
    private YvsComObjectifsPointVente selectedObjPoint;

    public ManagedObjectifVente() {
        modelesObjectifs = new ArrayList<>();
        selectedCommeciaux = new ArrayList<>();
        selectedAgences = new ArrayList<>();
        selectedPoints = new ArrayList<>();
    }

    public List<YvsAgences> getSelectedAgences() {
        return selectedAgences;
    }

    public void setSelectedAgences(List<YvsAgences> selectedAgences) {
        this.selectedAgences = selectedAgences;
    }

    public YvsComObjectifsAgence getSelectedObjAgence() {
        return selectedObjAgence;
    }

    public void setSelectedObjAgence(YvsComObjectifsAgence selectedObjAgence) {
        this.selectedObjAgence = selectedObjAgence;
    }

    public List<YvsBasePointVente> getSelectedPoints() {
        return selectedPoints;
    }

    public void setSelectedPoints(List<YvsBasePointVente> selectedPoints) {
        this.selectedPoints = selectedPoints;
    }

    public YvsComObjectifsPointVente getSelectedObjPoint() {
        return selectedObjPoint;
    }

    public void setSelectedObjPoint(YvsComObjectifsPointVente selectedObjPoint) {
        this.selectedObjPoint = selectedObjPoint;
    }

    public List<YvsComModeleObjectif> getModelesObjectifs() {
        return modelesObjectifs;
    }

    public void setModelesObjectifs(List<YvsComModeleObjectif> modelesObjectifs) {
        this.modelesObjectifs = modelesObjectifs;
    }

    public ModelObjectif getModelObjectif() {
        return modelObjectif;
    }

    public void setModelObjectif(ModelObjectif modelObjectif) {
        this.modelObjectif = modelObjectif;
    }

    public CibleObjectif getCible() {
        return cible;
    }

    public void setCible(CibleObjectif cible) {
        this.cible = cible;
    }

    public YvsComModeleObjectif getSelectModel() {
        return selectModel;
    }

    public void setSelectModel(YvsComModeleObjectif selectModel) {
        this.selectModel = selectModel;
    }

    public YvsComCibleObjectif getSelectCible() {
        return selectCible;
    }

    public void setSelectCible(YvsComCibleObjectif selectCible) {
        this.selectCible = selectCible;
    }

    public List<YvsComComerciale> getSelectedCommeciaux() {
        return selectedCommeciaux;
    }

    public void setSelectedCommeciaux(List<YvsComComerciale> selectedCommeciaux) {
        this.selectedCommeciaux = selectedCommeciaux;
    }

    public ObjectifsCom getObjectif() {
        return objectif;
    }

    public void setObjectif(ObjectifsCom objectif) {
        this.objectif = objectif;
    }

    public YvsComObjectifsComercial getSelectedObjectif() {
        return selectedObjectif;
    }

    public void setSelectedObjectif(YvsComObjectifsComercial selectedObjectif) {
        this.selectedObjectif = selectedObjectif;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public boolean controleFiche(ModelObjectif bean) {
        if ((bean.getCodeid() == null) ? bean.getCodeid().trim().isEmpty() : false) {
            getErrorMessage("Vous devez entrer le code d'identification du modèle !");
            return false;
        }
        if ((bean.getIndicateur() == null) ? bean.getIndicateur().trim().isEmpty() : false) {
            getErrorMessage("Vous devez préciser l'indicateur de performance !");
            return false;
        }
        if ((bean.getTitre() == null) ? bean.getTitre().trim().isEmpty() : false) {
            getErrorMessage("Vous devez entrer le titre du modèle !");
            return false;
        }
        return true;
    }

    @Override
    public void loadAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ModelObjectif recopieView() {
        ModelObjectif mod = new ModelObjectif();
        mod.setCodeid(modelObjectif.getCodeid());
        mod.setId(modelObjectif.getId());
        mod.setIndicateur(modelObjectif.getIndicateur());
        mod.setTitre(modelObjectif.getTitre());
        return mod;
    }

    @Override
    public void populateView(ModelObjectif bean) {
        cloneObject(modelObjectif, bean);
    }

    public void deleteBean_() {
        try {
            System.err.println("tabIds = " + tabIds);
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsComModeleObjectif> list = new ArrayList<>();
                YvsComModeleObjectif bean;
                for (Long ids : l) {
                    bean = modelesObjectifs.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);
                }
                resetFiche();
                modelesObjectifs.removeAll(list);
                succes();
                tabIds = "";
                update("table_model_objectifVente");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (modelObjectif.getId() >= 0) {
                dao.delete(new YvsComModeleObjectif(modelObjectif.getId()));
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible !");
            getException("Lymytz-Error >>>>>", ex);
        }
    }

    public void selectToDelete(YvsComModeleObjectif mod) {
        selectModel = mod;
        openDialog("dlgDelModel");
    }

    public void selectToDeleteCible(YvsComCibleObjectif mod) {
        selectCible = mod;
        openDialog("dlgCibleModel");
    }

    public void deleteLineObjectif() {
        if ((selectModel != null) ? selectModel.getId() > 0 : false) {
            try {
                dao.delete(new YvsComModeleObjectif(selectModel.getId()));
                modelesObjectifs.remove(selectModel);
                update("table_model_objectifVente");
            } catch (Exception ex) {
                getErrorMessage("Suppression impossible !");
                getException("Lymytz Error >>>", ex);
            }
        }
    }

    public void deleteLineCibleObjectif() {
        if ((selectCible != null) ? selectCible.getId() > 0 : false) {
            try {
                dao.delete(new YvsComCibleObjectif(selectCible.getId()));
                modelObjectif.getCiblesObjectifs().remove(selectCible);
                update("table_cible_objectifs");
                switch (selectCible.getTableExterne()) {
                    case Constantes.SCR_ARTICLES:
                        if (selectCible.getIdExterne().equals(cible.getArticle().getId())) {
                            cible.setArticle(new Articles());
                            update("zone_obj_cible_art");
                        }
                        break;
                    case Constantes.SCR_CONDITIONNEMENT:
                        if (selectCible.getIdExterne().equals(cible.getUnite().getId())) {
                            cible.setArticle(new Articles());
                            cible.setUnite(new Conditionnement());
                            update("zone_obj_cible_art");
                        }
                        break;
                    case Constantes.SCR_CLIENTS:
                        if (selectCible.getIdExterne().equals(cible.getClient().getId())) {
                            cible.setClient(new Client());
                            update("zone_obj_cible_client");
                        }
                        break;
                    case Constantes.SCR_DICTIONNAIRE:
                        if (selectCible.getIdExterne().equals(cible.getZone().getId())) {
                            cible.setZone(new Dictionnaire());
                            update("zone_obj_cible_zone");
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception ex) {
                getErrorMessage("Suppression impossible !");
                getException("Lymytz Error >>>", ex);
            }
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            YvsComModeleObjectif mod = (YvsComModeleObjectif) ev.getObject();
            selectOneLine(mod);
            tabIds = modelesObjectifs.indexOf(mod) + "";
        }
    }

    public void selectOneLine(YvsComModeleObjectif obj) {
        selectModel = obj;
        populateView(UtilCom.buildBeanObjectifs(obj));
        populateForm(obj);
        update("form_main_objectif_vente");
    }

    private void populateForm(YvsComModeleObjectif mod) {
        if (mod != null ? mod.getCibles() != null : false) {
            for (YvsComCibleObjectif c : mod.getCibles()) {
                switch (c.getTableExterne()) {
                    case Constantes.SCR_ARTICLES:
                        cible.setArticle(UtilProd.buildSimpleBeanArticles((YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{c.getIdExterne()})));
                        cible.setUnite(new Conditionnement());
                        break;
                    case Constantes.SCR_CONDITIONNEMENT:
                        cible.setUnite(UtilProd.buildBeanConditionnement((YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findById", new String[]{"id"}, new Object[]{c.getIdExterne()})));
                        if (cible.getUnite() != null ? cible.getUnite().getArticle() != null : false) {
                            cible.setArticle(cible.getUnite().getArticle());
                        }
                        break;
                    case Constantes.SCR_CLIENTS:
                        cible.setClient(UtilCom.buildBeanClient((YvsComClient) dao.loadOneByNameQueries("YvsComClient.findById", new String[]{"id"}, new Object[]{c.getIdExterne()})));
                        break;
                    case Constantes.SCR_DICTIONNAIRE:
                        cible.setZone(UtilSte.buildBeanDictionnaire((YvsDictionnaire) dao.loadOneByNameQueries("YvsDictionnaire.findById", new String[]{"id"}, new Object[]{c.getIdExterne()})));
                        break;
                }
            }
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        resetFiche(modelObjectif);
        modelObjectif.getCiblesObjectifs().clear();
        modelObjectif.setActif(true);
        cible = new CibleObjectif();
        selectModel = new YvsComModeleObjectif();
        update("form_main_objectif_vente");
    }

    @Override
    public boolean saveNew() {
        if (controleFiche(modelObjectif)) {
            YvsComModeleObjectif mod = UtilCom.buildBeanObjectifs(modelObjectif);
            mod.setSociete(currentAgence.getSociete());
            mod.setAuthor(currentUser);
            if (mod.getId() > 0) {
                mod.setDateUpdate(new Date());
                mod.setAuthor(currentUser);
                dao.update(mod);
                int idx = modelesObjectifs.indexOf(mod);
                if (idx >= 0) {
                    modelesObjectifs.set(idx, mod);
                }
            } else {
                mod.setId(null);
                mod.setDateSave(new Date());
                mod.setDateUpdate(new Date());
                mod = (YvsComModeleObjectif) dao.save1(mod);
                modelObjectif.setId(mod.getId());
                modelesObjectifs.add(0, mod);
            }
//            saveOrUpdateCible(mod);
            succes();
            update("table_model_objectifVente");
        }
        return true;
    }

    public void saveCibleArticle() {
        if (cible.getUnite() != null ? cible.getUnite().getId() > 0 : false) {
            saveCibleObjectif(Constantes.SCR_CONDITIONNEMENT, cible.getUnite().getId());
        } else if (cible.getArticle() != null ? cible.getArticle().getId() > 0 : false) {
            saveCibleObjectif(Constantes.SCR_ARTICLES, cible.getArticle().getId());
        }
    }

    public void saveCibleObjectif(String table, long element) {
        if (element > 0) {
            YvsComCibleObjectif y = (YvsComCibleObjectif) dao.loadOneByNameQueries("YvsComCibleObjectif.findByOneElt", new String[]{"table", "idExterne", "model"}, new Object[]{table, element, new YvsComModeleObjectif(modelObjectif.getId())});
            if (y != null ? y.getId() < 1 : true) {
                y = buildCible(element, selectModel, table);
                y.setId(null);
                y = (YvsComCibleObjectif) dao.save1(y);
                cible.setId(y.getId());

                int idx = modelObjectif.getCiblesObjectifs().indexOf(y);
                if (idx < 0) {
                    modelObjectif.getCiblesObjectifs().add(0, y);
                } else {
                    modelObjectif.getCiblesObjectifs().set(idx, y);
                }
                succes();
                update("table_cible_objectifs");
            } else {
                getErrorMessage("Cette cible est déjà définie !");
            }
        } else {
            getErrorMessage("Cible non définie !");
        }
    }

    private void saveOrUpdateCible(YvsComModeleObjectif mod) {
        if (mod.getCibles() != null) {
            boolean upArt = true, upCod = true, upClt = true, upZon = true;
            YvsComCibleObjectif cArt = null, cCod = null, cClt = null, cZon = null;
            for (YvsComCibleObjectif c : mod.getCibles()) {
                switch (c.getTableExterne()) {
                    case Constantes.SCR_ARTICLES:
                        if (cible.getArticle().getId() <= 0) {
                            //supprime cette cible
                            dao.delete(new YvsComCibleObjectif(c.getId()));
                            upArt = false;
                        }
                        cArt = c;
                        break;
                    case Constantes.SCR_CONDITIONNEMENT:
                        if (cible.getUnite().getId() <= 0) {
                            //supprime cette cible
                            dao.delete(new YvsComCibleObjectif(c.getId()));
                            upCod = false;
                        }
                        cCod = c;
                        break;
                    case Constantes.SCR_CLIENTS:
                        if (cible.getClient().getId() <= 0) {
                            //supprime cette cible
                            dao.delete(new YvsComCibleObjectif(c.getId()));
                            upClt = false;
                        }
                        cClt = c;
                        break;
                    case Constantes.SCR_DICTIONNAIRE:
                        if (cible.getZone().getId() <= 0) {
                            //supprime cette cible
                            dao.delete(new YvsComCibleObjectif(c.getId()));
                            upZon = false;
                        }
                        cZon = c;
                        break;
                }
            }
            if (upArt && cible.getArticle().getId() > 0) {
                if ((cArt != null) ? !cArt.getIdExterne().equals(cible.getArticle().getId()) : false) {
                    //modifie l'article
                    cArt.setIdExterne(cible.getArticle().getId());
                    cArt.setAuthor(currentUser);
                    cArt.setDateSave(new Date());
                    cArt.setDateUpdate(new Date());
                    dao.update(cArt);
                } else {
                    cArt = buildCible(cible.getArticle().getId(), mod, Constantes.SCR_ARTICLES);
                    cArt.setId(null);
                    cArt = (YvsComCibleObjectif) dao.save1(cArt);
                    modelObjectif.getCiblesObjectifs().add(cArt);
                }
            }
            if (upCod && cible.getUnite().getId() > 0) {
                if ((cCod != null) ? !cCod.getIdExterne().equals(cible.getUnite().getId()) : false) {
                    //modifie l'article
                    cCod.setIdExterne(cible.getUnite().getId());
                    cCod.setAuthor(currentUser);
                    cCod.setDateSave(new Date());
                    cCod.setDateUpdate(new Date());
                    dao.update(cCod);
                } else {
                    cCod = buildCible(cible.getUnite().getId(), mod, Constantes.SCR_CONDITIONNEMENT);
                    cCod.setId(null);
                    cCod = (YvsComCibleObjectif) dao.save1(cCod);
                    modelObjectif.getCiblesObjectifs().add(cCod);
                }
            }
            if (upClt && cible.getClient().getId() > 0) {
                if ((cArt != null) ? !cArt.getIdExterne().equals(cible.getClient().getId()) : false) {
                    //modifie l'article
                    cClt.setIdExterne(cible.getClient().getId());
                    cClt.setAuthor(currentUser);
                    cClt.setDateSave(new Date());
                    cClt.setDateUpdate(new Date());
                    dao.update(cClt);
                } else {
                    cClt = buildCible(cible.getClient().getId(), mod, Constantes.SCR_CLIENTS);
                    cClt.setId(null);
                    cClt = (YvsComCibleObjectif) dao.save1(cClt);
                    modelObjectif.getCiblesObjectifs().add(cClt);
                }
            }
            if (upZon && cible.getZone().getId() > 0) {
                if ((cZon != null) ? !cZon.getIdExterne().equals(cible.getZone().getId()) : false) {
                    //modifie l'article
                    cZon.setIdExterne(cible.getZone().getId());
                    cZon.setAuthor(currentUser);
                    cZon.setDateSave(new Date());
                    cZon.setDateUpdate(new Date());
                    dao.update(cZon);
                } else {
                    cZon = buildCible(cible.getZone().getId(), mod, Constantes.SCR_DICTIONNAIRE);
                    cZon.setId(null);
                    cZon = (YvsComCibleObjectif) dao.save1(cZon);
                    modelObjectif.getCiblesObjectifs().add(cZon);
                }
            }
        }
    }

    private YvsComCibleObjectif buildCible(Long idExt, YvsComModeleObjectif mod, String table) {
        YvsComCibleObjectif re = new YvsComCibleObjectif();
        re.setAuthor(currentUser);
        re.setDateSave(new Date());
        re.setDateUpdate(new Date());
        re.setIdExterne(idExt);
        re.setLibelle(cible.getLibelle());
        re.setObjectif(mod);
        re.setTableExterne(table);
        re.setNew_(true);
        return re;
    }

    public void selectOneLineArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            cible.setArticle(UtilProd.buildBeanArticles(bean));
        }
    }

    public void searchArticle(String refArt) {
        cible.getArticle().setDesignation("");
        cible.getArticle().setError(true);
        cible.getArticle().setId(0);
        ManagedArticles service = (ManagedArticles) giveManagedBean("Marticle");
        if (service != null) {
            Articles y = service.findArticleActif(refArt, true);
            if (service.getListArticle() != null ? !service.getListArticle().isEmpty() : false) {
                if (service.getListArticle().size() > 1) {
                    update("data_articles_objectifs_vente");
                } else {
                    cible.setArticle(y);
                    update("zone_obj_cible_art");
                }
                cible.getArticle().setError(false);
            }
        }
    }

    public void selectOneLineDico(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsDictionnaire bean = (YvsDictionnaire) ev.getObject();
            cible.setZone(UtilSte.buildBeanDictionnaire(bean));
        }
    }

    public void searchZone(String titre, String codeZone) {
        cible.getZone().setLibelle("");
        cible.getZone().setError(true);
        cible.getZone().setId(0);
        ManagedDico service = (ManagedDico) giveManagedBean("Mdico");
        if (service != null) {
            Dictionnaire y = service.findZoneActif(titre, codeZone, true);
            if (service.getDictionnaires() != null ? !service.getDictionnaires().isEmpty() : false) {
                if (service.getDictionnaires().size() > 1) {
                    update("data_zone_objectifs_vente");
                } else {
                    cible.setZone(y);
                }
                cible.getZone().setError(false);
            }
        }
    }

    public void searchClient(String code) {
        cible.getClient().setId(0);
        cible.getClient().setError(true);
        cible.getClient().setTiers(new Tiers());
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (m != null) {
            Client y = m.searchClient(code, true);
            if (m.getClients() != null ? !m.getClients().isEmpty() : false) {
                if (m.getClients().size() > 1) {
                    update("data_client_objectifs_vente");
                } else {
                    cible.setClient(y);
                    update("zone_obj_cible_client");
                }
                cible.getClient().setError(false);
            }
        }
    }

    public void selectOneLineClient(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComClient bean = (YvsComClient) ev.getObject();
            cible.setClient(UtilCom.buildBeanClient(bean));
            update("zone_obj_cible_client");
        }
    }
    public boolean initForm = true;

    public void loadAllModeleObjectif(boolean avancer) {
        loadAllModeleObjectif(avancer, initForm);
    }

    public void loadAllModeleObjectif(boolean avancer, boolean init) {
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        modelesObjectifs = paginator.executeDynamicQuery("YvsComModeleObjectif", "y.codeRef", avancer, init, (int) imax, dao);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComModeleObjectif> re = paginator.parcoursDynamicData("YvsComModeleObjectif", "y", "y.codeRef", getOffset(), dao);
        if (!re.isEmpty()) {
            selectOneLine(re.get(0));
            openDlgToAttribObj(re.get(0));
        }
    }

    public void activeObjectif(YvsComModeleObjectif model) {
        if (model != null ? model.getId() > 0 : false) {
            model.setActif(!model.getActif());
            dao.update(model);
            int idx = modelesObjectifs.indexOf(model);
            if (idx > -1) {
                modelesObjectifs.set(idx, model);
            }
            succes();
        }
    }

    public void openDlgToAttribObj(YvsComModeleObjectif model) {
        selectModel = model;
        selectOneLine(model);
        loadDataAttribObjectif();
        openDialog("dlgAttribObj");
        update("zone_attrib_obj");
        update("table_objects_objectif");
        update("table_model_objectifVente");
    }

    public void openDlgToAttribObj_() {
        if (modelObjectif.getId() > 0) {
            int idx = modelesObjectifs.indexOf(new YvsComModeleObjectif(modelObjectif.getId()));
            if (idx >= 0) {
                openDlgToAttribObj(modelesObjectifs.get(idx));
            } else {
                getErrorMessage("Aucun model n'a été selectionné !");
            }
        } else {
            getErrorMessage("Aucun model n'a été selectionné !");
        }
    }

    public void loadDataAttribObjectif() {
        switch (objectif.getSource()) {
            case "A": {
                ManagedAgence service = (ManagedAgence) giveManagedBean(ManagedAgence.class);
                if (service != null) {
                    modelObjectif.getObjectifsPeriodesAgs().clear();
                    for (YvsAgences c : service.getListAgence()) {
                        modelObjectif.getObjectifsPeriodesAgs().add(buildObjectif(c));
                    }
                }
                break;
            }
            case "P": {
                ManagedPointVente service = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                if (service != null) {
                    modelObjectif.getObjectifsPeriodesPoint().clear();
                    for (YvsBasePointVente c : service.getPointsvente()) {
                        modelObjectif.getObjectifsPeriodesPoint().add(buildObjectif(c));
                    }
                }
                break;
            }
            default: {
                ManagedCommerciaux service = (ManagedCommerciaux) giveManagedBean(ManagedCommerciaux.class);
                if (service != null) {
                    modelObjectif.getObjectifsPeriodes().clear();
                    for (YvsComComerciale c : service.getCommerciaux()) {
                        modelObjectif.getObjectifsPeriodes().add(buildObjectif(c));
                    }
                }
            }
        }
    }

    public void pagineDataObjAttrib(boolean avancer) {
        switch (objectif.getSource()) {
            case "A": {
                ManagedAgence service = (ManagedAgence) giveManagedBean(ManagedAgence.class);
                if (service != null) {
                    loadDataAttribObjectif();
                }
                break;
            }
            case "P": {
                ManagedPointVente service = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                if (service != null) {
                    service.loadAllPointVente(avancer, false);
                    loadDataAttribObjectif();
                }
                break;
            }
            default: {
                ManagedCommerciaux service = (ManagedCommerciaux) giveManagedBean(ManagedCommerciaux.class);
                if (service != null) {
                    service.loadAllCommerciaux(avancer, false);
                    loadDataAttribObjectif();
                }
            }
        }
    }

    private YvsComObjectifsComercial buildObjectif(YvsComComerciale c) {
        YvsComObjectifsComercial re;
        if (objectif.getPeriode().getId() > 0) {
            re = (YvsComObjectifsComercial) dao.loadOneByNameQueries("YvsComObjectifsComercial.findByPeriodeCom", new String[]{"periode", "commercial", "objectif"}, new Object[]{new YvsComPeriodeObjectif(objectif.getPeriode().getId()), c, new YvsComModeleObjectif(modelObjectif.getId())});
            if (re != null) {
                return re;
            }
        }
        re = new YvsComObjectifsComercial(-c.getId());
        re.setAuthor(currentUser);
        re.setCommercial(c);
        re.setValeur(0d);
        re.setObjectif(selectModel);
        if (objectif.getPeriode().getId() > 0) {
            ManagedPeriodeObjectif service = (ManagedPeriodeObjectif) giveManagedBean(ManagedPeriodeObjectif.class);
            if (service != null) {
                int idx = service.getPeriodes().indexOf(new YvsComPeriodeObjectif(objectif.getPeriode().getId()));
                if (idx >= 0) {
                    re.setPeriode(service.getPeriodes().get(idx));
                }
            }
        }
        return re;
    }

    private YvsComObjectifsAgence buildObjectif(YvsAgences c) {
        YvsComObjectifsAgence re;
        if (objectif.getPeriode().getId() > 0) {
            re = (YvsComObjectifsAgence) dao.loadOneByNameQueries("YvsComObjectifsAgence.findByPeriodeCom", new String[]{"periode", "agence", "objectif"}, new Object[]{new YvsComPeriodeObjectif(objectif.getPeriode().getId()), c, new YvsComModeleObjectif(modelObjectif.getId())});
            if (re != null) {
                return re;
            }
        }
        re = new YvsComObjectifsAgence(-c.getId());
        re.setAuthor(currentUser);
        re.setAgence(c);
        re.setValeur(0d);
        re.setObjectif(selectModel);
        if (objectif.getPeriode().getId() > 0) {
            ManagedPeriodeObjectif service = (ManagedPeriodeObjectif) giveManagedBean(ManagedPeriodeObjectif.class);
            if (service != null) {
                int idx = service.getPeriodes().indexOf(new YvsComPeriodeObjectif(objectif.getPeriode().getId()));
                if (idx >= 0) {
                    re.setPeriode(service.getPeriodes().get(idx));
                }
            }
        }
        return re;
    }

    private YvsComObjectifsPointVente buildObjectif(YvsBasePointVente c) {
        YvsComObjectifsPointVente re;
        if (objectif.getPeriode().getId() > 0) {
            re = (YvsComObjectifsPointVente) dao.loadOneByNameQueries("YvsComObjectifsPointVente.findByPeriodeCom", new String[]{"periode", "pointVente", "objectif"}, new Object[]{new YvsComPeriodeObjectif(objectif.getPeriode().getId()), c, new YvsComModeleObjectif(modelObjectif.getId())});
            if (re != null) {
                return re;
            }
        }
        re = new YvsComObjectifsPointVente(-c.getId());
        re.setAuthor(currentUser);
        re.setPointVente(c);
        re.setValeur(0d);
        re.setObjectif(selectModel);
        if (objectif.getPeriode().getId() > 0) {
            ManagedPeriodeObjectif service = (ManagedPeriodeObjectif) giveManagedBean(ManagedPeriodeObjectif.class);
            if (service != null) {
                int idx = service.getPeriodes().indexOf(new YvsComPeriodeObjectif(objectif.getPeriode().getId()));
                if (idx >= 0) {
                    re.setPeriode(service.getPeriodes().get(idx));
                }
            }
        }
        return re;
    }

    public void selectLineObjCom(SelectEvent ev) {
        selectedObjectif = (YvsComObjectifsComercial) ev.getObject();
        if (selectedObjectif != null) {
            objectif.setPeriode(UtilCom.buildBeanPeriode(selectedObjectif.getPeriode()));
            objectif.setModel(UtilCom.buildBeanObjectifs(selectedObjectif.getObjectif()));
            objectif.setMontantObjectif(selectedObjectif.getValeur());
            objectif.setComercial(new Commerciales(selectedObjectif.getCommercial().getId(), selectedObjectif.getCommercial().getNom_prenom()));
            update("zone_attrib_obj");
        }
    }

    public void selectLineObjAgs(SelectEvent ev) {
        selectedObjAgence = (YvsComObjectifsAgence) ev.getObject();
        if (selectedObjAgence != null) {
            objectif.setPeriode(UtilCom.buildBeanPeriode(selectedObjAgence.getPeriode()));
            objectif.setModel(UtilCom.buildBeanObjectifs(selectedObjAgence.getObjectif()));
            objectif.setMontantObjectif(selectedObjAgence.getValeur());
            objectif.setAgence(new Agence(selectedObjAgence.getAgence().getId(), selectedObjAgence.getAgence().getDesignation()));
            update("zone_attrib_obj");
        }
    }

    public void selectLineObjPoint(SelectEvent ev) {
        selectedObjPoint = (YvsComObjectifsPointVente) ev.getObject();
        if (selectedObjPoint != null) {
            objectif.setPeriode(UtilCom.buildBeanPeriode(selectedObjPoint.getPeriode()));
            objectif.setModel(UtilCom.buildBeanObjectifs(selectedObjPoint.getObjectif()));
            objectif.setMontantObjectif(selectedObjPoint.getValeur());
            objectif.setPointVente(new PointVente(selectedObjPoint.getPointVente().getId(), selectedObjPoint.getPointVente().getLibelle()));
            update("zone_attrib_obj");
        }
    }

    public void saveNewObjectifCom() {
        if ((objectif != null) ? objectif.getPeriode().getId() > 0 : false) {
            switch (objectif.getSource()) {
                case "A": {
                    if ((selectedObjAgence != null)) {
                        if (objectif.getMontantObjectif() > 0) {
                            selectedObjAgence.setDateUpdate(new Date());
                            selectedObjAgence.setValeur(objectif.getMontantObjectif());
                            selectedObjAgence.setObjectif(selectModel);
                            selectedObjAgence.setPeriode(new YvsComPeriodeObjectif(objectif.getPeriode().getId()));
                            selectedObjAgence.setNew_(true);
                            if (selectedObjAgence.getId() <= 0) {
                                selectedObjAgence.setId(null);
                                selectedObjAgence.setDateSave(new Date());
                                selectedObjAgence.setId(null);
                                selectedObjAgence = (YvsComObjectifsAgence) dao.save1(selectedObjAgence);
                            } else {
                                dao.update(selectedObjAgence);
                            }
                        } else {
                            if (selectedObjAgence.getId() > 0) {
                                try {
                                    dao.delete(selectedObjAgence);
                                } catch (Exception ex) {
                                    getException("Param Objectif -->>", ex);
                                    getWarningMessage("Opération non terminé");
                                }
                            }
                        }
                        update("table_agences_objectif");
                    }
                }
                case "P": {
                    if ((selectedObjPoint != null)) {
                        if (objectif.getMontantObjectif() > 0) {
                            selectedObjPoint.setDateUpdate(new Date());
                            selectedObjPoint.setValeur(objectif.getMontantObjectif());
                            selectedObjPoint.setObjectif(selectModel);
                            selectedObjPoint.setPeriode(new YvsComPeriodeObjectif(objectif.getPeriode().getId()));
                            selectedObjPoint.setNew_(true);
                            if (selectedObjPoint.getId() <= 0) {
                                selectedObjPoint.setId(null);
                                selectedObjPoint.setDateSave(new Date());
                                selectedObjPoint.setId(null);
                                selectedObjPoint = (YvsComObjectifsPointVente) dao.save1(selectedObjPoint);
                            } else {
                                dao.update(selectedObjPoint);
                            }
                        } else {
                            if (selectedObjPoint.getId() > 0) {
                                try {
                                    dao.delete(selectedObjPoint);
                                } catch (Exception ex) {
                                    getException("Param Objectif -->>", ex);
                                    getWarningMessage("Opération non terminé");
                                }
                            }
                        }
                        update("table_points_vente_objectif");
                    }
                }
                default: {
                    if ((selectedObjectif != null)) {
                        if (objectif.getMontantObjectif() > 0) {
                            selectedObjectif.setDateUpdate(new Date());
                            selectedObjectif.setValeur(objectif.getMontantObjectif());
                            selectedObjectif.setObjectif(selectModel);
                            selectedObjectif.setPeriode(new YvsComPeriodeObjectif(objectif.getPeriode().getId()));
                            selectedObjectif.setNew_(true);
                            if (selectedObjectif.getId() <= 0) {
                                selectedObjectif.setId(null);
                                selectedObjectif.setDateSave(new Date());
                                selectedObjectif.setId(null);
                                selectedObjectif = (YvsComObjectifsComercial) dao.save1(selectedObjectif);
                            } else {
                                dao.update(selectedObjectif);
                            }
                        } else {
                            if (selectedObjectif.getId() > 0) {
                                try {
                                    dao.delete(selectedObjectif);
                                } catch (Exception ex) {
                                    getException("Param Objectif -->>", ex);
                                    getWarningMessage("Opération non terminé");
                                }
                            }
                        }
                        update("table_commerciaux_objectif");
                    }
                }
            }
        } else {
            getErrorMessage("Voud devez precisez la période d'objectif !");
        }
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllModeleObjectif(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllModeleObjectif(true, true);
    }

    public String giveLibelleCible(String table, long id) {
        String re;
        switch (table) {
            case Constantes.SCR_ARTICLES:
                YvsBaseArticles b = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{id});
                re = (b != null) ? b.getDesignation() + " [" + b.getRefArt() + "] " : "";
                break;
            case Constantes.SCR_CONDITIONNEMENT:
                YvsBaseConditionnement o = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findById", new String[]{"id"}, new Object[]{id});
                re = (o != null) ? o.getArticle().getDesignation() + " [" + o.getArticle().getRefArt() + "] " + " [" + o.getUnite().getReference() + "] " : "";
                break;
            case Constantes.SCR_CLIENTS:
                YvsComClient c = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findById", new String[]{"id"}, new Object[]{id});
                re = (c != null) ? c.getNom_prenom() : "";
                break;
            case Constantes.SCR_DICTIONNAIRE:
                YvsDictionnaire d = (YvsDictionnaire) dao.loadOneByNameQueries("YvsDictionnaire.findById", new String[]{"id"}, new Object[]{id});
                re = (d != null) ? d.getLibele() : "";
                break;
            default:
                re = "";
                break;
        }
        return re;
    }

    public String giveTexteObjectif(YvsComModeleObjectif obj) {
        String re = "";
        if (obj != null) {
            switch (obj.getIndicateur()) {
                case Constantes.INDIC_CA:
                    re = "Réaliser un chiffre d'affaire de";
                    break;
                case Constantes.INDIC_MARGE:
                    re = "Réaliser une marge de";
                    break;
                case Constantes.INDIC_CREANCE:
                    re = "Recouvrer chez les partenaire les créances jusqu'a ";
                    break;
                case Constantes.INDIC_QUANTITE:
                    re = "Vendre ";
                    break;
                case Constantes.INDIC_NB_CLIENT:
                    re = "Atteindre le nombre";
                    break;
                default:
                    re = "";
                    break;
            }
        }
        return re;
    }

    public String giveTexteObjectif2(YvsComModeleObjectif obj) {
        String re = "";
        if (obj != null) {
            for (YvsComCibleObjectif c : reordonneCibles(obj)) {
                switch (c.getTableExterne()) {
                    case Constantes.SCR_ARTICLES:
                        re = "Sur le(s) produit(s) " + ((YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{c.getIdExterne()})).getRefArt() + ", ";
                        break;
                    case Constantes.SCR_CONDITIONNEMENT:
                        re = "Sur le(s) produit(s) " + ((YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findById", new String[]{"id"}, new Object[]{c.getIdExterne()})).getArticle().getRefArt() + ", ";
                        break;
                    case Constantes.SCR_CLIENTS:
                        re = "Chez le(s) client(s) " + ((YvsComClient) dao.loadOneByNameQueries("YvsComClient.findById", new String[]{"id"}, new Object[]{c.getIdExterne()})).getNom_prenom() + ", ";
                        break;
                    case Constantes.SCR_DICTIONNAIRE:
                        re = "Dans la zone " + ((YvsDictionnaire) dao.loadOneByNameQueries("YvsDictionnaire.findById", new String[]{"id"}, new Object[]{c.getIdExterne()})).getLibele();
                        break;
                    default:
                        break;
                }
            }
        }
        return re;
    }

    private List<YvsComCibleObjectif> reordonneCibles(YvsComModeleObjectif mod) {
        for (YvsComCibleObjectif c : mod.getCibles()) {
            switch (c.getTableExterne()) {
                case Constantes.SCR_CONDITIONNEMENT:
                    c.setOrdre(4);
                    break;
                case Constantes.SCR_ARTICLES:
                    c.setOrdre(3);
                    break;
                case Constantes.SCR_CLIENTS:
                    c.setOrdre(2);
                    break;
                case Constantes.SCR_DICTIONNAIRE:
                    c.setOrdre(1);
                    break;
                default:
                    break;
            }
        }
        Collections.sort(mod.getCibles(), new YvsComCibleObjectif());
        return mod.getCibles();
    }

    public void choosPeriodesObjectif(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                objectif.getPeriode().setId(id);
                loadDataAttribObjectif();
            } else {
                openDialog("dlgGesPeriode");
            }
        }
    }
}
