/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.theme;

/**
 *
 * @author LYMYTZ
 */
import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.compta.YvsBasePlanComptable;

@ManagedBean(name = "compteService", eager = true)
@SessionScoped
public class CompteService implements Serializable{

    private List<YvsBasePlanComptable> comptes;

    public CompteService() {
        comptes = new ArrayList<>();
    }

    public CompteService(List<YvsBasePlanComptable> comptes) {
        this.comptes = comptes;
    }

    public List<YvsBasePlanComptable> getComptes() {
        return comptes;
    }

    public void setComptes(List<YvsBasePlanComptable> comptes) {
        this.comptes = comptes;
    }
}
