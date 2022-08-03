/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification;

import yvs.base.produits.Articles;
import yvs.production.technique.GammeArticle;
import yvs.production.base.OperationsGamme;

/**
 *
 * @author Lymytz
 */
public class DetailPlanPDC extends OperationsGamme {

    private long id_;
    private Articles article = new Articles();
    private GammeArticle gammeArticle = new GammeArticle();
    private OperationsGamme phase = new OperationsGamme();
    private DetailPlanPDP pdp = new DetailPlanPDP();

    public DetailPlanPDC() {
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public GammeArticle getGammeArticle() {
        return gammeArticle;
    }

    public void setGammeArticle(GammeArticle gammeArticle) {
        this.gammeArticle = gammeArticle;
    }

    public DetailPlanPDC(long id) {
        this.id_ = id;
    }

    public OperationsGamme getPhase() {
        return phase;
    }

    public void setPhase(OperationsGamme phase) {
        this.phase = phase;
    }

    public long getId_() {
        return id_;
    }

    public void setId_(long id_) {
        this.id_ = id_;
    }

    public DetailPlanPDP getPdp() {
        return pdp;
    }

    public void setPdp(DetailPlanPDP pdp) {
        this.pdp = pdp;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (int) (this.id_ ^ (this.id_ >>> 32));
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
        final DetailPlanPDC other = (DetailPlanPDC) obj;
        if (this.id_ != other.id_) {
            return false;
        }
        return true;
    }

}
