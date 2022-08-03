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
public class PeriodePlanificationPDP extends PeriodePlanification {

    private List<DetailPlanPDP> details;

    public PeriodePlanificationPDP() {
        details = new ArrayList<>();
    }

    public PeriodePlanificationPDP(int id) {
        super(id);
        details = new ArrayList<>();
    }

    public List<DetailPlanPDP> getDetails() {
        return details;
    }

    public void setDetails(List<DetailPlanPDP> details) {
        this.details = details;
    }

}
