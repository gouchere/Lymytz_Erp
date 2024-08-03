/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lymytz
 */
public class PeriodePlanificationPIC extends PeriodePlanification {

    private List<DetailPlanPIC> details;

    public PeriodePlanificationPIC() {
        details = new ArrayList<>();
    }

    public PeriodePlanificationPIC(int id) {
        super(id);
        details = new ArrayList<>();
    }

    public List<DetailPlanPIC> getDetails() {
        return details;
    }

    public void setDetails(List<DetailPlanPIC> details) {
        this.details = details;
    }

}
