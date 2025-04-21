/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.contrat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import yvs.entity.grh.param.poste.YvsGrhModelPrimePoste;

/**
 *
 * @author Lymytz Dowes
 */
public class ModelContrat implements Serializable {

    private long id;
    private String intitule;
    private double salaireBaseHoraire;
    private double salaireBaseMensuel;
    private double congeAcquis;
    private double majorationConge;
    private List<YvsGrhModelPrimePoste> primes;
    private Preavis preavis = new Preavis();

    public ModelContrat() {
        primes = new ArrayList<>();
    }

    public ModelContrat(long id) {
        this.id = id;
        primes = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public double getSalaireBaseHoraire() {
        return salaireBaseHoraire;
    }

    public void setSalaireBaseHoraire(double salaireBaseHoraire) {
        this.salaireBaseHoraire = salaireBaseHoraire;
    }

    public double getSalaireBaseMensuel() {
        return salaireBaseMensuel;
    }

    public void setSalaireBaseMensuel(double salaireBaseMensuel) {
        this.salaireBaseMensuel = salaireBaseMensuel;
    }

    public double getCongeAcquis() {
        return congeAcquis;
    }

    public void setCongeAcquis(double congeAcquis) {
        this.congeAcquis = congeAcquis;
    }

    public double getMajorationConge() {
        return majorationConge;
    }

    public void setMajorationConge(double majorationConge) {
        this.majorationConge = majorationConge;
    }

    public List<YvsGrhModelPrimePoste> getPrimes() {
        return primes;
    }

    public void setPrimes(List<YvsGrhModelPrimePoste> primes) {
        this.primes = primes;
    }

    public Preavis getPreavis() {
        return preavis;
    }

    public void setPreavis(Preavis preavis) {
        this.preavis = preavis;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ModelContrat other = (ModelContrat) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
