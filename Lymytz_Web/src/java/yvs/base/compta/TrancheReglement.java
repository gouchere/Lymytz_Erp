/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class TrancheReglement implements Serializable {

    private long id;
    private int numero;
    private double taux;
    private int intervalJour;
    private Date dateUpdate = new Date();
    private ModelReglement model = new ModelReglement();
    private ModeDeReglement mode = new ModeDeReglement();
    private boolean selectActif, new_, update;

    public TrancheReglement() {
    }

    public TrancheReglement(long id) {
        this.id = id;
    }

    public ModeDeReglement getMode() {
        return mode;
    }

    public void setMode(ModeDeReglement mode) {
        this.mode = mode;
    }

    public ModelReglement getModel() {
        return model;
    }

    public void setModel(ModelReglement model) {
        this.model = model;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }

    public int getIntervalJour() {
        return intervalJour;
    }

    public void setIntervalJour(int intervalJour) {
        this.intervalJour = intervalJour;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isUpdate() {
        return id > 0;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TrancheReglement other = (TrancheReglement) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
