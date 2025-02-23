/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.societe;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.YvsSocietesInfosSuppl;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@SessionScoped
@ManagedBean
public class InfosSupplSociete implements Serializable {

    private long id;
    private String titre;
    private String valeur;
    private String type;
    private Date dateSave = new Date();

    public InfosSupplSociete() {
    }

    public InfosSupplSociete(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getType() {
        return type;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final InfosSupplSociete other = (InfosSupplSociete) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public static InfosSupplSociete buildBean(YvsSocietesInfosSuppl e) {
        InfosSupplSociete b = new InfosSupplSociete();
        if (e != null) {
            b.setId(e.getId());
            b.setTitre(e.getTitre());
            b.setValeur(e.getValeur());
            b.setType(e.getType());
            b.setDateSave(e.getDateSave());
        }
        return b;
    }

    public static YvsSocietesInfosSuppl buildEntity(InfosSupplSociete b, YvsSocietes s, YvsUsersAgence a) {
        YvsSocietesInfosSuppl e = new YvsSocietesInfosSuppl();
        if (b != null) {
            e.setId(b.getId());
            e.setDateSave(b.getDateSave());
            b.setId(e.getId());
            b.setTitre(e.getTitre());
            b.setValeur(e.getValeur());
            b.setType(e.getType());
            b.setDateSave(e.getDateSave());
            e.setDateUpdate(new Date());
            e.setAuthor(a);
            e.setSociete(s);
        }
        return e;
    }
}
