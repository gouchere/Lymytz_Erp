/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.production.planification.view.DataLigne2D;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ObjectMrp implements Serializable {

    private long id;
    private Articles article;
    private DataLigne2D linePdp;
    private List<DataLigne2D> lineNomenclature;

    public ObjectMrp() {
        lineNomenclature = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DataLigne2D getLinePdp() {
        return linePdp;
    }

    public void setLinePdp(DataLigne2D linePdp) {
        this.linePdp = linePdp;
    }

    public List<DataLigne2D> getLineNomenclature() {
        return lineNomenclature;
    }

    public void setLineNomenclature(List<DataLigne2D> lineNomenclature) {
        this.lineNomenclature = lineNomenclature;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ObjectMrp other = (ObjectMrp) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
