/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.print.contrat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.param.YvsSocietes;
import yvs.entity.print.YvsPrintContratEmployeArticle;
import yvs.entity.print.YvsPrintContratEmployeHeader;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class PrintContratHeader implements Serializable {

    private long id;
    private String nom;
    private String titre;
    private String model = "contrat_employe";
    private boolean defaut;
    private String partieSociete;
    private String partiePrestataire;
    private String preambule;
    private String definition;
    private Date dateSave = new Date();
    private List<YvsPrintContratEmployeArticle> articles;

    public PrintContratHeader() {
        this.articles = new ArrayList<>();
    }

    public PrintContratHeader(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isDefaut() {
        return defaut;
    }

    public void setDefaut(boolean defaut) {
        this.defaut = defaut;
    }

    public String getPartieSociete() {
        return partieSociete;
    }

    public void setPartieSociete(String partieSociete) {
        this.partieSociete = partieSociete;
    }

    public String getPartiePrestataire() {
        return partiePrestataire;
    }

    public void setPartiePrestataire(String partiePrestataire) {
        this.partiePrestataire = partiePrestataire;
    }

    public String getPreambule() {
        return preambule;
    }

    public void setPreambule(String preambule) {
        this.preambule = preambule;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public List<YvsPrintContratEmployeArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsPrintContratEmployeArticle> articles) {
        this.articles = articles;
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
        final PrintContratHeader other = (PrintContratHeader) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public static PrintContratHeader buildBean(YvsPrintContratEmployeHeader e) {
        PrintContratHeader r = new PrintContratHeader();
        if (e != null) {
            r.setId(e.getId());
            r.setDateSave(e.getDateSave());
            r.setDefaut(e.getDefaut());
            r.setDefinition(e.getDefinition());
            r.setModel(e.getModel());
            r.setTitre(e.getTitre());
            r.setNom(e.getNom());
            r.setPartiePrestataire(e.getPartiePrestataire());
            r.setPartieSociete(e.getPartieSociete());
            r.setPreambule(e.getPreambule());
        }
        return r;
    }

    public static YvsPrintContratEmployeHeader buildEntity(PrintContratHeader e, YvsSocietes s, YvsUsersAgence ua) {
        YvsPrintContratEmployeHeader r = new YvsPrintContratEmployeHeader();
        if (e != null) {
            r.setId(e.getId());
            r.setDateSave(e.getDateSave());
            r.setDefaut(e.isDefaut());
            r.setDefinition(e.getDefinition());
            r.setModel(e.getModel());
            r.setTitre(e.getTitre());
            r.setNom(e.getNom());
            r.setPartiePrestataire(e.getPartiePrestataire());
            r.setPartieSociete(e.getPartieSociete());
            r.setPreambule(e.getPreambule());
            r.setDateSave(new Date());
            r.setAuthor(ua);
            r.setSociete(s);
        }
        return r;
    }
}
