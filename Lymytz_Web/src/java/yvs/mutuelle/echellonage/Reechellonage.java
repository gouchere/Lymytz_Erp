/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.echellonage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.mutuelle.echellonage.YvsMutMensualite;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Reechellonage implements Serializable {

    private long id;
    private Date dateReechellonage = new Date();
    private int nombreEcheance;
    private int dureeSuspension;
    private Date dateEcheance;
    private String typeReport = "T";
    private List<YvsMutMensualite> mensualites;
//    private double fraisReechellonage;
//    private String nature = "Avance";
//    private double dureeDemande;
//    private Echellonage remboursement;
//    private boolean selectActif, new_;

    public Reechellonage() {
        mensualites = new ArrayList<>();
    }

    public int getDureeSuspension() {
        return dureeSuspension;
    }

    public void setDureeSuspension(int dureeSuspension) {
        this.dureeSuspension = dureeSuspension;
    }

    public Reechellonage(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateReechellonage() {
        return dateReechellonage;
    }

    public void setDateReechellonage(Date dateReechellonage) {
        this.dateReechellonage = dateReechellonage;
    }

    public void setNombreEcheance(int nombreEcheance) {
        this.nombreEcheance = nombreEcheance;
    }

    public int getNombreEcheance() {
        return nombreEcheance;
    }

    public void setDateEcheance(Date dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public Date getDateEcheance() {
        return dateEcheance;
    }

    public String getTypeReport() {
        return typeReport != null ? typeReport.trim().length() > 0 ? typeReport : Constantes.MUT_TYPE_REPORT_PARTIEL : Constantes.MUT_TYPE_REPORT_PARTIEL;
    }

    public void setTypeReport(String typeReport) {
        this.typeReport = typeReport;
    }

    public List<YvsMutMensualite> getMensualites() {
        return mensualites;
    }

    public void setMensualites(List<YvsMutMensualite> mensualites) {
        this.mensualites = mensualites;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Reechellonage other = (Reechellonage) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
