/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification;

import yvs.production.technique.GammeArticle;
import yvs.production.base.OperationsGamme;

/**
 *
 * @author Lymytz
 */
public class DetailPlanPDC_ extends DetailPlanPDP {

    private DetailPlanPDP pdp = new DetailPlanPDP();
    private GammeArticle gamme = new GammeArticle();
    private OperationsGamme phase = new DetailPlanPDC();

    public DetailPlanPDP getPdp() {
        return pdp;
    }

    public void setPdp(DetailPlanPDP pdp) {
        this.pdp = pdp;
    }

    public GammeArticle getGamme() {
        return gamme;
    }

    public void setGamme(GammeArticle gamme) {
        this.gamme = gamme;
    }

    public OperationsGamme getPhase() {
        return phase;
    }

    public void setPhase(OperationsGamme phase) {
        this.phase = phase;
    }

}
