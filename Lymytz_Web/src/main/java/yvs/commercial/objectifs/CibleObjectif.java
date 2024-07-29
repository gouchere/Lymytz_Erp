/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.objectifs;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.commercial.client.Client;
import yvs.parametrage.dico.Dictionnaire;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class CibleObjectif implements Serializable {

    private long id;
    private String tableExt;
    private String libelle;
    private long idExterne;
    private boolean activeCibleArticle;
    private boolean activeCibleClient;
    private boolean activeCibleZone;
    private Articles article = new Articles();
    private Conditionnement unite = new Conditionnement();
    private Client client = new Client();
    private Dictionnaire zone = new Dictionnaire();

    public CibleObjectif() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTableExt() {
        return tableExt;
    }

    public void setTableExt(String tableExt) {
        this.tableExt = tableExt;
    }

    public long getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(long idExterne) {
        this.idExterne = idExterne;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public Conditionnement getUnite() {
        return unite;
    }

    public void setUnite(Conditionnement unite) {
        this.unite = unite;
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

    public boolean isActiveCibleArticle() {
        return activeCibleArticle;
    }

    public void setActiveCibleArticle(boolean activeCibleArticle) {
        this.activeCibleArticle = activeCibleArticle;
    }

    public boolean isActiveCibleClient() {
        return activeCibleClient;
    }

    public void setActiveCibleClient(boolean activeCibleClient) {
        this.activeCibleClient = activeCibleClient;
    }

    public boolean isActiveCibleZone() {
        return activeCibleZone;
    }

    public void setActiveCibleZone(boolean activeCibleZone) {
        this.activeCibleZone = activeCibleZone;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final CibleObjectif other = (CibleObjectif) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
