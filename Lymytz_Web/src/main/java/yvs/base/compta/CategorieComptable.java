/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.BeanDeBase;
import yvs.base.produits.ArticlesCatComptable;
import yvs.entity.param.YvsSocietes;
import yvs.util.Constantes;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class CategorieComptable extends BeanDeBase implements Serializable {

    private long id;
    private String codeCategorie;
    private String nature = Constantes.ACHAT;
    private String codeAppel;
    private String designation;
    private boolean selectActif, new_, update;
    private boolean venteOnline, error;
    private Date dateSave = new Date();
    private List<ArticlesCatComptable> taxes;

    public CategorieComptable() {
    }

    public CategorieComptable(long id) {
        this.id = id;
    }

    public CategorieComptable(long id, String codeCategorie, String nature) {
        this.id = id;
        this.codeCategorie = codeCategorie;
        this.nature = nature;
    }

    public CategorieComptable(String codeCat, String nature) {
        this.codeCategorie = codeCat;
        this.nature = nature;
    }

    public boolean isVenteOnline() {
        return venteOnline;
    }

    public void setVenteOnline(boolean venteOnline) {
        this.venteOnline = venteOnline;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCodeCategorie() {
        return codeCategorie;
    }

    public void setCodeCategorie(String codeCategorie) {
        this.codeCategorie = codeCategorie;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodeAppel() {
        return codeAppel;
    }

    public void setCodeAppel(String codeAppel) {
        this.codeAppel = codeAppel;
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

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ArticlesCatComptable> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<ArticlesCatComptable> taxes) {
        this.taxes = taxes;
    }

    public List<CategorieComptable> loadListCatC(YvsSocietes societe, String type) {
        String[] ch = {"societe", "type"};
        Object[] val = {societe, type};
        List<Object[]> l = dao.loadListTableByNameQueries("YvsCatcompta.findPartialAll", ch, val);
        CategorieComptable fseur = new CategorieComptable();
        List<CategorieComptable> lf = new ArrayList<>();
        for (Object[] tab : l) {
            fseur.setId((long) tab[0]);
            fseur.setCodeCategorie((String) tab[1]);
            fseur.setCodeAppel((String) tab[2]);
            lf.add(fseur);
            fseur = new CategorieComptable();
        }
        return lf;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CategorieComptable other = (CategorieComptable) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
