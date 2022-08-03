/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.proj.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.proj.projet.YvsProjProjetService;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class Projet implements Serializable {

    private long id;
    private String code;
    private String libelle;
    private String description;
    private String codeAcces;
    private boolean actif = true;
    private boolean direct = false;
    private Date dateSave = new Date();
    private Projet parent;
    private List<YvsProjProjetService> services;

    public Projet() {
        services = new ArrayList<>();
    }

    public Projet(long id) {
        this();
        this.id = id;
    }

    public Projet(long id, String libelle) {
        this(id);
        this.libelle = libelle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(String codeAcces) {
        this.codeAcces = codeAcces;
    }

    public Projet getParent() {
        return parent;
    }

    public void setParent(Projet parent) {
        this.parent = parent;
    }

    public List<YvsProjProjetService> getServices() {
        return services;
    }

    public void setServices(List<YvsProjProjetService> services) {
        this.services = services;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Projet other = (Projet) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
