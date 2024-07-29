/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.achat;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class LotReception implements Serializable {

    private long id;
    private Date dateFabrication = new Date();
    private Date dateExpiration = new Date();
    private String statut, numero;
    private boolean actif = true;
    private boolean selectActif, new_;
    private Date dateSave = new Date();
    private Articles article = new Articles();
    private boolean updateArticle = true;

    public LotReception() {

    }

    public LotReception(long id) {
        this();
        this.id = id;
    }

    public LotReception(long id, String numero) {
        this(id);
        this.numero = numero;
    }

    public LotReception(String numero, Articles article, Date dateFabrication, Date dateExpiration) {
        this(0, numero);
        this.article = article;
        this.dateFabrication = dateFabrication;
        this.dateExpiration = dateExpiration;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateFabrication() {
        return dateFabrication;
    }

    public void setDateFabrication(Date dateFabrication) {
        this.dateFabrication = dateFabrication;
    }

    public String getStatut() {
        return statut != null ? statut.trim().length() > 0 ? statut : "V" : "V";
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
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

    public boolean isUpdateArticle() {
        return updateArticle;
    }

    public void setUpdateArticle(boolean updateArticle) {
        this.updateArticle = updateArticle;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
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
        final LotReception other = (LotReception) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LotReception{" + "id=" + id + ", numero=" + numero + '}';
    }

}
