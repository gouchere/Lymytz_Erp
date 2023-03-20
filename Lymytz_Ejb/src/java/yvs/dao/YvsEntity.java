/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao;

import yvs.entity.users.YvsUsersAgence;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author Lymytz Dowes
 */
public abstract class YvsEntity implements Serializable {

    @Size(max = 2147483647)
    @Column(name = "execute_trigger")
    private String executeTrigger;
    @Transient
    protected boolean reload = true;
    @Transient
    protected String adresseServeur;

    @Transient
    protected long idDistant;

    public long getIdDistant() {
        return idDistant;
    }

    public void setIdDistant(long idDistant) {
        this.idDistant = idDistant;
    }

    public String getAdresseServeur() {
        return adresseServeur;
    }

    public void setAdresseServeur(String adresseServeur) {
        this.adresseServeur = adresseServeur;
    }

    public String getExecuteTrigger() {
        return executeTrigger;
    }

    public void setExecuteTrigger(String executeTrigger) {
        this.executeTrigger = executeTrigger;
    }

    public boolean isReload() {
        return reload;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    public Long getId() {
        return 0L;
    }

    public abstract YvsUsersAgence getAuthor();

}
