/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.dico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import yvs.base.produits.ManagedArticles;
import yvs.dao.Options;
import yvs.entity.param.YvsBaseTarifPointLivraison;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsDictionnaireInformations;
import yvs.entity.produits.YvsBaseArticles;
import yvs.grh.UtilGrh;

import static yvs.grh.UtilGrh.buildBeanDictionnaire;

import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "Mdico")
@SessionScoped
public class ManagedDico extends Managed<Dictionnaire, YvsDictionnaire> implements Serializable {

    private Dictionnaire dictionnaire = new Dictionnaire();
    private YvsDictionnaire selectDico = new YvsDictionnaire();
    private Dictionnaire ville = new Dictionnaire(), etat = new Dictionnaire(), secteur = new Dictionnaire();
    private List<YvsDictionnaire> dictionnaires, parents, pays, villes, secteurs;

    private TarifLieux tarif = new TarifLieux();

    private TreeNode root;
    private TreeNode selectedNode;

    private long idParent, idPays, idVille, idSecteur;
    private String tabIds, numSearch_, titre, numParent;

    private List<YvsDictionnaire> listPays;
    private List<YvsDictionnaire> listVille;

    private Dictionnaire newPays = new Dictionnaire();
    private Dictionnaire newVille = new Dictionnaire();
    private long key = 1;

    private String fusionneTo;
    private List<String> fusionnesBy;

    private List<YvsBaseArticles> articles;

    public ManagedDico() {
        fusionnesBy = new ArrayList<>();
        dictionnaires = new ArrayList<>();
        parents = new ArrayList<>();
        pays = new ArrayList<>();
        villes = new ArrayList<>();
        secteurs = new ArrayList<>();
        listPays = new ArrayList<>();
        listVille = new ArrayList<>();
        articles = new ArrayList<>();
    }

    public TarifLieux getTarif() {
        return tarif;
    }

    public void setTarif(TarifLieux tarif) {
        this.tarif = tarif;
    }

    public YvsDictionnaire getSelectDico() {
        return selectDico;
    }

    public void setSelectDico(YvsDictionnaire selectDico) {
        this.selectDico = selectDico;
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

    public long getIdPays() {
        return idPays;
    }

    public void setIdPays(long idPays) {
        this.idPays = idPays;
    }

    public long getIdVille() {
        return idVille;
    }

    public void setIdVille(long idVille) {
        this.idVille = idVille;
    }

    public long getIdSecteur() {
        return idSecteur;
    }

    public void setIdSecteur(long idSecteur) {
        this.idSecteur = idSecteur;
    }

    public long getIdParent() {
        return idParent;
    }

    public void setIdParent(long idParent) {
        this.idParent = idParent;
    }

    public String getNumParent() {
        return numParent;
    }

    public void setNumParent(String numParent) {
        this.numParent = numParent;
    }

    public Dictionnaire getVille() {
        return ville;
    }

    public void setVille(Dictionnaire ville) {
        this.ville = ville;
    }

    public Dictionnaire getEtat() {
        return etat;
    }

    public void setEtat(Dictionnaire etat) {
        this.etat = etat;
    }

    public Dictionnaire getSecteur() {
        return secteur;
    }

    public void setSecteur(Dictionnaire secteur) {
        this.secteur = secteur;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getNumSearch_() {
        return numSearch_;
    }

    public void setNumSearch_(String numSearch_) {
        this.numSearch_ = numSearch_;
    }

    public Dictionnaire getDictionnaire() {
        return dictionnaire;
    }

    public void setDictionnaire(Dictionnaire dictionnaire) {
        this.dictionnaire = dictionnaire;
    }

    public List<YvsDictionnaire> getDictionnaires() {
        return dictionnaires;
    }

    public void setDictionnaires(List<YvsDictionnaire> dictionnaires) {
        this.dictionnaires = dictionnaires;
    }

    public List<YvsDictionnaire> getParents() {
        return parents;
    }

    public void setParents(List<YvsDictionnaire> parents) {
        this.parents = parents;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public long getKey() {
        return key;
    }

    public List<YvsDictionnaire> getPays() {
        return pays;
    }

    public void setPays(List<YvsDictionnaire> pays) {
        this.pays = pays;
    }

    public List<YvsDictionnaire> getVilles() {
        return villes;
    }

    public void setVilles(List<YvsDictionnaire> villes) {
        this.villes = villes;
    }

    public List<YvsDictionnaire> getSecteurs() {
        return secteurs;
    }

    public void setSecteurs(List<YvsDictionnaire> secteurs) {
        this.secteurs = secteurs;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public List<YvsDictionnaire> getListPays() {
        return listPays;
    }

    public void setListPays(List<YvsDictionnaire> listPays) {
        this.listPays = listPays;
    }

    public List<YvsDictionnaire> getListVille() {
        return listVille;
    }

    public void setListVille(List<YvsDictionnaire> listVille) {
        this.listVille = listVille;
    }

    public Dictionnaire getNewPays() {
        return newPays;
    }

    public void setNewPays(Dictionnaire newPays) {
        this.newPays = newPays;
    }

    public Dictionnaire getNewVille() {
        return newVille;
    }

    public void setNewVille(Dictionnaire newVille) {
        this.newVille = newVille;
    }

    public List<YvsBaseArticles> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticles> articles) {
        this.articles = articles;
    }

    @Override
    public void loadAll() {
        load(true, true);
        if (dictionnaire.getParent() == null) {
            dictionnaire.setParent(new Dictionnaire());
        }
        if (selectDico != null) {
            loadParents(selectDico, selectDico.getTitre());
        }
    }

    public void load(boolean avance, boolean init) {
        String table = "YvsDictionnaire y LEFT JOIN FETCH y.parent";
        dictionnaires = paginator.executeDynamicQuery("y", "y", table, "y.libele", avance, init, (int) imax, dao);
        update("data_dictionnaire_list");
        update("data_dictionnaire");
    }

    public void loadAllDicoActif(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.actif", "actif", true, "=", "AND"));
        dictionnaires = paginator.executeDynamicQuery("YvsDictionnaire", "y.libele", avance, init, (int) imax, dao);
    }

    public void loadAll(long imax) {
        this.imax = imax;
        load(true, true);
    }

    public void loadPaysFromPageArt() {
        if (currentAgence.getSociete().getVenteOnline()) {
            loadPays();
        }
    }

    public void loadPays() {
        if (pays != null ? pays.isEmpty() : false) {
            villes.clear();
            secteurs.clear();
        }
        champ = new String[]{"titre"};
        val = new Object[]{Constantes.T_PAYS};
        pays = dao.loadNameQueries("YvsDictionnaire.findByTitre", champ, val);
    }

    public void loadVilles() {
        if (villes != null ? villes.isEmpty() : false) {
            secteurs.clear();
        }
        champ = new String[]{"titre"};
        val = new Object[]{Constantes.T_VILLES};
        villes = dao.loadNameQueries("YvsDictionnaire.findByTitre", champ, val);
    }

    public void loadSecteurs() {
        champ = new String[]{"titre"};
        val = new Object[]{Constantes.T_SECTEURS};
        secteurs = dao.loadNameQueries("YvsDictionnaire.findByTitre", champ, val);
    }

    public void selectVilleByPays(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            loadVilles(new YvsDictionnaire(id));
        }
    }

    public void loadVilles(YvsDictionnaire y) {
        champ = new String[]{"parent"};
        val = new Object[]{y};
        nameQueri = "YvsDictionnaire.findVilleOnePays";
        villes = dao.loadNameQueries(nameQueri, champ, val);
        secteurs.clear();
    }

    public void loadSecteurs(YvsDictionnaire y) {
        secteurs = dao.loadNameQueries("YvsDictionnaire.findSecteurByParent", new String[]{"parent"}, new Object[]{y});
    }

    public TreeNode buildTreeDico() {
        TreeNode r = new DefaultTreeNode(new YvsDictionnaire((long) 0, "Dictionnaires", "Items"), null);
        champ = new String[]{};
        val = new Object[]{};
        List<YvsDictionnaire> l = dao.loadNameQueries("YvsDictionnaire.findByNotParent", champ, val);
        for (YvsDictionnaire f : l) {
            TreeNode fils = new DefaultTreeNode(new YvsDictionnaire(f.getId(), f.getLibele(), f.getAbreviation(), f.getTitre()), r);
            completeTreeDico(f, fils);
        }
        return r;
    }

    private void completeTreeDico(YvsDictionnaire parent, TreeNode root) {
        champ = new String[]{"parent"};
        val = new Object[]{parent};
        List<YvsDictionnaire> l = dao.loadNameQueries("YvsDictionnaire.findAllByParent", champ, val);
        if (l != null ? !l.isEmpty() : false) {
            for (YvsDictionnaire f : l) {
                if (!f.equals(parent)) {
                    TreeNode fils = new DefaultTreeNode(new YvsDictionnaire(f.getId(), f.getLibele(), f.getAbreviation(), f.getTitre()), root);
                    completeTreeDico(f, fils);
                }
            }
        }
    }

    public void loadParents(YvsDictionnaire y, String valeur) {
        String titre = Constantes.PAYS;
        switch (valeur) {
            case Constantes.VILLES:
                titre = Constantes.PAYS;
                break;
            case Constantes.SECTEURS:
                titre = Constantes.VILLES;
                break;
            default:
                titre = Constantes.SECTEURS;
                break;
        }

        champ = new String[]{"titre"};
        val = new Object[]{titre};
        nameQueri = "YvsDictionnaire.findAllByTitre";
        if (y != null ? y.getId() > 0 : false) {
            champ = new String[]{"titre", "id"};
            val = new Object[]{titre, y.getId()};
            nameQueri = "YvsDictionnaire.findByNotId";
        }
        parents = dao.loadNameQueries(nameQueri, champ, val);
        update("txt_parent_dictionnaire");
        update("main_dictionnaire");
    }

    @Override
    public boolean controleFiche(Dictionnaire bean) {
        if (bean.getTitre() != null ? bean.getTitre().trim().isEmpty() : true) {
            getErrorMessage("Chosissez un titre");
            return false;
        }
        if (bean.getLibelle() != null ? bean.getLibelle().trim().isEmpty() : true) {
            getErrorMessage("Chosissez un libelle");
            return false;
        }
        return true;
    }

    public boolean controleFiche(TarifLieux bean, boolean msg) {
        if (bean == null) {
            return false;
        }
        if (selectDico == null) {
            if (msg) {
                getErrorMessage(message.getMessage("M_controleFiche_Text1"));
            }
            return false;
        }
        if (articles == null ? true : articles.size() <= 0) {
            if (msg) {
                getErrorMessage(message.getMessage("M_controleFiche_Text2"));
            }
            return false;
        }
//        if (bean.getLieux() != null ? bean.getLieux().getId() < 1 : true) {
//            if (msg) {
//                getErrorMessage("Chosissez un lieux");
//            }
//            return false;
//        }
        return true;
    }

    public Dictionnaire recopie(Dictionnaire y, Dictionnaire p, String Titre) {
        Dictionnaire d = new Dictionnaire();
        d.setId(y.getId());
        d.setLibelle(y.getLibelle());
        d.setParent(p);
        d.setTitre(Titre);
        d.setAbreviation(y.getAbreviation());
        d.setActif(d.isUpdate() ? y.isActif() : true);
        d.setUpdate(y.isUpdate());
        return d;
    }

    @Override
    public void populateView(Dictionnaire bean) {
        cloneObject(dictionnaire, bean);
        update("form_dictionnaire");
    }

    @Override
    public void resetFiche() {
        _resetFiche(dictionnaire);
        selectDico = null;
        selectDico = new YvsDictionnaire();
        tabIds = "";
        loadParents(selectDico, selectDico.getTitre());
        resetFicheTarif();
        update("form_dictionnaire");
    }

    public void _resetFiche(Dictionnaire dictionnaire) {
        resetFiche(dictionnaire);
        dictionnaire.setTitre(Constantes.T_PAYS);
        dictionnaire.setParent(new Dictionnaire());
        dictionnaire.setTarifs(new ArrayList<YvsBaseTarifPointLivraison>());
    }

    public void resetFicheTarif() {
        tarif = new TarifLieux();
    }

    @Override
    public boolean saveNew() {
        YvsDictionnaire y = save();
        if (y != null ? y.getId() > 0 : false) {
            update("detailDico");
        }
        return true;
    }

    public YvsDictionnaire save() {
        String action = dictionnaire.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(dictionnaire)) {
                YvsDictionnaire y = UtilGrh.buildDictionnaire(dictionnaire, currentUser, currentAgence.getSociete());
                if (dictionnaire.getId() < 1) {
                    y.setId(null);
                    y = (YvsDictionnaire) dao.save1(y);
                    dictionnaire.setId(y.getId());
                    dictionnaires.add(0, y);
                } else {
                    dao.update(y);
                    if (dictionnaires.contains(y)) {
                        dictionnaires.set(dictionnaires.indexOf(y), y);
                    }
                }
                root = buildTreeDico();
//                saveNewTarif(false);
                actionOpenOrResetAfter(this);
                succes();
                update("data_dictionnaire");
                return y;
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
        }
        return null;
    }

    public void saveNewPays() {
        try {
            Dictionnaire bean = recopie(etat, null, Constantes.T_PAYS);
            if (controleFiche(bean)) {
                YvsDictionnaire entity = UtilGrh.buildDictionnaire(bean, currentUser, currentAgence.getSociete());
                entity.setId(null);
                entity = (YvsDictionnaire) dao.save1(entity);
                bean.setId(entity.getId());
                pays.add(0, entity);
                villes.clear();
                _resetFiche(etat);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            System.err.println("Error " + ex.getMessage());
        }
    }

    public void saveNewVille(Dictionnaire pays) {
        try {
            Dictionnaire bean = recopie(ville, pays, Constantes.T_VILLES);
            if (controleFiche(bean)) {
                YvsDictionnaire entity = UtilGrh.buildDictionnaire(bean, currentUser, currentAgence.getSociete());
                entity.setId(null);
                entity = (YvsDictionnaire) dao.save1(entity);
                bean.setId(entity.getId());
                villes.add(0, entity);
                _resetFiche(ville);
                secteurs.clear();
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            System.err.println("Error " + ex.getMessage());
        }
    }

    public void saveNewVilleById(long id) {
        try {
            if (id > 0) {
                Dictionnaire pays = new Dictionnaire(id);
                Dictionnaire bean = recopie(ville, pays, Constantes.T_VILLES);
                if (controleFiche(bean)) {
                    YvsDictionnaire entity = UtilGrh.buildDictionnaire(bean, currentUser, currentAgence.getSociete());
                    entity.setId(null);
                    entity = (YvsDictionnaire) dao.save1(entity);
                    bean.setId(entity.getId());
                    villes.add(0, entity);
                    _resetFiche(ville);
                    secteurs.clear();
                    succes();
                }
            } else {
                getErrorMessage("Vous devez precisez le pays");
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            System.err.println("Error " + ex.getMessage());
        }
    }

    public void saveNewSecteur(Dictionnaire ville) {
        try {
            Dictionnaire bean = recopie(secteur, ville, Constantes.T_SECTEURS);
            if (controleFiche(bean)) {
                YvsDictionnaire en = UtilGrh.buildDictionnaire(bean, currentUser, currentAgence.getSociete());
                en.setId(null);
                en = (YvsDictionnaire) dao.save1(en);
                bean.setId(en.getId());
                secteurs.add(0, en);
                _resetFiche(secteur);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            System.err.println("Error " + ex.getMessage());
        }
    }

    public void saveNewSecteurById(long id) {
        try {
            if (id > 0) {
                Dictionnaire ville = new Dictionnaire(id);
                Dictionnaire bean = recopie(secteur, ville, Constantes.T_SECTEURS);
                if (controleFiche(bean)) {
                    YvsDictionnaire en = UtilGrh.buildDictionnaire(bean, currentUser, currentAgence.getSociete());
                    en.setId(null);
                    en = (YvsDictionnaire) dao.save1(en);
                    bean.setId(en.getId());
                    secteurs.add(0, en);
                    _resetFiche(secteur);
                    succes();
                }
            } else {
                getErrorMessage("Vous devez precisez la ville");
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            System.err.println("Error " + ex.getMessage());
        }
    }

    public void saveNewTarif(boolean msg) {
        try {
            tarif.setLieux(dictionnaire);
            if (controleFiche(tarif, msg)) {
                //vérifie l'existence de la relation dictionnaireInfo
                YvsDictionnaireInformations dico = (YvsDictionnaireInformations) dao.loadOneByNameQueries("YvsDictionnaireInformations.findOne", new String[]{"lieux", "societe"}, new Object[]{selectDico, currentAgence.getSociete()});
                if (dico == null) {
                    selectDico.setInformation(new YvsDictionnaireInformations(-1L));
                    activeLivraison(selectDico);
                    dico = selectDico.getInformation();
                }
                if (dico != null) {
//                    YvsBaseTarifPointLivraison y = UtilGrh.buildTarifLieux(tarif, currentAgence.getSociete(), currentUser);
                    YvsBaseTarifPointLivraison y;
                    for (YvsBaseArticles article : articles) {
                        y = (YvsBaseTarifPointLivraison) dao.loadOneByNameQueries("YvsBaseTarifPointLivraison.findOne", new String[]{"article", "lieux"}, new Object[]{article, dico});
                        if (y == null) {
                            y = UtilGrh.buildTarifLieux(tarif, currentAgence.getSociete(), currentUser);
                            y.setArticle(article);
                            y.setLieuxLiv(dico);
                            y.setDateSave(new Date());
                            y.setDateUpdate(new Date());
                            y = (YvsBaseTarifPointLivraison) dao.save1(y);
//                            if (tarif.getId() < 1) {
//                                y = (YvsBaseTarifPointLivraison) dao.save1(y);
//                                tarif.setId(y.getId());
//                            } else {
//                                dao.update(y);
//                            }
//                            int idx = dictionnaire.getTarifs().indexOf(y);
//                            if (idx > -1) {
//                                dictionnaire.getTarifs().set(idx, y);
//                            } else {
//                                dictionnaire.getTarifs().add(y);
//                            }
                        } else {
                            y.setDelaiForLivraison(tarif.getDelaiForLivraison());
                            y.setDelaiForRetrait(tarif.getDelaiForRetrait());
                            y.setDelaiRetour(tarif.getDelaiRetour());
                            y.setFraisLivraison(tarif.getFraisLivraison());
                            y.setLivraisonDomicile(tarif.isLivraisonDomicile());
                            y.setDateUpdate(new Date());
                            dao.update(y);
                        }
                    }
                    if (msg) {
                        succes();
                    }
                } else {
                    if (msg) {
                        getErrorMessage(message.getMessage("M_saveNewTarif_Text1"), message.getMessage("M_saveNewTarif_Text2"));
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            getException("Error saveNewTarif : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                YvsDictionnaire bean;
                for (Long ids : l) {
                    if (ids >= 0) {
                        bean = dictionnaires.get(ids.intValue());
                        bean.setAuthor(currentUser);
                        bean.setDateUpdate(new Date());
                        dao.delete(bean);
                        if (bean.getId() == dictionnaire.getId()) {
                            resetFiche();
                        }
                    }
                }
                dictionnaires.removeAll(l);
                succes();
                update("data_dictionnaire");
            }
            tabIds = "";
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible ! Cet élement est déjà utiliser ");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsDictionnaire y) {
        selectDico = y;
    }

    public void deleteBean_() {
        try {
            if (selectDico != null) {
                dao.delete(selectDico);
                dictionnaires.remove(selectDico);
                if (selectDico.getId() == dictionnaire.getId()) {
                    resetFiche();
                }
                succes();
                update("data_dictionnaire");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteTarif(YvsBaseTarifPointLivraison y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                dao.delete(y);
                dictionnaire.getTarifs().remove(y);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void simpleSelectOnView(YvsDictionnaire y) {
        selectDico = y;
        populateView(UtilGrh.buildBeanDictionnaire(y));
    }

    private void selectOnView(YvsDictionnaire y) {
        selectDico = y;
        populateView(UtilGrh.buildBeanDictionnaire(y));
        loadParents(selectDico, selectDico.getTitre());
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsDictionnaire bean = (YvsDictionnaire) ev.getObject();
            tabIds = dictionnaires.indexOf(bean) + "";
            selectOnView(bean);
            update("data_dictionnaire_list");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewTarif(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBaseTarifPointLivraison bean = (YvsBaseTarifPointLivraison) ev.getObject();
            tarif = UtilGrh.buildBeanTarifLieux(bean);
        }
    }

    public void unLoadOnViewTarif(UnselectEvent ev) {
        resetFicheTarif();
    }

    public void onNodeSelect(NodeSelectEvent ev) {
        YvsDictionnaire f = (YvsDictionnaire) ev.getTreeNode().getData();
        selectOnView(f);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        load(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        load(true, true);
    }

    public void _choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllDicoActif(true, true);
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void chooseTitre() {
        loadParents(selectDico, dictionnaire.getTitre());
    }

    public void chooseParent() {
        if (dictionnaire.getParent() != null ? dictionnaire.getParent().getId() > 0 : false) {
            int idx = parents.indexOf(new YvsDictionnaire(dictionnaire.getParent().getId()));
            if (idx > -1) {
                dictionnaire.setParent(UtilGrh.buildBeanDictionnaire(parents.get(idx)));
            }
        }
    }

    public Dictionnaire choosePays(long id) {
        getVilles().clear();
        getSecteurs().clear();
        if (id > 0) {
            YvsDictionnaire y = getPays().get(getPays().indexOf(new YvsDictionnaire(id)));
            Dictionnaire d = UtilGrh.buildBeanDictionnaire(y);
            loadVilles(y);
//            villes = new ArrayList<>(y.getFils());
//            Collections.sort(villes);
            return d;
        } else {
            if (id < 0) {
                openDialog("dlgAddPays");
            }
        }
        return new Dictionnaire();
    }

    public Dictionnaire chooseVille(Dictionnaire pays, long id) {
        getSecteurs().clear();
        if (id > 0) {
            int idx = getVilles().indexOf(new YvsDictionnaire(id));
            if (idx >= 0) {
                YvsDictionnaire y = getVilles().get(idx);
                Dictionnaire d = UtilGrh.buildBeanDictionnaire(y);
//                loadSecteurs(y);
                secteurs = new ArrayList<>(y.getFils());
                Collections.sort(secteurs);
                return d;
            }
        } else {
            if (id < 0) {
                if (pays != null ? pays.getId() > 0 : false) {
                    openDialog("dlgAddVille");
                } else {
                    getErrorMessage("Vous devez selectionner le pays");
                }
            }
        }
        return new Dictionnaire();
    }

    public Dictionnaire chooseSecteur(Dictionnaire ville, long id) {
        if (id > 0) {
            int idx = secteurs.indexOf(new YvsDictionnaire(id));
            if (idx >= 0) {
                YvsDictionnaire y = getSecteurs().get(idx);
                return UtilGrh.buildBeanDictionnaire(y);
            }
        } else {
            if (id < 0) {
                if (ville != null ? ville.getId() > 0 : false) {
                    openDialog("dlgAddSecteur");
                } else {
                    getErrorMessage("Vous devez selectionner la ville");
                }
            }
        }
        return new Dictionnaire();
    }

    public void activeDictionnaire(YvsDictionnaire bean) {
        if (bean != null ? bean.getId() > 0 : false) {
            bean.setActif(!bean.getActif());
            String rq = "UPDATE yvs_dictionnaire SET actif=" + bean.getActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            dictionnaires.set(dictionnaires.indexOf(bean), bean);
            succes();
        }
    }

    public void activeLivraison(YvsDictionnaire bean) {
        if (bean != null ? (bean.getId() > 0 ? bean.getInformation() != null : false) : false) {
            bean.getInformation().setActiveLivraison(!bean.getInformation().getActiveLivraison());
            bean.getInformation().setDateUpdate(new Date());
            bean.getInformation().setAuthor(currentUser);
            bean.getInformation().setLieux(bean);
            bean.getInformation().setSociete(currentAgence.getSociete());
            if (bean.getInformation().getId() > 0) {
                dao.update(bean.getInformation());
            } else {
                bean.getInformation().setId(null);
                bean.getInformation().setDateSave(new Date());
                bean.setInformation((YvsDictionnaireInformations) dao.save1(bean.getInformation()));
            }
        }
    }

    public void addParamParent(boolean id) {
        ParametreRequete p = new ParametreRequete("y.parent", "parent", null, "=", "AND");
        if (!id) {
            if (numParent != null ? numParent.trim().length() > 0 : false) {
                p = new ParametreRequete(null, "parent", numParent.toUpperCase() + "%", "LIKE", "AND");
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.parent.abreviation)", "parent", numParent.toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.parent.libele)", "parent", numParent.toUpperCase() + "%", "LIKE", "OR"));
            }
        } else {
            if (idParent > 0) {
                p = new ParametreRequete("y.parent", "parent", new YvsDictionnaire(idParent), "=", "AND");
            }
        }
        paginator.addParam(p);
        load(true, true);
    }

    public void addParamPays() {
        ParametreRequete p = new ParametreRequete("y.parent", "parent", null, "=", "AND");
        if (idPays > 0) {
            p = new ParametreRequete("y.parent", "parent", new YvsDictionnaire(idPays), "=", "AND");
        }
        choosePays(idPays);
        paginator.addParam(p);
        load(true, true);
    }

    public void addParamVille() {
        ParametreRequete p = new ParametreRequete("y.parent", "parent", null, "=", "AND");
        if (idVille > 0) {
            p = new ParametreRequete("y.parent", "parent", new YvsDictionnaire(idVille), "=", "AND");
        }
        chooseVille(new Dictionnaire(idPays), idVille);
        paginator.addParam(p);
        load(true, true);
    }

    public void addParamSecteur() {
        ParametreRequete p = new ParametreRequete("y.parent", "parent", null, "=", "AND");
        if (idSecteur > 0) {
            p = new ParametreRequete("y.parent", "parent", new YvsDictionnaire(idSecteur), "=", "AND");
        }
        paginator.addParam(p);
        load(true, true);
    }

    public void searchDicos() {
        ParametreRequete p = new ParametreRequete("y.abreviation", "reference", null, "LIKE", "OR");
        if (numSearch_ != null ? numSearch_.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", numSearch_.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.abreviation)", "reference", numSearch_.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.libele)", "reference", numSearch_.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        load(true, true);
    }

    public void chooseTitreSearch(ValueChangeEvent ev) {
        titre = (String) ev.getNewValue();
        ParametreRequete p = new ParametreRequete("y.titre", "titre", titre);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        load(true, true);
    }

    public void updateContent() {

    }

    public void refresh() {
        update("detailDico");
    }

    public void choixTitre(ValueChangeEvent ev) {
        String titre = (String) ev.getNewValue();
    }

    public void loadDicoVille() {
        champ = new String[]{"titre"};
        val = new Object[]{Constantes.T_VILLES};
        listVille = dao.loadNameQueries("YvsDictionnaire.findByTitre", champ, val);
    }

    public void loadDicoPays() {
        champ = new String[]{"titre"};
        val = new Object[]{Constantes.T_PAYS};
        listPays = dao.loadNameQueries("YvsDictionnaire.findByTitre", champ, val);
    }

    public void loadDicoPaysAndVille() {
        champ = new String[]{"titre", "titre2"};
        val = new Object[]{Constantes.T_VILLES, Constantes.T_PAYS};
        listPays = dao.loadNameQueries("YvsDictionnaire.findLibele", champ, val);
    }

    public void createPays() {
        if ((newPays != null) ? !newPays.getLibelle().trim().equals("") : false) {
            YvsDictionnaire d = new YvsDictionnaire();
            d.setTitre(Constantes.T_PAYS);
            d.setLibele(newPays.getLibelle());
            d.setSociete(currentAgence.getSociete());
            d.setSupp(false);
            d.setActif(true);
            d.setAuthor(currentUser);
            d.setAbreviation(newPays.getAbreviation());
            d.setId(null);
            d = (YvsDictionnaire) dao.save1(d);
            listPays.add(0, d);
            newPays = new Dictionnaire();
            succes();
        }
    }

    public void createVille() {
        if ((newVille != null) ? !newVille.getLibelle().trim().equals("") : false) {
            if (newPays.getId() > 0) {
                YvsDictionnaire d = new YvsDictionnaire();
                d.setTitre(Constantes.T_VILLES);
                d.setLibele(newVille.getLibelle());
                d.setParent(new YvsDictionnaire(newPays.getId()));
                d.setSociete(currentAgence.getSociete());
                d.setSupp(false);
                d.setActif(true);
                d.setAbreviation(newVille.getAbreviation());
                d.setAuthor(currentUser);
                d.setId(null);
                d = (YvsDictionnaire) dao.save1(d);
                newVille = new Dictionnaire();
                listVille.add(0, d);
                succes();
            } else {
                getErrorMessage("Vous devez indiquer le pays !");
            }
        }
    }

    public Dictionnaire findZone(String num, boolean open) {
        Dictionnaire a = new Dictionnaire();
        a.setAbreviation(num);
        a.setError(true);
        ParametreRequete p = new ParametreRequete("y.abreviation", "reference", null, "LIKE", "OR");
        if (num != null ? num.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", num.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.abreviation)", "reference", num.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.libele)", "reference", num.toUpperCase() + "%", "LIKE", "OR"));

        }
        paginator.addParam(p);
        load(true, true);
        a = findZoneResult(open);
        if (a != null ? a.getId() < 1 : true) {
            a.setAbreviation(num);
            a.setError(true);
        }
        return a;
    }

    public Dictionnaire findZoneActif(String titre, String num, boolean open) {
        Dictionnaire a = new Dictionnaire();
        a.setAbreviation(num);
        a.setError(true);
        ParametreRequete p = new ParametreRequete("y.abreviation", "reference", null, "LIKE", "AND");
        if (num != null ? num.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", num.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.abreviation)", "reference", num.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.libele)", "reference", num.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        p = new ParametreRequete("y.titre", "titre", (titre != null) ? titre : (titre.trim().isEmpty()) ? null : titre, "=", "AND");
        paginator.addParam(p);
        loadAllDicoActif(true, true);
        a = findZoneResult(open);
        if (a != null ? a.getId() < 1 : true) {
            a.setAbreviation(num);
            a.setError(true);
        }
        return a;
    }

    private Dictionnaire findZoneResult(boolean open) {
        Dictionnaire a = new Dictionnaire();
        if (dictionnaires != null ? !dictionnaires.isEmpty() : false) {
            if (dictionnaires.size() > 1) {
                if (open) {
                    openDialog("dlgListZone");
                }
                a.setList(true);
            } else {
                a = (!dictionnaires.isEmpty()) ? buildBeanDictionnaire(dictionnaires.get(0)) : new Dictionnaire();
                a.setSelect(true);
            }
            a.setError(false);
        }
        return a;
    }

    public void initZones(Dictionnaire a) {
        if (a == null) {
            a = new Dictionnaire();
        }
        paginator.addParam(new ParametreRequete("y.abreviation", "reference", null));
        load(true, true);
        a.setSelect(false);
        a.setList(true);
    }

    public void initZones(Dictionnaire a, String titre) {
        if (a == null) {
            a = new Dictionnaire();
        }
        paginator.addParam(new ParametreRequete("y.abreviation", "reference", null));
        paginator.addParam(new ParametreRequete("y.titre", "titre", (titre != null) ? (titre.trim().length() > 0) ? titre : null : null, "=", "AND"));
        loadAllDicoActif(true, true);
        a.setSelect(false);
        a.setList(true);
    }

    public void onLoadInformation(YvsDictionnaire entity) {
        if (entity != null) {
            entity.setInformation((YvsDictionnaireInformations) dao.loadOneByNameQueries("YvsDictionnaireInformations.findOne", new String[]{"societe", "lieux"}, new Object[]{currentAgence.getSociete(), entity}));
            if (entity.getInformation() == null) {
                entity.setInformation(new YvsDictionnaireInformations());
            }
        }
    }

    public void fusionner(boolean fusionne) {
        try {
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                long newValue = dictionnaires.get(ids.get(0)).getId();
                if (!fusionne) {
                    if (!autoriser("base_user_fusion")) {
                        openNotAcces();
                        return;
                    }
                    String oldValue = "0";
                    for (int i : ids) {
                        if (dictionnaires.get(i).getId() != newValue) {
                            oldValue += "," + dictionnaires.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_dictionnaire", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                dictionnaires.remove(new YvsDictionnaire(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = dictionnaires.get(idx).getLibele();
                    } else {
                        YvsDictionnaire c = (YvsDictionnaire) dao.loadOneByNameQueries("YvsDictionnaire.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? c.getId() > 0 : false) {
                            fusionneTo = c.getLibele();
                        }
                    }
                    YvsDictionnaire c;
                    for (int i : ids) {
                        long oldValue = dictionnaires.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(dictionnaires.get(i).getLibele());
                            }
                        } else {
                            c = (YvsDictionnaire) dao.loadOneByNameQueries("YvsDictionnaire.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? c.getId() > 0 : false) {
                                fusionnesBy.add(c.getLibele());
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

    public void chooseVilleLivraison(ValueChangeEvent ev) {
        if (ev != null) {
            Long id = (Long) ev.getNewValue();
            secteurs.clear();
            villes = dao.loadNameQueries("YvsDictionnaireInformations.findByBarent", new String[]{"societe", "parent"}, new Object[]{currentAgence.getSociete(), new YvsDictionnaire(id)});
        }
    }

    public void chooseSecteurLivraison(ValueChangeEvent ev) {
        Long id = (Long) ev.getNewValue();
        secteurs = dao.loadNameQueries("YvsDictionnaireInformations.findByBarent", new String[]{"societe", "parent"}, new Object[]{currentAgence.getSociete(), new YvsDictionnaire(id)});
    }

    /*Filtrage article*/
    private String selection;

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public void loadArticle(boolean avancer, boolean init) {
        articles.clear();
        selection = null;
        ManagedArticles service = (ManagedArticles) giveManagedBean("Marticle");
        if (service != null) {
            service.loadActifArticle(avancer, init);
            buildInfosLivraison();
        }
    }

    public void gotoPagePaginator(boolean avancer, boolean init) {
        ManagedArticles service = (ManagedArticles) giveManagedBean("Marticle");
        if (service != null) {
            service.gotoPagePaginator();
            buildInfosLivraison();
        }
    }

    public void choosePaginatorArticle(ValueChangeEvent ev) {
        ManagedArticles service = (ManagedArticles) giveManagedBean("Marticle");
        if (service != null) {
            service._choosePaginator(ev);
            buildInfosLivraison();
        }
    }

    public void findArticle() {
        ManagedArticles service = (ManagedArticles) giveManagedBean("Marticle");
        if (service != null) {
            service.searchArticleActif(null, service.getSearchArticle(), false);
            buildInfosLivraison();
        }
    }

    public void addParamGroupe() {
        ManagedArticles service = (ManagedArticles) giveManagedBean("Marticle");
        if (service != null) {
            service._addParamGroupe();
            buildInfosLivraison();
        }
    }

    public void addParamFamille() {
        ManagedArticles service = (ManagedArticles) giveManagedBean("Marticle");
        if (service != null) {
            service._addParamFamille();
            buildInfosLivraison();
        }
    }

    public void addParamCategorie() {
        ManagedArticles service = (ManagedArticles) giveManagedBean("Marticle");
        if (service != null) {
            service._addParamCategorie();
            buildInfosLivraison();
        }
    }

    public void buildInfosLivraison() {
        ManagedArticles service = (ManagedArticles) giveManagedBean("Marticle");
        if (service != null) {
            for (YvsBaseArticles art : service.getArticlesResult()) {
                art.setInfoLiv((YvsBaseTarifPointLivraison) dao.loadOneByNameQueries("YvsBaseTarifPointLivraison.findOneByVille", new String[]{"ville", "article"}, new Object[]{selectDico, art}));
            }
        }
    }

    public void applySelection() {
        if (articles != null) {
            StringBuilder sb = new StringBuilder();
            if (articles.size() > 0) {
                sb.append(articles.get(0).getDesignation());
            }
            if (articles.size() > 1) {
                sb.append(", ").append(articles.get(1).getDesignation());
            }
            if (articles.size() > 2) {
                sb.append(", ").append(articles.get(2).getDesignation());
            }
            if (articles.size() > 3) {
                sb.append(", ").append(articles.get(3).getDesignation());
            }
            if (articles.size() > 4) {
                sb.append(", ").append(articles.get(4).getDesignation());
            }
            if (articles.size() > 5) {
                sb.append(", ").append(articles.get(5).getDesignation());
            }
            if (articles.size() > 6) {
                sb.append(", + ").append(articles.size() - 5).append(" autres sélectionnées");
            }
            selection = sb.toString();
        }
    }

    public void updateInfoLivOneArt(YvsBaseArticles art) {
        articles.clear();
        articles.add(art);
        selection = art.getDesignation();
        update("form_info_one_liv");
        openDialog("dlgInfoLivOne");
    }
}
