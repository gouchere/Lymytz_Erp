/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.vente;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.rrr.Ristourne;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class RistourneDocVente implements Serializable {

    private long id;
    private Ristourne ristourne = new Ristourne();
    private DocVente docVente = new DocVente();
    private double montant = 0;
    private boolean selectActif, new_, update;

    public RistourneDocVente() {
    }

    public RistourneDocVente(long id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Ristourne getRistourne() {
        return ristourne;
    }

    public void setRistourne(Ristourne remise) {
        this.ristourne = remise;
    }

    public DocVente getDocVente() {
        return docVente;
    }

    public void setDocVente(DocVente docVente) {
        this.docVente = docVente;
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
        final RistourneDocVente other = (RistourneDocVente) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
