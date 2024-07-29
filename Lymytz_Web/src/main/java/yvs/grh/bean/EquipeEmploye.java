/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.grh.personnel.YvsGrhEmployes;

/**
 *
 * @author hp Elite 8300
 */ 
@ManagedBean
@SessionScoped
public class EquipeEmploye implements Serializable {

    private int id;
    private String titreEquipe;
    private String groupeService;
    private List<YvsGrhEmployes> employes;
    private boolean actif=true;

    public EquipeEmploye() {
        employes = new ArrayList<>();
    }

    public EquipeEmploye(int id, String titreEquipe) {
        this.id = id;
        this.titreEquipe = titreEquipe;
        employes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitreEquipe() {
        return titreEquipe;
    }

    public void setTitreEquipe(String titreEquipe) {
        this.titreEquipe = titreEquipe;
    }

    public List<YvsGrhEmployes> getEmployes() {
        return employes;
    }

    public void setEmployes(List<YvsGrhEmployes> employes) {
        this.employes = employes;
    }

    public String getGroupeService() {
        return groupeService;
    }

    public void setGroupeService(String groupeService) {
        this.groupeService = groupeService;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + this.id;
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
        final EquipeEmploye other = (EquipeEmploye) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
