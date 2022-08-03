/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.production.planification;

import yvs.base.produits.FamilleArticle;

/**
 *
 * @author Lymytz
 */
public class DetailPlanPIC extends DetailPlan {
    
    private FamilleArticle famille = new FamilleArticle();

    public FamilleArticle getFamille() {
        return famille;
    }

    public void setFamille(FamilleArticle famille) {
        this.famille = famille;
    }

    public DetailPlanPIC() {
    }

    public DetailPlanPIC(int id) {
        super(id);
    }

    public DetailPlanPIC(int id, double valeur) {
        super(id, valeur);
    }
}
