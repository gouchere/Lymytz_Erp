/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class FamilleArticle implements Serializable {

    private long id;
    private String reference;
    private String designation;
    private String description;
    private String prefixe;
    private boolean selectActif, actif = true;
    private FamilleArticle parentFamille;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private List<FamilleArticle> yvsProdFamilleArticleList;
    private List<Articles> articles;
    private boolean select, new_, update;

    public FamilleArticle() {
        yvsProdFamilleArticleList = new ArrayList<>();
        articles = new ArrayList<>();
    }

    public FamilleArticle(long id, String reference, String designation, String description) {
        this();
        this.id = id;
        this.reference = reference;
        this.designation = designation;
        this.description = description;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getPrefixe() {
        return prefixe;
    }

    public void setPrefixe(String prefixe) {
        this.prefixe = prefixe;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isUpdate() {
        return id > 0;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public List<Articles> getArticles() {
        return articles;
    }

    public void setArticles(List<Articles> articles) {
        this.articles = articles;
    }

    public FamilleArticle getParentFamille() {
        return parentFamille;
    }

    public void setParentFamille(FamilleArticle parentFamille) {
        this.parentFamille = parentFamille;
    }

    public FamilleArticle(long id) {
        this.id = id;
        yvsProdFamilleArticleList = new ArrayList<>();
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FamilleArticle> getYvsProdFamilleArticleList() {
        return yvsProdFamilleArticleList;
    }

    public void setYvsProdFamilleArticleList(List<FamilleArticle> yvsProdFamilleArticleList) {
        this.yvsProdFamilleArticleList = yvsProdFamilleArticleList;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final FamilleArticle other = (FamilleArticle) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
