/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.analytique;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.comptabilite.tresorerie.CoutSupVirement;
import yvs.comptabilite.tresorerie.PieceTresorerie;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class CentreCoutVirement extends SectionAnalytique implements Serializable {

    private CoutSupVirement cout = new CoutSupVirement();

    public CentreCoutVirement() {
    }

    public CentreCoutVirement(long id) {
        super(id);
    }

    public CoutSupVirement getCout() {
        return cout;
    }

    public void setCout(CoutSupVirement cout) {
        this.cout = cout;
    }

}
