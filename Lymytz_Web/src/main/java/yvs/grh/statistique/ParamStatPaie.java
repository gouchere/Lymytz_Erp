/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.statistique;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.grh.paie.ElementSalaire;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ParamStatPaie implements Serializable {

    private long id;
    private Etats etat = new Etats();
    private List<ElementSalaire> elements;
    private List<GrilleDipe> grillesDipe;
    private List<TauxContribution> listTaux;

    public ParamStatPaie() {
        elements = new ArrayList<>();
        grillesDipe = new ArrayList<>();
        listTaux = new ArrayList<>();
    }

    public ParamStatPaie(Etats etat) {
        this.etat = etat;
        elements = new ArrayList<>();
        grillesDipe = new ArrayList<>();
        listTaux = new ArrayList<>();
    }

    public ParamStatPaie(long id, Etats etat) {
        this.id = id;
        this.etat = etat;
        elements = new ArrayList<>();
        grillesDipe = new ArrayList<>();
        listTaux = new ArrayList<>();
    }

    public Etats getEtat() {
        return etat;
    }

    public void setEtat(Etats etat) {
        this.etat = etat;
    }

    public List<ElementSalaire> getElements() {
        return elements;
    }

    public void setElements(List<ElementSalaire> elements) {
        this.elements = elements;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<GrilleDipe> getGrillesDipe() {
        return grillesDipe;
    }

    public void setGrillesDipe(List<GrilleDipe> grillesDipe) {
        this.grillesDipe = grillesDipe;
    }

    public List<TauxContribution> getListTaux() {
        return listTaux;
    }

    public void setListTaux(List<TauxContribution> listTaux) {
        this.listTaux = listTaux;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.etat);
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
        final ParamStatPaie other = (ParamStatPaie) obj;
        if (!Objects.equals(this.etat, other.etat)) {
            return false;
        }
        return true;
    }

    

}
