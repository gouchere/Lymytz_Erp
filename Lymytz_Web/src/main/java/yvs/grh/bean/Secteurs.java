/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.util.Managed;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class Secteurs extends Managed<Secteurs, YvsBaseCaisse> implements Serializable {

    private int idSecteur;
    private String secteur, description;
    private List<Secteurs> listValue;
    private List<Categories> listCategorie;

    public Secteurs() {
        listValue = new ArrayList<>();
        listCategorie = new ArrayList<>();
    }

    public Secteurs(int idSecteur, String secteur, String description) {
        this.idSecteur = idSecteur;
        this.secteur = secteur;
        this.description = description;
        listValue = new ArrayList<>();
        listCategorie = new ArrayList<>();
    }

    public int getIdSecteur() {
        return idSecteur;
    }

    public void setIdSecteur(int idSecteur) {
        this.idSecteur = idSecteur;
    }

    public String getSecteur() {
        return secteur;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    public List<Secteurs> getListValue() {
        return listValue;
    }

    public void setListValue(List<Secteurs> listValue) {
        this.listValue = listValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Categories> getListCategorie() {
        return listCategorie;
    }

    public void setListCategorie(List<Categories> listCategorie) {
        this.listCategorie = listCategorie;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.idSecteur;
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
        final Secteurs other = (Secteurs) obj;
        if (this.idSecteur != other.idSecteur) {
            return false;
        }
        return true;
    }

    @Override
    public boolean controleFiche(Secteurs bean) {
        //s'assuerer qu'il n'existe pas deux secteur de même intitulé
        return true;
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Secteurs recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(Secteurs bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean saveNew() {
        if (controleFiche(this)) {
            listValue.add(new Secteurs(numero++, this.secteur, this.description));
            resetFiche(this);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
