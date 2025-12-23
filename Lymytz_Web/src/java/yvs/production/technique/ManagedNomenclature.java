/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.technique;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.base.produits.ManagedArticles;
import yvs.base.produits.ManagedUniteMesure;
import yvs.base.produits.UniteMesure;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseCodeAcces;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.production.base.YvsProdComposantNomenclature;
import yvs.entity.production.base.YvsProdNomenclature;
import yvs.entity.production.base.YvsProdNomenclatureSite;
import yvs.entity.production.base.YvsProdOperationsGamme;
import yvs.entity.production.base.YvsProdSiteProduction;
import yvs.entity.production.pilotage.YvsProdComposantOp;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.init.Initialisation;
import yvs.production.UtilProd;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedNomenclature extends Managed<Nomenclature, YvsProdNomenclature> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ManagedNomenclature.class.getName());

    @ManagedProperty(value = "#{nomenclature}")
    private Nomenclature nomenclature;
    private Nomenclature otherNom = new Nomenclature();
    private List<YvsProdNomenclature> nomenclatures;
    private List<YvsProdNomenclature> listOthers;
    private YvsProdNomenclature selectNomenclature;
    private TreeNode root = new DefaultTreeNode();
    private YvsBaseCodeAcces codeAcces;
    private boolean date_up = false;
    private Date dateDebut_ = new Date(), dateFin_ = new Date();

    private ComposantNomenclature composant = new ComposantNomenclature();
    private ComposantNomenclature composantLie = new ComposantNomenclature();
    private YvsProdComposantNomenclature selectComposant;

    int droit = 0;
    private String tabIds, tabIds_composant;
    private boolean compose = true;

    private boolean dateSeacrh;
    private Boolean actifSearch, forCondSearch, masqueSearch;
    private String numSearch, artSearch, categorieSearch, composantSearch;
    private int siteSearch;
    private long familleSearch;
    private Date dateDebutSearch, dateFinSearch;

    private boolean notRappelDel = false;
    private boolean addComposant = false;

    private String fusionneTo;
    private List<String> fusionnesBy;
    private String fusionneToComposant;
    private List<String> fusionnesByComposant;

    private boolean displaySousProd;

    public ManagedNomenclature() {
        nomenclatures = new ArrayList<>();
        listOthers = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
        fusionnesByComposant = new ArrayList<>();
    }

    public long getFamilleSearch() {
        return familleSearch;
    }

    public void setFamilleSearch(long familleSearch) {
        this.familleSearch = familleSearch;
    }

    public boolean isDate_up() {
        return date_up;
    }

    public void setDate_up(boolean date_up) {
        this.date_up = date_up;
    }

    public Date getDateDebut_() {
        return dateDebut_;
    }

    public void setDateDebut_(Date dateDebut_) {
        this.dateDebut_ = dateDebut_;
    }

    public Date getDateFin_() {
        return dateFin_;
    }

    public void setDateFin_(Date dateFin_) {
        this.dateFin_ = dateFin_;
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

    public String getFusionneToComposant() {
        return fusionneToComposant;
    }

    public void setFusionneToComposant(String fusionneToComposant) {
        this.fusionneToComposant = fusionneToComposant;
    }

    public List<String> getFusionnesByComposant() {
        return fusionnesByComposant;
    }

    public void setFusionnesByComposant(List<String> fusionnesByComposant) {
        this.fusionnesByComposant = fusionnesByComposant;
    }

    public String getCategorieSearch() {
        return categorieSearch;
    }

    public void setCategorieSearch(String categorieSearch) {
        this.categorieSearch = categorieSearch;
    }

    public int getSiteSearch() {
        return siteSearch;
    }

    public void setSiteSearch(int siteSearch) {
        this.siteSearch = siteSearch;
    }

    public YvsBaseCodeAcces getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(YvsBaseCodeAcces codeAcces) {
        this.codeAcces = codeAcces;
    }

    public Boolean getForCondSearch() {
        return forCondSearch;
    }

    public void setForCondSearch(Boolean forCondSearch) {
        this.forCondSearch = forCondSearch;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public boolean isDateSeacrh() {
        return dateSeacrh;
    }

    public void setDateSeacrh(boolean dateSeacrh) {
        this.dateSeacrh = dateSeacrh;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public String getArtSearch() {
        return artSearch;
    }

    public void setArtSearch(String artSearch) {
        this.artSearch = artSearch;
    }

    public Date getDateDebutSearch() {
        return dateDebutSearch;
    }

    public void setDateDebutSearch(Date dateDebutSearch) {
        this.dateDebutSearch = dateDebutSearch;
    }

    public Date getDateFinSearch() {
        return dateFinSearch;
    }

    public void setDateFinSearch(Date dateFinSearch) {
        this.dateFinSearch = dateFinSearch;
    }

    public boolean isCompose() {
        return compose;
    }

    public void setCompose(boolean compose) {
        this.compose = compose;
    }

    public Nomenclature getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(Nomenclature nomenclature) {
        this.nomenclature = nomenclature;
    }

    public List<YvsProdNomenclature> getNomenclatures() {
        return nomenclatures;
    }

    public void setNomenclatures(List<YvsProdNomenclature> nomenclatures) {
        this.nomenclatures = nomenclatures;
    }

    public YvsProdNomenclature getSelectNomenclature() {
        return selectNomenclature;
    }

    public void setSelectNomenclature(YvsProdNomenclature selectNomenclature) {
        this.selectNomenclature = selectNomenclature;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public ComposantNomenclature getComposant() {
        return composant;
    }

    public void setComposant(ComposantNomenclature composant) {
        this.composant = composant;
    }

    public YvsProdComposantNomenclature getSelectComposant() {
        return selectComposant;
    }

    public void setSelectComposant(YvsProdComposantNomenclature selectComposant) {
        this.selectComposant = selectComposant;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getTabIds_composant() {
        return tabIds_composant;
    }

    public void setTabIds_composant(String tabIds_composant) {
        this.tabIds_composant = tabIds_composant;
    }

    public ComposantNomenclature getComposantLie() {
        return composantLie;
    }

    public void setComposantLie(ComposantNomenclature composantLie) {
        this.composantLie = composantLie;
    }

    public String getComposantSearch() {
        return composantSearch;
    }

    public void setComposantSearch(String composantSearch) {
        this.composantSearch = composantSearch;
    }

    public Nomenclature getOtherNom() {
        return otherNom;
    }

    public void setOtherNom(Nomenclature otherNom) {
        this.otherNom = otherNom;
    }

    public List<YvsProdNomenclature> getListOthers() {
        return listOthers;
    }

    public void setListOthers(List<YvsProdNomenclature> listOthers) {
        this.listOthers = listOthers;
    }

    public boolean isNotRappelDel() {
        return notRappelDel;
    }

    public void setNotRappelDel(boolean notRappelDel) {
        this.notRappelDel = notRappelDel;
    }

    public boolean isAddComposant() {
        return addComposant;
    }

    public void setAddComposant(boolean addComposant) {
        this.addComposant = addComposant;
    }

    public Boolean getMasqueSearch() {
        return masqueSearch;
    }

    public void setMasqueSearch(Boolean masqueSearch) {
        this.masqueSearch = masqueSearch;
    }

    public boolean isDisplaySousProd() {
        return displaySousProd;
    }

    public void setDisplaySousProd(boolean displaySousProd) {
        this.displaySousProd = displaySousProd;
    }

    @Override
    public void loadAll() {
        droit = buildDroit();
        addParamMasquer_(false);
    }

    private int buildDroit() {
        return autoriser("prod_nomenc_load_all") ? 1 : 2;
    }

    public void loadAll(boolean avance, boolean init) {
        switch (droit) {
            case 1:
                paginator.addParam(new ParametreRequete("y.article.famille.societe", "societe", currentAgence.getSociete(), "=", "AND"));
                nomenclatures = paginator.executeDynamicQuery("YvsProdNomenclature", "y.article.refArt , y.niveau", avance, init, (int) imax, dao);
                break;
            case 2:
                if (currentCreneauEquipe != null) {
                    paginator.addParam(new ParametreRequete("y.site", "site", currentCreneauEquipe.getSite(), "=", "AND"));
                    nomenclatures = paginator.executeDynamicQuery("DISTINCT y.nomenclature", "DISTINCT y.nomenclature", "YvsProdNomenclatureSite y", "y.nomenclature.article.refArt ASC", avance, init, (int) imax, dao);
                }
                break;
        }
        if (nomenclatures.size() == 1) {
            selectNomenclature = nomenclatures.get(0);
            onSelectObject(selectNomenclature);
            displaySousProd = true;
            displayCompSub_(true);
            execute("collapseForm('nomenclature')");
        } else {
            execute("collapseList('nomenclature')");
        }
    }

    public void init(boolean next) {
        loadAll(next, false);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsProdNomenclature> re = paginator.parcoursDynamicData("YvsProdNomenclature", "y", "y.article.refArt", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
            displaySousProd = true;
            displayCompSub_(true);
        }
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAll(true, true);
    }

    @Override
    public boolean controleFiche(Nomenclature bean) {
        if (bean.getCompose() == null || bean.getCompose().getId() < 1) {
            getErrorMessage("Vous devez choisir un article");
            return false;
        }
        if (bean.getQuantite() <= 0) {
            getErrorMessage("Vous devez précisez la quantitée");
            return false;
        }
        if (bean.getNiveau() < 1) {
            getErrorMessage("Vous devez précisez un niveau");
            return false;
        }
        if (bean.getUnite() == null || bean.getUnite().getId() < 1) {
            getErrorMessage("Vous devez précisez l'unité de mesure");
            return false;
        }
        champ = new String[]{"niveau", "article"};
        val = new Object[]{bean.getNiveau(), new YvsBaseArticles(bean.getCompose().getId())};
        YvsProdNomenclature p = (YvsProdNomenclature) dao.loadOneByNameQueries("YvsProdNomenclature.findByArticleNiveau", champ, val);
        if (p != null && !p.getId().equals(bean.getId())) {
            getErrorMessage("Vous avez deja enregistré une nomenclature de cette article avec ce niveau");
            return false;
        }
        champ = new String[]{"reference", "societe"};
        val = new Object[]{bean.getReference(), currentAgence.getSociete()};
        p = (YvsProdNomenclature) dao.loadOneByNameQueries("YvsProdNomenclature.findByReference", champ, val);
        if (p != null && !p.getId().equals(bean.getId())) {
            getErrorMessage("Vous avez deja enregistré une nomenclature avec cette reference");
            return false;
        }
        return true;
    }

    private boolean controleFicheComposant(ComposantNomenclature bean) {
        if (bean.getNomenclature() == null || bean.getNomenclature().getId() < 1) {
            getErrorMessage("votre nomenclature n'est pas référencé");
            return false;
        }
        if (bean.getArticle() == null || bean.getArticle().getId() < 1) {
            getErrorMessage("veuillez choisir l'article");
            return false;
        }
        if (bean.getQuantite() <= 0) {
            getErrorMessage("vous devez indiquer une quantité valide pour ce composant");
            return false;
        }
        if (bean.getOrdre() < 0) {
            getErrorMessage("vous devez indiquer un ordre positif");
            return false;
        }
        if (bean.getUnite() == null || bean.getUnite().getId() < 1) {
            getErrorMessage("vous devez indiquer une quantité valide pour ce composant");
            return false;
        }
        if (nomenclature.isQteLieAuxComposant() && bean.getCoefficient() == 0) {
            getErrorMessage("Vous devez choisir une unité de conditionnement qui a une equivalence");
            return false;
        }
        if (bean.isComposantLie()) {
            if (composantLie.getId() <= 0) {
                getErrorMessage("Selectionnez le composant de liaison !");
                return false;
            }
        }
        YvsProdComposantNomenclature y = (YvsProdComposantNomenclature) dao.loadOneByNameQueries("YvsProdComposantNomenclature.findByOne", new String[]{"article", "unite", "nomenclature"}, new Object[]{new YvsBaseArticles(bean.getArticle().getId()), new YvsBaseConditionnement(bean.getUnite().getId()), new YvsProdNomenclature(nomenclature.getId())});
        if (y != null && (y.getId() > 0 && !y.getId().equals(bean.getId()))) {
            getErrorMessage("Vous avez deja ajouté ce composant", "Il est peut-être désactivé.");
            return false;
        }
        if (bean.getId() < 1) {
            bean.setInsideCout(bean.getTypeComposant().equals(Constantes.PROD_OP_TYPE_COMPOSANT_NORMAL));
        }
        return true;
    }

    @Override
    public Nomenclature recopieView() {
        nomenclature.setQuantite(nomenclature.isQteLieAuxComposant() ? 0.0 : nomenclature.getQuantite());
        return nomenclature;
    }

    @Override
    public void populateView(Nomenclature bean) {
        cloneObject(nomenclature, bean);
        nomenclature.setComposants(dao.loadNameQueries("YvsProdComposantNomenclature.findByNomenclature", new String[]{"nomenclature"}, new Object[]{new YvsProdNomenclature(nomenclature.getId())}));
        nomenclature.setVal_total(valeurTotal());
    }

    private double valeurTotal() {
        Double val_total = 0.0;
        Double pr;
        Date dateReference = new Date(); // Optimisation: réutilisation de l'objet Date
        for (YvsProdComposantNomenclature compo : nomenclature.getComposants()) {
            pr = dao.getPr(compo.getArticle().getId(), 0, 0L, dateReference, compo.getUnite().getId());
            pr = pr > 0 ? pr : compo.getUnite().getPrixAchat();
            compo.setPr(pr);
            compo.setValeur(compo.getQuantite() * compo.getPr());
            if (compo.getActif() && compo.getInsideCout()) {
                val_total += compo.getValeur();
            }
        }
        return val_total;
    }

    public void populateViewComposant(ComposantNomenclature bean) {
        cloneObject(composant, bean);
        if (nomenclature.isQteLieAuxComposant() && composant.getCoefficient() == 0) {
            chooseUniteComposant();
        }
    }

    @Override
    public void resetFiche() {
        resetFiche(nomenclature);
        nomenclature.setComposants(new ArrayList<YvsProdComposantNomenclature>());
        nomenclature.setCompose(new Articles());
        nomenclature.setUnite(new Conditionnement());
        nomenclature.setNiveau(1);
        nomenclature.setQuantite(0.0);
        nomenclature.setPrincipal(false);
        nomenclature.setQteLieAuxComposant(false);
        nomenclature.getSites().clear();
        nomenclature.setMasquer(false);
        nomenclature.setForConditionnement(false);
        selectNomenclature = null;
        tabIds = "";
        resetFicheComposant();
        update("chk_principal_nomenclature");
        update("blog_form_nomenclature");
    }

    public void resetFicheComposant() {
        composant = new ComposantNomenclature();
        selectComposant = null;
        tabIds_composant = "";
        composant.setOrdre(nomenclature.getComposants().size() + 1);
    }

    @Override
    public boolean saveNew() {
        String action = nomenclature.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(nomenclature)) {
                YvsProdNomenclature y = UtilProd.buildBeanNomenclature(nomenclature, currentUser);
                if (y.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsProdNomenclature) dao.save1(y);
                    nomenclature.setId(y.getId());
                }
                int idx = nomenclatures.indexOf(y);
                if (idx > -1) {
                    nomenclatures.set(idx, y);
                } else {
                    nomenclatures.add(0, y);
                }
                succes();
                actionOpenOrResetAfter(this);
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error " + action, ex);
            return false;
        }
    }

    public void updateComposantNomenclature(CellEditEvent ev) {
        if (ev != null) {
            int row = ev.getRowIndex();
            if (row >= 0) {
                YvsProdComposantNomenclature co = nomenclature.getComposants().get(row);
                if (co != null && co.getId() > 0) {
                    co.setDateUpdate(new Date());
                    co.setAuthor(currentUser);
                    dao.update(co);
                    if (nomenclature.isQteLieAuxComposant()) {
                        int idx1 = nomenclatures.indexOf(co.getNomenclature());
                        if (idx1 >= 0) {
                            nomenclature.setQuantite(giveQteComposant());
                            YvsProdNomenclature m = nomenclatures.get(idx1);
                            m.setQuantite(nomenclature.getQuantite());
                            m.setAuthor(currentUser);
                            m.setDateUpdate(new Date());
                            dao.update(m);
                            update("group_qte_nomenclature");
                        }
                    }
                }
            }
        }
    }

    public boolean saveNewComposant() {
        long start = System.nanoTime();
        String action = composant.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (nomenclature.getId() <= 0) {
                saveNew();
            }
            composant.setNomenclature(nomenclature);
            if (controleFicheComposant(composant)) {
                YvsProdComposantNomenclature y = UtilProd.buildBeanNomenclature(composant, currentUser);
                if (composantLie.getId() > 0 && composant.isComposantLie()) {
                    y.setComposantLie(new YvsProdComposantNomenclature(composantLie.getId()));
                } else {
                    y.setComposantLie(null);
                }
                if (y.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsProdComposantNomenclature) dao.save1(y);
                }
                int idx = nomenclature.getComposants().indexOf(y);
                if (idx > -1) {
                    nomenclature.getComposants().set(idx, y);
                } else {
                    if (composant.getOrdre() > nomenclature.getComposants().size() - 1) {
                        nomenclature.getComposants().add(y);
                    } else {
                        try {
                            nomenclature.getComposants().add(composant.getOrdre(), y);
                        } catch (Exception ex) {
                            nomenclature.getComposants().add(0, y);
                            composant.setOrdre(nomenclature.getComposants().size() + 1);
                        }
                    }
                }
                if (nomenclature.isQteLieAuxComposant()) {
                    int idx1 = nomenclatures.indexOf(new YvsProdNomenclature(composant.getNomenclature().getId()));
                    if (idx1 >= 0) {
                        nomenclature.setQuantite(giveQteComposant());
                        YvsProdNomenclature m = nomenclatures.get(idx1);
                        m.setQuantite(nomenclature.getQuantite());
                        m.setAuthor(currentUser);
                        m.setDateUpdate(new Date());
                        dao.update(m);
                        update("group_qte_nomenclature");
                    }

                }
                nomenclature.setVal_total(valeurTotal());
                resetFicheComposant();
                succes();
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error " + action, ex);
            return false;
        } finally {
            long end = System.nanoTime();
            LOGGER.info("Temps d'execution de saveNewComposant : " + (end - start) / 1_000_000_000.0 + " s");
        }
    }

    private double giveQteComposant() {
        double re = 0;
        for (YvsProdComposantNomenclature c : nomenclature.getComposants()) {
            re += c.getQuantite() * c.getCoefficient();
        }
        return re;
    }

    @Override
    public void deleteBean() {
        try {
            if (tabIds != null && tabIds.trim().length() > 0) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsProdNomenclature> list = new ArrayList<>();
                YvsProdNomenclature y;
                for (Long ids : l) {
                    y = nomenclatures.get(ids.intValue());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    list.add(y);
                    dao.delete(y);
                    if (y.getId() == nomenclature.getId()) {
                        resetFiche();
                    }

                }
                nomenclatures.removeAll(list);
                succes();
                tabIds = "";

            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    public void deleteBean_(YvsProdNomenclature y) {
        selectNomenclature = y;
    }

    public void deleteBean_() {
        try {
            if (selectNomenclature != null && selectNomenclature.getId() > 0) {
                dao.delete(selectNomenclature);
                int idx = nomenclatures.indexOf(selectNomenclature);
                if (idx > -1) {
                    nomenclatures.remove(idx);
                }
                if (selectNomenclature.getId() == nomenclature.getId()) {
                    resetFiche();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    public void deleteBeanComposant() {
        try {
            if (tabIds_composant != null && tabIds_composant.trim().length() > 0) {
                String[] ids = tabIds_composant.trim().split("-");
                for (String o : ids) {
                    long id = Long.valueOf(o);
                    dao.delete(new YvsProdComposantNomenclature(id));
                    int idx = nomenclatures.indexOf(new YvsProdNomenclature(nomenclature.getId()));
                    if (idx > -1) {
                        YvsProdNomenclature m = nomenclatures.get(idx);
                        int i = m.getComposants().indexOf(new YvsProdComposantNomenclature(id));
                        if (i > -1) {
                            YvsProdComposantNomenclature y = m.getComposants().get(i);
                            if (nomenclature.isQteLieAuxComposant()) {
                                m.setQuantite(m.getQuantite() - y.getQuantite());
                                m.setAuthor(currentUser);
                                dao.update(m);
                            }
                            m.getComposants().remove(i);
                            nomenclatures.set(idx, m);
                        }
                    }
                    if (id == composant.getId()) {
                        resetFicheComposant();
                    }
                }
                succes();
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    public void deleteBeanComposant_(YvsProdComposantNomenclature y) {
        selectComposant = y;
        if (!notRappelDel) {
            openDialog("dlgConfirmDeleteComposante_");
        } else {
            deleteBeanComposant_();
        }
    }

    public void deleteBeanComposant_() {
        try {
            if (selectComposant != null && selectComposant.getId() > 0) {
                dao.delete(selectComposant);
                int idx = nomenclatures.indexOf(new YvsProdNomenclature(nomenclature.getId()));
                if (idx > -1) {
                    YvsProdNomenclature m = nomenclatures.get(idx);
                    int i = m.getComposants().indexOf(selectComposant);
                    if (i > -1) {
                        YvsProdComposantNomenclature y = m.getComposants().get(i);
                        if (nomenclature.isQteLieAuxComposant()) {
                            m.setQuantite(m.getQuantite() - y.getQuantite());
                            m.setAuthor(currentUser);
                            dao.update(m);
                        }
                        m.getComposants().remove(i);
                        nomenclatures.set(idx, m);
                    }
                }
                nomenclature.getComposants().remove(selectComposant);
                if (selectComposant.getId() == composant.getId()) {
                    resetFicheComposant();
                }
                nomenclature.setVal_total(valeurTotal());
                succes();
                update("txt-valeur_nomenclature");
                update("data_composant_nomenclature");
                update("form_composant_nomenclature");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSelectObject(YvsProdNomenclature y) {
        populateView(UtilProd.buildBeanNomenclature(y));
        //cache les composants innactif
        toogleDisplayComposantActif(false);
        loadNomenclatureSite(y);
        if (y.getUniteMesure() != null) {
            if (!nomenclature.getCompose().getConditionnements().contains(y.getUniteMesure())) {
                nomenclature.getCompose().getConditionnements().add(y.getUniteMesure());
                update("txt_unite_nomenclature");
            }
        }
        update("blog_form_nomenclature");
        update("cancel_nomenclature");
    }

    public void toogleDisplayComposantActif(Boolean b) {
        if (b) {
            //affiche les composant innactif
            nomenclature.setComposants(new ArrayList<YvsProdComposantNomenclature>(selectNomenclature.getComposants()));
            displayCompSub_(true);
        } else {
            List<YvsProdComposantNomenclature> l = new ArrayList<>(nomenclature.getComposants());
            for (YvsProdComposantNomenclature c : l) {
                if (!c.getActif()) {
                    nomenclature.getComposants().remove(c);
                }
            }
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null && ev.getObject() != null) {
            selectNomenclature = (YvsProdNomenclature) ev.getObject();
            onSelectObject(selectNomenclature);
            displaySousProd = true;
            displayCompSub_(true);
            tabIds = nomenclatures.indexOf(selectNomenclature) + "";
        }
    }

    public Double giveQuantite(YvsProdComposantNomenclature current) {
        if (current.getComposantLie() != null) {
            return current.getComposantLie().getQuantite() * current.getQuantite() / 100;
        } else {
            return current.getQuantite();
        }
    }

    public Double giveQuantite(YvsProdComposantNomenclature current, List<YvsProdComposantNomenclature> list) {
        if (current.getComposantLie() != null) {
            YvsProdComposantNomenclature co = null;
            for (YvsProdComposantNomenclature c : list) {
                if (c.equals(current)) {
                    co = c;
                }
            }
            if (co != null) {
                return co.getQuantite() * current.getQuantite() / 100;
            }
        } else {
            return current.getQuantite();
        }
        return 0d;
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewComposant(SelectEvent ev) {
        if (ev != null && ev.getObject() != null) {
            YvsProdComposantNomenclature y = (YvsProdComposantNomenclature) ev.getObject();
            if (y.getId() > 0) {
                if (y.getArticle().getConditionnements().isEmpty()) {
                    y.getArticle().setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{y.getArticle()}));
                }
                populateViewComposant(UtilProd.buildBeanComposantNomenclature(y));
                composant.setComposantLie(y.getComposantLie() != null);
                composantLie = UtilProd.buildBeanComposantNomenclature(y.getComposantLie());
            } else {
                getWarningMessage("Non éditable ici !");
            }
        }
    }

    public void openViewNomPsf(YvsProdComposantNomenclature y, long idArticle) {
        //Vérifie si l'article composant a une nomenclature
        if (y != null) {
            composant.setComposantLie(y.getComposantLie() != null);
            composantLie = UtilProd.buildBeanComposantNomenclature(y.getComposantLie());
        }
        listOthers = dao.loadNameQueries("YvsProdNomenclature.findByArticleActif", new String[]{"article"}, new Object[]{new YvsBaseArticles(idArticle)});
        if (listOthers != null && !listOthers.isEmpty()) {
            openDialog("dlgApplyPsf");
            otherNom = UtilProd.buildSimpleBeanNomenclature(listOthers.get(0));
            update("form_define_nom_");
        }
    }

    public void unLoadOnViewComposant(UnselectEvent ev) {
        resetFicheComposant();
    }

    public void toogleActiveAll(Boolean activ) {
        try {
            if (tabIds_composant != null && !tabIds_composant.trim().isEmpty()) {
                String[] ids = tabIds_composant.trim().split("-");
                for (String o : ids) {
                    int idx = Integer.valueOf(o);
                    if (activ) {
                        nomenclature.getComposants().get(idx).setActif(false);
                        toogleActiveComposant(nomenclature.getComposants().get(idx));
                    } else {
                        nomenclature.getComposants().get(idx).setActif(true);
                        toogleActiveComposant(nomenclature.getComposants().get(idx));
                    }
                }
            } else {
                getErrorMessage("Aucune selection n'a été trouvé !");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    public void toogleActiveComposant(YvsProdComposantNomenclature ra) {
        if (ra.getId() > 0) {
            ra.setActif(!ra.getActif());
            ra.setAuthor(currentUser);
            ra.setDateUpdate(new Date());
            nomenclature.setVal_total(valeurTotal());
            dao.update(ra);
        }
    }

    public void toogleInsideCout(YvsProdComposantNomenclature ra) {
        if (ra.getId() > 0) {
            ra.setInsideCout(!ra.getInsideCout());
            ra.setAuthor(currentUser);
            ra.setDateUpdate(new Date());
            dao.update(ra);
            nomenclature.setVal_total(valeurTotal());
            succes();
        }
    }

    private void chooseArticle(Articles y) {
        if (compose) {
            nomenclature.setUnitePreference(Constantes.UNITE_QUANTITE);
            nomenclature.setCompose(y);
            nomenclature.setReference(getReference(y));
            update("group_qte_nomenclature");
            update("txt_article_nomenclature");
            update("select_unite_preference");
            update("txt_reference_nomenclature");
        } else {
            composant.setArticle(y);
            //Vérifie si l'article composant a une nomenclature
            openViewNomPsf(null, y.getId());
            update("txt_qte_composant_converti");
            update("txt_unite_composant_nomenclature");
            update("txt_article_composant_nomenclature");
        }
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if (ev != null && ev.getObject() != null) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            bean.setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{bean}));
            chooseArticle(UtilProd.buildBeanArticleForForm(bean));
        }
    }

    public void loadOnViewUnite(SelectEvent ev) {
        if (ev != null && ev.getObject() != null) {
            YvsBaseConditionnement bean = (YvsBaseConditionnement) ev.getObject();
            composant.setUnite(UtilProd.buildBeanConditionnement(bean));
            update("txt_qte_composant_converti");
            update("txt_unite_composant_nomenclature");
        }
    }

    public void searchArticle(boolean compose) {
        this.compose = compose;
        if (compose) {
            searchArticleByCompose();
        } else {
            searchArticleByComposant();
        }
    }

    private void searchArticleByCompose() {
        String num = nomenclature.getCompose().getRefArt();
        nomenclature.getCompose().setDesignation("");
        nomenclature.getCompose().setError(true);
        nomenclature.getCompose().setId(0);
        if (num != null && !num.trim().isEmpty()) {
            ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
            if (m != null) {
                Articles y = m.searchArticleActif(null, num, true);
                if (m.getArticlesResult() != null && !m.getArticlesResult().isEmpty()) {
                    if (m.getArticlesResult().size() > 1) {
                        update("data_article_nomenclature");
                    } else {
                        chooseArticle(y);
                    }
                    nomenclature.getCompose().setError(false);
                }
            }
        }
    }

    private void searchArticleByComposant() {
        String num = composant.getArticle().getRefArt();
        composant.getArticle().setDesignation("");
        composant.getArticle().setError(true);
        composant.getArticle().setId(0);
        if (num != null && !num.trim().isEmpty()) {
            ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
            if (m != null) {
                Articles y = m.searchArticleActif(null, num, true);
                if (m.getArticlesResult() != null && !m.getArticlesResult().isEmpty()) {
                    if (m.getArticlesResult().size() > 1) {
                        update("data_article_nomenclature");
                    } else {
                        chooseArticle(y);
                    }
                    composant.getArticle().setError(false);
                }
            }
        }
    }

    public void initArticles(boolean compose) {
        this.compose = compose;
        ManagedArticles a = (ManagedArticles) giveManagedBean("Marticle");
        if (a != null) {
            a.initArticles(null, nomenclature.getCompose());
        }
        update("data_article_nomenclature");
    }

    public void choixUniteCompose(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            int idx = nomenclature.getCompose().getConditionnements().indexOf(new YvsBaseConditionnement(id));
            if (idx >= 0) {
                nomenclature.setUnite(UtilProd.buildBeanConditionnement(nomenclature.getCompose().getConditionnements().get(idx)));
            }
        }
    }

    public void choixUniteComposant(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            int idx = composant.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(id));
            if (idx >= 0) {
                YvsBaseConditionnement y = composant.getArticle().getConditionnements().get(idx);
                composant.setUnite(UtilProd.buildBeanConditionnement(y));
                chooseUniteComposant();
            }
        }
    }

    public void chooseUniteComposant() {
        if (composant.getUnite() != null && composant.getUnite().getId() > 0) {
            Double taux = 1D;
            if (composant.getUnite().getUnite().getId() != nomenclature.getUnite().getUnite().getId()) {
                taux = (Double) dao.loadOneByNameQueries("YvsBaseTableConversion.findTauxByUniteCorrespondance", new String[]{"unite", "uniteE"}, new Object[]{new YvsBaseUniteMesure(composant.getUnite().getUnite().getId()), new YvsBaseUniteMesure(nomenclature.getUnite().getUnite().getId())});
            }
            composant.setCoefficient(taux != null ? taux : 0);
        }
    }

    public void searchUniteMesure() {
        String num = composant.getUnite().getUnite().getReference();
        composant.getUnite().getUnite().setLibelle("");
        composant.getUnite().getUnite().setError(true);
        composant.getUnite().getUnite().setId(0);
        if (num != null && !num.trim().isEmpty()) {
            ManagedUniteMesure m = (ManagedUniteMesure) giveManagedBean(ManagedUniteMesure.class);
            if (m != null) {
                UniteMesure y = m.findUnite(num, true);
                if (m.getUnites() != null && !m.getUnites().isEmpty()) {
                    if (m.getUnites().size() > 1) {
                        update("data_unite_nomenclature");
                    } else {
                        composant.getUnite().setUnite(y);
                        update("txt_qte_composant_converti");
                    }
                    composant.getUnite().getUnite().setError(false);
                }
            }
        }
    }

    public void initUnites() {
        ManagedUniteMesure a = (ManagedUniteMesure) giveManagedBean(ManagedUniteMesure.class);
        if (a != null) {
            a.initUnites(composant.getUnite().getUnite());
        }
        update("data_unite_nomenclature");
    }

    public void blurQuantiteComposant() {
        if (nomenclature.isQteLieAuxComposant() && composant.getTypeComposant().equals("N")) {
            double qte = composant.getQuantite() * composant.getCoefficient();
            nomenclature.setQuantite(nomenclature.getQuantite() + qte);
            update("txt_qte_nomenclature");
        }
    }

    public void giveSoeQuantiteComposant() {
        if (nomenclature.isQteLieAuxComposant()) {
            nomenclature.setQuantite(0.0);
            for (YvsProdComposantNomenclature c : nomenclature.getComposants()) {
                if (c.getType().equals("N")) {
                    double qte = convertirUnite(new UniteMesure(c.getUnite().getId()), nomenclature.getUnite().getUnite(), c.getQuantite());
                    nomenclature.setQuantite(nomenclature.getQuantite() + qte);
                }
            }
        }
    }

    public void checkPrincipal() {
        if (nomenclature.isPrincipal()) {
            if (nomenclature.getCompose() != null && nomenclature.getCompose().getId() > 0) {
                champ = new String[]{"article"};
                val = new Object[]{new YvsBaseArticles(nomenclature.getCompose().getId())};
                YvsProdNomenclature p = (YvsProdNomenclature) dao.loadOneByNameQueries("YvsProdNomenclature.findByPrincipal", champ, val);
                if (p != null && (p.getId() > 0 && !p.getId().equals(nomenclature.getId()))) {
                    getErrorMessage("Cette article a déjà une nomenclature principale");
                    nomenclature.setPrincipal(false);
                    update("chk_principal_nomenclature");
                }
            }
        }
    }

    public void setPrincipal(YvsProdNomenclature y) {
        if (!y.getPrincipal()) {
            champ = new String[]{"article"};
            val = new Object[]{y.getArticle()};
            YvsProdNomenclature p = (YvsProdNomenclature) dao.loadOneByNameQueries("YvsProdNomenclature.findByPrincipal", champ, val);
            if (p != null && (p.getId() > 0 && !p.getId().equals(nomenclature.getId()))) {
                getErrorMessage("Cette article a déjà une nomenclature principale");
                return;
            }
        }
        y.setPrincipal(!y.getPrincipal());
        String rq = "UPDATE yvs_prod_nomenclature SET principal=" + y.getPrincipal() + " WHERE id=?";
        Options[] param = new Options[]{new Options(y.getId(), 1)};
        dao.requeteLibre(rq, param);
        nomenclatures.set(nomenclatures.indexOf(y), y);
    }

    public void setActive(YvsProdNomenclature y) {
        y.setActif(!y.getActif());
        String rq = "UPDATE yvs_prod_nomenclature SET actif=" + y.getActif() + " WHERE id=?";
        Options[] param = new Options[]{new Options(y.getId(), 1)};
        dao.requeteLibre(rq, param);
        nomenclatures.set(nomenclatures.indexOf(y), y);
    }

    public String getReference(Articles y) {
        String n = "NOME-" + y.getRefArt() + "/00";
        champ = new String[]{"reference", "societe"};
        val = new Object[]{n, currentAgence.getSociete()};
        YvsProdNomenclature p = (YvsProdNomenclature) dao.loadOneByNameQueries("YvsProdNomenclature.findByReference", champ, val);
        if (p != null && !p.getId().equals(nomenclature.getId())) {
            for (int i = 1; i < 100; i++) {
                if (i < 10) {
                    n = "NOME-" + y.getRefArt() + "/0" + i;
                } else {
                    n = "NOME-" + y.getRefArt() + "/" + i;
                }
                champ = new String[]{"reference", "societe"};
                val = new Object[]{n, currentAgence.getSociete()};
                p = (YvsProdNomenclature) dao.loadOneByNameQueries("YvsProdNomenclature.findByReference", champ, val);
                if (p == null || p.getId() < 1) {
                    return n;
                }
            }
        }
        return n;
    }

    public double convertirQteComposant() {
        return convertirUnite(composant.getUnite().getUnite(), nomenclature.getUnite().getUnite(), composant.getQuantite());
    }

    public void clearParams() {
        numSearch = null;
        artSearch = null;
        dateSeacrh = false;
        actifSearch = null;
        forCondSearch = null;
        categorieSearch = null;
        siteSearch = 0;
        familleSearch = 0;
        paginator.getParams().clear();
        addParamMasquer_(false);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.reference", "reference", null);
        if (numSearch != null && !numSearch.trim().isEmpty()) {
            switch (droit) {
                case 1:
                    p = new ParametreRequete("UPPER(y.reference)", "reference", numSearch.toUpperCase() + "%", "LIKE", "AND");
                    break;
                case 2:
                    p = new ParametreRequete("UPPER(y.nomclature.reference)", "reference", numSearch.toUpperCase() + "%", "LIKE", "AND");
                    break;
            }
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamForCond() {
        ParametreRequete p = new ParametreRequete("y.forConditionnement", "forCond", null);
        switch (droit) {
            case 1:
                p = new ParametreRequete("y.forConditionnement", "forCond", forCondSearch, "=", "AND");
                break;
            case 2:
                p = new ParametreRequete("y.nomclature.forConditionnement", "forCond", forCondSearch, "=", "AND");
                break;
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamActif() {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", null);
        switch (droit) {
            case 1:
                p = new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND");
                break;
            case 2:
                p = new ParametreRequete("y.nomclature.actif", "actif", actifSearch, "=", "AND");
                break;
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamCategorie() {
        ParametreRequete p = new ParametreRequete("y.gamme.article.categorie", "categorie", null);
        if (categorieSearch != null && !categorieSearch.trim().isEmpty()) {
            switch (droit) {
                case 1:
                    p = new ParametreRequete("y.article.categorie", "categorie", categorieSearch, "=", "AND");
                    break;
                case 2:
                    p = new ParametreRequete("y.nomclature.article.categorie", "categorie", categorieSearch, "=", "AND");
                    break;
            }
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamSite() {
        ParametreRequete p = new ParametreRequete("y.site", "site", null);
        updateParametreToSite(false);
        if (siteSearch > 0) {
            p = new ParametreRequete("y.site", "site", new YvsProdSiteProduction(siteSearch), "=", "AND");
        }
        paginator.addParam(p);
        if (siteSearch > 0) {
            updateParametreToSite(true);
        }
        loadAll(true, true);
    }

    public void addParamFamille() {
        ParametreRequete p = new ParametreRequete("y.article.famille", "famille", null);
        if (familleSearch > 0) {
            p = new ParametreRequete("y.article.famille", "famille", new YvsBaseFamilleArticle(familleSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    private void updateParametreToSite(boolean bySite) {
        droit = buildDroit();
        if (droit == 1) {
            for (int i = 0; i < paginator.getParams().size(); i++) {
                ParametreRequete p = paginator.getParams().get(i);
                if (!p.getParamNome().equals("site")) {
                    if (bySite) {
                        p.setAttribut(p.getAttribut() != null ? p.getAttribut().replace("y.", "y.nomenclature.") : "y.nomenclature.site");
                    } else {
                        p.setAttribut(p.getAttribut() != null ? p.getAttribut().replace("y.nomenclature.", "y.") : "y.site");
                    }
                    paginator.getParams().set(i, p);
                }
            }
            if (bySite) {
                droit = 2;
            }
        }
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.debutValidite", "dates", null, "=", "AND");
        if (dateSeacrh) {
            p = new ParametreRequete(null, "dates", null, "=", "AND");
            switch (droit) {
                case 1:
                    p.getOtherExpression().add(new ParametreRequete("y.debutValidite", "dates", dateDebutSearch, dateFinSearch, "BETWEEN", "AND"));
                    p.getOtherExpression().add(new ParametreRequete("y.finValidite", "dates", dateDebutSearch, dateFinSearch, "BETWEEN", "AND"));
                    break;
                case 2:
                    p.getOtherExpression().add(new ParametreRequete("y.nomclature.debutValidite", "dates", dateDebutSearch, dateFinSearch, "BETWEEN", "AND"));
                    p.getOtherExpression().add(new ParametreRequete("y.nomclature.finValidite", "dates", dateDebutSearch, dateFinSearch, "BETWEEN", "AND"));
                    break;
            }
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamArticle() {
        ParametreRequete p = new ParametreRequete("y.article", "article", null);
        if (artSearch != null && !artSearch.trim().isEmpty()) {
            p = new ParametreRequete(null, "article", artSearch.toUpperCase() + "%", "LIKE", "AND");
            switch (droit) {
                case 1:
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "article", artSearch.toUpperCase() + "%", "LIKE", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "article", artSearch.toUpperCase() + "%", "LIKE", "OR"));
                    break;
                case 2:
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.nomclature.article.refArt)", "article", artSearch.toUpperCase() + "%", "LIKE", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.nomclature.article.designation)", "article", artSearch.toUpperCase() + "%", "LIKE", "OR"));
                    break;
            }
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamComposant() {
        List<Long> ids = dao.loadNameQueries("YvsProdComposantNomenclature.findIdsNomByComposant", new String[]{"article", "societe"}, new Object[]{composantSearch.trim(), currentAgence.getSociete()});
        if (ids.isEmpty()) {
            ids.add(0L);
        }
        switch (droit) {
            case 1:
                paginator.addParam(new ParametreRequete("y.id", "ids", ids, "IN", "AND"));
                break;
            case 2:
                paginator.addParam(new ParametreRequete("y.nomenclature.id", "ids", ids, "IN", "AND"));
                break;
        }
        loadAll(true, true);
    }

    public void addParamMasquer_(Boolean masque) {
        switch (droit) {
            case 1:
                paginator.addParam(new ParametreRequete("y.masquer", "masquer", masque, "=", "AND"));
                break;
            case 2:
                paginator.addParam(new ParametreRequete("y.nomenclature.masquer", "masquer", masque, "=", "AND"));
                break;
        }
        loadAll(true, true);
    }

    public void addParamMasquer(ValueChangeEvent ev) {
        addParamMasquer_((Boolean) ev.getNewValue());
    }

    public String handleCommand(String command, String[] params) {

        return null;
    }

    public boolean toUpComposant(YvsProdComposantNomenclature y) {
        int idx = nomenclature.getComposants().indexOf(y);
        if (idx > -1) {
            return idx > 0;
        }
        return false;
    }

    public void upComposant(YvsProdComposantNomenclature y) {
        if (toUpComposant(y)) {
            int idx = nomenclature.getComposants().indexOf(y);
            YvsProdComposantNomenclature n = nomenclature.getComposants().get(idx - 1);
            if (n != null && n.getId() > 0) {
                int ordre = n.getOrdre();
                n.setOrdre(y.getOrdre() == 0 ? 1 : y.getOrdre());
                y.setOrdre(ordre);

                dao.update(n);
                dao.update(y);

                nomenclature.getComposants().set(idx, n);
                nomenclature.getComposants().set(idx - 1, y);
            }
            succes();
        }
    }

    public boolean toDownComposant(YvsProdComposantNomenclature y) {
        int idx = nomenclature.getComposants().indexOf(y);
        if (idx > -1) {
            return idx < nomenclature.getComposants().size() - 1;
        }
        return false;
    }

    public void downComposant(YvsProdComposantNomenclature y) {
        if (toDownComposant(y)) {
            int idx = nomenclature.getComposants().indexOf(y);
            YvsProdComposantNomenclature n = nomenclature.getComposants().get(idx + 1);
            if (n != null && n.getId() > 0) {
                int ordre = n.getOrdre();
                n.setOrdre(y.getOrdre());
                y.setOrdre(ordre == 0 ? 1 : ordre);

                dao.update(n);
                dao.update(y);

                nomenclature.getComposants().set(idx, n);
                nomenclature.getComposants().set(idx + 1, y);
                succes();
            }
        }
    }

    public void loadNomenclatureSite(YvsProdNomenclature y) {
        List<YvsProdSiteProduction> list = dao.loadNameQueries("YvsProdSiteProduction.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        YvsProdNomenclatureSite g;
        for (YvsProdSiteProduction s : list) {
            boolean exist = false;
            for (YvsProdNomenclatureSite m : y.getSites()) {
                if (m.getSite().equals(s)) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                g = new YvsProdNomenclatureSite(y, s);
                g.setDateSave(new Date());
                g.setDateUpdate(new Date());
                g.setAuthor(currentUser);
                nomenclature.getSites().add(g);
            }
        }
    }

    public void activeNomenclatureSite(YvsProdNomenclatureSite bean) {
        if (bean != null) {
            long id = bean.getId();
            if (bean.getId() > 0) {
                dao.delete(bean);
                bean.setId(-id);
            } else {
                bean.setId(null);
                bean = (YvsProdNomenclatureSite) dao.save1(bean);
            }
            int idx = nomenclature.getSites().indexOf(new YvsProdNomenclatureSite(id));
            if (idx > -1) {
                nomenclature.getSites().set(idx, bean);
            }
            succes();
        }
    }

    public void activeSelectNomeclature(YvsProdSiteProduction site) {
        List<Integer> selections = decomposeSelection(tabIds);
        YvsProdNomenclatureSite bean;
        Long NB;
        int n = 0;
        for (Integer i : selections) {
            bean = new YvsProdNomenclatureSite();
            bean.setAuthor(currentUser);
            bean.setDateSave(new Date());
            bean.setDateUpdate(new Date());
            bean.setNomenclature(nomenclatures.get(i));
            bean.setSite(site);
            NB = (Long) dao.loadObjectByNameQueries("YvsProdNomenclatureSite.findCOne", new String[]{"nomenclature", "site"}, new Object[]{nomenclatures.get(i), site});
            if (!Util.asLong(NB)) {
                dao.save(bean);
            }
            n++;
        }
        if (n > 0) {
            succes();
        } else {
            getWarningMessage("Aucune nomenclature n'a été selectionné ");
        }
    }

    public void desactiveSelectNomeclature(YvsProdSiteProduction site) {
        List<Integer> selections = decomposeSelection(tabIds);
        int n = 0;
        String query = "DELETE FROM yvs_prod_nomenclature_site WHERE nomenclature=? AND site=?";
        for (Integer i : selections) {
            dao.requeteLibre(query, new Options[]{new Options(nomenclatures.get(i).getId(), 1), new Options(site.getId(), 2)});
            n++;
        }
        if (n > 0) {
            succes();
        } else {
            getWarningMessage("Aucune nomenclature n'a été selectionné ");
        }
    }

    public void buildCodeAcces(YvsProdNomenclature y) {
        if (y != null) {
            codeAcces = y.getAcces();
            selectNomenclature = y;
            update("data_code_acces_nomenclature");
        }
    }

    public void attribCodeAcces(SelectEvent ev) {
        if (ev != null && ev.getObject() != null) {
            codeAcces = (YvsBaseCodeAcces) ev.getObject();
            selectNomenclature.setAcces(codeAcces);
            dao.update(selectNomenclature);
            int idx = nomenclatures.indexOf(selectNomenclature);
            if (idx > -1) {
                nomenclatures.set(idx, selectNomenclature);
            }
            succes();
        }
    }

    public void cloneNomenclature() {
        if (nomenclature.getId() > 0) {
            //recopie la nomenclature...
            YvsProdNomenclature y = UtilProd.buildBeanNomenclature(nomenclature, currentUser);
            y.setId(null);
            y = (YvsProdNomenclature) dao.save1(y);
            if (y != null && y.getId() > 0) {
                YvsProdComposantNomenclature current;
                if (y.getComposants() == null) {
                    y.setComposants(new ArrayList<YvsProdComposantNomenclature>());
                }
//                y.getComposants().clear();
                for (YvsProdComposantNomenclature c : nomenclature.getComposants()) {
                    if (c.getId() > 0) {
                        current = new YvsProdComposantNomenclature(c);
                        current.setId(null);
                        current.setNomenclature(y);
                        current.setAuthor(currentUser);
                        current.setDateSave(new Date());
                        current.setDateUpdate(new Date());
                        y.getComposants().add((YvsProdComposantNomenclature) dao.save1(current));
                    }
                }
                y.setNew_(true);
                nomenclatures.add(0, y);
                update("data_nomenclature");
                getInfoMessage("Nomenclature clôné avec succès !");
            }
        }
    }

    public void applyOtherNom() {
        if (otherNom.getId() > 0 && composant.getQuantite() > 0 && composant.getUnite().getId() > 0) {
            YvsProdComposantNomenclature cn;
            //save la pate
            double qte = composant.getQuantite();
            cn = findComposant(new YvsBaseArticles(composant.getArticle().getId()));
            if (cn == null) {
                saveNewComposant();
            }
            List<YvsProdComposantNomenclature> composants = dao.loadNameQueries("YvsProdComposantNomenclature.findByNomenclature", new String[]{"nomenclature"}, new Object[]{new YvsProdNomenclature(otherNom.getId())});
            for (YvsProdComposantNomenclature c : composants) {
                //Si le composant n'est pas encore dans la composition. si c'est le cas remplace
                cn = findComposant(c.getArticle());
                if (cn == null) {
                    cn = new YvsProdComposantNomenclature(c);
                    cn.setId(null);
                    cn.setDateSave(new Date());
                    cn.setQuantite(0d);
                }
                cn.setDateUpdate(new Date());
                cn.setNomenclature(new YvsProdNomenclature(nomenclature.getId()));
                double d = ((qte * c.getQuantite()) / c.getNomenclature().getQuantite());
                if (!addComposant) {
                    cn.setQuantite(d);
                } else {
                    cn.setQuantite(cn.getQuantite() + d);
                }
                if (cn.getId() <= 0) {
                    cn = (YvsProdComposantNomenclature) dao.save1(cn);
                    nomenclature.getComposants().add(cn);
                } else {
                    dao.update(cn);
                    int idx = nomenclature.getComposants().indexOf(cn);
                    if (idx >= 0) {
                        nomenclature.getComposants().set(idx, cn);
                    }
                }
            }
            update("data_composant_nomenclature");
        }
    }

    private YvsProdComposantNomenclature findComposant(YvsBaseArticles a) {
        for (YvsProdComposantNomenclature c : nomenclature.getComposants()) {
            if (c.getArticle().equals(a) && c.getId() > 0) {
                return c;
            }
        }
        return null;
    }

    public void moveComposantLie() {
        for (YvsProdComposantNomenclature c : nomenclature.getComposants()) {
            if (c.getId() > 0) {
                c.setComposantLie(null);
                dao.update(c);
            }
        }
        update("data_composant_nomenclature");
    }

    public void fusionner(boolean fusionne) {
        try {
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                long newValue = nomenclatures.get(ids.get(0)).getId();
                if (!fusionne) {
                    if (!autoriser("base_user_fusion")) {
                        openNotAcces();
                        return;
                    }
                    String oldValue = "0";
                    for (int i : ids) {
                        if (nomenclatures.get(i).getId() != newValue) {
                            oldValue += "," + nomenclatures.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_prod_nomenclature", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                nomenclatures.remove(new YvsProdNomenclature(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = nomenclatures.get(idx).getReference();
                    } else {
                        YvsProdNomenclature c = (YvsProdNomenclature) dao.loadOneByNameQueries("YvsProdNomenclature.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null && c.getId() > 0) {
                            fusionneTo = c.getReference();
                        }
                    }
                    YvsProdNomenclature c;
                    for (int i : ids) {
                        long oldValue = nomenclatures.get(i).getId();
                        if (oldValue != newValue) {
                            fusionnesBy.add(nomenclatures.get(i).getReference());
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 nomenclatures");
            }
        } catch (NumberFormatException ex) {
            getException("Error fusionner ", ex);
        }
    }

    public void fusionnerComposant(boolean fusionne) {
        try {
            fusionneToComposant = "";
            fusionnesByComposant.clear();
            List<Integer> ids = decomposeSelection(tabIds_composant);
            if (!ids.isEmpty()) {
                long newValue = nomenclature.getComposants().get(ids.get(0)).getId();
                if (!fusionne) {
                    if (!autoriser("base_user_fusion")) {
                        openNotAcces();
                        return;
                    }
                    String oldValue = "0";
                    for (int i : ids) {
                        if (nomenclature.getComposants().get(i).getId() != newValue) {
                            oldValue += "," + nomenclature.getComposants().get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_prod_composant_nomenclature", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 && !id.equals(newValue)) {
                                nomenclature.getComposants().remove(new YvsProdComposantNomenclature(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneToComposant = nomenclature.getComposants().get(idx).getArticle().getDesignation();
                    } else {
                        YvsProdComposantNomenclature c = (YvsProdComposantNomenclature) dao.loadOneByNameQueries("YvsProdComposantNomenclature.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null && c.getId() > 0) {
                            fusionneToComposant = c.getArticle().getDesignation();
                        }
                    }
                    YvsProdComposantNomenclature c;
                    for (int i : ids) {
                        long oldValue = nomenclature.getComposants().get(i).getId();
                        if (oldValue != newValue) {
                            fusionnesByComposant.add(nomenclature.getComposants().get(i).getArticle().getDesignation());
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 composants");
            }
        } catch (NumberFormatException ex) {
            getException("Error fusionner ", ex);
        }
    }

    public void confirmMasque(YvsProdNomenclature nom) {
        selectNomenclature = nom;
        if (!nom.getMasquer()) {
            openDialog("dlgConfirmMasque");
        } else {
            masqueNomenclature();
        }
    }

    public void masqueNomenclature() {
        if (selectNomenclature != null) {
            selectNomenclature.setMasquer(!selectNomenclature.getMasquer());
            selectNomenclature.setAuthor(currentUser);
            selectNomenclature.setDateUpdate(new Date());
            dao.update(selectNomenclature);
            if (selectNomenclature.getMasquer()) {
                nomenclatures.remove(selectNomenclature);
                closeDialog("dlgConfirmMasque");
            }
            update("data_nomenclature");
        }
    }

    private List<YvsProdComposantNomenclature> tempComposant;

    public void displayCompSub(ValueChangeEvent ev) {
        displayCompSub_((Boolean) ev.getNewValue());
    }

    public void displayCompSub_(Boolean b) {
        if (b != null ? b : false) {
            int idx = 1;
            tempComposant = new ArrayList<>();
            List<YvsProdComposantNomenclature> l = new ArrayList<>(nomenclature.getComposants());
            for (YvsProdComposantNomenclature c : l) {
                tempComposant.clear();
//                nomenclature.getComposants().addAll((idx), new ArrayList<>(displaySubComposant(c)));
                c.setSubCsomposants(new ArrayList<>(displaySubComposant(c)));
                idx = idx + tempComposant.size() + 1;
            }
        } else {
            tempComposant = new ArrayList<>();
            List<YvsProdComposantNomenclature> l = new ArrayList<>(nomenclature.getComposants());
            for (YvsProdComposantNomenclature c : l) {
                if (c.getId() < 0) {
                    nomenclature.getComposants().remove(c);
                }
            }
        }
        update("data_composant_nomenclature");
    }

    public List<YvsProdComposantNomenclature> displaySubComposant(YvsProdComposantNomenclature composant) {
        //Trouve la comenclature principale du composant
        if (!composant.getArticle().equals(composant.getNomenclature().getArticle())) {
            List<YvsProdComposantNomenclature> l;
            Long nb = (Long) dao.loadObjectByNameQueries("YvsProdNomenclature.countByArticleActif", new String[]{"article"}, new Object[]{composant.getArticle()});
            if (nb != null && nb > 1) {
                l = dao.loadNameQueries("YvsProdComposantNomenclature.findComposantNomByArticleP",
                        new String[]{"article"}, new Object[]{composant.getArticle()});
            } else {
                l = dao.loadNameQueries("YvsProdComposantNomenclature.findComposantNomByArticle",
                        new String[]{"article"}, new Object[]{composant.getArticle()});
            }
            for (YvsProdComposantNomenclature c : l) {
                c.setQuantite(composant.getQuantite() * c.getQuantite() / c.getNomenclature().getQuantite());
                c.setId(-c.getId());
                c.setPr(dao.getPr(c.getArticle().getId(), 0, 0L, new Date(), c.getUnite().getId()));
                c.setValeur(c.getQuantite() * c.getPr());
                tempComposant.add(c);
                displaySubComposant(c);
            }
        }
        return tempComposant;
    }

    public void toogleComposantAlternatif(YvsProdComposantNomenclature composant) {
        if (composant != null && composant.getId() > 0) {
            composant.setAlternatif(!composant.getAlternatif());
            composant.setAuthor(currentUser);
            composant.setDateUpdate(new Date());
            dao.update(composant);
            update("data_composant_nomenclature");
        }
    }

    /**
     * Liaison gamme*
     */
    private List<YvsProdComposantOp> composantsOperation = new ArrayList<>();

    public List<YvsProdComposantOp> getComposantsOperation() {
        return composantsOperation;
    }

    public void setComposantsOperation(List<YvsProdComposantOp> composantsOperation) {
        this.composantsOperation = composantsOperation;
    }

    public void checkGammeArticle(YvsProdComposantNomenclature composant) {
        List<YvsProdOperationsGamme> l;
        Long nb = (Long) dao.loadObjectByNameQueries("YvsProdGammeArticle.CountByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(nomenclature.getCompose().getId())});
        if (nb != null && nb > 1) {
            l = dao.loadNameQueries("YvsProdOperationsGamme.findByArticleP",
                    new String[]{"article"}, new Object[]{new YvsBaseArticles(nomenclature.getCompose().getId())});
        } else {
            l = dao.loadNameQueries("YvsProdOperationsGamme.findByArticle",
                    new String[]{"article"}, new Object[]{new YvsBaseArticles(nomenclature.getCompose().getId())});
        }
        YvsProdComposantOp c;
        composantsOperation.clear();
        int id = -100;
        for (YvsProdOperationsGamme o : l) {
            System.err.println("---- " + o.getCodeRef());
            c = (YvsProdComposantOp) dao.loadOneByNameQueries("YvsProdComposantOp.findByOne", new String[]{"operation", "composant"}, new Object[]{o, composant});
            if (c == null) {
                c = new YvsProdComposantOp(id++);
                c.setComposant(composant);
                c.setOperation(o);
                c.setSens(Constantes.STOCK_SENS_SORTIE);
                c.setQuantite(0d);
                c.setMargeQte(composant.getCoefficient());
            }
            composantsOperation.add(c);
        }
        openDialog("dlgOperation");
        update("table_composantOp");
    }

    public void addComposantOnOperation(YvsProdComposantOp c) {
        if (c != null && c.getId() < 0) {
            c.setDateSave(new Date());
            c.setDateUpdate(new Date());
            c.setAuthor(currentUser);
            c = (YvsProdComposantOp) dao.save1(c);
        }
    }

    public void onEditQuantite(CellEditEvent event) {
        String col = event.getColumn().getHeaderText();
        if (event.getRowIndex() >= 0) {
            YvsProdComposantOp old;
            YvsProdComposantOp coop = composantsOperation.get(event.getRowIndex());
            Double newValue = coop.getQuantite();
            if (coop.getId() > 0) {
                old = (YvsProdComposantOp) dao.loadOneByNameQueries("YvsProdComposantOp.findById", new String[]{"id"}, new Object[]{coop.getId()});
                Double oldValue = (old != null) ? old.getQuantite() : 0d;
                Double value = (Double) dao.loadObjectByNameQueries("YvsProdComposantOp.findSumByGammeComposant", new String[]{"composant", "gamme", "sens"}, new Object[]{coop.getComposant(), coop.getOperation().getGammeArticle(), coop.getSens()});
                if (((value != null ? value : 0) - oldValue + newValue) > 100) {
                    getErrorMessage("Vous ne pouvez pas entrer cette valeur...car la somme des taux depassera 100%");
                    composantsOperation.get(event.getRowIndex()).setQuantite(oldValue);
                    update("table_composantOp");
                    return;
                }
                coop.setDateUpdate(new Date());
                coop.setQuantite(newValue);
//                if (coop.getUnite().getId() <= 0) {
//                    coop.setUnite(coop.getComposant().getUnite());
//                }
                dao.update(coop);
                succes();
            }
        }

    }

    public void addOrDeleteComposantToOp(YvsProdComposantOp op) {
        if (op.getId() <= 0) {
            Double value = (Double) dao.loadObjectByNameQueries("YvsProdComposantOp.findSumByComposant", new String[]{"composant", "gamme", "sens"}, new Object[]{op.getComposant(), op.getOperation().getGammeArticle(), op.getSens()});
            if (((value != null ? value : 0) + op.getQuantite()) > 100) {
                getErrorMessage("Vous ne pouvez pas entrer cette valeur...car la somme des coefficients depassera 100%");
                return;
            }
            op.setDateUpdate(new Date());
            op.setAuthor(currentUser);
            op.setId(null);
//            op.setUnite(op.getComposant().getUnite());
            op.setTauxPerte(0d);
            op = (YvsProdComposantOp) dao.save1(op);
        } else {
            try {
                dao.delete(op);
                op.setId(-op.getId());
                Double value = (Double) dao.loadObjectByNameQueries("YvsProdComposantOp.findSumByGammeComposant", new String[]{"composant", "gamme", "sens"}, new Object[]{op.getComposant(), op.getOperation().getGammeArticle(), op.getSens()});
                double taux = 100 - (value != null ? value : 0);
                op.setQuantite(taux > 0 ? taux : 0);
            } catch (Exception ex) {
                getErrorMessage("Suppression impossible !");
                getException("Error Production ", ex);
            }
        }
        update("table_composantOp");
    }

    public void changeSensComposant(YvsProdComposantOp cop, char sens) {
        if (cop.getId() > 0) {
            cop.setSens(sens);
            cop.setAuthor(currentUser);
            cop.setDateUpdate(new Date());
            dao.update(cop);
        }
        update("table_composantOp");
    }

    List<YvsProdComposantNomenclature> composantDirect = new ArrayList<>();

    public void maintenance() {
        List<YvsProdNomenclature> ln = dao.loadNameQueries("YvsProdNomenclature.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        List<YvsProdComposantNomenclature> composantSub = new ArrayList<>();
        for (YvsProdNomenclature n : ln) {
            //trouve le sous produit
            composantDirect.clear();
            composantSub.clear();
            for (YvsProdComposantNomenclature c : getSousProduit(n)) {
                //récupère les composants
                composantSub.addAll(getSousComposants(c));
                c.setType(Constantes.PROD_OP_TYPE_COMPOSANT_SOUS_PRODUIT);
                c.setInsideCout(true);
                dao.update(c);
            }
            // on a les deux liste ici
            for (YvsProdComposantNomenclature c : composantDirect) {
                for (YvsProdComposantNomenclature cn : composantSub) {
                    if (c.getArticle().equals(cn.getArticle())) {
                        c.setActif(false);
                        c.setInsideCout(false);
                        dao.update(c);
                    }
                }
            }
        }
        succes();

    }

    private List<YvsProdComposantNomenclature> getSousProduit(YvsProdNomenclature n) {
        List<YvsProdComposantNomenclature> re = new ArrayList<>();
        for (YvsProdComposantNomenclature c : n.getComposants()) {
            if (c.getType().equals(Constantes.PROD_OP_TYPE_COMPOSANT_SOUS_PRODUIT)) {
                //nomenclature existe
                re.add(c);
            } else {
                composantDirect.add(c);
            }
        }
        return re;
    }

    private List<YvsProdComposantNomenclature> getSousComposants(YvsProdComposantNomenclature com) {
        return dao.loadNameQueries("YvsProdComposantNomenclature.findComposantNomByArticleP", new String[]{"article"}, new Object[]{com.getArticle()});
    }

    public void maintenance2() {
        List<YvsProdNomenclature> ln = dao.loadNameQueries("YvsProdNomenclature.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        //trouve le sous produit
        succes();

    }

    public void exporter() {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Liste des nomenclature");

            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            XSSFFont font = sheet.getWorkbook().createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 12);
            cellStyle.setFont(font);

            String[] headers = new String[]{"Reference", "Désignation", "Unite", "Quantitée"};
            int rowCount = 0;
            Row row = sheet.createRow(rowCount++);
            Cell cell;
            for (int i = 0; i < headers.length; i++) {
                cell = row.createCell(i);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(headers[i]);
            }
            List<YvsProdComposantNomenclature> composants;
            List<YvsProdNomenclature> list = dao.loadNameQueries("YvsProdNomenclature.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            for (YvsProdNomenclature item : list) {
                row = sheet.createRow(rowCount++);
                sheet.addMergedRegion(new CellRangeAddress(rowCount - 1, rowCount - 1, 0, headers.length - 1));
                cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(item.getArticle().getDesignation() + "(" + item.getReference() + ")");
                composants = dao.loadNameQueries("YvsProdComposantNomenclature.findByNomenclature", new String[]{"nomenclature"}, new Object[]{item});
                for (YvsProdComposantNomenclature component : composants) {
                    row = sheet.createRow(rowCount++);
                    row.createCell(0).setCellValue(component.getArticle().getRefArt());
                    row.createCell(1).setCellValue(component.getArticle().getDesignation());
                    row.createCell(2).setCellValue(component.getUnite().getUnite().getReference());
                    row.createCell(3).setCellValue(component.getQuantite());
                }
                rowCount++;
            }
            String destination = Initialisation.getCheminResource() + Initialisation.FILE_SEPARATOR + "nomenclature.xlsx";
            try (FileOutputStream outputStream = new FileOutputStream(destination)) {
                workbook.write(outputStream);
            }
            byte[] bytes = Util.read(new File(destination));
            FacesContext faces = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("Content-disposition", "attachment;filename=nomenclature.xlsx");
            response.setContentLength(bytes.length);
            try {
                response.getOutputStream().write(bytes);
                response.getOutputStream().flush();
            } catch (IOException ex) {
                log.log(Level.SEVERE, null, ex);
            }
            faces.responseComplete();
        } catch (IOException ex) {
            getException("exporter", ex);
        }
    }

}
