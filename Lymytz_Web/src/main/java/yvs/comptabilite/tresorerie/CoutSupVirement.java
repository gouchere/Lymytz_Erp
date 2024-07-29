/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.tresorerie;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.stock.CoutSupDoc;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class CoutSupVirement extends CoutSupDoc implements Serializable {

    private PieceTresorerie virement = new PieceTresorerie();

    public CoutSupVirement() {
    }

    public CoutSupVirement(long id) {
        super(id);
    }

    public PieceTresorerie getVirement() {
        return virement;
    }

    public void setVirement(PieceTresorerie virement) {
        this.virement = virement;
    }

}
