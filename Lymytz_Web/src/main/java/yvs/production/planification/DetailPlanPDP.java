/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification;

import yvs.base.produits.ArticleFournisseur;
import yvs.base.produits.Articles;


/**
 *
 * @author Lymytz
 */
public class DetailPlanPDP extends DetailPlan {

    private Articles article = new Articles();
    private ArticleFournisseur fournisseur = new ArticleFournisseur();
    private PeriodePlanificationPDP periode = new PeriodePlanificationPDP();

    public DetailPlanPDP() {
    }

    public DetailPlanPDP(long id) {
        super(id);
    }

    public DetailPlanPDP(long id, double valeur) {
        super(id, valeur);
    }

    public DetailPlanPDP(long id, String typeVal, double valeur) {
        super(id, typeVal, valeur);
    }

    public PeriodePlanificationPDP getPeriode() {
        return periode;
    }

    public void setPeriode(PeriodePlanificationPDP periode) {
        this.periode = periode;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public void setFournisseur(ArticleFournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public ArticleFournisseur getFournisseur() {
        return fournisseur;
    }

}
