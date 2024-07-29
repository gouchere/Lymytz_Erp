/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.salaire;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import yvs.mutuelle.base.PrimePoste;

/**
 *
 * @author hp Elite 8300 classe de trie des primes par dates ou par mutualiste
 */
public class OrderPrimes {

    private String periode;
    private List<PrimePoste> primes;
    private double montantTotal;

    public OrderPrimes() {
        primes = new ArrayList<>();
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public List<PrimePoste> getPrimes() {
        return primes;
    }

    public void setPrimes(List<PrimePoste> primes) {
        this.primes = primes;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.periode);
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
        final OrderPrimes other = (OrderPrimes) obj;
        if (!Objects.equals(this.periode, other.periode)) {
            return false;
        }
        return true;
    }

}
