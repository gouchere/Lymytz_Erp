/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class Langues {

    private int id;
    private String langue;
    private boolean lu, ecrit, parle;
    private boolean save;

    public Langues() {
    }

    public Langues(int id) {
        this.id = id;
    }

    public Langues(int id, String langue) {
        this.id = id;
        this.langue = langue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public boolean isLu() {
        return lu;
    }

    public void setLu(boolean lu) {
        this.lu = lu;
    }

    public boolean isEcrit() {
        return ecrit;
    }

    public void setEcrit(boolean ecrit) {
        this.ecrit = ecrit;
    }

    public boolean isParle() {
        return parle;
    }

    public void setParle(boolean parle) {
        this.parle = parle;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + this.id;
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
        final Langues other = (Langues) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
