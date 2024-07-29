/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.technique;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.production.base.OperationsGamme;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Gamme implements Serializable {
    private List<OperationsGamme> phases;

    public Gamme() {
        phases = new ArrayList<>();
    }

    public List<OperationsGamme> getPhases() {
        return phases;
    }

    public void setPhases(List<OperationsGamme> phases) {
        this.phases = phases;
    }

}
