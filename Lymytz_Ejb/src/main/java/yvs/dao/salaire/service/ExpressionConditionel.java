/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.salaire.service;

/**
 *
 * @author hp Elite 8300
 */
public class ExpressionConditionel {

    private String condition;
    private String expression;
    private double trancheMin, trancheMax;
    private boolean conditionOk;
    private boolean fonction;

    public ExpressionConditionel() {
    }

    public ExpressionConditionel(String condition, String expression) {
        this.condition = condition;
        this.expression = expression;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public double getTrancheMin() {
        return trancheMin;
    }

    public void setTrancheMin(double trancheMin) {
        this.trancheMin = trancheMin;
    }

    public double getTrancheMax() {
        return trancheMax;
    }

    public void setTrancheMax(double trancheMax) {
        this.trancheMax = trancheMax;
    }

    public boolean isConditionOk() {
        return conditionOk;
    }

    public void setConditionOk(boolean conditionOk) {
        this.conditionOk = conditionOk;
    }

    public boolean isFonction() {
        return fonction;
    }

    public void setFonction(boolean fonction) {
        this.fonction = fonction;
    }

}
