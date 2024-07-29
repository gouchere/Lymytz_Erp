/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.contrat;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import yvs.grh.bean.ContratEmploye;
import yvs.users.Users;

/**
 *
 * @author hp Elite 8300 modélise la suspension d'un contrat
 */
public class FinDeContrats implements Serializable {

    private long id;
    private String motif;
    private Date dateEffet = new Date();
    private Date dateSave;
    private double durrePreavis, anciennete;
    private String uniteDuree;
    private ContratEmploye contrat = new ContratEmploye();
    private Users author;
    private List<ElementIndemnite> indemnites;

    public FinDeContrats() {
        indemnites = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Date getDateEffet() {
        return dateEffet;
    }

    public void setDateEffet(Date dateEffet) {
        this.dateEffet = dateEffet;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public double getDurrePreavis() {
        return durrePreavis;
    }

    public void setDurrePreavis(double durrePreavis) {
        this.durrePreavis = durrePreavis;
    }

    public double getAnciennete() {
        return anciennete;
    }

    public void setAnciennete(double anciennete) {
        this.anciennete = anciennete;
    }

    public String getUniteDuree() {
        return uniteDuree;
    }

    public void setUniteDuree(String uniteDuree) {
        this.uniteDuree = uniteDuree;
    }

    public ContratEmploye getContrat() {
        return contrat;
    }

    public void setContrat(ContratEmploye contrat) {
        this.contrat = contrat;
    }

    public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    public List<ElementIndemnite> getIndemnites() {
        return indemnites;
    }

    public void setIndemnites(List<ElementIndemnite> indemnites) {
        this.indemnites = indemnites;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final FinDeContrats other = (FinDeContrats) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FinContrats{" + "id=" + id + '}';
    }

}
