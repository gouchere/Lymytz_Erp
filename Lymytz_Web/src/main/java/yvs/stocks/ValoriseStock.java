/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stocks;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Date;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.produits.YvsBaseArticles;
import yvs.util.Constantes;

/**
 *
 * @author GOUCHERE YVES
 */
public class ValoriseStock implements Serializable {

    public ValoriseStock() {
    }

    public double calculPRCMPU(double stock, YvsBaseArticles art, YvsBaseDepots dep, Date date, double qte, double prix, double lastPr) {
        double pr = 0;
        if (!Double.isNaN(lastPr) && !Double.isInfinite(lastPr)) {
            pr = ((stock * lastPr) + (qte * prix)) / (stock + qte);
        } else {
            pr = ((stock * art.getPua()) + (qte * prix)) / (stock + qte);
        }
        if (pr <= 0) {
            pr = lastPr;
        }
        return Constantes.arrondiA2Chiffre(pr);
    }
}
