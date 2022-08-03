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
import yvs.mutuelle.Mutuelle;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
//TypeCompte et TypeCaisse
public class TypeCompte implements Serializable {

    private long id;
    private String libelle;
    private String nature = "Salaire";//Salaire -- Credit -- Epargne
    private Mutuelle mutuelle = new Mutuelle();
    private Date dateSave = new Date();
    private boolean typeCaisse;
    private boolean selectActif, new_;

    public TypeCompte() {
    }

    public TypeCompte(long id) {
        this.id = id;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isTypeCaisse() {
        return typeCaisse;
    }

    public void setTypeCaisse(boolean typeCaisse) {
        this.typeCaisse = typeCaisse;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
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

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public Mutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(Mutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final TypeCompte other = (TypeCompte) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
