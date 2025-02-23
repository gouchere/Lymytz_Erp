/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.print.conge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.param.YvsSocietes;
import yvs.entity.print.YvsPrintDecisionCongeArticle;
import yvs.entity.print.YvsPrintDecisionCongeHeader;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class PrintDecisionCongeHeader implements Serializable {

    private long id;
    private String nom;
    private String titre;
    private String model = "decision_conge";
    private boolean defaut;
    private String introduction;
    private String definitionConventive;
    private Date dateSave = new Date();
    private List<YvsPrintDecisionCongeArticle> articles;

    public PrintDecisionCongeHeader() {
        this.articles = new ArrayList<>();
    }

    public PrintDecisionCongeHeader(long id) {
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getDefinitionConventive() {
        return definitionConventive;
    }

    public void setDefinitionConventive(String definitionConventive) {
        this.definitionConventive = definitionConventive;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public List<YvsPrintDecisionCongeArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsPrintDecisionCongeArticle> articles) {
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
        final PrintDecisionCongeHeader other = (PrintDecisionCongeHeader) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public static PrintDecisionCongeHeader buildBean(YvsPrintDecisionCongeHeader e) {
        PrintDecisionCongeHeader r = new PrintDecisionCongeHeader();
        if (e != null) {
            r.setId(e.getId());
            r.setDateSave(e.getDateSave());
            r.setDefaut(e.getDefaut());
            r.setDefinitionConventive(e.getDefinitionConventive());
            r.setModel(e.getModel());
            r.setTitre(e.getTitre());
            r.setNom(e.getNom());
            r.setIntroduction(e.getIntroduction());
        }
        return r;
    }

    public static YvsPrintDecisionCongeHeader buildEntity(PrintDecisionCongeHeader e, YvsSocietes s, YvsUsersAgence ua) {
        YvsPrintDecisionCongeHeader r = new YvsPrintDecisionCongeHeader();
        if (e != null) {
            r.setId(e.getId());
            r.setDateSave(e.getDateSave());
            r.setDefaut(e.isDefaut());
            r.setDefinitionConventive(e.getDefinitionConventive());
            r.setModel(e.getModel());
            r.setTitre(e.getTitre());
            r.setNom(e.getNom());
            r.setIntroduction(e.getIntroduction());
            r.setDateSave(new Date());
            r.setAuthor(ua);
            r.setSociete(s);
        }
        return r;
    }
}
