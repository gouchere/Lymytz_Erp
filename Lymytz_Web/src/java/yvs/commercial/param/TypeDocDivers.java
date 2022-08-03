/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.CodeAcces;
import yvs.entity.base.YvsBaseTypeDocCategorie;
import yvs.util.Constantes;
import yvs.util.Util;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class TypeDocDivers implements Serializable {

    private long id;
    private String libelle;
    private String description;
    private String module = Constantes.TYPE_OD;
    private boolean actif = true, canReception = true;
    private CodeAcces codeAcces = new CodeAcces();
    private Date dateSave = new Date();
    private List<YvsBaseTypeDocCategorie> categories;

    public TypeDocDivers() {
        this.categories = new ArrayList<>();
    }

    public TypeDocDivers(long id) {
        this();
        this.id = id;
    }

    public TypeDocDivers(long id, String libelle) {
        this(id);
        this.libelle = libelle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public CodeAcces getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(CodeAcces codeAcces) {
        this.codeAcces = codeAcces;
    }

    public String getModule() {
        return Util.asString(module) ? module : Constantes.TYPE_OD;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isCanReception() {
        return canReception;
    }

    public void setCanReception(boolean canReception) {
        this.canReception = canReception;
    }

    public List<YvsBaseTypeDocCategorie> getCategories() {
        return categories;
    }

    public void setCategories(List<YvsBaseTypeDocCategorie> categories) {
        this.categories = categories;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final TypeDocDivers other = (TypeDocDivers) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
