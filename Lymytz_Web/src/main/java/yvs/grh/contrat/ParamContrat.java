/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.contrat;

import java.util.ArrayList;
import java.util.List;
import yvs.parametrage.agence.SecteurActivite;

/**
 *
 * @author hp Elite 8300
 */
public class ParamContrat {

    private long id;
    private int periodeReference;
    private String formulePreavis, formuleSalaireMoyen;
    private SecteurActivite secteur = new SecteurActivite();
    private double ancienneteRequise;
    private List<GrilleTauxFinContrat> grillesTaux;

    public ParamContrat() {
        grillesTaux = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPeriodeReference() {
        return periodeReference;
    }

    public void setPeriodeReference(int periodeReference) {
        this.periodeReference = periodeReference;
    }

    public String getFormulePreavis() {
        return formulePreavis;
    }

    public void setFormulePreavis(String formulePreavis) {
        this.formulePreavis = formulePreavis;
    }

    public String getFormuleSalaireMoyen() {
        return formuleSalaireMoyen;
    }

    public void setFormuleSalaireMoyen(String formuleSalaireMoyen) {
        this.formuleSalaireMoyen = formuleSalaireMoyen;
    }

    public SecteurActivite getSecteur() {
        return secteur;
    }

    public void setSecteur(SecteurActivite secteur) {
        this.secteur = secteur;
    }

    public double getAncienneteRequise() {
        return ancienneteRequise;
    }

    public void setAncienneteRequise(double ancienneteRequise) {
        this.ancienneteRequise = ancienneteRequise;
    }

    public List<GrilleTauxFinContrat> getGrillesTaux() {
        return grillesTaux;
    }

    public void setGrillesTaux(List<GrilleTauxFinContrat> grillesTaux) {
        this.grillesTaux = grillesTaux;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ParamContrat other = (ParamContrat) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
