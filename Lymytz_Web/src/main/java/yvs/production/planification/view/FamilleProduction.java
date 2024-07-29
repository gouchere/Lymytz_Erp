/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification.view;

import yvs.base.produits.FamilleArticle;

/**
 *
 * @author hp Elite 8300
 */
public class FamilleProduction extends DataLigne {

    private long id;
    private FamilleArticle famille;

    public FamilleProduction() {
    }

    public FamilleProduction(FamilleArticle famille) {
        this.famille = famille;
    }

    public FamilleArticle getFamille() {
        return famille;
    }

    public void setFamille(FamilleArticle famille) {
        this.famille = famille;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final FamilleProduction other = (FamilleProduction) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
