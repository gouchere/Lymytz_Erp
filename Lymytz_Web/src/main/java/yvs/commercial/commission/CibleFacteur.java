/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.commission;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.commercial.client.CategorieClient;
import yvs.commercial.client.Client;
import yvs.parametrage.dico.Dictionnaire;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class CibleFacteur implements Serializable {

    private long id;
    private String tableExterne = Constantes.SCR_ARTICLES;
    private String libelle;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private long idExterne;
    private Articles article = new Articles();
    private Client client = new Client();
    private Dictionnaire zone = new Dictionnaire();
    private CategorieClient categorie = new CategorieClient();
    private FacteurTaux facteur = new FacteurTaux();

    public CibleFacteur() {
    }

    public CibleFacteur(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTableExterne() {
        return tableExterne;
    }

    public void setTableExterne(String tableExterne) {
        this.tableExterne = tableExterne;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public long getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(long idExterne) {
        this.idExterne = idExterne;
    }

    public FacteurTaux getFacteur() {
        return facteur;
    }

    public void setFacteur(FacteurTaux facteur) {
        this.facteur = facteur;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Dictionnaire getZone() {
        return zone;
    }

    public void setZone(Dictionnaire zone) {
        this.zone = zone;
    }

    public CategorieClient getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieClient categorie) {
        this.categorie = categorie;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final CibleFacteur other = (CibleFacteur) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
