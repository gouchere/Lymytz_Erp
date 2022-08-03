/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.CentreAnalytique;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ContentAnalytique implements Serializable {

    private long id;
    private double debit;
    private double credit;
    private double coefficient;
    private Date dateSasie = new Date();
    private Date dateSave = new Date();
    private ContentPieceCompta contenu = new ContentPieceCompta();
    private CentreAnalytique centre = new CentreAnalytique();

    public ContentAnalytique() {
    }

    public ContentAnalytique(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public Date getDateSasie() {
        return dateSasie != null ? dateSasie : new Date();
    }

    public void setDateSasie(Date dateSasie) {
        this.dateSasie = dateSasie;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public ContentPieceCompta getContenu() {
        return contenu;
    }

    public void setContenu(ContentPieceCompta contenu) {
        this.contenu = contenu;
    }

    public CentreAnalytique getCentre() {
        return centre;
    }

    public void setCentre(CentreAnalytique centre) {
        this.centre = centre;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ContentAnalytique other = (ContentAnalytique) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
