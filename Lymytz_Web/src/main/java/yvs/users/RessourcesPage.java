/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.users.YvsResourcePageGroup;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class RessourcesPage implements Serializable {

    private Integer id;
    private String reference;
    private String libelle;
    private String description;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private PageModule pageModule = new PageModule();
    private YvsResourcePageGroup groupe = new YvsResourcePageGroup();

    private boolean acces;

    public RessourcesPage() {
    }

    public RessourcesPage(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public boolean isAcces() {
        return acces;
    }

    public void setAcces(boolean acces) {
        this.acces = acces;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public PageModule getPageModule() {
        return pageModule;
    }

    public void setPageModule(PageModule pageModule) {
        this.pageModule = pageModule;
    }

    public YvsResourcePageGroup getGroupe() {
        return groupe;
    }

    public void setGroupe(YvsResourcePageGroup groupe) {
        this.groupe = groupe;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
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
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final RessourcesPage other = (RessourcesPage) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
