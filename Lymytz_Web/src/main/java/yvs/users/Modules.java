/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users;

import java.io.Serializable;import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Modules implements Serializable {

    private Integer id;
    private String libelle;
    private String description;
    private String reference;
    private List<PageModule> PageModuleList;
    private boolean acces;

    public Modules() {
        PageModuleList = new ArrayList<>();
    }

    public Modules(Integer id) {
        this.id = id;
        PageModuleList = new ArrayList<>();
    }

    public void setAcces(boolean acces) {
        this.acces = acces;
    }

    public boolean isAcces() {
        return acces;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<PageModule> getPageModuleList() {
        return PageModuleList;
    }

    public void setPageModuleList(List<PageModule> PageModuleList) {
        this.PageModuleList = PageModuleList;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final Modules other = (Modules) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
