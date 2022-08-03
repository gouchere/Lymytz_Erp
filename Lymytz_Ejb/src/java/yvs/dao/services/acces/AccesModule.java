/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.services.acces;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class AccesModule implements Serializable {

    //Liste des modules
    private boolean param_, grh_, com_, prod_, compta_, client_, mutuel_, stat_, base_, proj_;

    public AccesModule() {
    }

    public boolean isParam_() {
        return param_;
    }

    public void setParam_(boolean param_) {
        this.param_ = param_;
    }

    public boolean isGrh_() {
        return grh_;
    }

    public void setGrh_(boolean grh_) {
        this.grh_ = grh_;
    }

    public boolean isCom_() {
        return com_;
    }

    public void setCom_(boolean com_) {
        this.com_ = com_;
    }

    public boolean isProd_() {
        return prod_;
    }

    public void setProd_(boolean prod_) {
        this.prod_ = prod_;
    }

    public boolean isCompta_() {
        return compta_;
    }

    public void setCompta_(boolean compta_) {
        this.compta_ = compta_;
    }

    public boolean isClient_() {
        return client_;
    }

    public void setClient_(boolean client_) {
        this.client_ = client_;
    }

    public boolean isMutuel_() {
        return mutuel_;
    }

    public void setMutuel_(boolean mutuel_) {
        this.mutuel_ = mutuel_;
    }

    public boolean isStat_() {
        return stat_;
    }

    public void setStat_(boolean stat_) {
        this.stat_ = stat_;
    }

    public boolean isBase_() {
        return base_;
    }

    public void setBase_(boolean base_) {
        this.base_ = base_;
    }

    public boolean isProj_() {
        return proj_;
    }

    public void setProj_(boolean proj_) {
        this.proj_ = proj_;
    }

    @PostConstruct
    public void init() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        } catch (Exception ex) {
        }
    }
}
