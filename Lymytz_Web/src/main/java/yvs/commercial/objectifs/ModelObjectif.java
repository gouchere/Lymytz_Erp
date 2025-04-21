/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.objectifs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.commercial.objectifs.YvsComCibleObjectif;
import yvs.entity.commercial.objectifs.YvsComObjectifsAgence;
import yvs.entity.commercial.objectifs.YvsComObjectifsComercial;
import yvs.entity.commercial.objectifs.YvsComObjectifsPointVente;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ModelObjectif implements Serializable {

    private long id;
    private String indicateur;
    private String titre;
    private String codeid;
    private boolean actif;
    private List<YvsComCibleObjectif> ciblesObjectifs;
    private Date dateSave, dateUpdate;
    private String author;

    private List<YvsComObjectifsComercial> objectifsPeriodes;    
    private List<YvsComObjectifsAgence> objectifsPeriodesAgs;    
    private List<YvsComObjectifsPointVente> objectifsPeriodesPoint;    

    public ModelObjectif() {
        ciblesObjectifs = new ArrayList<>();
        objectifsPeriodes = new ArrayList<>();
        objectifsPeriodesAgs = new ArrayList<>();
        objectifsPeriodesPoint = new ArrayList<>();
    }

    public ModelObjectif(long id, String indicateur, String titre, String codeid) {
        this();
        this.id = id;
        this.indicateur = indicateur;
        this.titre = titre;
        this.codeid = codeid;
    }

    public List<YvsComObjectifsAgence> getObjectifsPeriodesAgs() {
        return objectifsPeriodesAgs;
    }

    public void setObjectifsPeriodesAgs(List<YvsComObjectifsAgence> objectifsPeriodesAgs) {
        this.objectifsPeriodesAgs = objectifsPeriodesAgs;
    }

    public List<YvsComObjectifsPointVente> getObjectifsPeriodesPoint() {
        return objectifsPeriodesPoint;
    }

    public void setObjectifsPeriodesPoint(List<YvsComObjectifsPointVente> objectifsPeriodesPoint) {
        this.objectifsPeriodesPoint = objectifsPeriodesPoint;
    }

    public List<YvsComCibleObjectif> getCiblesObjectifs() {
        return ciblesObjectifs;
    }

    public void setCiblesObjectifs(List<YvsComCibleObjectif> ciblesObjectifs) {
        this.ciblesObjectifs = ciblesObjectifs;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIndicateur() {
        return indicateur;
    }

    public void setIndicateur(String indicateur) {
        this.indicateur = indicateur;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getCodeid() {
        return codeid;
    }

    public void setCodeid(String codeid) {
        this.codeid = codeid;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<YvsComObjectifsComercial> getObjectifsPeriodes() {
        return objectifsPeriodes;
    }

    public void setObjectifsPeriodes(List<YvsComObjectifsComercial> objectifsPeriodes) {
        this.objectifsPeriodes = objectifsPeriodes;
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
        final ModelObjectif other = (ModelObjectif) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
