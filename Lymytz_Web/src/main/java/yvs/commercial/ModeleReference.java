/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz-Invité
 */
@ManagedBean
@SessionScoped
public class ModeleReference implements Serializable {

    private long id;
    private String prefix = new String();
    private String codePointvente;
    private String elementCode;
    private boolean codePoint;
    private int longueurCodePoint;
    private boolean jour;
    private boolean mois;
    private boolean annee;
    private int taille;
    private char separateur = '-';
    private ElementReference element = new ElementReference();
    private boolean selectActif, new_, update;
    private String apercu, module;
    private Date dateSave = new Date();

    public ModeleReference() {
    }

    public ModeleReference(long id) {
        this.id = id;
    }

    public String getModule() {
        return module != null ? module.trim().length() > 0 ? module : Constantes.MOD_COM : Constantes.MOD_COM;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getCodePointvente() {
        return codePointvente != null ? codePointvente : "";
    }

    public void setCodePointvente(String codePointvente) {
        this.codePointvente = codePointvente;
    }

    public boolean isCodePoint() {
        return codePoint;
    }

    public void setCodePoint(boolean codePoint) {
        this.codePoint = codePoint;
    }

    public int getLongueurCodePoint() {
        return longueurCodePoint;
    }

    public void setLongueurCodePoint(int longueurCodePoint) {
        this.longueurCodePoint = longueurCodePoint;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getApercu() {
        return apercu;
    }

    public void setApercu(String apercu) {
        this.apercu = apercu;
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

    public ElementReference getElement() {
        return element;
    }

    public void setElement(ElementReference element) {
        this.element = element;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isJour() {
        return jour;
    }

    public void setJour(boolean jour) {
        this.jour = jour;
    }

    public boolean isMois() {
        return mois;
    }

    public void setMois(boolean mois) {
        this.mois = mois;
    }

    public boolean isAnnee() {
        return annee;
    }

    public void setAnnee(boolean annee) {
        this.annee = annee;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public char getSeparateur() {
        return separateur;
    }

    public void setSeparateur(char separateur) {
        this.separateur = separateur;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getElementCode() {
        return elementCode != null ? elementCode : "";
    }

    public void setElementCode(String elementCode) {
        this.elementCode = elementCode;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final ModeleReference other = (ModeleReference) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
