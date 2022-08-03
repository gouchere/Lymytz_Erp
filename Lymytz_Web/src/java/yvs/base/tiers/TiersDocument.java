/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.tiers;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz-pc
 */
@ManagedBean
@SessionScoped
public class TiersDocument implements Serializable {

    private long id;
    private String titre;
    private String fichier;
    private String extension;
    private String numero;
    private String type;
    private Date dateSave = new Date();
    private Tiers tiers = new Tiers();

    public TiersDocument() {
    }

    public TiersDocument(long id) {
        this.id = id;
    }

    public TiersDocument(long id, String titre, String fichier, String extension) {
        this.id = id;
        this.titre = titre;
        this.fichier = fichier;
        this.extension = extension;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre != null ? titre.trim().length() > 0 ? titre : Constantes.FILE_TIERS_NUI_NAME : Constantes.FILE_TIERS_NUI_NAME;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getType() {
        return type != null ? type.trim().length() > 0 ? type : Constantes.FILE_TIERS_NUI : Constantes.FILE_TIERS_NUI;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Tiers getTiers() {
        return tiers;
    }

    public void setTiers(Tiers tiers) {
        this.tiers = tiers;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final TiersDocument other = (TiersDocument) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TiersDocument{" + "id=" + id + '}';
    }

}
