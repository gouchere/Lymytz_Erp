/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.dico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.param.YvsBaseTarifPointLivraison;
import yvs.entity.param.YvsDictionnaire;
import yvs.util.Constantes;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "dico")
@SessionScoped
public class Dictionnaire implements Serializable, Comparator<Dictionnaire> {

    private long id;
    private String libelle;
    private String titre = Constantes.T_PAYS;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private String abreviation;
    private Dictionnaire parent;
    private List<Dictionnaire> listFils;
    private List<YvsDictionnaire> fils;
    private List<YvsBaseTarifPointLivraison> tarifs;
    private boolean actif = true, update;
    private boolean select, list, error;

    public Dictionnaire() {
        listFils = new ArrayList<>();
        tarifs = new ArrayList<>();
        fils = new ArrayList<>();
    }

    public Dictionnaire(String titre) {
        this();
        this.libelle = titre;
    }

    public Dictionnaire(long id) {
        this();
        this.id = id;
    }

    public Dictionnaire(long id, String titre) {
        this(id);
        this.libelle = titre;
    }

    public Dictionnaire(long id, String libelle, String abreviation) {
        this(id, libelle);
        this.abreviation = abreviation;
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

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public Dictionnaire getParent() {
        return parent;
    }

    public void setParent(Dictionnaire parent) {
        this.parent = parent;
    }

    public List<Dictionnaire> getListFils() {
        return listFils;
    }

    public void setListFils(List<Dictionnaire> listFils) {
        this.listFils = listFils;
    }

    public List<YvsDictionnaire> getFils() {
        return fils;
    }

    public void setFils(List<YvsDictionnaire> fils) {
        this.fils = fils;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle != null ? libelle : "";
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getTitre() {
        return titre != null ? titre.trim().length() > 0 ? titre : Constantes.T_PAYS : Constantes.T_PAYS;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAbreviation() {
        String l = getLibelle().trim().length() < 4 ? getLibelle() : getLibelle().substring(0, 3);
        return abreviation != null ? abreviation.trim().length() > 0 ? abreviation : l : l;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public List<YvsBaseTarifPointLivraison> getTarifs() {
        return tarifs;
    }

    public void setTarifs(List<YvsBaseTarifPointLivraison> tarifs) {
        this.tarifs = tarifs;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Dictionnaire other = (Dictionnaire) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compare(Dictionnaire o1, Dictionnaire o2) {
        if (o1.getLibelle().compareTo(o2.getLibelle()) > 0) {
            return 1;
        } else if (o1.getLibelle().compareTo(o2.getLibelle()) < 0) {
            return -1;
        }
        return 0;
    }
}
