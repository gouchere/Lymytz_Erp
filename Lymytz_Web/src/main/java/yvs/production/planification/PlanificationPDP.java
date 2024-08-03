/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz
 */
@ManagedBean
@SessionScoped
public class PlanificationPDP extends Planification implements Serializable {

    private List<PeriodePlanificationPDP> periodes;

    public PlanificationPDP() {
        periodes = new ArrayList<>();
    }

    public PlanificationPDP(long id) {
        super(id);
        periodes = new ArrayList<>();
    }

    public List<PeriodePlanificationPDP> getPeriodes() {
        return periodes;
    }

    public void setPeriodes(List<PeriodePlanificationPDP> periodes) {
        this.periodes = periodes;
    }

}
