/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.base;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.mutuelle.CaisseMutuelle;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Reglement implements Serializable {

    private long id;
    private Date dateReglement = new Date();
    private double montant;
    private CaisseMutuelle caisse = new CaisseMutuelle();
    private boolean selectActif, new_;
    private char statutPiece;

    public Reglement() {
    }

    public Reglement(long id) {
        this.id = id;
    }

    public char getStatutPiece() {
        return statutPiece;
    }

    public void setStatutPiece(char statutPiece) {
        this.statutPiece = statutPiece;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateReglement() {
        return dateReglement;
    }

    public void setDateReglement(Date dateReglement) {
        this.dateReglement = dateReglement;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Reglement other = (Reglement) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
