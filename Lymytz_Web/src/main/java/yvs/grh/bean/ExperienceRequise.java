/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import yvs.parametrage.poste.PosteDeTravail;

/**
 *
 * @author LYMYTZ-PC
 */
public class ExperienceRequise {

    private long id;
    private PosteDeTravail poste;
    private int duree;

    public ExperienceRequise() {
    }

    public ExperienceRequise(long id, PosteDeTravail poste, int duree) {
        this.id = id;
        this.poste = poste;
        this.duree = duree;
    }

    public PosteDeTravail getPoste() {
        return poste;
    }

    public void setPoste(PosteDeTravail poste) {
        this.poste = poste;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        final ExperienceRequise other = (ExperienceRequise) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
