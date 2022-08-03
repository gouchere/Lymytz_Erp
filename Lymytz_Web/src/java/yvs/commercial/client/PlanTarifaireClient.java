/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.client;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.base.produits.FamilleArticle;
import yvs.base.produits.TemplateArticles;
import yvs.entity.commercial.client.YvsBasePlanTarifaireTranche;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class PlanTarifaireClient implements Serializable {

    private long id;
    private double puv, puvMin;
    private double remise, ristourne;
    private double coefAugmentation;
    private String naturePrixMin = Constantes.NATURE_MTANT;
    private String natureCoefAugmentation = Constantes.NATURE_MTANT;
    private String natureRemise = Constantes.NATURE_MTANT;
    private String natureRistourne = Constantes.NATURE_MTANT;
    private Conditionnement conditionnement = new Conditionnement();
    private Articles article = new Articles();
    private FamilleArticle famille = new FamilleArticle();
    private TemplateArticles template = new TemplateArticles();
    private CategorieClient categorie = new CategorieClient();
    private List<YvsBasePlanTarifaireTranche> grilles;
    private boolean selectActif, new_, actif = true, update, generation, forArticle = true, applyOnArticle = true;
    private Date dateSave = new Date();

    public PlanTarifaireClient() {
    }

    public PlanTarifaireClient(long id) {
        this();
        this.id = id;
    }

    public PlanTarifaireClient(long id, PlanTarifaireClient y) {
        this.id = id;
        this.puv = y.puv;
        this.puvMin = y.puvMin;
        this.remise = y.remise;
        this.ristourne = y.ristourne;
        this.coefAugmentation = y.coefAugmentation;
        this.grilles = y.grilles;
        this.selectActif = y.selectActif;
        this.new_ = y.new_;
        this.update = y.update;
        this.generation = y.generation;
        this.applyOnArticle = y.applyOnArticle;
    }

    public String getNaturePrixMin() {
        return naturePrixMin != null ? (naturePrixMin.trim().length() > 0 ? naturePrixMin : yvs.dao.salaire.service.Constantes.NATURE_MTANT) : yvs.dao.salaire.service.Constantes.NATURE_MTANT;
    }

    public void setNaturePrixMin(String naturePrixMin) {
        this.naturePrixMin = naturePrixMin;
    }

    public boolean isGeneration() {
        return generation;
    }

    public void setGeneration(boolean generation) {
        this.generation = generation;
    }

    public double getRistourne() {
        return ristourne;
    }

    public void setRistourne(double ristourne) {
        this.ristourne = ristourne;
    }

    public boolean isApplyOnArticle() {
        return applyOnArticle;
    }

    public void setApplyOnArticle(boolean applyOnArticle) {
        this.applyOnArticle = applyOnArticle;
    }

    public double getPuvMin() {
        return puvMin;
    }

    public void setPuvMin(double puvMin) {
        this.puvMin = puvMin;
    }

    public CategorieClient getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieClient categorie) {
        this.categorie = categorie;
    }

    public FamilleArticle getFamille() {
        return famille;
    }

    public void setFamille(FamilleArticle famille) {
        this.famille = famille;
    }

    public boolean isForArticle() {
        return forArticle;
    }

    public void setForArticle(boolean forArticle) {
        this.forArticle = forArticle;
    }

    public boolean isUpdate() {
        return id > 0;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCoefAugmentation() {
        return coefAugmentation;
    }

    public void setCoefAugmentation(double coefAugmentation) {
        this.coefAugmentation = coefAugmentation;
    }

    public String getNatureRemise() {
        return natureRemise != null ? (natureRemise.trim().length() > 0 ? natureRemise : Constantes.NATURE_TAUX) : Constantes.NATURE_TAUX;
    }

    public void setNatureRemise(String natureRemise) {
        this.natureRemise = natureRemise;
    }

    public String getNatureRistourne() {
        return natureRistourne != null ? (natureRistourne.trim().length() > 0 ? natureRistourne : Constantes.NATURE_TAUX) : Constantes.NATURE_TAUX;
    }

    public void setNatureRistourne(String natureRistourne) {
        this.natureRistourne = natureRistourne;
    }

    public String getNatureCoefAugmentation() {
        return natureCoefAugmentation != null ? (natureCoefAugmentation.trim().length() > 0 ? natureCoefAugmentation : Constantes.NATURE_TAUX) : Constantes.NATURE_TAUX;
    }

    public void setNatureCoefAugmentation(String natureCoefAugmentation) {
        this.natureCoefAugmentation = natureCoefAugmentation;
    }

    public double getPuv() {
        return puv;
    }

    public void setPuv(double puv) {
        this.puv = puv;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public TemplateArticles getTemplate() {
        return template;
    }

    public void setTemplate(TemplateArticles template) {
        this.template = template;
    }

    public List<YvsBasePlanTarifaireTranche> getGrilles() {
        return grilles;
    }

    public void setGrilles(List<YvsBasePlanTarifaireTranche> grilles) {
        this.grilles = grilles;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlanTarifaireClient other = (PlanTarifaireClient) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
