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
public class PlanificationPIC extends Planification implements Serializable {

    private List<PeriodePlanificationPIC> periodes;

    public PlanificationPIC() {
        periodes = new ArrayList<>();
    }

    public PlanificationPIC(long id) {
        super(id);
        periodes = new ArrayList<>();
    }

    public List<PeriodePlanificationPIC> getPeriodes() {
        return periodes;
    }

    public void setPeriodes(List<PeriodePlanificationPIC> periodes) {
        this.periodes = periodes;
    }
}
