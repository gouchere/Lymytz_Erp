/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.achat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.CategorieComptable;
import yvs.base.produits.Articles;
import yvs.commercial.UtilCom;
import yvs.base.produits.ManagedArticles;
import yvs.production.UtilProd;
import yvs.base.tiers.Tiers;
import yvs.commercial.ManagedCatCompt;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.fournisseur.Fournisseur;
import yvs.commercial.fournisseur.ManagedFournisseur;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.produits.YvsBaseArticles;
import yvs.util.Constantes;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedBonAchat extends ManagedCommercial<DocAchat, YvsComDocAchats> implements Serializable {

    private DocAchat docAchat = new DocAchat();
    private List<YvsComDocAchats> documents;
    private YvsComDocAchats selectDoc, entityDoc;

    private List<YvsComContenuDocAchat> contenus;
    private YvsComContenuDocAchat selectContenu, entityContenu;
    private ContenuDocAchat contenu = new ContenuDocAchat();

    private String tabIds, tabIds_article, tabIds_contenu;
    private boolean selectArt, listArt;
    // Nombre d'element a afficher dans le selectOneMenu
    private int subLenght;

    public ManagedBonAchat() {
        contenus = new ArrayList<>();
        documents = new ArrayList<>();
    }

    public int getSubLenght() {
        return subLenght;
    }

    public void setSubLenght(int subLenght) {
        this.subLenght = subLenght;
    }

    public boolean isListArt() {
        return listArt;
    }

    public void setListArt(boolean listArt) {
        this.listArt = listArt;
    }

    public boolean isSelectArt() {
        return selectArt;
    }

    public void setSelectArt(boolean selectArt) {
        this.selectArt = selectArt;
    }

    public YvsComDocAchats getEntityDoc() {
        return entityDoc;
    }

    public void setEntityDoc(YvsComDocAchats entityDoc) {
        this.entityDoc = entityDoc;
    }

    public YvsComContenuDocAchat getEntityContenu() {
        return entityContenu;
    }

    public void setEntityContenu(YvsComContenuDocAchat entityContenu) {
        this.entityContenu = entityContenu;
    }

    public List<YvsComContenuDocAchat> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComContenuDocAchat> contenus) {
        this.contenus = contenus;
    }

    public YvsComContenuDocAchat getSelectContenu() {
        return selectContenu;
    }

    public void setSelectContenu(YvsComContenuDocAchat selectContenu) {
        this.selectContenu = selectContenu;
    }

    public YvsComDocAchats getSelectDoc() {
        return selectDoc;
    }

    public void setSelectDoc(YvsComDocAchats selectDoc) {
        this.selectDoc = selectDoc;
    }

    public String getTabIds_contenu() {
        return tabIds_contenu;
    }

    public void setTabIds_contenu(String tabIds_contenu) {
        this.tabIds_contenu = tabIds_contenu;
    }

    public ContenuDocAchat getContenu() {
        return contenu;
    }

    public void setContenu(ContenuDocAchat contenu) {
        this.contenu = contenu;
    }

    public String getTabIds_article() {
        return tabIds_article;
    }

    public void setTabIds_article(String tabIds_article) {
        this.tabIds_article = tabIds_article;
    }

    public DocAchat getDocAchat() {
        return docAchat;
    }

    public void setDocAchat(DocAchat docAchat) {
        this.docAchat = docAchat;
    }

    public List<YvsComDocAchats> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComDocAchats> documents) {
        this.documents = documents;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        _load();
        loadAllBons(true, true);
        initView();
    }

    public void load(Boolean load) {

    }

    public void initView() {
        indiceNumsearch_ = genererPrefixe(Constantes.TYPE_BCA_NAME, currentDepot != null ? currentDepot.getId() : 0);
        if (((docAchat != null) ? docAchat.getFournisseur().getId() < 1 : true)) {
            docAchat = new DocAchat();
            docAchat.setTypeDoc(Constantes.TYPE_BCA);
            if (docAchat.getDocumentLie() == null) {
                docAchat.setDocumentLie(new DocAchat());
            }
            numSearch_ = "";
        }
    }

    private int buildDocByDroit() {
        return 1;
//        if (autoriser("fv_view_all_doc")) {
//            return 1;
//        } else if (autoriser("fv_view_only_doc_agence")) {
//            return 2;
//        } else if (autoriser("fv_view_only_doc_pv")) {
//            return 3;
//        } else {
//            return 4;
//        }
    }

    public void loadAllBons(boolean avance, boolean init) {
        ParametreRequete p;
        switch (buildDocByDroit()) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete("y.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
                paginator.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                p = new ParametreRequete("y.agence", "agence", currentAgence, "=", "AND");
                paginator.addParam(p);
                break;
            case 3: //charge tous les document de l'agence du users
                p = new ParametreRequete("y.agence", "agence", currentUser.getAgence(), "=", "AND");
                paginator.addParam(p);
                break;
            default:    //charge les document dont il est l'auteur du users
                p = new ParametreRequete("y.author", "users", currentUser.getUsers(), "=", "AND");
                paginator.addParam(p);
                break;

        }
        paginator.addParam(new ParametreRequete("y.typeDoc", "typeDoc", Constantes.TYPE_BCA, "=", "AND"));
        documents = paginator.executeDynamicQuery("YvsComDocAchats", "y.dateDoc DESC, y.numDoc DESC", avance, init, (int) imax, dao);
        subLenght = documents.size() > 10 ? 10 : documents.size();
        update("data_bon_achat");
    }

    public void loadEditableBons(boolean avance, boolean init) {
        ParametreRequete p;
        switch (buildDocByDroit()) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete("y.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
                paginator.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                p = new ParametreRequete("y.agence", "agence", currentAgence, "=", "AND");
                paginator.addParam(p);
                break;
            case 3: //charge tous les document de l'agence du users
                p = new ParametreRequete("y.agence", "agence", currentUser.getAgence(), "=", "AND");
                paginator.addParam(p);
                break;
            default:    //charge les document dont il est l'auteur du users
                p = new ParametreRequete("y.author", "users", currentUser.getUsers(), "=", "AND");
                paginator.addParam(p);
                break;

        }
        paginator.addParam(new ParametreRequete("y.typeDoc", "typeDoc", Constantes.TYPE_BCA, "=", "AND"));
        paginator.addParam(new ParametreRequete("y.statut", "statut", Constantes.ETAT_EDITABLE, "=", "AND"));
        documents = paginator.executeDynamicQuery("YvsComDocAchats", "y.dateDoc DESC, y.numDoc DESC", avance, init, (int) imax, dao);
        subLenght = documents.size() > 10 ? 10 : documents.size();
    }

    @Override
    public DocAchat recopieView() {
        docAchat.setTypeDoc(Constantes.TYPE_BCA);
//        return UtilCom.recopieAchat(docAchat, Constantes.TYPE_BCA);
        return docAchat;
    }

    public ContenuDocAchat recopieViewArticle(DocAchat doc) {
        return recopieViewArticle(doc, contenu);
    }

    public ContenuDocAchat recopieViewArticle(DocAchat doc, ContenuDocAchat contenu) {
        ContenuDocAchat c = new ContenuDocAchat();
        c.setActif(contenu.isActif());
        c.setArticle(contenu.getArticle());
        c.setCommentaire(contenu.getCommentaire());
        c.setId(contenu.getId());
        c.setPrixAchat(contenu.getPrixAchat());
        c.setRemiseRecu(contenu.getRemiseRecu());
        c.setQuantiteCommende(contenu.getQuantiteCommende());
        c.setQuantiteRecu(0);
        c.setDateLivraison((contenu.getDateLivraison() != null) ? contenu.getDateLivraison() : new Date());
        c.setDateContenu((contenu.getDateContenu() != null) ? contenu.getDateContenu() : new Date());
        c.setStatut((contenu.getStatut() != null) ? contenu.getStatut() : Constantes.ETAT_EDITABLE);
        c.setLot(contenu.getLot());
        c.setParent(contenu.getParent());
        c.setDocAchat(doc);
        c.setNew_(true);
        return c;
    }

    @Override
    public boolean controleFiche(DocAchat bean) {
        if (!_controleFiche(bean)) {
            return false;
        }
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
            return false;
        }
        return true;
    }

    public boolean _controleFiche(DocAchat bean) {
        if ((bean.getFournisseur() != null) ? bean.getFournisseur().getId() < 1 : true) {
            getErrorMessage("Vous devez indiquer le fournisseur");
            return false;
        }
        if (bean.getNumDoc() == null || bean.getNumDoc().equals("")) {
            String ref = genererReference(Constantes.TYPE_BCA_NAME, bean.getDateDoc());
            if (ref == null || ref.trim().equals("")) {
                return false;
            }
            bean.setNumDoc(ref);
        }
        if (!verifyDateAchat(bean.getDateDoc(), bean.getId() > 0)) {
            return false;
        }
        if (bean.isCloturer()) {
            getErrorMessage("Ce document est deja cloturer");
            return false;
        }
//        return writeInExercice(bean.getDateDoc());
        return true;
    }

    private boolean _controleFiche_(DocAchat bean) {
        if (bean == null) {
            getErrorMessage("Le devez selectionner un document");
            return false;
        }
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
            return false;
        }
        if (bean.isCloturer()) {
            getErrorMessage("Ce document est deja cloturer");
            return false;
        }
//        return writeInExercice(bean.getDateDoc());
        return true;
    }

    private boolean _controleFiche_(YvsComDocAchats bean) {
        if (bean == null) {
            getErrorMessage("vous devez selectionner un document");
            return false;
        }
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
            return false;
        }
        if (bean.getCloturer()) {
            getErrorMessage("Ce document est deja cloturer");
            return false;
        }
//        return writeInExercice(bean.getDateDoc());
        return true;
    }

    public boolean controleFicheArticle(ContenuDocAchat bean) {
        if (bean.getDocAchat() != null ? !bean.getDocAchat().isUpdate() : true) {
            selectDoc = _saveNew();
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                return false;
            }
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner l' article");
            return false;
        }
        return _controleFiche_(bean.getDocAchat());
    }

    @Override
    public void populateView(DocAchat bean) {
        cloneObject(docAchat, bean);
        setMontantTotalDoc(docAchat);
    }

    public void populateViewArticle(ContenuDocAchat bean) {
        bean.getArticle().setStock(dao.stocks(bean.getArticle().getId(), 0, 0, 0, 0, docAchat.getDateDoc(), bean.getConditionnement().getId(), bean.getLot().getId()));
        bean.getArticle().setPua(dao.getPua(bean.getArticle().getId(), 0));
        selectArt = true;
        cloneObject(contenu, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche_();
        update("blog_form_bon_achat");
    }

    public void resetFiche_() {
        docAchat = new DocAchat();

        selectDoc = new YvsComDocAchats();
        entityDoc = new YvsComDocAchats();

        contenus.clear();

        tabIds = "";
        resetFicheArticle();
    }

    public void resetFicheArticle() {
        resetFiche(contenu);
        contenu.setArticle(new Articles());
        contenu.setDocAchat(new DocAchat());
        contenu.setDateSave(new Date());

        entityContenu = new YvsComContenuDocAchat();
        selectContenu = new YvsComContenuDocAchat();

        selectArt = false;
        listArt = false;
        tabIds_article = "";
        update("blog_form_article_bon_achat");
        update("desc_contenu_bon_achat");
    }

    @Override
    public boolean saveNew() {
        YvsComDocAchats selectDoc = _saveNew();
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            this.selectDoc = selectDoc;
            succes();
            actionOpenOrResetAfter(this);
            return true;
        }
        return false;
    }

    public YvsComDocAchats _saveNew() {
        return _saveNew(recopieView());
    }

    public YvsComDocAchats _saveNew(DocAchat bean) {
        try {
            if (controleFiche(bean)) {
                YvsComDocAchats y = UtilCom.buildDocAchat(bean, currentUser, currentAgence);
                if (!bean.isUpdate()) {
                    y.setId(null);
                    y = (YvsComDocAchats) dao.save1(y);
                    docAchat.setId(y.getId());
                    documents.add(0, y);
                } else {
                    dao.update(y);
                    documents.set(documents.indexOf(y), y);
                }
                docAchat.setNumDoc(bean.getNumDoc());
                docAchat.setUpdate(true);
                update("data_bon_achat");
                update("entete_form_bon_achat");
                return y;
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            System.err.println("Error Insertion : " + ex.getMessage());
            return null;
        }
        return null;
    }

    public void saveNewArticle() {
        try {
            ContenuDocAchat bean = recopieViewArticle(docAchat);
            if (controleFicheArticle(bean)) {
                YvsComContenuDocAchat en = UtilCom.buildContenuDocAchat(bean, currentUser);
                if (bean.getId() <= 0) {
                    en.setId(null);
                    en = (YvsComContenuDocAchat) dao.save1(en);
                    contenu.setId(en.getId());
                    contenus.add(0, en);
                } else {
                    dao.update(en);
                    contenus.set(contenus.indexOf(en), en);
                }
                setMontantTotalDoc(docAchat);
                succes();
                resetFicheArticle();
                update("data_bon_achat");
                update("data_article_bon_achat");

            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            System.err.println("Error Insertion : " + ex.getMessage());
        }
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                String[] tab = tabIds.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComDocAchats bean = documents.get(documents.indexOf(new YvsComDocAchats(id)));
                    if (!_controleFiche_(bean)) {
                        return;
                    }
                    dao.delete(new YvsComDocAchats(bean.getId()));
                    if (documents.contains(bean)) {
                        documents.set(documents.indexOf(bean), bean);
                    }
                    if (id == docAchat.getId()) {
                        resetFiche();
                    }
                }
                succes();
                update("data_bon_achat");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsComDocAchats y) {
        selectDoc = y;
    }

    public void deleteBean_() {
        try {
            if (selectDoc != null) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                dao.delete(selectDoc);
                if (documents.contains(selectDoc)) {
                    documents.set(documents.indexOf(selectDoc), selectDoc);
                }
                if (selectDoc.getId() == docAchat.getId()) {
                    resetFiche();
                }
                succes();
                update("data_bon_achat");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanArticle() {
        try {
            if ((tabIds_article != null) ? !tabIds_article.equals("") : false) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                String[] tab = tabIds_article.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComContenuDocAchat bean = contenus.get(contenus.indexOf(new YvsComContenuDocAchat(id)));
                    dao.delete(new YvsComContenuDocAchat(bean.getId()));
                    contenus.remove(bean);
                    if (id == contenu.getId()) {
                        resetFicheArticle();
                    }
                }
                setMontantTotalDoc(docAchat);
                succes();
                update("data_article_bon_achat");
                update("data_bon_achat");

            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanArticle_(YvsComContenuDocAchat y) {
        selectContenu = y;
    }

    public void deleteBeanArticle_() {
        try {
            if (selectContenu != null) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                dao.delete(selectContenu);
                contenus.remove(selectContenu);
                setMontantTotalDoc(docAchat);
                if (selectContenu.getId() == contenu.getId()) {
                    resetFicheArticle();
                }
                succes();
                update("data_article_bon_achat");
                update("data_bon_achat");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void onSelectObject(YvsComDocAchats y) {
        selectDoc = y;
        populateView(UtilCom.buildBeanDocAchat(selectDoc));
        resetFicheArticle();
        contenus = loadContenus(selectDoc);
        update("data_article_bon_achat");
        update("blog_form_bon_achat");
        update("entet_form_bon_achat");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            onSelectObject((YvsComDocAchats) ev.getObject());
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche_();
        update("blog_form_bon_achat");
    }

    public void loadOnViewContenu(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComContenuDocAchat bean = (YvsComContenuDocAchat) ev.getObject();
            populateViewArticle(UtilCom.buildBeanContenuDocAchat(bean));
            update("blog_form_article_bon_achat");
            update("desc_contenu_bon_achat");
        }
    }

    public void unLoadOnViewContenu(UnselectEvent ev) {
        resetFicheArticle();
        update("blog_form_article_bon_achat");
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            chooseArticle(UtilProd.buildBeanArticles(bean));
            listArt = false;
        }
    }

    public void loadOnViewFsseur(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseFournisseur y = (YvsBaseFournisseur) ev.getObject();
            chooseFsseur(UtilCom.buildBeanFournisseur(y));
        }
    }

    public void init(boolean next) {
        loadAllBons(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllBons(true, true);
    }

    public void chooseStatut(ValueChangeEvent ev) {
        statut_ = ((String) ev.getNewValue());
        ParametreRequete p;
        if (statut_ != null ? statut_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statut", "statut", statut_);
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("statut", "statut", null);
        }
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    public void chooseCloturer(ValueChangeEvent ev) {
        cloturer_ = ((Boolean) ev.getNewValue());
        ParametreRequete p = new ParametreRequete("y.cloturer", "cloturer", cloturer_);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    public void chooseDateSearch() {
        ParametreRequete p;
        if (date_) {
            p = new ParametreRequete("y.dateDoc", "dateDoc", dateDebut_);
            p.setOperation("BETWEEN");
            p.setPredicat("AND");
            p.setOtherObjet(dateFin_);
        } else {
            p = new ParametreRequete("y.dateDoc", "dateDoc", null);
        }
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    public void chooseFournisseur() {
        if ((docAchat.getFournisseur() != null) ? docAchat.getFournisseur().getId() > 0 : false) {
            ManagedFournisseur s = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
            if (s != null) {
                YvsBaseFournisseur y = s.getFournisseurs().get(s.getFournisseurs().indexOf(new YvsBaseFournisseur(docAchat.getFournisseur().getId())));
                Fournisseur bean = UtilCom.buildBeanFournisseur(y);
                cloneObject(docAchat.getFournisseur(), bean);
            }
        }
    }

    public void chooseArticle(Articles t) {
        if (((docAchat.getFournisseur() != null) ? docAchat.getFournisseur().getId() > 0 : false)) {
            List<YvsBaseConditionnement> unites = dao.loadNameQueries("YvsBaseConditionnement.findByActifArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(t.getId())});
            t.setConditionnements(unites);
            if (contenu.getConditionnement() != null ? contenu.getConditionnement().getId() > 0 : false) {
                t.setStock(dao.stocks(t.getId(), 0, 0, 0, 0, docAchat.getDateDoc(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
            }
            t.setPua(dao.getPua(t.getId(), docAchat.getFournisseur().getId()));
            cloneObject(contenu.getArticle(), t);
            contenu.setPrixAchat(t.getPua());
            contenu.setRemiseRecu(dao.getRemiseAchat(t.getId(), contenu.getQuantiteCommende(), contenu.getPrixAchat(), docAchat.getFournisseur().getId()));
        } else {
            getErrorMessage("Selectionner dabord une source!");
            resetFicheArticle();
        }
    }

    public void chooseCategorie() {
        if ((docAchat.getCategorieComptable() != null) ? docAchat.getCategorieComptable().getId() > 0 : false) {
            ManagedCatCompt m = (ManagedCatCompt) giveManagedBean(ManagedCatCompt.class);
            if (m != null) {
                YvsBaseCategorieComptable d_ = m.getCategories().get(m.getCategories().indexOf(new YvsBaseCategorieComptable(docAchat.getCategorieComptable().getId())));
                CategorieComptable d = UtilCom.buildBeanCategorieComptable(d_);
                cloneObject(docAchat.getCategorieComptable(), d);
            }
        } else {
            docAchat.setCategorieComptable(new CategorieComptable());
        }
        setMontantTotalDoc(docAchat);
    }

    public void onQuantiteBlur() {
        double total = contenu.getQuantiteCommende() * contenu.getPrixAchat();
        double _remise = dao.getRemiseAchat(contenu.getArticle().getId(), contenu.getQuantiteCommende(), contenu.getPrixAchat(), docAchat.getFournisseur().getId());
        if (entityContenu != null ? entityContenu.getId() > 0 : false) {
            if (contenu.getId() > 0 && (entityContenu.getQuantiteRecu() == contenu.getQuantiteCommende())) {
                _remise = entityContenu.getRemise();
            }
        }
        contenu.setRemiseRecu(_remise);
        contenu.setPrixTotalRecu(total - contenu.getRemiseRecu());
        if (docAchat.getCategorieComptable() != null ? docAchat.getCategorieComptable().getId() > 0 : false) {
            contenu.setTaxe(dao.getTaxe(contenu.getArticle().getId(), docAchat.getCategorieComptable().getId(), 0, contenu.getRemiseRecu(), contenu.getQuantiteCommende(), contenu.getPrixAchat(), false, docAchat.getFournisseur().getId()));
            contenu.setPrixTotalRecu(contenu.getPrixTotalRecu() + contenu.getTaxe());
        } else {
            getWarningMessage("Selectionner la catégorie comptable!");
        }
    }

    public List<String> completeText(String query) {
        List<String> results = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            results.add(query + i);
        }
        return results;
    }

    public void sendByMail() {
        if ((entityDoc != null) ? entityDoc.getId() > 0 : false) {
            YvsBaseFournisseur fsseur = entityDoc.getFournisseur();
            String email = fsseur.getTiers().getEmail();
            if ((email != null) ? !email.equals("") : false) {
                if (Util.correctEmail(email)) {
                    transmisOrder();
                } else {
                    getErrorMessage("Impossible d'envoyer! Email Incorrect");
                }
            } else {
                getErrorMessage("Impossible d'envoyer! Le fournisseur n'a pas d'email");
            }
        }
    }

    public void annulerOrder() {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            List<YvsComDocAchats> l = dao.loadNameQueries("YvsComDocAchats.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
            if (l != null ? l.isEmpty() : true) {
                docAchat.setStatut(Constantes.ETAT_EDITABLE);
                selectDoc.setCloturer(false);
                selectDoc.setAnnulerBy(null);
                selectDoc.setValiderBy(null);
                selectDoc.setDateAnnuler(null);
                selectDoc.setDateCloturer(null);
                selectDoc.setDateValider(null);
                selectDoc.setAuthor(currentUser);
                selectDoc.setStatut(Constantes.ETAT_EDITABLE);
                dao.update(selectDoc);
                if (documents.contains(selectDoc)) {
                    documents.set(documents.indexOf(selectDoc), selectDoc);
                }
                update("data_bon_achat");
                update("grp_btn_etat_bon_achat");
            } else {
                for (YvsComDocAchats d : l) {
                    if (!d.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                        getErrorMessage("Impossible d'annuler cet ordre car il possède un transfert déja valide");
                        return;
                    }
                }
                openDialog("dlgConfirmAnnuler");
            }
        }
    }

    public void annulerOrder_() {
        try {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                List<YvsComDocAchats> l = dao.loadNameQueries("YvsComDocAchats.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
                for (YvsComDocAchats d : l) {
                    dao.delete(d);
                }
                docAchat.setStatut(Constantes.ETAT_EDITABLE);
                selectDoc.setCloturer(false);
                selectDoc.setAnnulerBy(null);
                selectDoc.setValiderBy(null);
                selectDoc.setDateAnnuler(null);
                selectDoc.setDateCloturer(null);
                selectDoc.setDateValider(null);
                selectDoc.setAuthor(currentUser);
                selectDoc.setStatut(Constantes.ETAT_EDITABLE);
                dao.update(selectDoc);
                if (documents.contains(selectDoc)) {
                    documents.set(documents.indexOf(selectDoc), selectDoc);
                }
                update("data_bon_achat");
                update("grp_btn_etat_bon_achat");
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            System.err.println("Erreur : " + ex.getMessage());
        }
    }

    public void refuserOrder() {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            List<YvsComDocAchats> l = dao.loadNameQueries("YvsComDocAchats.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
            if (l != null ? l.isEmpty() : true) {
                docAchat.setStatut(Constantes.ETAT_ANNULE);
                selectDoc.setCloturer(false);
                selectDoc.setAnnulerBy(currentUser.getUsers());
                selectDoc.setValiderBy(null);
                selectDoc.setDateAnnuler(new Date());
                selectDoc.setDateCloturer(null);
                selectDoc.setDateValider(null);
                selectDoc.setAuthor(currentUser);
                selectDoc.setStatut(Constantes.ETAT_ANNULE);
                dao.update(selectDoc);
                if (documents.contains(selectDoc)) {
                    documents.set(documents.indexOf(selectDoc), selectDoc);
                }
                update("data_bon_achat");
                update("grp_btn_etat_bon_achat");
            } else {
                for (YvsComDocAchats d : l) {
                    if (!d.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                        getErrorMessage("Impossible d'annuler cet ordre car il possède un transfert déja valide");
                        return;
                    }
                }
                openDialog("dlgConfirmRefuser");
            }
        }
    }

    public void refuserOrder_() {
        try {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                List<YvsComDocAchats> l = dao.loadNameQueries("YvsComDocAchats.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
                for (YvsComDocAchats d : l) {
                    dao.delete(d);
                }
                docAchat.setStatut(Constantes.ETAT_ANNULE);
                selectDoc.setCloturer(false);
                selectDoc.setAnnulerBy(currentUser.getUsers());
                selectDoc.setValiderBy(null);
                selectDoc.setDateAnnuler(new Date());
                selectDoc.setDateCloturer(null);
                selectDoc.setDateValider(null);
                selectDoc.setAuthor(currentUser);
                selectDoc.setStatut(Constantes.ETAT_ANNULE);
                dao.update(selectDoc);
                if (documents.contains(selectDoc)) {
                    documents.set(documents.indexOf(selectDoc), selectDoc);
                }
                update("data_bon_achat");
                update("grp_btn_etat_bon_achat");
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            System.err.println("Erreur : " + ex.getMessage());
        }
    }

    public void validerOrder() {
        if (selectDoc == null) {
            return;
        }
        if (!_controleFiche(docAchat)) {
            return;
        }
        docAchat.setStatut(Constantes.ETAT_VALIDE);
        selectDoc.setCloturer(false);
        selectDoc.setAnnulerBy(null);
        selectDoc.setDateAnnuler(null);
        selectDoc.setDateCloturer(null);
        selectDoc.setValiderBy(currentUser.getUsers());
        selectDoc.setDateValider(new Date());
        selectDoc.setAuthor(currentUser);
        selectDoc.setStatut(Constantes.ETAT_VALIDE);
        dao.update(selectDoc);
        if (documents.contains(selectDoc)) {
            documents.set(documents.indexOf(selectDoc), selectDoc);
        }
        update("data_bon_achat");
        update("grp_btn_etat_bon_achat");
    }

    public void transmisOrder() {
        if (selectDoc == null) {
            return;
        }
        if (!_controleFiche(docAchat)) {
            return;
        }
        docAchat.setStatut(Constantes.ETAT_SOUMIS);
        selectDoc.setStatut(Constantes.ETAT_SOUMIS);
        selectDoc.setAuthor(currentUser);
        dao.update(selectDoc);
        if (documents.contains(selectDoc)) {
            documents.set(documents.indexOf(selectDoc), selectDoc);
        }
        update("data_bon_achat");
        update("grp_btn_etat_bon_achat");
    }

    public void cloturer(YvsComDocAchats y) {
        selectDoc = y;
        update("id_confirm_close_bca");
    }

    public void cloturer() {
        if (selectDoc == null) {
            return;
        }
        docAchat.setCloturer(!docAchat.isCloturer());
        selectDoc.setCloturer(docAchat.isCloturer());
        selectDoc.setDateCloturer(docAchat.isCloturer() ? new Date() : null);
        selectDoc.setAuthor(currentUser);
        dao.update(selectDoc);
        if (documents.contains(selectDoc)) {
            documents.set(documents.indexOf(selectDoc), selectDoc);
        }
        update("data_bon_achat");
    }

    public void searchTranferts() {
        ParametreRequete p;
        if (numSearch_ != null ? numSearch_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.numDoc", "numDoc", "%" + numSearch_ + "%");
            p.setOperation(" LIKE ");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.numDoc", "numDoc", null);
        }
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    public void searchArticle() {
        String num = contenu.getArticle().getRefArt();
        contenu.getArticle().setDesignation("");
        contenu.getArticle().setError(true);
        contenu.getArticle().setId(0);
        listArt = false;
        selectArt = false;
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
            if (m != null) {
                Articles y = m.searchArticleActif("A", num, true);
                if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                    if (m.getArticlesResult().size() > 1) {
                        update("data_articles_bon_achat");
                    } else {
                        chooseArticle(y);
                    }
                    contenu.getArticle().setError(false);
                }
                listArt = y.isListArt();
                selectArt = y.isSelectArt();
            }
        }
    }

    public void initArticles() {
        listArt = false;
        ManagedArticles a = (ManagedArticles) giveManagedBean("Marticle");
        if (a != null) {
            a.initArticles("A", contenu.getArticle());
            listArt = contenu.getArticle().isListArt();
        }
        update("data_articles_bon_achat");
    }

    public void chooseFsseur(Fournisseur d) {
        if ((d != null) ? d.getId() > 0 : false) {
            cloneObject(docAchat.getFournisseur(), d);
            if (d.getCategorieComptable() != null) {
                cloneObject(docAchat.getCategorieComptable(), d.getCategorieComptable());
                update("select_categorie_comptable_bon_achat");
            }
            if (docAchat.getFournisseur() != null ? docAchat.getFournisseur().getModel() != null : false) {
                docAchat.setModeReglement(docAchat.getFournisseur().getModel());
            }
            update("select_model_bon_achat");
        }
        update("infos_bon_achat");
    }

    public void searchFsseur() {
        String num = docAchat.getFournisseur().getCodeFsseur();
        docAchat.getFournisseur().setId(0);
        docAchat.getFournisseur().setError(true);
        docAchat.getFournisseur().setTiers(new Tiers());
        ManagedFournisseur m = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
        if (m != null) {
            Fournisseur y = m.searchFsseur(num, true);
            if (m.getFournisseurs() != null ? !m.getFournisseurs().isEmpty() : false) {
                if (m.getFournisseurs().size() > 1) {
                    update("data_fsseur_bon_achat");
                } else {
                    chooseFsseur(y);
                }
                docAchat.getFournisseur().setError(false);
            }
        }
    }

    public void initFsseurs() {
        ManagedFournisseur m = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
        if (m != null) {
            m.initFsseurs(docAchat.getFournisseur());
        }
        update("data_fsseur_bon_achat");
    }

    public void removeDoublon(YvsComDocAchats y) {
        selectDoc = y;
        removeDoublon();
    }

    public void removeDoublon() {
        if ((entityDoc != null) ? entityDoc.getId() > 0 : false) {
            if (!entityDoc.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Le document doit etre editable pour pouvoir etre modifié");
                return;
            }
            removeDoublonAchat(entityDoc.getId());
            succes();
        }
    }

    @Override
    public void cleanAchat() {
        super.cleanAchat();
        loadAllBons(true, true);
    }

    public void equilibre(YvsComDocAchats y) {
        selectDoc = y;
        equilibre();
    }

    public void equilibre() {
        if ((selectDoc != null) ? selectDoc.getId() > 0 : false) {
            Map<String, String> statuts = dao.getEquilibreAchat(selectDoc.getId());
            if (statuts != null) {
                selectDoc.setStatutLivre(statuts.get("statut_livre"));
                selectDoc.setStatutRegle(statuts.get("statut_regle"));
            }
            succes();
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
